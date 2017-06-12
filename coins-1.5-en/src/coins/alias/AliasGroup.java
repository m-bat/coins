/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasGroup.java
 *
 * Created on September 4, 2003, 10:27 AM
 */

package coins.alias;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import coins.ir.hir.Exp;

/**
 * <p>This is the class that represents the set of HIR nodes
 * which may be aliased to a given HIR node. Objects that
 * elements of this set represent and the object the HIR
 * node that owns this set represents possibly share some
 * subspace in the memory space. A CONTENTS node with
 * uninitialized pointer value operand is considered to
 * represent an object that does not inhabit the memory space,
 * so its associated AliasGroup object is empty (even does not
 * contain the CONTENTS node itself).</p>
 * <p>Currently does not expose any public/protected
 * fields/methods except the ones inherited from
 * <code>HashSet</code>.</p>
 *
 * @see AliasAnal#getAliasGroupFor
 *
 * @author  hasegawa
 */
public class AliasGroup extends HashSet
{
  /**
   * Creates a new instance of AliasGroup with the default
   * initial capacity.
   *
   * @see java.util.HashSet#HashSet()
   */
  public AliasGroup()
  {
    super();
    }

  /**
   * For aesthetics when printing. Sorts the elements
   * (lvalue nodes) within this AliasGroup with their
   * indexes as the key. There is no change in the internal
   * state of this AliasGroup object.
   *
   * @return sorted <code>List</code> object.
   */
  public List sort()
  {
    Comparator lComp = new Comparator()
    {
      public int compare(Object o1, Object o2)
      {
        if (((Exp)o1).getIndex() < ((Exp)o2).getIndex())
          return -1;
        else if (((Exp)o1).getIndex() == ((Exp)o2).getIndex())
          return 0;
        else
          return 1;
      }
    };
    List lList = new LinkedList();
    Exp lExp;
    for (Iterator lIt = iterator(); lIt.hasNext();)
    {
      lExp = (Exp)lIt.next();
      lList.add(lExp);
    }

    Collections.sort(lList, lComp);
    return lList;
  }
}
