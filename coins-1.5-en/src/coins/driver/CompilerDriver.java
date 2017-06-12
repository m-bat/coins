/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 *
 * A framework of COINS compiler driver.<br>
 *
 * A CompilerDriver object represents an execution of whole compilation
 * process which is divided into four passes: preprocess, compile, assemble
 * and link.  An object which implements CompilerImplementation must be
 * provided to implement these four passes.<br>
 *
 * The semantics of driver control options such as -E, -c, -pipe, etc. are
 * determined by this class.
 *
 **/

public final class CompilerDriver {
  private static final File DEFAULT_TEMP_DIR = new File("/tmp");
                  /* As for Windows, how should we do? */

  private static final String PARALLEL_COMPILE_OPTION = "compile-parallel";

  private static final String FACTORY_CLASS_NAME
    = "coins.driver.SuffixFactory";

  private CompileSpecification fSpec;
  private TemporaryFileManager fTemporaryFileManager;

  private void initializeSuffixFactory() {
    File lSuffixFile;
    CoinsOptions lCoinsOptions = fSpec.getCoinsOptions();
    String lSuffixFilePathOption = lCoinsOptions.SUFFIX_FILE_PATH;
    Trace lTrace = fSpec.getTrace();

    if (lCoinsOptions.isSet(lSuffixFilePathOption)) {
      lTrace.trace("Driver", 9, "Using -coins:suffix option for suffix rules");
      String lSuffixFileName
	= (String)lCoinsOptions.getArg(lSuffixFilePathOption);
      SuffixFactory.initialize(new File(lSuffixFileName), fSpec);
    } else {
      String lSuffixFileName = SuffixFactory.DEFAULT_SUFFIX_FILE;
      lSuffixFile = new File(lCoinsOptions.getLibDir(), lSuffixFileName);
      if (lSuffixFile.isFile()) {
	lTrace.trace("Driver", 9,
		     "Using libdir/suffixes for suffix rules");
	SuffixFactory.initialize(lSuffixFile, fSpec);
      } else {
	String lFile = FACTORY_CLASS_NAME + File.separator + lSuffixFileName;
	InputStream lIn;
	try {
	  lTrace.trace("Driver", 9,
		       "Trying attached file for suffix rules");
	  Class lClass = Class.forName(FACTORY_CLASS_NAME);
	  lIn = getAttachedFileInputStream(lClass, lSuffixFileName);
	  SuffixFactory.initialize(lIn, lFile, fSpec);
	} catch (IOException lIOException) {
	  fSpec.getWarning()
	    .warning("Driver",
		     "Cannot read suffix database file ("
		     + lFile
		     + "). Using default setting: "
		     + lIOException.getMessage());
	  SuffixFactory.defaultInitialize(fSpec);
	} catch (ClassNotFoundException lClassNotFoundException) {
	  throw new NoClassDefFoundError(lClassNotFoundException.getMessage());
	}
      }
    }
  }

  /**
   *
   * A subroutine of getAttachedFileInputStream(Class, String).
   **/
  private static boolean searchClass(Class clazz, String cpe)
  throws IOException {
    File d = new File(cpe);
    String cname = clazz.getName();
    if (d.isDirectory()) {
      File f
	= new File(cpe + File.separator
		   + cname.replace('.', File.separatorChar) + ".class");
      return f.isFile();
    } else {
      JarFile jf = new JarFile(cpe);
      return jf.getJarEntry(cname.replace('.', '/') + ".class") != null;
    }
  }

  /**
   *
   * A subroutine of getAttachedFileInputStream(Class, String).
   **/
  private static InputStream getAttachedFileInputStream(String cpe,
							Class clazz,
							String path)
  throws IOException {
    File d = new File(cpe);
    String cname = clazz.getName();
    String pname;
    int i;
    if ((i = cname.lastIndexOf('.')) == -1) {
      pname = "";
    } else {
      pname = cname.substring(0, i);
    }
    if (d.isDirectory()) {
      File p = new File(cpe + File.separator
			+ pname.replace('.', File.separatorChar)
			+ File.separator + path);
      return new FileInputStream(p);
    } else {
      JarFile jf = new JarFile(cpe);
      String fname
	= pname.replace('.', '/') + '/'
	+ path.replace(File.separatorChar, '/');
      ZipEntry ze = jf.getJarEntry(fname);
      if (ze == null) {
	throw new FileNotFoundException(fname);
      } else {
	return jf.getInputStream(ze);
      }
    }
  }

