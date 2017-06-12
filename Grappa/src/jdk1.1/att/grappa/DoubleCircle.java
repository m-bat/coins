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
 * The DrawObject to use for rendering nodes whose shape is a doublecircle.
 * This class is a convenience and is equivalent to a Circle object with a
 * peripheries attribute of 2.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class DoubleCircle extends Circle
{
  /**
   * Construct a double-circle-shaped <code>DrawObject</code> for nodes.
   */
  public DoubleCircle() {
    //super();
    peripheries = 2;
  }
}
