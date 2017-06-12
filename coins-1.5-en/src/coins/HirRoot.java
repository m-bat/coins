/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// HirRoot.java

package coins;

import java.util.Iterator;

import coins.ir.IrList;
import coins.ir.IrListImpl;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl;
import coins.ir.hir.HirModify;
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;
import coins.sym.Sym;

/**
<PRE>
 *  HirRoot class is used to access HIR information and
 *  information prepared by other classes such as Sym, Flow, etc.
 *  This gives a trigger for accessing intermediate representation
 *  information such as the root of HIR tree, root of symbol table,
 *  and so on.
 *  HirRoot inherits ioRoot, symRoot, etc. from IrRoot.
 *  All HIR objects contain a reference to the HirRoot object
 *  from which intermediate representation information and methods
 *  can be quickly accessed.
 *  The HirRoot object contains a reference to IoRoot and SymRoot.
 *  Thus, every HIR objects can access information and methods of
 *  SymRoot, Sym and IoRoot.
 *
*** Coding rules in HirRoot
 *
 * Methods begin with lower case letter.
 * Constants (final static int, etc.) are spelled in upper case letters.
 * Indentation is 2 characters.
 * Formal parameters begin with p.
 * Local variables begin with l.
 * Methods and variables are named so that meaning is easily guessed.
 * Short names less than 3 characters are not used except for
 * very local purpose.
 * Protected or private fields starts with character f.
 * Public fields of xxxRoot class might not start with character f.
</PRE>
**/
public class
HirRoot extends IrRoot
{

  /** Record the HirRoot instance so that it can be refered from others.
  **/
  public final HirRoot
    hirRoot;

  /** hir: HIR instance used to invoke HIR methods in such way as
   *    hirRoot.hir.assignStmt(variableNode, expressionNode).
  **/
  public final HIR
    hir;

  /** sym is an instance of Sym class used to access
   *  Sym methods such as creation of Sym instance, etc.
   *  from othe classes in such way as
   *    hirRoot.symRoot.sym.defineVar("abc".intern(), symRoot.typeInt).
  **/
  public final Sym
    sym;

  /**  hirModify: Used to modify HIR trees.
  **/
  public final HirModify
    hirModify;

  /** hirSubpDefinition: Root of HIR tree representing current
   *                     subprogram definition.
  **/
  public SubpDefinition
    hirSubpDefinition = null;

  /** parser: Used to invoke parser methods.
  **/
  //SF040217 public Parser
  //SF040217  parser = null; //##19

  /** toHirC: Used to invoke ToHirC methods.
  **/
  //SF031217 public ToHirC
  //SF031217   toHirC = null; //##15

  /** toHirBase: Used to invoke ToHirBase methods.
  **/
  //SF031217 public ToHirBase
  //SF031217   toHirBase = null; //##15

  /** fFlowRoot: Used to invoke Flow methods.
  **/
  private FlowRoot
    fFlowRoot;

  private IrList
    fEmptyIrList = null;

//##51 BEGIN

/** Reference to SourceLanguage copied from SymRoot. **/
public final SourceLanguage // Used to invoke methods
  sourceLanguage;           // that depend on source language.

/** Reference to MachineParam copied from ioRoot*/
public final MachineParam
  machineParam;

//##51 END

//==== Constructor and other methods ====//

/** Constructor of the HirRoot class.
 *  @param pSymRoot: Reference of SymRoot to be shared
 *      beteen all objects of HIR.
**/
public
HirRoot( SymRoot pSymRoot )
{
  super(pSymRoot);
  hirRoot  = this;
  hir      = (HIR)(new HIR_Impl(this));
  sym      = pSymRoot.sym;
  fHirRoot = this;
  programRoot = (Program)(hir.program(null, symRoot.symTableRoot,
                                      null, null));
  ir          = programRoot;
  hirModify   = new HirModify(hirRoot); //##8
  machineParam   = pSymRoot.ioRoot.machineParam; //##51
  sourceLanguage = pSymRoot.sourceLanguage;      //##51
  ((HIR_Impl)hir).setParameters(machineParam, sourceLanguage); //##51
} // HirRoot

/** getFlowRoot: Get FlowRoot to access Flow information from HIR.
 *  It will be set by attachFlowRoot when FlowRoot information
 *  become available.
**/
public FlowRoot
getFlowRoot()
{
  return fFlowRoot;
}

/** attachFlowRoot: Attach FLowRoot information when it become available.
**/
public void
attachFlowRoot( FlowRoot pFlowRoot )
{
  fFlowRoot = pFlowRoot;
}

/** linkSubpDefinition: Record the subprogram definition
 *  under current processing.
**/
public void
linkSubpDefinition( SubpDefinition pSubpDefinition )
{
  hirSubpDefinition = pSubpDefinition;
}

/** getSubpDefinition: Get the subprogram definiton
 *  under current processing.
**/
public SubpDefinition
getSubpDefinition()
{
  return hirSubpDefinition;
}

public Iterator
emptyIterator()  //##8 Moved from HIR_Impl
{
  if (fEmptyIrList == null)
    fEmptyIrList = (IrList)(new IrListImpl(this));
  return (Iterator)fEmptyIrList.iterator();
}

} // HirRoot class

