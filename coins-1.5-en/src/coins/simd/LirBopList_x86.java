/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.List;
import java.util.LinkedList;
import coins.backend.Function;
import coins.backend.SyntaxError;
import coins.backend.lir.LirNode;
import coins.backend.util.ImList;

/**
 * BopList class
 */
public class LirBopList_x86 extends LirBopList {
  /**
   * Matching patterns.
   */
  public LirNode[] templateList=new LirNode[146];
  /**
   * Constructs LirBopList and initializes bopList.
   * @param f Function
   */
  public LirBopList_x86(Function f) {
    init(f);
  }
  /**
   * Initializes bopList.
   */
  public LirNode[] initTemplist() {
//    templateList[0]=mkBop("(ADD I8 (HOLE 0 I8) (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (BOR I8 (BAND I8 (SUB I8 (HOLE 1 I8) (HOLE 2 I8)) (TSTLES I8 (HOLE 2 I8) (HOLE 1 I8))) (BAND I8 (SUB I8 (HOLE 2 I8) (HOLE 1 I8)) (BNOT I8 (TSTLES I8 (HOLE 2 I8) (HOLE 1 I8))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 3 I8) (HOLE 4 I8)) (TSTLES I8 (HOLE 4 I8) (HOLE 3 I8))) (BAND I8 (SUB I8 (HOLE 4 I8) (HOLE 3 I8)) (BNOT I8 (TSTLES I8 (HOLE 4 I8) (HOLE 3 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 5 I8) (HOLE 6 I8)) (TSTLES I8 (HOLE 6 I8) (HOLE 5 I8))) (BAND I8 (SUB I8 (HOLE 6 I8) (HOLE 5 I8)) (BNOT I8 (TSTLES I8 (HOLE 6 I8) (HOLE 5 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 7 I8) (HOLE 8 I8)) (TSTLES I8 (HOLE 8 I8) (HOLE 7 I8))) (BAND I8 (SUB I8 (HOLE 8 I8) (HOLE 7 I8)) (BNOT I8 (TSTLES I8 (HOLE 8 I8) (HOLE 7 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 9 I8) (HOLE 10 I8)) (TSTLES I8 (HOLE 10 I8) (HOLE 9 I8))) (BAND I8 (SUB I8 (HOLE 10 I8) (HOLE 9 I8)) (BNOT I8 (TSTLES I8 (HOLE 10 I8) (HOLE 9 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 11 I8) (HOLE 12 I8)) (TSTLES I8 (HOLE 12 I8) (HOLE 11 I8))) (BAND I8 (SUB I8 (HOLE 12 I8) (HOLE 11 I8)) (BNOT I8 (TSTLES I8 (HOLE 12 I8) (HOLE 11 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 13 I8) (HOLE 14 I8)) (TSTLES I8 (HOLE 14 I8) (HOLE 13 I8))) (BAND I8 (SUB I8 (HOLE 14 I8) (HOLE 13 I8)) (BNOT I8 (TSTLES I8 (HOLE 14 I8) (HOLE 13 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 15 I8) (HOLE 16 I8)) (TSTLES I8 (HOLE 16 I8) (HOLE 15 I8))) (BAND I8 (SUB I8 (HOLE 16 I8) (HOLE 15 I8)) (BNOT I8 (TSTLES I8 (HOLE 16 I8) (HOLE 15 I8)))))))");

    templateList[0]=mkBop("(ADD I32 (ADD I32 (ADD I32 (ADD I32 (ADD I32 (ADD I32 (ADD I32 (ADD I32 (HOLE 0 I32) (BOR I32 (BAND I32 (SUB I32 (HOLE 2 I32) (HOLE 1 I32)) (TSTLES I32 (HOLE 1 I32) (HOLE 2 I32))) (BAND I32 (SUB I32 (HOLE 1 I32) (HOLE 2 I32)) (BNOT I32 (TSTLES I32 (HOLE 1 I32) (HOLE 2 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 4 I32) (HOLE 3 I32)) (TSTLES I32 (HOLE 3 I32) (HOLE 4 I32))) (BAND I32 (SUB I32 (HOLE 3 I32) (HOLE 4 I32)) (BNOT I32 (TSTLES I32 (HOLE 3 I32) (HOLE 4 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 6 I32) (HOLE 5 I32)) (TSTLES I32 (HOLE 5 I32) (HOLE 6 I32))) (BAND I32 (SUB I32 (HOLE 5 I32) (HOLE 6 I32)) (BNOT I32 (TSTLES I32 (HOLE 5 I32) (HOLE 6 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 8 I32) (HOLE 7 I32)) (TSTLES I32 (HOLE 7 I32) (HOLE 8 I32))) (BAND I32 (SUB I32 (HOLE 7 I32) (HOLE 8 I32)) (BNOT I32 (TSTLES I32 (HOLE 7 I32) (HOLE 8 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 10 I32) (HOLE 9 I32)) (TSTLES I32 (HOLE 9 I32) (HOLE 10 I32))) (BAND I32 (SUB I32 (HOLE 9 I32) (HOLE 10 I32)) (BNOT I32 (TSTLES I32 (HOLE 9 I32) (HOLE 10 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 12 I32) (HOLE 11 I32)) (TSTLES I32 (HOLE 11 I32) (HOLE 12 I32))) (BAND I32 (SUB I32 (HOLE 11 I32) (HOLE 12 I32)) (BNOT I32 (TSTLES I32 (HOLE 11 I32) (HOLE 12 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 14 I32) (HOLE 13 I32)) (TSTLES I32 (HOLE 13 I32) (HOLE 14 I32))) (BAND I32 (SUB I32 (HOLE 13 I32) (HOLE 14 I32)) (BNOT I32 (TSTLES I32 (HOLE 13 I32) (HOLE 14 I32)))))) (BOR I32 (BAND I32 (SUB I32 (HOLE 16 I32) (HOLE 15 I32)) (TSTLES I32 (HOLE 15 I32) (HOLE 16 I32))) (BAND I32 (SUB I32 (HOLE 15 I32) (HOLE 16 I32)) (BNOT I32 (TSTLES I32 (HOLE 15 I32) (HOLE 16 I32))))))");

// For pavgb
    templateList[1]=mkBop("(ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (BOR I8 (BAND I8 (SUB I8 (HOLE 1 I8) (HOLE 2 I8)) (TSTLES I8 (HOLE 2 I8) (HOLE 1 I8))) (BAND I8 (SUB I8 (HOLE 2 I8) (HOLE 1 I8)) (BNOT I8 (TSTLES I8 (HOLE 2 I8) (HOLE 1 I8))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 3 I8) (HOLE 4 I8)) (TSTLES I8 (HOLE 4 I8) (HOLE 3 I8))) (BAND I8 (SUB I8 (HOLE 4 I8) (HOLE 3 I8)) (BNOT I8 (TSTLES I8 (HOLE 4 I8) (HOLE 3 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 5 I8) (HOLE 6 I8)) (TSTLES I8 (HOLE 6 I8) (HOLE 5 I8))) (BAND I8 (SUB I8 (HOLE 6 I8) (HOLE 5 I8)) (BNOT I8 (TSTLES I8 (HOLE 6 I8) (HOLE 5 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 7 I8) (HOLE 8 I8)) (TSTLES I8 (HOLE 8 I8) (HOLE 7 I8))) (BAND I8 (SUB I8 (HOLE 8 I8) (HOLE 7 I8)) (BNOT I8 (TSTLES I8 (HOLE 8 I8) (HOLE 7 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 9 I8) (HOLE 10 I8)) (TSTLES I8 (HOLE 10 I8) (HOLE 9 I8))) (BAND I8 (SUB I8 (HOLE 10 I8) (HOLE 9 I8)) (BNOT I8 (TSTLES I8 (HOLE 10 I8) (HOLE 9 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 11 I8) (HOLE 12 I8)) (TSTLES I8 (HOLE 12 I8) (HOLE 11 I8))) (BAND I8 (SUB I8 (HOLE 12 I8) (HOLE 11 I8)) (BNOT I8 (TSTLES I8 (HOLE 12 I8) (HOLE 11 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 13 I8) (HOLE 14 I8)) (TSTLES I8 (HOLE 14 I8) (HOLE 13 I8))) (BAND I8 (SUB I8 (HOLE 14 I8) (HOLE 13 I8)) (BNOT I8 (TSTLES I8 (HOLE 14 I8) (HOLE 13 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 15 I8) (HOLE 16 I8)) (TSTLES I8 (HOLE 16 I8) (HOLE 15 I8))) (BAND I8 (SUB I8 (HOLE 16 I8) (HOLE 15 I8)) (BNOT I8 (TSTLES I8 (HOLE 16 I8) (HOLE 15 I8))))))");

// For sum of Vector elements
    templateList[2]=mkBop("(ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (HOLE 1 I8) (HOLE 2 I8)) (HOLE 3 I8)) (HOLE 4 I8)) (HOLE 5 I8)) (HOLE 6 I8)) (HOLE 7 I8)) (HOLE 8 I8)) (HOLE 9 I8)) (HOLE 10 I8)) (HOLE 11 I8)) (HOLE 12 I8)) (HOLE 13 I8)) (HOLE 14 I8)) (HOLE 15 I8)) (HOLE 16 I8))");
    templateList[3]=mkBop("(ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (HOLE 1 I8) (HOLE 2 I8)) (HOLE 3 I8)) (HOLE 4 I8)) (HOLE 5 I8)) (HOLE 6 I8)) (HOLE 7 I8)) (HOLE 8 I8))");
    templateList[4]=mkBop("(ADD I16 (ADD I16 (ADD I16 (ADD I16 (ADD I16 (ADD I16 (ADD I16 (HOLE 1 I16) (HOLE 2 I16)) (HOLE 3 I16)) (HOLE 4 I16)) (HOLE 5 I16)) (HOLE 6 I16)) (HOLE 7 I16)) (HOLE 8 I16))");
    templateList[5]=mkBop("(ADD I16 (ADD I16 (ADD I16 (HOLE 1 I16) (HOLE 2 I16)) (HOLE 3 I16)) (HOLE 4 I16))");
    templateList[6]=mkBop("(ADD I32 (ADD I32 (ADD I32 (HOLE 1 I32) (HOLE 2 I32)) (HOLE 3 I32)) (HOLE 4 I32))");

//
    templateList[7]=mkBop("(ADD I8 (ADD I8 (RSHS I8 (HOLE 1 I8) (INTCONST I8 1)) (RSHS I8 (HOLE 2 I8) (INTCONST I8 1))) (BAND I8 (BOR I8 (HOLE 1 I8) (HOLE 2 I8)) (INTCONST I8 1)))");
    templateList[8]=mkBop("(ADD I16 (ADD I16 (RSHS I16 (HOLE 1 I16) (INTCONST I16 1)) (RSHS I16 (HOLE 2 I16) (INTCONST I16 1))) (BAND I16 (BOR I16 (HOLE 1 I16) (HOLE 2 I16)) (INTCONST I16 1)))");
// For pmax,pmin
    templateList[9]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTGES I8 (HOLE 1 I8) (HOLE 2 I8))) (BAND I8 (HOLE 2 I8) (BNOT I8 (TSTGES I8 (HOLE 1 I8) (HOLE 2 I8)))))");
    templateList[10]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTGES I16 (HOLE 1 I16) (HOLE 2 I16))) (BAND I16 (HOLE 2 I16) (BNOT I16 (TSTGES I16 (HOLE 1 I16) (HOLE 2 I16)))))");
    templateList[11]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTLTS I8 (HOLE 1 I8) (HOLE 2 I8))) (BAND I8 (HOLE 2 I8) (BNOT I8 (TSTLTS I8 (HOLE 1 I8) (HOLE 2 I8)))))");
    templateList[12]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTLTS I16 (HOLE 1 I16) (HOLE 2 I16))) (BAND I16 (HOLE 2 I16) (BNOT I16 (TSTLTS I16 (HOLE 1 I16) (HOLE 2 I16)))))");
