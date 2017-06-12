/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.CompileError;
import coins.ir.hir.Stmt;
import coins.sym.Const;


//                      (##11 ): modified on Jan  2002.

/** BBlockHir interface: //##11
 *  Interface for HIR basic block.
 *
**/
public interface BBlockHir extends BBlock {
    /** insertLoopPreheader:
     *  Insert a basic block as the loop preheader of the loop
     *  starting with this basic block.
     *  The inserted loop preheader block has this block as its
     *  only one successor.
     *  Edges from predecessors of this block are changed to
     *  go to the inserted preheader except loop back edge.
     *  The loop back edge goes to this block as it is before
     *  the invocation of this method.
     *  Branch addresses of this block and its predecessors are
     *  changed so that consistency is kept.
     *  "this" should be a loop header basic block that dominates all
     *  basic blocks in the loop.
     *  @return the inserted basic block.
     **/
    BBlock insertLoopPreheader();

    /** insertConditionalInitPart:
     *  Insert a basic block as the conditional initiation block
     *  (conditionalInitBlock) of this loop (the loop starting with this
     *  basic block having loop header flag).
     *  The inserted conditionalInitBlock is preceeded by the
     *  expression statement built by copying the start condition of
     *  the loop so that the conditionalInitBlock is executed only when
     *  the start condition is satisfied.
     *  Edges from predecessors of this block are changed to
     *  go to the inserted start condition except loop back edge.
     *  The destination of the loop back edge is not changed.
     *  The conditionalInitBlock goes to loop body part of this loop
     *  treating the body part as its only one successor.
     *  Branch addresses of this block and its predecessors are
     *  changed so that consistency is kept.
     *  "this" should be a loop header basic block that dominates all
     *  basic blocks in the loop.
     *  @return the inserted conditionalInitBlock.
     **/
    public BBlock insertConditionalInitPart() throws CompileError;

    /** addSwitchCase:
     *  Add case selection part of switch statement contained in this
     *  block and adjust linkages between basic blocks
     *  (to change multiway jumps in HIR).
     *  If basic blocks corresponding to pLabeledStmt of the case
     *  selection part is not yet constructed, then they should be
     *  constructed before calling
     *  addSwitchCase.
     *  @param pConst: case selection constant.
     *  @param pLabeledStmt: labeled statement which is to be executed
     *      when case selection expression is evaluated as pConst.
     **/
    void addSwitchCase(Const pConst, Stmt pLabeledStmt);

    /** deleteSwitchCase:
     *  Delete a case selection constant of switch statement contained
     *  in this block. If all constants of a case selection statement
     *  are deleted, then the case selection statement itself is also
     *  deleted and linkages between basic blocks are adjusted
     *  (to change multiway jumps in HIR).
     *  @param pconst: case selection constant which is to be deleted.
     **/
    void deleteSwitchCase(Const pConst);

//##25 BEGIN

  public Stmt
  getFirstStmt();

  public Stmt
  getLastStmt();

//##25 END

}
 // BBlockHir interface
