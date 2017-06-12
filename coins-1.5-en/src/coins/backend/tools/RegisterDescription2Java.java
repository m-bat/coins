/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.tools;

import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.BitMapSet;
import coins.backend.util.ImList;
import coins.backend.util.NumberSet;
import coins.backend.util.QuotedString;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/** Preprocessing *.tmd to make register-model description parameters. **/
class RegisterDescription2Java {

  static class RegSymbol {
    final String name;
    final ImList record;
    final int type;
    final int id;

    RegSymbol parent;
    int from = 0;
    int regId = 0;

    boolean visited;

    RegSymbol(String name, int type, ImList record, int id) {
      this.record = record;
      this.name = name;
      this.type = type;
      this.id = id;

      this.parent = this;
    }

    /** Set subregister relation. **/
    void setSubregOf(RegSymbol parent, int position) {
      this.parent = parent;
      from = Type.bits(type) * position;
    }

    /** Canonicalize parent and from field. **/
    void canonRelation() {
      if (visited)
        throw new Error("SUBREG relation loop.");

      if (parent != this) {
        visited = true;
        parent.canonRelation();
        visited = false;
        from += parent.from;
        parent = parent.parent;
      }
    }

    public String toString() {
      return "(REG " + Type.toString(type) + " \"" + name + "\")";
    }
  }

  static class SymTab {
    Map table = new HashMap();
    BiList list = new BiList();
    int nsyms = 0;

    RegSymbol addSymbol(ImList desc) throws SyntaxError {
      String name = ((QuotedString)desc.elem()).body;
      RegSymbol sym = (RegSymbol)table.get(name);
      if (sym == null) {
        int type = Type.decode((String)desc.elem3rd());
        sym = new RegSymbol(name, type, desc, nsyms++);
        table.put(name, sym);
        list.add(sym);
      }
      return sym;
    }

    RegSymbol addSymbol(RegSymbol parent, int type, int position) {
      String name = parent.name + "/" + Type.toString(type) + "/" + position;
      RegSymbol sym = (RegSymbol)table.get(name);
      if (sym == null) {
        ImList desc = ImList.list(name, "REG", Type.toString(type),
                                  Integer.toString(Type.bytes(type)), "0");
        sym = new RegSymbol(name, type, desc, nsyms++);
        sym.setSubregOf(parent, position);
        table.put(name, sym);
        list.add(sym);
      }
      return sym;
    }

    RegSymbol get(String name) {
      return (RegSymbol)table.get(name);
    }

  }


  /** Set of registers. **/
  static class RegisterSet {
    BitMapSet regset;
    String name;
    int type;
    int id;

    RegisterSet(BitMapSet set, String name, int type) {
      this.name = name;
      regset = set;
      this.type = type;
      id = -1;
    }

    void setId(int id) {
      this.id = id;
    }
  }


  SymTab symtbl = new SymTab();
  PrintWriter debOut = new PrintWriter(System.err);

  PrintWriter javaOut;
  String targetName;

  BiList registerSets = new BiList();

  static final int MAXSET = 1000;

  RegSymbol[] regVec;
  RegisterSet[] regsetVec = new RegisterSet[MAXSET];
  int nSets;
  int nRegs = 1;

  int addrType;
  int boolType;


  RegSymbol installRegister(RegSymbol sym) {
    if (sym.regId == 0)
      sym.regId = nRegs++;
    return sym;
  }

  RegSymbol installSubRegister(RegSymbol parent, int type, int position) {
    RegSymbol sym = symtbl.addSymbol(parent, type, position);
    if (sym.regId == 0)
      sym.regId = nRegs++;
    return sym;
  }


  /** Canonicalize parent information of registers. **/
  void canonRegRelations() {
    for (BiLink p = symtbl.list.first(); !p.atEnd(); p = p.next()) {
      RegSymbol r = (RegSymbol)p.elem();
      r.canonRelation();
    }
  }


  void setRegVector() {
    regVec = new RegSymbol[nRegs];
    regVec[0] = null;
    for (BiLink p = symtbl.list.first(); !p.atEnd(); p = p.next()) {
      RegSymbol r = (RegSymbol)p.elem();
      if (r.regId != 0)
        regVec[r.regId] = r;
    }
  }


  RegisterSet findRegisterSet(BitMapSet set) {
    for (BiLink p = registerSets.first(); !p.atEnd(); p = p.next()) {
      RegisterSet regset = (RegisterSet)p.elem();
      if (regset.regset.equals(set))
        return regset;
    }
    return null;
  }


