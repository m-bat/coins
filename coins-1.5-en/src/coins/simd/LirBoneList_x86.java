/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.io.PushbackReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.LinkedList;
import coins.backend.Function;
import coins.backend.Op;
import coins.backend.SyntaxError;
import coins.backend.lir.LirNode;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;

/**
 * BoneList class.
 */
public class LirBoneList_x86 extends LirBoneList {
  public ImList[] templateList=new ImList[124];

  public ImList[] auxtemplateList=new ImList[17];

  /**
   * Constructs LirBoneList.
   * Initializes boneList and rewriteList.
   * @param  f  Function
   */
  public LirBoneList_x86(Function f) {
    init(f);
  }

  /**
   * Initializes boneList.
   */
  public ImList[] initBoneList() {
//For psadbw
    templateList[0]=
      mkBone("((1) 0 nil nil nil nil (1) ((0) (2 4 6 8 10 12 14 16) (3 5 7 9 11 13 15 17)))","(SET I8 (HOLE 0 I8) (ADD I8 (HOLE 1 I8) (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (BOR I8 (BAND I8 (SUB I8 (HOLE 2 I8) (HOLE 3 I8)) (TSTLES I8 (HOLE 3 I8) (HOLE 2 I8))) (BAND I8 (SUB I8 (HOLE 3 I8) (HOLE 2 I8)) (BNOT I8 (TSTLES I8 (HOLE 3 I8) (HOLE 2 I8))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 4 I8) (HOLE 5 I8)) (TSTLES I8 (HOLE 5 I8) (HOLE 4 I8))) (BAND I8 (SUB I8 (HOLE 5 I8) (HOLE 4 I8)) (BNOT I8 (TSTLES I8 (HOLE 5 I8) (HOLE 4 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 6 I8) (HOLE 7 I8)) (TSTLES I8 (HOLE 7 I8) (HOLE 6 I8))) (BAND I8 (SUB I8 (HOLE 7 I8) (HOLE 6 I8)) (BNOT I8 (TSTLES I8 (HOLE 7 I8) (HOLE 6 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 8 I8) (HOLE 9 I8)) (TSTLES I8 (HOLE 9 I8) (HOLE 8 I8))) (BAND I8 (SUB I8 (HOLE 9 I8) (HOLE 8 I8)) (BNOT I8 (TSTLES I8 (HOLE 9 I8) (HOLE 8 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 10 I8) (HOLE 11 I8)) (TSTLES I8 (HOLE 11 I8) (HOLE 10 I8))) (BAND I8 (SUB I8 (HOLE 11 I8) (HOLE 10 I8)) (BNOT I8 (TSTLES I8 (HOLE 11 I8) (HOLE 10 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 12 I8) (HOLE 13 I8)) (TSTLES I8 (HOLE 13 I8) (HOLE 12 I8))) (BAND I8 (SUB I8 (HOLE 13 I8) (HOLE 12 I8)) (BNOT I8 (TSTLES I8 (HOLE 13 I8) (HOLE 12 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 14 I8) (HOLE 15 I8)) (TSTLES I8 (HOLE 15 I8) (HOLE 14 I8))) (BAND I8 (SUB I8 (HOLE 15 I8) (HOLE 14 I8)) (BNOT I8 (TSTLES I8 (HOLE 15 I8) (HOLE 14 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 16 I8) (HOLE 17 I8)) (TSTLES I8 (HOLE 17 I8) (HOLE 16 I8))) (BAND I8 (SUB I8 (HOLE 17 I8) (HOLE 16 I8)) (BNOT I8 (TSTLES I8 (HOLE 17 I8) (HOLE 16 I8))))))))");
    templateList[1]=
      mkBone("((1) 0 nil nil nil nil nil ((0) (2 4 6 8 10 12 14 16) (1 3 5 7 9 11 13 15)))","(SET I8 (HOLE 0 I8) (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (ADD I8 (BOR I8 (BAND I8 (SUB I8 (HOLE 1 I8) (HOLE 2 I8)) (TSTLES I8 (HOLE 2 I8) (HOLE 1 I8))) (BAND I8 (SUB I8 (HOLE 2 I8) (HOLE 1 I8)) (BNOT I8 (TSTLES I8 (HOLE 2 I8) (HOLE 1 I8))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 3 I8) (HOLE 4 I8)) (TSTLES I8 (HOLE 4 I8) (HOLE 3 I8))) (BAND I8 (SUB I8 (HOLE 4 I8) (HOLE 3 I8)) (BNOT I8 (TSTLES I8 (HOLE 4 I8) (HOLE 3 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 5 I8) (HOLE 6 I8)) (TSTLES I8 (HOLE 6 I8) (HOLE 5 I8))) (BAND I8 (SUB I8 (HOLE 6 I8) (HOLE 5 I8)) (BNOT I8 (TSTLES I8 (HOLE 6 I8) (HOLE 5 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 7 I8) (HOLE 8 I8)) (TSTLES I8 (HOLE 8 I8) (HOLE 7 I8))) (BAND I8 (SUB I8 (HOLE 8 I8) (HOLE 7 I8)) (BNOT I8 (TSTLES I8 (HOLE 8 I8) (HOLE 7 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 9 I8) (HOLE 10 I8)) (TSTLES I8 (HOLE 10 I8) (HOLE 9 I8))) (BAND I8 (SUB I8 (HOLE 10 I8) (HOLE 9 I8)) (BNOT I8 (TSTLES I8 (HOLE 10 I8) (HOLE 9 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 11 I8) (HOLE 12 I8)) (TSTLES I8 (HOLE 12 I8) (HOLE 11 I8))) (BAND I8 (SUB I8 (HOLE 12 I8) (HOLE 11 I8)) (BNOT I8 (TSTLES I8 (HOLE 12 I8) (HOLE 11 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 13 I8) (HOLE 14 I8)) (TSTLES I8 (HOLE 14 I8) (HOLE 13 I8))) (BAND I8 (SUB I8 (HOLE 14 I8) (HOLE 13 I8)) (BNOT I8 (TSTLES I8 (HOLE 14 I8) (HOLE 13 I8)))))) (BOR I8 (BAND I8 (SUB I8 (HOLE 15 I8) (HOLE 16 I8)) (TSTLES I8 (HOLE 16 I8) (HOLE 15 I8))) (BAND I8 (SUB I8 (HOLE 16 I8) (HOLE 15 I8)) (BNOT I8 (TSTLES I8 (HOLE 16 I8) (HOLE 15 I8)))))))");

//For pavg
    templateList[2]=
      mkBone("((16 8) 1 t)","(SET I8 (HOLE 0 I8) (RSHS I8 (ADD I8 (ADD I8 (HOLE 1 I8) (HOLE 2 I8)) (INTCONST I8 1)) (INTCONST I8 1)))");
    templateList[3]=
      mkBone("((8 4) 1 t)","(SET I16 (HOLE 0 I16) (RSHS I16 (ADD I16 (ADD I16 (HOLE 1 I16) (HOLE 2 I16)) (INTCONST I16 1)) (INTCONST I16 1)))");
    templateList[4]=
      mkBone("((16 8) 1 t)","(SET I8 (HOLE 0 I8) (ADD I8 (ADD I8 (RSHS I8 (HOLE 1 I8) (INTCONST I8 1)) (RSHS I8 (HOLE 2 I8) (INTCONST I8 1))) (BAND I8 (BOR I8 (HOLE 1 I8) (HOLE 2 I8)) (INTCONST I8 1))))");
    templateList[5]=
      mkBone("((8 4) 1 t)","(SET I16 (HOLE 0 I16) (ADD I16 (ADD I16 (RSHS I16 (HOLE 1 I16) (INTCONST I16 1)) (RSHS I16 (HOLE 2 I16) (INTCONST I16 1))) (BAND I16 (BOR I16 (HOLE 1 I16) (HOLE 2 I16)) (INTCONST I16 1))))");
//For pmax,pmin
    templateList[6]=
      mkBone("((16 8) 1 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTGES I8 (HOLE 1 I8) (HOLE 2 I8))) (BAND I8 (HOLE 2 I8) (BNOT I8 (TSTGES I8 (HOLE 1 I8) (HOLE 2 I8))))))");
    templateList[7]=
      mkBone("((8 4) 1 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTGES I16 (HOLE 1 I16) (HOLE 2 I16))) (BAND I16 (HOLE 2 I16) (BNOT I16 (TSTGES I16 (HOLE 1 I16) (HOLE 2 I16))))))");
    templateList[8]=
      mkBone("((16 8) 1 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTLTS I8 (HOLE 1 I8) (HOLE 2 I8))) (BAND I8 (HOLE 2 I8) (BNOT I8 (TSTLTS I8 (HOLE 1 I8) (HOLE 2 I8))))))");
    templateList[9]=
      mkBone("((8 4) 1 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTLTS I16 (HOLE 1 I16) (HOLE 2 I16))) (BAND I16 (HOLE 2 I16) (BNOT I16 (TSTLTS I16 (HOLE 1 I16) (HOLE 2 I16))))))");
//For pcmpeq,pcmpgt,...
    templateList[10]=
      mkBone("((16 8) 0 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTEQ I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTEQ I8 (HOLE 2 I8) (HOLE 3 I8))))))");
    templateList[11]=
      mkBone("((8 4) 0 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTEQ I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTEQ I16 (HOLE 2 I16) (HOLE 3 I16))))))");
    templateList[12]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (BOR I32 (BAND I32 (HOLE 1 I32) (TSTEQ I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTEQ I32 (HOLE 2 I32) (HOLE 3 I32))))))");
    templateList[13]=
      mkBone("((16 8) 0 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTGTS I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTGTS I8 (HOLE 2 I8) (HOLE 3 I8))))))");
    templateList[14]=
      mkBone("((8 4) 0 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTGTS I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTGTS I16 (HOLE 2 I16) (HOLE 3 I16))))))");
    templateList[15]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (BOR I32 (BAND I32 (HOLE 1 I32) (TSTGTS I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTGTS I32 (HOLE 2 I32) (HOLE 3 I32))))))");
    templateList[16]=
      mkBone("((16 8) 0 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTNE I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTNE I8 (HOLE 2 I8) (HOLE 3 I8))))))");
    templateList[17]=
      mkBone("((8 4) 0 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTNE I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTNE I16 (HOLE 2 I16) (HOLE 3 I16))))))");
    templateList[18]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (BOR I32 (BAND I32 (HOLE 1 I32) (TSTNE I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTNE I32 (HOLE 2 I32) (HOLE 3 I32))))))");
    templateList[19]=
      mkBone("((16 8) 0 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTGES I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTGES I8 (HOLE 2 I8) (HOLE 3 I8))))))");
    templateList[20]=
      mkBone("((8 4) 0 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTGES I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTGES I16 (HOLE 2 I16) (HOLE 3 I16))))))");
    templateList[21]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (BOR I32 (BAND I32 (HOLE 1 I32) (TSTGES I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTGES I32 (HOLE 2 I32) (HOLE 3 I32))))))");
    templateList[22]=
      mkBone("((16 8) 0 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTLTS I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTLTS I8 (HOLE 2 I8) (HOLE 3 I8))))))");
    templateList[23]=
      mkBone("((8 4) 0 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTLTS I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTLTS I16 (HOLE 2 I16) (HOLE 3 I16))))))");
    templateList[24]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (BOR I32 (BAND I32 (HOLE 1 I32) (TSTLTS I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTLTS I32 (HOLE 2 I32) (HOLE 3 I32))))))");
    templateList[25]=
      mkBone("((16 8) 0 nil)","(SET I8 (HOLE 0 I8) (BOR I8 (BAND I8 (HOLE 1 I8) (TSTLES I8 (HOLE 2 I8) (HOLE 3 I8))) (BAND I8 (HOLE 4 I8) (BNOT I8 (TSTLES I8 (HOLE 2 I8) (HOLE 3 I8))))))");
    templateList[26]=
      mkBone("((8 4) 0 nil)","(SET I16 (HOLE 0 I16) (BOR I16 (BAND I16 (HOLE 1 I16) (TSTLES I16 (HOLE 2 I16) (HOLE 3 I16))) (BAND I16 (HOLE 4 I16) (BNOT I16 (TSTLES I16 (HOLE 2 I16) (HOLE 3 I16))))))");
    templateList[27]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (BOR I32 (BAND I32 (HOLE 1 I32) (TSTLES I32 (HOLE 2 I32) (HOLE 3 I32))) (BAND I32 (HOLE 4 I32) (BNOT I32 (TSTLES I32 (HOLE 2 I32) (HOLE 3 I32))))))");
