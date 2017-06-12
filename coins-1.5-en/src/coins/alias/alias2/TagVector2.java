/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * TagVector2.java
 *
 * Created on July 15, 2003, 2:33 PM
 */

package coins.alias.alias2;

import java.util.HashSet;
import java.util.Set;
import coins.alias.util.BitVector;
import coins.alias.util.BitVectorIterator;
import coins.alias.util.BriggsSet;
import coins.sym.Var;

/**
 * BitVector where each bit represents an object. Aggregate type
 * objects correspond to several bits. The last bit is specially
 * used for external objects.
 *
 * @author  hasegawa
 */
class TagVector2 extends BitVector
{

  private Set fSet = new HashSet();
  private BriggsSet fBriggs;

  private boolean fSetSynced;
    private boolean fBriggsSynced;

    boolean fIsDefinite;

    Var fAssociatedParam; // used for optimistic option

  /**
   * Creates a new instance of TagVector2 with the specified
   * bit length.
   *
   * @param pBitCount the bit length of the TagVector2.
   */
  TagVector2(int pBitCount)
  {
    super(pBitCount);
    fBriggs = new BriggsSet(pBitCount);
  }

  /**
   * Sets the bit at the specified position.
   *
   * @param pIndex the bit position the bit at which is to be set.
   */
  public void setBit(int pIndex)
  {
    super.setBit(pIndex);
    fSetSynced = false;
    fBriggsSynced = false;
  }

  /**
   * Resets the bit at the specified position.
   *
   * @param pIndex the bit position the bit at which is to be reset.
   */
  public void resetBit(int pIndex)
  {
    super.resetBit(pIndex);
    fSetSynced = false;
    fBriggsSynced = false;
  }

  /**
   * Performs the bitwise NOT operation on this <code>BitVector</code>
   * and store the result into the specified argument.
   * The argument must have the same length as this
   * <code>BitVector</code>.
   *
   * @param pResult the <code>BitVector</code> where the result
   * of the NOT operation is stored.
   * @return <code>pResult</code>, the result of the NOT operation.
   */
  public BitVector vectorNot(BitVector pResult)
  {
    ((TagVector2)pResult).fSetSynced = false;
    ((TagVector2)pResult).fBriggsSynced = false;
    return super.vectorNot(pResult);
  }

  /**
   * Performs the bitwise AND operation between this
   * <code>BitVector</code> and the argument <code>pOperand2</code>,
   * and stores the result into <code>pResult</code>.
   * Both <code>pOperand2</code> and <code>pResult</code> must
   * have the same length as this <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> with which this
   * <code>BitVector</code> is ANDed.
   * @param pResult the <code>BitVector</code> where the result
   * of the AND operation is stored.
   * @return <code>pResult</code>, the result of the AND operation.
   */
  public BitVector vectorAnd(BitVector pOperand2, BitVector pResult)
  {
    ((TagVector2)pResult).fSetSynced = false;
    ((TagVector2)pResult).fBriggsSynced = false;
    return super.vectorAnd(pOperand2, pResult);
  }

  /**
   * Performs the bitwise OR operation between this
   * <code>BitVector</code> and the argument <code>pOperand2</code>,
   * and stores the result into <code>pResult</code>.
   * Both <code>pOperand2</code> and <code>pResult</code> must
   * have the same length as this <code>BitVector</code>.
   *
   * @param pOperand2 the <code>BitVector</code> with which
   * this <code>BitVector</code> is ORed.
   * @param pResult the <code>BitVector</code> where the
   * result of the OR operation is stored.
   * @return <code>pResult</code>, the result of the OR operation.
   */
  public BitVector vectorOr(BitVector pOperand2, BitVector pResult)
  {
    ((TagVector2)pResult).fSetSynced = false;
    ((TagVector2)pResult).fBriggsSynced = false;
    return super.vectorOr(pOperand2, pResult);
  }

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
   * @param pResult the <code>BitVector</code> where
   * the result of the XOR operation is stored.
   * @return <code>pResult</code>, the result of the XOR operation.
   */
  public BitVector vectorXor(BitVector pOperand2, BitVector pResult)
  {
    ((TagVector2)pResult).fSetSynced = false;
    ((TagVector2)pResult).fBriggsSynced = false;
    return super.vectorXor(pOperand2, pResult);
  }

  /**
   * Performs the bitwise subtraction operation between
   * this <code>BitVector</code> and the argument <code>pOperand2</code>,
   * and stores the result into <code>pResult</code>.
   * Both <code>pOperand2</code> and <code>pResult</code> must have
   * the same length as this <code>BitVector</code>.
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
    ((TagVector2)pResult).fSetSynced = false;
    ((TagVector2)pResult).fBriggsSynced = false;
    return super.vectorSub(pOperand2, pResult);
  }

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
    ((TagVector2)pResult).fSetSynced = false;
    ((TagVector2)pResult).fBriggsSynced = false;
    return super.vectorCopy(pResult);
  }

  /**
   * Resets all the bits of this <code>BitVector</code>.
   *
   * @return this <code>BitVector</code> with all the bits reset.
   */
  public BitVector vectorReset()
  {
    super.vectorReset();
    fSetSynced = false;
    fBriggsSynced = false;
    return this;
  }


  /**
   * <p>Returns the BriggsSet view of this <code>BitVector</code>.
   * <code>BitVector</code> does not perform well for scanning,
   * so this method is for efficient scanning (while this set is
   * not modified). Change to the set returned by this method
   * will NOT be reflected in this bit vector and vice versa.</p>
   * <p>This method checks if this <code>BitVector</code> has
   * been modified after the previous call to this method,
   * and if not, returns the <code>BriggsSet</code> instance
   * returned by the previous call. If this <code>BitVector</code>
   * has been modified since the previous call, or there has
   * never been such a call, then it returns a new copy of
   * the <code>BriggsSet</code> synchronized with the current
   * state of this <code>BitVector</code>. So if the
   * <code>BriggsSet</code> instance returned by a previous
   * call to this method has been modified while this
   * <code>BitVector</code> itself has not been modified,
   * this method will return the same <ocde>BriggsSet</code>
   * instance returned by the previous call, which is not
   * synchronized with the current state of this
   * <code>BitVector</code>. In general, the <code>BriggsSet</code>
   * instance returned by this method should not be modified.</p>
   *
   * @return the <code>BriggsSet</code> view of this TagVector2.
   */
  BriggsSet toBriggsSet()
  {
    if (fBriggsSynced)
      return fBriggs;
    fBriggs.makeSetEmpty();
    BitVectorIterator lIt = bitVectorIterator();
    for (; lIt.hasNext();)
    {
      int lIndex = lIt.nextIndex();
      if (lIndex != -1)
        fBriggs.insertElement(lIndex);
    }
    fBriggsSynced = true;
    return fBriggs;
  }


    public boolean equals(Object pObject)
    {
        return super.equals(pObject) &&
          fIsDefinite == ((TagVector2)pObject).fIsDefinite;
    }

    public String toString()
    {
        return (fIsDefinite ? "D: " : "P: ")  + super.toString() +
            (fAssociatedParam == null ? "" : " Associated Param: "
             + fAssociatedParam.toString());
    }

}

