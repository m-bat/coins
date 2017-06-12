/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.FatalError;
import coins.PassException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * A factory of <tt>Suffix</tt> object.<br>
 *
 * <tt>SuffixFactory</tt> reads a suffix database file and creates
 * <tt>Suffix</tt> objects representing each suffix appeared in the file.  The
 * file consists of any number of record lines following a header line.
 * Format of a header line is as following:
 *
 * <blockquote>
 * <tt>#SRD, 2,</tt> <i>any string</i>
 * </blockquote>
 *
 * ``<tt>#SRD</tt>'' represents that this file is a suffix database file.
 * ``2'' means the format version number (i.e., version 2).  You can supply
 * any string to explain the contents.
 *
 * Format of a record line is as following:
 *
 * <blockquote>
 * <i>suffixspec</i><tt>,</tt> <i>language name</i><tt>,</tt>
 * <i>description</i><tt>,</tt> <i>afterpreprocess</i><tt>,</tt>
 * <i>aftercompile</i><tt>,</tt> <i>afterassemble</i>
 * </blockquote>
 *
 * Where <i>suffixspecs</i> is a <i>suffixspec</i>, or '/' separated
 * <i>suffixspec</i>s to show more than one suffix represent a same kind of
 * file.  A <i>suffixspec</i> is a suffix string may be followed by a
 * <tt>(<i>option</i>)</tt>.  The <i>option</i> is called as a suffix option.
 * More than one record can specify a same suffix as long as a suffix option
 * is supplied to all record lines but one.  The suffix options for a same
 * suffix must differ each other.  If a suffix option is given when getting a
 * <tt>Suffix</tt> object from the SuffixFactory, an object corresponding the
 * record which has the suffix option is returned.  A <i>suffixspec</i>
 * without a suffix option is a default record of the suffix.  <i>language
 * name</i> is a name of a programming language in which the file of the
 * suffix is written.  The <i>language name</i> can be used to distinguish the
 * programming language in compilation process when more than one programming
 * language is used to build a program.  <i>description</i> is a description
 * of this suffix.  Do not include commas in the <i>description</i>.
 * <i>afterpreprocess</i>, <i>aftercompile</i> and <i>afterassemble</i> are
 * suffix strings of output files of preprocessing, compilation and assemble.
 * A `-' can be specified when corresponding pass is not required for files
 * with this suffix.  Following is a sample suffix database file:
 *
 * <pre>
 * #SRD, 2, Suffix rule DB file, format version 2
 * c,		C,		C source,				i,s,o
 * i,		C,		preprocessed C source,			-,s,o
 * cc/cpp/cxx/C,C++,		C++ source,				ii,s,o
 * ii,		C++,		preprocessed C++ source,		-,s,o
 * java,	Java,		Java source,				-,class,-
 * java(native),Java,		Java source (native compile),		-,s,o
 * f,		FORTRAN,	FORTRAN source,				-,s,o
 * S,		Assembler,	assembly source (need preprocess),	s,-,o
 * s,		Assembler,	assembly source,			-,-,o
 * </pre>
 *
 * The third record line tells that there are four different suffixes which
 * represent C++ source file and preprocess, compilation and assemble are
 * required to process it.  The sixth record line tells that when
 * <tt>native</tt> option is given, a <tt>.java</tt> file is compiled into
 * an assembler source file.
 **/

public class SuffixFactory {
  /**
   * A default suffix database file name.
   **/
  public static final String DEFAULT_SUFFIX_FILE = "suffixes";

  private static final char COLUMN_DELIMITER = ',';
  private static final char SUFFIX_SPECS_DELIMITER = '/';
  private static final String OBJECT = "o";

  private static Map fSuffixTable;
  private static Suffix fObjectFileSuffix;

  private static String defaultSuffixRule = 
"#SRD, 2, Suffix rule DB file, format version 2\n" +
"c,		C,		C source,				i,s,o\n"  +
"c(out-newlir),	C,		C source,				i,lir,-\n" +
"i,		C,		preprocessed C source,			-,s,o\n"  +
"cc/cpp/cxx/C,	C++,		C++ source,				ii,s,o\n" +
"ii,		C++,		preprocessed C++ source,		-,s,o\n"  +
"java,		Java,		Java source,				-,class,-\n" +
"java(native),	Java,		Java source (native compile),		-,s,o\n"  +
"f,		FORTRAN,	FORTRAN source,				-,s,o\n"  +
"lir,		LIR,		new LIR,				-,s,o\n" +
"S,		Assembler,	assembly source (need preprocess),	s,-,o\n"  +
"s,		Assembler,	assembly source,			-,-,o\n";

  static void defaultInitialize(CompileSpecification pSpec) {
    pSpec.getTrace().trace("Driver", 9,
			   "The inner default is used for suffix rule");
    initialize(defaultSuffixRule, pSpec);
  }

  static void initialize(CompileSpecification pSpec) {
    initialize(new File(DEFAULT_SUFFIX_FILE), pSpec);
  }

  static void initialize() {initialize(new CommandLine());}

  private static List splitString(String pString, char pDelimiter) {
    ArrayList lBuffer = new ArrayList();
    int lLen = pString.length();
    int lIndex = 0;

    while (lIndex < lLen) {
      String lNextPart;
      int lDelimiterIndex = pString.indexOf(pDelimiter, lIndex);
      if (lDelimiterIndex == -1) {
	lNextPart = pString.substring(lIndex).trim();
	lIndex = lLen;
      } else {
	lNextPart = pString.substring(lIndex, lDelimiterIndex).trim();
	lIndex = lDelimiterIndex + 1;
      }
      lBuffer.add(lNextPart);
    }
    return lBuffer;
  }

  static boolean initialize(BufferedReader pReader,
			    String pFile, CompileSpecification pSpec)
    throws IOException
  {
    fSuffixTable = new Hashtable();
    String line;
    line = pReader.readLine();
    List lFields = splitString(line, COLUMN_DELIMITER);
    int formatVersion;
    if ((lFields.size() >= 2) && lFields.get(0).equals("#SRD")) {
      formatVersion = Integer.parseInt((String)lFields.get(1));
    } else {
      pSpec.getWarning().warning("Driver",
				 "Suffix file ("
				 + pFile
				 + ") format error: "
				 + (String)lFields.get(0));
      return false;
    }
    if (formatVersion == 2) {
      while ((line = pReader.readLine()) != null) {
	lFields = splitString(line, COLUMN_DELIMITER);
	switch (lFields.size()) {
	case 0:
	  /* skip empty line */
	  break;
	case 6:
	  List lSuffixStrings = splitString((String)lFields.get(0),
					    SUFFIX_SPECS_DELIMITER);
	  Iterator i = lSuffixStrings.iterator();
	  while (i.hasNext()) {
	    String lSuffixSpec = (String)i.next();
	    String lSuffixString;
	    String lSuffixOption;
	    if (lSuffixSpec.endsWith(")")) {
	      lSuffixString
		= lSuffixSpec.substring(0, lSuffixSpec.indexOf('(')).trim();
	      lSuffixOption
		= lSuffixSpec.substring(lSuffixSpec.indexOf('(') +1,
					lSuffixSpec.lastIndexOf(')')).trim();
	    } else {
	      lSuffixString = lSuffixSpec;
	      lSuffixOption = null;
	    }
	    Suffix s = new Suffix(lSuffixString,
				  lSuffixOption,
				  (String)lFields.get(1),
				  (String)lFields.get(2),
				  (String)lFields.get(3),
				  (String)lFields.get(4),
				  (String)lFields.get(5),
				  ! ((String)lFields.get(3)).equals("-"),
				  ! ((String)lFields.get(4)).equals("-"),
				  ! ((String)lFields.get(5)).equals("-"));
	    s.checkIntegrity(pSpec);
	    if (lSuffixOption == null) {
	      fSuffixTable.put(lSuffixString, s);
	    } else {
	      fSuffixTable.put(lSuffixString + "(" + lSuffixOption + ")", s);
	    }
	  }
	  break;
	default:
	  pSpec.getWarning().warning("Driver",
				     "Suffix file ("
				     + pFile
				     + ") format error: "
				     + (String)lFields.get(0));
	  break;
	}
      }
      fObjectFileSuffix = new Suffix("o", null, "object", "Object file", "-", "-", "-",
				     false, false, false);
      return true;
    } else {
      pSpec.getWarning().warning("Driver",
				 "Suffix file ("
				 + pFile
				 + ") unknown format version: "
				 + formatVersion);
      return false;
    }
  }

  static void initialize(File pFile, CompileSpecification pSpec) {
    try {
      BufferedReader lReader = new BufferedReader(new FileReader(pFile));
      if (! initialize(lReader, pFile.getAbsolutePath(), pSpec)) {
	defaultInitialize(pSpec);
      }
    } catch (IOException lException) {
      pSpec.getWarning().warning("Driver",
				 "Cannot read suffix database file. "
				 + "Using default setting: "
				 + lException.getMessage());
      defaultInitialize(pSpec);
    }
  }

  static void initialize(String pString, CompileSpecification pSpec) {
    try {
      StringReader lReader = new StringReader(pString);
      if (! initialize(new BufferedReader(lReader), "default suffix rule",
		       pSpec)) {
	throw new FatalError("Internal error: a default suffix rule is wrong.");
      }
    } catch (IOException lException) {
      throw new FatalError("Internal error: IOException with StringReader");
    }
  }

  static void initialize(InputStream pIn, String pFile,
			 CompileSpecification pSpec) {
    try {
      if (! initialize(new BufferedReader(new InputStreamReader(pIn)),
		       pFile, pSpec)) {
	defaultInitialize(pSpec);
      }
    } catch (IOException lException) {
      pSpec.getWarning().warning("Driver",
				 "Cannot read suffix database file. "
				 + "Using default setting: "
				 + lException.getMessage());
      defaultInitialize(pSpec);
    }
  }

  /**
   *
   * Returns a <tt>Suffix</tt> object representing a suffix of a specified
   * file name.  Uses the default record when more than one record is found in
   * the database file.  If no record matches to the suffix of the specified
   * file name, it is treated as an object file name and a <tt>Suffix</tt>
   * object representing an object file suffix is returned.
   *
   * @param file a file name.
   * @return a Suffix object.
   **/
  public static Suffix getSuffix(File file) {
    return getSuffix(file, null);
  }

  /**
   *
   * Returns a <tt>Suffix</tt> object representing a suffix of a specified
   * file name.  If a non-<tt>null</tt> option is specified, a record with
   * that option is searched first, and if not found, the default record is
   * searched.  If no record matches to the suffix of the specified file name,
   * it is treated as an object file name and a <tt>Suffix</tt> object
   * representing an object file suffix is returned.
   *
   * @param file a file name.
   * @return a Suffix object.
   **/
  public synchronized static Suffix getSuffix(File file, String option) {
    if (fSuffixTable == null) {
      initialize();
    }
    String lName = file.getName();
    int lIndex = lName.lastIndexOf('.');
    String lSuffix;
    if (lIndex == -1) {
      lSuffix = "";
    } else {
      lSuffix = lName.substring(lIndex + 1);
    }
    return getSuffix(lSuffix, option);
  }

  /**
   *
   * Returns a <tt>Suffix</tt> object representing a specified suffix string.
   * Uses the default record when more than one record is found in the data
   * base file.  If no record matches to the specified suffix string, it is
   * treated as an object file suffix and a <tt>Suffix</tt> object
   * representing an object file suffix is returned.
   *
   * @param suffixString a suffix string.
   * @return a Suffix object.
   **/
  public static Suffix getSuffix(String suffixString) {
    return getSuffix(suffixString, null);
  }

  /**
   *
   * Returns a <tt>Suffix</tt> object representing a specified suffix string.
   * If a non-<tt>null</tt> option is specified, a record with that option is
   * searched first, and if not found, the default record is searched.  If no
   * record matches to the specified suffix string, it is treated as an object 
   * file suffix and a <tt>Suffix</tt> object representing an object file
   * suffix is returned.
   *
   * @param suffixString a suffix string.
   * @param option a suffix option string.
   * @return a Suffix object.
   **/
  public static Suffix getSuffix(String suffixString, String option) {
    if ((option != null) && ! option.equals("")) {
      String lSuffixSpec = suffixString + "(" + option + ")";
      if (fSuffixTable.containsKey(lSuffixSpec)) {
	return (Suffix)fSuffixTable.get(lSuffixSpec);
      }
    }
    if (fSuffixTable.containsKey(suffixString)) {
      return (Suffix)fSuffixTable.get(suffixString);
    } else {
      return fObjectFileSuffix;
    }
  }
}
