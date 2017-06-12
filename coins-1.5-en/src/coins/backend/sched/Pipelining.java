/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sched;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import coins.backend.Op;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.sym.Label;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;

public class Pipelining {
	Schedule scheduler;
	LirFactory lir;
	FlowGraph flowGraph;
	BasicBlk basicBlk;
	DependGraph dependGraph;
	ResourceTable table;
	ScheduleInfo info;
	int[] latencyArray;
	
	public Pipelining(Schedule scheduler){
		this.scheduler = scheduler;
		this.lir = scheduler.lir;
	}
	
	class PairIndex{
		int stage;
		int index;
		PairIndex(int s, int i){
			stage = s;
			index = i;
		}
		
		PairIndex add(int i){
		  PairIndex result = new PairIndex(stage, index);
  		  result.index += i;
  		  if (result.index >= table.size){
  			  result.index -= table.size;
  			  result.stage++;
  			  if (result.index >= table.size)
  				  result.index = table.size - 1;
  		  }
  		  return result;
		}
		boolean equals(PairIndex other){
			return stage == other.stage && index == other.index;
		}
		boolean lessThan(PairIndex other){
  		  return stage < other.stage ||
  		      (stage == other.stage && index < other.index);
  	    }
  	    boolean greaterThan(PairIndex other){
  		  return stage > other.stage ||
  		      (stage == other.stage && index > other.index);
  	    }
		
  	  PairIndex incrementIndex(){
		  index++;
		  if (index >= table.size){
			  index = 0;
			  stage++;
		  }
		  return this;
	  }
	  PairIndex incrementStage(){
		  stage++;
		  return this;
	  }
	  PairIndex decrementIndex(){
		  index--;
		  if (index < 0){
			  index = table.size - 1;
			  stage--;
		  }
		  return this;
	  }
	  PairIndex copy(){
		  return new PairIndex(stage, index);
	  }
	  int distanceFrom(PairIndex p){
		  return (stage - p.stage) * table.size + (index - p.index);
	  }
	  
	  public String toString(){
			return "stage:"+stage+", index:"+index;
	  }
	}
	
	class ScheduledNode {
		DependNode node;
		PairIndex pIndex;
		ArrayList reasons; // dNode became schedulable by these reasons
		
		ScheduledNode(DependNode dn){
			node = dn;
			pIndex = null;
			reasons = new ArrayList();
		}
		ScheduledNode(DependNode dn, PairIndex pi){
			node = dn;
			pIndex = pi;
			reasons = new ArrayList();
		}
		ScheduledNode(DependNode dn, int stage, int index){
			node = dn;
			pIndex = new PairIndex(stage, index);
			reasons = new ArrayList();
		}
		
		void setPairIndex(int stage, int index){
			pIndex = new PairIndex(stage, index);
		}
		void setPairIndex(PairIndex pi){
			pIndex = pi;
		}
		
		boolean hasReasonWithTrueDepend() {
	        for (int i = 0; i < reasons.size(); i++) {
	        	Reason r = (Reason) reasons.get(i);
	          if (r.isTrueDepend) {
	            return true;
	          }
	        }
	        return false;
	    }
		
		public String toString(){
			StringBuffer bf = new StringBuffer("scheduled node:");
			if (pIndex == null)
				bf.append("not scheduled"+ "\n" + node.toString());
			else
				bf.append(pIndex.toString() + "\n" + node.toString());
			bf.append("\nreasons\n");
			for (int i = 0; i < reasons.size(); i++){
				bf.append(((Reason)reasons.get(i)).toString() + "\n");
			}
			return bf.toString();
		}
	}
	
	class Reason {
		ScheduledNode node;
		boolean isTrueDepend;
		Reason(ScheduledNode sn, boolean isT){
			node = sn;
			isTrueDepend = isT;
		}
		public String toString(){
			String trueOrFalse = "false on ";
			if (isTrueDepend)
				trueOrFalse = "true on ";
			return trueOrFalse + node.pIndex.toString();
		}
	}
	
	class ResourceTable {
		ArrayList table;
		int size;
		
		ResourceTable(int length) {
	        table = new ArrayList(length);
	        for(int i = 0; i < length; i++){
	          table.add(null);
	        }
	        size = length;
	    }
		
		ScheduledNode get(int index) {
	        return (ScheduledNode) table.get(index);
	    }

	    void set(ScheduledNode sn) {
	        table.set(sn.pIndex.index, sn);
	    }
	    
	    void set(ScheduledNode sn, int stage, int index) {
	    		sn.setPairIndex(stage, index);
	        table.set(index, sn);
	    }
	    void set(ScheduledNode sn, PairIndex pi) {
    		sn.setPairIndex(pi);
        table.set(pi.index, sn);
    }
	    
	    void add(ScheduledNode sn, PairIndex pi){
    			table.add(pi.index, sn);
    			sn.setPairIndex(pi);
    			size = table.size();
    			for (int i = pi.index; i < size; i++){
    				ScheduledNode sni = (ScheduledNode)table.get(i);
    				if (sni != null){
    					sni.pIndex.index = i;
    				}
    			}
	    }
	    
