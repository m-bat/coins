/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sym;

import coins.backend.Storage;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** Symbol table entry */
public abstract class Symbol {
  public final SymTab table;
  public final String name;
  public final int id;
  public final int storage;
  public final int type;
  public final int boundary;
  ImList opt;

  Symbol(SymTab table, String name, int id, int storage, int type, int boundary,
         ImList opt) {
    this.table = table;
    this.name = name;
    this.id = id;
    this.storage = storage;
    this.type = type;
    this.boundary = boundary;
    this.opt = opt == null ? ImList.Empty : opt;
  }


  /** Create new symbol from listarg **/
  static Symbol parseSymbol(SymTab table, int id, ImList list)
    throws SyntaxError {

    String name = ((QuotedString)list.elem()).body;
    int tag = Storage.decode((String)list.elem2nd());
    int type = Type.decode((String)list.elem3rd());
    int boundary, offset;

    switch (tag) {
    case Storage.STATIC:
      boundary = Integer.parseInt((String)list.elem4th());
      return new SymStatic(table, name, id, tag, type, boundary,
                           ((QuotedString)list.elem5th()).body,
                           (String)list.elem6th(),
                           list.scanOpt());

    case Storage.FRAME:
      boundary = Integer.parseInt((String)list.elem4th());
      offset = Integer.parseInt((String)list.elem5th());
      return new SymAuto(table, name, id, tag, type, boundary, offset,
                         list.scanOpt());

    case Storage.REG:
    default:
      if (list.length() < 5 || ((String)list.elem5th()).charAt(0) == '&') {
        boundary = 0;
        offset = Integer.parseInt((String)list.elem4th());
      } else {
        boundary = Integer.parseInt((String)list.elem4th());
        offset = Integer.parseInt((String)list.elem5th());
      }
      return new SymAuto(table, name, id, tag, type, boundary, offset,
                         list.scanOpt());
    }
  }


  /** Prepend space only if x is nonnull string **/
  String preSpace(String x) {
    return (x.length() == 0) ? x : " " + x;
  }

  /** Set options **/
  public void setOpt(ImList prepend) {
    this.opt = prepend.append(this.opt);
  }

  /** Return current option list. **/
  public ImList opt() { return opt; }


  /** Visualize symbol instance. **/
  public String toString() {
    return "\"" + name + "\"";
  }

  /** Convert to external form. **/
  public abstract Object toSexp();

  /** Return contents of symbol instance. **/
  public abstract String contents();

  /** Name in print **/
  public String printName() { return name; }

}
