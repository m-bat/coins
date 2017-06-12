/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// $Id: ConvToNewLIR.java,v 1.89 2009/04/10 01:44:54 kitamura Exp $

package coins.hir2lir;

import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompileThread;
import coins.driver.Trace;
import coins.HirRoot;
import coins.IoRoot;
import coins.Registry;  //##76
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AsmStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpListExp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirList;  //##101
import coins.ir.hir.IfStmt;
import coins.ir.hir.InfStmt;
import coins.ir.hir.JumpStmt;
import coins.ir.hir.LabelDef;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.PointedExp;
import coins.ir.hir.Program;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.ReturnStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SwitchStmt;
import coins.ir.hir.SymNode;
import coins.ir.hir.VarNode;
import coins.MachineParam;
import coins.MachineParamSparc; //##64
import coins.sym.BaseType;
import coins.sym.Const;
import coins.sym.Elem;
import coins.sym.EnumType;
import coins.sym.Label;
import coins.sym.Param;
import coins.sym.PointerType;
import coins.sym.StringConst;
import coins.sym.StructType;
import coins.sym.Subp;
import coins.sym.SubpType;
import coins.sym.Sym;
import coins.sym.SymIterator;
import coins.sym.SymNestIterator;
import coins.sym.SymTable;
import coins.sym.SymTableIterator;
import coins.sym.Type;
import coins.sym.UnionType;
import coins.sym.Var;
import coins.sym.VectorType;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;  //##101
/**
 * Convert HIR into LIRv2.
 *  Output is written to specified stream in S-expression like form
 *  rather than memory.
 */
public class ConvToNewLIR {
  private File fSourceFile;
  private PrintStream fOut;
  private HirRoot fHirRoot;

  private int tempCtr = 1;
  private int labelCtr = 1;        // LIR label (used op_until)

  private String returnVarName = null;
  private Type retType;
  private ImList autoSym = null;
  private ImList symtab = null;

  private Map symNumbers = new HashMap();
  private Map pureExterns = new HashMap(); // symbol which has SYM_EXTERN must be lookup by name, not by Sym instance.
  private java.util.Set appearedLabels = new HashSet();

  private MachineParam machineParam;

  private String ptrType;

  // source keyword for inline asm
  private static final String ASM_PARAM = "#param";
  private static final String ASM_CLOBBER = "#clobber";

  // LIR lexeme
  private static final String INTCONST = "INTCONST";
  private static final String FLOATCONST = "FLOATCONST";
  private static final String STATIC = "STATIC";
  private static final String FRAME = "FRAME";
  private static final String REG = "REG";
  private static final String SUBREG = "SUBREG";
  private static final String LABEL = "LABEL";
  private static final String NEG = "NEG";
  private static final String ADD = "ADD";
  private static final String SUB = "SUB";
  private static final String MUL = "MUL";
  private static final String DIVS = "DIVS";
  private static final String DIVU = "DIVU";
  private static final String MODS = "MODS";
  private static final String MODU = "MODU";
  private static final String CONVSX = "CONVSX";
  private static final String CONVZX = "CONVZX";
  private static final String CONVIT = "CONVIT";
  private static final String CONVFX = "CONVFX";
  private static final String CONVFT = "CONVFT";
  private static final String CONVFI = "CONVFI";
  private static final String CONVFS = "CONVFS";
  private static final String CONVFU = "CONVFU";
  private static final String CONVSF = "CONVSF";
  private static final String CONVUF = "CONVUF";
  private static final String BAND = "BAND";
  private static final String BOR = "BOR";
  private static final String BXOR = "BXOR";
  private static final String BNOT = "BNOT";
  private static final String LSHS = "LSHS";
  private static final String LSHU = "LSHU";
  private static final String RSHS = "RSHS";
  private static final String RSHU = "RSHU";
  private static final String TSTEQ = "TSTEQ";
  private static final String TSTNE = "TSTNE";
  private static final String TSTLTS = "TSTLTS";
  private static final String TSTLES = "TSTLES";
  private static final String TSTGTS = "TSTGTS";
  private static final String TSTGES = "TSTGES";
  private static final String TSTLTU = "TSTLTU";
  private static final String TSTLEU = "TSTLEU";
  private static final String TSTGTU = "TSTGTU";
  private static final String TSTGEU = "TSTGEU";
  private static final String MEM = "MEM";
  private static final String SET = "SET";
  private static final String JUMP = "JUMP";
  private static final String JUMPC = "JUMPC";
  private static final String JUMPN = "JUMPN";
  private static final String DEFLABEL = "DEFLABEL";
  private static final String CALL = "CALL";
  private static final String PROLOGUE = "PROLOGUE";
  private static final String EPILOGUE = "EPILOGUE";
  private static final String PARALLEL = "PARALLEL";
  private static final String USE = "USE";
  private static final String CLOBBER = "CLOBBER";
  private static final String PHI = "PHI";
  private static final String IF = "IF";
  private static final String LIST = "LIST";
  private static final String SPACE = "SPACE";
  private static final String ZEROS = "ZEROS";
  private static final String LINE = "LINE";
  private static final String INFO = "INFO";
  private static final String UNKNOWN = "UNKNOWN";
  private static final String MODULE = "MODULE";
  private static final String SYMTAB = "SYMTAB";
  private static final String DATA = "DATA";
  private static final String FUNCTION = "FUNCTION";
  private static final String LDEF = "LDEF";
  private static final String XDEF = "XDEF";
  private static final String XREF = "XREF";
  private static final String ASM = "ASM";
  // profile info
  private static final String HIR_PROF = "simulate";
  private static final String LIR_PROF = "SIMULATE";
  private static final String PROF_ON = "profileOn";
  private static final String PROF_OFF = "profileOff";
  // modifiers
  private static final String VOLATILE_M = "&V";
  private static final String ALIGN_M = "&align";
  private static final String ARG_M = "&argtype";
  private static final String CLOB_M = "&clobber";
  private static final String BODY_M = "&body";
  // info
  private static final String OPTSYM_M = "&syminfo";
  private static final String SYMINFO = "SYMINFO";
  private static final String ID_M = "&id";

  // todo: read tmd ?
  private static final QuotedString CSEG = new QuotedString(".text");
  private static final QuotedString DSEG = new QuotedString(".data");
  private static final QuotedString BSEG = new QuotedString(".bss");
  private static final QuotedString ROSEG = new QuotedString(".rodata");
  private static QuotedString LITERAL_SEG = CSEG; /* or CSEG or DSEG */
  private static final String prefLirLabel = "_llab";

  private static final String ZERO = "0";
  private static final QuotedString _EPILOGUE = new QuotedString("_epilogue");
  private static final String PADDING = ZEROS;

  private static final QuotedString BUILTIN_ALIGN = new QuotedString("__builtin_align");

  private ImList Lseq;
  private ImList Lbody;                // { Ldata | Lfunc }
  private ImList DZSSeq;        // { DataSeq | ZeroSeq | SpaceSeq }

  private boolean returnUsed;
  private int curline = -1;        // current source line number from hir
  private boolean autoString = false;
  // Is next literal auto ?
  // modified by op_setdata, op_assign
  private long vectorAssignLen = -1;        // set by op_assign
  private long dstVectorLen = -1; // set by op_assign
  // modified by __builtin_align, reset by op_contents
  private int builtin_align = 0;

  private long regionLoc = 0; // offset in region

  private boolean generateLineDefault = false; // debuginfo
  private boolean generateLine = false;        // depend prof and debuginfo
  private boolean generateProfileInfo = false;
  private boolean simulateOption = false; // true if simulate option is given. //##75
  private StringConverter strconv;

  private boolean emitRoseg = false;

  private boolean fixIdBug = false;

  private boolean debugUniqFun =
    false
    // true
    ;
  private int debugLevel = 0;  //##76
  private Map hirOptMap; //##81

  private boolean supportLabeledNull = true;

  private int memNum = 0;

  // emit unused HIR symbol to LIR SYMTAB ?
  private boolean omitUnusedSymbol = true;

  private void debug(String s) {
    // System.out.println(s);
    if (debugLevel > 0)      //##76
      System.out.println(s); //##76
  }

  private void bug(String s) {
    System.out.println("bug?:" + s);
  }
  // invalid HIR
  private void badHIR(String s) {
    System.out.println("bad HIR?: " + s);
  }

  // wrapper function
  private int getAlignment(Sym sym) {
    // Wed Jun  2 17:18:45 JST 2004
    // backend requires (align > 0)
    int align = sym.getSymType().getAlignment();
    if (align <= 0) {
      bug("align <= 0: " + sym);
      align = 4;
    }
    return align;
  }

  // NumSeq of LIR
  private class NumSeq {
    private Type type;                 // Ltype of this (Vector only)
    ImList seq;                        // { Fixnum | Flonum | Lexp }

    public NumSeq() {
      seq = ImList.Empty;
      type = null;
    }

    void add(ImList l) {        // ignores add(null)
      if (l != null) {
        seq = new ImList(l, seq);
      }
    }

    void addZeros(long n) {
      toDZSseq();                // flush first
      DZSSeq = new ImList(zeros(n), DZSSeq);
      seq = ImList.Empty;
    }

    // destructive promote
    void toDZSseq() {
      if (seq == ImList.Empty) {
        return;
      }
      ImList tmp = reverse(seq);
      if (type != null) {
        tmp = prefix(htype2ltype(type), tmp);
      }
      DZSSeq = new ImList(tmp, DZSSeq);
      seq = ImList.Empty;
    }

    Type replType(Type ty) {
      Type tmp = type;
      type = ty;
      return tmp;
    }

    long getElemSize() {
      return type.getSizeValue();
    }
  }
  private NumSeq numSeq;

  //
  private class StructOffset {
    long total;                  // total byte count of generated member
    long bitoff;
    long bitList;                // bit field initializer
    long valid;

    public StructOffset() {
      total = 0;
      bitoff = 0;
      bitList = 0;
      valid = 0;
    }

    void align(int n) {
      int tmp = (int) (n - total % n) % n;
      if (tmp > 0) {
        debug("explist align: " + n + " pad: " + tmp);
        DZSSeq = new ImList(list(PADDING, String.valueOf(tmp)), DZSSeq);
        total += tmp;
      } else {
        // System.out.println("OK aligned");
      }
    }

    void add(long n) {
      total += n;
    }
    void set(long n) {
      total = n;
    }

    void padding(long offset) {
      if (total > offset) {
        // Wed Jan 26 17:39:18 JST 2005
        /*
          struct { char c; / * A * / int i : 1; };
          at point A, total = 1, byte offset of i = 0 (bit offset = 8 or 24)
          so calling padding() at point A, total > offset is true.
        */
        // bug("offset " + offset + " < total " + total);
        return;
      }
      if (total < offset) {
        debug("total: " + total + " offset: " + offset + " -> padding " + (offset - total));
        DZSSeq = new ImList(list(PADDING, String.valueOf(offset - total)), DZSSeq);
      }
      total = offset;
    }

    void flushBitField(long byteoff) {
      debug("*** flushBitf: ofs = " + total + ", bit = " + bitoff);
      debug("        <-> byteoff = " + byteoff);
      //##88 int unitLeng = machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT; //##87
      int unitLeng = machineParam.numberOfBitsInAddressingUnit(); //##88
      int unitLengMinus1 = unitLeng - 1; //##87
      if (bitoff != 0 && total < byteoff) {
        //##87 int nbytes = (int) (bitoff + 7) / 8;
        int nbytes = (int) (bitoff + unitLengMinus1) / unitLeng; //##87
        ImList tmp = ImList.Empty;

        debug("         flushing value " + Long.toHexString(bitList));
        debug("          nbytes = " + nbytes);
        debug("         valid bits = " + Long.toHexString(valid));

        if (machineParam.isLittleEndian()) {
          //##87 for (int i = 0; i < 8; i++)
          for (int i = 0; i < unitLeng; i++)  //##87
         {
            //##87 if ((valid & (0xff << (i * 8))) != 0)
            if ((valid & (0xff << (i * unitLeng))) != 0) //##87
            {
              break;
            } else {
              // System.out.println(" ... skip ");
              //##87 bitList >>>= 8;
              bitList >>>= unitLeng; //##87
           }
          }

          switch (nbytes) {
          case 1:
          case 2:
          case 4:
          case 8:
            tmp = prefix(String.valueOf(bitList), tmp);
            DZSSeq = new ImList(prefix(n2Ixx(nbytes),  tmp), DZSSeq);
            break;
          default:
            for (int i = 0; i < nbytes; i++) {
              //##87 tmp = prefix(String.valueOf((bitList >> (i * 8)) & 0xff), tmp);
              tmp = prefix(String.valueOf((bitList >> (i * unitLeng)) & 0xff), tmp); //##87 ??
            }
            tmp = reverse(tmp);
            //##87 DZSSeq = new ImList(prefix("I8", tmp), DZSSeq);
            DZSSeq = new ImList(prefix(prefixStringForChar(), tmp), DZSSeq); //##87 ??
            // It is assumed that character is the shortest data. //##87
            break;
          }
        } else {
          //##87 for (int i = 7; i > 0; i++)
          for (int i = unitLengMinus1; i > 0; i++)  //##87
          {
            //##87 if ((valid & (0xff << (i * 8))) != 0)
             if ((valid & (0xff << (i * unitLeng))) != 0)  //##87
            {
              break;
            } else {
              // System.out.println(" ... skip ");
              //##87 bitList <<= 8;
              bitList <<= unitLeng; //##87
            }
          }

          switch (nbytes) {
          case 4:
          case 8:
            tmp = prefix(String.valueOf(bitList), tmp);
            DZSSeq = new ImList(prefix(n2Ixx(nbytes),  tmp), DZSSeq);
            break;
          default:
            int w = (nbytes > 4) ? 8 : 4;   //## ??

            debug("  value = " + bitList);
            for (int i = 1; i <= nbytes; i++) {
              // System.out.print(" " + (bitList >> ((w - i) * 8)));

              //##87 tmp = prefix(String.valueOf((bitList >> ((w - i) * 8)) & 0xff), tmp);
              tmp = prefix(String.valueOf((bitList >> ((w - i) * unitLeng)) & 0xff), tmp); //##87 ??
            }
            tmp = reverse(tmp);
            //##87 DZSSeq = new ImList(prefix("I8", tmp), DZSSeq);
            DZSSeq = new ImList(prefix(prefixStringForChar(), tmp), DZSSeq); //##87
            // It is assumed that character is the shortest data. //##87
            break;
          }
        }
        bitList = 0;
        total += nbytes;
        debug(" total = " + total);
        debug(" byteoff = " + byteoff);

        padding(byteoff);
        bitoff = 0;
        valid = 0;
      } else {
        // it's no time to flush.
      }
    }

    void pack(long l, int w, long m) {
      bitList |= l;
      valid |= m;
      bitoff += w;
      debug("pack wid, msk = " + w + ", " + Long.toHexString(m) + " <- val " + Long.toHexString(l));
    }
  }

  StructOffset stroff;

  class SymStat {
    private int symNum;
    public final static int KEEP = -2;
    public final static int UNUSED = -1;

    private int state;
    public final static int UNKNOWN = 0;
    private final static int INSTALLED = 1; // LIR symbol table
    private final static int GENERATED = 2; // need initialization ?
    private final static int REWRITE = 4;
    private final static int USED = 8;
    private final static int INITIAL_VALUE = REWRITE | USED;

    private Sym sym;
    SymStat(Sym sym) {
      symNum = UNUSED;
      state = INITIAL_VALUE;
      this.sym = sym;
    }
    int numbering() {
      if (isRewrite()) {
        symNum = tempCtr++;
      } else {
        symNum = KEEP;
      }
      return symNum;
    }
    int num() { return symNum; }
    void setLirSym() { state |= INSTALLED; }
    boolean isLirSym() { return (state & INSTALLED) != 0; }
    void needNotInit() { state |= GENERATED; }
    boolean isGenInit() { return (state & GENERATED) != 0; }
    void resetRewrite() { state &= ~REWRITE; }
    void setRewrite() { state |= REWRITE; }
    boolean isRewrite() { return (state & REWRITE) != 0; }
    Sym getSym() { return sym; }
    boolean isExtern() {
      int v = Sym.SYM_PRIVATE; {
        if (sym instanceof Subp) {
          v = ((Subp) sym).getVisibility();
        } else if (sym instanceof Var) {
          v = ((Var) sym).getVisibility();
        }
      }
      return (v == Sym.SYM_EXTERN);
    }
    void resetUsed() {
      if (isExtern() && pureExterns.get(sym.getName()) != null) {
        //        ((SymStat) pureExterns.get(sym.getName())).resetUsed();
      }
      state &= ~USED;
    }
    void setUsed() {
      if (isExtern() && pureExterns.get(sym.getName()) != null) {
        //        ((SymStat) pureExterns.get(sym.getName())).setUsed();
      }
      state |= USED;
    }
    boolean isUsed() { return (state & USED) != 0; }
  }

  private class ConvContext {
    private boolean isLvalue;
    private boolean emitIfExp;

    public ConvContext() {
      isLvalue = false;
      emitIfExp = false;
    }
    public ConvContext(ConvContext cc) {
      isLvalue = cc.isLvalue;
      emitIfExp = cc.emitIfExp;
    }

    public ConvContext(boolean isLvalue, boolean emitIfExp) {
      this.isLvalue = isLvalue;
      this.emitIfExp = emitIfExp;
    }

