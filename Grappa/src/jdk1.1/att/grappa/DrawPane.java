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
import java.io.*;
import java.util.*;

/**
 * This class is used for displaying the graph.  The actual drawing is done on
 * a <code>DrawCanvas</code> object, which is contained within the
 * <code>Drawpane</code> and managed by it.
 *
 * @see DrawCanvas
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DrawPane extends ScrollPane implements ComponentListener
{
  /**
   * Default time in milliseconds before element tip pops up.
   */
  public final static int tipperTime = 1000;

  private Backgrounder backgrounder = null;

  private boolean showText = true;

  private Subgraph drawGraph = null;

  private Window parentWindow = null;

  private boolean editMode = false;
  private Graph graph = null;
  private DrawCanvas canvas = null;

  private double fixedSizeFactor = 0;

  private LinearMap mapping = new LinearMap();
  private Rectangle graphBBox = null;
  private Dimension canvasSize = null;
  private double edgeFactor = 0;

  private Dimension maxCanvasSize = null;

  private boolean doScaling     = false;
  private ElementTipper elemTip = null;

  /**
   * Creates a <code>DrawPane</code> instance.
   *
   * @param graph the graph object to be displayed in this pane.
   * @param doScaling when true, canvas will be of fixed size and drawing
   *                  will be scaled to fit within it, otherwise grow canvas
   *                  area as needed and do not scale.  Note that in this latter
   *                  case the image may be clipped if there are no scrollbars.
   * @param scrollbarPolicy one of:
   *                  <code>ScrollPane.SCROLLBARS_ALWAYS</code>,
   *                  <code>ScrollPane.SCROLLBARS_AS_NEEDED</code>, or
   *                  <code>ScrollPane.SCROLLBARS_NEVER</code>
   * @param prefSize preferred size of view area onto canvas.
   * @param backgrounder an image drawing interface for painting an arbitrary image in the background
   */
  public DrawPane(Graph graph, boolean doScaling, int scrollbarPolicy, Dimension prefSize, Backgrounder backgrounder) {
    super(scrollbarPolicy);
    this.graph = graph;
    this.drawGraph = (Subgraph)graph;
    this.doScaling = doScaling;
    this.backgrounder = backgrounder;
    this.canvas = new DrawCanvas(this,prefSize);
    this.canvas.setMinimumSize(prefSize.width,prefSize.height);
    graph.addDrawPane(this);
    Panel panel = new Panel();
    FlowLayout fl = new FlowLayout();
    fl.setAlignment(FlowLayout.LEFT);
    fl.setVgap(0);
    fl.setHgap(0);
    panel.setLayout(fl);
    panel.setBackground(getGraph().getDrawObject().getGC().getBackground());
    panel.add(canvas);
    add(panel);
    elemTip = new ElementTipper(this,tipperTime);
    addComponentListener(this);
  }

  /**
   * Creates a <code>DrawPane</code> instance.
   * Assumes a default preferred size of 600x400.
   *
   * @param graph the graph object to be displayed in this pane.
   * @param doScaling when true, canvas will be of fixed size and drawing
   *                  will be scaled to fit within it, otherwise grow canvas
   *                  area as needed and do not scale.  Note that in this latter
   *                  case the image may be clipped if there are no scrollbars.
   * @param scrollbarPolicy one of:
   *                  <code>ScrollPane.SCROLLBARS_ALWAYS</code>,
   *                  <code>ScrollPane.SCROLLBARS_AS_NEEDED</code>, or
   *                  <code>ScrollPane.SCROLLBARS_NEVER</code>
   * @param backgrounder an image drawing interface for painting an arbitrary image in the background
   */
  public DrawPane(Graph graph, boolean doScaling, int scrollbarPolicy, Backgrounder backgrounder) {
    this(graph,doScaling,scrollbarPolicy,new Dimension(600,400),backgrounder);
  }

  /**
   * Creates a <code>DrawPane</code> instance.
   * Assumes a default preferred size of 600x400 and no background image.
   *
   * @param graph the graph object to be displayed in this pane.
   * @param doScaling when true, canvas will be of fixed size and drawing
   *                  will be scaled to fit within it, otherwise grow canvas
   *                  area as needed and do not scale.  Note that in this latter
   *                  case the image may be clipped if there are no scrollbars.
   * @param scrollbarPolicy one of:
   *                  <code>ScrollPane.SCROLLBARS_ALWAYS</code>,
   *                  <code>ScrollPane.SCROLLBARS_AS_NEEDED</code>, or
   *                  <code>ScrollPane.SCROLLBARS_NEVER</code>
   */
  public DrawPane(Graph graph, boolean doScaling, int scrollbarPolicy) {
    this(graph,doScaling,scrollbarPolicy,new Dimension(600,400),null);
  }

  /**
   * Creates a <code>DrawPane</code> instance.
   * Assumes there is no background image.
   *
   * @param graph the graph object to be displayed in this pane.
   * @param doScaling when true, canvas will be of fixed size and drawing
   *                  will be scaled to fit within it, otherwise grow canvas
   *                  area as needed and do not scale.  Note that in this latter
   *                  case the image may be clipped if there are no scrollbars.
   * @param scrollbarPolicy one of:
   *                  <code>ScrollPane.SCROLLBARS_ALWAYS</code>,
   *                  <code>ScrollPane.SCROLLBARS_AS_NEEDED</code>, or
   *                  <code>ScrollPane.SCROLLBARS_NEVER</code>
   * @param prefSize preferred size of view area onto canvas.
   */
  public DrawPane(Graph graph, boolean doScaling, int scrollbarPolicy, Dimension prefSize) {
    this(graph,doScaling,scrollbarPolicy,prefSize,null);
  }

  /**
   * Get the current drawing graph as set during setupPane().
   *
   * @return the drawing graph
   */
  public Subgraph getDrawGraph() {
    return drawGraph;
  }

  /**
   * Get the canvas associated with this drawing pane.
   *
   * @return the drawing canvas
   */
  public DrawCanvas getCanvas() {
    return canvas;
  }

  /**
   * Setup the pane.
   * Based on the characteristics of this pane and the current size of
   * the graph, the canvas size and / or linear mapping are adjusted as needed.
   * If the mapping is recalculated, observers of the mapping are notified and
   * this <code>DrawPane</code> is passed as an
   * argument to the <code>notifyObservers</code> method.
   *
   * @see java.util.notifyObservers(Object) 
   */
  public void setupPane() {
    setupPane(getDrawGraph());
  }
  
  public void setupPane(Subgraph sgraph) {
    drawGraph = sgraph;

    int width, height;
    
    Rectangle bbox = getDrawGraph().getBounds();

    if(graphBBox != null && edgeFactor == mapping.getEdgeFactor()) {
      if(doScaling) {
	if(getScrollbarDisplayPolicy() == ScrollPane.SCROLLBARS_NEVER) {
	  if(graphBBox.equals(bbox) && canvasSize.equals(getViewportSize())) return;
	} else {
	  if(graphBBox.equals(bbox) && canvasSize.equals(getCanvas().getSize())) return;
	}
      } else {
	if(graphBBox.equals(bbox)) return;
      }
    }

    if(bbox == null) {
      throw new RuntimeException("graph bounding box is null");
    }

    graphBBox = new Rectangle(bbox);

    // need this... but why also here (and in Graph.getBounds())
    //bbox.setSize(bbox.width+1,bbox.height+1);
    if(doScaling) {
      if(getScrollbarDisplayPolicy() == ScrollPane.SCROLLBARS_NEVER) {
	width = getViewportSize().width;
	height = getViewportSize().height;
	if(maxCanvasSize != null) {
	  if(width > maxCanvasSize.width) width = maxCanvasSize.width;
	  if(height > maxCanvasSize.height) height = maxCanvasSize.height;
	}
	getCanvas().setSize(width,height);
      } else {
	width = getCanvas().getSize().width;
	height = getCanvas().getSize().height;
	boolean change = false;
	if(width < getCanvas().getMinimumSize().width) {
	  width = getCanvas().getMinimumSize().width;
	  change = true;
	}
	if(height < getCanvas().getMinimumSize().height) {
	  height = getCanvas().getMinimumSize().height;
	  change = true;
	}
	if(maxCanvasSize != null) {
	  if(width > maxCanvasSize.width) {
	    width = maxCanvasSize.width;
	    change = true;
	  }
	  if(height > maxCanvasSize.height) {
	    height = maxCanvasSize.height;
	    change = true;
	  }
	}
	if(change) {
	  getCanvas().setSize(width,height);
	}
      }
    } else {
      width = (int)Math.round((double)bbox.width/mapping.getEdgeFactor());
      height = (int)Math.round((double)bbox.height/mapping.getEdgeFactor());
      if(width < getCanvas().getMinimumSize().width) {
	width = getCanvas().getMinimumSize().width;
      }
      if(height < getCanvas().getMinimumSize().height) {
	height = getCanvas().getMinimumSize().height;
      }
      if(maxCanvasSize != null) {
	if(width > maxCanvasSize.width) width = maxCanvasSize.width;
	if(height > maxCanvasSize.height) height = maxCanvasSize.height;
      }
      getCanvas().setSize(width,height);
    }
    doLayout();
    //if(doScaling) {
      //if(getScrollbarDisplayPolicy() == ScrollPane.SCROLLBARS_NEVER) {
	//mapping.setMap(bbox,getViewportSize());
      //} else {
	//mapping.setMap(bbox,getCanvas().getSize());
      //}
    //} else {
      mapping.setMap(bbox,getCanvas().getSize());
    //}
    canvasSize = new Dimension(width,height);
    edgeFactor = mapping.getEdgeFactor();
    mapping.notifyObservers(this);
  }

  /**
   * Set the maximum allowed canvas size.
   *
   * @param width maximum canvas width
   * @param height maximum canvas height
   * @return previous maximum size or null, if not set
   */
  public Dimension setMaxCanvasSize(int width, int height) {
    Dimension prev = maxCanvasSize;
    maxCanvasSize = new Dimension(width,height);
    Dimension canSize = getCanvas().getSize();
    boolean change = false;
    if(canSize.width > maxCanvasSize.width) {
      canSize.width = maxCanvasSize.width;
      change = true;
    }
    if(canSize.height > maxCanvasSize.height) {
      canSize.height = maxCanvasSize.height;
      change = true;
    }
    if(change) {
      getCanvas().setSize(canSize.width,canSize.height);
      if(canvasSize != null) {
	setupPane();
	paintCanvas();
      }
    }
      
    return prev;
  }

  /**
   * Get the maximum allowed canvas size.
   *
   * @return maximum canvas size or null, if not set
   */
  public Dimension getMaxCanvasSize() {
    return maxCanvasSize;
  }
   

  /**
   * Convert a co-ordinate in the pane to the co-ordinates of the graph.
   *
   * @param x the x co-ordinate
   * @param y the y co-ordinate
   * @return the resulting re-mapped point
   *
   * @see LinearMap#revMap(int,int)
   */
  public Point canonXY(int x, int y) {
    return mapping.revMap(x,y);
    //return(new Point((int)((double)(x)/scale.x) - offset.x, (int)((double)(y)/scale.y) - offset.y));
  }

  /**
   * Get the current setting for showing text in this pane.
   *
   * @return true if the graph is drawn with text information visible, false
   *         if the text information has been suppressed.
   */
  public boolean getShowText() {
    return showText;
  }

  /**
   * Set the current state for showing text in this pane.
   *
   * @param newValue the new value for the text visibility status
   * @return the previous value of the text visibility setting
   */
  public boolean setShowText(boolean newValue) {
    boolean oldValue = showText;
    showText = newValue;
    if(showText && !oldValue) {
      GraphEnumeration elems = getDrawGraph().elements();
      while(elems.hasMoreElements()) {
	((Element)elems.nextElement()).getDrawObject().getPeerFor(this).setupPeer(true);
      }
    }
    return oldValue;
  }

  /**
   * Map the supplied parameters describing a rectangle in the graph
   * co-ordinate system into the co-ordinate system of this pane.
   *
   * @param x x co-ordinate of upper-left corner of rectangle
   * @param y y co-ordinate of upper-left corner of rectangle
   * @param w width of rectangle
   * @param h height of rectangle
   * @return the corresponding rectangle in the pane co-ordinate system
   *
   * @see LinearMap#map(double,double,double,double)
   * @see DoubleRectangle#getApproximation()
   */
  public Rectangle scaleRect(int x, int y, int w, int h) {
    return  mapping.map((double)x,(double)y,(double)w,(double)h).getApproximation();
  }
  public Rectangle xlateRect(int x, int y, int w, int h) {
    Point pt =  mapping.map((double)x,(double)(y+h)).getApproximation();
    return new Rectangle(pt.x,pt.y,w,h);
  }

  /**
   * Map the supplied rectangle in the graph
   * co-ordinate system into the co-ordinate system of this pane.
   *
   * @param rect a rectangle in the graph co-ordindate system
   * @return the corresponding rectangle in the pane co-ordinate system
   *
   * @see #scaleRect(int,int,int,int)
   */
  public Rectangle scaleRect(Rectangle rect) {
    return scaleRect(rect.x, rect.y, rect.width, rect.height);
  }

  /**
   * Convert a length along the x-axis of the graph co-ordinate system into
   * the corresponding length in the pane co-ordinate system.
   *
   * @param len the length in the graph view
   * @return the length in the pane view
   *
   * @see #scaleYLength(double)
   * @see LinearMap#scaleX(double)
   * @see Math#abs(double)
   */
  public double scaleXLength(double len) {
    return Math.abs(mapping.scaleX(len));
  }

  /**
   * Convert a length along the y-axis of the graph co-ordinate system into
   * the corresponding length in the pane co-ordinate system.
   *
   * @param len the length in the graph view
   * @return the length in the pane view
   *
   * @see #scaleXLength(double)
   * @see LinearMap#scaleY(double)
   * @see Math#abs(double)
   */
  public double scaleYLength(double len) {
    return Math.abs(mapping.scaleY(len));
  }

  /**
   * Map a double precision co-ordinate in the graph view to the pane view.
   *
   * @param ptx the x co-ordinate in the graph view
   * @param pty the y co-ordinate in the graph view
   * @return the corresponding double-precision point in the pane view
   * @see LinearMap#map(double,double)
   */
  public DoublePoint scalePoint(double ptx, double pty) {
    return mapping.map(ptx,pty);
  }

  /**
   * Map an integer co-ordinate in the graph view to the pane view.
   *
   * @param ptx the x co-ordinate in the graph view
   * @param pty the y co-ordinate in the graph view
   * @return the corresponding double-precision point in the pane view
   * @see LinearMap#map(double,double)
   * @see DoublePoint#getApproximation()
   */
  public Point scalePoint(int ptx, int pty) {
    return mapping.map((double)ptx,(double)pty).getApproximation();
  }

  /**
   * Map a polygon in the graph view to a polygon in the pane view.
   *
   * @param inPoly the polygon in the graph view
   * @return the corresponding polygon in the pane view
   */
  public Polygon scalePoly(Polygon inPoly) {
    Polygon outPoly = new Polygon();
    Point pt = null;
    for(int i = 0; i < inPoly.npoints; i++) {
      pt = scalePoint(inPoly.xpoints[i],inPoly.ypoints[i]);
      outPoly.addPoint(pt.x,pt.y);
    }
    return outPoly;
  }

  /**
   * Map a set of integer co-ordinates in the graph view to a
   * their corresponding co-ordinates in the pane view.
   * The co-ordinates are passed using an <code>IntPairs</code> object.
   *
   * @param inPairs the co-ordinate set in the graph view
   * @return the corresponding co-ordinate set in the pane view
   */
  public IntPairs scalePairs(IntPairs inPairs) {
    return scalePairsUntil(inPairs,inPairs.size());
  }

  /**
   * Map a sub-set of integer co-ordinates in the graph view to a
   * their corresponding co-ordinates in the pane view.
   * The co-ordinates are passed using an <code>IntPairs</code> object.
   *
   * @param inPairs the co-ordinate set in the graph view
   * @param end the ending point within the supplied set (where <code>1<=end<=inPairs.size()</code>)
   * @return the corresponding co-ordinate set in the pane view
   */
  public IntPairs scalePairsUntil(IntPairs inPairs, int end) {
    IntPairs outPairs = null;
    if(end > inPairs.size()) {
      end = inPairs.size();
    }
    if(inPairs.xArray[0] == inPairs.xArray[end-1] && inPairs.yArray[0] == inPairs.yArray[end-1]) {
      outPairs = new IntPairs(end);
    } else {
      outPairs = new IntPairs(end+1);
    }
    Point pt = null;
    for(int i = 0; i < end; i++) {
      pt = scalePoint(inPairs.xArray[i],inPairs.yArray[i]);
      outPairs.insertPairAt(pt.x,pt.y,i);
    }
    if(inPairs.xArray[0] != inPairs.xArray[end-1] || inPairs.yArray[0] != inPairs.yArray[end-1]) {
      outPairs.insertPairAt(outPairs.xArray[0],outPairs.yArray[0],end);
    }
    return outPairs;
  }

  /**
   * Map a polygon represented as a set of integer co-ordinates in the graph
   * view to a polygon in the pane view.
   *
   * @param inPairs the co-ordinates representing the polygon in the graph view
   * @return the corresponding polygon in the pane view
   */
  public Polygon scalePoly(IntPairs inPairs) {
    Polygon polygon = new Polygon();
    int end = inPairs.size();
    Point pt = null;
    for(int i = 0; i < end; i++) {
      pt = scalePoint(inPairs.xArray[i],inPairs.yArray[i]);
      polygon.addPoint(pt.x,pt.y);
    }
    return polygon;
  }

  /**
   * Map a point in the graph view to the corresponding point in the pane view.
   *
   * @param pt the point in the graph view
   * @return the corresponding point in the pane view
   * @see #scalePoint(int,int)
   */
  public Point scalePoint(Point pt) {
    return(scalePoint(pt.x,pt.y));
  }

  /**
   * Map a dimension in the graph view to the corresponding dimension in the pane view.
   *
   * @param sz the dimension in the graph view
   * @return the corresponding dimension in the pane view
   * @see #scaleSize(int,int)
   */
  public Dimension scaleSize(Dimension sz) {
    return(scaleSize(sz.width,sz.height));
  }

  /**
   * Map a width and height in the graph view to the corresponding dimension in the pane view.
   *
   * @param wd the width in the graph view
   * @param ht the height in the graph view
   * @return the corresponding dimension in the pane view
   * @see LinearMap#scale(double,double)
   * @see DoubleDimension#getApproximation()
   */
  public Dimension scaleSize(int wd, int ht) {
    return mapping.scale((double)wd,(double)ht).getApproximation();
  }

  /**
   * Add an observer of the <code>LinearMap</code> used by this pane.
   *
   * @param obs the observer of the mapping to be added
   * @see java.util.Observable#addObserver(java.util.Observer)
   */
  public void addObserver(java.util.Observer obs) {
    mapping.addObserver(obs);
  }

  /**
   * Delete an observer of the <code>LinearMap</code> used by this pane.
   *
   * @param obs the observer of the mapping to be deleted
   * @see java.util.Observable#deleteObserver(java.util.Observer)
   */
  public void deleteObserver(java.util.Observer obs) {
    mapping.deleteObserver(obs);
  }

  /**
   * Deletes all observers of the <code>LinearMap</code> used by this pane.
   *
   * @see java.util.Observable#deleteObservers()
   */
  public void deleteObservers() {
    mapping.deleteObservers();
  }
  
  /**
   * Draw the background using the specified <code>Graphics</code> object.
   *
   * @param gr the drawing <code>Graphics</code>
   * @see #doBackground(java.awt.image.ImageObserver,Graphics)
   */
  public void doBackground(Graphics gr) {
    doBackground(null,gr);
  }

  /**
   * Draw the background using the specified <code>Graphics</code> object
   * and <code>ImageObserver</code>.
   *
   * @param obs the image observer, if any
   * @param gr the drawing <code>Graphics</code>
   */
  public void doBackground(java.awt.image.ImageObserver obs, Graphics gr) {
    if(backgrounder != null) {
      backgrounder.drawImage(obs,gr,this);
    }
  }

  /**
   * Get the canvasSize used the last time the pane was adjusted.
   * The canvas size in set in <code>setupPane()</code> and is used
   * for adjusting the <code>LinearMap</code>.
   *
   * @return the last canvas size used in setting the <code>LinearMap</code>.
   */
  public Dimension getMapCanvasSize() {
    return canvasSize;
  }

  /**
   * Set the background drawing object for this pane.
   *
   * @param backgrounder the object for drawing the background of this pane
   * @see Backgrounder
   */
  public void setBackgrounder(Backgrounder backgrounder) {
    this.backgrounder = backgrounder;
  }

  /**
   * Get the <code>Graphics</code> object of the canvas.
   *
   * @return the canvas <code>Graphics</code> object
   */
  public Graphics getCanvasGraphics() {
    return getCanvas().getGraphics();
  }

  /**
   * Repaint the canvas.
   */
  public void paintCanvas() {
    //Point pt = pnl.getScrollPosition();
    //Dimension sz = pnl.getViewportSize();
    getCanvas().setImageFlag(true);
    getCanvas().repaint();
  }

  /**
   * Clear the canvas.
   */
  public void clearCanvas() {
    getCanvas().clear();
  }

  /**
   * Clear the canvas and its linear mapping.
   */
  public void clobberCanvas() {
    clearCanvas();
    mapping = null;
  }

  /**
   * Get the graph associated with this pane.
   *
   * @return the graph associated with this pane
   */
  public Graph getGraph() {
    return graph;
  }

  /**
   * Write image in GIF 87a format to an output stream.
   * The GIF can contain up to 256 colors.
   *
   * @param output the OutputStream to use for writing
   */
  public void gifImage(OutputStream output) throws IOException {
    gifImage(output,256);
  }

  /**
   * Write image in GIF 87a format to an output stream
   *
   * @param output the OutputStream to use for writing
   * @param colors the maximum number of colors (up to 256) to be allowed in
   *               the GIF (the size of the GIF file can be reduced a bit
   *               by specifying a number smaller than 256 when that is
   *               appropriate to do).
   */
  public void gifImage(OutputStream output, int colors) throws IOException {
    //Dimension sz = getCanvas().getSize();
    //Image image = getCanvas().createImage(sz.width,sz.height);
    //Graphics gr = image.getGraphics();
    //gr.setClip(0,0,sz.width,sz.height);
    //getCanvas().paintGraphics(gr);
    GIFEncoder gifen = null;
    try {
      gifen = new GIFEncoder(getCanvas().getOSImage(),colors);
    } catch(AWTException awt) {
      throw new IOException("GIFEncoder could not create (" + awt.toString() + ")");
    }
    if(gifen == null) {
      throw new IOException("GIFEncoder is null");
    }
    gifen.Write(output);
  }

  /**
   * Draw the specified graph onto its associated DrawPanes.
   *
   * @param graph the graph to be drawn
   * @param all if true, draw all elements even if redraw flag is not set
   */
  public static void drawGraph(Graph graph) {
    drawGraph((Subgraph)graph);
  }

  public static void drawGraph(Subgraph subgraph) {
    if(subgraph == null) {
      return;
    }
    if(!subgraph.hasDrawObjects()) {
      try {
	DrawObject.buildDrawObjects(subgraph);
      } catch(InstantiationException ie) {
	Grappa.displayException(ie);
	return;
      }
    }
    Enumeration panes = subgraph.getGraph().getGraphPanes();
    DrawPane pane = null;
    while(panes.hasMoreElements()) {
      pane = (DrawPane)panes.nextElement();
      pane.setupPane(subgraph);
      pane.paintCanvas();
    }

    //Enumeration elems = graph.elements();
    //Element elem = null;
    //while(elems.hasMoreElements()) {
      //elem = (Element)elems.nextElement();
      //if(elem.getDrawObject().getRedrawFlag()) {
	//elem.getDrawObject().setupDrawObject(null);
      //}
    //}
  }

  /**
   * Assume the graph has been setup and drawn and now redraw it
   *
   * @param graph the graph to redraw
   */
  public static void redrawGraph(Graph graph) {
    redrawGraph((Subgraph)graph);
  }

  public static void redrawGraph(Subgraph subgraph) {
    if(subgraph == null) {
      return;
    }
    if(!subgraph.hasDrawObjects()) {
      try {
	DrawObject.buildDrawObjects(subgraph);
      } catch(InstantiationException ie) {
	Grappa.displayException(ie);
	return;
      }
    }
    Enumeration elems = subgraph.elements();
    Element elem = null;
    while(elems.hasMoreElements()) {
      elem = (Element)elems.nextElement();
      if(elem.getDrawObject().getRedrawFlag()) {
	elem.getDrawObject().redraw();
      }
    }
    //Enumeration panes = graph.getGraphPanes();
    //while(panes.hasMoreElements()) {
      //((DrawPane)panes.nextElement()).paintCanvas();
    //}
  }

  /**
   * Paint the DrawPanes associated with the given graph.
   *
   * @param graph the graph whose DrawPanes are to be cleared
   */
  public static void paintGraph(Graph graph) {
    paintGraph((Subgraph)graph);
  }

  public static void paintGraph(Subgraph subgraph) {
    Enumeration panes = subgraph.getGraph().getGraphPanes();
    Rectangle bbox = subgraph.getGraph().getBounds();
    DrawPane pnl = null;
    //Point pt = null;
    //Dimension sz = null;
    while(panes.hasMoreElements()) {
      pnl = (DrawPane)panes.nextElement();
      //pt = pnl.getScrollPosition();
      //sz = pnl.getViewportSize();
      //pnl.getCanvasGraphics().setClip(pt.x,pt.y,sz.width,sz.height);
      //pnl.paintCanvas(pnl.scaleRect(bbox));
      pnl.setupPane(subgraph);
      pnl.paintCanvas();
    }
  }

  public static void refreshGraph(Graph graph) {
    refreshGraph((Subgraph)graph);
  }

  public static void refreshGraph(Subgraph subgraph) {
    Enumeration panes = subgraph.getGraph().getGraphPanes();
    DrawPane pnl = null;
    while(panes.hasMoreElements()) {
      pnl = (DrawPane)panes.nextElement();
      pnl.getCanvas().resetOSImage();
      pnl.getCanvas().erase(pnl.getCanvasGraphics(),null);
    }
  }

  /**
   * Clear the DrawPanes associated with the given graph.
   *
   * @param graph the graph whose DrawPanes are to be cleared
   */
  public static void clearGraph(Graph graph) {
    clearGraph((Subgraph)graph);
  }

  public static void clearGraph(Subgraph subgraph) {
    Enumeration panes = subgraph.getGraph().getGraphPanes();
    DrawPane pnl = null;
    while(panes.hasMoreElements()) {
      pnl = (DrawPane)panes.nextElement();
      pnl.clearCanvas();
    }
  }

  /**
   * Find the graph element that contains the specified point.
   * The point is in the co-ordinate system of the pane.
   *
   * @param pt the point to check
   * @return the first element found that contains the point
   * @see AppObject#setAccessibility(boolean)
   */
  public Element findContainingElement(Point pt) {
    GraphEnumeration elems = getDrawGraph().elements(Grappa.NODE|Grappa.EDGE);
    Element elem;
    AppObject appobj;

    while(elems.hasMoreElements()) {
      elem = (Element)elems.nextElement();
      appobj = elem.getAppObject();
      if(appobj == null || !appobj.getAccessibility()) continue;
      if(elem.getDrawObject().containsPaneXY(this,pt.x,pt.y)) {
	return elem;
      }
    }
    return null;
  }

  /**
   * Set the percent of canvas size to be reserved for a margin.
   * The margin is the area between the bounding box of the graph
   * and the edge of the canvas.
   *
   * @param pct the percent of the canvas size to reserve as a margin
   */
  public int setPercentMargin(int pct) {
    return  mapping.setPercentMargin(pct);
  }

  /**
   * Calls <code>setupPane()</code> when the pane is resized.
   *
   * @param evt the trigger event
   * @see #setupPane()
   */
  public void componentResized(ComponentEvent evt) {
    if(getScrollbarDisplayPolicy() == ScrollPane.SCROLLBARS_NEVER) {
      Dimension vpsz = getViewportSize();
      Dimension mnsz = getCanvas().getMinimumSize();
      int dw = mnsz.width - vpsz.width;
      int dh = mnsz.height - vpsz.height;
      dw = (dw < 0) ? 0 : dw;
      dh = (dh < 0) ? 0 : dh;
      if(dw > 0 || dh > 0) {
	if(parentWindow == null) {
	  Container prnt = getParent();
	  while(prnt != null && !(prnt instanceof Window)) {
	    prnt = prnt.getParent();
	  }
	  parentWindow = (Window)prnt;
	}
	if(parentWindow != null) {
	  mnsz = parentWindow.getSize();
	  parentWindow.setSize(mnsz.width+=dw,mnsz.height+=dh);
	}
      }
    }
    setupPane(getDrawGraph());
  }

  /**
   * Included as required by the <code>ComponentListener</code> interface.
   * @param evt the trigger event
   * @see #componentResized(ComponentEvent)
   */
  public void componentShown(ComponentEvent evt) {
  }

  /**
   * Included as required by the <code>ComponentListener</code> interface.
   * @param evt the trigger event
   * @see #componentResized(ComponentEvent)
   */
  public void componentHidden(ComponentEvent evt) {
  }

  /**
   * Included as required by the <code>ComponentListener</code> interface.
   * @param evt the trigger event
   * @see #componentResized(ComponentEvent)
   */
  public void componentMoved(ComponentEvent evt) {
  }


  /**
   * Set the delay time before tip is searched for and displayed.
   *
   * @param time the minimum time in milliseconds that the cursor needs to
   *             remain motionless for the tip to appear
   *
   * @return the previous delay time
   */
  public int setTipTime(int time) {
    return elemTip.setTipTime(time);
  }

  /**
   * Set the fixed size factor.
   * The fixed size factor, when non-zero, is used to scale the width and
   * height by a fixed amount so that node size is not changed when zooming.
   *
   * @param factor the new fixed size factor.
   * @return the previous fixed size factor.
   */
  public double setFixedSizeFactor(double factor) {
    double old = fixedSizeFactor;
    fixedSizeFactor = factor;
    return old;
  }

  /**
   * Get the fixed size factor.
   *
   * @return the current fixed size factor
   * @see DrawPane#setFixedSizeFactor
   */
  public double getFixedSizeFactor() {
    return fixedSizeFactor;
  }
}

