/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow; //##60

/** BitVectorImpl class
 *
 **/
public class BitVectorImpl
  implements BitVector, Cloneable //##8
{
  protected int fLongWordLength = 1;
  protected int fBitLength = 64; //##fnami
  protected int fShiftMax = 63; //##fnami
  protected long[] fVectorWord;

  protected BitVectorImpl() // For subclass
  {
  }

  protected BitVectorImpl(int pLongWordCount)
  {
    fLongWordLength = pLongWordCount;
    fBitLength = (fLongWordLength * 64) - 1;
    //##62 fVectorWord = new long[pLongWordCount];
    fVectorWord = new long[pLongWordCount+1]; //##REFINE //##62
  }

  public void setBit(int pInx)
  {
    int lWord;
    int lInx;
    lWord = pInx / 64; //##fnami
    lInx = pInx - (lWord * 64); //##fnami
    fVectorWord[lWord] = fVectorWord[lWord] | (1L << (fShiftMax - lInx));
  }

  // setBit

  public void resetBit(int pInx)
  {
    int lWord;
    int lInx;
    long lMask;
    lWord = pInx / 64; //##fnami
    lInx = pInx - (lWord * 64); //##fnami
    lMask = ~(1L << (fShiftMax - lInx)); //##fnami
    fVectorWord[lWord] = fVectorWord[lWord] & lMask;
  }

  // resetBit

  public int getBit(int pInx)
  {
    int lWord;
    int lInx;
    int lBit;
    lWord = pInx / 64; //##fnami
    lInx = pInx - (lWord * 64); //##fnami
    lBit = (int)((fVectorWord[lWord] >>> (fShiftMax - lInx)) & 1L);

    return lBit;
  }

  // getBit

  public boolean isSet(int pInx)
  {
    return getBit(pInx) == 1;
  }

  public boolean isZero()
  {
    for (int i = 0; i < fLongWordLength; i++)
      if (fVectorWord[i] != 0) {
        return false;
      }

    return true;
  }

  // isZero

  public int getBitLength()
  {
    return fBitLength;
  }

  public int getWordLength()
  {
    return fLongWordLength;
  }

  public long[] getVectorWord()
  {
    return fVectorWord;
  }

  public BitVectorIterator bitVectorIterator(SubpFlow pSubpFlow)
  {
    return new BitVectorIteratorImpl(pSubpFlow, this);
  }

  public void vectorAnd(BitVector pOperand2, BitVector pResult)
  {
    if (pOperand2 == null) {
      return; //## pResult.vectorReset(); ?
    }

    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)pResult).fVectorWord[i] = ((BitVectorImpl)this).
        fVectorWord[i] &
        ((BitVectorImpl)pOperand2).fVectorWord[i];
  }

  // vectorAnd

  public void vectorOr(BitVector pOperand2, BitVector pResult)
  {
    if (pOperand2 == null) {
      return;
    }

    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)pResult).fVectorWord[i] = ((BitVectorImpl)this).
        fVectorWord[i] |
        ((BitVectorImpl)pOperand2).fVectorWord[i];
  }

  // vectorOr

  public void vectorXor(BitVector pOperand2, BitVector pResult)
  {
    if (pOperand2 == null) {
      return;
    }

    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)pResult).fVectorWord[i] = ((BitVectorImpl)this).
        fVectorWord[i] ^
        ((BitVectorImpl)pOperand2).fVectorWord[i];
  }

  // vectorXor

  public void vectorNot(BitVector pResult)
  {
    if (pResult == null) {
      return; //##12 ?
    }

    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)pResult).fVectorWord[i] = ~((BitVectorImpl)this).
        fVectorWord[i];
  }

  // vectorNot

  public void vectorSub(BitVector pOperand2, BitVector pResult)
  {
    if (pOperand2 == null) {
      return;
    }

    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)pResult).fVectorWord[i] = ((BitVectorImpl)this).
        fVectorWord[i] &
        (~((BitVectorImpl)pOperand2).fVectorWord[i]);
  }

  // vectorSub

  public void vectorCopy(BitVector pResult)
  {
    if (pResult == null) {
      return; //##12 ?
    }

    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)pResult).fVectorWord[i] = ((BitVectorImpl)this).
        fVectorWord[i];
  }

  // vectorCopy

  public boolean vectorEqual(BitVector pOperand2)
  {
    if (pOperand2 == null) {
      return false;
    }

    boolean lResult = false;
    int i = 0;

    do {
      lResult = (((BitVectorImpl)this).fVectorWord[i] !=
                 ((BitVectorImpl)pOperand2).fVectorWord[i]);
      i++;
    }
    while ((i < fLongWordLength) && (lResult == false));

    return (!lResult);
  }

  // vectorEqual

  public boolean equals(Object pObj)
  {
    if (pObj instanceof BitVector)
      return vectorEqual((BitVector)pObj);
    return false;
  }

  public int hashCode()
  {
    int code = 0;
    for (int i = 0; i < fVectorWord.length; i++)
      code += fVectorWord[i];
    return code;
  }

  public void vectorReset()
  {
    for (int i = 0; i < fLongWordLength; i++)
      ((BitVectorImpl)this).fVectorWord[i] = 0;
  }

  // vectorReset

  public Object clone() throws CloneNotSupportedException
  {
    try {
      BitVector lBitVect = (BitVector)super.clone();
      ((BitVectorImpl)lBitVect).fVectorWord = (long[])fVectorWord.clone();

      return lBitVect;
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError(e.toString());
    }
  }

  public String toString()
  {
    StringBuffer lBuffer = new StringBuffer(" ");
    int i;

    for (i = 1; i <= fBitLength; i++) {
      if (getBit(i) == 1) {
        lBuffer.append(" " + i);
      }
    }

    return lBuffer.toString().intern();
  }

  public String toStringDescriptive()
  {
    return toString();
  }
}
