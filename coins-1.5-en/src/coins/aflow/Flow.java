/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import java.util.Iterator;
import java.util.List;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.aflow.util.FAList;
import coins.aflow.util.FlowError;
import coins.driver.CoinsOptions;
import coins.driver.CompileSpecification;
import coins.driver.Trace;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Program;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
//import coins.ir.lir.LIRNode; // 2004.05.31 S.Noishi
//import coins.ir.lir.LIRType; // 2004.05.31 S.Noishi
//import coins.ir.lir.Prologue; // 2004.05.31 S.Noishi
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymNestIterator;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;


//##10: Modified on Dec. 2001.

/** <pre>Representation of data flow information:
 *
 *  Each expression is assigned an expression identifier (FlowExpId)
 *  if the expression has a value (such as r-value or l-value).
 *  If two expressions has the same form then
 *  the corresponding expression identifier is the same.
 *
 *  Interface hierarchy:
 *
 *   BitVector
 *     |- BBlockVector    -- for BBlock
 *     |- ExpVector       -- for expression
 *     |- FlowAnalSymVector -- for symbol
 *     |- PositionVector  -- for def/use point
 *         |- DefVector   -- for definition point
 *
 *   BitVectorIterator
 *     |- ExpVectorIterator  -- traverses all expressions
 *     |                        that have true bit in a given bit vector.
 *     |- FlowAnalSymVector  -- traverses all symbols that have true bit in a given bit vector.
 *     |- PositionVectorIterator
 *                           -- traverses all true def/use nodes
 *         |- DefVectorIterator -- traverses all def nodes
 *
 ** Class hierarchy:
 *
 *   BitVectorImpl  extends Root implements BitVector
 *        |- BBlockVectorImpl implements BBlockVector
 *        |- ExpVectorImpl      implements ExpVector
 *        |- FlowAnalSymVector implements FlowAnalSymVector
 *     |- PositionVectorImpl implements PositionVector
 *         |- DefVectorImpl  implements DefVector
 *
 *   BitVectorIteratorImpl           implements BitVectorIterator
 *     |- ExpVectorIteratorImpl      implements ExpVectorIterator
 *     |- FlowAnalSymVectorIteratorImpl implements FlowAnalSymVector
 *     |- PositionVectorIteratorImpl implements PositionVectorIterator
 *           |- DefVectorIteratorImpl implements DefVectorIterator
 *
 ** Basic data flow information is as follows:
 *
 *  Notations
 *    x, y, t, u : variable or register representing an operand.
 *    op         : operator.
 *    def(x)     : shows that value of x is definitely defined.
 *    mod(x)     : shows that value of x is posibly defined.
 *    use(x)     : shows that x is used.
 *    p(def(x))  : value of x is (definitely) modified (i.e. via assign) at program point p.
 *    p(mod(x, y, ...))  : value of x, y, ... are modified at program point p (modified means possibly changed).
 *    p(use(x))  : x is used at program point p.
 *    or_all(z)  : construct a set by applying or-operation
 *                 on all components indicated by z.
 *    and_all(z) : construct a set by applying and-operation
 *                 on all components indicated by z.
 *
 *  PDef(B)  =
 *            { p | p(mod(x, y, ...)) is included in B and after that point there is
 *                  no p' s.t. p'(def(x)) nor p" s.t. p"(def(y)), ... in B. }
 *  DKill(B) =
 *            { p | p(def(x)) is not included in B and
 *                  p'(def(x)) is included in B. }
 *
 *  PReach(B)=
 *            { p | there is some path from program point p
 *                  that modifies some variables x, y, ... (that is, p(mod(x, y, ...))) to the entry of B
 *                  such that there is no p'(def(x)) or no p''(def(y)) or ... on that path. }
 *              PReach(B) = or_all( (PDef(B') | (PReach(B') - DKill(B')))
 *                               for all predecessors B' of B)
 *  DDefined(B) =
 *            { x | x is definitely modified in B. }
 *  PDefined(B) =
 *            { x | x is posibly modified in B. }
 *  PExposed(B) =
 *            { x | x is possibly used in B and x is not definitely set in B
 *                  before x is used. }
 *  PUsed(B) = {x|x is possibly used in B}
 *
 *  DEGen(B) =
 *            { op(x,y) | expression op(x,y) is computed in B and after
 *                        that point, neither x nor y are possibly set in B. }
 *              Thus, the result of op(x,y) is available after B.
 *  PEKill(B) =
 *            { op(x,y) | operand x or y is possibly modified in B and the
 *                        expression op(x,y) is not re-evaluated after
 *                        that definition in B. }
 *              If t = op(x,y) is killed in B,
 *              then op(t,u) should also be killed in B.
 *  DAvailIn(B) =
 *            { op(x,y) | op(x,y) is computed in every paths to B and
 *                        x, y are not modified after the computations
 *                        on the paths. }
 *              Thus, the result of op(x,y) can be used without
 *              re-evaluation in B.
 *  DAvailOut(B) =
 *            { op(x,y) | op(x,y) is computed in every paths to the exit of B and
 *                        x, y are not modified after the computations
 *                        on the paths. }
 *              Thus, op(x,y) can be used without re-evaluation after B.
 *            Following relations hold.
 *              DAvailIn(B) = and_all(DAvailOut(B') for all predecessors B'
 *                                    of B) if B is not an entry block;
 *              DAvailIn(B) = { } if B is an entry block.
 *              DAvailOut(B) = DEGen(B) | (DAvailIn(B) - PEKill(B))
 *  PLiveIn(B) =
 *            { x | x is alive at entry to B, that is, on some path from
 *                  entrance point of B to use point of x, x is not definitely set. }
 *              Thus, x in PLiveIn(B) should not be changed until it is used.
 *  PLiveOut(B) =
 *            { x | x is live at exit from B, that is, there is some
 *                  path from B to B' where x is in PExposed(B'). }
 *            Following relations hold.
 *              PLiveOut(B) = or_all(PLiveIn(B') for all successors B' of B
 *              PLiveIn(B)  = PExposed(B) | (PLiveOut(B) - DDefined(B))
 *  DDefIn(B) =
 *            { x | x is always defined at entry to B whichever path
 *                  may be taken. }
 *              DDefIn(B) = and_all(DDefOut(B') for all predecessors B' of B)
 *  DDefOut(B) =
 *            { x | x is always defined at exit from B whichever path
 *                  may be taken.}
 *              DDefOut(B) = DDefined(B) | DefIn(B)
 *
 ** Data structure
 *
 *  There are several ways of representing data flow information
 *  such as bit vector representation and discrete list representation.
 *  When a new data flow information is introduced, it will be necessary
 *  to solve new data flow equation concerning it. For that purpose,
 *  several methods treating bit vector data structures are provided.
 *  By using these methods and methods in BitVectorInterface,
 *  we will be able to solve new data flow equation and to access the new
 *  data flow information.
 *
 *  In the bit vector representation, information can be accessed by
 *  position number of IR nodes and index number of symbols which
 *  can be get from IR node or symbol table each respectively.
 *  BBlockVector is a bit vector representing 0/1 information for each BBlock.
 *  DefVector is a bit vector representing 0/1 information for
 *  each value-setting node. If its value at position p is 1, true is represented
 *  for the IR node at p, if it is 0, false is represented at that node.
 *  PointVector is a bit vector representing 0/1 information for
 *  each IR node. If its value at position p is 1, true is represented
 *  for the IR node at p, if it is 0, false is represented at that node.
 *  FlowAnalSymVector is a bit vector representing 0/1 information
 *  for each symbol such as variable or register. A symbol is assigned a local index. By local I mean it is unique within a subprogram. If FlowAnalSymVector value at position
 *  i is 1, true is represented for the symbol having index number i,
 *  if it is 0, false is represented for that symbol. ExpVector is a bit vector representing 0/1 information for each unique expression. Expressions having the same form are assigned the same local object (FlowExpId). An expression is
 *  represented by the index number assigned to the FlowExpId
 *  corresponding to the expression. If an ExpVector's n-th bit is 1, true is represented for the expression having the FlowExpId object whose index is i.
 *
 *  All the indexes such as symbol indexes are numbered 1, 2, 3, ... .
 *  If such numbering is not yet performed, the index value
 *  will be 0. Position 0 in the bit vectors is not used.
 *
 ** Usage
 *
 *  Bit vector class having definite bit length should be defined as a
 *  subclass of BitVector.
 *  Its procedure for a subprogram is as follows:
 *
 *   // Flow Analyzer Initiation
 *    Assign index number to symbols accessed in the subprogram;
 *    Assign index number to FlowExpIds representing expression;
 *
 *   // Individual Flow Analyses Required for the Optimization to Perform
 *    Instantiate DefVectors
 *        by SubpFlow#defVector();
 *    Instantiate ExpVectors
 *        by SubpFlow#expVector();
 *    Manipulate the bit vectors
 *        by using methods for BitVector operation;
 *    Instantiate DefVectorIterator for some DefVector
 *        by SubpFlow#defVectorIterator();
 *    Scan all nodes having true bit in the DefVector
 *        by using the DefVectorIterator.
 *    Instantiate ExpVectorIterator for some ExpVector
 *        by flowRoot.subpFlow.expVectorIterator();
 *    Scan all symbols or expressions having true bit in the ExpVector
 *        by using the ExpVectorIterator.
 *  </pre>
 * <p>This class itself is a demo driver class for flow analysis modules.
 *  In real world, each flow analysis module is called on demand
 * by higher level modules. See { coins.aflow.util.SelfCollectingResults}.
 *
 */
