/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.CantHappenException;
import coins.backend.Function;
import coins.backend.Module;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.sym.Label;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;
import coins.backend.util.Misc;
import coins.backend.util.QuotedString;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


/**
 * Generate LIR nodes.
 */
public class LirFactory {
  /** Module **/
  private Module module;

  /** LirNode's id generator **/
  private int lirNodeIdCounter = 1;

  /** Memorize Constant nodes */
  private Map constNodeHash = new HashMap();

  /** Memorize LirSymRef/LirLabelRef nodes **/
  private Map refNodeHash = new HashMap();

  private static final boolean NewInfoFlag = false;

  public LirFactory(Module module) {
    this.module = module;
  }

  /** Return upper bound of LirNode id numbers. */
  public int idBound() { return lirNodeIdCounter; }


  /** LIR Node LABEL variant serial number generator */
  private int labelVariantCounter = 1;

  /** Return new LABEL variant number. **/
  public int getLabelVariant() { return labelVariantCounter++; }


  private LirNode internNode(Map tbl, LirNode obj) {
    LirNode old = (LirNode)tbl.get(obj);
    if (old != null)
      return old;
    tbl.put(obj, obj);
    lirNodeIdCounter++;
    return obj;
  }


  /** Create STRING node **/
  public LirNode stringconst(String string) {
    return internNode(constNodeHash, new LirString(lirNodeIdCounter, string));
  }

  /** Create FLOATCONST node */
  public LirNode fconst(int type, double value, ImList opt) {
    return internNode(constNodeHash,
                      new LirFconst(lirNodeIdCounter, type, value, opt));
  }

  /** Create FLOATCONST node (short form). */
  public LirNode fconst(int type, double value) {
    return fconst(type, value, null);
  }

  /** Create INTCONST node */
  public LirNode iconst(int type, long value, ImList opt) {
    return internNode(constNodeHash,
                      new LirIconst(lirNodeIdCounter, type, value, opt));
  }

  /** Create INTCONST node (short form) */
  public LirNode iconst(int type, long value) {
    return iconst(type, value, null);
  }

  /** Create hidden INTCONST node */
  public LirNode untaggedIconst(int type, long value) {
    return iconst(type, value, optUntagged);
  }

  /** Create STATIC/FRAME/REG node */
  public LirNode symRef(int opCode, int type, Symbol symbol, ImList opt) {
    if (opt == null)
      opt = ImList.Empty;
    return internNode(refNodeHash,
                      new LirSymRef(lirNodeIdCounter, opCode, type, symbol, opt));
  }

  /** Create STATIC/FRAME/REG node, short form */
  public LirNode symRef(Symbol symbol) {
    int op;
    int type = module.targetMachine.typeAddress;
    switch (symbol.storage) {
    case Storage.REG: op = Op.REG; type = symbol.type; break;
    case Storage.FRAME: op = Op.FRAME; break;
    case Storage.STATIC: op = Op.STATIC; break;
    default: throw new CantHappenException();
    }
    return symRef(op, type, symbol, ImList.Empty);
  }

  // /** Create REG with variant no. */
  // public LirNode symRef(int opCode, int type, Symbol symbol, int variant, ImList opt)
  // {
  //   if (opt == null)
  //     opt = ImList.Empty;
  //   return internNode(refNodeHash,
  //                     new LirSymRef(lirNodeIdCounter, opCode, type, symbol, variant, opt));
  // }

  /** Create LABEL node */
  public LirNode labelRef(int opCode, int type, Label label, ImList opt) {
    if (opt == null)
      opt = ImList.Empty;
    return internNode(refNodeHash,
                      new LirLabelRef(lirNodeIdCounter, opCode, type, label, opt));
  }

  /** Create LABEL node with variant */
  public LirNode labelRef(int opCode, int type, Label label, int variant, ImList opt) {
    if (opt == null)
      opt = ImList.Empty;
    return internNode(refNodeHash,
                      new LirLabelRef(lirNodeIdCounter, opCode, type, label, variant, opt));
  }

  /** Create LABEL node, short form */
  public LirNode labelRef(Label label) {
    return labelRef(Op.LABEL, module.targetMachine.typeAddress, label, ImList.Empty);
  }


