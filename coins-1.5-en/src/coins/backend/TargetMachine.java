/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.gen.CodeGenerator;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.SymTab;
import coins.backend.sym.Symbol;
import coins.backend.MachineParams;
/* import coins.backend.tmd.TMD; */
import coins.backend.util.BitMapSet;
import coins.backend.util.ImList;
import coins.backend.util.NumberSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


/** Target Description Module **/
public class TargetMachine {

  /** Machine parameters **/
  public final MachineParams machineParams;

  /** Address type for this machine. **/
  public final int typeAddress;

  /** Boolean type for this machine. **/
  public final int typeBool;


  private Root root;

  private Tmd tmd;

  private CodeGenerator targetCG;


  /** Assembler output stream **/
  private OutputStream asmStream;


  /** Initialize Target Description information **/
  public TargetMachine(SymTab symTab, String targetName, String convention, Module module) {

    root = module.root;

    root.registerTransformer(earlyRewritingTrig);
    root.registerTransformer(lateRewritingTrig);
    root.registerTransformer(instSelTrig);
    root.registerTransformer(convToAsmTrig);

    // Load and initialize code generator (either Scheme or Java)
    try {
      if (root.javaCG) {
        machineParams = (MachineParams)
          (Class.forName("coins.backend.gen.MachineParams_" + targetName)).newInstance();
        machineParams.init(module, symTab);

        typeAddress = machineParams.typeAddress();
        typeBool = machineParams.typeBool();

        targetCG = (CodeGenerator)
          (Class.forName("coins.backend.gen.CodeGenerator_" + targetName)).newInstance();
        targetCG.initialize(root, module, this, targetName, convention);
      } else {
        machineParams = (MachineParams)
          (Class.forName("coins.backend.tmd.MachineParams_" + targetName)).newInstance();
        machineParams.init(module, symTab);
        typeAddress = machineParams.typeAddress();
        typeBool = machineParams.typeBool();

        // tmd = new TMD(targetName);
        tmd = (Tmd)Class.forName("coins.backend.tmd.TMD").newInstance();
        tmd.init(targetName);
        if (!root.traceOK("TMD", 3))
          tmd.evals("(set! *debug* ())");
      }

    } catch (ClassNotFoundException e) {
      throw new Error("Class not found: " + e.getMessage());
    } catch (InstantiationException e) {
      throw new Error("Can't new: " + e.getMessage());
    } catch (IllegalAccessException e) {
      throw new Error("Illegal access: " + e.getMessage());
    } catch (IOException e) {
      throw new Error("IOException: " + e.getMessage());
    }

  }


  /** Set assembler output stream. **/
  public void setAsmStream(OutputStream stream) {
    asmStream = stream;
    if (root.javaCG)
      targetCG.setAsmStream(stream);
  }


  /** Return the alignment bytes for specified type. **/
  public int alignForType(int type) {
    if (root.javaCG)
      return targetCG.alignForType(type);
    else {
      int s = Type.bytes(type);
      switch (s) {
      case 1: case 3: case 5: case 7: return 1;
      case 2: case 6: return 2;
      case 4: return 4;
      case 8: return 8;
      default:
        return 8;
      }
    }
  }




  /** Generic interface for rewriters and code generators **/

  /** Early time pre-rewriting of LIR. **/
  public final GlobalTransformer earlyRewritingTrig = new GlobalTransformer() {
      public boolean doIt(Module module, ImList args) {
        if (root.javaCG) {
          module.apply(targetCG.earlyRewritingSequence());
        }
        return true;
      }

      public String name() { return "EarlyRewriting"; }

      public String subject() { return "Early-time Rewriting"; }
    };



  /** Late time pre-rewriting of LIR. **/
  public final GlobalTransformer lateRewritingTrig = new GlobalTransformer() {
      public boolean doIt(Module module, ImList args) {
        if (root.javaCG) {
          module.apply(targetCG.lateRewritingSequence());
        } else {
          module.apply(restructTrig);
        }
        return true;
      }

      public String name() { return "LateRewriting"; }

      public String subject() { return "Late-time Rewriting"; }
    };


  /** Late time pre-rewriting of LIR, function by function. **/
  final LocalTransformer restructTrig = new LocalTransformer() {
      public boolean doIt(Function func, ImList args) {
        restruct2(func);
        return true;
      }

      public boolean doIt(Data data, ImList args) { return true; }
      
      public String name() { return "LocalLateRewriting"; }

      public String subject() { return "Late-time Rewriting (local)"; }
    };


