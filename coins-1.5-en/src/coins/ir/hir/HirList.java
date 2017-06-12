/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.ir.IrList;

/** HirList interfac
 <PRE>
 *  HirList is an IrList that contains HIR objects as its elements.
 * Specifications of following methods are the same
 *  to those of List in java.util.
 *    add
 *    isEmpty
 *    size
 *    contains
 *    indexOf
 *    remove
 *    listIterator
</PRE>
**/
public interface
HirList extends HIR, IrList
{

/**
<PRE>
 * Specifications of following methods are the same
 *  to those of List in java.util.
 *    add
 *    isEmpty
 *    size
 *    contains
 *    indexOf
 *    remove
 *    listIterator
</PRE>
**/
public void
add( Object pElement );

public void
add( int pInsertionPosition, Object pObjectToBeInserted );

public Object
getFirst();

public Object
get( int pIndex );

public void
set( int pIndex, Object pElement );

public boolean
isEmpty();

public int
size();

public boolean
contains( Object pObject );

public int
indexOf( Object pObject );

public Object
remove( int pRemovePosition );

public boolean
remove( Object pObject );

public java.util.ListIterator
iterator();

public String
toString();

public void
print(int pIndent);

/** hirListClone
 *  Make the clone of this node to get a clone in the situation
 *  where clone() can not be used directly.
 *  @return the clone of this node.
 */
public HirList
hirListClone()throws CloneNotSupportedException;

} // HirList interface
