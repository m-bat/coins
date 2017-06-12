/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.ArrayList; //##60
import java.util.HashMap; //##65
import java.util.HashSet;
import java.util.Iterator;
import java.util.List; //##63

import coins.FlowRoot;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt; //##60
import coins.ir.hir.BlockStmt;
import coins.ir.hir.Exp; //##60
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl; //##60
import coins.ir.hir.HirIterator;
import coins.ir.hir.HirList; //##60
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabelNode;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.VarNode;
import coins.sym.ExpId;
import coins.sym.ExpIdImpl;
import coins.sym.FlowAnalSym;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymTable;  //##78
import coins.sym.SymTableImpl;  //##78
import coins.sym.Var;

/** HirSubpFlowImpl class
 *
 *  HIR subprogram flow analysis class.
 **/
public class
  HirSubpFlowImpl
  extends SubpFlowImpl
  implements HirSubpFlow
{

//====== Fields ======

  public static final int
    EXP_ID_HASH_SIZE = 257; // Prime number

  private ExpId[]
    //##60 fExpIdHashTable = new ExpId[EXP_ID_HASH_SIZE];
    fExpIdHashTable; //##60

  private ExpId
    fFirstExpId = null; // First ExpId in the current subprogram.

  private int[]
    fHashCodeOfNode;

//##62 BEGIN
  // fStmtExpSeq, fStmtExpSeqIndexForBBlock, fStmtExpCount are
  // computed in BBlockHirSubtreeIteratorImpl for this SubpDefinition
  // and reused after the computation.
  // fStmtExpSeq is made null in fSubpFlow.clearControlFlow().
  public HIR[]   // Sequence of Stmts and conditional expressions
    fStmtExpSeq = null; // in this SubpDefinition.
  public int[]  // fStmtExpSeq index for each BBlock.
    fStmtExpSeqIndexForBBlock = null; // This is indexed by BBlock number.
  public int     // Number of statements and conditional expressions
    fStmtExpCount = 0; // in this SubpDefinition

//##62 END

//---- Fields inherited from SubpFlowImpl ----

//  public final FlowRoot flowRoot;
//  public final IoRoot   ioRoot;
//  public final SymRoot  symRoot;
//  public final HirRoot  hirRoot;

//  public   Flow  flow;
//  public   LirRoot   lirRoot = null;

//  protected  int   fUsedGlobalSymCount = 0;
//  protected  int    // Numbers used in BitVector.
//  fPointVectorBitCount,   // PointVector  bit count.
//  fPointVectorWordCount,  // PointVector  long word count.
//  fExpVectorBitCount,   // ExpVector  bit count.
//  fExpVectorWordCount,  // ExpVector  long word count.
//  fBBlockVectorBitCount,  // BBlockVector bit count.
//  fBBlockVectorWordCount; // BBlockVector long word count.

//  protected SubpDefinition fSubpDefinition;
//  protected int fNodeCount = 0;
//  protected int fFlowIrLinkSize;
//  protected int fBBlockCount = 0;
//  protected int fUsedSymCount = 0;
//  protected int fSymExpCount = 0;
//  protected int fDefRefCount = 0;
//  protected BBlock  fEntryBBlock;
//  protected ArrayList fBBlockTable = new ArrayList(100);
//  protected ArrayList fSymIndexTable = new ArrayList(200);
//  protected FlowIrLinkCell[]  fFlowIrLink = null;
//  protected IrList    fDfoList = null;
//  protected IrList    fInverseDfoList = null;
//  protected int     fExpIdBase  = 0;

//====== Constructors ======

  public
    HirSubpFlowImpl(FlowRoot pFlowRoot,
                    SubpDefinition pSubpDefinition)
  {
    //##60 super(pFlowRoot);
    super(pFlowRoot, pSubpDefinition); //##60
    if (flowRoot.fSubpFlow != null) {      //##94
      flowRoot.resetAllFlowAnalSymLinks(); //##94
    }                                      //##94
    pFlowRoot.fSubpFlow = (SubpFlow)this; // Should override that of super class.
    ((FlowImpl)pFlowRoot.flow).fSubpFlow = (SubpFlow)this; // Override super class.
    ((FlowImpl)pFlowRoot.flow).fSubpFlowCurrent = this;    //##78
    pFlowRoot.setHirAnalysis();
    //##62 clearControlFlow(); //##60
    //##62 clearDataFlow(); //##60
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2, "HirSubpFlowImpl \n ");
  } // HirSubpFlowImpl