    public ConvContext lvalue() {
      ConvContext tmp = new ConvContext(this);
      tmp.isLvalue = true;
      return tmp;
    }
    public ConvContext nonLvalue() {
      ConvContext tmp = new ConvContext(this);
      tmp.isLvalue = false;
      return tmp;
    }
    public ConvContext emitIfExp() {
      ConvContext tmp = new ConvContext(this);
      tmp.emitIfExp = true;
      return tmp;
    }
    public ConvContext noIfExp() {
      ConvContext tmp = new ConvContext(this);
      tmp.emitIfExp = false;
      return tmp;
    }
    public boolean isLvalue() {
      return isLvalue;
    }
    public boolean isEmitIfExp() {
      return emitIfExp;
    }

  }
  private final ConvContext lvalue_noIfExp = new ConvContext(true, false);
  private final ConvContext lvalue_emitIfExp = new ConvContext(true, true);
  private final ConvContext nonLvalue_noIfExp = new ConvContext(false, false);
  private final ConvContext nonLvalue_emitIfExp = new ConvContext(false, true);

  private final ConvContext topLevelCc = nonLvalue_emitIfExp;

  // params. of asm-exp
  class AsmParam {
    String io;
    String ty;
    int pos;

    AsmParam() {
      io = ty = null;
      pos = -1;
    }

    void setType(String s) {
      ty = s.intern();

    }
    void setIo(String s) {
      io = s.intern();
    }
    void setPos(int i) {
      pos = i;
    }

    String getType() {
        if (ty == null) {
            bug("type is not set.");
        }
      return ty;
    }
    String getIo() {
      return io;
    }
    int getPos() {
      return pos;
    }

    boolean isValid() {
      if (this == no_param) {
        return false;
      }
      return (ty != null);
    }

    AsmParam addTo(AsmParamList l) {
      if (isValid()) {
        l.add(this);
      } else {
        // ignore bad parameters
        debug("adding bad param: " + this);
      }
      return no_param;
    }

    public String toString() {
      if (isValid()) {
        return "type = " + ty + ", io = " + ((io == null) ? "r" : io);
      }
      return "bad parameter spec. \"" + ty + io + "\"";
    }
  }

  class AsmParamList {
    ArrayList l;

    AsmParamList() {
      l = new ArrayList();
    }

    int size() {
      return l.size();
    }
    void add(AsmParam p) {
      l.add(p);
    }

    AsmParam get(int i) {
      return (AsmParam) l.get(i);
    }
  }

  AsmParam no_param = new AsmParam();

  /**
   * Construct ConvToNewLIR instance (singleton).
   *
   * Return the object created
   * @param pOut PrintStream object on which S-form LIR written.
   */
  public ConvToNewLIR(File pSourceFile, OutputStream pOut, HirRoot pHirRoot) {
    fSourceFile = pSourceFile;
    fOut = new PrintStream(pOut);
    fHirRoot = pHirRoot;
    strconv = new PlainStringConverter();
    debugLevel = fHirRoot.ioRoot.dbgToLir.getLevel(); //##76
    /*strconv = new LiteralStringConverter();*/
    {         // Wed Jun 30 14:38:34 JST 2004
      Thread th = Thread.currentThread();
      if (th instanceof CompileThread) {
        IoRoot io = ((CompileThread) th).getIoRoot();
        machineParam = io.machineParam;
        ptrType = hkind2ltype(Type.KIND_POINTER);
        CompileSpecification spec = io.getCompileSpecification();
        CoinsOptions coinsOptions = spec.getCoinsOptions();
        //##81 BEGIN
        String lHirOpt = coinsOptions.getArg("hirOpt"); //##81
        if (lHirOpt != null)
          hirOptMap = (Map)coinsOptions.parseArgument(lHirOpt, '/', '.'); //##81
        else
          hirOptMap = new HashMap();
        //##81 END
        if (coinsOptions.isSet("debuginfo")
            || coinsOptions.isSet(HIR_PROF)) {
          generateLineDefault = true;
          generateLine = generateLineDefault;
          if (coinsOptions.isSet(HIR_PROF)) { //##75
            simulateOption = true; //##75
          }
        } else {
          debug("coins:debuginfo is not set");
        }
        // Thu Mar 23 16:02:37 JST 2006
        // for hir unique name
        SymTable rootSymTab = io.symRoot.symTableRoot;
        rootSymTab.setUniqueNameToAllSym();

        // Tue Aug  8 15:23:21 JST 2006
        // for MicroBlaze
        if (coinsOptions.getArg(CommandLine.COINS_TARGET_NAME_OPTION).equals("mb")) {
            emitRoseg = true;
            LITERAL_SEG = ROSEG;
        }
      } else {
        // Mon Jul 12 18:55:13 JST 2004
        // this thread is not instanceof CompileThread ...
        machineParam = new MachineParamSparc(fHirRoot.ioRoot); //##64
      }
    }
  }

  /** Reverses ImList */
  private ImList reverse(ImList l) {
    return l.destructiveReverse();
  }

  /** Returns quoted representaion of s */
  private QuotedString quote(String s) {
    return new QuotedString(s);
  }
  /* Returns unique name of Sym */
  private QuotedString uniqName(Sym pSym) {
    if (pSym == null) {
      return quote("bug: uniqName(null)");
    }
    if (isRewrite(pSym)) {
      int varnum = getSymNum(pSym);
      if (varnum != SymStat.UNKNOWN) {
        // Tue Jul 22 18:18:54 JST 2003 by ak
        return quote(pSym.getName() + "." + varnum);
      }
    }
    return quote(pSym.getName());
  }
  // Tue Jul 22 18:18:54 JST 2003 by ak
  private String uniqStrName(int pNum, String pName) {
    return pName + "." + pNum;
  }

  /* list functions */
  private Object car(Object l) {
    return ((ImList) l).elem();
  }
  private Object cdr(Object l) {
    return ((ImList) l).next();
  }

  private ImList prefix(Object x, ImList y) {
    return new ImList(x, y);
  }
  private ImList prefix(Object x, Object y, ImList z) {
    return new ImList(x, new ImList(y, z));
  }
  private ImList prefix(Object x, Object y, Object z, ImList w) {
    return new ImList(x, new ImList(y, new ImList(z, w)));
  }
  private ImList prefix(Object x, Object y, Object z, Object w, ImList u) {
    return new ImList(x, new ImList(y, new ImList(z, new ImList(w, u))));
  }
  private ImList prefix(Object x, Object y, Object z, Object w, Object u, ImList v) {
    return new ImList(x, new ImList(y, new ImList(z, new ImList(w, new ImList(u, v)))));
  }

  private ImList list(Object x) {
    return new ImList(x, ImList.Empty);
  }
  private ImList list(Object x, Object y) {
    return new ImList(x, new ImList(y, ImList.Empty));
  }
  private ImList list(Object x, Object y, Object z) {
    return new ImList(x, new ImList(y, new ImList(z, ImList.Empty)));
  }
  private ImList list(Object x, Object y, Object z, Object w) {
    return new ImList(x, new ImList(y, new ImList(z, new ImList(w, ImList.Empty))));
  }
  private ImList list(Object x, Object y, Object z, Object w, Object u) {
    return new ImList(x, new ImList(y, new ImList(z, new ImList(w, new ImList(u, ImList.Empty)))));
  }
  private ImList list(Object x, Object y, Object z, Object w, Object u, Object v) {
    return new ImList(x, new ImList(y, new ImList(z, new ImList(w, new ImList(u, new ImList(v, ImList.Empty))))));
  }

  /** Add s to Lexp */
  private void addLexp(ImList s) {
    if (s != null) {
      Lseq = new ImList(s, Lseq);
    }
  } 
  /* Add s to Lexp if generateProfileInfo */
  private void addProfInfo(ImList s) {
    if (simulateOption) { //##75 fujise,tan
      if (s != null /*&& generateProfileInfo*/) {
        Lseq = new ImList(s, Lseq);
      }
    } //##75 fujise,tan
  }

  private ImList bblockProfInfo = ImList.Empty;

  /** Add info and s to Lexp */
  private void addLexpAfterInfo(ImList s) {
    addLexp(s);

    bblockProfInfo = s;

    /* This INFO node represents lineno of Basic block, not lineno of first stmt.
     * (eg. lineno of Basic block != lineno of first stmt of basic block)
     * But simulator treats this value as the latter not the former, so emit later.

     * ImList t = list(s, list(LINE, String.valueOf(curline)));
     * addProfInfo(new ImList(INFO, t));
     * // addLexp(new ImList(INFO, s));
    */
  }
  /** Add s and info to Lexp */
  private void addLexpBeforeInfo(ImList s) {
    ImList t = list(s, list(LINE, String.valueOf(curline)));
    addProfInfo(new ImList(INFO, t));
    addLexp(s);
  }

  /** Add s to Lbody */
  private void addLbody(ImList s) {
    if (s != null) {
      Lbody = new ImList(s, Lbody);
    }
  }
  /** Is exp Const 0 ? */
  private boolean isConstZero(HIR exp) {
    if (!(exp instanceof ConstNode)) {
      return false;
    }
    Const c = ((ConstNode) exp).getConstSym();
    if ((exp.getType().isFloating() && c.doubleValue() == 0.0)
        || (!exp.getType().isFloating() && c.longValue() == 0)) {
      return true;
    }
    return false;
  }

  private boolean isScalerType(Type ty) {
    return (ty instanceof BaseType
            || ty instanceof PointerType
            || ty instanceof EnumType);
  }

  private boolean isStrunion(Type ty) {
    return (ty instanceof StructType || ty instanceof UnionType);
  }

  private boolean isLiteral(HIR pHir) {
    if (pHir.getOperator() == HIR.OP_CONST) {
      Sym sym = ((ConstNode)pHir).getConstSym();
      if (sym.getSymKind() == Type.KIND_STRING_CONST) {
        return true;
      }
    }
    return false;
  }

  /**
   * Convert HIR to LIR
   * @param pProg HIR (must be Program object: why HIR?)
   */
  public ImList doConvert(HIR pProg) {
    ImList root = ImList.Empty;

    // Convert global symbols
    symtab = ImList.Empty;
    Lbody = ImList.Empty;

    // Wed Jun        2 11:15:43 JST 2004 by ak
    // numbering all symbols first, but not install here.
    {
      SymTableIterator stab_it = ((Program)pProg).getSymTable().getSymTableIterator();
      boolean isTop = true;

      if (debugUniqFun) {
        System.out.println("\n*** numbering all symbols");
      }

      while (stab_it.hasNext()) {
        SymTable stab = stab_it.next();

        if (debugUniqFun) {
          System.out.println("        ---- " + stab);
        }
        SymIterator symit2 = stab.getSymIterator();
        while (symit2.hasNext()) {
          Sym sym = symit2.next();
          if (isLirSym(sym)) {
            bug("already installed: " + sym);
          }
          if (sym.getSymKind() == Sym.KIND_ELEM) {
            install(sym);
            needNotInit(sym);
            keepName(sym);
            setUsed(sym);
            if (debugUniqFun) {
              System.out.println("    + " + sym + " -> " + uniqName(sym));
            }
          }
          if (sym.getSymKind() == Sym.KIND_SUBP
              || sym.getSymKind() == Sym.KIND_VAR) {
            install(sym);

            if (sym.getSymKind() == Sym.KIND_SUBP) {          // subp not used
              needNotInit(sym);
            }

            // add suffix (symbol number) or not
            int v = Sym.SYM_PRIVATE; {
              if (sym instanceof Subp) {
                v = ((Subp) sym).getVisibility();
              } else if (sym instanceof Var) {
                v = ((Var) sym).getVisibility();
              } else {
                badHIR("neither Subp nor Var: " + sym);
              }
            }
            switch (v) {
            case Sym.SYM_PUBLIC:
            case Sym.SYM_COMPILE_UNIT:
              keepName(sym);
              break;
            case Sym.SYM_EXTERN:
              keepName(sym);
              if (pureExterns.get(sym.getName()) == null) {
                pureExterns.put(sym.getName(), "");
                needNotInit(sym);
                resetUsed(sym);
              }
              break;
            case Sym.SYM_PRIVATE:
              if (isTop) {
                keepName(sym);
              } else {
                uniqSym(sym);
              }
              break;
            default:
              uniqSym(sym);
              break;
            }
            numbering(sym);
            if (debugUniqFun) {
              System.out.println("    + " + sym + " -> " + uniqName(sym));
            }
          } else if (sym.getSymKind() == Sym.KIND_PARAM) {
            install(sym);
            // always add suffix.
            uniqSym(sym);
            needNotInit(sym);
            numbering(sym);
            if (debugUniqFun) {
              System.out.println("    + " + sym + " -> " + uniqName(sym));
            }
          }
        }
        isTop = false;
      }
    }

    // Generate functions
    ListIterator subpit = ((Program)pProg).getSubpDefinitionList().iterator();

    while (subpit.hasNext()) {
      SubpDefinition subp = (SubpDefinition)subpit.next();

       // update curline before converting subpdef
       ImList line = null;
       if (subp.getChild1() == null) {
         badHIR("SubpDefinition.getChild1() returns null. " +
                subp.toStringDetail());
       }
       else {
         Sym sym = ((SymNode)subp.getChild1()).getSymNodeSym();
         if (sym.getDefinedFile() != null) {
           line = list(INFO, LINE, String.valueOf(sym.getDefinedLine()));
           curline = sym.getDefinedLine();
         }
      }

      ImList tmp = convertSubpDef(subp);

      // Tue Aug 31 20:34:44 JST 2004
      if (generateLine) {
        if (subp.getChild1() == null) {
          badHIR("SubpDefinition.getChild1() returns null. " +
                 subp.toStringDetail());
        }
        else {
          Sym sym = ((SymNode)subp.getChild1()).getSymNodeSym();
          if (sym.getDefinedFile() != null) {
            addLbody(list(INFO, LINE, String.valueOf(sym.getDefinedLine())));
          }
        }
      }

      addLbody(tmp);
      // subs = prefix(tmp, subs);
      // System.out.println("subp: " + tmp);
      // Generate initiation after static var numbering.
      HIR initPart = (HIR) subp.getInitiationPart();
      /* todo: check me ? */

      if (initPart != null && initPart instanceof BlockStmt) {
        Stmt st = ((BlockStmt) initPart).getFirstStmt();
        convertNode(st, topLevelCc);
      }
    }

    // Fri May        9 20:57:23 JST 2003 by ak
    // initiation
    {
      HIR initPart = (HIR) ((Program)pProg).getInitiationPart();
      if (initPart != null && initPart instanceof BlockStmt) {
        Stmt tmp = ((BlockStmt) initPart).getFirstStmt();
        if (tmp != null) {
          addLbody(convertNode(tmp, topLevelCc));
        }
      }
    }

    // generate not initialized static
    {
      Collection view = symNumbers.values();
      Iterator it = view.iterator();

      while (it.hasNext()) {
        Sym sym = ((SymStat) it.next()).getSym();
        if (!isGenInit(sym)) {
          lineInfo(sym);

          String size = String.valueOf(sym.getSymType().getSizeValue());
          ImList tmp = list(DATA, uniqName(sym), list(SPACE, size));
          addLbody(tmp);
          needNotInit(sym);
        }
      }
    }

    // emit symbols
    if (debugUniqFun) {
      System.out.println("\n*** global symbol table");
      ((Program)pProg).getSymTable().printSymTableDetail();
    }

    SymIterator symit = ((Program)pProg).getSymTable().getSymIterator();
    while (symit.hasNext()) {
      Sym sym = symit.next();
      String linkage;
      QuotedString segment;

      if (omitUnusedSymbol && !isUsed(sym)) {
        debug("          " + sym + " is not used.");
        continue;
      }

      switch (sym.getSymKind()) {
      case Sym.KIND_VAR:
        // Thu Mar 10 18:45:20 JST 2005
        if (!(sym instanceof Var) || ((Var)sym).getStorageClass() != Var.VAR_STATIC) {
          bug("non-static global is not supported in LIR: " + sym);
        }
        if (emitRoseg && ((Var)sym).getSymType().isConst()) {
          segment = ROSEG;
        } else {
          segment = DSEG;
        }
        break;
      case Sym.KIND_SUBP:
        segment = CSEG;
        break;
      default:
        continue;
      }

      linkage = null; // Thu Mar 10 18:29:12 JST 2005
      boolean publicVar = false;
      if (sym instanceof Var) {
        switch (((Var)sym).getVisibility()) {
          /* Wed Sep 24 19:50:04 JST 2003 */
          /* add non-public initiation */
        case Sym.SYM_PUBLIC:
        case Sym.SYM_PROTECTED: // NB: C has not protected ...
        case Sym.SYM_COMPILE_UNIT:
        case Sym.SYM_PRIVATE:
          if (((Var)sym).getVisibility() == Sym.SYM_PUBLIC) {
            linkage = XDEF;
          } else {
            linkage = LDEF;
          }
          if (((Var)sym).getInitialValue() == null) {
            segment = BSEG;
          } else {
            if (emitRoseg && ((Var)sym).getSymType().isConst()) {
              segment = ROSEG;
            } else {
              segment = DSEG;
            }
          }
          break;
        case Sym.SYM_EXTERN:
          linkage = XREF;
          // Mon Apr 19 18:42:48 JST 2004
          if (pureExterns.get(sym.getName()) == null) {
            pureExterns.put(sym.getName(), "");
          }
          break;
        }
      } else if (sym instanceof Subp) {
        switch (((Subp)sym).getVisibility()) {
        case Sym.SYM_PUBLIC:
          linkage = XDEF;
          break;
        case Sym.SYM_EXTERN:
          linkage = XREF;
          // Mon Apr 19 18:42:48 JST 2004
          if (pureExterns.get(sym.getName()) == null) {
            pureExterns.put(sym.getName(), "");
          }
          needNotInit(sym);
          break;
          // Thu Mar 11 18:23:14 JST 2004
        case Sym.SYM_PRIVATE:
        case Sym.SYM_COMPILE_UNIT:
          linkage = LDEF;
          break;
        }
      }

      // Thu Mar 10 18:25:57 JST 2005
      if (linkage == null) {
        badHIR("invalid storage class or linkage of global symbol (treat as LDEF): " + sym);
        linkage = LDEF;
      }
      ImList tmp = staticEnt(sym, segment, linkage);
      symtab = new ImList(tmp, symtab);
      setLirSym(sym);
    }

    // Close symtab
    if (symtab != ImList.Empty) {
      symtab = new ImList(SYMTAB, symtab);
      //System.out.println("sym: " + symtab);
    }

    ImList subs = reverse(Lbody);
    if (symtab != ImList.Empty) {
      subs = prefix(symtab, subs);
    }

    if (generateLineDefault && fSourceFile.getParent() != null) {
      root = prefix(MODULE, quote(
			fSourceFile.getParent()
			+ java.io.File.separator
			+ fSourceFile.getName())
		, subs);
    } else {
      root = prefix(MODULE, quote(fSourceFile.getName()), subs);
    }

    Thread th = Thread.currentThread();
    if (th instanceof CompileThread) {
      IoRoot io = ((CompileThread) th).getIoRoot();
      CompileSpecification spec = io.getCompileSpecification();
      Trace trace = spec.getTrace();
      if (trace.shouldTrace("LIR", 1)) {
        // Print to System.out, as Trace class doesn't provide
        // getTraceOut().  (Current implementation always use
        // System.out for TraceOut.)
        System.out.println("\n\nAfter HIR to LIR:");
        ImPrinter printer = new ImPrinter(new PrintWriter(System.out));
        printer.print(root);
      }
    }

    return root;
  }