public class Flow {
    public final FlowRoot flowRoot;
    public final HirRoot hirRoot;
    public final IoRoot ioRoot;
    public final Flow flow;
    private final CompileSpecification fSpec;
    private final Trace fTrace;
    private final CoinsOptions fCoinsOptions;
    protected coins.aflow.SubpFlow fSubpFlow; //##51

    public Flow(FlowRoot pFlowRoot) {
        flow = pFlowRoot.aflow;
        flowRoot = pFlowRoot;
        hirRoot = flowRoot.hirRoot;
        ioRoot = flowRoot.ioRoot;

        fSpec = ioRoot.getCompileSpecification();
        fCoinsOptions = fSpec.getCoinsOptions();
        fTrace = fSpec.getTrace();
    }

    /**
     * Assuming the input program is C, give some warnings.
     */
    public int doHirForC() {
        FlowResults lResults = flow.results();
        ShowFlow lShowFlow = new ShowFlow(lResults);
        IrList lSubpDefList = ((Program) hirRoot.programRoot).getSubpDefinitionList();
        Iterator subpDefIterator = lSubpDefList.iterator();
        int lReturnValue = 0;

        while (subpDefIterator.hasNext()) {
            SubpDefinition subpDef = (SubpDefinition) (subpDefIterator.next());
            SubpFlow lSubpFlow = flow.subpFlow(subpDef, lResults);
            fSubpFlow = lSubpFlow; //##57
            lSubpFlow.controlFlowAnal();
            lResults.find("Initiate", lSubpFlow);

            //						subpDef.printHir("aaaa");
            lResults.find("DefUseList", lSubpFlow);

            //			lShowFlow.showControlFlow(lSubpFlow);
            //			subpDef.printHir("aaaa");
            boolean lUnreachableCodeExists = false;
            FAList lBBlockTable = lSubpFlow.getBBlockTable();
            List lBBlocks = lSubpFlow.getBBlocks();
            lBBlockTable.toList().removeAll(lBBlocks); // Destroys CFG!

            for (Iterator lBBlockIt = lBBlockTable.iterator();
                    lBBlockIt.hasNext();) {
                BBlock lBBlock = (BBlock) lBBlockIt.next();
                Stmt lStmt = null;

                for (BBlockStmtIterator lBBlockStmtIt = new BBlockStmtIterator(
                            (BBlockHir) lBBlock);
                        lStmt != BBlockStmtIterator.EOB;) {
                    lStmt = (Stmt) lBBlockStmtIt.next();

                    int lOpCode = lStmt.getOperator();

                    switch (lOpCode) {
                    case HIR.OP_WHILE:
                    case HIR.OP_FOR:
                    case HIR.OP_UNTIL:
                    case HIR.OP_JUMP:
                    case HIR.OP_CALL:
                    case HIR.OP_ASSIGN:
                    case HIR.OP_IF:
                    case HIR.OP_INDEXED_LOOP:
                    case HIR.OP_SWITCH:
                    case HIR.OP_RETURN:
                    case HIR.OP_EXP_STMT:
                        flow.warning(subpDef.getSubpSym() +
                            ": unreachable code exists.");

                        //System.out.println(lStmt + " in " + lBBlock);
                        lUnreachableCodeExists = true;
                        lReturnValue = -1;
                    }
                }
            }

            Sym lSym;
            DefUseCell lCell;
            int lTypeKind;
            int lSymKind;

            if (!lUnreachableCodeExists) {
                for (SymIt lIt = new SymIt(flowRoot.symRoot.symTableCurrentSubp);
                        lIt.hasNext();) {
                    lSym = lIt.next();

                    if (lSym.getDefinedIn() == null) {
                        continue;
                    }

                    if (lSym instanceof Var) {
                        if (!((FAList) lResults.get("SymIndexTable", lSubpFlow)).contains(
                                    lSym)) {
                            flow.warning(subpDef.getSubpSym() +
                                ": unused variable " + lSym.getName());

                            continue;
                        }

                        if ((lSym.getSymKind() != Sym.KIND_ELEM)) {
                            switch (lSym.getSymType().getTypeKind()) {
                            case Type.KIND_VECTOR:
                            case Type.KIND_STRUCT:
                            case Type.KIND_UNION:
                                break;

                            default:

                                for (Iterator lIt0 = ((DefUseList) lResults.get(
                                            "DefUseList", lSym, lSubpFlow)).getDefUseCells()
                                                      .iterator();
                                        lIt0.hasNext();)
                                    if ((lCell = (DefUseCell) lIt0.next()).getDefNode() == DefUseCell.UNINITIALIZED) {
                                        flow.warning(subpDef.getSubpSym() +
                                            ": uninitialized variable " +
                                            lSym.getName());
                                        lReturnValue = -2;

                                        break;
                                    }
                            }
                        }
                    }
                }
            }

            if (fTrace.shouldTrace("Flow", 1)) {
                subpDef.printHir("After HIR flow analysis");
            }
        }

        return lReturnValue;
    }

