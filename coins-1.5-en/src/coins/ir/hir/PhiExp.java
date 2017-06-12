/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;
import coins.sym.Label;
import coins.sym.Type;
import coins.sym.Var;

/** PhiExp
 *  Phi expression interface.
**/
public interface 
PhiExp extends Exp {

public void
addAlternative( Var pVar, Label pLabel ); 
 
public IrList
getVarLabelList();

public Type
getVarType();

} // PhiExp
