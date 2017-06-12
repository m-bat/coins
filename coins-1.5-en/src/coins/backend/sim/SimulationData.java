/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sim;

import coins.backend.Module;

public class SimulationData {
    private Module acgModule;
    private Module bcgModule;
    private Module bcghostModule;

    public void setACGModule(Module unit)
    { acgModule = unit; }
    public void setBCGModule(Module unit)
    { bcgModule = unit; }
    public void setBCGhostModule(Module unit)
    { bcghostModule = unit; }

    public Module ACGModule()
    { return acgModule; }
    public Module BCGModule()
    { return bcgModule; }
    public Module BCGhostModule()
    { return bcghostModule; }

    public SimFuncTable SetupSimFuncTable(int simCount, int simMem)
    {
	return new SimFuncTable(acgModule, bcgModule, bcghostModule,
				simCount, simMem );
    }
}
