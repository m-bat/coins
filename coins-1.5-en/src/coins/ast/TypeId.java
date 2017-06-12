/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.ast;

/**
 * Type identifiers and storage specifiers.
 * The type identifiers are used for encoding a type.
 * The way of encoding is borrowed from the C++ compiler implementation.
 *
 * For example,
 * <ul><pre>
 * volatile char     =&gt; Vc
 * const signed int  =&gt; CSi
 * char*             =&gt; Pc
 * struct S          =&gt; &lt;S&gt;
 * union U           =&gt; (U)
 * enum E            =&gt; [E]
 * int k[20]         =&gt; A20i
 * int k[3][4]       =&gt; A3A4i
 * int k[]           =&gt; A?i
 * void f(char, int) =&gt; Fci_v
 * </pre></ul>
 *
 * <p>A <code>typedef</code>-ed name is expanded into a composition of
 * primitive types before it is encoded.  The resulting representation
 * of encoding a type
 * never includes <code>typedef</code>-ed names.
 */
public interface TypeId {
    /* types
     */
    static char CONST_T = 'C';
    static char VOLATILE_T = 'V';
    static char RESTRICT_T = 'R';  //##81
    static char SIGNED_T = 'S';
    static char UNSIGNED_T = 'U';

    static char STRUCT_BEGIN = '<';
    static char STRUCT_END = '>';

    static char UNION_BEGIN = '(';
    static char UNION_END = ')';

    static char ENUM_BEGIN = '[';	// equivalent to INT_T
    static char ENUM_END = ']';

    static char POINTER_T = 'P';
    static char ARRAY_T = 'A';
    static char NO_DIMENSION_T = '?';
    static char FUNCTION_T = 'F';
    //static char RETURN_T = '_'; // S.Fukuda 2002.10.20
    static char RETURN_T = '$';   // S.Fukuda 2002.10.20
    // Changed to the character which was not able to be used for the type name.
    static char ELLIPSIS_T = 'e';	// ...

    static char CHAR_T = 'c';
    static char SHORT_T = 's';
    static char INT_T = 'i';
    static char LONG_T = 'l';
    static char LONG_LONG_T = 'j';
    static char FLOAT_T = 'f';
    static char DOUBLE_T = 'd';
    static char LONG_DOUBLE_T = 'r';
    static char VOID_T = 'v';

    static char OFFSET_T = INT_T;	// pointer offset
    static char SIZE_T = INT_T;		// type of sizeof

    /* storage specifiers (including INLINE)
     */

    /**
     * No storage specifier.
     */
    static int S_NONE = 0;	// no specifiers given

    /**
     * <code>static</code> specifier
     */
    static int S_STATIC = 1;

    /**
     * <code>extern</code> specifier
     */
    static int S_EXTERN = 2;

    /**
     * <code>auto</code> specifier
     */
    static int S_AUTO = 4;

    /**
     * <code>inline</code> specifier
     */
    static int S_INLINE = 8;

    /**
     * <code>register</code> specifier
     */
    static int S_REGISTER = 16; //SF041126
}
