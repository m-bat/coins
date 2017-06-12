/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.casttohir;

import java.util.ListIterator;

import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmtImpl;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.IfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.StmtImpl;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.SymNode;
import coins.sym.Const;
import coins.sym.Label;
import coins.sym.Param;
import coins.sym.StringConst;
import coins.sym.Subp;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymTable;
import coins.sym.Var;

/** ToC
 * Convert HIR to C source code.
 * This class converts HIR-C, HIR-base, symbol tables to C source code for
 * debugging and error messages. The methods in this class are all static.
 *
 * @author  Shuichi Fukuda
**/
final class ToC
{
  private static final String[] operators  = new String[100];
  private static final int   [] ranks      = new int   [100];
  private static final String[] visibility = new String[10];
  private static final String[] storage    = new String[10];

  //-------------------------------------------------------------------
  static
  {
    ranks[HIR.OP_CONST         ]=99;
    ranks[HIR.OP_SYM           ]=99;
    ranks[HIR.OP_VAR           ]=99;
    ranks[HIR.OP_PARAM         ]=99;
    ranks[HIR.OP_SUBP          ]=99;
    ranks[HIR.OP_TYPE          ]=99;
    ranks[HIR.OP_LABEL         ]=99;
    ranks[HIR.OP_ELEM          ]=99;
    ranks[HIR.OP_CALL          ]=16;
    ranks[HIR.OP_SUBS          ]=16;
    ranks[HIR.OP_QUAL          ]=16; operators[HIR.OP_QUAL          ]=".";
    ranks[HIR.OP_ARROW         ]=16; operators[HIR.OP_ARROW         ]="->";
    ranks[HIR.OP_POST_INCR     ]=16; operators[HIR.OP_POST_INCR     ]="++";
    ranks[HIR.OP_POST_DECR     ]=16; operators[HIR.OP_POST_DECR     ]="--";
    ranks[HIR.OP_PRE_INCR      ]=15; operators[HIR.OP_PRE_INCR      ]="++";
    ranks[HIR.OP_PRE_DECR      ]=15; operators[HIR.OP_PRE_DECR      ]="--";
    ranks[HIR.OP_NEG           ]=15; operators[HIR.OP_NEG           ]="-";
    ranks[HIR.OP_NOT           ]=15; operators[HIR.OP_NOT           ]="~";
    ranks[HIR.OP_EQ_ZERO       ]=15; operators[HIR.OP_EQ_ZERO       ]="!";
    ranks[HIR.OP_ADDR          ]=15; operators[HIR.OP_ADDR          ]="&";
    ranks[HIR.OP_CONTENTS      ]=15; operators[HIR.OP_CONTENTS      ]="*";
    ranks[HIR.OP_SIZEOF        ]=15;
    ranks[HIR.OP_CONV          ]=14;
    ranks[HIR.OP_MULT          ]=13; operators[HIR.OP_MULT          ]="*";
    ranks[HIR.OP_DIV           ]=13; operators[HIR.OP_DIV           ]="/";
    ranks[HIR.OP_MOD           ]=13; operators[HIR.OP_MOD           ]="%";
    ranks[HIR.OP_ADD           ]=12; operators[HIR.OP_ADD           ]="+";
    ranks[HIR.OP_SUB           ]=12; operators[HIR.OP_SUB           ]="-";
    ranks[HIR.OP_OFFSET        ]=12; operators[HIR.OP_OFFSET        ]="-";
    ranks[HIR.OP_SHIFT_LL      ]=11; operators[HIR.OP_SHIFT_LL      ]="<<";
    ranks[HIR.OP_SHIFT_R       ]=11; operators[HIR.OP_SHIFT_R       ]=">>>";
    ranks[HIR.OP_SHIFT_RL      ]=11; operators[HIR.OP_SHIFT_RL      ]=">>";
    ranks[HIR.OP_CMP_GT        ]=10; operators[HIR.OP_CMP_GT        ]=">";
    ranks[HIR.OP_CMP_GE        ]=10; operators[HIR.OP_CMP_GE        ]=">=";
    ranks[HIR.OP_CMP_LT        ]=10; operators[HIR.OP_CMP_LT        ]="<";
    ranks[HIR.OP_CMP_LE        ]=10; operators[HIR.OP_CMP_LE        ]="<=";
    ranks[HIR.OP_CMP_EQ        ]=9;  operators[HIR.OP_CMP_EQ        ]="==";
    ranks[HIR.OP_CMP_NE        ]=9;  operators[HIR.OP_CMP_NE        ]="!=";
    ranks[HIR.OP_AND           ]=8;  operators[HIR.OP_AND           ]="&";
    ranks[HIR.OP_XOR           ]=7;  operators[HIR.OP_XOR           ]="^";
    ranks[HIR.OP_OR            ]=6;  operators[HIR.OP_OR            ]="|";
    ranks[HIR.OP_LG_AND        ]=5;  operators[HIR.OP_LG_AND        ]="&&";
    ranks[HIR.OP_LG_OR         ]=4;  operators[HIR.OP_LG_OR         ]="||";
    ranks[HIR.OP_SELECT        ]=3;
    ranks[HIR.OP_ASSIGN        ]=2;  operators[HIR.OP_ASSIGN        ]=" = ";
    ranks[HIR.OP_ADD_ASSIGN    ]=2;  operators[HIR.OP_ADD_ASSIGN    ]=" += ";
    ranks[HIR.OP_SUB_ASSIGN    ]=2;  operators[HIR.OP_SUB_ASSIGN    ]=" -= ";
    ranks[HIR.OP_MULT_ASSIGN   ]=2;  operators[HIR.OP_MULT_ASSIGN   ]=" *= ";
    ranks[HIR.OP_DIV_ASSIGN    ]=2;  operators[HIR.OP_DIV_ASSIGN    ]=" /= ";
    ranks[HIR.OP_MOD_ASSIGN    ]=2;  operators[HIR.OP_MOD_ASSIGN    ]=" %= ";
    ranks[HIR.OP_SHIFT_L_ASSIGN]=2;  operators[HIR.OP_SHIFT_L_ASSIGN]=" <<= ";
    ranks[HIR.OP_SHIFT_R_ASSIGN]=2;  operators[HIR.OP_SHIFT_R_ASSIGN]=" >>= ";
    ranks[HIR.OP_AND_ASSIGN    ]=2;  operators[HIR.OP_AND_ASSIGN    ]=" &= ";
    ranks[HIR.OP_OR_ASSIGN     ]=2;  operators[HIR.OP_OR_ASSIGN     ]=" |= ";
    ranks[HIR.OP_XOR_ASSIGN    ]=2;  operators[HIR.OP_XOR_ASSIGN    ]=" ^= ";
    ranks[HIR.OP_COMMA         ]=1;  operators[HIR.OP_COMMA         ]=",";

    visibility[Sym.SYM_EXTERN      ]="EXTERN ";
    visibility[Sym.SYM_PUBLIC      ]="PUBLIC ";
    visibility[Sym.SYM_PROTECTED   ]="PROTECTED ";
    visibility[Sym.SYM_PRIVATE     ]="PRIVATE ";
    visibility[Sym.SYM_COMPILE_UNIT]="COMPILEUNIT ";
    storage[Var.VAR_STATIC  ]="STATIC ";
    storage[Var.VAR_AUTO    ]="AUTO ";
    storage[Var.VAR_REGISTER]="REGISTER ";
  }
  //-------------------------------------------------------------------
  public static String getOp(int i)
  {
    return operators[i];
  }
  //-------------------------------------------------------------------
  private static String getIndent(int n)
  {
    if( n<=0 )
      return "";
    StringBuffer buf = new StringBuffer(n);
    while( n-->0 )
      buf.append(' ');
    return buf.toString();
  }
  //-------------------------------------------------------------------
  // SymTable
  //-------------------------------------------------------------------
  public static String tos(SymTable t,int n) // to string
  {
    if( t==null )
      return "";

    StringBuffer buf = new StringBuffer();
    String indent = getIndent(n);

    for( SymIterator i=t.getSymIterator(); i.hasNext(); )
    {
      Sym s = i.next();
      switch( s.getSymKind() )
      {
      case Sym.KIND_VAR:
        Var var = (Var)s;
        buf.append(indent)
           .append(visibility[var.getVisibility()])
           .append(storage   [var.getStorageClass()])
           .append(var.getSymType())
           .append(' ')
           .append(s.getName());
        if( var.getInitialValue()!=null )
          buf.append("=").append( tos(var.getInitialValue()) );
        buf.append(";\n");
        break;

      case Sym.KIND_SUBP:
        Subp subp = (Subp)s;
        buf.append(indent)
           .append(visibility[subp.getVisibility()])
           .append(s.getName())
           .append('(');
        for( ListIterator j=subp.getParamList().iterator(); j.hasNext(); )
        {
          Param param = (Param)j.next();
          buf.append('\n')
             .append(indent)
             .append(' ')
             .append(visibility[param.getVisibility()])
             .append(storage   [param.getStorageClass()])
             .append(param.getSymType())
             .append(' ')
             .append(param.getName())
             .append(',');
        }
        buf.append(')');
        if( subp.getHirBody()!=null )
          buf.append('\n')
             .append(tos( (Stmt)subp.getHirBody(), n+1 ));
        else
          buf.append(";\n");
        break;
      }
    }
    return buf.toString();
  }
  //-------------------------------------------------------------------
  // Program
  //-------------------------------------------------------------------
  public static String tos(SubpDefinition h,int n) // to string
  {
    return tos( h.getHirBody(), n+1 );
  }
  //-------------------------------------------------------------------
  // Statement
  //-------------------------------------------------------------------
  public static String tos(Stmt s,int n) // to string
  {
    String indent = getIndent(n);
    if( s==null )
      return indent+";";
    StringBuffer buf = new StringBuffer();

    switch( s.getOperator() )
    {
    case HIR.OP_BLOCK:
      buf.append(indent)
         .append("{\n")
         .append(tos( ((BlockStmtImpl)s).getSymTable(), n+1 ));
      for( Stmt i=((BlockStmtImpl)s).getFirstStmt();
           i!=null;
           i=((StmtImpl)i).getNextStmt() )
        buf.append(tos( i, n+1 ));
      buf.append(indent)
         .append("}\n");
      break;

    case HIR.OP_LABELED_STMT:
      buf.append(indent)
         .append( ((LabeledStmt)s).getLabel() );
      if( ((LabeledStmt)s).getStmt()!=null )
        buf.append(":\n")
           .append(tos( ((LabeledStmt)s).getStmt(), n ));
      else
        buf.append(":;\n");
      break;

    case HIR.OP_ASSIGN:
      buf.append(indent)
         .append(tos( ((AssignStmt)s).getLeftSide() ))
         .append(" = " )
         .append(tos( ((AssignStmt)s).getRightSide() ))
         .append(";\n" );
      break;

    case HIR.OP_IF:
      buf.append(indent)
         .append("if( ")
         .append(tos( ((IfStmt)s).getIfCondition() ))
         .append(" )\n")
         .append(tos( ((IfStmt)s).getThenPart(), n+1 ))
         .append(indent)
         .append("else\n")
         .append(tos( ((IfStmt)s).getElsePart(), n+1 ));
      break;

    case HIR.OP_WHILE:
      buf.append(indent)
         .append("while( ")
         .append(tos( ((LoopStmt)s).getLoopStartCondition() ))
         .append(" )\n")
         .append(tos( ((LoopStmt)s).getLoopBodyPart(), n+1 ));
      break;

    case HIR.OP_FOR:
      buf.append(tos( ((LoopStmt)s).getLoopInitPart(), n ))
         .append(indent)
         .append("for(; ")
         .append(tos( ((LoopStmt)s).getLoopStartCondition() ))
         .append("; )\n")
         .append(indent)
         .append("{\n")
         .append(tos( ((LoopStmt)s).getLoopBodyPart(), n+1 ))
         .append(tos( ((LoopStmt)s).getLoopStepPart(), n+1 ))
         .append(indent)
         .append("}\n");
      break;

    case HIR.OP_UNTIL:
      buf.append(indent)
         .append("do\n")
         .append(tos( ((LoopStmt)s).getLoopBodyPart(), n+1 ))
         .append(indent)
         .append("while( ")
         .append(tos( ((LoopStmt)s).getLoopEndCondition() ))
         .append(indent)
         .append(" ); )\n");
      break;

    case HIR.OP_JUMP:
      buf.append(indent)
         .append("goto ")
         .append( ((JumpStmt)s).getLabel() )
         .append(";\n");
         break;

    case HIR.OP_SWITCH:
      buf.append(indent)
         .append("switch( ")
         .append(tos( ((SwitchStmt)s).getSelectionExp() ))
         .append(" )\n");
      if( ((SwitchStmt)s).getBodyStmt().getOperator()==HIR.OP_BLOCK )
      {
        // switch statement with block.
        BlockStmtImpl block = (BlockStmtImpl)((SwitchStmt)s).getBodyStmt();
        buf.append(indent)
           .append("{\n")
           .append(tos( block.getSymTable(), n+1 ));
        for( Stmt i=block.getFirstStmt(); i!=null; i=((StmtImpl)i).getNextStmt() )
        {
          // Traverse all statements within the block.
          Const c;
          if( i.getOperator()==HIR.OP_LABELED_STMT // Case statement.
          && (c=getConstByLabel( (SwitchStmt)s, ((LabeledStmt)i).getLabel() ))!=null )
            buf.append(indent)
               .append("case ")
               .append( c.intValue() )
               .append(':');
          else // Not a case statement.
            buf.append(tos( i, n ));
        }
        buf.append(indent)
           .append("}\n");
      }
      else
      {
        // Switch statement without block.
        buf.append(tos( ((SwitchStmt)s).getBodyStmt(), n+1 ));
      }
      break;

    case HIR.OP_RETURN:
      buf.append(indent)
         .append("return ")
         .append(tos( ((ReturnStmt)s).getReturnValue() ))
         .append(";\n");
      break;

    case HIR.OP_EXP_STMT:
      buf.append(indent)
         .append(tos( ((ExpStmt)s).getExp() ))
         .append(";\n");
      break;

    //case HIR.OP_INF:

    default:
      buf.append(indent)
         .append("STMTERR\n");
    }
    return buf.toString();
  }
  //-------------------------------------------------------------------
  private static Const getConstByLabel(SwitchStmt s,Label l)
  {
    for( int i=0; i<s.getCaseCount(); i++ )
    {
      if( s.getCaseLabel(i)==l )
        return s.getCaseConst(i);
    }
    return null;
  }
  //-------------------------------------------------------------------
  // Expression
  //-------------------------------------------------------------------
  public static String tos(Exp e) // to string
  {
    if( e==null )
      return "";
    switch( e.getOperator() )
    {
    case HIR.OP_CONST:
      Sym y = ((ConstNode)e).getConstSym();
      switch( y.getSymKind() )
      {
      case Sym.KIND_INT_CONST:
        return Long  .toString( ((Const)y).longValue  () );
      case Sym.KIND_FLOAT_CONST:
        return Double.toString( ((Const)y).doubleValue() );
      case Sym.KIND_STRING_CONST:
        return "\""+((StringConst)y).getStringBody()+"\"";
      default:
        return "SYMERR";
      }
    case HIR.OP_SYM:
    case HIR.OP_PARAM:
    case HIR.OP_ELEM:
    case HIR.OP_VAR:
    case HIR.OP_SUBP:
      return ((SymNode)e).getSymNodeSym().getName();

    case HIR.OP_SUBS:
      return tos(e,1)+"["+tos(e,2)+"]";

    //case HIR.OP_INDEX:

    case HIR.OP_DECAY:
    case HIR.OP_UNDECAY:
      return tos(e,1);

    case HIR.OP_SIZEOF:
      return "sizeof("+tos(e.getExp1())+")";

    case HIR.OP_CALL:
      StringBuffer buf = new StringBuffer();
      buf.append(tos(e,1)).append("(");
      IrList actuallist = ((FunctionExp)e).getParamList(); // Actual parameter list.
      int    actualsize = actuallist.size();
      for( int i=0; i<actualsize; i++ )
      {
        if( i!=0 )
          buf.append(",");
        buf.append( tos((Exp)actuallist.get(i)) );
      }
      buf.append(")");
      return buf.toString();

    case HIR.OP_QUAL:
    case HIR.OP_ARROW:
    case HIR.OP_ASSIGN:

    case HIR.OP_ADD:
    case HIR.OP_SUB:
    case HIR.OP_MULT:
    case HIR.OP_DIV:
    case HIR.OP_MOD:
    case HIR.OP_AND:
    case HIR.OP_OR:
    case HIR.OP_XOR:
    case HIR.OP_CMP_EQ:
    case HIR.OP_CMP_NE:
    case HIR.OP_CMP_GT:
    case HIR.OP_CMP_GE:
    case HIR.OP_CMP_LT:
    case HIR.OP_CMP_LE:
    case HIR.OP_SHIFT_LL:
    case HIR.OP_SHIFT_R:
    case HIR.OP_SHIFT_RL:

    case HIR.OP_OFFSET:
    case HIR.OP_LG_AND:
    case HIR.OP_LG_OR:
    case HIR.OP_COMMA:

    case HIR.OP_ADD_ASSIGN:
    case HIR.OP_SUB_ASSIGN:
    case HIR.OP_MULT_ASSIGN:
    case HIR.OP_DIV_ASSIGN:
    case HIR.OP_MOD_ASSIGN:
    case HIR.OP_SHIFT_L_ASSIGN:
    case HIR.OP_SHIFT_R_ASSIGN:
    case HIR.OP_AND_ASSIGN:
    case HIR.OP_OR_ASSIGN:
    case HIR.OP_XOR_ASSIGN:
      return tos(e,1)+operators[e.getOperator()]+tos(e,2);

    case HIR.OP_EQ_ZERO: // !
    case HIR.OP_NOT: // ~
    case HIR.OP_NEG: // -

    case HIR.OP_ADDR:
    case HIR.OP_CONTENTS:

    case HIR.OP_PRE_INCR:
    case HIR.OP_PRE_DECR:
      return operators[e.getOperator()]+tos(e,1);

    case HIR.OP_CONV:
      return "("+e.getType()+")"+tos(e,1);

    case HIR.OP_NULL:
      return "NULL";

    case HIR.OP_SELECT:
      return tos(e,1)+"?"+tos(e,2)+":"+tos(e,3);

    case HIR.OP_POST_INCR:
    case HIR.OP_POST_DECR:
      return tos(e,1)+operators[e.getOperator()];

    default:
      return "OPERR";
    }
  }
  //-------------------------------------------------------------------
  private static String tos(Exp e,int i) // to string ith child of expression
  {
    Exp child = (Exp)e.getChild(i);
    if( child==null )
      return "CHILDERR";
    int pr = ranks[e    .getOperator()];
    int cr = ranks[child.getOperator()];
    if( pr>cr || pr==cr && i!=1 )
      return "("+tos(child)+")";
    else
      return tos(child);
  }
  //-------------------------------------------------------------------
}
