/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.IoRoot;
import coins.backend.ana.LiveVariableAnalysis;
import coins.backend.ana.LiveVariableSlotwise;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.Function;
import coins.backend.lir.LirLabelRef;
import coins.backend.lir.LirNode;
import coins.backend.Op;
import coins.backend.LocalTransformer;
import coins.backend.Data;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.Module;
import coins.backend.opt.JumpOpt;
import coins.backend.sym.Label;
import coins.backend.SyntaxError;
import coins.backend.TargetMachine;
import coins.backend.Type;
import coins.driver.CompileSpecification;
import coins.ssa.PublicSsa;
import coins.backend.util.ImList;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

/**
 * Class for SIMD optimization part driver
 */
public class SimdDriver implements LocalTransformer{
  public boolean doIt(Data data, ImList args) { return true; }
  public String name() { return "SimdDriver"; }
  public String subject() {
    return "SIMD optimizations";
  }

  public final IoRoot ioRoot;
  public static final int THR=SimdEnvironment.MsgThr;
  private SimdEnvironment env;

//  private boolean alignmentTestOption=true;
  private boolean alignmentTestOption=false;
//  private boolean memAccessOverlappingTestOption=true;
  private boolean memAccessOverlappingTestOption=false;
//  private boolean simdEstimationOption=true;
  private boolean simdEstimationOption=false;
  private BiList copiedBlkInfo;
  private int addrtype;
  private ImList optDefault=ImList.Empty;
  private Vector savedInstrLists;
  private Vector savedLabelList;
  private HashMap labelMap=new HashMap();

  /**
   * Constructs a SimdDriver object
   * @param m Module
   * @param io IoRoot
   * @param coinsSpec CompilerSpecification
   */
  public SimdDriver(Module m,IoRoot io,CompileSpecification coinsSpec) {
    ioRoot=io;
    PrintWriter out=new PrintWriter(io.printOut, true);
    env=new SimdEnvironment(m,coinsSpec,out);
    addrtype=m.targetMachine.typeAddress;
    alignmentTestOption=alignmentTestOption | memAccessOverlappingTestOption;
    savedInstrLists=new Vector();
    savedLabelList=new Vector();
  }

  /**
   * SIMD Optimize
   * @param f Function
   * @param args ImList
   **/
  public boolean doIt(Function f,ImList args) {
// Begin(2005.3.28)
    setOptions();
//System.out.println("alignmentTestOption:"+alignmentTestOption);
//System.out.println("memAccessOverlappingTestOption:"+memAccessOverlappingTestOption);
//System.out.println("simdEstimationOption:"+simdEstimationOption);
// End(2005.3.28)
    saveBasicBlkInfo(f);
    boolean simdrslt=callSimdOpt(f, args);
//System.out.println("After callSimdOpt:");
//f.printIt(env.output);
    if(simdrslt &&
        (getTestOption("alignmentTest") || getTestOption("memAccessOverlappingTest"))) {
      AlignmentTest aligntest=new AlignmentTest(f);
      BiList aligns=aligntest.doIt();
      if(!aligns.isEmpty()) {
        //
        Label simdlabel=genLabel(f, ".simdlabel");
        LirNode simdlabelRef=f.newLir.labelRef(simdlabel);
        Label nosimdlabel=genLabel(f, ".nosimdlabel");
        LirNode nosimdlabelRef=f.newLir.labelRef(nosimdlabel);
        //
        makeSimdBlkList(f, simdlabel);
        //
        insertNosimdBlks(f);
        //
        BasicBlk condBlk=makeCondBlk(f, aligntest, aligns, simdlabelRef, nosimdlabelRef);
        BasicBlk nosimdfstblk=(BasicBlk)f.flowGraph().basicBlkList.first().next().elem();
        LirNode prologue=(LirNode)nosimdfstblk.instrList().takeFirst();
        condBlk.instrList().addFirst(prologue);
        nosimdfstblk.setLabel(nosimdlabel);
        nosimdlabel.setBasicBlk(nosimdfstblk);
//System.out.println("After makeCondBlk:");
//f.printIt(env.output);
        //
        for(BiLink bp=f.flowGraph().basicBlkList.first(); !bp.atEnd(); bp=bp.next()) {
          BasicBlk blk=(BasicBlk)bp.elem();
//System.out.println("savedBasicBlkInfo:");
//blk.printIt(env.output);
          blk.maintEdges();
        }
        f.flowGraph().touch();
        f.flowGraph().dfstOrder();
        f.lirList();
      }
    }
//System.out.println("After SimdDriver:");
//f.printIt(env.output);
    return true;
  }

