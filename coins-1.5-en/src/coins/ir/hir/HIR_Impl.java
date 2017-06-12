/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set; //##60

import coins.HasStringObject;
import coins.HirRoot;
import coins.MachineParam;
import coins.SourceLanguage;
//##63 import coins.aflow.Flow;
//##63 import coins.aflow.FlowExpId;
//##63 import coins.aflow.FlowResults;
import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.ExpId;
import coins.sym.FlagBox;
import coins.sym.FlowAnalSym;
import coins.sym.Label;
import coins.sym.PointerType;
import coins.sym.StringConst;
import coins.sym.Subp;
import coins.sym.SubpImpl;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;
//##63 import coins.aflow.FlowExpId;
//##63 import coins.aflow.SubpFlow;
//##63 import coins.aflow.FlowResults;
import coins.FlowRoot;
import coins.flow.SubpFlow; //##71

//========================================//

/** HIR_Impl class
**/
public class
HIR_Impl implements HIR, HasStringObject, Cloneable
{

//====== Field declarations ======//

  public final  HirRoot hirRoot;  // Used for accessing Root variables.

  protected int fOperator;    // Operation code
                              // IR.OP_xxx, HIR.OP_xxx
  protected IR  fChildNode1;  // Child node 1. Null if no child 1.
  protected IR  fChildNode2;  // Child node 2. Null if no child 2.
  protected IR  fAdditionalChild[] = null; // Child 3, 4, 5, ...
  protected IR  fParentNode;  // Parent node. Null if no parent.
  protected int fChildCount;  // Child count. (Fixed for each
                              //  kind of IR node.)
  protected HirAnnex fHirAnnex = null; // Optional information.
  protected Type     fType     = null; // Data type of this node.
  protected final int fDbgLevel;  //##58

//---- Static Fields ----//

  protected static MachineParam
    machineParam;

  protected static SourceLanguage
    sourceLanguage;

//===== Constructors ======//

/** Default constructor */
public
HIR_Impl() {
  hirRoot = null;
  fOperator = HIR.OP_NULL;
  fDbgLevel = 0; //##58
}

/** Constructor to set hirRoot */
public
HIR_Impl( HirRoot pHirRoot )
{
  hirRoot = pHirRoot;
  fOperator = HIR.OP_NULL;
  fDbgLevel = hirRoot.ioRoot.dbgHir.getLevel(); //##58
}

    /** Usually, HIR constructor should be given operation code. */
protected
HIR_Impl( HirRoot pHirRoot, int pOperator )
{
  hirRoot = pHirRoot;
  fOperator = pOperator;
  fDbgLevel = hirRoot.ioRoot.dbgHir.getLevel(); //##58
}

public void
setParameters( MachineParam pMachineParam, SourceLanguage pSourceLanguage)
{
  machineParam   = pMachineParam;
  sourceLanguage = pSourceLanguage;
}

//========================================//
//   Methods specified in IR interface    //
//========================================//

//==== Access methods available to build IR nodes. ====//

//==== Methods to set/get informatin to/from IR node ====//

public int
getOperator() { return fOperator; }

public IR
getParent(){ return fParentNode; }

public void
setParent( IR pParent )
{
  fParentNode = pParent;
}

public int
getChildCount() {
  return fChildCount;
}

public IR
getChild1() { return (IR)fChildNode1; }

public IR
getChild2() { return (IR)fChildNode2; }

public IR
getChild( int pNumber ) {
  if (pNumber < 1 || fChildCount < pNumber) {
    return null;
  }
  switch (pNumber) {
  case 1 :  return (IR)fChildNode1;
  case 2 :  return (IR)fChildNode2;
  default : return (IR)fAdditionalChild[pNumber - 3];
  }
} // getChild

public void
setChild1( IR pChild ) {
  //##92 if (fChildNode1 != null)              //##92
  //##92   ((HIR)fChildNode1).setParent(null); //##92
  fChildNode1 = pChild;
  if (pChild != null)
    ((HIR_Impl)pChild).setParent(this);
}

public void
setChild2( IR pChild ) {
  //##92 if (fChildNode2 != null)              //##92
  //##92   ((HIR)fChildNode2).setParent(null); //##92
  fChildNode2 = pChild;
  if (pChild != null)
    ((HIR_Impl)pChild).setParent(this);
}

/** setChild
 *  Set pHir as pNumber-th child of this node.
 *  @param pNumber Child number
 *  @param pIR Child node to be connected.
**/
public void
setChild( int pNumber, IR pHir ) {
  IR lChildNode;
  if ((pNumber > 0)&&(pNumber <= fChildCount)) {
    lChildNode = getChild(pNumber);
    switch (pNumber) {
    case 1 :
      //##92 if (fChildNode1 != null)              //##92
      //##92   ((HIR)fChildNode1).setParent(null); //##92
      fChildNode1 = pHir;
      break;
    case 2 :
      //##92 if (fChildNode2 != null)              //##92
      //##92   ((HIR)fChildNode2).setParent(null); //##92
      fChildNode2 = pHir;
      break;
    default :
      //##92 if (fAdditionalChild[pNumber-3] != null)              //##92
      //##92   ((HIR)fAdditionalChild[pNumber-3]).setParent(null); //##92
      fAdditionalChild[pNumber-3] = pHir;
    }
    if (pHir != null)
      ((HIR_Impl)pHir).setParent(this); //##54
  }else
    hirRoot.ioRoot.msgRecovered.put( 1001, "Too big child number in setChild ");
} // setChild

public int
getChildNumber() {
  int lChildNumber, lChildCount;
  if (fParentNode != null) {
    lChildCount = fParentNode.getChildCount();
    for (lChildNumber = 1; lChildNumber <= lChildCount; lChildNumber++) {
      if (this == fParentNode.getChild(lChildNumber))
        break;
    }
    if (lChildNumber > lChildCount)
      lChildNumber = -1;
  }else
    lChildNumber = -1;
  return lChildNumber;
} // getChildNumber

public void
addInf( String pInfKindInterned, Object pInfObject )
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.addInf(pInfKindInterned, pInfObject);
} // addInf

public Object
getInf( String pInfKindInterned)
{
  if (fHirAnnex == null)
    return null;
  else
    return fHirAnnex.getInf(pInfKindInterned);
}

public void
removeInf( String pInfKindInterned)
{
  if (fHirAnnex != null)
    fHirAnnex.removeInf(pInfKindInterned);
}

public IrList
getInfList()
{
  if (fHirAnnex != null)
    return fHirAnnex.getInfList();
  else
    return null;
}

//##70 BEGIN
public void
copyInfListFrom( HIR pFromHir )
{
  if (pFromHir == null)
    return;
  IrList lInfList = pFromHir.getInfList();
  if (lInfList != null) {
    for (Iterator lListIt = lInfList.iterator();
         lListIt.hasNext(); ) {
      String lInfKind = (String)lListIt.next();
      Object lInfObj  = lListIt.next();
      addInf(lInfKind, lInfObj);
    }
  }
} // copyInfListFrom

public String
getInfString()
{
  if (fHirAnnex != null)
    return fHirAnnex.toStringInf();
  else
    return "";
}
//##70 END

public boolean
withInf()
{
  if ((fHirAnnex != null)&&(fHirAnnex.getInfList() != null))
    return true;
  else
    return false;
}

public Object
getWork()
{
  if (fHirAnnex == null)
    return null;
  else
    return fHirAnnex.getWork();
}

public void
setWork( Object pWork )
{
  if ((pWork == null)&&(fHirAnnex == null)) //##62
    return; //##62
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setWork(pWork);
}

public int
getIndex(){
  if (fHirAnnex == null)
    return 0;
  else
    return fHirAnnex.getIndex();
}

public void
setIndex( int pIndex )
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setIndex(pIndex);
}

public boolean
isStmt() {
  if (this instanceof Stmt)
    return true;
  else
    return false;
} // isStmt

public Stmt
getStmtContainingThisNode()
{
  HIR  lNode;
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, "\n getStmtContainingThisNode for " +
      toStringShort()); //##71;
  Stmt lStmt = null;
  if (this instanceof Stmt)
    lStmt = (Stmt)this;
  else {
    for (lNode = (HIR)fParentNode; lNode != null;
         lNode = (HIR)lNode.getParent()) {
      if (lNode instanceof Stmt) {
        lStmt = (Stmt)lNode;
        break;
      }
    }
  }
  if ((lStmt != null)&&(!(lStmt instanceof ExpStmt))) { //##71
    if ((lStmt.getParent()instanceof LabeledStmt) ||
        (lStmt.getParent()instanceof ExpStmt))
      lStmt = (Stmt)lStmt.getParent();
  } //##71
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, " return " + lStmt); //##71;
  return lStmt;
}// getStmtContainingThisNode

//====== IR methods requiring special attention ======//

public ExpId
  getExpId()
{
  ExpId lExpId = null;
  //##65 if ((hirRoot.getFlowRoot() != null) &&
  //##65     (hirRoot.getFlowRoot().subpUnderAnalysis != null)) { //##53
  if (hirRoot.getFlowRoot() != null) { //##65
    //##60 BEGIN
    coins.flow.SubpFlow lRevisedSubpFlow = hirRoot.getFlowRoot().fSubpFlow; //##60
    if ((lRevisedSubpFlow != null)
      &&(lRevisedSubpFlow.getSubpSym() == hirRoot.symRoot.subpCurrent)) //##78
    {
      lExpId = lRevisedSubpFlow.getExpId(this);
    }
    //##60 END
    /* //##63
    coins.aflow.SubpFlow lSubpFlow = hirRoot.getFlowRoot().aflow.getSubpFlow();
    if (lSubpFlow != null) {
      FlowResults lFlowResults = lSubpFlow.results();
      if (lFlowResults != null) {
        if (lFlowResults.containsKey("FlowExpIdForNode", this)) {
          FlowExpId lFlowExpId = lFlowResults.getFlowExpIdForNode(this);
          if (lFlowExpId != null) {
            lExpId = lFlowExpId.getExpId();
          }
        }
      }
    }
    */ //##63
  }
  return lExpId;
} // getExpId
/* //##78
public void
  setExpId(ExpId pId) // Deprecated. Use coins.flow.setExpId(IR). //##60
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setExpId(pId);
} // setExpId
*/ //##78
public FlowAnalSym
getSymOrExpId()
{
  Sym lSym;
  if (this instanceof SymNode) {
    lSym = getSym();
    if (! (lSym instanceof FlowAnalSym))
      lSym = null;
  }else
    lSym = getExpId();
  return (FlowAnalSym)lSym;
} // getSymOrExpId

public FlowAnalSym
getFlowAnalSym( )
{
  /* //##78
  if (fHirAnnex == null)
    return null;
  else
    return fHirAnnex.getFlowAnalSym();
  */ //##78
 return getSymOrExpId(); //##78
} // getFlowAnalSym

/* //##78
public void
setFlowAnalSym( ExpId pId )
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setFlowAnalSym(pId);
} // setFlowAnalSym
*/ //##78

public HIR getSourceNode1(){ return (HIR)fChildNode1; }

public HIR getSourceNode2(){ return (HIR)fChildNode2; }

public HIR
getSourceNode( int pNumber ){
  if ((pNumber <= 0) || (fChildCount < pNumber)) {
    return null;
  } else if (pNumber == 1) {
    return (HIR)fChildNode1;
  } else if (pNumber == 2) {
    return (HIR)fChildNode2;
  } else
    return (HIR)fAdditionalChild[pNumber - 3];
} // getSourceNode

