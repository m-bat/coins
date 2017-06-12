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

public class Table extends DrawNode
{
  protected final static int HASTEXT  = 1;
  protected final static int HASPORT  = 2;
  protected final static int HASTABLE = 4;
  protected final static int INTEXT   = 8;
  protected final static int INPORT   = 16;

  protected TableField tableField = null;
  protected String rects = null;

  protected MyDimension preferredSize = new MyDimension();
  protected boolean usePreferredSize = false;

  public Table() {
    //super();
    sides = 4;
    shape = DrawNode.TABLE;
  }

  private char[] parseArray = null;
  private int arrayOffset = 0;
  private int depth = 0;

  /**
   * Adjust values based on specified attribute pair.
   * Possibly set redraw flag.
   *
   * @param attr the attribute to be handled.
   * @return true if attribute was handled.
   */
  public boolean handleAttribute(Attribute attr) {
    if(attr == null) {
      return true;
    }
    
    String key = attr.getName();
    
    if(key.equals("label")) {
      TextLabel oldTl = textLabel;

      String label = (String)attr.getValue();
      if(label == null || label.length() == 0) {
	label = null;
      } else if(label.equals("\\N")) {
	label = getElement().getName();
      }

      textLabel = new TextLabel(label,gc,position);
      if(oldTl == null || !oldTl.sameText(textLabel)) {
	setRedrawFlag(true);
	setBoundsFlag(true);
      }
      return true;
    } else if(key.equals("rects")) {
      //String oldRects = rects;

      rects = (String)attr.getValue();
      /*
       *if(rects == null || rects.length() == 0) {
       *rects = null;
       *}
       *
       *if(oldRects != rects && (oldRects == null || !oldRects.equals(rects))) {
       *setRedrawFlag(true);
       *setBoundsFlag(true);
       *}
       */
      return true;
    }
    return super.handleAttribute(attr);
  }

  private boolean isctrl(char c) {
    return ((c) == '{' || (c) == '}' || (c) == '|' || (c) == '<' || (c) == '>');
  }

  public synchronized TableField parseTableLabel(String label, boolean LR) {
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

  protected void setTable() {
    String text = textLabel.getText();
    if((tableField = parseTableLabel(text,!getElement().getSubgraph().isLR())) == null) {
      text = "\\N";
      tableField = parseTableLabel(text,!getElement().getSubgraph().isLR());
    }
    MyDimension sz = tableField.sizeFields();
    preferredSize.width = (sz.width > getSize().width) ? sz.width : getSize().width;
    preferredSize.height = (sz.height > getSize().height) ? sz.height : getSize().height;
    if(usePreferredSize) {
      sz.setSize(preferredSize.width,preferredSize.height);
    } else {
      sz.setSize(getSize().width,getSize().height);
    }
    tableField.resizeFields(sz);
    MyPoint pos = new MyPoint((int)Math.round(-(double)sz.width/2.0),(int)Math.round((double)sz.height/2.0));
    tableField.positionFields(pos);

    if(drawShape == DrawNode.POLYGON) {
      setPolygon();
    } else {
      setBox();
    }
  }

  /**
   * Draws the table using the specified graphic context.
   *
   * @param context the graphic context to use when drawing.
   *                If the context is null, the object's context is used.
   * @param pane restrict drawing to the supplied pane, if supplied.
   */
  public void draw(GraphicContext context, DrawPane pane, boolean paintNow) {
    DotGraph graph = getElement().getGraph();
    Enumeration panes = null;
    DrawPane pnl = null;

    if(pane == null) {
      panes = graph.getPanes();
    
      if(!panes.hasMoreElements()) {
	return;
      }
      pnl = (DrawPane)panes.nextElement();
    } else {
      pnl = pane;
    }

    setBounds();

    if(context == null) {
      context = gc;
    }
    Color fillColor = getParentGC().getBackground();
    Color lineColor = context.getForeground();
    if(context.getFillMode()) {
      fillColor = context.getForeground();
      lineColor = getParentGC().getForeground();
    }

    Graphics gr = null;
    MyRectangle scaledBounds = null;
    IntPairs drawPairs = null;
    IntPairs scaledPairs = null;
    MyRectangle scaledRect = null;
    while(true) {
      gr = pnl.getOffscreenGraphics();

      if(peripheries >= 1) {
	switch(drawShape) {
	case DrawNode.POLYGON:
	  for(int i = 0; i < drawPoints.size(); i++) {
	    drawPairs = (IntPairs)(drawPoints.elementAt(i));
	    scaledPairs = pnl.scalePairs(drawPairs);

	    gr.setColor(fillColor);
	    gr.fillPolygon(scaledPairs.xArray,scaledPairs.yArray,scaledPairs.size());
	    gr.setColor(lineColor);
	    gr.drawPolygon(scaledPairs.xArray,scaledPairs.yArray,scaledPairs.size());
	  }
	  break;
	case DrawNode.RECT:
	  for(int i = 0; i < drawPoints.size(); i++) {
	    drawPairs = (IntPairs)(drawPoints.elementAt(i));
	    scaledRect = pnl.scaleRect(drawPairs.xArray[0],drawPairs.yArray[0],drawPairs.xArray[1],drawPairs.yArray[1]);

	    gr.setColor(fillColor);
	    gr.fillRect(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height);
	    gr.setColor(lineColor);
	    gr.drawRect(scaledRect.x,scaledRect.y,scaledRect.width,scaledRect.height);
	  }
	  break;
	default:
	  throw new IllegalStateException("table drawing shape is not recognized (value is " + shape + ")");
	}
      }

      
      emitFields(pnl, context, gr, lineColor, tableField);

      if(paintNow) pnl.paintCanvas();
      if(pane != null || !panes.hasMoreElements()) break;
      pnl = (DrawPane)panes.nextElement();
    }
  }

  private void emitFields(DrawPane pane, GraphicContext gc, Graphics gr, Color lineColor, TableField tf) {

    if(tf == null) return;

    int fc = tf.fieldCount();

    if(fc == 0) {
      if(tf.getLabel() != null && tf.getLabel().hasText()) {
	tf.getLabel().setPosition(
				  new MyPoint(
					    position.x +
					    (tf.getBounds().x + (int)Math.round((double)tf.getBounds().width/2.0)),
					    position.y -
					    (tf.getBounds().y + (int)Math.round((double)tf.getBounds().height/2.0))
					    )
				  );
	tf.getLabel().draw(pane,gc,false);
      }
      return;
    }
    MyPoint ptA = new MyPoint();
    MyPoint ptB = new MyPoint();
    TableField tfield = null;
    MyRectangle bnds = null;
    for(int cnt = 0; cnt < fc; cnt++) {
      tfield = (TableField)tf.fieldAt(cnt);
      bnds = tfield.getBounds();
      if(cnt > 0) {
	if(tf.isLR()) {
	  ptA.move(bnds.x,-bnds.y);
	  ptB.move(bnds.x,-(bnds.y+bnds.height));
	} else {
	  ptB.move(bnds.x+bnds.width,-(bnds.y+bnds.height));
	  ptA.move(bnds.x,-(bnds.y+bnds.height));
	}
	ptA.translate(position.x,position.y);
	ptB.translate(position.x,position.y);
	ptA = pane.scalePoint(ptA);
	ptB = pane.scalePoint(ptB);
	gr.setColor(lineColor);
	gr.drawLine(ptA.x,ptA.y,ptB.x,ptB.y);
      }
      emitFields(pane, gc, gr, lineColor, tfield);
    }
  }

  public TableField getTableField() {
    return tableField;
  }
}
