/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.regalo;

import java.util.Vector;

import coins.backend.CantHappenException;
import coins.backend.CollectVarInTree;
import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalAnalyzer;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.Storage;
import coins.backend.TargetMachine;
import coins.backend.ana.EnumRegVars;
import coins.backend.ana.InterferenceGraph;
import coins.backend.ana.LiveVariableSlotwise;
import coins.backend.ana.LiveVariableAnalysis;
import coins.backend.ana.LoopAnalysis;
import coins.backend.cfg.BasicBlk;
import coins.backend.MachineParams;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.PickUpVariable;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.BitMapSet;
import coins.backend.util.HashNumberSet;
import coins.backend.util.ImList;
import coins.backend.util.NumberSet;
import coins.backend.util.UnionFind;
import coins.backend.util.VectorSet;


/** Register allocation session object. **/
public class OptimisticCoalescingAllocation {


	
  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      return (new OptimisticCoalescingAllocation()).doIt(func);
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "OptimisticCoalescingAllocation"; }

    public String subject() { return "Optimistic Coalescing Allocation"; }
    
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();


  private Function func;
  private LirFactory newLir;
  private MachineParams machine;

  private Root root;

  // Basic analyses information
  private LoopAnalysis loopInfo;
  private EnumRegVars rn;
  private LiveVariableAnalysis liveInfo;
  private InterferenceGraph idg;


  // For coalescing
  private UnionFind ufo;
  private int[] name;

  // Physical registers
  private Symbol[] regvars;
  private int nRegvars;
  private int nPhyRegs;

  // Dataset for each variables
  private int[] regset;
  private int[] navail;
  private int[] assignedReg;

  private int[] degree;
  private int[] spillCost;
  
  // new DataSet
  private int[] savedSpillCost;
  //private InterferenceGraph originalIdg;
  private boolean[] isParent;
  private int[] originalRegset;
  private BitMapSet SplitColors;
  private HashNumberSet[] originalList;
  private HashNumberSet[] adjList;

  private boolean[] dontspill;

  private int[] status;
  static final int VAR_WORKING = 1;
  static final int VAR_ONSTACK = 2;
  static final int VAR_COALESCED = 3;
  static final int VAR_SPILLED = 4;
  static final int VAR_ASSIGNED = 5;

  // Workign lists/stack
  private int[] toBeAssigned;
  private int stackp;
  private BiList spilledVars;
  private BiList assignedVars;
  private BiList working;
  private BiList copyPairs;
  
  private BiList copyPairs2;
  
  //count the number of spill,coalescing,undoCoalescing
  private int coaNum, spillNum, undoCoaNum;
  private int spi, spi2;
  private int intry;
  private int[] ingredient;
  
  
  private static int sam1 = 0; 
  private static int sam2 = 0;
  private static int sam3 = 0;
  
 
  public static int GetSpill(){
	  return sam1;
  }
  public static int GetCoalesce(){
	  return sam2;
  }
  public static int GetUndocoa(){
	  return sam3;
  }


  /** Copy pair **/
  static class CopyPair {
    public final int x, y;

    public CopyPair(int x, int y) { this.x = x; this.y = y; }
  }


  /** Construct register allocation object. **/
  /*
  private OptimisticCoalescingAllocation(Function func) {
    this.func = func;
    newLir = func.newLir;
    this.machine = func.module.targetMachine.machineParams;
    root = func.root;
  }
  */

  /** Do register allocation, return true if successful. **/
  /*
  public static boolean allocate(Function func) {
    return new OptimisticCoalescingAllocation(func).doIt();
  }
  */
  /** Do register allocation
   *   and return true if code unchanged (no need to spill). **/
  private boolean doIt(Function f) {
    // Prepare datasets and working lists.
    build(f);
    toBeAssigned = new int[nRegvars];
    stackp = 0;
    
    // try to coalesce if possible
    //  or try to simplify if possible
    //   or choose spill candidate if possible
    //    stop otherwise.
    while (coalesce())
      ;
    
    while (simplify()|| potentialSpill())
      ;
    
    
    if (!working.isEmpty()) {
      if (root.traceOK("OptimisticCoalescingAllocation", 2)) {
        root.debOut.println("worklist not empty, left:");
        for (BiLink p = working.first(); !p.atEnd(); p = p.next()) {
          int v = rn.index((Symbol)p.elem());

          root.debOut.println(rn.toString(v) + ": "
                              + " degree=" + degree[v]
                              + " avail=" + navail[v]
                              + " distf=" + idg.disturbedFactor(v)
                              + " spillcost=" + spillCost[v]);
        }
      }
      throw new CantHappenException("working list not empty");
    }

    if (false && root.traceOK("OptimisticCoalescingAllocation", 2)) {
      root.debOut.println("Interference Graph:");
      idg.printAfterFunction(root.debOut);
    }

    // Pop up variables from toBeAssigned stack and assign registers.
    spilledVars = new BiList();
    assignedVars = new BiList();
    assignRegisters();
    
    /*System.out.println("spi = "+spi);
    System.out.println("spi2 = "+spi2);
    System.out.println();
    System.out.println("intry = "+intry);
    System.out.println("spill = "+spill);
    System.out.println("undo-coa = "+undoCoa);*/
    

    if (spilledVars.isEmpty()) {
      // Replace virtual registers with physical registers.
      replaceRegisters();
      
      //***after allcate and no spill, set the field of spill,coalescing numbers
      sam1 = spillNum;
      sam2 = coaNum;
      sam3 = undoCoaNum;

      if (root.traceOK("OptimisticCoalescingAllocation", 1)) {
        root.debOut.println();
        root.debOut.println("After OptimisticCoalescingAllocation (success):");
        func.printIt(root.debOut);
      }
      /*
      if (root.dispIntervalTime)
        root.debOut.println(" Coloring: " + root.timer.getIntervalTime());
      */
      return true;
    } else {
      // Rewrite references of spilled variables
      //replaceRegisters();
      rewrite();
      sam1 = spillNum;

      if (root.traceOK("OptimisticCoalescingAllocation", 1)) {
        root.debOut.println();
        root.debOut.println("After OptimisticCoalescingAllocation (fail):");
        func.printIt(root.debOut);
      }
      /*    
      if (root.dispIntervalTime)
        root.debOut.println(" Coloring: " + root.timer.getIntervalTime());
      */

      return false;
    }
  }



  /** Initialization for coloring procedure. **/
  private void build(Function f) {
		func = f;
		newLir = func.newLir;
		this.machine = func.module.targetMachine.machineParams;
		root = func.root;

		// Loop Analysis.
		loopInfo = (LoopAnalysis) func.require(LoopAnalysis.analyzer);
		// Register Numbering
		rn = (EnumRegVars) func.require(EnumRegVars.analyzer);

		liveInfo = (LiveVariableAnalysis) func
				.require(LiveVariableSlotwise.analyzer);
		// Create interference graph.
		idg = (InterferenceGraph) func
				.require(InterferenceGraph.analyzerCopyNotInterfere);

		
		if (root.traceOK("OptimisticCoalescingAllocation", 2)) {
			root.debOut.println();
			root.debOut.println("After live var. analysis/interference graph:");
			func.printIt(root.debOut, new LocalAnalyzer[] {
					LiveVariableSlotwise.analyzer,
					InterferenceGraph.analyzerCopyNotInterfere });
		}

		nRegvars = rn.nRegvars();
		nPhyRegs = rn.nPhyRegs();

		name = new int[nRegvars];
		ufo = new UnionFind(nRegvars);

		status = new int[nRegvars];

		working = new BiList();

		isParent = new boolean[nRegvars];

		degree = new int[nRegvars];
		regset = new int[nRegvars];
		navail = new int[nRegvars];
		assignedReg = new int[nRegvars];
		dontspill = new boolean[nRegvars];
		//plus code of mine****************************************
		originalRegset = new int[nRegvars];
		adjList = new HashNumberSet[nRegvars];
		originalList = new HashNumberSet[nRegvars];

		ingredient = new int[nRegvars];
		
		//set static counters**********************************************
		coaNum = 0;
	    spillNum = 0;
	    undoCoaNum = 0;
	    //*********************************************************
		spi = 0;
		spi2 = 0;
		intry = 0;
		//seted counters**********************************************
		
		
		/*System.out.println("(nRegVars, nPhyRegs) = (" + nRegvars + ","
				+ nPhyRegs + ")");*/

		// change!!
		for (int i = 1; i < nRegvars; i++) {
			// Add non-physical register variables to working list.
			if (i < nPhyRegs) {
				status[i] = VAR_WORKING;

				// working.add(rn.toSymbol(i));

			} else {
				working.add(rn.toSymbol(i));
				status[i] = VAR_WORKING;
			}
			name[i] = i;
			isParent[i] = false;
			ingredient[i] = 1;
		}

		// Physical registers
		for (int i = 1; i < nPhyRegs; i++) {
			assignedReg[i] = i;
			regset[i] = i;
			navail[i] = 1;
		}

		for (BiLink p = working.first(); !p.atEnd(); p = p.next()) {
			int v = rn.index((Symbol) p.elem());

			// regset[v]: register set of v
			// navail[v]: number of choices
			assignedReg[v] = 0;
			regset[v] = getRegSet(rn.toSymbol(v));
			navail[v] = machine.nAvail(regset[v]);
			dontspill[v] = isDontspill(rn.toSymbol(v));
		}

		// degree: number of neighbors in IG
		for (int i = 1; i < nRegvars; i++) {
			degree[i] = 0;
			for (NumberSet.Iterator p = idg.interfereSet(i).iterator(); p
					.hasNext();) {
				int t = p.next();
				int n = machine.igWeight(regset[i], regset[t]);
				if (n == 0 || machine.covered(t, idg.interfereSet(i)))
					idg.unsetInterfere(i, t);
				else
					degree[i] += n;
			}
		}
		// copy originalList & adjList
		for (int i = 1; i < nRegvars; i++) {
			adjList[i] = new HashNumberSet(nRegvars);
			originalList[i] = new HashNumberSet(nRegvars);
			//copy the IGset to the adj,original LIST;
			adjList[i].copy(idg.interfereSet(i));
			originalList[i].copy(idg.interfereSet(i));
		}
		// Pick up copy pairs & compute spill cost

		copyPairs = new BiList();
		copyPairs2 = new BiList();
		
		spillCost = new int[rn.nRegvars()];
		savedSpillCost = new int[rn.nRegvars()];

		NumberSet work = new VectorSet(nRegvars);

		CollectVarInTree cv = new CollectVarInTree(func);

		for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd(); p = p
				.next()) {
			BasicBlk blk = (BasicBlk) p.elem();

			// add 8^loopDepth
			int loopFactor = (1 << (loopInfo.nestLevel[blk.id] * 3));

			for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
				LirNode ins = (LirNode) q.elem();

				if (ins.opCode == Op.SET) {
					int dst = rn.index(ins.kid(0));
					int src = rn.index(ins.kid(1));
					if (dst != 0 && src != 0)
						addCopyPair(dst, src);
				} else if (ins.opCode == Op.PARALLEL) {
					int n = ins.nKids();
					for (int i = 0; i < n; i++) {
						if (ins.kid(i).opCode == Op.SET) {
							int dst = rn.index(ins.kid(i).kid(0));
							int src = rn.index(ins.kid(i).kid(1));
							if (dst != 0 && src != 0)
								addCopyPair(dst, src);
							// addCopyPair(dst, src); // bug?
						}
					}
				}
				if (ins.opCode != Op.PHI) {
					cv.getUseVars(ins, work);
					for (NumberSet.Iterator it = work.iterator(); it.hasNext();)
						spillCost[it.next()] += loopFactor;
					cv.getDefVars(ins, work);
					for (NumberSet.Iterator it = work.iterator(); it.hasNext();)
						spillCost[it.next()] += loopFactor;
				}
			}
		}
		

		
		CopyArray(savedSpillCost, spillCost);
		CopyArray(originalRegset, regset);

		if (root.traceOK("OptimisticCoalescingAllocation", 2)) {
			root.debOut.print("copy pairs: ");
			for (BiLink p = copyPairs.first(); !p.atEnd(); p = p.next()) {
				CopyPair pair = (CopyPair) p.elem();
				root.debOut.println("  " + rn.toString(pair.x) + "="
						+ rn.toString(pair.y)
						+ (idg.interfere(pair.x, pair.y) ? "* " : " "));
			}

			for (int i = nPhyRegs; i < nRegvars; i++) {
				root.debOut.println("var " + rn.toString(i) + ": spillcost="
						+ spillCost[i] + " degree=" + degree[i] + " disturb="
						+ idg.disturbedFactor(i) + " regset=["
						+ rn.toString(machine.regSetMap(regset[i])) + "]");
			}
		}
	}
  
  
  // xs will be ys
  private void CopyArray(int[] xs, int[] ys){
	  if(xs.length != ys.length){
		  System.out.println("CopyArray error!");
	  }
	  for(int i = 0; i < xs.length; i++){
		  xs[i] = ys[i];
	  }
  }

  private void addCopyPair(int x, int y) {
    copyPairs.add(new CopyPair(x, y));
    copyPairs2.add(new CopyPair(x,y));

    // Move copy-related variables to end of working list.
    BiLink p = working.locate(rn.toSymbol(x));
    if (p != null) {
      p.unlink();
      working.append(p);
    }
    p = working.locate(rn.toSymbol(y));
    if (p != null) {
      p.unlink();
      working.append(p);
    }
  }



  private boolean isPhysical(Symbol x) {
    return rn.index(x) < nPhyRegs;
  }

  private boolean isPhysical(int x) {
    return x < nPhyRegs;
  }


  /** Interference test considering physical registers. **/
  private boolean interfere(int x, int y) {
    if (idg.interfere(x, y))
      return true;

    if (isPhysical(x)) {
      short[] v = machine.overlapRegs(x);
      for (int i = 0; i < v.length; i++) {
        if (idg.interfere(v[i], y))
          return true;
      }
    }
    return false;
  }
  /*private boolean precoloredTest(int x, int y){
	  //System.out.println("in precoloredTest, ["+x+"], ["+y+"]");
	  
	  int regsetxy = machine.andSet(regset[x], regset[y]);
	  boolean georgeCond = true;
	  for (NumberSet.Iterator p = idg.interfereSet(y).iterator(); p.hasNext();) {
	      int t = p.next();
	      if (machine.igWeight(regsetxy, regset[t]) != 0) {
	        if (!idg.interfere(t, x)) {
	          // t is a neighbor of y, but not of x.
	          if (degree[t] >= navail[t]) {
	            //k += machine.igWeight(regsetxy, regset[t]);
	            georgeCond = false;
	          }
	        }
	      }
	  }
	  return georgeCond;
  }*/


  /** Coalesce variables if possible. **/
  
  //count the num of the coalescing pair from copypair to compare the assignReg;
  private int[] how(){
	  int[] test = new int[2];
	  int c = 0;
	  int ass = 0;
	  for(BiLink p = copyPairs2.first(); !p.atEnd(); p = p.next()) {
	      CopyPair pair = (CopyPair)p.elem();
	      
	      if(assignedReg[pair.x] == assignedReg[pair.y]){ 
	    	  ass++;
	      }
	      
	      
	      int x = nameOf(pair.x);
	      int y = nameOf(pair.y);
	      if(x == y && assignedReg[x] > 0){
	    	  c++;
	      }
	  }    
	  test[0] = c;
	  test[1] = ass;
	  return test;
  }
  private boolean coalesce() {
	  
	int len = copyPairs.length();
	
	
    for (BiLink p = copyPairs.first(); !p.atEnd(); p = p.next()) {
      CopyPair pair = (CopyPair)p.elem();
      
      /*System.out.println("coalesce:pair(["
              + pair.x + "] = [" + pair.y
              + "])");*/

      int x = nameOf(pair.x);
      int y = nameOf(pair.y);

      if (isPhysical(y)) {
        int w = x; x = y; y = w;
      }

      if (x == y) {
        if (root.traceOK("OptimisticCoalescingAllocation", 2))
          root.debOut.println("coalesce:pair("
                              + rn.toString(pair.x) + "=" + rn.toString(pair.y)
                              + ") already coalesced");
        //System.out.println("fail to coalesce(x=y so dont pass)");
        p.unlink();
        continue;
      }
      else if (interfere(x, y) || machine.andSet(regset[x], regset[y]) == 0) {//constrained?
        if (root.traceOK("OptimisticCoalescingAllocation", 2))
          root.debOut.println("coalesce:pair("
                              + rn.toString(pair.x) + "=" + rn.toString(pair.y)
                              + ") interfering");
        p.unlink();
        
        continue;
      }
      
      if (root.traceOK("OptimisticCoalescingAllocation", 2))
    	  root.debOut.print("coalesce:pair("
    			  + rn.toString(pair.x) + "=" + rn.toString(pair.y)
    			  + ") coalesced to "
    			  + rn.toString(x) + " degree: " + degree[x]);
      

      p.unlink();
      
      //coalescing x and y*******************************************************
      name[ufo.union(x, y)] = x;
      
      int regsetxy = machine.andSet(regset[x], regset[y]);
      if (regsetxy != regset[x]) {
    	  for (NumberSet.Iterator q = idg.interfereSet(x).iterator();q.hasNext();) {
    		  int t = q.next();
    		  int dx = machine.igWeight(regsetxy, regset[t]);
    		  if (dx == 0){
    			  idg.unsetInterfere(x, t);
    			  if(adjList[x].contains(t)){
    				  adjList[x].remove(t);
    				  adjList[t].remove(x);
    			  }
    		  }
    		  if (status[t] == VAR_WORKING)
    			  degree[x] += dx - machine.igWeight(regset[x], regset[t]);
    		  
    		  degree[t] += machine.igWeight(regset[t], regsetxy)
    		  - machine.igWeight(regset[t], regset[x]);
    	  }
      }
      for (NumberSet.Iterator q = idg.interfereSet(y).iterator(); q.hasNext();) {
    	  int t = q.next();
    	  if (machine.igWeight(regsetxy, regset[t]) != 0) {
    		  if (idg.setInterfere(x, t)) {
    			  if(!adjList[x].contains(t)){
    				  if(!(assignedReg[x] > 0)){
    					  adjList[x].add(t);
    				  }
    				  if(!(assignedReg[t] > 0)){
    					  adjList[t].add(x);
    				  }
    			  }
    			  if (status[t] == VAR_WORKING)
    				  degree[x] += machine.igWeight(regsetxy, regset[t]);
    			  
    			  degree[t] += machine.igWeight(regset[t], regsetxy);
    		  }
    	  }
      }
      regset[x] = regsetxy;
      
      navail[x] = machine.nAvail(regsetxy);
      spillCost[x] += spillCost[y];
      
      //count coalescing*****************************************************
      coaNum++;
      
      ingredient[x]++;
      ingredient[y] = 1;
      
      isParent[x] = true;
      isParent[y] = false;
      
      idg.disturbSet(x).addAll(idg.disturbSet(y));
      
      removeNode(y);
      status[y] = VAR_COALESCED;

      if (root.traceOK("OptimisticCoalescingAllocation", 2))
    	  root.debOut.println(" to " + degree[x]);
    }
    if(copyPairs.isEmpty()){
    	//System.out.println("all coalesce finished");
    	return false;
    }else{
    	System.out.println("emptyError!");
    	return true;
    }
  }  
      
  /** Remove low-degree node if possible. **/
  private boolean simplify() {
    for (BiLink p = working.first(); !p.atEnd(); p = p.next()) {
      int v = rn.index((Symbol)p.elem());

      if (degree[v] < navail[v]) {
        if (root.traceOK("OptimisticCoalescingAllocation", 2))
          root.debOut.println("simplify: "
                              + rn.toString(v) + " on stack");

        toBeAssigned[stackp++] = v;
        removeNode(v);
        status[v] = VAR_ONSTACK;
        return true;
      }
    }
    return false;
  }


  /** Select spill candidate. **/
  private static int INFINITY = 9999999;
  private boolean potentialSpill() {//ingredient?
    int chosen = -1;
    int chosenFactor = 0;
    for (BiLink p = working.first(); !p.atEnd(); p = p.next()) {
      int v = rn.index((Symbol)p.elem());
      int factor = (idg.disturbedFactor(v) - idg.disturbingFactor(v));

      if (isPhysical(v))
        throw new CantHappenException("physical register on working list");

      if (dontspill[v])
        continue;

      if (chosen < 0) {
        chosen = v;
        chosenFactor = factor;
      } else {
        
    	//use spillcost&ingredient
        /*int c = spillCost[v] - spillCost[chosen];
        if (c < 0 || (c == 0 && ingredient[v] > ingredient[chosen])
        		||(c == 0 && navail[v] > navail[chosen])){
        	 chosen = v;
             chosenFactor = factor;
        }*/
        
        //use disturbedfactor
        
        int d = factor - chosenFactor;
        if (d > 0
            || (d == 0 && ingredient[v] > ingredient[chosen]) 
            	|| (d == 0
                && (navail[v] > navail[chosen]
                    || (navail[v] == navail[chosen]
                        && spillCost[v] < spillCost[chosen])))) {
            chosen = v;
            chosenFactor = factor;
        }
      }
    }
    if (chosen < 0)
      return false;

    if (root.traceOK("OptimisticCoalescingAllocation", 2))
      root.debOut.println("chooseSpill: " + rn.toString(chosen) + " on stack, "
                          + " degree=" + degree[chosen]
                          + " avail=" + navail[chosen]
                          + " disturbf=" + chosenFactor
                          + " spillcost=" + spillCost[chosen]);
    
    //System.out.println("potentialspill--["+chosen+"]");
    
    toBeAssigned[stackp++] = chosen;
    removeNode(chosen);
    status[chosen] = VAR_ONSTACK;
    return true;
  }



  /** Remove variable v from working list, interference graph
   *   and copy-pair list. **/
  private void removeNode(int v) {
    working.remove(rn.toSymbol(v));
    for (NumberSet.Iterator p = idg.interfereSet(v).iterator(); p.hasNext();) {
      int w = p.next();
      degree[w] -= machine.igWeight(regset[w], regset[v]);
    }
    for (BiLink p = copyPairs.first(); !p.atEnd(); p = p.next()) {
      CopyPair pair = (CopyPair)p.elem();

      if (nameOf(pair.x) == v || nameOf(pair.y) == v)
        p.unlink();
    }
  }



  /** Assign registers. **/
  private void assignRegisters() {
		while (stackp > 0) {
			int v = toBeAssigned[--stackp];
			BitMapSet avail = (BitMapSet) (machine.regSetMap(regset[v]).clone());
			
			if (false && root.traceOK("OptimisticCoalescingAllocation", 2))
				root.debOut.println("assign: " + rn.toString(v) + " initially "
						+ rn.toString(avail));

			// System.out.println("avail["+v+"] is = "+avail);

			// assign by using only idgSet
			/*
			 * for (NumberSet.Iterator p = idg.interfereSet(v).iterator();
			 * p.hasNext();) { int w = p.next(); if (assignedReg[w] > 0) { if
			 * (false && root.traceOK("OptimisticCoalescingAllocation", 2))
			 * root.debOut.println("assign: drop " + rn.toString(w) + ": " +
			 * machine.registerToString(assignedReg[w]));
			 * machine.removeRegister(avail, assignedReg[w]); } }
			 */

			// assign by using adjList[]
			for (NumberSet.Iterator p = adjList[v].iterator(); p.hasNext();) {
				int w = p.next();
				if (assignedReg[w] > 0) {
					if (false && root.traceOK("OptimisticCoalescingAllocation",
							2))
						root.debOut.println("assign: drop " + rn.toString(w)
								+ ": "
								+ machine.registerToString(assignedReg[w]));
					machine.removeRegister(avail, assignedReg[w]);
				}
			}
			if (false && root.traceOK("OptimisticCoalescingAllocation", 2))
				root.debOut.println("assign: " + rn.toString(v) + " finally "
						+ rn.toString(avail));

			if (!((avail.nextElement(1)) < 0)) {
				assignedReg[v] = avail.nextElement(1);
				assignedVars.add(rn.toSymbol(v));
				status[v] = VAR_ASSIGNED;

				if (root.traceOK("OptimisticCoalescingAllocation", 2))
					root.debOut.println("assign: " + rn.toString(v)
							+ " assigned to: "
							+ machine.registerToString(assignedReg[v]));
			}else if (isParent[v]) {
				//if this node is coalesing-parent,Try;
				// for debug 2010.4.20
				//System.out.println("assignRegister : call TryPrimitiveNodes("+rn.toSymbol(v)+")");
				//
				TryPrimitiveNodes(v);
				//if Aggressive.... not Try, just do acturalSpill(v)!!
				//actualSpill(v);
				
			} else {//if this node is coalescing-child,actual-Spill;
				actualSpill(v);

				if (root.traceOK("OptimisticRegisterAllocation", 2))
					root.debOut.println("assign: " + rn.toString(v)
							+ " spilled.");
			}
		}
	}
  private void FixIdg3(int n){
	  for(NumberSet.Iterator p = adjList[n].iterator(); p.hasNext();){
		  int w = p.next();
		  if(originalList[n].contains(w)){
			  
		  }else{
			  if(!(assignedReg[w] > 0)){// //
				  adjList[w].remove(n);
			  }else{
				  if(interfere(w,n)){
					  //System.out.println("in fixIdg3, remove");
					  adjList[w].remove(n);
				  }
			  }
			  idg.unsetInterfere(w, n);
			  idg.unsetInterfere(n, w);
		  }
	  }
	  
	  adjList[n].copy(originalList[n]);
	  
	  for (NumberSet.Iterator p = adjList[n].iterator(); p.hasNext();) {
		  int w = p.next();
		  int v = nameOf(w);
		  // for debug 2010.4.9
		  //if(interfere(v,n)){//machine.igWeight(regset[n], regset[v])!= 0 && !(machine.covered(v, idg.interfereSet(n)))){//12/14
		  if(!interfere(v,n)){//machine.igWeight(regset[n], regset[v])!= 0 && !(machine.covered(v, idg.interfereSet(n)))){//12/14
		  //
			  idg.setInterfere(v, n);
	
			  if(!adjList[v].contains(n)){
				  if(!(assignedReg[v] > 0)){
					  adjList[v].add(n);
				  }
				  if(!(assignedReg[n] > 0)){
					  adjList[n].add(v);
				  }
			  }
		  }
	  }
  }
  private void TryPrimitiveNodes(int n) {

		BiList PrimitiveNodes = new BiList();
		Vector<Symbol> ColorableNodes = new Vector();
		PrimitiveNodes = UndoCo(n);

		for (BiLink p = PrimitiveNodes.first(); !p.atEnd(); p = p.next()) {
			int t = rn.index((Symbol) p.elem());
			BitMapSet avail = (BitMapSet) (machine.regSetMap(regset[t]).clone());

			// assign by using only idgSet
			/*
			 * for(NumberSet.Iterator r = idg.interfereSet(t).iterator();
			 * r.hasNext();){ int w = r.next(); if(assignedReg[w] > 0){
			 * machine.removeRegister(avail, assignedReg[w]); } }
			 */

			// assign by using adjList[]
			for (NumberSet.Iterator r = adjList[t].iterator(); r.hasNext();) {
				int w = r.next();
				if (assignedReg[w] > 0) {
					machine.removeRegister(avail, assignedReg[w]);
				}
			}
			if (!((avail.nextElement(1)) < 0)) {
				ColorableNodes.add(rn.toSymbol(t));

			} else {
				// for debug 2010.4.20
				//System.out.println("TryPrimitiveNodes actualSpill:"+rn.toSymbol(t).name);
				//
				actualSpill(t);
				coaNum--;
			}
		}

		int num = ColorableNodes.size();
		// ok2
		ColorableSet OkpassNodes = new ColorableSet();
		OkpassNodes.copy(Okcolor2(ColorableNodes));

		for (int i = 0; i < num; i++) {
			Symbol sym = (Symbol) ColorableNodes.get(i);
			int v = rn.index(sym);
			if (OkpassNodes.nodes.contains(sym)) {

				BitMapSet avail = (BitMapSet) (machine.regSetMap(regset[v])
						.clone());
				for (NumberSet.Iterator r = adjList[v].iterator(); r.hasNext();) {
					int w = r.next();
					if (assignedReg[w] > 0) {
						machine.removeRegister(avail, assignedReg[w]);
					}
				}
				assignedReg[v] = OkpassNodes.okColor;
				assignedVars.add(rn.toSymbol(v));
				status[v] = VAR_ASSIGNED;
				intry++;

				if (root.traceOK("OptimisticCoalescingAllocation", 2))
					// for debug 2010.4.8
					//root.debOut.println("assign: " + rn.toString(v)
						root.debOut.println("TryPrimitives assign: " + rn.toString(v)
					//
							+ " assigned to: "
							+ machine.registerToString(assignedReg[v]));
			} else {
				pushStack(v);
				status[v] = VAR_ONSTACK;

				coaNum--;
				// add by nakagawa
				undoCoaNum++;
				// add by nakagawa

			}
		}
		return;
		// 
	}
  private BiList UndoCo(int n){
	  BiList PrimitiveNodes = new BiList();
	  
	  PrimitiveNodes.add(rn.toSymbol(n));
	  for(int i = 0; i < nRegvars; i++){
		  if(status[i] == VAR_COALESCED){
			  if(nameOf(i) == n){
				  PrimitiveNodes.add(rn.toSymbol(i));
			  }
		  }
	  }
	  
	  for (BiLink p = PrimitiveNodes.first(); !p.atEnd(); p = p.next()){
		  
		  int prim = rn.index((Symbol)p.elem());
		  FixIdg3(prim);
		  
		  spillCost[prim] = savedSpillCost[prim];
		  status[prim] = VAR_WORKING;

		  ufo.undo(prim);
		  name[prim] = prim;
		  
		  regset[prim] = originalRegset[prim];
		  isParent[prim] = false;
		  ingredient[prim] = 1;
	  }
	  return PrimitiveNodes;
  }
  
  private ColorableSet Okcolor2(Vector nodes){
	  ColorableSet[] cs = new ColorableSet[nPhyRegs];
	  int l = nodes.size();
	  int mini = INFINITY;
	  int minIndex = 0;
	  for(int i = 1; i < nPhyRegs; i++){
		  cs[i] = new ColorableSet();
		  for(int j = 0; j < l; j++){
			  Symbol sym = (Symbol)nodes.get(j);
			  int v = rn.index(sym);
			  BitMapSet avail = (BitMapSet)(machine.regSetMap(regset[v]).clone());
			  
			  for (NumberSet.Iterator r = adjList[v].iterator(); r.hasNext();){
		    	  int w = r.next();
		    	  if(assignedReg[w] > 0){
		    		  machine.removeRegister(avail, assignedReg[w]);
		    	  }
		      }
			  
			  
			  if(avail.contains(i)){
				  cs[i].nodes.add(sym);
			  }else{
				  cs[i].spillcost += spillCost[v];
			  }
		  }
		  if(mini > cs[i].spillcost){
			  mini = cs[i].spillcost;
			  minIndex = i;
		  }
	  }
	  cs[minIndex].okColor = minIndex;
	  return cs[minIndex];
  }

  private void pushStack(int v){
	  int[] copy = new int[nRegvars];
	  for(int i = 0; i < stackp; i++){
		  int t = toBeAssigned[i];
		  copy[i+1] = t;
	  }
	  copy[0] = v;
	  for(int i = 0; i < stackp+1; i++){
		  int t = copy[i];
		  toBeAssigned[i] = t;
	  }
	  stackp = stackp+1;
  }
  
  class ColorableSet{
	  BiList nodes;
	  int spillcost;
	  int okColor;
	  public ColorableSet(){
		  this.nodes = new BiList();
		  this.spillcost = 0;
		  this.okColor = 0;
	  }
	  public void copy(ColorableSet s){
		  this.nodes = s.nodes.copy();
		  this.spillcost = s.spillcost;
		  this.okColor = s.okColor;
	  }
  }

  private void actualSpill(int v){
	 
	 assignedReg[v] = -1;
 	 spilledVars.add(rn.toSymbol(v));
 	 status[v] = VAR_SPILLED;
 	 
 	 //igredient[] is usually 1;
 	 spillNum = spillNum+ingredient[v];
 	 //System.out.println("ingre"+ingredient[v]);
 	 //System.out.println("spinum"+spillNum);
 	 
 	 spi++;
 	 spi2 = spi2 + ingredient[v];
 	 
  }

	 
  
  /** Replace virtual registers with physical registers. * */
  private void replaceRegisters() {
    for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd();
         p = p.next()) {
      for (BiLink q = ((BasicBlk)p.elem()).instrList().first(); !q.atEnd();
           q = q.next()) {
        LirNode ins = (LirNode)q.elem();

        replaceInst(ins);
        if (ins.opCode == Op.SET
            && (ins.kid(0).opCode == Op.REG || ins.kid(0).opCode == Op.SUBREG)
            && ins.kid(0).equals(ins.kid(1)))
          q.unlink();
      }
    }
  }

  private void replaceInst(LirNode tree) {
    int n = tree.nKids();
    for (int i = 0; i < n; i++) {
      if (tree.kid(i).opCode == Op.REG) {
        if (!isPhysical(((LirSymRef)tree.kid(i)).symbol)) {
          int v = nameOf(rn.index(tree.kid(i)));
          if (assignedReg[v] > 0)
            tree.setKid(i, machine.registerLir(assignedReg[v]));
        }
      } else
        replaceInst(tree.kid(i));
    }
  }


  private static final boolean newReplaceMethod = true;

  /** Insert load/save instructions before/after spilled variable ref/defs. **/
  private void rewrite() {
	  
    for (BiLink s = spilledVars.first(); !s.atEnd(); s = s.next()) {
      Symbol v = (Symbol)s.elem();

      // Frame variable
      Symbol frv = func.addSymbol("save_" + v.name, Storage.FRAME,
                                  v.type, v.boundary, 0, v.opt());
      int tmpn = 1;
      for (BiLink p = func.flowGraph().basicBlkList.first(); !p.atEnd();
           p = p.next()) {
        for (BiLink q = ((BasicBlk)p.elem()).instrList().first(); !q.atEnd();
             q = q.next()) {
          LirNode ins = (LirNode)q.elem();
          if (newReplaceMethod) {
        	  if (replaceRegToFrame(ins, v, frv)){
        		 
        		  q.setElem(newLir.replaceOptions(ins, removeInstT(ins.opt)));
        	  }
          }
          else {
            int reftype = hasReference(ins, v);
            if (reftype != 0) {
            	System.out.println("hoge");
            	Symbol temp = func.addSymbol(v.name + "%" + tmpn++, Storage.REG,
                                           v.type, v.boundary, 0, v.opt());
              replaceVar(ins, v, temp);
              if ((reftype & REF_USE) != 0) {
                // insert (set temp frv) before q;
                q.addBefore(newLir.operator(Op.SET, v.type,
                                            newLir.symRef(temp),
                                            newLir.operator(Op.MEM, v.type,
                                                            newLir.symRef(frv), null),
                                            null));
              }
              if ((reftype & REF_DEF) != 0) {
                // insert (set frv temp) after q;
                q.addAfter(newLir.operator(Op.SET, v.type,
                                           newLir.operator(Op.MEM, v.type, newLir.symRef(frv), null),
                                           newLir.symRef(temp), null));
                q = q.next();
              }
            }
          }
        }
      } 
    }
  }


  /** Replace spilled register variables into FRAMEs. **/
  private boolean replaceRegToFrame(LirNode tree, Symbol old, Symbol neu) {
    int oldi = rn.index(old);
    int n = tree.nKids();
    boolean changed = false;
    for (int i = 0; i < n; i++) {
      if (tree.kid(i).opCode == Op.REG) {
        if (nameOf(rn.index(tree.kid(i))) == oldi) {
          tree.setKid(i, newLir.node(Op.MEM, old.type, newLir.symRef(neu)));
          changed = true;
        }
      } else {
        changed |= replaceRegToFrame(tree.kid(i), old, neu);
      }
    }
    return changed;
  }


  /** Remove "&inst t" from list. **/
  private ImList removeInstT(ImList options) {
    if (options == null || options.atEnd() || options.next().atEnd())
      return options;

    if (options.elem() == "&inst" && options.elem2nd() == "t")
      return options.next2nd();

    Object car = options.elem();
    ImList next = options.next();
    ImList newNext = removeInstT(next);
    if (newNext == next)
      return options;
    else
      return new ImList(car, newNext);
  }


  private static final int REF_USE = 1;
  private static final int REF_DEF = 2;

  /* use LirNode#isRegisterOperand() instead.
  private boolean regOperand(LirNode node) {
    return (node.opCode == Op.REG
            || node.opCode == Op.SUBREG && node.kid(0).opCode == Op.REG);
  }
  */

  /** Return reference type if tree has reference of symbol v **/
  private int hasReference(LirNode tree, Symbol v) {
    int vi = rn.index(v);
    int reftype = 0;

    switch (tree.opCode) {
    case Op.PARALLEL:
      {
        int n = tree.nKids();
        for (int i = 0; i < n; i++)
          reftype |= hasReference(tree.kid(i), v);
      }
      break;

    case Op.SET:
      if (tree.kid(0).opCode == Op.MEM)
        reftype |= hasReference(tree.kid(0), v);
      else {
        if (tree.kid(0).opCode == Op.REG
            && nameOf(rn.index(tree.kid(0))) == vi)
          reftype |= REF_DEF;
      }
      reftype |= hasReference(tree.kid(1), v);
      break;

    case Op.CLOBBER:
      break;

    case Op.CALL:
      {
        // return values
        int n = tree.kid(2).nKids();
        for (int i = 0; i < n; i++) {
          if (tree.kid(2).kid(i).isRegisterOperand())
            if (tree.kid(2).kid(i).opCode == Op.REG
                && nameOf(rn.index(tree.kid(2).kid(i))) == vi)
              reftype |= REF_DEF;
        }
        // other operands (callee and parameters)
        reftype |= hasReference(tree.kid(0), v);
        reftype |= hasReference(tree.kid(1), v);
      }
      break;

    case Op.PROLOGUE:
      {
        int n = tree.nKids();
        for (int i = 1; i < n; i++) {
          if (tree.kid(i).isRegisterOperand())
            if (tree.kid(i).opCode == Op.REG
                && nameOf(rn.index(tree.kid(i))) == vi)
              reftype |= REF_DEF;
        }
      }
      break;

    case Op.REG:
      if (nameOf(rn.index(tree)) == vi)
        reftype |= REF_USE;
      break;

    default:
      {
        int n = tree.nKids();
        for (int i = 0; i < n; i++)
          reftype|= hasReference(tree.kid(i), v);
      }
      break;
    }
    return reftype;
  }
  

  /** Rename variables **/
  private void replaceVar(LirNode tree, Symbol old, Symbol neu) {
    int oldi = rn.index(old);
    int n = tree.nKids();
    for (int i = 0; i < n; i++) {
      if (tree.kid(i).opCode == Op.REG) {
        if (nameOf(rn.index(tree.kid(i))) == oldi) {
          tree.setKid(i, newLir.symRef(neu));
        }
      } else
        replaceVar(tree.kid(i), old, neu);
    }
  }


  /** Get regset name of variable. **/
  private String regsetName(Symbol regvar) {
    for (ImList p = regvar.opt(); !p.atEnd(); p = p.next()) {
      if (p.elem() == "&regset")
        return (String)p.elem2nd();
    }
    return "*reg*";
  }

  /** Get available register set for the variable. **/
  private int getRegSet(Symbol regvar) {
    return machine.getRegSet(regsetName(regvar));
  }

  /** Return true if dontspill flag set for the variable. **/
  private boolean isDontspill(Symbol regvar) {
    for (ImList p = regvar.opt(); !p.atEnd(); p = p.next()) {
      if (p.elem() == "&dontspill")
        return true;
    }
    return false;
  }
      

  /** Return real name (coalesced) of x **/
  int nameOf(int x) {
    if (x < 0)
      return x;
    if (x >= nRegvars) {
      root.debOut.println("Array index out of bounds: " + rn.toString(x));
      return x;
    }
    return name[ufo.find(x)];
  }


  /** Return true if symbol is already split variable (has two %s). **/
  boolean isAlreadySplit(Symbol sym) {
    int pos = sym.name.indexOf('%');
    return pos >= 0 && sym.name.indexOf('%', pos + 1) >= 0;
  }
}