  /** Return register set which has set 'set' in it.
   *  If not exist, install it. **/
  RegisterSet installRegisterSet(BitMapSet set, String name, int type) {
    RegisterSet regset = findRegisterSet(set);
    if (regset != null) {
      if (regset.name != null && name != null)
        throw new Error("There are two same sets: " + regset.name + ", " + name);
      if (name != null)
        regset.name = name;
    } else {
      regset = new RegisterSet(set, name, type);
      registerSets.add(regset);
    }
    return regset;
  }


  /** Return and set of RegisterSet x and y. */
  RegisterSet andSet(RegisterSet x, RegisterSet y) {
    BitMapSet andset = (BitMapSet)x.regset.clone();
    andset.meet(y.regset);
    RegisterSet regset = findRegisterSet(andset);
    if (regset == null) {
      regset = installRegisterSet(andset, null, x.type);
      if (nSets >= MAXSET)
        throw new Error("Too many sets (Max " + MAXSET + ")");
      regset.setId(nSets);
      regsetVec[nSets++] = regset;
    }
    return regset;
  }


  /** Return true if register x interferes y. **/
  boolean interferes(RegSymbol x, RegSymbol y) {
    if (x.parent != y.parent)
      return false;

    int xwidth = Type.bits(x.type);
    int ywidth = Type.bits(y.type);
    return (x.from <= y.from && y.from < x.from + xwidth
            || y.from <= x.from && x.from < y.from + ywidth);
  }


  /** Return true if register x covers y. **/
  boolean covers(RegSymbol x, RegSymbol y) {
    if (x.parent != y.parent)
      return false;
    if (x.type == 0)
      return true;
    if (y.type == 0)
      return false;

    int xwidth = Type.bits(x.type);
    int ywidth = Type.bits(y.type);
    return (x.from <= y.from && y.from + ywidth <= x.from + xwidth);
  }



  /** Return true if register set x interferes y. **/
  boolean interferes(RegisterSet x, RegisterSet y) {
    // group elements of x & y that have same symbol.
    BiList[] regs = new BiList[symtbl.nsyms];
    for (NumberSet.Iterator it = x.regset.iterator(); it.hasNext(); ) {
      RegSymbol r = regVec[it.next()];
      if (regs[r.parent.id] == null)
        regs[r.parent.id] = new BiList();
      regs[r.parent.id].add(r);
    }

    for (NumberSet.Iterator it = y.regset.iterator(); it.hasNext(); ) {
      RegSymbol r = regVec[it.next()];
      if (regs[r.parent.id] != null) {
        for (BiLink p = regs[r.parent.id].first(); !p.atEnd(); p = p.next()) {
          RegSymbol s = (RegSymbol)p.elem();
          if (interferes(r, s))
            return true;
        }
      }
    }
    return false;
  }


  /** Return relative weight of register set y when compared to x.
   ** Relative-weight(x,y)
   **     = number of elements in x covered by the element of y. **/
  int relativeWeight(RegisterSet x, RegisterSet y) {

    // group elements of x & y that have same symbol.
    BiList[] regs = new BiList[symtbl.nsyms];
    for (NumberSet.Iterator it = x.regset.iterator(); it.hasNext(); ) {
      RegSymbol r = regVec[it.next()];
      if (regs[r.parent.id] == null)
        regs[r.parent.id] = new BiList();
      regs[r.parent.id].add(r);
    }

    int max = 0;
    for (NumberSet.Iterator it = y.regset.iterator(); it.hasNext(); ) {
      RegSymbol r = regVec[it.next()];
      if (regs[r.parent.id] != null) {
        int n = 0;
        for (BiLink p = regs[r.parent.id].first(); !p.atEnd(); p = p.next()) {
          RegSymbol s = (RegSymbol)p.elem();
          if (interferes(r, s))
            n++;
        }
        if (n > max)
          max = n;
      }
    }
    return max;
  }
  

