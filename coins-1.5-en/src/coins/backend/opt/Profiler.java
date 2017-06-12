/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.driver.CompileSpecification;

import coins.backend.CantHappenException;
import coins.backend.Data;
import coins.backend.Module;
import coins.backend.Function;
import coins.backend.GlobalTransformer;
import coins.backend.LocalTransformer;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.Type;
import coins.backend.Storage;
import coins.backend.sym.Symbol;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirIconst;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/** Insert code for counting number of executions on each basic-block.
 **/
public class Profiler {
  public static class Trigger implements GlobalTransformer {

    int nCounters = 0;

    public boolean doIt(Module module, ImList args) {
      new Profiler(module);
      return true;
    }

    public String name() { return "Profiler"; }

    public String subject() { return "Profiler Code Insertion"; }
  }

  public static final Trigger trig = new Trigger();


  Module module;
  LirFactory lir;

  int counterType;
  int typeAddress;
  int counterSize;

  Symbol counterArray;
  int index;

  int nPositions = 512;
  int[] positions = new int[nPositions];
  int arraySize = 0;

  private void logPosition(int value) {
    if (arraySize >= nPositions) {
      int[] tmp = new int[nPositions * 2];
      for (int i = 0; i < nPositions; i++)
        tmp[i] = positions[i];
      nPositions *= 2;
      positions = tmp;
    }
    positions[arraySize++] = value;
  }

  /** Remove suffix from filename. **/
  private static String stem(String old) {
    int pos = old.indexOf('.');
    return (pos >= 0 ? old.substring(0, pos) : old);
  }

  /** Return true if character c is a component of the identifier. **/
  private static boolean isIdentifier(char c) {
    return ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z'
            || '0' <= c && c <= '9' || c == '_');
  }

  /** Convert a filename to identifier. **/
  private static String createIdentifier(String filename) {
    char[] str = filename.toCharArray();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < str.length; i++) {
      if (isIdentifier(str[i]))
        buf.append(str[i]);
    }
    return buf.toString();
  }

  /** Instantiation **/
  Profiler(Module module) {
    this.module = module;
    lir = module.newLir;

    counterType = Type.type(Type.INT, 32);
    typeAddress = module.targetMachine.typeAddress;
    counterSize = Type.bytes(counterType);

    // Create symbol entry of counter vector
    String vecName = ("__" + createIdentifier(stem(module.name)) + "_countervec").intern();
    counterArray = module.addSymbol
      (vecName, Storage.STATIC, Type.type(Type.AGGREGATE, 0),
       8, ".bss", "XREF", null);
    index = 0;

    // Insert counter-increment code to each function
    module.apply(new LocalTrigger());

    /*
     * // Reserve space of counter vector
     * module.addData(counterArray,
     *                lir.node(Op.SPACE, Type.UNKNOWN,
     *                         lir.iconst(typeAddress, index*counterSize)));
     */

    logPosition(0);
    // Flush statement-counter table.
    try {
      PrintWriter out = new PrintWriter(new FileOutputStream(stem(module.name) + ".prf"));
      out.println("# counter table for " + module.name);
      out.println("# name: " + vecName);
      out.println("# size: " + counterSize + " * " + index);
      for (int i = 1; i < arraySize; i++) {
        for (; positions[i] != 0; i++)
          out.print(positions[i] + " ");
        out.println();
      }
      out.flush();
    } catch (FileNotFoundException e) {
      throw new CantHappenException();
    }
  }



  class LocalTrigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      
      FlowGraph flowGraph = func.flowGraph();

      for (BiLink p = flowGraph.basicBlkList.first(); !p.atEnd(); p = p.next()) {
        BasicBlk blk = (BasicBlk)p.elem();
        boolean first = true;
        for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
          LirNode ins = (LirNode)q.elem();

          if (ins.opCode == Op.LINE) {
            int lineno = (int)(((LirIconst)ins.kid(0)).value);
            if (lineno != 0) {
              /* Generate code for increment counter.
               *  t: type of counter
               *  a: type of address
               * (SET t
               *   (MEM t
               *      (ADD a (STATIC a ".counter_for_this")
               *               (INTCONST a n*sizeof(t))))
               *   (ADD t
               *      (MEM t
               *         (ADD a (STATIC a ".counter_for_this")
               *               (INTCONST a n*sizeof(t))))
               *   (INTCONST t 1)))
               */
              if (first) {
                LirNode s = lir.node
                  (Op.SET, counterType, lir.node
                   (Op.MEM, counterType, lir.node
                    (Op.ADD, typeAddress, lir.node
                     (Op.STATIC, typeAddress, counterArray),
                     lir.iconst(typeAddress, index * counterSize))),
                   lir.node
                   (Op.ADD, counterType, lir.node
                    (Op.MEM, counterType, lir.node
                     (Op.ADD, typeAddress, lir.node
                      (Op.STATIC, typeAddress, counterArray),
                      lir.iconst(typeAddress, index * counterSize))),
                    lir.iconst(counterType, 1)));
                q.addAfter(s);
                first = false;
                index++;
                logPosition(0);
              }

              logPosition(lineno);
            }
          }
        }
      }

      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }

    public String name() { return "Profiler"; }

    public String subject() { return "Profiler Code Insertion"; }
  }


}
