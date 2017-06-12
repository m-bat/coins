/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sim;

import coins.backend.Module;

public class Simulator {

    Module acg;
    Module bcg;
    Module bcghost;

    SimFuncTable ft;
    int nft;

    /**instantiatoin**/
    Simulator(Module acgModule, Module bcgModule, Module bcghostModule,
	      int simCount, int simMem, TypicalPattern tp ) {
	acg = acgModule;
	bcg = bcgModule;
	bcghost = bcghostModule;

	ft = new SimFuncTable(acgModule, bcgModule, bcghostModule, simCount, simMem);
	nft = ft.nelems;

	/*   call other inititate procedures */

	/*
	for (int i = 0; i < nft; i++) {
	    Function func = ft.table[i].func;
	}
	*/
    }
}


