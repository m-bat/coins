/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lir2c;

import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirFconst;
import coins.backend.sym.Symbol;
import coins.backend.Op;
import coins.backend.Type;

/**
 * Convert: converts LirNode to C code.
 * a node of LirNode is converted to a line of C code
 * (excepts "switch" sentence).
 **/
public class Convert{
  /**
   * Constructor(with no arg)
   **/
  public Convert(){}

  /**
   * invoke: the method that you should call it first.
   * @param node the root node that will convert to C code.
   * @return the C string of lir nodes.
   **/
  public String invoke(LirNode node) {
    String results;

    results = dump(node,false,node);

    return results;
  }

  /**
   * dump: main method of the convertion.
   * it called by invoke method and dump method(dump method calls dump
   * method recursively).
   *
   * @param node the node that will convert to a part of C code.
   * @param inMEM the boolean value which node are in MEM expression.
   * @param parent the node that is a parent of the node.
   * @return the string that is a part of C code.
   **/
  String dump(LirNode node, boolean inMEM,LirNode parent) {
    String results = "";
    int num_src, num_src2;
    int opcode;
    int i;
    Integer int_value;
    Double float_value;
    Symbol  sym;
    LirNode child1, child2, child3, child4;
    ConvValue cv = new ConvValue();

    opcode = node.opCode;
    num_src = node.nKids();
    
    if (opcode == Op.CALL) {

        child1 = node.kid(0);
        if (child1.opCode != Op.MEM) {
          sym = ((LirSymRef)child1).symbol;
          results = cv.replace(sym.name) +"(";
        } else {
          String moe = dump(child1.kid(0),inMEM,node);

          if (child1.kid(0) instanceof LirSymRef) {
            sym = ((LirSymRef)(child1.kid(0))).symbol;
            // results = /* "*" */ "*" + /* cv.replace(sym.name) */ moe  +"(";
            results = cv.replace(sym.name) +"(";
          } else if (child1.kid(0).opCode ==  Op.ADD){
            Symbol  moe_sym = ((LirSymRef)(child1.kid(0).kid(0))).symbol;
            LirNode moe_1 = child1.kid(0).kid(1).kid(0);

            results = cv.replace(moe_sym.name)
              + "[" + dump(moe_1,inMEM,node) + "]" + "(";
          }
        }
//          // results = sym.name +"(";
//          results = cv.replace(sym.name) +"(";

        if (num_src == 1) {
          return results+")";
        }else{
          child2 = node.kid(1);
          for (i = 0; i < child2.nKids(); i++) {
            if (i == 0)
              results += dump(child2.kid(i),inMEM,node);
            else
              results += ", "+dump(child2.kid(i),inMEM,node);
          }

          child3 = node.kid(2);
          num_src2 = child3.nKids();
          if (num_src2 > 0) {
            results = dump(child3.kid(0),inMEM,node) + " = " + results;
          }

          return results + ")";
        }

    } else if (opcode == Op.JUMPN) {
      results = "switch(" + dump(node.kid(0),inMEM,node) + ") {\n";
      if (num_src == 2) {
        results += "default: goto " + dump(node.kid(1),inMEM,node) + ";}";
      } else {      // if (num_src == 3) {
        child1 = node.kid(1);
        num_src2 = child1.nKids();
        for (i = 0; i < num_src2 ; i++) {
          child2 = (child1.kid(i)).kid(0);
          child3 = (child1.kid(i)).kid(1);

          results += "case "+dump(child2,inMEM,node)+": goto "+ dump(child3,inMEM,node)+ ";break;\n";
        }
        results += "default: goto " + dump(node.kid(2),inMEM,node) + ";}";
      }

      return results;
    } else if (opcode == Op.PROLOGUE) {
      return "";
    } else if (opcode == Op.EPILOGUE){
      if (num_src == 1) {
        return "return";
      } else {
        return "return " + dump(node.kid(1),inMEM,node);
      }
    } else if (opcode == Op.LIST){
      for (i = 0; i < num_src - 1; i++) {
        results += dump(node.kid(i),inMEM,node) + ",";
      }
      results += dump(node.kid(i),inMEM,node);

      // now on debugging... 

      if (num_src == 1) {
        // return " list " + ""+ results +"";
        // return "" + ""+ results +"";
        return "{" + ""+ results +"}";
        // return "list1 " + ""+ results +"";
      } else {
        // return " list " + "{"+ results +"}";
        return "" + "{"+ results +"}";
        // return "listn " + "{"+ results +"}";
      }
    } else if (opcode == Op.ZEROS){
      for (i = 0; i < num_src - 1; i++) {
        results += dump(node.kid(i),inMEM,node) + ",";
      }
      results += dump(node.kid(i),inMEM,node);

      // return " zeros " + ""+ results +"";
      long num_zeros = Long.parseLong(results.trim());
      for (results = "", i = 0; i < num_zeros - 1 ; i++) {
        results += "0,";
      }

      results += "0";

      return results;

    } else if (opcode == Op.SPACE){
      for (i = 0; i < num_src - 1; i++) {
        results += dump(node.kid(i),inMEM,node) + ",";
      }
      results += dump(node.kid(i),inMEM,node);

      return " space " + ""+ results +"";
    } else if (opcode == Op.PHI){
      // results += "/* " + dump(node.kid(0),inMEM,node) + " = phi(";
      results += "" + dump(node.kid(0),inMEM,node) + " = phi(";
      for (i = 1; i < num_src - 1; i++ ) {
        child1 = node.kid(i);
        // By FUKUOKA Takeaki
        //result += dump(child1.kid(0),inMEM,node) + ":" + dump(child1.kid(1),inMEM,node) + ",";
        results += dump(child1.kid(0),inMEM,node) + ":" + dump(child1.kid(1),inMEM,node) + ", ";

      }
      child1 = node.kid(i);
      results += dump(child1.kid(0),inMEM,node) + ":" + dump(child1.kid(1),inMEM,node);

      // results += "); */";
      results += ");";

      return results;
    } else {
      if (num_src == 0) {
        /* FLOATCONST,INTCONST,STATIC,FRAME,REG,LABEL,DEFLABEL */
        
        if (opcode == Op.INTCONST) {
          int_value = new Integer((int)((LirIconst)node).value);
          return " "+int_value.toString();
          
        } else if (opcode == Op.FLOATCONST) {
          float_value = new Double(((LirFconst)node).value);
          return " "+float_value.toString();
          
        } else if (opcode == Op.LABEL) {
          // System.out.println("LABEL:"+node.toString());
          // return ""+ (((LirLabelRef)node).label).name + "";
          // return "/* LABEL:"+node.toString()+"," + (((LirLabelRef)node).label).toString()+ " */"+ (((LirLabelRef)node).label).name + "";
          // return "" + (((LirLabelRef)node).label).toString() + "";

          return "" + (((LirLabelRef)node).label).toString().replace('.','_') + "";

        } else if (opcode == Op.REG) {
          sym = ((LirSymRef)node).symbol;
          // return "REG(" + sym.name +")";
          // return "" + cv.replace(sym.name) +"";

          if (inMEM) {
            // return "(" +lirToTypeStr2(node) + " *)" + cv.replace(sym.name) +"";
            if (lirToTypeStr2(parent).equals("")) {
              return "(" +lirToTypeStr2(node) + " *)" + cv.replace(sym.name) +"";
            } else {
              return "(" +lirToTypeStr2(parent) + " *)" + cv.replace(sym.name) +"";
            }
          } else {
            return "" + cv.replace(sym.name) +"";
          } 
          
        } else if (opcode == Op.FRAME) {
          sym = ((LirSymRef)node).symbol;
          // return "FRAME(" + sym.name +")";
          // return "(unsigned char *)&(" + cv.replace(sym.name) +")";

          if (inMEM) {
            // return "((" +lirToTypeStr2(node) + " *)" + "(unsigned char *)&(" + cv.replace(sym.name) +"))";

            if (lirToTypeStr2(parent).equals("")) {
              return "((" +lirToTypeStr2(node) + " *)" + "(unsigned char *)&(" + cv.replace(sym.name) +"))";
            } else {
              return "((" +lirToTypeStr2(parent) + " *)" + "(unsigned char *)&(" + cv.replace(sym.name) +"))";
            }
          } else {
            return "(unsigned char *)&(" + cv.replace(sym.name) +")";
          } 
          
        } else if (opcode == Op.STATIC) {
          sym = ((LirSymRef)node).symbol;
          // return "STATIC(" + sym.name +")";
          // return "(" + cv.replace(sym.name) +")";

          // return "&(" + cv.replace(sym.name) +")";

          if (inMEM) {
            // return "((" +lirToTypeStr2(node) + " *)" + "(unsigned char *)&(" + cv.replace(sym.name) +"))";

            if (lirToTypeStr2(parent).equals("")) {
              return "((" +lirToTypeStr2(node) + " *)" + "(unsigned char *)&(" + cv.replace(sym.name) +"))";
            } else {
              return "((" +lirToTypeStr2(parent) + " *)" + "(unsigned char *)&(" + cv.replace(sym.name) +"))";
            }
          } else {
            return "(unsigned char *)&(" + cv.replace(sym.name) +")";
          } 

          
        } else if (opcode == Op.DEFLABEL) {
          /* dummy */
          // return ""+ (((LirLabelRef)node).label).name + ":";
          return ""+ (((LirLabelRef)node).label).name() + ":";
          
        } else {
          return " "+"|" + opcode + "|";
        }
      } else if (num_src == 1) { /* JUMP,NEG,BNOT etc. */
        child1 = node.kid(0);
        if (opcode == Op.JUMP) {
          child1 = node.kid(0);
          // return "goto "+ (((LirLabelRef)child1).label).name;
          return "goto "+ dump(child1,inMEM,node);
        } else if (opcode == Op.NEG) {
          return "(-("+ dump(child1,inMEM,node) + "))";
        } else if (opcode == Op.BNOT) {
          return "(~("+ dump(child1,inMEM,node) + "))";
        } else if (opcode == Op.MEM) {
          return "(" +lirToTypeStr3(node) + "(*("+ dump(child1,true,parent) + ")))";
        } else if (opcode == Op.CONVSX ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVZX ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVIT ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVFX ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVFT ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVFI ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVFS ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVFU ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVSF ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.CONVUF ) {
          return "(" + lirToTypeStr(node) + ")(" + dump(child1,inMEM,node) + ")";
        } else if (opcode == Op.LINE ) {
          return "/* line "+dump(child1,inMEM,node)+"*/\n";
        } else {
          results += dump(child1,inMEM,node);
          return "(" + opcode + ")" + results;
        }
      } else if (num_src == 2) {
        child1 = node.kid(0);
        child2 = node.kid(1);
        if (opcode == Op.SET) {
          return dump(child1,inMEM,node) + " = " + "(" + lirToTypeStr3(node) + "("
            + dump(child2,inMEM,node) + "))";
          
        } else if (opcode == Op.ADD) {
          return biOpToStr(node,child1,child2," + ",inMEM,parent);
        } else if (opcode == Op.SUB) {
          return biOpToStr(node,child1,child2," - ",inMEM,parent);
        } else if (opcode == Op.MUL) {
          return biOpToStr(node,child1,child2," * ",inMEM,parent);
        } else if (opcode == Op.DIVS) {
          return biOpToStr(node,child1,child2," / ",inMEM,parent);
        } else if (opcode == Op.DIVU) {
          return biOpToStr(node,child1,child2," / ",inMEM,parent);
        } else if (opcode == Op.MODS) {
          return biOpToStr(node,child1,child2," % ",inMEM,parent);
        } else if (opcode == Op.MODU) {
          return biOpToStr(node,child1,child2," % ",inMEM,parent);
          
        } else if (opcode == Op.BAND) {
          return biOpToStr(node,child1,child2," & ",inMEM,parent);
        } else if (opcode == Op.BOR) {
          return biOpToStr(node,child1,child2," | ",inMEM,parent);
        } else if (opcode == Op.BXOR) {
          return biOpToStr(node,child1,child2," ^ ",inMEM,parent);
          
        } else if (opcode == Op.LSHS) {
          return biOpToStr(node,child1,child2," << ",inMEM,parent);
        } else if (opcode == Op.LSHU) {
          return biOpToStr(node,child1,child2," << ",inMEM,parent);
        } else if (opcode == Op.RSHS) {
          return biOpToStr(node,child1,child2," >> ",inMEM,parent);
        } else if (opcode == Op.RSHU) {
          return biOpToStr(node,child1,child2," >> ",inMEM,parent);
          
        } else if (opcode == Op.TSTEQ ) {
          return "("+ dump(child1,inMEM,node) + " == "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTNE ) {
          return "("+ dump(child1,inMEM,node) + " != "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTLTS ) {
          return "("+ dump(child1,inMEM,node) + " < "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTLES ) {
          return "("+ dump(child1,inMEM,node) + " <= "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTGTS ) {
          return "("+ dump(child1,inMEM,node) + " > "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTGES ) {
          return "("+ dump(child1,inMEM,node) + " >= "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTLTU ) {
          return "("+ dump(child1,inMEM,node) + " < "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTLEU ) {
          return "("+ dump(child1,inMEM,node) + " <= "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTGTU ) {
          return "("+ dump(child1,inMEM,node) + " > "
            + dump(child2,inMEM,node)+")";
        } else if (opcode == Op.TSTGEU ) {
          return "("+ dump(child1,inMEM,node) + " >= "
            + dump(child2,inMEM,node)+")";
        } else {
          for (i = 0; i < num_src; i++) {
            results += dump(node.kid(i),inMEM,node);
          }
          return "<" + opcode + ">" + results;
        }
      } else if (num_src == 3) {
        child1 = node.kid(0);
        child2 = node.kid(1);
        child3 = node.kid(2);
        if (opcode == Op.JUMPC ) {
          return "if ("+ dump(child1,inMEM,node) 
            + ") { goto " + dump(child2,inMEM,node)
            + ";} else { goto " + dump(child3,inMEM,node) + ";}";
        } else if (opcode == Op.IF ) {
          return "("+ dump(child1,inMEM,node) 
            + ") ? (" + dump(child2,inMEM,node)
            + ") : ( " + dump(child3,inMEM,node) + ")";
        } else {
          for (i = 0; i < num_src; i++) {
            results += dump(node.kid(i),inMEM,node);
          }
          return "3[" + opcode + "]3" + results;
        }
      } else {
        for (i = 0; i < num_src; i++) {
          results += dump(node.kid(i),inMEM,node);
        }
        return "[" + Op.toName(opcode) + "]" + results;
      }
    }
    // not reached
    // return node.toString();
  }

