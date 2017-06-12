/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package  coins.simd;

import  coins.backend.Function;
import  coins.backend.Op;
import  coins.backend.Type;
import  coins.backend.cfg.BasicBlk;
import  coins.backend.lir.LirIconst;
import  coins.backend.lir.LirNode;
import  coins.backend.util.BiLink;
import  coins.util.IntBound;
import  coins.util.IntConst;
import  coins.util.IntLive;
//import  coins.simd.ba.BoundToLivebits;
import  java.util.Enumeration;
import  java.util.Hashtable;

/**
 * Class for the downward bound analysis.
 */
public class BoundanalysisDwForLir {
  private Function function;
  private BoundanalysisUwForLir bauw;
  private Hashtable boundTableDW;
  BoundanalysisDwForLir(Function f) {
    function=f;
    boundTableDW=new Hashtable();
  }
/**
 * Constructs a downward bound analyser.
 */
  public void boundanalysisDw(LirNode ins,BoundanalysisUwForLir bduw) {
    if(ins.opCode==Op.SET) {
        int size=Type.bits(ins.type);
        inherit(ins.kid(1),IntLive.valueOf(size),boundTableDW,bduw);
    }
  }
/**
 * Calculates live bits of each node.
 * @param  e  LirNode
 * @param  l  IntLive
 * @param  tbldw  Hashtable
 * @param  bduw  BoundanalysisUwForLir
 */
  private void inherit(LirNode e,IntLive l,Hashtable tbldw,BoundanalysisUwForLir bduw) {
    switch(e.opCode) {
      case Op.INTCONST:
        {
          return;
        }
//      case Op.FLOATCONST:
      case Op.STATIC:
      case Op.FRAME:
      case Op.REG:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          return;
        }
//      case Op.SUBREG:
//      case Op.LABEL:
//      case Op.DEFLABEL:
      case Op.NEG:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          inherit(e.kid(0),l.inheritNeg(),tbldw,bduw);
          return;
        }
      case Op.BNOT:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          inherit(e.kid(0),l.inheritBnot(),tbldw,bduw);
          return;
        }
      case Op.CONVSX:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          inherit(e.kid(0),l.inheritConvsx(Type.bits(e.kid(0).type)),tbldw,bduw);
          return;
        }
      case Op.CONVZX:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          inherit(e.kid(0),l.inheritConvzx(Type.bits(e.kid(0).type)),tbldw,bduw);
          return;
        }
      case Op.CONVIT:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          inherit(e.kid(0),l.inheritConvit(Type.bits(e.kid(0).type)),tbldw,bduw);
          return;
        }
//      case Op.CONVFX:
//      case Op.CONVFT:
//      case Op.CONVFI:
//      case Op.CONVSF:
//      case Op.CONVUF:
      case Op.MEM:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          int size=Type.bits(e.kid(0).type);
          inherit(e.kid(0),IntLive.valueOf(size),tbldw,bduw);
          return;
        }