            private static class SymIt implements SymIterator {
        private SymTable fSymTableCurrent;
        private final SymNestIterator fSymNestIt;
        private SymIterator fSymIt;
        private boolean fLookingDown = true;
        private boolean fNextChecked = false;

        private SymIt(SymTable pSymTable) {
            fSymTableCurrent = pSymTable;
            fSymNestIt = pSymTable.getSymNestIterator();
        }

        public boolean hasNext() {
            fNextChecked = true;

            if (fLookingDown) {
                if (fSymNestIt.hasNext()) {
                    return true;
                } else {
                    fLookingDown = false;
                }
            } else if (fSymIt.hasNext()) {
                return true;
            }

            while (true) {
                fSymTableCurrent = fSymTableCurrent.getParent();

                if (fSymTableCurrent == null) {
                    return false;
                }

                fSymIt = fSymTableCurrent.getSymIterator();

                if (fSymIt.hasNext()) {
                    return true;
                }
            }
        }

        public Sym next() {
            if (!fNextChecked) {
                throw new FlowError(
                    "hasNext() must be called before calling next().");
            }

            fNextChecked = false;

            if (fLookingDown) {
                return fSymNestIt.next();
            } else {
                return fSymIt.next();
            }
        }

        public Var nextVar() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Performs various HIR flow analyses. The command line switches determine which analyses to perform.
     *
     * See coins.FlowRoot.
     */
    public void doHir() {
        FlowResults.putRegClasses(new RegisterFlowAnalClasses());

        IrList lSubpDefList = ((Program) hirRoot.programRoot).getSubpDefinitionList();
        Iterator subpDefIterator = lSubpDefList.iterator();
        BBlock lBBlock;
        int lReturnValue = 0;
        dbg(1, "doHir",""); //##25;
        while (subpDefIterator.hasNext()) {
            SubpDefinition subpDef = (SubpDefinition) (subpDefIterator.next());
            dbg(2, "doHir",subpDef.getSubpSym().getName()); //##57;

            FlowResults lResults = flow.results();
            ShowFlow lShowFlow = new ShowFlow(lResults);
            SubpFlow lSubpFlow = flow.subpFlow(subpDef, lResults);
            fSubpFlow = lSubpFlow; //##57
            fSubpFlow.computeSetOfGlobalVariables(); //##57

            if (!checkTreeStructure(subpDef, ioRoot)) {
                throw new FlowError("Not tree.");
            }

            ;

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_CONTROL_FLOW)) {
                lSubpFlow.controlFlowAnal();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DOMINATOR)) {
                lSubpFlow.makeDominatorTree();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_POST_DOMINATOR)) {
                lSubpFlow.makePostdominatorTree();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW)) {
                lSubpFlow.initiateDataFlow();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_KILL)) {
//                lSubpFlow.findDDef();
                lSubpFlow.findPDef();
                lSubpFlow.findDKill();
//                lSubpFlow.findPKill();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_REACH)) {
//                lSubpFlow.findDReach();
                lSubpFlow.findPReach();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEFINED_EXPOSED)) {
                lSubpFlow.findDDefined();
                lSubpFlow.findPDefined();
//                lSubpFlow.findDExposedUsed();
                lSubpFlow.findPExposedUsed();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_EGEN_EKILL)) {
                lSubpFlow.findDEGen();
                lSubpFlow.findPEKill();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_AVAIL_IN_OUT)) {
                lSubpFlow.findDAvailInAvailOut();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_LIVE_IN_OUT)) {
                lSubpFlow.findPLiveInLiveOut();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_IN_OUT)) {
                lSubpFlow.findDDefInDefOut();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW)) {
 //               lSubpFlow.findDDefUse();
                lSubpFlow.findDefUse();

                //                                                lSubpFlow.findUseDef();
            }

            if (fTrace.shouldTrace("Flow", 1)) {
                lShowFlow.showControlFlow(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 2)) {
                lShowFlow.showDominatorTree(lSubpFlow);
                lShowFlow.showPostdominatorTree(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 3)) {
                lShowFlow.showImmediateDominators(lSubpFlow);
                lShowFlow.showImmediatePostdominators(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 1)) {
//                lShowFlow.showVectorsByName("DDef", lSubpFlow);
                lShowFlow.showVectorsByName("PDef", lSubpFlow);
                lShowFlow.showVectorsByName("DKill", lSubpFlow);
//                lShowFlow.showVectorsByName("PKill", lSubpFlow);
//                lShowFlow.showVectorsByName("DReach", lSubpFlow);
                lShowFlow.showVectorsByName("PReach", lSubpFlow);
                lShowFlow.showVectorsByName("DDefined", lSubpFlow);
                lShowFlow.showVectorsByName("PDefined", lSubpFlow);
                lShowFlow.showVectorsByName("PExposed", lSubpFlow);
                lShowFlow.showVectorsByName("PUsed", lSubpFlow);
                lShowFlow.showVectorsByName("DEGen", lSubpFlow);
                lShowFlow.showVectorsByName("PEKill", lSubpFlow);
                lShowFlow.showVectorsByName("DAvailIn", lSubpFlow);
                lShowFlow.showVectorsByName("DAvailOut", lSubpFlow);
                lShowFlow.showVectorsByName("PLiveIn", lSubpFlow);
                lShowFlow.showVectorsByName("PLiveOut", lSubpFlow);
                lShowFlow.showVectorsByName("DDefIn", lSubpFlow);
                lShowFlow.showVectorsByName("DDefOut", lSubpFlow);

   //             lShowFlow.showDDefUse(lSubpFlow);
   //             lShowFlow.showDUseDef(lSubpFlow);
                lShowFlow.showPDefUse(lSubpFlow);
                lShowFlow.showPUseDef(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 4)) {
                for (Iterator lBBlockIt = lSubpFlow.getBBlocks().iterator();
                        lBBlockIt.hasNext();) {
                    lBBlock = (BBlock) lBBlockIt.next();
//                    lShowFlow.showVectorsByName("DDef", lBBlock);
                    lShowFlow.showVectorsByName("PDef", lBBlock);
                    lShowFlow.showVectorsByName("DKill", lBBlock);
//                    lShowFlow.showVectorsByName("PKill", lBBlock);
//                    lShowFlow.showVectorsByName("DReach", lBBlock);
                    lShowFlow.showVectorsByName("PReach", lBBlock);
                    lShowFlow.showVectorsByName("DDefined", lBBlock);
                    lShowFlow.showVectorsByName("PDefined", lBBlock);
//                    lShowFlow.showVectorsByName("DExposed", lBBlock);
                    lShowFlow.showVectorsByName("PExposed", lBBlock);
//                    lShowFlow.showVectorsByName("DUsed", lBBlock);
                    lShowFlow.showVectorsByName("PUsed", lBBlock);
                    lShowFlow.showVectorsByName("DEGen", lBBlock);
                    lShowFlow.showVectorsByName("PEKill", lBBlock);
                    lShowFlow.showVectorsByName("DAvailIn", lBBlock);

                    //					lShowFlow.showVectorsByName("DAvailOut", lBBlock);
                    //					lShowFlow.showVectorsByName("PLiveIn", lBBlock);
                    lShowFlow.showVectorsByName("PLiveOut", lBBlock);
                    lShowFlow.showVectorsByName("DDefIn", lBBlock);

                    //					lShowFlow.showVectorsByName("DDefOut", lBBlock);
                }
            }
        }
    }

  //##52 BEGIN
