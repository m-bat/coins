/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.util;

import coins.backend.Module;
import coins.backend.Root;
import coins.backend.SyntaxError;
import coins.backend.SyntaxErrorException;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


/**
 * Immutable uni-directional list.
 *
 * Use BiList if you want mutable list.
 * Alternative to Lisp cons cell.
 */
public class ImList extends Object {
  private Object elem;
  private ImList next;

  public static final ImList Empty = new ImList(null, null);

  /** Create a link */
  public ImList(Object content, ImList tail) {
    elem = content;
    next = tail;
  }

  /** Create singleton list. */
  public ImList(Object content) {
    elem = content;
    next = Empty;
  }


  /** Create empty list. **/
  public static ImList list() { return Empty; }

  /** Create 1-element list. **/
  public static ImList list(Object obj) { return new ImList(obj); }

  /** Create 2-elements list. **/
  public static ImList list(Object obj1, Object obj2) {
    return new ImList(obj1, new ImList(obj2));
  }

  /** Create 3-elements list. **/
  public static ImList list(Object obj1, Object obj2, Object obj3) {
    return new ImList(obj1, new ImList(obj2, new ImList(obj3)));
  }

  /** Create 4-elements list. **/
  public static ImList list(Object obj1, Object obj2, Object obj3, Object obj4) {
    return new ImList(obj1, new ImList(obj2, new ImList(obj3, new ImList(obj4))));
  }

  /** Create 5-elements list. **/
  public static ImList list(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
    return new ImList(obj1, new ImList(obj2, new ImList(obj3, new ImList(obj4, new ImList(obj5)))));
  }

  /** Create 6-elements list. **/
  public static ImList list(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
    return new ImList(obj1, new ImList(obj2, new ImList(obj3, new ImList(obj4, new ImList(obj5, new ImList(obj6))))));
  }


  /** Return the next list header. */
  public ImList next() { return next; }

  /** Return the 2nd next list header. */
  public ImList next2nd() { return next.next; }

  /** Return the 3rd next list header. */
  public ImList next3rd() { return next.next.next; }

  /** Return first element of the list. */
  public Object elem() { return elem;  }

  /** Return second element of the list. */
  public Object elem2nd() { return next.elem; }

  /** Return third element of the list. */
  public Object elem3rd() { return next.next.elem; }

  /** Return fourth element of the list. */
  public Object elem4th() { return next.next.next.elem; }

  /** Return fifth element of the list. */
  public Object elem5th() { return next.next.next.next.elem; }

  /** Return sixth element of the list. */
  public Object elem6th() { return next.next.next.next.next.elem; }

  /** Return last element of the list. */
  public Object lastElem() {
    Object obj = null;
    for (ImList p = this; !p.atEnd(); p = p.next())
      obj = p.elem();
    return obj;
  }

  /** Return nth next of the list. */
  public ImList next(int n) {
    if (n < 0)
      throw new IllegalArgumentException();
    ImList p = this;
    for (; !p.atEnd(); p = p.next()) {
      if (n-- == 0)
        break;
    }
    return p;
  }
    
  /** Return nth element of the list. */
  public Object elem(int n) {
    if (n < 0)
      throw new IllegalArgumentException();
    for (ImList p = this; !p.atEnd(); p = p.next()) {
      if (n-- == 0)
        return p.elem();
    }
    return null;
  }
    
  /** Return true if this list is at end (empty). */
  public boolean atEnd() { return this == Empty; }

  /** Return true if this list is empty. */
  public boolean isEmpty() { return this == Empty; }

  /** Return the list concatenated this and <code>addendum</code>. **/
  public ImList append(ImList addendum) {
    if (addendum == Empty)
      return this;
    if (this == Empty)
      return addendum;
    return new ImList(elem, next.append(addendum));
  }

  /** Return number of elements of the list. */
  public int length() {
    int n = 0;
    for (ImList p = this; p != Empty; p = p.next)
      n++;
    return n;
  }

  /** Find a sublist which has element obj. **/
  public ImList locate(Object obj) {
    for (ImList p = this; !p.atEnd(); p = p.next()) {
      if (p.elem == obj)
        return p;
    }
    return null;
  }

