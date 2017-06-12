package coins.opt;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
import coins.SymRoot;
import coins.sym.Sym;
import coins.sym.Var;
import coins.flow.BBlock;
import coins.flow.BBlockSubtreeIterator;
import coins.flow.BBlockVector;
import coins.flow.SubpFlow;
import coins.flow.SubpFlowImpl;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpStmt;
import coins.ir.hir.ForStmt;
import coins.ir.hir.FunctionExp;
import coins.ir.hir.HIR;
import coins.ir.hir.HirIterator;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.LoopStmt;
import coins.ir.hir.Stmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.SubscriptedExp;
import coins.ir.hir.VarNode;

/**
 * PresHir
 */
public class PresrHir {
  HirRoot hirRoot;
  IoRoot ioRoot;
  FlowRoot flowRoot;
  SymRoot symRoot;
  Sym sym;
  SubpFlow subpFlow;
  SubpDefinition subpDef;
  int traceLevel;
  //
  HashMap backedgeMap;
  HashMap loopMap;

  /**
   * Constructor
   * @param pSubpFlow SubpFlow instance
   * @param pSubpDef SubpDef instance
   */
  PresrHir(SubpFlow pSubpFlow, SubpDefinition pSubpDef) {
    flowRoot = ((SubpFlowImpl)pSubpFlow).flowRoot;
    hirRoot = flowRoot.hirRoot;
    ioRoot = flowRoot.ioRoot;
    symRoot = flowRoot.symRoot;
    sym = symRoot.sym;
    subpFlow = pSubpFlow;
    subpDef = pSubpDef;
    traceLevel = ioRoot.dbgHir.getLevel();
  }

  /**
   * Main method of PresrHir
   */
  public boolean doIt() {
    // Show the control flow
    if(this.traceLevel > 0) {
      coins.flow.ShowControlFlow flow = flowRoot.controlFlow.getShowControlFlow();
      flow.showAll();
    }
    // Show SubpDefinition
    if(this.traceLevel > 0) {
      ioRoot.printOut.print("### Before Unroll Loop:\n");
      if(subpDef == null) {
        ioRoot.printOut.print("    SubpDefinition : Null\n");
      } else {
        subpDef.print(2, true);
      }
    }
    //
    backedgeMap = new HashMap();
    loopMap = new HashMap();
    // Look up back edges.
    lookupBackedges();
    // Find natural loops, and merge loops.
    findLoops();
    // Make a loop tree.
    mkLoopTree();
    // Find induction variables for loops
    detectIndVars();
    // Pick up array references
    HashMap arefMap = pickupARefs();
    // Calculae maximum distance of array refs.
    calcArefDist();
    // Show loop trees
    if(this.traceLevel > 0) {
      showLoopInfo(arefMap);
    }
    // Unroll Loops
    unrollLoops();
    //
    if(this.traceLevel > 0) {
      ioRoot.printOut.print("### After Unroll Loop:\n");
      if(subpDef == null) {
        ioRoot.printOut.print("    SubpDefinition : Null\n");
      } else {
        boolean finishResult = subpDef.finishHir();
        ioRoot.printOut.print("### finishHir : "+finishResult+"\n");
        subpDef.print(2, true);
      }
    }
    return true;
  }

  /**
   *  Find induction variabes
   */
  private void detectIndVars() {
    for(Iterator it = loopMap.values().iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      lp.detectIndVars();
    }
  }

  /**
   *  Look up back edges.
   */
  private void lookupBackedges() {
    int blkcnts = subpFlow.getNumberOfBBlocks();
    for(int i =1; i<=blkcnts; i++) {
      BBlock blk = subpFlow.getBBlock(i);
      List preds = blk.getPredList();
      for(Iterator it = preds.iterator(); it.hasNext();) {
        BBlock p = (BBlock)it.next();
        if(isDominated(p, blk)) setBackEdge(blk, p);
      }
    }
  }

  private boolean isDominated(BBlock blk1, BBlock blk2) {
    List domList = subpFlow.getDominatorList(blk1);
    return domList.contains(blk2);
  }

  /**
   *  Find natural loops
   */
  private void findLoops() {
    Set keys = backedgeMap.keySet();
    for(Iterator it = keys.iterator();it.hasNext();) {
      BBlock blk = (BBlock)it.next();
      HashSet bes = (HashSet)backedgeMap.get(blk);
      for(Iterator beit = bes.iterator(); beit.hasNext();) {
        BBlock srcblk = (BBlock)beit.next();
        Loop lp = new Loop(blk, srcblk);
        // Merge loops
        if(loopMap.containsKey(blk)) {
          Loop origLp = (Loop)loopMap.get(blk);
          lp = origLp.merge(lp);
        }
        loopMap.put(blk, lp);
      }
    }
  }

