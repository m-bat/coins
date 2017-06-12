/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SetRefReprHir.java
 *
 * Created on July 29, 2002, 11:18 AM
 */
package coins.aflow;

import coins.ir.hir.Stmt;


/**
 *
 * @author  hasegawa
 */
public interface SetRefReprHir extends SetRefRepr {
    /**
     * Returns the <code>Stmt</code> node that originated this <code>SetRefReprHir</code>. This is usually same as <code>getIR()</code>, but is differnet when this <code>SetRefReprHir</code> originates from an <code>IfStmt</code>.
     */
    Stmt getStmt();

    //        public boolean hasCall();
}
