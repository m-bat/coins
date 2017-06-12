/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * HaseBasedFlowExpId.java
 *
 * Created on June 26, 2002, 5:52 PM
 */
package coins.aflow;

import java.util.HashSet;

import coins.ir.IR;
import coins.aflow.util.FlowError; // 2004.06.01 S.Noishi
import coins.sym.Sym;         //##41
import coins.sym.FlowAnalSym; //##41
import coins.sym.ExpId;       //##41
import coins.sym.ExpIdImpl;   //##41
import coins.SymRoot;         //##41
import coins.IoRoot;          //##41
import coins.ir.hir.HIR;      //##41

//import coins.flow.*;

/**
 * An implementation of FlowExpId. Subtrees that have exactly the same structure have the same HashBasedFlowExpId.
 * @author  hasegawa
 */
abstract public class HashBasedFlowExpId implements FlowExpId {
    private HashBasedFlowExpId fNext;
    private final IR fLinkedNode; // Node linked to this FlowExpId.
    final int fIndex;
    protected FlowResults fResults;
    protected final IoRoot  ioRoot;  //##57
    protected final SymRoot symRoot; //##57
    int fOperationCount;
    final java.util.Set fOperandSet = new HashSet();
    final java.util.Set fOperandSet0 = new HashSet();
    //##25 SubpFlow fSubpFlow;
    protected SubpFlow fSubpFlow;  //##25
    protected IR fTree;
    protected boolean fHasCall;
    /** fLHS is true if corresponding expression is
     * left hand side expression of AssignStmt.
     */
    protected boolean fLHS = false;  //##53

    /** Creates new HaseBasedFlowExpId */

    /*        public HashBasedFlowExpId(IR pNode, int pIndex, FlowResults pResults)
            {
                    fLinkedNode = pNode;
                    fIndex = pIndex;
                    fResults = pResults;
                    populateInfo();

            }
     */

    protected ExpId fExpId; //##41

    HashBasedFlowExpId(IR pNode, int pIndex, SubpFlow pSubpFlow) {
        symRoot = ((SubpFlowImpl)pSubpFlow).symRoot; //##57
        ioRoot   = symRoot.ioRoot; //##57
        fSubpFlow = pSubpFlow;
        fLinkedNode = pNode;
        fIndex = pIndex;
        fResults = fSubpFlow.results();
        //##57 populateInfo();
        operandInfo(pNode);  //##57
        //##41 BEGIN
        //-- for ExpId
        String lExpIdName = generateExpIdName(pIndex);
        //##57 SymRoot symRoot = ((SubpFlowImpl)pSubpFlow).symRoot;
        //##57 IoRoot ioRoot   = symRoot.ioRoot;
        fExpId = (ExpId)((ExpIdImpl)(symRoot.symTableCurrent.
                         searchOrAdd(lExpIdName, Sym.KIND_EXP_ID,
                         symRoot.subpCurrent, true, true)));
        fExpId.setFlowExpId(this);
        //##57 ioRoot.dbgFlow.print( 5, "HashBasedFlowExpId", ((HIR)pNode).toStringShort()
        //##57                        + " " + fExpId.getName());
        //##41 END
    }

    protected HashBasedFlowExpId getNextInChain() {
        return fNext;
    }

    protected void setNextInChain(HashBasedFlowExpId pNext) {
        fNext = pNext;
    }

    public IR getLinkedNode() {
        return fLinkedNode;
    }

    public IR getTree() {
        return fTree;
    }

    public int getIndex() {
        return fIndex;
    }

    public java.util.Set getOperandSet() {
        return fOperandSet;
    }

    public java.util.Set getOperandSet0() {
        return fOperandSet0;
    }

    //##57 abstract void populateInfo();
    abstract public void operandInfo( IR pIr); //##57

    public String toString() {
        StringBuffer lBuffer = new StringBuffer();
        lBuffer.append("FlowExpId " + fIndex + " " + fTree);

        return lBuffer.toString();
    }

    public int getNumberOfOperations() {
        return fOperationCount;
    }

    private static int getNumberOfOperationsUnder(IR pIR, int pAccumulatedCount) {
        throw new FlowError(); // 2004.06.01 S.Noishi
    /*
        if (pIR instanceof Ltyped) {
            pAccumulatedCount++;
        }

        if (pIR != null) {
            throw new FlowError(); // 2004.05.31 S.Noishi
            pAccumulatedCount = getNumberOfOperationsUnder(((LIRNode) pIR).getLeftChild(),
                    pAccumulatedCount);
            pAccumulatedCount = getNumberOfOperationsUnder(((LIRNode) pIR).getRightChild(),
                    pAccumulatedCount);
        }

        return pAccumulatedCount;
    */
    }

    public DefUseList getDefUseList() {
        return (DefUseList) fResults.get("DefUseList0", this, fSubpFlow);
    }

    public UDList getUDList() {
        return (UDList) fResults.get("UDList0", this);
    }

    public SubpFlow getSubpFlow() {
        return fSubpFlow;
    }

    public boolean hasCall() {
        return fHasCall;
    }

    /*        public List getSubexps()
            {
                    throw new UnimplementedMethodException();
            }
     */

//##41 BEGIN
//==== Methods for ExpId ====//

 /** generateExpIdName:  //##41
  *  Generate ExpId name of the form
  *     xIdnnn
  *  where nnn is the string representing pIndex.
  *  If xIdnnn conflicts with other symbol, then
  *     xIdnnn_mm
  *  where mm is one of 1, 2, 3, ... .
  *  @param pIndex: index number of FlowExpId corresponding to the ExpId.
  *  @return the generated ExpId name.
 **/
protected String
generateExpIdName(int pIndex) { //##41
  String lName, lIndexString, lNameExtended;
  int lExpIdSuffix;
  Sym lSym;
  boolean lFound = false;

  lIndexString = Integer.toString(pIndex, 10);
  lName = ("_xId" + lIndexString).intern();
  lSym = ( (SubpFlowImpl) fSubpFlow).symRoot.symTableCurrent.searchLocal(
      lName, 0, false);
  if ( (lSym != null) && (lSym instanceof ExpId)) {
    ( (FlowAnalSym) lSym).resetFlowAnalInf();
    return lName;
  }
  lExpIdSuffix = 1;
  while (lFound == false) {
    lNameExtended = (lName + "_" + Integer.toString(lExpIdSuffix, 10)).intern();
    lSym = ( (SubpFlowImpl) fSubpFlow).symRoot.symTableCurrent.searchLocal(
        lNameExtended, 0, false);
    if (lSym == null)
      break;
    if (lSym instanceof ExpId) {
      ( (FlowAnalSym) lSym).resetFlowAnalInf();
      break;
    }
    lExpIdSuffix++;
  }
  return lName;
} // genareteExpIdName

public ExpId
getExpId()  //##41
{
  return fExpId;
}
//##41 END

//##53 BEGIN

public void
setLHSFlag()
{
  fLHS = true;
}

public boolean
isLHS()
{
  return fLHS;
}
//##53 END
}