public Sym
getSym(){ return null; }

public Sym
getResultOperand(){
  if ((fOperator == HIR.OP_ASSIGN)&&(fChildNode1 != null)) {
      return fChildNode1.getSym();
  }else
    return null;
} // getResultOperand

public Sym
getResultVar(){
  if ((fOperator == HIR.OP_ASSIGN)&&(fChildNode1 != null)) {
      return fChildNode1.getSym();
  }else
    return null;
} // getResultVar

public HIR  //##56
replaceSource1( HIR pOperand ) {
  HIR lResult = pOperand; //##56
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, " replaceSource1 " + toStringShort(),
      "to " + (pOperand == null ? "null" : ((HIR)pOperand).toStringShort()));
  fChildNode1 = pOperand;
  if (pOperand != null) {
//    ((HIR)pOperand).cutParentLink();
    ((HIR_Impl)pOperand).setParent(this); //##54
    pOperand.checkLinkage("replaceSource1"); //##70
  }
  return lResult;  //##56
} // replaceSource1

public HIR //##56
replaceSource2( HIR pOperand ) {
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, " replaceSource2 " + toStringShort(),
      "to " + (pOperand == null ? "null" : ((HIR)pOperand).toStringShort()));
  fChildNode2 = pOperand;
  if (pOperand != null) {
//    ((HIR)pOperand).cutParentLink();
    ((HIR_Impl)pOperand).setParent(this); //##54
    pOperand.checkLinkage("replaceSource2"); //##70
  }
  return pOperand; //##56
} // replaceSource2

// public void
public HIR    //##56
replaceSource( int pNumber, IR pOperand ){
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, " replaceSource "  + pNumber + " " + toStringShort(),
      "to " + (pOperand == null ? "null" : ((HIR)pOperand).toStringShort()));
  HIR lResult = (HIR)pOperand;  //##56
  if ((pNumber <= 0) || (fChildCount < pNumber))
    return lResult;  // Illegal parameter. Do not replace.
  if (pNumber == 1) {
    fChildNode1 = pOperand;
  } else if (pNumber == 2) {
    fChildNode2 = pOperand;
  } else {
    fAdditionalChild[pNumber-3] = pOperand;
  }
  if (pOperand != null) {
//    ((HIR)pOperand).cutParentLink();
    ((HIR_Impl)pOperand).setParent(this); //##54
    ((HIR)pOperand).checkLinkage("replaceSource:"+pNumber); //##70
  }
  return lResult; //##56
} // replaceSource

public void
replaceResultOperand( HIR pOperand ) { } // Only fo AssignStmt

public void
replaceResultVar( HIR pOperand ) { } // Only fo AssignStmt

public HIR                     //##56
replaceThisNode( HIR pNewNode )
{
  HIR lParent;
  int lChildNumber, lPosition;
  HIR lResult = pNewNode; //##56
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(3, "replaceThisNode " + toString(),
      "to " + (HIR)pNewNode); //##79
  if (pNewNode == this) //##80
    return lResult;  //##80
  if (lResult != null) //##79
    lResult.cutParentLink(); //##77
  lParent = (HIR)getParent();
  if (lParent != null) {
    lChildNumber = getChildNumber();
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, "parent " + lParent.toString() +
        " childNumber " + lChildNumber);
    if (lParent instanceof BlockStmt) {
      if ((this instanceof Stmt)&&(pNewNode instanceof Stmt)) {
        lResult = ((Stmt)this).replaceThisStmtWith((Stmt)pNewNode);
      }else {
        hirRoot.ioRoot.msgRecovered.put(1111,
          "replaceThisNode expects Stmt in BlockStmt " +
           toString() + " " + pNewNode.toString());
      }
    }else {
      if (lChildNumber > 0) {
        lResult = lParent.replaceSource(lChildNumber, (HIR)pNewNode);
      }
      else { // No child number is given. Parent may be a list or a block.
        if (lParent instanceof HirList) {
          lPosition = ((HirList)lParent).indexOf(this);
          if (lPosition >= 0)
            ((HirList)lParent).set(lPosition, pNewNode);
          else
            hirRoot.ioRoot.msgRecovered.put(1111,
              "In replaceThisNode, HirList does not contain this node " +
              toString());
        }
        else {
          hirRoot.ioRoot.msgRecovered.put(1111,
            "replaceThisNode expects HirList as 0-child parent " +
            lParent.toString() + " of " + toString());
        }
      }
    }
  }else {
    if (hirRoot.ioRoot.dbgHir.getLevel() > 0) //##79
      hirRoot.ioRoot.dbgHir.print(2,
        "replaceThisNode " + toString() + " has no parent" );
  }
  if (pNewNode != null) //##79
    pNewNode.checkLinkage("replaceThisNode"); //##70
  return lResult;  //##56
} // replaceThisNode

public void replaceOperator( int pOperator ){
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.msgWarning.put(" Do not use replaceOperator. It is a deleted method.");
  fOperator = pOperator;
}

public void
cutParentLink() {
  int lChildNumber;
  HIR lParent = (HIR)getParent();
  if (lParent == null)   // No parent link.
    return;
  lChildNumber = getChildNumber();
  ((HIR_Impl)this).fParentNode = null;   // Cut the link to parent.
  if (lChildNumber < 0)  // No link from parent.
    return;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(6, "cutParentLink of chld." +  lChildNumber +
   " " + toStringShort() + " to " + lParent.toStringShort());
  lParent.setChild(lChildNumber, null);  // Cut the link from parent.
  return;
} // cutParentLink

public void
cutParentLink( int pChildNumber )
{
  HIR lParent = (HIR)getParent();
  if (lParent == null)   // No parent link.
    return;
  ((HIR_Impl)this).fParentNode = null;   // Cut the link to parent.
  if (pChildNumber < 0)  // No link from parent.
    return;
  if (lParent.getChild(pChildNumber) != this)
    return;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(6, "cutParentLink of chld." +  pChildNumber +
   " " + toStringShort() + " to " + lParent.toStringShort());
  lParent.setChild(pChildNumber, null);  // Cut the link from parent.
  return;
} // cutParentLink

//==== Methods of HasStringObject and methods for printing ====//

public boolean
isSym() { return false; }

public boolean
isHIR() { return true; }

public boolean
isLIR() { return false; }

public boolean
isEmpty( HIR pHir )
{
  if ((pHir == null)||
      (pHir instanceof NullNode)||
      (pHir.getOperator() == HIR.OP_NULL)||
      ((pHir instanceof LabeledStmt)&&
       (pHir.isEmpty(((LabeledStmt)pHir).getStmt())))||
      ((pHir instanceof ExpStmt)&&
       (pHir.isEmpty(((ExpStmt)pHir).getExp()))))
    return true;
  else
    return false;
} // isEmpty

public boolean
isTerminal()
{
  if (fChildCount > 0)
    return false;
  if (this instanceof IrList)
    return false;
  else
    return true;
} // isTerminal

public boolean
contains( HIR pSubtree ) //##53
{
  if (pSubtree == null)
    return false;
  HIR lHir;
  //##71 BEGIN
  if ((hirRoot.getFlowRoot() != null)&&
      (hirRoot.getFlowRoot().fSubpFlow != null)&&
      (hirRoot.getFlowRoot().fSubpFlow.isComputed(SubpFlow.DF_EXPID))) {
    for (HirIterator lIterator = hirIterator(this);
        lIterator.hasNext(); ) {
     lHir = lIterator.next();
     if (lHir == pSubtree)
       return true;
     if ((lHir.getExpId() != null)&&
         (pSubtree.getExpId() != null)&&
         (lHir.getExpId() == pSubtree.getExpId()))
       return true;
   }
  }else {
    //##71 END
    for (HirIterator lIterator = hirIterator(this);
         lIterator.hasNext(); ) {
      lHir = lIterator.next();
      if (lHir == pSubtree)
        return true;
    }
  } //##71
  return false;
} // contains

public String
toString()
{
  String nodeString;
  nodeString = HIR.OP_CODE_NAME[fOperator] + " " + getIndex();
  if (fType != null)
    nodeString = nodeString + " " + fType.getName();
  if ((fDbgLevel >= 3)&&
      (hirRoot.ioRoot.dbgFlow.getLevel() > 0)) {
    ExpId lExpId = getExpId();
    if (lExpId != null)
      nodeString = nodeString + " " + lExpId.getName();
  }
  if (fDbgLevel >= 6) {  //##60
    nodeString = nodeString + " parent ";
    if (getParent() != null)
      nodeString = nodeString+((HIR)getParent()).toStringShort();
    else nodeString = nodeString + "NULL";
  }
  return nodeString;
} // toString

public String
toStringShort()
{
  String nodeString;
  nodeString = HIR.OP_CODE_NAME_DENSE[fOperator] + " " + getIndex();
  return nodeString;
} // toStringShort

public String
  toStringDetail()
{
  String nodeString = toString();
  //##60 BEGIN
  // ExpId lExpId = getExpId();
  // if (lExpId != null)
  //   nodeString = nodeString + " " + lExpId.getName();
  //##60 END
  if (!flagsAreAllFalse()) {
    if (fDbgLevel >= 3)
      nodeString = nodeString + getFlagBox().toString();
  }
  if ((fHirAnnex != null) && (fHirAnnex.getInfList() != null)&&
      (getOperator() != OP_INF)) { //##70
    nodeString = nodeString + " " + fHirAnnex.toStringInf();
  }
    /* //##60
    if (this instanceof Stmt) {
      if (((Stmt)this).getFileName() != null) {
        nodeString = nodeString + " file " + ((Stmt)this).getFileName()
            + " line " + ((Stmt)this).getLineNumber();
      }
       }
     */
    //##60
  return nodeString;
} // toStringDetail

public String
toStringWithChildren()
{
  StringBuffer lBuffer = new StringBuffer();
  if (fChildCount > 0) {
    lBuffer.append("(" + HIR.OP_CODE_NAME_DENSE[fOperator] + " ");
    if (this instanceof BlockStmt) {
      for (Stmt lStmt = ((BlockStmt)this).getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        lBuffer.append(lStmt.toStringWithChildren());
      }
    }else { // Not a block
      for (int i = 1; i <= fChildCount; i++) {
        HIR lChild = (HIR) getChild(i);
        if (lChild != null)
          lBuffer.append(lChild.toStringWithChildren());
        else
          lBuffer.append("null");
      }
    }
    lBuffer.append(")");
  }else { // Child count is 0.
    if (this instanceof SymNode) {  // ConstNode is also a SymNode.
      lBuffer.append("<" + HIR.OP_CODE_NAME_DENSE[fOperator] + " ");
      lBuffer.append(((SymNode)this).getSymNodeSym().getName() + ">");
    }else if (this instanceof IrList) {
      Object lObject;
      lBuffer.append("(" + HIR.OP_CODE_NAME_DENSE[fOperator] + " ");
      for (java.util.Iterator listIterator = ((IrList)this).iterator();
           listIterator.hasNext(); ) {
        lObject = listIterator.next();
        if (lObject instanceof HIR)
          lBuffer.append(((HIR)lObject).toStringWithChildren());
        else if (lObject instanceof Sym)
          lBuffer.append("<sym " +((Sym)lObject).getName() + ">");
        else if (lObject != null)
          lBuffer.append(lObject.toString()+" ");
      }
      lBuffer.append(")");
    }else if (this instanceof IrList) {
      Object lObject;
      lBuffer.append("(" + HIR.OP_CODE_NAME_DENSE[fOperator] + " ");
      for (java.util.Iterator listIterator = ((IrList)this).iterator();
           listIterator.hasNext(); ) {
        lObject = listIterator.next();
        if (lObject instanceof HIR)
          lBuffer.append(((HIR)lObject).toStringWithChildren());
        else if (lObject instanceof Sym)
          lBuffer.append("<sym " +((Sym)lObject).getName() + ">");
        else if (lObject != null)
          lBuffer.append(lObject.toString()+" ");
      }
      lBuffer.append(")");
    //##58 BEGIN
    }else if (this instanceof LabelDef) {
      lBuffer.append("<" + HIR.OP_CODE_NAME_DENSE[fOperator] + " ");
      lBuffer.append(((LabelDef)this).getLabel().getName() + ">");
    //##58 END
    }else  {
      lBuffer.append("<" + toStringShort() + ">");
    }
  }
  return lBuffer.toString();
} // toStringWithChildren

