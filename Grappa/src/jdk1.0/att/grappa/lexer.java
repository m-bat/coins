/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */

package att.grappa;

import java_cup.runtime.token;
import java_cup.runtime.str_token;
import java.util.Hashtable;
import java.io.*;

public class lexer
{
  /**
   * First character of lookahead.
   * Set to '\n' initially (needed for initial 2 calls to advance())
   */
  private int next_char = '\n';

  /**
   * Second character of lookahead.
   * Set to '\n' initially (needed for initial 2 calls to advance())
   */
  private int next_char2 = '\n';

  /**
   * Current line number for use in error messages.
   * Set to -1 to account for next_char/next_char2 initialization
   */
  private int current_line = -1;

  /**
   * Character position in current line.
   */
  private int current_position = 1;

  /**
   * EOF constant.
   */
  private static final int EOF_CHAR = -1;

  /**
   * needed to handle anonymous subgraphs since parser has no precedence
   */
  private boolean haveId = false;

  /**
   * needed for retreating
   */
  private int old_char;
  private int old_position;
  boolean retreated = false;

  /**
   * Count of total errors detected so far.
   */
  private int error_count = 0;

  /**
   *  Count of warnings issued so far
   */
  private int warning_count = 0;

  /**
   *  hash tables to hold symbols
   */
  private Hashtable keywords = new Hashtable(32);
  private Hashtable char_symbols = new Hashtable(32);

  private InputStream inStream;
  private PrintStream errStream = null;


  public lexer(InputStream input, PrintStream error) throws IllegalArgumentException {
    super();
    if (input == null) {
      throw new IllegalArgumentException("InputStream cannot be null");
    }
    inStream = input;
    errStream = error;
  }

  /**
   *  tables and reads the first two characters of lookahead.
   */
  public void init() throws IOException {
    // set up the keyword table
    keywords.put("strict", new Integer(sym.STRICT));
    keywords.put("digraph", new Integer(sym.DIGRAPH));
    keywords.put("graph", new Integer(sym.GRAPH));
    keywords.put("subgraph", new Integer(sym.SUBGRAPH));
    keywords.put("node", new Integer(sym.NODE));
    keywords.put("edge", new Integer(sym.EDGE));
    keywords.put("--", new Integer(sym.ND_EDGE_OP));
    keywords.put("->", new Integer(sym.D_EDGE_OP));

    //keywords.put ("fontsize",  new Integer (sym.FONTSIZE));
    //keywords.put ("fontname",  new Integer (sym.FONTNAME));
    //keywords.put ("fontcolor", new Integer (sym.FONTCOLOR));
    //keywords.put ("rankdir",   new Integer (sym.RANKDIR));
    //keywords.put ("version",   new Integer (sym.VERSION));
    //keywords.put ("color",     new Integer (sym.COLOR));
    //keywords.put ("label",     new Integer (sym.LABEL));
    //keywords.put ("description", new Integer (sym.DESCRIPTION));
    //keywords.put ("shape",     new Integer (sym.SHAPE));
    //keywords.put ("type",      new Integer (sym.TYPE));
    //keywords.put ("width",     new Integer (sym.WIDTH));
    //keywords.put ("height",    new Integer (sym.HEIGHT));
    //keywords.put ("style",     new Integer (sym.STYLE));
    //keywords.put ("lp",        new Integer (sym.LP));
    //keywords.put ("bb",        new Integer (sym.BB));
    //keywords.put ("rank",      new Integer (sym.RANK));
    //keywords.put ("dir",       new Integer (sym.DIR));
    //keywords.put ("pos",       new Integer (sym.POS));

    // set up the table of single character symbols
    char_symbols.put(new Integer(';'), new Integer(sym.SEMI));
    char_symbols.put(new Integer(','), new Integer(sym.COMMA));
    char_symbols.put(new Integer('{'), new Integer(sym.LCUR));
    char_symbols.put(new Integer('}'), new Integer(sym.RCUR));
    char_symbols.put(new Integer('['), new Integer(sym.LBR));
    char_symbols.put(new Integer(']'), new Integer(sym.RBR));
    char_symbols.put(new Integer('='), new Integer(sym.EQUAL));
    char_symbols.put(new Integer(':'), new Integer(sym.COLON));

    // read two characters of lookahead
    advance();
    advance();
  }
  /**
   * Advance the scanner one character in the input stream.  This moves
   * next_char2 to next_char and then reads a new next_char2.
   */
  public void advance() throws IOException {
    if(retreated) {
      retreated = false;
      int tmp_char = old_char;
      old_char = next_char;
      next_char = next_char2;
      next_char2 = tmp_char;
    } else {
      old_char = next_char;
      next_char = next_char2;
      if (next_char == EOF_CHAR) {
	next_char2 = EOF_CHAR;
      } else {
	next_char2 = inStream.read();
      }
    }

  /*
   * want to ignore a new-line if preceeding character is a backslash
   */
      if (next_char == '\\' && (next_char2 == '\n' || next_char2 == '\r')) {
	next_char = next_char2;
	next_char2 = inStream.read();
	if(next_char == '\r' && next_char2 == '\n') {
	next_char = next_char2;
	next_char2 = inStream.read();
	}
	next_char = next_char2;
	next_char2 = inStream.read();
      }


  /*
   * want to treat '\r' or '\n' or '\r''\n' as end-of-line,
   * but in all cases return only '\n'
   */
    if(next_char == '\r') {
      if(next_char2 == '\n') {
	next_char2 = inStream.read();
      }
      next_char = '\n';
    }
    // count this
    if (old_char == '\n') {
      current_line++;
      old_position = current_position;
      current_position = 1;
    } else {
      current_position++;
    }
  }

