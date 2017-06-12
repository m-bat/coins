/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * Factory.java
 *
 * Created on April 10, 2003, 2:20 PM
 */

package coins.aflow.util;

/**
 *
 * @author  hasegawa
 */
public class Factory
{
	
	/** Creates a new instance of Factory */
	public Factory()
	{
	}
	
	public static BitVector bitVector()
	{
		return new BitVectorImpl();
	}
	

}
