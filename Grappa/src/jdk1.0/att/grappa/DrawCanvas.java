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
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class DrawCanvas extends Canvas
{
  private DrawPane pane = null;

  // to record translations
  private int dx = 0;
  private int dy = 0;

  // mouse down/up info
  private int evtX = 0;
  private int evtY = 0;
  private MyPoint dragPt = null;
  private MyPoint evtPt = null;
  private DotElement evtElem = null;
  private DrawObject selObj = null;
  private DotElement delElem = null;

  private int evtMod = 0; // windows bug (JDK1.0.2) part 0

  private static MyDimension minSize = new MyDimension(50,50);

  private MyDimension prefSize = null;

  /**
   * @param pane the parent pane
   */
  public DrawCanvas(DrawPane pane, MyDimension prefSize) {
    super();
    this.pane = pane;
    this.prefSize = new MyDimension(prefSize);
  }

  public void y_shift(int y) {
    dy += y;
  }

  public void y_set(int y) {
    dy = y;
  }

  public int getY() {
    return dy;
  }

  public void x_shift(int x) {
    dx += x;
  }

  public void x_set(int x) {
    dx = x;
  }

  public int getX() {
    return dx;
  }

  public void translate(int x, int y) {
    dx += x;
    dy += y;
  }

  public void update(Graphics gr) {
    paint(gr);
  }

  public void paint(Graphics gr) {
    gr.translate(-dx,-dy);
    //dx = dy = 0;
    Image offscreen = pane.getImage();
    if(offscreen != null) {
      gr.drawImage(offscreen,0,0,this);
    } else {
      clear(gr);
    }
  }

  public void clear(Graphics gr) {
    MyDimension curSize = getSize();
    gr.setColor(pane.getGraph().getGC().getBackground());
    gr.fillRect(0,0,curSize.width,curSize.height);
  }

  public Dimension minimumSize() {
    return (Dimension)minSize;
  }

  public Dimension preferredSize() {
    return (Dimension)prefSize;
  }

  public void unionSizes(int width, int height) {
    MyDimension sz = getSize();
    sz.width = (sz.width > width) ? sz.width : width;
    sz.height = (sz.height > height) ? sz.height : height;
    setSize(sz);
  }

  public MyDimension getSize() {
    return new MyDimension(size());
  }

  public void setSize(MyDimension d) {
    resize((Dimension)d);
  }

  /*
   * Mouse behavior:
   *
   * Button 1 (un-modified) = selection
   * Button 3 (un-modified) = pop-up menu
   * Shift Button 1 = create (if not coincident with an existing item)
   * Ctrl Button 1 = delete
   *
   * Button down starts process, button up completes it (when valid).
   */

  public boolean mouseDown(Event evt, int x, int y) {
    if(pane.getImage() == null) return true;
    evtX = x;
    evtY = y;
    evtPt = pane.canonXY(x,y);
    dragPt = null;
    evtElem = pane.getGraph().findContainingElement(evtPt);
    evtMod = evt.modifiers; // windows bug (JDK1.0.2) part I
    if(evtElem != null) {
      switch(evt.modifiers) {
      case 0:
	if(!pane.getGraph().isSelectable()) break;
	// fall through to...
      case Event.META_MASK:
	if(evt.modifiers == Event.META_MASK && !pane.getGraph().isMenuable()) break;
	// highlight for select
	DrawObject dobj = evtElem.getDrawObject();
	if(selObj != null) {
	  selObj.highlightOff();
	}
	selObj = dobj;
	if(dobj != null) {
	  dobj.highlightSelect();
	}
	if(evt.modifiers == Event.META_MASK) { // just Button 3
	  // pop-up menu
	}
	break;
      case Event.CTRL_MASK:
	if(!pane.getGraph().isEditable()) break;
	// highlight for delete
	delElem = evtElem;
	highlightDelete(delElem,true);
	break;
      }
    }
    return true;
  }

  // TODO: evtElem == null and mouseDrag, then rubberband and on mouseUp, if
  // rectangle contains elements, then create subGraph

  public boolean mouseUp(Event evt, int x, int y) {
    if(pane.getImage() == null) return true;
    MyPoint pt = pane.canonXY(x,y);
    DotElement elem = null;

    if(x == evtX && y == evtY) {
      elem = evtElem;
    } else {
      elem = pane.getGraph().findContainingElement(pt);
    }

    evt.modifiers = evtMod; // window bug (JDK1.0.2) part 2
    switch(evt.modifiers) {
    case Event.META_MASK: // just Button 3
      if(elem != null && elem == evtElem) {
	// pop-up menu
	if(pane.getGraph().isMenuable()) {
	  if(elem.getAppObject() != null) elem.getAppObject().properties(evt,pt);
	}
      }
      if(selObj != null) {
	selObj.highlightOff();
	selObj = null;
      }
      break;
    case Event.CTRL_MASK: // shift Button 1
      if(selObj != null) {
	selObj.highlightOff();
	selObj = null;
      }
      if(!pane.getGraph().isEditable()) break;
      if(elem != null && evtElem == elem) {
	// do delete
	doDelete(delElem);
	delElem = null;
      }
      break;
    case 0: // just button 1
      if(!pane.getGraph().isSelectable()) break;
      if(elem != null && evtElem == elem) {
	// select the element
	if(elem.getAppObject() != null) elem.getAppObject().selected(evt,pt);
      } else {
	if(selObj != null) {
	  selObj.highlightOff();
	  selObj = null;
	}
      }
      break;
    case Event.SHIFT_MASK:
      if(selObj != null) {
	selObj.highlightOff();
	selObj = null;
      }
      if(!pane.getGraph().isEditable()) break;
      if(dragPt != null) {
	rubberLine(evtX,evtY,dragPt.x,dragPt.y,true);
	dragPt = null;
      }
      if(elem == null && (evtElem == null || !evtElem.isNode())) {
	// create a node
	Attribute attr = new Attribute("pos",pt.x + "," + (-pt.y));
	Vector attrs = new Vector();
	attrs.addElement(attr);
	DotSubgraph sg = pane.getGraph().getRoot();
	attr = sg.getNodeAttribute("label");
	if(attr == null || attr.getValue().equals("\\N")) {
	  attr = new Attribute("label","Node" + pane.getGraph().nodeId());
	  attrs.addElement(attr);
	}
	try {
	  elem = pane.getGraph().getRoot().createElement(Grappa.DOT_NODE,null,attrs);
	} catch(InstantiationException inex) {
	  return true;
	}
	elem.getDrawObject().setBounds();
	elem.getDrawObject().draw(null,null,true);
      } else if(elem != null && evtElem != elem && evtElem.isNode()) {
	if(elem.isNode()) {
	  // create an Edge
	  Vector info = new Vector(5);
	  info.addElement(elem); //tail
	  info.addElement(null); // tailPort (not available yet)
	  info.addElement(evtElem); //head
	  info.addElement(null); // headPort (not available yet)
	  Integer directed = new Integer((pane.getGraph().getType().endsWith("digraph"))?1:0);
	  info.addElement(directed);

	  MyPoint mid1 = new MyPoint(evtPt.x+(int)Math.round((double)(pt.x - evtPt.x)/3.0),evtPt.y+(int)Math.round((double)(pt.y - evtPt.y)/3.0));
	  MyPoint mid2 = new MyPoint(evtPt.x+(int)Math.round(2.0*(double)(pt.x - evtPt.x)/3.0),evtPt.y+(int)Math.round(2.0*(double)(pt.y - evtPt.y)/3.0));
	  MyPoint mid3 = new MyPoint(evtPt.x+(int)Math.round(0.98*(double)(pt.x - evtPt.x)),evtPt.y+(int)Math.round(0.98*(double)(pt.y - evtPt.y)));
	  String pos = null;
	  if(directed.intValue() == 1) {
	    pos = "e," + pt.x + "," + (-pt.y) + " " + evtPt.x + "," + (-evtPt.y) + " " + mid1.x + "," + (-mid1.y) + " " + mid2.x + "," + (-mid2.y) + " " + mid3.x + "," + (-mid3.y);
	  } else {
	    pos = evtPt.x + "," + (-evtPt.y) + " " + mid1.x + "," + (-mid1.y) + " " + mid2.x + "," + (-mid2.y) + " " + mid3.x + "," + (-mid3.y) + " " + pt.x + "," + (-pt.y);
	  }
	  Attribute attr = new Attribute("pos",pos);
	  Vector attrs = new Vector();
	  attrs.addElement(attr);
	  try {
	    elem = pane.getGraph().getRoot().createElement(Grappa.DOT_EDGE,info,attrs);
	  } catch(InstantiationException inex) {
	    return true;
	  }
	  elem.getDrawObject().setBounds();
	  elem.getDrawObject().draw(null,null,true);
	}
      }
      break;
    default:
      // ignore mixed signals
      if(selObj != null) {
	selObj.highlightOff();
	selObj = null;
      }
      break;
    }

    if(delElem != null) {
      highlightDelete(delElem,false);
      delElem = null;
    }
    
    return true;
    //return super.handleEvent(evt);
  }

  public boolean mouseDrag(Event evt, int x, int y) {
    if(evtElem == null || !evtElem.isNode()) return true;

    switch(evt.modifiers) {
    case Event.SHIFT_MASK: // Shift Button 1
      if(!pane.getGraph().isEditable()) break;
      MyPoint pt = new MyPoint(x,y);
      if(dragPt != null) {
	if(dragPt.equals(pt)) return true;
	rubberLine(evtX,evtY,dragPt.x,dragPt.y,false);
      }
      dragPt = pt;
      rubberLine(evtX,evtY,dragPt.x,dragPt.y,false);
      break;
    }
    return true;
  }

  public void rubberLine(int x0, int y0, int x1, int y1, boolean clear) {
    Graphics gr = getGraphics();
    paint(gr);
    if(!clear) {
      gr.setColor(pane.getGraph().getGC().getXORColor());
      gr.drawLine(x0,y0,x1,y1);
    }
  }


  public void highlightDelete(DotElement elem, boolean turnOn) {
    if(elem == null) return;
    DrawObject drwobj = elem.getDrawObject();
    if(drwobj == null) return;
    
    if(turnOn) drwobj.highlightDelete();
    else drwobj.highlightOff();

    if(elem.isNode()) {
      Enumeration enum = ((DotNode)elem).edgeElements();
      DotEdge edge = null;
      while(enum.hasMoreElements()) {
	edge = (DotEdge)enum.nextElement();
	drwobj = edge.getDrawObject();
	if(drwobj != null) {
	  if(turnOn) drwobj.highlightDelete();
	  else drwobj.highlightOff();
	}
      }
	
    }
  }

  public void doDelete(DotElement elem) {
    if(elem == null) return;
    AppObject appobj = null;
    
    if(elem.isNode()) {
      Enumeration enum = ((DotNode)elem).edgeElements();
      DotEdge edge = null;
      while(enum.hasMoreElements()) {
	edge = (DotEdge)enum.nextElement();
	appobj = edge.getAppObject();
	if(appobj != null) {
	  appobj.delete();
	}
      }
    }
    appobj = elem.getAppObject();
    if(appobj != null) {
      appobj.delete();
    }
    pane.getGraph().drawGraph();
  }
}
