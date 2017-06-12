/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.Map;

import coins.MachineParam;
import coins.aflow.FlowResults;
import coins.aflow.FlowUtil;
import coins.aflow.SetRefRepr;
import coins.aflow.UDChain;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl; //##51
import coins.sym.Const;
import coins.sym.Type;

/**
 * This class performs constant propagation and folding operations that are specific to HIR.
 */
//##66 public class ConstPropagationAndFoldingHir extends ConstPropagationAndFolding // Should extend ConstFoldingHir
public class ConstPropagationAndFoldingHirOld extends ConstPropagationAndFoldingOld // Should extend ConstFoldingHir //##66
{
	public final HIR hir;
	
        //##66 public ConstPropagationAndFoldingHir(FlowResults pResults)
        public ConstPropagationAndFoldingHirOld(FlowResults pResults) //##66
	{
                super( pResults);
		hir = flowRoot.hirRoot.hir;
	}
	
//	public ConstPropagationAndFoldingHir(FlowRoot pFlowRoot)
//	{
//		super(pFlowRoot);
//		hir = flowRoot.hirRoot.hir;
//	}
	
	
        IR checkRHSAndRegister(SetRefRepr pSetRefRepr, Map pDefs)
	{
		HIR lTopUseNode = (HIR)pSetRefRepr.topUseNode();
                if (lTopUseNode.getOperator() == HIR.OP_CONST)
                        return (IR)pDefs.put(pSetRefRepr.defFlowExpId(), lTopUseNode);
		return null;
	}
	
	Object constRHS(IR pDefNode)
	{
		Exp lRHSNode;
		Const lConst;
		
		if (!(pDefNode.equals(UDChain.UNINITIALIZED) || pDefNode.equals(UDChain.PARAM)))
		{
			lRHSNode = ((AssignStmt)pDefNode.getParent()).getRightSide();
			if (lRHSNode.getOperator() == HIR.OP_CONST)
			{
				lConst = ((ConstNode)lRHSNode).getConstSym();
				return lConst;
			}
		}
		return null;
		
	}
	
	IR createConstNodeFromConst(Object pConst)
	{
		return hir.constNode((Const)pConst);
	}
	
	

	
	boolean addressUsed(IR pIR)
	{
		HIR lParent = (HIR)pIR.getParent();
		if (lParent != null && lParent.getOperator() == HIR.OP_CONTENTS)
			return true;
		return false;
	}
	

	
	