  /** Create LABEL node with variant, short form */
  public LirNode labelRefVariant(Label label) {
    return labelRef(Op.LABEL, module.targetMachine.typeAddress, label, getLabelVariant(),
                    ImList.Empty);
  }


  /** Create unary operator node */
  public LirNode operator(int opCode, int type, LirNode operand, ImList opt) {
    return new LirUnaOp(lirNodeIdCounter++, opCode, type, operand, opt);
  }


  /** Create unary operator node (short form)
   ** @deprecated  use node() instead. */
  public LirNode operator0(int opCode, int type, LirNode operand) {
    return operator(opCode, type, operand, null);
  }


  /** Create binary operator node */
  public LirNode operator(int opCode, int type, LirNode operand0, LirNode operand1, ImList opt) {
    return new LirBinOp(lirNodeIdCounter++, opCode, type, operand0, operand1, opt);
  }

  /** Create binary operator node (short form)
   ** @deprecated  use node() instead. */
  public LirNode operator0(int opCode, int type, LirNode operand0, LirNode operand1) {
    return operator(opCode, type, operand0, operand1, null);
  }

  /** Create ternary operator node */
  public LirNode operator(int opCode, int type, LirNode operand0,
                          LirNode operand1, LirNode operand2, ImList opt) {
    LirNode [] src = new LirNode[3];
    src[0] = operand0;
    src[1] = operand1;
    src[2] = operand2;
    return new LirNaryOp(lirNodeIdCounter++, opCode, type, src, opt);
  }

  /** Create ternary operator node (short form)
   ** @deprecated  use node() instead. */
  public LirNode operator0(int opCode, int type, LirNode operand0,
                          LirNode operand1, LirNode operand2) {
    return operator(opCode, type, operand0, operand1, operand2, null);
  }

  /** Create n-ary operator node */
  public LirNode operator(int opCode, int type, LirNode operands[], ImList opt) {
    return new LirNaryOp(lirNodeIdCounter++, opCode, type, operands, opt);
  }

  /** Create n-ary operator node (short form)
   ** @deprecated  use node() instead. */
  public LirNode operator0(int opCode, int type, LirNode operands[]) {
    return operator(opCode, type, operands, null);
  }


  /** Create floating point constant LirNode (generic name) **/
  public LirNode node(int opCode, int type, double value) {
    if (opCode != Op.FLOATCONST)
      throw new CantHappenException();
    return fconst(type, value);
  }

  /** Create integer constant LirNode (generic name) **/
  public LirNode node(int opCode, int type, long value) {
    if (opCode != Op.INTCONST)
      throw new CantHappenException();
    return iconst(type, value);
  }


  /** Create symbol reference LirNode (generic name) **/
  public LirNode node(int opCode, int type, Symbol symbol) {
    return symRef(opCode, type, symbol, null);
  }

  /** Create label reference LirNode (generic name) **/
  public LirNode node(int opCode, int type, Label label) {
    return labelRef(opCode, type, label, null);
  }

  /** Create empty operator LirNode (generic name) **/
  public LirNode node(int opCode, int type) {
    return operator(opCode, type, new LirNode[0], null);
  }

  /** Create unary operator LirNode (generic name) **/
  public LirNode node(int opCode, int type, LirNode operand) {
    return operator(opCode, type, operand, null);
  }

  /** Create binary operator LirNode (generic name) **/
  public LirNode node(int opCode, int type, LirNode operand0, LirNode operand1) {
    return operator(opCode, type, operand0, operand1, null);
  }

  /** Create ternary operator LirNode (generic name) **/
  public LirNode node(int opCode, int type, LirNode operand0,
                      LirNode operand1, LirNode operand2) {
    return operator(opCode, type, operand0, operand1, operand2, null);
  }

  /** Create n-ary operator LirNode (generic name) **/
  public LirNode node(int opCode, int type, LirNode operands[]) {
    return operator(opCode, type, operands, null);
  }



  /** Make a deep copy of node. All descendants are copied. */
  public LirNode makeCopy(LirNode inst) {
    return inst.makeCopy(this);
  }


