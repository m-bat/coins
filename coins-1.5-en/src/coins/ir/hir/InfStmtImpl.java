/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;

import coins.HirRoot;
import coins.ir.IR;
import coins.ir.IrList; //##70
import coins.ir.IrListImpl; //##70
import coins.sym.Sym; //##71

/**
 *
 * Information node treated as a statement (pragma, comment line, etc.)
 *
**/
public class
InfStmtImpl extends StmtImpl implements InfStmt
{

  final String fInfKind; //##70
  public boolean fReformed = false;

public
InfStmtImpl( HirRoot pHirRoot, String pInfKindInterned, IrList pInfList )
{
  super(pHirRoot);
  fOperator = IR.OP_INF;
  fChildCount = 0; //##70
  fInfKind = pInfKindInterned;
  addInf(pInfKindInterned, pInfList);
  fType = hirRoot.symRoot.typeVoid;
  if (hirRoot.ioRoot.dbgHir.getLevel() > 0)
    hirRoot.ioRoot.dbgHir.print(4, "InfStmtImpl " + fInfKind + " " + pInfList);
}

//##70 BEGIN
public String
getInfKind()
{
  return fInfKind;
}

//##71 BEGIN
public String
getInfSubkindOf( String pInfKind )
{
  IrList lInfList = getInfList(pInfKind);
  String lString = null;
  if (lInfList != null) {
    Object lObject = lInfList.get(0);
    if (lObject instanceof String)
      lString = ((String)lObject).intern();
    else if (lObject instanceof Sym)
      lString = (((Sym)lObject).getName()).intern();
  }
  return lString;
} // getInfSubkindOf
//##71 END

public IrList getInfList(String pInfKind)
{
  if (fHirAnnex != null) {
    IrList lList = fHirAnnex.getInfList();
    if ((lList != null)&&(lList.get(0) == pInfKind)) {
      return (IrList)lList.get(1);
    }
  }
  return new IrListImpl(hirRoot);
}
//##70 END

public Object
clone() {
  InfStmtImpl lTree;
  try {
    lTree = (InfStmtImpl)super.clone();
  }catch (CloneNotSupportedException e) {
   hirRoot.ioRoot.msgRecovered.put(1100, "CloneNotSupportedException(InfStmt) "
                     + this.toString());
    return null;
  }
  return (Object)lTree;
} // clone

public String
toString() {
  if (fHirAnnex != null) {
    //##70 return "inf " + fHirAnnex.toString(); //##62
    return "inf " + fInfKind + " " + getInfList(fInfKind).toString();
  }else
    return "inf"; //##62
} // toString

//##70 BEGIN
public String
toStringShort() {
  return toString();
}
//##70 END

public void
accept( HirVisitor pVisitor )
{
  pVisitor.atInfStmt(this);
}

} // InfStmtImpl class
