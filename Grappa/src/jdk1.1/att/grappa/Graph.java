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
import java.io.*;

/**
 * This class is the root of the overall graph and provides methods for
 * working with the entire graph (for example. printing the graph). It is an
 * extension of the Subgraph class.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Graph extends Subgraph
{
  /**
   * The string used for indentation when printing out the graph.
   */
  public final static String INDENT_STRING = "  ";

  private StringBuffer indent = null;
  private PrintWriter errWriter = null;

  private boolean appObjectsSomewhere = false;
  private boolean drawObjectsSomewhere = false;

  //private UndoStack undoStack = new UndoStack();
  //private EditOp lastUndo = null;

  private int gid = 0;
  private int nid = 0;
  private int eid = 0;

  private boolean editable = false;
  private boolean menuable = false;
  private boolean selectable = true;

  private Element current = null;

  // directed graph?
  private boolean directed = true;

  // strict graph?
  private boolean strict = false;

  // DrawPanes for this graph
  private Vector graphPanes = null;

  // PopUpCard for showing graph-specific properties
  private PopUpCard graphCard = null;

  // for mapping id to an element
  Hashtable id2element = null;

  // Grappa global attributes (apply to all elements)
  private Hashtable grattributes = null;

  private GraphicContext selection = new GraphicContext();
  private GraphicContext deletion = new GraphicContext();
  private Vector groupings = new Vector();


  // tables for graph default node, edge and graph attributes, which are
  // initialized below
  private static Hashtable sysdfltNodeAttributes  = new Hashtable(8);
  private static Hashtable sysdfltEdgeAttributes  = new Hashtable(7);
  private static Hashtable sysdfltGraphAttributes = new Hashtable(11);

  // graph default node, edge and graph attributes, these should be
  // consistent with the dot layout program, although it is not necessary.
  static {
    // node
    putAttribute(sysdfltNodeAttributes,"color","black");
    putAttribute(sysdfltNodeAttributes,"fontcolor","black");
    putAttribute(sysdfltNodeAttributes,"fontname","TimesRoman");
    putAttribute(sysdfltNodeAttributes,"fontsize","14");
    putAttribute(sysdfltNodeAttributes,"height","0.5");
    putAttribute(sysdfltNodeAttributes,"label","\\N");
    putAttribute(sysdfltNodeAttributes,"shape","ellipse");
    putAttribute(sysdfltNodeAttributes,"width","0.75");

    // edge
    putAttribute(sysdfltEdgeAttributes,"color","black");
    putAttribute(sysdfltEdgeAttributes,"dir","forward");
    putAttribute(sysdfltEdgeAttributes,"fontcolor","black");
    putAttribute(sysdfltEdgeAttributes,"fontname","TimesRoman");
    putAttribute(sysdfltEdgeAttributes,"fontsize","14");
    putAttribute(sysdfltEdgeAttributes,"minlen","1");
    putAttribute(sysdfltEdgeAttributes,"weight","1");

    // graph
    putAttribute(sysdfltGraphAttributes,"clusterrank","local");
    putAttribute(sysdfltGraphAttributes,"color","black");
    putAttribute(sysdfltGraphAttributes,"fontcolor","black");
    putAttribute(sysdfltGraphAttributes,"fontname","TimesRoman");
    putAttribute(sysdfltGraphAttributes,"fontsize","14");
    putAttribute(sysdfltGraphAttributes,"margin","0.5,0.5");
    putAttribute(sysdfltGraphAttributes,"mclimit","1.0");
    putAttribute(sysdfltGraphAttributes,"nodesep","0.25");
    putAttribute(sysdfltGraphAttributes,"orientation","portrait");
    putAttribute(sysdfltGraphAttributes,"rankdir","TB");
    putAttribute(sysdfltGraphAttributes,"ranksep","0.75");
  }

  private static void putAttribute(Hashtable table, String name, String value) {
    Attribute attr = new Attribute(name,value);
    attr.clearChanged();
    table.put(name,attr);
  }

  /**
   * Creates a new, empty Graph object.
   *
   * @param graphName the name of this graph.
   * @param directed use true if graph is to be a directed graph
   * @param strict use true if graph is a strict graph
   */
  public Graph(String graphName, boolean directed, boolean strict) {
    //super();
    initialize(graphName);

    this.directed = directed;
    this.strict = strict;

    // grappa attributes used for drawing
    setGrappaAttribute(Grappa.PKG_LOWER+"BackgroundColor","white");
    setGrappaAttribute(Grappa.PKG_LOWER+"SelectionColor","red");
    setGrappaAttribute(Grappa.PKG_LOWER+"DeletionColor","grey85");
    setGrappaAttribute(Grappa.PKG_LOWER+"GroupingColors","wheat1|slategrey1|rosybrown3|plum|powderblue|limegreen|goldenrod|deeppink|coral|bisque2");
  }

  /**
   * Creates a directed graph that is not strict
   * A convenience method equivalent to Graph(graphName,true,false).
   *
   * @param graphName the name of this graph.
   * @see Graph#Graph(java.lang.String, boolean, boolean)
   */
  public Graph(String graphName) {
    this(graphName,true,false);
  }

  private void initialize(String graphName) {
    eid = nid = gid = 0;
    resetBBox();

    if(id2element != null) {
      id2element.clear();
    }

    setGraph(this);
    setSubgraph(null);
    setIdKey(Grappa.SUBGRAPH);
    addIdMapping(this);
    setName(graphName);

    Attribute attr = null;
    Enumeration enum = getGlobalAttributePairs(Grappa.NODE);
    while(enum.hasMoreElements()) {
      setNodeAttribute((Attribute)enum.nextElement());
    }
    enum = getGlobalAttributePairs(Grappa.EDGE);
    while(enum.hasMoreElements()) {
      setEdgeAttribute((Attribute)enum.nextElement());
    }
    enum = getGlobalAttributePairs(Grappa.SUBGRAPH);
    while(enum.hasMoreElements()) {
      setAttribute((Attribute)enum.nextElement());
    }
    setDrawObjects();
    setAppObjects();
  }

  /**
   * Used by Grappa to keep track of overall instantiation status
   */
  void setDrawObjectsSomewhere(boolean mode) {
    drawObjectsSomewhere = mode;
  }

  /**
   * Indicates that there are DrawObjects somewhere in the graph.
   *
   * @return true, if there are DrawObjects in the graph.
   */
  public boolean hasDrawObjectsSomewhere() {
    return drawObjectsSomewhere;
  }

  /**
   * Indicates that there are AppObjects somewhere in the graph.
   *
   * @return true, if there are AppObjects in the graph.
   */
  public boolean hasAppObjectsSomewhere() {
    return appObjectsSomewhere;
  }

  /**
   * Used by Grappa to keep track of overall instantiation status
   */
  void setAppObjectsSomewhere(boolean mode) {
    appObjectsSomewhere = mode;
  }


  /**
   * Gets Grappa default attribute.
   *
   * @param key the search key for the corresponding attribute.
   * @exception IllegalArgumentException whenever the key is null
   * @return the value of the matching Grappa default attribute or null.
   */
  public Attribute getGrappaAttribute(String key) throws IllegalArgumentException {
    if(key == null) {
      throw new IllegalArgumentException("key value cannot be null");
    }
    if(grattributes == null) return null;
    return ((Attribute)(grattributes.get(key)));
  }

  /**
   * Sets a Grappa package attribute.  A Grappa package attribute is one
   * specific to Grappa (for example, a display color) rather than an
   * attribute that relates to a graph.
   *
   * @param key the search key for the corresponding attribute.
   * @exception IllegalArgumentException whenever the key is not prefixed by Grappa.PKG_LOWER
   * @return the previous value of the matching Grappa default attribute or null.
   * @see Grappa#PKG_LOWER
   */
  public String setGrappaAttribute(String key, String value) throws IllegalArgumentException {
    if(grattributes == null) {
      grattributes = new Hashtable(3);
    }
    // the get also tests if key is null
    Attribute oldValue = getGrappaAttribute(key);
    if(oldValue == null) {
      if(!validGrappaAttributeKey(key)) {
	throw new IllegalArgumentException(Grappa.PKG_UPLOW + " attribute key must use \"" + Grappa.PKG_LOWER + "\" as a prefix");
      }
      oldValue = new Attribute(key,value);
      if(hasDrawObjects()) {
	GraphEnumeration enum = elements();
	DrawObject drwobj = null;
	while(enum.hasMoreElements()) {
	  drwobj = ((Element)enum.nextElement()).getDrawObject();
	  drwobj.attrOfInterest(key);
	  oldValue.addObserver(drwobj);
	}
      }
      grattributes.put(key,oldValue);
      return null;
    }
    return oldValue.getValue();
  }

  /**
   * Get an enumeration of the Grappa package attribute keys.
   *
   * @return an Enumeration of Attribute objects
   */
  public Enumeration getGrappaAttributeKeys() {
    if(grattributes == null) {
      return Grappa.emptyEnumeration.elements();
    }
    return grattributes.keys();
  }

  /**
   * Check if the given key has a format consistent with Grappa package
   * attribute keys.  A Grappa package key starts with Grappa.PKG_LOWER.
   *
   * @param key the key to validate
   * @return true if the supplied key could serve as a Grappa package attribute key.
   * @see Grappa#PKG_LOWER
   */
  public static boolean validGrappaAttributeKey(String key) {
    return (key != null && key.startsWith(Grappa.PKG_LOWER) && key.length() > Grappa.PKG_LOWER.length());
  }

  /**
   * Gets a graph default attribute. A graph default attribute determines
   * basic graph characteristics initially (e.g., node shape).
   *
   * @param type indicates attribute type.
   * @param key the search key for the corresponding attribute.
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return the value of the matching graph default attribute or null.
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public Attribute getGlobalAttribute(int type, String key) throws IllegalArgumentException {
    switch(type) {
    case Grappa.NODE:
      return((Attribute)sysdfltNodeAttributes.get(key));
    case Grappa.EDGE:
      return((Attribute)sysdfltEdgeAttributes.get(key));
    case Grappa.SUBGRAPH:
      return((Attribute)sysdfltGraphAttributes.get(key));
    }
    throw new IllegalArgumentException("specified type must be NODE, EDGE or SUBGRAPH");
  }

  /**
   * Gets an enumeration of the specified graph default attribute keys
   *
   * @param type indicates attribute type.
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return an Enumeration of String objects
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public Enumeration getGlobalAttributeKeys(int type) throws IllegalArgumentException {
    switch(type) {
    case Grappa.NODE:
      return(sysdfltNodeAttributes.keys());
    case Grappa.EDGE:
      return(sysdfltEdgeAttributes.keys());
    case Grappa.SUBGRAPH:
      return(sysdfltGraphAttributes.keys());
    }
    throw new IllegalArgumentException("specified type must be NODE, EDGE or SUBGRAPH");
  }

  /**
   * Gets an enumeration of the specified graph default attributes
   *
   * @param type indicates attribute type.
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return an Enumeration of Attribute objects
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public Enumeration getGlobalAttributePairs(int type) throws IllegalArgumentException {
    switch(type) {
    case Grappa.NODE:
      return(sysdfltNodeAttributes.elements());
    case Grappa.EDGE:
      return(sysdfltEdgeAttributes.elements());
    case Grappa.SUBGRAPH:
      return(sysdfltGraphAttributes.elements());
    }
    throw new IllegalArgumentException("specified type must be NODE, EDGE or SUBGRAPH");
  }

  /**
   * Get a count of the graph default attributes of a particular type.
   *
   * @param type indicates attribute type.
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return a count of the specified graph default attributes
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public int getGlobalAttributeSize(int type) throws IllegalArgumentException {
    switch(type) {
    case Grappa.NODE:
      return(sysdfltNodeAttributes.size());
    case Grappa.EDGE:
      return(sysdfltEdgeAttributes.size());
    case Grappa.SUBGRAPH:
      return(sysdfltGraphAttributes.size());
    }
    throw new IllegalArgumentException("specified type must be NODE, EDGE or SUBGRAPH");
  }

  /**
   * Instantiates both the application and drawing components of each of
   * the graph elements within the graph.
   * 
   * @deprecated Replaced by <code>buildObjects()</code>.
   * @see Graph#buildObjects()
   *
   * @exception InstantiationException occurs if there is a problem instantiating
   * the AppObjects or DrawObjects for this graph.
   */
  public void instantiateGraph() throws InstantiationException {
    buildObjects();
  }

  /**
   * Add id to element lookup table
   * (used in setId method)
   *
   * @param elem the element associated with the id
   */
  Element addIdMapping(Element elem) {
    if(elem == null) {
      return null;
    }
    if(id2element == null) {
      id2element = new Hashtable();
    }
    return (Element)id2element.put(elem.getIdKey(),elem);
  }

  /**
   * Creates a id key given a type and id number.
   *
   * @param type one of Grappa.NODE, Grappa.EDGE or Grappa.SUBGRAPH
   * @param id an id number
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return an idKey for an element
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  static Long idMapKey(int type, int id) throws IllegalArgumentException {
    long value = (long)(id);
    int tval = (type&(Grappa.NODE|Grappa.EDGE|Grappa.SUBGRAPH));
    if(tval == 0) {
      throw new IllegalArgumentException("supplied type does not specify node, edge or subgraph");
    }
    value = (value << Grappa.TYPES_SHIFT) | (type&(Grappa.NODE|Grappa.EDGE|Grappa.SUBGRAPH));
    return new Long(value);
  }

  /**
   * Get the type of the id key.
   *
   * @param idKey the id key to examine
   * @return the type of the id key (Grappa.NODE, Grappa.EDGE, Grappa.SUBGRAPH)
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  static int idKeyType(Long idKey) {
    long value = idKey.longValue();
    return (int)(value&(Grappa.NODE|Grappa.EDGE|Grappa.SUBGRAPH));
  }

  /**
   * Get the type of the id key.
   *
   * @param idKey the id key to examine
   * @return the type of the id key (Grappa.NODE, Grappa.EDGE, Grappa.SUBGRAPH)
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  static int idKeyId(Long idKey) {
    long value = idKey.longValue();
    return (int)(value>>>Grappa.TYPES_SHIFT);
  }

  /**
   * Get the element associated with an id key
   *
   * @param idKey the id key of the element to be located
   * @return the Element object matching the id key or null
   */
  Element element4Id(Long idKey) {
    if(id2element == null) {
      return null;
    }
    return (Element)id2element.get(idKey);
  }

  /**
   * Remove id2element dictionary element
   *
   * @param id the id number of the element entry to be removed
   */
  void removeIdMapping(Element elem) {
    if(id2element != null && elem != null) {
      id2element.remove(elem.getIdKey());
    }
  }

  /**
   * Output graph to specified Writer.
   *
   * @param output the Writer for writing
   */
  public void printGraph(Writer output) {
    PrintWriter out = null;
    
    if(output instanceof PrintWriter) {
      out = (PrintWriter)output;
    } else {
      out = new PrintWriter(output);
    }
    getGraph().printSubgraph(out);
    out.flush();
  }

  /**
   * Output graph to specified OutputStream.
   * A convenience method to accomodate the OuputStreams easily.
   *
   * @param output the OutputStream for writing
   */
  public void printGraph(OutputStream output) {
    printGraph(new PrintWriter(output));
  }

  /**
   * Get the next id number for the specified type and increment the counter.
   *
   * @param type type of id number to return
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return the next sequential id number (counter is incremented).
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  int nextId(int type) throws IllegalArgumentException {
    switch(type) {
    case Grappa.NODE:
      return(nid++);
    case Grappa.EDGE:
      return(eid++);
    case Grappa.SUBGRAPH:
      return(gid++);
    }
    throw new IllegalArgumentException("Type ("+type+") is not recognized.");
  }

  /**
   * Get the next id number for the specified type, but do not increment the counter.
   *
   * @param type type of id number to return
   * @exception IllegalArgumentException whenever the specified type is not valid
   * @return the next sequential id number (counter is not incremented).
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public int getId(int type) throws IllegalArgumentException {
    switch(type) {
    case Grappa.NODE:
      return(nid);
    case Grappa.EDGE:
      return(eid);
    case Grappa.SUBGRAPH:
      return(gid);
    }
    throw new IllegalArgumentException("Type ("+type+") is not recognized.");
  }

  /**
   * Get the current indent string.
   *
   * @return the current indent string.
   */
  public String getIndent() {
    if(indent == null) {
      indent = new StringBuffer(5 * INDENT_STRING.length());
    }
    return(indent.toString());
  }

  /**
   * Increase the indent string by appending INDENT_STRING.
   *
   * @see Graph#INDENT_STRING
   */
  public void incrementIndent() {
    if(indent == null) {
      indent = new StringBuffer(5 * INDENT_STRING.length());
    }
    indent.append(INDENT_STRING);
  }

  /**
   * Decrease the indent string by removing one INDENT_STRING.
   *
   * @see Graph#INDENT_STRING
   */
  public void decrementIndent() {
    int len = indent.length();

    if(len == 0) return;

    if(len < INDENT_STRING.length()) {
      indent.setLength(0);
    } else {
      indent.setLength(len - INDENT_STRING.length());
    }
  }

  /**
   * Check if the graph is directed.
   *
   * @return true if graph is a directed graph
   */
  public boolean isDirected() {
    return(directed);
  }

  /**
   * Check if the graph is strict (i.e., no self-loops).
   *
   * @return true if the graph is strict
   */
  public boolean isStrict() {
    return(strict);
  }

  //TODO find out dot options to fill or outline graph also orientation.

  /**
   * Reset this graph by removing all its elements and re-initiailizing
   * its internal variables.
   */
  public void reset() {
    String graphName = getName();
    delete();
    initialize(graphName);
  }

  /**
   * Reset this graph by removing all its elements and re-initiailizing
   * its internal variables and possibly changing its name, directedness
   * and strictness.
   */
  public void reset(String graphName, boolean directed, boolean strict) {
    name = new String(graphName);
    reset();
    this.directed = directed;
    this.strict = strict;
  }

  /**
   * Check if this graph is interactively editable (i.e., through mouse events).
   *
   * @return true if the graph can be edited interactively.
   */
  public boolean isEditable() {
    return editable;
  }

  /**
   * Set the editability of the graph.
   *
   * @param mode true to turn on editability.
   * @return previous value
   * @see Graph#isEditable()
   */
  public boolean setEditable(boolean mode) {
    boolean wasMode = editable;
    editable = mode;
    return wasMode;
  }

  /**
   * Check if graph elements are interactively selectable  (i.e., through mouse events).
   *
   * @return true if graph elements can be selected interactively.
   */
  public boolean isSelectable() {
    return selectable;
  }

  /**
   * Set the selectability of the graph.
   *
   * @param mode true to turn on selectability.
   * @return previous value
   * @see Graph#isSelectable()
   */
  public boolean setSelectable(boolean mode) {
    boolean wasMode = selectable;
    selectable = mode;
    return wasMode;
  }

  /**
   * Check if an element-specific menu is available interactively (i.e., through mouse events).
   *
   * @return true if an element-specific menu is available
   */
  public boolean isMenuable() {
    return menuable;
  }

  /**
   * Set whether element-specific menus are to be available interactively.
   *
   * @param mode true to turn on element-specific-menus.
   * @return previous value
   * @see Graph#isMenuable()
   */
  public boolean setMenuable(boolean mode) {
    boolean wasMode = menuable;
    menuable = mode;
    return wasMode;
  }

  /**
   * Set the PrintWriter for error messages.
   *
   * @param errWriter the PrintWriter to use for error messages.
   * @return the previous PrintWriter used for error messages.
   * @see java.io.PrintWriter
   */
  public PrintWriter setErrorWriter(PrintWriter errWriter) {
    PrintWriter oldWriter = this.errWriter;
    this.errWriter = errWriter;
    return oldWriter;
  }

  /**
   * Get the current PrintWriter used for error messages.
   *
   * @return the current PrintWriter used for error messages.
   * @see java.io.PrintWriter
   */
  public PrintWriter getErrorWriter() {
    return errWriter;
  }

  /**
   * Print the supplied message to the error output.
   * Nothing happens if the error output is set to null.
   *
   * @param msg the message to print on the error output.
   * @see Graph#setErrorWriter(java.io.PrintWriter)
   */
  public void printError(String msg) {
    printError(msg,null);
  }

  /**
   * Print the supplied message and exception information to the error output.
   * Nothing happens if the error output is set to null.
   *
   * @param msg the message to print on the error output.
   * @param ex if supplied, the stack trace associated with this exception is also printed.
   * @see Graph#setErrorWriter(java.io.PrintWriter)
   */
  public void printError(String msg, Exception ex) {
    if(getErrorWriter() == null) return;
    getErrorWriter().println("ERROR: " + msg);
    if(ex != null) ex.printStackTrace(getErrorWriter());
    getErrorWriter().flush();
  }

  /**
   * Get an enumeration of the panes upon which given graph is displayed.
   *
   * @return an Enumeration of DrawPane objects
   */
  public Enumeration getGraphPanes() {
    if(graphPanes == null) {
      return Grappa.emptyEnumeration.elements();
    }
    return graphPanes.elements();
  }

  /**
   * Adds a DrawPane to the graph's list of DrawPanes
   * if it is not already in the list.
   *
   * @param pane the DrawPane to be added
   */
  public void addDrawPane(DrawPane pane) {
    if(pane == null) return;
    if(graphPanes == null) {
      graphPanes = new Vector(2,2);
    }
    if(!graphPanes.contains(pane)) {
      graphPanes.addElement(pane);
      if(!hasDrawObjects()) {
	try {
	  buildDrawObjects();
	} catch(InstantiationException iex) {
	  graphPanes.removeElement(pane);
	  Grappa.displayException(iex,"DrawPane not added to graph");
	  return;
	}
      }
      GraphEnumeration enum = elements();
      DrawObject drwObj = null;
      while(enum.hasMoreElements()) {
	drwObj = ((Element)enum.nextElement()).getDrawObject();
	drwObj.createPeer(pane);
      }
    }
  }

  /**
   * Removes a DrawPane from the graph's list of DrawPanes.
   *
   * @param pane the DrawPane to be removed
   */
  public void removeDrawPane(DrawPane pane) {
    if(pane == null) return;
    if(graphPanes == null) return;
    if(!graphPanes.contains(pane)) return;
    graphPanes.removeElement(pane);
    pane.deleteObservers();
    if(hasDrawObjects()) {
      GraphEnumeration enum = elements();
      DrawObject drwObj = null;
      while(enum.hasMoreElements()) {
	drwObj = ((Element)enum.nextElement()).getDrawObject();
	drwObj.deletePeer(pane);
      }
    }
  }

  /**
   * Get the graph properties card.  The graph properities card is used for
   * displaying information such as local and default
   * attribute values for a specific element. A single card is used
   * (and re-used) for all elements in the graph.  Naturally, the content
   * displayed varies by element.
   *
   * @return the graph properties card for this graph
   */
  public PopUpCard getGraphPropertiesCard() {
    return graphCard;
  }

  /**
   * Sets the value of the graph properties card.
   *
   * @param newCard the new card to use as the graph properties card
   * @return the previous graph properties card
   */
  public PopUpCard setAsGraphPropertiesCard(PopUpCard newCard) {
    PopUpCard oldCard = graphCard;
    graphCard = newCard;
    return graphCard;
  }

  /**
   * Set the GraphicContext to use when an element is selected.
   *
   * @param context the GraphicContext to use for selection
   * @return the previous GraphicContext used for selection
   */
  public GraphicContext setSelectionContext(GraphicContext context) {
    GraphicContext oldValue = selection;
    if(context != null) selection = (GraphicContext)context.clone();
    return oldValue;
  }

  /**
   * Get the GraphicContext to use when an element is selected.
   *
   * @return the current GraphicContext used for selection
   */
  public GraphicContext getSelectionContext() {
    return selection;
  }

  /**
   * Set the GraphicContext to use when an element is to be deleted.
   *
   * @param context the GraphicContext to use to indicate a pending deletion
   * @return the previous GraphicContext used to indicate a pending deletion
   */
  public GraphicContext setDeletionContext(GraphicContext context) {
    GraphicContext oldValue = deletion;
    if(context != null) deletion = (GraphicContext)context.clone();
    return oldValue;
  }

  /**
   * Get the GraphicContext to use when an element is to be deleted.
   *
   * @return the current GraphicContext used to indicate a pending deletion
   */
  public GraphicContext getDeletionContext() {
    return deletion;
  }

  /**
   * Set the list of GraphicContexts to use when indicating groupings of elements.
   *
   * @param contexts the Vector of GraphicContexts to use when indicating groupings of elements
   * @return the previous Vector of GraphicContexts used when indicating groupings of elements
   */
  public Vector setGroupingContexts(Vector contexts) {
    Vector oldValue = groupings;
    if(contexts != null) {
      groupings = new Vector(contexts.size());
      for(int i = 0; i < contexts.size(); i++) {
	groupings.addElement(((GraphicContext)(contexts.elementAt(i))).clone());
      }
    }
    return oldValue;
  }

  /**
   * Add a GraphicContext to the list of GraphicContexts used when
   * indicating groupings of elements.
   *
   * @param gc the GraphicContext to be added
   * @return the GraphicContext that was added (which is a clone of the supplied context)
   */
  public GraphicContext addGroupingContext(GraphicContext gc) {
    GraphicContext newValue = (GraphicContext)gc.clone();
    groupings.addElement(newValue);
    return newValue;
  }

  /**
   * Get the size of the list of GraphicContexts used to indicate groupings
   * of elements.
   *
   * @return the size of the grouping context list
   */
  public int getGroupingContextsSize() {
    return groupings.size();
  }

  /**
   * Get a specific GraphicContext from the list of 
   * GraphicContexts used to indicate groupings
   * of elements.
   *
   * @param slot the slot number of the element in the list to return
   * @return the requested GraphicContext or null if there is no such list element
   */
  public GraphicContext getGroupingContextAt(int slot) {
    if(slot < 0 || slot >= getGroupingContextsSize()) {
      return null;
    }
    return ((GraphicContext)groupings.elementAt(slot));
  }

  /**
   * Set the supplied element as the current element of the graph.
   * A call to AppObject.setCurrent(boolean) is triggered also.
   *
   * @param newValue the element to set as the current element
   * @return the element that had been marked as the current element
   * @see AppObject#setCurrent(boolean)
   */
  public Element setCurrent(Element newValue) {
    Element oldValue = current;
    current = newValue;
    if(oldValue != null && oldValue.getAppObject() != null) {
      oldValue.getAppObject().setCurrent(false);
    }
    if(current != null && current.getAppObject() != null) {
      current.getAppObject().setCurrent(true);
    }
    return oldValue;
  }

  /**
   * Get the element marked as the current element.
   *
   * @return the graph element marked as the current element
   */
  public Element getCurrent() {
    return current;
  }


  /**
   * Replace all aspects of the current graph with the supplied graph.
   *
   * @param demon the graph taking possession of this graph.
   */
  public void replaceContents(Graph demon) {
    reset(demon.getName(),demon.isDirected(),demon.isStrict());
    nid = demon.getId(Grappa.NODE);
    eid = demon.getId(Grappa.EDGE);
    gid = demon.getId(Grappa.SUBGRAPH);
    editable = demon.isEditable();
    menuable = demon.isMenuable();
    selectable = demon.isSelectable();
    possessSubgraph((Subgraph)demon);
    addIdMapping(this);
  }
}

