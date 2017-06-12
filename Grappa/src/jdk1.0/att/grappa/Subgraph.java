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

public class Subgraph extends DrawSubgraph
{
  public Subgraph() {
    //super();
  }

  /**
   * Draws the subgraph element using the specified graphic context.
   *
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   * @param pane restrict drawing to the supplied pane, if supplied.
   */
  public void draw(GraphicContext context, DrawPane pane, boolean paintNow) {
    DotGraph graph = getElement().getGraph();
    Enumeration panes = graph.getPanes();
    
    if(!panes.hasMoreElements()) {
      return;
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
    MyRectangle gbbox = getElement().getGraph().getBounds();
    MyRectangle ebbox = getBounds();

    DrawPane pnl = null;
    Graphics gr = null;
    MyPoint scaledCenter = null;
    MyRectangle scaledBBox = ebbox;
    while(panes.hasMoreElements()) {
      pnl = (DrawPane)panes.nextElement();
      if(pane != null && pane != pnl) continue;
      gr = pnl.getGraphics();
      scaledBBox = pnl.scaleRect(ebbox);
      gr.setColor(fillColor);
      gr.fillOval(scaledBBox.x,scaledBBox.y,scaledBBox.width,scaledBBox.height);
      gr.setColor(lineColor);
      gr.drawOval(scaledBBox.x,scaledBBox.y,scaledBBox.width,scaledBBox.height);
      if(textLabel != null && textLabel.hasText()) {
	textLabel.draw(pnl,context,false);
      }
      if(paintNow) pnl.paintCanvas();
    }
  }
}