	 int setWithReason(ScheduledNode sn) {
        ArrayList list = sn.reasons;
        int latency = 0;

        if ( ! sn.hasReasonWithTrueDepend()) {
          return setWithFalseDepend(sn);
        }

        Reason r = (Reason) list.get(0);
        PairIndex baseMin = r.node.pIndex;
        PairIndex baseMax = baseMin;
        if (r.isTrueDepend)
          latency = r.node.node.latency;
        else
        	  latency = 1;
        PairIndex maxPair = baseMax.add(latency);
        for (int i = 1; i < list.size(); i++) {
          r = (Reason) list.get(i);
          PairIndex current = new PairIndex(r.node.pIndex.stage, r.node.pIndex.index);
          if (current.lessThan(baseMin) ){
        	    baseMin = current;
          }
          else if (current.greaterThan(baseMax)){
        	    baseMax = current;
          }
          if (r.isTrueDepend)
        	    latency = r.node.node.latency;
          else
        	    latency = 1;
          current = current.add(latency);
          if (current.greaterThan(maxPair)){
        	    maxPair = current;
          }
        }
        
        int range = baseMax.distanceFrom(baseMin);
        if (range < 0 || range >= size - 1){
        	  if (range == size - 1){
        		  add(sn, baseMax.incrementIndex());
        		  return baseMax.index;
        	  }
        	  else
        		  throw new PipeliningException("too large dependent range "+range+sn.toString());
        }
        PairIndex lastPair = baseMin.copy().incrementStage();
        PairIndex start = maxPair;
        if (lastPair.equals(maxPair)){
        	this.add(sn, maxPair);
        	return maxPair.index;
        }
        if (lastPair.lessThan(maxPair)){
        	  start = lastPair.copy().decrementIndex();
        }
        PairIndex current = start.copy();
        ScheduledNode currentNode;
        while(current.lessThan(lastPair)){
        	  if((currentNode = (ScheduledNode)table.get(current.index)) == null){
        		  this.set(sn, current);
        		  return current.index;
        	  }
        	  current.incrementIndex();
        }
        current = start.copy().decrementIndex();
        while(current.greaterThan(baseMax)){
        	  if((currentNode = (ScheduledNode)table.get(current.index)) == null){
        		  this.set(sn, current);
        		  return current.index;
        	  }
        	  current.decrementIndex();
        }
		this.add(sn, start);
		return start.index;
      }

      int setWithFalseDepend(ScheduledNode sn) {
    	     ArrayList list = sn.reasons;
    	     Reason r = (Reason)list.get(0);
    	     PairIndex baseMin = r.node.pIndex;
    	     PairIndex baseMax = baseMin;
	     for (int i = 1; i < list.size(); i++) {
	          r = (Reason) list.get(i);
	          PairIndex current = new PairIndex(r.node.pIndex.stage, r.node.pIndex.index);
	          if (current.lessThan(baseMin) ){
	        	    baseMin = current;
	          }
	          else if (current.greaterThan(baseMax)){
	        	    baseMax = current;
	          }
	      }
	      int range = baseMax.distanceFrom(baseMin);
	      if (range < 0 || range >= table.size() - 1){
	    	    if (range == size)
        		  add(sn, baseMax.incrementIndex());
        	    else
        		  throw new PipeliningException("too large dependent range "+range+sn.toString());
	      }
          PairIndex lastPair = baseMin.copy().incrementStage();
          PairIndex start = baseMax.add(1);
          PairIndex current = start.copy();
          ScheduledNode currentNode;
	      while(current.lessThan(lastPair)){
	    	    if((currentNode = (ScheduledNode)table.get(current.index)) == null){
        		  this.set(sn, current);
        		  return current.index;
        	    }
        	    current.incrementIndex();
	      }
	      this.add(sn, start);
		  return start.index;
      }
      
      int getMaxStage(){
	        int max = 0;
	        for(int i = 0; i < size; i++){
	          ScheduledNode sn = (ScheduledNode)table.get(i);
	          if(sn != null && max < sn.pIndex.stage){
	            max = sn.pIndex.stage;
	          }
	        }
	        return max;
	  }
	    
	    public String toString(){
    	  		StringBuffer sb = new StringBuffer("\n-----------ResourceTable-------\n");
    	  		for(int i = 0; i < table.size(); i++){
    	  			ScheduledNode sn = (ScheduledNode)table.get(i);
    	  			if (sn != null)
    	  				sb.append("\n"+sn.toString());
    	  		}
    	  		return sb.toString();
	    }
	}
	
	class PipeliningException extends RuntimeException{
		PipeliningException(String s){
			super(s);
		}
	}
	
