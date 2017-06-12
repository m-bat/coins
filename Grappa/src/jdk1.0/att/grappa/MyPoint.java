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

public class MyPoint extends Point
{
	public MyPoint() {
		super(0,0);
	}

	public MyPoint(int x, int y) {
		super(x,y);
	}

	public MyPoint(MyPoint p) {
		super(p.x,p.y);
	}
}
