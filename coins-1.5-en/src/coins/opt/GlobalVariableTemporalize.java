/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.opt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import coins.Debug;
import coins.SymRoot;
//##63 import coins.aflow.BBlock;
import coins.flow.BBlock; //##63
//##94 import coins.aflow.FlowResults;
//##63 import coins.aflow.InitiateFlowHir;
//##63 import coins.aflow.SetRefRepr;
import coins.flow.SetRefRepr; //##63
//##63 import coins.aflow.SetRefReprList;
import coins.flow.SetRefReprList; //##63
//##63 import coins.aflow.SubpFlow;
import coins.flow.SubpFlow; //##63
//##63 import coins.aflow.SubpFlowImpl;
import coins.flow.SubpFlowImpl; //##63
import coins.alias.AliasAnal;
import coins.alias.AliasAnalHir1;
import coins.alias.RecordAlias;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubpNode;
import coins.ir.hir.VarNode;
import coins.sym.Sym;
import coins.sym.Type;
import coins.sym.Var;


/**
 *
 * <p>title: ReplaceInfo class.</p>
 * <p>description: Information of global variable for replace.</p>
 * @author S.Noishi
 * @version 1.0
 */
class ReplaceInfo
{

  /**
   * <p>title: class Item</p>
   * <p>description: Information of global variable whitch is related by
   * key of Map structure.
   * It has following contants.
   * <ul>
   * <li>count of use.</li>
   * <li>temporal variable whitch replaced.</li>
   * <li>List of VarNode, whitch is global variable to replace temporal variable </li>
   * </ul>
   * </p>
   * @author S.Noishi
   * @version 1.0
   */
  class Item{
    protected int fCount;
    protected Var fReplacedVar;
    protected List fReplaceNodeList;

    /**
     * Construct this object.
     */
    public Item(){
      clear();
    }

    /**
     * Clear this object.
     */
    public void clear(){
      fCount = 0;
      fReplacedVar = null;
      fReplaceNodeList = new LinkedList();
    }

    /**
     * Get use count.
     * @return use count.
     */
    public int getCount(){
      return fCount;
    }

    /**
     * Get temporal var whitch replaced.
     * @return temporal var whitch replaced.
     */
    public Var getReplacedVar(){
      return fReplacedVar;
    }

    /**
     * Set temporal var whitch replaced
     * @param pVar temporal var whitch replaced.
     */
    public void setReplacedVar( Var pVar ){
      fReplacedVar = pVar;
    }

    /**
     * Get list of VarNodes whitch replace.
     * @return List of VarNodes whitch replace.
     */
    public List getReplaceNodeList(){
      return fReplaceNodeList;
    }

    /**
     * Get count of list of VarNodes whitch replace.
     * @return Count of list of VarNodes whitch replace.
     */
    public int getReplaceNodeCount(){
       return ( fReplaceNodeList == null ) ? 0 : fReplaceNodeList.size();
    }

    /**
     * Add using of VarNode,
     * and this node may be replaced by temporal var,
     * so add to list of replacenode.
     * @param pReplaceVarNode Var node whitch may be replaced by temporal var.
     */
    public void add( VarNode pReplaceVarNode ){
      fCount++;
      fReplaceNodeList.add( pReplaceVarNode );
    }

    /**
     * Remove VarNode from ReplaceNodeList,
     * when it is replaced by temporal variable.
     * @param pReplacedNode Removed var node whitch is replaced.
     * @return true if removed, false else.
     */
    public boolean removeReplacdNode( VarNode pReplacedNode ){
        // do not decrement fCount.
        return fReplaceNodeList.remove(pReplacedNode);
    }

  }

  /**
  * Map of global var to replace information.
  * this map is formed as following.
  *   key    : global var
  *   element: Item object, whitch is inner class of this.
  */
  protected Map fReplaceMap;

  /**
   * Construct this object.
   */
  public ReplaceInfo(){
   clear();
  }

