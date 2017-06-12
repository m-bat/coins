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

import java.io.*;
import java.util.*;
import java.awt.*;

/**
 * This abstract class is the root class for the
 * <A HREF="att.grappa.Node.html">Node</A>,
 * <A HREF="att.grappa.Edge.html">Edge</A>,
 * <A HREF="att.grappa.Subgraph.html">Subgraph</A> and
 * <A HREF="att.grappa.Graph.html">Graph</A> classes.
 * It is the basis for describing the graph elements.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public abstract class Element
{
  // the containing graph and the parent subgraph element
  private Graph graph = null;
  private Subgraph subgraph = null;
  private boolean deleteFlag = false;

  // identification
  private Long idKey = null;
  String name = null;

  // element deleted indicator
  private boolean visibilityFlag = true;

  // attributes
  Hashtable attributes = null;

  // tags
  private final static int TAGBASE = 3;
  private int[] tags = null;
  private String[] tagStrings = null;
  private int tagCnt = 0;
  private Vector moreTags = null; // for overflow
  private Vector moreTagStrings = null; // for overflow

  // object for extending the application of this element
  private AppObject appObject = null;

  /**
   * Element constructor needed only during init phase of
   * <A HREF="att.grappa.Graph.html">Graph</A> class.
   * Since the Element class is abstact, it cannot be instantiated directly.
   */
  protected Element() {
    // needed due to Graph init (a special case of Subgraph)
  }
  
  /**
   * Element constructor used during init phase of the
   * <A HREF="att.grappa.Node.html">Node</A>,
   * <A HREF="att.grappa.Edge.html">Edge</A> and
   * <A HREF="att.grappa.Subgraph.html">Subgraph</A> classes.
   * Since the Element class is abstact, it cannot be instantiated directly.
   *
   * @param type the type of the element (Grappa.NODE, Grappa.EDGE or Grappa.SUBGRAPH).
   * @param subg the subgraph containing this element.
   *
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  protected Element(int type, Subgraph subg) {
    //super();
    setSubgraph(subg);
    setGraph(subg.getGraph());
    setIdKey(type);
    getGraph().addIdMapping(this);
  }
    
    

  /**
   * Get the AppObject for this Element.
   *
   * @return the application object associated with this element
   *
   * @see AppObject
   */
  public AppObject getAppObject() {
    return(appObject);
  }

  /**
   * Get the DrawObject for this Element.
   *
   * @return the draw object associated with this element
   *
   * @see DrawObject
   */
  public DrawObject getDrawObject() {
    if(appObject == null) return null;
    return appObject.getDrawObject();
  }

  /**
   * Get the type of this Element. Useful for distinguishing Element objects.
   *
   * @return the appropriate class variable constant
   *
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public abstract int getType();

  /**
   * Check if this Element is a node. Overridden in Node to return true.
   *
   * @return false, unless overridden.
   *
   * @see Node#isNode()
   */
  public boolean isNode() {
    return(false);
  }

  /**
   * Check if this Element is an edge. Overridden in Edge to return true.
   *
   * @return false, unless overridden.
   *
   * @see Edge#isEdge()
   */
  public boolean isEdge() {
    return(false);
  }

  /**
   * Check if this Element is a subgraph. Overridden in Subgraph to return true.
   *
   * @return false, unless overridden.
   *
   * @see Subgraph#isSubgraph()
   */
  public boolean isSubgraph() {
    return(false);
  }

  /**
   * Intended to be a subclass-specific name generating method.
   * Used by edges and when nodes or graphs are created without
   * an explicit name.
   * Note that graphs and nodes should also have a setName that
   * takes an explicit name as an argument.
   */
  abstract void setName();

  /**
   * Get the name of this Element.
   *
   * @return the name of the element.
   */
  public String getName() {
    return(name);
  }
  
  /**
   * Sets or creates an attribute for this element from the attribute supplied.
   * The storage key is the attribute name.  If the value portion of the
   * supplied attribute is null, then the attribute will be removed from the
   * element.
   *
   * @param attr the attribute from which to set the element's attribute.
   * @return the value of the (local) attribute previously stored
   *         under the same name
   */
  public String setAttribute(Attribute attr) {
    if(attr == null) {
      return null;
    }
    return setAttribute(attr.getName(),attr.getValue());
  }
  
  /**
   * Sets or creates an attribute for this element from the supplied
   * arguments. The storage key is the attribute name.  If the value
   * argument is null, then the attribute will be removed from the element.
   *
   * @param name the attribute name
   * @param value the attribute value
   * @return the value of the (local) attribute previously stored
   *         under the same name
   */
  public String setAttribute(String name, String value) {
    if(attributes == null) {
      attributes = new Hashtable();
    }
    if(name == null) {
      throw new IllegalArgumentException("cannot set an attribute using a null name");
    }
    String oldValue = null;
    Attribute crntAttr =  getLocalAttribute(name);
    if(crntAttr == null) {
      if(value == null) {
	return null;
      }
      attributes.put(name,(crntAttr = new Attribute(name,value)));
      if(getAppObject() != null) {
	Attribute dfltAttr =  getDefaultAttribute(name);
	getAppObject().iSpy(crntAttr,dfltAttr);
      }
    } else {
      oldValue = crntAttr.getValue();
      if(value == null) {
	//System.err.println("direct removal of ("+name+","+value+") from "+getName());
	removeAttribute(name);
	return oldValue;
      } else {
	crntAttr.setValue(value);
      }
    }
    if(crntAttr.hasChanged()) {
      crntAttr.notifyObservers();
      // special case
      if(name.equals("tag")) {
	removeAllTags();
	char[] valarr = value.toCharArray();
	int i = 0;
	int j = 0;
	while(i<valarr.length) {
	  if(valarr[i] == ',') {
	    if(j!=i) addTag(new String(valarr,j,i-j));
	    j=i+1;
	  }
	  i++;
	}
	if(i!=j) addTag(new String(valarr,j,i-j));
      }
    }
    return oldValue;
  }

  
  /*
   * Removes the named attribute from the (local) attribute table and
   * applies the default attribute (if any)
   *
   * @param name the name of the attribute to be removed.
   * @return the default attribute pair for this attribute.
   */
  private Attribute removeAttribute(String name) {
    if(name == null) return null;
    Attribute dfltAttr =  getDefaultAttribute(name);
    Attribute attr = null;
    if(attributes != null) attr = (Attribute)attributes.remove(name);
    if(attr == null) return dfltAttr;
    if(dfltAttr == null) {
      attr.setValue("");
    }
    attr.setChanged();
    attr.notifyObservers(dfltAttr);
    return dfltAttr;
  }
  
  /**
   * Sets or creates a default attribute for this element type within the
   * containing subgraph of this element from the supplied arguments.
   * The storage key is the attribute name.
   * If the value argument is null, then the
   * attribute will be removed from the subgraph.
   *
   * @param name the attribute name
   * @param value the attribute value
   * @return the value of the (default) attribute previously stored
   *         under the same name
   */
  public String setDefaultAttribute(String name, String value) {
    return setDefaultAttribute(getType(),name,value);
  }
  
  /**
   * Sets or creates a default attribute of the specified type within the
   * containing subgraph of this element from the supplied arguments.
   * The storage key is the attribute name.
   * If the value argument is null, then the
   * attribute will be removed from the subgraph.
   *
   * @param type the default attribute type
   * @param name the attribute name
   * @param value the attribute value
   * @return the value of the (default) attribute previously stored
   *         under the same name
   */
  public String setDefaultAttribute(int type, String name, String value) {
    String oldValue = null;
    Subgraph subg = getSubgraph();
    switch(type) {
    case Grappa.NODE:
      oldValue = subg.setNodeAttribute(name,value);
      break;
    case Grappa.EDGE:
      oldValue = subg.setEdgeAttribute(name,value);
      break;
    case Grappa.SUBGRAPH:
      // ignore subg == null (i.e., root subgraph case) 
      if(subg != null) {
	oldValue = subg.setAttribute(name,value);
      }
      break;
    }
    return oldValue;
  }

  /**
   * Sets or creates a default attribute for this element type within the
   * containing subgraph of this element from the supplied arguments.
   * The storage key is the attribute name.
   * If the value portion of the supplied attribute is null, then the
   * attribute will be removed from the subgraph.
   *
   * @param attr the attribute to which the default should be set
   * @return the value of the (default) attribute previously stored
   *         under the same name
   */
  public String setDefaultAttribute(Attribute attr) {
    return setDefaultAttribute(getType(),attr);
  }

  /**
   * Sets or creates a default attribute of the specified type within the
   * containing subgraph of this element from the supplied arguments.
   * The storage key is the attribute name.
   * If the value portion of the supplied attribute is null, then the
   * attribute will be removed from the subgraph.
   *
   * @param type the default attribute type
   * @param attr the attribute to which the default should be set
   * @return the value of the (default) attribute previously stored
   *         under the same name
   */
  public String setDefaultAttribute(int type, Attribute attr) {
    if(attr == null) return null;
    String oldValue = null;
    Subgraph subg = getSubgraph();
    switch(type) {
    case Grappa.NODE:
      oldValue = subg.setNodeAttribute(attr);
      break;
    case Grappa.EDGE:
      oldValue = subg.setEdgeAttribute(attr);
      break;
    case Grappa.SUBGRAPH:
      // ignore subg == null (i.e., root subgraph case) 
      if(subg != null) {
	oldValue = subg.setAttribute(attr);
      }
      break;
    }
    return oldValue;
  }
  
  /**
   * Gets an enumeration of the keys for this Element's local attributes.
   *
   * @return an Enumneration of String objects
   */
  
  public Enumeration  getLocalAttributeKeys() {
    if(attributes == null) {
      return Grappa.emptyEnumeration.elements();
    }
    return(attributes.keys());
  }

  /**
   * Get an Enumeration of the Attribute objects for this Element.
   *
   * @return an Enumneration of the (local) Attribute objects.
   */
  public Enumeration  getLocalAttributePairs() {
    if(attributes == null) {
      return Grappa.emptyEnumeration.elements();
    }
    return(attributes.elements());
  }

  /**
   * Get an enumeration of all attribute pairs for this element.
   *
   * @return an enumeration of local and default Attribute objects for this element.
   */
  public Enumeration getAttributePairs() {
    Hashtable pairs = null;
    Attribute attr = null;

    Enumeration enum = getLocalAttributePairs();
    if(enum.hasMoreElements()) pairs = new Hashtable(32);
    while(enum.hasMoreElements()) {
      attr = (Attribute)enum.nextElement();
      pairs.put(attr.getName(),attr);
    }

    switch(getType()) {
    case Grappa.NODE:
      enum = getSubgraph().getNodeAttributePairs();
      break;
    case Grappa.EDGE:
      enum = getSubgraph().getEdgeAttributePairs();
      break;
    case Grappa.SUBGRAPH:
      enum = getLocalAttributePairs();
      break;
    }

    if(pairs != null) {
      while(enum.hasMoreElements()) {
	attr = (Attribute)enum.nextElement();
	if(!pairs.containsKey(attr.getName())) {
	  pairs.put(attr.getName(),attr);
	}
      }
      return pairs.elements();
    }

    return enum;
  }

  /**
   * Get only the corresponding local attribute for the specified key.  A local attribute is
   * one associated directly with this element as opposed to a subgraph
   * ancestor.
   *
   * @param key the search key for the corresponding attribute.
   *
   * @return the value of the local Attribute object matching the key or null.
   */
  public Attribute getLocalAttribute(String key) {
    if(attributes == null) return(null);
    return((Attribute)(attributes.get(key)));
  }

  /**
   * Get the corresponding default attribute for the specified type and key.
   *
   * @param type the type of the default attribute
   * @param key the search key for the corresponding attribute.
   * @return the value of the default Attribute object matching the key or null.
   */
  public Attribute getDefaultAttribute(int type, String key) {
    Attribute value = null;
    Subgraph sg = null;

    if(isSubgraph()) sg = (Subgraph)this;
    else sg = getSubgraph();

    switch(type) {
    case Grappa.NODE:
      value = sg.getNodeAttribute(key);
      break;
    case Grappa.EDGE:
      value = sg.getEdgeAttribute(key);
      break;
    case Grappa.SUBGRAPH:
      value = sg.getLocalAttribute(key);
      break;
    }
    return(value);
  }

  /**
   * Get the default attribute of this element for the specified key.
   *
   * @param key the search key for the corresponding attribute.
   * @return the value of the default Attribute object matching the key or null.
   */
  public Attribute getDefaultAttribute(String key) {
    return getDefaultAttribute(getType(),key);
  }

  /**
   * Get the Attribute of this Element for the specified key.
   * Search first local, then default attributes until a match is found.
   *
   * @param key the search key for the attribute.
   * @return the corresponding Attribute object or null.
   */
  public Attribute getAttribute(String key) {
    Attribute attr = null;
    
    if((attr = getLocalAttribute(key)) == null) {
      attr = getDefaultAttribute(key);
    }
    return(attr);
  }

  /**
   * Get the Attribute value of this Element for the specified key.
   * Search first local, then default attributes until a match is found.
   *
   * @param key the search key for the attribute.
   * @return the corresponding attribute value or null.
   */
  public String getAttributeValue(String key) {
    String value = null;
    
    Attribute attr = getAttribute(key);
    if(attr != null) {
      value = attr.getValue();
    }
    return(value);
  }

  /**
   * Checks to see if this element has an Attribute matching the key
   *
   * @param key the search key for the attribute.
   * @return true if there is a matching attribute, false otherwise.
   */
  public boolean hasAttributeForKey(String key) {
    if(getAttribute(key) == null) return(false);
    return(true);
  }

  /**
   * Get the Graph of this Element.
   *
   * @return the containing graph object.
   */
  public Graph getGraph() {
    return(graph);
  }

  /**
   * Get the containing Subgraph of this Element.
   *
   * @return the parent subgraph object.
   */
  public Subgraph getSubgraph() {
    return(subgraph);
  }

  /**
   * Set the containing graph for this element.
   *
   * @param graph the overall graph that contains this element.
   */
  void setGraph(Graph graph) {
    this.graph = graph;
  }

  /**
   * Set the parent subgraph for this element.
   *
   * @param subgraph the parent subgraph that contains this element.
   */
  public void setSubgraph(Subgraph subgraph) {
    if(this.subgraph != null && this.subgraph != subgraph) {
      switch(this.getType()) {
      case Grappa.NODE:
	this.subgraph.removeNode(((Node)this).getName());
	subgraph.addNode((Node)this);
	break;
      case Grappa.EDGE:
	this.subgraph.removeEdge(((Edge)this).getName());
	subgraph.addEdge((Edge)this);
	break;
      case Grappa.SUBGRAPH:
	this.subgraph.removeGraph(((Subgraph)this).getName());
	subgraph.addSubgraph((Subgraph)this);
	break;
      }
    }
    this.subgraph = subgraph;
  }

  /**
   * Get the ID number of this Element.
   *
   * @return the id number of this element.
   */
  public int getId() {
    return (int)((getIdKey().longValue())>>Grappa.TYPES_SHIFT);
  }

  /**
   * Get the ID of this Element as a Long object.
   *
   * @return the id object of this element.
   */
  public Long getIdKey() {
    return(idKey);
  }

  /**
   * Sets the id key of this element
   */
  protected void setIdKey(int type) {
    idKey = Graph.idMapKey(type,getGraph().nextId(type));
  }

  /**
   * Print a description of this element to the given print stream.
   *
   * @param out the print stream for output.
   */
  public void printElement(PrintWriter out) {
    String indent = new String(getGraph().getIndent());
    
    out.print(indent + toString());
    getGraph().incrementIndent();
    printAttributes(out,indent);
    getGraph().decrementIndent();
    out.println();
  }

  /*
   * Print attributes to given stream.  A square open bracket prefix and
   * closed bracket suffix enclose the attributes, but are printed only if
   * there are any attributes to print.  The supplied indent determines the
   * indentation of the final bracket (it is assumed the element name has
   * already printed to the output stream.
   *
   * @param out the print stream for output.
   * @param outerIndent the indent to use for the prefix and suffix.
   */
  private void  printAttributes(PrintWriter out, String outerIndent) {
    String indent = new String(getGraph().getIndent());
    String prefix = " [";
    String suffix = Grappa.NEW_LINE + outerIndent + "];";
    Attribute attr;
    String value;
    String key;
    boolean first = true;

    // special case
    if(tagCnt > 0) {
      first = false;
      out.println(prefix);
      StringBuffer strbuf = new StringBuffer(tagStrings[0]);
      for(int i = 1; i < tagCnt; i++) {
	strbuf.append(',');
	if(i < TAGBASE) {
	  strbuf.append(tagStrings[i]);
	} else {
	  strbuf.append((String)moreTagStrings.elementAt(i-TAGBASE));
	}
      }
      out.print(indent + "tag=" + canonString(strbuf.toString()));
    }

    if(attributes != null && !attributes.isEmpty()) {
      Enumeration attrs = attributes.elements();
      while(attrs.hasMoreElements()) {
	attr = (Attribute)(attrs.nextElement());
	key = attr.getName();
	// special case
	if(key.equals("tag")) continue;
	value = attr.getValue();
	if(attr != null && !attr.equalsValue(getDefaultAttribute(key))) {
	  if(first) {
	    first = false;
	    out.println(prefix);
	  } else {
	    out.println(",");
	  }
	  out.print(indent + key + " = " + canonString(value));
	}
      }
    }
    if(!first) {
      out.print(suffix);
    }
  }

  /**
   * Get the String rendition of the element.
   *
   * @return the string rendition of the element, quoted as needed.
   */
  public String toString() {
    return(canonString(getName()));
  }

  /**
   * Canonicalizes the supplied string for output.
   *
   * @param input the string to be quoted, possibly.
   * @return the input string, possibly enclosed in double quotes and
   *         with internal double quotes protected.
   */
  // essentially the agstrcanon function from libgraph (by S. J. North)
  public static String canonString(String input) {
    int len;

    if(input == null || (len = input.length()) == 0) {
      return("\"\"");
    }
    
    StringBuffer strbuf = new StringBuffer(len + 8);
    char[] array = input.toCharArray();
    char ch;
    boolean has_special = false;
    boolean maybe_num = (array[0] == '.' || (array[0] >= '0' && array[0] <= '9'));

    for(int isub = 0; isub < array.length; isub++) {
      if(array[isub] == '"') {
	strbuf.append('\\');
	has_special = true;
      } else {
	if(
	   array[isub] < '0' || (array[isub] > '9' && array[isub] < 'A') ||
	   (array[isub] > 'Z' && array[isub] < 'a' && array[isub] != '_') ||
	   array[isub] > 'z'
	   ) {
	  has_special = true;
	} else if(maybe_num && ((array[isub] < '0' || array[isub] > '9') && array[isub] != '.')) {
	  has_special = true;
	}
      }
      strbuf.append(array[isub]);
    }
    // annoying, but necessary kludge to make libgraph parser happy
    if(!has_special && len <= 8) {
      String low = input.toLowerCase();
      if(
	 low.equals("node") || low.equals("edge") || low.equals("graph") ||
	 low.equals("digraph") || low.equals("subgraph") || low.equals("strict")
	 ) {
	has_special = true;
      }
    }
    if(has_special) {
      strbuf.append('"');
      strbuf.insert(0,'"');
    }
    return(strbuf.toString());
  }

  /**
   * Canonicalizes the supplied string for look-up.
   *
   * @param input the string to be canonicalized.
   * @return the input string, with non-alphanumerics 
   *              removed and alphabetics are converted to lower-case.
   */
  public static String canonValue(String input) {
    if(input == null) return null;
    char[] array = input.toCharArray();
    int len = 0;
    boolean allDigits = true;
    for(int i = 0; i < array.length; i++) {
      if(Character.isUpperCase(array[i])) {
	array[len++] = Character.toLowerCase(array[i]);
	allDigits = false;
      } else if(Character.isLowerCase(array[i])) {
	array[len++] = array[i];
	allDigits = false;
      } else if(Character.isDigit(array[i])) {
	array[len++] = array[i];
      }
    }
    if(len == 0 || allDigits) return null;
    return new String(array,0,len);
  }

  /**
   * set AppObject associated with element.
   * @param appObject value to which appObject is to be set
   */
  protected void setAppObject(AppObject appObject) {
    this.appObject = appObject;
  }

  boolean getDeleteFlag() {
    return deleteFlag;
  }

  /*
   * Included to guard against looping in the case that the AppObject
   * delete() calls the Element delete();
   */
  private boolean deleteCalled = false;

  /**
   * Method for deleting an element.
   * Clears element references from graph tables and frees up space explicitly.
   * Also calls the AppObject delete() method.
   *
   * @see AppObject#delete()
   * @see Graph#reset()
   */
  public void delete() {
    if(deleteCalled) return;
    deleteCalled = true;
    if(getAppObject() != null) {
      getAppObject().delete();
    }
    detach();
  }

  void detach() {
    String name = getName();
    if(name == null) return; // another hedge against looping
    AppObject appobj = null;
    switch(getType()) {
    case Grappa.NODE:
      if(!getDeleteFlag()) {
	Enumeration enum = ((Node)this).edgeElements();
	while(enum.hasMoreElements()) {
	  ((Edge)enum.nextElement()).delete();
	}
      }
      getSubgraph().removeNode(name);
      break;
    case Grappa.EDGE:
      getSubgraph().removeEdge(name);
      break;
    case Grappa.SUBGRAPH:
      if(!getDeleteFlag()) {
	Enumeration enum = ((Subgraph)this).elements();
	Element elem = null;
	while(enum.hasMoreElements()) {
	  elem = (Element)enum.nextElement();
	  if(elem == (Element)this) continue;
	  if(!elem.getDeleteFlag()) {
	    elem.delete();
	  }
	}
      }
      if(getSubgraph() != null) getSubgraph().removeGraph(name);
      break;
    }
    getGraph().removeIdMapping(this);
    if(appObject != null) {
      DrawObject drwobj = getDrawObject();
      Enumeration enum = getAttributePairs();
      Attribute attr = null;
      while(enum.hasMoreElements()) {
	attr = (Attribute)(enum.nextElement());
	if(drwobj != null) {
	  attr.deleteObserver(drwobj);
	}
	attr.deleteObserver(appObject);
      }
      if(drwobj != null) drwobj.free();
      appObject.free();
    }
    free();
  }

  /**
   * Included to help speed release of valuable memory.
   */
  void free() {
    idKey = null;
    name = null;
    visibilityFlag = true;
    attributes = null;
    appObject = null;
    deleteFlag = true;
    deleteCalled = false;

    if(isSubgraph()) {
      if(subgraph == null) {
	((Subgraph)this).setAppObjects();
	((Subgraph)this).setDrawObjects();
      } else {
	subgraph.setAppObjects();
	subgraph.setDrawObjects();
      }
    }

    if(this == graph) deleteFlag = false;

    graph = null;
    subgraph = null;
  }

  /**
   * Tags the element with the supplied string.  Unlike attributes, tags are
   * only used within Grappa are cannot be saved to disk and reloaded.
   * 
   * @param tag the tag to associate with this Element.
   */
  public void addTag(String tag) {
    if(tag == null || tag.indexOf(',') >= 0) {
      throw new RuntimeException("tag value null or contains a comma (" + tag + ")");
    }
    if(tagCnt < TAGBASE) {
      if(tags == null) {
	tags = new int[TAGBASE];
	tagStrings = new String[TAGBASE];
      }
      tags[tagCnt] = tag.hashCode();
      tagStrings[tagCnt++] = tag;
    } else {
      if(moreTags == null) {
	moreTags = new Vector(TAGBASE,TAGBASE);
	moreTagStrings = new Vector(TAGBASE,TAGBASE);
      }
      tagCnt++;
      moreTags.addElement(new Integer(tag.hashCode()));
      moreTagStrings.addElement(tag);
    }
  }

  /**
   * Check if this Element has the supplied tag.
   *
   * @param tag tag value to be searched for
   * @return true, if this Element contains the supplied tag
   */
  public boolean hasTag(String tag) {
    if(tagCnt == 0) return false;
    int hash = tag.hashCode();
    for(int i = 0; i < tagCnt; i++) {
      if(i < TAGBASE) {
	if(tags[i] == hash) return true;
      } else {
	if(((Integer)moreTags.elementAt(i-TAGBASE)).intValue() == hash) return true;
      }
    }
    return false;
  }

  /**
   * Removes any and all tags associated with this element.
   */
  public void removeAllTags() {
    if(tagCnt == 0) return;
    if(moreTags != null) {
      moreTags.removeAllElements();
      moreTagStrings.removeAllElements();
    }
    tagCnt = 0;
  }

  /**
   * Removes the specified tag from this element.
   *
   * @param tag the tag value to remove
   */
  public void removeTag(String tag) {
    if(tagCnt == 0) return;
    int hash = tag.hashCode();
    int j = -1;
    for(int i = 0; i < tagCnt; i++) {
      if(j >= 0) {
	if(i < TAGBASE) {
	  tags[j] = tags[i];
	  tagStrings[j++] = tagStrings[i];
	} else if(j < TAGBASE) {
	  tags[j] = ((Integer)moreTags.elementAt(i-TAGBASE)).intValue();
	  tagStrings[j++] = (String)moreTagStrings.elementAt(i-TAGBASE);
	} else {
	  moreTags.removeElementAt(j-TAGBASE);
	  moreTagStrings.removeElementAt(j-TAGBASE);
	  break;
	}
      } else if(i < TAGBASE) {
	if(tags[i] == hash) {
	  j = i;
	  continue;
	}
      } else {
	if(((Integer)moreTags.elementAt(i-TAGBASE)).intValue() == hash) {
	  j = i;
	  moreTags.removeElementAt(j-TAGBASE);
	  moreTagStrings.removeElementAt(j-TAGBASE);
	  break;
	}
      }
    }
    if(j >= 0) tagCnt--;
  }

  /**
   * Sets the visibility flag of this element to the specified value.
   * Draws or erases the element on any existing DrawPanes.
   *
   * @param newValue the new value to assign to the visibilty flag
   * @return the old value of the visibility flag
   */
  public boolean setVisible(boolean newValue) {
    boolean oldValue = visibilityFlag;
    if(oldValue != newValue) {
      DrawObject drwobj = getDrawObject();
      if(drwobj != null) {
	if(newValue) {
	  visibilityFlag = newValue;
	  drwobj.draw();
	} else {
	  drwobj.erase(null);
	  visibilityFlag = newValue;
	}
      }
    } else {
      visibilityFlag = newValue;
    }
    return oldValue;
  }

  /**
   * Check if this Element is flagged as visible.
   *
   * @return the current value of this element's visibility flag
   */
  public boolean isVisible() {
    return visibilityFlag;
  }

  Hashtable getAttributeTable() {
    return(attributes);
  }

  void rawSetSubgraph(Subgraph subg) {
    subgraph = subg;
  }

  void possessElement(Element demon) {
    idKey = demon.getIdKey();
    attributes = demon.getAttributeTable();
  }
}
