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
 * The class for drawing a node of shape <i>record</i>.
 *
 * @version 1.1, 30 Sep 1999; Copyright 1996 - 1999 by AT&T Corp.
 * @author  <a href="mailto:john@research.att.com">John Mocenigo</a>, <a href="http://www.research.att.com">Research @ AT&T Labs</a>
 */
public class Table extends DrawNode
{
  private final static int HASTEXT  = 1;
  private final static int HASPORT  = 2;
  private final static int HASTABLE = 4;
  private final static int INTEXT   = 8;
  private final static int INPORT   = 16;

  private TableField tableField = null;
  private String rects = null;

  private Dimension preferredSize = new Dimension();
  private boolean usePreferredSize = false;

  private String label = null;

  /**
   * This constructor creates an uninitialized Table object.
   * Upon creation, a default
   * set of attributes for observing are specified (in addition to those
   * specified when its <code>super()</code> constructor is called.
   *
   * @see java.util.Observer
   */
  public Table() {
    //super();

    attrOfInterest("rects");

    sides = 4;
    shape = DrawNode.TABLE;
  }

  private char[] parseArray = null;
  private int arrayOffset = 0;
  private int depth = 0;

  private boolean isctrl(char c) {
    return ((c) == '{' || (c) == '}' || (c) == '|' || (c) == '<' || (c) == '>');
  }

  private synchronized TableField parseTableLabel(String label, boolean LR) {
    if(label == null) {
      return null;
    }

    parseArray = label.toCharArray();
    arrayOffset = 0;
    return doParse(LR, true);
  }
  
  private TableField doParse(boolean LR, boolean topLevel) {
    int maxf = 1;
    int cnt = 0;
    for(int pos = arrayOffset; pos < parseArray.length; pos++) {
      if(parseArray[pos] == '\\') {
	pos++;
	if(pos < parseArray.length && (parseArray[pos] == '{' || parseArray[pos] == '}' || parseArray[pos] == '|')) {
	  continue;
	}
      }
      if(parseArray[pos] == '{') {
	cnt++;
      } else if(parseArray[pos] == '}') {
	cnt--;
      } else if(cnt == 0 && parseArray[pos] == '|') {
	maxf++;
      }
      if(cnt < 0) {
	break;
      }
    }

    TableField rv = new TableField();
    rv.setLR(LR);
    rv.subfields(maxf);
    if(topLevel) {
      // rv.identify(null,1);
      rv.setTable(this);
    }

    StringBuffer textBuf, portBuf;
    textBuf = new StringBuffer();
    portBuf = new StringBuffer();
    
    int mode = 0;
    int fi = 0;
    boolean wflag = true;
    TableField tf = null;
    char curCh = '\000';
    while(wflag) {
      if(arrayOffset >= parseArray.length) {
	curCh = '\000';
	wflag = false;
      } else {
	curCh = parseArray[arrayOffset];
      }
      switch((int)curCh) {
      case '<':
	if((mode & (HASTABLE|HASPORT)) != 0) {
	  return null;
	}
	mode |= (HASPORT|INPORT);
	arrayOffset++;
	break;
      case '>':
	if((mode & INPORT) == 0) {
	  return null;
	}
	mode &= ~INPORT;
	arrayOffset++;
	break;
      case '{':
	arrayOffset++;
	if(mode != 0 || arrayOffset >= parseArray.length) {
	  return null;
	}
	mode = HASTABLE;
	if((tf = doParse(!LR,false)) == null) {
	  return null;
	} else {
	  rv.addField(tf);
	  //tf.identify(rv,rv.fieldCount());
	  tf.setTable(this);
	}
	break;
      case '}':
      case '|':
      case '\000':
	if((arrayOffset >= parseArray.length && !topLevel) || (mode&INPORT) != 0) {
	  return null;
	}
	if((mode&HASTABLE) == 0) {
	  tf = new TableField();
	  rv.addField(tf);
	  tf.setLR(!LR);
	  //tf.identify(rv,rv.fieldCount());
	  tf.setTable(this);
	  if((mode&HASPORT) != 0) {
	    tf.setId(portBuf.toString().trim());
	    portBuf.setLength(0);
	  }
	}
	if((mode&(HASTEXT|HASTABLE)) == 0) {
	  mode |= HASTEXT;
	  textBuf.append(' ');
	}
	if((mode&HASTEXT) != 0) {
	  tf.setLabel(textBuf.toString().trim(),getGC(),position);
	  //tf.setLR(true);
	  textBuf.setLength(0);
	}
	if(arrayOffset < parseArray.length) {
	  if(curCh == '}') {
	    arrayOffset++;
	    return rv;
	  }
	  mode = 0;
	  arrayOffset++;
	}
	break;
      case '\\':
	if(arrayOffset+1 < parseArray.length) {
	  if(isctrl(parseArray[arrayOffset+1])) {
	    arrayOffset++;
	    curCh = parseArray[arrayOffset];
	  } else if(parseArray[arrayOffset+1] == ' ') {
	    arrayOffset++;
	    curCh = TextLabel.NBSP;
	  }
	}
	// fall through...
      default:
	if((mode&HASTABLE) != 0 && curCh != ' ' && curCh != TextLabel.NBSP) {
	  return null;
	}
	if((mode&(INTEXT|INPORT)) == 0 && curCh != ' ' && curCh != TextLabel.NBSP) {
	  mode |= (INTEXT|HASTEXT);
	}
	if((mode&INTEXT) != 0) {
	  textBuf.append(curCh);
	} else if((mode&INPORT) != 0) {
	  portBuf.append(curCh);
	}
	arrayOffset++;
	break;
      }
    }
    return rv;
  }

