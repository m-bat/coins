/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/* RecordAlias.java //##31
 */
package coins.alias;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import coins.HirRoot;
import coins.IoRoot;
import coins.flow.SubpFlow; //##62
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirVisitorModel2;
import coins.ir.hir.PointedExp;
import coins.ir.hir.QualifiedExp;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.SymNode;
import coins.ir.hir.VarNode;
import coins.sym.FlowAnalSym; //##52
import coins.sym.Param; //##62
import coins.sym.Sym;
import coins.sym.Var;

/** RecordAlias
 *  Do the alias analysys of HIR subprogram and
 *  record its information so that it may be used in
 *  HIR-to-LIR conversion or LIR analysis.
 *  The sequence of statements
 *    AliasAnal lAlias2 = new AliasAnalHir2(hirRoot);
 *     ....
 *    lAliasAnal1.prepareForAliasAnalHir(subpDefinition);
 *    RecordAlias lAliasInf = new RecordAlias(lAliasAnal2, subpDefinition);
 *  will make following methods available:
 *    lAliasInf.possiblyAddressTaken(var)
 *        return true if var or its aliases are taken address
 *        else return false.
 *        (If false, var may be assigned to abstract register.)
 *     lAliasInf.mayAlias(var1, var2)
 *        return true if var1 and var2 are possibly aliased
 *  where, var, var1, var2 are HIR Var symbol which may be gotten
 *  from the corresponding LIR symbol.
 *  The above information is valid while one LIR symbol
 *  corresponds to at most one HIR symbol.
 */
public class RecordAlias
{
  public final HirRoot hirRoot;
  public final IoRoot  ioRoot0; // ioRoot is inherited from HirVisitorModel2.
//  public final AliasAnal fAliasAnalLevel1; // 2004.09.02 S.Nishi;
//  public final AliasAnal fAliasAnalLevel2; // 2004.09.02 S.Nishi;
  public final AliasAnal fAliasAnal;        // 2004.09.02 S.Nishi;

  protected Set fAddressTakenVars = new HashSet();
  protected Set fAliasOfAddressTakenVars = new HashSet();
  protected Set fSetOfGlobalVariables = new HashSet(); //##62

  /** Maps a variable to the set of VarNodes that are used to
   *  set value to the variable. */
  // protected Map fSetLvalueNodes = new HashMap();

  /** Maps a variable to the set of VarNodes that are used to
   *  refer the value of the variable. */
  // protected Map fRefNodesOfSym = new HashMap();

  /** Maps a variable to the set of VarNodes that are used to
   *  set or refer the value of the variable.
   *  (combined result of fSetNodesOfSym and fRefNodesOfSym */
  protected Map fNodesOfSym = new HashMap();

  /** Maps variable to the set of variables that are aliased to it. */
  // protected Map fSetAliasGroupOfSym = new HashMap();

  /** Maps variable to the set of variables that are aliased to it. */
  // protected Map fRefAliasGroupOfSym = new HashMap();

  /** Set of variables appered in the subprogram. */
  protected Set fSetOfSyms = new HashSet();

  /** Maps variable to the set of variables that are aliased to it. */
  protected Map fAliasGroupOfSym = new HashMap();

  /** Turned to true if call node with side effect appered in the subprogram.
   *  Call with no side effect does not change this field. */
  //##71 protected boolean fCallIncluded = false;
  protected boolean fCallWithSideEffectIncluded = false;
  // This field is not used in recording aliased symbols ? //##71
  // Is it treated in optimizer and flow analysis ? //##71

  protected int fDbgLevel; //##62

