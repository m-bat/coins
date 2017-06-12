/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IrList;
import coins.sym.Const;
import coins.sym.Label;
import coins.sym.LabelImpl; //##62

/**
 *  switch statement class.
 *  (by S. Fukuda)
**/
public class
SwitchStmtImpl extends StmtImpl implements SwitchStmt
{

// Components of switch statement
//   child1: Case selection expression.
//   child2: Jump table.
//   child3: Collection of statements to be selected.
//   child4: Indicate end of case statement.

private IrList caseList;  // Note that labels in the case list
        // may be changed by copy (or clone).
        // If label is required, do not use caseList but use child2.
  //##85 private Label  defaultLabel;
  public Label  defaultLabel; //##85
  //##85 private Label  endLabel;
  public Label  endLabel; //##85
/** SwitchStmtImpl
 *  SwitchStmt ->
 *               ( switchCode
 *                 Exp @         // Case selection expression.
 *                 JumpTable_ @  // List of constants and statement
 *                               // labels.
 *                 Stmt @        // Collection of statements to be
 *                               // selected.
 *                 LabelStmt_ @ )  // Indicates end of switch statement.
 *  JumpTable_ ->
 *               ( seqCode JumpList_ @
 *                         LabelNode @ ) // Default label.
 *  JumpList_ -> ( listCode List_of_SwitchCase @ )  // Corelate
 *                 // Exp value and list of SwitchCase_ pairs.
 *  SwitchCase_ -> ( seqCode ConstNode @   // Corelate Exp value and
 *                           LabelNode @ ) // switch statement label.
 *  child1: case selection expression
 *  child2: JumpTable of the form (seq jumpList LabelNode)
 *          where labelNode indicates default label, and
 *          jumpList is a list of const-label pairs
 *                (hirList (hirSeq constNode labelNode) ... )
 *  child3: statement containing statements to be selected.
 *  child4: LabeledStmt indicating end of switch statement.
 * If default statement does not appear (defaultLabel.getHIRposition()
 * is null), then the defaultLabel is attached to the switch end
 * statement (statement with endlabel).
**/
public
SwitchStmtImpl( HirRoot pHirRoot,
                Exp     pSelectionExp,
                IrList  pJumpList,
                Label   pDefaultLabel,
                Stmt    pBodyStmt,
                Label   pEndLabel )
{
  super(pHirRoot, HIR.OP_SWITCH);
  fAdditionalChild = new HIR[2];
  fChildCount = 4;

  caseList = pJumpList != null
           ? pJumpList
           : hirRoot.hir.irList();
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print(4, "SwitchStmt", "caseList " + caseList.toStringShort());
  for (java.util.ListIterator lIterator = caseList.iterator();
       lIterator.hasNext(); ) {
    HirSeq lConstLabelPair = (HirSeq)(lIterator.next());
    ////////SF031120[
    //lCaseLabel = ((LabelNode)(lConstLabelPair.getChild2())).getLabel();
    LabelNode lCaseLabelNode = (LabelNode)lConstLabelPair.getChild2();
    Label lCaseLabel = lCaseLabelNode.getLabel();
    ((LabelImpl)lCaseLabel).addToHirRefList(lCaseLabelNode); //##62
    ////////SF031120]
    lCaseLabel.setLabelKind(Label.SWITCH_CASE_LABEL);
    lCaseLabel.setOriginHir(this);
  }

  defaultLabel = pDefaultLabel!=null
               ? pDefaultLabel
               : hirRoot.symRoot.symTableCurrent.generateLabel();
  defaultLabel.setLabelKind(Label.SWITCH_DEFAULT_LABEL);
  defaultLabel.setOriginHir(this);
  ////////SF031120[
  LabelNode lDefaultLabelNode = hirRoot.hir.labelNode(defaultLabel);
  ((LabelImpl)defaultLabel).addToHirRefList(lDefaultLabelNode); //##62
  ////////SF031120]

  endLabel = pEndLabel != null
           ? pEndLabel
           : hirRoot.symRoot.symTableCurrent.generateLabel();
  endLabel.setLabelKind(Label.SWITCH_END_LABEL);
  endLabel.setOriginHir(this);
  LabeledStmt lSwitchEnd = hirRoot.hir.labeledStmt(endLabel,null);
  if (defaultLabel.getHirPosition() == null) // Default label is not yet
    lSwitchEnd.attachLabel(defaultLabel);

  setChildren( pSelectionExp,
               ////////SF031120[
               //hirRoot.hir.hirSeq( caseList, hirRoot.hir.labelNode(defaultLabel) ),
               hirRoot.hir.hirSeq((HirList)caseList,lDefaultLabelNode),
               ////////SF031120]
               pBodyStmt,
               lSwitchEnd );
  fType = pHirRoot.symRoot.typeVoid;
  if (fDbgLevel > 3) //##58
    hirRoot.ioRoot.dbgHir.print(4, " Child2", " " + ((HIR)getChild2()).toStringShort());
} // SwitchStmtImpl

public
SwitchStmtImpl() { }  // Dummy constructor for subclass.

public
SwitchStmtImpl( HirRoot pHirRoot )
{
  super(pHirRoot);
}

// get selection expression
public Exp
getSelectionExp()
{
  return (Exp)fChildNode1;
}

// set selection expression
public void
setSelectionExp(Exp pSelectionExp)
{
  setChild1(pSelectionExp);
}

// get case count
public int
getCaseCount()
{
  return getCaseList().size();
}

// get nth(head is 0) case const
// - if out of range , return null
public Const
getCaseConst(int index)
{
  HirList lCaseList = getCaseList();
  if( 0<=index && index<lCaseList.size() )
  {
    ConstNode node = (ConstNode)( (HirSeq)lCaseList.get(index) ).getChild1();
    return node.getConstSym();
  }
  return null;
}

// get nth(head is 0) case label
// - if out of range , return null
public Label
getCaseLabel(int index)
{
  HirList lCaseList = getCaseList();
  if( 0<=index && index<lCaseList.size() )
  {
    LabelNodeImpl node =
      (LabelNodeImpl)( (HirSeq)lCaseList.get(index) ).getChild2();
    return node.getLabel();
  }
  return null;
}

public LabelNode
getCaseLabelNode(int index)
{
  HirList lCaseList = getCaseList();
  if( 0<=index && index<lCaseList.size() )
  {
    LabelNodeImpl node =
      (LabelNodeImpl)( (HirSeq)lCaseList.get(index) ).getChild2();
    return (LabelNode)node;
  }
  return null;
}

// get default label
public Label
getDefaultLabel()
{
  return getDefaultLabelNode().getLabel();
}

public LabelNode
getDefaultLabelNode()
{
  HirSeq lJumpTable;
  lJumpTable = (HirSeq)getChild2();
  return (LabelNode)lJumpTable.getChild2();
}

// get break destination label
public Label
getEndLabel()
{
  return getSwitchEndNode().getLabel();
}

// get body statement
public Stmt
getBodyStmt()
{
  return (Stmt)fAdditionalChild[0];
}

public LabeledStmt
getSwitchEndNode()
{
  return (LabeledStmt)getChild(4);
}

/**Combine pStmt with conditional expression part pCond
 * of control statement so that pStmt should be executed before
 * pCond.
 * @param pStmt statement to be executed before pCond.
 * @param pCond conditional expression to be combined with pStmt.
 */
public void
combineWithConditionalExp(Stmt pStmt, HIR pCond) //##53
{
  insertPreviousStmt(pStmt);
  return;
} // combineWithConditionalExp

public Object
clone() throws CloneNotSupportedException {
  HIR lTree;
  try {
    lTree = (HIR)super.clone();
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(SwitchStmtImpl) " + this.toString());
    return null;
  }
  //## ((SwitchStmtImpl)lTree).caseList = (IrList)(caseList.clone());
  ((SwitchStmtImpl)lTree).caseList = ((HirList)caseList).hirListClone();
  ((SwitchStmtImpl)lTree).defaultLabel = defaultLabel;
  ((SwitchStmtImpl)lTree).endLabel     = endLabel;
  return (Object)lTree;
} // clone

/* //##85
public HIR
copyWithOperandsChangingLabels( IrList pLabelCorrespondence )
{
  HIR lTree = super.copyWithOperandsChangingLabels(pLabelCorrespondence);
  //## REFINE defaultLabel, endLabel.
  ((SwitchStmtImpl)lTree).defaultLabel
      = ((LabelNode)(lTree.getChild2().getChild2())).getLabel();
  ((SwitchStmtImpl)lTree).endLabel
      = ((LabeledStmt)(lTree.getChild(4))).getLabel();
  //##85 BEGIN
  for (HirIterator lIt = lTree.hirIterator(lTree.getChild2());
       lIt.hasNext(); ) {
    HIR lHir = lIt.next();
    if (lHir instanceof LabelNode) {
      Label lLabel = ((LabelNode)lHir).getLabel();
      lLabel.setOriginHir(lTree);
      if (fDbgLevel > 3)
        hirRoot.ioRoot.dbgHir.print(5, " setOrigin to " + lLabel.getName());
    }
  }
  //##85 END
  return lTree;
} // copyWithOperandsChangingLabels
*/ //##85

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atSwitchStmt(this);
}

private HirList
getCaseList()
{
  return (HirList)((HirSeq)getChild2()).getChild1();
}
} // SwitchStmtImpl class

