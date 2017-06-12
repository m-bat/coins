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
 * Essentially a structure for storing record shape labelling info.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class TableField implements Shape
{
  private MyDimension size = new MyDimension();
  private MyRectangle bounds = new MyRectangle();
  private TextLabel label = null;
  private TableField[] subFields = null;
  private int subFieldsUsed = 0;
  private boolean orientLR = false;
  private String idTag = null;

  private Table table = null;

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

  public void setTable(Table tbl) {
    table = tbl;
  }

  public Table getTable() {
    return table;
  }

  public MyRectangle getBounds() {
    return bounds;
  }

  public void setBounds(int x, int y, int width, int height) {
    bounds.setBounds(x,y,width,height);
  }

  public void setBounds(MyRectangle r) {
    bounds.setBounds(r);
  }

  public MyDimension getSize() {
    return size;
  }

  public void setSize(int width, int height) {
    size.setSize(width,height);
  }

  public void setSize(MyDimension d) {
    size.setSize(d.width,d.height);
  }

  public boolean hasFields() {
    if(subFields == null || subFields.length == 0 || subFieldsUsed == 0) {
      return false;
    }
    return true;
  }

  public int subfields(int len) {
    if(len < 1) return 0;
    subFields = new TableField[len];
    return subFields.length;
  }

  public int fieldCount() {
    if(subFields == null) {
      return 0;
    }
    return subFieldsUsed;
  }

  public void addField(TableField tf) {
    // can cause exception
    subFields[subFieldsUsed++] = tf;
  }

  public TableField fieldAt(int nbr) {
    if(nbr < 0 || nbr >= subFieldsUsed) return null;
    return subFields[nbr];
  }

  public Enumeration fields() {
    return new TableFieldEnumerator(subFields);
  }

  public boolean isLR() {
    return orientLR;
  }

  public void setLR(boolean lr) {
    orientLR = lr;
  }

  public String getId() {
    return idTag;
  }

  public void setId(String id) {
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

  public MyDimension sizeFields() {
    return sizeUpFields(this);
  }
  
  private MyDimension sizeUpFields(TableField tf) {
    int fc = tf.fieldCount();
    if(fc == 0) {
      if(tf.getLabel() != null) {
	tf.setSize(tf.getLabel().getSize());
      } else {
	tf.setSize(0,0);
      }
    } else {
      MyDimension dtmp = null;
      MyDimension dim = new MyDimension();
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

  public MyDimension resizeFields(MyDimension sz) {
    resizeUpFields(this,sz);
    return this.getSize();
  }
  
  private void resizeUpFields(TableField tf, MyDimension sz) {
    MyDimension delta = new MyDimension(sz.width - tf.getSize().width, sz.height - tf.getSize().height);
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
    MyDimension newSz = delta;
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

  public void positionFields(MyPoint pos) {
    posFields(this,pos);
  }

  private void posFields(TableField tf, MyPoint pos) {
    tf.setBounds(pos.x,pos.y-tf.getSize().height,tf.getSize().width,tf.getSize().height);
    // clip spillage outside outer-most bounds
    MyRectangle b1 = tf.getBounds();
    MyRectangle b2 = tf.getTable().getTableField().getBounds();
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
      posFields(tfield,new MyPoint(pos));
      if(tf.isLR()) {
	pos.x += tfield.getSize().width;
      } else {
	pos.y -= tfield.getSize().height;
      }
    }
  }

  public void setLabel(String str, GraphicContext gc, MyPoint pos) {
    label = new TextLabel(str, gc, pos);
  }

  public TextLabel getLabel() {
    return label;
  }
}

final class TableFieldEnumerator implements Enumeration
{
  TableField[] array;
  int count;

  TableFieldEnumerator(TableField[] ar) {
    array = ar;
    count = 0;
  }

  public boolean hasMoreElements() {
    if(array == null) return false;
    return (count < array.length && array[count] != null);
  }

  public Object nextElement() {
    synchronized (array) {
      if(count < array.length && array[count] != null) {
	return array[count++];
      }
    }
    throw new NoSuchElementException("TableFieldEnumerator");
  }
}
  
