/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import coins.FlowRoot;
import coins.SymRoot;
import coins.flow.BBlock;
import coins.sym.ExpId; //##60
//##60import coins.flow.FlowResults; //##60
import coins.flow.FlowUtil; //##60
import coins.flow.HirSubpFlowImpl; //##60
import coins.flow.InitiateFlowHir; //##60
import coins.flow.SetRefRepr; //##60
import coins.flow.SetRefReprList; //##60
import coins.flow.SubpFlow; //##60
//##60import coins.flow.CoinsIterator;
import coins.flow.FAList; //##60
//##60 import coins.ir.IR;
import coins.ir.hir.HIR; //##60
import coins.ir.hir.SubpDefinition; //##60
import coins.sym.ExpId; //##60
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.Type;

/**
 * Common subexpression elimination basic logic class.
 */
public abstract class CommonSubexpElim
{
  //  public final OptRoot optRoot;
  public final FlowRoot flowRoot;
  public final SymRoot symRoot;
  public final Sym sym;
  //##25 FlowResults fResults;
  //##60 public FlowResults fResults; //##25
  public final SubpFlow fSubpFlow; //##60
  protected Set fFunctionsWithoutSideEffect; //##82
  protected int fDbgLevel; //##58

//  private final boolean NO_ALIAS;

  //##25 private CommonSubexpElim(FlowRoot pFlowRoot)
  public CommonSubexpElim(FlowRoot pFlowRoot) //##25
  {
    //     optRoot = pOptRoot;
    flowRoot = pFlowRoot;
    symRoot = flowRoot.symRoot;
    //  hir = optRoot.hirRoot.hir;
    sym = symRoot.sym;
    fSubpFlow = pFlowRoot.fSubpFlow; //##60
    fFunctionsWithoutSideEffect = symRoot.getFunctionsWithoutSideEffect(); //##82
    fDbgLevel = flowRoot.ioRoot.dbgOpt1.getLevel(); //##60

//  NO_ALIAS = flowRoot.ioRoot.getCompileSpecification().getCoinsOptions().isSet("na");
  }

  /**
   * Performs the common subexpression elimination within each BBlock for the given SubpFlow. Inter-BBlock data flow is not considered (it is assumed no flow exists between BBlocks).
   *
   * @return true if the subprogram changed (optimized).
   */
  public boolean doBBlockLocal(SubpFlow pSubpFlow)
  {
    BBlock lBBlock;
    boolean lChanged = false;
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgOpt1.print(1, "\ndoBBlockLocal",
        pSubpFlow.getSubpSym().toString()); //##60
    //##60 BEGIN
    // Do control flow analysis
    // Assign ExpId
    // Do data flow analysis
    SubpDefinition lSubpDef = pSubpFlow.getSubpDefinition();
    //##60 flowRoot.controlFlow = flowRoot.flow.controlFlowAnal(fSubpFlow);
    // Control flow analysis is already done in Opt. //##60
    //##60 InitiateFlowHir lInitiateFlowHir = new InitiateFlowHir();
    //##60 lInitiateFlowHir.initiateDataFlow(pSubpFlow);
    if (flowRoot.flow.getFlowAnalStateLevel() <
        coins.flow.Flow.STATE_HIR_FLOW_AVAILABLE) //##60
      flowRoot.dataFlow = flowRoot.flow.dataFlowAnal(lSubpDef);
    //##60 END
    //##60 for (Iterator lIt = pSubpFlow.getBBlocks().iterator(); lIt.hasNext();)
    for (Iterator lIt = pSubpFlow.getBBlockTable().iterator();
         lIt.hasNext(); ) { //##60
      lBBlock = (BBlock)lIt.next();
     if (lBBlock != null) {
        //##60 lChanged = doBBlockLocal(lBBlock) || lChanged;
        if (lBBlock.getBBlockNumber() > 0) //##60
          lChanged = doBBlockLocal(lBBlock) | lChanged; //##60
      }
    }
    if (lChanged) {
      if (flowRoot.ioRoot.dbgOpt1.getLevel() > 2)
        fSubpFlow.getSubpDefinition().printHir("Changed HIR");
      lSubpDef.finishHir(); //##62
      fSubpFlow.setComputedFlag(fSubpFlow.CF_INDEXED); //##62
      pSubpFlow.setRestructureFlag(); //##25-1
    }
    if (fDbgLevel > 0)
      flowRoot.ioRoot.dbgOpt1.print(1, "\n change by CommonSubexpElim of "
        + pSubpFlow.getSubpSym().toString() + " is " + lChanged);
    return lChanged;
  }