//====== Methods to correlate HIR with index & ExpId ======

  /**
   * Divide HIR into basic blocks assuming that nodes of this SubpDefinition
   * are all numbered.
   * Make fFlowIrLink,
   *   fBBlockOfIR
   *   fUsedSymSet
   *   fSetOfGlobalVariables
   *   fHirNodeArray
   * and set index to FlowAnalSyms appeared in SubpDefinition.
   */
  //##78 public void
  public boolean //##78
    divideHirIntoBasicBlocks()
  {
    HirIterator lHirIterator;
    HIR lNode, // Current node.
      lParent; // Parent of lNode.
    coins.sym.Sym lSym;
    FlowAnalSym lFlowAnalSym;
    int lNodeIndex;
    Label lLabel, lSubpBlockLabel;
    BBlock lBBlock; // BBlock most recently created.
    IrList lLabelDefList;
    Subp lSubp;
    BlockStmt lSubpBody; // Block as the HIR body of subprogram.
    //## LabeledStmt lSubpBodyWithLabel;

    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2, "divideHirIntoBasicBlocks ",
        flowRoot.subpUnderAnalysis.getName() + " fIrIndexMin " +
        fIrIndexMin + " Max " + fIrIndexMax); //##62
    recordBBlock(bblock(), 0); //Avoid IndexOutofRangeException
    //##62 BEGIN
    lSubp = fSubpDefinition.getSubpSym();
    fNodeCount = fIrIndexMax - fIrIndexMin + 1;
    fFlowIrLinkSize = fNodeCount + 1; //##62
    fFlowIrLink = new IR[fFlowIrLinkSize];
    lBBlock = null; // Record a basic block created.
    boolean lImmediatelyAfterJumpReturn = false; //##63
    //##62 END

    //-- Assign index number to symbols actually used in current
    //   subprogram setting index to each node.
    //   Make label reference list for labels that are explicitly
    //   refered as jump target.
    for (lHirIterator = hirRoot.hir.hirIterator(fSubpDefinition.getHirBody());
         lHirIterator.hasNext(); ) {
      lNode = lHirIterator.next();
      if (lNode != null) {
        if (fDbgLevel > 3)
          ioRoot.dbgFlow.print(6, " lNode", lNode.toStringShort());
        lSym = lNode.getSym();
        if (lSym != null) {
          if (lSym instanceof FlowAnalSym) {
            lFlowAnalSym = (FlowAnalSym)lSym;
            if (lFlowAnalSym.getIndex() == 0) {
              recordSym(lFlowAnalSym); //##62
            }else {
              if (fDbgLevel > 0)
                flow.dbg(6, " " + lFlowAnalSym.getName()+
                            ":" + lFlowAnalSym.getIndex());
            }
          }
        }

        //##63 BEG
        lNodeIndex = lNode.getIndex();
        //##78 BEGIN
        if (lNodeIndex < fIrIndexMin) {
          ioRoot.msgRecovered.put(5010, "\nNode index " + lNodeIndex
            + " should be greater or equal to " + fIrIndexMin
            + " in " + fSubpDefinition.getSubpSym().getName()
            + "\n  Skip the flow analysis and the optimization.");
          return false;
        }
        //##78 END
        fFlowIrLink[lNodeIndex - fIrIndexMin] = lNode; //##60
        fBBlockOfIR[lNodeIndex - fIrIndexMin] = lBBlock; //##60
        if (lImmediatelyAfterJumpReturn &&
            (lNode.getOperator() != HIR.OP_LABELED_STMT)) {
          continue; // Control does not come here.
        }
        lImmediatelyAfterJumpReturn = false;
        //##63 END
        switch (lNode.getOperator()) {
          case HIR.OP_LABELED_STMT:
            lBBlock = bblock((LabeledStmt)lNode); // Create a basic block.
            fBBlockOfIR[lNodeIndex - fIrIndexMin] = lBBlock; // Store again. //##65
            //## lSubp.addBBlock(lBBlock); // DELETE
            lLabelDefList = ((LabeledStmt)lNode).getLabelDefList();
            if (lLabelDefList != null) {
              for (Iterator lIterator = lLabelDefList.iterator();
                   lIterator.hasNext(); ) { // Set link between HIR & Label.
                lLabel = ((coins.ir.hir.LabelDef)(lIterator.next())).getLabel();
                //##60 lLabel.setBBlock(lBBlock);
                flowRoot.fSubpFlow.setBBlock(lLabel, lBBlock); //##60
                fLabelDefCount = fLabelDefCount + 1; //##100
              }
            }
            lImmediatelyAfterJumpReturn = false;
            break;
            // begin
          case HIR.OP_CONTENTS:
            lParent = (HIR)lNode.getParent();
            if (lParent.getOperator() == HIR.OP_ASSIGN)
              lBBlock.setFlag(BBlock.HAS_PTR_ASSIGN, true);
          case HIR.OP_ARROW:
          case HIR.OP_ADDR:
            lBBlock.setFlag(BBlock.USE_PTR, true);
            break;
          case HIR.OP_QUAL:
            lBBlock.setFlag(BBlock.HAS_STRUCT_UNION, true);
            break;
          case HIR.OP_CALL:
            lBBlock.setFlag(BBlock.HAS_CALL, true);
            //##62 hasCallInSubp = true; //##62
            //##63 BEGIN
            fCallCount++; //##62
            fSubtreesContainingCall.add(lNode);
            for (HIR lHir = (HIR)lNode.getParent(); lHir != null;
                 lHir = (HIR)lHir.getParent()) {
              fSubtreesContainingCall.add(lNode);
            }
            //##63 END
            break;
          //##62 BEGIN
          case HIR.OP_ASSIGN:
            fAssignCount++;
            break;
          case HIR.OP_JUMP:
            lLabel = ((JumpStmt)lNode).getLabel();
            //##62 lLabel.addToHirRefList((LabelNode)((JumpStmt)lNode).getChild1());
            lImmediatelyAfterJumpReturn = true; //##63
            ((BBlockImpl)lBBlock).fControlTransfer = lNode; //##73
            break;
          case HIR.OP_RETURN: //##63
            lImmediatelyAfterJumpReturn = true; //##63
            ((BBlockImpl)lBBlock).fControlTransfer = lNode; //##73
            break;
          case HIR.OP_SWITCH:
            SwitchStmt lSwitchStmt = (SwitchStmt)lNode;
            int lCaseCount = lSwitchStmt.getCaseCount();
            for (int i = 0; i < lCaseCount; i++) {
              lLabel = lSwitchStmt.getCaseLabel(i);
              //##62 if (lLabel != null)
                //##62 lLabel.addToHirRefList(lSwitchStmt.getCaseLabelNode(i));
            }
            //##62 lSwitchStmt.getDefaultLabel().addToHirRefList((LabelNode)((SwitchStmt)
            //##62   lNode).getDefaultLabelNode());
          //##62 END
          default:
            break;
            // end
        } // End of switch
        //##63 lNodeIndex = lNode.getIndex();
        //##60 fFlowIrLink[lNodeIndex - fIndexMin] =
        //##60  (FlowIrLinkCell)(new FlowIrLinkCellImpl(lNode, lBBlock, 0));
        //##63 fFlowIrLink[lNodeIndex - fIrIndexMin] = lNode; //##60
        //##63 fBBlockOfIR[lNodeIndex - fIrIndexMin] = lBBlock; //##60
      } // End of if(lNode != null)
    }
    //##62 BEGIN
    fUsedSymCount = fSymExpCount;
    if (fDbgLevel > 0)
      //##100 ioRoot.dbgFlow.print(2, lSubp.getName() +
   	  ioRoot.dbgFlow.print(1, lSubp.getName() +  //##100
        " Number of", "Symbols:" +
    fUsedSymCount + " Nodes:" + fNodeCount + " "
    + " BBlocks:" + fBBlockCount            //##100
    + " definedLabels:" + fLabelDefCount);  //##100
    fExpVectorBitCount = fUsedSymCount + 2; //##60
    fExpVectorWordCount = (fExpVectorBitCount + 63) / 64; //##60

    int lNodeCount = fIrIndexMax - fIrIndexMin;
    //##101 if ((lNodeCount > fNodeCountLim1)||
    //##101    	(fSymExpCount > fSymCountLim1))
    if ((lNodeCount > fNodeCountLim1*fComplexityAllowanceNumber)|| //##101
    	(fSymExpCount > fSymCountLim1*fComplexityAllowanceNumber)) //##101
      fComplexity = 2;
    //##101 if ((fBBlockCount > fBBlockCountLim1)||    //##100
    //##101     (fLabelDefCount > fLabelDefCountLim1)) //##100
    if ((fBBlockCount > fBBlockCountLim1*fComplexityAllowanceNumber)||    //##101
        (fLabelDefCount > fLabelDefCountLim1*fComplexityAllowanceNumber)) //##101
      fComplexity = 2;                         //##100
    //##101 if ((lNodeCount > fNodeCountLim2)||
    //##101     (fSymExpCount > fSymCountLim2))
    if ((lNodeCount > fNodeCountLim2*fComplexityAllowanceNumber)|| //##101
    	(fSymExpCount > fSymCountLim2*fComplexityAllowanceNumber)) //##101
      fComplexity = 3;
    //##100 BEGIN
    //##101 if ((fBBlockCount > fBBlockCountLim2)|| 
    //##101    	(fLabelDefCount > fLabelDefCountLim2))
    if ((fBBlockCount > fBBlockCountLim2*fComplexityAllowanceNumber)||   //##101
    	(fLabelDefCount > fLabelDefCountLim2*fComplexityAllowanceNumber))//##101
      fComplexity = 3;
    //##101 if ((lNodeCount > fNodeCountLim3)||
    //##101    	(fSymExpCount > fSymCountLim3))
    if ((lNodeCount > fNodeCountLim3*fComplexityAllowanceNumber)|| //##101
    	(fSymExpCount > fSymCountLim3*fComplexityAllowanceNumber)) //##101
      fComplexity = 4;
    //##101 if ((fBBlockCount > fBBlockCountLim3)|| 
    //##101     (fLabelDefCount > fLabelDefCountLim3))
    if ((fBBlockCount > fBBlockCountLim3*fComplexityAllowanceNumber)||   //##101
        (fLabelDefCount > fLabelDefCountLim3*fComplexityAllowanceNumber))//##101
      fComplexity = 4;
    //##101 if ((lNodeCount > fNodeCountLim4)||
    //##101     (fSymExpCount > fSymCountLim4))
    if ((lNodeCount > fNodeCountLim4*fComplexityAllowanceNumber)|| //##101
    	(fSymExpCount > fSymCountLim4*fComplexityAllowanceNumber)) //##101
      fComplexity = 5;
    //##101 if ((fBBlockCount > fBBlockCountLim4)|| 
    //##101     (fLabelDefCount > fLabelDefCountLim4))
    if ((fBBlockCount > fBBlockCountLim4*fComplexityAllowanceNumber)||   //##101
        (fLabelDefCount > fLabelDefCountLim4*fComplexityAllowanceNumber))//##101
      fComplexity = 5;
    //##100 END
    //##62 END
    if (fDbgLevel > 0) {
      //### fComplexity = 3; //###
      flowRoot.ioRoot.dbgFlow.print(1, "\n Node count " +
        lNodeCount + " BBlock count " + getNumberOfBBlocks()+
        " UsedSymCount " + getUsedSymCount() + " SymExpCount " + getSymExpCount()
          + "\n  AssignCount " + getAssignCount()
          + " CallCount " + getCallCount()); //##62
      ioRoot.dbgFlow.print(1, "\nComplexity level of ",
        getSubpSym().getName() + " is " + fComplexity);
      if (fComplexity > 1)
        ioRoot.dbgFlow.print(1, "\n  Simplify alias analysis.");
      if (fComplexity > 2)
        ioRoot.dbgFlow.print(1, "\n  Simplify data flow analysis.");
    }
    setComputedFlag(CF_BBLOCK); //##60
    return true; //##78
  } // divideHirIntoBasicBlocks