	boolean pipelining(FlowGraph fGraph, BasicBlk bBlk, DependGraph dg, BiList instLINE){
		if ( ! hasLargeLatency(bBlk) )
			return false;
		flowGraph = fGraph;
		basicBlk = bBlk;
		dependGraph = dg;
		try{
			scheduleBranchAndDepend();
			ArrayList selectedList = selectFromSchedulable();
			scheduleNodesOfSelectedList(selectedList);
			scheduleDependentNode();
			scheduleRemainingSchedulableNodes();

			if (scheduler.root.traceOK("TMD", 1)){
				scheduler.debOut.println("\n-------loop kernel--------\n");
				scheduler.debOut.println(table.toString());
			}
		    if (table.getMaxStage() == 0){
		    	   	return false;
		    }
		    constructPipelinedBlocks(instLINE);
		}
		catch (PipeliningException pe){
			if (scheduler.root.traceOK("TMD", 1)){
				scheduler.debOut.println(pe);
				scheduler.debOut.println(dg);
			}
			return false;
		}
		return true;
	}
	
	boolean hasLargeLatency(BasicBlk bBlk){
		for (BiLink q = bBlk.instrList().first(); !q.atEnd(); q = q.next()) {
	        LirNode ins = (LirNode)q.elem();
	        ImList info = scheduler.codeGen.codeInfo(ins);
	        int cost = ((Integer)info.elem2nd()).intValue();
	        if (LirNodeInf.LOAD_LATENCY <= cost){
	          return true;
	        }
	    }
		return false;
	}
	
	void scheduleBranchAndDepend(){
		int listLength = dependGraph.schedulable.length() + dependGraph.unSchedulable.length();
		info = new ScheduleInfo(dependGraph.schedulable, dependGraph.unSchedulable);
	
		DependNode lastBranch = dependGraph.lastBranch;
		
		// compute dependence
	    BiList list_1 = new BiList();
	    info.addAllDependTo(list_1, lastBranch);	
	    // list_1 is the list of all dependNodes on which the lastBranch depends
	    list_1.sort();
	    Object[] sortedArray = list_1.toArray();
	    latencyArray = new int[sortedArray.length]; // array of accumulated latency
	    if (list_1.length() > 0) {
	      DependNode dn = (DependNode)sortedArray[latencyArray.length - 1];
	      latencyArray[latencyArray.length - 1] = dn.latency;
	      for (int i = latencyArray.length - 2; i >= 0; i--){ // accumulate latency
	    	     dn = (DependNode)sortedArray[i];
	    	     int iLatency  = 1 + latencyArray[i+1]; // initial of the i-th accumulated latency
	    	     for (int j = i+1; j < latencyArray.length; j++){
	    	    	   DependNode jNode = (DependNode)sortedArray[j];
	    	    	   if (jNode.trueDependOn.contains(dn) &&
	    	    			   dn.latency + latencyArray[j] > iLatency){
	    	    		   iLatency = dn.latency + latencyArray[j]; // max of the i-th accumulated latency
	    	    	   }
	    	     }
	    	     latencyArray[i] = iLatency; 
	      }
	      if (latencyArray[0] >= listLength) 
	    	    listLength = latencyArray[0] + 1;
	    }
	    // make the resource table
	    table = new ResourceTable(listLength);
	    
	    // schedule the last branch and the dependents
	    info.placeAt(lastBranch, 0, listLength - 1);
	    for (int i = latencyArray.length - 1; i >= 0; i--){
	    	DependNode dn = (DependNode)sortedArray[i];
   	       info.placeAt(dn, 0, listLength - 1 - latencyArray[i]);
        }
	}
	
	ArrayList selectFromSchedulable(){
		ArrayList selectedList = new ArrayList();
		for (int i = 0; i < info.schedulable.size(); i++) {
	        //select nodes from schedulable
			ScheduledNode sn = (ScheduledNode) info.schedulable.get(i);
	        DependNode temp = sn.node;
	        if (temp.beDepended.length() > 0)
	        	  selectedList.add(sn);
		}
		return selectedList;
	}
	
	void scheduleNodesOfSelectedList(ArrayList selectedList){
		boolean beginAtMiddle = false;
		if (latencyArray.length > 0)
			beginAtMiddle = latencyArray[0] > table.size / 2;
		int index;
		if (beginAtMiddle)
	        index = table.size / 2;
		else // begin at bottom
			index = table.size - 2;
	    for (int i = 0; i < selectedList.size(); i++) {
	        ScheduledNode temp = (ScheduledNode)selectedList.get(i);
	        if (beginAtMiddle)
	        	   index = info.placeAtOrAfter(temp, index);
		    else
		    	   index = info.placeAtOrBefore(temp, index);
	        info.removeFromSchedulable(temp);
	        info.eraseDependent(temp);
	    }
	}
	
	void scheduleDependentNode(){
	   do { 
		  //work with scheduleFirst
		 while (!info.scheduleFirst.isEmpty()) {
		     ScheduledNode sn = (ScheduledNode) info.scheduleFirst
		              .get(info.scheduleFirst.size() - 1);
		     info.scheduleFirst.remove(sn);
		     table.setWithReason(sn);
		     info.eraseDependent(sn);
		 }

		  //work with scheduleSecond
		  while (!info.scheduleSecond.isEmpty()) {
			  ScheduledNode sn = (ScheduledNode) info.scheduleSecond
		              .get(info.scheduleSecond.size() - 1);
		      info.scheduleSecond.remove(sn);
		      table.setWithFalseDepend(sn);
		      info.eraseDependent(sn);
		  }
	   } while (!info.scheduleFirst.isEmpty());		      
	}
	
