/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.util.Iterator;

import coins.sym.Type;

/**
  Implicit Manager
 */

public class ImplicitManager extends BaseManager{
  private Type[]  implicitTable = new Type[26];
  private boolean[] implicitCheck = new boolean[26];

  public ImplicitManager(FirToHir fth){
    super(fth);
  }

  void processImplicit(F77Sym f7Sym){
    for (int i = 0; i < 26; i++){
      implicitTable[i] = symRoot.typeFloat;
      implicitCheck[i] = false;
    }

    for (int i = 'i' - 'a'; i <= 'n' -'a'; i++){
      implicitTable[i] = symRoot.typeInt;
    }

    FirList implicitList = f7Sym.implicitList;
    // implicitList.print(0,"implicitList : ");
    Iterator it = implicitList.iterator();
    
    while(it.hasNext()){
      Pair p = (Pair)it.next();
      if(p == null){
        //
        dp("implicit: all implicit type declaration will be error.");
        for(int i=0;i<implicitTable.length;i++){
          //!K implicitTable[i] = null;
        }
        break;
      }
      Type t = fTypeUtil.getType((Pair)p.getLeft());
      String s = ((Token)p.getRight()).getLexem();
      for(int i=0,n=s.length();i<n;i++){
        int c = s.charAt(i) - 'a';
        if(implicitCheck[c]){
          // error
          printMsgFatal("already explicitly established : " + s.charAt(i));
          continue;
        }
        implicitCheck[c] = true;
        implicitTable[c] = t;
      }
    }
  }

  public Type getImplicitType(String id){
    // dp("getImplicitType: " + id);
    return implicitTable[id.charAt(0)-'a'] ;
  }
}


