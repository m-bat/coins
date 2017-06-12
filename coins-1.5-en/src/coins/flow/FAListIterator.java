/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FAListIterator.java
 *
 * Created on July 23, 2003, 11:37 AM
 */

//##60 package coins.aflow.util;
package coins.flow; //##60

import java.util.Iterator;

/**
 * Iterator interface for FAList.
 * @author  hasegawa
 */
public interface FAListIterator
  extends Iterator
{
  /**
   * same as ListIterator.previousIndex(), but the underlying list is 1-based.
   */
  public int previousIndex();
}