  // Return subprogram's symbol entry
  private Sym subpSymbol(SubpDefinition pSubp)
    {
      HIR p = pSubp;

      for (; p != null; p = (HIR)p.getParent()) {
        if (p.getOperator() == HIR.OP_SUBP_DEF)
          return ((SubpDefinition)p).getSubpSym();
      }
      return null;
    }

  // Convert a subprogram
  private ImList convertSubpDef(SubpDefinition pSubp)
    {
      Subp funSym = (Subp)subpSymbol(pSubp);
      fHirRoot.symRoot.subpCurrent = funSym; //##81
      retType = ((SubpType)funSym.getSymType()).getReturnType();

      // Thu Aug 10 15:30:34 JST 2006
      // par function default is simulate off.
      generateProfileInfo = false;

      if (debugUniqFun) {
        System.out.println("");
        System.out.println("*** local symbol table of subp " + funSym);
        pSubp.getSymTable().printSymTableDetail();
        System.out.println("");
      }

      // Write local static symbols and global function to global symbol
      // table
      {
        SymNestIterator symit = pSubp.getSymTable().getSymNestIterator();

        while (symit.hasNext()) {
          Sym sym = symit.next();
          String type;

          if (isLirSym(sym)) {
            continue;
          }
          // install to LIR symtab
          ImList tmpsym = symEnt("", sym);
          if (tmpsym != null) {
            symtab = prefix(tmpsym, symtab);
          }
        }
      }

      // Write local automatic symbols
      {
        SymTableIterator stab_it = ((SymTable) pSubp.getSymTable()).getSymTableIterator();
        autoSym = ImList.Empty;

        while (stab_it.hasNext()) {

          SymTable stab = stab_it.next();
          Sym owner = stab.getOwner();

          if (debugUniqFun) {
            System.out.println("");
            System.out.println("*** local symbol table of " + owner);
            stab.printSymTableDetail();
            System.out.println("");
          }

          if (owner instanceof Subp && owner != funSym) {
            //System.out.println("  owner " + owner + " != " + funSym);
            break;
          }

          SymIterator symit = stab.getSymIterator();
          while (symit.hasNext()) {
            Sym sym = symit.next();

            switch (sym.getSymKind()) {
            case Sym.KIND_PARAM:
            case Sym.KIND_VAR:
              // assert(sym instanceof VAR);
              if (((Var)sym).getStorageClass() == Var.VAR_AUTO) {
                //store = FRAME;
              } else if (((Var)sym).getStorageClass() == Var.VAR_REGISTER) {
                //store = REG;
              } else {
                // Maybe static
                continue;
              }
              break;
            default:
              continue;
            }
            ImList tmp = autoEnt(sym);
            autoSym = prefix(tmp, autoSym);
            needNotInit(sym);
          }
        }
      }

      // Prepare temporary variable for the value returned
      returnVarName = null;
      if (retType.getTypeKind() != Type.KIND_VOID) {
        returnVarName = uniqStrName(tempCtr++, "returnvalue");
        ImList tmp = frameEnt(returnVarName,
                              htype2ltype(retType),
                              String.valueOf(retType.getAlignment()),
                              ZERO); // NB: no optional field.
        autoSym = prefix(tmp, autoSym);
      }

      Lseq = ImList.Empty;
      // Generate Prologue
      {
        ImList prologue = ImList.Empty;

        // Tue Aug  8 16:42:04 JST 2006
        // moved from here. see below.

        HIR pHir = pSubp.getHirBody();
        pHir = (HIR) pHir.getChild2();
        if (pHir.getOperator() == HIR.OP_BLOCK) {
          pHir = (HIR) pHir.getChild1();
          if (pHir != null && pHir.getOperator() == HIR.OP_INF) {
          if ((((InfStmt)pHir).getInfKind() == HIR_PROF) && //##75
              simulateOption) { //##75
              addLexp(convertInfo(pHir));
              if (false) { // to be delete
                  InfStmt inf = (InfStmt) pHir;
                  System.out.println("inf of block: kind = " + inf.getInfKind());
                  IrList infs = pHir.getInfList();
                  if (infs != null) {
                      ListIterator it = infs.iterator();
                      try {
                          String key = (String) it.next();
                          if (key == HIR_PROF) {
                              // prof takes one IrList.
                              Object tmp = it.next();
                              if (tmp instanceof IrList) {
                                  ListIterator pit = ((IrList) tmp).iterator();
                                  String val = (String) pit.next();
                                  // addLexp(list(INFO, LIR_PROF, val)); // always add this info.
                                  if (val == PROF_ON) {
                                      generateProfileInfo = true;
                                      generateLine = true;
                                  } else if (val == PROF_OFF) {
                                      generateProfileInfo = false;
                                      generateLine = generateLineDefault;
                                  }
                              } else {
                                  bug("bad profile pragma: " + tmp.toString());
                              }
                          }
                      } catch (ClassCastException e) {
                          // not pragma, user customized info ? ignore it.
                          // why ignore ? ...
                      }
                  }
              }
          } //##75
          }
        }

        // Tue Aug  8 16:41:15 JST 2006
        // moved from above position.
        // Mon Sep 29 19:32:54 JST 2003
        // work around ...
        if (funSym.getParamList() == null) {
          badHIR("getParamList() returns null");
        } else {
          ListIterator params = funSym.getParamList().iterator();

          while (params.hasNext()) {
            Param param = (Param)params.next();
            if (param == null)
              break;
            ImList tmp; {
              Type parType = param.getSymType();
              if ((param instanceof SymNode)
                  && isReg(((SymNode) param).getSymNodeSym())) {
                tmp = list(REG, htype2ltype(param.getSymType()), uniqName(param));
              } else {
                tmp = memExp(htype2ltype(parType),
                             frameExp(hkind2ltype(Type.KIND_POINTER), uniqName(param)),
                             false, getAggregateAlign(parType), uniqName(param));
              }
            }
            prologue = prefix(tmp, prologue);
          }
        }
        addProfInfo(list(INFO, "BEGIN", PROLOGUE));
        prologue = prefix(PROLOGUE,
                          list("0", "0"),
                          prologue.destructiveReverse());
        addLexp(prologue);
        /* end of moving */

        addProfInfo(list(INFO, "END", PROLOGUE));
      }

      /* Mon May 26 18:02:28 JST 2003, delete old inititaion by ak*/
      // Main Program Initialization
      /* Tue May 13 11:09:31 JST 2003 by ak */
      // Generate Initialization
      /* Tue May 13 11:09:31 JST 2003 by ak */

      // Convert body
      returnUsed = false;
      convertNode(pSubp.getHirBody(), topLevelCc);

      // Generate Epilogue
      {
        ImList epilogue = ImList.Empty;
        addLexp(list(DEFLABEL, _EPILOGUE));

        if (retType.getTypeKind() != Type.KIND_VOID && returnUsed) {
          epilogue = list(memExp(htype2ltype(retType),
                                 frameExp(hkind2ltype(Type.KIND_POINTER), returnVarName), false, getAggregateAlign(retType), quote(returnVarName)));
        }
        epilogue = prefix(EPILOGUE,
                          list(ZERO, ZERO),
                          epilogue);
        addLexp(epilogue);
      }

      // Close local SYMTAB
      if (autoSym != ImList.Empty) {
        autoSym = reverse(autoSym);
        autoSym = prefix(SYMTAB, autoSym);
      }
      ImList body = reverse(Lseq);

      if (autoSym != ImList.Empty) {
        // output local symtab
        body = prefix(autoSym, body);
      }
      /*
        String funcName = funSym.getName();
        int funcnum = getSymNum(funSym);
        if (funcnum != SymStat.UNKNOWN) {
        funcName = funcName + "." + funcnum;
        }
        if (debugUniqFun) {
        System.out.println("close func " + funcName);
        }
      */
      needNotInit(funSym);
      return prefix(FUNCTION, uniqName(funSym), body);
    }

  // Convert statement/expression node
  private ImList convertNode(HIR pHir, ConvContext cc) {
    // System.out.println("in: " + pHir);
    ImList tmp = convertNode1(pHir, cc.nonLvalue());
    // System.out.println("out: " + tmp);
    return tmp;
  }

  private ImList convertLvalue(HIR pHir, ConvContext cc) {
    return convertNode1(pHir, cc.lvalue());
  }

  // convert address.
  private ImList convertAddress(HIR pHir, ConvContext cc) {
    ImList tmp = convertNode1(pHir, cc.lvalue());
    debug("lval: " + tmp);
    switch (pHir.getOperator()) {
    case HIR.OP_CALL:
      // (qual (call ...)) returns rval struct, need strip (MEM Axx ...)
      if (car(tmp) == MEM) {
        tmp = (ImList) car(cdr(cdr(tmp)));
        debug(" -> " + tmp);
      }
    }
    return tmp;
  }

  private ImList convertInfo(HIR pHir) {
      InfStmt inf = (InfStmt) pHir;
      String key = inf.getInfKind();
      if ((key == HIR_PROF) && (simulateOption == false)) { //##75
        return null; //##75
      }
      ListIterator it = null;
      try {
          IrList infs = inf.getInfList(key);
          it = infs.iterator();
      } catch (Exception e) {
          // no optional elements
      }

      ImList l = null;
      if (key == HIR_PROF) {
          if (it == null) {
              bug("bad prof");
              return null;
          }
          // prof takes one IrList.
          String val = (String) it.next();
          l = list(INFO, LIR_PROF, val); // always add this info.
          // change global context.
          if (val == PROF_ON) {
              generateProfileInfo = true;
              generateLine = true;
          } else if (val == PROF_OFF) {
              generateProfileInfo = false;
              generateLine = generateLineDefault;
          } else {
              l = list(INFO, LIR_PROF, val);
          }
          return l;
      }
      // other pragmas.
      l = list(key, INFO);
      // debug("key:" + key);
      while (it.hasNext()) {
          Object tmp = it.next();
          if (tmp instanceof Sym) {
              Sym sym = (Sym) tmp;
              if (sym.getSymKind() == Sym.KIND_LABEL) {
                  l = new ImList(sym.getName(), l);
              } else {
                  // debug("opt "+ uniqName(sym));
                  l = new ImList(uniqName(sym), l);
              }
          }
          else if (tmp instanceof String) {
              // debug("opt: string: "+ tmp);
              l = new ImList(tmp, l);
          }
      }
      l = l.destructiveReverse();
      return l;
  }

