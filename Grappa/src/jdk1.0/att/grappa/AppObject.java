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

public class AppObject
{
  // graph types
  private static Hashtable classesByTag  = new Hashtable(5,5);
  private static Hashtable tagsByClasses = new Hashtable(5,5);
  private static Vector tagOrder         = new Vector(3,3);

  private Vector editList = null;

  public final static String CLASSES_DELIM = "|";

  // associated graph element
  private DotElement dotElement = null;
  // associated draw object
  private DrawObject drawObject = null;

  private boolean redrawFlag = true;

  private boolean initialized = false;

  protected boolean setDrawDefaults = false;

  public AppObject() {
    initialized = false;
  }

  protected void initialize(DotElement element, boolean setDefaults) {
    if(initialized) return;
    dotElement = element;
    if(setDefaults) {
      setDefaultValues();
    }
    initialized = true;
  }
  
  /**
   * Called during construction to initialize object as needed.
   */
  protected void setDefaultValues() {
    setDrawDefaults = true;
  }

  public boolean getSetDrawDefaults() {
    return setDrawDefaults;
  }

  /**
   * Adjust values based on specified attribute and value.
   *
   * @param attr the attribute to be handled.
   * @return true if attribute was handled.
   */
  public boolean handleAttribute(Attribute attr) {
    if(attr == null) {
      return true;
    }
    
    return drawObject.handleAttribute(attr);
  }

  public void setRedrawFlag(boolean mode) {
    redrawFlag = mode;
  }
  
