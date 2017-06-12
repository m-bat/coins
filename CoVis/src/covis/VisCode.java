/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.BorderLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.Vector;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.FontMetrics;

/**
 *
 * The VisCode class shows Code part of CoVis window
 */
public class VisCode implements ItemListener {

  /**
   * Code Selection Button
   */
  private JComboBox cb = null;

  /**
   * Code Panel
   */
  private JScrollPane codePanel;

  /**
   * A JTable that shows code
   */
  private JTable table;

  /** A table model that use when Jtable displays. */
  private ReadOnlyTableModel tableModel;

  /** index of the source code */
  private static final String[] INDEX = {"Line", "Src", ""};
  /** index of the other codes */
  private static final String[] INDEX_SRC = {"Line", ""};

  /** indent width of the code */
  private final String INDENT = "  ";
  /** indent level of the code */
  private int indentLevel = 0;

  /** A value that indicates code to be displayed */
  private int codeType;
 
  /** LIR Visualizer */
  private VisFunc visFunc;

  /** a environment object of LIR Visualizer */
  private VisEnvironment visEnv;

  /** module data */
  private UnitData unitData;

  /** current function */
  private FuncData funcData;
  private SubFuncData subFuncData;
  private HashMap blockHash;

  /**
   * Constructor<br>
   * the constructor create button that select the code, 
   * code panel that shows code, and
   * hashmap that connect the blocks and lines
   * 
   * @param vis    VisFunc
   * @param env    VisEnviroment
   * @param type   Code type
   * @param basePanel parent panel of code panel
   */
  VisCode(VisFunc vis, VisEnvironment env, int type, JPanel basePanel) {
    visFunc = vis;
    visEnv = env;
    codeType = type;

    unitData = vis.unitData;
    this.funcData = vis.funcData;
    this.subFuncData = vis.subFuncData;

    JPanel buttonPanel = CodeButton(codeType);
    basePanel.setLayout(new BorderLayout());
    basePanel.add(buttonPanel, BorderLayout.PAGE_START);

    codePanel = new JScrollPane();
    basePanel.add(codePanel);

    blockHash = new HashMap();
  }

  /**
   * Get the JTable that shows the code.
   * @return the table that contains the code.
   */
  JTable getBaseComponent() {
    return table;
  }
	

  /**
   * a method that highlights the part of code that connected to
   * the specified label.
   * @param label   the label to be highlighting
   */
  void highlightBlock(String label) {
    int bline, eline;
    ListSelectionModel selectionModel = table.getSelectionModel();

    if (codeType != VisEnvironment.SOURCE) {
      VisBlock vb = (VisBlock)blockHash.get(label);
      bline = vb.startLine();
      eline = vb.endLine();
      //System.out.println("highlightBlock : "+bline+"-"+eline);

      if ((bline >= 0) && (eline >= 0))
        selectionModel.setSelectionInterval(bline, eline);
    } else {
      BlockData blk = (BlockData)subFuncData.blockHash.get(label);
      bline = highlightSourceBlock(blk, selectionModel);
    }

    table.setSelectionModel(selectionModel);

    if (bline >= 0) {
      JScrollBar vbar = codePanel.getVerticalScrollBar();
      setStartPosition(bline);
    }
  }

  /**
   * a method that highlights part of source code that connected to
   * the specified block.
   * @param blk             the block to be highlighting
   * @param selectionModel  ListSelectionModel of the source code
   * @return minimum line no. of highlight
   */
  private int highlightSourceBlock(BlockData blk,
                                   ListSelectionModel selectionModel) {

    selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    selectionModel.clearSelection();

    ViList exps;
    int minLine = Integer.MAX_VALUE;
    int min;
    //System.out.println("highlightSourceBlock :");

    /* Lir */
    exps = blk.getLirExps();
    if (exps != null) {
      min = highlightStmt(exps, selectionModel);
      if (minLine > min) minLine = min;
    }

    /* Hir */
    exps = blk.getHirExps();
    if (exps != null) {
      min = highlightStmt(exps, selectionModel);
      if (minLine > min) minLine = min;
    }

    exps = blk.getLir2cExps();
    if (exps != null) {
      min = highlightStmt(exps, selectionModel);
      if (minLine > min) minLine = min;
    }

    exps = blk.getHir2cExps();
    if (exps != null) {
      min = highlightStmt(exps, selectionModel);
      if (minLine > min) minLine = min;
    }
    if (minLine == Integer.MAX_VALUE)
      minLine = -1;

    return minLine;
  }