  private void setOptions() {
    String optstr=env.opt.getArg("simd");
    BiList options=decodeOptions(optstr);
//for(BiLink p=options.first(); !p.atEnd(); p=p.next()) {
//  System.out.println("option:"+(String)p.elem());
//}
    if(containsStr(options, "memAccessOverlappingTestOption"))
      memAccessOverlappingTestOption=true;
    if(containsStr(options, "alignmentTestOption"))
      alignmentTestOption=true;
    if(containsStr(options, "simdEstimationOption"))
      simdEstimationOption=true;
  }
  private boolean containsStr(BiList s, String str) {
    for(BiLink p=s.first(); !p.atEnd(); p=p.next()) {
      if(str.equals((String)p.elem())) return true;
    }
    return false;
  }
  private BiList decodeOptions(String options) {
    final int delimiter='/';
    BiList optionString=new BiList();

    int beginIndex=0;
    int endIndex=0;

    int length=options.length();
    while(endIndex<=length){
      beginIndex=endIndex;
      endIndex=options.indexOf(delimiter,beginIndex);
      if(endIndex==-1)
        endIndex=length;

      if(endIndex>beginIndex){
        String opt=options.substring(beginIndex,endIndex);
        opt=opt.trim();
        optionString.add(opt);
      }
      endIndex++;
    }
    return optionString;
  }
  public boolean callSimdOpt(Function f,ImList args) {
    boolean simdrslt;
    // If convertion (using bit operator)
    IfConvert ifConvert=new IfConvert(env,f);
    ifConvert.invoke();

    // Do jump optimization
    f.apply(JumpOpt.trig);

    // Copy and save Instructions.
    HashMap blkContents=new HashMap();
    for(BiLink p=f.flowGraph().basicBlkList.first(); !p.atEnd(); p=p.next()) {
      BasicBlk blk=(BasicBlk)p.elem();
      BiList instrs=blk.instrList().copy();
      for(BiLink q=instrs.first(); !q.atEnd(); q=q.next()) {
        LirNode ins=(LirNode)q.elem();
        q.setElem(ins.makeCopy(f.newLir));
      }
      blkContents.put(blk, instrs);
    }
    try {
     
    // Translate to SSA form.
    PublicSsa ssa=new PublicSsa(f,ioRoot);
    ssa.translate();

    // Generate DAG
    GenerateDag generateDag=new GenerateDag(env,f);

    // Back translate from SSA form.
    ssa.backTranslate();


//    f.printIt(env.output);

    // Iterate all over the basic blocks in the current control flow graph.
    for(BiLink p=f.flowGraph().basicBlkList.first();!p.atEnd();p=p.next()){
      BasicBlk blk=(BasicBlk)p.elem();

      ReplaceRegNames rrn=new ReplaceRegNames(f);
      rrn.toNewName(blk);

      // Boundanalysis 
      BoundanalysisForLir ba=new BoundanalysisForLir(f);
      ba.invoke(blk);

      // Constant folding
      ConstantFolding constFold=new ConstantFolding(f);
      constFold.invoke(blk);

      if(!blk.instrList().isEmpty()){
        Vector vs=Util.blkToVecs(blk);

        SimdOpt simdOpt=new SimdOpt(f);
        simdOpt.messageFlag=env.shouldDo(THR);
        Vector out=new Vector();
        LiveVariableAnalysis liveAna;
        liveAna=(LiveVariableAnalysis)f.require(LiveVariableSlotwise.analyzer);
        BiList liveOut=liveAna.liveOut(blk);
        Vector los=calcLiveOut(simdOpt,vs, LirUtil.btov(liveOut));

        for(int i=0; i<vs.size(); i++) {
          Vector v=(Vector)vs.elementAt(i);
          if(!chkSimd(v)) {
            out.addElement(v);
            continue;
          }
          liveOut=LirUtil.vtob((Vector)los.elementAt(i));
          out.addElement(simdOpt.invoke(liveOut, v, rrn));
        }
        Util.vecsToBlk(blk, out);
      }
    }
      //
      if(simdEstimationOption) {
        SimdEstimation se=new SimdEstimation(f);
        se.doIt();
      }
      simdrslt=true;
    }
    catch (SimdOptException e) {
      // Recover instructions.
      for(BiLink p=f.flowGraph().basicBlkList.first(); !p.atEnd(); p=p.next()) {
        BasicBlk blk=(BasicBlk)p.elem();
        BiList instrs=(BiList)blkContents.get(blk);
        blk.setInstrList(instrs);
      }
      simdrslt=false;
    }
    // If conversion (using IF operator)
    ifConvert.makeIfNode();

    return(simdrslt);
  }

