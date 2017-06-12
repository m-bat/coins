/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.lparallel;
import java.util.LinkedList;
////////////////////////////////////////////////////////////////////////////////////////////
//
//
// [parallel loop] LoopParallel
//
//
////////////////////////////////////////////////////////////////////////////////////////////
public interface LoopParallel  {
	public void LoopAnal() 	;
	public void LoopAnalLoopList(LinkedList pList) 	;
	public void SetOpenMPInfo() 	;
}
