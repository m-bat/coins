/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.IoRoot;
import coins.PassException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * A compiler thread.<br>
 *
 * COINS Compiler Driver API executes compile steps of one compile unit by one 
 * thread.  If the option -conis:compile-paralle is specified, all the threads 
 * are concurrently executed.<br>
 *
 * The compiler modules executed in the thread can get the thread object by a
 * method Thread.currentThread(), whose return value can be narrowed to this
 * class.<br>
 *
 * Some services are provided by the thread object:
 * <ul>
 *   <li> getting IoRoot
 *
 *        IoRoot object is required in many situations.  Compiler modules can
 *        avoid to pass it from method to method by an argument, 
 *        </li>
 *   <li> thread local variables
 *
 *        Compiler modules can define, set, refer, and remove variables by
 *        their names.  These variables are defined threadwise, and have
 *        different values in a different thread.
 *        </li>
 *   <li> creating temporary files
 *
 *        Compiler modules can create temporary files, which are removed when
 *        the program exited.
 *        </li>
 *   <li> referring / setting exit status
 *
 *        exit status code can be set and its current value can be referred.
 *        </li>
 * </ul>
 **/

public class CompileThread extends Thread {
  private static final File DEFAULT_OUTPUT_FILE = new File("a.out");
  private static final int IO_BUFFER_SIZE = 4096;

  private String myName = "Driver"; /* used building Exception object */

  String fSourceFileName;
  boolean fIsLinkerThread;
  TemporaryFileManager fTemporaryFileManager;
  CompilerImplementation fImplementation;
  CompileSpecification fSpec;
  IoRoot fIoRoot;
  CompileStatus fStatus;
  Map fThreadLocalVariables;
  StopWatch fClock;

  CompileThread(String pSourceFileName,
		CompileSpecification pSpec,
		TemporaryFileManager pTemporaryFileManager,
		CompilerImplementation pImplementation,
		CompileStatus pStatus,
		boolean pIsLinkerThread) {
    fSourceFileName = pSourceFileName;
    fIsLinkerThread = pIsLinkerThread;
    fSpec = pSpec;
    fTemporaryFileManager = pTemporaryFileManager;
    fImplementation = pImplementation;
    fStatus = pStatus;
    fThreadLocalVariables = new HashMap();
    fClock = new StopWatch();
  }

  /**
   *
   * Gets an IoRoot object.
   *
   * @return the IoRoot object.
   **/
  public IoRoot getIoRoot() {return fIoRoot;}

  /**
   *
   * Gets the current value of the exit status.
   * <ul>
   *   <li> 0
   *
   *        No compile error is found so far, in this compile unit and the
   *        other compile units compiled so far (when the compilation is
   *        executed in parallel, no compile error is found in the other
   *        compile threads, so far),
   *        </li>
   *   <li> 1
   *
   *        At least one compile error is found so far, which may be in the
   *        other compile unit, and
   *        </li>
   *   <li> the other value
   *
   *        Can be set by the method setExitStatus().
   *        </li>
   * </ul>
   *
   * @return the exit status value.
   **/
  public int getExitStatus() {return fStatus.getExitStatus();}

  /**
   *
   * Sets a value representing `abnormal end' to the exit status.
   **/
  public void setABEND() {fStatus.setABEND();}

  /**
   *
   * Sets a value to the exit status of this compilation.
   *
   * @param s the status value
   **/
  public void setExitStatus(int s) {fStatus.setExitStatus(s);}

  /**
   *
   * Tests if a thread local variable is defined or not.
   *
   * @param varName the name of the variable
   * @return <tt>true</tt> if the variable has been defined, <tt>false</tt>
   * otherwise.
   **/
  public synchronized boolean isDefinedThreadLocalVariable(String varName) {
    return fThreadLocalVariables.containsKey(varName);
  }

  /**
   *
   * Gets the value of a thread local variable.
   *
   * @param varName the name of the variable.
   * @return the value of the variable if it has been defined, <tt>null</tt>
   * otherwise.
   **/
  public synchronized Object getThreadLocalVariableValue(String varName) {
    return fThreadLocalVariables.get(varName);
  }