  /**
   * Clear this object.
   */
  public void clear(){
    fReplaceMap = new HashMap();
  }

  /**
   * get Item object related by pSym.
   * @param pSym indicate to get related Item object.
   * this may be global simple var sym.
   * @return Item object related by pSym.
   */
  protected Item getItem( Sym pSym ){
    return (Item)fReplaceMap.get( pSym );
  }

  /**
   * Add replace var node.
   * @param lGlobalVarNode var node of replacing.
   */
  public void add( VarNode lGlobalVarNode ){
    Sym lSym = lGlobalVarNode.getSym();
    Item lItem = getItem( lSym );
    if( lItem == null ){
      lItem = new Item();
      fReplaceMap.put( lSym, lItem );
    }
    lItem.add( lGlobalVarNode );
  }

  /**
   * Remove var of replacing.
   * @param pSym sym of removing.
   */
  public void remove( Sym pSym ){
    fReplaceMap.remove( pSym );
 }

 /**
  * Get Set of Sym larger than pCount.
  * @param pCount use count for replace.
  * @return Set of Sym larger than pCount.
  */
 public Set getReplaceSymSet( int pCount ){
   Set lReplaceSymSet = new HashSet();
   Set lKeySet = fReplaceMap.keySet();
   Iterator lIterator = lKeySet.iterator();
   while( lIterator.hasNext() ){
     Sym lSym = (Sym)lIterator.next();
     Item lItem = getItem(lSym);
     if( lItem != null ){
       if( (lItem.getCount() >= pCount) && (lItem.getReplaceNodeCount() > 0)){
           lReplaceSymSet.add(lSym);
       }
     }
   }
   return lReplaceSymSet;
 }

 /**
  * Get List of VarNode to replace, whitch is related by pSym.
  * @param pSym indicate to get related List of VarNode.
  * It may be global simple var.
  * @return List of VarNode.
  */
 public List getReplaceNodeList( Sym pSym ){
   Item lItem = getItem( pSym );
   return (lItem == null ) ? null : lItem.getReplaceNodeList();
 }

 /**
  * Get replaced temporal variable, whitch is related by pSym.
  * @param pSym indicate to get related temporal variable.
  * It may be global simple var.
  * @return Var sym whitch is tempral varialbe of replaced.
  */
 public Var getReplacedVar( Sym pSym ){
   Item lItem = getItem( pSym );
   return (lItem == null ) ? null : lItem.getReplacedVar();
 }

 /**
  * Set replaced temporal variable, whitch is related by pSym.
  * @param pSym indicate to set related temporal variable.
  * It may be global simple var.
  * @param pVar temporal variable.
  * @return true if successed, false else.
  */
 public boolean setReplacedVar( Sym pSym, Var pVar ){
   Item lItem = getItem( pSym );
   if( lItem == null ){
     return false;
   }
   lItem.setReplacedVar( pVar );
   return true;
 }

 /**
  * Remove Gloval temporal var from replaced node list
  * whitch is related by pSym.
  * @param pVarNode Global temporal var whitch is replaced.
  * @return treu if remoded, false else.
  */
 public boolean removeReplacedNode( VarNode pVarNode ){
     Sym lSym = pVarNode.getSym();
     Item lItem = getItem( lSym );
     return (lItem == null ) ? false : lItem.removeReplacdNode( pVarNode );
 }

}

/**
 *
 * Information of replaced temp var and corresponds global variable.
 *
 */
class TempInfo {

    /**
     * Map of global var to temporal var whitch replaced.
     * this map is formed as following.
     *   key     : global var.
     *   element : temporal var, whitch is replaced.
     */
     protected Map fTempVarMap = null;


    /**
     * Construct this object.
     *
     */
     public TempInfo(){
        clear();
     }

    /**
     * Clear this object.
     */
     public void clear(){
        fTempVarMap = new HashMap();
     }

      /**
      * Get temporal var whitch replaced.
      * @param pSym gloval var.
      * @return temporal var whitch replaced.
      */
      public Var get(Sym pSym) {
          return (Var)fTempVarMap.get(pSym);
      }

