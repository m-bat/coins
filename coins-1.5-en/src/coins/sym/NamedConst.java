/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;


//========================================//

public interface
NamedConst extends Const
{

/** getConstValue  (##5)
 *  Get the constant value assigned to this named constant.
**/
public Const
getConstValue();

/** getIndexValue
 *  Get the index value assigned to this named constant.
**/
int getIndexValue();

} // NamedConst
