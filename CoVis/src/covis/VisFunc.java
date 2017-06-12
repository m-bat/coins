/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;

/**
 * The VisFunc class represents tag "function" and tag "graph" part
 * of Visualizer.
 */
public class VisFunc {
  /**
   * main Pane for a function
   */
  public JSplitPane funcPanel;

  /**
   * Graph Window
   */
  public VisGraph visGraph;

  /**
   * Information Window
   */
  public VisInfo visInfo;

  /**
   * Upper Right Code (Source,Lir,Lir2c,Hir,Hir2c)
   */
  public VisCode visUpperCode;

  /**
   * Lower Right Code (Source,Lir,Lir2c,Hir,Hir2c)
   */
  public VisCode visLowerCode;

  /** module */ 
  public UnitData unitData;

  /** 
   * Data for a tag "funtion"
   */
  public FuncData funcData;
  /**
   * Data for a tag "graph"
   */
  public SubFuncData subFuncData;

  /**
   * CoVis Environment
   */
  private VisEnvironment env;

  /** the panel that displays upper right part (code panel) */
  private JPanel upperRightPane;

  /** the panel that displays lower right part (code panel) */
  private JPanel lowerRightPane;

  /** the panel that displays upper left part (graph panel) */
  private JScrollPane upperLeftPane;

  /** the panel that displays lower left part (information tree panel) */
  private JScrollPane lowerLeftPane;

  /** the panel that displays the base of tag "graph" */
  private JPanel unitPanel;

  /** the panel that displays the base of graph panel */
  private JPanel GPanel;

  /** the panel that displays the base of left part */
  private JSplitPane spaneLeft;

  /** the panel that displays the base of right part */
  private JSplitPane spaneRight;

  /**
   * Constructor for VisFunc
   * @param funcData Function data
   * @param visEnv   VisEnviroment
   */
  public VisFunc(FuncData funcData, VisEnvironment visEnv) {

    env = visEnv;
    this.funcData = funcData;
    this.subFuncData = funcData.currentSubFuncData;
    this.unitData = funcData.unitData;

    //
    // make each pane
    //
    upperRightPane = new JPanel();
    visUpperCode = new VisCode(this, env, VisEnvironment.UPPERCODE,
                               upperRightPane);

    lowerRightPane = new JPanel();
    visLowerCode = new VisCode(this, env, VisEnvironment.LOWERCODE,
                               lowerRightPane);

    GPanel = new JPanel();
    visGraph = new VisGraph(this, env, GPanel);

    lowerLeftPane = new JScrollPane();
    visInfo = new VisInfo(visGraph, env, lowerLeftPane);

    //
    // a construct panel for 1 function
    //
    spaneLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                               GPanel, lowerLeftPane);
    spaneLeft.setDividerLocation(VisEnvironment.DIV1_LOCATION);

    spaneRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                                upperRightPane, lowerRightPane);
    spaneRight.setDividerLocation(VisEnvironment.RIGHT_DIV_LOCATION);

    funcPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                           spaneLeft, spaneRight);
    funcPanel.setDividerLocation(VisEnvironment.DIV2_LOCATION);

    unitData.putCurrentFunc(funcData);
      
    visGraph.showGraph();
    showUpperCodePanel();
    showLowerCodePanel();
    clearBlockTree();

  }

  /**
   * Display another Function
   * @param funcData  Function Data
   * @param visEnv    Environment for Visualizer
   */
  public void setNewFunction(FuncData funcData, VisEnvironment visEnv) {
    env = visEnv;
    this.funcData = funcData;

    visGraph.showGraph(funcData);
    visUpperCode.showCode(funcData);
    visLowerCode.showCode(funcData);
    clearBlockTree();
  }


  /**
   * get Block by label
   * @param  key       label
   * @return BlockData Block Data labeled with key
   *
   */
  public BlockData getBlock(String key) {
    return (BlockData)funcData.currentSubFuncData.blockHash.get(key);
  }

  /**
   * Display Right Upper part of panel
   * it displaies current function data
   */
  public void showUpperCodePanel() {
    visUpperCode.showCode(funcData);
  }

  /**
   * Display Right Upper part of panel
   * @param func  SubFuncData (it represents tag "graph")
   */
  public void showUpperCodePanel(SubFuncData func) {
    subFuncData = func;
    visUpperCode.showCode(subFuncData);
  }

  /**
   * Highlight the line on the right upper part of panel
   * @param label  label to be highlight
   */
  public void showUpperCodePanel(String label) {
    visUpperCode.highlightBlock(label);
  }

  /**
   * Display Right Lower part of panel
   * it displaies current function data.
   */
  public void showLowerCodePanel() {
    visLowerCode.showCode(funcData);
  }

  /**
   * Display Right Lower part of panel
   * @param func  SubFuncData (it represents tag "graph")
   */
  public void showLowerCodePanel(SubFuncData func) {
    subFuncData = func;
    visLowerCode.showCode(subFuncData);
  }

  /**
   * Highlight the line on the right lower part of panel
   * @param label  label to be highlight
   */
  public void showLowerCodePanel(String label) {
    visLowerCode.highlightBlock(label);
  }

  /**
   * Display the Block Information (it represents tag "info")
   * @param lblock  the BlockData to be displayed
   */
  public void showBlockTree(BlockData lblock) {
    visInfo.showBlock(lblock);
  }

  /**
   * Clear the Block Information
   */
  public void clearBlockTree() {
    visInfo.showPrompt();
  }

  /**
   * make the Button for choosing Graph
   */
  public JComboBox GraphButton() {
    return visGraph.GraphButton();
  }

}

