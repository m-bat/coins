/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sym;

import coins.backend.CantHappenException;
import coins.backend.cfg.BasicBlk;


/** Label */
public class Label {
  /** Label name */
  private String name;

  //  LirNode refNode;

  /** The basic block this label belongs to. */
  private BasicBlk basicBlk;

  /** Create label with name n. */
  public Label(String n) { name = n; }

  /** Return name of label. **/
  public String name() { return name; }

  /** Rename the label. **/
  public void rename(String newName) { name = newName; }

  /** Return the basic block. */
  public BasicBlk basicBlk() {
    if (basicBlk == null)
      throw new CantHappenException("label \"" + name + "\" undefined.");
    return basicBlk;
  }

  /** Clear basic block. **/
  public void clear() { basicBlk = null; }

  /** Set the basic block. */
  public void setBasicBlk(BasicBlk b) {
    if (basicBlk != null)
      throw new CantHappenException("label \"" + name + "\" already has basic block.");
    basicBlk = b;
  }


  //  /** Set LirNode which refers to this label */
  //  public void setRefNode(LirNode node) {
  //    refNode = node;
  //  }

  //  /** Return LirNode which refers to this label */
  //  public LirNode refNode() {
  //    return refNode;
  //  }


  /** Visualize */
  public String toString() {
    return name;

    //if (basicBlk == null)
    //  return name;
    //else
    //  return basicBlk.label().name;
  }
}
