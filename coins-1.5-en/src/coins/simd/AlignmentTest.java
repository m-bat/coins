/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Function;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirNode;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;

/**
 * Class for testing alignment
 */
class AlignmentTest {
  private static int addrtype=Type.type(Type.INT, 32);
  private static ImList optDefault=ImList.Empty;
  private Function func;
//  private BiList alignments;
  private BiList srcAlignments;
  private BiList tgtAlignments;
  private final long ONE=1;
  private final LirNode iconst_1;

  /**
   * Constructs a AlignmentTest object
   * @param  f  Function
   */
  public AlignmentTest(Function func) {
    this.func=func;
//    alignments=new BiList();
    srcAlignments=new BiList();
    tgtAlignments=new BiList();
    iconst_1=func.newLir.iconst(addrtype, ONE);
  }
  /**
   * Checks alignments of statements in each basic blocks
   * @return BiList
   */
  public BiList doIt() {
    BiList basicBlkList=func.flowGraph().basicBlkList;
    for(BiLink p=basicBlkList.first(); !p.atEnd(); p=p.next()) {
      chkAlignmentBlk((BasicBlk)p.elem());
    }
//    return alignments;
    return merge();
  }
  private void chkAlignmentBlk(BasicBlk b) {
    for(BiLink insp=b.instrList().first(); !insp.atEnd(); insp=insp.next()) {
      chkAlignment((LirNode)insp.elem());
    }
  }
  private void chkAlignment(LirNode ins) {
//System.out.println("chkAlignment:"+ins);
    if(ins.opCode==Op.PARALLEL && ins.kid(0).opCode==Op.SET) {
      int k=ins.nKids();
      LirNode ins0=ins.kid(0);
      if(ins0.kid(0).opCode==Op.MEM) {
//        alignments.add(new Alignment(ins0.kid(0), k));
        tgtAlignments.add(new Alignment(ins0.kid(0), k));
      }
      if(ins0.kid(1).opCode==Op.MEM) {
//        alignments.add(new Alignment(ins0.kid(1), k));
        srcAlignments.add(new Alignment(ins0.kid(1), k));
      }
    }
  }
  /**
   * Makes condition codes of overlapping of memory access
   *
   * @return LirNode
   */
  public LirNode memAccessOverlappingTest() {
    BiList codes=new BiList();
    for(BiLink sp=srcAlignments.first(); !sp.atEnd(); sp=sp.next()) {
      Alignment sa=(Alignment)sp.elem();
      for(BiLink tp=tgtAlignments.first(); !tp.atEnd(); tp=tp.next()) {
        Alignment ta=(Alignment)tp.elem();
        //
        LirNode nd_1=func.newLir.operator(Op.TSTLTU, addrtype, sa.startAddr(), ta.startAddr(), optDefault);
        LirNode nd_2=func.newLir.operator(Op.ADD, addrtype, sa.startAddr(), sa.length(), optDefault);
        LirNode nd_3=func.newLir.operator(Op.TSTLTU, addrtype, ta.startAddr(), nd_2, optDefault);
        LirNode nd_4=func.newLir.operator(Op.BAND, addrtype, nd_1, nd_3, optDefault);
        //
        LirNode nd_5=func.newLir.operator(Op.TSTLTU, addrtype, ta.startAddr(), sa.startAddr(), optDefault);
        LirNode nd_6=func.newLir.operator(Op.ADD, addrtype, ta.startAddr(), ta.length(), optDefault);
        LirNode nd_7=func.newLir.operator(Op.TSTLTU, addrtype, sa.startAddr(), nd_5, optDefault);
        LirNode nd_8=func.newLir.operator(Op.BAND, addrtype, nd_5, nd_7, optDefault);
        //
        LirNode nd_9=func.newLir.operator(Op.BOR, addrtype, nd_4, nd_8, optDefault);
        LirNode nd=func.newLir.operator(Op.TSTEQ, addrtype, nd_9, iconst_1, optDefault);
        codes.add(nd);
      }
    }
    if(codes.length()==0) return null;
    LirNode code=(LirNode)codes.takeFirst();
    if(codes.length()>0) {
      for(BiLink cp=codes.first(); !cp.atEnd(); cp=cp.next()) {
        code=func.newLir.operator(Op.BOR, addrtype, code, (LirNode)cp.elem(), optDefault);
      }
      code=func.newLir.operator(Op.TSTEQ, addrtype, code, iconst_1, optDefault);
    }
    return code;
  }
  private BiList merge() {
    BiList bothaligns=new BiList();
    for(BiLink p=srcAlignments.first(); !p.atEnd(); p=p.next()) {
      bothaligns.add(p.elem());
    }
    for(BiLink p=tgtAlignments.first(); !p.atEnd(); p=p.next()) {
      bothaligns.addNew(p.elem());
    }
    return bothaligns;
  }
  /**
   * Returns srcAlignments
   * @return BiList
   */
  public BiList srcAlignments() {
    return srcAlignments;
  }
  /**
   * Returns tgtAlignments
   * @return BiList
   */
  public BiList tgtAlignments() {
    return tgtAlignments;
  }
  /**
   * Class for alignment info.data
   */
  class Alignment {
    private LirNode startAddr;
    private LirNode length;

    Alignment(LirNode e, int k) {
      if(e.opCode==Op.MEM) {
        startAddr=e.kid(0);
//        Integer i=new Integer(Type.bytes(e.type)*k);
//        length=func.newLir.iconst(addrtype, i.longValue());
        long i=Type.bytes(e.type)*k;
        length=func.newLir.iconst(addrtype, i);
      }
    }
    /**
     * Returns startAddr
     * @return LirNode
     */
    public LirNode startAddr() {
      return startAddr;
    }
    /**
     * Return length
     * @return LirNode
     */
    public LirNode length() {
      return length;
    }
    /**
     * Makes a condition code of checking alignments
     * @return LirNode
     */
    public LirNode toCondition() {
      LirNode e1=func.newLir.operator(Op.MODU, addrtype, startAddr, length, optDefault);
//      LirNode iconst0=func.newLir.iconst(addrtype, 0, optDefault);
//      LirNode cond=func.newLir.operator(Op.TSTEQ, addrtype, e1, iconst0, optDefault);
//      return cond;
      return e1;
    }
  }
}
