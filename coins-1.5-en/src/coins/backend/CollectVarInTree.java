/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.ana.EnumRegVars;
import coins.backend.lir.LirNode;
import coins.backend.lir.PickUpVariable;
import coins.backend.util.NumberSet;


/** Collect variables appeared in LirNode tree. **/
public class CollectVarInTree implements PickUpVariable {

  private Root root;
  private NumberSet pickUpWork;
  // private TargetMachine target;
  private EnumRegVars rn;

  /** Create CollectVarInTree instance for L-function <code>func</code>. */
  public CollectVarInTree(Function func) {
    root = func.root;
    // target = func.module.targetMachine;
    rn = (EnumRegVars)func.require(EnumRegVars.analyzer);
  }

  /** Call-back entry for interface PickUpVariable. **/
  public void meetVar(LirNode node) {
    int v = rn.index(node);
    if (v != 0)
      pickUpWork.add(v);
  }

  /** Collect used variables in LirNode tree <code>stmt</code>
   *   Variables are accumulated in set <code>work</code>. */
  public void getUseVars(LirNode stmt, NumberSet work) {
    work.clear();
    pickUpWork = work;
    stmt.pickUpUses(this);
  }

  /** Collect defined variables in LirNode tree <code>stmt</code>
   *   Variables are accumulated in set <code>work</code>. */
  public void getDefVars(LirNode stmt, NumberSet work) {
    work.clear();
    pickUpWork = work;
    stmt.pickUpDefs(this);
  }

}
