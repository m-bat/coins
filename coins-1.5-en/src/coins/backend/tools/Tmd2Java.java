/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.tools;

import coins.backend.Op;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/*
 * Parse *.tmd file and write tables in Java.
 */
class Tmd2Java {
  static final String PROTOFILE = "tmd.java.proto";

  static final int ANYTYPE = -1;

  private Tmd2Java() {}

  static String targetName;

  static PrintWriter out;
  static BufferedReader proto;

  static boolean defFileOnly = false;

  static boolean printExpanded = false;

  static RegisterDescription2Java regDesc;

  static Map definitions = new HashMap();

  static Map macros = new HashMap();


  /** Print expanded input. **/
  static void printBeautifully(ImList list) {
    PrintWriter wrt = new PrintWriter(System.out);
    for (; !list.atEnd(); list = list.next())
      ImList.printIt(wrt, list.elem());
  }

  /** RuleSet for rewriting. **/
  static RuleSet rewriting = new RuleSet("rewriting");

  /** RuleSet for instruction selection. **/
  static RuleSet instSel = new RuleSet("instSel");


  /** Grammar's right side hand pattern. **/
  static abstract class Pattern {
    /** Return nonterminal symbol which represents this pattern. **/
    abstract Nonterm lhs();

    /** Mark this pattern as 'used'. **/
    abstract void setUsed();

    /** Return subgoals in this pattern. **/
    abstract Nonterm[] realSubgoals();

    /** Return the java code fragments which represents $n. **/
    abstract String getNth(String parent, int n) throws SyntaxError;
  }


  /** Nonterminal Symbol **/
  static class Nonterm extends Pattern {
    final String name;
    final int value;

    /** defined? **/
    boolean defined;

    /** used? **/
    boolean used;

    /** If this nonterminal is a intermediate in derived rule,
     ** its right hand side. **/
    private OpPattern rhs;

    /** Default register set for this nonterminal. **/
    String defaultRegset;

    Nonterm(String name, int value) {
      this.name = name;
      this.value = value;
    }

    void setDefaultRegset(String regset) { this.defaultRegset = regset; }

    void setRhs(OpPattern rhs) { this.rhs = rhs; }

    void setDefined() { defined = true; }
    void setUsed() { used = true; }

    Nonterm[] realSubgoals() { return new Nonterm[]{this}; }

    Nonterm lhs() { return this; }

    OpPattern rhs() { return rhs; }

    String getNth(String parent, int n)
      throws SyntaxError {
      if (n == 0 || n == 1)
        return parent;
      else
        throw new SyntaxError("$" + n + ": too big");
    }

    public String toString() { return name; }

  } 


  /** Production's right hand side pattern. **/
  static class OpPattern extends Pattern {
    final int op;
    final int type;
    final Nonterm[] kids;
    final Object value;

    /** If this pattern is embedded in other pattern, its lhs **/
    private Nonterm lhs;

    OpPattern(int op, int type, Nonterm[] kids) {
      this.op = op;
      this.type = type;
      this.kids = kids;
      this.value = null;
    }

    OpPattern(int op, int type, Object value) {
      this.op = op;
      this.type = type;
      this.kids = new Nonterm[0];
      this.value = value;
    }

    public int hashCode() {
      int v = ((op << 8) + type) << 16;
      for (int i = 0; i < kids.length; i++)
        v += kids[i].hashCode();
      if (value != null)
        v += value.hashCode();
      return v;
    }

    public boolean equals(Object obj) {
      if (!(obj instanceof OpPattern))
        return false;
      OpPattern p = (OpPattern)obj;
      if (op != p.op || type != p.type || kids.length != p.kids.length)
        return false;
      for (int i = 0; i < kids.length; i++)
        if (kids[i] != p.kids[i])
          return false;
      if (value == null)
        return p.value == null;
      else
        return value.equals(p.value);
    }

    void setUsed() {
      for (int i = 0; i < kids.length; i++)
        kids[i].setUsed();
    }


    public String toString() {
      StringBuffer buf = new StringBuffer("(" + Op.toName(op)
                                          + " "
                                          + (type == ANYTYPE ? "_" : Type.toString(type)));
      for (int i = 0; i < kids.length; i++)
        buf.append(" " + kids[i].toString());
      if (value != null)
        buf.append(" " + value.toString());
      buf.append(")");
      return buf.toString();
    }

    /** Return left hand side of this pattern (for derived rule) **/
    Nonterm lhs() { return lhs; }

    /** Set this pattern's lhs (for derived rule) **/
    void setLhs(Nonterm lhs) { this.lhs = lhs; }

    private int countSubgoals() {
      int n = 0;
      for (int i = 0; i < kids.length; i++) {
        if (kids[i].rhs() != null)
          n += kids[i].rhs().countSubgoals();
        else
          n++;
      }
      return n;
    }

    private int fillSubgoals(Nonterm[] v, int ptr) {
      for (int i = 0; i < kids.length; i++) {
        if (kids[i].rhs() != null)
          ptr = kids[i].rhs().fillSubgoals(v, ptr);
        else
          v[ptr++] = kids[i];
      }
      return ptr;
    }

    /** Return vector of right side nonterminals
     **  (including ones in derived rule). **/
    Nonterm[] realSubgoals() {
      Nonterm[] v = new Nonterm[countSubgoals()];
      fillSubgoals(v, 0);
      return v;
    }

    /** Return the java code fragments which represents $n. **/
    String getNth(String parent, int n)
      throws SyntaxError {
      if (n == 0)
        return parent;
      int[] np = new int[1];
      np[0] = n;
      String s = getNth1(parent, np);
      if (s == null)
        throw new SyntaxError("$" + n + ": too big");
      return s;
    }


    private String getNth1(String parent, int[] n) {
      for (int i = 0; i < kids.length; i++) {
        String s = parent + ".kid(" + i + ")";
        if (kids[i].rhs() == null) {
          if (--n[0] == 0)
            return s;
        } else {
          String r = kids[i].rhs().getNth1(s, n);
          if (r != null)
            return r;
        }
      }
      return null;
    }

  }



  /** Production lhs <- rhs **/
  static class Prod implements Comparable {
    final String kind;
    final Nonterm lhs;
    final Pattern rhs;
    final int number;
    final int cost1, cost2;
    final String cond;
    final ImList code;
    final ImList value;
    final ImList clobber;
    final boolean useAfterDef;
    final String[] regsets;
    final long eqregs;
    final ImList replaceto;
    final ImList phase;

    Prod(int number, Nonterm lhs, Pattern rhs) {
      this.kind = "derived";
      this.lhs = lhs;
      this.rhs = rhs;

      lhs.setDefined();

      this.number = number;
      cost1 = cost2 = 0;
      cond = null;
      code = null;
      value = null;
      clobber = null;
      regsets = null;
      eqregs = 0;
      replaceto = null;
      phase = null;
      useAfterDef = false;

      if (rhs instanceof OpPattern)
        ((OpPattern)rhs).setLhs(lhs);
      rhs.setUsed();
    }