      /**
      * Set temporal var whitch replaced
      * @param pSym global var.
      * @param pVar temporal var whitch replaced.
      */
      public void set(Sym pSym, Var pVar ) {
          fTempVarMap.put(pSym, pVar );
      }
}


/**
 * Replace global variables to temporal variable in supprogram.
 */
public class GlobalVariableTemporalize
{

  /**
  * FlowResults this object refer
  */
  protected final SubpDefinition fSubpDef;

  /**
   * FlowResults this object refer
   */
  protected final SubpFlow fSubpFlow;


  /**
  * AliasAnal this object refer
  */
  protected final AliasAnal fAliasAnal;

  /**
   * RecordAlias this object refer.
   */
  private RecordAlias fRecordAlias;

  /**
   * Minumum use count for temporalize
   * default value is 2.
   */
  protected int fMinimumUseCountForTemporalize = 2;


  /**
   * Set of function names that is not modify any global vars.
   */
  protected Set fValidFunctionSet;

  /**
   * Infomation of temp var which replaced global var. 2005.03.16
   */
  private TempInfo    fTempInfo;

  /**
   * Construct this object.
   * @param pSubpDef Subp definition of adapting global variable temporalize
   * @param pSubpFlow SubpFlow  whitch is refered/changed.
   * @param pAliasAnal Alias analysis whitch is refered.
   * @author S.Noishi
   */
  public GlobalVariableTemporalize( SubpDefinition pSubpDef,
                                    SubpFlow pSubpFlow,
                                    AliasAnal pAliasAnal){
    fSubpDef = pSubpDef;
    fSubpFlow = pSubpFlow;
    fAliasAnal = pAliasAnal;
    // default of valid functions is only "printf"
    Set lValidFunctionSet = new HashSet();
    lValidFunctionSet.add( "printf" );
    fValidFunctionSet = lValidFunctionSet;
    fTempInfo = new TempInfo(); // 2005.03.16 S.Noishi
  }

  /**
   * Get SubpDefinition this object refer.
   * @return SubpDefinition this object refer
   * @author S.Noishi
   */
  protected SubpDefinition getSubpDef(){
    return fSubpDef;
  }

  /**
   * Get SubpFlow this object refer.
   * @return SubpFlow this object refer
   * @author S.Noishi
   */
  protected SubpFlow getSubpFlow(){
    return fSubpFlow;
  }

  /**
   * Get AliasAnal this object refer.
   * @return AliasAnal this object refer
   * @author S.Noishi
   */
  protected AliasAnal getAliasAnal(){
    return fAliasAnal;
  }

  /**
   * Get RecordAlias this object refer.
   * @return RecordAlias this object refer
   * @author S.Noishi
  */
  protected RecordAlias getRecordAlias(){
    return fRecordAlias;
  }

  /**
   * Get FlowResults this object refer.
   * @return FlowResults this object refer
   * @author S.Noishi
   */
//##63   protected FlowResults getFlowResults(){
//##63     return fSubpFlow.results();
//##63   }

  /**
   * Get Debug this object refer.
   * @return Debug this object refer
   * @author S.Noishi
   */
  protected Debug getDebug(){
    //##63 return getFlowResults().flowRoot.ioRoot.dbgOpt1;
    return fSubpFlow.getFlowRoot().ioRoot.dbgOpt1; //##63
  }

  /**
   * Get minimum use count for temporalize.
   * @return Minimum use count for temporalize
   * @author S.Noishi
   */
  public int getMinimumUseCountForTemporalize(){
    return fMinimumUseCountForTemporalize;
  }

  /**
   * Set minimum use count for temporalize.
   * @param pCount minimum use count for temporalize.
   * @author S.Noishi
  */
  public void setMinimumUseCountForTemporalize( int pCount ){
    fMinimumUseCountForTemporalize = pCount;
  }