//For PADD
    templateList[28]=
      mkBone("((16 8) 1 t)","(SET I8 (HOLE 0 I8) (ADD I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[29]=
      mkBone("((8 4) 1 t)","(SET I16 (HOLE 0 I16) (ADD I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[30]=
      mkBone("((4) 1 t)","(SET I32 (HOLE 0 I32) (ADD I32 (HOLE 1 I32) (HOLE 2 I32)))");
// For PMUL
    templateList[31]=
      mkBone("((16 8) 1 t)","(SET I8 (HOLE 0 I8) (MUL I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[32]=
      mkBone("((8 4) 1 t)","(SET I16 (HOLE 0 I16) (MUL I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[33]=
      mkBone("((4) 1 t)","(SET I32 (HOLE 0 I32) (MUL I32 (HOLE 1 I32) (HOLE 2 I32)))");
// For PSUB
    templateList[34]=
      mkBone("((16 8) 1 nil)","(SET I8 (HOLE 0 I8) (SUB I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[35]=
      mkBone("((8 4) 1 nil)","(SET I16 (HOLE 0 I16) (SUB I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[36]=
      mkBone("((4) 1 nil)","(SET I32 (HOLE 0 I32) (SUB I32 (HOLE 1 I32) (HOLE 2 I32)))");
// For PANDN
    templateList[37]=
      mkBone("((16 8) 1 nil nil nil)","(SET I8 (HOLE 0 I8) (BAND I8 (BNOT I8 (HOLE 1 I8)) (HOLE 2 I8)))");
    templateList[38]=
      mkBone("((8 4) 1 nil nil nil)","(SET I16 (HOLE 0 I16) (BAND I16 (BNOT I16 (HOLE 1 I16)) (HOLE 2 I16)))");
    templateList[39]=
      mkBone("((4) 1 nil nil nil)","(SET I32 (HOLE 0 I32) (BAND I32 (BNOT I32 (HOLE 1 I32)) (HOLE 2 I32)))");
// For PAND
    templateList[40]=
      mkBone("((16 8) 1 t nil nil)","(SET I8 (HOLE 0 I8) (BAND I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[41]=
      mkBone("((8 4) 1 t nil nil)","(SET I16 (HOLE 0 I16) (BAND I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[42]=
      mkBone("((4) 1 t nil nil)","(SET I32 (HOLE 0 I32) (BAND I32 (HOLE 1 I32) (HOLE 2 I32)))");
// For POR
    templateList[43]=
      mkBone("((16 8) 1 t nil nil)","(SET I8 (HOLE 0 I8) (BOR I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[44]=
      mkBone("((8 4) 1 t nil nil)","(SET I16 (HOLE 0 I16) (BOR I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[45]=
      mkBone("((4) 1 t nil nil)","(SET I32 (HOLE 0 I32) (BOR I32 (HOLE 1 I32) (HOLE 2 I32)))");
// For PXOR
    templateList[46]=
      mkBone("((16 8) 1 t nil nil)","(SET I8 (HOLE 0 I8) (BXOR I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[47]=
      mkBone("((8 4) 1 t nil nil)","(SET I16 (HOLE 0 I16) (BXOR I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[48]=
      mkBone("((4) 1 t nil nil)","(SET I32 (HOLE 0 I32) (BXOR I32 (HOLE 1 I32) (HOLE 2 I32)))");
// For NOT
    templateList[49]=
      mkBone("((16 8) 1 nil)","(SET I8 (HOLE 0 I8) (BNOT I8 (HOLE 1 I8)))");
    templateList[50]=
      mkBone("((8 4) 1 nil)","(SET I16 (HOLE 0 I16) (BNOT I16 (HOLE 1 I16)))");
    templateList[51]=
      mkBone("((4) 1 nil)","(SET I32 (HOLE 0 I32) (BNOT I32 (HOLE 1 I32)))");
// For NEG
    templateList[52]=
      mkBone("((16 8) 1 nil)","(SET I8 (HOLE 0 I8) (NEG I8 (HOLE 1 I8)))");
    templateList[53]=
      mkBone("((8 4) 1 nil)","(SET I16 (HOLE 0 I16) (NEG I16 (HOLE 1 I16)))");
    templateList[54]=
      mkBone("((4) 1 nil)","(SET I32 (HOLE 0 I32) (NEG I32 (HOLE 1 I32)))");
//For Shift
    templateList[55]=
      mkBone("((16 8) 1 nil nil nil nil (2))","(SET I8 (HOLE 0 I8) (RSHS I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[56]=
      mkBone("((8 4) 1 nil nil nil nil (2))","(SET I16 (HOLE 0 I16) (RSHS I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[57]=
      mkBone("((4) 1 nil nil nil nil (2))","(SET I32 (HOLE 0 I32) (RSHS I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[58]=
      mkBone("((16 8) 1 nil nil nil nil (2))","(SET I8 (HOLE 0 I8) (RSHU I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[59]=
      mkBone("((8 4) 1 nil nil nil nil (2))","(SET I16 (HOLE 0 I16) (RSHU I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[60]=
      mkBone("((4) 1 nil nil nil nil (2))","(SET I32 (HOLE 0 I32) (RSHU I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[61]=
      mkBone("((16 8) 1 nil nil nil nil (2))","(SET I8 (HOLE 0 I8) (LSHS I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[62]=
      mkBone("((8 4) 1 nil nil nil nil (2))","(SET I16 (HOLE 0 I16) (LSHS I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[63]=
      mkBone("((4) 1 nil nil nil nil (2))","(SET I32 (HOLE 0 I32) (LSHS I32 (HOLE 1 I32) (HOLE 2 I32)))");
//For Store
    templateList[64]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17)))","(PARALLEL (SET I8 (HOLE 2 I8) (MEM I8 (HOLE 1 I32))) (SET I8 (HOLE 3 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 1)))) (SET I8 (HOLE 4 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 2)))) (SET I8 (HOLE 5 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 3)))) (SET I8 (HOLE 6 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))) (SET I8 (HOLE 7 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 5)))) (SET I8 (HOLE 8 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 6)))) (SET I8 (HOLE 9 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 7)))) (SET I8 (HOLE 10 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 8)))) (SET I8 (HOLE 11 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 9)))) (SET I8 (HOLE 12 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 10)))) (SET I8 (HOLE 13 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 11)))) (SET I8 (HOLE 14 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 12)))) (SET I8 (HOLE 15 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 13)))) (SET I8 (HOLE 16 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 14)))) (SET I8 (HOLE 17 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 15)))))");
    templateList[65]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3 4 5 6 7 8 9)))","(PARALLEL (SET I8 (HOLE 2 I8) (MEM I8 (HOLE 1 I32))) (SET I8 (HOLE 3 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 1)))) (SET I8 (HOLE 4 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 2)))) (SET I8 (HOLE 5 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 3)))) (SET I8 (HOLE 6 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))) (SET I8 (HOLE 7 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 5)))) (SET I8 (HOLE 8 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 6)))) (SET I8 (HOLE 9 I8) (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 7)))))");
    templateList[66]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3 4 5 6 7 8 9)))","(PARALLEL (SET I16 (HOLE 2 I16) (MEM I16 (HOLE 1 I32))) (SET I16 (HOLE 3 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 2)))) (SET I16 (HOLE 4 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))) (SET I16 (HOLE 5 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 6)))) (SET I16 (HOLE 6 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 8)))) (SET I16 (HOLE 7 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 10)))) (SET I16 (HOLE 8 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 12)))) (SET I16 (HOLE 9 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 14)))))");
    templateList[67]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3 4 5)))","(PARALLEL (SET I16 (HOLE 2 I16) (MEM I16 (HOLE 1 I32))) (SET I16 (HOLE 3 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 2)))) (SET I16 (HOLE 4 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))) (SET I16 (HOLE 5 I16) (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 6)))))");
    templateList[68]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3 4 5)))","(PARALLEL (SET I32 (HOLE 2 I32) (MEM I32 (HOLE 1 I32))) (SET I32 (HOLE 3 I32) (MEM I32 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))) (SET I32 (HOLE 4 I32) (MEM I32 (ADD I32 (HOLE 1 I32) (INTCONST I32 8)))) (SET I32 (HOLE 5 I32) (MEM I32 (ADD I32 (HOLE 1 I32) (INTCONST I32 12)))))");