  private Vector calcLiveOut(SimdOpt so,Vector vs, Vector init) {
    Vector out=new Vector();
    out.setSize(vs.size());
    Vector lo=init;
    for(int i=vs.size(); i>0;) {
      Vector li=so.liveReg((Vector)vs.elementAt(--i), lo);
      lo=(Vector)li.elementAt(0);
      out.setElementAt(lo, i);
    }
    return out;
  }

  private boolean chkSimd(Vector v) {
    if(v.size()>0) {
      LirNode ins=(LirNode)v.elementAt(0);
      if(ins.opCode==Op.SET) return true;
    }
    return false;
  }

  private boolean alignmentTestOption() {
    return alignmentTestOption;
  }
  private void setAlignmentTestOption(boolean b) {
    alignmentTestOption=b;
  }

//
//

  private void saveBasicBlkInfo(Function func) {
    for(BiLink p=func.flowGraph().basicBlkList.first(); !p.atEnd(); p=p.next()) {
      BasicBlk blk=(BasicBlk)p.elem();
      BiList instrs=blk.instrList().copy();
      for(BiLink q=instrs.first(); !q.atEnd(); q=q.next()) {
        LirNode ins=(LirNode)q.elem();
        q.setElem(ins.makeCopy(func.newLir));
      }
      savedInstrLists.add(instrs);
      savedLabelList.add(blk.label());
    }
  }

  LirNode makeAlignmentTestCode(Function func, BiList aligns) {
    BiLink p=aligns.first();
    LirNode code=(LirNode)((AlignmentTest.Alignment)p.elem()).toCondition();
    if(aligns.length()>1) {
      for(p=p.next() ; !p.atEnd(); p=p.next()) {
        AlignmentTest.Alignment a=(AlignmentTest.Alignment)p.elem();
        code=func.newLir.operator(Op.ADD, addrtype, code, a.toCondition(), optDefault);
      }
    }
    LirNode iconst0=func.newLir.iconst(addrtype, 0, optDefault);
    code=func.newLir.operator(Op.TSTNE, addrtype, code, iconst0, optDefault);
    return code;
  }

  private BasicBlk makeCondBlk(Function func, AlignmentTest atest, BiList aligns, LirNode simdlabelRef, LirNode nosimdlabelRef) {
    LirNode cond=makeAlignmentTestCode(func, aligns);
    if(memAccessOverlappingTestOption) {
      cond=func.newLir.operator(Op.TSTEQ, addrtype,
            func.newLir.operator(Op.BOR, addrtype, cond, atest.memAccessOverlappingTest(), optDefault),
            func.newLir.iconst(addrtype, 1),
            optDefault);
    }
    LirNode jumpcode=makeBranch(func, cond, simdlabelRef, nosimdlabelRef);
    BiList instrList=new BiList();
    instrList.add(jumpcode);
    BiLink p=func.flowGraph().basicBlkList.first().next();
    BasicBlk blk=(BasicBlk)p.elem();
    BasicBlk newBlk=func.flowGraph().insertNewBlkBefore(blk);
    BasicBlk entryblk=func.flowGraph().entryBlk();
    newBlk.setInstrList(entryblk.instrList());
    entryblk.setInstrList(instrList);
    return entryblk;
  }

// to LirUtil
  private Label genLabel(Function func, String str) {
    return genLabel(func, str, 0);
  }
  private Label genLabel(Function func, String str, int n) {
    Label label;
    try {
      String strn=Integer.toString(n);
      label=func.internLabel(str+"_"+strn);
    }
    catch (SyntaxError e) {
      label=genLabel(func, str, ++n);
    }
    return label;
  }
//

