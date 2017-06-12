/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sym;

import coins.backend.Module;
import coins.backend.SyntaxError;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/** Symbol table. */
public class SymTab {
  private Map table = new HashMap();
  private BiList list = new BiList();

  /** Next symbol's id */
  // private int idCounter = 1;

  private Module module;

  /** Create empty symbol table. */
  public SymTab(Module mod) { module = mod; }


  /** Clear symbol table. **/
  public void clear() {
    table.clear();
    list.clear();
  }


  /** Create empty symbol table with initial id setting. */
  // public SymTab(int id) { idCounter = id; }

  /** Register STATIC symbol entry. */
  public Symbol addSymbol(String name, int storage, int type, int boundary,
                          String segment, String linkage, ImList opt) {
    Symbol sym = new SymStatic(this, name, module.genSymbolId(),
                               storage, type, boundary, segment, linkage, opt);
    table.put(name, sym);
    list.add(sym);
    return sym;
  }

  /** Register FRAME/REG symbol entry. */
  public Symbol addSymbol(String name, int storage, int type,
                        int boundary, int offset, ImList opt) {
    Symbol sym = new SymAuto(this, name, module.genSymbolId(),
                             storage, type, boundary, offset, opt);
    table.put(name, sym);
    list.add(sym);
    return sym;
  }

  /** Register temporary symbol entry. */
  public Symbol addSymbol(Symbol original, int type) {
    Symbol sym = new SymTemp(this, original, module.genSymbolId(), type);
    table.put(sym.name, sym);
    list.add(sym);
    return sym;
  }

  /** Parse new symbol from list argument and register it **/
  public Symbol addSymbol(ImList arg) throws SyntaxError {
    Symbol sym = Symbol.parseSymbol(this, module.genSymbolId(), arg);
    table.put(sym.name, sym);
    list.add(sym);
    return sym;
  }

  /** Find a symbol with key <code>name</code>. */
  public Symbol get(String name) { return (Symbol)table.get(name.intern()); }

  /** Remove symbol entry **/
  public void remove(String name) {
    list.remove(get(name));
    table.remove(name);
  }

  /** Remove symbol entry **/
  public void remove(Symbol sym) {
    list.remove(sym);
    table.remove(sym.name);
  }

  /** Remove overridden entries from list. **/
  public void sanitize() {
    for (BiLink p = list.first(); !p.atEnd(); ) {
      Symbol sym = (Symbol)p.elem();
      BiLink next = p.next();
      if (get(sym.name) != sym)
        p.unlink();
      p = next;
    }
  }


  /** Return the list of symbol entries. */
  public BiList symbols() { return list; }

  /** Return an iterator for accessing symbol entries. */
  public Iterator iterator() { return list.iterator(); }


  /** Set up reverse index table. **/
  public void makeReverseIndex(Symbol[] vec) {
    for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
      Symbol sym = (Symbol)p.elem();
      vec[sym.id] = sym;
    }
  }

    
  /** Return max id of the symbol + 1. */
  public int idBound() { return module.symbolIdBound(); }


  /** Print symbol table in standard form */
  public void printStandardForm(PrintWriter out, String indent) {
    out.println(indent + "(SYMTAB");
    for (BiLink p = list.first(); !p.atEnd(); p = p.next())
      out.println(indent + "  " + ((Symbol)p.elem()).contents());
    out.println(indent + ")");
  }

  
  /** Convert to external LIR format. **/
  public Object toSexp() {
    ImList q = ImList.list("SYMTAB");
    for (BiLink p = list.first(); !p.atEnd(); p = p.next())
      q = new ImList(((Symbol)p.elem()).toSexp(), q);
    return q.destructiveReverse();
  }
    

  /** Dump symbol table */
  public void printIt(PrintWriter out) {
    out.println("Symbol table:");
    for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
      out.println("  " + ((Symbol)p.elem()).contents());
    }
  }

  /** Dump symbol table */
  public void printIt(PrintWriter out, boolean printReg) {
    out.println("Symbol table:");
    for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
      Symbol sym = (Symbol)p.elem();
      if (printReg || sym.name.charAt(0) != '%')
        out.println("  " + sym.contents());
    }
  }
}
