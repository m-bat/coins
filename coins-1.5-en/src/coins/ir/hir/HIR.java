/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import coins.ir.IR;
import coins.ir.IrList;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.ExpId;
import coins.sym.FlagBox;
import coins.sym.FlowAnalSym;
import coins.sym.Label;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;

//========================================//
//                          //##54: Moved to HIR0 on Nov. 2004
//                          (##39):Revised on May 2004.
//                          (##21):Revised on Jun. 2003.
//                          (##20):Revised on Feb., Mar. 2003.
//                          (##19):Revised on Dec. 2002.
//                          (##14):Revised on Jun. 2002.
//                          (##12):Revised on Mar. 2002.
//                          (##10):Revised on Nov., Dec. 2001.
//                          (##9): Revised on Oct. 2001.

/** HIR interface
<PRE>
 * High Level Intermediate Representation (HIR) interface.
 * All HIR classes implement all methods declared in HIR interface
 * and IR interface which is inherited by the HIR interface.
 * See IR, Stmt, Exp, Sym interfaces and HirRoot class.
 *
 * Preliminaries
 *   High level intermediate representation is composed of operation
 *   information in a tree form, symbol information, data flow
 *   information, and others.
 *   The operation information tree is composed of nodes and edges.
 *   A node with children is called as nonleaf node and a node without
 *   child is called a leaf node.
 *   The term "node" means either leaf node or nonleaf node,
 *   the term "subtree" means either pure subtree composed of
 *   several nonleafs and leafs, or one leaf.
 *
 * Class/interface hierarchy:
 *   (Root is a class, others are interfaces.
 *    An interface xxx is implemented by corresponding class
 *    named xxxImpl or xxx_Impl.)
 *
 * Root
 * |
 * |- Sym
 * |   |- ...
 * |
 * |- Flow
 * |   |- ...
 * |
 * |- IR
 *     |- IR_factory    // IR object creation factory.
 *     |- IrList        // List of indefinite number of objects.
 *     |   |- HirList   // IrList whose elements are HIR objects.
 *     |
 *     |- LIR           // Low level Intermediate Representation
 *     |   |- ...
 *     |
 *     |- HIR0 // Simple HIR interface.
 *        |
 *        HIR // High level Intermediate Representation.
 *         |  // Usual operations on HIR are done by using HIR methods.
 *         |
 *         |- Program         // Program definition node.
 *         |- SubpDefinition  // Subprogram definition node.
 *         |- HirSeq          // Sequence of definite number of
 *         |                  // HIR objects.
 *         |- HirList         // IrList whose elements are HIR objects.
 *         |                  // (Note that elements of IrList may be Sym
 *         |                  //  and String but elements of HirList should
 *         |                  //  be HIR objects.)
 *         |                  // (Multi-inheritance of interface)
 *         |- Stmt            // Statement
 *         |   |- LabeledStmt // Labeled statement.
 *         |   |- AssignStmt  // Assignment statement.
 *         |   |- IfStmt      // If-statement.
 *         |   |- JumpStmt    // Jump (goto) statement.
 *         |   |              //  (Jump unconditionally)
 *         |   |- LoopStmt    // Loop statement.
 *         |   |   |- ForLoop     // For-loop.
 *         |   |   |- WhileLoop   // While-loop.
 *         |   |   |- RepeatLoop  // Repeat-while-true loop.
 *         |   |   |- IndexedLoop // Loop with index range
 *         |   |                  // (such as Fortran DO loop).
 *         |   |- ReturnStmt   // Return statement.
 *         |   |- SwitchStmt   // Switch (case) statement.
 *         |   |- BlockStmt    // Block representing a sequence
 *         |   |               //   of statements.
 *         |   |- ExpStmt      // Expression treated as a statement.
 *         |   |               // Call statement is treated as an
 *         |   |               // expression statement of function call.
 *         |   |               // Loop start condition expression has
 *         |   |               // label and treated as ExpStmt.
 *         |   |- InfStmt      // An information node
 *         |   |               // which can be treated as a statement.
 *         |   |               //  (pragma, comment line, etc.)
 *         |   |- AsmStmt      // Asm statement representing sequence of
 *         |                   // assembly language instructions.  //##70
 *         |- LabelDef         // Label definition node.
 * //      |- InfNode          // IR information node.         DELETED
 * //      |                   //  (pragma, comment line, etc.)
 *         |- Exp  // Expression
 *             |- ConstNode // Constant node
 *             |- SymNode   // Symbol node
 *             |   |- VarNode   // Variable name node.
 * //          |   |   |- ParamNode // formal parameter name node
 * //          |   |   |             DELETED
 *             |   |   |- ElemNode  // struct/union element name node
 *             |   |                 not DELETED
 *             |   |- SubpNode  // Subprogram name node.
 *             |   |- LabelNode // Label reference node.
 *             |   |- TypeNode  // Type name node.
 *             |
 *             |- SubscriptedExp // Subscripted variable.
 *             |- PointedExp     // Pointed object.
 *             |- QualifiedExp   // Qualified variable.
 *             |- FunctionExp    // Function call expression.
 *             |- PhiExp     // Phi function used in SSA
 *             |- ExpListExp // Expression representing a list of expressions
 *             |- NullNode   // Null (no-operation) node
 *
 *  Deleted classes:
 *    ParamNode,
 *    StmtBody, AssignExp,
 *    Nonleaf, Leaf, HIRdetail, BBlock, PBlock, ExitStmt,
 *    ContinueStmt. (##10)
 *
 * Abstract syntax of HIR:
 *
 *  Abstract syntax of HIR is shown as BNF syntax
 *  in the form ( operationCode attr child1 child2 ... ),
 *  where, child1 represents source operand 1, child2 represents
 *  source operand 2,  ... .  Instance of HIR may be regarded
 *  as a tree attached with operands as its subtree.
 *  The result operand is represented by the root node
 *  of the tree. The attr represents attributes
 *  such as type of the tree, etc.
 *  (Each node may be attached an expression identifier (expId)
 *  as an attribute. In such case, the expId attached to
 *  the root node of a subtree may be regarded as the identifier
 *  corresponding to the result value of the operation
 *  specified by the subtree.)
 *  Each child is also a subtree of the same form.
 *  Some child may be null.
 *  The operationCode and attr are not subtree but
 *  information attached to the root node of the subtree.
 *  (This notation is not explaining parse tree of HIR but
 *  explaining abstract syntax tree of HIR.)
 *
 *  Notations in the following BNF descriptions..
 *    identifier starting with lower case letter except attr: terminal
 *    identifier starting with upper case letter:  nonterminal
 *      identifier ending with "_":
 *             nonterminal that is not an interface
 *      identifier that does not end with "_":
 *             nonterminal representing an interface
 *    xxxCode: operation code xxx
 *    xxxSym : symbol recorded in the symbol table
 *            progSym : program name symbol
 *            subpSym : subprogram name symbol
 *            varSym  : variable name symbol
 *                      (including array/struct/union name symbol)
 *            paramSym: formal parameter name symbol
 *            elemSym : struct/union element name symbol
 *            labelSym: label name symbol
 *            typeSym : type name symbol
 *    xxxConst: constant recorded in the symbol table
 *            intConst   : integer constant
 *                         (int/short/long/unsigned int/ ...)
 *            floatConst : floating constant (float/double/ ...)
 *            charConst  : character constant (signed/unsigned)
 *            stringConst: character string constant
 *            boolConst  : boolean constant
 *                      (true is 1, false is 0 if converted to  integer)
 *    intConstValue:    integer constant
 *                      (not a symbol table entry but value itself).
 *    stringConstValue: string  constant
 *                      (not a symbol table entry but value itself).
 *    null       : empty (nothing, epsilon)
 *    NullNode   : a leaf node with operation code null.
 *    List_of_xxx: java.util.List with elements xxxx. (##10)
 *    Parenthesis is not a syntactic component but a delimiter.
 *      One subtree begins with left parenthesis and end with
 *      right parenthesis.
 *
 *  Note:
 *    Following items are not represented as a subtree
 *    dangling to a node but are represented as information
 *    attached to the node. They are not traversed when HIR tree
 *    is traversed but can be get by applying corresponding HIR
 *    methods to the node.
 *        Operation code     (xxxCode)
 *        Symbol table entry (xxxSym)
 *        Constant symbol    (xxxConst)
 *        Symbol table       (SymTable, GlobalSymTable_, LocalSymTable_)
 *        attr               (attribute such as node type which is
 *                            a symbol representing the type
 *                            of the result of HIR subtree.)
 *    Items represented as a subtree is indicated by @ to clarify.
 *    (@ is not a syntactic component but a postfix marker
 *     to avoid misunderstanding.)
 *
 *  Program   ->             // Program represents a compile unit.
 *     ( progCode attr       //
 *       GlobalSymTable_     // Global symbols of the program
 *       ProgSym_ @          // Program name (may be null).
 *       InitiationPart_ @   // Initial value specification.
 *       SubpList_ @ )       // Subprogram definition list.
 *  ProgSym_  ->
 *     ( symCode attr progSym )   // Program name symbol.
 *   | null
 *  GlobalSymTable_  ->   // Global symbol table of the program.
 *     SymTable           // As for SymTable, see Sym and SymTable.
 *   | null
 *  InitiationPart_ ->    // Initial value specification (##14)
 *     InitiationBlock_
 *   | NullNode
 *  InitiationBlock_ ->   // Block containing initiation statements.
 *     ( blockCode attr
 *       InitiationStmtSeq_ @ )
 *  InitiationStmtSeq_ -> // Sequence of initiation statements
 *     InitiationStmt_ @ InitiationStmtSeq_ @
 *   | null
 *  InitiationStmt_ ->    // Initiation statement
 *     ( setDataCode attr         // with Exp_l_: l-value
 *        Exp_l_ @ ValueSpec_ @ ) // ValueSpec_: constant Exp.
 *   | InfStmt                    //##74
 *   | AssignStmt
 *  ValueSpec_ ->
 *      ConstExp_         // Constant expression
 *   | ( explistCode attr // ExpListExp of ValueSpec_
 *       List_of_ValueSpec_ @ )
 *   | ( exprepeatCode attr        // Expression to represent repeating
 *       ValueSpec_ @ intConst @ ) //   ValueSpec_ intConst-times.
 *  ConstExp_ ->           BEGIN
 *     ConstNode          // Constant value
 *   | Exp                // Exp whose operands are all ConstNode
 *  SubpList_ ->
 *     ( listCode attr     // Subprogram definition list
 *       List_of_SubpDefinition @ )
 *  SubpDefinition ->      // Subprogram definition
 *     ( subpDefCode attr
 *       LocalSymTable_    // SymTable local in the subprogram.
 *       SubpNode @        // Subprogram name.
 *       InitiationPart_ @ // Subprogram initiation.
 *       SubpBody_ @ )     // HIR subprogram body
 *                         // (Control flow should be ended by return).
 *  //   LIR @ )      // LIR representation for the subprogram.  deleted
 *  SubpNode  ->      // Subprogram name node.
 *     ( symCode attr
 *       subpSym )
 *  LocalSymTable_ -> // Local symbol table (for subprogram, block, etc.)
 *     SymTable
 *   | null
 *  SubpBody_ ->      // HIR subprogram body is
 *     BlockStmt                 // block statement or
 *   | ( labeledStmtCode attr    // BlockStmt with Label.
 *        LabelDefinitionList_ @ // List of label definitions.
 *        BlockStmt @ )          // Statement body
 *     // BlockStmt of SubpBody should have LocalSymTable. //##70
 *  *  BlockStmt ->
 *     ( blockCode attr
 *       LocalSymTable_  // Define symbols local in the block.
 *       StmtSeq_ @ )    // Block makes a sequence of statements
 *                       // to be treated as one statement.
 *            // Statements in StmtSeq_ can be get
 *            // by getFirstStmt() and successive getNextStmt().
 *  StmtSeq_  ->
 *     Stmt @ StmtSeq_ @  // Statement sequence is a statement
 *   | null               // followed by statements or empty.
 *  Stmt      ->          // Statement is
 *     LabeledStmt        // a statement with label definitions or
 *   | StmtBody_          // a statement without label definition.
 *  LabeledStmt ->        // Statement with label definition.
 *     ( labeledStmtCode attr
 *        LabelDefinitionList_ @ // List of label definitions.
 *        StmtBody_ @ )          // Statement body (statement that is
 *                               // peeled label off).
 *  LabelDefinitionList_ ->   // List of LabelDef nodes.
 *     ( listCode attr
 *       List_of_LabelDef @ )
 *  LabelDef   ->             // Label definition node.
 *     ( labelDefCode attr
 *       labelSym )
 *  StmtBody_  ->     // Statement body.
 *     AssignStmt     // Assignment statement.
 *   | IfStmt         // If statement.
 *   | JumpStmt       // Jump (goto) statement.
 *   | LoopStmt       // Loop statement.
 *   | CallStmt_      // Subprogram call statement.
 *   | ReturnStmt     // Return statement.
 *   | SwitchStmt     // Switch (case selection) statement.
 *   | BlockStmt      // Block statement.
 *   | ExpStmt        // Expression treated as a statement.
 *   | InfStmt        // Inf (information) statement        //##70
 *                    // #Pragma is represented as a kind of
 *                    // InfStmt.
 *   | AsmStmt        // Asm (assemblyu language) statement //##70
 *   | SetDataStmt    // Set initial data statement.
 *
 *  AssignStmt ->     // Assign statement.
 *     ( assignCode attr
 *       Exp_l_ @     // l_value expression.
 *       Exp @ )      // Expression whose value is to be assigned.
 *                    // Exp_l_ and Exp should have the same type.
 *                    // Exp may be any scalar expression
 *                    // or struct/union expression.
 *                    // (Array expression will be permitted in future.)
 *   | ( CompoundAssignOperator_ attr  // HIR-C only
 *       Exp_l_ @
 *       Exp @ )
 *   | ( IncrDecrOperator_ attr        // HIR-C only
 *       Exp_l_ @ )
 *  Exp_l_  ->              // l-value expression is
 *     Exp                  // an expression representing an object
 *                          // (memory area to contain data).
 *  IfStmt    ->            // If statement
 *    ( ifCode attr
 *       ConditionalExp_ @  // Conditional expression.
 *       ThenPart_ @        // Then-part
 *       ElsePart_ @        // Else-part.
 *       LabeledStmt0_ @ )  // Statement with end-of-if label.
 *  ThenPart  ->
 *     LabeledStmt   // Executed if ConditionalExp is true.
 *   | null          //
 *  ElsePart  ->
 *     LabeledStmt   // Executed if ConditionalExp is false.
 *   | null          //
 *  LabeledStmt0_    ->         // LabeledStmt0_ is a labeled
 *     ( labeledStmtCode attr   // statement whose statement body
 *       LabelDefinitionList_ @ // may be null at first but it may become
 *       NullOrStmt_ @ )        // non-null by code optimization, etc.
 *                              // LabeledStmt0_ may be called labeled null.
 *  JumpStmt  ->
 *     ( jumpCode attr  // Jump to the statement with
 *       LabelNode @ )  // specified label.
 *  LoopStmt ->  // Loop statement is either for-loop, while-loop,
 *               // repeat-loop, indexed-loop, or other general loop.
 *               // All of them are implemented as a general loop
 *               // with some restriction depending on the loop type.
 *               // Compiler components (other than front part) should
 *               // treat general loop, that is, do not assume some child
 *               // is null without checking whether the child is null
 *               // or not. For example, a while-loop may be changed
 *               // to a loop with LoopInitPart_ and LoopStepPart_
 *               // by code optimizer. isSimpleForLoop(), isSimpleWhileLoop(),
 *               // isSimpleRepeatLoop() of LoopStmt interface check
 *               // whether the loop can be treated pure for-loop,
 *               // pure while-loop, etc.
 *               // There may be some cases where processing become
 *               // simple if the loop is either simple for-loop,
 *               // while-loop, repeat-loop, etc.
 *    ( LoopCode_ attr     // Loop kind code.
 *      LoopInitPart_ @    // Loop initiation part to be executed
 *                         // before repetition. It may be null.
 *                         // LoopInitPart_ should not contain
 *                         // control statements except for the one
 *                         // generated by addToConditionalInitPart
 *                         // of LoopStmt interface.
 *                         // As for expressions to be executed
 *                         // only once (loop invariant expressions, etc.),
 *                         // see addToConditionalInitPart of LoopStmt.
 *      StartConditionPart_ @  // Loop start conditional expression
 *                         // with loopBackLabel.
 *                         // If true, pass through to LoopBody_,
 *                         // otherwise transfer to LoopEndPart_
 *                         // to terminate the loop execution.
 *                         // If loop start condition part is null,
 *                         //  pass through to LoopBody_.
 *      LoopBody_ @        // Loop body repetitively executed.
 *                         // Pass through to EndCondition_.
 *                         // It is a block statement (BlockStmt)
 *                         // with loop start label and the blcok
 *                         // statement contains a labeled statement
 *                         // with loopStepLabel as its last statement.
 *                         // This should not be null but the block may
 *                         // have no executable statement and contains
 *                         // only a labeled statement with loopStepLabel.
 *                         // "continue" jumps to the loopStepLabel.
 *                         // The loopStepLabel may be omitted if
 *                         // there is no "jump loopStepLabel".
 *     EndCondition_ @     // Loop end condition expression.
 *                         // If false, transfer to LoopEndPart_
 *                         // to terminate the loop execution,
 *                         // otherwise pass through to
 *                         // LoopStepPart_.
 *     LoopStepPart_ @     // Loop step part that is to be executed
 *                         // before jumping to loopBackLabel.
 *                         // LoopStepPart_ should not contain
 *                         // control statements.
 *     LoopEndPart_ @ )    // Loop end part
 *                         // with loopEndLabel.
 *                         // "exit" (break in C) jumps to here.
 *     IndexedLoop_ attr   // Attributes for IndexedLoop.
 *                         // Not given for other loops.
 *  LoopCode_ attr ->
 *     whileCode attr      // while-loop
 *   | forCode attr        // for-loop
 *   | repeatCode attr     // repeat-while-true--loop
 *   | indexedLoopCode attr// indexed-loop
 *   | loopCode attr       // general loop other than above loops.
 *  LoopInitPart_   ->     // Loop initiation part.
 *     Stmt
 *   | null
 *  ConditionalInitPart_ ->  // This item is deleted. Give null for this item
 *     null                  // but use addToConditionalInitPart method
 *                           // of LoopStmt to move loop invariant expressions
 *                           // etc. from loop body so that they are executed
 *                           // only once.
 *  StartConditionPart_ ->      // Show start condition with
 *     ( labeledStmtCode attr   // loopBacklabel.
 *       LabelDefinitionList_ @
 *       BooleanExpStmtOrNull_ @ ) // loopStartConditionExpression.
 *  LoopBody_  ->               // Block statement with loopBodyLabel.
 *     ( labeledStmtCode attr   // The last statement of the block
 *       LabelDefinitionList_ @ // is a LabeledStmt0_ statement with
 *       BlockStmt_ @ )         // loopStepLabel.
 *  EndCondition_ ->            // ExpStmt showing loop end condition.
 *     BooleanExpStmtOrNull_
 *  LoopStepPart_  ->    // Statement to be executed before jumping
 *     Stmt              // to loopBackLabel.
 *   | null              // LoopStepPart_ should not contain
 *                       // statements that change control flow.
 *  LoopEndPart_  ->     // LabeledStmt0_ statement with loopEndLabel.
 *     LabeledStmt0_
 *  NullOrStmt_ ->       // Usually null but it may be
 *     null              // a statement (created during
 *   | Stmt              // HIR transformation).
 *  BooleanExpStmtOrNull_ ->    // Boolean expression statement or null.
 *     ExpStmt           // ExpStmt whose Exp part is a boolean expression.
 *   | null
 *  IndexedLoop_ attr  -> // Attributes for IndexedLoop.
 *     loopIndex attr     // Loop index (induction variable).
 *                        // See getLoopIndex().
 *     startValue attr    // Start value of the loop index.
 *                        // See getStartValue().
 *     endValue attr      // End value of the loop index.
 *                        // See getEndValue().
 *     stepValue attr     // Step value for the loop index.
 *                        // See getStepValue().
 *
 *  // Note. LoopInf may contain goto-loop that is difficult or
 *  //   impossible to be represent by above LoopStmt.
 *  //   (goto-loop is not implemented by LoopStmt.)
 *
 *  // LoopStmt is executed as follows:
 *  //   LoopInitPart_;
 *  //   if (loopStartConditionExpression == null) {
 *  //     Sequence of statements added by addToConditionalInitPart();
 *  //   }else {
 *  //     if (loopStartConditionExpression == false) {
 *  //       jump to loopEndLabel;
 *  //     }else { // ConditionalInitBlock
 *  //       Sequence of statements added by addToConditionalInitPart().
 *  //       jump to loopBodyLabel;
 *  //     }
 *  //   }
 *  //   loopBackLabel:
 *  //     if ((loopStartConditionExpression != null)&&
 *  //         (loopStartConditionExpression == false))
 *  //       jump to loopEndLabel;
 *  //   loopBodyLabel:
 *  //     Start of BlockStmt of LoopBody_
 *  //       Stastement sequence of the BlockStmt;
 *  //       (break statement jumps to loopEndLabel;)
 *  //       (continue statement jumps to loopStepLabel;)
 *  //       Rest of stastement sequence of the LoopBody_;
 *  //       loopStepLabel:;
 *  //     End of BlockStmt of LoopBody_
 *  //     if ((loopEndConditionExpression != null)&&
 *  //         (loopEndConditionExpression == false))
 *  //       jump to loopEndLabel;
 *  //     LoopStepPart;
 *  //     jump to loopBackLabel;
 *  //   loopEndLabel:
 *  //     Loop end part;
 *
 *  // BEGIN #21
 *  // ForStmt is created as a general loop where contents of
 *  //   ConditionalInitPart_, EndCondition_, LoopEndPart_
 *  //   are labeled null at first (but their statement body may
 *  //   become not null by some optimizing transformation).
 *  //   See isSimpleForLoop().
 *  // WhileStmt is created as a general loop where contents of
 *  //   LoopInitPart_, ConditionalInitPart_, EndCondition_,
 *  //   LoopStepPart_, LoopEndPart_
 *  //   are labeled null at first (but their statement body may
 *  //   become not null by some optimizing transformation).
 *  //   See isSimpleWhileLoop().
 *  // RepeatStmt is created as a general loop where contents of
 *  //   LoopInitPart, ConditionalInitPart_, StartCondition_,
 *  //   LoopStepPart_, LoopEndPart_
 *  //   are labeled null at first (but their statement body may
 *  //   become not null by some optimizing transformation).
 *  //   See isSimpleUntilLoop().
 *  // IndexedLoopStmt is created as a general loop where contents of
 *  //   ConditionalInitPart_, EndCondition_, LoopEndPart_
 *  //   are labeled null at first (but their statement body may
 *  //   become not null by some optimizing transformation).
 *  //   See isSimpleIndexedLoop().
 *  // IndexedLoopStmt represents a Fortran type loop where
 *  //   value of loop index is incremented or decremented by loop
 *  //   step value starting from loop start value and stops
 *  //   to loop before crossing the barrier of loop end value.
 *  //   (See IndexedLoopStmt interface.)
 *  // Indexed loop attributes (IndexedLoopAttr_) are available
 *  // only for IndexedLoopStmt.
 *  // END #21
 *
 *  CallStmt_   ->         // Subprogram call statement.
 *     ( expStmtCode attr  // Expression statement
 *       FunctionExp @ )   // with FunctionExp.
 *  FunctionExp ->         // Subprogram call expression.
 *     ( callCode attr
 *       Exp @             // Expression specifying subprogram
 *                         // to be called. It may be
 *                         // SubpNode or (addr SubpNode), etc.
 *       HirList @ )       // Actual parameter list.
 *  ReturnStmt ->          // Return statement.
 *     ( returnCode attr
 *       ReturnValue_ @ )  // Return value.
 *  ReturnValue_ ->
 *     Exp
 *   | null
 *  SwitchStmt ->          // Switch statement.
 *     ( switchCode attr
 *       Exp @             // Case selection expression.
 *       JumpTable_ @      // List of constants and statement labels.
 *       Stmt @            // Collection of statements to be selected.
 *       LabeledStmt0_ @ )  // Indicate end of case statement.
 *  JumpTable_ ->          // Jump table.
 *     ( seqCode attr
 *       JumpList_ @       // List of constant-label pairs.
 *       LabelNode @ )     // Default label.
 *  JumpList_ ->                 // Jump list.
 *     ( listCode attr           // Correlate Exp value
 *       List_of_SwitchCase @ )  // and list of SwitchCase_ pairs.
 *  SwitchCase_ ->       // List of SwitchCase_ pairs.
 *     ( seqCode attr
 *       ConstNode @     // Correlate Exp value and
 *       LabelNode @ )   // switch statement label.
 *  ExpStmt   ->         // Expression statement.
 *     ( expStmtCode attr
 *       Exp @ )         // Expression treated as a Stmt.
 *  NullNode  ->
 *     ( nullCode attr ) // NullNode is a terminal
 *                       // that is not null.
 *  InfStmt   ->
 *     ( infCode attr    // Information statement.
 *       InfKind_        // Information kind identifier.
 *       InfObject_  )   // Information.
 *                       // (InfKind_ and InfObject_ are not
 *                       // traversed by HIR traverse operations.)
 *  InfKind_  ->
 *     StringConstNode_ // String constant node showing the kind of
 *                      // information such as pragma, comment, etc. //##70
 *  InfObject_  ->      // Information.
 *     Object           // Object such as Sym, Exp, etc. or
 *                      // a list of Objects. It may or may not be HIR.
 *  StringConstNode_  ->            //##70
 *    (constCode attr stringConst ) // String constant node.  //##70
 *  Pragma_   ->   // Pragma is a direction to the compiler.
 *     ( infCode attr    // Pragma is represented as
 *                       // an information statement.
 *       InfKind_        // Pragma information kind identifier.
 *       InfObject_  )   // Pragma information.
 *  AsmStmt   ->
 *    ( asmCode attr  // Asm statement.
 *      StringConst @ // String constant representing
 *                    // parameter description pragma,
 *                    // clobber specification pragma, and
 *                    // assembly language instruction sequence
 *      HirList @ )   // List of l-value expressions (variable nodes,
 *                    // pointer expressions, etc.) and arithmetic
 *                    // expressions representing actual parameters.
 *     // As for detail, see AsmStmt interface.
 *  ConditionalExp_ ->     // boolean expression
 *     Exp
 *  Exp ->                 // Expression.
 *     Factor_
 *   | UnaryExp_
 *   | BinaryExp_
 *   | ExpListExp
 *   | TernaryExp_
 *   | NullNode
 | InfNode
 *  Factor_  ->
 *     ConstNode
 *   | SymNode
 *   | CompoundVar_
 *   | FunctionExp
 *   | PhiExp
 * //| AssignStmt          // HIR-C
 *   | ExpListExp
 *   | HirSeq              // Sequence of objects
 *  UnaryExp_ ->           // Unary expression.
 *     ( UnaryOperator_ attr
 *       Exp @ )
 *   | ( sizeofCode attr  // size of the type of Exp
 *        Exp @ )
 *   | ( sizeofCode attr  // size of the type
 *       TypeNode @ )     // represented by TypeNode.
 *  BinaryExp_ ->         // Binary expression.
 *     ( BinaryOperator_ attr
 *       Exp @
 *       Exp @ )
 *  TernaryExp_ ->        // Ternary expression.
       SelectExp          // HIR-C only
 *  CompoundVar_ ->    // Compound variable.
 *     SubscriptedExp  // Subscripted variable.
 *   | PointedExp      // Pointed variable.
 *   | QualifiedExp    // Qualified variable.
 *  SubscriptedExp ->  Subscripted variable.
 *     ( subsCode attr // Array with subscript expression.
 *       Exp @       // Expression indicating the array.
 *       Exp @ )     // Subscript expression.
//  | ( subsCode attr
//      Exp @       // Expression indicating an array.
//      ExpList @ ) // List of Subscripts.
//                  // (1st subscript, 2nd subscript,
//                  //  etc. for rectangular array.)
 *  ElemSize_    ->    // Element size.
 *     Exp
 *  QualifiedExp ->    // Qualified expression.
 *     ( qualCode attr // Qualified variable
 *       Exp @         // Should represent a structure or union.
 *       ElemNode @ )  // struct/union element.
 *  PointedExp ->       // Pointed expression.
 *     ( arrowCode attr // Pointed variable
 *       Exp @          // Expression representing a variable
 *       PointedElem_ @ ) // Pointed element.
 *  PointedElem_ ->
 *     ElemNode  // Pointed element (with displacement).
 *   | null      // Pointed location (0 displacement).
 *  ConstNode ->                      // Constant symbol.
 *     ( constCode attr intConst )    // integer   constant node
 *   | ( constCode attr floatConst )  // float     constant node
 *   | ( constCode attr charConst )   // character constant node
 *   | ( constCode attr stringConst ) // string    constant node
 *   | ( constCode attr boolConst )   // boolean   constant node
 *  SymNode   ->        // Symbol node.
 *     (symCode Sym )   // Program name, etc.
 *   | VarNode
 * //| ParamNode   DELETED
 *   | SubpNode
 *   | LabelNode
 *   | ElemNode   not DELETED
 *   | TypeNode
 *  VarNode   ->
 *     ( symCode attr varSym )   // Variable name node
 *  SubpNode  ->
 *     ( symCode attr subpSym )  // Subprogram name node
 *  LabelNode ->
 *     ( symCode attr labelSym ) // Label reference node
 *  ElemNode  ->
 *     ( symCode attr elemSym )  // structure/union element
 *                                    // name node.
 *  TypeNode  ->
 *     ( symCode attr typeSym )  // Type name node
 *  FunctionExp ->     // Function expression.
 *     ( callCode attr
 *       Exp @         // Expression specifying function
 *                     // to be called (SubpNode, (addr SubpNode), etc.).
 *       HirList @ )   // Actual parameter list.
 *                     // It is an HirList whose elements are Exp.
 *  IrList     ->
 *     ( listCode attr    // A List that can be treated as IR.
 *       List_of_Objects_ @ ) // Its elements may be Sym, String and IR object.
 *  HirList        ->
 *     ( listCode attr    // A List of HIR elements. It can be treated as HIR.
 *       List_of_HIR_Objects_ @ ) // Its elements should be HIR object.
 *  ExpListExp  ->        // Expression representing
 *     ( expListCode attr // a list of
 *       List_of_Exp @ )  // expressions in HIR form.
 *  HirSeq  ->
 *     ( seqCode attr HIR @ )  // Sequence of some definite
 *   | ( seqCode HIR @  HIR @ ) // number of HIR nodes.
 *   | ( seqCode HIR @  HIR @  HIR @ )
 *   | ( seqCode HIR @  HIR @  HIR @  HIR @ )
 *  PhiExp         ->
 *     (phiCode attr FlowVarList_ @ )  // phi function
 *  FlowVarList_   ->
 *     ( listCode attr
 *       List_of_VarLabelPair @ )
 *                               // List of (Var Label) pairs.
 *  VarLabelPair   ->
 *     ( listCode attr VarNode @  Label @)
 *  UnaryOperator_ ->
 *     notCode      // bitwise not (~) one's complement
 *                  // logical not (boolean not) (!)
 *   | negCode      // negate (unary minus)
 *   | addrCode     // get address (&)
 *   | contentsCode // get contents of pointed memory
 *   | convCode     // type conversion for basic type
 *   | decayCode    // convert array to pointer
 *   | sizeofCode   // sizeof operator
 *   | encloseCode  // honor parenthesis
 *   | IncrDecrOperator_  // Increment/decrement. HIR-C only.
 *  BinaryOperator_ ->
 *   | addCode      // add                 (+)
 *   | subCode      // subtract            (-)
 *   | offsetCode   // Offset between pointers (HIR-C only)
 *   | multCode     // multiply            (*)
 *   | divCode      // divide              (/)
 *   | modCode      // modulo              (%)
 *   | andCode      // bitwise and, logical and for bool (&)
 *   | orCode       // bitwise or,  logical or for bool  (|)
 *   | xorCode      // bitwise exclusive or, logical exclusive or (^)
 *   | shiftLLCode  // shift left  logical    (<<) //
 *   | shiftRCode   // shift right arithmetic (>>)
 *   | shiftRLCode  // shift right logical    (>>) //
 *   | undecayCode  // convert pointer to array
 *   | expRepeatCode   // Repeat constant values (operand 1)
 *                     //   count (operand 2) times.
 *   | CompareOperator_
 *   | CompareZeroOperator_            // HIR-C only
 *   | ShortCircuitOperator_           // HIR-C only
 *   | commaCode    // comma operator  // HIR-C only
 *  CompareOperator_ ->
 *     cmpEqCode    // equal
 *   | cmpNeCode    // not equal
 *   | cmpGtCode    // greater than
 *   | cmpGeCode    // greater or equal
 *   | cmpLtCode    // less than
 *   | cmpLeCode    // less or equal
 *  CompareZeroOperator_ ->                // HIR-C only
 *     eqZeroCode   // equal zero          // HIR-C only
 *  ShortCircuitOperator_ ->               // HIR-C only
 *     lgAndCode    // logical and (&&)    // HIR-C only
 *   | lgOrCode     // logical or  (||)    // HIR-C only
 *  IncrDeclOperator_ ->                             // HIR-C only
 *     preIncrCode  // pre-increment  (++) // HIR-C only
 *   | preDecrCode  // pre-decrement  (--) // HIR-C only
 *   | postIncrCode // post-increment (++) // HIR-C only
 *   | postDecrCode // post-decrement (--) // HIR-C only
 *  CompoundAssignmentOperator_ ->         // HIR-C only
 *     multAssignCode   // *=    // HIR-C only
 *   | divAssignCode    // /=    // HIR-C only
 *   | modAssignCode    // %=    // HIR-C only
 *   | addAssignCode    // +=    // HIR-C only
 *   | subAssignCode    // -=    // HIR-C only
 *   | shiftLAssignCode // <<=   // HIR-C only
 *   | shiftRAssignCode // >>=   // HIR-C only
 *   | andAssignCode    // &=    // HIR-C only
 *   | xorAssignCode    // ^=    // HIR-C only
 *   | orAssignCode     // |=    // HIR-C only
 *  SelectExp ->        //  (Exp ? Exp : Exp) // HIR-C only
 *     ( selectCode attr
 *       ConditionalExp @   Exp @  Exp @ )
 *
 *  attr ->             // Attribute attached to the HIR node
 *     typeSym          // Shows the type of HIR subtree.
 *     OptionalAttrSeq_ // Sequence of optional attributes
 *                      // that are not inevitable at the first stage
 *                      // of HIR generation. The optional attributes
 *                      // may be attached to give information used
 *                      // in some specific compiler modules or
 *                      // to help compiling processes.
 *  OptionalAttrSeq_ ->
 *     OptionalAttr_ OptionalAttrSeq_
 *   | null
 *  OptionalAttr_ ->
 *     StmtAttr_      // Attributes for statements
 *   | IndexNumber_   // Index number to identify the HIR node.
 *   | Flag_          // true/false flag.
 *   | InfItem_       // Node-wise information.
 *   | ExpId          // Expression identifier used in flow analysis.
 *   | Work_          // Phase-wise information.
 *  StmtAttr_ ->      // Attributes attached to statement subtrees.
 *     FileName_      // Source program file containing the statement.
 *     LineNumber_    // Line number of the line in which the
 *                    // statement started.
 *  FileName_ ->
 *     stringConstValue
 *  LineNumber_
 *     intConstvalue
 *  IndexNumber_ ->
 *     intConstValue
 *  Flag_ ->
 *     FlagNumber_
 *     FlagValue_
 *  FlagNumber_ ->
 *     intConstValue   // Such as  FLAG_NOCHANGE, FLAG_C_PTR,
 *                     // FLAG_CONST_EXP
 *  FlagValue_ ->
 *     true
 *   | false
 *  InfItem_ ->
 *     InfKind_ InfObject_
 *
 *
 *  Deleted methods:
 *    buildXxx, getSym (use getSym in IR or getSymNodeSym in SymNode)
 *    insertBBlockCode, insertGBlockCode, insertPBlockCode.
 *  Deleted operators:
 *    addUCode, subUCode, multUCode, divUCode.
 *    (Decide by Type attached to each node.)
 *    addA, subA, multA, divA, modA, shiftL.   (##9)
 *    lgNot  (##10)
 *
*** Construction of HIR objects:
 *  Most HIR objects are constructed by object factory methods
 *  such as program(....), subpDefinition(....), assignStmt(....),
 *  ifStmt(....), exp(....), etc. specified in the HIR interface.
 *  Users are recommended to use the object factory methods so as
 *  to avoid forgetting to specify essential data and to make
 *  relation with other objects.  It is not recommended to use
 *  "new" directly to construct HIR objects.
 *
*** Transformation of HIR:
 *  HIR are transformed in various phases of the compiler
 *  for optimization, parallelization, etc.
 *  Such transformation can be executed by invoking transformation
 *  methods such as replaceThisNode, addNextStmt, etc.
 *  Transformed result should always be tree. Care should be taken
 *  not to share leafs and branches between subtrees.
 *  The simple way of keeping tree structure in transformation is
 *  to make a new subtree copying some part of original tree
 *  and replace some branch so that no node is linked from
 *  plural parent nodes.
 *  It is strongly recommended not to destroy the control structure
 *  of HIR. For exmple, jump into a loop from outside, jump out of
 *  loop from loop step part, jump into if statement from outside,
 *  etc. although such transformations are not automatically prohibited.
 *
 *  HIR nodes should be numbered by applying setIndexNumberToAllNodes
 *  to Program tree or SubpDefinition subtree when HIR is created or
 *  modified. When HIR is created by a parser or entirely transformed
 *  by an optimizer, setIndexNumberToAllNodes(1) should be applied to
 *  hirRoot.programRoot. If subprogram-wise transformation is done
 *  (e.g., repeat to do subprogram-wise optimization and code generation),
 *  then apply setIndexNumberToAllNodes(subpDefinition.getNodeIndexMin())
 *  to SubpDefinition node "subpDefinition" of each subprogram.
 *
*** Operator number
 *public static final int
 *  OP_PROG          =  1,   // program( .... )
 *  OP_SUBP_DEF      =  2,   // subpDefinition( .... )
 *  OP_LABEL_DEF     =  3,   // labelDef( .... )
 *  OP_INF           =  4,   // infNode( .... )
 *  OP_CONST         =  5,   // constNode( .... )
 *  OP_SYM           =  6,   // symNode( .... )
 *  OP_VAR           =  7,   // varNode( .... )
 *  OP_PARAM         =  8,   //                UNNECESSARY ?
 *  OP_SUBP          =  9,   // subpNode( .... )
 *  OP_TYPE          = 10,   //                UNNECESSARY ?
 *  OP_LABEL         = 11,   // labelNode( .... )
 *  OP_ELEM          = 12,   // elemNode( .... )
 *  OP_LIST          = 14,   // irList( .... )  List of HIR/Sym objects
 *                           // such as HIR nodes and symbols
 *  OP_SEQ           = 15,   // hirSeq( .... )
 *  OP_ENCLOSE       = 16,   // exp( int, Exp ) Enclose subexpression

 *  OP_SUBS          = 17,   // subscriptedExp( .... )
 *  OP_QUAL          = 19,   // qualifiedExp( .... )
 *  OP_ARROW         = 20,   // pointedExp( .... )

 *  OP_STMT          = 21,   // Statement code lower. Stmt is
 *                           // an abstract class and there is
 *                           // no instance having OP_STMT.
 *  OP_LABELED_STMT  = 21,   // labeledStmt( .... )
 *                           // Use the same number as Stmt
 *                           // because Stmt is an abstract class.
 *  OP_ASSIGN        = 22,   // assignStmt( .... )
 *  OP_IF            = 23,   // ifStmt( .... )
 *  OP_WHILE         = 24,   // whileStmt( .... )
 *  OP_FOR           = 25,   // forStmt( .... )
 *  OP_REPEAT        = 26,   // repeatStmt( .... )
 *  OP_INDEXED_LOOP  = 27,   // indexedLoopStmt( .... )

 *  OP_JUMP          = 28,   // jumpStmt( .... )
 *  OP_SWITCH        = 32,   // switchStmt( .... )
 *  OP_CALL          = 33,   // callStmt( .... )
 *  OP_RETURN        = 34,   // returnStmt( .... )
 *  OP_BLOCK         = 35,   // blockStmt( .... )
 *  OP_EXP_STMT      = 36,   // expStmt( .... ) expression statement
 *  OP_ASM           = 37,   // asmStmt( .... )
 *  OP_STMT_UPPER    = 37,

// OP_ADD ... OP_XOR OP_CMP_EQ ... OP_CMP_LE OP_SHIFT_LL ... OP_SHIFT_LL
//         are binary operators used in exp(int, Exp, Exp).
 *  OP_ADD           = 38,
 *  OP_SUB           = 39,
 *  OP_MULT          = 41,
 *  OP_DIV           = 42,
 *  OP_MOD           = 43,

 *  OP_AND           = 46,
 *  OP_OR            = 47,
 *  OP_XOR           = 48,

 *  OP_CMP_EQ        = 51,
 *  OP_CMP_NE        = 52,
 *  OP_CMP_GT        = 53,
 *  OP_CMP_GE        = 54,
 *  OP_CMP_LT        = 55,
 *  OP_CMP_LE        = 56,

 *  OP_SHIFT_LL      = 58,
 *  OP_SHIFT_R       = 59,
 *  OP_SHIFT_RL      = 60,
//  OP_LG_NOT       = 61, // Use (OP_CMP_EQ e false) instead of (OP_LG_NOT e)
 *  OP_NOT           = 62,   // exp(OP_NOT, Exp)
 *  OP_NEG           = 63,   // exp(OP_NEG, Exp)
 *  OP_ADDR          = 64,   // exp(OP_ADDR, EXP )
 *  OP_CONV          = 65,   // convExp( .... )
 *  OP_DECAY         = 66,   // decayExp( .... )
 *  OP_UNDECAY       = 67,   // undecayExp( .... )
// OP_CONTENTS, OP_SSIZEOF are unary operator used in exp(int, Exp).
 *  OP_CONTENTS      = 68,   // contentsExp( .... )
 *  OP_SIZEOF        = 70,   // sizeofExp( .... )
//  OP_NOCHANGE_EXP = 71,   // See FLAG_NOCHANGE
 *  OP_SETDATA       = 71,
 *  OP_PHI           = 72,  // phiExp( .... )
 *  OP_NULL          = 73,  // nullNode();

// OP_OFFSET ... OP_LG_OR are binary operators used in exp(int, Exp, Exp).
 *  OP_OFFSET        = 76,  // exp( int, Exp, Exp ) // HIR-C only
 *  OP_LG_AND        = 77, // HIR-C only
 *  OP_LG_OR         = 78, // HIR-C only
 *  OP_SELECT        = 79, // HIR-C only
 *  OP_COMMA         = 80, // HIR-C only

 *  OP_EQ_ZERO       = 81, // ! // HIR-C only
 *  OP_PRE_INCR      = 82, // HIR-C only
 *  OP_PRE_DECR      = 83, // HIR-C only
 *  OP_POST_INCR     = 84, // HIR-C only
 *  OP_POST_DECR     = 85, // HIR-C only
 *  OP_ADD_ASSIGN    = 86, // HIR-C only
 *  OP_SUB_ASSIGN    = 87, // HIR-C only
 *  OP_MULT_ASSIGN   = 88, // HIR-C only
 *  OP_DIV_ASSIGN    = 89, // HIR-C only
 *  OP_MOD_ASSIGN    = 90, // HIR-C only

 *  OP_SHIFT_L_ASSIGN= 91, // HIR-C only
 *  OP_SHIFT_R_ASSIGN= 92, // HIR-C only
 *  OP_AND_ASSIGN    = 93, // HIR-C only
 *  OP_OR_ASSIGN     = 94, // HIR-C only
 *  OP_XOR_ASSIGN    = 95, // HIR-C only

 *  OP_EXPLIST       = 96, // Expression representing a list of expressions.
 *  OP_EXPREPEAT     = 97  // Repetition of the same values.
 *                         // OP_EXPLIST and OP_EXPREPEAT are used only in
 *                         // OP_SETDATA expression.
 *  ;

*** Flag numbers applied to HIR nodes:
 * static final int     // Flag numbers used in setFlag/getFlag.
 *                   // They should be a number between 1 to 31.
 *FLAG_NOCHANGE = 1, // This subtree should not be
 *                   // changed in optimization.
 *FLAG_C_PTR    = 2, // The operation (add, sub) is
 *                   // pointer operation peculiar to C.
 *FLAG_CONST_EXP= 3; // This subtree is a constant expression.
 *                   // (constant, address constant, null,
 *                   //  sizeof fixed-size-variable,
 *                   //  expression whose operands are all constant
 *                   //  expression)
 *  // Flag numbers 24-31 can be used in each phase for apbitrary purpose.
 *  // They may be destroyed by other phases.

*** Coding rules applied to all classes in the sym, hir packeges:
 *
 * Methods begin with lower case letter.
 * Constants (final static int, etc.) are spelled in upper case letters.
 * Indentation is 2 characters. Tab is not used for indentation.
 * Formal parameters begin with p.
 * Local variables begin with l.
 * Methods and variables are named so that meaning is easily guessed.
 * Short names less than 3 characters are not used except for
 * very local purpose.
 *
</PRE>
**/

