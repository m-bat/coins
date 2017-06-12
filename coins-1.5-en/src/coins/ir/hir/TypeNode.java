/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Type;

/**
 * Type name node interface.
**/
public interface
TypeNode extends SymNode {

/**
 * Get the data type of the node.
 * @return the data type.
 */
public Type
getType();

} // TypeNodeImpl class
