/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * FlowExpId.java
 *
 * Created on June 26, 2002, 5:51 PM
 */
package coins.aflow;

import java.util.Set;

import coins.ir.IR;

import coins.sym.ExpId; //##41

/**
 *
 * @author  hasegawa
 */
public interface FlowExpId {
    /**
     * Returns a copy of the tree structure this FlowExpId represents. "Copy" means the node returned is not part of the main IR tree. Each invocation of this method returns the same copy created when this FlowExpId was created, and does not return a fresh copy of the IR tree each time.
     */
    IR getTree();

    /**
     * Returns an instance of the IR node whose associated FlowExpId is this. Such a node is likely a part of the program tree so should not be modified.
     */
    IR getLinkedNode();

    /**
     * Returns the index attached to this FlowExpId. The indexing scope is the same as the scope of this FlowExpId.
     */
    int getIndex();

    /**
     * Returns the set of FlowAnalSyms that are operands of this FlowExpId. An operand of a FlowExpId is simply a FlowAnalSym that is attached to one of the nodes that comprise the tree represented by the FlowExpId.
     */
    Set getOperandSet();

    /**
     * Returns the set of FlowAnalSyms that are operands of this FlowExpId, and that hold the value that may contribute to the result of the computation of this FlowExpId. For example, in HIR, if a symbol node is operated by the addressOf operator, and there is no contentsOf operator operating at all afterwards, the symbol attached to the symbol node is not included in the set returned by this method.
     */
    Set getOperandSet0();

    /**
     * <p>Returns the list of FlowExpIds that are the subexpressions of this FlowExpId, in order of evaluation. For example, in LIR, if this FlowExpId has the form of</p>
     *
     * <p> (ADD:I32 (MEM:I32 (FRAME:I32 (VAR:I32 "a"))) (CONST:I32 1)) </p>
     *
     * this method will return [(VAR:I32 "a"), (FRAME:I32 (VAR:I32 "a")), (MEM:I32 (FRAME:I32 (VAR:I32 "a"))),
     * List getSubexps();
     */
    /**
     * Returns the number of operations this FlowExpId involves. This is not more than but roughly equal to the number of this FlowExpId's linked node's descendant nodes.
     */
    int getNumberOfOperations();

    boolean hasCall();

    DefUseList getDefUseList();

    UDList getUDList();

    public ExpId getExpId(); //##41
    public String toStringShort(); //##51
//##53 BEGIN

/** Set flag showing that corresponding expression is
 * left hand side expression of AssignStmt.
 */
public void setLHSFlag();  //##53

/** Return true if the corresponding expression is
 * left hand side expression of AssignStmt.
 * left hand side expression is not treated as common subexpression
 * in usual case.
 * @return true if setLHSFlag() is called, false otherwise.
 */
public boolean isLHS();    //##53

//##53 END
}
