/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
// MachineParam.java
//                    //##10  Dec. 2001
package coins;

import coins.sym.Type;

/** MachineParam class:
 *  Define target machine parameters and methods.
 *  This is the super class of MachineParamSparc, MachineParamX86, etc.
 *  Default is Sparc.
**/
public class
MachineParam
{

  public final IoRoot
    ioRoot;

public
MachineParam( IoRoot pIoRoot )
{
  ioRoot = pIoRoot;
}

//====== Global Constants (default target is SPARC version 8) ======//

//##87 BEGIN
  //##88 public static final int
  static final int //##88
    NUMBER_OF_BITS_IN_ADDRESSING_UNIT = 8; // Number of bits
         // in a byte for byte-addressing machine,
         // number of bits in a word for word-addressing machine.
  //##88 public static final int
  static final int //##88
    NUMBER_OF_BITS_IN_PACKED_CHAR = 8; // Minimum number of
         // bits required to represent a character data.
         // For a machine that has no packed character string,
         // it will be the same to
         //   SIZEOF_CHAR * NUMBER_OF_BITS_IN_ADDRESSING_UNIT,
         // but for a word addressing machine that can pack
         // several character data in one word,
         // NUMBER_OF_BITS_IN_PACKED_CHAR represents the
         // minimum number of bits required to represent
         // the packed character and this value (allocated number of
         // bits for the packed character) may be different from
         //   SIZEOF_CHAR * NUMBER_OF_BITS_IN_ADDRESSING_UNIT.
  //##88 public static final int
  static final int //##88
    NUMBER_OF_BITS_IN_PACKED_SHORT = 16; // Minimum number of
         // bits required to represent short int data.
         // For a machine that has no packed short int array,
         // it will be the same to
         //   SIZEOF_SHORT * NUMBER_OF_BITS_IN_ADDRESSING_UNIT,
         // but for a word addressing machine that can pack
         // several short int data in one word,
         // NUMBER_OF_BITS_IN_PACKED_SHORT represents the
         // minimum number of bits required to represent
         // the packed short int and this value (allocated number of
         // bits for the packed cshort int) may be different from
         //   SIZEOF_SHORT * NUMBER_OF_BITS_IN_ADDRESSING_UNIT.
//##87 END
//##88 BEGIN
public int
  numberOfBitsInAddressingUnit()
{
  return NUMBER_OF_BITS_IN_ADDRESSING_UNIT;
}

public int
  numberOfBitsInPackedChar()
{
  return NUMBER_OF_BITS_IN_PACKED_CHAR;
}

public int
numberOfBitsInPackedShort()
{
return NUMBER_OF_BITS_IN_PACKED_SHORT;
}
//##88 END

  //##88 public static final int
  static final int //##88
     // Number of addressing units
     // (byte or word) required to represent
     // the value of basic type.
    SIZEOF_BOOL       = 4, //##14 (for C and Sparc combination)
//  SIZEOF_S_CHAR     = 1, //##9
    SIZEOF_SHORT      = 2,
    SIZEOF_INT        = 4, //###
    SIZEOF_LONG       = 4, //##12
    SIZEOF_LONG_LONG  = 8, //##52
    SIZEOF_CHAR       = 1,
    SIZEOF_WCHAR      = 4, //##51
//  SIZEOF_U_CHAR     = 1, //##9
//  SIZEOF_U_SHORT    = 2, //##9
//  SIZEOF_U_INT      = 4, //##9
//  SIZEOF_U_LONG     = 4, //##9
//  SIZEOF_U_LONG_LONG= 8, //##9 //##38
    SIZEOF_VOID       = 4, //##17
    SIZEOF_PTR        = 4,
    SIZEOF_ENUM       = 4,
    SIZEOF_ADDRESS    = 4,
    SIZEOF_OFFSET     = 4,
    SIZEOF_FLOAT      = 4,
    SIZEOF_DOUBLE     = 8,
    SIZEOF_LONG_DOUBLE= 8;

  //##88 public static final int
  static final int //##88
      // Alignment of basic types in addressing units
    // Alignment of basic types in addressing units
    // (byte or word).
//##34    ALIGN_BOOL       = 1,
    ALIGN_BOOL       = 4, // See SIZEOF_BOOL //##34
//  ALIGN_S_CHAR     = 1, //##9
    ALIGN_SHORT      = 2,
    ALIGN_INT        = 4,
    ALIGN_LONG       = 4, //##fnami
    ALIGN_LONG_LONG  = 8, //##52
    ALIGN_CHAR       = 1,
    ALIGN_WCHAR      = 2, //##9
//  ALIGN_U_CHAR     = 1, //##9
//  ALIGN_U_SHORT    = 2, //##9
//  ALIGN_U_INT      = 4, //##9
//  ALIGN_U_LONG     = 4, //##9
//  ALIGN_U_LONG_LONG= 8, //##9 //##38
    ALIGN_VOID       = 4, //##17
    ALIGN_PTR        = 4,
    ALIGN_ENUM       = 4,
    ALIGN_ADDRESS    = 4,
    ALIGN_OFFSET     = 4,
    ALIGN_FLOAT      = 4,
    ALIGN_DOUBLE     = 8,
    ALIGN_LONG_DOUBLE= 8,
    ALIGN_STRUCT_MIN = 1, // Minimum alignment of struct/union. //##51
    ALIGN_REGION_MIN = 1, // Minimum alignment of Region.       //##51
    ALIGN_SUBP       = 4; // Alignment number of subprogram object code.

//##52  public static final int     // Allocation unit for bit field sequence.
//##52    BIT_FIELD_SEQ_UNIT = 1;   // byte for Sparc //##16

//##52  public static final int     // Endian
//##52    BIG_ENDIAN    = 0,
//##52    LITTLE_ENDIAN = 1;
//##52  public static final int
//##52    ENDIAN = BIG_ENDIAN;

  public static final int     // char code representation number
    INT_TYPE_OF_CHAR_IS_INT   = 0,
    INT_TYPE_OF_CHAR_IS_U_INT = 1;

//##52  public static final int     // char code representation //##10
//##52    INT_TYPE_OF_CHAR = INT_TYPE_OF_CHAR_IS_INT;  //##27

  public static final int     // address representation number
    INT_TYPE_OF_ADDRESS_IS_INT         = 0,
    INT_TYPE_OF_ADDRESS_IS_U_INT       = 1,
    INT_TYPE_OF_ADDRESS_IS_LONG        = 2,
    INT_TYPE_OF_ADDRESS_IS_U_LONG      = 3,
    INT_TYPE_OF_ADDRESS_IS_U_LONG_LONG = 4;
//##52  public static final int     // address representation //##10
//##52    INT_TYPE_OF_ADDRESS = INT_TYPE_OF_ADDRESS_IS_U_LONG;

//##52 BEGIN
/**
 * Select one of the constants INT_TYPE_OF_ADDRESS_IS_* (see abobe).
 * @return the selected constant.
 */
  public int    //##52
  getIntKindForAddress()
  {
    return INT_TYPE_OF_ADDRESS_IS_U_LONG;
  }

  /**
   * Select one of the constants INT_TYPE_OF_CHAR_IS_* (see abobe).
   * @return the selected constant.
   */
  public int
  getIntKindForChar()
  {
    return INT_TYPE_OF_CHAR_IS_INT;
  }

public int
getAlignment( int pTypeKind )
{
   switch (pTypeKind) {
     case Type.KIND_BOOL:        return ALIGN_BOOL;
     case Type.KIND_CHAR:        return ALIGN_CHAR;
     case Type.KIND_WCHAR:       return ALIGN_WCHAR;  //##51
     case Type.KIND_SHORT:       return ALIGN_SHORT;
     case Type.KIND_INT:         return ALIGN_INT;
     case Type.KIND_LONG:        return ALIGN_LONG;
     case Type.KIND_FLOAT:       return ALIGN_FLOAT;
     case Type.KIND_DOUBLE:      return ALIGN_DOUBLE;
     case Type.KIND_LONG_DOUBLE: return ALIGN_LONG_DOUBLE;
     case Type.KIND_POINTER:     return ALIGN_ADDRESS;
     case Type.KIND_LONG_LONG:   return ALIGN_LONG_LONG;
     case Type.KIND_U_CHAR:      return ALIGN_CHAR;
     case Type.KIND_U_SHORT:     return ALIGN_SHORT;
     case Type.KIND_U_INT:       return ALIGN_INT;
     case Type.KIND_U_LONG:      return ALIGN_LONG;
     case Type.KIND_U_LONG_LONG: return ALIGN_LONG_LONG;
     case Type.KIND_ADDRESS:     return ALIGN_ADDRESS;
     case Type.KIND_OFFSET:      return ALIGN_OFFSET;
     case Type.KIND_VOID:
     case Type.KIND_STRING:
     case Type.KIND_VECTOR:
     case Type.KIND_STRUCT:
     case Type.KIND_UNION:
                                 return ALIGN_CHAR;  // Return minimum alignment.
     case Type.KIND_ENUM:        return ALIGN_INT;  //##67
     case Type.KIND_REGION:      return ALIGN_INT;  //##67
     case Type.KIND_SUBP:        return ALIGN_SUBP; //##67
     default:
       return ALIGN_INT;
   }
} // getAlignment


  /**
   * Allocation unit for bit field sequence.
   * @return the number of allocation units.
   */
  public int
  //##87 minimumByteForBitFieldSequence()
  minimumNumberOfAddressingUnitsForBitFieldSequence() //##87
  {
    return 1;
  }

  public boolean
  initGlobalExplicitly()
  {
    return false;
  }

  public boolean
  initByDataCode()
  {
    return true;
  }
  //##52 END

//##52  public static final boolean
//##52    INIT_GLOBAL_EXPLICITLY = false;  //##16
         /* true : generate zero-filling instruction for global variable */
         /* false: do not generate zero-filling instruction */
//##52  public static final boolean
//##52    INIT_BY_DATA_CODE = true;  //##20
          /* true : initiate variable by data definition code */
          /* false: initiate variable by assignment statement */

//====== Methods to get machine parameters ======//

/**  evaluateSize
 *  @param pTypeKind: should be a type kind (see Type).
 *  return integer value representing the size of the type
 *      corresponding to pTypeKind in addressing unit.
 *      If it is not evaluable, return SIZEOF_INT.
**/
public int  //##change
evaluateSize( int pTypeKind )
{
        // return typeSizeExp.evaluateAsInt( );
    switch( pTypeKind ){
    case Type.KIND_BOOL:
      return SIZEOF_BOOL;
    case Type.KIND_CHAR:
      return SIZEOF_CHAR;
    case Type.KIND_WCHAR:                //##51
      return SIZEOF_WCHAR;  //##51
    case Type.KIND_SHORT:
      return SIZEOF_SHORT;
    case Type.KIND_INT:
      return SIZEOF_INT;
    case Type.KIND_LONG:
      return SIZEOF_LONG;
    case Type.KIND_FLOAT:
      return SIZEOF_FLOAT;
    case Type.KIND_DOUBLE:
      return SIZEOF_DOUBLE;
    case Type.KIND_LONG_DOUBLE:
      return SIZEOF_LONG_DOUBLE;
    case Type.KIND_POINTER:
      return SIZEOF_ADDRESS;
    case Type.KIND_LONG_LONG:
      return SIZEOF_LONG_LONG;
    case Type.KIND_U_CHAR:
      return SIZEOF_CHAR;
    case Type.KIND_U_SHORT:
      return SIZEOF_SHORT;
    case Type.KIND_U_INT:
      return SIZEOF_INT;
    case Type.KIND_U_LONG:
      return SIZEOF_LONG;
    case Type.KIND_U_LONG_LONG:
      return SIZEOF_LONG_LONG;
    case Type.KIND_ADDRESS:
      return SIZEOF_ADDRESS;
    case Type.KIND_OFFSET:
      return SIZEOF_OFFSET;
    case Type.KIND_VOID:
    case Type.KIND_STRING:
    case Type.KIND_VECTOR:
    case Type.KIND_STRUCT:
    case Type.KIND_UNION:
      return SIZEOF_CHAR;   // Return minimum size. //##38
    case Type.KIND_ENUM:
    case Type.KIND_REGION:  //##29
    case Type.KIND_SUBP:
//    case Type.KIND_CLASS:
//    case Type.KIND_DEFINED:
    default:
      return SIZEOF_INT;
    }
} // evaluateSize

/** getCharCode
 *  Get the character code of the given character.
 *  @param pChar: character to be converted.
 *  @return the character code.
**/
public int //##21
getCharCode( char pChar )
{
  int lCode = Character.digit(pChar, 10);
  if (lCode < 128)
    return lCode;  //## REFINE
  else
    return unicodeToJis(lCode);  //## REFINE
}

private int //##21 REFINE
unicodeToJis( int pUniCode)
{
  return pUniCode;
}

public Type
getStringElemType()
{
  return ioRoot.symRoot.getCharType(); //##27
}

/** balnkRegionName //##29
 *  @return the name to represent blank (unnamed) region.
 */
public String
blankRegionName()
{
  return "_blankRegion".intern();  //##29
}

public int
getLargestAlignment() //##29
{
  return ALIGN_LONG_DOUBLE;
}

/** isBigEndian //##16
 *  @return true if big endian is selected, false otherwise.
**/
public boolean
isBigEndian() { return true; }

/** isLittleEndian: //##16
 *  @return true if little endian is selected, false otherwise.
**/
public boolean
isLittleEndian() { return false; }

/** isPackedFromRight: //##16
 *  @return true if bit fields of a structure are packed from right
 *      (the least significant bit of 1st field is placed at
 *       the least significant bit of object).
**/
public boolean  //##change
isPackedFromRight() { return false; } //##31

/** isPackedFromLeft: //##16
 *  @return true if bit fields of a structure are packed from left
 *      (the most significant bit of 1st field is placed at
 *       the most significant bit of object).
**/
public boolean
isPackedFromLeft() { return true; } //##31

//##57 BEGIN
/** index to access COST_OF_INSTRUCTION
 */
public static final int
  COST_INDEX_TEMP_LOAD   = 1, // loading temporal variable
  COST_INDEX_GLOBAL_LOAD = 2, // loading global variable
  COST_INDEX_CALL        = 3; // subprogram call

/** costOfInstruction
 *  Approximate cost of executing instructions in unit of
 *  register-register integer addition.
 *  The cost of each executable HIR node is assumed at least 1,
 *  so that the cost of (assign <var x> (exp e)) is
 *  assumed to be (1 + (cost of x) + (cost of e)).
 *  This value is used in HIR optimization, etc.
 * @param pIndex is the index to select instruction in such way as
 *    COST_INDEX_TEMP_LOAD, COST_INDEX_CALL, etc.
 * @return the approximate cost of instruction specified by pIndex.
**/
public int
costOfInstruction( int pIndex )
{
  switch (pIndex) {
  case COST_INDEX_TEMP_LOAD:   return 4;
  case COST_INDEX_GLOBAL_LOAD: return 7;
  case COST_INDEX_CALL:        return 10;
  default: return 1;
  }
}
//##57 END

//##64 BEGIN
public int
  getNumberOfGeneralRegisters()
{
  return 32; // Default is Sparc
}

public int
  getNumberOfFloatingRegisters()
{
  return 32; // Default is Sparc
}
//##64 END

} // MachineParam class

