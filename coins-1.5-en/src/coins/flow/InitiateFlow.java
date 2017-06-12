/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
//##60 package coins.aflow;
package coins.flow; //##60

import java.util.ArrayList;
import java.util.List;

import coins.sym.FlowAnalSym;

/**
 * Initiate data flow analysis. this class's initiate(SubpFlow) method should be called before doing other flow analyses.
 */
public abstract class InitiateFlow //##60
{
  protected List fBBlockList = new ArrayList(); // List of BBlocks relevant for DFA //##11
  //##65 protected FlowAnalSym[] fFlowAnalSymTable; //##11

  abstract void initiateDataFlow(SubpFlow pSubpFlow);
}
