/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.sym.Const;
import coins.sym.Type;
import coins.sym.PointerType;
import coins.aflow.FlowResults;
////////////////////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  ref Array
//
//  Array area information ?
////////////////////////////////////////////////////////////////////////////////////////////
public class Ref_Array  {
    static final int REF_ARRAY_INDUCTION   = 1;
    static final int REF_ARRAY_UNKNOWN     = 2;
    static final int REF_ARRAY_INV_CONST   = 3;
    static final int REF_ARRAY_INVARIANT   = 4;
    static final int REF_ARRAY_REG_INV     = 5;
    static final int REF_ARRAY_REG_IND_INV = 6;
    static final int REF_ARRAY_REG_UNKNOWN = 7;
    static final int REF_ARRAY_MAX         = 8;
    static final int REF_RESULT = 0;
    static final int REF_DDEF   = 1;
    static final int REF_USE    = 2;
    static final int REF_MOD    = 3;
    static final int REF_EUSE   = 4;
    static final int REF_LIVE   = 5; //
    private double   dimArea; // [0.0 ~ 2.9] dimArea = max(refdim[j]) ?
    boolean dimdependence;
    int     dimension;      // Dimension.
    int     ID[];           // 1: induction exp, 2: unknown,
                            // 3: constant, 4: invariant exp,
                            // 5: invariant of bounded range,
                            // 6: induction with bounded range,
                            // 7: value range is unknown.
    long    ConstValue[];   // Constant value (when ID[] shows constant).
    IndExp  InductionExp[]; // Induction exp table (when ID[] is induction exp).
    Exp     IndexExp[];     // IndexExp[i]: i-th index expression.
    IndExp  RegIndInit[];   // REF_ARRAY_REG_IND_INV Init Value
    IndExp  RegIndLast[];   // REF_ARRAY_REG_IND_INV Last Value
    Exp     ArrayNode;      // Array element node (HIR.OP_SUBS)
    VarNode VarNode;        // Array variable node (HIR.OP_VAR)
    FlowResults fResults;
    LoopUtil fUtil;
    /**
    *
    * Ref_Array:
    *
    *
    **/
    Ref_Array(FlowResults pResults) {
        dimension = 1;
        dimdependence = false;
        dimArea      = 0;
        ID           = new int[dimension];
        ConstValue   = new long[dimension];
        InductionExp = new IndExp[dimension];
        RegIndInit   = new IndExp[dimension];
        RegIndLast   = new IndExp[dimension];
        IndexExp = new Exp[dimension];
        RegIndInit[0] = null;
        RegIndLast[0] = null;
        fResults = pResults;
    }
    /**
    *
    * copy:
    *
    *
    **/
    public Ref_Array copy() {
        Ref_Array ref;
        int i;
        ref               = new Ref_Array(fResults);
        ref.ID            = new int[dimension];
        ref.ConstValue    = new long[dimension];
        ref.InductionExp  = new IndExp[dimension];
        ref.RegIndInit    = new IndExp[dimension];
        ref.RegIndLast    = new IndExp[dimension];
        ref.IndexExp      = new Exp[dimension];
        ref.ArrayNode     = ArrayNode;
        ref.VarNode       = VarNode;
        ref.dimension     = dimension;
        ref.dimdependence = dimdependence;
        ref.dimArea       = dimArea;
        ref.fUtil         = fUtil; //##70

        for (i = 0;i<dimension;i++) {
            ref.ID[i] = ID[i];
            ref.ConstValue[i] = ConstValue[i];
            if (InductionExp[i] != null)
                ref.InductionExp[i] = InductionExp[i].copy();
            if (RegIndInit != null)
                ref.RegIndInit[i] = RegIndInit[i];
            if (RegIndLast != null)
                ref.RegIndLast[i] = RegIndLast[i];
            ref.IndexExp[i] = IndexExp[i];
        }
        return ref;
    }
    /**
    *
    * Ref_Array:
    *
    *
    **/
    Ref_Array(FlowResults pResults,Exp Node,LinkedList IndList,Invariant pinv,LoopUtil pUtil) {
        Exp tmp;
        Exp tmp1;
        ListIterator Ie;
        BasicInduction IndTable;
        int dim;

        int op;
        ArrayNode = Node;
        VarNode = null;
        fResults = pResults;
        fUtil = pUtil;
fUtil.Trace("Ref_Array :ArrayNode="+ArrayNode.toString(),5);
        //
        // Get VarNode a[i] : VarNode= (a)
        //

        tmp = Node.getExp1();
        while (tmp != null) {
fUtil.Trace("Ref_Array :tmpNode="+tmp.toString(),5);
            op=tmp.getOperator();
            if (op == HIR.OP_VAR || op == HIR.OP_ELEM) {
                VarNode = (VarNode)tmp;
                break;
            }
            //if (op != HIR.OP_SUBS)
            //    break;
            if (op == HIR.OP_QUAL)
                tmp =tmp.getExp2();
            else
                tmp =tmp.getExp1();
        }

        //
        // Get dimension
        //
fUtil.Trace("make_ref Array :Ver="+VarNode.toString(),5);
        Type vType =VarNode.getVar().getSymType();
fUtil.Trace("make_ref Array :vType="+vType.toString(),5);

        if (vType.getTypeKind() == Type.KIND_POINTER) {
           vType = ((PointerType)vType).getPointedType();
           //##70 BEGIN
           if (VarNode.getParent().getOperator() == HIR.OP_UNDECAY) {
             vType = ((HIR)VarNode.getParent()).getType();
           }
           //##70 END
        } else {
            dimension =vType.getDimension();
        }
        dimension =vType.getDimension();
fUtil.Trace("make_ref Array :dimension="+dimension,5);

        dimArea       = 0.0;
        ID            = new int[dimension];
        ConstValue    = new long[dimension];
        InductionExp  = new IndExp[dimension];
        RegIndInit    = new IndExp[dimension];
        RegIndLast    = new IndExp[dimension];
        IndexExp      = new Exp[dimension];
        dim          = 0 ;
        tmp =ArrayNode;

        while (tmp != null) {
            op=tmp.getOperator();
fUtil.Trace("make_ref Array tmp="+tmp.toString(),5);
            if (op != HIR.OP_SUBS)
                break;
            tmp1 = fUtil.SkipConv(tmp.getSubscriptExp());
            IndexExp[dim] = tmp1;
            if (tmp1.getOperator() == HIR.OP_CONST) {
                Const ConstSym =  ((ConstNode)tmp1).getConstSym();
                if (ConstSym.getSymType().isInteger() == true) {
                    ConstValue[dim] = ConstSym.longValue();
                    // ex) a[3] 3: const
                    ID[dim] = REF_ARRAY_INV_CONST ;
                }
            } else if (pinv.IsInvariant((HIR)tmp1) == true) {
                    // ex) a[p+2] p: invariant
                    ID[dim] = REF_ARRAY_INVARIANT;
            } else {
fUtil.Trace("make_ref Array tmp1="+tmp1.toString(),5);
                IndExp iExp = getIndExp(tmp1,IndList);
                if (iExp == null) {
                    // ex) a[x+y] x+y: unknown
                    ID[dim] = REF_ARRAY_UNKNOWN ;
                } else {
                    // ex) a[i+2] i: induction variable
                    ID[dim] =  REF_ARRAY_INDUCTION ;
                    InductionExp[dim] = iExp;
                }
            }
fUtil.Trace("make_ref Array :ID="+ID[dim],5);
            tmp =tmp.getArrayExp();
            ++dim;
        }

        SetdimArea(); // dimArea = max(refdim[j])
    }
    /**
    *
    * SetdimArea:
    *
    *
    **/
    public void SetdimArea() {
        int ID_Count[];
        int RegCount;
        int i;
        ID_Count = new int[REF_ARRAY_MAX];

        if (dimdependence == true) {
            //
            // ex)
            //  a[i][i];
            //
            dimArea = 2.9;
            return;
        }
        for (i = 0;i<REF_ARRAY_MAX;i++)
            ID_Count[i]= 0;
        for (i = 0;i<dimension;i++) {
            ID_Count[ID[i]] += 1;
        }
        RegCount =ID_Count[REF_ARRAY_REG_INV] + ID_Count[REF_ARRAY_REG_IND_INV]+ ID_Count[REF_ARRAY_REG_UNKNOWN] ;
        if (RegCount == 0) {
            //
            //  Dimensin = 0
            //
            if (ID_Count[REF_ARRAY_INDUCTION] + ID_Count[REF_ARRAY_UNKNOWN] >0) {
                dimArea = 0.5;
            } else {
                dimArea = 0.0;
            }
        } else if (RegCount == 1) {
            //
            //  Dimensin = 1
            //
            if (ID_Count[REF_ARRAY_REG_UNKNOWN] > 0) {
                dimArea = 1.9;
            } else if (ID_Count[REF_ARRAY_INDUCTION] + ID_Count[REF_ARRAY_UNKNOWN] > 0) {
                dimArea = 1.5;
            } else {
                dimArea = 1.0;
            }
        } else {
            //
            //  Dimensin = 2
            //
            if (ID_Count[REF_ARRAY_REG_UNKNOWN] > 0) {
                dimArea = 2.9;
            } if (ID_Count[REF_ARRAY_INDUCTION]  > 0 && ID_Count[REF_ARRAY_REG_IND_INV]  > 0) {
                dimArea = 2.5;
            } else  if (ID_Count[REF_ARRAY_REG_IND_INV] == 0) {
                dimArea = 2.0;
            } else  if (ID_Count[REF_ARRAY_REG_IND_INV] == 1) {
                dimArea = 2.1;
            } else {
                dimArea = 2.9;
            }
        }
    }
    /**
    *
    *  isRegUNKNOWN:
    *
    *
    *
    *
    **/
    boolean isRegUNKNOWN() {
        if (dimdependence == true)
            return true;
        else if (dimArea > 2.1)
            return true;
        else
            return false;
    }
    /**
    *
    * getIndExp:
    *
    *
    **/
    IndExp getIndExp(Exp Node,LinkedList IndList) {
        fUtil.Trace("getIndExp="+Node.toString(),5);
        for (Iterator Ie = IndList.iterator();Ie.hasNext();) {
            BasicInduction IndTable= (BasicInduction)Ie.next();
            fUtil.Trace("Node="+IndTable.DefVarNode.toString(),5);
            fUtil.Trace("indList Size="+IndTable.indExpList.size(),5);
            for (Iterator Ies = IndTable.indExpList.iterator();Ies.hasNext();) {
                IndExp Ind= (IndExp)Ies.next();
                //Trace("varNode1="+Ind.ind_node.toString());
                //Trace("varNode2="+Node.toString());
                if (Ind.ind_node== Node) {
                    return(Ind);
                }
            }
        }
        return (null);
    }
    /**
    *
    *  EQRefArray:
    *
    *
    **/
    boolean EQRefArray(Ref_Array b) {
        int dim;
        int i;

        if (VarNode.getVar() != b.VarNode.getVar())
            return false;
        if (this == b)
            return true;
        if (dimension != b.dimension)
            return false;

        dim = dimension;
        for (i=0;i<dim;i++) {
            if(this.EQRefArrayDim(b,i) == false) {
                return false ;
            }
        }
        return true ;
    }
    /**
    *
    *  EQRefArrayDim:
    *
    *
    **/
    boolean EQRefArrayDim(Ref_Array b,int dim) {

        if (VarNode.getVar() != b.VarNode.getVar())  {
            return false;
        }

        if (ID[dim] != b.ID[dim]) {
                return false;
        }
        if (ID[dim] == REF_ARRAY_INDUCTION) {
            IndExp aInd =InductionExp[dim];
            IndExp bInd =b.InductionExp[dim];
            if (aInd.EQindExp(bInd) == false) {
                    return false;
            }
        } else if (ID[dim] == REF_ARRAY_UNKNOWN) {
            return false;
        } else if (ID[dim] == REF_ARRAY_INV_CONST) {
            if (ConstValue[dim] != b.ConstValue[dim]) {
                return false;
            }
        } else if (ID[dim] == REF_ARRAY_INVARIANT) {
            return EQExpression(IndexExp[dim],b.IndexExp[dim]);
        } else if (ID[dim] == REF_ARRAY_REG_INV) {
            IndExp aInd =InductionExp[dim].AbsIndExp();
            IndExp bInd =b.InductionExp[dim].AbsIndExp();
            if (aInd.EQindExp(bInd) == false) {
                return false;
            }
        } else if (ID[dim] == REF_ARRAY_REG_IND_INV) {
            IndExp aInd =InductionExp[dim].AbsIndExp();
            IndExp bInd =b.InductionExp[dim].AbsIndExp();
            IndExp cInd = null;
            IndExp dInd = null;
            if (RegIndInit[dim] == null) {
                cInd =RegIndLast[dim].AbsIndExp();
            } else {
                cInd =RegIndInit[dim].AbsIndExp();
            }
            if (b.RegIndInit[dim] == null) {
                dInd =b.RegIndLast[dim].AbsIndExp();
            } else {
                dInd =b.RegIndInit[dim].AbsIndExp();
            }
            if (aInd.ind_inc != bInd.ind_inc ||
                aInd.InitConst != bInd.InitConst || aInd.LastConst != bInd.LastConst) {
                return false;
            }
            if (aInd.ind_inc != 1) {
                return false;
            }
            if (aInd.InitConst == true) {
                if (aInd.ind_init != bInd.ind_init) {
                    return false;
                }
            }
            if (aInd.LastConst == true) {
                if (aInd.ind_last != bInd.ind_last) {
                    return false;
                }
            }
            if (cInd.EQindExp(dInd) == false){
                return false;
            }
        } else if (ID[dim] == REF_ARRAY_REG_UNKNOWN) {
                return false;
        }
        return true;
    }
    /**
    *
    * EQExpression:
    *
    *
    **/
   public boolean  EQExpression(Exp node1,Exp node2) {

        if (node1 == null || node2 == null)
            return false;
        if (fResults.getFlowExpIdForNode(node1) == fResults.getFlowExpIdForNode(node2))
            return true;
        else
            return false;

    }