  /**
   * Performs the common subexpression elimination within the given BBlock.
   *
   * @return true if the underlying IR has changed (optimized).
   */
  public boolean doBBlockLocal(BBlock pBBlock)
  {
    if ((pBBlock == null)||(pBBlock.getBBlockNumber() == 0))
      return false; //##63
    //##60 SetRefReprList lSetRefReprs = (SetRefReprList)fResults.get("BBlockSetRefReprs", pBBlock);
    SetRefReprList lSetRefReprs = fSubpFlow.getSetRefReprList(pBBlock); //##60
    SetRefRepr lSetRefRepr;

    Map lCompoundToVarNode = new HashMap();
    Map lVarToCompound = new HashMap();
    Map lCompoundToNodeAndSetRefRepr = new HashMap();
    Map lContainsMap = new HashMap();

    //##60 IR lUseNode;
    //##60 IR lTopUseNode;
    HIR lUseNode; //##60
    HIR lTopUseNode; //##60

    //##25 FlowExpId lCompound;
    //##60 FlowExpId lCompound = null;  //##25
    //##60 FlowExpId llCompound;
    ExpId lCompound; //##60
    ExpId llCompound; //##60
    Sym lModSym;
    Set lAddressGrabbedSyms = null;
    Set lLHSSyms = null;
    HIR lNewTempNode; //##60
    //  Set lAddressedGrabbedSyms = new HashSet();
    //  Set lSetReprAddressGrabbedSyms = new HashSet();
    boolean lElimed = false;

    //##60 IR lElimedNode = null;
    //##60 IR lCallNode = null;
    HIR lElimedNode = null; //##60
    HIR lCallNode = null; //##60

    if (fDbgLevel > 0) //##58
      flowRoot.ioRoot.dbgOpt1.print(2, "doBBlockLocal ",
        pBBlock.toString() + " lSetRefReprs "
        + lSetRefReprs.toString()); //##25
      //##60 for (CoinsIterator lIt = lSetRefReprs.coinsIterator(); lIt.hasNext();)
    for (Iterator lIt = lSetRefReprs.iterator(); lIt.hasNext(); ) {
      lSetRefRepr = (SetRefRepr)lIt.next();
      if (fDbgLevel > 0) //##58
        flowRoot.ioRoot.dbgOpt1.print(4, " lSetRefRepr ",
          lSetRefRepr.toString()); //##25
      boolean lRegisterLHSToRHS = false;
      lTopUseNode = (HIR)lSetRefRepr.topUseNode(); //##60
      ListIterator lExpIt = lSetRefRepr.expListIterator(false, true);
      if (lSetRefRepr.sets()) {
        lAddressGrabbedSyms = (lSetRefRepr).modSyms();
        lLHSSyms = (lSetRefRepr).lhsSyms();
        if (fDbgLevel > 0) //##58
          flowRoot.ioRoot.dbgOpt1.print(4, " lLHHSyms "
            + lLHSSyms.toString()); //##25
      }
      Exps:
        for (; lExpIt.hasNext(); ) {
        lUseNode = (HIR)lExpIt.next();
        if (fDbgLevel > 0) //##58
          flowRoot.ioRoot.dbgOpt1.print(4, " lUseNode "
            + lUseNode.toString()); //##25
        boolean lSimpleElimSuccess = false;

        if (lElimedNode != null)
          if (FlowUtil.isUnder(lElimedNode, lUseNode))
            continue;

        // Function call check
        //##60 if (OptUtil.isCall(lUseNode))
        if (OptUtil.isCall(lUseNode))  {

          //  if (false)
          //##60 for (Iterator lSymIt = ((FAList)fResults.get("SymIndexTable", pBBlock.getSubpFlow())).iterator(); lSymIt.hasNext();)
          //##62 for (Iterator lSymIt = fSubpFlow.getSymIndexTable().iterator();
          //##62      lSymIt.hasNext(); ) {
          //##62   lModSym = (FlowAnalSym)lSymIt.next();
          //##62 BEGIN
          for (int lSymIndex = 0; lSymIndex < fSubpFlow.getSymExpCount();
               lSymIndex++) {
            lModSym = fSubpFlow.getIndexedSym(lSymIndex);
            //##62 END
            if (lVarToCompound.containsKey(lModSym)) {
              lCompoundToVarNode.remove(lVarToCompound.get(lModSym));
              lVarToCompound.remove(lModSym);
            }
            if (lContainsMap.containsKey(lModSym)) {
              for (Iterator lIt0 = ((Set)lContainsMap.get(lModSym)).iterator();
                   lIt0.hasNext(); ) {
                //##60 llCompound = (FlowExpId)lIt0.next();
                llCompound = (ExpId)lIt0.next(); //##60
                lCompoundToVarNode.remove(llCompound);
                lCompoundToNodeAndSetRefRepr.remove(llCompound);
              }
            }
          }
        }
        //##60 if (FlowUtil.getChildCount(lUseNode) != 0)
        if (lUseNode.getChildCount() != 0) { //##60
          //##60 lCompound = (FlowExpId)fResults.get("FlowExpIdForNode", lUseNode);
          lCompound = lUseNode.getExpId(); //##60
          if (fDbgLevel > 0) { //##58
            flowRoot.ioRoot.dbgOpt1.print(4, "Compound: ",
              lCompound.toString()); //##25
            flowRoot.ioRoot.dbgOpt1.print(4, "CompoundToVarNode: ",
              lCompoundToVarNode.toString()); //##25
            flowRoot.ioRoot.dbgOpt1.print(4, "CompoundToNodeAndSetRefRepr: ",
              lCompoundToNodeAndSetRefRepr.toString()); //##25
          }
          //##60 if (lCompound.hasCall())
          if (fSubpFlow.subtreesContainingCall().contains(lUseNode)) //##60
            continue Exps;

          if (lCompoundToVarNode.containsKey(lCompound)) {

            lElimed = eliminateSimple(lUseNode,
              (HIR)lCompoundToVarNode.get(lCompound), pBBlock.getSubpFlow(),
              lAddressGrabbedSyms, FlowUtil.isUnder(lSetRefRepr.defNode(),
              lUseNode)) == null ? false : true;
            if (lElimed) {
              lElimedNode = lUseNode;
              lSimpleElimSuccess = true;
//  System.out.print(pBBlock.getSetRefReprs().toString());
//  System.out.println();
            }
          }
          if (!lSimpleElimSuccess &&
              lCompoundToNodeAndSetRefRepr.containsKey(lCompound)) {
            HIR lPrevCompoundNode = (HIR)((Object[])
              lCompoundToNodeAndSetRefRepr.get(lCompound))[0];
            SetRefRepr lPrevCalcSetRefRepr = (SetRefRepr)((Object[])
              lCompoundToNodeAndSetRefRepr.get(lCompound))[1];
            lNewTempNode = eliminateComplex(lUseNode, lPrevCompoundNode,
              lPrevCalcSetRefRepr, lSetRefReprs, lCompoundToNodeAndSetRefRepr,
              lContainsMap);
            if (lNewTempNode != null) {
              lCompoundToVarNode.put(lCompound, lNewTempNode);
              lVarToCompound.put(lNewTempNode.getSym(), lCompound);
              lElimed = true;

              lElimedNode = lUseNode;
//  System.out.print(pBBlock.getSetRefReprs().toString());
//  System.out.println();
            }
          }
          else {
            if (lTopUseNode == lUseNode && lSetRefRepr.sets()) {
              //##60 if (((FlowExpId)fResults.getFlowExpIdForNode(lTopUseNode)).getNumberOfOperations() > ((FlowExpId)fResults.getFlowExpIdForNode(lSetRefRepr.defNode())).getNumberOfOperations())
              if (fSubpFlow.getExpId(lTopUseNode).getNumberOfOperations() >
                  fSubpFlow.getExpId(lSetRefRepr.defNode()).getNumberOfOperations())
                lRegisterLHSToRHS = true;
            }
            lCompoundToNodeAndSetRefRepr.put(lCompound, new Object[] {lUseNode,
              lSetRefRepr});
            registerUseSyms(lUseNode, lContainsMap, lCompound);
          }
        }
      }

      if (lSetRefRepr.sets()) {
        // Pointer assign check. For LIR, if the symbol is no longer marked as the pointer type, the below check doesn't work.
        if (true)
          for (Iterator lLHSSymIt = lLHSSyms.iterator(); lLHSSymIt.hasNext(); ) {
            Sym lLHSSym = (Sym)lLHSSymIt.next();
            if (lLHSSym.getSymType().getTypeKind() == Type.KIND_POINTER) {
              lCompoundToNodeAndSetRefRepr.clear();
              lCompoundToVarNode.clear();
              lVarToCompound.clear();
              break;
            }
          }

        boolean lIsDDef = (lSetRefRepr).defSym() != null;
        lAddressGrabbedSyms = lSetRefRepr.modSyms();
        for (Iterator lModSymIt = lAddressGrabbedSyms.iterator();
             lModSymIt.hasNext(); ) {
          lModSym = (FlowAnalSym)lModSymIt.next();

          if (lVarToCompound.containsKey(lModSym)) {
            lCompoundToVarNode.remove(lVarToCompound.get(lModSym));
            lVarToCompound.remove(lModSym);
          }

          if (lIsDDef && lRegisterLHSToRHS) {
            //##60 lCompoundToVarNode.put(fResults.get("FlowExpIdForNode", lTopUseNode), (lSetRefRepr).defNode());
            lCompoundToVarNode.put(fSubpFlow.getExpId(lTopUseNode),
              (lSetRefRepr).defNode()); //##60
            //##60 lVarToCompound.put(lModSym, fResults.get("FlowExpIdForNode", lSetRefRepr.topUseNode()));
            lVarToCompound.put(lModSym,
              fSubpFlow.getExpId(lSetRefRepr.topUseNode())); //##60
          }

          if (lContainsMap.containsKey(lModSym))
            for (Iterator lIt0 = ((Set)lContainsMap.get(lModSym)).iterator();
                 lIt0.hasNext(); ) {
              llCompound = (ExpId)lIt0.next(); //##60
              lCompoundToVarNode.remove(llCompound);
              lCompoundToNodeAndSetRefRepr.remove(llCompound);
            }

        }
      }

    }
    return lElimed;
  }

  abstract Set operandSet(Object o);

  abstract HIR eliminateSimple(HIR pCompoundNode, HIR pVarNode,
    SubpFlow pSubpFlow, Set pModSyms, boolean pMod); //##60

  abstract HIR eliminateComplex(HIR pCompoundNode, HIR pPrevCompoundNode,
    SetRefRepr pPrevCalcSetRefRepr, SetRefReprList pSetRefReprs,
    Map pCompoundToNodeAndSetRefRepr, Map ContainsMap); //##60

  abstract void registerUseSyms(HIR pSubtree, Map pContainsMap,
    Object pCompound); //##60
  //  abstract void reregisterSubexps(Object pCompound, SetRefRepr pSetRefRepr, Map pCompoundToNodeAndSetRefRepr);
}
