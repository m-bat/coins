/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.SymRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.HIR;
import coins.flow.ExpInf; //##60
import coins.flow.SubpFlow; //##62
import coins.flow.DefUseList; //##63
import coins.flow.SetRefRepr; //##63
import coins.flow.ExpInf; //##63
import coins.aflow.FlowExpId;

/** ExpIdImpl
**/
public class
//##60 ExpIdImpl extends OperandSymImpl implements ExpId
ExpIdImpl extends SymImpl implements ExpId //##60
{
  protected ExpId fHashChain = null;
  protected int   fIdNumber;

  protected IR
    fLinkedNode = null; //##60
  // fLinkedNode is set by setExpInf.
  // fLinkedNode may be changed by optimization. //##65

  //##65 B#GIN
  protected HIR
    fCopiedExp = null;
  // fCopiedExp is set by setExpInf.
  // It is not affected by optimization but keeps the corresponding
  // expression when setExpInf was called.
  //##65 END

  protected
  ExpInf fExpInf = null; //##60

  /** FlowExpId corresponding to this ExpId. */
  protected FlowExpId fFlowExpId;

//---- Fields for FlowAnalSym interface ----//
// These were deleted.

  /** Definition point and using points of this variable */
  //##63 protected coins.aflow.DefUseList
  //##63   fDefUseList = null;

  /** Definition list containing IR nodes that define the
   *   value of this symbol.
  **/
  protected IrList
    fDefList;

  /** Use list containing IR nodes that use (refer) this symbol.
  **/
  protected IrList
    fUseList;

  protected int
    fIndex;    // Index number to be used in flow anal, etc.

  protected java.util.Set
    fOperandSet;   // Set of operands (Var and Reg, excluding Const)
                   // It is assumed that the same ExpId
                   // has the same set of operands.

//==== Constructor ====//

public
ExpIdImpl( SymRoot pSymRoot, String pExpIdName, Sym pDefinedIn )
{
  super(pSymRoot);
  fName = pExpIdName;
  fKind = Sym.KIND_EXP_ID;
  fDefinedIn = pDefinedIn;
}

public IR getLinkedNode()
{
  return fLinkedNode; //##60
} // getLinkedNode

//##65 BEGIN
public HIR getCopiedExp()
{
  return fCopiedExp;
} // getCopiedExp
//##65 END

public Sym
getLinkedSym()
{
  HIR lLinkedNode = (HIR)getLinkedNode();
  if (lLinkedNode != null) {
    return lLinkedNode.getSym();
  }else
    return null;
}

public ExpId
getNextId() { return fHashChain; }

public void
setNextId( ExpId pNextId ) { fHashChain = pNextId; }

public String
toString() {
  String lString = super.toString();
  return lString;
} // toString

public String
toStringDetail() {
  String lString = super.toStringDetail();
  IR lLinkedNode = getLinkedNode();
  if (lLinkedNode != null) {
    lString = lString + " " + ((HIR)lLinkedNode).getIrName();
  }
  //##65 BEGIN
  if (fFlowExpId != null) {
    lString = lString + " " + fFlowExpId.toString();
  }
  //##65 END
  return lString;
} // toStringDetail

//##60 BEGIN moved from ExpIdEImpl

public void
setExpInf( ExpInf pExpInf )
{
  fExpInf = pExpInf;
  fLinkedNode = fExpInf.fLinkedIR;
  fCopiedExp = ((HIR)fLinkedNode).copyWithOperands(); //##65
}

public ExpInf
getExpInf()
{
  return fExpInf;
}

public boolean
isLHS()
{
  if (fLinkedNode != null) {
    HIR lParent = (HIR)fLinkedNode.getParent();
    if ((lParent != null)&&
        (lParent.getOperator() == HIR.OP_ASSIGN)) {
      if (lParent.getChild1() == fLinkedNode)
        return true;
      else
        return false;
    }else
      return false;
  }else
    return false;
}

public java.util.Set
getOperandSet() {
  if (fExpInf == null)
    return null;
  return fExpInf.getOperandSet();
}

public java.util.Set
getOperandSet0() {
  if (fExpInf == null)
    return null;
  return fExpInf.getOperandSet0();
}

public int
getNumberOfOperations()
{
  if (fExpInf == null)
    return 0;
  return fExpInf.getNumberOfOperations();
}

//##60 END

//==== Methods for FlowAnalSym interface ====//
// These were deleted.

/* getDefUseList
 *  Get DefUseList (list of definition points and use points) of this variable.
 *  "this" is a variable.
 *  @return the DefUseList of this variable if it is computed.
 *      If it is not computed, return null.
 *  @note symRoot.flow should be set before call by FlowRoot.
**/
/* //##63
public coins.aflow.DefUseList
getDefUseList( )
{
  if (fDefUseList == null)
  fDefUseList = symRoot.getFlowRoot().aflow.defUseList();
  return fDefUseList;
}

public void
setDefUseList( coins.aflow.DefUseList pDefUseList )
{
  fDefUseList = pDefUseList;
}

public void
addDefPoint( IR pDefNode )
{
  if (fDefList == null)
    fDefList = symRoot.getHirRoot().hir.irList();
  fDefList.add(pDefNode);
} // addDefPoint

public void
addUsePoint( IR pUseNode )
{
  if (fUseList == null)
    fUseList = symRoot.getHirRoot().hir.irList();
  fUseList.add(pUseNode);
} // addUsePoint

public IrList
getDefList()
{
  return fDefList;
}

public IrList
getUseList()
{
  return fUseList;
}
*/ //##63

//##63 BEGIN
public void
  setSetRefRepr( SetRefRepr pSetRefRepr )
{
  if (fExpInf == null)
    fExpInf = new ExpInf(pSetRefRepr.getIR());
  fExpInf.setSetRefRepr(pSetRefRepr);
}

public SetRefRepr
getSetRefRepr()
{
  if (fExpInf == null)
    return null;
  return fExpInf.getSetRefRepr();
}
//##63 END

public int
getIndex()
{
  return fIndex;
}

public void
setIndex( int pIndex)
{
  fIndex = pIndex;
}

public void
resetFlowAnalInf()
{
  //##63 fDefUseList = null;
  fDefList    = null;
  fUseList    = null;
  fIndex      = 0;
  fExpInf     = null; //##62
  fLinkedNode = null; //##62
  fHashChain  = null; //##62
  fOperandSet = null; //##62
  fFlowExpId  = null; //##62
} // resetFlowAnalInf

public void
addOperand( FlowAnalSym pOperand )
{
  if (fOperandSet == null)
    fOperandSet = new java.util.HashSet();
  fOperandSet.add(pOperand);
}

public FlowExpId
getFlowExpId()
{
  return fFlowExpId;
}

public void
setFlowExpId( FlowExpId pFlowExpId )
{
  fFlowExpId = pFlowExpId;
}

} // ExpIdImpl
