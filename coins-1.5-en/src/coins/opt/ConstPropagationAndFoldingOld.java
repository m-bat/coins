/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import coins.FlowRoot;
import coins.SymRoot;
import coins.aflow.BBlock;
import coins.aflow.DefVector;
import coins.aflow.FlowExpId;
import coins.aflow.FlowResults;
import coins.aflow.FlowUtil;
import coins.aflow.SetRefRepr;
import coins.aflow.SetRefReprList;
import coins.aflow.SubpFlow;
import coins.aflow.UDChain;
import coins.aflow.UDList;
import coins.aflow.util.CoinsIterator;
import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.sym.Sym;
import coins.sym.FlowAnalSym;
import coins.sym.Type;
/**
 *
 * Constant propagation and folding basic logic class.
 *
 */
//##66 public class ConstPropagationAndFolding
public class ConstPropagationAndFoldingOld //##66
{
    final static int JAVA_BYTE_SIZE = 1, JAVA_CHAR_SIZE = 2, JAVA_SHORT_SIZE = 2,
                      JAVA_INT_SIZE = 4, JAVA_LONG_SIZE = 8;

    //    public final OptRoot optRoot;
    public final FlowRoot flowRoot;
    public final SymRoot symRoot;
    //    public final HIR hir;
    public final Sym sym;
    FlowResults fResults;

    public final Opt opt;

