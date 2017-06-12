/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.IoRoot;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Warning controller.<br>
 *
 * A warning controller is initialized by warning options specified in a
 * command line and determine which warning message should be printed.<br>
 *
 * There are two types of warning messages: generic (category-free) warning
 * messages and categorized warining messages.<br>
 *
 * A `warning number' is an identifier of a warning message.  It can be given
 * to both types of a warning message, and if given, it is printed with the
 * message.  Assignment of a warning number is out of scope of this class.<br>
 *
 * A warning option should be given in the forms:
 *
 * <blockquote>
 * <tt>-W</tt><i>category</i>
 * </blockquote>
 * <blockquote>
 * <tt>-Wno-</tt><i>category</i>
 * </blockquote>
 * or,
 * <blockquote>
 * <tt>-Wall</tt>
 * </blockquote>
 *
 * The form <tt>-W</tt><i>category</i> is to print warning messages of
 * category <i>category</i>, and the form <tt>-Wno-</tt><i>category</i> is not
 * to print.  When both <tt>-W</tt><i>category</i> and
 * <tt>-Wno-</tt><i>category</i> are specified for a same category, the latter
 * one overwrites the formar one.  <tt>-Wall</tt> is to print all warning
 * messages, except ones which is specified not to print.  A generic warning
 * message should be printed only if the <tt>-Wall</tt> is specified.<br>
 *
 * A warning controller has a table which maps a warning category to a
 * <tt>Boolean</tt> object: true for to print it and false for not to print
 * it.
 **/

public class Warning {
  private static final String ALL = "all";
  private static final String NEGATIVE = "no-";

  private static final String fMessageHeader = "Warning";

  private final Map fWarningTable;
  private boolean fWarningAll = false;

  private IoRoot fIoRoot;

  private Map parseArgument(List pArguments) {
    Map lTable = new Hashtable();
    Iterator i = pArguments.iterator();
    while (i.hasNext()) {
      String lArg = (String)i.next();
      if (lArg.equals(ALL)) {
	fWarningAll = true;
      } else {
	if (lArg.startsWith(NEGATIVE)) {
	  lTable.put(lArg.substring(NEGATIVE.length()), new Boolean(false));
	} else {
	  lTable.put(lArg, new Boolean(true));
	}
      }
    }
    return lTable;
  }

  /**
   *
   * Constructs a default warning controller which has no warning.
   *
   * @param out the PrintStream
   **/
  public Warning() {
    this(new ArrayList());
  }

  /**
   *
   * Constructs a default warning controller which has no warning
   * specification and prints warning messages to a specified
   * <tt>IoRoot</tt>.
   *
   * @param out the PrintStream
   **/
  public Warning(IoRoot pIo) {
    this(new ArrayList(), pIo);
  }

  /**
   *
   * Constructs a warning controller which is initialized by warning options.
   * The options are given by a <tt>List</tt> of <tt>String</tt>s, each of
   * <tt>String</tt> is a command line option string excluding the leading
   * "-W".
   *
   * @param warningArguments the option strings
   **/
  public Warning(List warningArguments) {
    fWarningTable = parseArgument(warningArguments);
  }

  /**
   *
   * Constructs a warning controller which is initialized by warning options
   * and prints warning messages to a specified <tt>IoRoot</tt>.  The options
   * are given by a <tt>List</tt> of <tt>String</tt>s, each of <tt>String</tt>
   * is a command line option string excluding the leading "-W".
   *
   * @param warningArguments the option strings
   * @param pIo the <tt>IoRoot</tt>
   **/
  public Warning(List warningArguments, IoRoot pIo) {
    fIoRoot = pIo;
    fWarningTable = parseArgument(warningArguments);
  }

  /**
   * Return whether the <tt>-Wall</tt> is specified or not.
   *
   * @return <tt>true</tt> if specified, <tt>false</tt> otherwise.
   **/
  public synchronized boolean getGenericWarningFlag() {
    return fWarningAll;
  }

  /**
   *
   * Redefines whether the <tt>-Wall</tt> is specified or not.  Specify
   * <tt>true</tt> to set, <tt>false</tt> otherwise.
   *
   * @param newValue the new value
   * @return the old value
   **/
  public synchronized boolean setGenericWarningFlag(boolean newValue) {
    boolean oldValue = fWarningAll;
    fWarningAll = newValue;
    return oldValue;
  }

  /**
   *
   * Returns a corresponding <tt>Boolean</tt> value in the table associated
   * with a warning category, or <tt>null</tt> if the category does not exist
   * in the table.
   *
   * @param category the category
   **/
  public synchronized Boolean getWarningFlag(String category) {
    return (Boolean)fWarningTable.get(category);
  }

  /**
   *
   * Redefines a corresponding <tt>Boolean</tt> value in the table associated
   * with a warning category.  Returns the old value, or <tt>null</tt> if the
   * category did not exist in the table.
   *
   * @param category the category
   * @param newFlag the new value
   * @return the old value.
   **/
  public synchronized Boolean setWarningFlag(String category,
					     boolean newFlag) {
    return (Boolean)fWarningTable.put(category, new Boolean(newFlag));
  }

  /**
   * Tests if generic warning messages should be printed or not.
   *
   * @return <tt>true</tt> if it should be printed, <tt>false</tt> otherwise.
   **/
  public synchronized boolean shouldWarn() {
    return fWarningAll;
  }

  /**
   *
   * Tests if warning messages of a specified category should be printed or
   * not. 
   *
   * @param category the category
   * @return <tt>true</tt> if it should be printed, <tt>false</tt> otherwise.
   **/
  public synchronized boolean shouldWarn(String category) {
    if (fWarningTable.containsKey(category)) {
      return ((Boolean)fWarningTable.get(category)).booleanValue();
    } else {
      return shouldWarn();
    }
  }

  private void putMessage(String message) {
    if (fIoRoot == null) {
      System.out.println("Warning: " + message);
    } else {
      fIoRoot.msgWarning.put(message);
    }
  }

  private void putMessage(int warningNumber, String message) {
    if (fIoRoot == null) {
      System.out.println("Warning: " + message);
    } else {
      fIoRoot.msgWarning.put(warningNumber, message);
    }
  }

  public synchronized void setIoRoot(IoRoot pIo) {
    fIoRoot = pIo;
  }

  /**
   *
   * Prints a generic warning message when it should be printed.
   *
   * @param message the warning message
   **/
  public synchronized void warning(String message) {
    if (shouldWarn()) {
      putMessage(message);
    }
  }

  /**
   *
   * Prints a generic warning message with a warning number when it should be
   * printed.
   *
   * @param warningNumber the warning number
   * @param message the warning message
   **/
  public synchronized void warning(int warningNumber, String message) {
    if (shouldWarn()) {
      putMessage(warningNumber, message);
    }
  }

  /**
   *
   * Prints a warning message of a certain warning category when it should be
   * printed.
   *
   * @param category the warning category
   * @param message the warning message
   **/
  public synchronized void warning(String category, String message) {
    if (shouldWarn(category)) {
      putMessage(message);
    }
  }

  /**
   *
   * Prints a warning message of a certain warning category with a warning
   * number when it should be printed.
   *
   * @param warningNumber the warning number
   * @param category the warning category
   * @param message the warning message
   **/
  public synchronized void warning(int warningNumber,
				   String category, String message) {
    if (shouldWarn(category)) {
      putMessage(warningNumber, message);
    }
  }
}
