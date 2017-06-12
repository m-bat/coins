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
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class TextLabel
{
  /**
   * The Unicode no-break space character.
   */
  public final static char NBSP = '\u00a0'; // Unicode no-break space

  /**
   * Indicates that text should be left justified.
   */
  public static final int JUSTIFY_LEFT   =  1;
  /**
   * Indicates that text should be center justified.
   */
  public static final int JUSTIFY_CENTER =  2; // default
  /**
   * Indicates that text should be right justified.
   */
  public static final int JUSTIFY_RIGHT  =  4;

  /**
   * Indicates that text should be top justified.
   */
  public static final int JUSTIFY_TOP    =  8;
  /**
   * Indicates that text should be middle justified.
   */
  public static final int JUSTIFY_MIDDLE = 16; // default
  /**
   * Indicates that text should be bottom justified.
   */
  public static final int JUSTIFY_BOTTOM = 32;

  /**
   * Rough font sizing information for the roman (or serif) font.
   */
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
    0.4440,  0.4800,  0.2000,  0.4800,  0.5410,  0.0      // |z{|}~/
  };                                                      // +-----+

  /**
   * Rough font sizing information for the helvetica (or sansserif) font.
   */
  public final static double[] helveticaFontwidth = {     // +------+
    0.2780,  0.2780,  0.3550,  0.5560,  0.5560,  0.8890,  // | !"#$%|
    0.6670,  0.2210,  0.3330,  0.3330,  0.3890,  0.5840,  // |&'()*+|
    0.2780,  0.3330,  0.2780,  0.2780,  0.5560,  0.5560,  // |,-./01|
    0.5560,  0.5560,  0.5560,  0.5560,  0.5560,  0.5560,  // |234567|
    0.5560,  0.5560,  0.2780,  0.2780,  0.5840,  0.5840,  // |89:;<=|
    0.5840,  0.5560,  01.015,  0.6670,  0.6670,  0.7220,  // |>?@ABC|
    0.7220,  0.6670,  0.6110,  0.7780,  0.6110,  0.2780,  // |DEFGHI|
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

  /**
   * Rough font sizing information for the courier (or constant) font.
   */
  public final static double constantFontwidth = 0.6206;

  //public final static double[] serifFontwidth = {         // +------+
    //0.2299,  0.2839,  0.4062,  0.5039,  0.4842,  0.7852,  // | !"#$%|
    //0.7649,  0.2946,  0.3568,  0.3568,  0.4868,  0.5499,  // |&'()*+|
    //0.2501,  0.6219,  0.2427,  0.2742,  0.4842,  0.4842,  // |,-./01|
    //0.4842,  0.4842,  0.4842,  0.4842,  0.4842,  0.4842,  // |234567|
    //0.4842,  0.4842,  0.2564,  0.2638,  0.5458,  0.5568,  // |89:;<=|
    //0.5458,  0.4137,  0.8931,  0.7406,  0.6361,  0.6713,  // |>?@ABC|
    //0.7066,  0.6084,  0.5793,  0.7256,  0.7388,  0.3394,  // |DEFGHI|
    //0.3885,  0.6991,  0.5934,  0.9141,  0.7379,  0.7191,  // |JKLMNO|
    //0.5741,  0.7191,  0.6648,  0.5419,  0.6133,  0.7238,  // |PQRSTU|
    //0.7148,  0.9544,  0.7188,  0.7123,  0.6070,  0.3183,  // |VWXYZ[|
    //0.2742,  0.3183,  0.4767,  0.4906,  0.2872,  0.4400,  // |\]^_`a|
    //0.4842,  0.4342,  0.4842,  0.4418,  0.3233,  0.4791,  // |bcdefg|
    //0.5012,  0.2603,  0.2615,  0.4842,  0.2890,  0.7781,  // |hijklm|
    //0.5012,  0.4842,  0.4868,  0.4868,  0.3543,  0.3911,  // |nopqrs|
    //0.3201,  0.4988,  0.4794,  0.7254,  0.5230,  0.4744,  // |tuvwxy|
    //0.4452,  0.4406,  0.2041,  0.4406,  0.5930            // |z{|}~/
  //};                                                      // +-----+

  //public final static double[] sansSerifFontwidth = {     // +------+
    //0.2744,  0.2764,  0.3454,  0.5648,  0.5584,  0.8663,  // | !"#$%|
    //0.7087,  0.2400,  0.3447,  0.3447,  0.3951,  0.5766,  // |&'()*+|
    //0.2626,  0.6299,  0.2550,  0.2818,  0.5547,  0.5547,  // |,-./01|
    //0.5547,  0.5547,  0.5547,  0.5547,  0.5547,  0.5547,  // |234567|
    //0.5547,  0.5547,  0.2550,  0.2615,  0.5775,  0.5649,  // |89:;<=|
    //0.5775,  0.5472,  1.0087,  0.6928,  0.6773,  0.7365,  // |>?@ABC|
    //0.7314,  0.6598,  0.6010,  0.7629,  0.7314,  0.2924,  // |DEFGHI|
    //0.5152,  0.6912,  0.5666,  0.8546,  0.7314,  0.7682,  // |JKLMNO|
    //0.6648,  0.7708,  0.6875,  0.6699,  0.5846,  0.7238,  // |PQRSTU|
    //0.7029,  0.9156,  0.6914,  0.6825,  0.6496,  0.2742,  // |VWXYZ[|
    //0.2818,  0.2742,  0.5129,  0.5666,  0.2400,  0.5254,  // |\]^_`a|
    //0.5717,  0.5078,  0.5717,  0.5259,  0.3135,  0.5717,  // |bcdefg|
    //0.5547,  0.2173,  0.2227,  0.4842,  0.2173,  0.7856,  // |hijklm|
    //0.5621,  0.5643,  0.5717,  0.5717,  0.3468,  0.4907,  // |nopqrs|
    //0.3276,  0.5334,  0.5440,  0.7273,  0.5225,  0.5088,  // |tuvwxy|
    //0.4842,  0.3160,  0.2499,  0.3160,  0.5988            // |z{|}~/
  //};                                                      // +-----+

  private GraphicContext gc = null;
  private GraphicContext setupGC = null;
  private String text = null;
  private Dimension size = new Dimension();
  private Dimension drawSize = null;
  private Vector lines = new Vector();
  private Point position = new Point();
  private Rectangle bbox = new Rectangle();
  private boolean setBBoxFlag = true;
  private DrawObject drawObject = null;

  /**
   * Create a new <code>TextLabel</code> instance associated with a <code>DrawObject</code>.
   * 
   * @param drwObj the <code>DrawObject</code> associated with this <code>TextLabel</code>
   * @param str the text of the label
   * @param context the graohic context to use with the label
   * @param pos the label position (in graph co-ordinates)
   */
  public TextLabel(DrawObject drwObj, String str, GraphicContext context, Point pos) {
    // super();
    drawObject = drwObj;
    DrawObjectPeer thisPeer = drawObject.getPeer();
    if(thisPeer != null) {
      DrawObjectPeer peer = thisPeer;
      do {
	new TextLabelPeer(peer, this);
	peer = peer.getNext();
      } while(peer != thisPeer);
    }
    gc = context;
    setPosition(pos);
    setText(str);
  }

  /**
   * Set the position of the label.
   *
   * @param pt the label position in graph co-ordinates
   */
  void setPosition(Point pt) {
    if(position.x != pt.x || position.y != pt.y) setSetupFlag();
    position.setLocation(pt.x,pt.y);
  }

  void setSize(Dimension sz) {
    size.setSize(sz);
  }

  /**
   * Get the size of the text label.
   *
   * @return the size
   */
  public Dimension getSize() {
    return size;
  }

  /**
   * Get the position of the text label.
   *
   * @return the position
   */
  public Point getPosition() {
    return position;
  }

  /**
   * Change the text of the label.
   *
   * @param str the new label text
   */
  public void setText(String str) {
    if(text == null || !text.equals(str)) {
      text = str;
      setupGC = null;
      setupText(getGraphicContext());
    }
  }

  /**
   * Get the label text.
   *
   * @return the label text.
   */
  public String getText() {
    return text;
  }

  /**
   * Check if this object has text associated with it.
   *
   * @return true is there is associated text of length greater than zero; false otherwise.
   */
  public boolean hasText() {
    return (text != null && text.length() > 0);
  }

  /**
   * Get the graphic context supplied when this object was created.
   *
   * @return the creation graphic context.
   */
  public GraphicContext getGraphicContext() {
    return gc;
  }

  /**
   * Get the graphic context used for setting up this object.
   *
   * @return the setup graphic context.
   */
  public GraphicContext getSetupGC() {
    return setupGC;
  }

  void setupText(GraphicContext context) {
    if(getSetupGC() != null && getSetupGC().getFont().equals(context.getFont())) {
      return;
    }
    setBBoxFlag = true;
    lines.removeAllElements();
    size.setSize(0,0);

    setupGC = (GraphicContext)context.clone();

    if(text == null || context == null) {
      return;
    }

    StringBuffer buf = new StringBuffer();
    Dimension sz = null;
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
	  mergeSizes(storeLine(buf.toString().trim(),curCh,context),context);
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
    mergeSizes(storeLine(buf.toString().trim(),'n',context),context);
    buf.setLength(0);

    setSetupFlag();
  }

  private void mergeSizes(Dimension sz, GraphicContext context) {
    int wd = sz.width + context.getFontsize(); // margin
    int ht = sz.height + 2;               // interline
    size.setSize(((size.width > wd) ? size.width : wd),size.height + ht);
  }

  private Dimension storeLine(String line, char justification, GraphicContext context) {
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
    TextLine tl = new TextLine(line, just, context);
    lines.addElement(tl);
    return tl.getSize();
  }

  /**
   * Return an enumeration of the lines of text.
   *
   * @return an enumeration of <code>TextLine</code> objects.
   */
  public Enumeration lines() {
    if(lines == null) return Grappa.emptyEnumeration.elements();
    return lines.elements();
  }

  /**
   * Return a count of the lines of text.
   *
   * @return a count of lines of text in the label.
   */
  public int lineCount() {
    if(lines == null) return 0;
    return lines.size();
  }

  private void setBounds(int x, int y, int width, int height) {
    bbox.setBounds(x,y,width,height);
    setBBoxFlag = false;
  }

  /**
   * Get the bounding box of this text label.
   *
   * @return the bounding box.
   */
  public Rectangle getBounds() {
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

  //public boolean contains(Point p) {
    //if(
       //text == null
       //|| text.length() == 0
       //|| lines == null
       //|| lines.size() == 0
       //|| !getBounds().contains(p)
       //) return false;
    //
    //Enumeration lns = lines.elements();
    //TextLine textLine = null;
//
    //while(lns.hasMoreElements()) {
      //textLine = (TextLine)lns.nextElement();
      //if(textLine.getBounds().contains(p)) return true;
    //}
    //return false;
  //}

  /**
   * Checks if the supplied TextLabel
   * contains the same text and this one.
   *
   * @return true if text labels in each <code>TextLabel</code> are equal; false otherwise.
   */
  public boolean sameText(TextLabel tl) {
    if(tl == null) return false;
    if(text != tl.getText() && (text == null || !text.equals(tl.getText()))) return false;
    return true;
  }

  /**
   * Checks if the supplied text
   * is the same as the text in this label.
   *
   * @return true if text equals the text in this label; false otherwise.
   */
  public boolean sameText(String txt) {
    if(text != txt && (text == null || !text.equals(txt))) return false;
    return true;
  }

  /**
   * Determines whether two text labels are equal. Two instances of
   * <code>TextLabel</code> are equal if their text, position and
   * graphic context are equal.
   * @param      obj   an object to be compared with this point.
   * @return     <code>true</code> if the object to be compared is
   *                     an instance of <code>TextLabel</code> and has
   *                     the same values; <code>false</code> otherwise.
   * @see #sameText(TextLabel)
   * @see #getPosition()
   * @see #getGraphicContext()
   */
  public boolean equals(Object obj) {
    if(obj instanceof TextLabel) {
      TextLabel tl = (TextLabel)obj;
      if(!sameText(tl)) return false;
      if(!position.equals(tl.getPosition())) return false;
      if(!gc.equals(tl.getGraphicContext())) return false;
      return true;
    }
    return false;
  }

  /**
   * Get the draw object associated with this object.
   *
   * @return the associated <code>DrawObject</code>.
   */
  public DrawObject getDrawObject() {
    return drawObject;
  }

  private boolean setupFlag = true;

  /**
   * Check if it appears that this object needs to be set-up
   *
   * @return true if setup is required, false otherwise
   */
  public boolean setupNeeded() {
    return setupFlag;
  }

  /**
   * Set the value of the setup indicator to true.
   *
   * @see #setupNeeded()
   */
  public void setSetupFlag() {
    setupFlag = true;
  }

  /**
   * Set the value of the setup indicator to false.
   *
   * @see #setupNeeded()
   */
  public void resetSetupFlag() {
    setupFlag = false;
  }
}
