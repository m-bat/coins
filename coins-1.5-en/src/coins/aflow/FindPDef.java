/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindMod.java
 *
 * Created on July 24, 2002, 11:21 AM
 */
package coins.aflow;

import java.util.Set;

import coins.sym.FlowAnalSym;


/**
 * Finds the "PDef" vector for a BBlcok or a SetRefRepr. The PDef set for a BBlock this class finds always contains SetRefReprs that have subprogram calls. The PDef set for a SetRefRepr always contains the SetRefRepr if it has subprogram calls.
 *
 * @author  hasegawa
 */
public class FindPDef extends FindDef {
    /** Creates new FindMod */
    public FindPDef(FlowResults pResults) {
        super(pResults);
    }

    protected boolean addDefs(Set pDefSet, SetRefRepr pSetRefRepr) {
        FlowAnalSym lDefSym = (FlowAnalSym) pSetRefRepr.defSym();



        if (lDefSym != null) {


            if(pDefSet.contains(lDefSym) == true) { // add 2004.10.06 I.Fukuda
               return false;
            }
            pDefSet.add(lDefSym);
        }

        return pSetRefRepr.sets() ||
               //##71 pSetRefRepr.hasCall();
               pSetRefRepr.hasCallWithSideEffect(); //##71
    }

    protected void register(BBlock pBBlock, DefVector pVect) {
        pBBlock.setPDef(pVect);
    }

    public void find(SetRefRepr pSetRefRepr) {
        SubpFlow lSubpFlow = pSetRefRepr.getBBlock().getSubpFlow();
        DefVector lDef = flow.defVector(lSubpFlow);

        if (pSetRefRepr.sets() ||
            //##71 pSetRefRepr.hasCall())
            pSetRefRepr.hasCallWithSideEffect())  //##71
        {
            lDef.setBit(lSubpFlow.getSetRefReprs().indexOf(pSetRefRepr));
        }

        fResults.put("PDef", pSetRefRepr, lDef);
    }
}