public String
getIrName() {
  String nodeString;
  nodeString = HIR.OP_CODE_NAME_DENSE[fOperator] + " " + getIndex();
  return nodeString;
} // getIrName

public void
print(int pIndent)
{
  print(pIndent, false);
}

public void
print(int pIndent, boolean pDetail)
{
  String lineImage, lSpace;
  lSpace = hirRoot.hir.getIndentSpace(pIndent);
  if (pDetail) {
    if (getChildCount() == 0) {
      lineImage = lSpace + "<" + this.toStringDetail();
    }else {
      if (this instanceof HirSeq)
        lineImage = lSpace + "(seq "  + getIndex() + " " + getType().toStringShort();
      else
        lineImage = lSpace + "(" + this.toStringDetail();
    }
  }else {
    if (getChildCount() == 0) {
      lineImage = lSpace + "<" + this.toString();
    }else {
      if (this instanceof HirSeq)
        lineImage = lSpace + "(seq " + getIndex();
      else
        lineImage = lSpace + "(" + this.toString();
      }
  }
  if (getChildCount() == 0) {
    if (this instanceof HirList) {
      ((HirList)this).print( pIndent, pDetail );
    }else
      hirRoot.ioRoot.printOut.print("\n" + lineImage + ">");
  }else {
    hirRoot.ioRoot.printOut.print("\n" + lineImage);
    if (fChildNode1 != null) {
      //##58 BEGIN
      if (this instanceof BlockStmt) {
        for (Stmt lStmt = ((BlockStmt)this).getFirstStmt();
          lStmt != null;
          lStmt = lStmt.getNextStmt()) {
          lStmt.print(pIndent+1, pDetail);
        }
     }else
     //##58 END
       fChildNode1.print( pIndent+1, pDetail );
       //## ((HIR)fChildNode1).print( pIndent+1, pDetail );
    }else
      hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>");
    if (fChildCount >= 2) {
      if (fChildNode2 != null)
        fChildNode2.print( pIndent+1, pDetail );
      else
        hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>");
      for (int i = 2; i < fChildCount; i++) {
        if (fAdditionalChild[i-2] != null)
          fAdditionalChild[i-2].print(pIndent+1, pDetail);
        else
          hirRoot.ioRoot.printOut.print("\n" + lSpace + "<null 0 void>");
      }
    }
    if (!(this instanceof HirList))
      hirRoot.ioRoot.printOut.print(")");
  }
  //##58 if (getNextStmt() != null)
  //##58   getNextStmt().print( pIndent, pDetail );
} // print

public String
getIndentSpace( int pIndent )
{
  StringBuffer lSpace = new StringBuffer();
  int          localIndent = pIndent;
  lSpace.append(" ");
  if (localIndent < 0)
    localIndent = 0;
  localIndent = localIndent % 60;  // May change !
  for (int i = 0; i < localIndent; i++)
    lSpace.append(" ");
  return lSpace.toString();
} // getIndentSpace

//##51 BEGIN
//##62 BEGIN
public int
  setIndexNumberToAllNodes(int pStartNumber)
{
  return setIndexNumberToAllNodes(pStartNumber, false);
}

public int
  setIndexNumberToAllNodes(int pStartNumber, boolean pResetSymIndex)
//##62 END
{
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(2, "setIndexNumberToAllNodes under ",
      toStringShort() + " starting value " + pStartNumber +
      " resetSymIndex " + pResetSymIndex);
  //##65 BEGIN
  if ((hirRoot.getFlowRoot() != null) &&
      (hirRoot.getFlowRoot().fSubpFlow != null)) {
    hirRoot.getFlowRoot().fSubpFlow.resetComputedFlag(
      hirRoot.getFlowRoot().fSubpFlow.CF_INDEXED);
  }
  //##65 END
  int lIndex = pStartNumber;
  if (this instanceof Program) {
    this.setIndex(lIndex);
    lIndex++;
    if (getChild2() != null)
      //##60 lIndex = ((HIR_Impl)getChild2()).setIndexNumberToAllNodes1(lIndex);
      lIndex = ((HIR_Impl)getChild2()).setIndexNumberToAllNodes2(lIndex, pResetSymIndex); //##60
    coins.ir.IrList subpDefList
      = ((Program)hirRoot.programRoot).getSubpDefinitionList();
    Iterator subpDefIterator = subpDefList.iterator();
    while (subpDefIterator.hasNext()) {
      SubpDefinition subpDef = (SubpDefinition)(subpDefIterator.next());
      lIndex = subpDef.setIndexNumberToAllNodes(lIndex);
    }
  }
  else if (this instanceof SubpDefinition) {
    if (fDbgLevel > 0) //##58
      hirRoot.ioRoot.dbgHir.print(2, " " +
        ((SubpDefinition)this).getSubpSym().getName());
    ((SubpDefinitionImpl)this).fNodeIndexMin = pStartNumber;
    this.setIndex(lIndex); // SubpDef
    lIndex++;
    ((HIR)getChild1()).setIndex(lIndex); // subpSym node
    lIndex++;
    IrList lLabelDefList;
    if (getChild2() != null)
      //##60 lIndex = ((HIR_Impl)getChild2()).setIndexNumberToAllNodes1(lIndex);
      lIndex = ((HIR_Impl)getChild2()).setIndexNumberToAllNodes2(lIndex, pResetSymIndex); //##60
    if (((SubpDefinition)this).getHirBody() != null)
      //##60 lIndex = ((HIR_Impl)((SubpDefinition)this).getHirBody()).setIndexNumberToAllNodes1(lIndex);
      lIndex = ((HIR_Impl)((SubpDefinition)this).getHirBody()).
        setIndexNumberToAllNodes2(lIndex, pResetSymIndex); //##60
    ((SubpDefinitionImpl)this).fNodeIndexMax = lIndex;
    if (fDbgLevel > 0) //##58
      hirRoot.ioRoot.dbgHir.print(3, "\nSubpDef min " +
        ((SubpDefinition)this).getNodeIndexMin() + " max " +
        ((SubpDefinition)this).getNodeIndexMax());
  }
  else {
    if (getOperator() != HIR.OP_NULL)
      hirRoot.ioRoot.msgWarning.put(1220,
        "setIndexNumberToAllNodes should be specified for Program or SubpDefinition "
        + " given is " + toStringShort());
  }
  if ((hirRoot.getFlowRoot() != null) &&
      //##60  (hirRoot.getFlowRoot().subpFlow != null))
      //##60 hirRoot.getFlowRoot().subpFlow.setRestructureFlag();
      (hirRoot.getFlowRoot().fSubpFlow != null)) { //##60
    //##65 hirRoot.getFlowRoot().fSubpFlow.setRestructureFlag(); //##60
    hirRoot.getFlowRoot().fSubpFlow.setComputedFlag( //##60
      hirRoot.getFlowRoot().fSubpFlow.CF_INDEXED); //##60
  }
  return lIndex;
} // setIndexNumberToAllNodes

public int
  setIndexNumberToAllNodes2(int pStartNumber, boolean pResetSymIndex) //##60
{
  int lIndex = pStartNumber;
  if (fDbgLevel > 4)
    hirRoot.ioRoot.dbgHir.print(7, "setIndexNumberToAllNodes2 ",
      toStringShort() +
      " startValue " + pStartNumber); //###
  setIndex(lIndex);
  lIndex++;
  if (fChildCount == 0) {
    if (this instanceof HirList) {
      HirList lHirList = (HirList)this;
      //##64 for (int lElemIndex = 0; lElemIndex < lHirList.size(); lElemIndex++) {
      //##64   HIR lElem = (HIR)lHirList.get(lElemIndex);
      int lListSize = lHirList.size(); //##64
      int lListIndex = 0; //##64
      for (Iterator lIteratorL1 = lHirList.iterator(); //##64
           lIteratorL1.hasNext(); ) { //##64
        Object lObject = lIteratorL1.next(); //##70
        if (lObject instanceof HIR) { //##70
          HIR lElem = (HIR)lObject; //##70
          lListIndex++; //##64
          if (lListIndex > lListSize) //##64
            break; // Escape from errornous list (ring form). //##64
          if (lElem != null)
            lIndex = ((HIR_Impl)lElem).setIndexNumberToAllNodes2(lIndex,
              pResetSymIndex);
        }// else error
      }
    }
    else if (this instanceof ExpListExp) {
      ExpListExp lExpListExp = (ExpListExp)this;
      //##64 for (int lElemIndex = 0; lElemIndex < lExpListExp.size(); lElemIndex++) {
      //##64   HIR lElem = (HIR)lExpListExp.getExp(lElemIndex);
      for (Iterator lIteratorL2 = lExpListExp.iterator(); //##64
           lIteratorL2.hasNext(); ) { //##64
        HIR lElem = (HIR)lIteratorL2.next(); //##64
        if (lElem != null)
          lIndex = ((HIR_Impl)lElem).setIndexNumberToAllNodes2(lIndex, pResetSymIndex);
      }
    //##60 BEGIN
    }else if (this instanceof LabelDef) {
      Label lLabel = ((LabelDef)this).getLabel();
      //##62 if (pResetSymIndex)  //##62
      //##62   lLabel.resetHirRefList(); //##62
      if (getParent() instanceof IrList) {
        if (getParent().getParent() instanceof LabeledStmt) {
          LabeledStmt lLabeledStmt = (LabeledStmt)getParent().getParent();
          if (fDbgLevel > 0)
            hirRoot.ioRoot.dbgHir.print(5, " setHirPosition of", lLabel.getName()
              + " as " + this.toStringShort());
          lLabel.setHirPosition(lLabeledStmt);
        }
      }
      //##60 END
      //##62 BEGIN
    }else if (pResetSymIndex && (this instanceof SymNode)) {
      Sym lSymNodeSym = ((SymNode)this).getSymNodeSym();
      if (lSymNodeSym instanceof FlowAnalSym)
        ((FlowAnalSym)lSymNodeSym).resetFlowAnalInf();
    //##62 END
    }
  }
  else {
    if (this instanceof BlockStmt) {
      BlockStmt lBlockStmt = (BlockStmt)this;
      for (Stmt lStmt = lBlockStmt.getFirstStmt(); lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        lIndex = ((HIR_Impl)lStmt).setIndexNumberToAllNodes2(lIndex, pResetSymIndex);
      }
    }
    else {
      for (int lChildNumber = 1; lChildNumber <= fChildCount;
           lChildNumber++) {
        HIR lChild = (HIR)getChild(lChildNumber);
        if (lChild != null) {
          lIndex = ((HIR_Impl)lChild).setIndexNumberToAllNodes2(lIndex, pResetSymIndex);
        }
      }
    }
  }
  return lIndex;
} // setIndexNumberToAllNodes2