/* //##78
  public void
    attachLoopInf()
  {
    HIR lNode;
    LoopInf lLoopInf;
    Subp lSubp;

    lSubp = fSubpDefinition.getSubpSym();
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2, "attachLoopInf", lSubp.getName());
    //##78 lSubp.setFirstLoopInf(null); // Reset LoopInf linkage.
    lNode = fSubpDefinition.getHirBody();
    if (lNode != null) {
      attachLoopInfIfRequired(lNode, null);
    }
    //-- Debug print --
    //##60 lLoopInf = lSubp.getFirstLoopInf();
    //##60 if (lLoopInf != null)
    //##60   lLoopInf.print(2);
  } // attachLoopInf

  private void
    attachLoopInfIfRequired(HIR pNode, LoopInf pParentLoopInf)
  {
    LoopInf lLoopInf, lChildLoopInf1, lChildLoopInf2,
      lNextLoopInf1 = null, lNextLoopInf2 = null;
    HIR lNode;
    Stmt lStmt, lStmt1, lLoopBackPoint;
    LoopStmt lLoopStmt;
    Label lLabel;
    BBlock lBBlock, lEntryBBlock;
    Subp lSubpCurrent;
    if ((pNode != null) && (pNode instanceof Stmt)) {
      lStmt = (Stmt)pNode;
      if (fDbgLevel > 3)
        ioRoot.dbgFlow.print(5, " " + lStmt.toStringShort());
      switch (lStmt.getOperator()) {
        case HIR.OP_LABELED_STMT:
          lLabel = lStmt.getLabel();
          if (lLabel != null) {
            //##60 lBBlock = lLabel.getBBlock0();
            lBBlock = flowRoot.fSubpFlow.getBBlock0(lLabel); //##60
            if (pParentLoopInf != null) {
              pParentLoopInf.addBBlock(lBBlock);
              if (lBBlock.getFlag(BBlock.HAS_CALL))
                pParentLoopInf.propagateFlag(LoopInf.HAS_CALL);
              if (lBBlock.getFlag(BBlock.HAS_PTR_ASSIGN))
                pParentLoopInf.propagateFlag(LoopInf.HAS_PTR_ASSIGN);
              if (lBBlock.getFlag(BBlock.USE_PTR))
                pParentLoopInf.propagateFlag(LoopInf.USE_PTR);
              if (lBBlock.getFlag(BBlock.HAS_STRUCT_UNION))
                pParentLoopInf.propagateFlag(LoopInf.HAS_STRUCT_UNION);
            }
          }
          attachLoopInfIfRequired(((LabeledStmt)lStmt).getStmt(),
            pParentLoopInf);
          break;
        case HIR.OP_IF:
          attachLoopInfIfRequired(((IfStmt)lStmt).getThenPart(),
            pParentLoopInf);
          attachLoopInfIfRequired(((IfStmt)lStmt).getElsePart(),
            pParentLoopInf);
          break;
        case HIR.OP_WHILE:
        case HIR.OP_FOR:
        case HIR.OP_UNTIL:
        case HIR.OP_INDEXED_LOOP:
          if (fDbgLevel > 2)
            ioRoot.dbgFlow.print(3, "\n  Loop " + lStmt.toStringShort());
          lLoopStmt = (LoopStmt)lStmt;
          lLoopInf = (LoopInf)(new LoopInfImpl(flowRoot, lLoopStmt));
          lSubpCurrent = fSubpDefinition.getSubpSym();
          break;
        case HIR.OP_SWITCH:
          attachLoopInfIfRequired(((SwitchStmt)lStmt).getBodyStmt(),
            pParentLoopInf);
          break;
        case HIR.OP_BLOCK:
          lStmt1 = ((BlockStmt)lStmt).getFirstStmt();
          while (lStmt1 != null) {
            attachLoopInfIfRequired(lStmt1, pParentLoopInf);
            lStmt1 = lStmt1.getNextStmt();
          }
          break;
        case HIR.OP_EXP_STMT:
//  case HIR.OP_NOCHANGE_STMT:

        default:
          break;
      }
    }
  } // attachLoopInfIfRequired
*/ //##78

  public void
    divideLirIntoBasicBlocks()
  {

  } // divideLirIntoBasicBlock

