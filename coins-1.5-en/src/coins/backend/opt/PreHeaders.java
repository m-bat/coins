/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.opt;

import coins.backend.Data;
import coins.backend.Function;
import coins.backend.LocalTransformer;
import coins.backend.ana.DFST;
import coins.backend.ana.LoopAnalysis;
import coins.backend.cfg.BasicBlk;
import coins.backend.util.BiLink;
import coins.backend.util.ImList;


/** Insert pre-headers above loops. */
public class PreHeaders {

  /** Trigger class. **/
  private static class Trigger implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      (new PreHeaders()).doIt(func);
      return true;
    }

    public boolean doIt(Data data, ImList args) { return true; }


    public String name() { return "PreHeaders"; }

    public String subject() { return "Pre-header Insertion"; }
  }

  /** Trigger class singleton. **/
  public static final Trigger trig = new Trigger();

  public PreHeaders() {}

  /** Insert pre-headers above reducible loops. */
  public void doIt(Function f) {
    DFST dfst = (DFST)f.require(DFST.analyzer);
    LoopAnalysis loop = (LoopAnalysis)f.require(LoopAnalysis.analyzer);

    for (BiLink p = f.flowGraph().basicBlkList.first();
         !p.atEnd(); p = p.next()) {
      BasicBlk entry = (BasicBlk)p.elem();

      // Is block 'entry' is the entry of a reducible loop?
      if (loop.isLoop[entry.id] && !loop.multiEntry[entry.id]) {
        BasicBlk preHeader = f.flowGraph().insertNewBlkBefore(entry);

        // Rewrite edges go to entry only those not come from inside of the loop
        for (BiLink q = entry.predList().first(); !q.atEnd(); q = q.next()) {
          BasicBlk pred = (BasicBlk)q.elem();
          if (pred != preHeader && !dfst.isAncestorOf(entry, pred))
            pred.replaceSucc(entry, preHeader);
        }
      }
    }
  }

}
