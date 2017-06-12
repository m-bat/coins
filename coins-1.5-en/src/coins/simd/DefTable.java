/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.Enumeration;
import java.util.Vector;
import coins.backend.Op;
import coins.backend.lir.LirNode;

/**
 * Class for DefTable
 */
public class DefTable {
  private Relation defTable;
  private Vector singletons;
  /**
   * Constructs a DefTable object
   */
  DefTable() {
    defTable=new Relation();
    singletons=new Vector();
  }
  /**
   * Adds referents and referer of a given LirNode
   * @param e LirNode
   */
  public void add(LirNode e) throws SimdOptException {
    switch(e.opCode) {
      case Op.PARALLEL:
        {
          // We assume all components are SET exps.
          for(int i=0; i<e.nKids(); i++) {
            LirNode ekid=e.kid(i);
            if(ekid.opCode==Op.SET) {
              LirNode[] referents=LirUtil.pickupDefReferent(ekid.kid(0));
              for(int j=0; j<referents.length; j++) {
                defTable.add(referents[j], e);
// for debug
//System.out.println("defTable.add:");
//System.out.println("  referent="+referents[j]);
//System.out.println("  e="+e);
//
              }
            }
          }
          for(int i=0; i<e.nKids(); i++) {
            LirNode ekid=e.kid(i);
            if(ekid.opCode==Op.SET) {
              LirNode[] referents=LirUtil.pickupReferent(ekid);
              if(referents!=null && referents.length!=0) return;
            }
          }
          singletons.addElement(e);
// for debug
//System.out.println("singleton="+e);
//
          break;
        }
      case Op.SET:
        {
          LirNode[] referents=LirUtil.pickupDefReferent(e.kid(0));
          for(int i=0; i<referents.length; i++) {
            defTable.add(referents[i], e);
// for debug
//System.out.println("defTable.add:");
//System.out.println("  referent="+referents[i]);
//System.out.println("  e="+e);
//
          }
          LirNode[] refereds=LirUtil.pickupReferent(e);
          if(refereds==null || refereds.length==0) {
            singletons.addElement(e);
// for debug
//System.out.println("singleton="+e);
//
          }
          break;
        }
      case Op.LABEL:
      case Op.PURE:
      case Op.DEFLABEL:
      case Op.PROLOGUE:
      case Op.EPILOGUE:
      case Op.USE:
      case Op.CLOBBER:
      case Op.PHI:
      case Op.LIST:
      case Op.UNDEFINED:
      case Op.SPACE:
      case Op.ZEROS:
        break;
      default:
        break;
    }
  }
  /**
   * Retrieves a referer node of LirNode e in defTable
   * @param e LirNode
   * @return Vector
   */
  public Vector getLirs(LirNode e) {
    return (Vector)defTable.getRelated(e);
  }
  /**
   * Retrieves a referent of LirNode e in defTable
   * @param e LirNode
   * @return LirNode
   */
  public LirNode getReferent(LirNode e) throws SimdOptException {
    Vector referent=(Vector)defTable.getRevRelated(e);
    if(referent.size()!=1) throw new SimdOptException("Multiple referents");
    return (LirNode)referent.elementAt(0);
  }
  /**
   * Returns defTable's domain
   * @return Enumeration
   */
  public Enumeration getAllReferent() { return defTable.dom(); }
  /**
   * Returns the value of singletons
   * @return Vector
   */
  public Vector getSingletons() { return singletons; }
}
