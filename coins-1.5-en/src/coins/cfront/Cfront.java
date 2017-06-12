/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.cfront;

import coins.IoRoot;
import coins.SymRoot;
import coins.HirRoot;
import coins.PassException;

import coins.casttohir.ToHir;
import coins.casttohir.ToHirC;
import coins.casttohir.ToHirC2;
import coins.casttohir.ToHirCOpt;
import coins.casttohir.ToHirCOpt2;
import coins.casttohir.ToHirBase;
import coins.casttohir.ToHirBaseOpt;

import coins.driver.CoinsOptions;
import coins.driver.CompileSpecification;
import coins.driver.Driver;
import coins.driver.Suffix;
import coins.driver.Trace;

import coins.ir.hir.HIR;
import coins.ir.hir.Program;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;

public class Cfront extends Driver
{

  protected File sourceFile;
  protected Suffix suffix;
  protected InputStream in;
  protected IoRoot ioRoot;
  public SymRoot symRoot;
  public HirRoot hirRoot;
  protected int fDbgLevel;
  protected CompileSpecification spec;

  /**
   * Cfront
   * @param pSourceFile the source file name.
   * @param pSuffix suffix rule of the source file.
   * @param pIn input.
   * @param pIo the IoRoot.
   * @param pHirRoot HirRoot.
   **/
public
Cfront(File pSourceFile,
          Suffix pSuffix,
          InputStream pIn,
          IoRoot pIo,
          HirRoot pHirRoot)
{
   sourceFile = pSourceFile;
   suffix = pSuffix;
   //out = pOut;
   in = pIn;
   ioRoot = pIo;
   hirRoot = pHirRoot;
   symRoot = pHirRoot.symRoot;
   fDbgLevel = ioRoot.dbgToHir.getLevel();
   if (fDbgLevel > 0)
     ioRoot.dbgToHir.print(1, "Cfront\n");
} // Cfront constructor

 /**
  * Dummy constructor
  * (required for coins.lparallel.LoopPara)
  */
 public Cfront()
 {
 }
    /**
     * HIR tree creation from source code.
     *
     * @throws IOException any IO error.
     * @throws PassException unrecoverable error(s) found in processing.
     **/
    // protected void makeHirFromSource(File sourceFile, HirRoot hirRoot,
    //         Suffix suffix, InputStream in, IoRoot io)
public void
makeHirFromCSource()
  throws IOException, PassException
{
  if (fDbgLevel > 0)
    ioRoot.dbgToHir.print(1, "makeHirFromCSource\n");
  hirRoot.programRoot
      = makeHirCFromCSource(hirRoot, in, ioRoot);
  hirRoot.programRoot
      = makeHirBaseFromC(hirRoot, (HIR)hirRoot.programRoot, ioRoot);
} // makeHirFromCSource

  /**
   *
   * Makes an HIR-C tree from a C source program.  Derived classes can
   * override this method.
   *
   * @param hirRoot an HirRoot object.
   * @param in an input stream from which the C source program can be read.
   * @param io an IoRoot object.
   * @return a HIR root node.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected HIR
    makeHirCFromCSource(HirRoot hirRoot, InputStream in, IoRoot io)
    throws IOException, PassException
  {
    //---- AstToHirC converts ASTList to HIR-C.
    //   - ASTList is created inside AstToHirC.

    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    if (fDbgLevel > 0)
      ioRoot.dbgToHir.print(1, "makeHirCFromCSource\n");
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    boolean fromcSpecified = false;
    if (coinsOptions.isSet(HIR_OPT_OPTION)) {
      fromcSpecified
        = includedInDelimitedList(HIR_OPT_ARG_FROMC,
          OPT_OPTION_DELIMITER,
          coinsOptions.getArg(HIR_OPT_OPTION));
    }
    ToHir tohir
      = new ToHir(hirRoot, coinsOptions.isSet(OLD_LIR_OPTION), fromcSpecified);
    new ToHirC(tohir).astToHirC(in);
    // ToHirCOpt is required in ToHirVisit, ToHirCOpt2. //##71
    ToHirCOpt toHirCOpt = new ToHirCOpt(tohir); //##71
    new ToHirC2(tohir).visitProgram();
    if (fromcSpecified) {
      new ToHirCOpt2(tohir).visitProgram();
    } else {
      //##71 new ToHirCOpt(tohir).visitProgram();
      toHirCOpt.visitProgram(); //##71
    }
    HIR hir = (Program)hirRoot.programRoot;
    hir.finishHir(); //##62
    if (fDbgLevel >= 4) {
      ioRoot.dbgToHir.print(1, "\nHIR-C");
      hir.print(0);
    }
    if (io.addToTotalErrorCount(0) > 0) {
      throw new PassException(io.getSourceFile(),
        "Ast to HIR-C", "Error(s) in parsing source.");
    }
    return hir;
  } // makeHirCFromCSource

  /**
   *
   * Makes an HIR-Bsae tree from an HIR-C tree.
   *
   * @param hirRoot an HirRoot object.
   * @param hir a root node of HIR-C tree.
   * @param io an IoRoot object.
   * @return a root node of an HIR-Base tree.
   * @throws IOException any IO error.
   * @throws PassException unrecoverable error(s) found in processing.
   **/
  protected HIR
  makeHirBaseFromC(HirRoot hirRoot, HIR hir, IoRoot io)
    throws IOException, PassException
  {
    //---- HirCToHirBase converts HIR-C to HIR-base. ----//

    CompileSpecification spec = io.getCompileSpecification();
    Trace trace = spec.getTrace();
    if (fDbgLevel > 0)
      ioRoot.dbgToHir.print(1, "makeHirBaseFromC\n");
    CoinsOptions coinsOptions = spec.getCoinsOptions();
    boolean fromcSpecified = false;
    if (coinsOptions.isSet(HIR_OPT_OPTION)) {
      fromcSpecified =
        includedInDelimitedList(HIR_OPT_ARG_FROMC,
        OPT_OPTION_DELIMITER,
        coinsOptions.getArg(HIR_OPT_OPTION));
    }
    ToHir tohir
      = new ToHir(hirRoot, coinsOptions.isSet(OLD_LIR_OPTION), fromcSpecified);
    new ToHirBase(tohir).visitProgram();
    if (fromcSpecified) {
      new ToHirBaseOpt(tohir).visitProgram();
    }
    hir.setIndexNumberToAllNodes(1);

    if (fDbgLevel >= 3) {
      ioRoot.dbgToHir.print(1, "\nHIR-base");
      hir.print(0);
    }
    if (ioRoot.dbgSym.getLevel() >= 3) {
      ioRoot.dbgSym.print(3, "\nSym after HIR generation ");
      SymRoot symroot = hirRoot.symRoot;
      symroot.symTable.printSymTableAllDetail(symroot.symTableRoot);
    }
    if (io.addToTotalErrorCount(0) > 0) {
      throw new PassException(io.getSourceFile(), "HIR-C to HIR-Base",
        "Error(s) in making HIR-Base.");
    }
    if (hir.isTree()) {
      trace.trace("HIR", 2, "\nHIR-base does not violate tree structure.");
    }
    return hir;
  } // makeHirBaseFromC

} // Cfront
