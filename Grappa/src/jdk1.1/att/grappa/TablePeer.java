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
 * This class provides the basis for actually drawing table elements on a pane.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class TablePeer extends DrawNodePeer
{
  Vector tfPoints = null;
  Vector recentPeers = new Vector();
  TextLabelPeer lastPeer = null;
  TableFieldPeer tableFieldPeer = null;

  /**
   * Create an instance of a <code>TablePeer</code>
   * related to the supplied <code>Table</code>
   * and <code>DrawPane</code>.
   *
   * @param table the source object for this peer object
   * @param pane the pane to use when drawing this peer
   */
  public TablePeer(Table table, DrawPane pane) {
    super((DrawNode)table,pane);
  }

  public TextLabelPeer setTextLabelPeer(TextLabelPeer peer) {
    TextLabelPeer oldTextLabelPeer = lastPeer;
    lastPeer = peer;
    recentPeers.addElement(peer);
    return oldTextLabelPeer;
  }

  /**
   * Draws the table using the specified graphics information.
   *
   * @param gr the AWT graphics context to use for drawing, if the value
   *           is null, then the off-screen canvas <code>Graphics</code> is used.
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   */
  public void draw(Graphics gr, GraphicContext context) {
    if(context == null) {
      context = getDrawObject().getGC();
    }

    if(gr == null) gr = getDrawPane().getCanvasGraphics();

    if(context.getXORMode()) {
      gr.setXORMode(context.getXORColor());
    } else {
      gr.setPaintMode();
    }

    Rectangle[] rectangles = null;
    Polygon[] polygons = null;

    if(getDrawNode().getPeripheries() >= 1) {
      switch(getDrawNode().getDrawShape()) {
      case DrawNode.POLYGON:
	polygons = (Polygon[])outlines;
	for(int i = 0; i < polygons.length; i++) {
	  gr.setColor(getDrawObject().getColorGC().getBackground());
	  gr.fillPolygon(polygons[i]);
	  gr.setColor(getDrawObject().getColorGC().getForeground());
	  gr.drawPolygon(polygons[i]);
	}
	break;
      case DrawNode.TEXT:
	break;
      case DrawNode.RECT:
	rectangles = (Rectangle[])outlines;
	for(int i = 0; i < rectangles.length; i++) {
	  gr.setColor(getDrawObject().getColorGC().getBackground());
	  gr.fillRect(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height);
	  gr.setColor(getDrawObject().getColorGC().getForeground());
	  gr.drawRect(rectangles[i].x,rectangles[i].y,rectangles[i].width,rectangles[i].height);
	}
	break;
      default:
	throw new IllegalStateException("table drawing shape is not recognized (value is " + getDrawNode().getDrawShape() + ")");
      }
    }
      
    emitFields(context, gr, getDrawObject().getColorGC().getForeground(), tableFieldPeer);
  }

  /**
   * Setup this peer.
   * The setup does the actual co-ordinate mappings from what is available
   * in the <code>DrawNode</code> object to the <code>DrawPane</code> co-ordinates.
   *
   * @param setupTextToo indicates if text peer  should be setup as well
   */
  public void setupPeer(boolean setupTextToo) {
    super.setupPeer(false);
    tableFieldPeer = setupFields(((Table)getDrawObject()).getTableField());
  }

  private TableFieldPeer setupFields(TableField tf) {

    if(tf == null) return null;

    TableFieldPeer tfPeer = new TableFieldPeer(tf);

    int fc = tf.fieldCount();

    if(fc == 0) {
      if(tf.getTextLabel() != null && tf.getTextLabel().hasText()) {
	tfPeer.setTextLabelPeer(new TextLabelPeer(this,tf.getTextLabel()));
	tfPeer.getTextLabelPeer().setupPeer();
	tfPeer.getTextLabelPeer().setCenter(
					    getDrawPane().scalePoint(
								     getDrawNode().getPosition().x +
								     (tf.getBounds().x + (int)Math.round((double)tf.getBounds().width/2.0)),
								     getDrawNode().getPosition().y +
								     (tf.getBounds().y + (int)Math.round((double)tf.getBounds().height/2.0))
								     )
					    );
      }
      return tfPeer;
    }
    Point ptA = new Point();
    Point ptB = new Point();
    TableField tfield = null;
    Rectangle bnds = null;
    for(int cnt = 0; cnt < fc; cnt++) {
      tfield = (TableField)tf.fieldAt(cnt);
      bnds = tfield.getBounds();
      if(cnt > 0) {
	if(tf.isLR()) {
	  ptA.setLocation(bnds.x,bnds.y);
	  ptB.setLocation(bnds.x,(bnds.y+bnds.height));
	} else {
	  ptB.setLocation(bnds.x+bnds.width,(bnds.y+bnds.height));
	  ptA.setLocation(bnds.x,(bnds.y+bnds.height));
	}
	ptA.translate(getDrawNode().getPosition().x,getDrawNode().getPosition().y);
	ptB.translate(getDrawNode().getPosition().x,getDrawNode().getPosition().y);
	tfPeer.setPoint1At(cnt-1,getDrawPane().scalePoint(ptA));
	tfPeer.setPoint2At(cnt-1,getDrawPane().scalePoint(ptB));
      }
      tfPeer.setSubFieldAt(cnt,setupFields(tfield));
    }
    return tfPeer;
  }

  private void emitFields(GraphicContext gc, Graphics gr, Color lineColor, TableFieldPeer tfp) {

    if(tfp == null) return;

    int fc = tfp.fieldCount();

    if(fc == 0) {
      TextLabelPeer tlp = tfp.getTextLabelPeer();
      if(tlp != null) {
	tlp.draw(gr, gc);
      }
      return;
    }
    Point ptA = null;
    Point ptB = null;
    TableFieldPeer tfPeer = null;
    for(int cnt = 0; cnt < fc; cnt++) {
      tfPeer = (TableFieldPeer)tfp.getSubFieldAt(cnt);
      if(cnt > 0) {
	ptA = tfp.getPoint1At(cnt-1);
	ptB = tfp.getPoint2At(cnt-1);
	gr.setColor(lineColor);
	gr.drawLine(ptA.x,ptA.y,ptB.x,ptB.y);
      }
      emitFields(gc, gr, lineColor, tfPeer);
    }
  }
}
