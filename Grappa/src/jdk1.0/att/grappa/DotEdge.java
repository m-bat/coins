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
 * This class describes a dot edge.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class DotEdge extends DotElement
{
  /**
   * Initial capacity of the list of edge points.
   *
   * @see Vector
   */
  public final static int initialLineCapacity = 2;

  /*
   * end nodes 
   */
  private DotNode headNode;
  private String headPortId = null;
  private DotNode tailNode;
  private String tailPortId = null;

  private int arrowType = -1;

  /**
   * Use this constructor when creating an edge within a subgraph.
   *
   * @param subg the parent subgraph.
   * @param tail node anchoring the tail of the edge.
   * @param head node anchoring the head of the edge.
   */
  public DotEdge(DotSubgraph subg, DotNode tail, DotNode head) {
    this(subg,tail,null,head,null,subg.getGraph().getType().endsWith(Grappa.GRAPH_DIGRAPH));
  }

  /**
   * Use this constructor when creating an edge with ports within a subgraph.
   *
   * @param subg the parent subgraph.
   * @param tail node anchoring the tail of the edge.
   * @param tailPort the port to use within the tail node.
   * @param head node anchoring the head of the edge.
   * @param headPort the port to use within the head node.
   */
  public DotEdge(DotSubgraph subg, DotNode tail, String tailPort, DotNode head, String headPort) {
    this(subg,tail,tailPort,head,headPort,subg.getGraph().getType().endsWith(Grappa.GRAPH_DIGRAPH));
  }

  /**
   * Use this constructor when creating an edge with ports within a subgraph.
   * This constructor makes explicit the directedness of the edge.
   *
   * @param subg the parent subgraph.
   * @param tail node anchoring the tail of the edge.
   * @param tailPort the port to use within the tail node.
   * @param head node anchoring the head of the edge.
   * @param headPort the port to use within the head node.
   * @param directed for directed edge, set true; set false for undirected edge.
   */
  public DotEdge(DotSubgraph subg, DotNode tail, String tailPort, DotNode head, String headPort, boolean directed) {
    setSubgraph(subg);
    setGraph(subg.getGraph());
    setId(getGraph().nextEdgeId());
    tailNode = tail;
    if(tailPort != null) {
      tailPortId = new String(tailPort);
    }
    headNode = head;
    if(headPort != null) {
      headPortId = new String(headPort);
    }
    if(directed) {
      arrowType = Grappa.ARROW_LAST;
    } else {
      arrowType = Grappa.ARROW_NONE;
    }
    setName();
  }

  /**
   * Useful for testing the subclass type of a DotElement object.
   *
   * @return true if this object is a DotEdge.
   */
  public boolean isEdge() {
    return(true);
  }

  /**
   * Useful for distinguishing DotElement objects.
   * Implements the abstract DotElement method.
   *
   * @return the class variable constant Grappa.DOT_EDGE.
   * @see Grappa
   */
  public int typeFlag() {
    return(Grappa.DOT_EDGE);
  }

  /**
   * Generates and sets the name for this edge.
   * The generated name is the concatenation of tail node name,
   * the separator ">>", the head node name, the separator "##",
   * and the id of this edge Instance.
   * Also, takes the opportunity to add the edge to the subgraph and node
   * dictionaries.
   * Implements the abstract DotElement method.
   *
   * @see DotElement#getId()
   */
  public void setName() {
    String oldName = name;
    
    name = tailNode.getName() + ">>" + headNode.getName() + "##" + getId();

    // update subgraph node dictionary
    if(oldName != null) {
      getSubgraph().removeEdge(oldName);
    }
    getSubgraph().addEdge(this);
    tailNode.addEdge(this,false);
    headNode.addEdge(this,true);
  }

  public void free() {
    tailNode.removeEdge(this,false);
    headNode.removeEdge(this,true);
    tailNode = null;
    tailPortId = null;
    headNode = null;
    headPortId = null;
    super.free();
  }

  /**
   * Returns the node at the head end of the edge.
   *
   * @return the head node of the edge
   */
  public DotNode getHead() {
    return headNode;
  }

  /**
   * Returns the node at the tail end of the edge.
   *
   * @return the tail node of the edge
   */
  public DotNode getTail() {
    return tailNode;
  }

  /**
   * String representation of the edge.
   *
   * @return the string representation of the edge
   */
  public String toString() {
    String tail = null;
    String head = null;

    if(tailPortId == null) {
      tail = tailNode.toString();
    } else {
      tail = tailNode.toString() + ":" + canonString(tailPortId);
    }
    if(headPortId == null) {
      head = headNode.toString();
    } else {
      head = headNode.toString() + ":" + canonString(headPortId);
    }
    return(tail + arrowString() + head);
  }

  /*
   * convert the arrowType value into a string for output
   */
  private String arrowString() {
    String arrow = null;
    
    switch(arrowType) {
    case Grappa.ARROW_FIRST:
      arrow = " <- "; // should not occur
      break;
    case Grappa.ARROW_LAST:
      arrow = " -> ";
      break;
    case Grappa.ARROW_BOTH:
      arrow = " <-> "; // should not occur
      break;
    case Grappa.ARROW_NONE:
      arrow = " -- ";
      break;
    default:
      if(getGraph().getType().endsWith(Grappa.GRAPH_DIGRAPH)) {
	arrow = " -> ";
	arrowType = Grappa.ARROW_LAST;
      } else {
	arrow = " -- ";
	arrowType = Grappa.ARROW_NONE;
      }
      break;
    }
    return(arrow);
  }

  /**
   * Print the edge description to the provided stream.
   *
   * @param out the output stream for writing the description.
   */
  public void printEdge(PrintStream out) {
    this.printElement(out);
  }

  public static String computeEdge(DotNode head, DotNode tail, boolean directed) {
    String pos = null;
    
    Attribute attr1 = head.getAttribute("pos");
    Attribute attr2 = tail.getAttribute("pos");
    if(attr1 == null || attr2 == null) return "";

    String value = attr1.getValue();
    int idx = value.indexOf(',');
    FPoint headPos = new FPoint(
				Double.valueOf(value.substring(0,idx)).doubleValue(),
				Double.valueOf(value.substring(idx+1)).doubleValue()
				);
    value = attr2.getValue();
    idx = value.indexOf(',');
    FPoint tailPos = new FPoint(
				Double.valueOf(value.substring(0,idx)).doubleValue(),
				Double.valueOf(value.substring(idx+1)).doubleValue()
				);

    /*
    attr1 = head.getAttribute("width");
    attr2 = head.getAttribute("height");
    if(attr1 == null) attr1 = attr2;
    else if(attr2 == null) attr2 = attr1;
    if(attr1 == null) {
      attr1 = attr2 = new Attribute("width","0.5");
    }
    double wd = Double.valueOf(attr1.getValue()).doubleValue();
    double ht = Double.valueOf(attr2.getValue()).doubleValue();
    if(ht < wd) wd = ht;
    double headRad = wd * 0.5 * DrawObject.pointsPerInch;
    attr1 = head.getAttribute("width");
    attr2 = head.getAttribute("height");
    if(attr1 == null) attr1 = attr2;
    else if(attr2 == null) attr2 = attr1;
    if(attr1 == null) {
      attr1 = attr2 = new Attribute("width","0.5");
    }
    wd = Double.valueOf(attr1.getValue()).doubleValue();
    ht = Double.valueOf(attr2.getValue()).doubleValue();
    if(ht < wd) wd = ht;
    double tailRad = wd * 0.5 * DrawObject.pointsPerInch;
    */
    double headRad = 0;
    double tailRad = 0;

    double distance = Math.sqrt(
				(headPos.x - tailPos.x)*
				(headPos.x - tailPos.x)+
				(headPos.y - tailPos.y)*
				(headPos.y - tailPos.y)
				);
    if(distance < 1.0) distance = 1.0;

    FPoint pt0 = new FPoint(
			    headPos.x+(headRad*(tailPos.x - headPos.x)/distance),
			    headPos.y+(headRad*(tailPos.y - headPos.y)/distance)
			    );
    FPoint ptn = new FPoint(
			    tailPos.x+(tailRad*(headPos.x - tailPos.x)/distance),
			    tailPos.y+(tailRad*(headPos.y - tailPos.y)/distance)
			    );
    FPoint pt1 = new FPoint(
			    tailPos.x+(0.3333*(headPos.x - tailPos.x)),
			    tailPos.y+(0.3333*(headPos.y - tailPos.y))
			    );
    FPoint pt2 = new FPoint(
			    tailPos.x+(0.6777*(headPos.x - tailPos.x)),
			    tailPos.y+(0.6777*(headPos.y - tailPos.y))
			    );
    FPoint pt3 = new FPoint(
			    headPos.x+((0.02*distance+headRad)*(tailPos.x - headPos.x)/distance),
			    headPos.y+((0.02*distance+headRad)*(tailPos.y - headPos.y)/distance)
			    );

    if(directed) {
      pos = "e," +
	Math.round(pt0.x) + "," + Math.round(pt0.y) + " " +
	Math.round(ptn.x) + "," + Math.round(ptn.y) + " " +
	Math.round(pt1.x) + "," + Math.round(pt1.y) + " " +
	Math.round(pt2.x) + "," + Math.round(pt2.y) + " " +
	Math.round(pt3.x) + "," + Math.round(pt3.y);
    } else {
      pos =
	Math.round(ptn.x) + "," + Math.round(ptn.y) + " " +
	Math.round(pt1.x) + "," + Math.round(pt1.y) + " " +
	Math.round(pt2.x) + "," + Math.round(pt2.y) + " " +
	// Math.round(pt3.x) + "," + Math.round(pt3.y) + " " +
	Math.round(pt0.x) + "," + Math.round(pt0.y);
    }
    return pos;
  }
}