  /**
   *  Make a loop tree
   */
  private void mkLoopTree() {
    // Check the inclusion between loops
    Collection lps = loopMap.values();
    for(Iterator it = lps.iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      for(Iterator itsub = lps.iterator(); itsub.hasNext();) {
        Loop lp2 = (Loop)itsub.next();
        if(lp == lp2) continue;
        if(lp.include(lp2)) {
          lp.addDescendent(lp2);
          lp2.addAncestor(lp);
        }
      }
    }
    // Set parents
    //   Split roots
    ArrayList roots = loopRoots();
    //   Set nest levels to the descendents
    for(Iterator rtit = roots.iterator(); rtit.hasNext();) {
      Loop lp = (Loop)rtit.next();
      setNestLevels(lp, 1);
    }
    //  Set parent to loops
    for(Iterator pit = lps.iterator(); pit.hasNext();) {
      Loop ploop = (Loop)pit.next();
      setParentToLoop(ploop, ploop.nestLevel);
    }
    //   Set his/her children to a parent
    for(Iterator it = lps.iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      if(lp.parent == null) continue;
      lp.parent.addChild(lp);
    }
  }

  /**
   *  Show loop infos
   */
  private void showLoopInfo(HashMap refMap) {
    showLoopChildren();
    showLoopParent();
    showLoopTrees();
    showArrayRefs(refMap);
    showDistance();
  }

  private void showLoopParent() {
    Collection lps = loopMap.values();
    ioRoot.printOut.print("### Show Loop Parent (Begin)\n");
    for(Iterator it = lps.iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      Loop parent = lp.parent;
      if(parent == null) {
        ioRoot.printOut.print(lp.header.getBlockNumber()+" has no parent.\n");
        continue;
      }
      ioRoot.printOut.print(parent.header.getBlockNumber()
                            +" is the parent of "+lp.header.getBlockNumber()+"\n");
    }
    ioRoot.printOut.print("### Show Loop Parent (End)\n");
  }

  private void showLoopTrees() {
    ioRoot.printOut.print("### Show Loop Tree (Begin) ###\n");
    ArrayList roots = loopRoots();
    for(Iterator it = roots.iterator(); it.hasNext();) {
      Loop lp =(Loop)it.next();
      showLoopTree(lp, 0);
    }
    ioRoot.printOut.print("### Show Loop Tree (End) ###\n");
  }

  private void showLoopTree(Loop lp, int dth) {
    for(int i = 0; i <= dth; i++) {
      ioRoot.printOut.print("  ");
    }
    BBlock hd = lp.header;
    // Print block number of members
    ioRoot.printOut.print(hd.getBlockNumber()+"(nest level="+lp.nestLevel+"): ");
    for(Iterator it = lp.members.iterator(); it.hasNext();) {
      BBlock blk = (BBlock)it.next();
      ioRoot.printOut.print(blk.getBlockNumber()+", ");
    }
    ioRoot.printOut.print("\n");
    if(lp.children == null) return;
    for(Iterator it = lp.children.iterator(); it.hasNext();) {
      Loop chlp = (Loop)it.next();
      showLoopTree(chlp, dth+1);
    }
  }

  private void showLoopChildren() {
    ioRoot.printOut.print("### Show Loop Children (Begin) ###\n");
    Collection lps = loopMap.values();
    for(Iterator it = lps.iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      BBlock hd = lp.header;
      ioRoot.printOut.print(hd.getBlockNumber()+":");
      if(lp.children == null) {
        ioRoot.printOut.print("\n");
        continue;
      }
      for(Iterator chit = lp.children.iterator(); chit.hasNext();){
        Loop chlp = (Loop)chit.next();
        ioRoot.printOut.print(chlp.header.getBlockNumber()+", ");
      }
      ioRoot.printOut.print("\n");
    }
    ioRoot.printOut.print("### Show Loop Children (End) ###\n");
  }

  private void showDistance() {
    ioRoot.printOut.print("### Show Distance ###\n");
    for(Iterator it = loopMap.values().iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      ioRoot.printOut.print("Loop " + lp.header.getBlockNumber() + " : " 
                            + lp.maxDist + "\n");
    }
  }

  private ArrayList loopRoots() {
    ArrayList roots = new ArrayList();
    if(loopMap == null) return roots;
    Collection lps = loopMap.values();
    for(Iterator it = lps.iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      if(lp.ancestors == null || lp.ancestors.isEmpty()) roots.add(lp);
    }
    return roots;
  }

  private void setNestLevels(Loop lp, int n) {
    if(lp.nestLevel < n) {
      lp.nestLevel = n;
      HashSet ds = lp.descendents;
      if(ds == null) return;
      int n1 = n+1;
      for(Iterator it = ds.iterator(); it.hasNext();) {
        Loop dlp = (Loop)it.next();
        setNestLevels(dlp, n1);
      }
    }
  }

  private void setParentToLoop(Loop lp, int level) {
    HashSet ds = lp.descendents;
    if(ds == null) return;
    int level1 = level + 1;
    for(Iterator it = ds.iterator(); it.hasNext();) {
      Loop lp2 = (Loop)it.next();
      if(lp2.nestLevel == level1) {
        lp2.parent = lp;
      }
    }
  }

