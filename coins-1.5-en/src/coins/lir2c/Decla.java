/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lir2c;

/**
 * Decla: the subset of declarations.
 **/
public class Decla{
  public String indent;    // indent

  public String storage;   // one of auto, register, static,
                           //   extern and typedef.

  public String qualifier; // one of const, volatile.

  public String type;      // one of void, char, short, int,
                           //   long, float, double, signed,
                           //   unsigned,(struct or union specifier),
                           //   (enum specifier), (typedef name).

  public String pointer;   // "" or * or **, or ***...

  public String identifier;// the name of value

  public String array;     // "" or [], or [constant-expression],
                           // or [c-e][c-e], or...
  public String initializer; // = ?, = {a}, = {{a,b},{c,d}} ...

  public Decla() {
    indent = "    ";
    storage = "";
    type = "";
    qualifier = "";
    pointer = "";
    identifier = "";
    array = "";
    initializer = "";
  }

  public String toString() {
    String results = indent;

    if (! storage.equals(""))
      results += storage + " ";

    if (! qualifier.equals(""))
      results += qualifier + " ";

    if (! type.equals(""))
      results += type + " ";

    results += pointer;
    results += identifier;
    results += array;

    if (! initializer.equals(""))
      results += " = " + initializer;

    // results += ";";

    return results;
  }
}
