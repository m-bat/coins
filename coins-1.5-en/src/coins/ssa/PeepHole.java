// The name of a package is "coins.ssa"
package coins.ssa;

import coins.backend.LocalTransformer;
import coins.backend.Data;
import coins.backend.Function;
import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiLink;
import coins.backend.lir.LirNode;
import coins.backend.util.ImList;
import coins.backend.cfg.FlowGraph;

// Import coins.backend.Op, if you would like to refer kinds of operators.
import coins.backend.Op;

// Implement LocalTransformer
public class PeepHole implements LocalTransformer {

    private SsaEnvironment env;
    private SsaSymTab sstab;

    public PeepHole(SsaEnvironment e, SsaSymTab tab) {
	env = e;
	sstab = tab;
    }

    public String name() { return "PeepHole"; }
    public String subject() {
	return "Simple optimizer using peephole approach";
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public boolean doIt(Function function,ImList args) {
	// making a control graph.
	FlowGraph flow = function.flowGraph();

	for(BiLink bbl = flow.basicBlkList.first(); !bbl.atEnd(); bbl=bbl.next()){
	    BasicBlk bb=(BasicBlk)bbl.elem();

	    // Two continuous statements, "prevNode" and "node", are considered as a peephole,
	    // where prevNode records an immediately previous node of the node
	    BiLink prevNodel = null;
	    for(BiLink nodel=bb.instrList().first();!nodel.atEnd();
		prevNodel = nodel, nodel = nodel.next()){
		if (prevNodel != null) {
		    LirNode node = (LirNode)nodel.elem();
		    LirNode prevNode = (LirNode)prevNodel.elem();

		    // If the peephole matches a pattern: 
		    // (SET (MEM x) (REG r)); (SET (REG r') (MEM x)), where
		    // a subexpression (MEM x) of prevNode has to correspond to one of the node, 
		    // it can be transformed as follows: 
		    // (SET (MEM x) (REG r)); (SET (REG r') (REG r))
		    if (node.opCode == Op.SET && prevNode.opCode == Op.SET &&
			prevNode.kid(0).opCode == Op.MEM && 
                          (prevNode.kid(1).opCode == Op.REG || 
                           prevNode.kid(1).opCode == Op.INTCONST ||
                           prevNode.kid(1).opCode == Op.FLOATCONST) &&
			node.kid(0).opCode == Op.REG && node.kid(1).opCode == Op.MEM && 
			node.kid(1).equals(prevNode.kid(0))) {

			// Printing a statement before transformation for confirmation
			System.out.println(node.toString()+" is ");

			// Transformation of node.
			// The right-hand side is replaced with the left-hand side of prevNode.
			// LirNode has a tree structure. ith child of a node can be extracted through
			// node.kid(i), where i starts from 0.
			// If you would like to replace ith child of a node with node',
			// you can take advantage of node.setKid(i, node'). 
			// At this time, you may not directly use a part of other LirNode as node'.
			// Namely, node' has to be one copied from the part through makeCopy(env.lir). 
			node.setKid(1, prevNode.kid(1).makeCopy(env.lir));

			// Printing a statement after transformation for confirmation
			System.out.println("\treplaced with "+ node.toString());
		    }
		}
	    }
	}

	// If you have modified a control flow graph, you have to touch it.
	flow.touch();

	// The last of "doIt" returns true.
	return(true);
	    
    }
}
