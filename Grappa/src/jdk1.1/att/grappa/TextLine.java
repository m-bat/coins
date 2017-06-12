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

class TextLine
{
  GraphicContext gc = null;
  String line = null;
  Dimension size = new Dimension();
  int justify = TextLabel.JUSTIFY_LEFT|TextLabel.JUSTIFY_MIDDLE;
  Rectangle bbox = new Rectangle();

  TextLine(String text, int just, GraphicContext context) {
    gc = context;
    line = text;
    justify = just;
    if((justify&(TextLabel.JUSTIFY_TOP|TextLabel.JUSTIFY_MIDDLE|TextLabel.JUSTIFY_BOTTOM)) == 0) {
      justify |= TextLabel.JUSTIFY_MIDDLE;
    }
    computeSize();
  }

  public void setBounds(int x, int y, int width, int height) {
    bbox.setBounds(x,y,width,height);
  }

  public Rectangle getBounds() {
    return bbox;
  }

  private void computeSize() {
    boolean cwFont = false;
    double[] fontwidth = { TextLabel.constantFontwidth };
    String fontname = gc.getFontname().toLowerCase();
    if(fontname.startsWith("courier") || fontname.startsWith("monospaced")) {
      cwFont = true;
    } else if(fontname.startsWith("helvetica") || fontname.startsWith("sansserif")) {
      fontwidth = TextLabel.helveticaFontwidth;
    } else {
      fontwidth = TextLabel.romanFontwidth;
    }

    char[] array = line.toCharArray();
    double fwidth = 0;
    int value = 0;
    boolean hadNBSP = false;
    for(int i = 0; i < array.length; i++) {
      if(array[i] == TextLabel.NBSP) {
	array[i] = ' ';
	hadNBSP = true;
      }
      value = array[i] - 32;
      fwidth += (cwFont) ? fontwidth[0] : ((value >= 0 && value < fontwidth.length) ? fontwidth[value] : 0 );
    }
    if(hadNBSP) {
      line = new String(array,0,array.length);
    }
    int height = gc.getFontsize();
    int width = (int)Math.round((double)height * fwidth);
    size.setSize(width,height);
  }

  public Dimension getSize() {
    return size;
  }
}
