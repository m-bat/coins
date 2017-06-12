// How to write a local transformer:
// 1. Prepare a class for your local transformer implementing interface "LocalTransformer".
// 2. Give code for methods required by the interface: 
//    - "name" and "subject" methods derived from interface "Transformer"
//    - two "doIt" methods derived from interface "LocalTransformer"
//    Notice that you do not have to consider the Transformer
//    because the LocalTransformer has inherited the Transformer.  

// The name of a package is "coins.ssa"
package coins.ssa;

// The folloing packages would be at least necessary for a flow analysis.
import coins.backend.LocalTransformer;
import coins.backend.Data;
import coins.backend.Function;
import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiLink;
import coins.backend.lir.LirNode;
import coins.backend.util.ImList;
import coins.backend.cfg.FlowGraph;

// Implement LocalTransformer
public class PrintFlow implements LocalTransformer {

    // It is convenient to record the following environment and symbol table. 
    private SsaEnvironment env;
    private SsaSymTab sstab;

    public PrintFlow(SsaEnvironment e, SsaSymTab tab) {
	env = e;
	sstab = tab;
    }

    // Set a name and a brief explanation of your optimizer!
    public String name() { return "PrintFlow"; }
    public String subject() {
	return "Printing a control flow graph.";
    }

    // Use the other doIt method for your convenience!
    public boolean doIt(Data data, ImList args) { return true; }

    // The doIt is called by LirOptDriver. 
    // Parameters function and args are also provided by LirOptDriver.
    public boolean doIt(Function function,ImList args) {
	// making a control graph.
	FlowGraph flow = function.flowGraph();

	// Method "first"/"last" of "BiList" returns the first/last cell of 
	//   a bidirectional list structure which is often used in COINS. 
	// Each cell, which has type "BiLink", is used as a contener for the objects with various types.
	// Methods of the BiLink are as follows:
	// Method "elem" returns a content of a cell as a object with type Object.
	// Method "atEnd" returns true if a current cell is the last cell; 
	//   otherwise it returns false;
	// Method "next" is used if you would like to visit cells forwardly.
	// Method "prev" is used if you would like to visit cells backwardly.

	//Method basicBlkList of the FlowGraph returns the BiList with basic blocks.
	for(BiLink bbl = flow.basicBlkList.first(); !bbl.atEnd(); bbl=bbl.next()){
	    // The method elem of the BiLink  returns a content of a cell.
	    // It has to be casted to a suitable type. 
	    // In this case, it is "BasicBlk" which represents the type of a basic block. 
	    BasicBlk bb=(BasicBlk)bbl.elem();

	    // Method "predList" of the BasicBlk returns the BiList with predecessors of bb.
	    for (BiLink pl  = bb.predList().first(); !pl.atEnd(); pl = pl.next()) {
		BasicBlk p = (BasicBlk) pl.elem();

		// Each BasicBlk object has its own id. 
		System.out.println("["+ p.id + "] ->");
	    }
	    // Method "succList" of the BasicBlk returns the BiList with successors of bb.
	    for (BiLink sl  = bb.succList().first(); !sl.atEnd(); sl = sl.next()) {
		BasicBlk s = (BasicBlk) sl.elem();
		System.out.println("-> ["+ s.id + "]");
	    }
	    System.out.println("["+bb.id+"]:");

	    // Method "instrList" of the BasicBlk returns 
	    //   the BiList with statements that consist of nodes with type LirNode.
	    for(BiLink nodel=bb.instrList().first();!nodel.atEnd();nodel = nodel.next()){
		LirNode node = (LirNode)nodel.elem();

		// Method toString of the LirNode prints a tree with "node" as a root.
		System.out.println(node.toString());
	    }
	    System.out.print("\n");
	}

	// If you have modified the program, you have to touch it.
	//f.flowGraph().touch();

	// Finally, the "doIt" has to return true.
	return(true);

    }
}
	
