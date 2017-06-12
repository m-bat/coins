/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Trace controller.<br>
 *
 * A trace controller is initialized by trace options specified in a command
 * line and determines which trace message should be printed.<br>
 *
 * There are two types of trace messages: generic (category-free) trace
 * messages and categorized trace messages.  A trace message may have a
 * message level which is an integer value.  Trace messages without message
 * levels are called level-less trace messages and treated as level-zero.<br>
 *
 * A trace option is one of COINS options and should be given in the form:
 *
 * <blockquote>
 * <tt>-coins:trace=</tt><i>tracespec</i><tt>/</tt><i>tracespec</i>
 * <tt>/...</tt><br>
 * </blockquote>
 *
 * where <i>tracespec</i>s can be <i>level</i> or <i>category.level</i>.  When
 * <i>level</i> is specified, generic trace messages whose level is below the
 * <i>level</i> are shown.  When <i>category.level</i> is specified, trace
 * messages of category <i>category</i> whose level is below the <i>level</i>
 * are shown.  When more than one <i>level</i>s are specified, or more than
 * one <i>category.level</i>s are specified for a same <i>category</i>, the
 * latter one overwrites the formar one.  A special category "default" is
 * provided to specify a trace level of all trace categories (and
 * category-less messages) except categories appeared in other
 * <i>tracespec</i>s.<br>
 *
 * Trace controller holds a generic trace level and category-wise trace
 * levels.  A trace message whose message level is smaller than or equal to
 * the corresponding trace level is determined to be printed, otherwise not.
 * The generic trace level is for generic trace messages and a category-wise
 * trace level of a message category is for trace messages of that category.
 **/

public class Trace {
  private static final char CATEGORY_DELIMITER = '/';
  private static final char LEVEL_DELIMITER = '.';

  private static final String fMessageHeader = "TRACE";
  private static final String defaultCategoryName = "default";

  private final PrintStream fTraceOut;
  private final Map fTraceTable;
  private int fGenericTraceLevel;
  private boolean fGenericTraceLevelIsSet;
  private int fDefaultTraceLevel;
  private boolean fDefaultTraceLevelIsSet;

  private void parseTraceSpec(String pTraceSpec, Map pTable,
			      Warning pWarning) {
    int lLevelDelimiterIndex = pTraceSpec.indexOf(LEVEL_DELIMITER);
    if (lLevelDelimiterIndex == -1) {
      if (Character.isDigit(pTraceSpec.charAt(0))) {
	try {
	  fGenericTraceLevel = Integer.parseInt(pTraceSpec);
	  fGenericTraceLevelIsSet = true;
	} catch (NumberFormatException lNumberFormatException) {
	  pWarning.warning("Driver",
			   "Trace level must be a number: " + pTraceSpec);
	}
      } else {
	pTable.put(pTraceSpec, new Integer(Integer.MAX_VALUE));
      }
    } else {
      String lCategory = pTraceSpec.substring(0, lLevelDelimiterIndex);
      String lLevelString = pTraceSpec.substring(lLevelDelimiterIndex + 1);
      try {
	Integer lLevel = Integer.valueOf(lLevelString);
	if (lCategory.equals(defaultCategoryName)) {
	  fDefaultTraceLevelIsSet = true;
	  fDefaultTraceLevel = lLevel.intValue();
	} else {
	  pTable.put(lCategory, lLevel);
	}
      } catch (NumberFormatException lNumberFormatException) {
	pWarning.warning("Driver",
			 "Trace level must be a number: " + lLevelString);
      }
    }
  }