public interface
HIR extends IR, HIR0, Cloneable
{

//==== Access methods available to build IR nodes. ====//
//     (Factory methods for creating HIR will be explained later.)

/** program Make a program node.
 *  See Program interface.
 *  @param pProgName Program name (may be null).
 *  @param pGlobalSymTable Symbol table for global symbols.
 *  @param pInitiationPart Initiation statement (may be null).
 *  @param pSubpList List of subprograms; At first, it may be
 *      an empty list; (See addSubpDefinition of Program interface).
**/
//##54 public Program
//##54 program( Sym pProgName, SymTable pGlobalSymTable,
//##54          IR pInitiationPart, IrList pSubpList);

/** subpDefifnition
 *  Make a subprogram definition node and
 *  set symRoot.subpCurrent = pSubpSym,
 *  set symRoot.symTableCurrentSubp = pLocalSymTable.
 *  You may add initiation-part, HIR-body later
 *  by using addInitiationStmt, setHirBody of SubpDefinition
 *  interface.
 *  Start label and end label for the given subprogram
 *  are generated to make getStartLabel(), getEndLabel()
 *  of Subp interface effective.
 *  @param pSubpSym Subprogram symbol. Should be given.
 *  @param pLocalSymTable Symbol table local in the subprogram;
 *      If subprogram uses a local symbol table, this parameter
 *      should be given (by using pushSymTable() of SymTable
 *      interface); Even if the source program does not use
 *      local symbol, this parameter is required to register
 *      temporal variables and internal labels generated by
 *      the compiler. //##51
 *  @return the created SubpDefinition instance.
**/
//##54 public SubpDefinition
//##54 subpDefinition( Subp pSubpSym, SymTable pLocalSymTable );

/** subpDefinition without SymTable
 *  If source language admits no local symbol for subprogram,
 *  use this method to create SubpDefinition without
 *  local symbol table.
 *  Other items are the same as the previous method.
 *  @param pSubpSym Subprogram symbol. Should be given.
 *  @return the created SubpDefinition instance.
**/
//##54 public SubpDefinition
//##54 subpDefinition( Subp pSubpSym );

/** subpDefinition with full specification.
 *  Other items are the same as the previous method.
 *  @param pSubpSym Subprogram symbol. Should be given.
 *  @param pLocalSymTable Symbol table local in the subprogram;
 *      If subprogram uses a local symbol table, this parameter
 *      should be given (by using pushSymTable() of SymTable
 *      interface); Even if the source program does not use
 *      local symbol, this parameter is required to register
 *      temporal variables and internal labels generated by
 *      the compiler. //##51
 *  @param pInitiationPart Initiation statement block. optional.
 *  @param pHirBody Subprogram body in HIR. optional.
**/
public SubpDefinition
subpDefinition( Subp pSubpSym, SymTable pLocalSymTable,
                BlockStmt pInitiationPart, BlockStmt pHirBody);

/** irList Make an HirList node that makes a LinkedList.
 *  The created list can be treated as an HIR node.
 *  @param pList List of objects to be included in the HirList;
 *      The elements of the list are an HIR object or Sym object.
 *  @return IrList containing pList;
 *      Resultant list is also an instance of HIR.
**/
public coins.ir.IrList
irList( LinkedList pList );

/** irList Make an empty HirList node that make a LinkedList.
 *  Use methods of IrList to add elements to the list.
 *  The elements to be added are an HIR object or Sym object.
 *  The created list can be treated as an HIR node.
 *  @return an empty IrList;
 *      Resultant list is also an instance of HIR.
**/
//##54 public coins.ir.IrList
//##54 irList();

/** infNode
 *  Build an InfNode representing some information.
 *  The information may be defined in each compiler for arbitrary purpose.
 *  @param pInfKindInterned a string to identify the information kind of
 *      the InfNode; It should be an interned string (string with intern()).
 *      See coins.Registry.
 *  @param pInfData  any object to be attached (such as Sym or HIR node);
 *      The object kind is indicated by the pInfKindInterned;
 *      If it is an IrList, then it can represent complicated
 *      information structure.
 ***deprecated
**/
//##68 public InfNode
//##68 infNode( String pInfKindInterned, Object pInfData );

/** infStmt
 *  Build an InfStmt representing some information.
 *  The information may be defined in each compiler for arbitrary purpose.
 *  @param pInfKindInterned a string to identify the information kind of
 *      the InfNode. It should be an interned string (string with intern());
 *      See coins.Registry.
 *  @param pInfData IrList of objects to be attached (Sym, HIR node, String or IrList);
 *      The object kind is indicated by the pInfKindInterned;
 *      If an element of the IrList is an IrList, then the parameter can represent
 *      complicated information structure.
**/
public InfStmt
infStmt( String pInfKindInterned, IrList pInfData );

public InfStmt
infStmt( String pInfKind, Object pInfData ); //##70

//==== Methods to get/set HIR node information ====//

/** getType
 *  Get the type attached to this node.
 *  The type is usually attached when node is built by HIR
 *  factory methods such as exp.
 *  @return the type attached to this node.
**/
//##54 Type
//##54 getType();

/** setType
 *  Attach a type to this node.
 *  When HIR node is created by HIR factory methods,
 *  its type is set in the factory methods so that
 *  it is unnecessary to call setType again.
 *  @param pType type to be attached to this node;
 *      It should represent a proper type corresponding to the
 *      result of subtree represented by this node.
**/
void
setType( Type pType );

/** getNextStmt Get statement next to this statement.
 *  @return the statement next to this statement if
 *      this is a statement; If this is not a statement,
 *      return null.
**/
//##54 public Stmt
//##54 getNextStmt();

/** getFlag returns the value (true/false) of the flag indicated
 *  by pFlagNumber.
 *  See FLAG_NOCHANGE, FLAG_CONST_EXP, etc. of HIR interface.
 *  @param pFlagNumber flag identification number.
 *  @return the value of the flag.
**/
//##54 boolean
//##54 getFlag( int pFlagNumber);

/** setFlag sets the flag specified by pFlagNumber.
 *  See FLAG_NOCHANGE, FLAG_CONST_EXP, etc. of HIR interface.
 *  Some local flag numbers can be added in each phase.
 *  @param pFlagNumber flag identification number.
 *  @param pYesNo true or false to be set to the flag.
**/
//##54 void
//##54 setFlag( int pFlagNumber, boolean pYesNo);

/** getFlagBox
 *  Users are recommended to use getFlag( int pFlagNumber )
 *  except when they understand the treatment of FlagBox
 *  in detail.
 *  @return the flag box attached to this node;
 *      null if flag box is not attached (no flag is set).
**/
//##54 public FlagBox
//##54 getFlagBox();

/** setWork
 *  Set information privately used in each phase.
 *  @param pWork any information of arbitrary class to be set to this node;
 *      You may define a new class that represents arbitrary information
 *      and give its reference as pWork.
 *  This method is moved to IR interface.
**/
void
setWork( Object pWork ); //##54

/** getWork
 *  Get information set by setWork for this node.
 *  Users should cast by the name of the class used in setWork.
 *  @return the information object set by setWork.
 *  This method is moved to IR interface.
**/
Object
getWork();  //##54

//========================================//

/** getExpId
 *  Get the expression identifier assigned to this node.
 *  @return the expression identifier assigned to this node,
 *      or return null if it is not assigned.
**/
ExpId
getExpId();

/** setExpId
 *  Set the expression identifier computed for this node.
 * (Do not use this method but use coins.flow.SubpFlow.setExpId(IR). //##60
 ***deprecated
**/
//##62 void
//##62   setExpId( ExpId pExpId );

/** getSymOrExpId
 *  If this is a SymNode with FlowAnalSym instance,
 *  then return it
 *  else return getExpId().
 *  @return FlowAnalSym or ExpId attached to this node.
**/
public FlowAnalSym
getSymOrExpId();  //  Unnecessary ?

/** setFlowAnalSym
 *  Set ExpId as the flow analysis symbol for this node.
 *  @param pFlowAnalSym ExpId to be assigned to this node.
**/
//##78 void
//##78 setFlowAnalSym( ExpId pFlowAnalSym );

/** getChildNumber
 *  Get the child number of this node.
 *  If not found, return -1.
 *  (Usually, it is unnecessary to use this because
 *   the child number is known by stack, etc.)
**/
//##54 public int
//##54 getChildNumber();

/** isStmt
 *  @return true if this node is a statement node,
 *      otherwise return false.
**/
public boolean
isStmt();

/** isEmpty
 * Decide if pHir is empty or not, where empty means either
 *   null, NullNode, HIR with OP_NULL as its operator,
 *   LabeledStmt whose statement body isEmpty, or
 *   ExpStmt whose Exp part isEmpty.
 * @param pHir HIR node to be tested if empty or not.
 * @return true if pHir is empty else retuen false.
 */
public boolean
isEmpty( HIR pHir );

/** getStmtContainingThisNode
 *  Get the innermost statement or LabeledStmt containing this node
 *  by traversing ancestors of this node.
 *  If this is a statement, then this is the innermost statement.
 *  If the the parent of the innermost statement is  a LabeledStmt,
 *  then the LabeledStmt is returned else the innermost statement
 *  is returned.
 *  @return the innermost statement or LabeledStmt
 *    containing this node.
**/
//##54 public Stmt
//##54 getStmtContainingThisNode();

/** Check if pSubtree is contained in this subtree.
 * It is not recommended to apply this method to large subtree
 * but recommended to apply to small subtree such as expressions.
 * @param pSubtree subtree to be searched.
 * @return true if found, false if not found.
 */
public boolean
contains( HIR pSubtree );  //##53

/** cutParentLink
 *  Cut both links from/to parent node if they exist.
 *  (Used to prepare for moving a node.)
 *  This method is prepared for factory methods and it is not
 *  recommended to use this method except when user knows the
 *  structure of HIR in detail.
**/
public void
cutParentLink();

//========== Factory methods to create HIR node ==========//

//==== Factory methods to create terminal nodes ====//

/** varNode
 *  Make a VarNode instance having Var symbol pVar.
 *  @param pVar Variable symbol to be attached to the node.
 *  @return the resultant VarNode.
**/
//##54 public VarNode
//##54 varNode( Var pVar );

// public ParamNode
// paramNode( Param pParam );  DELETED

/** elemNode
 *  Make an ElemNode instance having Elem symbol pElem.
 *  @param pElem Struct/union element symbol to be attached to the node.
 *  @return the resultant ElemNode.
**/
//##54 public ElemNode
//##54 elemNode( Elem pElem );  not DELETED

/** subpNode
 *  Make a SubpNode instance having Subp symbol pSubp.
 *  @param pSubp Subprogram symbol to be attached to the node.
 *  @return the resultant SubpNode.
**/
//##54 public SubpNode
//##54 subpNode( Subp pSubp );

/** labelNode
 *  Make a LabelNode instance having Label symbol pLabel.
 *  (See labelDef.)
 *  @param pLabel Label symbol to be attached to the node.
 *  @return the resultant LabelNode.
**/
//##54 public LabelNode
//##54 labelNode( Label pLabel );

/** constNode
 *  Make a ConstNode instance having constant symbol pConst.
 *  @param pConst Constant symbol to be attached to the node.
 *  @return the resultant ConstNode.
**/
//##54 public ConstNode
//##54 constNode( Const pConst );

/** intConstNode
 *  Make a ConstNode instance having pIntValue as its value.
 *  The constant value is changed to Const symbol and attached to
 *  the constant node.
 *  @param pIntValue Integer value to be attached to the node.
 *  @return the resultant ConstNode.
**/
public ConstNode
intConstNode( int pIntValue );
//##54 public ConstNode
//##54 intConstNode( long pIntValue );

/////////////////////////////////////// S.Fukuda 2002.9.3 begin
/** offsetConstNode
 *  Make a ConstNode instance having pIntValue as its offset value.
 *  The offset value pIntValue is changed to Const symbol and
 *  attached to the constant node.
 *  @param pIntValue Offset value to be attached to the node.
 *  @return the resultant ConstNode.
**/
public ConstNode
offsetConstNode( long pIntValue );
/////////////////////////////////////// S.Fukuda 2002.9.3 end

/** trueNode
 *  Make a ConstNode instance having boolean true value.
 *  @return the true ConstNode.
**/
//##54 public ConstNode
//##54 trueNode();

/** falseNode
 *  Make a ConstNode instance having boolean false value.
 *  @return the false ConstNode.
**/
//##54 public ConstNode
//##54 falseNode();

/** symNode
 *  Make a SymNode instance having pSym as attached symbol.
 *  This method is used for symbols other than Var, Elem, Subp,
 *  Label, and constant.
 *  @param pSym Symbol to be attached to the node.
 *  @return the resultant SymNode.
**/
//##54 public SymNode
//##54 symNode( Sym pSym );

/** nullNode
 *  Make a NullNode instance.
 *  @return the NullNode.
**/
public NullNode
nullNode( );

/** labelDef
 *  Make a LabelDef instance that defines the label pLabel.
 *  (See labelNode.)
 *  @param pLabel Label symbol to be defined.
 *  @return the resultant LabelDef.
**/
//##54 public LabelDef
//##54 labelDef( Label pLabel );

//==== Factory methods to create nonterminal nodes ====//

/** exp
 *  Build unary expression according to the
 *  operator given:
 *  OP_NOT      // bitwise not (~) one's complement
 *              // logical not for bool (!)
 *     // Restriction in the current version:
 *     //   As for ConditionalExp in IfStmt and LoopStmt,
 *     //   use exp(HIR0.OP_CMP_EQ, e, hir.constNode(symRoot.boolConstFalse))
 *     //   instead of exp(HIR0.OP_NOT, e) for bool exp e.
 *  OP_NEG      // negate (unary minus)
 *  OP_ADDR     // get address (&)
 *  OP_CONTENTS // get contents of pointed memory
 *  OP_CONV     // type conversion for basic type
 *  OP_DECAY    // convert array to pointer
 *  OP_SIZEOF   // sizeof operator
 *  OP_ENCLOSE  // honor parenthesis
 *  The type of resultant expression is set according to the rule
 *  described in the HIR specifications, however, it may be necessary
 *  to do language-wise treatment. In such case, front-end part of the
 *  language should set type after creating the instance of Exp.
 *  (In C, expressions related to pointer and vector are specially
 *   treated in C front end ToHirC.)
 *
 *  @param pOperator unary operator such as
 *    OP_NOT, OP_NEG, OP_ADDR, OP_CONTENTS, OP_CONV,
 *    OP_DECAY, OP_SIZEOF, OP_ENCLOSE
 *       defined in HIR interface.
 *  @param pExp1 operand expression.
 *  @return the resultant unary expression subtree.
**/
//##54 public Exp
//##54 exp( int pOperator, Exp pExp1 );

/** exp
 *  Build binary expression.
 *  The type of resultant expression is set according to the rule
 *  described in the HIR specifications, however, it may be necessary
 *  to do language-wise treatment. In such case, front-end part of the
 *  language should set type after creating the instance of Exp.
 *  (In C, expressions related to pointer and vector are specially
 *   treated in C front end ToHirC.)
 *  @param pOperator binary operator such as OP_ADD, OP_MULT, etc.
 *       defined in HIR interface.
 *  @param pExp1 operand 1 expression.
 *  @param pExp2 operand 2 expression.
 *  @return the resultant binary expression subtree.
**/
//##54 public Exp
//##54 exp( int pOperator, Exp pExp1, Exp pExp2 );

/** exp:
 *  Build ternary expression (OP_SELECT, etc.).
 *  @param pExp1 operand 1 expression.
 *  @param pExp2 operand 2 expression.
 *  @param pExp3 operand 3 expression.
 *  @return the resultant ternary expression subtree.
**/
public Exp
exp( int pOperator, Exp pExp1, Exp pExp2, Exp pExp3 );

/** decayExp:
 *  Build an expression that decay a vector to a pointer to vector element,
 *  or decay a String into a pointer to Char element.
 *  @param pExp vector variable or string constant.
 *  @return decay expression with pointer type.
**/
//##54 public Exp
//##54 decayExp( Exp pExp );

/** undecayExp
 *  Build an expression that undecay a pointer to a vector whose element
 *  type is pointed type.
 *  @param pPointerExp pointer expression pointing to the
 *      first element of the resultant vector.
 *  @param pElemCount Constant expression showing element count of
 *      the resultant vector.
 *  @return the vector expression.
**/
public Exp
undecayExp( Exp pPointerExp, ConstNode pElemCount );

/** undecayExp
 *  Build an expression that undecay a pointer to a vector whose element
 *  type is pointed type.
 *  @param pPointerExp pointer expression pointing to the
 *      first element of the resultant vector.
 *  @param pElemCount Expression showing element count of
 *      the resultant vector.
 *  @param pLowerBound Expression showing the lower index bound of
 *      the resultant vector.
 *  @return the vector expression.
**/
public Exp
undecayExp( Exp pPointerExp, Exp pElemCount, Exp pLowerBound );

/** undecayExp
 *  Build an expression that undecay a pointer to a vector whose element
 *  type is pointed type.
 *  @param pPointerExp pointer expression pointing to the
 *      first element of the resultant vector.
 *  @param pElemCount  element count of the resultant vector.
 *  @return the vector expression.
**/
public Exp //SF030531
undecayExp( Exp pPointerExp, long pElemCount ); //SF030531

/** undecayExp
 *  Build an expression that undecay a pointer to a vector whose element
 *  type is pointed type.
 *  @param pPointerExp pointer expression pointing to the
 *      first element of the resultant vector.
 *  @param pElemCount  element count of the resultant vector.
 *  @param pLowerBound  lower index bound of the resultant vector.
 *  @return the vector expression.
**/
//##54 public Exp
//##54 undecayExp( Exp pPointerExp, long pElemCount, long pLowerBound );

/** subscriptedExp
 *  Build a subscripted expression.
 *  @param pArrayExp array expression to which subscript is
 *      to be attached;
 *      If multi-dimensional subscript is required, make pArrayExp
 *      as a subscripted variable and add more subscript by this method.
 *  @param pSubscript subscript expression.
 *  @return subscripted expression node having operation code subsCode.
**/
//##54 public SubscriptedExp
//##54 subscriptedExp( Exp pArrayExp, Exp pSubscript );

/** subscriptedExp
 *  Build an expression representing the indicated element
 *  of a vector whose element size is unfixed.
 *  If the size of the vector element is fixed and pElemSize is
 *  equal to it, then
 *    (subs pArrayExp pSubscript)
 *  is returned.
 *  If not, then a pointer expression representing the element
 *  located at the address
 *    addr(pArrayExp) + pElemSize * (pSubscript - lowerBound)
 *  is returned.
 *  As for vectors whose element size is constant or represented
 *  by an expression, use
 *    subscriptedExp( Exp pArrayExp, Exp pSubscript )
 *  instead of this method.
 *  @param pArrayExp is array expression to which subscript is
 *      to be attached (it is unfixed size vector).
 *  @param pSubscript is subscript expression.
 *  @param pElemSize is the element size of pArrayExp in bytes.
 *  @return either subscripted expression node having operation code subsCode
 *     or pointer expression if the element type is unfixed-size.
**/
public Exp
subscriptedExp( Exp pArrayExp, Exp pSubscript, Exp pElemSize );

/** qualifiedExp
 *  Build a qualified expression.
 *  @param pStructUnionExp expression specifying structure or union
 *      or region_variable to qualify.
 *  @param pElemNode An element of pStructUnionExp.
 *  @return qualified expression node having operation code qualCode.
**/
//##54 public QualifiedExp
//##54 qualifiedExp( Exp pStructUnionExp, ElemNode pElemNode );

/** pointedExp
 *  Build a pointed expression.
 *  @param pStructUnionExp expression specifying structure or union
 *      whose element is to be pointed.
 *  @param pElemNode An element of pStructUnionExp.
 *  @return pointed expression node having operation code arrowCode.
**/
//##54 public PointedExp
//##54 pointedExp( Exp pStructUnionExp, ElemNode pElemNode );

/** contentsExp
 *  Build an expression that gets the contents pointed by pPointerExp.
 *  @param pPointerExp Expression representing a pointer.
 *  @return the expression that gets the pointed expression.
**/
//##54 public Exp
//##54 contentsExp( Exp pPointerExp );

/** convExp
 *  Build an expression to convert the type of pExp to pType.
 *  @param pType Type to which pExp is to be changed.
 *  @param pExp Expression to be converted.
 *  @return the expression converted to pType.
**/
//##54 public Exp
//##54 convExp( Type pType, Exp pExp );

/** sizeofExp
 *  Build the expression that get the size of pType.
 *  @param pType Type of object.
 *  @return the expression representing the size of pType.
**/
//##54 public Exp
//##54 sizeofExp( Type pType );

/** sizeofExp
 *  Build the expression that get the size of pExp.
 *  @param pExp Expression whose size is to be taken.
 *  @return the expression representing the size of pExp.
**/
//##54 public Exp
//##54 sizeofExp( Exp pExp );

/** functionExp
 *  Build a function expression node that computes function value.
 *  The function may have no return value (void return value).
 *  If pFunctionSpec is (addr SubpNode) or SubpNode, then the
 *  subprogram represented by it is added to the call list
 *  of the current subprogram (symRoot.subpCurrent).
 *  The type of resultant expression is
 *    usually the type of return value, but
 *    type of pointed expression if pFunctionSpec is a pointer (to subprogram).
 *  @param pFunctionSpec function specification part which may be either
 *      a function name (SubpNode), or an expression specifying
 *      a function name ((addr SubpNode), etc.).
 *  @param pActualParamList actual parameter list (made by hirList).
 *  @return function expression node having operation code opCall.
**/
//##54 public FunctionExp
//##54 functionExp( Exp pFunctionSpec, IrList pActualParamList );

/** addrExp
 * Build addr expression representing the address of pExp.
 * pExp should be l-value expression such as variable or
 * SubpNode expression.
 * @param pExp l-value expression or SubpNode.
 * @return the addr expression.
 */
public Exp
addrExp( Exp pExp );

//##64 BEGIN
/**
 * conditionalExp builds boolean expression to be used as
 * conditional expression of IfStmt and LoopStmt.
 * This will transform pExp to comparison expression
 * if it is not so that conversion to LIR become easy.
 * @param pExp boolean expression.
 * @return pExp if pExp is a comparison expression or
 *         return comparison expression transformed from pExp.
 */
public Exp
conditionalExp( Exp pExp );
//##64 END

/** callStmt
 *  Build a subprogram call statement.
 *  Parameters are the same as those of functionExp.
 *  @param pSubpSpec subprogram specification part which may be either
 *      a subprogram name (SubpNode), or an expression specifying
 *      a subprogram name ((addr SubpNode), etc.).
 *  @param pActualParamList actual parameter list (made by hirList).
 *  @return function expression node having operation code opCall.
**/
//##54 public ExpStmt
//##54 callStmt( Exp pSubpSpec, IrList pActualParamList );

/** assignStmt
 *  Build an assignemnt statement.
 *  @param pLeft left hand side (l-value) expression to which
 *      the value of expression is to be assigned.
 *  @param pRight expression (subtree) that is to be assigned
 *      to pLeftSide.
 *  @return the subtree of the built statement
 *      with statement body operator assignCode.
 **/
//##54 public AssignStmt
//##54 assignStmt( Exp pLeft, Exp pRight );

/** ifStmt
 *  Build an if-statement with then-part (pThenPart) and
 *  else-part (pElsePart).
 *  Internal labels (then-label and else-label) are generated
 *  to be attached to the then-part and else-part.
 *  If pThenPart or pElsePart is null, a LabeledStmt with null
 *  statement body is generated as then-part or else-part.
 *  (null else-part can be deleted by deleteUselessLabeledStmtOfHir()
 *  of coins.flow.DeleteUnusedLabels1.)
 *  @param pCondition conditional expression subtree.
 *  @param pThenPart then-part statement that is executed when
 *      pCondition is true.
 *  @param pElsePart else-part statement that is executed when
 *      pCondition is false.
 *  @return the subtree of the built statement
 *      with statement body operator OP_IF;
 *      If else-part is not given, it is treated as null.
**/
//##54 public IfStmt
//##54 ifStmt( Exp pCondition, Stmt pThenPart, Stmt pElsePart );

/* whileStmt (simple)
 * Build while-loop statement.
 * If it contains break or continue statement, use getLoopSteplabel()
 * or getLoopEndLabel() of LoopStmt interface to get the label
 * to be used in the jump statement corresponding to the continue
 * or break statement.  User may also use the general
 * whileStmt method that has pLoopStepLabel and pLoopEndLabel.
 * @param pCondition Loop start condition expression.
 * @param pLoopBody Loop body to be repetitively executed.
 * @return WhileStmt subtree.
**/
//##54 public WhileStmt
//##54 whileStmt( Exp pCondition, Stmt pLoopBody );

/* whileStmt (general)
 * Build while-loop statement.
 * If user want to make a jump statement corresponding to break or
 * continue statement, generate pLoopStepLabel and pLoopEndLabel by
 * symRoot.symTableCurrent.generateLabel() and use the labels in
 * the jump statement.
 * @param pLoopBacklabel Label to be attached to the
 *    loop body.
 * @param pCondition Loop start condition expression.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pLoopStepLabel Label to which continue statement should jump.
 * @param pLoopEndLabel Label to whicjh break statement should jump.
 * @return WhileStmt subtree.
**/
public WhileStmt
whileStmt( Label pLoopBackLabel, Exp pCondition, Stmt pLoopBody,
           Label pLoopStepLabel, Label pLoopEndLabel );

/* forStmt (simple)
 * Build for-loop statement.
 * If it contains break or continue statement, use getLoopSteplabel()
 * or getLoopEndLabel() of LoopStmt interface to get the label
 * to be used in the jump statement corresponding to the continue
 * or break statement.  User may also use the general
 * forStmt method that has pLoopStepLabel and pLoopEndLabel.
 * @param pInitStmt Loop initiation statement that is executed
 *    before testing the loop start condition.
 * @param pCondition Loop start condition expression.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pStepPart Step part statement that is executed
 *    at the first of the next iteration.
 * @return forStmt subtree.
**/
//##54 public ForStmt
//##54 forStmt( Stmt pInitStmt, Exp pCondition,
//##54          Stmt pLoopBody, Stmt pStepPart );

/* forStmt (general)
 * Build for-loop statement.
 * @param pInitStmt Loop initiation statement that is executed
 *    before testing the loop start condition.
 * @param pLoopBacklabel Label to be attached to the
 *    loop body.
 * @param pCondition Loop start condition expression.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pLoopStepLabel Label to which break statement should jump.
 * @param pStepPart Step part statement that is executed
 *    at the first of the next iteration.
 * @param pLoopEndLabel Label to whicjh break statement should jump.
 * @return forStmt subtree.
**/
public ForStmt
forStmt( Stmt pInitStmt, Label pLoopBackLabel, Exp pCondition,
         Stmt pLoopBody, Label pLoopStepLabel, Stmt pStepPart,
         Label pLoopEndLabel );

/* repeatStmt (simple)
 * Build repeat-while-true-loop statement.
 * If it contains break or continue statement, use getLoopSteplabel()
 * or getLoopEndLabel() of LoopStmt interface to get the label
 * to be used in the jump statement corresponding to the continue
 * or break statement.  User may also use the general
 * repeatStmt method that has pLoopStepLabel and pLoopEndLabel.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pCondition Loop end condition expression.
 *         (if true, repeat to execute pLoopBody.)
 * @return repeatStmt subtree.
**/
//##54 public RepeatStmt
//##54 repeatStmt( Stmt pLoopBody, Exp pCondition );

/* repeatStmt (general)
 * Build repeat-while-true-loop statement.
 * @param pLoopBacklabel Label to be attached to the
 *    loop body.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pLoopStepLabel Label to which break statement should jump.
 * @param pCondition Loop end condition expression.
 *         (if true, repeat to execute pLoopBody.)
 * @param pLoopEndLabel Label to whicjh break statement should jump.
 * @return repeatStmt subtree.
**/
  public RepeatStmt
  repeatStmt( Label pLoopBackLabel, Stmt pLoopBody, Label pLoopStepLabel,
             Exp pCondition, Label pLoopEndLabel );


/* untilStmt (simple)
 * Build do-while-loop statement.
 * If it contains break or continue statement, use getLoopSteplabel()
 * or getLoopEndLabel() of LoopStmt interface to get the label
 * to be used in the jump statement corresponding to the continue
 * or break statement.  User may also use the general
 * untilStmt method that has pLoopStepLabel and pLoopEndLabel.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pCondition Loop end condition expression.
 * @return untilStmt subtree.
 * ***deprecated Use repeatStmt.
**/
//##62 public UntilStmt
//##62 untilStmt( Stmt pLoopBody, Exp pCondition );

/* untilStmt (general)
 * Build do-while-loop statement.
 * @param pLoopBacklabel Label to be attached to the
 *    loop body.
 * @param pLoopBody Loop body to be repetitively executed.
 * @param pLoopStepLabel Label to which break statement should jump.
 * @param pCondition Loop end condition expression.
 * @param pLoopEndLabel Label to whicjh break statement should jump.
 * @return untilStmt subtree.
 * ***deprecated Use repeatStmt.
**/
//##62 public UntilStmt
//##62 untilStmt( Label pLoopBackLabel, Stmt pLoopBody, Label pLoopStepLabel,
//##62            Exp pCondition, Label pLoopEndLabel );

/* indexedLoopStmt
 * Build simple indexed-loop (Fortran-type loop) statement.
 * @param pLoopIndex Simple variable that is incremented
 *    by pStepValue at each repetition of the loop
 *    starting from pStartValue toward pEndValue while not exceeding
 *    pEndvalue; Usually it is an integer variable but
 *    floating variable is also permitted.
 * @param pStartValue Start value of the loop index.
 * @param pEndValue End limit of the loop index.
 * @param pStepValue Step value to be used to increment
 *     the loop index; It should represent positive number.
 * @param pLoopBody Loop body to be repetitively executed.
 * @return IndexedLoopStmt subtree.
 * @see IndexedLoopStmt.
**/
public IndexedLoopStmt
indexedLoopStmt( Var pLoopIndex, Exp pStartValue,
  Exp pEndValue, Exp pStepValue, Stmt pLoopBody );

/* indexedLoopStmt
 * Build general indexed-loop (Fortran-type loop) statement.
 * @param pLoopIndex Simple variable that is incremented or
 *    decremented by pStepValue at each repetition of the loop
 *    starting from pStartValue toward pEndValue while not crossinging
 *    pEndvalue; Usually it is an integer variable but
 *    floating variable is also permitted.
 * @param pStartValue Start value of the loop index.
 * @param pEndValue End limit of the loop index.
 * @param pStepValue Step value to be used to increment
 *     the loop index; It should represent positive number.
 * @pUpward true if the loop index is incremented upward,
 *     false if it is decremented downward; If false,
 *     pEndValue is expected to be less or equal to pStartValue.
 * @param pLoopBody Loop body to be repetitively executed.
 * @return IndexedLoopStmt subtree.
 * @see IndexedLoopStmt.
**/
public IndexedLoopStmt
indexedLoopStmt( Var pLoopIndex, Exp pStartValue,
  Exp pEndValue, Exp pStepValue, boolean pUpward, Stmt pLoopBody );

/** labeledStmt
 *  Build labeled statement using pLabel as its label and
 *  pStmt as its statement body.
 *  It is recommended to use attachLabel of Stmt
 *  except in HIR access methods to make a labeled statement.
 *  If you want to use labeledStmt, give null as pStmt parameter.
 *  Relations between pStmt and others (such as previousStmt,
 *  nextStmt, parentNode) are not kept. If it is necessary to
 *  transfer such relations to this LabeledStmt, apply
 *  attachLabel method to pStmt instead of calling labeledStmt.
**/
//##54 public LabeledStmt
//##54 labeledStmt( Label pLabel, Stmt pStmt );

/** blockStmt
 *  Make an instance of block statement that may contain a sequence of
 *  statements.
 *  It is recommended to make an empty block statement by
 *  blockStmt(null) and then add statements by successively calling
 *  addLastStmt method of BlockStmt.
 *  @param pStmtSequence statement to be included in BlockStmt
 *                        (may be null).
 *  @return the resultant BlockStmt.
**/
//##54 public BlockStmt
//##54 blockStmt( Stmt pStmtSequence );

/** returnStmt
 *  Build return statement that terminates the execution of
 *      current subprogram under construction.
 *  @param pReturnValue return value of function subprogram.
 *      If the subprogram has no return value, it is null.
 *  @return the subtree of the built statement
 *      with statement body operator OP_RETURN.
**/
//##54 public ReturnStmt
//##54 returnStmt( Exp pReturnValue );

/** returnStmt
 *  Build return statement that terminates the execution of
 *  current subprogram under construction.
 *  It has no return value.
 *  @return the subtree of the built statement
 *      with statement body operator OP_RETURN.
**/
public ReturnStmt
returnStmt();

/** jumpStmt
 *  Create a jump statement and increment reference count of pLabelSym.
 *  @param pLabelSym jump target label.
 *  @return the created jump statement.
**/
//##54 public JumpStmt
//##54 jumpStmt( Label pLabelSym );

/** switchStmt
 *  Make a SwitchStmt instance.
 *  The jump list pJumpList is a list of pairs of case constant
 *  and case label. Following sequence may create a jump list
 *     created an empty list by irList() of HIR;
 *     generate a case-label and attach it to the statement
 *        corresponding to the case-constant;
 *     add hir.hirSeq(case-constant node, case-label node) to the list;
 *     repeat the generation of case-label and addition to the jump list;
 *  @param pSelectExp Expression to select case statement.
 *  @param pJumpList Jump list that correlate case constant
 *      and case label.
 *  @param pDefaultLabel label to be attached to default statement part.
 *  @param pSwitchBody Switch statement body that contains statements
 *      with case-label.
 *  @param pEndLabel Label as the target of jump corresponding to break.
 *  @return the resultant SwitchStmt.
**/

//##54 public SwitchStmt
//##54 switchStmt( Exp pSelectExp, IrList pJumpList, Label pDefaultLabel,
//##54             Stmt pSwitchBody, Label pEndLabel );

/** expStmt
 *  Create a statement treating pExp as a statement.
 *  ExpStmt appears as loop-condition, call statement, etc.
 *  The loop-condition is an expression but it has label, and so,
 *  it should be ExpStmt. The call statement is a function call
 *  expression treated as a stand alone statement, and so,
 *  it should be ExpStmt.
 *  @param pExp expression to be treated as a statement.
 *  @return the resultant ExpStmt.
**/
//##54 public ExpStmt
//##54 expStmt( Exp pExp );

/** nullStmt
 *  Make a statement having NullNode as its statement body.
 *  @return the null statement.
**/
public Stmt
nullStmt();

//##70 BEGIN
/** asmStmt
 * Make a statement representing
 *  asm( formalParams, instructionList, actualParams ).
 * @param pInstructions String representing formal parameters and
 *    sequence of instructions.
 * @param pActualParamList List of variable nodes and arithmetic expressions
 *    representing actual parameters.
 * @return instance of AsmStmt.
 */
public AsmStmt
asmStmt( String pInstructions,
         HirList pActualParamList );
//##70 END

/** phiExp
 *  Make a phi expression used in SSA.
 *  @param pVar variable to be selected.
 *  @param pLabel label attached to a basic block from which
 *     control tranfers.
 *  @return PhiExp.
**/
public PhiExp
phiExp( Var pVar, Label pLabel );

/** hirSeq Make an HirSeq node that have some definite number of
 *  children.
 *  @param pChild1 Child 1 HIR node.
 *  @return HirSeq with one child.
**/
public HirSeq
hirSeq( HIR pChild1 );

/** hirSeq Make an HirSeq node that have some definite number of
 *  children.
 *  @param pChild1 Child 1 HIR node.
 *  @param pChild2 Child 2 HIR node.
 *  @return HirSeq with 2 children.
**/
//##59 public HirSeq
//##59 hirSeq( HIR pChild1, HIR pChild2 );

/** hirSeq Make an HirSeq node that have some definite number of
 *  children.
 *  @param pChild1 Child 1 HIR node.
 *  @param pChild2 Child 2 HIR node.
 *  @param pChild3 Child 3 HIR node.
 *  @return HirSeq with 3 children.
**/
public HirSeq
hirSeq( HIR pChild1, HIR pChild2, HIR pChild3 );

/** setDataStmt
 * Make the statement that sets initial value or a list of
 * initial values (pValueExp) to a variable (pVariable).
 * The value(s) may be set at loading time (ahead of execution time)
 * depending on execution environment.
 * pVariable is the name of a scalar variable, an array variable,
 * or a structure variable. If pVariable is not a scalar variable,
 * pValueExp may be a list expression generated by expListExp method or
 * a repeat specification generated by expRepeat method.
 * pVariable is an l-value expression that may be a qualified
 * expression or an array element. The qualified expression may
 * appear to set initial values to elements of RegionType variable.
 * The array element may appear in HIR expressions corresponding to
 * Fortran DATA statement.
 * If pVariable is an instance of Var, then a copy of pValueExp
 * is set as the initial value of pvariable in the symbol table.
 * pValueExp or elements of pValueExp should be constant value.
 * See get, getWithRepeat, set methods of ExpListExp.
 * @param pVariable Variable to which initial value is to be set.
 * @param pValueExp Value or list of values to be set to pVariable.
 * @return the statement representing initail value setting.
 */
public SetDataStmt
setDataStmt( Exp pVariable, Exp pValueExp );

/** expList
 * Make an expression representing a list of expressions,
 * that is, make an instance of ExpListExp.
 * The resultant list can be treated as an instance of Exp.
 * Its elements may be an expression made by expRepeat method.
 * @param pList list of expressions.
 * @return expression list.
 */
public Exp
expList( List pList );

/** expRepeat
 * Make an expression representing a list of expressions
 * of the same value.
 * @param pValue Expression representing a value to be repeated.
 * @param pCount Specifies the number of repeat count.
 * @return repetition specification.
 */
public Exp
expRepeat( Exp pValue, Exp pCount );

//==== Primitive methods to make HIR node ====//
//   Not recommmended to be used except in HIR factory methods.

//==== Methods to link children to HIR node ====//
//   Not recommmended to be used except in HIR factory methods.

public void setChildren( IR p1, IR p2 );

public void setChildren( IR p1, IR p2, IR p3 );

/**
 * Make a copy of this HIR node
 * without copying children.
 * @return the copy of this node with null children.
 */
public HIR
hirNodeClone();

//====== Methods for manipulating HIR nodes ======//

/** copyWithOperands
 *  Make a subtree that is the same to this subtree.
 *  (Labels are not renamed.)
 *  In general, a subtree represents the computation of
 *  an operation and also the computation of its operands.
 *  Flow information is invalid as for copied subtree and
 *  should be computed again if required.
 *  If the subtree contains labels, it is recommended to use
 *  copyWithOperandsChangingLabels. Note that, IfStmt, LoopStmt
 *  (for, while, repeat, etc.), SwitchStmt
 *  contain internal labels and it is recommended to use
 *  copyWithOperandsChangingLabels to the subtree that may contain
 *  these statements.
 *  "this" should be the root node of a subtree contained
 *  in a subprogram body. Subprogram body itself should not be copied.
 *  @return the subtree made by the copy operation.
**/
//##54 HIR
//##54 copyWithOperands();

/** copyWithOperandsChangingLabels
 *  Make a new subtree that is the same to this subtree.
 *  If this subtree contains a label definition, then
 *  the label is renamed to avoid conflict.
 *  Flow information is invalid as for copied subtree and
 *  should be computed again if required.
 *  "this" should be the root node of a subtree contained
 *  in a subprogram body. Subprogram body should not be copied.
 *  @param pLabelCorrespondence label correspondence list;
 *      It is usually null; If label correspondence is to be
 *      specified, it should be of the form
 *        (IrList (IrList of original labels) (IrList of new labels) )
 *      "this" subtree should not contain labels listed
 *      in (IrList of new labels) so as escape from infinite
 *      replacement loop; This restriction is satisfied when
 *      pLabelCorrespondence is null;
 *      If this parameter is null, it is computed in this method
 *      and passed to HirModify subclass after calling copyWithOperands
 *      (without changing label).
 *  @return the copied subtree with labels changed.
**/
//##54 public HIR
//##54 copyWithOperandsChangingLabels( IrList pLabelCorrespondence );

/** replaceThisNode
 *  Replace "this" node by pNewNode.
 *  If this has parent, then the link between this and the
 *  parent is changed to the link between pNewNode and the parent.
 *  "this" may be any node that makes sense by replacement.
 *  @param pNewNode node that takes place of "this" node.
**/
//##56 void replaceThisNode( HIR pNewNode );
//##56 HIR replaceThisNode( HIR pNewNode ); //##56


/** getSourceNode1
 *  Get the 1st source operand node of "this" node where the
 *  source is an operand used/refered in "this" operation.
 *  See getSourceNode1 of HIR interface and LIR interface.
 *  @return 1st source operand of "this" node,
 *      return null if "this" has no source operand 1.
 *      If "this" is HIR node, its child 1 is returned.
 *      If "this" is LIR node, the node computing its operand 1
 *      is returned; If there is no such node, null is returned.
**/
HIR getSourceNode1();

/** getSourceNode2
 *  Get the 2nd source operand node of "this" node.
 *  Other matters and conditions are just like those of getSourceNode1.
 *  @return 2nd source operand of "this" node.
 *      return null if "this" has no 2nd source operand.
**/
HIR getSourceNode2();

/** getSourceNode
 *  Get the pNumber-th source operand of "this" node.
 *  Other matters and conditions are just like those of getSourceNode1.
 *  @param pNumber 1: source 1, 2: source 2, 3: source 3, ... .
 *  @return the pNumber-th source operand of "this" node,
 *      return null if "this" has no pNumber-th source operand.
**/
HIR getSourceNode( int pNumber );

/** replaceSource1
 *  Replace the source operand 1 of "this" node by pOperand.
 *  "this" should be a node that can have source operand 1,
 *  that is, nonleaf node if "this" is HIR node, etc.
 *  @param pOperand node that take place of source operand 1.
**/
HIR replaceSource1( HIR pOperand );  //##56

/** replaceSource2
 *  Replace the source operand 2 of "this" node by pOperand.
 *  Other matters and conditions are just like those of
 *  replaceSource1.
 *  "this" should be a node that can have source operand 2.
 *  @param pOperand node that take place of source operand 2.
**/
HIR replaceSource2( HIR pOperand );  //##56

/** replaceSource
 *  Replace pNumber-th source operand of "this" node by pOperand.
 *  Other matters and conditions are just like those of
 *  replaceSource1.
 *  "this" should be a node that can have source operand,
 *  that is, nonleaf node if "this" is HIR node, etc.
 *  @param pNumber 1: source 1, 2: source 2, 3: source 3, ... .
 *  @param pOperand node that take place of source operand.
**/
HIR replaceSource( int pNumber, IR pOperand );  //##56

public void
setChild1( IR p1 );

public void
setChild2( IR p2 );

/** getParent
 *  Get the parent of this node.
 *  @return the parent of this node.
 *      If this has no parent, return null.
**/
//## public IR
//## getParent();

/** setParent
 *  Set the parent of this node as pParent.
 *  This method should be used carefully so that consistency is kept.
 *  It is not recommended to use this method except in
 *  the methods built-in in coins/ir, coins/ir/hir, coins/ir/lir.
 *  Almost all HIR nodes has parent but some LIR node has no parent
 *  in which case, setParent will have no effect.
 *  (LIRTreeList, LIRTree have no parent.)
 *  @param pParent node to be set as parent of this node.
**/
public void              //##54
setParent( IR pParent ); //##54

/** hirClone
 *  Make the clone of this node to get a clone in the situation
 *  where clone() can not be used directly.
 *  @return the clone of this node.
 */
//##54 public HIR
//##54 hirClone()throws CloneNotSupportedException;

/** isTree
 *  Test if this does not violates tree structure, that is,
 *  detect node adherence in branches and
 *  handshake miss in parent-child relation.
 *  Issues message if some node is encountered twice in
 *  the process of traversing this subtree.
 *  If there is duplicated label definition, then return false. //##60
 *  @return true if this a tree else return false.
**/
public boolean
isTree();

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
//##54 public boolean
//##54 isSameAs( HIR pTree );

//##70 BEGIN
/**
 * Check parent-child linkage if debug level (ioRoot.dbgHir.getLevel())
 * is greater than 0. If the linkage is incorrect, issue message.
 * @param pCheckName name of HIR modification operation.
 */
public void
  checkLinkage( String pCheckName );
//##70 END

//==== Iterators to traverse HIR ====//

/** subpIterator
 *  Make an iterator that traverses all subprogram definitions
 *  in the current compile unit.
 *  Sequence of SubpDefinition will be get by using next()
 *  of the iterator successively.
 *  @return the iterator to traverse subprogram definitions.
**/
public Iterator
subpIterator();

/** hirIterator
<PRE>
 *  Get an iterator to traverse all nodes or statements under
 *  pSubtree. YOu can traverse the subtree by following methods
 *  of HirIterator
 *     next(): traverse all nodes.
 *     getNextStmt(): traverse all statements.
 *     getNextExecutableNode(): traverse executable nodes only.
 *  See HirIterator interface.
 *  @param pSubtree root of the subtree to be traversed.
 *  @return teh resultant HirIterator.
</PRE>
**/
//##54 public HirIterator
//##54 hirIterator( IR pSubtree );

//==== Visitor/Acceptor to process HIR ====//

/** accept
 *  Acceptor used in HIR visitor.
 *  See HirVisitor, HirVisitorModel1, HirVisitormodel2.
 *  @param pVisitor HirVisitor
**/
//##54 public void
//##54 accept( HirVisitor pVisitor );

//==== Methods to print HIR node ====//

/** toStringShort
 *  Get text string of this node showing node name and index only.
**/
public String toStringShort();

/** toStringDetail
 *  Get text string of this node showing detail information.
**/
public String toStringDetail();

/** toStringWithChildren
 * Get the string of this node and its children traversing the children
 * in depth-first order.
 * The result is shown in compact form.
 * @return the string of this node and its children.
**/
public String
toStringWithChildren();

/** print
 *  Print this subtree in text format traversing all children
 *  of this node.
 *  "this" may be any subtree (it may be a leaf node).
 *  @param pIndent number of heading spaces for indentation.
 *  @param pDetail true if print all main attributes,
 *         false if some detail attributes are to be omitted.
**/
//##54 public void print( int pIndent, boolean pDetail );

/** getIndentSace
 *  Get a sequence of spaces specified by pIndent.
 *  @param pIndent number of spaces to be generated.
 *  @return a string of spaces.
**/
public String getIndentSpace( int pIndent );

/** setIndexNumberToAllNodes
 *  Set sequencial index number to all nodes traversing the subtree
 *  rooted by this node in depth-first order.
 *  "this" node should be Program or SubpDefinition node. //##51
 *  The index number of this node is set to pStartNumber.
 *  If this node is an instance of SubpDefinition, then the minimum
 *  and the maximum of the index numberes set to the nodes of this
 *  subtree are recorded in SubpDefinition instance so that they
 *  can be accessed by getNodeIndexMin() and getNodeIndexMax()
 *  of SubpDefinition.
 *  @param pStartNumber  starting value of the index
 *            (give integer number greater than 0).  //##51
 *  @return (the last index number) + 1.
**/
//##54 public int
//##54 setIndexNumberToAllNodes( int pStartNumber );

/**
 * Set index number to all nodes and if pResetSymIndex is true,
 * reset Sym index by resetFlowAnalInf for FlowAnalSym nodes.
 * @param pStartNumber
 * @param pResetSymIndex true if resetFlowAnalInf is to be done.
 * @return the largest index number set to nodes.
 */
public int
  setIndexNumberToAllNodes(int pStartNumber, boolean pResetSymIndex); //##62

/**
 * finishHir does closing operations for HIR.
 * finishHir should be called for Program or SubpDefinition
 * when HIR building or restructuring were completed in such
 * timing as
 *   at the end of new HIR generation in front-end
 *    (call for hirRoot.programRoot),
 *   at the end of restructuring (optimization, parallelization, etc.)
 *    (call for corresponding SubpDefinition subtree).
 * If finishHir is scheduled to be called for programRoot after successive
 * generation or restructuring of SubpDefinitions before passing it to other
 * phases, then it is unnecessary to call it for each SubpDefinition.
 * finishHir set index number to all nodes of this subtree
 * and verifies its tree structure.
 * It also certificates the value of getHirPosition() for labels  //##60
 * and buildLabelRefList is called for SubpDefinition
 * (for Program, buildLabelRefList is called for every SubpDefinitions). //##62
 * If finishHir is called then it is not necessary to call
 * setIndexNumberToAllNnodes() nor isTree().
 * If error is found, then finishHir() returns false after isuing
 * error message. If no error is found then it returns true.
 * In either case, processing will continue.
 * @return true if descrepancy is not found, false if descrepancy is found.
 */
//##54 public boolean
//##54 finishHir();  //##61

//====== Constants available in HIR classes ======//

/** Operator number
**/
/* //##54 BEGIN
<PRE>
  public static final int
    OP_PROG          =  1,   // program( .... )
    OP_SUBP_DEF      =  2,   // subpDefinition( .... )
    OP_LABEL_DEF     =  3,   // labelDef( .... )
    OP_INF           =  4,   // infNode( .... )
    OP_CONST         =  5,   // constNode( .... )
    OP_SYM           =  6,   // symNode( .... )
    OP_VAR           =  7,   // varNode( .... )
    OP_PARAM         =  8,   //                UNNECESSARY ?
    OP_SUBP          =  9,   // subpNode( .... )
    OP_TYPE          = 10,   //                UNNECESSARY ?
    OP_LABEL         = 11,   // labelNode( .... )
    OP_ELEM          = 12,   // elemNode( .... )
    OP_LIST          = 14,   // irList( .... )  List of HIR/Sym objects
                             // such as HIR nodes and symbols
    OP_SEQ           = 15,   // hirSeq( .... )
    OP_ENCLOSE       = 16,   // exp( int, Exp ) Enclose subexpression

    OP_SUBS          = 17,   // subscriptedExp( .... )
    OP_INDEX         = 18,   // to be DELETED
    OP_QUAL          = 19,   // qualifiedExp( .... )
    OP_ARROW         = 20,   // pointedExp( .... )

    OP_STMT          = 21,   // Statement code lower. Stmt is
                             // an abstract class and there is
                             // no instance having OP_STMT.
    OP_LABELED_STMT  = 21,   // labeledStmt( .... )
                             // Use the same number as Stmt
                             // because Stmt is an abstract class.
    OP_ASSIGN        = 22,   // assignStmt( .... )
    OP_IF            = 23,   // ifStmt( .... )
    OP_WHILE         = 24,   // whileStmt( .... )
    OP_FOR           = 25,   // forStmt( .... )
    OP_REPEAT        = 26,   // repeatStmt( .... )
    OP_UNTIL         = 26,   // untilStmt( .... ) // to be DELETED
    OP_INDEXED_LOOP  = 27,   // indexedLoopStmt( .... )

    OP_JUMP          = 28,   // jumpStmt( .... )
    OP_SWITCH        = 32,   // switchStmt( .... )
    OP_CALL          = 33,   // callStmt( .... )
    OP_RETURN        = 34,   // returnStmt( .... )
    OP_BLOCK         = 35,   // blockStmt( .... )
    OP_EXP_STMT      = 36,   // expStmt( .... ) expression statement
    OP_ASM           = 37,   // asmStmt //##70
    OP_STMT_UPPER    = 37,   // Upper limit of statement operators

// OP_ADD ... OP_XOR OP_CMP_EQ ... OP_CMP_LE OP_SHIFT_LL ... OP_SHIFT_LL
//         are binary operators used in exp(int, Exp, Exp).
    OP_ADD           = 38, // Add
    OP_SUB           = 39, // Subtract
    OP_MULT          = 41, // Multiply
    OP_DIV           = 42, // Divide
    OP_MOD           = 43, // Modulo

    OP_AND           = 46, // Bitwise and, boolean and
    OP_OR            = 47, // Bitwise or, boolean or
    OP_XOR           = 48, // Bitwise exclusive or, boolean exclusive or

    OP_CMP_EQ        = 51, // Compare equal
    OP_CMP_NE        = 52, // Compare not equal
    OP_CMP_GT        = 53, // Compare greater than
    OP_CMP_GE        = 54, // Compare greater or equal
    OP_CMP_LT        = 55, // Compare less than
    OP_CMP_LE        = 56, // Compare less or equal

//  OP_SHIFT_L       = 57, // Deleted because real machines have not such instr.
    OP_SHIFT_LL      = 58, // Sfift logical and shift arithmetic may be
                           // properly used by seeing whether operand type
                           // is unsigned or signed, but some languages such
                           // as Java use different operator for logocal
                           // and arithmetic shift.
    OP_SHIFT_R       = 59, // Shift right arithmetic
    OP_SHIFT_RL      = 60, // Shift right logical

//## OP_LG_NOT       = 61, // Use OP_NOT, or generate
                           // (OP_CMP_EQ e false) instead of (OP_LG_NOT e)
    OP_NOT           = 62,   // exp(OP_NOT, Exp)
                             // or conditionalExp(OP_NOT, Exp) of HIR //##64
    OP_NEG           = 63,   // exp(OP_NEG, Exp)
    OP_ADDR          = 64,   // exp(OP_ADDR, EXP )
    OP_CONV          = 65,   // convExp( .... )
    OP_DECAY         = 66,   // decayExp( .... )
    OP_UNDECAY       = 67,   // undecayExp( .... )
// OP_CONTENTS, OP_SIZEOF are unary operator used in exp(int, Exp).
    OP_CONTENTS      = 68,   // contentsExp( .... )
    OP_SIZEOF        = 70,   // sizeofExp( .... )
//## OP_NOCHANGE_EXP = 71,   // See FLAG_NOCHANGE
    OP_SETDATA       = 71,
    OP_PHI           = 72,  // phiExp( .... )
    OP_NULL          = 73,  // nullNode();

//## OP_ADD_A        = 74,  // DELETE
//## OP_SUB_A        = 75,  // DELETE

// OP_OFFSET ... OP_LG_OR are binary operators used in exp(int, Exp, Exp).
    OP_OFFSET        = 76,  // exp( int, Exp, Exp ) // HIR-C only
    OP_LG_AND        = 77, // HIR-C only
    OP_LG_OR         = 78, // HIR-C only
    OP_SELECT        = 79, // HIR-C only
    OP_COMMA         = 80, // HIR-C only
    OP_EQ_ZERO       = 81, // ! // HIR-C only
    OP_PRE_INCR      = 82, // HIR-C only
    OP_PRE_DECR      = 83, // HIR-C only
    OP_POST_INCR     = 84, // HIR-C only
    OP_POST_DECR     = 85, // HIR-C only
    OP_ADD_ASSIGN    = 86, // HIR-C only
    OP_SUB_ASSIGN    = 87, // HIR-C only
    OP_MULT_ASSIGN   = 88, // HIR-C only
    OP_DIV_ASSIGN    = 89, // HIR-C only
    OP_MOD_ASSIGN    = 90, // HIR-C only

    OP_SHIFT_L_ASSIGN= 91, // HIR-C only
    OP_SHIFT_R_ASSIGN= 92, // HIR-C only
    OP_AND_ASSIGN    = 93, // HIR-C only
    OP_OR_ASSIGN     = 94, // HIR-C only
    OP_XOR_ASSIGN    = 95, // HIR-C only
    //////////////////////////////////// S.Fukuda 2002.9.12 begin
    OP_EXPLIST       = 96, // Expression representing a list of expressions.
    OP_EXPREPEAT     = 97  // Repetition of the same values.
                           // OP_EXPLIST and OP_EXPREPEAT are used only in
                           // OP_SETDATA expression.
    //////////////////////////////////// S.Fukuda 2002.9.12 end
    ;
</PRE>
*/ //##54 END

  public static final String[]      // Operator name for printing.
    OP_CODE_NAME = {          "none00  ",
      "prog    ", "subpDef ", "labelDef", "inf     ", "const   ",
      "sym     ", "var     ", "param   ", "subp    ", "type    ",
      "label   ", "elem    ", "none13  ", "list    ", "seq     ",
      "enclose ", "subs    ", "index   ", "qual    ", "arrow   ",
      "labeldSt", "assign  ", "if      ", "while   ", "for     ",
      "repeat  ", "indxLoop", "jump    ", "none29  ", "none30  ",
      "none31  ", "switch  ", "call    ", "return  ", "block   ",
      "expStmt ", "asm     ", "add     ", "sub     ", "none40  ", //##70
      "mult    ", "div     ", "mod     ", "none44  ", "none45  ",
      "and     ", "or      ", "xor     ", "none49  ", "none50  ",
      "cmpEq   ", "cmpNe   ", "cmpGt   ", "cmpGe   ", "cmpLt   ",
      "cmpLe   ", "none57  ", "shiftLl ", "shiftR  ", "shiftRl ",
      "none61  ", "not     ", "neg     ", "addr    ", "conv    ",
      "decay   ", "undecay ", "contents", "none69  ", "sizeof  ",
      "setData ", "phi     ", "nullNode", "none74  ", "none75  ",
      "offset  ", "lgAnd   ", "lgOr    ", "select  ", "comma   ",
      "eqZero  ", "preIncr ", "preDecr ", "postIncr", "postDecr",
      "addAsgn ", "subAsgn ", "multAsgn", "divAsgn ", "modAsgn ",
      "sftLAsgn", "sftRAsgn", "andAsgn ", "orAsgn  ", "xorAsgn ",
      //////////////////// S.Fukuda 2002.9.12 begin
      "expList ", "expRep  "
      //////////////////// S.Fukuda 2002.9.12 end
    };

  public static final String[]      // Operator name with no space
    OP_CODE_NAME_DENSE = {          "none00"  ,
      "prog"    , "subpDef" , "labelDef", "inf"     , "const"   ,
      "sym"     , "var"     , "param"   , "subp"    , "type"    ,
      "label"   , "elem"    , "none13"  , "list"    , "seq"     ,
      "enclose" , "subs"    , "index"   , "qual"    , "arrow"   ,
      "labeldSt", "assign"  , "if"      , "while"   , "for"     ,
      "repeat"  , "indxLoop", "jump"    , "none29"  , "none30"  ,
      "none31"  , "switch"  , "call"    , "return"  , "block"   ,
      "expStmt" , "asm"     , "add"     , "sub"     , "none40"  , //##70
      "mult"    , "div"     , "mod"     , "none44"  , "none45"  ,
      "and"     , "or"      , "xor"     , "none49"  , "none50"  ,
      "cmpEq"   , "cmpNe"   , "cmpGt"   , "cmpGe"   , "cmpLt"   ,
      "cmpLe"   , "none57"  , "shiftLl" , "shiftR"  , "shiftRl" ,
      "none61"  , "not"     , "neg"     , "addr"    , "conv"    ,
      "decay"   , "undecay" , "contents", "none69"  , "sizeof"  ,
      "setData" , "phi"     , "nullNode", "none74"  , "none75"  ,
      "offset"  , "lgAnd"   , "lgOr"    , "select"  , "comma"   ,
      "eqZero"  , "preIncr" , "preDecr" , "postIncr", "postDecr",
      "addAsgn" , "subAsgn" , "multAsgn", "divAsgn" , "modAsgn" ,
      "sftLAsgn", "sftRAsgn", "andAsgn" , "orAsgn"  , "xorAsgn" ,
      //////////////////// S.Fukuda 2002.9.12 begin
      "expList" , "expRep"
      //////////////////// S.Fukuda 2002.9.12 end
    };

/** Flag numbers applied to HIR nodes
**/
/* //##54 BEGIN
<PRE>
static final int        // Flag numbers used in setFlag/getFlag.
                        // They should be a number between 1 to 31.
  FLAG_NOCHANGE = 1,    // This subtree should not be
                        // changed in optimization.
  FLAG_C_PTR    = 2,    // The operation (add, sub) is
                        // pointer operation peculiar to C.
  FLAG_CONST_EXP= 3,    // This subtree is a constant expression.
                        // (constant, address constant, null,
                        //  sizeof fixed-size-variable,
                        //  expression whose operands are all constant
                        //  expression)
  FLAG_INIT_BLOCK= 4,   //  BlockStmt made for setting initial
                        //  values. This flag is set for BlockStmt
                        //  only.
  FLAG_LOOP_WITH_CONDITIONAL_INIT = 5;
                        // LoopStmt with non-null conditionalInitPart
                        // (irreducible loop that may be treated
                        // as a tamed loop.)

    // Flag numbers 24-31 can be used in each phase for apbitrary purpose.
    // They may be destroyed by other phases.
</PRE>
*/ //##54 END

//====== Methods to be deleted ======//



//====== Deleted items ======//

// conditionalInitPart
// index operator ##39
// cast operator  ##10

/** getFlowAnalSym
 *  Get the flow analysis symbol assigned to this node if it is given.
 *  FlowAnalSym is either Var, Reg, or ExpId.  (See coins.sym.FlowAnalSym.)
 *  @return the flow analysis symbol assigned to this node,
 *      or return null if it is not assigned.
**/
FlowAnalSym
getFlowAnalSym();

/** toString
 *  Get text representation of this node
 *  without traversing children.
 *  @return the text string representing the node
 *      including operation name, type, node index,
 *      and symbol name if this represents a symbol or constant.
**/
 String
 toString();

/** getIrName
 *  Get operation name and node index
 *  to display node in compact form for flow analysis, debug, etc.
**/
String getIrName();

//====== Methods moved from IR ======//

/** setIndex
 *  Set an index number to "this" node.
 *  (The index number is used to refer the node in data flow analysis.)
 *  @param pIndex the index number to be assigned to "this" node.
**/
void
setIndex( int pIndex ); //##54

//##70 BEGIN
/**
 * Copy the contents of InfList attached to pFromHir
 * to the InfList of this node. If this node has Inf
 * of the same kind as that of pFromHir, then it is
 * replaced by that of pFromHir. If this node has Inf
 * that is not included in pFromHir, then the Inf
 * remains unchanged.
 * If pFromHir has no Inf, then do nothing.
 * @param pFromHir HIR node that may have InfList.
 */
public void
copyInfListFrom( HIR pFromHir );

/**
 * Get the string image of Inf.
 * If there is no Inf, return "".
 * @return the string image of Inf.
 */
public String
  getInfString();
//##70 END

} // HIR interface
