/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.contrib;

import coins.driver.CompileSpecification;
import coins.backend.Root;
import coins.backend.Function;
import coins.backend.lir.*;
import coins.backend.cfg.*;
import coins.backend.sym.*;
import coins.backend.util.*;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.LocalTransformer;
import coins.backend.Data;
import coins.backend.ana.*;

import java.util.*;

public class RegPromote{
	
  //static final SymTab globalTable;
  BiList LoopList = new BiList();
	
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new RegPromote()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "RegPromote"; }

    public String subject() { return "Register Promotion"; }
  }

  public RegPromote(){}

  public static final Trigger trig = new Trigger();

  public static void attach(CompileSpecification spec, Root root) {
  //  if (spec.getCoinsOptions().isSet("regpromote")) // do-nothing
  //    root.addHook("+AfterEarlyRewriting", trig);
  }
  
  public static void attachRegPromote(Root root){
	  root.addHook("+AfterEarlyRewriting", trig);
  }

  //public Symbol[] shadowarr;
	
  BiList postAMBlist = new BiList();
	
  public void doIt(Function f){
    //make RPloop with it's header
    //only loops that have one entry(header) 
    FlowGraph cfg = f.flowGraph();
    //System.out.println("GOING....1");
    LoopAnalysis loopana = (LoopAnalysis)f.require(LoopAnalysis.analyzer);
    for (BiLink p = cfg.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      if(loopana.isLoop[blk.id] && !loopana.multiEntry[blk.id]){
        LoopList.add(new RPloop(blk,loopana.nestLevel[blk.id],f));
      }  
    }
    //System.out.println("GOING....2");
    //get all LirNodes that refer addresses of global variables
    /*for (BiLink p = cfg.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      for(BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()){
      LirNode node = (LirNode)q.elem();
      if(node.opCode == Op.SET && node.kid(0).opCode ==Op.MEM && node.kid(1).opCode == Op.STATIC){
      postAMBlist.add(((LirSymRef)node.kid(1)).symbol);
      //System.out.println("add postAMBlist");
      }
      }
      }*/
		
    //System.out.println("GOING....3");
    //add each RPloop members
    for (BiLink p = cfg.basicBlkList.first(); !p.atEnd(); p = p.next()) {
      BasicBlk blk = (BasicBlk)p.elem();
      BasicBlk temp;
      for(BiLink q = LoopList.first(); !q.atEnd(); q = q.next()){
        temp = ((RPloop)q.elem()).head;
        if(temp == loopana.loopHeader[blk.id]){
          ((RPloop)q.elem()).member.add(blk);
          //System.out.println("add blk " + blk.id + " to member");
        }
      }
    }
    for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
      RPloop loop = (RPloop)p.elem();
      if(loop.nestLevel == 1){
        compMember(loop);
      }
    }
		
    //System.out.println("GOING....4");
    //add each RPloop exits
    for (BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
      ((RPloop)p.elem()).addExit();
    }
    //System.out.println("GOING....5");
    //get srndLoops of Loop p(pelm)
    for (BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
      RPloop pelm = (RPloop)p.elem();
      for(BiLink q = LoopList.first(); !q.atEnd(); q = q.next()) {
        RPloop qelm = (RPloop)q.elem();
        //System.out.println("nestlevel is " + pelm.nestLevel);
        if(pelm.nestLevel != 1 && qelm.nestLevel < pelm.nestLevel){
          boolean temp = true;
          for(BiLink h = pelm.member.first(); !h.atEnd(); h = h.next()) {
            if(!qelm.member.contains((BasicBlk)h.elem())){temp = false; break;}					
          }
          if(temp == true) {
            pelm.srndLoop.add(qelm);
            //System.out.println("add srndLoop");
          }
					
        }else if(pelm.nestLevel == 1){
        }
      }
    }
		
    /*for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
      RPloop loop = (RPloop)p.elem();
      System.out.println("head is " + loop.head.id);
      System.out.print( " member is ");
      for(BiLink a = loop.member.first(); !a.atEnd(); a = a.next()){
      BasicBlk ablk = (BasicBlk)a.elem();
      System.out.print(ablk.id + ", ");
      }
      System.out.print(" exit is ");
      for(BiLink b = loop.exitList.first(); !b.atEnd(); b = b.next()){
      BasicBlk bblk = (BasicBlk)b.elem();
      System.out.print(bblk.id + ", ");
      }
      System.out.print(" loop pre" + ((BiList)loop.head.predList()).length());
      System.out.println(" nestlevel" + loop.nestLevel);
      }*/
    //calculate L_Explicit, L_Ambiguous, L_Promotable then L_Lift.
		
    //System.out.println("GOING....6");
    for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()){
      ((RPloop)p.elem()).getGV(postAMBlist);
    }
    for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()){
      ((RPloop)p.elem()).getLIFT();
    }
		
    //add RegSymbols to localSymTab and make DobsymList of L_Lift
    BiList slist = new BiList();
    BiList dobsymlist = new BiList();
    //System.out.println("GOING....7");
    for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()){
      BiList list = ((RPloop)p.elem()).L_Lift;
      for(BiLink q = list.first(); !q.atEnd(); q = q.next()){
        Symbol rsym = (Symbol)q.elem();
        if(!slist.contains(rsym)) {
          slist.add(rsym);
          String name = (rsym.name + "%").intern();
          //System.out.println(rsym.name +" is sym name in L_Lift");
          Symbol regsym = f.addSymbol(name, Storage.REG, rsym.type, rsym.boundary, 0, null);
          dobsymlist.add(new DobSym(rsym, regsym));
        }
      }
    }
		
    //System.out.println("GOING....8");
    //insert landing pad and exit then change MEM to REG
    for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()){
      //System.out.println(LoopList.length() + " loops.");
      ((RPloop)p.elem()).insertNewInst(cfg, dobsymlist);
      ((RPloop)p.elem()).PreCTR();
    }

    if (false) {
      for (BiLink p = cfg.basicBlkList.first(); !p.atEnd(); p = p.next()) {
        BasicBlk blk = (BasicBlk)p.elem();
        System.out.println("lb " + blk.id);
        for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
          LirNode b = (LirNode)q.elem();
          System.out.println(b);
        }
      }
      
      System.out.println("GOING....9");
    }
  }
	
  //complete member of each loop
  public BiList compMember(RPloop loop){
    BiList blist = loop.member;
    for(BiLink q = blist.first(); !q.atEnd(); q = q.next()){
      BasicBlk blk = (BasicBlk)q.elem();
      for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()){
        RPloop lp = (RPloop)p.elem();
        if(lp != loop && blk == lp.head){
          blist = compMember(lp);
	  for(BiLink r = blist.first(); !r.atEnd(); r = r.next()){
	      BasicBlk b = (BasicBlk)r.elem();
	      if(!loop.member.contains(b)){
		  loop.member.add(b);
	      }
	  }
	}
      }
    }
    return loop.member;
  }
}

