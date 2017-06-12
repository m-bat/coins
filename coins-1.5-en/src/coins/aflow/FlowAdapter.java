/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow;

import coins.FlowRoot;
import coins.HirRoot;
import coins.IoRoot;
// import coins.LirRoot; // 2004.05.31 S.Noishi
import coins.SymRoot;
import coins.aflow.util.AnalAdapter;
import coins.aflow.util.FlowError;
import coins.aflow.util.SelfCollectingResults;


/**
 * Adapter class for Analyzer interface.
 */
abstract public class FlowAdapter extends AnalAdapter {
    public final FlowRoot flowRoot;
    public final HirRoot hirRoot;
    //public final LirRoot lirRoot; 2004.05.31 S.Noishi
    public final IoRoot ioRoot;
    public final SymRoot symRoot;
    public final Flow flow;
    protected FlowResults fResults;

    public FlowAdapter(FlowResults pResults) {
        fResults = pResults;
        flowRoot = fResults.flowRoot;
        hirRoot = flowRoot.hirRoot;
        //lirRoot = flowRoot.lirRoot; // 2004.06.01 S.Noishi
        ioRoot = flowRoot.ioRoot;
        symRoot = flowRoot.symRoot;
        flow = flowRoot.aflow;
    }

    public void find(SelfCollectingResults pResults) {
        throw new FlowError(this.getClass().getName() +
            " does not correctly override this method.");
    }

    public void find(Object pObj, SelfCollectingResults pResults) {
        throw new FlowError(this.getClass().getName() +
            " does not correctly override this method.");
    }

    /*        public void find(AnalOptions pOpts, SelfCollectingResults pResults)
            {
                    throw new FlowError(this.getClass().getName() + " does not correctly override this method.");
            }
    */
    /*        public void find(Object pObj, AnalOptions pOpts, SelfCollectingResults pResults)
            {
                    throw new FlowError(this.getClass().getName() + " does not correctly override this method.");
            }
    */
    public void find(Object pObj, Object pObj0, SelfCollectingResults pResults) {
        throw new FlowError(this.getClass().getName() +
            " does not correctly override this method.");
    }

    public void find() {
        throw new FlowError(this.getClass().getName() +
            " does not correctly override this method.");
    }

    public void find(Object pObj) {
        throw new FlowError(this.getClass().getName() +
            " does not correctly override this method.");
    }

    /*    public void find(Object pObj, AnalOptions pOpts)
        {
                throw new FlowError(this.getClass().getName() + " does not correctly override this method.");
        }
      */
    /*public void find(AnalOptions pOpts)
    {
            throw new FlowError(this.getClass().getName() + " does not correctly override this method.");
    } */
    /*
      public void find(Object pObj, Object pObj0, AnalOptions pOpts)
      {
              throw new FlowError(this.getClass().getName() + " does not correctly override this method.");
      }
      */
    public void find(Object pObj, Object pObj0) {
        throw new FlowError(this.getClass().getName() +
            " does not correctly override this method.");
    }
}
