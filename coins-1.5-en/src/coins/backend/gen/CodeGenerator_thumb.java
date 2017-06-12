/*
Productions:
 1: label -> (LABEL _)
 2: xregb -> (REG I8)
 3: xregb -> (SUBREG I8)
 4: xregh -> (REG I16)
 5: xregh -> (SUBREG I16)
 6: xregsp -> (REG I32)
 7: xregw -> (REG I32)
 8: regfp -> (REG I32)
 9: xregw -> (SUBREG I32)
 10: xregf -> (REG F32)
 11: xregf -> (SUBREG F32)
 12: xregd -> (REG F64)
 13: xregd -> (SUBREG F64)
 14: regb -> xregb
 15: regh -> xregh
 16: regw -> xregw
 17: regf -> xregf
 18: regd -> xregd
 19: regsp -> xregsp
 20: regw -> regfp
 21: regw -> regsp
 22: const_5x4 -> (INTCONST _)
 23: const_5x2 -> (INTCONST _)
 24: const_5 -> (INTCONST _)
 25: const_8x4 -> (INTCONST _)
 26: const_8 -> (INTCONST _)
 27: const_8m -> (INTCONST _)
 28: const_7x4 -> (INTCONST _)
 29: const_7x4m -> (INTCONST _)
 30: const_3 -> (INTCONST _)
 31: shift_5 -> (INTCONST _)
 32: const_0 -> (INTCONST _)
 33: const_int -> (INTCONST _)
 34: const_static -> (STATIC _)
 35: const_any -> (ADD I32 const_static const_int)
 36: const_any -> (ADD I32 const_int const_static)
 37: const_any -> const_int
 38: const_any -> const_static
 39: addrsp -> (ADD I32 regsp const_8x4)
 40: addrsp -> regsp
 41: addrw -> (ADD I32 regw const_5x4)
 42: addrh -> (ADD I32 regw const_5x2)
 43: addrb -> (ADD I32 regw const_5)
 44: addr2 -> (ADD I32 regw regw)
 45: addr -> regw
 46: memw -> (MEM I32 addrsp)
 47: memw -> (MEM I32 addrw)
 48: memw -> (MEM I32 addr)
 49: memw -> (MEM I32 addr2)
 50: memh -> (MEM I16 addrh)
 51: memh -> (MEM I16 addr)
 52: memh -> (MEM I16 addr2)
 53: memh2 -> (MEM I16 addr2)
 54: memb -> (MEM I8 addrb)
 55: memb -> (MEM I8 addr)
 56: memb -> (MEM I8 addr2)
 57: memb2 -> (MEM I8 addr2)
 58: regw -> const_8
 59: regh -> const_8
 60: regb -> const_8
 61: regw -> const_8m
 62: regh -> const_8m
 63: regb -> const_8m
 64: regw -> const_int
 65: regh -> const_int
 66: regb -> const_int
 67: regw -> const_any
 68: regh -> const_any
 69: regb -> const_any
 70: regb -> memb
 71: regh -> memh
 72: regw -> memw
 73: regw -> (CONVZX I32 memb)
 74: regw -> (CONVZX I32 memh)
 75: regh -> (CONVZX I16 memb)
 76: regw -> (CONVSX I32 memb2)
 77: regw -> (CONVSX I32 memh2)
 78: regh -> (CONVSX I16 memb2)
 79: void -> (SET I32 memw regw)
 80: void -> (SET I16 memh regh)
 81: void -> (SET I8 memb regb)
 82: _1 -> (CONVIT I16 regw)
 83: void -> (SET I16 memh _1)
 84: _2 -> (CONVIT I8 regw)
 85: void -> (SET I8 memb _2)
 86: _3 -> (CONVIT I8 regh)
 87: void -> (SET I8 memb _3)
 88: void -> (JUMP _ label)
 89: void -> (SET I32 xregsp regsp)
 90: void -> (SET I32 xregsp regw)
 91: void -> (SET I32 xregw regw)
 92: void -> (SET I32 xregw regsp)
 93: void -> (SET I16 xregh regh)
 94: void -> (SET I8 xregb regb)
 95: regw -> (CONVZX I32 regh)
 96: regw -> (CONVZX I32 regb)
 97: regh -> (CONVZX I16 regb)
 98: regw -> (CONVSX I32 regh)
 99: regw -> (CONVSX I32 regb)
 100: regh -> (CONVSX I16 regb)
 101: regh -> (CONVIT I16 regw)
 102: regb -> (CONVIT I8 regw)
 103: regb -> (CONVIT I8 regh)
 104: regw -> (ADD I32 regw regw)
 105: regw -> (ADD I32 regw const_3)
 106: regw -> (ADD I32 regw const_8)
 107: regw -> (ADD I32 regw regsp)
 108: regw -> (ADD I32 regsp regw)
 109: regw -> (SUB I32 regw regw)
 110: regw -> (SUB I32 regw const_3)
 111: regw -> (SUB I32 regw const_8)
 112: regw -> (SUB I32 regw regsp)
 113: regw -> (SUB I32 regsp regw)
 114: regsp -> (ADD I32 regsp const_7x4)
 115: regsp -> (ADD I32 regsp const_7x4m)
 116: regsp -> (SUB I32 regsp const_7x4)
 117: regsp -> (SUB I32 regsp const_7x4m)
 118: regsp -> (ADD I32 regsp regw)
 119: regsp -> (ADD I32 regw regsp)
 120: regw -> (BAND I32 regw regw)
 121: regw -> (BOR I32 regw regw)
 122: regw -> (BXOR I32 regw regw)
 123: regw -> (MUL I32 regw regw)
 124: regw -> (DIVS I32 regw regw)
 125: regw -> (DIVU I32 regw regw)
 126: regw -> (MODS I32 regw regw)
 127: regw -> (MODU I32 regw regw)
 128: regw -> (LSHS I32 regw regw)
 129: regw -> (RSHS I32 regw regw)
 130: regw -> (RSHU I32 regw regw)
 131: regw -> (LSHS I32 regw const_5)
 132: regw -> (RSHS I32 regw shift_5)
 133: regw -> (RSHU I32 regw shift_5)
 134: regw -> (BNOT I32 regw)
 135: regw -> (NEG I32 regw)
 136: compop -> regw
 137: compop -> const_8
 138: regw -> (TSTEQ I32 regw compop)
 139: _4 -> (TSTEQ I32 regw compop)
 140: void -> (JUMPC _ _4 label label)
 141: _5 -> (TSTNE I32 regw compop)
 142: void -> (JUMPC _ _5 label label)
 143: _6 -> (TSTLTS I32 regw compop)
 144: void -> (JUMPC _ _6 label label)
 145: _7 -> (TSTLES I32 regw compop)
 146: void -> (JUMPC _ _7 label label)
 147: _8 -> (TSTGTS I32 regw compop)
 148: void -> (JUMPC _ _8 label label)
 149: _9 -> (TSTGES I32 regw compop)
 150: void -> (JUMPC _ _9 label label)
 151: _10 -> (TSTLTU I32 regw compop)
 152: void -> (JUMPC _ _10 label label)
 153: _11 -> (TSTLEU I32 regw compop)
 154: void -> (JUMPC _ _11 label label)
 155: _12 -> (TSTGTU I32 regw compop)
 156: void -> (JUMPC _ _12 label label)
 157: _13 -> (TSTGEU I32 regw compop)
 158: void -> (JUMPC _ _13 label label)
 159: _14 -> (BAND I32 regw regw)
 160: _15 -> (TSTEQ I32 _14 const_0)
 161: void -> (JUMPC _ _15 label label)
 162: _16 -> (TSTNE I32 _14 const_0)
 163: void -> (JUMPC _ _16 label label)
 164: _17 -> (TSTLTS I32 _14 const_0)
 165: void -> (JUMPC _ _17 label label)
 166: _18 -> (TSTLES I32 _14 const_0)
 167: void -> (JUMPC _ _18 label label)
 168: _19 -> (TSTGTS I32 _14 const_0)
 169: void -> (JUMPC _ _19 label label)
 170: _20 -> (TSTGES I32 _14 const_0)
 171: void -> (JUMPC _ _20 label label)
 172: _21 -> (TSTLTU I32 _14 const_0)
 173: void -> (JUMPC _ _21 label label)
 174: _22 -> (TSTLEU I32 _14 const_0)
 175: void -> (JUMPC _ _22 label label)
 176: _23 -> (TSTGTU I32 _14 const_0)
 177: void -> (JUMPC _ _23 label label)
 178: _24 -> (TSTGEU I32 _14 const_0)
 179: void -> (JUMPC _ _24 label label)
 180: void -> (CALL _ const_any)
 181: void -> (CALL _ regw)
 182: regd -> (CONVFX F64 regf)
 183: regf -> (CONVFT F32 regd)
 184: void -> (SET F64 xregd regd)
 185: void -> (SET F32 xregf regf)
 186: void -> regf
 187: void -> regd
 188: _25 -> (REG I32 "%sp")
 189: _26 -> (INTCONST I32 4)
 190: _27 -> (SUB I32 _25 _26)
 191: _28 -> (SET I32 _25 _27)
 192: _29 -> (MEM F32 _25)
 193: _30 -> (SET F32 _29 regf)
 194: void -> (PARALLEL _ _28 _30)
 195: _31 -> (INTCONST I32 8)
 196: _32 -> (SUB I32 _25 _31)
 197: _33 -> (SET I32 _25 _32)
 198: _34 -> (MEM F64 _25)
 199: _35 -> (SET F64 _34 regd)
 200: void -> (PARALLEL _ _33 _35)
 201: _36 -> (MEM I32 _25)
 202: _37 -> (SET I32 regw _36)
 203: _38 -> (ADD I32 _25 _26)
 204: _39 -> (SET I32 _25 _38)
 205: void -> (PARALLEL _ _37 _39)
 206: _40 -> (MEM I32 _38)
 207: _41 -> (SET I32 regw _40)
 208: _42 -> (ADD I32 _25 _31)
 209: _43 -> (SET I32 _25 _42)
 210: void -> (PARALLEL _ _37 _41 _43)
 211: void -> (SET F32 memf memf)
 212: _44 -> (MEM F64 base)
 213: void -> (SET F64 _44 _44)
*/
/*
Sorted Productions:
 45: addr -> regw
 136: compop -> regw
 186: void -> regf
 187: void -> regd
 21: regw -> regsp
 40: addrsp -> regsp
 20: regw -> regfp
 14: regb -> xregb
 15: regh -> xregh
 19: regsp -> xregsp
 16: regw -> xregw
 17: regf -> xregf
 18: regd -> xregd
 58: regw -> const_8
 59: regh -> const_8
 60: regb -> const_8
 137: compop -> const_8
 61: regw -> const_8m
 62: regh -> const_8m
 63: regb -> const_8m
 37: const_any -> const_int
 64: regw -> const_int
 65: regh -> const_int
 66: regb -> const_int
 38: const_any -> const_static
 67: regw -> const_any
 68: regh -> const_any
 69: regb -> const_any
 72: regw -> memw
 71: regh -> memh
 70: regb -> memb
 22: const_5x4 -> (INTCONST _)
 23: const_5x2 -> (INTCONST _)
 24: const_5 -> (INTCONST _)
 25: const_8x4 -> (INTCONST _)
 26: const_8 -> (INTCONST _)
 27: const_8m -> (INTCONST _)
 28: const_7x4 -> (INTCONST _)
 29: const_7x4m -> (INTCONST _)
 30: const_3 -> (INTCONST _)
 31: shift_5 -> (INTCONST _)
 32: const_0 -> (INTCONST _)
 33: const_int -> (INTCONST _)
 189: _26 -> (INTCONST I32 4)
 195: _31 -> (INTCONST I32 8)
 34: const_static -> (STATIC _)
 2: xregb -> (REG I8)
 4: xregh -> (REG I16)
 6: xregsp -> (REG I32)
 7: xregw -> (REG I32)
 8: regfp -> (REG I32)
 188: _25 -> (REG I32 "%sp")
 10: xregf -> (REG F32)
 12: xregd -> (REG F64)
 3: xregb -> (SUBREG I8)
 5: xregh -> (SUBREG I16)
 9: xregw -> (SUBREG I32)
 11: xregf -> (SUBREG F32)
 13: xregd -> (SUBREG F64)
 1: label -> (LABEL _)
 135: regw -> (NEG I32 regw)
 35: const_any -> (ADD I32 const_static const_int)
 36: const_any -> (ADD I32 const_int const_static)
 39: addrsp -> (ADD I32 regsp const_8x4)
 41: addrw -> (ADD I32 regw const_5x4)
 42: addrh -> (ADD I32 regw const_5x2)
 43: addrb -> (ADD I32 regw const_5)
 44: addr2 -> (ADD I32 regw regw)
 104: regw -> (ADD I32 regw regw)
 105: regw -> (ADD I32 regw const_3)
 106: regw -> (ADD I32 regw const_8)
 107: regw -> (ADD I32 regw regsp)
 108: regw -> (ADD I32 regsp regw)
 114: regsp -> (ADD I32 regsp const_7x4)
 115: regsp -> (ADD I32 regsp const_7x4m)
 118: regsp -> (ADD I32 regsp regw)
 119: regsp -> (ADD I32 regw regsp)
 203: _38 -> (ADD I32 _25 _26)
 208: _42 -> (ADD I32 _25 _31)
 109: regw -> (SUB I32 regw regw)
 110: regw -> (SUB I32 regw const_3)
 111: regw -> (SUB I32 regw const_8)
 112: regw -> (SUB I32 regw regsp)
 113: regw -> (SUB I32 regsp regw)
 116: regsp -> (SUB I32 regsp const_7x4)
 117: regsp -> (SUB I32 regsp const_7x4m)
 190: _27 -> (SUB I32 _25 _26)
 196: _32 -> (SUB I32 _25 _31)
 123: regw -> (MUL I32 regw regw)
 124: regw -> (DIVS I32 regw regw)
 125: regw -> (DIVU I32 regw regw)
 126: regw -> (MODS I32 regw regw)
 127: regw -> (MODU I32 regw regw)
 78: regh -> (CONVSX I16 memb2)
 100: regh -> (CONVSX I16 regb)
 76: regw -> (CONVSX I32 memb2)
 77: regw -> (CONVSX I32 memh2)
 98: regw -> (CONVSX I32 regh)
 99: regw -> (CONVSX I32 regb)
 75: regh -> (CONVZX I16 memb)
 97: regh -> (CONVZX I16 regb)
 73: regw -> (CONVZX I32 memb)
 74: regw -> (CONVZX I32 memh)
 95: regw -> (CONVZX I32 regh)
 96: regw -> (CONVZX I32 regb)
 84: _2 -> (CONVIT I8 regw)
 86: _3 -> (CONVIT I8 regh)
 102: regb -> (CONVIT I8 regw)
 103: regb -> (CONVIT I8 regh)
 82: _1 -> (CONVIT I16 regw)
 101: regh -> (CONVIT I16 regw)
 182: regd -> (CONVFX F64 regf)
 183: regf -> (CONVFT F32 regd)
 120: regw -> (BAND I32 regw regw)
 159: _14 -> (BAND I32 regw regw)
 121: regw -> (BOR I32 regw regw)
 122: regw -> (BXOR I32 regw regw)
 134: regw -> (BNOT I32 regw)
 128: regw -> (LSHS I32 regw regw)
 131: regw -> (LSHS I32 regw const_5)
 129: regw -> (RSHS I32 regw regw)
 132: regw -> (RSHS I32 regw shift_5)
 130: regw -> (RSHU I32 regw regw)
 133: regw -> (RSHU I32 regw shift_5)
 138: regw -> (TSTEQ I32 regw compop)
 139: _4 -> (TSTEQ I32 regw compop)
 160: _15 -> (TSTEQ I32 _14 const_0)
 141: _5 -> (TSTNE I32 regw compop)
 162: _16 -> (TSTNE I32 _14 const_0)
 143: _6 -> (TSTLTS I32 regw compop)
 164: _17 -> (TSTLTS I32 _14 const_0)
 145: _7 -> (TSTLES I32 regw compop)
 166: _18 -> (TSTLES I32 _14 const_0)
 147: _8 -> (TSTGTS I32 regw compop)
 168: _19 -> (TSTGTS I32 _14 const_0)
 149: _9 -> (TSTGES I32 regw compop)
 170: _20 -> (TSTGES I32 _14 const_0)
 151: _10 -> (TSTLTU I32 regw compop)
 172: _21 -> (TSTLTU I32 _14 const_0)
 153: _11 -> (TSTLEU I32 regw compop)
 174: _22 -> (TSTLEU I32 _14 const_0)
 155: _12 -> (TSTGTU I32 regw compop)
 176: _23 -> (TSTGTU I32 _14 const_0)
 157: _13 -> (TSTGEU I32 regw compop)
 178: _24 -> (TSTGEU I32 _14 const_0)
 54: memb -> (MEM I8 addrb)
 55: memb -> (MEM I8 addr)
 56: memb -> (MEM I8 addr2)
 57: memb2 -> (MEM I8 addr2)
 50: memh -> (MEM I16 addrh)
 51: memh -> (MEM I16 addr)
 52: memh -> (MEM I16 addr2)
 53: memh2 -> (MEM I16 addr2)
 46: memw -> (MEM I32 addrsp)
 47: memw -> (MEM I32 addrw)
 48: memw -> (MEM I32 addr)
 49: memw -> (MEM I32 addr2)
 201: _36 -> (MEM I32 _25)
 206: _40 -> (MEM I32 _38)
 192: _29 -> (MEM F32 _25)
 198: _34 -> (MEM F64 _25)
 212: _44 -> (MEM F64 base)
 81: void -> (SET I8 memb regb)
 85: void -> (SET I8 memb _2)
 87: void -> (SET I8 memb _3)
 94: void -> (SET I8 xregb regb)
 80: void -> (SET I16 memh regh)
 83: void -> (SET I16 memh _1)
 93: void -> (SET I16 xregh regh)
 79: void -> (SET I32 memw regw)
 89: void -> (SET I32 xregsp regsp)
 90: void -> (SET I32 xregsp regw)
 91: void -> (SET I32 xregw regw)
 92: void -> (SET I32 xregw regsp)
 191: _28 -> (SET I32 _25 _27)
 197: _33 -> (SET I32 _25 _32)
 202: _37 -> (SET I32 regw _36)
 204: _39 -> (SET I32 _25 _38)
 207: _41 -> (SET I32 regw _40)
 209: _43 -> (SET I32 _25 _42)
 185: void -> (SET F32 xregf regf)
 193: _30 -> (SET F32 _29 regf)
 211: void -> (SET F32 memf memf)
 184: void -> (SET F64 xregd regd)
 199: _35 -> (SET F64 _34 regd)
 213: void -> (SET F64 _44 _44)
 88: void -> (JUMP _ label)
 140: void -> (JUMPC _ _4 label label)
 142: void -> (JUMPC _ _5 label label)
 144: void -> (JUMPC _ _6 label label)
 146: void -> (JUMPC _ _7 label label)
 148: void -> (JUMPC _ _8 label label)
 150: void -> (JUMPC _ _9 label label)
 152: void -> (JUMPC _ _10 label label)
 154: void -> (JUMPC _ _11 label label)
 156: void -> (JUMPC _ _12 label label)
 158: void -> (JUMPC _ _13 label label)
 161: void -> (JUMPC _ _15 label label)
 163: void -> (JUMPC _ _16 label label)
 165: void -> (JUMPC _ _17 label label)
 167: void -> (JUMPC _ _18 label label)
 169: void -> (JUMPC _ _19 label label)
 171: void -> (JUMPC _ _20 label label)
 173: void -> (JUMPC _ _21 label label)
 175: void -> (JUMPC _ _22 label label)
 177: void -> (JUMPC _ _23 label label)
 179: void -> (JUMPC _ _24 label label)
 180: void -> (CALL _ const_any)
 181: void -> (CALL _ regw)
 194: void -> (PARALLEL _ _28 _30)
 200: void -> (PARALLEL _ _33 _35)
 205: void -> (PARALLEL _ _37 _39)
 210: void -> (PARALLEL _ _37 _41 _43)
*/
/*
Productions:
 1: _rewr -> (CONVUF _ _)
 2: _rewr -> (CONVFU _ _)
 3: _rewr -> (CONVUF _ _)
 4: _rewr -> (CONVFU _ _)
 5: _1 -> (STATIC I32 "__builtin_va_start")
 6: _2 -> (LIST _ _)
 7: _rewr -> (CALL _ _1 _2 _2)
 8: _3 -> (STATIC I32 "__builtin_alloca")
 9: _rewr -> (CALL _ _3 _2 _2)
 10: _rewr -> (PROLOGUE _)
 11: _rewr -> (EPILOGUE _)
 12: _rewr -> (CALL _)
 13: _rewr -> (JUMPN _)
 14: _rewr -> (SET _)
*/
/*
Sorted Productions:
 5: _1 -> (STATIC I32 "__builtin_va_start")
 8: _3 -> (STATIC I32 "__builtin_alloca")
 2: _rewr -> (CONVFU _ _)
 4: _rewr -> (CONVFU _ _)
 1: _rewr -> (CONVUF _ _)
 3: _rewr -> (CONVUF _ _)
 14: _rewr -> (SET _)
 13: _rewr -> (JUMPN _)
 7: _rewr -> (CALL _ _1 _2 _2)
 9: _rewr -> (CALL _ _3 _2 _2)
 12: _rewr -> (CALL _)
 10: _rewr -> (PROLOGUE _)
 11: _rewr -> (EPILOGUE _)
 6: _2 -> (LIST _ _)
*/
/* ----------------------------------------------------------
%   Copyright (C) 2004 The Coins Project Group               
%       (Read COPYING for detailed information.)             
----------------------------------------------------------- */
package coins.backend.gen;