  /** Install register set. **/
  void parseRegisterSet(String setname, ImList sexp)
    throws SyntaxError {

    BitMapSet set = new BitMapSet();
    int type = -1;

    for (ImList p = sexp; !p.atEnd(); p = p.next()) {
      Object elem = p.elem();
      if (!(elem instanceof ImList))
        throw new Error("Bad register set form: " + setname);

      ImList node = (ImList)elem;

      // Parse (REG t "%r") or (SUBREG t (REG t "%r") pos)
      if (node.elem() == "SUBREG") {
        ImList sub = (ImList)node.elem3rd();
        int subtype = Type.decode((String)node.elem2nd());
        if (type < 0)
          type = subtype;
        // if (subtype != type)
        //   throw new Error("Different type in regset: " + setname);
        int position = Integer.parseInt((String)node.elem4th());
        if (sub.elem() != "REG")
          throw new Error("REG expected but " + sub.elem());
        int regtype = Type.decode((String)sub.elem2nd());
        String name = ((QuotedString)sub.elem3rd()).body;
        RegSymbol sym = symtbl.get(name);
        if (sym == null)
          throw new Error("Undeclared REG: " + name);
        RegSymbol reg = installSubRegister(sym, subtype, position);
        set.add(reg.regId);
      }
      else if (node.elem() == "REG") {
        int regtype = Type.decode((String)node.elem2nd());
        if (type < 0)
          type = regtype;
        // if (regtype != type)
        //   throw new Error("Different type in regset: " + setname);
        String name = ((QuotedString)node.elem3rd()).body;
        RegSymbol sym = symtbl.get(name);
        if (sym == null)
          throw new Error("Undeclared REG: " + name);
        RegSymbol reg = installRegister(sym);
        set.add(reg.regId);
      }
      else
        throw new SyntaxError("REG expected but " + node.elem());
    }
    installRegisterSet(set, setname, type);
  }


