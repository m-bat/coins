/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.util.HashMap;


/**
 *
 * The VisUnit class represents tag "module" part of Visualizer
 * 
 */
public class VisUnit implements ItemListener {

  /** Environment */
  private VisEnvironment env;

  /** Data for a Unit */
  public UnitData unitData;
  public JPanel unitPanel;


  /** A function when the program carrys out only one function. */
  public FuncData currentFunc;
  public VisFunc  visFunc;

  /** the hash map that for searching. */
  private HashMap cmbStrToI;
  /** function box's strings. */
  private String [] funcStr;
  private JComboBox cb = null;
  private JPanel funcButtonPanel;
  private JComboBox graphButton;

/**
 * one of constructor.
 * @param unitData    Unit Data
 * @param unitPanel   JPanel for a unit
 * @param visEnv      VisEnviroment
 */
  public VisUnit(UnitData unitData, JPanel unitPanel, VisEnvironment visEnv) {

    env = visEnv;
    this.unitData = unitData;
    this.unitPanel = unitPanel;
    unitPanel.setLayout(new BorderLayout());

    //
    // make Data for 1 Unit
    currentFunc = unitData.currentFunc;

    //
    // make visualizer for 1 function
    visFunc = new VisFunc(currentFunc, env);

    //
    // set Function Button & Graph Button
    funcButtonPanel = new JPanel();
    funcButtonPanel.add(FuncButton(unitData.funcDataList));
    funcButtonPanel.add(visFunc.GraphButton());
    
    //
    // set Button panel & Function visualizer panel
    unitPanel.add(funcButtonPanel, BorderLayout.PAGE_START);
    unitPanel.add(visFunc.funcPanel, BorderLayout.CENTER);

  }

  /**********************************************************************/

  /**
   * Display New Function on the unitPanel
   * @param func   new function data
   */
  public void showNewFunction(FuncData func) {
    currentFunc = func;
    visFunc.setNewFunction(func, env);
  }

  /**
   * Function Button event
   * @param evt  Button event
   */
  public void itemStateChanged(ItemEvent evt) {

    String selstr = (String)cb.getSelectedItem();
    int index = cb.getSelectedIndex();

    if (index == -1) {
      return;
    }

    FuncData func = unitData.getFuncByName((String)evt.getItem());
    if (func == null) {
      System.err.println("getFuncByName returns null.");
    } else if (! func.funcName().equals(currentFunc.funcName())) {
      showNewFunction(func);
      
      unitData.putCurrentFunc(func);
      
    }
  }


  /**
   * Create Function Selection Button
   * @param funcList function data list
   * @return JComboBox for function selection button
   */
  private JComboBox FuncButton(ViList funcList) {

    funcStr = new String[unitData.numFunc];
    cmbStrToI = new HashMap();
    int i = 0;
    FuncData func;
    for (ViList q = funcList.first(); !q.atEnd(); q=q.next()) {
      func = (FuncData)q.elem();
      funcStr[i] = func.funcName();
      cmbStrToI.put(funcStr[i], new Integer(i));
      ++i;
    }

    cb = new JComboBox(funcStr);
    cb.setSelectedIndex(0);

    cb.setEditable(false);
    cb.addItemListener(this);

    return cb;
  }
 /*********************************************************************/
}
