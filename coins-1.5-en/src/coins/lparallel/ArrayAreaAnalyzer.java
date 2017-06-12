/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import coins.aflow.BBlock;
import coins.aflow.FlowResults;
import coins.aflow.FlowUtil;
import coins.ir.IR;
import coins.ir.hir.AssignStmt;
import coins.ir.hir.Exp;
import coins.ir.hir.HIR;
import coins.ir.hir.VarNode;
import coins.ir.hir.HirIterator;
import coins.sym.Var;
import coins.sym.VectorType;
import coins.sym.Type;
/**
*
*  ArrayAreaAnalyzer:
*  
*  Parallelization Array analysis class.
*  
**/
class ArrayAreaAnalyzer {
    private HIR hir;
    private BBlock fLoopExitBBlock;
    private FlowResults fResults;
    private RegionOp     fRegionOp;
    private LoopUtil fUtil;
    /**
    *
    *  ArrayAreaAnalyzer:
    *  
    * Parallelization domain analysis class.
    * @param phir   HIR
    * @param pUtil  utility for parallelization
    *  
    **/
    ArrayAreaAnalyzer(HIR phir,LoopUtil pUtil)
    {
        hir = phir;
        fUtil = pUtil;
    }
    /**
    *
    *  setLoopExitBBlock: 
    *
    *
    **/
    void setLoopExitBBlock( BBlock pBBlock) {
        fLoopExitBBlock = pBBlock;
        if (pBBlock != null)
            fUtil.Trace("ArrayAnal Exit BBlock=" +pBBlock.getBBlockNumber(),5);
    }
    /**
    *
    *  setFlowfResults: 
    *
    *
    **/
    void setFlowResults(FlowResults pResults) {

        fResults = pResults;

    }
    /**
    *
    *  setRegOp: 
    *
    **/
    void setRegOp(RegionOp pRegionOp) {

        fRegionOp = pRegionOp;

    }
    /**
    *
    *  getArrayList: 
    *
    *
    *  ex)
    *   expression : x = a[l]  + b[m][n]
    *   node       : = node
    *   ArrayList  : a[l] (HIR.OP_SUBS)  and b[m][n] (HIR.OP_SUBS)
    *
    *
    **/
    void getArrayList(IR node, List ArrayList) {
        HirIterator Ie;
        HIR nextNode;

        if (node != null) {
            //Ie =hir.hirIterator(node);
            Ie =FlowUtil.hirIterator((HIR)node);
            for (;Ie.hasNext();) {
                nextNode =Ie.next();
                if (nextNode != null) {
                    if (nextNode.getOperator() == HIR.OP_SUBS) {
                        if (nextNode.getParent().getOperator() != HIR.OP_SUBS)
                            ArrayList.add(nextNode);
                    }
                }
            }
        }
    }
    /**
    *
    * make_ref_Array:
    *
    *
    *
    **/
    Ref_Array  make_ref_Array (Exp node, LinkedList IndList,Invariant pInv) {
        Ref_Array ref;

        ref = new Ref_Array(fResults,node,IndList,pInv,fUtil);
        return ref;
    }
    /**
    *
    *  addaryelmList:
    *
    *
    **/
    void addaryelmList(LinkedList from,LinkedList to) {
        ListIterator  Ie;

        if (from == null || to == null) 
            return;
        for (Ie =from.listIterator();Ie.hasNext();) {
            Ref_Array ref = (Ref_Array)Ie.next();
            if (ref == null) continue;
            addaryelm(to,ref);
        }
    }
    /**
    *
    *  subaryelmList:
    *
    *
    *
    **/
    void subaryelmList(LinkedList from,LinkedList to) {
        ListIterator  Ie;

        if (from == null || to == null) 
            return;
        for (Ie =from.listIterator();Ie.hasNext();) { 
            Ref_Array fromRef = (Ref_Array)Ie.next();
            fUtil.Trace("Sub-----",5);
            // Debug...
            fromRef.DebugArrayRef(fUtil);
            //
            subaryelm(to,fromRef);
        }
    } 
    /**
    *
    *  mularyelmList:
    *
    *
    *
    **/
    void mularyelmList(LinkedList from,LinkedList to) {
        ListIterator  Ie;
        ListIterator  Ie1;
        LinkedList delList;
        boolean     exist;
        delList = new LinkedList();

        if (from == null || to == null) 
            return;
        /*
        for (Ie =to.listIterator();Ie.hasNext();) { 
            Ref_Array toref = (Ref_Array)Ie.next();
            exist = false;
            for (Ie1 =from.listIterator();Ie1.hasNext();) { 
                Ref_Array fromref = (Ref_Array)Ie1.next();
                if (fromref.VarNode.getVar() == toref.VarNode.getVar()) { 
                    exist = true;
                    break;
                }
            }
            if (exist == false) {
                delList.add(toref);
            }
        }
        
        for (Ie =delList.listIterator();Ie.hasNext();) 
            to.remove(Ie.next());
        */     
        //if( from.size() == 0) {
        //     to.clear();
        //} else {
            LinkedList result=new LinkedList();
            for (Ie =from.listIterator();Ie.hasNext();)  {
                 fUtil.Trace("mularyelm STRAT ",5);
                mularyelm(to,result,(Ref_Array)Ie.next());
            }
            to.clear() ;
            for (Ie1 =result.listIterator();Ie1.hasNext();)  
               to.add(Ie1.next());
        //}
    }
    /**
    *
    * make_refArrayCellList: 
    *   
    *
    **/
    LinkedList make_refArrayCellList(LinkedList refList,int ArrayAnal) {
        LinkedList llist;
        llist = new LinkedList();
        ListIterator  Ie;
        Ref_Array lref;
        RefArrayCell cell;
    
        for (Ie =refList.listIterator();Ie.hasNext();) {
            lref = (Ref_Array)Ie.next();
            Var varname =lref.VarNode.getVar();
            cell = get_refArrayCell(varname,llist);
            if (cell == null) {
                cell = new RefArrayCell(varname);    
                cell.ArrayRef.add(lref);
                cell.ArrayAnal = ArrayAnal; // DDEF,EUSE ...
                llist.add(cell);    
            } else {
                cell.ArrayRef.add(lref);
            }
        }
        return llist;
    }
    /**
    *
    *  get_refArrayCell:
    *   
    **/
    RefArrayCell get_refArrayCell(Var varname,LinkedList llist) {
        ListIterator  Ie;
        RefArrayCell cell;

        for (Ie =llist.listIterator();Ie.hasNext();) {
            cell = (RefArrayCell)Ie.next();
            if (cell.ArrayName == varname) {
                return cell;
            }
        }
        return null;
    }
    /**
    *
    *  refJudge:
    *
    **/
    int refJudge(LoopTable lTable,LinkedList refArray1, int ArrayAnal1,
                LinkedList refArray2, int ArrayAnal2,
        LinkedList resultList,Set LastPrivate ) {
        ListIterator  Ie1;
        ListIterator  Ie2;
        boolean dom_flg ;
        boolean inv_flg ;
        RefArrayCell cell;
        int i;
        int retMUL2=0;
        dom_flg = true;
        inv_flg = true;

fUtil.Trace("---pass:refJudge",1);

        for (Ie1 = refArray1.listIterator();Ie1.hasNext();) {
            Ref_Array ref1 = (Ref_Array)Ie1.next();

            //if (ArrayInv(ref1) == false)  
            //    inv_flg = false;
            if (ArrayInv2(ref1) == false)  
                inv_flg = false;

            for (Ie2 =refArray2.listIterator();Ie2.hasNext();) {
                Ref_Array ref2 = (Ref_Array)Ie2.next();
                //if (ref1 == ref2)
                //    continue;

                //if (ArrayInv(ref2) == false)  
                //    inv_flg = false;
                if (ArrayInv2(ref2) == false)  
                    inv_flg = false;

                int dim =ref1.dimension;
                retMUL2 = -1;
                for (i=0;i<dim;i++) {  
                    if (fRegionOp.regMUL2(ref1,ref2,i) ==0) {
                        retMUL2 = 0;
                        break;
                    }
                }
                if (retMUL2 != 0) { 
                    // Debug...
                    ref1.DebugArrayRef(fUtil);
                    ref2.DebugArrayRef(fUtil);
                    //
                    resultList.add(ref1);
                    resultList.add(ref2);
                }
            }
        } 
        fUtil.Trace("inv_flg = "+ inv_flg,5);
        if (inv_flg == true) {
            //
            // ex)
            //
            // 
            for (Ie1 =refArray1.listIterator();Ie1.hasNext();) {
                Ref_Array ref1 =(Ref_Array)Ie1.next();
                if (ArrayAnal1 == Ref_Array.REF_MOD) {
                    if (ArrayDDEF_check(lTable,ref1.VarNode) == true) { 
                        dom_flg = false;
                        LastPrivate.add(ref1.ArrayNode);
                        fUtil.Trace("add ArrayLastPrivate(1) = "+ ref1.ArrayNode.toString(),5);
                        resultList.add(ref1);
                    }
                }
            }
            for (Ie2 =refArray2.listIterator();Ie2.hasNext();) {
                Ref_Array ref2 =(Ref_Array)Ie2.next();
                if (ArrayAnal2 == Ref_Array.REF_MOD) {
                    if (ArrayDDEF_check(lTable,ref2.VarNode) == true){
                        dom_flg = false;
                        LastPrivate.add(ref2.ArrayNode);
                        fUtil.Trace("add ArrayLastPrivate(2) = "+ ref2.ArrayNode.toString(),5);
                        resultList.add(ref2);
                    }
                }
            }
            if (dom_flg == false) 
                return -1;    
        }
        if (resultList.size() == 0)
            return -1;
        else 
            return 1; // (safety) 0 and 1
    }
    /**
    *
    * indJudge:
    *   
    * 
    **/
    int indJudge( LoopTable lTable,LinkedList refList, boolean mod_flg,LinkedList unParalleizable ,Set LastPrivate ) {
        boolean dom_flg ;
        boolean inv_flg ;
        boolean ind_flg;
        boolean comp_flg;
        boolean same_flg;
        boolean indExist;
        Ref_Array  comp  ;

fUtil.Trace("---pass:indJudge",1);
fUtil.Trace("indJudge size="+refList.size(),5);

        dom_flg = true; 
        inv_flg = true;
        ind_flg = true;
        comp_flg = false;
        same_flg= false;
        indExist = false;
        //refList = new LinkedList();
        comp = null;
        mod_flg = false; // ALL
        for (ListIterator Ie =refList.listIterator();Ie.hasNext();) {
            Ref_Array ref =(Ref_Array)Ie.next();

            if (ArrayInv(ref) == false) {
                    inv_flg = false;
            } else {
                if (mod_flg == true) {
                     mod_flg = true;
                    //if (Arraydom_check(lTable,ref) == true){
                    //    LastPrivate.add(ref.ArrayNode);
                    //} else {
                    //    dom_flg = false;
                    //}
                } 
            }
            int dim = ref.dimension;
            int i;
            indExist= false;
            for (i=0;i<dim;i++) {
                if (ref.ID[i] == ref.REF_ARRAY_INDUCTION) {
                    indExist =true;
                    break;
                }
            }
            if (indExist == true) { 
                if (comp_flg == false) {
                    comp  = ref;    // Initialize
                    comp_flg = true;
                } else {
                    for (i=0;i<dim;i++) {
                        if (ref.ID[i] == ref.REF_ARRAY_INDUCTION &&  
                            comp.ID[i] == ref.REF_ARRAY_INDUCTION) {  
                            if (comp.InductionExp[i].EQindExp(ref.InductionExp[i]) == true) { 
                                fUtil.Trace("*********same***********",5);
                                comp.InductionExp[i].DebugIndExp(fUtil);
                                ref.InductionExp[i].DebugIndExp(fUtil);
                                fUtil.Trace("same=true",5);
                                same_flg= true;
                            } else { 
                                same_flg= false;
                                break;
                            }
                        }
                    }
                    if (same_flg == false)
                        break;
                }
            } else {
                ind_flg = false;
            }
        }

fUtil.Trace("ind_flg="+ind_flg,5);
fUtil.Trace("inv_flg="+inv_flg,5);
fUtil.Trace("dom_flg="+dom_flg,5);
fUtil.Trace("mod_flg="+mod_flg,5);
fUtil.Trace("same_flg="+same_flg,5);

        if (ind_flg == false && inv_flg== false) {
            ListAppend(refList,unParalleizable);
            return 1;
        }
        if (inv_flg == true)  {
            if (dom_flg== false) {
                ListAppend(refList,unParalleizable);
                return 1;
            } else {
                if (mod_flg == false) {
                    ListAppend(refList,unParalleizable);
                    return 1;
                } else { 
                    return -1;
                }
            }
        }    
        if (ind_flg == true)  {
            if (same_flg == true)  {
                return -1;
            } else {
                ListAppend(refList,unParalleizable);
                return 1;
            }
        }
        ListAppend(refList,unParalleizable);
        return  1;
    }
    /*
    *
    * ListAppend:
    *
    *
    */
    public void ListAppend(List from,List to) {
        ListIterator  Ie;
        //to.clear();
        for (Ie =from.listIterator();Ie.hasNext();) {
            to.add(Ie.next());
        }
    }
    /**
    *
    * Arraydom_check:
    *
    *
    **/
    private  boolean  Arraydom_check(LoopTable lTable,Ref_Array ref) {
        BBlock bblock;
        List dom ;

        bblock = (BBlock)fResults.get("BBlockForNode",ref.ArrayNode);
        
        dom =(List) fLoopExitBBlock.getDomForSubpFlow();

        if (dom.contains(bblock) == true) {
            return true;
        } else {
            return false;
        }
    }
    /**
    *
    * ArrayDDEF_check:
    *
    *
    **/
    private  boolean  ArrayDDEF_check(LoopTable lTable,VarNode vNode) {
        int dim ;
        long count;
        Type nextType=null;
        Ref_Array ref =null;
        RefArrayCell DDEFcell =get_refArrayCell(vNode.getVar(),
                                lTable.ControlRef.ddefArrayList);
        if(DDEFcell == null)
            return false; 
        if (DDEFcell.ArrayRef.size() !=1) 
           return false; 

         for (ListIterator Ie =DDEFcell.ArrayRef.listIterator();Ie.hasNext();) {
             ref = (Ref_Array)Ie.next();
             if (ref.dimdependence == true) {
                 //
                 //    ex)
                 //    int a[10][10];
                 //    for (i=0;i<10;i++) { 
                 //       for (j=0;j<10;j++) { 
                 //          a[j][j]=0;
                 //       }
                 //    }
                 //
                 //
                 continue;
             }
             nextType = ref.VarNode.getType(); 
             count =0;
             boolean all= true;
             for (int i=0;i<ref.dimension;i++) {
                count=((VectorType)nextType).getElemCount(); 
                if (ref.ID[i] != ref.REF_ARRAY_REG_INV) { 
                    if (ref.ID[i] != ref.REF_ARRAY_INDUCTION) { 
                        all = false;
                        break;
                    }
                }
                if (ref.InductionExp[i].valueConst == false) { 
                        all = false;
                        break;
                }
                if (ref.InductionExp[i].ind_inc  != 1) { 
                        all = false;
                        break;
                }
                if (ref.InductionExp[i].ind_init  != 0)  {
                        all = false;
                        break;
                }
                //fUtil.Trace("last="+ ref.InductionExp[i].ind_last  ,5); 
                //fUtil.Trace("count="+ (count-1),5);
                if (ref.InductionExp[i].ind_last  != (count-1)) { 
                        all = false;
                        break;
                }
                nextType = ((VectorType)nextType).getElemType();
            }
            if (all == true) {
                return true; 
            }
       }
        return false; 
    }
    /**
    *
    * ArrayInv:
    *
    *
    **/
    private  boolean  ArrayInv(Ref_Array ref) {
        int dim ;
        dim = ref.dimension;
        for (int i=0;i<dim;i++) {
            if (ref.ID[i] == ref.REF_ARRAY_INDUCTION)
                return false;
        }
        return true; // INVARIANT + UNKNOUWN  (safety)
    }
    /**
    *
    * ArrayInv2:
    *
    *
    **/
    private  boolean  ArrayInv2(Ref_Array ref) {
        int dim ;
        dim = ref.dimension;
        for (int i=0;i<dim;i++) {
            if (ref.ID[i] != ref.REF_ARRAY_REG_INV)
                return false;
        }
        return true; 
    }
    /**
    *
    * ExpandArrayList:
    *
    *
    **/
    public void ExpandArrayList(LoopTable pTable,LinkedList refList,
        LinkedList fromList,LinkedList ddefList, int refID) {

        LinkedList toList = new LinkedList();
        LinkedList euseList = new LinkedList();
        Ref_Array NewEUSEref;
        boolean   New;
        NewEUSEref = null;
        if (refID == Ref_Array.REF_EUSE ) {
            for (ListIterator Ie =fromList.listIterator();Ie.hasNext();) {
                RefArrayCell EUSEcell =(RefArrayCell)Ie.next();
                RefArrayCell DDEFcell = get_refArrayCell(EUSEcell.ArrayName,ddefList);
                if (DDEFcell == null)
                    continue;
                for (ListIterator Ie1 =EUSEcell.ArrayRef.listIterator();Ie1.hasNext();) {
                    Ref_Array EUSEref = (Ref_Array)Ie1.next();
                    New = false;
                    for (ListIterator Ie2 =DDEFcell.ArrayRef.listIterator();Ie2.hasNext();) {
                        Ref_Array DDEFref = (Ref_Array)Ie2.next();

                        if (EUSEref.VarNode.getVar() != DDEFref.VarNode.getVar())
                            continue;
                        int dim = EUSEref.dimension;
                        int analdim = -1;
                        int analcount=0;

                        for (int i=0;i<dim;i++) {
                            if (EUSEref.EQRefArrayDim(DDEFref,i) == false) {
                                analdim = i;
                                analcount ++;
                            }
                        }

                        if (analcount == 1) {
                            if (EUSEref.ID[analdim] == EUSEref.REF_ARRAY_INDUCTION  &&
                                DDEFref.ID[analdim] == DDEFref.REF_ARRAY_INDUCTION  &&
                                EUSEref.InductionExp[analdim].valueConst == true    &&
                                DDEFref.InductionExp[analdim].valueConst == true) { 
                                if ((DDEFref.InductionExp[analdim].ind_inc == 
                                    EUSEref.InductionExp[analdim].ind_inc) &&
                                    ((DDEFref.InductionExp[analdim].ind_init -  
                                    EUSEref.InductionExp[analdim].ind_init ) %
                                    EUSEref.InductionExp[analdim].ind_inc == 0)){
                                    //
                                    // ex)
                                    // EUSE (0,9,1)
                                    // DDEF (2,11,1)
                                    // New EUSE(1,2,1)
                                    //
                                    NewEUSEref = EUSEref.copy();
                                    NewEUSEref.InductionExp[analdim].ind_last  =  
                                            (DDEFref.InductionExp[analdim].ind_init  -  
                                            EUSEref.InductionExp[analdim].ind_inc); 

                                    NewEUSEref.InductionExp[analdim].original = false;

                                    if (NewEUSEref.InductionExp[analdim].ind_init == 
                                        NewEUSEref.InductionExp[analdim].ind_last) {
                                        NewEUSEref.ID[analdim] = EUSEref.REF_ARRAY_INV_CONST;
                                        NewEUSEref.ConstValue[analdim] = 
                                            NewEUSEref.InductionExp[analdim].ind_init;
                                    } 
                                    NewEUSEref.SetdimArea(); 
                                    New= true;
                                    break;
                                }
                            }
                        }
                    }  
                    if (New == true)
                        euseList.add(NewEUSEref); 
                    else
                        euseList.add(EUSEref); 
                } // End of for Loop (EUSE Cell)
                EUSEcell.ArrayRef = euseList;
                euseList = new LinkedList();
            }
        } 

        ExpandArray(pTable,toList,fromList,refID);
        for (ListIterator Ie =toList.listIterator();Ie.hasNext();) 
            addaryelm(refList,(Ref_Array)Ie.next());
    }
    /**
    *
    * ExpandArrayDDEF
    *
    *
    *
    **/
    public void ExpandArrayDDEF(LinkedList toList,LinkedList fromList) 
    {
        int i;
        fUtil.Trace("--ExpandArrayDDEF",5);
        LinkedList tmpList;
        tmpList = new LinkedList();
        for (ListIterator Ie =fromList.listIterator();Ie.hasNext();) {
            RefArrayCell cell =(RefArrayCell)Ie.next();
            for (ListIterator Ie1 =cell.ArrayRef.listIterator();Ie1.hasNext();) {
                Ref_Array from_ref=(Ref_Array)Ie1.next();
                boolean invFlag = true;
                for  (i=0;i<from_ref.dimension;i++) {
                    if (from_ref.ID[i] != Ref_Array.REF_ARRAY_REG_IND_INV) {
                        invFlag = false;
                        break;                      
                    } 
                } 
                if (invFlag == true) {
                    Ref_Array to_ref=from_ref.copy();
                    tmpList.add(to_ref);
                }
            }
        }
        for (ListIterator Ie =tmpList.listIterator();Ie.hasNext();)
            addaryelm(toList,(Ref_Array)Ie.next());
    }
    /**
    *
    * ExpandArray:
    *
    *   
    *
    **/
    public void ExpandArray(LoopTable pTable,LinkedList toList,LinkedList fromList,int refID) 
    {
        int i;
        LoopTable oTable;
        boolean outer_init_inv;
        boolean outer_init_ind;
        boolean outer_inv_flg;
        boolean outer_ind_flg;
        Invariant Inv;
        fUtil.Trace("--ExpandArray",5);
        fUtil.Trace("refID="+refID,5);
        oTable=pTable.OuterLoop;     // oTable -> OuterLoop Table.

        // ex)
        // [outer_init_inv = true]
        //     for (i=0;i<10;i++) {
        //         n=x;
        //         for (j=n;j<10;j++)
        //        ....
        // [outer_init_ind = true]
        //     for (i=0;i<10;i++) {
        //         for (j=i;j<10;j++)
        //        ....
        // [outer_inv_flag = true]
        //     for (i=0;i<10;i++) {
        //         n=x;
        //         for (j=0;j<n;j++)
        //        ....
        // [outer_ind_flag = true]
        //     for (i=0;i<10;i++) {
        //         n=x;
        //         for (j=0;j<i;j++)
        //        ....
        //

        outer_inv_flg = false;
        outer_ind_flg = false;
        
        for (ListIterator Ie =fromList.listIterator();Ie.hasNext();) {
            RefArrayCell cell =(RefArrayCell)Ie.next();
            for (ListIterator Ie1 =cell.ArrayRef.listIterator();Ie1.hasNext();) {
                Ref_Array from_ref=(Ref_Array)Ie1.next();
                fUtil.Trace("------ from_ref-----",5);
                from_ref.DebugArrayRef(fUtil);
                Ref_Array to_ref=from_ref.copy();
                int induction_count =0;
                for  (i=0;i<from_ref.dimension;i++) {
                    fUtil.Trace("EXPAND-DEBUG ID="+from_ref.ID[i],5);
                    switch (from_ref.ID[i]) {
                    case Ref_Array.REF_ARRAY_INV_CONST:  
                    //
                    // ex)
                    // for (i=0;i<10;i++)
                    //     for (j=0;j<10;j++)
                    //         a[3] = i+j;
                    //
                    break;
                    case Ref_Array.REF_ARRAY_UNKNOWN: 
                    case Ref_Array.REF_ARRAY_REG_UNKNOWN: 
                    //
                    // ex)
                    // for (i=0;i<10;i++)
                    //     for (j=0;j<10;j++) {
                    //         a[x+y....] = i+j; // unknown x+y.....
                    //     }
                    //
    
                    break;
                    case Ref_Array.REF_ARRAY_INVARIANT:  
                        if (oTable.fInv.IsInvariant(from_ref.IndexExp[i]) == false) { 
                            IndExp InductionExp=
                            from_ref.getIndExp(from_ref.IndexExp[i],oTable.IndList);
                            // Debug..
                            from_ref.DebugArrayRef(fUtil);
                            //
                            if (InductionExp == null) {
                                // ex)
                                // for (i=0;i<10;i++) {
                                //     for (j=0;j<10;j++) {
                                //         a[x+y....] = 0; // unknown
                                //     }
                                // }
                                to_ref.ID[i] = to_ref.REF_ARRAY_UNKNOWN;
                                fUtil.Trace("UNKNOWN",5);
                            } else {
                                // ex)
                                // for (i=0;i<10;i++) {
                                //     for (j=0;j<10;j++) {
                                //         a[i] = 0;
                                //     }
                                // }
                                fUtil.Trace("INDUCTION",5);
                                to_ref.ID[i] = to_ref.REF_ARRAY_INDUCTION;
                                to_ref.InductionExp[i] = InductionExp;
                            }
                        }
                        break;
                    case Ref_Array.REF_ARRAY_REG_IND_INV:
                        //induction_count +=1;
                        //to_ref.ID[i] = to_ref.REF_ARRAY_REG_UNKNOWN;
                        break;
                    case Ref_Array.REF_ARRAY_INDUCTION:
                        induction_count +=1;
                        fUtil.Trace("EXPAND-ARRAY_INDUCTION",5);
                    case Ref_Array.REF_ARRAY_REG_INV:
                        BasicInduction BasicInd;
                        IndExp   InductionExp = from_ref.InductionExp[i];
                        IndExp InitIndExp=null;
                        IndExp LastIndExp=null;
                        fUtil.Trace("EXPAND-DEBUG(1)",5);
                        if (InductionExp.valueConst == true) {
                            //
                            // ex)
                            //
                            // for(i=0:i<10;i++)
                            //     for(j=0:i<10;i++)
                            //
                            //     ID -> REF_ARRAY_REG_INV 
                            //
                            to_ref.ID[i] = to_ref.REF_ARRAY_REG_INV;
                            to_ref.InductionExp[i] = InductionExp;
                        } else {
                            BasicInd = InductionExp.IndTable;
                            outer_inv_flg = false;
                            outer_ind_flg = false;
                            outer_init_inv = false;
                            outer_init_ind = false;
                            InitIndExp = null;
                            LastIndExp = null;

                            LastIndExp =from_ref.getIndExp((Exp)pTable.finalExp,oTable.IndList) ;
                            fUtil.Trace("finalExp="+pTable.finalExp.toString(),5);
                            fUtil.Trace("oTable size="+oTable.IndList.size(),5);

                            // Debug...
                            // OuterLoop Induction Infomation.
                            //
                            //oTable.DebugInductionList(fUtil);
                            //

                            if(LastIndExp == null) {
                                outer_inv_flg = true;
                                outer_ind_flg = false;
                                for (Iterator Ief =pTable.finalExpList.iterator(); Ief.hasNext();) {
                                    HIR Node =(HIR)Ief.next();
                                    if (oTable.fInv.IsInvariant(Node) == false) 
                                        outer_inv_flg = false;
                                }
                                if (pTable.finalExpList.size() == 0)
                                    outer_inv_flg = false;
                            } else {
                                outer_inv_flg = false;
                                outer_ind_flg = true;
                            }
                            
                            for (Iterator Ie2 =BasicInd.InitDefList.iterator(); Ie2.hasNext();) {
                                IR Node =(IR)Ie2.next();
                                if (fUtil.loop_body(oTable,(HIR)Node) ==true) {
                                    fUtil.Trace("LoopBody true",5);
                                    AssignStmt ast;
                                    ast = (AssignStmt)Node;
                                    if(outer_init_inv == false) {
                                        fUtil.Trace(ast.getRightSide().toString(),5);
                                        InitIndExp =from_ref.getIndExp((Exp)(ast.getRightSide()),
                                            oTable.IndList) ;
                                        if (InitIndExp != null) {
                                            //
                                            // ex)
                                            //
                                            // for(i=0:i<10;i++)
                                            //     for(j=i;j<10;j++)
                                            //
                                            fUtil.Trace("outer_init_ind=true",5);
                                            outer_init_ind= true;
                                        }
                                    } else {
                                        outer_init_inv = true;
                                    }
                                } else  {
                                    outer_init_inv = false;
                                    outer_init_ind= false;
                                    break;
                                }
                            }
                            //
                            // Debug Trace
                            //
                            fUtil.Trace("outer_init_inv="+outer_init_inv,5);
                            fUtil.Trace("outer_inv_flg="+outer_inv_flg,5);
                            fUtil.Trace("outer_init_ind="+outer_init_ind,5);
                            fUtil.Trace("outer_ind_flg="+outer_ind_flg,5);

                            if ((outer_init_inv == true && InductionExp.LastConst == true) ||
                                (outer_inv_flg == true && InductionExp.InitConst == true) ) {
                                //
                                // ex)
                                //
                                // for(i=0:i<10;i++)
                                //
                                //     for(j=n;j<10;j++)
                                //
                                fUtil.Trace("REF_ARRAY_REG_INV",5);
                                to_ref.ID[i] = to_ref.REF_ARRAY_REG_INV;
                                to_ref.InductionExp[i] = InductionExp;
                            } else if((outer_init_ind == true && InductionExp.LastConst == true) || 
                                  (outer_ind_flg  == true && InductionExp.InitConst == true) ){
                                //
                                // ex)
                                //
                                // for(i=0:i<10;i++)
                                //     for(j=i;j<10;j++)
                                //
                                fUtil.Trace("REF_ARRAY_REG_IND_INV",5);
                                to_ref.ID[i] = to_ref.REF_ARRAY_REG_IND_INV;
                                to_ref.RegIndInit[i] =  InitIndExp; // InductionExp(Init)
                                to_ref.RegIndLast[i] =  LastIndExp; // InductionExp(Last)
                                to_ref.InductionExp[i] = InductionExp;
                            } else {
                                fUtil.Trace("REF_ARRAY_REG_UNKNOWN",5);
                                to_ref.ID[i] = to_ref.REF_ARRAY_REG_UNKNOWN;
                            }
                        }
                        break;
                    } // end of switch
                } // end of for (dim)
                fUtil.Trace("------ from_ref-----indCount"+induction_count,5);
                if (induction_count > 1)  {
                    to_ref.dimdependence = true;
                }
                to_ref.DebugArrayRef(fUtil);
                to_ref.SetdimArea();
                toList.add(to_ref);
            }
        }
    }
    /**
    *
    * TraceArrayCellList: 
    *   
    * 
    **/
    public void TraceArrayCellList(String comment,LinkedList CellList) {

        fUtil.Trace("TraceArrayCellList--"+comment,5);
        fUtil.TraceString = comment;
        for (Iterator Ie =CellList.iterator(); Ie.hasNext();) {
            RefArrayCell Cell =(RefArrayCell)Ie.next();
            fUtil.Trace("ArrayName="+Cell.ArrayName.toString(),5);
            printaryelmList(comment ,Cell.ArrayRef) ;
        }
        fUtil.TraceString = "";
    }
    /**
    *
    *  addaryelm:
    *
    **/
    void addaryelm(LinkedList refList,Ref_Array ref) {
        ListIterator  Ie;
        Ref_Array lref;
        Ref_Array llref;
        Ref_Array cref ;
        int i,dim,analdim,ret;
        fUtil.Trace("addaryelm st(1)",5);

        if (ref == null) return; 
        if (refList == null) return; 
        if (ref.isRegUNKNOWN() == true) { 

            // Debug...
            //ref.DebugArrayRef(fUtil);
            //

        fUtil.Trace("addaryelm st(2)",5);
            refList.add(ref);
            return ;
        }

        lref = null;
        cref = null;

        for (Ie =refList.listIterator();Ie.hasNext();) {
            lref = (Ref_Array)Ie.next();

            fUtil.Trace("addaryelm st(3)",5);
            if (lref.VarNode.getVar() != ref.VarNode.getVar()) 
                continue;
            fUtil.Trace("addaryelm st(3-1)",5);
            if (lref.isRegUNKNOWN() == true) 
                continue;
            fUtil.Trace("addaryelm st(3-2)",5);

            ref.DebugArrayRef(fUtil);
            lref.DebugArrayRef(fUtil);

            if (lref.EQRefArray(ref) == true) 
                continue;
            dim = lref.dimension;

             fUtil.Trace("addaryelm st(4)",5);
            analdim = -1;
            for (i=0;i<dim;i++) {
             fUtil.Trace("addaryelm st(5)",5);
                if (lref.EQRefArrayDim(ref,i) == false) {
                    if (analdim == -1)
                        analdim = i;
                    else {
                        analdim = -1; 
                        break;
                    }
                }
            }
            if (analdim == -1) 
                continue;
            cref =lref.copy();
            if (fRegionOp.regADD(lref, ref, cref,analdim) == 0) { 
                //
                // ex)
                //          list->[inv=0,10,1]    ref->[const=20]
                // result   list->[inv=1,10,1]->[const=20]
                //
                refList.remove(lref);
                refList.add(cref);
                return ;
            } 
        }
        //
        // ex)
        //          list->[inv=0,10,1]    ref->[const=20]
        // result   list->[inv=1,10,1]->[const=20]
        //
        fUtil.Trace("ADD REF-----",5);
        refList.add(ref);
    }
    /**
    *
    *  mularyelm:
    *
    *
    *
    **/
    void mularyelm(LinkedList refList,LinkedList result,Ref_Array ref) {
        ListIterator  Ie,Ie1;
        Ref_Array lref;
        Ref_Array cref ;
        int i,dim,ret;
        boolean     MulEqu;
        int     count ;

        if (ref == null) 
            return; 

        if (refList == null) 
            return; 


        lref = null;
        cref = null;
        fUtil.Trace("mularyelm",5);
        for (Ie = refList.listIterator();Ie.hasNext();) {
            lref = (Ref_Array)Ie.next();
        fUtil.Trace("mularyelm Loop1",5);
            if (lref.VarNode.getVar() != ref.VarNode.getVar()) {
                result.add(lref);
                continue;
            }
        fUtil.Trace("mularyelm Loop2",5);
            dim = lref.dimension;
            cref =lref.copy();
            MulEqu= true;
            count =0;
            for (i=0;i<dim;i++) {
                if (lref.EQRefArrayDim(ref,i) == false) {
                    MulEqu = false;
                    fUtil.Trace("CALL regMUL",5);
                    ret = fRegionOp.regMUL(lref, ref, cref,i) ;
                    if(ret == 0)  {
                       count = count +1;
                    }
                } 
 
            }
            fUtil.Trace("mularyelm MulEqu="+MulEqu,5);
            fUtil.Trace("mularyelm Count="+count,5);
            if (MulEqu == true || count == 1) {
                ref.DebugArrayRef(fUtil);
                lref.DebugArrayRef(fUtil);
                result.add(cref);
            }
        }
    }
    /**
    *
    * subaryelm:
    *
    *
    **/
    void subaryelm(LinkedList refList,Ref_Array ref) {
        ListIterator  Ie,Ie1;
        Ref_Array lref;
        Ref_Array cref ;
        int i,dim,analdim,ret;
        LinkedList result;

        if (ref == null) return;
        if (refList == null) return;
        result = new LinkedList();

        lref = null;
        cref = null;
        ret =0;
        for (Ie =refList.listIterator();Ie.hasNext();) {
            lref = (Ref_Array)Ie.next();
            if (lref.VarNode.getVar() != ref.VarNode.getVar()) {
                result.add(lref);
                continue;
            }
            if (ref.isRegUNKNOWN() == true) {
                result.add(lref);
                continue;
            }
            dim= lref.dimension;
            analdim = -2;
            for (i=0;i<dim;i++) {
                if (lref.EQRefArrayDim(ref,i) == false) {
                    if (analdim == -2)
                        analdim = i;
                    else {
                        analdim = -1;
                        break;
                    }
                }
            }
            if (analdim == -1) {
                result.add(lref);
                continue;
            } 
            if (analdim == -2) {
                continue; // Null
            } 
            cref =lref.copy();
            ret= fRegionOp.regSUB(lref, ref, cref,analdim) ; 
            if (ret == 0) {
                result.add(lref); // ex) a[3][8]   (regSUB) a[3][10]    = a[3][8]
            } else if (ret == -1) {
                continue;         // ex) a[3][8]   (regSUB) a[3][8]     = ( null) 
            } else if (ret == -2) {
                result.add(cref); // ex) a[1-20][8](regSUB) a[10-30][8] = a[10-20][8]
            }
        }

        refList.clear();
        for (Ie1 =result.listIterator();Ie1.hasNext();)
            refList.add(Ie1.next());
    }
    /**
    *
    * printaryelmList:
    *
    *
    *
    **/
    public void printaryelmList(String comment ,LinkedList ArrayList) {
        ListIterator  Ie;
        fUtil.Trace("ARRAYLIST-----",5);
        fUtil.Trace(comment,5);
        fUtil.Trace("ListSize="+ArrayList.size(),5);
        for (Ie =ArrayList.listIterator();Ie.hasNext();) {
            Ref_Array ref=(Ref_Array)Ie.next();
            ref.DebugArrayRef(fUtil);
        }
        fUtil.Trace("----------",5);
    }
}
