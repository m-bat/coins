/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * NodeListIterator.java
 *
 * Created on August 17, 2002, 1:55 PM
 */
//##60 package coins.aflow;
package coins.flow; //##60

import coins.ir.IR;

/**
 *
 * @author  hasegawa
 */
public interface NodeListIterator
  extends NodeIterator
{
  //	void addAsLastChild(IR pIR);
  boolean hasPrevious();

  int nextIndex();

  IR previous();

  int previousIndex();

  //  boolean skipSubtree();
  void set(IR pIR);
}