  private void setBackEdge(BBlock tgt, BBlock src) {
    HashSet hs = (HashSet)backedgeMap.get(tgt);
    if(hs == null) {
      hs = new HashSet();
      backedgeMap.put(tgt, hs);
    }
    hs.add(src);
  }

  private HashMap pickupARefs() {
    // refMap : (key : loop, value : HashMap)
    HashMap refMap = new HashMap();
    for(Iterator it = loopMap.values().iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      HashMap refs = lp.pickupARefs();
      refMap.put(lp, refs);
    }
    return refMap;
  }

  private void calcArefDist() {
    for(Iterator it = loopMap.keySet().iterator(); it.hasNext();) {
      BBlock blk = (BBlock)it.next();
      Loop lp = (Loop)loopMap.get(blk);
      lp.calcArefDist();
    }
  }

  // refMap : HashMap(key : loop, 
  //                  value : HashMap(key : BBlock,
  //                                  value : HashMap(key : Var,
  //                                                  value : ArrayList(SubscriptedExp)))
  private void showArrayRefs(HashMap refMap) {
    ioRoot.printOut.print("### Show Array References ###\n");
    for(Iterator it = refMap.keySet().iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      ioRoot.printOut.print("#### Pickup Refs Loop : "+ lp.header.getBlockNumber() + "\n");
      HashMap refs = (HashMap)refMap.get(lp);
      if(refs == null) {
        ioRoot.printOut.print("  Empty\n");
        continue;
      }
      for(Iterator it1 = refs.keySet().iterator(); it1.hasNext();) {
        BBlock blk = (BBlock)it1.next();
        HashMap arefMap = (HashMap)refs.get(blk);
        ioRoot.printOut.print("###### BBlock : "+ blk.getBlockNumber() + "\n");
        for(Iterator it2 = arefMap.keySet().iterator(); it2.hasNext();) {
          // key is an array part of SubscriptedExp
          Var v = (Var)it2.next();
          ioRoot.printOut.print(v.toString()+" : ");
          ArrayList alist = (ArrayList)arefMap.get(v);
          for(Iterator it3 = alist.iterator(); it3.hasNext();) {
            Exp aexp = (Exp)it3.next();
            ioRoot.printOut.print(aexp.toStringWithChildren() + ", ");
          }
          ioRoot.printOut.print("\n");
        }
      }
    }
  }

  private void unrollLoops() {
    LoopUnrolling unrol = new LoopUnrolling(hirRoot);
    for(HirIterator it = hirRoot.hir.hirIterator(subpDef.getHirBody());
        it.hasNextStmt();) {
      Stmt st = it.getNextStmt();
      if(st != null && st.getOperator() == HIR.OP_FOR) {
        Loop lp = getLoopInfo(st);
        if(lp == null) continue;
        int d = lp.maxDist;
        if(d <= 1) continue;
        unrol.setExpRate(d);
        if(unrol.isExpansible((ForStmt)st)) unrol.expandLoop((ForStmt)st);
      }
    }
  }

  private Loop getLoopInfo(Stmt st) {
    if(!(st instanceof LoopStmt)) {
      if(traceLevel > 0) {
        ioRoot.printOut.print("### Not LoopStmt:\n");
      }
      return null;
    }
    if(traceLevel > 0) {
      ioRoot.printOut.print("### LoopStmt:"+st.toStringWithChildren()+"\n");
    }
    Stmt lpst = ((LoopStmt)st).getLoopBackPoint();
    if(traceLevel > 0) {
      ioRoot.printOut.print("##### Loop Back Point Stmt:"+lpst.toStringWithChildren()+"\n");
    }
    for(Iterator it = loopMap.values().iterator(); it.hasNext();) {
      Loop lp = (Loop)it.next();
      BBlock blk = lp.getHeader();
      // Get the first element of blk
      BBlockSubtreeIterator blkit = blk.bblockSubtreeIterator(); 
      if(blkit.hasNext()) {
        HIR hir = (HIR)blkit.next();
        if(hir instanceof Stmt && isSameAs(lpst, hir)) {
          if(traceLevel > 0) {
            ioRoot.printOut.print("##### Matched:"+hir.toStringWithChildren()+"\n");
          }
          return lp;
        } else {
          if(traceLevel > 0) {
            ioRoot.printOut.print("##### Unmatched:"+hir.toStringWithChildren()+"\n");
          }
        }
      }
    }
    if(traceLevel > 0) {
      ioRoot.printOut.print("##### Unmatched\n");
    }
    return null;
  }

  // 
  boolean isSameAs(HIR h1, HIR h2) {
    if(h1 == null) return false;
    if(h1 instanceof LabeledStmt) return h1 == h2;
    return h1.isSameAs(h2);
  }

  class Loop {
    // header : Loop back block
    BBlock header;
    BBlock bkedgeSrc;
    HashSet members;
    Var variable;
    Loop parent;
    HashSet children;
    int nestLevel = 0;
    HashSet ancestors;
    HashSet descendents;
    HashMap arrayRefMap;
    Exp minAr;
    Exp maxAr;
    int maxDist;

