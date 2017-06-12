/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AssignFlowExpId.java
 *
 * Created on July 31, 2002, 2:10 PM
 *
 * Interface for the class that assigns <code>FlowExpId</code>s for nodes representing expressions. The class that implements this interface determines the identifications of such expressions. Such a class covers <code>SubpFlow</code>, which is the scope of the <code>FlowExpId</code>s.
 *
 */
package coins.aflow;

import coins.ir.IR;


/**
 *
 * @author  hasegawa
 */
public interface AssignFlowExpId {
    /**
     * Assigns a <code>FlowExpId</code> to the node <code>pIR</code>.
     *
     * @return the assigned <code>FlowExpId</code>.
     */
    FlowExpId assignToNode(IR pIR);

    /**
     * Assigns <code>FlowExpId</code>s to the entire scope currently under consideration (SubpFlow). This method can be called only once per lifetime of the object of the class that implements this interface, since this interface does not supply a method for clearing the results of the previous assignment.
     */
    void assign();
}
