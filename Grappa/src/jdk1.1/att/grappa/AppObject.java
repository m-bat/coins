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

import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class provides the basic handle for application writers to customize
 * graph elements.  Elements can be customized by extending this class and
 * overriding methods already provided or adding new variables and methods as
 * needed.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class AppObject implements Observer
{
  private final static String CLASSES_DELIM = "|";

  // graph types
  private static Hashtable classesByTag  = new Hashtable(5,5);
  private static Hashtable tagsByClasses = new Hashtable(5,5);
  private static Vector tagOrder         = new Vector(3,3);

  private Vector editList = null;

  private boolean accessibility = true;

  private Vector attrsOfInterest = null;

  // associated graph element
  private Element element = null;
  // associated draw object
  private DrawObject drawObject = null;

  private boolean redrawFlag = true;

  private boolean initialized = false;

  private static boolean initFinished = false;
  private static Class appClass = null;
  private static Class drwClass = null;

  static {
    tagOrder.addElement("shape");
    addTag("shape","ellipse",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Ellipse");
    addTag("shape","circle",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Circle");
    addTag("shape","egg",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Egg");
    addTag("shape","triangle",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Triangle");
    addTag("shape","box",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Box");
    addTag("shape","plaintext",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"PlainText");
    addTag("shape","diamond",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Diamond");
    addTag("shape","trapezium",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Trapezium");
    addTag("shape","parallelogram",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Parallelogram");
    addTag("shape","house",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"House");
    addTag("shape","hexagon",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Hexagon");
    addTag("shape","octagon",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Octagon");
    // general-polygonal shapes
    addTag("shape","polygon",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"DrawNode");
    // redundant shapes
    addTag("shape","doublecircle",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"DoubleCircle");
    addTag("shape","invtriangle",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"InvertedTriangle");
    addTag("shape","invtrapezium",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"InvertedTrapezium");
    addTag("shape","invhouse",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"InvertedHouse");
    addTag("shape","doubleoctagon",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"DoubleOctagon");
    addTag("shape","tripleoctagon",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"TripleOctagon");
    addTag("shape","Mdiamond",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"MDiamond");
    addTag("shape","Mcircle",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"MCircle");
    addTag("shape","Msquare",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"MSquare");
    // unrecognized (by layout program) shapes
    addTag("shape","square",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Square");
    addTag("shape","roundedbox",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"RoundedBox");
    addTag("shape","wedge",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Wedge");
    // non-polygonal shapes
    addTag("shape","record",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Table");
    addTag("shape","Mrecord",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"MTable");
    // edges
    addTag("shape","spline",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Spline");
    addTag("shape","line",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Line");
    //special cases
    addTag("element","subgraph",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"DrawSubgraph");
    // Note: an edge with no shape is the same as a edge with shape = spline.
    addTag("element","edge",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"DrawEdge");
    /*
     * DO NOT DO THIS... it is a duplication of earlier information , namely
     * shape/ellipse/AppObject/Ellipse,  and so causes an exception in addTag,
     * which results in an ExceptionInInitializationError!
     * addTag("element","node",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Ellipse"); // not really needed, since "shape" has a default value
     */
    initFinished = true;
  }

  /**
   * Creates an uninitialized AppObject.
   */
  public AppObject() {
    initialized = false;
  }

  /**
   * Initializes the AppObject.  Application writers extending the AppObject
   * class should best call <code>super.initialize(element)</code> when
   * overriding this method.
   *
   * @param element the Element associated with this AppObject
   */
  protected void initialize(Element element) {
    if(initialized) return;
    this.element = element;
    initialized = true;
    element.setAppObject(this);
    if(attrsOfInterest != null && attrsOfInterest.size() > 0) {
      Enumeration enum = attrsOfInterest.elements();
      String name = null;
      Attribute attr = null;
      while(enum.hasMoreElements()) {
	name = (String)(enum.nextElement());
	if(name == null || name.length() == 0) continue;
	attr = element.getAttribute(name);
	if(attr != null) {
	  attr.addObserver(this);
	  update(attr,null);
	}
      }
    }
    if(element.isSubgraph()) {
      ((Subgraph)element).setAppObjects();
    }
  }

  /**
   * Specify the classes to use for the DrawObject and AppObject of an
   * Element based on an attribute name/value pairing.  In other words,
   * when an Element has an attribute with name <code>tagName</code> and
   * value <code>tagValue</code>, then use <code>className</code> to
   * instantiate its AppObject and <code>drawClassName</code> to instantiate
   * its DrawObject. Those classes must exist and be sub-classes of
   * AppObject and DrawObject, respectively.
   *
   * @param tagName the attribute name to sit up and take notice of
   * @param tagValue the attribute value used for making the association
   * @param className the class to use for instantiating the AppObject
   * @param drawClassName the class to use for instantiating the DrawObject
   * @exception IllegalArgumentException whenever a parameter is null, a given
   *                                     class name does not exist or is not
   *                                     an appropriate sub-class, or the given
   *                                     relationship is not unique.
   */
  public static void addTag(String tagName, String tagValue, String className, String drawClassName) throws IllegalArgumentException {
    if(tagName == null || tagValue == null || className == null || drawClassName == null) {
      throw new IllegalArgumentException("arguments to addTag cannot be null");
    }
    if(initFinished) {
      Class tstClass = null;
      try {
	if(appClass == null) {
	  appClass = Class.forName(Grappa.PACKAGE_PREFIX+"AppObject");
	  drwClass = Class.forName(Grappa.PACKAGE_PREFIX+"DrawObject");
	}
	tstClass = Class.forName(className);
      } catch(ClassNotFoundException cnf) {
	throw new IllegalArgumentException("there is no class `" + className + "'");
      }
      if(!appClass.isAssignableFrom(tstClass)) {
	throw new IllegalArgumentException("class `" + className + "' is not a sub-class of AppObject");
      }
      try {
	tstClass = Class.forName(drawClassName);
      } catch(ClassNotFoundException cnf) {
	throw new IllegalArgumentException("there is no class `" + drawClassName + "'");
      }
      if(!drwClass.isAssignableFrom(tstClass)) {
	throw new IllegalArgumentException("class `" + drawClassName + "' is not a sub-class of DrawObject");
      }
    }
    if(!tagName.equals("element") && !tagListContains(tagName)) {
      addTagToList(tagName);
    }
    String newTag = canonTag(tagName,tagValue);
    String classes = className + CLASSES_DELIM + drawClassName;
    String oldTag = (String)tagsByClasses.get(classes);
    if(oldTag != null && !oldTag.equals(newTag)) {
      throw new IllegalArgumentException("tags::classes relationship must be unique (not so for " + newTag + "::" + classes + ")");
    }
    oldTag = (String)classesByTag.put(newTag,classes);
    if(oldTag != null) {
      tagsByClasses.remove(oldTag);
    }
    tagsByClasses.put(classes,newTag);
  }

  private static String getTagClassesString(String tag) {
    if(tag == null) {
      return null;
    }
    if(classesByTag == null) {
      return null;
    }
    return (String)classesByTag.get(tag);
  }

  /**
   * Get the class matching a name / value pair.  Whether the AppObject related
   * class or the DrawObject related class is returned depends on an
   * additional parameter.
   *
   * @param tagName the attribute name to require
   * @param tagValue the attribute value to require
   * @param getApp use true to get the AppObject class, false to get the DrawObject class
   * @exception IllegalArgumentException whenever the parameters are null
   * @exception ClassNotFoundException whenever the tagName/tagValue combination are not registered or the corresponding class is no longer found.
   * @return the requested Class
   */
  public static Class getTagClass(String tagName, String tagValue, boolean getApp) throws IllegalArgumentException, ClassNotFoundException {
    if(tagName == null || tagValue == null) {
      throw new IllegalArgumentException("method getTagClass() cannot take null arguments");
    }
    String tag = canonTag(tagName,tagValue);
    String classes = getTagClassesString(tag);
    if(classes == null) {
      throw new ClassNotFoundException("the tag \"" + tag + "\" is not recognized");
    }
    int delim = classes.indexOf(CLASSES_DELIM);
    String className = null;
    if(getApp) {
      className = classes.substring(0,delim);
    } else {
      className = classes.substring(delim+1);
    }
    // this call will throw a class not found exception, too.
    return Class.forName(className);
  }

  private static String canonTag(String tagName, String tagValue) {
    String canonTag = Element.canonValue(tagValue);
    if(tagName == null || canonTag == null) {
      throw new IllegalArgumentException("canonTag tagName is null or tagValue does not contain alphabetics");
    }
    return tagName + "=" + canonTag;
  }

  /**
   * Create the AppObject for the given Element.
   *
   * @param elem the Element whose AppObject is to be created
   * @exception InstantiationException whenever the AppObject cannot be created
   */
  public static void setAppObject(Element elem) throws InstantiationException {
    if(elem.getAppObject() != null) return;
    AppObject appObject = (AppObject)makeObject(elem,true);
    
    // appObject cannot be null (exception would have been thrown)
    appObject.initialize(elem);
  }

  static Object makeObject(Element elem, boolean setAppObj) throws InstantiationException {
    Object obj = null;
    Class objClass = null;

    for(int i = 0; i < tagOrder.size(); i++) {
      String tagName = (String)tagOrder.elementAt(i);
      Attribute attr = elem.getAttribute(tagName);
      if(attr == null) continue;
      String tagValue = attr.getValue();
      if(tagValue == null || tagValue.length() == 0) continue;
      objClass = null;
      try {
	objClass = getTagClass(tagName,tagValue,setAppObj);
      } catch(IllegalArgumentException iae) {
	throw new InstantiationException("illegal argument: " + iae.getMessage());
      } catch(ClassNotFoundException cnf) {
	throw new InstantiationException("class not found: " + cnf.getMessage());
      }
      if(objClass != null) break;
    }
    if(objClass == null) {
      try {
	if(elem.isSubgraph()) {
	  objClass = getTagClass("element","subgraph",setAppObj);
	} else if(elem.isEdge()) {
	  objClass = getTagClass("element","edge",setAppObj);
	} else {
	  objClass = getTagClass("element","node",setAppObj);
	}
      } catch(IllegalArgumentException iae) {
	throw new InstantiationException("illegal argument: " + iae.getMessage());
      } catch(ClassNotFoundException cnf) {
	throw new InstantiationException("class not found: " + cnf.getMessage());
      }
    }
    if(objClass == null) {
      throw new InstantiationException("class not found: no graph object element defined for '" + elem.getName() + "'");
    }
    try {
      obj = objClass.newInstance();
    } catch(IllegalAccessException ixe) {
      throw new InstantiationException("illegal access: " + ixe.getMessage());
    }
    return obj;
  }

  private static boolean tagListContains(String tag) {
    return tagOrder.contains(tag);
  }

  //public static Enumeration tagList() {
    //return tagOrder.elements();
  //}

  private static void addTagToList(String tagName) {
    // want the default ("shape") to always be searched last
    tagOrder.insertElementAt(tagName,tagOrder.size()-1);
  }

  //public static void resetTagList() {
    //tagOrder.insertElementAt(tagOrder.lastElement(),0);
    //tagOrder.setSize(1);
  //}

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
   * A convenience method for drawing -- calls the drawObject draw method
   *
   * @param gc the graphic context to use when drawing, if this value
               is null, then the draw object's context is used.
   * @param pane limit drawing to the specified pane, if this value
   *               is null, then draw on all panes for this graph.
   */ 
  public void draw(GraphicContext gc, DrawPane pane) {
    if(drawObject == null) return;
    drawObject.draw(gc,pane);
  }

  /**
   * A convenience method that is equivalent to draw(null,null).
   *
   * @see AppObject#draw(att.grappa.GraphicContext, att.grappa.DrawPane)
   */
  public void draw() {
    draw(null,null);
  }

  /**
   * Called whenever the currency of the associated Element changes.
   * Allows application writers to take specific action based on currency.
   *
   * @param mode true when the Element becomes current, false when the element is
   *             no longer current
   * @see Graph#setCurrent(att.grappa.Element)
   */
  public void setCurrent(boolean mode) {
    return;
  }

  /**
   * Get the element associated with this AppObject.
   *
   * @return the Element associated with this AppObject
   */
  public Element getElement() {
    return element;
  }

  /**
   * Get the draw object associated with this AppObject.
   *
   * @return the DrawObject associated with this AppObject
   */
  public DrawObject getDrawObject() {
    return drawObject;
  }

  /**
   * Called whenever the AppObject is selected by a mouseUp event.
   * Used for application specific handling
   *
   * @param evt the mouseUp event (for reference)
   * @param p the mouse up point (also for reference)
   */
  public boolean selected(MouseEvent evt, Point p) {
    return true;
  }

  /**
   * Called whenever drawing colors are being set. Allows for customiziation.
   *
   * @param colorGC the GraphicContext used for coloring. Note: foreground
   *                is used for line color, and the background is used for
   *                the fill color.
   * @param modes   current mode of the object (0 means normal, or can be some
   *                combination of DrawObject.SELECTION, DrawObject.DELETION,
   *                or DrawObject.GROUPING.
   * @param context the GraphicContext of the graph element
   * @param parentGC the GraphicContext of the element's (sub-graph) parent
   */
  public void setColors(GraphicContext colorGC, long modes, GraphicContext context, GraphicContext parentGC) {
      return;
  }

  /**
   * Set the accessibility of this AppObject.
   * An inaccessible AppObject means that its corresponding element will
   * not be found using DrawPane.findContainingElement() and this is
   * unreachable via mouse events.
   *
   * @param mode pass true to make the AppObject accessible (default)
   * @return the previous accessibility value
   * @see DrawPane#findContainingElement(java.awt.Point)
   */
  public boolean setAccessibility(boolean mode) {
    boolean oldValue = accessibility;
    accessibility = mode;
    return oldValue;
  }

  /**
   * Get the accessibility of this AppObject.
   *
   * @return true if the AppObject is currently accessible
   * @see AppObject#setAccessibility()
   */
  public boolean getAccessibility() {
    return accessibility;
  }
  
  /**
   * Called when AppObject properties are requested by a mouseUp event
   * By default, an AttributePanel is displayed in a PopUpCard.
   *
   * @param evt the mouseUp event (for reference)
   * @param p the mouse up point (also for reference)
   */
  public boolean properties(MouseEvent evt, Point p) {
    Element elem = getElement();
    Graph graph = elem.getGraph();
    PopUpCard propcard = graph.getGraphPropertiesCard();
    AttributePanel panel = null;
    if(propcard == null) {
      propcard = new PopUpCard("Properties");
      panel = new AttributePanel((Frame)propcard);
      propcard.insertLabelAndPanelAt("Attributes",panel,0);
      propcard.pack();
      graph.setAsGraphPropertiesCard(propcard);
    } else {
      panel = (AttributePanel)propcard.getPanels().elementAt(0);
    }
    panel.loadAttributePanel(elem,editList);
    propcard.show();
    return true;
  }

  /**
   * Included to help speed release of valuable memory.
   */
  public void free() {
    element = null;
    drawObject = null;
    redrawFlag = true;
    initialized = false;
  }

  /**
   * Called when an element is to be deleted.
   * Can be overridden to provide application specific delete behavior.
   * This method should not be called directly, rather use the
   * delete() method of the Element class.
   *
   */
  public void delete() {
  }

  /**
   * Add a name to the list of displayed/editable attributes.
   *
   * @param name the attribute name to add to the list
   */
  public void editListAdd(String name) {
    if(name == null || name.length() == 0) return;
    if(editList == null) {
      editList = new Vector(8,4);
    }
    editList.addElement(name);
  }

  /**
   * Clear the list of displayed/editable attributes (and thus make all
   * attributes available).
   */
  public void editListReset() {
    if(editList != null) {
      editList = null;
    }
  }

  /**
   * Empty the list of displayed/editable attributes (and thus make no
   * attributes available).
   */
  public void editListEmpty() {
    if(editList != null) {
      editList.removeAllElements();
    } else {
      editList = new Vector(8,4);
    }
  }

  /**
   * Get the size of the attribute edit list.
   *
   * @return the size of the edit list.
   */
  public int editListSize() {
    if(editList == null) return 0;
    return editList.size();
  }

  /**
   * Get an enumeration of the elements in the attribute edit list.
   *
   * @return an Enumeration of String objects
   */
  public Enumeration editListElements() {
    if(editList == null) {
      return Grappa.emptyEnumeration.elements();
    }
    return editList.elements();
  }

  /**
   * By default, the Element's name is returned.  Override to provide
   * a different name.  In particular, this method is used by the
   * AttributePanel class.
   *
   * @return the name of this AppObject
   * @see AttributePanel
   */
  public String getHandle() {
    return element.getName();
  }

  /**
   * Instantiates the AppObject for each element in a graph
   * 
   * @param graph the graph whose elements are to get AppObjects
   * @exception InstantiationException whenever the supplied graph is null or the AppObject cannot be created
   */
  static void buildAppObjects(Subgraph subgraph) throws InstantiationException {
    if(subgraph == null) {
      throw new InstantiationException("supplied graph is null");
    }
    GraphEnumeration elems = subgraph.elements();
    Element elem;
    while(elems.hasMoreElements()) {
      elem = (Element)elems.nextElement();
      AppObject.setAppObject(elem);
    }
  }

  /**
   * Set DrawObject associated with object.
   * @param drawObject value to which drawObject is to be set
   */
  protected void setDrawObject(DrawObject drawObject) {
    this.drawObject = drawObject;
  }

  /**
   * Check if supplied attribute is of interest; associated DrawObject,
   * if any, is also checked
   *
   * @param candidate the attribute to look for
   * @return true if the attribute is worth watching
   */
  public boolean iSpy(Attribute candidate) {
    return iSpy(candidate,null);
  }

  /**
   * Check if supplied attribute is of interest; associated DrawObject,
   * if any, is also checked.
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
    if(getDrawObject() != null) {
      boolean drawWatch = getDrawObject().iSpy(candidate,incumbent);
      watchIt = watchIt ? true : drawWatch;
    }
    return watchIt;
  }

  /**
   * Add the name of an attribute of interest to this object
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
   * Check if the name of an attribute of interest to this class
   *
   * @param name the name of the attribute
   * @return true when the name is of interest
   */
  public boolean isOfInterest(String name) {
    if(name == null || attrsOfInterest == null) return false;
    return attrsOfInterest.contains(name);
  }

  /**
   * This method is called whenever the observed Attribute is changed.
   * This method is requited by the Observer interface.
   *
   * @param obs the object being observed (in this case, an Attribute)
   * @param arg when not null indicated that observation should be shifted
   *            from the current Observable (Attribute) to the Observable
   *            (Attribute) indicated by the arg
   * @see Attribute
   * @see java.util.Observer
   * @see java.util.Observable
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
    // beyond that nothing to do, subclasses will override as needed
  }
}


