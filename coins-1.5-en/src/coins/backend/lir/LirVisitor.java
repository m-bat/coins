/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

/** Visitor interface for LirNode */
public interface LirVisitor {
  void visit(LirFconst node);
  void visit(LirIconst node);
  void visit(LirSymRef node);
  void visit(LirLabelRef node);
  void visit(LirUnaOp node);
  void visit(LirBinOp node);
  void visit(LirNaryOp node);
}

