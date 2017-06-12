/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//
// RegPromoteEX.java has been developed by Kensuke Mori (titech.ac.jp).
//
package coins.backend.contrib;

import coins.driver.CompileSpecification;
import coins.alias.anallir.AliasInformation;
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
import coins.backend.Module;
import coins.backend.ana.*;
import coins.backend.contrib.RegPromote;
import java.util.Vector;


public class RegPromoteEX {
	
	//static final SymTab globalTable;
	BiList LoopList = new BiList();
	
	private static class Trigger implements LocalTransformer {
		public boolean doIt(Function func, ImList args) {
			//System.out.println("RegPromoteEX Start");
			(new RegPromoteEX()).doIt(func);
			//System.out.println("RegPromoteEX End");
			return true;
		}
		public boolean doIt(Data data, ImList args) { return true; }
		public String name() { return "RegPromoteEX"; }		
		public String subject() { return "Register Promotion Extend Version"; }
	}

	public RegPromoteEX(){}

	public static final Trigger trig = new Trigger();

	public static void attach(CompileSpecification spec, Root root) {
		//if(spec.getCoinsOptions().isSet("regpromote-ex")) {
		//	root.addHook("+AfterEarlyRewriting", trig);
		//}
	}
	public static void attachRegPromoteEX(Root root){
		  root.addHook("+AfterEarlyRewriting", trig);
	  }
	
	//BiList postAMBlist = new BiList();
	