    Prod(String kind, int number, Nonterm lhs, Pattern rhs, ImList tail)
      throws SyntaxError {
      this.kind = kind;
      this.lhs = lhs;
      this.rhs = rhs;
      this.number = number;

      lhs.setDefined();

      int cost1 = 0, cost2 = 0;
      String cond = null;
      ImList code = null, value = null, clobber = null, phase = null;
      boolean useAfterDef = false;
      ImList replaceto = null;

      Nonterm[] rnts = rhs.realSubgoals();
      String[] regsets = new String[1 + rnts.length];
      regsets[0] = lhs.defaultRegset;
      for (int i = 0; i < rnts.length; i++)
        regsets[1 + i] = rnts[i].defaultRegset;
      long eqregs = 0;

      for (; !tail.atEnd(); tail = tail.next()) {
        if (tail.elem() instanceof ImList) {
          ImList p = (ImList)tail.elem();
          if (p.elem() == "cost" && kind == "rule") {
            cost1 = Integer.parseInt((String)p.elem2nd());
            if (p.length() >= 3)
              cost2 = Integer.parseInt((String)p.elem3rd());
            else
              cost2 = cost1;
          }
          else if (p.elem() == "cond") {
            if (p.elem2nd() instanceof QuotedString)
              cond = ((QuotedString)p.elem2nd()).body;
            else
              cond = (String)p.elem2nd();
          }
          else if (p.elem() == "code" && kind == "rule")
            code = p.next();
          else if (p.elem() == "value" && kind == "rule")
            value = p.next();
          else if (p.elem() == "clobber" && kind == "rule")
            clobber = p.next();
          else if (p.elem() == "regset" && kind == "rule") {
            for (p = p.next(); !p.atEnd(); p = p.next()) {
              ImList form = (ImList)p.elem();
              String dollar = (String)form.elem();
              int n = parseDollar(dollar);
              if (n > rnts.length)
                throw new SyntaxError(dollar + ": no such nonterm");
              regsets[n] = (String)form.elem2nd();
            }
          }
          else if (p.elem() == "use-after-def") {
            useAfterDef = true;
          }
          else if (p.elem() == "eqreg" && kind == "rule") {
            p = p.next();
            if (p.elem() instanceof ImList) {
              for (; !p.atEnd(); p = p.next())
                eqregs = doEqreg((ImList)p.elem(), eqregs, rnts);
            } else
              eqregs = doEqreg(p, eqregs, rnts);
          }
          else if (p.elem() == "to" && kind != "rule")
            replaceto = p.next();
          else if (p.elem() == "phase" && kind != "rule")
            phase = p.next();
          else
            throw new SyntaxError("unknown rule attribute: " + p.elem());
        }
        else
          throw new SyntaxError("unknown rule attribute: " + tail.elem());
      }

      checkDollar(code, rnts.length);
      checkDollar(value, rnts.length);

      this.cost1 = cost1;
      this.cost2 = cost2;
      this.cond = cond;
      this.code = code;
      this.value = value;
      this.clobber = clobber;
      this.regsets = regsets;
      this.eqregs = eqregs;
      this.replaceto = replaceto;
      this.phase = phase;
      this.useAfterDef = useAfterDef;

      rhs.setUsed();
    }

    private void checkDollar(ImList list, int max) throws SyntaxError {
      if (list == null)
        return;

      for (ImList p = list; !p.atEnd(); p = p.next()) {
        if (p.elem() instanceof String) {
          String s = (String)p.elem();
          if (s.charAt(0) == '$'
              && (s.charAt(1) != '$' && s.charAt(1) != 'L')) {
            if (Integer.parseInt(s.substring(1)) > max)
              throw new SyntaxError(s + ": no such nonterm");
          }
        } else if (p.elem() instanceof ImList)
          checkDollar((ImList)p.elem(), max);
      }
    }

    private long doEqreg(ImList form, long eqregs, Nonterm[] rnts)
      throws SyntaxError {
      if (form.length() != 2)
        throw new SyntaxError("eqreg: expecting pair");
      String to = (String)form.elem();
      int n = parseDollar(to);
      if (n > rnts.length)
        throw new SyntaxError(to + ": no such nonterm");
      if (n >= 64)
        throw new SyntaxError(to + ": too many leaves");
      eqregs |= 1 << n;
      String from = (String)form.elem2nd();
      if (parseDollar(from) != 0)
        throw new SyntaxError(from + ": must be $0");
      return eqregs;
    }

    boolean hasDelaySlot() {
      if (code != null) {
        for (ImList p = code; !p.atEnd(); p = p.next()) {
          if (p.elem() instanceof ImList) {
            ImList op = (ImList)p.elem();
            if (op.elem() == "delayslot")
              return true;
          }
        }
      }
      return false;
    }


    String expandCond(String basevar) throws SyntaxError {
      return expandJava(cond, basevar);
    }

    String expandJava(String template, String basevar)
      throws SyntaxError {
      StringBuffer buf = new StringBuffer();
      for (int i = 0; i < template.length(); i++) {
        if (template.charAt(i) == '$') {
          i++;
          int nth = template.charAt(i) - '0';
          if (nth == 0)
            buf.append(basevar);
          else
            buf.append(rhs.getNth(basevar, nth));
        } else
          buf.append(template.charAt(i));
      }
      return buf.toString();
    }


    void genRewriteCode(String basevar, String indent)
      throws SyntaxError {
      try {
        if (replaceto == null || replaceto.atEnd())
          out.println(" return null;");
        else {
          out.println(" {");
          out.println(indent + "  rewritten = true;");
          String subindent = indent + "  ";
          ImList p = replaceto;
          for (; !p.next().atEnd(); p = p.next())
            genRewriteStmt(p.elem(), basevar, subindent);
          out.println(subindent + "return "
                      + genRewriteExpr(p.elem(), basevar) + ";");
          out.println(indent + "}");
        }
      } catch (SyntaxError e) {
        throw new SyntaxError
          (e.getMessage() + " at: (to " +  replaceto + ")");
      }
    }


    void genRewriteStmt(Object form, String basevar, String indent)
      throws SyntaxError {

      if (form instanceof QuotedString) {
        // Java statement
        out.println(indent + expandJava(((QuotedString)form).body, basevar) + ";");
      }
      else if (form instanceof ImList) {
        ImList list = (ImList)form;
        if (list.atEnd())
          throw new SyntaxError("empty list");

        if (list.elem() == "let") {
          for (ImList p = list.next(); !p.atEnd(); p = p.next()) {
            if (!(p.elem() instanceof ImList))
              throw new SyntaxError("missing var-value pair at let");
            ImList v = (ImList)p.elem();
            out.println(indent + "LirNode " + v.elem() + " = "
                        + genRewriteExpr(v.elem2nd(), basevar) + ";");
          }
        }
        else if (list.elem() == "set!") {
          if (list.length() != 3)
            throw new SyntaxError("bad set!");
          out.println(indent + list.elem2nd() + " = "
                      + genRewriteExpr(list.elem3rd(), basevar) + ";");
        }
        else if (list.elem() == "pre") {
          out.println(indent + "{");
          for (ImList p = list.next(); !p.atEnd(); p = p.next()) {
            out.println(indent + "pre.add("
                        + genRewriteExpr(p.elem(), basevar) + ");");
          }
          out.println(indent + "}");
        }
        else if (list.elem() == "post") {
          out.println(indent + "{");
          for (ImList p = list.next(); !p.atEnd(); p = p.next()) {
            out.println(indent + "post.add("
                        + genRewriteExpr(p.elem(), basevar) + ");");
          }
          out.println(indent + "}");
        }
        else if (list.elem() == "if") {
          out.println(indent + "if ("
                      + genRewriteExpr(list.elem2nd(), basevar) + ")");
          if (list.length() == 3) {
            genRewriteStmt(list.elem3rd(), basevar, indent + "  ");
          }
          else if (list.length() == 4) {
            genRewriteStmt(list.elem3rd(), basevar, indent + "  ");
            out.println(indent + "else");
            genRewriteStmt(list.elem4th(), basevar, indent + "  ");
          }
          else
            throw new SyntaxError("bad if");
        }
        else if (list.elem() == "prog") {
          out.println(indent + "{");
          for (ImList p = list.next(); !p.atEnd(); p = p.next())
            genRewriteStmt(p.elem(), basevar, indent + "  ");
          out.println(indent + "}");
        }
        else if (list.elem() == "eval") {
          out.println(indent + expandJava(((QuotedString)list.elem2nd()).body, basevar) + ";");
        }
        else
          throw new SyntaxError("unknown: " + list.elem());
      }
    }


    String genRewriteExpr(Object form, String basevar)
      throws SyntaxError {
      if (form instanceof ImList)
        return genReplaceList((ImList)form, basevar);
      else if (form instanceof String)
        return genReplaceString((String)form, basevar);
      else if (form instanceof QuotedString)
        return genReplaceString(((QuotedString)form).body, basevar);
      else
        throw new SyntaxError("Neither string nor list");
    }

