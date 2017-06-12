/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.Element;

import java.awt.Font;
import javax.swing.JFrame;

/**
 * The VisEnvironment class contains common constants for CoVis.
 **/
public class VisEnvironment{
  JFrame frame;

  /** size of the main window width */
  public static final int WIDTH = 1000;
  /** size of the main window height */
  public static final int HEIGHT = 700;
		
  /** Initial x-position of the main window */
  public static final int X_LOCATION = 0;
  /** Initial y-position of the main window */
  public static final int Y_LOCATION = 0;

	
  /** Initial position of the divider in the unitPanel */
  public static final int DIV1_LOCATION = 400;
  /** Initial position of the divider in the unitPanel */
  public static final int DIV2_LOCATION = 250;
  /** Initial position of the divider in the unitPanel */
  public static final int RIGHT_DIV_LOCATION = 300;

  /** pulldown menu Items */
  public static final String FILE = "File";
  /** pulldown menu Items */
  public static final String OPEN = "Open";
  /** pulldown menu Items */
  public static final String EXIT = "Exit";
  /** pulldown menu Items */
  public static final String HELP = "Help";
  /** pulldown menu Items */
  public static final String VERSION = "Version";
  /** pulldown menu Items */
  public static final String CHOOSER_TITLE = "Load xml file";

  /** menu's font */
  public static final Font MENU_FONT 
  = new Font("Sunssherif", Font.PLAIN, 12);
  /** menu's font */
  public static final Font DIALOG_FONT 
  = new Font("Sunssherif", Font.PLAIN, 10);
	

  /** font for VisCode */
  public static final Font CODE_FONT
  = new Font("Monospaced", Font.PLAIN, 13);
  /** column0's width for VisCode */
  public static final int COL0_WIDTH = 40;
  /** column1's width for VisCode */
  public static final int COL1_WIDTH = 30;
  /**  column2's width for VisCode */
  public static final int COL2_WIDTH = 634;

  /** max no. character in column1 */
  public static final int WRAPLINE = 76;
  /** max no. character in column1 for source */
  public static final int WRAPLINE_SRC = 80;

  /** LIR Code Type for VisCode */
  public static final int LIR = 0;
  /** LIR2C Code Type for VisCode */
  public static final int LIR2C = 1;
  /** HIR Code Type for VisCode */
  public static final int HIR = 2;
  /** HIR2C Code Type for VisCode */
  public static final int HIR2C = 3;
  /** SOURCE Code Type for VisCode */
  public static final int SOURCE = 4;
  /** Other Code Type for VisCode */
  public static final int NONE = 5;

  /** default Code Type for VisCode Upper code */
  public static final int UPPERCODE = LIR;
  /** default Code Type for VisCode Lower code */
  public static final int LOWERCODE = LIR2C;
  /** label name of source for VisCode */
  public static final String SOURCE_LABEL = "source";

  /** font for VisInfo */
  public static final Font TREE_FONT
  = new Font("Monospaced", Font.PLAIN, 12);

  /**
   * Constructor<br>
   * make Visualizer environment class
   * @param frame  main frame of CoVis
   */
  public VisEnvironment(JFrame frame) {
    this.frame = frame;
  }

}