  /** table generation **/
  void genTables(String targetName, PrintWriter out) {
    // Compute all possible register sets.
    // z1 = x1 & y1, z2 = x2 & y2, z3 = z1 & z2, ...

    canonRegRelations();
    setRegVector();

    // install empty set.
    RegisterSet nullSet = installRegisterSet(new BitMapSet(), null, -1);
    regsetVec[0] = nullSet;
    nullSet.setId(0);
    nSets = 1;

    // install singletons.
    for (int i = 1; i < nRegs; i++) {
      BiList l = new BiList();
      l.add(regVec[i]);
      BitMapSet s = new BitMapSet();
      s.add(regVec[i].regId);
      regsetVec[nSets] = installRegisterSet(s, null, regVec[i].type);
      regsetVec[nSets].setId(nSets);
      nSets++;
    }

    // install other sets.
    for (BiLink p = registerSets.first(); !p.atEnd(); p = p.next()) {
      RegisterSet regset = (RegisterSet)p.elem();
      if (regset.id < 0) {
        regset.setId(nSets);
        regsetVec[nSets++] = regset;
      }
    }

    // install derived sets and make meet operation table.
    int[] andTbl = new int[16];
    {
      int k = 0;
      for (int i = 0; i < nSets; i++) {
        for (int j = 0; j < i; j++) {
          // interference test - deprecated.
          // interfereTbl[k] = interferes(regsetVec[i], regsetVec[j]);
          
          // intersection
          RegisterSet regset = andSet(regsetVec[i], regsetVec[j]);
          if (k >= andTbl.length) {
            // reallocate andTbl.
            int[] tmp = new int[andTbl.length * 2];
            for (int ii = 0; ii < andTbl.length; ii++)
              tmp[ii] = andTbl[ii];
            andTbl = tmp;
          }
          andTbl[k] = regset.id;
          k++;
        }
      }
    }

    int[][] weightTbl = new int[nSets][nSets];

    // Set relative weight table.
    for (int i = 0; i < nSets; i++) {
      for (int j = 0; j < nSets; j++) {
        weightTbl[i][j] = relativeWeight(regsetVec[i], regsetVec[j]);
      }
    }

    out.println("  public int nRegisters() { return " + nRegs + "; }");
    out.println("  public int nRegsets() { return " + nSets + "; }");
    out.println();

    if (addrType == 0)
      System.err.println("Warning: *type-address* undefined.");
    out.println("  public int typeAddress() { return " + addrType + "; }");
    if (boolType == 0)
      System.err.println("Warning: *type-bool* undefined.");
    out.println("  public int typeBool() { return " + boolType + "; }");

    out.println("  public String[] getSymName() {");
    out.println("    return new String[]{");
    for (BiLink p = symtbl.list.first(); !p.atEnd(); p = p.next()) {
      RegSymbol sym = (RegSymbol)p.elem();
      out.println("      \"" + sym.name + "\",");
    }
    out.println("    };");
    out.println("  };");

    out.println("  public int[] getSymType() {");
    out.println("    return new int[] {");
    for (BiLink p = symtbl.list.first(); !p.atEnd(); p = p.next()) {
      RegSymbol sym = (RegSymbol)p.elem();
      out.println("      " + sym.type + ",");
    }
    out.println("    };");
    out.println("  };");
    out.println("  public int[] getSymRegNumber() {");
    out.println("    return new int[] {");
    for (BiLink p = symtbl.list.first(); !p.atEnd(); p = p.next()) {
      RegSymbol sym = (RegSymbol)p.elem();
      out.println("      " + sym.regId + ",");
    }
    out.println("    };");
    out.println("  };");
    out.println();

    out.println("  public short[][] getOverlapReg() {");
    out.println("    return new short[][] { {},");
    for (int i = 1; i < nRegs; i++) {
      out.print("  /* " + i + ": */ {");
      for (int j = 1; j < nRegs; j++) {
        if (i != j && interferes(regVec[i], regVec[j]))
          out.print(j + ",");
      }
      out.println("},");
    }
    out.println("    };");
    out.println("  };");
    out.println();

    out.println("  public short[][] getSuperReg() {");
    out.println("    return new short[][] { {},");
    for (int i = 1; i < nRegs; i++) {
      out.print("  /* " + i + ": */ {");
      for (int j = 1; j < nRegs; j++) {
        if (i != j && covers(regVec[j], regVec[i]))
          out.print(j + ",");
      }
      out.println("},");
    }
    out.println("    };");
    out.println("  };");
    out.println();

    out.println("  public short[][] getSubReg() {");
    out.println("    return new short[][] { {},");
    for (int i = 1; i < nRegs; i++) {
      out.print("  /* " + i + ": */ {");
      for (int j = 1; j < nRegs; j++) {
        if (i != j && covers(regVec[i], regVec[j]))
          out.print(j + ",");
      }
      out.println("},");
    }
    out.println("    };");
    out.println("  };");
    out.println();



    out.println("  public String[] getRegsetName() {");
    out.println("    return new String[] {");
    for (int i = 0; i < nSets; i++) {
      if (regsetVec[i].name != null)
        out.println("      \"" + regsetVec[i].name + "\",");
    }
    out.println("    };");
    out.println("  };");
    out.println("  public int[] getRegsetNumber() {");
    out.println("    return new int[] {");
    for (int i = 0; i < nSets; i++) {
      if (regsetVec[i].name != null)
        out.println("      " + i + ",");
    }
    out.println("    };");
    out.println("  };");
    out.println("  public short[][] getRegsetMap() {");
    out.println("    return new short[][] {");
    for (int i = nRegs; i < nSets; i++) {
      out.print("      {");
      for (NumberSet.Iterator it = regsetVec[i].regset.iterator(); it.hasNext(); )
        out.print(it.next() + ",");
      out.println("},");
    }
    out.println("    };");
    out.println("  };");
    out.println();


    out.println("  public short[] getRegsetNAvail() {");
    out.println("    return new short[] {");
    {
      int col = 0;
      for (int i = 0; i < nSets; i++) {
        if (col == 0) out.print("      ");
        out.print(regsetVec[i].regset.size() + ", ");
        if (++col == 10) {
          out.println();
          col = 0;
        }
      }
    }
    out.println("    };");
    out.println("  };");
    out.println();


    out.println("  public int[] getCompAndTbl() {");
    out.println("    return new int[] {");
    {
      int k = 0;
      int pre = 0;
      int col = 0;
      for (int i = 0; i < nSets; i++) {
        for (int j = 0; j < i; j++) {
          if (andTbl[k] != 0) {
            if (pre == 0)
              out.print("      " + -k + ",");
            out.print(andTbl[k] + ",");
          } else {
            if (pre != 0)
              out.println();
          }
          pre = andTbl[k];
          k++;
        }
      }
    }
    out.println("    };");
    out.println("  };");
    out.println();


    out.println("  public int[] getCompWeightTbl() {");
    out.println("    return new int[] {");
    {
      int k = 0;
      for (int i = 0; i < nSets; i++) {
        int pre = 0;
        int count = 0;
        for (int j = 0; j < nSets; j++) {
          if (weightTbl[i][j] != pre) {
            if (pre != 0)
              out.println((count * 64 + pre) + ",");
            count = 0;
            if (weightTbl[i][j] != 0)
              out.print("    " + (i * nSets + j) + ",");
          }
          count++;
          pre = weightTbl[i][j];
        }
        if (pre != 0)
          out.println((count * 64 + pre) + ",");
      }
      out.println("    };");
      out.println("  };");
    }

    out.println();

    out.println("  public int[] getRegsetTypeTbl() {");
    out.println("    return new int[] {");
    {
      for (int i = 0; i < nSets; i++)
        out.println("    " + regsetVec[i].type + ",");
    }
    out.println("    };");
    out.println("  }");

    out.println("}");
  }


