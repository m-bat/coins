/*
Productions:
 1: regq -> (REG I64)
 2: regq -> (SUBREG I64)
 3: regl -> (REG I32)
 4: regl -> (SUBREG I32)
 5: regw -> (REG I16)
 6: regw -> (SUBREG I16)
 7: regb -> (REG I8)
 8: regb -> (SUBREG I8)
 9: regd -> (REG F64)
 10: regd -> (SUBREG F64)
 11: regf -> (REG F32)
 12: regf -> (SUBREG F32)
 13: con05 -> (INTCONST _)
 14: con08 -> (INTCONST _)
 15: con16 -> (INTCONST _)
 16: con64 -> (INTCONST _)
 17: rcs -> con05
 18: rcs -> regq
 19: rcs -> regl
 20: rbv -> con08
 21: rbv -> regq
 22: rbv -> regl
 23: addr -> con16
 24: addr -> regq
 25: addr -> regl
 26: addr -> (ADD I64 regq con16)
 27: addr -> (ADD I64 regl con16)
 28: sta -> (STATIC I64)
 29: label -> (LABEL _)
 30: reg64 -> regq
 31: reg64 -> regl
 32: reg64f -> regd
 33: reg64f -> regf
 34: fun -> addr
 35: fun -> asmcon
 36: asmcon -> con64
 37: asmcon -> sta
 38: asmcon -> (ADD I64 asmcon con64)
 39: asmcon -> (SUB I64 asmcon con64)
 40: regq -> (ADD I64 regq rbv)
 41: regq -> (SUB I64 regq rbv)
 42: regq -> (BAND I64 regq rbv)
 43: regq -> (BOR I64 regq rbv)
 44: regq -> (BXOR I64 regq rbv)
 45: regl -> (ADD I32 regl rbv)
 46: regl -> (SUB I32 regl rbv)
 47: regl -> (BAND I32 regl rbv)
 48: regl -> (BOR I32 regl rbv)
 49: regl -> (BXOR I32 regl rbv)
 50: regq -> (BNOT I64 regq)
 51: regl -> (BNOT I32 regl)
 52: regq -> (NEG I64 regq)
 53: regl -> (NEG I32 regl)
 54: regq -> (MUL I64 regq rbv)
 55: regq -> (DIVS I64 regq rbv)
 56: regl -> (MUL I32 regl rbv)
 57: regl -> (DIVS I32 regl rbv)
 58: regl -> (MUL I32 regq rbv)
 59: regl -> (DIVS I32 regq rbv)
 60: regq -> (ADD I64 regl con16)
 61: regq -> (ADD I64 regl rbv)
 62: _1 -> (INTCONST I32 2)
 63: _2 -> (LSHS I32 reg64 _1)
 64: regq -> (ADD I64 regq _2)
 65: _3 -> (INTCONST I32 3)
 66: _4 -> (LSHS I32 reg64 _3)
 67: regq -> (ADD I64 regq _4)
 68: regq -> (SUB I64 regq _2)
 69: regq -> (SUB I64 regq _4)
 70: _5 -> (INTCONST I64 2)
 71: _6 -> (LSHS I64 reg64 _5)
 72: regq -> (ADD I64 regq _6)
 73: _7 -> (INTCONST I64 3)
 74: _8 -> (LSHS I64 reg64 _7)
 75: regq -> (ADD I64 regq _8)
 76: regq -> (SUB I64 regq _6)
 77: regq -> (SUB I64 regq _8)
 78: regl -> (ADD I32 regl _2)
 79: regl -> (ADD I32 regl _4)
 80: regl -> (SUB I32 regl _2)
 81: regl -> (SUB I32 regl _4)
 82: regq -> (RSHS I64 regq rcs)
 83: regq -> (LSHS I64 regq rcs)
 84: regl -> (RSHS I32 regl rcs)
 85: regl -> (LSHS I32 regl rcs)
 86: regl -> (RSHS I32 regq rcs)
 87: regl -> (LSHS I32 regq rcs)
 88: regl -> (RSHU I32 regl rcs)
 89: regq -> (RSHU I64 regq rcs)
 90: regq -> (MODS I64 regq rbv)
 91: regq -> (MODU I64 regq rbv)
 92: regl -> (MODS I32 regl rbv)
 93: regl -> (MODU I32 regl rbv)
 94: _9 -> (MEM I64 addr)
 95: void -> (SET I64 _9 regq)
 96: _10 -> (MEM I32 addr)
 97: void -> (SET I32 _10 regl)
 98: _11 -> (MEM I16 addr)
 99: void -> (SET I16 _11 regw)
 100: _12 -> (MEM I8 addr)
 101: void -> (SET I8 _12 regb)
 102: _13 -> (MEM F64 addr)
 103: void -> (SET F64 _13 regd)
 104: _14 -> (MEM F32 addr)
 105: void -> (SET F32 _14 regf)
 106: void -> (LIST _ regq _9)
 107: void -> (LIST _ regl _10)
 108: void -> (LIST _ regw _11)
 109: void -> (LIST _ regb _12)
 110: void -> (LIST _ regd _13)
 111: void -> (LIST _ regf _14)
 112: regq -> (MEM I64 addr)
 113: regl -> (MEM I32 addr)
 114: regw -> (MEM I16 addr)
 115: regb -> (MEM I8 addr)
 116: regd -> (MEM F64 addr)
 117: regf -> (MEM F32 addr)
 118: regl -> (CONVSX I32 _11)
 119: regl -> (CONVZX I32 _11)
 120: regl -> (CONVSX I32 _12)
 121: regl -> (CONVZX I32 _12)
 122: void -> (JUMP _ label)
 123: _15 -> (TSTNE I64 reg64 rbv)
 124: void -> (JUMPC _ _15 label label)
 125: _16 -> (TSTNE I32 reg64 rbv)
 126: void -> (JUMPC _ _16 label label)
 127: _17 -> (TSTLTS I64 reg64 rbv)
 128: void -> (JUMPC _ _17 label label)
 129: _18 -> (TSTLES I64 reg64 rbv)
 130: void -> (JUMPC _ _18 label label)
 131: _19 -> (TSTEQ I64 reg64 rbv)
 132: void -> (JUMPC _ _19 label label)
 133: _20 -> (TSTLTU I64 reg64 rbv)
 134: void -> (JUMPC _ _20 label label)
 135: _21 -> (TSTLEU I64 reg64 rbv)
 136: void -> (JUMPC _ _21 label label)
 137: _22 -> (TSTLTS I64 rbv reg64)
 138: void -> (JUMPC _ _22 label label)
 139: _23 -> (TSTLES I64 rbv reg64)
 140: void -> (JUMPC _ _23 label label)
 141: _24 -> (TSTEQ I64 rbv reg64)
 142: void -> (JUMPC _ _24 label label)
 143: _25 -> (TSTLTU I64 rbv reg64)
 144: void -> (JUMPC _ _25 label label)
 145: _26 -> (TSTLEU I64 rbv reg64)
 146: void -> (JUMPC _ _26 label label)
 147: _27 -> (TSTGTS I64 rbv reg64)
 148: void -> (JUMPC _ _27 label label)
 149: _28 -> (TSTGES I64 rbv reg64)
 150: void -> (JUMPC _ _28 label label)
 151: _29 -> (TSTGTU I64 rbv reg64)
 152: void -> (JUMPC _ _29 label label)
 153: _30 -> (TSTGEU I64 rbv reg64)
 154: void -> (JUMPC _ _30 label label)
 155: _31 -> (TSTGTS I64 reg64 rbv)
 156: void -> (JUMPC _ _31 label label)
 157: _32 -> (TSTGES I64 reg64 rbv)
 158: void -> (JUMPC _ _32 label label)
 159: _33 -> (TSTGTU I64 reg64 rbv)
 160: void -> (JUMPC _ _33 label label)
 161: _34 -> (TSTGEU I64 reg64 rbv)
 162: void -> (JUMPC _ _34 label label)
 163: _35 -> (TSTNE I64 reg64f reg64f)
 164: void -> (JUMPC _ _35 label label)
 165: _36 -> (TSTLTS I64 reg64f reg64f)
 166: void -> (JUMPC _ _36 label label)
 167: _37 -> (TSTLES I64 reg64f reg64f)
 168: void -> (JUMPC _ _37 label label)
 169: _38 -> (TSTEQ I64 reg64f reg64f)
 170: void -> (JUMPC _ _38 label label)
 171: _39 -> (TSTGTS I64 reg64f reg64f)
 172: void -> (JUMPC _ _39 label label)
 173: _40 -> (TSTGES I64 reg64f reg64f)
 174: void -> (JUMPC _ _40 label label)
 175: regd -> (ADD F64 regd regd)
 176: regd -> (SUB F64 regd regd)
 177: regd -> (MUL F64 regd regd)
 178: regd -> (DIVS F64 regd regd)
 179: regf -> (ADD F32 regf regf)
 180: regf -> (SUB F32 regf regf)
 181: regf -> (MUL F32 regf regf)
 182: regf -> (DIVS F32 regf regf)
 183: regd -> (NEG F64 regd)
 184: regf -> (NEG F32 regf)
 185: regq -> con08
 186: regq -> con16
 187: regq -> con64
 188: regq -> sta
 189: regl -> con08
 190: regl -> con16
 191: regl -> con64
 192: regl -> sta
 193: regw -> con08
 194: regw -> con16
 195: regw -> con64
 196: regw -> sta
 197: regb -> con08
 198: regb -> con16
 199: regb -> con64
 200: regb -> sta
 201: regd -> (FLOATCONST F64 0.0)
 202: regf -> (FLOATCONST F32 0.0)
 203: void -> (SET I64 regq regq)
 204: void -> (SET I32 regl regl)
 205: void -> (SET I16 regw regw)
 206: void -> (SET I8 regb regb)
 207: void -> (SET I64 regl regl)
 208: void -> (SET I32 regq regq)
 209: void -> (SET I64 regl regq)
 210: void -> (SET I32 regq regl)
 211: void -> (SET I32 regl regq)
 212: void -> (SET I8 regb regq)
 213: void -> (SET F64 regd regd)
 214: void -> (SET F32 regf regf)
 215: regq -> (CONVZX I64 regl)
 216: regl -> (CONVZX I32 regw)
 217: regl -> (CONVZX I32 regb)
 218: regq -> (CONVSX I64 regl)
 219: regl -> (CONVSX I32 regw)
 220: regq -> (CONVSX I64 regb)
 221: regl -> (CONVSX I32 regb)
 222: regw -> (CONVSX I16 regb)
 223: regl -> (CONVIT I32 regq)
 224: regw -> (CONVIT I16 regq)
 225: regb -> (CONVIT I8 regq)
 226: regw -> (CONVIT I16 regl)
 227: regb -> (CONVIT I8 regl)
 228: regb -> (CONVIT I8 regw)
 229: regd -> (CONVSF F64 regq)
 230: regf -> (CONVSF F32 regq)
 231: regd -> (CONVSF F64 regl)
 232: regf -> (CONVSF F32 regl)
 233: regd -> (CONVFX F64 regf)
 234: regf -> (CONVFT F32 regd)
 235: regq -> (CONVFS I64 regd)
 236: regq -> (CONVFS I64 regf)
 237: regl -> (CONVFS I32 regd)
 238: regl -> (CONVFS I32 regf)
 239: void -> (CALL _ fun)
 240: void -> (CALL _ fun)
 241: void -> (PARALLEL _ void)
*/
/*
Sorted Productions:
 18: rcs -> regq
 21: rbv -> regq
 24: addr -> regq
 30: reg64 -> regq
 19: rcs -> regl
 22: rbv -> regl
 25: addr -> regl
 31: reg64 -> regl
 32: reg64f -> regd
 33: reg64f -> regf
 17: rcs -> con05
 20: rbv -> con08
 185: regq -> con08
 189: regl -> con08
 193: regw -> con08
 197: regb -> con08
 23: addr -> con16
 186: regq -> con16
 190: regl -> con16
 194: regw -> con16
 198: regb -> con16
 36: asmcon -> con64
 187: regq -> con64
 191: regl -> con64
 195: regw -> con64
 199: regb -> con64
 34: fun -> addr
 37: asmcon -> sta
 188: regq -> sta
 192: regl -> sta
 196: regw -> sta
 200: regb -> sta
 35: fun -> asmcon
 13: con05 -> (INTCONST _)
 14: con08 -> (INTCONST _)
 15: con16 -> (INTCONST _)
 16: con64 -> (INTCONST _)
 62: _1 -> (INTCONST I32 2)
 65: _3 -> (INTCONST I32 3)
 70: _5 -> (INTCONST I64 2)
 73: _7 -> (INTCONST I64 3)
 202: regf -> (FLOATCONST F32 0.0)
 201: regd -> (FLOATCONST F64 0.0)
 28: sta -> (STATIC I64)
 7: regb -> (REG I8)
 5: regw -> (REG I16)
 3: regl -> (REG I32)
 11: regf -> (REG F32)
 1: regq -> (REG I64)
 9: regd -> (REG F64)
 8: regb -> (SUBREG I8)
 6: regw -> (SUBREG I16)
 4: regl -> (SUBREG I32)
 12: regf -> (SUBREG F32)
 2: regq -> (SUBREG I64)
 10: regd -> (SUBREG F64)
 29: label -> (LABEL _)
 53: regl -> (NEG I32 regl)
 184: regf -> (NEG F32 regf)
 52: regq -> (NEG I64 regq)
 183: regd -> (NEG F64 regd)
 45: regl -> (ADD I32 regl rbv)
 78: regl -> (ADD I32 regl _2)
 79: regl -> (ADD I32 regl _4)
 179: regf -> (ADD F32 regf regf)
 26: addr -> (ADD I64 regq con16)
 27: addr -> (ADD I64 regl con16)
 38: asmcon -> (ADD I64 asmcon con64)
 40: regq -> (ADD I64 regq rbv)
 60: regq -> (ADD I64 regl con16)
 61: regq -> (ADD I64 regl rbv)
 64: regq -> (ADD I64 regq _2)
 67: regq -> (ADD I64 regq _4)
 72: regq -> (ADD I64 regq _6)
 75: regq -> (ADD I64 regq _8)
 175: regd -> (ADD F64 regd regd)
 46: regl -> (SUB I32 regl rbv)
 80: regl -> (SUB I32 regl _2)
 81: regl -> (SUB I32 regl _4)
 180: regf -> (SUB F32 regf regf)
 39: asmcon -> (SUB I64 asmcon con64)
 41: regq -> (SUB I64 regq rbv)
 68: regq -> (SUB I64 regq _2)
 69: regq -> (SUB I64 regq _4)
 76: regq -> (SUB I64 regq _6)
 77: regq -> (SUB I64 regq _8)
 176: regd -> (SUB F64 regd regd)
 56: regl -> (MUL I32 regl rbv)
 58: regl -> (MUL I32 regq rbv)
 181: regf -> (MUL F32 regf regf)
 54: regq -> (MUL I64 regq rbv)
 177: regd -> (MUL F64 regd regd)
 57: regl -> (DIVS I32 regl rbv)
 59: regl -> (DIVS I32 regq rbv)
 182: regf -> (DIVS F32 regf regf)
 55: regq -> (DIVS I64 regq rbv)
 178: regd -> (DIVS F64 regd regd)
 92: regl -> (MODS I32 regl rbv)
 90: regq -> (MODS I64 regq rbv)
 93: regl -> (MODU I32 regl rbv)
 91: regq -> (MODU I64 regq rbv)
 222: regw -> (CONVSX I16 regb)
 118: regl -> (CONVSX I32 _11)
 120: regl -> (CONVSX I32 _12)
 219: regl -> (CONVSX I32 regw)
 221: regl -> (CONVSX I32 regb)
 218: regq -> (CONVSX I64 regl)
 220: regq -> (CONVSX I64 regb)
 119: regl -> (CONVZX I32 _11)
 121: regl -> (CONVZX I32 _12)
 216: regl -> (CONVZX I32 regw)
 217: regl -> (CONVZX I32 regb)
 215: regq -> (CONVZX I64 regl)
 225: regb -> (CONVIT I8 regq)
 227: regb -> (CONVIT I8 regl)
 228: regb -> (CONVIT I8 regw)
 224: regw -> (CONVIT I16 regq)
 226: regw -> (CONVIT I16 regl)
 223: regl -> (CONVIT I32 regq)
 233: regd -> (CONVFX F64 regf)
 234: regf -> (CONVFT F32 regd)
 237: regl -> (CONVFS I32 regd)
 238: regl -> (CONVFS I32 regf)
 235: regq -> (CONVFS I64 regd)
 236: regq -> (CONVFS I64 regf)
 230: regf -> (CONVSF F32 regq)
 232: regf -> (CONVSF F32 regl)
 229: regd -> (CONVSF F64 regq)
 231: regd -> (CONVSF F64 regl)
 47: regl -> (BAND I32 regl rbv)
 42: regq -> (BAND I64 regq rbv)
 48: regl -> (BOR I32 regl rbv)
 43: regq -> (BOR I64 regq rbv)
 49: regl -> (BXOR I32 regl rbv)
 44: regq -> (BXOR I64 regq rbv)
 51: regl -> (BNOT I32 regl)
 50: regq -> (BNOT I64 regq)
 63: _2 -> (LSHS I32 reg64 _1)
 66: _4 -> (LSHS I32 reg64 _3)
 85: regl -> (LSHS I32 regl rcs)
 87: regl -> (LSHS I32 regq rcs)
 71: _6 -> (LSHS I64 reg64 _5)
 74: _8 -> (LSHS I64 reg64 _7)
 83: regq -> (LSHS I64 regq rcs)
 84: regl -> (RSHS I32 regl rcs)
 86: regl -> (RSHS I32 regq rcs)
 82: regq -> (RSHS I64 regq rcs)
 88: regl -> (RSHU I32 regl rcs)
 89: regq -> (RSHU I64 regq rcs)
 131: _19 -> (TSTEQ I64 reg64 rbv)
 141: _24 -> (TSTEQ I64 rbv reg64)
 169: _38 -> (TSTEQ I64 reg64f reg64f)
 125: _16 -> (TSTNE I32 reg64 rbv)
 123: _15 -> (TSTNE I64 reg64 rbv)
 163: _35 -> (TSTNE I64 reg64f reg64f)
 127: _17 -> (TSTLTS I64 reg64 rbv)
 137: _22 -> (TSTLTS I64 rbv reg64)
 165: _36 -> (TSTLTS I64 reg64f reg64f)
 129: _18 -> (TSTLES I64 reg64 rbv)
 139: _23 -> (TSTLES I64 rbv reg64)
 167: _37 -> (TSTLES I64 reg64f reg64f)
 147: _27 -> (TSTGTS I64 rbv reg64)
 155: _31 -> (TSTGTS I64 reg64 rbv)
 171: _39 -> (TSTGTS I64 reg64f reg64f)
 149: _28 -> (TSTGES I64 rbv reg64)
 157: _32 -> (TSTGES I64 reg64 rbv)
 173: _40 -> (TSTGES I64 reg64f reg64f)
 133: _20 -> (TSTLTU I64 reg64 rbv)
 143: _25 -> (TSTLTU I64 rbv reg64)
 135: _21 -> (TSTLEU I64 reg64 rbv)
 145: _26 -> (TSTLEU I64 rbv reg64)
 151: _29 -> (TSTGTU I64 rbv reg64)
 159: _33 -> (TSTGTU I64 reg64 rbv)
 153: _30 -> (TSTGEU I64 rbv reg64)
 161: _34 -> (TSTGEU I64 reg64 rbv)
 100: _12 -> (MEM I8 addr)
 115: regb -> (MEM I8 addr)
 98: _11 -> (MEM I16 addr)
 114: regw -> (MEM I16 addr)
 96: _10 -> (MEM I32 addr)
 113: regl -> (MEM I32 addr)
 104: _14 -> (MEM F32 addr)
 117: regf -> (MEM F32 addr)
 94: _9 -> (MEM I64 addr)
 112: regq -> (MEM I64 addr)
 102: _13 -> (MEM F64 addr)
 116: regd -> (MEM F64 addr)
 101: void -> (SET I8 _12 regb)
 206: void -> (SET I8 regb regb)
 212: void -> (SET I8 regb regq)
 99: void -> (SET I16 _11 regw)
 205: void -> (SET I16 regw regw)
 97: void -> (SET I32 _10 regl)
 204: void -> (SET I32 regl regl)
 208: void -> (SET I32 regq regq)
 210: void -> (SET I32 regq regl)
 211: void -> (SET I32 regl regq)
 105: void -> (SET F32 _14 regf)
 214: void -> (SET F32 regf regf)
 95: void -> (SET I64 _9 regq)
 203: void -> (SET I64 regq regq)
 207: void -> (SET I64 regl regl)
 209: void -> (SET I64 regl regq)
 103: void -> (SET F64 _13 regd)
 213: void -> (SET F64 regd regd)
 122: void -> (JUMP _ label)
 124: void -> (JUMPC _ _15 label label)
 126: void -> (JUMPC _ _16 label label)
 128: void -> (JUMPC _ _17 label label)
 130: void -> (JUMPC _ _18 label label)
 132: void -> (JUMPC _ _19 label label)
 134: void -> (JUMPC _ _20 label label)
 136: void -> (JUMPC _ _21 label label)
 138: void -> (JUMPC _ _22 label label)
 140: void -> (JUMPC _ _23 label label)
 142: void -> (JUMPC _ _24 label label)
 144: void -> (JUMPC _ _25 label label)
 146: void -> (JUMPC _ _26 label label)
 148: void -> (JUMPC _ _27 label label)
 150: void -> (JUMPC _ _28 label label)
 152: void -> (JUMPC _ _29 label label)
 154: void -> (JUMPC _ _30 label label)
 156: void -> (JUMPC _ _31 label label)
 158: void -> (JUMPC _ _32 label label)
 160: void -> (JUMPC _ _33 label label)
 162: void -> (JUMPC _ _34 label label)
 164: void -> (JUMPC _ _35 label label)
 166: void -> (JUMPC _ _36 label label)
 168: void -> (JUMPC _ _37 label label)
 170: void -> (JUMPC _ _38 label label)
 172: void -> (JUMPC _ _39 label label)
 174: void -> (JUMPC _ _40 label label)
 239: void -> (CALL _ fun)
 240: void -> (CALL _ fun)
 241: void -> (PARALLEL _ void)
 106: void -> (LIST _ regq _9)
 107: void -> (LIST _ regl _10)
 108: void -> (LIST _ regw _11)
 109: void -> (LIST _ regb _12)
 110: void -> (LIST _ regd _13)
 111: void -> (LIST _ regf _14)
*/
/*
Productions:
 1: _rewr -> (PROLOGUE _)
 2: _rewr -> (EPILOGUE _)
 3: _rewr -> (CALL _)
 4: _rewr -> (JUMPN _)
 5: _rewr -> (SET _)
 6: _rewr -> (FLOATCONST _ 0.0)
 7: _rewr -> (FLOATCONST F64)
 8: _rewr -> (FLOATCONST F32)
*/
/*
Sorted Productions:
 6: _rewr -> (FLOATCONST _ 0.0)
 8: _rewr -> (FLOATCONST F32)
 7: _rewr -> (FLOATCONST F64)
 5: _rewr -> (SET _)
 4: _rewr -> (JUMPN _)
 3: _rewr -> (CALL _)
 1: _rewr -> (PROLOGUE _)
 2: _rewr -> (EPILOGUE _)
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

/*************************************************************************/
/* Target machine description for Alpha, alpha.tmd since 2005-08-24      */
/* Ver 0.6.2 update 2007-01-12 by Tomohiro TSUKAMOTO and Kenji KISE      */

