/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import java.util.Iterator;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.ir.hir.Program;
import coins.ir.hir.SubpDefinition;

/** TestSym class
 *
 *  Test Sym and SymTable.
**/
public class
TestSym  
{

//------ Public fields ------//

  public final IoRoot     // Used to access Root information.
    ioRoot;

  public final SymRoot    // Used to access Root information.
    symRoot;

  public final HirRoot    // Used to access Root information.
    hirRoot;

//====== Constructors ======//

public
TestSym( HirRoot pHirRoot )
{
  ioRoot   = pHirRoot.ioRoot;
  symRoot  = pHirRoot.symRoot;
  hirRoot  = pHirRoot;
  if (ioRoot.dbgSym.getLevel() >= 3) {
    symRoot.symTableRoot.printSymTableAll(symRoot.symTableRoot);
    testSymTable();
    if (ioRoot.dbgSym.getLevel() >= 6)
      testRemove(symRoot.symTableRoot.getFirstChild());
  }
//##  testSymTable0(); //## DELETE
}

//====== Sym and SymTable test methods  ======//

private void
testSymTable()
{
  SymTable      lSymTable;
  coins.sym.Sym lSym, lSym1, lSym2;
  Type     lType;
  SymTableIterator lSymTIterator;
  SymIterator      lSymIterator;
  ioRoot.dbgSym.print(4,
    "\nTest generateDerivedSym & SymTableIterator\n");
  for (lSymTIterator = symRoot.symTableRoot.getSymTableIterator();
       lSymTIterator.hasNext();) {
    lSymTable = lSymTIterator.next();
    ioRoot.dbgSym.print(4, lSymTable.toString() + "\n");
      for (lSymIterator = lSymTable.getSymIterator();
           lSymIterator.hasNext(); ) {
        lSym = lSymIterator.next();
        if ((lSym != null)&&
            (! lSym.getFlag(Sym.FLAG_DERIVED_SYM))) {
          lSym1 = symRoot.symTableRoot.generateDerivedSym(lSym);
          lSym2 = symRoot.symTableRoot.generateDerivedSym(lSym);
          ioRoot.dbgSym.print(4, lSym.toString() + " " +
                                 lSym1.toString() + " " +
                                 lSym2.toString() + "\n");
          if ((lSym.getSymKind() == coins.sym.Sym.KIND_TYPE)&&
              (((Type)lSym).getTypeKind() != Type.KIND_SUBP)) {
            lType = ((Type)lSym).makeConstType();
            ioRoot.dbgSym.print(4, lType.toString() + "\n");
          }
        }
      }
  }
  ioRoot.dbgSym.print(4,  
    "\nTest SymNestIterator symTableRoot\n");
  for (SymNestIterator lIterator = symRoot.symTableRoot.getSymNestIterator();
       lIterator.hasNext(); ) {
    lSym = lIterator.next();
    if (lSym != null)
      ioRoot.dbgSym.print(4, lSym.toString() + "\n");
  }
  coins.ir.IrList lSubpDefList
      = ((Program)hirRoot.programRoot).getSubpDefinitionList();
  Iterator lSubpDefIterator = lSubpDefList.iterator();
  while (lSubpDefIterator.hasNext()) {
    SubpDefinition lSubpDef = (SubpDefinition)(lSubpDefIterator.next());
    ioRoot.dbgSym.print(4,  
      "\nTest SymNestIterator " + lSubpDef.getSubpSym().getName());
    for (SymNestIterator lIterator =
         lSubpDef.getSubpSym().getSymTable().getSymNestIterator();
         lIterator.hasNext(); ) {
      lSym = lIterator.next();
      if (lSym != null)
        ioRoot.dbgSym.print(4, lSym.toString() + "\n");
    }
  }
} // testSymTable

private void
testSymTable0()
{
  SymTable      lSymTable;
  coins.sym.Sym lSym, lSym1, lSym2;
  Type     lType;
  SymTableIterator lSymTIterator;
  SymIterator      lSymIterator;
  ioRoot.dbgSym.print(2,
    "\nTest SymTableIterator\n");
  for (lSymTIterator = symRoot.symTableRoot.getSymTableIterator();
       lSymTIterator.hasNext();) {
    lSymTable = lSymTIterator.next();
    ioRoot.dbgSym.print(2, lSymTable.toString() + "\n");
      for (lSymIterator = lSymTable.getSymIterator();
           lSymIterator.hasNext(); ) {
        lSym = lSymIterator.next();
        ioRoot.dbgSym.print(2, lSym.toString(), " ");
      }
  }
} // testSymTable0

private void
testRemove( SymTable pSymTable )  
{
  SymTable      lSymTable;
  coins.sym.Sym lSym, lSym1, lSym2;
  int           lKind;
  Type          lType;
  String        lName;
  Sym           lOwner, lNewSym;
  SymIterator   lSymIterator;
  if (pSymTable == null)
    return;
  ioRoot.dbgSym.print(2, "\nTest remove", pSymTable.getOwner().toString());
  for (lSymIterator = pSymTable.getSymIterator();
       lSymIterator.hasNext();) {
    lSym = lSymIterator.next();
    if ((lSym != null)&&(! lSym.getFlag(Sym.FLAG_GENERATED_SYM))) {
      ioRoot.dbgSym.print(2, " " + lSym.toString());
      lName = lSym.getName();
      lKind = lSym.getSymKind();
      lType = lSym.getSymType();
      if (lSym.getRecordedIn() != null)
        lOwner= lSym.getRecordedIn().getOwner();
      else
        lOwner = null;
      lSym.remove();
      lNewSym = pSymTable.searchOrAdd(lName, lKind, lOwner, true, true);
      lNewSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
    }
  }
  pSymTable.printSymTableDetail();
} // testRemove

} // TestSym class

