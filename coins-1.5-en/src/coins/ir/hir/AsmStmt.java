/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


//##70 BEGIN

/**
<PRE>
 * The C front of COINS accepts following asm statement:
 *   asm("#param paramDecsriptionList \n"
 *       "#clobber destroyed-register-list \n"
 *       asmInstructionSequence ,
 *       actual-parameter-list );
 * where
 *   paramDescriptionList -> paramDescription
 *       | paramDescriptionList , paramDescription
 *   paramDescription ->
 *         %register    // input parameter variable (rvalue)
 *       | w%register   // output parameter variable (lvalue)
 *       | m%register   // input/output parameter variable (lvalue)
 *       | s            // constant representing static address
 *       | a            // constant repreZsenting automatic variable address
 *   destroyed-register-list -> %register // register contents is destroyed
 *       | destroyed-register-list , %register
 *   asmInstructionSequence ->
 *         "asmInstruction \n"
 *       | asmInstructionSequence "asmInstruction \n"
 *   actualParameterList ->
 *         expression   // expression representing actual parameter
 *       | actualParameterList , expression
 * The %register represents a register or a class of registers
 * defined in the TMD.
 * The expression representing an actual parameter is
 * evaluated and its value (in case of rvalue) or address
 * (in case of lvalue) is set as the contents of
 * corresponding input parameter register.
 * 1st parameter register is represented as %1,
 * 2nd parameter register is represented as %2, ...
 * in asmInstructions.
 * The asmInstruction is an assembly language instruction
 * of the target machine.
 * The asm statement of COINS is not compatible with
 * that of gcc.
 * The asm statement is represented as AsmStmt in HIR
 * as follows:
 *  AsmStmt   ->
 *    ( asmCode attr  // Asm statement.
 *      StringConst @ // String constant representing
 *                    // parameter description pragma,
 *                    // clobber specification pragma, and
 *                    // assembly language instruction sequence
 *      HirList @ )   // List of l-value expressions (variable nodes,
 *                    // pointer expressions, etc.) and arithmetic
 *                    // expressions representing actual parameters.
 *
 * The above StringConst expression is the concatenation of
 *   paramDescriptionList, destroyed-register-list, and
 *   asmInstructionSequence.
 *
C program example:

 int  y = 1, vec[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
 int printf(char*, ...);
 int main() {
   int autov = 2, i = 3, x;
   // x = vec[i-1] + autov; y = y + y;
   asm("#param a,s,%I32,w%I32,m%I32\n"
       " mov %2(,%3,4),%4\n"
       " add %1(%%ebp),%4\n"
       " add %5,%5\n",
       &autov, vec, i-1, x, y);
   printf("x=%d y=%d\n", x, y);
   return 0;
 }

HIR corresponding to the example:

 (asm      31 void line 8
  <const    32 <VECT 76 0 char> "#param a,s,%I32,w%I32,m%I32\n mov %2(,%3,4),%4\n add %1(%%ebp),%4\n add %5,%5\n">
  (list 33
   (addr     34 <PTR int>
    <var      35 int autov>)
   (decay    36 <PTR int>
    <var      37 <VECT 10 0 int> vec>)
   (sub      38 int
    <var      39 int i>
    <const    40 int 1>)
   <var      41 int x>
   <var      42 int y>))
</PRE>
 **/
public interface
AsmStmt extends Stmt
{

/**
 * @return the 1st operand representing #param, #clobber
 *   and instruction sequence.
 */
public String
getInstructions();

/**
 * @return the list including actual parameter expressions. operand representing #param, #clobber
 */
public HirList
getActualParamList();

/**
 * Set the 1st operand representing #param, #clobber
 *   and instruction sequence.
 * @param the 1st operand to be set.
 */
public void
setInstructions( String pInstructions );

/**
 * Set the list including actual parameter expressions. operand representing #param, #clobber
 * @param actual parameter list to be set.
 */
public void
setActualParamList( HirList pActualParamList );

} // AsmStmt interface

//##70 END