class RPloop{
	
  public BasicBlk head;
  public BiList tail = new BiList();
  public BiList member = new BiList();
  public BiList srndLoop = new BiList();
  public BiList exitList = new BiList();
  public int nestLevel = 0;
  public Function f;
	
  public BiList L_Explicit = new BiList();
  public BiList L_Ambiguous = new BiList();
  public BiList L_Promotable = new BiList();
  public BiList L_Lift = new BiList();
  private BiList DSlist;
  private boolean isAmb = false;
	
  public RPloop(BasicBlk blk, int level, Function func){
    f = func;
    head = blk;
    nestLevel = level;
    member.add(blk);
  }
	
  public void addExit(){
    BasicBlk blk;
    //System.out.println("addExit 1"); 
    for(BiLink q = member.first(); !q.atEnd(); q = q.next()){
      BiList x = ((BasicBlk)q.elem()).succList();
      for(BiLink p = x.first(); !p.atEnd(); p = p.next()){
        exitList.add((BasicBlk)p.elem());
      }
    }
    //System.out.println("addExit 2"); 
    for (BiLink p = exitList.first(); !p.atEnd(); p = p.next()) {
      if(member.contains((BasicBlk)p.elem())){			
        exitList.remove((BasicBlk)p.elem());
      }else{}
    }
    //System.out.println("addExit 3"); 
  }
	
