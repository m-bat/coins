/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.sym;

import coins.ir.hir.Exp;

//========================================//

/** Elem interface
 *  Elem class (structure/union element class) interface.
 *  See getFirstElem of TypeInterface.
**/
public interface
Elem extends Var
{

/** isDispEvaluable
 * @return true if the displacement of this element
 *      is evaluable as integer value, false otherwise.
**/
boolean isDispEvaluable();

/** evaluateDisp
 *  Get the displacement of this element.
 *  If it is not evaluable, return 0.
 *  @return integer value representing the displacement
 *      of this element in byte.
**/
long evaluateDisp();

/** setDispExp
 *  Set the displacement of this element.
 *  @param pDispExp Expression representing the displacement
 *      of this element in byte.
**/
void    setDispExp( Exp pDispExp );            // (##5)

void setDisplacement( long pDisplacement );

/**
<PRE>
 * Set the size of bit field and set an indication showing
 * that this element is a bit field.
 * The size of bit field should not exceed the bit width
 * of long (typeLong.getSizeValue()*8).
 * See also setBitFieldOffset method.
</PRE>
 * @param pBitSize number of bits in this bit field.
 */
public void
setBitFieldSize( int pBitSize );

/** setBitFieldOffset
<PRE>
 *  Set bit field offset of this element if it is a bit field.
 *  Consider bit offset and bit field length of a bit field are
 *  represented by bitOffset and bitFieldSize each respectively.
 *  In the case of machineParam.isPackedFromLeft() is true,
 *  the most significant bit of the 1st field is placed at
 *  the most significant bit of "containing object" (which is
 *  explained later), and the bit field is placed at bit positions
 *     bitOffset through (bitOffset + bitFieldSize - 1)
 *  in the containing object, where the most significant bit position
 *  of the containing object is zero and the least significant bit
 *  position is (bit width of containing object -1).
 *  In the case of machineParam.isPackedFromLeft() is false, that is,
 *  machineParam.isPackedFromRight() is true,
 *  the least significant bit of the 1st field is placed at
 *  the least significant bit of containing object, and
 *  the bit field is placed at bit positions
 *     bitOffset through (bitOffset + bitFieldSize - 1)
 *  in the containing object, where the least significant bit position
 *  of the containing object is zero and the most significant bit
 *  position is (bit width of containing object -1).
 *  In both cases, displacement of the containing object within the
 *  surrounding structure is shown by evaluateDisp().
 *  Source language analyzer is not requested to set bit field offset
 *  but requested to set only the size of bit fields by
 *  setBitFieldSize method because the bit offset computation is
 *  usually done by finishStructType() of StructType.
 *  "containing object" mentioned in the previous paragraph is a
 *  hypothetical object that contains bit fields. The alignment/size
 *  of the containing object is considered to be the alignment/size
 *  of the largest bit field type among the consecutive
 *  bit fields (all of which have type int or unsigned int in C).
 *  When a bit field is encountered in structure declaration and
 *  the total size of preceding elements is lSize, then
 *  the displacement of the containing object is computed as
 *      (lSize / lElemTypeSize) * lElemTypeSize
 *  where lElemTypeSize is the size of the largest bit field type
 *  among the consecutive bit fields.
 *  If the containing object has space to hold the bit field,
 *  then the bit field is placed in the containing object.
 *  When there remains no space to hold new bit field after
 *  successive allocation of preceding bit fields, a new
 *  containing object is placed adjacent to the old one
 *  and remaining bit fields are placed in the new containing
 *  object.
 *  A bit field is contained in one containing object and does not
 *  span to multiple containing objects. Multiple bit fields may be
 *  contained in one containing object if the containing object can
 *  contain them and bit fields continues successively.
 *  The structure is supposed to have the alignment greater or equal
 *  to the alignment of the containing object and its size is the
 *  multiple of the size of the containing object.
 *  Consider a C language structure
 *     struct st1 {
 *       short s1;
 *       char  c1;
 *       unsigned int b1:5;
 *       unsigned int b2:1;
 *       unsigned int b3:3;
 *       char c2;
 *     };
 *  on machine having 2 as short size, 4 as int size. Displacements
 *  and bit offsets in this case will be as follows:
 *     element displacement  bit offset
 *       s1      0            -
 *       c1      2            -
 *       b1      0           24
 *       b2      0           29
 *       b3      4            0
 *       c2      5            -
 *  and the structure will have alignment 4 and size 8.
 *  As for C program
 *    struct bitField1 {
 *    unsigned a1:1;
 *    unsigned b1:1;
 *  };
 *  struct bitField2 {
 *    struct bitField1 st1;
 *    unsigned char cc1;
 *    struct bitField1 st2;
 *   unsigned char cc2;
 *  };
 * Displacements and bit offsets in this case will be as follows:
 *     element displacement  bit offset
 *     st1.a1    0            0
 *     st1.b1    0            1
 *     cc1       4            -
 *     st2.a1    8            0
 *     st2.b1    8            1
 *     cc2      12            -
 *  and the structure will have alignment 4 and size 16.

 *  A bit field can be loaded to register by loading the corresponding
 *  containing object whose displacement can be get be evaluateDisp().
 *  Access methods of a bit field shows
 *    evaluateDisp(): displacement of the containing object containing
 *                    the bit field.
 *    getBitSize():   the number of bits in the bit field.
 *    getBitOffset(): bit offset position in the containing object
 *                    (its meaning differs according to the value of
 *                     isPackedFromLeft() as mentioned before).
 *  @param pBitOffset bit offset of the bit field as explained above.
</PRE>
**/
public void
setBitFieldOffset( int pBitOffset );

/** setBitField  //##53
 *  Set bit field information for this element if it is a bit field.
 *  This method should not be called for elements that are not bit field.
 *  @param pBitSize number of bits in this bit field.
 *  @param pBitOffset bit offset of the field
 *     as explained above.
 *  ***deprecated
**/
//##68 public void
//##68 setBitField( int pBitSize, int pBitOffset );

/** getBitSize
 * See also setBitFieldOffset method.
 *  @return the number of bits in this field.
**/
public int
getBitSize();

/** getBitOffset
 * See also setBitFieldOffset method.
 *  @return the bit offset of this field.
**/
public int
getBitOffset();

/** isBitField
 *  @return true if this is a bit field, false otherwise.
**/
public boolean
isBitField();

/** searchElem
 *  Get the proper element having the same name as this symbol in
 *  the struct/union variable pVar.
 *  "this" is a symbol having the same name as the element to be searched.
 *  @param pVar sturcture or union variable.
 *  @return the proper element defined in pDefinedIn.
 *  This is used in such case as to distinguish proper element when
 *  same element name is used in different structures.
**/
Elem searchElem( Var pVar );

/** searchElem
 *  Get the proper element having the same name as this symbol in
 *  pRecordedIn construct (structure or union).
 *  "this" is a symbol having the same name as the element to be searched.
 *  @param pRecordedIn enclosing construct defining the element to be
 *      searched (sturcture type or union type in C).
 *  @return the proper element defined in pRecordedIn.
 *      If not found, return null.
 *  This is used in such case as to distinguish proper element when
 *  same element name is used in different structures.
**/
Elem
searchElem( Type pRecordedIn );

/** getUpperType
 *  @return StructType or UnionType instance containing this element.
**/
public Type
getUpperType();

} // Elem