//For Load
    templateList[69]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17)))","(PARALLEL (SET I8 (MEM I8 (HOLE 1 I32)) (HOLE 2 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 1))) (HOLE 3 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 2))) (HOLE 4 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 3))) (HOLE 5 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 6 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 5))) (HOLE 7 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 6))) (HOLE 8 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 7))) (HOLE 9 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 8))) (HOLE 10 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 9))) (HOLE 11 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 10))) (HOLE 12 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 11))) (HOLE 13 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 12))) (HOLE 14 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 13))) (HOLE 15 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 14))) (HOLE 16 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 15))) (HOLE 17 I8)))");
    templateList[70]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3 4 5 6 7 8 9)))","(PARALLEL (SET I8 (MEM I8 (HOLE 1 I32)) (HOLE 2 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 1))) (HOLE 3 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 2))) (HOLE 4 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 3))) (HOLE 5 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 6 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 5))) (HOLE 7 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 6))) (HOLE 8 I8)) (SET I8 (MEM I8 (ADD I32 (HOLE 1 I32) (INTCONST I32 7))) (HOLE 9 I8)))");
    templateList[71]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3 4 5 6 7 8 9)))","(PARALLEL (SET I16 (MEM I16 (HOLE 1 I32)) (HOLE 2 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 2))) (HOLE 3 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 4 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 6))) (HOLE 5 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 8))) (HOLE 6 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 10))) (HOLE 7 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 12))) (HOLE 8 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 14))) (HOLE 9 I16)))");
    templateList[72]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3 4 5)))","(PARALLEL (SET I16 (MEM I16 (HOLE 1 I32)) (HOLE 2 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 2))) (HOLE 3 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 4 I16)) (SET I16 (MEM I16 (ADD I32 (HOLE 1 I32) (INTCONST I32 6))) (HOLE 5 I16)))");
    templateList[73]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3 4 5)))","(PARALLEL (SET I32 (MEM I32 (HOLE 1 I32)) (HOLE 2 I32)) (SET I32 (MEM I32 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 3 I32)) (SET I32 (MEM I32 (ADD I32 (HOLE 1 I32) (INTCONST I32 8))) (HOLE 4 I32)) (SET I32 (MEM I32 (ADD I32 (HOLE 1 I32) (INTCONST I32 12))) (HOLE 5 I32)))");