  /**
   * lirToTypeStr: get type string of the variable, expression, etc by
   * calling coins.backend.Type@toString() method.
   * note: aggregate objects do not support.
   *
   * @param node the node to get the type string.
   * @return the type string of LirNode.
   */
  String lirToTypeStr(LirNode node) {
    String typestr, results;

    results = "";
    typestr = Type.toString(node.type);

    if (typestr.equals("I8")) {
      results = "char";
    } else if (typestr.equals("I16")) {
      // results = "char";
      results = "short";
    } else if (typestr.equals("I32")) {
      results = "int";
    } else if (typestr.equals("I64")) {
      results = "long";
    } else if (typestr.equals("F32")) {
      results = "float";
    } else if (typestr.equals("F64")) {
      results = "double";
    } else if (typestr.equals("F128")) {
      results = "long double";
    } else {
      // not supports aggregate objects.
      results = "AGGREGATE_" + typestr;
    }

    return results;
  }


  /**
   * lirToTypeStr2: get type string of the variable, expression, etc by
   * calling coins.backend.Type@toString() method.
   * note: aggregate objects do not support(return "").
   *
   * @param node the node to get the type string.
   * @return the type string of LirNode.
   */
  String lirToTypeStr2(LirNode node) {
    String typestr, results;

    results = "";
    typestr = Type.toString(node.type);

    if (typestr.equals("I8")) {
      results = "char";
    } else if (typestr.equals("I16")) {
      // results = "char";
      results = "short";
    } else if (typestr.equals("I32")) {
      results = "int";
    } else if (typestr.equals("I64")) {
      results = "long";
    } else if (typestr.equals("F32")) {
      results = "float";
    } else if (typestr.equals("F64")) {
      results = "double";
    } else if (typestr.equals("F128")) {
      results = "long double";
    } else {
      // not supports aggregate objects.
      results = "";
    }

    return results;
  }