  /**
   * Get function names that is not modify any global vars.
   * @return Set of function names that is not modify any global vars.
   * @author S.Noishi
   */
  public Set getValidFunctions(){
    return fValidFunctionSet;
  }

  /**
   * Set function names that is not modify any global vars.
   * @param pValidFunctions Set of function names
   *                        that is not modify any global vars.
   * @author S.Noishi
   */
  public void setValidFunctions( Set pValidFunctions ){
    fValidFunctionSet = pValidFunctions;
  }

  /**
   * Replace global variables to temporal variable in subprogram
   *
   * @return true if changed, else false.
   * @author S.Noishi
   */
  public boolean doSubprogram(){
   initialize();

   getDebug().print(1, "doSubprogram", getSubpFlow().getSubpSym().toString());
    // Do for each  BBlock of subprogram.
    boolean lChanged = false;
    //##63 for (Iterator lIt = getSubpFlow().getBBlocks().iterator();
    for (Iterator lIt = getSubpFlow().getBBlockTable().iterator(); //##63
         lIt.hasNext(); ) {
      BBlock lBBlock = (BBlock)lIt.next();
      if( doBBlock(lBBlock) ){
        lChanged = true;
      }
    }
    if (lChanged){
      //##62 getSubpFlow().setRestructureFlag();
    }
    return lChanged;
  }

  /**
   * Replace global variables to temporal variable in basic block
   *
   * @param  pBBlock BBlock replaceing global variables to temporal variable
   * @return true if changed, else false.
   * @author S.Noishi
   */
   public boolean doBBlock(BBlock pBBlock )
   {
     if ((pBBlock == null)||(pBBlock.getBBlockNumber() == 0))
       return false; //##63
     final String lMethodName = "doBBlock";
     int lChangeCount = 0;
     getDebug().print(2, lMethodName, "pBBlock:" + pBBlock.toString());

     //// Do for each statement of BBlock
     ReplaceInfo lReplaceInfo = new ReplaceInfo();
     Set lGlobalSimpleVariableSet = getGlobalSimpleVariableSet( pBBlock );
     //##63 SetRefReprList lSetRefReprList = (SetRefReprList)(getFlowResults().get("BBlockSetRefReprs", pBBlock));
     SetRefReprList lSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
     getDebug().print(6, lMethodName, "lSetRefReprList:" + lSetRefReprList.toString());
     Iterator lIterator = lSetRefReprList.iterator();
     while (lIterator.hasNext()) {
       SetRefRepr lSetRefRepr = (SetRefRepr) lIterator.next();
       // if statement has invalid call, discard all replace.
       if( hasInvalidCall( lSetRefRepr) ){
        getDebug().print(6, lMethodName, "lSetRefReprList:" + "Invalid call is included " + lSetRefRepr );
        lReplaceInfo.clear();
        continue;
       }
       // if statment has valid call, do not replace.
       // now, replace of function param has problem.
       if( lSetRefRepr.hasCallWithSideEffect() ){ //##71
         getDebug().print(6, lMethodName, "lSetRefReprList:" + "Valid call is included " + lSetRefRepr );
         continue;
       }
       //// Do for each use node of statement.
       Iterator lItUseNodeList = lSetRefRepr.useNodeIterator();
       while( lItUseNodeList.hasNext() ){
         // Check whther use nod's sym is global variable.
         HIR lNode = (HIR)lItUseNodeList.next();
         Sym lUseSym = lNode.getSym();
         if (! lGlobalSimpleVariableSet.contains(lUseSym)) {
           continue;
         }
         getDebug().print(6, lMethodName, "Use gloval var :" + lUseSym );

         // if gloval var is possible taken it's address, then do not replace.
         boolean isPossiblyAddressTaken = fRecordAlias.possiblyAddressTaken(lUseSym);
         if( isPossiblyAddressTaken ){
           getDebug().print(4, lMethodName, "Address taken :" + lUseSym );
           continue;
         }

         //// add to replace varnode
         lReplaceInfo.add( (VarNode)lNode );
         getDebug().print(6, lMethodName, "Add replace var node :" + lNode );
       }


       //// Do for each mod, and it's aliassed syms.
       Set lModSymsSet = lSetRefRepr.modSyms();
       getDebug().print(6, lMethodName, "Mod syms:" + lModSymsSet );
       lModSymsSet.retainAll(lGlobalSimpleVariableSet);
       getDebug().print(6, lMethodName, "Global mod syms:" + lModSymsSet );
       Set lAliassedSymsSet = getRecordAlias().aliasSymGroup(lModSymsSet);
       getDebug().print(6, lMethodName, "Aliassed syms:" + lModSymsSet );
       lAliassedSymsSet.retainAll(lGlobalSimpleVariableSet);
       getDebug().print(4, lMethodName, "Global aliassed syms:" + lModSymsSet );

       Iterator iMod = lAliassedSymsSet.iterator();
       while (iMod.hasNext()) {
         Sym lSymMod = (Sym)iMod.next();
         lReplaceInfo.remove( lSymMod );
       }

       //// replace
       lChangeCount += replace( lReplaceInfo );
     }

     getDebug().print(6, lMethodName, "replaced count :" + lChangeCount );
     return (lChangeCount > 0);
   }


