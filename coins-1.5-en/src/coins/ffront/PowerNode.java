/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.sym.Type;

/** Powered expression

  left ** right
  
 */
public class PowerNode extends BinaryNode{

  HirUtility fHirUtil;
  public PowerNode(Node left, Node right, FirToHir pfHir) {
    super(0, left, right, pfHir);
    fHirUtil = fHir.getHirUtility();
  }
  public void print(int level, String spaces){
    fHir.debugPrint(level, spaces+"binary: **\n");
    super.print(level, spaces);
  }
  public String toString(){
     return "PowerExp **";
  }

  /** Return Exp of "left ** right".
   * If
   *   right == 0 return 1
   *   right == 1 return left
   *   right == 2 return left * left
   *   right == 3 return left * left * left
   *   right == 4 return (left * left) * (left * left)
   * else return SubscrOrFunCall("_power", (left, right))
   * @return  power expression.
   */
  public Exp makeExp(){
    HIR hir = fHir.getHir();
    if (right instanceof Token){
      Token rightToken = (Token)right;
      if (rightToken.getKind() == Parser.INT_CONST){
        String rightStringValue = rightToken.getLexem();
        int rightValue = Integer.parseInt(rightStringValue);
        switch(rightValue){
          case 0:
            Exp lExp1 = left.makeExp();
            Type exp1Type = lExp1.getType();
            if (exp1Type.isInteger())
              return fHirUtil.makeConstInt1Node();
            else
              return fHirUtil.makeConstReal1Node();
          case 1:
            return left.makeExp();
          case 2:
            return (new BinaryNode(HIR.OP_MULT, left, left, fHir)).makeExp();
          case 3:
            BinaryNode bExp = new BinaryNode(HIR.OP_MULT, left, left, fHir);
            return (new BinaryNode(HIR.OP_MULT, bExp, left, fHir)).makeExp();
          case 4:
            bExp = new BinaryNode(HIR.OP_MULT, left, left, fHir);
            return (new BinaryNode(HIR.OP_MULT, bExp, bExp, fHir)).makeExp();
          default: break;
        }
      }
    }
    FirList paramList = new FirList(fHir);
    paramList.addedLast(left).addedLast(right);
    Token powerToken = new Token(0, "_power".intern(), Parser.IDENT);
    return (new SubscrOrFunCallNode(powerToken, paramList, fHir)).makeExp();
  }

  public FNumber getConstValue(){
    FNumber ln = null,rn = null;
    //fHir.p(left.toString());fHir.p(right.toString());
    
    if(left instanceof HasConstValue){
      ln = ((HasConstValue)left).getConstValue();
    }
    if(right instanceof HasConstValue){
      rn = ((HasConstValue)right).getConstValue();
    }
    if(ln != null && rn != null){
      int i;
      FNumber v = ln, r = FNumber.make(1);
      int     n = rn.intValue();
      for(i=0;i<n;i++){
        r = r.mul(v);
        //fHir.p(r.toString());
      }
      return r;
    }
    return null;
  }
}

