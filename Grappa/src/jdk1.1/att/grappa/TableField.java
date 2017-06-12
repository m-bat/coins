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

import java.awt.*;
import java.util.*;

/**
 * Essentially a structure for storing Table labelling info.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class TableField implements Shape
{
  private Dimension size = new Dimension();
  private Rectangle bounds = new Rectangle();
  private TextLabel label = null;
  private TextLabelPeer labelPeer = null;
  private TableField[] subFields = null;
  private int subFieldsUsed = 0;
  private boolean orientLR = false;
  private String idTag = null;

  private int peerPoint = -1;

  private Table table = null;

  /**
   * Creates an empty <code>TableField</code> instance.
   */
  public TableField() {
    //super();
  }


    /*
     * The stuff contained herein was for debugging
     *
     *  private TableField parent = null;
     *  private int number = 0;
     *
     *  public void identify(TableField prnt, int nbr) {
     *    parent = prnt;
     *    number = nbr;
     *  }
     *
     *  public TableField getParent() {
     *    return parent;
     *  }
     *
     *  public int getNumber() {
     *    return number;
     *  }
     *
     *  public String getIdentifier() {
     *    StringBuffer buf = new StringBuffer();
     *    if(isLR()) {
     *      buf.append("LR:");
     *    } else {
     *      buf.append("TB:");
     *    }
     *    buf.append(number);
     *    TableField prnt = getParent();
     *    while(prnt != null) {
     *      buf.append(',');
     *      buf.append(prnt.getNumber());
     *      prnt = prnt.getParent();
     *    }
     *    return buf.toString();
     *  }
     */

  /**
   * Set the value of the <code>Table</code> object
   * associated with this <code>TableField</code> object.
   *
   * @param tbl the <code>Table</code> with which to be associated 
   */
  void setTable(Table tbl) {
    table = tbl;
  }

  /**
   * Get the table associated with this TableField object.
   */
  Table getTable() {
    return table;
  }

  void setPeerPoint(int nbr) {
    peerPoint = nbr;
  }

  int getPeerPoint() {
    return peerPoint;
  }

  /**
   * Get the bounding box of this element
   *
   * @return the bounding box of this element
   */
  public Rectangle getBounds() {
    return bounds;
  }

  void setBounds(int x, int y, int width, int height) {
    bounds.setBounds(x,y,width,height);
  }

  void setBounds(Rectangle r) {
    bounds.setBounds(r);
  }

  /**
   * Get the size of this object.
   *
   * @return the size of this object.
   */
  public Dimension getSize() {
    return size;
  }

  void setSize(int width, int height) {
    size.setSize(width,height);
  }

  void setSize(Dimension d) {
    size.setSize(d.width,d.height);
  }

  boolean hasFields() {
    if(subFields == null || subFields.length == 0 || subFieldsUsed == 0) {
      return false;
    }
    return true;
  }

  synchronized int subfields(int len) {
    if(len < 1) return 0;
    subFields = new TableField[len];
    return subFields.length;
  }

  int fieldCount() {
    if(subFields == null) {
      return 0;
    }
    return subFieldsUsed;
  }

  synchronized void addField(TableField tf) {
    // can cause exception
    subFields[subFieldsUsed++] = tf;
  }

  TableField fieldAt(int nbr) {
    if(nbr < 0 || nbr >= subFieldsUsed) return null;
    return subFields[nbr];
  }

  //public Enumeration fields() {
  //  return new Enumerator();
  //}

  boolean isLR() {
    return orientLR;
  }

  void setLR(boolean lr) {
    orientLR = lr;
  }

  String getId() {
    return idTag;
  }

  void setId(String id) {
    idTag = null;
    if(id == null) return;
    char[] array = id.toCharArray();
    boolean hadNBSP = false;
    for(int i = 0; i < array.length; i++) {
      if(array[i] == TextLabel.NBSP) {
	array[i] = ' ';
	hadNBSP = true;
      }
    }
    if(hadNBSP) idTag = new String(array,0,array.length);
    else idTag = id;
  }

  Dimension sizeFields() {
    return sizeUpFields(this);
  }
  
  private Dimension sizeUpFields(TableField tf) {
    int fc = tf.fieldCount();
    if(fc == 0) {
      if(tf.getTextLabel() != null) {
	tf.setSize(tf.getTextLabel().getSize());
      } else {
	tf.setSize(0,0);
      }
    } else {
      Dimension dtmp = null;
      Dimension dim = new Dimension();
      for(int cnt = 0; cnt < fc; cnt++) {
	dtmp = sizeUpFields((TableField)tf.fieldAt(cnt));
	if(tf.isLR()) {
	  dim.width += dtmp.width;
	  dim.height = (dim.height > dtmp.height) ? dim.height : dtmp.height;
	} else {
	  dim.width = (dim.width > dtmp.width) ? dim.width : dtmp.width;
	  dim.height += dtmp.height;
	}
      }
      tf.setSize(dim);
    }
    return tf.getSize();
  }

  Dimension resizeFields(Dimension sz) {
    resizeUpFields(this,sz);
    return this.getSize();
  }
  
  void resizeUpFields(TableField tf, Dimension sz) {
    Dimension delta = new Dimension(sz.width - tf.getSize().width, sz.height - tf.getSize().height);
    tf.setSize(sz);

    int fc = tf.fieldCount();
    
    if(fc == 0) {
      return;
    }

    // adjust children, if any
    double incr = 0;
    if(tf.isLR()) {
      incr = (double)delta.width / (double)fc;
    } else {
      incr = (double)delta.height / (double)fc;
    }
    TableField tfield = null;
    int amt = 0;
    // reuse old space under new name for readability
    Dimension newSz = delta;
    for(int cnt = 0; cnt < fc; cnt++) {
      tfield = (TableField)tf.fieldAt(cnt);
      amt = (int)Math.floor(((double)(cnt+1))*incr) - (int)Math.floor(((double)cnt)*incr);
      if(tf.isLR()) {
	newSz.setSize(tfield.getSize().width+amt,sz.height);
      } else {
	newSz.setSize(sz.width,tfield.getSize().height+amt);
      }
      resizeUpFields(tfield,newSz);
    }
  }

  void positionFields(Point pos) {
    posFields(this,pos);
  }

  private void posFields(TableField tf, Point pos) {
    tf.setBounds(pos.x,pos.y-tf.getSize().height,tf.getSize().width,tf.getSize().height);
    // clip spillage outside outer-most bounds
    Rectangle b1 = tf.getBounds();
    Rectangle b2 = tf.getTable().getTableField().getBounds();
    int tmpi;
    tmpi = Math.max(b1.x,b2.x);
    // subtract 1 from outer width for reasons unknown
    b1.width = Math.min(b1.x+b1.width,b2.x+b2.width-1) - tmpi;
    b1.x = tmpi;
    tmpi = Math.max(b1.y,b2.y);
    b1.height = Math.min(b1.y+b1.height,b2.y+b2.height) - tmpi;
    b1.y = tmpi;

    int fc = tf.fieldCount();

    if(fc == 0) {
      return;
    }

    TableField tfield = null;
    for(int cnt = 0; cnt < fc; cnt++) {
      tfield = (TableField)tf.fieldAt(cnt);
      posFields(tfield,new Point(pos));
      if(tf.isLR()) {
	pos.x += tfield.getSize().width;
      } else {
	pos.y -= tfield.getSize().height;
      }
    }
  }

  void setLabel(String str, GraphicContext gc, Point pos) {
    label = new TextLabel(getTable(), str, gc, pos);
  }

  TextLabel getTextLabel() {
    return label;
  }

  /*
*  class Enumerator implements Enumeration {
*    int count = (subFields == null) ? 0 : subFields.length;
*
*    public boolean hasMoreElements() {
*      return(count > 0);
*    }
*
*    public Object nextElement() {
*      synchronized (TableField.this) {
*	if(count > subFields.length) count = subFields.length;
*	if(count == 0)
*	  throw new NoSuchElementException("TableFieldEnumerator");
*	return subFields[--count];
*      }
*    }
*  }
  */
}