    String genReplaceList(ImList p, String basevar)
      throws SyntaxError {
      String mnemonic = (String)p.elem();
      if (mnemonic == "eval")
        return expandJava(((QuotedString)p.elem2nd()).body, basevar);
      if (mnemonic == "norescan")
        return "noRescan(" + genRewriteExpr(p.elem2nd(), basevar) + ")";

      int op = Op.toCode(mnemonic);
      if (op < 0)
        throw new SyntaxError("Unknown opcode: " + mnemonic);
      p = p.next();
      int type = Type.UNKNOWN;
      if (Op.isTyped(op)) {
        type = Type.decode((String)p.elem());
        p = p.next();
      }
      switch (op) {
      case Op.INTCONST:
        return "lir.iconst(" + type + ", " + genRewriteExpr(p.elem(), basevar) + ")";
      case Op.FLOATCONST:
        return "lir.fconst(" + type + ", " + genRewriteExpr(p.elem(), basevar) + ")";
      case Op.STATIC:
      case Op.FRAME:
      case Op.REG:
        return "lir.node(Op." + mnemonic + ", " + type + ", "
          + genReplaceSym(p.elem(), basevar) + ")";
      case Op.LABEL:
        return "lir.node(Op." + mnemonic + ", " + type + ", "
          + genReplaceLabel(p.elem(), basevar) + ")";

      default:
        {
          StringBuffer buf = new StringBuffer();
          buf.append("lir.node(Op." + mnemonic + ", " + type);
          for (; !p.atEnd(); p = p.next())
            buf.append(", " + genRewriteExpr(p.elem(), basevar));
          buf.append(")");
          return buf.toString();
        }
      }
    }

    String genReplaceSym(Object form, String basevar)
      throws SyntaxError {
      if (form instanceof QuotedString)
        return "func.getSymbol(\"" + ((QuotedString)form).body + "\")";
      else
        return genRewriteExpr(form, basevar);
    }

    String genReplaceLabel(Object form, String basevar)
      throws SyntaxError {
      if (form instanceof QuotedString)
        return "func.internLabel(\"" + ((QuotedString)form).body + "\")";
      else
        return genRewriteExpr(form, basevar);
    }


    String genReplaceString(String str, String basevar)
      throws SyntaxError {
      if (str.charAt(0) == '$') {
        int nth = Integer.parseInt(str.substring(1));
        return rhs.getNth(basevar, nth);
      } else {
        return str;
      }
    }



    public String toString() {
      return number + ": " + lhs.toString() + " -> " + rhs.toString();
    }

    public int compareTo(Object o) {
      Prod x = (Prod)o;
      int diff = 0;
      if (this.rhs instanceof Nonterm) {
        if (!(x.rhs instanceof Nonterm))
          return -1;
        diff = ((Nonterm)this.rhs).value - ((Nonterm)x.rhs).value;
        if (diff != 0)
          return diff;
      } else {
        if (x.rhs instanceof Nonterm)
          return 1;
        diff = ((OpPattern)this.rhs).op - ((OpPattern)x.rhs).op;
        if (diff != 0)
          return diff;
        diff = ((OpPattern)this.rhs).type - ((OpPattern)x.rhs).type;
        if (diff != 0)
          return diff;
      }
      return this.number - x.number;
    }
  }



  /** Set of rules (defrule or defrewrite/defpattern) **/
  static class RuleSet {
    private Map nontermTable = new HashMap();
    BiList nonterms = new BiList();

    private Map patternTable = new HashMap();
    BiList patterns = new BiList();

    BiList productions = new BiList();

    /** Next number assigned to nonterm. **/
    private int nextNonterm = 0;

    /** Next number for GENSYMed nonterm name. **/
    private int nextGen = 1;

    /** Next number assigned to production. **/
    private int nextProd = 1;

    /** Mode indicates this ruleset's purpose (rewriting or instSel). **/
    private String mode;

    RuleSet(String mode) {
      this.mode = mode;
      Nonterm dontcare = nonterm("_");
      dontcare.setUsed();
      dontcare.setDefined();
    }

    /** Return number of nonterminals plus 1. **/
    int nnonterms() { return nextNonterm; }

    /** Start symbol **/
    private Nonterm startSym;

    /** Return start symbol. **/
    Nonterm startSym() { return startSym; }

    /** Set start symbol. **/
    void setStartSym(String sym) {
      startSym = nonterm(sym);
    }


    /** Intern Nonterm instance in this ruleset. **/
    Nonterm nonterm(String name) {
      if (name == null)
        name = "_" + nextGen++;
      Nonterm nt = (Nonterm)nontermTable.get(name);
      if (nt == null) {
        nt = new Nonterm(name, nextNonterm++);
        nontermTable.put(name, nt);
        nonterms.add(nt);
      }
      return nt;
    }

    /** Intern OpPattern instance in this ruleset. **/
    OpPattern pattern(int op, int type, Nonterm[] kids) {
      return internOpPattern(new OpPattern(op, type, kids));
    }

    /** Intern OpPattern instance in this ruleset. **/
    OpPattern pattern(int op, int type, Object value) {
      return internOpPattern(new OpPattern(op, type, value));
    }

    /** Intern OpPattern instance in this ruleset. **/
    OpPattern internOpPattern(OpPattern pat) {
      OpPattern p = (OpPattern)patternTable.get(pat);
      if (p != null)
        return p;
      patternTable.put(pat, pat);
      patterns.add(pat);
      return pat;
    }

    /** Add new production rule.
     ** @param rhs rule's right hand side pattern.
     ** @return instance of Prod just created. **/
    Prod addProd(OpPattern rhs) {
      Nonterm lhs = nonterm(null);
      Prod prod = new Prod(nextProd++, lhs, rhs);
      lhs.setRhs(rhs);
      productions.add(prod);
      return prod;
    }


    /** Add new production rule.
     ** @param kind one of "rule", "rewrite" or "pattern"
     ** @param lhs rule's left hand side.
     ** @param rhs rule's right hand side.
     ** @param tail rule's attributes.
     ** @return instance of Prod just created. **/
    Prod addProd(String kind, String lhs, Object rhs, ImList tail)
      throws SyntaxError {
      Nonterm left = nonterm(lhs);
      Pattern right;
      if (rhs instanceof String)
        right = nonterm((String)rhs);
      else
        right = parsePattern(rhs);
      Prod prod = new Prod(kind, nextProd++, left, right, tail);
      productions.add(prod);
      return prod;
    }



    /** Parse Right Hand Side of the production. */
    Pattern parsePattern(Object obj)
      throws SyntaxError {
      if (obj instanceof String) {
        if (isNumber((String)obj))
          return parsePattern(ImList.list("INTCONST", "_", obj));
        else
          return nonterm((String)obj);
      }
      else if (obj instanceof ImList) {
        ImList p = (ImList)obj;

        int op = Op.toCode((String)p.elem());
        if (op < 0)
          throw new SyntaxError("Unknown opcode: " + (String)p.elem());
        p = p.next();
        int type = ANYTYPE;
        if (Op.isTyped(op) && !p.atEnd()) {
          String typestr = (String)p.elem();
          if (typestr != "_")
            type = Type.decode(typestr);
          p = p.next();
        }

        Object value = null;
        switch (op) {
        case Op.INTCONST:
          if (!p.atEnd())
            value = new Long((String)p.elem());
          return pattern(op, type, value);

        case Op.FLOATCONST:
          if (!p.atEnd())
            value = new Double((String)p.elem());
          return pattern(op, type, value);

        case Op.STATIC:
        case Op.REG:
        case Op.FRAME:
          if (!p.atEnd())
            value = (QuotedString)p.elem();
          return pattern(op, type, value);

        default:
          Nonterm[] kids = new Nonterm[p.length()];
          int i = 0;
          for (; !p.atEnd(); p = p.next()) {
            Pattern kid = parsePattern(p.elem());
            Nonterm nt = kid.lhs();
            if (nt == null)
              nt = addProd((OpPattern)kid).lhs;
            kids[i++] = nt;
          }
          return pattern(op, type, kids);
        }
      }
      else
        throw new SyntaxError("Neither Symbol nor Cons");
    }


