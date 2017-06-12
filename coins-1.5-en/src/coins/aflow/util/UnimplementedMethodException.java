/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * UnimplementedMethodException.java
 *
 * Created on August 17, 2002, 1:53 PM
 */
package coins.aflow.util;


/**
 * <p>Thrown to indicate that the requested method is not implemented yet.</p>
 * <p>The method needs to be implemented immediately when this exception is thrown.</p>
 *
 *
 * @author  hasegawa
 */
public class UnimplementedMethodException extends FlowError {
    /** Creates a new instance of UnimplementedMethodException */
    public UnimplementedMethodException() {
    }

    public UnimplementedMethodException(String pComment) {
        super(pComment);
    }
}
