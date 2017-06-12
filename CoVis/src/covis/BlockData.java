/* ----------------------------------------------------------
%   Copyright (C) 2005 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package covis;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.util.Vector;

/**
 * The BlockData class holes the data represented by XML tag "node"
 * ("node" describes the Basic Block)
 */
public class BlockData {
  /** Function Data */
  public SubFuncData subFunc;

  /** BasicBlk */  
  private Element blkRoot;

  /** Node element : id */  
  public String label;

  /**Node element : next */
  public LabelType next;

  /**Node element : prev */
  public LabelType prev;

  /** Node element : info (child element is LabelList) */
  public ViList labelList;    

  /** Node element : info (child element is StringList) */
  public ViList stringList;

  /** Node element : statement (child element is lir) */
  private BlockCode lirData;
  /** Node element : statement (child element is lir2c) */
  private BlockCode lir2cData;
  /** Node element : statement (child element is hir) */
  private BlockCode hirData;
  /** Node element : statement (child element is hir2c) */
  private BlockCode hir2cData;

  /**
   * constructor
   * @param subFunc  data for tag "graph"
   * @param root     Dom root element for tag "node"
   */
  public BlockData(SubFuncData subFunc, Element root) {
    this.subFunc = subFunc;
    this.blkRoot = root;

    /* TagName.ID */
    LabelType id = new LabelType(root, TagName.ID);
    label = id.getName();
    //System.out.println("  label = "+label);

    /* Tagname.NEXT TagName.PREV */
    next = new LabelType(root, TagName.NEXT);
    prev = new LabelType(root, TagName.PREV);

    /* TagName.INFO */
    NodeList infoList = root.getElementsByTagName(TagName.INFO);
    labelList = setInfo(infoList, TagName.LABEL_LIST);
    stringList = setInfo(infoList, TagName.STRING_LIST);

    /* TagName.STATMENT */
    NodeList statementList = root.getElementsByTagName(TagName.STATEMENT);

    StatementType lines;
    lines = new StatementType(statementList, TagName.LIR);
    if (!lines.isEmpty()) {
      lirData = new BlockCode(lines);
    }
    lines = new StatementType(statementList, TagName.LIR2C);
    if (!lines.isEmpty()) {
      lir2cData = new BlockCode(lines);
    }
    lines = new StatementType(statementList, TagName.HIR);
    if (!lines.isEmpty()) {
      hirData = new BlockCode(lines);
    }
    lines = new StatementType(statementList, TagName.HIR2C);
    if (!lines.isEmpty()) {
      hir2cData = new BlockCode(lines);
    }

  }

  /**
   * get block Label
   * @return block label string
   */
  public String getBlockLabel() {
    return label;
  }

  /**
   * check the type code is empty
   * @return true : if the code of this type is empty
  **/
  public boolean isEmpty(int type) {
    switch(type) {
      case VisEnvironment.LIR2C:
        if (lir2cData == null)
          return true;
        else
          return lir2cData.isEmpty();

      case VisEnvironment.LIR:
        if (lirData == null)
          return true;
        else
          return lirData.isEmpty();

      case VisEnvironment.HIR2C:
        if (hir2cData == null)
          return true;
        else
          return hir2cData.isEmpty();

      case VisEnvironment.HIR:
        if (hirData == null)
          return true;
        else
          return hirData.isEmpty();

      default:
        return true;
    }
  }

  /**
   * returns Lir exp list
   * @return ViList of exps
   */
  public ViList getLirExps() {
    if (lirData != null) {
      return lirData.exps;
    }
    return null;
  }
  /**
   * returns Lir2c exp list
   * @return ViList of exps
   */
  public ViList getLir2cExps() {
    if (lir2cData != null) {
      return lir2cData.exps;
    }
    return null;
  }

  /**
   * returns Hir exp list
   * @return ViList of exps
   */
  public ViList getHirExps() {
    if (hirData != null) {
      return hirData.exps;
    }
    return null;
  }

  /**
   * returns Hir2c exp list
   * @return ViList of exps
   */
  public ViList getHir2cExps() {
    if (hir2cData != null) {
      return hir2cData.exps;
    }
    return null;
  }

  /**
   * set Information
   * @param nodeList Dom NodeList
   * @param tag      tag name for Information
   */
  private ViList setInfo(NodeList nodeList, String tag) {
    InfoData  data;
    ViList infoList = new ViList();

    for (int i = 0 ; i<nodeList.getLength() ; ++i) {
      Element elem = (Element)nodeList.item(i);
      LabelListType root = new LabelListType(elem, tag);

      if (root.isEmpty())
        return null;

      ViList list = root.getListList();

      for (ViList r = list.first(); !r.atEnd(); r=r.next) {
        LabelType label = (LabelType)r.elem();
        infoList.add(new InfoData(label.getName(), label.getList()));
      }
    }
    return infoList;
  }

  /*****************************************************************/
  /**
   * the local sub class for each type of source
   */
  private class BlockCode {
    ViList exps;

    /** constructor
     * @param lines Statements in the block
     */
    BlockCode(StatementType lines) {
      exps = lines.getList();
      for (ViList q = exps.first() ; !q.atEnd(); q=q.next()) {
        ExpType exp = (ExpType)q.elem();
        //System.out.println(exp.lineNo +" = "+ exp.string);
      }
    }

    /** check the content
     * @return true : if exps is exist
     */
    boolean isEmpty() {
      if (exps == null)
        return true;
      else {
        return exps.isEmpty();
      }
    }
  }

}
