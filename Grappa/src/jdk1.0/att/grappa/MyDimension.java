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

public class MyDimension extends Dimension
{
	public MyDimension() {
		super(0,0);
	}

	public MyDimension(int w, int h) {
		super(w,h);
	}

	public MyDimension(MyDimension d) {
		super(d.width, d.height);
	}

	public MyDimension(Dimension d) {
		super(d.width, d.height);
	}

	public MyDimension getSize() {
		return this;
	}

	public void setSize(Dimension d) {
		width = d.width;
		height = d.height;
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
	}
}
