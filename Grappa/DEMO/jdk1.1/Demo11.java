
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import att.grappa.*;

public class Demo11
{
  public static final String SCRIPT = "../formatDemo";
  public DemoFrame  frame  = null;
  public BirdFrame  birdframe  = null;

  public Subgraph subg = null;
  
  /*
   * If no arguments, reads from stdin; otherwise argument is input file.
   */
  public static void main(String[] args) {
    InputStream input = System.in;
    if(args.length > 1) {
      System.err.println("USAGE: java Demo11 [input_graph_file]");
      System.exit(1);
    } else if(args.length == 1) {
      if(args[0].equals("-")) {
	input = System.in;
      } else {
	try {
	  input = new FileInputStream(args[0]);
	} catch(FileNotFoundException fnf) {
	  System.err.println(fnf.toString());
	  System.exit(1);
	}
      }
    }
    Demo11 demo = new Demo11();
    demo.doDemo(input);
  }

  Demo11() {
  }

  void doDemo(InputStream input) {
    Parser program = new Parser(input,System.err);
    try {
      //program.debug_parse(4);
      program.parse();
    } catch(Exception ex) {
      System.err.println("Exception: " + ex.getMessage());
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    Graph graph = program.getGraph();
	// for huale's version
	// graph.addToBBox(null);

    System.err.println("The graph contains " + graph.countOfElements(Grappa.NODE|Grappa.EDGE|Grappa.SUBGRAPH) + " elements.");
    java.util.Vector vg = graph.vectorOfElements(Grappa.NODE|Grappa.EDGE|Grappa.SUBGRAPH);
    System.err.println("The total graph vector contains " + vg.size() + " elements.");

    graph.setEditable(true);
    graph.setMenuable(true);
    graph.setErrorWriter(new PrintWriter(System.err,true));
    //graph.printGraph(new PrintWriter(System.out));

      try {
	graph.buildObjects();
      } catch(InstantiationException ie) {
	System.err.println("Cannot instantiate: " + ie.getMessage());
	ie.printStackTrace(System.err);
	System.exit(1);
      }
    //System.err.println("bbox=" + graph.getBounds().toString());

    frame = new DemoFrame(graph);
    frame.addComponentListener(frame);

    if(subg != null) frame.getDrawPane().setupPane(subg);

    frame.show();

    birdframe = new BirdFrame(graph);
  }

  public void showBirdFrame(boolean makeVisible) {
    birdframe.setVisible(makeVisible);
  }

  private static final int initSize = 300;
  class BirdFrame implements ComponentListener
  {
    DrawPane pane = null;
    Graph graph = null;
    Frame bframe = null;
  
    public BirdFrame(Graph graph) {
      this.graph = graph;
    }

    private void buildFrame() {
      bframe = new Frame("BirdFrame");
      Dimension sz = null;
      Rectangle bnds = graph.getBounds();
      int mxsd = 10 + ((bnds.width > bnds.height) ? bnds.width : bnds.height);
      if(mxsd < initSize) {
	sz = new Dimension(bnds.width,bnds.height);
      } else {
	double factor = (double)initSize / (double)mxsd;
	sz = new Dimension((int)Math.round(factor*(double)bnds.width),(int)Math.round(factor*(double)bnds.height));
      }
      bframe.add("Center", pane = new DrawPane(graph,true,ScrollPane.SCROLLBARS_NEVER,sz));
      bframe.pack();
	// huale's version cannot handle the next line
      bframe.setSize(sz);
      bframe.addComponentListener(this);
    }

    public void setVisible(boolean viz) {
      if(bframe == null) buildFrame();
      bframe.setVisible(viz);
    }

    public DrawPane getDrawPane() {
      if(bframe == null) buildFrame();
      return pane;
    }

    public void componentResized(ComponentEvent evt) {
      //System.err.println("bird resized");
      //getDrawPane().setupPane();
      //if(bframe.isVisible()) getDrawPane().paintCanvas();
    }

    public void componentShown(ComponentEvent evt) {
      //System.err.println("bird shown");
      //getDrawPane().paintCanvas();
    }

    public void componentHidden(ComponentEvent evt) {
      //System.err.println("bird hidden");
      return;
    }

    public void componentMoved(ComponentEvent evt) {
      //System.err.println("bird moved");
      return;
    }
  }

  class DemoFrame extends Frame implements ActionListener, ComponentListener, ItemListener
  {
    Graphics gr = null;
    DrawPane pane = null;
    Button layout = null;
    Checkbox birdseye = null;
    Button capture = null;
    Button postscript = null;
    Button printer = null;
    Button draw = null;
    Button quit = null;
    Graph graph = null;
    Panel panel = null;
  
    public DemoFrame(Graph graph) {
      super("DemoFrame");
      this.graph = graph;

      GridBagLayout gbl = new GridBagLayout();
      GridBagConstraints gbc = new GridBagConstraints();

      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.anchor = GridBagConstraints.NORTHWEST;

      panel = new Panel();
      panel.setLayout(gbl);

      birdseye = new Checkbox("Bird's Eye");
      gbl.setConstraints(birdseye,gbc);
      panel.add(birdseye);
      birdseye.addItemListener(this);

      draw = new Button("Draw");
      gbl.setConstraints(draw,gbc);
      panel.add(draw);
      draw.addActionListener(this);

      layout = new Button("Layout");
      gbl.setConstraints(layout,gbc);
      panel.add(layout);
      layout.addActionListener(this);

      capture = new Button("GIF Capture");
      gbl.setConstraints(capture,gbc);
      panel.add(capture);
      capture.addActionListener(this);

      postscript = new Button("PostScript");
      gbl.setConstraints(postscript,gbc);
      panel.add(postscript);
      postscript.addActionListener(this);

      printer = new Button("Print");
      gbl.setConstraints(printer,gbc);
      panel.add(printer);
      printer.addActionListener(this);

      quit = new Button("Quit");
      gbl.setConstraints(quit,gbc);
      panel.add(quit);
      quit.addActionListener(this);

      add("Center", pane = new DrawPane(graph,false,1,new Dimension(650,650)));
      add("West", panel);

      pack();
      setSize(600,400);
      doLayout();
      pane.getCanvas().setMinimumSize(pane.getViewportSize());
    }

    public DrawPane getDrawPane() {
      return pane;
    }

    public void itemStateChanged(ItemEvent evt) {
      if(evt.getSource() instanceof Checkbox) {
	Checkbox tgt = (Checkbox)evt.getSource();
	if(tgt == birdseye) {
	  showBirdFrame(birdseye.getState());
	}
      }
    }

    public void actionPerformed(ActionEvent evt) {
      if(evt.getSource() instanceof Button) {
	Button tgt = (Button)evt.getSource();
	if(tgt == draw) {
	  DrawPane.refreshGraph(graph);
	} else if(tgt == quit) {
	  System.exit(0);
	} else if(tgt == printer) {
	  graph.printGraph(System.out);
	  System.out.flush();
	} else if(tgt == capture) {
	  try {
	    FileOutputStream output = new FileOutputStream("graph.gif");
	    pane.gifImage(output);
	  } catch(IOException io) {
	    io.printStackTrace(System.err);
	  }
	  System.err.println("Graph written to 'graph.gif'");
	} else if(tgt == postscript) {
	  try {
	    FileWriter output = new FileWriter("graph.ps");
	    pane.getCanvas().paintPostScript(new PrintWriter(output));
	  } catch(IOException io) {
	    io.printStackTrace(System.err);
	  }
	  System.err.println("Graph written to 'graph.ps'");
	} else if(tgt == layout) {
	  Process proc = null;
	  try {
	    proc = Runtime.getRuntime().exec(Demo11.SCRIPT);
	  } catch(Exception ex) {
	    System.err.println("Exception while setting up proc: " + ex.getMessage());
	    ex.printStackTrace(System.err);
	    proc = null;
	  }
	  if(proc != null) {
	    if(!Utilities.filterGraph(graph,proc)) {
	      System.err.println("ERROR: somewhere in filterGraph");
	    }
	    try {
	      int code = proc.waitFor();
	      if(code != 0) {
		System.err.println("WARNING: proc exit code is: " + code);
	      }
	    } catch(InterruptedException ex) {
	      System.err.println("Exception while closing down proc: " + ex.getMessage());
	      ex.printStackTrace(System.err);
	    }
	  }
	}
	graph.redrawGraph();
      }
    }

    public void componentResized(ComponentEvent evt) {
      //System.err.println("frame resized");
      //pane.setupPane();
      //pane.paintCanvas();
    }

    public void componentShown(ComponentEvent evt) {
      //System.err.println("frame shown");
      //pane.paintCanvas();
    }

    public void componentHidden(ComponentEvent evt) {
      //System.err.println("frame hidden");
      return;
    }

    public void componentMoved(ComponentEvent evt) {
      //System.err.println("frame moved");
      return;
    }
  }
}