    /**
    *
    * toString:
    *
    *
    **/
    public String toString()
    {
        //##70 return "ref_Array VarNode="+VarNode.toString();
        return "ref_Array "+VarNode.toStringShort(); //##70
    }
    /**
    *
    * trace:
    *
    *
    **/
    private void Trace(String s) {
        System.out.println(s );
    }

    /**
    *
    * DebugArrayRef:
    *
    *
    */
    void DebugArrayRef(LoopUtil pUtil)
    {
        int i;
        pUtil.Trace("----------DebugArrayRef---(START)---",5);
        pUtil.Trace("ArrayNode="+ArrayNode.toString(),5);
        pUtil.Trace("VarNode="+VarNode.toString(),5);
        pUtil.Trace("dimension="+dimension,5);
        pUtil.Trace("dimArea="+dimArea,5);
        pUtil.Trace("dimdependence="+dimdependence,5) ;
        for (i=0;i<dimension;i++) {
            pUtil.Trace("ID="+ID[i],5);
            switch (ID[i]) {
            case REF_ARRAY_INDUCTION   :
                pUtil.Trace("ID=ARRAY_INDUCTION",5);
                break;
            case REF_ARRAY_UNKNOWN     :
                pUtil.Trace("ID=ARRAY_UNKNOWN",5);
                break;
            case REF_ARRAY_INV_CONST   :
                pUtil.Trace("ID=ARRAY_INV_CONST",5);
                break;
            case REF_ARRAY_INVARIANT   :
                pUtil.Trace("ID=ARRAY_INVARIANT",5);
                break;
            case REF_ARRAY_REG_INV     :
                pUtil.Trace("ID=ARRAY_REG_INV",5);
                break;
            case REF_ARRAY_REG_IND_INV :
                pUtil.Trace("ID=ARRAY_REG_IND_INV",5);
                break;
            case REF_ARRAY_REG_UNKNOWN     :
                pUtil.Trace("ID=ARRAY_REG_UNKNOWN",5);
                break;
            }
            if (IndexExp[i] != null) //##83
              pUtil.Trace("Index="+IndexExp[i].toString(),5);
            pUtil.Trace("ConstValue="+ConstValue[i],5);
            if (InductionExp[i] != null) {
                InductionExp[i].DebugIndExp(pUtil);
            }
            if (RegIndInit[i] != null) {
                RegIndInit[i].DebugIndExp(pUtil);
            }
            if (RegIndLast[i] != null) {
                RegIndLast[i].DebugIndExp(pUtil);
            }
        }
        pUtil.Trace("----------DebugArrayRef---(END)---",5);
    }
}
