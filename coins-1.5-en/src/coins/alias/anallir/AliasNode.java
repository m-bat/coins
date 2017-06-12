/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

import coins.backend.lir.LirSymRef;

public class AliasNode {
    protected String    op;

    protected AliasNode child;

    protected LirSymRef node;

    public AliasNode(AliasNode child) {
        this.op = "MEM";
        this.child = child;
        node = null;
    }

    public AliasNode(LirSymRef node) {
        op = "ADDR";
        child = null;
        this.node = node;
    }

    public boolean isAddr() {
        return op.equals("ADDR");
    }

    public boolean isMem() {
        return op.equals("MEM");
    }

    public void printANode() {
        if (isAddr()) {
            System.out.println(node);
        } else {
            System.out.print(" MEM ");
            child.printANode();
        }
    }
}
