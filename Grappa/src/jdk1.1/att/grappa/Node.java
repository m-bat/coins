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
import java.io.*;
import java.awt.*;


/**
 * This class describes a node.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Node extends Element
{
  /**
   * Default node name prefix used by setName().
   *
   * @see Node#setName()
   */
  public final static String defaultNamePrefix = "N";

  private Vector inEdges = null;
  private Vector outEdges = null;

  private Vector Ports = null;

  /**
   * Use this constructor when creating a node within a subgraph.
   *
   * @param subg the parent subgraph.
   * @param name the name of this node.
   */
  public Node(Subgraph subg, String name) {
    super(Grappa.NODE,subg);
    setName(name);
  }

  /**
   * Use this constructor when creating a node within a subgraph
   * with an automatically generated name.
   *
   * @param subg the parent subgraph.
   * @see Node#setName()
   */
  public Node(Subgraph subg) {
    this(subg,(String)null);
  }

  // override Element methods

  /**
   * Check if this element is a node.
   * Useful for testing the subclass type of an Element object.
   *
   * @return true if this object is a Node.
   */
  public boolean isNode() {
    return(true);
  }

  /**
   * Get the type of this element.
   * Useful for distinguishing among Element objects.
   *
   * @return the class variable constant Grappa.NODE
   * @see Grappa#NODE
   */
  public int getType() {
    return(Grappa.NODE);
  }

  /**
   * Generates and sets the name for this node.
   * The generated name is the concatenation of Node.defaultNamePrefix
   * with the numeric id of this node instance.
   *
   * @see Node#defaultNamePrefix
   * @see Element#getId()
   */
  void setName() {
    String oldName = name;
    
    while(true) {
      name = Node.defaultNamePrefix + getId() + "_" + System.currentTimeMillis();
      if(getGraph().findNodeByName(name) == null) {
	break;
      }
    }

    // update subgraph node dictionary
    if(oldName != null) {
      getSubgraph().removeNode(oldName);
    }
    getSubgraph().addNode(this);
  }

  /**
   * Sets the node name to the supplied argument.
   * When the argument is null, setName() is called.
   *
   * @exception IllegalArgumentException when newName is not unique.
   * @param newName the new name for the node.
   * @see Node#setName()
   */
  void setName(String newName) throws IllegalArgumentException {
    if(newName == null) {
      setName();
      return;
    }

    String oldName = name;
    
    // test if name is the same as the old name (if any)
    if(oldName != null && oldName.equals(newName)) {
      return;
    }

    // is name unique?
    if(getGraph().findNodeByName(newName) != null) {
      throw new IllegalArgumentException("node name (" + newName + ") is not unique");
    }

    // update subgraph node dictionary
    if(oldName != null) {
      getSubgraph().removeNode(oldName);
    }
    name = newName;
    getSubgraph().addNode(this);
  }

  /**
   * Add the given edge to this node's inEdges or outEdges dictionaries,
   * if it is not already there.
   * The boolean indicates whether the edge terminates at (inEdge) or
   * emanates from (outEdge) the node.
   *
   * @param edge the edge to be added to this node's dictionary.
   * @param inEdge if set true, add to inEdges dictionary otherwise add
   *               to outEdges dictionary.
   * @see Edge
   */
  synchronized public void addEdge(Edge edge, boolean inEdge) {
    if(edge == null) return;
    if(inEdge) {
      if(inEdges == null) {
	inEdges = new Vector();
      }
      if(!inEdges.contains(edge)) {
	inEdges.addElement(edge);
      }
    } else {
      if(outEdges == null) {
	outEdges = new Vector();
      }
      outEdges.addElement(edge);
      if(!outEdges.contains(edge)) {
	outEdges.addElement(edge);
      }
    }
  }

  /**
   * Find an outbound edge given its head and key.
   *
   * @param head the Node at the head of the edge
   * @param key the key String associated with the edge
   *
   * @return the matching edge or null
   */
  public Edge findOutEdgeByKey(Node head, String key) {
    if(head == null || key == null || outEdges == null) {
      return null;
    }
    Edge edge = null;
    for(int i = 0; i < outEdges.size(); i++) {
      edge = (Edge)(outEdges.elementAt(i));
      if(head == edge.getHead() && key.equals(edge.getKey())) {
	return edge;
      }
    }
    return null;
  }

  /**
   * Find an inbound edge given its tail and key.
   *
   * @param tail the Node at the tail of the edge
   * @param key the key String associated with the edge
   *
   * @return the matching edge or null
   */
  public Edge findInEdgeByKey(Node tail, String key) {
    if(tail == null || key == null || inEdges == null) {
      return null;
    }
    Edge edge = null;
    for(int i = 0; i < inEdges.size(); i++) {
      edge = (Edge)(inEdges.elementAt(i));
      if(tail == edge.getTail() && key.equals(edge.getKey())) {
	return edge;
      }
    }
    return null;
  }

  /**
   * Remove the given edge from this node's inEdges or outEdges dictionaries.
   * The boolean indicates whether the edge terminates at (inEdge) or
   * emanates from (outEdge) the node.
   *
   * @param edge the edge to be removed from this node's dictionary.
   * @param inEdge if set true, remove from inEdges dictionary otherwise
   *               remove from outEdges dictionary.
   * @see Edge
   */
  synchronized public void removeEdge(Edge edge, boolean inEdge) {
    if(edge == null) return;
    if(inEdge) {
      if(inEdges == null) return;
      inEdges.removeElement(edge);
    } else {
      if(outEdges == null) return;
      outEdges.removeElement(edge);
    }
  }

  /**
   * Print the node description to the provided stream.
   *
   * @param out the output text stream for writing the description.
   */
  public void printNode(PrintWriter out) {
    this.printElement(out);
  }

  /**
   * Get an Enumeration of the edges directed to or from this node.
   *
   * @return an Enumeration of all the edges (in or out) associated with this node.
   */
  public Enumeration edgeElements() {
    return new Enumerator(inEdges, outEdges);
  }

  /**
   * Get an Enumeration of the edges directed to this node.
   *
   * @return an Enumeration of all the inbound edges associated with this node.
   */
  public Enumeration inEdgeElements() {
    return new Enumerator(inEdges, null);
  }

  /**
   * Get an Enumeration of the edges directed from this node.
   *
   * @return an Enumeration of all the outbound edges associated with this node.
   */
  public Enumeration outEdgeElements() {
    return new Enumerator(null, outEdges);
  }

  class Enumerator implements Enumeration {
    int inCnt = 0;
    int outCnt = 0;
    Vector inEdges = null;
    Vector outEdges = null;

    Enumerator(Vector inEdges, Vector outEdges) {
      inCnt = (inEdges == null) ? 0 : inEdges.size();
      outCnt = (outEdges == null) ? 0 : outEdges.size();
      this.inEdges = inEdges;
      this.outEdges = outEdges;
    }
  
    public boolean hasMoreElements() {
      int tmp;
      if(inCnt > 0 && inCnt > (tmp = inEdges.size())) inCnt = tmp;
      if(outCnt > 0 && outCnt > (tmp = outEdges.size())) outCnt = tmp;
      return((inCnt+outCnt) > 0);
    }

    public Object nextElement() {
      synchronized (Node.this) {
	int tmp;
      	if(inCnt > 0 && inCnt > (tmp = inEdges.size())) inCnt = tmp;
	if(inCnt > 0) {
	  return inEdges.elementAt(--inCnt);
	}
      	if(outCnt > 0 && outCnt > (tmp = outEdges.size())) outCnt = tmp;
	if(outCnt > 0) {
	  return outEdges.elementAt(--outCnt);
	}
	throw new NoSuchElementException("Node$Enumerator");
      }
    }
  }
}