//##60  public void linkLirWithDataFlowForSubp() {}

  /**
   * Allocate ExpId for each data handling node.
   * ExpId allocation is required for SetRefRepr computation and
   * data flow analysis.
   * The method allocateExpIdForSubp computes also
   *   fExpIdTable
   *   fExpIdList
   *   fSymIndexTable
   *   fDefRefIndex
   *   fDefIndex
   *   fDefRefPoint
   *   fDefPoint
   *   fDefinedSyms
   *   fFlowAnalSymTable
   */
  public void
    allocateExpIdForSubp()
  {
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2, "allocateExpIdForSubp",
        //## flowRoot.subpUnderAnalysis.getName() + " UsedSymCount " + fUsedSymCount);
        flowRoot.subpUnderAnalysis.getName() + " UsedSymCount " + fUsedSymCount
        + " IrIndex " + fIrIndexMax + "-" + fIrIndexMin
        + " BBlockCount " + fBBlockCount);
    if (isComputed(DF_EXPID)) {
      if (fDbgLevel > 0)
        ioRoot.dbgFlow.print(2, " use computed ExpIds.");
      return;
    }
    symRoot.symTableFlow = new SymTableImpl(symRoot); //##78
    ((SymTableImpl)symRoot.symTableFlow).fTableName = "FlowAnal"; //##78
    setUnderComputation(DF_EXPID); //##65
    fExpIdHashTable = new ExpId[EXP_ID_HASH_SIZE]; //##60
    int lNodeCount = fIrIndexMax - fIrIndexMin + 1; //##62
    //##65 fDefRefIndex = new int[lNodeCount + 2]; //##62
    //##65 fDefIndex = new int[lNodeCount + 2]; //##62
    //##65 fDefRefPoint = new IR[lNodeCount + 2];
    // Sizes of tables indexed by node index may exceed original
    // node count because there are some nodes generated by copying
    // during ExpId computation, etc. //##65
    fDefRefIndex = new int[lNodeCount * 2 + 2]; //##65
    fDefIndex = new int[lNodeCount * 2 + 2]; //##65
    fDefRefPoint = new IR[lNodeCount * 2 + 2]; //##65
    fDefRefPoint[0] = null; //fDefRefPoint[0]is not used. //##62
    fDefPoint = new IR[lNodeCount / 3]; //##62
    fDefPoint[0] = null; //fDefPoint[0]is not used. //##62
    //##62 allocateExpIdTable(); //##60
    //##65 fExpIdTable = new ExpId[fIrIndexMax - fIrIndexMin + 3]; //##62
    fExpIdTable = new ExpId[lNodeCount * 2 + 3]; //##65
    // Nodes of r-value Exp are copied in ExpId allocation
    // (see fMaxIndexOfCopiedNode).
    fSubtreesCopied = new ArrayList(); //##68
    // fSubtreesCopied contains r-value Exps copied in ExpId allocation.
    fFirstExpId = null; //##62
    fDefRefCount = 0;
    //##62 fSymExpCount = fUsedSymCount; // Inherit fUsedSymCount.
    //##65 fHashCodeOfNode = new int[lNodeCount + 3]; //##62
    fHashCodeOfNode = new int[lNodeCount * 2 + 3]; //##65
    fMaxIndexOfCopiedNode = fIrIndexMax; //##65

    allocateExpIdToNode(fSubpDefinition.getHirBody());
    //##62 BEGIN
    fSymIndexTable = new FlowAnalSym[fSymExpCount + 3];
    for (Iterator lSymIterator = fUsedSymSet.iterator();
         lSymIterator.hasNext(); ) {
      FlowAnalSym lSym = (FlowAnalSym)lSymIterator.next();
      if (lSym != null) {
        int lSymIndex = lSym.getIndex();
        fSymIndexTable[lSymIndex] = lSym;
      }
    }
    //##62 END
    //##60 BEGIN
    setComputedFlag(DF_EXPID);
    if (fDbgLevel >= 3) {
      ioRoot.dbgFlow.print(3, "\nHIR after ExpId allocation",
        fSubpDefinition.getSubpSym().getName());
      fSubpDefinition.print(1);
    }
    if (fDbgLevel > 0) {
      ioRoot.dbgFlow.print(2, " SymExpCount", " " + getSymExpCount());
      if (fDbgLevel >=2) {
        if (fDbgLevel >=4)
          printExpIdAndIrCorrespondence();
        System.out.print("\nMaximalCompoundVars " + getMaximalCompoundVars()); //##65
      }
    }
    //##60 END
    //##65 BEGIN
    int lSymCount = fDefinedSyms.size() + fUsedSymSet.size();
    if (fSymExpCount > lSymCount)
      lSymCount = fSymExpCount;
    fFlowAnalSymTable = new FlowAnalSym[lSymCount + 2];
    for (Iterator lSymIt = fDefinedSyms.iterator();
         lSymIt.hasNext(); ) {
      FlowAnalSym lSym = (FlowAnalSym)lSymIt.next();
      fFlowAnalSymTable[lSym.getIndex()] = lSym;
    }
    if (fDbgLevel >= 2) {
      ioRoot.dbgFlow.print(2, "\nFlowAnalSymTable length=" +
        fFlowAnalSymTable.length + " ");
      for (int lIndex = 1; lIndex <= lSymCount; lIndex++) {
        if (fFlowAnalSymTable[lIndex] != null)
          ioRoot.dbgFlow.print(2, fFlowAnalSymTable[lIndex].getName() + " ");
      }
    }
    //##65 END
  } // allocateExpIdForSubp

  //##60 public void
  /**
   * Allocate ExpId to pSubtree and its descendants.
   * If pSubtree is allocated an ExpId, then it is returned.
   * If pSubtree is not allocated ExpId, then (representative)
   * ExpId of its descendants is returned.
   * @param pSubtree to which ExpId is to be assigned.
   * @return ExpId of pSubtree or its descendant.
   */

  public ExpInf //##60
    allocateExpIdToNode(HIR pSubtree)
  {
    int lChild, lChildCount, lIndex;
    Stmt lStmt;
    ExpId lExpId = null; //##60
    if (pSubtree == null)
      return null;
    int lOpCode = pSubtree.getOperator();

    // ioRoot.dbgFlow.print(7, "allocateExpIdToNode", pSubtree.toStringShort());
    ExpInf lExpInfOfThis = new ExpInf(pSubtree);
    // If pSubtree is a VarNode representing a temporal variable
    // then get the expression represented by the temporal variable
    // and set information of its ExpInf to lExpInfOfThis.
    if (lOpCode == HIR.OP_VAR) {
      Exp lExp = getExpOfTemp((Var)pSubtree.getSym());
      if (lExp != null) {
        ExpId lExpIdOfTemp = this.getExpId(lExp);
        if (lExpIdOfTemp != null) {
          ExpInf lExpInfTemp = lExpIdOfTemp.getExpInf();
          lExpInfTemp.combineTo(lExpInfOfThis);
        }
      }
    }
    ExpInf lExpInf = null;
    //-- Allocate ExpId to children.
    if (lOpCode == HIR.OP_BLOCK) {
      for (lStmt = ((BlockStmt)pSubtree).getFirstStmt(); lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        lExpInf = allocateExpIdToNode(lStmt);
        if (lExpInf != null)
          lExpInf.combineTo(lExpInfOfThis);
      }
    }
    else {
      lChildCount = pSubtree.getChildCount();
      if (lChildCount == 0) {
        if (pSubtree instanceof HirList) {
          for (Iterator lIterator = ((HirList)pSubtree).iterator();
               lIterator.hasNext(); ) {
            HIR lListElem = (HIR)lIterator.next();
            lExpInf = allocateExpIdToNode(lListElem);
            if (lExpInf != null)
              lExpInf.combineTo(lExpInfOfThis);
          }
        }
      }
      else {
        for (lChild = 1; lChild <= lChildCount; lChild++) {
          HIR lChildNode = (HIR)pSubtree.getChild(lChild);
          lExpInf = allocateExpIdToNode(lChildNode);
          if (lExpInf != null)
            lExpInf.combineTo(lExpInfOfThis);
            // ExpId lChildExpId = this.getExpId(lChildNode);
            // if (lChildExpId != null) {
            //   lExpInfOfThis.getOperandSet().add(lChildExpId);
            // }
        }
      }
    }
    //-- Allocate ExpId to pSubtree and set DerRefPosition number.
    switch (lOpCode) {
      //-- Nonterminals computing some value:
      case HIR.OP_CALL:
        lExpInfOfThis.setCallFlag(); //##60
      case HIR.OP_SUBS:
      case HIR.OP_INDEX:
      case HIR.OP_QUAL:
      case HIR.OP_ARROW:
      case HIR.OP_ADD:
//  case HIR.OP_ADD_A :
      case HIR.OP_SUB:
//  case HIR.OP_SUB_A :
      case HIR.OP_MULT:
      case HIR.OP_DIV:
      case HIR.OP_MOD:
      case HIR.OP_AND:
      case HIR.OP_OR:
      case HIR.OP_XOR:
      case HIR.OP_CMP_EQ:
      case HIR.OP_CMP_NE:
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
      case HIR.OP_SHIFT_LL:
      case HIR.OP_SHIFT_R:
      case HIR.OP_SHIFT_RL:
//  case HIR.OP_LG_NOT  :
      case HIR.OP_NOT:
      case HIR.OP_NEG:
      case HIR.OP_ADDR:
      case HIR.OP_CONV:
      case HIR.OP_DECAY:
      case HIR.OP_UNDECAY:
      case HIR.OP_CONTENTS:
//  case HIR.OP_CAST  :
      case HIR.OP_SIZEOF:
      case HIR.OP_PHI:
      case HIR.OP_OFFSET:
      case HIR.OP_LG_AND:
      case HIR.OP_LG_OR:
        //-- Terminals representing some value:
      case HIR.OP_SYM:
      case HIR.OP_SUBP:
        lExpId = selectExpId(pSubtree, lExpInfOfThis);
        lExpInfOfThis.setNumberOfOperations(
          lExpInfOfThis.getNumberOfOperations() + 1);
        recordDefRefPoint(pSubtree); //##62
        /* //##62
        lIndex = pSubtree.getIndex();
        fDefRefCount++;
        //##60 fFlowIrLink[lIndex - fIndexMin].setDefRefPosition(fDefRefCount); //##60
        //##62 fDefRefPosition[lIndex - fIrIndexMin] = fDefRefCount; //##60
        fDefRefIndex[lIndex - fIrIndexMin] = fDefRefCount; //##62
        fDefRefPoint[fDefRefCount] = pSubtree; //##60
        */ //##62
        break;
      //##70 BEGIN
      case HIR.OP_ASM:
        //##70 lExpId = selectExpId(pSubtree, lExpInfOfThis);
        lExpInfOfThis.setNumberOfOperations(
          lExpInfOfThis.getNumberOfOperations() + 1);
        recordDefRefPoint(pSubtree);
        recordDefPoint(pSubtree); // Not all AsmStmt defines a value but
          // record it as DefPoint.
        break;
      //##70 END
      case HIR.OP_VAR:
      case HIR.OP_PARAM:
      case HIR.OP_ELEM:
        lExpId = selectExpId(pSubtree, lExpInfOfThis);
        recordDefRefPoint(pSubtree); //##62
        Var lVar = (Var)((VarNode)pSubtree).getSymNodeSym();
        //##60 BEGIN
        lExpInfOfThis.getOperandSet().add(lVar);
        HIR lParent = (HIR)pSubtree.getParent();
        if ((lParent == null) ||
            (lParent.getOperator() != HIR.OP_ADDR) ||
            dereferenced(pSubtree)) {
          lExpInfOfThis.getOperandSet0().add(lVar);
        }
        //##60 END
        break;
      case HIR.OP_ASSIGN:
        //##60 BEGIN
        // AssignStmt does not require ExpId but DefRefPoint should be recorded.
        lIndex = pSubtree.getIndex();
        recordDefRefPoint(pSubtree); //##62
        recordDefPoint(pSubtree); //##62
        ExpId lExpId_LHS = getExpId(pSubtree.getChild1());
        if (lExpId_LHS != null) {
          lExpId_LHS.setFlag(FLAG_EXPID_LHS, true); // Set LHS flag.
          ExpInf lExpInfOfLHS = lExpId_LHS.getExpInf();
          if (lExpInfOfLHS.getRValueExpId() == null) {
            HIR lRValueExp = ((HIR)pSubtree.getChild1()).copyWithOperands();
            fMaxIndexOfCopiedNode =((HIR_Impl)lRValueExp)
              .setIndexNumberToAllNodes2(
                  fMaxIndexOfCopiedNode + 1, false);
            ExpInf lRValueExpInf = allocateExpIdToNode(lRValueExp);
            ExpId lRValueExpId = getExpId(lRValueExp, lRValueExp.getIndex()); //##68
            lExpInfOfLHS.setRValueExpId(
                //##68 getExpId(lRValueExp, lRValueExp.getIndex())
                lRValueExpId);
            fSubtreesCopied.add(lRValueExp); //##68
            fExpIdTable[lRValueExp.getIndex() - fIrIndexMin] = lRValueExpId; //##68
            if (fDbgLevel > 3)
              ioRoot.dbgFlow.print(6, " r-value ExpId="
                      + lRValueExpId.getName());
          }
        }
        break;
        //##60 END
      case HIR.OP_CONST:
      default: // Other nodes do not require ExpId because
        break; // they do not directly do computation.
    }
    fExpIdTable[pSubtree.getIndex() - fIrIndexMin] = lExpId; //##62
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(6, "allocateExpIdToNode ",
        pSubtree.getIrName() + " position "
        + Integer.toString(fDefRefCount, 10));
    return lExpInfOfThis;
  } // allocateExpIdToNode

  /** selectExpId
   *  Select ExpId for pSubtree and assign it to pSubtree.
   **/
  //##60 void
  ExpId
    selectExpId(HIR pSubtree, ExpInf pExpInf)
  {
    ExpId lExpId;
    int lHashCode, lSymExpCount;
    String lExpIdName;
    if (pSubtree == null)
      return null;
    //##60 lExpId = pSubtree.getExpId();
    lExpId = getExpId(pSubtree); //##60
    if (lExpId != null)
      return lExpId;
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(6, " selectExpId ",
        pSubtree.getIrName());
    lHashCode = computeHashCodeOfNode(pSubtree);
    for (lExpId = fExpIdHashTable[lHashCode]; lExpId != null;
         lExpId = lExpId.getNextId()) {
      if (isSameTree(getLinkedSubtreeOfExpId(lExpId), pSubtree)) { //##60
        if (fDbgLevel > 3)
          ioRoot.dbgFlow.print(7, " same to " +
            getLinkedSubtreeOfExpId(lExpId).toStringShort()); //##60
        break;
      }
    }
    if (lExpId == null) { // Allocate new ExpId
      lExpIdName = generateExpIdName();
      //##78 lExpId = (ExpId)((ExpIdImpl)(symRoot.symTableCurrent.
      lExpId = (ExpId)((ExpIdImpl)(symRoot.symTableFlow.  //##78
        searchOrAdd(lExpIdName, Sym.KIND_EXP_ID,
                    symRoot.subpCurrent, true, true)));
      if (fFirstExpId == null) // Record the first ExpId.
        fFirstExpId = lExpId;
      lExpId.setExpInf(pExpInf); //##60
      recordSym(lExpId); //##62
      fExpIdList.add(lExpId); //##60
      if (fExpIdHashTable[lHashCode] != null)
        lExpId.setNextId(fExpIdHashTable[lHashCode]);
      fExpIdHashTable[lHashCode] = lExpId;
    }
    else {
    }
    // pSubtree.setExpId(lExpId);
    setExpId(pSubtree, lExpId); //##60
    //##65 BEGIN
    if (! fDefinedSyms.contains(lExpId)) {
      fDefinedSyms.add(lExpId);
      //##65 fFlowAnalSymTable[lExpId.getIndex()] = lExpId;
    }
    // If maximal compound variable, record it.
    switch (pSubtree.getOperator()) {
    case HIR.OP_QUAL:
      if ((pSubtree.getParent() == null)||
          (pSubtree.getParent().getOperator() != HIR.OP_QUAL))
        getMaximalCompoundVars().add(lExpId);
      break;
    case HIR.OP_ARROW:
      if ((pSubtree.getParent() == null)||
          (pSubtree.getParent().getOperator() != HIR.OP_ARROW))
      getMaximalCompoundVars().add(lExpId);
      break;
    case HIR.OP_SUBS:
      if ((pSubtree.getParent() == null)||
          (pSubtree.getParent().getOperator() != HIR.OP_SUBS)||
          (pSubtree.getChildNumber() != 1))
      getMaximalCompoundVars().add(lExpId);
      break;
    default:
      break;
    }
    //##65 END
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(6, lExpId.getName() + " " + lExpId.getIndex());
    return lExpId;
  } // selectExpId

  public int
    getHashCodeOfIndexedNode(int pNodeIndex)
  {
    return fHashCodeOfNode[pNodeIndex - fIrIndexMin]; //##60
  }

  public void
    setHashCodeOfIndexedNode(int pNodeIndex, int pHashCode)
  {
    fHashCodeOfNode[pNodeIndex - fIrIndexMin] = pHashCode; //##60
  }

  /** computeHashCodeOfNode
   *  Compute hash code of node pNode taking into account
   *  the hash codes of its children so that any two subtrees
   *  have the same hash code if they have the same shape.
   **/
  int
    computeHashCodeOfNode(HIR pNode)
  {
    int lCode, lNodeIndex, lChild, lChildCount;
    Sym lSym;

    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(7, " computeHashCodeOfNode ");
    if (pNode == null)
      return 0;
    lNodeIndex = pNode.getIndex();
    lCode = getHashCodeOfIndexedNode(lNodeIndex);
    if (lCode != 0) // Hash code is already computed. return it.
      return lCode;
    lCode = pNode.getOperator() + System.identityHashCode(pNode.getType());
    lSym = pNode.getSym();
    if (lSym != null) { // This is a leaf node attached with a symbol.
      lCode = lCode * 2 + System.identityHashCode(lSym);
    }
    else {
      lChildCount = pNode.getChildCount();
      for (lChild = 1; lChild <= lChildCount; lChild++) {
        lCode = lCode * 2
          + computeHashCodeOfNode((HIR)(pNode.getChild(lChild)));
      }
    }
    //##60 BEGIN
    // Assign different hash code for l-value compared to r-value.
    //##65 if ((pNode.getParent()instanceof AssignStmt) &&
    //##65     (pNode.getParent().getChild1() == pNode))
    if (FlowUtil.isAssignLHS(pNode)) //##65
    {
      lCode = lCode * 2;
    }
    //##60 END
    lCode = (lCode & 0x7FFFFFFF) % EXP_ID_HASH_SIZE;
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(7, Integer.toString(lCode, 10));
    setHashCodeOfIndexedNode(lNodeIndex, lCode);
    return lCode;
  } // computeHashCodeOfNode

  /** isSameTree
   *  @return true if pTree1 and pTree2 have the same shape,
   *    false otherwise.
   **/
  protected boolean
    isSameTree(HIR pTree1, HIR pTree2)
  {
    Sym lSym1, lSym2;
    int lChildCount, lChild;

    if (pTree1 == pTree2)
      return true;
    if ((pTree1 == null) || (pTree2 == null)) // One is null, the other is not.
      return false;
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.print(7, " isSameTree " +
        pTree1.getIrName() + " " + pTree2.getIrName());
    if (computeHashCodeOfNode(pTree1) != computeHashCodeOfNode(pTree2))
      return false;
    //-- With the same hash key. --
    lSym1 = pTree1.getSym();
    if (lSym1 != null) {
      if (lSym1 == pTree2.getSym())
        return true;
      else
        return false;
    }
    else { // The trees has no symbol attached.
      lChildCount = pTree1.getChildCount();
      if ((pTree1.getOperator() != pTree2.getOperator()) ||
          (pTree2.getChildCount() != lChildCount) ||
          (pTree1.getType() != pTree2.getType())) {
        return false; // Both nodes have the same characteristics.
      }
      else { // Examine children.
        for (lChild = 1; lChild <= lChildCount; lChild++) {
          if (!isSameTree((HIR)(pTree1.getChild(lChild)),
            (HIR)(pTree2.getChild(lChild))))
            return false;
        }
        return true; // All children of pTree1 are the same
        // to the corresponding child of pTree2.
      }
    }
  } // isSameTree