  /**
   * This method sets up the information needed to draw the table (record).
   * It is called from setBounds (where drawOrientation is set).
   * After obtaining the bounding box, it sets up the outline and
   * additional periphery information.
   */
  protected void setTable() {
    String text = getLabel();
    if((tableField = parseTableLabel(text,!getElement().getSubgraph().isLR())) == null) {
      text = "\\N";
      tableField = parseTableLabel(text,!getElement().getSubgraph().isLR());
    }
    Dimension sz = tableField.sizeFields();
    preferredSize.width = (sz.width > getSize().width) ? sz.width : getSize().width;
    preferredSize.height = (sz.height > getSize().height) ? sz.height : getSize().height;
    if(usePreferredSize) {
      sz.setSize(preferredSize.width,preferredSize.height);
    } else {
      sz.setSize(getSize().width,getSize().height);
    }
    tableField.resizeFields(sz);
    Point pos = new Point((int)Math.round(-(double)sz.width/2.0),(int)Math.round((double)sz.height/2.0));
    tableField.positionFields(pos);

    if(drawShape == DrawNode.POLYGON) {
      setPolygon();
    } else {
      setBox();
    }
  }

  /**
   * Get the table field associated with this table.
   *
   * @return the table field value
   */
  public TableField getTableField() {
    return tableField;
  }

  /**
   * This method is called whenever an observed Attribute is changed.
   * It is required by the <code>Observer</code> interface.
   *
   * @param obs the observable object that has been updated
   * @param arg when not null, it indicates that <code>obs</code> need no longer be
   *            observed and in its place <code>arg</code> should be observed.
   */
  public void update(Observable obs, Object arg) {
    // begin boilerplate
    if(!(obs instanceof Attribute)) {
      throw new IllegalArgumentException("expected to be observing attributes only (obs)");
    }
    Attribute attr = (Attribute)obs;
    if(arg != null) {
      if(!(arg instanceof Attribute)) {
	throw new IllegalArgumentException("expected to be observing attributes only (arg)");
      }
      attr.deleteObserver(this);
      attr = (Attribute)arg;
      attr.addObserver(this);
      // in case we call: super.update(obs,arg)
      obs = attr;
      arg = null;
    }
    // end boilerplate

    if(attr.getNameHash() == DrawObject.LABEL_HASH) {
      String oldLabel = label;

      String newLabel = (String)attr.getValue();
      if(newLabel == null || newLabel.length() == 0) {
	newLabel = null;
      } else if(newLabel.equals("\\N")) {
	newLabel = getElement().getName();
      }

      if(oldLabel == null || !oldLabel.equals(newLabel)) {
	label = newLabel;
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return;
    } else if(attr.getNameHash() == DrawObject.RECTS_HASH) {
      rects = (String)attr.getValue();
      return;
    }
    // if we got to here, then let the super class have a crack at it
    super.update(obs,arg);
  }

  /**
   * Get the label of this table.
   *
   * @return the table label
   */
  public String getLabel() {
    return label;
  }

  // so TextLabelPeer is not created automatically in DrawObjectPeer init
  /**
   * Overrides the <code>getTextLabel()</code> in <code>DrawObject</code>
   * so that a <code>null</code> value is always returned.
   * Table uses <code>TableField</code> to manage its label information.
   *
   * @return a value of <code>null</code> is always returned
   */
  public TextLabel getTextLabel() {
    return null;
  }

  /**
   * Creates the drawing peer specific for this object and the specified pane.
   *
   * @param pane the <code>DrawPane</code> upon which the object will be drawn.
   */
  public void createPeer(DrawPane pane) {
    if(pane == null) {
      throw new IllegalArgumentException("supplied DrawPane cannot be null");
    }
    // references to and from the Peer are set during its init, so
    // no need to capture the new peer
    new TablePeer(this, pane);
    return;
  }
}
