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
import java.awt.image.ImageObserver;

/**
 * An interface for defining an image drawing method to be used for
 * painting the background of the graph.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public interface Backgrounder
{
  /**
   * The method for drawing the background image.
   *
   * @param imgOb an observer that may be interested in the status of the
   *              image to be drawn
   * @param gr the drawing graphics
   * @param drawPane the <code>DrawPane</code> object making requesting this method.
   */
  public void drawImage(ImageObserver imgOb, Graphics gr, DrawPane drawPane);
}