	void scheduleRemainingSchedulableNodes(){
		int tableIndex = 0;
	    for (int i = 0; i < info.schedulable.size(); i++){
	    	   ScheduledNode sn = (ScheduledNode)info.schedulable.get(i);
		   tableIndex = info.placeAtOrAfter(sn, tableIndex);
	    }
	}
	
	void constructPipelinedBlocks(BiList instLINE){
	      ArrayList kernel = table.table;
	      
	      LirNode jumpNext = null;

	      BasicBlk[] epilogue = new BasicBlk[table.getMaxStage()];
	      for(int i = epilogue.length-1; i >= 0; i--){
	        if(i == epilogue.length-1){
	          epilogue[i] = flowGraph.insertNewBlkBefore(basicBlk);
	        }
	        else{
	          epilogue[i] = flowGraph.insertNewBlkBefore(epilogue[i+1]);
	        }
	      }
	      
	      BasicBlk loop = flowGraph.insertNewBlkBefore(epilogue[0]);
	      
	      BasicBlk[] prologue = new BasicBlk[table.getMaxStage()];
	      for(int i = prologue.length-1; i >= 0; i--){
	        if(i == prologue.length-1){
	          prologue[i] = flowGraph.insertNewBlkBefore(loop);
	        }
	        else{
	          prologue[i] = flowGraph.insertNewBlkBefore(prologue[i+1]);
	        }
	      }
	      
	      //connect epilogue block and successor
	      BiList succ = basicBlk.succList();
	      for (BiLink suc = succ.first(); !suc.atEnd(); suc = suc.next()) {
	        BasicBlk block = (BasicBlk) suc.elem();
	        if (block != basicBlk) {
	          epilogue[epilogue.length-1].addEdge(block);
	          //is type really 0?
	          jumpNext = lir.node(Op.JUMP, 0, lir.labelRef(block.label()));
	        }
	      }
	      epilogue[epilogue.length-1].removeEdge(basicBlk);
	      basicBlk.clearEdges();
	      flowGraph.basicBlkList.remove(basicBlk);

	      //connect prologue and predeccesor
	      BiList pred = basicBlk.predList();
	      for (BiLink pre = pred.first(); !pre.atEnd(); pre = pre.next()) {
	        BasicBlk block = (BasicBlk) pre.elem();
	        BiList instList = block.instrList();
	        LirNode last = (LirNode) instList.last().elem();
	        if (last.isBranch()) {
	          instList.remove(last);
	          if (last.opCode == Op.JUMP)
	            instList.add(lir.node(Op.JUMP, last.type, lir
	                .labelRef(prologue[0].label())));
	          else if (last.opCode == Op.JUMPC) {
	            Label[] targets = last.getTargets();
	            if (targets[0] == basicBlk.label())
	              instList.add(lir.node(Op.JUMPC, last.type, last
	                  .kid(0), lir.labelRef(prologue[0].label()), lir
	                  .labelRef(targets[1])));
	            else
	              instList.add(lir.node(Op.JUMPC, last.type, last
	                  .kid(0), lir.labelRef(targets[0]), lir.labelRef(prologue[0]
	                  .label())));
	          }
	          block.removeEdge(basicBlk);
	          block.addEdge(prologue[0]);
	        }
	      }

	      BiList inst;
	      
	      // make prologue's instructions from table
	      for(int i = 0; i < prologue.length; i++){
	        inst = new BiList();
	        if (i == 0)
	        	  inst.addAll(instLINE);
	        for(int j = 0; j < table.size; j++){
	          ScheduledNode sn = (ScheduledNode)table.get(j);
	          if (sn != null){
	          if(j == table.size-1){
	            LirNode lastInst = sn.node.instr;
	            LirNode testInst = lastInst.kid(0);
	            if(i == prologue.length-1){
	              inst.add(lir.node(lastInst.opCode, lastInst.type, lir.node(
	                  reverseOp(testInst.opCode), testInst.type, testInst.kid(0),
	                  testInst.kid(1)), lir.labelRef(epilogue[epilogue.length-i-1].label()), lir
	                  .labelRef(loop.label())));
	            }
	            else{
	              inst.add(lir.node(lastInst.opCode, lastInst.type, lir.node(
	                  reverseOp(testInst.opCode), testInst.type, testInst.kid(0),
	                  testInst.kid(1)), lir.labelRef(epilogue[epilogue.length-i-1].label()), lir
	                  .labelRef(prologue[i+1].label())));
	            }
	          }
	          else{
	            if(sn.pIndex.stage <= i){
	              inst.add(getLirNodeFromDependent(sn.node));
	            }
	          }
	          }
	        }
	        prologue[i].setInstrList(inst);
	      }
	      
	      //make loop's instructions from table
	      inst = new BiList();
	      for(int i = 0; i < table.size; i++){
	    	  ScheduledNode sn = (ScheduledNode)table.get(i);
	        if (sn != null){
	        if(i == table.size-1){
	          LirNode lastInst = sn.node.instr;
	          inst.add(lir.node(lastInst.opCode, lastInst.type, lastInst.kid(0), lir
	              .labelRef(loop.label()), lir.labelRef(epilogue[0].label())));
	        }
	        else{
	          inst.add(getLirNodeFromDependent(sn.node));
	        }
	        }
	      }
	      loop.setInstrList(inst);
	      
	      //make epilogue's instructions from table
	      for(int i = 0; i < epilogue.length; i++){
	    	    // epilogue[i] : prologue[epilogue.length - 1 - i]
	    	    // epilogue.length == table.getMaxStage();
	    	    inst = new BiList();
	    	    int prologueIndex = epilogue.length - 1 - i;
	    	    for (int j = prologueIndex + 1; j <= epilogue.length; j++){
	    	    	  for(int k = 0; k < table.size; k++){
	    	    		 ScheduledNode sn = (ScheduledNode)table.get(k);
	  	         if(sn != null && sn.pIndex.stage == j){
	  	           inst.add(getLirNodeFromDependent(sn.node));
	  	         }
	  	       }
	    	    }
	        if (i == epilogue.length-1){
	        	  inst.add(jumpNext);
	        }
	        else{
	        	  inst.add(lir.node(Op.JUMP, 0, lir.labelRef(epilogue[i+1].label())));
	        }
	        epilogue[i].setInstrList(inst);
	      }

	      loop.maintEdges();
	      for(int i = 0; i < table.getMaxStage(); i++){
	        prologue[i].maintEdges();
	        epilogue[i].maintEdges();
	      }

	    
	    scheduler.func.touch();
	}


