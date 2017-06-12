/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * NodeIterator.java
 *
 * Created on August 17, 2002, 1:38 PM
 */
package coins.aflow;

import coins.ir.IR;


/**
 *
 * @author  hasegawa
 */
public interface NodeIterator {
    boolean hasNext();

    IR next();

    boolean hasPrevious();

    IR previous();

    //	void remove();
}