// Flow analysis used in PRE
      public void doHir0(SubpDefinition subpDef,
                         FlowResults lResults,
                         SubpFlow lSubpFlow)
      {
          dbg(1, "\ndoHir0"," BEGIN" + subpDef.getSubpSym().getName()); //##56;
          BBlock lBBlock;
          int lReturnValue = 0;

          {
              ShowFlow lShowFlow = new ShowFlow(lResults);

              if (!checkTreeStructure(subpDef, ioRoot)) {
                  throw new FlowError("Not tree.");
              }
              ;
                  lSubpFlow.controlFlowAnal();
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DOMINATOR)) {
                  lSubpFlow.makeDominatorTree();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_POST_DOMINATOR)) {
                  lSubpFlow.makePostdominatorTree();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW)) {
                  lSubpFlow.initiateDataFlow();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_KILL)) {
                  //## lSubpFlow.findDDef();
                  lSubpFlow.findPDef();
                  lSubpFlow.findDKill();
                  //## lSubpFlow.findPKill();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_REACH)) {
                  //## lSubpFlow.findDReach();
                  lSubpFlow.findPReach();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEFINED_EXPOSED)) {
                  lSubpFlow.findDDefined();
                  lSubpFlow.findPDefined();
                  //## lSubpFlow.findDExposedUsed();
                  lSubpFlow.findPExposedUsed();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_EGEN_EKILL)) {
                  lSubpFlow.findDEGen();
                  lSubpFlow.findPEKill();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_AVAIL_IN_OUT)) {
                  lSubpFlow.findDAvailInAvailOut();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_LIVE_IN_OUT)) {
                  lSubpFlow.findPLiveInLiveOut();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_IN_OUT)) {
                  lSubpFlow.findDDefInDefOut();
              }
              if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW)) {
                  //## lSubpFlow.findDDefUse();
                  lSubpFlow.findDefUse();
              }
              if (fTrace.shouldTrace("Flow", 1)) {
                  lShowFlow.showControlFlow(lSubpFlow);
              }
              if (fTrace.shouldTrace("Flow", 2)) {
                  lShowFlow.showDominatorTree(lSubpFlow);
                  lShowFlow.showPostdominatorTree(lSubpFlow);
              }
              if (fTrace.shouldTrace("Flow", 3)) {
                  lShowFlow.showImmediateDominators(lSubpFlow);
                  lShowFlow.showImmediatePostdominators(lSubpFlow);
              }
              if (fTrace.shouldTrace("Flow", 1)) {
                  //## lShowFlow.showVectorsByName("DDef", lSubpFlow);
                  lShowFlow.showVectorsByName("PDef", lSubpFlow);
                  lShowFlow.showVectorsByName("DKill", lSubpFlow);
                  //## lShowFlow.showVectorsByName("PKill", lSubpFlow);
                  //## lShowFlow.showVectorsByName("DReach", lSubpFlow);
                  lShowFlow.showVectorsByName("PReach", lSubpFlow);
                  lShowFlow.showVectorsByName("DDefined", lSubpFlow);
                  lShowFlow.showVectorsByName("PDefined", lSubpFlow);
                  //## lShowFlow.showVectorsByName("DExposed", lSubpFlow);
                  lShowFlow.showVectorsByName("PExposed", lSubpFlow);
                  //## lShowFlow.showVectorsByName("DUsed", lSubpFlow);
                  lShowFlow.showVectorsByName("PUsed", lSubpFlow);
                  lShowFlow.showVectorsByName("DEGen", lSubpFlow);
                  lShowFlow.showVectorsByName("PEKill", lSubpFlow);
                  lShowFlow.showVectorsByName("DAvailIn", lSubpFlow);
                  lShowFlow.showVectorsByName("DAvailOut", lSubpFlow);
                  lShowFlow.showVectorsByName("PLiveIn", lSubpFlow);
                  lShowFlow.showVectorsByName("PLiveOut", lSubpFlow);
                  lShowFlow.showVectorsByName("DDefIn", lSubpFlow);
                  lShowFlow.showVectorsByName("DDefOut", lSubpFlow);
                  //## lShowFlow.showDDefUse(lSubpFlow);
                  //## lShowFlow.showDUseDef(lSubpFlow);
                  lShowFlow.showPDefUse(lSubpFlow);
                  lShowFlow.showPUseDef(lSubpFlow);
              }

              if (fTrace.shouldTrace("Flow", 4)) {
                  for (Iterator lBBlockIt = lSubpFlow.getBBlocks().iterator();
                          lBBlockIt.hasNext();) {
                      lBBlock = (BBlock) lBBlockIt.next();
                      //## lShowFlow.showVectorsByName("DDef", lBBlock);
                      lShowFlow.showVectorsByName("PDef", lBBlock);
                      lShowFlow.showVectorsByName("DKill", lBBlock);
                      //## lShowFlow.showVectorsByName("PKill", lBBlock);
                      //## lShowFlow.showVectorsByName("DReach", lBBlock);
                      lShowFlow.showVectorsByName("PReach", lBBlock);
                      lShowFlow.showVectorsByName("DDefined", lBBlock);
                      lShowFlow.showVectorsByName("PDefined", lBBlock);
                      //## lShowFlow.showVectorsByName("DExposed", lBBlock);
                      lShowFlow.showVectorsByName("PExposed", lBBlock);
                      //## lShowFlow.showVectorsByName("DUsed", lBBlock);
                      lShowFlow.showVectorsByName("PUsed", lBBlock);
                      lShowFlow.showVectorsByName("DEGen", lBBlock);
                      lShowFlow.showVectorsByName("PEKill", lBBlock);
                      lShowFlow.showVectorsByName("DAvailIn", lBBlock);
                      lShowFlow.showVectorsByName("PLiveOut", lBBlock);
                      lShowFlow.showVectorsByName("DDefIn", lBBlock);
                  }
              }
          }
          dbg(1, "\ndoHir0"," END"); //##56;
      }