//      case Op.JUMP:
//      case Op.USE:
//      case Op.CLOBBER:
      case Op.ADD:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritAdd(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritAdd(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritAdd(),tbldw,bduw);
            inherit(e.kid(1),l.inheritAdd(),tbldw,bduw);
          }
          return;
        }
      case Op.SUB:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritSub0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritSub1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritSub0(),tbldw,bduw);
            inherit(e.kid(1),l.inheritSub1(),tbldw,bduw);
          }
          return;
        }
      case Op.MUL:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritMul(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritMul(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritMul(),tbldw,bduw);
            inherit(e.kid(1),l.inheritMul(),tbldw,bduw);
          }
          return;
        }
      case Op.DIVS:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritDivs0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritDivs1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritDivs0(),tbldw,bduw);
            inherit(e.kid(1),l.inheritDivs1(),tbldw,bduw);
          }
          return;
        }
      case Op.DIVU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritDivu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritDivu1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritDivu0(),tbldw,bduw);
            inherit(e.kid(1),l.inheritDivu1(),tbldw,bduw);
          }
          return;
        }
      case Op.MODS:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritMods0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritMods1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritMods0(),tbldw,bduw);
            inherit(e.kid(1),l.inheritMods1(),tbldw,bduw);
          }
          return;
        }
      case Op.MODU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritModu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritModu1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritModu0(),tbldw,bduw);
            inherit(e.kid(1),l.inheritModu1(),tbldw,bduw);
          }
          return;
        }
      case Op.BAND:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritBand(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritBand(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritBand(),tbldw,bduw);
            inherit(e.kid(1),l.inheritBand(),tbldw,bduw);
          }
          return;
        }
      case Op.BOR:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritBor(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritBor(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritBor(),tbldw,bduw);
            inherit(e.kid(1),l.inheritBor(),tbldw,bduw);
          }
          return;
        }
      case Op.BXOR:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritBxor(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritBxor(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritBxor(),tbldw,bduw);
            inherit(e.kid(1),l.inheritBxor(),tbldw,bduw);
          }
          return;
        }
      case Op.LSHS:
      case Op.LSHU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritLsh0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            int t=Type.bits(e.type);
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritLsh1(cnvLirToIntConst(c),t),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritLsh0(),tbldw,bduw);
            int t=Type.bits(e.type);
            inherit(e.kid(1),l.inheritLsh1(t),tbldw,bduw);
          }
          return;
        }
      case Op.RSHS:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritRshs0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            int t=Type.bits(e.type);
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritRshs1(cnvLirToIntConst(c),t),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritRshs0(),tbldw,bduw);
            int t=Type.bits(e.type);
            inherit(e.kid(1),l.inheritRshs1(t),tbldw,bduw);
          }
          return;
        }
      case Op.RSHU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritRshu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            int t=Type.bits(e.type);
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritRshu1(cnvLirToIntConst(c),t),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritRshu0(),tbldw,bduw);
            int t=Type.bits(e.type);
            inherit(e.kid(1),l.inheritRshu1(t),tbldw,bduw);
          }
          return;
        }
      case Op.TSTEQ:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTsteq(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTsteq(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTsteq(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTsteq(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTNE:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstne(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstne(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstne(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstne(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTLTS:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstne(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstne(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstne(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstne(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTLES:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstles0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstles1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstles0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstles1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTGTS:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstgts0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstgts1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstgts0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstgts1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTGES:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstges0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstges1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstges0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstges1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTLTU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstltu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstltu1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstltu0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstltu1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTLEU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstleu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstleu1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstleu0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstleu1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTGTU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstgtu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstgtu1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstgtu0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstgtu1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
      case Op.TSTGEU:
        {
          IntBound bd=(IntBound)bduw.get(e);
          IntLive lv=bdToLive(bd);
          tbldw.put(e,l.intersection(lv));
          if(e.kid(1) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(1);
            inherit(e.kid(0),l.inheritTstgeu0(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else if(e.kid(0) instanceof LirIconst) {
            LirIconst c=(LirIconst)e.kid(0);
            inherit(e.kid(1),l.inheritTstgeu1(cnvLirToIntConst(c)),tbldw,bduw);
          }
          else {
            inherit(e.kid(0),l.inheritTstgeu0(Type.bits(e.kid(0).type)),tbldw,bduw);
            inherit(e.kid(1),l.inheritTstgeu1(Type.bits(e.kid(1).type)),tbldw,bduw);
          }
          return;
        }
//      case Op.SET:
//      case Op.JUMPC:
//      case Op.CALL:
//      case Op.JUMPN:
//      case Op.PROLOGUE:
//      case Op.EPILOGUE:
//      case Op.PARALLEL:
      default:
        return;
    }
  }
  private IntLive bdToLive(IntBound bd) {
    return BoundToLivebits.convert(bd);
  }
  private IntConst cnvLirToIntConst(LirIconst i) {
    return IntConst.valueOf(Type.bits(i.type),i.value);
  }
/**
 * Prints out nodes and their bounds.
 */
  public void show() {
    if(boundTableDW!=null) {
      Enumeration keys=boundTableDW.keys();
      while(keys.hasMoreElements()) {
        LirNode nd=(LirNode)keys.nextElement();
        System.out.println("KEY:"+nd.toString());
        IntLive lv=(IntLive)boundTableDW.get(nd);
        System.out.println("BOUND:"+lv.toString());
      }
    }
  }
/**
 * Retrieves live bits of a node.
 * @param  e  LirNode
 */
  public IntLive get(LirNode e) {
    return (IntLive)boundTableDW.get(e);
  }
/**
 * Stores live bits of a node.
 * @param  e  LirNode
 * @param  b  LirNode
 */
  public void put(LirNode e,IntLive b) {
    boundTableDW.put(e,b);
  }
}