/* //##60
protected int
setIndexNumberToAllNodes1( int pStartNumber ) //##51
{
  HIR  lHir, lSubpBody;
  IR   lIr;
  int  lIndex = pStartNumber;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(2, "setIndexNumberToAllNodes1 ", toStringShort() +
      " startValue " + pStartNumber);  //###
  HIR lHead = this;
  for (HirIterator lHirIterator = hirRoot.hir.hirIterator(this);
       lHirIterator.hasNext(); ) {
    lHir = lHirIterator.next();
    if (lHir != null) {
      lHir.setIndex(lIndex);
      lIndex++;
    }
  }
  return lIndex;
} // setIndexNumberToAllNodes1
*/ //##60

public boolean
finishHir()  //##61
{
  if (fDbgLevel > 0) {
    if (this instanceof SubpDefinition) //##64
      hirRoot.ioRoot.dbgHir.print(2, "\nfinishHir",
        ((SubpDefinition)this).getSubpSym().getName()); //##64
    else //##64
      hirRoot.ioRoot.dbgHir.print(2, "\nfinishHir", toString()); //##64
  }
  hirRoot.hirModify.modifyHirIfNecerrary((HIR)hirRoot.programRoot); //##88
  if ((this instanceof Program)||
      (this instanceof SubpDefinition)) {
    int lIndexStartNumber = this.getIndex();
    if (lIndexStartNumber <= 0)
      lIndexStartNumber = 1;
    this.setIndexNumberToAllNodes(lIndexStartNumber);
    boolean lTreeStructure = this.isTree();
    if (lTreeStructure)
      hirRoot.ioRoot.dbgHir.print(2, "\n" + toString() +
        " does not violate tree structure.\n");
    if (this instanceof Program) {
      //##71 BEGIN
      // Add/remove functionsWithoutSideEfffect by infStmt such as
      //  #pragma optControl functionsWithoutSideEffect  subp1 subp2 ...
      //  #pragma optControl functionsWithSideEffect  subp11 subp12 ...
      boolean lModifyFunctionsWithoutSideEffect = false;
      HIR lProgInitPart = (HIR)((Program)this).getInitiationPart();
      if (lProgInitPart instanceof BlockStmt) {
        for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
             lStmt != null;
             lStmt = lStmt.getNextStmt()) {
          IrList lSubpList = null;
          boolean lWithSideEffect = true;
          if ((lStmt instanceof InfStmt)&&
              (((InfStmt)lStmt).getInfKind() == "optControl")) {
            InfStmt lInfStmt = (InfStmt)lStmt;
            IrList lOptionList = ((InfStmt)lStmt).getInfList("optControl");
            String lOptionName = ((InfStmt)lStmt).getInfSubkindOf("optControl");
            hirRoot.ioRoot.dbgHir.print(4, " subkind " + lOptionName
              + " option list " + lOptionList + "\n");
            if (lOptionName == null) {
              hirRoot.ioRoot.dbgHir.print(1, "\nUnknown option subkind " +
                      ((InfStmt)lStmt).toString() + "\n");
              continue;
            }
            int lIndex;
            boolean lToBeChanged = false;
            if (lOptionName == "functionsWithoutSideEffect") {
              lWithSideEffect = false;
              lToBeChanged = true;
            } else if (lOptionName == "functionsWithSideEffect") {
              lWithSideEffect = true;
              lToBeChanged = true;
            }
            if (lToBeChanged) {
              lIndex = 0;
              for (Iterator lIt = lOptionList.iterator();
                   lIt.hasNext(); lIndex++) {
                Object lSubp = lIt.next();
                String lSubpName;
                if (lIndex <= 0) // First element is functionsWithoutSideEffect or
                  continue;      //                  functionsWithSideEffect.
                if (lSubp instanceof Sym) {
                  lSubpName = ((Sym)lSubp).getName().intern();
                }
                else if (lSubp instanceof String) {
                  lSubpName = ((String)lSubp).intern();
                }
                else {
                  hirRoot.ioRoot.dbgHir.print(2, "unexpected object " +
                    lSubp + " " + lSubp.getClass());
                  continue;
                }
                if (lWithSideEffect) {
                  // functionsWithSideEffect.
                  if (hirRoot.symRoot.sourceLanguage.
                    functionsWithoutSideEffect.contains(lSubpName)) {
                    hirRoot.ioRoot.dbgHir.print(3, " remove " + lSubpName);
                    hirRoot.symRoot.sourceLanguage.
                      functionsWithoutSideEffect.remove(lSubpName);
                    lModifyFunctionsWithoutSideEffect = true;
                  }
                }
                else {
                  // functionsWithoutSideEffect.
                  hirRoot.ioRoot.dbgHir.print(3, " add " + lSubpName);
                  hirRoot.symRoot.sourceLanguage.
                    functionsWithoutSideEffect.add(lSubpName);
                  lModifyFunctionsWithoutSideEffect = true;
                }
              } // End of option list iteration
            } // lOptionList != null
          } // InfStmt
        } // End for each Stmt in InitiationPart
      } // End of InitiationPart processing.
      if (lModifyFunctionsWithoutSideEffect) {
        hirRoot.ioRoot.dbgHir.print(3, "functionsWithoutSideEffect " +
          hirRoot.symRoot.sourceLanguage.
          functionsWithoutSideEffect);
      }
      if (fDbgLevel > 0) {
        for (Iterator lSymIt = hirRoot.symRoot.safeArray.iterator();
             lSymIt.hasNext(); ) {
          Sym lSym = (Sym)lSymIt.next();
          String lOwner = lSym.getDefinedInName();
          hirRoot.ioRoot.dbgHir.print(1, "safeArray:", lSym.getName()
            + " in " + lOwner);
        }
      }
      //##71 END
      for (Iterator lSubpIterator =
            ((Program)this).getSubpDefinitionList().iterator();
           lSubpIterator.hasNext(); ) {
        SubpDefinition lSubpDef = (SubpDefinition)lSubpIterator.next();
        lSubpDef.getSubpSym().buildLabelRefList();
      }
    }else if (this instanceof SubpDefinition) {
      ((SubpDefinition)this).getSubpSym().buildLabelRefList();
    }
    //####
    if (fDbgLevel > 4) {
      System.out.print("\n Test getNextNode() \n");
      HIR lNode = this;
      for (int lCount = 0; lCount < 1000; lCount++) {
        System.out.print("\n" + lNode.toStringShort());
        lNode = ((HIR_Impl)lNode).getNextNode();
        if (lNode == null)
          break;
      }
    }
    //####
    return lTreeStructure;
  }else {
    hirRoot.ioRoot.msgWarning.put(
        "\nfinishHir() should be called for Program or SubpDefinition, not for "
        + toString());
    return false;
  }
} // finishHir

//====== Error handling methods ======//

public void
warinig( int pId, String pMessage ) {
  hirRoot.ioRoot.msgOut.println("\nWarning " + pId + pMessage );
} // warning

public void
slightError( int pId, String pMessage ) {
  hirRoot.ioRoot.msgOut.println("\nSlight error " + pId + pMessage );
} // slightError

public void
severeError( int pId, String pMessage ) {
  hirRoot.ioRoot.msgOut.println("\nSevere error " + pId + pMessage );
} // severeError

public void
fatalError( int pId, String pMessage ) {
  hirRoot.ioRoot.msgOut.println("\nFatal error " + pId + pMessage );
} // fatalError

//====== Factory methods to create HIR node ======//

public Program
program( Sym pProgSym, SymTable pGlobalSymTable,
          IR pInitiationPart, IrList pSubpList) {
  if (hirRoot.programRoot == null) {
    hirRoot.programRoot = (Program)(new ProgramImpl( hirRoot, pProgSym,
                    pGlobalSymTable, pInitiationPart, pSubpList));
  }else {
    if (pProgSym != null)
      ((HIR)hirRoot.programRoot).replaceSource1(hirRoot.hir.symNode(pProgSym));
    if (pInitiationPart != null)
      ((HIR)hirRoot.programRoot).replaceSource2((HIR)pInitiationPart);
    if (pSubpList != null)
      ((HIR)hirRoot.programRoot).replaceSource(3, (HIR)pSubpList);
  }
  return (Program)hirRoot.programRoot;
}

public SubpDefinition
subpDefinition( Subp pSubpSym, SymTable pLocalSymTable ) {
  return (SubpDefinition)
         (new SubpDefinitionImpl(hirRoot, pSubpSym,
              pLocalSymTable, null, null));
} // subpDefinition

public SubpDefinition
subpDefinition( Subp pSubpSym ) {
  return (SubpDefinition)
         (new SubpDefinitionImpl(hirRoot, pSubpSym, null, null,
                                 null));
} // subpDefinition

public SubpDefinition
subpDefinition( Subp pSubpSym, SymTable pLocalSymTable,
                BlockStmt pInitiationPart, BlockStmt pHIRbody) {
 return (SubpDefinition)
        (new SubpDefinitionImpl(hirRoot, pSubpSym,
                               pLocalSymTable,
                               pInitiationPart, null));
} // subpDefinition

public coins.ir.IrList
irList( LinkedList pList )
{
  HirList lHirList;
  HIR     lList;
  lHirList = (HirList)(new HirListImpl(hirRoot, pList));
  lList = (HIR)lHirList;
  return (coins.ir.IrList)lList;
}

public coins.ir.IrList
irList()
{
  HirList lHirList;
  HIR     lList;
  lHirList = (HirList)(new HirListImpl(hirRoot));
  lList = (HIR)lHirList;
  return (coins.ir.IrList)lList;
}

//##70 BEGIN
public HirList
hirList()
{
  return (HirList)(new HirListImpl(hirRoot));
}
//##70 END

/**
 *
 * @param pInfKind
 * @param pInfData
 * @return the InfNode created.
 * ***deprecated InfNode was deleted. Use infStmt method.
 */
//##68 public InfNode
//##68 infNode( String pInfKind, Object pInfData ) {
//##68   return (InfNode)(new InfNodeImpl( hirRoot, pInfKind, pInfData ));
//##68 }

public InfStmt
infStmt( String pInfKind, IrList pInfData ) {
  return (InfStmt)(new InfStmtImpl( hirRoot, pInfKind, pInfData ));
}

//##70 BEGIN
public InfStmt
infStmt( String pInfKind, Object pInfData )
{
  if (pInfData instanceof IrList) {
    return (InfStmt)(new InfStmtImpl( hirRoot, pInfKind, (IrList)pInfData ));
  }else {
    IrList lList = irList();
    lList.add(pInfData);
    return (InfStmt)(new InfStmtImpl( hirRoot, pInfKind, lList));
  }
}
//##70 END

public VarNode
varNode( Var pVar ) {
  return (VarNode)(new VarNodeImpl(hirRoot, pVar));
}

public ElemNode
elemNode( Elem pElem ) {
  return (ElemNode)(new ElemNodeImpl(hirRoot, pElem));
}

public SubpNode
subpNode( Subp pSubp ) {
  return (SubpNode)(new SubpNodeImpl(hirRoot, pSubp));
}

