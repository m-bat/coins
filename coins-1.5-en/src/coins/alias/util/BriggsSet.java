/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * BriggsSet.java
 *
 * Created on July 24, 2003, 11:31 AM
 */

package coins.alias.util;

/**
 * Set of unsigned integers implemented using two arrays.
 * It has constant time performance for search, insertion,
 * deletion, and initialization (emptying) operations.
 * Scanning all the elements takes time proportional to
 * the size of the set. The largest integer (plus 1) that can
 * enter the set has to be specified when instatiating this
 * class, and the instatiation itself takes time proportional
 * to the number specified.
 *
 * @see "D. Morgan, Building an Optimizing Compiler, pp.90-92"
 * @author  hasegawa
 */
public class BriggsSet
{
  private int fIndexes[], fValues[], fNext;

  /** Creates a new instance of BriggsSet
   *
   * @param pSize the grand upper size, or the largest number
   * that can enter the set plus one.
   */
  public BriggsSet(int pSize)
  {
    fIndexes = new int[pSize];
    fValues = new int[pSize];
  }

  /**
   * Initializes the set.
   */
  public void makeSetEmpty()
  {
    fNext = 0;
  }

  /**
   * Searches for the given element.
   *
   * @param pElem the element to search for.
   * @return true if the element was found.
   */
  public boolean findElement(int pElem)
  {
    int lIndex;
    lIndex = fIndexes[pElem];
    return (lIndex < fNext && lIndex >= 0 &&
            fValues[lIndex] == pElem);
  }

  /**
   * Tries to insert the given element into this set.
   *
   * @param pElem the element to insert.
   * @return true if insertion really took place.
   */
  public boolean insertElement(int pElem)
  {
    if (!findElement(pElem))
    {
      fValues[fNext] = pElem;
      fIndexes[pElem] = fNext++;
      return true;
    }
    return false;
  }

  /**
   * Tries to delete the given element from this set.
   *
   * @param pElem the element to delete.
   * @return true if deletion really took place.
   */
  public boolean deleteElement(int pElem)
  {
    if (findElement(pElem))
    {
      fValues[fIndexes[pElem]] = fValues[--fNext];
      fIndexes[fValues[fNext]] = fIndexes[pElem];
      return true;
    }
    return false;
  }

  /**
   * Returns the scanner that iterates through this set.
   */
  public Scanner scanner()
  {
    return new Scanner()
    {
      private int i;

      public boolean hasNext()
      {
        return i < fNext;
      }

      public int next()
      {
        return fValues[i++];
      }

      public void delete()
      {
        fValues[--i] = fValues[--fNext];
        fIndexes[fValues[fNext]] = i;
      }
    };
  }
}