    /** Check grammar **/
    void checkGrammar() {
      for (BiLink p = nonterms.first(); !p.atEnd(); p = p.next()) {
        Nonterm symbol = (Nonterm)p.elem();
        if (!symbol.defined)
          System.err.println("** Warning: nonterminal " + symbol.name + " not defined");
        if (!symbol.used)
          System.err.println("** Warning: nonterminal " + symbol.name + " not used");
      }
    }

    private Prod[] sortedRules;

    /** Prepare for generation. **/
    void prepare() {
      // Sort tables for later use.
      sortedRules = new Prod[productions.length()];
      int j = 0;
      for (BiLink p = productions.first(); !p.atEnd(); p = p.next()) {
        Prod rule = (Prod)p.elem();
        sortedRules[j++] = rule;
      }
      Arrays.sort(sortedRules);
    }


    /** Print productions **/
    void printProductions(PrintWriter out) {
      out.println("/*");
      out.println("Productions:");
      for (BiLink p = productions.first(); !p.atEnd(); p = p.next()) {
        Prod rule = (Prod)p.elem();
        out.println(" " + rule.toString());
      }
      out.println("*/");
      if (true) {
        out.println("/*");
        out.println("Sorted Productions:");
        for (int i = 0; i < sortedRules.length; i++)
          out.println(" " + sortedRules[i]);
        out.println("*/");
      }
    }


    /** Generate parameters. **/
    void genParameters(PrintWriter out, String indent) {
      out.println(indent + "static final int NNONTERM = "
                  + nnonterms() + ";");
      out.println(indent + "static final int NRULES = "
                  + sortedRules.length + " + 1;");
      out.println(indent + "static final int START_NT = "
                  + startSym().value + ";");
      out.println();
      for (BiLink p = nonterms.first(); !p.atEnd(); p = p.next()) {
        Nonterm nt = (Nonterm)p.elem();
        out.println(indent + "static final int NT_" + nt.name + " = "
                    + nt.value + ";");
      }
      out.println();
      out.println(indent + "String nontermName(int nt) {");
      out.println(indent + "  switch (nt) {");
      for (BiLink p = nonterms.first(); !p.atEnd(); p = p.next()) {
        Nonterm nt = (Nonterm)p.elem();
        out.println(indent + "  case NT_" + nt.name + ": return \"" + nt.name + "\";");
      }
      out.println(indent + "  default: return null;");
      out.println(indent + "  }");
      out.println(indent + "};");
      
      /*
      out.println(indent + "static final String[] nontermNamev = {");
      for (BiLink p = nonterms.first(); !p.atEnd(); p = p.next()) {
        Nonterm nt = (Nonterm)p.elem();
        out.println(indent + "  \"" + nt.name + "\",");
      }
      out.println(indent + "};");
      */

      /*
      out.println();
      out.println(indent + "static final String[] productions = {");
      for (BiLink p = productions.first(); !p.atEnd(); p = p.next()) {
        Prod rule = (Prod)p.elem();
        out.println(indent + "  \"" + rule + "\",");
      }
      out.println(indent + "};");
      */
    }


    static final int INIT_CHUNK = 100;

    private static void initOne(String indent, Prod prod) {
      out.print(indent + "rulev[" + prod.number + "] = new Rule("
                + prod.number + ", "
                + (prod.rhs instanceof Nonterm ? "true, " : "false, ")
                + (prod.kind == "derived" ? "true, " : "false, ")
                + prod.lhs.value + ", "
                + quote(prod.toString()) + ", "
                + listString(prod.code) + ", "
                + listString(prod.value) + ", "
                + listString(prod.clobber) + ", "
                + prod.eqregs + ", "
                + prod.useAfterDef + ", "
                + prod.hasDelaySlot() + ", "
                + "new int[]{");
      if (prod.rhs instanceof OpPattern) {
        OpPattern pat = (OpPattern)prod.rhs;
        for (int k = 0; k < pat.kids.length; k++) {
          if (k != 0) out.print(",");
          out.print(pat.kids[k].value);
        }
      } else {
        out.print(((Nonterm)prod.rhs).value);
      }
      out.print("}, ");
      if (prod.regsets == null) {
        out.print("null");
      } else {
        out.print("new String[]{");
        for (int k = 0; k < prod.regsets.length; k++) {
          if (k != 0) out.print(", ");
          out.print(quote(prod.regsets[k]));
        }
        out.print("}");
      }
      out.println(");");
    }

    void genRuleTable(PrintWriter out, String indent, int pass) {
      switch (pass) {
      case 0:
        for (int i = 0; i < sortedRules.length; i++)
          initOne(indent, sortedRules[i]);
        break;
      case 1:
        for (int i = 0; i < sortedRules.length; i += INIT_CHUNK)
          out.println(indent + "rrinit" + i + "();");
        break;
      case 2:
        for (int i = 0; i < sortedRules.length; ) {
          out.println(indent + "static private void rrinit" + i + "() {");
          for (int j = 0; j < INIT_CHUNK && i < sortedRules.length; j++, i++)
            initOne(indent + "  ", sortedRules[i]);
          out.println(indent + "}");
        }
        break;
      }
    }


    void genChainRuleAction(PrintWriter out, String indent)
      throws SyntaxError {
      int prent = -1;
      for (int i = 0; i < sortedRules.length; i++) {
        if (!(sortedRules[i].rhs instanceof Nonterm))
          break;
        Nonterm rhs = (Nonterm)sortedRules[i].rhs;
        if (rhs.value != prent) {
          if (prent >= 0)
            out.println(indent + "  break;");
          prent = rhs.value;
          out.println(indent + "case NT_" + rhs.name + ":");
        }
        // generate condition
        if (sortedRules[i].cond != null)
          out.print(indent + "  if (" + sortedRules[i].expandCond("t") + ") ");
        else
          out.print(indent + "  ");
        if (mode == "rewriting")
          out.println("record(NT_" + sortedRules[i].lhs.name + ", "
                      + sortedRules[i].number + ");");
        else
          out.println("record(NT_" + sortedRules[i].lhs.name + ", "
                      + sortedRules[i].cost1 + " + cost1, "
                      + sortedRules[i].cost2 + " + cost2, "
                      + sortedRules[i].number + ");");
      }
      if (prent >= 0)
        out.println(indent + "  break;");
    }


    static final int TOO_MANY_RULES_THRESH = 360;

    void genRuleAction(PrintWriter out, String indent, int pass)
      throws SyntaxError {

      // if rule set is small, do not separate switch.
      if (sortedRules.length < TOO_MANY_RULES_THRESH) {
        if (pass == 2)
          return;
        pass = 0;
      }

      boolean genSubroutine = false;
      for (int i = 0; i < sortedRules.length; ) {
        if (!(sortedRules[i].rhs instanceof OpPattern)) {
          i++;
          continue;
        }
        OpPattern rhs = (OpPattern)sortedRules[i].rhs;
        int op = rhs.op;
        if (pass == 2)
          out.println(indent + "private void rract" + op + "(LirNode t, State kids[]) {");
        else
          out.println(indent + "case Op." + Op.toName(rhs.op) + ":");
        if (pass == 1)
          out.println(indent + "  rract" + op + "(t, kids);");
        boolean reached = true;
        int j = i;
        while (i < sortedRules.length
               && ((OpPattern)sortedRules[i].rhs).op == op) {
          if (pass == 1) {
            i++;
            continue;
          }
          rhs = (OpPattern)sortedRules[i].rhs;
          int type = rhs.type;
          String subindent = indent;
          if (type >= 0) {
            out.println(indent + "  if (t.type == " + rhs.type + ") {");
            subindent = indent + "  ";
          }
          int ii = i;
          for (; ii < sortedRules.length
                 && ((OpPattern)sortedRules[ii].rhs).type == type
                 && ((OpPattern)sortedRules[ii].rhs).op == op; ii++)
            ;
          if (pass == 2 && ii - i > 100) {
            genSubroutine = true;
            for (; i < ii; i+=100)
              out.println(subindent + "  rract" + op + "_" + i + "(t, kids);");
            i = ii;
          } else {
            for (; i < ii; i++)
              reached = genForPattern(sortedRules[i], subindent);
          }
          if (type >= 0) {
            out.println(indent + "  }");
            reached = true;
          }
        }
        if (pass == 2)
          out.println(indent + "}");
        else {
          if (reached)
            out.println(indent + "  break;");
        }

        if (genSubroutine) {
          i = j;
          while (i < sortedRules.length
                 && ((OpPattern)sortedRules[i].rhs).op == op) {
            rhs = (OpPattern)sortedRules[i].rhs;
            int type = rhs.type;
            int ii = i;
            for (; ii < sortedRules.length
                   && ((OpPattern)sortedRules[ii].rhs).type == type
                   && ((OpPattern)sortedRules[ii].rhs).op == op; ii++)
              ;
            if (ii - i > 100) {
              while (i < ii) {
                out.println(indent + "private void rract" + op + "_" + i
                            + "(LirNode t, State kids[]) {");
                for (int k = 0; k < 100 && i < ii; k++, i++)
                  genForPattern(sortedRules[i], indent + "  ");
                out.println(indent + "}");
              }
            } else {
              i = ii;
            }
          }
        }
      }

    }