public TypeNode
typeNode( Type pType ) {
  return (TypeNode)(new TypeNodeImpl(hirRoot, pType));
}

public LabelNode
labelNode( Label pLabel ) {
  return (LabelNode)(new LabelNodeImpl(hirRoot, pLabel));
}

public ConstNode
constNode( Const pConst ) {
  return (ConstNode)(new ConstNodeImpl(hirRoot, pConst));
}

public ConstNode
intConstNode( int pIntValue ) {
  return (ConstNode)(new ConstNodeImpl(hirRoot,
          hirRoot.symRoot.sym.intConst(pIntValue,
          hirRoot.symRoot.typeInt)));
}

public ConstNode
intConstNode( long pIntValue ) {
  return (ConstNode)(new ConstNodeImpl(hirRoot,
          hirRoot.symRoot.sym.intConst(pIntValue,
          hirRoot.symRoot.typeInt)));
}

public ConstNode
offsetConstNode( long pIntValue ) {
  ConstNode cn = intConstNode(pIntValue);
  cn.setType(hirRoot.symRoot.typeOffset);
  return cn;
}

public ConstNode
trueNode() {
  return (ConstNode)(new ConstNodeImpl(hirRoot,
          hirRoot.symRoot.sym.boolConst(true)));
}

public ConstNode
falseNode() {
  return (ConstNode)(new ConstNodeImpl(hirRoot,
          hirRoot.symRoot.sym.boolConst(false)));
}

public SymNode
symNode( Sym pSym ) {
  return (SymNode)(new SymNodeImpl(hirRoot, pSym));
}

public NullNode
nullNode( ) {
  return (NullNode)(new NullNodeImpl(hirRoot));
}

public LabelDef
labelDef( Label pLabel ) { return (LabelDef)(new LabelDefImpl(hirRoot, pLabel)); }

public Exp
exp( int pOperator, Exp pExp1 )
{
  return (Exp)(new ExpImpl(hirRoot, pOperator, pExp1));
} // exp 1

public Exp
exp( int pOperator, Exp pExp1, Exp pExp2 )
{
  if (pOperator == HIR.OP_CALL) { //##55
    return functionExp(pExp1, (IrList)pExp2); //##55
  }
  else { //##55
    //##64 BEGIN
    if ((pOperator == HIR.OP_ADD)&&
        (pExp1.getType() instanceof VectorType)) {
      // Subscripted expression represented as pointer expression.
      Exp lExp1 = pExp1;
      while (lExp1.getType()instanceof VectorType) {
        lExp1 = decayExp(lExp1);
      }
      return (Exp)(new ExpImpl(hirRoot, pOperator, lExp1, pExp2));
    }
    //##64 END
    return (Exp)(new ExpImpl(hirRoot, pOperator, pExp1, pExp2));
  }
} // exp 2

public Exp
exp( int pOperator, Exp pExp1, Exp pExp2, Exp pExp3 ) {
  return (Exp)(new ExpImpl(hirRoot, pOperator, pExp1, pExp2, pExp3)); }

public Exp
decayExp( Exp pExp )
{
  Type lType, lElemType, lPtrType;
  Exp  lExp;
  if (pExp == null)
    return null;
  lExp = (Exp)(new ExpImpl(hirRoot, HIR.OP_DECAY, pExp));
  lType = pExp.getType();
  if ((pExp instanceof ConstNode)&&
      (((ConstNode)pExp).getSymNodeSym() instanceof StringConst))
    lElemType = hirRoot.ioRoot.machineParam.getStringElemType();
  else if (lType instanceof VectorType)
    lElemType = ((VectorType)lType).getElemType();
  else {
    hirRoot.ioRoot.msgRecovered.put(1023, "Illegal decay operand "
                                    + pExp.toStringShort());
    lElemType = lType;
  }
  lPtrType = hirRoot.symRoot.sym.pointerType(lElemType);
  lExp.setType(lPtrType);
  return lExp;
} // decayExp

public Exp
undecayExp( Exp pPointerExp, ConstNode pElemCount )
{
  Type lPointerType, lElemType, lVectorType;
  Exp  lUndecay;
  lUndecay = (Exp)(new ExpImpl(hirRoot, HIR.OP_UNDECAY, pPointerExp,
                               pElemCount));
  lPointerType = pPointerExp.getType();
  if (lPointerType instanceof PointerType) {
    lElemType = ((PointerType)lPointerType).getPointedType();
  }else {
    hirRoot.ioRoot.msgRecovered.put(1023, "Illegal undecay operand "
                                    + pPointerExp.toStringShort());
    lElemType = lPointerType;
  }
  lVectorType = hirRoot.symRoot.sym.vectorType(lElemType, pElemCount);
  lUndecay.setType(lVectorType);
  return lUndecay;
} // undecayExp

public Exp
undecayExp( Exp pPointerExp, Exp pElemCount, Exp pLowerBound )
{
  Type lPointerType, lElemType, lVectorType;
  Exp  lUndecay;
  lUndecay = (Exp)(new ExpImpl(hirRoot, HIR.OP_UNDECAY, pPointerExp,
                               pElemCount));
  lPointerType = pPointerExp.getType();
  if (lPointerType instanceof PointerType) {
    lElemType = ((PointerType)lPointerType).getPointedType();
  }else {
    hirRoot.ioRoot.msgRecovered.put(1023, "Illegal undecay operand "
                                    + pPointerExp.toStringShort());
    lElemType = lPointerType;
  }
  lVectorType = hirRoot.symRoot.sym.vectorType(null, lElemType, pElemCount,
                                               pLowerBound);
  lUndecay.setType(lVectorType);
  return lUndecay;
} // undecayExp

////////////////////SF030531[
public Exp
undecayExp( Exp pPointerExp, long pElemCount )
{
  Type lPointerType, lElemType, lVectorType;
  Exp  lUndecay;
  lUndecay = (Exp)(new ExpImpl(hirRoot, HIR.OP_UNDECAY, pPointerExp,
                               intConstNode(pElemCount)));
  lPointerType = pPointerExp.getType();
  if (lPointerType instanceof PointerType) {
    lElemType = ((PointerType)lPointerType).getPointedType();
  }else {
    hirRoot.ioRoot.msgRecovered.put(1023, "Illegal undecay operand "
                                    + pPointerExp.toStringShort());
    lElemType = lPointerType;
  }
  lVectorType = hirRoot.symRoot.sym.vectorType(lElemType, pElemCount);
  lUndecay.setType(lVectorType);
  return lUndecay;
} // undecayExp
////////////////////SF030531]

public Exp
undecayExp( Exp pPointerExp, long pElemCount, long pLowerBound )
{
  Type lPointerType, lElemType, lVectorType;
  Exp  lUndecay;
  lUndecay = (Exp)(new ExpImpl(hirRoot, HIR.OP_UNDECAY, pPointerExp,
                               intConstNode(pElemCount)));
  lPointerType = pPointerExp.getType();
  if (lPointerType instanceof PointerType) {
    lElemType = ((PointerType)lPointerType).getPointedType();
  }else {
    hirRoot.ioRoot.msgRecovered.put(1023, "Illegal undecay operand "
                                    + pPointerExp.toStringShort());
    lElemType = lPointerType;
  }
  lVectorType = hirRoot.symRoot.sym.vectorType(null, lElemType, pElemCount,
                                               pLowerBound);
  lUndecay.setType(lVectorType);
  return lUndecay;
} // undecayExp

public SubscriptedExp
subscriptedExp( Exp pArrayExp, Exp pSubscript )
{
  SubscriptedExp lExp;
  VectorType lArrayType = (VectorType)pArrayExp.getType();
  Type lElemType  = lArrayType.getElemType();
  if (lElemType.getSizeExp() != null) {
    /* //##64
    if (SourceLanguage.subscriptWithIndex)
      lExp = (SubscriptedExp)new SubscriptedExpImpl(
        hirRoot, pArrayExp, pSubscript, (Exp)lElemType.getSizeExp().copyWithOperands() );
    else
    */ //##64
      lExp = (SubscriptedExp)new SubscriptedExpImpl(
        hirRoot, pArrayExp, pSubscript );
  }else {
    if (lElemType.getFlag(Sym.FLAG_UNFIXED_SIZE)) {
      hirRoot.ioRoot.msgError.put("Element type is unfixed size for " +
        pArrayExp.toStringShort() +
        ". Use subscriptedExp(Exp pArrayExp, Exp pSubscrit, Exp pElemSize)");
    }
    lExp = (SubscriptedExp)new SubscriptedExpImpl(
      hirRoot, pArrayExp, pSubscript);
  }
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 7, "subscriptedExp " + ((HIR)lExp.getChild1()).toStringShort() + " " + ((HIR)lExp.getChild2()).toStringShort());
  return lExp;
  ////////////////////////////////////// S.Fukuda 2002.8.16 end
} // subscriptedExp

public Exp
subscriptedExp( Exp pArrayExp, Exp pSubscript, Exp pElemSize )
{
  VectorType lArrayType = (VectorType)pArrayExp.getType();
  Type lElemType  = lArrayType.getElemType();
  if (pElemSize == null) {
    if (lElemType.getFlag(Sym.FLAG_UNFIXED_SIZE)) {
      hirRoot.ioRoot.msgError.put("Element size is not given for unfixed size array "
        + pArrayExp.toStringShort() + "[" + pSubscript.toStringShort() + "]");
      return null;
    }else
      return subscriptedExp(pArrayExp, pSubscript);
  }
  if (lElemType.getSizeExp() != null) {
    long lElemSizeOfType = lElemType.getSizeExp().evaluateAsLong();
    long lElemSizeByParam = pElemSize.evaluateAsLong();
    if ((lElemSizeOfType == lElemSizeByParam)&&
        (lElemSizeOfType > 0)) {
      return subscriptedExp(pArrayExp, pSubscript);
    }
    if ((lElemSizeOfType > 0)&&(lElemSizeByParam > 0)) {
      hirRoot.ioRoot.msgRecovered.put("Element size " + lElemSizeByParam +
        " does not match for " + pArrayExp.toStringShort());
    }
  }
  Exp lElemSize = pElemSize;
  if (lElemSize.getType() != hirRoot.symRoot.typeOffset)
    lElemSize = hirRoot.hir.convExp(hirRoot.symRoot.typeOffset, lElemSize);
  Exp lDisplacement;
  if ((! lArrayType.getLowerBoundExp().isEvaluable())||
      (lArrayType.getLowerBound() != 0)) {
    lDisplacement = hirRoot.hir.exp(HIR.OP_MULT, lElemSize,
      hirRoot.hir.exp(HIR.OP_SUB, pSubscript, lArrayType.getLowerBoundExp()));
  }else {
    lDisplacement = hirRoot.hir.exp(HIR.OP_MULT, lElemSize, pSubscript);
  }
  Exp lArrayAddress;
  if ((pArrayExp.getOperator() == HIR.OP_CONTENTS)&&
      (((HIR)pArrayExp.getChild1()).getType() instanceof PointerType)) {
    lArrayAddress = (Exp)pArrayExp.getChild1();
  }else if (pArrayExp.getType() instanceof PointerType) {
    lArrayAddress = pArrayExp;
  }else {
    lArrayAddress = hirRoot.hir.addrExp(pArrayExp);
  }
  Exp lElemAddress = hirRoot.hir.exp(HIR.OP_ADD, lArrayAddress, lDisplacement);
  Exp lExp = hirRoot.hir.contentsExp(lElemAddress);
  if ((lExp.getType() instanceof VectorType)&&
      (((VectorType)lExp.getType()).getElemType() instanceof VectorType)) {
    lExp = hirRoot.hir.decayExp(lExp);    // Pointer to sub-array.
    lExp = hirRoot.hir.contentsExp(lExp); // Element of sub-array.
  }
  return lExp;
} // subscriptedExp with elemSize

