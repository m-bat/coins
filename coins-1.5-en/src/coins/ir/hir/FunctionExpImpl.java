/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IrList;
import coins.sym.PointerType;
import coins.sym.SubpType;
import coins.sym.Type;

//========================================//
//                            (##5): modified on Apr. 2001.

/**
 * FunctionExpImpl
 *    Function call expression.
**/
public class
FunctionExpImpl extends ExpImpl implements FunctionExp
{

public
FunctionExpImpl( HirRoot pHirRoot, Exp pFunctionSpec, IrList pActualParamList ) 
{
  super(pHirRoot, HIR.OP_CALL); 
  fChildCount = 2;
  setChildren(pFunctionSpec, pActualParamList);
  /*///////////////////////////////////// S.Fukuda 2002.5.16 begin
  Type lType = pFunctionSpec.findSubpType();
  if (lType != null)
    fType = ((SubpType)lType).getReturnType();
  else {
    hirRoot.ioRoot.msgRecovered.put(1021,  
         "Default type int is applied as return value type.");
    fType = hirRoot.symRoot.typeInt;   
  }
  */
  //-- Set the type (fType) of this function expression.
  Type lType = pFunctionSpec.getType(); // Get the type of the parameter pFunctionSpec.
  if( lType.getTypeKind()==Type.KIND_POINTER )     // If pointer, then set
    lType = ((PointerType)lType).getPointedType(); // fType = pointed type.
  if( lType.getTypeKind()==Type.KIND_SUBP )    // If subprogram type, then set
    fType = ((SubpType)lType).getReturnType(); // fType = type of return value.
  else // Error for other cases.
  {
    hirRoot.ioRoot.msgRecovered.put(1021,
         "Default type int is applied as return value type.");
    fType = hirRoot.symRoot.typeInt; // Set typeInt for error cases.
  }
  ///////////////////////////////////// S.Fukuda 2002.5.16 end
}

public Exp
getFunctionSpec() { return (Exp)fChildNode1; }

public SubpNode
getFunctionNode()  
{
  Exp lChild1 = (Exp)getChild1();
  if (lChild1 instanceof SubpNode)
    return (SubpNode)lChild1;
  else if ((lChild1.getOperator() == HIR.OP_ADDR)&&
           (lChild1.getChild1() instanceof SubpNode))
    return (SubpNode)lChild1.getChild1();
  else
    return null; 
      // It may be the contents of a variable pointing to
      // a function. It can not be changed to SubpNode.
} // getFunctionNode

public void
setFunctionSpec(Exp pFunctionSpec) { setChild1(pFunctionSpec); }

public IrList
getParamList() { return (IrList)fChildNode2; }

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atFunctionExp(this);
}

} // FunctionExpImpl
