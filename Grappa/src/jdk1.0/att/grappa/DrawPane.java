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
import java.io.*;

/**
 *
 * @version 0.3, 05 Dec 1996; Copyright 1996 by AT&T Corp.
 * @author  John Mocenigo (john@research.att.com)
 */
public class DrawPane extends Panel
{
  public static final int JUSTIFY_LEFT   =  1;
  public static final int JUSTIFY_CENTER =  2; // default
  public static final int JUSTIFY_RIGHT  =  4;

  public static final int JUSTIFY_TOP    =  8;
  public static final int JUSTIFY_MIDDLE = 16; // default
  public static final int JUSTIFY_BOTTOM = 32;

  public static final int initialImageWidth = 1000;
  public static final int initialImageHeight = 1000;
  public static final int incrementImageWidth = 1000;
  public static final int incrementImageHeight = 1000;

  private int imageWidth = 0;
  private int imageHeight = 0;

  private Scrollbar vert = null;
  private Scrollbar horz = null;
  private MyDimension imageSize = new MyDimension(600, 400);
  private MyDimension preferredImageSize = new MyDimension(600, 400);

  private Backgrounder backgrounder = null;

  private boolean editMode = false;
  private DotGraph graph = null;
  private DrawCanvas canvas = null;

  private MyPoint offset = new MyPoint(0,0);
  private FPoint scale = new FPoint(1.0,1.0);

  private Image offscreen = null;

  private boolean doScaling     = false;

  /**
   * @param graph the graph object to be displayed in this pane.
   * @param doScaling when true, canvas will be of fixed size and drawing
   *                  will be scaled to fit within it, otherwise grow canvas
   *                  area as needed and do not scale.  Note that in this latter
   *                  case the image may be clipped if there are no scrollbars.
   * @param scrollbarPolicy one of ScrollPane.SCROLLBARS_ALWAYS, ScrollPane.SCROLLBARS_AS_NEEDED,
   *                        or ScrollPane.SCROLLBARS_NEVER
   * @param viewSize preferred size of view area onto canvas.
   * @param backgrounder an image to be centered in the background (for best results, the image should be
   *                        fully loaded already)
   */
  public DrawPane(DotGraph graph, boolean doScaling, int scrollbarPolicy, MyDimension viewSize, Backgrounder backgrounder) {
    //super(scrollbarPolicy);
    super();
    this.graph = graph;
    this.doScaling = doScaling;
    this.backgrounder = backgrounder;
    this.canvas = new DrawCanvas(this,viewSize);
    graph.addPane(this);

    //Add Components to the Applet.
    setLayout(new BorderLayout());
    add("Center", canvas);

    if(scrollbarPolicy > 0) {
      //Create horizontal scrollbar.
      horz = new Scrollbar(Scrollbar.HORIZONTAL);

      //Create vertical scrollbar.
      vert = new Scrollbar(Scrollbar.VERTICAL);

      add("East", vert);
      add("South", horz);
    }
  }

  public DrawPane(DotGraph graph, boolean doScaling, int scrollbarPolicy, Backgrounder backgrounder) {
    this(graph,doScaling,scrollbarPolicy,new MyDimension(600,400),backgrounder);
  }

  public DrawPane(DotGraph graph, boolean doScaling, int scrollbarPolicy) {
    this(graph,doScaling,scrollbarPolicy,new MyDimension(600,400),null);
  }

  public DrawPane(DotGraph graph, boolean doScaling, int scrollbarPolicy, MyDimension viewSize) {
    this(graph,doScaling,scrollbarPolicy,viewSize,null);
  }

  public void setScrollers() {

    validate();

    //Now that we've validated, then assuming this Applet is
    //visible, the canvas size is valid and we can adjust the
    //scrollbars to match the image area. [CHECK]

    resizeHorz();
    resizeVert();
  }

  public DrawCanvas getCanvas() {
    return canvas;
  }