import coins.backend.CantHappenException;
import coins.backend.Function;
import coins.backend.Module;
import coins.backend.Op;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.lir.LirFactory;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.SymAuto;
import coins.backend.sym.SymStatic;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import coins.backend.util.Misc;
import java.io.PrintWriter;

// imports here


import coins.backend.CantHappenException;
import coins.backend.Function;
import coins.backend.Module;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.SyntaxError;
import coins.backend.Type;
import coins.backend.lir.LirFconst;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.Label;
import coins.backend.sym.SymAuto;
import coins.backend.sym.SymStatic;
import coins.backend.sym.Symbol;
import coins.backend.util.BiLink;
import coins.backend.util.BiList;
import coins.backend.util.ImList;
import java.io.PrintWriter;

import java.io.OutputStream;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;	
import coins.backend.lir.LirLabelRef;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import coins.backend.ana.SaveRegisters;
import coins.backend.util.NumberSet;

import coins.backend.LocalTransformer;
import coins.backend.Transformer;
import coins.backend.Data;

import coins.backend.asmpp.LiteralAndBranchProcessor;
import coins.backend.asmpp.CPU;




public class CodeGenerator_thumb extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_thumb() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 5;
    static final int NRULES = 14 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;
    static final int NT__1 = 2;
    static final int NT__2 = 3;
    static final int NT__3 = 4;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      default: return null;
      }
    };

    final int[] rule = new int[NNONTERM];

    boolean rewritten;

    void record(int nt, int rule) {
      if (this.rule[nt] == 0) {
        this.rule[nt] = rule;
        switch (nt) {
        }
      }
    }

    LirNode labelAndRewrite(LirNode t, RewrState kids[], String phase,
                            BiList pre, BiList post) {
      switch (t.opCode) {
      case Op.STATIC:
        if (t.type == 514) {
          if (((LirSymRef)t).symbol.name == "__builtin_va_start") record(NT__1, 5);
          if (((LirSymRef)t).symbol.name == "__builtin_alloca") record(NT__3, 8);
        }
        break;
      case Op.CONVFU:
        if (phase == "early")  {
          rewritten = true;
          return rewriteCONVFU(t, pre);
        }
        if (phase == "early")  {
          rewritten = true;
          return rewriteCONVFU(t, pre);
        }
        break;
      case Op.CONVUF:
        if (phase == "early")  {
          rewritten = true;
          return rewriteCONVUF(t, pre);
        }
        if (phase == "early")  {
          rewritten = true;
          return rewriteCONVUF(t, pre);
        }
        break;
      case Op.SET:
        if (phase == "late") if (Type.tag(t.type) == Type.AGGREGATE)  {
          rewritten = true;
          return rewriteAggregateCopy(t, pre);
        }
        break;
      case Op.JUMPN:
        if (phase == "late")  {
          rewritten = true;
          return rewriteJumpn(t, pre);
        }
        break;
      case Op.CALL:
        if (kids[0].rule[NT__1] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%r7")), lir.iconst(514, makeVaStart(t.kid(1).kid(0)))));
        }
        if (kids[0].rule[NT__3] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          setAllocaUsed();
          {
          pre.add(lir.node(Op.SET, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.NEG, 514, lir.node(Op.LSHS, 514, lir.node(Op.RSHU, 514, lir.node(Op.ADD, 514, t.kid(1).kid(0), lir.iconst(514, 3)), lir.iconst(514, 2)), lir.iconst(514, 2))))));
          }
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.REG, 514, func.getSymbol("%sp")));
        }
        if (phase == "late")  {
          rewritten = true;
          return rewriteCall(t, pre, post);
        }
        break;
      case Op.PROLOGUE:
        if (phase == "late")  {
          rewritten = true;
          return noRescan(rewritePrologue(t, post));
        }
        break;
      case Op.EPILOGUE:
        if (phase == "late")  {
          rewritten = true;
          return noRescan(rewriteEpilogue(t, pre));
        }
        break;
      case Op.LIST:
        if (kids.length == 1) record(NT__2, 6);
        break;
      }
      return null;
    }

    public String toString() {
      StringBuffer buf = new StringBuffer();

      buf.append("State(");
      boolean comma = false;
      for (int i = 0; i < NNONTERM; i++) {
        if (rule[i] != 0) {
          if (comma) buf.append(",");
          buf.append(nontermName(i));
          comma = true;
        }
      }
      buf.append(")");

      return buf.toString();
    }

  }


  void initRewriteLabeling() {
    rewrStates = new RewrState[0];
  }

  /** set rewrStates. **/
  private void setRewrStates(int index, RewrState v) {
    if (index >= rewrStates.length) {
      RewrState[] w = new RewrState[Misc.clp2(index + 1)];
      for (int i = 0; i < rewrStates.length; i++)
        w[i] = rewrStates[i];
      rewrStates = w;
    }
    rewrStates[index] = v;
  }

  /** Return RewrState array. **/
  private RewrState getRewrStates(int index) {
    if (index < rewrStates.length)
      return rewrStates[index];
    else
      return null;
  }

  /** Rewrite L-expression. **/
  LirNode rewriteTree(LirNode tree, String phase, BiList pre, BiList post) {
    RewrState s = getRewrStates(tree.id);
    if (s != null && !s.rewritten)
      return tree;

    for (;;) {
      int n = nActualOperands(tree);
      RewrState[] kidst = new RewrState[n];
      for (int i = 0; i < n; i++) {
        LirNode r = rewriteTree(tree.kid(i), phase, pre, post);
        if (r != tree.kid(i))
          tree.setKid(i, r);
        kidst[i] = getRewrStates(tree.kid(i).id);
      }

      s = new RewrState();
      setRewrStates(tree.id, s);

      // rescanning disabled?
      if (disableRewrite.contains(tree.id))
        return tree;

      LirNode newTree = s.labelAndRewrite(tree, kidst, phase, pre, post);
      if (newTree == null)
        return tree;
      tree = newTree;

      if (false) {
        debOut.println("rewrite to: "
                       + (disableRewrite.contains(tree.id) ? "!" : "")
                       + tree);
      }
    }
  }



  /** State label for instruction selection engine. **/
  class State {
    static final int NNONTERM = 88;
    static final int NRULES = 213 + 1;
    static final int START_NT = 8;

    static final int NT__ = 0;
    static final int NT_regw = 1;
    static final int NT_regh = 2;
    static final int NT_regb = 3;
    static final int NT_regf = 4;
    static final int NT_regd = 5;
    static final int NT_regsp = 6;
    static final int NT_regfp = 7;
    static final int NT_void = 8;
    static final int NT_label = 9;
    static final int NT_xregb = 10;
    static final int NT_xregh = 11;
    static final int NT_xregsp = 12;
    static final int NT_xregw = 13;
    static final int NT_xregf = 14;
    static final int NT_xregd = 15;
    static final int NT_const_5x4 = 16;
    static final int NT_const_5x2 = 17;
    static final int NT_const_5 = 18;
    static final int NT_const_8x4 = 19;
    static final int NT_const_8 = 20;
    static final int NT_const_8m = 21;
    static final int NT_const_7x4 = 22;
    static final int NT_const_7x4m = 23;
    static final int NT_const_3 = 24;
    static final int NT_shift_5 = 25;
    static final int NT_const_0 = 26;
    static final int NT_const_int = 27;
    static final int NT_const_static = 28;
    static final int NT_const_any = 29;
    static final int NT_addrsp = 30;
    static final int NT_addrw = 31;
    static final int NT_addrh = 32;
    static final int NT_addrb = 33;
    static final int NT_addr2 = 34;
    static final int NT_addr = 35;
    static final int NT_memw = 36;
    static final int NT_memh = 37;
    static final int NT_memh2 = 38;
    static final int NT_memb = 39;
    static final int NT_memb2 = 40;
    static final int NT__1 = 41;
    static final int NT__2 = 42;
    static final int NT__3 = 43;
    static final int NT_compop = 44;
    static final int NT__4 = 45;
    static final int NT__5 = 46;
    static final int NT__6 = 47;
    static final int NT__7 = 48;
    static final int NT__8 = 49;
    static final int NT__9 = 50;
    static final int NT__10 = 51;
    static final int NT__11 = 52;
    static final int NT__12 = 53;
    static final int NT__13 = 54;
    static final int NT__14 = 55;
    static final int NT__15 = 56;
    static final int NT__16 = 57;
    static final int NT__17 = 58;
    static final int NT__18 = 59;
    static final int NT__19 = 60;
    static final int NT__20 = 61;
    static final int NT__21 = 62;
    static final int NT__22 = 63;
    static final int NT__23 = 64;
    static final int NT__24 = 65;
    static final int NT__25 = 66;
    static final int NT__26 = 67;
    static final int NT__27 = 68;
    static final int NT__28 = 69;
    static final int NT__29 = 70;
    static final int NT__30 = 71;
    static final int NT__31 = 72;
    static final int NT__32 = 73;
    static final int NT__33 = 74;
    static final int NT__34 = 75;
    static final int NT__35 = 76;
    static final int NT__36 = 77;
    static final int NT__37 = 78;
    static final int NT__38 = 79;
    static final int NT__39 = 80;
    static final int NT__40 = 81;
    static final int NT__41 = 82;
    static final int NT__42 = 83;
    static final int NT__43 = 84;
    static final int NT_memf = 85;
    static final int NT_base = 86;
    static final int NT__44 = 87;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regw: return "regw";
      case NT_regh: return "regh";
      case NT_regb: return "regb";
      case NT_regf: return "regf";
      case NT_regd: return "regd";
      case NT_regsp: return "regsp";
      case NT_regfp: return "regfp";
      case NT_void: return "void";
      case NT_label: return "label";
      case NT_xregb: return "xregb";
      case NT_xregh: return "xregh";
      case NT_xregsp: return "xregsp";
      case NT_xregw: return "xregw";
      case NT_xregf: return "xregf";
      case NT_xregd: return "xregd";
      case NT_const_5x4: return "const_5x4";
      case NT_const_5x2: return "const_5x2";
      case NT_const_5: return "const_5";
      case NT_const_8x4: return "const_8x4";
      case NT_const_8: return "const_8";
      case NT_const_8m: return "const_8m";
      case NT_const_7x4: return "const_7x4";
      case NT_const_7x4m: return "const_7x4m";
      case NT_const_3: return "const_3";
      case NT_shift_5: return "shift_5";
      case NT_const_0: return "const_0";
      case NT_const_int: return "const_int";
      case NT_const_static: return "const_static";
      case NT_const_any: return "const_any";
      case NT_addrsp: return "addrsp";
      case NT_addrw: return "addrw";
      case NT_addrh: return "addrh";
      case NT_addrb: return "addrb";
      case NT_addr2: return "addr2";
      case NT_addr: return "addr";
      case NT_memw: return "memw";
      case NT_memh: return "memh";
      case NT_memh2: return "memh2";
      case NT_memb: return "memb";
      case NT_memb2: return "memb2";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT_compop: return "compop";
      case NT__4: return "_4";
      case NT__5: return "_5";
      case NT__6: return "_6";
      case NT__7: return "_7";
      case NT__8: return "_8";
      case NT__9: return "_9";
      case NT__10: return "_10";
      case NT__11: return "_11";
      case NT__12: return "_12";
      case NT__13: return "_13";
      case NT__14: return "_14";
      case NT__15: return "_15";
      case NT__16: return "_16";
      case NT__17: return "_17";
      case NT__18: return "_18";
      case NT__19: return "_19";
      case NT__20: return "_20";
      case NT__21: return "_21";
      case NT__22: return "_22";
      case NT__23: return "_23";
      case NT__24: return "_24";
      case NT__25: return "_25";
      case NT__26: return "_26";
      case NT__27: return "_27";
      case NT__28: return "_28";
      case NT__29: return "_29";
      case NT__30: return "_30";
      case NT__31: return "_31";
      case NT__32: return "_32";
      case NT__33: return "_33";
      case NT__34: return "_34";
      case NT__35: return "_35";
      case NT__36: return "_36";
      case NT__37: return "_37";
      case NT__38: return "_38";
      case NT__39: return "_39";
      case NT__40: return "_40";
      case NT__41: return "_41";
      case NT__42: return "_42";
      case NT__43: return "_43";
      case NT_memf: return "memf";
      case NT_base: return "base";
      case NT__44: return "_44";
      default: return null;
      }
    };

    final int[] rule = new int[NNONTERM];
    final int[] cost1 = new int[NNONTERM];
    final int[] cost2 = new int[NNONTERM];

    void record(int nt, int cost1, int cost2, int rule) {
      if (this.rule[nt] == 0
          || (optSpeed ?
              (cost1 < this.cost1[nt]
               || cost1 == this.cost1[nt] && cost2 < this.cost2[nt])
              : (cost2 < this.cost2[nt]
                 || cost2 == this.cost2[nt] && cost1 < this.cost1[nt]))) {
        this.rule[nt] = rule;
        this.cost1[nt] = cost1;
        this.cost2[nt] = cost2;
        switch (nt) {
        case NT_regw:
          record(NT_addr, 0 + cost1, 0 + cost2, 45);
          record(NT_compop, 0 + cost1, 0 + cost2, 136);
          break;
        case NT_regf:
          record(NT_void, 0 + cost1, 0 + cost2, 186);
          break;
        case NT_regd:
          record(NT_void, 0 + cost1, 0 + cost2, 187);
          break;
        case NT_regsp:
          record(NT_regw, 1 + cost1, 1 + cost2, 21);
          record(NT_addrsp, 0 + cost1, 0 + cost2, 40);
          break;
        case NT_regfp:
          record(NT_regw, 0 + cost1, 0 + cost2, 20);
          break;
        case NT_xregb:
          record(NT_regb, 0 + cost1, 0 + cost2, 14);
          break;
        case NT_xregh:
          record(NT_regh, 0 + cost1, 0 + cost2, 15);
          break;
        case NT_xregsp:
          record(NT_regsp, 0 + cost1, 0 + cost2, 19);
          break;
        case NT_xregw:
          record(NT_regw, 0 + cost1, 0 + cost2, 16);
          break;
        case NT_xregf:
          record(NT_regf, 0 + cost1, 0 + cost2, 17);
          break;
        case NT_xregd:
          record(NT_regd, 0 + cost1, 0 + cost2, 18);
          break;
        case NT_const_8:
          record(NT_regw, 1 + cost1, 1 + cost2, 58);
          record(NT_regh, 1 + cost1, 1 + cost2, 59);
          record(NT_regb, 1 + cost1, 1 + cost2, 60);
          record(NT_compop, 0 + cost1, 0 + cost2, 137);
          break;
        case NT_const_8m:
          record(NT_regw, 2 + cost1, 2 + cost2, 61);
          record(NT_regh, 2 + cost1, 2 + cost2, 62);
          record(NT_regb, 2 + cost1, 2 + cost2, 63);
          break;
        case NT_const_int:
          record(NT_const_any, 0 + cost1, 0 + cost2, 37);
          record(NT_regw, 2 + cost1, 2 + cost2, 64);
          record(NT_regh, 2 + cost1, 2 + cost2, 65);
          record(NT_regb, 2 + cost1, 2 + cost2, 66);
          break;
        case NT_const_static:
          record(NT_const_any, 0 + cost1, 0 + cost2, 38);
          break;
        case NT_const_any:
          record(NT_regw, 3 + cost1, 3 + cost2, 67);
          record(NT_regh, 3 + cost1, 3 + cost2, 68);
          record(NT_regb, 3 + cost1, 3 + cost2, 69);
          break;
        case NT_memw:
          record(NT_regw, 3 + cost1, 3 + cost2, 72);
          break;
        case NT_memh:
          record(NT_regh, 3 + cost1, 3 + cost2, 71);
          break;
        case NT_memb:
          record(NT_regb, 3 + cost1, 3 + cost2, 70);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0x1F * 4 && ((LirIconst)t).signedValue() % 4 == 0) record(NT_const_5x4, 0, 0, 22);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0x1F * 2 && ((LirIconst)t).signedValue() % 2 == 0) record(NT_const_5x2, 0, 0, 23);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0x1F) record(NT_const_5, 0, 0, 24);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0xFF * 4 && ((LirIconst)t).signedValue() % 4 == 0) record(NT_const_8x4, 0, 0, 25);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0xFF) record(NT_const_8, 0, 0, 26);
        if (-255 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() < 0) record(NT_const_8m, 0, 0, 27);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0x7F * 4 && ((LirIconst)t).signedValue() % 4 == 0) record(NT_const_7x4, 0, 0, 28);
        if (0 < -((LirIconst)t).signedValue() && -((LirIconst)t).signedValue() <= 0x7F * 4 && -((LirIconst)t).signedValue() % 4 == 0) record(NT_const_7x4m, 0, 0, 29);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0x7) record(NT_const_3, 0, 0, 30);
        if (0 < ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 0x1F + 1) record(NT_shift_5, 0, 0, 31);
        if (((LirIconst)t).signedValue() == 0) record(NT_const_0, 0, 0, 32);
        record(NT_const_int, 0, 0, 33);
        if (t.type == 514) {
          if (((LirIconst)t).value == 4) record(NT__26, 0, 0, 189);
          if (((LirIconst)t).value == 8) record(NT__31, 0, 0, 195);
        }
        break;
      case Op.STATIC:
        record(NT_const_static, 0, 0, 34);
        break;
      case Op.REG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 2);
        }
        if (t.type == 258) {
          record(NT_xregh, 0, 0, 4);
        }
        if (t.type == 514) {
          if (isSp(t)) record(NT_xregsp, 0, 0, 6);
          if (!isSp(t) && !isFp(t)) record(NT_xregw, 0, 0, 7);
          if (isFp(t)) record(NT_regfp, 0, 0, 8);
          if (((LirSymRef)t).symbol.name == "%sp") record(NT__25, 0, 0, 188);
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 10);
        }
        if (t.type == 1028) {
          record(NT_xregd, 0, 0, 12);
        }
        break;
      case Op.SUBREG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 3);
        }
        if (t.type == 258) {
          record(NT_xregh, 0, 0, 5);
        }
        if (t.type == 514) {
          record(NT_xregw, 0, 0, 9);
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 11);
        }
        if (t.type == 1028) {
          record(NT_xregd, 0, 0, 13);
        }
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 1);
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 135);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_const_static] != 0) if (kids[1].rule[NT_const_int] != 0) record(NT_const_any, 0 + kids[0].cost1[NT_const_static] + kids[1].cost1[NT_const_int], 0 + kids[0].cost2[NT_const_static] + kids[1].cost2[NT_const_int], 35);
          if (kids[0].rule[NT_const_int] != 0) if (kids[1].rule[NT_const_static] != 0) record(NT_const_any, 0 + kids[0].cost1[NT_const_int] + kids[1].cost1[NT_const_static], 0 + kids[0].cost2[NT_const_int] + kids[1].cost2[NT_const_static], 36);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_const_8x4] != 0) record(NT_addrsp, 0 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_const_8x4], 0 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_const_8x4], 39);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_5x4] != 0) record(NT_addrw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_5x4], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_5x4], 41);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_5x2] != 0) record(NT_addrh, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_5x2], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_5x2], 42);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_5] != 0) record(NT_addrb, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_5], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_5], 43);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 44);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 104);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_3] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_3], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_3], 105);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_8] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_8], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_8], 106);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regsp] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regsp], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regsp], 107);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_regw], 108);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_const_7x4] != 0) record(NT_regsp, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_const_7x4], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_const_7x4], 114);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_const_7x4m] != 0) record(NT_regsp, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_const_7x4m], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_const_7x4m], 115);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regsp, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_regw], 118);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regsp] != 0) record(NT_regsp, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regsp], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regsp], 119);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__26] != 0) record(NT__38, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__26], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__26], 203);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__31] != 0) record(NT__42, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__31], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__31], 208);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 109);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_3] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_3], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_3], 110);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_8] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_8], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_8], 111);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regsp] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regsp], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regsp], 112);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_regw], 113);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_const_7x4] != 0) record(NT_regsp, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_const_7x4], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_const_7x4], 116);
          if (kids[0].rule[NT_regsp] != 0) if (kids[1].rule[NT_const_7x4m] != 0) record(NT_regsp, 1 + kids[0].cost1[NT_regsp] + kids[1].cost1[NT_const_7x4m], 1 + kids[0].cost2[NT_regsp] + kids[1].cost2[NT_const_7x4m], 117);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__26] != 0) record(NT__27, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__26], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__26], 190);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__31] != 0) record(NT__32, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__31], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__31], 196);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 123);
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 124);
        }
        break;
      case Op.DIVU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 125);
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 126);
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 127);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_memb2] != 0) record(NT_regh, 1 + kids[0].cost1[NT_memb2], 1 + kids[0].cost2[NT_memb2], 78);
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 100);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_memb2] != 0) record(NT_regw, 1 + kids[0].cost1[NT_memb2], 1 + kids[0].cost2[NT_memb2], 76);
          if (kids[0].rule[NT_memh2] != 0) record(NT_regw, 1 + kids[0].cost1[NT_memh2], 1 + kids[0].cost2[NT_memh2], 77);
          if (kids[0].rule[NT_regh] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 98);
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 99);
        }
        break;
      case Op.CONVZX:
        if (t.type == 258) {
          if (kids[0].rule[NT_memb] != 0) record(NT_regh, 1 + kids[0].cost1[NT_memb], 1 + kids[0].cost2[NT_memb], 75);
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 97);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_memb] != 0) record(NT_regw, 1 + kids[0].cost1[NT_memb], 1 + kids[0].cost2[NT_memb], 73);
          if (kids[0].rule[NT_memh] != 0) record(NT_regw, 1 + kids[0].cost1[NT_memh], 1 + kids[0].cost2[NT_memh], 74);
          if (kids[0].rule[NT_regh] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 95);
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 96);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_regw] != 0) record(NT__2, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 84);
          if (kids[0].rule[NT_regh] != 0) record(NT__3, 0 + kids[0].cost1[NT_regh], 0 + kids[0].cost2[NT_regh], 86);
          if (kids[0].rule[NT_regw] != 0) record(NT_regb, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 102);
          if (kids[0].rule[NT_regh] != 0) record(NT_regb, 0 + kids[0].cost1[NT_regh], 0 + kids[0].cost2[NT_regh], 103);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regw] != 0) record(NT__1, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 82);
          if (kids[0].rule[NT_regw] != 0) record(NT_regh, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 101);
        }
        break;
      case Op.CONVFX:
        if (t.type == 1028) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 182);
        }
        break;
      case Op.CONVFT:
        if (t.type == 516) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 183);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 120);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__14, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 159);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 121);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 122);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 134);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 128);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_const_5] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_const_5], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_const_5], 131);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 129);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_shift_5] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_shift_5], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_shift_5], 132);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 130);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_shift_5] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_shift_5], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_shift_5], 133);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 138);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__4, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 139);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__15, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 160);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__5, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 141);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__16, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 162);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__6, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 143);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__17, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 164);
        }
        break;
      case Op.TSTLES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__7, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 145);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__18, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 166);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__8, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 147);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__19, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 168);
        }
        break;
      case Op.TSTGES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__9, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 149);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__20, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 170);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__10, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 151);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__21, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 172);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__11, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 153);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__22, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 174);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__12, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 155);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__23, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 176);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_compop] != 0) record(NT__13, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_compop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_compop], 157);
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_const_0] != 0) record(NT__24, 0 + kids[0].cost1[NT__14] + kids[1].cost1[NT_const_0], 0 + kids[0].cost2[NT__14] + kids[1].cost2[NT_const_0], 178);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addrb] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addrb], 0 + kids[0].cost2[NT_addrb], 54);
          if (kids[0].rule[NT_addr] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 55);
          if (kids[0].rule[NT_addr2] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 56);
          if (kids[0].rule[NT_addr2] != 0) record(NT_memb2, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 57);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addrh] != 0) record(NT_memh, 0 + kids[0].cost1[NT_addrh], 0 + kids[0].cost2[NT_addrh], 50);
          if (kids[0].rule[NT_addr] != 0) record(NT_memh, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 51);
          if (kids[0].rule[NT_addr2] != 0) record(NT_memh, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 52);
          if (kids[0].rule[NT_addr2] != 0) record(NT_memh2, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 53);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addrsp] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addrsp], 0 + kids[0].cost2[NT_addrsp], 46);
          if (kids[0].rule[NT_addrw] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addrw], 0 + kids[0].cost2[NT_addrw], 47);
          if (kids[0].rule[NT_addr] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 48);
          if (kids[0].rule[NT_addr2] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 49);
          if (kids[0].rule[NT__25] != 0) record(NT__36, 0 + kids[0].cost1[NT__25], 0 + kids[0].cost2[NT__25], 201);
          if (kids[0].rule[NT__38] != 0) record(NT__40, 0 + kids[0].cost1[NT__38], 0 + kids[0].cost2[NT__38], 206);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT__25] != 0) record(NT__29, 0 + kids[0].cost1[NT__25], 0 + kids[0].cost2[NT__25], 192);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT__25] != 0) record(NT__34, 0 + kids[0].cost1[NT__25], 0 + kids[0].cost2[NT__25], 198);
          if (kids[0].rule[NT_base] != 0) record(NT__44, 0 + kids[0].cost1[NT_base], 0 + kids[0].cost2[NT_base], 212);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_regb], 81);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT__2] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT__2], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT__2], 85);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT__3] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT__3], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT__3], 87);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 94);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_memh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_memh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_memh] + kids[1].cost2[NT_regh], 80);
          if (kids[0].rule[NT_memh] != 0) if (kids[1].rule[NT__1] != 0) record(NT_void, 1 + kids[0].cost1[NT_memh] + kids[1].cost1[NT__1], 1 + kids[0].cost2[NT_memh] + kids[1].cost2[NT__1], 83);
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_regh], 93);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_memw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_memw] + kids[1].cost2[NT_regw], 79);
          if (kids[0].rule[NT_xregsp] != 0) if (kids[1].rule[NT_regsp] != 0) record(NT_void, 0 + kids[0].cost1[NT_xregsp] + kids[1].cost1[NT_regsp], 0 + kids[0].cost2[NT_xregsp] + kids[1].cost2[NT_regsp], 89);
          if (kids[0].rule[NT_xregsp] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregsp] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregsp] + kids[1].cost2[NT_regw], 90);
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regw], 91);
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regsp] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regsp], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regsp], 92);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__27] != 0) record(NT__28, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__27], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__27], 191);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__32] != 0) record(NT__33, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__32], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__32], 197);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__36] != 0) record(NT__37, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__36], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__36], 202);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__38] != 0) record(NT__39, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__38], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__38], 204);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__40] != 0) record(NT__41, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__40], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__40], 207);
          if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__42] != 0) record(NT__43, 0 + kids[0].cost1[NT__25] + kids[1].cost1[NT__42], 0 + kids[0].cost2[NT__25] + kids[1].cost2[NT__42], 209);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 185);
          if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__30, 0 + kids[0].cost1[NT__29] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT__29] + kids[1].cost2[NT_regf], 193);
          if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_memf] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_memf], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_memf], 211);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 184);
          if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__35, 0 + kids[0].cost1[NT__34] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT__34] + kids[1].cost2[NT_regd], 199);
          if (kids[0].rule[NT__44] != 0) if (kids[1].rule[NT__44] != 0) record(NT_void, 1 + kids[0].cost1[NT__44] + kids[1].cost1[NT__44], 1 + kids[0].cost2[NT__44] + kids[1].cost2[NT__44], 213);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) record(NT_void, 1 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 88);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__4] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__4] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__4] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 140);
        if (kids[0].rule[NT__5] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__5] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__5] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 142);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__6] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__6] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 144);
        if (kids[0].rule[NT__7] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__7] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__7] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 146);
        if (kids[0].rule[NT__8] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__8] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__8] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 148);
        if (kids[0].rule[NT__9] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__9] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__9] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 150);
        if (kids[0].rule[NT__10] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__10] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__10] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 152);
        if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__11] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__11] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 154);
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 156);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 158);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 161);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 163);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 165);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 167);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 169);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 171);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 173);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 175);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 177);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 179);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_const_any] != 0) record(NT_void, 2 + kids[0].cost1[NT_const_any], 2 + kids[0].cost2[NT_const_any], 180);
        if (kids[0].rule[NT_regw] != 0) record(NT_void, 5 + kids[0].cost1[NT_regw], 5 + kids[0].cost2[NT_regw], 181);
        break;
      case Op.PARALLEL:
        if (kids.length == 2) if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT__30] != 0) record(NT_void, 2 + kids[0].cost1[NT__28] + kids[1].cost1[NT__30], 2 + kids[0].cost2[NT__28] + kids[1].cost2[NT__30], 194);
        if (kids.length == 2) if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT__35] != 0) record(NT_void, 2 + kids[0].cost1[NT__33] + kids[1].cost1[NT__35], 2 + kids[0].cost2[NT__33] + kids[1].cost2[NT__35], 200);
        if (kids.length == 2) if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT__39] != 0) record(NT_void, 6 + kids[0].cost1[NT__37] + kids[1].cost1[NT__39], 6 + kids[0].cost2[NT__37] + kids[1].cost2[NT__39], 205);
        if (kids.length == 3) if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT__41] != 0) if (kids[2].rule[NT__43] != 0) record(NT_void, 6 + kids[0].cost1[NT__37] + kids[1].cost1[NT__41] + kids[2].cost1[NT__43], 6 + kids[0].cost2[NT__37] + kids[1].cost2[NT__41] + kids[2].cost2[NT__43], 210);
        break;
      }
    }

    public String toString() {
      StringBuffer buf = new StringBuffer();

      buf.append("State(");
      boolean comma = false;
      for (int i = 0; i < NNONTERM; i++) {
        if (rule[i] != 0) {
          if (comma) buf.append(",");
          buf.append(nontermName(i) + ":" + rule[i] + "[" + cost1[i] + "." + cost2[i] + "]");
          comma = true;
        }
      }
      buf.append(")");

      return buf.toString();
    }

    // State methods here
    
  }


  private static final Rule[] rulev = new Rule[State.NRULES];

  static {
    rrinit0();
    rrinit100();
    rrinit200();
  }
  static private void rrinit0() {
    rulev[45] = new Rule(45, true, false, 35, "45: addr -> regw", null, ImList.list(ImList.list("ind_0","$1")), null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[136] = new Rule(136, true, false, 44, "136: compop -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[186] = new Rule(186, true, false, 8, "186: void -> regf", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-F32*"});
    rulev[187] = new Rule(187, true, false, 8, "187: void -> regd", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-F64*"});
    rulev[21] = new Rule(21, true, false, 1, "21: regw -> regsp", ImList.list(ImList.list("mov","$0","$1")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-SP*"});
    rulev[40] = new Rule(40, true, false, 30, "40: addrsp -> regsp", null, ImList.list(ImList.list("ind_0","$1")), null, 0, false, false, new int[]{6}, new String[]{null, "*reg-SP*"});
    rulev[20] = new Rule(20, true, false, 1, "20: regw -> regfp", null, null, null, 0, false, false, new int[]{7}, new String[]{"*reg-I32*", "*reg-FP*"});
    rulev[14] = new Rule(14, true, false, 3, "14: regb -> xregb", null, null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I8*", null});
    rulev[15] = new Rule(15, true, false, 2, "15: regh -> xregh", null, null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I16*", null});
    rulev[19] = new Rule(19, true, false, 6, "19: regsp -> xregsp", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-SP*", null});
    rulev[16] = new Rule(16, true, false, 1, "16: regw -> xregw", null, null, null, 0, false, false, new int[]{13}, new String[]{"*reg-I32*", null});
    rulev[17] = new Rule(17, true, false, 4, "17: regf -> xregf", null, null, null, 0, false, false, new int[]{14}, new String[]{"*reg-F32*", null});
    rulev[18] = new Rule(18, true, false, 5, "18: regd -> xregd", null, null, null, 0, false, false, new int[]{15}, new String[]{"*reg-F64*", null});
    rulev[58] = new Rule(58, true, false, 1, "58: regw -> const_8", ImList.list(ImList.list("mov","$0",ImList.list("hash","$1"))), null, null, 0, false, false, new int[]{20}, new String[]{"*reg-I32*", null});
    rulev[59] = new Rule(59, true, false, 2, "59: regh -> const_8", ImList.list(ImList.list("mov","$0",ImList.list("hash","$1"))), null, null, 0, false, false, new int[]{20}, new String[]{"*reg-I16*", null});
    rulev[60] = new Rule(60, true, false, 3, "60: regb -> const_8", ImList.list(ImList.list("mov","$0",ImList.list("hash","$1"))), null, null, 0, false, false, new int[]{20}, new String[]{"*reg-I8*", null});
    rulev[137] = new Rule(137, true, false, 44, "137: compop -> const_8", null, ImList.list(ImList.list("hash","$1")), null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[61] = new Rule(61, true, false, 1, "61: regw -> const_8m", ImList.list(ImList.list("mov","$0",ImList.list("hash",ImList.list("abs","$1"))),ImList.list("neg","$0","$0")), null, null, 0, false, false, new int[]{21}, new String[]{"*reg-I32*", null});
    rulev[62] = new Rule(62, true, false, 2, "62: regh -> const_8m", ImList.list(ImList.list("mov","$0",ImList.list("hash",ImList.list("abs","$1"))),ImList.list("neg","$0","$0")), null, null, 0, false, false, new int[]{21}, new String[]{"*reg-I16*", null});
    rulev[63] = new Rule(63, true, false, 3, "63: regb -> const_8m", ImList.list(ImList.list("mov","$0",ImList.list("hash",ImList.list("abs","$1"))),ImList.list("neg","$0","$0")), null, null, 0, false, false, new int[]{21}, new String[]{"*reg-I8*", null});
    rulev[37] = new Rule(37, true, false, 29, "37: const_any -> const_int", null, null, null, 0, false, false, new int[]{27}, new String[]{null, null});
    rulev[64] = new Rule(64, true, false, 1, "64: regw -> const_int", ImList.list(ImList.list("load_const","$0","$1")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I32*", null});
    rulev[65] = new Rule(65, true, false, 2, "65: regh -> const_int", ImList.list(ImList.list("load_const","$0","$1")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I16*", null});
    rulev[66] = new Rule(66, true, false, 3, "66: regb -> const_int", ImList.list(ImList.list("load_const","$0","$1")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I8*", null});
    rulev[38] = new Rule(38, true, false, 29, "38: const_any -> const_static", null, null, null, 0, false, false, new int[]{28}, new String[]{null, null});
    rulev[67] = new Rule(67, true, false, 1, "67: regw -> const_any", ImList.list(ImList.list("ldr","$0",ImList.list("lit","$1"))), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-I32*", null});
    rulev[68] = new Rule(68, true, false, 2, "68: regh -> const_any", ImList.list(ImList.list("ldr","$0",ImList.list("lit","$1"))), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-I16*", null});
    rulev[69] = new Rule(69, true, false, 3, "69: regb -> const_any", ImList.list(ImList.list("ldr","$0",ImList.list("lit","$1"))), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-I8*", null});
    rulev[72] = new Rule(72, true, false, 1, "72: regw -> memw", ImList.list(ImList.list("ldr","$0","$1")), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[71] = new Rule(71, true, false, 2, "71: regh -> memh", ImList.list(ImList.list("ldrh","$0","$1")), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I16*", null});
    rulev[70] = new Rule(70, true, false, 3, "70: regb -> memb", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I8*", null});
    rulev[22] = new Rule(22, false, false, 16, "22: const_5x4 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[23] = new Rule(23, false, false, 17, "23: const_5x2 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[24] = new Rule(24, false, false, 18, "24: const_5 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[25] = new Rule(25, false, false, 19, "25: const_8x4 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[26] = new Rule(26, false, false, 20, "26: const_8 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[27] = new Rule(27, false, false, 21, "27: const_8m -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[28] = new Rule(28, false, false, 22, "28: const_7x4 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[29] = new Rule(29, false, false, 23, "29: const_7x4m -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[30] = new Rule(30, false, false, 24, "30: const_3 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[31] = new Rule(31, false, false, 25, "31: shift_5 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[32] = new Rule(32, false, false, 26, "32: const_0 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[33] = new Rule(33, false, false, 27, "33: const_int -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[189] = new Rule(189, false, true, 67, "189: _26 -> (INTCONST I32 4)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[195] = new Rule(195, false, true, 72, "195: _31 -> (INTCONST I32 8)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[34] = new Rule(34, false, false, 28, "34: const_static -> (STATIC _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 10, "2: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[4] = new Rule(4, false, false, 11, "4: xregh -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 12, "6: xregsp -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 13, "7: xregw -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 7, "8: regfp -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-FP*"});
    rulev[188] = new Rule(188, false, true, 66, "188: _25 -> (REG I32 \"%sp\")", null, null, null, 0, false, false, new int[]{}, null);
    rulev[10] = new Rule(10, false, false, 14, "10: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 15, "12: xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 10, "3: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 11, "5: xregh -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[9] = new Rule(9, false, false, 13, "9: xregw -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[11] = new Rule(11, false, false, 14, "11: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[13] = new Rule(13, false, false, 15, "13: xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 9, "1: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[135] = new Rule(135, false, false, 1, "135: regw -> (NEG I32 regw)", ImList.list(ImList.list("neg","$0","$1")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[35] = new Rule(35, false, false, 29, "35: const_any -> (ADD I32 const_static const_int)", null, ImList.list(ImList.list("offset","$1","$2")), null, 0, false, false, new int[]{28,27}, new String[]{null, null, null});
    rulev[36] = new Rule(36, false, false, 29, "36: const_any -> (ADD I32 const_int const_static)", null, ImList.list(ImList.list("offset","$2","$1")), null, 0, false, false, new int[]{27,28}, new String[]{null, null, null});
    rulev[39] = new Rule(39, false, false, 30, "39: addrsp -> (ADD I32 regsp const_8x4)", null, ImList.list(ImList.list("ind_N","$1","$2")), null, 0, false, false, new int[]{6,19}, new String[]{null, "*reg-SP*", null});
    rulev[41] = new Rule(41, false, false, 31, "41: addrw -> (ADD I32 regw const_5x4)", null, ImList.list(ImList.list("ind_N","$1","$2")), null, 0, false, false, new int[]{1,16}, new String[]{null, "*reg-I32*", null});
    rulev[42] = new Rule(42, false, false, 32, "42: addrh -> (ADD I32 regw const_5x2)", null, ImList.list(ImList.list("ind_N","$1","$2")), null, 0, false, false, new int[]{1,17}, new String[]{null, "*reg-I32*", null});
    rulev[43] = new Rule(43, false, false, 33, "43: addrb -> (ADD I32 regw const_5)", null, ImList.list(ImList.list("ind_N","$1","$2")), null, 0, false, false, new int[]{1,18}, new String[]{null, "*reg-I32*", null});
    rulev[44] = new Rule(44, false, false, 34, "44: addr2 -> (ADD I32 regw regw)", null, ImList.list(ImList.list("ind_R","$1","$2")), null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-I32*", "*reg-I32*"});
    rulev[104] = new Rule(104, false, false, 1, "104: regw -> (ADD I32 regw regw)", ImList.list(ImList.list("add","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[105] = new Rule(105, false, false, 1, "105: regw -> (ADD I32 regw const_3)", ImList.list(ImList.list("add","$0","$1",ImList.list("hash","$2"))), null, null, 0, false, false, new int[]{1,24}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[106] = new Rule(106, false, false, 1, "106: regw -> (ADD I32 regw const_8)", ImList.list(ImList.list("add","$0",ImList.list("hash","$2"))), null, null, 2, false, false, new int[]{1,20}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[107] = new Rule(107, false, false, 1, "107: regw -> (ADD I32 regw regsp)", ImList.list(ImList.list("add","$0","$2")), null, null, 2, false, false, new int[]{1,6}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-SP*"});
    rulev[108] = new Rule(108, false, false, 1, "108: regw -> (ADD I32 regsp regw)", ImList.list(ImList.list("add","$0","$1")), null, null, 4, false, false, new int[]{6,1}, new String[]{"*reg-I32*", "*reg-SP*", "*reg-I32*"});
    rulev[114] = new Rule(114, false, false, 6, "114: regsp -> (ADD I32 regsp const_7x4)", ImList.list(ImList.list("add","$0",ImList.list("hash",ImList.list("abs","$2")))), null, null, 2, false, false, new int[]{6,22}, new String[]{"*reg-SP*", "*reg-SP*", null});
    rulev[115] = new Rule(115, false, false, 6, "115: regsp -> (ADD I32 regsp const_7x4m)", ImList.list(ImList.list("sub","$0",ImList.list("hash",ImList.list("abs","$2")))), null, null, 2, false, false, new int[]{6,23}, new String[]{"*reg-SP*", "*reg-SP*", null});
    rulev[118] = new Rule(118, false, false, 6, "118: regsp -> (ADD I32 regsp regw)", ImList.list(ImList.list("add","$0","$2")), null, null, 2, false, false, new int[]{6,1}, new String[]{"*reg-SP*", "*reg-SP*", "*reg-I32*"});
    rulev[119] = new Rule(119, false, false, 6, "119: regsp -> (ADD I32 regw regsp)", ImList.list(ImList.list("add","$0","$1")), null, null, 4, false, false, new int[]{1,6}, new String[]{"*reg-SP*", "*reg-I32*", "*reg-SP*"});
    rulev[203] = new Rule(203, false, true, 79, "203: _38 -> (ADD I32 _25 _26)", null, null, null, 0, false, false, new int[]{66,67}, null);
    rulev[208] = new Rule(208, false, true, 83, "208: _42 -> (ADD I32 _25 _31)", null, null, null, 0, false, false, new int[]{66,72}, null);
    rulev[109] = new Rule(109, false, false, 1, "109: regw -> (SUB I32 regw regw)", ImList.list(ImList.list("sub","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[110] = new Rule(110, false, false, 1, "110: regw -> (SUB I32 regw const_3)", ImList.list(ImList.list("sub","$0","$1",ImList.list("hash","$2"))), null, null, 0, false, false, new int[]{1,24}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[111] = new Rule(111, false, false, 1, "111: regw -> (SUB I32 regw const_8)", ImList.list(ImList.list("sub","$0",ImList.list("hash","$2"))), null, null, 2, false, false, new int[]{1,20}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[112] = new Rule(112, false, false, 1, "112: regw -> (SUB I32 regw regsp)", ImList.list(ImList.list("sub","$0","$2")), null, null, 2, false, false, new int[]{1,6}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-SP*"});
    rulev[113] = new Rule(113, false, false, 1, "113: regw -> (SUB I32 regsp regw)", ImList.list(ImList.list("sub","$0","$1")), null, null, 4, false, false, new int[]{6,1}, new String[]{"*reg-I32*", "*reg-SP*", "*reg-I32*"});
    rulev[116] = new Rule(116, false, false, 6, "116: regsp -> (SUB I32 regsp const_7x4)", ImList.list(ImList.list("sub","$0",ImList.list("hash",ImList.list("abs","$2")))), null, null, 2, false, false, new int[]{6,22}, new String[]{"*reg-SP*", "*reg-SP*", null});
    rulev[117] = new Rule(117, false, false, 6, "117: regsp -> (SUB I32 regsp const_7x4m)", ImList.list(ImList.list("add","$0",ImList.list("hash",ImList.list("abs","$2")))), null, null, 2, false, false, new int[]{6,23}, new String[]{"*reg-SP*", "*reg-SP*", null});
    rulev[190] = new Rule(190, false, true, 68, "190: _27 -> (SUB I32 _25 _26)", null, null, null, 0, false, false, new int[]{66,67}, null);
    rulev[196] = new Rule(196, false, true, 73, "196: _32 -> (SUB I32 _25 _31)", null, null, null, 0, false, false, new int[]{66,72}, null);
    rulev[123] = new Rule(123, false, false, 1, "123: regw -> (MUL I32 regw regw)", ImList.list(ImList.list("mul","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[124] = new Rule(124, false, false, 1, "124: regw -> (DIVS I32 regw regw)", ImList.list(ImList.list("bl","thumb__divsi3")), null, ImList.list(ImList.list("REG","I32","%r1"),ImList.list("REG","I32","%r2"),ImList.list("REG","I32","%r3"),ImList.list("REG","I32","%ip"),ImList.list("REG","I32","%lr")), 0, false, false, new int[]{1,1}, new String[]{"*reg-r0-I32*", "*reg-r0-I32*", "*reg-r1-I32*"});
    rulev[125] = new Rule(125, false, false, 1, "125: regw -> (DIVU I32 regw regw)", ImList.list(ImList.list("bl","thumb__udivsi3")), null, ImList.list(ImList.list("REG","I32","%r1"),ImList.list("REG","I32","%r2"),ImList.list("REG","I32","%r3"),ImList.list("REG","I32","%ip"),ImList.list("REG","I32","%lr")), 0, false, false, new int[]{1,1}, new String[]{"*reg-r0-I32*", "*reg-r0-I32*", "*reg-r1-I32*"});
    rulev[126] = new Rule(126, false, false, 1, "126: regw -> (MODS I32 regw regw)", ImList.list(ImList.list("bl","thumb__modsi3")), null, ImList.list(ImList.list("REG","I32","%r1"),ImList.list("REG","I32","%r2"),ImList.list("REG","I32","%r3"),ImList.list("REG","I32","%ip"),ImList.list("REG","I32","%lr")), 0, false, false, new int[]{1,1}, new String[]{"*reg-r0-I32*", "*reg-r0-I32*", "*reg-r1-I32*"});
    rulev[127] = new Rule(127, false, false, 1, "127: regw -> (MODU I32 regw regw)", ImList.list(ImList.list("bl","thumb__umodsi3")), null, ImList.list(ImList.list("REG","I32","%r1"),ImList.list("REG","I32","%r2"),ImList.list("REG","I32","%r3"),ImList.list("REG","I32","%ip"),ImList.list("REG","I32","%lr")), 0, false, false, new int[]{1,1}, new String[]{"*reg-r0-I32*", "*reg-r0-I32*", "*reg-r1-I32*"});
    rulev[78] = new Rule(78, false, false, 2, "78: regh -> (CONVSX I16 memb2)", ImList.list(ImList.list("ldrsb","$0","$1")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I16*", null});
    rulev[100] = new Rule(100, false, false, 2, "100: regh -> (CONVSX I16 regb)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","24")),ImList.list("asr","$0","$0",ImList.list("hash","24"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[76] = new Rule(76, false, false, 1, "76: regw -> (CONVSX I32 memb2)", ImList.list(ImList.list("ldrsb","$0","$1")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I32*", null});
    rulev[77] = new Rule(77, false, false, 1, "77: regw -> (CONVSX I32 memh2)", ImList.list(ImList.list("ldrsh","$0","$1")), null, null, 0, false, false, new int[]{38}, new String[]{"*reg-I32*", null});
    rulev[98] = new Rule(98, false, false, 1, "98: regw -> (CONVSX I32 regh)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","16")),ImList.list("asr","$0","$0",ImList.list("hash","16"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[99] = new Rule(99, false, false, 1, "99: regw -> (CONVSX I32 regb)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","24")),ImList.list("asr","$0","$0",ImList.list("hash","24"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[75] = new Rule(75, false, false, 2, "75: regh -> (CONVZX I16 memb)", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I16*", null});
  }
  static private void rrinit100() {
    rulev[97] = new Rule(97, false, false, 2, "97: regh -> (CONVZX I16 regb)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","24")),ImList.list("lsr","$0","$0",ImList.list("hash","24"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[73] = new Rule(73, false, false, 1, "73: regw -> (CONVZX I32 memb)", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I32*", null});
    rulev[74] = new Rule(74, false, false, 1, "74: regw -> (CONVZX I32 memh)", ImList.list(ImList.list("ldrh","$0","$1")), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[95] = new Rule(95, false, false, 1, "95: regw -> (CONVZX I32 regh)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","16")),ImList.list("lsr","$0","$0",ImList.list("hash","16"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[96] = new Rule(96, false, false, 1, "96: regw -> (CONVZX I32 regb)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","24")),ImList.list("lsr","$0","$0",ImList.list("hash","24"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[84] = new Rule(84, false, true, 42, "84: _2 -> (CONVIT I8 regw)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[86] = new Rule(86, false, true, 43, "86: _3 -> (CONVIT I8 regh)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[102] = new Rule(102, false, false, 3, "102: regb -> (CONVIT I8 regw)", null, null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-I32*"});
    rulev[103] = new Rule(103, false, false, 3, "103: regb -> (CONVIT I8 regh)", null, null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-I16*"});
    rulev[82] = new Rule(82, false, true, 41, "82: _1 -> (CONVIT I16 regw)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[101] = new Rule(101, false, false, 2, "101: regh -> (CONVIT I16 regw)", null, null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[182] = new Rule(182, false, false, 5, "182: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("mvfd","$0","$1")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-F64*", "*reg-F32*"});
    rulev[183] = new Rule(183, false, false, 4, "183: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("mvfs","$0","$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F64*"});
    rulev[120] = new Rule(120, false, false, 1, "120: regw -> (BAND I32 regw regw)", ImList.list(ImList.list("and","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[159] = new Rule(159, false, true, 55, "159: _14 -> (BAND I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[121] = new Rule(121, false, false, 1, "121: regw -> (BOR I32 regw regw)", ImList.list(ImList.list("orr","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[122] = new Rule(122, false, false, 1, "122: regw -> (BXOR I32 regw regw)", ImList.list(ImList.list("eor","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[134] = new Rule(134, false, false, 1, "134: regw -> (BNOT I32 regw)", ImList.list(ImList.list("mvn","$0","$1")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[128] = new Rule(128, false, false, 1, "128: regw -> (LSHS I32 regw regw)", ImList.list(ImList.list("lsl","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[131] = new Rule(131, false, false, 1, "131: regw -> (LSHS I32 regw const_5)", ImList.list(ImList.list("lsl","$0","$1",ImList.list("hash","$2"))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[129] = new Rule(129, false, false, 1, "129: regw -> (RSHS I32 regw regw)", ImList.list(ImList.list("asr","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[132] = new Rule(132, false, false, 1, "132: regw -> (RSHS I32 regw shift_5)", ImList.list(ImList.list("asr","$0","$1",ImList.list("hash","$2"))), null, null, 0, false, false, new int[]{1,25}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[130] = new Rule(130, false, false, 1, "130: regw -> (RSHU I32 regw regw)", ImList.list(ImList.list("lsr","$1","$2")), null, null, 2, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[133] = new Rule(133, false, false, 1, "133: regw -> (RSHU I32 regw shift_5)", ImList.list(ImList.list("lsr","$0","$1",ImList.list("hash","$2"))), null, null, 0, false, false, new int[]{1,25}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[138] = new Rule(138, false, false, 1, "138: regw -> (TSTEQ I32 regw compop)", ImList.list(ImList.list("cmp","$1","$2")), null, null, 0, false, false, new int[]{1,44}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[139] = new Rule(139, false, true, 45, "139: _4 -> (TSTEQ I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[160] = new Rule(160, false, true, 56, "160: _15 -> (TSTEQ I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[141] = new Rule(141, false, true, 46, "141: _5 -> (TSTNE I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[162] = new Rule(162, false, true, 57, "162: _16 -> (TSTNE I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[143] = new Rule(143, false, true, 47, "143: _6 -> (TSTLTS I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[164] = new Rule(164, false, true, 58, "164: _17 -> (TSTLTS I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[145] = new Rule(145, false, true, 48, "145: _7 -> (TSTLES I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[166] = new Rule(166, false, true, 59, "166: _18 -> (TSTLES I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[147] = new Rule(147, false, true, 49, "147: _8 -> (TSTGTS I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[168] = new Rule(168, false, true, 60, "168: _19 -> (TSTGTS I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[149] = new Rule(149, false, true, 50, "149: _9 -> (TSTGES I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[170] = new Rule(170, false, true, 61, "170: _20 -> (TSTGES I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[151] = new Rule(151, false, true, 51, "151: _10 -> (TSTLTU I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[172] = new Rule(172, false, true, 62, "172: _21 -> (TSTLTU I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[153] = new Rule(153, false, true, 52, "153: _11 -> (TSTLEU I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[174] = new Rule(174, false, true, 63, "174: _22 -> (TSTLEU I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[155] = new Rule(155, false, true, 53, "155: _12 -> (TSTGTU I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[176] = new Rule(176, false, true, 64, "176: _23 -> (TSTGTU I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[157] = new Rule(157, false, true, 54, "157: _13 -> (TSTGEU I32 regw compop)", null, null, null, 0, false, false, new int[]{1,44}, null);
    rulev[178] = new Rule(178, false, true, 65, "178: _24 -> (TSTGEU I32 _14 const_0)", null, null, null, 0, false, false, new int[]{55,26}, null);
    rulev[54] = new Rule(54, false, false, 39, "54: memb -> (MEM I8 addrb)", null, null, null, 0, false, false, new int[]{33}, new String[]{null, null});
    rulev[55] = new Rule(55, false, false, 39, "55: memb -> (MEM I8 addr)", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[56] = new Rule(56, false, false, 39, "56: memb -> (MEM I8 addr2)", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[57] = new Rule(57, false, false, 40, "57: memb2 -> (MEM I8 addr2)", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[50] = new Rule(50, false, false, 37, "50: memh -> (MEM I16 addrh)", null, null, null, 0, false, false, new int[]{32}, new String[]{null, null});
    rulev[51] = new Rule(51, false, false, 37, "51: memh -> (MEM I16 addr)", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[52] = new Rule(52, false, false, 37, "52: memh -> (MEM I16 addr2)", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[53] = new Rule(53, false, false, 38, "53: memh2 -> (MEM I16 addr2)", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[46] = new Rule(46, false, false, 36, "46: memw -> (MEM I32 addrsp)", null, null, null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[47] = new Rule(47, false, false, 36, "47: memw -> (MEM I32 addrw)", null, null, null, 0, false, false, new int[]{31}, new String[]{null, null});
    rulev[48] = new Rule(48, false, false, 36, "48: memw -> (MEM I32 addr)", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[49] = new Rule(49, false, false, 36, "49: memw -> (MEM I32 addr2)", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[201] = new Rule(201, false, true, 77, "201: _36 -> (MEM I32 _25)", null, null, null, 0, false, false, new int[]{66}, null);
    rulev[206] = new Rule(206, false, true, 81, "206: _40 -> (MEM I32 _38)", null, null, null, 0, false, false, new int[]{79}, null);
    rulev[192] = new Rule(192, false, true, 70, "192: _29 -> (MEM F32 _25)", null, null, null, 0, false, false, new int[]{66}, null);
    rulev[198] = new Rule(198, false, true, 75, "198: _34 -> (MEM F64 _25)", null, null, null, 0, false, false, new int[]{66}, null);
    rulev[212] = new Rule(212, false, true, 87, "212: _44 -> (MEM F64 base)", null, null, null, 0, false, false, new int[]{86}, null);
    rulev[81] = new Rule(81, false, false, 8, "81: void -> (SET I8 memb regb)", ImList.list(ImList.list("strb","$2","$1")), null, null, 0, false, false, new int[]{39,3}, new String[]{null, null, "*reg-I8*"});
    rulev[85] = new Rule(85, false, false, 8, "85: void -> (SET I8 memb _2)", ImList.list(ImList.list("strb","$2","$1")), null, null, 0, false, false, new int[]{39,42}, new String[]{null, null, "*reg-I32*"});
    rulev[87] = new Rule(87, false, false, 8, "87: void -> (SET I8 memb _3)", ImList.list(ImList.list("strb","$2","$1")), null, null, 0, false, false, new int[]{39,43}, new String[]{null, null, "*reg-I16*"});
    rulev[94] = new Rule(94, false, false, 8, "94: void -> (SET I8 xregb regb)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{10,3}, new String[]{null, null, "*reg-I8*"});
    rulev[80] = new Rule(80, false, false, 8, "80: void -> (SET I16 memh regh)", ImList.list(ImList.list("strh","$2","$1")), null, null, 0, false, false, new int[]{37,2}, new String[]{null, null, "*reg-I16*"});
    rulev[83] = new Rule(83, false, false, 8, "83: void -> (SET I16 memh _1)", ImList.list(ImList.list("strh","$2","$1")), null, null, 0, false, false, new int[]{37,41}, new String[]{null, null, "*reg-I32*"});
    rulev[93] = new Rule(93, false, false, 8, "93: void -> (SET I16 xregh regh)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{11,2}, new String[]{null, null, "*reg-I16*"});
    rulev[79] = new Rule(79, false, false, 8, "79: void -> (SET I32 memw regw)", ImList.list(ImList.list("str","$2","$1")), null, null, 0, false, false, new int[]{36,1}, new String[]{null, null, "*reg-I32*"});
    rulev[89] = new Rule(89, false, false, 8, "89: void -> (SET I32 xregsp regsp)", null, null, null, 0, false, false, new int[]{12,6}, new String[]{null, null, "*reg-SP*"});
    rulev[90] = new Rule(90, false, false, 8, "90: void -> (SET I32 xregsp regw)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{12,1}, new String[]{null, null, "*reg-I32*"});
    rulev[91] = new Rule(91, false, false, 8, "91: void -> (SET I32 xregw regw)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{13,1}, new String[]{null, null, "*reg-I32*"});
    rulev[92] = new Rule(92, false, false, 8, "92: void -> (SET I32 xregw regsp)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{13,6}, new String[]{null, null, "*reg-SP*"});
    rulev[191] = new Rule(191, false, true, 69, "191: _28 -> (SET I32 _25 _27)", null, null, null, 0, false, false, new int[]{66,68}, null);
    rulev[197] = new Rule(197, false, true, 74, "197: _33 -> (SET I32 _25 _32)", null, null, null, 0, false, false, new int[]{66,73}, null);
    rulev[202] = new Rule(202, false, true, 78, "202: _37 -> (SET I32 regw _36)", null, null, null, 0, false, false, new int[]{1,77}, null);
    rulev[204] = new Rule(204, false, true, 80, "204: _39 -> (SET I32 _25 _38)", null, null, null, 0, false, false, new int[]{66,79}, null);
    rulev[207] = new Rule(207, false, true, 82, "207: _41 -> (SET I32 regw _40)", null, null, null, 0, false, false, new int[]{1,81}, null);
    rulev[209] = new Rule(209, false, true, 84, "209: _43 -> (SET I32 _25 _42)", null, null, null, 0, false, false, new int[]{66,83}, null);
    rulev[185] = new Rule(185, false, false, 8, "185: void -> (SET F32 xregf regf)", ImList.list(ImList.list("mvfs","$1","$2")), null, null, 0, false, false, new int[]{14,4}, new String[]{null, null, "*reg-F32*"});
    rulev[193] = new Rule(193, false, true, 71, "193: _30 -> (SET F32 _29 regf)", null, null, null, 0, false, false, new int[]{70,4}, null);
    rulev[211] = new Rule(211, false, false, 8, "211: void -> (SET F32 memf memf)", ImList.list(ImList.list("ldr","%lr","$2"),ImList.list("str","%lr","$1")), null, ImList.list(ImList.list("REG","I32","%lr")), 0, false, false, new int[]{85,85}, new String[]{null, null, null});
    rulev[184] = new Rule(184, false, false, 8, "184: void -> (SET F64 xregd regd)", ImList.list(ImList.list("mvfd","$1","$2")), null, null, 0, false, false, new int[]{15,5}, new String[]{null, null, "*reg-F64*"});
    rulev[199] = new Rule(199, false, true, 76, "199: _35 -> (SET F64 _34 regd)", null, null, null, 0, false, false, new int[]{75,5}, null);
    rulev[213] = new Rule(213, false, false, 8, "213: void -> (SET F64 _44 _44)", ImList.list(ImList.list("ldmia","$2","{%ip, %lr}"),ImList.list("stmia","$1","{%ip, %lr}")), null, ImList.list(ImList.list("REG","I32","%ip"),ImList.list("REG","I32","%lr")), 0, false, false, new int[]{87,87}, new String[]{null, null, null});
    rulev[88] = new Rule(88, false, false, 8, "88: void -> (JUMP _ label)", ImList.list(ImList.list("b","$1")), null, null, 0, false, false, new int[]{9}, new String[]{null, null});
    rulev[140] = new Rule(140, false, false, 8, "140: void -> (JUMPC _ _4 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("beq","$3")), null, null, 0, false, false, new int[]{45,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[142] = new Rule(142, false, false, 8, "142: void -> (JUMPC _ _5 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bne","$3")), null, null, 0, false, false, new int[]{46,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[144] = new Rule(144, false, false, 8, "144: void -> (JUMPC _ _6 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("blt","$3")), null, null, 0, false, false, new int[]{47,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[146] = new Rule(146, false, false, 8, "146: void -> (JUMPC _ _7 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("ble","$3")), null, null, 0, false, false, new int[]{48,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[148] = new Rule(148, false, false, 8, "148: void -> (JUMPC _ _8 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bgt","$3")), null, null, 0, false, false, new int[]{49,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[150] = new Rule(150, false, false, 8, "150: void -> (JUMPC _ _9 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bge","$3")), null, null, 0, false, false, new int[]{50,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[152] = new Rule(152, false, false, 8, "152: void -> (JUMPC _ _10 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("blo","$3")), null, null, 0, false, false, new int[]{51,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[154] = new Rule(154, false, false, 8, "154: void -> (JUMPC _ _11 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bls","$3")), null, null, 0, false, false, new int[]{52,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[156] = new Rule(156, false, false, 8, "156: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bhi","$3")), null, null, 0, false, false, new int[]{53,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[158] = new Rule(158, false, false, 8, "158: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bhs","$3")), null, null, 0, false, false, new int[]{54,9,9}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[161] = new Rule(161, false, false, 8, "161: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("beq","$4")), null, null, 0, false, false, new int[]{56,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[163] = new Rule(163, false, false, 8, "163: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("bne","$4")), null, null, 0, false, false, new int[]{57,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[165] = new Rule(165, false, false, 8, "165: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("blt","$4")), null, null, 0, false, false, new int[]{58,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
  }
  static private void rrinit200() {
    rulev[167] = new Rule(167, false, false, 8, "167: void -> (JUMPC _ _18 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("ble","$4")), null, null, 0, false, false, new int[]{59,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[169] = new Rule(169, false, false, 8, "169: void -> (JUMPC _ _19 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("bgt","$4")), null, null, 0, false, false, new int[]{60,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[171] = new Rule(171, false, false, 8, "171: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("bge","$4")), null, null, 0, false, false, new int[]{61,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[173] = new Rule(173, false, false, 8, "173: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("blo","$4")), null, null, 0, false, false, new int[]{62,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[175] = new Rule(175, false, false, 8, "175: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("bls","$4")), null, null, 0, false, false, new int[]{63,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[177] = new Rule(177, false, false, 8, "177: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("bhi","$4")), null, null, 0, false, false, new int[]{64,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[179] = new Rule(179, false, false, 8, "179: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("tst","$1","$2"),ImList.list("bhs","$4")), null, null, 0, false, false, new int[]{65,9,9}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null, null});
    rulev[180] = new Rule(180, false, false, 8, "180: void -> (CALL _ const_any)", ImList.list(ImList.list("bl","$1")), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[181] = new Rule(181, false, false, 8, "181: void -> (CALL _ regw)", ImList.list(ImList.list("bl",ImList.list("call_via","$1"))), null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-call-via*"});
    rulev[194] = new Rule(194, false, false, 8, "194: void -> (PARALLEL _ _28 _30)", ImList.list(ImList.list("stfs","$1",ImList.list("preinc","sp","-4"))), null, null, 0, false, false, new int[]{69,71}, new String[]{null, "*reg-F32*"});
    rulev[200] = new Rule(200, false, false, 8, "200: void -> (PARALLEL _ _33 _35)", ImList.list(ImList.list("stfd","$1",ImList.list("preinc","sp","-8"))), null, null, 0, false, false, new int[]{74,76}, new String[]{null, "*reg-F64*"});
    rulev[205] = new Rule(205, false, false, 8, "205: void -> (PARALLEL _ _37 _39)", ImList.list(ImList.list("ldmfd","sp!",ImList.list("regpair1","$1"))), null, null, 0, false, false, new int[]{78,80}, new String[]{null, "*reg-I32*"});
    rulev[210] = new Rule(210, false, false, 8, "210: void -> (PARALLEL _ _37 _41 _43)", ImList.list(ImList.list("ldmfd","sp!",ImList.list("regpair2","$1","$2"))), null, null, 0, false, false, new int[]{78,82,84}, new String[]{null, "*reg-I32*", "*reg-I32*"});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 514: return "*reg-I32*";
    case 258: return "*reg-I16*";
    case 130: return "*reg-I8*";
    case 1028: return "*reg-F64*";
    case 516: return "*reg-F32*";
    default:
      return null;
    }
  }


  void initLabeling(LirFactory lir) {
    stateVec = new State[lir.idBound()];
  }

  String showLabel(LirNode t) {
    return stateVec[t.id].toString();
  }


  void labelTree(LirNode t) {
    if (stateVec[t.id] == null) {
      int n = nActualOperands(t);
      State[] kid = new State[n];
      for (int i = 0; i < n; i++) {
        LirNode s = t.kid(i);
        labelTree(s);
        kid[i] = stateVec[s.id];
      }

      State st = new State();
      stateVec[t.id] = st;
      st.label(t, kid);
    }
  }

  Rule getRule(LirNode t, int goal) {
    return rulev[stateVec[t.id].rule[goal]];
  }

  int getCost1(LirNode t, int goal) {
    return stateVec[t.id].cost1[goal];
  }

  int getCost2(LirNode t, int goal) {
    return stateVec[t.id].cost2[goal];
  }

  int startNT() { return State.START_NT; }

  /* String nameOfNT(int nt) { return nontermNamev[nt]; } */

  /** Expand building-macro. **/
  Object expandBuildMacro(ImList form) {
    String name = (String)form.elem();
    return null;
  }

  /** Expand building-macro, for LirNode **/
  Object quiltLir(LirNode node) {
    switch (node.opCode) {
    default:
      return quiltLirDefault(node);
    }
  }

  /** Expand emit-macro for list form. **/
  String emitList(ImList form, boolean topLevel) {
    String name = (String)form.elem();
    if (name == "abs")
      return jmac1(emitObject(form.elem(1)));
    else if (name == "lit")
      return jmac2(emitObject(form.elem(1)));
    else if (name == "load_const")
      return jmac3(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "offset")
      return jmac4(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "call_via")
      return jmac5(emitObject(form.elem(1)));
    else if (name == "ind_N")
      return jmac6(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "ind_R")
      return jmac7(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "ind_0")
      return jmac8(emitObject(form.elem(1)));
    else if (name == "hash")
      return jmac9(emitObject(form.elem(1)));
    else if (name == "preinc")
      return jmac10(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regpair2")
      return jmac11(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regpair1")
      return jmac12(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac13(form.elem(1));
    else if (name == "epilogue")
      return jmac14(form.elem(1), emitObject(form.elem(2)));
    else if (name == "deflabel")
      return jmac15(emitObject(form.elem(1)));
    else if (name == "line")
      return jmac16(emitObject(form.elem(1)));
    return emitListDefault(form, topLevel);
  }

  /** Expand emit-macro for LirNode. **/
  String emitLir(LirNode node) {
    switch (node.opCode) {
    default:
      return emitLirDefault(node);
    }
  }

  // CodeGenerator methods here
  
  private LiteralAndBranchProcessor pp;
  
  final class Thumb extends CPU {
  
  	Thumb() {
  		bccRange = new int[] {-240, 250};
  		braRange = new int[] {-2024, 2024};
  		literalRange = new int[] {0, 1020};
  		bccMnemo = new String[] { "beq", "bne", "bcs", "bcc", "bhs", "blo", "bmi", "bpl",
  					  "bvs", "bvc", "bhi", "bls", "bge", "blt", "bgt", "ble" };
  		braMnemo = "b";
  		braLength = 2;
  		codeAlign = 1;
  	}
  
  	public int codeLength(String inst)
  	{
  		StringTokenizer tokens = new StringTokenizer(inst, " \t,");
  		if (tokens.hasMoreTokens()) {
  			String mnemo = tokens.nextToken();
  			if (mnemo.equalsIgnoreCase("bl")) {
  				return 4;
  			}
  		}
  		return 2;
  	}
  
  	public String[] rewriteToLongBranch(String label) {
  		return new String[] { "\tbl\t" + label };
  	}
  
  	public String toString() {
  		return "Thumb";
  	}
  }
  
  /** Run Literal and Branch post processor after generating assembly code. **/
  OutputStream insertPostProcessor(OutputStream out) {
     pp = LiteralAndBranchProcessor.postProcessor(out);
     pp.setCPU(new Thumb());
     return pp.pipeTo();
  }
  
  void notifyEndToPostProcessor() {
     pp.notifyEnd();
  }
  
  ImList regCallClobbers = new ImList(ImList.list("REG","I32","%r0"), new ImList(ImList.list("REG","I32","%r1"), new ImList(ImList.list("REG","I32","%r2"), new ImList(ImList.list("REG","I32","%r3"), ImList.list(ImList.list("REG","F64","%f0"),ImList.list("REG","F64","%f1"),ImList.list("REG","F64","%f2"),ImList.list("REG","F64","%f3"),ImList.list("REG","I32","%lr"))))));
  
  int tmpCnt = 0;
  
  /** Arm's function attribute **/
  static class ThumbAttr extends FunctionAttr {
  
    /** Maximum stack space used by call. **/
    int stackRequired;
  
    /** pretend value. */
    /* bytes count of register passed parameter which copied to stack on
       prologue. */
    int pretend;
  
    /* bytes count of register passed parameter which copied to stack on
       prologue on variadic argument. */
    int pretend2;
  
    /** pointer of aggregate return value. */
    LirNode hiddenPtr;
  
    /** is variadic ? */
    boolean variadic;
  
    /** callee save register */
    int adjustCalleeSaves;
  
    boolean requireFp;
    boolean allocaUsed;
    boolean regSaved;
  
    ThumbAttr(Function func) {
      super(func);
      stackRequired = 0;
      pretend = 0;
      pretend2 = 0;
      hiddenPtr = null;
      variadic = false;
      adjustCalleeSaves = -1;
      requireFp = false;
      allocaUsed = false;
      regSaved = false;
    }
  }
  
  FunctionAttr newFunctionAttr(Function func) {
  	return new ThumbAttr(func);
  }
  
  boolean isConv(int op)
  {
      switch (op) {
      case Op.CONVSX:
      case Op.CONVFX:
  	return true;
      default:
  	return false;
      }
  }
  
  boolean equalArg(LirNode x, LirNode y) {
      if (x.opCode == Op.MEM || isConv(x.opCode)) {
  	x = x.kid(0);
      }
      if (y.opCode == Op.MEM || isConv(y.opCode)) {
  	y = y.kid(0);
      }
      if (x instanceof LirSymRef && y instanceof LirSymRef) {
  	return ((LirSymRef)x).symbol == ((LirSymRef)y).symbol;
      }
      // System.out.println("equal ? " + x + " : "  + y);
      return false;
  }
  
  /** Return offset for va_start position. **/
  int makeVaStart(LirNode arg) {
    ThumbAttr at = (ThumbAttr)getFunctionAttr(func);
    at.variadic = true;
  
    LirNode node = null;
    for (BiLink p = func.firstInstrList().first(); !p.atEnd(); p = p.next()) {
      node = (LirNode)p.elem();
      if (node.opCode == Op.PROLOGUE)
        break;
    }
  
    int n = node.nKids();
  
    int regUsed = 0;
    int regUsed0 = 0;
    int total = 0;
  
    if (func.origEpilogue.nKids() > 1) {
        int t = func.origEpilogue.kid(1).type;
        if (Type.tag(t) == Type.AGGREGATE && Type.bytes(t) > REGWIDTH) {
  	  regUsed0 = regUsed = 1;
  	  total = REGWIDTH;
        }
    }
  
    for (int i = 1; i < n; i++) {
      LirNode x = node.kid(i);
  
      if (equalArg(arg, x)) {
  	debug("found last arg " + x);
  	break;
      }
  
      int b = Type.bytes(x.type);
      if (b < 4) {
  	b = REGWIDTH;			// round up to machine word
      }
      debug("+ " + x + " = " + b);
      total += b;
  
      regUsed0 = regUsed;
      while (b > 0) {
        regUsed++;
        b -= REGWIDTH;
      }
    }
    debug("last - 1 = r" + regUsed0);
    debug("last = r" + regUsed);
    debug("total = " + total);
  
    if (regUsed0 > 3) {
        // params. passed by register are still on register.
        total -= 16;
        total += Type.bytes(arg.type);
        at.pretend2 = MAXREGPARAM;
    } else {
        // need to dump to stack.
        at.pretend2 = regUsed;
        // address = fp + 4 + sizeof(lastArg).
        debug("sizeof(last arg) = " + Type.bytes(arg.type));
        total = Type.bytes(arg.type);
    }
    debug("offset = fp + 4 + " + total);
    return total + 4;
  }
  
  static final int I64 = Type.type(Type.INT, 64);
  static final int I32 = Type.type(Type.INT, 32);
  static final int I16 = Type.type(Type.INT, 16);
  static final int I8 = Type.type(Type.INT, 8);
  static final int F64 = Type.type(Type.FLOAT, 64);
  static final int F32 = Type.type(Type.FLOAT, 32);
  
  static final int MAXREGPARAM = 4;	// regs
  static final int REGWIDTH = 4;		// bytes
  static final int SIZE_OF_REGS_ON_STACK = 20;	// r4-r7,lr
  //static final int CALLEE_SAVE = 16;	// r4-r7
  //static final String NEED_OFFSET_REWRITE = "&vframe";
  
  private void debug(String s)
  {
  //    System.out.println(s);
  }
  
  private void debug2(String s)
  {
  //    System.out.println(":::" + s);
  }
  
  private void bug()
  {
    System.out.println("bug");
  }
  
  
  private void bug(String s)
  {
    System.out.println("bug: " + s);
  }
  
  private LirNode setExp(int type, LirNode op1, LirNode op2) {
      return lir.node(Op.SET, type, op1, op2);
  }
  
  private LirNode memExp(int type, LirNode op1) {
      return lir.node(Op.MEM, type, op1);
  }
  
  private LirNode memExp(int type, LirNode op1, int align) {
      if (true) {
      return lir.operator(Op.MEM, type, op1,
  		    ImList.list("&align", String.valueOf(align)));
      }
      return lir.operator(Op.MEM, type, op1, ImList.Empty);
  }
  
  
  private LirNode addExp(int type, LirNode op1, LirNode op2) {
      return lir.node(Op.ADD, type, op1, op2);
  }
  
  /*private LirNode addExp(int type, LirNode op1, LirNode op2, String opt) {
      if (false) {
  	return lir.operator(Op.ADD, type, op1, op2, ImList.list(opt));
      }
      return lir.operator(Op.ADD, type, op1, op2, ImList.Empty);
  }
  */
  
  private LirNode setI32(LirNode op1, LirNode op2) {
      return lir.node(Op.SET, I32, op1, op2);
  }
  
  private LirNode memI32(LirNode op1) {
      return lir.node(Op.MEM, I32, op1);
  }
  
  private LirNode memI32Base(LirNode base, int disp) {
      return memI32(addI32(base, disp));
  }
  
  private LirNode addI32(LirNode op1, int n) {
      return lir.node(Op.ADD, I32, op1, lir.iconst(I32, n));
  }
  
  
  private LirNode regI32(int nth) {
      return lir.symRef(module.globalSymtab.get("%r" + nth));
  }
  
  /** Rewrite FRAME node to target machine form. **/
  LirNode rewriteFrame(LirNode node) {
  	ThumbAttr attr = (ThumbAttr)getFunctionAttr(func);
  	int size = frameSize(func);
  	int off = ((SymAuto)((LirSymRef)node).symbol).offset();
  //System.out.println(node.toString() + ", size = " + size + ", off = " + off);
  	if (false && !attr.allocaUsed && node.type == I32 &&
  	    0 <= size + off && size + off <= 1020 && -off % 4 == 0) {
  		Symbol fp = func.module.globalSymtab.get("%sp");
  		return lir.node(Op.ADD, node.type, lir.symRef(fp), lir.iconst(I32, (long)(size + off)));
  	} else {
  		attr.requireFp = true;
  		Symbol fp = func.module.globalSymtab.get("%r7");
  		if (off < 0) {
  			return lir.node(Op.SUB, node.type, lir.symRef(fp), lir.iconst(I32, (long)-off));
  		} else {
  			return lir.node(Op.ADD, node.type, lir.symRef(fp), lir.iconst(I32, (long)off));
  		}
  	}
  }
  
  
  private LirNode regnode(int type, String name) {
      LirNode master = lir.symRef(module.globalSymtab.get(name));
      switch (Type.tag(type)) {
      case Type.INT:
  	if (type == I32)
  	    return master;
  	else if (type == I16)
  	    return lir.node(Op.SUBREG, I16, master, lir.untaggedIconst(I32, 0));
  	else if (type == I8)
  	    return lir.node(Op.SUBREG, I8, master, lir.untaggedIconst(I32, 0));
      case Type.FLOAT:
  	if (type == F64)
  	    return master;
  	else if (type == F32)
  	    return lir.node(Op.SUBREG, F32, master, lir.untaggedIconst(I32, 0));
      default:
  	return null;
      }
  }
  
  private LirNode stackMem(int type, int location, LirNode base) {
    return lir.node(Op.MEM, type, lir.node
  		  (Op.ADD, I32, base,
  		   lir.iconst(I32, location + adjustEndian(type))));
  }
  
  private int adjustEndian(int type) { 
      if (false) {
  	if (type == I8)
  	    return 3;
  	else if (type == I16)
  	    return 2;
  	else
  	    return 0;
      }
      return 0;
  }
  
  /** Rewrite EPILOGUE **/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
      debug("*** rewriting epilogue " + node);
      if (node.nKids() < 2) {
  	debug(" -> no");
  	return node;
      }
  
      LirNode epilogue = null;
      LirNode ret = node.kid(1);
  
      switch (Type.tag(ret.type)) {
      case Type.INT:
  	LirNode r0 = regnode(ret.type, "%r0");
  	pre.add(setExp(ret.type, r0, ret));
  	epilogue = lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), r0);
  	break;
  
      case Type.FLOAT:
  	LirNode f0 = regnode(ret.type, "%f0");
  	pre.add(setExp(ret.type, f0, ret));
  	epilogue = lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), f0);
  	break;
  
      case Type.AGGREGATE:
  	if (true || Type.bytes(ret.type) > REGWIDTH) { /* pnori */
  	    ThumbAttr attr = (ThumbAttr) getFunctionAttr(func);
  	    debug("*** reta = " + attr.hiddenPtr);
  	    // checkme
  	    pre.add(setExp(ret.type, memExp(ret.type, attr.hiddenPtr, 4 /* todo */), ret));
  	    epilogue = lir.node(Op.EPILOGUE, Type.UNKNOWN, new LirNode[]{});
  	} else {
  	    // use r0
              // NB: ret.kid(0) always resides on stack.
  	    pre.add(setI32(regI32(0), memI32(ret.kid(0))));
  	    epilogue = lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), regI32(0));
  	}
  	break;
      }
      debug("*** -pre-> " + pre);
      debug("*** -----> " + epilogue);
      return epilogue;
  }
  
  
  /** Return true if node is a complex one. **/
  boolean isComplex(LirNode node) {
    switch (node.opCode) {
    case Op.INTCONST:
    case Op.REG:
    case Op.STATIC:
    case Op.FRAME:
      return false;
    default:
      return true;
    }
  }
  
  /***********/
  
  private LirNode framenode(int type, int disp) {
  	return lir.node(Op.MEM, type, lir.node(Op.ADD, I32, regnode(I32, "%r7"), lir.iconst(I32, disp)));
  }
  
  int byte2nreg(int bytes) {
  	int i = (bytes + (REGWIDTH - 1)) / REGWIDTH;
  	if (i <= 0) {
  		bug("byte2nreg("+ bytes +") -> "+ i);
  	}
  	return i;
  }
  
  private final static boolean gcc_style = false;
  
  // move parameter to register
  private LirNode setArg(LirNode arg, int regNo) {
  	int ty = arg.type;
  	LirNode tmp = null;
  
  	switch (Type.bytes(arg.type)) {
  	case 1:
  	case 2:
  		if (gcc_style) {	// explicit truncate
  			tmp = setExp(ty, arg, lir.node(Op.CONVIT, ty, regI32(regNo)));
  		} else {
  			tmp = setExp(ty, arg, regnode(ty, "%r" + regNo));
  		}
  		break;
  	default:
  		bug();
  		// fall thru
  	case 4:
  		tmp = setExp(ty, arg, regI32(regNo));
  		break;
  	}
  	return tmp;
  }
  
  /** Rewrite PROLOGUE **/
  LirNode rewritePrologue(LirNode node, BiList post) {
      int regUsed = 0;
      int fpDisp = SIZE_OF_REGS_ON_STACK;
      ThumbAttr attr = (ThumbAttr) getFunctionAttr(func);
  
      final int n = node.nKids();
      LirNode r0 = regI32(0);
  
      debug("*** rewriting prologue " + node);
      debug("*** node.nKids = " + n);
  
      // list of new prologue
      LirNode[] argv = new LirNode[MAXREGPARAM + 1];
      int ai = 0;	// argv index
      argv[ai++] = node.kid(0);
  
      int i = 1;
  
      // Is first parameter a pointer to aggregate return-value ?
      if (func.origEpilogue.nKids() > 1) {
  	int t = func.origEpilogue.kid(1).type;
  	if (Type.tag(t) == Type.AGGREGATE /*&& Type.bytes(t) > REGWIDTH*/) { // pnori
  	    attr.hiddenPtr = func.newTemp(I32);
  	    post.add(setI32(attr.hiddenPtr, r0));
  	    argv[ai++] = r0;
  	    regUsed++;
  	}
      }
  
      for (/* empty */; regUsed < MAXREGPARAM && i < n; i++) {
  	LirNode arg = node.kid(i);
  
  	if (Type.tag(arg.type) == Type.INT) {
  	    // integral parameter passed via register -> keep it
  	    int regNo = regUsed++;
  	    argv[ai++] = regI32(regNo);      // add regnode to new prologue
  	    post.add(setArg(arg, regNo));
  
          } else if  (Type.tag(arg.type) == Type.FLOAT) {
  	    int regs = byte2nreg(Type.bytes(arg.type));
  	    if (regUsed + regs <= MAXREGPARAM) {
  	        int regNo = regUsed++;
  		argv[ai++] = regI32(regNo);
  		SymAuto sym = reserveFrame(".TMP_" + (tmpCnt++), arg.type);
  		post.add(setI32(memI32(lir.symRef(sym)), regI32(regNo)));
  		if (regs == 2) {
  		    regNo = regUsed++;
  		    argv[ai++] = regI32(regNo);
  		    post.add(setI32(memI32(lir.node(Op.ADD, I32, lir.symRef(sym), lir.iconst(I32, 4))), regI32(regNo)));
  		    ///// ??? is it necessary ????
  		    ///// attr.pretend += REGWIDTH;
  		}
  		post.add(setExp(arg.type, arg, 
  				memExp(arg.type, lir.symRef(sym))));
  		
  	    } else {
  		// first 4bytes of double reside in register.
  		int regNo = regUsed++;
  		argv[ai++] = regI32(regNo);
  		// latter half reside [fp+4]
  		post.add(setI32(framenode(I32, 4 /*fixme ?*/), regI32(regNo)));
  		post.add(setExp(arg.type, arg, framenode(arg.type, 4)));
  		attr.pretend += REGWIDTH;
  		fpDisp += 8;
  	    }
  
  	} else if (Type.tag(arg.type) == Type.AGGREGATE) {
  	    int regs = byte2nreg(Type.bytes(arg.type));
  	    debug("used " + regUsed);
  	    debug("aggregate uses " + regs + " regs");
  	    //  copy register to stack
  	    if (regUsed + regs <= MAXREGPARAM) {
  		for (int nth = 0; nth < regs; nth++) {
  		    int regNo = regUsed++;
  		    argv[ai++] = regI32(regNo);
  	            post.add(setI32(memI32Base(arg.kid(0), nth * REGWIDTH), regI32(regNo)));
  		}
  	    } else {
  		// splitted on register and stack
  		regs = MAXREGPARAM - regUsed;
                  int dstdisp = fpDisp;
  		for (int nth = 0; nth < regs; nth++) {
                      // fixed Tue Feb  8 20:47:45 JST 2005 (moved before loop)
  		    // int dstdisp = fpDisp;
  		    int regNo = regUsed++;
  		    argv[ai++] = regI32(regNo);
  		    post.add(setI32(framenode(I32, dstdisp), regI32(regNo)));
  debug("*** dstDisp " + dstdisp);
  		    dstdisp += REGWIDTH;
  		    attr.pretend += REGWIDTH;
  		}
  		// set offset
  		SymAuto var = (SymAuto)((LirSymRef)arg.kid(0)).symbol;
  		var.setOffset(fpDisp);
  debug("*** off " + fpDisp);
  		fpDisp += (Type.bytes(arg.type) + 3) & -4;
  		regUsed = MAXREGPARAM;
  	    }
  	} else {
  	    bug();
  	}
      }
  
      debug("total " + ai + " reg-param");
  
      for (/* empty */; i < n; i++) {
  	LirNode arg = node.kid(i);
  	if (Type.tag(arg.type) == Type.INT || Type.tag(arg.type) == Type.FLOAT) { // add set node
  		attr.requireFp = true;
  	    post.add(setExp(arg.type, arg, framenode(arg.type, fpDisp)));
  	    fpDisp += REGWIDTH;
  	    if (Type.bytes(arg.type) > REGWIDTH) { // double
  		fpDisp += 4;
  	    }
  	} else if (Type.tag(arg.type) == Type.AGGREGATE) { // set offset
  	    SymAuto var = (SymAuto)((LirSymRef)arg.kid(0)).symbol;
  	    var.setOffset(fpDisp);
  	    fpDisp += (Type.bytes(arg.type) + 3) & -4;
  	}
      }
  
      // create new prologue
      { 
  	LirNode[] tmp = new LirNode[ai];
  	tmp[0] = argv[0];
  	ai--;
  
  	for (i = 0; i < ai; i++) {
  	    tmp[i + 1] = argv[i + 1];
  	}
  
  	LirNode list = lir.node(Op.PROLOGUE, Type.UNKNOWN, tmp);
  	debug("*** ------> " + list);
  	debug("*** -post-> " + post);
  	return list;
      }
  }
  
  /** Rewrite CALL node. **/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
      LirNode callee = node.kid(0);
      LirNode args = node.kid(1);
  
      int regUsed = 0;
      int spDisp = 0;
      LirNode fval = null;
      boolean smallAggr = false;
  
      debug("*** rewrite call " + node);
  
      if (isComplex(callee)) {
        LirNode copy = func.newTemp(callee.type);
        pre.add(setExp(callee.type, copy, callee));
        node.setKid(0, copy);
      }
  
      LirNode sp = lir.symRef(func.module.globalSymtab.get("%sp"));
      // regnode(I32, "%sp");
      LirNode r0 = regI32(0);
  
      BiList regAssign = new BiList();	// holds assignment to r0-3
  
      // Is aggregate return ?
      if (node.kid(2).nKids() > 0) {
  	fval = node.kid(2).kid(0);
  	//	System.out.println("++++ call rewrite ? " + node.kid(2));
          if (Type.tag(fval.type) == Type.AGGREGATE) {
  	    debug("size " + Type.bytes(fval.type));
  //pnori	    if (Type.bytes(fval.type) > REGWIDTH) { // pass hidden pointer
  		//		System.out.println("++++ call rewrite Aggr");
  		regAssign.add(setI32(r0, fval.kid(0)));
  		regUsed++;
  		// Is it correct ? //##74
  		//		System.out.println(" -> Aggr" + fval);
  //	    } else {		// aggregate return by register
  //		smallAggr = true;
  		//		System.out.println("++++ call rewrite smallAggr" + fval);
  //	    }
  	} else {
  	    //	    System.out.println("++++ call rewrite !Aggr" + fval);
  	}
      }
  
      // parameters
      int n = args.nKids();
      debug("*** args : " + n);
  
      for (int i = 0; i < n; i++) {
          BiList inst = new BiList();
  
  	LirNode arg = args.kid(i);
  	int regNo = regUsed;
  	if (Type.tag(arg.type) == Type.INT) {
  	    if (isComplex(arg)) {
  		LirNode temp = func.newTemp(arg.type);
  		inst.add(setExp(arg.type, temp, arg));
  		arg = temp;
  	    }
  	    if (regUsed < MAXREGPARAM) {
  		regAssign.add(setExp(arg.type, regnode(arg.type, "%r" + regNo), arg));
  		regUsed++;
  	    } else {
  		inst.add(setExp(arg.type, memExp(arg.type, addI32(sp, spDisp)), arg));
  		spDisp += REGWIDTH;
  		// todo: Check the case of char. //##74
  	    }
  
  	} else if (Type.tag(arg.type) == Type.FLOAT) {
  	    if (isComplex(arg)) {
  		LirNode temp = func.newTemp(arg.type);
  		inst.add(setExp(arg.type, temp, arg));
  		arg = temp;
  	    }
  	    int w = Type.bytes(arg.type);
  	    int regs = byte2nreg(Type.bytes(arg.type));
  
  	    if (regUsed + regs <= MAXREGPARAM) {
  		// pass by register
  		// push Fxx
  		inst.add(
  			 lir.node(Op.PARALLEL, Type.UNKNOWN, 
  				  setExp(I32, sp,lir.node(Op.SUB, I32, sp, lir.iconst(I32, w))),
  				  setExp(arg.type, memExp(arg.type, sp), arg)));
  		// pop Ixx
  		if (w > REGWIDTH) {
  		    regAssign.add(lir.node(Op.PARALLEL, Type.UNKNOWN,
  					   setExp(I32, regI32(regNo), memExp(I32, sp)),
  					   setExp(I32, regI32(regNo + 1), memExp(I32, addI32(sp, REGWIDTH))),
  					   setExp(I32, sp, addI32(sp, w))));
  		    regUsed += 2;
  		    regNo += 2;
  		} else {
  		    regAssign.add(lir.node(Op.PARALLEL, Type.UNKNOWN,
  					   setExp(I32, regI32(regNo), memExp(I32, sp)),
  					   setExp(I32, sp, addI32(sp, w))));
  		    regNo++;
  		    regUsed++;
  		}
  
  	    } else if (regUsed < MAXREGPARAM) {
  		// pass by register and stack (w = 8)
  		if (w != 8) {
  		    bug("passing " + w + "bytes != 8 bytes");
  		}
  		inst.add(
  			 lir.node(Op.PARALLEL, Type.UNKNOWN, 
  				  setExp(I32, sp,lir.node(Op.SUB, I32, sp, lir.iconst(I32, w))),
  				  setExp(arg.type, memExp(arg.type, sp), arg)));
  		// load first 4 bytes to reg
  		regAssign.add(setExp(I32, regI32(regNo), memExp(I32, sp)));
  		regAssign.add(setExp(I32, sp, addI32(sp, 8)));
  		regNo++;
  		regUsed++;
  		// store last 4 bytes onto stack
  		inst.add(setExp(arg.type, memExp(arg.type, addI32(sp, spDisp + w - 4)), arg));
  		spDisp += REGWIDTH;
  	    } else {
  		// pass by stack
  		inst.add(setExp(arg.type, memExp(arg.type, addI32(sp, spDisp)), arg));
  		spDisp += w;
  	    }
  
  	} else if (Type.tag(arg.type) == Type.AGGREGATE) {
  	    // Tue Feb 15 19:47:50 JST 2005
  	    if (isComplex(arg)) {
  		SymAuto sym = reserveFrame(".TMP_" + (tmpCnt++), arg.type);
  		LirNode temp = memExp(arg.type, lir.symRef(sym));
  		// checkme
  		inst.add(setExp(arg.type, temp, arg));
  		arg = temp;
  	    }
  	    if (regUsed < MAXREGPARAM) {
  		int regs = byte2nreg(Type.bytes(arg.type));
  		debug("used " + regUsed);
  		debug("aggregate uses " + regs + " regs");
  		
  		if (regUsed + regs <= MAXREGPARAM) {
  		    for (int nth = 0; nth < regs; nth++) {
  			debug(" aggregate arg = " + arg);
  			regAssign.add(setI32(regI32(regUsed++), memI32Base(arg.kid(0), nth * REGWIDTH)));
  		    }
  		} else {    // split on register and stack
  		    regs = MAXREGPARAM - regUsed;
  		    int ty = Type.type(Type.AGGREGATE, (Type.bytes(arg.type) - regs * REGWIDTH) * 8);
  		    // Which is correct for this ? //##74
  		    System.out.println("agg?" + ty);
  		    inst.add(setExp(ty, memExp(ty, addI32(sp, 0)),
  				   memExp(ty, addI32(arg.kid(0), regs * REGWIDTH))));
  		    
  		    for (int nth = 0; nth < regs; nth++) {
  			regAssign.add(setI32(regI32(regUsed + nth), memI32Base(arg.kid(0), nth * REGWIDTH)));
  		    }
  		    // set offset
  		    regUsed = MAXREGPARAM;
  		    if (false) {
  			// this normalize causes serious problem ...
  			spDisp += (Type.bytes(arg.type) + 3) & -4 - regs * REGWIDTH; 
  		    } else {
  			spDisp += Type.bytes(arg.type) - regs * REGWIDTH; 
  		    }
  		}
  	    } else {
  		int ty = Type.type(Type.AGGREGATE, Type.bytes(arg.type) * 8);
  		// checkme
  		    inst.add(setExp(ty, memExp(ty, addI32(sp, spDisp)), memExp(ty, addI32(arg.kid(0), 0))));
  		spDisp += (Type.bytes(arg.type) + 3) & -4; 
  	    }
  //	    pre.addAllFirst(inst);
  
  	} else {
  	    bug();
  	}
  
  	// add this paramter (reverse order)
  	pre.addAllFirst(inst);
      }
  
      pre.addAll(regAssign);
  
      ThumbAttr at = (ThumbAttr)getFunctionAttr(func);
  
      if (spDisp > at.stackRequired) {
  	at.stackRequired = spDisp;
  //	at.requireFp = true;
      }
  
      // make new parameters (on register)
      int m = regUsed < MAXREGPARAM ? regUsed : MAXREGPARAM;
      LirNode[] newargv = new LirNode[m];
      for (int i = 0; i < m; i++) {
  	newargv[i] = regI32(i);
      }
  
      // modified CALL node
      LirNode list;
      try {
  	list = lir.node
  	    (Op.PARALLEL, Type.UNKNOWN, noRescan(lir.operator
  						 (Op.CALL, Type.UNKNOWN,
  						  node.kid(0),
  						  lir.node(Op.LIST, Type.UNKNOWN, newargv),
  						  node.kid(2),
  						  ImList.list())),
  	     lir.decodeLir(new ImList("CLOBBER", regCallClobbers), func, module));
      } catch (SyntaxError e) {
  	throw new CantHappenException();
      }
      
  //    pre.concatenate(assignReg);
  
      // value returned
      if (fval != null) {
  	switch (Type.tag(fval.type)) {
  	case Type.INT: {
  	    LirNode reg = regnode(fval.type, "%r0");
  	    LirNode tmp = func.newTemp(fval.type);
  	    post.add(setExp(fval.type, tmp, reg));
  	    post.add(setExp(fval.type, fval, tmp));
  	    list.kid(0).kid(2).setKid(0, reg);
  	    break; 
  	}
  	case Type.FLOAT: {
  	    LirNode reg = regnode(fval.type, "%f0");
  	    LirNode tmp = func.newTemp(fval.type);
  	    post.add(setExp(fval.type, tmp, reg));
  	    post.add(setExp(fval.type, fval, tmp));
  	    list.kid(0).kid(2).setKid(0, reg);
              break;
          }
  	case Type.AGGREGATE:
  	    if (smallAggr) {
  		LirNode tmp = func.newTemp(I32);
  
  		post.add(setI32(tmp, r0));
  		post.add(setI32(memI32(fval.kid(0)), tmp));
  		list.kid(0).kid(2).setKid(0, r0);
  	    }
  	    break;
  	}
      }
  
      debug("*** stack required by call = " + at.stackRequired);
      debug("*** -pre--> " + pre);
      debug("*** ------> " + list);
      debug("*** -post-> " + post);
      return list;
  }
  
  
  private String load_constant(long val, String reg)
  {
  	String s = "", inst = "";
  	int bits, mask, v, n = 100, i;
  
  	v = (int)val;
  	for (bits = 0, mask = 0xFF; bits <= 32 - 8; bits++, mask <<= 1) {
  		if ((v & ~mask) == 0) {
  			s = "\tmov\t" + reg + ",#" + ((v >> bits) & 0xFF) + "\t@ " + val;
  			i = 1;
  			if (bits > 0) {
  				s += "\n\tlsl\t" + reg + ",#" + bits;
  				i++;
  			}
  			if (i < n) {
  				n = i;
  				inst = s;
  			}
  		}
  	}
  	v = (int)-val;
  	for (bits = 0, mask = 0xFF; bits <= 32 - 8; bits++, mask <<= 1) {
  		if ((v & ~mask) == 0) {
  			s = "\tmov\t" + reg + ",#" + ((v >> bits) & 0xFF) + "\t@ " + val;
  			i = 1;
  			if (bits > 0) {
  				s += "\n\tlsl\t" + reg + ",#" + bits;
  				i++;
  			}
  			s += "\n\tneg\t" + reg + "," + reg;
  			i++;
  			if (i < n) {
  				n = i;
  				inst = s;
  			}
  		}
  	}
  	v = (int)~val;
  	for (bits = 0, mask = 0xFF; bits <= 32 - 8; bits++, mask <<= 1) {
  		if ((v & ~mask) == 0) {
  			s = "\tmov\t" + reg + ",#" + ((v >> bits) & 0xFF) + "\t@ " + val;
  			i = 1;
  			if (bits > 0) {
  				s += "\n\tlsl\t" + reg + ",#" + bits;
  				i++;
  			}
  			s += "\n\tmvn\t" + reg + "," + reg;
  			i++;
  			if (i < n) {
  				n = i;
  				inst = s;
  			}
  		}
  	}
  	v = (int)val & ~0xFF;
  	for (bits = 8, mask = 0xFF00; bits <= 32 - 8; bits++, mask <<= 1) {
  		if ((v & ~mask) == 0) {
  			s = "\tmov\t" + reg + ",#" + ((v >> bits) & 0xFF) + "\t@ " + val;
  			i = 1;
  			if (bits > 0) {
  				s += "\n\tlsl\t" + reg + ",#" + bits;
  				i++;
  			}
  			s += "\n\tadd\t" + reg + ",#" + (val & 0xFF);
  			i++;
  			if (i < n) {
  				n = i;
  				inst = s;
  			}
  		}
  	}
  	if (inst.length() > 0) {
  		return inst;
  	}
  	return "\tldr\t" + reg + ",=W" + val;
  }
  
  
  /*
   * Code building macros.
   */
  
  /* none */
  
  /* Code emission macros.
   *  Patterns not defined below will be converted to:
   *   (foo bar baz) --> foo   bar,baz   or foo(bar,baz)
   */
  
  /* absolute value */
  String jmac1(String x) {
  	if (x.charAt(0) == '-')
  		return x.substring(1);
  	return x;
  }
  
  /* literal */
  String jmac2(String x) {
  	return "=W" + x;
  }
  
  String jmac3(String x, String y) {
  	if (y.charAt(0) == '-') {
  		return load_constant(-Long.parseLong(y.substring(1)), x);
  	} else {
  		return load_constant(Long.parseLong(y), x);
  	}
  }
  
  String jmac4(String x, String y) {
  	return x + "+" + y;
  }
  
  String jmac5(String x)
  {
  	if (x.charAt(0) == '%') {
  		return "_call_via_" + x.substring(1);
  	}
  	return "_call_via_" + x;
  }
  
  String jmac6(String x, String y) {
  	return "[" + x + ",#" + y + "]";
  }
  
  String jmac7(String x, String y) {
  	return "[" + x + "," + y + "]";
  }
  
  String jmac8(String x) {
  	return "[" + x + "]";
  }
  
  String jmac9(String x) { return "#" + x; }
  
  /* special rules */
  String jmac10(String x, String y) {
      return "[" + x + ", #" + y + "]!";
  }
  
  String jmac11(String x, String y) {
      return "{" + x + ", " + y + "}";
  }
  
  String jmac12(String x) {
      return "{" + x + "}";
  }
  
  
  /** Generate prologue sequence. **/
  String jmac13(Object f) {
  	Function func = (Function)f;
  	int size = frameSize(func);
  	ThumbAttr attr = (ThumbAttr) getFunctionAttr(func);
  
  	size += attr.stackRequired;
  	debug("**** defemit prologue: frame size " + size);
  	size = (size + 3) & -4; // round up to 4byte boundary !!
  	// fixme ? (armgcc aligns 8 byte boundary ?)
  
  	String seq = "@ prologue\n";
  	seq += "\tpush\t{%r4,%r5,%r6,%r7,%lr}\n";
  	if (size > 0) {
  		if (size <= 508) {
  			if (attr.requireFp) {
  				seq += "\tmov\t%r7,%sp\n";
  			}
  			seq += "\tsub\t%sp,#" + size + "\n";
  		} else {
  			if (attr.requireFp) {
  				seq += "\tmov\t%ip,%sp\n";
  			}
  			seq += load_constant(-size, "%r7") + "\n";
  			seq += "\tadd\t%sp,%r7\n";
  			if (attr.requireFp) {
  				seq += "\tmov\t%r7,%ip\n";
  			}
  		}
  	} else if (attr.requireFp) {
  		seq += "\tmov\t%r7,%sp\n";
  	}
  	return seq;
  }
  
  /** Generate epilogue sequence. **/
  String jmac14(Object f, String rettype) {
  	boolean interwork = true;
  	Function func = (Function)f;
  	ThumbAttr attr = (ThumbAttr) getFunctionAttr(func);
  	int size = frameSize(func);
  	String seq = "@ epilogue\n";
  
  	size += attr.stackRequired;
  	size = (size + 3) & -4; // round up to 4byte boundary !!
  	if (attr.requireFp) {
  		seq += "\tmov\t%sp,%r7\n";
  	} else if (size > 0) {
  		if (size <= 508) {
  			seq += "\tadd\t%sp,#" + size + "\n";
  		} else {
  			seq += load_constant(size, "%r7") + "\n";
  			seq += "\tadd\t%sp,%r7\n";
  		}
  	}
  	// pop all callee save registers.
  	seq += "\tpop\t{%r4,%r5,%r6,%r7" + (!interwork ? ",%lr" : "") + "}\n";
  	if (interwork) {
  		seq += "\tpop\t{%r1}\n\tbx\t%r1\n";
  	}
  	seq += "\t.ltorg\n";
  	seq += "\t.size\t" + func.symbol.name + ", .-" + func.symbol.name + "\n";// + "@endfunction\n";
  	return seq;
  }
  
  
  String jmac15(String x) { return x + ":"; }
  
  String jmac16(String x) { return "@line " + x; }
  
  void emitComment(PrintWriter out, String comment) {
  	out.println("@ " + comment);
  }
  
  void emitBeginningOfSegment(PrintWriter out, String segment) {
  	out.println("\t.section \"" + segment + "\"");
  }
  
  void emitEndOfSegment(PrintWriter out, String segment) {
  	/* do nothing */
  }
  
  void emitDataLabel(PrintWriter out, String label) {
  	out.println(label + ":");
  }
  
  void emitCodeLabel(PrintWriter out, String label) {
  	out.println("\t.thumb_func");
  	out.println("\t.type\t" + label + ", %function");
  	out.println(label + ":");
  }
  
  
  /** Emit data align **/
  void emitAlign(PrintWriter out, int align) {
  	int n = 0;
  	while ((1 << n) < align) {
  		n++;
  	}
  	// n - 1 < log2(align) < n
  	if (n > 0) {
  		out.println("\t.align\t" + n);
  	}
  }
  
  /** Emit data common **/
  void emitCommon(PrintWriter out, SymStatic symbol, int bytes) {
  	if (symbol.linkage == "LDEF") {
  		out.println("\t.local\t" + symbol.name);
  	}
  	out.println("\t.common\t" + symbol.name + "," + bytes + "," + symbol.boundary);
  }
  
  /** Emit linkage information of symbol */
  void emitLinkage(PrintWriter out, SymStatic symbol) {
  	if (symbol.linkage == "XDEF") {
  		out.println("\t.global\t" + symbol.name);
  	}
  }
  
  
  /** Emit data zeros **/
  void emitZeros(PrintWriter out, int bytes) {
  	if (bytes > 0) {
  		out.println("\t.skip\t" + bytes);
  	}
  }
  
  
  /** Emit data **/
  void emitData(PrintWriter out, int type, LirNode node) {
  	if (type == I32) {
  		out.println("\t.word\t" + lexpConv.convert(node));
  	}
  	else if (type == I16) {
  		out.println("\t.short\t" + ((LirIconst)node).signedValue());
  	}
  	else if (type == I8) {
  		out.println("\t.byte\t" + ((LirIconst)node).signedValue());
  	}
  	else if (type == F64) {
  		double value = ((LirFconst)node).value;
  		long bits = Double.doubleToLongBits(value);
  		out.println("\t.long\t0x" + Long.toString((bits >> 32) & 0xffffffffL, 16) + " @ " + value);
  		out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16));
  	}
  	else if (type == F32) {
  		double value = ((LirFconst)node).value;
  		long bits = Float.floatToIntBits((float)value);
  		out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16) + " @ " + value);
  	}
  	else {
  		throw new CantHappenException("unknown type: " + type);
  	}
  }
  
  
  boolean isSp(LirNode node)
  {
  	boolean f = false;
  
  	Symbol vfp = func.module.globalSymtab.get("%sp");
  	if (node instanceof LirSymRef) {
  		Symbol sym = ((LirSymRef)node).symbol;
  		f = (sym == vfp);
  	}
  //	System.out.println("!!! isSp(): " + f + " " + node.toString());
  	return f;
  }
  
  
  boolean isFp(LirNode node)
  {
  	boolean f = false;
  
  	Symbol vfp = func.module.globalSymtab.get("%r7");
  	if (node instanceof LirSymRef) {
  		Symbol sym = ((LirSymRef)node).symbol;
  		f = (sym == vfp);
  	}
  //	System.out.println("!!! isSp(): " + f + " " + node.toString());
  	return f;
  }
  
  
  void setAllocaUsed()
  {
  	ThumbAttr attr = (ThumbAttr)getFunctionAttr(func);
  	attr.allocaUsed = true;
  	attr.requireFp = true;
  }
}
