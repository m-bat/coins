/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.FatalError;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * Provides temporary file management facility.<br>
 *
 * A temporary file with any presix and suffix can be created.<br>
 *
 * More than one manager can work together at same time.
 **/

public class TemporaryFileManager {
  private static final String DEFAULT_PREFIX = "COINS";
  private static final String DEFAULT_POSTFIX = null;

  private Collection fTemporaryFiles;
  private int fCounter = 0;
  private String myName = "TemporaryFileManager";

  /**
   * Constructs a new manager.
   **/
  public TemporaryFileManager() {
    fTemporaryFiles = new ArrayList();
  }

  /**
   * Deletes all temporary files created by this manager.
   **/
  public synchronized void cleanupTemporaryFiles() {
    Iterator i = fTemporaryFiles.iterator();
    while (i.hasNext()) {
      File lFile = (File)i.next();
      if (lFile.isFile()) {
	lFile.delete();
      }
    }
  }

  /**
   *
   * Creates a temporary file and returns its path name.
   *
   * @return a temporary file path name.
   * @throws IOException - failed to create a file.
   **/
  public File createTemporaryFile() throws IOException {
    return createTemporaryFile(DEFAULT_POSTFIX);
  }

  /**
   *
   * Creates a temporary file with a specified suffix and returns its path
   * name.
   *
   * @param suffix a suffix.
   * @return a temporary file path name.
   * @throws IOException - failed to create a file.
   **/
  public synchronized File createTemporaryFile(String suffix)
  throws IOException {
    try {
      return createTemporaryFile(DEFAULT_PREFIX, suffix);
    } catch (IllegalArgumentException lIllegalArgumentException) {
      FatalError lFatalError
	= new FatalError(myName +
			 ": panic(IllegalArgumentException): " +
			 lIllegalArgumentException.getMessage());
      System.err.println(lFatalError.getMessage());
      throw lFatalError;
    }
  }

  /**
   *
   * Creates a temporary file with a specified prefix and suffix and returns
   * its path name.  The prefix length must be three or more.
   *
   * @param prefix a prefix.
   * @param suffix a suffix.
   * @return a temporary file path name.
   * @throws IllegalArgumentException - prefix length is short.
   * @throws IOException - failed to create a file.
   **/
  public synchronized File createTemporaryFile(String prefix, String suffix)
    throws IllegalArgumentException, IOException {
    File lFile = File.createTempFile(prefix, suffix);
    fTemporaryFiles.add(lFile);
    return lFile;
  }

  /**
   *
   * Returns a list of temporary files ever created by this manager.  The
   * list is a <tt>Collection</tt> of <tt>File</tt>s.
   *
   * @return a temporary file path name list.
   **/
  public synchronized Collection getTemporaryFiles() {
    Collection ret = new ArrayList();
    ret.addAll(fTemporaryFiles);
    return ret;
  }
}