  public void sizeOffscreen() {
    int width = imageWidth;
    int height = imageHeight;

    MyRectangle bbox = graph.getBounds();

    if(offscreen == null) {
      // make sure the containers exists;
      // needed in Java 1.0, at any rate
      Container cont = this;
      Container nextCont = getParent();
      while(nextCont != null) {
	cont = nextCont;
	if(cont instanceof java.awt.Frame || cont instanceof java.applet.Applet) {
	}
	nextCont = nextCont.getParent();
      }
      //cont.setVisible(true);
      cont.show();
    }

    if(bbox == null) {
      if(offscreen != null) return;
      if(doScaling) {
	width = canvas.getSize().width;
      	height = canvas.getSize().height;
      } else {
	width = initialImageWidth;
      	height = initialImageHeight;
      }
    } else {
      // need this... but why also here (and in DotGraph.getBounds())
      bbox.setSize(bbox.width+1,bbox.height+1);
      if(doScaling) {
	width = canvas.getSize().width;
	height = canvas.getSize().height;
      } else {
	if(bbox.width < canvas.getSize().width) {
	  width = canvas.getSize().width;
	} else {
	  width = bbox.width;
	}
	if(bbox.height < canvas.getSize().height) {
	  height = canvas.getSize().height;
	} else {
	  height = bbox.height;
	}
      }
    }
    if(imageWidth == width && imageHeight == height && offscreen != null) return;
    imageWidth = width;
    imageHeight = height;
    offscreen = createImage(imageWidth,imageHeight);
    imageSize.setSize(imageWidth,imageHeight);
    Graphics gr = offscreen.getGraphics();
    gr.setColor(graph.getGC().getBackground());
    gr.fillRect(0,0,imageWidth,imageHeight);
    if(backgrounder != null) {
      backgrounder.drawImage(null,gr,imageWidth,imageHeight);
    }
    if(doScaling && bbox != null) {
      double xscale = 1.0;
      double yscale = 1.0;
      if(bbox.width > imageWidth) {
	xscale = (double)imageWidth / (double)bbox.width;
      }
      if(bbox.height > imageHeight) {
	yscale = (double)imageHeight / (double)bbox.height;
      }
      offset = new MyPoint(
-bbox.x + (int)Math.round((double)(imageWidth - bbox.width)/2.0),
-bbox.y + (int)Math.round((double)(imageHeight - bbox.height)/2.0)
);
      scale = new FPoint(xscale,yscale);
    } else if(bbox != null) {
      scale = new FPoint(1.0,1.0);
      offset = new MyPoint(
-bbox.x + (int)Math.round((double)(imageWidth - bbox.width)/2.0),
-bbox.y + (int)Math.round((double)(imageHeight - bbox.height)/2.0)
);
    } else {
      scale = new FPoint(1.0,1.0);
      offset = new MyPoint(0,0);
    }
  }

  public Image getImage() {
    if(offscreen == null) {
      sizeOffscreen();
    }
    return offscreen;
  }

  public Graphics getOffscreenGraphics() {
    if(offscreen == null) {
      sizeOffscreen();
    }
    return offscreen.getGraphics();
  }

  public MyPoint canonXY(int x, int y) {
    MyPoint sbar = new MyPoint(0,0);
    
    if(horz != null) sbar.x = horz.getValue();
    if(vert != null) sbar.y = vert.getValue();

    return(new MyPoint((int)((double)(x+sbar.x)/scale.x) - offset.x, (int)((double)(y+sbar.y)/scale.y) - offset.y));
  }


  public MyRectangle scaleRect(int ptx, int pty, int rw, int rh) {
    return new MyRectangle(
			   (int)Math.round(scale.x * (double)(ptx + offset.x)),
			   (int)Math.round(scale.y * (double)(pty + offset.y)),
			   (int)Math.round(scale.x * (double)rw),
			   (int)Math.round(scale.y * (double)rh)
			   );
  }

  public MyRectangle scaleRect(MyRectangle rect) {
    return scaleRect(rect.x, rect.y, rect.width, rect.height);
  }

  public double scaleXLength(double len) {
    return scale.x * len;
  }

  public double scaleYLength(double len) {
    return scale.y * len;
  }

  public FPoint scaleFPoint(double ptx, double pty) {
    return new FPoint(
		      scale.x * (double)(ptx + offset.x),
		      scale.y * (double)(pty + offset.y)
		      );
  }

  public MyPoint scalePoint(int ptx, int pty) {
    return new MyPoint(
		       (int)Math.round(scale.x * (double)(ptx + offset.x)),
		       (int)Math.round(scale.y * (double)(pty + offset.y))
		       );
  }

  public MyPolygon scalePoly(MyPolygon inPoly) {
    MyPolygon outPoly = new MyPolygon();
    for(int i = 0; i < inPoly.npoints; i++) {
      outPoly.addPoint(
		       (int)Math.round(scale.x * (double)(inPoly.xpoints[i] + offset.x)),
		       (int)Math.round(scale.y * (double)(inPoly.ypoints[i] + offset.y))
		       );
    }
    return outPoly;
  }

  public IntPairs scalePairsUntil(IntPairs inPairs, int end) {
    IntPairs outPairs = null;
    if(end > inPairs.size()) {
      end = inPairs.size();
    }
    if(inPairs.xArray[0] == inPairs.xArray[end-1] && inPairs.yArray[0] == inPairs.yArray[end-1]) {
      outPairs = new IntPairs(end);
    } else {
      outPairs = new IntPairs(end+1);
    }
    for(int i = 0; i < end; i++) {
      outPairs.insertPairAt(
			    (int)Math.round(scale.x * (double)(inPairs.xArray[i] + offset.x)),
			    (int)Math.round(scale.y * (double)(inPairs.yArray[i] + offset.y)),
			    i);
    }
    if(inPairs.xArray[0] != inPairs.xArray[end-1] || inPairs.yArray[0] != inPairs.yArray[end-1]) {
      outPairs.insertPairAt(outPairs.xArray[0],outPairs.yArray[0],end);
    }
    return outPairs;
  }

  public IntPairs scalePairs(IntPairs inPairs) {
    return scalePairsUntil(inPairs,inPairs.size());
  }

  public MyPoint scalePoint(MyPoint pt) {
    return(scalePoint(pt.x,pt.y));
  }

