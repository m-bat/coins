/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package  coins.simd;

import  coins.backend.cfg.BasicBlk;
import  coins.backend.Function;
import  coins.backend.lir.LirNode;
import  coins.backend.Op;
import  coins.backend.Type;
import  coins.backend.util.BiLink;
import  coins.backend.util.BiList;
import  coins.util.IntBound;
import  coins.util.IntLive;

/**
 * Class for the bound analysis.
 */
public class BoundanalysisForLir {
/**
 * Upward boundanalyser.
 */
  public BoundanalysisUwForLir bauw;
/*
 */
  private Function func;
/**
 * Downward boundanalyser.
 */
  public BoundanalysisDwForLir badw;
/**
 * Constructs a bound analyser.
 * @param  f  Function
 */
  BoundanalysisForLir(Function f) {
    func=f;
    bauw=new BoundanalysisUwForLir(f);
    badw=new BoundanalysisDwForLir(f);
  }
/**
 * Calculates downward bounds and upward bounds(live bits) of a node.
 * @param  ins  LirNode
 */
  public void boundanalysis(LirNode ins) {
    bauw.boundanalysisUw(ins);
//System.out.println("Result of upward boundanalysis:");
//bauw.show();
    badw.boundanalysisDw(ins,bauw);
//System.out.println("Result of downward boundanalysis:");
//badw.show();
  }
/**
 */
  public void invoke(BasicBlk blk) {
    if(!blk.instrList().isEmpty()) {
      for(BiLink lir=blk.instrList().first();!lir.atEnd();lir=lir.next()) {
        LirNode ins=(LirNode)lir.elem();
        if(ins.opCode==Op.SET && Type.tag(ins.type)==Type.INT) {
//System.out.println("Before:"+ins.toString());
          boundanalysis(ins);
          SizeConv sc=new SizeConv(func,badw);
          LirNode insNew=sc.convert(ins);
//System.out.println("After:"+insNew.toString());
          if(insNew!=ins) lir.setElem(insNew);
        }
      }
    }
  }
}
