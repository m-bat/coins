/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.IoRoot;
import coins.SourceLanguage;
import coins.SymRoot;

/** SymTableImpl -- Symbol tabel class implementing SymTable.
**/
public class
SymTableImpl implements SymTable
{

//---- Static fields ----//

    private static final int hashTableSize = 499;   // Prime number.

//---- Public fields ----//

  public final SymRoot
    symRoot;     // Used to access Root symbols.

  public final IoRoot
    ioRoot;     // Used to access Root symbols.

//---- Protected/private fields ----//

    public String fTableName = null;    // Name of this symbol table.
                                        // "Root", "Constant" or null.

    /** The parent of this symbol table, null if no parents. **/
    private SymTable tableParent = null;

    /** The brother symbol table **/
    private SymTable tableBrother = null;

    /** The child symbol tables **/
    private SymTable tableFirstChild = null; // The next child is the
                                             // brother of the 1st child.
    private SymTable tableLastChild  = null;

    /** Symbols in this symbol table */
    protected Sym firstSym = null;  // The first symbol in this SymTable.
                                    // Use getNextSym() in Sym to get
                                    // the next symbol.
    protected Sym lastSym  = null;  // The last symbol.
    protected Sym ownerSym = null;  // Symbol that owns this SymTable
                        // (subprogram if this is local to a subprogram).

    /** count: Number of symbols in this symbol table. */
    private int count = 0;

    /** Threshold to do rehash() */
    private int threshold;

    /** Contains symbols of this symbol table. **/
    private SymTableEntry[] fEntries;

    protected final int fDbgLevel; //##67

//==== Constructor ====//

public
SymTableImpl( SymRoot pSymRoot )
{
  symRoot  = pSymRoot;
  ioRoot   = pSymRoot.ioRoot;
  fEntries = new SymTableEntry[ hashTableSize ];
  threshold = ( hashTableSize * 3 ) >> 2; // Threshold to rehash.
  fDbgLevel = ioRoot.dbgSym.getLevel(); //##67
}

//==== Methods for initiating and constructing SymTable ====//

public SymTable
pushSymTable( Sym pOwner )
{
  SymTableImpl lTableNew = new SymTableImpl(symRoot);
  lTableNew.tableParent= this;
  lTableNew.ownerSym = pOwner;
  if( tableLastChild == null )
    tableFirstChild = lTableNew;
  else
    ((SymTableImpl)tableLastChild).tableBrother = lTableNew;
  tableLastChild = lTableNew;
  if (pOwner instanceof Subp) {
    ( (Subp) pOwner).setSymTable( (SymTable) lTableNew);
    symRoot.subpCurrent = (Subp)pOwner;
    symRoot.symTableCurrentSubp = lTableNew;
  }
  symRoot.symTableCurrent = lTableNew;
  if (fDbgLevel > 2) //##67
    ioRoot.dbgSym.print( 3, "pushSymTable",
           symRoot.symTableCurrent.toString());
  return (SymTable)lTableNew;
} // pushSymTable

/** popSymTable
 *  Close this (current) symbol table and make its ancestor symbol
 *  table as the current symbol table if this has the ancestor.
 *  The closed symbol table can be reopened by reopenSymTable.
 *  "this" should be the current symbol table to be closed.
 *  @return the ancestor symbol table that is resumed as
 *      the current symbol table. If this has no ancestor,
 *      then return null.
**/
public SymTable
popSymTable()
{
  if (tableParent != null)
    symRoot.symTableCurrent = tableParent;
  if (symRoot.symTableCurrent == symRoot.symTableRoot) {
    symRoot.symTableCurrentSubp = null;
  }else if( symRoot.symTableCurrent == null ){
    symRoot.symTableCurrentSubp = null;
    return symRoot.symTableRoot;
  }
  if (symRoot.symTableCurrent.getOwner() instanceof Subp) {
    symRoot.subpCurrent = (Subp)symRoot.symTableCurrent.getOwner();
    symRoot.symTableCurrentSubp = symRoot.symTableCurrent;
  }
  if (fDbgLevel > 2) //##67
    ioRoot.dbgSym.print( 3, "popSymTable",
          ioRoot.toStringObject(symRoot.symTableCurrent));
  return symRoot.symTableCurrent;
} // popSymTable

/** reopenSymTable
 *  Push this (current) symbol table as the ancestor symbol table
 *  and make the symbol table specified by the parameter as
 *  the new current symbol table.
 *  "this" should be the current symbol table that is to be made
 *      as the ancestor.
 *  @param pPreviousSymTable a symbol table that was once
 *      a direct descendant symbol table of this symbol table.
 *  @return pPreviousSymTable that is made as the new current
 *      symbol table.
**/
public SymTable
reopenSymTable( SymTable pPreviousSymTable )
{
  if (pPreviousSymTable != null) {
    symRoot.symTableCurrent = pPreviousSymTable;
    if (fDbgLevel > 2) //##67
      ioRoot.dbgSym.print(3, "reopenSymTable",
                        symRoot.symTableCurrent.toString());
    return symRoot.symTableCurrent;
  }else
    return this;
}  // reopenSymTable

public SymTable
getParent()
{
  return tableParent;
}

public SymTable
getFirstChild()
{
  return tableFirstChild;
}

public SymTable
getBrother()
{
  return tableBrother;
}

//==== Methods for recording and finding given symbol ====//

/** defineUnique
 *  Define a new symbol specified by the parameter pUniqueName
 *  in the current symbol table without traversing ancestor symbol tables.
 *  If there is already a symbol with the same name as pUniqueName,
 *  then it is treated as an erronous call and no new symbol is created.
 *  "this" should be the current symbol table.
 *  @param pUniqueName unique string representing the symbol to be defined.
 *  @param pSymKind kind of the symbol to be created (see GlobalConstants).
 *  @param pDefinedIn symbol representing language construct such as
 *      subprogram name or structure name that encloses the definition of
 *      the symbol to be defined. If there is no such construct, specify null.
 *      (See definedIn().)
 *  @return the new symbol having the name same to pUniqueName and whose
 *      symbol kind is pSymKind. If there is already a symbol with the same
 *      name as pUniqueName, then return null.
 *  Note:
 *      If pSymKind is either Sym.KIND_SUBP, Sym.KIND_VAR,
 *      Sym.KIND_ELEM, or Sym.KIND_CONST, etc. then either Subp, Var,
 *      Param, Elem, or Const instance is created each respectively.
 *      Several linkages are made between the defined symbol and
 *      pDefinedIn symbol (see definedIn(), FirstParam(), FirstLocalVar(),
 *      FirstElem(), NextVar(), NextSubp(), etc.)
 *      These rules are applied also to Define and redefine.
**/
public Sym
defineUnique( String pInternedName, int pSymKind, Sym pDefinedIn )
{
  Sym lSym;
  //SF040420 lSym = searchLocal(pInternedName, pSymKind, false);
  lSym = searchLocal(pInternedName, pSymKind, true); //SF040420
  if ((lSym == null)||
      (lSym.getSymKind() == Sym.KIND_REMOVED))
    return searchOrAdd( pInternedName, pSymKind, pDefinedIn,
                        true, true );
  else {
    if (ioRoot.dbgSym.getLevel() > 0)
      ioRoot.msgRecovered.put(1010, "double definition of "
                            + pInternedName);
    return null;
  }
} // defineUnique

/** define
 *  Define a new symbol specified by the parameter pUniqueName
 *  in the current symbol table. If there is already an old symbol with
 *  the same name as pUniqueName and with the same kind as pSymKind
 *  in this symbol table, then no new symbol is created and the old
 *  symbol is returned.
 *  Ancestor symbol tables are not traversed to find the same symbol.
 *  "this" should be the current symbol table.
 *  @param pUniqueName unique string representing the symbol to be defined.
 *  @param pSymKind kind of the symbol to be created (see GlobalConstants).
 *  @param pDefinedIn language construct symbol that encloses the definition
 *      of pUniqueName. If there is no such construct, specify null.
 *  @return the symbol having the name same to pUniqueName and whose
 *      symbol kind is pSymKind. If there is already a symbol with the same
 *      name as pUniqueName and the same kind as pSymKind, then return it.
**/
public Sym
define( String pInternedName, int pSymKind, Sym pDefinedIn )
{
  return searchOrAdd( pInternedName, pSymKind, pDefinedIn, false, true );
} // define

/** search
 *  Search the symbol specified by the parameter pInternedName in the
 *  current symbol table and its ancestors. The symbol is searched in
 *  the current symbol table first, and if not found, then
 *  searched in its direct ancestor, and if not found again,
 *  then searched in ancestor's ancestor, and so on.
 *  If there are more than one symbols having the same name then
 *  the one encountered first in the above search process is returned.
 *  "this" should be the current symbol table.
 *  See searchSymOfThisKind and redefine.
 *  @param pInternedName unique string representing the symbol to be searched.
 *  @return the symbol having the name same to pInternedName,
 *      where the symbol kind of the returned symbol may
 *      differ from that of pUniqueName given this time.
 *      If there is no same symbol then return null.
**/
public Sym
search( String pInternedName )
{
  SymTableEntry ent[] = fEntries;
  //##040204 SymTableImpl lCurrent = (SymTableImpl)symRoot.symTableCurrent;
  SymTableImpl lCurrent = this; //##040204
  SymTableImpl lParent;
  while( lCurrent != null ){
    Sym lSym = lCurrent.searchLocal( pInternedName, 0, false );
    if( lSym != null )
      return lSym;
    lParent = (SymTableImpl)lCurrent.tableParent;
    if (lParent != lCurrent)
      lCurrent = lParent;
    else
      lCurrent = null;
  }
  return null;
} // search

/** searchLocal
 *  Search the symbol which is specified by parameter pInternedName.
 *  If pSpecifiedKind is ture, search the symbol which is same as pInternedName and pSymKind.
 *  And Searching is current symbol table only.
 *  @param pInternedName symbol name which is Interned.
 *  @param pSymKind symbol kind.
 *  @param pSpecifiedKind If ture, search by name and kind.
 *  @return Sym The symbol's name is same as pInternedName. If pSpecifiedKind is ture, kind is also same kind.
**/
public Sym
searchLocal( String pInternedName, int pSymKind, boolean pSpecifiedKind )
{
  if( pInternedName == null )
    return null;
  SymTableEntry ent[] = fEntries;
  Sym currSym = null;
  int index = ( System.identityHashCode( pInternedName )
               & 0x7FFFFFFF ) % ent.length;
  for (SymTableEntry lSearchEntry = ent[index]; lSearchEntry != null;
     lSearchEntry = ((SymTableEntryImpl)lSearchEntry).next) {
    if (((SymTableEntryImpl)lSearchEntry).key == pInternedName) {
      currSym = ((SymTableEntryImpl)lSearchEntry).value;  // Found.
      if (currSym.isRemoved()) {
        currSym = null;
        continue;
      }else {
        if ( pSpecifiedKind == true &&
            ((SymTableEntryImpl)lSearchEntry).value.getSymKind() != pSymKind ){
          currSym = null;
        }
        break;
      }
    }
  }
  return currSym;
} // searchLocal

public SymTableEntry
searchLocalEntry( String pInternedName, int pSymKind,
                      boolean pSpecifiedKind )
{
  if( pInternedName == null )
    return null;
  SymTableEntry ent[] = fEntries;
  Sym currSym = null;
  SymTableEntry lEntry = null;
  int index = ( System.identityHashCode( pInternedName ) & 0x7FFFFFFF )
            % ent.length;
  for (SymTableEntry lSearchEntry = ent[index]; lSearchEntry != null;
       lSearchEntry = ((SymTableEntryImpl)lSearchEntry).next) {
    lEntry = lSearchEntry;
    if (((SymTableEntryImpl)lSearchEntry).key == pInternedName) {
      currSym = ((SymTableEntryImpl)lSearchEntry).value;  // Found.
      if (currSym.isRemoved()) {
        lEntry = null;
        continue;
      }else {
        if ( pSpecifiedKind == true &&
            ((SymTableEntryImpl)lSearchEntry).value.getSymKind() != pSymKind ){
          lEntry = null;
        }
        break;
      }
    }
  }
  return lEntry;
} // searchLocalEntry

/** searchOrAdd
 *  Search the symbol which is specified by parameter pInternedName, pSymKind.
 *  If pWithinThisTable is false search all parents symbol table are as well as searched.
 *  If pSpecifiedKind is ture, search the symbol which is same as pInternedName and pSymKind.
 *  If the symbol is found and then return it.
 *  If not, make new symbol, then add to this symbol table.
 *  @param String pInternedName symbol name which is Interned.
 *  @param int pSymKind symbol kind.
 *  @param boolean pWithinThisTable If ture, search this symbol table only.
 *  @param pSpecifiedKind If ture, search the symbol which is same name as pInternedName and same kind as pSymKind.
 *  @return Sym found symbol or generated symbol.
**/
public Sym
searchOrAdd( String  pInternedName, int pSymKind, Sym pDefinedIn,
             boolean pWithinThisTable, boolean pSpecifiedKind )
{
  Sym currSym, newSym;

  if (pInternedName == null)
    return null;
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 7, "searchOrAdd", pInternedName +
          " " + pSymKind + " " + pWithinThisTable +
          " " + pSpecifiedKind );
  if ((pSymKind >= Sym.KIND_CONST_FIRST)&&
      (pSymKind <= Sym.KIND_CONST_LAST )&&
      ( this != symRoot.symTableConst ) ){
    if (symRoot.symTableConst == null)
      symRoot.symTableConst = new SymTableImpl(symRoot);
    return ((SymTableImpl)symRoot.symTableConst).
            searchOrAdd( pInternedName, pSymKind, null, true, true);
  }
  if ((pSymKind == Sym.KIND_TYPE)&&    // Search a type symbol.  (##6)
      (this != symRoot.symTableRoot)&&
      (symRoot.symTableCurrentSubp != null)&&
      (this != symRoot.symTableCurrentSubp)) { // Search in symTableCurrentSubp.  9
    return ((SymTableImpl)symRoot.symTableCurrentSubp).
            searchOrAdd( pInternedName, pSymKind, pDefinedIn,
                         pWithinThisTable, pSpecifiedKind);

  }
  SymTableEntry ent[] = fEntries;
  int   hash = System.identityHashCode(pInternedName);
  int   index= (hash & 0x7FFFFFFF) % ent.length;
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 7, "index " + index);
  currSym = null;

  for (SymTableEntry lSearchEntry = ent[index]; lSearchEntry != null;
       lSearchEntry = ((SymTableEntryImpl)lSearchEntry).next) {
    if (((SymTableEntryImpl)lSearchEntry).key == pInternedName) {
      currSym = ((SymTableEntryImpl)lSearchEntry).value;  // Found.
      if (currSym.getSymKind() == Sym.KIND_REMOVED) {
        currSym = null;
        continue;
      }
      if ((pSpecifiedKind == true)&&
          (currSym.getSymKind() != pSymKind)&&
          (currSym.getSymKind() != Sym.KIND_OTHER)) {
        // Check allowable combination of new/old symbol kind.
        if (SourceLanguage
            .REDEFINABLE[currSym.getSymKind()][pSymKind] == 0) {
          ioRoot.msgRecovered.put(1010,
                     "Symbol " + currSym.getName() + " of kind "
                      + Sym.KIND_NAME[currSym.getSymKind()] +
                      " can not be redefined as " +
                      Sym.KIND_NAME[pSymKind]);
        }
        currSym = null;
      }
      if (currSym != null)
        break;
    }
  }
  if ( ( currSym == null ) && ( !pWithinThisTable )
       && ( tableParent != null ) ) {
    if (fDbgLevel > 3) //##67
      ioRoot.dbgSym.print( 7, "look parent");
    SymTableImpl lTable = (SymTableImpl)tableParent;
    while( lTable != null ){
      currSym = lTable.searchLocal( pInternedName,
                                    pSymKind, pSpecifiedKind );
      if( currSym != null )
        break;
      lTable = (SymTableImpl)lTable.tableParent;
    }
  }
  if (currSym != null){
    if (fDbgLevel > 3) //##67
      ioRoot.dbgSym.print( 7, " Found " + currSym.getSymKind());
    return currSym;
  }

  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 7, " Not found " + pSymKind);
  switch ( pSymKind ) {
  case Sym.KIND_BOOL_CONST:
    newSym = new BoolConstImpl(symRoot, pInternedName);
    break;
  case Sym.KIND_CHAR_CONST:
    newSym = new CharConstImpl(symRoot, pInternedName, symRoot.typeChar);
             // Set type later.
    break;
  case Sym.KIND_INT_CONST:
    newSym = new IntConstImpl(symRoot, pInternedName, symRoot.typeInt);
             // Set type later.
    break;
//## Other type constants:
  case Sym.KIND_FLOAT_CONST:
    newSym = new FloatConstImpl(symRoot, pInternedName, symRoot.typeFloat);
             // Set type later.
    break;
  case Sym.KIND_STRING_CONST:
    newSym = new StringConstImpl(symRoot, pInternedName);
    break;
  case Sym.KIND_NAMED_CONST:
    newSym = new NamedConstImpl(symRoot, pInternedName, 0); // Set index later.
    break;
  case Sym.KIND_VAR:
    newSym = new VarImpl(symRoot, pInternedName );
             // Set type later.
    break;
  case Sym.KIND_PARAM:
    newSym = new ParamImpl(symRoot, pInternedName , pDefinedIn );
             // Set type later.
    break;
  case Sym.KIND_ELEM:
    newSym = new ElemImpl(symRoot, pInternedName , pDefinedIn );
             // Set type later.
    break;
//**        case Sym.KIND_FIELD:
//**            newSym = new Field(symRoot, pInternedName , pDefinedIn );
//**            break;
  case Sym.KIND_SUBP:
    newSym = new SubpImpl( symRoot, pInternedName, null, pDefinedIn );
             // Set type later.
    break;
  case Sym.KIND_TYPE:    // Defined type name.
                         // Basic type is not treated here.
                         // (see baseType in SymImpl)
    newSym = new TypeImpl(symRoot);
    ((TypeImpl)newSym).fName = pInternedName;
    break;
  case Sym.KIND_LABEL:
    newSym = new LabelImpl( symRoot, pInternedName, pDefinedIn );
    break;
  case Sym.KIND_EXP_ID:
    newSym = new ExpIdImpl( symRoot, pInternedName, pDefinedIn );
    break;
  case Sym.KIND_OTHER:
    newSym = new SymImpl( symRoot, pInternedName );
    break;
  case Sym.KIND_AREG:
    newSym = null;
    break;
  case Sym.KIND_MREG:
    newSym = null;
    break;
  default:
    newSym = new SymImpl( symRoot, pInternedName );
    ((SymImpl)newSym).setSymKind(pSymKind); ///////////// S.Fukuda 2001.12.12
    break;
  }
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 7, " newSym " + newSym.getName() );
  if (count >= threshold) {
    rehash();
    ent = fEntries;
    index= (hash & 0x7FFFFFFF) % ent.length;
  }
  SymTableEntry lEntry2 = new SymTableEntryImpl(pInternedName,
                                          newSym, ent[index]);
  ent[index] = lEntry2;
  count++;
  linkSym(newSym);
  newSym.setDefinedIn( pDefinedIn );
  return newSym;
} // searchOrAdd