  //calculate L_Explicit ,L_Ambiguous and then L_Promotable. 
  public void getGV(BiList postAmb){
    L_Ambiguous = postAmb;
    //boolean containsCall = false;
    //int call = 1;
    //int nocall = 0;
    //System.out.println("getGV 1"); 
		
    for(BiLink q = member.first(); !q.atEnd(); q = q.next()){
      BasicBlk blk = (BasicBlk)q.elem();
      for(BiLink p = blk.instrList().first(); !p.atEnd(); p = p.next()){
        LirNode stmt = (LirNode)p.elem();
        searchGV(stmt,false,true);
      }
    }
		
    for(BiLink q = member.first(); !q.atEnd(); q = q.next()){
      BasicBlk blk = (BasicBlk)q.elem();
      for(BiLink p = blk.instrList().first(); !p.atEnd(); p = p.next()){
        LirNode stmt = (LirNode)p.elem();
        //if(stmt.opCode == Op.CALL) containsCall = true;
        searchGV(stmt,isAmb,false);
      }
    }
    /*if(containsCall){
      for(BiLink q = member.first(); !q.atEnd(); q = q.next()){
      BasicBlk blk = (BasicBlk)q.elem();
      for(BiLink p = blk.instrList().first(); !p.atEnd(); p = p.next()){
      LirNode stmt = (LirNode)p.elem();
      searchGV(stmt,1);
      }
      }
      }*/
    //System.out.println("getGV 2"); 
    for(BiLink p = L_Explicit.first(); !p.atEnd(); p = p.next()){
      if(L_Ambiguous.contains((Symbol)p.elem()) == false){
        L_Promotable.add(p.elem());
      }
    }
    //System.out.println("getGV 3"); 
  }

  //search explicit or ambiguous ref.
  public void searchGV(LirNode node, boolean semiamb, boolean instsrch){
		
    if(instsrch){
      if(node.opCode == Op.CALL){isAmb = true; return;}
      if(node.opCode == Op.MEM){
        LirNode mem = node.kid(0);
        //##100 if(mem.opCode == Op.MEM){
        if(mem.opCode == Op.MEM || mem.opCode == Op.REG){ //##100 Mori mail 120129
          isAmb = true;
        }		
					
				
        /*LirNode mem = node.kid(0);
          if(){
          isAmb = true;
          }
          if(semiamb){ isAmb = true; return;}
				
          //LirNode x = node.kid(0);
          searchGV(mem, true, instsrch);
        */
      }
    }
		
    if(node.opCode == Op.MEM && !instsrch){
      LirNode mem = node.kid(0);
			
      if(mem.opCode == Op.STATIC){
        //symbol of this node is explicit				
        if(!isAmb){
          Symbol sym = ((LirSymRef)mem).symbol;
          if(!L_Explicit.contains(sym)){
            L_Explicit.add(sym);
            //System.out.println("add to Explicit");
          }
          return;
        }else{
          Symbol sym = ((LirSymRef)mem).symbol;
          if(!L_Ambiguous.contains(sym)){
            L_Ambiguous.add(sym);
            //System.out.println("add to Amb ");
          }
        }
								
      }
    }
		
    int n = node.nKids();
    for(int i=0; i<n; i++){
      LirNode x = node.kid(i);
      searchGV(x, semiamb, instsrch);
    }
		
  }
	
  //get L_Lift
  public void getLIFT(){
    for(BiLink p = L_Promotable.first(); !p.atEnd(); p = p.next()){
      Symbol pelm = (Symbol)p.elem();
      //System.out.println("is there srnd?" + srndLoop.length());
      if(srndLoop.length() == 0){
        L_Lift.add(pelm);
        //System.out.println("add to L_Lift");
      }else{
        for(BiLink q = srndLoop.first(); !q.atEnd(); q = q.next()){				
          RPloop qelm = (RPloop)q.elem();
          if(qelm.L_Promotable.contains(pelm) == false) {
            L_Lift.add(pelm);
            //System.out.println("add to L_Lift");
          }
        }
      }
    }
  }
	
