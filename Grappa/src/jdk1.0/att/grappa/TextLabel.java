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
import java.util.*;

/**
 * Essentially a structure for storing labelling info.
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class TextLabel
{
  public final static char NBSP = '\u00a0'; // Unicode no-break space

  public static final int JUSTIFY_LEFT   =  1;
  public static final int JUSTIFY_CENTER =  2; // default
  public static final int JUSTIFY_RIGHT  =  4;

  public static final int JUSTIFY_TOP    =  8;
  public static final int JUSTIFY_MIDDLE = 16; // default
  public static final int JUSTIFY_BOTTOM = 32;

  public final static double[] romanFontwidth = {         // +------+
    0.2500,  0.3330,  0.4080,  0.5000,  0.5000,  0.8330,  // | !"#$%|
    0.7780,  0.3330,  0.3330,  0.3330,  0.5000,  0.5640,  // |&'()*+|
    0.2500,  0.3330,  0.2500,  0.2780,  0.5000,  0.5000,  // |,-./01|
    0.5000,  0.5000,  0.5000,  0.5000,  0.5000,  0.5000,  // |234567|
    0.5000,  0.5000,  0.2780,  0.2780,  0.5640,  0.5640,  // |89:;<=|
    0.5640,  0.4440,  0.9210,  0.7220,  0.6670,  0.6670,  // |>?@ABC|
    0.7220,  0.6110,  0.5560,  0.7220,  0.5560,  0.3330,  // |DEFGHI|
    0.3890,  0.7220,  0.6110,  0.8890,  0.7220,  0.7220,  // |JKLMNO|
    0.5560,  0.7220,  0.6670,  0.5560,  0.6110,  0.7220,  // |PQRSTU|
    0.7220,  0.9440,  0.7220,  0.7220,  0.6110,  0.3330,  // |VWXYZ[|
    0.2780,  0.3330,  0.4690,  0.5000,  0.3330,  0.4440,  // |\]^_`a|
    0.5000,  0.4440,  0.5000,  0.4440,  0.3330,  0.5000,  // |bcdefg|
    0.3330,  0.2780,  0.2780,  0.5000,  0.2780,  0.7780,  // |hijklm|
    0.5000,  0.5000,  0.5000,  0.5000,  0.3330,  0.3890,  // |nopqrs|
    0.2780,  0.5000,  0.5000,  0.7220,  0.5000,  0.5000,  // |tuvwxy|
    0.4440,  0.4800,  0.2000,  0.4800,  0.5410            // |z{|}~/
  };                                                      // +-----+

  public final static double[] helveticaFontwidth = {     // +------+
    0.2780,  0.2780,  0.3550,  0.5560,  0.5560,  0.8890,  // | !"#$%|
    0.6670,  0.2210,  0.3330,  0.3330,  0.3890,  0.5840,  // |&'()*+|
    0.2780,  0.3330,  0.2780,  0.2780,  0.5560,  0.5560,  // |,-./01|
    0.5560,  0.5560,  0.5560,  0.5560,  0.5560,  0.5560,  // |234567|
    0.5560,  0.5560,  0.2780,  0.2780,  0.5840,  0.5840,  // |89:;<=|
    0.5840,  0.5560,  01.015,  0.6670,  0.6670,  0.7220,  // |>?@ABC|
    0.7220,  0.6670,  0.6110,  0.7780,  0.6110,  0.2780,  // |DEFHFI|
    0.5000,  0.6670,  0.5560,  0.8330,  0.7220,  0.7780,  // |JKLMNO|
    0.6670,  0.7780,  0.7220,  0.6670,  0.6110,  0.7220,  // |PQRSTU|
    0.6670,  0.9440,  0.6670,  0.6670,  0.6110,  0.2780,  // |VWXYZ[|
    0.2780,  0.2780,  0.4690,  0.5560,  0.2220,  0.5560,  // |\]^_`a|
    0.5560,  0.5000,  0.5560,  0.5560,  0.2780,  0.5560,  // |bcdefg|
    0.2780,  0.2220,  0.2220,  0.5000,  0.2220,  0.8330,  // |hijklm|
    0.5560,  0.5560,  0.5560,  0.5560,  0.3330,  0.5000,  // |nopqrs|
    0.2780,  0.5560,  0.5000,  0.7220,  0.5000,  0.5000,  // |tuvwxy|
    0.5000,  0.3340,  0.2600,  0.3340,  0.5840,  0.0   ,  // |z{|}~/
  };                                                      // +-----+

  public final static double[] serifFontwidth = {         // +------+
    0.2299,  0.2839,  0.4062,  0.5039,  0.4842,  0.7852,  // | !"#$%|
    0.7649,  0.2946,  0.3568,  0.3568,  0.4868,  0.5499,  // |&'()*+|
    0.2501,  0.6219,  0.2427,  0.2742,  0.4842,  0.4842,  // |,-./01|
    0.4842,  0.4842,  0.4842,  0.4842,  0.4842,  0.4842,  // |234567|
    0.4842,  0.4842,  0.2564,  0.2638,  0.5458,  0.5568,  // |89:;<=|
    0.5458,  0.4137,  0.8931,  0.7406,  0.6361,  0.6713,  // |>?@ABC|
    0.7066,  0.6084,  0.5793,  0.7256,  0.7388,  0.3394,  // |DEFGHI|
    0.3885,  0.6991,  0.5934,  0.9141,  0.7379,  0.7191,  // |JKLMNO|
    0.5741,  0.7191,  0.6648,  0.5419,  0.6133,  0.7238,  // |PQRSTU|
    0.7148,  0.9544,  0.7188,  0.7123,  0.6070,  0.3183,  // |VWXYZ[|
    0.2742,  0.3183,  0.4767,  0.4906,  0.2872,  0.4400,  // |\]^_`a|
    0.4842,  0.4342,  0.4842,  0.4418,  0.3233,  0.4791,  // |bcdefg|
    0.5012,  0.2603,  0.2615,  0.4842,  0.2890,  0.7781,  // |hijklm|
    0.5012,  0.4842,  0.4868,  0.4868,  0.3543,  0.3911,  // |nopqrs|
    0.3201,  0.4988,  0.4794,  0.7254,  0.5230,  0.4744,  // |tuvwxy|
    0.4452,  0.4406,  0.2041,  0.4406,  0.5930            // |z{|}~/
  };                                                      // +-----+

  public final static double[] sansSerifFontwidth = {     // +------+
    0.2744,  0.2764,  0.3454,  0.5648,  0.5584,  0.8663,  // | !"#$%|
    0.7087,  0.2400,  0.3447,  0.3447,  0.3951,  0.5766,  // |&'()*+|
    0.2626,  0.6299,  0.2550,  0.2818,  0.5547,  0.5547,  // |,-./01|
    0.5547,  0.5547,  0.5547,  0.5547,  0.5547,  0.5547,  // |234567|
    0.5547,  0.5547,  0.2550,  0.2615,  0.5775,  0.5649,  // |89:;<=|
    0.5775,  0.5472,  1.0087,  0.6928,  0.6773,  0.7365,  // |>?@ABC|
    0.7314,  0.6598,  0.6010,  0.7629,  0.7314,  0.2924,  // |DEFGHI|
    0.5152,  0.6912,  0.5666,  0.8546,  0.7314,  0.7682,  // |JKLMNO|
    0.6648,  0.7708,  0.6875,  0.6699,  0.5846,  0.7238,  // |PQRSTU|
    0.7029,  0.9156,  0.6914,  0.6825,  0.6496,  0.2742,  // |VWXYZ[|
    0.2818,  0.2742,  0.5129,  0.5666,  0.2400,  0.5254,  // |\]^_`a|
    0.5717,  0.5078,  0.5717,  0.5259,  0.3135,  0.5717,  // |bcdefg|
    0.5547,  0.2173,  0.2227,  0.4842,  0.2173,  0.7856,  // |hijklm|
    0.5621,  0.5643,  0.5717,  0.5717,  0.3468,  0.4907,  // |nopqrs|
    0.3276,  0.5334,  0.5440,  0.7273,  0.5225,  0.5088,  // |tuvwxy|
    0.4842,  0.3160,  0.2499,  0.3160,  0.5988            // |z{|}~/
  };                                                      // +-----+

  public final static double constantFontwidth = 0.6206;

  private GraphicContext gc = null;
  private String text = null;
  private MyDimension size = new MyDimension();
  private MyDimension drawSize = null;
  private Vector lines = new Vector();
  private MyPoint position = new MyPoint();
  private MyRectangle bbox = new MyRectangle();
  private boolean setBBoxFlag = true;

  public TextLabel(String str, GraphicContext context, MyPoint pos) {
    // super();
    gc = context;
    position.move(pos.x,pos.y);
    setText(str);
  }

  public void setPosition(MyPoint pt) {
    position.move(pt.x,pt.y);
  }

  public void setSize(MyDimension sz) {
    size.setSize(sz);
  }

  public void setDrawSize(MyDimension sz) {
    setDrawSize(sz.width,sz.height);
  }
  
  public void setDrawSize(int width, int height) {
    if(drawSize == null) {
      drawSize = new MyDimension(width,height);
    } else {
      drawSize.setSize(width,height);
    }
  }

  public MyDimension getSize() {
    return size;
  }

  public MyDimension getDrawSize() {
    if(drawSize == null) return size;
    return drawSize;
  }

  public MyPoint getPosition() {
    return position;
  }

  public void setText(String str) {
    text = str;
    setupText();
  }

  public String getText() {
    return text;
  }

  public boolean hasText() {
    return (text != null && text.length() > 0);
  }

  GraphicContext getGraphicContext() {
    return gc;
  }

  private void setupText() {
    setBBoxFlag = true;
    lines.removeAllElements();
    size.setSize(0,0);
    if(text == null) {
      return;
    }

    StringBuffer buf = new StringBuffer();
    MyDimension sz = null;
    char[] array = text.toCharArray();
    int pos = 0;
    char curCh = '\000';
    while(pos < array.length) {
      curCh = array[pos++];
      if(curCh == '\\') {
	if(pos >= array.length) {
	  continue;
	}
	curCh = array[pos++];
	switch((int)curCh) {
	case 'l':
	case 'n':
	case 'r':
	  mergeSizes(storeLine(buf.toString().trim(),curCh));
	  buf.setLength(0);
	  break;
	case ' ':
	  buf.append(TextLabel.NBSP);
	  break;
	default:
	  buf.append(curCh);
	}
      } else {
	buf.append(curCh);
      }
    }
    mergeSizes(storeLine(buf.toString().trim(),'n'));
    buf.setLength(0);
  }

  private void mergeSizes(MyDimension sz) {
    int wd = sz.width + gc.getFontsize(); // margin
    int ht = sz.height + 2;               // interline
    size.setSize(((size.width > wd) ? size.width : wd),size.height + ht);
  }

  private MyDimension storeLine(String line, char justification) {
    int just = TextLabel.JUSTIFY_MIDDLE;
    switch((int)justification) {
    case 'l':
      just |= TextLabel.JUSTIFY_LEFT;
      break;
    case 'r':
      just |= TextLabel.JUSTIFY_RIGHT;
      break;
    case 'n':
    default:
      just |= TextLabel.JUSTIFY_CENTER;
      break;
    }
    TextLine tl = new TextLine(this, line, just);
    lines.addElement(tl);
    return tl.getSize();
  }

  private void setBounds(int x, int y, int width, int height) {
    bbox.setBounds(x,y,width,height);
    setBBoxFlag = false;
  }

  public MyRectangle getBounds() {
    if(setBBoxFlag == true) {
      setBounds(
		position.x - (int)Math.round((double)size.width/2.0),
		position.y - (int)Math.round((double)size.height/2.0),
		size.width,
		size.height
		);
    }
    return bbox;
  }

  public boolean contains(MyPoint p) {
    if(text == null || text.length() == 0 || lines == null || lines.size() == 0) return false;
    
    Enumeration lns = lines.elements();
    TextLine textLine = null;

    while(lns.hasMoreElements()) {
      textLine = (TextLine)lns.nextElement();
      if(textLine.getBounds().contains(p)) return true;
    }
    return false;
  }

  public void draw(DrawPane pane, GraphicContext inGC, boolean paintNow) {
    if(text == null || text.length() == 0 || lines == null || lines.size() == 0) return;
    
    if(inGC != null && inGC != gc) {
      gc = inGC;
      setupText();
    }

    Graphics gr = pane.getOffscreenGraphics();
    gr.setColor(gc.getFontcolor());
    gr.setFont(gc.getFont());
    FontMetrics fm = gr.getFontMetrics();

    String line = null;
    int xadj = 0, yadj = 0;
    int asc = fm.getAscent();
    // note: subtracting getFontsize() accounts for margin (unlike `dot' program)
    int hbxw = (int)Math.round((double)(getDrawSize().width-gc.getFontsize())/2.0);
    int hbxh = (int)Math.round((double)getDrawSize().height/2.0);
    Enumeration lns = lines.elements();
    TextLine textLine = null;
    int tlwd = 0;

    MyPoint pt = new MyPoint(position.x, position.y);
    pt.translate(0,-hbxh - 1 + (int)Math.round((double)((TextLine)(lines.firstElement())).size.height / 2.0));

    while(lns.hasMoreElements()) {
      textLine = (TextLine)lns.nextElement();
      tlwd = fm.stringWidth(textLine.line);
      if((textLine.justify&JUSTIFY_LEFT) == JUSTIFY_LEFT) {
	xadj = -hbxw;
      } else if((textLine.justify&JUSTIFY_RIGHT) == JUSTIFY_RIGHT) {
	xadj = hbxw - fm.stringWidth(textLine.line);
      } else {
	xadj = - (int)Math.round((double)tlwd / 2.0);
      }
      if((textLine.justify&JUSTIFY_BOTTOM) == JUSTIFY_BOTTOM) {
	yadj = 0;
      } else if((textLine.justify&JUSTIFY_TOP) == JUSTIFY_TOP) {
	yadj = - asc;
      } else {
	yadj = - (int)Math.round((double)asc / 2.0);
      }
      textLine.setBounds(pt.x+xadj,pt.y+yadj,tlwd,asc);
      MyPoint scaledCenter = pane.scalePoint(pt.x + xadj, pt.y - yadj);
      gr.drawString(textLine.line,scaledCenter.x,scaledCenter.y);
      pt.translate(0,(textLine.size.height+2));
    }
    if(paintNow) pane.paintCanvas();
  }

  public boolean sameText(TextLabel tl) {
    if(tl == null) return false;
    if(text != tl.getText() && (text == null || !text.equals(tl.getText()))) return false;
    return true;
  }

  public boolean sameText(String txt) {
    if(text != txt && (text == null || !text.equals(txt))) return false;
    return true;
  }

  public boolean equals(TextLabel tl) {
    if(!sameText(tl)) return false;
    if(!position.equals(tl.getPosition())) return false;
    if(!gc.equals(tl.getGraphicContext())) return false;
    return true;
  }

}

class TextLine {
  TextLabel parent = null;
  String line = null;
  MyDimension size = new MyDimension();
  int justify = TextLabel.JUSTIFY_LEFT|TextLabel.JUSTIFY_MIDDLE;
  MyRectangle bbox = new MyRectangle();

  TextLine(TextLabel prnt, String text) {
    this(prnt,text,TextLabel.JUSTIFY_LEFT|TextLabel.JUSTIFY_MIDDLE);
  }

  TextLine(TextLabel prnt, String text, int just) {
    parent = prnt;
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

  public MyRectangle getBounds() {
    return bbox;
  }

  private void computeSize() {
    GraphicContext gc = parent.getGraphicContext();

    boolean cwFont = false;
    double[] fontwidth = { TextLabel.constantFontwidth };
    if(gc.getFontname().substring(0,7).equalsIgnoreCase("Courier")) {
      cwFont = true;
    } else if(gc.getFontname().substring(0,9).equalsIgnoreCase("Helvetica")) {
      fontwidth = TextLabel.sansSerifFontwidth;
    } else {
      fontwidth = TextLabel.serifFontwidth;
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

  public MyDimension getSize() {
    return size;
  }
}
