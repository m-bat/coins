/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */

package coins.backend.sim;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.ana.Dominators;
import coins.backend.cfg.BasicBlk;
import coins.backend.gen.CodeGenerator;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import coins.backend.Module;
//import coins.backend.ModuleForProfiling;
import coins.backend.Op;
import coins.backend.Root;
import coins.backend.Function;
import coins.backend.Type;
import coins.backend.Storage;
import coins.backend.CantHappenException;
//##75 import coins.backend.sim.SimFuncTable;
import coins.backend.sim.SimFuncTableElem;

public class SimFuncTable {

    public Module acg, bcg, bcghost;
    public int nelems;
    public ArrayList ftable;
    public Hashtable hostglobalsymbol;
    public int ihostglobalsymbol;
    public Hashtable targetglobalsymbol;
    public int itargetglobalsymbol;

    /*  profile: counter log function */
    public String logfuncname;
    public Symbol logfuncsymbol;

    /*  profile: counters */
    public Symbol counterArray;
    public Symbol icounterArray;
    private int counterType;
    private int typeAddress;
    private int counterSize;
    private int counterLength;

    /*  profile: module id symbol*/
    public Symbol memAccessSymbol;

    /*  profile: fs log module id symbol */
    public String fsfuncname;
    public Symbol fsfuncsymbol;

    /*  profile: symbol table */
    public String symbolFileName;

    public TypicalPattern tp = new TypicalPattern();

    private int[] markersbaseindex;
    private int[] markerslineno;
    private String[] markersfuncname;

