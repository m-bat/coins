/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lir2c;

import coins.backend.Op;
import coins.backend.lir.LirNode;
import coins.backend.Data;

/**
 * AnaData: analyze Data object.
 * 
 **/
public class AnaData{
  public AnaData(){}
  public static String structStr(Data data) {
    String results = "";
    String tmpstr1, tmpstr2;
    int len = data.components.length;
    LirNode lnode, lnode2;

    if (len <= 1) {
      lnode = (LirNode)data.components[0];
      tmpstr1 = basicDataStr(lnode);
      if (tmpstr1.equals("Z")) {
        results = "Z";
      } else if (tmpstr1.equals("s")) {
        results = "S";
      } else if (tmpstr1.equals("L")) {
        if (lnode.nKids() <= 1) {
          lnode2 = lnode.kid(0);
          tmpstr2 = basicDataStr(lnode2);
          if (tmpstr2.equals("I") || tmpstr2.equals("F")) {
            results = "LN";
          } else if (tmpstr2.equals("S")) {
            results = "LS";
          }
        } else {
          lnode2 = lnode.kid(0);
          tmpstr2 = basicDataStr(lnode2);
          if (tmpstr2.equals("I") || tmpstr2.equals("F")) {
            results = "Ln";
          } else if (tmpstr2.equals("S")) {
            results = "LS";
          }
        }
      }
    } else {
      lnode = (LirNode)data.components[0];
      tmpstr1 = basicDataStr(lnode);
      if (tmpstr1.equals("Z")) {
        results = "ZZ";
      } else if (tmpstr1.equals("s")) {
        // not happen
        results = "SS";
      } else if (tmpstr1.equals("L")) {
        if (lnode.nKids() <= 1) {
          lnode2 = lnode.kid(0);
          tmpstr2 = basicDataStr(lnode2);
          if (tmpstr2.equals("I") || tmpstr2.equals("F")) {
            results = "LLN";
          } else if (tmpstr2.equals("S")) {
            // not happen
            results = "LLS";
          }
        } else {
          lnode2 = lnode.kid(0);
          tmpstr2 = basicDataStr(lnode2);
          if (tmpstr2.equals("I") || tmpstr2.equals("F")) {
            results = "LLn";
          } else if (tmpstr2.equals("S")) {
            // not happen
            results = "LSn";
          }
        }
      }
    }

    return results;
  }

  public static String basicDataStr(LirNode node) {
    String dataStr = "";
    int opcode, num_src;

    opcode = node.opCode;
    num_src = node.nKids();

    switch (opcode) {
      case Op.LIST:
        dataStr += "L"; break;
      case Op.ZEROS:
        dataStr += "Z"; break;
      case Op.SPACE:
        dataStr += "s"; break;
      case Op.INTCONST:
        dataStr += "I"; break;
      case Op.FLOATCONST:
        dataStr += "F"; break;
      case Op.STATIC:
        dataStr += "S"; break;
      default:
        dataStr += "*" + Op.toName(opcode);
    }

    return dataStr;
  }
}