public SymTableEntry
searchOrAddEntry( String  pInternedName, int pSymKind, Sym pDefinedIn,
             boolean pWithinThisTable, boolean pSpecifiedKind )
{
  Sym currSym, newSym;
  SymTableEntry lEntry = null;

  if (pInternedName == null)
    return null;
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 6, "searchOrAddEntry",
          pInternedName + " " + pSymKind
           + " " + pWithinThisTable + " " + pSpecifiedKind );
  if ((pSymKind >= Sym.KIND_CONST_FIRST)&&
      (pSymKind <= Sym.KIND_CONST_LAST )&&
      ( this != symRoot.symTableConst ) ){
    if (symRoot.symTableConst == null)
      symRoot.symTableConst = new SymTableImpl(symRoot);
    return ((SymTableImpl)symRoot.symTableConst).searchOrAddEntry(
            pInternedName, pSymKind, null, true, true);
  }
  SymTableEntry ent[] = fEntries;
  int   hash = System.identityHashCode(pInternedName);
  int   index= (hash & 0x7FFFFFFF) % ent.length;
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 6, " index " + index);
  currSym = null;

  ///////////////////////////////////////////////////////// S.Fukuda
  for( SymTableEntry lEntry1=ent[index]; lEntry1!=null;
       lEntry1=((SymTableEntryImpl)lEntry1).next )
    if( ((SymTableEntryImpl)lEntry1).key==pInternedName ) // Found.
    {
      if( pSpecifiedKind==true
      &&  ((SymTableEntryImpl)lEntry1).value.getSymKind()!=pSymKind )
        continue; // Continue if type differs.
      if (fDbgLevel > 3) //##67
        ioRoot.dbgSym.print( 6, " Found.  " + pSymKind);
      return lEntry1;
    }
  if( pWithinThisTable==false )
  {
    if (fDbgLevel > 3) //##67
      ioRoot.dbgSym.print( 6, " Look parent.");
    for( SymTable lTable=tableParent; lTable!=null;
         lTable=((SymTableImpl)lTable).tableParent )
    {
      SymTableEntry lEntry2 = ((SymTableImpl)lTable).searchLocalEntry(
                            pInternedName, pSymKind, pSpecifiedKind );
      if ((lEntry2==null)||
          ((SymTableEntryImpl)lEntry2).value.getSymKind()
            == Sym.KIND_REMOVED)
        continue;
      if (fDbgLevel > 3) //##67
        ioRoot.dbgSym.print( 6, " Found.  " + pSymKind);
      return lEntry2;
    }
  }
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 6, " Not found , and define "
          + pSymKind);
  ////////End Fukuda

  if (count >= threshold) {
    rehash();
    ent = fEntries;
    index= (hash & 0x7FFFFFFF) % ent.length;
  }
  SymTableEntry lEntry3 = new SymTableEntryImpl(pInternedName, null,
                                                ent[index]);
  ent[index] = lEntry3;
  count++;
  return lEntry3;
} // searchOrAddEntry

