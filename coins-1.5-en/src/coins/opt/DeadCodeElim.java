/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * DeadCodeElim.java
 *
 * Created on August 27, 2002, 4:30 PM
 */

package coins.opt;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set; //##63

//##63 import coins.aflow.BBlock;
import coins.flow.BBlock; //##63
//##63 import coins.aflow.Flow;
import coins.flow.Flow; //##63
//##63 import coins.aflow.FlowAnalSymVector;
import coins.flow.FlowAnalSymVector; //##63
import coins.aflow.FlowResults;
//##63 import coins.aflow.FlowUtil;
import coins.flow.FlowUtil; //##63
//##63 import coins.aflow.NodeIterator;
import coins.flow.NodeIterator; //##63
//##63 import coins.aflow.SetRefRepr;
import coins.flow.SetRefRepr; //##63
//##63 import coins.aflow.SetRefReprIterator;
import coins.flow.SetRefReprHirImpl; //##65
//##63 import coins.aflow.SetRefReprList;
import coins.flow.SetRefReprList; //##63
//##63 import coins.aflow.SubpFlow;
import coins.flow.SubpFlow; //##63
//##63 import coins.aflow.util.FAList;
//##97 import coins.flow.FAList; //##63
import coins.ir.IR;
import coins.ir.hir.AssignStmt; //##65
import coins.ir.hir.HIR;
import coins.ir.hir.Stmt;  //##97
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.Var; //##63

/**
 * Dead code elimination class.
 *
 * @author  hasegawa
 */
public class DeadCodeElim
{
  final FlowResults fResults;
  public final Flow flow;
  public final Opt opt;
  protected SubpFlow fSubpFlow; //##63
  protected int fDbgLevel; //##65

  /** Creates a new instance of DeadCodeElim */
  //##64 DeadCodeElim(FlowResults pResults)
  DeadCodeElim(FlowResults pResults, Opt pOpt) //##64
  {
      fResults = pResults;
      flow = fResults.flowRoot.flow;
      //##64 opt = new Opt(fResults.flowRoot);
      opt = pOpt; //##64
  }

  /**
   * Performs dead code elimination for the given SubpFlow. Only checks each BBlock once.
   */
  public boolean doSubp(SubpFlow pSubpFlow)
  {
    boolean lOptimized = false;
    fSubpFlow = pSubpFlow; //##63
    fDbgLevel = fResults.flowRoot.ioRoot.dbgOpt1.getLevel(); //##65
    //##97 flow.dbg(1, "deadcode elim", fSubpFlow.getSubpSym().getName()); //##78
    opt.dbg(1, "deadcode elim", fSubpFlow.getSubpSym().getName());  //##97
    //##63 for (Iterator lIt = pSubpFlow.getReachableBBlocks().iterator();
    for (Iterator lIt = pSubpFlow.getListOfBBlocksFromEntry().iterator(); //##63
         lIt.hasNext(); ) {
      lOptimized |= doBBlock((BBlock)lIt.next());
    }
    return lOptimized;
  }

