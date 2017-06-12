/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.IoRoot;
import coins.Registry;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.Hashtable;
import java.util.Set;

/**
 * Verify command line options whether they are
 * listed in Registry or not.
 * In case of adding new options or new option-items,
 * record them in Registry.java, and if they take
 * suboptions or option-items, add such statement as
 *   lIsCorrect &= checkCoinsOptions("hirOpt", Registry.HIR_OPT);
 * or
 *   lIsCorrect &= checkOptions("target", Registry.ARCH, ',', '=');
 * to isCoinsOptionsAreCorrect.
 * The method checkCoinsOptions is used for options having delimiter '/'
 * in such way as
 *   hirOpt=cf/cpf/cse
 * and the method checkOptions is used for options that do not include
 * delimiter '/', for example
 *   target=sparc-v8.
 */
public class CheckOptions
{
  public final IoRoot ioRoot;
  private static final char OPTION_DELIMITER = ',';
  private static final char VALUE_DELIMITER = '=';

  protected CompileSpecification fCompileSpecification;
  protected CoinsOptions fCoinsOptions;
  protected List fCompilerOptions;
  protected List fPreprocessorOptions;
  protected List fAssemblerOptions;
  protected List fLinkerOptions;
  protected int fDbgLevel;

public
CheckOptions( CompileSpecification pCompileSpecification,
                 IoRoot pIoRoot )
{
  ioRoot = pIoRoot;
  fCompileSpecification = pCompileSpecification;
  fCoinsOptions = fCompileSpecification.getCoinsOptions();
  /*
  fCompilerOptions = fCompileSpecification.getCompilerOptions();
  fAssemblerOptions = fCompileSpecification.getAssemblerOptions();
  fPreprocessorOptions = fCompileSpecification.getPreprocessorOptions();
  fLinkerOptions = fCompileSpecification.getLinkerOptions();
  */
 fDbgLevel = ioRoot.dbgControl.getLevel();
 if (fDbgLevel > 0)
  ioRoot.dbgControl.print(2,"\nCheckoptions " + pCompileSpecification); //##78

}

  /**
   * Check command line options assuming that they take
   * one of the forms
   *   -optionName
   *   -optionName:subOption1,subOptions,...
   *   -optionName:subOption=item1/item2/...,subOption2=item3/item4....
   * return true if option name and corresponding item name
   * are listed in Registry.java.
   * @return true if options are listed in Registry,
   *   return false if there is some option/item not listed in Registry.
   */
public boolean
isOptionsAreCorrect()
{
  boolean lIsCorrect = false;
  //-- Check compiler options
  Map lCommandLineOptions
    = ((CommandLine)fCompileSpecification).commandLineOptions();
  if (fDbgLevel > 0)
    ioRoot.dbgControl.print(2, "\n command line option " //##78
      + lCommandLineOptions + "\n");
    // Command line may be very long if it contains *.c, etc. //##78
  Set lKeySet = lCommandLineOptions.keySet();
  for (Iterator lIterator = lKeySet.iterator();
       lIterator.hasNext(); ) {
    String lOptionName = (String)lIterator.next();
    if (fDbgLevel > 0)
      ioRoot.dbgControl.print(2, "\noption " + lOptionName);
    if (lOptionName.charAt(0) == '-')
      lOptionName = lOptionName.substring(1);
    if (! isPrefixListed(lOptionName, Registry.OPTION)) {
      lIsCorrect = false;
      ioRoot.msgWarning.put("Undefined option " + lOptionName);
    }
  }
  //-- Check coins options
  if (! isCoinsOptionsAreCorrect())
    lIsCorrect = false;
  return lIsCorrect;
}
/**
 * Check options specified in -coins:....
 * @return true if no undefined one is found.
 */
public boolean
isCoinsOptionsAreCorrect()
{
  boolean lIsCorrect = true;
  // String lListOfItemValues;
  // Map lItemValue;
  Set lKeySet = fCoinsOptions.getOptionKeys();
  for (Iterator lIt = lKeySet.iterator();
       lIt.hasNext(); ) {
    String lOptionName = ((String)lIt.next()).intern();
    if (fDbgLevel > 0)
      ioRoot.dbgControl.print(2, "\noptionName=" + lOptionName);
    if (lOptionName == "item_key_list")
      continue;
    if (! isListed(lOptionName, Registry.COINS)) {
      lIsCorrect = false;
      ioRoot.msgWarning.put("Undefined option name " + lOptionName);
    }
  }
  lIsCorrect &= checkCoinsOptions("trace", Registry.TRACE);
  lIsCorrect &= checkCoinsOptions("hirOpt", Registry.HIR_OPT);
  lIsCorrect &= checkCoinsOptions("ssa-opt", Registry.SSA_OPT);
  lIsCorrect &= checkCoinsOptions("lir-opt", Registry.LIR_OPT);  //##96
  lIsCorrect &= checkCoinsOptions("simulate", Registry.SIMULATE_OPT); //##74
  lIsCorrect &= checkOptions("attach", Registry.ATTACH, ',', '=');
  lIsCorrect &= checkOptions("target", Registry.ARCH, ',', '=');
  lIsCorrect &= checkOptions("hir2c", Registry.HIR2C, ',', '=');
  lIsCorrect &= checkOptions("lir2c", Registry.LIR2C, ',', '=');
  // lIsCorrect &= checkCoinsOptions("barch", Registry.ARCH);
  // lIsCorrect &= checkCoinsOptions("b", Registry.ARCH);
  //
  //---- Add statements for options having item list. ----//
  return lIsCorrect;
} // isCoinsOptionsAreCorrect

protected boolean
checkCoinsOptions( String pItemName, String pValueList[])
{
  boolean lIsCorrect = false;
  if (pItemName != null) {
    String lListOfItemValues = fCoinsOptions.getArg(pItemName);
    if (lListOfItemValues == null) {
      // This option is not specified.
      return true;
    }
    Map lItemValue = fCoinsOptions.parseArgument(lListOfItemValues, '/', '.');
    List lItemKeyList = (List)lItemValue.get("item_key_list");
    if (fDbgLevel > 0)
      ioRoot.dbgControl.print(2, "\nitemKeyList= "
        + pItemName + " " +lItemKeyList);
    for (Iterator lIterator = lItemKeyList.iterator();
         lIterator.hasNext(); ) {
      String lValueName = (String)lIterator.next();
      if (! isListed(lValueName, pValueList)) {
        lIsCorrect = false;
        ioRoot.msgWarning.put("Undefined option item " + lValueName);
      }
    }
  }
  return lIsCorrect;
  } // checkCoinsOptions

