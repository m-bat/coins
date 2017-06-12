/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.IrList;

/** SubpType interface
**/
public interface
SubpType extends Type {

/** getParamTypeList
 *  Get the parameter type list of this SubpType.
 *  Before calling this method, either closeSubpHeader() or
 *  closeSubpPrototype() sould be called for corresponding subprogram.
 *  If not, returned value may be incorrect.
 *  @return parameter type list.
**/
public IrList
getParamTypeList();

/** getReturnType
 *  Get return value type.
 *  @return the return-value type of this subprogram.
**/
public Type
getReturnType();

/** hasOptionalParam
 *  @return true if this subprogram has optional parameter
 *          (represented by ... in C)
 *          otherwise return false.
**/
public boolean
hasOptionalParam();

/** permitAnyParam
 *  @return true if this subprogram permit any number of parameters
 *      of any type are permitted (in such case as
 *        extern sub();
 *        sub(a); sub(a, b);
 *      in old C language style).
**/
public boolean
hasNoParamSpec(); //##53

} // SubpType