    boolean genForPattern(Prod rule, String indent)
      throws SyntaxError {
      boolean ifexist = false;
      OpPattern rhs = (OpPattern)rule.rhs;
      out.print(indent + "  ");
      if (isVarOp(rhs.op)) {
        out.print("if (kids.length == " + rhs.kids.length + ") ");
        ifexist = true;
      }
      for (int k = 0; k < rhs.kids.length; k++) {
        if (rhs.kids[k].name != "_") {
          out.print("if (kids[" + k + "].rule[NT_" + rhs.kids[k].name
                    + "] != 0) ");
          ifexist = true;
        }
      }

      if (rhs.value != null) {
        switch (rhs.op) {
        case Op.INTCONST:
          out.print("if (((LirIconst)t).value == " + rhs.value + ") ");
          ifexist = true;
          break;
        case Op.FLOATCONST:
          out.print("if (((LirFconst)t).value == " + rhs.value + ") ");
          ifexist = true;
          break;
        case Op.STATIC:
        case Op.FRAME:
        case Op.REG:
          out.print("if (((LirSymRef)t).symbol.name == " + rhs.value + ") ");
          ifexist = true;
          break;
        default:
          throw new Error("constant field in " + Op.toName(rhs.op));
        }
      }

      // phase check
      if (rule.phase != null) {
        out.print("if (");
        boolean first = true;
        for (ImList p = rule.phase; !p.atEnd(); p = p.next()) {
          String name = (String)p.elem();
          if (!first) out.print(" || ");
          out.print("phase == \"" + name + "\"");
          first = false;
        }
        if (first) out.print("false");
        out.print(") ");
        ifexist = true;
      }

      // generate condition
      if (rule.cond != null) {
        out.print("if (" + rule.expandCond("t") + ") ");
        ifexist = true;
      }            
      if (mode == "rewriting") {
        if (rule.lhs.name == "_rewr") {
          rule.genRewriteCode("t", indent + "  ");
          if (!ifexist)
            return false;
        } else {
          out.print("record(NT_" + rule.lhs.name);
          out.println(", " + rule.number + ");");
        }
      } else {
        out.print("record(NT_" + rule.lhs.name + ", "
                  + rule.cost1);
        for (int k = 0; k < rhs.kids.length; k++)
          out.print(" + kids[" + k + "].cost1[NT_" + rhs.kids[k].name + "]");
        out.print(", " + rule.cost2);
        for (int k = 0; k < rhs.kids.length; k++)
          out.print(" + kids[" + k + "].cost2[NT_" + rhs.kids[k].name + "]");
        out.println(", " + rule.number + ");");
      }
      return true;
    }

  }




  /** Return true if s is a number string. **/
  static boolean isNumber(String s) {
    int n = s.length();
    int k = 0;
    if (k < n && (s.charAt(0) == '-' || s.charAt(0) == '+'))
      k = 1;
    int i = k;
    for (; i < n; i++) {
      if (!Character.isDigit(s.charAt(i)))
        return false;
    }
    return i > k;
  }




  /** Parse defrule. **/
  static void doDefRule(ImList form)
    throws SyntaxError {
    instSel.addProd("rule", (String)form.elem2nd(), form.elem3rd(), form.next3rd());
  }


  /** Parse defrewrite. **/
  static void doDefRewrite(ImList form)
    throws SyntaxError {
    rewriting.addProd("rewrite", "_rewr", form.elem2nd(), form.next2nd());
  }


  /** Parse defpattern. **/
  static void doDefPattern(ImList form)
    throws SyntaxError {
    rewriting.addProd("pattern", (String)form.elem2nd(), form.elem3rd(), form.next3rd());
  }


  static BiList javaMacros = new BiList();
  static int emCounter = 0;

  static class JavaMacro {
    String kind;
    String name;
    ImList args;
    int number;
    int opCode;

    /** Create Macro instance. **/
    JavaMacro(String kind, String name, ImList args) {
      this.kind = kind;
      this.name = name;
      this.args = args;
      number = ++emCounter;

      opCode = Op.toCode(name);
    }

    /** Return true if this macro is for LirNode. **/
    boolean forLirNode() {
      return opCode >= 0;
    }

    /** Return java method header. **/
    String methodHeader() {
      StringBuffer buf = new StringBuffer();
      buf.append((kind == DEFEMIT ? "String" : "Object") + " jmac" + number);
      buf.append("(");
      if (forLirNode())
        buf.append("LirNode " + args.elem());
      else {
        for (ImList p = args; !p.atEnd(); p = p.next()) {
          String arg = (String)p.elem();
          if (arg.charAt(0) == '=') {
            // argument are not evaluated
            buf.append("Object ");
            buf.append(arg.substring(1));
          } else {
            if (kind == DEFEMIT)
              buf.append("String ");
            else
              buf.append("Object ");
            buf.append(arg);
          }
          if (!p.next().atEnd())
            buf.append(", ");
        }
      }
      buf.append(")");
      return buf.toString();
    }

    /** Return invokation code. **/
    String invokeCode(String listvar, String flag) {
      StringBuffer buf = new StringBuffer();
      buf.append("jmac" + number);
      buf.append("(");
      if (forLirNode())
        buf.append(listvar);
      else {
        int num = 1;
        for (ImList p = args; !p.atEnd(); p = p.next()) {
          String arg = (String)p.elem();
          if (num > 1) buf.append(", ");
          if (arg.charAt(0) == '=') {
            // argument are not evaluated
            buf.append(listvar + ".elem(" + num + ")");
          } else {
            if (kind == DEFEMIT)
              buf.append("emitObject(" + listvar + ".elem(" + num + "))");
            else
              buf.append(listvar + ".elem(" + num + ")");
          }
          num++;
        }
      }
      buf.append(")");
      return buf.toString();
    }
  }

  /** expand %defemit(name args...) or %defbuild(name args...) **/
  static String expandDefMacro(String kind, PushbackReader rdr)
    throws SyntaxError, IOException {
    Object obj = ImList.readSexp(rdr);
    if (!(obj instanceof ImList))
      throw new SyntaxError("missing () after %" + kind);
    ImList form = (ImList)obj;
    String name = (String)form.elem();
    JavaMacro em = new JavaMacro(kind, name, form.next());
    javaMacros.add(em);
    return em.methodHeader();
  }


  /** Read entire file as a list. **/
  static ImList readSexpList(PushbackReader rdr, ImList tail)
    throws IOException, SyntaxError {
    if (tail == null)
      tail = ImList.Empty;
    ImList list = ImList.Empty;
    Object obj;
    while ((obj = ImList.readSexp(rdr)) != null && obj != "%%")
      list = new ImList(obj, list);
    return list.destructiveReverse(tail);
  }


