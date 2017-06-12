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
 * This class abstracts the dot node, edge and subgraph elements.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public abstract class DotElement
{
  /**
   * Initial capacity of the attribute dictionary.
   *
   * @see Hashtable
   */
  public final static int initialAttributesCapacity = 5;
  /**
   * Size increment of the attribute dictionary.
   *
   * @see Hashtable
   */
  public final static int attributesIncrement = 5;

  // the containing graph and the parent subgraph element
  private DotGraph graph = null;
  private DotSubgraph subgraph = null;

  // identification
  private Integer id = null;
  protected String name = null;

  // element deleted indicator
  protected boolean deletedFlag = false;

  // attributes
  protected Hashtable attributes = null;

  // object for drawing this element
  private AppObject appObject = null;

  /**
   * @return the dot object associated with this element
   *
   * @see AppObject
   */
  public AppObject getAppObject() {
    return(appObject);
  }

  /**
   * @return the draw object associated with this element's object
   *
   * @see DrawObject
   */
  public DrawObject getDrawObject() {
    if(appObject == null) return null;
    return appObject.getDrawObject();
  }

  /**
   * Useful for distinguishing DotElement objects.
   *
   * @return the appropriate class variable constant
   * @see Grappa
   */
  public abstract int typeFlag();

  /**
   * Overridden in DotNode to return true.
   *
   * @return false, unless overridden.
   *
   * @see DotNode#isNode()
   */
  public boolean isNode() {
    return(false);
  }

  /**
   * Overridden in DotEdge to return true.
   *
   * @return false, unless overridden.
   *
   * @see DotEdge#isNode()
   */
  public boolean isEdge() {
    return(false);
  }

  /**
   * Overridden in DotSubgraph to return true.
   *
   * @return false, unless overridden.
   *
   * @see DotSubgraph#isNode()
   */
  public boolean isGraph() {
    return(false);
  }

  /**
   * Intended to be a subclass-specific name generating method.
   * Used by edges and when nodes or graphs are created without
   * an explicit name.
   * Note that graphs and nodes should also have a setName that
   * takes an explicit name as an argument.
   */
  public abstract void setName();

  /**
   * @return the name of the element.
   */
  public String getName() {
    return(name);
  }
  
  /**
   * Enters the attribute into the attribute table.
   * The storage key is the attribute name.
   *
   * @param attr the attribute to be entered.
   * @return the attribute pair previously stored for this attribute.
   */
  public Attribute setAttribute(Attribute attr) {
    if(attributes == null) {
      attributes = new Hashtable(initialAttributesCapacity,attributesIncrement);
    }
    if(attr == null) {
      return null;
    }
    Attribute oldAttr =  ((Attribute)attributes.put(attr.getName(),attr));
    if(appObject != null) appObject.handleAttribute(attr);
    return oldAttr;
  }
  
  /**
   * Removes the named attribute from the (local) attribute table and
   * applies the default attribute (if any)
   *
   * @param name the name of the attribute to be removed.
   * @return the default attribute pair for this attribute.
   */
  public Attribute removeAttribute(String name) {
    Attribute oldAttr =  getDefaultAttribute(name);
    if(attributes != null) attributes.remove(name);
    if(appObject != null && oldAttr != null) appObject.handleAttribute(oldAttr);
    return oldAttr;
  }
  
  /**
   * Enters the attribute pair into the attribute table.
   * The storage key is the attribute name.
   *
   * @param name the attribute name to be entered.
   * @param value the associated attribute value to be entered.
   * @return the attribute pair previously stored for this name.
   */
  public Attribute setAttribute(String name, String value) {
    if(name == null) {
      throw new RuntimeException("Attempt to setAttribute using null name");
    }
    return setAttribute(new Attribute(name,value));
  }
  
  /**
   * @return an Enumneration of the (local) attribute keys.
   */
  
  public Enumeration  getAttributeKeys() {
    if(attributes == null) {
      attributes = new Hashtable(initialAttributesCapacity,attributesIncrement);
    }
    return(attributes.keys());
  }

  /**
   * @return an Enumneration of the (local) attribute pairs.
   */
  public Enumeration  getLocalAttributePairs() {
    if(attributes == null) {
      attributes = new Hashtable(initialAttributesCapacity,attributesIncrement);
    }
    return(attributes.elements());
  }

  /**
   * Returns an enumeration of all attribute pairs for this element.
   *
   * @return an enumeration of local and default attributes for this element.
   */
  public Enumeration getAttributePairs() {
    Hashtable pairs = new Hashtable(32);

    Attribute value = null;

    Enumeration enum = getLocalAttributePairs();
    while(enum.hasMoreElements()) {
      value = (Attribute)enum.nextElement();
      pairs.put(value.getName(),value);
    }

    DotSubgraph sg = null;

    switch(typeFlag()) {
    case Grappa.DOT_NODE:
      sg = getSubgraph();
      while(sg != null) {
	if((enum = sg.getNodeAttributePairs()) != null) {
	  while(enum.hasMoreElements()) {
	    value = (Attribute)enum.nextElement();
	    if(!pairs.containsKey(value.getName())) {
	      pairs.put(value.getName(),value);
	    }
	  }
	}
	sg = sg.getSubgraph();
      }
      if((enum = getGraph().getDefaultNodeAttributePairs()) != null) {
	while(enum.hasMoreElements()) {
	  value = (Attribute)enum.nextElement();
	  if(!pairs.containsKey(value.getName())) {
	    pairs.put(value.getName(),value);
	  }
	}
      }
      break;
    case Grappa.DOT_EDGE:
      sg = getSubgraph();
      while(sg != null) {
	if((enum = sg.getEdgeAttributePairs()) != null) {
	  while(enum.hasMoreElements()) {
	    value = (Attribute)enum.nextElement();
	    if(!pairs.containsKey(value.getName())) {
	      pairs.put(value.getName(),value);
	    }
	  }
	}
	sg = sg.getSubgraph();
      }
      if((enum = getGraph().getDefaultEdgeAttributePairs()) != null) {
	while(enum.hasMoreElements()) {
	  value = (Attribute)enum.nextElement();
	  if(!pairs.containsKey(value.getName())) {
	    pairs.put(value.getName(),value);
	  }
	}
      }
      break;
    case Grappa.DOT_GRAPH:
      sg = getSubgraph();
      while(sg != null) {
	if((enum = sg.getGraphAttributePairs()) != null) {
	  while(enum.hasMoreElements()) {
	    value = (Attribute)enum.nextElement();
	    if(!pairs.containsKey(value.getName())) {
	      pairs.put(value.getName(),value);
	    }
	  }
	}
	sg = sg.getSubgraph();
      }
      if((enum = getGraph().getDefaultGraphAttributePairs()) != null) {
	while(enum.hasMoreElements()) {
	  value = (Attribute)enum.nextElement();
	  if(!pairs.containsKey(value.getName())) {
	    pairs.put(value.getName(),value);
	  }
	}
      }
      break;
    }
    return pairs.elements();
  }

  /**
   * Returns only the corresponding local attribute.  A local attribute is
   * one associated directly with this element as opposed to a subgraph
   * ancestor.
   *
   * @param key the search key for the corresponding attribute.
   *
   * @return the value of the local attribute matching the key.
   */
  public Attribute getLocalAttribute(String key) {
    if(attributes == null) return(null);
    return((Attribute)(attributes.get(key)));
  }

  /**
   * Returns the first corresponding attribute found in the chain
   * of containing subgraphs.  If the attribute is not found in a
   * subgraph, the system defaults are checked as well.
   *
   * @param key the search key for the corresponding attribute.
   * @return the value of the default attribute matching the key or null.
   */
  public Attribute getDefaultAttribute(String key) {
    Attribute value = null;
    DotSubgraph sg = null;

    switch(typeFlag()) {
    case Grappa.DOT_NODE:
      sg = getSubgraph();
      while(sg != null && (value = sg.getNodeAttribute(key)) == null) {
	sg = sg.getSubgraph();
      }
      if(value == null) {
	value = DotGraph.getNodeAttribute(key);
      }
      break;
    case Grappa.DOT_EDGE:
      sg = getSubgraph();
      while(sg != null && (value = sg.getEdgeAttribute(key)) == null) {
	sg = sg.getSubgraph();
      }
      if(value == null) {
	value = DotGraph.getEdgeAttribute(key);
      }
      break;
    case Grappa.DOT_GRAPH:
      sg = getSubgraph();
      while(sg != null && (value = sg.getGraphAttribute(key)) == null) {
	sg = sg.getSubgraph();
      }
      if(value == null) {
	value = DotGraph.getGraphAttribute(key);
      }
      break;
    }
    return(value);
  }

  /**
   * Search first local, then default attributes until a match is found.
   *
   * @param key the search key for the attribute.
   * @return the corresponding attribute pair or null.
   */
  public Attribute getAttribute(String key) {
    Attribute attr = null;
    
    if((attr = getLocalAttribute(key)) == null) {
      attr = getDefaultAttribute(key);
    }
    return(attr);
  }

  /**
   * Search first local, then default attributes until a match is found.
   *
   * @param key the search key for the attribute.
   * @return the corresponding attribute value or null.
   */
  public String getAttributeValue(String key) {
    Attribute attr = null;
    String value = null;
    
    if((attr = getLocalAttribute(key)) == null) {
      attr = getDefaultAttribute(key);
    }
    if(attr != null) {
      value = attr.getValue();
    }
    return(value);
  }

  /**
   * Makes a copy of this elements attributes.
   *
   * @return a copy of the attributs or null.
   */
  public Hashtable copyAttributes() {
    if(attributes == null) return(null);
    Hashtable hcopy = new Hashtable(attributes.size(),attributesIncrement);

    Enumeration attrs = attributes.elements();
    Attribute attr;
    Attribute attrCopy;

    while(attrs.hasMoreElements()) {
      attr = (Attribute)(attrs.nextElement());
      attrCopy = attr.copy();
      hcopy.put(attrCopy.getName(),attrCopy);
    }
    return(hcopy);
  }

  //public void setAttributes(Hashtable attrs) {
  //  attributes = attrs;
  //}

  /**
   * Checks to see if there is a local attribute pair matching the key.
   *
   * @param key the search key for the attribute.
   * @return true if there is a matching local attribute, false otherwise.
   */
  public boolean hasAttributeValue(String key) {
    if(attributes == null) return(false);
    return(attributes.contains(key));
  }

  /**
   * @return the containing graph object.
   */
  public DotGraph getGraph() {
    return(graph);
  }

  /**
   * @return the parent subgraph object.
   */
  public DotSubgraph getSubgraph() {
    return(subgraph);
  }

  /**
   * Set the containing graph for this element.
   *
   * @param graf the overall graph that contains this element.
   */
  public void setGraph(DotGraph graf) {
    this.graph = graf;
  }

  /**
   * Set the parent subgraph for this element.
   *
   * @param subgraf the parent subgraph that contains this element.
   */
  public void setSubgraph(DotSubgraph subgraf) {
    if(subgraph != null && subgraph != subgraf) {
      switch(this.typeFlag()) {
      case Grappa.DOT_NODE:
	subgraph.removeNode(((DotNode)this).getName());
	subgraf.addNode((DotNode)this);
	break;
      case Grappa.DOT_EDGE:
	subgraph.removeEdge(((DotEdge)this).getName());
	subgraf.addEdge((DotEdge)this);
	break;
      case Grappa.DOT_GRAPH:
	subgraph.removeGraph(((DotSubgraph)this).getName());
	subgraf.addGraph((DotSubgraph)this);
	break;
      }
    }
    subgraph = subgraf;
  }

  /**
   * Set the id of this element, and set-up
   * id/element linkage.
   *
   * @param nbr the new id number of this element.
   */
  protected void setId(int nbr) {
    id = new Integer(nbr);
    getGraph().addIdMapping(this);
  }

  /**
   * @return the id number of this element.
   */
  public int getId() {
    return(id.intValue());
  }

  /**
   * @return the id object of this element.
   */
  public Integer getIdObj() {
    return(id);
  }

  /**
   * Print a description of this element to the given print stream.
   *
   * @param out the print stream for output.
   */
  public void printElement(PrintStream out) {
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
  private void  printAttributes(PrintStream out, String outerIndent) {
    String indent = new String(getGraph().getIndent());
    String prefix = " [";
    String suffix = outerIndent + "];";
    Attribute attr;
    String value;
    String key;
    int nbr = 0;

    if(attributes == null || attributes.isEmpty()) {
      return;
    }
    Enumeration attrs = attributes.elements();
    while(attrs.hasMoreElements()) {
      attr = (Attribute)(attrs.nextElement());
      key = attr.getName();
      value = attr.getValue();
      if(attr != null && !attr.equalsValue(getDefaultAttribute(key))) {
	nbr++;
	if(nbr == 1) {
	  out.println(prefix);
	}
	if(attrs.hasMoreElements()) {
	  out.println(indent + key + " = " + canonString(value) + ",");
	} else {
	  out.println(indent + key + " = " + canonString(value));
	}
      }
    }
    if(nbr > 0) {
      out.print(suffix);
    }
  }

  /**
   * String rendition of the element.
   *
   * @return the string rendition of the element, quoted as needed.
   */
  public String toString() {
    return(canonString(getName()));
  }

  /**
   * Canonicalizes string for output.
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
   * Canonicalizes string for look-up.
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
   * Instantiate AppObject associated with element.
   * @param setDefaults indicates that object may override some attribute
   *                    values with default values specific to the AppObject
   *                    (usually set false unless creating an element
   *                    interactively).
   */
  public void setAppObject(boolean setDefaults) throws InstantiationException {
    try {
      appObject = AppObject.getAppObject(this,setDefaults);
    }
    catch(ExceptionInInitializerError err) {
      Exception ex = (Exception)err.getException();
      throw new InstantiationException(ex.getMessage());
    }
  }

  public void detach() {
    String name = getName();
    if(getSubgraph() != null) {
      switch(typeFlag()) {
      case Grappa.DOT_NODE:
	getSubgraph().removeNode(name);
	break;
      case Grappa.DOT_EDGE:
	getSubgraph().removeEdge(name);
	break;
      case Grappa.DOT_GRAPH:
	getSubgraph().removeGraph(name);
	break;
      }
    }
    Integer id = getIdObj();
    getGraph().removeIdMapping(id);
    DrawObject drwobj = getDrawObject();
    if(drwobj != null) drwobj.free();
    if(appObject != null) appObject.free();
    free();
  }

  public void free() {
    graph = null;
    subgraph = null;
    id = null;
    name = null;
    deletedFlag = false;
    attributes = null;
    appObject = null;
  }
}
