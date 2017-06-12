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
 * This class provides the basis for the drawing of graph elements.
 * Extensions of this class and its subclasses allow the drawing methods
 * to be customized.  A large set of polygonal shapes are supplied with
 * Grappa.  The size and position of the object are as they are specified
 * by the element attributes.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public abstract class DrawObject implements Observer, Shape
{
  /**
   * The number of points per inch. This choice is fairly standard and also
   * it is the one used by the layout programs (e.g., dot).
   */
  public final static double pointsPerInch = 72.0;
  //public final static double fudgeFactor = 1.42;

  private Vector attrsOfInterest = null;

  private GraphicContext colorGC = new GraphicContext();

  private static final int[] zeroPair = {0, 0};

  /**
   * Value for indicating a selection highlight
   */
  public final static long SELECTION = 1;
  /**
   * Value for indicating a deletion highlight
   */
  public final static long DELETION  = 2;
  /**
   * Value for indicating a grouping highlight
   */
  public final static long GROUPING  = 4;

  private long highlightModes = 0;

  /**
   * Hash code for bounding box attribute (bb)
   */
  public final static int BBOX_HASH        = "bb".hashCode();
  /**
   * Hash code for color attribute (color)
   */
  public final static int COLOR_HASH       = "color".hashCode();
  /**
   * Hash code for distortion attribute (distortion)
   */
  public final static int DISTORTION_HASH  = "distortion".hashCode();
  /**
   * Hash code for fontcolor attribute (fontcolor)
   */
  public final static int FONTCOLOR_HASH   = "fontcolor".hashCode();
  /**
   * Hash code for fontname attribute (fontname)
   */
  public final static int FONTNAME_HASH    = "fontname".hashCode();
  /**
   * Hash code for fontsize attribute (fontsize)
   */
  public final static int FONTSIZE_HASH    = "fontsize".hashCode();
  /**
   * Hash code for fontstyle attribute (fontstyle)
   */
  public final static int FONTSTYLE_HASH   = "fontstyle".hashCode();
  /**
   * Hash code for height attribute (height)
   */
  public final static int HEIGHT_HASH      = "height".hashCode();
  /**
   * Hash code for label attribute (label)
   */
  public final static int LABEL_HASH       = "label".hashCode();
  /**
   * Hash code for label position attribute (lp)
   */
  public final static int LP_HASH          = "lp".hashCode();
  /**
   * Hash code for orientation attribute (orientation)
   */
  public final static int ORIENTATION_HASH = "orientation".hashCode();
  /**
   * Hash code for peripheries attribute (peripheries)
   */
  public final static int PERIPHERIES_HASH = "peripheries".hashCode();
  /**
   * Hash code for position attribute (pos)
   */
  public final static int POS_HASH         = "pos".hashCode();
  /**
   * Hash code for rank direction attribute (rankdir)
   */
  public final static int RANKDIR_HASH     = "rankdir".hashCode();
  /**
   * Hash code for rectangles attribute (rects)
   */
  public final static int RECTS_HASH       = "rects".hashCode();
  /**
   * Hash code for sides attribute (sides)
   */
  public final static int SIDES_HASH       = "sides".hashCode();
  /**
   * Hash code for skew attribute (skew)
   */
  public final static int SKEW_HASH        = "skew".hashCode();
  /**
   * Hash code for style attribute (style)
   */
  public final static int STYLE_HASH       = "style".hashCode();
  /**
   * Hash code for width attribute (width)
   */
  public final static int WIDTH_HASH       = "width".hashCode();

  private AppObject appObject = null;
  private DrawObjectPeer peer = null;

  private GraphicContext gc = new GraphicContext();

  private TextLabel textLabel = null;

  private boolean redrawFlag = true;

  private boolean initialized = false;

  /**
   * This flag indicates if there is a need to call setBounds() 
   */
  private boolean boundsFlag = true;

  /**
   * The bounding box of the element
   */
  protected Rectangle BoundingBox = new Rectangle();

  /**
   * Of course, this abstract class cannot be directly instantiated.
   * This constructor creates an uninitialized DrawObject with a default
   * set of attributes that it will observe (in its role as an Observer)
   *
   * @see java.util.Observer
   */
  public DrawObject() {
    initialized = false;

    attrOfInterest("color");
    attrOfInterest("fontcolor");
    attrOfInterest("fontname");
    attrOfInterest("fontsize");
    attrOfInterest("fontstyle");
    attrOfInterest("label");
  }

  /**
   * Initializes the DrawObject.  Application writers extending the DrawObject
   * class and deciding to override this method should best call
   * <code>super.initialize(obj)</code> in that case.
   *
   * @param obj the AppObject associated with this DrawObject
   */
  protected void initialize(AppObject obj) {
    if(initialized) return;
    appObject = obj;
    initialized = true;
    appObject.setDrawObject(this);
    Enumeration enum = getElement().getGraph().getGraphPanes();
    while(enum.hasMoreElements()) {
      createPeer((DrawPane)(enum.nextElement()));
    }
    // all Grappa attributes are automatically of interest
    Enumeration keys = getElement().getGraph().getGrappaAttributeKeys();
    while(keys.hasMoreElements()) {
      attrOfInterest((String)keys.nextElement());
    }
    if(attrsOfInterest != null && attrsOfInterest.size() > 0) {
      enum = attrsOfInterest.elements();
      Element elem = getElement();
      String name = null;
      Attribute attr = null;
      while(enum.hasMoreElements()) {
	name = (String)(enum.nextElement());
	if(name == null || name.length() == 0) continue;
	if(!Graph.validGrappaAttributeKey(name) || (attr = getElement().getGraph().getGrappaAttribute(name)) == null) {
	  attr = elem.getAttribute(name);
	}
	if(attr != null) {
	  attr.addObserver(this);
	  update(attr,null);
	}
      }
    }
    if(getElement().isSubgraph()) {
      Subgraph sg = (Subgraph)getElement();
      sg.setAppObjects();
      sg.setDrawObjects();
    }
  }
  
  /**
   * Setting the bounds flag true indicates that the bounding box needs
   * to be recomputed.  Whenever the flag is set to true, the bounds flag for
   * the entire graph is also set to true.
   *
   * @param mode value to which the bounds flag should be set
   */
  public void setBoundsFlag(boolean mode) {
    boundsFlag = mode;
    if(mode) {
      Element elem = getElement();
      if(elem.isSubgraph()) {
	((Subgraph)elem).setBoundsFlag(true);
      } else {
	elem.getSubgraph().setBoundsFlag(true);
      }
    }
  }
  
  /**
   * Get the value of the bounds flag.
   *
   * @return the current value of the bounds flag
   */
  public boolean getBoundsFlag() {
    return boundsFlag;
  }

  /**
   * Get the bounding box of this element
   *
   * @return the bounding box of this element
   */
  public Rectangle getBounds() {
    if(getBoundsFlag()) {
      setBounds();
    }
    if(BoundingBox == null) {
      BoundingBox = new Rectangle();
    }
    return BoundingBox.getBounds();
  }

  /**
   * This method to be called when the bounding box needs to be calculated.
   *
   * @return the bounding box of this object in graph co-ordinates
   */
  public abstract Rectangle setBounds();
  
  /**
   * Remove the local attribute from the element associated with this
   * DrawObject.  Which element to remove is determined by the name of the
   * supplied attribute, and the go-ahead for removal is that the value of the
   * supplied attribute be null or the empty string.  When the attribute is
   * removed, the associated default attribute, if any, takes effect.
   *
   * @param attr the name of this attribute determines what local attribute is
   *             to be removed and the value determines whether the attribute
   *             should be removed (a null or empty value indicates removal is
   *             desired).
   * @exception IllegalAttributeException whenever the attribute to be removed
   *                                      is one that must be present for
   *                                      drawing purposes
   * @return true if the attribute is removed, false otherwise
   */
  public boolean emptyMeansRemove(Attribute attr) throws IllegalAttributeException {
    if(attr.getValue() != null && attr.getValue().length() > 0) {
      return false;
    }
    int type = getElement().getType();
    Subgraph sg = getElement().getSubgraph();
    Vector chain = new Vector(4,4);
    Attribute dfltAttr = null;
    while(true) {
      if(sg == null) {
	dfltAttr = getElement().getGraph().getGlobalAttribute(type,attr.getName());
	if(dfltAttr == null || dfltAttr.getValue() == null || dfltAttr.getValue().length() == 0) {
	  throw new IllegalAttributeException("The " + ((type==Grappa.NODE)?"node":((type==Grappa.EDGE)?"edge":"graph")) + " attribute `" + attr.getName() + "' cannot have an empty value for drawing purposes.");
	}
	dfltAttr = new Attribute(dfltAttr);
	dfltAttr.clearChanged();
	break;
      } else {
	dfltAttr = ((type==Grappa.NODE)?
		    sg.getNodeAttribute(attr.getName()):
		    ((type==Grappa.EDGE)?
		    sg.getEdgeAttribute(attr.getName()):
		    sg.getLocalAttribute(attr.getName())));
	if(dfltAttr != null && dfltAttr != attr && dfltAttr.getValue() != null && dfltAttr.getValue().length() > 0) {
	  break;
	}
      }
      chain.addElement(sg);
      sg = sg.getSubgraph();
    }
    for(int i = chain.size()-1; i>=0; i--) {
      sg = (Subgraph)chain.elementAt(i);
      if(type==Grappa.NODE) {
	sg.setNodeAttribute(dfltAttr);
      } else if(type==Grappa.EDGE) {
	sg.setEdgeAttribute(dfltAttr);
      } else {
	sg.setAttribute(dfltAttr);
      }
    }
    // at this point, there is a valid default attribute and it has been applied
    if(attr == getElement().getLocalAttribute(attr.getName())) {
      if(getElement().isSubgraph()) {
	// set default
	//System.err.println("remove local empty and restoring default "+attr.getName()+" for "+getElement().getName());
	getElement().setAttribute(dfltAttr);
      } else {
	// remove local
	//System.err.println("remove local empty "+attr.getName()+" from "+getElement().getName());
	getElement().setAttribute(attr.getName(),null);
      }
    }
    return true;
  }

  /**
   * Set the redraw flag.
   *
   * @param mode set the redraw flag to this value
   */
  public void setRedrawFlag(boolean mode) {
    redrawFlag = mode;
  }
  
  /**
   * Get the current redraw flag.
   *
   * @return true if a redraw is indicated
   */
  public boolean getRedrawFlag() {
    return redrawFlag;
  }

  /**
   * Converts string of delimited integers to an array of integers.
   * The delimiters are any of comma, space or tab. Note that multiple
   * consecutive delimiters are treated as a single delimiter.
   * If string is null or contains no tokens, an array of length two is
   * returned, both of whose elements equal zero.
   *
   * @param tuple a string of delimited integers.
   * @exception NumberFormatException whenever a token in the tuple string is neither a delimiter nor an integer
   * @return the supplied string as an array of ints
   */
  public static int[] arrayForTuple(String tuple) throws NumberFormatException {
    if(tuple == null) return zeroPair;

    StringTokenizer st = new StringTokenizer(tuple,", \t",false);

    int[] array = new int[st.countTokens()];

    int i = 0;
    while(st.hasMoreTokens()) {
      array[i++] = Integer.valueOf(st.nextToken()).intValue();
    }

    if(array == null) return zeroPair;
    return array;
  }

  /**
   * Converts string of comma separated integers to a Point.
   *
   * @param tuple a string of two comma separated integers.
   * @return the supplied string as a Point.
   *
   * @exception IllegalArgumentException whenever the tuple string is not of the correct format
   * @exception NumberFormatException if <code>arrayForTuple</code> does
   */
  public static Point pointForTuple(String tuple) throws IllegalArgumentException, NumberFormatException {
    int[] array = arrayForTuple(tuple);

    if(array.length != 2) {
      throw new IllegalArgumentException("tuple \"" + tuple + "\" should have form \"x,y\" (has layout been performed?)");
    }
    
    // flip y-axis NOFLIP
    return(new Point(array[0],array[1]));
  }

  /**
   * Converts string of comma separated integers to a Rectangle.
   *
   * @param tuple a string of four comma separated integers.
   * @return the supplied string as a Rectangle.
   *
   * @exception IllegalArgumentException whenever the tuple string is not of the correct format
   * @exception NumberFormatException if <code>arrayForTuple</code> does
   */
  public static Rectangle rectForTuple(String tuple) throws IllegalArgumentException, NumberFormatException {
    int[] array = arrayForTuple(tuple);
    int itmp = 0;

    if(array.length != 4) {
      throw new IllegalArgumentException("tuple \"" + tuple + "\" should have form \"x,y,width,height\" (has layout been performed?)");
    }

    if(array[2] < array[0]) {
      itmp = array[2];
      array[2] = array[0];
      array[0] = itmp;
    }
    if(array[3] < array[1]) {
      itmp = array[3];
      array[3] = array[1];
      array[1] = itmp;
    }
      
    return(new Rectangle(array[0],array[1],array[2]-array[0],array[3]-array[1]));
  }

  /**
   * Converts pair of floating point strings to a Dimension.
   * In addition, the units are converted from inches to points.
   *
   * @param width the width string.
   * @param height the height string.
   * @return the supplied width and height strings as a Dimension.
   * @see DrawObject#pointsPerInch
   */
  protected Dimension dimensionForWidthHeight(String w, String h) {
    double width  = Double.valueOf(w).doubleValue();
    double height = Double.valueOf(h).doubleValue();
    int sizeW = (int)(width  * pointsPerInch);
    int sizeH = (int)(height * pointsPerInch);
    return new Dimension(sizeW,sizeH);
  }

  /**
   * Prepares this object for drawing with respect to the specified context.
   *
   * @param context the context to be used when drawing the object
   */
  public void setupDrawObject(GraphicContext context) {
    if(context == null) context = getGC();
    setBounds();
    if(setupNeeded()) setupPeers(context);
  }

  /**
   * Draws the graph element using the specified graphic context.
   *
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   * @param pane restrict drawing to the supplied pane, if supplied.
   */
  public void draw(GraphicContext context, DrawPane pane) {
    if(!getElement().isVisible()) return;
    if(context == null) context = getGC();
    setColors(getHighlightModes(),context);
    setBounds();
    if(setupNeeded()) setupPeers(context);

    DrawObjectPeer objectPeer = peer;
    do {
      if(pane == null || objectPeer.isDrawnOn(pane)) {
	DrawCanvas canvas = objectPeer.getDrawPane().getCanvas();
	objectPeer.draw(canvas.getOSGraphics(),context);
	Rectangle rect = objectPeer.getBounds();
    	canvas.repaint(rect.x,rect.y,rect.width+1,rect.height+1);
	if(pane != null) return;
      }
      objectPeer = objectPeer.getNext();
    } while(objectPeer != peer);
  }

  /**
   * Erases the graph element from the specified pane.
   *
   * @param pane the pane from which the drawn object is to be erased
   */
  public void erase(DrawPane pane) {
    setBounds();
    if(setupNeeded()) setupPeers(getGC());

    DrawObjectPeer objectPeer = peer;
    do {
      if(pane == null || objectPeer.isDrawnOn(pane)) {
	DrawCanvas canvas = objectPeer.getDrawPane().getCanvas();
	Rectangle rect = objectPeer.getBounds();
    	canvas.erase(null,rect);
	if(pane != null) return;
      }
      objectPeer = objectPeer.getNext();
    } while(objectPeer != peer);
  }

  /**
   * A convenience method that is equivalent to draw(null,null).
   * @see DrawObject#draw(att.grappa.GraphicContext, att.grappa.DrawPane)
   */
  public void draw() {
    draw(null,null);
  }

  /**
   * Get the graphic context of this object.
   *
   * @return this object's graphic context
   */
  public GraphicContext getGC() {
    return gc;
  }
  
  /**
   * Get the graphic context of this object's parent subgraph.
   *
   * @return the graphic context of the subgraph containing this object
   */
  public GraphicContext getParentGC() {
    Subgraph subg = getElement().getSubgraph();
    if(subg == null) {
      // root subgraph, so return graph GC
      return getElement().getGraph().getDrawObject().getGC();
    }
    return subg.getDrawObject().getGC();
  }

  //public double rotateX(double x, double y, double cosine, double sine) {
    //return(x * cosine + y * sine);
  //}

  //public double rotateY(double x, double y, double cosine, double sine) {
    //return(y * cosine - x * sine);
  //}

  Vector parseStyle(String style) {
    Vector output = new Vector(1,5);
    if(style == null) return output;
    if(style.length() == 0) {
      output.addElement("");
      return output;
    }
    boolean in_parens = false;
    int pos = 0, offset = 0;
    char[] array = style.toCharArray();
    while(pos < array.length) {
      while(pos < array.length && (Character.isWhitespace(array[pos]) || array[pos] == ',')) pos++;
      if(pos == array.length) {
	output.addElement("");
	return output;
      }
      offset = pos;
      if(array[pos] == '(') {
	if(in_parens) {
	  getElement().getGraph().printError("Nested parentheses not allowed in 'style' attribute");
	  output.removeAllElements();
	  output.addElement("");
	  return output;
	}
	in_parens = true;
	// put out paren token
	pos++;
      } else if(array[pos] == ')') {
	if(!in_parens) {
	  getElement().getGraph().printError("Unmatched ')' in 'style' attribute");
	  output.removeAllElements();
	  output.addElement("");
	  return output;
	}
	in_parens = false;
	// put out paren token
	pos++;
      } else {
	while(pos < array.length && array[pos] != ',' && array[pos] != '(' && array[pos] != ')') pos++;
      }
      output.addElement(new String(array,offset,pos - offset));
    }
    if(in_parens) {
      getElement().getGraph().printError("Unmatched '(' in 'style' attribute");
      output.removeAllElements();
      output.addElement("");
      return output;
    }
    return output;
  }

  /**
   * Get the element associated with this object.
   *
   * @return the <code>Element</code> object underlying this object
   */
  public Element getElement() {
    if(appObject == null) return null;
    return appObject.getElement();
  }

  /**
   * Reset this object and release its resources for garbage collection.
   */
  public void free() {
    if(peer != null) {
      DrawObjectPeer previous = peer;
      DrawObjectPeer current = previous.getNext();
      while(previous != current) {
	previous.setNext(current.getNext());
	current.free();
	current = previous.getNext();
      }
      current.free();
      peer = null;
    }
    setRedrawFlag(true);
    setBoundsFlag(true);
    initialized = false;
    appObject = null;
    textLabel = null;
  }

  /**
   * Delete the peer associated with this object and the specified pane.
   *
   * @param pane the pane related to the peer to be deleted
   */
  public void deletePeer(DrawPane pane) {
    if(peer == null) return;
    DrawObjectPeer previous = peer;
    DrawObjectPeer candidate = previous.getNext();
    do {
      if(candidate.isDrawnOn(pane)) {
	if(previous == candidate) {
	  // there is only one peer
	  peer = null;
	} else {
	  previous.setNext(candidate.getNext());
	  if(candidate == peer) {
	    peer = previous;
	  }
	}
	candidate.free();
	return;
      }
      previous = candidate;
      candidate = candidate.getNext();
    } while(previous != peer);
  }

  /**
   * Turn the specified highlighting mode on for this object
   *
   * @param mode the specific mode of highlighting
   */
  public void highlightOn(long mode) {
    highlightModes |= mode;
    draw(getGC(),null);
  }

  /**
   * Turn the specified highlighting mode off for this object
   *
   * @param mode the specific mode of highlighting
   */
  public void highlightOff(long mode) {
    highlightModes &= ~(mode);
    draw(getGC(),null);
  }

  /**
   * Get the current value of the highlight mode settings
   *
   * @return the highlight mode settings of this object
   */
  public long getHighlightModes() {
    return highlightModes;
  }

  void setColors(long modes, GraphicContext context) {
    if(modes == 0) {
      if(context.getFillMode()) {
	// line color
	colorGC.setForeground(getParentGC().getForeground());
	// fill color
	colorGC.setBackground(context.getForeground());
	// font color
	colorGC.setFontcolor(getParentGC().getFontcolor());
      } else {
	// line color
	colorGC.setForeground(context.getForeground());
	// fill color
	colorGC.setBackground(getParentGC().getBackground());
	// font color
	colorGC.setFontcolor(context.getFontcolor());
      }
      // xor color
      colorGC.setXORColor(context.getXORColor());
      // xor mode
      colorGC.setXORMode(context.getXORMode());
    } else if((modes&DrawObject.SELECTION) != 0) {
      GraphicContext selectGC = getElement().getGraph().getSelectionContext();
      // line color
      colorGC.setForeground(selectGC.getForeground());
      // fill color
      colorGC.setBackground(selectGC.getBackground());
      // font color
      colorGC.setFontcolor(selectGC.getFontcolor());
      // xor color
      colorGC.setXORColor(selectGC.getXORColor());
      // xor mode
      colorGC.setXORMode(selectGC.getXORMode());
    } else if((modes&DrawObject.DELETION) != 0) {
      GraphicContext deleteGC = getElement().getGraph().getDeletionContext();
      // line color
      colorGC.setForeground(deleteGC.getForeground());
      // fill color
      colorGC.setBackground(deleteGC.getBackground());
      // font color
      colorGC.setFontcolor(deleteGC.getFontcolor());
      // xor color
      colorGC.setXORColor(deleteGC.getXORColor());
      // xor mode
      colorGC.setXORMode(deleteGC.getXORMode());
    } else if((modes&DrawObject.GROUPING) != 0) {
      long grp = DrawObject.GROUPING<<1;
      for(int i = 0; i<getElement().getGraph().getGroupingContextsSize(); i++) {
	if((modes&grp) != 0) {
	  GraphicContext groupGC = (GraphicContext)(getElement().getGraph().getGroupingContextAt(i));
	  // line color
	  colorGC.setForeground(groupGC.getForeground());
	  // fill color
	  colorGC.setBackground(groupGC.getBackground());
	  // font color
	  colorGC.setFontcolor(groupGC.getFontcolor());
	  // xor color
	  colorGC.setXORColor(groupGC.getXORColor());
	  // xor mode
	  colorGC.setXORMode(groupGC.getXORMode());
	  break;
	}
	grp<<=1;
      }
    }
    if(appObject != null) {
	appObject.setColors(colorGC,modes,context,getParentGC());
    }
  }

  /**
   * Check if the specified highlight mode is active
   *
   * @return true if the mode is active, false otherwise
   */
  public boolean hasHighlightMode(long mode) {
    return ((getHighlightModes()&mode) != 0);
  }

  /**
   * Get the graphic context used for coloring.
   * A specific context for coloring is used since the highlighting modes
   * can change the color settings, but the original values need to be
   * retained as well.
   *
   * @return the coloring context
   */
  public GraphicContext getColorGC() {
    return colorGC;
  }

  /**
   * Use this method when changing the highlighting colors.
   * @return indicator of modes that have changed
   */
  static long setHighlightColor(Graph graph, String color, long mode) {
    long changedModes = 0;
    Color oldValue = null;
    GraphicContext workingGC = null;
    switch((int)mode) {
    case (int)DrawObject.SELECTION:
      workingGC = graph.getSelectionContext();
      oldValue = workingGC.setForeground(GraphicContext.getColor(color,workingGC.getBackground()));
      //since colors are cached can compare easily
      if(oldValue != workingGC.getForeground()) {
	changedModes |= DrawObject.SELECTION;
      }
      oldValue = workingGC.setFontcolor(GraphicContext.getColor(color,workingGC.getBackground()));
      //since colors are cached can compare easily
      if(oldValue != workingGC.getFontcolor()) {
	changedModes |= DrawObject.SELECTION;
      }
      break;
    case (int)DrawObject.DELETION:
      workingGC = graph.getDeletionContext();
      oldValue = workingGC.setForeground(GraphicContext.getColor(color,workingGC.getBackground()));
      //since colors are cached can compare easily
      if(oldValue != workingGC.getForeground()) {
	changedModes |= DrawObject.DELETION;
      }
      oldValue = workingGC.setFontcolor(GraphicContext.getColor(color,workingGC.getBackground()));
      //since colors are cached can compare easily
      if(oldValue != workingGC.getFontcolor()) {
	changedModes |= DrawObject.DELETION;
      }
      break;
    case (int)DrawObject.GROUPING:
      StringTokenizer st = new StringTokenizer(color,"|,:\t\n\r;/-",false);
      int count = 0;
      long grp = DrawObject.GROUPING<<1;
      GraphicContext tmpGC = null;
      String groupColor = null;
      while(st.hasMoreTokens()) {
	groupColor = st.nextToken();
	// getGroupingContext will return null if count is too big
	workingGC = graph.getGroupingContextAt(count);
	if(workingGC == null) {
	  if(tmpGC == null) {
	    // just need one since it will be cloned when added
	    tmpGC = new GraphicContext();
	  }
	  workingGC = graph.addGroupingContext(tmpGC);
	}
	oldValue = workingGC.setForeground(GraphicContext.getColor(groupColor,workingGC.getBackground()));
	//since colors are cached can compare easily
	if(oldValue != workingGC.getForeground()) {
	  changedModes |= (grp|DrawObject.GROUPING);
	}
	oldValue = workingGC.setFontcolor(GraphicContext.getColor(groupColor,workingGC.getBackground()));
	//since colors are cached can compare easily
	if(oldValue != workingGC.getFontcolor()) {
	  changedModes |= (grp|DrawObject.GROUPING);
	}
	grp <<= 1;
	count++;
      }
      break;
    }
    return changedModes;
  }

  /**
   * Instantiates the DrawObject for each element in a graph; this method
   * will ensure that the AppObjects have already been instantitated
   * 
   * @param graph the graph whose elements are to get DrawObjects
   * @exception InstantiationException occurs if there is a problem instantiating the DrawObjects or AppObjects for this graph
   */
  static void buildDrawObjects(Subgraph subgraph) throws InstantiationException {
    if(subgraph == null) {
      throw new InstantiationException("supplied graph is null");
    }
    Enumeration elems = subgraph.elements();
    Element elem;
    while(elems.hasMoreElements()) {
      elem = (Element)elems.nextElement();
      setDrawObject(elem);
    }
  }

  /**
   * Create the DrawObject for the given Element.
   *
   * @param elem the Element whose DrawOpbject is to be created
   * @exception InstantiationException whenever the DrawObject cannot be created
   */
  public static void setDrawObject(Element elem) throws InstantiationException {
    if(elem.getAppObject() == null) {
      AppObject.setAppObject(elem);
    }
    if(elem.getDrawObject() != null) return;
    DrawObject drawObj = (DrawObject)AppObject.makeObject(elem,false);
    // at this point, both DrawObject and AppObject exist
    drawObj.initialize(elem.getAppObject());
  }

  /**
   * Check if supplied attribute is of interest.
   *
   * @param candidate the attribute to look for
   * @return true if the attribute is worth watching
   */
  public boolean iSpy(Attribute candidate) {
    return iSpy(candidate,null);
  }

  /**
   * Check if supplied attribute is of interest.
   * The incumbent, if not null or equal to the candidate, is no longer
   * observed.
   *
   * @param candidate the attribute to look for
   * @param incumbent the attribute that possibly was observed earlier
   * @return true if the attribute is worth watching
   */
  public boolean iSpy(Attribute candidate, Attribute incumbent) {
    boolean watchIt = false;
    if(isOfInterest(candidate.getName())) {
      watchIt = true;
      candidate.addObserver(this);
      // stop observing incumbent, if needed
      if(incumbent != null && incumbent != candidate) incumbent.deleteObserver(this);
    }
    return watchIt;
  }

  /**
   * Add the name of an attribute of interest to this class
   *
   * @param name the name of the attribute
   */
  protected void attrOfInterest(String name) {
    if(name == null || isOfInterest(name)) return;
    if(attrsOfInterest == null) {
      attrsOfInterest = new Vector(8,8);
    }
    attrsOfInterest.addElement(name);
    Element elem = getElement();
    if(elem != null) {
      Attribute attr = elem.getAttribute(name);
      if(attr != null) {
	attr.addObserver(this);
	update(attr,null);
      }
    }
  }

  /**
   * Remove the name of an attribute of interest to this object
   *
   * @param name the name of the attribute
   */
  protected void attrNotOfInterest(String name) {
    if(name == null || !isOfInterest(name)) return;
    attrsOfInterest.removeElement(name);
    if(getElement() != null) {
      Attribute attr = getElement().getAttribute(name);
      if(attr != null) attr.deleteObserver(this);
    }
  }

  /**
   * Check if the name of an attribute of interest to this object
   *
   * @param name the name of the attribute
   * @return true when the name is of interest
   */
  public boolean isOfInterest(String name) {
    if(name == null || attrsOfInterest == null) return false;
    return attrsOfInterest.contains(name);
  }

  /**
   * This method is called whenever an observed Attribute is changed.
   * It is required by the <code>Observer</code> interface.
   *
   * @param obs the observable object that has been updated
   * @param arg when not null, it indicates that <code>obs</code> need no longer be
   *            observed and in its place <code>arg</code> should be observed.
   */
  public void update(Observable obs, Object arg) {
    // begin boilerplate
    if(!(obs instanceof Attribute)) {
      throw new IllegalArgumentException("expected to be observing attributes only (obs)");
    }
    Attribute attr = (Attribute)obs;
    if(arg != null) {
      if(!(arg instanceof Attribute)) {
	throw new IllegalArgumentException("expected to be observing attributes only (arg)");
      }
      attr.deleteObserver(this);
      attr = (Attribute)arg;
      attr.addObserver(this);
      // in case we call: super.update(obs,arg)
      obs = attr;
      arg = null;
    }
    // end boilerplate

    Color oldColor = null;
    
    if(attr.getNameHash() == DrawObject.COLOR_HASH) {
      if(emptyMeansRemove(attr)) return;
      oldColor = getGC().setForeground(attr.getValue());
      if(!oldColor.equals(getGC().getForeground())) {
	setRedrawFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.FONTCOLOR_HASH) {
      if(emptyMeansRemove(attr)) return;
      oldColor = getGC().setFontcolor(attr.getValue());
      if(!oldColor.equals(getGC().getFontcolor())) {
	setRedrawFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.FONTNAME_HASH) {
      if(emptyMeansRemove(attr)) return;
      String oldName = getGC().setFontname(attr.getValue());
      if(!oldName.equals(getGC().getFontname())) {
	setRedrawFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.FONTSIZE_HASH) {
      if(emptyMeansRemove(attr)) return;
      int oldSize = getGC().setFontsize(attr.getValue());
      if(oldSize != getGC().getFontsize()) {
	setRedrawFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.FONTSTYLE_HASH) {
      if(emptyMeansRemove(attr)) return;
      int oldStyle = getGC().setFontstyle(GraphicContext.xlateFontStyle(attr.getValue()));
      if(oldStyle != getGC().getFontstyle()) {
	setRedrawFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.LABEL_HASH) {
      TextLabel oldTl = textLabel;

      String label = (String)attr.getValue();
      if(label == null || label.length() == 0) {
	label = null;
      }
      if(oldTl == null) {
	textLabel = new TextLabel(this,label,getGC(),new Point());
	setRedrawFlag(true);
	setBoundsFlag(true);
      } else if(!oldTl.sameText(label)) {
	textLabel = new TextLabel(this,label,getGC(),oldTl.getPosition());
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.LP_HASH) {
      if(emptyMeansRemove(attr)) return;
      Point newPosition = pointForTuple(attr.getValue());
      if(textLabel == null) {
	textLabel = new TextLabel(this,null,getGC(),newPosition);
      } else if(!textLabel.getPosition().equals(newPosition)) {
	textLabel.setPosition(newPosition);
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(Graph.validGrappaAttributeKey(attr.getName())) {
      String value;
      if((value = attr.getValue()) != null && value.length() > 0) {
	long mode = 0;
	if(attr.getName().equals(Grappa.PKG_LOWER+"SelectionColor")) {
	  mode = DrawObject.SELECTION;
	} else if(attr.getName().equals(Grappa.PKG_LOWER+"DeletionColor")) {
	  mode = DrawObject.DELETION;
	} else if(attr.getName().equals(Grappa.PKG_LOWER+"GroupingColors")) {
	  mode = DrawObject.GROUPING;
	} else if(attr.getName().equals(Grappa.PKG_LOWER+"BackgroundColor")) {
	  if(attr.getObject() == null) {
	    attr.setObject(GraphicContext.getColor(value,getGC().getBackground()));
	  }
	  if(attr.getObject() != null) {
	    Color color = (Color)attr.getObject();
	    oldColor = getGC().setBackground(color);
	    if(color != oldColor) {
	      draw();
	    }
	  }
	}
	if(mode != 0) {
	  if(attr.getObject() == null) {
	    attr.setObject(new Long(setHighlightColor(getElement().getGraph(),value,mode)));
	  }
	  if(hasHighlightMode(mode)) {
	    if(hasHighlightMode(((Long)attr.getObject()).longValue())) {
	      draw(getGC(),null);
	    }
	  }
	}
      }
    }
  }

  /**
   * Redraw the object.
   */
  public void redraw() {
    if(getRedrawFlag()) {
      if(getBoundsFlag()) {
	setBounds();
      }
      draw(null,null);
      setRedrawFlag(false);
    }
  }

  /**
   * Checks if the supplied point is inside this object as it is
   * drawn on the supplied pane.
   *
   * @param pane the pane that supplied the frame of reference for the coordinates
   * @param pt the point to check
   * @return true if the point is contained in this object with respect to the specified pane
   */
  public boolean containsPanePoint(DrawPane pane, Point pt) {
    return containsPaneXY(pane,pt.x,pt.y);
  }

  /**
   * Checks if the supplied coordinates are inside this object as it is
   * drawn on the supplied pane.
   *
   * @param pane the pane that supplied the frame of reference for the coordinates
   * @param x the x-coordinate to check
   * @param y the y-coordinate to check
   * @return true if the coordinates are contained in this object with respect to the specified pane
   */
  public boolean containsPaneXY(DrawPane pane, int x, int y) {
    if(peer == null) return false;
    DrawObjectPeer candidate = peer;
    do {
      if(candidate.isDrawnOn(pane)) {
	peer = candidate; // so next search starts here
	return(candidate.contains(x,y));
      }
      candidate = candidate.getNext();
    } while(candidate != peer);
    return false;
  }
  
  /**
   * Creates the drawing peer specific for this object and the specified pane.
   *
   * @param pane the <code>DrawPane</code> upon which the object will be drawn.
   */
  //
  // should do something like this:
  //    if(pane == null) throw new IllegalArgumentException(...);
  //    new <SpecificPeerClass>(this,pane);
  //    return;
  public abstract void createPeer(DrawPane pane);

  /**
   * Set the starting peer for the linked-list of peers associated with this object.
   * @param peer the peer to set as the starting point
   * @return the previous starting point or null
   */
  public DrawObjectPeer setPeer(DrawObjectPeer peer) {
    DrawObjectPeer oldPeer = this.peer;
    this.peer = peer;
    return oldPeer;
  }

  /**
   * Get the starting peer for the linked-list of peers associated with this object.
   * @return the starting point or null
   */
  public DrawObjectPeer getPeer() {
    return peer;
  }

  /**
   * Get the peer for this object and the specified pane
   *
   * @param pane the pane whose peer is requested
   * @return the peer associated with the supplied pane or null
   */
  public DrawObjectPeer getPeerFor(DrawPane pane) {
    if(peer == null) return null;
    DrawObjectPeer candidate = peer;
    do {
      if(candidate.isDrawnOn(pane)) {
	peer = candidate; // so next search starts here
	return(candidate);
      }
      candidate = candidate.getNext();
    } while(candidate != peer);
    return null;
  }

  /**
   * Get the text label associated with this object
   *
   * @return this object's <code>TextLabel</code>
   */
  public TextLabel getTextLabel() {
    return textLabel;
  }

  /**
   * Set the text label for this object
   *
   * @param newTextLabel the new text label value
   * @return the old text label value
   */
  public TextLabel setTextLabel(TextLabel newTextLabel) {
    TextLabel oldTextLabel = textLabel;
    textLabel = newTextLabel;
    return oldTextLabel;
  }

  private boolean setupFlag = true;

  /**
   * Check if it appears that this object needs to be set-up
   *
   * @return true if setup is required, false otherwise
   */
  public boolean setupNeeded() {
    return setupFlag;
  }

  /**
   * Mark this object as requiring set-up
   */
  public void needSetup() {
    setupFlag = true;
  }
  
  /**
   * Set-up the peers associated with this object.
   *
   * @param context the context in which to perform the set-up
   */
  public void setupPeers(GraphicContext context) {
    boolean textSetup = false;
    if(getTextLabel() != null) {
      getTextLabel().setupText(context);
      textSetup = getTextLabel().setupNeeded();
    }
    DrawObjectPeer peer = getPeer();
    do {
      peer.setupPeer(textSetup);
      peer = peer.getNext();
    } while(peer != getPeer());
    setupFlag = false;
    if(textSetup) textLabel.resetSetupFlag();
  }
}