  static Object replaceSubstr(Object x, String fv, Object val)
    throws SyntaxError {
    if (fv.charAt(0) != '@')
      return x;

    if (x instanceof String) {
      String str = (String)x;
      int at = str.indexOf(fv);
      if (at < 0)
        return x;
      return (str.substring(0, at) + val.toString()
              + str.substring(at + fv.length())).intern();
    }
    else if (x instanceof QuotedString) {
      String str = ((QuotedString)x).body;
      int at = str.indexOf(fv);
      if (at < 0)
        return x;
      return new QuotedString(str.substring(0, at)
                              + val.toString()
                              + str.substring(at + fv.length()));
    }
    else
      return x;
  }


  static ImList replaceList(ImList body, Object fv, Object val, ImList tail)
    throws SyntaxError {
    if (fv instanceof String)
      return replaceList(body, ImList.list(fv), ImList.list(val), tail);
    else if (fv instanceof ImList) {
      if (((ImList)fv).length() != ((ImList)val).length())
        throw new SyntaxError("foreach parameter list unmatch");
    }
    else
      throw new SyntaxError("bad parameter list");
    return replaceListAux(body, (ImList)fv, (ImList)val, tail);
  }

  static ImList replaceListAux(ImList body, ImList fpl, ImList apl, ImList tail)
    throws SyntaxError {
    if (body.atEnd())
      return tail;

    tail = replaceListAux(body.next(), fpl, apl, tail);
    Object x = body.elem();

    if (x instanceof ImList)
      x = replaceListAux((ImList)x, fpl, apl, ImList.Empty);
    else {
      for (ImList p = fpl, q = apl; !p.atEnd(); p = p.next(), q = q.next()) {
        String fp = (String)p.elem();
        if (fp == x)
          return desugar(ImList.list(q.elem())).append(tail);
      }
      for (ImList p = fpl, q = apl; !p.atEnd(); p = p.next(), q = q.next()) {
        String fp = (String)p.elem();
        if (fp.charAt(0) == '@')
          x = replaceSubstr(x, fp, q.elem());
      }
    }
    return new ImList(x, tail);
  }


  static ImList replaceLoop(ImList body, Object cv, ImList vals)
    throws SyntaxError {
    if (vals.atEnd())
      return ImList.Empty;
    return replaceList(body, cv, vals.elem(),
                       replaceLoop(body, cv, vals.next()));
  }
    

  /** Expand foreach macro. **/
  static ImList expandForeach(ImList sexp)
    throws SyntaxError {
    try {
      if (sexp.elem() != "foreach")
        throw new Error("Can't happen");
      if (!(sexp.elem3rd() instanceof ImList))
        throw new SyntaxError("values not list:");
      return replaceLoop(sexp.next3rd(),
                         sexp.elem2nd(),
                         desugar((ImList)sexp.elem3rd()));
    } catch (SyntaxError e) {
      throw new SyntaxError(e.getMessage() + ": " +  sexp);
    }
  }


  /** Expand macro **/
  static ImList expandMacro(ImList form)
    throws SyntaxError {
    String name = (String)form.elem();
    ImList def = (ImList)macros.get(name);
    if (def == null) {
      throw new Error("?");
    }
    ImList formal = ((ImList)def.elem()).next();
    def = def.next();
    return replaceList(def, formal, form.next(), ImList.Empty);
  }


  /** Expand if **/
  static ImList expandIf(ImList form)
    throws SyntaxError {
    if (form.length() != 3 && form.length() != 4)
      throw new SyntaxError("Bad @if");
    if (evalCond(form.elem2nd()))
      return (ImList)form.elem3rd();
    else if (form.length() < 4)
      return ImList.Empty;
    else
      return (ImList)form.elem4th();
  }

  /** Evaluate condition part of @if **/
  static boolean evalCond(Object x) throws SyntaxError {
    ImList xx = desugar(ImList.list(x));
    if (xx.isEmpty())
      return false;
    if (xx.elem() == ImList.Empty)
      return false;
    if (xx.elem() == "nil")
      return false;
    return true;
  }

  static ImList expandEq(ImList form) throws SyntaxError {
    form = desugar(form);
    if (form.length() != 3)
      throw new SyntaxError("bad eq");
    return ImList.list(form.elem2nd().equals(form.elem3rd()) ? "t" : "nil");
  }

  static ImList expandNe(ImList form) throws SyntaxError {
    form = desugar(form);
    if (form.length() != 3)
      throw new SyntaxError("bad ne");
    return ImList.list(form.elem2nd().equals(form.elem3rd()) ? "nil" : "t");
  }

  static ImList expandDefined(ImList form) throws SyntaxError {
    form = desugar(form);
    if (form.length() != 2)
      throw new SyntaxError("bad defined");
    return ImList.list(isMacro(form.elem2nd())? "t" : "nil");
  }

  static ImList expandNot(ImList form) throws SyntaxError {
    if (form.length() != 2)
      throw new SyntaxError("bad not");
    return ImList.list(evalCond(form.elem2nd())? "nil" : "t");
  }

  

  static boolean isMacro(Object name) {
    return (macros.get(name) != null);
  }

  /** Register macro definition **/
  static void registerMacroDef(ImList form) throws SyntaxError {
    /* form is (defmacro (macname arg1 arg2...) body...) */
    Object o = form.elem2nd();
    if (o instanceof ImList) {
      ImList cform = (ImList)o;
      if (cform.elem() instanceof String) {
        macros.put((String)cform.elem(), form.next());
        return;
      }
    }
    throw new SyntaxError("bad formed defmacro: " + form);
  }