  /**
   *
   * Sets a value to a thread local variable.  If the variable is has not been
   * defined yet, the variable is newly defined and the value is set.
   *
   * @param varName the name of the variable
   * @param the new value of the variable
   * @return the old value of the variable if it has been defined,
   * <tt>null</tt> otherwise.
   **/
  public synchronized Object setThreadLocalVariableValue(String varName,
							 Object value) {
    return fThreadLocalVariables.put(varName, value);
  }

  /**
   *
   * Removes a thread local variable.
   *
   * @param varName the name of the variable
   * @return the value of the variable if it has been defined, <tt>null</tt>
   * otherwise.
   **/
  public synchronized Object removeThreadLocalVariable(String varName) {
    return fThreadLocalVariables.remove(varName);
  }

  /**
   *
   * Returns an elapsed mili-seconds since this thread was started.
   *
   * @return elapsed time.
   **/
  public long elapsedTime() {
    return fClock.read();
  }

  /**
   *
   * Returns a <tt>File</tt> object representing a newly created temporary
   * file.  The file is removed when the compiler exited.
   *
   * @return a File object representing a temporary file.
   * @throws IOException if temporary file creation fails.
   **/
  public File createTemporaryFile() throws IOException {
    return fTemporaryFileManager.createTemporaryFile();
  }

  private void doPreprocess(File pSourceFile,
			    Suffix pSuffix,
			    InputStream pIn,
			    OutputStream pOut,
			    CompilerImplementation pImplementation,
			    IoRoot pIo)
    throws IOException, PassException {
    pImplementation.preprocess(pSourceFile, pSuffix, pOut, pIo);
  }

  private void doCompile(File pSourceFile,
			 Suffix pSuffix,
			 InputStream pIn,
			 OutputStream pOut,
			 CompilerImplementation pImplementation,
			 IoRoot pIo)
    throws IOException, PassException {
    pImplementation.compile(pSourceFile, pSuffix, pIn, pOut, pIo);
  }

  private void doAssemble(File pSourceFile,
			  Suffix pSuffix,
			  InputStream pIn,
			  File pOut,
			  CompilerImplementation pImplementation,
			  IoRoot pIo)
    throws IOException, PassException {
    fSpec.getTrace().trace("Driver", 5000, "doAssemble");
    pImplementation.assemble(pSourceFile, pSuffix, pIn, pOut, pIo);
  }

  private void doLink(File pOut,
		      CompilerImplementation pImplementation,
		      IoRoot pIo)
    throws IOException, PassException {
    pImplementation.link(pOut, pIo);
  }

  abstract class PassGroup {
    String fName;
    File fSourceFile;
    Suffix fSuffix;
    File fOutputFile = null;

    PassGroup(File pSourceFile) {
      fSourceFile = pSourceFile;
      CoinsOptions lCoinsOptions = fSpec.getCoinsOptions();
      String lSuffixOption = lCoinsOptions.getArg(lCoinsOptions.SUFFIX_OPTION);
      fSuffix = SuffixFactory.getSuffix(pSourceFile, lSuffixOption);
    }

    String getName() {return fName;}
    abstract boolean isSkipped();
    abstract boolean isRequired();
    abstract boolean isStopHere();
    abstract Suffix outputSuffix();
    abstract void go(CompileThread pThread,
		     InputStream pIn, OutputStream pOut, IoRoot io)
      throws IOException, PassException;

    void go(CompileThread pThread, InputStream pIn, File pOut, IoRoot io)
      throws IOException, PassException {
      OutputStream lOut = new FileOutputStream(pOut);
      try {
	go(pThread, pIn, lOut, io);
      } catch (IOException lIOException) {
	lOut.close();
	pOut.delete();
	throw lIOException;
      } catch (PassException lPassException) {
	lOut.close();
	pOut.delete();
	throw lPassException;
      }
      lOut.close();
    }