//For FloatingPoint
//For Store
    templateList[74]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3 4 5)))","(PARALLEL (SET F32 (HOLE 2 F32) (MEM F32 (HOLE 1 I32))) (SET F32 (HOLE 3 F32) (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))) (SET F32 (HOLE 4 F32) (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 8)))) (SET F32 (HOLE 5 F32) (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 12)))))");
    templateList[75]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3)))","(PARALLEL (SET F32 (HOLE 2 F32) (MEM F32 (HOLE 1 I32))) (SET F32 (HOLE 3 F32) (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 4)))))");
    templateList[76]=
      mkBone("((1) 2 nil nil nil nil nil ((2 3)))","(PARALLEL (SET F64 (HOLE 2 F64) (MEM F64 (HOLE 1 I32))) (SET F64 (HOLE 3 F64) (MEM F64 (ADD I32 (HOLE 1 I32) (INTCONST I32 8)))))");
//For Load
    templateList[77]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3 4 5)))","(PARALLEL (SET F32 (MEM F32 (HOLE 1 I32)) (HOLE 2 F32)) (SET F32 (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 3 F32)) (SET F32 (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 8))) (HOLE 4 F32)) (SET F32 (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 12))) (HOLE 5 F32)))");
    templateList[78]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3)))","(PARALLEL (SET F32 (MEM F32 (HOLE 1 I32)) (HOLE 2 F32)) (SET F32 (MEM F32 (ADD I32 (HOLE 1 I32) (INTCONST I32 4))) (HOLE 3 F32)))");
    templateList[79]=
      mkBone("((1) 1 nil nil nil nil nil ((2 3)))","(PARALLEL (SET F64 (MEM F32 (HOLE 1 I32)) (HOLE 2 F64)) (SET F64 (MEM F64 (ADD I32 (HOLE 1 I32) (INTCONST I32 8))) (HOLE 3 F64)))");