  /** Expand include file **/
  static ImList expandInclude(ImList form, ImList tail)
    throws SyntaxError {
    if (form.length() != 2)
      throw new SyntaxError("include: must have one and only one arg");
    if (!(form.elem2nd() instanceof QuotedString))
      throw new SyntaxError("include: expecting quoted string ");
    QuotedString file = (QuotedString)form.elem2nd();
    try {
      PushbackReader rdr = new PushbackReader(new FileReader(file.body));
      return readSexpList(rdr, tail);
    } catch (FileNotFoundException e) {
      throw new Error("include: file " + file + " not found");
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }


  /** Expand syntax sugar (foreach). **/
  static ImList desugar(ImList list)
    throws SyntaxError {
    ImList append = ImList.Empty;
    if (false) {
      if (list.atEnd())
        System.out.println("desugaring: ()");
      else
        System.out.println("desugaring: (" + list.elem() + "...");
    }

    while (!list.atEnd()) {
      Object obj = list.elem();
      list = list.next();
      if (obj instanceof ImList) {
        ImList form = (ImList)obj;
        if (form.elem() == "foreach")
          //          append = expandForeach(form).destructiveReverse(append);
          list = expandForeach(form).append(list);
        else if (form.elem() == "defmacro")
          registerMacroDef(form);
        else if (form.elem() == "include")
          list = expandInclude(form, list);
        else if (form.elem() == "@if")
          list = expandIf(form).append(list);
        else if (form.elem() == "@eq")
          list = expandEq(form).append(list);
        else if (form.elem() == "@ne")
          list = expandNe(form).append(list);
        else if (form.elem() == "@not")
          list = expandNot(form).append(list);
        else if (form.elem() == "@defined")
          list = expandDefined(form).append(list);
        else if (isMacro(form.elem()))
          list = expandMacro(form).append(list);
        else
          append = new ImList(desugar(form), append);
      }
      else {
        if (false)
          System.out.println("desugar atom: " + obj);
        append = new ImList(obj, append);
      }
    }
    return append.destructiveReverse();
  }


  static void doDef(ImList form) throws SyntaxError {
    if (form.elem2nd() instanceof String)
      definitions.put((String)form.elem2nd(), form.elem3rd());
    regDesc.doDef(form);
  }



  static void doDefStart(ImList form) throws SyntaxError {
    if (form.length() != 2 || !(form.elem2nd() instanceof String))
      throw new SyntaxError("Malformed defstart");
    instSel.setStartSym((String)form.elem2nd());
  }

  static BiList typeRegsetList = new BiList();

  static void doDefRegset(ImList form) throws SyntaxError {
    
    form = form.next();
    if (form.elem() instanceof ImList) {
      for (; !form.atEnd(); form = form.next())
        doDefRegsetPair((ImList)form.elem());
    } else
      doDefRegsetPair(form);
  }

  static void doDefRegsetPair(ImList pair) throws SyntaxError {
    if (pair.length() != 2)
      throw new SyntaxError("defregset: not a pair");

    Nonterm reg = instSel.nonterm((String)pair.elem());
    reg.setDefaultRegset((String)pair.elem2nd());
  }


  static void doDefRegsetVar(ImList form) throws SyntaxError {
    
    form = form.next();
    if (form.elem() instanceof ImList) {
      for (; !form.atEnd(); form = form.next())
        doDefRegsetVarPair((ImList)form.elem());
    } else
      doDefRegsetVarPair(form);
  }

  static void doDefRegsetVarPair(ImList pair) throws SyntaxError {
    if (pair.length() != 2)
      throw new SyntaxError("defregsetvar: not a pair");

    int type = Type.decode((String)pair.elem());
    typeRegsetList.add(ImList.list(new Integer(type), (String)pair.elem2nd()));
  }


  static boolean isVarOp(int op) {
    return op == Op.PARALLEL || op == Op.LIST;
  }


  /** Read line from pushback reader. **/
  static String readLine(PushbackReader rdr)
    throws IOException {
    StringBuffer buf = new StringBuffer();
    int ch;
    while ((ch = rdr.read()) >= 0) {
      if (ch == '\n')
        break;
      buf.append((char)ch);
    }
    return buf.toString();
  }


  /** Read whitespaces from pushback reader. **/
  static String readWhites(PushbackReader rdr)
    throws IOException {
    StringBuffer buf = new StringBuffer();
    int ch;
    while ((ch = rdr.read()) >= 0) {
      if (ch == '\n' || !Character.isWhitespace((char)ch)) {
        rdr.unread(ch);
        break;
      }
      buf.append((char)ch);
    }
    return buf.toString();
  }


  /** Read token from pushback reader. **/
  static String readToken(PushbackReader rdr)
    throws IOException {
    StringBuffer buf = new StringBuffer();
    int ch;
    while ((ch = rdr.read()) >= 0) {
      if (!Character.isLetterOrDigit((char)ch)) {
        rdr.unread(ch);
        break;
      }
      buf.append((char)ch);
    }
    return buf.toString();
  }

  static final String STATE = "State";
  static final String CODEGENERATOR = "CodeGenerator";
  static final String DEFEMIT = "defemit";
  static final String DEFBUILD = "defbuild";
  static final String IMPORT = "import";

  /** Convert TMD to Java. **/
  static void parseTmd(PushbackReader rdr)
    throws SyntaxError, IOException {

    // Read grammars.
    ImList list = desugar(readSexpList(rdr, null));

    if (printExpanded) {
      printBeautifully(list);
    }

    // Read Java part.
    ImList javaList = ImList.Empty;
    ImList imports = ImList.Empty;
    ImList cgMethods = ImList.Empty;
    ImList stMethods = ImList.Empty;

    String mode = IMPORT;
    for (;;) {
      String line = readWhites(rdr);
      int ch = rdr.read();
      if (ch < 0)
        break;

      if (ch != '%') {
        rdr.unread(ch);
        line = line + readLine(rdr);
        javaList = new ImList(line, javaList);
        continue;
      }

      // seen '%' at start of line.
      String word = readToken(rdr);
      if (word.equals(DEFEMIT)) {
        String head = expandDefMacro(DEFEMIT, rdr);
        line = line + head + readLine(rdr);
        javaList = new ImList(line, javaList);
        continue;
      }
      else if (word.equals(DEFBUILD)) {
        String head = expandDefMacro(DEFBUILD, rdr);
        line = line + head + readLine(rdr);
        javaList = new ImList(line, javaList);
        continue;
      }

      readLine(rdr); // ignore rest of line

      if (mode == IMPORT) 
        imports = javaList;
      else if (mode == STATE)
        stMethods = javaList;
      else if (mode == CODEGENERATOR)
        cgMethods = javaList;

      if (word.equals(STATE)) {
        mode = STATE;
        javaList = stMethods;
      }
      else if (word.equals(CODEGENERATOR)) {
        mode = CODEGENERATOR;
        javaList = cgMethods;
      }
      else
        throw new SyntaxError("Unknown %" + word);
    }

    if (mode == IMPORT)
      imports = javaList;
    else if (mode == STATE)
      stMethods = javaList;
    else if (mode == CODEGENERATOR)
      cgMethods = javaList;

    imports = imports.destructiveReverse();
    stMethods = stMethods.destructiveReverse();
    cgMethods = cgMethods.destructiveReverse();

    // Parse grammars
    for (; !list.atEnd(); list = list.next()) {
      Object obj = list.elem();

      try {
        if (obj instanceof ImList) {
          ImList form = (ImList)obj;
          if (form.elem() == "def")
            doDef(form);
          else if (defFileOnly)
            continue;
          else if (form.elem() == "defstart")
            doDefStart(form);
          else if (form.elem() == "defrule")
            doDefRule(form);
          else if (form.elem() == "defregset")
            doDefRegset(form);
          else if (form.elem() == "defregsetvar")
            doDefRegsetVar(form);
          else if (form.elem() == "defrewrite")
            doDefRewrite(form);
          else if (form.elem() == "defpattern")
            doDefPattern(form);
          else
            throw new SyntaxError("unknown: " + form);
        } else
          throw new SyntaxError("unknown: " + obj);
      } catch (SyntaxError e) {
        throw new SyntaxError(e.getMessage() + " at: " + obj);
      }
    }

    if (defFileOnly)
      return;

    if (instSel.startSym() == null)
      instSel.setStartSym("void");
    instSel.startSym().setUsed();
    instSel.checkGrammar();
    instSel.prepare();
    instSel.printProductions(out);

    rewriting.setStartSym("_rewr");
    rewriting.startSym().setUsed();
    rewriting.startSym().setDefined();
    rewriting.checkGrammar();
    rewriting.prepare();
    rewriting.printProductions(out);

    // Generate Java source
    String line;
    while ((line = proto.readLine()) != null) {
      int n = 0;
      for (; n < line.length(); n++) {
        if (!Character.isWhitespace(line.charAt(n)))
          break;
      }
      String indent = line.substring(0, n);
      String trim = line.substring(n);
      if (trim.startsWith("$decl")) {
        // Generate parameters
        if (trim.substring(5).startsWith("-rewrite"))
          rewriting.genParameters(out, indent);
        else
          instSel.genParameters(out, indent);
      }
      else if (trim.startsWith("$tables")) {
        // Generate Rule table
        instSel.genRuleTable(out, indent, getPass(trim, 7));
      }
      else if (trim.startsWith("$chains")) {
        // Generate chain action
        if (trim.substring(7).startsWith("-rewrite"))
          rewriting.genChainRuleAction(out, indent);
        else
          instSel.genChainRuleAction(out, indent);
      }
      else if (trim.startsWith("$rules")) {
        // Generate Rule Action
        if (trim.substring(6).startsWith("-rewrite"))
          rewriting.genRuleAction(out, indent, getPass(trim, 13));
        else
          instSel.genRuleAction(out, indent, getPass(trim, 6));
      }
      else if (trim.startsWith("$regsettype")) {
        // register-set and type table.
        for (BiLink q = typeRegsetList.first(); !q.atEnd(); q = q.next()) {
          ImList pair = (ImList)q.elem();
          out.println(indent + "case " + pair.elem()
                      + ": return " + "\"" + pair.elem2nd() + "\";");
        }
      }
      else if (trim.startsWith("$import")) {
        // copy imports
        for (ImList p = imports; !p.atEnd(); p = p.next())
          out.println(indent + (String)p.elem());
      }
      else if (trim.startsWith("$state")) {
        // copy State methods
        for (ImList p = stMethods; !p.atEnd(); p = p.next())
          out.println(indent + replaceDollars((String)p.elem()));
      }
      else if (trim.startsWith("$codegenerator")) {
        // copy CodeGenerator methods
        for (ImList p = cgMethods; !p.atEnd(); p = p.next())
          out.println(indent + replaceDollars((String)p.elem()));
      }
      else if (trim.startsWith("$buildmac")) {
        // generate defbuild dispatcher
        boolean first = true;
        for (BiLink p = javaMacros.first(); !p.atEnd(); p = p.next()) {
          JavaMacro em = (JavaMacro)p.elem();
          if (em.kind == DEFBUILD && !em.forLirNode()) {
            out.println(indent + (first ? "" : "else ")
                        + "if (name == \"" + em.name + "\")");
            out.println(indent + "  return " + em.invokeCode("form", null) + ";");
            first = false;
          }
        }
      }
      else if (trim.startsWith("$buildlir")) {
        // generate defbuild dispatcher
        for (BiLink p = javaMacros.first(); !p.atEnd(); p = p.next()) {
          JavaMacro em = (JavaMacro)p.elem();
          if (em.kind == DEFBUILD && em.forLirNode()) {
            out.println(indent + "case Op." + em.name + ":");
            out.println(indent + "  return " + em.invokeCode("node", null) + ";");
          }
        }
      }
      else if (trim.startsWith("$emitlist")) {
        // generate defemit dispatcher
        boolean first = true;
        for (BiLink p = javaMacros.first(); !p.atEnd(); p = p.next()) {
          JavaMacro em = (JavaMacro)p.elem();
          if (em.kind == DEFEMIT && !em.forLirNode()) {
            out.println(indent + (first ? "" : "else ")
                        + "if (name == \"" + em.name + "\")");
            out.println(indent + "  return " + em.invokeCode("form", null) + ";");
            first = false;
          }
        }
      }
      else if (trim.startsWith("$emitlir")) {
        // generate defemit dispatcher
        for (BiLink p = javaMacros.first(); !p.atEnd(); p = p.next()) {
          JavaMacro em = (JavaMacro)p.elem();
          if (em.kind == DEFEMIT && em.forLirNode()) {
            out.println(indent + "case Op." + em.name + ":");
            out.println(indent + "  return " + em.invokeCode("node", null) + ";");
          }
        }
      }
      else {
        out.println(replaceDollars(line));
      }
    }
    out.flush();
  }

  static private int getPass(String s, int n) {
    if (s.length() < n)
      return 0;
    switch (s.charAt(n)) {
    case '1': return 1;
    case '2': return 2;
    default:
      return 0;
    }
  }

  static String quote(String x) {
    if (x == null)
      return "null";

    StringBuffer buf = new StringBuffer();
    buf.append('\"');
    int n = x.length();
    for (int i = 0; i < n; i++) {
      if (x.charAt(i) == '\"')
        buf.append('\\');
      buf.append(x.charAt(i));
    }
    buf.append('\"');
    return buf.toString();
  }



  /** parse $1, $2, ... **/
  static int parseDollar(String x) throws SyntaxError {
    if (x.charAt(0) != '$')
      throw new SyntaxError(x + ": $n expected");
    return Integer.parseInt(x.substring(1));
  }



  static void listString1(StringBuffer buf, Object obj) {
    if (obj instanceof ImList) {
      ImList form = (ImList)obj;
      if (form.length() <= 5) {
        buf.append("ImList.list(");
        boolean first = true;
        for (ImList p = form; !p.atEnd(); p = p.next()) {
          if (!first) buf.append(",");
          listString1(buf, p.elem());
          first = false;
        }
        buf.append(")");
      } else {
        buf.append("new ImList(");
        listString1(buf, form.elem());
        buf.append(", ");
        listString1(buf, form.next());
        buf.append(")");
      }
    }
    else if (obj instanceof QuotedString)
      buf.append(obj.toString());
    else if (obj instanceof String)
      buf.append("\"" + obj + "\"");
    else if (obj instanceof Integer)
      buf.append("new Integer(" + obj + ")");
    else
      throw new Error("can't print list.");
  }
  

  static String listString(Object obj) {
    if (obj == null)
      return "null";

    StringBuffer buf = new StringBuffer();
    listString1(buf, obj);
    return buf.toString();
  }


  static String replaceDollars(String str) {
    StringBuffer buf = new StringBuffer();
    int ptr = 0, pos = 0;
    while ((pos = str.indexOf("$", ptr)) >= 0) {
      buf.append(str.substring(ptr, pos));
      if (str.substring(pos).startsWith("$target")) {
        ptr = pos + 7;
        buf.append(targetName);
      }
      else if (str.substring(pos).startsWith("$defined(")) {
        ptr = pos + 9;
        pos = str.indexOf(")", ptr);
        String name = str.substring(ptr, pos);
        Object value = definitions.get(name);
        if (value == null)
          value = macros.get(name);
        buf.append(value != null ? "true" : "false");
        ptr = pos + 1;
      }
      else if (str.substring(pos).startsWith("$def(")) {
        ptr = pos + 5;
        pos = str.indexOf(")", ptr);
        String name = str.substring(ptr, pos);
        Object value = definitions.get(name);
        if (value == null)
          System.err.println("Symbol `" + name + "' undefined");
        buf.append(listString(value));
        ptr = pos + 1;
      }
      else {
        ptr = pos + 1;
        buf.append("$");
      }
    }
    buf.append(str.substring(ptr));

    return buf.toString();
  }
      


  static String usage = "Usage: java Tmd2Java [-d][-p protofile][-t targetname][-x][-Dmacro[=value]] target.tmd";

  public static void main(String[] argv) {
    
    String protoFile = PROTOFILE;
    String packageName = "coins.backend.gen";

    int ap = 0;
    while (argv[ap].startsWith("-")) {
      if (argv[ap].equals("-p")) {
        ap++;
        protoFile = argv[ap];
        ap++;
      }
      else if (argv[ap].equals("-k")) {
        ap++;
        packageName = argv[ap];
        ap++;
      }
      else if (argv[ap].equals("-d")) {
        ap++;
        defFileOnly = true;
      }
      else if (argv[ap].equals("-t")) {
        ap++;
        targetName = argv[ap];
        ap++;
      }
      else if (argv[ap].equals("-x")) {
        ap++;
        printExpanded = true;
      }
      else if (argv[ap].startsWith("-D")) {
        String def;
        if (argv[ap].length() > 2) {
          def = argv[ap].substring(2);
        } else {
          ap++;
          if (ap >= argv.length)
            throw new Error(usage);
          def = argv[ap];
        }
        ap++;
        // parse macroname=value
        String name = def;
        ImList body = ImList.Empty;
        int at = def.indexOf("=");
        if (at >= 0) {
          name = def.substring(0, at);
          body = ImList.list(def.substring(at + 1));
        }
        macros.put(name, new ImList(ImList.list(name), body));
      }
      else
        throw new Error(usage);
    }
    if (argv.length != ap + 1)
      throw new Error(usage);

    if (!argv[ap].endsWith(".tmd"))
      throw new Error(".tmd expected");

    if (targetName == null)
      targetName = argv[ap].substring(0, argv[ap].indexOf(".tmd"));
    String cgfile = "CodeGenerator_" + targetName + ".java";
    String rdfile = "MachineParams_" + targetName + ".java";

    try {
      PushbackReader rdr = new PushbackReader(new FileReader(argv[ap]));
      if (!defFileOnly) {
        out = new PrintWriter(new FileOutputStream(cgfile));
        proto = new BufferedReader(new FileReader(protoFile));
      }

      regDesc = new RegisterDescription2Java(targetName, rdfile, packageName);

      parseTmd(rdr);

      regDesc.close();

    } catch (SyntaxError e) {
      throw new Error(e.getMessage());
    } catch (FileNotFoundException e) {
      throw new Error(e.getMessage());
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }
}
