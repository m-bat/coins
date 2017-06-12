/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import coins.FlowRoot;
import coins.IoRoot;
import coins.ir.IR;
//##60 import coins.sym.AReg;
import coins.sym.ExpId;
import coins.sym.FlowAnalSym;
import coins.sym.Sym;
import coins.sym.SymTable;

// public class ShowDataFlow extends Root {
public class ShowDataFlow
{

  public final FlowRoot flowRoot; // Used to access Root information.
  public final IoRoot ioRoot; // Used to access Root information.
  public final SubpFlow fSubpFlow; //##62

  private DataFlow fDataFlow;
  private int fDefCount;
  //private int fExpCount;
  private int fExpIdCount;
  private int fPointCount;
  private int fBBlockCount;
  private List fBBlockList;
  private ShowDataFlowByName fShowDataFlowByName = null; //##60

  /**
   * ShowDataFlow
   *
   **/
  public ShowDataFlow() // for subclass invocation.
  {
    flowRoot = null;
    ioRoot = null;
    fSubpFlow = null;
  }

  public ShowDataFlow(DataFlow pDataFlow)
  {
    int i;
    //	fFlow = pFlow;
    fDataFlow = pDataFlow;
    flowRoot = ((DataFlowImpl)pDataFlow).flowRoot;
    ioRoot = flowRoot.ioRoot;
    fSubpFlow = flowRoot.fSubpFlow; //##62
    initData();
  }

  private void initData()
  {
    fDefCount = fDataFlow.getDefCount();
    //  	fExpCount=fDataFlow.getExpCount();
    // fExpIdCount  = fDataFlow.getExpIdCount();
    fExpIdCount = fDataFlow.getFlowAnalSymCount();
    fPointCount = fDataFlow.getPointCount();
    fBBlockCount = fSubpFlow.getNumberOfBBlocks(); //##62
    //	fBBlockCount = fDataFlow. getNumberOfBBlocks();
    fBBlockList = fDataFlow.getBBlockList();
  }

  /**
   *
   * showAll
   *
   **/
  public void showAll()
  {
    showPno();
    showAllBitVectors();
    showDefUse();
    showUseDef(); //##63
  }

  /**
   * showSummary
   **/
  public void showSummary()
  {
    java.util.ListIterator lListIterator;
    ShowDataFlowByName lShow;
    java.util.Set lDefUseSyms;
    DefUseList lDefUseList;
    BBlock lBBlock;
    FlowAnalSym lSym;
    Sym lLinkedSym;
    int i;
    fSubpFlow.printExpIdAndIrCorrespondence(); //##73
    lShow = ((DataFlowImpl)fDataFlow).showDataFlowByName;
    for (lListIterator = fBBlockList.listIterator();
         lListIterator.hasNext(); ) {
      lBBlock = (BBlock)lListIterator.next();
      ioRoot.printOut.print("BBlock " +
        lBBlock.getBlockNumber() + " " +
        lBBlock.getLabel().getName() + "\n");
      ioRoot.printOut.print(" def    ");
      lShow.showPointVectorByName(lBBlock.getDef());
      ioRoot.printOut.print(" kill   ");
      lShow.showPointVectorByName(lBBlock.getKill());
      ioRoot.printOut.print(" reach  ");
      lShow.showPointVectorByName(lBBlock.getReach());
      ioRoot.printOut.print(" defined  ");
      lShow.showExpVectorByName(lBBlock.getDefined());
      ioRoot.printOut.print(" used   ");
      lShow.showExpVectorByName(lBBlock.getUsed());
      ioRoot.printOut.print(" exposed  ");
      lShow.showExpVectorByName(lBBlock.getExposed());
      ioRoot.printOut.print(" eGen   ");
      lShow.showExpVectorByName(lBBlock.getEGen());
      ioRoot.printOut.print(" eKill  ");
      lShow.showExpVectorByName(lBBlock.getEKill());
      if (ioRoot.dbgFlow.getLevel() >= 3) {
        ioRoot.printOut.print(" eKillAll"); //##62
        lShow.showExpVectorByName(lBBlock.getEKillAll()); //##62
      }
      ioRoot.printOut.print(" availin  ");
      lShow.showExpVectorByName(lBBlock.getAvailIn());
      ioRoot.printOut.print(" availOut ");
      lShow.showExpVectorByName(lBBlock.getAvailOut());
      ioRoot.printOut.print(" liveIn   ");
      lShow.showExpVectorByName(lBBlock.getLiveIn());
      ioRoot.printOut.print(" liveOut  ");
      lShow.showExpVectorByName(lBBlock.getLiveOut());
      ioRoot.printOut.print(" defIn  ");
      lShow.showExpVectorByName(lBBlock.getDefIn());
      ioRoot.printOut.print(" defOut   ");
      lShow.showExpVectorByName(lBBlock.getDefOut());
      ioRoot.printOut.print(" defNodes ");
      lShow.showNodeSet(lBBlock.getDefNodes());
      //##60 ioRoot.printOut.print(" useNodes ");
      //##60 lShow.showNodeSet(lBBlock.getUseNodes());
    }
    showDefUse(); //##60
    showUseDef(); //##63
  } // showSummary