   /**
    * Replace by information of pReplaceInfo having.
    * In this routine, compare use count of each global variables pReplaceInfo
    * having to minimum use count for temporalize whitch this object having.
    * If some global variable >= minimum use,
    * then use nodes of it is replaced by temporal variable.
    * @param pReplaceInfo this object has information of use count of
    * each global variables at searching process.
    * @return count of replaced times.
    */
   private int replace( ReplaceInfo pReplaceInfo ){
     final String lMethodName = "replace";
     int lReplaceCount = 0;
     //// Do for each replace sym.
     Set lReplaceSymSet = pReplaceInfo.getReplaceSymSet( getMinimumUseCountForTemporalize() );
     getDebug().print(4, lMethodName, "Replace global vars:" + lReplaceSymSet );
     Iterator lReplaceSymSetIterator = lReplaceSymSet.iterator();
     while( lReplaceSymSetIterator.hasNext() ){
       Sym lSym = (Sym)lReplaceSymSetIterator.next();

       List lReplaceNodeList = pReplaceInfo.getReplaceNodeList( lSym );
       if( lReplaceNodeList == null ){
         getDebug().print(3, lMethodName, "Get replacenode of sym :" + lSym + " is null." );
         continue;
       }
       getDebug().print(4, lMethodName,
           "Replace Nodes of gloval var: " + lSym + " is " + lReplaceNodeList );

       //// Do for each replace Node.
       Iterator lReplaceNodeListIterator = lReplaceNodeList.iterator();
       while( lReplaceNodeListIterator.hasNext() ){
         VarNode lVarNode = (VarNode)lReplaceNodeListIterator.next();
         if( replace( lVarNode, pReplaceInfo ) ){
             lReplaceNodeListIterator.remove();
             lReplaceCount++;
         }
       }
     }
     return lReplaceCount;
   }

