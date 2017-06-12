/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

//import java.lang.*;
//import java.io.*;
import java.util.List;
import java.util.LinkedList;
import coins.backend.Function;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;

/**
 * Decoder class for Lir.
 * This class copied and modified coins.backend.Function.decodeLir
 * for testing SIMD locally.
 */
public class LirDecoder {
  /**
   * LirFactory
   */
  private LirFactory newLir;
  /**
   * Function
   */
  private Function func;
  /**
   * Constructs LirDecoder.
   * @param  f  Function
   */
  public LirDecoder(Function f) {
    newLir=f.newLir;
    func=f;
  }
  /**
   * Decode ImList which represents LirNode.
   * @param stmt ImList, which represents a LirNode.
   * @return LirNode
   */
  public LirNode decodeLir(ImList stmt) throws SyntaxError {
    int code = Op.toCode((String)stmt.elem());
    switch (code) {
        // leaf nodes
    case Op.INTCONST:
      return newLir.iconst(Type.decode((String)stmt.elem2nd()),
                           Integer.parseInt((String)stmt.elem3rd()),
                           ImList.Empty);
    case Op.FLOATCONST:
      return newLir.fconst(Type.decode((String)stmt.elem2nd()),
                           Double.parseDouble((String)stmt.elem3rd()),
                           ImList.Empty);
//    case Op.STATIC:
//    case Op.FRAME:
    case Op.REG:
//    case Op.SUBREG:
      {
        int type = Type.decode((String)stmt.elem2nd());
        String name = (String)stmt.elem3rd();
        Symbol sym = func.localSymtab.get(name);
        if (sym == null) {
            sym = func.module.globalSymtab.get(name);
            if (sym == null)
		sym=registerRegname(func,stmt);
        }
        return newLir.symRef(code, type, sym, ImList.Empty);
      }

    case Op.SUBREG:
      {
        LirNode inum=newLir.iconst(Type.decode("I32"),
                                   Integer.parseInt((String)stmt.elem4th()),
                                   ImList.Empty);
        return newLir.operator(code, Type.decode((String)stmt.elem2nd()),
                             decodeLir((ImList)stmt.elem3rd()),
                             inum,ImList.Empty);
      }

      // Label reference/definition
//    case Op.LABEL:
//      {
//        String name = (String)stmt.elem3rd();
//        Label label = (Label)labelTable.get(name);
//        if (label == null) {
//          label = new Label(name);
//          labelTable.put(name, label);
//        }
//        return newLir.labelRef(code, Type.ADDRESS, label);
//      }
//
//    case Op.DEFLABEL:
//      {
//        String name = (String)stmt.elem2nd();
//        Label label = (Label)labelTable.get(name);
//        if (label == null) {
//          label = new Label(name);
//          labelTable.put(name, label);
//        }
//        return newLir.labelRef(code, Type.UNKNOWN, label);
//      }

    // unary operators with type
    case Op.BNOT:
    case Op.NEG:
    case Op.CONVSX:
    case Op.CONVZX:
    case Op.CONVIT:
    case Op.CONVFX:
    case Op.CONVFT:
    case Op.CONVFI:
    case Op.CONVSF:
    case Op.CONVUF:
    case Op.MEM:
      return newLir.operator(code, Type.decode((String)stmt.elem2nd()),
                             decodeLir((ImList)stmt.elem3rd()), ImList.Empty);

    case Op.HOLE:
      {
        // Hole number is implemented as iconst,temporally.
        LirNode hnum=newLir.iconst(Type.decode("I32"),
                                   Integer.parseInt((String)stmt.elem2nd()),
                                   ImList.Empty);
        return newLir.operator(code, Type.decode((String)stmt.elem3rd()),
                               hnum, ImList.Empty);
      }

    // unary operators w/o type
    case Op.JUMP:
    case Op.USE:
    case Op.CLOBBER:
      return newLir.operator(code, Type.UNKNOWN,
                             decodeLir((ImList)stmt.elem2nd()), ImList.Empty);

    // binary operators
    case Op.ADD:
    case Op.SUB:
    case Op.MUL:
    case Op.DIVS:
    case Op.DIVU:
    case Op.MODS:
    case Op.MODU:
    case Op.BAND:
    case Op.BOR:
    case Op.BXOR:
    case Op.LSHS:
    case Op.LSHU:
    case Op.RSHS:
    case Op.RSHU:
    case Op.TSTEQ:
    case Op.TSTNE:
    case Op.TSTLTS:
    case Op.TSTLES:
    case Op.TSTGTS:
    case Op.TSTGES:
    case Op.TSTLTU:
    case Op.TSTLEU:
    case Op.TSTGTU:
    case Op.TSTGEU:
    case Op.SET:
      return newLir.operator(code, Type.decode((String)stmt.elem2nd()),
                             decodeLir((ImList)stmt.elem3rd()),
                             decodeLir((ImList)stmt.elem4th()),
                             ImList.Empty);

//    case Op.JUMPC: // ternary operator
//      {
//        LirNode[] src = new LirNode[3];
//        src[0] = decodeLir((ImList)stmt.elem2nd());
//        src[1] = decodeLir((ImList)stmt.elem3rd());
//        src[2] = decodeLir((ImList)stmt.elem4th());
//        return newLir.operator(code, Type.UNKNOWN, src);
//      }
        
//    case Op.CALL: // currently with type
//      {
//        int type = Type.decode((String)stmt.elem2nd());
//        LirNode callee = decodeLir((ImList)stmt.elem3rd());
//        int n = ((ImList)stmt.elem4th()).length(); // parameter list length
//        LirNode[] param = new LirNode[n];
//        int i = 0;
//        for (ImList p = (ImList)stmt.elem4th(); !p.atEnd(); p = p.next())
//          param[i++] = decodeLir((ImList)p.elem());
//        return newLir.operator(code, type, callee,
//                               newLir.operator(Op.LIST, Type.UNKNOWN, param));
//      }

//    case Op.JUMPN:
//      {
//        LirNode value = decodeLir((ImList)stmt.elem2nd());
//        ImList cases = (ImList)stmt.elem3rd();
//        int n = cases.length();
//        LirNode[] labels = new LirNode[n];
//        int i = 0;
//        for (ImList p = cases; !p.atEnd(); p = p.next()) {
//          ImList c = (ImList)p.elem();
//          labels[i++] = newLir.operator(Op.LIST, Type.UNKNOWN,
//                                        decodeLir((ImList)c.elem()),
//                                        decodeLir((ImList)c.elem2nd()));
//        }
//        return newLir.operator(code, Type.UNKNOWN, value,
//                               newLir.operator(Op.LIST, Type.UNKNOWN, labels),
//                               decodeLir((ImList)stmt.elem4th()));
//      }
        
//    case Op.PROLOGUE:
//    case Op.EPILOGUE:
//      {
//        int n = stmt.next().length();
//        LirNode[] opr = new LirNode[n];
//        ImList frame = (ImList)stmt.elem2nd();
//        opr[0] = newLir.operator(Op.LIST, Type.UNKNOWN,
//                                 newLir.iconst(Type.ADDRESS,
//                                               Integer.parseInt((String)frame.elem())),
//                                 newLir.iconst(Type.ADDRESS,
//                                               Integer.parseInt((String)frame.elem2nd())));
//        int i = 1;
//        for (ImList p = stmt.next().next(); !p.atEnd(); p = p.next())
//          opr[i++] = decodeLir((ImList)p.elem());
//        return newLir.operator(code, Type.UNKNOWN, opr);
//      }

    case Op.PARALLEL:
      {
        int n = stmt.next().length();
        LirNode[] src = new LirNode[n];
        int i = 0;
        for (ImList p = stmt.next(); !p.atEnd(); p = p.next())
          src[i++] = decodeLir((ImList)p.elem());
        return newLir.operator(code, Type.UNKNOWN, src, ImList.Empty);
      }

    default:
      throw new Error("Unknown opCode");
    }

  }
  /**
   * Registers a register name into Function's localSymtab.
   * @param  f  Function
   * @param  stmt  ImList
   * @return Symbol, which is registered to the Function.
   */
  private Symbol registerRegname(Function f,ImList stmt) throws SyntaxError {
    return f.localSymtab.addSymbol((String)stmt.elem3rd(),
                              Storage.decode("REG"),
                              Type.decode((String)stmt.elem2nd()),
                              0,0,
                              ImList.Empty);
  }
}
