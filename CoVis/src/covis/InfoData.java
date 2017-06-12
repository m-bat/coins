/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

/**
 * Part of Info
 */
class InfoData {

  public String title;
  public ViList data;

  public InfoData(String title) {
    this.title = title;
    data = new ViList();
  }

  public InfoData(String title, ViList b) {
    String label;
    data = new ViList();

    //System.out.print("InfoData : "+ title);
    this.title = title+": ";
 
    for (ViList r = b.first();!r.atEnd();r=r.next()) {
      label = (String)r.elem();
      //data.add(label.toString().replace('.', '_'));
      data.add(label.toString());
      //System.out.print(" , "+label);
    }
    //System.out.println(" ");
  }

  public void append(InfoData child) {
    data.add(child);
  }

  public boolean hasChildren() {
    ViList blk = data.first();
    if (data.isEmpty()) {
      return false;
    }
    if (blk.elem() instanceof InfoData) {
      return true;
    } else {
      return false;
    }
  }

  public ViList getChild() {
    ViList blk = data.first();
    if (blk.elem() instanceof InfoData)
      return data;
    else
      return null;
  }

  public String toString() {
    String str = title;
    boolean isFirst = true;
    String label;

    for (ViList r = data.first();!r.atEnd();r=r.next()) {
      if (r.elem() instanceof InfoData) {
        break;
      } else {
        label = (String)r.elem();
        str += (isFirst ? " " : ", ") + label;
        isFirst = false;
      }
    }
    return str;
  }

}
