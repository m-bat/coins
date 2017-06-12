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

public class Line extends DrawEdge
{
  public Line() {
    //super();
  }

  /**
   * Draws the line using the specified graphic context.
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
    Color lineColor = context.getForeground();

    context.setLineStyle(lineStyle);

    while(true) {
      drawSpline(pnl,context.getLineStyle(),lineColor);
      if(textLabel != null && textLabel.hasText()) {
	textLabel.draw(pnl,context,false);
      }
      if(paintNow) pnl.paintCanvas();
      if(pane != null || !panes.hasMoreElements()) break;
      pnl = (DrawPane)panes.nextElement();
    }
  }
}