    File changeSuffix(Suffix pNewSuffix) {
      File parentFile = fSourceFile.getParentFile();
      String name = fSourceFile.getName();
      int index = name.lastIndexOf('.');
      String root;
      if (index == -1) {
	root = name;
      } else {
	root = name.substring(0, index);
      }
      return new File(parentFile, root + '.' + pNewSuffix.toString());
    }

    File outputFile() throws PassException {
      if (fOutputFile == null) {
	if (fSpec.isSet(fSpec.OUTPUT_FILE)) {
	  return new File((String)fSpec.getArg(fSpec.OUTPUT_FILE));
	} else {
	  return changeSuffix(outputSuffix());
	}
      } else {
	return fOutputFile;
      }
    }

    void setOutputFile(File pFile) {fOutputFile = pFile;}
  }

  class Preprocessor extends PassGroup {
    Preprocessor(File pSourceFile) {
      super(pSourceFile);
      fName = "preprocessor";
    }
    boolean isRequired() {return fSuffix.isPreprocessRequired();}
    Suffix outputSuffix() {return fSuffix.afterPreprocess();}

    boolean isSkipped() {
      return false;
    }

    void go(CompileThread pThread,
	    InputStream pIn, OutputStream pOut, IoRoot io)
      throws IOException, PassException {
      pThread.doPreprocess(fSourceFile, fSuffix, pIn, pOut, fImplementation, io);
    }

    boolean isStopHere() {
      return fSpec.isSet(fSpec.PREPROCESS_ONLY);
    }
  }

  class Compiler extends PassGroup {
    Compiler(File pSourceFile) {
      super(pSourceFile);
      fName = "compiler";
    }
    boolean isRequired() {return fSuffix.isCompileRequired();}
    Suffix outputSuffix() {return fSuffix.afterCompile();}

    boolean isSkipped() {
      return fSpec.isSet(fSpec.PREPROCESS_ONLY);
    }

    void go(CompileThread pThread,
	    InputStream pIn, OutputStream pOut, IoRoot io)
      throws IOException, PassException {
      pThread.doCompile(fSourceFile, fSuffix, pIn, pOut, fImplementation, io);
    }

    boolean isStopHere() {
      return fSpec.isSet(fSpec.COMPILE_ONLY);
    }
  }

  class Assembler extends PassGroup {
    Assembler(File pSourceFile) {
      super(pSourceFile);
      fName = "assembler";
    }
    boolean isRequired() {return fSuffix.isAssembleRequired();}
    Suffix outputSuffix() {return fSuffix.afterAssemble();}

    boolean isSkipped() {
      return fSpec.isSet(fSpec.PREPROCESS_ONLY)
	|| fSpec.isSet(fSpec.COMPILE_ONLY);
    }

    void go(CompileThread pThread,
	    InputStream pIn, OutputStream pOut, IoRoot io)
      throws IOException, PassException {
      File lTempFile = createTemporaryFile();
      pThread.doAssemble(fSourceFile, fSuffix, pIn, lTempFile, fImplementation, io);
      InputStream lIn = new FileInputStream(lTempFile);
      new StreamCopier(lIn, pOut).go();
      pOut.close();
    }

    void go(CompileThread pThread, InputStream pIn, File pOut, IoRoot io)
      throws IOException, PassException {
      pThread.doAssemble(fSourceFile, fSuffix, pIn, pOut, fImplementation, io);
    }

    boolean isStopHere() {return true;}

    File outputFile() throws PassException {
      if (fOutputFile == null) {
	if (fSpec.isSet(fSpec.ASSEMBLE_ONLY)) {
	  if (fSpec.isSet(fSpec.OUTPUT_FILE)) {
	    fOutputFile = new File((String)fSpec.getArg(fSpec.OUTPUT_FILE));
	  } else {
	    fOutputFile = changeSuffix(outputSuffix());
	  }
	} else {
	  try {
	    fOutputFile = createTemporaryFile();
	  } catch (IOException lIOException) {
	    String lMessage = lIOException.getMessage();
	    throw new PassException(fSourceFile, "assembler", lMessage);
	  }
	}
      }
      return fOutputFile;
    }
  }

  class Linker extends PassGroup {
    Linker() {
      super(new File(".")); /* tentative */
      fName = "linker";
    }

