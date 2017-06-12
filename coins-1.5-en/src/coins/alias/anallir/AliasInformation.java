/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

import java.util.HashMap;
import java.util.Vector;

import coins.backend.sym.Symbol;

public class AliasInformation {
	private HashMap info;

	public AliasInformation() {
		info = new HashMap();
	}
	
	public void add(Symbol sym, Vector pointsToSet){
		// Put the name of sym to pointsToSet because the local
		// variable sym itself will be changed by IntroVirReg.
		info.put(sym.name, pointsToSet);
		//info.put(sym, pointsToSet);
	}
	
	public Vector getPointsToSet(Symbol sym){
	
		return (Vector) info.get(sym.name);
		//return (Vector) info.get(sym);
	}
	// Use this as a local variable. If sym.name is not found,
	// erase '%' and retry.
	public Vector getPointsToSetLocal(Symbol sym){
		String name = (sym.name).intern();
		Vector v = (Vector) info.get(name);
		if(v == null && name.endsWith("%")){
			// Erase the character %.
			name = name.substring(0,name.length() - 1);
			v = (Vector) info.get(name);
		}
		return v;
		//return (Vector) info.get(sym);
	}
}
