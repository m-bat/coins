/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * Scanner.java
 *
 * Created on July 24, 2003, 11:56 AM
 */

package coins.alias.util;

/**
 * Scans a set of integers (<code>int</code>s).
 * An imaginary cursor that advances its position with each call
 * to next is used to identify the current position.
 * The behavior is undefined if the underlying set is modified
 * (in any way except the delete method of this Scanner)
 * while scanning.
 *
 * @author  hasegawa
 */
public interface Scanner
{
  /**
   * Returns true if there are more elements.
   *
   * @return true if there are more elements.
   */
  boolean hasNext();

  /**
   * Returns the next element and advances the cursor by one.
   *
   * @return the next element.
   */
  int next();

  /**
   * Deletes the element returned by the last call to next.
   *  The object the cursor points to is unchanged.
   */
  void delete();
}

