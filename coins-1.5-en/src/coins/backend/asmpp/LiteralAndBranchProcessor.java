/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
  Literal pool and Short branch converter
*/

/*
Convert literals and branches. //##74

Generation of literal:
  Attach prefix "=W" or "=S" or "=B" for literals.
        ldr    %r0,=W12345
  will be changed to
        ldr    %r0,.LB1
  ...
 .LB1:
        .word 12345
"=W" is changed to .word, "=S" to .short, and "=B" to .byte.

Generation of branch:
  Branch is generated always in short-branch form.
  beq	.L1
  ...
 .L1:
If its branch distance exceeds the limit of actual branch, then
the post-process converter changes it to
  bne	.LB2
  b .L1	; short unconditional branch
 .LB2:
  ...
 .L1:
If the branch distance of  b .L1 exceeds the limit, then
it is changed to
  bne	.LB2
  bl	.L1	; Long unconditional branch (bl in case of Thumb).
 .LB2:
  ...
 .L1:
*/

package coins.backend.asmpp;
import java.io.*;
import java.util.*;

/*
  The following class named CPU is required when LiteralAndBranchProcessor
  is to be used as a stand-alone processor.
  If it is used as the post processor of the backend, CPU is not used.
*/

final class Arm extends CPU {

  Arm() {
    bccRange = new int[] {-16000000, 16000000};
    braRange = new int[] {-16000000, 16000000};
    literalRange = new int[] {-1000, 1000};
    bccMnemo = new String[] { };
    braMnemo = "b";
    braLength = 4;
    codeAlign = 2;
  }

  public int codeLength(String inst)
  {
    return 4;
  }

  public String toString() {
    return "Arm";
  }
}

final class Thumb extends CPU {

  Thumb() {
    bccRange = new int[] {-240, 250};
    braRange = new int[] {-2024, 2024};
    literalRange = new int[] {0, 1020};
    bccMnemo = new String[] { "beq", "bne", "bcs", "bcc", "bhs", "blo", "bmi", "bpl",
            "bvs", "bvc", "bhi", "bls", "bge", "blt", "bgt", "ble" };
    braMnemo = "b";
    braLength = 2;
    codeAlign = 1;
  }

  public int codeLength(String inst)
  {
    StringTokenizer tokens = new StringTokenizer(inst, " \t,");
    if (tokens.hasMoreTokens()) {
      String mnemo = tokens.nextToken();
      if (mnemo.equalsIgnoreCase("bl")) {
        return 4;
      }
    }
    return 2;
  }

  public String[] rewriteToLongBranch(String label) {
    return new String[] { "\tbl\t" + label };
  }

  public String toString() {
    return "Thumb";
  }
}

final class Sh4 extends CPU {

  Sh4() {
    bccRange = new int[] {-500, 512};
    braRange = new int[] {-4088, 4096};
    literalRange = new int[] {0, 1020};
    bccMnemo = new String[] { "bt", "bf" };
    braMnemo = "bra";
    braLength = 4; // delayed branch
    codeAlign = 1;
  }

  public int codeLength(String inst)
  {
    StringTokenizer tokens = new StringTokenizer(inst, " \t,");
    if (tokens.hasMoreTokens()) {
      String mnemo = tokens.nextToken();
      if (mnemo.equalsIgnoreCase(braMnemo)) {
        // delayed branch, add size of NOP;
        return 4;
      }
    }
    return 2;
  }

  public String generateBra(String label) {
    // delayed branch, add NOP
    return "\t" + braMnemo + "\t" + label + "\n\tnop";
  }

  public String[] rewriteToLongBranch(String label) {
    return new String[] { "\tmov.l\tr0,@-r15",
              "\tmov.l\t=W" + label + ",r0",
              "\tjmp\t@r0",
              "\tmov.l\t@r15+,r0" };
  }

  public String toString() {
    return "Sh4";
  }
}


/* Assembler Instructions */

abstract class AsmLine {

  public static final String byteMark = "=B";
  public static final String shortMark = "=S";
  public static final String wordMark = "=W";
  public static final String doubleMark = "=D"; // =Dxx1_xx2 -> .word xx1, xx2
  public static final String prefix = ".LB";
  public static CPU cpu;

  String line;
  int address;
  int size;

  AsmLine(String line) {
    this.line = line;
  }