  /**
   * a method that highlights part of source code that connected to
   * the expression
   * @param exps            the expression list to be highlighting
   * @param selectionModel  ListSelectionModel of the source code
   * @return minimum line no. of highlight
   */
  private int highlightStmt(ViList exps, ListSelectionModel selectionModel) {
    int minLine = Integer.MAX_VALUE;

    for (ViList q = exps.first() ; !q.atEnd(); q=q.next()) {
      ExpType exp = (ExpType)q.elem();
      int num = exp.lineNo;
      //System.out.print(num+" ");
      if (num >= 0) {
        --num;
        selectionModel.addSelectionInterval(num, num);
        if (minLine > num) minLine = num;
      }
    }
    return minLine;
  }

  /**
   * displays the code
   * @param selected  the tag name to be displayed
   */
  public void showCode(String selected) {
    int index;
    if (selected.matches(TagName.LIR)) {
      index = VisEnvironment.LIR;
    } else if (selected.matches(TagName.LIR2C)) {
      index = VisEnvironment.LIR2C;
    } else if (selected.matches(TagName.HIR)) {
      index = VisEnvironment.HIR;
    } else if (selected.matches(TagName.HIR2C)) {
      index = VisEnvironment.HIR2C;
    } else {
      index = VisEnvironment.SOURCE;
    }

    showCode(index);
  }

  /**
   * displays the code
   * @param type the code type to be displayed
   */
  public void showCode(int type) {
    if (funcData.currentSubFuncData.isAvalable(type)) {
      codeType = type;
      showCode(funcData.currentSubFuncData);
    }
  }

  /**
   * displays the code
   * @param func the function to be displayed
   */
  public void showCode(FuncData func) {
    if (func.currentSubFuncData.isAvalable(codeType)) {
      funcData = func;
      showCode(funcData.currentSubFuncData);
    }
  }

  /**
   * displays the code
   * @param subFunc the graph function to be displayed
   */
  public void showCode(SubFuncData subFunc) {
    if (subFunc.isAvalable(codeType)) {
      if (codeType == VisEnvironment.SOURCE) {
        tableModel = new ReadOnlyTableModel(INDEX_SRC);
        showSourceBody();
      } else {
        tableModel = new ReadOnlyTableModel(INDEX);
        switch (codeType) {
          case VisEnvironment.LIR2C:
            showLir2cBody(subFunc);
            break;
          case VisEnvironment.LIR:
            showLirBody(subFunc);
            break;
          case VisEnvironment.HIR2C:
            showHir2cBody(subFunc);
            break;
          case VisEnvironment.HIR:
            showHirBody(subFunc);
            break;
          default:
            System.err.println("Error : display code type is not specified !!");
        }
      }

      table = new JTable(tableModel);
      table.setFont(VisEnvironment.CODE_FONT);
      table.setEnabled(false);

      // not display separation lines.
      table.setShowHorizontalLines(false);		

      // set the column width
      TableColumnModel columnModel = table.getColumnModel();
      FontMetrics f = table.getFontMetrics(VisEnvironment.CODE_FONT);
      columnModel.getColumn(0).setPreferredWidth(VisEnvironment.COL0_WIDTH);
      if (codeType != VisEnvironment.SOURCE) {
        columnModel.getColumn(1).setPreferredWidth(VisEnvironment.COL1_WIDTH);
        columnModel.getColumn(2).setPreferredWidth(VisEnvironment.COL2_WIDTH);
      } else {
        columnModel.getColumn(1).setPreferredWidth(
          VisEnvironment.COL1_WIDTH + VisEnvironment.COL2_WIDTH);
      }

//columnModel.getColumn(1).setPreferredWidth(f.charWidth('A')*VisEnvironment.WRAPLINE+6);

      // do not auto-adjusting the column width.
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

      if (codeType == VisEnvironment.SOURCE)
        setStartPosition(funcData.srcLineNo());

      // set the table visible.
      table.setVisible(true);

      if (codePanel != null) {
        codePanel.setViewportView(table);
      }
      else
        System.err.println("panel is null");

      subFuncData = subFunc;
    }
    return;
  }