    /**
     *  Constructor
     *  @param hblk BBlock(header)
     *  @param tblk BBlock
     */
    Loop(BBlock hblk, BBlock tblk) {
      header = hblk;
      bkedgeSrc = tblk;
      members = findLoopMems(hblk, tblk);
    }

    Loop(BBlock hblk, HashSet mems) {
      header = hblk;
      members = mems;
    }

    HashSet getMems() {
      return members;
    }

    boolean isProperMember(BBlock blk) {
      if(!this.members.contains(blk)) return false;
      if(this.children == null) return true;
      for(Iterator it = this.children.iterator(); it.hasNext();) {
        Loop chblk = (Loop)it.next();
        if(chblk.members.contains(blk)) return false;
      }
      return true;
    }

    BBlock getHeader() {
      return header;
    }

    void addChild(Loop child) {
      HashSet cs = this.children;
      if(cs == null) {
        cs = new HashSet();
        this.children = cs;
      }
      cs.add(child);
    }

    void addAncestor(Loop lp) {
      if(this.ancestors == null) {
        this.ancestors = new HashSet();
      }
      this.ancestors.add(lp);
    }

    void addDescendent(Loop lp) {
      if(this.descendents == null) {
        this.descendents = new HashSet();
      }
      this.descendents.add(lp);
    }

    boolean include(Loop lp) {
      BBlock hd = lp.header;
      return this.members.contains(hd);
    }

    Loop merge(Loop lp) {
      if(this.header != lp.getHeader()) return null;
      HashSet mems = lp.getMems();
      this.members.addAll(mems);
      return new Loop(header, members);
    }

    void detectIndVars() {
      if(traceLevel > 0) {
        ioRoot.printOut.print("### Detect Induction Variables ###\n");
      }
      ArrayList cand = new ArrayList();
      HashSet discand = new HashSet();
      ArrayList work = new ArrayList();
      work.add(this.header);
      HashSet chked = new HashSet();
      while(!work.isEmpty()) {
        BBlock blk = (BBlock)work.get(0);
        work.remove(blk);
        if(chked.contains(blk)) continue;
        chked.add(blk);
        // for debug
        if(traceLevel > 0) {
          ioRoot.printOut.print("### BBlock : ");
          ioRoot.printOut.print(blk.toString()+"\n");
        }
        if(isProperMember(blk)) {
          for(BBlockSubtreeIterator it = blk.bblockSubtreeIterator(); it.hasNext();) {
            HIR hir = (HIR)it.next();
            if(hir instanceof AssignStmt) {
              AssignStmt st = (AssignStmt)hir;
              Exp left = st.getLeftSide();
              if(!(left instanceof VarNode)) continue;
              Var v = ((VarNode)left).getVar();
              // for debug
              if(traceLevel > 0) {
                ioRoot.printOut.print("Ind.var assign:"+st.toStringWithChildren()+"\n");
              }
              // If v is defined more than once, get rid of it.
              if(discand.contains(v)) continue;
              if(cand.contains(v)) {
                cand.remove(v);
                discand.add(v);
                continue;
              }
              Exp right = st.getRightSide();
              int op = right.getOperator();
              if(op == HIR.OP_ADD || op == HIR.OP_SUB) {
                Exp e1 = right.getExp1();
                if(!(e1 instanceof VarNode)) continue;
                String vname1 = e1.getVar().getName();
                String vname2 = v.getName();
                if(vname1 ==null || vname2 == null || !vname1.equals(vname2)) continue;
                Exp e2 = right.getExp2();
                if(!e2.isEvaluable()) continue;
                cand.add(v);
                // Show the induction variable
                if(traceLevel > 0) {
                  ioRoot.printOut.print(st.toStringDetail()+"\n");
                }
              }
            }
          }
        }
        List succ = blk.getSuccList();
        work.addAll(succ);
      }
      // Set the induction variable in this loop.
      if(cand.size() == 1) {
        this.variable = (Var)cand.get(0);
      } else {
        // for debug
        if(traceLevel > 0) {
          ioRoot.printOut.print("##### Ind.Var cand:");
          for(Iterator candIt = cand.iterator(); candIt.hasNext();) {
            ioRoot.printOut.print((Integer)candIt.next()+", ");
          }
          ioRoot.printOut.print("\n");
        }
      }
      // for debug
      if(traceLevel > 0) {
        if(this.variable != null) {
          ioRoot.printOut.print("Ind.Var.: "+this.variable.getName()+"\n");
        } else {
          ioRoot.printOut.print("Ind.Var.: Null\n");
        }
        for(Iterator it = this.header.getPredList().iterator(); it.hasNext();) {
          BBlock pred = (BBlock)it.next();
          ioRoot.printOut.print("### Predecessor of Loop header ###\n");
          for(BBlockSubtreeIterator subit = subpFlow.bblockSubtreeIterator(pred); subit.hasNext();) {
            HIR hir = (HIR)subit.next();
            ioRoot.printOut.print(hir.toStringWithChildren()+"\n");
          }
        }
      }
    }

