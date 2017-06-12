/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import coins.ir.hir.AssignStmt;
import coins.ir.hir.VarNode;
    /**
    *
    * Basic inducion table
    *
    * Linked from IndList (element of IndList) of LoopTable.
    *
    **/
public class BasicInduction  {

    AssignStmt stmt;        // j(DEF) = j(USE) +n
    VarNode DefVarNode;     // j(DEF)
    VarNode UseVarNode;     // j(USE)
    long inc;               // n (increment value)

    boolean InitConstFlag;  // true if initial value is constant.
    VarNode InitConstNode;  // Initial value node.
    long    InitConstValue; // Initial value (if InitConstFlag is true)
    boolean LastConstFlag;  // true if last value is constant.
    long    LastConstValue; // Last value.
    boolean loop_ctr;       // Treu if this is loop control variable.
    LinkedList indExpList;  // List of induction expressions (IndExp).
    LinkedList InitDefList; // Unused ?
    /**
    *
    * BasicInducion:
    *
    * basic induction table
    *
    * @param pstmt HIR-Assign
    * @param pDefVarNode HIR-DefNode
    * @param pUseVarNode HIR-UseNode
    * @param pinc  The number of incriments.
    *
    **/
    BasicInduction (AssignStmt pstmt,VarNode pDefVarNode,VarNode pUseVarNode,long pinc)
    {
        stmt = pstmt;
        DefVarNode = pDefVarNode;
        UseVarNode = pUseVarNode;
        InitConstFlag = false;
        LastConstFlag = false;
        inc = pinc;
        indExpList = new LinkedList();
        InitDefList = new LinkedList();
    }
    /**
    *
    * DebugInduction:
    *
    *  induction table is traced.
    *
    * @param pUtil Utility object
    */
    void DebugInduction(LoopUtil pUtil)
    {
        pUtil.Trace("-----DebugInduction------",5);
        pUtil.Trace("DefVerNode="+DefVarNode.toString(),5);
        if (InitConstNode != null)
            pUtil.Trace("InitVerNode="+InitConstNode.toString(),5);
        pUtil.Trace("inc="+inc,5);
        pUtil.Trace("loop_ctr="+ loop_ctr,5);
        pUtil.Trace("InitConstFlag="+ InitConstFlag,5);
        pUtil.Trace("LastConstFlag="+ LastConstFlag,5);
        pUtil.Trace("InitConstValue="+InitConstValue,5);
        pUtil.Trace("LastConstValue="+LastConstValue,5);
        pUtil.Trace("indExpSize="+indExpList.size(),5);
        for(Iterator Ie = indExpList.iterator();Ie.hasNext();) {
            IndExp Ind= (IndExp)Ie.next();
            Ind. DebugIndExp(pUtil);
        }
        pUtil.Trace("------------------",5);

    }
    /**
    *
    * SetInductionLastData:
    *
    * A lastvalue is set up.
    *
    * @param LastValue  lastvalue
    * @param pUtil Utility object
    **/
    public void SetInductionLastData( long LastValue,LoopUtil pUtil) {

        pUtil.Trace("SetInduction LastData Var"+DefVarNode.toString(),5);     // j(DEF)
        if (InitConstFlag == false && Math.abs(inc) != 1)  {
            return ;
        }
        //
        // COMMENT:
        // ---------------
        // case1) init == n
        // ---------------
        //
        // for(i=2;i<9;i++) {
        //
        // for (v0=0;v<7;v++) { // repeat_no_normalize = 7 (LastValue =6)
        //  i= 1*v0 +2 ------->  LastConstValue= LastValue*inc+ InitConstValue= 6*1+2=8
        //
        // for(i=11;i>9;i--) {
        //
        // for (v0=0;v<2;v++) { // repeat_no_normalize = 2 (LastValue =1)
        //  i= (-1)*v0 +11 ------->  LastConstValue= LastValue*inc+ InitConstValue= 1*-1+11=1
        //
        //
        //
        //
        // ---------------
        // case2 (init =x)  and inc = 1  and (x< 9)
        // ---------------
        //
        // for(i=x;i<9;i++) {
        //
        //  i=x
        //  v0 =i
        // for (v1=0;v1<(9-v0);v1++) { // repeat_no_normalize = 9 (LastValue =8-v0)
        //  i= 1*v1 +v0 ------->  LastConstValue= LastValue*inc+ InitConstValue
        //                                        (8-v0)   *1  + v0            = 8
        //
        // ---------------
        // case3 (init =x)  and inc = 1  and (x< 9)
        // ---------------
        //
        // for(i=x;i>9;i--) {
        //
        // i=x
        // v0 =i
        // for (v1=0;v1<(9-v0)*-1;v1++) { // repeat_no_normalize = -9 (LastValue =-10+v0)
        //  i= -1*v1 +v0 ------->  LastConstValue= LastValue*inc  + InitConstValue
        //                                         (-10+v0)  *-1+ v0       = 10
        //
        if (InitConstFlag == true) {
            LastConstFlag = true;
            LastConstValue = LastValue*inc +InitConstValue;
        } else {
            LastConstFlag = true;
            LastConstValue = LastValue ;
        }
        pUtil.Trace("Inductin LastData Value="+LastConstValue,5);     // j(DEF)

        for(ListIterator Ie =indExpList.listIterator(); Ie.hasNext();) {
            IndExp indExpTable= (IndExp)Ie.next();
            pUtil.Trace("Inductin LastData (2)ind ="+indExpTable.ind_inc,5) ;
            //
            // COMMENT:
            //
            //  ----------------------------------------------------------
            //                   Expression:
            //  a[3*j +6] =x --> ind_inc=3,ind_init = +6
            //                   ind_last = LastValue*ind_inc + ind_init ;
            //                            = LastValue(j)*3 + (+6) ;
            //  ----------------------------------------------------------
            //
            //---------
            // EXSAMPE1
            //---------
            //
            // [Original Code]
            // j= 7;
            // for(i=1;i<9;i++) {
            //   a[3*j+6] = x;
            //   j= j+2;
            //  }
            //
            // [Convert]
            // j=7
            // i=1
            // for (v0=0;v<8;v++) { // repeat_no_normalize = 8(LastValue=7)
            //  i= 1*v0 +1
            //  j= 2*v0 +7
            //  a[3*j +6] =x
            // }
            //
            //  [Induction Value]
            //   V0      0   1   2  3  4  5  6   7
            //   I       1   2   3  4  5  6  7   8
            //   J       7   9  11 13 15 17 19  21
            //   3*j+6  27  33  39 45 51 57 63  69
            //
            //          INIT STEP   LAST
            //   V0      0    1        7 :LastValue(repet_no-1)
            //   I       1    1        8 :LastValue * 1 +1
            //   J       7    2       21 :LastValue * 2 +7
            //   3*j+6  27    6       69 :LastValue * 6 + 27
            //
            //   3*j+6 = 3*(2*V0+7) +6 = 6*V0+21+6 = 6*V0+21
            //
            //
            //---------
            // EXSAMPE2
            //---------
            //
            // [Original Code]
            // j= 7;
            // i=0
            // for(i=1;i<9;i++) {
            //   j= j+2;
            //   a[3*j+6] = x;
            //  }
            //
            // [Convert]
            // j=7
            // i=1
            // for (v0=0;v<8;v++) { // repeat_no_normalize = 8(LastValue=7)
            //  i= 1*v0 +1
            //  j= 2*v0 +7
            //  a[3*(j+2) +6] =x
            // }
            //
            //  [Induction Value]
            //   V0           0   1   2  3  4  5  6   7
            //   I            1   2   3  4  5  6  7   8
            //   J            7   9  11 13 15 17 19  21
            //   3*(j+2)+6    33  39 45 51 57 63  69 75
            //
            //                INIT STEP   LAST
            //   V0            0    1        7 :LastValue(repet_no-1)
            //   I             1    1        8 :LastValue * 1 +1
            //   J             7    2       21 :LastValue * 2 +7
            //   3*(j+2)+6    33    6       75 :LastValue * 6 +  33
            //
            //   3*(j+2)+6 = 3*(2*V0+7+2) +6 = 6*V0+27+6 = 6*V0+33
            //
            //
            //---------
            // EXSAMPE3
            //---------
            //
            // [Original Code]
            // j= 7;
            // for(i=1;i<9;i=i+3) {
            //   j= j+2;
            //   a[3*j+6] = x;
            //  }
            //
            // [Convert]
            // j=7
            // i=1
            // for (v0=0;v<3;v++) { // repeat_no_normalize =3 (LastValue=2)
            //  i= 3*v0 +1
            //  j= 2*v0 +7
            //  a[3*(j+2) +6] =x
            // }
            //
            //  [Induction Value]
            //   V0           0   1   2
            //   I            1   4   7
            //   J            7   9  11
            //   3*(j+2)+6    33  39 45
            //
            //                INIT STEP   LAST
            //   V0            0    1        2 :LastValue(repet_no-1)
            //   I             1    3        7 :LastValue * 1 +1
            //   J             7    2       11 :LastValue * 2 +7
            //   3*(j+2)+6    33    6       45 :LastValue * 6 +  33
            //
            //   3*(j+2)+6 = 3*(2*V0+7+2) +6 = 6*V0+27+6 = 6*V0+33
            //
            if (indExpTable.InitConst == true) {
                indExpTable.ind_last = LastValue * indExpTable.ind_inc + indExpTable.ind_init ;
                indExpTable.valueConst = true;
                indExpTable.LastConst = true;
            } else {
                indExpTable.ind_last = LastValue * indExpTable.ind_inc + indExpTable.ind_init ;
                indExpTable.LastConst= true;
                indExpTable.ind_init =  0;
            }
            pUtil.Trace("Inductin LastData *LastValue ="+LastValue,5) ;
            pUtil.Trace("Inductin LastData *ind_inc ="+indExpTable.ind_inc,5) ;
            pUtil.Trace("Inductin LastData *ind_init ="+indExpTable.ind_init,5) ;
            pUtil.Trace("Inductin LastData *ind_last ="+indExpTable.ind_last,5) ;
        }
    }

  //##71 BEGIN
  public VarNode
    getVarNode()
  {
    return DefVarNode;
  }
  //##71 END
}