  /** Make a shallow copy of node. Only top-level node is copyied. */
  public LirNode makeShallowCopy(LirNode inst) {
    return inst.makeShallowCopy(this);
  }


  /** Replace options of LIR node. **/
  public LirNode replaceOptions(LirNode inst, ImList newOpt) {
    return inst.replaceOptions(this, newOpt);
  }



  /** Remove quotation mark from object. **/
  private String unquote(Object obj) {
    if (obj instanceof QuotedString)
      return ((QuotedString)obj).body;
    else
      return (String)obj;
  }


  /** &untagged option **/
  public static final ImList optUntagged
    = new ImList("&untagged", ImList.Empty);


  /** Parse long integer with optional suffix (L,LL,U,UL,ULL) **/
  private long parseLong(String s) {
    int n = s.length();
    for (; n > 0; n--) {
      switch (s.charAt(n - 1)) {
      case 'l':
      case 'L':
      case 'u':
      case 'U':
        continue;
      }
      break;
    }
    return Long.parseLong(s.substring(0, n));
  }


  /** Convert LIR in ImList to LirNode. **/
  public LirNode decodeLir(Object node, Function func, Module mod)
    throws SyntaxError {
    if (node instanceof QuotedString)
      return stringconst(((QuotedString)node).body);
    if (node instanceof String)
      return stringconst((String)node);

    if (!(node instanceof ImList))
      throw new SyntaxError("QuotedString or ImList expected");

    ImList stmt = (ImList)node;
    int code = Op.toCode((String)stmt.elem());
    if (code < 0)
      throw new SyntaxError("Unknown opcode: " + (String)stmt.elem());
    ImList opt = stmt.scanOpt();

    switch (code) {
        // leaf nodes
    case Op.INTCONST:
      return iconst(Type.decode((String)stmt.elem2nd()),
                    parseLong((String)stmt.elem3rd()), opt);
    case Op.FLOATCONST:
      return fconst(Type.decode((String)stmt.elem2nd()),
                    Double.parseDouble((String)stmt.elem3rd()), opt);
    case Op.STATIC:
    case Op.FRAME:
    case Op.REG:
      {
        int type = Type.decode((String)stmt.elem2nd());
        String name = unquote(stmt.elem3rd());
        Symbol sym;
        if (func != null)
          sym = func.getSymbol(name);
        else if (mod != null)
          sym = mod.getSymbol(name);
        else
          throw new CantHappenException("Symbol reference outside function/module: " + name);
        if (sym == null)
          throw new CantHappenException("Undefined symbol: " + name);

        return symRef(code, type, sym, opt);
      }

    case Op.SUBREG:
      {
        int type = Type.decode((String)stmt.elem2nd());
        LirNode src = decodeLir((ImList)stmt.elem3rd(), func, mod);
        LirNode pos = decodeFixnum(stmt.elem4th());
        return operator(Op.SUBREG, type, src, pos, opt);
      }

      // Label reference/definition
    case Op.LABEL:
      {
        String name = unquote(stmt.elem3rd());
        if (func == null)
          throw new CantHappenException("LABEL reference outside function");

        Label label = func.internLabel(name);
        if (!stmt.next().next().next().atEnd()) {
          String v = (String)stmt.elem4th();
          if (!v.startsWith("&")) {
            int num = Integer.parseInt(v);
            func.reserveLabelVariantNo(num);
            return labelRef(Op.LABEL, module.targetMachine.typeAddress,
                            label, num, opt);
          }
        }
        return labelRef(Op.LABEL, module.targetMachine.typeAddress, label, opt);
      }

    case Op.DEFLABEL:
      {
        String name = unquote(stmt.elem2nd());
        Label label = func.internLabel(name);
        return operator(code, Type.UNKNOWN, labelRef(label), opt);
      }

    // unary operators with type
    case Op.CONVFI:
      code = Op.CONVFU;
      // fall thru
    case Op.NEG:
    case Op.BNOT:
    case Op.CONVSX:
    case Op.CONVZX:
    case Op.CONVIT:
    case Op.CONVFX:
    case Op.CONVFT:
    case Op.CONVFS:
    case Op.CONVFU:
    case Op.CONVSF:
    case Op.CONVUF:
    case Op.MEM:
      return operator(code, Type.decode((String)stmt.elem2nd()),
                      decodeLir((ImList)stmt.elem3rd(), func, mod), opt);

    // unary operators w/o type
    case Op.JUMP:
      return operator(code, Type.UNKNOWN,
                      decodeLir((ImList)stmt.elem2nd(), func, mod), opt);


    // binary operators
    case Op.LSHU:
      code = Op.LSHS;
    // fall thru
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
      return operator(code, Type.decode((String)stmt.elem2nd()),
                      decodeLir((ImList)stmt.elem3rd(), func, mod),
                      decodeLir((ImList)stmt.elem4th(), func, mod), opt);

    case Op.JUMPC: // ternary operator
      {
        LirNode[] src = new LirNode[3];
        src[0] = decodeLir((ImList)stmt.elem2nd(), func, mod);
        src[1] = decodeLir((ImList)stmt.elem3rd(), func, mod);
        src[2] = decodeLir((ImList)stmt.elem4th(), func, mod);
        return operator(code, Type.UNKNOWN, src, opt);
      }

    case Op.CALL:
      {
        LirNode callee = decodeLir((ImList)stmt.elem2nd(), func, mod);
        int n = ((ImList)stmt.elem3rd()).length(); // parameter list length
        LirNode[] inParam = new LirNode[n];
        int i = 0;
        for (ImList p = (ImList)stmt.elem3rd(); !p.atEnd(); p = p.next())
          inParam[i++] = decodeLir((ImList)p.elem(), func, mod);
        n = ((ImList)stmt.elem4th()).length(); // return value list length
        LirNode[] outParam = new LirNode[n];
        i = 0;
        for (ImList p = (ImList)stmt.elem4th(); !p.atEnd(); p = p.next())
          outParam[i++] = decodeLir((ImList)p.elem(), func, mod);
        return operator(code, Type.UNKNOWN, callee,
                        operator(Op.LIST, Type.UNKNOWN, inParam, null),
                        operator(Op.LIST, Type.UNKNOWN, outParam, null), opt);
      }

    case Op.ASM:
      {
        LirNode body = decodeLir(stmt.elem2nd(), func, mod);
        int n = ((ImList)stmt.elem3rd()).length(); // parameter list length
        LirNode[] inParam = new LirNode[n];
        int i = 0;
        for (ImList p = (ImList)stmt.elem3rd(); !p.atEnd(); p = p.next())
          inParam[i++] = decodeLir((ImList)p.elem(), func, mod);
        n = ((ImList)stmt.elem4th()).length(); // parameter list length
        LirNode[] outParam = new LirNode[n];
        i = 0;
        for (ImList p = (ImList)stmt.elem4th(); !p.atEnd(); p = p.next())
          outParam[i++] = decodeLir((ImList)p.elem(), func, mod);
        n = ((ImList)stmt.elem5th()).length(); // return value list length
        LirNode[] inoutParam = new LirNode[n];
        i = 0;
        for (ImList p = (ImList)stmt.elem5th(); !p.atEnd(); p = p.next())
          inoutParam[i++] = decodeLir((ImList)p.elem(), func, mod);
        return operator(code, Type.UNKNOWN,
                        new LirNode[] {
                          body,
                          operator(Op.LIST, Type.UNKNOWN, inParam, null),
                          operator(Op.LIST, Type.UNKNOWN, outParam, null),
                          operator(Op.LIST, Type.UNKNOWN, inoutParam, null)
                        },
                        opt);
      }

    case Op.JUMPN:
      {
        LirNode value = decodeLir((ImList)stmt.elem2nd(), func, mod);
        ImList cases = (ImList)stmt.elem3rd();
        int n = cases.length();
        LirNode[] labels = new LirNode[n];
        int i = 0;
        for (ImList p = cases; !p.atEnd(); p = p.next()) {
          ImList c = (ImList)p.elem();
          labels[i++] = operator(Op.LIST, Type.UNKNOWN,
                                 decodeLir((ImList)c.elem(), func, mod),
                                 decodeLir((ImList)c.elem2nd(), func, mod),
                                 null);
        }
        return operator(code, Type.UNKNOWN, value,
                        operator(Op.LIST, Type.UNKNOWN, labels, null),
                        decodeLir((ImList)stmt.elem4th(), func, mod), opt);
      }

    case Op.PROLOGUE:
    case Op.EPILOGUE:
      {
        int n = 1;
        for (ImList p = stmt.next().next(); p != opt; p = p.next())
          n++;
        LirNode[] opr = new LirNode[n];
        ImList frame = (ImList)stmt.elem2nd();
        opr[0] = operator(Op.LIST, Type.UNKNOWN,
                          decodeFixnum(frame.elem()),
                          decodeFixnum(frame.elem2nd()), null);
        int i = 1;
        for (ImList p = stmt.next().next(); p != opt; p = p.next())
          opr[i++] = decodeLir((ImList)p.elem(), func, mod);
        return operator(code, Type.UNKNOWN, opr, opt);
      }

    case Op.PARALLEL:
    case Op.CLOBBER:
    case Op.USE:
      {
        int n = 0;
        for (ImList p = stmt.next(); p != opt; p = p.next())
          n++;
        LirNode[] src = new LirNode[n];
        n = 0;
        for (ImList p = stmt.next(); p != opt; p = p.next())
          src[n++] = decodeLir((ImList)p.elem(), func, mod);
        return operator(code, Type.UNKNOWN, src, opt);
      }

    case Op.IF:
      return operator(Op.IF, Type.decode((String)stmt.elem2nd()),
                      decodeLir((ImList)stmt.elem3rd(), func, mod),
                      decodeLir((ImList)stmt.elem4th(), func, mod),
                      decodeLir((ImList)stmt.elem5th(), func, mod), opt);

    case Op.LINE:
      {
        int lineno = Integer.parseInt((String)stmt.elem2nd());
        return operator
          (code, Type.UNKNOWN,
           iconst(Type.type(Type.INT, 32), lineno, optUntagged), opt);
      }

    case Op.INFO:
      if (stmt.elem2nd() == "LINE") {
        int lineno = Integer.parseInt((String)stmt.elem3rd());
        return operator
          (Op.LINE, Type.UNKNOWN,
           iconst(Type.type(Type.INT, 32), lineno, optUntagged), opt);
      } else {
        // #pragma
        if (NewInfoFlag) {
          int n = 0;
          for (ImList p = stmt.next(); !p.atEnd() && p != opt; p = p.next())
            n++;
          LirNode[] vec = new LirNode[n];
          int i = 0;
          for (ImList p = stmt.next(); !p.atEnd() && p != opt; p = p.next())
            vec[i++] = decodeLir(p.elem(), func, mod);
          return operator
            (Op.INFO, Type.UNKNOWN, vec, opt);
        } else {
          return operator
            (Op.INFO, Type.UNKNOWN, new LirNode[0], stmt.next());
        }
      }

    default:
      throw new SyntaxError("Unknown opCode");
    }

  }


