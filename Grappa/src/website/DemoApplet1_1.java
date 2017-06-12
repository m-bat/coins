import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.util.*;
import java.net.*;
import att.grappa.*;

public class DemoApplet1_1 extends Applet implements ActionListener
{
  Graphics gr = null;
  DrawPane pane = null;
  Button gif = null;
  Button submit_g = null;
  Button submit_t = null;
  TextArea textarea = null;
  Graph graph = null;

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
    StringReader input = new StringReader(grf);
    Parser program = new Parser(input, new PrintWriter(System.err,true));
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
      graph.buildObjects();
    } catch(InstantiationException ie) {
      System.err.println("Cannot instantiate: " + ie.getMessage());
      ie.printStackTrace(System.err);
    }

    pane = new DrawPane(graph,false,1,new Dimension(400,440));
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
    submit_g.addActionListener(this);
    gb.setConstraints(submit_g,gbc);
    add(submit_g);
    gbc.gridwidth = GridBagConstraints.RELATIVE;
    gif = new Button("Generate GIF");
    gif.addActionListener(this);
    gb.setConstraints(gif,gbc);
    add(gif);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    submit_t = new Button("Press to Submit Text");
    submit_t.addActionListener(this);
    gb.setConstraints(submit_t,gbc);
    add(submit_t);
    gbc.weightx = 0.0;
    textarea = new TextArea(15,60);
    textarea.setEditable(true);
    gb.setConstraints(textarea,gbc);
    add(textarea);

    DrawPane.drawGraph(graph);
    StringWriter theGraph = new StringWriter();
    graph.printGraph(theGraph);
    textarea.append(theGraph.toString());
    textarea.select(0,0);
    theGraph.close();
  }

  public void actionPerformed(ActionEvent evt) {
    if(evt.getSource() instanceof Button) {
      Button tgt = (Button)evt.getSource();
      if(tgt == gif) {
	if(pane != null) {
	  AppletContext context = getAppletContext();
	  context.showStatus("Generating GIF (this may take time)...");
	  try {
	    URL docbase = getDocumentBase();
	    URL url = new URL("http://" + docbase.getHost() + "/~john/cgi-bin/write_gif");
	    URLConnection urlConn = url.openConnection();
	    urlConn.setDoInput(true);
	    urlConn.setDoOutput(true);
	    urlConn.setUseCaches(false);
	    urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	    OutputStream output = urlConn.getOutputStream();
	    pane.gifImage(output);
	    output.flush();
	    output.close();
	    BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
	    String gifurl = input.readLine();
	    input.close();
	    if(gifurl != null && gifurl.length() > 0) {
	      context.showDocument(new URL(gifurl),"gif_display");
	    }
	  } catch(Exception ex) {
	    Grappa.displayException(ex);
	  }
	}
	return;
      } else if(tgt == submit_g) {
	try {
	  URL docbase = getDocumentBase();
	  URL url = new URL("http://" + docbase.getHost() + "/~john/cgi-bin/format-graph");
	  URLConnection urlConn = url.openConnection();
	  urlConn.setDoInput(true);
	  urlConn.setDoOutput(true);
	  urlConn.setUseCaches(false);
	  urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	  if(!Utilities.filterGraph(graph,urlConn)) {
	    System.err.println("ERROR: somewhere in filterGraph");
	  }
	  textarea.setText("");
	  StringWriter theGraph = new StringWriter();
	  graph.printGraph(theGraph);
	  textarea.append(theGraph.toString());
	  textarea.select(0,0);
	  theGraph.close();
	}
	catch(Exception ex) {
	  Grappa.displayException(ex);
	}
	return;
      } else if(tgt == submit_t) {
	try {
	  URL docbase = getDocumentBase();
	  URL url = new URL("http://" + docbase.getHost() + "/~john/cgi-bin/format-graph");
	  URLConnection urlConn = url.openConnection();
	  urlConn.setDoInput(true);
	  urlConn.setDoOutput(true);
	  urlConn.setUseCaches(false);
	  urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	  String content = textarea.getText();
	  DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
	  printout.writeBytes(content);
	  printout.flush();
	  printout.close();
	  InputStream input = urlConn.getInputStream();
	  Parser program = new Parser(input, System.err, graph);
	  try {
	    program.parse();
	  } catch(Exception ex) {
	    System.err.println("Exception: " + ex.getMessage());
	    ex.printStackTrace(System.err);
	  }
	  input.close();
	  textarea.setText("");
	  try {
	    graph.buildObjects();
	  } catch(InstantiationException ie) {
	    System.err.println("Cannot instantiate: " + ie.getMessage());
	    ie.printStackTrace(System.err);
	  }
	  DrawPane.clearGraph(graph);
	  DrawPane.drawGraph(graph);
	  StringWriter theGraph = new StringWriter();
	  graph.printGraph(theGraph);
	  textarea.append(theGraph.toString());
	  textarea.select(0,0);
	  theGraph.close();
	}
	catch(Exception ex) {
	  Grappa.displayException(ex);
	}
	return;
      }
    }
  }
}
