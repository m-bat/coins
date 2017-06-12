/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * Factory.java
 *
 * Created on June 30, 2003, 2:25 PM
 */

package coins.alias.alias2;

import coins.HirRoot;
import coins.alias.AliasGroup;

/**
 * Factory class.
 * @author  hasegawa
 */
class AliasFactory2
{
  /**
   * The <code>HirRoot</code> object shared by every module
   * in the program.
   */
  public final HirRoot hirRoot;

  /**
   * Creates a new instance of the factory class.
   *
   * @param pHirRoot <code>HirRoot</code> object shared
   * by every module in the program.
   */
  AliasFactory2(HirRoot pHirRoot)
  {
    hirRoot = pHirRoot;
  }


  /**
   * Creates a new instance of <code>TagVector2</code>
   * with the specified length.
   *
   * @param pBitCount length of the <code>TagVector2</code>.
   * @return new instance of <code>TagVector2</code>
   * with the specified length.
   */
  TagVector2 tagVector(int pBitCount)
  {
    return new TagVector2(pBitCount);
  }

  /**
   * Creates a new instance of <code>AliasGroup</code> with
   * the default initial capacity and default load factor,
   * which is 0.75.
   *
   * @return new instance of <code>AliasGroup</code>.
   * @see java.util.HashSet#HashSet()
   */
  AliasGroup aliasGroup()
  {
    return new AliasGroup();
  }

}