//For Add Sub Mul Div
    templateList[80]=
      mkBone("((4) 1 t)","(SET F32 (HOLE 0 F32) (ADD F32 (HOLE 1 F32) (HOLE 2 F32)))");
    templateList[81]=
      mkBone("((2) 1 t)","(SET F64 (HOLE 0 F64) (ADD F64 (HOLE 1 F64) (HOLE 2 F64)))");
    templateList[82]=
      mkBone("((4) 1 nil)","(SET F32 (HOLE 0 F32) (SUB F32 (HOLE 1 F32) (HOLE 2 F32)))");
    templateList[83]=
      mkBone("((2) 1 nil)","(SET F64 (HOLE 0 F64) (SUB F64 (HOLE 1 F64) (HOLE 2 F64)))");
    templateList[84]=
      mkBone("((4) 1 t)","(SET F32 (HOLE 0 F32) (MUL F32 (HOLE 1 F32) (HOLE 2 F32)))");
    templateList[85]=
      mkBone("((2) 1 t)","(SET F64 (HOLE 0 F64) (MUL F64 (HOLE 1 F64) (HOLE 2 F64)))");
    templateList[86]=
      mkBone("((4) 1 nil)","(SET F32 (HOLE 0 F32) (DIVS F32 (HOLE 1 F32) (HOLE 2 F32)))");
    templateList[87]=
      mkBone("((2) 1 nil)","(SET F64 (HOLE 0 F64) (DIVS F64 (HOLE 1 F64) (HOLE 2 F64)))");
//For Convert
    templateList[88]=
      mkBone("((4) 0 nil)","(SET F32 (HOLE 0 F32) (CONVSF F32 (HOLE 1 I32)))");
    templateList[89]=
      mkBone("((2) 0 nil)","(SET F64 (HOLE 0 F64) (CONVSF F64 (HOLE 1 I64)))");
    templateList[90]=
      mkBone("((4) 0 nil)","(SET I32 (HOLE 0 I32) (CONVFI I32 (HOLE 1 F32)))");
    templateList[91]=
      mkBone("((2) 0 nil)","(SET I64 (HOLE 0 I64) (CONVFI I64 (HOLE 1 F64)))");
    templateList[92]=
      mkBone("((2) 0 nil)","(SET F64 (HOLE 0 F64) (CONVFX F64 (HOLE 1 F32)))");
    templateList[93]=
      mkBone("((2) 0 nil)","(SET F32 (HOLE 0 F32) (CONVFT F32 (HOLE 1 I64)))");

//For Convert
    templateList[94]=
//      mkBone("((8 4) 0 nil)","(SET I8 (HOLE 0 I8) (CONVIT I8 (HOLE 1 I16)))");
      mkBone("((8) 0 nil)","(SET I8 (HOLE 0 I8) (CONVIT I8 (HOLE 1 I16)))");
    templateList[95]=
// Illegal pattern.
//      mkBone("((4) 0 nil)","(SET I8 (HOLE 0 I8) (CONVIT I8 (HOLE 1 I32)))");
      mkBone("((4) 0 nil)","(SET I8 (INTCONST I8 0) (CONVIT I8 (HOLE 1 I32)))");
    templateList[96]=
      mkBone("((4) 0 nil)","(SET I6 (HOLE 0 I6) (CONVIT I16 (HOLE 1 I32)))");
