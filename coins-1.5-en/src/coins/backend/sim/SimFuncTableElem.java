/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sim;

import java.util.ArrayList;
import java.util.Hashtable;

import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;
import coins.backend.lir.LirNode;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import coins.backend.Function;
import coins.backend.Op;

public class SimFuncTableElem {
    public String name;

    public Hashtable hostlocalsymbol;
    public int ihostlocalsymbol;
    public Hashtable targetlocalsymbol;
    public int itargetlocalsymbol;


    public Function acg;
    public Function bcg;
    public Function bcghost;

    public FlowGraph acgflow;
    public FlowGraph bcgflow;
    public FlowGraph bcghostflow;
    public int iacgbb, ibcgbb, ibcghostbb;
    public ArrayList acgbb = new ArrayList();
    public ArrayList bcgbb = new ArrayList();
    public ArrayList bcghostbb = new ArrayList();

    public ArrayList markersacg = new ArrayList();
    public ArrayList markersbaseacg;
    public ArrayList markerscountacg;
    public Hashtable markersbcg = new Hashtable();
    public Hashtable markersbcghost = new Hashtable();
    public Hashtable memsbcghosthash = new Hashtable();
    public ArrayList memsbcghost = new ArrayList();
    public ArrayList memsacgbb = new ArrayList();

    /*
    public ArrayList callacg = new ArrayList();
    public Hashtable callbcghost = new Hashtable();
    */
    public ArrayList acg2bcghost;
    public ArrayList acg2bcg;


    public int imarkersacg;
    public int imarkerscountacg;
    public int imarkersbcg;
    public int imarkersbcghost;
    public int imemsbcghosthash;
    public int imemsbcghost;
    public int imemsacg;

    /*
    public int icallacg;
    public int icallbcghost;
    */