    HashMap pickupARefs() {
      // key : BBlock, value : HashMap(key : Var, value : ArrayList(SubscriptedExp)
      HashMap refs = new HashMap();
      this.arrayRefMap = refs;
      HashSet chker = new HashSet();
      ArrayList work = new ArrayList();
      work.add(this.header);
      while(!(work.isEmpty())) {
        BBlock blk = (BBlock)work.get(0);
        work.remove(blk);
        if(!this.members.contains(blk)) continue;
        if(chker.contains(blk)) continue;
        chker.add(blk);
        HashMap arefMap = new HashMap();
        refs.put(blk, arefMap);
        pickupARefs(blk, arefMap);
        if(blk == this.bkedgeSrc) continue;
        work.addAll(blk.getSuccList());
      }
      return refs;
    }

    private void pickupARefs(BBlock blk, HashMap arefMap) {
      for(BBlockSubtreeIterator it = blk.bblockSubtreeIterator(); it.hasNext();) {
        HIR hir = (HIR)it.next();
        // for debug
        if(traceLevel > 0) {
          ioRoot.printOut.print("## "+hir.toStringWithChildren()+"\n");
        }
        if(hir instanceof LabeledStmt) continue;
        if(hir instanceof AssignStmt) {
          AssignStmt assignSt = (AssignStmt)hir;
          Exp left = assignSt.getLeftSide();
          pickupARefsExp(left, arefMap);
          Exp right = assignSt.getRightSide();
          pickupARefsExp(right, arefMap);
          continue;
        }
        if(hir instanceof ExpStmt) {
          Exp exp = ((ExpStmt)hir).getExp();
          if(exp == null) continue;
          pickupARefsExp(exp, arefMap);
          continue;
        }
        continue;
      }
    }

    private void pickupARefsExp(Exp e, HashMap arefs) {
      switch(e.getOperator()) {
        case HIR.OP_SUBS:
          if(!(e instanceof SubscriptedExp)) break;
          SubscriptedExp se = (SubscriptedExp)e;
          Var av = getArrayVar(se);
          ArrayList alist = (ArrayList)arefs.get(av);
          if(alist == null) {
            alist = new ArrayList();
            arefs.put(av, alist);
          }
          alist.add(se);
          break;
        case HIR.OP_CALL:
          IrList list = ((FunctionExp)e).getParamList();
          // for debug
          if(traceLevel > 0) {
            ioRoot.printOut.print("## Call Exp : " + e.toStringWithChildren() + "\n");
          }
          for(Iterator listIt = list.iterator(); listIt.hasNext();) {
            IR ir = (IR)listIt.next();
            if(!(ir instanceof Exp)) continue;
            // for debug
            if(traceLevel > 0) {
              ioRoot.printOut.print("IR is an exp : " + ir.toString() + "\n");
            }
            pickupARefsExp((Exp)ir, arefs);
          }
          break;
        case HIR.OP_ADD:
        case HIR.OP_SUB:
        case HIR.OP_MULT:
        case HIR.OP_DIV:
        case HIR.OP_MOD:
        case HIR.OP_AND:
        case HIR.OP_OR:
        case HIR.OP_XOR:
        case HIR.OP_CMP_EQ:
        case HIR.OP_CMP_NE:
        case HIR.OP_CMP_GT:
        case HIR.OP_CMP_GE:
        case HIR.OP_CMP_LT:
        case HIR.OP_CMP_LE:
        case HIR.OP_SHIFT_LL:
        case HIR.OP_SHIFT_R:
        case HIR.OP_SHIFT_RL:
          pickupARefsExp(e.getExp1(), arefs);
          pickupARefsExp(e.getExp2(), arefs);
          break;
        case HIR.OP_NOT:
        case HIR.OP_NEG:
        case HIR.OP_ADDR:
          pickupARefsExp(e.getExp1(), arefs);
          break;
        default: break;
        }
    }

    private Var getArrayVar(SubscriptedExp e) {
      Exp ar = e.getArrayExp();
      if(ar instanceof VarNode) return ((VarNode)ar).getVar();
      if(ar instanceof SubscriptedExp) {
        return getArrayVar((SubscriptedExp)ar);
      }
      return null;
    }

    private int calcArefDist() {
      propagateArefs();
      // for debug
      if(traceLevel > 0) {
        ioRoot.printOut.print("### Calc Aref Dist. Blk : "+this.header.getBlockNumber()+" ###\n");
      }
      if(this.bkedgeSrc == null) return 0;
      HashMap refs = (HashMap)this.arrayRefMap.get(this.bkedgeSrc);
      if(refs == null) return 0;
      int dist = 0;
      for(Iterator it = refs.keySet().iterator(); it.hasNext();) {
        Var v = (Var)it.next();
        ArrayList alist = (ArrayList)refs.get(v);
        this.minAr = null;
        this.maxAr = null;
        minmax(alist, this.variable);
        // for debug
        if(traceLevel > 0) {
          ioRoot.printOut.print("### Min Max (Var = "+this.variable+") : ");
          if(this.minAr == null) {
            ioRoot.printOut.print("null, ");
          } else {
            ioRoot.printOut.print(this.minAr.toStringWithChildren()+", ");
          }
          if(this.maxAr == null) {
            ioRoot.printOut.print("null, ");
          } else {
            ioRoot.printOut.print(this.maxAr.toStringWithChildren()+", \n");
          }
        }
        //
        dist = max(dist, distance(this.minAr, this.maxAr));
        // for debug
        if(traceLevel > 0) {
          if(this.minAr != null && this.maxAr != null) {
            ioRoot.printOut.print("### distance("+this.minAr.toStringWithChildren()
                                  +", "+this.maxAr.toStringWithChildren()+")="+dist+"\n");
          } else {
            ioRoot.printOut.print("### distance="+dist+"\n");
          }
        }
      }
      this.maxDist = dist;
      return this.maxDist;
    }

