/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.hir2c;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import coins.ir.hir.HIR;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;
////////////////////////////////////////////////////////////////////////////////////////////
//
//   HIR-Base  To  C  Language ( PrintDef) 
//
////////////////////////////////////////////////////////////////////////////////////////////
public class  PrintDef {
    private PrintWriter printOut;
    private KeyWords KeyWord; 
    private Hashtable DefListSys; 
    public Hashtable DefList; 
    /**
    *
    * PrintDef:
    *  
    **/
    PrintDef(PrintWriter pOut) {
        printOut = pOut;
        KeyWord = new KeyWords();
        DefList = new Hashtable();
        DefListSys = new Hashtable();
        //setDefList();
    }
    /**
    *
    * setDefList:
    *
    **/
    void setDefList() {    
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_ENCLOSE) , "(a)  (a)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_ADD), "(a,b) (a) + (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_SUB), "(a,b) (a) - (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_MULT) , "(a,b) (a) * (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_DIV), "(a,b) (a) / (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_MOD), "(a,b) (a) % (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_AND) , "(a,b) (a) & (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_OR), "(a,b) (a) | (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_XOR), "(a,b) (a) ^ (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CMP_EQ), "(a,b) (a) == (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CMP_NE), "(a,b) (a) != (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CMP_GT), "(a,b) (a) > (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CMP_GE), "(a,b) (a) >= (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CMP_LT), "(a,b) (a) < (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CMP_LE), "(a,b) (a) <= (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_SHIFT_LL), "(a,b) (a) << (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_SHIFT_R), "(a,b) (a) >> (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_SHIFT_RL) , "(a,b) (a) >> (b)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_NOT) , "(a) ~(a)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_NEG) , "(a) -(a)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_ADDR) , "(a) &(a)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_DECAY) , "(a) (a)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_UNDECAY) , "(a,b) (a)");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_CONTENTS), " *");
        DefListSys.put(KeyWord.getOpKeyWord(HIR.OP_NULL), " ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_BOOL) , " int ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_SHORT) , " short ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_INT) , " int ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_OFFSET) , " int ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_LONG) , " long ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_LONG_LONG) , " long long ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_CHAR) , " char ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_U_CHAR), " unsigned char ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_U_SHORT), " unsigned short ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_U_INT), " unsigned int ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_U_LONG), " unsigned long ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_U_LONG_LONG) , " unsigned long long ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_ADDRESS) , " ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_VOID) , " void ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_FLOAT) , " float ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_DOUBLE) , " double ");
        DefListSys.put(KeyWord.getTypeKeyWord(Type.KIND_LONG_DOUBLE) , " long double ");
        DefListSys.put(KeyWord.getSymKeyWord(Sym.SYM_EXTERN) , " extern ");
        DefListSys.put(KeyWord.getSymKeyWord(Sym.SYM_PUBLIC) , " ");
        DefListSys.put(KeyWord.getSymKeyWord(Sym.SYM_PROTECTED) , " ");
        DefListSys.put(KeyWord.getSymKeyWord(Sym.SYM_PRIVATE) , " ");
        DefListSys.put(KeyWord.getSymKeyWord(Sym.SYM_COMPILE_UNIT) ,"  ");
        DefListSys.put(KeyWord.getSymKeyWord(Var.VAR_STATIC) ," static ");
        DefListSys.put(KeyWord.getSymKeyWord(Var.VAR_AUTO) ,"  ");
        DefListSys.put(KeyWord.getSymKeyWord(Var.VAR_REGISTER) ," register ");

        // a[LowerBound(i,n)]
        DefListSys.put(KeyWord.getHir2cKeyWord(KeyWords.HIR2C_LOWERBOUND) ,"(a,b) (a) - (b)");

        // HIR :a = "String"  C : memcpy(a,"String",n); 
        DefListSys.put(KeyWord.getHir2cKeyWord(KeyWords.HIR2C_STRINGCOPY) ,"(a,b,c) memcpy(a,b,c)");

        // HIR p+n*size       C:(char*)p+n*size
        DefListSys.put(KeyWord.getHir2cKeyWord(KeyWords.HIR2C_POINTER) ,"(a) ((char*)a)");
    }
    /**
    *
    * Converter:
    *
    **/
    void Converter() {    
        Set keySetIt ;
        Iterator Ie;
        keySetIt = DefListSys.keySet();
        for (Ie=keySetIt.iterator();Ie.hasNext();) {
            String DefName = (String) Ie.next();
            String DefValue = (String)DefListSys.get(DefName);
            PrintLine("#define " + DefName + DefValue);
        }
        keySetIt = DefList.keySet();
        for (Ie=keySetIt.iterator();Ie.hasNext();) {
            String DefName = (String) Ie.next();
            String DefValue = (String)DefList.get(DefName);
            PrintLine("#define " + DefName + DefValue);
        }
    }
    /*
    *
    * PrintLine:
    *
    **/
    private void PrintLine(String s) {
        printOut.println(s);
    }
}
