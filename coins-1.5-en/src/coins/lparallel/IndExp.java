/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import coins.ir.hir.Exp;
import coins.ir.hir.VarNode;
////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] inductin Exp
//
//  Induction expression table.
////////////////////////////////////////////////////////////////////////
class IndExp  {
    public BasicInduction IndTable; // Induction Table
    public Exp ind_node;            // Induction expression (HIR_OP_XX Node)
    public VarNode InitConstNode;   // Inital value node.
    public boolean InitConst;       // true: Const
    public boolean LastConst;       // true: Const
    public boolean valueConst;      // true:  InitConst== true and LastConst== true
    public boolean original;        // Not a copy ?
    public long ind_inc;            // Increment value.
    public long ind_init;           // Initial value,
    public long ind_last;           // Last value.
    /**
    *
    * IndExp:
    *
    *
    **/
    IndExp()
    {
        ind_inc = 0;
        ind_init = 0;
        ind_last = 0;
        valueConst = false;
        InitConst = false;
        LastConst = false;
        original   = true;
        ind_node =  null;
        InitConstNode = null;
    }
    /**
    *
    * setExpData:
    *
    *
    **/
    void setExpData(Exp node,long inc,long init,long last,
                        boolean pvalueConst,VarNode InitNode)
    {
        ind_node = node;
        ind_inc = inc;
        ind_init = init;           // init + null(0) (if valueConst == false)
        ind_last = last;
        InitConst = pvalueConst;
        InitConstNode  = InitNode; // tmp# = Use     (if valueConst == false)
    }
    /**
    *
    * copy:
    *
    *
    **/
    IndExp copy()
    {
        IndExp Ind;
        Ind = new IndExp();
        Ind.IndTable=IndTable;
        Ind.ind_node = ind_node;
        Ind.InitConstNode = InitConstNode;
        Ind.valueConst  = valueConst;
        Ind.InitConst  = InitConst;
        Ind.LastConst  = LastConst;
        Ind.ind_inc = ind_inc;
        Ind.ind_init = ind_init;
        Ind.ind_last = ind_last;
        Ind.original = false;
        return Ind;
    }
    /**
    *
    * AdsIndExp:
    *
    *
    **/
    IndExp AbsIndExp()
    {
        IndExp Ind= copy();
        if(ind_inc < 0) {
            Ind.ind_inc *= -1;
            Ind.ind_init = ind_last;
            Ind.ind_last = ind_init;
            Ind.InitConst  = LastConst;
            Ind.LastConst  = InitConst;
        }
        return Ind;
    }
    /**
    *
    *  EQindExp:
    *
    *
    **/
    boolean EQindExp(IndExp a) {
        if (a.valueConst != valueConst)
            return false;
        if (a.InitConst != InitConst)
            return false;
        if (a.ind_inc != ind_inc)
            return false;
        if (a.ind_init != ind_init)
            return false;
        if (a.ind_last != ind_last)
                return false;
        return true;
    }
    /**
    *
    * DebugIndExp:
    *
    *
    */
    void DebugIndExp(LoopUtil pUtil)
    {
            pUtil.Trace("**DebugIndExp",5);
            pUtil.Trace("   Original="+original,5);
            pUtil.Trace("   valueConst="+valueConst,5);
            pUtil.Trace("   InitConst="+InitConst,5);
            pUtil.Trace("   LastConst="+LastConst,5);
            if(ind_node == null)
                pUtil.Trace("IndNode=(NULL)",5);
            else
                pUtil.Trace("IndNode="+ind_node.toString(),5);
            pUtil.Trace("   init="+ind_init,5);
            pUtil.Trace("   last="+ind_last,5);
            pUtil.Trace("   inc="+ind_inc,5);
    }
}
