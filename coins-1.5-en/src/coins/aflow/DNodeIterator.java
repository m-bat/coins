/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * DNodeIterator.java
 *
 * Created on February 17, 2003, 1:25 PM
 */
package coins.aflow;

import coins.ir.IR;


/**
 *
 * @author  hasegawa
 */
public interface DNodeIterator {
    public IR next();

    public boolean hasNext();

    public IR skipSubtree();

    //	public booremove();
}
