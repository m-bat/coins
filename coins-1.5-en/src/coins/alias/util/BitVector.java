/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.alias.util;


/** BitVector class:
 * Unlike <code>java.util.BitSet</code> class, this class offers
 * methods for non-destructive bit operations, where the result
 * of the operation will be stored in the last argument of
 * such methods.
 **/
public class BitVector
{
  /**
   * Array of <code>long</code>s that internally holds data.
   */
  protected final long[] fVectorWord;

  /**
   * Length of the <code>long</code> array that internally holds data.
   */
  protected final int fLongWordLength; // = 1;

  /**
   * Length of this <code>BitVector</code>.
   */
  protected final int fBitLength; //  = 64; //##fnami

  /**
   * Maximum possible shift of bits within a <code>long</code> word, or 63.
   */
  protected static final int fShiftMax = 63; //##fnami


  /**
   * The number of bits that are not used.
   */
  private final int fRemainingBits;

  /**
   * Creates an instance of <code>BitVector</code> with the specified length.
   *
   * @param pBitLength the length of the <code>BitVector</code>
   */
  public BitVector(int pBitLength)
  {
    fBitLength = pBitLength;
    fLongWordLength = (fBitLength + 63) / 64;
    fRemainingBits = 64 * fLongWordLength - fBitLength;
    fVectorWord = new long[fLongWordLength];
  }

  /**
   * Sets bit at the specified position.
   *
   * @param pInx the index (position) of the bit to be set.
   */
  public void setBit(int pInx)
  {
    int lWord;
    int lInx;
    lWord = pInx / 64; //##fnami
    lInx = pInx - (lWord * 64); //##fnami
    fVectorWord[lWord] = fVectorWord[lWord] | (1L << (fShiftMax - lInx));
  }
  // setBit

  /**
   * Resets bit at the specified position.
   *
   * @param pInx the index (position) of the bit to be reset.
   */
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

  /**
   * Returns bit state at the specified position.
   *
   * @param pInx the index (position) of the bit to get.
   * @return 1 if the specified bit is set, 0 otherwise.
   */
  public int getBit(int pInx)
  {
    int lWord;
    int lInx;
    int lBit;
    lWord = pInx / 64; //##fnami
    lInx = pInx - (lWord * 64); //##fnami
    lBit = (int) ((fVectorWord[lWord] >>> (fShiftMax - lInx)) & 1L);

    return lBit;
  }
  // getBit

  /**
   * Queries if the specified bit is set.
   *
   * @param pInx the index (position) of the bit to query.
   * @return true if the specified bit is set.
   */
  public boolean isSet(int pInx)
  {
    return getBit(pInx) == 1;
  }

  /**
   * Queries if all the bits of this <code>BitVector</code>'s is unset.
   *
   * @return true if all the bits are unset.
   */
  public boolean isZero()
  {
    for (int i = 0; i < fLongWordLength - 1; i++)
      if (fVectorWord[i] != 0)
      {
        return false;
      }
    return fVectorWord[fLongWordLength - 1] >>> fRemainingBits == 0;
  }
  // isZero

  /**
   * Returns the length of this <code>BitVector</code>.
   *
   * @return the length of this <code>BitVector</code>.
   */
  public int getBitLength()
  {
    return fBitLength;
  }

  /**
   * Returns the <code>long</code> array that internally holds data for this <code>BitVector</code>.
   *
   * @return the <code>long</code> array that internally holds data.
   */
  public long[] getVectorWord()
  {
    return fVectorWord;
  }

  /**
   * Returns the length of the <code>long</code> array that internally
   * holds data for this <code>BitVector</code>.
   *
   * @return the length of the <code>long</code> array that internally
   *   holds data.
   */
  public int getWordLength()
  {
    return fLongWordLength;
  }

  /**
   * Returns the <code>BitVectorIterator</code> that is backed by
   * this <code>BitVector</code>.
   *
   * @return the <code>BitVectorIterator</code> that is backed by
   * this <code>BitVector</code>.
   */
  public BitVectorIterator bitVectorIterator()
  {
    return new BitVectorIterator(this);
  }

  /**
   * Performs the bitwise NOT operation on this <code>BitVector</code>
   * and store the result into the specified argument. The argument
   * must have the same length as this <code>BitVector</code>.
   *
   * @param pResult the <code>BitVector</code> where the result of
   *   the NOT operation is stored.
   * @return <code>pResult</code>, the result of the NOT operation.
   */
  public BitVector vectorNot(BitVector pResult)
  {
    for (int i = 0; i < fLongWordLength; i++)
      pResult.fVectorWord[i] = ~fVectorWord[i];
    return pResult;
  }
  // vectorNot

  /**
   * Performs the bitwise AND operation between this <code>BitVector</code>
   * and the argument <code>pOperand2</code>, and stores the result
   * into <code>pResult</code>. Both <code>pOperand2</code> and
   * <code>pResult</code> must have the same length as this
   * <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> with which this
   * <code>BitVector</code> is ANDed.
   * @param pResult the <code>BitVector</code> where the result of
   * the AND operation is stored.
   * @return <code>pResult</code>, the result of the AND operation.
   */
  public BitVector vectorAnd(BitVector pOperand2, BitVector pResult)
  {

    for (int i = 0; i < fLongWordLength; i++)
      pResult.fVectorWord[i] = fVectorWord[i] & pOperand2.fVectorWord[i];
    return pResult;
  }
  // vectorAnd