  private Map parseArgument(String pArgument, Warning pWarning) {
    Map lTable = new Hashtable();
    String lArgument = pArgument.trim();
    int lIndex = 0;
    int lLen = lArgument.length();

    while (lIndex < lLen) {
      int lCategoryDelimiterIndex;
      lCategoryDelimiterIndex = lArgument.indexOf(CATEGORY_DELIMITER, lIndex);
      String lNextArg;
      if (lCategoryDelimiterIndex == -1) {
	lNextArg = lArgument.substring(lIndex);
	lIndex = lLen;
      } else {
	lNextArg = lArgument.substring(lIndex, lCategoryDelimiterIndex);
	lIndex = lCategoryDelimiterIndex + 1;
      }
      parseTraceSpec(lNextArg, lTable, pWarning);
    }
    return lTable;
  }

  /**
   *
   * Constructs a new default trace controller, which prints no trace messages
   * to <tt>System.out</tt>.
   * @param warning warnings should go here.
   **/
  public Trace(Warning warning) {
    this(System.out, warning);
  }

  /**
   *
   * Constructs a new default trace controller, which prints no trace messages
   * to a specified <tt>PrintStream</tt>.
   *
   * @param out the trace output <tt>PrintStream</tt>.
   * @param warning warnings should go here.
   **/
  public Trace(PrintStream out, Warning warning) {
    this(out, "", warning);
  }

  /**
   *
   * Constructs a new trace controller, which prints trace messages specified
   * to be printed by command line options.  During parsing the options, some
   * warning messages may be produced.
   *
   * @param out the trace output <tt>PrintStream</tt>.
   * @param traceArgument argument of the <tt>trace</tt> option.
   * @param warning warnings should go here.
   **/
  public Trace(PrintStream out, String traceArgument, Warning warning) {
    fTraceOut = out;
    fGenericTraceLevelIsSet = false;
    fDefaultTraceLevelIsSet = false;
    fTraceTable = parseArgument(traceArgument, warning);
  }

  /**
   *
   * Tests if a generic level-less trace message should be printed or not.
   * Returns <tt>true</tt> if the generic trace level is zero, <tt>false</tt>
   * otherwise.
   **/
  public synchronized boolean shouldTrace() {
    return fGenericTraceLevelIsSet || fDefaultTraceLevelIsSet;
  }

  /**
   *
   * Tests if a generic trace message of a certain level should be printed or
   * not.  Returns <tt>true</tt> if the generic trace level is less than or
   * equal to the message level, <ff>false</tt> otherwise.
   *
   * @param level the message level
   **/
  public synchronized boolean shouldTrace(int level) {
    return
      fGenericTraceLevelIsSet && (fGenericTraceLevel >= level) ||
      fDefaultTraceLevelIsSet && (fDefaultTraceLevel >= level);
  }

  /**
   *
   * Tests if a level-less trace message of a certain category should be
   * printed or not.  Returns <tt>true</tt> if the category-wise trace level
   * of the category is specified, <tt>false</tt> otherwise.
   **/
  public synchronized boolean shouldTrace(String category) {
    return fTraceTable.containsKey(category);
  }

  /**
   *
   * Tests if a trace message of a certain category and level should be
   * printed or not.  Returns <tt>true</tt> if the category-wise trace level
   * of the category is less than or equal to the message level,
   * <tt>false</tt> otherwise.
   **/
  public synchronized boolean shouldTrace(String category, int level) {
    if (shouldTrace(category)) {
      return ((Integer)fTraceTable.get(category)).intValue() >= level;
    } else {
      return fDefaultTraceLevelIsSet && (fDefaultTraceLevel >= level);
    }
  }

  private void putMessage(String message) {
    fTraceOut.println("[" + fMessageHeader + "] " + message);
  }

  private void putMessage(String category, String message) {
    fTraceOut.println("[" + fMessageHeader + ":" + category + "] " + message);
  }

  private void putMessage(int level, String message) {
    fTraceOut.println("[" + fMessageHeader + ":" + level + "] " + message);
  }

  private void putMessage(String category, int level, String message) {
    fTraceOut.println("[" + fMessageHeader + ":"
		      + category + "." + level + "] " + message);
  }

