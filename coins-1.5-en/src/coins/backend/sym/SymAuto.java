/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sym;

import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;


/** Symbol table entry for automatic objects */
public class SymAuto extends Symbol {
  /** Offset of the FRAME object */
  private int offset;

  /** Create automatic objects' symbol entry. Only SymTab calls. */
  SymAuto(SymTab table, String name, int id, int storage, int type, int boundary,
          int offset, ImList opt) {
    super(table, name, id, storage, type, boundary, opt);
    this.offset = offset;
  }

  /** Return offset of the object. **/
  public int offset() { return offset; }

  /** Change offset of the object. **/
  public void setOffset(int offset) { this.offset = offset; }

  /** Convert to external LIR format. **/
  public Object toSexp() {
    return ImList.list(new QuotedString(name),
                       Storage.toString(storage),
                       Type.toString(type),
                       boundary + "",
                       offset + "")
      .append(opt.makeCopy());
  }

  /** Visualize */
  public String contents() {
    return "(\"" + name + "\" " + Storage.toString(storage)
      + " " + Type.toString(type) + " "
      + boundary + " "
      + offset
      + preSpace(opt.toStringWOParen())
      + ")";
  }
}
