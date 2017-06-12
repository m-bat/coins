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

/**
 * This class provides general-purpose, non-essential methods.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Utilities {
  /**
   * Filter the supplied graph using the given connector.
   * The connector is either a java.lang.Process or a
   * java.net.URLConnection.
   * As such, it provides an output stream to which the graph can be
   * written and an input stream from which the processed graph can be
   * read back in (to replace the original graph).
   * Such filtering is useful for processing the graph through a layout
   * engine such as the <i>dot</i> program.
   *
   * @param graph the graph to be processed
   * @param connector a Process or URLConnector that provides an input and
   *                  output stream
   * @return true if the filtering completed successfully, false otherwise.
   */
  public static boolean filterGraph(Graph graph, Object connector) {
    return filterGraph(graph,connector,null);
  }

  /**
   * Filter the supplied graph using the given connector.
   * The connector is either a java.lang.Process or a
   * java.net.URLConnection.
   * As such, it provides an output stream to which the graph can be
   * written and an input stream from which the processed graph can be
   * read back in (to replace the original graph).
   * Such filtering is useful for processing the graph through a layout
   * engine such as the <i>dot</i> program.
   *
   * @param graph the graph to be processed
   * @param connector a Process or URLConnector that provides an input and
   *                  output stream
   * @param preamble if not null, a string sent to filter prior to graph
   * @return true if the filtering completed successfully, false otherwise.
   */
  public static boolean filterGraph(Graph graph, Object connector, String preamble) {
    if(connector == null) return false;
    OutputStream toFilterRaw = null;
    try {
      if(connector instanceof java.lang.Process) {
	toFilterRaw = ((java.lang.Process)connector).getOutputStream();
      } else if(connector instanceof java.net.URLConnection) {
	toFilterRaw = ((java.net.URLConnection)connector).getOutputStream();
      } else {
	return false;
      }
    } catch(IOException ioex) {
      Grappa.displayException(ioex);
      return false;
    }
    PrintWriter toFilter = new PrintWriter(toFilterRaw);
    String content = null;
    boolean status = true;
    try {
      StringWriter theGraph = new StringWriter();
      graph.printGraph(theGraph);
      theGraph.flush();
      content = theGraph.toString();
      theGraph.close();
    } catch(Exception ex) {
      Grappa.displayException(ex);
      return false;
    }
    try {
      if(preamble != null) {
	toFilter.println(preamble);
	toFilter.flush();
      }
      toFilter.print(content);
      toFilter.flush();
      toFilter.close();
    } catch(Exception ex) {
      Grappa.displayException(ex);
      return false;
    }
    InputStream fromFilterRaw = null;
    try {
      if(connector instanceof java.lang.Process) {
	fromFilterRaw = ((java.lang.Process)connector).getInputStream();
      } else if(connector instanceof java.net.URLConnection) {
	fromFilterRaw = ((java.net.URLConnection)connector).getInputStream();
      } else {
	return false;
      }
    } catch(IOException ioex) {
      Grappa.displayException(ioex);
      return false;
    }
    BufferedReader fromFilter = new BufferedReader(new InputStreamReader(fromFilterRaw));
    StringBuffer newGraph = new StringBuffer(content.length() + 128);
    try {
      String line = null;
      while((line = fromFilter.readLine()) !=  null) {
	newGraph.append(line);
	// assume a lone right-brace on a line is the end-of-graph

	if(line.equals("}") || line.equals("}\r")) {
	  break;
	}
	/*
	 * Need to append new-line on the chance that there was a
	 * backslash-newline (otherwise need to test for a lone
	 * backslash at the end of the string and remove it...
	 * cheaper to just append a newline.
	 */
	newGraph.append(Grappa.NEW_LINE);
      }
    } catch(Exception ex) {
      Grappa.displayException(ex);
      status = false;
      if(newGraph.length() == 0) {
	newGraph.append(content);
	content = null;
      }
    }
    try {
      fromFilter.close();
    } catch(IOException io) {}
    Reader fromReader = null;
    try {
      fromReader = new StringReader(newGraph.toString());
    } catch(Exception ex) {
      Grappa.displayException(ex);
      return false;
    }
    Parser program = new Parser(fromReader,graph.getErrorWriter(),graph);
    try {
      program.parse();
    } catch(Exception ex) {
      Grappa.displayException(ex);
      status = false;
      try {
	fromReader.close();
	fromReader = new StringReader(content);
      } catch(Exception ex2) {
	Grappa.displayException(ex2);
	return false;
      }
      program = new Parser(fromReader,graph.getErrorWriter(),graph);
      try {
	program.parse();
      } catch(Exception ex2) {
	Grappa.displayException(ex2);
	return false;
      }
    }
    try {
      graph.buildObjects();
    } catch(InstantiationException ex) {
      Grappa.displayException(ex);
      DrawPane.clearGraph(graph);
      return false;
    }
    DrawPane.clearGraph(graph);
    DrawPane.drawGraph(graph);
    return status;
  }

  /**
   * Compute the line running between the supplied nodes as a string sutiable
   * for use as an edge "pos" attribute.  The line runs from the center point
   * (as given by the "pos" attribute) of the supplied tail node to the center
   * point of the supplied head node. The string format depends on whether the
   * edge is a directed edge or not.
   * 
   * @param head the ending node for the line
   * @param tail the starting node for the line
   * @param directed true if the output should be formatted for a directed
   *                 edge; false otherwise.
   * @return a string in edge "pos" attribute format or an empty string if
   *         there is any problems formatting the string.
   */
  public static String computeEdgePos(Node head, Node tail, boolean directed) {
    String pos = null;
    
    Attribute attr1 = head.getAttribute("pos");
    Attribute attr2 = tail.getAttribute("pos");
    if(attr1 == null || attr2 == null) return "";

    java.awt.Point tmpPt = null;
    try {
      tmpPt = DrawObject.pointForTuple(attr1.getValue());
    } catch(Exception ex) {
      return "";
    }
    DoublePoint headPos = new DoublePoint(tmpPt);
    try {
      tmpPt = DrawObject.pointForTuple(attr2.getValue());
    } catch(Exception ex) {
      return "";
    }
    DoublePoint tailPos = new DoublePoint(tmpPt);

    double headRad = 0;
    double tailRad = 0;

    double distance = Math.sqrt(
				(headPos.x - tailPos.x)*
				(headPos.x - tailPos.x)+
				(headPos.y - tailPos.y)*
				(headPos.y - tailPos.y)
				);
    if(distance < 1.0) distance = 1.0;

    DoublePoint pt0 = new DoublePoint(
			    headPos.x+(headRad*(tailPos.x - headPos.x)/distance),
			    headPos.y+(headRad*(tailPos.y - headPos.y)/distance)
			    );
    DoublePoint ptn = new DoublePoint(
			    tailPos.x+(tailRad*(headPos.x - tailPos.x)/distance),
			    tailPos.y+(tailRad*(headPos.y - tailPos.y)/distance)
			    );
    DoublePoint pt1 = new DoublePoint(
			    tailPos.x+(0.3333*(headPos.x - tailPos.x)),
			    tailPos.y+(0.3333*(headPos.y - tailPos.y))
			    );
    DoublePoint pt2 = new DoublePoint(
			    tailPos.x+(0.6777*(headPos.x - tailPos.x)),
			    tailPos.y+(0.6777*(headPos.y - tailPos.y))
			    );
    DoublePoint pt3 = new DoublePoint(
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
