/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.ir.IrList;
import coins.ir.hir.Exp;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.Type;

/** Call statement
 */
public class CallStmt extends FStmt{
  private Token fIdent;          // name of called subroutine
  private FirList fOptArgs;      // Fir list of actual parameters
  private IrList fParamList;     // HIR list of actual parameters
  private IrList fParamTypeList; // HIR list of actual parameter types
  boolean hasAltReturn = false;  // true if this has alternate returns
  FirList fLabels = null;
  private Subp fSubp;

  public CallStmt(Token pIdent, FirList pOptArgs, int line, FirToHir pfHir){
    super(line, pfHir);
    fIdent   = pIdent;
    fOptArgs = pOptArgs;
    fLabels  = new FirList(fHir);
  }
  public void print(int level, String spaces){
    super.print(level, spaces);
    fHir.debugPrint(level, spaces+" CallStmt"+"\n");
  }
  public String toString(){
    return super.toString()+" Call statement";
  }

  /**
   *
   */
  public void process(){
    preprocess();
    Sym sym = fHir.getSym();

    fParamList     = hir.irList();
    fParamTypeList = hir.irList();

    if (fOptArgs != null){
      for (Iterator it = fOptArgs.iterator(); it.hasNext(); ){
        Node lParam = ((Node)it.next());
        if(lParam instanceof Token && ((Token)lParam).getLexem() == "*"){
          // do nothing
        }
        else{
          Exp lParamAddr = lParam.makeArgAddr(this);
          if (lParamAddr != null){
            fParamList.add(lParamAddr);
          }
        }
      }
      for (Iterator it = fOptArgs.iterator(); it.hasNext(); ){
        Node lParam = ((Node)it.next());
        if(lParam instanceof Token && ((Token)lParam).getKind() == Parser.LABEL){
          // fLabels.add(lParam);
        }
        else{
          Exp e = lParam.makeExp();
          // TODO: for fchar vector, also should pass char's length
          if(e instanceof FortranCharacterExp){
            fParamList.add(((FortranCharacterExp)e).getLength());
          }
        }
      }
    }
    Sym lSym = fDeclMgr.search(fIdent.getLexem());
    Type returnType = fTypeUtil.getVoidType();
    if (hasAltReturn){
      // change to FUNCTION call
      returnType = fTypeUtil.getIntType();
    }
    if (lSym == null){
      // implicit
      //returnType = fDeclMgr.getImplicitType(fIdent.getLexem());
      fSubp = fDeclMgr.defineSubp(fIdent.getLexem(), returnType, Sym.SYM_PUBLIC, null);
    }
    else if (lSym.getSymKind() == Sym.KIND_SUBP){
      fSubp = (Subp)lSym;
    }
    else{
      fSubp = fDeclMgr.defineSubp(fIdent.getLexem(), returnType, Sym.SYM_PUBLIC, null);
      // fHir.printMsgError(fIdent.getLexem()+" is not a subroutine name");
      // return;
    }

    if (hasAltReturn){
      Exp subpExp = hir.functionExp(fHirUtil.makeSubpExp(fSubp), fParamList);
      stmt = new ComputedGoto(fLine, fHir).makeSwitchStmt(subpExp, fLabels);
    }
    else{
      stmt = hir.callStmt(fHirUtil.makeSubpExp(fSubp), fParamList);
    }
    super.process();
  }
}


