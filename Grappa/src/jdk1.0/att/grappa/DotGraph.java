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
 * This class describes the overall dot graph entity.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class DotGraph implements Shape
{
  /**
   * The string used for indentation when printing out the graph.
   */
  public final static String indentString = "  ";
  private StringBuffer indent = null;
  private PrintStream errStream = null;

  //private UndoStack undoStack = new UndoStack();
  //private EditOp lastUndo = null;

  private int gid = 0;
  private int nid = 0;
  private int eid = 0;

  private boolean editable = false;
  private boolean menuable = false;
  private boolean selectable = true;

  private GraphicContext gc = null;

  private boolean boundsFlag = true;

  // panes where this graph is displayed
  private Vector panes = new Vector(2,2);

  // the file
  private String theFile = null;

  // the root graph
  private DotSubgraph root = null;

  // type
  private String type = null;

  // name
  private String name;

  // for displaying properties (used in AppObject)
  private PopUpCard propertiesCard = null;

  /**
   * Initial capacity of the element dictionary.
   *
   * @see Hashtable
   */
  public final static int initialElementsCapacity = 16;
  /**
   * Size increment of the element dictionary.
   *
   * @see Hashtable
   */
  public final static int elementsIncrement = 32;

  // for mapping id to an element
  protected Hashtable id2element = null;


  // default node, edge and graph attributes, these should be consistent
  // with the dot layout program, although it is not necessary.
  private static Hashtable defaultNodeAttributes  = new Hashtable(8,5);
  private static Hashtable defaultEdgeAttributes  = new Hashtable(7,5);
  private static Hashtable defaultGraphAttributes = new Hashtable(11,5);

  private MyRectangle BBox = null;
  private MyRectangle baseBBox = null;
  private MyRectangle cropBBox = null;

  static {
    // node
    defaultNodeAttributes.put("color",new Attribute("color","black"));
    defaultNodeAttributes.put("fontcolor",new Attribute("fontcolor","black"));
    defaultNodeAttributes.put("fontname",new Attribute("fontname","TimesRoman"));
    defaultNodeAttributes.put("fontsize",new Attribute("fontsize","14"));
    defaultNodeAttributes.put("height",new Attribute("height","0.5"));
    defaultNodeAttributes.put("label",new Attribute("label","\\N"));
    defaultNodeAttributes.put("shape",new Attribute("shape","ellipse"));
    defaultNodeAttributes.put("width",new Attribute("width","0.75"));

    // edge
    defaultEdgeAttributes.put("color",new Attribute("color","black"));
    defaultEdgeAttributes.put("dir",new Attribute("dir","forward"));
    defaultEdgeAttributes.put("fontcolor",new Attribute("fontcolor","black"));
    defaultEdgeAttributes.put("fontname",new Attribute("fontname","TimesRoman"));
    defaultEdgeAttributes.put("fontsize",new Attribute("fontsize","14"));
    defaultEdgeAttributes.put("minlen",new Attribute("minlen","1"));
    defaultEdgeAttributes.put("weight",new Attribute("weight","1"));

    // graph
    defaultGraphAttributes.put("clusterrank",new Attribute("clusterrank","local"));
    defaultGraphAttributes.put("color",new Attribute("color","black"));
    defaultGraphAttributes.put("fontcolor",new Attribute("fontcolor","black"));
    defaultGraphAttributes.put("fontname",new Attribute("fontname","TimesRoman"));
    defaultGraphAttributes.put("fontsize",new Attribute("fontsize","14"));
    defaultGraphAttributes.put("margin",new Attribute("margin","0.5,0.5"));
    defaultGraphAttributes.put("mclimit",new Attribute("mclimit","1.0"));
    defaultGraphAttributes.put("nodesep",new Attribute("nodesep","0.25"));
    defaultGraphAttributes.put("orientation",new Attribute("orientation","portrait"));
    defaultGraphAttributes.put("rankdir",new Attribute("rankdir","TB"));
    defaultGraphAttributes.put("ranksep",new Attribute("ranksep","0.75"));
  }

  /**
   * Gets system default node attribute.
   *
   * @param key the search key for the corresponding attribute.
   * @return the value of the default attribute matching the key or null.
   */
  public static Attribute getNodeAttribute(String key) {
    return((Attribute)defaultNodeAttributes.get(key));
  }

  /**
   * Gets an enumeration of the default node attributes
   *
   * @return the requested enumeration or null if there are no node attributes.
   */
  public Enumeration getDefaultNodeAttributePairs() {
    if(defaultNodeAttributes == null) return(null);
    return(defaultNodeAttributes.elements());
  }

  /**
   * Gets an enumeration of the default edge attributes
   *
   * @return the requested enumeration or null if there are no edge attributes.
   */
  public Enumeration getDefaultEdgeAttributePairs() {
    if(defaultEdgeAttributes == null) return(null);
    return(defaultEdgeAttributes.elements());
  }

  /**
   * Gets an enumeration of the default graph attributes
   *
   * @return the requested enumeration or null if there are no graph attributes.
   */
  public Enumeration getDefaultGraphAttributePairs() {
    if(defaultGraphAttributes == null) return(null);
    return(defaultGraphAttributes.elements());
  }

  /**
   * Gets system default edge attribute.
   *
   * @param key the search key for the corresponding attribute.
   * @return the value of the default attribute matching the key or null.
   */
  public static Attribute getEdgeAttribute(String key) {
    return((Attribute)defaultEdgeAttributes.get(key));
  }

  /**
   * Gets system default graph attribute.
   *
   * @param key the search key for the corresponding attribute.
   * @return the value of the default attribute matching the key or null.
   */
  public static Attribute getGraphAttribute(String key) {
    return((Attribute)defaultGraphAttributes.get(key));
  }

  /**
   * @param graphName the name of this graph.
   */
  public DotGraph(String graphName) {
    name = new String(graphName);
    root = new DotSubgraph(this,name);
  }

  /**
   * @param graphName the name of this graph.
   * @param graphType the type of this graph (e.g., digraph).
   */
  public DotGraph(String graphName, String graphType) throws IllegalArgumentException {
    name = new String(graphName);
    root = new DotSubgraph(this,name);
    setGraphType(graphType);
  }

  /**
   * Sets the graph type. Recognized types are: digraph, graph, strict
   * digraph and strict graph.
   *
   * @param graphType the type of this graph.
   */
  public void setGraphType(String graphType) throws IllegalArgumentException {
    if(graphType.equalsIgnoreCase("digraph")) {
      type = Grappa.GRAPH_DIGRAPH;
    } else if(graphType.equalsIgnoreCase("graph")) {
      type = Grappa.GRAPH_GRAPH;
    } else if(graphType.equalsIgnoreCase("strict digraph")) {
      type = Grappa.GRAPH_STRICT_DIGRAPH;
    } else if(graphType.equalsIgnoreCase("strict graph")) {
      type = Grappa.GRAPH_STRICT_GRAPH;
    } else {
      throw new IllegalArgumentException("unknown graph type `" + graphType + "'");
    }
  }

  /*
  // undo buffer
  private boolean undoAble;
  private DotNodes undoNodes = new DotNodes(5, 5);
  private DotEdges undoEdges = new DotEdges(5, 5);

  public DotGraph copy() {
    DotGraph new_graph = new DotGraph(name);
    new_graph.theFile = theFile;
    new_graph.graph_attrs = graph_attrs.copy();
    new_graph.node_attrs = graph_attrs.copy();
    new_graph.edge_attrs = graph_attrs.copy();

    for (int i = 0; i < nodedict.size(); i++) {
      String nid = (String) nodedict.elementAt(i);
      new_graph.nodedict.addElement(nid);
    }

    for (int i = 0; i < edgedict.size(); i++) {
      String nid = (String) edgedict.elementAt(i);
      new_graph.edgedict.addElement(nid);
    }

    new_graph.nodes = nodes.copy();
    new_graph.edges = edges.copy();
    for (int i = 0; i < subgraphs.size(); i++) {
      DotGraph subGraph = (DotGraph) subgraphs.elementAt(i);
      DotGraph new_subGraph = subGraph.copy();
      new_graph.insertSubgraph(new_subGraph);
    }

    return new_graph;
  }

  public void setGraphToElements() {
    for (int i = 0; i < nodedict.size(); i++) {
      DotNode node = nodedict.elementAt(i);
      node.setGraph(this);
    }

    for (int i = 0; i < edgedict.size(); i++) {
      DotEdge edge = edgedict.elementAt(i);
      edge.setGraph(this);
    }

    for (int i = 0; i < graphdict.size(); i++) {
      DotSubgraph subgraph = (DotSubgraph) graphdict.elementAt(i);
      subgraph.setGraphToElements();
    }
  }
  */


  /**
   * Instantiates the physical representation of each graph element
   * within the graph.
   */
  public void instantiateGraph() throws InstantiationException {
    root.instantiateGraph();
  }

  /**
   * Add id to element lookup table
   * setId method)
   *
   * @param elem the element associated with the id
   */
  public DotElement addIdMapping(DotElement elem) {
    if(elem == null) {
      return null;
    }
    if(id2element == null) {
      id2element = new Hashtable(initialElementsCapacity,elementsIncrement);
    }
    return (DotElement)id2element.put(elem.getIdObj(),elem);
  }

  /**
   * Return element associated with id
   *
   * @param id the id number of the element to be located
   */
  protected DotElement element4Id(Integer id) {
    if(id2element == null) {
      return null;
    }
    return (DotElement)id2element.get(id);
  }

  /**
   * Remove id2element dictionary element
   *
   * @param id the id number of the element entry to be removed
   */
  protected DotElement removeIdMapping(Integer id) {
    DotElement elem = null;

    if(id2element != null) {
      elem = (DotElement)id2element.remove(id);
    }
    return elem;
  }

  /**
   * Write graph to specified Writer
   *
   * @param output the Writer for writing
   */
  public void printGraph(OutputStream output) {
    PrintStream out = null;
    
    if(output instanceof PrintStream) {
      out = (PrintStream)output;
    } else {
      out = new PrintStream(output);
    }
    getRoot().printSubgraph(out);
    out.flush();
  }

  /**
   * @return the file name for storing the graph, or null if not set.
   */
  public String getFile() {
    if (theFile == null) {
      return "";
    }
    return theFile;
  }

  /**
   * Set the file name for storing the graph.
   *
   * @param fileName the new file name.
   */
  public void setFile(String fileName) {
    theFile = fileName;
  }

  /**
   * @return the name of the graph.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the next sequential node id number (counter is incremented).
   */
  public int nextNodeId() {
    return(nid++);
  }

  /**
   * @return the next sequential node id number (counter is not incremented).
   */
  public int nodeId() {
    return(nid);
  }

  /**
   * @return the next sequential edge id number (counter is incremented).
   */
  public int nextEdgeId() {
    return(eid++);
  }

  /**
   * @return the next sequential edge id number (counter is not incremented).
   */
  public int edgeId() {
    return(eid);
  }

  /**
   * @return the next sequential subgraph id number (counter is incremented).
   */
  public int nextGraphId() {
    return(gid++);
  }

  /**
   * @return the next sequential subgraph id number (counter is not incremented).
   */
  public int graphId() {
    return(gid);
  }

  /**
   * @return the current indent string.
   */
  public String getIndent() {
    if(indent == null) {
      indent = new StringBuffer(5 * indentString.length());
    }
    return(indent.toString());
  }

  /**
   * String rendition of the graph.
   *
   * @return the name of the graph, quoted as needed.
   */
  public String toString() {
    return("\"" + getName() + "\"");
  }

  /**
   * Increase the indent string by the indentString.
   *
   * @see DotGraph#indentString
   */
  public void incrementIndent() {
    if(indent == null) {
      indent = new StringBuffer(5 * indentString.length());
    }
    indent.append(indentString);
  }

  /**
   * Decrease the indent string by the indentString.
   *
   * @see DotGraph#indentString
   */
  public void decrementIndent() {
    int len = indent.length();

    if(len == 0) return;

    if(len < indentString.length()) {
      indent.setLength(0);
    } else {
      indent.setLength(len - indentString.length());
    }
  }

  /**
   * Return the root subgraph, which contains the entire graph.
   *
   * @return the root subgraph.
   */
  public DotSubgraph getRoot() {
    return(root);
  }

  /**
   * Return the type of the graph.
   *
   * @return the graph type.
   */
  public String getType() {
    return(type);
  }

  /**
   * @return a pane element
   */
  public DrawPane getPaneAt(int idx) {
    if(idx < 0 || panes.size() < (idx+1)) return null;
    return ((DrawPane)panes.elementAt(idx));
  }

  /**
   * @return an enumeration of the panes upon which this graph is displayed.
   */
  public Enumeration getPanes() {
    return panes.elements();
  }

  /**
   * Add pane to list.
   */
  public void addPane(DrawPane pane) {
    if(!panes.contains(pane)) {
      panes.addElement(pane);
    }
  }

  //TODO find out dot options to fill or outline graph also orientation.

  /**
   * @return the Graphic Context of the graph.
   */
  public GraphicContext getGC() {
    if(gc == null) {
      gc = new GraphicContext();
    }
    return gc;
  }

  /**
   * Adds given rectangle to graph bounding box.
   * A null argument just returns the current BBox.
   *
   * @param bbox bounding box to be added to graph bounding box.
   * @return the augmented graph bounding box.
   */
  public MyRectangle addToBBox(MyRectangle bbox) {
    if(bbox == null) return BBox;
    bbox.setSize(bbox.width+1,bbox.height+1);
    if(BBox == null) {
      BBox = new MyRectangle(bbox.x,bbox.y,bbox.width,bbox.height);
    } else {
      BBox.add(bbox);
    }
    return BBox;
  }

  public MyRectangle getBounds() {
    MyRectangle bbox = BBox;
    if(boundsFlag || BBox == null) bbox = computeBBox();
    if(getCropBBox() != null) {
      bbox = bbox.intersection(getCropBBox());
    }
    return bbox;
  }

  public MyRectangle computeBBox() {
    if(!boundsFlag && BBox != null) return BBox;
    BBox = getBaseBBox();
    Enumeration elems = getRoot().elements();
    DotElement elem;
    DrawObject drawable;
    MyRectangle ebbox;
    while(elems.hasMoreElements()) {
      elem = (DotElement)elems.nextElement();
      drawable = elem.getDrawObject();
      if(drawable == null) {
	continue;
      }
      ebbox = drawable.getBounds();
      if(ebbox == null) {
	continue;
      }
      addToBBox(ebbox);
    }
    boundsFlag = false;
    if(BBox == null) BBox = new MyRectangle(0,0,1,1);
    return BBox;
  }

  public DotElement findContainingElement(MyPoint pt) {
    Enumeration elems = getRoot().elements();
    DotElement elem;
    AppObject appobj;

    while(elems.hasMoreElements()) {
      elem = (DotElement)elems.nextElement();
      appobj = elem.getAppObject();
      if(appobj == null || !appobj.isAccessible()) continue;
      if(elem.getDrawObject().PointInObject(pt)) {
	return elem;
      }
    }
    return null;
  }
  
  public void setBoundsFlag(boolean mode) {
    boundsFlag = mode;
  }
  
  public boolean getBoundsFlag() {
    return boundsFlag;
  }


  public void drawGraph() {
    if(panes.size() == 0) {
      return;
    }
    if(getRoot() == null) {
      return;
    }
    /*
     * DrawPane pnl = null;
     * MyRectangle bnds = getBounds();
     * for(int i = 0; i < panes.size(); i++) {
     * pnl = (DrawPane)panes.elementAt(i);
     * pnl.getCanvas().unionSizes(bnds.width,bnds.height);
     * }
     */
    //BBox = null;
    clearGraph();
    Enumeration elems = getRoot().elements();
    DotElement elem = null;
    while(elems.hasMoreElements()) {
      elem = (DotElement)elems.nextElement();
      elem.getDrawObject().draw(null,null,false);
    }
    paintGraph();
  }

  public void paintGraph() {
    DrawPane pnl = null;

    for(int i = 0; i < panes.size(); i++) {
      pnl = (DrawPane)panes.elementAt(i);
      pnl.paintCanvas();
    }
  }

  public void clearGraph() {
    DrawPane pnl = null;

    for(int i = 0; i < panes.size(); i++) {
      pnl = (DrawPane)panes.elementAt(i);
      pnl.clearCanvas();
    }
  }

  public boolean isLR() {
    return getRoot().isLR();
  }

  public void reset() {
    Enumeration elems = getRoot().elements();
    DotElement elem;
    while(elems.hasMoreElements()) {
      elem = (DotElement)elems.nextElement();
      elem.detach();
    }
    eid = nid = gid = 0;
    BBox = null;
    root = null;
    clearGraph();
    root = new DotSubgraph(this,name);
  }

  public void reset(String graphName, String graphType) throws IllegalArgumentException {
    name = new String(graphName);
    reset();
    setGraphType(graphType);
  }

  public boolean isEditable() {
    return editable;
  }

  public boolean setEditable(boolean mode) {
    boolean wasMode = mode;
    editable = mode;
    return wasMode;
  }

  public boolean isSelectable() {
    return selectable;
  }

  public boolean setSelectable(boolean mode) {
    boolean wasMode = mode;
    selectable = mode;
    return wasMode;
  }

  public boolean isMenuable() {
    return menuable;
  }

  public boolean setMenuable(boolean mode) {
    boolean wasMode = mode;
    menuable = mode;
    return wasMode;
  }

  public PopUpCard getPropertiesCard() {
    return propertiesCard;
  }

  public PopUpCard setPropertiesCard(PopUpCard card) {
    PopUpCard oldPropCard = propertiesCard;
    propertiesCard = card;
    return oldPropCard;
  }

  public boolean filterGraph(InputStream fromFilterRaw, OutputStream toFilterRaw) {
    if(fromFilterRaw == null || toFilterRaw == null) return false;
    DataInputStream fromFilter = null;
    if(fromFilterRaw instanceof DataInputStream) {
      fromFilter = (DataInputStream)fromFilterRaw;
    } else {
      fromFilter = new DataInputStream(fromFilterRaw);
    }
    PrintStream toFilter = null;
    if(toFilterRaw instanceof PrintStream) {
      toFilter = (PrintStream)toFilterRaw;
    } else {
      toFilter = new PrintStream(toFilterRaw);
    }
    /*
     * semaphore is used to find end of graph to avoid annoying read errors
     * (not EOFException) -- it is a bit of a kludge as it depends on the filter
     * program behaving as expected
     */
    String[] semaphore = { "graph \"semaphore\" {", "}" };
    String content = null;
    boolean status = true;
    try {
      ByteArrayOutputStream theGraph = new ByteArrayOutputStream();
      printGraph(theGraph);
      theGraph.flush();
      content = theGraph.toString();
      theGraph.close();
    } catch(Exception ex) {
      return false;
    }
    try {
      toFilter.print(content);
      // append semaphore
      for(int i = 0; i < semaphore.length; i++) {
	toFilter.println(semaphore[i]);
      }
      toFilter.flush();
      toFilter.close();
    } catch(Exception ex) {
      return false;
    }
    StringBuffer newGraph = new StringBuffer(content.length() + 128);
    try {
      String line = null;
      while((line = fromFilter.readLine()) !=  null) {
	if(line.startsWith(semaphore[0])) {
	  break;
	}
	newGraph.append(line);
      }
    } catch(Exception ex) {
      if(getErrStream() != null) {
	getErrStream().println("ERROR: during read from filter... exception message is: " + ex.getMessage());
	ex.printStackTrace(getErrStream());
      }
      status = false;
      if(newGraph.length() == 0) {
	newGraph.append(content);
	content = null;
      }
    }
    try {
      fromFilter.close();
    } catch(IOException io) {}
    DataInputStream fromStream = null;
    try {
      fromStream = new DataInputStream(new StringBufferInputStream(newGraph.toString()));
    } catch(Exception ex) {
      return false;
    }
    parser program = new parser(fromStream,getErrStream(),this);
    try {
      program.parse();
    } catch(Exception ex) {
      status = false;
      if(getErrStream() != null) {
	getErrStream().println("ERROR: during parse... exception message is: " + ex.getMessage());
	ex.printStackTrace(getErrStream());
      }
      try {
	fromStream.close();
	fromStream = new DataInputStream(new StringBufferInputStream(content));
      } catch(Exception ex2) {
	return false;
      }
      program = new parser(fromStream,getErrStream(),this);
      try {
	program.parse();
      } catch(Exception ex2) {
	return false;
      }
    }
    try {
      instantiateGraph();
    } catch(InstantiationException ex) {
      if(getErrStream() != null) {
	getErrStream().println("ERROR: during instantiation... exception message is: " + ex.getMessage());
	ex.printStackTrace(getErrStream());
      }
      clearGraph();
      return false;
    }
    drawGraph();
    return status;
  }

  public PrintStream setErrStream(PrintStream errStream) {
    PrintStream oldStream = this.errStream;
    this.errStream = errStream;
    return oldStream;
  }

  public PrintStream getErrStream() {
    return errStream;
  }

  public MyRectangle setBaseBBox(MyRectangle box) {
    MyRectangle old = baseBBox;
    baseBBox = box;
    return old;
  }

  public MyRectangle getBaseBBox() {
    return baseBBox;
  }

  public MyRectangle setCropBBox(MyRectangle box) {
    MyRectangle old = cropBBox;
    cropBBox = box;
    return old;
  }

  public MyRectangle getCropBBox() {
    return cropBBox;
  }
}