  /**
   *
   * showDefVector
   *
   **/
  public void showDefVector(DefVector pdef)
  {
    if (ioRoot.dbgFlow.getLevel() > 3) //## Tan
      showVector((BitVector)pdef, fDefCount);
    ((DataFlowImpl)fDataFlow).showDataFlowByName.showPointVectorByName(pdef);
    ioRoot.printOut.print("\n");
  }

  /**
   *
   * showExpVector
   *
   **/
  public void showExpVector(ExpVector pexp)
  {
    //	  showVector(pexp, fExpCount);
    if (ioRoot.dbgFlow.getLevel() > 3) //## Tan
      showVector((BitVector)pexp, fExpIdCount);
    ((DataFlowImpl)fDataFlow).showDataFlowByName.showExpVectorByName(pexp);
    ioRoot.printOut.print("\n");
  }

  /**
   *
   * showPointVector
   *
   **/
  public void showPointVector(PointVector pPoint)
  {
    if (ioRoot.dbgFlow.getLevel() > 3) //## Tan
      showVector((BitVector)pPoint, fPointCount);
    ((DataFlowImpl)fDataFlow).showDataFlowByName.showPointVectorByName(pPoint);
    ioRoot.printOut.print("\n");
  }

  /**
   *
   * showVector
   *
   **/
  public void showVector(BitVector pbit, int pMaxno)
  {
    if (pMaxno == 0)
      ioRoot.printOut.print("0 Length Vector\n");
    int i;
    int j;
    int k;
    int col;
    int col1;
    String l;
    col = 30;
    col1 = getCol(pMaxno) + 1;
    for (i = 1; i <= pMaxno; i++) {
      if ((i % col) == 1) {
        ioRoot.printOut.print("\n");
        for (j = i; j < i + col; j++) {
          outNumber(j, col1);
          if (j == pMaxno)
            break;
        }
        ioRoot.printOut.print("\n");
        for (j = i; j < i + col; j++) {
          for (k = 1; k <= col1; k++)
            ioRoot.printOut.print("-");
          if (j == pMaxno)
            break;
        }
        ioRoot.printOut.print("\n");
      }
      if (pbit.getBit(i) == 1)
        outNumber(1, col1);
      else
        outNumber(0, col1);
    }
    ioRoot.printOut.print("\n");
    // ioRoot.printOut.println("");
  }

  /**
   *
   * showVector(BitVector)
   *
   */
  void showVector(BitVector pBitVector)
  {
    if (pBitVector instanceof DefVector)
      showDefVector((DefVector)pBitVector);
    else if (pBitVector instanceof ExpVector)
      showExpVector((ExpVector)pBitVector);
    else if (pBitVector instanceof PointVector)
      showPointVector((PointVector)pBitVector);
    else
      throw new IllegalArgumentException("Unexpected argument type.");
  }

  /**
   *
   * showVector(BitVector, String)
   *
   */
  void showVector(BitVector pBitVector, String pComment)
  {
    ioRoot.printOut.print(pComment+"\n");
    showVector(pBitVector);
  }

  /**
   *
   * getCol
   *
   **/
  private int getCol(int pnum)
  {
    String intStr;
    intStr = String.valueOf(pnum);
    return intStr.length();
  }

  /**
   *
   * outNumber
   *
   **/
  private void outNumber(int pnum, int pcol)
  {
    int sp;
    int i;
    sp = pcol - getCol(pnum);
    for (i = 0; i < sp; i++) {
      ioRoot.printOut.print(" ");
    }
    ioRoot.printOut.print(pnum);
  }

