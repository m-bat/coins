/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */

package coins.ast;

/**
 * Token identifiers.
 */
public interface TokenId {
    static int EOF = -1;

    static int ASM = 300;
    static int AUTO = 301;
    static int CHAR = 302;
    static int CONST = 303;
    static int DOUBLE = 304;
    static int ENUM = 305;
    static int EXTERN = 306;
    static int FLOAT = 307;
    static int INT = 308;
    static int LONG = 309;
    static int REGISTER = 310;
    static int SHORT = 311;
    static int SIGNED = 312;
    static int STATIC = 313;
    static int STRUCT = 314;
    static int TYPEDEF = 315;
    static int UNION = 316;
    static int UNSIGNED = 317;
    static int VOID = 318;
    static int VOLATILE = 319;
    static int MUTABLE = 320;
    static int BREAK = 321;
    static int CASE = 322;
    static int CONTINUE = 323;
    static int DEFAULT = 324;
    static int DO = 325;
    static int ELSE = 326;
    static int FOR = 327;
    static int GOTO = 328;
    static int IF = 329;
    static int RETURN = 330;
    static int SIZEOF = 331;
    static int SWITCH = 332;
    static int WHILE = 333;

    static int INLINE = 334;	// now it's part of ANSI C.
    static int RESTRICT = 335; //##81

    // If you change the value of NEQ ... ARROW, then
    // check Parser.getOpPrecedence() and Parser.nextIsAssignOp().

    static int MOD_E = 350;	// %=
    static int AND_E = 351;	// &=
    static int MUL_E = 352;	// *=
    static int PLUS_E = 353;	// +=
    static int MINUS_E = 354;	// -=
    static int DIV_E = 355;	// /=
    static int LSHIFT_E = 356;	// <<=
    static int EXOR_E = 357;	// ^=
    static int OR_E = 358;	// |=
    static int RSHIFT_E = 359;	// >>=

    static int NEQ = 360;	// !=
    static int LE = 361;		// <=
    static int GE = 362;		// >=
    static int EQ = 363;		// ==
    static int PLUSPLUS = 364;	// ++
    static int MINUSMINUS = 365;	// --
    static int LSHIFT = 366;	// <<
    static int RSHIFT = 367;	// >>
    static int OROR = 368;	// ||
    static int ANDAND = 369;	// &&
    static int ELLIPSIS = 370;	// ...
    static int ARROW = 371;	// ->

    static int CAST_OP = 380;	// cast operator
    static int FUNCALL = 381;	// function call
    static int COND_OP = 382;	// conditional expression ?:
    static int INDEX_OP = 383;	// array index []

    static int IDENTIFIER = 400;
    static int CHAR_CONST = 401;
    static int INT_CONST = 402;
    static int UINT_CONST = 403;
    static int LONG_CONST = 404;
    static int ULONG_CONST = 405;
    static int FLOAT_CONST = 406;
    static int DOUBLE_CONST = 407;
    static int LONG_DOUBLE_CONST = 407;	// equivalent to DOUBLE_CONST
    static int STRING_L = 408;
    static int TYPEDEF_NAME = 409;
    static int STRING_WL = 410;

    static int LONGLONG_CONST = 411; //SF041020
    static int ULONGLONG_CONST = 412; //SF041020
    static int PRAGMA = 413; //##70

    static int SKIP_GCC_ATTRIBUTE = 498; //##85
    static int SKIP_GCC_ASM = 499; //##70
    static int BAD_TOKEN = 500;
    static int IGNORE = 501;
}