  /**
   * lirToTypeStr3: get type string and parenthes of the variable, expression, etc by
   * calling coins.backend.Type@toString() method.
   * note: if the node is aggregate object, return "".
   *
   * @param node the node to get the type string.
   * @return the type string and parenthes of LirNode.
   */
  String lirToTypeStr3(LirNode node) {
    String typestr, results;

    results = "";
    typestr = Type.toString(node.type);

    if (typestr.equals("I8")) {
      results = "char";
    } else if (typestr.equals("I16")) {
      // results = "char";
      results = "short";
    } else if (typestr.equals("I32")) {
      results = "int";
    } else if (typestr.equals("I64")) {
      results = "long";
    } else if (typestr.equals("F32")) {
      results = "float";
    } else if (typestr.equals("F64")) {
      results = "double";
    } else if (typestr.equals("F128")) {
      results = "long double";
    } else {
      // not supports aggregate objects.
      results = "";
    }

    if (results.equals("")) {
      return "";
    } else {
      return "(" + results +")";
    }
  }

  /**
   * biOpToStr: get the expression of C code for bi Operator node(ADD,etc).
   *
   * @param node the node to convert.
   * @param child1 the node of first child node.
   * @param child2 the node of second child node.
   * @param ope bi operator
   * @param inMEM the node that is in the MEM expression is true, or false.
   * @param parent the node that is a parent of the node.
   * @return the C code for the node.
   */
  String biOpToStr(LirNode node, LirNode child1, LirNode child2,
                   String Ope, boolean inMEM,LirNode parent) {
    String results = "";

    if (inMEM) {
      inMEM = false;

      if (lirToTypeStr2(parent).equals("")) {
        results = "((" +lirToTypeStr2(node) + " *)("+ dump(child1,inMEM,node) + Ope
          + dump(child2,inMEM,node)+"))";
      } else {
        results = "((" +lirToTypeStr2(parent) + " *)("+ dump(child1,inMEM,node) + Ope
          + dump(child2,inMEM,node)+"))";
      }
    } else {
      results = "((" +lirToTypeStr2(node) + ")("+ dump(child1,inMEM,node) + Ope
            + dump(child2,inMEM,node)+"))";
    }

    return results;
  }
}
