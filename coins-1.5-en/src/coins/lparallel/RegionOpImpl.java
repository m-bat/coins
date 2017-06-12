/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
//##70 import coins.aflow.BBlock;
//##70 import coins.aflow.FlowResults;
//##70 import coins.aflow.FlowUtil;
//import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.sym.Var;
///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  RegionOPImpl
//
//
///////////////////////////////////////////////////////////////////////////////
public class RegionOpImpl  implements RegionOp{
    /**
    *
    *  RegionOp:
    *
    *  Implementing array region operations.
    **/
    void RegionOpImpl()
    {
    }
    /**
    *
    *  regADD:
    *
    *  The operation method of [ref array] is as follows.
    *
    *                        UNKNOWN     INVARIANT   INDUCTION
    *           UNKNOWN      (safety)   (safety)     (safety)
    *           INVARIANT    (safety)    case-1       case-2
    *           INDUCTION    (safety)    case-2       case-3
    *
    *        ex1)  a[3]  regADD a[2]     -> case-1
    *        ex2)  a[i]  regADD a[j+2]   -> case-3
    *
    *
    *
    **/
    public int regADD(Ref_Array reg1,Ref_Array reg2,Ref_Array reg3,int dim) {
        int id1;
        int id2;

        id1 = reg1.ID[dim];
        id2 = reg2.ID[dim];

        //
        // REF_ARRAY_REG_IND_INV
        //

        if (id1 == reg1.REF_ARRAY_REG_IND_INV) {
            if (id2 == reg2.REF_ARRAY_INV_CONST) {
                if (reg1.RegIndInit[dim] == null) { // for(j=n;j<i;j++);
                    if (reg1.InductionExp[dim].InitConst == true) {
                        if(reg1.InductionExp[dim].ind_init == reg2.ConstValue[dim]) {
                            return 0;
                        }
                    }
                } else { //for (j=i;j<n;j++)
                    if (reg1.InductionExp[dim].LastConst == true) {
                        if(reg1.InductionExp[dim].ind_last == reg2.ConstValue[dim]) {
                            return 0;
                        }
                    }
                }
            } else if (id2 == reg2.REF_ARRAY_REG_IND_INV){
                if(reg1.EQRefArrayDim(reg2,dim) == true) {
                    return 0;
                }
                IndExp aInd =reg1.InductionExp[dim].AbsIndExp();
                IndExp bInd =reg2.InductionExp[dim].AbsIndExp();
                IndExp cInd = null;
                IndExp dInd = null;
                if (reg1.RegIndInit[dim] == null) {
                    cInd =reg1.RegIndLast[dim].AbsIndExp();
                } else {
                    cInd =reg1.RegIndInit[dim].AbsIndExp();
                }
                if (reg2.RegIndInit[dim] == null) {
                    dInd =reg2.RegIndLast[dim].AbsIndExp();
                } else {
                    dInd =reg2.RegIndInit[dim].AbsIndExp();
                }
                if (aInd.ind_inc != 1) {
                    return -1;
                }
                if (cInd.EQindExp(dInd) == false) {
                    return -1;
                }
                if ((aInd.InitConst == false && bInd.InitConst == true) ||
                    (aInd.InitConst == true  && bInd.InitConst == false)) {
                    long ind_init= 0;
                    long ind_last=0  ;
                    if( aInd.InitConst == true) {
                        ind_init = aInd.ind_init;
                        ind_last = bInd.ind_last;
                    } else {
                        ind_init = bInd.ind_init;
                        ind_last = aInd.ind_last;
                    }
                    if (ind_init <= ind_last) {
                        reg3.ID[dim] =  reg1.REF_ARRAY_REG_INV;
                        reg3.RegIndInit[dim] =   null;
                        reg3.RegIndLast[dim] =   null;
                        reg3.InductionExp[dim].ind_init = ind_init;
                        reg3.InductionExp[dim].ind_last = ind_last;
                        reg3.InductionExp[dim].ind_inc  = 1;
                        reg3.InductionExp[dim].InitConst  = true;
                        reg3.InductionExp[dim].LastConst  = true;
                        reg3.InductionExp[dim].valueConst  = true;
                        reg3.InductionExp[dim].original    = false;
                        reg3.SetdimArea();
                        return 0;
                    }
                }
            }  else {
                return -1;
            }
        }
        if (id2 == reg1.REF_ARRAY_REG_IND_INV) {
            if (id1 == reg2.REF_ARRAY_INV_CONST) {
                if (reg2.RegIndInit[dim] == null) { // for(j=n;j<i;j++);
                    if (reg2.InductionExp[dim].InitConst == true) {
                        if(reg2.InductionExp[dim].ind_init == reg1.ConstValue[dim]) {
                            reg3.ID[dim] = id2;
                            reg3.InductionExp[dim] = reg2.InductionExp[dim];
                            reg3.RegIndInit[dim] = reg2.RegIndInit[dim];
                            reg3.SetdimArea();
                            return 0;
                        }
                    }
                } else { //for (j=i;j<n;j++)
                    if (reg2.InductionExp[dim].LastConst == true) {
                        if(reg2.InductionExp[dim].ind_last == reg1.ConstValue[dim]) {
                            reg3.ID[dim] = id2;
                            reg3.InductionExp[dim] = reg2.InductionExp[dim];
                            reg3.RegIndInit[dim] = reg2.RegIndInit[dim];
                            reg3.RegIndLast[dim] = reg2.RegIndLast[dim];
                            reg3.SetdimArea();
                            return 0;
                        }
                    }
                }
            }
            return -1;
        }
        if (id1 == reg1.REF_ARRAY_INDUCTION) {
            if (id2 == reg2.REF_ARRAY_INDUCTION )  {
                if (reg1.InductionExp[dim].EQindExp(reg2.InductionExp[dim]) == true){
                    return 0;
                }
            }
            return -1 ;
        }
        if (id2 == reg2.REF_ARRAY_INDUCTION) {
            return -1 ;
        }

        //
        // Case UNKNOWN [(safety)]
        //

        if (id1 == reg1.REF_ARRAY_UNKNOWN || id2== reg2.REF_ARRAY_UNKNOWN) {
            return  -1; // (safety)
        }
        if (id1 == reg1.REF_ARRAY_REG_UNKNOWN || id2== reg2.REF_ARRAY_REG_UNKNOWN) {
            return  -1; // (safety)
        }

        //
        // Case INVARIANT -INVARIANT  [case-1]
        //

        if (id1 == reg1.REF_ARRAY_INV_CONST  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (reg1.ConstValue[dim] == reg2.ConstValue[dim]) {
                return 0;
            }
            if (Math.abs((reg1.ConstValue[dim] - reg2.ConstValue[dim])) == 1) {
                // ex)
                //  reg1 x[0]
                //  reg2 x[1]
                //
                //  reg3 x[(0,1,1)]
                //
                reg3.ID[dim] =  reg1.REF_ARRAY_REG_INV;
                reg3.RegIndInit[dim] =   null;
                reg3.RegIndLast[dim] =   null;
                reg3.InductionExp[dim] = new IndExp();
                if (reg1.ConstValue[dim] > reg2.ConstValue[dim]) {
                   reg3.InductionExp[dim].ind_init = reg2.ConstValue[dim];
                } else {
                   reg3.InductionExp[dim].ind_init = reg1.ConstValue[dim];
                }
                reg3.InductionExp[dim].ind_last = reg3.InductionExp[dim].ind_init +1;
                reg3.InductionExp[dim].ind_inc  = 1;
                reg3.InductionExp[dim].InitConst  = true;
                reg3.InductionExp[dim].LastConst  = true;
                reg3.InductionExp[dim].valueConst  = true;
                reg3.InductionExp[dim].original    = false;
                reg3.SetdimArea();
                return 0;
            }
            return  -1;
        }

        IndExp Ind1=null;
        IndExp Ind2=null;
        if (id1 == reg1.REF_ARRAY_REG_INV ) {
            Ind1 = reg1.InductionExp[dim].AbsIndExp();
            if (Ind1.valueConst == false)
                return -1; //  (safety)
        }
        if (id2 == reg2.REF_ARRAY_REG_INV ) {
            Ind2 = reg2.InductionExp[dim].AbsIndExp();
            if (Ind2.valueConst == false)
                return -1; //  (safety)
        }

        //
        // Case INVD-INVC [case-2]
        //


        if (id1 == reg1.REF_ARRAY_REG_INV  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (Ind1.ind_init ==reg2.ConstValue[dim] ||
                Ind1.ind_last ==reg2.ConstValue[dim]) {
                return 0;
            }
            if (Ind1.ind_init < reg2.ConstValue[dim] && Ind1.ind_last > reg2.ConstValue[dim]  &&
                    ((reg2.ConstValue[dim] -Ind1.ind_init) % Ind1.ind_inc) == 0) {
                    // ex [1,7,2] value= 5
                return 0;
            }
            if ((Ind1.ind_init - Ind1.ind_inc) == reg2.ConstValue[dim]) {
                reg3.InductionExp[dim] = Ind1;
                reg3.InductionExp[dim].ind_init = reg2.ConstValue[dim];
                return 0;
            }
            if ((Ind1.ind_last + Ind1.ind_inc) == reg2.ConstValue[dim]) {
                reg3.InductionExp[dim] = Ind1;
                reg3.InductionExp[dim].ind_last = reg2.ConstValue[dim];
                return 0;
            }
            return -1;
        }
        //
        // Case INVC-INVD [case-2]
        //

        if (id2 == reg2.REF_ARRAY_REG_INV  && id1 == reg1.REF_ARRAY_INV_CONST) {
            if (Ind2.ind_init ==reg1.ConstValue[dim] ||
                Ind2.ind_last ==reg1.ConstValue[dim]) {
                reg3.ID[dim] =  id2;
                reg3.InductionExp[dim] = reg2.InductionExp[dim].copy();
                reg3.SetdimArea();
                return 0;
            }
            if (Ind2.ind_init < reg1.ConstValue[dim]   && Ind2.ind_last > reg1.ConstValue[dim]  &&
                ((reg1.ConstValue[dim] -Ind2.ind_init) % Ind2.ind_inc) == 0) {
                reg3.ID[dim] = id2;
                reg3.InductionExp[dim] = Ind2;
                reg3.SetdimArea();
                // ex [1,7,2] value=5
                return 0;
            }
            if ((Ind2.ind_init - Ind2.ind_inc) == reg1.ConstValue[dim]) {
                reg3.InductionExp[dim] = Ind2;
                reg3.InductionExp[dim].ind_init = reg1.ConstValue[dim];
                reg3.SetdimArea();
                return 0;
            }
            if ((Ind2.ind_last + Ind2.ind_inc) == reg1.ConstValue[dim]) {
                reg3.InductionExp[dim] = Ind2;
                reg3.InductionExp[dim].ind_last = reg1.ConstValue[dim];
                reg3.SetdimArea();
                return 0;
            }
            return -1; // (safety)
        }
        //
        // Case INVD-INVD [case-3]
        //

        if (id2 == reg2.REF_ARRAY_REG_INV  && id1 == reg1.REF_ARRAY_REG_INV) {
            if (Ind1.ind_inc == Ind2.ind_inc) {
                if ((Ind2.ind_last < Ind1.ind_init) || (Ind1.ind_last < Ind2.ind_init)) {
                    if (Ind1.ind_inc == 1) {
                        if ((Ind1.ind_last +1) == Ind2.ind_init) {
                            reg3.InductionExp[dim].ind_init =  Ind1.ind_init;
                            reg3.InductionExp[dim].ind_last =  Ind2.ind_last;
                            reg3.InductionExp[dim].original = false;
                            reg3.InductionExp[dim].ind_inc = 1;
                            // ex) [1,7,1]  + [8,10,1] = [1,10,2]
                            return 0;
                        }
                        if ((Ind2.ind_last +1) == Ind1.ind_init) {
                            reg3.InductionExp[dim].ind_init =  Ind2.ind_init;
                            reg3.InductionExp[dim].ind_last =  Ind1.ind_last;
                            reg3.InductionExp[dim].original = false;
                            reg3.InductionExp[dim].ind_inc = 1;
                            // ex)  [8,10,1]  + [1,7,+1]= [1,10,2]
                            return 0;
                        }
                    }
                    return -1;
                } else {
                    if (Math.abs(Ind1.ind_init - Ind2.ind_init) % Ind1.ind_inc == 0) {
                        reg3.InductionExp[dim].ind_init = Math.min(Ind1.ind_init,Ind2.ind_init);
                        reg3.InductionExp[dim].ind_last = Math.max(Ind1.ind_last,Ind2.ind_last);
                        reg3.InductionExp[dim].original = false;
                        reg3.InductionExp[dim].ind_inc = Ind1.ind_inc;
                        // ex) [1,7,2]  + [3,9,2] = [1,9,2]
                        return 0;
                    }
                }
            }
            return -1;
        }
        return  -1;
    }
    /**
    *
    *  regSUB:
    *
    *  The operation method of [ref array] is as follows.
    *
    *                        UNKNOWN     INVARIANT   INDUCTION
    *           UNKNOWN      (safety)   (safety)     (safety)
    *           INVARIANT    (safety)    case-1      reg1-inv
    *           INDUCTION    (safety)    reg1-ind    case-2
    *
    *        ex1)  a[3]  regSUB a[2]     -> case-1
    *        ex2)  a[i]  regSUB a[j+2]   -> case-2
    *                      i and j are  induction variables(REG_INV).
    *
    *
    *
    **/
    public int regSUB(Ref_Array reg1,Ref_Array reg2,Ref_Array reg3,int dim) {
        int id1;
        int id2;

        id1 = reg1.ID[dim];
        id2 = reg2.ID[dim];
        //reg1.DebugArrayRef(fUtil);
        //reg2.DebugArrayRef(fUtil);

        if (id1 == reg1.REF_ARRAY_INDUCTION) {
            if (id2 == reg2.REF_ARRAY_INDUCTION) {
                if (reg1.InductionExp[dim].EQindExp(reg2.InductionExp[dim]) == true) {
                    return -1; // Null
                } else {
                    return 0 ;
                }
            } else {
                return 0 ;
            }
        }
        if (id2 == reg2.REF_ARRAY_INDUCTION) {
                return 0 ;
        }

        //
        // Case UNKNOWN [(safety)]
        //

        if (id1 == reg1.REF_ARRAY_UNKNOWN || id2 == reg2.REF_ARRAY_UNKNOWN) {
            return  0;
        }
        if (id1 == reg1.REF_ARRAY_REG_UNKNOWN || id2 == reg2.REF_ARRAY_REG_UNKNOWN) {
            return  0;
        }
        if (id1 == reg1.REF_ARRAY_REG_IND_INV || id2 == reg2.REF_ARRAY_REG_IND_INV) {
            return  0;
        }
        //
        // Case INVARIANT -INVARIANT  [case-1]
        //

        if (id1 == reg1.REF_ARRAY_INV_CONST  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (reg1.ConstValue[dim] == reg2.ConstValue[dim]) {
                return -1; // NULL
            } else {
                return  0;
            }
        }


        IndExp Ind1=null ;
        IndExp Ind2=null ;
        if (id1 == reg1.REF_ARRAY_REG_INV) {
            Ind1 = reg1.InductionExp[dim].AbsIndExp();
            if (Ind1.valueConst== false)
                    return 0;
        }
        if (id2 == reg1.REF_ARRAY_REG_INV) {
            Ind2 = reg2.InductionExp[dim].AbsIndExp();
            if (Ind2.valueConst== false)
                    return 0;
        }

        if (id1 == reg1.REF_ARRAY_REG_INV  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (Ind1.ind_init == reg2.ConstValue[dim]) {
                reg3.InductionExp[dim] = Ind1;
                reg3.InductionExp[dim].ind_init = Ind1.ind_init+ Ind1.ind_inc;
                if (reg3.InductionExp[dim].ind_init == reg3.InductionExp[dim].ind_last) {
                    reg3.ID[dim]=reg2.REF_ARRAY_INV_CONST;
                    reg3.ConstValue[dim] =reg3.InductionExp[dim].ind_init;
                    reg3.SetdimArea();
                }
                return -2;
            } else  if (Ind1.ind_last == reg2.ConstValue[dim]) {
                reg3.InductionExp[dim] = Ind1;
                reg3.InductionExp[dim].ind_last = Ind1.ind_last- Ind1.ind_inc;
                if (reg3.InductionExp[dim].ind_init == reg3.InductionExp[dim].ind_last) {
                    reg3.ID[dim]=reg2.REF_ARRAY_INV_CONST;
                    reg3.ConstValue[dim] =reg3.InductionExp[dim].ind_init;
                    reg3.SetdimArea();
                }
                return -2;
            }
            return 0;
        }
        if (id1 == reg1.REF_ARRAY_REG_INV  && id2 == reg2.REF_ARRAY_INVARIANT)
            return 0;

        if (id2 == reg2.REF_ARRAY_REG_INV  && id1== reg1.REF_ARRAY_INV_CONST) {
            if (Ind2.ind_inc ==1) {
                if (Ind2.ind_init <= reg1.ConstValue[dim] &&
                    Ind2.ind_last >= reg1.ConstValue[dim]) {
                    return -1; // NULL
                }
            }
            return 0;
        }
        if (id2== reg2.REF_ARRAY_REG_INV  && id1 == reg1.REF_ARRAY_INVARIANT)
                return 0;
        //
        // Case INVD-INVD [case-2]
        //
        if (id2 == reg2.REF_ARRAY_REG_INV  && id1 == reg1.REF_ARRAY_REG_INV) {
            if ((Ind2.ind_last < Ind1.ind_init) || (Ind1.ind_last < Ind2.ind_init)) {
                return 0;
            } else {
                if ((Ind1.ind_inc == Ind2.ind_inc)  ||
                    (Ind1.ind_inc % Ind2.ind_inc ==0) ||
                    (Ind2.ind_inc % Ind1.ind_inc ==0) ) {
                    if ((Math.abs( Ind1.ind_init - Ind2.ind_init) % Ind1.ind_inc) ==0 ) {
                        if (Ind1.ind_inc <= Ind2.ind_inc) {
                            if ((Ind1.ind_init >= Ind2.ind_init) &&
                                (Ind1.ind_last <= Ind2.ind_last)){
                                // ex)
                                //  reg1 [ 10,20,1]
                                //  reg2 [ 0,100,1]
                                //  result = reg1 -reg2 = null
                                return -1;
                            } else {
                                if ((Ind1.ind_init  < Ind2.ind_init) &&
                                    (Ind1.ind_last   < Ind2.ind_last)) {
                                    // ex)
                                    // reg1 [ 0,20,1]
                                    // reg2 [ 10,30,1]
                                    // result = [0,10,1]
                                    reg3.InductionExp[dim] = Ind1;
                                    reg3.InductionExp[dim].ind_last = Ind2.ind_init ;
                                    reg3.InductionExp[dim].ind_inc =  Ind2.ind_inc;
                                    if (reg3.InductionExp[dim].ind_init ==
                                        reg3.InductionExp[dim].ind_last) {

                                        reg3.ID[dim]=reg2.REF_ARRAY_INV_CONST;
                                        reg3.ConstValue[dim] =reg3.InductionExp[dim].ind_init;
                                    }
                                    reg3.SetdimArea();
                                    return -2;
                                }
                                if ((Ind1.ind_init  > Ind2.ind_init) &&
                                    (Ind1.ind_last  > Ind2.ind_last)) {
                                    // ex)
                                    // reg1 [ 10,30,1]
                                    // reg2 [ 0,20,1]
                                    // result = [20,30,1]
                                    reg3.InductionExp[dim] = Ind1;
                                    reg3.InductionExp[dim].ind_init = Ind2.ind_last;
                                    reg3.InductionExp[dim].ind_inc =  Ind2.ind_inc;
                                    if (reg3.InductionExp[dim].ind_init ==
                                        reg3.InductionExp[dim].ind_last) {
                                        reg3.ID[dim]=reg2.REF_ARRAY_INV_CONST;
                                        reg3.ConstValue[dim] =reg3.InductionExp[dim].ind_init;
                                    }
                                    reg3.SetdimArea();
                                    return -2;
                                }
                                if ((Ind1.ind_init  == Ind2.ind_init) &&
                                    (Ind1.ind_last   > Ind2.ind_last)) {
                                    // ex)
                                    // reg1 [ 0,30,1]
                                    // reg2 [ 0,10,1]
                                    // result = [11,30,1]
                                    reg3.InductionExp[dim] = Ind1;
                                    reg3.InductionExp[dim].ind_init = Ind2.ind_last+Ind2.ind_inc;
                                    reg3.InductionExp[dim].ind_inc =  Ind2.ind_inc;
                                    if (reg3.InductionExp[dim].ind_init ==
                                        reg3.InductionExp[dim].ind_last) {
                                        reg3.ID[dim]=reg2.REF_ARRAY_INV_CONST;
                                        reg3.ConstValue[dim] =reg3.InductionExp[dim].ind_init;
                                    }
                                    reg3.SetdimArea();
                                    return -2;
                                }
                                if ((Ind1.ind_init < Ind2.ind_init) &&
                                    (Ind1.ind_last  > Ind2.ind_last)) {
                                    // ex)
                                    // reg1 [ 0,40,1]
                                    // reg2 [ 10,30,1]
                                    // result = [0,40,1]
                                    //
                                    // result1 = [0,10,1]  result2 = [30,40,1]
                                    reg3.InductionExp[dim] = Ind1;
                                    reg3.SetdimArea();
                                    return -2;
                                }
                            }
                        }
                    }
                }
            }
        }
        return  0;
    }
    /**
    *
    *  regMUL:
    *
    *  The operation method of [ref array] is as follows.
    *
    *                        UNKNOWN     INVARIANT   INDUCTION
    *           UNKNOWN      (safety)   (safety)     (safety)
    *           INVARIANT    (safety)    case-1       case-2
    *           INDUCTION    (safety)    case-2       case-3
    *
    *        ex1)  a[3]  regMUL a[2]    -> case-1
    *        ex2)  a[i]  regMUL a[j+2]  -> case-3
    *                          i and j are  induction variables.
    *
    *
    *
    **/
    public int regMUL(Ref_Array reg1,Ref_Array reg2,Ref_Array reg3,int dim) {

        Ref_Array resultArray;
        int id1;
        int id2;

        id1 = reg1.ID[dim];
        id2 = reg2.ID[dim];

        if (id1 == reg1.REF_ARRAY_REG_IND_INV && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (reg1.RegIndInit[dim] == null) { // for(j=n;j<i;j++);
                if (reg1.InductionExp[dim].InitConst == true) {
                    if(reg1.InductionExp[dim].ind_init == reg2.ConstValue[dim]) {
                        reg3.ID[dim]          =  reg2.REF_ARRAY_INV_CONST;
                        reg3.ConstValue[dim]  = reg2.ConstValue[dim];
                        reg3.InductionExp[dim]=  null;
                        reg3.SetdimArea();
                        return 0;
                    }
                }
            } else { //for (j=i;j<n;j++)
                if (reg1.InductionExp[dim].LastConst == true) {
                    if(reg1.InductionExp[dim].ind_last == reg2.ConstValue[dim]) {
                        reg3.ID[dim]          =  reg2.REF_ARRAY_INV_CONST;
                        reg3.ConstValue[dim]  = reg2.ConstValue[dim];
                        reg3.InductionExp[dim]=  null;
                        reg3.SetdimArea();
                        return 0;
                    }
                }
            }
            return -1 ;
        }
        if (id2 == reg2.REF_ARRAY_REG_IND_INV && id1 == reg1.REF_ARRAY_INV_CONST) {
            if (reg2.RegIndInit[dim] == null) { // for(j=n;j<i;j++);
                if (reg2.InductionExp[dim].InitConst == true) {
                    if(reg2.InductionExp[dim].ind_init == reg1.ConstValue[dim]) {
                        reg3.ID[dim]          =  reg1.REF_ARRAY_INV_CONST;
                        reg3.ConstValue[dim]  = reg1.ConstValue[dim];
                        reg3.InductionExp[dim]=  null;
                        reg3.SetdimArea();
                        return 0;
                    }
                }
            } else { //for (j=i;j<n;j++)
                if (reg2.InductionExp[dim].LastConst == true) {
                    if(reg2.InductionExp[dim].ind_last == reg1.ConstValue[dim]) {
                        reg3.ID[dim]          =  reg1.REF_ARRAY_INV_CONST;
                        reg3.ConstValue[dim]  = reg1.ConstValue[dim];
                        reg3.InductionExp[dim]=  null;
                        reg3.SetdimArea();
                        return 0;
                    }
                }
            }
            return -1 ;
        }
        if (id1 == reg1.REF_ARRAY_REG_IND_INV && id2 == reg2.REF_ARRAY_REG_IND_INV) {
            IndExp aInd =reg1.InductionExp[dim].AbsIndExp();
            IndExp bInd =reg2.InductionExp[dim].AbsIndExp();
            IndExp cInd = null;
            IndExp dInd = null;
            if (reg1.RegIndInit[dim] == null) {
                cInd =reg1.RegIndLast[dim].AbsIndExp();
            } else {
                cInd =reg1.RegIndInit[dim].AbsIndExp();
            }
            if (reg2.RegIndInit[dim] == null) {
                dInd =reg2.RegIndLast[dim].AbsIndExp();
            } else {
                dInd =reg2.RegIndInit[dim].AbsIndExp();
            }
            if (aInd.ind_inc != 1) {
                return -1;
            }
            if (cInd.EQindExp(dInd) == false) {
                return -1;
            }
            return 0;

        }
        if (id1 == reg1.REF_ARRAY_INDUCTION) {
            if (id2 == reg2.REF_ARRAY_INDUCTION )  {
                if (reg1.InductionExp[dim].EQindExp(reg2.InductionExp[dim]) == true) {
                    return 0;
                } else {
                    return -1 ;
                }
            } else {
                return -1 ;
            }
        }
        if (id2 == reg2.REF_ARRAY_INDUCTION) {
            return -1 ;
        }


        //
        // Case UNKNOWN [(safety)]
        //

        if (id1 == reg1.REF_ARRAY_UNKNOWN || id2 == reg2.REF_ARRAY_UNKNOWN) {
            return  -1; // NULL
        }
        if (id1 == reg1.REF_ARRAY_REG_UNKNOWN || id2 == reg2.REF_ARRAY_REG_UNKNOWN) {
            return  -1; // NULL
        }

        //
        // Case INVARIANT -INVARIANT  [case-1]
        //
        if (id1 == reg1.REF_ARRAY_INV_CONST  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (reg1.ConstValue[dim] == reg2.ConstValue[dim]) {
                return  0;
            } else {
                return -1; //NULL
            }
        }
        if (id1 == reg1.REF_ARRAY_INVARIANT && id2 == reg2.REF_ARRAY_INVARIANT) {
            if (reg1.EQExpression(reg1.IndexExp[dim],reg2.IndexExp[dim]) == true) {
                return  0;
            } else {
                return -1; // NULL
            }
        }

        IndExp Ind1=null;
        IndExp Ind2=null;


        if (id1 == reg1.REF_ARRAY_REG_INV) {
            Ind1 = reg1.InductionExp[dim].AbsIndExp();
            if (Ind1.valueConst== false)
                    return 0;
        }
        if (id2 == reg1.REF_ARRAY_REG_INV) {
            Ind2 = reg2.InductionExp[dim].AbsIndExp();
            if (Ind2.valueConst== false)
                    return 0;
        }

        //
        // Case INVD-INVC [case-2]
        //

        if (id1 == reg1.REF_ARRAY_REG_INV  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if ((Ind1.ind_init ==reg2.ConstValue[dim]) || (Ind1.ind_last ==reg2.ConstValue[dim]) ){
                reg3.ID[dim]          =  id2;
                reg3.ConstValue[dim]  = reg2.ConstValue[dim];
                reg3.InductionExp[dim]=  null;
                reg3.SetdimArea();
                return 0;
            }
            if (Ind1.ind_init < reg2.ConstValue[dim] && Ind1.ind_last > reg2.ConstValue[dim]  &&
                ((reg2.ConstValue[dim] -Ind1.ind_init) % Ind1.ind_inc) == 0) {
                reg3.ID[dim]          =  id2;
                reg3.ConstValue[dim]  = reg2.ConstValue[dim];
                reg3.InductionExp[dim]=  null;
                reg3.SetdimArea();
                return 0;
            }
            return -1; //  NULL
        }

        //
        // Case INVC-INVD [case-2]
        //

        if (id2 == reg2.REF_ARRAY_REG_INV  && id1 == reg1.REF_ARRAY_INV_CONST) {
            if (Ind2.valueConst== false) {
                return -1; //NULL
            }
            if ((Ind2.ind_init ==reg1.ConstValue[dim]) || (Ind2.ind_last == reg1.ConstValue[dim])){
                return 0;
            }
            if (Ind2.ind_init < reg1.ConstValue[dim] && Ind2.ind_last > reg1.ConstValue[dim]  &&
                ((reg1.ConstValue[dim] -Ind2.ind_init) % Ind2.ind_inc) == 0) {
                return 0;
            }
            return -1; // NULL
        }

        //
        // Case INVD-INVD [case-3]
        //

        if (id2 == reg2.REF_ARRAY_REG_INV  && id1 == reg1.REF_ARRAY_REG_INV) {

            if (Ind1.valueConst == false  || Ind2.valueConst == false) {
                return -1; //NULL
            }

            if ((Ind1.ind_inc == Ind2.ind_inc)  ||
                ((Ind1.ind_inc % Ind2.ind_inc)== 0) || ((Ind2.ind_inc % Ind1.ind_inc)== 0) ) {

                if (((Math.abs(Ind1.ind_init - Ind2.ind_init) % Ind1.ind_inc)  == 0)) {
                    if (Math.max(Ind1.ind_init,Ind2.ind_init)  >
                        Math.min(Ind1.ind_last,Ind2.ind_last) ){
                        return -1; //NULL
                    }  else {
                        reg3.InductionExp[dim] = Ind1;
                        reg3.InductionExp[dim].ind_init =
                        Math.max(Ind1.ind_init,Ind2.ind_init);
                        reg3.InductionExp[dim].ind_last =
                        Math.min(Ind1.ind_last,Ind2.ind_last);
                        reg3.InductionExp[dim].ind_inc = Math.max(Ind1.ind_inc,Ind2.ind_inc);
                        reg3.SetdimArea();
                        return 0;
                    }
                }
                return -1; //NULL
            }
        }
        return -1;
    }
    /**
    *
    *  regMUL2:
    *
    *  The operation method of [ref array] is as follows.
    *
    *                        UNKNOWN     INVARIANT   INDUCTION
    *           UNKNOWN      (safety)   (safety)     (safety)
    *           INVARIANT    (safety)    case-1       case-2
    *           INDUCTION    (safety)    case-2       case-3
    *
    *        ex1)  a[3]  regMUL2 a[2]    -> case-1
    *        ex2)  a[i]  regMUL2 a[j+2]  -> case-3
    *                          i and j are  induction variables.
    *
    *
    *
    **/
    public int regMUL2(Ref_Array reg1,Ref_Array reg2,int dim) {

        Ref_Array resultArray;
        int id1,id2;


        if (reg1.dimension != reg2.dimension)
            return -1;

        id1 = reg1.ID[dim];
        id2 = reg2.ID[dim] ;
        if (id1 == reg1.REF_ARRAY_REG_INV)
            id1 = reg1.REF_ARRAY_INDUCTION ;
        if (id2 == reg2.REF_ARRAY_REG_INV)
            id2 = reg2.REF_ARRAY_INDUCTION ;
        if (id1 == reg1.REF_ARRAY_REG_IND_INV)
            id1 = reg1.REF_ARRAY_INDUCTION ;
        if (id2 == reg2.REF_ARRAY_REG_IND_INV)
            id2 = reg2.REF_ARRAY_INDUCTION ;

        //
        // Case UNKNOWN [(safety)]
        //

        if (id1 == reg1.REF_ARRAY_UNKNOWN || id2 == reg2.REF_ARRAY_UNKNOWN) {
            return  -1;
        }
        if (id1 == reg1.REF_ARRAY_REG_UNKNOWN || id2 == reg2.REF_ARRAY_REG_UNKNOWN) {
            return  -1;
        }
        //
        // Case INVARIANT -INVARIANT  [case-1]
        //
        if (id1 == reg1.REF_ARRAY_INV_CONST  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (reg1.ConstValue[dim] == reg2.ConstValue[dim]) {
                return  -1;
            } else {
                return 0;
            }
        }
        if (id1== reg1.REF_ARRAY_INVARIANT && id2 == reg2.REF_ARRAY_INVARIANT) {
            if (reg1.EQExpression(reg1.IndexExp[dim],reg2.IndexExp[dim]) == true) {
                return  -1;
            } else {
                return 0;
            }
        }
        if (id1 == reg1.REF_ARRAY_INVARIANT)
            return -1;
        if (id2 == reg2.REF_ARRAY_INVARIANT)
            return -1;

        //
        // Case INVD-INVC [case-2]
        //

        if (id1 == reg1.REF_ARRAY_INDUCTION  && id2 == reg2.REF_ARRAY_INV_CONST) {
            if (reg1.InductionExp[dim].InitConst== true) {
                if (reg1.InductionExp[dim].ind_init ==reg2.ConstValue[dim]) {
                    return -1;
                }
            }
            if (reg1.InductionExp[dim].LastConst== true) {
                if (reg1.InductionExp[dim].ind_last ==reg2.ConstValue[dim]) {
                    return -1; // induction :uninitialize
                }
            }
            if (reg1.InductionExp[dim].valueConst== false) {
                return -1; // induction :unInitialize
            }
            if (reg1.InductionExp[dim].ind_init< reg2.ConstValue[dim] &&
                reg1.InductionExp[dim].ind_last > reg2.ConstValue[dim]  &&
                ((reg2.ConstValue[dim] -reg1.InductionExp[dim].ind_init)
                    % reg1.InductionExp[dim].ind_inc) == 0) {
                return -1;
            }
        }

        //
        // Case INVC-INVD [case-2]
        //

        if (id2 == reg2.REF_ARRAY_INDUCTION  && id1 == reg1.REF_ARRAY_INV_CONST) {
            if (reg2.InductionExp[dim].InitConst == true) {
                if (reg2.InductionExp[dim].ind_init == reg1.ConstValue[dim]) {
                    return -1;
                }
            }
            if (reg2.InductionExp[dim].LastConst == true) {
                if (reg2.InductionExp[dim].ind_last == reg1.ConstValue[dim]) {
                    return -1; // induction :uninitialize
                }
            }
            if (reg2.InductionExp[dim].valueConst == false) {
                return -1;
            }
            if (reg2.InductionExp[dim].ind_init < reg1.ConstValue[dim] &&
                reg2.InductionExp[dim].ind_last > reg1.ConstValue[dim]  &&
                ((reg1.ConstValue[dim] -reg2.InductionExp[dim].ind_init) %
                    reg2.InductionExp[dim].ind_inc) == 0) {
                return -1;
            }
        }

        //
        // Case INVD-INVD [case-3]
        //

        if (id2 == reg2.REF_ARRAY_INDUCTION  && id1 == reg1.REF_ARRAY_INDUCTION) {
            if (reg1.InductionExp[dim].valueConst== false ||
                    reg2.InductionExp[dim].valueConst== false) {
                return -1;
            }
            long gcd = Euclid(reg1.InductionExp[dim].ind_inc,reg2.InductionExp[dim].ind_inc);

            //
            // GCD Test
            //
            if ((reg1.InductionExp[dim].ind_init- reg2.InductionExp[dim].ind_init) % gcd != 0) {
                return 0;
            } else {
                long InitMax=Math.max(
                    reg1.InductionExp[dim].ind_init,reg2.InductionExp[dim].ind_init);
                long LastMin=Math.min(
                    reg1.InductionExp[dim].ind_last,reg2.InductionExp[dim].ind_last);
                if (InitMax > LastMin) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
        return  0;
    }
    /**
    *
    * Euclid:
    *
    *
    **/
    private long Euclid( long a,long b) {
        long g;
        long g1=Math.max(a,Math.abs(b));
        long g2=Math.min(a,Math.abs(b));
        do {
            g= g1 % g2;
            g1 = g2;
            g2 =g;
        } while (g != 0);
        return g1;
    }
}