  /** Parse (DATA) component. **/
  public LirNode decodeDataCompo(ImList node, Module module)
    throws SyntaxError {
    ImList opt = node.scanOpt();

    if (node.elem() == "SPACE")
      return operator(Op.SPACE, Type.UNKNOWN,
                      decodeFixnum(node.elem2nd()), opt);
    else if (node.elem() == "ZEROS")
      return operator(Op.ZEROS,  Type.UNKNOWN,
                      decodeFixnum(node.elem2nd()), opt);
    else {
      int type = Type.decode((String)node.elem());
      node = node.next();
      int nelem = node.length();
      LirNode[] vec = new LirNode[nelem];
      for (int i = 0; i < nelem; i++) {
        if (node.atEnd())
          throw new CantHappenException();
        Object obj = node.elem();
        if (obj instanceof String) {
          if (Type.tag(type) == Type.INT)
            vec[i] = iconst(type, Long.parseLong((String)obj));
          else if (Type.tag(type) == Type.FLOAT)
            vec[i] = fconst(type, Double.parseDouble((String)obj));
          else
            throw new CantHappenException();
        }
        else
          vec[i] = decodeLir((ImList)obj, null, module);
        node = node.next();
      }
      return operator(Op.LIST, type, vec, opt);
    }
  }


  private LirNode decodeFixnum(Object node) throws SyntaxError {
    if (node instanceof String)
      return iconst(module.targetMachine.typeAddress,
                    Long.parseLong((String)node), optUntagged);

    if (node instanceof ImList
        && (String)((ImList)node).elem() == "INTCONST")
      return iconst(Type.decode((String)((ImList)node).elem2nd()),
                    Long.parseLong((String)((ImList)node).elem3rd()),
                    optUntagged);

    throw new SyntaxError("fixnum expected");
  }


