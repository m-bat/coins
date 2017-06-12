/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

public class FlowError extends Error {
    public FlowError() {
        super();
    }

    public FlowError(String s) {
        super(s);
    }
}
