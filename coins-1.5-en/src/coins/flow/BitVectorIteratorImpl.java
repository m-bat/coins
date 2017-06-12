/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow; //##60

/** BitVectorIteratorImpl class  (##6)
 *
 **/
public class BitVectorIteratorImpl
  implements BitVectorIterator
{
  protected int fLongWordLength;
  protected int fBitCount;
  protected int fShiftMax = 63; //##fnami
  protected long[] fVectorWord;
  protected final BitVector fBitVector;
  //##60 private int fBitPosition;
  protected int fBitPosition; //##60
  public final SubpFlow fSubpFlow; //##60

  public BitVectorIteratorImpl(SubpFlow pSubpFlow, BitVector pBitVector)
  {
    fSubpFlow = pSubpFlow; //##60
    fLongWordLength = pBitVector.getWordLength();
    fBitCount = pBitVector.getBitLength() + 1;
    fVectorWord = pBitVector.getVectorWord();
    fBitPosition = 0;
    fBitVector = pBitVector;
  }

  public boolean hasNext()
  {
    if (fBitPosition < (fBitCount - 1)) {
      return true;
    }
    else {
      return false;
    }
  }

  // hasNext

  public int next()
  {
    return++fBitPosition;
  }

  public int nextIndex()
  {
    int lInx;
    int lValue = 0;
    int lWordPosition;

    while (fBitPosition < (fBitCount - 1)) { // REFINE algorithm.
      fBitPosition++;
      lWordPosition = fBitPosition / 64; //##fnami
      lInx = fBitPosition - (lWordPosition * 64); //##fnami
      lValue = (int)((fVectorWord[lWordPosition] >>> (fShiftMax - lInx)) &
        1);

      if (lValue > 0) {
        break;
      }
    }
    ;

    if (lValue == 1) {
      return fBitPosition;
    }
    else {
      return 0;
    }
  }

  // nextIndex

  public int currentIndex()
  {
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