  /** Return shallow copy of list. **/
  public ImList makeCopy() {
    ImList list = Empty;
    for (ImList p = this; !p.atEnd(); p = p.next())
      list = new ImList(p.elem, list);
    return list.destructiveReverse();
  }

  /** Scan &opt */
  public ImList scanOpt() {
    ImList list = this;
    for (; !list.atEnd(); list = list.next()) {
      if (list.elem instanceof String
          && ((String)list.elem).charAt(0) == '&')
        break;
    }
    return list;
  }


  /** Reverse the list destructively. Think three times before use. */
  public ImList destructiveReverse() {
    ImList p = this, q = Empty;
    while (p != Empty) {
      ImList w = p.next;
      p.next = q;
      q = p;
      p = w;
    }
    return q;
  }


  /** Reverse the list destructively with tail list. */
  public ImList destructiveReverse(ImList tail) {
    ImList p = this, q = tail;
    while (p != Empty) {
      ImList w = p.next;
      p.next = q;
      q = p;
      p = w;
    }
    return q;
  }


  /** Read S-expression and build them up in ImList form.
   *  @param rdr Reader where input comes from.
   *  @return the list or String object. null indicates EOF. */
  public static Object readSexp(Reader rdr)
    throws IOException, SyntaxError {
    return readSexp(new PushbackReader(rdr));
  }

  /** Read S-expression from Reader prd and build them up in ImList form.
   *  @return the list or String object. null indicates EOF. */
  public static Object readSexp(PushbackReader prd)
    throws IOException, SyntaxError {
    int c;

    c = skipSpaces(prd);
    if (c < 0)
      return null;

    if (c == '\'') // quote
      return new ImList("\'", new ImList(readSexp(prd), Empty));

    else if (c == '`') // backquote
      return new ImList("`", new ImList(readSexp(prd), Empty));

    else if (c == ',') // comma
      return new ImList(",", new ImList(readSexp(prd), Empty));

    else if (c == '(') {
      ImList ptr = Empty;
      for (;;) {
        c = skipSpaces(prd);
        if (c < 0)
          throw new SyntaxError("Unexpected EOF");
        if (c == ')')
          break;
        prd.unread(c);
        ptr = new ImList(readSexp(prd), ptr);
      }
      return ptr.destructiveReverse();
    }

    else if (c == ')')
      throw new SyntaxError("Invalid )");

    else if (c == '"') {
      // String
      StringBuffer buf = new StringBuffer();

    quoteloop:
      for (;;) {
        if ((c = prd.read()) < 0)
          break;
        if (c == '"')
          break;
        if (c == '\\') {
          if ((c = prd.read()) < 0)
            break;
          switch (c) {
          case 'n': c = '\n'; break;
          case 't': c = '\t'; break;
          case 'r': c = '\r'; break;
          case 'b': c = '\b'; break;
          case 'f': c = '\f'; break;
          default:
            if ('0' <= c && c <= '7') {
              int val = 0;
              for (int i = 0; i < 3 && ('0' <= c && c <= '7'); i++) {
                val = val * 8 + c;
                c = prd.read();
              }
              if (c < 0)
                break quoteloop;
              prd.unread(c);
            }
            break;
          }
        }
        buf.append((char)c);
      }
      return new QuotedString(buf.toString());
    }
    else {
      // symbol or number atom
      StringBuffer buf = new StringBuffer();
      for (;;) {
        buf.append((char)c);
        if ((c = prd.read()) < 0)
          break;
        if (Character.isWhitespace((char)c) || c == ')' || c == '(') {
          prd.unread(c);
          break;
        }
      }
      return buf.toString().intern();
    }
  }

  private static int skipSpaces(PushbackReader prd)
    throws IOException {
    int c;

    for (;;) {
      // Skip whitespaces
      while ((c = prd.read()) >= 0 && Character.isWhitespace((char)c))
        ;
      if (c != ';')
        return c;

      // Skip comments until end of line
      while ((c = prd.read()) >= 0 && c != '\n')
        ;
    }
  }


