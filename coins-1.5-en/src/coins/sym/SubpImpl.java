/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet; //##71

import coins.SymRoot;
//##63 import coins.aflow.BBlock;
//##78 import coins.flow.LoopInf; //##63
//##63 import coins.aflow.SubpFlow;
//##63 import coins.aflow.util.FAList;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.LabelDef;
import coins.ir.hir.LabelNode; //##62
import coins.ir.hir.HirIterator; //##62
import coins.ir.hir.JumpStmt; //##62
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt; //##62

/** Subp Class
 *  Subp (subprogram) class.
 *
 *  Subprogram means such program construct as procedure, function,
 *  method, constructor, destructor, statement function, library
 *  function, etc.
 *  Each subprogram has its local symbol table which is maintained
 *  by pushSymbolTable, popSymbolTable, reopenSymbolTable in
 *  SymbolTableInterface.
 *  Enhancement is required to implement object oriented languages.
 **/

public class
SubpImpl extends SymImpl implements Subp
{

//===== Field declarations ======//

  /** Link to the next subproguram */
  protected Subp
    fNextSubp = null;

  /** Kind of this subprogram */
  protected int
    fSubpKind = 0;
    //   one of ( ordinary, member, constractor, destractor )

  protected Type
    fReturnValueType;

  /** Visibility attribute
   *   (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
   *    SYM_COMPILE_UNIT)
  **/
  private int
    fVisibility = Sym.SYM_PRIVATE;

  protected SymTable
    fLocalSymTable;

  /** Parameter IrList of this subprogram.
   *  This is effective only after closeSubpHeader is called.
  */
  protected IrList
    fParamList = symRoot.getHirRoot().hir.irList();

  /** fParamListIsSet is changed to true if fParamList is
   *  set (so as not to change it any more).
   *  fParamList is set by closeSubpHeader but
   *  not set by closeSubpPrototype.
   */
  protected boolean
    fParamListIsSet = false;

  /** Parameter type list of this subprogram.
   *  This is temporally used for constructing SubpType and
   *  getParamTypeList() of SubpType does not refer this field. **/
  protected IrList
    fParamListTemp = null;

  /** Parameter type list temporally used in addParamType. */
  protected IrList
    fParamTypeListTemp = null;

  /** Optional parameter generated for this subprogram
   *  if this subprogram has optional parameters.
   *  fOptParam is null if this subprogram has no optional param. */
  protected Param
    fOptParam = null;  // TO BE DELETED

  /** fIsAnyParamPermitted is true if any number of parameters of any
   *  kind are permitted. */
  protected boolean
    fNoParamSpec = false;  //##53

  /* The first local variable of this subproguram */

  /** List of temporal long variables ganerated by compiler */
  protected IrList fTempVarList = null;
  protected int    fTempCount = 0;

  /** List of temporal double variables ganerated by compiler */
  protected IrList fTempDVarList = null;
  protected int    fTempDCount = 0;

  /** The IR SubpDefinition node of this subprogram */
  protected SubpDefinition
    fSubpDefinition = null;

  /** The HIR body statement of this subprogram */
  protected Stmt
    fHirBody = null;

  /**  initiation procedure of this subprogram */
  protected BlockStmt
    fInitiationProcess = null;

  /** Defineded labels in this subprogram */
  protected IrList
    fLabelDefList = null;

  protected Label
    fStartLabel = null,      // Label attached to entry point.
    fEndLabel   = null;      // Label attached to exit  point.

  /** List of subprograms called from this subprogram */
  protected IrList
    fCallList = null;

  /** Flow analysis information link that is currently active
   *  (either fHirSubpFlow or fLirSubpFlow)
  **/
 //##60 protected SubpFlow fSubpFlow = null;

  /** Set of FlowAnalSym symbols accessed in this subprogram */
  protected java.util.Set
    fAccessedSyms;

  //##81 BEGIN
  /**
   * fSafeArrayAll is set to true if #pragama safeArrayAll is given.
   * It means that all subscripted variables
   * and pointer expressions representing array elements
   * never access memory location outside the corresponding
   * array and their subscript values and offset values
   * are all non-negative.
   */
  public boolean
    fSafeArrayAll = false;
  //##81 END
  /** The entry basic block of this subprogaram */
  //##60 protected BBlock
  //##60   fEntryBBlock = null;

  /** Number of abstract int registers assigned in this subprogram.
      (This field is not yet used. Change get/setAssignedIntRegCount()) */
  //##81 protected int
  //##81   fAssignedIntRegCount;

  /** Number of abstract float registers assigned in this subprogram.
      (This field is not yet used. Change get/setAssignedFloatRegCount()) */
  //##81 protected int
  //##81   fAssignedFloatRegCount;

  /** Loop Information of the first outermost loop */
  //##78 protected LoopInf
  //##78   fFirstLoopInf = null;

  /** Information locally used for flow analysis, optimization,
      parallelyzation, etc. of this subprogram.
  **/
  protected Object fFlowInf;     // Information for flow analyer.
  protected Object fOptInf;      // Information for optimizer.
  protected Object fParallelInf; // Information for parallelyzer.
  //##81 protected Object fRegAllocInf; // Information for register allocator.
  //##81 protected Object fCodeGenInf;  // Information for code generator.

  protected int    fErrorCount;  // Number of syntax/semantic errors.

//====== Constructors ======//

public
SubpImpl( SymRoot pSymRoot )
{
  super(pSymRoot);
}

public
SubpImpl( SymRoot pSymRoot, String pName, Type pReturnValueType, Sym pDefinedIn)
{
  super(pSymRoot);
  fName = pName;
  fKind = Sym.KIND_SUBP;
  fReturnValueType = pReturnValueType;
  fDefinedIn = pDefinedIn;
}

//====== Methods to get/set information ======//

    /** getNextSubp
     *  Get the subprogram next to this one having the same scope as this.
     *  Default sequence is defined according
     *  to the order of creation by defineUnique, etc. in SymInterface,
     *  but it may also be set by setNextSubp.
     *  @return the next subprogram which has the same DefinedIn as
     *      this subprogram. If there is no next subprogram,
     *      then return null.
     **/
public Subp
getNextSubp() {
    return fNextSubp;
}

    /** setNextSubp
     *  Set pNext as the subprogram next to this one and make the
     *  original next subprogram as the next one of pNext.
     *  @param pNext subprogram defined in the same scope as this
     *      subprogram.
     **/
public void
setNextSubp( Subp pNext ) {
    fNextSubp = pNext;
}

    /** getSubpKind
     *  Get subprogram kind
     *  (subpOrdinary/subpMember/subpConstructor/subpDestructor).
     *  @return int The subprogram kind of this subprogram.
     **/
public int
getSubpKind( ) {
    return fSubpKind;
}
    /** setSubpKind
     *  Set subprogram kind
     *  (subpOrdinary/subpMember/subpConstructor/subpDestructor).
     *  @param pSubpKind subprogram kind to be set to this subprogram.
     *  setSubpKind set the subprogram kind of this subprogram as pSubpKind.
     *
     **/
public void
setSubpKind( int pSubpKind ) {
    fSubpKind = pSubpKind;
}

/** getVisibility
 *  setVisibility
 *  Get/set the visibility attribute of the subprogram.
 *  "this" may be any subprogram.
 *  @param pVisibility visibility attribute to be set by setVisibility.
 *     (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *      SYM_COMPILE_UNIT). </PRE>
 *  getVisibility @return the visibility attribte.
 *  setVisibility set visibility attribute.
 */
public int
getVisibility() {
  return fVisibility;
}
public void
setVisibility( int pVisibility ) {
  if ((fVisibility == Sym.SYM_PUBLIC)&&
      (pVisibility == Sym.SYM_EXTERN))
    fVisibility = Sym.SYM_PUBLIC;
   else
    fVisibility = pVisibility;
}

public SymTable
getSymTable() { return fLocalSymTable; }

public void
setSymTable( SymTable pSymTable ) { fLocalSymTable = pSymTable; }

public Type
getReturnValueType() { return fReturnValueType; }

public void
setReturnValueType( Type pType ) { fReturnValueType = pType; }

public IrList
getParamList() {
  return fParamList;
}

public IrList
getParamTypeList()
{
  if (getSymType() != null)
    return ((SubpType)getSymType()).getParamTypeList();
  else
    return null;
}

public void
addParam( Param pParam ) {
  if (fParamListTemp == null)
    fParamListTemp = symRoot.getHirRoot().hir.irList();
  if (fParamTypeListTemp == null)
    fParamTypeListTemp = symRoot.getHirRoot().hir.irList();
  fParamListTemp.add(pParam);
  fParamTypeListTemp.add(pParam.getSymType());
  fParamList = fParamListTemp;  // For the case of forgetting closeSubpHeader.
} // addParam

public void
addParamType( Type pParamType ) {
  if (fParamTypeListTemp == null)
    fParamTypeListTemp = symRoot.getHirRoot().hir.irList();
  fParamTypeListTemp.add(pParamType);
} // addParamType

/** getOptionalParam -- TO BE DELETED
 *  Get the formal parameter generated by
 *  setOptionalParam for this subprogram. If this subprogram has
 *  no optional parameter, return null.
 *  @return the formal parameter generated for "..." in parameter
 *      specification.
**/
public Param
getOptionalParam( ) {
  return fOptParam;
}

/** setOptionalParam  -- TO BE DELETED
 *  Generate a formal parameter corresponding to "..." in parameter
 *  specification and make getOptionalParam() to be true
 *  for this subprogram.
**/
public Param
setOptionalParam( ) {
  Param lOptParam = symRoot.symTableCurrent.
          generateParam(symRoot.typeInt, this );
  lOptParam.markAsOptional();
  fOptParam = lOptParam;
  return (Param)lOptParam;
} // setOptionalParam


//##82 BEGIN
public boolean
hasOptionalParam()
{
  return ((SubpType)getSymType()).hasOptionalParam();
} // hasOptionalParam
//##82 END

public void
setNoParamSpec() //##53
{
  fNoParamSpec = true;
}


public boolean
hasNoParamSpec() //##53
{
  return fNoParamSpec;
}

public void
closeSubpHeader()
{
  SubpType lSubpType;
  boolean  lOptParam;
  if (fDbgLevel > 2) //##58
    symRoot.ioRoot.dbgSym.print(3, "closeSubpHeader", toString());
  if (fOptParam == null)
    lOptParam = false;
  else
    lOptParam = true;
  if (! fParamListIsSet) {
    if (fParamListTemp != null)
      fParamList = fParamListTemp;
    else
      fParamList = symRoot.getHirRoot().hir.irList();
    fParamListIsSet = true;
  }
  lSubpType = symRoot.sym.subpType(getReturnValueType(),
      getParamList(), lOptParam, fNoParamSpec, getDefinedIn());
   if (lSubpType != getSymType()) {
    setSymType(lSubpType);
  }
  fParamListTemp     = null;
  fParamTypeListTemp = null;
} // closeSubpHeader

public void
closeSubpPrototype()
{
  SubpType lSubpType;
  boolean  lOptParam;
  if (fDbgLevel > 2) //##58
    symRoot.ioRoot.dbgSym.print(3, "closeSubpPrototype", toString());
  if (fOptParam == null)
    lOptParam = false;
  else
    lOptParam = true;
  if (! fParamListIsSet) {  // Do not set after closeSubpHeader.
    lSubpType = symRoot.sym.subpType(getReturnValueType(),
        fParamTypeListTemp, lOptParam, fNoParamSpec, getDefinedIn()); //##55
    setSymType(lSubpType);
  }
  fParamTypeListTemp = null;
} // closeSubpPrototype

private void  // Unused because this function is
checkParamListAndParamTypeList()  // language dependent.
{
  IrList lParamTypeList = null;
  Type lType, lTempType;
  if (fDbgLevel > 2) //##58
    symRoot.ioRoot.dbgSym.print(4, "checkParamListAndParamTypeList ");
  if (getSymType() != null)
    lParamTypeList = ((SubpType)getSymType()).getParamTypeList();
  if ((fParamTypeListTemp == null)&&
      (lParamTypeList == null))
    return;
  if ((fParamTypeListTemp != null)&&
      (lParamTypeList != null)) {
    Iterator lTempIterator = fParamTypeListTemp.iterator();
    for (Iterator lIterator = lParamTypeList.iterator();
         lIterator.hasNext(); ) {
      lType = (Type)lIterator.next();
      if (lTempIterator.hasNext()) {
        lTempType = (Type)lTempIterator.next();
        if ((lType == lTempType)||
            (lType.getOrigin() == lTempType.getOrigin()))
          continue;
      }
      symRoot.ioRoot.msgWarning.put(3104, "Parameter type of " +
            getName() + " may differ with its prototype declaration. "
            + lType.toString() + " ");
   }
  }
} // checkParamListAndParamTypelist

/** getFirstLocalVar
 *  //## Deleted. See  nextVar of SymIterator.
 *  Get the first local variable of this subprogram.
 *  @return the first local variable of this subprogram.
 *      If this has no local variable, then return null.
 *  The first local variable is set when a variable is defined for
 *  the first time specifying this subprogram by pDefinedIn parameter
 *  in define etc.
**/

public SubpDefinition
getSubpDefinition() { return fSubpDefinition; }

public void
setSubpDefinition( SubpDefinition pSubpDefinition )
{
  fSubpDefinition = pSubpDefinition;
}

/** getHirBody
 *  Get the procedural body of this subprogram represented in high
 *  level intermediate representation (HIR).
 *  @return HIR procedural body of this subprogram.
 *      If no procedural body is given in this compile unit (i.e.
 *      external subprogram) then return null.
**/
public Stmt
getHirBody() {
  return fHirBody;
}
/** setHirBody
 *  Set the procedural body of this subprogram represented in high
 *  level intermediate representation (HIR).
 *  @param pHirBody procedural body represented in HIR specifying
 *      operations to be performed when this subprogram is called.
 *  setHirBody set pHirBody as the HIR procedural body of this subprogram.
**/
public void
setHirBody( Stmt pHirBody, Label pStartLabel, Label pEndLabel ) {
  if (pHirBody != null) {
    if (pHirBody.getOperator() == HIR.OP_LABELED_STMT) {
      fHirBody = pHirBody;
      pHirBody.attachLabel(pStartLabel);
    }else {    // Change to a labeled statement
               // so that flow information can linked with this.
      fHirBody = symRoot.getHirRoot().hir.
                 labeledStmt(pStartLabel, pHirBody);
    }
    if (pHirBody instanceof BlockStmt)
      ((BlockStmt)pHirBody).setSubpBodyFlag(true);
  }
  if (pStartLabel != null)
    fStartLabel = pStartLabel;
  if (pEndLabel != null)
    fEndLabel = pEndLabel;
} // setHirBody

    /** initiationProcedure
     *  Get HIR initiation procedure of this subprogram, where the initiation
     *  procedure is executed at the first invocation of this subprogram
     *  or at the beginning of execution and skipped in later invocations.
     *  @return initiation procedure block. Return null if no initiation
     *      procedure is specified.
     **/
public BlockStmt
initiationProcedure() {
  return fInitiationProcess;
}

    /** addInitiationStmt
     *  Add pInitiation as the HIR statement in initiation procedure.
     *  @param pInitiation statement to be put in the ititiation block.
     **/
public void
addInitiationStmt( Stmt pInitiation ) {
}

/** getLabelDefList
 *  Get the list of labels defined in this subprogram.
 *  A label is added to the label list of symRoot.subpCurrent
 *  when defineLabel of Sym is called.
 *  @return the list of labels defined in this subprogram.
**/
public IrList
getLabelDefList() {
  return fLabelDefList;
}

public void
resetLabelLink()
{
  IrList lLabelDefList = getLabelDefList();
  Label       lLabel;
  LabeledStmt lLabeledStmt;
  coins.ir.hir.LabelDef lLabelDef;
  if (fDbgLevel > 0) //##58
    symRoot.ioRoot.dbgSym.print(2, "resetLabelLink", toString());
  if (lLabelDefList != null) {
    for (Iterator lIterator = lLabelDefList.iterator();
         lIterator.hasNext(); ) {
      lLabel = (Label)lIterator.next();
      if (lLabel != null)
        ((LabelImpl)lLabel).resetHirRefList();
    }
  }
} // resetLabelLink

public void
addToLabelDefList( Label pLabel ) {  // (##4)
  if (pLabel == null)
    return;
  if (fLabelDefList == null)
    fLabelDefList = symRoot.getHirRoot().hir.irList();
  if (! fLabelDefList.contains(pLabel))
    fLabelDefList.add(pLabel);
}

public void
removeLabelDef( Label pLabel )
{
  if (fDbgLevel > 3)
    symRoot.ioRoot.msgWarning.put(" Do not use removeLabelDef. It is deleted.");
  if ((pLabel == null)||(fLabelDefList == null))
    return;
  fLabelDefList.remove(pLabel);
}

/* //##62
public void
addLabeledStmtList( Label pLabel, LabeledStmt pStmt ) { // (##4)
  if (pLabel == null)
    return;
  addToLabelDefList(pLabel);
  pLabel.setHirPosition(pStmt);
} // addLabeledStmtList
*/ //##62

public Stmt
getStmtWithLabel( Label pLabel ){  // (##4)
  if (pLabel != null)
    return pLabel.getHirPosition();
  /* //##62
  Label  lLabel;
  IrList lLabelDefList = getLabelDefList();
  if (lLabelDefList == null)
    return null;
  for (Iterator lIterator = lLabelDefList.iterator();
       lIterator.hasNext(); ) {
    lLabel = ((coins.ir.hir.LabelDef)lIterator.next()).getLabel();
    if (lLabel == pLabel)
      return lLabel.getHirPosition();
  }
*/ //##62
  return null;  // Not found.
} // getStmtWithLabel


public Label
getStartLabel()
{
  return fStartLabel;
}

public void
setStartLabel( Label pLabel )
{
  fStartLabel = pLabel;
}

public Label
getEndLabel()
{
  return fEndLabel;
}

public void
setEndLabel( Label pLabel )
{
  fEndLabel = pLabel;
}

/** getCallList
 *  Get the list of subprograms called in this subprogram.
 *  @return list of subprograms called in this subprogram.
**/
public IrList
getCallList() {
  return fCallList;
}

/** addToCallList
 *  Add pCallee as a subprogram in the call list of this subprogram.
 *  If pCallee is already in the call list, no addition is performed.
 *  @param pCallee a subprogram called in this subprogram.
**/
public void
addToCallList( Subp pCallee ) {
  if (fCallList == null)
    fCallList = symRoot.getHirRoot().hir.irList();
  if (! fCallList.contains(pCallee))
    fCallList.add( pCallee );
}

/* //##60
public SubpFlow
getSubpFlow()
{
  return fSubpFlow;
} // getSubpFlow
*/ //##60

//##60 public void setSubpFlow( SubpFlow pSubpFlow ){
//##60   fSubpFlow = pSubpFlow;
//##60 }

public java.util.Set
getAccessedSyms()
{
  return fAccessedSyms;
}

public void
setAccessedSyms( java.util.Set pAccessedSyms )
{
  fAccessedSyms = pAccessedSyms;
}

    /** getEntryBBlock
     *  @return the entry basic block of the graph of basic blocks
     *      corresponding to this subprogram. If there is no basic
     *      block for this subprogram, return null.
     **/
/* //##60
public BBlock
getEntryBBlock() {
  if (fSubpFlow != null)
    return fSubpFlow.getEntryBBlock();
  else
    return null;
}

public FAList
getBBlockTable()
{
  if (fSubpFlow != null)
    return fSubpFlow.getBBlockTable();
  else
    return null;
}

public BBlock
getBBlock( int pBlockNumber ) {
  if (fSubpFlow != null)
    return fSubpFlow.getBBlock(pBlockNumber);
  else
    return null;
}
*/ //##60

/* //##78
public LoopInf
getFirstLoopInf() {
  return fFirstLoopInf;
}

public void
setFirstLoopInf( LoopInf pLoopInf ) {
  fFirstLoopInf = pLoopInf;
}
*/ //##78

public Object getFlowInf    () { return fFlowInf; }
public void   setFlowInf    ( Object pInf ) { fFlowInf = pInf; }
public Object getOptInf     () { return fOptInf; } //##80
public void   setOptInf     ( Object pInf ) { fOptInf = pInf; }
public Object getParallelInf() { return fParallelInf; }
public void   setParallelInf( Object pInf ) { fParallelInf = pInf; }
//##81 public Object getRegAllocInf() { return fRegAllocInf; }
//##81 public void   setRegAllocInf( Object pInf ) { fRegAllocInf = pInf; }
//##81 public Object getCodeGenInf () { return fCodeGenInf; }
//##81 public void   setCodeGenInf ( Object pInf ) { fFlowInf = pInf; }

public boolean
isSafeArrayAll()
{
  return fSafeArrayAll;
}

public void
addToErrorCount( int pCount )
{
  fErrorCount = fErrorCount + pCount;
}

public int
getErrorCount()
{
  return fErrorCount;
}

//##62 BEGIN
public void
buildLabelRefList()
{
  if (getHirBody() == null) //##76
    return;  //##76
  if (fDbgLevel > 0)
    symRoot.ioRoot.dbgSym.print(2, "buildLabelRefList", this.getName());
  HashSet lReferedLabels = new HashSet(); //##71
  // Reset old label ref list.
  if (fLabelDefList != null) {
    if (fDbgLevel > 0)
      symRoot.ioRoot.dbgSym.print(2, "\n resetLabelRefList ");
    for (Iterator lIterator = fLabelDefList.iterator();
         lIterator.hasNext(); ) {
      Label lLabel = (Label)lIterator.next();
      if (fDbgLevel > 2)
        symRoot.ioRoot.dbgSym.print(4, " " + lLabel.getName());
      if (lLabel.getHirRefList() != null)
        ((LabelImpl)lLabel).resetHirRefList();
    }
  }
  fLabelDefList = symRoot.getHirRoot().hir.irList();
  for (HirIterator lNodeIterator = getHirBody().hirIterator(getHirBody());
       lNodeIterator.hasNext(); ) {
    HIR lHir = lNodeIterator.next();
    if (lHir instanceof LabelDef) {
      Label lDefLabel = ((LabelDef)lHir).getLabel(); //##76
      fLabelDefList.add(lDefLabel); //##76
      if (fDbgLevel > 2)
        symRoot.ioRoot.dbgSym.print(4, " " + lHir.toStringShort()
          + " " + lDefLabel.getName());
    }else if (lHir instanceof LabelNode) {
      Label lLabel2 = ((LabelNode)lHir).getLabel();
      ((LabelImpl)lLabel2).addToHirRefList((LabelNode)lHir);
      lReferedLabels.add(lLabel2); //##71
      if (fDbgLevel > 2)
        symRoot.ioRoot.dbgSym.print(4, " " + lHir.toStringShort()
          + " " + lLabel2.getName());
    }else if ((lHir instanceof JumpStmt)||(lHir instanceof SwitchStmt)) {
      if (fDbgLevel > 2)
        symRoot.ioRoot.dbgSym.print(4, " " + lHir.toStringShort());
    }
 }
 //##71 BEGIN
 for (Iterator lSymIt = lReferedLabels.iterator();
      lSymIt.hasNext(); ) {
   Sym lSym = (Sym)lSymIt.next();
   if (lSym instanceof Label) {
     if (((Label)lSym).getHirPosition() == null) {
       symRoot.ioRoot.msgRecovered.put(" Label " + lSym.getName() +
                                         " undefined.");
     }
   }
 }
 //##71 END
 if (fDbgLevel >= 3)    //##76
   printLabelRefList(); //##76
} // buildLabelRefList
//##62 END

//##76 BEGIN
public void
printLabelRefList()
{
 //-- Print the label reference list.
 System.out.print("\nLabelRefList of " + this.getName());
 ArrayList lUnreferedLabels = new ArrayList();
 System.out.print("\n Explicitly referenced Labeles");
 for (Iterator lIter3 = fLabelDefList.iterator();
      lIter3.hasNext(); ) {
   Label lLabel3 = (Label)lIter3.next();
   IrList lRefList3 = lLabel3.getHirRefList();
   if ((lRefList3 == null)||
       lRefList3.isEmpty()) {
     lUnreferedLabels.add(lLabel3);
   }else {
     System.out.print("\n " + lLabel3.getName());
     for (Iterator lIter4 = lRefList3.iterator();
          lIter4.hasNext(); ) {
       HIR lLabelNode = (HIR)lIter4.next();
       System.out.print(" " + lLabelNode.toStringShort());
     }
   }
 }
 System.out.print("\n Labeles that are not explicitly refered:\n  ");
 for (Iterator lIter5 = lUnreferedLabels.iterator();
      lIter5.hasNext(); ) {
   Label lLabel5 = (Label)lIter5.next();
   System.out.print(" " + lLabel5.getName());
 }
 System.out.print("\n");
} // printLabelRefList
//##76 END

public String
toStringDetail() {
  String lString;
  Sym    lParam;
  lString = super.toStringDetail();
  if (fParamListIsSet) {
    lParam = (Sym)fParamList.getFirst();
    if (lParam != null)
      lString = lString + " param1 " + lParam.toStringShort();
  }
  if (fCallList != null) {
    lString = lString + " callList " + fCallList.toStringShort();
  }
  lString = lString + " " + Sym.VISIBILITY[fVisibility];
  //##81 BEGIN
  if (fSafeArrayAll) {
    lString = lString + " safeArrayAll";
  }
  //##81 END
  return lString;
} // toStringDetail

} // SubpImpl class