public void
linkSym( Sym pNewSym )
{
  if (lastSym == null) {
    firstSym = pNewSym;
    lastSym  = pNewSym;
  }
  else {
    ((SymImpl)lastSym).fNextSym = pNewSym;
    lastSym = pNewSym;
  }
  pNewSym.setRecordedIn(this);
} // linkSym

/** searchSymOfThisKind
 *  Search for the symbol having the same name as the parameter pSym and
 *  its kind is same to pSymKind  in the same manner as Search.
 *  If one is found then it is returned. If not found, then
 *  null is returned.
 *  "this" should be the current symbol table.
 *  @param pSym a symbol having the same name as the one to be searched.
 *  @param pSymKind kind of the symbol to be searched (see GlobalConstants).
 *  @return the symbol whose name and kind are the same to those of
 *      pSym, or return null if not found.
**/
public Sym
searchSymOfThisKind( Sym pSym, int pSymKind)
{
  SymTableEntry ent[] = fEntries;
  String lName = pSym.getName( );
  int index = ( System.identityHashCode( lName )
               & 0x7FFFFFFF ) % ent.length;

  SymTableImpl lCurrent = (SymTableImpl)symRoot.symTableCurrent;
  while( lCurrent != null ){
    Sym lSym = lCurrent.searchLocal( lName, pSymKind, true );
    if( lSym != null )
      return lSym;
    lCurrent = (SymTableImpl)lCurrent.tableParent;
  }
  return null;
} // searchSymOfThisKind

