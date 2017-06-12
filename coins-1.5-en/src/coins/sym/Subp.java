/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

//##63 import coins.aflow.util.FAList;
import java.util.ArrayList;

//##63 import coins.aflow.BBlock;
import coins.flow.LoopInf; //##63
import coins.flow.SubpFlow; //##63
import coins.ir.IrList;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;

//========================================//
//                            (##14): modified on Jun. 2002.
//                            (##4 ): modified on Mar. 2001.
//                            (##2 ): modified on Nov. 2000.

/** Subp interface
<PRE>
 *  Subp (subprogram) class interface.
 *
 *  Subprogram means such program construct as procedure, function,
 *  method, constructor, destructor, statement function, library
 *  function, etc.
 *  Each subprogram has its local symbol table which is maintained
 *  by pushSymbolTable, popSymbolTable, reopenSymbolTable in
 *  Sym interface.
 *  To define a subprogram, write such statement sequence as follows
 *  (see guidance.txt):
 *     Subp lMain = sym.defineSubp("main".intern(), symRoot.typeInt);
 *     //-- int printf(char* pFormat, ...);
 *     Subp lPrintf = sym.defineSubp("printf".intern(), symRoot.typeInt);
 *     Type lCharPtr = sym.pointerType(symRoot.typeChar);
 *     SymTable lLocalSymTable1 = symRoot.symTableRoot.pushSymTable(lPrintf);
 *     Param lParam1 = sym.defineParam("pFormat".intern(),symRoot.typeStringAny);
 *     lPrintf.addParam(lParam1);
 *     lPrintf.setOptionalParam();
 *     lPrintf.setVisibility(Sym.SYM_EXTERN);
 *     lPrintf.closeSubpHeader();
 *     lLocalSymTable1.popSymTable();
 *     lLocalSymTable2 = symRoot.symTableRoot.pushSymTable(lMain);
 *     lMain.closeSubpHeader();
 *     ....
 *  Enhancement is required to implement object oriented languages.
 *    (method overloading, etc.)
 *  Related methods:
 *    To traverse local symbols declared in this subprogram,
 *      use getSymNestIterator() of SymTable get by getSymTable().
 *    To traverse local variables declared in this subprogram,
 *      use getVarIterator() of SymTable get by getSymTable().
 *    To traverse formal parameters of this subprogram,
 *      use iterator() of IrList get by getParamList().
 *    See methods of SubpDefinition interface such as
 *      getLirBody,
 *      initiationProcedure, addInitiationStmt, etc.
 *    See methods of SubpFlow interface such as
 *      getEntryBBlock, etc.
 *    See methods of DataFlow interface such as
 *      getBBlockList, etc.
</PRE>
**/
public interface
Subp extends Sym
{

/** getNextSubp
 *  Get the subprogram next to this one having the same scope as this.
 *  Default sequence is defined according
 *  to the order of creation by defineUnique, etc. in SymInterface,
 *  but it may also be set by setNextSubp.
 *  @return the next subprogram which has the same DefinedIn as
 *      this subprogram. If there is no next subprogram,
 *      then return null.
**/
Subp
getNextSubp();

/** setNextSubp
 *  Set pNext as the subprogram next to this one and make the
 *  original next subprogram as the next one of pNext.
 *  @param pNext subprogram defined in the same scope as this
 *      subprogram.
**/
void
setNextSubp( Subp pNext );

/** getSubpKind
 *  Get subprogram kind:
 *  (subpOrdinary/subpMember/subpConstructor/subpDestructor).
 *  @return the subprogram kind of this subprogram
**/
int  getSubpKind();

/** setSubpKind
 *  Set subprogram kind
 *  (subpOrdinary/subpMember/subpConstructor/subpDestructor).
 *  @param pSubpKind subprogram kind to be set to this subprogram.
**/
void setSubpKind( int pSubpKind );

/** getVisibility
<PRE>
 *  Get the visibility attribute of the subprogram.
 *      (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *       SYM_COMPILE_UNIT)
</PRE>
 *  @return the visibility attribute of the subprogram
**/
int  getVisibility( );

/** SetVisibility
 *  Sset the visibility attribute of the subprogram.
 *  @param pVisibility visibility attribute to be set by setVisibility.
 *      (SYM_EXTERN, SYM_PUBLIC, SYM_PROTECTED, SYM_PRIVATE,
 *       SYM_COMPILE_UNIT)
**/
void setVisibility( int pVisibility );

/** getSymTable
 *  Get the symbol table local to this subprogram.
 *  (The symbol table given by setSymTable.)
 *  @return the symbol table local to this subprogram.
**/
public SymTable
getSymTable();

/** setSymTable
 *  Set the symbol table local to this subprogram.
 *  If the symbol table is nested, then give the outermost
 *  symbol table local to this subprogram.
 *  @param pSymTable symbol table local to this subprogram.
**/
public void
setSymTable( SymTable pSymTalbe );

/** getReturnValueType
 *  Get the return value type of this subprogram.
 *  @return the return value type.
**/
public Type
getReturnValueType();

/** setReturnValueType
 *  Set the return value type of this subprogram.
 *  @param pType the return value type.
**/
public void
setReturnValueType( Type pType );

/** getParamList
 *  Get the parameter list of this subprogram.
 *  If it has no parameter, then return empty list. //##80
 *  Before calling this method, closeSubpHeader should be called.
 *  The returned value may be incorrect if this is called before
 *  calling closeSubpHeader.
 *  @return the parameter list.
**/
public IrList
getParamList();

/** getParamTypeList
 *  Get the parameter type list of this subprogram.
 *  If it has no parameter, then return null.
 *  If neither closeSubpHeader nore closeSubpPrototype
 *  is called before calling this method, getParamTypeList
 *  may return null.
 *  @return the list of parameter types.
**/
public IrList
getParamTypeList();

/** addParam
<PRE>
 *  Add parameter to the parameter list of this subprogram.
 *  If closeSubpPrototype has been called by processing
 *  prototype declaration, getParamTypeList will return
 *  the list of parameter types. It is recommended to check
 *  the consistency between prototype declaration and
 *  subprogram definition before calling addParam for
 *  each parameter. If null is returned by getParamTypeList,
 *  then prototype declaration is not yet given
 *  (closeSubpPrototype is not yet called.)
 *  See closeSubpHeader.
</PRE>
**/
public void
addParam( Param pParam );

/** addParamType
 *  Add parameter type to a temporal parameter type list.
 *  See closeSubpPrototype.
**/
public void
addParamType( Type pParamType );

/** closeSubpHeader
<PRE>
 *  Finalize the header part of subprogram.
 *  This method will set subprogram type for this subprogram
 *  and set other inevitable information for this subprogram.
 *  Before calling this method, addParam, setOptionalParam,
 *  setVisibility should be called if required and return value type
 *  should be given if required as it is written
 *  in defineSubp of Sym interface.
 *  It is the responsibility of language dependent front end
 *  to check the consistency between prototype declaration and
 *  subprogram definition.
 *  If closeSubpPrototype has been called by processing
 *  prototype declaration, getParamTypeList will return
 *  the list of parameter types. It is recommended to check
 *  the consistency between prototype declaration and
 *  subprogram definition before calling addParam for
 *  each parameter. The closeSubpHeader will adjust
 *  the parameter type list according to the list get by
 *  getParamList and so the consistency check will not be
 *  effective after the call.
</PRE>
**/
void
closeSubpHeader();

/** closeSubpPrototype
<PRE>
 *  Finalize a prototype declaration of subprogram.
 *  This method will set subprogram type for this subprogram.
 *  Before calling this method, addParamType, setOptionalParam,
 *  setVisibility should be called if required and return value type
 *  should also be given if required.
 *  It is the responsibility of language dependent front end
 *  to check the consistency between prototype declaration and
 *  subprogram definition.
 *  Typical sequence of processing prototype declaration is:
 *    Subp lSubp = symRoot.sym.defineSubp("name".intern(), returnType);
 *    lSubp.resetParamTypeList(); // If multiple declaration is allowed.
 *    lSubp.addParamType(paramType1);
 *    lSubp.addParamType(paramType2);
 *    ....
 *    lSubp.setOptionalparam(); // only when optional parameter is given.
 *    lSubp.setVisibility(Sym.SYM_PUBLIC); // only if public.
 *    lSubp.closeSubpPrototype();
</PRE>
**/
 void
 closeSubpPrototype();

/** getOptionalParam
 *  Get the formal parameter generated by
 *  setOptionalParam for this subprogram. If this subprogram has
 *  no optional parameter, return null.
 *  @return the formal parameter generated for "..." in parameter
 *      specification.
**/
//##82 Param getOptionalParam( );

/** setOptionalParam
 *  Generate formal parameter corresponding to "..." in parameter
 *  specification and make getOptionalParam() to be true
 *  for this subprogram..
**/
//##82 Param setOptionalParam( );

//##82 BEGIN
/** hasOptionalParam
 *  @return true if this subprogram has optional parameter
 *     (represented by ... in C in such way as int printf(char*, ...))
 *     otherwise return false.
**/
public boolean
hasOptionalParam();
//##82 END

/**
<PRe>
 * @return true if any number of parameters of any type are permitted
 *    in such case as
 *        extern sub();
 *        sub(a); sub(a, b);
 *    in old C language style.
</pre>
 */
boolean hasNoParamSpec(); //##53

/**
 * Permit any number of parameters of any type for this subprogram.
 */
void setNoParamSpec(); //##53

/** getSubpDefinition
 *  Get the SubpDefinition node defining the IR body of this subprogram.
 *  HIR definition and LIR definition can be get via SubpDefinition.
 *  @return the SubpDefinition node of this subprogram.
**/
public SubpDefinition
getSubpDefinition();

/** setSubpDefinition
 *  Set the SubpDefinition node defining the IR body of this subprogram.
 *  @param pSubpDefinition the SubpDefinition node of this subprogram.
**/
public void
setSubpDefinition( SubpDefinition pSubpDefinition );

/** getStmtWithLabel
 *  Get the HIR Stmt attached with pLabel.
 *  @param pLabel  Label with which Stmt is to be searched.
 *  @return Stmt attached with pLabel.
 *      return null if no statement is found having pLabel.
**/
public Stmt
getStmtWithLabel( Label pLabel );

/** getHirBody
<PRE>
 *  Get the procedural body in HIR of this subprogram.
 *  See SubpDefinition interface and
 *       getLirBody of SubpDefinition.
 *  Return the procedural body of this subprogram
 *      represented in HIR.
 *      If no procedural body is given in this compile unit (i.e.
 *      external subprogram) then return null.
</PRE>
 *  @return the procedural body of this subprogram.
**/
Stmt
getHirBody();

/** setHirBody
<PRE>
 *  Set the procedural body of this subprogram represented in high
 *  level intermediate representation (HIR).
 *  Usually this is invoked when SubpDefinition is created.
 *  pStartLabel, pEndLabel will be generated in SubpDefinition
 *  (See subpDefinition of HIR interface).
</PRE>
 *  @param pHirBody procedural body represented in HIR specifying
 *      operations to be performed when this subprogram is called.
 *  @param pStartLabel Label to be attached at entry point.
 *  @param pEndLabel   Label to be attached at exit point.
**/
void
setHirBody(Stmt pHirBody, Label pStartLabel, Label pEndLabel);

/**
 * Build the list of labels defined in the subprogram so as
 * getLabelDefList() returns proper list, and
 * build the list of LabelNode for every labels to show the
 * label node refering them.
 * This method may be called by finishHir() of HIR interface.
 *
 */
public void
buildLabelRefList();

/** getLabelDefList
 *  Get the list of labels defined in this subprogram.
 *  A label is added to the label list of symRoot.subpCurrent
 *  when defineLabel of Sym is called.
 *  @return the list of labels defined in this subprogram.
**/
//##62 IrList
//##62 getLabelDefList();

//##76 BEGIN
/**
 * Print the label reference list built by
 *  buildLabelRefList().
 */
public void
printLabelRefList();
//##76 END

/** resetLabelLink
 *  Reset label reference list of labels in this subprogram.
 *  getLabelRefList() for labels will return null when resetLabelLink
 *  is called and addToHirList, addToLirList are not yet called.
**/
public void
resetLabelLink();

/** addLabelDefList
 *  Add pLabel to the list of labels defined in this subprogram.
 *  This method is called from defineLabel of Sym and it is
 *  not necessary to call this from other methods.
 *  @param pLabel label defined in this subprogram.
**/
//##62 void
//##62 addToLabelDefList(Label pLabel);

/** removeLabelDef
 *  Remove label from the list of labeles defined in this subprogram.
 *  Before calling this method, LabeledStmt defining this label should be
 *  removed from the subprogram body of this subprogram.
 *  @param pLabel label to be deleted.
**/
//##62 public void
//##62 removeLabelDef( Label pLabel );

/** getStartLabel
 *  setStartLabel
 *  Get/set a label attached to the entry point of this
 *  subprogram. The start label is attached by SubpDefinition
 *  and other classes are not required to worry about setting it.
 *  @param pLabel label to be set as start label.
**/
public Label getStartLabel();
public void  setStartLabel( Label pLabel );

/** getEndLabel
 *  setEndLabel
 *  Get/set a label attached to the exit point of this
 *  subprogram. The end label is attached by SubpDefinition
 *  and other classes are not required to worry about setting it.
 *  @param pLabel label to be set as end label.
**/
public Label getEndLabel();
public void  setEndLabel( Label pLabel );

/** getCallList
 *  Get the list of subprograms called in this subprogram.
 *  @return list of subprograms called in this subprogram.
 *  See addToCallList.
**/
IrList
getCallList();

/** addToCallList
 *  Add pCallee as a subprogram to the call list of this subprogram.
 *  If pCallee is already in the call list, no addition is performed.
 *  @param pCallee a subprogram called in this subprogram.
**/
void
addToCallList( Subp pCallee );

//##81 BEGIN
/**
 * @return true if #pragma safeArrayAll is specified.
 */
public boolean
isSafeArrayAll();
//##81 END

/** getSubpFlow    get flow analysis information link.
 *                  If FlowRoot.isHirAnalysis() then getHirSubpFlow(),
 *                  else if FlowRoot.isLirAnalysis() then getLirSubpFlow().
 *  If flow analysis information is not yet computed,
 *  above method return null.
 *  getHirSubpFlow get flow analysis information link for HIR.
 *  getLirSubpFlow get flow analysis information link for LIR.
 *  setHirSubpFlow set flow analysis information link for HIR.
 *  setLirSubpFlow set flow analysis information link for HIR.
**/
//##60 SubpFlow    getSubpFlow();

/** setSubpFlow
 *  Set flow analysis information link of HIR or LIR.
 *  @param pSubpFlow flow analysis link of HIR or LIR.
 *      If pSubpFlow is HirSubpFlow, set HirSubpFlow, else
 *      if pSubpFlow is LirSubpFlow, set LirSubpFlow.
**/
//##60 void        setSubpFlow( SubpFlow pSubpFlow);

/** getAccessedSyms
 *  Get the set of FlowAnalSym symbols whose value is set or used
 *  in this subprogram.
 *  @return the set of symbols accessed in this subprogram.
**/
//##60 public java.util.Set
//##60 getAccessedSyms();

/** setAccessedSyms
 *  Set the set of FlowAnalSym symbols whose value is set or used
 *  in this subprogram.
 *  @param pAccessedSyms set of symbols accessed in this subprogram.
**/
//##60 public void
//##60 setAccessedSyms( java.util.Set pAccessedSyms );

/** getBBlock
 *  Get basic block whose block number is pBBlock.
 *  @param pBBlockNumber Block number of the basic block to get.
 *  @return basic block with block number pBBlock.
**/
//##60 public BBlock getBBlock( int pBlockNumber );

/** getBBlockTable
 *  @return the table of basic blocks in this subprogram.
 *      The i-th entry of the table contains BBlock with block number i.
**/
//##60 public FAList getBBlockTable();

/** getFirstLoopInf
 *  @return LoopInf of the first outermost loop contained
 *      in this subprogram. If there is no loop, return null.
**/
//##78 public LoopInf
//##78 getFirstLoopInf();

/** setFirstLoopInf
 *  Record LoopInf of the first outermost loop contained
 *  in this subprogram.
 *  @param pLoopInf First outermost LoopInf of this subprogram.
**/
//##78 public void
//##78 setFirstLoopInf( LoopInf pLoopInf );

/** getDefUseList
 *  Get DefUseList (list of definition points and use points) of
 *  this subprogram.
 *  @return the DefUseList of this subprogram if it is computed.
 *      If it is not computed, return null.
**/
//# DefUseList
//# getDefUseList( );

/** Get/set information locally used for flow analysis, optimization,
 *  parallelyzation, etc. of this subprogram.   (##5)
 *  The information will not conflict with others and will not be
 *  destroyed by aliasing.
**/
public Object getFlowInf    ();
public void   setFlowInf    ( Object pInf );
public Object getOptInf     (); //##80
public void   setOptInf     ( Object pInf );
public Object getParallelInf();
public void   setParallelInf( Object pInf );
//##52 public Object getRegAllocInf();
//##52 public void   setRegAllocInf( Object pInf );
//##52 public Object getCodeGenInf ();
//##52 public void   setCodeGenInf ( Object pInf );

/** addToErrorCount
 *  Add pCount to the number of syntax/semantic error counter of this subprogram.
**/
public void
addToErrorCount( int pCount );

/** getErrorCount
 *  Get the number of syntax/semantic errors of this subprogram.
**/
public int
getErrorCount();


/** getLirBody //## DELETED
 *              //## Use getSubpDefinition() and its getLirBody().
 *  Get the procedural body of this subprogram represented in low
 *  level intermediate representation (LIR).
**/
//# LIRseq getLirBody();
//# void   setLIRboby( LIRseq pLirBody );

/** initiationProcedure //## DELETED
 *                       //## See SubpDefinition interface.
 *  Get HIR initiation procedure of this subprogram, where the initiation
 *  procedure is executed at the first invocation of this subprogram
 *  or at the beginning of execution and skipped in later invocations.
 *  @return initiation procedure block. Return null if no initiation
 *      procedure is specified.
**/
//# HIR
//# initiationProcedure();

/** addInitiationStmt   //## DELETED
 *                       //## See SubpDefinition interface.
 *  Add pInitiation as the HIR statement in initiation procedure.
 *  @param pInitiation statement to be put in the ititiation block.
**/
//# void
//# addInitiationStmt( Stmt pInitiation );

/** getEntryBBlock
 *  @return the entry basic block of the graph of basic blocks
 *      corresponding to this subprogram. If there is no basic block
 *      for this subprogram, return null.
**/
//##60 BBlock getEntryBBlock();
//## void   setEntryBBlock( BBlock pBlock ); //## DELETED

/** resetBBlockList //## DELETED
 *  If BBlockList is not yet created, create an empty list.
 *  If BBlockList is already created, clear it and
 *  reset entry basic block.
**/
//## public void
//## resetBBlockList();

/** addBBlock //## DELETED
 *  Add pBBlock at the end of BBlockList.
 *  If getEntryBBlock() is null, set pBBlock as the
 *  entry basic block.
 *  @param pBBlock basic block of this subprogram.
**/
//## public void
//## addBBlock( BBlock pBBlock );

} // Subp interface
