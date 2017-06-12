/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.IoRoot;
import coins.PassException;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Compiler implementation object.<br>
 *
 * In COINS Compiler Driver API, user must provide implemtation of four
 * compilation steps: preprocess, compile, assemble, and link.  These
 * implementations are called from a CompilerDriver object via this interface.
 **/

public interface CompilerImplementation {
  /**
   * Preprocessor implementation.<br>
   *
   * The input source file name can be obtained as an <tt>File</tt>.  Output
   * lines should be written to the <tt>OutputStream</tt>.  Command line
   * options and arguments can be obtained from the
   * <tt>CompileSpecification</tt>.
   *
   * @param sourceFile the source file name.
   * @param suffix suffix rule of the source file.
   * @param in the <tt>InputStream</tt>.
   * @param out the <tt>OutputStream</tt>.
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  void preprocess(File sourceFile,
		  Suffix suffix,
		  OutputStream out,
		  IoRoot io) throws IOException, PassException;

  /**
   * Compiler implementation.<br>
   *
   * Input lines can be read from the <tt>InputStream</tt>.  Output lines
   * should be written to the <tt>OutputStream</tt>.  The input source file
   * name (before preprocessing) can be obtained as the <tt>File</tt>.
   * Command line options and arguments can be obtained from a
   * <tt>CompileSpecification</tt>.
   *
   * @param sourceFile the source file name.
   * @param suffix suffix rule of the source file.
   * @param in the <tt>InputStream</tt>.
   * @param out the <tt>OutputStream</tt>.
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error
   * @throws PassException unrecoverable error(s) found in processing
   **/
  void compile(File sourceFile,
	       Suffix suffix,
	       InputStream in,
	       OutputStream out,
	       IoRoot io) throws IOException, PassException;

  /**
   * Assembler implementation.<br>
   *
   * Input lines can be read from the <tt>InputStream</tt>.  Output lines
   * should be written to the <tt>File</tt>.  The input source file name
   * (before preprocessing) can be obtained as the <tt>sourceFile</tt>.
   * Command line options and arguments can be obtained from a
   * <tt>CompileSpecification</tt>.
   *
   * @param sourceFile the source file name
   * @param suffix suffix rule of the source file.
   * @param in the <tt>InputStream</tt>
   * @param out the output <tt>File</tt>
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error
   * @throws PassException unrecoverable error(s) found in processing
   **/
  void assemble(File sourceFile,
		Suffix suffix,
		InputStream in,
		File out,
		IoRoot io) throws IOException, PassException;

  /**
   * Linker implementation.<br>
   *
   * Output executable file should be written to a <tt>File</tt>.  Command
   * line options and arguments can be obtained from a CompileSpecification.
   *
   * @param out the output <tt>File</tt>
   * @param io the <tt>IoRoot</tt>.
   * @throws IOException any IO error
   * @throws PassException unrecoverable error(s) found in processing
   **/
  void link(File out, IoRoot io) throws IOException, PassException;
}