  public String getLine() {
    return line;
  }

  public int setAddress(int address) {
    this.address = address;
    return address;
  }

  public int getAddress() {
    return address;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }

  public String toString() {
    return address + ":" + size + ":" + line;
  }

  public String generate() {
    return line;
  }
}

abstract class InstWithCode extends AsmLine{

  InstWithCode(String line) {
    super(line);
  }

  public int setAddress(int address) {
    this.address = address;
    return address + size;
  }
}

abstract class Mnemo extends InstWithCode {

  Mnemo(String line) {
    super(line);
    size = cpu.codeLength(line);
  }
}

class NormalInstruction extends Mnemo {

  NormalInstruction(String line) {
    super(line);
  }
}

class LiteralInstruction extends Mnemo {

  String literal;
  LtorgInstruction ltorg;

  LiteralInstruction(String line, String literal) {
    super(line);
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }

  public String toString() {
    String s = super.toString() + "\t" + literal;
    if (ltorg != null) {
      s += " -> " + ltorg.toLabel(literal);
    }
    return s;
  }

  public String generate() {
    int index = line.indexOf(literal);
    String s = ltorg != null ? ltorg.toLabel(literal) : literal;
    return line.substring(0, index) + s + line.substring(index + literal.length());
  }

  public int getLiteralSize() {
    int v = 1 << cpu.codeAlign;
    if (literal.startsWith(byteMark)) {
      return (1 + v - 1) & -v;
    } else if (literal.startsWith(shortMark)) {
      return (2 + v - 1) & -v;
    } else if (literal.startsWith(doubleMark)) {
      return (8 + v - 1) & -v;
    }
    return (4 + v - 1) & -v;
  }

  public void setLtorg(LtorgInstruction ltorg) {
    this.ltorg = ltorg;
  }
}

class BccInstruction extends Mnemo {

  String mnemo;
  String label;

  BccInstruction(String line, String mnemo, String label) {
    super(line);
    this.mnemo = mnemo;
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public String getMnemo() {
    return mnemo;
  }

  public String getRevMnemo() {
    return cpu.getRevMnemo(mnemo);
  }

  public String generate() {
    return cpu.generateBcc(mnemo, label);
  }
}

class BraInstruction extends Mnemo {

  String label;

  BraInstruction(String line, String label) {
    super(line);
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public String generate() {
    return cpu.generateBra(label);
  }
}

abstract class Pseudo extends InstWithCode {

  Pseudo(String line) {
    super(line);
  }
}

class LabelInstruction extends Pseudo {

  String name;