//
    templateList[97]=
      mkBone("((16 8) 2 nil 2)","(SET I8 (HOLE 0 I8) (TSTLTS I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[98]=
      mkBone("((8 4) 2 nil 2)","(SET I16 (HOLE 0 I16) (TSTLTS I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[99]=
      mkBone("((4 2) 2 nil 2)","(SET I32 (HOLE 0 I32) (TSTLTS I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[100]=
      mkBone("((16 8) 1 nil)","(SET I8 (HOLE 0 I8) (TSTGTS I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[101]=
      mkBone("((8 4) 1 nil)","(SET I16 (HOLE 0 I16) (TSTGTS I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[102]=
      mkBone("((4) 1 nil)","(SET I32 (HOLE 0 I32) (TSTGTS I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[103]=
      mkBone("((16 8) 1 nil 3)","(SET I8 (HOLE 0 I8) (TSTLES I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[104]=
      mkBone("((8 4) 1 nil 3)","(SET I16 (HOLE 0 I16) (TSTLES I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[105]=
      mkBone("((4) 1 nil 3)","(SET I32 (HOLE 0 I32) (TSTLES I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[106]=
      mkBone("((16 8) 2 nil 4)","(SET I8 (HOLE 0 I8) (TSTGES I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[107]=
      mkBone("((8 4) 2 nil 4)","(SET I16 (HOLE 0 I16) (TSTGES I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[108]=
      mkBone("((4) 2 nil 4)","(SET I32 (HOLE 0 I32) (TSTGES I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[109]=
      mkBone("((16 8) 2 nil)","(SET I8 (HOLE 0 I8) (TSTEQ I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[110]=
      mkBone("((8 4) 2 nil)","(SET I16 (HOLE 0 I16) (TSTEQ I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[111]=
      mkBone("((4) 2 nil)","(SET I32 (HOLE 0 I32) (TSTEQ I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[112]=
      mkBone("((16 8) 2 nil)","(SET I8 (HOLE 0 I8) (TSTNE I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[113]=
      mkBone("((8 4) 2 nil)","(SET I16 (HOLE 0 I16) (TSTNE I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[114]=
      mkBone("((4) 2 nil)","(SET I32 (HOLE 0 I32) (TSTNE I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[115]=
      mkBone("((16 8) 2 nil)","(SET I8 (HOLE 0 I8) (TSTLTU I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[116]=
      mkBone("((8 4) 2 nil)","(SET I16 (HOLE 0 I16) (TSTLTU I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[117]=
      mkBone("((4) 2 nil)","(SET I32 (HOLE 0 I32) (TSTLTU I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[118]=
      mkBone("((16 8) 2 nil)","(SET I8 (HOLE 0 I8) (TSTGTU I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[119]=
      mkBone("((8 4) 2 nil)","(SET I16 (HOLE 0 I16) (TSTGTU I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[120]=
      mkBone("((4) 2 nil)","(SET I32 (HOLE 0 I32) (TSTGTU I32 (HOLE 1 I32) (HOLE 2 I32)))");

    templateList[121]=
      mkBone("((16 8) 2 nil)","(SET I8 (HOLE 0 I8) (TSTGEU I8 (HOLE 1 I8) (HOLE 2 I8)))");
    templateList[122]=
      mkBone("((8 4) 2 nil)","(SET I16 (HOLE 0 I16) (TSTGEU I16 (HOLE 1 I16) (HOLE 2 I16)))");
    templateList[123]=
      mkBone("((4) 2 nil)","(SET I32 (HOLE 0 I32) (TSTGEU I32 (HOLE 1 I32) (HOLE 2 I32)))");

    return templateList;
  }
  /**
   * Rewrite patterns.
   */
  public ImList[] rewriteList=new ImList[7];
  /**
   * Initializes rewriteList.
   */
  public ImList[] initRewriteList() {
    rewriteList[0]=mkRw("((SET I64 (HOLE 0 I64) (BAND I64 (HOLE 1 I64) (HOLE 2 I64))))");
    rewriteList[1]=mkRw("((SET I64 (HOLE 0 I64) (BXOR I64 (HOLE 1 I64) (HOLE 2 I64))))");
    rewriteList[2]=mkRw("((SET I16 (HOLE 2 I16) (TSTGTS I16 (HOLE 2 I16) (HOLE 1 I16))))"); // TSTLTS
    rewriteList[3]=mkRw("((SET I16 (HOLE 1 I16) (TSTGTS I16 (HOLE 1 I16) (HOLE 2 I16))) (SET I16 (HOLE 1 I16) (BNOT I16 (HOLE 1 I16))))"); // TSTLES
    rewriteList[4]=mkRw("((SET I16 (HOLE 2 I16) (TSTGTS I16 (HOLE 2 I16) (HOLE 1 I16))) (SET I16 (HOLE 2 I16) (BNOT I16 (HOLE 2 I16))))"); // TSTGES
    rewriteList[5]=mkRw("((SET I16 (HOLE 1 I16) (BAND I16 (BNOT I16 (HOLE 1 I16)) (INTCONST I16 0))))"); // BNOT
    rewriteList[6]=mkRw("((SET I64 (HOLE 0 I64) (BAND I64 (BNOT I64 (HOLE 1 I64)) (HOLE 2 I64))))");

    return rewriteList;
  }

  public ImList[] initAuxBoneList() {
    auxtemplateList[0]=
      mkBone("((2) 0 nil nil nil nil (1 3) ((0 2)))","(PARALLEL (SET I32 (HOLE 0 I32) (HOLE 1 I32)) (SET I32 (HOLE 2 I32) (HOLE 3 I32)))");
    auxtemplateList[1]=
      mkBone("((1) 0 nil nil nil nil (1 3 5 7 9 11 13 15) ((0 2 4 6 8 10 12 14)))", "(PARALLEL (SET I8 (HOLE 0 I8) (MEM I8 (HOLE 1 I32))) (SET I8 (HOLE 2 I8) (MEM I8 (HOLE 3 I32))) (SET I8 (HOLE 4 I8) (MEM I8 (HOLE 5 I32))) (SET I8 (HOLE 6 I8) (MEM I8 (HOLE 7 I32))) (SET I8 (HOLE 8 I8) (MEM I8 (HOLE 9 I32))) (SET I8 (HOLE 10 I8) (MEM I8 (HOLE 11 I32))) (SET I8 (HOLE 12 I8) (MEM I8 (HOLE 13 I32))) (SET I8 (HOLE 14 I8) (MEM I8 (HOLE 15 I32))))");

    auxtemplateList[2]=
      mkBone("((1) 0 nil nil nil nil (1 3 5 7 9 11 13 15 17 19 21 23 25 27 29 31) ((0 2 4 6 8 10 12 14 16 18 20 22 24 26 28 30)))", "(PARALLEL (SET I8 (HOLE 0 I8) (MEM I8 (HOLE 1 I32))) (SET I8 (HOLE 2 I8) (MEM I8 (HOLE 3 I32))) (SET I8 (HOLE 4 I8) (MEM I8 (HOLE 5 I32))) (SET I8 (HOLE 6 I8) (MEM I8 (HOLE 7 I32))) (SET I8 (HOLE 8 I8) (MEM I8 (HOLE 9 I32))) (SET I8 (HOLE 10 I8) (MEM I8 (HOLE 11 I32))) (SET I8 (HOLE 12 I8) (MEM I8 (HOLE 13 I32))) (SET I8 (HOLE 14 I8) (MEM I8 (HOLE 15 I32))) (SET I8 (HOLE 16 I8) (MEM I8 (HOLE 17 I32))) (SET I8 (HOLE 18 I8) (MEM I8 (HOLE 19 I32))) (SET I8 (HOLE 20 I8) (MEM I8 (HOLE 21 I32))) (SET I8 (HOLE 22 I8) (MEM I8 (HOLE 23 I32))) (SET I8 (HOLE 24 I8) (MEM I8 (HOLE 25 I32))) (SET I8 (HOLE 26 I8) (MEM I8 (HOLE 27 I32))) (SET I8 (HOLE 28 I8) (MEM I8 (HOLE 29 I32))) (SET I8 (HOLE 30 I8) (MEM I8 (HOLE 31 I32))))");

    auxtemplateList[3]=
      mkBone("((1) 0 nil nil nil nil (1 3 5 7) ((0 2 4 6)))", "(PARALLEL (SET I16 (HOLE 0 I16) (MEM I16 (HOLE 1 I32))) (SET I16 (HOLE 2 I16) (MEM I16 (HOLE 3 I32))) (SET I16 (HOLE 4 I16) (MEM I16 (HOLE 5 I32))) (SET I16 (HOLE 6 I16) (MEM I16 (HOLE 7 I32))))");

    auxtemplateList[4]=
      mkBone("((1) 0 nil nil nil nil (1 3 5 7 9 11 13 15) ((0 2 4 6 8 10 12 14)))", "(PARALLEL (SET I16 (HOLE 0 I16) (MEM I16 (HOLE 1 I32))) (SET I16 (HOLE 2 I16) (MEM I16 (HOLE 3 I32))) (SET I16 (HOLE 4 I16) (MEM I16 (HOLE 5 I32))) (SET I16 (HOLE 6 I16) (MEM I16 (HOLE 7 I32))) (SET I16 (HOLE 8 I16) (MEM I16 (HOLE 9 I32))) (SET I16 (HOLE 10 I16) (MEM I16 (HOLE 11 I32))) (SET I16 (HOLE 12 I16) (MEM I16 (HOLE 13 I32))) (SET I16 (HOLE 14 I16) (MEM I16 (HOLE 15 I32))))");

    auxtemplateList[5]=
      mkBone("((1) 0 nil nil nil nil (1 3) ((0 2)))", "(PARALLEL (SET I32 (HOLE 0 I32) (MEM I32 (HOLE 1 I32))) (SET I32 (HOLE 2 I32) (MEM I32 (HOLE 3 I32))))");

    auxtemplateList[6]=
      mkBone("((1) 0 nil nil nil nil (1 3 5 7) ((0 2 4 6)))", "(PARALLEL (SET I32 (HOLE 0 I32) (MEM I32 (HOLE 1 I32))) (SET I32 (HOLE 2 I32) (MEM I32 (HOLE 3 I32))) (SET I32 (HOLE 4 I32) (MEM I32 (HOLE 5 I32))) (SET I32 (HOLE 6 I32) (MEM I32 (HOLE 7 I32))))");

    auxtemplateList[7]=
      mkBone("((1) 0 nil nil nil nil (1 3 5 7) ((0 2 4 6)))", "(PARALLEL (SET F32 (HOLE 0 F32) (MEM F32 (HOLE 1 I32))) (SET F32 (HOLE 2 F32) (MEM F32 (HOLE 3 I32))) (SET F32 (HOLE 4 F32) (MEM F32 (HOLE 5 I32))) (SET F32 (HOLE 6 F32) (MEM F32 (HOLE 7 I32))))");

    auxtemplateList[8]=
      mkBone("((1) 0 nil nil nil nil (1 3) ((0 2)))", "(PARALLEL (SET F64 (HOLE 0 F64) (MEM F64 (HOLE 1 I32))) (SET F64 (HOLE 2 F64) (MEM F64 (HOLE 3 I32))))");

    auxtemplateList[9]=
      mkBone("((1) 0 nil nil nil nil (0 2 4 6 8 10 12 14) ((1 3 5 7 9 11 13 15)))", "(PARALLEL (SET I8 (MEM I8 (HOLE 0 I32)) (HOLE 1 I8)) (SET I8 (MEM I8 (HOLE 2 I32)) (HOLE 3 I8)) (SET I8 (MEM I8 (HOLE 4 I32)) (HOLE 5 I8)) (SET I8 (MEM I8 (HOLE 6 I32)) (HOLE 7 I8)) (SET I8 (MEM I8 (HOLE 8 I32)) (HOLE 9 I8)) (SET I8 (MEM I8 (HOLE 10 I32)) (HOLE 11 I8)) (SET I8 (MEM I8 (HOLE 12 I32)) (HOLE 13 I8)) (SET I8 (MEM I8 (HOLE 14 I32)) (HOLE 15 I8)))");

    auxtemplateList[10]=
      mkBone("((1) 0 nil nil nil nil (0 2 4 6 8 10 12 14 16 18 20 22 24 26 28 30) ((1 3 5 7 9 11 13 15 17 19 21 23 25 27 29 31)))", "(PARALLEL (SET I8 (MEM I8 (HOLE 0 I32)) (HOLE 1 I8)) (SET I8 (MEM I8 (HOLE 2 I32)) (HOLE 3 I8)) (SET I8 (MEM I8 (HOLE 4 I32)) (HOLE 5 I8)) (SET I8 (MEM I8 (HOLE 6 I32)) (HOLE 7 I8)) (SET I8 (MEM I8 (HOLE 8 I32)) (HOLE 9 I8)) (SET I8 (MEM I8 (HOLE 10 I32)) (HOLE 11 I8)) (SET I8 (MEM I8 (HOLE 12 I32)) (HOLE 13 I8)) (SET I8 (MEM I8 (HOLE 14 I32)) (HOLE 15 I8)) (SET I8 (MEM I8 (HOLE 16 I32)) (HOLE 17 I8)) (SET I8 (MEM I8 (HOLE 18 I32)) (HOLE 19 I8)) (SET I8 (MEM I8 (HOLE 20 I32)) (HOLE 21 I8)) (SET I8 (MEM I8 (HOLE 22 I32)) (HOLE 23 I8)) (SET I8 (MEM I8 (HOLE 24 I32)) (HOLE 25 I8)) (SET I8 (MEM I8 (HOLE 26 I32)) (HOLE 27 I8)) (SET I8 (MEM I8 (HOLE 28 I32)) (HOLE 29 I8)) (SET I8 (MEM I8 (HOLE 30 I32)) (HOLE 31 I8)))");

    auxtemplateList[11]=
      mkBone("((1) 0 nil nil nil nil (0 2 4 6) ((1 3 5 7)))", "(PARALLEL (SET I16 (MEM I16 (HOLE 0 I32)) (HOLE 1 I16)) (SET I16 (MEM I16 (HOLE 2 I32)) (HOLE 3 I16)) (SET I16 (MEM I16 (HOLE 4 I32)) (HOLE 5 I16)) (SET I16 (MEM I16 (HOLE 6 I32)) (HOLE 7 I16)))");

    auxtemplateList[12]=
      mkBone("((1) 0 nil nil nil nil (0 2 4 6 8 10 12 14) ((1 3 5 7 9 11 13 15)))", "(PARALLEL (SET I16 (MEM I16 (HOLE 0 I32)) (HOLE 1 I16)) (SET I16 (MEM I16 (HOLE 2 I32)) (HOLE 3 I16)) (SET I16 (MEM I16 (HOLE 4 I32)) (HOLE 5 I16)) (SET I16 (MEM I16 (HOLE 6 I32)) (HOLE 7 I16)) (SET I16 (MEM I16 (HOLE 8 I32)) (HOLE 9 I16)) (SET I16 (MEM I16 (HOLE 10 I32)) (HOLE 11 I16)) (SET I16 (MEM I16 (HOLE 12 I32)) (HOLE 13 I16)) (SET I16 (MEM I16 (HOLE 14 I32)) (HOLE 15 I16)))");


    auxtemplateList[13]=
      mkBone("((1) 0 nil nil nil nil (0 2) ((1 3)))", "(PARALLEL (SET I32 (MEM I32 (HOLE 0 I32)) (HOLE 1 I32)) (SET I32 (MEM I32 (HOLE 2 I32)) (HOLE 3 I32)))");

    auxtemplateList[14]=
      mkBone("((1) 0 nil nil nil nil (0 2 4 6) ((1 3 5 7)))", "(PARALLEL (SET I32 (MEM I32 (HOLE 0 I32)) (HOLE 1 I32)) (SET I32 (MEM I32 (HOLE 2 I32)) (HOLE 3 I32)) (SET I32 (MEM I32 (HOLE 4 I32)) (HOLE 5 I32)) (SET I32 (MEM I32 (HOLE 6 I32)) (HOLE 7 I32)))");

    auxtemplateList[15]=
      mkBone("((1) 0 nil nil nil nil (0 2 4 6) ((1 3 5 7)))", "(PARALLEL (SET F32 (MEM F32 (HOLE 0 I32)) (HOLE 1 F32)) (SET F32 (MEM F32 (HOLE 2 I32)) (HOLE 3 F32)) (SET F32 (MEM F32 (HOLE 4 I32)) (HOLE 5 F32)) (SET F32 (MEM F32 (HOLE 6 I32)) (HOLE 7 F32)))");

    auxtemplateList[16]=
      mkBone("((1) 0 nil nil nil nil (0 2) ((1 3)))", "(PARALLEL (SET F64 (MEM F64 (HOLE 0 I32)) (HOLE 1 F64)) (SET F64 (MEM F64 (HOLE 2 I32)) (HOLE 3 F64)))");

    return auxtemplateList;
  }

// First parameter i must correspond to the index of auxtemplateList.
  public boolean chkAuxCond(int i, LirNode inst) {
    switch(i) {
      case 0:
        {
          if(inst.opCode!=Op.PARALLEL) return false;
          for(int j=0; j<inst.nKids(); j++) {
            LirNode subinst=inst.kid(j);
            if(subinst.opCode!=Op.SET) return false;
            if(subinst.kid(1).opCode!=Op.INTCONST) return false;
          }
          return true;
        }
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
        {
          if(inst.opCode!=Op.PARALLEL) return false;
          LirNode b=boneBody(auxtemplateList[i]);
          if(b.opCode!=Op.PARALLEL) return false;
          int k=b.nKids();
          if(inst.nKids()!=k) return false;
          for(int j=0; j<inst.nKids(); j++) {
            LirNode inst0=inst.kid(j);
            if(inst0.opCode!=Op.SET) return false;
            if(inst0.kid(1).opCode!=Op.MEM) return false;
          }
          LirNode exp=inst.kid(0).kid(1);
          Symbol bsym=LirUtil.basesymbol(exp);
          long dispval=LirUtil.dispval(exp);
          long incval=LirUtil.calcIncval(exp.type);
          for(int j=1; j<inst.nKids(); j++) {
            LirNode e=inst.kid(j).kid(1);
            if(LirUtil.basesymbol(e)!=bsym) return false;
            dispval=dispval+incval;
            if(LirUtil.dispval(e)!=dispval) return false;
          }
          return true;
        }
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
        {
          if(inst.opCode!=Op.PARALLEL) return false;
          LirNode b=boneBody(auxtemplateList[i]);
          if(b.opCode!=Op.PARALLEL) return false;
          int k=b.nKids();
          if(inst.nKids()!=k) return false;
          for(int j=0; j<inst.nKids(); j++) {
            LirNode inst0=inst.kid(j);
            if(inst0.opCode!=Op.SET) return false;
            if(inst0.kid(0).opCode!=Op.MEM) return false;
          }
          LirNode exp=inst.kid(0).kid(0);
          Symbol bsym=LirUtil.basesymbol(exp);
          long dispval=LirUtil.dispval(exp);
          long incval=LirUtil.calcIncval(exp.type);
          for(int j=1; j<inst.nKids(); j++) {
            LirNode e=inst.kid(j).kid(0);
            if(LirUtil.basesymbol(e)!=bsym) return false;
            dispval=dispval+incval;
            if(LirUtil.dispval(e)!=dispval) return false;
          }
          return true;
        }
      default:
        return false;
    }
  }
}