    private int max(int x, int y) {
      if(x > y) return x;
      return y;
    }

    private void propagateArefs() {
      ArrayList work = new ArrayList();
      work.add(this.header);
      HashSet chker = new HashSet();
      while(!work.isEmpty()) {
        BBlock blk = (BBlock)work.get(0);
        work.remove(blk);
        // Check predecessors are all checked.
        if(blk != this.header) {
          List preds = blk.getPredList();
          boolean brkflg = false;
          for(Iterator it = preds.iterator(); it.hasNext();) {
            BBlock pblk = (BBlock)it.next();
            if(!this.members.contains(pblk)) continue;
            if(isDominated(blk, pblk) && !chker.contains(pblk)) {
              brkflg = true;
              break;
            }
          }
          if(brkflg) {
            work.add(blk);
            continue;
          }
        }
        if(chker.contains(blk)) continue;
        chker.add(blk);
        HashMap refsOrig = (HashMap)this.arrayRefMap.get(blk);
        if(refsOrig == null) {
          refsOrig = new HashMap();
          this.arrayRefMap.put(blk, refsOrig);
        }
        for(Iterator blkIt = blk.getSuccList().iterator(); blkIt.hasNext();) {
          BBlock sblk = (BBlock)blkIt.next();
          if(sblk == this.header) continue;
          if(!this.members.contains(sblk)) continue;
          HashMap refs = (HashMap)this.arrayRefMap.get(sblk);
          if(refs == null) {
            refs = new HashMap();
            this.arrayRefMap.put(sblk, refs);
          }
          for(Iterator keyIt = refsOrig.keySet().iterator(); keyIt.hasNext();) {
            Var v = (Var)keyIt.next();
            ArrayList alist = (ArrayList)refsOrig.get(v);
            ArrayList alist2 = (ArrayList)refs.get(v);
            if(alist2 == null) {
              alist2 = new ArrayList();
              refs.put(v, alist2);
            }
            alist2.addAll(alist);
          }
          List kids = blk.getSuccList();
          work.addAll(kids);
        }
      }
    }

    // Calculate the distance of two SubscriptedExp
    private int distance(Exp e1, Exp e2) {
      if(e1 == e2) return 0;
      if(e1 == null || e2 == null) return 0;
      if(!(e1 instanceof SubscriptedExp)) return 0;
      if(!(e2 instanceof SubscriptedExp)) return 0;
      SubscriptedExp se1 = (SubscriptedExp)e1;
      SubscriptedExp se2 = (SubscriptedExp)e2;
      Exp sc1 = se1.getSubscriptExp();
      Exp sc2 = se2.getSubscriptExp();
      // When Subscript parts are same, calculate the distance from Array parts
      if(sc1.isSameAs(sc2))
        return distance(se1.getArrayExp(), se2.getArrayExp());
      // Otherwise there must be difference between Subscript parts, so
      // calculate the distance from Subscript parts
      if(se1.getArrayExp().isSameAs(se2.getArrayExp()))
        return distsub(sc1, sc2);
      return 0;
    }

    private int distsub(Exp e1, Exp e2) {
      switch(e1.getOperator()) {
      case HIR.OP_CONV:
        return distsub(e1.getExp1(), e2);
      case HIR.OP_ADD: {
        switch(e2.getOperator()) {
        case HIR.OP_CONV:
          return distsub(e1, e2.getExp1());
        case HIR.OP_ADD:
          if(e1.getExp1().isSameAs(e2.getExp1()))
            return Math.abs(getConstVal(e1.getExp2(), 1)-getConstVal(e2.getExp2(), 1));
          return 0;
        case HIR.OP_SUB:
          if(e1.getExp1().isSameAs(e2.getExp1()))
            return Math.abs(getConstVal(e1.getExp2(), 1)+getConstVal(e2.getExp2(), 1));
          return 0;
        case HIR.OP_VAR:
          if(e1.getExp1().isSameAs(e2))
            return Math.abs(getConstVal(e1.getExp2(), 1));
          return 0;
        default:
          return 0;
        }
      }
      case HIR.OP_SUB: {
        switch(e2.getOperator()) {
        case HIR.OP_CONV:
          return distsub(e1, e2.getExp1());
        case HIR.OP_ADD:
          if(e1.getExp1().isSameAs(e2.getExp1()))
            return Math.abs(getConstVal(e1.getExp2(), 1)+getConstVal(e2.getExp2(), 1));
          return 0;
        case HIR.OP_SUB:
          if(e1.getExp1().isSameAs(e2.getExp1()))
            return Math.abs(getConstVal(e1.getExp2(), 1)-getConstVal(e2.getExp2(), 1));
          return 0;
        case HIR.OP_VAR:
          if(e1.getExp1().isSameAs(e2))
            return Math.abs(getConstVal(e1.getExp2(), 1));
          return 0;
        default:
          return 0;
        }
      }
      case HIR.OP_VAR: {
        switch(e2.getOperator()) {
        case HIR.OP_CONV:
          return distsub(e1, e2.getExp1());
        case HIR.OP_ADD:
        case HIR.OP_SUB:
          if(e1.isSameAs(e2.getExp1()))
            return Math.abs(getConstVal(e2.getExp2(), 1));
          return 0;
        default:
          return 0;
        }
      }
      default:
        return 0;
      }
    }

