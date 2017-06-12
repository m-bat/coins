/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.LinkedList;

import coins.sym.Var;
////////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop]  ref Array Cell
//
//  Array area information cell ?
////////////////////////////////////////////////////////////////////////////////
class RefArrayCell  {
    public Var ArrayName;        // Array symbol.
    public int ArrayAnal;        // DEF or MOD
    public LinkedList  ArrayRef; // List of array references.
    /**
    *
    *
    **/
    RefArrayCell()
    {
        ArrayRef = new LinkedList();
    }
    RefArrayCell(Var pArrayVar)
    {
        ArrayName = pArrayVar;
        ArrayRef = new LinkedList();
    }
}
