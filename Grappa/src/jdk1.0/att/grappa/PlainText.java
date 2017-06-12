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

public class PlainText extends DrawNode
{
  protected static GraphicContext selectGc = null;
  
  public PlainText() {
    //super();
    peripheries = 0;
    shape = DrawNode.TEXT;
  }

  public void highlightSelect() {
    if(PlainText.selectGc == null) {
      PlainText.selectGc = new GraphicContext();
      PlainText.selectGc.setFontcolor(Grappa.getSelectColor());
    }
    PlainText.selectGc.setForeground(gc.getForeground());
    PlainText.selectGc.setFillMode(gc.getFillMode());
    PlainText.selectGc.setBackground(gc.getBackground());
    PlainText.selectGc.setFont(gc.getFont());
    PlainText.selectGc.setXORColor(gc.getXORColor());
    PlainText.selectGc.setXORMode(gc.getXORMode());
    PlainText.selectGc.setClipRect(gc.getClipRect());
    PlainText.selectGc.setLineStyle(gc.getLineStyle());
    draw(PlainText.selectGc,null,true);
  }
}
