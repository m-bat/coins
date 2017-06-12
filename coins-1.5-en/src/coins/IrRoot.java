/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// IrRoot.java

package coins;

import coins.ir.IR;

/** IrRoot class is the super class of HirRoot and LirRoot.
 *  It contains references to IoRoot object and SymRoot object
 *  which are used in accessing information and methods
 *  of IoRoot and Sym.
 *  All HIR objects and LIR objects contain a reference to
 *  IrRoot object.
**/
public class
IrRoot
{

//==== Coding rule ====//
// Protected or private fields starts with character f.
// Public fields of xxxRoot class might not start with character f.

//---- Public fields ----//

  /** ioRoot records the reference to the IoRoot object
   *  got from SymRoot passed as a parameter of costructors
   *  of IrRoot, HirRoot, LirRoot.
   *  It is used in accessing IoRoot information and
   *  invoking IoRoot methods.
  **/
  public final IoRoot
    ioRoot;

  /** symRoot records the reference to the SymRoot object
   *  passed as a parameter of costructors for IrRoot,
   *  HirRoot, LirRoot.
   *  It is used in accessing information and methods of
   *  SymRoot and Sym.
  **/
  public final SymRoot
    symRoot;

  /** irRoot records the instance of IrRoot so as it can be
   *  refered from others.
  **/
  public final IrRoot
    irRoot;

  /** ir records the root of program tree represented in HIR or LIR.
  **/
  public IR
    ir = null;

  /** programRoot records the root of program tree represented
   *  in HIR or LIR.
  **/
  public IR
    programRoot = null;

//---- Protected/private fields ----//

  protected int     fInitiateIrCount = 0;
  protected HirRoot fHirRoot = null; // Set in HirRoot.

//==== Constructor ====//

public            // Constructor of the Root class.
IrRoot()
{
  ioRoot  = null;
  symRoot = null;
  irRoot  = this;
}

/** IrRoot is usually invoked from the constructors of
 *  HirRoot and LirRoot.
 *  @param pSymRoot: Reference of SymRoot to be shared
 *      indirectly beteen all objects of IR (HIR or LIR).
**/
public
IrRoot( SymRoot pSymRoot )
{
  symRoot = pSymRoot;
  ioRoot  = pSymRoot.ioRoot;
  irRoot  = this;
} // IrRoot class

/** getHirRoot:
 *  Get HirRoot reference.
**/
public HirRoot
getHirRoot()
{
  return fHirRoot;
}

} // IrRoot class