  private ImList convertNode1(HIR pHir, ConvContext cc) {
    ImList result = ImList.Empty;

    while (pHir != null) {
      Type type = pHir.getType();
      String ltype = htype2ltype(type);
      boolean isVolatile = type.isVolatile();

      switch (pHir.getOperator()) {
      case HIR.OP_INF: {
          if (true) {
            //##76  addLexp(convertInfo(pHir));
            //##76 BEGIN
            InfStmt inf = (InfStmt) pHir;
            String key = inf.getInfKind().intern();
            for (int i1 = 0; i1 < Registry.INF_KIND.length; i1++) {
              if (key == Registry.INF_KIND[i1]) {
                addLexp(convertInfo(pHir));
                break;
              }
            }
            //##76 END
          } else {
              IrList infs = pHir.getInfList();
              if (infs != null) {
                  ListIterator it = infs.iterator();
                  try {
                      String key = (String) it.next();
                      if (key == HIR_PROF) {
                          // prof takes one IrList.
                          Object tmp = it.next();
                          if (tmp instanceof IrList) {
                              ListIterator pit = ((IrList) tmp).iterator();
                              String val = (String) pit.next();
                              addLexp(list(INFO, LIR_PROF, val)); // always add this info.
                              if (val == PROF_ON) {
                                  generateProfileInfo = true;
                                  generateLine = true;
                              } else if (val == PROF_OFF) {
                                  generateProfileInfo = false;
                                  generateLine = generateLineDefault;
                              }
                          } else {
                              bug("bad profile pragma: " + tmp.toString());
                          }
                      } else {
                          // todo: pass through without processing. //##74
                          System.out.println("thru key = " + key);
                      }
                  } catch (ClassCastException e) {
                      // not pragma, user customized info ? ignore it.
                      // why ignore ? ...
                  }
              }
          }
        break;
      }
      // Expressions
      case HIR.OP_PARAM:
      case HIR.OP_VAR:
        {
          Sym sym = ((SymNode)pHir).getSymNodeSym();
          setUsed(sym);
          String store;

          if (cc.isLvalue()) {
            // Wed Mar  9 17:38:23 JST 2005
            if (isReg(sym)) {
              result = list(REG, htype2ltype(sym.getSymType()), uniqName(sym));
              return result;
            }

            if (((Var)sym).getStorageClass() == Var.VAR_STATIC)
              store = STATIC;
            else
              store = FRAME;
            result = list(store, hkind2ltype(Type.KIND_POINTER), uniqName(sym));
          } else {
            ImList tmp = convertLvalue(pHir, cc);
            if (isReg(sym)) {
              // Wed Mar  9 17:38:23 JST 2005
              // nothing to do here.
              // (we don't assume vector in register.)
              return tmp;
            }
            // Tue Oct 28 20:18:31 JST 2003
            if (vectorAssignLen > 0) {
              ltype = size2ltype(vectorAssignLen);
              vectorAssignLen = -1;
              // keep dstVectorLen value.
            }
            result = memExp(ltype, tmp, isVolatile, getAggregateAlign(type), optId(tmp));
          }
        }
        return result;

      case HIR.OP_SUBP:
        {
          Sym sym = ((SymNode)pHir).getSymNodeSym();
          setUsed(sym);
          result = staticExp(uniqName(sym));
        }
        return result;

      case HIR.OP_CONST:
        {
          Sym sym = ((ConstNode)pHir).getConstSym();
          // System.out.println("csym: " + sym);
          if (sym.getSymKind() == Type.KIND_STRING_CONST) {
            if (cc.isLvalue() || autoString) {
              int varnum = getSymNum(sym);
              if (varnum == SymStat.UNKNOWN) { // not installed.
                                // install to symtab
                installSpecial(sym);
                ImList tmp = staticEntString(sym);
                symtab = new ImList(tmp, symtab);
                debug("add: " + tmp + " = " + ((StringConst)sym).getStringBody());
                                // generate (DATA ...)
                if (dstVectorLen > 0) {
                  // System.out.println("assign vector: real len " + dstVectorLen);
                  // addLbody(strConst(uniqStrName(varnum, "string"), ((StringConst)sym).getStringBody(), dstVectorLen));
                  addLbody(strConst(stringName(sym), ((StringConst)sym).getStringBody()));
                  dstVectorLen = -1;
                  vectorAssignLen = -1;
                } else {
                  addLbody(strConst(stringName(sym), ((StringConst)sym).getStringBody()));
                }
                needNotInit(sym);

              }
              result = staticExp(stringName(sym));
              if (!cc.isLvalue()) {        // need block copy
                result = memExp(htype2ltype(pHir.getType()), result);
              } // else pointer assignment. (do nothing)
            } else {
              String s = ((ConstNode)pHir).getConstSym().getName();
              long len = 0;
              if (sym.getSymType() instanceof VectorType) {
                len = ((VectorType) sym.getSymType()).getElemCount(); // literal length, must be const.
                if (len <= 0) {
                  badHIR("literal length <= 0\n");
                }
              } else {
                                /* HIR bug */
                len = s.length() - 2;
              }
              result = charSeq(s.substring(1, s.length() -1) /* de-quote */, len);
            }
          } else if (type.isFloating()) {
            // Wed Jul 30 11:29:34 JST 2003 by ak
            // NB: float to double conversion might cause some problem ...
            // (But current coins front end treat float const. as double.)
            result = floatConst(ltype, ((ConstNode)pHir).getConstSym().doubleValue());
          } else {
            // Wed Jul 30 11:29:34 JST 2003 by ak
            result = intConst(ltype, ((ConstNode)pHir).getConstSym().longValue());
          }
        }
        return result;

      case HIR.OP_ADD:           return convertBinop(pHir, ADD, cc);
      case HIR.OP_SUB:           return convertBinop(pHir, SUB, cc);
      case HIR.OP_MULT:
        return convertBinop(pHir, MUL, cc);

      case HIR.OP_DIV:
        return convertBinop(pHir, type.isUnsigned() ? DIVU : DIVS, cc);
      case HIR.OP_MOD:
        return convertBinop(pHir, type.isUnsigned() ? MODU : MODS, cc);
      case HIR.OP_NEG:           return convertUnaop(pHir, NEG, cc);
      case HIR.OP_NOT:
        {  // Mon Jun 14 19:43:53 JST 2004
          ImList bnot = convertUnaop(pHir, BNOT, cc);
          Type ty = pHir.getType();
          if (ty.getTypeKind() != Type.KIND_BOOL) {
            return bnot;
          }
          ImList lnot = list(BAND, htype2ltype(ty), bnot, intConst(htype2ltype(ty), 1));
          return lnot;
        }
      case HIR.OP_AND:           return convertBinop(pHir, BAND, cc);
      case HIR.OP_OR:           return convertBinop(pHir, BOR, cc);
      case HIR.OP_XOR:           return convertBinop(pHir, BXOR, cc);
        //          case HIR.OP_SHIFT_L: convertBinop(pHir, "LSHS"); return;
      case HIR.OP_SHIFT_R: return convertBinop(pHir, RSHS, cc);
      case HIR.OP_SHIFT_LL: return convertBinop(pHir, LSHU, cc);
      case HIR.OP_SHIFT_RL: return convertBinop(pHir, RSHU, cc);

      case HIR.OP_CMP_EQ:     return convertCmp(pHir, TSTEQ, cc);
      case HIR.OP_CMP_NE:     return convertCmp(pHir, TSTNE, cc);
      case HIR.OP_CMP_GT:
        // Mon Jun  7 14:10:32 JST 2004
        // type of l == type of r (HIR spec.
        return convertCmp(pHir, ((HIR) pHir.getChild1()).getType().isUnsigned() ? TSTGTU : TSTGTS, cc);
      case HIR.OP_CMP_GE:
        return convertCmp(pHir, ((HIR) pHir.getChild1()).getType().isUnsigned() ? TSTGEU : TSTGES, cc);
      case HIR.OP_CMP_LT:
        return convertCmp(pHir, ((HIR) pHir.getChild1()).getType().isUnsigned() ? TSTLTU : TSTLTS, cc);
      case HIR.OP_CMP_LE:
        return convertCmp(pHir, ((HIR) pHir.getChild1()).getType().isUnsigned() ? TSTLEU : TSTLES, cc);

      case HIR.OP_CONV:
        {
          Type srcType = ((HIR)pHir.getChild1()).getType();
          /* Thu Jul 31 18:38:24 JST 2003 by ak */
          if (srcType.isFloating()) {
            if (type.isFloating()) {
              // float to float
              if (srcType.getSizeValue() > type.getSizeValue())
                result = convertUnaop(pHir, CONVFT, cc);
              else
                result = convertUnaop(pHir, CONVFX, cc);
            } else {
              // float to int
              if (false) {
                result = convertUnaop(pHir, CONVFI, cc);
              }
              if (type.isUnsigned())
                result = convertUnaop(pHir, CONVFU, cc);
              else
                result = convertUnaop(pHir, CONVFS, cc);
            }
          } else {
            if (type.isFloating()) {
              // int to float
              if (srcType.isUnsigned())
                result = convertUnaop(pHir, CONVUF, cc);
              else
                result = convertUnaop(pHir, CONVSF, cc);
            } else {
              // int to int
              long d = type.getSizeValue() - srcType.getSizeValue();
              if (d > 0) {
                                // extend
                if (srcType.isUnsigned())
                  result = convertUnaop(pHir, CONVZX, cc);
                else {
                  //##81 BEGIN
                  if ((pHir.getParent().getOperator() == HIR.OP_SUBS)&&
                      (pHir.getParent().getChild2() == pHir)) {
                    // pHir is a subscript expression.
                    HIR lBrother = (HIR)pHir.getParent().getChild1();
                    VectorType lVectorType =
                      (VectorType)lBrother.getType();
                    if ((fHirRoot.ioRoot.getMachineName().intern() == "x86_64")&&
                        //### (fHirRoot.ioRoot.getMachineName().intern() == "alpha")&& //### only for testing.
                        (lVectorType.getLowerBound() >= 0)) {
                      Var lVar = null;
                      if (lBrother instanceof VarNode)
                        lVar = (Var)((VarNode)lBrother).getSymNodeSym();
                      else if ((lBrother.getOperator() == HIR.OP_UNDECAY)&&
                               (lBrother.getChild1() instanceof VarNode))
                        lVar = (Var)((VarNode)lBrother.getChild1()).getSymNodeSym();
                      else if ((lBrother.getOperator() == HIR.OP_SUBS)&&
                               (lBrother.getChild1() instanceof VarNode))
                        lVar = (Var)((VarNode)lBrother.getChild1()).getSymNodeSym();
                      if ((lVar != null)&&
                          (hirOptMap.keySet().contains("safeArrayAll")||
                           fHirRoot.symRoot.subpCurrent.isSafeArrayAll()||
                           fHirRoot.symRoot.safeArray.contains(lVar))) {
                         result = convertUnaop(pHir, CONVZX, cc);
                         debug("\n " + pHir.toStringShort() +
                             " is non-negative subscript. Use CONVZX.");
                      }else {
                        result = convertUnaop(pHir, CONVSX, cc);
                      }
                    }else {
                      result = convertUnaop(pHir, CONVSX, cc);
                    }
                  }else
                  //##81 END
                  result = convertUnaop(pHir, CONVSX, cc);
                }
              } else if (d < 0)
                result = convertUnaop(pHir, CONVIT, cc);
              else
                result = convertNode((HIR)pHir.getChild1(), cc); // do nothing
            }
          }
        }
        return result;

      case HIR.OP_CONTENTS:
        if (builtin_align != 0) {
          badHIR("nested __builtin_align() is not supported. (ignored)");
        }
        builtin_align = 0;
        if (cc.isLvalue())
          result = convertNode((HIR)pHir.getChild1(), cc);
        else {
          ImList tmp = convertNode((HIR)pHir.getChild1(), cc);
          if (builtin_align != 0) {
            result = memExp(ltype, tmp, isVolatile, builtin_align, optId(tmp));
            builtin_align = 0;
          } else {
            result = memExp(ltype, tmp, isVolatile, getAggregateAlign(type), optId(tmp));
          }
        }
        return result;
      case HIR.OP_SUBS:
        {
          // System.out.println("Subs useIndex: " + useIndex);
          // System.out.println("l: "+ pHir.getChild1());
          ImList l = convertLvalue((HIR)pHir.getChild1(), cc);
          ImList r = convertNode((HIR)pHir.getChild2(), cc);
          ImList baseExp = null;

          /* Mon Oct  6 14:42:55 JST 2003 */
          {
            Type v = null;
            long scale = 1;

            if (pHir.getChild1() instanceof Exp) {
              /* (arr[])[] */
              v = ((Exp) pHir.getChild1()).getType();

              /* Tue Oct 28 20:00:35 JST 2003 */
              if (v instanceof VectorType) {
                                // Fri Jul  9 11:43:44 JST 2004
                VectorType vt = (VectorType) v;
                HIR lowerBound = vt.getLowerBoundExp();
                if (lowerBound != null) {
                  if (lowerBound instanceof ConstNode
                      && ((ConstNode) lowerBound).getIntValue() == 0) {
                    // lower base is 0.
                    baseExp = null;
                  } else {
                    baseExp = convertNode(lowerBound, cc);
                  }
                } else {
                  badHIR("getLowerBoundExp() returns null, treat as 0. " + vt);
                  baseExp = null;
                }
                                // out: baseExp
                debug("lower exp " + baseExp);
              } else {
                                // todo ?
                badHIR("operand of op_sub node must be vector. " + v);
              }
            } else if (pHir.getChild1() instanceof VarNode) {
              /* dead code ? */
              v = ((Sym) ((VarNode) pHir.getChild1()).getSymNodeSym()).getSymType();
            } else if (pHir.getChild1() instanceof ConstNode) {
              /* "str"[1] */
              v = ((ConstNode) pHir.getChild1()).getConstSym().getSymType();
            } else {
              System.out.println("front bug ? " + pHir.getChild1());
            }

            // shift base
            // Wed Jan 11 11:30:06 JST 2006
            // use sizeof POINTER instead of I32
            if (baseExp != null) {
              r = list(SUB, hkind2ltype(Type.KIND_POINTER), r, baseExp);
            }

            // get scale factor
            if (v instanceof VectorType) {
              scale = ((VectorType) v).getElemType().getSizeValue();
              // debug("  scale = " + scale);
            } else {
              badHIR("front bug ?");
            }

            // emit scaling, if necessary.
            // Wed Jan 11 11:21:08 JST 2006
            // use sizeof POINTER instead of I32
            if (scale != 1) {
              if (scale == 0) { // front bug, treat as 1
                badHIR("getSizeValue() returns 0 in op_subs's operand. (cannot calculate correct address) " + v);
              } else if (scale > 0) {
                r = list(MUL, hkind2ltype(Type.KIND_POINTER), r, intConst(hkind2ltype(Type.KIND_POINTER), scale));
              } else { // scale < 0
                ImList scaleExp = convertNode(((VectorType) v).getElemType().getSizeExp(), cc);
                debug("scale < 0, sizeExp: " +        scaleExp);
                r = list(MUL, hkind2ltype(Type.KIND_POINTER), r, scaleExp);
              }
            }
          }
          // System.out.println("l: "+ l);
          // System.out.println("r: "+ r);
          result = address(l, r);

          /* Tue Oct 28 20:42:03 JST 2003 (aggregate copy) */
          if (vectorAssignLen > 0) {
            ltype = size2ltype(vectorAssignLen);
            vectorAssignLen = -1;
            dstVectorLen = -1;
          }

          result = cc.isLvalue() ? result
            : memExp(ltype, result, isVolatile, getAggregateAlign(type), complexMem);
          return result;
        }
      case HIR.OP_ADDR:
      case HIR.OP_DECAY:
        return convertLvalue((HIR)pHir.getChild1(), cc);

      case HIR.OP_UNDECAY:        // UNDECAY appears CoinsCC030716.
        if (!cc.isLvalue()) {
          return null;
        }
        return convertNode((HIR)pHir.getChild1(), cc);

      case HIR.OP_QUAL:
        return
          convertLoadMember
          (convertAddress(((QualifiedExp)pHir).getQualifierExp(), cc),
           ((QualifiedExp)pHir).getQualifiedElem(),
           type, cc);

      case HIR.OP_ARROW:
        return
          convertLoadMember
          (convertNode(((PointedExp)pHir).getPointerExp(), cc),
           ((PointedExp)pHir).getPointedElem(),
           type, cc);

      case HIR.OP_CALL:
        {
          boolean needFuncval = true;
          String functionVarName = null; //Thu Jun 24 14:18:53 JST 2004

          if (type.getTypeKind() != Type.KIND_VOID) {
            if (pHir.getParent().getOperator() == HIR.OP_ASSIGN) {
              debug("parent is assign, delete tmp-copy");
              needFuncval = false;
            } else {
              // generate temporal.
              functionVarName = uniqStrName(tempCtr++, "functionvalue");
              debug("parent is not assign, generate " + functionVarName);

              // assert ltype != Sym.KIND_SUBP;
              autoSym = prefix(frameEnt(functionVarName,
                                        ltype,
                                        String.valueOf(type.getAlignment()),
                                        ZERO),
                               autoSym);
            }
          }
          FunctionExp exp = (FunctionExp)pHir;
          ListIterator params = exp.getParamList().iterator();
          ImList callee = convertNode(exp.getFunctionSpec(), cc);

          // Mon Mar 14 13:11:43 JST 2005
          // process __builtin_align()
          if (car(callee) == STATIC && BUILTIN_ALIGN.equals(car(cdr(cdr(callee))))) {
            return convert_builtin_align(params, cc);
          }

          ImList paramList = ImList.Empty;
          while (params.hasNext()) {
            paramList = new ImList(convertNode((HIR)params.next(), cc), paramList);
          }
          paramList = reverse(paramList);
          //System.out.println("param: " + paramList);
          ImList out = null;
          if (type.getTypeKind() != Type.KIND_VOID) {
            // Thu Jul 17 19:03:38 JST 2003 by ak
            if (needFuncval) {
              result = memExp(ltype, frameExp(hkind2ltype(Type.KIND_POINTER), functionVarName), false, -1, quote(functionVarName));
            } else {
              result = convertNode((HIR) pHir.getParent().getChild1(), cc);
            }
            out = list(result);
          } else {
            out = result = ImList.Empty;
          }
          // add &id
          ImList opt = ImList.Empty;
          //##75 if (true /*generateProfileInfo*/)
          if (simulateOption || generateLine) { //##75
            Object target = car(callee);
            if (target == STATIC) {
              Object id = car(cdr(cdr(callee)));
              if (id instanceof QuotedString) {
                opt = list(ID_M, list(id, String.valueOf(memNum++)));
              } else {
                bug("bad STATIC: " + callee);
                opt = list(ID_M, String.valueOf(memNum++));
              }
            } else {                // complex callee.
              opt = list(ID_M, String.valueOf(memNum++));
            }
          }

          // System.out.println("result: " + out);
          addProfInfo(list(INFO, "BEGIN", CALL));
          addLexp(prefix(CALL, callee, paramList, out, opt));
          addProfInfo(list(INFO, "END", CALL));
          return needFuncval ? result : null;
        }

        // Statements
      case HIR.OP_ASSIGN:
        lineInfo(pHir);
        autoString = true;

        // Tue Oct 28 20:08:47 JST 2003
        {
          VectorType v = null;
          if (pHir.getType() instanceof VectorType) {
            v = (VectorType) pHir.getType();
            vectorAssignLen = v.getElemCount();// fixme
            // System.out.println("assign: " + pHir);
            // System.out.println("len " + vectorAssignLen);
            // Tue Mar 16 19:06:05 JST 2004
            VectorType dstType =  (VectorType) ((HIR)pHir.getChild1()).getType(); // lhs must be vector
            dstVectorLen = dstType.getElemCount(); // fixme

            if (vectorAssignLen <= 0 || dstVectorLen <= 0) {
              System.out.println("op_assign: length <= 0 (not supported)\n");
            }
          }
        }
        {
          // Fri Jun  4 19:13:54 JST 2004
          ImList r = convertNode((HIR)pHir.getChild2(), cc);
          if (r == null) {
            // already generated "l = func()" instead of "r = func(); l = r"
            // do nothing here.
          } else {
            // generate l = r
            ImList tmp;
            HIR lhs = (HIR)pHir.getChild1();
            switch (lhs.getOperator()) {
            case HIR.OP_QUAL:
              tmp = convertStoreMember
                (convertAddress(((QualifiedExp)lhs).getQualifierExp(), cc),
                 ((QualifiedExp)lhs).getQualifiedElem(), type, r, cc);
              break;
            case HIR.OP_ARROW:
              tmp = convertStoreMember
                (convertNode(((PointedExp)lhs).getPointerExp(), cc),
                 ((PointedExp)lhs).getPointedElem(), type, r, cc);
              break;
            default:
              ImList l = convertNode((HIR)pHir.getChild1(), cc);
              tmp = list(SET, htype2ltype(pHir.getType()), l, r);
              break;
            }
            addLexp(tmp);
          }
        }
        break;

      case HIR.OP_BLOCK:
        lineInfo(pHir);
        lineInfo(((BlockStmt)pHir).getFirstStmt());
        regionLoc = 0;
        addLexp(convertNode(((BlockStmt)pHir).getFirstStmt(), cc));
        regionLoc = 0;
        break;

      case HIR.OP_RETURN:
        {
          lineInfo(pHir);
          if (returnVarName != null) {
            // Tue Apr 20 11:57:41 JST 2004
            // next comment may be obsoleted ...
            // Wed Jul 23 18:15:48 JST 2003 by ak
            // NB: current CoinsCC forbid return without value for non
            // void function except int-function.
            if (ltype.equals(UNKNOWN)) {// remove me ?
              ltype = hkind2ltype(Type.KIND_INT); // changed from "I32"
            }
            // Tue Jun        8 11:42:07 JST 2004
            if (!type.equals(retType)) {
              badHIR("type mismatch in " + pHir + " (" + type + " != " + retType + ", -> treat as " + retType + ")");
              ltype = htype2ltype(retType);
            }

            ImList tmp = convertNode(((ReturnStmt)pHir).getReturnValue(), cc);

            if (tmp != null) {
              addLexp(list(SET,
                           ltype,
                           memExp(ltype, frameExp(hkind2ltype(Type.KIND_POINTER), returnVarName),
                                  false, getAggregateAlign(type), quote(returnVarName)),
                           tmp)
                      );
              returnUsed = true;
            }
          }
          addLexpBeforeInfo(
                  list(JUMP,
                       list(LABEL, hkind2ltype(Type.KIND_POINTER), _EPILOGUE))
                  );
        }
        break;

      case HIR.OP_IF:
        {
          lineInfo(pHir);
          if (false /*supportLabeledNull*/) {
            // add dummy return to labeledNull for test
            Stmt ins = fHirRoot.hir.returnStmt(fHirRoot.hir.intConstNode(123));
            LabeledStmt tmp =(LabeledStmt) pHir.getSourceNode(4);
            tmp.combineStmt(ins, false);
          }

          IfStmt stm = (IfStmt)pHir;
          //##76 Label thenlabel = stm.getThenPart().getLabel();
          //##76 Label elselabel = stm.getElsePart().getLabel();
          Label endlabel = stm.getEndLabel();
          //##76 BEGIN
          Label thenlabel;
          if (stm.getThenPart() == null)
            thenlabel = endlabel;
          else
            thenlabel = stm.getThenPart().getLabel();
          Label elselabel;
          if (stm.getElsePart() == null)
            elselabel = endlabel;
          else
            elselabel = stm.getElsePart().getLabel();
          //##76 END
          //##64N ImList cond = convertNode(stm.getIfCondition(), cc.noIfExp());
          //##64N addLexp(jumpc(cond, thenlabel, elselabel));
          jumpc2(stm.getIfCondition(), thenlabel, elselabel, cc.noIfExp()); //##64N
          if (stm.getThenPart() != null) { //##76
            addLexp(convertNode(stm.getThenPart(), cc /*.emitIfExp()*/));
            addLexpBeforeInfo(jump(endlabel));
          } //##76
          if (stm.getElsePart() != null) { //##76
            addLexp(convertNode(stm.getElsePart(), cc /*.emitIfExp()*/));
          } //##76
          // "End label" part.
          if (!supportLabeledNull) {
            addLexpAfterInfo(convertDeflabel(endlabel));
          } else {
            addLexp(convertNode((Stmt)stm.getChild(4), cc));
          }
          break;
        }
        /**/
      case HIR.OP_WHILE:
      case HIR.OP_INDEXED_LOOP:
      case HIR.OP_FOR:
      case HIR.OP_REPEAT:
        {
          lineInfo(pHir);
          LoopStmt stm = (LoopStmt)pHir;
          Label looplabel = stm.getLoopBackLabel();
          Label endlabel = stm.getLoopEndLabel();
          ImList tmp = null;

          if (stm.getLoopInitPart() != null) {
            addLexp(convertNode(stm.getLoopInitPart(), cc /*.emitIfExp()*/));
          }

          // NB: getLoopStartCondition() returns conditional exp only.
          addLexpAfterInfo(convertDeflabel(looplabel));

          // start condition (optional ?)
          if (stm.getLoopStartCondition() != null) {
            //##64N tmp = convertNode(stm.getLoopStartCondition(), cc.noIfExp());
            //##64N addLexp(jumpc(tmp, stm.getLoopBodyLabel(), endlabel));
            jumpc2(stm.getLoopStartCondition(), stm.getLoopBodyLabel(), //##64N
                   endlabel, cc.noIfExp()); //##64N
          }

          // loop body (required)
          addLexp(convertNode(stm.getLoopBodyPart(), cc /*.emitIfExp()*/));

          // end condition (optional)
          if (stm.getLoopEndCondition() != null) {
            //##64N tmp = convertNode(stm.getLoopEndCondition(), cc.noIfExp());
            //##64N int lab = labelCtr++;
            //##64N addLexp(jumpc(tmp, lab, endlabel));
            int  lab = labelCtr++; //##64N
            Label newLabel = fHirRoot.sym.defineLabel(prefLirLabel + lab); //##64N
            jumpc2(stm.getLoopEndCondition(), newLabel, endlabel, cc.noIfExp()); //##64N

            addLexp(convertDefLirLabel(lab));
          }

          // step part (optional)
          if (stm.getLoopStepPart() != null) {
            addLexp(convertNode(stm.getLoopStepPart(), cc /*.emitIfExp()*/));
          }
          addLexpBeforeInfo(jump(looplabel));

          // loop end part
          addLexp(convertNode(stm.getLoopEndPart(), cc /*.emitIfExp() */));
        }
        break;

      case HIR.OP_LABELED_STMT:
        {
          LabeledStmt stm = (LabeledStmt)pHir;
          ListIterator it = stm.getLabelDefList().iterator();
          while (it.hasNext()) {
            if (!supportLabeledNull) {
              addLexpAfterInfo(convertDeflabel(((coins.ir.hir.LabelDef)it.next()).getLabel()));
            } else {
              Label lLabel = ((coins.ir.hir.LabelDef)it.next()).getLabel();
              if (!appearedLabels.contains(lLabel)) {
                addLexpAfterInfo(convertDeflabel(lLabel));
              }
            }
          }
          addLexp(convertNode(stm.getStmt(), cc));
        }
        break;

      case HIR.OP_EXP_STMT:
        {
          lineInfo(pHir);
          Exp exp = ((ExpStmt)pHir).getExp();
          if (exp != null) {
            ImList tmp = convertNode(exp, cc);
            // value of expStmt must be discard, possibly ...
            // addLexp(tmp);
          }
        }
        break;

      case HIR.OP_JUMP:
        lineInfo(pHir);
        addLexpBeforeInfo(jump(((JumpStmt)pHir).getLabel()));
        break;

      case HIR.OP_SWITCH:
        {
          lineInfo(pHir);
          if (false /*supportLabeledNull*/) {
            // add dummy return to labeledNull
            Stmt ins = fHirRoot.hir.returnStmt(fHirRoot.hir.intConstNode(456));
            LabeledStmt tmp =(LabeledStmt) pHir.getSourceNode(4);
            tmp.combineStmt(ins, false);
          }

          SwitchStmt stm = (SwitchStmt)pHir;
          ImList selExp = convertNode(stm.getSelectionExp(), cc);

          ltype = htype2ltype(stm.getSelectionExp().getType());

          int n = stm.getCaseCount();
          ArrayList caseLab = new ArrayList();
          for (int i = 0; i < n; i++) {
            caseLab.add(list(intConst(ltype, stm.getCaseConst(i).getName()),
                             labelOperand(stm.getCaseLabel(i))));
          }
          Label defaultlabel = stm.getDefaultLabel();

          ImList tmp = ImList.Empty;
          for (int i = n - 1; i >= 0; i--) {
            tmp = prefix(caseLab.get(i), tmp);
          }
          tmp = list(tmp, labelOperand(defaultlabel));
          addLexpBeforeInfo(tmp = prefix(JUMPN, selExp, tmp));
          //System.out.println("labs:" + tmp);
          addLexp(convertNode(stm.getBodyStmt(), cc));
          if (!appearedLabels.contains(defaultlabel)) {
            addLexpAfterInfo(convertDeflabel(defaultlabel));
          }
          // "Switch end" part.
          if (!supportLabeledNull) {
            addLexpAfterInfo(convertDeflabel(stm.getEndLabel()));
          } else {
            addLexp(convertNode((LabeledStmt)stm.getChild(4), cc));
          }
        }
        break;

      case HIR.OP_SETDATA:        // Fri May  9 20:36:34 JST 2003
        {
          DZSSeq = ImList.Empty;
          lineInfo(pHir);
          autoString = false;
          stroff = new StructOffset();

          // special treatment for fortran block common ...
          if (pHir.getChild1().getOperator() == HIR.OP_QUAL) {
            long bcePadding = 0;
            Exp qual = ((QualifiedExp)pHir.getChild1()).getQualifierExp();
            Sym qsym = ((SymNode)qual).getSymNodeSym();
            Sym bceSym = ((SymNode) ((HIR)pHir.getChild1()).getChild2()).getSymNodeSym();
            setUsed(qsym);
            needNotInit(qsym);
            setUsed(bceSym);
            needNotInit(bceSym);

            long off = ((QualifiedExp)pHir.getChild1()).getQualifiedElem().evaluateDisp();
            System.out.println("qualExp " + qual.toStringDetail());
            System.out.println("qsym " + qsym.toStringDetail());
            if (regionLoc < off) {
              // fixme: generate dummy label and emit padding here ?
              bcePadding = off - regionLoc;
              regionLoc = off;
            } else if (regionLoc == off) {
              /* do nothing */
            } else {
              badHIR("region offset mismatch: cur " + regionLoc + " next " + off);
            }
            ImList bcElem = null;
            HIR r = (HIR) pHir.getChild2();
            if (r instanceof ExpListExp) {
              numSeq = new NumSeq();
              convertNode1(r, cc.nonLvalue());
              bcElem = reverse(DZSSeq);
            }
            else if (r instanceof ConstNode) {
              bcElem = list(list(htype2ltype(r.getType()), convertNode1(r, cc.nonLvalue())));
            }
            else {
              badHIR("region init (expect ExpListExp)");
            }
            if (bcElem != null) {
              addLbody(prefix(DATA,
                              uniqName(bceSym),
                              bcElem));
            }
            break;
          } // end of fortran specific code ...

          {                        // Mon Mar 22 17:49:40 JST 2004
            Sym sym = ((SymNode)pHir.getChild1()).getSymNodeSym();
            setUsed(sym);
            needNotInit(sym);
          }
          /* prepare lhs */
          Type vtype = null;
          if (pHir.getChild1() instanceof VarNode) {
            vtype = ((VarNode) pHir.getChild1()).getVar().getSymType();
          } /* todo: else { bug ?} */

          debug("setdata: *** type " + vtype);

          HIR r = (HIR) pHir.getChild2();
          ImList data;
          if (isScalerType(vtype)) {
            Object tmp;
            if (r instanceof ConstNode) { // spcecial rule for num
              // Wed Sep 24 22:02:50 JST 2003
              // tmp = ((ConstNode) r).getConstSym().toString();
              if (vtype.isFloating())
                tmp = Double.toString(((ConstNode) r).getConstSym().doubleValue());
              else
                tmp = Long.toString(((ConstNode) r).getConstSym().longValue());
            } else {
              tmp = convertNode1(r, cc.nonLvalue());
            }
            // Tue Feb  1 11:41:03 JST 2005
            if (tmp == null) {
              badHIR("non constant exp. in initializer: " + r);
            }
            // Fri Aug        8 16:36:27 JST 2003
            data = list(list(htype2ltype(vtype), tmp));
            debug("setdata: *** data " + data);
          }
          else if (vtype instanceof VectorType) {
            VectorType tmp = (VectorType) vtype;
            vtype = getVectorElemType(vtype);
            debug("*** r: " + r);
            if (r instanceof ExpListExp) {
              numSeq = new NumSeq();
              convertNode1((ExpListExp) r, cc.nonLvalue()); // emit initializer
              data = reverse(DZSSeq); // ExpList returns no ImList.
              debug("setdata: *** data " + data);
            } else {                // char s[] = "string";
              long len = tmp.getElemCount(); // must be const.
              long slen = ((VectorType) (((ConstNode) r).getConstSym()).getSymType()).getElemCount(); // must be const.

              if (len <= 0 || slen <= 0) {
                badHIR("vector length <= 0\n");
              }
              // Fri Sep 26 20:57:58 JST 2003
              // add zeros, in case strlen < sizeof arr.
              // System.out.println("vlen " + len + ", slen " + slen);
              if (len - slen > 0) {
                data = list(convertNode1(r, cc.nonLvalue()), zeros(len - slen));
              } else {
                data = list(convertNode1(r, cc.nonLvalue()));
              }
            }
            debug("setdata: *** data " + data);
          }
          else if (isStrunion(vtype)) {
            // fixme: head alignment ?
            // System.out.println("su align: " + vtype.getAlignment());
            if (r instanceof ExpListExp) {
              numSeq = new NumSeq();
              convertNode1((ExpListExp) r, cc.nonLvalue());
              data = reverse(DZSSeq);
              // System.out.println("struct-init-top: " + data);
            } else {
              data = list("bug? strunion init (expect ExpListExp)");
            }
          }
          else {
            // others ... bug ?
            data = list("bug? setdata");
            convertNode1(r, cc.nonLvalue());
          }
          lineInfo(((SymNode) pHir.getChild1()).getSymNodeSym());
          addLbody(prefix(DATA,
                          uniqName(((SymNode) pHir.getChild1()).getSymNodeSym()),
                          data));
        }
        // common tail
        break;

      case HIR.OP_EXPLIST:
        {
          /* Thu May 15 16:26:46 JST 2003 */
          debug("explist:" + pHir);
          ExpListExp l = (ExpListExp) pHir;
          Type ty = l.getType();
          debug("type:" + ty);
          /* Wed Aug  6 20:07:38 JST 2003 */
          stroff.align(ty.getAlignment()); // to make sure
          if (ty instanceof VectorType) {
            Type old = numSeq.replType(getVectorElemType(ty)); // push numSeqType
            long len = l.length();
            long vlen = ((VectorType) ty).getElemCount(); // fixme
            if (vlen <= 0) {
              badHIR("length of op_explist <= 0\n");
            }

            long total = stroff.total + numSeq.getElemSize() * vlen;
            // System.out.println("ilen: " + len + " vlen :" + vlen);
            vlen = (len < vlen) ? len : vlen;
            // ??
            // ??
            ListIterator iter = l.iterator();
            for (int i = 0; i < vlen; i++) {
              //HIR tmp = l.getExp(i);
              HIR tmp = (HIR)iter.next();
              Sym c = null;

              if (tmp.getOperator() == HIR.OP_CONST
                  && (c = ((ConstNode) tmp).getConstSym()).getSymKind() == Type.KIND_STRING_CONST) {
                                /* Fri Sep 26 19:58:35 JST 2003 */
                                /*  avoid bad LIR (I8 (I8 ...)),
                                    in case l.get(i) is string literal */
                String s = c.getName();
                                // System.out.println("l(i) is "+ tmp);
                int j;
                for (j = 1; j < s.length() - 1; j++) { // de-quote
                  numSeq.add(intConst("I8", (int) s.charAt(j)));
                }
                --j;
                                // NB: this boring cast is safe.
                len = ((VectorType) ((VectorType) ty).getElemType()).getElemCount(); // fixme
                                // NB: c.getSymType()).getElemCount() is not array size but string length.
                if (len <= 0) {
                  badHIR("length of op_explist <= 0\n");
                }
                if (j < len) {
                  numSeq.addZeros(len - j);
                }
              } else {
                numSeq.add(convertNode1(tmp, cc.nonLvalue()));
              }
            }
            stroff.set(total);
            numSeq.toDZSseq();
            numSeq.replType(old); // pop numSeqType
          } else if (isStrunion(ty)) {
            IrList elem = ty.getElemList();
            Type old = numSeq.replType((Type) null); // push numSeqType
            long top = stroff.total;

            debug("-- struct head align " + ty.getAlignment() + "@" + top);

            int len = (ty instanceof UnionType) ? 1 : elem.size();

            for (int i = 0; i < len; i++) {
              long ofs = ((Elem) elem.get(i)).evaluateDisp() + top;
              debug("  disp = " + ((Elem) elem.get(i)).evaluateDisp());
              debug("[" + elem.get(i) + "] top: " + top + " off: " + ofs);

              stroff.flushBitField(ofs);
              ImList tmp = convertNode1(l.getExp(i), cc.nonLvalue());
              long total = ofs + ((Sym) elem.get(i)).getSymType().getSizeValue();
              stroff.padding(ofs);

              // pack bit field const.
              if (((Elem) elem.get(i)).isBitField()) {
                if (l.getExp(i).isEvaluable()) {
                  // NB: Exp.evalute() retain top level conv node
                  // long val = evalConst(l.getExp(i));
                  long val = l.getExp(i).evaluateAsLong();

                  Elem bitf = (Elem) elem.get(i);
                  int bsz = bitf.getBitSize();
                  int bitoff = bitf.getBitOffset();
                  if (machineParam.isBigEndian()) {
                    bitoff = machineParam.evaluateSize(Type.KIND_INT) * 8 - bitoff - bsz;
                  }
                  debug("  packing ofs = " + ofs + ", bit field pos = " + bitoff + " wid = " + bsz + " <- " + val);
                  val &= ((1L << bsz) - 1);
                  val <<= bitoff;
                  stroff.pack(val, bsz, ((1L << bsz) - 1) << bitoff);
                } else {
                  // todo: address ?
                  badHIR("non constant exp. in bit-field initializer");
                }
              } else {
                if (tmp != null) {
                  // sizeof(dst) > sizeof(src)
                  if (isLiteral((HIR) l.getExp(i))) {
                    long sz = ((Sym) elem.get(i)).getSymType().getSizeValue();
                    Sym sym = ((ConstNode) l.getExp(i)).getConstSym();
                    int gen = ((StringConst)sym).getStringBody().length();
                    // System.out.println(" size " + sz);
                    // System.out.println(" len " + gen);
                    if (gen < sz) {
                      DZSSeq = new ImList(tmp, DZSSeq);
                      tmp = zeros(sz - gen - 1);
                    } else if (gen > sz) {
                      bug("literal init.");
                    }
                    DZSSeq = new ImList(tmp, DZSSeq);
                  } else {
                    DZSSeq = new ImList(list(htype2ltype(((Sym) elem.get(i)).getSymType()), tmp), DZSSeq);
                  }
                  stroff.set(total);
                }
                                //System.out.println("out " + total + " in " + stroff.total);
              }
            }
            // emit tail padding
            long su_size = ty.getSizeValue();
            //if (su_size != (stroff.total - top)) {
            // System.out.println("tail padding: " + (su_size - (stroff.total - top)));
            //}
            stroff.flushBitField(su_size + top);
            stroff.padding(su_size + top);
            numSeq.toDZSseq();
            numSeq.replType(old); // pop numSeqType
          }
          // for block common
          else {
            IrList elem = ty.getElemList(); // ??? not used ???
            int len = l.length();
            System.out.println("*** void len " +  len);
            numSeq = new NumSeq();
            for (int i = 0; i < len; i++) {
              numSeq.replType(l.getExp(i).getType());
              System.out.println("*** elem " + convertNode(l.getExp(i), cc));
            }
          }
        }
        break;
      case HIR.OP_EXPREPEAT:
        /* Mon May 19 14:15:15 JST 2003 */
        // CAUTION: We assume floating point zero is represented by
        // integer 0. (IEEE-754 ensures this.)
        {
          HIR exp = (HIR) pHir.getChild1();
          int n = ((ConstNode) pHir.getChild2()).getConstSym().intValue();
          long size = n * numSeq.getElemSize();
          // DO NOT USE exp.getType().getSizeValue() here.
          if (isConstZero(exp)) {
            numSeq.toDZSseq(); // flush first.
            DZSSeq = new ImList(zeros(size), DZSSeq);
          } else {
            for (int i = 0; i < n; i++) {
              numSeq.add(convertNode1(exp, cc.nonLvalue()));
            }
            numSeq.toDZSseq();
          }
          stroff.add(size);
        }
        break;
      case HIR.OP_ASM:                //
          {
              HIR tmp = (HIR) pHir.getChild1();
              ArrayList args = new ArrayList();

              // convert parameters.
              // (This processing may be better to be done in some other place.)
              {
                ListIterator params = ((AsmStmt) pHir).getActualParamList().iterator();
                while (params.hasNext()) {
                  args.add(convertNode((HIR)params.next(), cc));
                }
              }

              String body = "bug";
              if (tmp instanceof ConstNode) {
                // String tmp2 = ((StringConst) ((ConstNode) tmp).getConstSym()).makeCstring();
                // body = tmp2.substring(1, tmp2.length() - 1);
                body = ((StringConst) ((ConstNode) tmp).getConstSym()).getStringBody();

              }
              debug("body = " + body);

              // #param
              AsmParamList asm_params = null;
              if (body.startsWith(ASM_PARAM)) {
                body = body.substring(ASM_PARAM.length());
                // int end = body.indexOf("\\n");
                int end = body.indexOf("\n");

                asm_params = doAsmParam(body.substring(0, end), false);
                //body = body.substring(end + 2 /* \\n */);
                body = body.substring(end + 1 /* \n */);
              }
              // #clobber
              AsmParamList asm_clobbers = null;
              if (body.startsWith(ASM_CLOBBER)) {
                  body = body.substring(ASM_CLOBBER.length());
                  // int end = body.indexOf("\\n");
                  int end = body.indexOf("\n");
                  asm_clobbers = doAsmParam(body.substring(0, end), true);
                  // body = body.substring(end + 2 /* \\n */);
                  body = body.substring(end + 1 /* \n */);
              }

              // construct LIR

              ImList inp, outp, inoutp, argp, clobp;
              inp = outp = inoutp = argp = clobp = ImList.Empty;

              ImList asm = ImList.Empty;

              // optional clobber list
              if (asm_clobbers != null) {
                for (int i = 0; i < asm_clobbers.size(); i++) {
                  clobp = new ImList(asm_clobbers.get(i).getType(), clobp);
                }
                asm = list(CLOB_M, clobp);
              }

              // optional param list
              if (asm_params != null) {
                  AsmParam p = null;
                  AsmParamList ins, outs, inouts;

                  ins = new AsmParamList();
                  outs = new AsmParamList();
                  inouts = new AsmParamList();

                  // classify args by modifier.
                  for (int i = 0; i < asm_params.size(); i++) {
                      if (i >= args.size()) {
                        // too many args specified in #param, ignore it.
                        // todo: warning
                        break;
                      }

                      p = asm_params.get(i);
                      ImList q = (ImList) args.get(i);

                      // check a, s consistency.
                      {
                        String ty = p.getType();
                        // "a" and "s" require frame or static exp.
                        // if mem exp is passed, strip it.
                        // todo: message
                        if (ty == "a") {
                          if (car(q) == MEM) {
                            // System.err.println("need &");
                            q = (ImList) car(cdr(cdr(q)));
                          }
                        } else if (ty == "s") {
                          if (car(q) == MEM) {
                            // System.err.println("need &");
                            q = (ImList) car(cdr(cdr(q)));
                          }
                        }
                      }

                      String io = p.getIo();
                      if (io == null) {
                          p.addTo(ins);
                          inp = new ImList(q, inp);
                      }
                      else if (io == "w") {
                          p.addTo(outs);
                          outp = new ImList(q, outp);
                      }
                      else if (io == "m") {
                          p.addTo(inouts);
                          inoutp = new ImList(q, inoutp);
                      }
                  }

                  int pos = 0;
                  for (int i = 0, n = ins.size(); i < n; i++) {
                      p = (AsmParam) ins.get(i);
                      p.setPos(pos++);
                      argp = new ImList(p.getType(), argp);
                  }
                  for (int i = 0, n = outs.size(); i < n; i++) {
                      p = (AsmParam) outs.get(i);
                      p.setPos(pos++);
                      argp = new ImList(p.getType(), argp);
                  }
                  for (int i = 0, n = inouts.size(); i < n; i++) {
                      p = (AsmParam) inouts.get(i);
                      p.setPos(pos++);
                      argp = new ImList(p.getType(), argp);
                  }

                  argp = argp.destructiveReverse();
                  asm = new ImList(ARG_M, new ImList(argp, asm));
              }

              // construct mandatory part.
              body = doAsmBody(body, asm_params);
              inp = inp.destructiveReverse();
              outp = outp.destructiveReverse();
              inoutp = inoutp.destructiveReverse();

              asm = prefix(ASM, quote(body), inp, outp, inoutp, asm);
              addProfInfo(list(INFO, "BEGIN", ASM));
              addLexp(asm);
              addProfInfo(list(INFO, "END", ASM));
              break;
          }

      case HIR.OP_NULL:
      case HIR.OP_PHI:
      default:
        // System.out.println("!!" + pHir.getOperator());
        break;
      }
      pHir = (HIR)pHir.getNextStmt();
    }
    return null;
  }

