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
import java.awt.event.*;
import java.util.*;

/**
 * The canvas used for drawing graphs.  Its constructor is called
 * automatically by the <code>DrawPane</code> constructor.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawCanvas extends Canvas
{
  private boolean imageFlag = false;
  DrawPane pane = null;
  private Image osImage = null;
  private Graphics osGraphics = null;
  private Dimension osSize = null;
  private Rectangle osDrawn = null;

  private static Dimension minSize = new Dimension(50,50);

  private Dimension prefSize = null;

  /*
   * @param pane the parent pane
   * @param prefSize the preferred size of the canvas
   */
  DrawCanvas(DrawPane pane, Dimension prefSize) {
    super();
    this.pane = pane;
    this.prefSize = new Dimension(prefSize);
    Mouser mouser = new Mouser();
    addMouseListener(mouser);
    addMouseMotionListener(mouser);
  }

  public void update(Graphics gr) {
    paint(gr);
  }

  public void paint(Graphics gr) {
    Dimension sz = getSize();
    if(osImage == null || osSize.width != sz.width || osSize.height != sz.height) {
      osSize = new Dimension(sz);
      osImage = createImage(sz.width,sz.height);
      osGraphics = osImage.getGraphics();
      imageFlag = true;
    }
    if(imageFlag) {
      paintOS(null);
    }
    gr.drawImage(osImage,0,0,null);
    imageFlag = false;
  }

  /**
   * Get the off-screen image used for double buffering.
   *
   * @return the off-screen image
   */
  public Image getOSImage() {
    return osImage;
  }

  /**
   * Reset the off-screen image by setting it to null, a new image will
   * automatically be created the next time <code>paint</code> is called.
   */
  public void resetOSImage() {
    osImage = null;
  }

  /**
   * Get the <code>Graphics</code> used by the off-screen image
   *
   * @return the off-screen image Graphics
   */
  public Graphics getOSGraphics() {
    return osGraphics;
  }

  /*
   *public void paintElement(Element elem) {
   *elem.getDrawObject().draw(null,getDrawPane());
   *}
   */

  /**
   * Writes a <A HREF="http://www.adobe.com/prodindex/postscript/overview.html">PostScript</A> representation
   * of the canvas to the supplied PrintWriter.
   */
  public void paintPostScript(java.io.PrintWriter pw) {
    PSGr psgr = new PSGr((java.io.Writer)pw,getGraphics(),getBounds());
    psgr.setBackground(getGraph().getDrawObject().getGC().getBackground());
    psgr.setColor(getGraph().getDrawObject().getGC().getBackground());
    Dimension sz = getSize();
    psgr.fillRect(0,0,sz.width,sz.height);
    getDrawPane().doBackground(psgr);
    Enumeration elems = getDrawPane().getDrawGraph().elements();
    Element elem = null;
    Graphics saveGraphics = osGraphics;
    osGraphics = psgr;
    while(elems.hasMoreElements()) {
      elem = (Element)elems.nextElement();
      elem.getDrawObject().draw(null,getDrawPane());
    }
    psgr.showpage();
    osGraphics = saveGraphics;
  }
  
  private void paintOS(Rectangle cliprect) {
    //if(cliprect != null) {
      //cliprect = new Rectangle(cliprect);
      //cliprect.grow(1,1);
    //}
    DrawObject dobj = getGraph().getDrawObject();
    if(dobj == null) {
	osGraphics.setColor(Color.white);
    } else {
	osGraphics.setColor(dobj.getGC().getBackground());
    }
    osGraphics.setClip(cliprect);
    if(cliprect == null) {
      Dimension sz = getSize();
      osGraphics.fillRect(0,0,sz.width,sz.height);
      //cliprect = new Rectangle(0,0,sz.width,sz.height);
    } else {
      osGraphics.fillRect(cliprect.x,cliprect.y,cliprect.width,cliprect.height);
    }
    getDrawPane().doBackground(osGraphics);
    Enumeration elems = getDrawPane().getDrawGraph().elements();
    Element elem = null;
    dobj = null;
    while(elems.hasMoreElements()) {
      elem = (Element)elems.nextElement();
      if(cliprect == null || cliprect.intersects(elem.getDrawObject().getPeerFor(getDrawPane()).getBounds())) {
	if((dobj = elem.getDrawObject()) == null) {
	  try {
	    DrawObject.setDrawObject(elem);
	    dobj = elem.getDrawObject();
	  }
	  catch(InstantiationException ie) {
	    getDrawPane().getGraph().printError("Problem creating DrawObject for " + elem.getName() + " in DrawCanvas!");
	    continue;
	  }
	}
	dobj.draw(null,getDrawPane());
      }
    }
  }

  /**
   * Redraws the graph (thereby erasing any spurious info).
   */
  public void erase(Graphics gr, Rectangle cliprect) {
    Dimension sz = getSize();
    if(osImage == null || osSize.width != sz.width || osSize.height != sz.height) {
      osSize = new Dimension(sz);
      osImage = createImage(sz.width,sz.height);
      osGraphics = osImage.getGraphics();
      paintOS(null);
    } else {
      paintOS(cliprect);
    }
    if(gr == null) {
      gr = getGraphics();
    }
    gr.drawImage(osImage,0,0,this);
  }

  /**
   * Clear the canvas (and off-screen image) by filling with the background
   * color of the graph.
   */
  public void clear() {
    Dimension curSize = getSize();
    Graphics gr = getGraphics();
    if(gr != null) {
      DrawObject dobj = getGraph().getDrawObject();
      Color bkgd = null;
      if(dobj == null) {
	bkgd = Color.white;
      } else {
	bkgd = getGraph().getDrawObject().getGC().getBackground();
      }
      gr.setColor(bkgd);
      gr.fillRect(0,0,curSize.width,curSize.height);
      if(osImage != null) {
	//osGraphics.setColor(bkgd);
      	//osGraphics.fillRect(0,0,osSize.width,osSize.height);
	resetOSImage();
      }
    }
  }

  public Dimension getMinimumSize() {
    return minSize;
  }

  public void setMinimumSize(Dimension d) {
    setMinimumSize(d.width,d.height);
  }

  public void setMinimumSize(int w, int h) {
    minSize.setSize(w,h);
  }

  public Dimension getPreferredSize() {
    return prefSize;
  }

  /**
   * Get the <code>DrawPane</code> containing this canvas.
   *
   * @return the containing pane.
   */
  public DrawPane getDrawPane() {
    return pane;
  }

  /**
   * Get the <code>Graph</code> using this canvas.
   *
   * @return the graph using this canvas
   */
  public Graph getGraph() {
    return getDrawPane().getGraph();
  }

  public void setSize(Dimension sz) {
    setSize(sz.width,sz.height);
  }

  public void setSize(int w, int h) {
    if(w < minSize.width) w = minSize.width;
    if(h < minSize.height) h = minSize.height;
    prefSize.setSize(w,h);
    super.setSize(w,h);
  }

  
  public void setImageFlag(boolean mode) {
    imageFlag = mode;
  }

  public boolean getImageFlag() {
    return imageFlag;
  }

  /*
   * Mouse behavior:
   *
   * Button 1 (un-modified) = selection
   * Button 3 (un-modified) or just META key = pop-up menu
   * Shift Button 1 = create (if not coincident with an existing item)
   * Ctrl Button 1 = delete
   *
   * Button down starts process, button up completes it (when valid).
   */
  class Mouser extends MouseAdapter implements MouseMotionListener {
    // mouse down/up info
    int evtX = 0;
    int evtY = 0;
    Point dragPt = null;
    Point evtPt = null;
    Element evtElem = null;
    Element delElem = null;

    final int BUTTON1_MASK = 0; // needed for a bug in some JVMs

    
    public void mousePressed(MouseEvent evt) {
      int x = evt.getX();
      int y = evt.getY();
      evtX = x;
      evtY = y;
      evtPt = new Point(x,y);
      dragPt = null;
      evtElem = getDrawPane().findContainingElement(evtPt);



      processBlock: if(evtElem != null) {
	int evtMod = evt.getModifiers();
	if(
	   evtMod == BUTTON1_MASK ||
	   evtMod == InputEvent.BUTTON1_MASK
	   ) {
	  if(!getGraph().isSelectable()) break processBlock;
	  highlightSelect();
	} else if(
		  evtMod == (BUTTON1_MASK|InputEvent.CTRL_MASK)
		  ||
		  evtMod == (InputEvent.BUTTON1_MASK|InputEvent.CTRL_MASK)
		  ) {
	  if(!getGraph().isEditable()) break processBlock;
	  // highlight for delete
	  delElem = evtElem;
	  highlightDelete(delElem,true);
	} else if(evtMod == InputEvent.BUTTON3_MASK) {
	  if(!getGraph().isMenuable()) break processBlock;
	  highlightSelect();
	  // do pop-up menu here
	}
      }
      // no wrap up
    }

    public void mouseReleased(MouseEvent evt) {
      int x = evt.getX();
      int y = evt.getY();
      Point pt = new Point(x,y);
      Element elem = null;

      if(x == evtX && y == evtY) {
	elem = evtElem;
      } else {
	elem = getDrawPane().findContainingElement(pt);
      }

      int evtMod = evt.getModifiers();
      processBlock: if(
		       evtMod == BUTTON1_MASK
		       ||
		       evtMod == InputEvent.BUTTON1_MASK
		       ) {
	if(!getGraph().isSelectable()) break processBlock;
	if(elem != null && evtElem == elem) {
	  // select the element
	  if(elem.getAppObject() != null) elem.getAppObject().selected(evt,pt);
	} else {
	  if(getGraph().getCurrent() != null) {
	    getGraph().setCurrent(null).getDrawObject().highlightOff(DrawObject.SELECTION);
	  }
	}
      } else if(evtMod == InputEvent.BUTTON3_MASK) {
	if(elem != null && elem == evtElem) {
	  // pop-down menu here
	  if(getGraph().isMenuable()) {
	    if(elem.getAppObject() != null) elem.getAppObject().properties(evt,pt);
	  }
	}
	if(getGraph().getCurrent() != null) {
	  getGraph().setCurrent(null).getDrawObject().highlightOff(DrawObject.SELECTION);
	}
      } else if(
		evtMod == (BUTTON1_MASK|InputEvent.CTRL_MASK)
		||
		evtMod == (InputEvent.BUTTON1_MASK|InputEvent.CTRL_MASK)
		) {
	if(getGraph().getCurrent() != null) {
	  getGraph().setCurrent(null).getDrawObject().highlightOff(DrawObject.SELECTION);
	}
	if(!getGraph().isEditable()) break processBlock;
	if(elem != null && evtElem == elem) {
	  // do delete
	  doDelete(delElem);
	  delElem = null;
	}
      } else if(
		evtMod == (BUTTON1_MASK|InputEvent.SHIFT_MASK)
		||
		evtMod == (InputEvent.BUTTON1_MASK|InputEvent.SHIFT_MASK)
		) {
	if(getGraph().getCurrent() != null) {
	  getGraph().setCurrent(null).getDrawObject().highlightOff(DrawObject.SELECTION);
	}
	if(!getGraph().isEditable()) break processBlock;
	if(dragPt != null) {
	  rubberLine(evtX,evtY,dragPt.x,dragPt.y);
	  dragPt = null;
	}
	if(elem == null && (evtElem == null || !evtElem.isNode())) {
	  // create a node
	  Point cpt = getDrawPane().canonXY(pt.x,pt.y);
	  Attribute attr = new Attribute("pos",cpt.x + "," + cpt.y);
	  Vector attrs = new Vector();
	  attrs.addElement(attr);
	  attr = getGraph().getNodeAttribute("label");
	  if(attr == null || attr.getValue().equals("\\N")) {
	    attr = new Attribute("label","Node" + getGraph().getId(Grappa.NODE));
	    attrs.addElement(attr);
	  }
	  try {
	    elem = getGraph().createElement(Grappa.NODE,null,attrs);
	  } catch(InstantiationException inex) {
	    Grappa.displayException(inex);
	    break processBlock;
	  }
	  elem.getDrawObject().setBounds();
	  elem.getDrawObject().draw();
	} else if(elem != null && evtElem != null && evtElem != elem && evtElem.isNode()) {
	  if(elem.isNode()) {
	    // create an Edge
	    Vector info = new Vector(5);
	    info.addElement(elem); //tail
	    info.addElement(null); // tailPort (not available yet)
	    info.addElement(evtElem); //head
	    info.addElement(null); // headPort (not available yet)
	    info.addElement(null); // key

	    //Point hpt = ((DrawNode)evtElem.getDrawObject()).getPosition();
	    //Point tpt = ((DrawNode)elem.getDrawObject()).getPosition();
	    Point hpt = getDrawPane().canonXY(evtPt.x,evtPt.y);
	    Point tpt = getDrawPane().canonXY(pt.x,pt.y);

	    Point mid1 = new Point(hpt.x+(int)Math.round((double)(tpt.x - hpt.x)/3.0),hpt.y+(int)Math.round((double)(tpt.y - hpt.y)/3.0));
	    Point mid2 = new Point(hpt.x+(int)Math.round(2.0*(double)(tpt.x - hpt.x)/3.0),hpt.y+(int)Math.round(2.0*(double)(tpt.y - hpt.y)/3.0));
	    Point mid3 = new Point(hpt.x+(int)Math.round(0.98*(double)(tpt.x - hpt.x)),hpt.y+(int)Math.round(0.98*(double)(tpt.y - hpt.y)));
	    String pos = null;
	    if(getGraph().isDirected()) {
	      pos = "e," + tpt.x + "," + tpt.y + " " + hpt.x + "," + hpt.y + " " + mid1.x + "," + mid1.y + " " + mid2.x + "," + mid2.y + " " + mid3.x + "," + mid3.y;
	    } else {
	      pos = hpt.x + "," + hpt.y + " " + mid1.x + "," + mid1.y + " " + mid2.x + "," + mid2.y + " " + mid3.x + "," + mid3.y + " " + tpt.x + "," + tpt.y;
	    }
	    Attribute attr = new Attribute("pos",pos);
	    Vector attrs = new Vector();
	    attrs.addElement(attr);
	    try {
	      elem = getGraph().createElement(Grappa.EDGE,info,attrs);
	    } catch(InstantiationException inex) {
	      Grappa.displayException(inex);
	      break processBlock;
	    }
	    elem.getDrawObject().setBounds();
	    elem.getDrawObject().draw();
	  }
	}
      } else {
	// ignore mixed signals
	if(getGraph().getCurrent() != null) {
	  getGraph().setCurrent(null).getDrawObject().highlightOff(DrawObject.SELECTION);
	}
      }

      if(delElem != null) {
	highlightDelete(delElem,false);
	delElem = null;
      }
    }

    // TODO: evtElem == null and mouseDrag, then rubberband and on mouseUp, if
    // rectangle contains elements, then create subGraph

    public void mouseDragged(MouseEvent evt) {
      if(evtElem == null || !evtElem.isNode()) return;

      int evtMod = evt.getModifiers();

      Point pt = evt.getPoint();
      if(dragPt != null) {
	//if(dragPt.equals(pt)) return;
	rubberLine(evtX,evtY,dragPt.x,dragPt.y);
	dragPt = null;
      }

      processBlock: if(
		       evtMod == (BUTTON1_MASK|InputEvent.SHIFT_MASK)
		       ||
		       evtMod == (InputEvent.BUTTON1_MASK|InputEvent.SHIFT_MASK)
		       ) {
	if(!getGraph().isEditable()) break processBlock;
	dragPt = pt;
	rubberLine(evtX,evtY,dragPt.x,dragPt.y);
      }
      // no wrap up
    }

    public void mouseMoved(MouseEvent evt) {
      return;
    }

    private void highlightSelect() {
      Element elem = getGraph().setCurrent(evtElem);
      if(elem != null) {
	elem.getDrawObject().highlightOff(DrawObject.SELECTION);
      }
      evtElem.getDrawObject().highlightOn(DrawObject.SELECTION);
    }

    private void rubberLine(int x0, int y0, int x1, int y1) {
      Graphics gr = getGraphics();
      gr.setXORMode(getGraph().getDrawObject().getGC().getXORColor());
      gr.drawLine(x0,y0,x1,y1);
    }


    private void highlightDelete(Element elem, boolean turnOn) {
      if(elem == null) return;
      DrawObject drwobj = elem.getDrawObject();
      if(drwobj == null) return;
    
      if(turnOn) drwobj.highlightOn(DrawObject.DELETION);
      else drwobj.highlightOff(DrawObject.DELETION);

      if(elem.isNode()) {
	Enumeration enum = ((Node)elem).edgeElements();
	Edge edge = null;
	while(enum.hasMoreElements()) {
	  edge = (Edge)enum.nextElement();
	  drwobj = edge.getDrawObject();
	  if(drwobj != null) {
	    if(turnOn) drwobj.highlightOn(DrawObject.DELETION);
	    else drwobj.highlightOff(DrawObject.DELETION);
	  }
	}
	
      }
    }

    private void doDelete(Element elem) {
      if(elem == null) return;
      elem.delete();
      DrawPane.refreshGraph(getDrawPane().getDrawGraph());
    }
  }
}
