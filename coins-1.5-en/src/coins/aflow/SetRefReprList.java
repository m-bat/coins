/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.aflow.util.CoinsList;


/**
 * List of <code>SetRefRepr</code>s that support <code>SetRefReprIterator</code>.
 */
public abstract class SetRefReprList extends CoinsList {
    private BBlock fBBlock;
    private SubpFlow fSubpFlow;
    FlowResults fResults;
	public Flow flow;
	
    private SetRefReprList() {
        super();
    }

    protected SetRefReprList(BBlock pBBlock) {
        this();
        fBBlock = pBBlock;
        fResults = fBBlock.results();
		flow = fResults.flowRoot.aflow;
    }

    /**
     * Returns a <code>BBlock</code> object this <code>SetRefReprList</code> is associated with. If this <code>SetRefReprList</code> is not associated with a <code>BBlock</code> returns <code>null</code>.
     */
    public BBlock getBBlock() {
        return fBBlock;
    }

    /**
     * Returns a <code>SubpFlow</code> object this <code>SetRefReprList</code> is associated with. If this <code>SetRefReprList</code> is not associated with a <code>SubpFlow</code> returns <code>null</code>.
     */

    /*public SubpFlow subpFlow()
    {
            return fSubpFlow;
    }*/
    abstract public SetRefReprIterator setRefReprIterator();

    abstract public SetRefReprIterator setRefReprIterator(int pIndex);

    //	abstract public SetRefReprListIterator setRefReprListIterator();
}
