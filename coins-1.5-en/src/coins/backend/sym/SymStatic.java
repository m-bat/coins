/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sym;

import coins.backend.ModuleElement;
import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** Symbol table entry for static objects */
public class SymStatic extends Symbol {
  /** Segment name */
  public final String segment;

  /** Indicates whether external or internal */
  public final String linkage;

  /** Body of the function/data. */
  ModuleElement body;

  /** Create static objects' symbol entry. Only SymTab calls. */
  SymStatic(SymTab table, String name, int id, int storage, int type, int boundary,
            String segment, String linkage, ImList opt) {
    super(table, name, id, storage, type, boundary, opt);
    this.segment = segment;
    this.linkage = linkage;
  }

  /** Return body */
  public ModuleElement body() { return body; }

  /** Set function/data body */
  public void setBody(ModuleElement elem) { body = elem; }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    return ImList.list(new QuotedString(name),
                       Storage.toString(storage),
                       Type.toString(type),
                       boundary + "",
                       new QuotedString(segment),
                       linkage)
      .append(opt.makeCopy());
  }

  /** Visualize */
  public String contents() {
    return "(\"" + name + "\" " + Storage.toString(storage)
      + " " + Type.toString(type)
      + " " + boundary + " \"" + segment + "\" " + linkage
      + preSpace(opt.toStringWOParen()) + ")";
  }
}
