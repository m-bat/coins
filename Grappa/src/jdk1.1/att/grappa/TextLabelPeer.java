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
 * A class for drawing text labels.
 * The <code>TextLabelPeer</code> is associated with a
 * <code>DrawObjectPeer</code> and a <code>TextLabel</code>.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class TextLabelPeer
{
  private Point center = null;
  private Dimension size = null;
  private Rectangle bbox = null;
  private Vector lines = new Vector();
  private Font font = null;
  private DrawObjectPeer drawObjectPeer = null;
  private TextLabel textLabel = null;

  int rawFontsize = 0;
  
  /**
   * Creates a <code>TextLabelPeer</code> instance.
   *
   * @param drwObjPr the <code>DrawObjectPeer</code>
   *                 associated with this <code>TextLabelPeer</code>
   * @param textLabel the <code>TextLabel</code> associated with this peer
   */
  public TextLabelPeer(DrawObjectPeer drwObjPr, TextLabel textLabel) {
    drawObjectPeer = drwObjPr;
    drawObjectPeer.setTextLabelPeer(this);
    this.textLabel = textLabel;
  }
  
  /**
   * Gets the bounds of this peer in terms of the <code>DrawPane</code> coordinates.
   *
   * @return the bounding box of the peer
   */
  public Rectangle getBounds() {
    if(bbox == null) {
      setupPeer();
    }
    if(!getDrawPane().getShowText()) return new Rectangle(center.x,center.y,0,0);
    return bbox;
  }

  /**
   * Checks if the supplied co-ordinates are in
   * this peer.
   *
   * @param x the x co-ordinate to check
   * @param y the y co-ordinate to check
   * @return true if the co-ordinates lie inside the peer; false otherwise.
   */
  public boolean inPeer(int x, int y) {
    if(getBounds() == null) System.err.println("getBounds is Null");
    if(!getBounds().contains(x,y)) return false;
    Enumeration lns = lines();
    while(lns.hasMoreElements()) {
      if(((TextLinePeer)(lns.nextElement())).getBounds().contains(x,y)) {
	return true;
      }
    }
    return false;
  }

  Font getFont() {
    return font;
  }

  /**
   * Draws the text label using the specified graphics information.
   *
   * @param gr the AWT graphics context to use for drawing, if the value
   *           is null, then the off-screen canvas <code>Graphics</code> is used.
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   */
  public void draw(Graphics gr, GraphicContext context) {
    if(!getDrawPane().getShowText()) return;
    if(context == null) {
      context = getTextLabel().getSetupGC();
    }
    if(rawFontsize < 0) return;
    if(gr == null) gr = getDrawPane().getCanvasGraphics();
    if(context.getXORMode()) {
      gr.setXORMode(context.getXORColor());
    } else {
      gr.setPaintMode();
    }
    gr.setColor(context.getFontcolor());
    gr.setFont(getFont());

    Enumeration lns = lines();
    TextLinePeer textLinePeer = null;
    Point offset = null;

    while(lns.hasMoreElements()) {
      textLinePeer = (TextLinePeer)lns.nextElement();
      offset = textLinePeer.getOffset();
      gr.drawString(textLinePeer.getText(),center.x+offset.x,center.y+offset.y);
    }
  }

  /**
   * Get the <code>DrawObjectPeer</code> associated with this object.
   *
   * @return the associated <code>DrawObjectPeer</code>.
   */
  public DrawObjectPeer getDrawObjectPeer() {
    return drawObjectPeer;
  }

  /**
   * Get the <code>DrawObject</code> associated with this object.
   * The association is through the <code>DrawObjectPeer</code>.
   *
   * @return the associated <code>DrawObject</code>.
   * @see #getDrawObjectPeer()
   */
  public DrawObject getDrawObject() {
    return getDrawObjectPeer().getDrawObject();
  }

  /**
   * Get the <code>TextLabel</code> associated with this object.
   *
   * @return the associated <code>TextLabel</code>.
   */
  public TextLabel getTextLabel() {
    return textLabel;
  }

  /**
   * Get the <code>DrawPane</code> associated with this object.
   * The association is through the <code>DrawObjectPeer</code>.
   *
   * @return the associated <code>DrawPane</code>.
   * @see #getDrawObjectPeer()
   */
  public DrawPane getDrawPane() {
    if(getDrawObjectPeer() == null) return null;
    return getDrawObjectPeer().getDrawPane();
  }

  /**
   * Setup this peer.
   * The setup does the actual co-ordinate mappings from what is available
   * in the <code>TextLabel</code> object to the <code>DrawPane</code> co-ordinates.
   */
  public void setupPeer() {
    if(!getDrawPane().getShowText()) return;
    DrawPane pane = getDrawPane();
    center = pane.scalePoint(getTextLabel().getPosition());
    size = pane.scaleSize(getTextLabel().getSize());

    GraphicContext gc = getTextLabel().getSetupGC();
    rawFontsize = (int)Math.round(pane.scaleYLength((double)gc.getFontsize()));
    if(rawFontsize < 1) return;
    if(rawFontsize == gc.getFontsize()) {
      font = gc.getFont();
    } else {
      font = GraphicContext.fontFromCache(gc.getFontname(),gc.getFontstyle(),rawFontsize);
    }
    FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);

    String line = null;
    int xadj = 0, yadj = 0;
    int asc = fm.getAscent();
    int midAsc = (int)Math.round((double)asc / 2.0);
    // note: subtracting getFontsize() accounts for margin (unlike `dot' program)
    int hbxw = (int)Math.round((double)(size.width-rawFontsize)/2.0);
    int hbxh = (int)Math.round((double)size.height/2.0);
    Enumeration lns = getTextLabel().lines();
    TextLine textLine = null;
    int tlwd = 0;

    Point pt = new Point(0,0);
    pt.translate(0,-hbxh - 1 + (int)Math.round((double)(rawFontsize / 2.0)));

		 
    if(lines == null) {
      lines = new Vector(getTextLabel().lineCount());
    } else {
      lines.removeAllElements();
    }
    bbox = null;
    TextLinePeer peer = null;
    while(lns.hasMoreElements()) {
      textLine = (TextLine)lns.nextElement();
      tlwd = fm.stringWidth(textLine.line);
      if((textLine.justify&TextLabel.JUSTIFY_LEFT) == TextLabel.JUSTIFY_LEFT) {
	xadj = -hbxw;
      } else if((textLine.justify&TextLabel.JUSTIFY_RIGHT) == TextLabel.JUSTIFY_RIGHT) {
	xadj = hbxw - tlwd;
      } else {
	xadj = - (int)Math.round((double)tlwd / 2.0);
      }
      if((textLine.justify&TextLabel.JUSTIFY_BOTTOM) == TextLabel.JUSTIFY_BOTTOM) {
	yadj = 0;
      } else if((textLine.justify&TextLabel.JUSTIFY_TOP) == TextLabel.JUSTIFY_TOP) {
	yadj = - asc;
      } else {
	yadj = - midAsc;
      }
      lines.addElement(peer = new TextLinePeer(textLine.line,new Point(pt.x+xadj,pt.y-yadj),new Dimension(tlwd,asc),new Rectangle(center.x+pt.x+xadj,center.y+pt.y+yadj,tlwd,asc)));
      if(bbox == null) {
	bbox = new Rectangle(peer.getBounds());
      } else {
	bbox.add(peer.getBounds());
      }
      pt.translate(0,(rawFontsize+2));
    }
    if(bbox == null) {
      bbox = new Rectangle(center.x,center.y,0,0);
    }
  }

  /**
   * Draw a string in the drawing pane.
   *
   * @param text the text to draw
   * @param pt the center point of the text in graph co-ordinates
   * @param justify text justification indicator
   * @param gc the Grappa graphic context to use
   * @param gr the AWT graphic context to use
   * @param dp the drawing pane whose linear mapping will be used
   */
  public static void drawString(String text, Point pt, int justify, GraphicContext gc, Graphics gr, DrawPane dp) {
    gr.setColor(gc.getFontcolor());
    gr.setFont(gc.getFont());
    FontMetrics fm = gr.getFontMetrics();
    int xadj, yadj;
    int twd = fm.stringWidth(text);
    int asc = fm.getAscent();
    if((justify&TextLabel.JUSTIFY_LEFT) == TextLabel.JUSTIFY_LEFT) {
      xadj = 0;
    } else if((justify&TextLabel.JUSTIFY_RIGHT) == TextLabel.JUSTIFY_RIGHT) {
      xadj = - twd;
    } else {
      xadj = - (int)Math.round((double)twd / 2.0);
    }
    if((justify&TextLabel.JUSTIFY_BOTTOM) == TextLabel.JUSTIFY_BOTTOM) {
      yadj = 0;
    } else if((justify&TextLabel.JUSTIFY_TOP) == TextLabel.JUSTIFY_TOP) {
      yadj = - asc;
    } else {
      yadj = - (int)Math.round((double)asc / 2.0);
    }
    Point scaledCenter = dp.scalePoint(pt.x + xadj, pt.y - yadj);
    gr.drawString(text,scaledCenter.x,scaledCenter.y);
  }

  Enumeration lines() {
    if(lines == null) return Grappa.emptyEnumeration.elements();
    return lines.elements();
  }

  class TextLinePeer {
    private String text = null;
    private Point offset = null;
    private Dimension size = null;
    private Rectangle bbox = null;

    TextLinePeer(String text, Point offset, Dimension size, Rectangle bbox) {
      this.text = text;
      this.offset = offset;
      this.size = size;
      this.bbox = bbox;
    }

    String getText() {
      return text;
    }

    Point getOffset() {
      return offset;
    }

    Dimension getSize() {
      return size;
    }

    Rectangle getBounds() {
      return bbox;
    }
  }

  Point setCenter(Point pt) {
    Point oldCenter = center;
    center = pt;
    return oldCenter;
  }

  /**
   * Get the (scaled) center point of the text.
   *
   * @return the text center point.
   */
  public Point getCenter() {
    return center;
  }
}
