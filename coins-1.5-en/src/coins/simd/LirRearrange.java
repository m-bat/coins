/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import coins.backend.Op;
import coins.backend.lir.LirNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * Class for rearrangement
 */
public class LirRearrange {
  private LirDefUseRel duRel;
  private LirOrder order;
  private Vector out;
  /**
   * Constructs an LirRearrange object
   */
  public LirRearrange() {
    duRel=new LirDefUseRel();
    order=new LirOrder();
  }
  /**
   * Rearranges an ordering
   * @param lirs Vector
   * @param rrn ReplaceRegNames
   */
  public Vector invoke(Vector lirs, ReplaceRegNames rrn) {
    out=new Vector();
    try {
      return rearrange(lirs, rrn);
    }
    catch (SimdOptException e) {
      if(e.getMessage().equals("Lir ordering destroyed.")) {
        // Remove "PARALLEL" op.
        Vector newlirs=chkAndMerge(lirs, out);
        // Try again.
        try {
          duRel=new LirDefUseRel();
          order=new LirOrder();
          out=new Vector();
          Vector result=rearrange(newlirs, rrn);
//          System.out.println("The second time, rearrangement succeeeds.");
          return result;
        }
        catch (SimdOptException e2) {
//          System.out.println("SimdOptException : LirRearrange not revenged "
//                              +e2.getMessage());
          return lirs;
        }
      }
//      System.out.println("SimdOptException : LirRearrange "+e.getMessage());
      return lirs;
    }
  }
  private Vector rearrange(Vector lirs, ReplaceRegNames rrn) throws SimdOptException {
    int size=lirs.size();
    int counter=0;
    // Make def-use relation
    duRel.mkDefUseRel(lirs);
    // Add ntmap to def-use relation
    addNtmapToDuRel(rrn);
    // Rearrange lirs with def-use
    LinkedList que=new LinkedList();
    getRoots(que);
    while(que.size()>0) {
      if(counter>2*size) {
//
//        System.out.println("Rearrange's intermediate result:");
//        for(int i=0; i<out.size(); i++) System.out.println(out.elementAt(i));
//        System.out.println("Que's contents:");
//        for(int i=0; i<que.size(); i++) System.out.println(que.get(i));
        throw new SimdOptException("Lir ordering destroyed.");
//
      }
      Object obj=que.getFirst();
      if(obj instanceof LirNode) {
        LirNode e=(LirNode)obj;
        Vector parents=getParents(e);
        if(parents==null || out.containsAll(parents)) {
          out.addElement(e);
          que.removeFirst();
          Vector children=getChildren(e);
          append(que, children);
          counter=0;
        }
        else {
          que.removeFirst();
          Vector children=getChildren(e);
          append(que, children);
          que.addLast(e);
          counter++;
        }
      };
    };
    return out;
  }
//  private void mkDefUseRel(Vector lirs) throws SimdOptException {
//    for(int i=0; i<lirs.size(); i++) {
//      duRel.decompose((LirNode)lirs.elementAt(i));
//    }
//    duRel.build();
//  }
  private void getRoots(List ls) { duRel.getRoots(ls); }
  private Vector getParents(LirNode e) { return duRel.getParents(e); }
  private Vector getChildren(LirNode e) { return duRel.getChildren(e); }
  private void append(List ls, Vector v) {
    if(v==null) return;
    for(int i=0; i<v.size(); i++) {
      if(!(ls.contains(v.elementAt(i)))) ls.add(v.elementAt(i));
    }
  }
  private void addNtmapToDuRel(ReplaceRegNames rrn) throws SimdOptException {
    Collection s=rrn.getNtmap();
    Iterator it=s.iterator();
    while(it.hasNext()) {
      List ls=(List)it.next();
      while(ls.size()>1) {
        LirNode e=(LirNode)ls.get(0);
        ls.remove(0);
        for(int i=0; i<ls.size(); i++) {
          Vector us=duRel.getUseLirs(e);
          Vector ds=duRel.getDefLirs((LirNode)ls.get(i));
          addToRel(us, ds);
        }
      }
    }
  }
  private void addToRel(Vector v1, Vector v2) throws SimdOptException {
    if(v1!=null && v2!=null) {
      for(int i=0; i<v1.size(); i++) {
        for(int j=0; j<v2.size(); j++) {
          LirNode e1=(LirNode)v1.elementAt(i);
          LirNode e2=(LirNode)v2.elementAt(j);
// Begin(2004.9.17)
//          if(e1!=e2) duRel.add(e1, e2);
          if(e1!=e2) {
            duRel.add(e1, e2);
//System.out.println("less:"+e1);
//System.out.println("than:"+e2);
          }
// End(2004.9.17)
        }
      }
    }
  }
  private void mkOrdering(Vector lirs, ReplaceRegNames rrn) throws SimdOptException {
    order.put(duRel.getRelation());
    // Saturate the ordering
    order.saturate();
    // Append original ordering
    //  Not yet
  }
  private Vector chkAndMerge(Vector lirs, Vector res) {
    Vector vtmp=new Vector();
    for(int i=0; i<lirs.size(); i++) {
      LirNode e=(LirNode)lirs.elementAt(i);
      if(!res.contains(e)) {
        if(e.opCode==Op.PARALLEL) {
          for(int j=0; j<e.nKids(); j++) vtmp.addElement(e.kid(j));
        }
        else {
          vtmp.addElement(e);
        }
      }
    }
    for(int i=0; i<res.size(); i++) vtmp.addElement(res.elementAt(i));
    return vtmp;
  }
}
