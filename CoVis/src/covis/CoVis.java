/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import java.io.File;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 * The CoVis class is the main class of Coins Visualizer : CoVis
 *  
 */
public class CoVis implements ActionListener {
  /** main frame of CoVis window */
  static JFrame frame;
  /** main Panel of CoVis window */
  static JPanel mainPanel;
  /** tab Pane , each tab represents a module */
  static JTabbedPane tabbedPanel;

  /** main class of CoVis */
  static CoVis visDriver;

  /** Visualizer environment */
  private VisEnvironment env;

  /** main class of Viewer part */
  private Visualizer vis;

  /** XML File name */
  private static String xmlFileName;

  /**
   * CoVis main function
   *
   * @param argv XML File Name
   *
   */
  public static void main(String argv[]) {
    if (argv.length > 1) {
      System.err.println("Usage: cmd filename");
      System.exit(1);
    } else {
      xmlFileName = null;
      if (argv.length != 0)
        xmlFileName = new String(argv[0]);

      javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            showVisualizer(xmlFileName);
          }
        });
    }
  }

  /**
   * Create the main frame of the CoVis
   *
   * @param xmlFileName Xml File Name
   */
  private static void showVisualizer(String xmlFileName) {
    // Create the main frame
    frame = new JFrame("CoVis Ver. 1.0");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    visDriver = new CoVis(xmlFileName);

    // Create Pulldown Menu
    frame.setJMenuBar(visDriver.makeMenuBar());
    // Create Dom data and the main Pane
    frame.getContentPane().add(visDriver.makeMainPanel());

    // Create the title
    String title = visDriver.getTitle();
    if (title != null)
      frame.setTitle(title);

    // Display the window
    frame.setSize(VisEnvironment.WIDTH, VisEnvironment.HEIGHT);
    frame.setLocation(VisEnvironment.X_LOCATION, VisEnvironment.Y_LOCATION);
    frame.setVisible(true);
  }

  /**
   * create Menu Bar
   */
  private JMenuBar makeMenuBar() {
    // make menu bars.
    JMenuBar menubar  = new JMenuBar();
    JMenu menu;
    JMenuItem item;

    menubar.add(menu = new JMenu(VisEnvironment.FILE));
    menu.setMnemonic(KeyEvent.VK_S);
    menu.setFont(VisEnvironment.MENU_FONT);

    menu.add(item = new JMenuItem(VisEnvironment.OPEN, 'O'));
    item.setFont(VisEnvironment.MENU_FONT);
    item.addActionListener(this);

    menu.add(item = new JMenuItem(VisEnvironment.EXIT, 'E'));
    item.setFont(VisEnvironment.MENU_FONT);
    item.addActionListener(this);

    menubar.add(menu = new JMenu(VisEnvironment.HELP));
    menu.setMnemonic(KeyEvent.VK_H);

    menu.setFont(VisEnvironment.MENU_FONT);
    menu.add(item = new JMenuItem(VisEnvironment.VERSION,'V'));
    item.setFont(VisEnvironment.MENU_FONT);
    item.addActionListener(this);

    return menubar;
  }
 

  /**
   * create Maine Panel
   */
  private JPanel makeMainPanel() {
    vis = new Visualizer(xmlFileName, env);
    mainPanel = new JPanel(new BorderLayout());

    tabbedPanel = vis.makeVisPanel();
    if (tabbedPanel != null) {
      mainPanel.add(tabbedPanel, BorderLayout.CENTER);
    }
    String warning = vis.makeWarningDialog();
    if (warning != null) {
      UIManager.put("OptionPane.okButtonText", "OK");
      JOptionPane.showMessageDialog(mainPanel, warning,"Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
    return mainPanel;
  }


  /**
   * create Maine Panel
   */
  private void reMakeMainPanel(File newFile) {
    mainPanel.removeAll();
    xmlFileName = new String(newFile.getAbsolutePath());
    //System.out.println("new xml file is "+xmlFileName);

    vis = new Visualizer(xmlFileName, env);
    tabbedPanel = vis.makeVisPanel();
    if (tabbedPanel != null) {
      mainPanel.add(tabbedPanel, BorderLayout.CENTER);
    }
    String warning = vis.makeWarningDialog();
    if (warning != null) {
      UIManager.put("OptionPane.okButtonText", "OK");
      JOptionPane.showMessageDialog(mainPanel, warning,"Warning",
                                   JOptionPane.WARNING_MESSAGE);
    }


    String title = visDriver.getTitle();
    if (title != null)
      frame.setTitle(title);
    frame.setVisible(true);
  }

  private String getTitle() {
    return vis.getProgramName();
  }

  /*
   *  (non-Javadoc)
   * @see java.awt.event.ActionListener
   * #actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed (ActionEvent ae) {
    Object obj = ae.getSource();
    String text = ((JMenuItem) obj).getText();

    if (text.startsWith(VisEnvironment.EXIT)) {
      frame.setVisible(false);
      frame.dispose();
      System.exit(0);
    }

    if (text.startsWith(VisEnvironment.OPEN)) {
      XmlChooser fc = new XmlChooser(new File("./"));
      int returnVal = fc.showDialog(frame);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedXml();
        //This is where a real application would open the file.
        //System.out.println("Opening : " + file.getName());
        visDriver.reMakeMainPanel(file);
      }

    } else if (text.startsWith(VisEnvironment.VERSION)) {
      JOptionPane.showMessageDialog(frame,
                                    "COINS Visualizer\nVersion 20050301",
                                    "Version Info.",
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    frame.setVisible(true);
  }


/*****************************************************************************/
  /**
   * constructor 
   * @param xmlFileName Data file
   */
  public CoVis(String xmlFileName) {
    env = new VisEnvironment(frame);
  }

}