  LabelInstruction(String line, String name) {
    super(line);
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

class AlignInstruction extends Pseudo {

  int factor;

  AlignInstruction(String line, int factor) {
    super(line);
    this.factor = factor;
  }

  public int setAddress(int address) {
    int v = 1 << factor;
    int a = (address + v - 1) & -v;
    this.address = address;
    this.size = a - address;
    return a;
  }
}

class LtorgInstruction extends Pseudo {

  ArrayList literals;
  int label;
  int blockSize;
  int braLength;

  LtorgInstruction(String line) {
    super(line);
    literals = new ArrayList();
    label = 0;
    blockSize = 0;
    braLength = 0;
  }

  public void reset() {
    literals.clear();
    label = 0;
    blockSize = 0;
    size = 0;
  }

  public int setAddress(int address) {
    int v = 1 << cpu.codeAlign;
    this.address = address;
    if (literals.isEmpty()) {
      return address;
    } else {
      String l = (String)literals.get(0);
      if (l.startsWith(wordMark)) {
        v = 1 << 2;
      }
      size = ((address + braLength + v - 1) & -v) - address;
      v = 1 << cpu.codeAlign;
    }
    return (address + size + blockSize + v - 1) & -v;
  }

  public void addLiteral(String l) {
    int index;

    for (index = 0; index < literals.size(); index++) {
      Object o = literals.get(index);
      if (l.equals(o)) {
        return;
      }
    }
    literals.add(l);
    if (l.startsWith(byteMark)) {
      blockSize += 1;
    } else if (l.startsWith(shortMark)) {
      blockSize = ((blockSize + 1) & -2) + 2;
    } else if (l.startsWith(doubleMark)) {
      blockSize = ((blockSize + 3) & -4) + 8;
    } else {
      blockSize = ((blockSize + 3) & -4) + 4;
    }
//		System.out.println("LtorgInstruction.addLiteral: " + l + ", blockSize = " + blockSize);
  }

  public int getAddressOf(String value) {
    int offset = 0, bytes = 0;
    String l;
    for (int index = 0; index < literals.size(); index++) {
      l = (String)literals.get(index);
      if (l.startsWith(byteMark)) {
        bytes = 1;
      } else if (l.startsWith(shortMark)) {
        offset = (offset + 1) & -2;
        bytes = 2;
      } else if (l.startsWith(doubleMark)) {
        offset = (offset + 3) & -4;
        bytes = 8;
      } else {
        offset = (offset + 3) & -4;
        bytes = 4;
      }
      if (value.equals(l)) {
        break;
      }
      offset += bytes;
    }
    if (value.startsWith(shortMark)) {
      offset = (offset + 1) & -2;
    } else if (value.startsWith(wordMark) || value.startsWith(doubleMark)) {
      offset = (offset + 3) & -4;
    }
//		System.out.println(value + " -> " + address + ":" + size + ":" + offset);
    return address + size + offset;
  }

  public String listLiterals() {
    String str = "";
    for (int index = 0; index < literals.size(); index++) {
      str += " " + (String)literals.get(index);
    }
    return str;
  }

  public String toString() {
    return "LtorgInstruction[" + address + ":" + size + ":" + blockSize + ":" + listLiterals() + "]";
  }

  public String generate() {
    String str = "";
    int align = cpu.codeAlign;

    if (!literals.isEmpty()) {
      for (int index = 0; index < literals.size(); index++) {
        String s = (String)literals.get(index);
        if (s.startsWith(byteMark)) {
          str += "\n" + prefix + (label + index) + ":\n\t.byte\t" + s.substring(2);
          align = 0;
        } else if (s.startsWith(shortMark)) {
          if (align == 0) {
            str += "\n\t.align\t1";
          }
          str += "\n" + prefix + (label + index) + ":\n\t.short\t" + s.substring(2);
          align = 1;
        } else if (s.startsWith(doubleMark)) {
          if (align != 2) {
            str += "\n\t.align\t2";
          }
          str += "\n" + prefix + (label + index) + ":\n\t.word\t" + s.substring(2).replace('_', ',');
          align = 2;
        } else {
          if (align != 2) {
            str += "\n\t.align\t2";
          }
          str += "\n" + prefix + (label + index) + ":\n\t.word\t" + s.substring(2);
          align = 2;
        }
      }
      if (align < cpu.codeAlign) {
        str += "\n\t.align\t" + cpu.codeAlign;
      }
      if (str.charAt(0) == '\n') {
        str = str.substring(1);
      }
    }
    return str;
  }

  public String toLabel(String l) {
    if (label == 0) {
      return l;
    }
    for (int index = 0; index < literals.size(); index++) {
      if (l.equals(literals.get(index))) {
        return prefix + (label + index);
      }
    }
    return null;
  }

  public int setLabel(int l) {
    label = l;
    return literals.size();
  }
}

class BraLtorgInstruction extends LtorgInstruction {

  BraLtorgInstruction(String line) {
    super(line);
    this.braLength = cpu.braLength;
  }

  public String toString() {
    return "BraLtorgInstruction[" + address + ":" + size + ":" + blockSize + ":" + listLiterals() + "]";
  }

  public String generate() {
    String str = "";

    if (!literals.isEmpty()) {
      str += "\t" + cpu.braMnemo + "\t" + prefix + (label + literals.size()) + "\n";
      str += super.generate();
      str += "\n" + prefix + (label + literals.size()) + ":";
    }
    return str;
  }

  public int setLabel(int l) {
    l = super.setLabel(l);
    return l == 0 ? 0 : l + 1;
  }
}

class WordInstruction extends Pseudo {

  WordInstruction(String line, int params) {
    super(line);
    setSize(4 * params);
  }
}

class ShortInstruction extends Pseudo {

  ShortInstruction(String line, int params) {
    super(line);
    setSize(2 * params);
  }
}

class ByteInstruction extends Pseudo {

  ByteInstruction(String line, int params) {
    super(line);
    setSize(1 * params);
  }
}

class OtherInstruction extends AsmLine {

  OtherInstruction(String line) {
    super(line);
  }
}


public class LiteralAndBranchProcessor extends Thread {

  OutputStream finalOut;
  PipedOutputStream out;
  PipedInputStream stream;
  PrintWriter wrt;

  ArrayList Instructions = new ArrayList();
  Hashtable Labels = new Hashtable(); // (name, Label)
  Hashtable LiteralTable = new Hashtable();
  ArrayList Ltorgs = new ArrayList();
  CPU cpu;
  boolean changed;
  int serialNumber = 1;

  private void dumpInstructions() {
    for (int i = 0; i < Instructions.size(); i++) {
      AsmLine inst = (AsmLine)Instructions.get(i);
      wrt.println(inst.generate());
    }
  }

  private void debugDumpInstructions() {
    for (int i = 0; i < Instructions.size(); i++) {
      System.out.println(Instructions.get(i));
    }
  }

  private void dumpLabels() {
    System.out.println("Labels:");
    Enumeration e = Labels.elements();
    while (e.hasMoreElements()) {
      LabelInstruction l = (LabelInstruction)e.nextElement();
      System.out.println(l.getName() + " = " + l.getAddress());
    }
  }

  private void calcAddress() {
    int pc = 0;
    for (int index = 0; index < Instructions.size(); index++) {
      AsmLine inst = (AsmLine)Instructions.get(index);
      if (inst instanceof InstWithCode) {
        pc = inst.setAddress(pc);
      }
    }
  }

  private void insertBraLtorg() {
    int counter = -1;
    int limit = (cpu.literalRange[1] * 95) / 100;
    for (int index = 0; index < Instructions.size(); index++) {
      AsmLine inst = (AsmLine)Instructions.get(index);
      if (inst instanceof LtorgInstruction) {
        Ltorgs.add(inst);
        counter = -1;
        while (index + 1 < Instructions.size() &&
               (AsmLine)Instructions.get(index + 1) instanceof LtorgInstruction) {
          Instructions.remove(index + 1);
        }
      } else if (inst instanceof LiteralInstruction) {
        if (counter < 0) {
          counter = 0;
        }
        counter += ((LiteralInstruction)inst).getLiteralSize();
      } else if (inst instanceof BraInstruction) {
        Instructions.add(index + 1, new LtorgInstruction("\t.ltorg"));
      }
      if (!(counter < 0) && (counter += inst.getSize()) > limit) {
        if (!(index + 1 < Instructions.size()) ||
            !(Instructions.get(index + 1) instanceof BraInstruction)) {
          String s = "\t" + cpu.braMnemo + "\tLABEL";
          inst = new BraLtorgInstruction(s);
          Instructions.add(index, inst);
          Ltorgs.add(inst);
          counter = -1;
        }
      }
//			System.out.println(counter + ":" + Instructions.get(index));
    }
    calcAddress();
  }

  private void convertLiteral() {
    LtorgInstruction lo, llo, blo;
    LiteralInstruction li;
    ArrayList a;
    String value;
    int i, j, addr;
    Enumeration e;

    // reset
    for (i = 0; i < Ltorgs.size(); i++) {
      ((LtorgInstruction)Ltorgs.get(i)).reset();
    }
    calcAddress();

    // allocate literals in ltorg block
    e = LiteralTable.elements();
    while (e.hasMoreElements()) {

      // If it is already registered, use the registered one.
      // If it is not registered, place it as far as possible. //##74
      a = (ArrayList)e.nextElement();
      llo = blo = null;
      for (i = 0; i < a.size(); i++) {
        li = (LiteralInstruction)a.get(i);
        value = li.getLiteral();
        addr = li.getAddress();
        if (llo != null && cpu.inLiteralRange(llo.getAddressOf(value) - addr)) {
          llo.addLiteral(value);
          li.setLtorg(llo);
//					System.out.println(value + " -> " + llo);
        } else if (blo != null && cpu.inLiteralRange(blo.getAddressOf(value) - addr)) {
          blo.addLiteral(value);
          li.setLtorg(blo);
//					System.out.println(value + " -> " + blo);
        } else {
          llo = blo = null;
          for (j = 0; j < Ltorgs.size(); j++) {
            lo = (LtorgInstruction)Ltorgs.get(j);
            if (cpu.inLiteralRange(lo.getAddressOf(value) - addr)) {
              if (lo instanceof BraLtorgInstruction) {
                blo = (BraLtorgInstruction)lo;
              } else {
                llo = (LtorgInstruction)lo;
              }
            }
          }
          if (llo != null) {
            llo.addLiteral(value);
            li.setLtorg(llo);
//						System.out.println(value + " -> " + llo);
            blo = null;
          } else if (blo != null) {
            blo.addLiteral(value);
            li.setLtorg(blo);
//						System.out.println(value + " -> " + blo);
          } else {
            System.err.println("Literal allocation failed...");
            System.exit(1);
          }
        }
        calcAddress();
      }
    }
  }

  private void convertBranch() {
    int index, offset;
    AsmLine inst;
    String labelName;
    LabelInstruction label;

//		System.out.println("Converting branchs...");
    for (index = 0; index < Instructions.size(); index++) {
      inst = (AsmLine)Instructions.get(index);
      if (inst instanceof BccInstruction) {
        BccInstruction bcc = (BccInstruction)inst;
        labelName = bcc.getLabel();
        label = (LabelInstruction)Labels.get(labelName);
        offset = label.getAddress() - bcc.getAddress();
        if (!cpu.inBccRange(offset)) {
          // rewrite
          Instructions.remove(index);
          String newLabelName = AsmLine.prefix + serialNumber++;
          String[] newcode = { "\t" + bcc.getRevMnemo() + "\t" + newLabelName,
                   "\t" + cpu.braMnemo + "\t" + labelName,
                   "\t.ltorg",
                   newLabelName + ":" };
          for (int j = 0; j < newcode.length; j++) {
            inst = createInstruction(newcode[j]);
            Instructions.add(index++, inst);
          }
          calcAddress();
          changed = true;
//					System.out.println(bcc + " -> " + newcode);
        }
      } else if (inst instanceof BraInstruction) {
        BraInstruction bra = (BraInstruction)inst;
        labelName = bra.getLabel();
        label = (LabelInstruction)Labels.get(labelName);
        offset = label.getAddress() - bra.getAddress();
//				System.out.println("bra=" + label + ",offset=" + offset);
        if (!cpu.inBraRange(offset)) {
          // rewrite
          Instructions.remove(index);
          String[] newcode = cpu.rewriteToLongBranch(labelName);
          for (int j = 0; j < newcode.length; j++) {
            inst = createInstruction(newcode[j]);
            Instructions.add(index++, inst);
          }
          calcAddress();
          changed = true;
//					System.out.println(bra + " -> " + newcode);
        }
      }
    }
  }

  private void convert() {
    int pass = 0;
    calcAddress();
    convertBranch();
    insertBraLtorg();
//		debugDumpInstructions();
    do {
      pass++;
      if (pass > 10) {
        System.err.println("Too many passes...");
        System.exit(1);
      }
//			System.out.println("***PASS*** " + pass);
      changed = false;
      convertLiteral();
      convertBranch();
//			debugDumpInstructions();
    } while (changed);
    // output
    for (int index = 0; index < Instructions.size(); index++) {
      AsmLine l = (AsmLine)Instructions.get(index);
      if (l instanceof LtorgInstruction) {
        int n = ((LtorgInstruction)l).setLabel(serialNumber);
        if (n > 0) {
          serialNumber += n;
        } else {
//					System.out.println("removing " + l);
          Instructions.remove(index);
          index--;
        }
      }
    }
    dumpInstructions();
  }

  private AsmLine parse(String line) {
    StringTokenizer tokens = new StringTokenizer(line, " \t,");
    if (tokens.hasMoreTokens()) {
      String s = tokens.nextToken();
      if (s.charAt(0) == '@' || s.charAt(0) == '#') {
        return new OtherInstruction(line);
      }
      if (s.charAt(s.length() - 1) == ':') {
        return new LabelInstruction(line, s.substring(0, s.length() - 1));
      }
      if (s.equalsIgnoreCase(".align")) {
        return new AlignInstruction(line, Integer.parseInt(tokens.nextToken()));
      } else if (s.equalsIgnoreCase(".ltorg")) {
        return new LtorgInstruction(line);
      } else if (s.equalsIgnoreCase(".ltorg2")) {
        return new BraLtorgInstruction(line);
      } else if (s.equalsIgnoreCase(".word") || s.equalsIgnoreCase(".long")) {
        int params = 0;
        while (tokens.hasMoreTokens()) {
          tokens.nextToken();
          params++;
        }
        return new WordInstruction(line, params);
      } else if (s.equalsIgnoreCase(".short")) {
        int params = 0;
        while (tokens.hasMoreTokens()) {
          tokens.nextToken();
          params++;
        }
        return new ShortInstruction(line, params);
      } else if (s.equalsIgnoreCase(".byte")) {
        int params = 0;
        while (tokens.hasMoreTokens()) {
          tokens.nextToken();
          params++;
        }
        return new ByteInstruction(line, params);
      } else if (cpu.isBcc(s)) {
        return new BccInstruction(line, s, tokens.nextToken());
      } else if (cpu.isBra(s)) {
        return new BraInstruction(line, tokens.nextToken());
      } else if (s.charAt(0) == '.') {
        return new OtherInstruction(line);
      } else {
        while (tokens.hasMoreTokens()) {
          s = tokens.nextToken();
          if (s.startsWith(AsmLine.byteMark) || s.startsWith(AsmLine.shortMark) ||
              s.startsWith(AsmLine.wordMark) || s.startsWith(AsmLine.doubleMark)) {
            return new LiteralInstruction(line, s);
          }
        }
        return new NormalInstruction(line);
      }
    }
    return new OtherInstruction(line);
  }

  private AsmLine createInstruction(String line) {
    AsmLine inst;

    inst = parse(line);
    if (inst instanceof LabelInstruction) {
      Labels.put(((LabelInstruction)inst).getName(), inst);
    }
    if (inst instanceof LiteralInstruction) {
      ArrayList a;
      LiteralInstruction l = (LiteralInstruction)inst;
      String value = l.getLiteral();
      a = (ArrayList)LiteralTable.get(value);
      if (a == null) {
        a = new ArrayList();
        LiteralTable.put(value, a);
      }
      a.add(l);
    }
    return inst;
  }

  private LiteralAndBranchProcessor(String name, OutputStream finalOut) {
    super(name);
    try {
      this.out = new PipedOutputStream();
      this.stream = new PipedInputStream(out);
      this.finalOut = finalOut;
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }

  public OutputStream pipeTo() { return out; }

  public void run() {
    try {
      String line;
      AsmLine inst;
      int pc = 0;
      BufferedReader rdr = new BufferedReader(new InputStreamReader(stream));
      wrt = new PrintWriter(finalOut);

//			System.out.println("thread started");
      while ((line = rdr.readLine()) != null) {
        inst = createInstruction(line);
        pc = inst.setAddress(pc);
        Instructions.add(inst);
        if (inst.getLine().startsWith("@endfunction")) {
          convert();
          Instructions.clear();
          Labels.clear();
          LiteralTable.clear();
          Ltorgs.clear();
          pc = 0;
        }
      }
      convert();
//			System.out.println("thread end");
      wrt.close();
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }


  public static LiteralAndBranchProcessor postProcessor(OutputStream finalOut) {
    LiteralAndBranchProcessor thr = new LiteralAndBranchProcessor("LiteralAndBranchProcessor", finalOut);
    thr.start();
    return thr;
  }

  public void notifyEnd() {
    try {
      out.close();
      join();
    } catch (IOException e) {
      throw new Error(e.getMessage());
    } catch (InterruptedException e) {
      throw new Error(e.getMessage());
    }
  }

  public void setCPU(CPU cpu) {
    this.cpu = cpu;
    AsmLine.cpu = cpu;
  }

  public static void main(String[] args) {
    boolean err = false;
    LiteralAndBranchProcessor pp = postProcessor(System.out);
//		BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("arm")) {
        pp.setCPU(new Arm());
      } else if (args[0].equalsIgnoreCase("thumb")) {
        pp.setCPU(new Thumb());
      } else if (args[0].equalsIgnoreCase("sh4")) {
        pp.setCPU(new Sh4());
      } else {
        err = true;
      }
    } else {
      err = true;
    }
    if (err) {
      System.err.println("Usage: LiteralAndBranchProcessor arm|thumb|sh4");
      System.exit(1);
    }
    try {
      OutputStream out = pp.pipeTo();
      byte[] buffer = new byte[1000];
      int len;
      while ((len = System.in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      out.close();
      pp.notifyEnd();
    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