  public boolean getRedrawFlag() {
    return redrawFlag;
  }

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
    addTag("shape","polygon",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Polygonal");
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
    //special cases
    addTag("element","graph",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Subgraph");
    addTag("element","edge",Grappa.PACKAGE_PREFIX+"AppObject",Grappa.PACKAGE_PREFIX+"Line");
  }

  public static void addTag(String tagName, String tagValue, String className, String drawClassName) {
    if(tagName == null || tagValue == null || className == null || drawClassName == null) {
      throw new IllegalArgumentException("arguments to addTag cannot be null");
    }
    String newTag = canonTag(tagName,tagValue);
    String classes = className + CLASSES_DELIM + drawClassName;
    String oldTag = (String)tagsByClasses.get(classes);
    if(oldTag != null && !oldTag.equals(newTag)) {
      throw new IllegalArgumentException("tags/classes relationship must be unique (not so for " + newTag + "/" + classes + ")");
    }
    classesByTag.put(newTag,classes);
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

  public static Class getTagClass(String tagName, String tagValue, boolean getApp) throws ClassNotFoundException {
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
    String canonTag = DotElement.canonValue(tagValue);
    if(tagName == null || canonTag == null) {
      throw new IllegalArgumentException("canonTag tagName is null or tagValue does not contain alphabetics");
    }
    return tagName + "=" + canonTag;
  }

  public static AppObject getAppObject(DotElement elem, boolean setDefaults) throws InstantiationException {
    AppObject appObject = null;
    Class appClass = null;
    Class drawClass = null;

    for(int i = 0; i < tagOrder.size(); i++) {
      String tagName = (String)tagOrder.elementAt(i);
      Attribute attr = elem.getAttribute(tagName);
      if(attr == null) continue;
      String tagValue = attr.getValue();
      if(tagValue == null) continue;
      appClass = null;
      try {
	appClass = getTagClass(tagName,tagValue,true);
	drawClass = getTagClass(tagName,tagValue,false);
      } catch(IllegalArgumentException iae) {
	throw new InstantiationException("illegal argument: " + iae.getMessage());
      } catch(ClassNotFoundException cnf) {
	throw new InstantiationException("class not found: " + cnf.getMessage());
      }
      if(appClass != null) break;
    }
    if(appClass == null) {
      try {
	if(elem.isGraph()) {
	  appClass = getTagClass("element","graph",true);
	  drawClass = getTagClass("element","graph",false);
	} else if(elem.isEdge()) {
	  appClass = getTagClass("element","edge",true);
	  drawClass = getTagClass("element","edge",false);
	} else {
	  appClass = getTagClass("element","node",true);
	  drawClass = getTagClass("element","node",false);
	}
      } catch(IllegalArgumentException iae) {
	throw new InstantiationException("illegal argument: " + iae.getMessage());
      } catch(ClassNotFoundException cnf) {
	throw new InstantiationException("class not found: " + cnf.getMessage());
      }
    }
    if(appClass == null) {
      throw new InstantiationException("class not found: no graph object element defined!");
    }
    try {
      appObject = (AppObject)appClass.newInstance();
    } catch(IllegalAccessException ixe) {
      throw new InstantiationException("illegal access: " + ixe.getMessage());
    }
    appObject.initialize(elem, setDefaults);
    if(drawClass == null) {
      throw new InstantiationException("draw class not found: no drawing object defined!");
    }
    // the following can throw an Instantiation exception
    appObject.setDrawObject(drawClass);
    return appObject;
  }

  private void setDrawObject(Class drawClass) throws InstantiationException {
    if(drawClass == null) {
      throw new InstantiationException("draw class is null!");
    }
    DrawObject drawObject = null;

    try {
      drawObject = (DrawObject)drawClass.newInstance();
    } catch(IllegalAccessException ixe) {
      throw new InstantiationException("illegal access: " + ixe.getMessage());
    }
    drawObject.initialize(this);
    this.drawObject = drawObject;
  }

  public static Enumeration tagList() {
    return tagOrder.elements();
  }

  public static void addTagToList(String tagName) {
    tagOrder.insertElementAt(tagName,tagOrder.size()-1);
  }

  public static void resetTagList() {
    tagOrder.insertElementAt(tagOrder.lastElement(),0);
    tagOrder.setSize(1);
  }

  /**
   * A convenience method for drawing -- calls the drawObject draw method
   *
   * @param gc the graphic context to use when drawing, if this value
               is null, then the draw object's context is used.
   * @param pane limit drawing to the specified pane, if this value
   *               is null, then draw on all panes for this graph.
   */
  public void draw(GraphicContext gc, DrawPane pane, boolean paintNow) {
    if(drawObject == null) return;
    drawObject.draw(gc,pane,paintNow);
  }

  /**
   * A convenience method that is equivalent to draw(null,null,false).
   */
  public void draw() {
    draw(null,null,false);
  }

  public DotElement getElement() {
    return dotElement;
  }

  public DrawObject getDrawObject() {
    return drawObject;
  }

  /* perhaps these should only be in DotElement
  public Attribute setAttribute(Attribute attr) {
    if(attr == null) return null;
    return getElement().setAttribute(attr);
  }

  public Attribute setAttribute(String name, String value) {
    if(name == null) return null;
    return setAttribute(new Attribute(name,value));
  }
  */

  /**
   * called when AppObject is selected by a mouseUp event and
   * used for application specific handling
   *
   * @param evt the mouseUp event (for reference)
   * @param p the mouse up point (also for reference)
   */
  public boolean selected(Event evt, MyPoint p) {
    return true;
  }

  public boolean isAccessible() {
    return true;
  }
  
  /**
   * called when AppObject properties are requested by a mouseUp event
   *
   * @param evt the mouseUp event (for reference)
   * @param p the mouse up point (also for reference)
   */
  public boolean properties(Event evt, MyPoint p) {
    DotElement elem = getElement();
    DotGraph graph = elem.getGraph();
    PopUpCard propcard = graph.getPropertiesCard();
    AttributePanel panel = null;
    if(propcard == null) {
      propcard = new PopUpCard("Properties");
      panel = new AttributePanel((Frame)propcard);
      propcard.insertLabelAndPanelAt("Attributes",panel,0);
      propcard.pack();
      graph.setPropertiesCard(propcard);
    } else {
      panel = (AttributePanel)propcard.getPanels().elementAt(0);
    }
    panel.loadAttributePanel(elem,editList);
    propcard.show();
    return true;
  }

  public void free() {
    dotElement = null;
    drawObject = null;
    redrawFlag = true;
    initialized = false;
    setDrawDefaults = false;
  }

  public void delete() {
    // can override to get application specific delete behavior (but call super.delete() eventually)
    getElement().detach();
  }

  public void editListAdd(String name) {
    if(name == null || name.length() == 0) return;
    if(editList == null) {
      editList = new Vector(8,4);
    }
    editList.addElement(name);
  }

  public void editListReset() {
    if(editList != null) {
      editList = null;
    }
  }

  public void editListEmpty() {
    if(editList != null) {
      editList.removeAllElements();
    } else {
      editList = new Vector(8,4);
    }
  }

  public int editListSize() {
    if(editList == null) return 0;
    return editList.size();
  }

  public Enumeration editListElements() {
    if(editList == null) {
      editList = new Vector(8,4);
    }
    return editList.elements();
  }

  public String getHandle() {
    return dotElement.getName();
  }
}
