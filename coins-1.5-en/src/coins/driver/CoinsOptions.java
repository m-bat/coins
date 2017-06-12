/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList; //##64
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map; //##64
import java.util.Hashtable; //##64
import java.util.Set; //##71



/**
 * An abstraction of COINS options at a command line.<br>
 * Standard COINS options (which means the options that affects driver API
 * objects) are listed below:
 * <ul>
 *   <li><tt>-coins:debug</tt><br>
 *       Debugging compiler itself.  Temorary files are not cleared.
 *   <li><tt>-coins:trace=</tt><i>tracespecs</i><br>
 *       See class <tt>Trace</tt> in more detail.
 *   <li><tt>-coins:libdir=</tt><i>path</i><br>
 *       COINS Compiler Driver API reads several configuration files from a
 *       library directory.  This option can change the library directory
 *       path.
 *   <li><tt>-coins:property=</tt><i>path</i><br>
 *       COINS Compiler Driver API reads a property file which is named
 *       <i>libdir</i><tt>/properties</tt> by default, where <i>libdir</i> is
 *       the library directory.  This option can change the property file
 *       path.
 *   <li><tt>-coins:suffix=</tt><i>path</i><br>
 *       COINS Compiler Driver API reads a suffix database file which is
 *       named <i>libdir</i><tt>/suffixes</tt> by default, where <i>libdir</i>
 *       is the library directory.  This option can change the suffix data
 *       base file path.
 *   <li><tt>-coins:suffixoption=</tt><i>option</i><br>
 *       COINS Compiler Driver API reads records with specified <i>option</i>
 *       from a suffix database file.
 * </ul>
 * More than one COINS options can be specifed by delimiting them by `,'s,
 * e.g. <tt>-coins:debug,trace=Driver.8/2,suffix=/tmp/mysuffixes</tt>.<br>
 *
 * Although standard COINS options have an constant representation in this
 * class, users can define and use any option and argument of it.<br>
 **/

public class CoinsOptions {
  /**
   * The default library directory name at a user home directory.
   **/
  public static final String DEFAULT_LIBDIR_NAME_AT_HOME = "coins";

  /**
   * The default library directory path when a directory `coins' doesn't
   * exist at a user home directory.
   **/
  public static final String DEFAULT_LIBDIR = ".";

  /**
   * The default property file name.
   **/
  public static final String DEFAULT_PROPERTY_FILENAME = "properties";

  /**
   * The option string to tell that the compiler is under debugging.<br>
   **/
  public static final String DEBUG = "debug";

  /**
   * The option string to preserve temporary files.<br>
   **/
  public static final String PRESERVE_FILES = "preserveFiles";

  /**
   * The "library directory" option string.
   **/
  public static final String LIBDIR = "libdir";

  /**
   * The "property file path" option string.
   **/
  public static final String PROPERTY_FILE = "property";

  /**
   * The "suffix database file path" option string.
   **/
  public static final String SUFFIX_FILE_PATH = "suffix";

  /**
   * The "suffix option" option string.
   **/
  public static final String SUFFIX_OPTION = "suffixoption";

  /**
   * The "trace" option string.
   **/
  public static final String TRACE = "trace";

  private static final char OPTION_DELIMITER = ',';
  private static final char VALUE_DELIMITER = '=';

  private Properties fOptions;
  private final File fLibDir;
  protected String fArgumentString; //##71

  private void parseArgument(String pArgumentString, Properties pTable) {
    String lArgument = pArgumentString.trim();
    int lIndex = 0;
    int lLen = lArgument.length();
    while (lIndex < lLen) {
      int lOptionDelimiterIndex = lArgument.indexOf(OPTION_DELIMITER, lIndex);
      String lNextOption;
      if (lOptionDelimiterIndex == -1) {
  lNextOption = lArgument.substring(lIndex);
  lIndex = lLen;
      } else {
  lNextOption = lArgument.substring(lIndex, lOptionDelimiterIndex);
  lIndex = lOptionDelimiterIndex + 1;
      }
      int lValueDelimiterIndex = lNextOption.indexOf(VALUE_DELIMITER);
      String lOption;
      String lValue;
      if (lValueDelimiterIndex == -1) {
  lOption = lNextOption;
  lValue = "";
      } else {
  lOption = lNextOption.substring(0, lValueDelimiterIndex);
  lValue = lNextOption.substring(lValueDelimiterIndex + 1);
      }
      pTable.setProperty(lOption, lValue);
    }
  }