import coins.backend.sym.Label;
import coins.backend.Storage;
import coins.backend.Data;
import coins.backend.LocalTransformer;
import coins.backend.Transformer;
import coins.backend.util.BitMapSet;
import coins.backend.util.NumberSet;
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirLabelRef;
import coins.backend.ana.SaveRegisters;
import coins.Version;  //##76
import java.lang.System;
import java.lang.Integer;
import java.util.*;

/*************************************************************************/



public class CodeGenerator_alpha extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_alpha() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 2;
    static final int NRULES = 8 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
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
      case Op.FLOATCONST:
        if (((LirFconst)t).value == 0.0)  return null;
        if (t.type == 516) {
          if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 516, lir.node(Op.STATIC, 1026, module.constToData(t)));
          }
        }
        if (t.type == 1028) {
          if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 1028, lir.node(Op.STATIC, 1026, module.constToData(t)));
          }
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
    static final int NNONTERM = 61;
    static final int NRULES = 241 + 1;
    static final int START_NT = 7;

    static final int NT__ = 0;
    static final int NT_regq = 1;
    static final int NT_regl = 2;
    static final int NT_regw = 3;
    static final int NT_regb = 4;
    static final int NT_regd = 5;
    static final int NT_regf = 6;
    static final int NT_void = 7;
    static final int NT_con05 = 8;
    static final int NT_con08 = 9;
    static final int NT_con16 = 10;
    static final int NT_con64 = 11;
    static final int NT_rcs = 12;
    static final int NT_rbv = 13;
    static final int NT_addr = 14;
    static final int NT_sta = 15;
    static final int NT_label = 16;
    static final int NT_reg64 = 17;
    static final int NT_reg64f = 18;
    static final int NT_fun = 19;
    static final int NT_asmcon = 20;
    static final int NT__1 = 21;
    static final int NT__2 = 22;
    static final int NT__3 = 23;
    static final int NT__4 = 24;
    static final int NT__5 = 25;
    static final int NT__6 = 26;
    static final int NT__7 = 27;
    static final int NT__8 = 28;
    static final int NT__9 = 29;
    static final int NT__10 = 30;
    static final int NT__11 = 31;
    static final int NT__12 = 32;
    static final int NT__13 = 33;
    static final int NT__14 = 34;
    static final int NT__15 = 35;
    static final int NT__16 = 36;
    static final int NT__17 = 37;
    static final int NT__18 = 38;
    static final int NT__19 = 39;
    static final int NT__20 = 40;
    static final int NT__21 = 41;
    static final int NT__22 = 42;
    static final int NT__23 = 43;
    static final int NT__24 = 44;
    static final int NT__25 = 45;
    static final int NT__26 = 46;
    static final int NT__27 = 47;
    static final int NT__28 = 48;
    static final int NT__29 = 49;
    static final int NT__30 = 50;
    static final int NT__31 = 51;
    static final int NT__32 = 52;
    static final int NT__33 = 53;
    static final int NT__34 = 54;
    static final int NT__35 = 55;
    static final int NT__36 = 56;
    static final int NT__37 = 57;
    static final int NT__38 = 58;
    static final int NT__39 = 59;
    static final int NT__40 = 60;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regq: return "regq";
      case NT_regl: return "regl";
      case NT_regw: return "regw";
      case NT_regb: return "regb";
      case NT_regd: return "regd";
      case NT_regf: return "regf";
      case NT_void: return "void";
      case NT_con05: return "con05";
      case NT_con08: return "con08";
      case NT_con16: return "con16";
      case NT_con64: return "con64";
      case NT_rcs: return "rcs";
      case NT_rbv: return "rbv";
      case NT_addr: return "addr";
      case NT_sta: return "sta";
      case NT_label: return "label";
      case NT_reg64: return "reg64";
      case NT_reg64f: return "reg64f";
      case NT_fun: return "fun";
      case NT_asmcon: return "asmcon";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
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
        case NT_regq:
          record(NT_rcs, 0 + cost1, 0 + cost2, 18);
          record(NT_rbv, 0 + cost1, 0 + cost2, 21);
          record(NT_addr, 0 + cost1, 0 + cost2, 24);
          record(NT_reg64, 0 + cost1, 0 + cost2, 30);
          break;
        case NT_regl:
          record(NT_rcs, 0 + cost1, 0 + cost2, 19);
          record(NT_rbv, 0 + cost1, 0 + cost2, 22);
          record(NT_addr, 0 + cost1, 0 + cost2, 25);
          record(NT_reg64, 0 + cost1, 0 + cost2, 31);
          break;
        case NT_regd:
          record(NT_reg64f, 0 + cost1, 0 + cost2, 32);
          break;
        case NT_regf:
          record(NT_reg64f, 0 + cost1, 0 + cost2, 33);
          break;
        case NT_con05:
          record(NT_rcs, 0 + cost1, 0 + cost2, 17);
          break;
        case NT_con08:
          record(NT_rbv, 0 + cost1, 0 + cost2, 20);
          record(NT_regq, 1 + cost1, 1 + cost2, 185);
          record(NT_regl, 1 + cost1, 1 + cost2, 189);
          record(NT_regw, 1 + cost1, 1 + cost2, 193);
          record(NT_regb, 1 + cost1, 1 + cost2, 197);
          break;
        case NT_con16:
          record(NT_addr, 0 + cost1, 0 + cost2, 23);
          record(NT_regq, 1 + cost1, 1 + cost2, 186);
          record(NT_regl, 1 + cost1, 1 + cost2, 190);
          record(NT_regw, 1 + cost1, 1 + cost2, 194);
          record(NT_regb, 1 + cost1, 1 + cost2, 198);
          break;
        case NT_con64:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 36);
          record(NT_regq, 1 + cost1, 1 + cost2, 187);
          record(NT_regl, 1 + cost1, 1 + cost2, 191);
          record(NT_regw, 1 + cost1, 1 + cost2, 195);
          record(NT_regb, 1 + cost1, 1 + cost2, 199);
          break;
        case NT_addr:
          record(NT_fun, 0 + cost1, 0 + cost2, 34);
          break;
        case NT_sta:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 37);
          record(NT_regq, 1 + cost1, 1 + cost2, 188);
          record(NT_regl, 1 + cost1, 1 + cost2, 192);
          record(NT_regw, 1 + cost1, 1 + cost2, 196);
          record(NT_regb, 1 + cost1, 1 + cost2, 200);
          break;
        case NT_asmcon:
          record(NT_fun, 0 + cost1, 0 + cost2, 35);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        if (isConstU((LirIconst)t,  5)) record(NT_con05, 0, 0, 13);
        if (isConstU((LirIconst)t,  8)) record(NT_con08, 0, 0, 14);
        if (isConstS((LirIconst)t, 16)) record(NT_con16, 0, 0, 15);
        record(NT_con64, 0, 0, 16);
        if (t.type == 514) {
          if (((LirIconst)t).value == 2) record(NT__1, 0, 0, 62);
          if (((LirIconst)t).value == 3) record(NT__3, 0, 0, 65);
        }
        if (t.type == 1026) {
          if (((LirIconst)t).value == 2) record(NT__5, 0, 0, 70);
          if (((LirIconst)t).value == 3) record(NT__7, 0, 0, 73);
        }
        break;
      case Op.FLOATCONST:
        if (t.type == 516) {
          if (((LirFconst)t).value == 0.0) record(NT_regf, 1, 1, 202);
        }
        if (t.type == 1028) {
          if (((LirFconst)t).value == 0.0) record(NT_regd, 1, 1, 201);
        }
        break;
      case Op.STATIC:
        if (t.type == 1026) {
          record(NT_sta, 0, 0, 28);
        }
        break;
      case Op.REG:
        if (t.type == 130) {
          record(NT_regb, 0, 0, 7);
        }
        if (t.type == 258) {
          record(NT_regw, 0, 0, 5);
        }
        if (t.type == 514) {
          record(NT_regl, 0, 0, 3);
        }
        if (t.type == 516) {
          record(NT_regf, 0, 0, 11);
        }
        if (t.type == 1026) {
          record(NT_regq, 0, 0, 1);
        }
        if (t.type == 1028) {
          record(NT_regd, 0, 0, 9);
        }
        break;
      case Op.SUBREG:
        if (t.type == 130) {
          record(NT_regb, 0, 0, 8);
        }
        if (t.type == 258) {
          record(NT_regw, 0, 0, 6);
        }
        if (t.type == 514) {
          record(NT_regl, 0, 0, 4);
        }
        if (t.type == 516) {
          record(NT_regf, 0, 0, 12);
        }
        if (t.type == 1026) {
          record(NT_regq, 0, 0, 2);
        }
        if (t.type == 1028) {
          record(NT_regd, 0, 0, 10);
        }
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 29);
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 53);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 184);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 52);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 183);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 45);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__2] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__2], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__2], 78);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 79);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 179);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con16] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con16], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con16], 26);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con16] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con16], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con16], 27);
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con64] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con64], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con64], 38);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 40);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con16] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con16], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con16], 60);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 61);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__2] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__2], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__2], 64);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__4] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__4], 67);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__6] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__6], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__6], 72);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__8] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__8], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__8], 75);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 175);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 46);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__2] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__2], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__2], 80);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 81);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 180);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con64] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con64], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con64], 39);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 41);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__2] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__2], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__2], 68);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__4] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__4], 69);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__6] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__6], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__6], 76);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__8] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__8], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__8], 77);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 176);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 3 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 3 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 56);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 3 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 3 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 58);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 181);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 3 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 54);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 177);
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 3 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 3 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 57);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 3 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 3 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 59);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 182);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 3 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 55);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 178);
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 92);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 90);
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 93);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 91);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 222);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT__11] != 0) record(NT_regl, 1 + kids[0].cost1[NT__11], 1 + kids[0].cost2[NT__11], 118);
          if (kids[0].rule[NT__12] != 0) record(NT_regl, 1 + kids[0].cost1[NT__12], 1 + kids[0].cost2[NT__12], 120);
          if (kids[0].rule[NT_regw] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 219);
          if (kids[0].rule[NT_regb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 221);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regq, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 218);
          if (kids[0].rule[NT_regb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 220);
        }
        break;
      case Op.CONVZX:
        if (t.type == 514) {
          if (kids[0].rule[NT__11] != 0) record(NT_regl, 1 + kids[0].cost1[NT__11], 1 + kids[0].cost2[NT__11], 119);
          if (kids[0].rule[NT__12] != 0) record(NT_regl, 1 + kids[0].cost1[NT__12], 1 + kids[0].cost2[NT__12], 121);
          if (kids[0].rule[NT_regw] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 216);
          if (kids[0].rule[NT_regb] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 217);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 215);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 225);
          if (kids[0].rule[NT_regl] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 227);
          if (kids[0].rule[NT_regw] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 228);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 224);
          if (kids[0].rule[NT_regl] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 226);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 223);
        }
        break;
      case Op.CONVFX:
        if (t.type == 1028) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 233);
        }
        break;
      case Op.CONVFT:
        if (t.type == 516) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 234);
        }
        break;
      case Op.CONVFS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regl, 3 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 237);
          if (kids[0].rule[NT_regf] != 0) record(NT_regl, 3 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 238);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 235);
          if (kids[0].rule[NT_regf] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 236);
        }
        break;
      case Op.CONVSF:
        if (t.type == 516) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regq], 3 + kids[0].cost2[NT_regq], 230);
          if (kids[0].rule[NT_regl] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regl], 4 + kids[0].cost2[NT_regl], 232);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regq], 3 + kids[0].cost2[NT_regq], 229);
          if (kids[0].rule[NT_regl] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regl], 4 + kids[0].cost2[NT_regl], 231);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 47);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 42);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 48);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 43);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rbv], 49);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rbv], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rbv], 44);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 51);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 50);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT__1] != 0) record(NT__2, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT__1], 63);
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT__3] != 0) record(NT__4, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT__3], 66);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 85);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rcs], 87);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT__5] != 0) record(NT__6, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT__5], 71);
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT__7] != 0) record(NT__8, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT__7], 74);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rcs], 83);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 84);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rcs], 86);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rcs], 82);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 88);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_rcs], 89);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__19, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 131);
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__24, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 141);
          if (kids[0].rule[NT_reg64f] != 0) if (kids[1].rule[NT_reg64f] != 0) record(NT__38, 0 + kids[0].cost1[NT_reg64f] + kids[1].cost1[NT_reg64f], 0 + kids[0].cost2[NT_reg64f] + kids[1].cost2[NT_reg64f], 169);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__16, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 125);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__15, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 123);
          if (kids[0].rule[NT_reg64f] != 0) if (kids[1].rule[NT_reg64f] != 0) record(NT__35, 0 + kids[0].cost1[NT_reg64f] + kids[1].cost1[NT_reg64f], 0 + kids[0].cost2[NT_reg64f] + kids[1].cost2[NT_reg64f], 163);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__17, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 127);
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__22, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 137);
          if (kids[0].rule[NT_reg64f] != 0) if (kids[1].rule[NT_reg64f] != 0) record(NT__36, 0 + kids[0].cost1[NT_reg64f] + kids[1].cost1[NT_reg64f], 0 + kids[0].cost2[NT_reg64f] + kids[1].cost2[NT_reg64f], 165);
        }
        break;
      case Op.TSTLES:
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__18, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 129);
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__23, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 139);
          if (kids[0].rule[NT_reg64f] != 0) if (kids[1].rule[NT_reg64f] != 0) record(NT__37, 0 + kids[0].cost1[NT_reg64f] + kids[1].cost1[NT_reg64f], 0 + kids[0].cost2[NT_reg64f] + kids[1].cost2[NT_reg64f], 167);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 1026) {
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__27, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 147);
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__31, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 155);
          if (kids[0].rule[NT_reg64f] != 0) if (kids[1].rule[NT_reg64f] != 0) record(NT__39, 0 + kids[0].cost1[NT_reg64f] + kids[1].cost1[NT_reg64f], 0 + kids[0].cost2[NT_reg64f] + kids[1].cost2[NT_reg64f], 171);
        }
        break;
      case Op.TSTGES:
        if (t.type == 1026) {
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__28, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 149);
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__32, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 157);
          if (kids[0].rule[NT_reg64f] != 0) if (kids[1].rule[NT_reg64f] != 0) record(NT__40, 0 + kids[0].cost1[NT_reg64f] + kids[1].cost1[NT_reg64f], 0 + kids[0].cost2[NT_reg64f] + kids[1].cost2[NT_reg64f], 173);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__20, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 133);
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__25, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 143);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 1026) {
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__21, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 135);
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__26, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 145);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 1026) {
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__29, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 151);
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__33, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 159);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 1026) {
          if (kids[0].rule[NT_rbv] != 0) if (kids[1].rule[NT_reg64] != 0) record(NT__30, 0 + kids[0].cost1[NT_rbv] + kids[1].cost1[NT_reg64], 0 + kids[0].cost2[NT_rbv] + kids[1].cost2[NT_reg64], 153);
          if (kids[0].rule[NT_reg64] != 0) if (kids[1].rule[NT_rbv] != 0) record(NT__34, 0 + kids[0].cost1[NT_reg64] + kids[1].cost1[NT_rbv], 0 + kids[0].cost2[NT_reg64] + kids[1].cost2[NT_rbv], 161);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addr] != 0) record(NT__12, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 100);
          if (kids[0].rule[NT_addr] != 0) record(NT_regb, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 115);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addr] != 0) record(NT__11, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 98);
          if (kids[0].rule[NT_addr] != 0) record(NT_regw, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 114);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addr] != 0) record(NT__10, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 96);
          if (kids[0].rule[NT_addr] != 0) record(NT_regl, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 113);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_addr] != 0) record(NT__14, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 104);
          if (kids[0].rule[NT_addr] != 0) record(NT_regf, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 117);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_addr] != 0) record(NT__9, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 94);
          if (kids[0].rule[NT_addr] != 0) record(NT_regq, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 112);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_addr] != 0) record(NT__13, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 102);
          if (kids[0].rule[NT_addr] != 0) record(NT_regd, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 116);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT__12] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT__12] + kids[1].cost2[NT_regb], 101);
          if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_regb], 206);
          if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_regq], 212);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT__11] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT__11] + kids[1].cost2[NT_regw], 99);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 205);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT__10] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT__10] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT__10] + kids[1].cost2[NT_regl], 97);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 204);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 208);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regl], 210);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regq], 211);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT__14] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT__14] + kids[1].cost2[NT_regf], 105);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 214);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT__9] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT__9] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT__9] + kids[1].cost2[NT_regq], 95);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 203);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 207);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regq], 209);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT__13] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT__13] + kids[1].cost2[NT_regd], 103);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 213);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) record(NT_void, 1 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 122);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 124);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 126);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 128);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 130);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 132);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 134);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 136);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 138);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 140);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 142);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 144);
        if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__26] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__26] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 146);
        if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__27] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__27] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 148);
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__28] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__28] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 150);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__29] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__29] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 152);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__30] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__30] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 154);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__31] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__31] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 156);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__32] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__32] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 158);
        if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__33] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__33] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 160);
        if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__34] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__34] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 162);
        if (kids[0].rule[NT__35] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__35] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__35] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 164);
        if (kids[0].rule[NT__36] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__36] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__36] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 166);
        if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__37] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__37] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 168);
        if (kids[0].rule[NT__38] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__38] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__38] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 170);
        if (kids[0].rule[NT__39] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__39] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__39] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 172);
        if (kids[0].rule[NT__40] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT__40] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__40] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 174);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_fun] != 0) if (t.kid(0).opCode == Op.STATIC || t.kid(0).opCode == Op.MEM) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 239);
        if (kids[0].rule[NT_fun] != 0) if (t.kid(0).opCode == Op.REG || t.kid(0).opCode == Op.INTCONST) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 240);
        break;
      case Op.PARALLEL:
        if (kids.length == 1) if (kids[0].rule[NT_void] != 0) record(NT_void, 0 + kids[0].cost1[NT_void], 0 + kids[0].cost2[NT_void], 241);
        break;
      case Op.LIST:
        if (kids.length == 2) if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__9] != 0) record(NT_void, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__9], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__9], 106);
        if (kids.length == 2) if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__10] != 0) record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__10], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__10], 107);
        if (kids.length == 2) if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__11], 108);
        if (kids.length == 2) if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT__12] != 0) record(NT_void, 1 + kids[0].cost1[NT_regb] + kids[1].cost1[NT__12], 1 + kids[0].cost2[NT_regb] + kids[1].cost2[NT__12], 109);
        if (kids.length == 2) if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT__13] != 0) record(NT_void, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT__13], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT__13], 110);
        if (kids.length == 2) if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT__14] != 0) record(NT_void, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT__14], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT__14], 111);
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
    
    /*************************************************************************/
    private boolean isConstZero(LirIconst c){ /* Is Constant-Zero ? */
      return (0 == c.signedValue());
    }
    
    /*************************************************************************/
    private boolean isConstS(LirIconst c, int n){ /* Is Constant-Signed ? */
      if(n<0 || n>64)
        throw new CantHappenException("## isConstS() n out of range");
      long bound = (1<<(n-1));
      return (-bound <= c.signedValue()) && (c.signedValue() < bound);
    }
    
    /*************************************************************************/
    private boolean isConstU(LirIconst c, int n){ /* Is Constant-Unsigned ? */
      if(n<0 || n>64)
        throw new CantHappenException("## isConstU() n out of range");
      long bound = (1<<n);
      return (0 <= c.signedValue()) && (c.signedValue() < bound);
    }
    
    /*************************************************************************/
    /*************************************************************************/
  }


  private static final Rule[] rulev = new Rule[State.NRULES];

  static {
    rrinit0();
    rrinit100();
    rrinit200();
  }
  static private void rrinit0() {
    rulev[18] = new Rule(18, true, false, 12, "18: rcs -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[21] = new Rule(21, true, false, 13, "21: rbv -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[24] = new Rule(24, true, false, 14, "24: addr -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[30] = new Rule(30, true, false, 17, "30: reg64 -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[19] = new Rule(19, true, false, 12, "19: rcs -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[22] = new Rule(22, true, false, 13, "22: rbv -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[25] = new Rule(25, true, false, 14, "25: addr -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[31] = new Rule(31, true, false, 17, "31: reg64 -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[32] = new Rule(32, true, false, 18, "32: reg64f -> regd", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-F64*"});
    rulev[33] = new Rule(33, true, false, 18, "33: reg64f -> regf", null, null, null, 0, false, false, new int[]{6}, new String[]{null, "*reg-F32*"});
    rulev[17] = new Rule(17, true, false, 12, "17: rcs -> con05", null, null, null, 0, false, false, new int[]{8}, new String[]{null, null});
    rulev[20] = new Rule(20, true, false, 13, "20: rbv -> con08", null, null, null, 0, false, false, new int[]{9}, new String[]{null, null});
    rulev[185] = new Rule(185, true, false, 1, "185: regq -> con08", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I64*", null});
    rulev[189] = new Rule(189, true, false, 2, "189: regl -> con08", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I32*", null});
    rulev[193] = new Rule(193, true, false, 3, "193: regw -> con08", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I16*", null});
    rulev[197] = new Rule(197, true, false, 4, "197: regb -> con08", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I08*", null});
    rulev[23] = new Rule(23, true, false, 14, "23: addr -> con16", null, null, null, 0, false, false, new int[]{10}, new String[]{null, null});
    rulev[186] = new Rule(186, true, false, 1, "186: regq -> con16", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I64*", null});
    rulev[190] = new Rule(190, true, false, 2, "190: regl -> con16", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I32*", null});
    rulev[194] = new Rule(194, true, false, 3, "194: regw -> con16", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I16*", null});
    rulev[198] = new Rule(198, true, false, 4, "198: regb -> con16", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I08*", null});
    rulev[36] = new Rule(36, true, false, 20, "36: asmcon -> con64", null, null, null, 0, false, false, new int[]{11}, new String[]{null, null});
    rulev[187] = new Rule(187, true, false, 1, "187: regq -> con64", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I64*", null});
    rulev[191] = new Rule(191, true, false, 2, "191: regl -> con64", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I32*", null});
    rulev[195] = new Rule(195, true, false, 3, "195: regw -> con64", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I16*", null});
    rulev[199] = new Rule(199, true, false, 4, "199: regb -> con64", ImList.list(ImList.list("_ldw",ImList.list("_r","$0"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I08*", null});
    rulev[34] = new Rule(34, true, false, 19, "34: fun -> addr", null, null, null, 0, false, false, new int[]{14}, new String[]{null, null});
    rulev[37] = new Rule(37, true, false, 20, "37: asmcon -> sta", null, ImList.list(ImList.list("_static","$1")), null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[188] = new Rule(188, true, false, 1, "188: regq -> sta", ImList.list(ImList.list("lda",ImList.list("_r","$0"),ImList.list("_static","$1"))), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I64*", null});
    rulev[192] = new Rule(192, true, false, 2, "192: regl -> sta", ImList.list(ImList.list("lda",ImList.list("_r","$0"),ImList.list("_static","$1"))), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I32*", null});
    rulev[196] = new Rule(196, true, false, 3, "196: regw -> sta", ImList.list(ImList.list("lda",ImList.list("_r","$0"),ImList.list("_static","$1"))), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I16*", null});
    rulev[200] = new Rule(200, true, false, 4, "200: regb -> sta", ImList.list(ImList.list("lda",ImList.list("_r","$0"),ImList.list("_static","$1"))), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I08*", null});
    rulev[35] = new Rule(35, true, false, 19, "35: fun -> asmcon", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[13] = new Rule(13, false, false, 8, "13: con05 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[14] = new Rule(14, false, false, 9, "14: con08 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[15] = new Rule(15, false, false, 10, "15: con16 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[16] = new Rule(16, false, false, 11, "16: con64 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[62] = new Rule(62, false, true, 21, "62: _1 -> (INTCONST I32 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[65] = new Rule(65, false, true, 23, "65: _3 -> (INTCONST I32 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[70] = new Rule(70, false, true, 25, "70: _5 -> (INTCONST I64 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[73] = new Rule(73, false, true, 27, "73: _7 -> (INTCONST I64 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[202] = new Rule(202, false, false, 6, "202: regf -> (FLOATCONST F32 0.0)", ImList.list(ImList.list("fclr",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{}, new String[]{"*reg-F32*"});
    rulev[201] = new Rule(201, false, false, 5, "201: regd -> (FLOATCONST F64 0.0)", ImList.list(ImList.list("fclr",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{}, new String[]{"*reg-F64*"});
    rulev[28] = new Rule(28, false, false, 15, "28: sta -> (STATIC I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 4, "7: regb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I08*"});
    rulev[5] = new Rule(5, false, false, 3, "5: regw -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I16*"});
    rulev[3] = new Rule(3, false, false, 2, "3: regl -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I32*"});
    rulev[11] = new Rule(11, false, false, 6, "11: regf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-F32*"});
    rulev[1] = new Rule(1, false, false, 1, "1: regq -> (REG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I64*"});
    rulev[9] = new Rule(9, false, false, 5, "9: regd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-F64*"});
    rulev[8] = new Rule(8, false, false, 4, "8: regb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I08*"});
    rulev[6] = new Rule(6, false, false, 3, "6: regw -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I16*"});
    rulev[4] = new Rule(4, false, false, 2, "4: regl -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I32*"});
    rulev[12] = new Rule(12, false, false, 6, "12: regf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-F32*"});
    rulev[2] = new Rule(2, false, false, 1, "2: regq -> (SUBREG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-I64*"});
    rulev[10] = new Rule(10, false, false, 5, "10: regd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{"*reg-F64*"});
    rulev[29] = new Rule(29, false, false, 16, "29: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[53] = new Rule(53, false, false, 2, "53: regl -> (NEG I32 regl)", ImList.list(ImList.list("subq",ImList.list("_r","%31"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[184] = new Rule(184, false, false, 6, "184: regf -> (NEG F32 regf)", ImList.list(ImList.list("cpysn",ImList.list("_r","$1"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F32*", "*reg-F32*"});
    rulev[52] = new Rule(52, false, false, 1, "52: regq -> (NEG I64 regq)", ImList.list(ImList.list("subq",ImList.list("_r","%31"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[183] = new Rule(183, false, false, 5, "183: regd -> (NEG F64 regd)", ImList.list(ImList.list("cpysn",ImList.list("_r","$1"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F64*", "*reg-F64*"});
    rulev[45] = new Rule(45, false, false, 2, "45: regl -> (ADD I32 regl rbv)", ImList.list(ImList.list("addl",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[78] = new Rule(78, false, false, 2, "78: regl -> (ADD I32 regl _2)", ImList.list(ImList.list("s4addl",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[79] = new Rule(79, false, false, 2, "79: regl -> (ADD I32 regl _4)", ImList.list(ImList.list("s8addl",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,24}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[179] = new Rule(179, false, false, 6, "179: regf -> (ADD F32 regf regf)", ImList.list(ImList.list("adds",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[26] = new Rule(26, false, false, 14, "26: addr -> (ADD I64 regq con16)", null, ImList.list(ImList.list("_plus","$1","$2")), null, 0, false, false, new int[]{1,10}, new String[]{null, "*reg-I64*", null});
    rulev[27] = new Rule(27, false, false, 14, "27: addr -> (ADD I64 regl con16)", null, ImList.list(ImList.list("_plus","$1","$2")), null, 0, false, false, new int[]{2,10}, new String[]{null, "*reg-I32*", null});
    rulev[38] = new Rule(38, false, false, 20, "38: asmcon -> (ADD I64 asmcon con64)", null, ImList.list(ImList.list("_plus","$1","$2")), null, 0, false, false, new int[]{20,11}, new String[]{null, null, null});
    rulev[40] = new Rule(40, false, false, 1, "40: regq -> (ADD I64 regq rbv)", ImList.list(ImList.list("addq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[60] = new Rule(60, false, false, 1, "60: regq -> (ADD I64 regl con16)", ImList.list(ImList.list("lda",ImList.list("_r","$0"),ImList.list("_lda",ImList.list("_r","$2"),ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{2,10}, new String[]{"*reg-I64*", "*reg-I32*", null});
    rulev[61] = new Rule(61, false, false, 1, "61: regq -> (ADD I64 regl rbv)", ImList.list(ImList.list("addq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I64*", "*reg-I32*", null});
    rulev[64] = new Rule(64, false, false, 1, "64: regq -> (ADD I64 regq _2)", ImList.list(ImList.list("s4addq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,22}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[67] = new Rule(67, false, false, 1, "67: regq -> (ADD I64 regq _4)", ImList.list(ImList.list("s8addq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,24}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[72] = new Rule(72, false, false, 1, "72: regq -> (ADD I64 regq _6)", ImList.list(ImList.list("s4addq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,26}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[75] = new Rule(75, false, false, 1, "75: regq -> (ADD I64 regq _8)", ImList.list(ImList.list("s8addq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,28}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[175] = new Rule(175, false, false, 5, "175: regd -> (ADD F64 regd regd)", ImList.list(ImList.list("addt",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[46] = new Rule(46, false, false, 2, "46: regl -> (SUB I32 regl rbv)", ImList.list(ImList.list("subl",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[80] = new Rule(80, false, false, 2, "80: regl -> (SUB I32 regl _2)", ImList.list(ImList.list("s4subl",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[81] = new Rule(81, false, false, 2, "81: regl -> (SUB I32 regl _4)", ImList.list(ImList.list("s8subl",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,24}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[180] = new Rule(180, false, false, 6, "180: regf -> (SUB F32 regf regf)", ImList.list(ImList.list("subs",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[39] = new Rule(39, false, false, 20, "39: asmcon -> (SUB I64 asmcon con64)", null, ImList.list(ImList.list("_minus","$1","$2")), null, 0, false, false, new int[]{20,11}, new String[]{null, null, null});
    rulev[41] = new Rule(41, false, false, 1, "41: regq -> (SUB I64 regq rbv)", ImList.list(ImList.list("subq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[68] = new Rule(68, false, false, 1, "68: regq -> (SUB I64 regq _2)", ImList.list(ImList.list("s4subq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,22}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[69] = new Rule(69, false, false, 1, "69: regq -> (SUB I64 regq _4)", ImList.list(ImList.list("s8subq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,24}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[76] = new Rule(76, false, false, 1, "76: regq -> (SUB I64 regq _6)", ImList.list(ImList.list("s4subq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,26}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[77] = new Rule(77, false, false, 1, "77: regq -> (SUB I64 regq _8)", ImList.list(ImList.list("s8subq",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,28}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[176] = new Rule(176, false, false, 5, "176: regd -> (SUB F64 regd regd)", ImList.list(ImList.list("subt",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[56] = new Rule(56, false, false, 2, "56: regl -> (MUL I32 regl rbv)", ImList.list(ImList.list("mull",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[58] = new Rule(58, false, false, 2, "58: regl -> (MUL I32 regq rbv)", ImList.list(ImList.list("mulq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I32*", "*reg-I64*", null});
    rulev[181] = new Rule(181, false, false, 6, "181: regf -> (MUL F32 regf regf)", ImList.list(ImList.list("muls",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[54] = new Rule(54, false, false, 1, "54: regq -> (MUL I64 regq rbv)", ImList.list(ImList.list("mulq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[177] = new Rule(177, false, false, 5, "177: regd -> (MUL F64 regd regd)", ImList.list(ImList.list("mult",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[57] = new Rule(57, false, false, 2, "57: regl -> (DIVS I32 regl rbv)", ImList.list(ImList.list("divl",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[59] = new Rule(59, false, false, 2, "59: regl -> (DIVS I32 regq rbv)", ImList.list(ImList.list("divq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I32*", "*reg-I64*", null});
    rulev[182] = new Rule(182, false, false, 6, "182: regf -> (DIVS F32 regf regf)", ImList.list(ImList.list("divs",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[55] = new Rule(55, false, false, 1, "55: regq -> (DIVS I64 regq rbv)", ImList.list(ImList.list("divq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[178] = new Rule(178, false, false, 5, "178: regd -> (DIVS F64 regd regd)", ImList.list(ImList.list("divt",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[92] = new Rule(92, false, false, 2, "92: regl -> (MODS I32 regl rbv)", ImList.list(ImList.list("reml",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[90] = new Rule(90, false, false, 1, "90: regq -> (MODS I64 regq rbv)", ImList.list(ImList.list("remq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[93] = new Rule(93, false, false, 2, "93: regl -> (MODU I32 regl rbv)", ImList.list(ImList.list("remlu",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
  }
  static private void rrinit100() {
    rulev[91] = new Rule(91, false, false, 1, "91: regq -> (MODU I64 regq rbv)", ImList.list(ImList.list("remqu",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[222] = new Rule(222, false, false, 3, "222: regw -> (CONVSX I16 regb)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"8",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"8",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I16*", "*reg-I08*"});
    rulev[118] = new Rule(118, false, false, 2, "118: regl -> (CONVSX I32 _11)", ImList.list(ImList.list("ldw",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{31}, new String[]{"*reg-I32*", null});
    rulev[120] = new Rule(120, false, false, 2, "120: regl -> (CONVSX I32 _12)", ImList.list(ImList.list("ldb",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-I32*", null});
    rulev[219] = new Rule(219, false, false, 2, "219: regl -> (CONVSX I32 regw)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"16",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"16",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[221] = new Rule(221, false, false, 2, "221: regl -> (CONVSX I32 regb)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"24",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"24",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I32*", "*reg-I08*"});
    rulev[218] = new Rule(218, false, false, 1, "218: regq -> (CONVSX I64 regl)", null, null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I64*", "*reg-I32*"});
    rulev[220] = new Rule(220, false, false, 1, "220: regq -> (CONVSX I64 regb)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"56",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"56",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I64*", "*reg-I08*"});
    rulev[119] = new Rule(119, false, false, 2, "119: regl -> (CONVZX I32 _11)", ImList.list(ImList.list("ldw",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{31}, new String[]{"*reg-I32*", null});
    rulev[121] = new Rule(121, false, false, 2, "121: regl -> (CONVZX I32 _12)", ImList.list(ImList.list("ldb",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-I32*", null});
    rulev[216] = new Rule(216, false, false, 2, "216: regl -> (CONVZX I32 regw)", ImList.list(ImList.list("zapnot",ImList.list("_r","$1"),"3",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[217] = new Rule(217, false, false, 2, "217: regl -> (CONVZX I32 regb)", ImList.list(ImList.list("zapnot",ImList.list("_r","$1"),"1",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I32*", "*reg-I08*"});
    rulev[215] = new Rule(215, false, false, 1, "215: regq -> (CONVZX I64 regl)", ImList.list(ImList.list("zapnot",ImList.list("_r","$1"),"15",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I64*", "*reg-I32*"});
    rulev[225] = new Rule(225, false, false, 4, "225: regb -> (CONVIT I8 regq)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"56",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"56",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I08*", "*reg-I64*"});
    rulev[227] = new Rule(227, false, false, 4, "227: regb -> (CONVIT I8 regl)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"56",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"56",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I08*", "*reg-I32*"});
    rulev[228] = new Rule(228, false, false, 4, "228: regb -> (CONVIT I8 regw)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"56",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"56",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I08*", "*reg-I16*"});
    rulev[224] = new Rule(224, false, false, 3, "224: regw -> (CONVIT I16 regq)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"48",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"48",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I64*"});
    rulev[226] = new Rule(226, false, false, 3, "226: regw -> (CONVIT I16 regl)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),"48",ImList.list("_r","$0")),ImList.list("sra",ImList.list("_r","$0"),"48",ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[223] = new Rule(223, false, false, 2, "223: regl -> (CONVIT I32 regq)", ImList.list(ImList.list("addl",ImList.list("_r","$1"),ImList.list("_r","%31"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I64*"});
    rulev[233] = new Rule(233, false, false, 5, "233: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("fmov",ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F64*", "*reg-F32*"});
    rulev[234] = new Rule(234, false, false, 6, "234: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("cvtts",ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F64*"});
    rulev[237] = new Rule(237, false, false, 2, "237: regl -> (CONVFS I32 regd)", ImList.list(ImList.list("cvttq",ImList.list("_r","$1"),ImList.list("_r","%f25")),ImList.list("stt",ImList.list("_r","%f25"),ImList.list("_mem_tmp",".TMP25")),ImList.list("ldq",ImList.list("_r","$0"),ImList.list("_mem_tmp",".TMP25"))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I32*", "*reg-F64*"});
    rulev[238] = new Rule(238, false, false, 2, "238: regl -> (CONVFS I32 regf)", ImList.list(ImList.list("cvttq",ImList.list("_r","$1"),ImList.list("_r","%f25")),ImList.list("stt",ImList.list("_r","%f25"),ImList.list("_mem_tmp",".TMP25")),ImList.list("ldq",ImList.list("_r","$0"),ImList.list("_mem_tmp",".TMP25"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-F32*"});
    rulev[235] = new Rule(235, false, false, 1, "235: regq -> (CONVFS I64 regd)", ImList.list(ImList.list("cvttq",ImList.list("_r","$1"),ImList.list("_r","%f25")),ImList.list("stt",ImList.list("_r","%f25"),ImList.list("_mem_tmp",".TMP25")),ImList.list("ldq",ImList.list("_r","$0"),ImList.list("_mem_tmp",".TMP25"))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I64*", "*reg-F64*"});
    rulev[236] = new Rule(236, false, false, 1, "236: regq -> (CONVFS I64 regf)", ImList.list(ImList.list("cvttq",ImList.list("_r","$1"),ImList.list("_r","%f25")),ImList.list("stt",ImList.list("_r","%f25"),ImList.list("_mem_tmp",".TMP25")),ImList.list("ldq",ImList.list("_r","$0"),ImList.list("_mem_tmp",".TMP25"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I64*", "*reg-F32*"});
    rulev[230] = new Rule(230, false, false, 6, "230: regf -> (CONVSF F32 regq)", ImList.list(ImList.list("stq",ImList.list("_r","$1"),ImList.list("_mem_tmp",".TMP25")),ImList.list("ldt",ImList.list("_r","%f25"),ImList.list("_mem_tmp",".TMP25")),ImList.list("cvtqs",ImList.list("_r","%f25"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-F32*", "*reg-I64*"});
    rulev[232] = new Rule(232, false, false, 6, "232: regf -> (CONVSF F32 regl)", ImList.list(ImList.list("stl",ImList.list("_r","$1"),ImList.list("_mem_tmp",".TMP25")),ImList.list("lds",ImList.list("_r","$0"),ImList.list("_mem_tmp",".TMP25")),ImList.list("cvtlq",ImList.list("_r","$0"),ImList.list("_r","%f25")),ImList.list("cvtqs",ImList.list("_r","%f25"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-F32*", "*reg-I32*"});
    rulev[229] = new Rule(229, false, false, 5, "229: regd -> (CONVSF F64 regq)", ImList.list(ImList.list("stq",ImList.list("_r","$1"),ImList.list("_mem_tmp",".TMP25")),ImList.list("ldt",ImList.list("_r","%f25"),ImList.list("_mem_tmp",".TMP25")),ImList.list("cvtqt",ImList.list("_r","%f25"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-F64*", "*reg-I64*"});
    rulev[231] = new Rule(231, false, false, 5, "231: regd -> (CONVSF F64 regl)", ImList.list(ImList.list("stl",ImList.list("_r","$1"),ImList.list("_mem_tmp",".TMP25")),ImList.list("lds",ImList.list("_r","$0"),ImList.list("_mem_tmp",".TMP25")),ImList.list("cvtlq",ImList.list("_r","$0"),ImList.list("_r","%f25")),ImList.list("cvtqt",ImList.list("_r","%f25"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-F64*", "*reg-I32*"});
    rulev[47] = new Rule(47, false, false, 2, "47: regl -> (BAND I32 regl rbv)", ImList.list(ImList.list("and",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[42] = new Rule(42, false, false, 1, "42: regq -> (BAND I64 regq rbv)", ImList.list(ImList.list("and",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[48] = new Rule(48, false, false, 2, "48: regl -> (BOR I32 regl rbv)", ImList.list(ImList.list("or",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[43] = new Rule(43, false, false, 1, "43: regq -> (BOR I64 regq rbv)", ImList.list(ImList.list("or",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[49] = new Rule(49, false, false, 2, "49: regl -> (BXOR I32 regl rbv)", ImList.list(ImList.list("xor",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,13}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[44] = new Rule(44, false, false, 1, "44: regq -> (BXOR I64 regq rbv)", ImList.list(ImList.list("xor",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,13}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[51] = new Rule(51, false, false, 2, "51: regl -> (BNOT I32 regl)", ImList.list(ImList.list("ornot",ImList.list("_r","%31"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[50] = new Rule(50, false, false, 1, "50: regq -> (BNOT I64 regq)", ImList.list(ImList.list("ornot",ImList.list("_r","%31"),ImList.list("_r","$1"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[63] = new Rule(63, false, true, 22, "63: _2 -> (LSHS I32 reg64 _1)", null, null, null, 0, false, false, new int[]{17,21}, null);
    rulev[66] = new Rule(66, false, true, 24, "66: _4 -> (LSHS I32 reg64 _3)", null, null, null, 0, false, false, new int[]{17,23}, null);
    rulev[85] = new Rule(85, false, false, 2, "85: regl -> (LSHS I32 regl rcs)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,12}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[87] = new Rule(87, false, false, 2, "87: regl -> (LSHS I32 regq rcs)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,12}, new String[]{"*reg-I32*", "*reg-I64*", null});
    rulev[71] = new Rule(71, false, true, 26, "71: _6 -> (LSHS I64 reg64 _5)", null, null, null, 0, false, false, new int[]{17,25}, null);
    rulev[74] = new Rule(74, false, true, 28, "74: _8 -> (LSHS I64 reg64 _7)", null, null, null, 0, false, false, new int[]{17,27}, null);
    rulev[83] = new Rule(83, false, false, 1, "83: regq -> (LSHS I64 regq rcs)", ImList.list(ImList.list("sll",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,12}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[84] = new Rule(84, false, false, 2, "84: regl -> (RSHS I32 regl rcs)", ImList.list(ImList.list("sra",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,12}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[86] = new Rule(86, false, false, 2, "86: regl -> (RSHS I32 regq rcs)", ImList.list(ImList.list("sra",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,12}, new String[]{"*reg-I32*", "*reg-I64*", null});
    rulev[82] = new Rule(82, false, false, 1, "82: regq -> (RSHS I64 regq rcs)", ImList.list(ImList.list("sra",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,12}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[88] = new Rule(88, false, false, 2, "88: regl -> (RSHU I32 regl rcs)", ImList.list(ImList.list("zapnot",ImList.list("_r","$1"),"15",ImList.list("_r","$1")),ImList.list("srl",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{2,12}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[89] = new Rule(89, false, false, 1, "89: regq -> (RSHU I64 regq rcs)", ImList.list(ImList.list("srl",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","$0"))), null, null, 0, false, false, new int[]{1,12}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[131] = new Rule(131, false, true, 39, "131: _19 -> (TSTEQ I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[141] = new Rule(141, false, true, 44, "141: _24 -> (TSTEQ I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[169] = new Rule(169, false, true, 58, "169: _38 -> (TSTEQ I64 reg64f reg64f)", null, null, null, 0, false, false, new int[]{18,18}, null);
    rulev[125] = new Rule(125, false, true, 36, "125: _16 -> (TSTNE I32 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[123] = new Rule(123, false, true, 35, "123: _15 -> (TSTNE I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[163] = new Rule(163, false, true, 55, "163: _35 -> (TSTNE I64 reg64f reg64f)", null, null, null, 0, false, false, new int[]{18,18}, null);
    rulev[127] = new Rule(127, false, true, 37, "127: _17 -> (TSTLTS I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[137] = new Rule(137, false, true, 42, "137: _22 -> (TSTLTS I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[165] = new Rule(165, false, true, 56, "165: _36 -> (TSTLTS I64 reg64f reg64f)", null, null, null, 0, false, false, new int[]{18,18}, null);
    rulev[129] = new Rule(129, false, true, 38, "129: _18 -> (TSTLES I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[139] = new Rule(139, false, true, 43, "139: _23 -> (TSTLES I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[167] = new Rule(167, false, true, 57, "167: _37 -> (TSTLES I64 reg64f reg64f)", null, null, null, 0, false, false, new int[]{18,18}, null);
    rulev[147] = new Rule(147, false, true, 47, "147: _27 -> (TSTGTS I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[155] = new Rule(155, false, true, 51, "155: _31 -> (TSTGTS I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[171] = new Rule(171, false, true, 59, "171: _39 -> (TSTGTS I64 reg64f reg64f)", null, null, null, 0, false, false, new int[]{18,18}, null);
    rulev[149] = new Rule(149, false, true, 48, "149: _28 -> (TSTGES I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[157] = new Rule(157, false, true, 52, "157: _32 -> (TSTGES I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[173] = new Rule(173, false, true, 60, "173: _40 -> (TSTGES I64 reg64f reg64f)", null, null, null, 0, false, false, new int[]{18,18}, null);
    rulev[133] = new Rule(133, false, true, 40, "133: _20 -> (TSTLTU I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[143] = new Rule(143, false, true, 45, "143: _25 -> (TSTLTU I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[135] = new Rule(135, false, true, 41, "135: _21 -> (TSTLEU I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[145] = new Rule(145, false, true, 46, "145: _26 -> (TSTLEU I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[151] = new Rule(151, false, true, 49, "151: _29 -> (TSTGTU I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[159] = new Rule(159, false, true, 53, "159: _33 -> (TSTGTU I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[153] = new Rule(153, false, true, 50, "153: _30 -> (TSTGEU I64 rbv reg64)", null, null, null, 0, false, false, new int[]{13,17}, null);
    rulev[161] = new Rule(161, false, true, 54, "161: _34 -> (TSTGEU I64 reg64 rbv)", null, null, null, 0, false, false, new int[]{17,13}, null);
    rulev[100] = new Rule(100, false, true, 32, "100: _12 -> (MEM I8 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[115] = new Rule(115, false, false, 4, "115: regb -> (MEM I8 addr)", ImList.list(ImList.list("ldb",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I08*", null});
    rulev[98] = new Rule(98, false, true, 31, "98: _11 -> (MEM I16 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[114] = new Rule(114, false, false, 3, "114: regw -> (MEM I16 addr)", ImList.list(ImList.list("ldw",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I16*", null});
    rulev[96] = new Rule(96, false, true, 30, "96: _10 -> (MEM I32 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[113] = new Rule(113, false, false, 2, "113: regl -> (MEM I32 addr)", ImList.list(ImList.list("ldl",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I32*", null});
    rulev[104] = new Rule(104, false, true, 34, "104: _14 -> (MEM F32 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[117] = new Rule(117, false, false, 6, "117: regf -> (MEM F32 addr)", ImList.list(ImList.list("lds",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-F32*", null});
    rulev[94] = new Rule(94, false, true, 29, "94: _9 -> (MEM I64 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[112] = new Rule(112, false, false, 1, "112: regq -> (MEM I64 addr)", ImList.list(ImList.list("ldq",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I64*", null});
    rulev[102] = new Rule(102, false, true, 33, "102: _13 -> (MEM F64 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[116] = new Rule(116, false, false, 5, "116: regd -> (MEM F64 addr)", ImList.list(ImList.list("ldt",ImList.list("_r","$0"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-F64*", null});
    rulev[101] = new Rule(101, false, false, 7, "101: void -> (SET I8 _12 regb)", ImList.list(ImList.list("stb",ImList.list("_r","$2"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{32,4}, new String[]{null, null, "*reg-I08*"});
    rulev[206] = new Rule(206, false, false, 7, "206: void -> (SET I8 regb regb)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{4,4}, new String[]{null, "*reg-I08*", "*reg-I08*"});
    rulev[212] = new Rule(212, false, false, 7, "212: void -> (SET I8 regb regq)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{4,1}, new String[]{null, "*reg-I08*", "*reg-I64*"});
    rulev[99] = new Rule(99, false, false, 7, "99: void -> (SET I16 _11 regw)", ImList.list(ImList.list("stw",ImList.list("_r","$2"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{31,3}, new String[]{null, null, "*reg-I16*"});
    rulev[205] = new Rule(205, false, false, 7, "205: void -> (SET I16 regw regw)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{3,3}, new String[]{null, "*reg-I16*", "*reg-I16*"});
    rulev[97] = new Rule(97, false, false, 7, "97: void -> (SET I32 _10 regl)", ImList.list(ImList.list("stl",ImList.list("_r","$2"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{30,2}, new String[]{null, null, "*reg-I32*"});
    rulev[204] = new Rule(204, false, false, 7, "204: void -> (SET I32 regl regl)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{2,2}, new String[]{null, "*reg-I32*", "*reg-I32*"});
    rulev[208] = new Rule(208, false, false, 7, "208: void -> (SET I32 regq regq)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-I64*", "*reg-I64*"});
    rulev[210] = new Rule(210, false, false, 7, "210: void -> (SET I32 regq regl)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{1,2}, new String[]{null, "*reg-I64*", "*reg-I32*"});
    rulev[211] = new Rule(211, false, false, 7, "211: void -> (SET I32 regl regq)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{2,1}, new String[]{null, "*reg-I32*", "*reg-I64*"});
    rulev[105] = new Rule(105, false, false, 7, "105: void -> (SET F32 _14 regf)", ImList.list(ImList.list("sts",ImList.list("_r","$2"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{34,6}, new String[]{null, null, "*reg-F32*"});
    rulev[214] = new Rule(214, false, false, 7, "214: void -> (SET F32 regf regf)", ImList.list(ImList.list("fmov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{6,6}, new String[]{null, "*reg-F32*", "*reg-F32*"});
    rulev[95] = new Rule(95, false, false, 7, "95: void -> (SET I64 _9 regq)", ImList.list(ImList.list("stq",ImList.list("_r","$2"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{29,1}, new String[]{null, null, "*reg-I64*"});
  }
  static private void rrinit200() {
    rulev[203] = new Rule(203, false, false, 7, "203: void -> (SET I64 regq regq)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-I64*", "*reg-I64*"});
    rulev[207] = new Rule(207, false, false, 7, "207: void -> (SET I64 regl regl)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{2,2}, new String[]{null, "*reg-I32*", "*reg-I32*"});
    rulev[209] = new Rule(209, false, false, 7, "209: void -> (SET I64 regl regq)", ImList.list(ImList.list("mov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{2,1}, new String[]{null, "*reg-I32*", "*reg-I64*"});
    rulev[103] = new Rule(103, false, false, 7, "103: void -> (SET F64 _13 regd)", ImList.list(ImList.list("stt",ImList.list("_r","$2"),ImList.list("_mem",ImList.list("_r","$1")))), null, null, 0, false, false, new int[]{33,5}, new String[]{null, null, "*reg-F64*"});
    rulev[213] = new Rule(213, false, false, 7, "213: void -> (SET F64 regd regd)", ImList.list(ImList.list("fmov",ImList.list("_r","$2"),ImList.list("_r","$1"))), null, null, 0, false, false, new int[]{5,5}, new String[]{null, "*reg-F64*", "*reg-F64*"});
    rulev[122] = new Rule(122, false, false, 7, "122: void -> (JUMP _ label)", ImList.list(ImList.list("br",ImList.list("_r","%31"),ImList.list("_jumplabel","$1"))), null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[124] = new Rule(124, false, false, 7, "124: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("cmpeq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{35,16,16}, new String[]{null, null, null, null, null});
    rulev[126] = new Rule(126, false, false, 7, "126: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("cmpeq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{36,16,16}, new String[]{null, null, null, null, null});
    rulev[128] = new Rule(128, false, false, 7, "128: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("cmplt",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{37,16,16}, new String[]{null, null, null, null, null});
    rulev[130] = new Rule(130, false, false, 7, "130: void -> (JUMPC _ _18 label label)", ImList.list(ImList.list("cmple",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{38,16,16}, new String[]{null, null, null, null, null});
    rulev[132] = new Rule(132, false, false, 7, "132: void -> (JUMPC _ _19 label label)", ImList.list(ImList.list("cmpeq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{39,16,16}, new String[]{null, null, null, null, null});
    rulev[134] = new Rule(134, false, false, 7, "134: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("cmpult",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{40,16,16}, new String[]{null, null, null, null, null});
    rulev[136] = new Rule(136, false, false, 7, "136: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("cmpule",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{41,16,16}, new String[]{null, null, null, null, null});
    rulev[138] = new Rule(138, false, false, 7, "138: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("cmple",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{42,16,16}, new String[]{null, null, null, null, null});
    rulev[140] = new Rule(140, false, false, 7, "140: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("cmplt",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{43,16,16}, new String[]{null, null, null, null, null});
    rulev[142] = new Rule(142, false, false, 7, "142: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("cmpne",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{44,16,16}, new String[]{null, null, null, null, null});
    rulev[144] = new Rule(144, false, false, 7, "144: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("cmpule",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{45,16,16}, new String[]{null, null, null, null, null});
    rulev[146] = new Rule(146, false, false, 7, "146: void -> (JUMPC _ _26 label label)", ImList.list(ImList.list("cmpult",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{46,16,16}, new String[]{null, null, null, null, null});
    rulev[148] = new Rule(148, false, false, 7, "148: void -> (JUMPC _ _27 label label)", ImList.list(ImList.list("cmplt",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{47,16,16}, new String[]{null, null, null, null, null});
    rulev[150] = new Rule(150, false, false, 7, "150: void -> (JUMPC _ _28 label label)", ImList.list(ImList.list("cmple",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{48,16,16}, new String[]{null, null, null, null, null});
    rulev[152] = new Rule(152, false, false, 7, "152: void -> (JUMPC _ _29 label label)", ImList.list(ImList.list("cmpult",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{49,16,16}, new String[]{null, null, null, null, null});
    rulev[154] = new Rule(154, false, false, 7, "154: void -> (JUMPC _ _30 label label)", ImList.list(ImList.list("cmpule",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%25")),ImList.list("bne",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{50,16,16}, new String[]{null, null, null, null, null});
    rulev[156] = new Rule(156, false, false, 7, "156: void -> (JUMPC _ _31 label label)", ImList.list(ImList.list("cmple",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{51,16,16}, new String[]{null, null, null, null, null});
    rulev[158] = new Rule(158, false, false, 7, "158: void -> (JUMPC _ _32 label label)", ImList.list(ImList.list("cmplt",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{52,16,16}, new String[]{null, null, null, null, null});
    rulev[160] = new Rule(160, false, false, 7, "160: void -> (JUMPC _ _33 label label)", ImList.list(ImList.list("cmpule",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{53,16,16}, new String[]{null, null, null, null, null});
    rulev[162] = new Rule(162, false, false, 7, "162: void -> (JUMPC _ _34 label label)", ImList.list(ImList.list("cmpult",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%25")),ImList.list("beq",ImList.list("_r","%25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{54,16,16}, new String[]{null, null, null, null, null});
    rulev[164] = new Rule(164, false, false, 7, "164: void -> (JUMPC _ _35 label label)", ImList.list(ImList.list("cmpteq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%f25")),ImList.list("fbeq",ImList.list("_r","%f25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{55,16,16}, new String[]{null, null, null, null, null});
    rulev[166] = new Rule(166, false, false, 7, "166: void -> (JUMPC _ _36 label label)", ImList.list(ImList.list("cmptlt",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%f25")),ImList.list("fbne",ImList.list("_r","%f25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{56,16,16}, new String[]{null, null, null, null, null});
    rulev[168] = new Rule(168, false, false, 7, "168: void -> (JUMPC _ _37 label label)", ImList.list(ImList.list("cmptle",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%f25")),ImList.list("fbne",ImList.list("_r","%f25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{57,16,16}, new String[]{null, null, null, null, null});
    rulev[170] = new Rule(170, false, false, 7, "170: void -> (JUMPC _ _38 label label)", ImList.list(ImList.list("cmpteq",ImList.list("_r","$1"),ImList.list("_r","$2"),ImList.list("_r","%f25")),ImList.list("fbne",ImList.list("_r","%f25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{58,16,16}, new String[]{null, null, null, null, null});
    rulev[172] = new Rule(172, false, false, 7, "172: void -> (JUMPC _ _39 label label)", ImList.list(ImList.list("cmptlt",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%f25")),ImList.list("fbne",ImList.list("_r","%f25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{59,16,16}, new String[]{null, null, null, null, null});
    rulev[174] = new Rule(174, false, false, 7, "174: void -> (JUMPC _ _40 label label)", ImList.list(ImList.list("cmptle",ImList.list("_r","$2"),ImList.list("_r","$1"),ImList.list("_r","%f25")),ImList.list("fbne",ImList.list("_r","%f25"),ImList.list("_jumplabel","$3"))), null, null, 0, false, false, new int[]{60,16,16}, new String[]{null, null, null, null, null});
    rulev[239] = new Rule(239, false, false, 7, "239: void -> (CALL _ fun)", ImList.list(ImList.list("jsr",ImList.list("_r","%26"),ImList.list("_jumplabel","$1")),ImList.list("ldgp",ImList.list("_r","%gp"),ImList.list("_lda","0",ImList.list("_r","%26")))), null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[240] = new Rule(240, false, false, 7, "240: void -> (CALL _ fun)", ImList.list(ImList.list("mov",ImList.list("_r","$1"),ImList.list("_r","%27")),ImList.list("jsr",ImList.list("_r","%26"),ImList.list("_jumplabel","%27")),ImList.list("ldgp",ImList.list("_r","%gp"),ImList.list("_lda","0",ImList.list("_r","%26")))), null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[241] = new Rule(241, false, false, 7, "241: void -> (PARALLEL _ void)", null, null, null, 0, false, false, new int[]{7}, new String[]{null, null});
    rulev[106] = new Rule(106, false, false, 7, "106: void -> (LIST _ regq _9)", ImList.list(ImList.list("ldq",ImList.list("_r","$1"),ImList.list("_mem",ImList.list("_r","$2")))), null, null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-I64*", null});
    rulev[107] = new Rule(107, false, false, 7, "107: void -> (LIST _ regl _10)", ImList.list(ImList.list("ldl",ImList.list("_r","$1"),ImList.list("_mem",ImList.list("_r","$2")))), null, null, 0, false, false, new int[]{2,30}, new String[]{null, "*reg-I32*", null});
    rulev[108] = new Rule(108, false, false, 7, "108: void -> (LIST _ regw _11)", ImList.list(ImList.list("ldw",ImList.list("_r","$1"),ImList.list("_mem",ImList.list("_r","$2")))), null, null, 0, false, false, new int[]{3,31}, new String[]{null, "*reg-I16*", null});
    rulev[109] = new Rule(109, false, false, 7, "109: void -> (LIST _ regb _12)", ImList.list(ImList.list("ldb",ImList.list("_r","$1"),ImList.list("_mem",ImList.list("_r","$2")))), null, null, 0, false, false, new int[]{4,32}, new String[]{null, "*reg-I08*", null});
    rulev[110] = new Rule(110, false, false, 7, "110: void -> (LIST _ regd _13)", ImList.list(ImList.list("ldt",ImList.list("_r","$1"),ImList.list("_mem",ImList.list("_r","$2")))), null, null, 0, false, false, new int[]{5,33}, new String[]{null, "*reg-F64*", null});
    rulev[111] = new Rule(111, false, false, 7, "111: void -> (LIST _ regf _14)", ImList.list(ImList.list("lds",ImList.list("_r","$1"),ImList.list("_mem",ImList.list("_r","$2")))), null, null, 0, false, false, new int[]{6,34}, new String[]{null, "*reg-F32*", null});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 1026: return "*reg-I64*";
    case 514: return "*reg-I32*";
    case 258: return "*reg-I16*";
    case 130: return "*reg-I08*";
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
    if (name == "_mem_tmp")
      return jmac2(form.elem(1));
    return null;
  }

  /** Expand building-macro, for LirNode **/
  Object quiltLir(LirNode node) {
    switch (node.opCode) {
    case Op.SUBREG:
      return jmac1(node);
    default:
      return quiltLirDefault(node);
    }
  }

  /** Expand emit-macro for list form. **/
  String emitList(ImList form, boolean topLevel) {
    String name = (String)form.elem();
    if (name == "prologue")
      return jmac3(form.elem(1));
    else if (name == "epilogue")
      return jmac4(form.elem(1), emitObject(form.elem(2)));
    else if (name == "_plus")
      return jmac5(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "_minus")
      return jmac6(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "_r")
      return jmac7(emitObject(form.elem(1)));
    else if (name == "_jumplabel")
      return jmac8(emitObject(form.elem(1)));
    else if (name == "_lda")
      return jmac9(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "_ldw")
      return jmac10(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "_mem")
      return jmac11(emitObject(form.elem(1)));
    else if (name == "deflabel")
      return jmac12(emitObject(form.elem(1)));
    else if (name == "_static")
      return jmac13(emitObject(form.elem(1)));
    else if (name == "line")
      return jmac14(emitObject(form.elem(1)));
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
  
  ImList regCallClobbers = new ImList(ImList.list("REG","I64","%0"), new ImList(ImList.list("REG","I64","%1"), new ImList(ImList.list("REG","I64","%2"), new ImList(ImList.list("REG","I64","%3"), new ImList(ImList.list("REG","I64","%4"), new ImList(ImList.list("REG","I64","%5"), new ImList(ImList.list("REG","I64","%6"), new ImList(ImList.list("REG","I64","%7"), new ImList(ImList.list("REG","I64","%8"), new ImList(ImList.list("REG","I64","%16"), new ImList(ImList.list("REG","I64","%17"), new ImList(ImList.list("REG","I64","%18"), new ImList(ImList.list("REG","I64","%19"), new ImList(ImList.list("REG","I64","%20"), new ImList(ImList.list("REG","I64","%21"), new ImList(ImList.list("REG","I64","%22"), new ImList(ImList.list("REG","I64","%23"), new ImList(ImList.list("REG","I64","%24"), new ImList(ImList.list("REG","I64","%25"), new ImList(ImList.list("REG","I64","%28"), new ImList(ImList.list("REG","F64","%f0"), new ImList(ImList.list("REG","F64","%f1"), new ImList(ImList.list("REG","F64","%f10"), new ImList(ImList.list("REG","F64","%f11"), new ImList(ImList.list("REG","F64","%f12"), new ImList(ImList.list("REG","F64","%f13"), new ImList(ImList.list("REG","F64","%f14"), new ImList(ImList.list("REG","F64","%f15"), new ImList(ImList.list("REG","F64","%f16"), new ImList(ImList.list("REG","F64","%f17"), new ImList(ImList.list("REG","F64","%f18"), new ImList(ImList.list("REG","F64","%f19"), new ImList(ImList.list("REG","F64","%f20"), new ImList(ImList.list("REG","F64","%f21"), new ImList(ImList.list("REG","F64","%f22"), new ImList(ImList.list("REG","F64","%f23"), new ImList(ImList.list("REG","F64","%f24"), new ImList(ImList.list("REG","F64","%f25"), ImList.list(ImList.list("REG","F64","%f26"),ImList.list("REG","F64","%f27"),ImList.list("REG","F64","%f28"),ImList.list("REG","F64","%f29"),ImList.list("REG","F64","%f30"))))))))))))))))))))))))))))))))))))))));
  
  /***** system parameters                                             *****/
  /*************************************************************************/
  static final int Regsize        =    8; /* Alpha 64bit(8byte) arch.      */
  static final int FirstArgument  =   16; /* to pass the first argument    */
  static final int NumRegArgument =    6; /* number of register argument   */
  
  /***** Alpha function attribute *****/
  /*************************************************************************/
  static class AlphaAttr extends FunctionAttr {
    String bitmask;     /* Bitmask String                      */
    int argc;           /* Number of Argument                  */
    int numCall;        /* Number of Call                      */
    int stackRequired;  /* Maximum stack space used by call    */
    /**********************************************************************/
    AlphaAttr(Function func) {
      super(func);
      bitmask = "0x400fe00"; /* savereg $15($fp), $26($ra) */
      argc    = 0;
      numCall = 0;
    }
  
  }
  
  /***** Rewrite FRAME node to target machine form. *****/
  /*************************************************************************/
  LirNode rewriteFrame(LirNode node) {
    Symbol fp = func.module.globalSymtab.get("%fp");
    
    /***** Placement AGGREGATE on stack frame ????? *****/
    SymAuto sym = (SymAuto)((LirSymRef)node).symbol;
    long _off = sym.offset();
    LirNode ret;
    ret = lir.node(Op.ADD, node.type, lir.symRef(fp),
  		 lir.iconst(I64, (long)(_off)));
    return ret;
  }
  
  
  /***** Early-time pre-rewriting sequence *****/
  /*************************************************************************/
  private final Transformer[] myEarlyTransSeq = {
    AggregateByReference.trig,
    localEarlyRewritingTrig
  };
  
  /** Return early time pre-rewriting sequence. **/
  /*************************************************************************/
  public Transformer[] earlyRewritingSequence() { return myEarlyTransSeq; }
  
  /***** Late-time pre-rewriting sequence. *****/
  /*************************************************************************/
  private final Transformer[] myLateTransSeq = {
    localLateRewritingTrig,
    ProcessFramesTrig
  };
  
  /***** Return late time pre-rewriting sequence. *****/
  /*************************************************************************/
  public Transformer[] lateRewritingSequence() { return myLateTransSeq; }
  
  /** Set register set for REG symbol. **/
  /*************************************************************************/
  void _setRegsetOf(Symbol sym, String regset) {
    if(regset == null)
      throw new CantHappenException("regset set to null");
    sym.setOpt(ImList.list("&regset", regset));
  }
  
  /** Get register set for REG symbol. **/
  /*************************************************************************/
  String _getRegsetOf(Symbol sym) {
    for (ImList p = sym.opt(); !p.atEnd(); p = p.next()) {
      if (p.elem() == "&regset")
        return (String)p.elem2nd();
    }
    return null;
  }
  
  /*************************************************************************/
  private LirNode nthArg(int no, int[] types, LirNode sp){
    if(no<NumRegArgument){
      switch (Type.tag(types[no])) {
      case Type.INT:
      case Type.AGGREGATE:
        return regnode(types[no], "%"  + (FirstArgument + no));
      case Type.FLOAT:
        return regnode(types[no], "%f" + (FirstArgument + no));
      default:
        throw new CantHappenException("Error: LirNode nthArg()" + no);
      }
    }
    else{	/***** Use the stack, not registers *****/
      int offset = (no - NumRegArgument) * Regsize;
      return lir.node(Op.MEM, types[no],
  		    lir.node(Op.ADD, I64, sp, lir.iconst(I64, offset)));
    }
  }
  
  /***** Rewrite PROLOGUE *****/
  /*************************************************************************/
  LirNode rewritePrologue(LirNode node, BiList post) {
    AlphaAttr at = (AlphaAttr)getFunctionAttr(func);
    int n = node.nKids();
    LirNode fp = regnode(I64, "%fp");
  
    at.argc = n - 1;
  
    int types[] = new int[n];
    int origArgc = 0;
    int firstNo = 0;
    if(func.origEpilogue.nKids() > 1
       && Type.tag(func.origEpilogue.kid(1).type)==Type.AGGREGATE){
      firstNo = 1;
      at.argc++;
    }
  
    for(int i=1; i<n; i++){
      LirNode arg = node.kid(i);
      types[firstNo+i-1] = arg.type;
  
      LirNode src = nthArg(firstNo+i-1, types, fp);
  
      post.add(lir.node(Op.SET, arg.type, arg, src));
      origArgc++;
    }
  
    LirNode[] argv = new LirNode[origArgc+1];
    argv[0] = node.kid(0);
    for(int i=0; i<origArgc; i++) argv[i+1] = nthArg(firstNo+i, types, fp);
    return lir.node(Op.PROLOGUE, Type.UNKNOWN, argv);
  }
  
  /*************************************************************************/
  private LirNode regnode(int type, String name) {
    LirNode node, nodeI32, nodeI16, nodeI08, nodeF32;
    int tag = Type.tag(type);
    node    = lir.symRef(module.globalSymtab.get(name));
    nodeI32 = lir.node(Op.SUBREG, I32, node, lir.untaggedIconst(I64, 0));
    nodeI16 = lir.node(Op.SUBREG, I16, node, lir.untaggedIconst(I64, 0));
    nodeI08 = lir.node(Op.SUBREG, I8,  node, lir.untaggedIconst(I64, 0));
    nodeF32 = lir.node(Op.SUBREG, F32, node, lir.untaggedIconst(I64, 0));
  
    if(type == I64)      return node;
    else if(type == I32) return nodeI32;
    else if(type == I16) return nodeI16;
    else if(type == I8 ) return nodeI08;
    else if(type == F64) return node;
    else if(type == F32) return nodeF32;
    return null;
  }
  
  /***** Return the register for value returned. *****/
  /*************************************************************************/
  LirNode returnReg(int type) {
    switch(Type.tag(type)){
    case Type.INT:
      switch(Type.bytes(type)){
      case 1:
      case 2:
      case 4: 
      case 8: return regnode(type, "%0"); /** why not %0 ????? **/
      default: return null;
      }
    case Type.FLOAT:
      switch (Type.bytes(type)){
      case 4:
      case 8: return regnode(type, "%f0");
      default: return null;
      }
    case Type.AGGREGATE:
      return regnode(I64, "%0");
    default: return null;
    }
  }
  
  /***** Rewrite EPILOGUE *****/
  /*************************************************************************/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
    if (node.nKids() < 2) {
      return node;
    }
  
    LirNode ret = node.kid(1);
    LirNode tmp = func.newTemp(ret.type);
    LirNode reg = returnReg(ret.type);
    LirNode fp  = regnode(I64, "%fp");
  
    switch (Type.tag(ret.type)) {
    case Type.INT:
    case Type.FLOAT:
      if(isComplex(ret)) {
        pre.add(lir.node(Op.SET, tmp.type, tmp, ret));
        pre.add(lir.node(Op.SET, reg.type, reg, tmp));
      } else
        pre.add(lir.node(Op.SET, reg.type, reg, ret));
  
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
    case Type.AGGREGATE:
      pre.add(lir.node
  	    (Op.SET, I64, reg, regnode(I64, "%"+FirstArgument)));
      pre.add(lir.node
  	    (Op.SET, ret.type, lir.node
  	     (Op.MEM, ret.type, reg), ret));
      return node;
    default:
      throw new CantHappenException("error_in_rewriteEpilogue");
    }
  }
  
  /***** Return true if node is a complex one *****/
  /*************************************************************************/
  boolean isComplex(LirNode node) {
    switch(node.opCode){
    case Op.FLOATCONST:
    case Op.INTCONST  :
    case Op.REG       :
    case Op.STATIC    :
    case Op.FRAME     : return false;
    default           : return true;
    }
  }
  
  /***** Rewrite CALL node. *****/
  /*************************************************************************/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
    AlphaAttr at = (AlphaAttr)getFunctionAttr(func);
    BiList list1 = new BiList();
    BiList list2 = new BiList();
    LirNode sp   = regnode(I64, "%sp");
  
    LirNode callee = node.kid(0);
    LirNode args  = node.kid(1);
    LirNode ret   = null;
  
    at.numCall++;
  
    int paramCount = 0;
  
    if(node.kid(2).nKids()>0) ret=node.kid(2).kid(0);
  
    boolean reta = false;
    if(ret != null && Type.tag(ret.type) == Type.AGGREGATE){
      LirNode dest = regnode(I64, "%"+FirstArgument);
      list1.add(lir.node(Op.SET, I64, dest, ret.kid(0)));
      reta = true;
      paramCount++;
    }
  
    /***** parameters *****/
  
    int n = args.nKids();
    int types[] = new int[n+1];
  
    for(int i=0; i<n; i++){
      LirNode arg = args.kid(i);
      types[paramCount] = arg.type;
  
      LirNode temp = func.newTemp(arg.type);
      LirNode dest = nthArg(paramCount++, types, sp);
  
      switch (Type.tag(arg.type)){
      case Type.INT:
      case Type.FLOAT:
        if(dest.opCode == Op.MEM || isComplex(arg)){
          list1.add(lir.node(Op.SET, temp.type, temp, arg));
          list2.add(lir.node(Op.SET, dest.type, dest, temp));
        } else {
          list2.add(lir.node(Op.SET, dest.type, dest, arg));
        }
        break;
      default:
        throw new CantHappenException("Unexpected rewriteCALL");
      }
    }
  
    // round up to 8byte boundary
    int required = ((n - NumRegArgument) * 8 + 7) & -8; 
    if(at.stackRequired < required) at.stackRequired = required;
  
    /*************** ????? ************/
    LirNode[] newargv = new LirNode[n];
    int j=0;
    for(BiLink p=list2.first(); !p.atEnd(); p=p.next()){
      LirNode ins = (LirNode)p.elem();
      newargv[j++] = ins.kid(0);
    }
    try {
      node = lir.node
        (Op.PARALLEL, Type.UNKNOWN,
         noRescan(lir.operator
         (Op.CALL, Type.UNKNOWN,
  	node.kid(0),
  	lir.node(Op.LIST, Type.UNKNOWN, newargv),
          node.kid(2),
          reta ? ImList.list("&reta", new Integer(Type.bytes(ret.type)))
          : ImList.list())),
         lir.decodeLir(new ImList("CLOBBER", regCallClobbers), func, module));
    } catch (SyntaxError e) {
      throw new CantHappenException();
    }
    
    pre.concatenate(list1);
    pre.concatenate(list2);
  
    /***** the function returns value using $0 *****/
    if(ret!=null && ret.opCode!=Op.FRAME){
      switch(Type.tag(ret.type)){
      case Type.INT  :
      case Type.FLOAT:{
        LirNode tmp = func.newTemp(ret.type);
        LirNode reg = returnReg(ret.type);
        post.add(lir.node(Op.SET, ret.type, tmp, reg));
        post.add(lir.node(Op.SET, ret.type, ret, tmp));
        node.kid(0).kid(2).setKid(0, reg);
        break;
      }
      case Type.AGGREGATE:
        break;
      }
    }
    return node;
  }
  
  /*************************************************************************/
  String segmentForConst() { return ".rdata"; }
  
  /** Return the alignment bytes for specified type. **/
  public int alignForType(int type) {
    switch (Type.bytes(type)) {
    case 1: case 3: case 5: case 7: return 1;
    case 2: case 6:                 return 2;
    case 4:                         return 4;
    case 8:                         return 8;
    default:                        return 8;
    }
  }
  
  /***** simple functions *****/
  /*************************************************************************/
  void emitComment(PrintWriter out, String comment) { /* do nothing */ }
  void emitBeginningOfModule(PrintWriter out){ /* do nothing */ }
  void emitEndOfModule(PrintWriter out){ /* do nothing */ }
  void emitEndOfSegment(PrintWriter out, String segment){ /* do nothing */ }
  FunctionAttr newFunctionAttr(Function func) { return new AlphaAttr(func);}
  
  /*************************************************************************/
  void emitBeginningOfSegment(PrintWriter out, String segment) {
    out.println(segment);
  }
  
  /*************************************************************************/
  void emitDataLabel(PrintWriter out, String label) {
    String name = label;
    if(name.startsWith("string")) // "string." --> "$LC"
      name = "$LC" + name.substring(name.indexOf(".")+1);  
    out.println(name + ":");
  }
  
  /*************************************************************************/
  void emitCodeLabel(PrintWriter out, String label){
    if(label.charAt(0)!='.') out.println(label + ":");
    else out.println("$" + label.substring(1) + ":");
  }
  
  /*************************************************************************/
  void emitLinkage(PrintWriter out, SymStatic symbol) {
    if(symbol.linkage == "XDEF"){
      out.println("\t.globl\t" + symbol.name);
      if(symbol.segment == ".text") out.println("\t.ent\t" + symbol.name);
    } else if (symbol.linkage == "LDEF") {
      if(symbol.segment == ".text")
        if(!symbol.name.startsWith("string.") && symbol.type==Type.UNKNOWN)
          out.println("\t.ent\t" + symbol.name);
    }
  }
  
  /*************************************************************************/
  void emitZeros(PrintWriter out, int bytes) {
    if (bytes > 0)
      out.println("\t.space\t" + bytes);
  }
  
  /*************************************************************************/
  void emitData(PrintWriter out, int type, LirNode node){
    if(type == I64){
      String value = lexpConv.convert(node);
      if(value.startsWith("string")) // "string." --> "$LC"
        value = "$LC" + value.substring(value.indexOf(".")+1);  
      out.println("\t.quad\t" + value);
    }
    else if(type == I32){
      out.println("\t.long\t" + lexpConv.convert(node));
    }
    else if(type == I16){
      out.println("\t.word\t" + ((LirIconst)node).signedValue());
    }
    else if(type == I8){
      out.println("\t.byte\t" + ((LirIconst)node).signedValue());
    }
    else if(type == F64){
      out.println("\t.t_floating\t" + ((LirFconst)node).value);
    }
    else if(type == F32){
      int bits = Float.floatToIntBits((float)((LirFconst)node).value);
      out.println("\t.long\t0x" + Integer.toHexString(bits));
    }
    else{
      throw new CantHappenException("unknown type: " + type);
    }
  }
  
  /***** Code building macros *****/
  /*************************************************************************/
  /*************************************************************************/
  Object jmac1(LirNode x){ /** ???????????????? **/
    Symbol reg = ((LirSymRef)x.kid(0)).symbol;
    int dtype = x.type;
    int offset = (int)((LirIconst)x.kid(1)).value;
    if(dtype==F32 && offset==1)
      return "%f" + (Integer.parseInt(reg.name.substring(2)) + 1);
    else
      return reg.name;
  }
  /*************************************************************************/
  Object jmac2(Object name) {
    SymAuto sym = reserveFrame((String)name, I64);
    return sym.offset() + "($fp)";
  }
  
  /***** Code emission macros *****/
  /*************************************************************************/
  /*************************************************************************/
  void emitIdent(PrintWriter out, String str){
    //##76 out.println("\t# Coins Compiler version 1.4.1");
    out.println("\t# Coins Compiler version " + Version.version);  //##76
    out.println("\t# Alpha TMD version 0.6.2");
    out.println("\t.set noreorder");
    out.println("\t.set volatile");
    out.println("\t.set noat");
    out.println("\t.arch ev4");
  }
  
  /*************************************************************************/
  String jmac3(Object f){
    Function func = (Function)f;
    AlphaAttr at  = (AlphaAttr)getFunctionAttr(func);
    String str_header  = new String();
    String str         = new String();
    int size;
    int offset = at.stackRequired;
  
    /*****  *****/
    if( 0 < at.numCall ) {
      SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
      NumberSet.Iterator it  = saveList.calleeSave.iterator();
      while ( it.hasNext() ) {
        int    reg  = it.next();
        String dest = machineParams.registerToString(reg);
        
        dest = "$" + dest.substring(1);
  
        if(!dest.startsWith("$f"))
  	str += "\tstq\t" + dest + "," + offset + "($sp)\n";
        else
  	str += "\tstt\t" + dest + "," + offset + "($sp)\n";
  
        offset += Regsize;
      }
  
      str += "\tstq\t$26," + offset + "($sp)\n";
      offset += Regsize;
    }
  
    /*****  *****/
    size = frameSize(func) + offset;
    size = (size + 7) & -8; // round up to 8byte boundary
  
    if(0<size||0<at.argc-NumRegArgument) {
      str += "\tstq\t$fp," + offset + "($sp)\n";
      size += Regsize;
    }
  
    if(0<size && size<=255)
      str_header += "\tsubq\t$sp," + size + ",$sp\n";
    else if(0<size)
      str_header += "\tlda\t$sp," + "-" + size + "($sp)\n";
    
    if(0<size)
      str += "\taddq\t$sp," +  size  + ",$fp\n";
    
  
    str_header  = "\t.mask\t" + at.bitmask + ",-" + size + "\n"
                + "\tldgp\t$gp,0($27)\n"
                + str_header;
  
    return str_header + str;
  }
  
  /*************************************************************************/
  String jmac4(Object f, String rettype) {
    Function func = (Function)f;
    AlphaAttr at  = (AlphaAttr)getFunctionAttr(func);
    String str = new String();
    int size;
    int offset = at.stackRequired;
  
    /*****  *****/
    if( 0 < at.numCall ) {
      SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
      NumberSet.Iterator  it = saveList.calleeSave.iterator();
      while ( it.hasNext() ) {
        int    reg  = it.next();
        String dest = machineParams.registerToString(reg);
        
        dest = "$" + dest.substring(1);
  
        if(!dest.startsWith("$f"))
  	str += "\tldq\t" + dest + "," + offset + "($sp)\n";
        else
  	str += "\tldt\t" + dest + "," + offset + "($sp)\n";
  
        offset += Regsize;
      }
  
      str += "\tldq\t$26," + offset + "($sp)\n";
      offset += Regsize;
  
    }
  
    /*****  *****/
    size = frameSize(func) + offset;
    size = (size + 7) & -8; // round up to 8byte boundary
  
    if(0<size||0<at.argc-NumRegArgument) {
      str += "\tldq\t$fp," + offset + "($sp)\n";
      size += Regsize;
    }
  
    if(0<size && size<=255)
      str += "\taddq\t$sp," + size + ",$sp\n";
    else if(0<size)
      str += "\tlda\t$sp," + size + "($sp)\n";
    
    str += "\tret\t$31,($26),1\n";
    str += "\t.end\t" + func.symbol.name + "\n";
  
    return str;
  }
  
  /*************************************************************************/
  String jmac5(String x, String y){
    if(y.charAt(0) == '-') return x + y;
    else return x + "+" + y;
  }
  
  /*************************************************************************/
  String jmac6(String x, String y){
    if (y.charAt(0) == '-') return x + "+" + y.substring(1);
    else return x + "-" + y;
  }
  
  /*************************************************************************/
  String jmac7(String x){ // rename %reg -> $reg
    if(x.charAt(0)=='%') return "$" + x.substring(1);
    else return x;
  }
  
  /*************************************************************************/
  String jmac8(String x){ // rename .LABEL -> $LABEL
    if(x.charAt(0)=='.')       return "$" + x.substring(1);
    else if (x.charAt(0)=='%') return "($" + x.substring(1) + "),0";
    else return x;
  }
  
  /*************************************************************************/
  String jmac9(String x, String y){ return x + "(" + y + ")"; }
  
  /*************************************************************************/
  String jmac10(String dest, String src) {
    long val = Long.parseLong(src.toString());
    long bound = (1<<15);
  
    if(0 == val)
      return "\tclr\t" + dest + "\n";
    if((-bound <= val) && (val < bound))
      return "\tmov\t" + val + "," + dest + "\n";
    return "\tlda\t" + dest + "," + val + "\n";
  
  }
  
  /*************************************************************************/
  String jmac11(String x){
    int pos_p = x.indexOf('+'); /* string position of '+' */
    int pos_m = x.indexOf('-'); /* string position of '-' */
  
    if(pos_p<=0 && pos_m<=0 && x.charAt(0)=='$'){ /* x is register alone */
      return "0(" + x + ")";
    }
    else if(pos_p<=0 && pos_m<=0) {
      return x;
    }
    else if(pos_p>0 && pos_m<=0 ){ /** $reg + con ->  con($reg) **/
      return x.substring(pos_p+1) + "(" + x.substring(0, pos_p) + ")";
    }
    else if(pos_p<=0 && pos_m>0 ){ /** $reg - con -> -con($reg) **/
      return x.substring(pos_m) + "(" + x.substring(0, pos_m) + ")";
    }
  
    return "wrong_mem";
  }
  
  /*************************************************************************/
  String jmac12(String x){ ///// rename .LABEL -> $LABEL:
    if(x.charAt(0)=='.') return "$" + x.substring(1) + ":";
    return x + ":";
  }
  
  /*************************************************************************/
  String jmac13(String x) {
    String name = x;
    if(x.startsWith("string"))
      name = "$LC" + x.substring(x.indexOf(".")+1); // "string." --> "$LC"
    return name;
  }
  
  /*************************************************************************/
  String jmac14(String x) { return "\t# line " + x; }
}
