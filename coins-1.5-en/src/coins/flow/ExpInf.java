/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import coins.ir.IR;
import coins.sym.ExpId; //##65

import java.util.HashSet;

/** ExpInf
 * Expression information.
 * Linked from ExpId.
**/
public class
ExpInf
{
  public final IR fLinkedIR;
  int fOperationCount;
  final java.util.Set fOperandSet;
  final java.util.Set fOperandSet0;
  boolean fHasCall;
  protected SetRefRepr fSetRefRepr = null; //##62
  protected ExpId fRValueExpId = null; //##65

//==== Constructor ====//

public
ExpInf( IR pIR )
{
  fLinkedIR = pIR;
  fOperationCount = 0;
  fOperandSet  = new HashSet();
  fOperandSet0 = new HashSet();
  fHasCall = false;
} // ExpInf

public java.util.Set
getOperandSet() {
  return fOperandSet;
}

public java.util.Set
getOperandSet0() {
  return fOperandSet0;
}

public int
getNumberOfOperations()
{
  return fOperationCount;
}

public void
setNumberOfOperations( int pOperationCount )
{
  fOperationCount = pOperationCount;
}

public boolean
hasCall()
{
  return fHasCall;
}

public void
setCallFlag()
{
  fHasCall = true;
}

public void
  combineTo(ExpInf pExpInf )
{
  pExpInf.fOperandSet.addAll(fOperandSet);
  pExpInf.fOperandSet0.addAll(fOperandSet0);
  pExpInf.fOperationCount = pExpInf.fOperationCount + fOperationCount;
  pExpInf.fHasCall = pExpInf.fHasCall | fHasCall;
} // combineTo

//##62 BEGIN
public SetRefRepr
  getSetRefRepr()
{
  return fSetRefRepr;
}

public void setSetRefRepr( SetRefRepr pSetRefRepr )
{
  fSetRefRepr = pSetRefRepr;
}
//##62 END

//##65 BEGIN
/**
 * Get the ExpId corresponding to r-value expression
 * having the same form as the l-value expression
 * corresponding to this ExpInf.
 * If this ExpInf does not correspond to l-value, then
 * the result is null.
 * @return the ExpId corresponding to r-value expression.
 */
public ExpId
  getRValueExpId()
{
  return fRValueExpId;
}

public void
  setRValueExpId( ExpId pRValueExpId )
{
  fRValueExpId = pRValueExpId;
}
//##65 END
} // ExpInf

