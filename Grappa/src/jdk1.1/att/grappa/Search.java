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

/**
 * This class generates an enumeration of graph elements that satisfy
 * the search criteria supplied at creation.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Search implements GraphEnumeration
{

  /**
   * Used to indicate a search of element IDs
   */
  public final static int ID        = 1;
  /**
   * Used to indicate a search of element names
   */
  public final static int NAME      = 2;
  /**
   * Used to indicate a search of element tags
   */
  public final static int TAG       = 3;
  /**
   * Used to indicate a search of element attributes
   */
  public final static int ATTR      = 4;
  /**
   * Used to indicate a search of a set of element attributes
   */
  public final static int AND_ATTRS = 5;
  /**
   * Used to indicate a search of any of a set of element attributes
   */
  public final static int OR_ATTRS  = 6;

  // the search pattern
  private Object searchObject = null;

  // the type of search
  private int searchType = 0;

  // root Subgraph for the search
  private Subgraph root = null;

  // types to search
  private int elemTypes = 0;

  // nextElement to return;
  private Element nextElem = null;
  
  // Elements that match the search criteria
  private Vector matches = null;

  // position of nextElem in Vector (if applicable)
  private int nextPos = -1;

  /**
   * Perform a search of elements in the supplied enumeration.
   *
   * @param elemEnum the enumeration of elements to be searched
   * @param searchType the type of search to perform
   * @param searchObject the search object to match against
   *
   * @see #ID
   * @see #NAME
   * @see #TAG
   * @see #ATTR
   * @see #AND_ATTRS
   * @see #OR_ATTRS
   */
  public Search(GraphEnumeration elemEnum, int searchType, Object searchObject) {
    if(elemEnum == null) {
      throw new IllegalArgumentException("GraphEnumeration cannot be null");
    }
    if(searchObject == null) {
      throw new IllegalArgumentException("searchObject cannot be null");
    }
    this.elemTypes = elemEnum.getEnumerationTypes();
    this.root = elemEnum.getSubgraphRoot();
    doSearch(elemEnum,searchType,searchObject);
  }
  
  /**
   * Perform a search of elements in the defined enumeration.
   * The enumeration to be searched is generated using the
   * <code>Subgraph</code>
   * <code>elements(int)</code> method.
   *
   * @param root the root subgraph in which to begin the search
   * @param elemTypes the type of graph elements to search
   * @param searchType the type of search to perform
   * @param searchObject the search object to match against
   *
   * @see Subgraph#elements(int)
   * 
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   *
   * @see #ID
   * @see #NAME
   * @see #TAG
   * @see #ATTR
   * @see #AND_ATTRS
   * @see #OR_ATTRS
   */
  public Search(Subgraph root, int elemTypes, int searchType, Object searchObject) {
    int type = (elemTypes&(Grappa.NODE|Grappa.EDGE|Grappa.SUBGRAPH));
    if(searchType != ID && (type == 0 || type != elemTypes)) {
      throw new IllegalArgumentException("Invalid element type specified");
    }
    if(searchObject == null) {
      throw new IllegalArgumentException("searchObject cannot be null");
    }
    this.elemTypes = elemTypes;
    this.root = root;
    doSearch(null,searchType,searchObject);
  }

  private void doSearch(GraphEnumeration elems, int searchType, Object searchObject) {
    Attribute candidate = null;
    Attribute[] attrs = null;
    Element elem = null;

    switch(searchType) {
    case ID:
      if(!(searchObject instanceof Long)) {
	throw new IllegalArgumentException("Object must be a Long when doing an ID search");
      }
      if(getSubgraphRoot() == null) {
	throw new IllegalArgumentException("Must supply Subgraph root when doing an ID search");
      }
      this.searchType = searchType;
      this.searchObject = searchObject;
      nextElem = getSubgraphRoot().getGraph().element4Id((Long)searchObject);
      break;
    case NAME:
      if(!(searchObject instanceof String)) {
	throw new IllegalArgumentException("Object must be a String when doing a NAME search");
      }
      this.searchType = searchType;
      this.searchObject = searchObject;
      String name = (String)searchObject;
      if(elems == null) {
	elems = getSubgraphRoot().elements(elemTypes);
      }
      elem = null;
      while(elems.hasMoreElements()) {
	elem = (Element)(elems.nextElement());
	if(name.equals(elem.getName())) {
	  if(nextElem == null) {
	    nextElem = elem;
	  } else {
	    if(matches == null) {
	      matches = new Vector(4,16);
	      nextPos = 0;
	    }
	    matches.addElement(elem);
	  }
	}
      }
      break;
    case TAG:
      if(!(searchObject instanceof String)) {
	throw new IllegalArgumentException("Object must be a String when doing a TAG search");
      }
      this.searchType = searchType;
      this.searchObject = searchObject;
      String tag = (String)searchObject;
      if(elems == null) {
	elems = getSubgraphRoot().elements(elemTypes);
      }
      elem = null;
      while(elems.hasMoreElements()) {
	elem = (Element)(elems.nextElement());
	if(elem.hasTag(tag)) {
	  if(nextElem == null) {
	    nextElem = elem;
	  } else {
	    if(matches == null) {
	      matches = new Vector(4,16);
	      nextPos = 0;
	    }
	    matches.addElement(elem);
	  }
	}
      }
      break;
    case ATTR:
      if(!(searchObject instanceof Attribute)) {
	throw new IllegalArgumentException("Object must be an Attribute when doing an ATTR search");
      }
      this.searchType = searchType;
      this.searchObject = searchObject;
      Attribute attr = (Attribute)searchObject;
      if(elems == null) {
	elems = getSubgraphRoot().elements(elemTypes);
      }
      elem = null;
      candidate = null;
      while(elems.hasMoreElements()) {
	elem = (Element)(elems.nextElement());
	if((candidate = elem.getLocalAttribute(attr.getName())) != null) {
	  if(attr.getValue() == null || candidate.getValue().equals(attr.getValue())) {
	    if(nextElem == null) {
	      nextElem = elem;
	    } else {
	      if(matches == null) {
		matches = new Vector(4,16);
		nextPos = 0;
	      }
	      matches.addElement(elem);
	    }
	  }
	}
      }
      break;
    case AND_ATTRS:
      if(!(searchObject instanceof Attribute[])) {
	throw new IllegalArgumentException("Object must be an Attribute[] when doing an AND_ATTR search");
      }
      this.searchType = searchType;
      this.searchObject = searchObject;
      attrs = (Attribute[])searchObject;
      if(attrs.length == 0) break;
      if(elems == null) {
	elems = getSubgraphRoot().elements(elemTypes);
      }
      elem = null;
      candidate = null;
      int i = 0;
      while(elems.hasMoreElements()) {
	elem = (Element)(elems.nextElement());
	for(i = 0; i < attrs.length; i++) {
	  if((candidate = elem.getLocalAttribute(attrs[i].getName())) == null) break;
	  if(attrs[i].getValue() != null && !candidate.getValue().equals(attrs[i].getValue())) break;
	}
	if(i == attrs.length) {
	  if(nextElem == null) {
	    nextElem = elem;
	  } else {
	    if(matches == null) {
	      matches = new Vector(4,16);
	      nextPos = 0;
	    }
	    matches.addElement(elem);
	  }
	}
      }
      break;
    case OR_ATTRS:
      if(!(searchObject instanceof Attribute[])) {
	throw new IllegalArgumentException("Object must be an Attribute[] when doing an OR_ATTR search");
      }
      this.searchType = searchType;
      this.searchObject = searchObject;
      attrs = (Attribute[])searchObject;
      if(attrs.length == 0) break;
      if(elems == null) {
	elems = getSubgraphRoot().elements(elemTypes);
      }
      elem = null;
      candidate = null;
      while(elems.hasMoreElements()) {
	elem = (Element)(elems.nextElement());
	for(i = 0; i < attrs.length; i++) {
	  if((candidate = elem.getLocalAttribute(attrs[i].getName())) == null) continue;
	  if(attrs[i].getValue() != null && !candidate.getValue().equals(attrs[i].getValue())) continue;
	  if(nextElem == null) {
	    nextElem = elem;
	  } else {
	    if(matches == null) {
	      matches = new Vector(4,16);
	      nextPos = 0;
	    }
	    matches.addElement(elem);
	  }
	  break;
	}
      }
      break;
    default:
      throw new IllegalArgumentException("Invalid search type specified");
    }
  }

  /**
   * Tests if this enumeration contains more elements.
   *
   * @return <code>true</code> if this enumeration contains more elements;
   * <code>false</code> otherwise.
   */
  public boolean hasMoreElements() {
    return (nextElem != null);
  }

  /**
   * Returns the next element of this enumeration. 
   *
   * @return the next element of this enumeration.
   * @exception NoSuchElementException  if no more elements exist.
   */
  public Object nextElement() throws NoSuchElementException {
    if(nextElem == null) {
      throw new NoSuchElementException("Search");
    }
    Element elem = nextElem;
    if(nextPos < 0) {
      nextElem = null;
    } else {
      nextElem = (Element)matches.elementAt(nextPos);
      if(++nextPos == matches.size()) {
	nextPos = -1;
      }
    }
    return elem;
  }

  /**
   * Get the search object for this <code>Search</code> instance.
   * @return the search object
   */
  public Object getSearchObject() {
    return searchObject;
  }

  /**
   * Get the search type for this <code>Search</code> instance.
   * @return the search type
   *
   * @see #ID
   * @see #NAME
   * @see #TAG
   * @see #ATTR
   * @see #AND_ATTRS
   * @see #OR_ATTRS
   */
  public int getSearchType() {
    return searchType;
  }

  /**
   * A convenience method that just returns a cast of a call to nextElement(). 
   *
   * @return the next graph element in the enumeration.
   * @exception NoSuchElementException whenever the enumeration has no more elements. 
   * @see #nextElement()
   */
  public Element nextGraphElement() throws NoSuchElementException {
    return (Element)nextElement();
  }

  /**
   * Get the root of this enumeration. 
   *
   * @return the root subgraph for this enumeration.
   */
  public Subgraph getSubgraphRoot() {
    return root;
  }

  /**
   * Get the types of elements possibly contained in this enumeration.
   *
   * @return an indication of the types of elements in this enumeration
   * @see Grappa#NODE
   * @see Grappa#EDGE
   * @see Grappa#SUBGRAPH
   */
  public int getEnumerationTypes() {
    return elemTypes;
  }

  /**
   * Search a graph for an element ID.
   *
   * @param graph the graph to be searched
   * @param id the element ID for which to search
   *
   * @return the matching graph element or null if no match.
   * @see Element#getId()
   */
  public static Element findById(Graph graph, Long id) {
    if(graph == null) return null;
    return graph.element4Id(id);
  }
}