  /**
   * Performs the bitwise OR operation between this <code>BitVector</code>
   * and the argument <code>pOperand2</code>, and stores the result
   * into <code>pResult</code>. Both <code>pOperand2</code> and
   * <code>pResult</code> must have the same length as this
   * <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> with which this
   * <code>BitVector</code> is ORed.
   * @param pResult the <code>BitVector</code> where the result
   * of the OR operation is stored.
   * @return <code>pResult</code>, the result of the OR operation.
   */
  public BitVector vectorOr(BitVector pOperand2, BitVector pResult)
  {
    for (int i = 0; i < fLongWordLength; i++)
      pResult.fVectorWord[i] = fVectorWord[i] | pOperand2.fVectorWord[i];
    return pResult;
  }
  // vectorOr

  /**
   * Performs the bitwise exclusive OR (XOR) operation between
   * this <code>BitVector</code> and the argument
   * <code>pOperand2</code>, and stores the result into
   * <code>pResult</code>. Both <code>pOperand2</code> and
   * <code>pResult</code> must have the same length as this
   * <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> with which
   * this <code>BitVector</code> is XORed.
   * @param pResult the <code>BitVector</code> where the
   * result of the XOR operation is stored.
   * @return <code>pResult</code>, the result of the XOR operation.
   */
  public BitVector vectorXor(BitVector pOperand2, BitVector pResult)
  {
    for (int i = 0; i < fLongWordLength; i++)
      pResult.fVectorWord[i] = fVectorWord[i] ^ pOperand2.fVectorWord[i];
    return pResult;
  }
  // vectorXor

  /**
   * Performs the bitwise subtraction operation between this
   * <code>BitVector</code> and the argument <code>pOperand2</code>,
   *  and stores the result into <code>pResult</code>.
   * Both <code>pOperand2</code> and <code>pResult</code> must
   * have the same length as this <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> by which amount
   * this <code>BitVector</code> is reduced (the second operand
   * of the bitwise subtraction operation).
   * @param pResult the <code>BitVector</code> where the result
   * of the subtraction operation is stored.
   * @return <code>pResult</code>, the result of the subtraction
   * operation.
   */
  public BitVector vectorSub(BitVector pOperand2, BitVector pResult)
  {

    for (int i = 0; i < fLongWordLength; i++)
      pResult.fVectorWord[i] = fVectorWord[i] &(~pOperand2.fVectorWord[i]);
    return pResult;
  }
  // vectorSub

  /**
   * Copies the contents of this <code>BitVector</code> into
   * the specified argument. The argument must have the same
   * length as this <code>BitVector</code>.
   *
   * @param pResult the destination of the copy operation.
   * @return <code>pResult</code>, the result of the copy operation.
   */
  public BitVector vectorCopy(BitVector pResult)
  {
    for (int i = 0; i < fLongWordLength; i++)
      pResult.fVectorWord[i] = fVectorWord[i];
    return pResult;
  }
  // vectorCopy

  /**
   * Compares this <code>BitVector</code> with the specified
   * argument for equality. Two <code>BitVector</code>s are equal
   * when they have the same contents (bit sequences). The argument
   * must have the same length as this <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> to be compared
   * with this <code>BitVector</code>.
   * @return true if all the bits of this <code>BitVector</code>
   * and those of the argument are equal.
   */
  public boolean vectorEqual(BitVector pOperand2)
  {
    int i;

    for ( i = 0; i < fLongWordLength - 1; i++)
      if (fVectorWord[i] != pOperand2.fVectorWord[i])
        return false;
    return fVectorWord[fLongWordLength - 1] >>> fRemainingBits == pOperand2.fVectorWord[fLongWordLength - 1] >>> fRemainingBits;
  }
  // vectorEqual

  /**
   * The equality for two <code>BitVector</code> objects is specified
   * by <code>vectorEqual<code>(). This method returns true if and
   * only if the specified argument <code>pObject</code> is an
   * instance of <code>BitVector</code>, its length is equal to
   * the length of this <code>BitVector</code>, and
   * <code>vectorEqual((BitVector)pObject)</code> returns true.
   *
   * @param pObject the object to be compared with this
   * <code>BitVector</code>.
   * @return true if the specified argument <code>pObject</code>
   * is a <code>BitVector</code> having the same length as this
   * one and <code>vectorEqual((BitVector)pObject)</code> returns true.
   * @see #vectorEqual
   */
  public boolean equals(Object pObject)
  {
    return pObject instanceof BitVector && ((BitVector)pObject).fBitLength == fBitLength && vectorEqual((BitVector)pObject);
  }

  /**
   * Resets all the bits of this <code>BitVector</code>.
   *
   * @return this <code>BitVector</code> with all the bits reset.
   */
  public BitVector vectorReset()
  {
    for (int i = 0; i < fLongWordLength; i++)
      fVectorWord[i] = 0;
    return this;
  }
  // vectorReset

  /**
   * Returns a <code>String</code> representation of this
   * <code>BitVector</code>. The resultant <code>String</code>
   * contains the bit positions of the set bits separated by spaces.
   *
   * @return the <code>String</code> representation of this
   * <code>BitVector</code>.
   */
   public String toString()
  {
    StringBuffer lBuffer = new StringBuffer(" ");
    int i;

    for (i = 0; i < fBitLength; i++)
    {
      if (getBit(i) == 1)
      {
        lBuffer.append(" " + i);
      }
    }

    return lBuffer.toString().intern();
  }

  /**
   * Returns a <code>String</code> representation of this
   * <code>BitVector</code>, which is at least as descriptive
   * as the one returned by <code>toString()</code>. It should
   * usually contain the <code>String</code> representation of
   * the objects that are associated with the set bits.
   * This method returns the same <code>String</code> as
   * <code>toString()</codce> for this class.
   *
   * @return the <code>String</code> representation of this
   * BitVector that is possibly more detailed than that
   * returned by toString.
   */
  public String toStringDescriptive()
  {
    return toString();
  }
}
// BitVector class