//##52 END


    /**
     * Performs various LIR flow analyses. The command line switches determine which analyses to perform.
     *
     * See coins.FlowRoot.
     */
    public void doLir() {
        FlowResults.putRegClasses(new RegisterFlowAnalClasses());

        IrList lSubpDefList = ((Program) hirRoot.programRoot).getSubpDefinitionList();
        Iterator subpDefIterator = lSubpDefList.iterator();
        int lReturnValue = 0;

        while (subpDefIterator.hasNext()) {
            SubpDefinition subpDef = (SubpDefinition) (subpDefIterator.next());

            FlowResults lResults = flow.results();
            ShowFlow lShowFlow = new ShowFlow(lResults);
            SubpFlow lSubpFlow = flow.subpFlow(subpDef, lResults);

            //			if (!checkTreeStructure(subpDef, ioRoot))
            //				            throw new FlowError("Not tree.");
            //				;
            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_CONTROL_FLOW)) {
                lSubpFlow.controlFlowAnal();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DOMINATOR)) {
                lSubpFlow.makeDominatorTree();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_POST_DOMINATOR)) {
                lSubpFlow.makePostdominatorTree();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW)) {
                lSubpFlow.initiateDataFlow();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_KILL)) {
//                lSubpFlow.findDDef();
                lSubpFlow.findPDef();
                lSubpFlow.findDKill();
//                lSubpFlow.findPKill();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_REACH)) {
//                lSubpFlow.findDReach();
                lSubpFlow.findPReach();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEFINED_EXPOSED)) {
                lSubpFlow.findDDefined();
                lSubpFlow.findPDefined();
                lSubpFlow.findDExposedUsed();
                lSubpFlow.findPExposedUsed();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_EGEN_EKILL)) {
                lSubpFlow.findDEGen();
                lSubpFlow.findPEKill();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_AVAIL_IN_OUT)) {
                lSubpFlow.findDAvailInAvailOut();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_LIVE_IN_OUT)) {
                lSubpFlow.findPLiveInLiveOut();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_IN_OUT)) {
                lSubpFlow.findDDefInDefOut();
            }

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_MINIMAL_DATA_FLOW)) {
                lSubpFlow.findDefUse();

                //                                                lSubpFlow.findUseDef();
            }

            if (fTrace.shouldTrace("Flow", 1)) {
                lShowFlow.showControlFlow(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 2)) {
                lShowFlow.showDominatorTree(lSubpFlow);
                lShowFlow.showPostdominatorTree(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 3)) {
                lShowFlow.showImmediateDominators(lSubpFlow);
                lShowFlow.showImmediatePostdominators(lSubpFlow);
            }

            if (fTrace.shouldTrace("Flow", 1)) {
                lShowFlow.showVectorsByName("DDef", lSubpFlow);
                lShowFlow.showVectorsByName("PDef", lSubpFlow);
                lShowFlow.showVectorsByName("DKill", lSubpFlow);
                lShowFlow.showVectorsByName("PKill", lSubpFlow);
                lShowFlow.showVectorsByName("DReach", lSubpFlow);
                lShowFlow.showVectorsByName("PReach", lSubpFlow);
                lShowFlow.showVectorsByName("DDefined", lSubpFlow);
                lShowFlow.showVectorsByName("PDefined", lSubpFlow);
                lShowFlow.showVectorsByName("DExposed", lSubpFlow);
                lShowFlow.showVectorsByName("PExposed", lSubpFlow);
                lShowFlow.showVectorsByName("DUsed", lSubpFlow);
                lShowFlow.showVectorsByName("PUsed", lSubpFlow);
                lShowFlow.showVectorsByName("DEGen", lSubpFlow);
                lShowFlow.showVectorsByName("PEKill", lSubpFlow);
                lShowFlow.showVectorsByName("DAvailIn", lSubpFlow);
                lShowFlow.showVectorsByName("DAvailOut", lSubpFlow);
                lShowFlow.showVectorsByName("PLiveIn", lSubpFlow);
                lShowFlow.showVectorsByName("PLiveOut", lSubpFlow);
                lShowFlow.showVectorsByName("DDefIn", lSubpFlow);
                lShowFlow.showVectorsByName("DDefOut", lSubpFlow);

//                lShowFlow.showDDefUse(lSubpFlow);
//                lShowFlow.showDUseDef(lSubpFlow);
                lShowFlow.showPDefUse(lSubpFlow);
                lShowFlow.showPUseDef(lSubpFlow);
            }
        }
    }

    /**
     * Returns true if the given node is thought of as executable, giving grounds to what would be returned by getNextExecutableNode() in BBlockNodeIterator.
     */
    public static boolean isExecutable(IR pIR) {
        if (pIR instanceof HIR) {
            switch (pIR.getOperator()) {
            case HIR.OP_PROG:
            case HIR.OP_SUBP_DEF:
            case HIR.OP_LABEL_DEF:
            case HIR.OP_INF:
            case HIR.OP_SUBP:
            case HIR.OP_TYPE:
            case HIR.OP_LABEL:
            case HIR.OP_LIST:
            case HIR.OP_SEQ:
            case HIR.OP_ENCLOSE:
            case HIR.OP_LABELED_STMT:
            case HIR.OP_IF:
            case HIR.OP_WHILE:
            case HIR.OP_FOR:
            case HIR.OP_UNTIL:
            case HIR.OP_INDEXED_LOOP:
            case HIR.OP_JUMP: //##9
            case HIR.OP_SWITCH:
            case HIR.OP_BLOCK:
            case HIR.OP_EXP_STMT:
            case HIR.OP_NULL:
                return false;

            case HIR.OP_CONST:
            case HIR.OP_SYM:
            case HIR.OP_VAR:
            case HIR.OP_PARAM:
            case HIR.OP_ELEM:
            case HIR.OP_SUBS:
            case HIR.OP_INDEX:
            case HIR.OP_QUAL:
            case HIR.OP_ARROW:
            case HIR.OP_ASSIGN:
            case HIR.OP_CALL:
            case HIR.OP_RETURN:
            case HIR.OP_ADD:
            case HIR.OP_SUB:
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
            case HIR.OP_NOT:
            case HIR.OP_NEG:
            case HIR.OP_ADDR:
            case HIR.OP_CONV:
            case HIR.OP_DECAY:
            case HIR.OP_UNDECAY: //##10
            case HIR.OP_CONTENTS:
            case HIR.OP_SIZEOF:
            case HIR.OP_SETDATA:
            case HIR.OP_PHI:
            case HIR.OP_OFFSET:
            case HIR.OP_LG_AND:
            case HIR.OP_LG_OR:
            case HIR.OP_SELECT:
            case HIR.OP_COMMA:
            case HIR.OP_PRE_INCR:
            case HIR.OP_PRE_DECR:
            case HIR.OP_POST_INCR:
            case HIR.OP_POST_DECR:
            case HIR.OP_ADD_ASSIGN:
            case HIR.OP_SUB_ASSIGN:
            case HIR.OP_MULT_ASSIGN:
            case HIR.OP_DIV_ASSIGN:
            case HIR.OP_MOD_ASSIGN:
            case HIR.OP_SHIFT_L_ASSIGN:
            case HIR.OP_SHIFT_R_ASSIGN:
            case HIR.OP_AND_ASSIGN:
            case HIR.OP_OR_ASSIGN:
            case HIR.OP_XOR_ASSIGN:
                return true;

            default:
                throw new FlowError();
            }
        } else {
            // 2004.05.31 S.Noishi
            throw new FlowError();

            /*
            LIRType lLIRType = ((LIRNode) pIR).getLIRType();

            if ((lLIRType == LIRType.OPERATOR) ||
                    (lLIRType == LIRType.CONDITION) ||
                    (lLIRType == LIRType.JUMP) ||
                    (lLIRType == LIRType.REGISTER) ||
                    (lLIRType == LIRType.CONST) ||
                    (lLIRType == LIRType.VARIABLE) ||
                    (lLIRType == LIRType.MEMORY) || (lLIRType == LIRType.CALL) ||
                    (lLIRType == LIRType.LISTD) ||
                    (lLIRType == LIRType.SUBREG) || (lLIRType == LIRType.USE) ||
                    (lLIRType == LIRType.CLOBBER) ||
                    (lLIRType == LIRType.ASMCONST)) {
                return true;
            } else if ((lLIRType == LIRType.LABEL) ||
                    (lLIRType == lLIRType.LABELDEF) ||
                    (lLIRType == LIRType.LISTA) ||
                    (lLIRType == LIRType.PARALLEL) ||
                    (lLIRType == LIRType.PROLOGUE) ||
                    (lLIRType == LIRType.EPILOGUE) ||
                    (lLIRType == LIRType.PHI) ||
                    (lLIRType == LIRType.LINEINFO)) {
                return false;
            } else {
                throw new FlowError();
            }
            */
        }
    }

    /**
     * Checks if the tree structure under the given HIR node is valid.
     */
    public static boolean checkTreeStructure(HIR pHIR, IoRoot pIoRoot) {
        HIR lChild;
        HIR lHir;
        HIR lParent;
        Stmt lStmt;
        boolean lVerified;

        //                System.out.println(pHIR);
        int lOpCode = pHIR.getOperator();

        //            for (HirIterator lIt = FlowUtil.hirIterator(pHIR); lIt.hasNext();)
        if (lOpCode == HIR.OP_BLOCK) {
            for (lStmt = ((BlockStmt) pHIR).getFirstStmt(); lStmt != null;
                    lStmt = lStmt.getNextStmt())
                if (!checkTreeStructure(lStmt, pIoRoot)) {
                    return false;
                }
        } else if (lOpCode == HIR.OP_LIST) {
            for (Iterator lIt = ((HirList) pHIR).iterator(); lIt.hasNext();) {
                lHir = (HIR) lIt.next();

                if (((lParent = (HIR) lHir.getParent()) != null) &&
                        (lParent != pHIR)) {
                    lVerified = false;

                    for (int i = 1; i <= lParent.getChildCount(); i++)
                        if (lParent.getChild(i) == lHir) {
                            lVerified = true;
                        }

                    if (!lVerified) {
                        pIoRoot.msgRecovered.put(5555,
                            "Flow: HIR tree check for (" + lHir +
                            ") failed. (" + lHir + ")'s parent (" + lParent +
                            ") does not have (" + lHir + ") as its child.");

                        return false;
                    }
                }

                checkTreeStructure(lHir, pIoRoot);
            }
        } else {
            for (int i = 1; i <= pHIR.getChildCount(); i++) {
                lChild = (HIR) pHIR.getChild(i);

                if (lChild != null) {
                    if (lChild.getParent() != pHIR) {
                        pIoRoot.msgRecovered.put(5555,
                            "Flow: HIR tree check for (" + pHIR +
                            ") failed. (" + pHIR + ")'s " + i + "th child (" +
                            lChild + ")'s parent is (" + lChild.getParent() +
                            ")");

                        return false;
                    }

                    if (!checkTreeStructure(lChild, pIoRoot)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    //##52 void dbg(int level, String pHeader, Object pObject)
    public void dbg(int level, String pHeader, Object pObject) //##52
    {  //##52
        ioRoot.dbgFlow.printObject(level, pHeader, pObject);
        ioRoot.dbgFlow.println(level);
    }

    public void dbg(int level, Object pObject) //##52
      {
        if (pObject == null)
          ioRoot.dbgFlow.print(level, " null ");
       else
          ioRoot.dbgFlow.print(level, " " + pObject.toString());
    }

   //##25 void message(int level, String mes) {
   public void message(int level, String mes) //##52
    {
        ioRoot.dbgFlow.print(level, "", mes);
    }

    /**
     *  output warning message.
     * @param  mes  warning message
     */
    void warning(String mes) {
        ioRoot.msgWarning.put(5555, ioRoot.getSourceFile() + ": " + mes);
        message(6, "WARNING: " + ioRoot.getSourceFile() + ": " + mes);
        ioRoot.getCompileSpecification().getWarning().warning(5555, "Flow",
            ioRoot.getSourceFile() + ": " + mes);
    }

    public void test() {
        FlowResults.putRegClasses(new RegisterFlowAnalClasses());

        IrList lSubpDefList = ((Program) hirRoot.programRoot).getSubpDefinitionList();
        Iterator subpDefIterator = lSubpDefList.iterator();
        int lReturnValue = 0;

        while (subpDefIterator.hasNext()) {
            SubpDefinition subpDef = (SubpDefinition) (subpDefIterator.next());

            FlowResults lResults = flow.results();
            ShowFlow lShowFlow = new ShowFlow(lResults);
            SubpFlow lSubpFlow = flow.subpFlow(subpDef, lResults);

            lSubpFlow.controlFlowAnal();

            if (flowRoot.getFlowAnalOption(FlowRoot.OPTION_DEF_KILL)) {
                lSubpFlow.findDExposedUsed();

                //       lSubpFlow.findMod();
                //       lSubpFlow.findReach();
            }

//            System.out.println(lResults.fAnalDependenceGraph);
//            System.out.println();
//            System.out.println();
//            System.out.println(lResults.fAnalDependenceGraph.minimals());
//
////            for (Iterator lIt = lResults.fAnalDependenceGraph.minimals()
////                                                             .iterator();
//                    lIt.hasNext();) {
//                lResults.invalidate((List) lIt.next());
//            }

            System.out.println();
            System.out.println();
            System.out.println(lResults);
        }
    }


        // Factory methods

        public AssignFlowExpId assigner(SubpFlow pSubpFlow)
        {
                if (pSubpFlow instanceof HirSubpFlow)
                        return new AssignHashBasedFlowExpIdHir((HirSubpFlow)pSubpFlow);
                else
                        // return new AssignHashBasedFlowExpIdLir((LirSubpFlow)pSubpFlow);
                        throw new FlowError();   // 2004.05.31 S.Noishi
        }

        public BBlockHir bblock(LabeledStmt pLabeledStmt, HirSubpFlow pHirSubpFlow)
        {
                return new BBlockHirImpl(pLabeledStmt, pHirSubpFlow);
        }

        /* 2004.05.31 S.Noishi
        public BBlockLir bblock(coins.ir.lir.LabelDef pLabelDef, LirSubpFlow pLirSubpFlow)
        {
                return new BBlockLirImpl(pLabelDef, pLirSubpFlow);
        }
        */
        /* 2004.05.31 S.Noishi
        public BBlockLir bblock(Prologue pPrologue, LirSubpFlow pLirSubpFlow)
        {
                return new BBlockLirImpl(pPrologue, pLirSubpFlow);
        }
        */

        public BBlockSubtreeIterator bblockSubtreeIterator(BBlock pBBlock)
        {
                if (pBBlock instanceof BBlockHir)
                        return new BBlockStmtIterator((BBlockHir)pBBlock);
                else
                        // return new BBlockLirSubtreeIteratorImpl((BBlockLir)pBBlock);
                        throw new FlowError();   // 2004.05.31 S.Noishi
        }

        public BBlockVector bblockVector(SubpFlow pSubpFlow)
        {
                return new BBlockVectorImpl(pSubpFlow);
        }

        public DefUseCell defUseCell(IR pDefNode)
        {
                return new DefUseCellImpl(flowRoot, pDefNode);
        }

    public DefUseList defUseList() {
        return new DefUseListImpl(flowRoot);
    }

    public DefVector defVector(SubpFlow pSubpFlow) {
        return new DefVectorImpl(pSubpFlow);
    }


        public Edge edge(BBlock pFrom, BBlock pTo)
        {
                return new EdgeImpl(flowRoot, pFrom, pTo);
        }

        public ExpVector expVector(SubpFlow pSubpFlow)
        {
                return new ExpVectorImpl(pSubpFlow);
        }

        public FlowAnalSymVector flowAnalSymVector(SubpFlow pSubpFlow)
        {
                return new FlowAnalSymVectorImpl(pSubpFlow);
        }

//	public LoopInf loopInf(IR pEntryNode)
//	{
//		return new LoopInfImpl(flowRoot, pEntryNode);
//	}

        public PointVector pointVector(SubpFlow pSubpFlow)
        {
                return new PointVectorImpl(pSubpFlow);
        }

        public FlowResults results() {
        return new FlowResults(flowRoot);
    }

        public SetRefRepr setRefRepr(Stmt pStmt, BBlockHir pBBlockHir)
        {
          //##25    return new SetRefReprHirImpl(pStmt, pBBlockHir);
          return new SetRefReprHirEImpl(pStmt, pBBlockHir, false, null); //##25
        }

        /* 2004.05.31 S.Noishi
        public SetRefRepr setRefRepr(LIRNode pLIRNode, BBlockLir pBBlockLir)
        {
                return new SetRefReprLirImpl(pLIRNode, pBBlockLir);
        }
        */

    public SetRefReprList setRefReprList(BBlock pBBlock)
        {
                if (pBBlock instanceof BBlockHir)
                        return new SetRefReprListHir((BBlockHir)pBBlock);
                else
                        // return new SetRefReprListLir((BBlockLir)pBBlock);
                        throw new FlowError();   // 2004.05.31 S.Noishi
        }

        public SubpFlow subpFlow(SubpDefinition pSubpDef,
        FlowResults pResults) {
        if (pResults.flowRoot.isHirAnalysis()) {
            return new HirSubpFlowImpl(pSubpDef, pResults);
        } else {
            // return new LirSubpFlowImpl(pSubpDef, pResults);
            throw new FlowError();   // 2004.05.31 S.Noishi
        }
    }

        public UDChain udChain(IR pUseNode)
        {
                return new UDChainImpl(flowRoot, pUseNode);
        }

    public UDList udList() {
        return new UDListImpl(flowRoot);
    }

public SubpFlow getSubpFlow() //##51
{
  return fSubpFlow;
}

public void setSubpFlow( SubpFlow pSubpFlow )  //##51
{
  fSubpFlow = pSubpFlow;
}

}
