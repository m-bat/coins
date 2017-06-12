/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir;


/** IrList interfac
 *  List of expressions, symbols, and others used in package ir.
 *  Use LinkedList in java.util for linking objects.
 * Specifications of following methods are the same
 *  to those of List in java.util.
 *    add
 *    get
 *    getFirst
 *    set
 *    isEmpty
 *    size
 *    contains
 *    indexOf
 *    remove
 *    iterator
**/
public interface
IrList extends IR
{

public void
add( Object pElement );

public void
add( int pInsertionPosition, Object pObjectToBeInserted );

public Object
get( int pIndex );

public Object
getFirst();

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

public void
clear();
public java.util.ListIterator
iterator();

/** toStringShort
 *  @return the text representation of this list in short form.
**/

public String
toStringShort();

/** getClone
 * @return a clone of this list.
**/
public IR
getClone()throws CloneNotSupportedException;

} // IrList interface
