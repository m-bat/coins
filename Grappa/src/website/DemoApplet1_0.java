import java.awt.*;
import java.io.*;
import java.applet.*;
import java.util.*;
import java.net.*;
import att.grappa.*;

public class DemoApplet1_0 extends Applet
{
  Graphics gr = null;
  DrawPane pane = null;
  Button gif = null;
  Button submit_g = null;
  Button submit_t = null;
  TextArea textarea = null;
  DotGraph graph = null;

  public DrawPane getPane() {
    return pane;
  }

  public void init() {
    String grf = getParameter("graph");
    char[] inGrf = grf.toCharArray();
    char[] outGrf = new char[grf.length()+1];
    int j = 0;
    for(int i=0; i<grf.length(); i++) {
      switch(inGrf[i]) {
      case '&':
	switch(inGrf[i+1]) {
	case 'q': // assume &quot;
	  if(inGrf[i+5] == ';') {
	    i += 5;
	    inGrf[i] = '"';
	  }
	  break;
	case 'a': // assume &amp;
	  if(inGrf[i+4] == ';') {
	    i += 4;
	    inGrf[i] = '&';
	  }
	  break;
	case 'g': // assume &gt;
	  if(inGrf[i+3] == ';') {
	    i += 3;
	    inGrf[i] = '>';
	  }
	  break;
	case 'l': // assume &lt;
	  if(inGrf[i+3] == ';') {
	    i += 3;
	    inGrf[i] = '<';
	  }
	  break;
	default:
	  break;
	}
	// fall through to...
      default:
	outGrf[j++] = inGrf[i];
	break;
      }
    }
    grf = new String(outGrf,0,j);
    InputStream input = new StringBufferInputStream(grf);
    parser program = new parser(input, System.err);
    try {
      program.parse();
    } catch(Exception ex) {
      System.err.println("Exception: " + ex.getMessage());
      ex.printStackTrace(System.err);
    }
    graph = program.getGraph();
    graph.setEditable(true);
    graph.setMenuable(true);
    try {
      graph.instantiateGraph();
    } catch(InstantiationException ie) {
      System.err.println("Cannot instantiate: " + ie.getMessage());
      ie.printStackTrace(System.err);
    }
    pane = new DrawPane(graph,false,1,new MyDimension(300,300));
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    setLayout(gb);
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gb.setConstraints(pane,gbc);
    add(pane);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    submit_g = new Button("Press to Submit Graph");
    gb.setConstraints(submit_g,gbc);
    add(submit_g);
    gbc.gridwidth = GridBagConstraints.RELATIVE;
    gif = new Button("Generate GIF");
    gb.setConstraints(gif,gbc);
    add(gif);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    submit_t = new Button("Press to Submit Text");
    gb.setConstraints(submit_t,gbc);
    add(submit_t);
    gbc.weightx = 0.0;
    textarea = new TextArea(15,60);
    textarea.setEditable(true);
    gb.setConstraints(textarea,gbc);
    add(textarea);

    graph.drawGraph();
    pane.setScrollers();
    ByteArrayOutputStream theGraph = new ByteArrayOutputStream();
    graph.printGraph(theGraph);
    textarea.appendText(theGraph.toString());
    textarea.select(0,0);
  }

  public boolean action(Event evt, Object arg) {
    if(evt.target instanceof Button) {
      Button tgt = (Button)evt.target;
      if(tgt == gif) {
	if(pane != null) {
	  AppletContext context = getAppletContext();
	  context.showStatus("Freezing image (this will take time)...");
	  try {
	    URL url = new URL("http://www.research.att.com/cgi-bin/cgiwrap/~john/write_gif");
	    URLConnection urlConn = url.openConnection();
	    urlConn.setDoInput(true);
	    urlConn.setDoOutput(true);
	    urlConn.setUseCaches(false);
	    urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	    OutputStream output = urlConn.getOutputStream();
	    pane.gifImage(output);
	    output.flush();
	    output.close();
	    DataInputStream input = new DataInputStream(urlConn.getInputStream());
	    String gifurl = input.readLine();
	    input.close();
	    if(gifurl != null && gifurl.length() > 0) {
	      context.showDocument(new URL(gifurl),"gif_display");
	    }
	  } catch(Exception ex) {
	    System.err.println("exception..." + ex.toString());
	    ex.printStackTrace(System.err);
	  }
	}
	return true;
      } else if(tgt == submit_g) {
	try {
	  URL url = new URL("http://www.research.att.com/cgi-bin/cgiwrap/~john/format-graph");
	  URLConnection urlConn = url.openConnection();
	  urlConn.setDoInput(true);
	  urlConn.setDoOutput(true);
	  urlConn.setUseCaches(false);
	  urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	  ByteArrayOutputStream theGraph = new ByteArrayOutputStream();
	  graph.printGraph(theGraph);
	  theGraph.flush();
	  //String content = "theGraph=" + URLEncoder.encode(theGraph.toString());
	  String content = theGraph.toString();
	  theGraph.close();
	  DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
	  printout.writeBytes(content);
	  printout.flush();
	  printout.close();
	  DataInputStream input = new DataInputStream(new BufferedInputStream(urlConn.getInputStream()));
	  parser program = new parser(input, System.err, graph);
	  try {
	    program.parse();
	  } catch(Exception ex) {
	    System.err.println("Exception: " + ex.getMessage());
	    ex.printStackTrace(System.err);
	  }
	  input.close();
	  textarea.setText("");
	  try {
	    graph.instantiateGraph();
	  } catch(InstantiationException ie) {
	    System.err.println("Cannot instantiate: " + ie.getMessage());
	    ie.printStackTrace(System.err);
	  }
	  graph.drawGraph();
	  pane.setScrollers();
	  theGraph = new ByteArrayOutputStream();
	  graph.printGraph(theGraph);
	  textarea.appendText(theGraph.toString());
	  textarea.select(0,0);
	}
	catch(Exception ex) {
	  System.err.println("exception..." + ex.toString());
	}
	return true;
      } else if(tgt == submit_t) {
	try {
	  URL url = new URL("http://www.research.att.com/cgi-bin/cgiwrap/~john/format-graph");
	  URLConnection urlConn = url.openConnection();
	  urlConn.setDoInput(true);
	  urlConn.setDoOutput(true);
	  urlConn.setUseCaches(false);
	  urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	  //String content = "theGraph=" + URLEncoder.encode(textarea.getText());
	  String content = textarea.getText();
	  DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
	  printout.writeBytes(content);
	  printout.flush();
	  printout.close();
	  DataInputStream input = new DataInputStream(urlConn.getInputStream());
	  parser program = new parser(input, System.err, graph);
	  try {
	    program.parse();
	  } catch(Exception ex) {
	    System.err.println("Exception: " + ex.getMessage());
	    ex.printStackTrace(System.err);
	  }
	  input.close();
	  textarea.setText("");
	  try {
	    graph.instantiateGraph();
	  } catch(InstantiationException ie) {
	    System.err.println("Cannot instantiate: " + ie.getMessage());
	    ie.printStackTrace(System.err);
	  }
	  graph.drawGraph();
	  pane.setScrollers();
	  ByteArrayOutputStream theGraph = new ByteArrayOutputStream();
	  graph.printGraph(theGraph);
	  textarea.appendText(theGraph.toString());
	  textarea.select(0,0);
	}
	catch(Exception ex) {
	  System.err.println("exception..." + ex.toString());
	}
	return true;
      }
    }
    return(super.action(evt,arg));
  }
}