  private String searchLibDirAtHome() {
    String dpath
      = System.getProperty("user.home") + File.separator
      + DEFAULT_LIBDIR_NAME_AT_HOME;
    File d = new File(dpath);
    if (d.isDirectory()) {
      return dpath;
    } else {
      return null;
    }
  }

  private File libDir(Properties pOptions) {
    File lLibDir;
    String lLibDirName = pOptions.getProperty(LIBDIR);

    if (lLibDirName != null) {
      lLibDir = new File(lLibDirName);
    } else if ((lLibDirName = searchLibDirAtHome()) != null) {
      lLibDir = new File(lLibDirName);
    } else {
      lLibDir = new File(DEFAULT_LIBDIR);
    }
    return lLibDir;
  }

  private Properties parsePropertyFile(File pPropertyFile, Warning pWarning) {
    Properties lProperties = new Properties();
    try {
      InputStream pIn = new FileInputStream(pPropertyFile);
      lProperties.load(pIn);
    } catch (IOException lIOException) {
      pWarning.warning("Driver",
           "Cannot read property file ("
           + pPropertyFile.getAbsolutePath()
           + "): "
           + lIOException.getMessage());
    }
    return lProperties;
  }

  private void mergeTables(Properties dest, Properties src) {
    Enumeration pEnumeration = src.propertyNames();
    while (pEnumeration.hasMoreElements()) {
      String lKey = (String)pEnumeration.nextElement();
      dest.setProperty(lKey, src.getProperty(lKey));
    }
  }

  /**
   *
   * Constructs a CoinsOptions object from an argument string of a -coins
   * option.  Argument string is a comma separated option list excluding the
   * leading <tt>-coins:</tt>.
   *
   * @param argumentString an argument string.
   **/
  public CoinsOptions(String argumentString) {
    fArgumentString = argumentString; //##71
    fOptions = new Properties();
    parseArgument(argumentString, fOptions);
    fLibDir = libDir(fOptions);
  }

  /**
   *
   * Reads a property file if it exists and merges its contents to the option
   * table when they don't exist.
   *
   * @param pWarning warning message controler.
   **/
  public void readPropertyFile(Warning pWarning) {
    String lPropertyPath = fOptions.getProperty(PROPERTY_FILE);
    File lPropertyFile;
    if (lPropertyPath == null) {
      lPropertyFile = new File(fLibDir, DEFAULT_PROPERTY_FILENAME);
    } else {
      lPropertyFile = new File(lPropertyPath);
    }
    Properties lProperties = fOptions;
    fOptions = parsePropertyFile(lPropertyFile, pWarning);
    mergeTables(fOptions, lProperties);
  }

  /**
   * Tests if an option is set or not in this COINS option.
   *
   * @param pOption the option string (left side of a delimiting equal sign).
   * @return <tt>true</tt> if the option is set, <tt>false</tt> otherwise.
   **/
  public synchronized boolean isSet(String pOption) {
    return fOptions.getProperty(pOption) != null;
  }

  /**
   * Returns the argument of an option in this COINS option.  When the option
   * is not set, a <tt>null</tt> is returned.  When the option is set but no
   * argument is specified (i.e., no delimiting equal sign exists or no string
   * exists at right side of delimiting equal sign), a "" is returned.
   *
   * @param pOption the option string (left side of a delimitin equal sign).
   * @return the argument of <tt>option</tt> or <tt>null</tt>.
   **/
  public synchronized String getArg(String pOption) {
    return fOptions.getProperty(pOption);
  }

  /**
   * Sets a COINS option without argument.
   *
   * @param pOption an option to be set.
   **/
  public synchronized void set(String pOption) {
    fOptions.setProperty(pOption, "");
  }

  /**
   * Sets a COINS option with an argument.
   *
   * @param pOption an option to be set.
   * @param pArg an argument of the option.
   **/
  public synchronized void set(String pOption, String pArg) {
    fOptions.setProperty(pOption, pArg);
  }