  AsmParamList doAsmParam(String s, boolean regOnly) {
    int i = 0;
    AsmParamList ret = new AsmParamList();

    AsmParam p = no_param;

    debug("param = " + s);
    while (true) {
      p = new AsmParam();

      // eat white sp. (but in Java manner ...)
      while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
        i++;
      }
      if (i >= s.length()) {
        break;
      }

      // modifier
      if (!regOnly) {
        if (s.charAt(i) == 'w' || s.charAt(i) == 'm') {
          p.setIo(s.substring(i, i + 1));
          i++;
        }
        if (i >= s.length()) {
          break;
        }
      }

      // type
      switch (s.charAt(i)) {
      case '%':
        int sta = i;
        while (i < s.length() && !Character.isWhitespace(s.charAt(i)) && s.charAt(i) != ',') {
          i++;
        }
        p.setType(s.substring(sta, i));
        break;
      case 'a': case 's':
        if (!regOnly) {
          p.setType(s.substring(i, i + 1));
          i++;
          break;
        }
        // fall thru */
      default:                        // bad format
        // todo: error message
        System.err.println("bad format: " + s.substring(i));
        return ret;
      }
      debug("" + p);
      if (p.isValid()) {
        p.addTo(ret);
      }
      p = no_param; // no_param is required in any case. //##74
      // eat trailing part.
      boolean garbage = false;
      while (i < s.length() && s.charAt(i) != ',') {
        if (!Character.isWhitespace(s.charAt(i))) {
          garbage = true;
        }
        i++;
      }
      if (i >= s.length()) {        // end of param
        debug("found end");
        break;
      }
      if (s.charAt(i) != ',') {
        // trailer is garbage.
        debug("garbage [" + s.substring(i) + "]");
        // todo: warning ?
        break;
      }
      // found next param
      i++;
    }

