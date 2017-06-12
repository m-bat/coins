/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FindPEKill.java
 *
 * Created on March 13, 2003, 5:54 PM
 */
package coins.aflow;

import coins.alias.RecordAlias; //##53
import java.util.Set;


/**
 *
 * @author  hasegawa
 */
public class FindPEKill extends FindEKill {
    /** Creates a new instance of FindPEKill */
    public FindPEKill(FlowResults pResults) {
        super(pResults);
    }

    protected void addEKill(Set pEKillSet, FlowExpId pFlowExpId, BBlock pBBlock) {
        if (flow.fSubpFlow != null) {                                 //##53
          RecordAlias lRecordAlias = flow.fSubpFlow.getRecordAlias(); //##53
          if (lRecordAlias != null) {                                 //##53
            addEKill(pEKillSet, pFlowExpId, pBBlock, lRecordAlias);   //##53
            return;                                                   //##53
          }                                                           //##53
        }                                                             //##53
        flow.dbg(5, "\n addEKill " + pFlowExpId.toString() + " " + pBBlock);  //##57
        FlowAnalSymVector lPDefined = pBBlock.getPDefined();
        flow.dbg(6, "definedSyms " + lPDefined.toString());  //##57
        Set lOperands = pFlowExpId.getOperandSet0();
        flow.dbg(6, " operands " + lOperands.toString());  //##57
        FlowAnalSymVector lUseVarVect = FlowAnalSymVectorImpl.forSet(lOperands,
                pBBlock.getSubpFlow());
        lUseVarVect.vectorAnd(lPDefined, lUseVarVect);

        if (!lUseVarVect.isZero()) {
          pEKillSet.add(pFlowExpId);
          flow.dbg(5, " add to EKill " + pFlowExpId.toStringShort());
        }
    }

   protected void register(BBlock pBBlock, ExpVector pEKill) {
        flow.dbg(5, " setPEkill " + pEKill.toStringShort() + "\n");    //##53
        pBBlock.setPEKill(pEKill);
    }

    protected boolean kills(SetRefRepr pSetRefRepr, FlowExpId pFlowExpId) {
        RecordAlias lRecordAlias = flow.fSubpFlow.getRecordAlias();   //##53
        if (lRecordAlias != null)                                     //##53
          return killsByAlias(pSetRefRepr, pFlowExpId, lRecordAlias); //##53
        Set lModSyms = pSetRefRepr.modSyms00();

        return lModSyms.removeAll(pFlowExpId.getOperandSet0());
    }

//##53 BEGIN

protected void
addEKill(Set pEKillSet, FlowExpId pFlowExpId, BBlock pBBlock,
                   RecordAlias pRecordAlias)
{
  flow.dbg(5, "\n addEKill " + pFlowExpId.toString() + " " + pBBlock);  //##57
  RecordAlias lRecordAlias = flow.fSubpFlow.getRecordAlias(); //##56
  FlowAnalSymVector lPDefined = pBBlock.getPDefined();  // modSymsStmt ??
  Set lDefinedSyms = lPDefined.flowAnalSyms();  // Defined symbols.
  flow.dbg(6, "definedSyms " + lDefinedSyms.toString());  //##57
  Set lAliasSymGroup = lRecordAlias.aliasSymGroup(lDefinedSyms);  //##56
  flow.dbg(6, " aliasSymGroup " + lAliasSymGroup.toString());  //##57
  FlowAnalSymVector lAliasVect = FlowAnalSymVectorImpl.forSet(lAliasSymGroup,
     pBBlock.getSubpFlow()); // Change lAliasSymGroup to FlowAnalSymVector. //##56
  lPDefined.vectorOr(lAliasVect, lPDefined); // Treat aliases to be defined.
  Set lOperands = pFlowExpId.getOperandSet0(); // Get operands of the expression.
  flow.dbg(6, " operands " + lOperands.toString());  //##57
  FlowAnalSymVector lUseVarVect = FlowAnalSymVectorImpl.forSet(lOperands,
  pBBlock.getSubpFlow());  // Change lOperands to FlowAnalSymVector.
  lUseVarVect.vectorAnd(lPDefined, lUseVarVect); // Check if any operand
      // of the expression represented by pFlowExpId is in the defined symbols
      // or its aliases.
  flow.dbg(5, " addEKill " + pFlowExpId.toString() + " DefinedByAlias [" +
           lPDefined.toStringShort()+ "]");
  if (!lUseVarVect.isZero()) { // If some operand is defined, then
      // treat the expression as killed.
    pEKillSet.add(pFlowExpId);
    flow.dbg(5, " add to EKill " + pFlowExpId.toStringShort());
  }
} // addEKill

protected boolean
killsByAlias(SetRefRepr pSetRefRepr, FlowExpId pFlowExpId,
                 RecordAlias pRecordAlias )
{
  Set lModSyms = pSetRefRepr.modSyms00();
  Set lModSymsAlias = pRecordAlias.aliasSymGroup(lModSyms);
  flow.dbg(5, " killsByAlias " + pFlowExpId.toString() + " mod " + lModSymsAlias);
  boolean lChanged = lModSymsAlias.removeAll(pFlowExpId.getOperandSet0());
  return lChanged;
} // killsByAlias

//##53 END

    protected void register(SetRefRepr pSetRefRepr, ExpVector pEKill) {
      flow.dbg(2, " putPEkill " + pEKill);    //##56
        fResults.put("PEKill", pSetRefRepr, pEKill);
    }
}
