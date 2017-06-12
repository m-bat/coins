
import java.awt.*;
import java.io.*;
import att.grappa.*;

public class Demo102
{
  public static final String SCRIPT = "../formatDemo";
  
  /*
   * If no arguments, reads from stdin; otherwise argument is input file.
   */
  public static void main(String[] args) {
		Grappa.setSelectColor("blue");
    DemoFrame  frame  = null;
    InputStream input = System.in;
    if(args.length > 1) {
      System.err.println("USAGE: java Demo102 [input_graph_file]");
      System.exit(1);
    } else if(args.length == 1) {
      try {
	input = new FileInputStream(args[0]);
      } catch(FileNotFoundException fnf) {
	System.err.println(fnf.toString());
	System.exit(1);
      }
    }
    parser program = new parser(input,System.err);
    try {
      program.parse();
    } catch(Exception ex) {
      System.err.println("Exception: " + ex.getMessage());
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    DotGraph graph = program.getGraph();

    graph.setEditable(true);
    graph.setMenuable(true);
    graph.setErrStream(System.err);
    //graph.printGraph(System.out);

    try {
      graph.instantiateGraph();
    } catch(InstantiationException ie) {
      System.err.println("Cannot instantiate: " + ie.getMessage());
      ie.printStackTrace(System.err);
      System.exit(1);
    }
    //System.err.println("bbox=" + graph.getBounds().toString());

    frame = new DemoFrame(graph);

    frame.show();

    graph.drawGraph();
  }
}

class DemoFrame extends Frame
{
  Graphics gr = null;
  DrawPane pane = null;
  Button layout = null;
  Button writer = null;
  Button draw = null;
  Button quit = null;
  DotGraph graph = null;
  
  public DemoFrame(DotGraph graph) {
    super("DemoFrame");
    this.graph = graph;
    add("Center", pane = new DrawPane(graph,false,1,new MyDimension(650,650)));
    add("North", draw = new Button("Draw"));
    add("South", layout = new Button("Layout"));
    add("West", writer = new Button("Write"));
    add("East", quit = new Button("Quit"));
    resize(600,400);
  }

  public DrawPane getPane() {
    return pane;
  }

  public boolean action(Event evt, Object arg) {
    if(evt.target instanceof Button) {
      Button tgt = (Button)evt.target;
      if(tgt == draw) {
	graph.drawGraph();
	return true;
      } else if(tgt == quit) {
	System.exit(0);
      } else if(tgt == writer) {
	try {
	  FileOutputStream output = new FileOutputStream("graph.gif");
	  pane.gifImage(output);
	} catch(IOException io) {
	  io.printStackTrace(System.err);
	}
	System.err.println("Graph written to 'graph.gif'");
	return true;
      } else if(tgt == layout) {
	Process proc = null;
	try {
	  proc = Runtime.getRuntime().exec(Demo102.SCRIPT);
	} catch(Exception ex) {
	  System.err.println("Exception while setting up proc: " + ex.getMessage());
	  ex.printStackTrace(System.err);
	  proc = null;
	}
	if(proc != null) {
	  if(!graph.filterGraph(new DataInputStream(proc.getInputStream()),proc.getOutputStream())) {
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
	return true;
      }
    }
    return(super.action(evt,arg));
  }
}