  /** Rewrite function before code generation.**/
  public void restruct2(Function func) {

    if (root.javaCG)
      throw new CantHappenException();

    try {
      String rewrittenCode = null;

      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      PrintWriter out = new PrintWriter(bytes);
      func.printStandardForm(out);
      out.close();

      if (root.dispIntervalTime)
        root.debOut.println(" ASCII-form convertion: " + root.timer.getIntervalTime());

      if (root.traceOK("TMD", 2)) {
        root.debOut.println();
        root.debOut.println("Before restruct: ");
        root.debOut.print(bytes.toString());
        root.debOut.flush();
      }

      if (rewrittenCode == null)
        rewrittenCode = tmd.restructure(bytes.toString());

      if (root.dispIntervalTime)
        root.debOut.println(" Restructuring: " + root.timer.getIntervalTime());

      if (root.traceOK("TMD", 2)) {
        root.debOut.println();
        root.debOut.println("After restruct: ");
        root.debOut.print(rewrittenCode);
        root.debOut.println();
        root.debOut.flush();
      }

      // Reload matched code.
      Function machineDep;
      Object sexp = ImList.readSexp(new PushbackReader
                                    (new StringReader(rewrittenCode)));
      if (!(sexp instanceof ImList))
        throw new CantHappenException("readSexp returns null or atom object");

      func.reload((ImList)sexp);
      if (root.dispIntervalTime)
        root.debOut.println(" Reload: " + root.timer.getIntervalTime());

    } catch (SyntaxError e) {
      throw new CantHappenException(e.toString());
    } catch (IOException e) {
      throw new CantHappenException(e.toString());
    }
  }

    

  public final LocalTransformer instSelTrig = new LocalTransformer() {
      public boolean doIt(Function func, ImList args) {
        instSel2(func);
        return true;
      }

      public boolean doIt(Data data, ImList args) { return true; }
      
      public String name() { return "InstSel"; }

      public String subject() { return "Instruction Selection"; }
    };


  public CodeGenerator getTargetCG() {
    if (!root.javaCG)
      throw new CantHappenException("not Java CG");
    return targetCG;
  }



  /** Convert function func to machine dependent form. **/
  public void instSel2(Function func) {
    // New Code Generator?
    if (root.javaCG) {
      // Java tmd
      targetCG.instructionSelection(func);
      
    } else {
      // Scheme tmd
      try {
        String machineDepStr = null;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(bytes);
        func.printStandardForm(out);
        out.close();

        if (root.dispIntervalTime)
          root.debOut.println(" ASCII-form convertion: " + root.timer.getIntervalTime());

        if (root.traceOK("TMD", 2)) {
          root.debOut.println();
          root.debOut.println("Before instsel: ");
          root.debOut.print(bytes.toString());
          root.debOut.flush();
        }

        if (machineDepStr == null)
          machineDepStr = tmd.instsel(bytes.toString());

        if (root.dispIntervalTime)
          root.debOut.println(" Instruction Selection: " + root.timer.getIntervalTime());

        if (root.traceOK("TMD", 2)) {
          root.debOut.println();
          root.debOut.println("After instsel: ");
          root.debOut.print(machineDepStr);
          root.debOut.println();
          root.debOut.flush();
        }

        // Reload matched code.
        Function machineDep;
        Object sexp = ImList.readSexp(new PushbackReader
                                      (new StringReader(machineDepStr)));
        if (!(sexp instanceof ImList))
          throw new CantHappenException("readSexp returns null or atom object");
        func.reload((ImList)sexp);
        if (root.dispIntervalTime)
          root.debOut.println(" Reload: " + root.timer.getIntervalTime());

      } catch (SyntaxError e) {
        throw new CantHappenException(e.toString());
      } catch (IOException e) {
        throw new CantHappenException(e.toString());
      }
    }
  }


  /** Convert to Assembly Language **/
  public final GlobalTransformer convToAsmTrig = new GlobalTransformer() {
      public boolean doIt(Module module, ImList args) {
        // New Code Generator?
        if (root.javaCG) {
          // TMD in Java
          targetCG.genHeader(module);
          module.apply(targetCG.convToAsm());
          targetCG.genTrailer(module);
          targetCG.close();
        } else {
          // TMD in Scheme
          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
          PrintWriter out = new PrintWriter(bytes);
          module.printStandardForm(out);
          out.close();
          tmd.asmout(bytes.toString(), new PrintWriter(asmStream));
        }
        return true;
      }

      public String name() { return "ConvToAsm"; }

      public String subject() { return "Convert to Assembly Language"; }
    };


  /** Emit named constant to assembler source. **/
  public void emitNamedConst(String name, LirNode value) {
    if (root.javaCG)
      targetCG.emitNamedConst(name, value);
  }

}