  LirNode makeBranch(Function func, LirNode cond, LirNode to1, LirNode to2) {
    LirNode[] src=new LirNode[3];
    src[0]=cond; src[1]=to1; src[2]=to2;
    LirNode branch=func.newLir.operator(Op.JUMPC, Type.UNKNOWN, src, optDefault);
    return branch;
  }
  private BiList makeSimdBlkList(Function func, Label simdlabel) {
    String PREFIX=".__simd__";
    BiList simdblks=func.flowGraph().basicBlkList;
    changeLabels(func, simdblks, PREFIX);
    BasicBlk fstblk=(BasicBlk)simdblks.first().elem();
//System.out.println("fstblk:");
//fstblk.printIt(env.output);
    fstblk.instrList().takeFirst();
    fstblk.setLabel(simdlabel);
    simdlabel.setBasicBlk(fstblk);
    return simdblks;
  }

  private void changeLabels(Function func, BiList blks, String str) {
    for(BiLink p=blks.first(); !p.atEnd() ; p=p.next()) {
      BasicBlk blk=(BasicBlk)p.elem();
      Label label=blk.label();
      Label newLabel;
      try {
        newLabel=func.internLabel(str+label.name());
        blk.setLabel(newLabel);
        newLabel.setBasicBlk(blk);
      }
      catch (SyntaxError e) {
        newLabel=label;
      }
//System.out.println("label:  "+label+"    newlabel:  "+newLabel);
      labelMap.put(label, newLabel);
    }
    for(BiLink p=blks.first(); !p.atEnd() ; p=p.next()) {
      for(BiLink ip=((BasicBlk)p.elem()).instrList().first(); !ip.atEnd(); ip=ip.next()) {
        LirNode ins=(LirNode)ip.elem();
        changeLabel(func, ins, labelMap, str);
      }
    }
  }
  private void changeLabel(Function func, LirNode e, HashMap labelMap, String str) {
    for(int i=0; i<e.nKids(); i++) {
      LirNode kid=e.kid(i);
      if(kid instanceof LirLabelRef) {
        Label label=((LirLabelRef)kid).label;
        Label newLabel=(Label)labelMap.get(kid);
        if(newLabel==null) {
          try {
            newLabel=func.internLabel(str+label.name());
          }
          catch (SyntaxError err) {
            newLabel=label;
          }
        }
        e.setKid(i, func.newLir.labelRef(newLabel));
      }
      else {
        changeLabel(func, kid, labelMap, str);
      }
    }
  }
  private void insertNosimdBlks(Function func) {
    BiLink bp=func.flowGraph().basicBlkList.first().next();
    BasicBlk blk=(BasicBlk)bp.elem();
    for(int i=0; i<savedInstrLists.size()-1; i++) {
      blk=func.flowGraph().insertNewBlkBefore(blk);
    }
    //
    BasicBlk exitblk=func.flowGraph().exitBlk();
    Label exitlabel=exitblk.label();
    Label nosimdexitlabel=(Label)savedLabelList.lastElement();
    for(int i=0; i<savedInstrLists.size()-1; i++) {
      BiList instrList=(BiList)savedInstrLists.elementAt(i);
      for(BiLink insp=instrList.first(); !insp.atEnd(); insp=insp.next()) {
        LirNode ins=(LirNode)insp.elem();
        if(ins.isBranch()) {
          ins.replaceLabel(nosimdexitlabel, exitlabel, func.newLir);
        }
      }
    }
    //
    BiLink p=func.flowGraph().basicBlkList.first();
    BasicBlk simdfstblk=(BasicBlk)p.elem();
    BiList instrList_0=simdfstblk.instrList();
    Label label_0=simdfstblk.label();
    for(int i=0; i<savedInstrLists.size()-1; i++) {
      BasicBlk b=(BasicBlk)p.elem();
      BiList instrList=(BiList)savedInstrLists.elementAt(i);
      b.setInstrList(instrList);
      Label label=(Label)savedLabelList.elementAt(i);
      if(label!=null) {
        b.setLabel(label);
        label.clear();
        label.setBasicBlk(b);
      }
      p=p.next();
    }
    simdfstblk=(BasicBlk)p.elem();
    simdfstblk.setInstrList(instrList_0);
    simdfstblk.setLabel(label_0);
  }
  private boolean getTestOption(String str) {
    if(str=="alignmentTest") {
      return alignmentTestOption;
    }
    if(str=="memAccessOverlappingTest") {
      return memAccessOverlappingTestOption;
    }
    return false;
  }
}