	public void doIt(Function f) {
		AliasInformation aliasInf = f.module.aliasInf;
		
		if(aliasInf == null){
			regPromoteError("aliasInformation == null.",f);
			//System.out.println("Error : aliasInformation == null. RegPromote-EX fin.");
			return;
		}
		
		BiList DereferencedTable  = new BiList();
        try {
        	BiList globalSyms = f.module.globalSymtab.symbols();
    		BiList localSyms = f.localSymtab.symbols();
    		Symbol sym;
    		Vector v;
        	//DereferencedTable making
            for(BiLink l = globalSyms.first(); !l.atEnd(); l = l.next()) {
            	sym = (Symbol)l.elem();
        		v = aliasInf.getPointsToSet(sym);
        		if(v != null){
        			if(v.size() >= 1){
                		for(int i = 0; i < v.size(); i++) {
            				Symbol dsym = (Symbol)v.elementAt(i);
            				if(!DereferencedTable.contains(dsym)){
            					DereferencedTable.add(dsym);	
            				}
            			}
                	}
        		}
        		else{
        			//if(sym.name != ".stackRequired"){
        			throw new NullPointerException(sym.name + "Global Symbol's analysys failed.");
        			//}
        		}
            }
            for(BiLink l = localSyms.first(); !l.atEnd(); l = l.next()) {
            	sym = (Symbol)l.elem();
            	//System.out.println(sym);
        		v = aliasInf.getPointsToSetLocal(sym);
        		if(v != null){
        			if(v.size() >= 1){
                		for(int i = 0; i < v.size(); i++) {
            				Symbol dsym = (Symbol)v.elementAt(i);
            				if(!DereferencedTable.contains(dsym)){
            					DereferencedTable.add(dsym);	
            				}
            			}
                	}
        		}
        		else{
        			if(!sym.name.startsWith(".AG") && !sym.name.startsWith(".T")){
        			//if(!sym.name.startsWith(".AG")){
        				throw new NullPointerException(sym.name + "Local Symbol's analysys failed.");
        			}
        		}
            }
    	}
    	catch(NullPointerException e) {
    		regPromoteError("DereferencedTable making fail.",f);
    		//System.out.println("Error : DereferencedTable making fail. RegPromote-EX fin.");
			return;
    	}
		
		//make RPloop with it's header
		//only loops that have one entry(header) 
		FlowGraph cfg = f.flowGraph();
		LoopAnalysis loopana = (LoopAnalysis)f.require(LoopAnalysis.analyzer);
		for(BiLink p = cfg.basicBlkList.first(); !p.atEnd(); p = p.next()) {
			BasicBlk blk = (BasicBlk)p.elem();
			if(loopana.isLoop[blk.id] && !loopana.multiEntry[blk.id]) {
				LoopList.add(new RPloopEX(blk,loopana.nestLevel[blk.id],f));
			}
		}
		for(BiLink p = cfg.basicBlkList.first(); !p.atEnd(); p = p.next()) {
			BasicBlk blk = (BasicBlk)p.elem();
			RPloopEX temp;
			for(BiLink q = LoopList.first(); !q.atEnd(); q = q.next()){
				temp = (RPloopEX)q.elem();
				if(temp.head == loopana.loopHeader[blk.id]){
					temp.member.add(blk);
				}
			}
		}
		for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {	
			RPloopEX loop = (RPloopEX)p.elem();
			if(loop.nestLevel == 1) {
				compMember(loop);
			}
		}
		//add each RPloop exits
		for (BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
			((RPloopEX)p.elem()).addExit();
		}
		//get srndLoops of Loop p(pelm)
		for (BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
			RPloopEX pelm = (RPloopEX)p.elem();
			for(BiLink q = LoopList.first(); !q.atEnd(); q = q.next()) {
				RPloopEX qelm = (RPloopEX)q.elem();
				//System.out.println("nestlevel is " + pelm.nestLevel);
				if(pelm.nestLevel != 1 && qelm.nestLevel < pelm.nestLevel){
					boolean temp = true;
					for(BiLink h = pelm.member.first(); !h.atEnd(); h = h.next()) {
						if(!qelm.member.contains((BasicBlk)h.elem())){
							temp = false; break;
						}					
					}
					if(temp) {	
						pelm.srndLoop.add(qelm);
					}
				}
			}
		}
        
		for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
			RPloopEX lp = (RPloopEX)p.elem();
			lp.DereferencedTable = DereferencedTable;
			lp.getGV();
		}
		//add RegSymbols to localSymTab and make DobsymList of L_Lift
		BiList slist = new BiList();
		BiList dobsymlist = new BiList();
		RPloopEX lp;
		for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()){
			lp = (RPloopEX)p.elem();
			lp.getLIFT();
			lp.makeDobSymList(slist,dobsymlist);
			//insert landing pad and exit then change MEM to REG
			lp.insertNewInst(cfg);
			lp.PreCTR();
		}
	}
	
	//complete member of each loop
	public BiList compMember(RPloopEX loop) {
		BiList blist = loop.member;
		for(BiLink q = blist.first(); !q.atEnd(); q = q.next()) {		
			BasicBlk blk = (BasicBlk)q.elem();
			for(BiLink p = LoopList.first(); !p.atEnd(); p = p.next()) {
				RPloopEX lp = (RPloopEX)p.elem();
				if(lp != loop && blk == lp.head) {
					blist = compMember(lp);
					for(BiLink r = blist.first(); !r.atEnd(); r = r.next()){
						BasicBlk b = (BasicBlk)r.elem();
						if(!loop.member.contains(b)) {
							loop.member.add(b);
						}
					}
				}
			}
		}
		return loop.member;
	}
	
	public void regPromoteError(String msg, Function f){
		System.out.println("Error : " + msg + " RegPromote-EX fin.");
		System.out.println("RegPromote Start.");
		(new RegPromote()).doIt(f);
	}
}

class RPloopEX {
	public BasicBlk head;
	//public BiList tail = new BiList();
	public BiList member = new BiList();
	public BiList srndLoop = new BiList();
	public BiList exitList = new BiList();
	public int nestLevel = 0;
	public Function f;
	public BiList L_Variable = new BiList();
	public BiList L_Promotable = new BiList();
	public BiList L_Lift = new BiList();
	private BiList DSlist;
	private boolean isFunc = false;
	private boolean isMem = false;
	public BiList DereferencedTable;
	private BiList hash = new BiList();

	public RPloopEX(BasicBlk blk, int level, Function func) {
		f = func;
		head = blk;
		nestLevel = level;
		member.add(blk);
	}
	
	public void addExit() {
//		System.out.println("addExit");
		BasicBlk blk;
		for(BiLink q = member.first(); !q.atEnd(); q = q.next()) {
			BiList x = ((BasicBlk)q.elem()).succList();
			for(BiLink p = x.first(); !p.atEnd(); p = p.next()) {
				exitList.add((BasicBlk)p.elem());
			}
		}
		for (BiLink p = exitList.first(); !p.atEnd(); p = p.next()) {	
			if(member.contains((BasicBlk)p.elem())) {
				exitList.remove((BasicBlk)p.elem());
			}
		}
	}
	
