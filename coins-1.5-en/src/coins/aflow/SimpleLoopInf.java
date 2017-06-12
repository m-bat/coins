/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * SimpleLoopInf.java
 *
 * Created on January 6, 2004, 3:40 PM
 */

package coins.aflow;

import java.util.Set;
/**
 *
 * @author  hasegawa
 */
public class SimpleLoopInf
{
    private Set fBBlocks;
    private BBlock fHeader;
    private SimpleLoopInf f1stChild, fNextBrother, fParent;
    
    /** Creates a new instance of SimpleLoopInf */
    public SimpleLoopInf()
    {
    }
    
    public Set getBBlocks()
    {
        return fBBlocks;
    }
    
    public BBlock getEntryBBlock()
    {
        return fHeader;
    }
        
    
    public SimpleLoopInf getFirstChild()
    {
        return f1stChild;
    }
    
    public SimpleLoopInf getNextBrother()
    {
        return fNextBrother;
    }
    public SimpleLoopInf getParent()
    {
        return fParent;
    }
    
}
