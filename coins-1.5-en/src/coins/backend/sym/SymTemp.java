/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sym;

import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** Temporary objects */
public class SymTemp extends Symbol {

  /** Original symbol */
  public final Symbol original;

  /** Create automatic object's symbol entry. Only SymTab calls. */
  SymTemp(SymTab table, Symbol original, int id, int type) {
    super(table, ".r"+id, id, Storage.REG, type, 0, null);
    this.original = original;
  }

  public String printName() {
    return name + (original != null ?  "/" + original.name : "");
  }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    return ImList.list(new QuotedString(printName()),
                       Storage.toString(storage),
                       Type.toString(type),
                       boundary + "")
      .append(opt.makeCopy());
  }


  /** Visualize */
  public String contents() {
    return
      "(\"" + printName() + "\" "
      + Storage.toString(storage)
      + " " + Type.toString(type)
      + " " + boundary
      + preSpace(opt.toStringWOParen())
      + ")";
  }
}