	//calculate L_Promotable.
	public void getGV() {
		for(BiLink q = member.first(); !q.atEnd(); q = q.next()) {
			BasicBlk blk = (BasicBlk)q.elem();
			for(BiLink p = blk.instrList().first(); !p.atEnd(); p = p.next()) {
				searchAll((LirNode)p.elem());
				if(isFunc){
					return;
				}
			}
		}
		if(isMem){
			for(BiLink p = L_Variable.first(); !p.atEnd(); p = p.next()) {
				Symbol pelm = (Symbol)p.elem();
				if(!DereferencedTable.contains(pelm)){
					L_Promotable.add(pelm);
				}
			}
		}
		else{
			for(BiLink p = L_Variable.first(); !p.atEnd(); p = p.next()) {
				L_Promotable.add((Symbol)p.elem());
			}
		}
		//System.out.println("L_Promotable = " + L_Promotable);
	}

	private void searchAll(LirNode node) {
		if(node.opCode == Op.CALL) {
			isFunc = true;
			return;
		}
		if(node.opCode == Op.MEM){
			LirNode mem = node.kid(0);
	        if(mem.opCode == Op.MEM || mem.opCode == Op.REG){
	          isMem = true;
	        }	
	    }
		if(node.opCode == Op.STATIC) {
			Symbol sym = ((LirSymRef)node).symbol;
			if(!hash.contains(sym)) {
				if(isRegisterType(sym.type)) {
					//System.out.println("symbol = " + sym);
					L_Variable.add(sym);
				}
				hash.add(sym);	
			}
		}
		int n = node.nKids();
		for(int i = 0; i < n; i++) {
			searchAll(node.kid(i));
		}
	}
	
	//get L_Lift
	public void getLIFT() {
		for(BiLink p = L_Promotable.first(); !p.atEnd(); p = p.next()) {
			Symbol pelm = (Symbol)p.elem();
			if(srndLoop.length() == 0) {
				L_Lift.add(pelm);
				//System.out.println("add to L_Lift");
			} else {
				boolean isReg = false;
				for(BiLink q = srndLoop.first(); !q.atEnd(); q = q.next()) {				
					RPloopEX qelm = (RPloopEX)q.elem();
					if(qelm.L_Promotable.contains(pelm)){
						isReg = true;
						break;
					}
				}
				if(!isReg){
					L_Lift.add(pelm);
				}
			}
		}
		//System.out.println("L_lift = " + L_Lift);
		//System.out.println("DereferencedTable = " + DereferencedTable);
	}
	
	//Register Name Making
	public void makeDobSymList(BiList slist, BiList dobsymlist) {
		Symbol rsym;
		Symbol regsym;
		for(BiLink q = L_Lift.first(); !q.atEnd(); q = q.next()){
			rsym = (Symbol)q.elem();
			regsym = null;
			if(!slist.contains(rsym)){
				slist.add(rsym);
				String name = (rsym.name + "%").intern();
				//System.out.println(rsym.name + " " + rsym.type + " is sym name in L_Lift");
				regsym = f.addSymbol(name, Storage.REG, rsym.type, rsym.boundary, 0, null);
				DobSym dobsym = new DobSym(rsym, regsym);
				dobsymlist.add(dobsym);
				//System.out.println("1 : DobSym(" + rsym + ", " + regsym + ") add.");
			}
		}
		DSlist = dobsymlist;
	}