  public void insertNewInst(FlowGraph cfg, BiList dob){
    DSlist = dob;
    //insert landing pad		
    BiList hpList = head.predList();
    BasicBlk nblk1 = cfg.insertNewBlkBefore(head);
    BiList templist1 = (BiList)nblk1.instrList();		
		
    //System.out.println(templist1.length() + "new blk df inst");
    //System.out.println(((LirNode)templist1.first().elem()).opCode);
    for(BiLink x = hpList.first(); !x.atEnd(); x = x.next()){
      BasicBlk hblk = (BasicBlk)x.elem();
      if(!member.contains(hblk) && hblk != nblk1){
				
        for(BiLink hp3 = hblk.succList().first(); !hp3.atEnd(); hp3=hp3.next()){
          BasicBlk b = (BasicBlk)hp3.elem();
          //System.out.println("succ of headpred " + b.id);
        }
        //LirNode jop = (LirNode)hblk.instrList().last().elem();				
        //LirLabelRef lop = (LirLabelRef)jop.kid(0);				
        hblk.replaceSucc(head, nblk1);					
        //System.out.println("insert new blk entry");
        //insert instructions
        BiList instr1 = new BiList();
        //System.out.println(L_Lift.length());
        for(BiLink a = L_Lift.first(); !a.atEnd(); a = a.next()){
          instr1.add(getToRegInst((Symbol)a.elem(), 0));
          //System.out.println("insert MemRegInst");
        }
        instr1.add((LirNode)templist1.first().elem());
        nblk1.setInstrList(instr1);
        //add new Symbols to localSymTab
      }
    }
		
		
    //insert exit
    //System.out.println(exitList.length() + " exits.");
    for(BiLink p = exitList.first(); !p.atEnd(); p = p.next()){
      BasicBlk eblk = (BasicBlk)p.elem();
      BasicBlk nblk = cfg.insertNewBlkBefore(eblk);
      BiList templist2 = (BiList)nblk.instrList();
			
      BiList pList = eblk.predList();
      for(BiLink q = pList.first(); !q.atEnd(); q = q.next()){
        BasicBlk pblk = (BasicBlk)q.elem();
        if(member.contains(pblk)){					
          pblk.replaceSucc(eblk, nblk);										
          //insert instructions
          BiList instr = new BiList();
          for(BiLink y = L_Lift.first(); !y.atEnd(); y = y.next()){
            instr.add(getToRegInst((Symbol)y.elem(), 1));
          }
          instr.add((LirNode)templist2.first().elem());
          nblk.setInstrList(instr); 
        }
      }
    }					
  }
	
  // return RegtoMem(0) or MemtoReg(1)
  public LirNode getToRegInst(Symbol sym, int flag){
    LirNode setInst = null;
    LirNode sttc = f.newLir.symRef(sym);
    LirNode reg = null;
    LirNode memr = f.newLir.operator0(Op.MEM, sym.type, sttc);
    for(BiLink p = DSlist.first(); !p.atEnd(); p = p.next()){
      DobSym ds = (DobSym)p.elem();
      if(ds.sym1 == sym){
        reg = f.newLir.symRef(ds.sym2);
        break;
      }
    }
    //System.out.println("target sym is " + sym.id + "in new blk");
    if(flag == 0){
      setInst = f.newLir.operator0(Op.SET, sym.type, reg, memr);			
    }else if (flag == 1){
      setInst = f.newLir.operator0(Op.SET, sym.type, memr, reg);			
    }else{}
    return setInst;
  }
	
  public void PreCTR(){
    //DSlist = dob;
    for(BiLink q = member.first(); !q.atEnd(); q = q.next()){
      BasicBlk blk = (BasicBlk)q.elem();
      for(BiLink p = blk.instrList().first(); !p.atEnd(); p = p.next()){
        LirNode stmt = (LirNode)p.elem();
        changeToReg(stmt);
      }
    }
  }
			
  public void changeToReg(LirNode node){		
    int n = node.nKids();
    for(int i = 0; i<n; i++){
      LirNode knode = node.kid(i);
      if(knode.opCode == Op.MEM){
        LirNode mem = knode.kid(0);
        if(mem.opCode == Op.STATIC){
          Symbol sym = ((LirSymRef)mem).symbol;
          if(L_Lift.contains(sym)){
            for(BiLink p = DSlist.first(); !p.atEnd(); p = p.next()){
              DobSym ds = (DobSym)p.elem();
              if(ds.sym1 == sym){
                node.setKid(i,f.newLir.symRef(ds.sym2));
								
              }
            }
          }
        }
      }
      changeToReg(knode);
    }
    return;
  }
	
}

class DobSym{
  Symbol sym1;
  Symbol sym2;
  public DobSym(Symbol s1, Symbol s2){
    sym1=s1;
    sym2=s2;
  }
}
