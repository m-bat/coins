/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.hir.Exp;

/** VectorType
 *  Interface for vector type.
**/
public interface
VectorType extends Type  {

/** getElemType
 *  @return the type of vector element.
**/
public Type
getElemType();

/** getElemCountExp
  * Get the expression representing the number of elements in the
  * vector. If the number of elements is given as constant,
  * then this method returns ConstNode expression representing the value.
 *  @return the expression representing the number of
 *      elements in the vector.
**/
public Exp
getElemCountExp();

/** getElemCount
 *  Get the number of elements in the vector.
 *  If the number is not yet fixed or it is given as expression
 *  containing variable, 0 will be returned.
 *  @return the number of elements in the vector.
  *
**/
public long  
getElemCount();

/** getLowerBoundExp
 *  @return the expression representing the lower bound of
 *      index for this vector.
**/
public Exp
getLowerBoundExp();

/** getLowerBound
 *  @return the lower bound of index for this vector.
 *      If the lower bound is not specified, 0 is assumed.
**/
public long  
getLowerBound();

/** isRectangularArray
 * @return true if this is a vector of constant number of elements
 *     whose type has fixed uniform size,
 *     where fixed uniform size elements are:
 *       basic type (other than PointerType, SubpType)
 *       EnumType
 *       StructType/UnionType whose elements are all fixed uniform size
 *       VectorType with true value for RectangularArray()
**/
public boolean
isRectangularArray();

/** setAlignment 
 * Set alignment value to be equal to the pAlignment which should be
 * greater or equal to the alignment of element type.
 * Default value of alignment is set to the alignment of
 * element type when an instance of VectorType is created.
 * @param pAlignment alignment value to be set.
 */
public void
setAlignment( int pAlignment ); 

} // VectorType interface

