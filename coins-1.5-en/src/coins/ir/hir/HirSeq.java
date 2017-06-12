/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ir.hir;


/** HirSeq interface
<PRE>
 *  Sequence of expressions, symbols, and others.
 * There are following public constructors in the class
 * HirSeqImpl corresponding the this interface.
 * They construct HirSeq objects according to the
 * number of child HIR subtrees given as parameters.

 HirSeqImpl( HirRoot pHirRoot, HIR pChild1 )
 HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2 )
 HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2,
             HIR pChild3 )
 HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2,
             HIR pChild3, HIR pChild4 )
 HirSeqImpl( HirRoot pHirRoot, HIR pChild1, HIR pChild2,
             HIR pChild3, HIR pChild4, HIR pChild5 )
 public String
</PRE>
**/
public interface
HirSeq extends HIR
{

} // HirSeq interface

