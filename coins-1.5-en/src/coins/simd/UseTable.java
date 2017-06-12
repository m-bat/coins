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
 * Class for a use table
 */
public class UseTable {
  private Relation useTable;
  /**
   * Constructs a UseTable object
   */
  UseTable() {
    useTable=new Relation();
  }
  /**
   * Adds use elements of a given L-expression to a use table
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
              LirNode[] referents=LirUtil.pickupReferent(ekid.kid(1));
              for(int j=0; j<referents.length; j++) {
                useTable.add(referents[j], e);
// for debug
//System.out.println("useTable.add:");
//System.out.println("  referent="+referents[j]);
//System.out.println("  e="+e);
//
              }
              if(ekid.kid(0).opCode==Op.MEM) {
                LirNode[] mreferents=LirUtil.pickupUseReferent(ekid.kid(0));
                for(int j=0; j<mreferents.length; j++) {
                  useTable.add(mreferents[j], e);
// for debug
//System.out.println("useTable.add:");
//System.out.println("  referent="+referents[j]);
//System.out.println("  e="+e);
//
                }
              }
            }
          }
          break;
        }
      case Op.SET:
        {
          LirNode[] referents=LirUtil.pickupReferent(e.kid(1));
          for(int i=0; i<referents.length; i++) {
            useTable.add(referents[i], e);
// for debug
//System.out.println("useTable.add:");
//System.out.println("  referent="+referents[i]);
//System.out.println("  e="+e);
//
          }
          if(e.kid(0).opCode==Op.MEM) {
            LirNode[] mreferents=LirUtil.pickupUseReferent(e.kid(0));
            for(int i=0; i<mreferents.length; i++) {
              useTable.add(mreferents[i], e);
// for debug
//System.out.println("useTable.add:");
//System.out.println("  mreferent="+mreferents[i]);
//System.out.println("  e="+e);
//
            }
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
        {
          LirNode[] referents=LirUtil.pickupReferent(e);
          for(int i=0; i<referents.length; i++) useTable.add(referents[i], e);
          break;
        }
    }
  }
  /**
   * Retrieves 
   * @param e LirNode
   * @return Vector
   */
  public Vector getLirs(LirNode e) {
    return useTable.getRelated(e);
  }
  public LirNode getReferent(LirNode e) throws SimdOptException {
    Vector referent=useTable.getRevRelated(e);
    if(referent.size()!=1) throw new SimdOptException("Multiple referents");
    return (LirNode)referent.elementAt(0);
  }
  public Enumeration getAllReferent() {
    return useTable.dom();
  }
}