   /**
    * Replace pVarNode by information pReplaceInfo having.
    * @param pVarNode replace node.
    * @param pReplaceInfo refer/create global temporal var for replacing.
    * @return true if replaced, false else.
    */
    private boolean replace( VarNode pVarNode, ReplaceInfo pReplaceInfo ){
      final String lMethodName = "replace";
      boolean isChange = false;
      Stmt lStmt = pVarNode.getStmtContainingThisNode();
      if (lStmt == null) {
        getDebug().print(3, lMethodName, "Get Stmt failed.");
        return false;
      }
      // Replace the gloval variable to temporal variable
      getDebug().print(6, lMethodName, "Stmt:" + lStmt );

      //##63 SymRoot lSymRoot = getFlowResults().flowRoot.symRoot;
      SymRoot lSymRoot = fSubpFlow.getFlowRoot().symRoot; //##63

      Var lGlobalVar = (Var)pVarNode.getSym();

      // If already assigned correspond temp to the global variable, use it.
      Var lInsertedLHSVar = fTempInfo.get(lGlobalVar);
      if( lInsertedLHSVar == null ){
          lInsertedLHSVar= lSymRoot.symTableCurrent.generateVar(
                                  pVarNode.getType(), lSymRoot.subpCurrent);
           fTempInfo.set(lGlobalVar, lInsertedLHSVar);
      }
      if(pReplaceInfo.getReplacedVar(lGlobalVar) == null ){
          // Insert Assign Stmt 'type Temp = global var;'
         AssignStmt lInsertedStmt = createAssignStmt( lInsertedLHSVar, (Exp)pVarNode );
         insertAssignStmt( lStmt, lInsertedStmt );
         getDebug().print(4, lMethodName, "Insert temp assign stmt:" + lInsertedStmt );

         pReplaceInfo.setReplacedVar( lGlobalVar, lInsertedLHSVar );
      }

/*   Modify 2005.03.16 S.Noishi
     // If already assigned correspond temp to the global variable, use it.
     Var lInsertedLHSVar = pReplaceInfo.getReplacedVar(lGlobalVar);
     // If not assigned temp yet, insert new temp assigne statement.
    if( lInsertedLHSVar == null ){
         lInsertedLHSVar= lSymRoot.symTableCurrent.generateVar(
                                 pVarNode.getType(), lSymRoot.subpCurrent);
         // Insert Assign Stmt 'type Temp = global var;'
        AssignStmt lInsertedStmt = createAssignStmt( lInsertedLHSVar, (Exp)pVarNode );
        insertAssignStmt( lStmt, lInsertedStmt );
        getDebug().print(4, lMethodName, "Insert temp assign stmt:" + lInsertedStmt );

        pReplaceInfo.setReplacedVar( lGlobalVar, lInsertedLHSVar );
     }
*/

     getDebug().print(6, lMethodName, "Temp var:" + lInsertedLHSVar.toStringShort());

      // Replace global variable to temp variable.
      //##63 HIR hir = getFlowResults().flowRoot.hirRoot.hir;
      HIR hir = fSubpFlow.getFlowRoot().hirRoot.hir; //##63
      //##57 VarNode lNewNode0 = hir.varNode(lInsertedLHSVar);
      //##57 OptUtil.replaceNode(pVarNode, lNewNode0); //##57 pVarNode is later replaced.
      VarNode lNewNode = hir.varNode(lInsertedLHSVar);
      getDebug().print(3, lMethodName, " replaceNode " +  pVarNode + " by " + lNewNode);
      OptUtil.replaceNode( pVarNode, lNewNode);
      // do not remove this position. iterated upper routine.
      //if( ! pReplaceInfo.removeReplacedNode(pVarNode) ){
      //    getDebug().print(3, lMethodName, " removeReplacedNode of " +  pVarNode + " failed." );
      //}
      isChange = true;

      return isChange;

    }

   /**
    * create assign statement for helping
    * replace global variable to temp variable.
    * This method is only for helping replace to temp variable,
    * but It's considered for create any assign stmt.
    * @param pLHSVar LHS variable of assign statement.
    * @param pRHSExp RHS expression of assign statement.
    * @return assign statement created.
    * @author S.Noishi
    */
   private AssignStmt createAssignStmt( Var pLHSVar, Exp pRHSExp ){
     //##63 HIR hir = getFlowResults().flowRoot.hirRoot.hir;
     HIR hir = fSubpFlow.getFlowRoot().hirRoot.hir; //##63
     Exp lInsertedRHS = (Exp)pRHSExp.copyWithOperands();
     VarNode lInsertedLHS = hir.varNode(pLHSVar);
     return hir.assignStmt(lInsertedLHS, lInsertedRHS);
   }


