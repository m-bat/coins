/*
Productions:
 1: xregb -> (REG I8)
 2: xregb -> (SUBREG I8)
 3: xregh -> (REG I16)
 4: xregh -> (SUBREG I16)
 5: xregl -> (REG I32)
 6: xregl -> (SUBREG I32)
 7: xregq -> (REG I64)
 8: xregq -> (SUBREG I64)
 9: xregf -> (REG F32)
 10: xregf -> (SUBREG F32)
 11: xregd -> (REG F64)
 12: xregd -> (SUBREG F64)
 13: regb -> xregb
 14: regh -> xregh
 15: regl -> xregl
 16: regq -> xregq
 17: regf -> xregf
 18: regd -> xregd
 19: addr -> regl
 20: addr -> con13
 21: addr -> (ADD I32 regl regl)
 22: addr -> (ADD I32 regl con13)
 23: addr -> (SUB I32 regl negcon13)
 24: fun -> addr
 25: fun -> asmcon
 26: con -> (INTCONST _)
 27: sta -> (STATIC I32)
 28: asmcon -> con
 29: asmcon -> sta
 30: asmcon -> (ADD I32 asmcon con)
 31: asmcon -> (SUB I32 asmcon con)
 32: con13 -> (INTCONST _)
 33: negcon13 -> (INTCONST _)
 34: con5 -> (INTCONST _)
 35: rc -> regl
 36: rc -> con13
 37: rcs -> regl
 38: rcs -> con5
 39: regl -> con13
 40: regh -> con13
 41: regb -> con13
 42: regq -> con13
 43: regq -> con
 44: regl -> asmcon
 45: regh -> asmcon
 46: void -> (SET I32 xregl regl)
 47: void -> (SET I16 xregh regh)
 48: void -> (SET I8 xregb regb)
 49: _1 -> (INTCONST _ 0)
 50: _2 -> (SUBREG I32 xregq _1)
 51: void -> (SET I32 _2 regl)
 52: _3 -> (INTCONST _ 1)
 53: _4 -> (SUBREG I32 xregq _3)
 54: void -> (SET I32 _4 regl)
 55: regl -> (SUBREG I32 xregq _1)
 56: regl -> (SUBREG I32 xregq _3)
 57: void -> (SET I64 xregq regq)
 58: void -> (SET F32 xregf regf)
 59: void -> (SET F64 xregd regd)
 60: regl -> (MEM I32 addr)
 61: regh -> (MEM I16 addr)
 62: regb -> (MEM I8 addr)
 63: regf -> (MEM F32 addr)
 64: regq -> (MEM I64 addr)
 65: _5 -> (MEM I64 addr)
 66: regl -> (SUBREG I32 _5 _3)
 67: regl -> (SUBREG I32 _5 _1)
 68: _6 -> (MEM I16 addr)
 69: regl -> (CONVSX I32 _6)
 70: _7 -> (MEM I8 addr)
 71: regl -> (CONVSX I32 _7)
 72: regl -> (CONVZX I32 _6)
 73: regl -> (CONVZX I32 _7)
 74: regd -> (MEM F64 addr)
 75: regd -> (MEM F64 addr)
 76: void -> (SET I64 _5 regq)
 77: _8 -> (MEM F32 addr)
 78: void -> (SET F32 _8 regf)
 79: _9 -> (MEM I32 addr)
 80: void -> (SET I32 _9 regl)
 81: void -> (SET I16 _6 regh)
 82: void -> (SET I8 _7 regb)
 83: _10 -> (CONVIT I16 regl)
 84: void -> (SET I16 _6 _10)
 85: _11 -> (CONVIT I8 regl)
 86: void -> (SET I8 _7 _11)
 87: _12 -> (MEM F64 addr)
 88: void -> (SET F64 _12 regd)
 89: void -> (SET F64 _12 regd)
 90: label -> (LABEL _)
 91: void -> (JUMP _ label)
 92: _13 -> (TSTEQ I32 regq regq)
 93: void -> (JUMPC _ _13 label label)
 94: _14 -> (TSTNE I32 regq regq)
 95: void -> (JUMPC _ _14 label label)
 96: _15 -> (TSTLTS I32 regq regq)
 97: void -> (JUMPC _ _15 label label)
 98: _16 -> (TSTLES I32 regq regq)
 99: void -> (JUMPC _ _16 label label)
 100: _17 -> (TSTGTS I32 regq regq)
 101: void -> (JUMPC _ _17 label label)
 102: _18 -> (TSTGES I32 regq regq)
 103: void -> (JUMPC _ _18 label label)
 104: _19 -> (TSTLTU I32 regq regq)
 105: void -> (JUMPC _ _19 label label)
 106: _20 -> (TSTLEU I32 regq regq)
 107: void -> (JUMPC _ _20 label label)
 108: _21 -> (TSTGTU I32 regq regq)
 109: void -> (JUMPC _ _21 label label)
 110: _22 -> (TSTGEU I32 regq regq)
 111: void -> (JUMPC _ _22 label label)
 112: _23 -> (TSTEQ I32 regl rc)
 113: void -> (JUMPC _ _23 label label)
 114: _24 -> (TSTNE I32 regl rc)
 115: void -> (JUMPC _ _24 label label)
 116: _25 -> (TSTLTS I32 regl rc)
 117: void -> (JUMPC _ _25 label label)
 118: _26 -> (TSTLES I32 regl rc)
 119: void -> (JUMPC _ _26 label label)
 120: _27 -> (TSTGTS I32 regl rc)
 121: void -> (JUMPC _ _27 label label)
 122: _28 -> (TSTGES I32 regl rc)
 123: void -> (JUMPC _ _28 label label)
 124: _29 -> (TSTLTU I32 regl rc)
 125: void -> (JUMPC _ _29 label label)
 126: _30 -> (TSTLEU I32 regl rc)
 127: void -> (JUMPC _ _30 label label)
 128: _31 -> (TSTGTU I32 regl rc)
 129: void -> (JUMPC _ _31 label label)
 130: _32 -> (TSTGEU I32 regl rc)
 131: void -> (JUMPC _ _32 label label)
 132: _33 -> (TSTEQ I32 rc regl)
 133: void -> (JUMPC _ _33 label label)
 134: _34 -> (TSTNE I32 rc regl)
 135: void -> (JUMPC _ _34 label label)
 136: _35 -> (TSTLTS I32 rc regl)
 137: void -> (JUMPC _ _35 label label)
 138: _36 -> (TSTLES I32 rc regl)
 139: void -> (JUMPC _ _36 label label)
 140: _37 -> (TSTGTS I32 rc regl)
 141: void -> (JUMPC _ _37 label label)
 142: _38 -> (TSTGES I32 rc regl)
 143: void -> (JUMPC _ _38 label label)
 144: _39 -> (TSTLTU I32 rc regl)
 145: void -> (JUMPC _ _39 label label)
 146: _40 -> (TSTLEU I32 rc regl)
 147: void -> (JUMPC _ _40 label label)
 148: _41 -> (TSTGTU I32 rc regl)
 149: void -> (JUMPC _ _41 label label)
 150: _42 -> (TSTGEU I32 rc regl)
 151: void -> (JUMPC _ _42 label label)
 152: _43 -> (TSTEQ I32 regf regf)
 153: void -> (JUMPC _ _43 label label)
 154: _44 -> (TSTEQ I32 regd regd)
 155: void -> (JUMPC _ _44 label label)
 156: _45 -> (TSTNE I32 regf regf)
 157: void -> (JUMPC _ _45 label label)
 158: _46 -> (TSTNE I32 regd regd)
 159: void -> (JUMPC _ _46 label label)
 160: _47 -> (TSTLTS I32 regf regf)
 161: void -> (JUMPC _ _47 label label)
 162: _48 -> (TSTLTS I32 regd regd)
 163: void -> (JUMPC _ _48 label label)
 164: _49 -> (TSTLES I32 regf regf)
 165: void -> (JUMPC _ _49 label label)
 166: _50 -> (TSTLES I32 regd regd)
 167: void -> (JUMPC _ _50 label label)
 168: _51 -> (TSTGTS I32 regf regf)
 169: void -> (JUMPC _ _51 label label)
 170: _52 -> (TSTGTS I32 regd regd)
 171: void -> (JUMPC _ _52 label label)
 172: _53 -> (TSTGES I32 regf regf)
 173: void -> (JUMPC _ _53 label label)
 174: _54 -> (TSTGES I32 regd regd)
 175: void -> (JUMPC _ _54 label label)
 176: regq -> (ADD I64 regq regq)
 177: regq -> (SUB I64 regq regq)
 178: regq -> (BAND I64 regq regq)
 179: regq -> (BOR I64 regq regq)
 180: regq -> (BXOR I64 regq regq)
 181: regq -> (NEG I64 regq)
 182: regq -> (LSHS I64 regq con)
 183: regq -> (LSHS I64 regq con)
 184: regq -> (LSHS I64 regq con)
 185: regq -> (LSHS I64 regq regl)
 186: regq -> (RSHS I64 regq con)
 187: regq -> (RSHS I64 regq con)
 188: regq -> (RSHS I64 regq con)
 189: regq -> (RSHS I64 regq regl)
 190: regq -> (RSHU I64 regq con)
 191: regq -> (RSHU I64 regq con)
 192: regq -> (RSHU I64 regq con)
 193: regq -> (RSHU I64 regq regl)
 194: regq -> (BNOT I64 regq)
 195: regl -> (ADD I32 regl rc)
 196: regl -> (SUB I32 regl rc)
 197: regl -> (BAND I32 regl rc)
 198: regl -> (BOR I32 regl rc)
 199: regl -> (BXOR I32 regl rc)
 200: regl -> (ADD I32 rc regl)
 201: regl -> (BAND I32 rc regl)
 202: regl -> (BOR I32 rc regl)
 203: regl -> (BXOR I32 rc regl)
 204: regl -> (NEG I32 regl)
 205: regl -> (BNOT I32 regl)
 206: regl -> (RSHS I32 regl rcs)
 207: regl -> (RSHU I32 regl rcs)
 208: regl -> (LSHS I32 regl rcs)
 209: regq -> (MUL I64 regq regq)
 210: regq -> (DIVS I64 regq regq)
 211: regq -> (DIVU I64 regq regq)
 212: regq -> (MODS I64 regq regq)
 213: regq -> (MODU I64 regq regq)
 214: regl -> (MUL I32 regl regl)
 215: regl -> (DIVS I32 regl regl)
 216: regl -> (DIVU I32 regl regl)
 217: regl -> (MODS I32 regl regl)
 218: regl -> (MODU I32 regl regl)
 219: regl -> (MUL I32 regl rc)
 220: regl -> (MUL I32 rc regl)
 221: regl -> (MUL I32 regl rc)
 222: regl -> (MUL I32 rc regl)
 223: regl -> (MUL I32 regl rc)
 224: regl -> (MUL I32 rc regl)
 225: regl -> (MUL I32 regl rc)
 226: regl -> (MUL I32 rc regl)
 227: regl -> (MUL I32 regl rc)
 228: regl -> (MUL I32 rc regl)
 229: regl -> (MUL I32 regl rc)
 230: regl -> (MUL I32 rc regl)
 231: regl -> (MUL I32 regl rc)
 232: regl -> (MUL I32 rc regl)
 233: regl -> (DIVS I32 regl rc)
 234: regl -> (DIVU I32 regl rc)
 235: regl -> (MODS I32 regl rc)
 236: regl -> (MODU I32 regl rc)
 237: regf -> (ADD F32 regf regf)
 238: regd -> (ADD F64 regd regd)
 239: regf -> (SUB F32 regf regf)
 240: regd -> (SUB F64 regd regd)
 241: regf -> (MUL F32 regf regf)
 242: regd -> (MUL F64 regd regd)
 243: regf -> (DIVS F32 regf regf)
 244: regd -> (DIVS F64 regd regd)
 245: regf -> (NEG F32 regf)
 246: regd -> (NEG F64 regd)
 247: regq -> (CONVSX I64 regl)
 248: regq -> (CONVSX I64 regh)
 249: regq -> (CONVSX I64 regb)
 250: regl -> (CONVSX I32 regh)
 251: regl -> (CONVSX I32 regb)
 252: regh -> (CONVSX I16 regb)
 253: regq -> (CONVZX I64 regl)
 254: regq -> (CONVZX I64 regh)
 255: regq -> (CONVZX I64 regb)
 256: regl -> (CONVZX I32 regh)
 257: regl -> (CONVZX I32 regb)
 258: regh -> (CONVZX I16 regb)
 259: regl -> (CONVIT I32 regq)
 260: regh -> (CONVIT I16 regq)
 261: regb -> (CONVIT I8 regq)
 262: regh -> (CONVIT I16 regl)
 263: regb -> (CONVIT I8 regl)
 264: regb -> (CONVIT I8 regh)
 265: regd -> (CONVFX F64 regf)
 266: regf -> (CONVFT F32 regd)
 267: regq -> (CONVFS I64 regf)
 268: regq -> (CONVFS I64 regd)
 269: regl -> (CONVFS I32 regf)
 270: regh -> (CONVFS I16 regf)
 271: regb -> (CONVFS I8 regf)
 272: regl -> (CONVFS I32 regd)
 273: regh -> (CONVFS I16 regd)
 274: regb -> (CONVFS I8 regd)
 275: regf -> (CONVSF F32 regq)
 276: regd -> (CONVSF F64 regq)
 277: regf -> (CONVSF F32 regl)
 278: regd -> (CONVSF F64 regl)
 279: void -> (CALL _ fun)
 280: void -> (CALL _ fun)
 281: void -> (PARALLEL _ void)
*/
/*
Sorted Productions:
 19: addr -> regl
 35: rc -> regl
 37: rcs -> regl
 13: regb -> xregb
 14: regh -> xregh
 15: regl -> xregl
 16: regq -> xregq
 17: regf -> xregf
 18: regd -> xregd
 24: fun -> addr
 20: addr -> con13
 36: rc -> con13
 39: regl -> con13
 40: regh -> con13
 41: regb -> con13
 42: regq -> con13
 25: fun -> asmcon
 44: regl -> asmcon
 45: regh -> asmcon
 28: asmcon -> con
 43: regq -> con
 29: asmcon -> sta
 38: rcs -> con5
 26: con -> (INTCONST _)
 32: con13 -> (INTCONST _)
 33: negcon13 -> (INTCONST _)
 34: con5 -> (INTCONST _)
 49: _1 -> (INTCONST _ 0)
 52: _3 -> (INTCONST _ 1)
 27: sta -> (STATIC I32)
 1: xregb -> (REG I8)
 3: xregh -> (REG I16)
 5: xregl -> (REG I32)
 9: xregf -> (REG F32)
 7: xregq -> (REG I64)
 11: xregd -> (REG F64)
 2: xregb -> (SUBREG I8)
 4: xregh -> (SUBREG I16)
 6: xregl -> (SUBREG I32)
 50: _2 -> (SUBREG I32 xregq _1)
 53: _4 -> (SUBREG I32 xregq _3)
 55: regl -> (SUBREG I32 xregq _1)
 56: regl -> (SUBREG I32 xregq _3)
 66: regl -> (SUBREG I32 _5 _3)
 67: regl -> (SUBREG I32 _5 _1)
 10: xregf -> (SUBREG F32)
 8: xregq -> (SUBREG I64)
 12: xregd -> (SUBREG F64)
 90: label -> (LABEL _)
 204: regl -> (NEG I32 regl)
 245: regf -> (NEG F32 regf)
 181: regq -> (NEG I64 regq)
 246: regd -> (NEG F64 regd)
 21: addr -> (ADD I32 regl regl)
 22: addr -> (ADD I32 regl con13)
 30: asmcon -> (ADD I32 asmcon con)
 195: regl -> (ADD I32 regl rc)
 200: regl -> (ADD I32 rc regl)
 237: regf -> (ADD F32 regf regf)
 176: regq -> (ADD I64 regq regq)
 238: regd -> (ADD F64 regd regd)
 23: addr -> (SUB I32 regl negcon13)
 31: asmcon -> (SUB I32 asmcon con)
 196: regl -> (SUB I32 regl rc)
 239: regf -> (SUB F32 regf regf)
 177: regq -> (SUB I64 regq regq)
 240: regd -> (SUB F64 regd regd)
 214: regl -> (MUL I32 regl regl)
 219: regl -> (MUL I32 regl rc)
 220: regl -> (MUL I32 rc regl)
 221: regl -> (MUL I32 regl rc)
 222: regl -> (MUL I32 rc regl)
 223: regl -> (MUL I32 regl rc)
 224: regl -> (MUL I32 rc regl)
 225: regl -> (MUL I32 regl rc)
 226: regl -> (MUL I32 rc regl)
 227: regl -> (MUL I32 regl rc)
 228: regl -> (MUL I32 rc regl)
 229: regl -> (MUL I32 regl rc)
 230: regl -> (MUL I32 rc regl)
 231: regl -> (MUL I32 regl rc)
 232: regl -> (MUL I32 rc regl)
 241: regf -> (MUL F32 regf regf)
 209: regq -> (MUL I64 regq regq)
 242: regd -> (MUL F64 regd regd)
 215: regl -> (DIVS I32 regl regl)
 233: regl -> (DIVS I32 regl rc)
 243: regf -> (DIVS F32 regf regf)
 210: regq -> (DIVS I64 regq regq)
 244: regd -> (DIVS F64 regd regd)
 216: regl -> (DIVU I32 regl regl)
 234: regl -> (DIVU I32 regl rc)
 211: regq -> (DIVU I64 regq regq)
 217: regl -> (MODS I32 regl regl)
 235: regl -> (MODS I32 regl rc)
 212: regq -> (MODS I64 regq regq)
 218: regl -> (MODU I32 regl regl)
 236: regl -> (MODU I32 regl rc)
 213: regq -> (MODU I64 regq regq)
 252: regh -> (CONVSX I16 regb)
 69: regl -> (CONVSX I32 _6)
 71: regl -> (CONVSX I32 _7)
 250: regl -> (CONVSX I32 regh)
 251: regl -> (CONVSX I32 regb)
 247: regq -> (CONVSX I64 regl)
 248: regq -> (CONVSX I64 regh)
 249: regq -> (CONVSX I64 regb)
 258: regh -> (CONVZX I16 regb)
 72: regl -> (CONVZX I32 _6)
 73: regl -> (CONVZX I32 _7)
 256: regl -> (CONVZX I32 regh)
 257: regl -> (CONVZX I32 regb)
 253: regq -> (CONVZX I64 regl)
 254: regq -> (CONVZX I64 regh)
 255: regq -> (CONVZX I64 regb)
 85: _11 -> (CONVIT I8 regl)
 261: regb -> (CONVIT I8 regq)
 263: regb -> (CONVIT I8 regl)
 264: regb -> (CONVIT I8 regh)
 83: _10 -> (CONVIT I16 regl)
 260: regh -> (CONVIT I16 regq)
 262: regh -> (CONVIT I16 regl)
 259: regl -> (CONVIT I32 regq)
 265: regd -> (CONVFX F64 regf)
 266: regf -> (CONVFT F32 regd)
 271: regb -> (CONVFS I8 regf)
 274: regb -> (CONVFS I8 regd)
 270: regh -> (CONVFS I16 regf)
 273: regh -> (CONVFS I16 regd)
 269: regl -> (CONVFS I32 regf)
 272: regl -> (CONVFS I32 regd)
 267: regq -> (CONVFS I64 regf)
 268: regq -> (CONVFS I64 regd)
 275: regf -> (CONVSF F32 regq)
 277: regf -> (CONVSF F32 regl)
 276: regd -> (CONVSF F64 regq)
 278: regd -> (CONVSF F64 regl)
 197: regl -> (BAND I32 regl rc)
 201: regl -> (BAND I32 rc regl)
 178: regq -> (BAND I64 regq regq)
 198: regl -> (BOR I32 regl rc)
 202: regl -> (BOR I32 rc regl)
 179: regq -> (BOR I64 regq regq)
 199: regl -> (BXOR I32 regl rc)
 203: regl -> (BXOR I32 rc regl)
 180: regq -> (BXOR I64 regq regq)
 205: regl -> (BNOT I32 regl)
 194: regq -> (BNOT I64 regq)
 208: regl -> (LSHS I32 regl rcs)
 182: regq -> (LSHS I64 regq con)
 183: regq -> (LSHS I64 regq con)
 184: regq -> (LSHS I64 regq con)
 185: regq -> (LSHS I64 regq regl)
 206: regl -> (RSHS I32 regl rcs)
 186: regq -> (RSHS I64 regq con)
 187: regq -> (RSHS I64 regq con)
 188: regq -> (RSHS I64 regq con)
 189: regq -> (RSHS I64 regq regl)
 207: regl -> (RSHU I32 regl rcs)
 190: regq -> (RSHU I64 regq con)
 191: regq -> (RSHU I64 regq con)
 192: regq -> (RSHU I64 regq con)
 193: regq -> (RSHU I64 regq regl)
 92: _13 -> (TSTEQ I32 regq regq)
 112: _23 -> (TSTEQ I32 regl rc)
 132: _33 -> (TSTEQ I32 rc regl)
 152: _43 -> (TSTEQ I32 regf regf)
 154: _44 -> (TSTEQ I32 regd regd)
 94: _14 -> (TSTNE I32 regq regq)
 114: _24 -> (TSTNE I32 regl rc)
 134: _34 -> (TSTNE I32 rc regl)
 156: _45 -> (TSTNE I32 regf regf)
 158: _46 -> (TSTNE I32 regd regd)
 96: _15 -> (TSTLTS I32 regq regq)
 116: _25 -> (TSTLTS I32 regl rc)
 136: _35 -> (TSTLTS I32 rc regl)
 160: _47 -> (TSTLTS I32 regf regf)
 162: _48 -> (TSTLTS I32 regd regd)
 98: _16 -> (TSTLES I32 regq regq)
 118: _26 -> (TSTLES I32 regl rc)
 138: _36 -> (TSTLES I32 rc regl)
 164: _49 -> (TSTLES I32 regf regf)
 166: _50 -> (TSTLES I32 regd regd)
 100: _17 -> (TSTGTS I32 regq regq)
 120: _27 -> (TSTGTS I32 regl rc)
 140: _37 -> (TSTGTS I32 rc regl)
 168: _51 -> (TSTGTS I32 regf regf)
 170: _52 -> (TSTGTS I32 regd regd)
 102: _18 -> (TSTGES I32 regq regq)
 122: _28 -> (TSTGES I32 regl rc)
 142: _38 -> (TSTGES I32 rc regl)
 172: _53 -> (TSTGES I32 regf regf)
 174: _54 -> (TSTGES I32 regd regd)
 104: _19 -> (TSTLTU I32 regq regq)
 124: _29 -> (TSTLTU I32 regl rc)
 144: _39 -> (TSTLTU I32 rc regl)
 106: _20 -> (TSTLEU I32 regq regq)
 126: _30 -> (TSTLEU I32 regl rc)
 146: _40 -> (TSTLEU I32 rc regl)
 108: _21 -> (TSTGTU I32 regq regq)
 128: _31 -> (TSTGTU I32 regl rc)
 148: _41 -> (TSTGTU I32 rc regl)
 110: _22 -> (TSTGEU I32 regq regq)
 130: _32 -> (TSTGEU I32 regl rc)
 150: _42 -> (TSTGEU I32 rc regl)
 62: regb -> (MEM I8 addr)
 70: _7 -> (MEM I8 addr)
 61: regh -> (MEM I16 addr)
 68: _6 -> (MEM I16 addr)
 60: regl -> (MEM I32 addr)
 79: _9 -> (MEM I32 addr)
 63: regf -> (MEM F32 addr)
 77: _8 -> (MEM F32 addr)
 64: regq -> (MEM I64 addr)
 65: _5 -> (MEM I64 addr)
 74: regd -> (MEM F64 addr)
 75: regd -> (MEM F64 addr)
 87: _12 -> (MEM F64 addr)
 48: void -> (SET I8 xregb regb)
 82: void -> (SET I8 _7 regb)
 86: void -> (SET I8 _7 _11)
 47: void -> (SET I16 xregh regh)
 81: void -> (SET I16 _6 regh)
 84: void -> (SET I16 _6 _10)
 46: void -> (SET I32 xregl regl)
 51: void -> (SET I32 _2 regl)
 54: void -> (SET I32 _4 regl)
 80: void -> (SET I32 _9 regl)
 58: void -> (SET F32 xregf regf)
 78: void -> (SET F32 _8 regf)
 57: void -> (SET I64 xregq regq)
 76: void -> (SET I64 _5 regq)
 59: void -> (SET F64 xregd regd)
 88: void -> (SET F64 _12 regd)
 89: void -> (SET F64 _12 regd)
 91: void -> (JUMP _ label)
 93: void -> (JUMPC _ _13 label label)
 95: void -> (JUMPC _ _14 label label)
 97: void -> (JUMPC _ _15 label label)
 99: void -> (JUMPC _ _16 label label)
 101: void -> (JUMPC _ _17 label label)
 103: void -> (JUMPC _ _18 label label)
 105: void -> (JUMPC _ _19 label label)
 107: void -> (JUMPC _ _20 label label)
 109: void -> (JUMPC _ _21 label label)
 111: void -> (JUMPC _ _22 label label)
 113: void -> (JUMPC _ _23 label label)
 115: void -> (JUMPC _ _24 label label)
 117: void -> (JUMPC _ _25 label label)
 119: void -> (JUMPC _ _26 label label)
 121: void -> (JUMPC _ _27 label label)
 123: void -> (JUMPC _ _28 label label)
 125: void -> (JUMPC _ _29 label label)
 127: void -> (JUMPC _ _30 label label)
 129: void -> (JUMPC _ _31 label label)
 131: void -> (JUMPC _ _32 label label)
 133: void -> (JUMPC _ _33 label label)
 135: void -> (JUMPC _ _34 label label)
 137: void -> (JUMPC _ _35 label label)
 139: void -> (JUMPC _ _36 label label)
 141: void -> (JUMPC _ _37 label label)
 143: void -> (JUMPC _ _38 label label)
 145: void -> (JUMPC _ _39 label label)
 147: void -> (JUMPC _ _40 label label)
 149: void -> (JUMPC _ _41 label label)
 151: void -> (JUMPC _ _42 label label)
 153: void -> (JUMPC _ _43 label label)
 155: void -> (JUMPC _ _44 label label)
 157: void -> (JUMPC _ _45 label label)
 159: void -> (JUMPC _ _46 label label)
 161: void -> (JUMPC _ _47 label label)
 163: void -> (JUMPC _ _48 label label)
 165: void -> (JUMPC _ _49 label label)
 167: void -> (JUMPC _ _50 label label)
 169: void -> (JUMPC _ _51 label label)
 171: void -> (JUMPC _ _52 label label)
 173: void -> (JUMPC _ _53 label label)
 175: void -> (JUMPC _ _54 label label)
 279: void -> (CALL _ fun)
 280: void -> (CALL _ fun)
 281: void -> (PARALLEL _ void)
*/
/*
Productions:
 1: _rewr -> (DIVS I32 _ _)
 2: _rewr -> (CONVUF _ _)
 3: _rewr -> (CONVFU _ _)
 4: _1 -> (STATIC I32 "__builtin_va_start")
 5: _2 -> (LIST _ _)
 6: _rewr -> (CALL _ _1 _2 _2)
 7: _3 -> (STATIC I32 "alloca")
 8: _rewr -> (CALL _ _3 _2 _2)
 9: _rewr -> (STATIC I32 ".stackRequired")
 10: _rewr -> (CONVFS I64 _)
 11: _rewr -> (CONVFS I64 _)
 12: _rewr -> (FLOATCONST F32)
 13: _rewr -> (FLOATCONST F64)
 14: _4 -> (CONVSX I64 _)
 15: _rewr -> (CONVIT I32 _4)
 16: _5 -> (CONVZX I64 _)
 17: _rewr -> (CONVIT I32 _5)
 18: _rewr -> (LSHS I64 _ _)
 19: _rewr -> (RSHS I64 _ _)
 20: _rewr -> (RSHU I64 _ _)
 21: _rewr -> (PROLOGUE _)
 22: _rewr -> (EPILOGUE _)
 23: _rewr -> (REG I32 ".strretp")
 24: _rewr -> (CALL _)
 25: _rewr -> (JUMPN _)
 26: _rewr -> (SET _)
*/
/*
Sorted Productions:
 12: _rewr -> (FLOATCONST F32)
 13: _rewr -> (FLOATCONST F64)
 4: _1 -> (STATIC I32 "__builtin_va_start")
 7: _3 -> (STATIC I32 "alloca")
 9: _rewr -> (STATIC I32 ".stackRequired")
 23: _rewr -> (REG I32 ".strretp")
 1: _rewr -> (DIVS I32 _ _)
 14: _4 -> (CONVSX I64 _)
 16: _5 -> (CONVZX I64 _)
 15: _rewr -> (CONVIT I32 _4)
 17: _rewr -> (CONVIT I32 _5)
 10: _rewr -> (CONVFS I64 _)
 11: _rewr -> (CONVFS I64 _)
 3: _rewr -> (CONVFU _ _)
 2: _rewr -> (CONVUF _ _)
 18: _rewr -> (LSHS I64 _ _)
 19: _rewr -> (RSHS I64 _ _)
 20: _rewr -> (RSHU I64 _ _)
 26: _rewr -> (SET _)
 25: _rewr -> (JUMPN _)
 6: _rewr -> (CALL _ _1 _2 _2)
 8: _rewr -> (CALL _ _3 _2 _2)
 24: _rewr -> (CALL _)
 21: _rewr -> (PROLOGUE _)
 22: _rewr -> (EPILOGUE _)
 5: _2 -> (LIST _ _)
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


import coins.backend.sym.Label;
import coins.backend.Storage;
import coins.backend.Data;
import coins.backend.LocalTransformer;
import coins.backend.Transformer;





public class CodeGenerator_sparc extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_sparc() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 7;
    static final int NRULES = 26 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;
    static final int NT__1 = 2;
    static final int NT__2 = 3;
    static final int NT__3 = 4;
    static final int NT__4 = 5;
    static final int NT__5 = 6;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT__4: return "_4";
      case NT__5: return "_5";
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
        if (t.type == 516) {
          if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 516, lir.node(Op.STATIC, 514, module.constToData(t)));
          }
        }
        if (t.type == 1028) {
          if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 1028, lir.node(Op.STATIC, 514, module.constToData(t)));
          }
        }
        break;
      case Op.STATIC:
        if (t.type == 514) {
          if (((LirSymRef)t).symbol.name == "__builtin_va_start") record(NT__1, 4);
          if (((LirSymRef)t).symbol.name == "alloca") record(NT__3, 7);
          if (((LirSymRef)t).symbol.name == ".stackRequired") if (phase == "const")  {
            rewritten = true;
            return lir.iconst(514, ((SparcAttr)getFunctionAttr(func)).stackRequired + 64);
          }
        }
        break;
      case Op.REG:
        if (t.type == 514) {
          if (((LirSymRef)t).symbol.name == ".strretp") if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 514, lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%fp")), lir.iconst(514, 64)));
          }
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (phase == "late") if (isPower2(t.kid(1)))  {
            rewritten = true;
            return rewriteDIVStoShift(t, pre);
          }
        }
        break;
      case Op.CONVSX:
        if (t.type == 1026) {
          record(NT__4, 14);
        }
        break;
      case Op.CONVZX:
        if (t.type == 1026) {
          record(NT__5, 16);
        }
        break;
      case Op.CONVIT:
        if (t.type == 514) {
          if (kids[0].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.CONVSX, 514, t.kid(0).kid(0));
          }
          if (kids[0].rule[NT__5] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.CONVZX, 514, t.kid(0).kid(0));
          }
        }
        break;
      case Op.CONVFS:
        if (t.type == 1026) {
          if (phase == "late") if (t.kid(0).type == F64)  {
            rewritten = true;
            LirNode temp = func.newTemp(I64);
            {
            pre.add(lir.node(Op.CALL, 0, lir.node(Op.STATIC, 514, func.getSymbol("__dtoll")), lir.node(Op.LIST, 0, t.kid(0)), lir.node(Op.LIST, 0, temp)));
            }
            return temp;
          }
          if (phase == "late") if (t.kid(0).type == F32)  {
            rewritten = true;
            LirNode temp = func.newTemp(I64);
            {
            pre.add(lir.node(Op.CALL, 0, lir.node(Op.STATIC, 514, func.getSymbol("__ftoll")), lir.node(Op.LIST, 0, t.kid(0)), lir.node(Op.LIST, 0, temp)));
            }
            return temp;
          }
        }
        break;
      case Op.CONVFU:
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
        break;
      case Op.LSHS:
        if (t.type == 1026) {
          if (phase == "late") if (t.kid(1).type == I64)  {
            rewritten = true;
            return lir.node(Op.LSHS, 1026, t.kid(0), lir.node(Op.CONVIT, 514, t.kid(1)));
          }
        }
        break;
      case Op.RSHS:
        if (t.type == 1026) {
          if (phase == "late") if (t.kid(1).type == I64)  {
            rewritten = true;
            return lir.node(Op.RSHS, 1026, t.kid(0), lir.node(Op.CONVIT, 514, t.kid(1)));
          }
        }
        break;
      case Op.RSHU:
        if (t.type == 1026) {
          if (phase == "late") if (t.kid(1).type == I64)  {
            rewritten = true;
            return lir.node(Op.RSHU, 1026, t.kid(0), lir.node(Op.CONVIT, 514, t.kid(1)));
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
        if (kids[0].rule[NT__1] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%fp")), lir.iconst(514, makeVaStart(t.kid(1).kid(0)))));
        }
        if (kids[0].rule[NT__3] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          {
          pre.add(lir.node(Op.SET, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.SUB, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.BAND, 514, lir.node(Op.ADD, 514, t.kid(1).kid(0), lir.iconst(514, 7)), lir.iconst(514, -8)))));
          }
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.STATIC, 514, func.getSymbol(".stackRequired"))));
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
        if (kids.length == 1) record(NT__2, 5);
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
    static final int NNONTERM = 79;
    static final int NRULES = 281 + 1;
    static final int START_NT = 7;

    static final int NT__ = 0;
    static final int NT_regq = 1;
    static final int NT_regl = 2;
    static final int NT_regh = 3;
    static final int NT_regb = 4;
    static final int NT_regf = 5;
    static final int NT_regd = 6;
    static final int NT_void = 7;
    static final int NT_xregb = 8;
    static final int NT_xregh = 9;
    static final int NT_xregl = 10;
    static final int NT_xregq = 11;
    static final int NT_xregf = 12;
    static final int NT_xregd = 13;
    static final int NT_addr = 14;
    static final int NT_con13 = 15;
    static final int NT_negcon13 = 16;
    static final int NT_fun = 17;
    static final int NT_asmcon = 18;
    static final int NT_con = 19;
    static final int NT_sta = 20;
    static final int NT_con5 = 21;
    static final int NT_rc = 22;
    static final int NT_rcs = 23;
    static final int NT__1 = 24;
    static final int NT__2 = 25;
    static final int NT__3 = 26;
    static final int NT__4 = 27;
    static final int NT__5 = 28;
    static final int NT__6 = 29;
    static final int NT__7 = 30;
    static final int NT__8 = 31;
    static final int NT__9 = 32;
    static final int NT__10 = 33;
    static final int NT__11 = 34;
    static final int NT__12 = 35;
    static final int NT_label = 36;
    static final int NT__13 = 37;
    static final int NT__14 = 38;
    static final int NT__15 = 39;
    static final int NT__16 = 40;
    static final int NT__17 = 41;
    static final int NT__18 = 42;
    static final int NT__19 = 43;
    static final int NT__20 = 44;
    static final int NT__21 = 45;
    static final int NT__22 = 46;
    static final int NT__23 = 47;
    static final int NT__24 = 48;
    static final int NT__25 = 49;
    static final int NT__26 = 50;
    static final int NT__27 = 51;
    static final int NT__28 = 52;
    static final int NT__29 = 53;
    static final int NT__30 = 54;
    static final int NT__31 = 55;
    static final int NT__32 = 56;
    static final int NT__33 = 57;
    static final int NT__34 = 58;
    static final int NT__35 = 59;
    static final int NT__36 = 60;
    static final int NT__37 = 61;
    static final int NT__38 = 62;
    static final int NT__39 = 63;
    static final int NT__40 = 64;
    static final int NT__41 = 65;
    static final int NT__42 = 66;
    static final int NT__43 = 67;
    static final int NT__44 = 68;
    static final int NT__45 = 69;
    static final int NT__46 = 70;
    static final int NT__47 = 71;
    static final int NT__48 = 72;
    static final int NT__49 = 73;
    static final int NT__50 = 74;
    static final int NT__51 = 75;
    static final int NT__52 = 76;
    static final int NT__53 = 77;
    static final int NT__54 = 78;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regq: return "regq";
      case NT_regl: return "regl";
      case NT_regh: return "regh";
      case NT_regb: return "regb";
      case NT_regf: return "regf";
      case NT_regd: return "regd";
      case NT_void: return "void";
      case NT_xregb: return "xregb";
      case NT_xregh: return "xregh";
      case NT_xregl: return "xregl";
      case NT_xregq: return "xregq";
      case NT_xregf: return "xregf";
      case NT_xregd: return "xregd";
      case NT_addr: return "addr";
      case NT_con13: return "con13";
      case NT_negcon13: return "negcon13";
      case NT_fun: return "fun";
      case NT_asmcon: return "asmcon";
      case NT_con: return "con";
      case NT_sta: return "sta";
      case NT_con5: return "con5";
      case NT_rc: return "rc";
      case NT_rcs: return "rcs";
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
      case NT_label: return "label";
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
      case NT__44: return "_44";
      case NT__45: return "_45";
      case NT__46: return "_46";
      case NT__47: return "_47";
      case NT__48: return "_48";
      case NT__49: return "_49";
      case NT__50: return "_50";
      case NT__51: return "_51";
      case NT__52: return "_52";
      case NT__53: return "_53";
      case NT__54: return "_54";
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
        case NT_regl:
          record(NT_addr, 0 + cost1, 0 + cost2, 19);
          record(NT_rc, 0 + cost1, 0 + cost2, 35);
          record(NT_rcs, 0 + cost1, 0 + cost2, 37);
          break;
        case NT_xregb:
          record(NT_regb, 0 + cost1, 0 + cost2, 13);
          break;
        case NT_xregh:
          record(NT_regh, 0 + cost1, 0 + cost2, 14);
          break;
        case NT_xregl:
          record(NT_regl, 0 + cost1, 0 + cost2, 15);
          break;
        case NT_xregq:
          record(NT_regq, 0 + cost1, 0 + cost2, 16);
          break;
        case NT_xregf:
          record(NT_regf, 0 + cost1, 0 + cost2, 17);
          break;
        case NT_xregd:
          record(NT_regd, 0 + cost1, 0 + cost2, 18);
          break;
        case NT_addr:
          record(NT_fun, 0 + cost1, 0 + cost2, 24);
          break;
        case NT_con13:
          record(NT_addr, 0 + cost1, 0 + cost2, 20);
          record(NT_rc, 0 + cost1, 0 + cost2, 36);
          record(NT_regl, 1 + cost1, 1 + cost2, 39);
          record(NT_regh, 1 + cost1, 1 + cost2, 40);
          record(NT_regb, 1 + cost1, 1 + cost2, 41);
          record(NT_regq, 2 + cost1, 2 + cost2, 42);
          break;
        case NT_asmcon:
          record(NT_fun, 0 + cost1, 0 + cost2, 25);
          record(NT_regl, 2 + cost1, 2 + cost2, 44);
          record(NT_regh, 2 + cost1, 2 + cost2, 45);
          break;
        case NT_con:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 28);
          record(NT_regq, 4 + cost1, 4 + cost2, 43);
          break;
        case NT_sta:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 29);
          break;
        case NT_con5:
          record(NT_rcs, 0 + cost1, 0 + cost2, 38);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        record(NT_con, 0, 0, 26);
        if (is13bitConst(((LirIconst)t).signedValue())) record(NT_con13, 0, 0, 32);
        if (is13bitConst(-((LirIconst)t).signedValue())) record(NT_negcon13, 0, 0, 33);
        if (0 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 31) record(NT_con5, 0, 0, 34);
        if (((LirIconst)t).value == 0) record(NT__1, 0, 0, 49);
        if (((LirIconst)t).value == 1) record(NT__3, 0, 0, 52);
        break;
      case Op.STATIC:
        if (t.type == 514) {
          record(NT_sta, 0, 0, 27);
        }
        break;
      case Op.REG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 1);
        }
        if (t.type == 258) {
          record(NT_xregh, 0, 0, 3);
        }
        if (t.type == 514) {
          record(NT_xregl, 0, 0, 5);
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 9);
        }
        if (t.type == 1026) {
          record(NT_xregq, 0, 0, 7);
        }
        if (t.type == 1028) {
          record(NT_xregd, 0, 0, 11);
        }
        break;
      case Op.SUBREG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 2);
        }
        if (t.type == 258) {
          record(NT_xregh, 0, 0, 4);
        }
        if (t.type == 514) {
          if (t.isPhysicalRegister()) record(NT_xregl, 0, 0, 6);
          if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT__1] != 0) record(NT__2, 0 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT__1], 50);
          if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT__3] != 0) record(NT__4, 0 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT__3], 53);
          if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT__1] != 0) record(NT_regl, 1 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT__1], 1 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT__1], 55);
          if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT__3] != 0) record(NT_regl, 1 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT__3], 1 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT__3], 56);
          if (kids[0].rule[NT__5] != 0) if (kids[1].rule[NT__3] != 0) record(NT_regl, 3 + kids[0].cost1[NT__5] + kids[1].cost1[NT__3], 1 + kids[0].cost2[NT__5] + kids[1].cost2[NT__3], 66);
          if (kids[0].rule[NT__5] != 0) if (kids[1].rule[NT__1] != 0) record(NT_regl, 3 + kids[0].cost1[NT__5] + kids[1].cost1[NT__1], 1 + kids[0].cost2[NT__5] + kids[1].cost2[NT__1], 67);
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 10);
        }
        if (t.type == 1026) {
          record(NT_xregq, 0, 0, 8);
        }
        if (t.type == 1028) {
          record(NT_xregd, 0, 0, 12);
        }
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 90);
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 204);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 245);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 181);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regd, 6 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 246);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 21);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con13] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con13], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con13], 22);
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con], 30);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 195);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 200);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 237);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 176);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 238);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_negcon13] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_negcon13], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_negcon13], 23);
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con], 31);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 196);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 239);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 177);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 240);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (!machineOptV8) record(NT_regl, 20 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 214);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (machineOptV8) record(NT_regl, 6 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 219);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (machineOptV8) record(NT_regl, 6 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 220);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (is3(t.kid(1))) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 221);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (is3(t.kid(0))) record(NT_regl, 2 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 222);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (is5(t.kid(1))) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 223);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (is5(t.kid(0))) record(NT_regl, 2 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 224);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (is6(t.kid(1))) record(NT_regl, 3 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 3 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 225);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (is6(t.kid(0))) record(NT_regl, 3 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 3 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 226);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (is7(t.kid(1))) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 227);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (is7(t.kid(0))) record(NT_regl, 2 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 228);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (is9(t.kid(1))) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 229);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (is9(t.kid(0))) record(NT_regl, 2 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 230);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (is10(t.kid(1))) record(NT_regl, 3 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 3 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 231);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) if (is10(t.kid(0))) record(NT_regl, 3 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 3 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 232);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 241);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 20 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 209);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 242);
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (!machineOptV8) record(NT_regl, 20 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 215);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (machineOptV8) record(NT_regl, 45 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 6 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 233);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 17 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 243);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 20 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 210);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 20 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 244);
        }
        break;
      case Op.DIVU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (!machineOptV8) record(NT_regl, 20 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 216);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (machineOptV8) record(NT_regl, 44 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 6 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 234);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 20 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 211);
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (!machineOptV8) record(NT_regl, 20 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 217);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (machineOptV8) record(NT_regl, 52 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 8 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 235);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 20 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 212);
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (!machineOptV8) record(NT_regl, 20 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 218);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (machineOptV8) record(NT_regl, 51 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 7 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 236);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 20 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 213);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 252);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT__6] != 0) record(NT_regl, 3 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 69);
          if (kids[0].rule[NT__7] != 0) record(NT_regl, 3 + kids[0].cost1[NT__7], 1 + kids[0].cost2[NT__7], 71);
          if (kids[0].rule[NT_regh] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 250);
          if (kids[0].rule[NT_regb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 251);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 247);
          if (kids[0].rule[NT_regh] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regh], 3 + kids[0].cost2[NT_regh], 248);
          if (kids[0].rule[NT_regb] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regb], 3 + kids[0].cost2[NT_regb], 249);
        }
        break;
      case Op.CONVZX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 258);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT__6] != 0) record(NT_regl, 3 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 72);
          if (kids[0].rule[NT__7] != 0) record(NT_regl, 3 + kids[0].cost1[NT__7], 1 + kids[0].cost2[NT__7], 73);
          if (kids[0].rule[NT_regh] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 256);
          if (kids[0].rule[NT_regb] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 257);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 253);
          if (kids[0].rule[NT_regh] != 0) record(NT_regq, 3 + kids[0].cost1[NT_regh], 3 + kids[0].cost2[NT_regh], 254);
          if (kids[0].rule[NT_regb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 255);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_regl] != 0) record(NT__11, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 85);
          if (kids[0].rule[NT_regq] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 261);
          if (kids[0].rule[NT_regl] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 263);
          if (kids[0].rule[NT_regh] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regh], 1 + kids[0].cost2[NT_regh], 264);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regl] != 0) record(NT__10, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 83);
          if (kids[0].rule[NT_regq] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 260);
          if (kids[0].rule[NT_regl] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 262);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 259);
        }
        break;
      case Op.CONVFX:
        if (t.type == 1028) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 265);
        }
        break;
      case Op.CONVFT:
        if (t.type == 516) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 266);
        }
        break;
      case Op.CONVFS:
        if (t.type == 130) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regb, 7 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 271);
          if (kids[0].rule[NT_regd] != 0) record(NT_regb, 7 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 274);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regh, 7 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 270);
          if (kids[0].rule[NT_regd] != 0) record(NT_regh, 7 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 273);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regl, 7 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 269);
          if (kids[0].rule[NT_regd] != 0) record(NT_regl, 7 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 272);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regq, 10 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 267);
          if (kids[0].rule[NT_regd] != 0) record(NT_regq, 10 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 268);
        }
        break;
      case Op.CONVSF:
        if (t.type == 516) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regf, 10 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 275);
          if (kids[0].rule[NT_regl] != 0) record(NT_regf, 7 + kids[0].cost1[NT_regl], 3 + kids[0].cost2[NT_regl], 277);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regd, 10 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 276);
          if (kids[0].rule[NT_regl] != 0) record(NT_regd, 7 + kids[0].cost1[NT_regl], 3 + kids[0].cost2[NT_regl], 278);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 197);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 201);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 178);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 198);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 202);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 179);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 199);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 203);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 180);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 205);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 194);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 208);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 4 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 4 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 182);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 183);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() >= 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 184);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regq, 10 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regl], 185);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 206);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 4 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 4 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 186);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 187);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() >= 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 188);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regq, 10 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regl], 189);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 207);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 4 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 4 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 190);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 191);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() >= 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 192);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regq, 10 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regl], 193);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__13, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 92);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__23, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 112);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__33, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 132);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__43, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 152);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__44, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 154);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__14, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 94);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__24, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 114);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__34, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 134);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__45, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 156);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__46, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 158);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__15, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 96);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__25, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 116);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__35, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 136);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__47, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 160);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__48, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 162);
        }
        break;
      case Op.TSTLES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__16, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 98);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__26, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 118);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__36, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 138);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__49, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 164);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__50, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 166);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__17, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 100);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__27, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 120);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__37, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 140);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__51, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 168);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__52, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 170);
        }
        break;
      case Op.TSTGES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__18, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 102);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__28, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 122);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__38, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 142);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__53, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 172);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__54, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 174);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__19, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 104);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__29, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 124);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__39, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 144);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__20, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 106);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__30, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 126);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__40, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 146);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__21, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 108);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__31, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 128);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__41, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 148);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__22, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 110);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 130);
          if (kids[0].rule[NT_rc] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__42, 0 + kids[0].cost1[NT_rc] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_rc] + kids[1].cost2[NT_regl], 150);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addr] != 0) record(NT_regb, 3 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 62);
          if (kids[0].rule[NT_addr] != 0) record(NT__7, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 70);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addr] != 0) record(NT_regh, 3 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 61);
          if (kids[0].rule[NT_addr] != 0) record(NT__6, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 68);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addr] != 0) record(NT_regl, 3 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 60);
          if (kids[0].rule[NT_addr] != 0) record(NT__9, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 79);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_addr] != 0) record(NT_regf, 3 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 63);
          if (kids[0].rule[NT_addr] != 0) record(NT__8, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 77);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_addr] != 0) record(NT_regq, 2 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 64);
          if (kids[0].rule[NT_addr] != 0) record(NT__5, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 65);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_addr] != 0) if (unalignedDouble(t)) record(NT_regd, 6 + kids[0].cost1[NT_addr], 2 + kids[0].cost2[NT_addr], 74);
          if (kids[0].rule[NT_addr] != 0) if (!unalignedDouble(t)) record(NT_regd, 2 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 75);
          if (kids[0].rule[NT_addr] != 0) record(NT__12, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 87);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 48);
          if (kids[0].rule[NT__7] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT__7] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT__7] + kids[1].cost2[NT_regb], 82);
          if (kids[0].rule[NT__7] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT__7] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT__7] + kids[1].cost2[NT__11], 86);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_regh], 47);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT__6] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT__6] + kids[1].cost2[NT_regh], 81);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__10] != 0) record(NT_void, 1 + kids[0].cost1[NT__6] + kids[1].cost1[NT__10], 1 + kids[0].cost2[NT__6] + kids[1].cost2[NT__10], 84);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regl], 46);
          if (kids[0].rule[NT__2] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT__2] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT__2] + kids[1].cost2[NT_regl], 51);
          if (kids[0].rule[NT__4] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT__4] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT__4] + kids[1].cost2[NT_regl], 54);
          if (kids[0].rule[NT__9] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT__9] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT__9] + kids[1].cost2[NT_regl], 80);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 3 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 2 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 58);
          if (kids[0].rule[NT__8] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT__8] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT__8] + kids[1].cost2[NT_regf], 78);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT_regq], 57);
          if (kids[0].rule[NT__5] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT__5] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT__5] + kids[1].cost2[NT_regq], 76);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 6 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 2 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 59);
          if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_regd] != 0) if (unalignedDouble(t.kid(0))) record(NT_void, 2 + kids[0].cost1[NT__12] + kids[1].cost1[NT_regd], 2 + kids[0].cost2[NT__12] + kids[1].cost2[NT_regd], 88);
          if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_regd] != 0) if (!unalignedDouble(t.kid(0))) record(NT_void, 1 + kids[0].cost1[NT__12] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT__12] + kids[1].cost2[NT_regd], 89);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 91);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 93);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 95);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 97);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 99);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 101);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 103);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 105);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 107);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 109);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 8 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 8 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 111);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 113);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 115);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 117);
        if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__26] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__26] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 119);
        if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__27] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__27] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 121);
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__28] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__28] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 123);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__29] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__29] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 125);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__30] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__30] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 127);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__31] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__31] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 129);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__32] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__32] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 131);
        if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__33] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__33] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 133);
        if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__34] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__34] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 135);
        if (kids[0].rule[NT__35] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__35] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__35] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 137);
        if (kids[0].rule[NT__36] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__36] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__36] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 139);
        if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__37] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__37] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 141);
        if (kids[0].rule[NT__38] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__38] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__38] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 143);
        if (kids[0].rule[NT__39] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__39] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__39] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 145);
        if (kids[0].rule[NT__40] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__40] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__40] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 147);
        if (kids[0].rule[NT__41] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__41] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__41] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 149);
        if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT__42] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__42] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 151);
        if (kids[0].rule[NT__43] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__43] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__43] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 153);
        if (kids[0].rule[NT__44] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__44] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__44] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 155);
        if (kids[0].rule[NT__45] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__45] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__45] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 157);
        if (kids[0].rule[NT__46] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__46] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__46] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 159);
        if (kids[0].rule[NT__47] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__47] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__47] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 161);
        if (kids[0].rule[NT__48] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__48] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__48] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 163);
        if (kids[0].rule[NT__49] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__49] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__49] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 165);
        if (kids[0].rule[NT__50] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__50] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__50] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 167);
        if (kids[0].rule[NT__51] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__51] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__51] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 169);
        if (kids[0].rule[NT__52] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__52] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__52] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 171);
        if (kids[0].rule[NT__53] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__53] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__53] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 173);
        if (kids[0].rule[NT__54] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__54] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__54] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 175);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_fun] != 0) if (t.opt.locate("&reta") == null) record(NT_void, 10 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 279);
        if (kids[0].rule[NT_fun] != 0) if (t.opt.locate("&reta") != null) record(NT_void, 10 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 280);
        break;
      case Op.PARALLEL:
        if (kids.length == 1) if (kids[0].rule[NT_void] != 0) record(NT_void, 0 + kids[0].cost1[NT_void], 0 + kids[0].cost2[NT_void], 281);
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
    
    private boolean is13bitConst(long value) {
      // keep extra 4byte space for accessing long long variable.
      return -4096L <= value && value < (4096L - 4);
    }
    
    private boolean is3(LirNode rc){
      if (rc instanceof LirIconst)
        return ((LirIconst)rc).signedValue() == 3;
      return false;
    }
    
    private boolean is5(LirNode rc){
      if (rc instanceof LirIconst)
        return ((LirIconst)rc).signedValue() == 5;
      return false;
    }
    
    private boolean is6(LirNode rc){
      if (rc instanceof LirIconst)
        return ((LirIconst)rc).signedValue() == 6;
      return false;
    }
    
    private boolean is7(LirNode rc){
      if (rc instanceof LirIconst)
        return ((LirIconst)rc).signedValue() == 7;
      return false;
    }
    
    private boolean is9(LirNode rc){
      if (rc instanceof LirIconst)
        return ((LirIconst)rc).signedValue() == 9;
      return false;
    }
    
    private boolean is10(LirNode rc){
      if (rc instanceof LirIconst)
        return ((LirIconst)rc).signedValue() == 10;
      return false;
    }
    
    private boolean unalignedDouble(LirNode p) {
      if (p.opCode != Op.MEM)
        throw new CantHappenException("not MEM");
      ImList align = p.opt.locate("&align");
      if (align != null) {
        align = align.next();
        if (!align.atEnd())
          return Integer.parseInt((String)align.elem()) != 8;
      }
      p = p.kid(0);
      if (p.opCode == Op.ADD
          && p.kid(0).opCode == Op.REG
          && p.kid(1).opCode == Op.INTCONST) {
        LirSymRef reg = (LirSymRef)p.kid(0);
        if (reg.symbol.name.equals("%fp") || reg.symbol.name.equals("%sp"))
          return (((LirIconst)p.kid(1)).value % 8 != 0);
      }
      return false;
    }
    
    
    
    
  }


  private static final Rule[] rulev = new Rule[State.NRULES];

  static {
    rrinit0();
    rrinit100();
    rrinit200();
  }
  static private void rrinit0() {
    rulev[19] = new Rule(19, true, false, 14, "19: addr -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[35] = new Rule(35, true, false, 22, "35: rc -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[37] = new Rule(37, true, false, 23, "37: rcs -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[13] = new Rule(13, true, false, 4, "13: regb -> xregb", null, null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I8*", null});
    rulev[14] = new Rule(14, true, false, 3, "14: regh -> xregh", null, null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I16*", null});
    rulev[15] = new Rule(15, true, false, 2, "15: regl -> xregl", null, null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I32*", null});
    rulev[16] = new Rule(16, true, false, 1, "16: regq -> xregq", null, null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I64*", null});
    rulev[17] = new Rule(17, true, false, 5, "17: regf -> xregf", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-F32*", null});
    rulev[18] = new Rule(18, true, false, 6, "18: regd -> xregd", null, null, null, 0, false, false, new int[]{13}, new String[]{"*reg-F64*", null});
    rulev[24] = new Rule(24, true, false, 17, "24: fun -> addr", null, null, null, 0, false, false, new int[]{14}, new String[]{null, null});
    rulev[20] = new Rule(20, true, false, 14, "20: addr -> con13", null, null, null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[36] = new Rule(36, true, false, 22, "36: rc -> con13", null, null, null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[39] = new Rule(39, true, false, 2, "39: regl -> con13", ImList.list(ImList.list("mov","$1","$0")), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I32*", null});
    rulev[40] = new Rule(40, true, false, 3, "40: regh -> con13", ImList.list(ImList.list("mov","$1","$0")), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I16*", null});
    rulev[41] = new Rule(41, true, false, 4, "41: regb -> con13", ImList.list(ImList.list("mov","$1","$0")), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I8*", null});
    rulev[42] = new Rule(42, true, false, 1, "42: regq -> con13", ImList.list(ImList.list("mov",ImList.list("qlow","$1"),ImList.list("qlow","$0")),ImList.list("mov",ImList.list("qhigh","$1"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I64*", null});
    rulev[25] = new Rule(25, true, false, 17, "25: fun -> asmcon", null, null, null, 0, false, false, new int[]{18}, new String[]{null, null});
    rulev[44] = new Rule(44, true, false, 2, "44: regl -> asmcon", ImList.list(ImList.list("_set","$1","$0")), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-I32*", null});
    rulev[45] = new Rule(45, true, false, 3, "45: regh -> asmcon", ImList.list(ImList.list("_set","$1","$0")), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-I16*", null});
    rulev[28] = new Rule(28, true, false, 18, "28: asmcon -> con", null, null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[43] = new Rule(43, true, false, 1, "43: regq -> con", ImList.list(ImList.list("_set",ImList.list("qlow","$1"),ImList.list("qlow","$0")),ImList.list("_set",ImList.list("qhigh","$1"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{19}, new String[]{"*reg-I64*", null});
    rulev[29] = new Rule(29, true, false, 18, "29: asmcon -> sta", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[38] = new Rule(38, true, false, 23, "38: rcs -> con5", null, null, null, 0, false, false, new int[]{21}, new String[]{null, null});
    rulev[26] = new Rule(26, false, false, 19, "26: con -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[32] = new Rule(32, false, false, 15, "32: con13 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[33] = new Rule(33, false, false, 16, "33: negcon13 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[34] = new Rule(34, false, false, 21, "34: con5 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[49] = new Rule(49, false, true, 24, "49: _1 -> (INTCONST _ 0)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[52] = new Rule(52, false, true, 26, "52: _3 -> (INTCONST _ 1)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[27] = new Rule(27, false, false, 20, "27: sta -> (STATIC I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 8, "1: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 9, "3: xregh -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 10, "5: xregl -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[9] = new Rule(9, false, false, 12, "9: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 11, "7: xregq -> (REG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[11] = new Rule(11, false, false, 13, "11: xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 8, "2: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[4] = new Rule(4, false, false, 9, "4: xregh -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 10, "6: xregl -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[50] = new Rule(50, false, true, 25, "50: _2 -> (SUBREG I32 xregq _1)", null, null, null, 0, false, false, new int[]{11,24}, null);
    rulev[53] = new Rule(53, false, true, 27, "53: _4 -> (SUBREG I32 xregq _3)", null, null, null, 0, false, false, new int[]{11,26}, null);
    rulev[55] = new Rule(55, false, false, 2, "55: regl -> (SUBREG I32 xregq _1)", ImList.list(ImList.list("mov",ImList.list("qlow","$1"),"$0")), null, null, 0, false, false, new int[]{11,24}, new String[]{"*reg-I32*", null});
    rulev[56] = new Rule(56, false, false, 2, "56: regl -> (SUBREG I32 xregq _3)", ImList.list(ImList.list("mov",ImList.list("qhigh","$1"),"$0")), null, null, 0, false, false, new int[]{11,26}, new String[]{"*reg-I32*", null});
    rulev[66] = new Rule(66, false, false, 2, "66: regl -> (SUBREG I32 _5 _3)", ImList.list(ImList.list("ld",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{28,26}, new String[]{"*reg-I32*", null});
    rulev[67] = new Rule(67, false, false, 2, "67: regl -> (SUBREG I32 _5 _1)", ImList.list(ImList.list("ld",ImList.list("mem",ImList.list("+","$1","4")),"$0")), null, null, 0, false, false, new int[]{28,24}, new String[]{"*reg-I32*", null});
    rulev[10] = new Rule(10, false, false, 12, "10: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 11, "8: xregq -> (SUBREG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 13, "12: xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[90] = new Rule(90, false, false, 36, "90: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[204] = new Rule(204, false, false, 2, "204: regl -> (NEG I32 regl)", ImList.list(ImList.list("neg","$1","$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[245] = new Rule(245, false, false, 5, "245: regf -> (NEG F32 regf)", ImList.list(ImList.list("fnegs","$1","$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F32*"});
    rulev[181] = new Rule(181, false, false, 1, "181: regq -> (NEG I64 regq)", ImList.list(ImList.list("sub","%g0",ImList.list("qlow","$1"),ImList.list("qlow","$0")),ImList.list("subx","%g0",ImList.list("qhigh","$1"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[246] = new Rule(246, false, false, 6, "246: regd -> (NEG F64 regd)", ImList.list(ImList.list("fnegs","$1","$0"),ImList.list("fmovs",ImList.list("dreg-low","$1"),ImList.list("dreg-low","$0"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F64*", "*reg-F64*"});
    rulev[21] = new Rule(21, false, false, 14, "21: addr -> (ADD I32 regl regl)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{2,2}, new String[]{null, "*reg-I32*", "*reg-I32*"});
    rulev[22] = new Rule(22, false, false, 14, "22: addr -> (ADD I32 regl con13)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{2,15}, new String[]{null, "*reg-I32*", null});
    rulev[30] = new Rule(30, false, false, 18, "30: asmcon -> (ADD I32 asmcon con)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{18,19}, new String[]{null, null, null});
    rulev[195] = new Rule(195, false, false, 2, "195: regl -> (ADD I32 regl rc)", ImList.list(ImList.list("add","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[200] = new Rule(200, false, false, 2, "200: regl -> (ADD I32 rc regl)", ImList.list(ImList.list("add","$2","$1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[237] = new Rule(237, false, false, 5, "237: regf -> (ADD F32 regf regf)", ImList.list(ImList.list("fadds","$1","$2","$0")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[176] = new Rule(176, false, false, 1, "176: regq -> (ADD I64 regq regq)", ImList.list(ImList.list("addcc",ImList.list("qlow","$1"),ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("addx",ImList.list("qhigh","$1"),ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I64*", "*reg-I64*", "*reg-I64*"});
    rulev[238] = new Rule(238, false, false, 6, "238: regd -> (ADD F64 regd regd)", ImList.list(ImList.list("faddd","$1","$2","$0")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[23] = new Rule(23, false, false, 14, "23: addr -> (SUB I32 regl negcon13)", null, ImList.list(ImList.list("+","$1",ImList.list("minus","$2"))), null, 0, false, false, new int[]{2,16}, new String[]{null, "*reg-I32*", null});
    rulev[31] = new Rule(31, false, false, 18, "31: asmcon -> (SUB I32 asmcon con)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{18,19}, new String[]{null, null, null});
    rulev[196] = new Rule(196, false, false, 2, "196: regl -> (SUB I32 regl rc)", ImList.list(ImList.list("sub","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[239] = new Rule(239, false, false, 5, "239: regf -> (SUB F32 regf regf)", ImList.list(ImList.list("fsubs","$1","$2","$0")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[177] = new Rule(177, false, false, 1, "177: regq -> (SUB I64 regq regq)", ImList.list(ImList.list("subcc",ImList.list("qlow","$1"),ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("subx",ImList.list("qhigh","$1"),ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I64*", "*reg-I64*", "*reg-I64*"});
    rulev[240] = new Rule(240, false, false, 6, "240: regd -> (SUB F64 regd regd)", ImList.list(ImList.list("fsubd","$1","$2","$0")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[214] = new Rule(214, false, false, 2, "214: regl -> (MUL I32 regl regl)", ImList.list(ImList.list("call",".mul"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{2,2}, new String[]{"*reg-o0-I32*", "*reg-o0-I32*", "*reg-o1-I32*"});
    rulev[219] = new Rule(219, false, false, 2, "219: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("smul","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[220] = new Rule(220, false, false, 2, "220: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("smul","$2","$1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[221] = new Rule(221, false, false, 2, "221: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("sll","$1","1","%g1"),ImList.list("add","$1","%g1","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[222] = new Rule(222, false, false, 2, "222: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("sll","$2","1","%g1"),ImList.list("add","$2","%g1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[223] = new Rule(223, false, false, 2, "223: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("sll","$1","2","%g1"),ImList.list("add","$1","%g1","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[224] = new Rule(224, false, false, 2, "224: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("sll","$2","2","%g1"),ImList.list("add","$2","%g1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[225] = new Rule(225, false, false, 2, "225: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("sll","$1","1","%g1"),ImList.list("add","$1","%g1","$0"),ImList.list("sll","$0","1","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[226] = new Rule(226, false, false, 2, "226: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("sll","$2","1","%g1"),ImList.list("add","$2","%g1","$0"),ImList.list("sll","$0","1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[227] = new Rule(227, false, false, 2, "227: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("sll","$1","3","%g1"),ImList.list("sub","%g1","$1","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[228] = new Rule(228, false, false, 2, "228: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("sll","$2","3","%g1"),ImList.list("sub","%g1","$2","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[229] = new Rule(229, false, false, 2, "229: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("sll","$1","3","%g1"),ImList.list("add","%g1","$1","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[230] = new Rule(230, false, false, 2, "230: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("sll","$2","3","%g1"),ImList.list("add","%g1","$2","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[231] = new Rule(231, false, false, 2, "231: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("sll","$1","2","%g1"),ImList.list("add","$1","%g1","$0"),ImList.list("sll","$0","1","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[232] = new Rule(232, false, false, 2, "232: regl -> (MUL I32 rc regl)", ImList.list(ImList.list("sll","$2","2","%g1"),ImList.list("add","$2","%g1","$0"),ImList.list("sll","$0","1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[241] = new Rule(241, false, false, 5, "241: regf -> (MUL F32 regf regf)", ImList.list(ImList.list("fmuls","$1","$2","$0")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[209] = new Rule(209, false, false, 1, "209: regq -> (MUL I64 regq regq)", ImList.list(ImList.list("call","__mul64"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,1}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o23-I64*"});
    rulev[242] = new Rule(242, false, false, 6, "242: regd -> (MUL F64 regd regd)", ImList.list(ImList.list("fmuld","$1","$2","$0")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[215] = new Rule(215, false, false, 2, "215: regl -> (DIVS I32 regl regl)", ImList.list(ImList.list("call",".div"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{2,2}, new String[]{"*reg-o0-I32*", "*reg-o0-I32*", "*reg-o1-I32*"});
    rulev[233] = new Rule(233, false, false, 2, "233: regl -> (DIVS I32 regl rc)", new ImList(ImList.list("sra","$1","31","%g1"), ImList.list(ImList.list("mov","%g1","%y"),ImList.list("nop"),ImList.list("nop"),ImList.list("nop"),ImList.list("sdiv","$1","$2","$0"))), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[243] = new Rule(243, false, false, 5, "243: regf -> (DIVS F32 regf regf)", ImList.list(ImList.list("fdivs","$1","$2","$0")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[210] = new Rule(210, false, false, 1, "210: regq -> (DIVS I64 regq regq)", ImList.list(ImList.list("call","__div64"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,1}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o23-I64*"});
    rulev[244] = new Rule(244, false, false, 6, "244: regd -> (DIVS F64 regd regd)", ImList.list(ImList.list("fdivd","$1","$2","$0")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[216] = new Rule(216, false, false, 2, "216: regl -> (DIVU I32 regl regl)", ImList.list(ImList.list("call",".udiv"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{2,2}, new String[]{"*reg-o0-I32*", "*reg-o0-I32*", "*reg-o1-I32*"});
    rulev[234] = new Rule(234, false, false, 2, "234: regl -> (DIVU I32 regl rc)", ImList.list(ImList.list("mov","0","%y"),ImList.list("nop"),ImList.list("nop"),ImList.list("nop"),ImList.list("udiv","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[211] = new Rule(211, false, false, 1, "211: regq -> (DIVU I64 regq regq)", ImList.list(ImList.list("call","__udiv64"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,1}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o23-I64*"});
    rulev[217] = new Rule(217, false, false, 2, "217: regl -> (MODS I32 regl regl)", ImList.list(ImList.list("call",".rem"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{2,2}, new String[]{"*reg-o0-I32*", "*reg-o0-I32*", "*reg-o1-I32*"});
    rulev[235] = new Rule(235, false, false, 2, "235: regl -> (MODS I32 regl rc)", new ImList(ImList.list("sra","$1","31","%g1"), new ImList(ImList.list("mov","%g1","%y"), new ImList(ImList.list("nop"), ImList.list(ImList.list("nop"),ImList.list("nop"),ImList.list("sdiv","$1","$2","%g1"),ImList.list("smul","%g1","$2","%g1"),ImList.list("sub","$1","%g1","$0"))))), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[212] = new Rule(212, false, false, 1, "212: regq -> (MODS I64 regq regq)", ImList.list(ImList.list("call","__rem64"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,1}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o23-I64*"});
    rulev[218] = new Rule(218, false, false, 2, "218: regl -> (MODU I32 regl regl)", ImList.list(ImList.list("call",".urem"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{2,2}, new String[]{"*reg-o0-I32*", "*reg-o0-I32*", "*reg-o1-I32*"});
    rulev[236] = new Rule(236, false, false, 2, "236: regl -> (MODU I32 regl rc)", new ImList(ImList.list("mov","0","%y"), new ImList(ImList.list("nop"), ImList.list(ImList.list("nop"),ImList.list("nop"),ImList.list("udiv","$1","$2","%g1"),ImList.list("umul","%g1","$2","%g1"),ImList.list("sub","$1","%g1","$0")))), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[213] = new Rule(213, false, false, 1, "213: regq -> (MODU I64 regq regq)", ImList.list(ImList.list("call","__urem64"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,1}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o23-I64*"});
    rulev[252] = new Rule(252, false, false, 3, "252: regh -> (CONVSX I16 regb)", ImList.list(ImList.list("sll","$1","8","$0"),ImList.list("sra","$0","8","$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I16*", "*reg-I8*"});
  }
  static private void rrinit100() {
    rulev[69] = new Rule(69, false, false, 2, "69: regl -> (CONVSX I32 _6)", ImList.list(ImList.list("ldsh",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-I32*", null});
    rulev[71] = new Rule(71, false, false, 2, "71: regl -> (CONVSX I32 _7)", ImList.list(ImList.list("ldsb",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{30}, new String[]{"*reg-I32*", null});
    rulev[250] = new Rule(250, false, false, 2, "250: regl -> (CONVSX I32 regh)", ImList.list(ImList.list("sll","$1","16","$0"),ImList.list("sra","$0","16","$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[251] = new Rule(251, false, false, 2, "251: regl -> (CONVSX I32 regb)", ImList.list(ImList.list("sll","$1","24","$0"),ImList.list("sra","$0","24","$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[247] = new Rule(247, false, false, 1, "247: regq -> (CONVSX I64 regl)", ImList.list(ImList.list("mov","$1",ImList.list("qlow","$0")),ImList.list("sra","$1","31",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I64*", "*reg-I32*"});
    rulev[248] = new Rule(248, false, false, 1, "248: regq -> (CONVSX I64 regh)", ImList.list(ImList.list("sll","$1","16",ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qlow","$0"),"16",ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qlow","$0"),"31",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I64*", "*reg-I16*"});
    rulev[249] = new Rule(249, false, false, 1, "249: regq -> (CONVSX I64 regb)", ImList.list(ImList.list("sll","$1","24",ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qlow","$0"),"24",ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qlow","$0"),"31",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I64*", "*reg-I8*"});
    rulev[258] = new Rule(258, false, false, 3, "258: regh -> (CONVZX I16 regb)", ImList.list(ImList.list("and","$1","255","$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[72] = new Rule(72, false, false, 2, "72: regl -> (CONVZX I32 _6)", ImList.list(ImList.list("lduh",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-I32*", null});
    rulev[73] = new Rule(73, false, false, 2, "73: regl -> (CONVZX I32 _7)", ImList.list(ImList.list("ldub",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{30}, new String[]{"*reg-I32*", null});
    rulev[256] = new Rule(256, false, false, 2, "256: regl -> (CONVZX I32 regh)", ImList.list(ImList.list("sll","$1","16","$0"),ImList.list("srl","$0","16","$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[257] = new Rule(257, false, false, 2, "257: regl -> (CONVZX I32 regb)", ImList.list(ImList.list("and","$1","255","$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[253] = new Rule(253, false, false, 1, "253: regq -> (CONVZX I64 regl)", ImList.list(ImList.list("mov","$1",ImList.list("qlow","$0")),ImList.list("mov","0",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I64*", "*reg-I32*"});
    rulev[254] = new Rule(254, false, false, 1, "254: regq -> (CONVZX I64 regh)", ImList.list(ImList.list("sll","$1","16",ImList.list("qlow","$0")),ImList.list("srl",ImList.list("qlow","$0"),"16",ImList.list("qlow","$0")),ImList.list("mov","0",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I64*", "*reg-I16*"});
    rulev[255] = new Rule(255, false, false, 1, "255: regq -> (CONVZX I64 regb)", ImList.list(ImList.list("and","$1","255",ImList.list("qlow","$0")),ImList.list("mov","0",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I64*", "*reg-I8*"});
    rulev[85] = new Rule(85, false, true, 34, "85: _11 -> (CONVIT I8 regl)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[261] = new Rule(261, false, false, 4, "261: regb -> (CONVIT I8 regq)", ImList.list(ImList.list("and",ImList.list("qlow","$1"),"255","$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-I64*"});
    rulev[263] = new Rule(263, false, false, 4, "263: regb -> (CONVIT I8 regl)", ImList.list(ImList.list("and","$1","255","$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-I32*"});
    rulev[264] = new Rule(264, false, false, 4, "264: regb -> (CONVIT I8 regh)", ImList.list(ImList.list("and","$1","255","$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I8*", "*reg-I16*"});
    rulev[83] = new Rule(83, false, true, 33, "83: _10 -> (CONVIT I16 regl)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[260] = new Rule(260, false, false, 3, "260: regh -> (CONVIT I16 regq)", ImList.list(ImList.list("sll",ImList.list("qlow","$1"),"16","$0"),ImList.list("srl","$0","16","$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I64*"});
    rulev[262] = new Rule(262, false, false, 3, "262: regh -> (CONVIT I16 regl)", ImList.list(ImList.list("sll","$1","16","$0"),ImList.list("srl","$0","16","$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[259] = new Rule(259, false, false, 2, "259: regl -> (CONVIT I32 regq)", ImList.list(ImList.list("mov",ImList.list("qlow","$1"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I64*"});
    rulev[265] = new Rule(265, false, false, 6, "265: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("fstod","$1","$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F64*", "*reg-F32*"});
    rulev[266] = new Rule(266, false, false, 5, "266: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("fdtos","$1","$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F32*", "*reg-F64*"});
    rulev[271] = new Rule(271, false, false, 4, "271: regb -> (CONVFS I8 regf)", ImList.list(ImList.list("fstoi","$1","%f0"),ImList.list("st","%f0",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0")), null, ImList.list(ImList.list("REG","F64","%f0")), 0, false, false, new int[]{5}, new String[]{"*reg-I8*", "*reg-F32*"});
    rulev[274] = new Rule(274, false, false, 4, "274: regb -> (CONVFS I8 regd)", ImList.list(ImList.list("fdtoi","$1","%f0"),ImList.list("st","%f0",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0")), null, ImList.list(ImList.list("REG","F64","%f0")), 0, false, false, new int[]{6}, new String[]{"*reg-I8*", "*reg-F64*"});
    rulev[270] = new Rule(270, false, false, 3, "270: regh -> (CONVFS I16 regf)", ImList.list(ImList.list("fstoi","$1","%f0"),ImList.list("st","%f0",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0")), null, ImList.list(ImList.list("REG","F64","%f0")), 0, false, false, new int[]{5}, new String[]{"*reg-I16*", "*reg-F32*"});
    rulev[273] = new Rule(273, false, false, 3, "273: regh -> (CONVFS I16 regd)", ImList.list(ImList.list("fdtoi","$1","%f0"),ImList.list("st","%f0",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0")), null, ImList.list(ImList.list("REG","F64","%f0")), 0, false, false, new int[]{6}, new String[]{"*reg-I16*", "*reg-F64*"});
    rulev[269] = new Rule(269, false, false, 2, "269: regl -> (CONVFS I32 regf)", ImList.list(ImList.list("fstoi","$1","%f0"),ImList.list("st","%f0",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0")), null, ImList.list(ImList.list("REG","F64","%f0")), 0, false, false, new int[]{5}, new String[]{"*reg-I32*", "*reg-F32*"});
    rulev[272] = new Rule(272, false, false, 2, "272: regl -> (CONVFS I32 regd)", ImList.list(ImList.list("fdtoi","$1","%f0"),ImList.list("st","%f0",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0")), null, ImList.list(ImList.list("REG","F64","%f0")), 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-F64*"});
    rulev[267] = new Rule(267, false, false, 1, "267: regq -> (CONVFS I64 regf)", ImList.list(ImList.list("call","__ftoll"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), new ImList(ImList.list("REG","I32","%o4"), new ImList(ImList.list("REG","I32","%o5"), new ImList(ImList.list("REG","I32","%g2"), new ImList(ImList.list("REG","I32","%g3"), new ImList(ImList.list("REG","I32","%g4"), new ImList(ImList.list("REG","F64","%f2"), new ImList(ImList.list("REG","F64","%f4"), ImList.list(ImList.list("REG","F64","%f6"),ImList.list("REG","F64","%f8"),ImList.list("REG","F64","%f10"),ImList.list("REG","F64","%f12"),ImList.list("REG","F64","%f14")))))))))))), 0, false, true, new int[]{5}, new String[]{"*reg-o01-I64*", "*reg-f0-F32*"});
    rulev[268] = new Rule(268, false, false, 1, "268: regq -> (CONVFS I64 regd)", ImList.list(ImList.list("call","__dtoll"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), new ImList(ImList.list("REG","I32","%o4"), new ImList(ImList.list("REG","I32","%o5"), new ImList(ImList.list("REG","I32","%g2"), new ImList(ImList.list("REG","I32","%g3"), new ImList(ImList.list("REG","I32","%g4"), new ImList(ImList.list("REG","F64","%f2"), new ImList(ImList.list("REG","F64","%f4"), ImList.list(ImList.list("REG","F64","%f6"),ImList.list("REG","F64","%f8"),ImList.list("REG","F64","%f10"),ImList.list("REG","F64","%f12"),ImList.list("REG","F64","%f14")))))))))))), 0, false, true, new int[]{6}, new String[]{"*reg-o01-I64*", "*reg-f01-F64*"});
    rulev[275] = new Rule(275, false, false, 5, "275: regf -> (CONVSF F32 regq)", ImList.list(ImList.list("call","__floatdisf"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), new ImList(ImList.list("REG","I32","%o4"), new ImList(ImList.list("REG","I32","%o5"), new ImList(ImList.list("REG","I32","%g2"), new ImList(ImList.list("REG","I32","%g3"), new ImList(ImList.list("REG","I32","%g4"), new ImList(ImList.list("REG","F64","%f2"), new ImList(ImList.list("REG","F64","%f4"), ImList.list(ImList.list("REG","F64","%f6"),ImList.list("REG","F64","%f8"),ImList.list("REG","F64","%f10"),ImList.list("REG","F64","%f12"),ImList.list("REG","F64","%f14")))))))))))), 0, false, true, new int[]{1}, new String[]{"*reg-f0-F32*", "*reg-o01-I64*"});
    rulev[277] = new Rule(277, false, false, 5, "277: regf -> (CONVSF F32 regl)", ImList.list(ImList.list("st","$1",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0"),ImList.list("fitos","$0","$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-F32*", "*reg-I32*"});
    rulev[276] = new Rule(276, false, false, 6, "276: regd -> (CONVSF F64 regq)", ImList.list(ImList.list("call","__floatdidf"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), new ImList(ImList.list("REG","I32","%o4"), new ImList(ImList.list("REG","I32","%o5"), new ImList(ImList.list("REG","I32","%g2"), new ImList(ImList.list("REG","I32","%g3"), new ImList(ImList.list("REG","I32","%g4"), new ImList(ImList.list("REG","F64","%f2"), new ImList(ImList.list("REG","F64","%f4"), ImList.list(ImList.list("REG","F64","%f6"),ImList.list("REG","F64","%f8"),ImList.list("REG","F64","%f10"),ImList.list("REG","F64","%f12"),ImList.list("REG","F64","%f14")))))))))))), 0, false, true, new int[]{1}, new String[]{"*reg-f01-F64*", "*reg-o01-I64*"});
    rulev[278] = new Rule(278, false, false, 6, "278: regd -> (CONVSF F64 regl)", ImList.list(ImList.list("st","$1",ImList.list("mem",ImList.list("+","%sp","64"))),ImList.list("ld",ImList.list("mem",ImList.list("+","%sp","64")),"$0"),ImList.list("fitod","$0","$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-F64*", "*reg-I32*"});
    rulev[197] = new Rule(197, false, false, 2, "197: regl -> (BAND I32 regl rc)", ImList.list(ImList.list("and","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[201] = new Rule(201, false, false, 2, "201: regl -> (BAND I32 rc regl)", ImList.list(ImList.list("and","$2","$1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[178] = new Rule(178, false, false, 1, "178: regq -> (BAND I64 regq regq)", ImList.list(ImList.list("and",ImList.list("qlow","$1"),ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("and",ImList.list("qhigh","$1"),ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I64*", "*reg-I64*", "*reg-I64*"});
    rulev[198] = new Rule(198, false, false, 2, "198: regl -> (BOR I32 regl rc)", ImList.list(ImList.list("or","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[202] = new Rule(202, false, false, 2, "202: regl -> (BOR I32 rc regl)", ImList.list(ImList.list("or","$2","$1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[179] = new Rule(179, false, false, 1, "179: regq -> (BOR I64 regq regq)", ImList.list(ImList.list("or",ImList.list("qlow","$1"),ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("or",ImList.list("qhigh","$1"),ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I64*", "*reg-I64*", "*reg-I64*"});
    rulev[199] = new Rule(199, false, false, 2, "199: regl -> (BXOR I32 regl rc)", ImList.list(ImList.list("xor","$1","$2","$0")), null, null, 0, false, false, new int[]{2,22}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[203] = new Rule(203, false, false, 2, "203: regl -> (BXOR I32 rc regl)", ImList.list(ImList.list("xor","$2","$1","$0")), null, null, 0, false, false, new int[]{22,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[180] = new Rule(180, false, false, 1, "180: regq -> (BXOR I64 regq regq)", ImList.list(ImList.list("xor",ImList.list("qlow","$1"),ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("xor",ImList.list("qhigh","$1"),ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I64*", "*reg-I64*", "*reg-I64*"});
    rulev[205] = new Rule(205, false, false, 2, "205: regl -> (BNOT I32 regl)", ImList.list(ImList.list("not","$1","$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[194] = new Rule(194, false, false, 1, "194: regq -> (BNOT I64 regq)", ImList.list(ImList.list("not",ImList.list("qlow","$1"),ImList.list("qlow","$0")),ImList.list("not",ImList.list("qhigh","$1"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[208] = new Rule(208, false, false, 2, "208: regl -> (LSHS I32 regl rcs)", ImList.list(ImList.list("sll","$1","$2","$0")), null, null, 0, false, false, new int[]{2,23}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[182] = new Rule(182, false, false, 1, "182: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("srl",ImList.list("qlow","$1"),ImList.list("-","32","$2"),"%g1"),ImList.list("sll",ImList.list("qhigh","$1"),"$2",ImList.list("qhigh","$0")),ImList.list("or","%g1",ImList.list("qhigh","$0"),ImList.list("qhigh","$0")),ImList.list("sll",ImList.list("qlow","$1"),"$2",ImList.list("qlow","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[183] = new Rule(183, false, false, 1, "183: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("mov",ImList.list("qlow","$1"),ImList.list("qhigh","$0")),ImList.list("mov","0",ImList.list("qlow","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[184] = new Rule(184, false, false, 1, "184: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("sll",ImList.list("qlow","$1"),ImList.list("-","$2","32"),ImList.list("qhigh","$0")),ImList.list("mov","0",ImList.list("qlow","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[185] = new Rule(185, false, false, 1, "185: regq -> (LSHS I64 regq regl)", ImList.list(ImList.list("call","__ashldi3"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,2}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o2-I32*"});
    rulev[206] = new Rule(206, false, false, 2, "206: regl -> (RSHS I32 regl rcs)", ImList.list(ImList.list("sra","$1","$2","$0")), null, null, 0, false, false, new int[]{2,23}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[186] = new Rule(186, false, false, 1, "186: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("sll",ImList.list("qhigh","$1"),ImList.list("-","32","$2"),"%g1"),ImList.list("srl",ImList.list("qlow","$1"),"$2",ImList.list("qlow","$0")),ImList.list("or","%g1",ImList.list("qlow","$0"),ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qhigh","$1"),"$2",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[187] = new Rule(187, false, false, 1, "187: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("mov",ImList.list("qhigh","$1"),ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qhigh","$1"),"31",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[188] = new Rule(188, false, false, 1, "188: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("sra",ImList.list("qhigh","$1"),ImList.list("-","$2","32"),ImList.list("qlow","$0")),ImList.list("sra",ImList.list("qhigh","$1"),"31",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[189] = new Rule(189, false, false, 1, "189: regq -> (RSHS I64 regq regl)", ImList.list(ImList.list("call","__ashrdi3"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,2}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o2-I32*"});
    rulev[207] = new Rule(207, false, false, 2, "207: regl -> (RSHU I32 regl rcs)", ImList.list(ImList.list("srl","$1","$2","$0")), null, null, 0, false, false, new int[]{2,23}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[190] = new Rule(190, false, false, 1, "190: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("sll",ImList.list("qhigh","$1"),ImList.list("-","32","$2"),"%g1"),ImList.list("srl",ImList.list("qlow","$1"),"$2",ImList.list("qlow","$0")),ImList.list("or","%g1",ImList.list("qlow","$0"),ImList.list("qlow","$0")),ImList.list("srl",ImList.list("qhigh","$1"),"$2",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[191] = new Rule(191, false, false, 1, "191: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("mov",ImList.list("qhigh","$1"),ImList.list("qlow","$0")),ImList.list("mov","0",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[192] = new Rule(192, false, false, 1, "192: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("srl",ImList.list("qhigh","$1"),ImList.list("-","$2","32"),ImList.list("qlow","$0")),ImList.list("mov","0",ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[193] = new Rule(193, false, false, 1, "193: regq -> (RSHU I64 regq regl)", ImList.list(ImList.list("call","__lshrdi3"),ImList.list("delayslot")), null, new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), ImList.list(ImList.list("REG","I32","%o4"),ImList.list("REG","I32","%o5"),ImList.list("REG","I32","%g2"),ImList.list("REG","I32","%g3"),ImList.list("REG","I32","%g4"))))), 0, false, true, new int[]{1,2}, new String[]{"*reg-o01-I64*", "*reg-o01-I64*", "*reg-o2-I32*"});
    rulev[92] = new Rule(92, false, true, 37, "92: _13 -> (TSTEQ I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[112] = new Rule(112, false, true, 47, "112: _23 -> (TSTEQ I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[132] = new Rule(132, false, true, 57, "132: _33 -> (TSTEQ I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[152] = new Rule(152, false, true, 67, "152: _43 -> (TSTEQ I32 regf regf)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[154] = new Rule(154, false, true, 68, "154: _44 -> (TSTEQ I32 regd regd)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[94] = new Rule(94, false, true, 38, "94: _14 -> (TSTNE I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[114] = new Rule(114, false, true, 48, "114: _24 -> (TSTNE I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[134] = new Rule(134, false, true, 58, "134: _34 -> (TSTNE I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[156] = new Rule(156, false, true, 69, "156: _45 -> (TSTNE I32 regf regf)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[158] = new Rule(158, false, true, 70, "158: _46 -> (TSTNE I32 regd regd)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[96] = new Rule(96, false, true, 39, "96: _15 -> (TSTLTS I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[116] = new Rule(116, false, true, 49, "116: _25 -> (TSTLTS I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[136] = new Rule(136, false, true, 59, "136: _35 -> (TSTLTS I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[160] = new Rule(160, false, true, 71, "160: _47 -> (TSTLTS I32 regf regf)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[162] = new Rule(162, false, true, 72, "162: _48 -> (TSTLTS I32 regd regd)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[98] = new Rule(98, false, true, 40, "98: _16 -> (TSTLES I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[118] = new Rule(118, false, true, 50, "118: _26 -> (TSTLES I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[138] = new Rule(138, false, true, 60, "138: _36 -> (TSTLES I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[164] = new Rule(164, false, true, 73, "164: _49 -> (TSTLES I32 regf regf)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[166] = new Rule(166, false, true, 74, "166: _50 -> (TSTLES I32 regd regd)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[100] = new Rule(100, false, true, 41, "100: _17 -> (TSTGTS I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[120] = new Rule(120, false, true, 51, "120: _27 -> (TSTGTS I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[140] = new Rule(140, false, true, 61, "140: _37 -> (TSTGTS I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[168] = new Rule(168, false, true, 75, "168: _51 -> (TSTGTS I32 regf regf)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[170] = new Rule(170, false, true, 76, "170: _52 -> (TSTGTS I32 regd regd)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[102] = new Rule(102, false, true, 42, "102: _18 -> (TSTGES I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[122] = new Rule(122, false, true, 52, "122: _28 -> (TSTGES I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[142] = new Rule(142, false, true, 62, "142: _38 -> (TSTGES I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[172] = new Rule(172, false, true, 77, "172: _53 -> (TSTGES I32 regf regf)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[174] = new Rule(174, false, true, 78, "174: _54 -> (TSTGES I32 regd regd)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[104] = new Rule(104, false, true, 43, "104: _19 -> (TSTLTU I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[124] = new Rule(124, false, true, 53, "124: _29 -> (TSTLTU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[144] = new Rule(144, false, true, 63, "144: _39 -> (TSTLTU I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[106] = new Rule(106, false, true, 44, "106: _20 -> (TSTLEU I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[126] = new Rule(126, false, true, 54, "126: _30 -> (TSTLEU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[146] = new Rule(146, false, true, 64, "146: _40 -> (TSTLEU I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[108] = new Rule(108, false, true, 45, "108: _21 -> (TSTGTU I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
  }
  static private void rrinit200() {
    rulev[128] = new Rule(128, false, true, 55, "128: _31 -> (TSTGTU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[148] = new Rule(148, false, true, 65, "148: _41 -> (TSTGTU I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[110] = new Rule(110, false, true, 46, "110: _22 -> (TSTGEU I32 regq regq)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[130] = new Rule(130, false, true, 56, "130: _32 -> (TSTGEU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,22}, null);
    rulev[150] = new Rule(150, false, true, 66, "150: _42 -> (TSTGEU I32 rc regl)", null, null, null, 0, false, false, new int[]{22,2}, null);
    rulev[62] = new Rule(62, false, false, 4, "62: regb -> (MEM I8 addr)", ImList.list(ImList.list("ldsb",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I8*", null});
    rulev[70] = new Rule(70, false, true, 30, "70: _7 -> (MEM I8 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[61] = new Rule(61, false, false, 3, "61: regh -> (MEM I16 addr)", ImList.list(ImList.list("ldsh",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I16*", null});
    rulev[68] = new Rule(68, false, true, 29, "68: _6 -> (MEM I16 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[60] = new Rule(60, false, false, 2, "60: regl -> (MEM I32 addr)", ImList.list(ImList.list("ld",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I32*", null});
    rulev[79] = new Rule(79, false, true, 32, "79: _9 -> (MEM I32 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[63] = new Rule(63, false, false, 5, "63: regf -> (MEM F32 addr)", ImList.list(ImList.list("ld",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-F32*", null});
    rulev[77] = new Rule(77, false, true, 31, "77: _8 -> (MEM F32 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[64] = new Rule(64, false, false, 1, "64: regq -> (MEM I64 addr)", ImList.list(ImList.list("ldd",ImList.list("mem","$1"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I64*", null});
    rulev[65] = new Rule(65, false, true, 28, "65: _5 -> (MEM I64 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[74] = new Rule(74, false, false, 6, "74: regd -> (MEM F64 addr)", ImList.list(ImList.list("ld",ImList.list("mem","$1"),"$0"),ImList.list("ld",ImList.list("mem",ImList.list("dframe-low","$1")),ImList.list("dreg-low","$0"))), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-F64*", null});
    rulev[75] = new Rule(75, false, false, 6, "75: regd -> (MEM F64 addr)", ImList.list(ImList.list("ldd",ImList.list("mem","$1"),"$0")), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-F64*", null});
    rulev[87] = new Rule(87, false, true, 35, "87: _12 -> (MEM F64 addr)", null, null, null, 0, false, false, new int[]{14}, null);
    rulev[48] = new Rule(48, false, false, 7, "48: void -> (SET I8 xregb regb)", ImList.list(ImList.list("mov","$2","$1")), null, null, 0, false, false, new int[]{8,4}, new String[]{null, null, "*reg-I8*"});
    rulev[82] = new Rule(82, false, false, 7, "82: void -> (SET I8 _7 regb)", ImList.list(ImList.list("stb","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{30,4}, new String[]{null, null, "*reg-I8*"});
    rulev[86] = new Rule(86, false, false, 7, "86: void -> (SET I8 _7 _11)", ImList.list(ImList.list("stb","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{30,34}, new String[]{null, null, "*reg-I32*"});
    rulev[47] = new Rule(47, false, false, 7, "47: void -> (SET I16 xregh regh)", ImList.list(ImList.list("mov","$2","$1")), null, null, 0, false, false, new int[]{9,3}, new String[]{null, null, "*reg-I16*"});
    rulev[81] = new Rule(81, false, false, 7, "81: void -> (SET I16 _6 regh)", ImList.list(ImList.list("sth","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{29,3}, new String[]{null, null, "*reg-I16*"});
    rulev[84] = new Rule(84, false, false, 7, "84: void -> (SET I16 _6 _10)", ImList.list(ImList.list("sth","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{29,33}, new String[]{null, null, "*reg-I32*"});
    rulev[46] = new Rule(46, false, false, 7, "46: void -> (SET I32 xregl regl)", ImList.list(ImList.list("mov","$2","$1")), null, null, 0, false, false, new int[]{10,2}, new String[]{null, null, "*reg-I32*"});
    rulev[51] = new Rule(51, false, false, 7, "51: void -> (SET I32 _2 regl)", ImList.list(ImList.list("mov","$2",ImList.list("qlow","$1"))), null, null, 0, false, false, new int[]{25,2}, new String[]{null, null, "*reg-I32*"});
    rulev[54] = new Rule(54, false, false, 7, "54: void -> (SET I32 _4 regl)", ImList.list(ImList.list("mov","$2",ImList.list("qhigh","$1"))), null, null, 0, false, false, new int[]{27,2}, new String[]{null, null, "*reg-I32*"});
    rulev[80] = new Rule(80, false, false, 7, "80: void -> (SET I32 _9 regl)", ImList.list(ImList.list("st","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{32,2}, new String[]{null, null, "*reg-I32*"});
    rulev[58] = new Rule(58, false, false, 7, "58: void -> (SET F32 xregf regf)", ImList.list(ImList.list("fmovs","$2","$1")), null, null, 0, false, false, new int[]{12,5}, new String[]{null, null, "*reg-F32*"});
    rulev[78] = new Rule(78, false, false, 7, "78: void -> (SET F32 _8 regf)", ImList.list(ImList.list("st","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{31,5}, new String[]{null, null, "*reg-F32*"});
    rulev[57] = new Rule(57, false, false, 7, "57: void -> (SET I64 xregq regq)", ImList.list(ImList.list("mov",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("mov",ImList.list("qhigh","$2"),ImList.list("qhigh","$1"))), null, null, 0, false, false, new int[]{11,1}, new String[]{null, null, "*reg-I64*"});
    rulev[76] = new Rule(76, false, false, 7, "76: void -> (SET I64 _5 regq)", ImList.list(ImList.list("std",ImList.list("qhigh","$2"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{28,1}, new String[]{null, null, "*reg-I64*"});
    rulev[59] = new Rule(59, false, false, 7, "59: void -> (SET F64 xregd regd)", ImList.list(ImList.list("fmovs","$2","$1"),ImList.list("fmovs",ImList.list("dreg-low","$2"),ImList.list("dreg-low","$1"))), null, null, 0, false, false, new int[]{13,6}, new String[]{null, null, "*reg-F64*"});
    rulev[88] = new Rule(88, false, false, 7, "88: void -> (SET F64 _12 regd)", ImList.list(ImList.list("st","$2",ImList.list("mem","$1")),ImList.list("st",ImList.list("dreg-low","$2"),ImList.list("mem",ImList.list("dframe-low","$1")))), null, null, 0, false, false, new int[]{35,6}, new String[]{null, null, "*reg-F64*"});
    rulev[89] = new Rule(89, false, false, 7, "89: void -> (SET F64 _12 regd)", ImList.list(ImList.list("std","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{35,6}, new String[]{null, null, "*reg-F64*"});
    rulev[91] = new Rule(91, false, false, 7, "91: void -> (JUMP _ label)", ImList.list(ImList.list("ba","$1"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{36}, new String[]{null, null});
    rulev[93] = new Rule(93, false, false, 7, "93: void -> (JUMPC _ _13 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), ImList.list(ImList.list("bne","$4"),ImList.list("delayslot"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("be","$3"),ImList.list("nop"))), null, null, 0, false, true, new int[]{37,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[95] = new Rule(95, false, false, 7, "95: void -> (JUMPC _ _14 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), ImList.list(ImList.list("bne","$3"),ImList.list("delayslot"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bne","$3"),ImList.list("nop"))), null, null, 0, false, true, new int[]{38,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[97] = new Rule(97, false, false, 7, "97: void -> (JUMPC _ _15 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("bl","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("bg","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("blu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{39,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[99] = new Rule(99, false, false, 7, "99: void -> (JUMPC _ _16 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("bl","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("bg","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bleu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{40,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[101] = new Rule(101, false, false, 7, "101: void -> (JUMPC _ _17 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("bg","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("bl","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bgu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{41,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[103] = new Rule(103, false, false, 7, "103: void -> (JUMPC _ _18 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("bg","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("bl","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bgeu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{42,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[105] = new Rule(105, false, false, 7, "105: void -> (JUMPC _ _19 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("blu","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("bgu","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("blu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{43,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[107] = new Rule(107, false, false, 7, "107: void -> (JUMPC _ _20 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("blu","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("bgu","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bleu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{44,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[109] = new Rule(109, false, false, 7, "109: void -> (JUMPC _ _21 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("bgu","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("blu","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bgu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{45,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[111] = new Rule(111, false, false, 7, "111: void -> (JUMPC _ _22 label label)", new ImList(ImList.list("cmp",ImList.list("qhigh","$1"),ImList.list("qhigh","$2")), new ImList(ImList.list("bgu","$3"), new ImList(ImList.list("delayslot"), ImList.list(ImList.list("blu","$4"),ImList.list("nop"),ImList.list("cmp",ImList.list("qlow","$1"),ImList.list("qlow","$2")),ImList.list("bgeu","$3"),ImList.list("nop"))))), null, null, 0, false, true, new int[]{46,36,36}, new String[]{null, "*reg-I64*", "*reg-I64*", null, null});
    rulev[113] = new Rule(113, false, false, 7, "113: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("be","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{47,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[115] = new Rule(115, false, false, 7, "115: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bne","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{48,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[117] = new Rule(117, false, false, 7, "117: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bl","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{49,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[119] = new Rule(119, false, false, 7, "119: void -> (JUMPC _ _26 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("ble","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{50,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[121] = new Rule(121, false, false, 7, "121: void -> (JUMPC _ _27 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bg","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{51,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[123] = new Rule(123, false, false, 7, "123: void -> (JUMPC _ _28 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bge","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{52,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[125] = new Rule(125, false, false, 7, "125: void -> (JUMPC _ _29 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("blu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{53,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[127] = new Rule(127, false, false, 7, "127: void -> (JUMPC _ _30 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bleu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{54,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[129] = new Rule(129, false, false, 7, "129: void -> (JUMPC _ _31 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bgu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{55,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[131] = new Rule(131, false, false, 7, "131: void -> (JUMPC _ _32 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bgeu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{56,36,36}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[133] = new Rule(133, false, false, 7, "133: void -> (JUMPC _ _33 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("be","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{57,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[135] = new Rule(135, false, false, 7, "135: void -> (JUMPC _ _34 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bne","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{58,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[137] = new Rule(137, false, false, 7, "137: void -> (JUMPC _ _35 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bg","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{59,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[139] = new Rule(139, false, false, 7, "139: void -> (JUMPC _ _36 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bge","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{60,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[141] = new Rule(141, false, false, 7, "141: void -> (JUMPC _ _37 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bl","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{61,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[143] = new Rule(143, false, false, 7, "143: void -> (JUMPC _ _38 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("ble","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{62,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[145] = new Rule(145, false, false, 7, "145: void -> (JUMPC _ _39 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bgu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{63,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[147] = new Rule(147, false, false, 7, "147: void -> (JUMPC _ _40 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bgeu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{64,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[149] = new Rule(149, false, false, 7, "149: void -> (JUMPC _ _41 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("blu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{65,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[151] = new Rule(151, false, false, 7, "151: void -> (JUMPC _ _42 label label)", ImList.list(ImList.list("cmp","$2","$1"),ImList.list("bleu","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{66,36,36}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[153] = new Rule(153, false, false, 7, "153: void -> (JUMPC _ _43 label label)", ImList.list(ImList.list("fcmpes","$1","$2"),ImList.list("delayslot"),ImList.list("fbue","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{67,36,36}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[155] = new Rule(155, false, false, 7, "155: void -> (JUMPC _ _44 label label)", ImList.list(ImList.list("fcmped","$1","$2"),ImList.list("delayslot"),ImList.list("fbue","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{68,36,36}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[157] = new Rule(157, false, false, 7, "157: void -> (JUMPC _ _45 label label)", ImList.list(ImList.list("fcmpes","$1","$2"),ImList.list("delayslot"),ImList.list("fbne","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{69,36,36}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[159] = new Rule(159, false, false, 7, "159: void -> (JUMPC _ _46 label label)", ImList.list(ImList.list("fcmped","$1","$2"),ImList.list("delayslot"),ImList.list("fbne","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{70,36,36}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[161] = new Rule(161, false, false, 7, "161: void -> (JUMPC _ _47 label label)", ImList.list(ImList.list("fcmpes","$1","$2"),ImList.list("delayslot"),ImList.list("fbul","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{71,36,36}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[163] = new Rule(163, false, false, 7, "163: void -> (JUMPC _ _48 label label)", ImList.list(ImList.list("fcmped","$1","$2"),ImList.list("delayslot"),ImList.list("fbul","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{72,36,36}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[165] = new Rule(165, false, false, 7, "165: void -> (JUMPC _ _49 label label)", ImList.list(ImList.list("fcmpes","$1","$2"),ImList.list("delayslot"),ImList.list("fbule","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{73,36,36}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[167] = new Rule(167, false, false, 7, "167: void -> (JUMPC _ _50 label label)", ImList.list(ImList.list("fcmped","$1","$2"),ImList.list("delayslot"),ImList.list("fbule","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{74,36,36}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[169] = new Rule(169, false, false, 7, "169: void -> (JUMPC _ _51 label label)", ImList.list(ImList.list("fcmpes","$1","$2"),ImList.list("delayslot"),ImList.list("fbug","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{75,36,36}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[171] = new Rule(171, false, false, 7, "171: void -> (JUMPC _ _52 label label)", ImList.list(ImList.list("fcmped","$1","$2"),ImList.list("delayslot"),ImList.list("fbug","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{76,36,36}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[173] = new Rule(173, false, false, 7, "173: void -> (JUMPC _ _53 label label)", ImList.list(ImList.list("fcmpes","$1","$2"),ImList.list("delayslot"),ImList.list("fbuge","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{77,36,36}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[175] = new Rule(175, false, false, 7, "175: void -> (JUMPC _ _54 label label)", ImList.list(ImList.list("fcmped","$1","$2"),ImList.list("delayslot"),ImList.list("fbuge","$3"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{78,36,36}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[279] = new Rule(279, false, false, 7, "279: void -> (CALL _ fun)", ImList.list(ImList.list("call","$1"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{17}, new String[]{null, null});
    rulev[280] = new Rule(280, false, false, 7, "280: void -> (CALL _ fun)", ImList.list(ImList.list("call","$1"),ImList.list("delayslot"),ImList.list("unimp",ImList.list("_getaggsize","$$"))), null, null, 0, false, true, new int[]{17}, new String[]{null, null});
    rulev[281] = new Rule(281, false, false, 7, "281: void -> (PARALLEL _ void)", null, null, null, 0, false, false, new int[]{7}, new String[]{null, null});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 1026: return "*reg-I64*";
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
    if (name == "_getaggsize")
      return jmac1(form.elem(1));
    else if (name == "_set")
      return jmac2(form.elem(1), form.elem(2));
    return null;
  }

  /** Expand building-macro, for LirNode **/
  Object quiltLir(LirNode node) {
    switch (node.opCode) {
    case Op.SUBREG:
      return jmac3(node);
    default:
      return quiltLirDefault(node);
    }
  }

  /** Expand emit-macro for list form. **/
  String emitList(ImList form, boolean topLevel) {
    String name = (String)form.elem();
    if (name == "+")
      return jmac4(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-")
      return jmac5(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "minus")
      return jmac6(emitObject(form.elem(1)));
    else if (name == "mem")
      return jmac7(emitObject(form.elem(1)));
    else if (name == "dreg-low")
      return jmac8(emitObject(form.elem(1)));
    else if (name == "dframe-low")
      return jmac9(emitObject(form.elem(1)));
    else if (name == "qlow")
      return jmac10(emitObject(form.elem(1)));
    else if (name == "qhigh")
      return jmac11(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac12(form.elem(1));
    else if (name == "epilogue")
      return jmac13(form.elem(1), emitObject(form.elem(2)));
    else if (name == "line")
      return jmac14(emitObject(form.elem(1)));
    else if (name == "delayslot")
      return jmac15();
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
  
  private boolean isPower2(LirNode node){
    if (node instanceof LirIconst){
      long value = ((LirIconst)node).signedValue();
      return (value & (value - 1)) == 0;
    }
    return false;
  }
  
  ImList regCallClobbers = new ImList(ImList.list("REG","I32","%g0"), new ImList(ImList.list("REG","I32","%g1"), new ImList(ImList.list("REG","I32","%g2"), new ImList(ImList.list("REG","I32","%g3"), new ImList(ImList.list("REG","I32","%g4"), new ImList(ImList.list("REG","I32","%g5"), new ImList(ImList.list("REG","I32","%g6"), new ImList(ImList.list("REG","I32","%g7"), new ImList(ImList.list("REG","I32","%o0"), new ImList(ImList.list("REG","I32","%o1"), new ImList(ImList.list("REG","I32","%o2"), new ImList(ImList.list("REG","I32","%o3"), new ImList(ImList.list("REG","I32","%o4"), new ImList(ImList.list("REG","I32","%o5"), new ImList(ImList.list("REG","F64","%f0"), new ImList(ImList.list("REG","F64","%f2"), new ImList(ImList.list("REG","F64","%f4"), new ImList(ImList.list("REG","F64","%f6"), new ImList(ImList.list("REG","F64","%f8"), new ImList(ImList.list("REG","F64","%f10"), new ImList(ImList.list("REG","F64","%f12"), new ImList(ImList.list("REG","F64","%f14"), new ImList(ImList.list("REG","F64","%f16"), new ImList(ImList.list("REG","F64","%f18"), new ImList(ImList.list("REG","F64","%f20"), ImList.list(ImList.list("REG","F64","%f22"),ImList.list("REG","F64","%f24"),ImList.list("REG","F64","%f26"),ImList.list("REG","F64","%f28"),ImList.list("REG","F64","%f30")))))))))))))))))))))))))));
  
  private boolean machineOptV8;
  
  
  /** Sparc's function attribute **/
  static class SparcAttr extends FunctionAttr {
  
    /** Maximum stack space used by call. **/
    int stackRequired;
  
    /** True if the function is variable argument. **/
    boolean varArgFunction;
  
    SparcAttr(Function func) {
      super(func);
      stackRequired = ((7 * 4) + 7) & -8;
    }
  }
  
  FunctionAttr newFunctionAttr(Function func) {
    return new SparcAttr(func);
  }
  
  
  /** Return offset for va_start position. **/
  int makeVaStart(LirNode arg) {
    SparcAttr at = (SparcAttr)getFunctionAttr(func);
    at.varArgFunction = true;
  
    LirNode node = null;
    for (BiLink p = func.firstInstrList().first(); !p.atEnd(); p = p.next()) {
      node = (LirNode)p.elem();
      if (node.opCode == Op.PROLOGUE)
        break;
    }
    int n = node.nKids();
    int offset = 64 + 4;
    for (int i = 1; i < n; i++) {
      LirNode x = node.kid(i);
      offset += (Type.bytes(x.type) + 3) & -4;
      if (equalArg(arg, x))
        return offset;
    }
    /* error("va_start: bad argument") */
    return 64 + 4;
  }
  
  
  
  boolean equalArg(LirNode x, LirNode y) {
    if (x.opCode == Op.MEM)
      x = x.kid(0);
    if (y.opCode == Op.MEM)
      y = y.kid(0);
    return ((LirSymRef)x).symbol == ((LirSymRef)y).symbol;
  }
  
  
  LirNode rewriteDIVStoShift(LirNode node, BiList pre){
    LirNode src1 = node.kid(0);
    LirNode src2 = node.kid(1);
    long src2Value = ((LirIconst)src2).signedValue();
    int k;
    for (k = 0; (src2Value >>= 1) > 0; k++)
         ;
    if (k == 0 || k > 31)
      return null;
    if (src1.opCode != Op.REG) {
      src1 = func.newTemp(src1.type);
      pre.add(lir.node(Op.SET, src1.type, src1, node.kid(0)));
    }
    LirNode dst = func.newTemp(node.type);
    LirNode temp = func.newTemp(node.type);
    if (k == 1)
      pre.add(lir.node
              (Op.SET, temp.type, temp, lir.node
               (Op.RSHU, src1.type, src1, lir.iconst(src2.type, 31))));
    else
      pre.add(lir.node
              (Op.SET, temp.type, temp, lir.node
               (Op.RSHU, src1.type, lir.node
                (Op.RSHS, src1.type, src1, lir.iconst(src2.type, k-1)),
                lir.iconst(src2.type, 32-k)))); 
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.RSHS, temp.type, lir.node
              (Op.ADD, temp.type, temp, src1),
              lir.iconst(src2.type, k))));
    return dst;
  }
  
  
  
  private static final double FLT2_32 = 4294967296.0; // 2^32
  private static final double FLT2_64 = 18446744073709551616.0; // 2^64
  
  
  /** Rewrite CONVUF **/
  LirNode rewriteCONVUF(LirNode node, BiList pre) {
    LirNode src = node.kid(0);
    if (src.opCode != Op.REG) {
      src = func.newTemp(src.type);
      pre.add(lir.node(Op.SET, src.type, src, node.kid(0)));
    }
    LirNode dst = func.newTemp(node.type);
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.CONVSF, dst.type, src)));
    Label tlabel = func.newLabel();
    Label flabel = func.newLabel();
    pre.add(lir.node
            (Op.JUMPC, Type.UNKNOWN, lir.node
             (Op.TSTGES, I32, src, lir.iconst(src.type, 0)),
             lir.labelRef(tlabel), lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.ADD, dst.type, dst,
              lir.fconst(dst.type, Type.bits(src.type) > 32 ? FLT2_64 : FLT2_32))));
    pre.add(lir.node
            (Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(tlabel)));
    return dst;
  }
  
  private static final double FLT2_31 = 2147483648.0;
  private static final double FLT2_63 = 9223372036854775808.0;
  
  /** Rewrite CONVFU(x) to:
   **  if (x < 2147483648) CONVFS(x)
   **  else CONVFS(x - 2147483648) + 214748648 **/
  LirNode rewriteCONVFU(LirNode node, BiList pre) {
    LirNode src = node.kid(0);
    if (src.opCode != Op.REG) {
      src = func.newTemp(src.type);
      pre.add(lir.node(Op.SET, src.type, src, node.kid(0)));
    }
    Label tlabel = func.newLabel();
    Label flabel = func.newLabel();
    Label elabel = func.newLabel();
    LirNode dst = func.newTemp(node.type);
    double boundary = Type.bits(dst.type) > 32 ? FLT2_63 : FLT2_31;
    long bias = Type.bits(dst.type) > 32 ? -9223372036854775808L : -2147483648;
    pre.add(lir.node
            (Op.JUMPC, Type.UNKNOWN, lir.node
             (Op.TSTGES, I32, src,
              lir.fconst(src.type, boundary)),
             lir.labelRef(tlabel), lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(tlabel)));
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.ADD, dst.type, lir.node
              (Op.CONVFS, dst.type, lir.node
               (Op.SUB, src.type, src, lir.fconst(src.type, boundary))),
              lir.iconst(dst.type, bias))));
    pre.add(lir.node(Op.JUMP, Type.UNKNOWN, lir.labelRef(elabel)));
    pre.add(lir.node(Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(flabel)));
    pre.add(lir.node
            (Op.SET, dst.type, dst, lir.node
             (Op.CONVFS, dst.type, src)));
    pre.add(lir.node(Op.DEFLABEL, Type.UNKNOWN, lir.labelRef(elabel)));
    return dst;
  }
  
  
  
  
  /** Rewrite FRAME node to target machine form. **/
  LirNode rewriteFrame(LirNode node) {
    Symbol fp = func.module.globalSymtab.get("%fp");
    int off = ((SymAuto)((LirSymRef)node).symbol).offset();
    return lir.node
      (Op.ADD, node.type, lir.symRef(fp), lir.iconst(I32, (long)off));
  }
  
  
  
  /** Early-time pre-rewriting sequence. **/
  
  
  /** Return early time pre-rewriting sequence. **/
  public Transformer[] earlyRewritingSequence() {
    return new Transformer[] {
      AggregateByReference.trig,
      localEarlyRewritingTrig
    };
  }
  
  
  /** Replace unresolved constants. (in alloca) **/
  final LocalTransformer replaceConstTrig = new LocalTransformer() {
      public boolean doIt(Function func, ImList args) {
        prerewrite(func, "const");
        return true;
      }
  
      public boolean doIt(Data data, ImList args) { return true; }
        
      public String name() { return "ReplaceConst"; }
  
      public String subject() { return "Replacing Constant value"; }
    };
  
  
  
  /** Return late time pre-rewriting sequence. **/
  public Transformer[] lateRewritingSequence() {
    return new Transformer[] {
      AggregatePropagation.trig,
      localLateRewritingTrig,
      replaceConstTrig,
      ProcessFramesTrig
    };
  }
  
  
  
  
  static final int MAXREGPARAM = 6;
  
  
  /** Rewrite PROLOGUE **/
  LirNode rewritePrologue(LirNode node, BiList post) {
    BiLink sync = post.first();
  
    int n = node.nKids();
    int paramCounter = 0;
    LirNode base = regnode(I32, "%fp");
  
    SparcAttr at = (SparcAttr)getFunctionAttr(func);
    if (at.varArgFunction) {
      post.add(lir.node
               (Op.SET, I32, nthStack(I32, 0, base), regnode(I32, "%i0")));
      post.add(lir.node
               (Op.SET, I32, nthStack(I32, 1, base), regnode(I32, "%i1")));
      post.add(lir.node
               (Op.SET, I32, nthStack(I32, 2, base), regnode(I32, "%i2")));
      post.add(lir.node
               (Op.SET, I32, nthStack(I32, 3, base), regnode(I32, "%i3")));
      post.add(lir.node
               (Op.SET, I32, nthStack(I32, 4, base), regnode(I32, "%i4")));
      post.add(lir.node
               (Op.SET, I32, nthStack(I32, 5, base), regnode(I32, "%i5")));
    }
    
    for (int i = 1; i < n; i++) {
      LirNode arg = node.kid(i);
      switch (Type.tag(arg.type)) {
      case Type.INT:
        if (Type.bits(arg.type) > 32) {
          post.add(lir.node
                   (Op.SET, I32, lir.node
                    (Op.SUBREG, I32, arg, lir.untaggedIconst(I32, 1)),
                    nthParam(I32, "i", paramCounter++, base)));
          post.add(lir.node
                   (Op.SET, I32, lir.node
                    (Op.SUBREG, I32, arg, lir.untaggedIconst(I32, 0)),
                    nthParam(I32, "i", paramCounter++, base)));
        } else {
          post.add(lir.node
                   (Op.SET, arg.type, arg,
                    nthParam(arg.type, "i", paramCounter++, base)));
        }
        break;
      case Type.FLOAT:
        if (Type.bits(arg.type) <= 32) {
          // float
          if (paramCounter < MAXREGPARAM)
            sync = sync.addAfter(syncParam(paramCounter, base));
          post.add(lir.node
                   (Op.SET, arg.type, arg,
                    nthStack(arg.type, paramCounter++, base)));
        } else {
          // double
          if (paramCounter < MAXREGPARAM)
            sync = sync.addAfter(syncParam(paramCounter, base));
          if (paramCounter + 1 < MAXREGPARAM)
            sync = sync.addAfter(syncParam(paramCounter + 1, base));
          post.add(lir.node
                   (Op.SET, arg.type, arg, nthStack(arg.type, paramCounter, base)));
          paramCounter += 2;
        }
        break;
      }
    }
    if(root.spec.getCoinsOptions().isSet("gprof")){
      Symbol mcount = module.globalSymtab.addSymbol("_mcount", 4, Type.UNKNOWN, 4, ".text", "XREF", ImList.Empty);
       post.add(lir.node(Op.CALL, Type.UNKNOWN, lir.symRef(Op.STATIC, I32, mcount, ImList.Empty), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0]), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0])));
    }
    // Put new PROLOGUE operator.
    int argc = paramCounter;
    if (argc > MAXREGPARAM)
      argc = MAXREGPARAM;
    LirNode[] argv = new LirNode[argc + 1];
    argv[0] = node.kid(0);
    for (int i = 0; i < argc; i++)
      argv[i + 1] = nthParam(I32, "i", i, base);
    return lir.node(Op.PROLOGUE, Type.UNKNOWN, argv);
  }
  
  
  
  
  
  
  private LirNode regnode(int type, String name) {
    if (Type.tag(type) == Type.INT) {
      LirNode master = lir.symRef(module.globalSymtab.get(name));
      if (type == master.type)
        return master;
      else if (type == I16)
        return lir.node
          (Op.SUBREG, I16, master, lir.untaggedIconst(I32, 0));
      else if (type == I8)
        return lir.node
          (Op.SUBREG, I8, master, lir.untaggedIconst(I32, 0));
    }
    else if (Type.tag(type) == Type.FLOAT) {
      LirNode master = lir.symRef(module.globalSymtab.get(name));
      if (type == F64)
        return master;
      else if (type == F32)
        return lir.node
          (Op.SUBREG, F32, master, lir.untaggedIconst(I32, 0));
    }
    return null;
  }
  
  
  private LirNode nthParam(int type, String pref, int counter, LirNode base) {
    if (counter < MAXREGPARAM) {
      return regnode(type, "%" + pref + counter);
    } else {
      return nthStack(type, counter, base);
    }
  }
  
  private LirNode nthStack(int type, int counter, LirNode base) {
    return lir.node
      (Op.MEM, type,
       lir.node
       (Op.ADD, I32, base,
        lir.iconst(I32, 64 + 4 + counter * 4 + adjustForBigEndian(type))));
  }
  
  private int adjustForBigEndian(int type) {
    if (type == I8)
      return 3;
    else if (type == I16)
      return 2;
    else
      return 0;
  }
  
  private LirNode syncParam(int counter, LirNode base) {
    return lir.node
      (Op.SET, I32, nthStack(I32, counter, base),
       nthParam(I32, "i", counter, base));
  }
  
  private LirNode unSyncParam(int counter, LirNode base) {
    return lir.node
      (Op.SET, I32, nthParam(I32, "o", counter, base),
       nthStack(I32, counter, base));
  }
  
  
  
  /** Rewrite EPILOGUE **/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
  
    if (node.nKids() < 2)
      return node;
  
    LirNode ret = node.kid(1);
    LirNode reg;
  
    switch (Type.tag(ret.type)) {
    case Type.INT:
      if (Type.bits(ret.type) > 32)
        reg = regnode(ret.type, "%i01");
      else
        reg = regnode(ret.type, "%i0");
      pre.add(lir.node(Op.SET, ret.type, reg, ret));
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
  
    case Type.FLOAT:
      reg = regnode(ret.type, "%f0");
      pre.add(lir.node(Op.SET, ret.type, reg, ret));
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
  
    case Type.AGGREGATE:
      reg = regnode(I32, "%i0");
      pre.add(lir.node
              (Op.SET, I32, reg, lir.node
               (Op.MEM, I32, lir.node
                (Op.ADD, I32, regnode(I32, "%fp"),
                 lir.iconst(I32, 64)))));
      pre.add(lir.node
              (Op.SET, ret.type,
               lir.node(Op.MEM, ret.type, reg), ret));
      return node; // keep original info for final code emission
    }
    return null;
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
  
  
  /** Rewrite CALL node. **/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
    BiList list1 = new BiList();
    BiList list2 = new BiList();
    BiList list3 = new BiList();
    boolean reta = false;
    LirNode base = regnode(I32, "%sp");
  
    LirNode callee = node.kid(0);
    LirNode args = node.kid(1);
    LirNode ret = null;
    if (node.kid(2).nKids() > 0)
      ret = node.kid(2).kid(0);
  
  
    // callee
    if (isComplex(callee)) {
      LirNode copy = func.newTemp(callee.type);
      list2.add(lir.node(Op.SET, callee.type, copy, callee));
      node.setKid(0, copy);
    }
  
    // value returned: in case of aggregate
    if (ret != null && Type.tag(ret.type) == Type.AGGREGATE) {
      list1.add(lir.node
                (Op.SET, I32, lir.node
                 (Op.MEM, I32, lir.node
                  (Op.ADD, I32, base, lir.iconst(I32, 64))),
                 ret.kid(0)));
      reta = true;
    }
  
    // parameters
    int n = args.nKids();
    int paramCounter = 0;
    for (int i = 0; i < n; i++) {
      LirNode arg = args.kid(i);
      switch (Type.tag(arg.type)) {
      case Type.INT:
        {
          LirNode src = arg;
          if (Type.bits(arg.type) > 32) {
            if (isComplex(arg)) {
              src = func.newTemp(arg.type);
              list2.add(lir.node(Op.SET, arg.type, src, arg));
            }
            LirNode inst = lir.node
              (Op.SET, I32,
               nthParam(I32, "o", paramCounter++, base), highword(src));
            if (inst.kid(0).opCode == Op.MEM)
              list1.add(inst);
            else
              list3.add(inst);
            inst = lir.node
              (Op.SET, I32,
               nthParam(I32, "o", paramCounter++, base), lowword(src));
            if (inst.kid(0).opCode == Op.MEM)
              list1.add(inst);
            else
              list3.add(inst);
          } else {
            if (paramCounter < MAXREGPARAM && isComplex(arg)) {
              src = func.newTemp(arg.type);
              list2.add(lir.node(Op.SET, arg.type, src, arg));
            }
            LirNode inst = lir.node
              (Op.SET, src.type,
               nthParam(src.type, "o", paramCounter++, base), src);
            if (inst.kid(0).opCode == Op.MEM)
              list1.add(inst);
            else
              list3.add(inst);
          }
          break;
        }
      case Type.FLOAT:
        {
          LirNode dest = nthStack(arg.type, paramCounter, base);
          list1.add(lir.node(Op.SET, arg.type, dest, arg));
          if (paramCounter < MAXREGPARAM)
            list3.add(unSyncParam(paramCounter, base));
          paramCounter++;
          if (Type.bits(arg.type) > 32) {
            if (paramCounter < MAXREGPARAM)
              list3.add(unSyncParam(paramCounter, base));
            paramCounter++;
          }
          break;
        }
      default:
        throw new CantHappenException("Unexpected CALL parameter" + node);
      }
    }
  
    SparcAttr at = (SparcAttr)getFunctionAttr(func);
    int required = (((1 + paramCounter) * 4) + 7 & -8);
    if (required > at.stackRequired)
      at.stackRequired = required;
  
    int m = paramCounter < MAXREGPARAM ? paramCounter : MAXREGPARAM;
    LirNode[] newargv = new LirNode[m];
    int j = 0;
    for (BiLink p = list3.first(); !p.atEnd(); p = p.next()) {
      LirNode ins = (LirNode)p.elem();
      newargv[j++] = ins.kid(0);
    }
  
    try {
      node = lir.node
        (Op.PARALLEL, Type.UNKNOWN, noRescan(lir.operator
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
    pre.concatenate(list3);
  
    // value returned
    if (ret != null) {
      switch (Type.tag(ret.type)) {
      case Type.INT:
        {
          LirNode reg;
          if (Type.bits(ret.type) > 32)
            reg = regnode(ret.type, "%o01");
          else
            reg = regnode(ret.type, "%o0");
          // if ret is a complex l-value (e.g. a[i*j]),
          //  it's address computation may destroy %o0.
          // To avoid that, another temporary variable is needed.
          LirNode tmp = func.newTemp(ret.type);
          post.add(lir.node(Op.SET, ret.type, tmp, reg));
          post.add(lir.node(Op.SET, ret.type, ret, tmp));
          node.kid(0).kid(2).setKid(0, reg);
          break;
        }
      case Type.FLOAT:
        {
          LirNode reg = regnode(ret.type, "%f0");
          LirNode tmp = func.newTemp(ret.type);
          post.add(lir.node(Op.SET, ret.type, tmp, reg));
          post.add(lir.node(Op.SET, ret.type, ret, tmp));
          node.kid(0).kid(2).setKid(0, reg);
          break;
        }
      case Type.AGGREGATE:
        // no action needed
        break;
      }
    }
  
    return node;
  }
  
  
  /** Return higher 32bit of 64bit operand. **/
  LirNode highword(LirNode exp) {
    if (exp.opCode == Op.REG)
      return lir.node(Op.SUBREG, I32, exp, lir.iconst(I32, 1));
    else if (exp.opCode == Op.MEM && exp.type == I64)
      return lir.node(Op.MEM, I32, exp);
    else
      return lir.node(Op.CONVIT, I32, lir.node
                      (Op.RSHU, I64, exp, lir.iconst(I32, 32)));
  }
  
  
  /** Return lower 32bit of 64bit operand. **/
  LirNode lowword(LirNode exp) {
    if (exp.opCode == Op.REG)
      return lir.node(Op.SUBREG, I32, exp, lir.iconst(I32, 0));
    else if (exp.opCode == Op.MEM && exp.type == I64)
      return lir.node(Op.MEM, I32, lir.node
                      (Op.ADD, I32, exp, lir.iconst(I32, 4)));
    else
      return lir.node(Op.CONVIT, I32, exp);
  }
  
  
  
  
  /*
   * Code building macros.
   */
  
  /** Return aggregate size & 0xfff. **/
  Object jmac1(Object x) {
    LirNode node = (LirNode)x;
  
    ImList p = node.opt.locate("&reta");
    if (p == null)
      throw new CantHappenException("missing aggregate size");
  
    int size = ((Integer)p.elem2nd()).intValue();
    return new Integer(size & 0xfff);
  }
  
  /** Expand _set macro s.t. copying 32bit constant x to y. **/
  Object jmac2(Object x, Object y) {
    return ImList.list
      (ImList.list("sethi", ImList.list("%hi", x), y),
       ImList.list("or", y, ImList.list("%lo", x), y));
  }
  
  Object jmac3(LirNode x) {
    Symbol reg = ((LirSymRef)x.kid(0)).symbol;
    int dtype = x.type;
    int offset = (int)((LirIconst)x.kid(1)).value;
    if (dtype == F32 && offset == 1)
      return "%f" + (Integer.parseInt(reg.name.substring(2)) + 1);
    else if (reg.type == I64) {
      if (offset == 0)
        return reg.name.substring(0, 2) + reg.name.substring(3);
      else
        return reg.name.substring(0, 3);
    }
    else
      return reg.name;
  }
  
  
  /* Code emission macros.
   *  Patterns not defined below will be converted to:
   *   (foo bar baz) --> foo   bar,baz   or foo(bar,baz)
   */
  
  String jmac4(String x, String y) {
    if (y.charAt(0) == '-')
      return x + y;
    else
      return x + "+" + y;
  }
  
  String jmac5(String x, String y) {
    if (y.charAt(0) == '-')
      return x + "+" + y.substring(1);
    else
      return x + "-" + y;
  }
  
  String jmac6(String con) {
    return -Integer.parseInt(con) + "";
  }
  
  
  String jmac7(String x) { return "[" + x + "]"; }
  
  /* %defemit(high x) { return "%hi(" + x + ")"; } */
  /* %defemit(low x) { return "%lo(" + x + ")"; } */
  
  String jmac8(String x) {
    return "%f" + (Integer.parseInt(x.substring(2)) + 1);
  }
  
  String jmac9(String x) {
    return x + "+4";
  }
  
  /** Return lower 32bit of memory/register/constant operand. **/
  String jmac10(String x) {
    if (x.charAt(0) == '%')
      return x.substring(0, 2) + x.substring(3);
    else
      return "" + (Long.parseLong(x) & 0xffffffffL);
  }
  
  /** Return upper 32bit of memory/register/constant operand. **/
  String jmac11(String x) {
    if (x.charAt(0) == '%')
      return x.substring(0, 3);
    else
      return "" + ((Long.parseLong(x) >> 32) & 0xffffffffL);
  }
  
  
  String jmac12(Object f) {
    Function func = (Function)f;
    SparcAttr at = (SparcAttr)getFunctionAttr(func);
  
    int size = frameSize(func) + 64 + at.stackRequired;
    size = (size + 7) & -8; // round up to 8byte boundary
    if (size < 4096) {
      return "\tsave\t%sp,-" + size + ",%sp";
    } else {
      return
        "\tsethi\t%hi(-" + size + "),%g1\n"
        + "\tor\t%g1,%lo(-" + size + "),%g1\n"
        + "\tsave\t%sp,%g1,%sp";
    }
  }
  
  String jmac13(Object f, String rettype) {
    if (rettype == "aggregate")
      return "\tjmp\t%i7+12\n\trestore";
    else
      return "\tret\n\trestore";
  }
  
  String jmac14(String x) { return "!line " + x; }
  
  String jmac15() { return "\tnop"; }
  
  
  public int alignForType(int type) {
    if (type == F64)
      return 8;
    else
      return 4;
  }
  
  String segmentForConst() { return ".rodata"; }
  
  /** Emit data **/
  void emitData(PrintWriter out, int type, LirNode node) {
    if (type == I64) {
      long v = ((LirIconst)node).signedValue();
      out.println("\t.word\t" + ((v >> 32) & 0xffffffffL)
                  + "," + (v & 0xffffffffL));
    }
    else if (type == I32) {
      out.println("\t.word\t" + lexpConv.convert(node));
    }
    else if (type == I16) {
      out.println("\t.half\t" + ((LirIconst)node).signedValue());
    }
    else if (type == I8) {
      out.println("\t.byte\t" + ((LirIconst)node).signedValue());
    }
    else if (type == F64) {
      double value = ((LirFconst)node).value;
      long bits = Double.doubleToLongBits(value);
      out.println("\t.word\t0x" + Long.toString((bits >> 32) & 0xffffffffL, 16)
                  + ",0x" + Long.toString(bits & 0xffffffffL, 16)
                  + " /* " + value + " */");
    }
    else if (type == F32) {
      double value = ((LirFconst)node).value;
      long bits = Float.floatToIntBits((float)value);
      out.println("\t.word\t0x" + Long.toString(bits & 0xffffffffL, 16)
                  + " /* " + value + " */");
    }
    else {
      throw new CantHappenException("unknown type: " + type);
    }
  }
  
  
  /** Emit data common (.lcomm directive not available on SPARC) **/
  void emitCommon(PrintWriter out, SymStatic symbol, int bytes) {
    if (symbol.linkage == "LDEF")
      out.println("\t.local\t" + symbol.name);
    out.println("\t.common\t" + symbol.name + "," + bytes + ","
                + symbol.boundary);
  }
  
  
  /** initialize **/
  void initializeMachineDep()
  {
    if (convention == "v8" || convention == "V8")
      machineOptV8 = true;
  }
}