    boolean isRequired() {return true;}
    Suffix outputSuffix() {return null;}

    boolean isSkipped() {
      return fSpec.isSet(fSpec.PREPROCESS_ONLY)
	|| fSpec.isSet(fSpec.COMPILE_ONLY)
	|| fSpec.isSet(fSpec.ASSEMBLE_ONLY);
    }

    void go(CompileThread pThread,
	    InputStream pIn, OutputStream pOut, IoRoot io)
      throws IOException, PassException {
      File lTempFile = createTemporaryFile();
      pThread.doLink(lTempFile, fImplementation, io);
      InputStream lIn = new FileInputStream(lTempFile);
      new StreamCopier(lIn, pOut).go();
      pOut.close();
    }

    void go(CompileThread pThread, InputStream pIn, File pOut, IoRoot io)
      throws IOException, PassException {
      pThread.doLink(pOut, fImplementation, io);
    }

    boolean isStopHere() {
      return true;
    }

    File outputFile() throws PassException {
      if (fSpec.isSet(fSpec.OUTPUT_FILE)) {
	return new File((String)fSpec.getArg(fSpec.OUTPUT_FILE));
      } else {
	return DEFAULT_OUTPUT_FILE;
      }
    }
  }

  /**
   * Checks message counts and determines whether to continue compilation or
   * not as follows:
   * <ul>
   *   <li> When at least one warning has been issued ...
   *        just resets the counter.</li>
   *   <li> When at least one recovered error has been issued ...
   *        resets the counter and sets the exit status to 1,</li>
   *   <li> When at least one compile error has been issued ...
   *        resets the counter, sets the exit status to 1, and throws a
   *        PassException, </li>
   * </ul>
   * @param io an IoRoot.
   * @param passName name of the compile step (e.g., assemble, link, etc.)
   * @param status an object which maintains the exit status
   * @throws PassException if a compile error has been issued.
   **/
  private void errorCheck(IoRoot io,
			  String passName,
			  CompileStatus status)
    throws PassException {
    io.msgWarning.resetMessageCountOfThisClass();
    if (io.msgRecovered.getMessageCountOfThisClass() > 0) {
      io.msgRecovered.resetMessageCountOfThisClass();
      status.setABEND();
    }
    if (io.msgError.getMessageCountOfThisClass() > 0) {
      io.msgError.resetMessageCountOfThisClass();
      status.setABEND();
      throw new PassException(io.getSourceFile(), passName,
			      "compilation stopped due to compile error(s)");
    }
  }

  /**
   * Reads data from an InputStream and stores them to a file.
   *
   * @param in the source <tt>InputStream</tt>
   * @param dest the destination <tt>File</tt>
   * @throws IOException if storing data to a file fails
   **/
  public void storeToFile(InputStream in, File dest) throws IOException {
    int len;
    byte[] buffer = new byte[IO_BUFFER_SIZE];
    OutputStream lOut = new FileOutputStream(dest);
    while ((len = in.read(buffer)) > 0) {
      lOut.write(buffer, 0, len);
    }
    lOut.close();
  }