   /**
    * Insert assign statement to previous of the statement indicated by pStmt.
    * It is helper method for replacing global variable to temp variable.
    * @param pStmt Statement whtch is inserted assign stertment by previous.
    * @param pInsertStmt Assign statement of insert.
    * @author S.Noishi
    */
   private void insertAssignStmt( Stmt pStmt, AssignStmt pInsertStmt ){
     if (pStmt instanceof LabeledStmt) {
       // Insert before the label.
       Stmt lStmtBody = ((LabeledStmt)pStmt).getStmt();
       if (lStmtBody == null) {
         ((LabeledStmt)pStmt).setStmt(pInsertStmt);
       } else {
         lStmtBody.insertPreviousStmt(pInsertStmt, pStmt.getBlockStmt());
       }
     } else {
       pStmt.insertPreviousStmt(pInsertStmt, pStmt.getBlockStmt());
     }
   }

   /**
    * Check whether pSetRefRepl has invalid call statement.
    * It is helper of doBBlock().
    * @param pSetRefRepl SetRefRepl of checking.
    * @return true if pSetRefRepl has call statement, else false.
    * @author S.Noishi
    */
   private boolean hasInvalidCall( SetRefRepr pSetRefRepl ){
     final String lMethodName = "hasInvalidCall";
     boolean lHasInvalidCall = false;
     List lCallNodes = pSetRefRepl.callNodes();
     // Do for each call node.
     Iterator lItCollNodes = lCallNodes.iterator();
     while (lItCollNodes.hasNext()) {
       // Get function name
       FunctionExp lFunctionExp = (FunctionExp)lItCollNodes.next();
       Exp lExp = (Exp)lFunctionExp.getChild1();
       String lFunctionName = null;
       SubpNode lFunctionNode = lFunctionExp.getFunctionNode();
       if (lFunctionNode != null) {
         Sym lFunctionSym = lFunctionNode.getSym();
         lFunctionName = lFunctionSym.getName();
       }
       if ((lFunctionName == null) || (lFunctionName.length() == 0)) {
         getDebug().print(6, lMethodName,
           "Non named function is found. It's regarded as valid function.");
         lHasInvalidCall = true;
         break;
       } else {
         getDebug().print(8, lMethodName, "Function name: " + lFunctionName);
         Set lValidFunctionsSet = getValidFunctions();
         if ((lValidFunctionsSet != null) &&
           lValidFunctionsSet.contains(lFunctionName)) {
           getDebug().print(6, lMethodName, "Function name: "
             + lFunctionName + " is valid function.");
           ; // This function is not invalid.
         } else {
           lHasInvalidCall = true;
           break;
         }
       }
     }
     return lHasInvalidCall;
   }

   /**
    * Check whether pBBlock has invalid call statement.
    * It is helper of doBBlock().
    * @param pBBlock BBlock of checking.
    * @return true if pBBlock has call statement, else false.
    * @author S.Noishi
    */
   private boolean hasInvalidCall( BBlock pBBlock )
   {
     final String lMethodName = "hasInvalidCall";
     //##63 SetRefReprList lSetRefReprList = (SetRefReprList)(getFlowResults().get(
     //##63   "BBlockSetRefReprs", pBBlock));
     SetRefReprList lSetRefReprList = fSubpFlow.getSetRefReprList(pBBlock); //##63
     getDebug().print(6, lMethodName,
       "lSetRefReprList:" + lSetRefReprList.toString());
     Iterator lIterator = lSetRefReprList.iterator();
     boolean lHasInvalidCall = false;
     // Do for each statement;
     while (lIterator.hasNext()) {
       SetRefRepr lSetRefRepr = (SetRefRepr)lIterator.next();
       if( hasInvalidCall( lSetRefRepr ) ){
         lHasInvalidCall = true;
         break;
       }
     }
     return lHasInvalidCall;
   }

