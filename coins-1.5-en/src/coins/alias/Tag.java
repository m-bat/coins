/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*
 * Tag.java
 *
 * Created on June 30, 2003, 2:40 PM
 */
package coins.alias;

import java.util.ArrayList;
import java.util.List;
import coins.ir.hir.HIR;
import coins.sym.FlagBox;
import coins.sym.FlagBoxImpl;
import coins.sym.Type;

/**
 *<p> A Tag corresponds to some area in memory. It is
 * assciated with the set of lvalue expressions that have
 * the same form. So this could have extended ExpId,
 * but instead has a field that links to the ExpId
 * (<code>MyExpId</code>) it represents.</p>
 *<p> Tags form a tree structure in which parent nodes
 * represent areas in memory that includes the areas
 * child nodes represent. This relationship is used
 * to assign bits to each <code>Tag</code>. It also
 * makes it possible, via promote() method,
 * to accurately handle the situation where some area
 * obsuring operations, such as pointer arithmetic,
 * hss been performed.
 *
 *
 * @author  hasegawa
 */
class Tag
{
  /**
   * <code>MyExpId</code> object this Tag is associated with.
   */
  MyExpId fMyExpId;

  /**
   * Kind of this Tag. See the static fields of the names
   * KIND_**** of this class.
   */
  int fKind;

  /**
   * Storage class of this Tag.
   */
  int fStorageClass;

  /**
   * <code>Type</code> of the object this Tag represents.
   * Tags that represent
   */
  Type fType;

  /**
   * Bit position this Tag correponds to in the
   * <code>TagVector</code>s to be created. <code>-1</code>
   * if there is no such corresponding bit.
   */
  int fBitPos = -1;

  /**
   * Specifies if this Tag represents a unique area in memory.
   *  A Tag associated with a <code>Var</code> or constant-subscript element of an array <code>Var</code> or <code>malloc</code>ed area is unique.
   */
  boolean fIsUnique;

  /**
   * <em>Parent</em> Tag of this Tag. The parent Tag of
   * a Tag corresponds to the area in memory that directly
   * encloses the area the original Tag represents. As such,
   * the original Tag must represent a relatively limited
   * area in memory (isAnchored), or fAnchored is <code>null</code>.
   *
   * See #fChildren
   * See #isAnchored
   */
  Tag fParent;

  /**
   * <code>List</code> of <em>child</em> Tags of this Tag.
   * A child Tag of a Tag corresponds to an area in memory
   * that is directly enclosed by the area the original
   * Tag represnts. As such, the original Tag must represent
   * a relatively limited area in memory (isAnchored), or
   *  fChildren is empyt.
   *
   * See #fParent
   * See #isAnchored
   */
  List fChildren = new ArrayList();
  private Tag fAnchor;

    Tag fUnionAncestor;

  /**
   * The kind for the Tags represent vector type objects.
   */
   static final int KIND_VECTOR = 5;

   /**
    * The kind for the Tags that represent struct type objects.
    */
   static final int KIND_STRUCT = 6;

   /**
    * The kind for the Tags that represent union type objects.
    */
   static final int KIND_UNION = 7;

   /**
    * The kind for the Tags that represent atomic (scalar)
    * type objects.
    */
   static final int KIND_ATOMIC = 8;

   /**
    * The kind for the Tag whose type is unknown,
    */
   static final int KIND_UNKNOWN = 9;

  /**
   * Root storage class, the storage class that corresponds
   * to the whole memory space.
   */
  static final int STO_ROOT = 0;

  /**
   * Current frmae storage class, the storage class that
   * corresponds to automatic variables declared within
   * the current subprogram.
   */
  static final int STO_CUR_FRAME = 1;

  /**
   * Static storage class, the storage class that corresponds
   * to static <code>Var</code>s.
   */
  static final int STO_STATIC = 2;

  /**
   * Heap storage class, the storage class that corresponds
   * to areas allocated by the current subprogram.
   */
  static final int STO_HEAP = 3;

  /**
   * Other storage class, the storage class that does not
   * fall into current frame or static or heap. It covers
   * automatic <code>Var</code>s from subprograms calling
   * the current subprogram, global <code>Var</code>s that
   *  do not appear in the current subprogram, and areas
   * allocated by the subprograms calling the current subprogram.
   */
  static final int STO_OTHER = 4;

  /**
   * <code>TagVector</code> object that represents all
   * the objects this Tag may represnt. This will change
   *  as the analysis goes on.
   */
  TagVector fTagVect;

  // Flags
  private FlagBox fFlags = new FlagBoxImpl();

  /**
   * Flag number for anchoredness.
   *
   * See getFlag
   * See setFlag
   */
  static final int ANCHORED = 0;

  /**
   * Flag number for whether being a union descendant.
   *
   * See getFlag
   * See setFlag
   */
  static final int HAS_UNION_ANCESTOR = 1;