//##60 BEGIN

  public void
    computeBBlockSetRefReprs()
  {
    if (fDbgLevel > 0)
      flowRoot.flow.dbg(4, "\ncomputeBBlockSetRefReprs"); //##63
    recordSetRefReprs(); //##63
  } // computeBBlockSetRefReprs

  /**
   * Return true if parent or ancestor if CONTENTS node.
   * @param pHir
   * @return true if pHir is operand of contents node.
   */
  private boolean
    dereferenced(HIR pHir)
  {
    HIR lHir = pHir;
    while ((lHir != null) && (lHir != pHir)) {
      if (lHir.getOperator() == HIR.OP_CONTENTS) {
        return true;
      }
      lHir = (HIR)lHir.getParent();
    }
    return false;
  } // dereferenced
//##60 END

//##62 BEGIN
  public boolean
    hasCall()
  {
    //##62 return hasCallInSubp;
    if (fCallCount > 0)
      return true;
    else
      return false;
  }

  public IR[]       // This should be used only within the package flow.
    getFlowIrLink() // This is not made public in HirSubpFlow.
  {
    return fFlowIrLink;
  }
//##62 END

//##63 BEGIN

// recordSetRefReprs() was moved from DataFlowImpl so that it can be
// called from ConstFoldingHir, etc.

  /**
   * Records the SetRefReprs each BBlock contains.
   */
  public void recordSetRefReprs()
  {
    //##60 BEGIN
    if (fDbgLevel > 0)
      ioRoot.dbgFlow.print(2, "\nrecordSetRefReprs",
        getSubpSym().getName());
    if (isComputedOrUnderComputation(DF_SETREFREPR)) {
      if (fDbgLevel > 0)
        ioRoot.dbgFlow.print(2, "already computed");
      return;
    }
    //##60 END
    //##65 BEGIN
    if (! isComputed(DF_EXPID)) {
      allocateExpIdForSubp();
    }
    //##65 END
    setUnderComputation(DF_SETREFREPR); //##62
    //##65 BEGIN
    if (fArrayOfSetRefReprList == null) {
      fArrayOfSetRefReprList = new SetRefReprList[fBBlockCount + 1];
      if (fDbgLevel > 3)
        ioRoot.dbgFlow.print(4, "allocate fArrayOfSetRefReprList "
          + fBBlockCount + 1);
    }
    //##65 END
    BBlock lBBlock;
    for (Iterator lCFGIterator = getBBlockList().iterator();
         lCFGIterator.hasNext(); ) {
      lBBlock = (BBlock)lCFGIterator.next();
      if (flowRoot.isHirAnalysis())
        recordSetRefReprs((BBlockHirImpl)lBBlock);
    }
    //##60 fDefCount=setRefRepr.getDefSetRefReprCount();
    fDefCount = getDefCount(); //##60
    //##60 DefVectorImpl.setVectorLength(flowRoot, fDefCount);
    setComputedFlag(DF_SETREFREPR); //##60
  } // recordSetRefReprs