	IR foldBinary(IR pParent, IR pChild, IR pChild0)
	{
		boolean lBoolResult;
		long lIntResult;
		double lFloatResult;
		Exp lNewExpNode = null;
		//		IR lNewExpNode = null;
		int lOpCode = pParent.getOperator();
		Type lType = ((HIR)pParent).getType();
		int lTypeKind = lType.getTypeKind();
		boolean lIsIntType = (lTypeKind <= Type.KIND_INT_UPPER_LIM);
		boolean lIsSigned = (lTypeKind < Type.KIND_UNSIGNED_LOWER_LIM);
		if (lTypeKind == Type.KIND_OFFSET)
			return pParent;
		ConstNode lConstNode = null;
		ConstNode lConstNode0 = null;
		Const lConstSym = null;
		Const lConstSym0 = null;
		boolean lTrivialCheck = false;
		boolean lTrivialCheck0 = false;
		boolean lIsOne = false;
		boolean lIsOne0 = false;
		boolean lIsZero = false;
		boolean lIsZero0 = false;
		boolean lIsFalse = false;
		boolean lIsFalse0 = false;
		boolean lIsTrue = false;
		boolean lIsTrue0 = false;
//                OptUtil OptUtil = new OptUtil(fResults);
		
		if (pChild instanceof ConstNode)
		{
			lConstNode = (ConstNode)pChild;
			lConstSym = lConstNode.getConstSym();
			lTrivialCheck = true;
		}
		if (pChild0 instanceof ConstNode)
		{
			lConstNode0 = (ConstNode)pChild0;
			lConstSym0 = lConstNode0.getConstSym();
			lTrivialCheck0 = true;
		}
		if (!(lTrivialCheck || lTrivialCheck0))
			return pParent;
		
		if (lTrivialCheck && lTrivialCheck0)
		{
			switch (lOpCode) // The below is not comprehensive.
			{
				case HIR.OP_ADD:
					if (lIsIntType)
					{
						lIntResult = lConstNode.getConstSym().longValue() + lConstNode0.getConstSym().longValue();
						lNewExpNode = hir.constNode( sym.intConst(lIntResult, lType));
					} else
					{
						lFloatResult = lConstNode.getConstSym().doubleValue() + lConstNode0.getConstSym().doubleValue();
						lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
					}
					break;
				case HIR.OP_SUB:
					if (lIsIntType)
					{
						lIntResult = lConstNode.getConstSym().longValue() - lConstNode0.getConstSym().longValue();
						lNewExpNode = hir.constNode( sym.intConst(lIntResult, lType));
					} else
					{
						lFloatResult = lConstNode.getConstSym().doubleValue() - lConstNode0.getConstSym().doubleValue();
						lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
					}
					break;
				case HIR.OP_MULT:
					if (lIsIntType)
					{
						lIntResult = lConstNode.getConstSym().longValue() * lConstNode0.getConstSym().longValue();
						lNewExpNode = hir.constNode( sym.intConst(lIntResult, lType));
					} else
					{
						lFloatResult = lConstNode.getConstSym().doubleValue() * lConstNode0.getConstSym().doubleValue();
						lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
					}
					break;
				case HIR.OP_DIV:
					if (lIsIntType)
					{
						if (lIsSigned)
							// ##51 switch (MachineParam.evaluateSize(lTypeKind))
                                                        switch (((HIR_Impl)hir).hirRoot.machineParam.evaluateSize(lTypeKind)) //##51
							{
								case JAVA_BYTE_SIZE:
									lIntResult = (byte)lConstNode.getConstSym().longValue() / (byte)lConstNode0.getConstSym().longValue();
									break;
								case JAVA_SHORT_SIZE:
									lIntResult = lConstNode.getConstSym().shortValue() / lConstNode0.getConstSym().shortValue();
									break;
								case JAVA_INT_SIZE:
									lIntResult = lConstNode.getConstSym().intValue() / lConstNode0.getConstSym().intValue();
									break;
								case JAVA_LONG_SIZE:
									lIntResult = lConstNode.getConstSym().longValue() / lConstNode0.getConstSym().longValue();
									break;
								default: return pParent;
							}
						else
							//##51 switch (MachineParam.evaluateSize(lTypeKind))
                                                        switch (((HIR_Impl)hir).hirRoot.machineParam.evaluateSize(lTypeKind)) //##51
							{
								case JAVA_BYTE_SIZE:
									lIntResult = (char)(byte)lConstNode.getConstSym().longValue() / (char)(byte)lConstNode.getConstSym().longValue();
									break;
								case JAVA_CHAR_SIZE:
									lIntResult = lConstNode.getConstSym().charValue() / lConstNode0.getConstSym().charValue();
									break;
								case JAVA_INT_SIZE:
									lIntResult = (lConstNode.getConstSym().longValue() & (long)Integer.MAX_VALUE + 1) / (lConstNode0.getConstSym().longValue() & (long)Integer.MAX_VALUE + 1);
									break;
								default: return pParent;
							}
							lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
					} else
					{
						lFloatResult = lConstNode.getConstSym().doubleValue() / lConstNode0.getConstSym().doubleValue();
						lNewExpNode = hir.constNode(sym.floatConst(lFloatResult, lType));
					}
					break;
				case HIR.OP_MOD:
					if (lIsSigned)
                                                //##51 switch (MachineParam.evaluateSize(lTypeKind))
                                                switch (((HIR_Impl)hir).hirRoot.machineParam.evaluateSize(lTypeKind)) //##51
						{
							case JAVA_BYTE_SIZE:
								lIntResult = (byte)lConstNode.getConstSym().longValue() % (byte)lConstNode0.getConstSym().longValue();
								break;
							case JAVA_SHORT_SIZE:
								lIntResult = lConstNode.getConstSym().shortValue() % lConstNode0.getConstSym().shortValue();
								break;
							case JAVA_INT_SIZE:
								lIntResult = lConstNode.getConstSym().intValue() % lConstNode0.getConstSym().intValue();
								break;
							case JAVA_LONG_SIZE:
								lIntResult = lConstNode.getConstSym().longValue() % lConstNode0.getConstSym().longValue();
								break;
							default: return pParent;
						}
					else   //##51switch (MachineParam.evaluateSize(lTypeKind))
                                                switch (((HIR_Impl)hir).hirRoot.machineParam.evaluateSize(lTypeKind)) //##51
						{
							case JAVA_BYTE_SIZE:
								lIntResult = (char)(byte)lConstNode.getConstSym().longValue() % (char)(byte)lConstNode.getConstSym().longValue();
								break;
							case JAVA_CHAR_SIZE:
								lIntResult = lConstNode.getConstSym().charValue() % lConstNode0.getConstSym().charValue();
								break;
							case JAVA_INT_SIZE:
								lIntResult = (lConstNode.getConstSym().longValue() & (long)Integer.MAX_VALUE + 1) % (lConstNode0.getConstSym().longValue() & (long)Integer.MAX_VALUE + 1);
								break;
							default: return pParent;
						}
						lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
						break;
				case HIR.OP_AND:
					lBoolResult = (lConstNode.getConstSym().longValue() & lConstNode.getConstSym().longValue()) != 0;
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_OR:
					lBoolResult = (lConstNode.getConstSym().longValue() | lConstNode.getConstSym().longValue()) != 0;
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_XOR:
					lBoolResult = (lConstNode.getConstSym().longValue() ^ lConstNode.getConstSym().longValue()) != 0;
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_CMP_EQ:
					if (lIsIntType)
						lBoolResult = (lConstNode.getConstSym().longValue() == lConstNode0.getConstSym().longValue());
					else
						lBoolResult = (lConstNode.getConstSym().doubleValue() == lConstNode0.getConstSym().doubleValue());
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_CMP_NE:
					if (lIsIntType)
						lBoolResult = (lConstNode.getConstSym().longValue() != lConstNode0.getConstSym().longValue());
					else
						lBoolResult = (lConstNode.getConstSym().doubleValue() != lConstNode0.getConstSym().doubleValue());
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_CMP_GT:
					if (lIsIntType)
						lBoolResult = (lConstNode.getConstSym().longValue() > lConstNode0.getConstSym().longValue());
					else
						lBoolResult = (lConstNode.getConstSym().doubleValue() > lConstNode0.getConstSym().doubleValue());
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_CMP_GE:
					if (lIsIntType)
						lBoolResult = (lConstNode.getConstSym().longValue() >= lConstNode0.getConstSym().longValue());
					else
						lBoolResult = (lConstNode.getConstSym().doubleValue() >= lConstNode0.getConstSym().doubleValue());
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_CMP_LT:
					if (lIsIntType)
						lBoolResult = (lConstNode.getConstSym().longValue() < lConstNode0.getConstSym().longValue());
					else
						lBoolResult = (lConstNode.getConstSym().doubleValue() < lConstNode0.getConstSym().doubleValue());
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
				case HIR.OP_CMP_LE:
					if (lIsIntType)
						lBoolResult = (lConstNode.getConstSym().longValue() <= lConstNode0.getConstSym().longValue());
					else
						lBoolResult = (lConstNode.getConstSym().doubleValue() <= lConstNode0.getConstSym().doubleValue());
					lNewExpNode = hir.constNode(sym.boolConst(lBoolResult));
					break;
					
				case HIR.OP_SHIFT_LL:
					lIntResult = lConstNode.getConstSym().longValue() << lConstNode0.getConstSym().longValue();
					lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
					break;
				case HIR.OP_SHIFT_R:
				     //##51	switch (MachineParam.evaluateSize(lTypeKind))
                                        switch (((HIR_Impl)hir).hirRoot.machineParam.evaluateSize(lTypeKind)) //##51
					{
						case JAVA_BYTE_SIZE:
							lIntResult = (byte)lConstNode.getConstSym().longValue() >> lConstNode.getConstSym().longValue();
							lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
							break;
						case JAVA_SHORT_SIZE:
							lIntResult = lConstNode.getConstSym().shortValue() >> lConstNode.getConstSym().longValue();
							lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
							break;
						case JAVA_INT_SIZE:
							lIntResult = lConstNode.getConstSym().intValue() >> lConstNode.getConstSym().longValue();
							lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
							break;
						case JAVA_LONG_SIZE:
							lIntResult = lConstNode.getConstSym().longValue() >> lConstNode.getConstSym().longValue();
							lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
							break;
						default: return pParent;
					}
				case HIR.OP_SHIFT_RL:
					lIntResult = lConstNode.getConstSym().longValue() >>> lConstNode0.getConstSym().longValue();
					lNewExpNode = hir.constNode(sym.intConst(lIntResult, lType));
					break;
					
				case HIR.OP_INDEX:
					return pParent;
					
				default: throw new UnsupportedOperationException("Operator " + HIR.OP_CODE_NAME[lOpCode] + "not yet taken care of.");
			}
		} else
		{
			if (lTrivialCheck)
			{
				if (lConstSym.equals(symRoot.boolConstFalse))
					lIsFalse = true;
				else if (lConstSym.equals(symRoot.boolConstTrue))
					lIsTrue = true;
				else if (lConstSym.equals(symRoot.doubleConst0) || (lConstSym.equals(symRoot.floatConst0)) || (lConstSym.equals(symRoot.intConst0)) || (lConstSym.equals(symRoot.longConst0)))
					lIsZero = true;
				else if (lConstSym.equals(symRoot.intConst1))
					lIsOne = true;
				else
					return pParent;
			} else
			{
				if (lConstSym0.equals(symRoot.boolConstFalse))
					lIsFalse0 = true;
				else if (lConstSym0.equals(symRoot.boolConstTrue))
					lIsTrue0 = true;
				else if (lConstSym0.equals(symRoot.doubleConst0) || (lConstSym0.equals(symRoot.floatConst0)) || (lConstSym0.equals(symRoot.intConst0)) || (lConstSym0.equals(symRoot.longConst0)))
					lIsZero0 = true;
				else if (lConstSym0.equals(symRoot.intConst1))
					lIsOne0 = true;
				else
					return pParent;
			}
			switch (lOpCode) // The below is not comprehensive.
			{
				case HIR.OP_ADD:
				case HIR.OP_SUB:
					if (lIsZero)
						lNewExpNode = (Exp)pChild0;
					else if (lIsZero0)
						lNewExpNode = (Exp)pChild;
					else
						return pParent;
					break;
				case HIR.OP_MULT:
					if (lIsZero)
					{
                                                if (FlowUtil.hasCallUnder(pChild0))
							return pParent;
						lNewExpNode = hir.constNode(lConstSym);
					} else if (lIsZero0)
					{
                                                if (FlowUtil.hasCallUnder(pChild))
							return pParent;
						lNewExpNode = hir.constNode(lConstSym0);
					}
					else if (lIsOne)
						lNewExpNode = (Exp)pChild0;
					else if (lIsOne0)
						lNewExpNode = (Exp)pChild;
					else
						return pParent;
					break;
				case HIR.OP_DIV:
					if (lIsZero)
					{
                                                if (FlowUtil.hasCallUnder(pChild0))
							return pParent;
						lNewExpNode = hir.constNode(lConstSym);
					} else if (lIsOne0)
						lNewExpNode = (Exp)pChild;
					else
						return pParent;
					break;
				case HIR.OP_MOD:
					if (lIsZero)
					{
                                                if (FlowUtil.hasCallUnder(pChild0))
							return pParent;
						lNewExpNode = hir.constNode(lConstSym);
					} else if (lIsOne0)
					{
                                                if (FlowUtil.hasCallUnder(pChild0))
							return pParent;
						lNewExpNode = hir.constNode(sym.intConst(0, lType));
					} else
						return pParent;
					break;
				case HIR.OP_SHIFT_LL:
				case HIR.OP_SHIFT_R:
				case HIR.OP_SHIFT_RL:
					if (lIsZero)
					{
                                                if (FlowUtil.hasCallUnder(pChild0))
							return pParent;
						lNewExpNode = hir.constNode(lConstSym);
					} else if (lIsZero0)
						lNewExpNode = (Exp)pChild;
					else
						return pParent;
					break;
					
				case HIR.OP_ASSIGN:
				case HIR.OP_AND:
				case HIR.OP_OR:
				case HIR.OP_XOR:
				case HIR.OP_CMP_EQ:
				case HIR.OP_CMP_NE:
				case HIR.OP_CMP_GT:
				case HIR.OP_CMP_GE:
				case HIR.OP_CMP_LT:
				case HIR.OP_CMP_LE:
				case HIR.OP_INDEX:
					return pParent;
					
				default: throw new UnsupportedOperationException("Operator " + HIR.OP_CODE_NAME[lOpCode] + "not yet taken care of.");
				
			}
		}
		
		
		switch(lOpCode) // Suppress writing Boolean constants because the backend does not support it.
		{
			case HIR.OP_CMP_EQ:
			case HIR.OP_CMP_NE:
			case HIR.OP_CMP_GT:
			case HIR.OP_CMP_GE:
			case HIR.OP_CMP_LT:
			case HIR.OP_CMP_LE:
				return pParent;
		}
		
		
                OptUtil.replaceNode(pParent, lNewExpNode);
		return lNewExpNode;
	}
	
}
