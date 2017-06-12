/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.ir.hir;

// ExpListExp should be a subclass of HirList. // REFINE

/**
<PRE>
 * ExpListExp
 * Interface for the expression representing a list of expressions (Exp).
 * Its operator is OP_EXPLIST.
 * This is used to represent a list of initial values.
 * The element of the list may be a repetition specification
 * that specifies repetition count (as child 1) and elements
 * to be repeated (as child 2).
 *   ExpListExp -> (explistCode List_of_ListElement_ )
 *   ListElement_ -> Exp
 *                 | (exprepeatCode RepeatCount_ ListElement_ )
 *   RepeatCount_ -> ConstNode // integer constant node representing
 *                             // repetition count.
 * @author  Shuichi Fukuda
 * @version 2002.9.3
</PRE>
**/
public interface ExpListExp extends Exp, HirList  //##51
{
  /** size
   * @return the size (number of elements) of the expression list.
   */
  public int size();

  /** length
   * @return the length (number of elements) of the expression list.
   */
  public int length();

  /** getExp
   * Get the i-th element of the expression list, where i is given
   * by the parameter pIndex. If there is no i-th element, return null.
   * @param pIndex element index number.
   * @return the pIndex-th element of the expression list.
   */
  public Exp getExp(int pIndex);

  /** getWithRepeat
 <PRE>
   * Get the i-th final element of the expression list assuming as if
   * the nest of list is expanded, where i is given by the parameter
   * pIndex.
   * If the i-th element is (exprepeatCode n elem), then
   * elem is assumed to be repeated n-times and its first one
   * is treated as the i-th element.
   * If there is no i-th element, return null.
   * The end of list may be detected by encountering null
   * as the return value of getWithRepeat method.
   * Example
   *   Given list:
   *     (OP_EXPLIST c1 c2 (OP_EXPREPEAT c3 c4) c5)
   *   Sequence of Exp to be get by getWithRepeat:
   *     c1 c2 c4 c4 c4 c5
   *   where, each of c1, c2, c3, c4, c5 represents constant expression
   *   whose value is 1, 2, 3, 4, 5 respectively.
</PRE>
   * @param pIndex index number corresponding to the element
   *     to be returned.
   * @return the pIndex-th element of the expression list;
   *     if there is no pIndex-th element, then return null.
   */
  public Exp getWithRepeat(int pIndex);

  /** set
   * Set pExp as pIndex-th element of the expression list.
   * If there is no pIndex-th element in the list, then do nothing.
   * @param pIndex  index number corresponding to the element
   *     to be set.
   * @param pExp element to be set.
   */
  public void setExp(int pIndex, Exp pExp);

  /**
   * Make iterator to traverse all elements of the list.
   * @return the iterator.
   */
  public java.util.ListIterator iterator(); //##64

  public String toString();

  /**
   * Print the list enclosing the string images of
   * all elements by parenthesis.
   * The start column is indicated by pIndent.
   * @param pIndent the start column to print.
   */
   public void print(int pIndent);

   /**
    * Print the list enclosing the detailed string images
    * of all elements by parenthesis.
    * The start column is indicated by pIndent.
    * @param pIndent the start column to print.
    */
   public void print(int indent, boolean detail);
}
