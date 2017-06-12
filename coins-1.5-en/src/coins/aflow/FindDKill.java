/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindDKill.java
 *
 * Created on March 12, 2003, 4:35 PM
 */
package coins.aflow;

import coins.sym.FlowAnalSym;
import coins.alias.RecordAlias; //##53
import java.util.Set;           //##53


/**
 * Finds the "DKill" vector for a BBlock or SetRefRepr. Th DKill set for a SetRefRepr this class finds is empty if the SetRefRepr is not an AssignStmt with its LHS a VarNode; if it is, the DKill set contains those SetRefReprs that are AssignStmts and have the same LHS as the SetRefRepr in question.
 * @author  hasegawa
 */
public class FindDKill extends FindKill {
    /** Creates a new instance of FindDKill */
    public FindDKill(FlowResults pResults) {
        super(pResults);
    }

    protected boolean kills(SetRefRepr pKiller, SetRefRepr pKillee) {
        if (flow.fSubpFlow != null) {                               //##57
          RecordAlias lRecordAlias = flow.fSubpFlow.getRecordAlias(); //##53
          if (lRecordAlias != null) //##53
            return killsByAlias(pKiller, pKillee, lRecordAlias); //##53
        }  //##57
        FlowAnalSym lDefSym = pKiller.defSym();

        if ((lDefSym != null) && (lDefSym == pKillee.defSym())) {
            return true;
        }

        return false;
    }

//##53 BEGIN
protected boolean
killsByAlias(SetRefRepr pKiller, SetRefRepr pKillee, RecordAlias pRecordAlias)
{
  FlowAnalSym lKillerDefSym = pKiller.defSym();
  FlowAnalSym lKilleeDefSym = pKillee.defSym();
  //## if ((lDefSym != null) && (lDefSym == pKillee.defSym()))
  //##   return true;
  if (lKillerDefSym != null) {
    Set lModSyms = pKiller.modSyms00();
    Set lModSymsAlias = pRecordAlias.aliasSymGroup(lModSyms);
    flow.dbg(5, " killsByalias killer " + lModSymsAlias +
             " killee " + lKilleeDefSym);
    if (lModSymsAlias.contains(lKilleeDefSym))
      return true;
  }
  return false;
}

//##53 END

    protected void register(SetRefRepr pSetRefRepr, DefVector pDefVect) {
        fResults.put("DKill", pSetRefRepr, pDefVect);
    }

    protected DefVector getKill(SetRefRepr pSetRefRepr) {
        return (DefVector) fResults.get("DKill", pSetRefRepr);
    }

    protected void register(BBlock pBBlock, DefVector pDefVect) {
        pBBlock.setDKill(pDefVect);
    }
}