    if (p.isValid()) {
      debug("" + p);
      p = p.addTo(ret);
    }
    debug("next param = [" + s.substring(i) + "]");
    return ret;
  }

  String doAsmBody(String s, AsmParamList params) {
    int i = 0;
    StringBuffer dst = new StringBuffer();
    while (i < s.length()) {
      while (i < s.length() && s.charAt(i) != '%') {
        dst.append(s.charAt(i++));
      }
      if (i >= s.length()) {
        break;
      }
      if (s.charAt(i) == '%') {
        if (i + 1 >= s.length()) {
          // % terminate string, invalid
          // todo: error message
          System.err.println("single % in asm.");
          break;
        }
        dst.append('%');
        i++;
        if (s.charAt(i) == '%') { // %% -> %
          i ++;
            continue;
        }
        // read position
        if (s.charAt(i) == '0') { // 0 can not be used but leave it tentatively.
          dst.append('0');
          i++;
          continue;
        }
        if (Character.isDigit(s.charAt(i))) {        // digit+
          int t = i;
          while (i < s.length() && Character.isDigit(s.charAt(i))) {
            i++;
          }
          int pos = Integer.parseInt(s.substring(t, i)) - 1; // %1 -> 0
          if (params != null) {
              debug("pos = " + pos + " -> " + params.get(pos).getPos());
              dst.append(params.get(pos).getPos() + 1); // 0 -> %1
          }else { // #param is not given but the body contains %[1-9]. //##74
              // todo: error message
              System.err.println("positional parameter used, but no #param line");
              dst.append(pos);
          }
        }
      }
    }
    return dst.toString();
  }

  //##64N BEGIN
  private void
    jumpc2(HIR pHir, Label labelTrue, Label labelFalse, ConvContext cc){
    int lab;
    Label newLabel;
    ImList newLabDef; // This declaration may be unnecessary.
    switch (pHir.getOperator()){
    case HIR.OP_NOT:
      jumpc2((HIR)pHir.getChild1(), labelFalse, labelTrue, cc);
      return;
    case HIR.OP_AND:
      lab = labelCtr++;
      newLabel = fHirRoot.sym.defineLabel(prefLirLabel + lab);
      jumpc2((HIR)pHir.getChild1(), newLabel, labelFalse, cc);
      newLabDef = convertDefLirLabel(lab);
      addLexpBeforeInfo(newLabDef);
      jumpc2((HIR)pHir.getChild2(), labelTrue, labelFalse, cc);
      return;
    case HIR.OP_OR:
      lab = labelCtr++;
      newLabel = fHirRoot.sym.defineLabel(prefLirLabel + lab);
      jumpc2((HIR)pHir.getChild1(), labelTrue, newLabel, cc);
      newLabDef = convertDefLirLabel(lab);
      addLexpBeforeInfo(newLabDef);
      jumpc2((HIR)pHir.getChild2(), labelTrue, labelFalse, cc);
      return;
    default:
      ImList cond = convertConditionNode(pHir, cc);
      addLexpBeforeInfo(jumpc(cond, labelTrue, labelFalse));
      return;
    }
  } // jumpc2

  private ImList
    convertConditionNode(HIR pHir, ConvContext cc)
    {
      int lOperator = pHir.getOperator();
      if ((lOperator >= HIR.OP_CMP_EQ)&&(lOperator <= HIR.OP_CMP_LE)) {
        return convertNode(pHir, cc);
      }else { // non-comparison expression
        Exp lCondExp = (Exp)pHir.conditionalExp((Exp)pHir);
        return convertNode(lCondExp, cc);
      }
    } // convertConditionNode
  //##64N END

  /** Convert expression stroing x.member / x->member. **/
  private ImList convertStoreMember(ImList base, Elem elem, Type type,
                                    ImList value, ConvContext cc) {
    String ltype = htype2ltype(type);
    String inttype = hkind2ltype(Type.KIND_INT);
    String ptrtype = hkind2ltype(Type.KIND_POINTER);

    long offset = elem.evaluateDisp();

    ImList result;
    if (elem.isBitField()) {
      //System.out.println("*** store member: bitfield " + elem);
      int cellType = Type.KIND_INT;
      int wordbits = machineParam.evaluateSize(Type.KIND_INT) * 8;
      int bitoffset = elem.getBitOffset();
      int bitwidth = elem.getBitSize();
      if (machineParam.isBigEndian())
        bitoffset = wordbits - bitoffset - bitwidth;
      boolean isUnsigned = elem.getSymType().isUnsigned();
      long mask = (1L << bitwidth) - 1;

      if (false)
        System.out.println("loadmember: " + elem + ": "
                           + " type=" + elem.getSymType());

      String tmpName = uniqStrName(tempCtr++, "ptr");
      autoSym = prefix
        (frameEnt
         (tmpName, ptrtype,
          String.valueOf(machineParam.getAlignment(Type.KIND_POINTER)),
          ZERO),
         autoSym);
      addLexp(list(SET, ptrtype,
                   memExp(ptrtype, frameExp2(tmpName)),
                   address(base, intConst(ptrtype, offset))));
      ImList lval = memExp(hkind2ltype(cellType),
                           memExp(ptrtype, frameExp2(tmpName)));
      ImList tree = lval;
      if (cellType != Type.KIND_INT)
        tree = list(CONVZX, inttype, lval);
      result = list
        (SET, hkind2ltype(cellType), lval, list
         (CONVIT, hkind2ltype(cellType), list
          (BOR, inttype, list
           (BAND, inttype, tree, intConst(inttype, ~(mask << bitoffset))), list
           (LSHU, inttype, list
            (BAND, inttype, value,
             intConst(inttype, mask)),
            intConst(inttype, bitoffset)))));
    } else {
      result = address(base, intConst(ptrtype, offset));
      debug("disp: " +        offset);

      { // Tue Mar 16 21:10:29 JST 2004
        // clip by src size
        if (vectorAssignLen > 0) {
          ltype = size2ltype(vectorAssignLen);
        }
      }
      // Mon Feb 14 16:10:33 JST 2005
      // result = list(SET, ltype, memExp(ltype, result), value);
      result = list(SET, ltype,
                    memExp(ltype, result, type.isVolatile(), getAggregateAlign(type), optId(result)),
                    value);
    }
    return result;
  }

  /** Convert expression loading x.member / x->member. **/
  private ImList convertLoadMember(ImList base, Elem elem,
                                   Type type, ConvContext cc) {
    String ltype = htype2ltype(type);
    String inttype = hkind2ltype(Type.KIND_INT);
    String ptrtype = hkind2ltype(Type.KIND_POINTER);

    long offset = elem.evaluateDisp();

    ImList result;
    if (elem.isBitField()) {
      //System.out.println("*** load member: bitfield " + elem);

      if (cc.isLvalue())
        throw new Error("taking address of bit field");

      int cellType = Type.KIND_INT;
      int wordbits = machineParam.evaluateSize(Type.KIND_INT) * 8;
      int bitoffset = elem.getBitOffset();
      int bitwidth = elem.getBitSize();
      if (machineParam.isBigEndian())
        bitoffset = wordbits - bitoffset - bitwidth;
      boolean isUnsigned = elem.getSymType().isUnsigned();
      long mask = (1L << bitwidth) - 1;

      if (false)
        System.out.println("loadmember: " + elem + ": "
                           + " type=" + elem.getSymType());

      ImList tree = memExp(hkind2ltype(cellType),
                           address(base, intConst(ptrtype, offset)));
      if (cellType != Type.KIND_INT)
        tree = list(CONVZX, inttype, tree);
      if (isUnsigned) {
        tree = list
          (RSHU, inttype, tree, intConst(inttype, bitoffset));
        result = list(BAND, inttype, tree, intConst(inttype, mask));
      } else {
        tree = list
          (RSHS, inttype,
           list(LSHU, inttype, tree,
                intConst(inttype, wordbits - (bitwidth + bitoffset))),
           intConst(inttype, wordbits - bitwidth));
        result = tree;
      }
    } else {
      result = address(base, intConst(ptrtype, offset));
      debug("disp: " +        offset);

      { // Tue Mar 16 21:10:29 JST 2004
        // clip by src size
        if (vectorAssignLen > 0) {
          ltype = size2ltype(vectorAssignLen);
        }
      }
      result = cc.isLvalue() ? result
        : memExp(ltype, result, type.isVolatile(), getAggregateAlign(type), optId(result));
    }
    return result;
  }

  // Get element type of multi-dimensional array.
  private Type getVectorElemType(Type pVec)
    {
      Type ty = ((VectorType) pVec).getElemType();
      if (ty instanceof VectorType) {
        return getVectorElemType((VectorType) ty);
      }
      return ty;
    }

  //private long evalConst(HIR pHir) {
  //ConstNode c;
  //bug("bit field initializer is not supported !");
  //if (pHir.getOperator() == HIR.OP_CONV) {
  //c = ((Exp) pHir.getChild1()).evaluate();
  //} else {
  //  //c = ((Exp) pHir).evaluate();
  //}
  //return 0; //c.getLongValue();
  // }

  private ImList convertCmp(HIR pHir, String pOpname, ConvContext cc)
    {
      String ifexpType = hkind2ltype(Type.KIND_INT);
      ImList l = convertNode((HIR)pHir.getChild1(), cc.emitIfExp());
      ImList r = convertNode((HIR)pHir.getChild2(), cc.emitIfExp());
      ImList tmp = list(pOpname, htype2ltype(pHir.getType()), l, r);

      switch (pHir.getOperator()) {
      default:
        bug("cmp(binop)");
        break;
      case HIR.OP_CMP_EQ:
      case HIR.OP_CMP_NE:
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
        // require t = lt = rt
        if (cc.isEmitIfExp()) {
          tmp = list(IF, ifexpType, tmp, intConst(ifexpType, 1), intConst(ifexpType, 0));
        }
        break;
      }
      return tmp;
    }

  // Convert binary operator node
  private ImList convertBinop(HIR pHir, String pOpname, ConvContext cc)
    {
      ImList l = convertNode((HIR)pHir.getChild1(), cc);
      ImList r = convertNode((HIR)pHir.getChild2(), cc);
      ImList tmp = list(pOpname, htype2ltype(pHir.getType()), l, r);

      switch (pHir.getOperator()) {
      default:
        break;
      case HIR.OP_CMP_EQ:
      case HIR.OP_CMP_NE:
      case HIR.OP_CMP_GT:
      case HIR.OP_CMP_GE:
      case HIR.OP_CMP_LT:
      case HIR.OP_CMP_LE:
        bug("binop(cmp)");
        break;
      }

      // type consistency check
      if (false) {
        Type t = pHir.getType();
        Type lt = ((HIR)pHir.getChild1()).getType();
        Type rt = ((HIR)pHir.getChild2()).getType();

        switch (pHir.getOperator()) {
        default:
          if (!t.equals(lt) || !t.equals(rt)) {
            badHIR("type constraint violation in " + pHir.toStringDetail());
            System.out.println("node " + t.toStringDetail());
            System.out.println("l " + lt.toStringDetail());
            System.out.println("r " + rt.toStringDetail());
          }
          break;
        case HIR.OP_SHIFT_R:
        case HIR.OP_SHIFT_LL:
        case HIR.OP_SHIFT_RL:
          // match pHir and left or right op.
          if (!t.equals(lt) && !t.equals(rt)) {
            badHIR("type constraint violation in " + pHir.toStringDetail());
            System.out.println("node " + t);
            System.out.println("l " + lt);
            System.out.println("r " + rt);
          }
          break;
        case HIR.OP_ADD:
        case HIR.OP_SUB:
          if (t.isInteger() || t.isFloating()) {
            if (!t.equals(lt) || !t.equals(rt)) {
              badHIR("type constraint violation in " + pHir.toStringDetail());
              System.out.println("node " + t);
              System.out.println("l " + lt);
              System.out.println("r " + rt);
            }
          }
          break;
        case HIR.OP_CMP_EQ:
        case HIR.OP_CMP_NE:
        case HIR.OP_CMP_GT:
        case HIR.OP_CMP_GE:
        case HIR.OP_CMP_LT:
        case HIR.OP_CMP_LE:
          if (!lt.equals(rt)) {
            badHIR("type constraint violation in " + pHir.toStringDetail());
            System.out.println("l " + lt);
            System.out.println("r " + rt);
          }
          break;
        }
        //System.out.println("binop: " + tmp);
      }
      return tmp;
    }

  // __builtin_align()
  private ImList convert_builtin_align(ListIterator params, ConvContext cc) {
    // __builtin_align
    ImList ptr = ImList.Empty;
    builtin_align = 0;
    if (params.hasNext()) {
      ptr = convertNode((HIR)params.next(), cc);
    }
    if (params.hasNext()) {
      HIR tmp = (HIR)params.next();
      if (tmp instanceof ConstNode) {
        builtin_align = ((ConstNode) tmp).getConstSym().intValue();
      }
    }
    if (ptr == null) {
      badHIR("__builtin_align(bad pointer)");
    }
    if (builtin_align == 0) {
      badHIR("__builtin_align(bad align) -> treat as 4");
      builtin_align = 4;
    }
    return ptr;
  }

  // Convert unary operator node
  private ImList convertUnaop(HIR pHir, String pOpname, ConvContext cc) {
    ImList tmp = convertNode((HIR)pHir.getChild1(), cc);
    return list(pOpname, htype2ltype(pHir.getType()), tmp);
  }

  // Generate DEFLABEL
  private ImList convertDeflabel(Label pLabel) {
    appearedLabels.add(pLabel);
    return list(DEFLABEL, quote(pLabel.getName()));
  }
  private ImList convertDefLirLabel(int pLabel) {
    return list(DEFLABEL, quote(prefLirLabel + pLabel));
  }

  // Return LABEL operand
  private ImList labelOperand(Label pLabel) {
    return list(LABEL, hkind2ltype(Type.KIND_POINTER), quote(pLabel.getName()));
  }
  private ImList lirLabel(int pLabel) {
    return list(LABEL, hkind2ltype(Type.KIND_POINTER), quote(prefLirLabel + pLabel));
  }

  // Construct LIR Sexp.
  private boolean isReg(Sym sym) {
    switch (sym.getSymType().getTypeKind()) {
    case Type.KIND_VECTOR:
    case Type.KIND_STRUCT:
    case Type.KIND_UNION:
      return false;
    default:
      return ((Var)sym).getStorageClass() == Var.VAR_REGISTER;
    }
  }

  private ImList intConst(Object type, Object num) {
    return list(INTCONST, type, num);
  }
  private ImList intConst(Object type, long num) {
    return list(INTCONST, type, String.valueOf(num));
  }
  private ImList floatConst(Object type, Object num) {
    return list(FLOATCONST, type, num);
  }
  private ImList floatConst(Object type, double num) {
    return list(FLOATCONST, type, String.valueOf(num));
  }
  private ImList frameExp2(String s) {
    return list(FRAME, hkind2ltype(Type.KIND_POINTER), quote(s));
  }
  private ImList frameExp2(QuotedString qs) {
    return list(FRAME, hkind2ltype(Type.KIND_POINTER), qs);
  }
  private ImList frameExp(Object type, String s) {
    return list(FRAME, type, quote(s));
  }
  private ImList frameExp(Object type, QuotedString qs) {
    return list(FRAME, type, qs);
  }

  private static QuotedString complexMem = new QuotedString(" _ ");

  // Wed Jun 14 11:13:37 JST 2006
  private QuotedString optId(ImList l) {
    QuotedString tmp = null;
    if (true /*generateProfileInfo*/) {
      Object target = car(l);
      if (target == FRAME || target == STATIC) {
        Object id = car(cdr(cdr(l)));
        if (id instanceof QuotedString) {
          tmp = (QuotedString) id;
        } else {
          bug("broken FRAME/STATIC: " + l);
        }
      } else {
        tmp = complexMem;
      }
      // System.out.println("prof = true, passed: " + l.toString());
    } else {
      // System.out.println("prof = false but passed: " + l.toString());
    }
    return tmp;
  }

  private ImList memExp(Object type, Object exp) {
    return list(MEM, type, exp);
  }
  private ImList memExp(Object type, Object exp, Object m) {
    return list(MEM, type, exp, m);
  }

  private ImList memExp(Object type, Object exp, boolean isVolatile, int align, Object id) {
    ImList tmp = ImList.Empty;
    if (align <= 1) {
      align = -1;
    }
    //##75 if (/*generateProfileInfo &&*/ id != null)
    if (simulateOption || generateLine) { //##75
      // Wed Jul  5 18:10:52 JST 2006
      if (id == complexMem) {
        tmp = list(ID_M, String.valueOf(memNum++));
      } else if (id instanceof QuotedString) {
        tmp = list(ID_M, list(id, String.valueOf(memNum++)));
      } else {
        bug("id is not QS");
        tmp = list(ID_M, String.valueOf(memNum++));
      }
      /*
      else {
        if (id == complexMem) {
          tmp = list(ID_M, new Integer(memNum++));
        } else {
          tmp = list(ID_M, list(id, new Integer(memNum++)));
        }
      }
      */
    }

    if (!isVolatile && align < 0) {
      return prefix(MEM, type, exp, tmp);
    }
    if (!isVolatile) {
      return prefix(MEM, type, exp, ALIGN_M, String.valueOf(align), tmp);
    }
    if (align < 0) {
      return prefix(MEM, type, exp, VOLATILE_M, tmp);
    }
    tmp = prefix(ALIGN_M, String.valueOf(align), tmp);
    return prefix(MEM, type, exp, VOLATILE_M, tmp);
  }

  private ImList staticExp(String s) {
    return list(STATIC, hkind2ltype(Type.KIND_POINTER), quote(s));
  }
  private ImList staticExp(QuotedString s) {
    return list(STATIC, hkind2ltype(Type.KIND_POINTER), s);
  }
  private ImList jump(Label label) {
    return list(JUMP, labelOperand(label));
  }
  private ImList jumpc(Object cond, Label label1, Label label2) {
    return list(JUMPC, cond, labelOperand(label1), labelOperand(label2));
  }

  // used by op_until
  private ImList jumpc(Object cond, int label1, Label label2) {
    return list(JUMPC, cond, lirLabel(label1), labelOperand(label2));
  }

  private ImList address(Object l, Object r) {
    return list(ADD, hkind2ltype(Type.KIND_POINTER), l, r);
  }


  // Wed Mar 22 19:11:31 JST 2006
  // optional HIR symbol information
  private ImList optsym(Sym sym) {
    String filename = sym.getDefinedFile();
    // file name might be null.
    if (filename == null) {
      filename = "";
    }
    return list(OPTSYM_M,
                quote(sym.getUniqueName()),
                quote(filename),
                new Integer(sym.getDefinedLine()),
                new Integer(sym.getDefinedColumn()));
  }

  // make symbol table entry of local symbol
  private ImList autoEnt(Sym sym) {
    String type = UNKNOWN;
    String store = FRAME;

    if (sym.getSymKind() == Sym.KIND_VAR
        || sym.getSymKind() == Sym.KIND_PARAM) {
      if (sym.getSymType().getSizeValue() <= 0) {
        bug("**** size <= 0" + sym.getSymType());
      }

      type = htype2ltype(sym.getSymType());
      // Wed Jan 7 13:13:08 JST 2004
      // UNKNOWN local auto symbol is forbidden in LIR
      // -> treat as INT
      if (type == UNKNOWN) {
        badHIR("unknown size auto symbol -> treat as INT " + sym);
        type = hkind2ltype(Type.KIND_INT);
      }
      // storage class
      if (((Var)sym).getStorageClass() == Var.VAR_AUTO) {
        store = FRAME;
      } else if (((Var)sym).getStorageClass() == Var.VAR_REGISTER) {
        store = REG;
      } else {
        bug("bad storage class:" + sym + " -> treat as FRAME");
      }
    }

    ImList opt = optsym(sym);
    opt = prefix(type,
                 String.valueOf(getAlignment(sym)),
                 ZERO, opt);
    return prefix(uniqName(sym), store, opt);
  }


  private ImList frameEnt(String s, Object type, Object align, Object offset) {
    return list(quote(s), FRAME, type, align, offset);
  }

  // make symbol table entry of global symbol
  private ImList staticEntBase(Sym sym, QuotedString qs, QuotedString seg, Object linkage) {
    String type = UNKNOWN;
    if (sym.getSymKind() == Sym.KIND_VAR) {
      type = htype2ltype(sym.getSymType());
    }
    ImList opt = optsym(sym);
    opt = prefix(type,
                 String.valueOf(getAlignment(sym)),
                 seg, linkage, opt);
    return prefix(qs, STATIC, opt);
  }

  private ImList staticEnt(Sym sym, QuotedString seg, Object linkage) {
    return staticEntBase(sym, uniqName(sym), seg, linkage);
  }

  private ImList staticEntString(Sym sym) {
    return staticEntBase(sym, quote(stringName(sym)),
                         LITERAL_SEG, LDEF);
  }

  private ImList strConst(String name, String s) {
      return list(DATA, quote(name), charSeq(s));
  }
  private ImList strConst(String name, String s, long len) {
    // Tue Mar 16 19:34:57 JST 2004
    // charSeq(s, len) might be return ((I8 ...) (ZEROS n))
    ImList tmp = charSeq(s, len);
    return list(DATA, quote(name), tmp);
  }
  private ImList charSeq(String s) {
    return strconv.charSeq(s);
  }
  private ImList charSeq(String s, long len) {
    return strconv.charSeq(s, len);
  }

  private int setCurLine(HIR pHir) {
    int thisLine = -1;

    if (!generateLine)
      return thisLine;

    if (pHir instanceof Stmt) {
      Stmt tmp = (Stmt) pHir;
      if (tmp.getFileName() != null) {
        if (curline != tmp.getLineNumber()) {
          thisLine = tmp.getLineNumber();
          //System.out.println("#" + tmp.getFileName() + " " + tmp.getLineNumber());
        }
        if (thisLine == 0) {
          debug("no lineno for " + tmp);
        }

      } else if (pHir.getOperator() == HIR.OP_ASSIGN) {
        // Thu Jul 29 14:42:52 JST 2004
        // cfront does NOT set line info of auto initialized variables
        HIR lhs = (HIR) pHir.getChild1();
        if (lhs == null) {
          badHIR("op_assign.getChild1() returns null. " + pHir.toStringDetail());
          return thisLine;
        } else if (lhs.getOperator() == HIR.OP_VAR) {
          Sym sym = ((SymNode) lhs).getSymNodeSym();
          if (sym.getDefinedFile() != null && curline < sym.getDefinedLine()) {
            thisLine = sym.getDefinedLine();
          }
          if (thisLine == 0) {
            debug("no lineno for " + sym);
          }
        }
      }
      if (thisLine != -1 && thisLine != 0) {
        curline = thisLine;
      }
    }

    return thisLine;
  }

  private void lineInfo(HIR pHir) {
    if (!generateLine)
      return;

    int thisLine = setCurLine(pHir);

    if (thisLine != -1 && thisLine != 0) {
      /* see comments in addLexpAfterInfo() */
      if (bblockProfInfo != ImList.Empty) {
        ImList t = list(bblockProfInfo, list(LINE, String.valueOf(thisLine)));
        addProfInfo(new ImList(INFO, t));
        bblockProfInfo = ImList.Empty;
      }

      //##101 addLexp(list(INFO, LINE, String.valueOf(thisLine)));
      //##101 BEGIN
      ImList lInf;
      IrList lIrList = pHir.getInfList();
      if (lIrList != null) {
        //## StringBuffer lLineEtc = new StringBuffer("");
        //## lLineEtc.append(infListString(lIrList));
     	ImList lInfList = infListToImList(ImList.Empty, lIrList);
    	lInf = list(INFO, LINE, String.valueOf(thisLine), list(INFO, " " 
    			+ lInfList.toString()));
    	debug("\n HIR node inf "+ lInf.toString());
    	/*##
    	System.out.print("\n lInfList");
    	ImList lList = lInfList;
    	for (; lList != null&(!lList.isEmpty()); lList = lList.next()) {
    	  Object lObj = lList.elem();
    	  if (lObj == null)
    	    System.out.print(" <null>");
    	  else 
      	    System.out.print(" "+ lObj.toString());
    	}
    	##*/
      }else {
    	lInf = list(INFO, LINE, String.valueOf(thisLine));
      }
      addLexp(lInf);
      //##101 END
    }
    return;
  } // linrInfo(HIR)

  /** emit lineinfo to data */
  private void lineInfo(Sym sym) {
    if (!generateLine)
      return;

    if (sym.getDefinedFile() != null) {
        addLbody(list(INFO, LINE, String.valueOf(sym.getDefinedLine())));
        debug("sym defined: " + sym.getDefinedLine());
    }
  } // lineInfo(Sym)

  //##101 BEGIN
  private ImList infListToImList(ImList pImList, IrList pIrList)
  {
    //## System.out.print("n infListToImList " + pIrList.toString()); //###
    ImList lList = pImList;
	if (! pIrList.isEmpty()) {
	  //## System.out.print(" size " + pIrList.size());
	  for (Iterator lIt = pIrList.iterator(); lIt.hasNext();) {
		Object lElem = lIt.next();
		//## System.out.print(" " + lElem + " class=" + lElem.getClass()); //###
		if (lElem instanceof HirList) {
		  lList = lList.append(infListToImList(pImList, (IrList)lElem));
		}else {
		  if (lElem instanceof Var) {
			lList = lList.append(list(((Var)lElem).getUniqueName())); 
		  }else {
		    lList = lList.append(list(lElem));
		  }
		}
	  }
	}
	  return lList;
  } // infListToImList
  
  private String infListString(IrList pIrList)
  {
	StringBuffer lStringB = new StringBuffer("");
	//## System.out.print("n infListToImList " + pIrList.toString()); //###
	if (pIrList.isEmpty())
	  return "";
	else {
	  //## System.out.print(" size " + pIrList.size()); //###
	  for (Iterator lIt = pIrList.iterator(); lIt.hasNext();) {
		Object lElem = lIt.next();
		//## System.out.print(" " + lElem + " class=" + lElem.getClass()); //###
		if (lElem instanceof HirList) {
	      lStringB.append(" " + infListString((IrList)lElem));
	    }else {
		  if (lElem instanceof Var) {
			String lVarName = ((Var)lElem).getUniqueName();
			lStringB.append(" " + lVarName);
		  }else {
		    lStringB.append(lElem.toString());
		  }
		}
	  }
	  return lStringB.toString();
	}
  } // infListString
  //##101 END
  private ImList zeros(long n) {
    return list(ZEROS, String.valueOf(n));
  }

  // Wed Jun  2 14:45:55 JST 2004
  private ImList symEnt(String type, Sym sym) {
    ImList tmp = null;

    if (sym.getSymKind() == Sym.KIND_ELEM) {
      tmp = list(uniqName(sym),
                 STATIC, UNKNOWN, "1", DSEG, XREF);
      needNotInit(sym);
      setLirSym(sym);
    }

    if (sym.getSymKind() == Sym.KIND_VAR) {
      if (((Var)sym).getVisibility() == Sym.SYM_EXTERN) {
        // extern decl.
        // Mon Apr 19 20:56:32 JST 2004
        if (pureExterns.get(sym.getName()) == null) {
          // System.out.println("*** pure external " + sym.getName());
          pureExterns.put(sym.getName(), "");
          tmp = staticEnt(sym, DSEG, XREF);
          setLirSym(sym); // sym is real extern symbol.
          needNotInit(sym);
        }
      }
      else if (((Var)sym).getStorageClass() == Var.VAR_STATIC) {
        // Wed Jun  2 15:50:19 JST 2004
        // already numbered.
        // numberingSym(sym);

        if (emitRoseg && ((Var)sym).getSymType().isConst()) {
          tmp = staticEnt(sym, ROSEG, LDEF);
        } else {
          tmp = staticEnt(sym, DSEG, LDEF);
        }
        setLirSym(sym);
      }
    }

    /* Tue Aug 26 20:03:39 JST 2003 */
    if (sym.getSymKind() == Sym.KIND_SUBP) {
      String linkage = LDEF;
      switch (((Subp)sym).getVisibility()) {
      case Sym.SYM_PUBLIC: linkage = XDEF; break;
      case Sym.SYM_EXTERN: linkage = XREF; break;
      case Sym.SYM_PRIVATE:
      case Sym.SYM_COMPILE_UNIT:
        break;
      }
      tmp = staticEnt(sym, CSEG, linkage);
      if (debugUniqFun) {
        System.out.println("func " + sym + " -> " + uniqName(sym));
      }
      needNotInit(sym);
      setLirSym(sym);
    }
    return tmp;
  }

  // aggregate type ?
  private boolean isAggregate(Type pType) {
    int pTypeKind = pType.getTypeKind();
    switch (pTypeKind) {
    case Type.KIND_VECTOR:
    case Type.KIND_STRUCT:
    case Type.KIND_UNION:
      return true;
    }
    return false;
  }

  // need &align ?
  private int getAggregateAlign(Type pType) {
    return isAggregate(pType) ? pType.getAlignment() : 0;
  }

  // Convert HIR type to LIR type
  private String htype2ltype(Type pType) {
    int pTypeKind = pType.getTypeKind();
    switch (pTypeKind) {
    case Type.KIND_VECTOR:
    case Type.KIND_STRUCT:
    case Type.KIND_UNION:
      // Thu May 20 20:32:28 JST 2004
      long len = pType.getSizeValue();
      if (len <= 0) {
        // System.out.println("aggregate size is not constant -> treat as A32");
        return UNKNOWN;
      }
      //##87 return "A" + String.valueOf(len * 8);
      //##88 return "A" + String.valueOf(len * machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT); //##87
      return "A" + String.valueOf(len * machineParam.numberOfBitsInAddressingUnit()); //##88

    default:
      return hkind2ltype(pTypeKind);
    }
  }

  private String size2ltype(long n) {
    //##87 return "A" + (n * 8);
    //##88 return "A" + (n * machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT); //##87
    return "A" + (n * machineParam.numberOfBitsInAddressingUnit()); //##88
  }

  private String n2Ixx(int n) {
    //##87 return "I" + (n * 8);
    //##88 return "I" + (n * machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT); //##87
    return "I" + (n * machineParam.numberOfBitsInAddressingUnit()); //##88
  }

  // Convert HIR type kind to LIR type
  private String hkind2ltype(int pTypeKind)
    {
      if (pTypeKind == Type.KIND_STRING)
        pTypeKind = Type.KIND_POINTER;
      switch (pTypeKind) {
      case Type.KIND_UNDEF:
      case Type.KIND_BOOL:
        //        case Type.KIND_S_CHAR:
      case Type.KIND_SHORT:
      case Type.KIND_INT:
      case Type.KIND_LONG:
      case Type.KIND_LONG_LONG:
      case Type.KIND_CHAR:
      case Type.KIND_U_CHAR:
      case Type.KIND_U_SHORT:
      case Type.KIND_U_INT:
      case Type.KIND_U_LONG:
      case Type.KIND_U_LONG_LONG:
      case Type.KIND_ADDRESS:
      case Type.KIND_OFFSET:
      case Type.KIND_ENUM:
      case Type.KIND_POINTER:
        /* //##87
        switch (machineParam.evaluateSize(pTypeKind)) {
        case 1: return "I8";
        case 2: return "I16";
        case 4: return "I32";
        case 8: return "I64";
        case 16: return "I128";
        default:
          // assert(0);
          ;
        }
        */ //##87
        return prefixStringForInt(machineParam.evaluateSize(pTypeKind)); //##87

      case Type.KIND_VOID:
        return UNKNOWN;

      case Type.KIND_FLOAT:
      case Type.KIND_DOUBLE:
      case Type.KIND_LONG_DOUBLE:
        /* //##87
        switch (machineParam.evaluateSize(pTypeKind)) {
        case 4:  return "F32";
        case 8:  return "F64";
        case 16: return "F128";
        default:
          // assert(0);
          ;
        }
        */ //##87
        return prefixStringForFloat(machineParam.evaluateSize(pTypeKind)); //##87

      case Type.KIND_STRING:
      case Type.KIND_STRUCT:
      case Type.KIND_UNION:
      case Type.KIND_DEFINED:
      case Type.KIND_SUBP:
        return "AGGREGATE";
      }

      return UNKNOWN;
    }


  // Print Quoted String
  private void printQuotedString(String s)
    {
      int n = s.length();
      StringBuffer buf = new StringBuffer();
      buf.append('"');
      for (int i = 0; i < n; i++) {
        char c = s.charAt(i);
        if (' ' <= c && c <= '~')
          buf.append(c);
        else if (c == '\n')
          buf.append("\\n");
        else if (c == '\t')
          buf.append("\\t");
        else {
          buf.append('\\');
          String v = "00" + Integer.toOctalString((int)c);
          int k = v.length();
          buf.append(v.substring(k - 3));
        }
      }
      buf.append('"');
    }

  private int getSymNum(Sym pSym)
    {
      SymStat tmp = (SymStat) symNumbers.get(pSym);
      if (tmp == null) {
        return SymStat.UNKNOWN;
      }
      return tmp.num();
    }

  // generate string lieteral
  private String stringName(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      return "bug: " + pSym;
    }
    return "string" + "." + tmp.num();
  }

  private void install(Sym pSym)
    {
      SymStat tmp = new SymStat(pSym);
      symNumbers.put(pSym, tmp);
    }

  private void installSpecial(Sym pSym)
    {
      SymStat tmp = new SymStat(pSym);
      symNumbers.put(pSym, tmp);
      tmp.numbering();
    }

  // Fri Jun 25 18:59:13 JST 2004
  private boolean isRemoved(Sym pSym) {
    if (pSym.isRemoved()) {        // front bug
      badHIR("removed symbol is used [" + pSym.toStringDetail() + "]");
      return true;
    }
    return false;
  }

  // Assign a number to the Symbol pSym
  private int numbering(Sym pSym)
    {
      SymStat tmp = (SymStat) symNumbers.get(pSym);
      return tmp.numbering();
    }

  private boolean isLirSym(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      return false;
    }
    return tmp.isLirSym();
  }

  // pSym is installed LIR symtab.
  private void setLirSym(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      if (!isRemoved(pSym)) {
        bug("1(bad HIR ?) not installed: " + pSym.toStringDetail());
      }
      return;
    }
    tmp.setLirSym();
    return;
  }

  private boolean isUsed(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      // i.e. type name, tag name ...
      return false;
    }
    return tmp.isUsed();
  }

  private void setUsed(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      if (!isRemoved(pSym)) {
        bug("3(bad HIR ?) not installed: " + pSym.toStringDetail());
      }
      return;
    }
    tmp.setUsed();
    return;
  }

  private void resetUsed(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      if (!isRemoved(pSym)) {
        bug("4(bad HIR ?) not installed: " + pSym.toStringDetail());
      }
      return;
    }
    tmp.resetUsed();
    return;
  }

  private boolean isGenInit(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      if (!isRemoved(pSym)) {
        bug("isGenInit(): not installed: " + pSym.toStringDetail());
      }
      return false;
    }
    return tmp.isGenInit();
  }

  // pSym is initialized or need not initialize.
  private void needNotInit(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      if (!isRemoved(pSym)) {
        bug("needNotInit(): not installed: " + pSym.toStringDetail());
      }
      return;
    }
    tmp.needNotInit();
    return;
  }

  private boolean isRewrite(Sym pSym) {
    SymStat tmp = (SymStat) symNumbers.get(pSym);
    if (tmp == null) {
      if (!isRemoved(pSym)) {
        bug("7(bad HIR ?) not installed: " + pSym.toStringDetail());
      }
      return false;
    }
    return tmp.isRewrite();
  }

  private int uniqSym(Sym pSym)
    {
      SymStat tmp = (SymStat) symNumbers.get(pSym);
      if (tmp == null) {
        if (!isRemoved(pSym)) {
          bug("8(bad HIR ?) not installed: " + pSym.toStringDetail());
        }
        return -1;
      } else {
        tmp.setRewrite();
      }
      return tmp.num();
    }

  private int keepName(Sym pSym)
    {
      SymStat tmp = (SymStat) symNumbers.get(pSym);
      if (tmp == null) {
        if (!isRemoved(pSym)) {
          bug("(bad HIR ?) not installed: " + pSym.toStringDetail());
        }
        return -1;
      } else {
        tmp.resetRewrite();
        // System.out.println("keep: " + pSym);
      }
      return tmp.num();
    }

  public void print(ImList l)
    {
      ImPrinter printer = new ImPrinter(new PrintWriter(fOut));
      printer.print(l);
    }

  /* ImPrinter: print ImList representation of LIR */
  /* create: Fri Jun 13 15:36:42 JST 2003 */
  /* update: Mon Jun 23 13:07:57 JST 2003 */
  public final static int DEFAULT_INDENT_STEP = 2; // must be >= 2
  public final static Object[] keywordList = {
    MODULE,
    SYMTAB,
    DATA,
    FUNCTION,
    SET,
    JUMP,
    JUMPC,
    JUMPN,
    DEFLABEL,
    CALL,
    ASM,
    PROLOGUE,
    EPILOGUE,
    PARALLEL,
    CLOBBER,
    INFO
  };
  public final static Object[] tableList = {
    SYMTAB
  };
  public final static Object[] specList = {
    DEFLABEL,
    INFO
  };

  private abstract class StringConverter {
    public StringConverter() {
    }
    abstract ImList charSeq(String s);
    abstract ImList charSeq(String s, long len);
  }
  private class PlainStringConverter extends StringConverter {
    public PlainStringConverter() {
    }
    public ImList charSeq(String s) {
      ImList tmp = ImList.Empty;
      tmp = new ImList(ZERO, tmp);

      for (int i = s.length() - 1; i >= 0; i--) {
        tmp = prefix(String.valueOf((int) s.charAt(i)), tmp);
      }
      //##87 return prefix("I8", tmp); // todo: I8???
      return prefix(prefixStringForChar(), tmp); //##87
    }
    // CAUTION: this method returns ((I8 ...) ()) or ((I8 ...) (ZEROS n))
    public ImList charSeq(String s, long len) {
      ImList tmp = ImList.Empty;
      int i = 0;
      for ( ; i < s.length() && i < len; i++) {
        tmp = prefix(String.valueOf((int) s.charAt(i)), tmp);
      }
      if (false) {                        // Tue Mar 16 19:23:44 JST 2004
        //##87 tmp =  prefix("I8", reverse(tmp));
        tmp =  prefix(prefixStringForChar(), reverse(tmp)); //##87
        if (len - i > 0) {
          tmp = list(tmp, zeros(len - i));
        } else {
          tmp = list(tmp, ImList.Empty);
        }
        return tmp;
      } else {
        for ( ; i < len; i++) {
          tmp = prefix(ZERO, tmp);
        }
        //##87 return prefix("I8", reverse(tmp)); // todo: I8???
        return prefix(prefixStringForChar(), reverse(tmp));  //##87
      }
    }
  }

  private class LiteralStringConverter extends PlainStringConverter {
    private boolean is_special(int c) {
      return c < ' ' || c > '~' || c == '"';
    }
    public ImList charSeq(String s) {
      return charSeq(s, s.length() + 1); // one for \0
    }
    public ImList charSeq(String s, long len) {
      ImList tmp = ImList.Empty;
      StringBuffer b = new StringBuffer();
      int i = 0;
      for ( ; i < s.length() && i < len; i++) {
        int c = (int) s.charAt(i);
        if (is_special(c)) {
          if (b.length() > 0) {
            tmp = prefix(quote(b.toString()), tmp);
            b.delete(0, b.length());
          }
          tmp = prefix(String.valueOf(c), tmp);
        } else {
          b.append((char) c);
        }
      }
      if (b.length() > 0) {
        tmp = prefix(quote(b.toString()), tmp);
        b.delete(0, b.length());
      }
      for ( ; i < len; i++) {
        tmp = prefix(ZERO, tmp);
      }
      //##87 return prefix("I8", reverse(tmp)); // todo: I8???
      return prefix(prefixStringForChar(), reverse(tmp)); //##87
    }
  }

  //##87 BEGIN
