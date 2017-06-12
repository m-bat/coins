/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;


/** BitVectorIterator interface:  (##6)
 *
 **/
public interface BitVectorIterator {
    /** hasNext:
     * Sees whether the <code>BitVector</code> associated with this <code>BitVectorIterator</code> has more elements. This method does not check the contents of the remaining bits.
     *  @return true if the vector has next element,
     *          false otherwise.
     **/
    boolean hasNext();

    /**
     * Returns the next bit position of this <code>BitVectorIterator</code>.
     */
    int next();

    /** nextIndex:
     *  Returns the next index of bit position that has value 1.
     *  If the there is no non-zero bit remaining, then 0 is returned. Therefore, having <code>hasNext</code> returned <code>true</code> does not guarantee this method returns meaningful (nonzero) value.
     **/
    int nextIndex();

    /**
     * Returns the current index of bit position. This is what was last returned by <code>next()</code> or <code>nextIndex()</code>.
     */
    int currentIndex();
	
	/**
	 * Sets the bit for the position returned by the last call to next() or nextIndex(). The behavior is undefined if there was no such call or the call returned 0.
	 */
	void setBit();
	
	/**
	 * Resets the bit for the position returned by the last call to next() or nextIndex(). The behavior is undefined if there was no such call or the call returned 0.
	 */
	void resetBit();
}
 // BitVectorIterator interface
