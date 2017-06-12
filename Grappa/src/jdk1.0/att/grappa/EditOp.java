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

public class EditOp
{
  private DotGraph graph = null;
  private int opType = -1;
  private DotElement dotElement = null;
  private Vector attributes = null;
  private Object info = null;

  private Stack elements = new Stack();

  public EditOp(DotGraph grf, int type, DotElement elem, Vector attrs, Object obj_info) {
    graph = grf;
    opType = type;
    dotElement = elem;
    attributes = attrs;
    info = obj_info;
    doEdit();
  }

  public void doEdit() {
    switch(opType) {
    case Grappa.EDIT_CUT:
      break;
    case Grappa.EDIT_DELETE:
      break;
    case Grappa.EDIT_PASTE:
      break;
    case Grappa.EDIT_COPY:
      break;
    case Grappa.EDIT_ADD:
      break;
    }
  }

  public DotElement getElement() {
    return dotElement;
  }

  private void removeElement(DotElement element) {
    //elements.push(element.makePeer());
    //element.deleteElement();
  }
}