public QualifiedExp
qualifiedExp( Exp pStructUnionExp, ElemNode pElemNode ) {
  return (QualifiedExp)(new QualifiedExpImpl(hirRoot, pStructUnionExp,
                                             pElemNode ));
}

public PointedExp
pointedExp( Exp pStructUnionExp, ElemNode pElemNode ) {
  return (PointedExp)(new PointedExpImpl(hirRoot, pStructUnionExp,
                                             pElemNode ));
}

public Exp
contentsExp( Exp pPointerExp ) {
  return (Exp)(new ExpImpl( hirRoot, HIR.OP_CONTENTS, pPointerExp ));
}

public Exp
convExp( Type pType, Exp pExp )
{
  if (pExp.getType() == pType) //##64
    return pExp;               //##64
  Exp lExp = (Exp)(new ExpImpl(hirRoot, HIR.OP_CONV, pExp));
  lExp.setType(pType);
  return lExp;
}

public Exp
sizeofExp( Type pType )
{
  if (pType.getSizeExp() != null)
    return (Exp)pType.getSizeExp().copyWithOperands();
  else {
    hirRoot.ioRoot.msgRecovered.put(1022,
      "Illegal sizeof-expression for type " + pType.getName());
    return intConstNode(machineParam.evaluateSize(Type.KIND_INT)); //##52
  }
} // sizeofExp

public Exp
sizeofExp( Exp pExp )
{
  if ((pExp.getType() != null)&&(pExp.getType().getSizeExp() != null))
    return (Exp)pExp.getType().getSizeExp().copyWithOperands();
  else {
    hirRoot.ioRoot.msgRecovered.put(1022,
      "Illegal sizeof-expression for node " + pExp.toString());
    return intConstNode(machineParam.evaluateSize(Type.KIND_INT)); //##52
  }
} // sizeofExp

public FunctionExp
functionExp( Exp pFunctionSpec, IrList pActualParamList ) {
  Subp lSubp = null;
  if (hirRoot.symRoot.subpCurrent != null) {
    if (pFunctionSpec instanceof SubpNode)
      lSubp = (Subp)((SubpNode)pFunctionSpec).getSymNodeSym();
    else if (pFunctionSpec.getChild1() instanceof SubpNode)
      lSubp = (Subp)((SubpNode)pFunctionSpec.getChild1()).getSymNodeSym();
    if (lSubp != null)
      hirRoot.symRoot.subpCurrent.addToCallList(lSubp);
  }
  return (FunctionExp)
         (new FunctionExpImpl(hirRoot, pFunctionSpec, pActualParamList));
}

public Exp
addrExp( Exp pExp )
{
   return hirRoot.hir.exp(OP_ADDR, pExp);
}

//##64 BEGIN
public Exp
conditionalExp1( Exp pExp )
{
  if (pExp == null)
    return null;
  int lOperator = pExp.getOperator();
  if ((lOperator >= HIR.OP_CMP_EQ)&&(lOperator <= HIR.OP_CMP_LE)) {
    return pExp;
  }else { // non-comparison expression
    Exp lChild1 = (Exp)pExp.getChild1();
    if (lOperator == HIR.OP_NOT) {
      int lOperator2 = lChild1.getOperator();
      if ((lOperator2 >= HIR.OP_CMP_EQ) &&
          (lOperator2 <= HIR.OP_CMP_LE)) {
        return exp(inversionTable[lOperator2 - HIR.OP_CMP_EQ],
                   (Exp)((Exp)lChild1.getChild1()).copyWithOperands(),
                   (Exp)((Exp)lChild1.getChild2()).copyWithOperands());
      }
    }
    /* //##64
    // Following conversion code causes "LgAnd" error in ToHirBase.
    Exp lCondExp = (Exp)pExp.copyWithOperands();
    if (pExp.getType() != hirRoot.symRoot.typeInt) {
      lCondExp = hirRoot.hir.exp(HIR.OP_CONV, lCondExp);
      lCondExp.setType(hirRoot.symRoot.typeInt);
    }
    Exp lCmpExp = hirRoot.hir.exp(HIR.OP_CMP_NE, lCondExp,
                                  hirRoot.hir.intConstNode(0));
    return lCmpExp;
    */ //##64
   return pExp;
  }
}  // conditionalExp1

public Exp
conditionalExp( Exp pExp )
{
  if (pExp == null)
    return null;
  int lOperator = pExp.getOperator();
  if ((lOperator >= HIR.OP_CMP_EQ)&&(lOperator <= HIR.OP_CMP_LE)) {
    return pExp;
  }else { // non-comparison expression
    Exp lChild1 = (Exp)pExp.getChild1();
    if (lOperator == HIR.OP_NOT) {
      int lOperator2 = lChild1.getOperator();
      if ((lOperator2 >= HIR.OP_CMP_EQ) &&
          (lOperator2 <= HIR.OP_CMP_LE)) {
        return exp(inversionTable[lOperator2 - HIR.OP_CMP_EQ],
                   (Exp)((Exp)lChild1.getChild1()).copyWithOperands(),
                   (Exp)((Exp)lChild1.getChild2()).copyWithOperands());
      }
    }
    Exp lCondExp = (Exp)pExp.copyWithOperands();
    if (pExp.getType() != hirRoot.symRoot.typeInt) {
      lCondExp = hirRoot.hir.exp(HIR.OP_CONV, lCondExp);
      lCondExp.setType(hirRoot.symRoot.typeInt);
    }
    Exp lCmpExp = hirRoot.hir.exp(HIR.OP_CMP_NE, lCondExp,
                                  hirRoot.hir.intConstNode(0));
    return lCmpExp;
  }
}  // conditionalExp
//##64 END

public ExpStmt
callStmt( Exp pSubpSpec, IrList pActualParamList ) {
  return expStmt(functionExp(pSubpSpec, pActualParamList));
}

public AssignStmt
assignStmt( Exp pLeft, Exp pRight ) {
  return (AssignStmt)(new AssignStmtImpl(hirRoot, pLeft, pRight));
}

public IfStmt
ifStmt( Exp pCondition, Stmt pThenPart, Stmt pElsePart ) {
  return (IfStmt)(new IfStmtImpl(hirRoot, pCondition,
  //##64N return (IfStmt)(new IfStmtImpl(hirRoot, conditionalExp1(pCondition), //##64
    pThenPart, pElsePart));
}

public WhileStmt
whileStmt( Exp pCondition, Stmt pLoopBody )
{
  Label lLoopBackLabel, lLoopStepLabel, lLoopEndLabel;
  lLoopBackLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  lLoopStepLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  lLoopEndLabel  = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  return (WhileStmt)(new WhileStmtImpl(hirRoot, lLoopBackLabel,
                     pCondition,
                     //##64N conditionalExp1(pCondition), //##64
                     pLoopBody, lLoopStepLabel, lLoopEndLabel));
}

public WhileStmt
whileStmt( Label pLoopBackLabel, Exp pCondition, Stmt pLoopBody,
           Label pLoopStepLabel, Label pLoopEndLabel )
{
  return (WhileStmt)(new WhileStmtImpl(hirRoot, pLoopBackLabel,
                     pCondition,
                     //##64N conditionalExp1(pCondition), //##64
                     pLoopBody, pLoopStepLabel, pLoopEndLabel));
}

public ForStmt
forStmt( Stmt pInitStmt, Exp pCondition,
         Stmt pLoopBody, Stmt pStepPart )
{
  Label lLoopBackLabel, lLoopStepLabel, lLoopEndLabel;
  lLoopBackLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  lLoopStepLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  lLoopEndLabel  = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  return (ForStmt)(new ForStmtImpl(hirRoot, pInitStmt, lLoopBackLabel,
                    pCondition,
                    //##64N conditionalExp1(pCondition), //##64
                    pLoopBody, lLoopStepLabel, pStepPart, lLoopEndLabel));
}

public ForStmt
forStmt( Stmt pInitStmt, Label pLoopBackLabel, Exp pCondition,
         Stmt pLoopBody, Label pLoopStepLabel, Stmt pStepPart,
         Label pLoopEndLabel )
{
  return (ForStmt)(new ForStmtImpl(hirRoot, pInitStmt, pLoopBackLabel,
                    pCondition,
                    //##64N conditionalExp1(pCondition), //##64
                    pLoopBody, pLoopStepLabel, pStepPart, pLoopEndLabel));
}

public RepeatStmt
repeatStmt(Stmt pLoopBody, Exp pCondition)
{
Label lLoopBackLabel, lLoopStepLabel, lLoopEndLabel;
lLoopBackLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
lLoopStepLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
lLoopEndLabel = hirRoot.symRoot.symTableCurrentSubp.generateLabel();
  return (RepeatStmt) (new RepeatStmtImpl(hirRoot, lLoopBackLabel,
                           pLoopBody, lLoopStepLabel,
                           pCondition,
                           //##64N conditionalExp1(pCondition), //##64
                           lLoopEndLabel));
}

public RepeatStmt
repeatStmt(Label pLoopBackLabel, Stmt pLoopBody, Label pLoopStepLabel,
Exp pCondition, Label pLoopEndLabel)
{
  return (RepeatStmt) (new RepeatStmtImpl(hirRoot, pLoopBackLabel, pLoopBody,
                           pLoopStepLabel, pCondition,
                           //##64N pLoopStepLabel, conditionalExp1(pCondition), //##64
                           pLoopEndLabel));
}

public IndexedLoopStmt
indexedLoopStmt( Var pLoopIndex, Exp pStartValue,
  Exp pEndValue, Exp pStepValue, Stmt pStmtBody )
{
  return (IndexedLoopStmt)(new IndexedLoopStmtImpl(hirRoot, pLoopIndex,
        pStartValue, pEndValue, pStepValue, true, pStmtBody));
}

public IndexedLoopStmt
indexedLoopStmt( Var pLoopIndex, Exp pStartValue,
  Exp pEndValue, Exp pStepValue, boolean pUpward, Stmt pStmtBody )
{
  return (IndexedLoopStmt)(new IndexedLoopStmtImpl(hirRoot, pLoopIndex,
        pStartValue, pEndValue, pStepValue, pUpward, pStmtBody));
}

public LabeledStmt
labeledStmt( Label pLabel, Stmt pStmt ) {
  return (LabeledStmt)(new LabeledStmtImpl(hirRoot, pLabel, pStmt)); }

public BlockStmt
blockStmt( Stmt pStmtSequence ) {
  return (BlockStmt)(new BlockStmtImpl(hirRoot, pStmtSequence)); }

public ReturnStmt
returnStmt( Exp pReturnValue ) {
  return (ReturnStmt)(new ReturnStmtImpl(hirRoot, pReturnValue));
}

public ReturnStmt
returnStmt() {
  return (ReturnStmt)(new ReturnStmtImpl(hirRoot));
}

public JumpStmt
jumpStmt( Label pLabelSym ) {
  return (JumpStmt)(new JumpStmtImpl(hirRoot, pLabelSym));
}