   /**
      * Initialize for
      * replace global variables to temporal variable in supprogram.
      *
      * @author S.Noishi
      */
     protected void initialize(){
       /* //##64
//       ((SubpFlowImpl)getSubpFlow()).fHirAnalExtended = true;
//       ShowFlow lShowFlow = new ShowFlow(getFlowResults()); // Commented because it may be not needed 2004.10.21 S.Noishi
       ((SubpFlowImpl)getSubpFlow()).allocateSetRefReprTable();
       // Check empty against following message.   // 2004.10.21 S.Noishi
       //     "Recovered error 5555: MakeControlFlowGraph: CFG info already exists.
       //      Call FlowResults#clearAll before begining a new analysis."
       if( getFlowResults().isEmpty() ){
         getSubpFlow().controlFlowAnal();
       }
       InitiateFlowHir lInitiateFlow = new InitiateFlowHir(getFlowResults());
       lInitiateFlow.initiate(getSubpFlow());
//       ((HirSubpFlowImpl)getSubpFlow()).computeBBlockSetRefReprs( getSubpFlow());
       */ //##63
       //##63 BEGIN
       if (! fSubpFlow.isComputed(fSubpFlow.CF_CFG)) {
         fSubpFlow.getFlowRoot().flow.controlFlowAnal(fSubpFlow);
       }
       //##63 END
       fRecordAlias = new RecordAlias((AliasAnalHir1)getAliasAnal(),
         getSubpDef()); //##62
       //##63 ((SubpFlowImpl)getSubpFlow()).fRecordSetRefReprs.find(getSubpFlow());

       fTempInfo.clear();   // 2005.03.16 S.Noishi

/*
       //////////////// debugging for test ////////////////
       String lMethodName = "initialize";
       BBlock lBBlock = (BBlock)(getSubpFlow().getBBlocks().iterator().next() );
       SetRefReprList lSetRefReprList = (SetRefReprList)(getFlowResults().get("BBlockSetRefReprs", lBBlock));
       getDebug().print(6, lMethodName, "lSetRefReprList:" + lSetRefReprList );
       getDebug().print(6, lMethodName, "lSetRefReprList size:" + lSetRefReprList.size());
       Iterator lIterator = lSetRefReprList.iterator();
       while (lIterator.hasNext()) {
         SetRefRepr lSetRefRepl = (SetRefRepr) lIterator.next();
         boolean isE = lSetRefRepl instanceof SetRefReprHirEImpl;
         getDebug().print(6, lMethodName, "isE:" + isE );
       }
*/
     }

     /**
      * Get set of global simple variable of pBBlock
      * @param pBBlock BBlock of which get global simple variable set.
      * @return Set of global simple variable
      * @author S.Noishi
      */
     private Set getGlobalSimpleVariableSet( BBlock pBBlock ){
       final String lMethodName = "getGlobalSimpleVariableSet";
       getDebug().print(3, lMethodName, "BBlock:" + pBBlock);
       Set lGlobalVariablesSet = fSubpFlow.setOfGlobalVariables(); // Symple variable ?
       getDebug().print(6, lMethodName,
         "lGlobalVariablesSet:" + lGlobalVariablesSet);

       // Create simple global variables set to lGlobalSympleVariableSet
       // from global bariables set
       Set lGlobalSimpleVariableSet = new HashSet();
       Iterator lItGlobal = lGlobalVariablesSet.iterator();
       while (lItGlobal.hasNext()) {
         Sym sym = (Sym)lItGlobal.next();
         if (sym instanceof Var) {
           getDebug().print(8, lMethodName,
             "Sym:" + sym + "Kind:" + sym.getSymKindName());
           // check sym is not volatile type.
           Type lType = sym.getSymType();
           if( lType == null ){
             getDebug().print(3, lMethodName,
               "getSymType() returns null of sym:" + sym );
           }
           else{
            if( lType.isVolatile() ){
              getDebug().print(3, lMethodName,
               "sym:" + sym + " is volatile type." );
             continue; // do not add of volatile type.
            }
           }
           // check sym is basic type.
           if (sym.getSymType().isBasicType()) {
             lGlobalSimpleVariableSet.add(sym);
           }
         }
       }
       return lGlobalSimpleVariableSet;
    }
} // GlobalVariableTemporalization