private String
prefixStringForChar()
{
  //##88 int lCharBitLeng = machineParam.SIZEOF_CHAR
  //##88   * machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT;
  int lCharBitLeng = machineParam.evaluateSize(Type.KIND_CHAR) //##88
      * machineParam.numberOfBitsInAddressingUnit(); //##88
  String lBitLeng;
  if (lCharBitLeng <= 8)
    lBitLeng = "I8";
  else if (lCharBitLeng <= 16)
    lBitLeng = "I16";
  else if (lCharBitLeng <= 32)
    lBitLeng = "I32";
  else
    lBitLeng = "I64";
  return lBitLeng;
} // prefixStringForChar

private String
prefixStringForInt( int pSize)
{
  int lIntBitLeng = pSize
      //##88  * machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT;
      * machineParam.numberOfBitsInAddressingUnit(); //##88
  String lBitLeng;
  if (lIntBitLeng <= 8)
    lBitLeng = "I8";
  else if (lIntBitLeng <= 16)
    lBitLeng = "I16";
  else if (lIntBitLeng <= 32)
    lBitLeng = "I32";
  else if (lIntBitLeng <= 64)
    lBitLeng = "I64";
  else if (lIntBitLeng <= 128)
    lBitLeng = "I128";
  else
    lBitLeng = "I256";
  return lBitLeng;
} // prefixStringForInt