  /** Create register description table. **/
  RegisterDescription2Java(String targetName, String file, String packageName)
    throws IOException {

    this.targetName = targetName;
    javaOut = new PrintWriter(new FileOutputStream(file));

    // print header.
    javaOut.println("package " + packageName + ";");
    javaOut.println();
    javaOut.println("import coins.backend.Storage;");
    javaOut.println("import coins.backend.sym.SymTab;");
    javaOut.println("import coins.backend.util.ImList;");
    javaOut.println();
    javaOut.println("public class MachineParams_" + targetName
                    + " extends coins.backend.MachineParams {");
    javaOut.println();
  }


  /** Parse form (def *reg-foo* ...) **/
  void doDef(ImList form) throws SyntaxError {
    if (form.elem() != "def")
      throw new Error("def expected but " + form.elem());

    Object value = form.elem3rd();

    if (form.elem2nd() instanceof ImList) {
      // Parsing (def (REG I8 "%ah") (SUBREG I8 (REG I32 "%eax") 1))
      ImList definee = (ImList)form.elem2nd();
      if (definee.elem() == "REG") {
        String name = ((QuotedString)definee.elem3rd()).body;
        RegSymbol left = symtbl.get(name);
        if (left == null)
          throw new Error("Undeclared:" + definee);
        if (Type.decode((String)definee.elem2nd()) != left.type)
          throw new Error("Bad register type: " + definee);
        ImList definer = (ImList)value;
        if (definer.elem() != "SUBREG")
          throw new Error("SUBREG expected");
        if (Type.decode((String)definer.elem2nd()) != left.type)
          throw new Error("Bad SUBREG type: " + definee);
        ImList parentReg = (ImList)definer.elem3rd();
        if (parentReg.elem() != "REG")
          throw new Error("REG expected");
        RegSymbol parent = symtbl.get(((QuotedString)parentReg.elem3rd()).body);
        if (parent == null)
          throw new Error("Undeclared: " + parentReg);
        if (Type.decode((String)parentReg.elem2nd()) != parent.type)
          throw new Error("Bad register type: " + definee);

        int position = Integer.parseInt((String)definer.elem4th());
        left.setSubregOf(parent, position);
        return;
      }
    }

    String defname = (String)form.elem2nd();
    if (defname == "*real-reg-symtab*") {
      // Parse physical register symbol table
      ImList p = (ImList)value;
      if (p.elem() != "SYMTAB")
        throw new Error("SYMTAB expected but " + p.elem());

      p = p.next();
      /* int i = 0; */
      for (; !p.atEnd(); p = p.next())
        symtbl.addSymbol((ImList)p.elem());
    }
    else if (defname == "*cmplib-xref-symtab*") {
      // External symbol requied
      ImList p = (ImList)value;
      if (p.elem() != "SYMTAB")
        throw new Error("SYMTAB expected but " + p.elem());
      javaOut.println("  public void addRequired(SymTab symtbl) {");
      for (p = p.next(); !p.atEnd(); p = p.next()) {
        ImList e = (ImList)p.elem();
        javaOut.println("    symtbl.addSymbol("
                    + "\"" + ((QuotedString)e.elem()).body + "\", "
                    + "Storage." + e.elem2nd() + ", "
                    + Type.decode((String)e.elem3rd()) + ", "
                    + e.elem4th() + ", "
                    + "\"" + ((QuotedString)e.elem5th()).body + "\", "
                    + "\"" + e.elem6th() + "\", "
                    + "ImList.Empty);");
      }
      javaOut.println("  }");
      javaOut.println();
    }
    else if (defname.startsWith("*reg") && defname.endsWith("*"))
      parseRegisterSet(defname, (ImList)value);

    else if (defname.equals("*type-address*"))
      addrType = Type.decode((String)value);
    else if (defname.equals("*type-bool*"))
      boolType = Type.decode((String)value);

    // otherwise simply ignored
  }


  /** Generate register description table. **/
  void close() {
    genTables(targetName, javaOut);

    javaOut.flush();
    debOut.flush();
  }

}
