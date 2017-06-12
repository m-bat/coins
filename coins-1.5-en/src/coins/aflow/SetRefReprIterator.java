/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SetRefReprIterator.java
 *
 * Created on August 28, 2002, 3:20 PM
 */
package coins.aflow;

import coins.aflow.util.CoinsIterator;
import coins.ir.IR;


/**
 * CoinsIterator tailored to SetRefReprList.
 * @author  hasegawa
 */
public interface SetRefReprIterator extends CoinsIterator {
    /**
     *  Removes a <code>SetRefRepr</code> as per to the contract of
     * the { coins.aflow.util.CoinsIterator#remove()},
     * and in the case of HIR, also removes the subtree associated
     * with the removed <code>SetRefRepr</code>.
     */
    public void remove();

    /**
     *  Creates a <code>SetRefRepr</code> that encloses the given
     *  <code>IR</code> object (subtree), and adds the generated
     *  <code>SetRefRepr</code> as per to the contract of the
     * { coins.aflow.util.CoinsIterator#add(Object)}, and in the case
     * of HIR, also adds the given <code>IR</code> subtree to the
     *  appropriate context within the HIR program tree.
     */
    public void add(IR pIR);

    /**
     *  Creates a <code>SetRefRepr</code> that encloses the given
     *  <code>IR</code> object (subtree), and adds the generated
     * <code>SetRefRepr</code> as per to the contract of the
     *  {link coins.aflow.util.CoinsIterator#addAfter(Object)},
     * and in the case of HIR, also adds the given <code>IR</code> subtree
     *  to the appropriate context within the HIR program tree.
     */
    public void addAfter(IR pIR);
}
