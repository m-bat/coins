/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.lang.StringBuffer;

import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.driver.CoinsOptions;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.HIR_Impl;
import coins.ir.hir.HirIterator;
import coins.ir.hir.HirList;
import coins.ir.hir.InfStmt;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Program;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.SymNode;
import coins.ir.hir.VarNode;
import coins.sym.Const;  //##91
import coins.sym.Label;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.Subp;
import coins.sym.SubpImpl;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymTable;
import coins.sym.Type;
import coins.sym.Var;
import coins.sym.VectorType;

/**
 */
public class GlobalReform
{
// GlobalReform transforms a subprogram by global pattern matching.
// For that purpose, correspondence of a pair of patterns named
// in-pattern and out-pattern is to be given in the same form
// as a subprogram with parameters as it is mentioned later.
// The in-pattern and the out-pattern are a block containing an
// expression (an instance of ExpStmt) or a sequence of
// statements. If a part of input program matches with
// some in-pattern, then it is transformed to the corresponding
// out-pattern replacing parameters as mentioned later.
// This transformation is invoked when
//    -coins:hirOpt=globalReform
// is specified as a compile option.
//
// Wide variety of transformations can be done by the global pattern matching,
// however, its applicability is limited because the transformation is
// limited only to some program fragments that match with given in-patterns.
//
// I. Introductory explanation
//
// (1) Example 1
//
// Before explaining in detail, let us show a simple example:
/*
   #pragma globalReform patternSym copy
   #pragma globalReform target main
   void copy( char *pa, char *pb, int pn, int pi )
   {
    iPattern:
     {
       for (pi = 0; pi < pn; pi++)
         *pb++ = *pa++;
     }
    oPattern:
     {
       memcpy(pb, pa, pn);
     }
   }
   int main()
   {
     char x[100] = {1, 2, 3, 4, 5, 6, 7, 8}, y[100];
     int j;
     char *px = x;
     char *py = y;
     for (j = 0; j < 100; j++)
      *py++ = *px++;
     printf(" %d %d %d \n", y[0], y[1], y[2]);
     return 0;
   }
*/
// In this example, the in-pattern is
//   for (pi = 0; pi < pn; pi++)
//     *pb++ = *pa++;
// and the out-pattern is
//   memcpy(pb, pa, pn);
// The pragma
//   #pragma globalReform patternSym copy
// indicates that the subprogram showing the correspondence
// of above in-pattern and out-pattern is "copy".
// The pragma
//   #pragma globalReform target main
// indicates that above transformation should be applied
// to the subprogram "main".
// In this example, the statement
//   for (j = 0; j < 100; j++)
//     *py++ = *px++;
// fits in with the in-pattern and it is transformed to
//     memcpy(py, px, 100);
// where, the parameters pa, pb, pn, pi correspond
// to expressions px, py, 100, j respectively and the
// parameters contained in the out-pattern are replaced
// with these expressions, respectively.
//
// (2) Basic notions
//
// The name of a subprogram showing the correspondence of
// an in-pattern and an out-pattern is called a pattern symbol
// and it should be listed up by a pragma before it appears
// in such a way as
//   #pragma globalReform patternSym pattern1 pattern2 ...
// where the keyword "globalReform" indicates that this pragma
// is specified for the global pattern matching, and the keyword
// "patternSym" indicates that names of subprograms showing
// pattern correspondence follows in the same line.
// The name of a subprogram to be transformed by the global pattern
// matching should be listed up by a pragma before it appears
// in such a way as
//   #pragma globalReform target subp1 subp2 ...
// where the keyword "target" indicates that names of
// subprograms to be transformed follows in the same line.
//
// A subprogram showing the correspondence of an in-pattern and
// an out-pattern takes the following form:
//
// void patternSymbol( type1 pParam1, type2 pParam2, ... )
// {
//   iPattern:
//   {
//     statement1
//     statement2
//     ....
//   }
//   oPattern:
//   {
//     statement4
//     statement5
//     ....
//   }
//  }
// where, patternSymbol is an identifier showing the name of
// the pattern correspondence.
// Parameter types (type1, type2, ... ) are usually a type
// of expression (Exp or subclass of Exp) and the corresponding
// parameter (formal parameter pParam1, pParam2, ...) fits in
// with an expression of the type compatible with the parameter type.
// A parameter qualified as "const" can match only with a
// constant of the specified type.
// The block labeled by "iPattern" represents an in-pattern to
// be matched with input program. The block labeled by
// "oPattern represents an out-pattern representing the
// result pattern of the transformation.
// The statement1, statement2, ..., statement4, statement5, ...
// in the in-pattern and the out-pattern may
// contain parameters.
// Some expressions contained in the in-pattern and
// the out-pattern may represent an expression
// called pattern nonterminal as explained later so that
// expressions of various forms can match the in-pattern and
// the corresponding matched expressions may appear in the
// out-pattern.
// A parameter in the in-pattern represents usually an expression
// in an input HIR subtree that fits in with the in-pattern.
// The expression represented by the parameter is the one located
// at the position that corresponds to the location where
// the formal parameter is located in the in-pattern.
// The HIR subtree fitted in with the in-pattern is transformed
// to the corresponding out-pattern replacing parameters
// (formal parameters) contained in the out-pattern with the
// expression (actual parameter) corresponding to the formal
// parameter.
//
// A parameter, say param1, may represent a statement instead
// of expression if it is listed up in a pragma with the
// keyword "stmtParam"
//   #pragma globalReform stmtParam param1
// in the scope of the parameter in such a way as
//
// void patternSymbol( type1 pParam1, type2 pParam2, ... )
// {
// #pragma globalReform stmtParam param1 param2 ...
//   iPattern:
//   {
//     statement1
//     statement2
//     ....
//   }
//   oPattern:
//   {
//     statement1
//     statement2
//     ....
//   }
//  }
// For the parameter representing a statement (statement
// parameter), give void* as its type in the parameter list.
// The statement parameter fits in with a statement that is
// placed at the same position in the input program as the
// position where the statement parameter is located in the
// in-pattern.  As for other matters, the same rules as above
// are applied to the statement parameters.
//
// If an expression is to be given as an in-pattern or an
// out-pattern, then it should be given as an expression
// statement (ExpStmt) in such a form as
//   iPattern
//   {
//     p * 10;
//   }
// If an in-pattern is an expression then corresponding
// out-pattern should also be an expression, e.g.,
//   oPattern:
//   {
//     p * 8 + p * 2;
//   }
//
// In the global transformation, each one of in-patterns may
// be considered as a template to be matched with a part of
// the input program, where a parameter in the in-pattern may
// be considered as a hole in the template. The template is
// considered to be fitted in with a part of the input program
// if both of them have the same form except for the hole where
// any expression/statement may fall in, that is, in the pattern
// matching, HIR subtrees having the same form as some in-pattern
// are searched where each parameter in the in-pattern is treated
// to be a formal parameter to which an actual parameter of
// any complexity may correspond as far as the type of the actual
// parameter is compatible with the type of the formal parameter.
// If the same parameter appears in several places in the
// in-pattern, then the corresponding actual parameter should
// be the same in every positions in the part of input program.
// If not, the in-pattern is treated to be failed in the
// matching.
// The part of input program to be matched with the in-pattern
// may not necessarily be a statement nor an expression but
// may be a sequence of statements embedded within a longer
// sequence of statements (see Example 2).
// If a pattern symbol itself appears in an in-pattern or an
// out-pattern, then it is treated as a subprogram (see Example 3).
//
// As the result of pattern matching, an HIR subtree having
// the same form as the corresponding out-pattern is generated
// replacing every formal parameters included in the out-pattern
// with the corresponding actual parameters.
// If an out-pattern contains a non-parameter variable that is
// (defined in the pattern definition subprogram but) not
// defined in the subprogram in which the out-pattern is to be
// expanded, the variable is replaced by a generated temporal
// variable.
//
// By using nonterminal symbols with parameters, variety of
// transformations can be done as it is explained later.
//
// The pragma having a keyword "noFurtherChange"
//    #pragma globalReform noFurtherChange pattern1 pattern2 ...
// specifies that the result of transformation by pattern1,
// pattern2, ... should not be transformed by further HIR optimization.
// When all global transformations are done, HIR body of the
// pattern symbols are set to empty block so as to suppress
// further optimization and code generation for them.
//
// II. Patterns that do not include nonterminal symbols
//
// (1) Example 2 -- Matching a sequence of statements
//   Consider that a target machine has SIMD (Single-Instruction
// Multiple-Data) instructions and the summation of absolute
// values of 8 bit signed integer numbers can be done quickly
// by using one of the SIMD instructions.
// In the following example, the sequence of statements
//     sum = 0;
//     for (i = 0; i < 256; i = i + 1) {
//       if (buf[i] >= 0)
//         sum = sum + buf[i];
//       else
//         sum = sum - buf[i];
//     }
// can fit in with the in-pattern of the pattern symbol named
// "absAdd" and it is changed to the statement calling
// "absAddChar" function that does the same computation by
// using one of the SIMD instructions.
/*
  #pragma globalReform patternSym absAdd
  #pragma globalReform target main
  #define BSIZE 256
  void absAdd( signed char pd[], int pi, int pm, int psum )
  {
   iPattern:
    {
      psum = 0;
      for (pi = 0; pi < pm; pi = pi + 1 ) {
        if (pd[pi] >= 0)
          psum = psum + pd[pi];
        else
          psum = psum - pd[pi];
      }
    }
   oPattern:
    {
      psum = absAddChar(pd, pm);
    }
  }
  int main()
  {
    signed char buf[BSIZE], v;
    int i, j;
    int sum;
    for (j = 0; j < BSIZE; j++) {
      buf[j] = 128 - j;
    }
    sum = 0;
    for (i = 0; i < BSIZE; i = i + 1) {
      if (buf[i] >= 0)
        sum = sum + buf[i];
      else
        sum = sum - buf[i];
    }
    printf("sum= %d\n", sum);
    return 0;
  }
*/
//
// (2) Example 3 -- SIMD instruction generation
//      with the help of asm inline assembler
//
// There are many coding patterns that are suitable
// for execution by SIMD instructions.
// The global pattern matching can recognize some of
// such patterns and change them to sequences of SIMD
// instructions with the help of the asm inline assembly
// feature of C language.
//
// As an example, let us consider coordinate translations
// in X-, Y-, Z-axis. Rotation and scaling up/down can be
// represented by 3 dimensional translation matrix.
// By using 4 dimensional translation matrix that is
// added moving distances in X-, Y-, Z-coordinates as
// the 4-th values, all coordinate translations
// combining parallel movement, rotation, and scaling
// up/down can be treated uniformly. In this case,
// the coordinate of a point is represented as
// (x, y, z, 1).
// In computer graphics, coordinates are usually represented
// as integer values of limited size, where computed
// results larger than the specified limit are truncated
// to the limit value (saturation operation).
//
// When the translation matrix is represented as
//    short m[4][4];
// and the coordinate of a point is represented as
//    short c[4];
// then the translation result can be computed by
//
//   for (i=0; i< 4; i++) {
//     t[i]=c[3]*m[i][3]+c[2]*m[i][2]
//           +c[1]*m[i][1]+c[0]*m[i][0];
//   }
//   for(j=0; j< 4; j++) c[j]=t[j];
//
// using a temporal variable t and integer variables
// i and j.
//
// The above translation can be done by using MMX
// instructions of the Intel Pentium machine in such
// a sequence
/*
    mov    eax,c     //edi = &c[0]
    mov    ebx,m     //ebx = &m[0][0]
    movq mm0,[eax]   //mm0: c[3]: c[2]: c[1]: c[O]
                     // move quad words to mm0 (64bits)
    movq mm1,[ebx]   //mm1: m[0][3]:m[0][2]:m[0][1]:m[0][0]
    movq mm2,[ebx+8] //mm2: m[1][3]:m[1][2]:m[1][1]:m[1][0]
    movq mm3,[ebx+16]//mm3: m[2][3]:m[2][2]:m[2][1]:m[2][0]
    movq mm4,[ebx+24]//mm4: m[3][3]:m[3][2]:m[3][1]:m[3][O]
    pmaddwd mm1,mm0  //mm1: c[3]*m[0][3]+c[2]*m[0][2]: c[1]*m[0][1]+c[0]*m[0][0]
    pmaddwd mm2,mm0  //mm2: c[3]*m[1][3]+c[2]*m[1][2]: c[1]*m[1][1]+c[0]*m[1][0]
    pmaddwd mm3,mm0  //mm3: c[3]*m[2][3]+c[2]*m[2][2]: c[1]*m[2][1]+c[0]*m[2][0]
    pmaddwd mm4,mm0  //mm4: c[3]*m[3][3]+c[2]*m[3][2]: c[1]*m[3][1]+c[0]*m[3][0]
    packssdw mm1,mm2 //mm1: c[3]*m[1][3]+c[2]*m[1][2]: c[1]*m[1][1]+c[0]*m[1][0]
                     //   : c[3]*m[0][3]+c[2]*m[0][2]: c[1]*m[0][1]+c[0]*m[0][0]
                     // 4 packed words in mm1, mm2 to 4 packed words in mm1
                     // with saturation operation.
    movq [temp1],mm1 // short temp1[4];
    packssdw mm3,mm4 //mm3: c[3]*m[3][3]+c[2]*m[3][2]: c[1]*m[3][1]+c[0]*m[3][0]
                     //   : c[3]*m[2][3]+c[2]*m[2][2]: c[1]*m[2][1]+c[0]*m[2][0]
    movq[temp2],mm3  // short temp2[4];
    emms             // empty MMX state so that FPU reg can be used for floating op.
  )
  c[0]=temp1[0]+temp1[1]; // Move the results from temp1 and temp2.
  c[1]=temp1[2]+temp1[3];
  c[2]=temp2[0]+temp2[1];
  c[3]=temp2[2]+temp2[3];
*/
// In the MMX instruction coding, the saturation
// operation is executed but in the above C language
// coding, the saturation operation is not yet
// considered.
//
// The global reform optimizer catches such a coding
// pattern as described above and changes it to
// the corresponding MMX instruction sequence.
// Let us see the following program:
/*
 #pragma globalReform patternSym linearTrans
 #pragma globalReform target main
 int printf(char*, ...);

 void linearTrans(short pc[4], short pm[4][4], short pt[4], int pi, int pj)
 {
   short temp1[4], temp2[4];
  iPattern:
   {
     for (pi=0;pi< 4;pi++) {
       pt[pi]=pc[3]*pm[pi][3]+pc[2]*pm[pi][2]+pc[1]*pm[pi][1]+pc[0]*pm[pi][0];
     }
     for(pj=0;pj< 4;pj++) pc[pj]=pt[pj];
   }
  oPattern:
   {
   asm (
     "#param %I32,%I32,%I32,%I32\n"
     "#clobber %mm0,%mm1,%mm2,%mm3,%mm4\n"
      "movq (%1),%mm0\n"
      "movq (%2),%mm1\n"
      "movq 8(%2),%mm2\n"
      "movq 16(%2),%mm3\n"
      "movq 24(%2),%mm4\n"
      "pmaddwd %mm0,%mm1\n"
      "pmaddwd %mm0,%mm2\n"
      "pmaddwd %mm0,%mm3\n"
      "pmaddwd %mm0,%mm4\n"
      "packssdw %mm2,%mm1\n"
      "movq %mm1,(%3)\n"
      "packssdw %mm4,%mm3\n"
      "movq %mm3,(%4)\n"
      "emms\n",
     pc, pm, temp1, temp2
   );
   pc[0]=temp1[0]+temp1[1];
   pc[1]=temp1[2]+temp1[3];
   pc[2]=temp2[0]+temp2[1];
   pc[3]=temp2[2]+temp2[3];
  }
 } // linearTrans

 int main()
 {
   int i;
   short tt[4][4] = {{1, 0, 0, 0}, {0, 1, 0, 1}, {0, 0, 1, 0}, {0, 0, 0, 1}};
   short xyz[4]    = {10, 12, 13, 3};
   short tmp[4] = {0};
   printf("before   %d %d %d %d \n", xyz[0], xyz[1], xyz[2], xyz[3]);
   for (i=0;i< 4;i++) {
     tmp[i]=xyz[3]*tt[i][3]+xyz[2]*tt[i][2]+xyz[1]*tt[i][1]+xyz[0]*tt[i][0];
   }
   for(i=0;i< 4;i++) xyz[i]=tmp[i];
   printf("linTrans %d %d %d %d \n", xyz[0], xyz[1], xyz[2], xyz[3]);
   return 0;
 }
*/
// The function definition linearTrans defines the
// C coding pattern as the block labeled by iPattern
// and the corresponding MMX coding sequence as the
// block labeled by oPattern.
// The code fragment
//   for (i=0;i< 4;i++) {
//     tmp[i]=xyz[3]*tt[i][3]+xyz[2]*tt[i][2]+xyz[1]*tt[i][1]+xyz[0]*tt[i][0];
//   }
//   for(i=0;i< 4;i++) xyz[i]=tmp[i];
// in the main program matches with this pattern.
// When the coordinate translation of this program is
// executed 20000000 times on
//   Intel Pentium 4 machine (2.8GHz)
// with and without compile option
//   hirOpt=globalReform
// the measured results of execution time was
//   with globalReform  without globalReform
//      real  8.712s    17.389s
//      user  8.624s    17.331s
//      sys   0.015s     0.031s
// This shows the effectiveness of MMX code generation
// by using globalReform optimization.
//
//
// (3) Example 4 -- Transform a recursion to a loop.
//
// As it is mentioned, the description of an in-pattern and
// an out-pattern is represented in the form of a subprogram,
// and its name is called a pattern symbol. If the pattern
// symbol is used in the in-pattern or out-pattern of the
// pattern description, then the pattern symbol is treated
// in the same way as a parameter that matches with a
// subprogram. In this way, a transformation for recursive
// subprogram can be described.
//
// In the follwing example, the pattern symbol recmult appears
// in the in-pattern and the out-pattern. It is treated as a
// parameter that fits in with the function fact. Thus, the
// body of the function fact is transformed to a loop.
/*
  #pragma globalReform patternSym recmult
  #pragma globalReform target fact
  int recmult( int px )
  {
   iPattern:
    {
      if (px <= 1)
        return 1;
      else
        return px * recmult(px - 1);
    }
   oPattern:
    {
      int lx, i;
      lx = 1;
      for (i = 1; i <= px; i++) {
        lx = lx * i;
      }
      return lx;
    }
  }
  int fact( int p )
  {
    if (p <= 1)
      return 1;
    else
      return p * fact(p - 1);
  }
*/
//
// Note that this is not a general transformation of recursion to loop
// but a transformation of program fragments that matches with the
// given in-pattern.
//
// III. Patterns that use nonterminal expressions
//
// (1) Example 5 -- Trivial nonterminal
//
// In the above examples, variant parts of patterns are
// represented by parameters and their structure can not be
// specified. In some cases, it may be desirable to restrict
// a variant part to some specific form such as a subscripted
// variable. In order to specify the syntax of variant parts,
// expressions named pattern nonterminal are available.
// Let's see a simple (trivial) example.
/*
 #pragma globalReform patternSym extractPower
 #pragma globalReform nonterminal power
 #pragma globalReform target main
 int _bnfOr(int p, ...) { return p; };
 double power( double p1 );
 double transformPower( double p2 );
 void extractPower( double pv1 )
 {
  iPattern:
   {
     power(pv1);
   }
  oPattern:
   {
     transformPower(power(pv1));
   }
 }
 double power( double pv2)
 {
   _bnfOr(2,
     pv2 * pv2,
     power(pv2) * pv2);
 }
 double aa = 2.0, bb = 3.0;
 int main()
 {
   double x, y, z;
   x = aa * aa;
   y = aa * bb * aa;
   z = aa * aa * bb;
   printf(" %f %f %f \n", x, y, z);
   return 0;
 }
}
*/
// The above example extracts power expressions that multiply
// the same variable several times and call the function
// transformPower. (This example is made to explain the usage
// of pattern nonterminals and is not intended to increase
// execution speed.)
// The function power is a pattern nonterminal that is similar
// to BNF nonterminal. In BNF, a power expression of variable
// v may be defined as
//   powerExp ::= powerExp "*" var | var
// but if the symbol var is a nonterminal representing variables,
// then it is not possible to restrict its operand to the
// same variable. If nonterminals may have parameters, then
// such restriction can be represented.
// A pattern nonterminal may have parameters and its production
// definition takes the form of a function definition that may
// use a special function named "_bnfOr" that selects one of
// productions listed up in the actual parameter list. In the
// definition of the pattern nonterminal
//   double power( double pv2) {
//     _bnfOr(2, pv2 * pv2, power(pv2) * pv2);
//   }
// pv2 is a formal parameter of the pattern nonterminal named
// "power".
//     _bnfOr(2, pv2 * pv2, power(pv2) * pv2);
// represents to select either
//     pv2 * pv2
// or
//     power(pv2) * pv2
// according to given input program. The prototype declaration
// of _bnfOr is
//     int _bnfOr(int, ...);
// The first parameter of _bnfOr is a dummy one attached
// to make _bnfOr as a C function having indefinite number
// of parameters.
//
// When
//     aa * aa
// is given as an expression of input program, then at the
// trial matching with
//     pv2 * pv2
// pv2 is associated with the first operand aa and this trial
// succeeds because the second operand is also aa which
// is tha same as the variable associated with pv2. The
// transformed result in this case is
//    transformPower(aa * aa).
// When
//     aa * bb * aa
// is given as an input expression, at the first trial,
//     power(pv2) * pv2
// is selected associating the second operand pv2 to aa
// because aa*bb*aa is (aa*bb)*aa and its second operand is aa.
// Next, a trial matching of
//     power(pv2)
// with
//     aa * bb
// is done peeling off the trailing "*aa". This trial fails
// because its second operand bb differs from the variable aa
// which was associated with pv2.
// When
//     aa * aa * bb
// is given as an input expression, the first trial matching
// as a whole fails. In the next step, the pattern named power
// is tried to match with the subexpression
//     aa * aa
// and it succeeds. Thus,
//     transformPower(aa * aa) * bb
// is produced as the final result.
//
// (2) Definition form of nonterminals
//
// In general, pattern matching using pattern nonterminal is
// defined as follows.
//
// The pattern nonterminal is similar to a BNF nonterminal and it
// takes the form of a function call that may have parameters.
// The definition of the pattern nonterminal takes the form of
// a function definition that corresponds to a BNF production
// defining a BNF nonterminal. The definition of the pattern
// nonterminal, say NT(p1, p2, ..., pn), takes such a form as
//     type0 NT( type1 p1, type2 p2, ..., typen pn )
//     {
//       _bnfOr(num,
//           exp1,
//           exp2, ...,
//           expm);
//     }
// where type0 represents the type of the expression represented
// by the pattern nonterminal, type1, type2, ... , typen
// represent the type of the formal parameters p1, p2, ..., pn,
// respectively. Each of exp1, exp2, ..., expn represents one
// of the expressions with which the pattern nonterminal is to
// be replaced.
// The function _bnfOr represents to select one of the argument
// expressions exp1, exp2, ..., expm. The first argument num
// may be any integer constant that is added to make the
// function _bnfOr take the form of a C language function with
// indefinite number of arguments.
// The expressions exp1, exp2, ..., expm are C expressions
// that may contain the nonterminal parameters p1, p2, ..., pn
// and references to some nonterminal (in the form of a function
// call).
// Above form is similar to a BNF production of the form
//     NT ::= exp1 | exp2 | ... | expn
// where major differences are as follows:
// (a) Selection of alternatives is represented by _bnfOr
//    function rather than the "or" symbol |.
// (b) Nonterminal reference takes the form of a function call
//    that may have parameters rather than a simple nonterminal
//    identifier.
//
// In the following explanations, the word nonterminal represents
// a pattern nonterminal except otherwise mentioned.
// The definition of a nonterminal may also take such a form
// as
//    type0 NT( type1 p1, type2 p2, ..., typen pn )
//    {
//      exp;
//    }
// when it is not necessary to specify alternatives.
//
// (3) Instanciation of nonterminals
//
// In BNF descriptions such as
//   Expr ::= Var | Expr + Var
//   Var  ::= Identifier
// if x is an identifier, then x, x + x, x + x + x are all
// Expr. Thus, variety of expressions matches to the same
// nonterminal named Expr.
// In the global reform, the same  nonterminal pattern
// can be matched with variety of expressions. At each
// matching operation of a nonterminal pattern, an instance
// of the nonterminal pattern is constructed and matching
// information is recorded as field values of the instance.
// As for the formal parameters and local variables of
// a nonterminal pattern, new instances of the parameters
// and local variables are constructed in accordance with
// the instanciation of the pattern nonterminal, and matching
// information for them are recorded as their field values.
//
// (4) Matching of nonterminals
//
// A reference to a nonterminal may be contained in the
// in-pattern or in the definition of some nonterminal.
// The refered nonterminal is compared with a part of the
// input program at the position where the comparison
// proceeded to the nonterminal reference. When a nonterminal
// is refered, its instance is crerated.
// If the definition of the nonterminal takes the form of
//     type0 NT( type1 p1, type2 p2, ..., typen pn ) {
//       _bnfOr(num, exp1, exp2, ..., expm);
//     }
// then exp1, exp2, ..., expm are successively compared with
// the input program and if fitted one is found, then the
// nonterminal reference is treated as succeeded to match.
// If none of them fitted, then the nonterminal reference is
// treated as failed in the matching.
// If the definition of the nonterminal takes the form of
//     type0 NT( type1 p1, type2 p2, ..., typen pn ) {
//       exp;
//     }
// then exp is matched with the input program and its matching
// result is the matching result of the nonterminal instance
// (invoked by the nonterminal reference).
//
// There is a little difference in the treatment of parameters
// for pattern symbol (pattern parameters) and parameters for
// nonterminals (nonterminal parameter). The actual parameter
// of a nonterminal reference in the in-pattern may be an
// expression containing a pattern parameter. In the matching
// process, the formal parameter contained in the definition
// body of a nonterminal is replaced with the corresponding
// actual parameter of the nonterminal instance. The pattern
// parameter contained in the in-pattern fits in with the input
// program expression located at the position where comparison
// proceeded. A pattern parameter appears in the definition
// body of a nonterminal as the result of replacing formal
// parameter with corresponding actual parameter.
// When a nonterminal reference succeeded to fit in with some
// part of the input program, then the matched part of the
// input program is recorded as the expansion of the
// nonterminal instance (invoked by the nonterminal reference).
// In such expansion, pattern parameters are left without
// replacing with corresponding subpart of the input program.
// In other words, the expansion of a nonterminal reference
// is the matched alternative of _bnfOr of the nonterminal
// where replacement of formal parameters of the nonterminal
// by corresponding actual parameters is done. In the
// expansion of a nonterminal whose definition may contain
// other nonterminal reference, all nonterminal references are
// expanded but pattern parameters conveyed from the in-pattern
// are left so that their replacement can be adjusted later.
// For the same nonterminal reference, its expansion may
// differ according to the instance created by the nonterminal
// reference.
//
// (5) Output pattern handling
//
// As for a nonterminal reference in the out-pattern,
// there should be the nonterminal reference of the same form
// in the corresponding in-pattern. The expansion of the
// nonterminal reference in the out-pattern is the same to
// the expansion of the corresponding nonterminal reference
// in the in-pattern.
// In order to make further adjustment possible, a special
// function
//    _reform( n_th_instance, patternParam,
//             expForReplacement, nontermReference )
// is provided where
//    n_th_instane: integer constant
//    ptternParam: pattern parameter of the pattern
//                 containing _reform
//    expForReplacement: expression to be used to replace
//                 the parameter patternParam
//                 (usually it is an expression containing
//                 patternParam as its operand)
//    nontermReference: nonterminal reference
// The expansion of nonterminal instances are computed
// while the matching of the pattern is executed. In the
// expansion (expanded HIR) computed at the matching phase,
// all nonterminal references are expanded but it may contain
// pattern parameters as it is already mentioned.
// A pattern parameter can be considered as a hole of a
// template that matches to some part of the input program,
// and the HIR subtree that is located at the hole of the
// template is recorded as the one with which the pattern
// parameter is to be replaced. The replacement of pattern
// parameters in the expanded HIR is done in the adjust
// phase which is executed after the matching phase.
// The function _reform can further modify the pattern
// parameter replacement.
//
// (6) Modification by _reform function
//
// The special function
//    _reform( n_th_instance, patternParam,
//             expForReplacement, nontermReference )
// transforms the expanded HIR of the nonterminal instance
// created by nontermReference in order to cope with such
// requests as to generate several versions of the expanded
// HIR changing values of pattern parameters contained in it.
// The above function at first transforms the expanded HIR
// by replacing patterParam containted in the exapnded HIR
// with expForReplacement which may be an expression having
// patternParam as its operand. At the second step,
// patternParam in the resultant extended HIR are replaced
// with the HIR subtree corresponding to patternParam.
// (Above is a conceptual explanation. In actual coding,
// instances of patternParam (say p) contained in
// expForReplacement (say e(p)) are replaced with the HIR
// subtree (say q) and then all instances of patternParam
// in the expanded HIR are replaced by the resultant
// expForReplacement because
//   replacing p in the expanded HIR with e(p) and then
//   replacing p of e(p) in the resultant expanded HIR
//   with q
// is the same to
//   replacing p in e(p) with q (producing e(q)) and then
//   replacing p in the expanded HIR with e(q).
// )
// The example shown in the next section will help
// to understand.
// If the in-pattern contains multiple nonterminal references
// of the same form, assign a sequence number (0, 1, 2, ... )
// to them and give selected one on the number as the first
// parameter n_th_instance. If there is only one instance
// of the nonterminal reference or the first one is to be
// selected, then give 0 as the parameter n_th_instance.
//
// (7) Example 6 -- Loop unrolling

// Let's consider an example using _reform.
/*
 #pragma globalReform patternSym loopUnroll
 #pragma globalReform nonterminal expWith termWith factWith iExp
 #pragma globalReform target main
 int printf(char*, ...);
 #define BSIZE 999
 int expWith( int pi);
 int termWith( int pi);
 int factWith( int pi);
 int iExp( int px);
 int _bnfOr(int p, ...) { return p; }
 int _reform( int p, ... ) { return p; };
 int printf(char*, ...);

 void loopUnroll( int pzz[], int pi, int pFrom, int pTo)
 {
  iPattern:
   {
     for (pi = pFrom; pi < pTo; pi++) {
         pzz[iExp(pi)] = expWith(pi);
     }
   }
  oPattern:
   {
     for (pi = pFrom; pi < pTo-1; pi=pi+2) {
       pzz[iExp(pi)] = expWith(pi);
       pzz[_reform(0, pi, pi+1, iExp(pi))] = _reform(0, pi, pi+1, expWith(pi));
     }
     if ((pTo-pFrom) % 2 != 0)
       pzz[pTo-1] = _reform(0, pi, pTo-1, expWith(pi));
   }
 }
 int expWith( int pi2)
 {
 #pragma globalReform transparentFitting pc2 (pi2)
   int pc2;
  _bnfOr(1,
   pc2 + expWith(pi2),
   pc2 - expWith(pi2),
   expWith(pi2) + pc2,
   expWith(pi2) - pc2,
   expWith(pi2) + termWith(pi2),
   expWith(pi2) - termWith(pi2),
   termWith(pi2)
   );
 }
 int termWith( int pi3 )
 {
 #pragma globalReform transparentFitting pc3 (pi3)
   int pc3;
  _bnfOr(2,
   pc3 * termWith(pi3),
   pc3 / termWith(pi3),
   termWith(pi3) * pc3,
   termWith(pi3) / pc3,
   termWith(pi3) * factWith(pi3),
   termWith(pi3) / factWith(pi3),
   factWith(pi3)
   );
 }
 int factWith( int pi4 )
 {
 #pragma globalReform transparentFitting pc4 (pi4)
   int *pc4;
  _bnfOr(2,
   pc4[iExp(pi4)],
   pi4
   );
 }
 int iExp( int px )
 {
 #pragma globalReform transparentFitting pc5 (px)
   int pc5;
  _bnfOr(3,
   px + pc5,
   px - pc5,
   px * pc5,
   px / pc5,
   px
   );
 }
 int main()
 {
   int zz[BSIZE];
   int i, n;
   n = BSIZE;
   for (i = 0; i < n; i++) {
       zz[i] = i;
   }
   for (i = 0; i < n; i++) {
       zz[i] = zz[i] * 2;
   }
   printf(" %d %d %d %d %d\n", n,
          zz[0], zz[1], zz[n-2], zz[n-1]);
   return 0;
 }
*/
// The pattern named loopUnrol changes loops such as
//    for (i = 0; i < n; i++) {
//      zz[i] = zz[i]*2;
//    }
// to such a form as
//    for (i = 0; i < n-1; i=i+2) {
//      zz[i] = zz[i]*2;
//      zz[i+1] = zz[i+1]*2;
//    }
//    if ((n-0) % 2 != 0)
//      zz[n-1] = zz[n-1]*2;
// by unrolling the loops. Variables as arithmetic operands
// of the loop body may be any subscripted variable having
// i + c2 or i - c2, i * pc, i / c2 as its subscript,
// where c1 and c2 may be any integer expression that does
// not contain the loop index i. Pattern nonterminals are
// used to specify such syntax.
//
// The pattern nonterminal
//    int iExp( int px ) {
//    #pragma globalReform transparentFitting pc5 (px)
//      int pc5;
//      _bnfOr(3, px+pc5, px-pc5, px*pc5, px/pc5, px );
//    }
// fits in with one of expressions
//   px + pc5, px - pc5, px * pc5, px / pc5, px
// where the expression pc5 does not contain the parameter
// px which is conveyed from upper construct. (The upper
// construct means a nonterminal description referring
// this pattern nonterminal or an input component that
// fits in with this pattern).
// The pragma
//    #pragma globalReform transparentFitting p (q, r, ..., s)
// declares that the expression corresponding to p does not
// contain any of the variables q, r, ..., s.
// The pattern nonterminal
//   int factWith( int pi4 ) {
//   #pragma globalReform transparentFitting pc4 (pi4)
//     int *pc4;
//    _bnfOr(2,
//     pc4[iExp(pi4)],
//     pi4
//     );
//   }
// fits in with any subscripted variable whose subscript
// is an integer expression satisfying the restriction of
// iExp(pi4), where pc4 is an array variable that is
// different from pi4 conveyed from upper construct.
// The nonterminal also fits in with the variable pi4.
// The pattern nonterminal
//   int termWith( int pi3 ) {
//   #pragma globalReform transparentFitting pc3 (pi3)
//     int pc3;
//    _bnfOr(2,
//     pc3 * termWith(pi3),
//     pc3 / termWith(pi3),
//     termWith(pi3) * pc3,
//     termWith(pi3) / pc3,
//     termWith(pi3) * factWith(pi3),
//     termWith(pi3) / factWith(pi3),
//     factWith(pi3)
//     );
//   }
// fits in with any expression specified by
// factWith(pi3) when the top operator of the
// expression is neither '*' nor '/'. If the top operator of
// the expression is either '*' or '/', then the pattern
// nonterminal fits in with the expression when its first
// operand fits in with termWith(pi3) and its second
// operand is either an expression specified by
// factWith(pi3) or pc3 that does not contain pi3 as operand.
// The parameter pi3 is conveyed from upper construct.
// The pattern nonterminal
//   int expWith( int pi2) {
//   #pragma globalReform transparentFitting pc2 (pi2)
//     int pc2;
//    _bnfOr(1,
//     pc2 + expWith(pi2),
//     pc2 - expWith(pi2),
//     expWith(pi2) + pc2,
//     expWith(pi2) - pc2,
//     expWith(pi2) + termWith(pi2),
//     expWith(pi2) - termWith(pi2),
//     termWith(pi2)
//     );
//   }
// matches with any expression specified
// by termWith(pi2) if its top operator is neither
// '+' nor '-'.  If its top operator is either '+' or '-',
// then the pattern nonterminal matches with the expression
// when its first operand matches with
//   expWith(pi2)
// and its second operand is either an expression specified
// by termWith(pi2) or pc2. The parameter pi2 is conveyed
// from upper construct.
// In the matching process of the in-patterm with the first
// loop of the main program, trial matching of the statement
//    pzz[iExp(pi)] = expWith(pi);
// in the in-pattern with the statement
//    zz[i] = i;
// takes place.
// In that trial, correspondence of pattern parameter pzz
// with variables zz is tentatively established and an
// instance of the pattern nonterminal iExp is created.
// The iExp(pi) fits in with i making correspondence of pi
// to i and the expanded HIR of the instance of iExp is
// the pattern parameter pi. As the matching of the first
// operand of the assign statement succeeds, trial
// matching of expWith(pi) with the right hand side of
// the assign statement takes place.
// At the beginning of the trial matching for the right hand
// side, an instance of expWith is created. As the right hand
// side expression has neither '+' nor '-' as its top
// operator, the production
//   termWith(pi2)
// is selected to be used in the sub-trial and an instance of
// termWith is created. Since the top operator is neither
// '*' nor '/', the production
//   factWith(pi3)
// is selected and an instance of factWith is created.
// In the trial matching of factWith, pi4 is selected because
// the right hand side is not a subscripted variable but
// a simple variable i.
// The above matching succeeds and correspondences of
//   instance of pi4      to pi
//   instance of iExp     to pi
//   instance of factWith to pi
//   instance of termWith to pi
//   instance of expWith  to pi
//   pattern parameter pi to i
// are established.
// In the out-pattern, there is a statement
//   pzz[_reform(0, pi, pi+1, iExp(pi))]
//       = _reform(0, pi, pi+1, expS(pzz, pi)); .
// As for _reform(0, pi, pi+1, iExp(pi)), the expanded HIR
// of iExp(pi) is pi and pi is associated to i, hence
//   pzz[_reform(0, pi, pi+1, iExp(pi))]
// is transformed to
//   i+1 .
// As for _reform(0, pi, pi+1, expWith(pi)), the expanded
// HIR of expWith(pi) is pi, and pi is assosicated to i.
// Hence
//   _reform(0, pi, pi+1, expS(pzz, pi))
// is also transformed to
//   i+1 .
// It leads to the transformation of
//    for (i = 0; i < n; i++) {
//      zz[i] = i;
//    }
// to such a form as
//    for (i = 0; i < n-1; i=i+2) {
//      zz[i] = i;
//      zz[i+1] = i+1;
//    }
//    if ((n-0) % 2 != 0)
//      zz[n-1] = n-1;
//
// In the matching process of the in-patterm with the second
// loop of the main program, trial matching of the statement
//    pzz[iExp(pi)] = expWith(pi);
// in the in-pattern with the statement
//    zz[i] = zz[i] * 2;
// takes place.
// The sub-pattern pzz[iExp(pi)] matches with the left hand
// side zz[i] of the assign-statement and the correspondence
// of pzz with zz is tentatively established.
// In the next step, trial matching of
//    expWith(pi)
// with the right hand side
//    zz[i] * 2
// takes place. As the right hand side expression has
// neither '+' nor '-' as its top operator, the production
//   termWith(pi2)
// is selected to be used in the sub-trial and an instance of
// termWith is created.
// As the top operator of the input expression
//   zz[i] * 2
// is '*' and its operand 1 contains the variable i,
//   termWith(pi3) * pc3
// is selected as the sub-pattern to be matched with.
// It leads to a sequence of trial matchings of
//   termWith(pi2)
//   factWith(pi3)
//   pc4[iExp(pi4)]
// with the subscripted variable zz[i] and new instances of
// nonterminals and local variables are associated with
// expressions as follows:
//   pattern parameter pzz to zz
//   instance of pi4       to pi
//   instance of iExp      to pi
//   instance of factWith  to pzz[pi]
//   instance of termWith  to pzz[pi]
//   instance of pc3       to 2
//   another instance of termWith to pzz[pi]*pc3
//   instance of expWith   to pzz[pi]*pc3
//   pattern parameter pi  to i
// Thus, expWith(pi) matches with the expression zz[i]*2
// and it is concluded that the pattern loopUnroll
// matches with the second loop. By evaluating _reform
// expressions in the out-pattern, the loop
//    for (i = 0; i < n; i++) {
//      zz[i] = zz[i]*2;
//    }
// in the input program is transformed to
//    for (i = 0; i < n-1; i=i+2) {
//      zz[i] = zz[i]*2;
//      zz[i+1] = zz[i+1]*2;
//    }
//    if ((n-0) % 2 != 0)
//      zz[n-1] = zz[n-1]*2;
// by substituting pattern parameters and fitting
// symbol with the corresponding expressions.
//
// (8) Other special function
//
// There is a special function named _assignStmt that
// generates assign-statement from given parameters.
// Assign-statement requires l-value expression such as
// variable as its left hand side expression. This means
// that pattrern nonterminals can not be written at the
// left hand side of assign-statements. It is a big
// restriction in describing global patterns.
// The expression
//    _assignStmt( 0, expression_L, expression_R )
// written in in-pattern matches with an assign-statement
// of the form
//    expL = expR;
// where expL and expR are expressions that match with
// expression_L and expression_R respectively. Here,
// expression_L may be an l-value expression or a
// pattern nonterminal representing an l-value expression,
// and expression_R is an r-value expression or a pattern
// nonterminal representing r-value expression.
// The first parameter 0 is a dummy parameter to make the
// prototype of the function as
//    int _assignStmt(int, ...);
// so that its other parameters may be of any type.
// The expression
//    _assignStmt( 0, expression_L, expression_R )
// written in out-pattern generates an assign-statement
// of the form
//    expL = expR;
// where the symbols expression_L, expression_R, expL, expR
// have the same meaning as above.
//
// COINS has the optimization module that is dedicated to loop unrolling.
// It is implemented in about 2000 lines of coding and can be applied
// to wide variety of loops. The above global pattern matching example can
// not replace the dedicated loop unrolling module but shows another
// implementation of loop unrolling tansformation in less than 100
// lines of coding applicable only for typical coding patterns.
//
// Note
//
// In the above discussions, transformations are explained as
// source program transformations for ease of understanding,
// however, in actual, they are an HIR-to-HIR transformation.
// The global pattern matching using pattern nonterminals
// will arrow variety of transformations.
//
  protected HirRoot hirRoot;
  protected SymRoot symRoot;
  protected IoRoot ioRoot;
  protected Map fOptionMap;  // Map of compile option and its specifications.
  protected CoinsOptions fCoinsOptions; // Specifications of option -coins:
  protected List fPatternList; // List of pattern symbols specified by
                   // #pragma globalReform patternSym pattern1 pattern2 ... .
  protected Map  fInPatternMap;  // Map a pattern symbol to its in-pattern.
  protected Map  fOutPatternMap; // Map a pattern symbol to its out-pattern.
  protected List fNonterminalList; // List of nonterminals defined.
  protected Set  fNonterminalSet;  // Set  of nonterminals defined.
  protected Map  fProductionMap;   // Map a nonterminal to its production.
  protected Set  fFittingSet;    // Set of fitting symbols.
  protected Map  fTransparentMap;// Map a transparent variable to a set of
                                 // parameters that does not appear in the
                                 // expression corresponding to the variable.
  protected int  fPatternCount;  // Number of patterns.
  protected int  fNontermPatCount; // Counter used to index nontermPat symbols
                                 // (pattern symbols and nonterminal symbols).
  protected int  fSubrootCount;  // Counter for getting the total number of
                                 // subroots that appear as the right-hand-side
                                 // HIR in in-patterns and nonterminals.
  protected int  fProductionCount; // Number of patterns plus total number
                                 // of productions defined in nonterminals.
  protected Subp[] fNontermPat;  // Array of nontermPat symbols
                                 // indexed by the index of nontermPat symbol.
  protected Map  fLocalVarListMap; // Map the pattern symbol to the list
                   // of variables declared locally in its out-pattern.
  protected List fSubpToReform; // List of subprograms to be reformed.
                   // They are specified by
                   // #pragma globalReform target subp1 subp2 ... .
  protected Set fStmtParamSet;
                   // List of pattern parameters that represent a statement.
                   // Such statement parameters are specified by
                   // #pragma globalReform stmtParam param1 param2 ... .
  protected Set fNoFurtherChange;  // Set of pattern symbols
                   // for which transformed results are not desirable
                   // for further transformation so as to keep
                   // the transformed shape until it is properly
                   // treated by some specific module such as SIMD
                   // instruction generation.
                   //##91 BEGIN
  protected Set fCompileTimeEval;  // Set of functions
                   // to be evaluated at compile time.
                   // They are one of
                   //   _getName(Sym): get the name string of the symbol
                   //   _varArgList(): list of the variable number of arguments
                   //   _varArgFormat():
  protected Set fFitToAnyCall;  // Set of functions that
                   // are treated to fit to any subprogram call.
                   // The set includes
                   //   _anyFun
                   //##91 END
  protected Subp fCurrentPatternSym = null; // The pattern symbol
                   // that is used in current transformation/matching.
  protected boolean[] fUsedAsPatternRoot;
    // fUseAsPatternRoot[op] is 1 if HIR operator code op is used
    // as the operator of subroot of some in-pattern.
    // This array is used as the first level filter to quickly
    // find an HIR subtree to be matched with in-patterns.
  protected boolean[] fUsedAsSubRoot;  // Unused ?  // REFINE
    // fUseAsSubRoot[op] is 1 if HIR operator code op is used
    // as the operator of subroot of some in-pattern or
    // some nonterminal.
    // This array is used as the first level filter to quickly
    // find an HIR subtree to be matched with in-patterns and
    // nonterminal.
    //
    //-- The arrays fCodeIndexFrom[], fCodeIndexTo[],
    //   fPatternCodeUpper[], fPatternCodeLower[],
    //   fStmtsInPattern[], fNext1CodeUpper[], fNext1CodeLower[],
    //   fNext2CodeUpper[], fNext2CodeLower[]
    //   are indexed by the value of getIndex(patternSym) or
    //   getIndex(nonterminal).
  protected int fCodeIndexFrom[];
    // Starting index of fPatternCodeUpper and fPatternCodeLower.
  protected int fCodeIndexTo[];
    // Ending index of fPatternCodeUpper and fPatternCodeLower.
    // Usually, pattern symbol has only one pair of upper and lower,
    // but a nonterminal symbol may have multiple pairs of
    // upper and lower because for each production connected by
    // _bnfOr there corresponds one pair of upper and lower.
  protected int fStmt1CodeIndexFrom[], fStmt1CodeIndexTo[],
                fStmt2CodeIndexFrom[], fStmt2CodeIndexTo[];
    // Same as fCodeIndexFrom, fCodeIndexTo except that
    // they correspond to statement 1 and statement 2 of
    // the pattern having multiple statements.
    // For a pattern i having only one statement or expression,
    // fStmt1CodeIndexFrom[i] and fStmt1CodeIndexTo[i] are 0.
  protected double fPatternCodeUpper[],
                   fPatternCodeLower[];
    // fPatternCodeUpper and fPatternCodeLower hold the upper
    // bound and lower bound of pattern code respectively.
    // For each pattern or nonterminal i, starting index and ending index
    // are indicated by fCodeIndexFrom[i], fCodeIndexTo[i].
  protected int fStmtsInPattern[];
    // Number of statements in each pattern.
  protected Set fPatternParameters;
    // Set of symbols used as formal parameter in some pattern.
 protected Set fNonterminalParameters;
      // Set of symbols used as formal parameter in some nonterminal definition.
  protected int fProductionsInNonterminal[];
    // Number of productions for each nonterminal.
    // Indexed by the value of getIndex(nonterminal).
  protected int fMaxStmtsInPattern;
    // Maximum number of statements in in-patterns encountered.
  protected double fNextStmtCode;
    // fNextStmtCode holds the pattern code of the
    // next statement.
    // If it is not yet computed, fNextStmtCode is 0.
  protected Stmt fCurrentStmt,  // Statement under matching process.
                 fNextStmt;     // Statement next to fCurrentStmt.
  protected Stmt fTailStmtMatched; // Statement up to which
    // transformation has been done according to
    // a pattern of statement sequence (by matchInputStmtSeq).
  protected Map fMatchingDataForNontermInstance;  // Map of MatchingData
    // that matched last time for each nonterminal instance.
    // It uses call exp itself (without using makeHirkey).
  protected Map fMatchingDataForNonterm;  // Map of MatchingData
    // that matched last time for each nonterminal.
    // It uses call exp as key (makeHirKey).
  protected MatchingData[] fLastMatchingDataForNonterm;  // Map of MatchingData
    // that matched last time for each nonterminal.
    // It is indexed by the index number assigned to the nontermPat symbols.
  protected Map fGlobalPatternParamMap; // Map of pattern parameters
    // common between MatchingData instances for the patterns
    // that are in process of application.
    // It is recorded by the method putToMap.
  protected Map fExpandedNontermInstance;  // For each nonterminal instance,
    // matched production is expanded and recorded here.
    // The expanded HIR contains neither nonterminal nor _bnfOr, _bnfSeq.
    // The expanded HIR does not contain nonterminal parameter
    // but may contain pattern parameter (nonterminal parameter
    // in the matched production is changed to pattern parameter
    // seeing correspondences between actual parameter and formal
    // parameter).
    // The expande HIR is get by using an instance of NontermPatInstance
    // as a key.
  protected Map fExpandedNonterm;  // For each nonterminal,
    // matched production is expanded and recorded here.
    // It uses call exp as key (makeHirKey).
  protected NontermPatInstance fNontermPatInstanceOfCurrentPattern;
    // Record the nontermPatInstance of the current pattern.
  protected Set fHirKeys;
    // Set of HIR expressions used as keys in fInputForArgList.
  protected Set[] fFormalParams;  // Array of the set of formal parameters
    // indexed by the index number assigned to the
    // nontermPat symbols.
    // This is used to get the set of formal parameters for each
    // pattern or nonterminal.
  //##86 BEGIN
  protected int[] fInstanceNumberForNonterminals;
    // Current instance number for each nonterminal.
    // It is indexed by the index number assigned to the nontermPat symbols
    // (getIndex).
  protected Map fOriginalVarMap;
    // Map the instance of a parameter or fitting symbol
    // to the original variable
    // from which the instance is generated (instanciated).
  protected Map fNontermInstanceInPattern;
    // Map a nonterminal call expression in in-pattern to
    // the NontermPatInstance corresponding to the expression.
    // There may be several nonterminal calls having the same form.
    // To solve this problem, its key is the representative call exp
    // get by getHirKey and its value is the list of NontermPatInstances
    // arranged in the same order as corresponding call appeares in
    // the in-pattern.

  //##86 END
  static double fHirCodeLim   = 100.0; // A number greater than fDontCareCode.
  static double fDontCareCode =  99.0; // A number greater than any HIR operator code.
  static double fHirCodeLim2  = fHirCodeLim  * fHirCodeLim;
  static double fHirCodeLim3  = fHirCodeLim2 * fHirCodeLim;
  static double fHirCodeLim4  = fHirCodeLim3 * fHirCodeLim;
  static double fHirCodeLim5  = fHirCodeLim4 * fHirCodeLim;
  static double fHirCodeLim6  = fHirCodeLim5 * fHirCodeLim;
  protected boolean fChanged;
            // fChanged is set to true if some transformation is done.
            // It is set to false if no change is done.
  protected int fDbgLevel;  // Debug level.
  protected HIR hir;

  /**
   * Constructor GlobalTransform.
   * Process pragmas
   *   #pragma globalReform patternSym pattern1 pattern2 ...
   *   #pragma globalReform target subp1 subp2 ...
   * Make pattern map fPatternMap showing the correspondence
   * of in-pattern and out-pattern checking the consistency
   * of parameter usage in the pattern.
   * @param pHirRoot HirRoot conveyed.
   */
  public GlobalReform(HirRoot pHirRoot)
  {
    hirRoot = pHirRoot;
    symRoot = pHirRoot.symRoot;
    ioRoot = pHirRoot.ioRoot;
    fDbgLevel = ioRoot.dbgOpt1.getLevel();
    hir = hirRoot.hir;
    fCoinsOptions = ioRoot.getCompileSpecification().getCoinsOptions();
    String lOptArg = fCoinsOptions.getArg("hirOpt");
    fOptionMap = fCoinsOptions.parseArgument(lOptArg, '/', '.');
    if (fDbgLevel > 0) {
      dbgOut(1, "\nGlobalReform\n");
      dbgOut(2, "\n CoinsOptions " + fCoinsOptions);
      dbgOut(2, "\n Option map " + fOptionMap);
    }
    if (! fOptionMap.containsKey("globalReform"))
    {
      dbgOut(2, "no globalReform option is given.\n");
      return;
    }
    fNontermPatCount  = 0;
    List lSubprograms = new ArrayList();
    fPatternList      = new ArrayList();
    fInPatternMap     = new HashMap();
    fOutPatternMap    = new HashMap();
    fLocalVarListMap  = new HashMap();
    fSubpToReform     = new ArrayList();
    fNonterminalList  = new ArrayList();
    fNonterminalSet   = new HashSet();
    fProductionMap    = new HashMap();
    fTransparentMap   = new HashMap();
    fFittingSet       = new HashSet();
    fNoFurtherChange  = new HashSet();
    fCompileTimeEval  = new HashSet();  //##91
    fFitToAnyCall     = new HashSet();  //##91
    fStmtParamSet     = new HashSet();  //##80
    fPatternParameters= new HashSet();
    fNonterminalParameters = new HashSet();
    Program lProgram = (Program)hirRoot.programRoot;
    IrList lSubpList = lProgram.getSubpDefinitionList();
    if (fDbgLevel >= 4) {
      dbgOut(3, "\nHIR before GlobalReform \n");
      lProgram.print(0);
    }
    //-- Make the list of subprograms defined.
    for (Iterator lIt = lSubpList.iterator();
         lIt.hasNext(); ) {
      SubpDefinition lSubpDef = (SubpDefinition)lIt.next();
      lSubprograms.add(lSubpDef.getSubpSym());
    }
    dbgOut(3, "Subprograms defined", lSubprograms + "\n");
    int lSubpCount = lSubpList.size();
    fNontermPat = new Subp[lSubpCount + 1];
    //## fInputForArgList = new Map[lSubpCount + 1];
    fLastMatchingDataForNonterm = new MatchingData[lSubpCount + 1];
    //##88 fLastExpandedNonterm        = new HIR[lSubpCount + 1];
    fMatchingDataForNontermInstance = new HashMap();
    fMatchingDataForNonterm = new HashMap();
    fExpandedNonterm        = new HashMap();
    fExpandedNontermInstance= new HashMap();
    fHirKeys = new HashSet();
    //---- Get pragma information from program initiation part. ----//
    HIR lProgInitPart = (HIR)lProgram.getInitiationPart();
    if (lProgInitPart instanceof BlockStmt) {
      for (Stmt lStmt = ((BlockStmt)lProgInitPart).getFirstStmt();
           lStmt != null;
           lStmt = lStmt.getNextStmt()) {
        if ((lStmt instanceof InfStmt) &&
            (((InfStmt)lStmt).getInfKind() == "globalReform")) {
          if (fDbgLevel > 2)
            dbgOut(3, lStmt.toStringWithChildren() + "\n");
          IrList lOptionList = ((InfStmt)lStmt).getInfList("globalReform");
          int lIndex;
          if (fDbgLevel > 2)
            dbgOut(4, " option list " + lOptionList + "\n");
          String lOptionName = ((InfStmt)lStmt).getInfSubkindOf("globalReform");
          if (lOptionName == null) {
            dbgOut(1, "\nUnknown option subkind " +
              ((InfStmt)lStmt).toStringWithChildren() + "\n");
            continue;
          }
          if (lOptionName == "patternSym") {
            // #pragma globalReform patternSym pattern1 pattern2 ...
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              if (fDbgLevel > 2)
                dbgOut(5, " " + lSubp
                + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                // Record the symbol of pattern
                // that takes the form of subprogram.
                fPatternList.add(lSubp);
                fNontermPatCount++;
                fNontermPat[fNontermPatCount] = (Subp)lSubp;
                ((Subp)lSubp).setOptInf(new OptInf());
                ((OptInf)((SubpImpl)lSubp).getOptInf()).fIndex =
                  fNontermPatCount;
              }
            }
          }else if (lOptionName == "target") {
            // #pragma globalReform target subp1 subp2 ...
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                  lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              if (fDbgLevel > 2)
                dbgOut(5, " " + lSubp
                 + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                // Record the subprogram to be transformed.
                fSubpToReform.add(lSubp);
              }
            }
          }else if (lOptionName == "noFurtherChange") {
            // #pragma globalReform noFurtherChange pattern1 pattern2 ...
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                  lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              if (fDbgLevel > 2)
                dbgOut(5, " " + lSubp
                 + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Subp)) {
                // Record the pattern as no further change is desirable.
                fNoFurtherChange.add(lSubp);
              }
            }
         }else if (lOptionName == "nonterminal") {
           // #pragma globalReform nonterminal nonterm1 nonterm2 ...
           lIndex = 0;
           for (Iterator lIt = lOptionList.iterator();
                 lIt.hasNext(); lIndex++) {
             Object lSubp = lIt.next();
             if (fDbgLevel > 2)
               dbgOut(5, " " + lSubp
                + " " + lSubp.getClass() + "\n");
             if ((lSubp instanceof Subp)) {
               // Record the subprogram to be transformed.
               fNonterminalList.add(lSubp);
               fNonterminalSet.add(lSubp);
               fNontermPatCount++;
               fNontermPat[fNontermPatCount] = (Subp)lSubp;
               ((Subp)lSubp).setOptInf(new OptInf());
               ((OptInf)((SubpImpl)lSubp).getOptInf()).fIndex =
                 fNontermPatCount;
             }
           }
         //##91 BEGIN
         }else if ((lOptionName == "compileTimeEval")||
                   (lOptionName == "fitToAnyCall")) {
            // #pragma globalReform compileTimeEval func1 func2 ...
            lIndex = 0;
            for (Iterator lIt = lOptionList.iterator();
                  lIt.hasNext(); lIndex++) {
              Object lSubp = lIt.next();
              if (fDbgLevel > 2)
                dbgOut(5, " " + lSubp
                 + " " + lSubp.getClass() + "\n");
              if ((lSubp instanceof Sym)) {
                if (lOptionName == "compileTimeEval") {
                  // Record it as a compile time evaluation function.
                  fCompileTimeEval.add(lSubp);
                }else if (lOptionName == "fitToAnyCall") {
                  fFitToAnyCall.add(lSubp);
                }
              }
            }
         //##91 END
         }else {
            dbgOut(1, "\nUnknown option " + lOptionName + "\n");
            continue;
          }
        } // End for globalReform option
      } // End for each Stmt in InitiationPart
    } // End of ProgInitPart
    fPatternCount = fPatternList.size();
    fStmtsInPattern = new int[fPatternCount + 1];
    dbgOut(1, "List of patterns", fPatternList.toString());
    dbgOut(1, "List of nonterminals", fNonterminalList.toString());
    dbgOut(1, "Subprograms to be reformed", fSubpToReform.toString());
    //---- Make pattern map. ----//
    SubpDefinition lSubpDef;
    Label lInLabel, lOutLabel;
    Stmt lInPatternPart, lOutPatternPart;
    HIR lInPattern, lOutPattern;
    //-- Process the list of patterns.
    for (Iterator lIt = fPatternList.iterator();
         lIt.hasNext(); ) {
      Subp lSubp = (Subp)lIt.next();
      dbgOut(2, "\n Scan pattern " + lSubp.getName());
      // lSubp is a pattern having the form of a subprogram.
      for (Iterator lParamIt = lSubp.getParamList().iterator();
           lParamIt.hasNext(); ) {
        Sym lParam1 = (Sym)lParamIt.next();
        fPatternParameters.add(lParam1);
      }
      lSubpDef = lSubp.getSubpDefinition();
      Stmt lSubpBody = lSubpDef.getHirBody();
      if (lSubpBody instanceof LabeledStmt) {
        lSubpBody = ((LabeledStmt)lSubpBody).getStmt();
      }
      lInPattern = null;
      lOutPattern = null;
      Stmt lStmt;
      dbgOut(2, "\n lSubpBody " + lSubpBody.toString());
      if (lSubpBody instanceof BlockStmt) {
        lStmt = ((BlockStmt)lSubpBody).getFirstStmt();
        while (lStmt != null) {
          if (fDbgLevel > 1)
            dbgOut(3, "\n lStmt " + lStmt.toStringWithChildren());
          if ((lStmt instanceof InfStmt) &&
              (((InfStmt)lStmt).getInfKind() == "globalReform")) {
            // This is a pragma.
            if (fDbgLevel > 1)
              dbgOut(3, lStmt.toString() + "\n");
            IrList lOptionList = ((InfStmt)lStmt).getInfList("globalReform");
            int lOptIndex;
            if (fDbgLevel > 1)
              dbgOut(4, " option list " + lOptionList + "\n");
            String lOptionName = ((InfStmt)lStmt).getInfSubkindOf(
              "globalReform");
            if (lOptionName == null) {
              dbgOut(1, "\nUnknown option subkind " +
                ((InfStmt)lStmt).toString() + "\n");
              continue;
            }
            if (lOptionName == "stmtParam") {
              // #pragma globalReform stmtParam param1 param2 ...
              lOptIndex = 0;
              for (Iterator lOptIt = lOptionList.iterator();
                   lOptIt.hasNext(); lOptIndex++) {
                Object lParam = lOptIt.next();
                if (fDbgLevel > 2)
                  dbgOut(5, " " + lParam + " " + lParam.getClass() + "\n");
                if (lParam instanceof Param) {
                  // Record it as a parameter representing a statement.
                  fStmtParamSet.add(lParam);
                }
              }
            }else if ((lOptionName == "transparent")||
                      (lOptionName == "transparentFitting")) {
              processTransparent(lSubp, lOptionName, lOptionList);
            }
          } // End of InfStmt
          else if ((lStmt instanceof LabeledStmt) &&
                   (((LabeledStmt)lStmt).getLabel().getName()
                    == "iPattern")) {
            // iPattern: { .... } is encountered.
            // Treat it as an in-pattern.
            lInPattern = ((LabeledStmt)lStmt).getStmt();
            if (lInPattern == null) {
              lStmt = lStmt.getNextStmt();
              lInPattern = lStmt;
            }
            if (lInPattern.getOperator() == HIR.OP_BLOCK) {
              Stmt lStmtFirst = ((BlockStmt)lInPattern).getFirstStmt();
              while (lStmtFirst instanceof InfStmt)
                lStmtFirst = lStmtFirst.getNextStmt(); // Skip InfStmt
              if (lStmtFirst != null) {
                if (lStmtFirst.getNextStmt() == null) {
                  // Contains only one statement.
                  // Peal off the block.
                  if (lStmtFirst.getOperator() == HIR.OP_EXP_STMT) {
                    // in-pattern is an expression.
                    lInPattern = ((ExpStmt)lStmtFirst).getExp();
                  }else {
                    lInPattern = lStmtFirst;
                  }
                }else {
                  // The block contains multiple statements.
                  // Leave as a BlockStmt.
                }
              }
            }
          }
          else if ((lStmt instanceof LabeledStmt) &&
                   (((LabeledStmt)lStmt).getLabel().getName() ==
                    "oPattern")) {
            // oPattern: { .... } is encountered.
            // Treat it as an out-pattern.
            lOutPattern = ((LabeledStmt)lStmt).getStmt();
            if (lOutPattern == null) {
              lStmt = lStmt.getNextStmt();
              lOutPattern = lStmt;
            }
            if (lOutPattern.getOperator() == HIR.OP_BLOCK) {
              Stmt lStmtFirst = ((BlockStmt)lOutPattern).getFirstStmt();
              while (lStmtFirst instanceof InfStmt)
                lStmtFirst = lStmtFirst.getNextStmt(); // Skip InfStmt
              if ((lStmtFirst != null)&&
                  (lStmtFirst.getNextStmt() == null)) {
                // Contains only one statement.
                // Peal off the block.
                if (lStmtFirst.getOperator() == HIR.OP_EXP_STMT) {
                  // in-pattern is an expression.
                  lOutPattern = ((ExpStmt)lStmtFirst).getExp();
                }else {
                  lOutPattern = lStmtFirst;
                }
              }
            }
          }
          lStmt = lStmt.getNextStmt();
        } // End of Stmt loop.
        if (lInPattern == null)
          ioRoot.msgRecovered.put(5111, "iPattern is not found in "
            + lSubp.getName());
        if (lOutPattern == null)
          ioRoot.msgRecovered.put(5111, "oPattern is not found in "
            + lSubp.getName());
        if ((lInPattern != null) && (lOutPattern != null)) {
          // Record in the map showing the correspondence of
          // in-pattern and out-pattern.
          fInPatternMap.put(lSubp, lInPattern);
          fOutPatternMap.put(lSubp, lOutPattern);
          setNonterminalFlag(lInPattern);
          setNonterminalFlag(lOutPattern);
       }
        else
          continue;
      } // End of BlockStmt
      List lLocalVarList = new ArrayList();
      checkConsistency(lSubp, lLocalVarList, lInPattern, lOutPattern);
      fLocalVarListMap.put(lSubp, lLocalVarList);
    } // End of pattern list processing
    dbgOut(2, "InPatternMap", fInPatternMap.toString());
    dbgOut(2, "OutPatternMap", fOutPatternMap.toString());
    dbgOut(2, "transparentFitting", fFittingSet.toString());
    dbgOut(2, "compileTimeEval", fCompileTimeEval.toString());
    dbgOut(2, "LocalVarListMap", fLocalVarListMap.toString());
    fProductionCount = fPatternCount;
    //---- Process nonterminals
    for (Iterator lNtIt = fNonterminalList.iterator();
      lNtIt.hasNext(); ) {
      Subp lSubp = (Subp)lNtIt.next();
      dbgOut(2, "\n Scan the production part of the nonterminal " + lSubp.getName());
      for (Iterator lParamIt = lSubp.getParamList().iterator();
           lParamIt.hasNext(); ) {
        Sym lParam1 = (Sym)lParamIt.next();
        fNonterminalParameters.add(lParam1);
      }
      // lSubp is a nonterminal definition having the form of a subprogram.
      lSubpDef = lSubp.getSubpDefinition();
      Stmt lSubpBody = lSubpDef.getHirBody();
      if (lSubpBody instanceof LabeledStmt) {
        lSubpBody = ((LabeledStmt)lSubpBody).getStmt();
      }
      HIR lRhsOfProduction = null; // Right hand side of production.
      Stmt lStmt;
      dbgOut(2, "\n lSubpBody " + lSubpBody.toString());
      if (lSubpBody instanceof BlockStmt) {
        lStmt = ((BlockStmt)lSubpBody).getFirstStmt();
        while (lStmt != null) {
        // Definition of nonterminals usually contains one statement
        // of the form _bnfOr( ... ) or _bnfSeq( ... )
        // plus sequence of pragmas.
          if (fDbgLevel > 1)
            dbgOut(3, "\n lStmt " + lStmt.toStringWithChildren());
          if ((lStmt instanceof InfStmt) &&
              (((InfStmt)lStmt).getInfKind() == "globalReform")) {
            // This is a pragma.
            if (fDbgLevel > 1)
              dbgOut(3, lStmt.toString() + "\n");
            IrList lOptionList = ((InfStmt)lStmt).getInfList("globalReform");
            int lOptIndex;
            if (fDbgLevel > 1)
                dbgOut(4, " option list " + lOptionList + "\n");
            String lOptionName = ((InfStmt)lStmt).getInfSubkindOf(
                "globalReform");
            if (lOptionName == null) {
                dbgOut(1, "\nUnknown option subkind " +
                  ((InfStmt)lStmt).toString() + "\n");
                continue;
            }
            if ((lOptionName == "transparent")||
                (lOptionName == "transparentFitting")||
                (lOptionName == "transparentStmt")) {
              processTransparent(lSubp, lOptionName, lOptionList);
            }
          } // End of InfStmt
          else if ((lStmt instanceof ExpStmt)&&
                   (lStmt.getChild1().getOperator() == HIR.OP_CALL)) {
            HIR lCallNode = (HIR)lStmt.getChild1();
            Sym lSubpSym = null;
            if (lCallNode.getChild1()instanceof SymNode) {
              lSubpSym = ((SymNode)lCallNode.getChild1()).getSymNodeSym();
            }else if ((lCallNode.getChild1().getOperator() == HIR.OP_ADDR) &&
               (lCallNode.getChild1().getChild1()instanceof SymNode)) {
               lSubpSym = ((SymNode)lCallNode.getChild1().getChild1()).
                 getSymNodeSym();
            }
            if (fDbgLevel > 0)
              dbgOut(4, "subpSym " + lSubpSym);
            if (lSubpSym instanceof Subp) {
              String lSubpName = lSubpSym.getName().intern();
              if (lSubpName == "_bnfOr") {
                lRhsOfProduction = lStmt;
                HirList lArgList = (HirList)lCallNode.getChild2();
                fProductionCount = fProductionCount +
                  lArgList.size() - 1; // Parameter 1 is dummy.
              }else if (lSubpName == "_bnfSeq") {
                lRhsOfProduction = lStmt;
                fProductionCount++;
              }else if (lSubpName == "_assignStmt") {
                lRhsOfProduction = lStmt;
              }
            }
          }else if (lStmt instanceof ExpStmt){
            lRhsOfProduction = lStmt;
          }
          if (fDbgLevel > 0)
            dbgOut(4, "\n RhsProduction " + lRhsOfProduction);
          if (lRhsOfProduction != null) {
            if (fProductionMap.containsKey(lSubp)&&
                (fProductionMap.get(lSubp) != null)) {
              // Production appeared again.
              ioRoot.msgWarning.put("Nonterminal " + lSubp.getName() +
                  " has multiple productions without using _bnfOr. Ignore the remainder.");
            }else {
              // First RHS description for this nonterminal.
              fProductionMap.put(lSubp, lRhsOfProduction);
              fOutPatternMap.put(lSubp, lStmt);
              setNonterminalFlag(lStmt);
              if (fDbgLevel > 3) {
                dbgOut(4, "\nproduction " + lSubp.getName() +
                  " " + lRhsOfProduction.toStringWithChildren());
              }
            }
          }
          lStmt = lStmt.getNextStmt();
        } // End of Stmt seq.
      } // End of BlockStmt

    } // End of nonterminal list processing
    if (fDbgLevel > 0) {
      dbgOut(1, "\nproductionMap " + fProductionMap.toString());
      dbgOut(1, "\nFitting symbols " + fFittingSet.toString());
      dbgOut(2, "stmtParamSet", fStmtParamSet.toString()); //##80
    }
  } // GlobalReform

  /**
   * doReform transforms subprograms listed up in
   *   #pragma globalReform target subp1 subp2 ...
   * replacing HIR subtrees matched with some in-pattern
   * treating parameters in in-pattern and out-pattern properly.
   * HIR body of patterns are set to empty block at exit
   * so as to suppress further optimization and code generation
   * for them.
   * @param pReformPatternList is empty at entry and contains
   *     all patterns specified by #pragma globalReform at exit.
   * @return true if HIR is changed by the transformation,
   *     false otherwise.
   */
public boolean
doReform( List pReformPatternList )
{
  boolean lChanged = false;
  fChanged = false;
  if (fSubpToReform.isEmpty() ||
      fPatternList.isEmpty()) {
    // Nothing to be transformed.
    return lChanged;
  }
  // Compute pattern code range for each pattern
  // to do matching quickly.
  dbgOut(2, "\ndoReform");
  computePatternCodeRange();
  for (Iterator lSubpIt = fSubpToReform.iterator();
       lSubpIt.hasNext(); ) {
    // Pick up a subprogram to be transformed.
    Subp lSubp = (Subp)lSubpIt.next();
    SubpDefinition lSubpDef = lSubp.getSubpDefinition();
    if (lSubpDef == null)
      continue;
    dbgOut(2, "Reform", lSubp.getName());
    symRoot.subpCurrent = lSubp;
    symRoot.symTableCurrentSubp = lSubp.getSymTable();
    HIR lSubpBody = lSubpDef.getHirBody();
    if (lSubpBody instanceof LabeledStmt)
      lSubpBody = ((LabeledStmt)lSubpBody).getStmt();
    //---- Try to transform the subprogram body. ----//
    // Set fCurrentStmt, fNextStmt.
    fCurrentStmt  = null;
    fNextStmt     = null;
    fNextStmtCode = 0.0;
    if (lSubpBody instanceof Stmt) {
      fCurrentStmt = (Stmt)lSubpBody;
      if (fCurrentStmt instanceof BlockStmt)
        fNextStmt = ((BlockStmt)fCurrentStmt).getNextStmt();
      else
        fNextStmt = fCurrentStmt.getNextStmt();
    }
    // Try to transform the subprogram body.
    // Make MatchingData specifying neither nonterminal instance
    // nor parent MatchingData.
    MatchingData lData = new MatchingData(null, null);
    HIR lNewBody = tryToReform(lData, lSubpBody);
    if (lNewBody != lSubpBody) {
      lNewBody = hirRoot.hirModify.popoutStmtInExp((Stmt)lNewBody); //##91
      lSubpBody.replaceThisNode(lNewBody);
      lChanged = true;
      fChanged = true;
      dbgOut(2, "Reform has changed", lSubp.getName());
      if (fDbgLevel >= 4) {
        lSubpDef.printHir("Replaced result");
      }
    }
  } // End of SubpToReform iteration
  //-- Make the HIR body of pattern definitions and nonterminal
  //   definitions as empty block so as to suppress code generation for them.
  pReformPatternList.addAll(fPatternList);
  Program lProgram = (Program)hirRoot.programRoot;
  IrList lSubpDefList = lProgram.getSubpDefinitionList();
  for (Iterator lIt2 = lSubpDefList.iterator();
       lIt2.hasNext(); ) {
    SubpDefinition lSubpDef = (SubpDefinition)lIt2.next();
    Subp lPatternSubp = lSubpDef.getSubpSym();
    int lIndex = getIndex(lPatternSubp);
    if (fNontermPat[lIndex] != null) {
      BlockStmt lEmptyBlock = hir.blockStmt(null);
      lSubpDef.setHirBody(lEmptyBlock);
      dbgOut(2, "\nEmpty the HIR body of " + lPatternSubp.getName());
    }
  }
  if (lChanged) {
    ((HIR)hirRoot.programRoot).finishHir();
  }
  return lChanged;
} // doReform

/**
 * Try to transform given HIR subtree pHir.
 * If it matches with some in-pattern, then it is transformed
 * to the form indicated by the out-pattern corresponding to
 * the in-pattern.
 * The matching process is done in 3 steps to speedup
 * the matching.
 * Case 1 - Pattern is one expression or one statement.
 *   step 1: operator of pHir is the same to the operator
 *           of root node of some in-pattern.
 *   step 2: pattern code of pHir is in the range of upper/lower
 *           pattern code of some in-pattern.
 *   step 3: Every nodes in pHir are the same to those
 *           of some in-pattern in the corresponding position
 *           except for parameters.
 * Case 2 - Pattern is a sequence of statements
 *   step 1: operator of the first statement in pHir is the same to
 *           the operator of root node (or 1st statement)
 *           of some in-pattern.
 *   step 2: pattern codes of 1st statement and 2nd statement
 *           starting from pHir are in the range of upper/lower pattern
 *           codes of 1st statement and 2nd statement of some
 *           in-pattern, respectively.
 *   step 3: Every nodes in statement sequence starting from pHir
 *           are the same to those of some in-pattern in the
 *           corresponding position except for parameters.
 * If the matched in-pattern is a sequence of stetements,
 * fTailStmtMatched hold the statement of input statement
 * sequence corresponding to the last statement of the matched
 * in-pattern to show that the next pattern match is to be
 * started from fTailStmtMatched.getNextStmt().
 * @param pHir HIR subtree to be transformed or statement
 *           from which matching is to be started.
 * @return transformed HIR subtree (that may be a block statement
 *     when the transformed result is a sequence of statements).
 */
protected HIR
tryToReform( MatchingData pData, HIR pHir )
{
  boolean lChanged = false;
  HIR lNewHir = pHir;
  Subp lPatternSym = null;
  HIR lPattern;
  if (pHir == null)
    return null;
  dbgOut(2, "tryToReform", pHir.toStringShort());
  int lOpCode = pHir.getOperator();
  if (fUsedAsPatternRoot[lOpCode]) {
    // Operator is the same to the operator
    // of root node of some in-pattern.
    double lPatternCode = patternCode(pHir);
    dbgOut(2, "pattern code " + lPatternCode);
    for (Iterator lPatIt1 = fPatternList.iterator();
         lPatIt1.hasNext(); ) {
      lPatternSym = (Subp)lPatIt1.next();
      int lIndex = getIndex(lPatternSym);
      int lIndexFrom = fCodeIndexFrom[lIndex];
      int lIndexTo   = fCodeIndexTo[lIndex];
      if (fDbgLevel > 1)
        dbgOut(4, "\npattern " + lPatternSym.getName() +
           " range " + fPatternCodeLower[lIndexFrom]
          + " - " +  fPatternCodeUpper[lIndexFrom] + " index "
          + " from " + lIndexFrom + " to " + lIndexTo
          + lIndex + " stmtCount " + fStmtsInPattern[lIndex]);
      boolean lIncluded = false;
      for (int lx1 = lIndexFrom; lx1 < lIndexTo; lx1++) {
        if ((lPatternCode >= fPatternCodeLower[lx1])&&
          (lPatternCode <= fPatternCodeUpper[lx1])) {
          lIncluded = true;
          break;
        }
      }
      lPattern = (HIR)fInPatternMap.get(lPatternSym);
      int lOpCodeP =  lPattern.getOperator();
      if (! lIncluded) {
        if (lPattern.getFlag(HIR.FLAG_NONTERMINAL)) {
          lIncluded = true;
        }else if ((lOpCode == lOpCodeP)&&
            (((lPattern.getChild1() != null)&&
              ((HIR)lPattern.getChild1()).getFlag(HIR.FLAG_NONTERMINAL))||
             ((lPattern.getChild2() != null)&&
              ((HIR)lPattern.getChild2()).getFlag(HIR.FLAG_NONTERMINAL)))) {
             lIncluded = true;
           }
        if (fDbgLevel > 2)
          dbgOut(4, "\n pattern " + lPattern.toStringShort() +
                 " has nonterminal or not: " + lIncluded);
      }
      if (lIncluded) {
        // The pattern code of pHir is in the range of upper/lower
        // pattern code of some subroot of this in-pattern.
        // Advance to detailed matching.
        fCurrentPatternSym = lPatternSym;
        NontermPatInstance lNontermInstance
          = new NontermPatInstance(lPatternSym, 0, null, null);
        // Instance number = 0, call exp = null, parent nontertminal = null.
        fNontermPatInstanceOfCurrentPattern = lNontermInstance;
        MatchingData lData = new MatchingData(lNontermInstance, pData);
        fGlobalPatternParamMap = new HashMap();
        //##86 BEGIN
        for (int i = 0; i <= fNontermPatCount; i++) {
          fInstanceNumberForNonterminals[i] = 0;
        }
        fOriginalVarMap = new HashMap();
        fNontermInstanceInPattern = new HashMap();
        //##86 END
        if (isMatchedPattern(lData,
                             lPattern, pHir, null)) {
          if (fDbgLevel > 1) {
            dbgOut(2, "\nPattern " + lPattern.toStringShort()
             + " matched with " + pHir.toStringShort()
             + "\n Parameter correspondence "
              + dbgMap(pData.paramCorresp)
             //##090506 + " expandedHir " + lData.expandedHir);
            + " expandedHir " + toStringWC(lData.expandedHir)); //##090506
            //##090506 if (lData.expandedHir != null)
            //##090506   dbgOut(3, "\n expandedHir is "
            //##090506     + lData.expandedHir.toStringWithChildren());
          }
          // pHir matched to the in-pattern represented by lPattern.
          // Transform pHir by the pattern.
          lNewHir = transformByPatternSym(lData,
                      lPatternSym, pHir);
          if (lNewHir != pHir)
            fChanged = true;
          pData.reflectCorresp(lData);
          if (fDbgLevel > 1)
            dbgOut(3, "\n result of tryToReform " +
              lNewHir.toStringWithChildren());
          return lNewHir;
        }
      }
      if ((fStmtsInPattern[lIndex] > 1)&&
          (pHir instanceof Stmt)) {
        //-- Try to match as a sequence of statements.
        double lStmt1Code, lStmt2Code;
        // Compare the code with those of statement 1.
        if (fDbgLevel > 1)
          dbgOut(4, "\n multiple Stmt " + fStmtsInPattern[lIndex]
                 + " " + lPatternCode);
        lIndexFrom = fStmt1CodeIndexFrom[lIndex];
        lIndexTo   = fStmt1CodeIndexTo[lIndex];
        lIncluded = false;
        for (int lx1 = lIndexFrom; lx1 < lIndexTo; lx1++) {
          if (fDbgLevel > 3)
            dbgOut(5, " stmt1[" + fPatternCodeLower[lx1]
                   + "-" + fPatternCodeUpper[lx1] + "]");
          if ((lPatternCode >= fPatternCodeLower[lx1])&&
              (lPatternCode <= fPatternCodeUpper[lx1])) {
            lIncluded = true;
            break;
          }
        }
        if (lIncluded) {
          // Statement 1 of in-pattern has matching possibility.
          Stmt lNextStmt = ((Stmt)pHir).getNextStmt();
          if (lNextStmt != null) {
            if (lNextStmt == fNextStmt) {
              if (fNextStmtCode > 0.0) {
                lStmt2Code = fNextStmtCode;
              }
              else {
                lStmt2Code = patternCode(lNextStmt);
                fNextStmtCode = lStmt2Code;
              }
            }
            else {
              lStmt2Code = patternCode(lNextStmt);
              if (fNextStmt == null) {
                fNextStmt = lNextStmt;
                fNextStmtCode = lStmt2Code;
              }
            }
            if (fDbgLevel > 1)
              dbgOut(4, "\n stmt2 " + lNextStmt + " " + lStmt2Code);
            // Compare the code of statement 2.
            lIndexFrom = fStmt2CodeIndexFrom[lIndex];
            lIndexTo   = fStmt2CodeIndexTo[lIndex];
            lIncluded = false;
            for (int lx1 = lIndexFrom; lx1 < lIndexTo; lx1++) {
              if (fDbgLevel > 3)
                dbgOut(5, " stmt2[" + fPatternCodeLower[lx1]
                       + "-" + fPatternCodeUpper[lx1] + "]");
              if ((lStmt2Code >= fPatternCodeLower[lx1])&&
                  (lStmt2Code <= fPatternCodeUpper[lx1])) {
                lIncluded = true;
                break;
              }
            }
            // Statement 2 of in-pattern has matching possibility.
            if (lIncluded) {
              // Try to match statement sequence.
              lPattern = (HIR)fInPatternMap.get(lPatternSym);
              fCurrentPatternSym = lPatternSym;
              Map lParamCorresp2 = new HashMap(); // Map to record
              Map lArrayCorresp2 = new HashMap(); // Map to record
              NontermPatInstance lNontermPatInstance
                = new NontermPatInstance(lPatternSym, 0, null, null);
              // Instance number = 0, call exp = null, parent = null.
              MatchingData lData2 = new MatchingData(lNontermPatInstance, pData);
              if (isMatchedStmtSeq(lData2,
                  lPattern, (Stmt)pHir, lIndex, null)) {
                 // Transform the statement sequence.
                  lNewHir = transformStmtSeq(lData2,
                       lPatternSym, (Stmt)pHir);
                 pData.reflectCorresp(lData2);
              }
            }
          }
        }
        if (lNewHir != pHir) {
          fChanged = true;
          if (fDbgLevel > 1)
            dbgOut(3, "\n result of tryToReform " +
              lNewHir.toStringWithChildren());
          return lNewHir;
        }
      }
    } // End of pattern list
  }else {
    if (fDbgLevel >= 4)
      dbgOut(4, " skip " + pHir.toStringShort());
  }
  if (lOpCode == HIR.OP_BLOCK) {
    Stmt lStmt = ((BlockStmt)pHir).getFirstStmt();
    HIR lNewHir2;
    while (lStmt != null) {
      Stmt lNextStmt = lStmt.getNextStmt(); // Record the next statement
           // before it is replaced.
      Stmt lTailStmtMatched = fTailStmtMatched;
      fTailStmtMatched = null;
      MatchingData lData3 = new MatchingData(pData.nontermPat, pData);
      lNewHir2 = tryToReform(lData3, lStmt);
      if (lNewHir2 != lStmt) {
        if (lNewHir2 instanceof Stmt) {
          lStmt.replaceThisStmtWith((Stmt)lNewHir2);
          lChanged = true;
        }else if (lNewHir2 instanceof Exp) {
          Stmt lNewStmt2 = hir.expStmt((Exp)lNewHir2);
          lStmt.replaceThisStmtWith(lNewStmt2);
          lChanged = true;
        }else {
          ioRoot.msgRecovered.put(5122, "type mismatch in replacing "
            + lStmt.toStringShort() + " with " + lNewHir2.toStringWithChildren());
        }
        if (fTailStmtMatched != null) {
          if (fDbgLevel > 0)
            dbgOut(2, "\nfTailStmtMatched " + fTailStmtMatched
              + " lNextStmt " + lNextStmt);
          // Delete the statement sequence matched with the
          // in-pattern composed of a sequence of statements.
          Stmt lStmtNextToSkip;
          int lStmtCount = fMaxStmtsInPattern;
          while ((lNextStmt != fTailStmtMatched)&&
                 (lNextStmt.getIndex() < fTailStmtMatched.getIndex())&&
                 (lStmtCount > 0)) {
            lStmtNextToSkip = lNextStmt.getNextStmt();
            if (fDbgLevel > 0)
              dbgOut(2, "\nSkip remainder " + lNextStmt);
             lNextStmt.deleteThisStmt();
            lNextStmt = lStmtNextToSkip;
            lStmtCount--;
          }
          if (lNextStmt == fTailStmtMatched) {
            // Delete the last statement matched.
            lStmtNextToSkip = lNextStmt.getNextStmt();
            lNextStmt.deleteThisStmt();
            lNextStmt = lStmtNextToSkip;
            if (fDbgLevel > 0)
              dbgOut(2, "\n advance to " + lNextStmt);
          }else {
            ioRoot.msgRecovered.put("\nTail statment of pattern "
              + lPatternSym + " does not match with input program "
              + lNextStmt );
          }
        }
        fTailStmtMatched = lTailStmtMatched;
      }
      lStmt = lNextStmt;
    }
  }else if (lOpCode == HIR.OP_LIST) {
    HIR lNewHir3;
    for (Iterator lIt3 = ((HirList)pHir).iterator();
         lIt3.hasNext(); ) {
      HIR lHir3 = (HIR)lIt3.next();
      if (lHir3 != null) {
        MatchingData lData4 = new MatchingData(pData.nontermPat, pData);
        lNewHir3 = tryToReform(lData4, lHir3);
        if (lNewHir3 != lHir3) {
          // lHir3.replaceThisNode(lNewHir3);
          replaceHirTree(lHir3, lNewHir3);
          lChanged = true;
        }
      }
    }
  }else {
    HIR lNewHir4;
    for (int lChildIndex = 1; lChildIndex <= pHir.getChildCount();
         lChildIndex++) {
      HIR lChild = (HIR)pHir.getChild(lChildIndex);
      if (lChild != null) {
        MatchingData lData5 = new MatchingData(pData.nontermPat, pData);
        lNewHir4 = tryToReform(lData5, lChild);
        if (lNewHir4 != lChild) {
          // lChild.replaceThisNode(lNewHir4);
          replaceHirTree(lChild, lNewHir4);
          lChanged = true;
        }
      }
    }
  }
  if (lChanged) {
    fChanged = true;
    if (fDbgLevel > 1)
      dbgOut(3, "\n result of tryToReform " +
        pHir.toStringWithChildren());
    return pHir.copyWithOperandsChangingLabels(null);
  }else {
    return pHir;
  }
}  // tryToReform

/**
 * isMatchedPattern discriminates whether HIR subtree pHir
 * matches with the pattern pSubPattern (in-pattern or a
 * part of in-pattern) comparing pHir with pSubPattern.
 * In the process of comparison, a parameter in pSubPattern
 * is treated to match with any HIR expression of the same
 * type or matches with any statement if the parameter is
 * a statement parameter listed up in
 *   #pragma globalReform stmtParam
 * The correspondence of parameter and HIR subtree located
 * in the corresponding position is recorded in
 * pData.paramCorresp or pData.nontermParamCorresp.
 * The input expression/statement corresponding to the
 * pattern is recorded in pData.expandedHir
 * which may contain pattern parameter so that further
 * replacement of the parameter is possible.
 * In C language, array element expression has several
 * variations such as subs-expression or pointer-expression.
 * It makes the process of comparison and the process of
 * replacement of parameters complicated. This problem
 * is handled by
 *   isMatchedSubs (for subscripted variable expression), and
 *   isMatchedArray (for array expression).
 *
 * @param pData matching data.
 * @param pSubPattern in-pattern or part of in-pattern.
 * @param pHir input expression/statement to be compared
 *   with pSubPattern.
 * @param pAncestorNontermPatInstance ancestor instance
 *   of nonterminal or pattern.
 * @return true if pHir matched with pSubPattern
 *   false if no.
 */
protected boolean
isMatchedPattern( MatchingData pData,
    HIR pSubPattern, HIR pHir, //## HIR pAncestorCallNode,
    NontermPatInstance pAncestorNontermPatInstance )
{
  if (fDbgLevel > 1)
      dbgOut(4, "\n isMatchedPattern " + pSubPattern
             + " with " + pHir + " in " + pData.nontermPat
             + " ancestor " + pAncestorNontermPatInstance);
  // Make the new instance lGlobalPatternParamMap to save the
  // contents of fGlobalPatternParamMap.
  // It is used to restore fGlobalPatternParamMap when matching failed.
  Map lGlobalPatternParamMap = new HashMap(fGlobalPatternParamMap);
  if (pSubPattern == null) {
    if (pHir == null) { // Both are null.
      return postProcess(true, lGlobalPatternParamMap, pData);
    }else {
      return postProcess(false, lGlobalPatternParamMap, pData);
    }
  }
    // pSubPattern != null
  if (fDbgLevel > 3) {
    dbgOut(5, " corresp " + dbgMap(pData.paramCorresp)
           + " " + dbgMap(pData.nontermParamCorresp));
  }
  boolean lResult = false;
   if (pSubPattern instanceof SymNode) {
    Sym lSym = ((SymNode)pSubPattern).getSymNodeSym();
    Var lSymInstance = null;
    //##92 BEGIN
    Sym lInputSym = null;
    if (pHir instanceof SymNode) {
      lInputSym = ((SymNode)pHir).getSymNodeSym();
      if (lSym == lInputSym) {
        pData.expandedHir = pHir.copyWithOperands(); //##93
        return postProcess(true, lGlobalPatternParamMap, pData);
      }else if ((lInputSym instanceof Var)&&
            (lSym instanceof Var)&&
            lSym.getFlag(Sym.FLAG_GENERATED_SYM)&&
            (! isFittingSym(lSym))) {
        fFittingSet.add(lSym);
        if (fDbgLevel > 3) {
          dbgOut(5, "\n Treat " + lSym.getName()
             + " as a fitting symbol.");
        }
      }
    }
    //##92 END
    if (lSym instanceof Param) {
      // Treat pHir as matched with the parameter lSym.
      Param lParamInstance = getParamInstance((Param)lSym, pAncestorNontermPatInstance);;
      lResult = recordParamCorresp(pData, lParamInstance, pHir);
      return postProcess(lResult, lGlobalPatternParamMap, pData);
    }else if (lSym == fCurrentPatternSym) {
      // Treat pHir as matched with lSym (recursion).
      lResult = recordParamCorresp(pData, lSym, pHir);
      return postProcess(lResult, lGlobalPatternParamMap, pData);
    //##81 BEGIN
    }else if (isFittingSym(lSym)) {
      // Fitting symbol usually contains no parameters.
      Var lFittingInstance
        = getVarInstance((Var)lSym, pAncestorNontermPatInstance);
      boolean lMatched = false;
      if (pHir.getType().isCompatibleWith(lSym.getSymType())&&
          isTransparent((Var)lSym, pHir, pData)) {
        if (pData.paramCorresp.containsKey(lFittingInstance)) {
          HIR lValue = (HIR)pData.paramCorresp.get(lFittingInstance);
          if (isSameTree(pHir, lValue))
            lMatched = true;
        }else {
          // New correspondence.
          putToMap(pData.paramCorresp, lFittingInstance, pHir);
          lMatched = true;
        }
      }
      if (lMatched) {
        pData.matchedInput = pHir;
        pData.expandedHir = hir.varNode((Var)lFittingInstance);
        return postProcess(true, lGlobalPatternParamMap, pData);
      }else {
        if (fDbgLevel > 0)
          dbgOut(4, "\n Fitting symbol " + lSym.getName()
                 + " does not match with " + pHir);
        return postProcess(false, lGlobalPatternParamMap, pData);
      }
    }else if ((pHir instanceof ConstNode)&&
              (lSym instanceof Var)&&
              (lSym.getSymType().isConst())) {
      // Variable with const qualifier.
      pData.matchedInput = pHir;
      pData.expandedHir = pHir.copyWithOperands();
      Var lConstVar = (Var)lSym;
      if (pAncestorNontermPatInstance.oldToNewParamMap.containsKey(lSym))
        lConstVar = (Var)pAncestorNontermPatInstance.oldToNewParamMap.get(lSym);
      else if (pData.nontermPat.oldToNewParamMap.containsKey(lSym))
        lConstVar = (Var)pData.nontermPat.oldToNewParamMap.get(lSym);
      putToMap(pData.paramCorresp, lConstVar, pHir);
      return postProcess(true, lGlobalPatternParamMap, pData);
    }
    //##81 END
  } // end of SymNode
  if (pHir == null) {
    return postProcess(false, lGlobalPatternParamMap, pData);
  }
  // pHir and pSubPattern are not null.
  boolean lIsMatched;
  boolean lReturn = false;
  int lOpCodeP = pSubPattern.getOperator();
  int lOpCodeH = pHir.getOperator();
  if (lOpCodeP != lOpCodeH) {
    if (pSubPattern.getFlag(HIR.FLAG_NONTERMINAL)) {
      // pSubPattern is either
      //   defined nonterminal or
      //   _bnfOr, _assignStmt, _reform.
      Sym lSubpSym = null;
      HirList lArgList = null;
      if (fDbgLevel > 0)
          dbgOut(4, " NONTERMINAL flag " + pSubPattern);
      if (lOpCodeP == HIR.OP_CALL) {
        if  ((pSubPattern.getChild1().getOperator() == HIR.OP_ADDR)&&
             (pSubPattern.getChild1().getChild1() instanceof SymNode))
          lSubpSym = ((SymNode)pSubPattern.getChild1().getChild1()).getSymNodeSym();
        else if (pSubPattern.getChild1() instanceof SymNode)
          lSubpSym = ((SymNode)pSubPattern.getChild1()).getSymNodeSym();
        if (lSubpSym != null) {
          String lSubpName = lSubpSym.getName().intern();
          lArgList = (HirList)pSubPattern.getChild2();
          if (fDbgLevel > 0)
            dbgOut(4, " subp " + lSubpSym.getName());
          // Make new instance of the nonterminal.
          int lIndex1 = getIndex((Subp)lSubpSym);
          NontermPatInstance lNontermPatInstance =
            new NontermPatInstance((Subp)lSubpSym,
              fInstanceNumberForNonterminals[lIndex1],
              pSubPattern, pData.nontermPat);
          fInstanceNumberForNonterminals[lIndex1]++;
          MatchingData lData = new MatchingData(lNontermPatInstance, pData);
           NontermPatInstance lAncestorNontermPatInstance;
          lAncestorNontermPatInstance = pData.nontermPat;
          if (lAncestorNontermPatInstance == null)
              lAncestorNontermPatInstance
                = fNontermPatInstanceOfCurrentPattern;
          if (isMatchedNonterminal(lData, lNontermPatInstance,
                   pSubPattern, pHir,
                   lAncestorNontermPatInstance)) {
            // Already expanded in isMatchedNonterminal.
            pData.reflect(lData);
            pData.matchedInput = pHir;
            pData.expandedHir = lData.expandedHir;
            if (fDbgLevel > 2) {
              dbgOut(5, " copy expandedHir " + lData.expandedHir +
                     " of " + lSubpSym.getName());
              dbgOut(3, "\n matched input of " + lSubpSym.getName() + " = "
                + pHir.toStringWithChildren());
            }
            return postProcess(true, lGlobalPatternParamMap, pData);
          }else {
            //##86 BEGIN
            // Remove failed records.

            //##86 END
          }
        }
      }
    }else if ((lOpCodeP == HIR.OP_SUBS)||(lOpCodeH == HIR.OP_SUBS)||
        ((pSubPattern.getChild1() != null)&&
         (((HIR)pSubPattern.getChild1()).getType() instanceof VectorType))||
        ((pHir.getChild1() != null)&&
         (((HIR)pHir.getChild1()).getType() instanceof VectorType))||
        ((pSubPattern.getType() instanceof PointerType)&&
         (pHir.getType() instanceof PointerType))) {
      // May be
      //   (subs <var arrayVar> subscriptExp)
      //   (subs (undecay pointerExp elemCount) subscriptExp)
      //   (contents (add pointerExp (mult elemSize subscriptExp)))
      MatchingData lData = new MatchingData(pData.nontermPat, pData);
      lIsMatched = isMatchedSubs(lData,
               pSubPattern, pHir, pAncestorNontermPatInstance);
      // Already expanded in isMatchedSubs.
      if (lIsMatched)
        pData.reflect(lData);
      return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
    }else if (((lOpCodeP == HIR.OP_UNDECAY)&&
               (pHir.getType() instanceof VectorType))||
              ((lOpCodeH == HIR.OP_UNDECAY)&&
              (pSubPattern.getType() instanceof VectorType))){
      // May be
      //   (subs (undecay pointerExp elemCount) subscriptExp)
      MatchingData lData = new MatchingData(pData.nontermPat, pData);
      lIsMatched = isMatchedArray(lData,
              pSubPattern, pHir, pAncestorNontermPatInstance);
      if (lIsMatched)
        pData.reflect(lData);
      lReturn = postProcess(lIsMatched, lGlobalPatternParamMap, pData);
       return lReturn; //##86
    }else if ((lOpCodeP == HIR.OP_CONV)&&
              (((HIR)pSubPattern.getChild1()).getType().getFinalOrigin()
               == pHir.getType().getFinalOrigin())) {
      // May be essentially the same.
      MatchingData lData = new MatchingData(pData.nontermPat, pData);
      lIsMatched = isMatchedPattern(lData,
              (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance);
      if (lIsMatched) {
        pData.reflect(lData);
      }else {
      }
      return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
    }else if ((lOpCodeH == HIR.OP_CONV)&&
              (((HIR)pHir.getChild1()).getType().getFinalOrigin()
               == pSubPattern.getType().getFinalOrigin())) {
      // May be essentially the same.
      MatchingData lData = new MatchingData(pData.nontermPat, pData);
      lIsMatched =  isMatchedPattern(lData,
                pSubPattern, (HIR)pHir.getChild1(), pAncestorNontermPatInstance);
      if (lIsMatched) {
        pData.reflect(lData);
      }else {
      }
      return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
      //##80 BEGIN
    }else if ((lOpCodeP == HIR.OP_EXP_STMT)&&
              (pSubPattern.getChild1() instanceof VarNode)&&
              (((VarNode)pSubPattern.getChild1()).getSymNodeSym() instanceof Param)&&
              (pHir instanceof Stmt)) {
        // Statement parameter matches to the statement.
        Sym lParam = ((VarNode)pSubPattern.getChild1()).getSymNodeSym();
        lReturn = recordParamCorresp(pData, lParam, pHir);
        if (! lReturn) {
        }
        return postProcess(lReturn, lGlobalPatternParamMap, pData);
      }else  if ((lOpCodeH == HIR.OP_EXP_STMT)&&
                (pSubPattern instanceof Exp)) {
        // Expression matching
        MatchingData lData = new MatchingData(pData.nontermPat, pData);
        lIsMatched = isMatchedPattern(lData,
                 pSubPattern, (HIR)pHir.getChild1(), pAncestorNontermPatInstance);
        if (lIsMatched) {
          pData.reflect(lData);
        }else {
        }
        return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
      }else  if ((lOpCodeP == HIR.OP_EXP_STMT)&&
                 (pHir instanceof Exp)) {
         // Expression matching
         MatchingData lData = new MatchingData(pData.nontermPat, pData);
         lIsMatched = isMatchedPattern(lData,
             (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance);
         if (lIsMatched) {
           pData.reflect(lData);
         }
         return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
       }else  if ((lOpCodeP == HIR.OP_EXP_STMT)&&
                  (pSubPattern.getChild1().getOperator() == HIR.OP_CALL)&&
                  (pHir instanceof AssignStmt)) {
         MatchingData lData = new MatchingData(pData.nontermPat, pData);
         lIsMatched = isMatchedPattern(lData,
                  (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance);
         if (lIsMatched) {
           pData.reflect(lData);
         }
         return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
      }else if (((lOpCodeH >= HIR.OP_CMP_EQ)&&(lOpCodeH <= HIR.OP_CMP_LE))||
                ((lOpCodeP >= HIR.OP_CMP_EQ)&&(lOpCodeP <= HIR.OP_CMP_LE))) {
        // Comparison expression having different operation code.
        MatchingData lData = new MatchingData(pData.nontermPat, pData);
        lIsMatched = isMatchedCondition(lData,
          pSubPattern, pHir, pAncestorNontermPatInstance);
        if (lIsMatched) {
          pData.reflect(lData);
        }
        return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
      //##80 END
    }
    return postProcess(false, lGlobalPatternParamMap, pData);
  } // End of if(lOpCodeP != lOpCodeH)
  if (! lReturn) {
  // Both have the same operation code
  // and matching has not yet succeeded.
  if (pSubPattern instanceof SymNode) {
    Sym lSym11 = ((SymNode)pSubPattern).getSymNodeSym();
    if (lSym11 == ((SymNode)pHir).getSym()) {
      lIsMatched = true;
      pData.matchedInput = pHir;
      if (lSym11 instanceof Var)
        pData.expandedHir = traceParamCorresp(pData, (Var)lSym11, pHir, false); //##
      else
        pData.expandedHir = pSubPattern.copyWithOperands();
    }else {
      lIsMatched = false;
    }
    return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
  }else if (pSubPattern instanceof BlockStmt) {
    MatchingData lData1 = new MatchingData(pData.nontermPat, pData);
    Stmt lStmt1 = ((BlockStmt)pSubPattern).getFirstStmt();
    Stmt lStmt2 = ((BlockStmt)pHir).getFirstStmt();
    BlockStmt lNewBlock = hir.blockStmt(null);
    while (lStmt1 != null) {
      MatchingData lData1_1 = new MatchingData(lData1.nontermPat, lData1);
      lData1_1.reflectCorresp(lData1);
      if (! isMatchedPattern(lData1_1,
              lStmt1, lStmt2, pAncestorNontermPatInstance)) {
        return postProcess(false, lGlobalPatternParamMap, pData);
      }
      lData1.reflectCorresp(lData1_1);
      HIR lExpandedHir = lData1_1.expandedHir;
      if (lExpandedHir instanceof Exp)
        lExpandedHir = hir.expStmt((Exp)lExpandedHir);
      lNewBlock.addLastStmt((Stmt)lExpandedHir);
      lStmt1 = lStmt1.getNextStmt();
      if (lStmt2 == null) {
        if (fDbgLevel > 2)
          dbgOut(4, "\n Block " + pHir + " is short.");
        return postProcess(false, lGlobalPatternParamMap, pData);
      }
      lStmt2 = lStmt2.getNextStmt();
    }
    if (lStmt2 == null) {
      // All statements have matched.
      lIsMatched = true;
      pData.reflectCorresp(lData1);
      pData.matchedInput = pHir;
      pData.expandedHir = lNewBlock;
    }else {
      if (fDbgLevel > 2)
        dbgOut(4, "\n There remains some statement in " + pHir);
      lIsMatched =  false;
    }
    return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
  }else if (pSubPattern instanceof HirList) {
    MatchingData lData2 = new MatchingData(pData.nontermPat, pData);
    Iterator lItP = ((HirList)pSubPattern).iterator();
    Iterator lItH = ((HirList)pHir).iterator();
    HirList lNewList = hir.hirList();
    while (lItP.hasNext()) {
      HIR lElemP = (HIR)lItP.next();
      HIR lElemH = null;
      if (lItH.hasNext())
        lElemH = (HIR)lItH.next();
      MatchingData lData2_1 = new MatchingData(pData.nontermPat, pData);
      lData2_1.reflectParamCorresp(lData2); //##81
      if (! isMatchedPattern(lData2_1,
               lElemP, lElemH, pAncestorNontermPatInstance)) {
        return postProcess(false, lGlobalPatternParamMap, pData);
      }else {
        lNewList.add(lData2_1.expandedHir);
        lData2.reflectCorresp(lData2_1);
      }
    }
    if (lItH.hasNext()) {
      // There remains some elements in pHir.
      lIsMatched = false;
    }else {
      // All elements have matched.
      lIsMatched = true;
      pData.reflectCorresp(lData2);
      pData.matchedInput = pHir;
      pData.expandedHir = lNewList;
    }
    return postProcess(lIsMatched, lGlobalPatternParamMap, pData);
  }else if (pSubPattern instanceof LabeledStmt) {
    MatchingData lData3 = new MatchingData(pData.nontermPat, pData);
    if (isMatchedPattern(lData3,
           (HIR)pSubPattern.getChild2(),
           (HIR)pHir.getChild2(), pAncestorNontermPatInstance)) {
       HIR lNewHir = pHir.copyWithOperandsChangingLabels(null); // REFINE
       if (pSubPattern.getChild2() != null) {
         ((HIR)lNewHir.getChild2()).replaceThisNode(
           hirCopyWithOperandsChangingLabels(lData3.expandedHir));//##93
       }
       pData.reflectCorresp(lData3);
       pData.matchedInput = pHir;
       pData.expandedHir = lNewHir;
       return postProcess(true, lGlobalPatternParamMap, pData);
     }else {
       return postProcess(false, lGlobalPatternParamMap, pData);
     }
    }
  } // End of ! lReturn
  // Check children.
  HIR lNewHir;
  if ((pHir instanceof LoopStmt)||(pHir instanceof SwitchStmt))
    lNewHir = pHir.copyWithOperands();
  else
    lNewHir = ((HIR_Impl)pHir).hirNodeClone();
  MatchingData lData4 = new MatchingData(pData.nontermPat, pData);
  //##91 BEGIN
  if ((lOpCodeP == HIR.OP_CALL)&&
      (pSubPattern.getChild1() instanceof VarNode)&&
      (pHir.getChild1() != null)) {
    // Call with function parameter.
    // Get the parameter correspondence.
    Var lSubpParam = (Var)((VarNode)pSubPattern.getChild1()).getSymNodeSym();
    HIR lChildNode1 = (HIR)pHir.getChild1();
    if (lChildNode1.getOperator() == HIR.OP_ADDR)
      lChildNode1 = (HIR)lChildNode1.getChild1();
    if (lChildNode1 instanceof SymNode) {
      lData4.paramCorresp.put(lSubpParam, lChildNode1.copyWithOperands());
    }
    //Check parameter list.
    HIR lArgPattern = (HIR)pSubPattern.getChild2();
    HIR lArgList    = (HIR)pHir.getChild2();
    lNewHir.setChild1(((HIR)pHir.getChild1()).copyWithOperands());
    if (!isMatchedPattern(lData4, lArgPattern, lArgList,
      pAncestorNontermPatInstance)) {
      return postProcess(false, lGlobalPatternParamMap, pData);
    }
    lNewHir.setChild2(lArgList.copyWithOperands());
  }else {
  //##91 END
    for (int lChildNum = 1; lChildNum <= pSubPattern.getChildCount();
         lChildNum++) {
      HIR lChildP = (HIR)pSubPattern.getChild(lChildNum);
      HIR lChildH = (HIR)pHir.getChild(lChildNum);
      if (fDbgLevel > 3)
        dbgOut(6, "\n  Child" + lChildNum + " " + lChildP + " " + lChildH);
      MatchingData lData4_1 = new MatchingData(pData.nontermPat, pData);
      lData4_1.reflectParamCorresp(lData4); //##81
      if (!isMatchedPattern(lData4_1, lChildP, lChildH,
        pAncestorNontermPatInstance)) {
        return postProcess(false, lGlobalPatternParamMap, pData);
      }
      if ((lData4_1.expandedHir != null) &&
          (lData4_1.expandedHir.getOperator() == HIR.OP_EXP_STMT) &&
          (pHir.getChild(lChildNum)instanceof Exp))
        lNewHir.setChild(lChildNum, lData4_1.expandedHir.getChild1());
      else
        lNewHir.setChild(lChildNum, lData4_1.expandedHir);
      lData4.reflectCorresp(lData4_1);
    }
  } //##91
  // All children have matched.
  pData.reflectCorresp(lData4);
  pData.matchedInput = pHir;
  pData.expandedHir = lNewHir;
  return postProcess(true, lGlobalPatternParamMap, pData);
} // isMatchedPattern

/**
 * If matching failed, then restore fGlobalPatternParamMap
 * by their previous value pGlobalPatternParamMap.
 * If matching succeeded, then leave fGlobalPatternParamMap
 * unchanged (in the updated status).
 * @param pMatched true if the matching succeeded, otherwise false.
 * @param pGlobalPatternParamMap Previous pattern param mapping.
  * @return pMatched.
 */
boolean
postProcess( boolean pMatched, Map pGlobalPatternParamMap,
               MatchingData pData)
{
  if (pMatched) {
    pData.succeeded = true;
  }else {
    fGlobalPatternParamMap = pGlobalPatternParamMap; // Restore
    pData.succeeded = false;
  }
  if (fDbgLevel > 2) {
    dbgOut(5, "\n isMatchedPattern return " + pMatched
           + " " + pData.nontermPat);
    if (pMatched)
      dbgOut(6, " " + dbgMap(pData.paramCorresp)
             + dbgMap(pData.nontermParamCorresp));
  }
  return pMatched;
} // postProcess

/**
 * isMatchedSubs discriminates whether HIR subtree pHir
 * matches with the pattern pSubPattern (in-pattern or
 * part of in-pattern) comparing pHir with pSubPattern
 * where pHir or pSubPattern may be an expression
 * representing an array element.
 * In HIR corresponding to C language, array element may
 * be represented as one of
 *   (subs <var arrayVar> subscriptExp)
 *   (subs (undecay pointerExp elemCount) subscriptExp)
 *   (contents (add pointerExp (mult elemSize subscriptExp)))
 * and each combination of them should be considered
 * in the comparison. isMatchedSubs does such comparison
 * and record the correspondence of parameter in pSubPattern
 * and the expression in pHir.
 * @param pData matching data.
 * @param pSubPattern in-pattern or its subexpression.
 * @param pHir part of input program to be matched.
 * @param pAncestorNontermPatInstance ancestor instance
 *   of nonterminal or pattern.
 * @return true if pHir matched with pSubPattern.
 */
protected boolean
isMatchedSubs( MatchingData pData,
               HIR pSubPattern, HIR pHir, //## HIR pAncestorCallNode,
               NontermPatInstance pAncestorNontermPatInstance )
{
  if (fDbgLevel > 1)
    dbgOut(4, "\n  isMatchedSubs " + pSubPattern.toStringShort()
      + " with " + pHir + " in " + pData.nontermPat
      + " ancestorNonterm " + pAncestorNontermPatInstance);
  // Array element may be represented by one of
  //   (subs <var arrayVar> subscriptExp)
  //   (subs (undecay pointerExp elemCount) subscriptExp)
  //   (contents (add pointerExp (mult elemSize subscriptExp)))
  MatchingData lData = new MatchingData(pData.nontermPat, pData);
  int lOpCodeP = pSubPattern.getOperator();
  int lOpCodeH = pHir.getOperator();
  HIR lChildP1 = (HIR)pSubPattern.getChild1();
  HIR lChildH1 = (HIR)pHir.getChild1();
  if ((lOpCodeP != HIR.OP_SUBS)&&(lOpCodeH == HIR.OP_SUBS)) {
    if ((lOpCodeP == HIR.OP_CONTENTS)&&
        (lChildP1.getOperator() == HIR.OP_ADD)&&
        (lChildP1.getChild2().getOperator() == HIR.OP_MULT)) {
      // HIR lResult = (HIR)pHir.copyWithOperands(); // REFINE
      HIR lResult = pSubPattern.copyWithOperands();
      if (isMatchedArray(lData,
            (HIR)lChildP1.getChild1(), lChildH1, pAncestorNontermPatInstance)) {
        replaceHirTree((HIR)lResult.getChild1().getChild1(), // operand1 of add (array var)
          lData.expandedHir.copyWithOperands()); // array variable
        MatchingData lData2 = new MatchingData(lData.nontermPat, pData);
        if (isMatchedPattern(lData2,
           (HIR)lChildP1.getChild2().getChild2(), // operand 2 of mult.
          (HIR)pHir.getChild2(), pAncestorNontermPatInstance)) { // operand 2 of subs.
          lData.reflect(lData2);
          replaceHirTree((HIR)lResult.getChild1().getChild2().getChild2(), // operand 2 of mult
            // lData2.expandedHir.copyWithOperands()); // operand 2 of subs
            lData.expandedHir.copyWithOperands()); // operand 2 of subs //##090505
          lData.matchedInput = pHir;
          lData.expandedHir = lResult;
          pData.reflect(lData);
          pData.matchedInput = pHir;
          pData.expandedHir = lResult;
          if (fDbgLevel > 2)
            dbgOut(4, "\n expandedHir of isMatchedSubs for " + pSubPattern.toStringShort()
          + ": " + toStringWC(pData.expandedHir)); //##93
          return true;
        }
      }
    }
    return false;
  }else if ((lOpCodeP == HIR.OP_SUBS)&&(lOpCodeH != HIR.OP_SUBS)) {
    if ((lOpCodeH == HIR.OP_CONTENTS)&&
        (lChildH1.getOperator() == HIR.OP_ADD)&&
        (lChildH1.getChild2().getOperator() == HIR.OP_MULT)) {
      if (isMatchedArray(lData,
            lChildP1, (HIR)lChildH1.getChild1(), pAncestorNontermPatInstance)) {
        MatchingData lData_1 = new MatchingData(lData.nontermPat, lData);
        if ( isMatchedPattern(lData_1,
              (HIR)pSubPattern.getChild2(),
              (HIR)lChildH1.getChild2().getChild2(), pAncestorNontermPatInstance)) {
          HIR lNewHir = pHir.copyWithOperands();
          ((HIR)lNewHir.getChild1()).setChild1(lData.expandedHir.copyWithOperands());
          ((HIR)((HIR)lNewHir.getChild1()).getChild2()).setChild1(lData_1.expandedHir.copyWithOperands());
          pData.reflectCorresp(lData);
          pData.reflectCorresp(lData_1);
          pData.matchedInput = pHir;
          pData.expandedHir = lNewHir;
          if (fDbgLevel > 2)
            dbgOut(4, "\n expandedHir of isMatchedSubs for " + pSubPattern.toStringShort()
              + ": " + lNewHir.toStringWithChildren());
          return true;
        }
      }
    }
    return false;
  }else if((lOpCodeP == HIR.OP_SUBS)&&(lOpCodeH == HIR.OP_SUBS)) {
    if (isMatchedArray(lData, lChildP1, lChildH1, pAncestorNontermPatInstance)) {
      MatchingData lData_2 = new MatchingData(pData.nontermPat, pData);
      if (isMatchedPattern(lData_2,
          (HIR)pSubPattern.getChild2(),
          //##91 (HIR)lChildH1.getChild2(),
          (HIR)pHir.getChild2(), //##91
          pAncestorNontermPatInstance)) {
        pData.reflectCorresp(lData);
        pData.reflectCorresp(lData_2);
        HIR lNewHir = pHir.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        lNewHir.setChild2(lData_2.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        if (fDbgLevel > 2)
          dbgOut(4, "\n expandedHir of isMatchedSubs for " + pSubPattern.toStringShort()
            + ": " + lNewHir.toStringWithChildren());
        return true;
      }
    }
  }else if ((pSubPattern.getType() instanceof PointerType)&&
            (pHir.getType() instanceof PointerType)) {
    if ((lOpCodeP == HIR.OP_DECAY)||
        (lOpCodeP == HIR.OP_ADDR)) {
      if (isMatchedArray(lData,
              (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance)) {
        pData.reflectCorresp(lData);
        pData.matchedInput = pHir;
        pData.expandedHir = lData.expandedHir;
        if (fDbgLevel > 2)
          dbgOut(4, "\n expandedHir of isMatchedSubs for " + pSubPattern.toStringShort()
            + ": " + toStringWC(lData.expandedHir)); //##93
        return true;
      }
    }else if ((lOpCodeH == HIR.OP_DECAY)||
                (lOpCodeH == HIR.OP_ADDR)) {
      if(isMatchedArray(lData,
          pSubPattern, (HIR)pHir.getChild1(), pAncestorNontermPatInstance)) {
        pData.reflectCorresp(lData);
        HIR lNewHir = pHir.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        if (fDbgLevel > 2)
          dbgOut(4, "\n expandedHir of isMatchedSubs for " + pSubPattern.toStringShort()
            + ": " + lNewHir.toStringWithChildren());
        return true;
      }
    }
  }
  return false;
} // isMatchedSubs

/**
 * isMatchedArray discriminates whether HIR subtree pHir
 * matches with the pattern pSubPattern (in-pattern or
 * part of in-pattern) comparing pHir with pSubPattern
 * where pHir or pSubPattern may be an expression
 * representing an array (vector) or array element.
 * In HIR corresponding to C language, array and
 * array element may be represented as one of
 *   (subs <var varOfArrayType> subscriptExp)
 *   (contents (add <varOfPointerType> (mult elemSize subscriptExp)))
 *   (undecay pointerExp elemCount)
 *   <var varOfArrayType>
 *   <var varOfPointerType>
 *   (decay arrayExp)
 *   (addr variableExp)
 *   (subs <var arrayVar> subscriptExp)
 *   (subs (undecay pointerExp elemCount) subscriptExp)
 *   (contents (add pointerExp (mult elemSize subscriptExp)))
 * and each combination of them should be considered
 * in the comparison.
 * @param pData matching data.
 * @param pSubPattern in-pattern or its subexpression.
 * @param pHir part of input program to be matched.
 * @param pAncestorNontermPatInstance ancestor instance
 *   of nonterminal or pattern.
 * @return true if pHir matched with pSubPattern.
 */
protected boolean
isMatchedArray( MatchingData pData,
                HIR pSubPattern, HIR pHir, //## HIR pAncestorCallNode,
                NontermPatInstance pAncestorNontermPatInstance )
{
  // pSubPattern and pHir may be one of
  //   (subs <var varOfArrayType> subscriptExp)
  //   (contents (add <varOfPointerType> (mult elemSize subscriptExp)))
  //   (undecay pointerExp elemCount)
  //   <var varOfArrayType>
  //   <var varOfPointerType>
  //   (decay arrayExp)
  //   (addr variableExp)
  if (fDbgLevel > 1)
    dbgOut(4, "\n  isMatchedArray " +
      pSubPattern.toStringShort() + " " + pHir.toStringShort()
      + " ancestor " + pAncestorNontermPatInstance);
  MatchingData lData = new MatchingData(pData.nontermPat, pData);
  int lOpCodeP = pSubPattern.getOperator();
  int lOpCodeH = pHir.getOperator();
  Type lTypeP = pSubPattern.getType();
  Type lTypeH = pHir.getType();
  boolean lResult;
  if ((pSubPattern instanceof SymNode)&&
      ((((SymNode)pSubPattern).getSymNodeSym() instanceof Param)||
       isFittingSym(((SymNode)pSubPattern).getSymNodeSym()))) {
    Sym lParamSym = ((SymNode)pSubPattern).getSymNodeSym();
    if ((! (lTypeP instanceof VectorType)&&
        (! (lTypeP instanceof PointerType)))) {
      ioRoot.msgRecovered.put(5212, "Illegal array type for " +
        lParamSym.getName() + " in " +
          pSubPattern.toStringShort() + " "
          +lTypeP.toStringShort());
      return false;
    }
    lResult = recordParamCorresp(lData, lParamSym, pHir);
    if (lResult) {
      pData.reflectCorresp(lData);
      pData.matchedInput = pHir;
      pData.expandedHir = traceParamCorresp(pData, (Var)lParamSym, pHir, false); //##
    }
    return lResult;
  } // end of SymNode of Param Sym
  else if ((lTypeP instanceof PointerType)&&
            (lTypeH instanceof PointerType)) {
    if ((lOpCodeP == HIR.OP_DECAY)||
        (lOpCodeP == HIR.OP_ADDR)) {
      if (isMatchedArray(lData,
             (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pSubPattern.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }else if ((lOpCodeH == HIR.OP_DECAY)||
              (lOpCodeH == HIR.OP_ADDR)) {
      if ( isMatchedArray(lData,
               pSubPattern, (HIR)pHir.getChild1(), pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pHir.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }
    if (((PointerType)lTypeP).getPointedType() !=
        ((PointerType)lTypeH).getPointedType()) {
      dbgOut(4, "Array type mismatch " +
        pSubPattern.toStringShort() + " " +lTypeP.toStringShort() +
        pHir.toStringShort() + " " +lTypeH.toStringShort());
      return false;
    }
    //##91 recordParamCorresp is required to be called, is't it ?
    pData.matchedInput = pHir;
    pData.expandedHir = pSubPattern.copyWithOperandsChangingLabels(null); //## ?
    return true ;
  }else if ((lTypeP instanceof VectorType)&&
            (lTypeH instanceof VectorType)) {
    if (lOpCodeP == HIR.OP_UNDECAY) {
      if (isMatchedArray(lData,
                  (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pSubPattern.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }
    if (((VectorType)lTypeP).getElemType() !=
        ((VectorType)lTypeH).getElemType()) {
      dbgOut(3, "Element type mismatch " +
        pSubPattern.toStringShort() + " " +lTypeP.toStringShort() +
        pHir.toStringShort() + " " +lTypeH.toStringShort());
      return false;
    }
    if ((lOpCodeP == HIR.OP_CONTENTS)&&
        (lOpCodeH == HIR.OP_SUBS)) { // Multiple dimension
      if (isMatchedSubs(lData, pSubPattern, pHir, pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }else if ((lOpCodeP == HIR.OP_SUBS)&&
              (lOpCodeH == HIR.OP_CONTENTS)) { // Multiple dimension
      if (isMatchedSubs(lData, pSubPattern, pHir, pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }
    //##91 BEGIN
    if ((lOpCodeP == HIR.OP_SUBS)||
        (lOpCodeH == HIR.OP_SUBS)) {
      if (isMatchedSubs(lData, pSubPattern, pHir, pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        return true;
      }
    }
    //##91 END
    //##91 recordParamCorresp is required to be called, is't it ?
    pData.matchedInput = pHir;
    pData.expandedHir = pSubPattern.copyWithOperandsChangingLabels(null); //## ?
    return true;
  }else if ((lTypeP instanceof PointerType)&&
            (lTypeH instanceof VectorType)) {
    if ((lOpCodeP == HIR.OP_DECAY)||
        (lOpCodeP == HIR.OP_ADDR)) {
      if (isMatchedArray(lData,
                 (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pSubPattern.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }else if (lOpCodeH == HIR.OP_UNDECAY) {
      if (isMatchedArray(lData,
                        pSubPattern, (HIR)pHir.getChild1(),
                        pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pHir.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }else {
      // Here, following conditions hold:
      //   ((!(pSubPattern instanceof SymNode))||
      //    (!((SymNode)pSubPattern).getSymNodeSym() instanceof Param)) &&
      //   (lTypeP instanceof PointerType)&&(lTypeH instanceof VectorType) &&
      //   ((lOpCodeP != HIR.OP_DECAY)&&(lOpCodeP != HIR.OP_ADDR))
      //   lData is new MatchingData(pData.nontermPat, pData);
      if (((VectorType)lTypeH).getElemType() ==
          ((PointerType)lTypeP).getPointedType()) {
        if (fDbgLevel > 2)
          dbgOut(6, " ((VectorType)lTypeH).getElemType() =="
                   + " ((PointerType)lTypeP).getPointedType() ");
        pData.reflect(lData); //## ?
        pData.matchedInput = pHir;
        pData.expandedHir = pSubPattern.copyWithOperands(); //## ?
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }
  }else if ((lTypeP instanceof VectorType)&&
            (lTypeH instanceof PointerType)) {
    if (lOpCodeP == HIR.OP_UNDECAY) {
      if (isMatchedArray(lData,
               (HIR)pSubPattern.getChild1(), pHir,
               pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pSubPattern.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }else if ((lOpCodeH == HIR.OP_DECAY)||
              (lOpCodeH == HIR.OP_ADDR)) {
      if (isMatchedArray(lData,
               pSubPattern, (HIR)pHir.getChild1(),
               pAncestorNontermPatInstance)) {
        pData.reflect(lData);
        HIR lNewHir = pHir.hirNodeClone();
        lNewHir.setChild1(lData.expandedHir.copyWithOperands());
        pData.matchedInput = pHir;
        pData.expandedHir = lNewHir;
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }else {
      if (((VectorType)lTypeP).getElemType() ==
          ((PointerType)lTypeH).getPointedType()) {
        if (fDbgLevel > 2)
          dbgOut(6, " ((VectorType)lTypeP).getElemType() =="
                   + " ((PointerType)lTypeH).getPointedType() ");
        pData.matchedInput = pHir;
        pData.expandedHir = pSubPattern.copyWithOperandsChangingLabels(null); //??
        return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
    }
  }else {
    // Check children.
    HIR lNewHir = pHir.hirNodeClone();
    for (int lChildNum = 1; lChildNum <= pSubPattern.getChildCount();
         lChildNum++) {
      MatchingData lData3 = new MatchingData(pData.nontermPat, pData);
      if (! isMatchedPattern(lData3,
              (HIR)pSubPattern.getChild(lChildNum),
              (HIR)pHir.getChild(lChildNum), pAncestorNontermPatInstance)) {
        if (fDbgLevel > 2)
          dbgOut(6, " isMatchedArray failed ");
        return false;
      }
      if ((lData3.expandedHir != null)&&
          (lData3.expandedHir.getOperator() == HIR.OP_EXP_STMT)&&
          (pHir.getChild(lChildNum) instanceof Exp))
        lNewHir.setChild(lChildNum,
          ((HIR)lData3.expandedHir.getChild1()).copyWithOperands());
      else
        lNewHir.setChild(lChildNum,
          lData3.expandedHir.copyWithOperandsChangingLabels(null));
      lData.reflectCorresp(lData3);
    }
    // All children have matched.
    pData.reflectCorresp(lData);
    pData.matchedInput = pHir;
    pData.expandedHir = lNewHir;
    return true;
  }
} // isMatchedArray

  /**
   * isMatchedStmtSeq discriminates whether HIR subtree
   * matches with the pattern pSubPattern (in-pattern or
   * part of in-pattern) that is a sequence of statements
   * comparing the statement sequence starting with pStmt.
   * @param pData Maching data.
   * @param pSubPattern Pattern taking the form of a sequence
   *   of statements.
   * @param pStmt Statement from which comparison should
   *   take place.
   * @param pPatternIndex Index assigned to the pattern
   *   (used to the number of statements in the pattern).
   * @param pAncestorNontermPatInstance ancestor instance
   *   of nonterminal or pattern.
   * @return true if matched, false otherwise.
   */
protected boolean
isMatchedStmtSeq( MatchingData pData,
    HIR pSubPattern, Stmt pStmt, int pPatternIndex,
    NontermPatInstance pAncestorNontermPatInstance )
{
  int lStmtCount = fStmtsInPattern[pPatternIndex];
  if (fDbgLevel > 1)
    dbgOut(3, "\nisMatchedStmtSeq " + pSubPattern.toStringShort()
           + " with " + pStmt.toStringShort() +
           " index " + pPatternIndex + " stmtCount " + lStmtCount
           + " ancestor " + pAncestorNontermPatInstance);
  Stmt lStmt, lStmtInPattern;
  lStmt = pStmt;
  if (pSubPattern instanceof BlockStmt) {
    BlockStmt lNewBlock = hir.blockStmt(null);
    lStmtInPattern = ((BlockStmt)pSubPattern).getFirstStmt();
    MatchingData lData = new MatchingData(pData.nontermPat, pData);
    for (int lx = 0; lx < lStmtCount; lx++) {
      if (fDbgLevel > 1)
        dbgOut(3, "\n stmt " + lx + " " + lStmt);
      if (lStmt == null)
        return false;
      MatchingData lData_1 = new MatchingData(lData.nontermPat, lData);
      if (! isMatchedPattern(lData_1,
                lStmtInPattern, lStmt, pAncestorNontermPatInstance)) {
        return false;
      }
      lNewBlock.addLastStmt((Stmt)lData_1.expandedHir);
      lData.reflectCorresp(lData_1);
      lStmtInPattern = lStmtInPattern.getNextStmt();
      lStmt = lStmt.getNextStmt();
    }
    // All statements have matched.
    pData.reflect(lData);
    pData.matchedInput = pStmt; //??
    pData.expandedHir = lNewBlock;
    if (fDbgLevel > 2)
      dbgOut(4, "\n expandedHir of matchedInputStmtSeq "
             + lNewBlock.toStringWithChildren());
    return true;
  }else {
    return false;
  }
} // isMatchedStmtSeq

//##80 BEGIN
/**
 * isMatchedCondition: Check comparison expression having different
 * operation code, where either pSubPattern or pHir have
 * comparison operator.
 * @param pData matching data.
 * @param pSubPattern in-pattern or part of in-pattern.
 * @param pHir input expression/statement to be compared
 *   with pSubPattern.
 * @param pAncestorNontermPatInstance ancestor instance
 *   of nonterminal or pattern.
 * @return true if pHir matched with pSubPattern
 *   false if no.
 */
protected boolean
isMatchedCondition( MatchingData pData,
    HIR pSubPattern, HIR pHir, //## HIR pAncestorCallNode,
    NontermPatInstance pAncestorNontermPatInstance )
{
  // Both of pSubPattern and pHir are not null.
  int lOpCodeP = pSubPattern.getOperator();
  int lOpCodeH = pHir.getOperator();
  boolean lResult;
  if ((lOpCodeP == HIR.OP_CMP_NE)&&
      (pSubPattern.getChild2() instanceof ConstNode)&&
      (((ConstNode)pSubPattern.getChild2()).getIntValue() == 0)) {
    if ((lOpCodeH >= HIR.OP_CMP_EQ)&&
        (lOpCodeH <= HIR.OP_CMP_LE)) {
      if ((pSubPattern.getChild1() instanceof SymNode)&&
          (((SymNode)pSubPattern.getChild1()).getSymNodeSym()
            instanceof Param)) {
        // Condition parameter and comparison expressin.
        Sym lParam = ((SymNode)pSubPattern.getChild1()).getSymNodeSym();
        lResult = recordParamCorresp(pData, lParam, pHir);
        return lResult;
      }else {
        // exp1 < exp2  matches with (exp1 < exp2) != 0
        MatchingData lData = new MatchingData(pData.nontermPat, pData);
          if (isMatchedPattern(lData,
            (HIR)pSubPattern.getChild1(), pHir, pAncestorNontermPatInstance)) {
            pData.reflect(lData);
            return true;
          }else
            return false;
      }
    } //(lOpCodeH >= HIR.OP_CMP_EQ)&&(lOpCodeH <= HIR.OP_CMP_LE)
  } // End for pSubPattern: conditionExp != 0
  return false;
} // isMatchedCondition

//##80 END

/**
 * isMatchedNonterminal discriminates whether the nonterminal
 * (pMetaSym) refered by pNonterminalCall matches with
 * the input HIR tree pHir.
 * A new instance of the nonterminal is made and all of its
 * parameters are also instanciated so that the same nonterminal
 * refered in the same form of call expression may match with
 * different HIR if its instance differs.
 * Similarly, the same parameter may have different actual
 * parameter (matched input) if they have multiple instances.
 * The input expression/statement corresponding to the
 * pattern is recorded in pData.expandedHir
 * which may contain pattern parameter so that further
 * replacement of the parameter is possible.
 * @param pData MatchingData to be refered and updated
 *            in the matching.
 * @param pNontermInstance Nonterminal instanciated for this
 *        call (pNontermCall) which may be defined nonterminal
 *        or meta symbol (_bnfOr, _bnfSeq, _reform).
 * @param pNontermCall Expression calling pMetaSym.
 * @param pHir Input HIR to be matched with the nonterminal.
 * @param pAncestorCallNode
 * @param pAncestorNontermPatInstance Nonterminal instance
 *        in which body the current nonterminal is instanciated
 *        (by pNontermCall).
 * @return true if matched nonterminal.
 */
protected boolean
isMatchedNonterminal( MatchingData pData,
    NontermPatInstance pNontermInstance, HIR pNontermCall, HIR pHir,
    NontermPatInstance pAncestorNontermPatInstance)
{
  Subp lNontermSym = pNontermInstance.nontermPat;
  // Save correspondence maps.
  int lNontermIndex = getIndex(lNontermSym);
  String lMetaSymName = lNontermSym.getName().intern();
  if (fDbgLevel > 1) {
    dbgOut(2, "\nisMatchedNonterminal entry "
           + lMetaSymName + " by " + pNontermCall
           + " " + toStringWC(pNontermCall)
           + " with " + pHir.toStringShort()
           + " in " + pData.nontermPat
           + " ancestor " + pAncestorNontermPatInstance);
  }
  Map lFormalActualParamCorresp;
  HirList lArgList = (HirList)pNontermCall.getChild2();
  if (lMetaSymName == "_bnfOr") {
    lArgList = (HirList)pNontermCall.getChild2();
    Iterator lListIt1 = lArgList.iterator();
    HIR lPattern1 = (HIR)lListIt1.next(); // Skip dummy argument.
    for ( ; lListIt1.hasNext(); ) {
      lPattern1 = (HIR)lListIt1.next();  // The next pattern in _bnfOr.
      // Check pattern code
      MatchingData lData1 = new MatchingData(pNontermInstance, pData);
      if (isMatchedPattern(lData1,
           lPattern1, pHir, pAncestorNontermPatInstance)) {
        // lPattern1 is the first pattern matched.
        // Update the maps.
        pData.reflect(lData1);
        pData.matchedProduction = lPattern1;
        if (pData.parentData != null)
          pData.parentData.matchedProduction = lPattern1;
        if (fDbgLevel > 3)
          dbgOut(5, "\n set parent " + pData.parentData.nontermPat
                 + " of " + pData.nontermPat +
                 " " + pNontermInstance
                 + " matchedProd " + lPattern1.toStringShort());
        pData.matchedInput = pHir;
        Set lTraversedNonterminals = new HashSet();
        // expandOutPattern(pData, lPattern1, lTraversedNonterminal );
        pData.expandedHir = lData1.expandedHir;
        if (pData.expandedHir != null) {
          recordExpandedHir(lPattern1,
            pData.expandedHir,
            pNontermInstance);
        }
        if (fDbgLevel > 2) {
          dbgOut(5, " copy expandedHir " + lData1.expandedHir +
                 " of selected production " + lPattern1);
          dbgOut(6, "\nisMatchedNonterminal return true "
                  + lMetaSymName + " "
                  + "\n   matchedProd " + pData.matchedProduction
                  + "\n   matchedInput " + pData.matchedInput.toStringWithChildren()
                  + "\n   expandedHir " + toStringWC(pData.expandedHir) //##93
                  + "\n   paramCorresp " + dbgMap(pData.paramCorresp)
                  + "\n   nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
        }
        pData.succeeded = true;
        return true;
      }
    }
    if (fDbgLevel > 2)
      dbgOut(6, "\nisMatchedNonterminal return false "
             + lMetaSymName );
    // Leave the maps unchanged.
    pData.succeeded = false;
    return false;
  }else if (lMetaSymName == "_bnfSeq") {
    MatchingData lData2 = new MatchingData(pNontermInstance, pData);
    Iterator lListIt2 = lArgList.iterator();
    HIR lPattern2 = (HIR)lListIt2.next(); // Skip dummy argument.
    for ( ; lListIt2.hasNext(); ) {
      lPattern2 = (HIR)lListIt2.next();
      // Check pattern code
      if (! isMatchedPattern(lData2,
           lPattern2, pHir, pAncestorNontermPatInstance)) {
        if (fDbgLevel > 2)
          dbgOut(6, "\nisMatchedNonterminal return false "
                 + lMetaSymName );
       // Leave the maps unchanged.
       pData.succeeded = false;
       return false;
      }
    }
    // Update the maps.
    pData.reflect(lData2);
    pData.matchedProduction = null; // ??
    Set lTraversedNonterminals = new HashSet();
    expandOutPattern(pData, lPattern2, lTraversedNonterminals );
    if (pData.expandedHir != null) {
      recordExpandedHir(lPattern2,
        pData.expandedHir,
        pNontermInstance);
    }
    if (fDbgLevel > 2) {
      dbgOut(6, "\nisMatchedNonterminal return true "
              + lMetaSymName + " "
              + "\n   matchedProd " + pData.matchedProduction
              + "\n   matchedInput " + pData.matchedInput.toStringWithChildren()
              + "\n   expandedHir " + toStringWC(pData.expandedHir) //##93
             + "\n   paramCorresp " + dbgMap(pData.paramCorresp)
             + "\n   nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
    }
    pData.succeeded = true;
    return true;
  }else if (lMetaSymName == "_assignStmt") {
    if (isMatchedAssignStmt(pData, pNontermInstance,
         pNontermCall, pHir, pAncestorNontermPatInstance)) {
      if (fDbgLevel > 2) {
        dbgOut(6, "\nisMatchedNonterminal return true "
               + lMetaSymName + " "
               + "\n   matchedProd " + pData.matchedProduction
               + "\n   matchedInput " + pData.matchedInput.toStringWithChildren()
               //##0906506 "\n   expandedHir " + pData.expandedHir.toStringWithChildren()
               + "\n   expandedHir " + toStringWC(pData.expandedHir) //##93
               + "\n   paramCorresp " + dbgMap(pData.paramCorresp)
               + "\n   nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
           }
           pData.succeeded = true;
           return true;
      }else {
        if (fDbgLevel > 2)
          dbgOut(6, "\nisMatchedNonterminal return false "
                 + lMetaSymName );
        // Leave the maps unchanged.
        pData.succeeded = false;
        return false;
    }
  }
  // Call defined nonterminal
  //##86 BEGIN
  //-- Instanciate formal parameters.
  if (lNontermSym.getParamList().size() > 0) {
    for (int i = 0; i < lNontermSym.getParamList().size();
         i++) {
      Param lParam = (Param)lNontermSym.getParamList().get(i);
      Param lNewParam = instanciateParam(lNontermSym, lParam,
        fInstanceNumberForNonterminals[lNontermIndex]);
      pNontermInstance.oldToNewParamMap.put(lParam, lNewParam);
      // fOriginalVarMap.put(lNewParam, lParam); // done in initiateVar
      if (fDbgLevel > 2)
        dbgOut(5, " put(" + lParam.getName() + "," +
           lNewParam.getName() + ") to " + pNontermInstance);
    }
  }
  //-- Instanciate local variables (fitting symbols).
  SymTable lSymTable = lNontermSym.getSymTable();
  for (SymIterator lSymIt = lSymTable.getSymIterator();
       lSymIt.hasNext(); ) {
    Sym lSymV = (Sym)lSymIt.next();
    if ((lSymV instanceof Var)&&
        (! (lSymV instanceof Param))) {
      Var lSymVar = (Var)lSymV;
      Var lNewVar = instanciateVar(lNontermSym,
        lSymVar, Sym.KIND_VAR,
        fInstanceNumberForNonterminals[lNontermIndex]);
      pNontermInstance.oldToNewParamMap.put(lSymVar, lNewVar);
      if (fDbgLevel > 2)
        dbgOut(5, " put(" + lSymVar.getName() + "," +
           lNewVar.getName() + ") to " + pNontermInstance);
    }
  }
  //##86 END
  lFormalActualParamCorresp = getFormalActualParamCorrespondence(
     pData, pNontermInstance, pNontermCall, false);
  pData.nontermParamCorresp.putAll(lFormalActualParamCorresp);
  lArgList = (HirList)pNontermCall.getChild2();
  if (fProductionMap.containsKey(lNontermSym)) {
    HIR lProduction = (HIR)fProductionMap.get(lNontermSym);
    if (lProduction.getOperator() == HIR.OP_EXP_STMT) {
      if ((lProduction.getChild1().getOperator() == HIR.OP_CALL)&&
          (((HIR)lProduction.getChild1()).getFlag(HIR.FLAG_NONTERMINAL))) {
        // It referes another nonterminal.
        lProduction = (HIR)lProduction.getChild1();
      }else if (pHir instanceof Exp) {
        // It is an expression.
        lProduction = (HIR)lProduction.getChild1();
      }
    }
    pData.matchedProduction = lProduction; // May be rewritten in child _bnfOr
    MatchingData lData3 = new MatchingData(pNontermInstance, pData);
    if (! isMatchedPattern(lData3,
                 lProduction, pHir, pAncestorNontermPatInstance)) {
      if (fDbgLevel > 2)
        dbgOut(6, "\nisMatchedNonterminal return false "
               + lMetaSymName );
      pData.succeeded = false;
      return false;
    }
    // Matched nonterminal.
    lFormalActualParamCorresp = getFormalActualParamCorrespondence(
         lData3, pNontermInstance, pNontermCall, false);
    pData.reflect(lData3);
    pData.nontermParamCorresp.putAll(lFormalActualParamCorresp);
    pData.recordMatchingData( pNontermInstance, pNontermCall, lData3);
    Set lTraversedNonterminals = new HashSet();
    pData.matchedInput = pHir;
    pData.expandedHir = lData3.expandedHir;
    if (fDbgLevel > 2)
      dbgOut(5, " copy expandedHir " + lData3.expandedHir
             + " of " + lData3.nontermPat);
    if (pData.expandedHir != null) {
      recordExpandedHir(pNontermCall,
        pData.expandedHir,
        pNontermInstance);
    }
    if (fDbgLevel > 2) {
      dbgOut(6, "\nisMatchedNonterminal return true "
              + lMetaSymName + " "
              + "\n   matchedProd " + pData.matchedProduction
              + "\n   matchedInput " + pData.matchedInput.toStringWithChildren()
              + "\n   expandedHir " + toStringWC(pData.expandedHir) //##93
              + "\n   paramCorresp " + dbgMap(pData.paramCorresp)
              + "\n   nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
    }
    pData.succeeded = true;
    return true;
  }
  if (fDbgLevel > 2)
    dbgOut(6, "\nisMatchedNonterminal return false "
            + lMetaSymName );
  // Leave the maps unchanged.
  pData.succeeded = false;
  return false;
} // isMatchedNonterminal

/**
 * Examine whether the assign statement pHir matches with
 * the pattern specified by _assignStmt.
 * @param pData Matching data to be used.
 * @param pNontermInstance Instance of the nonterminal
 *        (_assignStmt) representing the pattern.
 * @param pNontermCall HIR expression calling _assignStmt.
 * @param pHir HIR to be matched with the pattern.
 * @param pAncestorNontermPatInstance Ancestor pattern or
 *        ancestor nonterminal.
 * @return  true if matched, false otherwise.
 */
protected boolean
isMatchedAssignStmt( MatchingData pData,
      NontermPatInstance pNontermInstance,
      HIR pNontermCall, HIR pHir,
      NontermPatInstance pAncestorNontermPatInstance )
{
  if (fDbgLevel > 1) {
    dbgOut(2, "\nisMatchedAssignStmt entry "
               + " " + toStringWC(pNontermCall)
               + " with " + pHir.toStringShort()
               + " in " + pData.nontermPat
               + " ancestor " + pAncestorNontermPatInstance);
  }
  SymNode lSymNode = null;
  if (pNontermCall.getChild1() instanceof SymNode)
    lSymNode = (SymNode)pNontermCall.getChild1();
  else if (pNontermCall.getChild1().getChild1() instanceof SymNode)
    lSymNode = (SymNode)pNontermCall.getChild1().getChild1();
  Sym lNontermSym = lSymNode.getSymNodeSym();
  if (pHir.getOperator() == HIR.OP_ASSIGN) {
    HirList lArgList = (HirList)pNontermCall.getChild2();
    // Skip the first parameter.
    HIR lPatternL = (HIR)lArgList.get(1);
    HIR lPatternR = (HIR)lArgList.get(2);
    HIR lAssignL = (HIR)pHir.getChild1();
    HIR lAssignR = (HIR)pHir.getChild2();
    MatchingData lData1 = new MatchingData(pNontermInstance, pData);
    if (isMatchedPattern(lData1, lPatternL, lAssignL,
              pAncestorNontermPatInstance)) {
      MatchingData lData2 = new MatchingData(pNontermInstance, pData);
      if (isMatchedPattern(lData2, lPatternR, lAssignR,
             pAncestorNontermPatInstance)) {
        pData.reflectCorresp(lData1);
        pData.reflectCorresp(lData2);
        pData.matchedInput = pHir;
        HirList lArgList2 = hir.hirList();
        lArgList2.add(hir.intConstNode(0)); // Dummy argument
        lArgList2.add(lData1.expandedHir.copyWithOperands());
        lArgList2.add(lData2.expandedHir.copyWithOperands());
        SymNode lAssignStmtSymNode = hir.symNode(lNontermSym);
        pData.expandedHir = hir.callStmt(lAssignStmtSymNode, lArgList2);
        recordExpandedHir(pNontermCall, pData.expandedHir,
                 pNontermInstance);
        if (fDbgLevel > 2) {
          dbgOut(4, "\nisMatchedAssignStmt return true "
                 + "\n   matchedInput " + pData.matchedInput.toStringWithChildren()
                 + "\n   expandedHir " + toStringWC(pData.expandedHir) //##93
                 + "\n   paramCorresp " + dbgMap(pData.paramCorresp)
                 + "\n   nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
        }
        pData.succeeded = true;
        return true;
      }
    }
  }
  // Matching failed. Leave the maps unchanged.
  if (fDbgLevel > 2)
    dbgOut(4, "\nisMatchedAssignStmt return false ");
  pData.succeeded = false;
  return false;
} // isMatchedAssignStmt

/**
 * transformByPatternSym transforms pInHir according to the
 * pair of in-pattern and out-pattern defined by
 * pPatternSym. It is called from tryToReform.
 * For each parameter in in-pattern,
 * corresponding expression/statement in pInHir should be
 * already recorded in pData by isMatchedPattern method.
 * Usually, transformByPatternSym is invoked in tryToReform
 * when an expression or statement is discriminated to be
 * matched with some pattern by isMatchedPattern.
 * The transformed result raise the flag FLAG_NOCHANGE to
 * show that further optimization is not desirable. //REFINE
 * @param pData matching data.
 * @param pPatternSym pattern symbol showing in-pattern and
 *     out-pattern to be used in transformation.
 * @param pInHir a part of input program to be transformed.
 * @return HIR subtree generated as the result of the
 *     transformation.
 */
protected HIR
transformByPatternSym( MatchingData pData,
             Subp pPatternSym, HIR pInHir)
{
  if ((pPatternSym == null)||(pInHir == null))
    return pInHir;
  HIR lInPattern = null;
  if (fInPatternMap.containsKey(pPatternSym))
    lInPattern = (HIR)fInPatternMap.get(pPatternSym);
  if (lInPattern == null) {
    dbgOut(1, "Undefined pattern "
      + pPatternSym.getName()
      + " for " + pInHir.toStringShort());
    return pInHir;
  }
  if (fDbgLevel > 1) {
    dbgOut(3, "transformByPatternSym", pInHir.toStringShort()
           + " by " + lInPattern.toStringShort() +
           " " + dbgMap(pData.paramCorresp) +
           " " + dbgMap(pData.nontermParamCorresp)
           );
    dbgOut(3, "\nfOriginalVarMap " +
          dbgMap(fOriginalVarMap));
    dbgOut(3, "\nfNonterminalInstanceInPattern " +
          dbgMap(fNontermInstanceInPattern));
  }
  fCurrentPatternSym = pPatternSym;
  // Generate variables corresponding to the local variables
  // defined in the pattern and parepare to replace them with
  // the gererated variable.
  List lLocalVarList = (List)fLocalVarListMap.get(pPatternSym);
  for (Iterator lIt1 = lLocalVarList.iterator();
       lIt1.hasNext(); ) {
    Var lVar = (Var)lIt1.next();
    if (symRoot.symTableCurrentSubp.search(lVar.getName().intern()) == null) {
       Var lTempVar = symRoot.symTableCurrentSubp.generateVar(lVar.getSymType());
        pData.paramCorresp.put(lVar, hir.varNode(lTempVar));
        if (fDbgLevel > 0)
          dbgOut(2, " map " + lVar.getName() + " to " + lTempVar.getName());
    }
  }
  //-- Construct out-pattern adjusted to the input.
  HIR lOutPattern, lOutHir;
  lOutPattern = (HIR)fOutPatternMap.get(pPatternSym);
  lOutHir = lOutPattern.copyWithOperandsChangingLabels(null);
  if (fDbgLevel > 2) {
    if (lOutHir != null)
      dbgOut(4, "\n outHir " + lOutHir.toStringWithChildren());
    else
      dbgOut(4, "\n outHir null");
    dbgOut(4, "\nparamCorresp " + dbgMap(pData.paramCorresp));
    dbgOut(4, "\nnontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
  }
  //-- Complete the paramCorresp seeing nontermParamCorresp.
  makeParamCorrespComplete(pData);
  // lOutHir Should be a copy because if VarNode is used in
  //   (contents (add (var v) (mult xx xx))
  //  the parent of the VarNode is replaced in the transform method.
  Set lTraversedMatchingData = new HashSet();
  lOutHir = adjustOutPattern(pData, lOutHir,
               lTraversedMatchingData, 0);
  if (fDbgLevel > 0)
    dbgOut(3, "\noutHir adjusted " + lOutHir.toStringWithChildren());
  if (fNoFurtherChange.contains(fCurrentPatternSym)) {
    lOutHir.setFlag(HIR.FLAG_NOCHANGE, true);
  }
  if (fDbgLevel > 0) {
    dbgOut(3, "\nresult of transformByPatternSym "
           + lOutHir.toStringWithChildren());
  }
  //-- Reset information for transformation.
  int lSize = fLastMatchingDataForNonterm.length; //##88
  for (int lInx = 0; lInx < lSize; lInx++) {
    fLastMatchingDataForNonterm[lInx] = null;
  }
  return lOutHir;
} // transformByPatternSym

/**
 * expandOutPattern expands pOutHir according to pData.
 * Fitting symbols and nonterminal parameters are replaced
 * by the corresponding HIR recorded in pData.
 * If pOutHir contains a nonterminal call, a new instance
 * of the nonterminal is created and its expansion is determinde
 * by Nonterminal calls are replaced by the corresponding HIR
 * determined by expandNonterminal (or getExpandedHir).
 * Pattern parameters and other expressions contained in pOutHir
 * are left unchanged.
 * @param pData Matching data to be used.
 * @param pOutHir HIR to be expanded.
 * @param pTraversedNonterminals Set of traverced nonterminal
 *        instances (used to avoid infinite loop).
 * @return the expanded result.
 */
protected HIR
expandOutPattern( MatchingData pData, HIR pOutHir
  , Set pTraversedNonterminals)
{
  if (pOutHir == null)
    return null;
  if (fDbgLevel > 3) {
    dbgOut(5, "\n expandOutPattern " + pData.nontermPat
           + " " + pOutHir.toStringShort());
    dbgOut(6, " " + pOutHir.toStringWithChildren());
  }
  HIR lNewHir = null;
  int lOpCode = pOutHir.getOperator();
  int lChildCount = pOutHir.getChildCount();
  if (lChildCount <= 0) {
    if (pOutHir instanceof SymNode) {
      Sym lSym = ((SymNode)pOutHir).getSymNodeSym();
      if (isFittingSym(lSym)||
          pData.nontermParamCorresp.containsKey(lSym)) {
        MatchingData lData = pData;
        if (isFittingSym(lSym)&&
            (! pData.nontermParamCorresp.containsKey(lSym))) {
          if (pData.nontermPat.oldToNewParamMap.containsKey(lSym)) {
            lData = new MatchingData(pData.nontermPat, pData);
            HIR lFittingHir =
              (HIR)pData.nontermPat.oldToNewParamMap.get(lSym);
            if (fDbgLevel > 2)
              dbgOut(4, " Add fitting symbol correspondence "
                + lSym.getName() + " = " + lFittingHir.toStringWithChildren());
            pData.nontermParamCorresp.put(lSym, lFittingHir);
          }
        }
        Set lTraversedMatchingData = new HashSet();
        lNewHir = replaceParameters(lData, pOutHir.copyWithOperands(),
              lTraversedMatchingData, true);
        pData.expandedHir = lNewHir;
        return lNewHir;
      }else {
        lNewHir = pOutHir.copyWithOperands();
      }
      pData.expandedHir = lNewHir;
      return lNewHir;
    }else if (pOutHir instanceof HirList) {
      HirList lNewList = (HirList)pOutHir.hirNodeClone();
      for (Iterator lIt = ((IrList)pOutHir).iterator();
           lIt.hasNext(); ) {
        HIR lElem = (HIR)lIt.next();
        if (lElem != null) {
          HIR lNewElem = expandOutPattern(pData, lElem,
                          pTraversedNonterminals); //, pTraversedMatchingData);
          lNewList.add(lNewElem);
        }
      }
      pData.expandedHir = lNewList;
      return lNewList;
    }else {
      lNewHir = pOutHir.copyWithOperands();
      pData.expandedHir = lNewHir;
      return lNewHir;
    }
  }else if (pOutHir.getFlag(HIR.FLAG_NONTERMINAL)||
            (lOpCode == HIR.OP_CALL)) {
    Subp lNontermSym = null;
    if (lOpCode == HIR.OP_CALL) {
      if  ((pOutHir.getChild1().getOperator() == HIR.OP_ADDR)&&
           (pOutHir.getChild1().getChild1() instanceof SymNode))
        lNontermSym = (Subp)((SymNode)pOutHir.getChild1().getChild1()).getSymNodeSym();
      else if (pOutHir.getChild1() instanceof SymNode)
        lNontermSym = (Subp)((SymNode)pOutHir.getChild1()).getSymNodeSym();
      if (lNontermSym != null) {
        if (fNonterminalSet.contains(lNontermSym)) {
          int lNontermIndex = getIndex(lNontermSym);
          NontermPatInstance lNontermPatInstance =
            new NontermPatInstance(lNontermSym,
              fInstanceNumberForNonterminals[lNontermIndex],
              pOutHir, pData.nontermPat);
          HIR lExpandedNonterm = getExpandedHir(pOutHir, lNontermPatInstance);
          if (pTraversedNonterminals.contains(lNontermSym)&&
              (lExpandedNonterm != null))
          {
            lNewHir = lExpandedNonterm.copyWithOperandsChangingLabels(null);
            if (fDbgLevel > 2)
              dbgOut(4, " use fExpandedNonterminal of "
                + lNontermSym.getName());
          }else {
            MatchingData lData = new MatchingData(lNontermPatInstance, pData);
            lNewHir = expandNonterminal(lData, lNontermPatInstance,
              pOutHir, pTraversedNonterminals);
            pData.reflect(lData);
            if (lNewHir != null) {
              recordExpandedHir(pOutHir,
                lNewHir,
                pData.nontermPat);
            }else {
              //# fExpandedNonterminal[getIndex((Subp)pData.nontermPat)] = null;
            }
            if (fDbgLevel > 2) {
              dbgOut(3, "\nSet expandedHir " + lNewHir +
                " to " + pData.nontermPat);
              if (lNewHir != null)
                dbgOut(5, " " + lNewHir.toStringWithChildren());
            }
          }
          pData.expandedHir = lNewHir;
          return lNewHir;
        }else {
          String lSymName = lNontermSym.getName().intern();
          if (lSymName == "_bnfOr") {
            MatchingData lData = new MatchingData(pData.nontermPat, pData);
            HIR lMatchedProduction = pData.matchedProduction;
            if (fDbgLevel > 0)
              dbgOut(4, "\n _bnfOr matchedProd " + lMatchedProduction);
            if (lMatchedProduction == null) {
              HIR lExpandedNonterm = getExpandedHir(pOutHir,  pData.nontermPat);
              if (lExpandedNonterm != null)
              {
                HIR lNewHir1 = lExpandedNonterm.copyWithOperandsChangingLabels(null);
                if (fDbgLevel > 0)
                  dbgOut(4, " return fExpandedNonterminal " +
                      lNewHir1.toStringWithChildren());
                return lNewHir1;
              }else {
                ioRoot.msgRecovered.put("Matched production is not found at "
                  + pOutHir + " in " + pData.nontermPat);
              }
            }
            lNewHir = expandOutPattern(lData, lMatchedProduction,
              pTraversedNonterminals);
            if ((fDbgLevel > 2)&&(lNewHir != null))
              dbgOut(4, "\n result for " + pData.nontermPat
               + " " + lNewHir.toStringWithChildren());
            pData.expandedHir = lNewHir;
            return lNewHir;
          }else if (lSymName == "_bnfSeq") {
            //??
          }
        }
      }
    }
    // Return pOutHir without changing it.
    pData.expandedHir = pOutHir;
    return pOutHir;
  }else if (lOpCode == HIR.OP_BLOCK) {
    MatchingData lData = new MatchingData(pData.nontermPat, pData);
    BlockStmt lNewBlock = hir.blockStmt(null);
    Stmt lStmt = ((BlockStmt)pOutHir).getFirstStmt();
    Stmt lNextStmt;
    while (lStmt != null) {
      lNextStmt = lStmt.getNextStmt();
      Stmt lNewStmt = (Stmt)expandOutPattern(lData, lStmt,
        pTraversedNonterminals);
      lNewBlock.addLastStmt(lNewStmt);
      lStmt = lNextStmt;
    }
    pData.expandedHir = lNewBlock;
    return lNewBlock;
  }else {
    MatchingData lData = new MatchingData(pData.nontermPat, pData);
    lNewHir = pOutHir.hirNodeClone();
    for (int lChildIndex = 1; lChildIndex <= lChildCount;
         lChildIndex++) {
      HIR lChild = (HIR)pOutHir.getChild(lChildIndex);
      if (lChild != null) {
        MatchingData lData_1 = new MatchingData(lData.nontermPat, lData);
        HIR lNewChild = expandOutPattern(lData_1, lChild,
          pTraversedNonterminals);
        if ((lNewChild != null)&&
            (lNewChild.getOperator() == HIR.OP_EXP_STMT)&&
            (lChild instanceof Exp))
          lNewHir.setChild(lChildIndex, lNewChild.getChild1());
        else
          lNewHir.setChild(lChildIndex, lNewChild);
      }
    }
    pData.expandedHir = lNewHir;
    return lNewHir;
  }
} // expandOutPattern

/**
 * expandNonterminal expands the right hand side of the
 * selected productin of the nonterminal pNontermPatInstance
 * and return it.
 * If the result of previous expansion is recorded
 * in pData, then return it.
 * If it is a recursive call, do not expand.
 * @param pData MatchingData to be used.
 * @param pNontermPatInstance Instance of a nonterminal.
 * @param pCallExp Expression that calls the nonterminal.
 * @param pTraversedNonterminals Set of traverced nonterminal
 *        instances (used to avoid infinite loop).
 * @return the expanded result.
 */
protected HIR
expandNonterminal( MatchingData pData,
  NontermPatInstance pNontermPatInstance,
  HIR pCallExp,
  Set pTraversedNonterminals)
{
  Map lParamCorresp;
  if (fDbgLevel > 0) {
    dbgOut(3, "\nexpandNonterminal " + pNontermPatInstance
           + " pData " + pData.nontermPat
           + " matchedProd " + pData.matchedProduction
           + " expandHir " + pData.expandedHir
           );
    if (fDbgLevel > 3) {
      dbgOut(5, "\n  " + dbgMap(pData.paramCorresp)
             + " " + dbgMap(pData.nontermParamCorresp));
      if (pData.expandedHir != null)
        dbgOut(6, "\n pData.expandedHir "
               + pData.expandedHir.toStringWithChildren())
;    }
  }
  HIR lNewHir;
  Subp lNonterminal = pNontermPatInstance.nontermPat;
  int lIndex = getIndex(lNonterminal);
  if (pTraversedNonterminals.contains(pNontermPatInstance)
      &&(pData.expandedHir != null)
      &&(! getLeafOperands(pData.expandedHir).contains(lNonterminal))) {
    lNewHir = pData.expandedHir; //??
    dbgOut(1, "\n avoid left recursion in " + pNontermPatInstance);
    dbgOut(4, " fExpandedNonterm "
           + " pData.expandHir " + pData.expandedHir);
  }else if (pData.expandedHir != null) {
    lNewHir = pData.expandedHir;
    if (fDbgLevel > 1)
      dbgOut(4, " use pData.expandedHir");
  }else {
    pTraversedNonterminals.add(pNontermPatInstance);
    MatchingData lData2;
    lData2 = new MatchingData(pNontermPatInstance, pData);
    HIR lNontermBody;
    if ((pData.matchedProduction != null)&&
        (! getLeafOperands(pData.matchedProduction).contains(lNonterminal)))
      lNontermBody = pData.matchedProduction;
    else
      lNontermBody = (HIR)fOutPatternMap.get(lNonterminal);
    lNewHir = expandOutPattern(lData2, lNontermBody,
      pTraversedNonterminals);
    lData2.expandedHir = lNewHir;
  }
  if (fDbgLevel > 2) {
    dbgOut(3, "\nexpandedHir of " + pNontermPatInstance
            + "= " + lNewHir);
    if (lNewHir != null)
      dbgOut(5, " " + lNewHir.toStringWithChildren());
  }
  recordExpandedHir(pCallExp, lNewHir, pNontermPatInstance);
  pData.expandedHir = lNewHir;
  return lNewHir;
} // expandNonterminal

/**
 * Adjust the out pattern pOutHir corresponding to the pattern/nonterminal
 * represented by pData, that is,
 * If pOutHir is a pattern/nonterminal param node, then
 *   replace it by the HIR corresponding to it;
 * If pOutHir represents a nonterminal, then
 *   replace it by the expanded HIR corresponding to it
 *   invoking adjustNonterminal with a
 *   MachingData obtained by copying pData;
 * If pOutHir represents
 *   _reform(n_th_instance, patternParam, replacementExp,
 *           nonterminalCallExp),
 *   then expand the nonterminal and then replace the pattern
 *   parameter (patternParam) contained in it with
 *   replacementExp and then replace the pattern parameters
 *   according to the correspondence given in pData.
 *   If there are several nonterminal calls having the same
 *   form in a pattern definition, then the nonterminal instance
 *   corresponding to the i-th nonterminal call
 *   is selected where the sequence number i is given
 *   by the parameter pNthInstance.
 * If pOutHir is a list, then replace each element
 *   by applying adjustOutPattern to the element;
 * If pOutHir is a block, then replace each statement
 *   by applying adjustOutPattern to the statement;
 * If pOutHir is other subtree, then replace each child
 *   by applying adjustOutPattern to the child;
 * @param pData Matching data to be used.
 * @param pOutHir Out pattern corresponding to the pattern/nonterminal
 *   represented by pData (it is already a copy and
 *   unnecessary to be copied in this method).
 * @param pTraversedMatchingData Set of traversed matching data
 *   to be used to avoid the application of the same matching data.
 * @param pNthInstance Sequence number (0, 1, 2, ...) assigned
 *   to a nonterminal call to distinguish it from other nonterminal
 *   calls having the same form in the definiton of a pattern.
 * @return the adjusted HIR.
 */
protected HIR
adjustOutPattern( MatchingData pData, HIR pOutHir,
  Set pTraversedMatchingData, int pNthInstance )
{
  if (pOutHir == null)
    return null;
  if (fDbgLevel > 3)
    dbgOut(5, "\n adjustOutPattern " + pData.nontermPat
           + " " + pNthInstance + "-th "
           + pOutHir.toStringShort());
  Subp lNontermSym = null;
  //##91 Subp lSubpSym = null;
  Sym lSubpSym = null; //##91
  HIR lAdjustedResult = null;
  int lNthInstance = pNthInstance;
  int lOpCode = pOutHir.getOperator();
  int lChildCount = pOutHir.getChildCount();
  if (lOpCode == HIR.OP_CALL) {
    if  ((pOutHir.getChild1().getOperator() == HIR.OP_ADDR)&&
         (pOutHir.getChild1().getChild1() instanceof SymNode)) {
      //##91 lSubpSym = (Subp)((SymNode)pOutHir.getChild1().getChild1()).getSymNodeSym();
      lSubpSym = ((SymNode)pOutHir.getChild1().getChild1()).getSymNodeSym(); //##91
  }else if (pOutHir.getChild1() instanceof SymNode) {
     //##91 lSubpSym = (Subp)((SymNode)pOutHir.getChild1()).getSymNodeSym();
     lSubpSym = ((SymNode)pOutHir.getChild1()).getSymNodeSym(); //##91
  }
    if (lSubpSym != null) {
      //##91 BEGIN
      if (fCompileTimeEval.contains(lSubpSym)) {
        if (fDbgLevel > 0)
          dbgOut(4, "\n compileTimeEval " + lSubpSym.getName());
        if (lSubpSym.getName() == "_getName") {
          HIR lArgList = (HIR)pOutHir.getChild2();
          if (lArgList instanceof IrList) {
            HIR lArg2 = (HIR)((IrList)lArgList).get(1);
            if (fDbgLevel > 0)
              dbgOut(4, " arg2 " + lArg2);
            HIR lArg = adjustOutPattern(pData, lArg2,
               pTraversedMatchingData, pNthInstance );
            if (lArg.getOperator() == HIR.OP_CONV)
              lArg = (HIR)lArg.getChild1();
            if (fDbgLevel > 0)
              dbgOut(4, " arg " +lArg);
            if (lArg instanceof SymNode) {
              String lName = ((SymNode)lArg).getSymNodeSym().getName();
              Const lConst = symRoot.sym.stringConst(lName);
              HIR lConstNode = hir.constNode(lConst);
              HIR lConstPtr = hir.decayExp((ConstNode)lConstNode);
              lAdjustedResult = lConstPtr;
              if (fDbgLevel > 1) {
                dbgOut(4, "\n result of adjustOutPattern: " +
                  lAdjustedResult.toStringWithChildren());
              }
              return lAdjustedResult;
            }
          }
        }
      }
      //##91 END
      if (fNonterminalSet.contains(lSubpSym)) {
        //##91 lNontermSym = lSubpSym;
        lNontermSym = (Subp)lSubpSym; //##91
     }else {
        String lSymName = lSubpSym.getName();
        if ((lSymName == "_bnfOr")||
            (lSymName == "_bnfSeq")||
            (lSymName == "_assignStmt")||
            (lSymName == "_reform")) {
          //##91 lNontermSym = lSubpSym;
          lNontermSym = (Subp)lSubpSym; //##91
        }
      }
    }
  }
  if (lChildCount <= 0) {
    if (pOutHir instanceof SymNode) {
      Sym lSym = ((SymNode)pOutHir).getSymNodeSym();
      if ( //(lSym instanceof Param)&&
          (pData.paramCorresp.containsKey(lSym)||
           pData.nontermParamCorresp.containsKey(lSym))) {
          HIR lNewHir = replaceParameters(pData, pOutHir,
                pTraversedMatchingData, false);
          lAdjustedResult = lNewHir;
      }else
        lAdjustedResult = pOutHir;
    }else if (pOutHir instanceof HirList) {
      for (Iterator lIt = ((IrList)pOutHir).iterator();
           lIt.hasNext(); ) {
        HIR lElem = (HIR)lIt.next();
        if (lElem != null) {
          HIR lNewElem = adjustOutPattern(pData, lElem,
                            pTraversedMatchingData, lNthInstance);
          if (lElem.getParent() != null) {
            // Replace only when lElem is not cutted off by replacement.
            replaceHirTree(lElem, lNewElem);
          }
        }
      }
      lAdjustedResult = pOutHir;
    }else {
      lAdjustedResult = pOutHir;
    }
  }else if (pOutHir.getFlag(HIR.FLAG_NONTERMINAL)||
            ((lOpCode == HIR.OP_CALL)&&
             (lNontermSym != null))) {
    if (lOpCode == HIR.OP_CALL) {
      if  ((pOutHir.getChild1().getOperator() == HIR.OP_ADDR)&&
           (pOutHir.getChild1().getChild1() instanceof SymNode))
        lNontermSym = (Subp)((SymNode)pOutHir.getChild1().getChild1()).getSymNodeSym();
      else if (pOutHir.getChild1() instanceof SymNode)
        lNontermSym = (Subp)((SymNode)pOutHir.getChild1()).getSymNodeSym();
      if (lNontermSym != null) {
        String lNontermName = lNontermSym.getName().intern();
        if (lNontermName == "_reform") {
          lAdjustedResult = evaluateReform(pData, pOutHir,
                     pTraversedMatchingData);
        }else if (fNonterminalSet.contains(lNontermSym)) {
          int lNontermIndex = getIndex(lNontermSym);
          NontermPatInstance lNontermPatInstance = null;
          HIR lCallKey = makeHirKey(pOutHir);
          List lInstanceList = null;
          if (fNontermInstanceInPattern.containsKey(lCallKey)) {
            lInstanceList = (List)fNontermInstanceInPattern.get(lCallKey);
            lNontermPatInstance = (NontermPatInstance)lInstanceList.get(pNthInstance);
          }
          if (lNontermPatInstance == null) {
            ioRoot.msgRecovered.put("Nonterminal expression "
              + pOutHir.toStringWithChildren()
              + " in oPattern is not defined in iPattern."
              + " Result may be incorrect.");
            if ((lInstanceList != null)&&
                (! lInstanceList.isEmpty()))
              lNontermPatInstance = (NontermPatInstance)lInstanceList.get(0);
            else
              lNontermPatInstance = pData.nontermPat;
          }
          MatchingData lNontermData =
            new MatchingData(lNontermPatInstance, pData);
          if (fLastMatchingDataForNonterm[lNontermIndex] != null) {
            MatchingData lData = lNontermData.getMatchingData(lNontermPatInstance, pOutHir);
            if (fDbgLevel > 1) {
              dbgOut(4, "\n fMatchingDataForNonterm " + lNontermPatInstance
                + " " + dbgMap(lData.paramCorresp)
                + " " + dbgMap(lData.nontermParamCorresp)
                + " " + lData.expandedHir.toStringShort());
            }
            lNontermData.reflectCorresp(lData);
            lNontermData.matchedInput = lData.matchedInput; //##81
            lNontermData.expandedHir = lData.expandedHir.
                  copyWithOperandsChangingLabels(null); //##86
          }
          HIR lNewHir = adjustNonterminal(lNontermData,
            lNontermPatInstance, pOutHir, pTraversedMatchingData,
            lNthInstance, false);
          lAdjustedResult = lNewHir;
        }else {
          String lSymName = lNontermSym.getName().intern();
          if (lSymName == "_assignStmt") {
            IrList lParamList = (IrList)pOutHir.getChild2();
            // Skip the first param.
            HIR lLhs = (HIR)lParamList.get(1);
            HIR lRhs = (HIR)lParamList.get(2);
            if (fDbgLevel > 2)
              dbgOut(4, "\n _assignStmt(" + lLhs.toStringWithChildren()
                + ", " + lRhs.toStringWithChildren()+ ")");
            Exp lLhsExp = (Exp)adjustOutPattern(pData, lLhs,
                          pTraversedMatchingData, 0);
            Exp lRhsExp = (Exp)adjustOutPattern(pData, lRhs,
                          pTraversedMatchingData, 0);
            lAdjustedResult = hir.assignStmt(lLhsExp, lRhsExp);
          }else if (lSymName == "_bnfOr") {
            if (fDbgLevel > 0)
              dbgOut(2, "\n _bnfOr encountered in " + pData.nontermPat);
          }else if (lSymName == "_bnfSeq") {
            HIR lMatchedProduction = pData.matchedProduction;
            if (fDbgLevel > 0)
              dbgOut(4, "\n _bnfOr matchedProd " + lMatchedProduction);
            HIR lNewHir = adjustOutPattern(pData, lMatchedProduction,
              pTraversedMatchingData, 0);
            lAdjustedResult = lNewHir;
          }else {
            lAdjustedResult = pOutHir;
          }
        }
      }else
        lAdjustedResult = pOutHir;
    }else
      lAdjustedResult = pOutHir;
  }else if (lOpCode == HIR.OP_BLOCK) {
    MatchingData lData = new MatchingData(pData.nontermPat, pData);
    BlockStmt lNewHir = hir.blockStmt(null);
    Stmt lStmt = ((BlockStmt)pOutHir).getFirstStmt();
    Stmt lNextStmt;
    while (lStmt != null) {
      lNextStmt = lStmt.getNextStmt();
      Stmt lNewStmt;
      if ((lStmt instanceof ExpStmt)&&
          (lStmt.getChild1() instanceof SymNode)&&
          fStmtParamSet.contains(((SymNode)lStmt.getChild1()).getSymNodeSym())) {
        if (fDbgLevel > 2)
          dbgOut(4, "\n Statement parameter was found in " + lStmt);
        SymNode lSymNode = (SymNode)((HIR)lStmt.getChild1()).hirClone();
        lNewStmt = (Stmt)replaceStatementParameter(lData,
                     lSymNode, pTraversedMatchingData, false);
      }else {
        lNewStmt = (Stmt)adjustOutPattern(lData, lStmt,
          pTraversedMatchingData, lNthInstance);
      }
      lNewHir.addLastStmt(lNewStmt);
      lStmt = lNextStmt;
    }
    lAdjustedResult = lNewHir;
  }else if (lOpCode == HIR.OP_EXP_STMT) {
    HIR lNewHir = adjustOutPattern(pData, (HIR)pOutHir.getChild1(),
              pTraversedMatchingData, pNthInstance);
    if (lNewHir instanceof Exp) {
      lAdjustedResult = hir.expStmt((Exp)lNewHir);
    }else {
      lAdjustedResult = lNewHir;
    }
  }else {
    MatchingData lData = new MatchingData(pData.nontermPat, pData);
    HIR lNewHir = pOutHir.hirNodeClone();
    for (int lChildIndex = 1; lChildIndex <= lChildCount;
         lChildIndex++) {
      HIR lChild = (HIR)pOutHir.getChild(lChildIndex);
      if (lChild != null) {
        HIR lNewChild = adjustOutPattern(lData, lChild,
              pTraversedMatchingData, pNthInstance);
        if ((lNewChild != null)&&
            (lNewChild.getOperator() == HIR.OP_EXP_STMT)&&
            (lChild instanceof Exp))
          lNewHir.setChild(lChildIndex, lNewChild.getChild1());
        else
          lNewHir.setChild(lChildIndex, lNewChild);
      }
    }
    lAdjustedResult = lNewHir;
  }
  if (fDbgLevel > 1) {
    if (lAdjustedResult == null)
      dbgOut(4, "\n result of adjustOutPattern: null");
    else
      dbgOut(4, "\n result of adjustOutPattern: " +
           lAdjustedResult.toStringWithChildren());
  }
  return lAdjustedResult;
} // adjustOutPattern

/**
 * Change a nonterminal call to a plain HIR subtree
 * that has resolved parameters and nonterminal calls
 * by executing following steps:
 * (1) Get the instance of MatchingData lData1
 *   corresponding to the nonterminal call pCallNode.
 *   Get the expanded Hir lExpandedHir corresponding to
 *   the nonterminal instance pNontermPatInstance.
 * (2) Make the matching data complete, that is,
 *   trace the mapping sequence of nonterminal parameters
 *   and if it reaches to a pattern parameter, then
 *   make the nonterminal parameter corresponds to the HIR
 *   corresponding to the pattern parameter.
 * (3) Get the expanded HIR corresponding to the nonterminal
 *   instance pNontermPatInstance.
 * (4) Replace parameters in the expanded HIR.
 * (5) Return the resultant expanded HIR.
 * This is called from adjustOutPattern.
 * @param pData MatchingData instance to be used.
 * @param pNontermPatInstance Nonterminal instance.
 * @param pCallNode Nonterminal call node that instanciated
 *     pNontermPatInstance.
 * @param pTraversedMatchingData
 * @param pTraversedMatchingData Set of matching data already
 *     traversed (used to escape from infinite loop).
 * @param pNthInstance Sequence number (0, 1, 2, ...) assigned
 *   to a nonterminal call to distinguish it from other nonterminal
 *   calls having the same form in the definiton of a pattern.
 * @param pRevisedMatchingData true if pData is a revised one and
 *   unnecessary to use getMatchingData method.
 * @return Resultant HIR subtree adjusted.
 */
protected HIR
adjustNonterminal( MatchingData pData,
  NontermPatInstance pNontermPatInstance,
  HIR pCallNode, Set pTraversedMatchingData,
 int pNthInstance, boolean pRevisedMatchingData )
{
  Map lParamCorresp;
  if ((pNontermPatInstance == null)||
      (pCallNode.getOperator() != HIR.OP_CALL)) {
    return pCallNode;
  }
  if (fDbgLevel > 0) {
    dbgOut(3, "\nadjustNonterminal " + pNontermPatInstance
           + " " + pNthInstance + "-th pData "
           + pData.nontermPat
           + "\n  " + pCallNode.toStringWithChildren());
    if (fDbgLevel > 2) {
      dbgOut(5, "\n  " + dbgMap(pData.paramCorresp)
             + " " + dbgMap(pData.nontermParamCorresp));
      dbgOut(5, "\n expandedHir of pData "
             + toStringWC(pData.expandedHir)); //##93
      dbgOut(5, "\n matchedInput "
               + toStringWC(pData.matchedInput)); //##93
    }
  }
  Subp lNonterminal = pNontermPatInstance.nontermPat;
  int lNthInstance = pNthInstance;
  int lIndex = getIndex(lNonterminal);
  HIR lKey = makeHirKey(pCallNode);
  HIR lExpandedHir = null;
  MatchingData lData1;
  if (pRevisedMatchingData)
    lData1 = pData;
  else
    lData1 = pData.getMatchingData(pNontermPatInstance, pCallNode);
  MatchingData lData2;
  lExpandedHir = getExpandedHir(pCallNode, pNontermPatInstance)
        .copyWithOperandsChangingLabels(null);
  //## lData2 = new MatchingData(pNontermPatInstance, pData);
  lData2 = new MatchingData(pNontermPatInstance, lData1); //##
  lData2.expandedHir = lExpandedHir;
  // lNewHir = lMatchedInput.copyWithOperandsChangingLabels(null);
  //-- For each parameter, get actual parameter
  //   with which the parameter is to be replaced.
  //## lParamCorresp =
  //##   getFormalActualParamCorrespondence(lData2,
  //##   pNontermPatInstance, pCallNode, true);
  lParamCorresp = lData2.paramCorresp; //##
  //--
  // Adjust matching data for the case where actual nonterminal
  // parameter is different from that of in matching phase, e.g.
  //   void interpolate( double paa[], double pbb[], int pi, int pj ){
  //     iPattern: { paa[iExp(pi)]= subsVar(pbb,pj); }
  //     oPattern: { paa[iExp(pi)]= subsVar(pbb,pj)+subsVar(pbb,pj+1))/2.0; }
  //   }
  // REFINE Is it simplified when _reform is introduced ?
  makeParamCorrespComplete(lData2);
  // In the above method call, nonterminal parameters are
  // resolved to pattern parameters or HIR other than
  // parameter node.
  // Resolve all pattern parameters in lParamCorresp
  // to HIR that does not contain parameters by calling
  // adjustMatchingData.
  lParamCorresp = adjustMatchingData(lData2, lParamCorresp);
  lData2.paramCorresp.putAll(lParamCorresp);
  makeParamCorrespComplete(lData2);
  //-- Replace parameters.
  Set lTracedParams = new HashSet();
  if (fDbgLevel > 3) {
    dbgOut(5, "\n expandedHir to be passed to replaceParameters "
           + toStringWC(lData2.expandedHir)); //##93
  }
  // Replace nonterminal parameters in the expanded HIR at the
  // 1st stage and revise the parameter correspondences.
  HIR lNewHir = replaceParameters(lData2, lData2.expandedHir,
                  pTraversedMatchingData, true);
  if ((fDbgLevel > 3)&&(lNewHir != null))
    dbgOut(5, "\nReplace parameters after nonterminal parameter replacement "
           + "\n  " + lNewHir.toStringWithChildren());
  // Replace pattern parameters at the 2nd stage.
  lNewHir = replaceParameters(lData2, lNewHir,
                  pTraversedMatchingData, false);
  if (lNewHir == null) {
    // If the expanded HIR is not found, search it using
    // pCallNode and pNontermPatInstance.
    // (This will not happen.)
    lNewHir = getExpandedHir(pCallNode, pNontermPatInstance)
        .copyWithOperandsChangingLabels(null);
    if (fDbgLevel > 0)
      dbgOut(4, " expandedHir is null");
    if (lData2.matchedProduction != null) {
      if (fDbgLevel > 0)
        dbgOut(4, " take matchedProduction");
      MatchingData lData3 = new MatchingData(lData2.parentData.nontermPat,
        lData2.parentData);
      lData3.reflectCorresp(lData2);
     Set lTraversedNonterminals = new HashSet();
     lNewHir =  expandNonterminal(lData2, pNontermPatInstance,
       pCallNode, lTraversedNonterminals);
     if (fDbgLevel > 1)
       dbgOut(4, "\n expandNonterminal " + lNewHir.toStringShort());
    }else {
      lNewHir = lData2.matchedInput;
      if (fDbgLevel > 0)
        dbgOut(4, " take matchedInput");
    }
  }
  if (fDbgLevel > 1) {
    dbgOut(3, "\nRevised paramCorresp " + lData2.nontermPat
           + dbgMap(lData2.paramCorresp)
           + " " + dbgMap(lData2.nontermParamCorresp)
           + "\n expandedHir " + lNewHir);
    dbgOut(4, "\n  NewHir " + lNewHir.toStringWithChildren());
  }
  if (fLastMatchingDataForNonterm[lIndex] == null)
    lData1.recordMatchingData(pNontermPatInstance, pCallNode, lData1);
  if (fDbgLevel > 0)
    dbgOut(4, "\n Save expandedHir "
          + pNontermPatInstance + " "
           + lData1.expandedHir);
  if (fDbgLevel > 1)
    dbgOut(4, "\n adjustNonterminal result for " +
           pNontermPatInstance + ":" + lNewHir.toStringWithChildren());
  return lNewHir;
} // adjustNonterminal

/**
 * Evaluate the special function _reform
 * by calling reformByChangingParam( ... )
 * after examining given arguments.
 * The parameter pOutHir is an expanded HIR that may contain
 * pattern parameters and _assignStmt function.
 * @param pData Matching data to be used.
 * @param pOutHir expanded HIR to be transformed.
 * @param pTraversedMatchingData Set of traversed matching data.
 * @return the transformed HIR.
 */
protected HIR
evaluateReform( MatchingData pData, HIR pOutHir,
  Set pTraversedMatchingData )
{
  HIR lAdjustedResult;
  IrList lArgList = (IrList)pOutHir.getChild2();
  int lNthInstance = ((ConstNode)lArgList.get(0)).getIntValue();
  HIR lParamNode = (HIR)lArgList.get(1);
  HIR lRewriteExp = (HIR)lArgList.get(2);
  HIR lNontermCallExp = (HIR)lArgList.get(3);
  if (fDbgLevel > 2)
    dbgOut(5, "\nevaluateReform " + pData.nontermPat
             + " " + lNthInstance + "-th "
             + lNontermCallExp.toStringWithChildren());
  if (lNontermCallExp.getOperator() == HIR.OP_CALL) {
    Subp lNontermSym2 = null;
    if  ((lNontermCallExp.getChild1().getOperator() == HIR.OP_ADDR)&&
         (lNontermCallExp.getChild1().getChild1() instanceof SymNode))
      lNontermSym2 = (Subp)((SymNode)lNontermCallExp.getChild1()
                             .getChild1()).getSymNodeSym();
    else if (lNontermCallExp.getChild1() instanceof SymNode)
      lNontermSym2 = (Subp)((SymNode)lNontermCallExp.getChild1()).getSymNodeSym();
    if ((lNontermSym2 != null)&&
        fNonterminalSet.contains(lNontermSym2)) {
      lAdjustedResult = reformByChangingParam( pData,
              lNontermCallExp, lParamNode, lRewriteExp,
              pTraversedMatchingData, lNthInstance );
    }else {
      if (fDbgLevel > 0)
        dbgOut(1, "_reform parameter is not a nonterminal reference "
               + lNontermCallExp);
      lAdjustedResult = adjustOutPattern( pData, lNontermCallExp,
                  pTraversedMatchingData, lNthInstance );
    }
  }else {
    if (fDbgLevel > 0)
      dbgOut(1, "_reform parameter is not a nonterminal reference "
             + lNontermCallExp.toStringWithChildren());
    lAdjustedResult = adjustOutPattern( pData, lNontermCallExp,
              pTraversedMatchingData, lNthInstance );
  }
  return lAdjustedResult;
} // evaluateReform

/**
 * Get the HIR corresponding to pattern parameters
 * that are given as keys of pParamCorresp which
 * maps pattern parameters to primary HIR that may contain
 * parameters. The resultant HIR does not nontain
 * parameters any more.
 * In this processing, the correspondence of parameter
 * replacement sequence is traced and if the HIR
 * given by pData (as the one corresponding to a parameter)
 * contains a parameter node, then the parameter node is
 * replaced with corresponding HIR.
 * (This method is called in adjustNonterminal.)
 * @param pData The instance of MatchingData to be used
 *   in tracing the correspondence of parameters.
 * @param pParamCorresp Input correspondence of pattern
 *   parameters and replacement expressions.
 * @return Resultant correspondence of pattern parameters and
 *   replacement expressions.
 */
protected Map
adjustMatchingData( MatchingData pData, Map pParamCorresp )
{
  if (fDbgLevel > 1)
    dbgOut(3, "\nadjustMatchingData of " + pData.nontermPat);
  Map lParamCorresp = new HashMap();
  lParamCorresp.putAll(pParamCorresp);
  Set lKeys = pParamCorresp.keySet();
  for (Iterator lIt = lKeys.iterator();
       lIt.hasNext(); ) {
    Var lVar = (Var)lIt.next();
    HIR lParamValue = ((HIR)pParamCorresp.get(lVar)).copyWithOperands();
    if (lParamValue.getChildCount() == 0) {
      continue;
    }
    // Compound expression.
    Var lTracedParam = lVar;
    HIR lTracedHir = traceParamCorresp(pData, lVar, pData.matchedInput, true);
    // lTracedHir: HIR corresponding to lTracedParam.
    if (lTracedHir instanceof SymNode) {
      lTracedParam = (Var)((SymNode)lTracedHir).getSymNodeSym();
    }
    if (fDbgLevel > 1)
      dbgOut(4, "\n replace " + lVar.getName() + " ("
             + lTracedHir.toStringWithChildren()
             + ") in " + lParamValue.toStringWithChildren());
    HIR lMappedHirOfTracedParam = null;
    if (pData.paramCorresp.containsKey(lTracedParam))
      lMappedHirOfTracedParam = (HIR)pData.paramCorresp.get(lTracedParam);
    else if (pData.nontermParamCorresp.containsKey(lTracedParam))
      lMappedHirOfTracedParam = (HIR)pData.nontermParamCorresp.get(lTracedParam);
    // lTracedParam: One of pattern parameter in pParamCorresp.
    // lMappedHirOfTracedParam: HIR or pattern parameter node get
    //   by tracing the replacement sequence of parameters in pData.
    // lParamValue: HIR that is given as the one corresponding
    //   to the pattern parameter by pParamCorresp.
    //-- Replace lTracedParam nodes contained in lParamValue
    //   with lMappedHirOfTracedParam.
    if (lMappedHirOfTracedParam != null) {
       if (fDbgLevel > 1)
        dbgOut(4, " by " + lMappedHirOfTracedParam.toStringWithChildren());
      List lChangeList = new ArrayList();
      for (HirIterator lHirIt = hir.hirIterator(lParamValue);
           lHirIt.hasNext(); ) {
        HIR lNode = lHirIt.next();
        if ((lNode instanceof SymNode) &&
            (((SymNode)lNode).getSymNodeSym() == lTracedParam)) {
          lChangeList.add(lNode);
        }
      }
      for (Iterator lIt2 = lChangeList.iterator();
           lIt2.hasNext(); ) {
        Exp lNode2 = (Exp)lIt2.next();
        replaceExpAdjustingType(lNode2, (Exp)lMappedHirOfTracedParam.copyWithOperands());
      }
      putToMap(lParamCorresp, lTracedParam, lParamValue);
    } // (lMappedHirOfTracedParam != null)
  } // end of for-loop
  return lParamCorresp;
} // adjustMatchingData

/**
 * traceReplacementSeq gets replica HIR that is used to replace
 * pParam which may be either a nonterminal parameter or
 * a pattern parameter.
 * If the HIR to be used to replace the parameter contains
 * othter pattern/nonterminal parameters, then the contained
 * parameters are replaced. This process continues successively
 * until there remains no pattern/nonterminal parameters except
 * the case where some parameter is contained in the replica
 * HIR recursively.
 * @param pData Matching data used in the replacement.
 * @param pParam a pattern parameter or a nonterminal parameter.
 * @param pTracedParam Set of traced parameters.
 * @param pTraversedMatchingData Set of traversed matching data.
 * @param pNontermParamOnly true if replacement is to be done for
 *     nonterminal parameters and leave pattern parameters unchanged,
 *     false if both of pattern parameters and nonterminal parameters
 *     are to be replaced.
 * @return the replica to be used to replace pParam.
 */
protected HIR
traceReplacementSeq( MatchingData pData, // Subp pPatternSym,
  Sym pParam,
  Set pTracedParam, Set pTraversedMatchingData, boolean pNontermParamOnly )
{
  if (fDbgLevel > 0) {
    dbgOut(4, "\n traceReplacementSeq " + pData.nontermPat
           + " " + pParam.getName());
    dbgOut(5, " " + dbgMap(pData.paramCorresp) + " "
           + dbgMap(pData.nontermParamCorresp));
  }
  pTraversedMatchingData.add(pData);
  MatchingData lData
    = new MatchingData(pData.nontermPat,  pData);
  HIR lReplacer;
  if (pNontermParamOnly&&
      lData.nontermParamCorresp.containsKey(pParam))
    lReplacer = (HIR)lData.nontermParamCorresp.get(pParam);
  else
    lReplacer = (HIR)lData.paramCorresp.get(pParam);
  if (lReplacer == null) {
    if (fDbgLevel > 3)
      dbgOut(4, " no value for " + pParam.getName());
    return hir.varNode((Var)pParam);
  }
  Set lOperands = getLeafOperands(lReplacer);
  HIR lReplacerCopy = lReplacer.copyWithOperandsChangingLabels(null);
  if (lOperands.contains(pParam)) {
    return lReplacerCopy;
  }
  pTracedParam.add(pParam);
  HirIterator lItCopy = hir.hirIterator(lReplacerCopy);
  for (HirIterator lIt = hir.hirIterator(lReplacer);
       lIt.hasNext(); ) {
    HIR lHir = lIt.next();
    HIR lHirCopy = lItCopy.next();
    if (fDbgLevel > 3)
      dbgOut(5, " " + lHir.toStringShort());
    if (lHir instanceof SymNode) {
      Sym lSym = ((SymNode)lHir).getSymNodeSym();
      if (pNontermParamOnly&&
          (! isFittingSym(lSym))&&
          (! isNontermParam(lSym))) {
        continue;
      }
      if ((isPatternParam(lSym)||
           isFittingSym(lSym))&&
          lData.paramCorresp.containsKey(lSym)) {
        // lSym is a pattern parameter that may appear in
        // out-pattern directly.
        // Replace this node by the corresponding actual parameter
        // and put it to pData.paramCorresp.
        HIR lActualArg = (HIR)lData.paramCorresp.get(lSym);
        // lHirCopy.replaceThisNode(lActualArg);
        replaceHirTree(lHirCopy, lActualArg.copyWithOperands());
        // Modify lReplacer whose components are iterated.
        // Is it OK ?
        /* //###
        if ((!(lReplacerCopy instanceof SymNode))||
            (((SymNode)lReplacerCopy).getSymNodeSym() != lSym)) {
          putToMap(lData.paramCorresp, lSym, lReplacerCopy);
           if (fDbgLevel > 0)
            dbgOut(4, "\n  modify the value for " + lSym.getName()
              + " to " + lReplacerCopy.toStringWithChildren());
        }
        */ //###
        if ((!(lReplacerCopy instanceof SymNode))||
            (((SymNode)lReplacerCopy).getSymNodeSym() != pParam)) {
          putToMap(lData.paramCorresp, pParam, lReplacerCopy);
           if (fDbgLevel > 0)
            dbgOut(4, "\n  modify the value for " + pParam.getName()
              + " to " + lReplacerCopy.toStringWithChildren());
        }
      }else {
        if (lData.nontermParamCorresp.containsKey(lSym) &&
            (!pTracedParam.contains(lSym))) {
          HIR lReplacer2 =
            traceReplacementSeq(lData,
            lSym, pTracedParam,
            pTraversedMatchingData, pNontermParamOnly );
          replaceHirTree(lHirCopy, lReplacer2.copyWithOperands());
        }
      }
    }
  }
  HIR lNewReplacer = lReplacerCopy;
  if (! pTraversedMatchingData.contains(pData)) {
    lNewReplacer = replaceParameters(lData, lReplacerCopy,
      pTraversedMatchingData, true);
  }
  pTraversedMatchingData.remove(pData);
  return replaceParameters(lData,  lNewReplacer, pTraversedMatchingData,
    pNontermParamOnly);
} // traceReplacementSeq

/**
 * Trace the sequence of nonterminal parameter replacement
 * and return the HIR that corresponds to the parameter
 * pParamInstance directly or indirectly.
 * The result will not contain nonterminal parameters but may
 * contain pattern parameter or the result does not contain
 * pattern/nonterminal parameters.
 * pData.succeeded is set true if no discrepancy was found,
 * false otherwise.
 * At first, the sequence of nonterminal parameter replacement
 * is traced. It stops to trace when a pattern parameter is
 * reached or an HIR that is not a parameter node is reached.
 * The 2nd phase differs according to the value of pAdjustPhase.
 * If pAdjustPhase is false (called in matching phase), then
 *   if pParamInstance is a pattern parameter and the HIR
 *   corresponding to pParamInstance takes the same form
 *   as pMatchedHir, then the result is a parameter node
 *   representing pParamInstance and pData.succeeded is
 *   set true.
 *   If the HIR corresponding to pParamInstance has different
 *   form compared to pMatchedHir, then the result is a
 *   parameter node representing pParamInstance and
 *   pData.succeeded is false.
 * If pAdjustPhase is true (called in adjustOutPattern
 *   directly or indirectly), then the result is the parameter
 *   node representing pParamInstance if pParamInstance is
 *   a pattern parameter or the result is the copy of pMatchedHir
 *   if not. In any case pData.succeeded is set true if
 *   pAdjustPhase is true..
 * The resultant HIR is adjusted according to the type of
 * pParamInstance by applying decay/undecay/conv operation
 * if necessary.
 * If discrepancy was found in the above processing, then
 * the the result may be wrong.
 * @param pData Matching data to be used in tracing the replacement
 *     sequence.
 * @param pParamInstance Instance of nonterminal parameter or pattern parameter.
 * @param pMatchedHir Input HIR that matched to pParam.
 * @param pAdjustPhase true if called in adjustOutPattern/adjustNonterminal,
 *     false otherwize.
 * @return (copy of) the HIR corresponding to pParamInstance
 *     or a parameter node representing pParamInstance.
 */
protected HIR
traceParamCorresp( MatchingData pData, Var pParamInstance,
  HIR pMatchedHir, boolean pAdjustPhase )
{
  if (fDbgLevel > 3) {
    dbgOut(4, "\n traceParamCorresp " + pParamInstance.getName()
           + " of " + pData.nontermPat + " " + pMatchedHir);
    dbgOut(6, " by " + dbgMap(pData.paramCorresp) +
           " " + dbgMap(pData.nontermParamCorresp));
  }
  HIR lResult = null;
  // Var lParam = pParam;
  Var lOriginalParam = getOriginalVar(pParamInstance);
  Var lParamInstance = getVarInstance(pParamInstance, pData.nontermPat);;
  HIR lHir = null;
  while (pData.nontermParamCorresp.containsKey(lParamInstance)) {
    lHir = (HIR)pData.nontermParamCorresp.get(lParamInstance);
    if (lHir instanceof SymNode) {
      Sym lSym = ((SymNode)lHir).getSymNodeSym();
      if (fDbgLevel > 3)
        dbgOut(6, " lSym " + lSym.getName());
      if ((lSym == pParamInstance)||
          (lSym == lParamInstance))
        break;
      if ((isNontermParam(lSym)||
           isPatternParam(lSym))) {
        // Trace the symbol corresponding to lParamInstance.
        //lParam = (Var)lSym;
        lOriginalParam = getOriginalParam((Param)lSym);
        lParamInstance = getParamInstance((Param)lSym, pData.nontermPat);
        if ((! pAdjustPhase)&&isPatternParam(lOriginalParam))
          break;
      }
      else
        break;
    }else
      break;
  }
  //##81 BEGIN
  if (pAdjustPhase) {
    if (lHir != null) {
      if (isPatternParam(lOriginalParam)) {
        lResult = hir.varNode(lParamInstance); // Return pattern param node.
      }else // Return HIR corresponding to the parameter.
        lResult = lHir.copyWithOperandsChangingLabels(null);
    }else {
      // lHir == null. Correspondence is not yet given.
      lResult = hir.varNode(lParamInstance);
    }
    pData.succeeded = true;
  }else { // ! pAdjustPhase
  //##81 END
    if (((isPatternParam(lOriginalParam))||
         isFittingSym(lOriginalParam))&&
        pData.paramCorresp.containsKey(lParamInstance)) { // lParam is
      // already registered and it is either pattern param or fitting symbol.
      if (isSameTree((HIR)pData.paramCorresp.get(lParamInstance), pMatchedHir)) { //##81
        lResult = hir.varNode(lParamInstance);
        pData.succeeded = true;
     }else {  //##81
        if (fDbgLevel > 0)
          dbgOut(2, "\n In traceParamCorresp, previous value "
               + pData.paramCorresp.get(lParamInstance)
               + " of " + lParamInstance.getName() + " does not match with "
               + pMatchedHir.toStringWithChildren());
        // Establish new matching.
        lResult = pMatchedHir.copyWithOperandsChangingLabels(null);
        pData.succeeded = false;
      }
    }else if (lHir != null) {
      if (isPatternParam(lOriginalParam)||
          (lParamInstance == pParamInstance)||
          (lOriginalParam == pParamInstance)) {
        lResult = hir.varNode(lParamInstance); // Return pattern parameter node.
        pData.succeeded = true;
      }else {
        MatchingData lData1 = new MatchingData(pData.nontermPat, pData);
        lResult = traceParamCorresp(lData1, lParamInstance,
                    pMatchedHir, pAdjustPhase);
        if (lData1.succeeded) {
          pData.reflect(lData1);
        }else
          pData.succeeded = false;
      }
    }else {
      // lHir == null. HIR correspondence is not yet given.
      lResult = hir.varNode(lParamInstance);
      pData.succeeded = true;
    }
  }
  // Adjust the result according to the type of pParamInstance
  // by applying decay/undecay/conv operation if necessary.
  Type lParamType = pParamInstance.getSymType();
  Type lResultType = lResult.getType();
  if (lResultType != lParamType) {
    if (fDbgLevel > 1)
      dbgOut(4, "\n  adjust type " + lResultType +
             " to " + lParamType);
    if ((lParamType instanceof PointerType)&&
        (lResultType instanceof VectorType)) {
      lResult = hir.decayExp((Exp)lResult);
    }else if ((lParamType instanceof VectorType)&&
              (lResultType instanceof PointerType)) {
      lResult = hir.undecayExp((Exp)lResult,
        ((VectorType)lParamType).getElemCount());
    }else {
      if (lResult instanceof Exp)
        lResult = hir.convExp(lParamType, (Exp)lResult);
    }
  }
  if (fDbgLevel > 3) {
    dbgOut(4, "\n traceParamCorresp result= " + pParamInstance.getName()
           + " org " + lOriginalParam
           + " of " + pData.nontermPat +
           " = " + lResult.toStringWithChildren());
  }
  return lResult;
} // traceParamCorresp

protected HIR
traceParamCorrespSimply( MatchingData pData, Var pParamInstance )
{
  if (fDbgLevel > 3) {
    dbgOut(4, "\n traceParamCorrespSimpleMode " + pParamInstance.getName()
           + " of " + pData.nontermPat);
    dbgOut(6, " by " + dbgMap(pData.paramCorresp) +
           " " + dbgMap(pData.nontermParamCorresp));
  }
  HIR lResult = null;
  // Var lParam = pParam;
  Var lOriginalParam = getOriginalVar(pParamInstance);
  Var lParamInstance = getVarInstance(pParamInstance, pData.nontermPat);;
  HIR lHir = null;
  while (pData.nontermParamCorresp.containsKey(lParamInstance)) {
    lHir = (HIR)pData.nontermParamCorresp.get(lParamInstance);
    if (lHir instanceof SymNode) {
      Sym lSym = ((SymNode)lHir).getSymNodeSym();
      if (fDbgLevel > 3)
        dbgOut(6, " lSym " + lSym.getName());
      if ((lSym == pParamInstance)||
          (lSym == lParamInstance))
        break;
      if ((isNontermParam(lSym)||
           isPatternParam(lSym))) {
        // Trace the symbol corresponding to lParamInstance.
        // lParam = (Var)lSym;
        lOriginalParam = getOriginalParam((Param)lSym);
        lParamInstance = getParamInstance((Param)lSym, pData.nontermPat);
      }
      else
        break;
    }else
      break;
  }
  lResult = lHir;
  if (fDbgLevel > 3) {
    dbgOut(4, "\n traceParamCorrespSimpleMode result= "
           + lResult);
  }
  return lResult;
} // traceParamCorrespSimpleMode

/**
 * If a nonterminal param is mapped to a pattern param
 * directly or indirectly (by tracing the sequence of
 * nontermParam-to-nontermParam correspondence),
 * then record the HIR corresponding to the nonterm parameter
 * as the HIR corresponding to the pattern parameter.
 * @param pData Matching data in which nontermParam-to-nontermParam
 *     correspondence is to be traced.
 */
protected void
makeParamCorrespComplete( MatchingData pData )
{
  if (fDbgLevel > 2) {
    dbgOut(3, "\nmakeParamCorrespComplete " + pData.nontermPat);
    dbgOut(4, "\n  paramCorresp " + dbgMap(pData.paramCorresp)
           + "\n  nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
  }
  //##86 BEGIN
  if (pData.madeComplete) {
    if (fDbgLevel > 2)
      dbgOut(3, " It is already made complete.");
    return;
  }
  //##86 END
  boolean lAdded = false;
  Set lParamCorrespKeys = pData.paramCorresp.keySet();
  Set lNontermParamCorrespKeys = pData.nontermParamCorresp.keySet();
  for (Iterator lIt1 = lNontermParamCorrespKeys.iterator();
       lIt1.hasNext(); ) {
    Sym lNtParam1 = (Sym)lIt1.next(); // Nonterminal parameter.
    Object lObj1 = pData.nontermParamCorresp.get(lNtParam1);
    if (fDbgLevel > 1)
      dbgOut(5, " " + lNtParam1.getName() + "="
             + ((HIR)lObj1).toStringWithChildren());
    Set lScanned = new HashSet();
    Sym lPatternParam = null;
    if (! lParamCorrespKeys.contains(lNtParam1)) {
      // The nonterminal param is not recorded in pData.paramCorresp.
      continue;  // Skip it.
    }
    // lNtParam1 has correspondence with an expression in the input program.
    while (lObj1 instanceof SymNode) {
      Sym lSym1 = ((SymNode)lObj1).getSymNodeSym();
      if (lScanned.contains(lSym1)) {
        if (fDbgLevel > 0)
          dbgOut(1, "\n In completing paramCorresp, recursion occured by "
                 + lSym1 + " for " + lNtParam1);
        break;
      }
      lScanned.add(lSym1);
      // lSym1: symbol mapped by the nonterminal parameter.
      if (isPatternParam(lSym1)) {
        // lNtParam1 is mapped to a pattern param.
        lPatternParam = lSym1;
        break;
      }else {
        lScanned.add(lNtParam1);
        if ((lSym1 instanceof Param)&&
            lNontermParamCorrespKeys.contains(
              getParamInstance((Param)lSym1, pData.nontermPat))) {
          // lNtParam1 is mapped to a nonterminal param.
          lObj1 = pData.nontermParamCorresp.get(lSym1);
        }else {
          lObj1 = null;
          lPatternParam = null;
        }
      }
    }
    if (lPatternParam != null) {
      // lNtParam1 is ultimately mapped to a pattern param lPatternParam.
      // Record the correspondence of the pattern param and
      // HIR corresponding to lNtParam1.
      putToMap(pData.paramCorresp, lPatternParam,
          (HIR)pData.paramCorresp.get(
            getParamInstance((Param)lNtParam1, pData.nontermPat)));
      lAdded = true;
    }
  }
  if (lAdded && (fDbgLevel > 1)) {
    dbgOut(4, "\n Revised paramCorresp " + dbgMap(pData.paramCorresp));
  }
} // makeParamCorrespComplete

/**
 * Replace nonterminal/pattern parameters and fitting symbols
 * contained in pOutHi by the HIR correnponding to the parameters
 * or fitting symbols where the correspondence is given by pData.
 *
  if (pOutHir instanceof SymNode) {
    if (it is a parameter or fitting symbol) {
      if (pNontermParamOnly&&(it is a pattern parameter)) {
        return pOutHir; // Do not replace pattern parameter.
      }
      It is a parameter or fitting symbol.
      Get the HIR subtree that corrresponds to the parameter
        in the form all parameters contained in it are already replaced
        by using traceReplacementSeq.
      if (its type is not VectorType) {
        Replace the node by the HIR subtree obtained by traceReplacementSeq.
      }else {
        It is an array parameter (parameter node of VectorType).
        Construct the resultant HIR subtree by replacing
          the parameter node or its parent expression
          considering that the array parameters
          may appear in such form as
            (subs <var varOfArrayType> subscriptExp)
            (subs (undecay varOfPointerType elemCount) subscriptExp)
            (contents (add <varOfPointerType> (mult elemSize subscriptExp)))
            <var varOfArrayType>
            <var varOfPointerType>
            (decay arrayExp)
            (addr variableExp)
            (conv <var varOfArrayAType>)
           (list <var varOfArrayType> ... )
           (list <var varOfPointerType> ... )
    }else {
      Other symbol node.
      if (the symbol is a variable  that is not defined in the
          subprogram under transformation) {
          (! lCorrespKeySet.contains(lSym))) {
        Generate a temporal variable and replace the variable node
          and register the correspondence in pParamCorresp.
      }else if (correspondence is given in pData) {
        Make the resultant expression by getting the expression
          corresponding to the symbol using pData.
      }
    }
  } // end of SymNode
  else if (pOutHir instanceof HirList) {
    Make a list by replacing all of its elements.
  }else if (pOutHir instanceof BlockStmt) {
     Make a block by replacing all statements contained in it.
  }else if (pOutHir instanceof LabeledStmt) {
    Make a labeled statement generating new label and replacing is body statement.
  }else {
    For other HIR having children, make the resultant HIR
      by replacing its children.
  }
  if (succeeded to get the resultant HIR lReplica) {
    return lReplica;
  }else {
      return pOutHir;
  }
 *
 * @param pData Matching data giving the correspondence of
 *     parameters and HIR subtrees.
 * @param pOutHir HIR subtree whose parameter nodes are
 *     to be replaced (copy should be given if original HIR
 *     subtree should not be changed).
 * @param pTraversedMatchingData Set of matching data already
 *     traversed (used to escape from infinite loop).
 * @param pNontermParamOnly Set true if only nonterminal
 *     parameter nodes are to be replaced and leave pattaern
 *     parameter nodes are to be left unchanged, set false if
 *     both of nonterminal/pattern parameter nodes are to be
 *     replaced.
 * @return the result of replacement (return pOutHir if no
 *     replacement is taken).
 */
protected HIR
replaceParameters( MatchingData pData, // Subp pPatternSym,
             HIR pOutHir, Set pTraversedMatchingData,
           boolean pNontermParamOnly  )
{
  pTraversedMatchingData.add(pData);
  if (pOutHir == null)
    return pOutHir;
  boolean lNoParent = false;
  if (pOutHir.getParent() == null)
    lNoParent = true;
  Set lCorrespKeySet = new HashSet();
  lCorrespKeySet.addAll(pData.paramCorresp.keySet());
  lCorrespKeySet.addAll(pData.nontermParamCorresp.keySet());
  if (fDbgLevel > 2) {
    dbgOut(3, "\nreplaceParameters " + pOutHir.toStringShort()
           + " " + pNontermParamOnly + " lNoParent " + lNoParent);
    if (fDbgLevel > 4) {
      dbgOut(5, " correspKey " + dbgSet(lCorrespKeySet));
    }
  }
  int lOpCode = pOutHir.getOperator();
  int lChildCount = pOutHir.getChildCount();
  if (pOutHir instanceof SymNode) {
    // It may be a parameter.
    HIR lReplica = null;
    Sym lSym = ((SymNode)pOutHir).getSymNodeSym();
    if ((lSym instanceof Param)||
        isFittingSym(lSym)) {
      if (fDbgLevel > 1)
        dbgOut(5, "\n  paramCorresp " + dbgMap(pData.paramCorresp)+
          "\n  nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
      if (pNontermParamOnly&&
          (! isFittingSym(lSym))&&
          (! isNontermParam(lSym))) {
        // This is not a nonterminal parameter.
        // Do not replace.
        if (fDbgLevel > 3)
          dbgOut(5, " non-nonterminal parameter "
                 + lSym.getName());
        return pOutHir;
      }
      // lSym is a parameter.
      // Get the expression/statement lReplica corresponding to it.
      if (lCorrespKeySet.contains(lSym)&&
          (! (lSym.getSymType() instanceof VectorType))
          // &&(pData.paramCorresp.get(lSym) != null)
          ) {
        Set lTracedParam = new HashSet();
        lReplica = traceReplacementSeq(pData, lSym,
            lTracedParam, pTraversedMatchingData, pNontermParamOnly);
        // It is registered in pParamCorresp.
        if (fDbgLevel > 2)
          dbgOut(5, "\n Replace " + lSym.getName() + " by " + lReplica);
        // Replacement may already been executed.
        // Eccessive replacement should be avoided.
        // REFINE
        // Do not replace parent because such replacement will
        // destroy the control structure of HIR traversing
        // such as getNextStmt() or HirIterator.
        /*
        if ((lReplica instanceof Stmt) &&
            (fStmtParamSet.contains(lSym))) {
          if (fStmtParamSet.contains(lSym) &&
              (pOutHir.getParent() instanceof ExpStmt)) {
            // Statement parameter. Replace ExpStmt.
            lReplica = ((Stmt)pOutHir.getParent()).replaceThisStmtWith((Stmt)
              lReplica);
          }
          else {
            lReplica = ((Stmt)pOutHir).replaceThisStmtWith((Stmt)lReplica);
          }
          if (lNoParent) {
            return lReplica;
          }
        }
        else
        */
        if (lReplica instanceof Exp) {
          lReplica = replaceExpAdjustingType((Exp)pOutHir, (Exp)lReplica);
          if (lNoParent)
            return lReplica;
        }
        else if (lReplica == null) {
          if (pOutHir instanceof Stmt) {
            ((Stmt)pOutHir).deleteThisStmt();
          }
          else {
            pOutHir.replaceThisNode(null);
          }
        }
        else {
          ioRoot.msgRecovered.put(5122, "Irregal replacement of " +
            pOutHir.toString() + " with " + lReplica.toStringWithChildren());
        }
      }
      else if ((lSym.getSymType() instanceof VectorType)&&
                lCorrespKeySet.contains(lSym)
                ) {
        // It is an array parameter.
        Set lTracedParam = new HashSet();
        HIR lNewNode = traceReplacementSeq(pData, lSym,
            lTracedParam, pTraversedMatchingData, pNontermParamOnly);
        if (fDbgLevel > 2)
          dbgOut(5, "\n replace array elem "
                 + lSym.getName() + " " + lNewNode);
        // Replace lNode or its parent expression.
        HIR lParent = (HIR)pOutHir.getParent();
        int lOpCodeP = lParent.getOperator();
        int lOpCodeN = lNewNode.getOperator();
        // Consider the combinations of
        //   (subs <var varOfArrayType> subscriptExp)
        //   (subs (undecay <var varOfPointerType> elemCount) subscriptExp)
        //   (contents (add <var varOfPointerType> (mult elemSize subscriptExp)))
        //   <var varOfArrayType>
        //   <var varOfPointerType>
        //   (decay arrayExp)
        //   (undecay <var varOfPointerType> elemCount)
        //   (addr variableExp)
        //   (list <var varOfArrayType> ... )
        //   (list <var varOfPointerType> ... )
        // as lParent and lNewNode.
        if ((lOpCodeP == HIR.OP_ADD) && (lParent.getParent() != null) &&
            (lParent.getParent().getOperator() == HIR.OP_CONTENTS) &&
            (lParent.getChild2().getOperator() == HIR.OP_MULT)) {
          // Exp containing lNode in out-pattern:
          //   (contents (add lNode (mult elemSize subscriptExp)))
          if ((lNewNode instanceof VarNode) &&
              ((lNewNode.getParent() == null) ||
               (lNewNode.getParent().getOperator() == HIR.OP_SUBS)) &&
              (lNewNode.getType()instanceof VectorType)) {
            // Exp containing lNewNode corresponding to lNode:
            //   (subs lNewNode subscriptExp)

         HIR lSubscript =replaceParameters(pData, // pPatternSym,
              (HIR)lParent.getChild2().getChild2(), pTraversedMatchingData,
              pNontermParamOnly);
            Exp lArrayElem = hir.subscriptedExp((Exp)lNewNode.
              copyWithOperands(),
              (Exp)lSubscript);
            replaceExpAdjustingType((Exp)lParent.getParent(), lArrayElem);
          }
          else if ((lNewNode instanceof VarNode) &&
            (lNewNode.getParent() != null) &&
            (lNewNode.getParent().getOperator() == HIR.OP_ADD) &&
            (lNewNode.getParent().getParent() != null) &&
            (lNewNode.getParent().getParent().getOperator()
             == HIR.OP_CONTENTS) &&
            (lNewNode.getParent().getChild2().getOperator()
             == HIR.OP_MULT)) {
            // Exp containing lNewNode corresponding to lNode:
            //   (contents (add lNewNode (mult elemSize subscriptExp)))
            Exp lSubscript = (Exp)replaceParameters(pData, // pPatternSym,
              (HIR)lNewNode.getParent().getChild2().getChild2(),
              pTraversedMatchingData, pNontermParamOnly);
            Exp lElemSize = (Exp)replaceParameters(pData, // pPatternSym,
              (HIR)lNewNode.getParent().getChild2().getChild1(),
              pTraversedMatchingData, pNontermParamOnly);
            Exp lOffset = hir.exp(HIR.OP_MULT, lElemSize, lSubscript);
            lReplica = hir.exp(HIR.OP_CONTENTS,
              hir.exp(HIR.OP_ADD, (Exp)lNewNode.copyWithOperands(),
              lOffset));
            replaceExpAdjustingType((Exp)lParent.getParent(),
              (Exp)lReplica);
          }
          else if ((lNewNode instanceof VarNode) &&
            (lNewNode.getParent() != null) &&
            (lNewNode.getParent().getOperator() == HIR.OP_SUBS)) {
            // REFINE
          }
        }
        else if ((lOpCodeP == HIR.OP_UNDECAY) &&
                 (lParent.getParent() != null) &&
                 (lParent.getParent().getOperator() == HIR.OP_SUBS) &&
                 (lNewNode.getType()instanceof VectorType) &&
                 (lNewNode.getParent() != null) &&
                 (lNewNode.getParent().getOperator() == HIR.OP_SUBS)) {
          // Exp containing lNode in out-pattern:
          //   (subs (undecay lNode elemCount) subscriptExp)
          // Exp containing lNewNode to be used for replacing lNode:
          //   (subs lNewNode suscriptExp)
          Exp lArray = (Exp)lNewNode.copyWithOperands();
          Exp lSubscript = (Exp)replaceParameters(pData, // pPatternSym,
            (Exp)lNewNode.getParent().getChild2(),
            pTraversedMatchingData, pNontermParamOnly);
          lReplica = hir.subscriptedExp(lArray, lSubscript);
          lReplica = replaceExpAdjustingType((Exp)lParent.getParent(),
            (Exp)lReplica);
        }
        else if (lOpCodeP == HIR.OP_LIST) {
          // lNode is used as parameter list.
          lReplica = replaceExpAdjustingType((Exp)pOutHir,
            (Exp)lNewNode.copyWithOperands());
        }
        else if (lOpCodeP == HIR.OP_CONV) {
          // lNode type is adjusted.
          lReplica = replaceExpAdjustingType((Exp)pOutHir,
            (Exp)lNewNode.copyWithOperands());
        }
        else {
          // REFINE
          lReplica = replaceExpAdjustingType((Exp)pOutHir,
            (Exp)lNewNode.copyWithOperands());
        }
      }
      else {
        dbgOut(2, " No corresponcdence to " +
               lSym.getName());
      }
    } // end of param sym
    else {
      // Other symbol.
      if ((lSym instanceof Var) &&
          (! lCorrespKeySet.contains(lSym))) {
        // Generate a temporal variable for a variable
        // that is not defined in the subprogram under transformation
        // and register the correspondence in pParamCorresp.
        if (symRoot.symTableCurrentSubp.search(
          lSym.getName().intern(), lSym.getSymKind()) == null) {
          Var lTempVar =
            symRoot.symTableCurrentSubp.generateVar(lSym.getSymType());
          pData.paramCorresp.put(lSym, lTempVar);
          if (fDbgLevel > 0)
            dbgOut(2, " put(" + lSym.getName() + "," + lTempVar.getName() + ")");
          lCorrespKeySet.add(lSym);  //##91
        }
      }
      if (lCorrespKeySet.contains(lSym)) {
        Var lTempVar = null;
        Object lObj;
        if (pNontermParamOnly &&
            pData.nontermParamCorresp.containsKey(lSym))
          lObj = pData.nontermParamCorresp.get(lSym);
        else
          lObj = pData.paramCorresp.get(lSym);
        if (lObj instanceof Var) {
          lTempVar = (Var)lObj;
          lReplica = hir.varNode(lTempVar);
        }
        else if (lObj instanceof Exp) {
          lReplica = ((Exp)lObj).copyWithOperands();
        }
        lReplica = replaceExpAdjustingType((Exp)pOutHir, (Exp)lReplica);
        if (lNoParent)
          return lReplica;
      }
    }
    if (lReplica != null) {
      //## lReplica = replaceParameters(pData, pPatternSym,
      //      lReplica, pParamRewrite); // ??
      return lReplica;
    }else {
      return pOutHir;
    }
  } // end of SymNode
  else if (lChildCount <= 0) {
    if (pOutHir instanceof HirList) {
      HirList lNewList = hir.hirList();
      for (Iterator lIt = ((IrList)pOutHir).iterator();
           lIt.hasNext(); ) {
        HIR lElem = (HIR)lIt.next();
        if (lElem != null) {
          HIR lNewElem = replaceParameters(pData,//  pPatternSym,
            lElem, pTraversedMatchingData, pNontermParamOnly);
          //##91 lNewList.add(lElem);
          lNewList.add(lNewElem);  //##91
        }else
          lNewList.add(null);
      }
      return lNewList;
    }else {
      // return pOutHir;
    }
  }else if (lOpCode == HIR.OP_BLOCK) {
    BlockStmt lNewBlock = hir.blockStmt(null);
    Stmt lStmt = ((BlockStmt)pOutHir).getFirstStmt();
    Stmt lNextStmt;
    while (lStmt != null) {
      lNextStmt = lStmt.getNextStmt();
      Stmt lNewStmt = (Stmt)replaceParameters(pData, // pPatternSym,
        lStmt, pTraversedMatchingData, pNontermParamOnly);
      lNewStmt.cutParentLink(); //##81
      lNewBlock.addLastStmt(lNewStmt);
      lStmt = lNextStmt;
    }
    return lNewBlock;
  }else if (lOpCode == HIR.OP_LABELED_STMT) {
    LabeledStmt lNewLabeledStmt = (LabeledStmt)pOutHir.hirNodeClone();
    lNewLabeledStmt.setChild1((HIR)((HIR)pOutHir.getChild1())
      .copyWithOperandsChangingLabels(null));
    HIR lStmt = (HIR)pOutHir.getChild2();
    if (lStmt != null) {
      lStmt = replaceParameters(pData, // pPatternSym,
        lStmt, pTraversedMatchingData, pNontermParamOnly);
      lNewLabeledStmt.setChild2(lStmt);
    }
    return lNewLabeledStmt;
  /*
  //##91 BEGIN
  }else if (lOpCode == HIR.OP_LIST) {
    HirList lNewHirList = hir.hirList();
    for (java.util.ListIterator lIt = ((HirList)pOutHir).iterator();
         lIt.hasNext(); ) {
      Object lElem = lIt.next();
      if (lElem instanceof HIR) {
        HIR lNewElem = replaceParameters(pData,
          (HIR)lElem, pTraversedMatchingData, pNontermParamOnly);
        lNewHirList.add(lNewElem);
      }else {
        lNewHirList.add(lElem); // should be clone
      }
     }
     if (fDbgLevel > 3)
       dbgOut(5, "\n  return " + lNewHirList.toStringWithChildren());
     return lNewHirList;
  //##91 END
  */
  }else {
    HIR lNewHir = pOutHir.hirNodeClone();
    for (int lChildIndex = 1; lChildIndex <= lChildCount;
         lChildIndex++) {
      HIR lChild = (HIR)pOutHir.getChild(lChildIndex);
      if (lChild != null) {
        HIR lNewChild = replaceParameters(pData, // pPatternSym,
          lChild, pTraversedMatchingData, pNontermParamOnly);
        if (lNewChild.getOperator() == HIR.OP_EXP_STMT) {
          if ((pOutHir instanceof LoopStmt)||
              (pOutHir instanceof BlockStmt)||
              (pOutHir instanceof LabeledStmt)) {
          }else {
            if (fDbgLevel > 1)
              dbgOut(4, " peel off ExpStmt of " + lChild.toStringWithChildren());
            lNewChild = (HIR)lNewChild.getChild1();
          }
        }
        if ((lChild instanceof Exp)&&
            (lNewChild instanceof ExpStmt)) {
          lNewHir.setChild(lChildIndex, lNewChild.getChild1());
        }else {
          lNewHir.setChild(lChildIndex, lNewChild);
        }
      }
    }
    if (fDbgLevel > 3)
      dbgOut(5, "\n  return " + lNewHir.toStringWithChildren());
    return lNewHir;
  }
  if (fDbgLevel > 3)
    dbgOut(5, "\n  return " + pOutHir.toStringWithChildren());
   return pOutHir;
} // replaceParameters

 /**
  * Replace the node of statement parameter with the sequence
  * of statements corresponding to it.
  * @param pData MatchingData to be used.
  * @param pSymNode Statement parameter node.
  * @param pTraversedMatchingData Set of matching data already
  *     traversed (used to escape from infinite loop).
  * @param pNontermParamOnly Set true if only nonterminal
  *     parameter nodes are to be replaced and leave pattaern
  *     parameter nodes are to be left unchanged, set false if
  *     both of nonterminal/pattern parameter nodes are to be
  *     replaced.
  * @return The replaced result.
  */
 protected HIR
 replaceStatementParameter( MatchingData pData,
              SymNode pSymNode, Set pTraversedMatchingData,
            boolean pNontermParamOnly  )
 {
   pTraversedMatchingData.add(pData);
   if (fDbgLevel > 2) {
     dbgOut(3, "\nreplaceStatementParameter " + pSymNode.toStringShort()
            + " " + pNontermParamOnly );
     dbgOut(5, "\n  paramCorresp " + dbgMap(pData.paramCorresp)+
        "\n  nontermParamCorresp " + dbgMap(pData.nontermParamCorresp));
   }
   Set lCorrespKeySet = new HashSet();
   lCorrespKeySet.addAll(pData.paramCorresp.keySet());
   lCorrespKeySet.addAll(pData.nontermParamCorresp.keySet());
   HIR lReplica = null;
   Sym lSym = pSymNode.getSymNodeSym();
   if (lSym instanceof Param){
     // Get the statement corresponding to it.
     if (lCorrespKeySet.contains(lSym)) {
         Set lTracedParam = new HashSet();
         lReplica = traceReplacementSeq(pData, lSym,
             lTracedParam, pTraversedMatchingData, pNontermParamOnly);
         // It is registered in pParamCorresp.
        return lReplica;
     }
   }
   if (fDbgLevel > 0)
     dbgOut(1, "\n Parameter " + pSymNode.toStringShort()
            + " is illegal for replaceStatementParameter");
   return lReplica;
 } // replaceStatementParameter

/**
 * reformByChangingParam does the transformation specified by
 *   _reform(n_th_instance, patternParam, replacementExp,
 *            nonterminalCallExp).
 * (1) Get the nonterminal instance specified by
 *    nonterminalCallExp and n_th_instance.
 * (2) Get the matching data corresponding to the
 *    nonterminal instance.
 * (2) Replace all parameter nodes same as pParamNode in
 *    pRewriteExp by the corresponding HIR given by the
 *    matching data and record the revised rewrite-expression
 *    as the new HIR corresponding to the parameter
 *    in local MatchingData.
 *    pRewriteExp may contain pattern parameters and
 *    _assignStmt function.
 * (3) Adjust the expanded HIR of the nonterminal instance
 *    using the revised local MatchingData (by adjustnonterminal).
 * (4) Return the result of adjustNonterminal.
 * (This method is called in adjustOutPattern.)
 * @param pData Matching data to be used.
 * @param pNontermCall Nonterminal call expression specified
 *     in _reform expression.
 * @param pParamNode Parameter node to be rewritten as
 *     as specified in _reform expression.
 * @param pRewriteExp HIR to be used to replace pParamNode
 *     as specified in _reform expression.
 * @param pTraversedMatchingData Set of matching data already
 *     traversed (used to convey information to adjustNonterminal).
 * @param pNthInstance Sequence number of nonterminal instance
 *     among the instances having the same form of call expression
 *     in the in-pattern.
 * @return The resultant HIR subtree for the given _reform expression.
 */
 protected HIR
 reformByChangingParam( MatchingData pData, HIR pNontermCall,
     HIR pParamNode, HIR pRewriteExp,
     Set pTraversedMatchingData, int pNthInstance )
{
  if (fDbgLevel > 2)
    dbgOut(5, "\nreformByChangingParam " + pData.nontermPat
      + " " + pNontermCall.toStringWithChildren()
      + " param " + pParamNode.toStringShort()
      + " exp " + pRewriteExp.toStringWithChildren()
      + " " + pNthInstance +"-th instance ");
  HIR lRewriteExp = pRewriteExp.copyWithOperands();
  //-- Get the nonterminal instance (lNontermInstance).
  NontermPatInstance lNontermInstance = null;
  List lNontermInstanceList = null;
  HIR lKey = makeHirKey(pNontermCall);
  if (fNontermInstanceInPattern.containsKey(lKey)) {
    lNontermInstanceList = (List)fNontermInstanceInPattern.get(lKey);
    if (lNontermInstanceList.size() > pNthInstance) {
      lNontermInstance
        = (NontermPatInstance)lNontermInstanceList.get(pNthInstance);
    }else if (! lNontermInstanceList.isEmpty()) {
      ioRoot.msgRecovered.put("\nInstance " + pNthInstance
        + " is not found for " + pNontermCall.toStringWithChildren()
        + ". Take the first instance.");
      lNontermInstance
        = (NontermPatInstance)lNontermInstanceList.get(0);
    }
  }
  if (fDbgLevel > 1)
    dbgOut(3, " nontermInstance " + lNontermInstance);
  if (lNontermInstance != null) {
    //-- Get the matching data (lOrgData) for the nonterminal instance.
    MatchingData lOrgData = null;
    if (fMatchingDataForNontermInstance.containsKey(lNontermInstance))
      lOrgData = (MatchingData)fMatchingDataForNontermInstance
                 .get(lNontermInstance);
    else {
      ioRoot.msgRecovered.put("\nNo MatchingData for "
        + lNontermInstance  + ". Search other instance.");
      if (fMatchingDataForNonterm.containsKey(lKey))
        lOrgData = (MatchingData)fMatchingDataForNonterm.get(lKey);
      else {
        ioRoot.msgRecovered.put("\n MatchingData for "
          + lNontermInstance + " is not found. Ignore it.");
        return null;
      }
    }
    //-- Get the parameter symbol.
    Sym lParamToRewrite;
    if ((pParamNode instanceof SymNode)&&
        (((SymNode)pParamNode).getSymNodeSym() instanceof Param)) {
      lParamToRewrite = ((SymNode)pParamNode).getSymNodeSym();
    }else {
      ioRoot.msgRecovered.put("\nIn reformByChangingParam "
        + pParamNode.toStringWithChildren()
        + " should be a parameter node.");
      return adjustNonterminal( pData, lNontermInstance,
        pNontermCall, pTraversedMatchingData, 0, false );
    }
    if (fDbgLevel > 1)
      dbgOut(3, "\n Use OrgData " + lOrgData.nontermPat
             + " paramToRewrite " + lParamToRewrite);
    //-- Make the local matching data for the nonterminal instance.
    MatchingData lNewData
      = new MatchingData(lNontermInstance, lOrgData);
    //-- Replace all parameter nodes same as pParamNode in
    //   pRewriteExp by the corresponding HIR given by the
    //   matching data.
    Set lParamSet = lNewData.paramCorresp.keySet();
    for (Iterator lIt = lParamSet.iterator();
         lIt.hasNext(); ) {
      Var lParam = (Var)lIt.next();
      Var lOrgParam = getOriginalVar(lParam);
      if (fDbgLevel > 2)
        dbgOut(5, " param " + lParam + " " + lOrgParam);
      if (lOrgParam == lParamToRewrite) {
        HIR lOrgValue = (HIR)lOrgData.paramCorresp.get(lParam);
        // Pick up all parameter nodes to rewrite for
        // the rewrite expression.
        List lRewriteList = new ArrayList();
        for (HirIterator lHirIt = lRewriteExp.hirIterator(lRewriteExp);
             lHirIt.hasNext(); ) {
          HIR lHir = lHirIt.next();
          if ((lHir instanceof SymNode)&&
              (((SymNode)lHir).getSymNodeSym() == lParamToRewrite)) {
            lRewriteList.add(lHir);
          }
        }
        // Then rewrite the picked up parameter nodes by lRewriteExp
        // and record the correspondence to the local MatchingData.
        for (Iterator lListIt = lRewriteList.iterator();
             lListIt.hasNext(); ) {
          HIR lHir = (HIR)lListIt.next();
          replaceHirTree(lHir, lOrgValue.copyWithOperands());
        }
        lNewData.paramCorresp.put(lParam, lRewriteExp);
        if (fDbgLevel > 2)
          dbgOut(4, "\n Rewrite param " + lParam.getName()
                 + " with " + pRewriteExp.toStringWithChildren()
                 + " result " + lRewriteExp.toStringWithChildren());
        break;
      }
    }
    //-- Adjust the expanded HIR corresponding to the nonterminal
    //   instance according to the revised local matching data.
    HIR lAdjustedHir =
      adjustNonterminal( lNewData, lNontermInstance,
        pNontermCall, pTraversedMatchingData, 0, true );
    if (fDbgLevel > 2)
      dbgOut(4, "\n reformByChangingParam result " +
             lAdjustedHir.toStringWithChildren());
    return lAdjustedHir;
  }else {
    ioRoot.msgRecovered.put("\n" + pNontermCall.toStringWithChildren()
      + " has no instance in in-pattern. Ignore it.");
    return null;
  }
} // reformByChangingParam

/**
 * Transform the statement sequence beginning with pInStmt
 * that matched with the statement pattern pPatternsym
 * by applying adjustOutPattern to the corresponding
 * out-pattern and then applying replaceParameters to the
 * adjusted result.
 * At return, fTailStmtMatched represents the last statement
 * in the matched input statement sequence.
 * (This method is called in tryToReform.)
 * @param pData MatchingData instance to be used.
 * @param pPatternSym Pattern symbol representing a statement pattern.
 * @param pInStmt Heading statement of the matched statement sequence.
 * @return The resultant HIR.
 */
protected HIR
transformStmtSeq( MatchingData pData,
             Subp pPatternSym,
             Stmt pInStmt)
{
  if ((pPatternSym == null)||(pInStmt == null))
    return pInStmt;
  if (fDbgLevel > 1)
    dbgOut(2, "transformStmtSeq", pInStmt.toStringShort());
  fTailStmtMatched = pInStmt;
  HIR lInPattern = (HIR)fInPatternMap.get(pPatternSym);
  int lIndex = getIndex(pPatternSym);
  int lStmtCount = fStmtsInPattern[lIndex];
  if (lInPattern == null) {
    dbgOut(1, "Undefined pattern " + pPatternSym.getName()
           + " for " + pInStmt.toStringShort());
    return pInStmt;
  }
  fCurrentPatternSym = pPatternSym;
  HIR lOutPattern = (HIR)(HIR)fOutPatternMap.get(pPatternSym);
  lOutPattern = lOutPattern.copyWithOperandsChangingLabels(null);
  HIR lOutHir;
  Stmt lLastStmt = pInStmt;
  for (int lx = 0; lx < lStmtCount - 1; lx++) {
    lLastStmt = lLastStmt.getNextStmt();
  }
  if (fDbgLevel > 1)
    dbgOut(3, " by " + lInPattern.toStringShort()
      + " index " + lIndex + " lastStmt " + lLastStmt);
  Set lTraversedMatchingData = new HashSet();
  lOutHir = adjustOutPattern(pData, lOutPattern, lTraversedMatchingData, 0);
  Set lTraversedMatchingData2 = new HashSet();
  lOutHir = replaceParameters(pData, // pPatternSym,
    lOutHir, lTraversedMatchingData2, false);
  if (fNoFurtherChange.contains(fCurrentPatternSym)) {
    lOutHir.setFlag(HIR.FLAG_NOCHANGE, true);
  }
  fTailStmtMatched = lLastStmt;
  return lOutHir;
} // transformStmtSeq

/** Get formal-actual parameter correspondence for the nonterminal
 * pNontermPat that is called by the node pCallNode and return the
 * correspondence. If the caller argument contains child nonterminal call,
 * then formal-actual parameter correspondence for the child nonterminal
 * is computed by getParamCorrespForArgWithNonterm.
 * The result is not reflected to pData in this method.
 * @param pData MatchingData inherited.
 * @param pNontermPat nonterminal instance.
 * @param pCallNode Node calling the nonterminal.
 * @param pAdjustPhase True if called in adjust phase, false otherwise.
 * @return the resultant correspondence.
 */
protected Map
getFormalActualParamCorrespondence( MatchingData pData,
  NontermPatInstance pNontermPat, HIR pCallNode,
  boolean pAdjustPhase )
{
  if (fDbgLevel > 1)
    dbgOut(3, "\ngetFormalActualParamCorrespondence entry "
           + pNontermPat + " " + pCallNode + " "
           + pCallNode.toStringWithChildren()
           + " " + pNontermPat + " in " + pNontermPat.parent);
  Map lNontermParamCorresp = new HashMap();
  if ((pNontermPat == null)||
      (pNontermPat.nontermPat == null)||
      (pCallNode == null))
    return lNontermParamCorresp;
  NontermPatInstance lParentNontermInstance
     = pNontermPat.parent;
  while (lParentNontermInstance.nontermPat.getName().intern()
         == "_bnfOr") {
    lParentNontermInstance = lParentNontermInstance.parent;
    if (fDbgLevel > 1)
      dbgOut(3, " ancestor " + lParentNontermInstance);
  }
  IrList lFormalParamList = pNontermPat.nontermPat.getParamList();
  HirList lActualParamList = (HirList)pCallNode.getChild2();
  Iterator lIt1 = lFormalParamList.iterator();
  for (Iterator lIt2 = lActualParamList.iterator();
         lIt1.hasNext() && lIt2.hasNext(); ) {
    Param lFormalParam = (Param)lIt1.next();
    Param lFormalParamInstance = getParamInstance(lFormalParam,
        pNontermPat);
    HIR lActualParamNode = (HIR)lIt2.next();
    if (fDbgLevel > 2)
      dbgOut(4, " formal " + lFormalParamInstance +
             " actual " + lActualParamNode);
    Var lActualParamSym = null;
    Var lActualParamInstance = null;
    Map lParamCorresp = new HashMap();
    if ((lFormalParamInstance != null)&&
        (lActualParamNode instanceof SymNode)) {
      Sym lSym = ((SymNode)lActualParamNode).getSymNodeSym();
      Var lVarInstance = null;
      if (lSym instanceof Var) {
       lVarInstance
         = getVarInstance((Var)lSym, lParentNontermInstance);
      }
      if (lVarInstance instanceof Param) {
        lActualParamSym = (Param)lSym;
        lActualParamInstance = (Param)lVarInstance;
      }else if ((lVarInstance != null)&&
                lVarInstance.getSymType().isConst()) {
         lActualParamSym = (Var)lSym;
         lActualParamInstance = lVarInstance;
      }
      if (lActualParamSym == null)
        continue;
      if ((lActualParamInstance == lFormalParam)||
          (lActualParamSym == lFormalParamInstance)||
          (lActualParamInstance == lFormalParamInstance)) {
        // Escape self correspondence.
        if (fDbgLevel > 2)
          dbgOut(4, " Escape self correspondence "
                 + lFormalParam + " " + lFormalParamInstance + " "
                 + lActualParamSym + " "
                 + lActualParamInstance);
        continue;
      }
      VarNode lActualParamInstanceNode;
      if (lActualParamInstance != null) {
        lActualParamInstanceNode = hir.varNode(lActualParamInstance);
      }else {
        lActualParamInstanceNode = hir.varNode(lActualParamSym);
      }
      if (fDbgLevel > 2)
        dbgOut(4, " " + lActualParamInstance);
      Set lOperands = getLeafOperands(lActualParamNode);
      Sym lNonterminalInArg = null;
      for (Iterator lIt = lOperands.iterator();
           lIt.hasNext(); ) {
        Object lLeaf = lIt.next();
        if (fNonterminalSet.contains(lLeaf)) {
          lNonterminalInArg = (Sym)lLeaf;
          break;
        }
      }
      if (lNonterminalInArg != null) {
        // Nonterminal is called in this argument.
        Set lTraversedNonterminals = new HashSet();
        MatchingData lData = new MatchingData(pData.nontermPat, pData);
        if (fDbgLevel > 2)
          dbgOut(4, " Arg contains nonterminal "
                 + lNonterminalInArg.getName());
        lParamCorresp =
          getParamCorrespForArgWithNonterm(lData,
            lActualParamNode, lFormalParamInstance,
            pNontermPat, pAdjustPhase);
      }else {
        //Add formal-actual correspondence for this parameter.
        if (pAdjustPhase) {
          HIR lPrevVal = null;
          if (fGlobalPatternParamMap.containsKey(lFormalParamInstance))
            lPrevVal = (HIR)fGlobalPatternParamMap.get(lFormalParamInstance);
          else if (pData.nontermParamCorresp.containsKey(lFormalParamInstance))
            lPrevVal = (HIR)pData.nontermParamCorresp.get(lFormalParamInstance);
          if (lPrevVal != null) {
            if (! isSameTree(lActualParamInstanceNode, lPrevVal)) {
              if (fDbgLevel > 0)
                dbgOut(2, "Inconsistent mapping in GlobalReform for "
                + lFormalParamInstance.getName() + " "
                + lActualParamInstanceNode.toStringWithChildren() + " and "
                + lPrevVal.toStringWithChildren()
                + " in " + pData.nontermPat);
              //##86 REFINE what should be done ?
            }
          }
        }
        putToMap(lParamCorresp, lFormalParamInstance,
                 lActualParamInstanceNode);
      }
      //##86 REFINE Is their any discrepancy ?
      lNontermParamCorresp.putAll(lParamCorresp);
    }else {
      // Actual param is not a SymNode.
      // Replace nonterminal call
      HIR lActualParamExp = expandExp(pData,
        lActualParamNode, lParentNontermInstance);
      putToMap(lNontermParamCorresp, lFormalParamInstance,
        lActualParamExp);
    }
  }  // End of parameter list.
  if (fDbgLevel > 1)
    dbgOut(3, "\n getFormalActualParamCorrespondence return "
           + dbgMap(lNontermParamCorresp));
  return lNontermParamCorresp;
} // getFormalActualParamCorrespondence

/**
 * Get formal-actual parameter correspondence for the formal parameter
 * pCallerFormalParam and actual parameter pArgExp that calls
 * child nonterminal. The matching data pData is not changed.
 * @param pData Inherited MatchingData.
 * @param pArgExp Actual parameter that calls child nonterminal.
 * @param pCallerFormalParam Formal parameter corresponding to pArgExp.
 * @param pParentNonterm Nonterminal that calls the child nonterminal.
 * @return formal-actual parameter correspondence.
 */
protected Map
getParamCorrespForArgWithNonterm( MatchingData pData,
  HIR pArgExp, Sym pCallerFormalParam,
  NontermPatInstance pParentNonterm,
  boolean pAdjustPhase )
{
  if (fDbgLevel > 1)
    dbgOut(3, "\ngetParamCorrespForArgWithNonterm entry "
           + pArgExp.toStringWithChildren() +
           " callerFormalParam " + pCallerFormalParam.getName()
           + " of " + pParentNonterm);
  Map lNontermParamCorresp = new HashMap();
  boolean lProcessed = false;
  if (pArgExp.getOperator() == HIR.OP_CALL) {
    Subp lChildNonterm = null;
    if ((pArgExp.getChild1().getOperator() == HIR.OP_ADDR) &&
        (pArgExp.getChild1().getChild1() instanceof SymNode))
      lChildNonterm = (Subp)((SymNode)pArgExp.getChild1().getChild1()).
        getSymNodeSym();
    else if (pArgExp.getChild1() instanceof SymNode)
      lChildNonterm = (Subp)((SymNode)pArgExp.getChild1()).getSymNodeSym();
    if (lChildNonterm != null) {
      NontermPatInstance lChildNontermInstance = null;
      if (pParentNonterm.callToNontermInstanceMap.containsKey(pArgExp))
        lChildNontermInstance =
         (NontermPatInstance)pParentNonterm.callToNontermInstanceMap.get(pArgExp);
      if (fNonterminalSet.contains(lChildNonterm)) {
        if (fDbgLevel > 0)
          dbgOut(4, " " + lChildNonterm.getName() +
                 " " + lChildNontermInstance + " is called from "
                 + pParentNonterm);
        // put(pCallerFormalParam, elementOfArgList)
        for (Iterator lIt = ((IrList)pArgExp.getChild2()).iterator();
             lIt.hasNext(); ) {
          HIR lElem = (HIR)lIt.next();
          if (lElem instanceof SymNode) {
            Sym lArgSym = ((SymNode)lElem).getSymNodeSym();
            if (fDbgLevel > 0)
                dbgOut(4, " " + lChildNonterm.getName() +
                  " is called with param " + lArgSym.getName());
            if (isNontermParam(lArgSym)) {
              if (! pData.nontermParamCorresp.
                  containsKey(pCallerFormalParam)) {
                if (pAdjustPhase&&
                    fGlobalPatternParamMap.containsKey(pCallerFormalParam)) {
                  HIR lPrevVal = (HIR)fGlobalPatternParamMap.get(pCallerFormalParam);
                  if (! isSameTree(lElem, lPrevVal)) {
                    ioRoot.msgRecovered.put("Inconsistent mapping in GlobalReform for "
                        + pCallerFormalParam.getName() + " "
                        + lElem.toStringWithChildren() + " and "
                        + lPrevVal.toStringWithChildren());
                  }
                }
                putToMap(lNontermParamCorresp, pCallerFormalParam,
                  lElem);
              }
            }
          }
        }
        Map lNontermParamCorresp2 =
          getFormalActualParamCorrespondence(pData,
              lChildNontermInstance, pArgExp, pAdjustPhase);
        lNontermParamCorresp.putAll(lNontermParamCorresp2);
        lProcessed = true;
      }
    }
  }
  if (!lProcessed) {
    ioRoot.msgRecovered.put(
      "Argument with nonterminal call should not contain oper operator "
      + pArgExp.toStringWithChildren());
  }
  if (fDbgLevel > 1)
    dbgOut(3, "\n getParamCorrespForArgWithNonterm return "
           + dbgMap(lNontermParamCorresp));
  return lNontermParamCorresp;
} // getParamCorrespForArgWithNonterm

/**
 * Expand all nonterminal calls in pExp to the
 * the expandedExp of the nonterminal instance corresponding
 * to the nonterminal call.
 * (Called in getFormalActualParamCorrespondence.)
 * @param pData MatchingData to be used.
 * @param pExp HIR to be expanded.
 * @param pParentNonterm Nonterminal instance
 * @return the resultant expanded HIR.
 */
protected HIR
  expandExp( MatchingData pData,
    HIR pExp, NontermPatInstance pParentNonterm )
{
  if (pExp == null)
      return pExp;
  if (fDbgLevel > 0)
    dbgOut(2, "\n expandExp " + pExp +
           " " + pExp.toStringWithChildren() +
           " in " + pParentNonterm);
    HIR lExpandedExp = pExp;
    int lChildCount = pExp.getChildCount();
    if (lChildCount <= 1)
      return pExp;
    Sym lNontermSym = null, lSym = null;
    if (pExp.getOperator() == HIR.OP_CALL) {
      HIR lChild1 = (HIR)pExp.getChild1();
      if (lChild1 instanceof SymNode) {
        lSym = ((SymNode)lChild1).getSymNodeSym();
        if (fNonterminalSet.contains(lSym))
            lNontermSym = lSym;
      }else if ((lChild1.getOperator() == HIR.OP_ADDR)&&
                (lChild1.getChild1() instanceof SymNode)) {
        lSym = ((SymNode)lChild1.getChild1()).getSymNodeSym();
      }
      if ((lSym != null)&&
           fNonterminalSet.contains(lSym))
        lNontermSym = lSym;
    }
    if (lNontermSym != null) {
      NontermPatInstance lNontermInstance =
        getNontermInstance((Subp)lNontermSym, pExp,
              pParentNonterm );
      MatchingData lData = null;
      if (fMatchingDataForNonterm.containsKey(lNontermInstance)) {
        lData = (MatchingData)fMatchingDataForNonterm.get(lNontermInstance);
        lExpandedExp = lData.expandedHir.copyWithOperands();
      }else if (fExpandedNontermInstance.containsKey(lNontermInstance)) {
        lExpandedExp = (HIR)fExpandedNontermInstance.get(lNontermInstance);
      }else {
        if (fDbgLevel > 0)
          dbgOut(2, "\n expandedExp not found for "
                 + pExp.toStringWithChildren()
                 + " in expandExp. Use fExpandedNonterm.");
        HIR lKey = makeHirKey(pExp);
        if (fExpandedNonterm.containsKey(lKey)) {
          lExpandedExp = (HIR)fExpandedNonterm.get(lKey);
        }
      }
      if (lExpandedExp != pExp)
        lExpandedExp = lExpandedExp.copyWithOperands();
    }else {
      boolean lChanged = false;
      HIR lNewExp = pExp.hirClone();
      for (int i = 1; i < lChildCount; i++) {
        HIR lChild = (HIR)pExp.getChild(i);
        HIR lNewChild = expandExp(pData, lChild, pParentNonterm);
        if (lNewChild != lChild) {
          lChanged = true;
        }
        lNewExp.setChild(i, lNewChild);
      }
      if (lChanged)
        lExpandedExp = lNewExp;
    }
    if (fDbgLevel > 1)
      dbgOut(4, "\n expandExp " + pExp + " result="
             + lExpandedExp.toStringWithChildren());
    return lExpandedExp;
} // expandExp

  /**
   * Check the consistency of parameters in in-pattern
   * and out-pattern. Any of following cases are treated
   * as inconsistent usage:
   *   Some parameter in the out-pattern does not appear
   *   in the in-pattern.
   *   Some parameter in the in-pattern does not appear
   *   in the out-pattern.
   * @param pPattern subprogram representing a pattern.
   * @param pLocalVarList list of variables declared locally
   *     in pOutPattern.
   * @param pInPattern in-pattern.
   * @param pOutPattern out-pattern.
   * @return true if no inconsistency is found, false if some
   *     inconsistency is found.
   */
protected boolean
checkConsistency( Subp pPattern, List pLocalVarList,
                  HIR pInPattern, HIR pOutPattern )
{
  boolean lResult = true;
  if (fDbgLevel > 0)
    dbgOut(3, "checkConsistency", pInPattern.toStringShort()
       + " and " + pOutPattern.toStringShort());
  SymTable lSubpSymTable = pPattern.getSymTable();
  Set lInParamAppeared = new HashSet();
  Set lOutParamAppeared = new HashSet();
  for (HirIterator lIt1 = pInPattern.hirIterator(pInPattern);
       lIt1.hasNext(); ){
    HIR lHir1 = (HIR)lIt1.next();
    if (lHir1 instanceof SymNode) {
      Sym lSym1 = ((SymNode)lHir1).getSymNodeSym();
      if (lSym1 instanceof Param) {
        lInParamAppeared.add(lSym1);
      }else if (lSym1 == pPattern) {
        // The pattern symbol appeared in the in-pattern as a subprogram.
        // Treat it as a parameter representing a recursive subprogram.
        lInParamAppeared.add(lSym1);
      }
    }
  }
  for (HirIterator lIt2 = pOutPattern.hirIterator(pOutPattern);
       lIt2.hasNext(); ){
    HIR lHir2 = (HIR)lIt2.next();
    if (lHir2 instanceof SymNode) {
      Sym lSym2 = ((SymNode)lHir2).getSymNodeSym();
      if (lSym2 instanceof Param) {
        lOutParamAppeared.add(lSym2);
        if (! lInParamAppeared.contains(lSym2)) {
          ioRoot.msgRecovered.put(5122, "Parameter " +
            lSym2.getName()
            + " has not appeared in the input pattern of "
            + pPattern.getName());
          lResult = false;
        }
      }else if (lSym2 instanceof Var) {
        // Variable declared local in out-pattern.
        //##81 BEGIN
        if (symRoot.symTableRoot.search(lSym2.getName().intern(),
            lSym2.getSymKind())
            == null) {
          // Variable declared as local in pPattern
          if (!pLocalVarList.contains(lSym2))
            pLocalVarList.add(lSym2);
       }
      //##81 END
      }else if (lSym2 == pPattern) {
        // Treat as a recursive subprogram.
        lOutParamAppeared.add(lSym2);
      }

    }
  }
  Set lInParam2 = new HashSet();
  lInParam2.addAll(lInParamAppeared);
  lInParam2.removeAll(lOutParamAppeared);
  if (! lInParam2.isEmpty()) {
    dbgOut(2, "Parameters " + lInParam2.toString()
      + " have not appeared in the output pattern of "
      + pPattern.getName());
  }
  if (fDbgLevel > 0)
    dbgOut(2, "\nVariables local to out-pattern " + pLocalVarList);
  return lResult;
} // checkConsistency

/**
 * Compute the pattern code of pHir digging out up to 2 levels
 * of children.
 * @param pHir geven HIR subtree.
 * @return pattern code of pHir.
 */
protected double
patternCode( HIR pHir )
{
  // Pattern code of HIR subtree
  //  c0 --- c1 --- c11
  //     |      |-- c12
  //     |
  //     |-- c2 --- c21
  //            |-- c22
  // is
  //   c0*L*L*L*L*L*L + c1*L*L*L*L*L + c2*L*L*L*L
  //   + c11*L*L*L + c12*L*L + c21*L + c22
  // where, c0, c1, etc. are operator code and
  // L is fHirCodeLim (greater than any HIR operator code).
  double lPatternCode;
  if (pHir == null)
    return 0;
  lPatternCode = ((double)pHir.getOperator()) * fHirCodeLim6;
  HIR lChild1 = (HIR)pHir.getChild1();
  if (lChild1 != null) {
    if (fDbgLevel > 2)
      dbgOut(8, " child1 " + lChild1.getOperator());
    lPatternCode = lPatternCode + ((double)lChild1.getOperator())
                                * fHirCodeLim5;
    HIR lChild11 = (HIR)lChild1.getChild1();
    if (lChild11 != null) {
      lPatternCode = lPatternCode + ((double)lChild11.getOperator())
                                  * fHirCodeLim3;
    }
    HIR lChild12 = (HIR)lChild1.getChild2();
    if (lChild12 != null) {
      lPatternCode = lPatternCode + ((double)lChild12.getOperator())
                                  * fHirCodeLim2;
    }
  }
  HIR lChild2 = (HIR)pHir.getChild2();
  if (lChild2 != null) {
    if (fDbgLevel > 2)
      dbgOut(8, " child2 " + lChild2.getOperator());
    lPatternCode = lPatternCode + ((double)lChild2.getOperator())
                                * fHirCodeLim4;
    HIR lChild21 = (HIR)lChild2.getChild1();
    if (lChild21 != null) {
      lPatternCode = lPatternCode + ((double)lChild21.getOperator())
                                  * fHirCodeLim;
    }
    HIR lChild22 = (HIR)lChild2.getChild2();
    if (lChild22 != null) {
      lPatternCode = lPatternCode + (double)lChild22.getOperator();
    }
  }
  return lPatternCode;
} // patternCode

/**
 * computePatternCodeRange
 * compute pattern code range (fPatternCodeUpper and fPatternCodeLower)
 * of all patterns.
 * If the pattern is a block statement containing multiple statements,
 * then compute code range of its 1st statement and 2nd statement
 * (fStmt1CodeUpper/Lower and fStmt2CodeUpper/Lower) and the number of
 * statements in the pattern (fStmtsInPattern).
 * The pattern code of HIR subtree
 *   c0 --- c1 --- c11
 *      |      |-- c12
 *      |
 *      |-- c2 --- c21
 *             |-- c22
 * is computed as
 *   c0*L*L*L*L*L*L + c1*L*L*L*L*L + c2*L*L*L*L
 *   + c11*L*L*L + c12*L*L + c21*L + c22
 * where, c0, c1, etc. are operator code and
 * L is fHirCodeLim (greater than any HIR operator code).
 * If some child in above tree is a parameter, then
 * fDontCareCode (greater than any opCode) is used as its
 * operator code for computing upper code
 * and 0 is used as its operator (corresponding to the case
 * where the child is null) for computing lower code.
 */
protected void
computePatternCodeRange()
{
  fSubrootCount = 0;
  HIR lNode;
  double lUpperCode, lLowerCode;
  double lCode;
  PatternCodeRange lRange;
  dbgOut(2, "\ncomputePatternCodeRange");
  List lPatternCodeRangeList = new ArrayList();
  fCodeIndexFrom = new int[fNontermPatCount + 1];
  fCodeIndexTo   = new int[fNontermPatCount + 1];
  fStmt1CodeIndexFrom = new int[fNontermPatCount + 1];
  fStmt1CodeIndexTo   = new int[fNontermPatCount + 1];
  fStmt2CodeIndexFrom = new int[fNontermPatCount + 1];
  fStmt2CodeIndexTo   = new int[fNontermPatCount + 1];
  fFormalParams       = new Set[fNontermPatCount + 1];
  fInstanceNumberForNonterminals = new int[fNontermPatCount + 1]; //##86
  fUsedAsPatternRoot = new boolean[(int)fHirCodeLim + 1];
  fUsedAsSubRoot     = new boolean[(int)fHirCodeLim + 1];
  fMaxStmtsInPattern = 1;
  for (int i = 0; i < fUsedAsPatternRoot.length; i++)
    fUsedAsPatternRoot[i] = false;
  for (int i = 0; i < fUsedAsSubRoot.length; i++)
    fUsedAsSubRoot[i] = false;
  List[] lPatternCodeRangeArray = new List[fNontermPatCount + 1];
  List[] lStmt1CodeRange = new List[fNontermPatCount + 1];
  List[] lStmt2CodeRange = new List[fNontermPatCount + 1];
  for (int i = 0; i < fNontermPatCount + 1; i++) {
    lPatternCodeRangeArray[i] = new ArrayList();
    lStmt1CodeRange[i] = new ArrayList();
    lStmt2CodeRange[i] = new ArrayList();
    fFormalParams[i] = new HashSet();
  }
  int lNontermPatIndex;
  for (Iterator lIt1 = fPatternList.iterator();
       lIt1.hasNext(); ) {
    Subp lPatternSym = (Subp)lIt1.next();
    lNontermPatIndex = getIndex(lPatternSym);
    if (fDbgLevel > 2)
      dbgOut(3, "\n "+ lNontermPatIndex + " " + lPatternSym.getName());
    for (Iterator lParamIt2 = lPatternSym.getParamList().iterator();
         lParamIt2.hasNext(); ) {
      Sym lParam = (Sym)lParamIt2.next();
      fFormalParams[lNontermPatIndex].add(lParam);
    }
    HIR lInPattern = (HIR)fInPatternMap.get(lPatternSym);
    if (lInPattern == null)
      continue;
    List lSubrootList1 = new ArrayList(); // Pattern root
    listUpHeadingHir(lPatternSym, lSubrootList1, lInPattern);
    for (Iterator lIt5 = lSubrootList1.iterator();
         lIt5.hasNext(); ) {
      HIR lHir1 = (HIR)lIt5.next();
      lRange = computePatternCodeRangeOf(lHir1);
      lPatternCodeRangeArray[lNontermPatIndex].add(lRange);
      fUsedAsPatternRoot[lHir1.getOperator()] = true;
      fUsedAsSubRoot[lHir1.getOperator()] = true;
      fSubrootCount++;
      if (fDbgLevel > 1)
        dbgOut(4, "\n " + lHir1.toStringShort() + " [" + lRange.fLower + " - " +
               lRange.fUpper + "]");
    }
    Stmt lStmt1 = null, lStmt2 = null, lStmt3 = null;
    lNode = lInPattern;
    if (lNode instanceof BlockStmt) {
      // Block statement. Its child 1 is the first statement.
      // Get the number of statements (fStmtsInPattern) and
      // compute pattern code of up to 2 statements in the block
      // (fStmt1CodeUpper/Lower, fStmt2CodeUpper/Lower).
      lStmt1 = ((BlockStmt)lNode).getFirstStmt();
      while (lStmt1 instanceof InfStmt) { // Skip InfStmt
        lStmt1 = lStmt1.getNextStmt();
      }
      if (lStmt1 != null) {
        lStmt2 = lStmt1.getNextStmt();
        fStmtsInPattern[lNontermPatIndex] = 1;
        // Treat the first statement in the same way as
        // the subroot of the pattern.
        List lSubrootList2 = new ArrayList(); // First statement subroot.
        listUpHeadingHir(lPatternSym, lSubrootList2, lStmt1);
        for (Iterator lIt6 = lSubrootList2.iterator();
             lIt6.hasNext(); ) {
          HIR lHir1 = (HIR)lIt6.next();
          lRange = computePatternCodeRangeOf(lHir1);
          lStmt1CodeRange[lNontermPatIndex].add(lRange);
          fUsedAsPatternRoot[lHir1.getOperator()] = true;
          fUsedAsSubRoot[lHir1.getOperator()] = true;
          fSubrootCount = fSubrootCount + 2; // Add 2 because the
              // statement 1 nay be counted twice in fPatternCodeUpper/Lower.
          if (fDbgLevel > 1)
            dbgOut(4, "\n stmt1 " + lHir1.toStringShort() + " [" + lRange.fLower + " - " +
                   lRange.fUpper + "]");
        }
        while (lStmt2 instanceof InfStmt) { // Skip InfStmt.
          lStmt2 = lStmt2.getNextStmt();
        }
        if (lStmt2 != null) {
          lStmt3 = lStmt2.getNextStmt();
          fStmtsInPattern[lNontermPatIndex] = 2;
          List lSubrootList3 = new ArrayList(); // 2nd statement subroot
          listUpHeadingHir(lPatternSym, lSubrootList3, lStmt2);
          for (Iterator lIt7 = lSubrootList3.iterator();
               lIt7.hasNext(); ) {
            HIR lHir1 = (HIR)lIt7.next();
            lRange = computePatternCodeRangeOf(lHir1);
            lStmt2CodeRange[lNontermPatIndex].add(lRange);
            fSubrootCount++;
            if (fDbgLevel > 1)
              dbgOut(4, "\n stmt2 " + lHir1.toStringShort() + " [" + lRange.fLower + " - " +
                     lRange.fUpper + "]");
          }
          // Count the number of statements in the block.
          Stmt lStmtn = lStmt3;
          while (lStmtn != null) {
            fStmtsInPattern[lNontermPatIndex]
              = fStmtsInPattern[lNontermPatIndex] + 1;
            lStmtn = lStmtn.getNextStmt();
          }
          if (fDbgLevel > 3)
            dbgOut(4, "\n stmtCount " + fStmtsInPattern[lNontermPatIndex] + " lStmtn " + lStmtn); //###
        }
      }
    }
    if (fStmtsInPattern[lNontermPatIndex] > fMaxStmtsInPattern)
      fMaxStmtsInPattern = fStmtsInPattern[lNontermPatIndex];
  } // End of pattern list
  for (Iterator lIt11 = fNonterminalList.iterator();
       lIt11.hasNext(); ) {
    Subp lNonterminal = (Subp)lIt11.next();
    lNontermPatIndex = getIndex(lNonterminal);
    if (fDbgLevel > 0)
      dbgOut(2, "\n " + lNonterminal.getName() + " " + lNontermPatIndex);
    for (Iterator lParamIt2 = lNonterminal.getParamList().iterator();
         lParamIt2.hasNext(); ) {
      Sym lParam = (Sym)lParamIt2.next();
      fFormalParams[lNontermPatIndex].add(lParam);
    }
    HIR lRhsProduction = (HIR)fProductionMap.get(lNonterminal);
    List lSubrootList4 = new ArrayList();
    listUpHeadingHir(lNonterminal, lSubrootList4, lRhsProduction);
    for (Iterator lIt12 = lSubrootList4.iterator();
         lIt12.hasNext(); ) {
      HIR lHir2 = (HIR)lIt12.next();
      lRange = computePatternCodeRangeOf(lHir2);
      lPatternCodeRangeArray[lNontermPatIndex].add(lRange);
      fUsedAsSubRoot[lHir2.getOperator()] = true;
      fSubrootCount++;
      if (fDbgLevel > 1)
        dbgOut(4,"\n " + lHir2.toStringShort() +  " [" + lRange.fLower + " - " +
               lRange.fUpper + "]");
    }
  } // End of nonterminal list

  //-- Build fPatrternCodeUpper/Lower from the lists of
  //   code ranges.
  dbgOut(2, "\nbuild fPatternCodeUpper/Lower fSubrootCount="
         + fSubrootCount);
  fPatternCodeUpper = new double[fSubrootCount + 1];
  fPatternCodeLower = new double[fSubrootCount + 1];
  int lIndex10;
  int lCodeIndexFromTo = 0;
  for (Iterator lIt20 = fPatternList.iterator();
       lIt20.hasNext(); ) {
    Subp lPatternSym = (Subp)lIt20.next();
    lIndex10 = getIndex(lPatternSym);
    fCodeIndexFrom[lIndex10] = lCodeIndexFromTo;
    lCodeIndexFromTo = setPatternCodeRange(lCodeIndexFromTo,
      lPatternCodeRangeArray[lIndex10]);
    if (fStmtsInPattern[lIndex10] > 1) {
      lCodeIndexFromTo = setPatternCodeRange(lCodeIndexFromTo,
            lStmt1CodeRange[lIndex10]);
    }
    fCodeIndexTo[lIndex10] = lCodeIndexFromTo;
    if (fDbgLevel > 0) {
      dbgOut(3, "\n" + lPatternSym.getName() + " from " +
        fCodeIndexFrom[lIndex10] + " to " + fCodeIndexTo[lIndex10]);
      dbgOut(3, "\n Parameters " + fFormalParams[lIndex10].toString());
    }
  }
  for (Iterator lIt22 = fNonterminalList.iterator();
       lIt22.hasNext(); ) {
    Subp lNonterminal = (Subp)lIt22.next();
    lIndex10 = getIndex(lNonterminal);
    fCodeIndexFrom[lIndex10] = lCodeIndexFromTo;
    lCodeIndexFromTo = setPatternCodeRange(lCodeIndexFromTo,
          lPatternCodeRangeArray[lIndex10]);
    fCodeIndexTo[lIndex10] = lCodeIndexFromTo;
    if (fDbgLevel > 0)
      dbgOut(3, "\n" + lNonterminal.getName() + " from " +
        fCodeIndexFrom[lIndex10] + " to " + fCodeIndexTo[lIndex10]);
  }
  for (Iterator lIt24 = fPatternList.iterator();
       lIt24.hasNext(); ) {
    Subp lPatternSym = (Subp)lIt24.next();
    lIndex10 = getIndex(lPatternSym);
    fStmt1CodeIndexFrom[lIndex10] = 0;
    fStmt2CodeIndexFrom[lIndex10] = 0;
    fStmt1CodeIndexTo[lIndex10] = 0;
    fStmt2CodeIndexTo[lIndex10] = 0;
    if (fStmtsInPattern[lIndex10] > 1) {
      fStmt1CodeIndexFrom[lIndex10] = lCodeIndexFromTo;
      lCodeIndexFromTo = setPatternCodeRange(lCodeIndexFromTo,
            lStmt1CodeRange[lIndex10]);
      fStmt1CodeIndexTo[lIndex10] = lCodeIndexFromTo;
      fStmt2CodeIndexFrom[lIndex10] = lCodeIndexFromTo;
      lCodeIndexFromTo = setPatternCodeRange(lCodeIndexFromTo,
              lStmt2CodeRange[lIndex10]);
      fStmt2CodeIndexTo[lIndex10] = lCodeIndexFromTo;
      if (fDbgLevel > 0) {
        dbgOut(3, "\n" + lPatternSym.getName() + " stmt1[" +
               fStmt1CodeIndexFrom[lIndex10] + " - "
               + fStmt1CodeIndexTo[lIndex10] + "] stmt2[" +
               fStmt2CodeIndexFrom[lIndex10] + " - "
               + fStmt2CodeIndexTo[lIndex10] + "]");
        dbgOut(3, "\n Parameters " + fFormalParams[lIndex10].toString());
      }
    }
  }
  if (fDbgLevel > 0) {
    dbgOut(2, "\nfPatternParameters " + fPatternParameters.toString());
    dbgOut(2, "\nfNonterminalParameters " + fNonterminalParameters.toString());
    dbgOut(2, "\nfStmtParamSet " + fStmtParamSet.toString());
    dbgOut(2, "\nfCompileTimeEval " + fCompileTimeEval.toString());
    dbgOut(2, "\nfFitToAnyCall " + fFitToAnyCall.toString());
    dbgOut(2, "\nfUsedAsPatternRoot ");
    for (int i = 0; i < fUsedAsPatternRoot.length; i++) {
      if (fUsedAsPatternRoot[i])
        dbgOut(2, " " + HIR.OP_CODE_NAME_DENSE[i]);
    }
  }
} // computePatternCodeRange

/**
 * computePatternCodeRangeOf(.....) computes pattern code range
 * of pHir and set upper to pUpper[0], loper to pLower[0].
 * @param pHir  HIR for which the pattern code range is to be computed.
 * @param pUpper hold the upper value at pUpper[0].
 * @param pLower hold the lower value at pLower[0].
 */
protected PatternCodeRange
computePatternCodeRangeOf( HIR pHir )
{
  PatternCodeRange lRange = new PatternCodeRange();
  HIR lNode = pHir;
  double lUpperCode, lLowerCode, lCode;
  if (fDbgLevel > 0)
    dbgOut(4, "\n computePatternCodeRangeOf " + lNode);
  int lOpCode = lNode.getOperator();
  lUpperCode = ((double)lOpCode) * fHirCodeLim6;
  lLowerCode = lUpperCode;
  HIR lChild1 = (HIR)lNode.getChild1();
  if (lChild1 != null) {
    if (fDbgLevel > 2)
      dbgOut(5, " child1 " + lChild1.getOperator());
    lCode = (double)lChild1.getOperator();
    if (lChild1 instanceof Param) {
      lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim5;
    //##91 BEGIN
    }else if ((lOpCode == HIR.OP_CALL)&&
              (lChild1 instanceof VarNode)) {
      // Call with function parameter.
      // It may match with any function call.
      lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim5;
    //##91 END
    }else {
      lUpperCode = lUpperCode + lCode * fHirCodeLim5;
      lLowerCode = lLowerCode + lCode * fHirCodeLim5;
    }
    HIR lChild11 = (HIR)lChild1.getChild1();
    if (lChild11 != null) {
      lCode = (double)lChild11.getOperator();
      if (lChild11 instanceof Param) {
        lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim3;
      }else {
        lUpperCode = lUpperCode + lCode * fHirCodeLim3;
        lLowerCode = lLowerCode + lCode * fHirCodeLim3;
      }
    }
    HIR lChild12 = (HIR)lChild1.getChild2();
    if (lChild12 != null) {
      lCode = (double)lChild12.getOperator();
      if (lChild11 instanceof Param) {
        lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim2;
      }else {
        lUpperCode = lUpperCode + lCode * fHirCodeLim2;
        lLowerCode = lLowerCode + lCode * fHirCodeLim2;
      }
    }
  }
  HIR lChild2 = (HIR)lNode.getChild2();
  if (lChild2 != null) {
    if (fDbgLevel > 2)
      dbgOut(5, " child2 " + lChild2.getOperator());
    lCode = (double)lChild2.getOperator();
    if (lChild2 instanceof Param) {
      lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim4;
    //##91 BEGIN
    }else if ((lOpCode == HIR.OP_CALL)&&
            (lChild1 instanceof VarNode)) {
      // Call with function parameter.
      // It may match with any function call.
      lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim5;
    //##91 END
    }else {
      lUpperCode = lUpperCode + lCode * fHirCodeLim4;
      lLowerCode = lLowerCode + lCode * fHirCodeLim4;
    }
    HIR lChild21 = (HIR)lChild2.getChild1();
    if (lChild21 != null) {
      lCode = (double)lChild21.getOperator();
      if (lChild21 instanceof Param) {
        lUpperCode = lUpperCode + fDontCareCode * fHirCodeLim;
      }else {
        lUpperCode = lUpperCode + lCode * fHirCodeLim;
        lLowerCode = lLowerCode + lCode * fHirCodeLim;
      }
    }
    HIR lChild22 = (HIR)lChild2.getChild2();
    if (lChild22 != null) {
      lCode = (double)lChild22.getOperator();
      if (lChild22 instanceof Param) {
        lUpperCode = lUpperCode + fDontCareCode;
      }else {
        lUpperCode = lUpperCode + lCode;
        lLowerCode = lLowerCode + lCode;
      }
    }
  }
  lRange.fUpper = lUpperCode * 1.001; // Give allowance of 0.1%.
  lRange.fLower = lLowerCode * 0.999;
  return lRange;
} // computePatternCodeRangeOf

/**
 * Set the pattern code ranges in pRangeList to
 * fPatternCodeUpper/Lower starting from pStartIndex and return
 * the updated next index value.
 * @param pStartIndex start position to store the pattern codes.
 * @param pRangeList list of pattern codes.
 * @return the updated next position.
 */
int
setPatternCodeRange( int pStartIndex, List pRangeList )
{
  int lIndex = pStartIndex;
  for (Iterator lIt = pRangeList.iterator();
       lIt.hasNext(); ) {
    boolean lDuplicated = false;
    PatternCodeRange lRange = (PatternCodeRange)lIt.next();
    for (int lx = pStartIndex; lx < lIndex; lx++) {
      if ((lRange.fUpper == fPatternCodeUpper[lx])&&
          (lRange.fLower == fPatternCodeLower[lx])) {
        lDuplicated = true;
        break;
      }
    }
    if (! lDuplicated) {
      fPatternCodeUpper[lIndex] = lRange.fUpper;
      fPatternCodeLower[lIndex] = lRange.fLower;
      lIndex++;
    }
  }
  return lIndex;
} // setPatternCodeRange

/**
 * Replace expression lOld by lNew adjusting type to
 * that of lOld.
 * @param lOld old expression to be replaced.
 * @param lNew new expression for replacement.
 * @return replaced expression.
 */
protected HIR
replaceExpAdjustingType( Exp lOld, Exp lNew )
{
  if (fDbgLevel > 2) {
    dbgOut(3, "\n replaceExpAdjustingType " + lOld.toStringWithChildren()
           + " with " + lNew.toStringWithChildren());
  }
  Exp lExp = lNew;
  Type lOldType = lOld.getType();
  Type lNewType = lNew.getType();
  if ((lOldType instanceof PointerType)&&
      (lNewType instanceof VectorType)) {
    lExp = hirRoot.hir.decayExp(lNew);
  }else if ((lOldType instanceof VectorType)&&
            (lNewType instanceof PointerType)) {
    lExp = hirRoot.hir.undecayExp(lNew, ((VectorType)lOldType).getElemCount());
  }else if (lOldType != lNewType) {
    lExp = hirRoot.hir.convExp(lOldType, lNew);
  }
  if (fDbgLevel > 2) {
    if (lExp != lNew)
      dbgOut(3, " changing to " + lExp.toStringWithChildren());
  }
  Exp lNewExp = (Exp)replaceHirTree(lOld, lExp);
  return lNewExp;
} // replaceExpAdjustingType

protected HIR  //## NOT YET USED
adjustType( Exp pExp, Type pType )
{
  if (fDbgLevel > 2) {
    dbgOut(4, "\n adjustType of " + pExp.toStringWithChildren()
           + " to " + pType);
  }
  Exp lExp = pExp;
  Type lNewType = pExp.getType();
  if ((pType instanceof PointerType)&&
      (lNewType instanceof VectorType)) {
    lExp = hirRoot.hir.decayExp(pExp);
  }else if ((pType instanceof VectorType)&&
            (lNewType instanceof PointerType)) {
    lExp = hirRoot.hir.undecayExp(pExp, ((VectorType)pType).getElemCount());
  }else if (pType != lNewType) {
    lExp = hirRoot.hir.convExp(pType, pExp);
  }
  if (fDbgLevel > 2) {
    if (lExp != pExp)
      dbgOut(3, " changing to " + lExp.toStringWithChildren());
  }
  return lExp;
} // adjustType

/**
 * Replace pOld with pNew according to their kind.
 * If isSameTree(pOld,pNew), do not replace.
 * @param pOld Old HIR to be replaced.
 * @param pNew New HIR used to replace.
 * @return the replaced result.
 */
protected HIR
replaceHirTree( HIR pOld, HIR pNew )
{
  if (fDbgLevel > 2) {
    dbgOut(4, "\n replaceHirTree " + pOld
           + " with " + pNew);
  }
  if (isSameTree(pOld, pNew)) {
    if (fDbgLevel > 2)
      dbgOut(4, " same tree (do not replace)");
    return pOld;
  }
  HIR lHir = pNew;
  if ((pNew instanceof ExpStmt)&&
      (pOld instanceof Exp)) {
    lHir = (HIR)pNew.getChild1();
  }
  HIR lNewHir;
  if ((pOld instanceof Stmt)&&(lHir instanceof Stmt)) {
    lNewHir = ((Stmt)pOld).replaceThisStmtWith((Stmt)lHir);
  }else {
    lNewHir = pOld.replaceThisNode(lHir);
    // Cut off lOld so that it is not replaced again (in adjustOutPattern).
    pOld.cutParentLink();
  }
  return lNewHir;
} // replaceHirTree


/** isSameTree is a little different from that of coins.flow.HirSubpFlowImpl.
 * @param pTree1 HIR tree to be compared.
 * @param pTree2 Another HIR tree to be compared.
 * @return true if pTree1 and pTree2 have the same shape and contents,
 *    false otherwise.
 **/
protected boolean
  isSameTree( HIR pTree1, HIR pTree2 )
{
  Sym lSym1, lSym2;
  int lChildCount, lChild;

  if (pTree1 == pTree2)
    return true;
  if ((pTree1 == null) || (pTree2 == null)) // One is null, the other is not.
    return false;
  if (fDbgLevel > 3)
    ioRoot.dbgOpt1.print(7, " isSameTree " +
      pTree1.getIrName() + " " + pTree2.getIrName());
  if (pTree1.getOperator() != pTree2.getOperator()) {
    // Operator codes are different.
    if ((pTree1.getOperator() == HIR.OP_CONV)&&
        ((HIR)pTree1.getChild1()).getType().getFinalOrigin()
        == pTree2.getType().getFinalOrigin()) {
      // May be essentially the same.
      return isSameTree((HIR)pTree1.getChild1(), pTree2);
    }else if ((pTree2.getOperator() == HIR.OP_CONV)&&
        ((HIR)pTree2.getChild1()).getType().getFinalOrigin()
        == pTree1.getType().getFinalOrigin()) {
      // May be essentially the same.
      return isSameTree(pTree1, (HIR)pTree2.getChild1());
    }
    return false;
  }
  // Operator codes are the same.
  if (patternCode(pTree1) != patternCode(pTree2)) {
    // Pattern codes are different.
    return false;
  }
  //-- With the same pattern code. --
  if (pTree1 instanceof SymNode) {
    if (((SymNode)pTree1).getSymNodeSym()
        == ((SymNode)pTree2).getSymNodeSym())
      // Symbols are the same.
      return true;
    else
      return false;
  }
  else { // The trees has no symbol attached.
    lChildCount = pTree1.getChildCount();
    if ((pTree2.getChildCount() != lChildCount) ||
        (pTree1.getType() != pTree2.getType())) {
      return false; // Child count or type differ.
    }
    else { // Examine lower constructs.
      if (pTree1 instanceof BlockStmt) {
        Stmt lStmt1 = ((BlockStmt)pTree1).getFirstStmt();
        Stmt lStmt2 = ((BlockStmt)pTree2).getFirstStmt();
        while ((lStmt1 != null)&&(lStmt2 != null)) {
          // Check whether contained statements are the same or not.
          if (isSameTree(lStmt1, lStmt2)) {
            lStmt1 = lStmt1.getNextStmt();
            lStmt2 = lStmt2.getNextStmt();
          }else {
            return false;
          }
        }
        if ((lStmt1 == null)&&(lStmt2 == null)) {
          // Both reached to the end of BlockStmt.
          return true;
        }else
        return false;
      }else if (pTree1 instanceof IrList) {
        // Check whether all elements are the same or not.
        Iterator lIt1 = ((IrList)pTree1).iterator();
        Iterator lIt2 = ((IrList)pTree2).iterator();
        while (lIt1.hasNext()&&lIt2.hasNext()) {
          HIR lHir1 = (HIR)lIt1.next();
          HIR lHir2 = (HIR)lIt2.next();
          if (! isSameTree(lHir1, lHir2))
            return false;
        }
        if (lIt1.hasNext()||lIt2.hasNext()) {
          // There remains some elements in one of the lists.
          return false;
        }else {
          return true;
        }
      }else {
        // Examine children.
        for (lChild = 1; lChild <= lChildCount; lChild++) {
          if (!isSameTree((HIR)(pTree1.getChild(lChild)),
            (HIR)(pTree2.getChild(lChild)))) {
            // Difference was found in the children.
            return false;
          }
        }
        return true; // All children of pTree1 are the same
        // to the corresponding children of pTree2.
      }
    }
  }
} // isSameTree

/**
 * Process the pragma specifications such as
 *   #pragma globalReform transparentFitting pc (pzz2, pi2)
 * and record the transparency relations to fTransparentMap.
 * @param pPattern  Pattern symbol.
 * @param pOptionName Option name ("transparentFitting", etc.).
 * @param pOptionList List of the transparency relation.
 */
protected void
processTransparent( Subp pPattern, String pOptionName, IrList pOptionList )
{
  Object lTransparentVar = pOptionList.get(1);
  if (fDbgLevel > 1)
    dbgOut(4, "\nprocessTransparent " + pOptionName + " " + lTransparentVar
    + " " + lTransparentVar.getClass() + "\n");
  if (lTransparentVar instanceof Var) {
    Object lList = pOptionList.get(2);
    if (fDbgLevel > 1)
      dbgOut(5, " " + lList + " " + lList.getClass() + "\n");
    if (pOptionName == "transparentFitting")
      fFittingSet.add(lTransparentVar);
    Set lTransVarSet = new HashSet();
    if (lList instanceof IrList) {
      IrList lVarList = (IrList)lList;
      for (Iterator lListIt = lVarList.iterator();
        lListIt.hasNext(); ) {
        Object lVar = lListIt.next();
        if (lVar instanceof Var) {
          lTransVarSet.add(lVar);
        }
      }
    }
    fTransparentMap.put(lTransparentVar, lTransVarSet);
    if (fDbgLevel > 1)
      dbgOut(3, "\n transparent " + ((Var)lTransparentVar).getName()
      + " " + lTransVarSet );
  }else {
    ioRoot.msgWarning.put("pragma globalReform " + pOptionName
      + " " + lTransparentVar + " should be a variable" );
  }
} // processTransparent

/**
 * Set FLAG_NONTERMINAL to all call nodes that call _bnfOr,
 * _bnfSeq, or _assignStmt contained in pPattern.
 * @param pPattern HIR to which nodes nonterminal flag is
 *     to be attached.
 */
protected void
setNonterminalFlag(HIR pPattern)
{
  if (pPattern == null)
    return;
  for (HirIterator lHirIt = hir.hirIterator(pPattern);
       lHirIt.hasNext(); ) {
    HIR lHir = lHirIt.next();
    if (lHir != null) {
      if (lHir.getOperator() == HIR.OP_CALL) {
        Sym lSubp = null;
        if ((lHir.getChild1().getOperator() == HIR.OP_ADDR)&&
            (lHir.getChild1().getChild1() instanceof SymNode))
          lSubp = ((SymNode)lHir.getChild1().getChild1()).getSymNodeSym();
        else if (lHir.getChild1() instanceof SymNode)
          lSubp = ((SymNode)lHir.getChild1()).getSymNodeSym();
        if (lSubp != null) {
          String lSubpName = lSubp.getName().intern();
          if ((lSubpName == "_bnfOr")||
              (lSubpName == "_bnfSeq")||
              (lSubpName == "_reform")||
              (lSubpName == "_assignStmt")||
              fNonterminalSet.contains(lSubp)) {
            lHir.setFlag(HIR.FLAG_NONTERMINAL, true);
            if (fDbgLevel > 0)
              dbgOut(4, "\n setNonterminalFlag " + lHir.toStringShort());
          }
        }
       }
    }
  }
} // setNonterminalFlag

/**
 * Get the index number of a pattern or a nonterminal.
 * @param pSubp is  a pattern or a nonterminal.
 * @return the index value.
 */
int getIndex( Subp pSubp )
{
  if (((SubpImpl)pSubp).getOptInf() != null)
    return ((OptInf)((SubpImpl)pSubp).getOptInf()).fIndex;
  else
    return 0;
} // getIndex

/**
 * If pDeclVar is declared as transparent to some variables
 * and if pHir contains any of such variables or
 * pDeclVar is included in pHir then
 * pHir is treated as not transparent to pDeclVar
 * @param pDeclVar variable that should be transparent.
 * @param pHir HIR to be examined.
 * @return true if tranparent.
 */
protected boolean
isTransparent( Var pDeclVar, HIR pHir, MatchingData pData )
{
  if (pHir != null) {
    if (fDbgLevel > 1) {
      dbgOut(3, "\n isTransparent " + pDeclVar.getName() +
         " " + pHir.toStringWithChildren() +
         " " + dbgMap(pData.paramCorresp)
         + " " + dbgMap(pData.nontermParamCorresp));
    }
    MatchingData lData = new MatchingData(pData.nontermPat, pData);
    makeParamCorrespComplete(lData);
    if (fTransparentMap.containsKey(pDeclVar)) {
      Set lTransSetOrg = (Set)fTransparentMap.get(pDeclVar);
      Set lInstances = new HashSet(lTransSetOrg);
      for (Iterator lIt = lTransSetOrg.iterator();
           lIt.hasNext(); ) {
        Var lSym = (Var)lIt.next();
        lInstances.add(getVarInstance(lSym, pData.nontermPat));
      }
      lTransSetOrg.addAll(lInstances);
      Set lTransSet = new HashSet(lTransSetOrg);
      for (Iterator lIt = lTransSetOrg.iterator();
           lIt.hasNext(); ) {
        Var lVar = (Var)lIt.next();
        HIR lHir = traceParamCorrespSimply(pData, lVar );
        if (lHir instanceof SymNode) {
          Sym lSymCorresp = ((SymNode)lHir).getSymNodeSym();
          lTransSet.add(lSymCorresp);
          if (pData.paramCorresp.containsKey(lSymCorresp)) {
            HIR lHir2 = (HIR)pData.paramCorresp.get(lSymCorresp);
            if (lHir2 instanceof SymNode) {
              lTransSet.add(((SymNode)lHir2).getSymNodeSym());
            }
          }
        }
      }
      Set lLeafOperands = getLeafOperands(pHir);
      if (fDbgLevel > 3)
        dbgOut(4, "\n lTransSet " + lTransSet +
               " leafOperands " + lLeafOperands);
      lTransSet.retainAll(lLeafOperands);
      if ((! lTransSet.isEmpty())||
          lLeafOperands.contains(pDeclVar)) {
        if (fDbgLevel > 1) dbgOut(3, " isTransparent false");
        return false;
      }
      for (Iterator lIt = lLeafOperands.iterator();
           lIt.hasNext();) {
        Sym lSym = (Sym)lIt.next();
        if (lSym instanceof Subp) {
          if (fDbgLevel > 1) dbgOut(3, " isTransparent false");
          return false;
        }
      }
    }
    if (fDbgLevel > 1) dbgOut(3, " isTransparent true");
    return true;
  }else {
    // pHir is null.
    if (fDbgLevel > 1) dbgOut(3, " isTransparent true");
    return true;
  }
} // isTransparent

/**
 * Get the set of leaf operands of pHir.
 * Constant operands are excluded.
 * @param pHir HIR subtree.
 * @return the set of leaf operands.
 */
protected Set
getLeafOperands( HIR pHir )
{
  Set lLeafOperands = new HashSet();
  if (pHir != null) {
    for (HirIterator lIt = hir.hirIterator(pHir);
         lIt.hasNext(); ) {
      HIR lHir = lIt.next();
      if (lHir != null) {
        if (lHir instanceof SymNode) {
          if (lHir instanceof ConstNode)
            continue;
          Sym lSym = ((SymNode)lHir).getSymNodeSym();
          lLeafOperands.add(lSym);
        }
      }
    }
    if (fDbgLevel > 0)
      dbgOut(6, "\n  getLeafOperands " + lLeafOperands);
  }
  return lLeafOperands;
} // getLeafOperands

/**
 * List up top subtrees for pHir and add the subtrees to
 * pHeadingHirList.
 * A pattern or nonterminal may have multiple top subtrees
 * if the right-hand side of their production begins with _bnfOr
 * or begins with a nonterminal having multiple top subtrees.
 * @param pNontermPatSym pattern symbol or nonterminal symbol.
 * @param pHeadingHirList is a list to which top subtrees are
 *           to be added.
 * @param pHir is the right-hand side of the production
 *     corresponding to the pattern or the nonterminal.
 */
protected void
listUpHeadingHir( Subp pNontermPatSym, List pHeadingHirList, HIR pHir )
{
  if (pHir == null)
    return;
  if (fDbgLevel > 3)
    dbgOut(5, "\n listUpHeadingHir " + pHir.toStringShort());
  int lOpCode = pHir.getOperator();
  if (lOpCode == HIR.OP_EXP_STMT) {
    listUpHeadingHir(pNontermPatSym, pHeadingHirList, (HIR)pHir.getChild1());
    return;
  }else if (lOpCode == HIR.OP_CALL) {
    Sym lSym = null;
    if ((pHir.getChild1().getOperator() == HIR.OP_ADDR)&&
        (pHir.getChild1().getChild1() instanceof SymNode))
      lSym = ((SymNode)pHir.getChild1().getChild1()).getSymNodeSym();
    else if (pHir.getChild1() instanceof SymNode)
      lSym = ((SymNode)pHir.getChild1()).getSymNodeSym();
    if (lSym != null) {
      HirList lArgList = (HirList)pHir.getChild2();
      String lSymName = lSym.getName().intern();
      if (lSymName == "_bnfOr") {
        HIR lElem = null;
        for (Iterator lIt = lArgList.iterator();
             lIt.hasNext(); ) {
          if (lElem == null) {
            lElem = (HIR)lIt.next();
            continue; // Skip the dummy parameter 1
          }
          lElem = (HIR)lIt.next();
          listUpHeadingHir(pNontermPatSym, pHeadingHirList, lElem);
        }
        return;
      }else if (lSymName == "_bnfSeq") {
        listUpHeadingHir(pNontermPatSym, pHeadingHirList, (HIR)lArgList.get(1));
        return;
      }else if (pHir.getFlag(HIR.FLAG_NONTERMINAL)||
                 fNonterminalSet.contains(lSym)) {
        if (fProductionMap.containsKey(lSym)) {
          HIR lProduction = (HIR)fProductionMap.get(lSym);
          listUpHeadingHir(pNontermPatSym, pHeadingHirList, lProduction);
        }
      }
    }
  }
  pHeadingHirList.add(pHir);
  if (fDbgLevel > 3)
    dbgOut(5, "add " + pHir.toStringShort());
} // listUpHeadingHir

/**
 * Record the correspondence between the parameter pParam and
 * the (matched) input pHir.
 * This method also records matchedInput and expandedHir to pData.
 * @param pData matching data inherited.
 * @param pParam symbol that may be (1) a pattern/nonterminal parameter instance
 *     or (2) a pattern symbol itself representing recursive function
 *     or (3) a fitting symbol instance.
 * @param pHir input HIR that is treaded as matched with pParam.
 * @return true if correspondence was found false if not found.
 */
protected boolean
recordParamCorresp( MatchingData pData, Sym pParam, HIR pHir )
{
  if (fDbgLevel > 1)
    dbgOut(3, "\nrecordParamCorresp " + pData.nontermPat
           + " " + pParam.getName() + " " + pHir);
  MatchingData lData = new MatchingData(pData.nontermPat, pData);
  if (pParam instanceof Param) {
    Param lParamInstance = getParamInstance((Param)pParam, lData.nontermPat);
    // lParamInstance is an instance of the parameter if it has
    // instance or the parameter itself if it has no instance.
    Param lOriginalParam = getOriginalParam((Param)pParam);
    if (lData.paramCorresp.containsKey(lParamInstance)||
        lData.nontermParamCorresp.containsKey(lParamInstance)) {
      // This parameter is already recorded.
      HIR lHirForParam;
      if (lData.paramCorresp.containsKey(lParamInstance)) {
        lHirForParam = (HIR)lData.paramCorresp.get(lParamInstance);
      }else {
        lHirForParam = (HIR)lData.nontermParamCorresp.get(lParamInstance);
      }
      if (isSameTree(lHirForParam, pHir)) {
        // Has the same correspondence with the previous occurence.
        lData.matchedInput = pHir;
        lData.expandedHir = traceParamCorresp(lData,
              (Var)lParamInstance, pHir, false); //##
        // REFINE check consistency
        if (lData.succeeded) {
          pData.reflect(lData);
          return true;
        }else {
          pData.succeeded = false;
          return false;
        }
      } // end of the same tree
      else if (isNontermParam(lOriginalParam)) {
        // lParam is a nonterminal parameter.
        lData.matchedInput = pHir;
        HIR lTracedHir = null;
        Sym lHirSym = null;
        if (lHirForParam instanceof SymNode) {
          lHirSym = ((SymNode)lHirForParam).getSymNodeSym();
          lTracedHir = traceParamCorresp(lData, (Var)lHirSym, pHir, false);
          if (! lData.succeeded) {
            pData.succeeded = false;
            return false;
          }
          pData.reflect(lData);
          // REFINE check consistency
          if (fDbgLevel > 2)
            dbgOut(6, " lHirForParam " + lHirSym.getName() +
              " lTracedHir " + lTracedHir.toStringWithChildren());
          if ((lTracedHir instanceof SymNode) &&
              isPatternParam(
                    ((SymNode)lTracedHir).getSymNodeSym())) {
            // The nonterminal parameter was mapped to a pattern parameter.
            Sym lPatternParam = ((SymNode)lTracedHir).getSymNodeSym();
            if ((!(pHir instanceof SymNode)) ||
                ((((SymNode)pHir).getSymNodeSym() != pParam) &&
                 (((SymNode)pHir).getSymNodeSym() != pParam))) {
              // Not a recursive reference.
              if (!lData.paramCorresp.containsKey(lPatternParam)) {
                // There is an indirect reference from lPatternParam to pHir.
                putToMap(lData.paramCorresp, lPatternParam, pHir);
              }
              pData.reflect(lData);
              pData.expandedHir = hir.varNode((Var)lPatternParam);
              pData.succeeded = true;
              return true;
            }
          }
          //##81 BEGIN
          else {
            // The nonterminal parameter correspondes to an HIR
            // that is neither a pattern parameter
            // nor a nonterminal parameter.
            boolean lTrueFalse = false;
            HIR lValue = null;
            if (lData.paramCorresp.containsKey(lParamInstance))
              lValue = (HIR)lData.paramCorresp.get(lParamInstance);
            else if (lData.nontermParamCorresp.containsKey(lParamInstance))
              lValue = (HIR)lData.nontermParamCorresp.get(lParamInstance);
            if (lValue != null) { // Correspondence is given.
              if (isSameTree(lValue, lTracedHir)) {
                lTrueFalse = true;
                //##86 pData.expandedHir = lTracedHir;
                lData.expandedHir = lTracedHir.copyWithOperandsChangingLabels(null); //##86 Unnecessary to copy ?
              }else {
                if (fDbgLevel > 1)
                  dbgOut(3, " previous value of " + lParamInstance.getName() +
                    " " + lValue.toStringWithChildren() +
                    " differs with " + lTracedHir.toStringWithChildren());
                lTrueFalse = true;
                //## pData.expandedHir = pHir.copyWithOperandsChangingLabels(null); // BAD
                lData.expandedHir = lTracedHir.copyWithOperandsChangingLabels(null); // BAD
              }
              if (lTracedHir instanceof VarNode) {
                Var lTracedVar = (Var)((VarNode)lTracedHir).getSymNodeSym();
                if (lTracedVar.getSymType().isConst()&&
                    (pHir instanceof ConstNode)) {
                   if (lData.paramCorresp.containsKey(lTracedVar)) {
                     if (! isSameTree((HIR)lData.paramCorresp.get(lTracedVar), pHir)) {
                       if (fDbgLevel > 1)
                         dbgOut(3, " previous value of " + lTracedVar.getName() +
                           " " + ((HIR)lData.paramCorresp.
                             get(lTracedVar)).toStringWithChildren() +
                           " differs with " + pHir.toStringWithChildren());
                       // Do not revise but leave the correspondence unchanged.
                    }
                   }else {
                     putToMap(lData.paramCorresp, lTracedVar, pHir);
                   }
                }
              }
            }else {
              // No previous value.
              lTrueFalse = true;
              //##86 putToMap(pData.paramCorresp, lParamInstance, lTracedHir);
              if (isPatternParam(lOriginalParam))
                putToMap(lData.paramCorresp, lParamInstance, lTracedHir);
              else
                putToMap(lData.nontermParamCorresp, lParamInstance, lTracedHir);
              //##86 pData.expandedHir = lTracedHir;
              lData.expandedHir = lTracedHir.copyWithOperandsChangingLabels(null); //##86 Unnecessary to copy ?
            }
            if (lData.succeeded)
              pData.reflect(lData);
            pData.succeeded = lTrueFalse;
            return lTrueFalse;
          }
          //##81 END
        } // end of SymNode
        else {
          // Change the correspondence.
          if (fDbgLevel > 2)
            dbgOut(3, " Change correspondence of " + lParamInstance.getName()
              + " from " + lHirForParam + " to " + pHir.toStringWithChildren());
          putToMap(lData.nontermParamCorresp, lParamInstance, pHir);
        }
        lData.expandedHir = traceParamCorresp(lData, (Var)lParamInstance, pHir, false); //##
        // REFINE check consistency
        if (lData.succeeded) {
          pData.reflect(lData);
          return true;
        }else {
          pData.succeeded = false;
          return false;
        }
      }else {
        dbgOut(3, " Param " + lParamInstance.getName()  //##80
          + " has different correspondence: " + lHirForParam
          + " and " + pHir.toStringWithChildren());
        pData.succeeded = false;
        return false;
      }
    } // end of already appeared param
    else {
      // First apperance of this parameter.
      if (pHir != null) {
        Type lParamType = pParam.getSymType();
        Type lHirType = pHir.getType();
        if ((lParamType == lHirType)||
            fStmtParamSet.contains(lOriginalParam)||
            (lParamType.getFinalOrigin() == lHirType.getFinalOrigin())||
            ((lParamType instanceof PointerType)&&(lHirType instanceof VectorType))||
            ((lParamType instanceof VectorType)&&(lHirType instanceof PointerType))
            ) {
          if (lParamType.isConst()) {
            if (! (pHir instanceof ConstNode)) {
              dbgOut(6, "Const param " + pParam.getName()
                     + " request constant instead of " + pHir.toStringShort());
            }
          }
          // Record the correspondence.
          if (isNontermParam(lOriginalParam)&&
              (pHir instanceof SymNode)&&
              (isNontermParam(((SymNode)pHir).getSymNodeSym())||
               isPatternParam(((SymNode)pHir).getSymNodeSym()))) {
            putToMap(lData.nontermParamCorresp, lParamInstance, pHir);
          }else if (isPatternParam(lOriginalParam)) {
            putToMap(lData.paramCorresp, lParamInstance, pHir);
          }else {
            //##? putToMap(pData.paramCorresp, lParamInstance, pHir);
          }
          lData.matchedInput = pHir;
          lData.expandedHir = traceParamCorresp(lData, (Var)lParamInstance, pHir, false); //##
          // REFINE check consistency
          if (lData.succeeded) {
            pData.reflect(lData);
            return true;
          }else {
            pData.succeeded = false;
            return false;
          }
        }
        else {
          dbgOut(3, " Param " + lParamInstance.getName() + " may correspond to " +
            pHir.toStringShort() + " but has different type.");
          pData.succeeded = false;
          return false;
        }
      }else {
        dbgOut(3, "Param " + pParam.getName() + " corresponds to null ");
        if (isNontermParam(lOriginalParam)&&
            (pHir instanceof SymNode)&&
            (((SymNode)pHir).getSymNodeSym() instanceof Param)&&
            isNontermParam(((SymNode)pHir).getSymNodeSym())) {
          putToMap(lData.nontermParamCorresp, lParamInstance, pHir);
        }else {
          putToMap(lData.paramCorresp, lParamInstance, pHir);
         }
        lData.matchedInput = pHir;
        lData.expandedHir = traceParamCorresp(lData, (Var)lParamInstance, pHir, false); //##
        // REFINE check consistency
        if (lData.succeeded) {
          pData.reflect(lData);
          return true;
        }else {
          pData.succeeded = false;
          return false;
        }
      }
    } // End of first apperance of this parameter.
  } // End of (pParam instanceof Param)
  else if (pParam == fCurrentPatternSym) {
    // The pattern may represent a recursive function.
    if (pHir instanceof SymNode) {
      Sym lHirSym = ((SymNode)pHir).getSymNodeSym();
      if (lHirSym instanceof Subp) {
        lData.matchedInput = pHir;
        putToMap(lData.paramCorresp, pParam, pHir);
        // Fitting symbol usually contains no parameters.
        lData.expandedHir = pHir.copyWithOperandsChangingLabels(null);
        lData.succeeded = true;
        pData.reflect(lData);
        return true;
      }
    }
    if (fDbgLevel > 2)
      dbgOut(6, "\nRecursively used pattern " + pParam.getName()
             + " does not match with " + pHir.toStringWithChildren());
    pData.succeeded = false;
    return false;
  }
  else if (isFittingSym(pParam)) {
    // pParam is a fitting symbol.
    if (pHir.getType().isCompatibleWith(pParam.getSymType())||
        (pHir.getType() instanceof PointerType)||
        (pHir.getType() instanceof VectorType)){
      Sym lSymInstance = getVarInstance((Var)pParam, lData.nontermPat);
      lData.matchedInput = pHir;
      putToMap(lData.paramCorresp, lSymInstance, pHir);
      // Fitting symbol usually contains no parameters.
      lData.expandedHir = pHir.copyWithOperandsChangingLabels(null);
      lData.succeeded = true;
      pData.reflect(lData);
      return true;
    }else {
      // Type of the fitting symbol differs.
      dbgOut(6, " Type of fitting symbol differs for " + pParam.getName());
      pData.succeeded = false;
      return false;
    }
  } // End of fitting symbol.
  // Neither parameter nor fitting symbol.
  dbgOut(6, " Symbol (neither parameter nor fitting symbol) differs");
  pData.succeeded = false;
  return false;
} // recordParamCorresp

/**
 * Put the correspondence of pSym-pHir to pMap
 * if it is not a circular reference that is,
 * if pSym is not an operand of pHir.
 * If the correspondence is not recorded in fGlobalPatternParamMap,
 * then record it. If pSym is recorded in fGlobalPatternParamMap
 * as different tree, then "Inconsistent with previous value"
 * message is issued.
 * @param pMap map to record the correspondence.
 * @param pSym variable (usually a parameter instance).
 * @param pHir HIR subtree that corresponds to pSym.
 */
protected void
putToMap( Map pMap, Sym pSym, HIR pHir )
{
  if ((pSym != null)&&(pHir != null)) {
    Set lOperands = getLeafOperands(pHir);
    if (! lOperands.contains(pSym)) {
      if (isPatternParam(pSym)) {
        if (fGlobalPatternParamMap.containsKey(pSym)) {
          HIR lPrevHir = (HIR)fGlobalPatternParamMap.get(pSym);
          if (! isSameTree(pHir, lPrevHir)) {
            if (fDbgLevel > 1)
              dbgOut(3, " Inconsistent with previous value "
                + lPrevHir.toStringWithChildren());
           }
        }else {
          // Record the mapping.
          fGlobalPatternParamMap.
            put(pSym, pHir.copyWithOperandsChangingLabels(null));
        }
     }
      pMap.put(pSym, pHir);
      if (fDbgLevel > 3)
        dbgOut(4, " put(" + pSym.getName() + ","
               + pHir.toStringWithChildren() + ")");
    }else {
      if (fDbgLevel > 3)
        dbgOut(4, " circular put(" + pSym.getName() + ","
               + pHir.toStringWithChildren() + ") is ignored");
    }
  }
} // putToMap

protected void
dbgOut( int pLevel, String pMessage)
{
  ioRoot.dbgOpt1.print(pLevel, pMessage);
}

protected void
dbgOut( int pLevel, String pMessageClass, String pMessage)
{
  ioRoot.dbgOpt1.print(pLevel, pMessageClass, pMessage);
}

/**
 * make the text string showing the contents of the map pMap.
 * @param pMap Map to be displayed in the form of text string.
 * @return the resultant test string.
 */
protected String
dbgMap( Map pMap )
{
  if (pMap == null)
    return "null";
  else if (pMap.isEmpty())
    return "{}";
  StringBuffer lBuf = new StringBuffer("{");
  boolean lFirst = true;
  for (Iterator lIt = pMap.keySet().iterator();
       lIt.hasNext(); ) {
    Object lKey = lIt.next();
    String lKeyString;
    if (lKey instanceof Sym)
      lKeyString = ((Sym)lKey).getName();
    else if ((lKey instanceof HIR) || (lKey instanceof IR))
      lKeyString = ((HIR)lKey).toStringShort();
    else
      lKeyString = lKey.toString();
    Object lValue = pMap.get(lKey);
    String lValueString;
    if (lValue instanceof Sym)
      lValueString = ((Sym)lValue).getName();
    else if ((lValue instanceof HIR) || (lValue instanceof IR)) {
      if ((fDbgLevel > 3)&&(lValue instanceof HIR))
        lValueString = ((HIR)lValue).toStringWithChildren();
      else
        lValueString = ((IR)lValue).toStringShort();
    }
    else if (lValue == null)
      lValueString = "null";
    else
      lValueString = lValue.toString();
    if (! lFirst)
      lBuf.append(", ");
    lFirst = false;
    lBuf.append(lKeyString);
    lBuf.append("=");
    lBuf.append(lValueString);
  }
  lBuf.append("}");
  return lBuf.toString();
} // dbgMap

//##91 BEGIN
protected String
dbgSet( Set pSet )
{
  if (pSet == null)
    return "null";
  else if (pSet.isEmpty())
    return "{}";
  StringBuffer lBuf = new StringBuffer("{");
  boolean lFirst = true;
  for (Iterator lIt = pSet.iterator();
       lIt.hasNext(); ) {
    Object lKey = lIt.next();
    String lKeyString;
    if (lKey instanceof Sym)
      lKeyString = ((Sym)lKey).getName();
    else if ((lKey instanceof HIR) || (lKey instanceof IR))
      lKeyString = ((HIR)lKey).toStringShort();
    else
      lKeyString = lKey.toString();
    if (! lFirst)
      lBuf.append(", ");
    lFirst = false;
    lBuf.append(lKeyString);
  }
  lBuf.append("}");
  return lBuf.toString();
} // dbgSet
//##91 END

//##93 BEGIN
public String
toStringWC( HIR pHir ) // previous name is hirToString.
{
  if (pHir == null)
    return "null";
  else
    return pHir.toStringWithChildren();
}

public HIR
hirCopyWithOperands( HIR pHir )
{
  if (pHir == null)
    return null;
  else
    return pHir.copyWithOperands();
}

public HIR
hirCopyWithOperandsChangingLabels( HIR pHir )
{
  if (pHir == null)
    return null;
  else
    return pHir.copyWithOperandsChangingLabels(null);
}
//##93 END
/**
 * OptInf is used to represent an object instanciated
 * for each pattern and nonterminal.
 * It keeps index number for patterns and nonterminals.
 */
protected class
OptInf
{
  public int fIndex = 0;
} // OptInf

/**
 * PatternCodeRange represents
 * pattern code range including upper value and lower value.
 */
protected class
PatternCodeRange
{
  public double fUpper;
  public double fLower;
} // PatternCodeRange

/**
 * Record the copy of pExpandedHir to
 *   fExpandedNontermInstance using pNontermPatInstance as key and
 *   fExpandedNonterm using makeHirKey(pCallExp) as key.
 * @param pCallExp call expression for the nonterminal.
 * @param pExpandedHir expanded HIR for the nonterminal.
 * @param pNontermPatInstance nonterminal instance.
 */
protected void
recordExpandedHir( HIR pCallExp, HIR pExpandedHir,
   NontermPatInstance pNontermPatInstance )
{
  if ((pCallExp == null)||(pExpandedHir == null)||
      (pNontermPatInstance == null)) {
    if (fDbgLevel > 0)
      dbgOut(1, "\nrecordExpandedHir has null argument "
             + pCallExp + " " + pExpandedHir + " " + pNontermPatInstance);
    return;
  }
  HIR lExpandedHir = pExpandedHir.copyWithOperandsChangingLabels(null);
  fExpandedNontermInstance.put(pNontermPatInstance, lExpandedHir);
  HIR lKey = makeHirKey(pCallExp);
  fExpandedNonterm.put(lKey, lExpandedHir);
  Subp lNonterminal = pNontermPatInstance.nontermPat;
  int lIndex = getIndex(lNonterminal);
  //##88 fLastExpandedNonterm[lIndex] = lExpandedHir;
  if (fDbgLevel > 3)
    dbgOut(6, "\nrecordExpandedHir " + pCallExp.toStringWithChildren()
           +
           " key "+ lKey.toStringWithChildren()
           + "= " + pExpandedHir.toStringWithChildren());
} // recordExpandedHir

/**
 * Get the expanded HIR corresponding to pCallExp
 * calling pNonterminal. The same nonterminal may have
 * different expansion according to its actual parameter
 * list.
 * //##86 REFINE Is there anay case where call expressions
 * // of the same type has different expansion ?
 * @param pCallExp expression calling pNonterminal.
 * @param pNonterminal nonterminal called by pCallExp.
 * @return the expanded HIR corresponding to pCallExp.
 */
protected HIR
getExpandedHir( HIR pCallExp,
                NontermPatInstance pNontermInstance )
{
  HIR lResult;

  if (fExpandedNontermInstance.containsKey(pNontermInstance)) {
    lResult = (HIR)fExpandedNontermInstance.get(pNontermInstance);
  }else {
    HIR lKey = makeHirKey(pCallExp);
    if (fExpandedNonterm.containsKey(lKey)) {
      lResult = (HIR)fExpandedNonterm.get(lKey);
    }
    else {
      Subp lNonterminal = pNontermInstance.nontermPat;
      //##88 int lIndex = getIndex(lNonterminal);
      //##88 lResult = fLastExpandedNonterm[lIndex];
      lResult = null; //##88
    }
  }
  if (lResult != null) {
    if ((lResult.getOperator() == HIR.OP_EXP_STMT)&&
        (lResult.getChild1().getOperator() == HIR.OP_CALL)&&
        (lResult.getChild1().getChild1() instanceof SymNode)&&
        (((SymNode)lResult.getChild1().getChild1()).getSymNodeSym()
        .getName() == "_assignStmt")) {
      if (fDbgLevel > 2)
        dbgOut(6, "getExpandedHir has _assignStmt "
               + lResult.toStringWithChildren());
      IrList lArgList = (IrList)lResult.getChild1().getChild2();
      lResult = hir.assignStmt(
          (Exp)lArgList.get(1), (Exp)lArgList.get(2));
    }
  }
  if (fDbgLevel > 2) {
    if (lResult != null)
      dbgOut(6, "\n  getExpandedHir of " + pNontermInstance
           + "= " + lResult.toStringWithChildren());
    else
      dbgOut(6, "\n  getExpandedHir of " + pNontermInstance
            + " result undefined");
  }
  return lResult;
} // getExpandedHir

/**
 * Make an HIR that can be used as a key of maps
 * so that there is only one instance for each group of
 * HIR subtrees where member subtrees have the same shape
 * when they are included in the same group
 * (keeping one to one correspondence between key
 * and group).
 * @param pExp HIR subtree to which key is to be searched.
 * @return the key corresponding to the group to which
 *     pExp belongs.
 */
protected HIR
  makeHirKey( HIR pExp )
{
  HIR lKey;
  if (fHirKeys.contains(pExp)) {
    return pExp;
  }else {
    for (Iterator lIt = fHirKeys.iterator();
         lIt.hasNext(); ) {
      HIR lHir = (HIR)lIt.next();
      if (isSameTree(pExp,lHir)) {
        return lHir;
      }
    }
    lKey = pExp.copyWithOperands();
    fHirKeys.add(lKey);
    return lKey;
  }
} // makeHirKey

/**
 * Get the instance of the nonterminal pNonterm corresponding
 * to the nonterminal call expression pCallExp
 * seeing callToNontermInstanceMap of the parent nonterminal
 * pParentInstance.
 * If it is not found then look for the ancestor of the parent
 * (parent of the parent, etc.).
 * (This method is refered in expandExp.)
 * @param pNonterm Nonterminal symbol.
 * @param pCallExp Nonterminal call expression.
 * @param pParentInstance Instance of parent nonterminal or pattern
 *     whose definition body contains the nonterminal call pCallExp.
 * @return the nonterminal instance.
 */
protected NontermPatInstance
getNontermInstance( Subp pNonterm, HIR pCallExp,
        NontermPatInstance pParentInstance )
{
  if (pParentInstance.callToNontermInstanceMap.containsKey(pCallExp)) {
    return (NontermPatInstance)pParentInstance.
       callToNontermInstanceMap.get(pCallExp);
  }else if (pParentInstance.parent != null) {
    return getNontermInstance(pNonterm, pCallExp,
            pParentInstance.parent);
  }else {
    if (fDbgLevel > 0)
      dbgOut(2, "\n NontermInstance not found for " + pCallExp
             + " " + pCallExp.toStringWithChildren()
             + " use fMatchingDataForNonterm or parent nonterm instance.");
    HIR lKey = makeHirKey(pCallExp);
    if (fMatchingDataForNonterm.containsKey(lKey)) {
      MatchingData lData =
        (MatchingData)fMatchingDataForNonterm.get(lKey);
      return lData.nontermPat;
    }else {
      return pParentInstance;
    }
  }
} // getNontermInstance

/**
 * Get a new instance of the parameter pParam used in
 * the nonterminal pnonterm.
 * @param pNonterm Nonterminal symbol.
 * @param pParam Formal parameter of pNonterm.
 * @param pInstanceNumber Instance number.
 * @return the instanciated parameter.
 */
protected Param
instanciateParam( Subp pNonterm, Param pParam,
  int pInstanceNumber )
{
  return (Param)instanciateVar(pNonterm, pParam, Sym.KIND_PARAM,
          pInstanceNumber);
} // instanciateParam

/**
 * Get the new instance of the variable pVar.
 * @param pNonterm Nonterminal containing the variable pVar.
 * @param pVar Variable symbol.
 * @param pKind Symbol kind of the variable.
 * @param pInstanceNumber Instance number within the nonterminal
 *    (this parameter is not yet used).
 * @return the instanciated variable.
 */
protected Var
instanciateVar( Subp pNonterm, Var pVar, int pKind,
                int pInstanceNumber )
{
  String lVarName = pVar.getName();
  Var lNewInstance = (Var)symRoot.symTableCurrentSubp.
    generateSym(pVar.getSymType(), pKind, (lVarName+"_").intern(),
                symRoot.subpCurrent);
  lNewInstance.setSymType(pVar.getSymType());
  fOriginalVarMap.put(lNewInstance, pVar);
  return lNewInstance;
} // instanciateVar

/**
 * Get the parameter instanciated from pParam for the
 * instance of the nonterminal pNontermInstance.
 * (Formal parameters are instanciated for each
 *  nonterminal instance.)
 * @param pParam Formal parameter from which new parameter
 *    is instanciated.
 * @param pNontermInstance Instance of a nonterminal.
 * @return the instance of the parameter.
 */
protected Param
getParamInstance( Param pParam, NontermPatInstance pNontermInstance )
{
  return (Param)getVarInstance(pParam, pNontermInstance);
} // getParamInstance

/**
 * Get the variable instanciated from pVar for the
 * instance of the nonterminal pNontermInstance.
 * (Local variables (including formal parameters) are
 *  instanciated for each nonterminal instance.)
 * If not found, then search among variables instanciated
 * for ancestor nonterminal instances.
 * @param pVar Variable  from which new variable is instanciated.
 * @param pNontermInstance Instance of a nonterminal.
 * @return the instance of the variable.
 */
protected Var
getVarInstance( Var pVar, NontermPatInstance pNontermInstance )
{
  if (fDbgLevel > 4)
    dbgOut(6, "\n  getVarInstance of " + pVar.getName()
           + " in " + pNontermInstance);
  Var lResultVar = pVar;
  if ((pNontermInstance != null)&&
      (pNontermInstance.oldToNewParamMap != null)) {
    if (fDbgLevel > 4)
      dbgOut(6, " " + dbgMap(pNontermInstance.oldToNewParamMap));
    if (pNontermInstance.oldToNewParamMap.containsKey(pVar)) {
      lResultVar = (Var)pNontermInstance.oldToNewParamMap.get(pVar);
    }else if (pNontermInstance.parent != null) {
      NontermPatInstance lParent = pNontermInstance.parent;
      while (((lParent.nontermPat.getName() == "_bnfOr")||
              (lParent.nontermPat.getName() == "_assignStmt"))&&
              (lParent.parent != null)) {
         lParent = lParent.parent;
       }
      if (lParent.oldToNewParamMap.containsKey(pVar)&&
          (lParent.oldToNewParamMap.get(pVar) != pVar))
        lResultVar = getVarInstance(pVar, lParent);
    }
  }
  if (fDbgLevel > 4)
    dbgOut(6, " = " + lResultVar.getName());
  return lResultVar;
} // getVarInstance

/**
 * Get the original parameter from which pParam is instanciated.
 * If pParam is already the original parameter, then return it.
 * @param pParam Instance of a formal parameter.
 * @return the original parameter.
 */
protected Param
getOriginalParam( Param pParam )
{
  return (Param)getOriginalVar(pParam);
} // getOriginalParam

/**
 * Get the original variable from which pVar is instanciated.
 * If pVar is already the original variable, then return it.
 * @param pVar Instance of a variable.
 * @return the original variable.
 */
protected Var
getOriginalVar( Var pVar )
{
  if (fOriginalVarMap.containsKey(pVar))
    return (Var)fOriginalVarMap.get(pVar);
  else
    return pVar;
} // getOriginalVar

/**
 * If pSym is a pattern parameter or an instance of a
 * pattern parameter, then return true, else return false.
 * @param pSym Symbol to be examined.
 * @return true or false.
 */
protected boolean
isPatternParam( Sym pSym )
{
  if (pSym instanceof Param) {
    Param lOriginalParam = getOriginalParam((Param)pSym);
    if (fPatternParameters.contains(lOriginalParam))
      return true;
    else
      return false;
  }
  return false;
} // isPatternParam

/**
 * If pSym is a nonterminal parameter or an instance of a
 * nonterminal parameter, then return true, else return false.
 * @param pSym Symbol to be examined.
 * @return true or false.
 */
protected boolean
isNontermParam( Sym pSym )
{
  if (pSym instanceof Param) {
    Param lOriginalParam = getOriginalParam((Param)pSym);
    if (fNonterminalParameters.contains(lOriginalParam))
      return true;
    else
      return false;
  }
  return false;
} // isNontermParam

/**
 * If pSym is a fitting symbol or an instance of a
 * fitting symbol, then return true, else return false.
 * @param pSym Symbol to be examined.
 * @return true or false.
 */

protected boolean
isFittingSym( Sym pSym )
{
  if (pSym instanceof Var) {
    Var lOriginalVar = getOriginalVar((Var)pSym);
    if (fFittingSet.contains(lOriginalVar))
      return true;
    else
      return false;
  }
  return false;
} // isFittingSym

//##86 END

/**
 * The class MatchingData represents various matching insormation
 * and provides methods to set/refer them.
 */
protected class
MatchingData
{
  public NontermPatInstance nontermPat;   // Nonterminal symbol instance,
                            // pattern symbol, or _bnfOr, _bnfSeq.
  public Map paramCorresp;  // Map pattern parameter instance
                            // to corresponding input.
  public Map nontermParamCorresp; // Map nonterminal parameter
                            // instance to parent nonterminal parameter.
  public HIR matchedProduction; // Selected production for nonterminal.
                                // Null for pattern symbol ?
  public HIR matchedInput;  // Input Exp/Stmt that matched to this nontermPat.
                            // It is not copied but points to the original HIR.
  public HIR expandedHir;   // Expanded Exp/Stmt for this
    // instance of nontermPat.
    // In expandedHir, a nonterminal is replaced by its RHS (right hand side)
    // Exp/Stmt. _bnfOr is replaced by RHS of the selected production,
    // nonterminal parameter is replaced by Exp of pattern parameter,
    // fitting symbol is replaced by corresponding input Exp.
    // Pattern parameter is not yet replaced in the expandedHir.
    // Thus, expandedHir does not contain any of nonterminal symbol,
    // _bnfOr, _bnfSeq, nonterminal parameter, fitting symbol
    // but contains pattern parameters.
    // It does not points to the original HIR but it is a new instance
    // generated by copying and modifying the original HIR.
  public boolean succeeded; // true if the last matching succeeded.
         // Set in traceParamCorresp, etc.
  public MatchingData parentData; // Matching data corresponding to
         // outer construct that invoked this nontermPat symbol.
  public boolean madeComplete = false; // true if processed by
         // makeParamCorrespComplete.  //##86

/**
 * Make an instance of MatchingData for the nonterminal or pattern
 * pnontermPat inheriting information of the parent matchingData
 * pParentData.
 * Inherited information:
 *   paramCorresp, nontermParamCorresp, matchedInput, succeeded,
 *   and if parent represent the same nonterminal or pattern, then
 *   matchedProduction and expandedHir.
 * @param pNontermPat Instance of a nonterminal or pattern.
 * @param pParentData Parent matching data to inherit.
 */
  public MatchingData( NontermPatInstance pNontermPat, MatchingData pParentData )
  {
    nontermPat = pNontermPat;
    paramCorresp = new HashMap();
    nontermParamCorresp = new HashMap();
    matchedProduction = null;
    matchedInput = null;
    succeeded = false;
    expandedHir = null;
    parentData = pParentData;
    if (fDbgLevel > 1)
      dbgOut(3, "\n    new MatchingData " + pNontermPat);
    if (pParentData != null) {
      // Inherit parent information.
      if (fDbgLevel > 1) {
        if (pParentData != null) {
          dbgOut(3, " parent " + pParentData.nontermPat);
          dbgOut(5, "\n    " + dbgMap(pParentData.paramCorresp)
                 + " " + dbgMap(pParentData.nontermParamCorresp));
        }else
          dbgOut(3, " parent null");

      }
      paramCorresp.putAll(pParentData.paramCorresp);
      nontermParamCorresp.putAll(pParentData.nontermParamCorresp);
      if (nontermPat == pParentData.nontermPat) {
        // If the same nonterminal, transfer matchedProduction.
        matchedProduction = pParentData.matchedProduction;
        expandedHir = pParentData.expandedHir; //##
      }else {
        // matchedProduction and expandedHir are null.
      }
      matchedInput = pParentData.matchedInput;
      succeeded = pParentData.succeeded;
    }
  } // constructor
/*
  public MatchingData( MatchingData pParentData )
  {
    new MatchingData(pParentData.nontermPat, pParentData);
  }
*/
/**
 * For this MatchingData, set following information of pData:
 *   paramCorresp, nontermParamCorresp,
 *   matchedProduction, matchedInput, expandedHir, succeeded.
 * @param pData Matching data from which information is to be copied.
 * @return Nwe instance of the matching data.
 */
  public MatchingData
  reflect( MatchingData pData )
  {
    if (fDbgLevel > 1) {
      dbgOut(4, "\n reflect " + pData.nontermPat + " to " +
             nontermPat);
    }
    paramCorresp.putAll(pData.paramCorresp);
    nontermParamCorresp.putAll(pData.nontermParamCorresp);
    if ((nontermPat == pData.nontermPat)
        ||(! fNonterminalSet.contains(nontermPat))
        ||(! fNonterminalSet.contains(pData.nontermPat))
        ) {
      // If same nonterminal or _bnfOr, copy matchedProduction.
      matchedProduction = pData.matchedProduction;
    }
    matchedInput = pData.matchedInput;
    if (pData.expandedHir != null) {
      if ((nontermPat == pData.nontermPat)
          ||(! fNonterminalSet.contains(nontermPat.nontermPat))
          ||(! fNonterminalSet.contains(pData.nontermPat.nontermPat))
          ) {
        expandedHir = pData.expandedHir;
      }
    }
    succeeded = pData.succeeded;
    // parentData = pData.parentData;
    if (fDbgLevel > 1) {
      if (matchedInput != null)
        dbgOut(5, " matchedInput: " + matchedInput.toStringWithChildren());
      else
        dbgOut(5, " matchedInput: null");
      if (matchedProduction != null)
        dbgOut(5, " production " + matchedProduction.toStringWithChildren());
      if (expandedHir != null)
        dbgOut(5, "\n  expandedHir: " + expandedHir.toStringWithChildren());
      else
        dbgOut(5, "\n  expandedHir: null");
      dbgOut(5, "\n  " + dbgMap(paramCorresp)
              + " " + dbgMap(nontermParamCorresp));
    }
    return this;
  } // reflect

  /**
   * Reflect paramCorresp and nontermParamCorresp of pData
   * @param pData matching data to inherit.
   * @return this matching data updated.
   */
  public MatchingData
  reflectCorresp( MatchingData pData )
  {
    if (pData == null)
      return this;
    if (fDbgLevel > 1) {
      dbgOut(4, "\n reflectCorresp "+ pData.nontermPat +
             " to " + nontermPat);
    }
    paramCorresp.putAll(pData.paramCorresp);
    nontermParamCorresp.putAll(pData.nontermParamCorresp);
    if (fDbgLevel > 2) {
        dbgOut(5, " " + dbgMap(paramCorresp)
               + " " + dbgMap(nontermParamCorresp));
    }
    return this;
  } // reflectCorresp

  /**
   * Reflect paramCorresp of pData
   *  (do not reflect nontermParamCorresp).
   * @param pData matching data to inherit.
   * @return this matching data updated.
   */
  public MatchingData
  reflectParamCorresp( MatchingData pData )
  {
    if (pData == null)
      return this;
    if (fDbgLevel > 1) {
      dbgOut(4, "\n reflectParamCorresp "+ pData.nontermPat +
             " to " + nontermPat);
    }
    paramCorresp.putAll(pData.paramCorresp);
    if (fDbgLevel > 2) {
        dbgOut(5, " " + dbgMap(paramCorresp));
    }
    return this;
  } // reflectParamCorresp

  /**
   * Record the matched input and the matching data (pData)
   * corresponding to the call exp (pCallExp) of pNontermInstance.
   * Use pCallExp itself as the key for fMatchingDataForNontermInstance.
   * Use makeHirKey(pCallExp)as the key for fMatchingDataForNonterm.
   * This also records pData as the last matching data for
   * the nonterminal.
   * @param pNontermInstance nonterminal instance corresponding to pCallExp.
   * @param pCallExp expression calling pNonterminal.
   * @param pData matching data corresponding to the call exp.
   */
  public void
  recordMatchingData( NontermPatInstance pNontermInstance, HIR pCallExp,
                      MatchingData pData)
{
  if (fMatchingDataForNontermInstance.containsKey(pNontermInstance)) {
    if (fDbgLevel > 2) {
      dbgOut(3, "\n  NontermInstance " + pNontermInstance +
             " is already recorded for " +
             ((MatchingData)fMatchingDataForNontermInstance
              .get(pNontermInstance)).nontermPat);
    }
  }
  fMatchingDataForNontermInstance.put(pNontermInstance, pData);
  int lIndex = getIndex(pNontermInstance.nontermPat);
  HIR lKey = makeHirKey(pCallExp);
  fLastMatchingDataForNonterm[lIndex] = pData;
  fMatchingDataForNonterm.put(lKey, pData);
  //## fInputForArgList[lIndex].put(lKey, pData.matchedInput);
  if (fDbgLevel > 2) {
    dbgOut(5, "\n  recordMatchingData " + pNontermInstance +
           " put(" + lKey.toStringWithChildren() +
           "," + pData.matchedInput.toStringWithChildren() + ")" );
  }
} // recordMatchingData

/**
 * Get the instance of MatchingData that corresponds to the
 * nonterminal call pCallExp
 * (by refering fMatchingDataForNontermInstance).
 * @param pNontermInstance
 * @param pCallExp
 * @return the instance of matching data.
 */
public MatchingData
getMatchingData( NontermPatInstance pNontermInstance, HIR pCallExp )
{
  MatchingData lData;
  if (fMatchingDataForNontermInstance.containsKey(pNontermInstance)) {
    if (fDbgLevel > 2)
      dbgOut(6, "\n  getMatchingData of " + pNontermInstance
             + " for " + pCallExp.toStringWithChildren() );
    return (MatchingData)fMatchingDataForNontermInstance.get(pNontermInstance);
  }
  HIR lKey = makeHirKey(pCallExp);
  if (fMatchingDataForNonterm.containsKey(lKey)) {
    lData = (MatchingData)fMatchingDataForNonterm.get(lKey);
    if (lData != null) {
      if (fDbgLevel > 2)
        dbgOut(6, "\n  getMatchingData of " + pCallExp.toStringWithChildren());
      return lData;
    }
  }
  int lIndex = getIndex(pNontermInstance.nontermPat);
  if (fDbgLevel > 2)
    dbgOut(6, "\n  getMatchingData of "
           + pNontermInstance.nontermPat.getName()
           + " from fLastMatchingDataForNonterm");
  return fLastMatchingDataForNonterm[lIndex];
} // getMatchingData

} // subclass MatchingData

//##86 BEGIN

/**
 * This class holds information for the instance of
 * a nonterminal or a pattern and provides methods for them.
 */
protected class
NontermPatInstance
{
  public Subp nontermPat;     // Nonterminal or pattern.
  public int instanceNumber;  // Instance number ( > 0)
  public Map oldToNewParamMap;// Map original symbol to instanciated symbol.
                              // (parameter or fitting symbol)
  public HIR nontermCallExp;  // Expression calling the nontermPat or null.
  public NontermPatInstance parent; // Nonterminal or pattern
           // that calls the nontermPat.
           // If the nontermPat is an in-pattern, then the parent is null.
  public Map callToNontermInstanceMap; // Map nonterminal calls
           // (contained in the definition of the nonterminal)
           // to corresponding nonterminal instance.
/**
 * Generate an instance of the nonterminal pNonterminal.
 * @param pNonterminal Nonterminal symbol.
 * @param pInstanceNumber Instance number.
 * @param pCallExp Expression calling pNonterminal or null.
 * @param pParent Instance of the parent nonterminal that invokes
 *         pNonterminal (null if none).
 */
  public NontermPatInstance( Subp pNonterminal, int pInstanceNumber,
          HIR pCallExp, NontermPatInstance pParent )
  {
    if (fDbgLevel > 1) {
      dbgOut(4, "\n NontermPatInstance for " +
             pCallExp + " " + toStringWC(pCallExp)
             + " " + pInstanceNumber + " parent " + pParent);
    }
    nontermPat = pNonterminal;
    instanceNumber = pInstanceNumber;
    oldToNewParamMap = new HashMap();
    nontermCallExp = pCallExp;
    parent = pParent;
    callToNontermInstanceMap = new HashMap();
    if ((pCallExp != null)&&
         ((pParent == null)||
          fInPatternMap.containsKey(pParent.nontermPat))) {
      List lList;
      HIR lKey = makeHirKey(pCallExp);
      if (fNontermInstanceInPattern.containsKey(lKey))
        lList = (List)fNontermInstanceInPattern.get(lKey);
      else {
        lList = new LinkedList();
        fNontermInstanceInPattern.put(lKey, lList);
      }
      lList.add(this);
    }
  } // Constructor

  public String toString()
  {
    return nontermPat.getName() + ":" + instanceNumber;
  }
} // subclass NontermInstance

//##86 END

} // class GlobalReform
