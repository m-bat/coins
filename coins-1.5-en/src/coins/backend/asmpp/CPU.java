/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.asmpp;

//import java.io.*;
//import java.util.*;


/* CPU Architecture */

public abstract class CPU {

	public int[] bccRange;
	public int[] braRange;
	public int[] literalRange;
	public String[] bccMnemo;
	public String braMnemo;
	public int braLength;
	public int codeAlign;

	public int codeLength(String inst) {
		return 1;
	}

	public String generateBcc(String mnemo, String label) {
		return "\t" + mnemo + "\t" + label;
	}

	public String generateBra(String label) {
		return "\t" + braMnemo + "\t" + label;
	}

	public String[] rewriteToLongBranch(String label) {
		return new String[] { "\t" + braMnemo + "\t" + label };
	}

	public boolean isBcc(String mnemo) {
		for (int i = 0; i < bccMnemo.length; i++) {
			if (mnemo.equalsIgnoreCase(bccMnemo[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean isBra(String mnemo) {
		return mnemo.equalsIgnoreCase(braMnemo);
	}

	public boolean inBccRange(int n) {
		return (bccRange[0] <= n && n <= bccRange[1]);
	}

	public boolean inBraRange(int n) {
		return (braRange[0] <= n && n <= braRange[1]);
	}

	public boolean inLiteralRange(int n) {
		return (literalRange[0] <= n && n <= literalRange[1]);
	}

	public String getRevMnemo(String mnemo) {
		for (int i = 0; i < bccMnemo.length; i++) {
			if (mnemo.equalsIgnoreCase(bccMnemo[i])) {
				i ^= 1;
				return bccMnemo[i];
			}
		}
		return null;
	}

	public abstract String toString();
}
