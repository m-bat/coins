/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;


/** BitVectorIteratorImpl class:  (##6)
 *
 **/
public class BitVectorIteratorImpl implements BitVectorIterator {
    private int fLongWordLength;
    private int fBitCount;
    private int fShiftMax = 63; //##fnami
    private long[] fVectorWord;
	private final BitVector fBitVector;
    private int fBitPosition;

    public BitVectorIteratorImpl(BitVector pBitVector) //##8
     {
        fLongWordLength = pBitVector.getWordLength(); //##8
        fBitCount = pBitVector.getBitLength() + 1; //##8
        fVectorWord = pBitVector.getVectorWord(); //##8
        fBitPosition = 0;
		fBitVector = pBitVector;
    }

    public boolean hasNext() {
        if (fBitPosition < (fBitCount - 1)) {
            return true;
        } else {
            return false;
        }
    }
     // hasNext

    public int next() {
        return ++fBitPosition;
    }

    public int nextIndex() {
        int lInx;
        int lValue = 0;
        int lWordPosition;

        while (fBitPosition < (fBitCount - 1)) { // REFINE algorithm.
            fBitPosition++;
            lWordPosition = fBitPosition / 64; //##fnami
            lInx = fBitPosition - (lWordPosition * 64); //##fnami
            lValue = (int) ((fVectorWord[lWordPosition] >>> (fShiftMax - lInx)) &
                1);

            if (lValue > 0) {
                break;
            }
        }

        ;

        if (lValue == 1) {
            return fBitPosition;
        } else {
            return 0;
        }
    }
     // nextIndex

    public int currentIndex() {
        return fBitPosition;
    }
	

	public void resetBit()
	{
		fBitVector.resetBit(fBitPosition);
	}

	public void setBit()
	{
		fBitVector.setBit(fBitPosition);
	}
	
}
 // BitVectorIteratorImpl class