  private void retreat() {
    if(retreated) return;
    retreated = true;
    if(old_char == '\n') {
      current_line--;
      current_position = old_position;
    } else {
      current_position--;
    }
    int tmp_char = next_char2;
    next_char2 = next_char;
    next_char = old_char;
    old_char = tmp_char;
  }

  /**
   * Emit an error message.  The message will be marked with both the
   * current line number and the position in the line.  Error messages
   * are printed on print stream passed to lexer (if any) and a
   * GraphParserException is thrown.
   *
   * @param message the message to print.
   */
  private void emit_error(String message) {
    String output = "lexer error at " + current_line +
			"(" + current_position + "): " + message;
    if(errStream != null) {
      errStream.println("ERROR: " + output);
    }
    error_count++;
    throw new GraphParserException(output);
  }

  /**
   * Emit a warning message.  The message will be marked with both the
   * current line number and the position in the line.  Messages are
   * printed on print stream passed to lexer (if any).
   *
   * @param message the message to print.
   */
  private void emit_warn(String message) {
    if(errStream != null) {
      errStream.println("WARNING: parse warning at " + current_line +
			"(" + current_position + "): " + message);
    }
    warning_count++;
  }                              // Determine if a character is ok to start an id.

  /**
   * Check if character is a valid initial character;
   * @param ch the character in question.
   */
  private boolean id_start_char(int ch) {
    return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
      (ch == '_') || (ch >= '0' && ch <= '9');

    // later need to deal with non-8-bit chars here
  }

  /**
   * Determine if a character is ok for the middle of an id.
   * @param ch the character in question.
   */
  private boolean id_char(int ch) {
    return id_start_char(ch) || (ch >= '0' && ch <= '9');
  }

  /**
   * Try to look up a single character symbol, returns -1 for not found.
   * @param ch the character in question.
   */
  private int find_single_char(int ch) {
    Integer result;

    result = (Integer) char_symbols.get(new Integer((char) ch));
    if (result == null) {
      return -1;
    } else {
      return result.intValue();
    }
  }

  /**
   * Handle swallowing up a comment.  Both old style C and new style C++
   * comments are handled.
   */
  private void swallow_comment() throws IOException {
    // next_char == '/' at this point.

    // Is it a traditional comment?
    if (next_char2 == '*') {
      // swallow the opener
      advance();
      advance();

      // swallow the comment until end of comment or EOF
      for (;;) {
        // if its EOF we have an error
        if (next_char == EOF_CHAR) {
          emit_error("Specification file ends inside a comment");
          return;
        }
        // if we can see the closer we are done
        if (next_char == '*' && next_char2 == '/') {
          advance();
          advance();
          return;
        }
        // otherwise swallow char and move on
        advance();
      }
    }
    // is its a new style comment
    if (next_char2 == '/') {

      // swallow the opener
      advance();
      advance();

      // swallow to '\n', '\f', or EOF
      while (next_char != '\n' && next_char != '\f' && next_char != EOF_CHAR) {
        advance();
      }

      return;

    }
    // shouldn't get here, but... if we get here we have an error
    emit_error("Malformed comment in specification -- ignored");
    advance();
  }

