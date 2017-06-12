/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import  coins.util.IntBound;
import  coins.util.IntConst;
import  coins.util.IntLive;

/**
 * Class for converting bounds into live bits.
 */
public final class BoundToLivebits {
  private static final IntBound posdom8=mkDom(8,1);
  private static final IntBound negdom8=mkDom(8,-1);
  private static final IntBound posdom16=mkDom(16,1);
  private static final IntBound negdom16=mkDom(16,-1);
  private static final IntBound posdom32=mkDom(32,1);
  private static final IntBound negdom32=mkDom(32,-1);
  private static final IntBound posdom64=mkDom(64,1);
  private static final IntBound negdom64=mkDom(64,-1);
  private static final IntBound[] posdoms={posdom8,posdom16,null,posdom32,null,null,null,posdom64};
  private static final IntBound[] negdoms={negdom8,negdom16,null,negdom32,null,null,null,negdom64};

  private static IntBound mkDom(int t,int s) {
    IntConst lo,up;
    IntConst m=IntConst.valueOf(t,1);
    if(s>0) {
      lo=IntConst.valueOf(t,0);
      up=m.lsh(t-1).sub(m);
      return new IntBound(lo,up);
    }
    if(s<0) {
      lo=m.lsh(t-1);
      up=m.lsh(t).sub(m);
      return new IntBound(lo,up);
    }
    throw new IllegalArgumentException("mkDcom:"+String.valueOf(s));
  }

/**
 * Converts a bound to live bits.
 * @param  bd  IntBound
 */
  public static IntLive convert(IntBound bd) {
    IntConst lo=bd.lower;
    IntConst up=bd.upper;
    int size=bd.size();
    IntBound posdom=posdoms[size/8-1];
    IntBound negdom=negdoms[size/8-1];
//System.out.println("posdom:"+posdom.toString());
//System.out.println("negdom:"+negdom.toString());
    if(posdom.contains(lo)) {
      if(posdom.contains(up)) {
        if(lo.compareTo(up)<=0) {
//System.out.println("pos-pos-le");
          return IntLive.valueOf(cover(up));
        }
//System.out.println("pos-pos-gt");
        return IntLive.valueOf(alldom(size));
      }
//System.out.println("pos-neg");
      return IntLive.valueOf(alldom(size));
    }
    if(posdom.contains(up)) {
      IntLive b1=IntLive.valueOf(cover(up));
      IntLive b2=b1.union(IntLive.valueOf(cover(lo.bnot())));
//System.out.println("neg-pos");
      return b2.union(IntLive.valueOf(b2.intConstValue().lsh(1)));
    }
    if(lo.compareTo(up)<=0) {
//System.out.println("neg-neg-le");
      return IntLive.valueOf(cover(lo.bnot()));
    }
//System.out.println("other");
    return IntLive.valueOf(alldom(size));
  }

/**
 * Makes a cover.
 * @param  c  IntConst
 */
  public static IntConst cover(IntConst c) {
//    int size=c.size;
    int size=c.size();
    IntConst d=c;
    for(int i=1;i<=size;i=i*2) d=d.bor(d.rshu(i));
    return d;
  }

  private static IntConst alldom(int t) {
    IntConst lo=IntConst.valueOf(t,0);
    return lo.bnot();
  }
}
