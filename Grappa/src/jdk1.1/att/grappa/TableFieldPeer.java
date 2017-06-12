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
 * A class for drawing the <code>TableField</code> information of a <code>Table</code>.
 * Specific <code>DrawPane</code> information is stored lower down
 * at the <code>TextLabelPeer</code> level.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class TableFieldPeer
{
  TableField tableField = null;
  TableFieldPeer[] subFields = null;
  Point[] point1 = null;
  Point[] point2 = null;
  Point position = null;
  TextLabelPeer textLabelPeer = null;

  /**
   * Create an instance of a <code>TableFieldPeer</code>
   * related to the supplied <code>TableField</code>.
   *
   * @param tf the source object for this peer object
   */
  public TableFieldPeer(TableField tf) {
    //super();
    tableField = tf;
    int fc = tf.fieldCount();
    if(fc > 0) {
      subFields = new TableFieldPeer[fc];
      if(fc > 1) {
	point1 = new Point[fc-1];
	point2 = new Point[fc-1];
      }
    }
  }

  void setPoint1At(int pos, Point pt) {
    point1[pos] = pt;
  }

  Point getPoint1At(int pos) {
    return point1[pos];
  }

  void setPoint2At(int pos, Point pt) {
    point2[pos] = pt;
  }

  Point getPoint2At(int pos) {
    return point2[pos];
  }

  void setSubFieldAt(int pos, TableFieldPeer tfp) {
    subFields[pos] = tfp;
  }

  TableFieldPeer getSubFieldAt(int pos) {
    return subFields[pos];
  }

  void setTextLabelPeer(TextLabelPeer tlp) {
    textLabelPeer = tlp;
  }

  TextLabelPeer getTextLabelPeer() {
    return textLabelPeer;
  }

  void setPosition(Point pt) {
    position = pt;
  }

  Point getPosition() {
    return position;
  }

  int fieldCount() {
    if(subFields == null) return 0;
    return subFields.length;
  }
}
  