  /**
   *
   * showPno
   *
   **/
  public void showPno()
  {
    BBlock b;
    IR s; //## Tan
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int NodeIndex;
    int NodePno;
    initData();
    ioRoot.printOut.print("=====[Pno]=====\n");
    initData();
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      for (Is = b.bblockSubtreeIterator(); Is.hasNext(); ) {
        s = Is.next(); //## Tan
        if (s == null) //## Tan
          break; //## Tan
        NodeIndex = s.getIndex();
        NodePno = fDataFlow.getDefIndex(NodeIndex);

        ioRoot.printOut.print("NodeIndex=" + NodeIndex);
        ioRoot.printOut.print("	PNO=" + NodePno+"\n");
      }
    }
  }

  /**
   *
   * showDef
   *
   **/
  public void showDef()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    int i;
    initData();
    ioRoot.printOut.print("=====[Def(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showDefVector(b.getDef());
    }
  }

  /**
   *
   * showKill
   *
   **/
  public void showKill()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[Kill(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showDefVector(b.getKill());
    }
  }

  /**
   *
   * showReach
   *
   **/
  public void showReach()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[Reach(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showDefVector(b.getReach());
    }
  }

  /**
   *
   * showDefined
   *
   **/
  public void showDefined()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[Defined(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getDefined());
    }
  }

  // BEGIN addition by Tan
  /**
   *
   * showUsed
   *
   **/
  public void showUsed()
  {
    BBlock b;
    java.util.ListIterator Ie;
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[Used(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getUsed());
    }
  }

  // END of addition

  /**
   *
   * showExposed
   *
   */
  public void showExposed()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[Exposed(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getExposed());
    }
  }

  /**
   *
   * showEGen
   *
   */
  public void showEGen()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[EGen(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getEGen());
    }
  }

  /**
   *
   * showEKill
   *
   */
  public void showEKill()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[EKill(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getEKill());
    }
  }

  /**
   *
   * showAvailIn
   *
   **/
  public void showAvailIn()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[AvailIn(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getAvailIn());
    }
  }

  /**
   *
   * showAvailOut
   *
   **/
  public void showAvailOut()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[AvailOut(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getAvailOut());
    }
  }

  /**
   *
   * showLiveIn
   *
   **/
  public void showLiveIn()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[LiveIn(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getLiveIn());
    }
  }

  /**
   *
   * showLiveOut
   *
   **/
  public void showLiveOut()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[LiveOut(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getLiveOut());
    }
  }

  /**
   *
   * showDefIn
   *
   */
  public void showDefIn()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[DefIn(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getDefIn());
    }
  }

  /**
   *
   * showDefOut
   *
   */
  public void showDefOut()
  {
    BBlock b;
    //## ListIterator  Ie; //## Tan
    java.util.ListIterator Ie; //## Tan
    BBlockSubtreeIterator Is;
    int i;
    initData();
    ioRoot.printOut.print("=====[DefOut(B)]=====\n");
    for (Ie = fBBlockList.listIterator(); Ie.hasNext(); ) {
      b = (BBlock)Ie.next();
      ioRoot.printOut.print("BlockNO =");
      ioRoot.printOut.print(b.getBlockNumber()+"\n");
      showExpVector(b.getDefOut());
    }
  }

  void showDefVectors()
  {
    showDef();
    showKill();
    //showIn();
    //showOut();
    showReach();
  }

  void showExpVectors()
  {
    showDefined();
    showUsed();
    showExposed();
    showEGen();
    showEKill();
    showAvailIn();
    showAvailOut();
    showLiveIn();
    showLiveOut();
    showDefIn();
    showDefOut();
  }

  void showBasic()
  {
    showDef();
    showKill();
    showDefined();
    showExposed();
    showEGen();
    showEKill();
  }

  void showSolved()
  {
    //showIn();
    //showOut();
    showReach();
    showAvailIn();
    showAvailOut();
    showLiveIn();
    showLiveOut();
    showDefIn();
    showDefOut();
  }

  /*void showInOutRelated() {
    showDef();
    showKill();
    showIn();
    showOut();
    }*/

  void showReachRelated()
  {
    showDef();
    showKill();
    showReach();
  }

  void showAvailInAvailOutRelated()
  {
    showEGen();
    showEKill();
    showAvailIn();
    showAvailOut();
  }

  void showLiveInLiveOutRelated()
  {
    showExposed();
    showDefined();
    showLiveIn();
    showLiveOut();
  }

  void showDefInDefOutRelated()
  {
    showDefined();
    showDefIn();
    showDefOut();
  }

  void showAllBitVectors()
  {
    showDefVectors();
    showExpVectors();
  }

  /**
   *
   * showDefUse
   *
   * Shows DefUseList for each FlowAnalSym.
   *
   */
  //##60 BEGIN
  public void showDefUseOld()
  {
    FlowAnalSym lSym;
    Sym lLinkedSym;
    DefUseList lDefUseList;
    Set lDefUseSyms = new java.util.HashSet();
    lDefUseSyms.addAll(fSubpFlow.getDefinedSyms());
    lDefUseSyms.addAll(fSubpFlow.getUsedSyms());
    ioRoot.printOut.print("=====[DefUse]===== of " +
      fSubpFlow.getSubpSym().getName() + "\n");
    lDefUseList = fSubpFlow.getDefUseList(); //##63
    for (Iterator lIterator = lDefUseSyms.iterator();
         lIterator.hasNext(); ) {
      lSym = (FlowAnalSym)lIterator.next();
      if (lSym != null) {
        /* //##63
          lDefUseList = lSym.getDefUseList0();
          if (lDefUseLis != null) {
            ioRoot.printOut.print("\n " + lSym.getName());
            lLinkedSym = null;
            if (lSym instanceof ExpId)
              lLinkedSym = ((ExpId)lSym).getLinkedSym();
            if (lLinkedSym != null)
              ioRoot.printOut.print("(" + lLinkedSym.getName() + ")");
            lDefUseList.toString();
          }
        }
        */ //##63
        //##63 BEGIN
        ioRoot.printOut.print("\n " + lSym.getName());
        lLinkedSym = null;
        if (lSym instanceof ExpId)
          lLinkedSym = ((ExpId)lSym).getLinkedSym();
        if (lLinkedSym != null)
          ioRoot.printOut.print("(" + lLinkedSym.getName() + ")");
        List lDefNodeList = fSubpFlow.getDefNodeList(lSym);
        for (Iterator lNodeIt = lDefNodeList.iterator();
             lNodeIt.hasNext(); ) {
          DefUseChain lDefUseChain
            = lDefUseList.getDefUseChain((IR)lNodeIt.next());
          if (lDefUseChain != null) {
            ioRoot.printOut.print("\n  " + lDefUseChain.toStringByName());
          }
        }
        //##63 END
      }
      Set lUndefinedUseNodes = fDataFlow.getUndefinedUseNodeOfSym(lSym);
      if (lUndefinedUseNodes != null) {
        ioRoot.printOut.print("\n  Use-nodes without corresponding Def-node\n   ");
        for (Iterator lIterator3 = lUndefinedUseNodes.iterator();
             lIterator3.hasNext(); ) {
          IR lUseNode2 = (IR)lIterator3.next();
          ioRoot.printOut.print(" " + lUseNode2.toStringShort());
        }
      }
    }
  } // showDefUseOld
  //##60 END

  //##63 BEGIN
  public void showDefUse()
  {
    if (! fSubpFlow.isComputed(fSubpFlow.DF_DEFUSELIST))
      flowRoot.flow.dataFlow().findDefUse();
    DefUseList lDefUseList = fSubpFlow.getDefUseList();
    ioRoot.printOut.print("\n=====[DefUse]===== of " +
      fSubpFlow.getSubpSym().getName() + "\n");
    lDefUseList.print();
  } // showDefUse

  public void showUseDef()
  {
    if (! fSubpFlow.isComputed(fSubpFlow.DF_USEDEFLIST))
      flowRoot.flow.dataFlow().findUseDef();
    UseDefList lUseDefList = fSubpFlow.getUseDefList();
    ioRoot.printOut.print("\n=====[UseDef]===== of " +
      fSubpFlow.getSubpSym().getName() + "\n");
    ioRoot.printOut.print(lUseDefList.toStringByName());
    ioRoot.printOut.print("\n");
  } // showUseDef
  //##63 END

  //##60 BEGIN
  public void setShowDataFlowByName( ShowDataFlowByName pShowDataFlowByName )
  {
    fShowDataFlowByName = pShowDataFlowByName;
  }
  //##60 END
} // ShowDataFlow
