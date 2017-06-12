/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */

package att.grappa;

import java.util.*;

public class UndoStack extends Stack {
  private static int maxStackSize = 32;

  public UndoStack() {
    //super();
    ensureCapacity(maxStackSize);
  }

  public UndoStack(int size) {
    //super();
    setMaxStackSize(size);
  }

  public int getMaxStackSize() {
    return(maxStackSize);
  }

  public void setMaxStackSize(int size) {
    if(size < 1) size = 1;
    if(size < size()) {
      while(size < size()) {
	removeElementAt(0);
      }
    } else if(size > capacity()) {
      ensureCapacity(size);
    }
    maxStackSize = size;
  }

  public Object push(Object item) {
    if(size() == maxStackSize) {
      removeElementAt(0); // first element is oldest
    }
    return(super.push(item));
  }
}