public SwitchStmt
switchStmt( Exp pSelectExp, IrList pJumpList, Label pDefaultLabel,
            Stmt pSwitchBody, Label pEndLabel ) {
  return (SwitchStmt)(new SwitchStmtImpl(hirRoot, pSelectExp, pJumpList,
                      pDefaultLabel, pSwitchBody, pEndLabel ));
}

public ExpStmt
expStmt( Exp pExp ) {
  return (ExpStmt)(new ExpStmtImpl(hirRoot, pExp));
}

public Stmt
nullStmt() {
  return expStmt(nullNode());
}

public PhiExp
phiExp( Var pVar, Label pLabel) {
  return (PhiExp)(new PhiExpImpl(hirRoot, pVar, pLabel));
}

public HirSeq
hirSeq( HIR pChild1 ) {
  return (HirSeq)(new HirSeqImpl(hirRoot, pChild1));
}

public HirSeq
hirSeq( HIR pChild1, HIR pChild2 ) {
  return (HirSeq)(new HirSeqImpl(hirRoot, pChild1, pChild2));
}

public HirSeq
hirSeq( HIR pChild1, HIR pChild2, HIR pChild3 ) {
  return (HirSeq)(new HirSeqImpl(hirRoot, pChild1, pChild2, pChild3));
}

public SetDataStmt
setDataStmt( Exp pVariable, Exp pValueExp )
{
  if ((pVariable instanceof VarNode)&&(pValueExp != null)) {
    Var lVar = (Var)((VarNode)pVariable).getSymNodeSym();
    lVar.setInitialValue(pValueExp);
  }
  return (SetDataStmt)(new SetDataStmtImpl(hirRoot, pVariable, pValueExp)); //SF040525
} // setDataStmt

public Exp
expList( java.util.List pList )
{
  return (Exp)(ExpListExp)(new ExpListExpImpl(hirRoot, pList));
} // expListExp

public Exp
expRepeat( Exp pValue, Exp pCount )
{
  return exp(OP_EXPREPEAT, pValue, pCount);
}

//##70 BEGIN
public AsmStmt
asmStmt( String pInstructions,
         HirList pActualParamList )
{
  ConstNode lInstructions
    = constNode(hirRoot.symRoot.sym.stringConstFromQuotedString(pInstructions));
  return (AsmStmt)(new AsmStmtImpl( hirRoot, lInstructions,
   pActualParamList ));
}


//##70 END

//==== Methods to set children to IR node ====//

public void
setChildren( IR p1, IR p2 ) {
  fChildNode1 = p1;
  if (p1 != null)
    ((HIR_Impl)p1).setParent(this); //##54
  fChildNode2 = p2;
  if (p2 != null)
    ((HIR_Impl)p2).setParent(this); //##54
}

public void
setChildren( IR p1, IR p2, IR p3 ) {
  fChildNode1 = p1;
  if (p1 != null)
    ((HIR_Impl)p1).setParent(this); //##54
  fChildNode2 = p2;
  if (p2 != null)
    ((HIR_Impl)p2).setParent(this); //##54
  setChild(3, p3);
}

public void
setChildren( IR p1, IR p2, IR p3, IR p4 ) {
  setChild1(p1);
  setChild2(p2);
  setChild(3, p3);
  setChild(4, p4);
}

public void
setChildren( IR p1, IR p2, IR p3, IR p4, IR p5 ) {
  setChild1(p1);
  setChild2(p2);
  setChild(3, p3);
  setChild(4, p4);
  setChild(5, p5);
}

//===========================================//

//====== Methods not specified in IR interface
//               but specified in HIR interface     ======//

public Type getType(){ return fType; }

public void setType( Type pType ){ fType = pType; }

public Stmt
getNextStmt() { return null; } // Null except for Stmt node

//##65 BEGIN
/**
 * getNextNode is not recommended to be used in traversing HIR subtree
 * because it has high overhead. It is recommended to use next() of
 * HirIterator. getNextNode is provided for the case where
 * HirIterator can not be used (in such cases as modification of HIR takes
 * place while traversing HIR or getting next node in ancestors).
 * getNextNode returns the node next to this traversing HIR tree
 * in depth first order. If this subtree has no next node, then
 * ancestor node is searched.
 * null nodes are skipped and if null is returned, it means there
 * is no next node.
 * @return the node next to this node.
 */
public HIR
getNextNode()
{
  HIR lNextNode = null;
  if (getChildCount() == 0) {
    if (this instanceof HirList) { // ExpListExp is an instance of HirList
      HirList lHirList = (HirList)this;
      for (int lElemIndex = 0; lElemIndex < lHirList.size(); lElemIndex++) {
        lNextNode = (HIR)lHirList.get(lElemIndex);
        if (lNextNode != null)
          break;
        if (fDbgLevel > 3)
          hirRoot.ioRoot.dbgHir.print(5, " null elem " + lElemIndex);
      }
    }
  }else {
    if (this instanceof BlockStmt) {
      BlockStmt lBlockStmt = (BlockStmt)this;
      lNextNode = ((BlockStmt)this).getFirstStmt();
    }else {
      for (int lChildNumber = 1; lChildNumber <= getChildCount();
           lChildNumber++) {
        lNextNode = (HIR)getChild(lChildNumber);
        if (lNextNode != null)
          break;
        if (fDbgLevel > 3)
          hirRoot.ioRoot.dbgHir.print(5, " null child " + lChildNumber);
      }
    }
  }
  if (lNextNode == null)
    lNextNode = getNextNodeSeeingAncestor(this);
  return lNextNode;
} // getNextNode

protected HIR
  getNextNodeSeeingAncestor( HIR pHir )
{
  if (fDbgLevel > 3)
    hirRoot.ioRoot.dbgHir.print(5, " getNextNodeSeeingAncestor " +
      pHir.toStringShort());
  HIR lNextNode = null;
  if (pHir instanceof Stmt) {
    lNextNode = ((Stmt)pHir).getNextStmt();
  }
  if (lNextNode != null)
    return lNextNode;
  HIR lParent = (HIR)pHir.getParent();
  if (lParent == null) {
    if (fDbgLevel > 3)
      hirRoot.ioRoot.dbgHir.print(5, " null parent " + pHir.toStringShort());
    return null;
  }
  if (lParent instanceof BlockStmt) {
    // getNextStmt() was null. See ancestor.
    lNextNode = getNextNodeSeeingAncestor(lParent);
    return lNextNode;
  }
  int lChildNumber = pHir.getChildNumber();
  if (lChildNumber < lParent.getChildCount()) {
    for (lChildNumber = lChildNumber + 1;
         lChildNumber <= lParent.getChildCount();
         lChildNumber++) {
      lNextNode = (HIR)lParent.getChild(lChildNumber);
      if (lNextNode != null)
        return lNextNode;
      if (fDbgLevel > 3)
        hirRoot.ioRoot.dbgHir.print(5, " null child " + lChildNumber);
    }
  }
  if (lParent instanceof HirList) { // ExpListExp is subclass of HirList
    int lElemNumber = ((HirList)lParent).indexOf(pHir);
    if (lElemNumber < 0)
      return null;
    while (lElemNumber+1 < ((HirList)lParent).size()) {
      lElemNumber++;
      lNextNode = (HIR)((HirList)lParent).get(lElemNumber);
      if (lNextNode != null)
        return lNextNode;
    }
  }
  if (lNextNode == null)
    lNextNode = getNextNodeSeeingAncestor(lParent);
  return lNextNode;
} // getNextNodeSeeingAncestor
//##65 END

/**
 *  getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  setFlag sets the flag of specified number.
 *  @param pFlagNumber flag identification number.
**/
public boolean
getFlag( int pFlagNumber)
{
  if (fHirAnnex == null)
    return false;
  else
    return fHirAnnex.getFlag(pFlagNumber);
} // getFlag

/** setFlag
 *  setFlag sets the flag of specified number.
 *  @param pFlagNumber flag identification number.
 *  @param pYesNo true or false to be set to the flag.
**/
public void
setFlag( int pFlagNumber, boolean pYesNo)
{
  if (fHirAnnex == null)
    fHirAnnex = new HirAnnex(hirRoot);
  fHirAnnex.setFlag(pFlagNumber, pYesNo);
} // setFlag

public boolean
flagsAreAllFalse()
{
  if (fHirAnnex == null)
    return true;
  else
    return fHirAnnex.flagsAreAllFalse();
}

public FlagBox
getFlagBox()
{
  if (fHirAnnex == null)
    return null;
  else
    return fHirAnnex.getFlagBox();
}

//====== Methods for manipulating HIR nodes ======//

/** clone
 * Override Object.clone in HIR.
 * @return cloned HIR object.
 * @exception CloneNotSupportedException
**/
public Object
clone() throws CloneNotSupportedException {
  int lChild;
  HIR_Impl lTree = (HIR_Impl)super.clone();
  if (fDbgLevel > 4) //##58
    hirRoot.ioRoot.dbgHir.print( 6, "clone of " + lTree.toStringShort());
  lTree.fOperator = fOperator;
  //##  lTree.fParentNode = fParentNode;
  lTree.fChildCount = fChildCount;
  if (fChildCount > 0) {
    if (fChildNode1 != null) {
      lTree.setChild1(((HIR)fChildNode1).copyWithOperands());
      if (fDbgLevel > 4) //##58
        hirRoot.ioRoot.dbgHir.print( 6, " child1 " + ((HIR)lTree.getChild1()).toStringShort());
  }else {
      if (fDbgLevel > 4) //##58
        hirRoot.ioRoot.dbgHir.print( 6, " child1  null");
      }
  }
  if (fChildCount >= 2) {
    if (fChildNode2 != null) {
      lTree.setChild2(((HIR)fChildNode2).copyWithOperands());
      if (fDbgLevel > 4) //##58
        hirRoot.ioRoot.dbgHir.print( 6, " child2 " + ((HIR)lTree.getChild2()).toStringShort());
    }else {
      if (fDbgLevel > 4) //##58
        hirRoot.ioRoot.dbgHir.print( 6, " child2  null");
    }
    //### BEGIN
    //##79 if (lTree.fOperator == HIR.OP_CMP_LT) {
    //##79   hirRoot.ioRoot.dbgHir.print( 6, " child2 parent " + lTree.getChild2().getParent()); //##79
    //##79 }
    //### END
  }
  if (fChildCount >= 3) {
    lTree.fAdditionalChild = new IR[fChildCount - 2];
    for (lChild = 3; lChild <= fChildCount; lChild++) {
      if (fAdditionalChild[lChild-3] != null) {
        if (fAdditionalChild[lChild-3] instanceof HIR) {
          lTree.setChild(lChild,
              ((HIR)fAdditionalChild[lChild-3]).copyWithOperands());
         if (fDbgLevel > 4) //##58
           hirRoot.ioRoot.dbgHir.print( 6, " child" + lChild + " " + ((HIR)lTree.getChild(lChild)).toStringShort());
        }else {
          if (fDbgLevel > 4) //##58
            hirRoot.ioRoot.dbgHir.print( 6, " child" + lChild + "  null");
          }
      }else
        lTree.fAdditionalChild[lChild-3] = null;
    }
  }
  if (fHirAnnex != null) {
    lTree.fHirAnnex = (HirAnnex)(fHirAnnex.clone());
    lTree.setWork(getWork()); //##62 ??
  }
  lTree.fType = fType;
  return (HIR)lTree;
} // clone