/** redefine
 *  Create a new symbol that has the name same as this symbol but
 *  having the symbol kind indicated by the parameter pSymKind
 *  in the current symbol table.
 *  "this" should be the current symbol table.
 *  Usage of redefine:
 *    If the source language permits the same name to be defined
 *    as a name of different kind in the same scope, then Search may
 *    return a symbol having the same name but with different kind.
 *    In that case, search ancestors again by searchSymOfThisKind
 *    and if not found, then use redefine to define the symbol
 *    as the new one of pSymKind. For example, enumerator may have
 *    the same name as that of some subprogram in C.
 *  @param pSymKind symbol kind of the symbol to be created.
 *  @param pDefinedIn language construct symbol that encloses the definition
 *      of pSym. If there is no such construct, specify null.
 *  @return the created symbol.
**/
public Sym
redefine( Sym pSym, int pSymKind, Sym pDefinedIn )
{
  String lName = pSym.getName( );
  return searchOrAdd( lName, pSymKind, pDefinedIn, true, true );
} // redefine

/** rehash
 *  Do rehashing when count exceeds threshold.
**/
private void
rehash( )
{
  int   oldSize = fEntries.length;
  int   newSize = (oldSize << 1) + 1;  // newSize = oldSize*2 + 1.
  int   index;
  SymTableEntry[] oldEnt = fEntries;
  SymTableEntry[] newEnt = new SymTableEntry[newSize];
  threshold = (newSize * 3) >> 2;      // threshold = (newSize*3)/4
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 4, "rehash " + this.toString(),
     "oldSize " + oldSize + " newSize " +
     newSize + " threshold " + threshold);
  fEntries   = newEnt;
  for (int i = oldSize; i-- > 0; ) {   // Copy from old entry to new entry.
    // Copy downward so that "next" chain can be set easily.
    SymTableEntry old = oldEnt[i];
    while (old != null) {
      SymTableEntry lEntry = old;
      old = ((SymTableEntryImpl)old).next;
      if (((SymTableEntryImpl)lEntry).value != null) {
        if (((SymTableEntryImpl)lEntry).value.getSymKind()
            == Sym.KIND_REMOVED)
          continue;
        index = (System.identityHashCode(
          ((SymTableEntryImpl)lEntry).key) & 0x7FFFFFFF) % newSize;
        ((SymTableEntryImpl)lEntry).next = newEnt[index];
        newEnt[index] = lEntry;
      }
      else {
        ioRoot.dbgSym.print(1, " NULL in rehash " + this.toString());
      }
    }
  }
} // rehash

