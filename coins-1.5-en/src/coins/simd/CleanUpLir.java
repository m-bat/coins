/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Op;
import coins.backend.lir.LirNode;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Delete useless load/store in LIR transformed to SIMD form
 * if it is an assignment expression with register (REG or SUBREG)
 * operands on both sides. If the destination (left hand side) register is not
 * used until it is assigned to the source (right hand side) operand, then
 * the assignment expressions are deleted.
 * Ex.)
 *   x = y;
 *   ....  (no use of x)
 *   y = x;
 **/
class CleanUpLir{
/**
 * Pair of destination (variable to which value is assigned) and
 * source (value to be assigned).
**/
private Hashtable srcAndDst;
/**
 * List of destination variables of assignment expressions.
**/
private List keyList;
/**
 * List of LIR to be processed.
**/
private List lirList;
// Begin(2004.11.16)
private Vector lirPos;
// End(2004.11.16)

CleanUpLir(List lir) {
  lirList=lir;
// Begin(2004.11.16)
  lirPos=new Vector();
// End(2004.11.16)
}

/**
 * Delete useless assignment expressions
 * receiving list of LIR to be deleted.
**/
public void invoke(){
  analyze();
  clean();
}

/**
 * Find assignment expressions to be deleted.
**/
private void analyze(){
  srcAndDst=new Hashtable();
  keyList=new LinkedList();

  for(int i=0;i<lirList.size();i++){
    LirNode inst=(LirNode)lirList.get(i);
    // Ignore expressions other than SET.
    if(inst.opCode==Op.SET) {
      LirNode dst=(LirNode)inst.kid(0);
      LirNode src=(LirNode)inst.kid(1);

      // If both operands of the SET are REG or SUBREG,
      // call findRedef giving the SET as the start point of search.
         if((dst.opCode==Op.REG || dst.opCode==Op.SUBREG) &&
            (src.opCode==Op.REG || src.opCode==Op.SUBREG)) {

        findRedef(inst,dst,src,i);

      }
    }
  }
}

/**
 * For assignment expression of the form dst = src,
 * search for the assignment expression of the form src = dst.
 * If found, register the pair of assignment expressions to
 * the hash table treating dst = src as key.
 * If dst of dst = src is used before src = dst appeares,
 * do not delete the pair bacause the assignment expression is necessary.
   @param def is LIR to be analyzed.
   @param dst is the destination of the assignment.
   @param src is the source of assignment.
   @param place represents the offset of the assignment relative to
      the given LIR.
**/
private void findRedef(LirNode def,LirNode dst,LirNode src,int place){
  for(int i=place+1;i<lirList.size();i++){
    LirNode cons=(LirNode)lirList.get(i);
    if(cons.opCode==Op.SET){
      // Examine src = dst for dst = src and if found, record the
      // expression and def to the table.

      //System.out.println(cons.cdr().cdr().car()+" --> "+dst);
      //System.out.println(cons.cdr().car()+" ==> "+src+"\n");
      if(src.equals(cons.kid(0)) && dst.equals(cons.kid(1))) {
        //System.out.println(def);
        //System.out.println("\t"+cons);
// Begin(2004.11.16)
//        if(!keyList.contains(def)){
//          keyList.add(def);
//          srcAndDst.put(def,cons);
//        }
        lirPos.addElement(new Integer(place));
        lirPos.addElement(new Integer(i));
// End(2004.11.16)
        return;
      }
    }
    // Examine if dst is used before the corresponding assignment
    // appeares. If it is used, then do not delete the expression.
//    Stack stack=new Stack();
//    stack.push(cons);
//    while(!stack.empty()){
//      LirNode c=(LirNode)stack.pop();
//      if(c.cdr()!=SirList.nil)
//        stack.push((Cons)c.cdr());
//      if(c.car()==regCell || c.car()==subregCell){
//        if(SirList.equal(dst,c)){
//          //System.out.println("find : "+def);
//          return;
//        }
//      }
//      else if(!c.car().isAtom())
//        stack.push((Cons)c.car());
//    }
    if(LirUtil.isUsed(dst, cons)) return;
  }
}

/**
 * Delete the pairs of assignments registered in the hash table.
**/
private void clean(){
// Begin(2004.11.16)
//  for(int i=0;i<keyList.size();i++){
//    LirNode rm1=(LirNode)keyList.get(i);
//    LirNode rm2=(LirNode)srcAndDst.get(rm1);
//
//    //System.out.println(rm1);
//    //System.out.println(rm2);
//
//    lirList.remove(rm1);
//    lirList.remove(rm2);
//  }
  Object[] lirPosArray=lirPos.toArray();
  Arrays.sort(lirPosArray);
// for test
//System.out.println("Removed lir place, length="+lirPosArray.length);
//for(int i=0; i<lirPosArray.length; i++) System.out.println((Integer)lirPosArray[i]+", ");
//
  for(int i=lirPosArray.length-1; i>=0; i--) {
    lirList.remove(((Integer)lirPosArray[i]).intValue());
  }
// End(2004.11.16)
}
}