	public void insertNewInst(FlowGraph cfg) {
		//insert landing pad		
		BiList hpList = head.predList();
		BasicBlk nblk1 = cfg.insertNewBlkBefore(head);
		BiList templist1 = (BiList)nblk1.instrList();	
		
		//System.out.println(templist1.length() + "new blk df inst");
		//System.out.println(((LirNode)templist1.first().elem()).opCode);
		for(BiLink x = hpList.first(); !x.atEnd(); x = x.next()) {
			BasicBlk hblk = (BasicBlk)x.elem();
			if(!member.contains(hblk) && hblk != nblk1) {
				hblk.replaceSucc(head, nblk1);					
				//System.out.println("insert new blk entry");
				//insert instructions
				BiList instr1 = new BiList();
				//System.out.println(L_Lift.length());
				for(BiLink a = L_Lift.first(); !a.atEnd(); a = a.next()) {
					instr1.add(getToRegInst((Symbol)a.elem(), 0));
				}
				instr1.add((LirNode)templist1.first().elem());
				nblk1.setInstrList(instr1);
				//add new Symbols to localSymTab
			}
		}
		
		//insert exit
		//System.out.println(exitList.length() + " exits.");
		for(BiLink p = exitList.first(); !p.atEnd(); p = p.next()) {
			BasicBlk eblk = (BasicBlk)p.elem();
			BasicBlk nblk = cfg.insertNewBlkBefore(eblk);
			BiList templist2 = (BiList)nblk.instrList();
			BiList pList = eblk.predList();
			for(BiLink q = pList.first(); !q.atEnd(); q = q.next()) {
				BasicBlk pblk = (BasicBlk)q.elem();
				if(member.contains(pblk)) {
					pblk.replaceSucc(eblk, nblk);										
					//insert instructions
					BiList instr = new BiList();
					for(BiLink y = L_Lift.first(); !y.atEnd(); y = y.next()) {
						instr.add(getToRegInst((Symbol)y.elem(), 1));
					}
					instr.add((LirNode)templist2.first().elem());
					nblk.setInstrList(instr); 
				}
			}
		}
	}
	
	// return RegtoMem(0) or MemtoReg(1)
	public LirNode getToRegInst(Symbol sym, int flag) {
//		System.out.println("getToRegInst = " + sym);
		LirNode setInst = null;
		LirNode sttc = f.newLir.symRef(sym);
		LirNode reg = null;
//		System.out.println("LirNode = " + sttc);
		sttc = f.newLir.node(Op.MEM, sym.type, sttc);	
		for(BiLink p = DSlist.first(); !p.atEnd(); p = p.next()) {
			DobSym ds = (DobSym)p.elem();
			//System.out.println("ds.sym1 = " + ds.sym1);
			if(ds.sym1 == sym) {
				//System.out.println("reginst ok, sym2 = " + ds.sym2);
				reg = f.newLir.symRef(ds.sym2);
				break;
			}
		}
		//System.out.println("target sym is " + sym.id + "in new blk");
		if(flag == 0) {
			setInst = f.newLir.node(Op.SET, sym.type, reg, sttc);
		} 
		else{
			setInst = f.newLir.node(Op.SET, sym.type, sttc, reg);			
		}
		//System.out.println("getToRegInst end : " + setInst);
		return setInst;
	}

	public void PreCTR() {
//		System.out.println("PreCTR");
		//DSlist = dob;
		for(BiLink q = member.first(); !q.atEnd(); q = q.next()) {
			BasicBlk blk = (BasicBlk)q.elem();
			for(BiLink p = blk.instrList().first(); !p.atEnd(); p = p.next()) {
				changeToReg((LirNode)p.elem());
			}
		}
	}
	public void changeToReg(LirNode node) {		
//		System.out.println("changeToReg = " + node);
//		System.out.println("LirNode = " + node);
		int n = node.nKids();
		for(int i = 0; i < n; i++) {
			LirNode knode = node.kid(i);
//			System.out.println("knode = " + knode);
			if(knode.opCode == Op.MEM) {
				LirNode mem = knode.kid(0);
				if(mem.opCode == Op.STATIC) {
					Symbol sym = ((LirSymRef)mem).symbol;
					if(L_Lift.contains(sym)) {
						for(BiLink p = DSlist.first(); !p.atEnd(); p = p.next()) {
							DobSym ds = (DobSym)p.elem();
							if(ds.sym1 == sym) {
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
	
	public static boolean isRegisterType(int type) {
		//Except type == 0
		if(type == 0){
			return false;
		}
		else{
			return Type.tag(type) != Type.AGGREGATE;
		}
	}
//	public static boolean isRegisterType(int type) {
//		//System.out.println("isProperType : " + type);
//		switch(type) {
//		case 130:
//			//char
//		case 258:
//			//short
//		case 514:
//			//int
//		case 516:
//			//float
//		case 1026:
//			//long,pointer
//		case 1028:
//			//double
//			return true;
//		default:
//			return false;
//		}
//	}

}

//Exist in Regpromote.java
//class DobSym {	
//	Symbol sym1;
//	Symbol sym2;
//	public DobSym(Symbol s1, Symbol s2) {
//		sym1=s1;
//		sym2=s2;
//	}
//}
