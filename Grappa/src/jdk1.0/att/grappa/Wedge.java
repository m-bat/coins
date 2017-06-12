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

public class Wedge extends DrawNode
{
  public Wedge() {
    //super();
    shape = DrawNode.ARC;
    int[] array = new int[2];
    extras = new Vector(1);
    extras.addElement(array);
    array[0] =   0; // start angle
    array[1] = 180; // arc angle
  }

  /**
   * Draws the node using the specified graphic context.
   *
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   * @param pane restrict drawing to the supplied pane, if supplied.
   */
  public void draw(GraphicContext context, DrawPane pane, boolean paintNow) {
    DotGraph graph = getElement().getGraph();
    Enumeration panes = null;
    DrawPane pnl = null;

    if(pane == null) {
      panes = graph.getPanes();
    
      if(!panes.hasMoreElements()) {
	return;
      }
      pnl = (DrawPane)panes.nextElement();
    } else {
      pnl = pane;
    }

    setBounds();

    if(context == null) {
      context = gc;
    }
    Color fillColor = getParentGC().getBackground();
    Color lineColor = context.getForeground();
    if(context.getFillMode()) {
      fillColor = context.getForeground();
      lineColor = getParentGC().getForeground();
    }
    boolean doLabel = true;
    Attribute labelAttr = getElement().getAttribute("label");
    String label = null;
    if(labelAttr == null || (label = labelAttr.getValue()) == null || label.length() == 0) {
      doLabel = false;
    } else if(label.equals("\\N")) {
      label = getElement().getName();
    }

    Graphics gr = null;
    MyPoint scaledCenter = null;
    MyRectangle scaledBounds = null;
    IntPairs drawPairs = null;
    IntPairs scaledPairs = null;
    MyRectangle scaledRect = null;
    while(true) {
      gr = pnl.getOffscreenGraphics();

      if(peripheries >= 1) {
	scaledCenter = pnl.scalePoint(position);
	for(int i = 0; i < drawPoints.size(); i++) {
	  drawPairs = (IntPairs)(drawPoints.elementAt(i));
	  scaledRect = pnl.scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);

	  gr.setColor(fillColor);
	  gr.fillArc(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height,drawPairs.xArray[2],drawPairs.yArray[2]);
	  gr.setColor(lineColor);
	  gr.drawArc(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height,drawPairs.xArray[2],drawPairs.yArray[2]);
	  // quick and dirty fixed start(90) & arc(180) angle case
	  gr.drawLine(scaledRect.x,scaledCenter.y,scaledCenter.x,scaledRect.y+scaledRect.height);
	  gr.drawLine(scaledCenter.x,scaledRect.y+scaledRect.height,scaledRect.x+scaledRect.width,scaledCenter.y);
	}
      }

      if((getDrawStyle()&DrawNode.AUXLABELS) != 0) {
	Attribute attr = null;
	if((attr = getElement().getAttribute("toplabel")) != null) {
	  if(scaledCenter == null) scaledCenter = pnl.scalePoint(position);
	  if(scaledBounds == null) scaledBounds = pnl.scaleRect(getBounds());
	  Mlabel_hack(attr.getValue(),true,scaledCenter,scaledBounds,pnl, context);
	}
	if((attr = getElement().getAttribute("bottomlabel")) != null) {
	  if(scaledCenter == null) scaledCenter = pnl.scalePoint(position);
	  if(scaledBounds == null) scaledBounds = pnl.scaleRect(getBounds());
	  Mlabel_hack(attr.getValue(),false,scaledCenter,scaledBounds,pnl, context);
	}
      }
      
      if(doLabel) {
	pnl.drawString(label,context,position,DrawPane.JUSTIFY_CENTER|DrawPane.JUSTIFY_MIDDLE);
      }
      if(paintNow) pnl.paintCanvas();
      if(pane != null || !panes.hasMoreElements()) break;
      pnl = (DrawPane)panes.nextElement();
    }
  }
}