  /**
   * Creates a new instance of Tag that is associated with
   * a <code>MyExpId</code> object.
   *
   * @param pMyExpId <code>MyExpId</code> object this Tag
   * is associated with.
   * @param pIsAnchored Is this Tag associated with
   * a single named <code>Var</code>?
   * @param pKind kind of this Tag.
   * @param pStorageClass storage class of this Tag.
   */
  Tag(MyExpId pMyExpId, boolean pIsAnchored, int pKind,
      int pStorageClass)
  {
    fMyExpId = pMyExpId;
    if (pIsAnchored)
      fAnchor = this;
    fKind = pKind;
    fStorageClass = pStorageClass;
    fType = fMyExpId.getHir().getType();
    if (fType.getTypeKind() == Type.KIND_UNION)
        {
            setFlag(Tag.HAS_UNION_ANCESTOR, true);
            fUnionAncestor = this;
        }
  }

  /**
   * Creates a new instance of Tag that is associated with
   * an area allocated by a single <code>malloc</code>
   * invocation. <code>malloc</code> invocation within
   * loops are not considered distinct (there are
   * considered a single invocation).
   *
   * @param pmallocInvocationNode <code>FunctionExp</code>
   * node that is an <code>malloc</code> invocation node.
   */
  Tag(HIR pmallocInvocationNode)
  {
    fAnchor = this;
//		fKind = KIND_HEAP;
    fKind = KIND_UNKNOWN;
    fStorageClass = STO_HEAP;
    fIsUnique = true;

  }

  /**
   * Creates a new instance of Tag that corresponds to
   * the STO_OTHER storage class.
   *
   * @param pKind the kind of Tag to be created.
   */
  Tag(int pKind)
  {
    fKind = pKind;
    switch (fKind)
    {
      case Tag.KIND_UNKNOWN:
        fStorageClass = Tag.STO_OTHER;
        break;
      default:
        throw new AliasError("Unexpected.");
    }
  }

//	/**
//	 * Creates a new instance of Tag that is of specified kind.
//    There is no corresponding <code>MyExpId</code> object.
//	 *
//	 * @param pKind kind of the Tag to be created.
//	 */
//	Tag(int pKind)
//	{
//		fKind = pKind;
//		switch (fKind)
//		{
////			case Tag.KIND_CUR_FRAME:
////				fStorageClass = Tag.STO_CUR_FRAME;
////				break;
////			case Tag.KIND_STATIC:
////				fStorageClass = Tag.STO_STATIC;
////				break;
////			case Tag.KIND_HEAP:
////				fStorageClass = Tag.STO_HEAP;
////				break;
//			case Tag.KIND_UNKNOWN:
//				fStorageClass = Tag.STO_OTHER;
//				break;
////			case Tag.KIND_ROOT:
////				fStorageClass = Tag.STO_ROOT;
////				break;
//			default:
//				throw new AliasError("This constructor is not for an anchored Tag.");
//		}
//	}

  /**
   * Inherits attributes from the parent Tag. Attributes to
   * be inherited are anchoredness (specified by isAnchored)
   * and HAS_UNION_ANCESTOR flag. Additionally, if this Tag
   * is of kind UNION, sets the HAS_UNION_ANCESTOR flag.
   *
   * See #isAnchored
   * See HAS_UNION_ANCESTOR
   * See KIND_UNION
   */
   void inheritAttributes()
  {
    if (fParent != null)
    {
      fAnchor = fParent.fAnchor;
      setFlag(Tag.HAS_UNION_ANCESTOR, fKind == KIND_UNION || fParent.getFlag(Tag.HAS_UNION_ANCESTOR));
            if (fParent.getFlag(Tag.HAS_UNION_ANCESTOR))
                fUnionAncestor = fParent.fUnionAncestor;
    }
  }

  /**
   * Returns <code>true</code> if this Tag has a corresponding
   * <em>anchor</em>. A Tag represents an area in memory,
   * but which area it represents may not be accurately
   * determined at compile time. Tags which are anchored
   * can only represent areas in memory that are inside
   * the area the anchor Tag represents.
   *
   * @return true if this Tag has a corresponding anchor.
   */
  boolean isAnchored()
  {
    return fAnchor != null;
  }

  /**
   * Returns the Tag that covers the area that the result
   * of the pointer operation that has a pointer expression
   * that points to one of the areas that this Tag
   * represents as one of its operands can point to.
   * It is either fAnchor or this Tag itself.
   *
   * @return the Tag the area which it represents
   * the result of the pointer operations involving
   * pointers to the area this Tag represents may point to.
   */
  Tag promote()
  {
    return isAnchored() ? fAnchor : this;
  }


  boolean getFlag(int pFlag)
  {
    return fFlags.getFlag(pFlag);
  }

  void setFlag(int pFlag, boolean pYesNo)
  {
    fFlags.setFlag(pFlag, pYesNo);
  }

  /**
   * Returns the String representation of this Tag.
   *
   * @return the String representation of this Tag.
   */
  public String toString()
  {
    StringBuffer lBuff = new StringBuffer();
    lBuff.append("tag ");
    if (fMyExpId != null)
      lBuff.append(fMyExpId.getHir().toString());
    else
      lBuff.append("kind: " + fKind);
    return lBuff.toString();
  }
}

