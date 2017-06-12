/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins;

import coins.FatalError;
import coins.driver.Warning;
import coins.driver.CoinsOptions;

/** Message:
 *  Message displaying class to be used for
 *  debugging and error processing.
**/
public class
Message
{

  IoRoot fIoRoot;  // IoRoot passes at instanciation.
  String fHeader;  // Message header for classification.
  int    fMessageCountOfThisClass;  // Message count of this message class.

/** Message:
 *  Constructor to initiate all Message objects.
 *  @param pIoRoot: IoRoot received at instantiation time.
**/
public
Message( IoRoot pIoRoot )
{
  fIoRoot = pIoRoot;
}

/** Message:
 *  Constructor for each Message object.
 *  @param pIoRoot: IoRoot received at instantiation time.
**/
public
Message( IoRoot pIoRoot, String pHeader )
{
  fIoRoot = pIoRoot;
  fHeader = pHeader;
}

/* Never call initiate() more than once per a Message object */
public void
initiate( )
{
  /**
   * FatalMessage is a subclass of Message, which throws a FatalError whenever
   * a message is put.
   **/
  class FatalMessage extends Message {
    public
    FatalMessage( IoRoot pIoRoot, String pHeader )
    {
      super(pIoRoot, pHeader);
    }
    public void
    put( String pMessage )
    {
      super.put(pMessage);
      throw new FatalError(pMessage);
    }
    public void
    put( int pMessageNumber, String pMessage )
    {
      super.put(pMessageNumber, pMessage);
      throw new FatalError("" + pMessageNumber + ":" + pMessage);
    }
  }

  /**
   * RecoveredErrorMessage is a subclass of Message.
   **/
  class RecoveredErrorMessage extends Message {
    public
    RecoveredErrorMessage( IoRoot pIoRoot, String pHeader )
    {
      super(pIoRoot, pHeader);
    }
    private void checkCount()
    {
      CoinsOptions lCoinsOptions
	= fIoRoot.getCompileSpecification().getCoinsOptions();
      if (lCoinsOptions.isSet("max-recovered-errors")) {
	String lArg = lCoinsOptions.getArg("max-recovered-errors");
	int lMax = new Integer(lArg).intValue();
	if ((lMax > 0) && (fMessageCountOfThisClass == lMax + 1)) {
	  fIoRoot.msgError.put(fIoRoot.getSourceFile().getName()
			       + ": Too many recovered errors");
	}
      }
    }
    public synchronized void
    put( String pMessage )
    {
      super.put(pMessage);
      checkCount();
    }
    public synchronized void
    put( int pMessageNumber, String pMessage )
    {
      super.put(pMessageNumber, pMessage);
      checkCount();
    }
  }

  /**
   * WarningMessage is a subclass of Message.
   **/
  class WarningMessage extends Message {
    public
    WarningMessage( IoRoot pIoRoot, String pHeader )
    {
      super(pIoRoot, pHeader);
    }
    private void checkCount()
    {
      CoinsOptions lCoinsOptions
	= fIoRoot.getCompileSpecification().getCoinsOptions();
      if (lCoinsOptions.isSet("max-warnings")) {
	String lArg = lCoinsOptions.getArg("max-warnings");
	int lMax = new Integer(lArg).intValue();
	if ((lMax > 0) && (fMessageCountOfThisClass == lMax + 1)) {
	  fIoRoot.msgError.put(fIoRoot.getSourceFile().getName()
			       + ": Too many warnings");
	}
      }
    }
    public synchronized void
    put( String pMessage )
    {
      super.put(pMessage);
      checkCount();
    }
    public synchronized void
    put( int pMessageNumber, String pMessage )
    {
      super.put(pMessageNumber, pMessage);
      checkCount();
    }
  }

  fIoRoot.msgNote      = new Message(fIoRoot, "Note");
  fIoRoot.msgWarning   = new WarningMessage(fIoRoot, "Warning");
  fIoRoot.msgRecovered = new RecoveredErrorMessage(fIoRoot, "Recovered error");
  fIoRoot.msgError     = new Message(fIoRoot, "Compile error");
  fIoRoot.msgFatal     = new FatalMessage(fIoRoot, "Fatal error");
} // initiate

/** put:
 *  Put message.
 *  This may be used to issue error message in catch clause.
 *  If dbgControl.getLevel() > 0, then the message is also printed
 *  to printOut, too.
**/
public synchronized void
put( String pMessage )
{
  fIoRoot.msgOut.println("\n" + fHeader + " " + pMessage );
  if ((fIoRoot.msgOut != fIoRoot.printOut)&&
      (fIoRoot.dbgControl.getLevel() > 0))
    fIoRoot.printOut.println("\n" + fHeader + " " + pMessage );
  fIoRoot.incrementMessageCount();
  fMessageCountOfThisClass++;
} // put without message number

/** put:
 *  Put message with message number.
 *  If dbgControl.getLevel() > 0, then the message is also printed
 *  to printOut, too.
**/
public synchronized void
put( int pMessageNumber, String pMessage )
{
  String lMessage = "\n" + fHeader + " " + pMessageNumber + ": " + pMessage;
  fIoRoot.msgOut.println(lMessage);
  if ((fIoRoot.msgOut != fIoRoot.printOut) &&
      (fIoRoot.dbgControl.getLevel() > 0))
    fIoRoot.printOut.println(lMessage);
  fIoRoot.incrementMessageCount();
  fMessageCountOfThisClass++;
} // put with number

public synchronized int
getMessageCountOfThisClass()
{
  return fMessageCountOfThisClass;
}

public synchronized void
resetMessageCountOfThisClass()
{
  fMessageCountOfThisClass = 0;
}

// Error Level  Message
//number
// 0000-0999 Messages related to compiler control.
// 1000-1999 Messages related to intermediate representation.
// 2000-2999 Messages related to syntax analysis.
// 3000-4999 Messages related to semantic analysis.
// 5000-5999 Messages related to flow analysis and code optimization.
// 6000-6999 Messages related to parallelization.
// 7000-7999 Messages related to back end.
// 8000-8999 Messages related to others.

//   100 3 IO error. (Main)
//  1001 2 Too big child number in setChild
//  1010 2 double definition of xxx (SymTableImpl)
//  1010 2 Symbol xxx of kind yy can not be redefined as zz. (SymTableImpl)
//  1011 2 const conflicts with volatile (TypeImpl)
//  1011 2 volatile conflicts with const (TypeImpl)
//  1011 2 volatile conflicts with const (TypeImpl)
//  1011 2 irregal qualification (TypeImpl)
//  1012 2 Undefined variable xxx. (SymImpl)
//  1013 3 Element type of VectorType not found (SymImpl)
//  1014 3 Displacement is not evaluable      (ElemImpl)
//  1015 3 TypeSize is not evaluable     (TypeImpl)
//  1016 2 StringConst param has no quote (StringConstImpl)
//  1016 2 stringConst param has no quote (SymImpl)
//  1020 2 Function pointer is requested (ExpImpl)
//  1021 2 Default type int is applied as return value type. (FunctionExpImpl)
//  1022 2 Illegal sizeof-expression for type (HIR_Impl)
//  1022 2 Illegal sizeof-expression for node (HIR_Impl)
//  1023 2 Illegal decay operand.      (HIR_Impl)
//  1023 2 Illegal undecay operand.    (HIR_Impl)
//  1024 2 Inf identifier xxx does not match with yyy (HirAnnex)
//  1025 2
//  1100 2 CloneNotSupportedException. (copyWithOperands)
//  1100 2 CloneNotSupportedException. (InfNodeImpl)
//  1100 2 CloneNotSupportedException. (LabelDefImpl)
//  1100 2 CloneNotSupportedException. (SymNodeImpl)
//  1100 2 CloneNotSupportedException. (StmtImpl)
//  1100 2 CloneNotSupportedException. (IrListImpl)
//  1103 2 IrList.clone() ClassCastException. (IrListImpl)
//  1110 2 Subprogram symbol is required in SubpDefinition.
//                                     (SubpDefinition)
//  1111 2 replaceThisNode expects Stmt. (HIR_Impl)
//  1111 2 replaceThisNode expects BlockStmt or HirList as parent. (HIR_Impl)
//  1210 2 ConditionalInitPart does not take if-statement form.
//                                     (LoopStmtImpl)
//  1211 2 ConditionalInitPart does not take if-statement form.
//                                     (LoopStmtImpl)
//  1220 2 setIndexNumberToAllNodes should be specified for Program or SubpDefinition (HIR_Impl).
//  3101 2 Case type is not equal      (ToHirBase)
//  3102 2 Illegal pointer operation   (ToHitBase)
//  3103 2 Incomptlete type. Size is not evaluable. (ToHitBase)
//  3104 2 Parameter type may differ with its prototype declaration. SubpImpl)
//  5011 2 Incomplete flow information (SubpFlow.cfgIterator)
//  5012 2 Incomplete flow information (SubpFlow.cfgFromExitIterator)
//  5013 2 Incomplete flow information (SubpFlow.symListIterator)
//  5013 2 Incomplete flow information (Flow.initiateHirDataFlowAnal)
//  5021 2 ExpId is not attached (nextExpNode)
//  5022 2 Null parameter in insertCopiedStmt
//  5022 2 Null parameter in moveStmt
//  5023 2 fuseSuccessor with null parameter
//  5023 2 fuseSuccessor with unmatched successor list
//  5023 2 fuseSuccessor with unmatched predecessor list


} // Message class