    // e is assumed to be Var or Var +/- Const or Const
    private int getConstVal(Exp e, int sign) {
      if(e instanceof ConstNode)
        return sign * ((ConstNode)e).getIntValue();
      if(e instanceof VarNode) return 0;
      switch(e.getOperator()) {
      case HIR.OP_CONV:
        return getConstVal(e.getExp1(), sign);
      case HIR.OP_ADD:
        if(!(e.getExp1() instanceof VarNode)) return 0;
        return getConstVal(e.getExp2(), sign);
      case HIR.OP_SUB:
        if(!(e.getExp1() instanceof VarNode)) return 0;
        return getConstVal(e.getExp2(), - sign);
      default:
        break;
      }
      return 0;
    }

    /*
     *  Comparee Exps and determine the minimum and the maximum among them.
     *  Local fields minAr and maxAr will be updated.
     */
    private void minmax(ArrayList alist, Var v) {
      Exp min = null;
      Exp max = null;
      if(alist == null || alist.size() == 0) {
        this.minAr = null;
        this.maxAr = null;
        return;
      }
      for(Iterator it = alist.iterator(); it.hasNext();) {
        SubscriptedExp e = (SubscriptedExp)it.next();
        if(!(containsIdx(e, v))) continue;
        if(min == null) {
          min = e;
          max = e;
          continue;
        }
        if(isSmaller(e, min)) {
          // for debug
          if(traceLevel > 0) {
            ioRoot.printOut.print("isSmaller : "+e.toStringWithChildren()
                                  +",  "+min.toStringWithChildren()+"\n");
          }
          min = e;
        } 
        if(isSmaller(max, e)) {
          // for debug
          if(traceLevel > 0) {
            ioRoot.printOut.print("isSmaller : "+max.toStringWithChildren()
                                  +",  "+e.toStringWithChildren()+"\n");
          }
          max = e;
        }
        // for debug
        if(traceLevel > 0) {
          ioRoot.printOut.print("isNotSmaller : "+e.toStringWithChildren()
                                +",  "+ min.toStringWithChildren()+"\n");
          ioRoot.printOut.print("isNotSmaller : "+max.toStringWithChildren()
                                +",  "+e.toStringWithChildren()+"\n");
        }
      }
      this.minAr = min;
      this.maxAr = max;
    }

    private boolean containsIdx(SubscriptedExp e, Var v) {
      // Check the subscript part
      Exp s = e.getSubscriptExp();
      if(containsIdxSub(s, v)) return true;
      // Check the array part
      Exp ar = e.getArrayExp();
      if(ar instanceof SubscriptedExp)
        return containsIdx((SubscriptedExp)ar, v);
      return false;
    }

    private boolean containsIdxSub(Exp e, Var v) {
      switch(e.getOperator()) {
      case HIR.OP_VAR:
        return (((VarNode)e).getVar() == v);
      case HIR.OP_ADD:
      case HIR.OP_SUB:
      case HIR.OP_SHIFT_LL:
      case HIR.OP_SHIFT_R:
      case HIR.OP_SHIFT_RL:
        return (containsIdxSub(e.getExp1(), v) || containsIdxSub(e.getExp2(), v));
      case HIR.OP_CONV:
      case HIR.OP_NOT:
      case HIR.OP_NEG:
      case HIR.OP_ADDR:
        return (containsIdxSub(e.getExp1(), v));
      default:
        break;
      }
      return false;
    }

    private boolean isSmaller(Exp e1, Exp e2) {
      if(!(e1 instanceof SubscriptedExp)) return false;
      if(!(e2 instanceof SubscriptedExp)) return false;
      SubscriptedExp se1 = (SubscriptedExp)e1;
      SubscriptedExp se2 = (SubscriptedExp)e2;
      Exp ar1 = se1.getArrayExp();
      Exp sc1 = se1.getSubscriptExp();
      Exp ar2 = se2.getArrayExp();
      Exp sc2 = se2.getSubscriptExp();
      return (sc1.isSameAs(sc2) && isSmaller(ar1, ar2)) || (ar1.isSameAs(ar2) && isSmallerSubscript(sc1, sc2));
    }