public SymTable
subpSymTable()
{
  SymTable lSymTable;
  lSymTable = this;
  while ((lSymTable != null)&&
         (lSymTable.getOwner() != null)&&
         (lSymTable.getOwner().getSymKind() != Sym.KIND_SUBP)) {
    lSymTable = lSymTable.getParent();
  }
  if ((lSymTable == null)||(lSymTable.getOwner() == null)||
      (lSymTable.getOwner().getSymKind() != Sym.KIND_SUBP))
    lSymTable = symRoot.symTableRoot;
  return lSymTable;
} // subpSymTable

/** generateVar
 *  Generate a variable of type pType in the symbol table
 *  of current subprogram symRoot.subpCurrent
 *  (to be used as temporal variable, etc.).
 *  If subpCurrent is null, the variable is recorded in
 *  symTableRoot.
 *  @param pType type of the variable to be generated.
 *  @return the generated variable.
**/
public Var
generateVar( Type pType )
{
  SymTable lSymTable = subpSymTable();
  VarImpl  varSym;
  if (lSymTable == null)
    lSymTable = symRoot.symTableRoot;
  return lSymTable.generateVar( pType, symRoot.subpCurrent );
} // generateVar

/** generateVar
 *  Generate a variable of type pType in this symbol table
 *  (to be used as temporal variable, etc.).
 *  "this" should be the current symbol table.
 *  @param pType type of the variable to be generated.
 *  @param pDefinedIn language construct symbol that encloses the
 *      definition of the generated variable. If there is no such
 *      construct, specify null.
 *  @return the generated variable.
**/
public Var
generateVar( Type pType, Sym pDefinedIn )
{
  SymTable lSymTable = subpSymTable();
  VarImpl  varSym;
  //##51 while ( lSymTable.search(("_var" + symRoot.getVarCount()).intern()) != null)
  //##51   symRoot.incrementVarCount();
  while (symRoot.conflictingSpecialSyms.contains(            //##51
         ("_var" + symRoot.incrementVarCount()).intern()));  //##51
  varSym = (VarImpl)lSymTable.searchOrAdd(("_var" +
                symRoot.getVarCount()).intern(),
                Sym.KIND_VAR, pDefinedIn, true, true);
  varSym.fType = pType;
  varSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
  symRoot.incrementVarCount();
  return (Var)varSym;
} // generateVar

public Param
generateParam( Type pType, Sym pDefinedIn )
{
  SymTable lSymTable = subpSymTable();
  ParamImpl lParamSym;
//##51  while ( lSymTable.search(("_param" +
//##51              symRoot.incrementParamCount()).intern()) != null)
//##51    ;
  while (symRoot.conflictingSpecialSyms.contains(                //##51
         ("_param" + symRoot.incrementParamCount()).intern()));  //##51
  lParamSym = (ParamImpl)lSymTable.searchOrAdd(("_param" +
                symRoot.getParamCount()).intern(),
                Sym.KIND_PARAM, pDefinedIn, true, true);
  lParamSym.fType = pType;
  lParamSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return (Param)lParamSym;
} // generateParam

public Elem
generateElem( Type pType, Sym pDefinedIn)
{
  ElemImpl lElemSym;
  while (symRoot.conflictingSpecialSyms.contains(              //##51
         ("_elem" + symRoot.incrementElemCount()).intern()));  //##51
  lElemSym = (ElemImpl)searchOrAdd(
               ("_elem" + symRoot.getElemCount()).intern(),
                Sym.KIND_ELEM, pDefinedIn, true, true);
  lElemSym.fType = pType;
  lElemSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return (Elem)lElemSym;
} // generateElem

