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
import java.awt.event.*;

/**
 * The ElementTipper provides what is commonly called a ToolTip.
 * When the cursor remains motionless for a brief time over an element
 * in a pane and that element has an attribute called "tip", then the
 * value of the "tip" attribute is briefly displayed at the cursor
 * location.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class ElementTipper implements Runnable, MouseListener, MouseMotionListener
{
  /*
   * Number of milliseconds in advance of true wake up time that activity
   * is permitted (to account for processing delays (approximately))
   */
  private static final int LEAD_TIME = 3;
  private long time;
  private Thread thread = null;
  private long wakeup;
  private boolean isDisarmed = false;
  private boolean isShowing = false;
  private Point pt = null;
  private Point activePt = null;
  private GraphicContext gc = new GraphicContext();
  private DrawPane pane = null;
  private int cursorHeight = 18;
  private Cursor origCursor = null;
  private int canvasCursor = Cursor.DEFAULT_CURSOR;
  


  /**
   * Construct an <code>ElementTipper</code> for the given pane using the given
   * timeout value.
   *
   * @param pane the <code>DrawPane</code> where the tipper is to be active
   * @param time the minimum time in milliseconds that the cursor needs to
   *             remain motionless for the tip to appear
   */
  public ElementTipper(DrawPane pane, int time) {
    this.pane = pane;
    if(time <= LEAD_TIME) time = LEAD_TIME+1;
    this.time = time;
    thread = new Thread(this);
    gc.setFont(GraphicContext.fontFromCache("sanserif",Font.PLAIN,12));
    gc.setBackground("lightgoldenrodyellow");
    // gc.setForeground("black"); // the default, so no need to set
    pane.getCanvas().addMouseListener(this);
    pane.getCanvas().addMouseMotionListener(this);
    origCursor = pane.getCanvas().getCursor();
  }

  private DrawPane getDrawPane() {
    return pane;
  }

  public void finalize() {
    if(thread != null) {
      thread.destroy();
    }
  }

  /**
   * Get the graphic context used for drawing the tip.
   *
   * @return the graphic context for this tip
   */
  public GraphicContext getGC() {
    return gc;
  }

  /**
   * The run method for the ElementTipper thread.  Mostly, this thread sleeps.
   * However, after at most <code>time</code> milliseconds, it will wake up,
   * clear any currently displayed tip text, check if it has moved since the
   * last time it displayed a tip.  If not, it goes back to sleep, otherwise
   * it checks if it has been  at least <code>time</code> milliseconds since
   * the cursor was last moved.  If the cursor has not moved for that time,
   * then it checks if the current point is within a graph element.  If it is,
   * then it checks if the element has a tip attribute and if so displays it.
   * Then it goes back to sleep for <code>time</code> milliseconds.
   */ 
  public void run() {
    long delta = time;
    while(true) {
      try {
	thread.sleep(delta);
      } catch(InterruptedException ex) {
      }
      // clearTip();
      if(isDisarmed) {
	delta = time;
	continue;
      }
      if(pt == activePt) continue;
      delta = wakeup - System.currentTimeMillis();
      if(delta < LEAD_TIME) {
	delta = time;
	// check if pt is in element
	activePt = pt;
	Element elt = null;
	try {
	  elt = getDrawPane().findContainingElement(pt);
	}
	catch(IllegalStateException ex) {
	  activePt = null;
	  continue;
	}
	catch(NullPointerException np) {
	  activePt = null;
	  continue;
	}
	if(elt != null) {
	  String tip = elt.getAttributeValue("tip");
	  if(tip != null) {
	    isShowing = true;
	    // compute and draw rectangle with tip
	    Graphics gr = getDrawPane().getCanvasGraphics();
	    gr.setFont(gc.getFont());
	    FontMetrics fm = gr.getFontMetrics();
	    int width = fm.stringWidth(tip) + 8;
	    int height = fm.getHeight() + 4;
	    Point sp = getDrawPane().getScrollPosition();
	    Dimension ps = getDrawPane().getViewportSize();
	    Dimension cs = getDrawPane().getCanvas().getSize();
	    int x = sp.x;
	    cs.width -= x;
	    if(cs.width > ps.width) cs.width = ps.width;
	    if(width < cs.width) {
	      if((cs.width - width - (pt.x-sp.x)) > 0) {
		x = pt.x;
	      } else {
		x = sp.x + cs.width - width;
	      }
	    }
	    int y = sp.y;
	    cs.height -= y;
	    if(cs.height > ps.height) cs.height = ps.height;
	    if(height < cs.height) {
	      if((cs.height - height - (pt.y + cursorHeight - sp.y)) > 0) {
		y = pt.y + cursorHeight;
	      } else {
		y = sp.y + cs.height - height;
	      }
	    }
	    gc.setClipRect(x,y,width,height);
	    gr.setClip(gc.getClipRect());
	    int w = width - 1;
	    int h = height - 1;
	    gr.setColor(gc.getBackground());
	    gr.fillRect(x,y,w,h);
	    gr.setColor(gc.getForeground());
	    gr.drawRect(x,y,w,h);
	    gr.drawString(tip,x+4,y+h-5);
	    delta = time + time;
	  }
	}
      }
    }
  }

  /**
   * When the mouse is clicked, clear the tip (if showing).
   *
   * @param evt the mouse event that caused this method to be called
   */
  public void mouseClicked(MouseEvent evt) {
    clearTip();
  }

  /**
   * When the mouse is pressed, clear the tip (if showing) and
   * disarm the tip display mechanism.
   *
   * @param evt the mouse event that caused this method to be called
   */
  public void mousePressed(MouseEvent evt) {
    isDisarmed = true;
    clearTip();
  }

  /**
   * When the mouse is released, re-arm the tip display mechanism and
   * reset the wake-up time.
   *
   * @param evt the mouse event that caused this method to be called
   */
  public void mouseReleased(MouseEvent evt) {
    isDisarmed = false;
    //wakeup = evt.getWhen() + time;
    wakeup = System.currentTimeMillis() + time;
  }

  /**
   * When the mouse is enters the canvas, behave as if the mouse moved and
   * either resume or start the thread as needed.
   *
   * @param evt the mouse event that caused this method to be called
   *
   * @see #mouseMoved(MouseEvent)
   */
  public void mouseEntered(MouseEvent evt) {
    getDrawPane().getCanvas().setCursor(Cursor.getPredefinedCursor(canvasCursor));
    mouseMoved(evt);
    if(thread.isAlive()) thread.resume();
    else thread.start();
  }

  /**
   * When the mouse is leaves the canvas, suspend the thread and clear the tip.
   *
   * @param evt the mouse event that caused this method to be called
   */
  public void mouseExited(MouseEvent evt) {
    thread.suspend();
    clearTip();
    getDrawPane().getCanvas().setCursor(origCursor);
  }

  private void clearTip() {
    if(isShowing) {
      isShowing = false;
      Rectangle rect = gc.getClipRect();
      getDrawPane().getCanvas().repaint(rect.x,rect.y,rect.width,rect.height);
    }
  }

  /**
   * Whenever the mouse moves, reset the wake-up time, reset set the current
   * point and clear the tip.
   *
   * @param evt the mouse event that caused this method to be called
   */
  public void mouseMoved(MouseEvent evt) {
    //wakeup = evt.getWhen() + time;
    wakeup = System.currentTimeMillis() + time;
    pt = evt.getPoint();
    activePt = null;
    clearTip();
  }

  /**
   * Behave as with <code>mouseMoved</code>
   *
   * @param evt the mouse event that caused this method to be called
   *
   * @see #mouseMoved(MouseEvent)
   */
  public void mouseDragged(MouseEvent evt) {
    mouseMoved(evt);
  }

  /**
   * Set the delay time before tip is searched for and displayed.
   *
   * @param time the minimum time in milliseconds that the cursor needs to
   *             remain motionless for the tip to appear
   *
   * @return the previous delay time
   */
  public int setTipTime(int time) {
    int old = time;
    if(time <= LEAD_TIME) time = LEAD_TIME + 1;
    this.time = time;
    return old;
  }
}
