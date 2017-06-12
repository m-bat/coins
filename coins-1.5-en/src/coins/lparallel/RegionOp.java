/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
//##70 import coins.aflow.FlowResults;
///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] RegionOpInterface
//
//  Array region operations ?
///////////////////////////////////////////////////////////////////////////////
public interface RegionOp {
    /**
    *
    *  regADD:
    *
    *
    **/
    public int regADD(Ref_Array reg1,Ref_Array reg2,Ref_Array reg3,int dim);
    /**
    *
    *  regSUB:
    *
    **/
    public int regSUB(Ref_Array reg1,Ref_Array reg2,Ref_Array reg3,int dim);
    /**
    *
    *  regMUL:
    *
    *
    **/
    public int regMUL(Ref_Array reg1,Ref_Array reg2,Ref_Array reg3,int dim);
    /**
    *
    *  regMUL2:
    *
    *
    **/
    public int regMUL2(Ref_Array reg1,Ref_Array reg2,int dim);
}
