/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;



/**
 * <p>Title: Analyzer</p>
 * <p>Description: Interface for analyzer classes. Class that
 * implements this interface supports the automatic analysis mechanism.</p>
 *
 *
 * @author unascribed
 * @version 1.0
 * @see SelfCollectingResults
 */
public interface Analyzer
{
    /**
     * Performs the analysis.
     *
     */
    void find(SelfCollectingResults pResults);
    
    /**
     * Performs the analysis with given argument.
     *
     * @param pObject argument object.
     */
    void find(Object pObject, SelfCollectingResults pResults);
    
    /**
     * Performs the analysis with the given arguments.
     */
    void find(Object pObj, Object pObj0, SelfCollectingResults pResults);
    
    /**
     * Performs the analysis.
     */
    void find();
    
    /**
     * Performs the analysis with the given argument.
     */
    void find(Object pObj);
    
    /**
     * Performs the analysis with the given argument and option.
     */
    
    //        void find(Object pObj, AnalOptions pOpts);
    
    /**
     * Performs the analysis with the given arguments.
     */
    void find(Object pObj, Object pObj0);
    
    /**
     * Performs the analysis with the given arguments and option.
     */
    
    //        void find(Object pObj, Object pObj0, AnalOptions pOpts);
}
