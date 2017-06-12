/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * MyExpId.java
 *
 * Created on July 22, 2003, 11:24 AM
 */

package coins.alias;

import coins.ir.hir.HIR;

/**
 * ExpId class used for alias analysis.
 * Currently doesn't implement coins.sym.ExpId or extend
 * coins.sym.ExpIdImpl.
 *
 * @author  hasegawa
 */
public class MyExpId
{
  HIR fHIR;   // Original HIR given at MyExpId creation. //##57
  HIR fCopy;  // Copy of fHIR (fHIR.copyWithOperands())  //##57
  Tag fTag;

  /**
   * Workspace object that can be used for any purpose.
   */
  public Object work;

  /** Creates a new instance of MyExpId
   *
   * @param pHIR HIR node the subtree rooted at which this
   * MyExpId object represents.
   */
  public MyExpId(HIR pHIR)
  {
    fHIR = pHIR;
  }

  /**
   * Returns an instance of HIR node that is the root of the
   * HIR tree represented by this <code>MyExpId</code> object.
   * The returned node is part of the program tree (if not
   * optimized out) so should not be modified.
   *
   * @return a root node of the tree this MyExpId represents,
   * which may be part of the program tree.
   */
  public HIR getLinkedNode()
  {
    return fHIR;
  }

  /**
   * Returns an instance of HIR node that is the root of
   * the HIR tree represented by this <code>MyExpId</code> object.
   * The returned node is detached from the program tree
   * so modification to it is free (but note this method
   * does not return a fresh copy of such a tree each time
   * it is called).
   *
   * @return a root node of the tree this MyExpId represents,
   * detached from the program tree.
   */
  public HIR getHir()
  {
    if (fCopy == null)
      fCopy = fHIR.copyWithOperands();
    return fCopy;
  }

  /**
   * Returns a <code>String</code> representation of
   * this MyExpId object.
   *
   * @return a <code>String</code> representation of
   * this MyExpId object.
   */
  public String toString()
  {
    StringBuffer lBuff = new StringBuffer();
    lBuff.append("xId ");
    lBuff.append(fHIR.toString());
    return lBuff.toString();
  }

}