  /**
   *
   * Prints a generic level-less trace message when it should be printed.
   *
   * @param message the message.
   **/
  public synchronized void trace(String message) {
    if (shouldTrace()) {
      putMessage(message);
    }
  }

  /**
   *
   * Prints a generic message of a certain message level when it should be
   * printed.
   *
   * @param level the message level.
   * @param message the message.
   **/
  public synchronized void trace(int level, String message) {
    if (shouldTrace(level)) {
      putMessage(level, message);
    }
  }

  /**
   *
   * Prints a level-less message of a certain message category when it should
   * be printed.
   *
   * @param cateogry the message category.
   * @param message the message.
   **/
  public synchronized void trace(String category, String message) {
    if (shouldTrace(category)) {
      putMessage(category, message);
    }
  }

  /**
   *
   * Prints a message of a certain message category and level when it should
   * be printed.
   *
   * @param cateogry the message category
   * @param level the message level
   * @param message the message
   **/
  public synchronized void trace(String category, int level, String message) {
    if (shouldTrace(category, level)) {
      putMessage(category, level, message);
    }
  }

  /**
   *
   * Returns the generic trace level, which is used to determine whether a
   * generic (category free) trace message should be printed or not.
   *
   * @return the generic trace level.
   **/
  public synchronized int getGenericTraceLevel() {
    if (fGenericTraceLevelIsSet) {
      return fGenericTraceLevel;
    } else {
      return 0;
    }
  }

  /**
   *
   * Returns the default category trace level, which is used to
   * determine whether a trace message whose category is not appeared
   * in trace specifications should be printed or not.
   *
   * @return the default category trace level.
   **/
  public synchronized int getDefaultTraceLevel() {
    if (fDefaultTraceLevelIsSet) {
      return fDefaultTraceLevel;
    } else {
      return 0;
    }
  }

  /**
   *
   * Return a category-wise trace level of a category.  This method simply
   * returns a trace level specified in a compile specification.  You should
   * use one of <tt>shouldTrace</tt> methods to see if some trace messages
   * should be printed or not.
   *
   * @param category the category.
   * @return a category-wise trace level.
   **/
  public synchronized int getTraceLevel(String category) {
    if (fTraceTable.containsKey(category)) {
      return ((Integer)fTraceTable.get(category)).intValue();
    } else {
      return 0;
    }
  }

  /**
   * Redefines the generic trace level.
   *
   * @param newLevel a new value of the generic trace level.
   * @return an old value of the generic trace level.
   **/
  public synchronized int setGenericTraceLevel(int newLevel) {
    int oldLevel = fGenericTraceLevel;
    fGenericTraceLevel = newLevel;
    fGenericTraceLevelIsSet = true;
    return oldLevel;
  }

  /**
   * Redefines the default trace level.
   *
   * @param newLevel a new value of the default trace level.
   * @return an old value of the default trace level.
   **/
  public synchronized int setDefaultTraceLevel(int newLevel) {
    int oldLevel = fDefaultTraceLevel;
    fDefaultTraceLevel = newLevel;
    fDefaultTraceLevelIsSet = true;
    return oldLevel;
  }

  /**
   * Redefines a category-wise trace level.
   *
   * @param category a message category.
   * @param newLevel a new value of the category-wise trace level.
   * @return an old value of the category-wise trace level.
   **/
  public synchronized int setTraceLevel(String category, int newLevel) {
    int oldLevel;
    if (category.equals(defaultCategoryName)) {
      oldLevel = fDefaultTraceLevel;
      fDefaultTraceLevel = newLevel;
      fDefaultTraceLevelIsSet = true;
      return oldLevel;
    } else {
      Integer old = (Integer)fTraceTable.get(category);
      if (old == null) {
	oldLevel = 0;
      } else {
	oldLevel = old.intValue();
      }
      fTraceTable.put(category, new Integer(newLevel));
      return oldLevel;
    }
  }
}