  /**
   * displays C Source code
   */
  private void showSourceBody() {
    int line_num = 1;

    try {
      FileReader freader = new FileReader(unitData.file);
      BufferedReader breader = new BufferedReader(freader);

      while (true) {
        String str = breader.readLine();
        Vector row = new Vector(2);
        if (str != null) {
          if (str.length() == 0) {
            str = new String(" ");
          }
          Vector lines = wrappedLine(str, VisEnvironment.WRAPLINE_SRC, indentLevel);

          for (int i = 0; i < lines.size() ; ++i) {
            row = new Vector(2);
            if (i == 0) 
              row.addElement(new Integer(line_num));
            else
              row.addElement(" ");

            row.addElement((String)lines.elementAt(i));
            tableModel.addRow(row);
          }
          line_num += lines.size();
          //appendRow(tableModel, str, line_num++, 0);
        } else {
          break;
        }
      }

      breader.close();
      freader.close();

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

  }


  /**
   * set the display position of the code window
   * @param startLine  the line position of the code window
   */
  private void setStartPosition(int startLine) {
    if (startLine >= 0) {
      ListSelectionModel selectionModel = table.getSelectionModel();
      table.setSelectionModel(selectionModel);
      JScrollBar vbar = codePanel.getVerticalScrollBar();
      if (startLine > 0) {
        vbar.setValue((vbar.getMaximum() - vbar.getMinimum()) / table.getRowCount() * startLine);
      } else {
        vbar.setValue(0);
      }
    }
  }


  /**
   * displays LIR code
   * @param func  the graph function to be displayed
   */
  private void showLirBody(SubFuncData func) {
    BlockData blk;
    VisBlock vb;
    int indentLevel = 0;
    int line_num = 1;
    int num = 0;
    int num_title = 0;

    /** appendRow header */
    //num += appendRow(tableModel, ("(FUNCTION \"" + func.funcName() + "\""),
    //                         line_num++, indentLevel++);

    /** appendRow body */
    for (ViList r = func.blockList.first(); !r.atEnd() ; r=r.next()) {
      blk = (BlockData)r.elem();
      vb = new VisBlock(blk);
      blockHash.put(blk.label, vb);

      num_title = appendRow(tableModel, ("(DEFLABEL \"" + blk.label + "\")"),
                       line_num++, indentLevel++);
      vb.setStartLine(num);

      if (!blk.isEmpty(VisEnvironment.LIR)) {
        //System.out.print("showLirBody "+ blk.label + " start = "+num);
        //blk.putLir_Start(num);
        num += num_title;

        for (ViList q = blk.getLirExps().first(); !q.atEnd() ; q=q.next()) {
          ExpType str = (ExpType)q.elem();
          num += appendRow(tableModel, str.string, line_num++, str.lineNo, indentLevel);
        }
        //blk.putLir_End(num-1);
        //System.out.println(" end = "+(num-1));
      } else {
        num += num_title;
      }
      vb.setEndLine(num - 1);
      --indentLevel;
    }

    /** appendRow footer */
    --indentLevel;
    num += appendRow(tableModel, ")", line_num++, indentLevel);

  }

  /**
   * displays LIR2C code
   * @param func  the graph function to be displayed
   */
  private void showLir2cBody(SubFuncData func) {
    BlockData blk;
    VisBlock vb;
    indentLevel = 0;
    int line_num = 1;
    int num = 0;
    int num_title = 0;

    /** appendRow header */
    //num += appendRow(tableModel, func.ir2cHeader, line_num++, indentLevel++);

    /** appendRow body */
    for ( ViList r = func.blockList.first(); !r.atEnd() ; r=r.next()) {
      blk = (BlockData)r.elem();
      vb = new VisBlock(blk);
      blockHash.put(blk.label, vb);

      num_title = appendRow(tableModel, (blk.label + ":"),
                       line_num++, indentLevel++);
      vb.setStartLine(num);

      if (!blk.isEmpty(VisEnvironment.LIR2C)) {
        //blk.putLir2c_Start(num);
        num += num_title;
        //System.out.print("showLir2cBody "+ blk.label + " start = "+num);
        ViList s = blk.getLir2cExps();

        for (ViList q=s.first(); !q.atEnd() ; q=q.next()) {
          ExpType str = (ExpType)q.elem();
          num += appendRow(tableModel, str.string, line_num++, str.lineNo,
                           indentLevel);
        }
        //blk.putLir2c_End(num-1);
        //System.out.println(" end = "+num);
      } else {
        num += num_title;
      }
      vb.setEndLine(num - 1);
      --indentLevel;
    }

    /** appendRow footer */
    --indentLevel;
    num += appendRow(tableModel, "}", line_num, indentLevel);
  }

  /**
   * displays HIR code
   * @param func  the graph function to be displayed
   */
  private void showHirBody(SubFuncData func) {
    BlockData blk;
    VisBlock vb;
    int indentLevel = 0;
    int line_num = 1;
    int num = 0;
    int num_title = 0;

    /** appendRow header */
    //num += appendRow(tableModel, ("(FUNCTION \"" + func.funcName() + "\""),
    //                         line_num++, indentLevel++);

    /** appendRow body */
    for (ViList r = func.blockList.first(); !r.atEnd() ; r=r.next()) {
      blk = (BlockData)r.elem();
      vb = new VisBlock(blk);
      blockHash.put(blk.label, vb);

      num_title = appendRow(tableModel, ("(DEFLABEL \"" + blk.label + "\")"),
                       line_num++, indentLevel++);
      vb.setStartLine(num);

      if (!blk.isEmpty(VisEnvironment.HIR)) {
        //blk.putHir_Start(num);
        num += num_title;

        //System.out.print("showHirBody "+ blk.label + " start = "+num);
        for (ViList q = blk.getHirExps().first(); !q.atEnd() ; q=q.next()) {
          ExpType str = (ExpType)q.elem();
          num += appendRow(tableModel, str.string, line_num++, str.lineNo, indentLevel);
        }
        //blk.putHir_End(num-1);
        //System.out.println(" end = "+(num-1));
      } else {
        num += num_title;
      }
      vb.setEndLine(num - 1);
      --indentLevel;
    }
    /** appendRow footer */
    --indentLevel;
    num += appendRow(tableModel, ")", line_num++, indentLevel);

  }

  /**
   * displays HIR2C code
   * @param func  the graph function to be displayed
   */
  private void showHir2cBody(SubFuncData func) {
    BlockData blk;
    VisBlock vb;
    indentLevel = 0;
    int line_num = 1;
    int num = 0;
    int num_title = 0;

    /** appendRow header */
    //num += appendRow(tableModel, func.ir2cHeader, line_num++, indentLevel++);

    /** appendRow body */
    for ( ViList r = func.blockList.first(); !r.atEnd() ; r=r.next()) {
      blk = (BlockData)r.elem();
      vb = new VisBlock(blk);
      blockHash.put(blk.label, vb);

      num_title = appendRow(tableModel, (blk.label + ":"),
                       line_num++, indentLevel++);
      vb.setStartLine(num);

      if (!blk.isEmpty(VisEnvironment.HIR2C)) {
        //blk.putHir2c_Start(num);
        num += num_title;
        //System.out.print("showHir2cBody "+ blk.label + " start = "+num);
        ViList s = blk.getHir2cExps();

        for (ViList q=s.first(); !q.atEnd() ; q=q.next()) {
          ExpType str = (ExpType)q.elem();
          num += appendRow(tableModel, str.string, line_num++, str.lineNo, indentLevel);
        }
        //blk.putHir2c_End(num-1);
        //System.out.println(" end = "+num);
      } else {
        num += num_title;
      }
      vb.setEndLine(num - 1);
      --indentLevel;
    }

    /** appendRow footer */
    --indentLevel;
    num += appendRow(tableModel, "}", line_num, indentLevel);
  }

  /**
   * Append the code to the Code window
   * @param tableModel  the code window
   * @param line        source code string
   * @param line_num    line number
   * @param indentLevel inednt lebel
   *
   * @return number of displayed line
   **/
  private int appendRow(ReadOnlyTableModel tableModel,
                        String line, int line_num, int indentLevel) {

    Vector row = new Vector(3);
    if (line.length() == 0) {
      line = new String(" ");
    }

    Vector lines = wrappedLine(line, VisEnvironment.WRAPLINE, indentLevel);

    for (int i = 0; i < lines.size() ; ++i) {
      row = new Vector(3);
      if (i == 0) 
        row.addElement(new Integer(line_num));
      else
        row.addElement(" ");

      row.addElement(" ");
      //System.out.println("appendRow.elementAt("+i+"): "+(String)lines.elementAt(i));
      row.addElement((String)lines.elementAt(i));
      tableModel.addRow(row);
    }

    return lines.size();
  }

  private int appendRow(ReadOnlyTableModel tableModel,
                        String line, int line_num, int src_line, int indentLevel) {

    Vector row = new Vector(3);
    if (line.length() == 0) {
      line = new String(" ");
    }

    Vector lines = wrappedLine(line, VisEnvironment.WRAPLINE, indentLevel);

    for (int i = 0; i < lines.size() ; ++i) {
      row = new Vector(3);
      if (i == 0) {
        row.addElement(new Integer(line_num));
        if (src_line >= 0) {
          row.addElement(new Integer(src_line));
        } else {
          row.addElement(" ");
        }
      } else {
        row.addElement(" ");
        row.addElement(" ");
      }
      //System.out.println("appendRow.elementAt("+i+"): "+(String)lines.elementAt(i));
      row.addElement((String)lines.elementAt(i));
      tableModel.addRow(row);
    }

    return lines.size();
  }

  /**
   * Method to make the line which wrapped to the specified width
   * @param line         source string
   * @param wrapline     wrapping width
   * @param indentLevel  indent level
   *
   * @return Vector buffer of the strings
   */
  private Vector wrappedLine(String line, int wrapline, int indentLevel) {
    Vector buf = new Vector();
    int indent = indentLevel * INDENT.length();
    int wrap = wrapline - indent;

    //System.out.println("wrappedLine.line: "+line);
    StringBuffer indentBuf = new StringBuffer();
    for (int i = 0; i < indentLevel; ++i) {
      indentBuf.append(INDENT);
    }

    for (int i = 0; i < line.length(); i += wrap) {
      if (line.length() < (i + wrap)) {
        buf.addElement(indentBuf.toString()+line.substring(i));
        break;
      }
      else
        buf.addElement(indentBuf.toString()+line.substring(i,i+wrap) + "\n");
    }

    return buf;
  }

  /*
   * create Function Selection Button
   * @param init initial selected button 
   *
   * @return JPanel for Button
   */
  private JPanel CodeButton(int init) {
    Vector types = new Vector();
    
    int[] codeIndex = new int[10];

    int total = 0;

    if (subFuncData.isAvalable(VisEnvironment.LIR)) {
      types.add(TagName.LIR);
      codeIndex[total++] = VisEnvironment.LIR;
    }
    if (subFuncData.isAvalable(VisEnvironment.LIR2C)) {
      types.add(TagName.LIR2C);
      codeIndex[total++] = VisEnvironment.LIR2C;
    }
    if (subFuncData.isAvalable(VisEnvironment.HIR)) {
      types.add(TagName.HIR);
      codeIndex[total++] = VisEnvironment.HIR;
    }
    if (subFuncData.isAvalable(VisEnvironment.HIR2C)) {
      types.add(TagName.HIR2C);
      codeIndex[total++] = VisEnvironment.HIR2C;
    }
    if (unitData.file.canRead()) {
      types.add(VisEnvironment.SOURCE_LABEL);
      codeIndex[total++] = VisEnvironment.SOURCE;
    }

    cb = new JComboBox(types);
    if (total > 0) {
      if (init >= total) {
        init = total - 1;
        codeType = codeIndex[init];
      }
      codeType = codeIndex[init];
      //System.out.println("init = "+init+" codeType = "+codeType);

      cb.setSelectedIndex(init);
      cb.setEditable(false);
      cb.addItemListener(this);

      JPanel buttonPanel = new JPanel();
      buttonPanel.add(cb);

      return buttonPanel;
    } else {
      return null;
    }
  }


  /**
   * itemState Listner
   * @param evt   event 
   */
  public void itemStateChanged(ItemEvent evt) {
    if (evt.getStateChange() == ItemEvent.SELECTED) {
      String selstr = (String)cb.getSelectedItem();
      //System.out.println("itemStateChanged : "+selstr);
      showCode(selstr);
    }
  }

  /*****************************************************************/
  /**
   * the VisBlock is the sub class for each type of source (Source/IR/IR2C)
   */
  private class VisBlock {
    int start;
    int end;
    BlockData blk;

    /**
     * Constructor<br>
     * @param blk block data
     */
    VisBlock(BlockData blk) {
      start = -1;
      end = -1;
      this.blk = blk;
    }

    /**
     * set the start line of the code
     * @param line   start line
     */
    private void setStartLine(int line) {
      start = line;
    }
    /**
     * set the end line of the code
     * @param line   end line
     */
    private void setEndLine(int line) {
      end = line;
    }
    /**
     * get the start line of the code
     * @return start line
     */
    private int startLine() {
      return start;
    }
    /**
     * get the end line of the code
     * @return end line
     */
    private int endLine() {
      return end;
    }
    /**
     * get the block of the code
     * @return BlockData
     */
    private BlockData block() {
      return blk;
    }
  }

/*****************************************************************/
  /**
   * the sub class that defines read-only table model.
   */
  private class ReadOnlyTableModel extends DefaultTableModel {
    /**
     * constructor: make a read-only table model.
     * @param index index
     */
    ReadOnlyTableModel(String[] index) {
      super(index, 0);
    }
    /*
     *  (not Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex,int colmunIndex) {
      return false;
    }
  }

}
