/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;

/*
 * Substring Exp
 */

public class SubstringNode extends Pair {
  Node fBody;
  Pair fSubs;
  /**
   * @param p1: body
   * @param p2: subscripted
   * @param pfHir
   */
  public SubstringNode(Node p1, Pair p2, FirToHir pfHir) {
    super(p1, p2, pfHir);
    fBody = p1;
    fSubs = p2;
  }

  /* makeArgAddr
   * @see ffront.Node#makeArgAddr(ffront.FStmt)
   */
  public Exp makeArgAddr(FStmt pFStmt) {
    FortranCharacterExp exp = (FortranCharacterExp)makeExp();
    return exp.getBody();
  }

  /* makeExp
   * @see ffront.Node#makeExp()
   */
  public Exp makeExp(){
    HIR hir = fHir.getHir();
    Exp e1, e2;
    Exp idx;
    FortranCharacterExp body =
      fHir.getTypeUtility().castFortranCharacterExp(fBody.makeExp());

    if(fSubs.getLeft() != null){
      e1 = fSubs.getLeft().makeExp();
    }
    else{
      e1 = fHir.getHirUtility().makeIntConstNode(1);
    }
    
    if(fSubs.getRight() != null){
      e2 = fSubs.getRight().makeExp();
    }
    else{
      e2 = body.getLength();
    }
    
    idx = hir.exp(HIR.OP_SUB,
                  e1,
                  fHir.getHirUtility().makeIntConstNode(1));
    
    /**
     * TODO: ??
     */
    return fHir.getHirUtility().makeFortranCharacterExp(
      hir.exp(HIR.OP_ADD, body.getBody(), idx),
      hir.exp(HIR.OP_SUB, e2,
              hir.exp(HIR.OP_SUB,
                      e1, fHir.getHirUtility().makeIntConstNode(1))));
  }
}

