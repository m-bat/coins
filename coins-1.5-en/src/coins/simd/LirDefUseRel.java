/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import coins.backend.Op;
import coins.backend.lir.LirNode;

/**
 * Class for a def-use relation
 */
public class LirDefUseRel {
  private DefTable defTable;
  private UseTable useTable;
  private Relation defuseRel;
  /**
   * Constructs an LirDefUseRel object
   */
  public LirDefUseRel() {
    defTable=new DefTable();
    useTable=new UseTable();
  }
  /**
   * Makes a def-use relation from L-expressions
   * @param lirs Vector
   */
  public void mkDefUseRel(Vector lirs) throws SimdOptException {
    for(int i=0; i<lirs.size(); i++) {
      decompose((LirNode)lirs.elementAt(i));
    }
    build(lirs);
  }
  /**
   * Returns a def-use relation
   * @return Relation
   */
  public Relation getRelation() {
    return defuseRel;
  }
  /**
   * Adds elements to a def-use table
   * @param e1 LirNode
   * @param e2 LirNode
   */
  public void add(LirNode e1, LirNode e2) throws SimdOptException {
    defuseRel.add(e1, e2);
  }
  /**
   * Decomposes a LirNode and adds to defTable, useTable
   * @param e LirNode
   */
  public void decompose(LirNode e) throws SimdOptException {
    defTable.add(e);
    useTable.add(e);
  }
  /**
   * Builds a def-use relation from L-expressions
   * @param lirs Vector
   */
  public void build(Vector lirs) throws SimdOptException {
    defuseRel=new Relation();
    mkDefUseForMem(lirs);
    Enumeration keys=defTable.getAllReferent();
    while(keys.hasMoreElements()) {
      LirNode k=(LirNode)keys.nextElement();
      if(k.opCode==Op.MEM) continue;
      Vector refered=defTable.getLirs(k);
      Vector refs=useTable.getLirs(k);
      defuseRel.append(refered, refs);
    }
  }
  /**
   * Retrieves L-expressions related to a given L-expression in defTable
   * @param e LirNode
   * @return Vector
   */
  public Vector getDefLirs(LirNode e) {
    if(e.opCode==Op.REG) return defTable.getLirs(e);
    return null;
  }
  /**
   * Retrieves L-expressions related to a given L-expression in useTable
   * @param e LirNode
   * @return Vector
   */
  public Vector getUseLirs(LirNode e) {
    if(e.opCode==Op.REG) return useTable.getLirs(e);
    return null;
  }
  // {x|Rel(x,e)}
  /**
   * Retrieves a L-expression related to given L-expression in use-def relation
   * @param e LirNode
   * @return Vector
   */
  public Vector getParents(LirNode e) { return defuseRel.getRevRelated(e); }
  // {x|Rel(e,x)}
  /**
   * Retrieves a L-expression related to given L-expression in def-userelation
   * @param e LirNode
   * @return Vector
   */
  public Vector getChildren(LirNode e) { return defuseRel.getRelated(e); }
  /**
   * Retrieves L-expressions which is not related to any L-expression in def-use relation
   * @param ls List
   */
  public void getRoots(List ls) {
    Vector singletons=defTable.getSingletons();
    for(int i=0; i<singletons.size(); i++) ls.add(singletons.elementAt(i));
    Enumeration dr=defTable.getAllReferent();
    while(dr.hasMoreElements()) {
      Object obj=dr.nextElement();
      if(obj instanceof LirNode) {
        LirNode referent=(LirNode)obj;
        Vector v=defTable.getLirs(referent);
        for(int i=0; i<v.size(); i++) {
          LirNode e=(LirNode)v.elementAt(i);
          if(isRoot(e) && !(ls.contains(e))) ls.add(e);
        }
      }
    }
  }
  private boolean isRoot(LirNode e) {
    Vector defs=defuseRel.getRevRelated(e);
    return (defs==null || defs.size()==0);
  }
  private void mkDefUseForMem(Vector lirs) throws SimdOptException {
    for(int i=0; i<lirs.size(); i++) {
      LirNode inst=(LirNode)lirs.elementAt(i);
      if(!LirMemUtil.hasMemDef(inst)) continue;
      LirNode mem=LirMemUtil.getMemName(inst);
      if(mem==null) continue;  // Illegal !
      Vector use=new Vector();
      LirMemUtil.findMemUse(lirs, i+1, mem, use);
      if(use.size()==0) continue;
      Vector def=new Vector();
      def.addElement(inst);
      defuseRel.append(def, use);
    }
  }
}