  protected Set fEmptySet = new HashSet(); //##62

/** RecordAlias
 *  Do the alias analysys of HIR subprogram and
 *  record its information (alias group, etc.) so that
 *  it may be used in HIR-to-LIR conversion or LIR analysis.
 * @param pAliasAnal Instance of AliasAnalHir1.
 * @param pSubpDef Subprogram definition subtree.
**/
//##54 RecordAlias( AliasAnalHir1 pAliasAnal, SubpDefinition pSubpDef )
public
RecordAlias( AliasAnal pAliasAnal, SubpDefinition pSubpDef)
{
  AliasAnalHir1 lAliasAnalHir = (AliasAnalHir1)pAliasAnal; // 2004.09.02 S.Noishi
  hirRoot = lAliasAnalHir.hirRoot; // 2004.09.02 S.Noishi
  ioRoot0 = hirRoot.ioRoot;
  ioRoot0.dbgAlias.print(1, "RecordAlias",
      pSubpDef.getSubpSym().toString());    //##56
  fDbgLevel = ioRoot0.dbgAlias.getLevel(); //##62
//  fAliasAnalLevel1 = pAliasAnal; // 2004.09.02 S.Noishi
//  fAliasAnalLevel2 = null;       // 2004.09.02 S.Noishi
//  fAliasAnalLevel1.prepareForAliasAnalHir(pSubpDef);
  fAliasAnal = pAliasAnal; // 2004.09.02 S.Noishi
  fAliasAnal.prepareForAliasAnalHir(pSubpDef); // 2004.09.02 S.Noishi

  AliasGroupInSubp
//   lAliasGroup = new AliasGroupInSubp(pAliasAnal, pSubpDef);
  lAliasGroup = new AliasGroupInSubp(lAliasAnalHir, pSubpDef); // 2004.09.02 S.Noishi
  lAliasGroup.visit(pSubpDef.getHirBody());
  computeAliasGroupForSyms();
} // RecordAlias

//##62 BEGIN
public
RecordAlias( AliasAnal pAliasAnal, SubpDefinition pSubpDef,
               SubpFlow pSubpFlow) //##62
{
  AliasAnalHir1 lAliasAnalHir = (AliasAnalHir1)pAliasAnal; // 2004.09.02 S.Noishi
  hirRoot = lAliasAnalHir.hirRoot; // 2004.09.02 S.Noishi
  ioRoot0 = hirRoot.ioRoot;
  ioRoot0.dbgAlias.print(1, "RecordAlias",
      pSubpDef.getSubpSym().toString());    //##56
  fDbgLevel = ioRoot0.dbgAlias.getLevel(); //##62
  fAliasAnal = pAliasAnal; // 2004.09.02 S.Noishi
  if ((pSubpFlow == null)|| //##62
      (pSubpFlow.getComplexityLevel() <= 1)) { //##62
      fAliasAnal.prepareForAliasAnalHir(pSubpDef); // 2004.09.02 S.Noishi
      AliasGroupInSubp lAliasGroup = new AliasGroupInSubp(lAliasAnalHir, pSubpDef); // 2004.09.02 S.Noishi
      lAliasGroup.visit(pSubpDef.getHirBody());
      computeAliasGroupForSyms();
  //##62 BEGIN
  }else {
    simplifiedAliasGrouping(pSubpFlow);
  }
  //##62 END
} // RecordAlias

/** possiblyAddressTaken
 * See if a variable is taken its address or not.
 * If the variable is aliased to some one that is taken address,
 * then the variable is treated as address-taken variable.
 * If subscripted variable of an array is taken address, then
 * the array is treated as the one that is taken address.
 * If element of structure/union is taken address, then the
 * structure/union is treated as the one that is taken address.
 * @param pSym Variable (Var, Param, Elem) or
 *     ExpId for subscripted/qualified variable.
 * @return true if pSym or its aliases are taken address
 *      else return false.
 */
public boolean
possiblyAddressTaken( Sym pSym )
{
  boolean lBool = fAliasOfAddressTakenVars.contains(pSym);
  ioRoot0.dbgAlias.print(3, "possiblyAddressTaken", pSym.toString() + " " +
                        lBool);
  return lBool;
}

/** mayAlias
 * See if 2 variables are aliased or not.
 * @param pSym1 Variable (Var, Param, Elem) or
 *     ExpId for subscripted/qualified variable.
 * @param pSym2 Variable (Var, Param, Elem) or
 *     ExpId for subscripted/qualified variable.
 * @return true if pSym1 is aliased to pSym2 or
 *     pSym2 is aliased to pSym1,
 *     else return false.
 */
public boolean
mayAlias( Sym pSym1, Sym pSym2 )
{
  boolean lMayAlias = false;
  if (fAliasGroupOfSym.containsKey(pSym1)) {
    if (((Set)fAliasGroupOfSym.get(pSym1)).contains(pSym2))
      lMayAlias = true;
  }
  if ((! lMayAlias) && fAliasGroupOfSym.containsKey(pSym2)) {
    if (((Set)fAliasGroupOfSym.get(pSym2)).contains(pSym1))
      lMayAlias = true;
  }
  if (fDbgLevel > 2) //##62
    ioRoot0.dbgAlias.print(3, "mayAlias", pSym1.toString() + " " +
                        pSym2.toString() + " " + lMayAlias);
  return lMayAlias;
} // mayAlias

//##52 BEGIN

/**
 * Get the set of symbols that may be aliased to pSym.
 * The returned result should not be changed (do not add elements, ...)
 * because the returned result may be shared with others. //##62
 * @param pSym symbol to be examined.
 * @return the set of symbols that may be aliased to pSym.
 */
public Set
aliasSyms( Sym pSym ) //##25
{
  //##62 Set lSymGroup = new HashSet();
  Set lSymGroup = fEmptySet;
  if (fAliasGroupOfSym.containsKey(pSym)) {
    //##62 lSymGroup.addAll((Set)fAliasGroupOfSym.get(pSym));
    lSymGroup = (Set)fAliasGroupOfSym.get(pSym); //##62
  }
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(4, "aliasSyms", pSym.toString()
                         + " " + lSymGroup);
  return lSymGroup;
} // aliasSymGroup

/**
 * Get the symbols aliased with the symbols in pSet.
 * (The result always cover pSet.) //##84
 * @param pSet Set of symbols to be examined.
 * @return the set of symbols aliased with some of pSet.
 */
public Set
aliasSymGroup( Set pSet ) //##52
{
  Set lSymGroup = new HashSet();
  Sym lSym;
  for (Iterator lIterator = pSet.iterator();
       lIterator.hasNext(); ) {     //##62 //REFINE loop
    lSym = (Sym)lIterator.next();
    if (fAliasGroupOfSym.containsKey(lSym))
      lSymGroup.addAll( (Set) fAliasGroupOfSym.get(lSym));
    lSymGroup.add(lSym); //##84
  }
  if (fDbgLevel > 2) //##62
    ioRoot0.dbgAlias.print(3, "aliasSymGroup", pSet.toString()
                         + "=" + lSymGroup);
  return lSymGroup;
} // aliasSymGroup

public Set
aliasExps( Exp pExp ) //##55
{
  Exp lAliasNode;
//  Set lAliasNodeSet = fAliasAnalLevel1.getAliasGroupFor(pExp);
  Set lAliasNodeSet = fAliasAnal.getAliasGroupFor(pExp); // 2004.09.02 S.Noishi  Set lAliasExpSet = new HashSet();
  Set lAliasExpSet = new HashSet();
  FlowAnalSym lSym;
  if (lAliasNodeSet != null) {
    for (Iterator lNodeIter2 = lAliasNodeSet.iterator();
         lNodeIter2.hasNext(); ) {
      lAliasNode = (Exp) lNodeIter2.next();
      lSym = lAliasNode.getSymOrExpId();
      if (lSym != null)
        lAliasExpSet.add(lSym);
     }
  }
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(4, "aliasExps", pExp.toStringShort()
                         + "=" + lAliasExpSet);
  return lAliasExpSet;
} // aliasExps

public Set
aliasExpGroup( Set pNodeSet ) //##55
{
  Set lExpGroup = new HashSet();
  Set lAliasNodeSet;
  HIR lHir;
  Exp lAliasNode;
  FlowAnalSym lSym;
  for (Iterator lIterator = pNodeSet.iterator();
       lIterator.hasNext(); ) {
    lHir = (HIR)lIterator.next();
    if (lHir instanceof Exp) {
//      lAliasNodeSet = fAliasAnalLevel1.getAliasGroupFor((Exp)lHir);
      lAliasNodeSet = fAliasAnal.getAliasGroupFor((Exp)lHir);  // 2004.09.02 S.Noishi
      if (lAliasNodeSet != null) {
        for (Iterator lNodeIter2 = lAliasNodeSet.iterator();
             lNodeIter2.hasNext(); ) {
          lAliasNode = (Exp) lNodeIter2.next();
          lSym = lAliasNode.getSymOrExpId();
          if (lSym != null)
            lExpGroup.add(lSym);
        }
      }
    }
  }
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(4, "aliasExpGroup", pNodeSet.toString()
                         + "=" + lExpGroup);
  return lExpGroup;
} // aliasExpGroup

//##52 END


/** AliasGroupInSubp
 * Compute alias group in a subprogram.
 */
private class
AliasGroupInSubp extends HirVisitorModel2
{

/** AliasGroupInSubp
 * Compute alias group in the subprogram pSubpDef.
 * @param pAliasAnal Instance of AliasAnalHir1.
 * @pSubpDef Subprogram definition subtree.
**/
private
AliasGroupInSubp(AliasAnalHir1 pAliasAnal, SubpDefinition pSubpDef)
{
  super(pAliasAnal.hirRoot);
  ioRoot0.dbgAlias.print(3, "AliasGroupInSubp", pSubpDef.toString());
}

/** atAssignStmt
 * Record LHS (left hand side) variable and LHS node
 * to fSetLvalueNodes which maps variable to the set of VarNodes
 * used to set value to the variable.
 * Process children before return.
 * @param pStmt assignment statement.
**/
public void
atAssignStmt( AssignStmt pStmt )
{
  Var lVar = null;
  Exp lValueExp = (Exp)pStmt.getChild1();
  VarNode lVarNode = getLvalueSymNode(lValueExp);
  if (lVarNode instanceof VarNode) {
    if (fDbgLevel > 3) //##62
      ioRoot.dbgAlias.print(4, "atAssignStmt", lVarNode.toStringShort());
    lVar = (Var)lVarNode.getSymNodeSym();
    // recordLvalueNodeForSym(lVar, lValueExp, fSetLvalueNodes);
    recordLvalueNodeForSym(lVar, lValueExp, fNodesOfSym);
  }
  visitChildren(pStmt);
} // atAssignStmt

/** atFunctionExp
 * Set flag fCallIncluded and process children.
 * @param pExp function expression.
**/
public void
atFunctionExp( FunctionExp pExp )
{
  if (fDbgLevel > 3) //##62
    ioRoot.dbgAlias.print(4, "atFunctionExp", pExp.toStringShort());
  //##71 fCallIncluded = true;
  //##71 BEGIN
  SymNode lSubpNode = null;
  if (pExp.getChild1() instanceof SymNode)
    lSubpNode = (SymNode)pExp.getChild1();
  else if ((pExp.getChild1().getOperator() == HIR.OP_ADDR)&&
            (pExp.getChild1().getChild1() instanceof SymNode))
    lSubpNode = (SymNode)pExp.getChild1().getChild1();
  if ((lSubpNode == null)||
      (! symRoot.sourceLanguage.functionsWithoutSideEffect.
       contains(lSubpNode.getSymNodeSym().getName()))) {
    fCallWithSideEffectIncluded = true;
    if (fDbgLevel > 3) //##62
      ioRoot.dbgAlias.print(4, pExp.toStringShort(), "may change global variables.");
  }else {
    ioRoot.dbgAlias.print(2, pExp.toStringShort(), "has no side effect.");
  }
  //##71 END
  visitChildren(pExp);
} // atFunctionExp

/** atVarNode
 * Record the symbol of pVarNode to fSetOfSyms.
 * If pVarNode is not recorded as set-node, then it is
 * assumed to be a ref-node and record (variable, pVarNode)
 * to fRefNodesOfSym which maps a variable to the set of
 * nodes refering the value of the variable.
**/
public void
atVarNode( VarNode pVarNode )
{
  if (fDbgLevel > 3) //##62
    ioRoot.dbgAlias.print(4, "atVarNode", pVarNode.toStringShort());
  Var lVar = (Var)pVarNode.getSymNodeSym();
  fSetOfSyms.add(lVar);
//  if ((! fSetLvalueNodes.containsKey(lVar))||
//      (((Set)fSetLvalueNodes.get(lVar)).contains(pVarNode)))
//    recordLvalueNodeForSym(lVar, pVarNode, fRefNodesOfSym);
  if ((! fNodesOfSym.containsKey(lVar))||
      (((Set)fNodesOfSym.get(lVar)).contains(pVarNode)))
     recordLvalueNodeForSym(lVar, pVarNode, fNodesOfSym);
} // atVarNode

/** atSubscriptedExp
 * If pExp is not recorded as set-node, then it is
 * assumed to be a ref-node and record the pair
 * (l-value symbol of child-1, pExp)
 * to fRefNodesOfSym which maps a variable to the set of
 * nodes refering the value of the variable.
 * Process children before return.
**/
public void
atSubscritedExp(SubscriptedExp pExp) //##55
{
  if (fDbgLevel > 3) //##62
    ioRoot.dbgAlias.print(4, "atSubscriptedExp", pExp.toStringShort());
  Sym lSym = getLvalueSymbol((Exp)pExp.getChild1());
//  if ((! fSetLvalueNodes.containsKey(lSym))||
//      (((Set)fSetLvalueNodes.get(lSym)).contains(pExp)))
//    recordLvalueNodeForSym(lSym, pExp, fRefNodesOfSym);
  if ((! fNodesOfSym.containsKey(lSym))||
      (((Set)fNodesOfSym.get(lSym)).contains(pExp)))
    recordLvalueNodeForSym(lSym, pExp, fNodesOfSym);
  visitChildren(pExp);
} // atSubscriptedExp

/** atQualifiedExp
 * If pExp is not recorded as set-node, then it is
 * assumed to be a ref-node and record the pair
 * (element symbol (child 2), pExp)
 * to fRefNodesOfSym.
 * Process children before return.
**/
public void
atQualifiedExp(QualifiedExp pExp)
{
  if (fDbgLevel > 3) //##62
    ioRoot.dbgAlias.print(4, "atQualifiedExp", pExp.toStringShort());
  Sym lSym = getLvalueSymbol((Exp)pExp.getChild2());
//  if ((! fSetLvalueNodes.containsKey(lSym))||
//      (((Set)fSetLvalueNodes.get(lSym)).contains(pExp)))
//    recordLvalueNodeForSym(lSym, pExp, fRefNodesOfSym);
  if ((! fNodesOfSym.containsKey(lSym))||
      (((Set)fNodesOfSym.get(lSym)).contains(pExp)))
    recordLvalueNodeForSym(lSym, pExp, fNodesOfSym);
  visitChildren(pExp);
} // atQualifiedExp

/** atPointedExp
 * Assumed pExp to be a ref-node and record the pair
 * (pointer symbol (child 1), pExp)
 * to fRefNodesOfSym.
 * Process children before return.
**/
public void
atPointedExp(PointedExp pExp)
{
  if (fDbgLevel > 3) //##62
    ioRoot.dbgAlias.print(4, "atPointedExp", pExp.toStringShort());
  Sym lSym = getLvalueSymbol((Exp)pExp.getChild1());
  // recordLvalueNodeForSym(lSym, pExp, fRefNodesOfSym);
  recordLvalueNodeForSym(lSym, pExp, fNodesOfSym);
  visitChildren(pExp);
} // atPointedExp

/** atExp
 * If OP_ADDR, then record l-value variable to fAddressTakenVars
 *   and record (l-value variable, pExp) to fRefNodesOfSym.
 * If OP_CONTENTS or OP_UNDECAY, then
 *   record (l-value variable, pExp) to fRefNodesOfSym.
 * Process children before return.
**/
public void
atExp(Exp pExp)
{
  if (fDbgLevel > 3) //##62
    ioRoot.dbgAlias.print(4, "atExp", pExp.toStringShort());
  Sym lSym;
  Exp lChild;
  int lVisibility;
  switch (pExp.getOperator()) {
  case HIR.OP_ADDR:
    lChild = (Exp)pExp.getChild1();
    lSym = getLvalueSymbol(lChild);
    if (lSym instanceof Var) {
      fAddressTakenVars.add(lSym);
//      if ((! fSetLvalueNodes.containsKey(lSym))||
//          (((Set)fSetLvalueNodes.get(lSym)).contains(pExp)))
//        recordLvalueNodeForSym(lSym, pExp, fRefNodesOfSym);
      if ((! fNodesOfSym.containsKey(lSym))||
          (((Set)fNodesOfSym.get(lSym)).contains(pExp)))
        recordLvalueNodeForSym(lSym, pExp, fNodesOfSym);
    }
    visitChildren(pExp);
    break;
  case HIR.OP_CONTENTS:
  case HIR.OP_UNDECAY:
    lSym = getLvalueSymbol((Exp)pExp.getChild1());
    // recordLvalueNodeForSym(lSym, pExp, fRefNodesOfSym);
    recordLvalueNodeForSym(lSym, pExp, fNodesOfSym);
    visitChildren(pExp);
    break;
  default:
    visitChildren(pExp);
  }
} // atExp

} // AliasGroupInSubp

/** computeAliasGroupForSyms:
 * Compute alias group for all variables ppeared in the subprogram.
 * Computation is done for fSetAliasGroupOfSym and fRefAliasGroupOfSym
 * and then they are combined as fAliasGroupOfSym.
 * Initial values for fSetAliasGroupOfSym and fRefAliasGroupOfSym are
 * set of l-value variables computed from fSetLvalueNodes and
 * fRefNodesOfSym respectively.
 *
**/
protected void
computeAliasGroupForSyms() //##55
{
  Var lVar;
  if (fDbgLevel > 0) //##62
    ioRoot0.dbgAlias.print(2, "computeAliasGroupOfSyms", fSetOfSyms.toString());
  // fAliasGroupOfSym.clear();
  for (Iterator lIterator = fSetOfSyms.iterator();
       lIterator.hasNext(); ) { //##62 //## REFINE loop
    lVar = (Var)lIterator.next();
    Set lAliasNodes = new HashSet();
    /*
    if (fSetAliasGroupOfSym.containsKey(lVar))
      lAliasNodes.addAll((Set)fSetAliasGroupOfSym.get(lVar));
    if (fRefAliasGroupOfSym.containsKey(lVar))
      lAliasNodes.addAll((Set)fRefAliasGroupOfSym.get(lVar));
     */
//    if (fAliasGroupOfSym.containsKey(lVar))
//      lAliasNodes.addAll((Set)fAliasGroupOfSym.get(lVar));
    if (fNodesOfSym.containsKey(lVar))
            lAliasNodes.addAll((Set)fNodesOfSym.get(lVar));
    if (fDbgLevel > 3) //##62
      ioRoot0.dbgAlias.print(5, " nodes of var " + lVar.toString() + lAliasNodes.toString());
    fNodesOfSym.put(lVar, lAliasNodes);
  }
  computeAliasFromNodeSet(fNodesOfSym, fAliasGroupOfSym);
  for (Iterator lIterator = fAddressTakenVars.iterator();
       lIterator.hasNext(); ) {
    lVar = (Var)lIterator.next();
    if (fAliasGroupOfSym.containsKey(lVar))
      fAliasOfAddressTakenVars.addAll((Set)fAliasGroupOfSym.get(lVar));
  }
} // computeAliasGroupOfVar

/** computeAliasFromNodeSet
 * Compute pAliasMap from pNodeMap, that is,
 * compute the set of aliases for each variable from pNodeMap
 * (variable, set of nodes using the variable)
 * and make pAliasMap which is a map
 * (variable, set of symbols aliased to the variable)
 * @param pNodeMap maps variable to set of nodes using the variable.
 * @param pAliasMap maps variable to set of symbols aliased to the variable.
**/
private void
computeAliasFromNodeSet( Map pNodeMap, Map pAliasMap )  //##55
{
  if (fDbgLevel > 1) //##67
    ioRoot0.dbgAlias.print(2, "computeAliasFromNodeSet", " ");
  if (fDbgLevel > 3) //##67
    printMapShort(" nodeMap ", pNodeMap);
  Set lSetSyms = pNodeMap.keySet();
  Sym lKeySym, lSym, lAliasSym;
  Exp lExpNode, lAliasNode;
  for (Iterator lIterator = lSetSyms.iterator();
       lIterator.hasNext(); ) {
    lKeySym = (Var)lIterator.next();
    Set lSetNodes = (Set)pNodeMap.get(lKeySym);
    if (fDbgLevel > 3) //##62
      ioRoot0.dbgAlias.print(5, " keySym " + lKeySym.getName() + lSetNodes.toString());
    Set lSymSet = new HashSet();
    for (Iterator lNodeIter = lSetNodes.iterator();
         lNodeIter.hasNext(); ) {
      lExpNode = (Exp)lNodeIter.next();
      lSym = getLvalueSymbol(lExpNode);
      if (lSym != null)
        lSymSet.add(lSym);
      if (fDbgLevel > 3) //##62
        ioRoot0.dbgAlias.print(4, " expNode " + lExpNode.toStringShort());
     // if (fAliasAnalLevel1.hasAliasInf(lExpNode)) {
       // Set lAliasNodes = fAliasAnalLevel1.getAliasGroupFor(lExpNode); //Ogasawara 040830
//     Set lAliasNodes = fAliasAnalLevel1.getAliasGroupFor((Exp)ChangeLvalue(lExpNode)); //Ogasawara 040830
     Set lAliasNodes = fAliasAnal.getAliasGroupFor((Exp)ChangeLvalue(lExpNode)); //Ogasawara 040830 // 2004.09.02 S.Noishi
        if (lAliasNodes != null) {
          if (fDbgLevel > 3) //##67
            System.out.print(" aliasNodes of " + lAliasNodes + "\n");
          for (Iterator lNodeIter2 = lAliasNodes.iterator();
               lNodeIter2.hasNext(); ) {
            lAliasNode = (Exp) lNodeIter2.next();
            lAliasSym = getLvalueSymbol(lAliasNode);
            if (lAliasSym != null)
              lSymSet.add(lAliasSym);
          }
        }
      // }
    }
    pAliasMap.put(lKeySym, lSymSet);
  }
  if (ioRoot0.dbgAlias.getLevel() >= 3)
    printMapShort(" aliasMap", pAliasMap);
} // computeAliasFromNodeSet

//Ogasawara 040830 begin
public HIR  ChangeLvalue(HIR pHir)
{

  HIR lHir;
  if (pHir == null)
    return null;
  switch (pHir.getOperator()) {
  case HIR.OP_VAR:
  case HIR.OP_ELEM:
  case HIR.OP_SUBP:
      lHir = pHir;
      break;
  case HIR.OP_SUBS:
  case HIR.OP_ADDR:
  case HIR.OP_UNDECAY:
      lHir = ChangeLvalue((HIR)pHir.getChild1());
    break;
  case HIR.OP_QUAL:
  case HIR.OP_ARROW:
      lHir = pHir;
    break;
  default:
      lHir = pHir;
  }
  return lHir;
}
//Ogasawara 040830 End


/** getLvalueSymbol
 *  @param pHir l-value node.
 *  @return the l-value symbol of pHir,
 *      or return null if pHir has no l-value symbol.
**/
public Sym
getLvalueSymbol( HIR pHir )
{
  Sym lSym;
  if (pHir == null)
    return null;
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(5, "getLvalueSymbol", pHir.toStringShort());
  switch (pHir.getOperator()) {
  case HIR.OP_VAR:
  case HIR.OP_ELEM:
  case HIR.OP_SUBP:
      lSym = (Sym)((SymNode)pHir).getSymNodeSym();
      break;
  case HIR.OP_SUBS:
  case HIR.OP_ADDR:
  case HIR.OP_UNDECAY:
    lSym = getLvalueSymbol((HIR)pHir.getChild1());
    break;
  case HIR.OP_QUAL:
  case HIR.OP_ARROW:
    lSym = getLvalueSymbol((HIR)pHir.getChild2());
    break;
  default:
    lSym = null;
  }
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(5, " " + ioRoot0.toStringObject(lSym));
  return lSym;
} // getLvalueSymbol

/** getLvalueSymNode
**/
public VarNode
getLvalueSymNode( HIR pHir )
{
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(5, "getLvalueSymNode", pHir.toStringShort());
  if (pHir instanceof VarNode)
    return (VarNode)pHir;
  switch (pHir.getOperator()) {
  case HIR.OP_SUBS:
  case HIR.OP_ADDR:
  case HIR.OP_UNDECAY:
    return getLvalueSymNode((HIR)pHir.getChild1());
  case HIR.OP_QUAL:
  case HIR.OP_ARROW:
    return getLvalueSymNode((HIR)pHir.getChild2());
  default:
    return null;
  }
} // getLvalueSymNode

/** recordLvalueNodeForSym
 * Record pExp to the set of l-value nodes
 * corresponding to the variable pSym.
 * @param pSym variable symbol.
 * @param pExp variable node that uses pSym.
 * @param pMap map showing the correspondence between
 *    the variable and set of variable nodes.
**/
void
recordLvalueNodeForSym( Sym pSym, Exp pExp, Map pMap )
{
  if ((pSym == null)||(pExp == null))
    return;
  if (! pMap.containsKey(pSym))
    pMap.put(pSym, new HashSet());
  Set lNodeSet = (Set)pMap.get(pSym);
  lNodeSet.add(pExp);
  if (fDbgLevel > 3) //##62
    ioRoot0.dbgAlias.print(5, " addNode ", lNodeSet.toString());
} // recordLvalueNodeForSym

/** recordAliasOfExp:
**/
/*
void
recordAliasOfExp( Sym pSym, Exp pExp )
{
  Set lAliasGroup;
  if (! fAliasGroupOfSym.containsKey(pSym))
    fAliasGroupOfSym.put(pSym, new HashSet());
  lAliasGroup = (Set)fAliasGroupOfSym.get(pSym);
  lAliasGroup.addAll(fAliasAnalLevel1.getAliasGroupFor(pExp));
} // recordAliasOfExp
*/

public void
printMapShort(String pHeader, Map pMap )
{
  Set lValueSet;
  Object lKey;
  System.out.print(pHeader + "\n"); //##62
  if (pMap != null) {
    Set lKeySet = pMap.keySet();
    for (Iterator lIterator = lKeySet.iterator();
         lIterator.hasNext(); ) {
      lKey = lIterator.next();
      if ((lKey instanceof Var)||
          (lKey instanceof HIR)) {
        if (lKey instanceof Var) {
          System.out.print(" " + ( (Var) lKey).getName() + "=[");
          lValueSet = (Set) pMap.get( (Var) lKey);
        }else {
          System.out.print(" " + ( (HIR) lKey).toStringShort() + "=[");
          lValueSet = (Set) pMap.get( (HIR) lKey);
        }
        for (Iterator lIterator2 = lValueSet.iterator();
             lIterator2.hasNext(); ) {
          Object lObject = lIterator2.next();
          if (lObject instanceof Sym)
            System.out.print(((Sym)lObject).getName() + " ");
          else if (lObject instanceof HIR)
            System.out.print(((HIR)lObject).toStringShort() + " ");
        }
        System.out.print("]\n"); //##62
      }
    }
  }
} // printMapShort

//##62 BEGIN
/** simplifiedAliasGrouping is selected for subprograms with
 * large complexity level (pSubpFlow.getComplexityLevel()) > 1).
 * It neither computes point-to graph nore scans the
 * subprogram.
 * For each variable used in the subprogram, its alias group
 * contains
 *   all address-taken-variables if the variablbe is taken
 *     its address
 *   all global variables if the variable is a formal parameter
 *     or the subprogram contains call and the variable is
 *     global.
 * @param pSubpFlow
 */
protected void
  simplifiedAliasGrouping( SubpFlow pSubpFlow )
{
  ioRoot0.dbgAlias.print(2, "\nsimplifiedAliasGrouping",
     pSubpFlow.getSubpSym().getName() + "\n");
  FlowAnalSym lSym;
  Var lVar, lKeyVar;
  fSetOfGlobalVariables = pSubpFlow.setOfGlobalVariables();
  fAddressTakenVars = pSubpFlow.setOfAddressTakenVariables();
  //##62 for (Iterator lSymIt = pSubpFlow.getSymIndexTable().iterator();lSymIt.hasNext(); )
  for (int lSymIndex = 0; lSymIndex < pSubpFlow.getSymExpCount();
       lSymIndex++) { //##62
    //##62 lSym = (FlowAnalSym)lSymIt.next();
    lSym = pSubpFlow.getIndexedSym(lSymIndex); //##62
    if (lSym instanceof Var) {
      fSetOfSyms.add(lSym);
    }
  }
  for (Iterator lSymIt2 = fSetOfSyms.iterator();
         lSymIt2.hasNext(); ) {
    lKeyVar = (Var)lSymIt2.next();
    if (! fAliasGroupOfSym.containsKey(lKeyVar)) {
      Set lAliasGroup = new HashSet();
      if (fAddressTakenVars.contains(lKeyVar)) {
          lAliasGroup.addAll(fAddressTakenVars);
      }
      if ((pSubpFlow.hasCall() &&
           fSetOfGlobalVariables.contains(lKeyVar))||
          (lKeyVar instanceof Param)) {
        lAliasGroup.addAll(fSetOfGlobalVariables);
      }
      fAliasGroupOfSym.put(lKeyVar, lAliasGroup);
    }
    if (ioRoot0.dbgAlias.getLevel() >= 3)
      printMapShort("fAliasGroupOfSym", fAliasGroupOfSym);
  }
} // simplifiedAliasGrouping

//##62 END

} // RecordAlias class