	    //Information for scheduling
	    class ScheduleInfo {

	      ArrayList scheduleFirst;
	      ArrayList scheduleSecond;
	      ArrayList schedulable;
	      ArrayList unschedulable;
	      ArrayList removed;

	      ScheduleInfo(BiList sch, BiList unsch) {
	        scheduleFirst = new ArrayList();
	        scheduleSecond = new ArrayList();
	        schedulable = new ArrayList();
	        unschedulable = new ArrayList();
	        removed = new ArrayList();
	        for (BiLink i = sch.first(); !i.atEnd(); i = i.next()) {
	          schedulable.add(new ScheduledNode((DependNode) i.elem()));
	        }
	        for (BiLink i = unsch.first(); !i.atEnd(); i = i.next()) {
	          unschedulable.add(new ScheduledNode((DependNode) i.elem()));
	        }
	      }
	      
	      void addAllDependTo(BiList list, DependNode dn){
	    	  	for (BiLink n = dn.trueDependOn.first(); !n.atEnd(); n = n.next()){
	    	  		DependNode dn1 = (DependNode) n.elem();
	    	  		if(!list.contains(dn1)){
	    	  			list.add(dn1);
	    	  			addAllDependTo(list, dn1);
	    	  		}
	    	  	}
	    	  	for (BiLink n = dn.falseDependOn.first(); !n.atEnd(); n = n.next()){
	    	  		DependNode dn1 = (DependNode) n.elem();
	    	  		if(!list.contains(dn1)){
	    	  			list.add(dn1);
	    	  			addAllDependTo(list, dn1);
	    	  		}
	    	  	}
	      }
	      
	      void removeFromSchedulable(ScheduledNode node){
	    	    DependNode dn = node.node;
	        for(int i = 0; i < schedulable.size(); i++){
	        	ScheduledNode sn = (ScheduledNode)schedulable.get(i);
	          if(dn.equals(sn.node)){
	            schedulable.remove(sn);
	            removed.add(sn);
	            return;
	          }
	        }
	      }
	      
	      void removeFromUnschedulable(ScheduledNode node){
	    	    DependNode dn = node.node;
	        for(int i = 0; i < unschedulable.size(); i++){
	        	ScheduledNode sn = (ScheduledNode)unschedulable.get(i);
	          if(dn.equals(sn.node)){
	            unschedulable.remove(sn);
	            removed.add(sn);
	            return;
	          }
	        }
	      }
	      
	      void remove(ScheduledNode node){
	    	  	removeFromUnschedulable(node);
	    	  	removeFromSchedulable(node);
	      }
	      
	      boolean isRemoved(ScheduledNode node){
	    	     for(int i = 0; i < removed.size(); i++){
	    	    	     ScheduledNode sn = (ScheduledNode)removed.get(i);
		          if(node.equals(sn)){
		            return true;
		          }
		     }
	    	     return false;
	      }