    SimFuncTable(Module acg, Module bcg, Module bcghost,
     int simCount, int simMem) {
  this.acg = acg;
  this.bcg = bcg;
  this.bcghost = bcghost;

  BiList elembcg = bcg.elements;
  BiList elembcghost = bcghost.elements;
  BiList elemacg = acg.elements;
  BiLink pbcg = elembcg.first();
  BiLink pbcghost = elembcghost.first();
  BiLink pacg = elemacg.first();
  Function fbcg;
  Function fbcghost = null;
  Function facg = null;
  String funcname;

  tp = new TypicalPattern();

  ftable = new ArrayList();
  nelems = 0;
  for (; !pbcg.atEnd(); pbcg = pbcg.next()) {
      if ( pbcg.elem() instanceof Function ) {
    fbcg = (Function) pbcg.elem();
    funcname = fbcg.symbol.name;
    for (;;) {
        for (; !pbcghost.atEnd(); pbcghost = pbcghost.next()) {
      if ( pbcghost.elem() instanceof Function ) {
          fbcghost = (Function) pbcghost.elem();
          if ( funcname == fbcghost.symbol.name) break;
      }
        }
        if ( pbcghost.atEnd() )
      pbcghost = elembcghost.first();
        else break;
    }

    for (;;) {
        for (; !pacg.atEnd(); pacg = pacg.next()) {
      if ( pacg.elem() instanceof Function ) {
          facg = (Function) pacg.elem();
          if ( funcname == facg.symbol.name) break;
      }
        }
        if ( pacg.atEnd() )
      pacg = elemacg.first();
        else break;
    }
    ftable.add(new SimFuncTableElem(funcname, facg, fbcg, fbcghost,
            simCount, simMem));
    nelems++;
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
    }


    /** these three local function are copied from Profiler.java */
    /** Remove suffix from filename. **/
    private static String stem(String old) {
  int pos = old.indexOf('.');
  return (pos >= 0 ? old.substring(0, pos) : old);
    }

    /** Return true if character c is a component of the identifier. **/
    private static boolean isIdentifier(char c) {
  return ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z'
    || '0' <= c && c <= '9' );
    }

    /** Convert a filename to identifier. **/
    private static String createIdentifier(String filename) {
  char[] str = filename.toCharArray();
  StringBuffer buf = new StringBuffer("_");
  for (int i = 0; i < str.length; i++) {
      if (isIdentifier(str[i])) {
    buf.append(str[i]);
      } else if ( str[i] == '_' ) {
    buf.append("__");
      } else {
    buf.append("_");
    buf.append(new Integer(str[i]));
    buf.append("_");
      }
  }
  return buf.toString();
    }

    public void setTypicalPattern(TypicalPattern Type) {
  tp = Type;
    }

    public void analyze(Root root, String targetName, String hostName,
      int simCount, int simMem) {

  /*  simCount: 1 function, 2:  bblcok, 3: both */


  markersbaseindex = new int[nelems+1]; /* for functions */

  LirFactory lir = bcghost.newLir;
  typeAddress = bcghost.targetMachine.typeAddress;

  /*********************************************************/
  /* profile:                                              */
  /* allocate counter data */

  counterType = Type.type(Type.INT, 32);
  counterSize = Type.bytes(counterType);

  counterLength = 0;
  for ( int i = 0; i < nelems; i++ ) {
      markersbaseindex[i] = counterLength;
      SimFuncTableElem sfte;
      sfte = (SimFuncTableElem)ftable.get(i);
      counterLength += sfte.imarkersacg;
      /*	    counterLength += sfte.imarkersacgarray; */
      if ( root.isOptionSet("simulatemarklog") ) {
    System.out.println("mark:"+ new Integer(markersbaseindex[i]) +
           ":" + new Integer(sfte.imarkersacg));
      }
  }
  markersbaseindex[nelems] = counterLength;

  markerslineno = new int[counterLength];
  markersfuncname = new String[counterLength];

  String vecName = ("__" + createIdentifier(stem(bcghost.name)) +
        "_countervec").intern();

  String ivecName = ("__" + createIdentifier(stem(bcghost.name)) +
        "_icountervec").intern();

  counterArray = bcghost.addSymbol
      (vecName, Storage.STATIC, Type.type(Type.AGGREGATE, 0),
       8, ".bss", "XDEF", null);
  bcghost.addData(counterArray,
      lir.node
      (Op.SPACE, Type.UNKNOWN,
       lir.iconst(typeAddress, counterLength*counterSize)));

  icounterArray = bcghost.addSymbol
      (ivecName, Storage.STATIC, Type.type(Type.INT,32),
       4, ".rodata", "XDEF", null);
  /* .data or .rodata or .text */
  bcghost.addData(icounterArray,
      lir.node
      (Op.INTCONST, Type.type(Type.INT,32),
       (long)counterLength));


  /* allocate memory access data */

  /*  void ** __ModuleName_memaccessmoduleid */
  String memAccessName = ("__" + createIdentifier(stem(bcghost.name)) + "_memaccessmoduleid").intern();
  memAccessSymbol = bcghost.addSymbol
      (memAccessName, Storage.STATIC, Type.INT, //Type.bytes(Type.INT),
       4, ".bss", "XREF", null);
  bcghost.addData(memAccessSymbol,
      lir.node
      (Op.SPACE, Type.UNKNOWN,
       lir.iconst(typeAddress, (long)Type.bytes(Type.type(Type.INT,32)))));

  /*   allocate logging function  */

  logfuncname = "__coins_sim_log_9".intern();
  logfuncsymbol = bcghost.addSymbol
      ( logfuncname, Storage.STATIC, Type.UNKNOWN, 4, ".text",
        "XREF", null );

  /*   frame size logging function */
  fsfuncname = "__coins_sim_fslog_4".intern();
  fsfuncsymbol = bcghost.addSymbol
      ( fsfuncname, Storage.STATIC, Type.UNKNOWN, 4, ".text",
        "XREF", null );

  /*********************************************************/


  /*  for each function */

        for ( int i = 0; i < nelems; i++ ) {
            SimFuncTableElem sfte;
            sfte = (SimFuncTableElem)ftable.get(i);

      if ( root.isOptionSet("simulatemarklog") ) {
    System.out.println("markers: " + sfte.name + ":" +
           new Integer(markersbaseindex[i]));
      }
            ///////////////
      Dominators dom = (Dominators)(sfte.acg).require(Dominators.analyzer);

      /* bb: for each basic block */

            for ( int j = 0; j < sfte.iacgbb; j++ ) {
                ArrayList tmp = sfte.memsacgbb;
                ImList bb = (ImList)tmp.get(j);

                /*    { #mems: int, lmemsacg: arraylist } */
                /* ImList bb = (ImList)sfte.memsacgbb.get(j); */

                int ilmemsacg = ((Integer)bb.elem()).intValue();
                ArrayList lmemsacg = (ArrayList)(bb.elem2nd());

                /*  lmemsacg: mem expressions list */
                /*  ilmemsacg:  #memacg */

    /* memshash is for BCGHOST */
                Hashtable memshash= sfte.memsbcghosthash;

    /*  for each mem expression in ACG */

                for ( int k = 0; k < ilmemsacg; k++ ) {

                    ImList llmems =(ImList) lmemsacg.get(k);

                    /*    id, name, node, lvalue */

                    String memid = (String)llmems.elem();
                    String  name  = (String)llmems.elem2nd();
                    LirNode node  = (LirNode)llmems.elem3rd();
                    String slvalue = (String)llmems.elem4th();
        String profmodes = (String) llmems.elem6th();

        if ( root.isOptionSet("simulatelog") ) {
      System.out.println("A:" + new Integer(j) + ":"
             +  memid + ":"
             + name + ":" + slvalue
             + ":" + node.toString());
        }

                    ImList mems = (ImList)memshash.get(memid);

                    /*  if memid is in BCGHOST */

                    if ( mems != null ) {
      /* set use flag in BCGHOST */
                        memshash.put(memid,
                                           ImList.list(mems.elem(),
                                                       mems.elem2nd(),
                                                       mems.elem3rd(),
                                                       mems.elem4th(),
                   mems.elem6th(),
                                                       "yes"));

      if ( root.isOptionSet("simulatelog") ) {
          System.out.println("H:" + memid + ":"
                 + (String)mems.elem() + ":"
                 + ((LirNode)mems.elem2nd()).toString()
                 + ":"
                 + ((LirNode)((BiLink)mems.elem3rd()).elem()).toString()
                 + ":" + (Integer)mems.elem4th()
                 + ":" + profmodes );
      }
                    }
                }

            }

      /*  listing up nochecked mems info in membcghosthash */

      /*  no use members with ID move to no id collection */
      /* this no id collection is used for unknown memory
               access */

            Hashtable memshash = sfte.memsbcghosthash;
            Enumeration keys = memshash.keys();
            for (Enumeration e = keys;
                 e.hasMoreElements(); ) {
                String memid = (String) e.nextElement();
                ImList memsinfo = (ImList) memshash.get(memid);
    String profmodes = (String) memsinfo.elem5th();
                String used = (String) memsinfo.elem6th();
                if ( used.equals("no") && profmodes.equals("TRUE")) {
                    String name = (String) memsinfo.elem();
                    LirNode node = (LirNode) memsinfo.elem2nd();
                    BiLink insp = (BiLink) memsinfo.elem3rd();
                    Integer ibb = (Integer)memsinfo.elem4th();
                    ArrayList mems0 = (ArrayList)(sfte.memsbcghost).get(ibb.intValue());
                    mems0.add(ImList.list(node, insp, ibb, "yes"));
                }
            }
      if ( root.isOptionSet("simulatelog") ) {
    for ( int j = 0; j < sfte.ibcghostbb; j++ ) {
        ArrayList tmp = sfte.memsbcghost;
        ArrayList lmemsbcghost = (ArrayList) tmp.get(j);
        int ilmemsbcghost = lmemsbcghost.size();
        for ( int k = 0; k < ilmemsbcghost; k++ ) {
      Integer sj= new Integer(j);
      ImList il = (ImList) (lmemsbcghost.get(k));
      System.out.println(sj + ":" +
             ((LirNode)il.elem()).toString() + ":" +
             ((LirNode)(((BiLink)(il.elem2nd())).elem())).toString() + ":" +
             (Integer)(il.elem3rd()) + ":" +
             (String)(il.elem4th()) );
        }
                }
            }

            /*  listing markers in ACG */

      if ( bcghost.root.isOptionSet("simulateLog") ) {
    if ( root.isOptionSet("simulatemarklog") ) {
        System.out.println("marker list:");
    }
    for (int k = 0; k < sfte.iacgbb; k++ ) {
        if ( sfte.markersacg.get(k) instanceof ImList ) {
      ImList lmarker = (ImList) sfte.markersacg.get(k);
      /*			while ( ! lmarker.isEmpty() ) { */
      while ( lmarker.elem() != null ) {
          if ( lmarker.elem() instanceof String ) {
        lmarker = lmarker.next();
        continue;
          }
          ImList elem = (ImList) lmarker.elem();
          lmarker = lmarker.next();
          System.out.println(" " + elem.elem() + ":" +
               elem.elem2nd() + ":" +
               elem.elem3rd());
          ImList elem2 =  (ImList) sfte.markersbcghost.get(elem.elem());
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
        } if ( sfte.markersacg.get(k) instanceof String ) {
      System.out.println("  " + new Integer(k) + ":" + "no markers");
        }
    }
      }

      if ( bcghost.root.isOptionSet("simulateLog") ) {
    System.out.println("marker list(1):");
    for (int k = 0; k < sfte.iacgbb; k++ ) {
        if ( sfte.markersacg.get(k) instanceof ImList ) {
      ImList lmarker = (ImList) sfte.markersacg.get(k);
      /* lmarker: (elem . ...)*/
      /* elem: (label lineno T/F)*/
      /*	while ( ! lmarker.isEmpty() ) { */
      while ( lmarker.elem() != null ) {
          if ( lmarker.elem() instanceof String ) {
        lmarker = lmarker.next();
        continue;
          }
          ImList elem = (ImList) lmarker.elem();
          lmarker = lmarker.next();
          System.out.println(" " + elem.elem() + ":" +
                 elem.elem2nd() + ":" +
                 elem.elem3rd());
          ImList elem2 =  (ImList) sfte.markersbcghost.get(elem.elem());
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
        } if ( sfte.markersacg.get(k) instanceof String ) {
      System.out.println("  " + new Integer(k) + ":" + "no markers");
        }
    }
      }

            /*  if no markers , try a dominant block of it */
      if ( sfte == null ) {
            for ( int j = 0; j < sfte.iacgbb; j++ ) {
    if ( (sfte.markersacg.get(j) instanceof String) ) {
        /* no marker basic block */
        BasicBlk elem = (BasicBlk) sfte.acgbb.get(j);
        do {
      BasicBlk domb = dom.immDominator(elem);
      for ( int k = 0; k < sfte.iacgbb; k++ ) {
          if ( ((BasicBlk)sfte.acgbb.get(k)) == domb ) {
        if ( sfte.markersacg.get(k) instanceof ImList ) {
            sfte.markersacg.set(j, sfte.markersacg.get(k));
            /* patch for no marker basic blocks */
            (sfte.imarkersacg)++;
            break;
        } else {
            elem = domb;
            break;
        }
          }
      }

        } while ( (sfte.markersacg).get(j) instanceof String );
    }
      }
      }

      if ( bcghost.root.isOptionSet("simulateLog") ) {
    System.out.println("marker list(2):");
    for (int k = 0; k < sfte.iacgbb; k++ ) {
        if ( sfte.markersacg.get(k) instanceof ImList ) {
      ImList lmarker = (ImList) sfte.markersacg.get(k);
      /* lmarker: (elem . ...)*/
      /* elem: (label lineno T/F)*/
      /* while ( ! lmarker.isEmpty() ) { */
      while ( lmarker.elem() != null ) {
          if ( lmarker.elem() instanceof String ) {
        lmarker = lmarker.next();
        continue;
          }
          ImList elem = (ImList) lmarker.elem();
          lmarker = lmarker.next();
          System.out.println(" " + elem.elem() + ":" +
                 elem.elem2nd() + ":" +
                 elem.elem3rd());
          ImList elem2 =  (ImList) sfte.markersbcghost.get(elem.elem());
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
        } if ( sfte.markersacg.get(k) instanceof String ) {
      System.out.println("  " + new Integer(k) + ":" + "no markers");
        }
    }
      }


            /*  search bb in BCG */
      /*  make  BB correspondense table for
                ACG and BCGHOST */

      /* add logging code to BCGHOST */
      /*  - generate codes   */
      /*     --  memory access */
      /*     --  execute counter  */


      if ( simMem != 0 ) {
      /* case of using mem id */
      Hashtable memsbhash = sfte.memsbcghosthash;

      ArrayList pendings = new ArrayList();
      int ipendings = 0;

      for ( int j = 0; j < sfte.iacgbb; j++ ) {
    ImList bb = (ImList)((ArrayList)sfte.memsacgbb).get(j);
    int ilmemsacg = ((Integer)bb.elem()).intValue();
    ArrayList lmemsacg = (ArrayList)(bb.elem2nd());
    for ( int k = 0; k < ilmemsacg; k++ ) {
        ImList lmems = (ImList)lmemsacg.get(k);
        String memid = (String)lmems.elem();
        String profmodes = (String)lmems.elem6th();
        if ( profmodes.equals("TRUE") ) {
      if (! memid.equals("-1")) {
          ImList llmems = (ImList) memsbhash.get(memid);
          BiLink insp = (BiLink) llmems.elem3rd();
          LirNode node = (LirNode) llmems.elem2nd();
          String slvalue = (String) lmems.elem4th();
          setmemlogsA(insp, node, slvalue, sfte, "x86", root, i);
      } else {
          LirNode node = (LirNode) lmems.elem3rd();
          String  slvalue = (String) lmems.elem4th();
          if ( sfte.markersacg.get(j) instanceof ImList ) {
        ImList markers = (ImList)sfte.markersacg.get(j);
        String marker;
        if ( markers.elem() != null ) {
            ImList ile = (ImList)markers.elem();
            marker = (String)ile.elem();
        } else {
            /* illegal case */
            marker = null;
        }
        Hashtable lbcghosthash = sfte.markersbcghost;
        ImList val = (ImList)lbcghosthash.get(marker);
        int ib = ((Integer)val.elem()).intValue();
        BiLink insp = (BiLink) val.elem3rd();

        /* got basic block number */
        if ( tp.analyze(node, targetName, root) ) {
            setmemlogsB(insp, ib, node, slvalue, sfte, targetName, root, i);
        } else {
            pendings.add(ImList.list((Integer)val.elem(),
                   node, targetName));
            ipendings++;
        }
          }
      }
        }
    }
      }
      /* pendings: unknown memory accesses */
      int ipendingbb = -1;
      for ( int j = 0; j < ipendings; j++ ) {
    ImList list = (ImList)pendings.get(j);
    int ip = ((Integer) (list.elem())).intValue();
    if ( ip != ipendingbb ) {
        ipendingbb = ip;
        setmemlogsC(ip, (LirNode)list.elem2nd(), sfte, targetName, root, i);
    }
      }
      if (root.isOptionSet("simulateLog")) {
    System.out.println("Simulation LIR (memAccess)");
    ImList.printIt(root.debOut, sfte.bcghost.toSexp());
      }
      }

      /*  prologue and eplogue  */
      CodeGenerator targetCG = acg.targetMachine.getTargetCG();
      int  framesize = sfte.acg.frameSize();

      for ( int j = 0; j < sfte.ibcghostbb; j++ ) {
    BasicBlk elem = (BasicBlk) sfte.bcghostbb.get(j);
    for ( BiLink insp = elem.instrList().first();
          !insp.atEnd(); ) {
        LirNode node = (LirNode) insp.elem();
        if ( node.opCode == Op.PROLOGUE ) {
      setframesize(true, insp, sfte, framesize, simCount);
        } else if ( node.opCode == Op.EPILOGUE ) {
      setframesize(false, insp, sfte, framesize, simCount);
        }
        insp = insp.next();
    }
      }

      if (root.isOptionSet("simulateLog")) {
    System.out.println("Simulation LIR (FramePointer)");
    ImList.printIt(root.debOut, sfte.bcghost.toSexp());
      }

      /*  logging dap  */
      if ( simCount != 0 ) {
    if ( bcghost.root.isOptionSet("simulateLog") ) {
        System.out.println("marker list(3):");
        for (int k = 0; k < sfte.iacgbb; k++ ) {
      if ( sfte.markersacg.get(k) instanceof ImList ) {
          ImList lmarker = (ImList) sfte.markersacg.get(k);
          while ( lmarker.elem() != null ) {
        if ( lmarker.elem() instanceof String ) {
            lmarker = lmarker.next();
            continue;
        }
        ImList elem = (ImList) lmarker.elem();
        lmarker = lmarker.next();
        System.out.println(" " + elem.elem() + ":" +
               elem.elem2nd() + ":" +
               elem.elem3rd());
        ImList elem2 =  (ImList) sfte.markersbcghost.get(elem.elem());
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
      } if ( sfte.markersacg.get(k) instanceof String ) {
          System.out.println("  " + new Integer(k) + ":" + "no markers");
      }
        }
    }


    /*   logging processing for ... */

    int iacgbblim;
    if ( simCount == 1 ) {
        iacgbblim = 1;
    } else {
        iacgbblim = sfte.iacgbb;
    }

    int jmarker = 0;
    for ( int j = 0; j < iacgbblim; j++ ) {
        ArrayList sftemarkersacg = sfte.markersacg;
        if ( sftemarkersacg.get(j) instanceof ImList ) {
      ImList markers = (ImList)sftemarkersacg.get(j);
      ImList minfo;
      /*    for (  ; ! markers.isEmpty(); markers = markers.next()) {     */
      for (  ; markers.elem() != null; markers = markers.next()) {
          if ( markers.elem() instanceof String ) {
        continue;
          }
          minfo = (ImList) markers.elem();
          String marker = (String) minfo.elem();
          String lineno = (String) minfo.elem2nd();
          String simon  = (String) minfo.elem3rd();
          if ( simon.equals("TRUE") ) {
        ImList bmarker = (ImList)((sfte.markersbcghost).get(marker));
        int ibb = ((Integer) bmarker.elem()).intValue();
        BiLink binsp  = (BiLink) bmarker.elem3rd();
        /*
          setmarkerlogs(ibb, j, marker, lineno, binsp, sfte, root, i);
        */
        setmarkerlogs(ibb, j, jmarker, marker, lineno, binsp, sfte, root, i);
          }
          jmarker++;
      }
        }
    }
    if ( root.isOptionSet("simulatemarklog") ) {
        System.out.println("     : " + new Integer(jmarker));
    }
      }
  }
  if (root.traceOK("simulatedump", 1)) {
      root.debOut.println("External-LIR Format:");
      ImList.printIt(root.debOut, bcghost.toSexp());
  }
  /*   symbol table file generate  */

  symbolFileName = (createIdentifier(stem(bcghost.name)) +
        "_symbols.tbl");
  symbolFileOut(symbolFileName, acg, bcghost);

    }



    //   ImList.list(new QuotedString(name),
    //              Storage.toString(storage),
    //                 Type.toString(type),
    //                 boundary + "",
    //                 new QuotedString(segment),
    //                 linkage)

    public void symbolTable(Root root, Module module, String kind){
  /* 1stly, global table */
  ImList symSexp = (ImList)module.toSexp();
  //	System.out.println(symacgSexp.toString());
  Hashtable globalsymbol;
  int iglobalsymbol;

  //	if ( kind.equals("Host") ) {
  //	    globalsymbol = new Hashtable();
  //	    iglobalsymbol = 0;
  //	}

  globalsymbol = new Hashtable();
  iglobalsymbol = 0;

  String modName = ((QuotedString) symSexp.elem2nd()).body;
  ImList body  = (ImList) ((ImList)symSexp.next()).next();
  for ( ImList me = (ImList) body.elem(); me != null;
        body = body.next(), me = (ImList) body.elem()) {
      if ( ((String)me.elem()).equals("FUNCTION")) {
    String funcName = ((QuotedString)me.elem2nd()).body;
    ImList lbody = ((ImList) me.next()).next();
    SimFuncTableElem sfte = null;
    Hashtable localsymbol;
    int  ilocalsymbol;

    for ( int k = 0; k < nelems; k++ ) {
        sfte = (SimFuncTableElem)ftable.get(k);
        if ( sfte.name.equals(funcName)) {	break;   }
    }

    localsymbol = new Hashtable();
    ilocalsymbol = 0;

    for ( ImList le = (ImList) lbody.elem(); le != null;
          lbody = lbody.next(), le = (ImList) lbody.elem()) {
        if ( ((String)le.elem()).equals("SYMTAB")) {
      /*
      System.out.println("Local." + kind);
      */
      ImList syms = le.next();
      for ( ImList e = (ImList)syms.elem(); e != null;
            syms = syms.next(), e = (ImList) syms.elem()) {
          String name = ((QuotedString)e.elem()).body;
          String storage = (String)e.elem2nd();
          if ( storage.equals("REG") ) {
          } else {
        String stype = (String)e.elem3rd();
        String segment = (String)e.elem4th();
        String linkage;
        if ( e.elem5th() instanceof QuotedString ) {
            linkage = ((QuotedString)e.elem5th()).body;
        } else {
            linkage = (String)e.elem5th();
        }
        String newname = name.intern();
        if ( localsymbol.get(newname) == null ) {
            ImList symelem = ImList.list
          ( new Integer(ilocalsymbol),
            storage, stype, segment, linkage );
            localsymbol.put(newname, symelem);
            ilocalsymbol++;

            if ( root.isOptionSet("profsymlog") ) {

          System.out.println( newname + ":" +
                  storage + ":" +
                  stype + ":" +
                  segment + ":" +
                  linkage );
            }
        }
          }
      }
        }
        if ( kind.equals("Host") ) {
      sfte.hostlocalsymbol = localsymbol;
      sfte.ihostlocalsymbol = ilocalsymbol;
        } else {
      sfte.targetlocalsymbol = localsymbol;
      sfte.itargetlocalsymbol = ilocalsymbol;
        }
    }
      } else if ( ((String)me.elem()).equals("SYMTAB") ) {
    /*
    System.out.println("Global." + kind );
    */
    ImList syms = me.next();
    for ( ImList e = (ImList)syms.elem(); e != null;
          syms = syms.next(), e = (ImList) syms.elem()) {
        String name = (((QuotedString)e.elem()).body).intern();
        String storage = (String)e.elem2nd();
        if ( storage.equals("REG") ) {
        } else {
      String stype = (String)e.elem3rd();
      String segment = (String)e.elem4th();
      String linkage;
      if ( e.elem5th() instanceof QuotedString ) {
          linkage = ((QuotedString)e.elem5th()).body;
      } else {
          linkage = (String)e.elem5th();
      }
      if ( globalsymbol.get(name) == null ) {
          ImList symelem = ImList.list
        ( new Integer(iglobalsymbol),
          storage, stype, segment, linkage );
          globalsymbol.put(name,symelem);
          iglobalsymbol++;
          if ( root.isOptionSet("profsymlog") ) {
        System.out.println( name + ":" +
                new Integer(iglobalsymbol-1) + ":" +
                storage + ":" +
                stype + ":" +
                segment + ":" +
                linkage );
          }
      }
                    }
                }
    if ( kind.equals("Host") ) {
        hostglobalsymbol = globalsymbol;
        ihostglobalsymbol = iglobalsymbol;
    } else {
        targetglobalsymbol = globalsymbol;
        itargetglobalsymbol = iglobalsymbol;
    }
            } else if ( ((String)me.elem()).equals("DATA") ) {
            } else {
    if ( root.isOptionSet("profsymlog") ) {
        System.out.println(me.toString());
    }
            }
        }

    }




    /*  generate symbol table */
    /*  also generate marker-lineno info for each marker */

    private  void symbolFileOut(String fileName, Module acg, Module bcghost) {
  PrintWriter out;
  try {
      out = new PrintWriter(new FileOutputStream(fileName));
  } catch ( FileNotFoundException e) {
      throw new CantHappenException();
  }

  out.println("# symbol table for " + acg.name);
  out.println("Target,0,0,0,0,0");
  out.println("Global,0,0,0,0,0");
  symbolOut(out, targetglobalsymbol, itargetglobalsymbol);
  int findexbase = 0;
  for ( int i = 0; i < nelems; i++ ) {
      SimFuncTableElem sfte = (SimFuncTableElem)ftable.get(i);
      out.println("Function," + sfte.name + "," + i + "," +
      /*new Integer(sfte.iacgbb) + ",0,0");*/
      new Integer(markersbaseindex[i+1] - findexbase) +
      ",0,0");
      symbolOut(out, sfte.targetlocalsymbol,sfte.itargetlocalsymbol);
      findexbase = markersbaseindex[i+1];
  }
  out.println("Host,0,0,0,0,0");
  out.println("Global,0,0,0,0,0");
  symbolOut(out, hostglobalsymbol, ihostglobalsymbol);
  for ( int i = 0; i < nelems; i++ ) {
      SimFuncTableElem sfte = (SimFuncTableElem)ftable.get(i);
      out.println("Function," + sfte.name + "," + i + "," +
      "0,0,0");
      symbolOut(out, sfte.hostlocalsymbol,sfte.ihostlocalsymbol);
  }

  /* marker information */
  out.println("Markers," + counterLength + ",0,0,0,0");
  for ( int i = 0; i < counterLength; i++ ) {
      out.println(i + "," + markerslineno[i] + ",0,0,0,0");
  }

  out.flush();
    }

    private static void symbolOut(PrintWriter out, Hashtable symbol,
          int isymbol) {
  Enumeration keys = symbol.keys();
  ImList[] stable = new ImList[isymbol];
  for ( Enumeration e = keys; e.hasMoreElements(); ) {
      String symName = (String)e.nextElement();
      ImList elem = (ImList)symbol.get(symName);
      int index = ((Integer)elem.elem()).intValue();
      stable[index] = ImList.list(symName, elem);
  }

  for ( int i = 0; i < isymbol; i++ ) {
      ImList eelem  = (ImList)stable[i].elem2nd();
      /*
      System.out.println(i + "," + stable[i].elem());
      */
      out.println(i + "," +
      stable[i].elem() + "," +
      eelem.elem2nd() + "," +
      eelem.elem3rd() + "," +
      eelem.elem4th() + "," +
      eelem.elem5th() );
  }
    }


    public void transcode() {
    }

    private void setmarkerlogs(int ibcg, int ibb, int iacg, String marker,
              String lineno,
              BiLink insp,
              SimFuncTableElem sfte,
              Root root, int fi){
  LirFactory lir = sfte.bcghost.newLir;
  int ln = new Integer(lineno).intValue();
  int index = markersbaseindex[fi] + iacg;
  /**/
  if ( root.isOptionSet("simulatemarklog") ) {
      System.out.println("index: " + marker + ":" + new Integer(fi) + ":" +
             new Integer(ibb) + ":" +
             new Integer(markersbaseindex[fi]) + ":" +
             new Integer(iacg) );
  }
  /**/
  markerslineno[index] = ln;
  LirNode s = lir.node
      (Op.SET, counterType, lir.node
       (Op.MEM, counterType, lir.node
        (Op.ADD, typeAddress, lir.node
         (Op.STATIC, typeAddress, counterArray),
         lir.iconst(typeAddress, index * counterSize))),
       lir.node
       (Op.ADD, counterType, lir.node
        (Op.MEM, counterType, lir.node
         (Op.ADD, typeAddress, lir.node
    (Op.STATIC, typeAddress, counterArray),
    lir.iconst(typeAddress, index * counterSize))),
        lir.iconst(counterType, 1)));
  insp.addBefore(s);

  if ( root.traceOK("simulatedump", 1) ) {
      System.out.println("marker(" + new Integer(iacg) + "," +
             marker + "):lineno(" + lineno + "):" +
             (String)((LirNode)insp.elem()).toString());
  }
    }

    /*  sfte.memsbcghost has a memory access list which isn't used  */
    private void setmemlogsC(int ip, LirNode node,
            SimFuncTableElem sfte,
            String targetName,
            Root root, int fi) {
  if ( root.traceOK("simulateignoredump", 1) ) {
      System.out.println(" ignore:" + node.toString());
  }
  /*
  for ( int j = 0; j < sfte.imemsbcghost; j++ ) {
      System.out.println(((ImList)(sfte.memsbcghost.get(j))).toString());
  }
  */
    }


    private void setmemlogsB(BiLink insp, int ib, LirNode node,
            String slvalue,
            SimFuncTableElem sfte,
            String targetName,
            Root root, int fi) {
  BasicBlk elem = (BasicBlk) sfte.bcghostbb.get(ib);
  if ( root.isOptionSet("simulatelog") ) {
      System.out.println("LOG(TARGET):" +
             node.toString());
  }
  setmemlogsA1(insp, node, slvalue, sfte, targetName, root, "target", fi);
    }

    private void setmemlogsA(BiLink insp, LirNode node,
           String slvalue,
           SimFuncTableElem sfte,
           String targetName,
           Root root, int fi) {
  if ( root.isOptionSet("simulatelog") ) {
      System.out.println("LOG(HOST):" +
             node.toString());
  }
  setmemlogsA1(insp, node, slvalue, sfte, targetName, root, "host", fi);
    }
    private void setmemlogsA1(BiLink insp, LirNode node,
            String slvalue,
            SimFuncTableElem sfte,
            String targetName,
             Root root, String hosttarget,
             int fi) {
  LirNode knode = node.kid(0);
  int typeid = node.type;
  switch (knode.opCode) {
  case Op.FRAME:
  case Op.STATIC:
      {
                String rname = ((LirSymRef)knode).symbol.name;
                /*  call _coins_simlog1(name, slvalue) // local variable */
    addlogfunc(0, insp, sfte, fi, rname, hosttarget, typeid, slvalue, (Object)"", (Object)"");
    if ( root.isOptionSet("simulatelog") ) {
        System.out.println("call _coins_simlog1frame_" +
           hosttarget + "(" +
           Type.toString(typeid) + "," +
                                   rname + "," + slvalue + ")");
    }
            }
            break;
        case Op.REG:
            /*   frame or not */
            {
    String rname = ((LirSymRef)knode).symbol.name;
    if ( tp.frameregister(rname, targetName ) ) {
        addlogfunc(1, insp, sfte, fi, rname, hosttarget, typeid, slvalue, (Object)"ADD",(Object)(new Long(0)));
        if ( root.isOptionSet("simulatelog") ) {
      System.out.println("call _coins_simlog2frame_" +
             hosttarget + "(" +
             Type.toString(typeid) + "," +
             "ADD," +
             rname + ",0," + slvalue + ")" );
        }
    } else {
        String calc = knode.toString();
        addlogfunc(2, insp, sfte, fi, rname, hosttarget, typeid, slvalue, (Object)knode, (Object)"");
        if ( root.isOptionSet("simulatelog") ) {
      System.out.println("call _coins_simlog2directaccess_" +
             hosttarget + "(" +
             Type.toString(typeid) + "," +
             calc + "." + slvalue + ")" );
        }
    }
      }
      break;
  case Op.ADD:
  case Op.SUB:
      /* frame or not */
      /* a(r + offset) */
      String opcode;
      if ( knode.opCode == Op.ADD) {
    opcode = "ADD";
      } else {
    opcode = "SUB";
      }
      LirNode rnode = knode.kid(0);
      if ( rnode.opCode != Op.REG ) {
    String calc = knode.toString();
    addlogfunc(3, insp, sfte, fi, "", hosttarget, typeid, slvalue, (Object)knode, (Object)"");
    if ( root.isOptionSet("simulatelog") ) {
        System.out.println("call _coins_simlog2directaccess_" +
               hosttarget + "(" +
               Type.toString(typeid) + "," +
               calc + "." + slvalue + ")" );
    }
      } else {
    LirNode onode = knode.kid(1);
    String rname = ((LirSymRef)rnode).symbol.name;
    if ( tp.frameregister(rname, targetName ) ) {
        if ( onode.opCode == Op.INTCONST ) {
      String offset = new Long(((LirIconst) onode).value).toString();
      addlogfunc(4, insp, sfte, fi, rname, hosttarget, typeid, slvalue, (Object)opcode, (Object)onode);
      if ( root.isOptionSet("simulatelog") ) {
          System.out.println("call _coins_simlog3dframe_" +
                 hosttarget + "(" +
                 Type.toString(typeid) + "," +
                 opcode + "," +
                 rname + "." + offset +
                 "," + slvalue + ")" );
      }
        } else {
      String calc = knode.toString();
      addlogfunc(5, insp, sfte, fi, rname, hosttarget, typeid, slvalue, (Object)knode, (Object)opcode);
      if ( root.isOptionSet("simulatelog") ) {
          System.out.println("call _coins_simlog2directaccess_" +
                 hosttarget + "(" +
                 Type.toString(typeid) + "," +
                 opcode + "," +
                 calc + "." + slvalue + ")" );
      }
        }
    } else {
        String calc = knode.toString();
        addlogfunc(6, insp, sfte, fi, rname, hosttarget, typeid, slvalue, (Object)knode, (Object)opcode);
        if ( root.isOptionSet("simulatelog") ) {
      System.out.println("call _coins_simlog2directaccess_" +
             hosttarget + "(" +
             Type.toString(typeid) + ","+
             opcode + "," +
             calc + "." + slvalue + ")" );
        }
    }
      }
      break;

  default:
      String calc = knode.toString();
      addlogfunc(7, insp, sfte, fi, "", hosttarget, typeid, slvalue, (Object)knode, (Object)"");
      if ( root.isOptionSet("simulatelog") ) {
    System.out.println("call _coins_simlog2directaccess_" +
           hosttarget + "(" +
           Type.toString(typeid) + "," +
           calc + "." + slvalue + ")" );
      }
  }
    }


    private void addlogfunc(int kind, BiLink insp,
          SimFuncTableElem sfte,
          int funcindex, String name,
          String hosttarget,
          int typeid,
          String slvalue,
          Object option, Object option2)
    {
  long ki, ht, fi, ti, ni, sl, gl;
  LirFactory lir = sfte.bcghost.newLir;

  ki = kind;
  fi = funcindex;
  ti = typeid;
  if ( !hosttarget.equals("host") ) {
      ht = 0;
  } else {
      ht = 1;
  }
  if ( slvalue.equals("false") ) {
      sl = 0;
  } else {
      sl = 1;
  }
  int i32 = Type.type(Type.INT,32);

  /*
    ImList symelem = ImList.list
    ( new Integer(sfte.ilocalsymbol),
    storage, stype, segment, linkage );
  */

  if ( hosttarget.equals("host"))  {
      if ( sfte. hostlocalsymbol.get(name) != null ) {
    ImList il = (ImList) sfte.hostlocalsymbol.get(name);
    ni = (long) ( (Integer)il.elem()).intValue();
    gl = 1;
      }  else if  ( hostglobalsymbol.get(name) != null ){
    ImList il = (ImList) hostglobalsymbol.get(name);
    ni = (long) ( (Integer)il.elem()).intValue();
    gl = 0;
      } else {
    ni = -1;
    gl = 0;
      }
  } else {
      if (  sfte.targetlocalsymbol.get(name) != null ) {
    ImList il = (ImList) sfte.targetlocalsymbol.get(name);
    ni = (long) ( (Integer)il.elem()).intValue();
    gl = 1;
      }  else if  ( targetglobalsymbol.get(name) != null ){
    ImList il = (ImList) targetglobalsymbol.get(name);
    ni = (long) ( (Integer)il.elem()).intValue();
    gl = 0;
      } else {
    ni = -1;
    gl = 0;
      }
  }
  /*
  System.out.println(ni + "," + ht +","+ gl + "," + name);
  */
  LirNode[] argv = new LirNode[9];
  argv[0] = lir.node(Op.MEM,i32, lir.node
         (Op.STATIC, i32, memAccessSymbol));
  argv[1] = lir.node(Op.INTCONST, i32, ki);
  argv[2] = lir.node(Op.INTCONST, i32, ht);
  argv[3] = lir.node(Op.INTCONST, i32, ti);
  argv[4] = lir.node(Op.INTCONST, i32, fi);
  argv[5] = lir.node(Op.INTCONST, i32, ni);
  argv[6] = lir.node(Op.INTCONST, i32, sl);
  argv[7] = lir.node(Op.INTCONST, i32, gl);
  switch ((int)ki) {
  case 0:
      {
    argv[8] = lir.node(Op.INTCONST, i32, (long)0);
      }
      break;
  case 1:
      {
    String addsub = (String)option;
    long asi;
    if ( addsub.equals("ADD") ) {
        asi = 0;
    } else {
        asi = 1;
    }
    long offset = ((Long)option2).longValue();

    argv[7] = lir.node(Op.INTCONST, i32, asi);
    argv[8] = lir.node(Op.INTCONST, i32, offset);
      }
      break;
  case 2:
      {
    LirNode knode = (LirNode) option;
    argv[7] = lir.makeCopy(knode);
    argv[8] = lir.node(Op.INTCONST, i32, (long)0);
      }
      break;
  case 3:
      {
    LirNode knode = (LirNode) option;
    argv[7] = lir.makeCopy(knode);
    argv[8] = lir.node(Op.INTCONST, i32, (long)0);
      }
      break;
  case 4:
      {
    String opcode = (String) option;
    long as1;
    if ( opcode.equals("ADD") ) {
        as1 = 0;
    } else {
        as1 = 1;
    }
    LirIconst onode = (LirIconst) option2;
    argv[7] = lir.node(Op.INTCONST, i32, as1);
    argv[8] = lir.makeCopy(onode);
      }
      break;
  case 5:
      {
    String opcode = (String) option;
    long as1;
    if ( opcode.equals("ADD") ) {
        as1 = 0;
    } else {
        as1 = 1;
    }
    LirNode onode = (LirNode) option2;
    argv[7] = lir.node(Op.INTCONST, i32, as1);
    argv[8] = lir.makeCopy(onode);
      }
      break;
  case 6:
      {
    LirNode enode = (LirNode) option;
    argv[7] = lir.makeCopy(enode);
    argv[8] = lir.node(Op.INTCONST, i32, (long)0);
      }
      break;
  case 7:
      {
    LirNode enode = (LirNode) option;
    argv[7] = lir.makeCopy(enode);
    argv[8] = lir.node(Op.INTCONST, i32, (long)0);
      }
      break;
  }
  LirNode retval = null;
  LirNode[] emptyVector = new LirNode[]{};
  LirNode newstatement =
      lir.node
      ( Op.CALL, Type.UNKNOWN,
        lir.node(Op.STATIC, i32, logfuncsymbol),
        lir.node(Op.LIST, Type.UNKNOWN, argv),
        lir.node(Op.LIST, Type.UNKNOWN, emptyVector));

  /*  confirm LirNode is correct
  System.out.println("node =" + newstatement);
  */
  insp.addBefore(newstatement);
    }

    private void setframesize(boolean proepi, BiLink insp,
            SimFuncTableElem sfte, int framesize,
            int simCount ) {
  LirFactory lir = sfte.bcghost.newLir;
  int i32 = Type.type(Type.INT,32);

  LirNode[] argv = new LirNode[4];
  long proorepi;
  if ( proepi) {
      proorepi = 0;
  } else {
      proorepi = 1;
  }
  argv[0] = lir.node(Op.INTCONST, i32, proorepi);
  argv[1] = lir.node(Op.INTCONST, i32, (long)framesize);
  long mainornot;
  if ( sfte.name.equals("main") ) {
      mainornot = 1;
  } else {
      mainornot = 0;
  }
  argv[2] = lir.node(Op.INTCONST, i32, mainornot );
  argv[3] = lir.node(Op.INTCONST, i32, (long) simCount);
  LirNode retval = null;
  LirNode[] emptyVector = new LirNode[]{};
  LirNode newstatement =
      lir.node
      ( Op.CALL, Type.UNKNOWN,
        lir.node(Op.STATIC, i32, fsfuncsymbol),
        lir.node(Op.LIST, Type.UNKNOWN, argv),
        lir.node(Op.LIST, Type.UNKNOWN, emptyVector));
  if ( proepi ) {
      insp.addAfter(newstatement);
  } else {
      insp.addBefore(newstatement);
  }
    }
}
/*
how to make code:
 LirFactory lir;
 (CALL
     (STATIC type name )
     ( ARG0 ARG1 ARG2 ARG3 ))

 is as follows:
  LirNode[] lirarray = new LirNode[narity];
  lirarray[0] = lir.node(arg0.op, arg0.type, arg0.operand ....)


  lir.node
 ( Op.CALL   lir.node
     (Op.STATIC, i32, symbol)
     lirarray)

function
(name , Storage.STATIC, Type.UNKNOWN, 4, ".text", "XREF", null);


*/
