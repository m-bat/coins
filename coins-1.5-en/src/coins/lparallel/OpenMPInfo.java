/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;

import coins.HirRoot;
import coins.Registry;
import coins.aflow.DefUseCell;
import coins.aflow.DefUseList;
import coins.aflow.FlowResults;
import coins.aflow.SubpFlow;
import coins.aflow.FlowUtil;
import coins.ir.IR;
import coins.ir.IrList;
import coins.ir.hir.BlockStmt;
import coins.ir.hir.BlockStmtImpl;
import coins.ir.hir.ConstNode;
import coins.ir.hir.Exp;
import coins.ir.hir.ExpImpl;
import coins.ir.hir.ForStmt;
import coins.ir.hir.HIR;
import coins.ir.hir.LabeledStmt;
import coins.ir.hir.SubpDefinition;
import coins.ir.hir.Stmt;
import coins.ir.hir.StmtImpl;
import coins.ir.hir.VarNode;
import coins.sym.Var;
import coins.sym.SymTable;
/////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] OpenMP  Info
//   Generate C program normalizing the loop and
//   adding OpenMP pragma ?
//
/////////////////////////////////////////////////////////////////////////////
public class OpenMPInfo {
    private SubpFlow fSubpFlow;
    private FlowResults fresults;
    private HIR  hir;
    private LoopUtil fUtil;
    private HirRoot  HirRoot;
    /**
    *
    * OpneMPInfo:
    *
    *
    **/
    public OpenMPInfo(SubpFlow pSubpFlow,FlowResults presults) {
        fSubpFlow = pSubpFlow;
        fresults = presults;
        hir =fresults.flowRoot.hirRoot.hir;
        fUtil = new LoopUtil(fresults,fSubpFlow);
        HirRoot = fresults.flowRoot.hirRoot;
    }
    /**
    *
    * showLoopAnalResult:
    *
    *
    **/
    public void showLoopAnalResult() {
        LinkedList LoopInfo;
        ListIterator  Ie;
        ListIterator  Ie1;
        Iterator  Ie2;
        LoopTable lTable;
        ArrayAreaAnalyzer  ArrayAnalysis ;
        ArrayAnalysis = new ArrayAreaAnalyzer(null,fUtil);
        ArrayAnalysis.setFlowResults(null);
        ArrayAnalysis.setLoopExitBBlock(null) ;
        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable = (LoopTable)Ie.next();
            fUtil.Trace("NestLevel=" + lTable.fNestLevel,5);
            fUtil.Trace("LoopStmt " + lTable.LoopStmt.toStringShort() + " " + lTable,5); //##70
            if (lTable.getParaFlag(fUtil,"showLoopAnalResult") == true) {
                fUtil.Trace("LastPrivate",5);
                for (Ie2 =lTable.LastPrivate.iterator();Ie2.hasNext();) {
                    Exp VarNode = (Exp) Ie2.next();
                    fUtil.Trace(VarNode.toString(),5);
                }
                fUtil.Trace("Private",5);
                for (Ie2 =lTable.Private.iterator();Ie2.hasNext();) {
                    Exp VarNode = (Exp) Ie2.next();
                    fUtil.Trace(VarNode.toString(),5);
                }
            } else {
                fUtil.Trace("VAR:true-dependence",5);
                for (Ie1 = lTable.varResultList.listIterator();Ie1.hasNext();) {
                    Var var = (Var) Ie1.next();
                    fUtil.Trace(var.getName(),5);
                }
                ArrayAnalysis.printaryelmList("true-dependence",lTable.mod_euseResultList);
                ArrayAnalysis.printaryelmList("anti-dependence", lTable.use_modResultList);
                ArrayAnalysis.printaryelmList("output-dependence", lTable.mod_modResultList);
            }
        }
    }
    /**
    *
    *  setParamInfoLoopList:
    *
    *
    **/
    void setPragmaInfoLoopList(LinkedList LoopInfo,boolean Child) {

        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable=(LoopTable)Ie.next();
            if (Child == true) {
                setPragmaInfoLoopList(lTable.InnerLoopList,Child);
            }
            setPragmaInfoLoop(lTable);
        } // End of For Loop

    }
    /**
    *
    * setPragmaInfoLoop:
    *
    *
    **/
    void setPragmaInfoLoop(LoopTable pTable) {
        LinkedList LoopInfo;
        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;

        fUtil.Trace("---pass:setPragmaInfoLoop",1);

        lTable= pTable;
        fUtil.Trace("NestLevel=" + lTable.fNestLevel,5);
        if (lTable.getParaFlag(fUtil,"setParamInfoLoop") == true) {

            //
            // Get Attribute
            //

            IrList Attr =hir.irList();
            HIR hirNode = (HIR)lTable.LoopStmt;
            hirNode.removeInf(Registry.INF_KIND_OPEN_MP);
            hirNode.addInf(Registry.INF_KIND_OPEN_MP,Attr);

            //
            // Set Value
            //

            fUtil.Trace("#pragma",2);
            Attr.add("#pragma omp  parallel for ");

            //
            // LastPrivate
            //

            boolean lastExist = false;
            for (Ie1 = lTable.LastPrivate.iterator();Ie1.hasNext();) {
                 Exp ExpNode = (Exp)Ie1.next();
                 if (Attr.contains(ExpNode.getVar()) == true)
                     continue;
                 if (lastExist == false) {
                     lastExist = true;
                     Attr.add(" lastprivate(");
                 } else {
                    Attr.add(",");
                 }
                 Attr.add( ExpNode.getVar());
             }
             if (lastExist == true) {
                 Attr.add(")");
             }

             //
             // Private
             //

             boolean privateExist = false;
             for (Ie1 = lTable.Private.iterator();Ie1.hasNext();) {
                 Exp ExpNode= (Exp)Ie1.next();
                 if (Attr.contains(ExpNode.getVar()) == true)
                     continue;
                 if (privateExist == false) {
                     privateExist = true;
                     Attr.add(" private(");
                 } else {
                     Attr.add(",");
                 }
                 Attr.add( ExpNode.getVar());
             }
             if (privateExist == true) {
                 Attr.add(")");
             }
             //
             // Reduction
             //

            boolean Exist = false;
             for (Ie1 = lTable.ReductionADDList.iterator();Ie1.hasNext();) {
                 Reduction red = (Reduction)Ie1.next();
                 if (Exist == false) {
                     Exist = true;
                     Attr.add(" reduction(+:");
                 } else {
                     Attr.add(",");
                 }
                 Attr.add( red.DefVarNode.getVar());
             }
             if (Exist == true) {
                 Attr.add(")");
             }
             Exist = false;
             for (Ie1 = lTable.ReductionMULList.iterator();Ie1.hasNext();) {
                 Reduction red = (Reduction)Ie1.next();
                 if (Exist == false) {
                     Exist = true;
                     Attr.add(" reduction(*:");
                 } else {
                     Attr.add(",");
                 }
                 Attr.add(red.DefVarNode.getVar());
             }
             if (Exist == true) {
                 Attr.add(")");
             }
             Exist = false;
             for (Ie1 = lTable.ReductionSUBList.iterator();Ie1.hasNext();) {
                 Reduction red = (Reduction)Ie1.next();
                 if (Exist == false) {
                     Exist = true;
                     Attr.add(" reduction(-:");
                 } else {
                     Attr.add(",");
                 }
                 Attr.add( red.DefVarNode.getVar());
             }
             if (Exist == true) {
                 Attr.add(")");
             }
       }
    } // End of For Loop
    /**
    *
    * setPragmaInfo:
    *
    *
    **/
    void setPragmaInfo() {
        LinkedList LoopInfo;
        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;

        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        fUtil.Trace("---pass:setPragmaInfo",1);
        setPragmaInfoLoopList(LoopInfo,true);
    }
    /**
    *
    *  setErrorInfoLoopList:
    *
    *
    **/
    void setErrorInfoLoopList(LinkedList LoopInfo,boolean Child) {

        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;
        for (Ie = LoopInfo.listIterator();Ie.hasNext();) {
            lTable=(LoopTable)Ie.next();
            if (Child == true) {
                setErrorInfoLoopList(lTable.InnerLoopList,Child);
            }
            setErrorInfoLoop(lTable);
        } // End of For Loop
    }
    /**
    *
    * setErrorInfoLoop:
    *
    *
    **/
    public void setErrorInfoLoop(LoopTable pTable) {
        LinkedList LoopInfo;
        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;
        IrList Attr;
        lTable = pTable;
        fUtil.Trace("NestLevel=" + lTable.fNestLevel,5);
        if (lTable.getParaFlag(fUtil,"setErrorInfo") == false) {
            Attr = makeAttribute((HIR)lTable.LoopStmt);
            /*
            if (lTable.StructPrivate.size() !=0) {
                Attr.add("// loop parallelization impossible: Structure(");
                for (Ie1 = lTable.StructPrivate.iterator();Ie1.hasNext();) {
                    HIR var = (HIR) Ie1.next();
                    Attr.add(" ");
                    Attr.add(var);
                }
                Attr.add(")");
            }
            */
            if (lTable.varUnParalellLastPrivateList.size() !=0) {
                Attr.add("// loop parallelization impossible: UnParalellLastPrivate(");
                for (Ie1 = lTable.varUnParalellLastPrivateList.listIterator();Ie1.hasNext();) {
                    Var var = (Var) Ie1.next();
                    Attr.add(" ");
                    Attr.add(var);
                }
                Attr.add(")");
            }
            if (lTable.varResultList.size() != 0) {
                Attr.add("// loop parallelization impossible: true-dependence:(");
                  for (Ie1 = lTable.varResultList.listIterator();Ie1.hasNext();) {
                    Var var = (Var) Ie1.next();
                    Attr.add(" ");
                    Attr.add(var);
                }
                Attr.add(")");
             }
            if (lTable.mod_euseResultList.size() !=0) {
                Attr.add("//true-dependence:(");
                setArrayListValue(Attr,lTable.mod_euseResultList);
                Attr.add(")");
            }
            if (lTable.use_modResultList.size() !=0) {
                Attr.add("//anti-dependence:(");
                setArrayListValue(Attr,lTable.use_modResultList);
                Attr.add(")");
            }
            if (lTable.mod_modResultList.size() !=0) {
                Attr.add("//output-dependence:(");
                setArrayListValue(Attr,lTable.mod_modResultList);
                Attr.add(")");
            }
        }
    }
    /**
    *
    * setErrorInfo:
    *
    *
    **/
    public void setErrorInfo() {
        LinkedList LoopInfo;
        ListIterator  Ie;
        Iterator  Ie1;
        LoopTable lTable;

        LoopInfo = (LinkedList)fresults.get("LoopParallelList",fSubpFlow);
        fUtil.Trace("---pass:setErrorInfo",1);
        setErrorInfoLoopList(LoopInfo,true);
    }
    /**
    *
    *
    * setArrayListValue:
    *
    *
    **/
    private void setArrayListValue(IrList   Attr  ,LinkedList ArrayList) {
        ListIterator  Ie;
        LinkedList tmplist = new LinkedList();
        for (Ie = ArrayList.listIterator();Ie.hasNext();) {
            Ref_Array ref = (Ref_Array)Ie.next();
            Var v = ref.VarNode.getVar();
            if (tmplist.contains(v) == false) {
                Attr.add(" ");
                Attr.add(v);
                tmplist.add(v);
            }
        }
    }
    /**
    *
    *   makeAttribute:
    *
    *
    */
    IrList makeAttribute(HIR phirNode) {
        IrList list ;
        list = (IrList)phirNode.getInf(Registry.INF_KIND_OPEN_MP);
        if (list == null) {
            list =hir.irList();
            phirNode.addInf(Registry.INF_KIND_OPEN_MP,list);
        }
        return list;
    }
    private void Trace(String s) {
        //ioRoot.printOut.println(s);
        //System.out.println(s );
    }
}