  /**
   *
   * Returns an InputStream of a file, which is in a directory where a loadable
   * class file is in it (or one of sub-directories of the directory).  Note
   * that the file and the directory may be on a jar/zip file.
   *
   * @param clazz a class object the class
   * @param path a relative path of the directory where the file exists
   * @return the InputStream object.
   * @throws IOException failed to get InputStream
   * @throws ClassNotFoundException failed to find `clazz' from class-path.
   **/

  public static InputStream getAttachedFileInputStream(Class clazz,
						       String path)
  throws ClassNotFoundException, IOException {
    String classpath = System.getProperty("java.class.path");
    int i;
    while ((i = classpath.indexOf(":")) != -1) {
      String segment = classpath.substring(0, i);
      if (searchClass(clazz, segment)) {
	return getAttachedFileInputStream(segment, clazz, path);
      } else {
	classpath = classpath.substring(i + 1);
      }
    }
    if (searchClass(clazz, classpath)) {
      return getAttachedFileInputStream(classpath, clazz, path);
    } else {
      throw new ClassNotFoundException(clazz.getName());
    }
  }

  /**
   * Constructs a framework object.
   *
   * @param args a command line given to the <tt>main()</tt>.
   * @param implementation user defined driver object.
   **/
  public CompilerDriver(CompileSpecification spec) {
    fSpec = spec;
    fTemporaryFileManager = new TemporaryFileManager();
    initializeSuffixFactory();
  }

  private void preserveTemporaryFiles() {
    Collection lTemporaryFiles = fTemporaryFileManager.getTemporaryFiles();

    if (lTemporaryFiles.size() > 0) {
      System.err.println("Following temporary files are preserved:");
      Iterator i = lTemporaryFiles.iterator();
      while (i.hasNext()) {
	File lFile = (File)i.next();
	System.err.println("  " + lFile.getAbsolutePath());
      }
    }
  }

  private void cleanup() {
    CoinsOptions lCoinsOptions = fSpec.getCoinsOptions();
    if ((! lCoinsOptions.isSet(lCoinsOptions.DEBUG))
	&& (! lCoinsOptions.isSet(lCoinsOptions.PRESERVE_FILES))) {
      fSpec.getTrace().trace("Driver", 5000, "cleaning up...");
      fTemporaryFileManager.cleanupTemporaryFiles();
    }
  }

  /**
   * Drives compilation processes: preprocess, compile, assemble and link.
   *
   * @param implementation a compiler implementation
   * @return 0 if no error, 1 otherwise
   **/
  public int go(CompilerImplementation implementation) {
    CompileStatus status = new CompileStatus();

    if (fSpec.isSet(fSpec.HELP)) {
      fSpec.showHelp(System.out, implementation);
      return 0;
    }
    fSpec.getTrace().trace("Driver", 5000, fSpec.toString());
    try {
      Iterator i = fSpec.getSourceFiles().iterator();
      fSpec.getTrace().trace("Driver", 5000, "go(1)");
      if (i.hasNext()) {
	fSpec.getTrace().trace("Driver", 5000, "go(2)");
	CompileStatus staus = new CompileStatus();
	Collection lThreads = new ArrayList();
	Thread lThread;
	while (i.hasNext()) {
	  lThread = new CompileThread((String)i.next(), fSpec,
				      fTemporaryFileManager, implementation,
				      status, false);
	  lThread.start();
	  if (fSpec.getCoinsOptions().isSet(PARALLEL_COMPILE_OPTION)) {
	    lThreads.add(lThread);
	  } else {
	    lThread.join();
	  }
	}
	i = lThreads.iterator();
	while (i.hasNext()) {
	  ((Thread)i.next()).join();
	}
	if (status.isLinkingRequired()) {
	  lThread = new CompileThread("", fSpec, fTemporaryFileManager,
				      implementation, status, true);
	  lThread.start();
	  lThread.join();
	} else {
	  fSpec.getTrace().trace("Driver", 5000, "Linking is cancelled.\n");
	}
      } else {
	/* No input files specified. */
	System.err.println("No input files.");
	status.setABEND();
      }
    } catch (InterruptedException lInterruptedException) {
      status.setABEND();
    } finally {
      cleanup();
    }
    return status.getExitStatus();
  }
}
