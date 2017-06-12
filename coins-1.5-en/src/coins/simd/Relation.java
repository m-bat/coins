/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Relation {
  private Hashtable relation;
  private Hashtable revrelation;
  private int flag;
  Relation() {
    relation=new Hashtable();
    revrelation=new Hashtable();
  }
  public void add(Object o1, Object o2) throws SimdOptException {
    add1(relation, o1, o2);
    add1(revrelation, o2, o1);
  }
  private void add1(Hashtable tbl, Object o1, Object o2) throws SimdOptException {
// for debug
//if(o1==o2) {
//System.out.println("saturate:");
//printRel();
//}
//
    if(o1==o2)
      throw new SimdOptException(o1.toString()+"Reflective relation is unexpected");
    Vector objs=(Vector)tbl.get(o1);
    if(objs==null) {
      objs=new Vector();
// for debug
//if(tbl==relation) {
//System.out.println("add:");
//System.out.println("  "+o1);
//System.out.println("  "+o2);
//}
//
      objs.addElement(o2);
      tbl.put(o1, objs);
      flag=1;
    }
    else {
      if(!objs.contains(o2)) {
// for debug
//if(tbl==relation) {
//System.out.println("add:");
//System.out.println("  "+o1);
//System.out.println("  "+o2);
//}
//
        objs.addElement(o2);
        flag=1;
      }
    }
  }
  public void append(Vector o1, Vector o2) throws SimdOptException {
    if((o1==null) || (o2==null)) return;
    for(int i=0; i<o1.size(); i++) {
      if(o1.elementAt(i)==null) continue;
      for(int j=0; j<o2.size(); j++) {
        add(o1.elementAt(i), o2.elementAt(j));
// for debug
//System.out.println("Relation.add:");
//System.out.println("  fst="+o1.elementAt(i));
//System.out.println("  snd="+o2.elementAt(j));
//
      }
    }
  }
  public boolean isRelated(Object o1, Object o2) {
    Vector objs=(Vector)relation.get(o1);
    if(objs==null) return false;
    return objs.contains(o2);
  }
  public Vector getRelated(Object o) { return (Vector)relation.get(o); }
  public Vector getRevRelated(Object o) { return (Vector)revrelation.get(o); }
  public void saturate() throws SimdOptException {
// for debug
//System.out.println("saturate:");
//printRel();
//
    flag=1;
    while(flag>0) {
      flag=0;
      Enumeration keys=relation.keys();
      while(keys.hasMoreElements()) {
        Object k=keys.nextElement();
        Vector v=(Vector)relation.get(k);
        if(v==null) continue;
        for(int i=0; i<v.size(); i++) {
          Vector w=(Vector)relation.get(v.elementAt(i));
          if(w==null) continue;
          for(int j=0; j<w.size(); j++) add(k, w.elementAt(j));
        }
      }
    }
  }
  public Enumeration dom() { return relation.keys(); }
  public Enumeration rng() { return revrelation.keys(); }
  public void printRel() {
    Enumeration keys=relation.keys();
    while(keys.hasMoreElements()) {
      Object k=keys.nextElement();
      System.out.println(k.toString()+" <");
      Vector v=(Vector)relation.get(k);
      if(v==null) continue;
      for(int i=0; i<v.size(); i++) {
        System.out.println("  <<"+v.elementAt(i).toString());
      }
    }
  }
}
