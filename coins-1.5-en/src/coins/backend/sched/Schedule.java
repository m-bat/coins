/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.sched;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import coins.driver.CoinsOptions;
import coins.driver.CompileSpecification;
import coins.backend.Function;
import coins.backend.Data;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.gen.CodeGenerator;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;

/**
 * Instruction scheduler for before and after register allocation.
 */
public class Schedule {

  private static class TriggerB implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new Schedule()).schedule(func, "Before");
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "ScheduleBefore"; }

    public String subject() {
      return "Instruction Scheduling (before register allocation)";
    }
  }

  /** Trigger class singleton. **/
  public static final TriggerB before = new TriggerB();

  private static class TriggerA implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new Schedule()).schedule(func, "After");
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "ScheduleAfter"; }

    public String subject() {
      return "Instruction Scheduling (after register allocation)";
    }
  }

  /** Trigger class singleton. **/
  public static final TriggerA after = new TriggerA();

  public static void attach(CompileSpecification spec, Root root) {
	//root.addHook("+AfterToMachineCode", after);
	CoinsOptions coinsOptions = spec.getCoinsOptions();
    if (coinsOptions.isSet("schedule") || coinsOptions.isSet("pipelining") || coinsOptions.isSet("schedule-after")) {
      // root.addHook("+AfterFirstInstSel", before); // do-nothing
    }
    else {
    	root.addHook("+AfterToMachineCode", after);
    }
  }
  
  public static void attachScheduleBefore(Root root){
	  root.addHook("+AfterFirstInstSel", before);
  }
  public static void attachScheduleAfter(Root root){
	  root.addHook("+AfterToMachineCode", after);
  }

  static final int MAX_LATENCY = 1000;
  Root root;
  CodeGenerator codeGen;
  Function func;
  LirFactory lir;
  PrintWriter debOut;
  boolean isX86;
  Pipelining pipe = null;
  boolean isPipelining = false;

  /**
   * Constructor for scheduling after register allocation.
   */
  public Schedule(){}

  /**
   * Instruction scheduler before/after register allocation
   */
  public void schedule(Function f, String mode) {
    func = f;
    lir = f.newLir;
    root = f.root;
    debOut = root.debOut;
    codeGen = func.module.targetMachine.getTargetCG();
    CoinsOptions coinsOptions = root.spec.getCoinsOptions();
    isX86 = coinsOptions.getArg("target-arch").equals("x86");
    FlowGraph flowGraph = func.flowGraph();

    if (coinsOptions.isSet("pipelining0") && mode == "After"){
    		if (pipe == null)
			pipe = new Pipelining(this);
    		codeGen.prepareCodeInfo(func);
    		pipe.pipelining0(flowGraph);
    }
    
    if (coinsOptions.isSet("pipelining")  && mode == "Before"){
    		isPipelining = true;
    		if (pipe == null)
    			pipe = new Pipelining(this);
    }

    codeGen.prepareCodeInfo(func);
    
    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();

      // Make dependency graph.
      DependGraph dg = new DependGraph(func);
      BiList instrList = new BiList();
      boolean hasPARALLEL = false;
      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();
        if(ins.opCode == Op.LINE || ins.opCode == Op.INFO){
        	 instrList.add(ins);
          continue;
        }
        DependNode dn = new DependNode(ins, this);
        dg.add(dn);

        switch (ins.opCode) {
        case Op.PROLOGUE:
          dn.setLatency(MAX_LATENCY);// to be scheduled at first
          break;
        case Op.EPILOGUE:
          break;
        case Op.PARALLEL:
      	  hasPARALLEL = true;
      	  if (root.traceOK("TMD", 1)) {
              debOut.println("\nDependent graph of a segment before scheduling\n");
              debOut.println(dg);
          }
      	  BiList instrListSeg
		        = mode == "After" ? dg.scheduleInst() : dg.scheduleLir();
		  if (root.traceOK("TMD", 1)) {
		        debOut.println("\nList of Lir nodes after scheduling\n");
		        debOut.println(instrListSeg);
		  }
		  instrList.addAll(instrListSeg);
		  dg.newSegment();
          break;
        case Op.JUMP:
        case Op.JUMPC:
          dg.hasBranch(dn);
        default:
/*          if (ins.opCode == Op.PARALLEL && ins.kid(0).opCode == Op.ASM){
              dn.setLatency(LirNodeInf.ASM_LATENCY);
              break;
          }*/
          ImList info = codeGen.codeInfo(ins);// generated codes with attached cost
          dn.setLatency(((Integer)info.elem2nd()).intValue());// cost of the codes
          dn.setMachineCodeSize(((Integer)info.elem3rd()).intValue());
          if (((Boolean)info.elem()).booleanValue())
            dn.letHaveDelaySlot();
          break;
        }
 /*       if (ins.opCode == Op.PARALLEL && ins.kid(0).opCode == Op.CALL){
        		hasPARALLEL = true;
        	    if (root.traceOK("TMD", 1)) {
                debOut.println("\nDependent graph of a segment before scheduling\n");
                debOut.println(dg);
             }
        	     BiList instrListSeg
		        = mode == "After" ? dg.scheduleInst() : dg.scheduleLir();
		     if (root.traceOK("TMD", 1)) {
		        debOut.println("\nList of Lir nodes after scheduling\n");
		        debOut.println(instrListSeg);
		     }
		     instrList.addAll(instrListSeg);
		     dg.newSegment();
         }*/
      }

      if (root.traceOK("TMD", 1)) {
        debOut.println("\nDependent graph of a basic block before scheduling\n");
        debOut.println(dg);
      }
      
      if (isPipelining && ! hasPARALLEL){
    	  	BiList succ = blk.succList();
    	  	if (succ.contains(blk)) {
    	  		if (pipe.pipelining(flowGraph, blk, dg, instrList))
    	  			continue;
    	  		else{
    	  			dg = pipe.reconstructDg(blk);
    	  		}
    	  	}
      }

      // Reorder instructions.
      BiList instrListSeg
        = mode == "After" ? dg.scheduleInst() : dg.scheduleLir();
      if (root.traceOK("TMD", 1)) {
        debOut.println("\nList of Lir nodes after scheduling\n");
        debOut.println(instrListSeg);
      }
      instrList.addAll(instrListSeg);
      blk.setInstrList(instrList);
    }

    func.touch();
  }

}