	      //algorithm 3-2
	      void eraseDependent(ScheduledNode node) {
	    	    int index = node.pIndex.index;
	    	    DependNode dn = node.node;
	        	for (int i = 0; i < unschedulable.size(); i++ ){
	          ScheduledNode unschTemp = (ScheduledNode) unschedulable.get(i);
	          DependNode unschNode = unschTemp.node;
	          if (unschNode.trueDependOn.contains(dn)) {
	            unschNode.trueDependOn.remove(dn);
	            unschTemp.reasons.add(new Reason(node, true));
	            if (unschNode.trueDependOn.isEmpty()
	                && unschNode.falseDependOn.isEmpty()) {
	              scheduleFirst.add(unschTemp);
	              unschedulable.remove(unschTemp);
	              i--;
	            }
	          }
	          if (unschNode.falseDependOn.contains(dn)) {
	            unschNode.falseDependOn.remove(dn);
	            unschTemp.reasons.add(new Reason(node, false));
	            if (unschNode.trueDependOn.isEmpty()
	                && unschNode.falseDependOn.isEmpty()) {
	              boolean isAdded = false;
	              for (int j = 0; j < unschTemp.reasons.size(); j++) {
	            	  Reason reason = (Reason) unschTemp.reasons.get(j);
	                if (!isAdded && reason.isTrueDepend) {
	                  scheduleFirst.add(unschTemp);
	                  isAdded = true;
	                }
	              }
	              if (!isAdded) {
	                scheduleSecond.add(unschTemp);
	              }
	              unschedulable.remove(unschTemp);
	              i--;
	            }
	          }
	        }
	      }

	      void placeAt(DependNode dn, int stage, int index){
	    	  	 ScheduledNode node = new ScheduledNode(dn, stage, index);
	    	     table.set(node);
	    	     eraseDependent(node);
	    	     remove(node);
	      }
	      
	      int placeAtOrBefore(ScheduledNode sn, int index){
	    	  	int i = index;
	    	  	while(i >= 0 && table.get(i) != null)
    	  			i--;
    	  		if (i < 0){
    	  			throw new PipeliningException("can not placeAtOrBefore "+index);
    	  		}
    	  		table.set(sn, 0, i);
	    	  	return i;
	      }
	      
	      int placeAtOrAfter(ScheduledNode sn, int index){
	    	  	int i = index;
	    	  	while(i < table.size && table.get(i) != null)
  	  			i++;
	    	  	if (i < table.size){
  	  		   table.set(sn, 0, i);
	    	  	   return i;
	    	  	}
	    	  	else
	    	  		return placeAtOrBefore(sn, index);
	      }
	      
	      PairIndex placeAtOrAfter(ScheduledNode sn, PairIndex pi){
	    	    int i = pi.index;
	    	    while (i < table.size && table.get(i) != null)
	    	    	  i++;
	    	    if (i < table.size){
	    	    	   table.set(sn, pi.stage, i);
	    	    	   return new PairIndex(pi.stage, i);
	    	    }
	    	    else
	    	    	   return placeAtOrAfter(sn, new PairIndex(pi.stage+1, 0));
	      }
	      
	      public String toString(){
	    	  	StringBuffer sb = new StringBuffer("\n---------ScheduleInfo---------\n");
	    	  	sb.append("schedulable\n");
	    	  	for (int i = 0;  i < schedulable.size(); i++){
	    	  		ScheduledNode temp = (ScheduledNode) schedulable.get(i);
	    	  		sb.append(temp.toString()+"\n");
	    	  	}
	    	  	sb.append("unschedulable\n");
	    	  	for (int i = 0;  i < unschedulable.size(); i++){
	    	  		ScheduledNode temp = (ScheduledNode) unschedulable.get(i);
	    	  		sb.append(temp.toString()+"\n");
	    	  	}
	    	  	sb.append("scheduleFirst\n");
	    	  	for (int i = 0;  i < scheduleFirst.size(); i++){
	    	  		ScheduledNode temp = (ScheduledNode) scheduleFirst.get(i);
	    	  		sb.append(temp.toString()+"\n");
	    	  	}
	    	  	sb.append("scheduleSecond\n");
	    	  	for (int i = 0;  i < scheduleSecond.size(); i++){
	    	  		ScheduledNode temp = (ScheduledNode) scheduleSecond.get(i);
	    	  		sb.append(temp.toString()+"\n");
	    	  	}
	    	  	return sb.toString();
	      }
	    } 
	  
