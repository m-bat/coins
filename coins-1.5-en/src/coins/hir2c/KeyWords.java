/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2c;
//////////////////////////////////////////////////////////////////////////
//
//  HIR-Base  To  C Language   KeyWords Table
//   (High level intermediate representation)
//
///////////////////////////////////////////////////////////////////////////
class KeyWords  {
        private static final String[]     // C-o:HIR-C only
            OP_KEY_WORDS = {          "XX none00  ",
                "XX Program" ,"XX subpDef "  ,"XX labelDef"  ,"XX inf Node " ,
                "hir_c_"     ,"XX SymNode "  ,"XX VarNode"   ,"XX Param "    ,
                "XX subpNode","XX typeNode " ,"XX labelNode" ,"XX elemNode"  ,
                "XX none13  ","XX list    "  ,"XX seq     "  ,"hir___ENCLOSE" ,
                "XX subs    ","XX index   "  ,"XX qual    "  ,"XX arrow   "  ,
                "XX labeldSt"," = "          ,"if "          ,"while "       ,
                "for "       , "XX until "   ,"XX indxLoop"  , "goto "       ,
                "XX none29"  ,"XX none30  "  ,"XX none31  " ,"switch"        ,
                "XX call    ","return  "     ,"XX block   "  ,"XX expStmt "  ,
         //##70 "XX none37  ","hir___ADD"     ,"hir___SUB"     ,"XX none40  "  ,
                "asm",         "hir___ADD"     ,"hir___SUB"     ,"XX none40  "  , //##70
                "hir___MULT"  ,"hir___DIV"     ,"hir___MOD"     ,"XX none44 "   ,
                "XX none45  ", "hir___AND"    ,"hir___OR"      , "hir___XOR"    ,
                "XX none49 " ,"XX none50  "  ,"hir___CMP_EQ" ,"hir___CMP_NE"   ,
                "hir___CMP_GT","hir___CMP_GE"  ,"hir___LT"      ,"hir___LE"      ,
                "XX non57   ","hir___SHIFT_LL","hir___SHIFT_R","hir___SHIFT_RL" ,
                "XX none61  ","hir___NOT"     ,"hir___NEG"     ,"hir___ADDR"    ,
                "XX conv   " ,"hir___DECAY"   ,"hir___UNDECAY" ,"hir___CONTENTS",
                "XX none69  ","sizeof"       ,"XX none71"    ,"XX phi     "  ,
                "hir___NULL"  ,"XX node74  "  ,"XX node75  "  ,"C-o  offset"  ,
                "C-o &&   "  , "C-o ||"      ,"C-o select  " ,"C-o comma  "  ,
                "XX none81  ","C-o ++"       ,"C-o --"       ,"C-o ++"       ,
                "C-o --"     , "C-o += "     ,"C-o -="       ,"C-o *= "      ,
                " C-o /= "   ,"C-o %= "      , "C-o <<= "    ,"C-o >>= "     ,
                "C-o  &=    ","C-o |=  "     ,"C-o ^= "      , "Empty1"      ,
                "Empty2"     ,"Empty3"       ,"Empty4"       ,"Empty5"
                };
        private static final String[]     //
            TYPE_KEY_WORDS = { "KIND_UNDEF",
            //  1                 2                 3
            "hir_t_bool"   ,"XX KIND_S_char"     ,"hir_t_short "  ,
            //  4                 5                 6
            "hir_t_int"   ,"hir_t_long"       ,"hir_t_long_long"      ,
            //  7                 8                 9
            "hir_t_char"  ,"hir_t_u_char"    ,"hir_t_u_short"         ,
            //  10                11                12
            "hir_t_u_int" ,"hir_t_u_long"   ,"hir_t_u_long_long"      ,
            //  13                14                15
            "hir_t_addr"  ,"hir_t_offset"    ,"hir_t_void"            ,
            //  16                17                18
            "hir_t_float"  ,"hir_t_double"   ,"hir_t_long_double "    ,
            //  19                20                21
            "XX KIND_..LIM " ,"XX KIND_STRING"        ,"enum "        ,
            //  22                23                24
            "*"              ,"XX KIND_VECTOR"        ,"struct "      ,
            //  25                26                27
            "union "         , "xx KIND_DEFINED"      ,"XX KIND_SUBP"  ,
            "Empty1"         , "Empty2"             , "Empty3" };

        private static final String[]     //
            SYM_KEY_WORDS = { "SYMUNDEF",
                    "hir_s_extern",
                    "hir_s_public",
                    "hir_s_protected",
                    "hir_s_private",
                    "hir_s_compile_unit",
                    "hir_v_static",
                    "hir_v_auto",
                    "hir_v_register",
                    "Empty1",
                    "Empty2",
                    "Empty3"
                };

        public  static final  int HIR2C_LOWERBOUND =1;
        public  static final  int HIR2C_STRINGCOPY =2;
        public  static final  int HIR2C_POINTER =3;
        private static final String[]     //
            HIR2C_KEY_WORDS = { "HIR2CUNDEF",
                    "hir__LowerBound",
                    "hir__StringCopy",
                    "hir__Pointer",
                    "Empty2",
                    "Empty3"
                };
    /**
    *
    * getOpKeyWord: HIR.OP_XXX ==> C Language KeyWord
    *
    *
    **/
    String getOpKeyWord(int pOp) {
        return  OP_KEY_WORDS[pOp];
    }
    /**
    *
    * getTypeKeyWord:  TYPE.KIND_XXX ==> C Language KeyWord
    *
    *
    **/
    String getTypeKeyWord(int pTypeKind) {
        return  TYPE_KEY_WORDS[pTypeKind] + " ";

    }
    /**
    *
    * getSymKeyWord:  Var.VAR_XXX , Sym.SYM_XXX ==> C Language KeyWord
    *
    *
    **/
    String getSymKeyWord(int pSym) {
        return  SYM_KEY_WORDS[pSym] + " ";
    }
    /**
    *
    * getHir2cKeyWord:
    *
    *
    **/
    String getHir2cKeyWord(int pno) {
        return  HIR2C_KEY_WORDS[pno] ;
    }

}
