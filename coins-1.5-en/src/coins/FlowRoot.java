/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// FlowRoot.java
//  Delete flow and make link to aflow. 040619 //##41

package coins;

import coins.aflow.SubpFlow; //##41
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;
import coins.sym.Subp;
import coins.sym.FlowAnalSym; //##94

/**
<PRE>
/** FlowRoot class is used to access Flow information and
 *  information prepared by other classes such as Sym, HIR, etc.
 *  All Flow objects contain a reference to the FlowRoot object
 *  from which intermediate representation information and methods
 *  can be quickly accessed.
 *  FlowRoot contains references of IoRoot, SymRoot, HirRoot, etc.
 *  Thus, every Flow objects can access information and methods of
 *  SymRoot, Sym, HirRoot, HIR, LirRoot, and IoRoot.
</PRE>
**/
public class
FlowRoot
{

//==== Public fields ====//

  /** ioRoot records the reference to the IoRoot object
   *  which is got from HirRoot or LirRoot object passed as
   *  a parameter of FlowRoot constructor.
   *  It is used in accessing IoRoot information and
   *  invoking IoRoot methods.
  **/
  public final IoRoot
    ioRoot;

  /** symRoot records the reference to the SymRoot object
   *  got from HirRoot or LirRoot passed as a parameter of
   *  costructors for HirRoot or LirRoot.
   *  It is used in accessing information and methods of
   *  SymRoot and Sym.
  **/
  public final SymRoot
    symRoot;

  /** HirRoot records the reference to the HirRoot object
   *  passed as a parameter of costructors for FlowRoot.
   *  It is used in accessing information and methods of
   *  HIR.
  **/
  public final HirRoot
    hirRoot;

  /** Record the FlowRoot instance so that it can be refered from others.
  **/
  public final FlowRoot
    flowRoot;

  /** aflow: Flow instance used to invoke Flow methods.
  **/
  public final coins.aflow.Flow aflow;
  public final coins.flow.Flow flow; //##60
  public coins.flow.ControlFlow controlFlow; //##60
  public coins.flow.DataFlow dataFlow; //##60

  /** subpFlow: SubpFlow instance that is currently active.
  **/
  public coins.aflow.SubpFlow //##41
    subpFlow = null;          //##41

//##60 BEGIN
  public coins.flow.SubpFlow // SubpFlow under analysis.
    fSubpFlow = null;        // This is set by coins.flow.SubpFlowImpl.
                             // (Unnecessary to be set by others)
//##60 END

  /** subpUnderAnalysis: Subprogram under flow analysis.
  **/
  public Subp
    subpUnderAnalysis = null;

//==== Protected/private fields ====//

  protected boolean
    fHirAnalysis = true;   // true  if HIR is under analysis,
                           // false if LIR is under analysis.

  protected FlagBox
    fFlowAnalOption;

  protected FlowRoot
    fInheritedFlowRoot = null;

//==== Constructors ====//

/** Constructor for HIR flow analysis.
 *  @param pHirRoot: Reference of HirRoot to be shared
 *      beteen all objects of Flow.
**/
public
FlowRoot( HirRoot pHirRoot )
{
  hirRoot  = pHirRoot;
  ioRoot   = pHirRoot.ioRoot;
  symRoot  = pHirRoot.symRoot;
  flowRoot = this;

  aflow = (coins.aflow.Flow)(new coins.aflow.Flow(this));
  flow = (coins.flow.Flow)(new coins.flow.FlowImpl(this)); //##60

  fHirAnalysis = true;
  fFlowAnalOption = (FlagBox)(new FlagBoxImpl());
  symRoot.attachFlowRoot(this);
  hirRoot.attachFlowRoot(this);
} // FlowRoot for HIR


//====== Methods to control data flow analysis ======//

public boolean
getFlowAnalOption( int pOptionId )
{
  return fFlowAnalOption.getFlag(pOptionId);
}

public void
setFlowAnalOption( int pOptionId, boolean pYesNo )
{
  if (pYesNo) { // When true, set option and its prerequisites to true.
    for (int i = 1; i < OPTION_COUNT; i++) {
      if (OPTION_MATRIX[pOptionId][i] == 1)
//         fFlowAnalOption.setFlag(pOptionId, true);
         fFlowAnalOption.setFlag(i, true);  // 2002/12/22 HASEGAWA
    }
  }else  // When false, set the specified option to false.
    fFlowAnalOption.setFlag(pOptionId, false);
} // setFlowAnalOption

public void
resetAllFlowAnalOptions()
{
  for (int i = 1; i < OPTION_COUNT; i++)
    fFlowAnalOption.setFlag(i, false);
}

//##94 BEGIN
/**
 * Reset Flow information link of all FlowAnalSyms
 * recorded in coins.flow.SubpFlow
 * and coins.aflow.SubpFlowImpl
 * if there are such links.
 * This method is called when instantiationg
 * FlowRoot,
 * coins.flow.Flow, coins.flow.HirSubpFlowImpl,
 * coins.aflow.Flow, coins.aflow.HirSubpFlowImpl
 * in order to reset previous flow sym links.
 */
public void
resetAllFlowAnalSymLinks()
{
  ioRoot.dbgFlow.print(1, "\nresetAllFlowAnalSymLink ");
  if (fSubpFlow != null) {
    fSubpFlow.resetFlowSymLinkForRecordedSym();
    fSubpFlow.resetGlobalFlowSymLink();
  }
  if (subpFlow != null) {
    if (ioRoot.dbgFlow.getLevel() > 1) {
      java.util.Set lSetOfFlowAnalSyms =
        ((coins.aflow.SubpFlowImpl)subpFlow).fSetOfFlowAnalSyms;
      ioRoot.dbgFlow.print(1,
        "\nresetFlowAnalSymLink of aflow fSetOfFlowAnalSyms "
        + lSetOfFlowAnalSyms);
     if (lSetOfFlowAnalSyms != null) {
       for (java.util.Iterator lIt = lSetOfFlowAnalSyms.iterator();
            lIt.hasNext(); ) {
         FlowAnalSym lSym = (FlowAnalSym)lIt.next();
         lSym.resetFlowAnalInf();
       }
     }
    }
  }
} // resetAllFlowAnalSymLinks
//##94 END

/** See which of HIR and LIR is currently analyzed.
 *  @return true if HIR is currently analyzed, false otherwize.
**/
public boolean
isHirAnalysis()
{
  return fHirAnalysis;
}

/** See which of HIR and LIR is currently analyzed.
 *  @return true if LIR is currently analyzed, false otherwize.
**/
public boolean
isLirAnalysis()
{
  return (! fHirAnalysis);
}

/** Indicate that HIR is currently analyzed.
**/
public void
setHirAnalysis()
{
  fHirAnalysis = true;
}

/** Indicate that LIR is currently analyzed.
**/
public void
setLirAnalysis()
{
  fHirAnalysis = false;
}

public FlowRoot
getInheritedFlowRoot()
{
  return fInheritedFlowRoot;
}

public void
setInheritedFlowRoot( FlowRoot pInheritedFlowRoot )
{
  fInheritedFlowRoot = pInheritedFlowRoot;
}


//===== Constants ======//

/** State of flow analysis
**/
  public static final int
    STATE_DATA_UNAVAILABLE    = 0,
    STATE_CFG_RESTRUCTURING   = 1,
    STATE_CFG_AVAILABLE       = 2,
    STATE_DATA_FLOW_AVAILABLE = 3,
    STATE_HIR_FLOW_AVAILABLE  = 4,
    STATE_LIR_FLOW_AVAILABLE  = 5;

/** Flow analysis option
**/
  public static final int
    OPTION_MINIMAL_CONTROL_FLOW  =  1,  // Separate into basic blocks and
                                        // compute predecessor/successor relations.
    OPTION_DOMINATOR             =  2,  // Option 1 plus dominator relation.
    OPTION_POST_DOMINATOR        =  3,  // Option 1 plus post dominator relation.
    OPTION_STANDARD_CONTROL_FLOW =  4,  // Option 1, 2, 3 are included.
    OPTION_MINIMAL_DATA_FLOW     =  5,  // Option 1 plus DefUseList computation
                                        // including numbering of variables and
                                        // IR nodes.
    OPTION_DEF_KILL              =  6,  // Option 1, 5 plus Def/Kill computation.
    OPTION_REACH                 =  7,  // Option 1, 5, 6 plus Reach computation.
    OPTION_DEFINED_EXPOSED       =  8,  // Option 1, 5 plus Defined/Exposed.
    OPTION_EGEN_EKILL            =  9,  // Option 1, 5 plus EGen/EKill.
    OPTION_AVAIL_IN_OUT          = 10,  // Option 1, 5, 9 plus AvailIn/AvailOut.
    OPTION_LIVE_IN_OUT           = 11,  // Option 1, 5, 8 plus LiveIn/LiveOut.
    OPTION_DEF_IN_OUT            = 12,  // Option 1, 5 plus DefIn/DefOut.
    OPTION_STANDARD_DATA_FLOW    = 13;  // Options 1 through 12 are all covered.

  public static final int
    OPTION_COUNT = 14;                  // Number of flow options.

  public static final int[][]
    OPTION_MATRIX = {
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //  0
      { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //  1
      { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //  2
      { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //  3
      { 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //  4
      { 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, //  5
      { 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0}, //  6
      { 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0}, //  7
      { 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0}, //  8
      { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0}, //  9
      { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0}, // 10
      { 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0}, // 11
      { 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0}, // 12
      { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}  // 13
     };

} // FlowRoot class