private String
prefixStringForFloat( int pSize)
{
  int lIntBitLeng = pSize
      //##88  * machineParam.NUMBER_OF_BITS_IN_ADDRESSING_UNIT;
      * machineParam.numberOfBitsInAddressingUnit(); //##88
  String lBitLeng;
  if (lIntBitLeng <= 32)
    lBitLeng = "F32";
  else if (lIntBitLeng <= 64)
    lBitLeng = "F64";
  else if (lIntBitLeng <= 80)
    lBitLeng = "F80";
  else if (lIntBitLeng <= 128)
    lBitLeng = "F128";
  else
    lBitLeng = "F256";
  return lBitLeng;
} // prefixStringForInt
  //##87 END

  private class ImPrinter {
    private int indentStep = DEFAULT_INDENT_STEP;
    private int nest = 0;
    private PrintWriter out;
    private Set keywords;
    private Set tables;
    private Set specs;
    private boolean needSpace = false;
    private final String longSpace =
      /*1234567890123456789012345678901234567890123456789012345678*/
      "                                                            ";
    public ImPrinter() {
      this(new PrintWriter((OutputStream) System.out));
    }

    public ImPrinter(PrintWriter out)
      {
        this.out = out;
        this.keywords = new HashSet();
        this.tables = new HashSet();
        this.specs = new HashSet();

        for (int i = 0; i < keywordList.length; i++) {
          this.keywords.add(keywordList[i]);
        }
        for (int i = 0; i < tableList.length; i++) {
          this.tables.add(tableList[i]);
        }
        for (int i = 0; i < specList.length; i++) {
          this.specs.add(specList[i]);
        }
      }

    private void outString(String s) {
      out.print(s);
      needSpace = true;
    }
    private void outStringln(String s) {
      out.println(s);
      needSpace = true;
    }
    private void outSpace() {
      if (needSpace) {
        out.print(" ");
      }
      needSpace = false;
    }
    private void outSpace(int n) {
      int n1;
      if (needSpace) {
        while (n > 0) {
          n1 = n;
          if (n1 > longSpace.length()) {
            n1 = longSpace.length();
          }
          n -= n1;
          out.print(longSpace.substring(0, n1));
        }
      }
      needSpace = false;
    }

    private void outParens() {
      if (nest > 0) {
        outSpace();
        for (int i = 0; i < nest; i++) {
          outString("(");
        }
      }
      nest = 0;
    }

    private void pp1(Object l, int indent, boolean inTable)
      {
        if (l instanceof ImList) {
          while (l != ImList.Empty) {
            boolean inTable2 = inTable;
            if (car(l) instanceof ImList) {
              if (inTable || keywords.contains(car(car(l)))) {
                outStringln("");
                if (specs.contains(car(car(l)))) {
                  outSpace((indent - 1) * indentStep + indentStep - 1);
                } else {
                  outSpace(indent * indentStep);
                }
                if (tables.contains(car(car(l)))) {
                  inTable2 = true;
                }
              }
              nest++;
            }
            pp1(car(l), indent + 1, inTable2);
            l = cdr(l);
          }
          outParens();
          outString(")");
          return;
        } else {
          // String or QuotedString
          if (l == null) {
            // bad ImList.
            outString("[null]");
          } else {
            if (nest > 0) {
              outParens();
            } else {
              outSpace();
            }
            outString(l.toString());
          }
        }
      }

    public void print(ImList l) {
      nest = 1;
      pp1(l, 1, false);
      outStringln("");
      out.flush();
    }
  }
}

/*
  Local Variables:
  mode: jde
  c-basic-offset: 2
  End:
*/
