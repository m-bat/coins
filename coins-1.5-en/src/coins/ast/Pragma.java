/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Pragma.
 */
public class Pragma extends ASTree implements Stmnt { //SF050304
    private String text; // The text filed contains all remaining
      // characters after "pragma" in the #pragma line.
    private String fileName;
    private int lineNumber;

    public Pragma(String text, String name, int line) {
        this.text = text;
        fileName = name;
        lineNumber = line;
    }

    public void accept(Visitor v) {
       v.atPragma(this);
    }

    public String getText(){ return text; }

    public String fileName() { return fileName; }

    public int lineNumber() { return lineNumber; }

    public ASTree getLeft() { return null; }

    public ASTree getRight() { return null; }

    public void setLeft(ASTree _left) {}

    public void setRight(ASTree _right) {}

    public String toString() { return "<pragma "+text+">"; }
}