// recordSetRefReprs(BBlock) and recordSetRefReprsForNode were moved
// from DataFlowHirImpl so that recordSetRefReprs can be
// called from ConstFoldingHir, etc.

  /** recordSetRefReprs
   * Compute and record the SetRefReprs for each subtree (statement)
   * in the given BBlock.
   *  Only supports HIR so far.
   * @param pBBlock BBlock whose SetRefReprs will be recorded.
   */
  public void recordSetRefReprs(BBlockHirImpl pBBlock)
  {
    if (pBBlock == null)
      return; //##63
    if (fDbgLevel > 3)
      ioRoot.dbgFlow.printObject(4, "recordSetRefReprs",
        "B" + pBBlock.getBBlockNumber()); //##62
    int lNodeIndexMin = getIrIndexMin(); //##60
    if (flowRoot.isHirAnalysis()) {
      //##63 List lSetRefReprList = new ArrayList();
      SetRefReprList lSetRefReprList = new SetRefReprList(pBBlock); //##63
      IR lSubtree;
      SetRefRepr lSetRefRepr;
      int lOpCode;
      int lDefIndex, lNodeIndex;
      for (BBlockSubtreeIterator lBBlockSubtreeIterator = pBBlock.
           bblockSubtreeIterator(); lBBlockSubtreeIterator.hasNext(); ) {
        lSubtree = lBBlockSubtreeIterator.next();
        if (lSubtree == null)
          continue;
        if (fDbgLevel > 3)
          ioRoot.dbgFlow.printObject(4, " next subtree in recordSetRefReprs",
            lSubtree.toStringShort()); //##62
        recordSetRefReprsForNode((HIR)lSubtree, lSetRefReprList, pBBlock);
      }
      //##63 pBBlock.setWorkFA(lSetRefReprList);
      setSetRefReprList(pBBlock, lSetRefReprList); //##63
      if (fDbgLevel >= 5) //##63
        //##65 ioRoot.dbgFlow.print(5, lSetRefReprList.toString()); //##63
        ioRoot.dbgFlow.print(5, getSetRefReprList(pBBlock).toString()); //##65
    }
    else
      throw new RuntimeException("Not implemented.");
  } // recordSetRefReprs

  /**
   * Make instance of SetRefReprHirEImpl if pSubtree is AssignStmt,
   * ExpStmt (FunctionExp, conditional exp) or Return statement
   * and record it to pSetRefReprList.
   * @param pSubtree
   * @param pSetRefReprList
   * @param pBBlock
   */
  public void recordSetRefReprsForNode(HIR pSubtree,
      List pSetRefReprList, BBlock pBBlock)
  {
    int lNodeIndexMin = getIrIndexMin();
    SetRefRepr lSetRefRepr;
    int lOpCode;
    int lDefIndex, lNodeIndex;
    int lChildCount = pSubtree.getChildCount();
    //##65 if (lChildCount <= 0)
    if ((lChildCount <= 0)&&(! (pSubtree instanceof VarNode))) //##65
      return;
    if (fDbgLevel > 3) //##67
      ioRoot.dbgFlow.printObject(5, "\n  recordSetRefReprsForNode",
    pSubtree.toStringShort() + " " + getExpId(pSubtree));
    lOpCode = pSubtree.getOperator();
    /* //##63
    if (fDbgFlow > 0) {
      switch (lOpCode) {
        case HIR.OP_ASSIGN:
        case HIR.OP_RETURN:
        case HIR.OP_EXP_STMT:
        case HIR.OP_CALL:
          ioRoot.dbgFlow.printObject(4, "\n  record to SetRefReprsList",
          pSubtree.toStringShort() + " " + fSubpFlow.getExpId(pSubtree));
      }
    }
    */ //##63
    switch (lOpCode) {
      case HIR.OP_ASSIGN:
        //##60 lSetRefRepr=(SetRefRepr)(new SetRefReprHirImpl((HIR)lSubtree, this));
        lSetRefRepr = (SetRefRepr)(new SetRefReprHirEImpl(pSubtree,
          pBBlock, true, null)); //##65
        pSetRefReprList.add(lSetRefRepr);
        //##62 fSetRefReprTable[pSubtree.getIndex() - ((SubpFlowImpl)fSubpFlow).getIrIndexMin()] = lSetRefRepr; //##60
        //##62 fSetRefReprTable[pSubtree.getIndex() - lNodeIndexMin] = lSetRefRepr; //##62
        setSetRefReprOfIR(lSetRefRepr, pSubtree); //##62
        if (lSetRefRepr.sets()) {
          //##62 lDefIndex = fSubpFlow.getDefPoint(pSubtree.getIndex()).getIndex() - lNodeIndexMin;
          //##62 lDefIndex = fSubpFlow.incrementDefCount(); //##62
          //##62 lNodeIndex = lSetRefRepr.getIR().getIndex();
          //##62 fNodeIndexTable[lDefIndex] = lNodeIndex;
          //##62 fDefNodeIndexTable[lDefIndex] = lNodeIndex; //##62
          //##62 fDefSetRefReprNoTable[lNodeIndex - lNodeIndexMin] = lDefIndex; //##60
          //##62 flow.dbg(4, " lDefIndex " + lDefIndex); //##62
        }
        break;
      case HIR.OP_RETURN:
      case HIR.OP_EXP_STMT:
        //##62 BEGIN
        HIR lChild1 = (HIR)pSubtree.getChild1();
        if (lChild1 != null) {
          if (lChild1.getOperator() == HIR.OP_CALL) {
            recordSetRefReprsForNode((HIR)pSubtree.getChild1(),
              pSetRefReprList, pBBlock);
          }
          else {
            lSetRefRepr = (SetRefRepr)(new SetRefReprHirEImpl(lChild1,
              pBBlock, false, null));
            pSetRefReprList.add(lSetRefRepr);
          }
        }
        break;
        //##62 END
      case HIR.OP_CALL:
        //##60 lSetRefRepr=(SetRefRepr)(new SetRefReprHirImpl((HIR)lSubtree, this));
        lSetRefRepr = (SetRefRepr)(new SetRefReprHirEImpl(pSubtree,
          pBBlock, false, null)); //##60
        pSetRefReprList.add(lSetRefRepr);
        //##62 fSetRefReprTable[pSubtree.getIndex() - lNodeIndexMin] = lSetRefRepr; //##62
        setSetRefReprOfIR(lSetRefRepr, pSubtree); //##70
        break;
      //##70 BEGIN
      case HIR.OP_ASM:
        lSetRefRepr = (SetRefRepr)(new SetRefReprHirEImpl(pSubtree,
          pBBlock, false, null));
        pSetRefReprList.add(lSetRefRepr);
        setSetRefReprOfIR(lSetRefRepr, pSubtree);
        break;
      //##70 END
      case HIR.OP_JUMP:
        break;
      case HIR.OP_LABELED_STMT:
        break;
      default:
        //##63 BEGIN
        if (pSubtree instanceof Exp) { // Conditional expression
                                       // and switch-selection expression. //##65
          lSetRefRepr = (SetRefRepr)(new SetRefReprHirEImpl(pSubtree,
                pBBlock, false, null));
          pSetRefReprList.add(lSetRefRepr);
          setSetRefReprOfIR(lSetRefRepr, pSubtree); //##62
        }
        //##63 END
        break;
    } // End of switch
  } // recordSetRefReprsForNode

//##63 END

} // HirSubpFlowImpl class
