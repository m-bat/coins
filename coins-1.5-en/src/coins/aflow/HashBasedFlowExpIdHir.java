/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * HashBasedFlowExpIdHir.java
 *
 * Created on August 14, 2002, 3:50 PM
 */
package coins.aflow;

import coins.ir.IR;      //##57
import coins.ir.hir.Exp; //##57
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.sym.Sym;
import coins.sym.Var;  //##57

/**
 *
 * @author  hasegawa
 */
public class HashBasedFlowExpIdHir extends HashBasedFlowExpId
{
/**
 * Creates a new instance of HashBasedFlowExpIdHir with <code>pNode</code> as its representative linked node.
 */

HashBasedFlowExpIdHir(HIR pNode, int pIndex, SubpFlow pSubpFlow)
{
  super(pNode, pIndex, pSubpFlow);
}

//##57 void populateInfo()
public void operandInfo(IR pIR)
{
  HIR lHIR;
  HIR lParent;
  Sym lSym;

  //##57 BEGIN
  HIR lLinkedHir = (HIR)pIR;
  // If pIR is a VarNode representing a temporal variable
  // then get the expression represented by the temporal variable.
  if (lLinkedHir.getOperator() == HIR.OP_VAR) {
    Var lVar = (Var)lLinkedHir.getSym();
    Exp lExp = fSubpFlow.getExpOfTemp(lVar);
    if (lExp != null)
      lLinkedHir = lExp;
  }
  for (HirIterator lHirIt = FlowUtil.hirIterator(lLinkedHir);
  //##57 END
  //##57 for (HirIterator lHirIt = FlowUtil.hirIterator((HIR)getLinkedNode());
       lHirIt.hasNext(); ) {
    lHIR = lHirIt.next();

    switch (lHIR.getOperator()) {
      case HIR.OP_CALL:
        fHasCall = true;
        fOperationCount++;
        break;

      case HIR.OP_VAR:
      case HIR.OP_PARAM:
      case HIR.OP_ELEM: // ?
        fOperandSet.add(lSym = lHIR.getSym());

        if (((lParent = (HIR)lHIR.getParent()) == null) ||
            (lParent.getOperator() != HIR.OP_ADDR) ||
            dereferenced(lHIR)) {
          fOperandSet0.add(lSym);
        }

      case HIR.OP_SUBP:
      case HIR.OP_SUBS:
      case HIR.OP_INDEX:
      case HIR.OP_QUAL:
      case HIR.OP_ARROW:
      case HIR.OP_ADD:
      case HIR.OP_SUB:
      case HIR.OP_MULT:
      case HIR.OP_DIV:
      case HIR.OP_MOD:
      case HIR.OP_AND:
      case HIR.OP_OR:
      case HIR.OP_XOR:
      case HIR.OP_CMP_EQ:
      case HIR.OP_CMP_NE:
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
      case HIR.OP_SHIFT_LL:
      case HIR.OP_SHIFT_R:
      case HIR.OP_SHIFT_RL:
      case HIR.OP_NOT:
      case HIR.OP_NEG:
      case HIR.OP_ADDR:
      case HIR.OP_CONV:
      case HIR.OP_DECAY:
      case HIR.OP_UNDECAY: //##10
      case HIR.OP_CONTENTS:
      case HIR.OP_SIZEOF:
      case HIR.OP_PHI:
      case HIR.OP_OFFSET:
      case HIR.OP_LG_AND:
      case HIR.OP_LG_OR:
        fOperationCount++;
    }
  }
  if (pIR.getOperator() == HIR.OP_VAR)
    fOperandSet.add(((HIR)pIR).getSym());
  ioRoot.dbgFlow.print(5, " operand of " + this.toString()
    + " " + fOperandSet.toString()); //##57
  fTree = ((HIR)getLinkedNode()).copyWithOperands();
} // operandInfo

    private boolean dereferenced(HIR pHIR)
    {
        while ((pHIR != null) && (pHIR != getTree()))
        {
            if (pHIR.getOperator() == HIR.OP_CONTENTS)
            {
                return true;
            }

            pHIR = (HIR) pHIR.getParent();
        }

        return false;
    }

    public String toString()
    {
        StringBuffer lBuffer = new StringBuffer();
        lBuffer.append("FlowExpId " + fIndex +
        //##53 System.getProperty("line.separator"));
        " "); //##53
        lBuffer.append(FlowUtil.toString((HIR)getTree()));

        return lBuffer.toString();
    }

public String toStringShort()  //##51
{
  return "_XId" + fIndex;
}

}