    //    public ArrayList instrsbcghost = new ArrayList();
    //    public int iinstrsbcghost;
    /*  buffers for new statement of logging */
    public ArrayList instrsbcghostnew = new ArrayList();

    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    SimFuncTableElem(String funcname, 
		     Function acgf, Function bcgf, Function bcghostf,
		     int simCount, int simMem ) {
	acg = acgf;
	bcg = bcgf;
	bcghost = bcghostf;

	/*
	  how to add "call statement" to rewrite bcghostf
	  according to LocalTransformer
	  using Func.newLir: Lir factory Instance;
	  LirFactory newlir = Func.newLir;
	  newlir.node( Op...., Type, Arg1, Arg2, Arg3, ...);
	  in detail, refer to LirFactory.java and Profiler.java.
	  using addBefore or addAfter of BiLink, insert logging a Lir code.
	 */

	this.name = funcname.intern();
	acgflow = acgf.flowGraph();
	bcgflow = bcgf.flowGraph();
	bcghostflow = bcghostf.flowGraph();

	/*  make lists of basic blocks */
	/*  acg,  bcg,  bcghost */
	iacgbb = 0;
	for (BiLink p = acgflow.basicBlkList.first();
	     !p.atEnd();
	     p = p.next()) {
	    acgbb.add((BasicBlk)p.elem());
	    iacgbb++;
	}

	ibcgbb = 0;
	for (BiLink p = bcgflow.basicBlkList.first();
	     !p.atEnd();
	     p = p.next()) {
	    bcgbb.add((BasicBlk)p.elem());
	    ibcgbb++;
	}

	ibcghostbb = 0;
	for (BiLink p = bcghostflow.basicBlkList.first();
	     !p.atEnd();
	     p = p.next()) {
	    bcghostbb.add((BasicBlk)p.elem());
	    ibcghostbb++;
	}

	boolean profmode = false;
	String profmodes = FALSE;
	imarkersacg = 0;
	/*	icallacg = 0; */
	for ( int ia = 0; ia < iacgbb; ia++ ) {
	    /*  for each basic block */
	    BasicBlk elem = (BasicBlk) acgbb.get(ia);
	    /* clear marker pool */
	    markersacg.add(ImList.Empty);
	    for ( BiLink insp = elem.instrList().first();
		  !insp.atEnd();) {
		/* for each instruction of each basic block */
		LirNode node = (LirNode) insp.elem();
		BiLink next = insp.next();
		if ( node.opCode == Op.INFO ) {
		    String marker;
		    if ( node.opt != null ) {
			ImList list = node.opt;
			if ( list.elem() instanceof String ) {
			    if ( ((String)list.elem()).equals("SIMULATE")) {
				String simcom =  (String)list.elem2nd();
				if ( simcom.equals("profileOn") ) {
				    profmode = true;
				} else if ( simcom.equals("profileOff")) {
				    profmode = false;
				}
			    }
				/*  open/close are not implemented */
			} else if ( list.elem() instanceof ImList ) {
			    ImList node2 =  (ImList) list.elem();
			    if ( node2.elem() instanceof String ) {
				if ( ((String)node2.elem()).equals("DEFLABEL")) {
				    marker = ((QuotedString)node2.elem2nd()).body;
				    ImList node4 = (ImList)list.elem2nd();
				    if ( node4.elem() != null ) {
					if ( ((String)node4.elem()).equals("LINE") ) {
					    String lineno = (String)node4.elem2nd();
					    ImList tmp = (ImList)markersacg.get(ia);
						if ( profmode ) {
						    profmodes = TRUE;
						} else {
						    profmodes = FALSE;
						}
						markersacg.set(ia,
							       tmp.append(ImList.list(ImList.list(marker, lineno, profmodes))));
						if ( acg.root.isOptionSet("simulatemarklog") ) {

						    System.out.println(" " + funcname + ":" + new Integer(ia) + ":" +
								       new Integer(imarkersacg) + ":" + marker + ":" + lineno);
						}
						imarkersacg++;
						
					}
				    }
				}
			    }
			}
		    }
		}
		insp = next;
	    }
	    /* no marker check */
	    if ( markersacg.get(ia) == ImList.Empty ) {
		markersacg.set(ia, profmodes );
	    }
	    if ( acg.root.isOptionSet("simulatemarklog") ) {
		System.out.println(" markers: " + funcname + ":" + 
				   new Integer(imarkersacg));
	    }
	    /*
	    markersbaseacg.set(i,imarkersacg);
	    markerscountacg.set(i, imarkerscountacg);
	    */
	}
	imarkersbcg = 0;
	for ( int i = 0; i < ibcgbb; i++ ) {
	    BasicBlk elem = (BasicBlk) bcgbb.get(i);
	    for ( BiLink insp = elem.instrList().first();
		  !insp.atEnd();) {
		LirNode node = (LirNode) insp.elem();
		BiLink next = insp.next();
		if ( node.opCode == Op.INFO ) {
		    String marker;
		    if ( node.opt != null ) {
			ImList list = node.opt;
			if ( list.elem() instanceof ImList ) {
			    ImList node2=  (ImList) list.elem();
			    if ( node2 instanceof ImList ) {
				if ( ((String)node2.elem()).equals("DEFLABEL")) {
				    ImList node3 = node2.next();
				    ImList node4 = list.next();
				    marker = (String) ((QuotedString)(node3.elem())).body;
				    if ( node4.elem() != null ) {
					if ( node4.elem() instanceof ImList) {
					    ImList node5 = (ImList) node4.elem();
					    if ( ((String)node5.elem()).equals("LINE")) {
						String lineno = (String)node5.elem2nd();
						markersbcg.put(marker,
							       ImList.list(new Integer(i), lineno));
						imarkersbcg++;
					    }
					}
				    }
				}
			    }
			}
		    }
		    
		}
		insp = next;
	    }
	}
	imarkersbcghost = 0;
	/*
	icallbcghost = 0;
	*/
	for ( int i = 0; i < ibcghostbb; i++ ) {
	    BasicBlk elem = (BasicBlk) bcghostbb.get(i);
	    for ( BiLink insp = elem.instrList().first();
		  !insp.atEnd();) {
		LirNode node = (LirNode) insp.elem();
		BiLink next = insp.next();
		if ( node.opCode == Op.INFO ) {
                    String marker;
                    if ( node.opt != null ) {
                        ImList list = node.opt;
                        if ( list.elem() instanceof ImList ) {
                            ImList node2=  (ImList) list.elem();
                            if ( node2 instanceof ImList ) {
                                if ( ((String)node2.elem()).equals("DEFLABEL")) {
                                    ImList node3 = node2.next();
                                    ImList node4 = list.next();
                                    marker = (String) ((QuotedString)(node3.elem())).body;
                                    if ( node4.elem() != null ) {
                                        if ( node4.elem() instanceof ImList) {
                                            ImList node5 = (ImList) node4.elem();
                                            if (((String)node5.elem()).equals("LINE")) {
                                                String lineno = (String)node5.elem2nd();
                                                markersbcghost.put(marker,
                                                                   ImList.list(new Integer(i), lineno, insp));
                                                imarkersbcghost++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
		}
                insp = next;
            }
        }

	if ( bcghost.root.isOptionSet("simulateLog") ) {
	    System.out.println("marker list:");
	    for (int k = 0; k < iacgbb; k++ ) {
		if ( markersacg.get(k) instanceof ImList ) {
		    ImList lmarker = (ImList) markersacg.get(k);
		    while ( ! lmarker.isEmpty() ) {
			ImList elem = (ImList) lmarker.elem();
			lmarker = lmarker.next();
			System.out.println(" " + elem.elem() + ":" +
					   elem.elem2nd() + ":" +
					   elem.elem3rd());
			ImList elem2 =  (ImList) markersbcghost.get(elem.elem());
			/*
			  elem2.elem():    Integer: bblock id;
			  elem2.elem2nd(): String: lineno;
			  elem2.elem3rd(): BiLink: insp;
			*/
			Integer logi = (Integer) elem2.elem();
			String  loglineno = (String) elem2.elem2nd();
			LirNode loglir = (LirNode)((BiLink)elem2.elem3rd()).elem();
			ImList logsexp = (ImList) loglir.toSexp();
			System.out.println("  " + logi + ":" +
					   loglineno + ":" +
					   logsexp.toString() );
		    }
		} if ( markersacg.get(k) instanceof String ) {
		    System.out.println("  " + new Integer(k) + ":" + "no markers");
		}
	    }
	}


	/* memory access */
	profmode = false;

        for ( int i = 0; i < iacgbb; i++ ) {
            BasicBlk elem;
            ArrayList lmemsacg;
            elem = (BasicBlk) acgbb.get(i);
            lmemsacg = new ArrayList();
            imemsacg = 0;
            for (BiLink insp = elem.instrList().first(); !insp.atEnd();) {
                /*  for each statement */
                LirNode node = (LirNode) insp.elem();
                BiLink next = insp.next();
                /*  search mems in each expression */
                profmode = TreeTraverse(node, lmemsacg, false, profmode);
                insp = next;
            }
            /* for each basic block */
            /*   {#mems: int, lmemsacg: arraylist}  */
            memsacgbb.add(i, ImList.list(new Integer(imemsacg),lmemsacg));
        }

        //      iinstrsbcghost = 0;
        imemsbcghosthash = 0;
	profmode = false;

        for ( int i = 0; i < ibcghostbb; i++ ) {
            BasicBlk elem = (BasicBlk) bcghostbb.get(i);
            //      int lno = 0;
            ArrayList lmemsbcghost = new ArrayList();
            for (BiLink insp = elem.instrList().first(); !insp.atEnd();) {
                LirNode node = (LirNode) insp.elem();
                BiLink next = insp.next();
                /* make instruction tables */
                //              instrsbcghost.add(iinstrsbcghost, insp);
                //              instrsbcghostnew.add(iinstrsbcghost, null);
                //              iinstrsbcghost++;

                profmode = 
		    TreeTraverseHash(node, lmemsbcghost, memsbcghosthash, 
				     false,// lno, 
				     new Integer(i), insp, profmode);

                //              lno++;
                insp = next;
            }
            memsbcghost.add(i, lmemsbcghost);
        }
    }


    private boolean TreeTraverse(LirNode node, ArrayList mems, boolean lvalue,
				 boolean profmode )
    {
        int opcode = node.opCode;
	boolean profmode2= profmode;
	String profmodes;
        switch ( opcode ) {
        case Op.INFO:
	    ImList infonode = node.opt;
	    if ( ! infonode.isEmpty() ) {
		if ( (infonode.elem()) instanceof String ) {
		    if ( ((String)(infonode.elem())).equals("SIMULATE") ) {
			String simmode = (String)(infonode.elem2nd());
			if ( simmode.equals("profileOn") ) {
			    profmode2 = true;
			} else if ( simmode.equals("profileOff") ) {
			    profmode2 = false;
			}
		    }
		}
	    }
            break;
        case Op.SET: 
            {
              profmode2 = TreeTraverse(node.kid(0), mems, true, profmode2 );
              profmode2 = TreeTraverse(node.kid(1), mems, false, profmode2 );
            }
            break;
        case Op.CALL:
            {
                profmode2 = TreeTraverse(node.kid(0), mems, false, profmode2 );
                profmode2 = TreeTraverse(node.kid(1), mems, false, profmode2 );
                LirNode retval = node.kid(2);
                int nret = retval.nKids();
                for ( int i = 0; i < nret; i++ ) {
                    profmode2 =
			TreeTraverse(retval.kid(i), mems, true, profmode2 );
                }
            }
            break;
        case Op.ASM:
            /* ignore */
            break;
        case Op.MEM:
            {
                /*   ImList: ( ID, VarName, Node, lvalue ) */
                /*
                  ( MEM type (ADD type (REG type Fp) (INTCONST type Offset)))
                  ( MEM type (SUB type (REG type Fp) (INTCONST type Offset)))
                  ( MEM type (REG type Fp))
                  ( MEM type (STATIC type Name))
                  ->   variable on stack
                  (MEM type arg  &id Id ) or (MEM type arg &id (Name Id)) 
                  (MEM type (FRAME Name))
                  (STATIC type Name) ???
                */
                ImList memidnode = node.opt;
                profmode2 = TreeTraverse(node.kid(0), mems, false, profmode2 );
                if ( memidnode.isEmpty() ) {
                    String slv;
                    if ( lvalue ) {
                        slv = "true";
                    } else {
                        slv = "false";
                    }
		    if ( profmode2 ) {
			profmodes = SimFuncTableElem.TRUE;
		    } else {
			profmodes = SimFuncTableElem.FALSE;
		    }
                    mems.add(ImList.list("-1",
                                         (Object)"", 
                                         (Object)node, 
                                         (Object)slv,
					 (Object) new Integer(imemsacg),
					 (Object) profmodes ));
                    imemsacg++;
                } else {
                    if ( ((String)memidnode.elem()).equals("&id") ) {
                        if ( memidnode.elem2nd() instanceof ImList ) {
                            ImList memidnode1 = (ImList) memidnode.elem2nd();
                            String name = ((QuotedString) memidnode1.elem()).body;
                            String memid = (String) memidnode1.elem2nd();
                            String slv;
                            if ( lvalue ) {
                                slv = "true";
                            } else {
                                slv = "false";
                            }
			    if ( profmode2 ) {
				profmodes = SimFuncTableElem.TRUE;
			    } else {
				profmodes = SimFuncTableElem.FALSE;
			    }

                            mems.add(ImList.list((Object)memid, 
                                                 (Object)name, 
                                                 (Object)node, 
                                                 (Object)slv,
						 (Object) new Integer(imemsacg),
						 (Object)profmodes));
                            imemsacg++;
                        } else {
                            String memid = (String) memidnode.elem2nd();
                            String slv;
                            if ( lvalue ) {
                                slv = "true";
			    } else {
				slv = "false";
			    }
			    if ( profmode2 ) {
				profmodes = SimFuncTableElem.TRUE;
			    } else {
				profmodes = SimFuncTableElem.FALSE;
			    }

			    mems.add(ImList.list((Object)memid, 
						 (Object)"", 
						 (Object)node, 
						 (Object)slv,
						 (Object) new Integer(imemsacg),
						 (Object)profmodes));
			    imemsacg++;

			}

			/*
			if ( memidnode.elem(1) instanceof ImList ) {
			    ImList memidnode1 = (ImList) memidnode.elem(1);
			    String name = ((QuotedString) memidnode1.elem(0)).body;
			    Integer memid = (Integer) memidnode1.elem(1);
			    mems.add(ImList.list( memid, name, node ));
			}
			*/
		    }
		}
	    }
	    break;
	default:
	    {
		int narity = node.nKids();
		for ( int i = 0; i < narity; i++ ) {
		    profmode2 = 
			TreeTraverse(node.kid(i), mems, false, profmode2 );
		}
	    }
	    break;
	}
	return profmode2;
    }

    private boolean TreeTraverseHash(LirNode node, ArrayList mems0, 
				     Hashtable mems, boolean lvalue, // int lno, 
				     Integer ibb, BiLink insp, boolean profmode1)
    {
	int opcode = node.opCode;
	int nk = node.nKids();
	boolean profmode2 = profmode1;
	String profmodes2;
	switch ( opcode ) {
	case Op.INFO:
	    break;
	case Op.SET: 
	    {
		profmode2 = 
		    TreeTraverseHash(node.kid(0), mems0, mems, true, //lno, 
				     ibb, insp, profmode2);
		profmode2 =
		    TreeTraverseHash(node.kid(1), mems0, mems, false, //lno, 
				     ibb, insp, profmode2);
	    }
	    break;
	case Op.CALL:
	    {
		profmode2 =
		    TreeTraverseHash(node.kid(0), mems0, mems, false, //lno, 
				     ibb, insp, profmode2);
		profmode2 =
		    TreeTraverseHash(node.kid(1), mems0, mems, false, //lno, 
				     ibb, insp, profmode2);
		LirNode retval = node.kid(2);
		int nret = retval.nKids();
		for ( int i = 0; i < nret; i++ ) {
		    profmode2 = 
			TreeTraverseHash(retval.kid(i), mems0, mems, true, //lno,
					 ibb, insp, profmode2);
		}
	    }
	    break;
	case Op.ASM:
	    /* ignore */
	    break;
	case Op.MEM:
	    {
		/*   Lno, VarName, Node  */
		ImList memidnode = node.opt;
		profmode2 = 
		    TreeTraverseHash(node.kid(0), mems0, mems, false, //lno, 
				     ibb, insp, profmode2);
		if ( profmode2 ) {
		    profmodes2 = "TRUE";
		} else{
		    profmodes2 = "FALSE";
		}

		if ( memidnode.isEmpty() ) {
		    mems0.add(ImList.list(node, insp, ibb, "no", profmodes2));
			      //new Integer(lno), new Integer(iinstrsbcghost)));
		    imemsbcghost++;
		} else {
		    if ( ((String)memidnode.elem()).equals("&id") ) {
			if ( memidnode.elem(1) instanceof ImList ) {
			    ImList memidnode1 = (ImList) memidnode.elem(1);
			    String name = ((QuotedString) memidnode1.elem(0)).body;
			    String memid = (String)memidnode1.elem(1);
			    mems.put(memid, ImList.list(name, node, insp, ibb, "no", profmodes2));
			    imemsbcghosthash++;
			} else {
			    String memid = (String)memidnode.elem(1);
			    mems.put(memid, ImList.list("", node, insp, ibb, "no", profmodes2));
			    imemsbcghosthash++;
			}
		    }
		}
	    }
	    break;
	default:
	    for ( int i = 0; i < nk; i++ ) {
		profmode2 = 
		    TreeTraverseHash(node.kid(i),mems0, mems, false, //lno, 
				     ibb, insp, profmode2);
	    }
	}
	return profmode2;
    }
	
		/* register (Function)p.elem() to FunctionTable */
		/* name */
		/* cfg of acg */
		/* cfg of bcg */
		/* cfg of bcghost */
		/* array of basic blocks of acg */
		/* array of basic blocks of bcg */
		/* array of basic blocks of bcghost */
		/* hash map of BCG markers */
		/* hash map of BCGhost mem expressions */


}