    //    private final boolean NO_ALIAS;
    /**
    *
    * ConstPropagationAndFolding:
    *
    *
    **/
    //##66 public ConstPropagationAndFolding(FlowResults pResults)
    public ConstPropagationAndFoldingOld(FlowResults pResults) //##66
    {
        this(pResults.flowRoot);
        fResults = pResults;
    }
    /**
    *
    *
    *     ex)
    *     expression :p = (int) a;
    *     exp        := (int) [node]
    *     return     ; a [node] ;
    *
    */
    HIR SkipConv(HIR exp ) {
        HIR Curr = exp;
        Type CurrType;
        Type expType;
        expType = exp.getType();
        while(Curr.getOperator() == HIR.OP_CONV) {
            if (Curr.getType().getTypeKind() != expType.getTypeKind())
                break;
            Curr = (HIR)Curr.getChild1();

        }
        return Curr;
    }
    /*
    *
    *
    **/
    //##66 private ConstPropagationAndFolding(FlowRoot pFlowRoot)
    private ConstPropagationAndFoldingOld(FlowRoot pFlowRoot) //##66
    {
        flowRoot = pFlowRoot;
        symRoot = flowRoot.symRoot;
        sym = symRoot.sym;
        opt = new Opt(flowRoot);
        //NO_ALIAS = flowRoot.ioRoot.getCompileSpecification().getCoinsOptions().isSet("optNa");

    }
    /**
    *
    * doSubp:
    *
    *
    **/
    public boolean doSubp(SubpFlow pSubpFlow)
    {
        boolean lChanged, lChangedAtLeastOnce = false;
        BBlock lBBlock;
        opt.dbg(2, "\nConstPropagationAndFoldingOld.doSubp", pSubpFlow.getSubpSym().getName()); //##66

        do
        {
            lChanged = false;
            for (Iterator lIt = pSubpFlow.getBBlocksFromEntry().iterator();
                 lIt.hasNext();)
            {
                lBBlock = (BBlock)lIt.next();
                if (lBBlock != null) //##66
                  lChanged = doBBlock(lBBlock) || lChanged;
            }
            lChangedAtLeastOnce |= lChanged;
        } while (lChanged);

        return lChangedAtLeastOnce;
    }
    /**
    *
    * doBBlock:
    *
    *
    **/
    public boolean doBBlock(BBlock pBBlock)
    {
        opt.dbg(5, "\nConstPropagationAndFoldingOld.doBBlock",
                "B" +pBBlock.getBBlockNumber()); //##66
        SetRefReprList lSetRefReprs = (SetRefReprList)fResults.get("BBlockSetRefReprs", pBBlock);
        SetRefRepr lSetRefRepr;
        Map lDefs = new HashMap();
        SubpFlow lSubpFlow = pBBlock.getSubpFlow();
        UDChain lUDChain, lUDChain0;
        IR lExp;
        IR lConstNode;
        IR lFoldedNode;
        IR lParent;
        Sym lSym;
        IR lBrotherNode;
        IR lCallNode = null;
        boolean lReplaced = false;
        FlowExpId lFlowExpId;
        DefVector lDReach, lPReach;


        for (CoinsIterator lIt = lSetRefReprs.coinsIterator();
             lIt.hasNext();) {
            lSetRefRepr = (SetRefRepr)lIt.next();
            opt.dbg(4, "lSetRefRepr", lSetRefRepr.toString());  //##66
            Iterator lExpIt = lSetRefRepr.expListIterator();
            for (; lExpIt.hasNext();) {
                lExp = (IR)lExpIt.next();
                opt.dbg(5, "lExp", lExp.toStringShort());  //##66
                if (!FlowUtil.isLvalue(lExp) || FlowUtil.notDereferenced(lExp))
                    continue;
                // I.Fukuda
                HIR p = (HIR)((IR)lExp).getParent();
                if( p != null) {
                    if(p.getOperator() == HIR.OP_SWITCH) {
                        continue;
                    }
                }

                try
                {
                    int lTypeKind = lExp.getSym().getSymType().getTypeKind();
                    // Substitution of vector variable causes problem in the backend.
                    // LIR's structure variable shouldn't be replaced with the value of
                    // its first member.
                    if (lTypeKind == Type.KIND_VECTOR ||
                        lTypeKind == Type.KIND_STRUCT)
                        continue;
                } catch (NullPointerException e)
                {
                }

                if (lSetRefRepr.sets()) {
                    if (lExp == lSetRefRepr.defNode())
                        continue;
                }


                lFlowExpId = fResults.getFlowExpIdForNode(lExp);

                if (lFlowExpId != null) {

                   // 2005.02.11 I.Fukuda
                  Sym sSym = ((HIR)lExp).getSym();
                  if(sSym == null)
                         continue;
                   if (!(sSym instanceof FlowAnalSym))
                         continue;
                  if (lSubpFlow.getSymIndexTable().contains(sSym) == false)
                         continue;
                   //Type Etype =lExp.getType();
                   int llTypeKind = lExp.getSym().getSymType().getTypeKind();
                   if (llTypeKind >  Type.KIND_INT_UPPER_LIM)
                       continue;
                    opt.dbg(5, "Used var=", ((VarNode)lExp).getVar().toString());
                    UDList lUDList =
                    (UDList)fResults.get("UDList", ((VarNode)lExp).getVar(),lSubpFlow);
                    if (lUDList == null) continue;
                    lUDChain = lUDList.getUDChain(lExp);
                    //
                    //lUDChain = ((UDList)fResults.get("UDList0",
                    //                           lFlowExpId,lSubpFlow)).getUDChain(lExp);

                    opt.dbg(5, "lUDChain", lUDChain);
                    if (lUDChain.getDefList().size() == 1) {


                        IR lDefNode = (IR)lUDChain.getDefList().get(0);
                        //
                        // comment : 2004.08.10 I.Fukuda
                        //  int x,y;
                        //   x=1;    (lDefNode -> assign)
                        //   y=x+2;
                        //
                        //System.out.println("Constant propagation");
                        // System.out.println("lDefNode="+lDefNode.toString());

                        // Checks whether the node really defines (instead of modifies)
                        // the symbol.
                        ////////////////////////////////////////////////////////////////////
                        // I.Fukuda (2005.02.11)
                        //if (lDefNode != UDChain.UNINITIALIZED &&
                        //    lDefNode != UDChain.PARAM &&
                        //     fResults.getFlowExpIdForNode((HIR)SkipConv((HIR)FlowUtil.getChild1(lDefNode)))
                        //     == lFlowExpId) {
                        if (lDefNode == UDChain.UNINITIALIZED  ||
                            lDefNode == UDChain.PARAM )
                             continue;
                        Sym dSym = ((HIR)lDefNode.getChild1()).getSym();
                        if (dSym == null)
                        if (!(dSym instanceof FlowAnalSym))
                            continue;
                        if (sSym != dSym)
                            continue;
                        HIR Child2 =SkipConv((HIR)FlowUtil.getChild2(lDefNode));
                        if (FlowUtil.isConstNode(Child2)) {
                         lConstNode = OptUtil.createConstNodeFromConstNode( Child2, flowRoot);
                        //if (FlowUtil.isConstNode(FlowUtil.getChild2(lDefNode))) {
                        //    lConstNode = OptUtil.createConstNodeFromConstNode(
                        //                               FlowUtil.getChild2(lDefNode), flowRoot);
                        ////////////////////////////////////////////////////////////////////

                            //
                            // comment : 2004.08.10 I.Fukuda
                            //  int x,y;
                            //   x=1;    (lConstNode -> '1')
                            //   y=x+2;  (lExp ->x)
                            //
                            //System.out.println("lConstNode="+lConstNode.toString());
                            //System.out.println("lExp="+lExp.toString());

                            int pTypeKind = lConstNode.getSym().getSymType().getTypeKind();

                            //
                            // comment : 2004.08.10 I.Fukuda
                            // replace:
                            // y=x+1 --> y= 1+2;
                            //
                            lReplaced = true;
                             OptUtil.replaceNode(lExp, lConstNode);

                             lFoldedNode = lConstNode;
                              do {
                                    lParent = lFoldedNode.getParent();
                                    if (lParent == null)
                                        break;

                                    //
                                    // comment : 2004.08.10 I.Fukuda
                                    //
                                    // y= 1+2; (lParent --> '+')
                                    //

                                    lFoldedNode = OptUtil.fold(lParent, flowRoot);
                                } while (lFoldedNode != lParent
                                                 && FlowUtil.isConstNode(lFoldedNode));
                           }
                      }
                }
            }
        }
        return lReplaced;
    }

}
