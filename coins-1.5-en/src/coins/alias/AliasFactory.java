/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * Factory.java
 *
 * Created on June 30, 2003, 2:25 PM
 */

package coins.alias;

import coins.HirRoot;
import coins.ir.hir.HIR;
import coins.ir.hir.SubpDefinition;

/**
 * Factory class.
 * @author  hasegawa
 */
class AliasFactory
{
  /**
   * The <code>HirRoot</code> object shared by every module
   * in the program.
   */
  public final HirRoot hirRoot;

  /**
   * Creates a new instance of the factory class.
   *
   * @param pHirRoot <code>HirRoot</code> object shared by
   * every module in the program.
   */
  AliasFactory(HirRoot pHirRoot)
  {
    hirRoot = pHirRoot;
  }

  /**
   * Creates a new instance of <code>MyExpId</code>.
   *
   * @param pHIR HIR node the generated <code>MyExpId</code>
   * object corresponds to.
   * @return new instance of <code>MyExpId</code> that
   * correpsonds to the specified arguement.
   */
  MyExpId myExpId(HIR pHIR)
  {
    return new MyExpId(pHIR);
  }

  /**
   * Creates a new instance of <code>Tag</code> that
   * correponds to the Tag.STO_OTHER storage class.
   *
   * @return a new instance of <code>Tag</code> that
   * corresponds to the Tag.STO_OTHER storage class.
   * @see Tag
   */
  Tag otherTag()
  {
    return new Tag(Tag.KIND_UNKNOWN);
  }

  /**
   * Creates a new instance of <code>Tag</code> that has
   * no corresponding <code>MyExpId</code> object.
   *
   * @param pKind the kind of the generated tag.
   * @return new instance of <code>Tag</code> of specified kind.
   * @see Tag
   */
//	Tag tag( int pKind)
//	{
//		return new Tag( pKind);
//	}

  /**
   * Creates a new instance of <code>Tag</code> that
   * corresponds to the specified <code>MyExpId</code> object.
   *
   * @param pMyExpId <code>MyExpId</code> object the
   * generated <code>Tag</code> corresponds to.
   * @param pIsAnchored specifies whether the generated
   * <code>Tag</code> is anchored (corresponds to some
   * limited area in current frame/static area).
   * @param pKind kind of the generated tag.
   * @param pStorageClass storage class of the generated tag.
   * @return new instance of <code>Tag</code> with the
   * attributes specified by the arguments.
   * @see Tag
   */
  Tag tag(MyExpId pMyExpId, boolean pIsAnchored, int pKind, int pStorageClass)
  {
    return new Tag(pMyExpId, pIsAnchored, pKind, pStorageClass);
  }

  /**
   * Creates a new instance of <code>Tag</code> that
   * corresponds to the area allocated by the specified
   * malloc invocation node.
   *
   * @param pmallocInvocationNode malloc invocation node
   * the generated tag corresponds to.
   * @return new instance of <code>Tag</code> that corresponds
   * to the specified malloc invocation.
   */
  Tag mallocTag(HIR pmallocInvocationNode)
  {
    return new Tag(pmallocInvocationNode);
  }

  /**
   * Creates a new instance of <code>MyExpIdAssigner</code>
   * that assigns <code>MyExpId</code>s to the nodes
   * contained in the specified <code>SubpDefinition</code> object.
   *
   * @param pSubpDef <code>SubpDefinition</code> object
   * the generated <code>MyExpIdAssigner</code> object is
   * resposible for.
   * @return new instance of <code>MyExpIdAssigner</code>
   * that is responsible for the specified argument.
   */
  MyExpIdAssigner myExpIdAssigner(SubpDefinition pSubpDef)
  {
    return new MyExpIdAssigner(pSubpDef, hirRoot);
  }

  /**
   * Creates a new instance of <code>TagTreeBuilder</code>
   * that assigns <code>Tag</code>s to the
   * <code>MyExpId</code> objects in the specified argument
   * (<code>pMyExpIds</code>) and builds the tree relation
   * between those <code>Tag</code>s.
   *
   * @param pSubpDef <code>SubpDefinition</code> object
   * the generated <code>TagTreeBuilder</code> object is
   * responsible for.
   * @param pMyExpIds array of <code>MyExpId</code> objects
   * where the index of the array corresponds to the index
   * of the HIR node each <code>MyExpId</code> object
   * corresponds to.
   * @param pIsOptimistic determines the set of assumptions
   * about aliasing.
   * @return new instance of <code>TagTreeBuilder</code>.
   * @see AliasAnalHir1#AliasAnalHir1(boolean, HirRoot)
   */
  TagTreeBuilder tagTreeBuilder(SubpDefinition pSubpDef, MyExpId pMyExpIds[],
    boolean pIsOptimistic)
  {
    return new TagTreeBuilder(pSubpDef, pMyExpIds, pIsOptimistic, hirRoot);
  }

  /**
   * Creates a new instance of <code>TagVector</code>
   * with the specified length.
   *
   * @param pBitCount length of the <code>TagVector</code>.
   * @return new instance of <code>TagVector</code> with
   * the specified length.
   */
  TagVector tagVector(int pBitCount)
  {
    return new TagVector(pBitCount);
  }

  /**
   * Creates a new instance of <code>AliasGroup</code>
   * with the default initial capacity and default load factor,
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

