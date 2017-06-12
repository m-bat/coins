/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


/** PointerType interface
**/
public interface
PointerType extends Type {

/** getPointedType
 *  Get the pointed type of this pointer type.
 *  @return Type the pointed type of this type.
**/
public Type
getPointedType();

/** isDeclaredAsArray
 *  Test if this pointer type is declared as a popinter
 *  representing an array (as it is done in C language).
 *  @return true if it is declared as array, else return false.
**/
public boolean
isDeclaredAsArray();

/** getElemCount
 *  @return the number of elements of the array represented
 *      by this pointer if this pointer represents an array;
 *      return 0 if this pointer does not represent array.
**/
public long  
getElemCount();

/** getLowerBound
 *  @return lower bound of array subscript
 *      if this pointer represents an array;
 *      return 0 if this pointer does not represent array.
**/
public long  
getLowerBound();

} // PointerType interface
