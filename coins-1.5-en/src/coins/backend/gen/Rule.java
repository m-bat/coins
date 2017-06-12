/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.gen;

import coins.backend.util.ImList;


/**
 ** Production rule body.
 **/
class Rule {
  /** Produciton rule number **/
  final int id;

  /** Visible form of production **/
  final String def;

  /** Object code in S-expression **/
  final ImList code;

  /** Value code in S-expression **/
  final ImList value;

  /** Clobber list **/
  final ImList clobber;

  /** Chain rule flag **/
  final boolean isChain;

  /** Derived rule flag **/
  final boolean isDerived;

  /** Left hand side nonterminal **/
  final int lhs;

  /** Nonterminals appear in right hand side
   ** (not including ones in subrules). **/
  final int[] subgoals;

  /** Register set name for each nonterminal.
   ** (including ones in subrules).
   ** regset[0] is for lhs. **/
  final String[] regsets;

  /** Register equation.
   ** Assign $n to same register as $0 if (((eqregs >> n) & 1) == 1). **/
  final long eqregs;

  /** Use after def flag. **/
  final boolean useAfterDef;

  /** Has delay slot. **/
  final boolean hasDelaySlot;

  /** Create Rule object. **/
  Rule(int id, boolean isChain, boolean isDerived, int lhs, String def,
       ImList code, ImList value, ImList clobber, long eqregs,
       boolean useAfterDef, boolean hasDelaySlot,
       int[] subgoals, String[] regsets) {
    this.id = id;
    this.isChain = isChain;
    this.isDerived = isDerived;
    this.lhs = lhs;
    this.def = def;
    this.code = code;
    this.value = value;
    this.clobber = clobber;
    this.subgoals = subgoals;
    this.regsets = regsets;
    this.eqregs = eqregs;
    this.useAfterDef = useAfterDef;
    this.hasDelaySlot = hasDelaySlot;
  }

  /** Return number of children of this production. **/
  // int nKids() { return subgoals.length; }

  /** Return Nth subgoal. **/
  // int subgoal(int nth) { return subgoals[nth]; }

  /** Visualize **/
  public String toString() { return def; }
}
