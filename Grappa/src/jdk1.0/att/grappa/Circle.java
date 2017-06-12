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

public class Circle extends Ellipse
{
  public Circle() {
    //super();
    regular = true;
  }

  protected void setDefaultDrawAttributes() {
    if(appObject == null) return;

    Attribute oldAttr = null;

    super.setDefaultDrawAttributes();

    oldAttr = getElement().getAttribute("width");
    if(oldAttr == null) {
      oldAttr = getElement().getAttribute("height");
      if(oldAttr == null) {
	oldAttr = getElement().setAttribute(new Attribute("width","0.75"));
	oldAttr = getElement().setAttribute(new Attribute("height","0.75"));
      } else {
	oldAttr = getElement().setAttribute(new Attribute("width",oldAttr.getValue()));
      }
    } else {
      oldAttr = getElement().setAttribute(new Attribute("height",oldAttr.getValue()));
    }

    return;
  }
}
