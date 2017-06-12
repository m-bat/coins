/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.sym.Const;

/**
 *
 * Asm node is treated as a statement.
 *
**/
public class
AsmStmtImpl extends StmtImpl implements AsmStmt
{

public
AsmStmtImpl( HirRoot pHirRoot, ConstNode pInstructions,
             HirList pActualParams )
{
  super(pHirRoot);
  fOperator = HIR.OP_ASM;
  fChildCount = 2;
  setChild1(pInstructions);
  setChild2(pActualParams);
  fType = hirRoot.symRoot.typeVoid;
}

/**
 * Get the sequence of assembly language instructions
 * in the form of String.
 * @return the assembly language instruction sequence.
 */
public String
getInstructions()
{
  if (fChildNode1 instanceof ConstNode) {
    Const lConst = ((ConstNode)fChildNode1).getConstSym();
    return lConst.makeJavaString(lConst.getName());
  }else
    return "";
} // getInstructions

/**
 * Get the list of actual parameters.
 * @return the list of actual parameters.
 */
public HirList
getActualParamList()
{
  return (HirList)getChild2();
}

/**
 * Set pInstructions as the assembly language instruction
 * sequence.
 * @param pInstructions
 */
public void
setInstructions( String pInstructions )
{
  setChild1(constNode(hirRoot.symRoot.sym.stringConst(pInstructions)));
}

/**
 *
 * @param pInstructionList
 */
public void
setInstructionList( HirList pInstructionList )
{
  setChild2(pInstructionList);
}

public void
setActualParamList( HirList pActualParamList )
{
  setChild(3, pActualParamList);
}

public Object
clone() {
  AsmStmtImpl lTree;
  try {
    lTree = (AsmStmtImpl)super.clone();
  }catch (CloneNotSupportedException e) {
   hirRoot.ioRoot.msgRecovered.put(1100, "CloneNotSupportedException(AsmStmt) "
                     + this.toString());
    return null;
  }
  return (Object)lTree;
} // clone

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atAsmStmt(this);
}

} // AsmStmtImpl class
