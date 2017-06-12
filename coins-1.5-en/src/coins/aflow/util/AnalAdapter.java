/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

abstract public class AnalAdapter implements Analyzer {
    public void find(SelfCollectingResults pResults) {
        throw new FlowError("Adapter class method invoked.");
    }

    public void find(Object pObj, SelfCollectingResults pResults) {
        throw new FlowError("Adapter class method invoked.");
    }

    public void find(Object pObj, Object pObj0, SelfCollectingResults pResults) {
        throw new FlowError("Adapter class method invoked.");
    }

    public void find() {
        throw new FlowError("Adapter class method invoked.");
    }

    public void find(Object pObj) {
        throw new FlowError("Adapter class method invoked.");
    }

    public void find(Object pObj, Object pObj0) {
        throw new FlowError("Adapter class method invoked.");
    }
}
