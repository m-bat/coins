/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.PassException;
import java.io.File;
import java.io.IOException;

/**
 *
 * An object of this class represents a `suffix rule' of a suffix.  Using the
 * object, whether a certain pass (preprocessing, compilation and assemble)
 * should be applied to a source file with the suffix or not can be inspeced
 * and a suffix of output file after the pass can be obtained.<br>
 *
 * There is no public constructor of this class.  Use class
 * <tt>SuffixFactory</tt> to obtain an instance.
 **/

public class Suffix {
  private String fSuffixString;
  private String fSuffixOption;
  private String fDescription;
  private String fLanguageName;
  private String fAfterPreprocess;
  private String fAfterCompile;
  private String fAfterAssemble;
  boolean fIsPreprocessRequired;
  boolean fIsCompileRequired;
  boolean fIsAssembleRequired;

  Suffix(String pSuffixString,
	 String pSuffixOption,
	 String pLanguageName,
	 String pDescription,
	 String pAfterPreprocess,
	 String pAfterCompile,
	 String pAfterAssemble,
	 boolean pIsPreprocessRequired,
	 boolean pIsCompileRequired,
	 boolean pIsAssembleRequired) {
    fSuffixString = pSuffixString;
    fSuffixOption = pSuffixOption;
    fDescription = pDescription;
    fLanguageName = pLanguageName;
    fAfterPreprocess = pAfterPreprocess;
    fAfterCompile = pAfterCompile;
    fAfterAssemble = pAfterAssemble;
    fIsPreprocessRequired = pIsPreprocessRequired;
    fIsCompileRequired = pIsCompileRequired;
    fIsAssembleRequired = pIsAssembleRequired;
  }

  /**
   *
   * Returns a description of this suffix.
   *
   * @return a description of this suffix.
   **/
  public String getDescription() {
    return fDescription;
  }

  /**
   *
   * Returns a name of a programming language in which the file of this suffix 
   * is written.
   *
   * @return a programming language name
   **/
  public String getLanguageName() {
    return fLanguageName;
  }

  /**
   *
   * Returns a suffix option, specified to this suffix rule.  If no suffix
   * option is specified, null is returned.
   *
   * @return suffix option, or null if not specified.
   **/
  public String getSuffixOption() {
    return fSuffixOption;
  }

  /**
   *
   * Returns a suffix of output file after preprocessing a file with this
   * suffix.
   *
   * @return a suffix of preprocessed file.
   **/
  public Suffix afterPreprocess() {
    return SuffixFactory.getSuffix(fAfterPreprocess);
  }

  /**
   *
   * Returns a suffix of output file after compilation of a file with this
   * suffix.
   *
   * @return a suffix of compiled file.
   **/
  public Suffix afterCompile() {
    return SuffixFactory.getSuffix(fAfterCompile);
  }

  /**
   *
   * Returns a suffix of output file after assembling of a file with this
   * suffix.
   *
   * @return a suffix of assembled file.
   **/
  public Suffix afterAssemble() {
    return SuffixFactory.getSuffix(fAfterAssemble);
  }

  /**
   *
   * Tests if a preprocessing is required for a source file with this suffix.
   *
   * @return <tt>true</tt> if a preprocessing is required, <tt>false</tt> otherwise.
   **/
  public boolean isPreprocessRequired() {
    return fIsPreprocessRequired;
  }

  /**
   *
   * Tests if a compilation is required for a source file with this suffix.
   *
   * @return <tt>true</tt> if a compilation is required, <tt>false</tt> otherwise.
   **/
  public boolean isCompileRequired() {
    return fIsCompileRequired;
  }

  /**
   *
   * Tests if an assembling is required for a source file with this suffix.
   *
   * @return <tt>true</tt> if an assembling is required, <tt>false</tt> otherwise.
   **/
  public boolean isAssembleRequired() {
    return fIsAssembleRequired;
  }

  /**
   *
   * Returns a string representation of this suffix.
   *
   * @return a string representation of this suffix.
   **/
  public String toString() {
    return fSuffixString;
  }

  private void sameSuffixWarning(String pPass,
				 String pOpt,
				 CompileSpecification pSpec) {
    Warning lWarning = pSpec.getWarning();
    lWarning.warning("Driver",
		     "Suffix rule record " + fSuffixString
		     + ((fSuffixOption == null)
			? "" : "(" + fSuffixOption + ")")
		     + ": a suffix after " + pPass
		     + " is same as a source suffix (i.e., specifying "
		     + pOpt + " will destroy the source file).");
  }

  /**
   *
   * Checks integrity conditions of this object.
   *
   * @param pSpec a CompileSpecification object.
   **/
  void checkIntegrity(CompileSpecification pSpec) {
    if (fSuffixString.equals(fAfterPreprocess)) {
      sameSuffixWarning("preprocess", "-E", pSpec);
    }
    if (fSuffixString.equals(fAfterCompile)) {
      sameSuffixWarning("compile", "-S", pSpec);
    }
    if (fSuffixString.equals(fAfterAssemble)) {
      sameSuffixWarning("assemble", "-c", pSpec);
    }
  }
}
