/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

/*
 * Created on 2005/11/13
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import coins.backend.sym.Symbol;

/**
 * @author K_Yoshiba
 */
public class ECR extends DisjointSet {

	private Symbol symbol;

	private AliasType type;

	private HashSet pending;

	public ECR() {
		this(null, new AlphaType());
	}

	public ECR(Symbol symbol, AliasType type) {
		super();
		this.symbol = symbol;
		this.type = type;
		this.pending = null;
	}

	public AliasType getType() {
		ECR rep = (ECR) find();
		return rep.type;
	}

	public Symbol getSymbol() {
		return symbol;
	}
	
	public ECR getECR() {
		return (ECR) find();
	}

	public void addPending(ECR ecr) {
		ECR rep = (ECR) find();
		if (rep.pending == null) {
			rep.pending = new HashSet();
		}
		rep.pending.add(ecr);
	}

	public void unionPending(ECR ecr1, ECR ecr2) {
		if ((ecr1.pending != null) && (ecr2.pending != null)) {
			HashSet h = (HashSet) ecr1.pending.clone();
			Iterator iter = ecr2.pending.iterator();
			while (iter.hasNext()) {
				h.add(iter.next());
			}
			pending = h;
		} else if (ecr1.pending != null) {
			pending = (HashSet) ecr1.pending.clone();
		} else if (ecr2.pending != null) {
			pending = (HashSet) ecr2.pending.clone();
		}

		if (this != ecr1)
			ecr1.pending = null;
		if (this != ecr2)
			ecr2.pending = null;
	}

	public void join(ECR ecr) {
		AliasType t1 = getType();
		AliasType t2 = ecr.getType();
		ECR e = (ECR) union(ecr);

		if (t1.isBottom()) {
			e.type = t2;
			if (t2.isBottom()) {
				e.unionPending(this, ecr);
			} else {
				if (pending != null) {
					Iterator iter = pending.iterator();
					while (iter.hasNext()) {
						e.join((ECR) iter.next());
					}
					pending = null;
				}
			}
		} else {
			e.type = t1;
			if (t2.isBottom()) {
				if (ecr.pending != null) {
					Iterator iter = ecr.pending.iterator();
					while (iter.hasNext()) {
						e.join((ECR) iter.next());
					}
					ecr.pending = null;
				}
			} else {
				t1.unify(t2);
			}
		}
	}

	public void cjoin(ECR ecr) {
		if (ecr.getType().isBottom()) {
			ecr.addPending(this);
		} else {
			join(ecr);
		}
	}

	public void setType(AliasType type) {
		ECR rep = (ECR) find();
		rep.type = type;
		if (pending != null) {
			Iterator iter = pending.iterator();
			while (iter.hasNext()) {
				join((ECR) iter.next());
			}
			pending = null;
		}
	}
	
	public void printPointsToSet() {
		System.out.print(symbol.name + " -> ");
		ECR tau = ((AlphaType) getType()).getTau();
		int size = tau.size();
		for(int i = 0; i < size; i++) {
			ECR e = (ECR) tau.getElements().get(i);
			if(!e.type.isBottom() && (e.symbol != null)) {
				System.out.print(e.symbol.name + "  ");
			}
		}
		System.out.println();
	}
	
	public Vector getPointsToSet(){
		Vector vec = new Vector();
		ECR tau = ((AlphaType) getType()).getTau();
		int size = tau.size();
		for(int i = 0; i < size; i++) {
			ECR e = (ECR) tau.getElements().get(i);
			if(!e.type.isBottom() && (e.symbol != null)) {
				vec.add(e.symbol);
			}
		}
		return vec;
	}
}
