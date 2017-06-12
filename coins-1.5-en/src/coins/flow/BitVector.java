/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.flow; //##60

/**
 * Vector of bits. Instances of (the classes that inplement) this interface represent various data flow information, and, through the bit manipulation operations this interface provides, are the basis for nontrivial flow analyses that require solving equations by iteration.
 *
 */
public interface BitVector 
{
  /** setBit
   *  Set pIndex-th bit of this bit vector to 1.
   *  @return this bit vector whose pIndex-th bit is reset to 1.
   **/
  public void setBit(int pInx);

  /** resetBit
   *  Reset pIndex-th bit of this bit vector to 0.
   *  @return this bit vector whose pIndex-th bit is reset to 0.
   **/
  public void resetBit(int pInx);

  /** getBit
   *  Get pIndex-th bit of this bit vector.
   *  @return pIndex-th bit of this bit vector.
   **/
  public int getBit(int pInx);

  /**
   * Same as <code>getBit(pInx) == 1</code>.
   */
  boolean isSet(int pInx);

  /**
   * Are all the bits zero?
   */
  boolean isZero();

  /**
   * Returns the length of this <code>BitVector</code>, not counting the 0-th bit, which is not used.
   */
  public int //##8
   getBitLength(); //##8

  /**
   * Returns the # of long words this <code>BitVector</code> uses to store its data.
   */
  public int //##8
   getWordLength(); //##8

  /**
   * Returns the array of <code>long</code>, which is where the actual data for this <code>BitVector</code> is stored.
   */
  public long[] getVectorWord(); //##8

  /**
   * Returns the <code>BitVectorIterator</code> object backed by this <code>BitVector</code>
   */
  BitVectorIterator bitVectorIterator( SubpFlow pSubpFlow ); //##60

  /** vectorAnd;
   *  Make a bit vector and set its value by executing bit-wise-and
   *  operation on <code>this</code> and pOperand2.
   *  @return the bit vector obtained by bit-wise-and operaion.
   **/
  public void vectorAnd(BitVector pOperand2, BitVector pResult);

  /** vectorOr;
   *  Make a bit vector and set its value by executing bit-wise-or
   *  operation on <code>this</code> and pOperand2.
   *  @return the bit vector obtained by bit-wise-or operaion.
   **/
  public void vectorOr(BitVector pOperand2, BitVector pResult);

  /** vectorXor;
   *  Make a bit vector and set its value by executing bit-wise exclusive-or
   *  operation on <code>this</code> and pOperand2.
   *  @return the bit vector obtained by bit-wise exclusive-or operaion.
   **/
  public void vectorXor(BitVector pOperand2, BitVector pResult);

  /** vectorNot;
   *  Make a bit vector and set its value by executing bit-wise-not
   *  operation on pOperand.
   *  @return the bit vector obtained by bit-wise-not operaion.
   **/
  public void vectorNot(BitVector pResult);

  /** vectorSub;
   *  Make a bit vector and set its value by executing bit-wise-sub
   *  operation on <code>this</code> and pOperand2.
   *  (pOperand2 is subtracted from this.) //##62
   */
  public void vectorSub(BitVector pOperand2, BitVector pResult);

  /** vectorCopy;
   *  Make a bit vector and set its value same to that of pOperand.
   *  @return a new bit vector having the same value as pOperand.
   **/
  public void vectorCopy(BitVector pResult);

  /** vectorEqual;
   *  See if <code>this</code> and pOperand2 have the same value or not.
   *  @return true if <code>this</code> and pOperand2 have the same value,
   *    false otherwise.
   **/
  public boolean vectorEqual(BitVector pOperand2);

  /**
   * Same as <code>vectorEqual</code> if the specified argument is an instance of <code>BitVector</code>, otherwise returns <code>false</code>.
   */
  public boolean equals(Object pObj);

  /** vectorReset;
   *  Reset all bits of this vector to 0.
   *  @return this vector after resetting all of its bits to 0.
   **/
  void vectorReset();

  /** toString
   *  Get the sequence of indexes corresponding to the position
   *  having 1.
   **/
  public String toString();

  /**
   * Returns a possibly more descriptive string representation than <code>toString()</code> of this <code>BitVector</code>.
   */
  String toStringDescriptive();
}
 // BitVector interface
