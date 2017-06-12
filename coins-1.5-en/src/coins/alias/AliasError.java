/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * AliasError.java
 *
 * Created on July 15, 2003, 5:16 PM
 */

package coins.alias;

/**
 * Class for errors thrown by alias analysis 
 * @author  hasegawa
 */
public class AliasError extends Error
{
	/** Creates a new instance of AliasError */
	public AliasError()
	{
		super();
	}
	
	/**
	 * Creates a new instance of AliasError with an informational <code>String</code>.
	 */
	public AliasError(String pString)
	{
		super(pString);
	}
}