  private InputStream callPassGroup(PassGroup pPassGroup,
				    File pSourceFile,
				    InputStream pIn)
    throws PassException {
    if (! pPassGroup.isSkipped()) {
      try {
	if (pPassGroup.isRequired()) {
	  if (pIn == null && pSourceFile != null) {
	    pIn = new FileInputStream(pSourceFile);
	  }
	  if (pPassGroup.isStopHere()) {
	    File lDest = pPassGroup.outputFile();
	    fSpec.getTrace().trace("Driver", 5000,
				   "output file = " + lDest.getName());
	    pPassGroup.go(this, pIn, lDest, fIoRoot);
	    pIn = null;
	  } else {
	    if (fSpec.isSet(fSpec.PIPE)) {
	      ByteArrayOutputStream lOut = new ByteArrayOutputStream();
	      pPassGroup.go(this, pIn, lOut, fIoRoot);
	      byte[] buffer = lOut.toByteArray();
	      lOut.close();
	      pIn = new ByteArrayInputStream(buffer);
	    } else {
	      File lTemp = createTemporaryFile();
	      fSpec.getTrace().trace("Driver", 5000,
				     "temporary file = " + lTemp.getPath());
	      pPassGroup.go(this, pIn, lTemp, fIoRoot);
	      pIn = new FileInputStream(lTemp);
	    }
	  }
	  errorCheck(fIoRoot, pPassGroup.getName(), fStatus);
	} else {
	  if (pPassGroup.isStopHere()) {
	    if (pIn == null) {
	      pPassGroup.setOutputFile(pSourceFile);
	    } else {
	      File lDest = pPassGroup.outputFile();
	      storeToFile(pIn, lDest);
	      pIn = null;
	    }
	  }
	}
      } catch (IOException lIOException) {
	String lMessage = lIOException.getMessage();
	fIoRoot.msgError.put(lMessage);
	if (pSourceFile == null) {
	  throw new PassException(myName, lMessage);
	} else {
	  throw new PassException(pSourceFile, myName, lMessage);
	}
      }
    }
    return pIn;
  }

  public void run() {
    fClock.start();
    Trace lTrace = fSpec.getTrace();
    CoinsOptions lCoinsOptions = fSpec.getCoinsOptions();
    String lArch = lCoinsOptions.getArg(CommandLine.COINS_TARGET_OPTION);
    try {
      if (fIsLinkerThread) {
	String lLanguageName =
	  SuffixFactory.getSuffix("o", null).getLanguageName();
	fIoRoot = new IoRoot(new File(""), System.out, System.out, System.err,
			     fSpec, lArch, lLanguageName);
 	lTrace.trace("Driver", 5000, "new IoRoot for linker thread, target="
		     + lArch + ", language=" + lLanguageName);
	fSpec.getWarning().setIoRoot(fIoRoot);
	callPassGroup(new Linker(), null, null);
      } else {
	lTrace.trace("Driver", 5000, "run(3)");
	File lSourceFile = new File(fSourceFileName);
	String lSufOption = lCoinsOptions.getArg(lCoinsOptions.SUFFIX_OPTION);
	Suffix lSuffix = SuffixFactory.getSuffix(lSourceFile, lSufOption);
	String lLanguageName = lSuffix.getLanguageName();
	fIoRoot = new IoRoot(lSourceFile, System.out, System.out, System.err,
			     fSpec, lArch, lLanguageName);
	lTrace.trace("Driver", 5000, "new IoRoot, target=" + lArch
		     + ", language=" + lLanguageName);
	fSpec.getWarning().setIoRoot(fIoRoot);
	lTrace.trace("Driver", 5000, "run(4)");
	InputStream lIn = null;
	lIn = callPassGroup(new Preprocessor(lSourceFile), lSourceFile, lIn);
	lTrace.trace("Driver", 5000, "run(5)");
	lIn = callPassGroup(new Compiler(lSourceFile), lSourceFile, lIn);
	lTrace.trace("Driver", 5000, "run(6)");
	Assembler lAssembler = new Assembler(lSourceFile);
	callPassGroup(lAssembler, lSourceFile, lIn);
	lTrace.trace("Driver", 5000, "run(7)");
	fSpec.setObjectFile(fSourceFileName,
			    lAssembler.outputFile().getPath());
      }
    } catch (PassStopException lPassStopException) {
      lTrace.trace("Driver", 5000,
		   "PassStopException: " + lPassStopException.getMessage());
      /* global exitting, linking is cancelled */
      fStatus.cancelLinking();
    } catch (PassException lPassException) {
      lTrace.trace("Driver", 5000,
		   "PassException: " + lPassException.getMessage());
      fStatus.setABEND();
    } catch (RuntimeException lRuntimeException) {
      lTrace.trace("Driver", 5000,
		   "RuntimeException: " + lRuntimeException.getMessage());
      fStatus.setABEND();
      throw lRuntimeException;
    } catch (Error lError) {
      lTrace.trace("Driver", 5000,
		   "Error: " + lError.getMessage());
      fStatus.setABEND();
      throw lError;
    }
  }
}

