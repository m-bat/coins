/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.cfront;

import coins.ast.Declarator;
import coins.ast.DeclaratorList;
import coins.ast.TypeId;

class OldFuncArgs implements TypeId {
    OldFuncArgs next;
    Object arg;

    OldFuncArgs(Object a, OldFuncArgs args) {
  next = args;
  arg = a;
    }

    boolean subst(Declarator d, String name) {
  for (OldFuncArgs list = this; list != null; list = list.next)
      if (list.arg == name) {
    list.arg = d;
    return true;
      }
  return false;
    }

    private static final byte[] intType = { (byte)INT_T }; //SF030206

    static DeclaratorList toDeclList(OldFuncArgs args, Lex lex)
  throws ParseError
    {
  DeclaratorList list = null;
  for (; args != null; args = args.next)
      if (args.arg instanceof Declarator) {
    list = new DeclaratorList((Declarator)args.arg, list);
      ////////////////////SF030206[
      //else
      //	throw new ParseError(lex,
      //			     "no type is specified: " + args.arg);
      } else { // defult arg type is int.
    Declarator decl = new Declarator(
        (String)args.arg, lex.getFileName(), lex.getLineNumber() );
    decl.setType(intType,lex.parser.evaluator.toSize[INT_T]);
    list = new DeclaratorList(decl,list);
    args.arg = decl; //SF041030
      }
      ////////////////////SF030206]

  return list;
    }
}
