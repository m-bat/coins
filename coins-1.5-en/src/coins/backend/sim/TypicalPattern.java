/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.sim;

import coins.backend.Op;
import coins.backend.Root;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;


public class TypicalPattern {
    public boolean localAnalyze(String machine, LirNode node, Root root) {
	return false;
    }

    public final boolean analyze(LirNode node, String machine, Root root) {
	boolean res;
	res = localAnalyze(machine, node, root);

	if ( res ) return true;

	LirNode knode = node.kid(0);
        switch (knode.opCode) {
        case Op.FRAME:
        case Op.STATIC:
            return true;
        case Op.REG:
            /*   frame or not */
            {
                String rname = ((LirSymRef)knode).symbol.name;
                if ( frameregister(rname, machine) ) {
                    return true;
                } else {
                    return false;
                }
            }
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
	    if ( rnode.opCode == Op.REG ) {
		/* case of LirSymRef 
		  STATIC/FRAME/REG */
		String rname = ((LirSymRef)rnode).symbol.name;
		if ( frameregister(rname, machine) ) {
		    LirNode onode = knode.kid(1);
		    if ( onode.opCode == Op.INTCONST ) {
			String offset = (new Long(((LirIconst) onode).value)).toString();
			return true;
		    } else {
			return false;
		    }
		} else {
		    return false;
		}
            } else {
		return false;
	    }
        default:
            return false;
        }
    }

    public boolean isFrameRegister(String rname, String machine) {
	return false;
    }

    public final boolean frameregister(String rname, String machine) {
	String fp;
	String newname;
	if ( isFrameRegister(rname,machine) ) return true;

        if ( machine.equals("x86")) {
	    fp = "%ebp";
	} else if ( machine.equals("arm")) {
	    fp = "%fp";
	} else {
	    return false;
	}
	newname = rname.substring(0,fp.length());
	if ( newname.equals(fp) ) {
	    return true;
	} else {
	    return false;
	}
    }

}