public Label
generateLabel()
{
  SymTable lSymTable = subpSymTable();
  LabelImpl lLabelSym;
  while (symRoot.conflictingSpecialSyms.contains(                //##51
         ("_lab" + symRoot.incrementLabelCount()).intern()));  //##51
  lLabelSym = (LabelImpl)lSymTable.searchOrAdd(("_lab" +
                symRoot.getLabelCount()).intern(),
                Sym.KIND_LABEL, null, true, true);
  lLabelSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return (Label)lLabelSym;
} // generateLabel

////////////////////////////////////////////////// S.Fukuda 2001.12.20 begin
public Sym
generateTag()
{
  SymTable lSymTable = subpSymTable();
  String lTagName;
  Sym lTag;
  do
    lTagName = ( "_tag"+symRoot.incrementSymCount() ).intern();
  while (symRoot.conflictingSpecialSyms.contains(lTagName));  //##51
  lTag = lSymTable.searchOrAdd( lTagName, Sym.KIND_TAG, null, true, true );
  lTag.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return lTag;
} // generateTag

public Sym
generateTag(String pTagName)
{
  Sym lTag;
  //SF031217 SymTable lSymTable = subpSymTable();
  SymTable lSymTable = this; //SF031217
  lTag = lSymTable.searchOrAdd( pTagName, Sym.KIND_TAG, null, true, true );
  lTag.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return lTag;
} // generateTag

/** generateSym
 *  Generate a symbol of type pType in this symbol table
 *  (to be used as block name, etc.).
 *  "this" should be the current symbol table.
 *  @param pType type of the symbol to be generated.
 *  @param pSymKind symbol kind number of the symbol to be generated.
 *  @param pPrefix prefix to be attached to the symbol name.
 *  @param pDefinedIn language construct symbol that encloses the
 *      definition of the generated variable. If there is no such
 *      construct, specify null.
 *  @return the generated symbol.
 *  As for variable, parameter, element, label,
 *  use generateVar, generateParam, generateElem, generateLabel.
**/
public Sym
generateSym( Type pType, int pSymKind, String pPrefix, Sym pDefinedIn )
{
  SymTable lSymTable = subpSymTable();
  SymImpl lSym;
  String  lPrefix = "_" + pPrefix;
  while (symRoot.conflictingSpecialSyms.contains(             //##51
         (lPrefix + symRoot.incrementSymCount()).intern()));  //##51
  lSym = (SymImpl)lSymTable.searchOrAdd((lPrefix +
                               symRoot.getSymCount()).intern(),
                pSymKind, pDefinedIn, true, false);
  lSym.fType = pType;
  lSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return (Sym)lSym;
} // generateSym

public Sym
generateDerivedSym( Sym pBaseSym )
{
  SymImpl lSym;
  String  lBaseName;
  SymInf  lSymInf;
  lSymInf = pBaseSym.getOrAddInf();
  lBaseName = "_" + pBaseSym.getName() + "_";
  while (symRoot.conflictingSpecialSyms.contains(                     //##51
         (lBaseName + lSymInf.incrementDerivedSymCount()).intern())); //##51
  lSym = (SymImpl)searchOrAdd((lBaseName +
                               lSymInf.getDerivedSymCount()).intern(),
                pBaseSym.getSymKind(), null, true, false);
  lSym.fType = pBaseSym.getSymType();
  lSym.setFlag(Sym.FLAG_DERIVED_SYM, true);
  lSym.setFlag(Sym.FLAG_GENERATED_SYM, true);
  return (Sym)lSym;
} // generateDerivedSym

//##70 BEGIN
/**
 * Generate symbol name which is unique in this SymTable
 * and its ancestors.
 * @param pHeader is a string to be used as header of the generated name.
 * @return the generated name
 */
public String
generateSymName( String pHeader )
{
  String lSymName = "";
  for (int i = 0; i < 1000; i++) {
    lSymName = (pHeader + "_" + i).intern();
    if (search(lSymName) != null) {
      // Same name was found. Skip to the next one.
      continue;
    }
    // Unique name was found
    break;
  }
  return lSymName;
} // generateSymName


//##70 END

public SymIterator
getSymIterator()
{
  return (SymIterator)(new SymIteratorImpl(this));
} // getSymIterator

public SymNestIterator
getSymNestIterator()
{
  return (SymNestIterator)(new SymNestIteratorImpl(this));
} // getSymNestIterator

public SymTableIterator
getSymTableIterator()
{
  return (SymTableIterator)(new SymTableIteratorImpl(this));
} // getSymTableIterator

public Sym
getFirstSym()
{
  Sym lSym = firstSym;
  while ((lSym != null)&&
         lSym.isRemoved()) {
    lSym = lSym.getNextSym();
  }
  return lSym;
}

public boolean
isInThisSymTable( Sym pSym )
{
  if (pSym == null)
    return false;
  if (searchLocal(pSym.getName().intern(), pSym.getSymKind(), true)
                  != null)
    return true;
  else
    return false;
} // isInThisSymTable

public String
toString()
{
  String lString = "";
  if ((fTableName != null)
      &&(fTableName != "")) //##78
    lString = fTableName;
  else if (ownerSym != null)
    lString = ownerSym.getName();
  else if (this == symRoot.symTableConst)
    lString = "Const";
  return "SymTable " + lString;
} // toString

public void
printSymTableAll( SymTable pSymTable )
{
  if (pSymTable == null)
    return;
  pSymTable.printSymTable();
  printSymTableAll(((SymTableImpl)pSymTable).tableFirstChild);
  printSymTableAll(((SymTableImpl)pSymTable).tableBrother);
} // printSymTableAll

//##70 BEGIN
/**
 *
 */
public void
printSymTableAllDetail( )
{
  symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
  symRoot.symTableConst.printSymTableDetail();
  if (fDbgLevel > 2) {
    SymTable lSymTable = symRoot.symTableUnique;
    ioRoot.printOut.print("\n\nsymTableUnique\n");
    for (Sym lSym = lSymTable.getFirstSym(); lSym != null;
         lSym = ((SymImpl)lSym).fNextSym) {
      if (lSym.getOriginalSym() != lSym) {
        String lDefinedInName = lSym.getOriginalSym().getDefinedInName();
        if ((lDefinedInName != null)&&(lDefinedInName != ""))
          lDefinedInName = " in " + lDefinedInName;
        else
          lDefinedInName = "";
        ioRoot.printOut.print("\n  " + lSym.toStringDetail() +
          " original " + lSym.getOriginalSym().getName() +
          lDefinedInName);
      }
    }
  }
} // printSymTableAllDetail

