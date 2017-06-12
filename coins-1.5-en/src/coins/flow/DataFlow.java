/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.List;
import java.util.Set;

import coins.ir.IR;
import coins.ir.hir.HIR;
import coins.sym.FlowAnalSym;

/**
 * Interface for data flow analysis methods.
 *
 * All the data flow information is available accessing this interface.
 *
 * There are some dependecies between the methods in this interface.
 * For example, findReach() mustn't be called before both findDef() and findKill() have been called,
 * and findKill() in turn depends on the result of findDefined(). Please be careful in using.
 * findAll() method should call those methods in the correct order.
 *
 * @see DataFlowImpl
 * @see SetRefRepr
 */
public interface
  DataFlow
{
  /**
   * Finds and sets the Def vector for the entire flow.
   */
  void findDef();

  /**
   * Finds and sets the Def vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Def vector to find.
   */
  void findDef(BBlock pBBlock);

  /**
   * Finds and sets the Kill vector for the entire flow.
   */
  void findKill();

  /**
   * Finds and sets the Kill vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Kill vector to find.
   */
  void findKill(BBlock pBBlock);

  /**
   * Finds and sets the Defined vector for the entire flow.
   */
  void findDefined();

  /**
   * Finds and sets the Defined vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Defined vector to find.
   */
  void findDefined(BBlock pBBlock);

//##70 BEGIN
  /**
   * Finds and sets the Used vector for the entire flow.
   */
  void findUsed();

  /**
   * Finds and sets the Used vector for the given BBlock.
   * @param pBBlock BBlock whose Used vector to find.
   */
  void findUsed(BBlock pBBlock);
//##70 END

  /**
   * Finds and sets the Exposed vector for the entire flow.
   */
  void findExposed();

  /**
   * Finds and sets the Exposed vector for the given BBlock.
   *
   * @param pBBlock BBlock whose Exposed vector to find.
   */
  void findExposed(BBlock pBBlock);

  /**
   * Finds and sets the EGen vector for the entire flow
   * considering alias.
   */
  void findEGen();

  /**
   * Finds and sets the EGen vector for the given BBlock
   * considering alias.
   *
   * @param pBBlock BBlock whose EGen vector to find.
   */
  void findEGen(BBlock pBBlock);

  /**
   * Finds and sets the EKill vector for the entire flow.
   */
  void findEKill();

  /**
   * Finds and sets the EKill vector for the given BBlock
   * considering alias.
   *
   * @param pBBlock BBlock whose EKill vector to find.
   */
  void findEKill(BBlock pBBlock);

  /**
   * Finds and sets the In/Out vector for the entire flow.
   */
  //void findInOut();

  /**
   * Finds and sets the Reach vector for the entire flow.
   */
  void findReach();

  /**
   * Finds and sets the AvailIn/AvailOut vector for the entire flow.
   */
  void findAvailInAvailOut();

  /**
   * Finds and sets the LiveIn/LiveOut vector for the entire flow.
   */
  void findLiveInLiveOut();

  /**
   * Finds and sets the DefIn/DefOut vector for the entire flow.
   */
  void findDefInDefOut();

  /**
   * Finds and sets the data flow items that are independent of the control flow, that is, Def, Kill, Defined, Exposed, EGen, and EKill vectors.
   *
   * @see #findDef()
   * @see #findDefined()
   * @see #findKill()
   * @see #findExposed()
   * @see #findEGen()
   * @see #findEKill()
   */
  void findBasic();

  /**
   * Solves all the data flow equations to find In, Out, Reach, AvailIn, AvailOut, LiveIn, LiveOut, DefIn, and DefOut vectors.
   *
   * //@see #findInOut()
   * @see #findReach()
   * @see #findAvailInAvailOut()
   * @see #findLiveInLiveOut()
   * @see #findDefInDefOut()
   */
  void solveAll();

  /**
   * Finds and sets all the BitVectors, that is, Def, Kill, In, Out, Reach, Defined, Exposed,
     * EGen, EKill, AvailIn, AvailOut, LiveIn, LiveOut, DefIn, and DefOut vectors.
   *
   * @see #findBasic()
   * @see #solveAll()
   */
  void findAllBitVectors();

  /**
   * Finds and sets the DefUseList for each FlowAnalSym
   * without considering side effects of alias and call.
   */
  void findDefUse();

  /**
   * Finds and sets the DefUseList for each FlowAnalSym
   * considering side effects of alias and call.
   */
  void findDefUseExhaustively(); //##73

  /**
   * Finds and sets the UseDefList for each FlowAnalSym
   * considering alias and call.
   */
  public void
    findUseDef(); //##63

  /**
   * Finds and sets the UseDefList for each FlowAnalSym
   * without considering side effects of alias and call.
   */
  public void
    findUseDefExhaustively(); //##73


  /**
   * Finds and sets all the data flow information. This method consists of findAllBitVectors and findDefUse.
   *
   * @see #findAllBitVectors()
   * @see #findDefUse()
   */
  void findAll();

  /**
   * Returns the # of value-setting SetRefReprs.
   *
   * @return the # of value-setting SetRefReprs.
   */
  int getDefCount();

  /**
   * Returns the # of Syms defined (declared) in this flow.
   *
   * @return the # of Syms defined (declared) in this flow.
   */
  //int getExpCount();

  /**
   * Returns the # of ExpIds generated in this flow.
   *
   * @return the # of FlowAnalSyms generated in this flow.
   */
  // int getExpIdCount();
  int getFlowAnalSymCount();

  /**
   * Returns the # of "Points" in this flow.
   *
   * @return the # of "Points" in this flow.
   */
  int getPointCount();

  /**
   * Returns the DefSetRefRepr index (SetRefRepr no. that is assigned to value-setting SetRefReprs) of the node that has the given IR index.
   *
   * @return the DefSetRefRepr index (SetRefRepr no. that is assigned to value-setting SetRefReprs) of the node that has the given IR index.
   */
  int getDefIndex(int pIRIndex);

  /**
   * Returns the IR node that corresponds to the given DefSetRefRepr index (entry of the DefVector).
   *
   * @return the IR node that corresponds to the given DefSetRefRepr index (entry of the DefVector).
   */
  IR getNodeFromDefIndex(int pDefIndex);

  /**
   * Returns the List of BBlocks in this flow.
   * (Excluding null and 0-numbered BBlock)
   * @return the List of BBlocks in this flow.
   */
  List getBBlockList();

  /**
   * Returns the Set of ExpIds that fall under the given subtree and are used.
   * The ExpId that is attached to the "Def node" will not be included if the given subtree is a value-setting node (AssignStmt in HIR).
     * The ExpId that corresponds to the given subtree is also included in the Set.
   *
   * @param pSubtree IR node that is the root of the subtree to examine.
   * @return the Set of ExpIds that fall under the given subtree and are used.
   */
  // java.util.Set getUseExpIds(IR pSubtree);
  java.util.Set getUseFlowAnalSyms(IR pSubtree);

  java.util.Set getUseFlowAnalSymsForHir(HIR pSubtree);

  /**
   * Returns the ExpId that has the given index.
   *
   * @param pExpIdIndex index of the ExpId to get.
   * @return the ExpId that has the given index.
   */
  // ExpId getExpId(int pExpIdIndex);
  FlowAnalSym getFlowAnalSym(int pFlowAnalSymIndex);

  /**
   * Changes the position in the ExpVector into the ExpId index.
   *
   * @param pBitPosition position in the ExpVector.
   */
  public int
    expReverseLookup(int pBitPosition);

//##60 BEGIN

/**
 * Changes the IR node index into the "Def" index.
 * @param pNodeIndex index of the value-setting node (AssignStmt in HIR).
 */
public int defLookup(int pNodeIndex);

/**
 * Changes the "Def" index into the IR node index.
 * @param pBitPosition index in the DefVector for which to find the node index.
 */
public int defReverseLookup(int pBitPosition);

/**
 * Changes the ExpId index into the position in the ExpVector. Currently does nothing.
 * @param pExpIdIndex index of ExpId.
 */
public int expLookup(int pExpIdIndex);

//##60 END

  /**
   * Returns the node index corresponding to the given "DefSetRefReprNo",
   *  which is the index attached to every value-setting SetRefRepr.
   *
   * @param pDefSetRefReprNo index of value-setting SetRefRepr.
   * @return the node index corresponding to the given "DefSetRefReprNo".
   */
  //##62 public int getNodeIndex(int pDefSetRefReprNo);
  public int
    getDefNodeIndex(int pDefSetRefReprNo); //##62

  /**
   * Returns the node that has the given index.
   *
   * @param pNodeIndex index of the node to be gotten.
   * @return the node that has the given index.
   */
  public IR
    getNode(int pNodeIndex);

  /**
   * Shows the Def vector for every BBlock.
   */
  void showDef();

  /**
   * Shows the Kill vector for every BBlock.
   */
  void showKill();

  /**
   * Shows the In vector for every BBlock.
   */
  //void showIn();

  /**
   * Shows the Out vector for every BBlock.
   */
  //void showOut();

  /**
   * Shows the Reach the vector for every BBlock.
   */
  void showReach();

  /**
   * Shows the Defined vector for every BBlock.
   */
  void showDefined();

  /**
   * Shows the Exposed vector for every BBlock.
   */
  void showExposed();

  /**
   * Shows the EGen vector for every BBlock.
   */
  void showEGen();

  /**
   * Shows the EKill vector for every BBlock.
   */
  void showEKill();

  /**
   * Shows the AvailIn vector for every BBlock.
   */
  void showAvailIn();

  /**
   * Shows the AvailOut vector for every BBlock.
   */
  void showAvailOut();

  /**
   * Shows the LiveIn vector for every BBlock.
   */
  void showLiveIn();

  /**
   * Shows the LiveOut vector for every BBlock.
   */
  void showLiveOut();

  /**
   * Shows the DefIn vector for every BBlock.
   */
  void showDefIn();

  /**
   * Shows the DefOut vector for every BBlock.
   */
  void showDefOut();

  /**
   * Shows all the DefVectors for every BBlock.
   *
   * @see #showDef()
   * @see #showKill()
   * //@see #showIn()
   * //@see #showOut()
   * @see #showReach()
   */
  void showDefVectors();

  /**
   * Shows all the ExpVectors for every BBlock.
   *
   * @see #showDefined()
   * @see #showExposed()
   * @see #showEGen()
   * @see #showEKill()
   * @see #showAvailIn()
   * @see #showAvailOut()
   * @see #showLiveIn()
   * @see #showLiveOut()
   * @see #showDefIn()
   * @see #showDefOut()
   */
  void showExpVectors();

  /**
   * Shows all the data flow items that are independent of the control flow, that is, Def, Kill, Defined, Exposed, EGen, and EKill vectors.
   *
   * @see #showDef()
   * @see #showKill()
   * @see #showDefined()
   * @see #showExposed()
   * @see #showEGen()
   * @see #showEKill()
   */
  void showBasic();

  /**
   * Shows all the data flow items data flow equations have found, that is, In, Out, Reach, AvailIn, AvailOut, LiveIn, LiveOut, DefIn, and DefOut vectors.
   *
   * //@see #showIn()
   * //@see #showOut()
   * @see #showReach()
   * @see #showAvailIn()
   * @see #showAvailOut()
   * @see #showLiveIn()
   * @see #showLiveOut()
   * @see #showDefIn()
   * @see #showDefOut()
   */
  void showSolved();

  /**
   * Shows BitVectors related to (needed to solve) In/Out vectors, that is, Def, Kill, In, and Out vectors.
   *
   * @see #showDef()
   * @see #showKill()
   * @see #showIn()
   * @see #showOut()
   */
  //  void showInOutRelated();

  /**
   * Shows BitVectors related to (needed to solve) Reach vectors, that is, Def, Kill, and Reach vectors.
   *
   * @see #showDef()
   * @see #showKill()
   * @see #showReach()
   */
  void showReachRelated();

  /**
   * Shows BitVectors related to (needed to solve) AvailIn/AvailOut vectors, that is, EGen, EKill, AvailIn, and AvailOut vectors.
   *
   * @see #showEGen()
   * @see #showEKill()
   * @see #showAvailIn()
   * @see #showAvailOut()
   */
  void showAvailInAvailOutRelated();

  /**
   * Shows BitVectors related to (needed to solve) LiveIn/LiveOut vectors, that is, Exposed, Defined, LiveIn, and LiveOut vectors.
   *
   * @see #showExposed()
   * @see #showDefined()
   * @see #showLiveIn()
   * @see #showLiveOut()
   */
  void showLiveInLiveOutRelated();

  /**
   * Shows BitVectors related to (needed to solve) DefIn/DefOut vectors, that is, Defined, DefIn, and DefOut vectors.
   *
   * @see #showDefined()
   * @see #showDefIn()
   * @see #showDefOut()
   */
  void showDefInDefOutRelated();

  /**
   * Shows all the BitVectors.
   *
   * @see #showDefVectors()
   * @see #showExpVectors()
   */
  void showAllBitVectors();

  /**
   * Shows the DefUseList for each FlowAnalSym.
   */
  void showDefUse();

  /**
   * Shows the UseDefList for each FlowAnalSym.
   */
  void showUseDef(); //##73

  /**
   * Shows all the information found in this analysis.
   *
   * @see #showAllBitVectors()
   * @see #showDefUse()
   */
  void showAll();

  /**
   * Show summary of data flow information.
   */
  void showSummary();

  public Set getUndefinedUseNodeOfSym( FlowAnalSym lSym ); //##60

} // DataFlow interface