  /**
   * Returns library directory path.  It is searched from the following
   * sources in this order:
   * <ol>
   *   <li> COINS options
   *   <li> property file (specified by "<tt>property=</tt><i>path</i>" way)
   *   <li> default value (i.e., ".");
   * </ol>
   *
   * @return library directory path.
   **/
  public synchronized File getLibDir() {
    return fLibDir;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();
    Enumeration e = fOptions.keys();
    if (e.hasMoreElements()) {
      s.append("-coins:");
      do {
  String key = (String)e.nextElement();
  s.append(key);
  String arg = fOptions.getProperty(key);
  if (! arg.equals("")) {
    s.append("=" + arg);
  }
  if (e.hasMoreElements()) {
    s.append(",");
  }
      } while (e.hasMoreElements());
    }
    return s.toString();
  }

//##64 BEGIN
  /** parseArgument
   * Get option categories doing division of pArgument by pCategoryDelimiter
   * and then record option items and corresponding option values
   * doing division of each option category by pValueDelimiter.
   * The return value also contains the list of option categories
   * which  can be get by get("item_key_list"). The list is ordered
   * in the same sequence as given in pArgument.
   * Example
   *   For compiler option
   *       -coins:hirOpt=loopexp.4/cpf/pre,trace=HIR.1
   *   by coding
   *       CoinsOptions lOptions = ioRoot.getCompileSpecification().getCoinsOptions();
   *   lOption will represent information specified by
   *       hirOpt=loopexp.4/cpf/pre,trace=HIR.1
   *   and by coding
   *       String lOptArg = lOptions.getArg("hirOpt");
   *   lOptArg will represent "loopexp.4/cpf/pre,trace=HIR.1"
   *   and by coding
   *       Map lHirOptionItems = parseArgument(lOptArg, '/', '.');
   *   lHirOptionImems will contain key-value correspondence
   *     { ("loopexp", "4"), ("cpf", ""), ("pre", ""),
   *       ("item_key_list", ("loopexp", "cpf", "pre")) }
   * The coding of this method is similar to parseArgument of Trace class.
   * @param pArgument is a character string showing options.
   * @return the Map showing option item and option value correspondence.
   */
  public Map parseArgument(String pArgument,
                           char pCategoryDelimiter, char pValueDelimiter )
  {
    Map lTable = new Hashtable();
    List lKeyList = new ArrayList(); //##64
    String lArgument = pArgument.trim();
    int lIndex = 0;
    int lLen = lArgument.length();

    while (lIndex < lLen) {
      int lCategoryDelimiterIndex;
      lCategoryDelimiterIndex = lArgument.indexOf(pCategoryDelimiter, lIndex);
      String lNextArg;
      if (lCategoryDelimiterIndex == -1) {
        lNextArg = lArgument.substring(lIndex);
        lIndex = lLen;
      }
      else {
        lNextArg = lArgument.substring(lIndex, lCategoryDelimiterIndex);
        lIndex = lCategoryDelimiterIndex + 1;
      }
      parseItem(lNextArg, lTable, pValueDelimiter, lKeyList);
    }
    lTable.put("item_key_list", lKeyList);
    return lTable;
  } // parseArgument

  private void parseItem(String pTraceSpec, Map pTable, char pValueDelimiter,
      List pKeyList)
  {
    int lLevelDelimiterIndex = pTraceSpec.indexOf(pValueDelimiter);
    if (lLevelDelimiterIndex == -1) {
      pTable.put(pTraceSpec, "");
      pKeyList.add(pTraceSpec);
    } else {
      String lCategory = pTraceSpec.substring(0, lLevelDelimiterIndex);
      String lValueString = pTraceSpec.substring(lLevelDelimiterIndex + 1);
      pTable.put(lCategory, lValueString);
      pKeyList.add(lCategory);
       }
  } // parseItem

//##64 END

//##71 BEGIN
public Set
getOptionKeys()
{
  Map lOptionMap = parseArgument(fArgumentString, ',', '=');
  Set lOptionKeys = lOptionMap.keySet();
  return lOptionKeys;
}
//##71 END
} // CoinsOptions
