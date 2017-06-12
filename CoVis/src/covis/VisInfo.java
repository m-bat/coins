/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JScrollPane;

/**
 * The VisInfo class that shows the information of the Block
 * specified at the Graph Window
 */
class VisInfo implements TreeSelectionListener {

  /** tree that displaies information */
  private JTree escTree;

  /** CoVis Environment */
  private VisEnvironment visEnv;
  /** Function Window class*/
  private VisFunc visFunc;
  /** Graph Window class*/
  private VisGraph visGraph;

  /** Data of the Block */
  private BlockData currBlock;           /** current selection BasicBlk */

  /**
   * Constructor
   *     make a information tree
   * @param vis        Visualizer of Graph
   * @param env        Visualizer environment data
   * @param InfoPanel  parent pane
   */
  public VisInfo(VisGraph vis, VisEnvironment env, JScrollPane InfoPanel) {
    visEnv = env;
    this.visGraph = vis;

    escTree = new JTree();

    escTree.setFont(VisEnvironment.TREE_FONT);
    escTree.getSelectionModel().setSelectionMode
                    (TreeSelectionModel.SINGLE_TREE_SELECTION);
    escTree.addTreeSelectionListener(this);
    escTree.setVisible(true);
		
    if (InfoPanel != null)
      InfoPanel.setViewportView(escTree);
    else
      System.err.println("panel is null");

  }
	
  /**
   * show initial view of Information window
   */
  public void showPrompt() {
    DefaultMutableTreeNode root;
    root = new DefaultMutableTreeNode("Select Basic Block");
    escTree.setModel(new DefaultTreeModel(root));

    currBlock  = null;
  }
	
  /**
   * Show the information for the Block
   * @param blk      the Block Data
   */
  public void showBlock(BlockData blk) {
    DefaultMutableTreeNode root;
    DefaultMutableTreeNode node;
    InfoData info;

    currBlock  = blk;
    root = new DefaultMutableTreeNode(blk.label);

    if (blk.labelList != null) {
      for (ViList r = blk.labelList.first(); !r.atEnd() ; r = r.next()) {
        info = (InfoData)r.elem();
        node = new DefaultMutableTreeNode(info.toString());

        //System.out.println("label.title = "+info.title);
        root.add(node);
      }
    }

    if (blk.stringList != null) {
      for (ViList r = blk.stringList.first(); !r.atEnd() ; r = r.next()) {
        info = (InfoData)r.elem();
        node = new DefaultMutableTreeNode(info.toString());
        //System.out.println("stringList.title = "+info.title);
        root.add(node);
      }
    }

    escTree.setModel(new DefaultTreeModel(root));
  }


  /**
   * The transactions when click a information
   * @param e    event
   */
  public void valueChanged(TreeSelectionEvent e) {
    DefaultMutableTreeNode nodeS = (DefaultMutableTreeNode)
      escTree.getLastSelectedPathComponent();

    if (nodeS != null) {
      //System.out.println("valueChanged : " +nodeS.toString());
      String[] labels = getLabels(nodeS.toString());

      for (int i = 0; i < labels.length; i++) {
        labels[i] = labels[i].replaceAll(" ","");
        //System.out.println("label = "+labels[i]);
      }

      if (labels != null) {
        visGraph.clearNodes();
        visGraph.fillNodes(labels);
      }
    }
  }

  /********************************************************************/

  /**
   * Convert selected line to labels
   * @param treeString original labels
   * @return converted labels
   */
  private String[] getLabels(String treeString) {

    String str = treeString.replaceFirst(".*:","");
    return str.split(",");
 
  }

}