  public void paintCanvas() {
    paint(null);
    canvas.paint(canvas.getGraphics());
  }

  public void clearCanvas() {
    Graphics gr = canvas.getGraphics();
    if(gr != null) canvas.clear(gr);
    offscreen = null;
  }

  public void clobberCanvas() {
    clearCanvas();
    imageWidth = imageHeight = 0;
  }

  public void drawString(String text, GraphicContext gc, MyPoint pt, int justify) {
    if(offscreen == null) {
      sizeOffscreen();
    }
    Graphics gr = offscreen.getGraphics();
    gr.setColor(gc.getFontcolor());
    gr.setFont(gc.getFont());
    FontMetrics fm = gr.getFontMetrics();
    int xadj, yadj;
    if((justify&JUSTIFY_LEFT) == JUSTIFY_LEFT) {
      xadj = 0;
    } else if((justify&JUSTIFY_RIGHT) == JUSTIFY_RIGHT) {
      xadj = - fm.stringWidth(text);
    } else {
      xadj = - (int)Math.round((double)fm.stringWidth(text) / 2.0);
    }
    if((justify&JUSTIFY_BOTTOM) == JUSTIFY_BOTTOM) {
      yadj = 0;
    } else if((justify&JUSTIFY_TOP) == JUSTIFY_TOP) {
      yadj = - fm.getAscent();
    } else {
      yadj = - (int)Math.round((double)fm.getAscent() / 2.0);
    }
    MyPoint scaledCenter = scalePoint(pt.x + xadj, pt.y - yadj);
    gr.drawString(text,scaledCenter.x,scaledCenter.y);
  }

  public boolean handleEvent(Event evt) {
    switch (evt.id) {
    case Event.SCROLL_LINE_UP:
    case Event.SCROLL_LINE_DOWN:
    case Event.SCROLL_PAGE_UP:
    case Event.SCROLL_PAGE_DOWN:
    case Event.SCROLL_ABSOLUTE:
      if (evt.target == vert) {
	canvas.y_set(((Integer)evt.arg).intValue());
	canvas.repaint();
      }
      if (evt.target == horz) {
	canvas.x_set(((Integer)evt.arg).intValue());
	canvas.repaint();
      }
    }
    return super.handleEvent(evt);
  }

  //Don't call this until the canvas size is valid.
  void resizeHorz() {
    if(horz == null) return;
    int canvasWidth = canvas.size().width;

    if (canvasWidth <= 0) {
      System.err.println("Canvas has no width; can't resize scrollbar");
      return;
    }

    //Shift everything to the right if we're displaying empty space
    //on the right side.
    if ((canvas.getX() + canvasWidth) > imageSize.width) {
      int newtx = imageSize.width - canvasWidth;
      if (newtx < 0) {
	newtx = 0;
      }
      canvas.x_set(newtx);
    }

    horz.setValues(//draw the part of the image that starts at this x:
		   canvas.getX(), 
		   //amount to scroll for a "page":
		   (int)(canvasWidth * 0.9), 
		   //minimum image x to specify:
		   0,
		   //maximum image x to specify:
		   imageSize.width - canvasWidth);
    //"visible" arg to setValues() has no effect after scrollbar is visible.
    horz.setPageIncrement((int)(canvasWidth * 0.9));
    return;
  }

  //Don't call this until the canvas size is valid.
  void resizeVert() {
    if(vert == null) return;
    int canvasHeight = canvas.size().height;

    if (canvasHeight <= 0) {
      System.err.println("Canvas has no height; can't resize scrollbar");
      return;
    }

    //Shift everything downward if we're displaying empty space
    //on the bottom.
    if ((canvas.getY() + canvasHeight) > imageSize.height) {
      int newty = imageSize.height - canvasHeight;
      if (newty < 0) {
	newty = 0;
      }
      canvas.y_set(newty);
    }

    vert.setValues(//initially draw part of image starting at this y:
		   canvas.getY(), 
		   //visible arg--amount to scroll for a "page":
		   (int)(canvasHeight * 0.9), 
		   //minimum image y to specify:
		   0,
		   //maximum image y to specify:
		   imageSize.height - canvasHeight);

    //"visible" arg to setValues() has no effect after scrollbar is visible.
    vert.setPageIncrement((int)(canvasHeight * 0.9));
    return;
  }

  public void paint(Graphics g) {
    //This method probably was called due to applet being resized.
    resizeHorz();
    resizeVert();

    return;
  }

  public DotGraph getGraph() {
    return graph;
  }

  public void setBackgrounder(Backgrounder backgrounder) {
    this.backgrounder = backgrounder;
  }

  /**
   * write image in GIF 87a format to output stream
   *
   * @arg output the OutputStream to use
   */
  public void gifImage(OutputStream output) throws IOException {
    if(offscreen == null) {
      throw new IOException("image is null");
    }
    GIFEncoder gifen = null;
    try {
      gifen = new GIFEncoder(offscreen);
    } catch(AWTException awt) {
      throw new IOException("GIFEncoder could not create (" + awt.toString() + ")");
    }
    if(gifen == null) {
      throw new IOException("GIFEncoder is null");
    }
    gifen.Write(output);
  }
}