  /** Fold all constant-expressions in <code>tree</code>. **/
  public LirNode evalTree(LirNode tree) {
    int n = tree.nKids();
    for (int i = 0; i < n; i++) {
      LirNode node = evalTree(tree.kid(i));
      if (node != tree.kid(i))
        tree.setKid(i, node);
    }

    return foldConstant(tree);
  }


  /** Fold constant-expression in a L-exp. <code>node</code>. **/
  public LirNode foldConstant(LirNode node) {
    long svalue0 = 0, svalue1 = 0;
    long uvalue0 = 0, uvalue1 = 0;
    double dvalue0 = 0.0, dvalue1 = 0.0;
    boolean iconst0 = true, iconst1 = true;
    boolean fconst0 = true, fconst1 = true;

    int n = node.nKids();
    if (n >= 1) {
      switch (node.kid(0).opCode) {
      case Op.INTCONST:
        svalue0 = ((LirIconst)node.kid(0)).signedValue();
        uvalue0 = ((LirIconst)node.kid(0)).unsignedValue();
        fconst0 = false;
        break;
      case Op.FLOATCONST:
        dvalue0 = ((LirFconst)node.kid(0)).value;
        iconst0 = false;
        break;
      default:
        iconst0 = fconst0 = false;
        break;
      }
    }
    if (n >= 2) {
      switch (node.kid(1).opCode) {
      case Op.INTCONST:
        svalue1 = ((LirIconst)node.kid(1)).signedValue();
        uvalue1 = ((LirIconst)node.kid(1)).unsignedValue();
        fconst1 = false;
        break;
      case Op.FLOATCONST:
        dvalue1 = ((LirFconst)node.kid(1)).value;
        iconst1 = false;
        break;
      default:
        iconst1 = fconst1 = false;
        break;
      }
    }

    if (iconst0 && node.opCode == Op.IF) {
      if (svalue0 != 0)
        return node.kid(1);
      else
        return node.kid(2);
    }

    if (iconst0 && iconst1) {
      long ivalue = 0;
      int bits = Type.bits(node.type);
      long mask = (1L << bits) - 1;
      switch (node.opCode) {
      case Op.NEG: ivalue = -svalue0; break;
      case Op.BNOT: ivalue = ~svalue0; break;

      case Op.CONVSX: ivalue = svalue0; break;
      case Op.CONVZX: ivalue = uvalue0; break;
      case Op.CONVIT: ivalue = (svalue0 & mask); break;

      case Op.CONVSF:
        if (Type.tag(node.type) == Type.FLOAT) {
          double fvalue = svalue0;
          return fconst(node.type, fvalue);
        }
        break;
      case Op.CONVUF:
        if (Type.tag(node.type) == Type.FLOAT) {
          double fvalue = (new BigInteger(Long.toHexString(uvalue0), 16)).doubleValue();
          return fconst(node.type, fvalue);
        }
        break;

      case Op.ADD: ivalue = svalue0 + svalue1; break;
      case Op.SUB: ivalue = svalue0 - svalue1; break;
      case Op.MUL: ivalue = svalue0 * svalue1; break;
      //##77 case Op.DIVS: ivalue = svalue0 / svalue1; break;
      //##77 BEGIN
      case Op.DIVS:
        if (svalue1 == 0) {
          return node;
        }else {
          ivalue = svalue0 / svalue1; break;
        }
      //##77 END
      case Op.DIVU:
        if (bits >= 64)
          return node;
        if (uvalue1 == 0) //##77
          return node; //##77
        ivalue = uvalue0 / uvalue1;
        break;
      //##77 case Op.MODS: ivalue = svalue0 % svalue1; break;
      //##77 BEGIN
      case Op.MODS:
        if (svalue1 == 0) {
          return node;
        }else {
          ivalue = svalue0 % svalue1; break;
        }
      //##77 END
      case Op.MODU:
        if (bits >= 64)
          return node;
        if (uvalue1 == 0) //##77
          return node; //##77
        ivalue = uvalue0 % uvalue1;
        break;
      case Op.BAND: ivalue = svalue0 & svalue1; break;
      case Op.BOR: ivalue = svalue0 | svalue1; break;
      case Op.BXOR: ivalue = svalue0 ^ svalue1; break;
      case Op.LSHS: ivalue = svalue0 << svalue1; break;
      case Op.LSHU: ivalue = uvalue0 << svalue1; break;
      case Op.RSHS: ivalue = svalue0 >> svalue1; break;
      case Op.RSHU: ivalue = uvalue0 >>> svalue1; break;
      case Op.TSTEQ: ivalue = (svalue0 == svalue1) ? 1 : 0; break;
      case Op.TSTNE: ivalue = (svalue0 != svalue1) ? 1 : 0; break;
      case Op.TSTLTS: ivalue = (svalue0 < svalue1) ? 1 : 0; break;
      case Op.TSTLES: ivalue = (svalue0 <= svalue1) ? 1 : 0; break;
      case Op.TSTGTS: ivalue = (svalue0 > svalue1) ? 1 : 0; break;
      case Op.TSTGES: ivalue = (svalue0 >= svalue1) ? 1 : 0; break;
      case Op.TSTLTU:
        if ((uvalue0 ^ uvalue1) < 0) { // signs differ?
          ivalue = (uvalue0 < 0) ? 0 : 1;
        } else
          ivalue = (uvalue0 < uvalue1) ? 1 : 0;
        break;
      case Op.TSTLEU:
        if ((uvalue0 ^ uvalue1) < 0) { // signs differ?
          ivalue = (uvalue0 < 0) ? 0 : 1;
        } else
          ivalue = (uvalue0 <= uvalue1) ? 1 : 0;
        break;
      case Op.TSTGTU:
        if ((uvalue0 ^ uvalue1) < 0) { // signs differ?
          ivalue = (uvalue0 >= 0) ? 0 : 1;
        } else
          ivalue = (uvalue0 > uvalue1) ? 1 : 0;
        break;
      case Op.TSTGEU:
        if ((uvalue0 ^ uvalue1) < 0) { // signs of two are different?
          ivalue = (uvalue0 >= 0) ? 0 : 1;
        } else
          ivalue = (uvalue0 >= uvalue1) ? 1 : 0;
        break;

      default:
        return node;
      }
      return iconst(node.type, ivalue);
    }

    if (fconst0 && fconst1) {
      double fvalue = 0;
      int ivalue = 0;

      switch (node.opCode) {
      case Op.TSTEQ: ivalue = (dvalue0 == dvalue1) ? 1 : 0; break;
      case Op.TSTNE: ivalue = (dvalue0 != dvalue1) ? 1 : 0; break;
      case Op.TSTLTS: ivalue = (dvalue0 < dvalue1) ? 1 : 0; break;
      case Op.TSTLES: ivalue = (dvalue0 <= dvalue1) ? 1 : 0; break;
      case Op.TSTGTS: ivalue = (dvalue0 > dvalue1) ? 1 : 0; break;
      case Op.TSTGES: ivalue = (dvalue0 >= dvalue1) ? 1 : 0; break;
      default:
        switch (node.opCode) {
        case Op.CONVFS:
        case Op.CONVFU:
          if (Type.tag(node.type) == Type.INT)
            return iconst(node.type, (long)dvalue0);
          break;
        case Op.CONVFT:
          if (Type.bits(node.type) <= 32)
            fvalue = (float)dvalue0;
          else
            fvalue = dvalue0;
          break;
        case Op.CONVFX:
          fvalue = dvalue0;
          break;
        case Op.ADD: fvalue = dvalue0 + dvalue1; break;
        case Op.SUB: fvalue = dvalue0 - dvalue1; break;
        case Op.MUL: fvalue = dvalue0 * dvalue1; break;
        case Op.DIVS: fvalue = dvalue0 / dvalue1; break;
        default:
          return node;
        }
        return fconst(node.type, fvalue);
      }
      return iconst(node.type, ivalue);
    }

    // At least one of operands is not a constant.
    switch (node.opCode) {
    case Op.ADD:
      if (iconst1 && node.kid(0).opCode == Op.ADD) {
        if (node.kid(0).kid(1).opCode == Op.INTCONST)
          return node
            (Op.ADD, node.type, node.kid(0).kid(0),
             iconst(node.type,
                    ((LirIconst)node.kid(0).kid(1)).signedValue() + svalue1));
        if (node.kid(0).kid(0).opCode == Op.INTCONST)
          return node
            (Op.ADD, node.type, node.kid(0).kid(1),
             iconst(node.type,
                    ((LirIconst)node.kid(0).kid(0)).signedValue() + svalue1));
      }
      if (iconst0 && node.kid(1).opCode == Op.ADD) {
        if (node.kid(1).kid(1).opCode == Op.INTCONST)
          return node
            (Op.ADD, node.type, node.kid(1).kid(0),
             iconst(node.type,
                    ((LirIconst)node.kid(1).kid(1)).signedValue() + svalue0));
        if (node.kid(1).kid(0).opCode == Op.INTCONST)
          return node
            (Op.ADD, node.type, node.kid(1).kid(1),
             iconst(node.type,
                    ((LirIconst)node.kid(1).kid(0)).signedValue() + svalue0));
      }

      if (iconst0 && svalue0 == 0)
        return node.kid(1);
      if (fconst0 && dvalue0 == 0.0)
        return node.kid(1);
      // fall thru
    case Op.SUB:
      if (iconst1 && svalue1 == 0)
        return node.kid(0);
      if (fconst1 && dvalue1 == 0.0)
        return node.kid(0);
      break;

    case Op.MUL:
      if (iconst0 && svalue0 == 0)
        return node.kid(0);
      if (iconst0 && Misc.powersOf2(svalue0))
        return foldConstant(makeMulShift(node.type, node.kid(1), svalue0));
      if (iconst1 && svalue1 == 0)
        return node.kid(1);
      if (iconst1 && Misc.powersOf2(svalue1))
        return foldConstant(makeMulShift(node.type, node.kid(0), svalue1));

      if (fconst0 && dvalue0 == 0.0)
        return node.kid(0);
      if (fconst0 && dvalue0 == 1.0)
        return node.kid(1);
      if (fconst1 && dvalue1 == 0.0)
        return node.kid(1);
      if (fconst1 && dvalue1 == 1.0)
        return node.kid(0);
      break;

    case Op.DIVU:
      if (iconst1 && Misc.powersOf2(svalue1))
        return foldConstant(makeDivuShift(node.type, node.kid(0), svalue1));
      break;

    case Op.DIVS:
      if (iconst1 && svalue1 == 1)
        return node.kid(0);
      break;

    case Op.MODU:
      if (iconst1 && Misc.powersOf2(svalue1))
        return node
          (Op.BAND, node.type, node.kid(0),
           iconst(node.type, svalue1 - 1));
      break;

    case Op.LSHU:
    case Op.LSHS:
    case Op.RSHU:
    case Op.RSHS:
      if (iconst1 && svalue1 == 0)
        return node.kid(0);
      break;

    case Op.CONVIT:
    case Op.CONVSX:
    case Op.CONVZX:
    case Op.CONVFT:
    case Op.CONVFX:
      if (node.type == node.kid(0).type)
        return node.kid(0);
      break;
    }

    return node;
  }


  /** Return shift node as an replacement of MUL. **/
  private LirNode makeMulShift(int type, LirNode x, long val) {
    int n = Misc.binlog(val);
    if (n == 0)
      return x;
    else
      return node(Op.LSHS, type, x, iconst(type, n));
  }

  /** Return shift node as an replacement of DIVU. **/
  private LirNode makeDivuShift(int type, LirNode x, long val) {
    int n = Misc.binlog(val);
    if (n == 0)
      return x;
    else
      return node(Op.RSHU, type, x, iconst(type, n));
  }


}
