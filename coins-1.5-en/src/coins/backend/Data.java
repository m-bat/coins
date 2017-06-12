/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.lir.LirFactory;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.sym.SymStatic;
import coins.backend.sym.Symbol;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import java.io.PrintWriter;
import java.math.BigInteger;


/** Represent L-Data. */
public class Data extends ModuleElement {

  /** Contents of (DATA) **/
  public final LirNode[] components;

  /** Factory object for creating LirNode **/
  private final LirFactory newLir;

  /** Parse S-expression DATA description and convert to internal form */
  public Data(Module module, ImList ptr)
    throws SyntaxError  {
    super(module, ((QuotedString)ptr.elem2nd()).body);

    newLir = module.newLir;
    if (ptr.elem() != Keyword.DATA)
      throw new SyntaxError("DATA expected but: " + ptr.elem());
    ptr = ptr.next();
    ptr = ptr.next();

    // components: (SPACE bytes) or (ZEROS bytes) or (Type value...)
    int nelem = ptr.length();
    components = new LirNode[nelem];
    for (int i = 0; i < nelem; i++) {
      if (ptr.atEnd())
        throw new CantHappenException();
      components[i] = newLir.evalTree(newLir.decodeDataCompo((ImList)ptr.elem(), module));
      ptr = ptr.next();
    }
  }


  /** Create new constant/space/zeros object. **/
  public Data(Module module, Symbol sym, LirNode value) {
    super(module, (SymStatic)sym);

    newLir = module.newLir;
    components = new LirNode[1];

    switch (value.opCode) {
    case Op.INTCONST:
    case Op.FLOATCONST:
      components[0] = newLir.node(Op.LIST, value.type, value);
      break;

    case Op.SPACE:
    case Op.ZEROS:
      components[0] = value;
      break;

    default:
      throw new CantHappenException("Attempt to make non-constant DATA object.");
    }
  }


  /** Return the value if this Data is a scaler, null otherwise. **/
  public LirNode scalerValue() {
    if (components.length == 1
        && components[0].opCode == Op.LIST
        && components[0].nKids() == 1)
      return components[0].kid(0);
    else
      return null;
  }



  /** Apply some transformation on DATA with arguments. **/
  public boolean apply(LocalTransformer xform, ImList args) {
    return xform.doIt(this, args);
  }

  /** Apply some transformation on DATA. **/
  public boolean apply(LocalTransformer xform) {
    return xform.doIt(this, ImList.list());
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    ImList list = ImList.Empty;
    list = new ImList("DATA", list);
    list = new ImList(new QuotedString(symbol.name), list);

    for (int i = 0; i < components.length; i++) {
      LirNode node = components[i];
      if (node.opCode == Op.LIST) {
        ImList sub = ImList.Empty;
        sub = new ImList(Type.toString(node.type), sub);
        int n = node.nKids();
        for (int j = 0; j < n; j++) {
          LirNode elem = node.kid(j);
          if (elem.opCode == Op.INTCONST)
            sub = new ImList(((LirIconst)elem).value + "", sub);
          else if (elem.opCode == Op.FLOATCONST)
            sub = new ImList(((LirFconst)elem).value + "", sub);
          else
            sub = new ImList(elem.toSexp(), sub);
        }
        list = new ImList(sub.destructiveReverse(), list);
      } else
        list = new ImList(node.toSexp(), list);
    }

    return list.destructiveReverse();
  }
    
    

  /** Print DATA in standard form. */
  public void printStandardForm(PrintWriter out) {
    out.print("(DATA \"" + symbol.name + "\"");
    for (int i = 0; i < components.length; i++) {
      LirNode node = components[i];
      if (node.opCode == Op.LIST) {
        out.print(" (" + Type.toString(node.type));
        int n = node.nKids();
        for (int j = 0; j < n; j++) {
          LirNode elem = node.kid(j);
          if (elem.opCode == Op.INTCONST)
            out.print(" " + ((LirIconst)elem).value);
          else if (elem.opCode == Op.FLOATCONST)
            out.print(" " + ((LirFconst)elem).value);
          else
            out.print(" " + elem);
        }
        out.print(")");
      } else
        out.print(" " + node);
    }
    out.println(")");
  }


  /** Dump internal data structure of the Data. */
  public void printIt(PrintWriter out) {
    printStandardForm(out);
  }


  /** Dump internal data structure of the Data (Analysis ignored). */
  public void printIt(PrintWriter out, LocalAnalyzer[] anals) {
    printStandardForm(out);
  }

}