  /**
   * Swallow up a quote string.  Quote strings begin with a double quote
   * and include all characters up to the first occurrence of another double
   * quote (there is no way to include a double quote inside a quote string).
   * The routine returns an str_token object suitable for return by the scanner.
   */
  private str_token do_quote_string() throws IOException {
    StringBuffer result = new StringBuffer();

    // at this point we have lookahead of a double quote -- swallow that
    advance();

    // save chars until we see a double quote
    while (!(next_char == '"')) {
      // skip line break
      if (next_char == '\\' && (next_char2 == '\n' || next_char2 == '\r')) {
        advance();
	if(next_char == '\r' && next_char2 == '\n') {
	  advance();
	}
	advance();
	continue;
      }
      // if we have run off the end issue a message and break out of loop
      if (next_char == EOF_CHAR) {
        emit_error("Specification file ends inside a code string");
        break;
      }
      // otherwise record the char and move on
      result.append(new Character((char) next_char));
      advance();
    }

    // advance past the closing double quote and build a return token
    advance();
    haveId = true;
    return new str_token(sym.ID, result.toString());
  }

  /**
   * Process an identifier.  Identifiers begin with a letter, underscore,
   * or dollar sign, which is followed by zero or more letters, numbers,
   * underscores or dollar signs.  This routine returns an str_token suitable
   * for return by the scanner.
   */
  private str_token do_id() throws IOException {
    StringBuffer result = new StringBuffer();
    String result_str;
    Integer keyword_num;
    char buffer[] = new char[1];

    // next_char holds first character of id
    buffer[0] = (char) next_char;
    result.append(buffer, 0, 1);
    advance();

    // collect up characters while they fit in id
    while (id_char(next_char)) {
      buffer[0] = (char) next_char;
      result.append(buffer, 0, 1);
      advance();
    }
    // extract a string and try to look it up as a keyword
    result_str = result.toString();
    keyword_num = (Integer) keywords.get(result_str);

    // if we found something, return that keyword
    if (keyword_num != null) {
      haveId = false;
      return new str_token(keyword_num.intValue());
    }

    // otherwise build and return an id token with an attached string
    haveId = true;
    return new str_token(sym.ID, result_str);
  }

  /**
   * The actual routine to return one token.  This is normally called from
   * next_token(), but for debugging purposes can be called indirectly from
   * debug_next_token().
   */
  public token real_next_token() throws IOException {
    int sym_num;

    for (;;) {
      // look for white space
      if (next_char == ' ' || next_char == '\t' || next_char == '\n' ||
          next_char == '\f') {

        // advance past it and try the next character
        advance();
        continue;
      }

      // look for edge operator
      if (next_char == '-') {
        if (next_char2 == '>') {
          advance();
          advance();
	  haveId = false;
          return new token(sym.D_EDGE_OP);
        } else if (next_char2 == '-') {
          advance();
          advance();
	  haveId = false;
          return new token(sym.ND_EDGE_OP);
        }
      }

      // look for a single character symbol
      sym_num = find_single_char(next_char);
      if (sym_num != -1) {
	if (sym_num == sym.LCUR && !haveId) {
	  str_token intermediate = new str_token(sym.SUBGRAPH);
	  token result = (token) intermediate;
	  haveId = true;
	  retreat();
	  return result;
	}

        // found one -- advance past it and return a token for it
        advance();
	haveId = false;
        return new token(sym_num);
      }

      // look for quoted string
      if (next_char == '"') {
        str_token intermediate = do_quote_string();
        token result = (token) intermediate;
        return result;
      }

      // look for a comment
      if (next_char == '/' && (next_char2 == '*' || next_char2 == '/')) {
        // swallow then continue the scan
        swallow_comment();
        continue;
      }

      // look for an id or keyword
      if (id_start_char(next_char)) {
        str_token intermediate = do_id();
        token result = (token) intermediate;
        return result;
      }

      // look for EOF
      if (next_char == EOF_CHAR) {
	haveId = false;
        return new token(sym.EOF);
      }

      // if we get here, we have an unrecognized character
      emit_warn("Unrecognized character '" +
		new Character((char) next_char) + "'(" + next_char +
		") -- ignored");

      // advance past it
      advance();
    }
  }

  /**
   * Return one token.  This is the main external interface to the scanner.
   * It consumes sufficient characters to determine the next input token
   * and returns it.  To help with debugging, this routine actually calls
   * real_next_token() which does the work.
   */
  public token next_token(int debugLevel) throws IOException {
    if(debugLevel > 0) {
      token result = real_next_token();
      if(result instanceof str_token) {
        str_token str_result = (str_token)result;
	if(errStream != null && debugLevel >= 5) {
	  errStream.println("DEBUG: lexer: next_token() => " + str_result.sym + " (" + str_result.str_val + ")");
	}
      } else {
	if(errStream != null && debugLevel >= 5) {
	  errStream.println("DEBUG: lexer: next_token() => " + result.sym);
	}
      }
      return result;
    } else {
      return real_next_token();
    }
  }
}
