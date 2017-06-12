/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.LinkedList;

import coins.sym.Var;
import coins.sym.Subp;
import coins.sym.Type;

/**
 * Reports warnings and errors according to the condition.
 * <pre>
 * The following are reported only once with each function or symbol.
 * - long long is not supported
 * - long double is not supported
 * - bit-field is not supported
 * - variable argument list is not supported
 * - wchar_t string is not supported
 * </pre>
 *
 * @author  Shuichi Fukuda
**/
class ConditionalReporter
{
  private final ToHir toHir;
  //private LinkedList longLongList   = new LinkedList();
  private LinkedList longDoubleList = new LinkedList();
  //private LinkedList bitFieldList   = new LinkedList();
  private LinkedList vaArgList      = new LinkedList();
  private LinkedList wideCharList   = new LinkedList();

  //-------------------------------------------------------------------
  public ConditionalReporter(ToHir tohir)
  {
    toHir  = tohir;
  }
  //-------------------------------------------------------------------
  /**
   * Show warning message of not suported type.
   *
   * @param  var Var
  **/
  void isSupportedType(Var var)
  {
    switch( var.getSymType().getTypeKind() )
    {
    ////////SF041020[
    //case Type.KIND_LONG_LONG:
    //case Type.KIND_U_LONG_LONG:
    //  if( !longLongList.contains(var) )
    //  {
    //    longLongList.addFirst(var);
    //    toHir.warning("long long is not supported: "+var.getName());
    //  }
    //  break;
    ////////SF041020]
    case Type.KIND_LONG_DOUBLE:
      if( !longDoubleList.contains(var) )
      {
        longDoubleList.addFirst(var);
        toHir.warning("long double is not supported: "+var.getName());
      }
      break;
    }
  }
  //-------------------------------------------------------------------
  /**
   * Show warning message of bit-field.
   *
   * @param  elem Elem
  **/
  //void isBitField(Elem elem)
  //{
  //  if( elem.isBitField()
  //  &&  !bitFieldList.contains(elem) )
  //  {
  //    bitFieldList.addFirst(elem);
  //    toHir.warning("bit-field is not supported: "+elem.getName());
  //  }
  //}
  //-------------------------------------------------------------------
  /**
   * Show warning message of va_arg.
   *
   * @param  subp Subp
  **/
  void isVaArg(Subp subp)
  {
    String name = subp.getName();
    if( (name.equals("__builtin_next_arg")
    ||   name.equals("__builtin_saveregs")
    ||   name.equals("__builtin_va_start") //SF041121
    ||   name.equals("__builtin_va_arg") //SF041121
    ||   name.equals("__builtin_va_end")) //SF041121
    &&  !vaArgList.contains(toHir.symRoot.subpCurrent) )
    {
      vaArgList.addFirst(toHir.symRoot.subpCurrent);
      //##71 toHir.warning("variable argument list is not supported: in "
      //##71   +toHir.symRoot.subpCurrent.getName()+"()");
    }
  }
  //-------------------------------------------------------------------
  /**
   * Show warning message of wchar_t.
   *
   * @param  wchar true if wchat_t.
  **/
  void isWideChar(boolean wchar)
  {
    if( wchar
    &&  !wideCharList.contains(toHir.symRoot.subpCurrent) )
    {
      wideCharList.addFirst(toHir.symRoot.subpCurrent);
      toHir.warning(
        "wchar_t is not supported: in "
        + ( toHir.symRoot.subpCurrent==null
          ? "global"
          : toHir.symRoot.subpCurrent.getName()+"()" ) );
    }
  }
  //-------------------------------------------------------------------
}