  /** Return true if x is an ImList and equals to this list. **/
  public boolean equals(Object x) {
    if (!(x instanceof ImList))
      return false;
    if (this == (ImList)x)
      return true;
    ImList p = this;
    ImList q = (ImList)x;
    for (; p != Empty && q != Empty; p = p.next, q = q.next) {
      if (!(p.elem).equals(q.elem))
        return false;
    }
    return p == q; // true if both p and q reach end of list
  }


  /** Visualize */
  public String toString() {
    return "(" + toStringWOParen() + ")";
  }

  /** Visualize without () **/
  public String toStringWOParen() {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for (ImList p = this; p != Empty; p = p.next) {
      if (!first) buf.append(" ");
      if (p.elem == null)
        buf.append("<null>");
      else
        buf.append(p.elem.toString());
      first = false;
    }
    return buf.toString();
  }



  private static class PrettyPrinter {
    static final int DEFAULT_WIDTH = 80;

    private PrintWriter writer;
    private int width;
    private int col;

    private int step = 2;

    PrettyPrinter(PrintWriter writer) {
      this(writer, DEFAULT_WIDTH);
    }

    PrettyPrinter(PrintWriter writer, int width) {
      this.writer = writer;
      this.width = width;
      this.col = 0;
    }

    PrettyPrinter(PrintWriter writer, int width, int indent) {
      this.writer = writer;
      this.width = width;
      this.col = indent;
    }

    int lengthOf(Object obj) {
      return obj.toString().length();
    }

    void printSpace(PrintWriter writer, int n) {
      for (int i = 0; i < n; i++)
        writer.print(" ");
    }

    void printIt(Object obj) {
      printItX(obj);
      writer.println();
      writer.flush();
    }

    void printItX(ImList list) {
      if (col + lengthOf(list) < width) {
        writer.print(list.toString());
        col += lengthOf(list);
        return;
      }
      writer.print("(");
      col++;

      int topCol = col;
      boolean tol = true;
      boolean prelist = false;
      ImList p = list;
      for (; !p.atEnd(); p = p.next()) {
        Object obj = p.elem();
        if (!tol) {
          if (prelist || obj instanceof ImList || col + 1 + lengthOf(obj) >= width) {
            writer.println();
            col = topCol + step;
            printSpace(writer, col);
          } else {
            writer.print(" ");
            col++;
          }
        }
        printItX(obj);
        prelist = (obj instanceof ImList);
        tol = false;
      }
      writer.print(")");
      col++;
      return;
    }
    


    void printItX(Object obj) {
      if (obj instanceof ImList)
        printItX((ImList)obj);
      else {
        String s = obj.toString();
        if (s.charAt(0) == '(')
          writer.print("!");
        writer.print(s);
        col += s.length();
      }
    }
  }



  /** Print beautifully. **/
  public void printIt(PrintWriter writer) {
    (new PrettyPrinter(writer)).printIt(this);
  }

  /** Print beautifully. **/
  public void printIt(PrintWriter writer, int width) {
    (new PrettyPrinter(writer, width)).printIt(this);
  }

  /** Print beautifully with indentation. **/
  public void printIt(PrintWriter writer, int width, int indent) {
    (new PrettyPrinter(writer, width, indent)).printIt(this);
  }


  /** Print beautifully any object. **/
  public static void printIt(PrintWriter writer, Object obj) {
    (new PrettyPrinter(writer)).printIt(obj);
  }


  /** Test Driver **/
  public static void main(String[] args) {
    try {
//      Object obj = readSexp(new InputStreamReader(System.in));
      Object obj = readSexp(new FileReader("gcd.c.lir"));
      //     System.out.println("Pretty Printer Output:");
      //     (new PrettyPrinter(new PrintWriter(System.out))).printIt(obj);
      CompileSpecification spec = new CommandLine();
      Root root = new Root(spec, new PrintWriter(System.err));
      Module mod = new Module(obj, "mips", "spim", root);
      mod.printStandardForm(new PrintWriter(new FileWriter("gcd.c.lir.out")));

    } catch(java.io.IOException e) {
      throw new Error("IOexception");
    } catch(SyntaxError e) {
      throw new Error("Syntax error");
  } catch(SyntaxErrorException e) {
      throw new Error("Syntax error Exception");
    }                       
  }
}
