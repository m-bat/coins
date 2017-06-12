/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
//##71 import java.util.LinkedList;
import java.util.Set;  //##71
import coins.SourceLanguage; //##71

///////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] PreDefinedFunctions
//
//  Declare functions that have no side effect and
//    are not obstacle of parallelization.
///////////////////////////////////////////////////////////////////////////////
public class  PreDefinedFunctions {
    //##71 private LinkedList  fFunctions;
    /**
    *
    * PreDefineFunctins:
    *
    *
    **/
  /* //##71
  PreDefinedFunctions() {
  fFunctions = new LinkedList();
        fFunctions.add("sin");
        fFunctions.add("cos");
        fFunctions.add("tan");

        fFunctions.add("asin");
        fFunctions.add("acos");
        fFunctions.add("atan");

        fFunctions.add("sinh");
        fFunctions.add("cosh");
        fFunctions.add("tanh");

        fFunctions.add("log");
        fFunctions.add("exp");
        fFunctions.add("pow");
        fFunctions.add("sqrt");
        // ...
        // ...
        // ...
        //fFunctions.add("printf");
     }
    boolean contains(String  funcname) {
        return fFunctions.contains(funcname);
    }
    */ //##71
//##71 BEGIN
  protected Set
    fPredefinedFunctions;

  PreDefinedFunctions( SourceLanguage pSourceLanguage )
  {
    fPredefinedFunctions = pSourceLanguage.functionsWithoutSideEffect;
  } // PredefinedFunctions

  boolean contains(String  funcname) {
      return fPredefinedFunctions.contains(funcname);
  }
//##71 END

} // PredefinedFunctions
