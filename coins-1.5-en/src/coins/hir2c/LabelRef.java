/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2c;
import java.util.HashSet;

import coins.ir.hir.SubpDefinition;
import coins.sym.Label;
////////////////////////////////////////////////////////////////////////////////////////////
//
//   HIR-Base  To  C  Language (LabelRef) 
//
////////////////////////////////////////////////////////////////////////////////////////////
public class  LabelRef {
    public HashSet fLabelSet; 
    /**
    *
    * LabelRef:
    *  
    **/
    LabelRef() {
        fLabelSet = new HashSet();
    }
    /**
    *
    * initLabelRef:
    *
    **/
    void initLabelRef( SubpDefinition pSubpDef) {
    }
    /**
    *
    * putLabelRef:
    *
    **/
    void putLabelRef(Label pLabel) {
        fLabelSet.add(pLabel); 
    }
    /**
    *
    * IsLabelRef:
    *
    **/
    boolean IsLabelRef(Label pLabel) {
        return fLabelSet.contains(pLabel); 
    }
}