// For pcmpeq,pcmpgt
    templateList[13]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTEQ I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTEQ I8 (HOLE 2 I8) (HOLE 3 I8)))))");
    templateList[14]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTEQ I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTEQ I16 (HOLE 2 I16) (HOLE 3 I16)))))");
    templateList[15]=mkBop("(BOR I32 (BAND I32 (HOLE 1 I32) (TSTEQ I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTEQ I32 (HOLE 2 I32) (HOLE 3 I32)))))");
    templateList[16]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTGTS I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTGTS I8 (HOLE 2 I8) (HOLE 3 I8)))))");
    templateList[17]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTGTS I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTGTS I16 (HOLE 2 I16) (HOLE 3 I16)))))");
    templateList[18]=mkBop("(BOR I32 (BAND I32 (HOLE 1 I32) (TSTGTS I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTGTS I32 (HOLE 2 I32) (HOLE 3 I32)))))");
    templateList[19]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTNE I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTNE I8 (HOLE 2 I8) (HOLE 3 I8)))))");
    templateList[20]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTNE I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTNE I16 (HOLE 2 I16) (HOLE 3 I16)))))");
    templateList[21]=mkBop("(BOR I32 (BAND I32 (HOLE 1 I32) (TSTNE I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTNE I32 (HOLE 2 I32) (HOLE 3 I32)))))");
    templateList[22]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTGES I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTGES I8 (HOLE 2 I8) (HOLE 3 I8)))))");
    templateList[23]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTGES I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTGES I16 (HOLE 2 I16) (HOLE 3 I16)))))");
    templateList[24]=mkBop("(BOR I32 (BAND I32 (HOLE 1 I32) (TSTGES I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTGES I32 (HOLE 2 I32) (HOLE 3 I32)))))");
    templateList[25]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTLTS I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTLTS I8 (HOLE 2 I8) (HOLE 3 I8)))))");
    templateList[26]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTLTS I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTLTS I16 (HOLE 2 I16) (HOLE 3 I16)))))");
    templateList[27]=mkBop("(BOR I32 (BAND I32 (HOLE 1 I32) (TSTLTS I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTLTS I32 (HOLE 2 I32) (HOLE 3 I32)))))");
    templateList[28]=mkBop("(BOR I8 (BAND I8 (HOLE 1 I8) (TSTLES I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTLES I8 (HOLE 2 I8) (HOLE 3 I8)))))");
    templateList[29]=mkBop("(BOR I16 (BAND I16 (HOLE 1 I16) (TSTLES I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTLES I16 (HOLE 2 I16) (HOLE 3 I16)))))");
    templateList[30]=mkBop("(BOR I32 (BAND I32 (HOLE 1 I32) (TSTLES I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTLES I32 (HOLE 2 I32) (HOLE 3 I32)))))");

    templateList[31]=mkBop("(RSHS I8 (ADD I8 (ADD I8 (HOLE 1 I8) (HOLE 2 I8)) (INTCONST I8 1)) (INTCONST I8 1))");
    templateList[32]=mkBop("(RSHS I16 (ADD I16 (ADD I16 (HOLE 1 I16) (HOLE 2 I16)) (INTCONST I16 1)) (INTCONST I16 1))");
    templateList[33]=mkBop("(CONVIT I8 (ADD I32 (CONVZX I32 (HOLE 1 I8)) (CONVZX I32 (HOLE 2 I8))))");

    templateList[34]=mkBop("(MEM I32 (ADD I32 (HOLE 1 I32) (HOLE 2 I32)))");
    templateList[35]=mkBop("(MEM I32 (HOLE 1 I32))");
    templateList[36]=mkBop("(MEM I16 (ADD I32 (HOLE 1 I32) (HOLE 2 I32)))");
    templateList[37]=mkBop("(MEM I16 (HOLE 1 I32))");
    templateList[38]=mkBop("(MEM I8 (ADD I32 (HOLE 1 I32) (HOLE 2 I32)))");
    templateList[39]=mkBop("(MEM I8 (HOLE 1 I32))");

    templateList[40]=mkBop("(ADD I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[41]=mkBop("(ADD I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[42]=mkBop("(ADD I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[43]=mkBop("(SUB I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[44]=mkBop("(SUB I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[45]=mkBop("(SUB I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[46]=mkBop("(MUL I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[47]=mkBop("(MUL I16 (HOLE 1 I16) (HOLE 2 I16))");

    templateList[48]=mkBop("(BAND I32 (BNOT I32 (HOLE 1 I32)) (HOLE 2 I32))");
    templateList[49]=mkBop("(BAND I16 (BNOT I16 (HOLE 1 I16)) (HOLE 2 I16))");
    templateList[50]=mkBop("(BAND I8 (BNOT I8 (HOLE 1 I8)) (HOLE 2 I8))");
    templateList[51]=mkBop("(BAND I128 (HOLE 1 I128) (HOLE 2 I128))");
    templateList[52]=mkBop("(BAND I64 (HOLE 1 I64) (HOLE 2 I64))");
    templateList[53]=mkBop("(BAND I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[54]=mkBop("(BAND I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[55]=mkBop("(BAND I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[56]=mkBop("(BOR I128 (HOLE 1 I128) (HOLE 2 I128))");
    templateList[57]=mkBop("(BOR I64 (HOLE 1 I64) (HOLE 2 I64))");
    templateList[58]=mkBop("(BOR I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[59]=mkBop("(BOR I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[60]=mkBop("(BOR I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[61]=mkBop("(BXOR I128 (HOLE 1 I128) (HOLE 2 I128))");
    templateList[62]=mkBop("(BXOR I64 (HOLE 1 I64) (HOLE 2 I64))");
    templateList[63]=mkBop("(BXOR I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[64]=mkBop("(BXOR I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[65]=mkBop("(BXOR I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[66]=mkBop("(NEG I32 (HOLE 1 I32))");
    templateList[67]=mkBop("(NEG I16 (HOLE 1 I16))");
    templateList[68]=mkBop("(NEG I8 (HOLE 1 I8))");
    templateList[69]=mkBop("(BNOT I32 (HOLE 1 I32))");
    templateList[70]=mkBop("(BNOT I16 (HOLE 1 I16))");
    templateList[71]=mkBop("(BNOT I8 (HOLE 1 I8))");
    templateList[72]=mkBop("(LSHU I128 (HOLE 1 I128) (HOLE 2 I128))");
    templateList[73]=mkBop("(LSHU I64 (HOLE 1 I64) (HOLE 2 I64))");
    templateList[74]=mkBop("(LSHU I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[75]=mkBop("(LSHU I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[76]=mkBop("(LSHU I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[77]=mkBop("(LSHS I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[78]=mkBop("(LSHS I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[79]=mkBop("(LSHS I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[80]=mkBop("(RSHU I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[81]=mkBop("(RSHU I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[82]=mkBop("(RSHU I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[83]=mkBop("(RSHS I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[84]=mkBop("(RSHS I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[85]=mkBop("(RSHS I8 (HOLE 1 I8) (HOLE 2 I8))");

    templateList[86]=mkBop("(TSTGTS I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[87]=mkBop("(TSTGTS I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[88]=mkBop("(TSTGTS I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[89]=mkBop("(TSTGES I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[90]=mkBop("(TSTGES I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[91]=mkBop("(TSTGES I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[92]=mkBop("(TSTLTS I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[93]=mkBop("(TSTLTS I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[94]=mkBop("(TSTLTS I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[95]=mkBop("(TSTLES I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[96]=mkBop("(TSTLES I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[97]=mkBop("(TSTLES I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[98]=mkBop("(TSTEQ I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[99]=mkBop("(TSTEQ I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[100]=mkBop("(TSTEQ I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[101]=mkBop("(TSTNE I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[102]=mkBop("(TSTNE I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[103]=mkBop("(TSTNE I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[104]=mkBop("(TSTLTU I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[105]=mkBop("(TSTLTU I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[106]=mkBop("(TSTLTU I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[107]=mkBop("(TSTGTU I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[108]=mkBop("(TSTGTU I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[109]=mkBop("(TSTGTU I8 (HOLE 1 I8) (HOLE 2 I8))");
    templateList[110]=mkBop("(TSTGEU I32 (HOLE 1 I32) (HOLE 2 I32))");
    templateList[111]=mkBop("(TSTGEU I16 (HOLE 1 I16) (HOLE 2 I16))");
    templateList[112]=mkBop("(TSTGEU I8 (HOLE 1 I8) (HOLE 2 I8))");

    templateList[113]=mkBop("(CONVZX I32 (HOLE 1 I16))");
    templateList[114]=mkBop("(CONVZX I32 (HOLE 1 I8))");
    templateList[115]=mkBop("(CONVSX I128 (HOLE 1 I32))");
    templateList[116]=mkBop("(CONVSX I64 (HOLE 1 I32))");
    templateList[117]=mkBop("(CONVSX I128 (HOLE 1 I16))");
    templateList[118]=mkBop("(CONVSX I64 (HOLE 1 I16))");
    templateList[119]=mkBop("(CONVSX I32 (HOLE 1 I16))");
    templateList[120]=mkBop("(CONVSX I128 (HOLE 1 I8))");
    templateList[121]=mkBop("(CONVSX I64 (HOLE 1 I8))");
    templateList[122]=mkBop("(CONVSX I32 (HOLE 1 I8))");
    templateList[123]=mkBop("(CONVSX I16 (HOLE 1 I8))");
    templateList[124]=mkBop("(CONVIT I16 (HOLE 1 I32))");
    templateList[125]=mkBop("(CONVIT I8 (HOLE 1 I32))");
    templateList[126]=mkBop("(MEM F32 (ADD I32 (HOLE 1 I32) (HOLE 2 I32)))");
    templateList[127]=mkBop("(MEM F32 (HOLE 1 I32))");
    templateList[128]=mkBop("(MEM F64 (ADD I32 (HOLE 1 I32) (HOLE 2 I32)))");
    templateList[129]=mkBop("(MEM F64 (HOLE 1 I32))");
    templateList[130]=mkBop("(ADD F32 (HOLE 1 F32) (HOLE 2 F32))");
//    templateList[131]=mkBop("(ADD F64 (HOLE 1 I64) (HOLE 2 F64))");
    templateList[131]=mkBop("(ADD F64 (HOLE 1 F64) (HOLE 2 F64))");
    templateList[132]=mkBop("(SUB F32 (HOLE 1 F32) (HOLE 2 F32))");
    templateList[133]=mkBop("(SUB F64 (HOLE 1 F64) (HOLE 2 F64))");
    templateList[134]=mkBop("(MUL F32 (HOLE 1 F32) (HOLE 2 F32))");
    templateList[135]=mkBop("(MUL F64 (HOLE 1 F64) (HOLE 2 F64))");
    templateList[136]=mkBop("(DIVS F32 (HOLE 1 F32) (HOLE 2 F32))");
    templateList[137]=mkBop("(DIVS F64 (HOLE 1 F64) (HOLE 2 F64))");
    templateList[138]=mkBop("(CONVFT F32 (HOLE 1 F64))");
    templateList[139]=mkBop("(CONVFX F64 (HOLE 1 F32))");
    templateList[140]=mkBop("(CONVSF F32 (HOLE 1 I32))");

    templateList[141]=mkBop("(CONVSF F64 (HOLE 1 I64))");

    templateList[142]=mkBop("(CONVFI I32 (HOLE 1 F32))");
    templateList[143]=mkBop("(CONVFI I64 (HOLE 1 F64))");

    templateList[144]=mkBop("(CONVIT I8 (HOLE 1 I16))");
    templateList[145]=mkBop("(MODS I8 (HOLE 1 I8) (HOLE 2 I8))");

    return templateList;
  };
}