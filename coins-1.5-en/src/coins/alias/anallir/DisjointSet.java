/* ---------------------------------------------------------------------
%   Copyright (C) 2012 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.anallir;

/*
 * Created on 2005/11/13
 */

import java.util.Vector;

/**
 * @author K_Yoshiba
 */
public class DisjointSet {

	private DisjointSet parent;

	private int rank;

	private Vector elements;

	public DisjointSet() {
		parent = this;
		rank = 0;
		elements = new Vector();
		elements.add(this);
	}

	public DisjointSet find() {
		if (parent != this) {
			parent = parent.find();
		}
		return parent;
	}
	
	public Vector getElements() {
		return elements;
	}

	public int size() {
		DisjointSet rep = find();
		return rep.elements.size();
	}

	public boolean equivalent(DisjointSet set) {
		if (this == set) {
			return true;
		}
		if (this.find() == set.find()) {
			return true;
		}
		return false;
	}

	private final void mergeElements(DisjointSet set) {
		if (this == set) {
			return;
		}

		int len = set.elements.size();
		for (int i = 0; i < len; i++) {
			elements.add(set.elements.get(i));
		}
	}

	public DisjointSet union(DisjointSet set) {
		DisjointSet x = find();
		DisjointSet y = set.find();

		if (x == y) {
			return x;
		}

		if (x.rank > y.rank) {
			//System.out.println(this + " <- " + set);
			y.parent = x;
			x.mergeElements(y);
			return x;
		} else {
			//System.out.println(this + " -> " + set);
			x.parent = y;
			if (x.rank == y.rank) {
				y.rank++;
			}
			y.mergeElements(x);
			return y;
		}
	}

	public void printElements() {
		int len = size();
		for (int i = 0; i < len; i++) {
			System.out.print(elements.get(i) + "  ");
		}
		System.out.println();
	}

	public static void main(String args[]) {
		DisjointSet s1 = new DisjointSet();
		DisjointSet s2 = new DisjointSet();
		DisjointSet s3 = new DisjointSet();
		DisjointSet s4 = new DisjointSet();
		DisjointSet s5 = new DisjointSet();
		s1.printElements();
		s2.printElements();
		s3.printElements();
		s4.printElements();
		s5.printElements();

		s1 = s1.union(s2);
		s3 = s3.union(s4);
		s1.printElements();
		s3.printElements();

		s3 = s3.union(s5);
		s3.printElements();

		s1 = s1.union(s3);
		s1.printElements();
	}
}
