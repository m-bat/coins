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

/**
 * This class provides a common set of constant, class variables
 * used by the classes in the grappa package.  Some convenience
 * methods for exception display are also included.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public abstract class Grappa
{
  /**
   * NOTE: this class is abstract, so the constructor should not be used directly.
   */
  public Grappa() {
  }
  
  /**
   * Package prefix
   */
  public final static String PACKAGE_PREFIX = "att.grappa.";

  /**
   * Package name as an up-low string.
   */
  public final static String PKG_UPLOW = "Grappa";
  /**
   * Package name as an upper-case string (as a convenience).
   */
  public final static String PKG_UPPER = "GRAPPA";
  /**
   * Package name as an lower-case string (as a convenience).
   */
  public final static String PKG_LOWER = "grappa";

  /**
   * The new-line string for this system
   */
  public final static String NEW_LINE = System.getProperty("line.separator");

  /*
   * edit operations (NOT IMPLEMENTED YET)
  public final static int EDIT_UNDO              = 0;
  public final static int EDIT_CUT               = 1;
  public final static int EDIT_PASTE             = 2;
  public final static int EDIT_COPY              = 3;
  public final static int EDIT_DELETE            = 4;
  public final static int EDIT_ADD               = 5;
   */

  /**
   * Element type value indicating a node.
   */
  public final static int NODE               = 1;
  /**
   * Element type value indicating an edge.
   */
  public final static int EDGE               = 2;
  /**
   * Element type value indicating a graph (or subgraph).
   */
  public final static int SUBGRAPH           = 4;

  /**
   * Maximum number of bits needed to represet the Element types.
   * Element type is merged with the element id number (a sequentially
   * assigned number) to ensure a unique identifier (within an invocation of
   * the package).
   *
   */
  public final static int TYPES_SHIFT            = 3;

  /**
   * Integer value for indicating a solid line.
   */
  public final static int LINE_SOLID         = 0;
  /**
   * Integer value for indicating a dashed line.
   */
  public final static int LINE_DASHED        = 1;
  /**
   * Integer value for indicating a dotted line.
   */
  public final static int LINE_DOTTED        = 2;
  /**
   * Default line width value.
   */
  public final static int LINE_WIDTH         = 1;

  /**
   * String value indicating a solid line style.
   */
  public final static String LINE_SOLID_STRING  = "solid";
  /**
   * String value indicating a dashed line style.
   */
  public final static String LINE_DASHED_STRING = "dashed";
  /**
   * String value indicating a dotted line style.
   */
  public final static String LINE_DOTTED_STRING = "dotted";
  /**
   * String value used when indicating the line width style value.
   */
  public final static String LINE_WIDTH_STRING = "linewidth";

  /*
   * arrow types (these can be OR'ed together)
   */
  /**
   * No arrow indicator.
   */
  public final static int ARROW_NONE             = 0;
  /**
   * Indicator for arrow at first point.
   */
  public final static int ARROW_FIRST            = 1;
  /**
   * Indicator for arrow at last point.
   */
  public final static int ARROW_LAST             = 2;
  /**
   * Indicator for arrow at both ends.
   */
  public final static int ARROW_BOTH             = 3;

  /**
   * Name prefix for name generation of unnamed subgraphs
   */
  public final static String ANONYMOUS_PREFIX    = "_anonymous_";

  private static final ExceptionDisplay exceptionDisplay = new ExceptionDisplay(Grappa.PKG_UPLOW + ":  Exception Detected");

  /**
   * Boolean for enabling/disabling exception pop-up window display.
   */
  public static boolean doDisplayException = true;

  /**
   * Method for displaying an exception in a pop-up window (if enabled).
   * @param ex The exception value about which information is to be displayed.
   * @see Grappa#doDisplayException
   * @see DisplayException
   */
  public static void displayException(java.lang.Exception ex) {
    if(doDisplayException) exceptionDisplay.displayException(ex);
  }

  /**
   * Method for displaying an exception in a pop-up window (if enabled).
   * @param ex The exception value about which information is to be displayed.
   * @param msg Additional text to be displayed ahead of exception info.
   * @see Grappa#doDisplayException
   * @see DisplayException
   */
  public static void displayException(java.lang.Exception ex, java.lang.String msg) {
    if(doDisplayException) exceptionDisplay.displayException(ex,msg);
  }

  /**
   * A convenience Vector useful when an enumeration is to be returned, but
   * the object to be enumerated is null (in which case, the return value can
   * be <I>Grappa.emptyEnumeration.elements()</I>, whose <I>hasMoreElements()</I> method
   * will return <B>false</B>).
   */
  public static final java.util.Vector emptyEnumeration = new java.util.Vector(0,0);
}
