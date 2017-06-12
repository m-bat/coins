import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import att.grappa.*;

public class DemoApplet1_2 extends JApplet implements ActionListener, GrappaConstants
{
    GrappaPanel gp = null;
    JButton gif = null;
    JButton submit_g = null;
    JButton submit_t = null;
    JTextArea textarea = null;
    JPanel jpanel = null;
    Graph graph = null;

    public GrappaPanel getGrappaPanel() {
	return gp;
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
    

	java.awt.geom.Rectangle2D bb = graph.getBoundingBox().getBounds();
	graph.setAttribute(MINBOX_ATTR,new GrappaBox(bb.getX(),bb.getY(),2*bb.getWidth(),bb.getHeight()));

	gp = new GrappaPanel(graph);
	gp.addGrappaListener(new GrappaAdapter());
	//gp.setSize(new Dimension((int)(2*bb.getWidth()),(int)(2*bb.getHeight())));

	JScrollPane jspg = new JScrollPane(gp,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	jspg.getViewport().setBackingStoreEnabled(true);

	GridBagLayout gb = new GridBagLayout();

	jpanel = new JPanel(gb);
	setContentPane(jpanel);

	GridBagConstraints gbc = new GridBagConstraints();

	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.weightx = 1.0;
	gbc.weighty = 1.0;
	gbc.anchor = GridBagConstraints.CENTER;
	gb.setConstraints(jspg,gbc);
	jpanel.add(jspg);

	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.gridwidth = 1;
	gbc.gridheight = 1;
	gbc.weightx = 1.0;
	gbc.weighty = 0.0;
	gbc.anchor = GridBagConstraints.CENTER;

	gbc.gridwidth = GridBagConstraints.RELATIVE;

	submit_g = new JButton("Press to Submit Graph");
	submit_g.addActionListener(this);
	gb.setConstraints(submit_g,gbc);
	jpanel.add(submit_g);

	//gbc.gridwidth = GridBagConstraints.RELATIVE;
	//gif = new JButton("Generate GIF");
	//gif.addActionListener(this);
	//gb.setConstraints(gif,gbc);
	//jpanel.add(gif);

	gbc.gridwidth = GridBagConstraints.REMAINDER;
	submit_t = new JButton("Press to Submit Text");
	submit_t.addActionListener(this);
	gb.setConstraints(submit_t,gbc);
	jpanel.add(submit_t);

	gbc.anchor = GridBagConstraints.CENTER;
	gbc.weightx = 0;
	JScrollPane jspt = new JScrollPane();
	jspt.getViewport().setBackingStoreEnabled(true);
	textarea = new JTextArea();
	textarea.setEditable(true);
	jspt.setViewportView(textarea);
	jspt.setMinimumSize(new Dimension(400,250));
	gb.setConstraints(jspt,gbc);
	jpanel.add(jspt);

	StringWriter theGraph = new StringWriter();
	graph.printGraph(theGraph);
	textarea.append(theGraph.toString());
	textarea.select(0,0);
	try {
	    theGraph.close();
	} catch(IOException io) {
	}
    }

    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() instanceof JButton) {
	    JButton tgt = (JButton)evt.getSource();
	    if(tgt == gif) {
		if(gp != null) {
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
			//pane.gifImage(output);
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
		    if(!GrappaSupport.filterGraph(graph,urlConn)) {
			System.err.println("ERROR: somewhere in filterGraph");
		    }
		    graph.repaint();
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
		    graph.repaint();
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