//##70 END

public void
printSymTableAllDetail( SymTable pSymTable )
{
  if (pSymTable == null)
    return;
  pSymTable.printSymTableDetail();
  printSymTableAllDetail(((SymTableImpl)pSymTable).tableFirstChild);
  printSymTableAllDetail(((SymTableImpl)pSymTable).tableBrother);
} // printSymTableAllDetail

public void
printSymTable()
{
  String lString = toString();
  if (tableParent != null)
    lString = lString + " parent " + tableParent.toString();
  ioRoot.printOut.print("\n" + lString + "\n");
  for (Sym lSym = firstSym; lSym != null;
       lSym = ((SymImpl)lSym).fNextSym) {
    if (!(lSym instanceof ExpId))
      ioRoot.printOut.print("\n  " + lSym.toString());
    if (lSym.isRemoved())
      ioRoot.printOut.print(" REMOVED ");
  }
  ioRoot.printOut.print("\n");
} // printSymTable

/** printSymTableDetail
 *  Print this symbol table.
 *  printDetail: After executing printSymTable(), print other attributes.
 *  "this" may be any symbol table.
**/
public void
printSymTableDetail()
{
  String lString = toString();
  if (tableParent != null)
    lString = lString + " parent " + tableParent.toString();
  if (getSubp() != null)
    lString = lString + " subp " + getSubp().toString();
  ioRoot.printOut.print("\n" + lString + "\n");
  for (Sym lSym = firstSym; lSym != null;
       lSym = ((SymImpl)lSym).fNextSym) {
    if ((!(lSym instanceof ExpId))||
        (ioRoot.dbgSym.getLevel() >= 2)) {
      ioRoot.printOut.print("\n  " + lSym.toStringDetail());
      if (ioRoot.dbgSym.getLevel() > 6) {
        if (lSym.getRecordedIn() != null)
          ioRoot.printOut.print(" owner " + lSym.getRecordedIn().getOwnerName());
        else
          ioRoot.printOut.print(" owner not given");
      }
    }
    if (lSym.isRemoved())
      ioRoot.printOut.print(" REMOVED ");
  }
}

public Sym getOwner()
{
  return ownerSym;
}

public String
getOwnerName()
{
  if (ownerSym != null)
    return ownerSym.getName();
  else
    return "null";
}


public Subp
getSubp()
{
  SymTable lSymTable = this;
  while ((lSymTable != null)&&(lSymTable.getOwner() != null)) {
    if (lSymTable.getOwner().getSymKind() == Sym.KIND_SUBP)
      return (Subp)lSymTable.getOwner();
    lSymTable = lSymTable.getParent();
  }
  return null;

} // getSubp

public int
getSymCount( )
{
  return count;
}

////////////////////////////////////////////////////////// S.Fukuda
// Get hash table index.
private int
getHashIndex(String name)
{                          // Fukuda
  return ( System.identityHashCode(name)&0x7FFFFFFF )%fEntries.length;
}

//Add instance of SymTableEntry
private void
newEntry(Sym s)
{                  // Fukuda
  if( count++>=threshold )
    rehash();
  String     name = s.getName();
  int       index = getHashIndex(name);
  SymTableEntry e = new SymTableEntryImpl(name,s,fEntries[index]);
  fEntries[index] = e;
  linkSym(s);
} // newEntry

/** searchOrAddSym
 * Add symbol s to this symbol table.
 * If there is already the symbol in this table, then return it
 * without adding the symbol.
 * //## previous version public Sym entry(Sym s)
 * @param s Symbol to be searched or added.
 * @return the symbol found or added.
 */
public Sym
searchOrAddSym(Sym s)
{             // Fukuda
  if(s==null)
    return null;
  Sym old = searchLocal( s.getName(), s.getSymKind() );
  if( old!=null )
  {
    if (fDbgLevel > 3) //##67
      ioRoot.dbgToHir.print( 9, "old", ""+s );
    return old;
  }
  newEntry(s);
  if (fDbgLevel > 3) //##67
    ioRoot.dbgToHir.print( 9, "new", ""+s );
  return s;
} // searchOrAddSym

/** search
 * Search a symbol named pName starting from this symbol table
 * and upward (ancestors).
 * @param pName name of the symbol to be serached.
 * @param symKind symbol kind (See Sym.KIND_xxx).
 * @return the symbol if found, null otherwise.
 */
public Sym
search(String pName,int symkind)
{                               // Fukuda
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 8, "search ", pName + " " );
  for( SymTable t=this; t!=null; t=t.getParent() )
  {
    Sym s = t.searchLocal(pName,symkind);
    if(s!=null)
      return s;
  }
  return null;
} // search

/** searchLocal
 * Search a symbol named pName withis this symbol table
 * without traversing other symbol table.
 * @param pName name of the symbol to be serached.
 * @param symKind symbol kind (See Sym.KIND_xxx).
 * @return the symbol if found in this symbol table, null otherwise.
 */
public Sym
searchLocal(String pName,int symkind)
{                                    // Fukuda
  if (fDbgLevel > 3) //##67
    ioRoot.dbgSym.print( 8, "searchLocal ", pName + " " );
  if(pName==null)
    return null;
  for( SymTableEntryImpl e = (SymTableEntryImpl)fEntries[getHashIndex(pName)];
       e!=null;
       e = (SymTableEntryImpl)e.next )
    if( e.key==pName && e.value.getSymKind()==symkind )
      return e.value;
  return null;
} // searchLocal

/** searchTableHaving
 * Search a symbol symbol table containing the symbol named pName
 * starting from this symbol table and upward (ancestors).
 * @param s Symbol to be searched if it is contained in some
 *     symbol table.
 * @return the symbol table containing the symbol s if found, null otherwise.
 */
