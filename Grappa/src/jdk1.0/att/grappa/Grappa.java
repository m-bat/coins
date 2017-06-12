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

/**
 * This class provides a common set of constant, class variables
 * used by the classes in the grappa package.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public abstract class Grappa
{
  /**
   * Package prefix
   */
  public final static String PACKAGE_PREFIX = "att.grappa.";

  /*
   * edit operations
   */
  public final static int EDIT_UNDO              = 0;
  public final static int EDIT_CUT               = 1;
  public final static int EDIT_PASTE             = 2;
  public final static int EDIT_COPY              = 3;
  public final static int EDIT_DELETE            = 4;
  public final static int EDIT_ADD               = 5;

  /*
   * DotElement types
   */
  public final static int DOT_NODE               = 0;
  public final static int DOT_EDGE               = 1;
  public final static int DOT_GRAPH              = 2;

  /*
   * line types
   */
  public final static int DOT_LINE_SOLID         = 0;
  public final static int DOT_LINE_DASHED        = 1;
  public final static int DOT_LINE_DOTTED        = 2;

  /*
   * line types as strings
   */
  public final static String DOT_LINE_SOLID_STRING  = "solid";
  public final static String DOT_LINE_DASHED_STRING = "dashed";
  public final static String DOT_LINE_DOTTED_STRING = "dotted";

  // these are OR'ed together
  public final static int ARROW_NONE             = 0;
  public final static int ARROW_FIRST            = 1;
  public final static int ARROW_LAST             = 2;
  public final static int ARROW_BOTH             = 3;

  // graph types
  public final static String GRAPH_GRAPH         = "graph";
  public final static String GRAPH_STRICT_GRAPH  = "strict graph";
  public final static String GRAPH_DIGRAPH       = "digraph";
  public final static String GRAPH_STRICT_DIGRAPH= "strict digraph";

  // for anonymous subgraphs
  public final static String ANONYMOUS_PREFIX    = "_anonymous_";

  private static String VbarSide = "West";
  private static String HbarSide = "South";

  public static String setVbarSize(boolean westSide) {
    String oldSide = VbarSide;
    if(westSide) VbarSide = "West";
    else VbarSide = "East";
    return oldSide;
  }

  public static String setHbarSize(boolean topSide) {
    String oldSide = HbarSide;
    if(topSide) HbarSide = "North";
    else HbarSide = "South";
    return oldSide;
  }

  public static String getVbarSide() {
    return VbarSide;
  }

  public static String getHbarSide() {
    return HbarSide;
  }

  private static Color selectColor = GraphicContext.getColor("red",null);
  private static Color selectFontcolor = GraphicContext.getColor("white",null);
  private static Color deleteColor = GraphicContext.getColor("grey85",null);

  public static Color getSelectColor() {
    return selectColor;
  }

  public static Color setSelectColor(String color) {
    Color oldColor = selectColor;
    selectColor = GraphicContext.getColor(color,oldColor);
    return oldColor;
  }

  public static Color getSelectFontcolor() {
    return selectFontcolor;
  }

  public static Color setSelectFontcolor(String color) {
    Color oldColor = selectFontcolor;
    selectFontcolor = GraphicContext.getColor(color,oldColor);
    return oldColor;
  }

  public static Color getDeleteColor() {
    return deleteColor;
  }

  public static Color setDeleteColor(String color) {
    Color oldColor = deleteColor;
    deleteColor = GraphicContext.getColor(color,oldColor);
    return oldColor;
  }
}