  /**
   * Perform dead code elimination for the given BBlock.
   */
  public boolean doBBlock(BBlock pBBlock)
  {
    if ((pBBlock == null)||(pBBlock.getBBlockNumber() == 0))
      return false; //##63
    //##97 flow.dbg(3, "\nBBlock " + pBBlock.getBBlockNumber()); //##78
    opt.dbg(3, "\nBBlock", " " + pBBlock.getBBlockNumber()); //##97
    //##63 SetRefReprList lSetRefReprs = pBBlock.getSetRefReprs();
    SetRefReprList lSetRefReprs = fSubpFlow.getSetRefReprList(pBBlock); //##63
    SetRefRepr lSetRefRepr;
    SubpFlow lSubpFlow = pBBlock.getSubpFlow();
    //##63 FlowAnalSymVector lLive = flow.flowAnalSymVector(lSubpFlow);
    FlowAnalSymVector lLive = lSubpFlow.flowAnalSymVector(); //##63
    //##63 FlowAnalSymVector lFullVect = flow.flowAnalSymVector(lSubpFlow);
    FlowAnalSymVector lFullVect = lSubpFlow.flowAnalSymVector(); //##63
    lFullVect.vectorNot(lFullVect);
    FlowAnalSymVector lUsed = pBBlock.getUsed(); //##78
    Set lUsedSyms = lUsed.flowAnalSyms(); //##78
    //##63 pBBlock.getPLiveOut().vectorCopy(lLive);
    pBBlock.getLiveOut().vectorCopy(lLive); //##63
    Set lLiveSet = new java.util.HashSet(); //##97
    lLiveSet.addAll(lLive.flowAnalSyms());  //##97
    //##97 flow.dbg(4, "LiveOut", lLive.toStringDescriptive()); //##78
    opt.dbg(4, "LiveOut", lLive.toStringDescriptive()); //##97
    opt.dbg(4, "LiveSet", lLiveSet.toString()); //##97
    //##97 flow.dbg(4, "Used", lUsedSyms.toString());   //##78
    opt.dbg(4, "Used", lUsedSyms.toString());   //##97
    IR lIR;
    IR lCallNode = null;
    boolean lHasCall;
    //##63 FAList lSymIndexTable = lSubpFlow.getSymIndexTable();
    Set lSetOfUsedSyms = lSubpFlow.getUsedSyms(); //##63
    boolean lOptimized = false;
    FlowAnalSym lFlowAnalSym;
    //##63 final FlowAnalSymVector lExternallyReferrableVars = flow.flowAnalSymVector(lSubpFlow);
    //##97 final FlowAnalSymVector lExternallyReferrableVars = lSubpFlow.
    //##97   flowAnalSymVector(); //##63
    //##97 LinkedList FlowAnalList;
    //##97 FlowAnalList = new LinkedList();
    Set lUsedSymsInNonDeletedStmt = new java.util.HashSet();  //##97

    //##63 for (int i = 1; i <= lSubpFlow.getSymIndexTable().size(); i++)
    //##63     lExternallyReferrableVars.setBit(i);
    //##63 BEGIN
    //##97 for (Iterator lIterator = lSetOfUsedSyms.iterator();
    //##97      lIterator.hasNext(); ) {
    //##97   Sym lSym = (Sym)lIterator.next();
    //##97   lExternallyReferrableVars.setBit(((FlowAnalSym)lSym).getIndex());
    //##97 }
    //##63 END
    //##63 for (SetRefReprIterator lSetRefReprIt
    //##63      = lSetRefReprs.setRefReprIterator(lSetRefReprs.size());
    //##63      lSetRefReprIt.hasPrevious();)
    for (Iterator lSetRefReprIt = lSetRefReprs.reverseIterator(); //##63
         lSetRefReprIt.hasNext(); ) { //##63
      //##63 lSetRefRepr = (SetRefRepr)lSetRefReprIt.previous();
      lSetRefRepr = (SetRefRepr)lSetRefReprIt.next(); //##63
      HIR lDefNode = (HIR)lSetRefRepr.defNode(); //##65
      lHasCall = lSetRefRepr.hasCallWithSideEffect(); //##71
      boolean lMayWriteToExternAddress = mayWriteToExternalAddress(lSetRefRepr); //##97
      Stmt lStmt = ((SetRefReprHirImpl)lSetRefRepr).getStmt();
      if ((fDbgLevel > 1)&&(lDefNode != null)) {
        //##97 flow.dbg(4, "SetRefRepr", "Def " + lDefNode.toStringShort()
    	opt.dbg(4, "SetRefRepr", "Def " + lDefNode.toStringShort()  //##97
                 + " hasCall " + lHasCall + " sets " + lSetRefRepr.sets()
                 + " topUseNode " + lSetRefRepr.topUseNode().toStringShort()
                 + " extrern " + lMayWriteToExternAddress //##97
                 + " use " + lSetRefRepr.useSyms()        //##97
                 + " IR " + lSetRefRepr.getIR()           //##97
                 ); //##78
    	if (lStmt != null)                                //##97
    	  opt.dbg(4, " Containing Stmt", lStmt.toStringShort()); //##97
      }
      if (lSetRefRepr.sets()) {
    	// If this is Assign and LHS is the same as RHS then delete this statement ? //##97
        //##63 if (FlowUtil.isSameTree(lSetRefRepr.defNode(), lSetRefRepr.topUseNode(), fResults))
        if (((HIR)lSetRefRepr.defNode()).isSameAs((HIR)lSetRefRepr.topUseNode()))  //##63
        { //##63
          opt.dbg(4, "LHS=RHS for ", lDefNode.toStringShort()); //##97
          //##65 if (!lHasCall)
          //##97 if ((!lHasCall)&&(lDefNode instanceof AssignStmt)) //##65
          if ((!lHasCall)&&(((SetRefReprHirImpl)lSetRefRepr).getStmt() //##97
        		  instanceof AssignStmt))                              //##97
          {
            if (fDbgLevel >= 3)
              //##97 flow.dbg(3, "deleteStmt", lDefNode.toStringShort()); //##65
              opt.dbg(3, "deleteStmt", lDefNode.toStringShort()); //##97
            OptUtil.deleteStmt(((SetRefReprHirImpl)lSetRefRepr).getStmt()); //##65
            lSetRefReprIt.remove();
            /* //##65
            if (opt.shouldTrace(3)) {
              lSubpFlow.getSubpDefinition().printHir("Dead code elim");
            }
              */ //##65
            lOptimized = true;
            continue;
          }
        }
        FlowAnalSym lDefSym = (FlowAnalSym)lSetRefRepr.defSym();

        //##63 FlowAnalSymVector lIntersection = flow.flowAnalSymVector(lSubpFlow);
        //##97 FlowAnalSymVector lIntersection = lSubpFlow.flowAnalSymVector(); //##63
        //##63 lSetRefRepr.getPDefined().vectorAnd(lLive, lIntersection);
        //##97 pBBlock.getDefined().vectorAnd(lLive, lIntersection); //##63
        Set lIntersectionSet = new java.util.HashSet(); //##97
        lIntersectionSet.addAll(lSetRefRepr.modSyms()); //##97
        lIntersectionSet.retainAll(lLiveSet);           //##97
        opt.dbg(4, "IntersectionSet", lIntersectionSet.toString()); //##97
        //##97 if (lIntersection.isZero() && !mayWriteToExternalAddress(lSetRefRepr) &&
        if (lIntersectionSet.isEmpty() && !lMayWriteToExternAddress && //##97
            !lHasCall) {
          // The modified variables do not live-out. //##97
          // The assign statement may be deleted.    //##97
          boolean remove;
          remove = true;
          for (NodeIterator lNodeIt =
               FlowUtil.nodeListIterator(lSetRefRepr.getIR(), true, false);
               lNodeIt.hasNext(); ) {
            HIR node = (HIR)lNodeIt.next();
            if (fDbgLevel >= 4)
              opt.dbg(5, " node", node.toStringShort());  //##97
            //##63 if  (FlowUtil.flowAnalSym(node) != null)  {
            if (node.getSym()instanceof FlowAnalSym) { //##63
              lFlowAnalSym = (FlowAnalSym)node.getSym();
              if (fDbgLevel >= 3)
                //##97 flow.dbg(5, "symNode", node.toStringShort()); //##78
                opt.dbg(5, "symNode", node.toStringShort() + " address taken "
                		 + lFlowAnalSym.getFlag(Sym.FLAG_ADDRESS_TAKEN)); //##97
              if (lFlowAnalSym.getFlag(Sym.FLAG_ADDRESS_TAKEN) == true) {
                remove = false;
                break;
              }
              //##63 if(FlowUtil.IsVarSyms((Sym)lFlowAnalSym) == false) {
              if (!((lFlowAnalSym instanceof Var) && //##63
                    lFlowAnalSym.getSymType().isScalar())) { //##63
                remove = false;
                break;
              }
               //##97 if (FlowAnalList.contains(lFlowAnalSym) == true) 
              if (lUsedSymsInNonDeletedStmt.contains(lFlowAnalSym)) //##97
              {
                  remove = false;  // This symbol is used later.
                  break;
              }
              if (lFlowAnalSym.isGlobal() == true) {
                remove = false;
                break;
              }
              //##78 BEGIN
              //##97 if (lUsedSyms.contains(lFlowAnalSym)) {
              //##97   // REFINE (see DefUseList)
              //##97   remove = false;
              //##97   break;
              //##97 }
              //##78 END
            }
          } // for (NodeIterator ..)

          //##65 if (remove == true)
          if (remove &&
              //##97 (((SetRefReprHirImpl)lSetRefRepr).getStmt() instanceof AssignStmt)) //##65
              (lStmt instanceof AssignStmt)) //##97
          {
            if (fDbgLevel >= 3) //##65
              //##97 flow.dbg(3, "removeStmt",
              opt.dbg(3, "removeStmt",  //##97
               ((SetRefReprHirImpl)lSetRefRepr).getStmt().toStringShort()); //##65
            OptUtil.deleteStmt(((SetRefReprHirImpl)lSetRefRepr).getStmt()); //##65
            lSetRefReprIt.remove();
            lOptimized = true; //##65
          } // if(remove ..)
          else {
            lUsedSymsInNonDeletedStmt.addAll(lSetRefRepr.useSyms()); //##97
            if (fDbgLevel > 3)                           //##97
                opt.dbg(4, "Sym used later", lSetRefRepr.useSyms()); //##97
       	    /** //##97
            for (NodeIterator lNodeIt =
                 FlowUtil.nodeListIterator(lSetRefRepr.getIR(), true, false);
                 lNodeIt.hasNext(); ) {
              HIR node = (HIR)lNodeIt.next();
              //##63 if  (FlowUtil.flowAnalSym(node) != null)  {
              if (node.getSym() instanceof FlowAnalSym) { //##63
                lFlowAnalSym = (FlowAnalSym)node.getSym();
                //##97 FlowAnalList.add(lFlowAnalSym);
                lUsedSymsInNonDeletedStmt.add(lFlowAnalSym); //##97
              }
            }
       	    **/ //##97
          }
          /* //##65
          if (opt.shouldTrace(3)) {
            lSubpFlow.getSubpDefinition().printHir("Dead code elim");
          }
            */ //##65
          //##65 lOptimized = true;
        }  // if (lIntersectionSet.isEmpty() .. ) 
        else {
          if (lDefSym != null) {
            //##63 lLive.resetBit(lSymIndexTable.indexOf(lDefSym));
            lLive.resetBit(lDefSym.getIndex()); //##63
            //##63 lLive.vectorOr(lSetRefRepr.getPExposed(), lLive);
          }
          lLive.vectorOr(pBBlock.getExposed(), lLive); //##63
          lUsedSymsInNonDeletedStmt.addAll(lSetRefRepr.useSyms()); //##97
        }
      } //  if (lSetRefRepr.sets())
      else if (!lSetRefRepr.hasControl() && !lSetRefRepr.isReturn() &&
               //##65 !lHasCall)
               !lHasCall &&
               (((SetRefReprHirImpl)lSetRefRepr).getStmt() instanceof AssignStmt)) //##65
      {
        OptUtil.deleteStmt(((SetRefReprHirImpl)lSetRefRepr).getStmt()); //##65
        lSetRefReprIt.remove();
//         for (NodeIterator lNodeIt = FlowUtil.nodeListIterator(lSetRefRepr.getIR(), true, false); lNodeIt.hasNext();) // Assuming left-to-right evaluation order
//         {
//           lIR = lNodeIt.next();
//        
//         }
        lOptimized = true;
      }
      else {
        //##63 lLive.vectorOr(lSetRefRepr.getPExposed(), lLive);
        lLive.vectorOr(pBBlock.getExposed(), lLive); //##63
        lUsedSymsInNonDeletedStmt.addAll(lSetRefRepr.useSyms()); //##97        
      }
    } // for (Iterator lSetRefReprIt .. )
    return lOptimized;
  }

  private static boolean mayWriteToExternalAddress(SetRefRepr pSetRefRepr)
  {
    IR lDefNode = pSetRefRepr.defNode();
    return lDefNode != null && FlowUtil.mayBeExternalAddress(lDefNode);
  }
}
