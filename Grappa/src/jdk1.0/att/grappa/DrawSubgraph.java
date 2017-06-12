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

public abstract class DrawSubgraph extends DrawObject
{
  protected static GraphicContext selectGc = null;
  protected boolean orientLR = false;
  
  protected MyDimension size = new MyDimension();
  protected MyPoint position = new MyPoint();

  /**
   * Default shape to use when representing a subgraph.
   */
  public final static String defaultGraphShape = "box";

  public DrawSubgraph() {
    //super();
  }

  /**
   * Adjust values based on specified attribute pair.
   * Possibly set redraw flag.
   *
   * @param attr the attribute to be handled.
   * @return true if attribute was handled.
   */
  public boolean handleAttribute(Attribute attr) {
    if(attr == null) {
      return true;
    }
    
    String key = attr.getName();
    
    if(key.equals("label")) {
      TextLabel oldTl = textLabel;

      String label = (String)attr.getValue();
      if(label == null || label.length() == 0) {
	label = null;
      }
      if(oldTl == null) {
	textLabel = new TextLabel(label,gc,new MyPoint());
	setRedrawFlag(true);
	setBoundsFlag(true);
      } else if(!oldTl.sameText(label)) {
	textLabel = new TextLabel(label,gc,oldTl.getPosition());
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
    } else if(key.equals("lp")) {
      if(emptyMeansRemove(attr)) return true;
      String value = attr.getValue();
      if(value == null || value.length() == 0) return true;

      MyPoint newPosition = pointForTuple(value);
      if(textLabel == null) {
	textLabel = new TextLabel(null,gc,newPosition);
      } else if(!textLabel.getPosition().equals(newPosition)) {
	textLabel.setPosition(newPosition);
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("rankdir")) {
      if(emptyMeansRemove(attr)) return true;
      // add "doLayoutFlag" for when this changes
      String value = attr.getValue();

      if(value == null) return true;

      if(value.equals("LR")) {
	setLR(true);
      } else {
	setLR(false);
      }
      return true;
    }
    return(super.handleAttribute(attr));
  }

  public void setLR(boolean newLR) {
    orientLR = newLR;
  }

  public boolean isLR() {
    return orientLR;
  }

  public MyRectangle setBounds() {
    MyRectangle oldBox = BoundingBox;
    if(!getBoundsFlag()) return oldBox;
    int x = position.x - (int)Math.ceil((double)size.width/2.0);
    /*
     * Assume center point (position) is already adjusted for flipped y-axis
     * and just subtract height adjustment (i.e., do y - dy rather
     * than -(y + dy)).
     */
    int y = position.y - (int)Math.ceil((double)size.height/2.0);
    int width = ((size.width % 2) == 0) ? size.width : size.width + 1;
    int height = ((size.height % 2) == 0) ? size.height : size.height + 1;
    BoundingBox.setBounds(x,y,width,height);
    setBoundsFlag(false);
    return oldBox;
  }

 public boolean PointInSpecificObject(MyPoint p) {
    /* to be done */
    return false;
  }

  public void highlightSelect() {
    if(DrawSubgraph.selectGc == null) {
      DrawSubgraph.selectGc = new GraphicContext();
      DrawSubgraph.selectGc.setForeground(Grappa.getSelectColor());
      DrawSubgraph.selectGc.setFontcolor(Grappa.getSelectColor());
      DrawSubgraph.selectGc.setFillMode(false);
    }
    DrawSubgraph.selectGc.setBackground(gc.getBackground());
    DrawSubgraph.selectGc.setFont(gc.getFont());
    DrawSubgraph.selectGc.setXORColor(gc.getXORColor());
    DrawSubgraph.selectGc.setXORMode(gc.getXORMode());
    DrawSubgraph.selectGc.setClipRect(gc.getClipRect());
    DrawSubgraph.selectGc.setLineStyle(gc.getLineStyle());
    draw(DrawSubgraph.selectGc,null,true);
  }

  public void highlightDelete() {
    if(DrawSubgraph.deleteGc == null) {
      DrawSubgraph.deleteGc = new GraphicContext();
      DrawSubgraph.deleteGc.setForeground(Grappa.getDeleteColor());
      DrawSubgraph.deleteGc.setFontcolor(Grappa.getDeleteColor());
    }
    DrawSubgraph.deleteGc.setFillMode(gc.getFillMode());
    DrawSubgraph.deleteGc.setBackground(gc.getBackground());
    DrawSubgraph.deleteGc.setFont(gc.getFont());
    DrawSubgraph.deleteGc.setXORColor(gc.getXORColor());
    DrawSubgraph.deleteGc.setXORMode(gc.getXORMode());
    DrawSubgraph.deleteGc.setClipRect(gc.getClipRect());
    DrawSubgraph.deleteGc.setLineStyle(gc.getLineStyle());
    draw(DrawSubgraph.deleteGc,null,true);
  }
}