    private boolean isSmallerSubscript(Exp e1, Exp e2) {
      if(e1 == null)
        return ((e2 instanceof ConstNode) && ((ConstNode)e2).getIntValue() > 0);
      if(e2 == null)
        return ((e1 instanceof ConstNode) && ((ConstNode)e1).getIntValue() < 0);
      if(e1 == e2) return false;
      if(e1.isSameAs(e2)) return false;
      // For simple forms
      if(e1 instanceof ConstNode) {
        if(e2 instanceof ConstNode)
          return (((ConstNode)e1).getIntValue() < ((ConstNode)e2).getIntValue());
        return false;
      }
      if(e1 instanceof VarNode) {
        int op2 = e2.getOperator();
        switch(op2) {
        case HIR.OP_CONV:
          return isSmallerSubscript(e1, e2.getExp1());
        case HIR.OP_ADD:
          if((e2.getExp1().isSameAs(e1)) && (e2.getExp2() instanceof ConstNode))
            return ((ConstNode)e2.getExp2()).getIntValue() > 0;
          return false;
        case HIR.OP_SUB:
          if((e2.getExp1().isSameAs(e1)) && (e2.getExp2() instanceof ConstNode))
            return ((ConstNode)e2.getExp2()).getIntValue() < 0;
          return false;
        default: return false;
        }
      }
      // For complex forms
      int op1 = e1.getOperator();
      switch(op1) {
      case HIR.OP_CONV:
        return isSmallerSubscript(e1.getExp1(), e2);
        // e1 : I + c
      case HIR.OP_ADD: 
        if(e2 instanceof ConstNode) return false;
        if(e2 instanceof VarNode) {
          if(e1.getExp1().isSameAs(e2) && e1.getExp2() instanceof ConstNode)
            return ((ConstNode)e1.getExp2()).getIntValue() < 0;
          return false;
        }
        switch(e2.getOperator()) {
        case HIR.OP_CONV:
          return isSmallerSubscript(e1, e2.getExp1());
          // e2 : I + d
        case HIR.OP_ADD:
          if(!e1.getExp1().isSameAs(e2.getExp1())) return false;
          return isSmallerSubscript(e1.getExp2(), e2.getExp2());
          // e2 : I - d
        case HIR.OP_SUB:
          if(!e1.getExp1().isSameAs(e2.getExp1())) return false;
          Exp subexp1 = e1.getExp2();
          Exp subexp2 = e2.getExp2();
          if(!(subexp1 instanceof ConstNode) || !(subexp2 instanceof ConstNode))
            return false;
          return ((ConstNode)subexp1).getIntValue() < -(((ConstNode)subexp2).getIntValue());
        default: return false;
        }
        // e1 : I - c
      case HIR.OP_SUB:
        if(e2 instanceof ConstNode) return false;
        if(e2 instanceof VarNode) {
          if(!e1.getExp1().isSameAs(e2) && e1.getExp2() instanceof ConstNode)
            return ((ConstNode)e1.getExp2()).getIntValue() > 0;
          return false;
        }
        switch(e2.getOperator()) {
        case HIR.OP_CONV:
          return isSmallerSubscript(e1, e2.getExp1());
        case HIR.OP_ADD: {
          if(!e1.getExp1().isSameAs(e2.getExp1())) return false;
          Exp subexp1 = e1.getExp2();
          Exp subexp2 = e2.getExp2();
          if(!(subexp1 instanceof ConstNode) || !(subexp2 instanceof ConstNode))
            return false;
          return -(((ConstNode)subexp1).getIntValue()) < ((ConstNode)subexp2).getIntValue();
        }
        case HIR.OP_SUB: {
          if(!e1.getExp1().isSameAs(e2.getExp1())) return false;
          Exp subexp1 = e1.getExp2();
          Exp subexp2 = e2.getExp2();
          if(!(subexp1 instanceof ConstNode) || !(subexp2 instanceof ConstNode))
            return false;
          return -(((ConstNode)subexp1).getIntValue()) < -(((ConstNode)subexp2).getIntValue());
        }
        default: return false;
        }
      default: return false;
      }
    }

    HashSet findLoopMems(BBlock hblk, BBlock tblk) {
      HashSet mems = new HashSet();
      mems.add(tblk);
      // if hblk equals to tblk then hblk is only one member.
      if(hblk == tblk) return mems;
      List work = tblk.getPredList();
      while(!work.isEmpty()) {
        BBlock b = (BBlock)work.get(0);
        work.remove(b);
        if(mems.contains(b)) continue;
        if(b == hblk) continue;
        if(b.isEntryBlock()) continue; // Failure ?
        if(!isDominated(b, hblk)) continue;
        mems.add(b);
        work.addAll(b.getPredList()); // ???
      }
      mems.add(hblk);
      return mems;
    }
  }
}