public SymTable
searchTableHaving(Sym s)
{                        // Fukuda
  if( s==null)
    return null;
  String name = s.getName();
  int symkind = s.getSymKind();
  for( SymTable t=this; t!=null; t=t.getParent() )
    if( t.searchLocal(name,symkind)!=null )
      return t;
  return symRoot.symTableRoot;
} // searchTableHas

public String
makeNewName(String pOldName, String lSubpName, int index)
{           // Chen
  String lNewName,sindex;

  if(lSubpName == ""){
    lNewName = pOldName;
  }
  else {
    //##70 lNewName = pOldName.concat("_");
    //##70 lNewName = lNewName.concat(lSubpName);
    lNewName = lSubpName.concat("_"); //##70
    lNewName = lNewName.concat(pOldName); //##70
  }

  if(index == 0){
    return lNewName.intern();
  }
  else {
    lNewName = lNewName.concat("_");
    sindex = String.valueOf(index);
    lNewName = lNewName.concat(sindex);
    return lNewName.intern();
  }
} //makeNewName

public String
generateUniqueName(Sym pOldSym, Subp pSubp)
{                    // Chen
  String   lSubpName,lOldName,lNewName;
  int      index,lSymKind;
  SymTable lSymTable = this;

  lOldName = pOldSym.getName();
  lSymKind = pOldSym.getSymKind();
  /* //##70
  if( symRoot.symTableUnique.searchLocal(lOldName,lSymKind,false)
      == null){
    lNewName = lOldName;
  }
  else {
      */ //##70
    index = 0;
    //get the name of subprogram that defined this symbol.
    if (pSubp != null)
      lSubpName = pSubp.getName();
    else
      lSubpName = "";
    lNewName = makeNewName(lOldName,lSubpName,index);
    while(symRoot.symTableUnique.searchLocal(lNewName,lSymKind,false)
          != null){
      lNewName = makeNewName(lOldName,lSubpName,++index);
    }
  //##70 }
  return lNewName.intern();
} // generateUniqueName

public String
generateConstName(Sym pOldSym, int index)
{                        // Chen
  String lNewName, sindex;
  sindex = String.valueOf(index);
  lNewName = "const_".concat(sindex);
  return lNewName.intern();
} //generateConstName

public void
setUniqueNameToAllSym()
{                        // Chen
  Sym              lSym,lSym1, lUniqueSym;
  String           lUniqueName , lConstName;
  SymTableIterator lSymTableIterator;
  SymIterator      lIterator;
  SymTable         lSymTable = this;
  Subp             lSubp;
  int lSymKind, lSymKindConst,index=0;
  ioRoot.dbgSym.print(1, "setUniqueNameToAllSym", " ");
  if (ioRoot.dbgSym.getLevel() >= 5) { //## 5
    ioRoot.dbgSym.print(4, "SymTables", "before generation"); //## 5
    symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
  }
  for (lSymTableIterator = symRoot.symTableRoot.getSymTableIterator();
       lSymTableIterator.hasNext(); ) {
    lSymTable = lSymTableIterator.next();
    if (lSymTable == null)
      continue;
    lSubp     = lSymTable.getSubp();    // Subprogram for this table.
    if (fDbgLevel > 3) //##67
      ioRoot.dbgSym.print(4, " ", ioRoot.toStringObject(lSymTable));
    for(lIterator = lSymTable.getSymIterator();
        lIterator.hasNext();){
      lSym = lIterator.next();
      //##70 if ((((SymImpl)lSym).fUniqueName != null)||
      if ((((SymImpl)lSym).fUniqueNameSym != null)||  //##70
          (lSym.getSymKind() == Sym.KIND_REMOVED))
        continue;
      //##70 lUniqueName = generateUniqueName(lSym, lSubp);
      //##70 lUniqueSym = symRoot.symTableUnique.searchOrAdd(lUniqueName,
      //##70                    Sym.KIND_OTHER,null,false,false);
      lSymKind = lSym.getSymKind();
      //##70 if (lUniqueName != lSym.getName()) {
        switch (lSymKind) {
        case Sym.KIND_VAR:
        case Sym.KIND_PARAM:
        case Sym.KIND_SUBP:
        case Sym.KIND_LABEL:
          lUniqueName = generateUniqueName(lSym, lSubp);
          if (lUniqueName != lSym.getName()) {
            lUniqueSym = symRoot.symTableUnique.searchOrAdd(lUniqueName,
                             Sym.KIND_OTHER,null,false,false);
            //##70 ((SymImpl)lSym).setUniqueName(lUniqueName);
            ((SymImpl)lSym).setUniqueNameSym(lUniqueSym); //##70
          }
          break;
        default:
          break;
        }
      //##70 }
    }
  }
  for(lIterator = symRoot.symTableConst.getSymIterator();
      lIterator.hasNext();){
      index++;
      lSym1 = lIterator.next();
      lSymKindConst = Sym.KIND_OTHER;
      lConstName = generateConstName(lSym1,index);
      while(symRoot.symTableUnique.searchLocal(lConstName,Sym.KIND_OTHER,
                                               false) != null){
        lConstName = generateConstName(lSym1,++index);
      }
      Sym lUniqueNameConst = //##70
        symRoot.symTableUnique.searchOrAdd(lConstName,
            lSymKindConst,null,true,false);
      if (lConstName != lSym1.getName()) {
        //##70 ((SymImpl)lSym1).setUniqueName(lConstName);
        ((SymImpl)lSym1).setUniqueNameSym(lUniqueNameConst); //##70
      }
      }
  if (ioRoot.dbgSym.getLevel() >= 2) {
    symRoot.symTable.printSymTableAllDetail(symRoot.symTableRoot);
    symRoot.symTable.printSymTableAllDetail(symRoot.symTableConst);
    if (ioRoot.dbgSym.getLevel() >= 5) {
      ioRoot.dbgSym.print(2, "Print", "SymTableUnique");
      symRoot.symTable.printSymTableAllDetail(symRoot.symTableUnique);
    }
  }
} //setUniqueNameToAllSym

} // SymTableImpl class