public IR
getClone()throws CloneNotSupportedException
{
  return (HIR)this.clone();
}

public HIR
hirClone() //##80 throws CloneNotSupportedException
{
//##80  return (HIR)this.clone();
//##80 BEGIN
  HIR lHir;
  try {
    lHir = (HIR)(this.clone());
    ((HIR_Impl)lHir).setParent(null);
    return lHir;
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(HIR_Impl) " + this.toString());
    return null;
  }
//##80 END
}

public HIR
hirNodeClone()
{
  int lChild;
  HIR_Impl lTree;
  if (fDbgLevel > 4) //##58
    hirRoot.ioRoot.dbgHir.print(5, "hirNodeClone of " + toStringShort());
  try {
    lTree = (HIR_Impl)super.clone();
    lTree.fOperator = fOperator;
    //##  lTree.fParentNode = fParentNode;
    lTree.fChildCount = fChildCount;
    if (fHirAnnex != null) {
      lTree.fHirAnnex = (HirAnnex)(fHirAnnex.clone());
      lTree.setWork(getWork()); //##62 ??
    }
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(HIR_Impl) " + this.toString());
    return null;
  }
  lTree.fType = fType;
  return (HIR)lTree;
} // hirNodeClone
//##80 END

public HIR
copyWithOperands() {
  HIR lHir;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 5, " copyWithOperands", this.toStringShort());
  try {
    lHir = (HIR)(this.clone());
    ((HIR_Impl)lHir).setParent(null); //##54
    return lHir;
  }catch (CloneNotSupportedException e) {
    hirRoot.ioRoot.msgRecovered.put(1100,
      "CloneNotSupportedException(HIR_Impl) " + this.toString());
    return null;
  }
} // copyWithOperands

/** copyWithOperandsChangingLabels
 *  Copy this subtree changing labels that are defined in this subtree
 *  to avoid label duplication.
 *  @param pLabelCorrespondence null if this method is called
 *      from outside HIR access methods. If it is null, it is
 *      computed in this method and passed to subclasses
 *      after copyWithOperands (without changing label).
 *  @return the subtree with labels changed.
**/
public HIR
copyWithOperandsChangingLabels( IrList pLabelCorrespondence ) {
  IrList lLabelCorrespondence;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 5, " copyWithOperandsChangingLabels",
      this.toStringShort()); //##70
  HIR lTree = this.copyWithOperands();
  if (lTree instanceof Exp) { //##80
    return lTree; //##80
  } //##80
  if (pLabelCorrespondence == null) {
    lLabelCorrespondence = hirRoot.hirModify.makeLabelCorrespondenceList(lTree);
  }else {
    lLabelCorrespondence = pLabelCorrespondence;
  }
  hirRoot.hirModify.changeLabelsInTree(lTree, lLabelCorrespondence);
  hirRoot.hirModify.adjustLabelInf(lTree, lLabelCorrespondence); //##85
  return lTree;
} // copyWithOperandsChangingLabels

/** isTree
 *  Test if this does not violates tree structure, that is,
 *  detect node adherence in branches and
 *  handshake miss in parent-child relation.
 *  Issues message if some node is encountered twice in
 *  the process of traversing this subtree.
 *  @param pVisitedNodes Give null in usual case;
 *       Set of visited nodes is given when isTree is
 *       invoked recursively by isTree.
 *  @return true if this a tree else return false.
**/
protected boolean
//##60 isTree( java.util.Set pVisitedNodes, int pErrorCounter )
  isTree(Set pVisitedNodes, int pErrorCounter,
         Set pSetOfLabels) //##60
{
  java.util.Iterator lIterator;
  java.util.Set lVisitedNodes;
  java.util.Set lSetOfLabels; //##60
  String lMessage;
  HIR lNode, lParent;
  Stmt lStmt;
  boolean lInOrder; // True if no wrong point.
  int lChildNumber;
  int lErrorCounter = pErrorCounter; //##51
  int lErrorLimit = 100;

  if (pErrorCounter > lErrorLimit) {
    hirRoot.ioRoot.msgNote.put("Too many errors found in isTree()");
    return false;
  }
  lInOrder = true;
  //##60 if (pVisitedNodes == null)
  //##60  lVisitedNodes = new HashSet();
  //##60 else
  lVisitedNodes = pVisitedNodes;
  lSetOfLabels = pSetOfLabels;

  if (lVisitedNodes.contains(this)) {
    hirRoot.ioRoot.msgNote.put("HIR " + this.toStringDetail() +
      " violates tree structure. Duplicated node.");
    lInOrder = false;
    lErrorCounter++;
  }
  else {
    lVisitedNodes.add(this);
  }

  if (getChildCount() == 0) {
    if (this instanceof HirList) {
      for (lIterator = ((HirList)this).iterator();
           lIterator.hasNext() && (lErrorCounter <= lErrorLimit); ) {
        Object lObject = lIterator.next(); //##70
        if (lObject instanceof HIR) { //##70
          HIR lNode1 = (HIR)lObject; //##70
          if (lNode1 != null) {
            if (lNode1.getParent() != this) {
              hirRoot.ioRoot.msgNote.put("HIR " + this.toStringDetail() +
                " violates tree structure. Bad parent link for "
                + lNode1.toStringDetail()
                + " in Subp " + hirRoot.symRoot.subpCurrent); //##85
              lInOrder = false;
              lErrorCounter++;
            }
            if (!((HIR_Impl)lNode1).isTree(lVisitedNodes, lErrorCounter,
              lSetOfLabels)) //##60
              lInOrder = false;
          }
        }// else error.
      }
      //##60 BEGIN
    }
    else if (this instanceof LabelDef) {
      Label lDefinedLabel = ((LabelDef)this).getLabel();
      if (lSetOfLabels.contains(lDefinedLabel)) {
        hirRoot.ioRoot.msgNote.put("HIR " + this.toStringDetail() +
          " has duplicated label " + lDefinedLabel.getName()
          + " in Subp " + hirRoot.symRoot.subpCurrent); //##85
        lInOrder = false;
      }
      else {
        lSetOfLabels.add(lDefinedLabel);
      }
      //##60 END
    }
  }
  else { // Child count > 0
    if (this instanceof BlockStmt) {
      for (lStmt = ((BlockStmt)this).getFirstStmt();
           (lStmt != null) && (lErrorCounter <= lErrorLimit);
           lStmt = lStmt.getNextStmt()) {
        if (lStmt.getParent() != this) {
          hirRoot.ioRoot.msgNote.put("HIR " + this.toStringDetail() +
            " violates tree structure. Bad parent link for "
            + lStmt.toStringDetail());
          lInOrder = false;
          lErrorCounter++;
        }
        if (!((HIR_Impl)lStmt).isTree(lVisitedNodes, lErrorCounter,
          lSetOfLabels)) //##60
          lInOrder = false;
      }
    }
    else {
      for (int i = 1; i <= fChildCount; i++) {
        lNode = (HIR)getChild(i);
        if (lNode != null) {
          lParent = (HIR)lNode.getParent();
          if ((lParent != this) ||
              (lNode.getChildNumber() != i)) {
            lInOrder = false;
            hirRoot.ioRoot.msgNote.put("HIR " + this.toStringDetail() +
              " violates tree structure. Bad parent link for "
              + lNode.toStringDetail()
              + " in Subp " + hirRoot.symRoot.subpCurrent); //##85
            lErrorCounter++;
          }
          if (!((HIR_Impl)lNode).isTree(lVisitedNodes, lErrorCounter,
            lSetOfLabels)) //##60
            lInOrder = false;
        }
      }
    }
  }
  return lInOrder;
} // isTree

public boolean
  isTree()
{
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(4, "\nisTree check for " + toString());
  if (this.getIndex() == 0) //##040125
    this.setIndexNumberToAllNodes(1); //##040125
  Set lVisitedNodes = new java.util.HashSet(); //##60
  Set lSetOfLabels = new java.util.HashSet(); //##60
  return isTree(lVisitedNodes, 0, lSetOfLabels); //##60
} // isTree


/** isSameAs
 *  Compare this tree with pTree and if they have the same tree shape
 *  (same operator and same operands) then return true.
 *  In the comparison, attributes are not compared.
 *  (This method is public but less efficient than the protected method
 *   isSameTree in flow.HirSubpFlowImpl.)
 *  @param pTree HIR tree to be compared.
 *  @return true if this tree has the same shape as pTree,
 *      false otherwise.
**/
public boolean
isSameAs( HIR pTree )
{
  Sym lSym1, lSym2;
  int lChildCount, lChild;

  if (this == pTree)
    return true;
  if (pTree== null)  // One is null, the other is not.
    return false;
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print( 7, " isSameAs " +
         this.getIrName() + " " + pTree.getIrName());
  lSym1 = pTree.getSym();
  if (lSym1 != null) {
    if (lSym1 == this.getSym())
      return true;
    else
      return false;
  }else {   // The trees has no symbol attached.
    lChildCount = pTree.getChildCount();
    if ((this.getOperator() != pTree.getOperator())||
        (this.getChildCount() != lChildCount)||
        (this.getType() != pTree.getType())) {
      return false;  // Both nodes have the same characteristics.
    }else {  // Examine children.
      for (lChild = 1; lChild <= lChildCount; lChild++) {
        if (! ((HIR)this.getChild(lChild)).isSameAs((HIR)pTree.getChild(lChild)))
          return false;
      }
      return true;   // All children of pTree1 are the same
                     // to the corresponding child of pTree2.
    }
  }
} // isSameTree

//##70 BEGIN
public void
checkLinkage( String pHeader )
{
  //##70 BEGIN
  if (fDbgLevel >= 1) {
    HIR lParent = (HIR)getParent();
    if (lParent != null) {
      if ((lParent instanceof BlockStmt)||
          (lParent instanceof HirList)||
          (lParent instanceof IrList)) {
        // No parent-link check.
      }else {
        int lChildNum = this.getChildNumber();
        if (lParent.getChild(lChildNum) != this) {
          hirRoot.ioRoot.msgWarning.put(" Bad parent-link in " + pHeader +
            " for " + toStringShort() + " parent " + lParent.toStringShort()
            + ":" + lChildNum);
          hirRoot.ioRoot.dbgHir.print(4, lParent.toStringWithChildren());
        }
      }
    }
  }
} // printLinkage
//##70 END

//====== Iterators ======//

public Iterator
subpIterator() {
  IrList lSubpList = ((Program)hirRoot.programRoot).getSubpDefinitionList();
  if (lSubpList != null)
    return lSubpList.iterator();
  else
    return hirRoot.emptyIterator();
} // subpIterator

public HirIterator
hirIterator( IR pSubtree )
{
  return (HirIterator)(new HirIteratorImpl(hirRoot,
                                           pSubtree, false));
} // hirIterator

//====== Visitor/Acceptor ======//

public void
accept( HirVisitor pVisitor )
{
  if (fDbgLevel > 0) //##58
    hirRoot.ioRoot.dbgHir.print(3, "accept ", toStringShort());
}

//##64 BEGIN
static final int inversionTable[]= {
//  OP_CMP_EQ     OP_CMP_NE     OP_CMP_GT     OP_CMP_GE     OP_CMP_LT     OP_CMP_LE
HIR.OP_CMP_NE,HIR.OP_CMP_EQ,HIR.OP_CMP_LE,HIR.OP_CMP_LT,HIR.OP_CMP_GE,HIR.OP_CMP_GT
};
//##64 END

} // HIR_Impl class