	    DependGraph reconstructDg(BasicBlk blk){
	    		DependGraph dg = new DependGraph(scheduler.func);
		      BiList instrList = new BiList();

		      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
		        LirNode ins = (LirNode) q.elem();
		        if (ins.opCode == Op.LINE || ins.opCode == Op.INFO){
		        	 instrList.add(ins);
		          continue;
		        }
		        DependNode dn = new DependNode(ins, scheduler);
		        dg.add(dn);

		        switch (ins.opCode) {
		          case Op.PROLOGUE:
		            dn.setLatency(Schedule.MAX_LATENCY);// to be scheduled at first
		            break;

		          case Op.EPILOGUE:
		            break;
		          case Op.JUMP:
		          case Op.JUMPC:
		            dg.hasBranch(dn);
		          default:
		            ImList info = scheduler.codeGen.codeInfo(ins);// generated codes with attached
		            // cost
		            dn.setLatency(((Integer) info.elem2nd()).intValue());// cost of the
		            // codes
		//            dn.setMachineCodeSize(((Integer) info.elem3rd()).intValue());
		//            if (((Boolean) info.elem()).booleanValue())
		//              dn.letHaveDelaySlot();
		            break;
		        }
		      }
		      return dg;
	    }


	 /**
	   * covert DependNode to LirNode
	   * @param node DependNode coverting to LirNode 
	   * @return LirNode providing from DependNode
	   */
	  LirNode getLirNodeFromDependent(DependNode node){
	    LirNode original = node.instr;
	    
	    int n = original.nKids();
	    int opcode = original.opCode;
	    int type = original.type;
	    
	    switch(n){
	      case 0:
	        return lir.node(opcode ,type);
	      case 1:
	        return lir.node(opcode, type, original.kid(0));
	      case 2:
	        return lir.node(opcode, type, original.kid(0), original.kid(1));
	      case 3:
	        return lir.node(opcode, type, original.kid(0), original.kid(1), original.kid(2));
	      default:
	        return null;
	    }
	    
	  }
	  
	  int reverseOp(int op){
		  switch(op){
		  case Op.TSTEQ: return Op.TSTNE;
		  case Op.TSTNE: return Op.TSTEQ;
		  case Op.TSTGES: return Op.TSTLTS;
		  case Op.TSTLTS: return Op.TSTGES;
		  case Op.TSTGEU: return Op.TSTLTU;
		  case Op.TSTLTU: return Op.TSTGEU;
		  case Op.TSTGTS: return Op.TSTLES;
		  case Op.TSTLES: return Op.TSTGTS;
		  case Op.TSTGTU: return Op.TSTLEU;
		  case Op.TSTLEU: return Op.TSTGTU;
		  default: return op;
		  }
	  }
	  
	  /**
	   * get inner-most-loop basic blocks
	   * @param f funcion searching loop block
	   * @return list of nner-most-loop block
	   */
	  BiList getLoopBlks(FlowGraph flowGraph){ 
	    
	    BiList list = new BiList();
	    for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
	      BasicBlk blk=(BasicBlk)p.elem();
	      BiList succ = blk.succList();
	      if (succ.contains(blk))
	    	  	list.add(blk);
	    }
	    return list;
	  }
	    
	  /**
	   * reduce loops unsatisfying conditions
	   * @param list list of loop block
	   * @return list of blocks for pipelining
	   */
	  BiList eliminatingUnnecessaryLoop(BiList list){
	    BiList newList = new BiList();
	    
	    for(BiLink p = list.first(); !p.atEnd(); p = p.next()){
	      BasicBlk blk = (BasicBlk)p.elem();
	      boolean flag = false;
	      for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
	        LirNode ins = (LirNode)q.elem();
	        ImList info = scheduler.codeGen.codeInfo(ins);
	        int cost = ((Integer)info.elem2nd()).intValue();
	        if(ins.opCode == Op.PARALLEL) {
	          flag = false;
	          break;
	        }  
	        else if(LirNodeInf.LOAD_LATENCY <= cost){
	          flag = true;
	        }
	      }
	      if(flag){
	        newList.add(blk);
	      }
	    }     
	    
	    return newList;
	  }

	  /**
	   * pipelining inner-most-loops in flowGraph
	   * (this method rewrites blocks and instructions)
	   * @param flowGraph FlowGraph for pipelining
	   */
	  void pipelining0(FlowGraph flowGraph){
	    
	    BiList list = eliminatingUnnecessaryLoop(getLoopBlks(flowGraph));
	    
	    if(list.isEmpty()){
	      return;
	    }
	    
	    for(BiLink p = list.first(); !p.atEnd(); p = p.next()){
	        
	        //start pipelining for each loop
	        BasicBlk blk = (BasicBlk)p.elem();
	        
	        //      set dependent
	        BiList dependent = new BiList();
	        int max = 0;
	        DependNode maxNode = null;
	        for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
	          LirNode ins = (LirNode)q.elem();
	          DependNode dn = new DependNode(ins, scheduler);
	          ImList info = scheduler.codeGen.codeInfo(ins);
	          int latency = ((Integer)info.elem2nd()).intValue();
	          dn.setLatency(latency);
	          if(max < latency && ins.opCode != Op.JUMPC){
	            maxNode = dn;
	            max = latency;
	          }
	          dependent.add(dn);
	        }
	        
	        for(BiLink i = dependent.first(); !i.atEnd(); i = i.next()){
	          for(BiLink j = i.next(); !j.atEnd(); j = j.next()){
	            ((DependNode)j.elem()).dependOn((DependNode)i.elem());
	          }
	        }
	        
	        if(maxNode != null){
	            //set prologue
	            BiList prologueList = maxNode.dependOn(dependent);
	            prologueList.add(maxNode);

	            //set branch
	            DependNode lastNode = (DependNode)dependent.last().elem();
	            BiList branchList = new BiList();
	 
	            for (BiLink i = lastNode.dependOn(dependent).first(); !i.atEnd(); i = i.next()){
	            	 if (!prologueList.contains(i.elem()))
	            		 branchList.add(i.elem());
	            }
	            branchList.add(lastNode); 
	            
	            prologueList.concatenate(branchList.copy());
	                        
	            //set epilogue
	            BiList epilogueList = new BiList();
	            for(BiLink i = dependent.first(); !i.atEnd(); i = i.next()){
	              DependNode temp = (DependNode)i.elem();
	              if(!prologueList.contains(temp) && !branchList.contains(temp)){
	                epilogueList.add(temp);
	              }
	            }
	            
	            if (epilogueList.length() == 0)
	            	 continue; // no-pipelining
	            
	            BiList loopList = epilogueList.copy();
	            loopList.concatenate(prologueList.copy());
	            
	            // last instr of epilogue
	            LirNode lastInstr = null;
	            
	            //preparing basic block
	            BasicBlk epilogue = flowGraph.insertNewBlkBefore(blk);
	            
	            BasicBlk loopKernel = flowGraph.insertNewBlkBefore(epilogue);
	            BasicBlk prologue = flowGraph.insertNewBlkBefore(loopKernel);
	            
	            
	            //connect epilogue block and successor
	            BiList succ = blk.succList();
	            for(BiLink suc = succ.first(); !suc.atEnd(); suc = suc.next()){
	              BasicBlk block = (BasicBlk)suc.elem();
	              if(block != blk){
	                epilogue.addEdge(block);
	                //is type really 0?
	                lastInstr = lir.node(Op.JUMP, 0, lir.labelRef(block.label()));
	              }
	            }
	            epilogue.removeEdge(blk);
	            blk.clearEdges();
	            flowGraph.basicBlkList.remove(blk);
	            
	            //connect prologue and predeccesor
	            BiList pred = blk.predList();
	            for(BiLink pre = pred.first(); !pre.atEnd(); pre = pre.next()){
	              BasicBlk block = (BasicBlk)pre.elem();
	              BiList instList = block.instrList();
	              LirNode lastBranch = (LirNode)instList.last().elem();
	              if(lastBranch.isBranch()){
	                instList.remove(lastBranch);
	                if (lastBranch.opCode == Op.JUMP)
	                	instList.add(lir.node(Op.JUMP, lastBranch.type, lir.labelRef(prologue.label())));
	                else if (lastBranch.opCode == Op.JUMPC){
	                	Label[] targets = lastBranch.getTargets();
	                	if (targets[0] == blk.label())
	                		instList.add(lir.node(Op.JUMPC, lastBranch.type, lastBranch.kid(0),
	                				lir.labelRef(prologue.label()), lir.labelRef(targets[1])));
	                	else
	                		instList.add(lir.node(Op.JUMPC, lastBranch.type, lastBranch.kid(0),
	                				lir.labelRef(targets[0]), lir.labelRef(prologue.label())));
	                }               	
	                block.removeEdge(blk);
	                //block.addEdge(prologue);  //error
	              }
	            }

	            
	            // make loop kernel's instructions from depend nodes 
	            BiList newLoop = new BiList();
	            for(BiLink i = loopList.first(); !i.atEnd(); i = i.next()){
	              DependNode temp = (DependNode)i.elem();
	              if(i == loopList.last()){
	                newLoop.add(lir.node(temp.instr.opCode, temp.instr.type, temp.instr.kid(0), 
	                		lir.labelRef(loopKernel.label()), lir.labelRef(epilogue.label())));
	              }
	              else{
	                newLoop.add(getLirNodeFromDependent(temp));
	              }
	            }
	            
	            //make prologue's instructions from depend nodes
	            BiList newPrologue = new BiList();
	            for(BiLink i = prologueList.first(); !i.atEnd(); i = i.next()){
	              DependNode temp = (DependNode)i.elem();
	              if(i == prologueList.last()){       	  
	               LirNode lastInst = temp.instr;              
	               LirNode testInst = lastInst.kid(0);
	            	  newPrologue.add(lir.node(lastInst.opCode, lastInst.type, 
	            			  lir.node(reverseOp(testInst.opCode), testInst.type, testInst.kid(0), testInst.kid(1)), 
	                       lir.labelRef(epilogue.label()), lir.labelRef(loopKernel.label()))); 
	              }
	              else{
	                newPrologue.add(getLirNodeFromDependent(temp));
	              }
	            }
	            
	            // make epilogue's instructions from depend nodes
	            BiList newEpilogue = new BiList();
	            for(BiLink i = epilogueList.first(); !i.atEnd(); i = i.next()){
	              DependNode temp = (DependNode)i.elem();
	              newEpilogue.add(getLirNodeFromDependent(temp));
	            }
	            newEpilogue.add(lastInstr);
	            
	            //set instructions
	            loopKernel.setInstrList(newLoop);
	            prologue.setInstrList(newPrologue);
	            epilogue.setInstrList(newEpilogue);
	            
	            loopKernel.maintEdges();
	            prologue.maintEdges();
	            epilogue.maintEdges();
	        }
	    }
	    scheduler.func.touch();
	    
	  }

}
