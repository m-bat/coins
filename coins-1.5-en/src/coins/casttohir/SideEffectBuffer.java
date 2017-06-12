/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.LinkedList;
import java.util.ListIterator;

import coins.ir.IR;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;

/** SideEffectBuffer
 * Hold side effect statements and process them (move, insert, etc.).<br>
 * Side effect statement: statement that leave side effect (assign statement,
 *   call statement, etc.).<br>
 * Sequence point: program point where side effect should be completed at the
 *   latest.<br>
 * Swept out statement: statement moved forward or backward to perform side
 *   effect at proper point as defined by the grammar.<br>
 * Sweep out backward: move a side effect statement in front of the current
 *   statement.<br>
 * Sweep out forward: move a side effect statement to some position succeeding
 *   the current statement.<br>
 *
 * @author  Shuichi Fukuda
**/
public class SideEffectBuffer
{
  /** toHir
   * Offers cooperation with the object of other packages.
   */
  private ToHir toHir;

  /** prevList
   * Side effect statements swept up backward
   * (to be placed in front of current statement).
   */
  private LinkedList prevList;

  /** nextList
   * Side effect statements swept up forward
   * (to be placed next to current statement).
   */
  private LinkedList nextList;

  //-------------------------------------------------------------------
  /** SideEffectBuffer
   * Constructor.
   *
   * @param  tohir Offers cooperation with the object of other packages.
  **/
  SideEffectBuffer(ToHir tohir)
  {
    toHir    = tohir;
    prevList = new LinkedList();
    nextList = new LinkedList();
  }
  //-------------------------------------------------------------------
  /** isEmpty
   * @return  True if there is no statements swept out.
  **/
  boolean isEmpty()
  {
    return prevList.isEmpty() && nextList.isEmpty();
  }
  //-------------------------------------------------------------------
  /** isEmptyPrev
   * @return  True if there is no statement swept backward.
  **/
  boolean isEmptyPrev()
  {
    return prevList.isEmpty();
  }
  //-------------------------------------------------------------------
  /** isEmptyNext
   * @return  True if there is no statement swept forward.
  **/
  boolean isEmptyNext()
  {
    return nextList.isEmpty();
  }
  //-------------------------------------------------------------------
  /** addPrev
   * Add statement to the list of statements to be swept backward.
   *
   * @param   s Added statement.
   * @return  This SideEffectBuffer.
  **/
  SideEffectBuffer addPrev(HIR s)
  {
    prevList.addLast(s);
    return this;
  }
  //-------------------------------------------------------------------
  /** addNext
   * Add statement to the list of statements to be swept forward.
   *
   * @param   s Added statement.
   * @return  This SideEffectBuffer.
  **/
  SideEffectBuffer addNext(HIR s)
  {
    nextList.addFirst(s);
    return this;
  }
  //-------------------------------------------------------------------
  /** add
   * Connect the statement lists of the child with the statement lists of this
   * SideEffectBuffer, and clear the statement lists of the child.
   *
   * @param  child The child SideEffectBuffer.
   **/
  void add(SideEffectBuffer child)
  {
    prevList.addAll( child.prevList );
    nextList.addAll( 0, child.nextList );
    child.prevList.clear();
    child.nextList.clear();
  }
  //-------------------------------------------------------------------
  /** addPrev
   * Connect the statement lists of the child with the prevList of this
   * SideEffectBuffer, and clear the statement lists of the child.
   *
   * @param  child The child SideEffectBuffer.
   **/
  void addPrev(SideEffectBuffer child)
  {
    prevList.addAll( child.prevList );
    prevList.addAll( child.nextList );
    child.prevList.clear();
    child.nextList.clear();
  }
  //-------------------------------------------------------------------
  /** addNext
   * Connect the statement lists of the child with the nextList of this
   * SideEffectBuffer, and clear the statement lists of the child.
   *
   * @param  child The child SideEffectBuffer.
   **/
  void addNext(SideEffectBuffer child)
  {
    nextList.addAll( 0, child.nextList );
    nextList.addAll( 0, child.prevList );
    child.prevList.clear();
    child.nextList.clear();
  }
  //-------------------------------------------------------------------
  /** moveNextToPrev
   * Move the swept forward statements to the list of swept backward
   * statements by moving statements in nextList to prevList.
  **/
  void moveNextToPrev()
  {
    prevList.addAll( nextList );
    nextList.clear();
  }
  //-------------------------------------------------------------------
  /** toStmt
   * Change the swept out statement list to a statement. If there is only one
   * statement swept out, then return it. If there are multiple statements,
   * the make a block statement where statements in prevList are arranged
   * first and then statements in nextList are arranged next.
   *
   * @return  The resultant statement.
  **/
  Stmt toStmt()
  {
    prevList.addAll( nextList );
    nextList.clear();
    switch( prevList.size() )
    {
    case 0:
      return null;
    case 1:
      return (Stmt)prevList.removeFirst();
    default:
      BlockStmt block = toHir.newBlockStmt(null);
      while( prevList.size()!=0 )
        block.addLastStmt( (Stmt)prevList.removeFirst() );
      return block;
    }
  }
  //-------------------------------------------------------------------
  /** toExp
   * Change the list of swept backward expressions to an expression
   * by successively changing expressions in prevList to comma expression
   * and appending parameter e as value expression of the comma expression.
   *
   * @param   e The second operand of the most outside comma expression.
   * @return  The resultant expression.
  **/
  Exp toExp(Exp e)
  {
    //prevList.addAll( nextList );
    //nextList.clear();
    if( prevList.size()==0 )
      return e;
    Exp comma = (Exp)prevList.removeFirst();
    while( prevList.size()!=0 )
      comma = toHir.hir.exp(HIR.OP_COMMA,comma,(Exp)prevList.removeFirst());
    return toHir.hir.exp(HIR.OP_COMMA,comma,e);
  }
  //-------------------------------------------------------------------
  // insert to previous/next of the statement
  //-------------------------------------------------------------------
  /** addToStmtPrev
   * Insert the statements swept backward (prevList elements) in front of the
   * statement s. If a prevList element is an expression, then change it to
   * ExpStmt and insert it.
   *
   * @param  s Statement showing the position of insertion.
   * @param  reserve True if the inserted statements are to be left.
  **/
  final void addToStmtPrev(Stmt s,boolean reserve)
  {
    if( prevList.size()!=0 )
    {
      Stmt insertpoint = getInsertPoint(s);
      for( ListIterator i=prevList.listIterator(); i.hasNext(); )
      {
        //Stmt stmt = (Stmt)i.next();
        Object object = i.next();
        Stmt stmt;
        if( object instanceof Stmt )
          stmt = (Stmt)object;
        else if( object instanceof Exp )
          stmt = toHir.newExpStmt((Exp)object);
        else
        {
          toHir.fatal("addToStmtPrev");
          return;
        }
        if( reserve )
          stmt = (Stmt)stmt.copyWithOperands();
        else
          i.remove();
        insertpoint.insertPreviousStmt(stmt,null);
      }
    }
  }
  //-------------------------------------------------------------------
  /** addToStmtNext
   * Insert the statements swept forward (nextList elements) at the position
   * next to the statement s. If a nextList element is an expression, then
   * change it to ExpStmt and insert it.
   *
   * @param  s Statement showing the position of insertion.
   * @param  reserve True if the inserted statements are to be left.
  **/
  final void addToStmtNext(Stmt s,boolean reserve)
  {
    if( nextList.size()!=0 )
    {
      Stmt insertpoint = getInsertPoint(s);
      for( ListIterator i=nextList.listIterator(nextList.size());
           i.hasPrevious(); )
      {
        Stmt stmt = (Stmt)i.previous();
        if( reserve )
          stmt = (Stmt)stmt.copyWithOperands();
        else
          i.remove();
        insertpoint.addNextStmt(stmt);
      }
    }
  }
  //-------------------------------------------------------------------
  /** getInsertionPoint
   * Get the position to where statements (swept out from s) can be inserted.
   * If the parent of s is a block statement, then s is the insertion point.
   * If no, then enclose s by a newly generated block and return s as the
   * insertion point.
   * <pre>
   * 1)  (BlockStmt
   *       (xxxStmt )
   *       (yyyStmt )   <- s  -> insert point
   *       (zzzStmt )
   *
   * 2)  (notBlockStmt
   *       (xxxStmt     <- s
   *           ||
   *           \/
   *     (notBlockStmt
   *       (BlockStmt   <create block>
   *         (xxxStmt   <- s  -> insert point
   * </pre>
   * @param  s Statement showing the candidate of insertion point.
   * @return The statement where swept out statements can be inserted.
   */
  private Stmt getInsertPoint(Stmt s)
  {
    IR parent = s.getParent();
    if( parent!=null )
    {
      if( parent.getOperator()==HIR.OP_BLOCK )
        return s;
      // create block
      int childcount = parent.getChildCount();
      for( int i=1; i<=childcount; i++ )
        if( parent.getChild(i)==s )
        {
          parent.setChild( i, toHir.newBlockStmt(s) );
          return s;
        }
    }
    toHir.fatal("getInsertPoint  PARENT="+parent+"  CHILD="+s);
    return s;
  }
  //-------------------------------------------------------------------
  // add at first/last of the block
  //-------------------------------------------------------------------
  /** addToBlockLast
   * Add swept backward statements (in prevList) as the last statement
   * of the block which is either parameter s or a block generated to
   * surround s.
   *
   * @param  s Statement showing the candidate of insertion.
   * @param  reserve True if the inserted statements are to be left.
  **/
  final void addToBlockLast(Stmt s,boolean reserve)
  {
    if( prevList.size()!=0 )
    {
      BlockStmt blockstmt = getBlockStmt(s);
      for( ListIterator i=prevList.listIterator(); i.hasNext(); )
      {
        Stmt stmt = (Stmt)i.next();
        if( reserve )
          stmt = (Stmt)stmt.copyWithOperands();
        else
          i.remove();
        blockstmt.addLastStmt(stmt);
      }
    }
  }
  //-------------------------------------------------------------------
  /** addToBlockLast
   * Add swept forward statements (in nextList) as the last statement
   * of the block which is either parameter s or a block generated to
   * surround s.
   *
   * @param  s Statement showing the candidate of insertion.
   * @param  reserve True if the inserted statements are to be left.
  **/
  final void addToBlockFirst(Stmt s,boolean reserve)
  {
    if( nextList.size()!=0 )
    {
      BlockStmt blockstmt = getBlockStmt(s);
      for( ListIterator i=nextList.listIterator(nextList.size());
           i.hasPrevious(); )
      {
        Stmt stmt = (Stmt)i.previous();
        if( reserve )
          stmt = (Stmt)stmt.copyWithOperands();
        else
          i.remove();
        blockstmt.addFirstStmt(stmt);
      }
    }
  }
  //-------------------------------------------------------------------
  /** getBlockStmt
   * From parameter s, make a block statement in which swept out statements
   * can be inserted.
   * <pre>
   * 0)  (BlockStmt )      =>  (BlockStmt )  --> return
   *
   * 1)  (LabeledStmt          (LabeledStmt
   *       (BlockStmt      =>    (BlockStmt  --> return
   *
   * 2)  (LabeledStmt          (LabeledStmt
   *       (xxxStmt ))     =>    (BlockStmt  --> return
   *                               (xxxStmt )))
   *
   * 3)  (xxxStmt )        =>  (BlockStmt  --> return
   *                             (xxxStmt ))
   * </pre>
   * @param   s Statement showing the candidate of insertion point.
   * @return  Resultant block statement.
  **/
  BlockStmt getBlockStmt(Stmt s)
  {
    switch( s.getOperator() )
    {
    case HIR.OP_BLOCK: // 0)
      return (BlockStmt)s;

    case HIR.OP_LABELED_STMT:
      Stmt labeledstmt = ((LabeledStmt)s).getStmt();
      if( labeledstmt==null
      ||  labeledstmt.getOperator()!=HIR.OP_BLOCK ) // 1)
      {
        labeledstmt = toHir.newBlockStmt(labeledstmt); // 2)
        ((LabeledStmt)s).setStmt(labeledstmt);
      }
      return (BlockStmt)labeledstmt;

    default: // 3)
      IR parent = s.getParent();
      if( parent!=null )
      {
        int childcount = parent.getChildCount();
        for( int i=1; i<=childcount; i++ )
          if( parent.getChild(i)==s )
          {
            BlockStmt blockstmt = toHir.newBlockStmt(s);
            parent.setChild(i,blockstmt);
            return blockstmt;
          }
      }
      toHir.fatal("getBlockStmt  PARENT="+parent+"  CHILD="+s);
    }
    return null;
  }
  //-------------------------------------------------------------------
}
