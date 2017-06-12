/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.util;


/** BitVectorIteratorImpl class:  (##6)
 * <p>This class offers methods for iterating over a <code>BitVector</code> instance. As <code>BitVector</code>s in general cannot be efficiently scanned, this class is just a compromise for when there is really a need to scan over a <code>BitVector</code>. If iteration performance is important, other data structures than a <code>BitVector</code> should be considered. </p>
 * <p>The iteration is implemented by maintaining and advancing the <em>cursor</em> position, which is the bit position of the underlying <code>BitVector</code> this <code>BitVectorIterator</code> currently has hold on.</p>
 * <p>The methods in this class do not necessarily throw appropriate exceptions; an instance of this class may silently enter an invalid state, so care should be taken. 
 * <p>Some of the methods' names (<code>hasNext</code> and <code>next</code>) are carried over from <code>java.util.Iterator</code> interface, but their behaviors may be different from what may be expected from their names.
 */
public class BitVectorIterator
{
	/**
	 * The length of the array that holds actual data for the BitVector instance this BitVectorIterator instance scans.
	 */
	private int fLongWordLength;
	
	private int fBitCount;
	private int fShiftMax = 63; //##fnami
	private long[] fVectorWord;
	private final BitVector fBitVector;
	private int fBitPosition;
	
	/**
	 * Creates a <code>BitVectorIterator</code> instance that scans the given argument. The cursor position (returned by currentIndex) is set to -1.
	 *
	 * @param pBitVector the <code>BitVector</code> this <code>BitVectorIterator</code> scans.
	 */
	public BitVectorIterator(BitVector pBitVector) //##8
	{
		fLongWordLength = pBitVector.getWordLength(); //##8
		fBitCount = pBitVector.getBitLength(); //##8
		fVectorWord = pBitVector.getVectorWord(); //##8
		fBitPosition =  -1;
		fBitVector = pBitVector;
	}
	
	/**
	 * Returns <code>true</code> if the cursor is not at or beyond the end of the underlying <code>BitVector</code>. (In other words, returns true if next would return a number less than the length of the underlying <code>BitVector</code>.)
	 *
	 * @return true if iteration has more elements.
	 */
	public boolean hasNext()
	{
		return (fBitPosition < fBitCount - 1);
	}
	// hasNext

	/**
	 * Returns the next bit position and advances the cursor by one. The value returned by this method may not be within the length of the underlying BitVector.
	 *
	 * @return the next bit position.
	 */
	public int next()
	{
		return ++fBitPosition;
	}
	
	/**
	 * Returns the next bit position that is set and advances the cursor up to the returned position. If there is no remaining bit that is set, returns -1, and the cursor will be at the end of the underlying BitVector.
	 *
	 * @return the next bit position that is set, or -1 if there is no such bit.
	 */
	public int nextIndex()
	{
		int lInx;
		int lValue = 0;
		int lWordPosition;
		
		while (fBitPosition < fBitCount - 1)
		{ // REFINE algorithm.
			fBitPosition++;
			lWordPosition = fBitPosition / 64; //##fnami
			lInx = fBitPosition - (lWordPosition * 64); //##fnami
			lValue = (int) ((fVectorWord[lWordPosition] >>> (fShiftMax - lInx)) &
			1);
			if (lValue > 0)
			{
				break;
			}
		}
		
		;
		
		if (lValue == 1)
		{
			return fBitPosition;
		} else
		{
			return -1;
		}
	}
	// nextIndex
	
	/**
	 * Returns the current cursor position.
	 *
	 * @return the current cursor position.
	 */
	public int currentIndex()
	{
		return fBitPosition;
	}
	
	/**
	 * Resets the bit at the current cursor position.
	 */
	public void resetBit()
	{
		fBitVector.resetBit(fBitPosition);
	}
	
	/**
	 * Sets the bit at the current cursor position.
	 */
	public void setBit()
	{
		fBitVector.setBit(fBitPosition);
	}
	
}
// BitVectorIteratorImpl class
