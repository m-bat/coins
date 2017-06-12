/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.sym.Const;

/**
 *
 * Constant node
 *
**/
public interface ConstNode extends SymNode {

/** getConstSym
<PRE>
 *  If      boolean true  node then return symRoot.intConst1
 *  else if boolean false node then return symRoot.intConst0
 *  else if NamedConst node then return corresponding Const symbol.
 *  else return Const attached to this node.
</PRE>
**/
public Const
getConstSym();

/**
 * Get the value of the canstant castint it to integer.
 * @return the constant value as integer.
 */
public int
getIntValue();

/**
 * Get the value of the canstant castint it to long integer.
 * @return the constant value as long integer.
 */
public long
getLongValue();  //##52

/**
 * @return true if it is the integer constant 0,
 *    false otherwize.
 */
public boolean
isIntConst0();   // (##5)

/**
 * @return true if it is the integer constant 1,
 *    false otherwize.
 */
public boolean
isIntConst1();   // (##5)

/**
 * @return true if it is the boolean constant true,
 *    false otherwize.
 */
public boolean
isTrueConstNode();    // (##5)

/**
 * @return true if it is the boolean constant false,
 *    false otherwize.
 */
public boolean
isFalseConstNode();   // (##5)

} // ConstNode interface
