/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */
/*
 * for backwards compatability kludge
 */

package att.grappa;

import java.awt.*;

public class MyRectangle extends Rectangle
{
  public MyRectangle() {
    super(0,0,0,0);
  }

  public MyRectangle(int x, int y, int w, int h) {
    super(x,y,w,h);
  }

  public MyRectangle(Rectangle r) {
    super(r.x,r.y,r.width,r.height);
  }

  public Dimension getSize() {
    return new Dimension(width,height);
  }

  public MyRectangle getBounds() {
    return this;
  }

  public void setBounds(int x, int y, int w, int h) {
    reshape(x,y,w,h);
  }

  public void setBounds(MyRectangle r) {
    reshape(r.x,r.y,r.width,r.height);
  }

  public void setSize(int w, int h) {
    resize(w,h);
  }

  public boolean contains(int x, int y) {
    return inside(x,y);
  }

  public boolean contains(Point p) {
    return inside(p.x, p.y);
  }

  public MyRectangle intersection(MyRectangle rect) {
    if(rect == null) return null;
    Rectangle r1 = (Rectangle)this;
    Rectangle r2 = (Rectangle)rect;
    Rectangle r3 = r1.intersection(r2);
    MyRectangle r4 = new MyRectangle(r3.x,r3.y,r3.width,r3.height);
    return r4;
  }
}