  protected boolean
  checkOptions( String pItemName, String pValueList[],
                char pOptionDelimiter, char pValueDelimiter)
  {
    boolean lIsCorrect = false;
    if (pItemName != null) {
      String lListOfItemValues = fCoinsOptions.getArg(pItemName);
      if (lListOfItemValues == null) {
        // This option is not specified.
        return true;
      }
      Map lItemValue = fCoinsOptions.parseArgument(lListOfItemValues,
       pOptionDelimiter, pValueDelimiter );
      List lItemKeyList = (List)lItemValue.get("item_key_list");
      if (fDbgLevel > 0) {
        ioRoot.dbgControl.print(2, "\nlistOfItemValues "
          + pItemName + " " + lListOfItemValues + " itemValue " + lItemValue);
        ioRoot.dbgControl.print(2, "\n itemKeyList= "
          + lItemKeyList);
      }
      //##75 BEGIN
      if (pItemName.intern() == "attach") {
       lItemKeyList = separateByDelimiter((String)lItemKeyList.get(0), "/");
      }
      //##75 END
      for (Iterator lIterator = lItemKeyList.iterator();
           lIterator.hasNext(); ) {
        String lValueName = (String)lIterator.next();
        if (! isListed(lValueName, pValueList)) {
          lIsCorrect = false;
          ioRoot.msgWarning.put("Undefined option item " + lValueName);
        }
      }
    }
    return lIsCorrect;
    } // checkOptions

public boolean
isListed( String pItemValue, String pItemValueList[])
{
  if ((pItemValue != null)&&(pItemValueList != null)) {
    String lItemValue = pItemValue.intern();
    if (fDbgLevel > 0)
      ioRoot.dbgControl.print(3, "itemValue= " + lItemValue);
    for (int i = 0; i < pItemValueList.length; i++) {
      if (lItemValue == pItemValueList[i]) {
        return true;
      }
    }
  }
  return false;
} // isListed

public boolean
isPrefixListed( String pItemValue, String pItemValueList[])
{
  if ((pItemValue != null)&&(pItemValueList != null)) {
    for (int i = 0; i < pItemValueList.length; i++) {
      if (pItemValue.startsWith(pItemValueList[i])) {
        return true;
      }
    }
  }
  return false;
} // isListed

//##75 BEGIN
/**
 * Get the set of substrings contained in pText where each
 * substring is separated by the delimiting string pDelimiter.
 * e.g.
 *   separateByDelimiter("coins.backend.sched.Schedule/RegPromote", "/")
 *   will return {"coins.backend.sched.Schedule", "RegPromote"}
 * This can be made static method by erazing debug statement
 *   ioRoot.dbgControl.print( ... );
 * @param pText text to be divided into substrings.
 * @param lDelimiter delimiting marker.
 * @return the set of substrings.
 */
public List
separateByDelimiter( String pText, String pDelimiter )
{
  ArrayList lList = new ArrayList();
  int lHeadIndex, lTailIndex, lLength;
  String lSubstring;
  lHeadIndex = 0;
  if (pText != null) {
    lLength = pText.length();
    while (lHeadIndex < lLength) {
      lTailIndex = pText.indexOf(pDelimiter, lHeadIndex);
      if (lTailIndex <= 0)
        lTailIndex = lLength;
      lSubstring = pText.substring(lHeadIndex, lTailIndex);
      lList.add(lSubstring);
      lHeadIndex = lTailIndex + pDelimiter.length();
    }
  }
  ioRoot.dbgControl.print(2, "\nseparateByDelimiter "
    + pText+ "="  + lList);
  return lList;
} // separateByDelimiter
//##75 END

} // CheckOptions
