/*
Productions:
 1: xregb -> (REG I8)
 2: xregb -> (SUBREG I8)
 3: xregw -> (REG I16)
 4: xregw -> (SUBREG I16)
 5: xregl -> (REG I32)
 6: xregl -> (SUBREG I32)
 7: xregq -> (REG I64)
 8: xregq -> (SUBREG I64)
 9: xregf -> (REG F32)
 10: xregf -> (SUBREG F32)
 11: xregd -> (REG F64)
 12: xregd -> (SUBREG F64)
 13: regb -> xregb
 14: regw -> xregw
 15: regl -> xregl
 16: regq -> xregq
 17: regf -> xregf
 18: regd -> xregd
 19: con -> (INTCONST _)
 20: sta -> (STATIC I32)
 21: regd -> (FLOATCONST F64 0.0)
 22: regd -> (FLOATCONST F64 1.0)
 23: regf -> (FLOATCONST F32 0.0)
 24: regf -> (FLOATCONST F32 1.0)
 25: asmcon -> con
 26: asmcon -> sta
 27: asmcon -> (ADD I32 asmcon con)
 28: asmcon -> (SUB I32 asmcon con)
 29: lab -> (LABEL I32)
 30: base -> asmcon
 31: base -> regl
 32: base -> (ADD I32 regl asmcon)
 33: base -> (ADD I32 asmcon regl)
 34: base -> (SUB I32 regl con)
 35: index -> regl
 36: _1 -> (INTCONST I32 2)
 37: index -> (MUL I32 regl _1)
 38: _2 -> (INTCONST I32 4)
 39: index -> (MUL I32 regl _2)
 40: _3 -> (INTCONST I32 8)
 41: index -> (MUL I32 regl _3)
 42: _4 -> (INTCONST I32 1)
 43: index -> (LSHS I32 regl _4)
 44: index -> (LSHS I32 regl _1)
 45: _5 -> (INTCONST I32 3)
 46: index -> (LSHS I32 regl _5)
 47: addr -> base
 48: addr -> index
 49: addr -> (ADD I32 base index)
 50: addr -> (ADD I32 index base)
 51: memq -> (MEM I64 addr)
 52: meml -> (MEM I32 addr)
 53: memw -> (MEM I16 addr)
 54: memb -> (MEM I8 addr)
 55: memf -> (MEM F32 addr)
 56: memd -> (MEM F64 addr)
 57: rcl -> regl
 58: rcl -> asmcon
 59: rcw -> regw
 60: rcw -> con
 61: rcb -> regb
 62: rcb -> con
 63: mrcl -> meml
 64: mrcl -> rcl
 65: mregl -> meml
 66: mregl -> regl
 67: mrcw -> memw
 68: mrcw -> rcw
 69: mregw -> memw
 70: mregw -> regw
 71: mrcb -> memb
 72: mrcb -> rcb
 73: mregb -> memb
 74: mregb -> regb
 75: mregq -> memq
 76: mregq -> regq
 77: mrcq -> mregq
 78: mrcq -> con
 79: callarg -> sta
 80: callarg -> regl
 81: regl -> addr
 82: regq -> mrcq
 83: regl -> mrcl
 84: regw -> mrcw
 85: regb -> mrcb
 86: regd -> memd
 87: regf -> memf
 88: void -> (SET I64 memq regq)
 89: void -> (SET I32 meml rcl)
 90: void -> (SET I16 memw rcw)
 91: void -> (SET I8 memb rcb)
 92: void -> (SET I64 xregq regq)
 93: void -> (SET I32 xregl regl)
 94: void -> (SET I16 xregw regw)
 95: void -> (SET I8 xregb regb)
 96: void -> (SET F32 xregf regf)
 97: void -> (SET F64 xregd regd)
 98: void -> (SET F64 memd regd)
 99: void -> (SET F32 memf regf)
 100: _6 -> (REG I32 "%esp")
 101: _7 -> (SUB I32 _6 regl)
 102: void -> (SET I32 _6 _7)
 103: _8 -> (SUB I32 _6 _3)
 104: _9 -> (MEM I64 _8)
 105: _10 -> (SET I64 _9 mrcq)
 106: _11 -> (SET I32 _6 _8)
 107: void -> (PARALLEL _ _10 _11)
 108: _12 -> (SUB I32 _6 _2)
 109: _13 -> (MEM I32 _12)
 110: _14 -> (SET I32 _13 mrcl)
 111: _15 -> (SET I32 _6 _12)
 112: void -> (PARALLEL _ _14 _15)
 113: _16 -> (MEM F32 _12)
 114: _17 -> (SET F32 _16 memf)
 115: void -> (PARALLEL _ _17 _15)
 116: _18 -> (MEM F64 _8)
 117: _19 -> (SET F64 _18 memd)
 118: void -> (PARALLEL _ _19 _11)
 119: _20 -> (SET F32 _16 regf)
 120: void -> (PARALLEL _ _20 _15)
 121: _21 -> (SET F64 _18 regd)
 122: void -> (PARALLEL _ _21 _11)
 123: regq -> (ADD I64 regq mrcq)
 124: regq -> (SUB I64 regq mrcq)
 125: regq -> (BAND I64 regq mrcq)
 126: regq -> (BOR I64 regq mrcq)
 127: regq -> (BXOR I64 regq mrcq)
 128: regq -> (NEG I64 regq)
 129: regq -> (BNOT I64 regq)
 130: regq -> (LSHS I64 regq con)
 131: regq -> (LSHS I64 regq con)
 132: regq -> (LSHS I64 regq con)
 133: regq -> (LSHS I64 regq shfct)
 134: regq -> (RSHS I64 regq con)
 135: regq -> (RSHS I64 regq con)
 136: regq -> (RSHS I64 regq con)
 137: regq -> (RSHS I64 regq shfct)
 138: regq -> (RSHU I64 regq con)
 139: regq -> (RSHU I64 regq con)
 140: regq -> (RSHU I64 regq con)
 141: regq -> (RSHU I64 regq shfct)
 142: shfct -> memq
 143: shfct -> regq
 144: shfct -> regl
 145: shfct -> regw
 146: shfct -> regb
 147: regq -> (MUL I64 regq regq)
 148: regq -> (DIVS I64 mrcq mrcq)
 149: regq -> (DIVU I64 mrcq mrcq)
 150: regq -> (MODS I64 mrcq mrcq)
 151: regq -> (MODU I64 mrcq mrcq)
 152: regl -> (ADD I32 regl mrcl)
 153: regl -> (SUB I32 regl mrcl)
 154: regl -> (BAND I32 regl mrcl)
 155: regl -> (BOR I32 regl mrcl)
 156: regl -> (BXOR I32 regl mrcl)
 157: regl -> (ADD I32 mrcl regl)
 158: regl -> (BAND I32 mrcl regl)
 159: regl -> (BOR I32 mrcl regl)
 160: regl -> (BXOR I32 mrcl regl)
 161: regl -> (NEG I32 regl)
 162: regl -> (BNOT I32 regl)
 163: regl -> (LSHS I32 regl con)
 164: regl -> (RSHS I32 regl con)
 165: regl -> (RSHU I32 regl con)
 166: regl -> (LSHS I32 regl shfct)
 167: regl -> (RSHS I32 regl shfct)
 168: regl -> (RSHU I32 regl shfct)
 169: regl -> (MUL I32 regl mrcl)
 170: regl -> (MUL I32 mrcl regl)
 171: regl -> (DIVS I32 regl regl)
 172: regl -> (DIVU I32 regl regl)
 173: regl -> (MODS I32 regl regl)
 174: regl -> (MODU I32 regl regl)
 175: regmemd -> regd
 176: regmemd -> memd
 177: regmemf -> regf
 178: regmemf -> memf
 179: regd -> (ADD F64 regd regmemd)
 180: regd -> (SUB F64 regd regmemd)
 181: regd -> (MUL F64 regd regmemd)
 182: regd -> (DIVS F64 regd regmemd)
 183: regd -> (NEG F64 regd)
 184: regf -> (ADD F32 regf regmemf)
 185: regf -> (SUB F32 regf regmemf)
 186: regf -> (MUL F32 regf regmemf)
 187: regf -> (DIVS F32 regf regmemf)
 188: regf -> (NEG F32 regf)
 189: reglb -> regl
 190: reglb -> regb
 191: regq -> (CONVSX I64 regl)
 192: regq -> (CONVSX I64 regw)
 193: regq -> (CONVSX I64 regb)
 194: regl -> (CONVSX I32 mregw)
 195: regl -> (CONVSX I32 mregb)
 196: regw -> (CONVSX I16 mregb)
 197: regq -> (CONVZX I64 mregl)
 198: regq -> (CONVZX I64 mregw)
 199: regq -> (CONVZX I64 mregb)
 200: regq -> (CONVZX I64 mregb)
 201: regl -> (CONVZX I32 mregw)
 202: regl -> (CONVZX I32 mregb)
 203: regw -> (CONVZX I16 mregb)
 204: regl -> (CONVIT I32 regq)
 205: regw -> (CONVIT I16 regq)
 206: regb -> (CONVIT I8 regq)
 207: regw -> (CONVIT I16 regl)
 208: regb -> (CONVIT I8 regl)
 209: regb -> (CONVIT I8 regw)
 210: regd -> (CONVSF F64 memq)
 211: regd -> (CONVSF F64 regq)
 212: regd -> (CONVSF F64 meml)
 213: regd -> (CONVSF F64 reglb)
 214: regd -> (CONVSF F64 memw)
 215: regd -> (CONVSF F64 regw)
 216: regf -> (CONVSF F32 memq)
 217: regf -> (CONVSF F32 regq)
 218: regf -> (CONVSF F32 meml)
 219: regf -> (CONVSF F32 reglb)
 220: regf -> (CONVSF F32 memw)
 221: regf -> (CONVSF F32 regw)
 222: regd -> (CONVFX F64 regf)
 223: regf -> (CONVFT F32 regd)
 224: regq -> (CONVFS I64 regd)
 225: regl -> (CONVFS I32 regd)
 226: regw -> (CONVFS I16 regd)
 227: regb -> (CONVFS I8 regd)
 228: _22 -> (CONVFS I64 regd)
 229: void -> (SET I64 memq _22)
 230: _23 -> (CONVFS I32 regd)
 231: void -> (SET I32 meml _23)
 232: _24 -> (CONVFS I16 regd)
 233: void -> (SET I32 memw _24)
 234: regq -> (CONVFS I64 regf)
 235: regl -> (CONVFS I32 regf)
 236: regw -> (CONVFS I16 regf)
 237: regb -> (CONVFS I8 regf)
 238: _25 -> (CONVFS I64 regf)
 239: void -> (SET I64 memq _25)
 240: _26 -> (CONVFS I32 regf)
 241: void -> (SET I32 meml _26)
 242: _27 -> (CONVFS I16 regf)
 243: void -> (SET I32 memw _27)
 244: void -> (JUMP _ lab)
 245: _28 -> (TSTEQ I32 regq mrcq)
 246: void -> (JUMPC _ _28 lab lab)
 247: _29 -> (TSTNE I32 regq mrcq)
 248: void -> (JUMPC _ _29 lab lab)
 249: _30 -> (TSTLTS I32 regq mrcq)
 250: void -> (JUMPC _ _30 lab lab)
 251: _31 -> (TSTLES I32 regq mrcq)
 252: void -> (JUMPC _ _31 lab lab)
 253: _32 -> (TSTGTS I32 regq mrcq)
 254: void -> (JUMPC _ _32 lab lab)
 255: _33 -> (TSTGES I32 regq mrcq)
 256: void -> (JUMPC _ _33 lab lab)
 257: _34 -> (TSTLTU I32 regq mrcq)
 258: void -> (JUMPC _ _34 lab lab)
 259: _35 -> (TSTLEU I32 regq mrcq)
 260: void -> (JUMPC _ _35 lab lab)
 261: _36 -> (TSTGTU I32 regq mrcq)
 262: void -> (JUMPC _ _36 lab lab)
 263: _37 -> (TSTGEU I32 regq mrcq)
 264: void -> (JUMPC _ _37 lab lab)
 265: _38 -> (TSTEQ I32 regl mrcl)
 266: void -> (JUMPC _ _38 lab lab)
 267: _39 -> (TSTNE I32 regl mrcl)
 268: void -> (JUMPC _ _39 lab lab)
 269: _40 -> (TSTLTS I32 regl mrcl)
 270: void -> (JUMPC _ _40 lab lab)
 271: _41 -> (TSTLES I32 regl mrcl)
 272: void -> (JUMPC _ _41 lab lab)
 273: _42 -> (TSTGTS I32 regl mrcl)
 274: void -> (JUMPC _ _42 lab lab)
 275: _43 -> (TSTGES I32 regl mrcl)
 276: void -> (JUMPC _ _43 lab lab)
 277: _44 -> (TSTLTU I32 regl mrcl)
 278: void -> (JUMPC _ _44 lab lab)
 279: _45 -> (TSTLEU I32 regl mrcl)
 280: void -> (JUMPC _ _45 lab lab)
 281: _46 -> (TSTGTU I32 regl mrcl)
 282: void -> (JUMPC _ _46 lab lab)
 283: _47 -> (TSTGEU I32 regl mrcl)
 284: void -> (JUMPC _ _47 lab lab)
 285: _48 -> (TSTEQ I32 mrcl regl)
 286: void -> (JUMPC _ _48 lab lab)
 287: _49 -> (TSTNE I32 mrcl regl)
 288: void -> (JUMPC _ _49 lab lab)
 289: _50 -> (TSTLTS I32 mrcl regl)
 290: void -> (JUMPC _ _50 lab lab)
 291: _51 -> (TSTLES I32 mrcl regl)
 292: void -> (JUMPC _ _51 lab lab)
 293: _52 -> (TSTGTS I32 mrcl regl)
 294: void -> (JUMPC _ _52 lab lab)
 295: _53 -> (TSTGES I32 mrcl regl)
 296: void -> (JUMPC _ _53 lab lab)
 297: _54 -> (TSTLTU I32 mrcl regl)
 298: void -> (JUMPC _ _54 lab lab)
 299: _55 -> (TSTLEU I32 mrcl regl)
 300: void -> (JUMPC _ _55 lab lab)
 301: _56 -> (TSTGTU I32 mrcl regl)
 302: void -> (JUMPC _ _56 lab lab)
 303: _57 -> (TSTGEU I32 mrcl regl)
 304: void -> (JUMPC _ _57 lab lab)
 305: _58 -> (TSTEQ I32 regb mrcb)
 306: void -> (JUMPC _ _58 lab lab)
 307: _59 -> (TSTNE I32 regb mrcb)
 308: void -> (JUMPC _ _59 lab lab)
 309: _60 -> (TSTEQ I32 mrcb regb)
 310: void -> (JUMPC _ _60 lab lab)
 311: _61 -> (TSTNE I32 mrcb regb)
 312: void -> (JUMPC _ _61 lab lab)
 313: _62 -> (TSTEQ I32 regd regmemd)
 314: void -> (JUMPC _ _62 lab lab)
 315: _63 -> (TSTNE I32 regd regmemd)
 316: void -> (JUMPC _ _63 lab lab)
 317: _64 -> (TSTEQ I32 regf regmemf)
 318: void -> (JUMPC _ _64 lab lab)
 319: _65 -> (TSTNE I32 regf regmemf)
 320: void -> (JUMPC _ _65 lab lab)
 321: _66 -> (TSTLTS I32 regd regmemd)
 322: void -> (JUMPC _ _66 lab lab)
 323: _67 -> (TSTGES I32 regd regmemd)
 324: void -> (JUMPC _ _67 lab lab)
 325: _68 -> (TSTLTS I32 regf regmemf)
 326: void -> (JUMPC _ _68 lab lab)
 327: _69 -> (TSTGES I32 regf regmemf)
 328: void -> (JUMPC _ _69 lab lab)
 329: _70 -> (TSTGTS I32 regd regmemd)
 330: void -> (JUMPC _ _70 lab lab)
 331: _71 -> (TSTLES I32 regd regmemd)
 332: void -> (JUMPC _ _71 lab lab)
 333: _72 -> (TSTGTS I32 regf regmemf)
 334: void -> (JUMPC _ _72 lab lab)
 335: _73 -> (TSTLES I32 regf regmemf)
 336: void -> (JUMPC _ _73 lab lab)
 337: void -> (CALL _ callarg)
*/
/*
Sorted Productions:
 76: mregq -> regq
 143: shfct -> regq
 31: base -> regl
 35: index -> regl
 57: rcl -> regl
 66: mregl -> regl
 80: callarg -> regl
 144: shfct -> regl
 189: reglb -> regl
 59: rcw -> regw
 70: mregw -> regw
 145: shfct -> regw
 61: rcb -> regb
 74: mregb -> regb
 146: shfct -> regb
 190: reglb -> regb
 177: regmemf -> regf
 175: regmemd -> regd
 13: regb -> xregb
 14: regw -> xregw
 15: regl -> xregl
 16: regq -> xregq
 17: regf -> xregf
 18: regd -> xregd
 25: asmcon -> con
 60: rcw -> con
 62: rcb -> con
 78: mrcq -> con
 26: asmcon -> sta
 79: callarg -> sta
 30: base -> asmcon
 58: rcl -> asmcon
 47: addr -> base
 48: addr -> index
 81: regl -> addr
 75: mregq -> memq
 142: shfct -> memq
 63: mrcl -> meml
 65: mregl -> meml
 67: mrcw -> memw
 69: mregw -> memw
 71: mrcb -> memb
 73: mregb -> memb
 87: regf -> memf
 178: regmemf -> memf
 86: regd -> memd
 176: regmemd -> memd
 64: mrcl -> rcl
 68: mrcw -> rcw
 72: mrcb -> rcb
 83: regl -> mrcl
 84: regw -> mrcw
 85: regb -> mrcb
 77: mrcq -> mregq
 82: regq -> mrcq
 19: con -> (INTCONST _)
 36: _1 -> (INTCONST I32 2)
 38: _2 -> (INTCONST I32 4)
 40: _3 -> (INTCONST I32 8)
 42: _4 -> (INTCONST I32 1)
 45: _5 -> (INTCONST I32 3)
 23: regf -> (FLOATCONST F32 0.0)
 24: regf -> (FLOATCONST F32 1.0)
 21: regd -> (FLOATCONST F64 0.0)
 22: regd -> (FLOATCONST F64 1.0)
 20: sta -> (STATIC I32)
 1: xregb -> (REG I8)
 3: xregw -> (REG I16)
 5: xregl -> (REG I32)
 100: _6 -> (REG I32 "%esp")
 9: xregf -> (REG F32)
 7: xregq -> (REG I64)
 11: xregd -> (REG F64)
 2: xregb -> (SUBREG I8)
 4: xregw -> (SUBREG I16)
 6: xregl -> (SUBREG I32)
 10: xregf -> (SUBREG F32)
 8: xregq -> (SUBREG I64)
 12: xregd -> (SUBREG F64)
 29: lab -> (LABEL I32)
 161: regl -> (NEG I32 regl)
 188: regf -> (NEG F32 regf)
 128: regq -> (NEG I64 regq)
 183: regd -> (NEG F64 regd)
 27: asmcon -> (ADD I32 asmcon con)
 32: base -> (ADD I32 regl asmcon)
 33: base -> (ADD I32 asmcon regl)
 49: addr -> (ADD I32 base index)
 50: addr -> (ADD I32 index base)
 152: regl -> (ADD I32 regl mrcl)
 157: regl -> (ADD I32 mrcl regl)
 184: regf -> (ADD F32 regf regmemf)
 123: regq -> (ADD I64 regq mrcq)
 179: regd -> (ADD F64 regd regmemd)
 28: asmcon -> (SUB I32 asmcon con)
 34: base -> (SUB I32 regl con)
 101: _7 -> (SUB I32 _6 regl)
 103: _8 -> (SUB I32 _6 _3)
 108: _12 -> (SUB I32 _6 _2)
 153: regl -> (SUB I32 regl mrcl)
 185: regf -> (SUB F32 regf regmemf)
 124: regq -> (SUB I64 regq mrcq)
 180: regd -> (SUB F64 regd regmemd)
 37: index -> (MUL I32 regl _1)
 39: index -> (MUL I32 regl _2)
 41: index -> (MUL I32 regl _3)
 169: regl -> (MUL I32 regl mrcl)
 170: regl -> (MUL I32 mrcl regl)
 186: regf -> (MUL F32 regf regmemf)
 147: regq -> (MUL I64 regq regq)
 181: regd -> (MUL F64 regd regmemd)
 171: regl -> (DIVS I32 regl regl)
 187: regf -> (DIVS F32 regf regmemf)
 148: regq -> (DIVS I64 mrcq mrcq)
 182: regd -> (DIVS F64 regd regmemd)
 172: regl -> (DIVU I32 regl regl)
 149: regq -> (DIVU I64 mrcq mrcq)
 173: regl -> (MODS I32 regl regl)
 150: regq -> (MODS I64 mrcq mrcq)
 174: regl -> (MODU I32 regl regl)
 151: regq -> (MODU I64 mrcq mrcq)
 196: regw -> (CONVSX I16 mregb)
 194: regl -> (CONVSX I32 mregw)
 195: regl -> (CONVSX I32 mregb)
 191: regq -> (CONVSX I64 regl)
 192: regq -> (CONVSX I64 regw)
 193: regq -> (CONVSX I64 regb)
 203: regw -> (CONVZX I16 mregb)
 201: regl -> (CONVZX I32 mregw)
 202: regl -> (CONVZX I32 mregb)
 197: regq -> (CONVZX I64 mregl)
 198: regq -> (CONVZX I64 mregw)
 199: regq -> (CONVZX I64 mregb)
 200: regq -> (CONVZX I64 mregb)
 206: regb -> (CONVIT I8 regq)
 208: regb -> (CONVIT I8 regl)
 209: regb -> (CONVIT I8 regw)
 205: regw -> (CONVIT I16 regq)
 207: regw -> (CONVIT I16 regl)
 204: regl -> (CONVIT I32 regq)
 222: regd -> (CONVFX F64 regf)
 223: regf -> (CONVFT F32 regd)
 227: regb -> (CONVFS I8 regd)
 237: regb -> (CONVFS I8 regf)
 226: regw -> (CONVFS I16 regd)
 232: _24 -> (CONVFS I16 regd)
 236: regw -> (CONVFS I16 regf)
 242: _27 -> (CONVFS I16 regf)
 225: regl -> (CONVFS I32 regd)
 230: _23 -> (CONVFS I32 regd)
 235: regl -> (CONVFS I32 regf)
 240: _26 -> (CONVFS I32 regf)
 224: regq -> (CONVFS I64 regd)
 228: _22 -> (CONVFS I64 regd)
 234: regq -> (CONVFS I64 regf)
 238: _25 -> (CONVFS I64 regf)
 216: regf -> (CONVSF F32 memq)
 217: regf -> (CONVSF F32 regq)
 218: regf -> (CONVSF F32 meml)
 219: regf -> (CONVSF F32 reglb)
 220: regf -> (CONVSF F32 memw)
 221: regf -> (CONVSF F32 regw)
 210: regd -> (CONVSF F64 memq)
 211: regd -> (CONVSF F64 regq)
 212: regd -> (CONVSF F64 meml)
 213: regd -> (CONVSF F64 reglb)
 214: regd -> (CONVSF F64 memw)
 215: regd -> (CONVSF F64 regw)
 154: regl -> (BAND I32 regl mrcl)
 158: regl -> (BAND I32 mrcl regl)
 125: regq -> (BAND I64 regq mrcq)
 155: regl -> (BOR I32 regl mrcl)
 159: regl -> (BOR I32 mrcl regl)
 126: regq -> (BOR I64 regq mrcq)
 156: regl -> (BXOR I32 regl mrcl)
 160: regl -> (BXOR I32 mrcl regl)
 127: regq -> (BXOR I64 regq mrcq)
 162: regl -> (BNOT I32 regl)
 129: regq -> (BNOT I64 regq)
 43: index -> (LSHS I32 regl _4)
 44: index -> (LSHS I32 regl _1)
 46: index -> (LSHS I32 regl _5)
 163: regl -> (LSHS I32 regl con)
 166: regl -> (LSHS I32 regl shfct)
 130: regq -> (LSHS I64 regq con)
 131: regq -> (LSHS I64 regq con)
 132: regq -> (LSHS I64 regq con)
 133: regq -> (LSHS I64 regq shfct)
 164: regl -> (RSHS I32 regl con)
 167: regl -> (RSHS I32 regl shfct)
 134: regq -> (RSHS I64 regq con)
 135: regq -> (RSHS I64 regq con)
 136: regq -> (RSHS I64 regq con)
 137: regq -> (RSHS I64 regq shfct)
 165: regl -> (RSHU I32 regl con)
 168: regl -> (RSHU I32 regl shfct)
 138: regq -> (RSHU I64 regq con)
 139: regq -> (RSHU I64 regq con)
 140: regq -> (RSHU I64 regq con)
 141: regq -> (RSHU I64 regq shfct)
 245: _28 -> (TSTEQ I32 regq mrcq)
 265: _38 -> (TSTEQ I32 regl mrcl)
 285: _48 -> (TSTEQ I32 mrcl regl)
 305: _58 -> (TSTEQ I32 regb mrcb)
 309: _60 -> (TSTEQ I32 mrcb regb)
 313: _62 -> (TSTEQ I32 regd regmemd)
 317: _64 -> (TSTEQ I32 regf regmemf)
 247: _29 -> (TSTNE I32 regq mrcq)
 267: _39 -> (TSTNE I32 regl mrcl)
 287: _49 -> (TSTNE I32 mrcl regl)
 307: _59 -> (TSTNE I32 regb mrcb)
 311: _61 -> (TSTNE I32 mrcb regb)
 315: _63 -> (TSTNE I32 regd regmemd)
 319: _65 -> (TSTNE I32 regf regmemf)
 249: _30 -> (TSTLTS I32 regq mrcq)
 269: _40 -> (TSTLTS I32 regl mrcl)
 289: _50 -> (TSTLTS I32 mrcl regl)
 321: _66 -> (TSTLTS I32 regd regmemd)
 325: _68 -> (TSTLTS I32 regf regmemf)
 251: _31 -> (TSTLES I32 regq mrcq)
 271: _41 -> (TSTLES I32 regl mrcl)
 291: _51 -> (TSTLES I32 mrcl regl)
 331: _71 -> (TSTLES I32 regd regmemd)
 335: _73 -> (TSTLES I32 regf regmemf)
 253: _32 -> (TSTGTS I32 regq mrcq)
 273: _42 -> (TSTGTS I32 regl mrcl)
 293: _52 -> (TSTGTS I32 mrcl regl)
 329: _70 -> (TSTGTS I32 regd regmemd)
 333: _72 -> (TSTGTS I32 regf regmemf)
 255: _33 -> (TSTGES I32 regq mrcq)
 275: _43 -> (TSTGES I32 regl mrcl)
 295: _53 -> (TSTGES I32 mrcl regl)
 323: _67 -> (TSTGES I32 regd regmemd)
 327: _69 -> (TSTGES I32 regf regmemf)
 257: _34 -> (TSTLTU I32 regq mrcq)
 277: _44 -> (TSTLTU I32 regl mrcl)
 297: _54 -> (TSTLTU I32 mrcl regl)
 259: _35 -> (TSTLEU I32 regq mrcq)
 279: _45 -> (TSTLEU I32 regl mrcl)
 299: _55 -> (TSTLEU I32 mrcl regl)
 261: _36 -> (TSTGTU I32 regq mrcq)
 281: _46 -> (TSTGTU I32 regl mrcl)
 301: _56 -> (TSTGTU I32 mrcl regl)
 263: _37 -> (TSTGEU I32 regq mrcq)
 283: _47 -> (TSTGEU I32 regl mrcl)
 303: _57 -> (TSTGEU I32 mrcl regl)
 54: memb -> (MEM I8 addr)
 53: memw -> (MEM I16 addr)
 52: meml -> (MEM I32 addr)
 109: _13 -> (MEM I32 _12)
 55: memf -> (MEM F32 addr)
 113: _16 -> (MEM F32 _12)
 51: memq -> (MEM I64 addr)
 104: _9 -> (MEM I64 _8)
 56: memd -> (MEM F64 addr)
 116: _18 -> (MEM F64 _8)
 91: void -> (SET I8 memb rcb)
 95: void -> (SET I8 xregb regb)
 90: void -> (SET I16 memw rcw)
 94: void -> (SET I16 xregw regw)
 89: void -> (SET I32 meml rcl)
 93: void -> (SET I32 xregl regl)
 102: void -> (SET I32 _6 _7)
 106: _11 -> (SET I32 _6 _8)
 110: _14 -> (SET I32 _13 mrcl)
 111: _15 -> (SET I32 _6 _12)
 231: void -> (SET I32 meml _23)
 233: void -> (SET I32 memw _24)
 241: void -> (SET I32 meml _26)
 243: void -> (SET I32 memw _27)
 96: void -> (SET F32 xregf regf)
 99: void -> (SET F32 memf regf)
 114: _17 -> (SET F32 _16 memf)
 119: _20 -> (SET F32 _16 regf)
 88: void -> (SET I64 memq regq)
 92: void -> (SET I64 xregq regq)
 105: _10 -> (SET I64 _9 mrcq)
 229: void -> (SET I64 memq _22)
 239: void -> (SET I64 memq _25)
 97: void -> (SET F64 xregd regd)
 98: void -> (SET F64 memd regd)
 117: _19 -> (SET F64 _18 memd)
 121: _21 -> (SET F64 _18 regd)
 244: void -> (JUMP _ lab)
 246: void -> (JUMPC _ _28 lab lab)
 248: void -> (JUMPC _ _29 lab lab)
 250: void -> (JUMPC _ _30 lab lab)
 252: void -> (JUMPC _ _31 lab lab)
 254: void -> (JUMPC _ _32 lab lab)
 256: void -> (JUMPC _ _33 lab lab)
 258: void -> (JUMPC _ _34 lab lab)
 260: void -> (JUMPC _ _35 lab lab)
 262: void -> (JUMPC _ _36 lab lab)
 264: void -> (JUMPC _ _37 lab lab)
 266: void -> (JUMPC _ _38 lab lab)
 268: void -> (JUMPC _ _39 lab lab)
 270: void -> (JUMPC _ _40 lab lab)
 272: void -> (JUMPC _ _41 lab lab)
 274: void -> (JUMPC _ _42 lab lab)
 276: void -> (JUMPC _ _43 lab lab)
 278: void -> (JUMPC _ _44 lab lab)
 280: void -> (JUMPC _ _45 lab lab)
 282: void -> (JUMPC _ _46 lab lab)
 284: void -> (JUMPC _ _47 lab lab)
 286: void -> (JUMPC _ _48 lab lab)
 288: void -> (JUMPC _ _49 lab lab)
 290: void -> (JUMPC _ _50 lab lab)
 292: void -> (JUMPC _ _51 lab lab)
 294: void -> (JUMPC _ _52 lab lab)
 296: void -> (JUMPC _ _53 lab lab)
 298: void -> (JUMPC _ _54 lab lab)
 300: void -> (JUMPC _ _55 lab lab)
 302: void -> (JUMPC _ _56 lab lab)
 304: void -> (JUMPC _ _57 lab lab)
 306: void -> (JUMPC _ _58 lab lab)
 308: void -> (JUMPC _ _59 lab lab)
 310: void -> (JUMPC _ _60 lab lab)
 312: void -> (JUMPC _ _61 lab lab)
 314: void -> (JUMPC _ _62 lab lab)
 316: void -> (JUMPC _ _63 lab lab)
 318: void -> (JUMPC _ _64 lab lab)
 320: void -> (JUMPC _ _65 lab lab)
 322: void -> (JUMPC _ _66 lab lab)
 324: void -> (JUMPC _ _67 lab lab)
 326: void -> (JUMPC _ _68 lab lab)
 328: void -> (JUMPC _ _69 lab lab)
 330: void -> (JUMPC _ _70 lab lab)
 332: void -> (JUMPC _ _71 lab lab)
 334: void -> (JUMPC _ _72 lab lab)
 336: void -> (JUMPC _ _73 lab lab)
 337: void -> (CALL _ callarg)
 107: void -> (PARALLEL _ _10 _11)
 112: void -> (PARALLEL _ _14 _15)
 115: void -> (PARALLEL _ _17 _15)
 118: void -> (PARALLEL _ _19 _11)
 120: void -> (PARALLEL _ _20 _15)
 122: void -> (PARALLEL _ _21 _11)
*/
/*
Productions:
 1: _rewr -> (CONVUF _ _)
 2: _rewr -> (CONVFU _ _)
 3: _rewr -> (PROLOGUE _)
 4: _rewr -> (EPILOGUE _)
 5: _rewr -> (CALL _)
 6: _rewr -> (ASM _)
 7: _1 -> (STATIC I32 "__builtin_va_start")
 8: _2 -> (LIST _ _)
 9: _rewr -> (CALL _ _1 _2 _2)
 10: _3 -> (STATIC I32 "alloca")
 11: _rewr -> (CALL _ _3 _2 _2)
 12: _rewr -> (FLOATCONST _ 0.0)
 13: _rewr -> (FLOATCONST _ 1.0)
 14: _rewr -> (FLOATCONST F32)
 15: _rewr -> (FLOATCONST F64)
 16: _rewr -> (TSTNE I32 sbyteopr sbyteopr)
 17: _rewr -> (TSTEQ I32 sbyteopr sbyteopr)
 18: _rewr -> (TSTNE I32 ubyteopr ubyteopr)
 19: _rewr -> (TSTEQ I32 ubyteopr ubyteopr)
 20: sbyteopr -> (CONVSX I32 _)
 21: sbyteopr -> (INTCONST I32)
 22: ubyteopr -> (CONVZX I32 _)
 23: ubyteopr -> (INTCONST I32)
 24: _4 -> (CONVSX _ _)
 25: _rewr -> (LSHS I32 _ _4)
 26: _rewr -> (RSHS I32 _ _4)
 27: _rewr -> (RSHU I32 _ _4)
 28: _rewr -> (LSHS I64 _ _4)
 29: _rewr -> (RSHS I64 _ _4)
 30: _rewr -> (RSHU I64 _ _4)
 31: _rewr -> (REG I32 ".strretp")
 32: _rewr -> (JUMPN _)
 33: _rewr -> (SET _)
*/
/*
Sorted Productions:
 21: sbyteopr -> (INTCONST I32)
 23: ubyteopr -> (INTCONST I32)
 12: _rewr -> (FLOATCONST _ 0.0)
 13: _rewr -> (FLOATCONST _ 1.0)
 14: _rewr -> (FLOATCONST F32)
 15: _rewr -> (FLOATCONST F64)
 7: _1 -> (STATIC I32 "__builtin_va_start")
 10: _3 -> (STATIC I32 "alloca")
 31: _rewr -> (REG I32 ".strretp")
 24: _4 -> (CONVSX _ _)
 20: sbyteopr -> (CONVSX I32 _)
 22: ubyteopr -> (CONVZX I32 _)
 2: _rewr -> (CONVFU _ _)
 1: _rewr -> (CONVUF _ _)
 25: _rewr -> (LSHS I32 _ _4)
 28: _rewr -> (LSHS I64 _ _4)
 26: _rewr -> (RSHS I32 _ _4)
 29: _rewr -> (RSHS I64 _ _4)
 27: _rewr -> (RSHU I32 _ _4)
 30: _rewr -> (RSHU I64 _ _4)
 17: _rewr -> (TSTEQ I32 sbyteopr sbyteopr)
 19: _rewr -> (TSTEQ I32 ubyteopr ubyteopr)
 16: _rewr -> (TSTNE I32 sbyteopr sbyteopr)
 18: _rewr -> (TSTNE I32 ubyteopr ubyteopr)
 33: _rewr -> (SET _)
 32: _rewr -> (JUMPN _)
 5: _rewr -> (CALL _)
 9: _rewr -> (CALL _ _1 _2 _2)
 11: _rewr -> (CALL _ _3 _2 _2)
 3: _rewr -> (PROLOGUE _)
 4: _rewr -> (EPILOGUE _)
 8: _2 -> (LIST _ _)
 6: _rewr -> (ASM _)
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
import coins.backend.ana.SaveRegisters;
import coins.backend.util.NumberSet;
import coins.backend.util.BitMapSet;
import java.io.*;





public class CodeGenerator_x86 extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_x86() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 8;
    static final int NRULES = 33 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;
    static final int NT__1 = 2;
    static final int NT__2 = 3;
    static final int NT__3 = 4;
    static final int NT_sbyteopr = 5;
    static final int NT_ubyteopr = 6;
    static final int NT__4 = 7;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT_sbyteopr: return "sbyteopr";
      case NT_ubyteopr: return "ubyteopr";
      case NT__4: return "_4";
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
      case Op.INTCONST:
        if (t.type == 514) {
          if (-128 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 127) record(NT_sbyteopr, 21);
          if (((LirIconst)t).unsignedValue() <= 255) record(NT_ubyteopr, 23);
        }
        break;
      case Op.FLOATCONST:
        if (((LirFconst)t).value == 0.0)  return null;
        if (((LirFconst)t).value == 1.0)  return null;
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
          if (((LirSymRef)t).symbol.name == "__builtin_va_start") record(NT__1, 7);
          if (((LirSymRef)t).symbol.name == "alloca") record(NT__3, 10);
        }
        break;
      case Op.REG:
        if (t.type == 514) {
          if (((LirSymRef)t).symbol.name == ".strretp") if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.MEM, 514, lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%ebp")), lir.iconst(514, 8)));
          }
        }
        break;
      case Op.CONVSX:
        record(NT__4, 24);
        if (t.type == 514) {
          if (t.kid(0).type == I8) record(NT_sbyteopr, 20);
        }
        break;
      case Op.CONVZX:
        if (t.type == 514) {
          if (t.kid(0).type == I8) record(NT_ubyteopr, 22);
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
        if (t.type == 514) {
          if (kids[1].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.LSHS, 514, t.kid(0), t.kid(1).kid(0));
          }
        }
        if (t.type == 1026) {
          if (kids[1].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.LSHS, 1026, t.kid(0), t.kid(1).kid(0));
          }
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[1].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHS, 514, t.kid(0), t.kid(1).kid(0));
          }
        }
        if (t.type == 1026) {
          if (kids[1].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHS, 1026, t.kid(0), t.kid(1).kid(0));
          }
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[1].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHU, 514, t.kid(0), t.kid(1).kid(0));
          }
        }
        if (t.type == 1026) {
          if (kids[1].rule[NT__4] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHU, 1026, t.kid(0), t.kid(1).kid(0));
          }
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_sbyteopr] != 0) if (kids[1].rule[NT_sbyteopr] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.TSTEQ, 514, stripConv(t.kid(0)), stripConv(t.kid(1)));
          }
          if (kids[0].rule[NT_ubyteopr] != 0) if (kids[1].rule[NT_ubyteopr] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.TSTEQ, 514, stripConv(t.kid(0)), stripConv(t.kid(1)));
          }
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_sbyteopr] != 0) if (kids[1].rule[NT_sbyteopr] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.TSTNE, 514, stripConv(t.kid(0)), stripConv(t.kid(1)));
          }
          if (kids[0].rule[NT_ubyteopr] != 0) if (kids[1].rule[NT_ubyteopr] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.TSTNE, 514, stripConv(t.kid(0)), stripConv(t.kid(1)));
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
        if (kids[0].rule[NT__1] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%ebp")), lir.iconst(514, makeVaStart(t.kid(1).kid(0)))));
        }
        if (kids[0].rule[NT__3] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          setAllocaCalled();
          {
          pre.add(lir.node(Op.SET, 514, lir.node(Op.REG, 514, func.getSymbol("%esp")), lir.node(Op.SUB, 514, lir.node(Op.REG, 514, func.getSymbol("%esp")), lir.node(Op.BAND, 514, lir.node(Op.ADD, 514, t.kid(1).kid(0), lir.iconst(514, 3)), lir.iconst(514, -4)))));
          }
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.REG, 514, func.getSymbol("%esp")));
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
        if (kids.length == 1) record(NT__2, 8);
        break;
      case Op.ASM:
        if (phase == "late")  {
          rewritten = true;
          return noRescan(rewriteAsm(t, pre, post));
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
    static final int NNONTERM = 116;
    static final int NRULES = 337 + 1;
    static final int START_NT = 8;

    static final int NT__ = 0;
    static final int NT_regq = 1;
    static final int NT_regl = 2;
    static final int NT_reglb = 3;
    static final int NT_regw = 4;
    static final int NT_regb = 5;
    static final int NT_regf = 6;
    static final int NT_regd = 7;
    static final int NT_void = 8;
    static final int NT_xregb = 9;
    static final int NT_xregw = 10;
    static final int NT_xregl = 11;
    static final int NT_xregq = 12;
    static final int NT_xregf = 13;
    static final int NT_xregd = 14;
    static final int NT_con = 15;
    static final int NT_sta = 16;
    static final int NT_asmcon = 17;
    static final int NT_lab = 18;
    static final int NT_base = 19;
    static final int NT_index = 20;
    static final int NT__1 = 21;
    static final int NT__2 = 22;
    static final int NT__3 = 23;
    static final int NT__4 = 24;
    static final int NT__5 = 25;
    static final int NT_addr = 26;
    static final int NT_memq = 27;
    static final int NT_meml = 28;
    static final int NT_memw = 29;
    static final int NT_memb = 30;
    static final int NT_memf = 31;
    static final int NT_memd = 32;
    static final int NT_rcl = 33;
    static final int NT_rcw = 34;
    static final int NT_rcb = 35;
    static final int NT_mrcl = 36;
    static final int NT_mregl = 37;
    static final int NT_mrcw = 38;
    static final int NT_mregw = 39;
    static final int NT_mrcb = 40;
    static final int NT_mregb = 41;
    static final int NT_mregq = 42;
    static final int NT_mrcq = 43;
    static final int NT_callarg = 44;
    static final int NT__6 = 45;
    static final int NT__7 = 46;
    static final int NT__8 = 47;
    static final int NT__9 = 48;
    static final int NT__10 = 49;
    static final int NT__11 = 50;
    static final int NT__12 = 51;
    static final int NT__13 = 52;
    static final int NT__14 = 53;
    static final int NT__15 = 54;
    static final int NT__16 = 55;
    static final int NT__17 = 56;
    static final int NT__18 = 57;
    static final int NT__19 = 58;
    static final int NT__20 = 59;
    static final int NT__21 = 60;
    static final int NT_shfct = 61;
    static final int NT_regmemd = 62;
    static final int NT_regmemf = 63;
    static final int NT__22 = 64;
    static final int NT__23 = 65;
    static final int NT__24 = 66;
    static final int NT__25 = 67;
    static final int NT__26 = 68;
    static final int NT__27 = 69;
    static final int NT__28 = 70;
    static final int NT__29 = 71;
    static final int NT__30 = 72;
    static final int NT__31 = 73;
    static final int NT__32 = 74;
    static final int NT__33 = 75;
    static final int NT__34 = 76;
    static final int NT__35 = 77;
    static final int NT__36 = 78;
    static final int NT__37 = 79;
    static final int NT__38 = 80;
    static final int NT__39 = 81;
    static final int NT__40 = 82;
    static final int NT__41 = 83;
    static final int NT__42 = 84;
    static final int NT__43 = 85;
    static final int NT__44 = 86;
    static final int NT__45 = 87;
    static final int NT__46 = 88;
    static final int NT__47 = 89;
    static final int NT__48 = 90;
    static final int NT__49 = 91;
    static final int NT__50 = 92;
    static final int NT__51 = 93;
    static final int NT__52 = 94;
    static final int NT__53 = 95;
    static final int NT__54 = 96;
    static final int NT__55 = 97;
    static final int NT__56 = 98;
    static final int NT__57 = 99;
    static final int NT__58 = 100;
    static final int NT__59 = 101;
    static final int NT__60 = 102;
    static final int NT__61 = 103;
    static final int NT__62 = 104;
    static final int NT__63 = 105;
    static final int NT__64 = 106;
    static final int NT__65 = 107;
    static final int NT__66 = 108;
    static final int NT__67 = 109;
    static final int NT__68 = 110;
    static final int NT__69 = 111;
    static final int NT__70 = 112;
    static final int NT__71 = 113;
    static final int NT__72 = 114;
    static final int NT__73 = 115;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regq: return "regq";
      case NT_regl: return "regl";
      case NT_reglb: return "reglb";
      case NT_regw: return "regw";
      case NT_regb: return "regb";
      case NT_regf: return "regf";
      case NT_regd: return "regd";
      case NT_void: return "void";
      case NT_xregb: return "xregb";
      case NT_xregw: return "xregw";
      case NT_xregl: return "xregl";
      case NT_xregq: return "xregq";
      case NT_xregf: return "xregf";
      case NT_xregd: return "xregd";
      case NT_con: return "con";
      case NT_sta: return "sta";
      case NT_asmcon: return "asmcon";
      case NT_lab: return "lab";
      case NT_base: return "base";
      case NT_index: return "index";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT__4: return "_4";
      case NT__5: return "_5";
      case NT_addr: return "addr";
      case NT_memq: return "memq";
      case NT_meml: return "meml";
      case NT_memw: return "memw";
      case NT_memb: return "memb";
      case NT_memf: return "memf";
      case NT_memd: return "memd";
      case NT_rcl: return "rcl";
      case NT_rcw: return "rcw";
      case NT_rcb: return "rcb";
      case NT_mrcl: return "mrcl";
      case NT_mregl: return "mregl";
      case NT_mrcw: return "mrcw";
      case NT_mregw: return "mregw";
      case NT_mrcb: return "mrcb";
      case NT_mregb: return "mregb";
      case NT_mregq: return "mregq";
      case NT_mrcq: return "mrcq";
      case NT_callarg: return "callarg";
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
      case NT_shfct: return "shfct";
      case NT_regmemd: return "regmemd";
      case NT_regmemf: return "regmemf";
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
      case NT__55: return "_55";
      case NT__56: return "_56";
      case NT__57: return "_57";
      case NT__58: return "_58";
      case NT__59: return "_59";
      case NT__60: return "_60";
      case NT__61: return "_61";
      case NT__62: return "_62";
      case NT__63: return "_63";
      case NT__64: return "_64";
      case NT__65: return "_65";
      case NT__66: return "_66";
      case NT__67: return "_67";
      case NT__68: return "_68";
      case NT__69: return "_69";
      case NT__70: return "_70";
      case NT__71: return "_71";
      case NT__72: return "_72";
      case NT__73: return "_73";
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
          record(NT_mregq, 0 + cost1, 0 + cost2, 76);
          record(NT_shfct, 0 + cost1, 0 + cost2, 143);
          break;
        case NT_regl:
          record(NT_base, 0 + cost1, 0 + cost2, 31);
          record(NT_index, 0 + cost1, 0 + cost2, 35);
          record(NT_rcl, 0 + cost1, 0 + cost2, 57);
          record(NT_mregl, 0 + cost1, 0 + cost2, 66);
          record(NT_callarg, 0 + cost1, 0 + cost2, 80);
          record(NT_shfct, 0 + cost1, 0 + cost2, 144);
          record(NT_reglb, 0 + cost1, 0 + cost2, 189);
          break;
        case NT_regw:
          record(NT_rcw, 0 + cost1, 0 + cost2, 59);
          record(NT_mregw, 0 + cost1, 0 + cost2, 70);
          record(NT_shfct, 0 + cost1, 0 + cost2, 145);
          break;
        case NT_regb:
          record(NT_rcb, 0 + cost1, 0 + cost2, 61);
          record(NT_mregb, 0 + cost1, 0 + cost2, 74);
          record(NT_shfct, 0 + cost1, 0 + cost2, 146);
          record(NT_reglb, 2 + cost1, 2 + cost2, 190);
          break;
        case NT_regf:
          record(NT_regmemf, 0 + cost1, 0 + cost2, 177);
          break;
        case NT_regd:
          record(NT_regmemd, 0 + cost1, 0 + cost2, 175);
          break;
        case NT_xregb:
          record(NT_regb, 0 + cost1, 0 + cost2, 13);
          break;
        case NT_xregw:
          record(NT_regw, 0 + cost1, 0 + cost2, 14);
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
        case NT_con:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 25);
          record(NT_rcw, 0 + cost1, 0 + cost2, 60);
          record(NT_rcb, 0 + cost1, 0 + cost2, 62);
          record(NT_mrcq, 0 + cost1, 0 + cost2, 78);
          break;
        case NT_sta:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 26);
          record(NT_callarg, 0 + cost1, 0 + cost2, 79);
          break;
        case NT_asmcon:
          record(NT_base, 0 + cost1, 0 + cost2, 30);
          record(NT_rcl, 0 + cost1, 0 + cost2, 58);
          break;
        case NT_base:
          record(NT_addr, 0 + cost1, 0 + cost2, 47);
          break;
        case NT_index:
          record(NT_addr, 0 + cost1, 0 + cost2, 48);
          break;
        case NT_addr:
          record(NT_regl, 1 + cost1, 3 + cost2, 81);
          break;
        case NT_memq:
          record(NT_mregq, 0 + cost1, 0 + cost2, 75);
          record(NT_shfct, 1 + cost1, 1 + cost2, 142);
          break;
        case NT_meml:
          record(NT_mrcl, 0 + cost1, 0 + cost2, 63);
          record(NT_mregl, 0 + cost1, 0 + cost2, 65);
          break;
        case NT_memw:
          record(NT_mrcw, 0 + cost1, 0 + cost2, 67);
          record(NT_mregw, 0 + cost1, 0 + cost2, 69);
          break;
        case NT_memb:
          record(NT_mrcb, 0 + cost1, 0 + cost2, 71);
          record(NT_mregb, 0 + cost1, 0 + cost2, 73);
          break;
        case NT_memf:
          record(NT_regf, 1 + cost1, 1 + cost2, 87);
          record(NT_regmemf, 0 + cost1, 0 + cost2, 178);
          break;
        case NT_memd:
          record(NT_regd, 1 + cost1, 1 + cost2, 86);
          record(NT_regmemd, 0 + cost1, 0 + cost2, 176);
          break;
        case NT_rcl:
          record(NT_mrcl, 0 + cost1, 0 + cost2, 64);
          break;
        case NT_rcw:
          record(NT_mrcw, 0 + cost1, 0 + cost2, 68);
          break;
        case NT_rcb:
          record(NT_mrcb, 0 + cost1, 0 + cost2, 72);
          break;
        case NT_mrcl:
          record(NT_regl, 1 + cost1, 1 + cost2, 83);
          break;
        case NT_mrcw:
          record(NT_regw, 1 + cost1, 1 + cost2, 84);
          break;
        case NT_mrcb:
          record(NT_regb, 1 + cost1, 1 + cost2, 85);
          break;
        case NT_mregq:
          record(NT_mrcq, 0 + cost1, 0 + cost2, 77);
          break;
        case NT_mrcq:
          record(NT_regq, 2 + cost1, 2 + cost2, 82);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        record(NT_con, 0, 0, 19);
        if (t.type == 514) {
          if (((LirIconst)t).value == 2) record(NT__1, 0, 0, 36);
          if (((LirIconst)t).value == 4) record(NT__2, 0, 0, 38);
          if (((LirIconst)t).value == 8) record(NT__3, 0, 0, 40);
          if (((LirIconst)t).value == 1) record(NT__4, 0, 0, 42);
          if (((LirIconst)t).value == 3) record(NT__5, 0, 0, 45);
        }
        break;
      case Op.FLOATCONST:
        if (t.type == 516) {
          if (((LirFconst)t).value == 0.0) record(NT_regf, 1, 1, 23);
          if (((LirFconst)t).value == 1.0) record(NT_regf, 1, 1, 24);
        }
        if (t.type == 1028) {
          if (((LirFconst)t).value == 0.0) record(NT_regd, 1, 1, 21);
          if (((LirFconst)t).value == 1.0) record(NT_regd, 1, 1, 22);
        }
        break;
      case Op.STATIC:
        if (t.type == 514) {
          record(NT_sta, 0, 0, 20);
        }
        break;
      case Op.REG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 1);
        }
        if (t.type == 258) {
          record(NT_xregw, 0, 0, 3);
        }
        if (t.type == 514) {
          record(NT_xregl, 0, 0, 5);
          if (((LirSymRef)t).symbol.name == "%esp") record(NT__6, 0, 0, 100);
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
          record(NT_xregw, 0, 0, 4);
        }
        if (t.type == 514) {
          record(NT_xregl, 0, 0, 6);
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
        if (t.type == 514) {
          record(NT_lab, 0, 0, 29);
        }
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 161);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 188);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 128);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 183);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con], 27);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_asmcon] != 0) record(NT_base, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_asmcon], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_asmcon], 32);
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_base, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_regl], 33);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_index] != 0) record(NT_addr, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_index], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_index], 49);
          if (kids[0].rule[NT_index] != 0) if (kids[1].rule[NT_base] != 0) record(NT_addr, 0 + kids[0].cost1[NT_index] + kids[1].cost1[NT_base], 0 + kids[0].cost2[NT_index] + kids[1].cost2[NT_base], 50);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 152);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 157);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 184);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 123);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 179);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con], 28);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_base, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 34);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__7, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT_regl], 101);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__3] != 0) record(NT__8, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__3], 103);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__2] != 0) record(NT__12, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__2], 108);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 153);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 185);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 124);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 180);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__1] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__1], 37);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__2] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__2], 39);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__3] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__3], 41);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 169);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 170);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 186);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 7 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 7 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 147);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 181);
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 171);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 187);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 148);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 182);
        }
        break;
      case Op.DIVU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 172);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 149);
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 173);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 150);
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 174);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 151);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_mregb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 196);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_mregw] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregw], 2 + kids[0].cost2[NT_mregw], 194);
          if (kids[0].rule[NT_mregb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 195);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 191);
          if (kids[0].rule[NT_regw] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 192);
          if (kids[0].rule[NT_regb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 193);
        }
        break;
      case Op.CONVZX:
        if (t.type == 258) {
          if (kids[0].rule[NT_mregb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 203);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_mregw] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregw], 2 + kids[0].cost2[NT_mregw], 201);
          if (kids[0].rule[NT_mregb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 202);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_mregl] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregl], 2 + kids[0].cost2[NT_mregl], 197);
          if (kids[0].rule[NT_mregw] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregw], 2 + kids[0].cost2[NT_mregw], 198);
          if (kids[0].rule[NT_mregb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 199);
          if (kids[0].rule[NT_mregb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 200);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 206);
          if (kids[0].rule[NT_regl] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 208);
          if (kids[0].rule[NT_regw] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 209);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 205);
          if (kids[0].rule[NT_regl] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 207);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 204);
        }
        break;
      case Op.CONVFX:
        if (t.type == 1028) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regd, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 222);
        }
        break;
      case Op.CONVFT:
        if (t.type == 516) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regf, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 223);
        }
        break;
      case Op.CONVFS:
        if (t.type == 130) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 227);
          if (kids[0].rule[NT_regf] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 237);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 226);
          if (kids[0].rule[NT_regd] != 0) record(NT__24, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 232);
          if (kids[0].rule[NT_regf] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 236);
          if (kids[0].rule[NT_regf] != 0) record(NT__27, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 242);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 225);
          if (kids[0].rule[NT_regd] != 0) record(NT__23, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 230);
          if (kids[0].rule[NT_regf] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 235);
          if (kids[0].rule[NT_regf] != 0) record(NT__26, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 240);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 224);
          if (kids[0].rule[NT_regd] != 0) record(NT__22, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 228);
          if (kids[0].rule[NT_regf] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 234);
          if (kids[0].rule[NT_regf] != 0) record(NT__25, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 238);
        }
        break;
      case Op.CONVSF:
        if (t.type == 516) {
          if (kids[0].rule[NT_memq] != 0) record(NT_regf, 1 + kids[0].cost1[NT_memq], 1 + kids[0].cost2[NT_memq], 216);
          if (kids[0].rule[NT_regq] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 217);
          if (kids[0].rule[NT_meml] != 0) record(NT_regf, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 218);
          if (kids[0].rule[NT_reglb] != 0) record(NT_regf, 1 + kids[0].cost1[NT_reglb], 1 + kids[0].cost2[NT_reglb], 219);
          if (kids[0].rule[NT_memw] != 0) record(NT_regf, 1 + kids[0].cost1[NT_memw], 1 + kids[0].cost2[NT_memw], 220);
          if (kids[0].rule[NT_regw] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 221);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_memq] != 0) record(NT_regd, 1 + kids[0].cost1[NT_memq], 1 + kids[0].cost2[NT_memq], 210);
          if (kids[0].rule[NT_regq] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 211);
          if (kids[0].rule[NT_meml] != 0) record(NT_regd, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 212);
          if (kids[0].rule[NT_reglb] != 0) record(NT_regd, 1 + kids[0].cost1[NT_reglb], 1 + kids[0].cost2[NT_reglb], 213);
          if (kids[0].rule[NT_memw] != 0) record(NT_regd, 1 + kids[0].cost1[NT_memw], 1 + kids[0].cost2[NT_memw], 214);
          if (kids[0].rule[NT_regw] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 215);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 154);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 158);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 125);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 155);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 159);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 126);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 156);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 160);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 127);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 162);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 129);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 43);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__1] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__1], 44);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__5] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__5], 46);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 163);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 166);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 130);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 131);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() > 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 132);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 133);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 164);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 167);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 134);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 135);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() > 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 136);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 137);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 165);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 168);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 138);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 139);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() > 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 140);
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 141);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__28, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 245);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__38, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 265);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__48, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 285);
          if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_mrcb] != 0) record(NT__58, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_mrcb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_mrcb], 305);
          if (kids[0].rule[NT_mrcb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__60, 0 + kids[0].cost1[NT_mrcb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_mrcb] + kids[1].cost2[NT_regb], 309);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__62, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 313);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__64, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 317);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__29, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 247);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__39, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 267);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__49, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 287);
          if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_mrcb] != 0) record(NT__59, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_mrcb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_mrcb], 307);
          if (kids[0].rule[NT_mrcb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__61, 0 + kids[0].cost1[NT_mrcb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_mrcb] + kids[1].cost2[NT_regb], 311);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__63, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 315);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__65, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 319);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__30, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 249);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__40, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 269);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__50, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 289);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__66, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 321);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__68, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 325);
        }
        break;
      case Op.TSTLES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__31, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 251);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__41, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 271);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__51, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 291);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__71, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 331);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__73, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 335);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__32, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 253);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__42, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 273);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__52, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 293);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__70, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 329);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__72, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 333);
        }
        break;
      case Op.TSTGES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__33, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 255);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__43, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 275);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__53, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 295);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__67, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 323);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__69, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 327);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__34, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 257);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__44, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 277);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__54, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 297);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__35, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 259);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__45, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 279);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__55, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 299);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__36, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 261);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__46, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 281);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__56, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 301);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__37, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 263);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__47, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 283);
          if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__57, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 303);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addr] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 54);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addr] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 53);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addr] != 0) record(NT_meml, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 52);
          if (kids[0].rule[NT__12] != 0) record(NT__13, 0 + kids[0].cost1[NT__12], 0 + kids[0].cost2[NT__12], 109);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_addr] != 0) record(NT_memf, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 55);
          if (kids[0].rule[NT__12] != 0) record(NT__16, 0 + kids[0].cost1[NT__12], 0 + kids[0].cost2[NT__12], 113);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_addr] != 0) record(NT_memq, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 51);
          if (kids[0].rule[NT__8] != 0) record(NT__9, 0 + kids[0].cost1[NT__8], 0 + kids[0].cost2[NT__8], 104);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_addr] != 0) record(NT_memd, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 56);
          if (kids[0].rule[NT__8] != 0) record(NT__18, 0 + kids[0].cost1[NT__8], 0 + kids[0].cost2[NT__8], 116);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_rcb] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_rcb], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_rcb], 91);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 95);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT_rcw] != 0) record(NT_void, 1 + kids[0].cost1[NT_memw] + kids[1].cost1[NT_rcw], 1 + kids[0].cost2[NT_memw] + kids[1].cost2[NT_rcw], 90);
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regw], 94);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT_rcl] != 0) record(NT_void, 1 + kids[0].cost1[NT_meml] + kids[1].cost1[NT_rcl], 1 + kids[0].cost2[NT_meml] + kids[1].cost2[NT_rcl], 89);
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regl], 93);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__7] != 0) if (convention == "cygwin") record(NT_void, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__7], 102);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__8] != 0) record(NT__11, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__8], 106);
          if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__14, 0 + kids[0].cost1[NT__13] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT__13] + kids[1].cost2[NT_mrcl], 110);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__12] != 0) record(NT__15, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__12], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__12], 111);
          if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__23] != 0) record(NT_void, 2 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__23], 2 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__23], 231);
          if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__24] != 0) record(NT_void, 2 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__24], 2 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__24], 233);
          if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__26] != 0) record(NT_void, 2 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__26], 2 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__26], 241);
          if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__27] != 0) record(NT_void, 2 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__27], 2 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__27], 243);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 96);
          if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_regf], 99);
          if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_memf] != 0) record(NT__17, 0 + kids[0].cost1[NT__16] + kids[1].cost1[NT_memf], 0 + kids[0].cost2[NT__16] + kids[1].cost2[NT_memf], 114);
          if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__20, 0 + kids[0].cost1[NT__16] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT__16] + kids[1].cost2[NT_regf], 119);
        }
        if (t.type == 1026) {
          if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT_regq], 88);
          if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT_regq], 92);
          if (kids[0].rule[NT__9] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__10, 0 + kids[0].cost1[NT__9] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT__9] + kids[1].cost2[NT_mrcq], 105);
          if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT__22] != 0) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT__22], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT__22], 229);
          if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT__25] != 0) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT__25], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT__25], 239);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 97);
          if (kids[0].rule[NT_memd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_memd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_memd] + kids[1].cost2[NT_regd], 98);
          if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_memd] != 0) record(NT__19, 0 + kids[0].cost1[NT__18] + kids[1].cost1[NT_memd], 0 + kids[0].cost2[NT__18] + kids[1].cost2[NT_memd], 117);
          if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__21, 0 + kids[0].cost1[NT__18] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT__18] + kids[1].cost2[NT_regd], 121);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_lab] != 0) record(NT_void, 3 + kids[0].cost1[NT_lab], 3 + kids[0].cost2[NT_lab], 244);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__28] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__28] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 246);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__29] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__29] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 248);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__30] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__30] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 250);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__31] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__31] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 252);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__32] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__32] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 254);
        if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__33] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__33] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 256);
        if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__34] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__34] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 258);
        if (kids[0].rule[NT__35] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__35] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__35] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 260);
        if (kids[0].rule[NT__36] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__36] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__36] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 262);
        if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__37] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__37] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 264);
        if (kids[0].rule[NT__38] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__38] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__38] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 266);
        if (kids[0].rule[NT__39] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__39] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__39] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 268);
        if (kids[0].rule[NT__40] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__40] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__40] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 270);
        if (kids[0].rule[NT__41] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__41] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__41] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 272);
        if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__42] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__42] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 274);
        if (kids[0].rule[NT__43] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__43] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__43] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 276);
        if (kids[0].rule[NT__44] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__44] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__44] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 278);
        if (kids[0].rule[NT__45] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__45] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__45] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 280);
        if (kids[0].rule[NT__46] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__46] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__46] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 282);
        if (kids[0].rule[NT__47] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__47] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__47] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 284);
        if (kids[0].rule[NT__48] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__48] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__48] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 286);
        if (kids[0].rule[NT__49] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__49] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__49] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 288);
        if (kids[0].rule[NT__50] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__50] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__50] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 290);
        if (kids[0].rule[NT__51] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__51] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__51] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 292);
        if (kids[0].rule[NT__52] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__52] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__52] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 294);
        if (kids[0].rule[NT__53] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__53] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__53] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 296);
        if (kids[0].rule[NT__54] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__54] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__54] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 298);
        if (kids[0].rule[NT__55] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__55] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__55] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 300);
        if (kids[0].rule[NT__56] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__56] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__56] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 302);
        if (kids[0].rule[NT__57] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__57] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__57] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 304);
        if (kids[0].rule[NT__58] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__58] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__58] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 306);
        if (kids[0].rule[NT__59] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__59] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__59] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 308);
        if (kids[0].rule[NT__60] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__60] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__60] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 310);
        if (kids[0].rule[NT__61] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__61] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__61] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 312);
        if (kids[0].rule[NT__62] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__62] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__62] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 314);
        if (kids[0].rule[NT__63] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__63] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__63] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 316);
        if (kids[0].rule[NT__64] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__64] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__64] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 318);
        if (kids[0].rule[NT__65] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__65] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__65] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 320);
        if (kids[0].rule[NT__66] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__66] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__66] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 322);
        if (kids[0].rule[NT__67] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__67] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__67] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 324);
        if (kids[0].rule[NT__68] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__68] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__68] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 326);
        if (kids[0].rule[NT__69] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__69] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__69] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 328);
        if (kids[0].rule[NT__70] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__70] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__70] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 330);
        if (kids[0].rule[NT__71] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__71] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__71] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 332);
        if (kids[0].rule[NT__72] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__72] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__72] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 334);
        if (kids[0].rule[NT__73] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__73] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__73] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 336);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_callarg] != 0) record(NT_void, 4 + kids[0].cost1[NT_callarg], 4 + kids[0].cost2[NT_callarg], 337);
        break;
      case Op.PARALLEL:
        if (kids.length == 2) if (kids[0].rule[NT__10] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 2 + kids[0].cost1[NT__10] + kids[1].cost1[NT__11], 2 + kids[0].cost2[NT__10] + kids[1].cost2[NT__11], 107);
        if (kids.length == 2) if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__14] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__14] + kids[1].cost2[NT__15], 112);
        if (kids.length == 2) if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__17] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__17] + kids[1].cost2[NT__15], 115);
        if (kids.length == 2) if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT__19] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT__19] + kids[1].cost2[NT__11], 118);
        if (kids.length == 2) if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__20] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__20] + kids[1].cost2[NT__15], 120);
        if (kids.length == 2) if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT__21] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT__21] + kids[1].cost2[NT__11], 122);
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
    
    /** Return true if node is a stack top register. **/
    /*
    boolean isStackTopReg(LirNode node) {
      return ((LirSymRef)node).symbol.name.startsWith("%t");
    }
    */
    
  }


  private static final Rule[] rulev = new Rule[State.NRULES];

  static {
    rrinit0();
    rrinit100();
    rrinit200();
    rrinit300();
  }
  static private void rrinit0() {
    rulev[76] = new Rule(76, true, false, 42, "76: mregq -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[143] = new Rule(143, true, false, 61, "143: shfct -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-ebxecx-I64*"});
    rulev[31] = new Rule(31, true, false, 19, "31: base -> regl", null, ImList.list(ImList.list("base",ImList.list(),"$1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[35] = new Rule(35, true, false, 20, "35: index -> regl", null, ImList.list(ImList.list("index","$1","1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[57] = new Rule(57, true, false, 33, "57: rcl -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[66] = new Rule(66, true, false, 37, "66: mregl -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[80] = new Rule(80, true, false, 44, "80: callarg -> regl", null, ImList.list(ImList.list("ind","$1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[144] = new Rule(144, true, false, 61, "144: shfct -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-ecx-I32*"});
    rulev[189] = new Rule(189, true, false, 3, "189: reglb -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[59] = new Rule(59, true, false, 34, "59: rcw -> regw", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-I16*"});
    rulev[70] = new Rule(70, true, false, 39, "70: mregw -> regw", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-I16*"});
    rulev[145] = new Rule(145, true, false, 61, "145: shfct -> regw", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-cx-I16*"});
    rulev[61] = new Rule(61, true, false, 35, "61: rcb -> regb", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-I8*"});
    rulev[74] = new Rule(74, true, false, 41, "74: mregb -> regb", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-I8*"});
    rulev[146] = new Rule(146, true, false, 61, "146: shfct -> regb", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-cl-I8*"});
    rulev[190] = new Rule(190, true, false, 3, "190: reglb -> regb", ImList.list(ImList.list("movsbl","$1","$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[177] = new Rule(177, true, false, 63, "177: regmemf -> regf", null, null, null, 0, false, false, new int[]{6}, new String[]{null, "*reg-tmp-F32*"});
    rulev[175] = new Rule(175, true, false, 62, "175: regmemd -> regd", null, null, null, 0, false, false, new int[]{7}, new String[]{null, "*reg-tmp-F64*"});
    rulev[13] = new Rule(13, true, false, 5, "13: regb -> xregb", null, null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I8*", null});
    rulev[14] = new Rule(14, true, false, 4, "14: regw -> xregw", null, null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I16*", null});
    rulev[15] = new Rule(15, true, false, 2, "15: regl -> xregl", null, null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I32*", null});
    rulev[16] = new Rule(16, true, false, 1, "16: regq -> xregq", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-I64*", null});
    rulev[17] = new Rule(17, true, false, 6, "17: regf -> xregf", null, null, null, 0, false, false, new int[]{13}, new String[]{"*reg-tmp-F32*", null});
    rulev[18] = new Rule(18, true, false, 7, "18: regd -> xregd", null, null, null, 0, false, false, new int[]{14}, new String[]{"*reg-tmp-F64*", null});
    rulev[25] = new Rule(25, true, false, 17, "25: asmcon -> con", null, null, null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[60] = new Rule(60, true, false, 34, "60: rcw -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[62] = new Rule(62, true, false, 35, "62: rcb -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[78] = new Rule(78, true, false, 43, "78: mrcq -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[26] = new Rule(26, true, false, 17, "26: asmcon -> sta", null, null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[79] = new Rule(79, true, false, 44, "79: callarg -> sta", null, null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[30] = new Rule(30, true, false, 19, "30: base -> asmcon", null, ImList.list(ImList.list("base","$1",ImList.list())), null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[58] = new Rule(58, true, false, 33, "58: rcl -> asmcon", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[47] = new Rule(47, true, false, 26, "47: addr -> base", null, ImList.list(ImList.list("addr","$1",ImList.list())), null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[48] = new Rule(48, true, false, 26, "48: addr -> index", null, ImList.list(ImList.list("addr",ImList.list(),"$1")), null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[81] = new Rule(81, true, false, 2, "81: regl -> addr", ImList.list(ImList.list("leal","$1","$0")), null, null, 0, false, false, new int[]{26}, new String[]{"*reg-I32*", null});
    rulev[75] = new Rule(75, true, false, 42, "75: mregq -> memq", null, null, null, 0, false, false, new int[]{27}, new String[]{null, null});
    rulev[142] = new Rule(142, true, false, 61, "142: shfct -> memq", ImList.list(ImList.list("movb","$1","$0")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-cl-I8*", null});
    rulev[63] = new Rule(63, true, false, 36, "63: mrcl -> meml", null, null, null, 0, false, false, new int[]{28}, new String[]{null, null});
    rulev[65] = new Rule(65, true, false, 37, "65: mregl -> meml", null, null, null, 0, false, false, new int[]{28}, new String[]{null, null});
    rulev[67] = new Rule(67, true, false, 38, "67: mrcw -> memw", null, null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[69] = new Rule(69, true, false, 39, "69: mregw -> memw", null, null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[71] = new Rule(71, true, false, 40, "71: mrcb -> memb", null, null, null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[73] = new Rule(73, true, false, 41, "73: mregb -> memb", null, null, null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[87] = new Rule(87, true, false, 6, "87: regf -> memf", ImList.list(ImList.list("flds","$1","$0")), null, null, 0, false, false, new int[]{31}, new String[]{"*reg-tmp-F32*", null});
    rulev[178] = new Rule(178, true, false, 63, "178: regmemf -> memf", null, null, null, 0, false, false, new int[]{31}, new String[]{null, null});
    rulev[86] = new Rule(86, true, false, 7, "86: regd -> memd", ImList.list(ImList.list("fldl","$1","$0")), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-tmp-F64*", null});
    rulev[176] = new Rule(176, true, false, 62, "176: regmemd -> memd", null, null, null, 0, false, false, new int[]{32}, new String[]{null, null});
    rulev[64] = new Rule(64, true, false, 36, "64: mrcl -> rcl", null, null, null, 0, false, false, new int[]{33}, new String[]{null, null});
    rulev[68] = new Rule(68, true, false, 38, "68: mrcw -> rcw", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[72] = new Rule(72, true, false, 40, "72: mrcb -> rcb", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[83] = new Rule(83, true, false, 2, "83: regl -> mrcl", ImList.list(ImList.list("movl","$1","$0")), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[84] = new Rule(84, true, false, 4, "84: regw -> mrcw", ImList.list(ImList.list("movw","$1","$0")), null, null, 0, false, false, new int[]{38}, new String[]{"*reg-I16*", null});
    rulev[85] = new Rule(85, true, false, 5, "85: regb -> mrcb", ImList.list(ImList.list("movb","$1","$0")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I8*", null});
    rulev[77] = new Rule(77, true, false, 43, "77: mrcq -> mregq", null, null, null, 0, false, false, new int[]{42}, new String[]{null, null});
    rulev[82] = new Rule(82, true, false, 1, "82: regq -> mrcq", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),ImList.list("qlow","$0")),ImList.list("movl",ImList.list("qhigh","$1"),ImList.list("qhigh","$0"))), null, null, 0, true, false, new int[]{43}, new String[]{"*reg-I64*", null});
    rulev[19] = new Rule(19, false, false, 15, "19: con -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[36] = new Rule(36, false, true, 21, "36: _1 -> (INTCONST I32 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[38] = new Rule(38, false, true, 22, "38: _2 -> (INTCONST I32 4)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[40] = new Rule(40, false, true, 23, "40: _3 -> (INTCONST I32 8)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[42] = new Rule(42, false, true, 24, "42: _4 -> (INTCONST I32 1)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[45] = new Rule(45, false, true, 25, "45: _5 -> (INTCONST I32 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[23] = new Rule(23, false, false, 6, "23: regf -> (FLOATCONST F32 0.0)", ImList.list(ImList.list("fldz","$0")), null, null, 0, false, false, new int[]{}, new String[]{"*reg-tmp-F32*"});
    rulev[24] = new Rule(24, false, false, 6, "24: regf -> (FLOATCONST F32 1.0)", ImList.list(ImList.list("fld1","$0")), null, null, 0, false, false, new int[]{}, new String[]{"*reg-tmp-F32*"});
    rulev[21] = new Rule(21, false, false, 7, "21: regd -> (FLOATCONST F64 0.0)", ImList.list(ImList.list("fldz","$0")), null, null, 0, false, false, new int[]{}, new String[]{"*reg-tmp-F64*"});
    rulev[22] = new Rule(22, false, false, 7, "22: regd -> (FLOATCONST F64 1.0)", ImList.list(ImList.list("fld1","$0")), null, null, 0, false, false, new int[]{}, new String[]{"*reg-tmp-F64*"});
    rulev[20] = new Rule(20, false, false, 16, "20: sta -> (STATIC I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 9, "1: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 10, "3: xregw -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 11, "5: xregl -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[100] = new Rule(100, false, true, 45, "100: _6 -> (REG I32 \"%esp\")", null, null, null, 0, false, false, new int[]{}, null);
    rulev[9] = new Rule(9, false, false, 13, "9: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 12, "7: xregq -> (REG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[11] = new Rule(11, false, false, 14, "11: xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 9, "2: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[4] = new Rule(4, false, false, 10, "4: xregw -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 11, "6: xregl -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[10] = new Rule(10, false, false, 13, "10: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 12, "8: xregq -> (SUBREG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 14, "12: xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[29] = new Rule(29, false, false, 18, "29: lab -> (LABEL I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[161] = new Rule(161, false, false, 2, "161: regl -> (NEG I32 regl)", ImList.list(ImList.list("negl","$0")), null, null, 2, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[188] = new Rule(188, false, false, 6, "188: regf -> (NEG F32 regf)", ImList.list(ImList.list("fchs","$1","$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*"});
    rulev[128] = new Rule(128, false, false, 1, "128: regq -> (NEG I64 regq)", ImList.list(ImList.list("negl",ImList.list("qlow","$0")),ImList.list("adcl",ImList.list("imm","0"),ImList.list("qhigh","$0")),ImList.list("negl",ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[183] = new Rule(183, false, false, 7, "183: regd -> (NEG F64 regd)", ImList.list(ImList.list("fchs","$1","$0")), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*"});
    rulev[27] = new Rule(27, false, false, 17, "27: asmcon -> (ADD I32 asmcon con)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{17,15}, new String[]{null, null, null});
    rulev[32] = new Rule(32, false, false, 19, "32: base -> (ADD I32 regl asmcon)", null, ImList.list(ImList.list("base","$2","$1")), null, 0, false, false, new int[]{2,17}, new String[]{null, "*reg-I32*", null});
    rulev[33] = new Rule(33, false, false, 19, "33: base -> (ADD I32 asmcon regl)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{17,2}, new String[]{null, null, "*reg-I32*"});
    rulev[49] = new Rule(49, false, false, 26, "49: addr -> (ADD I32 base index)", null, ImList.list(ImList.list("addr","$1","$2")), null, 0, false, false, new int[]{19,20}, new String[]{null, null, null});
    rulev[50] = new Rule(50, false, false, 26, "50: addr -> (ADD I32 index base)", null, ImList.list(ImList.list("addr","$2","$1")), null, 0, false, false, new int[]{20,19}, new String[]{null, null, null});
    rulev[152] = new Rule(152, false, false, 2, "152: regl -> (ADD I32 regl mrcl)", ImList.list(ImList.list("addl","$2","$0")), null, null, 2, false, false, new int[]{2,36}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[157] = new Rule(157, false, false, 2, "157: regl -> (ADD I32 mrcl regl)", ImList.list(ImList.list("addl","$1","$0")), null, null, 4, false, false, new int[]{36,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[184] = new Rule(184, false, false, 6, "184: regf -> (ADD F32 regf regmemf)", ImList.list(ImList.list("fadd","$2","$1","$0")), null, null, 0, false, false, new int[]{6,63}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[123] = new Rule(123, false, false, 1, "123: regq -> (ADD I64 regq mrcq)", ImList.list(ImList.list("addl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("adcl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,43}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[179] = new Rule(179, false, false, 7, "179: regd -> (ADD F64 regd regmemd)", ImList.list(ImList.list("fadd","$2","$1","$0")), null, null, 0, false, false, new int[]{7,62}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[28] = new Rule(28, false, false, 17, "28: asmcon -> (SUB I32 asmcon con)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{17,15}, new String[]{null, null, null});
    rulev[34] = new Rule(34, false, false, 19, "34: base -> (SUB I32 regl con)", null, ImList.list(ImList.list("base",ImList.list("minus","$2"),"$1")), null, 0, false, false, new int[]{2,15}, new String[]{null, "*reg-I32*", null});
    rulev[101] = new Rule(101, false, true, 46, "101: _7 -> (SUB I32 _6 regl)", null, null, null, 0, false, false, new int[]{45,2}, null);
    rulev[103] = new Rule(103, false, true, 47, "103: _8 -> (SUB I32 _6 _3)", null, null, null, 0, false, false, new int[]{45,23}, null);
    rulev[108] = new Rule(108, false, true, 51, "108: _12 -> (SUB I32 _6 _2)", null, null, null, 0, false, false, new int[]{45,22}, null);
    rulev[153] = new Rule(153, false, false, 2, "153: regl -> (SUB I32 regl mrcl)", ImList.list(ImList.list("subl","$2","$0")), null, null, 2, false, false, new int[]{2,36}, new String[]{"*reg-I32*", "*reg-I32*", null});
  }
  static private void rrinit100() {
    rulev[185] = new Rule(185, false, false, 6, "185: regf -> (SUB F32 regf regmemf)", ImList.list(ImList.list("fsub","$2","$1","$0")), null, null, 0, false, false, new int[]{6,63}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[124] = new Rule(124, false, false, 1, "124: regq -> (SUB I64 regq mrcq)", ImList.list(ImList.list("subl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("sbbl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,43}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[180] = new Rule(180, false, false, 7, "180: regd -> (SUB F64 regd regmemd)", ImList.list(ImList.list("fsub","$2","$1","$0")), null, null, 0, false, false, new int[]{7,62}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[37] = new Rule(37, false, false, 20, "37: index -> (MUL I32 regl _1)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{2,21}, new String[]{null, "*reg-I32*"});
    rulev[39] = new Rule(39, false, false, 20, "39: index -> (MUL I32 regl _2)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{2,22}, new String[]{null, "*reg-I32*"});
    rulev[41] = new Rule(41, false, false, 20, "41: index -> (MUL I32 regl _3)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{2,23}, new String[]{null, "*reg-I32*"});
    rulev[169] = new Rule(169, false, false, 2, "169: regl -> (MUL I32 regl mrcl)", ImList.list(ImList.list("imull","$2","$0")), null, null, 2, false, false, new int[]{2,36}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[170] = new Rule(170, false, false, 2, "170: regl -> (MUL I32 mrcl regl)", ImList.list(ImList.list("imull","$1","$0")), null, null, 4, false, false, new int[]{36,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[186] = new Rule(186, false, false, 6, "186: regf -> (MUL F32 regf regmemf)", ImList.list(ImList.list("fmul","$2","$1","$0")), null, null, 0, false, false, new int[]{6,63}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[147] = new Rule(147, false, false, 1, "147: regq -> (MUL I64 regq regq)", new ImList(ImList.list("movl",ImList.list("qhigh","$2"),"%ebx"), ImList.list(ImList.list("imull","%eax","%ebx"),ImList.list("imull",ImList.list("qlow","$2"),"%edx"),ImList.list("addl","%edx","%ebx"),ImList.list("mull",ImList.list("qlow","$2")),ImList.list("addl","%ebx","%edx"))), null, ImList.list(ImList.list("REG","I32","%ebx")), 0, false, false, new int[]{1,1}, new String[]{"*reg-edxeax-I64*", "*reg-edxeax-I64*", "*reg-I64*"});
    rulev[181] = new Rule(181, false, false, 7, "181: regd -> (MUL F64 regd regmemd)", ImList.list(ImList.list("fmul","$2","$1","$0")), null, null, 0, false, false, new int[]{7,62}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[171] = new Rule(171, false, false, 2, "171: regl -> (DIVS I32 regl regl)", ImList.list(ImList.list("cdq"),ImList.list("idivl","$2")), null, ImList.list(ImList.list("REG","I32","%edx")), 2, false, false, new int[]{2,2}, new String[]{"*reg-eax-I32*", "*reg-I32*", "*reg-mod$2-I32*"});
    rulev[187] = new Rule(187, false, false, 6, "187: regf -> (DIVS F32 regf regmemf)", ImList.list(ImList.list("fdiv","$2","$1","$0")), null, null, 0, false, false, new int[]{6,63}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[148] = new Rule(148, false, false, 1, "148: regq -> (DIVS I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__divdi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{43,43}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[182] = new Rule(182, false, false, 7, "182: regd -> (DIVS F64 regd regmemd)", ImList.list(ImList.list("fdiv","$2","$1","$0")), null, null, 0, false, false, new int[]{7,62}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[172] = new Rule(172, false, false, 2, "172: regl -> (DIVU I32 regl regl)", ImList.list(ImList.list("xorl","%edx","%edx"),ImList.list("divl","$2")), null, ImList.list(ImList.list("REG","I32","%edx")), 2, false, false, new int[]{2,2}, new String[]{"*reg-eax-I32*", "*reg-I32*", "*reg-mod$2-I32*"});
    rulev[149] = new Rule(149, false, false, 1, "149: regq -> (DIVU I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__udivdi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{43,43}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[173] = new Rule(173, false, false, 2, "173: regl -> (MODS I32 regl regl)", ImList.list(ImList.list("cdq"),ImList.list("idivl","$2")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{2,2}, new String[]{"*reg-edx-I32*", "*reg-eax-I32*", "*reg-mod$2-I32*"});
    rulev[150] = new Rule(150, false, false, 1, "150: regq -> (MODS I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__moddi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{43,43}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[174] = new Rule(174, false, false, 2, "174: regl -> (MODU I32 regl regl)", ImList.list(ImList.list("xorl","%edx","%edx"),ImList.list("divl","$2")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{2,2}, new String[]{"*reg-edx-I32*", "*reg-eax-I32*", "*reg-mod$2-I32*"});
    rulev[151] = new Rule(151, false, false, 1, "151: regq -> (MODU I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__umoddi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{43,43}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[196] = new Rule(196, false, false, 4, "196: regw -> (CONVSX I16 mregb)", ImList.list(ImList.list("movsbw","$1","$0")), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I16*", null});
    rulev[194] = new Rule(194, false, false, 2, "194: regl -> (CONVSX I32 mregw)", ImList.list(ImList.list("movswl","$1","$0")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I32*", null});
    rulev[195] = new Rule(195, false, false, 2, "195: regl -> (CONVSX I32 mregb)", ImList.list(ImList.list("movsbl","$1","$0")), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I32*", null});
    rulev[191] = new Rule(191, false, false, 1, "191: regq -> (CONVSX I64 regl)", ImList.list(ImList.list("cdq")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-edxeax-I64*", "*reg-eax-I32*"});
    rulev[192] = new Rule(192, false, false, 1, "192: regq -> (CONVSX I64 regw)", ImList.list(ImList.list("cwde"),ImList.list("cdq")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-edxeax-I64*", "*reg-ax-I16*"});
    rulev[193] = new Rule(193, false, false, 1, "193: regq -> (CONVSX I64 regb)", ImList.list(ImList.list("movsbl","%al","%eax"),ImList.list("cdq")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-edxeax-I64*", "*reg-al-I8*"});
    rulev[203] = new Rule(203, false, false, 4, "203: regw -> (CONVZX I16 mregb)", ImList.list(ImList.list("movzbw","$1","$0")), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I16*", null});
    rulev[201] = new Rule(201, false, false, 2, "201: regl -> (CONVZX I32 mregw)", ImList.list(ImList.list("movzwl","$1","$0")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I32*", null});
    rulev[202] = new Rule(202, false, false, 2, "202: regl -> (CONVZX I32 mregb)", ImList.list(ImList.list("movzbl","$1","$0")), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I32*", null});
    rulev[197] = new Rule(197, false, false, 1, "197: regq -> (CONVZX I64 mregl)", ImList.list(ImList.list("movl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I64*", null});
    rulev[198] = new Rule(198, false, false, 1, "198: regq -> (CONVZX I64 mregw)", ImList.list(ImList.list("movzwl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I64*", null});
    rulev[199] = new Rule(199, false, false, 1, "199: regq -> (CONVZX I64 mregb)", ImList.list(ImList.list("movzwl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I64*", null});
    rulev[200] = new Rule(200, false, false, 1, "200: regq -> (CONVZX I64 mregb)", ImList.list(ImList.list("movl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I64*", null});
    rulev[206] = new Rule(206, false, false, 5, "206: regb -> (CONVIT I8 regq)", ImList.list(ImList.list("movb",ImList.list("regblow",ImList.list("qlow","$1")),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-bytable-I64*"});
    rulev[208] = new Rule(208, false, false, 5, "208: regb -> (CONVIT I8 regl)", ImList.list(ImList.list("movb",ImList.list("regblow","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-bytable-I32*"});
    rulev[209] = new Rule(209, false, false, 5, "209: regb -> (CONVIT I8 regw)", ImList.list(ImList.list("movb",ImList.list("regblow","$1"),"$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I8*", "*reg-bytable-I16*"});
    rulev[205] = new Rule(205, false, false, 4, "205: regw -> (CONVIT I16 regq)", ImList.list(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$1")),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I64*"});
    rulev[207] = new Rule(207, false, false, 4, "207: regw -> (CONVIT I16 regl)", ImList.list(ImList.list("movw",ImList.list("regwlow","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[204] = new Rule(204, false, false, 2, "204: regl -> (CONVIT I32 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I64*"});
    rulev[222] = new Rule(222, false, false, 7, "222: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("fmov","$1","$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F32*"});
    rulev[223] = new Rule(223, false, false, 6, "223: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("fstps","$1",ImList.list("reserve-tmpl")),ImList.list("flds",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F64*"});
    rulev[227] = new Rule(227, false, false, 5, "227: regb -> (CONVFS I8 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"%ax"), ImList.list(ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movb",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{7}, new String[]{"*reg-I8*", "*reg-tmp-F64*"});
    rulev[237] = new Rule(237, false, false, 5, "237: regb -> (CONVFS I8 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"%ax"), ImList.list(ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movb",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{6}, new String[]{"*reg-I8*", "*reg-tmp-F32*"});
    rulev[226] = new Rule(226, false, false, 4, "226: regw -> (CONVFS I16 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"$0"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"$0"), ImList.list(ImList.list("movw","$0",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movw",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-I16*", "*reg-tmp-F64*"});
    rulev[232] = new Rule(232, false, true, 66, "232: _24 -> (CONVFS I16 regd)", null, null, null, 0, false, false, new int[]{7}, null);
    rulev[236] = new Rule(236, false, false, 4, "236: regw -> (CONVFS I16 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"$0"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"$0"), ImList.list(ImList.list("movw","$0",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movw",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I16*", "*reg-tmp-F32*"});
    rulev[242] = new Rule(242, false, true, 69, "242: _27 -> (CONVFS I16 regf)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[225] = new Rule(225, false, false, 2, "225: regl -> (CONVFS I32 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow","$0")), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow","$0")), ImList.list(ImList.list("movw",ImList.list("regwlow","$0"),ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movl",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-I32*", "*reg-tmp-F64*"});
    rulev[230] = new Rule(230, false, true, 65, "230: _23 -> (CONVFS I32 regd)", null, null, null, 0, false, false, new int[]{7}, null);
    rulev[235] = new Rule(235, false, false, 2, "235: regl -> (CONVFS I32 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow","$0")), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow","$0")), ImList.list(ImList.list("movw",ImList.list("regwlow","$0"),ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movl",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-tmp-F32*"});
    rulev[240] = new Rule(240, false, true, 68, "240: _26 -> (CONVFS I32 regf)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[224] = new Rule(224, false, false, 1, "224: regq -> (CONVFS I64 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$0")),ImList.list("reserve-cw1")), ImList.list(ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$1",ImList.list("reserve-tmpq")),ImList.list("movl",ImList.list("qlow",ImList.list("reserve-tmpq")),ImList.list("qlow","$0")),ImList.list("movl",ImList.list("qhigh",ImList.list("reserve-tmpq")),ImList.list("qhigh","$0")),ImList.list("fldcw",ImList.list("reserve-cw0"))))))), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-I64*", "*reg-tmp-F64*"});
    rulev[228] = new Rule(228, false, true, 64, "228: _22 -> (CONVFS I64 regd)", null, null, null, 0, false, false, new int[]{7}, null);
    rulev[234] = new Rule(234, false, false, 1, "234: regq -> (CONVFS I64 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$0")),ImList.list("reserve-cw1")), ImList.list(ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$1",ImList.list("reserve-tmpq")),ImList.list("movl",ImList.list("qlow",ImList.list("reserve-tmpq")),ImList.list("qlow","$0")),ImList.list("movl",ImList.list("qhigh",ImList.list("reserve-tmpq")),ImList.list("qhigh","$0")),ImList.list("fldcw",ImList.list("reserve-cw0"))))))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I64*", "*reg-tmp-F32*"});
    rulev[238] = new Rule(238, false, true, 67, "238: _25 -> (CONVFS I64 regf)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[216] = new Rule(216, false, false, 6, "216: regf -> (CONVSF F32 memq)", ImList.list(ImList.list("fildll","$1","$0")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-tmp-F32*", null});
    rulev[217] = new Rule(217, false, false, 6, "217: regf -> (CONVSF F32 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),ImList.list("qlow",ImList.list("reserve-tmpq"))),ImList.list("movl",ImList.list("qhigh","$1"),ImList.list("qhigh",ImList.list("reserve-tmpq"))),ImList.list("fildll",ImList.list("reserve-tmpq"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-tmp-F32*", "*reg-I64*"});
    rulev[218] = new Rule(218, false, false, 6, "218: regf -> (CONVSF F32 meml)", ImList.list(ImList.list("fildl","$1","$0")), null, null, 0, false, false, new int[]{28}, new String[]{"*reg-tmp-F32*", null});
    rulev[219] = new Rule(219, false, false, 6, "219: regf -> (CONVSF F32 reglb)", ImList.list(ImList.list("movl","$1",ImList.list("reserve-tmpl")),ImList.list("fildl",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-tmp-F32*", "*reg-I32*"});
    rulev[220] = new Rule(220, false, false, 6, "220: regf -> (CONVSF F32 memw)", ImList.list(ImList.list("filds","$1","$0")), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-tmp-F32*", null});
    rulev[221] = new Rule(221, false, false, 6, "221: regf -> (CONVSF F32 regw)", ImList.list(ImList.list("movw","$1",ImList.list("reserve-tmpl")),ImList.list("filds",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-tmp-F32*", "*reg-I16*"});
    rulev[210] = new Rule(210, false, false, 7, "210: regd -> (CONVSF F64 memq)", ImList.list(ImList.list("fildll","$1","$0")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-tmp-F64*", null});
    rulev[211] = new Rule(211, false, false, 7, "211: regd -> (CONVSF F64 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),ImList.list("qlow",ImList.list("reserve-tmpq"))),ImList.list("movl",ImList.list("qhigh","$1"),ImList.list("qhigh",ImList.list("reserve-tmpq"))),ImList.list("fildll",ImList.list("reserve-tmpq"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-tmp-F64*", "*reg-I64*"});
    rulev[212] = new Rule(212, false, false, 7, "212: regd -> (CONVSF F64 meml)", ImList.list(ImList.list("fildl","$1","$0")), null, null, 0, false, false, new int[]{28}, new String[]{"*reg-tmp-F64*", null});
    rulev[213] = new Rule(213, false, false, 7, "213: regd -> (CONVSF F64 reglb)", ImList.list(ImList.list("movl","$1",ImList.list("reserve-tmpl")),ImList.list("fildl",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-tmp-F64*", "*reg-I32*"});
    rulev[214] = new Rule(214, false, false, 7, "214: regd -> (CONVSF F64 memw)", ImList.list(ImList.list("filds","$1","$0")), null, null, 0, false, false, new int[]{29}, new String[]{"*reg-tmp-F64*", null});
    rulev[215] = new Rule(215, false, false, 7, "215: regd -> (CONVSF F64 regw)", ImList.list(ImList.list("movw","$1",ImList.list("reserve-tmpl")),ImList.list("filds",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-tmp-F64*", "*reg-I16*"});
    rulev[154] = new Rule(154, false, false, 2, "154: regl -> (BAND I32 regl mrcl)", ImList.list(ImList.list("andl","$2","$0")), null, null, 2, false, false, new int[]{2,36}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[158] = new Rule(158, false, false, 2, "158: regl -> (BAND I32 mrcl regl)", ImList.list(ImList.list("andl","$1","$0")), null, null, 4, false, false, new int[]{36,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[125] = new Rule(125, false, false, 1, "125: regq -> (BAND I64 regq mrcq)", ImList.list(ImList.list("andl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("andl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,43}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[155] = new Rule(155, false, false, 2, "155: regl -> (BOR I32 regl mrcl)", ImList.list(ImList.list("orl","$2","$0")), null, null, 2, false, false, new int[]{2,36}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[159] = new Rule(159, false, false, 2, "159: regl -> (BOR I32 mrcl regl)", ImList.list(ImList.list("orl","$1","$0")), null, null, 4, false, false, new int[]{36,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[126] = new Rule(126, false, false, 1, "126: regq -> (BOR I64 regq mrcq)", ImList.list(ImList.list("orl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("orl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,43}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[156] = new Rule(156, false, false, 2, "156: regl -> (BXOR I32 regl mrcl)", ImList.list(ImList.list("xorl","$2","$0")), null, null, 2, false, false, new int[]{2,36}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[160] = new Rule(160, false, false, 2, "160: regl -> (BXOR I32 mrcl regl)", ImList.list(ImList.list("xorl","$1","$0")), null, null, 4, false, false, new int[]{36,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[127] = new Rule(127, false, false, 1, "127: regq -> (BXOR I64 regq mrcq)", ImList.list(ImList.list("xorl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,43}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[162] = new Rule(162, false, false, 2, "162: regl -> (BNOT I32 regl)", ImList.list(ImList.list("notl","$0")), null, null, 2, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[129] = new Rule(129, false, false, 1, "129: regq -> (BNOT I64 regq)", ImList.list(ImList.list("notl",ImList.list("qlow","$0")),ImList.list("notl",ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[43] = new Rule(43, false, false, 20, "43: index -> (LSHS I32 regl _4)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{2,24}, new String[]{null, "*reg-I32*"});
    rulev[44] = new Rule(44, false, false, 20, "44: index -> (LSHS I32 regl _1)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{2,21}, new String[]{null, "*reg-I32*"});
    rulev[46] = new Rule(46, false, false, 20, "46: index -> (LSHS I32 regl _5)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{2,25}, new String[]{null, "*reg-I32*"});
    rulev[163] = new Rule(163, false, false, 2, "163: regl -> (LSHS I32 regl con)", ImList.list(ImList.list("sall",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,15}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[166] = new Rule(166, false, false, 2, "166: regl -> (LSHS I32 regl shfct)", ImList.list(ImList.list("sall","%cl","$0")), null, null, 2, false, false, new int[]{2,61}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[130] = new Rule(130, false, false, 1, "130: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("shldl",ImList.list("imm","$2"),ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("shll",ImList.list("imm","$2"),ImList.list("qlow","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[131] = new Rule(131, false, false, 1, "131: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("xorl",ImList.list("qlow","$0"),ImList.list("qlow","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[132] = new Rule(132, false, false, 1, "132: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("xorl",ImList.list("qlow","$0"),ImList.list("qlow","$0")),ImList.list("shll",ImList.list("imm",ImList.list("-32","$2")),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[133] = new Rule(133, false, false, 1, "133: regq -> (LSHS I64 regq shfct)", new ImList(ImList.list("shldl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")), new ImList(ImList.list("shll","%cl",ImList.list("qlow","$0")), ImList.list(ImList.list("testb",ImList.list("imm","32"),"%cl"),ImList.list("je","$L1"),ImList.list("movl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("xorl",ImList.list("qlow","$0"),ImList.list("qlow","$0")),ImList.list("deflabel","$L1")))), null, null, 2, false, false, new int[]{1,61}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[164] = new Rule(164, false, false, 2, "164: regl -> (RSHS I32 regl con)", ImList.list(ImList.list("sarl",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,15}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[167] = new Rule(167, false, false, 2, "167: regl -> (RSHS I32 regl shfct)", ImList.list(ImList.list("sarl","%cl","$0")), null, null, 2, false, false, new int[]{2,61}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[134] = new Rule(134, false, false, 1, "134: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("shrdl",ImList.list("imm","$2"),ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[135] = new Rule(135, false, false, 1, "135: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","31"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[136] = new Rule(136, false, false, 1, "136: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("sarl",ImList.list("imm",ImList.list("-32","$2")),ImList.list("qhigh","$0")),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","31"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[137] = new Rule(137, false, false, 1, "137: regq -> (RSHS I64 regq shfct)", new ImList(ImList.list("shrdl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")), new ImList(ImList.list("sarl","%cl",ImList.list("qhigh","$0")), ImList.list(ImList.list("testb",ImList.list("imm","32"),"%cl"),ImList.list("je","$L1"),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","31"),ImList.list("qhigh","$0")),ImList.list("deflabel","$L1")))), null, null, 2, false, false, new int[]{1,61}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[165] = new Rule(165, false, false, 2, "165: regl -> (RSHU I32 regl con)", ImList.list(ImList.list("shrl",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,15}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[168] = new Rule(168, false, false, 2, "168: regl -> (RSHU I32 regl shfct)", ImList.list(ImList.list("shrl","%cl","$0")), null, null, 2, false, false, new int[]{2,61}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[138] = new Rule(138, false, false, 1, "138: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("shrdl",ImList.list("imm","$2"),ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("shrl",ImList.list("imm","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[139] = new Rule(139, false, false, 1, "139: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("xor",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[140] = new Rule(140, false, false, 1, "140: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("shrl",ImList.list("imm",ImList.list("-32","$2")),ImList.list("qhigh","$0")),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("xor",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,15}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[141] = new Rule(141, false, false, 1, "141: regq -> (RSHU I64 regq shfct)", new ImList(ImList.list("shrdl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")), new ImList(ImList.list("shrl","%cl",ImList.list("qhigh","$0")), ImList.list(ImList.list("testb",ImList.list("imm","32"),"%cl"),ImList.list("je","$L1"),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0")),ImList.list("deflabel","$L1")))), null, null, 2, false, false, new int[]{1,61}, new String[]{"*reg-I64*", "*reg-I64*", null});
  }
  static private void rrinit200() {
    rulev[245] = new Rule(245, false, true, 70, "245: _28 -> (TSTEQ I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[265] = new Rule(265, false, true, 80, "265: _38 -> (TSTEQ I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[285] = new Rule(285, false, true, 90, "285: _48 -> (TSTEQ I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[305] = new Rule(305, false, true, 100, "305: _58 -> (TSTEQ I32 regb mrcb)", null, null, null, 0, false, false, new int[]{5,40}, null);
    rulev[309] = new Rule(309, false, true, 102, "309: _60 -> (TSTEQ I32 mrcb regb)", null, null, null, 0, false, false, new int[]{40,5}, null);
    rulev[313] = new Rule(313, false, true, 104, "313: _62 -> (TSTEQ I32 regd regmemd)", null, null, null, 0, false, false, new int[]{7,62}, null);
    rulev[317] = new Rule(317, false, true, 106, "317: _64 -> (TSTEQ I32 regf regmemf)", null, null, null, 0, false, false, new int[]{6,63}, null);
    rulev[247] = new Rule(247, false, true, 71, "247: _29 -> (TSTNE I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[267] = new Rule(267, false, true, 81, "267: _39 -> (TSTNE I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[287] = new Rule(287, false, true, 91, "287: _49 -> (TSTNE I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[307] = new Rule(307, false, true, 101, "307: _59 -> (TSTNE I32 regb mrcb)", null, null, null, 0, false, false, new int[]{5,40}, null);
    rulev[311] = new Rule(311, false, true, 103, "311: _61 -> (TSTNE I32 mrcb regb)", null, null, null, 0, false, false, new int[]{40,5}, null);
    rulev[315] = new Rule(315, false, true, 105, "315: _63 -> (TSTNE I32 regd regmemd)", null, null, null, 0, false, false, new int[]{7,62}, null);
    rulev[319] = new Rule(319, false, true, 107, "319: _65 -> (TSTNE I32 regf regmemf)", null, null, null, 0, false, false, new int[]{6,63}, null);
    rulev[249] = new Rule(249, false, true, 72, "249: _30 -> (TSTLTS I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[269] = new Rule(269, false, true, 82, "269: _40 -> (TSTLTS I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[289] = new Rule(289, false, true, 92, "289: _50 -> (TSTLTS I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[321] = new Rule(321, false, true, 108, "321: _66 -> (TSTLTS I32 regd regmemd)", null, null, null, 0, false, false, new int[]{7,62}, null);
    rulev[325] = new Rule(325, false, true, 110, "325: _68 -> (TSTLTS I32 regf regmemf)", null, null, null, 0, false, false, new int[]{6,63}, null);
    rulev[251] = new Rule(251, false, true, 73, "251: _31 -> (TSTLES I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[271] = new Rule(271, false, true, 83, "271: _41 -> (TSTLES I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[291] = new Rule(291, false, true, 93, "291: _51 -> (TSTLES I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[331] = new Rule(331, false, true, 113, "331: _71 -> (TSTLES I32 regd regmemd)", null, null, null, 0, false, false, new int[]{7,62}, null);
    rulev[335] = new Rule(335, false, true, 115, "335: _73 -> (TSTLES I32 regf regmemf)", null, null, null, 0, false, false, new int[]{6,63}, null);
    rulev[253] = new Rule(253, false, true, 74, "253: _32 -> (TSTGTS I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[273] = new Rule(273, false, true, 84, "273: _42 -> (TSTGTS I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[293] = new Rule(293, false, true, 94, "293: _52 -> (TSTGTS I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[329] = new Rule(329, false, true, 112, "329: _70 -> (TSTGTS I32 regd regmemd)", null, null, null, 0, false, false, new int[]{7,62}, null);
    rulev[333] = new Rule(333, false, true, 114, "333: _72 -> (TSTGTS I32 regf regmemf)", null, null, null, 0, false, false, new int[]{6,63}, null);
    rulev[255] = new Rule(255, false, true, 75, "255: _33 -> (TSTGES I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[275] = new Rule(275, false, true, 85, "275: _43 -> (TSTGES I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[295] = new Rule(295, false, true, 95, "295: _53 -> (TSTGES I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[323] = new Rule(323, false, true, 109, "323: _67 -> (TSTGES I32 regd regmemd)", null, null, null, 0, false, false, new int[]{7,62}, null);
    rulev[327] = new Rule(327, false, true, 111, "327: _69 -> (TSTGES I32 regf regmemf)", null, null, null, 0, false, false, new int[]{6,63}, null);
    rulev[257] = new Rule(257, false, true, 76, "257: _34 -> (TSTLTU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[277] = new Rule(277, false, true, 86, "277: _44 -> (TSTLTU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[297] = new Rule(297, false, true, 96, "297: _54 -> (TSTLTU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[259] = new Rule(259, false, true, 77, "259: _35 -> (TSTLEU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[279] = new Rule(279, false, true, 87, "279: _45 -> (TSTLEU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[299] = new Rule(299, false, true, 97, "299: _55 -> (TSTLEU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[261] = new Rule(261, false, true, 78, "261: _36 -> (TSTGTU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[281] = new Rule(281, false, true, 88, "281: _46 -> (TSTGTU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[301] = new Rule(301, false, true, 98, "301: _56 -> (TSTGTU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[263] = new Rule(263, false, true, 79, "263: _37 -> (TSTGEU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,43}, null);
    rulev[283] = new Rule(283, false, true, 89, "283: _47 -> (TSTGEU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,36}, null);
    rulev[303] = new Rule(303, false, true, 99, "303: _57 -> (TSTGEU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{36,2}, null);
    rulev[54] = new Rule(54, false, false, 30, "54: memb -> (MEM I8 addr)", null, ImList.list(ImList.list("mem","byte","$1")), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[53] = new Rule(53, false, false, 29, "53: memw -> (MEM I16 addr)", null, ImList.list(ImList.list("mem","word","$1")), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[52] = new Rule(52, false, false, 28, "52: meml -> (MEM I32 addr)", null, ImList.list(ImList.list("mem","long","$1")), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[109] = new Rule(109, false, true, 52, "109: _13 -> (MEM I32 _12)", null, null, null, 0, false, false, new int[]{51}, null);
    rulev[55] = new Rule(55, false, false, 31, "55: memf -> (MEM F32 addr)", null, ImList.list(ImList.list("mem","float","$1")), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[113] = new Rule(113, false, true, 55, "113: _16 -> (MEM F32 _12)", null, null, null, 0, false, false, new int[]{51}, null);
    rulev[51] = new Rule(51, false, false, 27, "51: memq -> (MEM I64 addr)", null, ImList.list(ImList.list("mem","quad","$1")), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[104] = new Rule(104, false, true, 48, "104: _9 -> (MEM I64 _8)", null, null, null, 0, false, false, new int[]{47}, null);
    rulev[56] = new Rule(56, false, false, 32, "56: memd -> (MEM F64 addr)", null, ImList.list(ImList.list("mem","double","$1")), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[116] = new Rule(116, false, true, 57, "116: _18 -> (MEM F64 _8)", null, null, null, 0, false, false, new int[]{47}, null);
    rulev[91] = new Rule(91, false, false, 8, "91: void -> (SET I8 memb rcb)", ImList.list(ImList.list("movb","$2","$1")), null, null, 0, false, false, new int[]{30,35}, new String[]{null, null, null});
    rulev[95] = new Rule(95, false, false, 8, "95: void -> (SET I8 xregb regb)", ImList.list(ImList.list("movb","$2","$1")), null, null, 0, false, false, new int[]{9,5}, new String[]{null, null, "*reg-I8*"});
    rulev[90] = new Rule(90, false, false, 8, "90: void -> (SET I16 memw rcw)", ImList.list(ImList.list("movw","$2","$1")), null, null, 0, false, false, new int[]{29,34}, new String[]{null, null, null});
    rulev[94] = new Rule(94, false, false, 8, "94: void -> (SET I16 xregw regw)", ImList.list(ImList.list("movw","$2","$1")), null, null, 0, false, false, new int[]{10,4}, new String[]{null, null, "*reg-I16*"});
    rulev[89] = new Rule(89, false, false, 8, "89: void -> (SET I32 meml rcl)", ImList.list(ImList.list("movl","$2","$1")), null, null, 0, false, false, new int[]{28,33}, new String[]{null, null, null});
    rulev[93] = new Rule(93, false, false, 8, "93: void -> (SET I32 xregl regl)", ImList.list(ImList.list("movl","$2","$1")), null, null, 0, false, false, new int[]{11,2}, new String[]{null, null, "*reg-I32*"});
    rulev[102] = new Rule(102, false, false, 8, "102: void -> (SET I32 _6 _7)", ImList.list(ImList.list("call",ImList.list("symbol","_alloca"))), null, null, 0, false, false, new int[]{45,46}, new String[]{null, "*reg-eax-I32*"});
    rulev[106] = new Rule(106, false, true, 50, "106: _11 -> (SET I32 _6 _8)", null, null, null, 0, false, false, new int[]{45,47}, null);
    rulev[110] = new Rule(110, false, true, 53, "110: _14 -> (SET I32 _13 mrcl)", null, null, null, 0, false, false, new int[]{52,36}, null);
    rulev[111] = new Rule(111, false, true, 54, "111: _15 -> (SET I32 _6 _12)", null, null, null, 0, false, false, new int[]{45,51}, null);
    rulev[231] = new Rule(231, false, false, 8, "231: void -> (SET I32 meml _23)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{28,65}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[233] = new Rule(233, false, false, 8, "233: void -> (SET I32 memw _24)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistps","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{29,66}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[241] = new Rule(241, false, false, 8, "241: void -> (SET I32 meml _26)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{28,68}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[243] = new Rule(243, false, false, 8, "243: void -> (SET I32 memw _27)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistps","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{29,69}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[96] = new Rule(96, false, false, 8, "96: void -> (SET F32 xregf regf)", ImList.list(ImList.list("fmov","$2","$1")), null, null, 0, false, false, new int[]{13,6}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[99] = new Rule(99, false, false, 8, "99: void -> (SET F32 memf regf)", ImList.list(ImList.list("fstp","$2","$1")), null, null, 0, false, false, new int[]{31,6}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[114] = new Rule(114, false, true, 56, "114: _17 -> (SET F32 _16 memf)", null, null, null, 0, false, false, new int[]{55,31}, null);
    rulev[119] = new Rule(119, false, true, 59, "119: _20 -> (SET F32 _16 regf)", null, null, null, 0, false, false, new int[]{55,6}, null);
    rulev[88] = new Rule(88, false, false, 8, "88: void -> (SET I64 memq regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$2"),"$1"),ImList.list("movl",ImList.list("qhigh","$2"),ImList.list("after","$1","4"))), null, null, 0, false, false, new int[]{27,1}, new String[]{null, null, "*reg-I64*"});
    rulev[92] = new Rule(92, false, false, 8, "92: void -> (SET I64 xregq regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("movl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1"))), null, null, 0, false, false, new int[]{12,1}, new String[]{null, null, "*reg-I64*"});
    rulev[105] = new Rule(105, false, true, 49, "105: _10 -> (SET I64 _9 mrcq)", null, null, null, 0, false, false, new int[]{48,43}, null);
    rulev[229] = new Rule(229, false, false, 8, "229: void -> (SET I64 memq _22)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{27,64}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[239] = new Rule(239, false, false, 8, "239: void -> (SET I64 memq _25)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{27,67}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[97] = new Rule(97, false, false, 8, "97: void -> (SET F64 xregd regd)", ImList.list(ImList.list("fmov","$2","$1")), null, null, 0, false, false, new int[]{14,7}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[98] = new Rule(98, false, false, 8, "98: void -> (SET F64 memd regd)", ImList.list(ImList.list("fstp","$2","$1")), null, null, 0, false, false, new int[]{32,7}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[117] = new Rule(117, false, true, 58, "117: _19 -> (SET F64 _18 memd)", null, null, null, 0, false, false, new int[]{57,32}, null);
    rulev[121] = new Rule(121, false, true, 60, "121: _21 -> (SET F64 _18 regd)", null, null, null, 0, false, false, new int[]{57,7}, null);
    rulev[244] = new Rule(244, false, false, 8, "244: void -> (JUMP _ lab)", ImList.list(ImList.list("jmp","$1")), null, null, 0, false, false, new int[]{18}, new String[]{null, null});
    rulev[246] = new Rule(246, false, false, 8, "246: void -> (JUMPC _ _28 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jne","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("je","$3")), null, null, 0, false, false, new int[]{70,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[248] = new Rule(248, false, false, 8, "248: void -> (JUMPC _ _29 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jne","$3"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{71,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[250] = new Rule(250, false, false, 8, "250: void -> (JUMPC _ _30 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jl","$3"),ImList.list("jg","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{72,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[252] = new Rule(252, false, false, 8, "252: void -> (JUMPC _ _31 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jl","$3"),ImList.list("jg","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{73,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[254] = new Rule(254, false, false, 8, "254: void -> (JUMPC _ _32 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jg","$3"),ImList.list("jl","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{74,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[256] = new Rule(256, false, false, 8, "256: void -> (JUMPC _ _33 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jg","$3"),ImList.list("jl","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{75,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[258] = new Rule(258, false, false, 8, "258: void -> (JUMPC _ _34 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jb","$3"),ImList.list("ja","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{76,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[260] = new Rule(260, false, false, 8, "260: void -> (JUMPC _ _35 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jb","$3"),ImList.list("ja","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{77,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[262] = new Rule(262, false, false, 8, "262: void -> (JUMPC _ _36 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("ja","$3"),ImList.list("jb","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{78,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[264] = new Rule(264, false, false, 8, "264: void -> (JUMPC _ _37 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("ja","$3"),ImList.list("jb","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{79,18,18}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[266] = new Rule(266, false, false, 8, "266: void -> (JUMPC _ _38 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{80,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[268] = new Rule(268, false, false, 8, "268: void -> (JUMPC _ _39 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{81,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[270] = new Rule(270, false, false, 8, "270: void -> (JUMPC _ _40 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{82,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[272] = new Rule(272, false, false, 8, "272: void -> (JUMPC _ _41 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{83,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[274] = new Rule(274, false, false, 8, "274: void -> (JUMPC _ _42 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{84,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[276] = new Rule(276, false, false, 8, "276: void -> (JUMPC _ _43 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{85,18,18}, new String[]{null, "*reg-I32*", null, null, null});
  }
  static private void rrinit300() {
    rulev[278] = new Rule(278, false, false, 8, "278: void -> (JUMPC _ _44 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{86,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[280] = new Rule(280, false, false, 8, "280: void -> (JUMPC _ _45 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{87,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[282] = new Rule(282, false, false, 8, "282: void -> (JUMPC _ _46 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{88,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[284] = new Rule(284, false, false, 8, "284: void -> (JUMPC _ _47 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{89,18,18}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[286] = new Rule(286, false, false, 8, "286: void -> (JUMPC _ _48 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{90,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[288] = new Rule(288, false, false, 8, "288: void -> (JUMPC _ _49 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{91,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[290] = new Rule(290, false, false, 8, "290: void -> (JUMPC _ _50 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{92,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[292] = new Rule(292, false, false, 8, "292: void -> (JUMPC _ _51 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{93,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[294] = new Rule(294, false, false, 8, "294: void -> (JUMPC _ _52 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{94,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[296] = new Rule(296, false, false, 8, "296: void -> (JUMPC _ _53 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{95,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[298] = new Rule(298, false, false, 8, "298: void -> (JUMPC _ _54 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{96,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[300] = new Rule(300, false, false, 8, "300: void -> (JUMPC _ _55 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{97,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[302] = new Rule(302, false, false, 8, "302: void -> (JUMPC _ _56 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{98,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[304] = new Rule(304, false, false, 8, "304: void -> (JUMPC _ _57 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{99,18,18}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[306] = new Rule(306, false, false, 8, "306: void -> (JUMPC _ _58 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{100,18,18}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[308] = new Rule(308, false, false, 8, "308: void -> (JUMPC _ _59 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{101,18,18}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[310] = new Rule(310, false, false, 8, "310: void -> (JUMPC _ _60 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{102,18,18}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[312] = new Rule(312, false, false, 8, "312: void -> (JUMPC _ _61 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{103,18,18}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[314] = new Rule(314, false, false, 8, "314: void -> (JUMPC _ _62 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{104,18,18}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[316] = new Rule(316, false, false, 8, "316: void -> (JUMPC _ _63 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{105,18,18}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[318] = new Rule(318, false, false, 8, "318: void -> (JUMPC _ _64 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{106,18,18}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[320] = new Rule(320, false, false, 8, "320: void -> (JUMPC _ _65 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{107,18,18}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[322] = new Rule(322, false, false, 8, "322: void -> (JUMPC _ _66 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{108,18,18}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[324] = new Rule(324, false, false, 8, "324: void -> (JUMPC _ _67 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{109,18,18}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[326] = new Rule(326, false, false, 8, "326: void -> (JUMPC _ _68 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{110,18,18}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[328] = new Rule(328, false, false, 8, "328: void -> (JUMPC _ _69 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{111,18,18}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[330] = new Rule(330, false, false, 8, "330: void -> (JUMPC _ _70 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{112,18,18}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[332] = new Rule(332, false, false, 8, "332: void -> (JUMPC _ _71 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{113,18,18}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[334] = new Rule(334, false, false, 8, "334: void -> (JUMPC _ _72 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{114,18,18}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[336] = new Rule(336, false, false, 8, "336: void -> (JUMPC _ _73 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{115,18,18}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[337] = new Rule(337, false, false, 8, "337: void -> (CALL _ callarg)", ImList.list(ImList.list("call","$1")), null, null, 0, false, false, new int[]{44}, new String[]{null, null});
    rulev[107] = new Rule(107, false, false, 8, "107: void -> (PARALLEL _ _10 _11)", ImList.list(ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1"))), null, null, 0, false, false, new int[]{49,50}, new String[]{null, null});
    rulev[112] = new Rule(112, false, false, 8, "112: void -> (PARALLEL _ _14 _15)", ImList.list(ImList.list("pushl","$1")), null, null, 0, false, false, new int[]{53,54}, new String[]{null, null});
    rulev[115] = new Rule(115, false, false, 8, "115: void -> (PARALLEL _ _17 _15)", ImList.list(ImList.list("pushl","$1")), null, null, 0, false, false, new int[]{56,54}, new String[]{null, null});
    rulev[118] = new Rule(118, false, false, 8, "118: void -> (PARALLEL _ _19 _11)", ImList.list(ImList.list("pushl",ImList.list("after","$1","4")),ImList.list("pushl","$1")), null, null, 0, false, false, new int[]{58,50}, new String[]{null, null});
    rulev[120] = new Rule(120, false, false, 8, "120: void -> (PARALLEL _ _20 _15)", ImList.list(ImList.list("sub",ImList.list("imm","4"),"%esp"),ImList.list("fstp","$1",ImList.list("mem","float",ImList.list("addr",ImList.list("base",ImList.list(),"%esp"),ImList.list())))), null, null, 0, false, false, new int[]{59,54}, new String[]{null, "*reg-tmp-F32*"});
    rulev[122] = new Rule(122, false, false, 8, "122: void -> (PARALLEL _ _21 _11)", ImList.list(ImList.list("sub",ImList.list("imm","8"),"%esp"),ImList.list("fstp","$1",ImList.list("mem","double",ImList.list("addr",ImList.list("base",ImList.list(),"%esp"),ImList.list())))), null, null, 0, false, false, new int[]{60,50}, new String[]{null, "*reg-tmp-F64*"});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 1026: return "*reg-I64*";
    case 514: return "*reg-I32*";
    case 258: return "*reg-I16*";
    case 130: return "*reg-I8*";
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
    if (name == "reserve-tmpq")
      return jmac2();
    else if (name == "reserve-tmpl")
      return jmac3();
    else if (name == "reserve-cw0")
      return jmac4();
    else if (name == "reserve-cw1")
      return jmac5();
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
    if (name == "after")
      return jmac6(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "+")
      return jmac7(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-")
      return jmac8(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-32")
      return jmac9(emitObject(form.elem(1)));
    else if (name == "imm")
      return jmac10(emitObject(form.elem(1)));
    else if (name == "ind")
      return jmac11(emitObject(form.elem(1)));
    else if (name == "mem")
      return jmac12(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "addr")
      return jmac13(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "base")
      return jmac14(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "index")
      return jmac15(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regwlow")
      return jmac16(emitObject(form.elem(1)));
    else if (name == "regblow")
      return jmac17(emitObject(form.elem(1)));
    else if (name == "qlow")
      return jmac18(emitObject(form.elem(1)));
    else if (name == "qhigh")
      return jmac19(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac20(form.elem(1));
    else if (name == "epilogue")
      return jmac21(form.elem(1), emitObject(form.elem(2)));
    else if (name == "minus")
      return jmac22(emitObject(form.elem(1)));
    else if (name == "line")
      return jmac23(emitObject(form.elem(1)));
    else if (name == "symbol")
      return jmac24(emitObject(form.elem(1)));
    else if (name == "genasm")
      return jmac25(emitObject(form.elem(1)), form.elem(2));
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
  
  // private SillyPostProcessor pp;
  //
  // /** Run silly post processor after generating assembly code. **/
  // OutputStream insertPostProcessor(OutputStream out) {
  //    pp = SillyPostProcessor.postProcessor(out);
  //    return pp.pipeTo();
  // }
  // 
  // void notifyEndToPostProcessor() {
  //    pp.notifyEnd();
  // }
  
  ImList regCallClobbers = ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","I32","%ecx"),ImList.list("REG","I32","%edx"));
  
  
  /** X86's function attribute **/
  static class X86Attr extends FunctionAttr {
  
    /** Maximum stack space used by call. **/
    int stackRequired;
  
    /** Temporary variable used for int to float conversion **/
    int tmpOffset;
  
    /** alloca called in it **/
    boolean allocaCalled;
  
    X86Attr(Function func) {
      super(func);
      stackRequired = 0;
      tmpOffset = 0;
    }
  }
  
  FunctionAttr newFunctionAttr(Function func) {
    return new X86Attr(func);
  }
  
  
  /** Set alloca called. **/
  void setAllocaCalled() {
    X86Attr attr = (X86Attr)getFunctionAttr(func);
    attr.allocaCalled = true;
  }
  
  
  /** Return true if node is a conversion operation. **/
  private boolean isConv(LirNode node) {
    switch (node.opCode) {
    case Op.CONVSX:
    case Op.CONVZX:
    case Op.CONVIT:
    case Op.CONVFX:
    case Op.CONVFT:
    case Op.CONVFI:
    case Op.CONVFS:
    case Op.CONVFU:
    case Op.CONVSF:
    case Op.CONVUF:
      return true;
    default:
      return false;
    }
  }
  
  
  /** Return offset for va_start position. **/
  int makeVaStart(LirNode arg) {
    LirNode node = null;
    for (BiLink p = func.firstInstrList().first(); !p.atEnd(); p = p.next()) {
      node = (LirNode)p.elem();
      if (node.opCode == Op.PROLOGUE)
        break;
    }
    int n = node.nKids();
    int offset = 8;
    if (isConv(arg))
      arg = arg.kid(0);
    for (int i = 1; i < n; i++) {
      LirNode x = node.kid(i);
      offset += (Type.bytes(x.type) + 3) & -4;
      if (equalArg(arg, x))
        return offset;
    }
    /* error("va_start: bad argument") */
    return 8;
  }
  
  
  boolean equalArg(LirNode x, LirNode y) {
    if (x.opCode == Op.MEM)
      x = x.kid(0);
    if (y.opCode == Op.MEM)
      y = y.kid(0);
    return ((LirSymRef)x).symbol == ((LirSymRef)y).symbol;
  }
  
  
  LirNode stripConv(LirNode tree) {
    switch (tree.opCode) {
    case Op.CONVSX: case Op.CONVZX:
      return tree.kid(0);
    default:
      return tree;
    }
  }
  
  
  /** Rewrite FRAME node to target machine form. **/
  LirNode rewriteFrame(LirNode node) {
    Symbol ebp = func.getSymbol("%ebp");
    int off = ((SymAuto)((LirSymRef)node).symbol).offset();
    return lir.node
      (Op.ADD, node.type, lir.symRef(ebp), lir.iconst(I32, (long)off));
  }
  
  
  /** Return early time pre-rewriting sequence. **/
  public Transformer[] earlyRewritingSequence() {
    return new Transformer[] {
      localEarlyRewritingTrig
    };
  }
  
  
  /** Replace unresolved constants. (in alloca) **/
  /*
  final LocalTransformer replaceConstTrig = new LocalTransformer() {
      public boolean doIt(Function func, ImList args) {
        SymAuto sym = (SymAuto)func.getSymbol(".strretp");
        if (sym != null)
          sym.setOffset(8);
        return true;
      }
  
      public boolean doIt(Data data, ImList args) { return true; }
        
      public String name() { return "ReplaceConst"; }
  
      public String subject() { return "Replacing Constant value"; }
    };
  */
  
  
  
  /** Return late time pre-rewriting sequence. **/
  public Transformer[] lateRewritingSequence() {
    return new Transformer[] {
      AggregatePropagation.trig,
      localLateRewritingTrig,
      ProcessFramesTrig
    };
  }
  
  
  
  
  
  
  
  static final int MAXREGPARAM = 6;
  
  
  /** Rewrite PROLOGUE **/
  LirNode rewritePrologue(LirNode node, BiList post) {
    int location = 8;
    LirNode base = regnode(I32, "%ebp");
  
    if (func.origEpilogue.nKids() > 1
        && Type.tag(func.origEpilogue.kid(1).type) == Type.AGGREGATE) {
      // First parameter is a pointer to struct returning value.
      location += 4;
    }
  
    int n = node.nKids();
    for (int i = 1; i < n; i++) {
      LirNode arg = node.kid(i);
  
      if (arg.opCode == Op.MEM) {
        // set location to symbol table
        if (arg.kid(0).opCode != Op.FRAME)
          throw new CantHappenException("Malformed aggregate parameter");
        SymAuto var = (SymAuto)((LirSymRef)arg.kid(0)).symbol;
        var.setOffset(location);
      } else {
        post.add(lir.node(Op.SET, arg.type, arg,
                          stackMem(arg.type, location, base)));
      }
      location = ((location + Type.bytes(arg.type)) + 3) & -4;
    }
    if(root.spec.getCoinsOptions().isSet("gprof")){
      Symbol mcount = module.globalSymtab.addSymbol("mcount", 4, Type.UNKNOWN, 4, ".text", "XREF", ImList.Empty);
       post.add(lir.node(Op.CALL, Type.UNKNOWN, lir.symRef(Op.STATIC, I32, mcount, ImList.Empty), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0]), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0])));
  
      if(convention.equals("cygwin") && func.symbol.name.equals("main")) {
        Symbol monstartup = module.globalSymtab.addSymbol("_monstartup", 4, Type.UNKNOWN, 4, ".text", "XREF", ImList.Empty);
          post.add(lir.node(Op.CALL, Type.UNKNOWN, lir.symRef(Op.STATIC, I32, monstartup, ImList.Empty), lir.node(Op.LIST,   Type.UNKNOWN, new LirNode[0]), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0])));
       }
    }
    return lir.node(Op.PROLOGUE, Type.UNKNOWN, node.kid(0));
  }
  
  
  
  
  private LirNode regnode(int type, String name) {
    LirNode master = lir.symRef(module.globalSymtab.get(name));
    switch (Type.tag(type)) {
    case Type.INT:
      return master;
  
    case Type.FLOAT:
      if (type == F64)
        return master;
      else if (type == F32)
        return lir.node
          (Op.SUBREG, F32, master, lir.untaggedIconst(I32, 0));
  
    default:
      return null;
    }
  }
  
  
  private LirNode stackMem(int type, int location, LirNode base) {
    return lir.node
      (Op.MEM, type, lir.node
       (Op.ADD, I32, base,
        lir.iconst(I32, location + adjustEndian(type))));
  }
  
  private int adjustEndian(int type) { return 0; }
  
  
  private LirNode makePush(LirNode operand) {
    LirNode stackPtr = regnode(I32, "%esp");
    if (Type.tag(operand.type) == Type.FLOAT) {
      int size = Type.bytes(operand.type);
      return lir.node
        (Op.PARALLEL, Type.UNKNOWN, lir.node
         (Op.SET, operand.type, lir.node
          (Op.MEM, operand.type, lir.node
           (Op.SUB, I32, stackPtr, lir.iconst(I32, size))),
          operand), lir.node
         (Op.SET, I32, stackPtr, lir.node
          (Op.SUB, I32, stackPtr, lir.iconst(I32, size))));
    } else {
      if (Type.bits(operand.type) <= 32) {
        return lir.node
          (Op.PARALLEL, Type.UNKNOWN, lir.node
           (Op.SET, I32, lir.node
            (Op.MEM, I32, lir.node
             (Op.SUB, I32, stackPtr, lir.iconst(I32, 4))),
            operand), lir.node
           (Op.SET,
            I32, stackPtr, lir.node
            (Op.SUB, I32, stackPtr, lir.iconst(I32, 4))));
      } else {
        return lir.node
          (Op.PARALLEL, Type.UNKNOWN, lir.node
           (Op.SET, I64, lir.node
            (Op.MEM, I64, lir.node
             (Op.SUB, I32, stackPtr, lir.iconst(I32, 8))),
            operand), lir.node
           (Op.SET,
            I32, stackPtr, lir.node
            (Op.SUB, I32, stackPtr, lir.iconst(I32, 8))));
      }
    }
  }
  
  
  
  /** Return the register for value returned. **/
  LirNode returnReg(int type) {
    switch (Type.tag(type)) {
    case Type.INT:
      switch (Type.bytes(type)) {
      case 1: return regnode(type, "%al");
      case 2: return regnode(type, "%ax");
      case 4: return regnode(type, "%eax");
      case 8: return regnode(type, "%edxeax");
      default:
        return null;
      }
    case Type.FLOAT:
      if (!false)
        return regnode(type, "%t0");
      else
        return regnode(type, "%XMM0");
  
    default:
      return null;
    }
  }
  
  
  /** Rewrite EPILOGUE **/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
  
    if (node.nKids() < 2)
      return node;
  
    LirNode ret = node.kid(1);
    LirNode reg;
  
    switch (Type.tag(ret.type)) {
    case Type.INT:
    case Type.FLOAT:
      reg = returnReg(ret.type);
      pre.add(lir.node(Op.SET, ret.type, reg, ret));
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
  
    case Type.AGGREGATE:
      pre.add(lir.node
               (Op.SET, ret.type, lir.operator
                (Op.MEM, ret.type, stackMem(I32, 8, regnode(I32, "%ebp")),
                 ImList.list("&align", "4")),
  	      ret));
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, new LirNode[]{});
  
    default:
      throw new CantHappenException();
    }
  }
  
  
  
             
  /** Return true if node is a complex one.
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
  **/
  
  
  /** Rewrite CALL node. **/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
  
    LirNode args = node.kid(1);
    LirNode ret = null;
    if (node.kid(2).nKids() > 0)
      ret = node.kid(2).kid(0);
  
    // push arguments
    int loc = 0;
    int n = args.nKids();
    int paramCounter = 0;
    LirNode spreg = regnode(I32, "%esp");
    for (int i = n; --i >= 0; ) {
      LirNode arg = args.kid(i);
      switch (Type.tag(arg.type)) {
      case Type.INT:
        if (Type.bits(arg.type) < 32)
          arg = lir.node(Op.CONVZX, I32, arg);
        pre.add(makePush(arg));
        loc += Type.bytes(arg.type);
        break;
      case Type.FLOAT:
        pre.add(makePush(arg));
        loc += Type.bytes(arg.type);
        break;
      case Type.AGGREGATE:
        pre.add(lir.node
                 (Op.SET, I32, spreg, lir.node
                  (Op.SUB, I32, spreg, lir.iconst(I32, Type.bytes(arg.type)))));
        pre.add(lir.node
  	      (Op.SET, arg.type, lir.operator
                 (Op.MEM, arg.type, spreg, ImList.list("&align", "4")), arg));
        loc += (Type.bytes(arg.type) + 3) & -4;
        break;
      }
    }
  
    // value returned: in case of aggregate
    LirNode retReg = ret;
    if (ret != null) {
      switch (Type.tag(ret.type)) {
      case Type.INT:
      case Type.FLOAT:
        retReg = returnReg(ret.type);
        break;
      case Type.AGGREGATE:
        if (ret.opCode != Op.MEM)
          throw new CantHappenException();
        pre.add(makePush(ret.kid(0))); // push address of return value holder
        loc += 4;
        break;
      }
    }
  
    LirNode[] emptyVector = new LirNode[]{};
    try {
      LirNode retNode = lir.node(Op.LIST, Type.UNKNOWN, emptyVector);
      if (retReg != null)
        retNode = lir.node(Op.LIST, Type.UNKNOWN, retReg);
      node = lir.node
        (Op.PARALLEL, Type.UNKNOWN,
         noRescan
         (lir.node
          (Op.CALL, Type.UNKNOWN, node.kid(0),
           lir.node(Op.LIST, Type.UNKNOWN, emptyVector),
           retNode)),
          lir.decodeLir(new ImList("CLOBBER", regCallClobbers), func, module));
    } catch (SyntaxError e) {
      throw new CantHappenException();
    }
  
    // value returned
    if (ret != null) {
      switch (Type.tag(ret.type)) {
      case Type.INT:
      case Type.FLOAT:
        {
          if (isSimple(ret)) {
            post.add(lir.node(Op.SET, ret.type, ret, retReg));
          } else {
            LirNode tmp = func.newTemp(ret.type);
            post.add(lir.node(Op.SET, ret.type, tmp, retReg));
            post.add(lir.node(Op.SET, ret.type, ret, tmp));
          }
          break;
        }
      case Type.AGGREGATE:
        // no action needed
        break;
      }
    }
  
    // Restore stack pointer.
    if (loc != 0)
      post.add(lir.node
               (Op.SET, I32, spreg, lir.node
                (Op.ADD, I32, spreg, lir.iconst(I32, loc))));
  
    return node;
  }
  
  
  /** Postprocess list-form assembler source.
   ** @param list assembler source in list form. **/
  void peepHoleOpt(BiList list) {
    postRewriteFloatOps(list);
    if (root.traceOK("TMD", 1)) {
      debOut.println();
      debOut.println("After rewriting floatOps for function " + func.symbol.name + ":");
      for (BiLink p = list.first(); !p.atEnd(); p = p.next())
        debOut.println("  " + p.elem());
      debOut.println();
    }
  }
  
  
  /** Postrewrite floating point instructions. **/
  void postRewriteFloatOps(BiList list) {
    int counter = 1;
    int defAt[] = new int[9];
    int sp = 0;
  
    for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
      ImList inst = (ImList)p.elem();
  
      String op = (String)inst.elem();
      int dest = tregNum(inst.lastElem());
  
      int src = -1, src2 = -1;
  
      if (op == "fldz" || op == "fld1") {
        inst = ImList.list(op);
        sp++;
      }
      else if (op == "fldl" || op == "fildl" || op == "fildll"
               || op == "flds" || op == "filds") {
        if ((src = fregNum(inst.elem2nd())) >= 0)
          inst = ImList.list(op, "%st(" + (src + sp) + ")");
        else
          inst = ImList.list(op, inst.elem2nd());
        sp++;
      }
      else if (op == "fchs") {
        inst = ImList.list(op);
      }
      else if (op == "fadd" || op == "fmul") {
        if (tregNum(inst.elem2nd()) >= 0 && tregNum(inst.elem3rd()) >= 0) {
          // add/multiply top 2 elements and pop.
          inst = ImList.list((op + "p").intern(), "%st", "%st(1)");
          sp--;
        } else if ((src = fregNum(inst.elem2nd())) >= 0) {
          // add/multiply %fX register to stack top.
          inst = ImList.list(op, "%st(" + (src + sp) + ")", "%st");
        } else {
          // add/multiply contents of memory to stack top.
          inst = ImList.list((op + floatSizeSuffix(inst.elem2nd())).intern(),
                             inst.elem2nd());
        }
      }
      else if (op == "fsub" || op == "fdiv") {
        if ((src2 = tregNum(inst.elem2nd())) >= 0
            && (src = tregNum(inst.elem3rd())) >= 0) {
          // subtract/divide top 2 elements and pop.
          // CAUTION! GNU assembler generates wrong code for FSUB,FSUBR,
          // FDIV,FDIVR. Their mnemonics are interchanged.
          // So, if you are going to use another assembler,
          //  swap following "rp" and "p".
          if (defAt[src2] > defAt[src])
            inst = ImList.list((op + "rp").intern(), "%st", "%st(1)");
          else if (defAt[src2] < defAt[src])
            inst = ImList.list((op + "p").intern(), "%st", "%st(1)");
          else
            inst = ImList.list((op + "?").intern());
          sp--;
        } else if ((src = fregNum(inst.elem2nd())) >= 0) {
          // subtract/divide %fX register from stack top.
          inst = ImList.list(op, "%st(" + (src + sp) + ")", "%st");
        } else {
          // subtract/divide contents of memory from stack top.
          inst = ImList.list((op + floatSizeSuffix(inst.elem2nd())).intern(),
                             inst.elem2nd());
        }
      }
      else if (op == "fcom") {
        if ((src2 = tregNum(inst.elem2nd())) >= 0
            && (src = tregNum(inst.elem3rd())) >= 0) {
          // compare top 2 elements and pop.
          if (defAt[src2] > defAt[src])
            p.addBefore(ImList.list("fxch"));
          inst = ImList.list("fcompp");
          sp -= 2;
        } else if ((src = fregNum(inst.elem2nd())) >= 0) {
          // compare %fX register with stack top.
          inst = ImList.list("fcomp", "%st(" + (src + sp) + ")");
          sp--;
        } else {
          // compare contents of memory with stack top.
          inst = ImList.list(("fcomp" + floatSizeSuffix(inst.elem2nd())).intern(),
                             inst.elem2nd());
          sp--;
        }
      }
      else if (op == "fstp") {
        if ((src = fregNum(inst.elem3rd())) >= 0)
          inst = ImList.list(op, "%st(" + (src + sp) + ")");
        else
          inst = ImList.list((op + floatSizeSuffix(inst.elem3rd())).intern(),
                             inst.elem3rd());
        sp--;
      }
      else if (op == "fstps" || op == "fistpl" || op == "fistpll" || op == "fistps") {
        inst = ImList.list(op, inst.elem3rd());
        sp--;
      }
  
      if (dest >= 0)
        defAt[dest] = counter++;
  
      if (op == "fmov")
        p.unlink();
      else
        p.setElem(inst);
    }
  }
  
  
  /** Return nonnegative number if operand is "%tX" register.
   ** @param operand to be tested.
   ** @return register number, or -1 if operand is not a %t register. **/
  int tregNum(Object operand) {
    if (operand instanceof String && ((String)operand).startsWith("%t"))
      return Integer.parseInt(((String)operand).substring(2));
    else
      return -1;
  }
  
  /** Return nonnegative number if operand is "%fX" register.
   ** @param operand to be tested.
   ** @return register number, or -1 if operand is not a %t register. **/
  int fregNum(Object operand) {
    if (operand instanceof String && ((String)operand).startsWith("%f"))
      return Integer.parseInt(((String)operand).substring(2));
    else
      return -1;
  }
  
  
  /** Return floating point memory's size.
   ** @param arg memory operand list.
   ** @return "s" for float, "d" for double. **/
  String floatSizeSuffix(Object arg) {
    ImList mem = (ImList)arg;
    if (mem.elem() == "mem") {
      String size = (String)((ImList)mem).elem2nd();
      if (size == "float")
        return "s";
      else if (size == "double")
        return "l";
    }
    throw new CantHappenException();
  }
  
  
  
  /*
   * Code building macros.
   */
  
  
  /** Decode SUBREG node. **/
  Object jmac1(LirNode x) {
    Symbol reg = ((LirSymRef)x.kid(0)).symbol;
    int dtype = x.type;
    int offset = (int)((LirIconst)x.kid(1)).value;
    if (dtype == I16) {
      if (offset == 0)
        return "%" + reg.name.substring(2);
    } else if (dtype == I8) {
      int namel = reg.name.length();
      if (offset == 0)
        return "%" + reg.name.substring(namel - 2, namel - 1) + "l";
      else if (offset == 1)
        return "%" + reg.name.substring(namel - 2, namel - 1) + "h";
    } else if (dtype == F32 || (false && dtype == F64)) {
      if (offset == 0)
        return reg.name;
    }
    throw new CantHappenException();
  }
  
  Object jmac2() {
    SymAuto sym = reserveFrame(".TMPQ", I64);
    return sym.offset() + "(%ebp)";
  }
  
  Object jmac3() {
    SymAuto sym = reserveFrame(".TMPL", I32);
    return sym.offset() + "(%ebp)";
  }
  
  Object jmac4() {
    SymAuto sym = reserveFrame(".CONVCW0", I16);
    return sym.offset() + "(%ebp)";
  }
  
  Object jmac5() {
    SymAuto sym = reserveFrame(".CONVCW1", I16);
    return sym.offset() + "(%ebp)";
  }
  
  
  
  /* Code emission macros.
   *  Patterns not defined below will be converted to:
   *   (foo bar baz) --> foo   bar,baz   or foo(bar,baz)
   */
  
  String jmac6(String x, String y) {
    return emitAfter(x, y);
  }
  
  String emitAfter(String x, String y) {
    if (x.charAt(x.length() - 1) != ')')
      return x + "+" + y;
    else if (x.charAt(0) == '-' || x.charAt(0) == '(')
      return y + x;
    else
      return y + "+" + x;
  }
  
  String jmac7(String x, String y) {
    if (y.charAt(0) == '-')
      return x + y;
    else
      return x + "+" + y;
  }
  
  String jmac8(String x, String y) {
    if (y.charAt(0) == '-')
      return x + "+" + y.substring(1);
    else
      return x + "-" + y;
  }
  
  String jmac9(String x) {
    return "" + (Integer.parseInt(x) - 32);
  }
  
  String jmac10(String x) { return "$" + x; }
  
  String jmac11(String x) { return "*" + x; }
  
  String jmac12(String type, String x) { return x; }
  
  String jmac13(String base, String index) {
    if (index == "")
      return base;
    else if (base == "" || base.charAt(base.length() - 1) != ')')
      return base + "(," + index + ")";
    else
      return base.substring(0, base.length() - 1) + "," + index + ")";
  }
  
  String jmac14(String con, String reg) {
    if (reg == "")
      return con;
    else
      return con + "(" + reg + ")";
  }
  
  String jmac15(String reg, String scale) {
    if (scale == "1")
      return reg;
    else
      return reg + "," + scale;
  }
  
  /** Return lower half register name. **/
  String jmac16(String x) { return "%" + x.substring(2); }
  
  /** Return lowest byte register name. **/
  String jmac17(String x) {
    return "%" + x.substring(x.length() - 2, x.length() - 1) + "l";
  }
  
  
  /** Return lower 32bit of memory/register/constant operand. **/
  String jmac18(String x) {
    if (x.charAt(0) == '$')
      return "$" + (Long.parseLong(x.substring(1)) & 0xffffffffL);
    else if (x.charAt(0) == '%')
      return "%" + x.substring(x.length() - 3);
    else
      return x;
  }
  
  
  /** Return upper 32bit of memory/register/constant operand. **/
  String jmac19(String x) {
    if (x.charAt(0) == '$')
      return "$" + ((Long.parseLong(x.substring(1)) >> 32) & 0xffffffffL);
    else if (x.charAt(0) == '%')
      return x.substring(0, x.length() - 3);
    else
      return emitAfter(x, "4");
  }
  
  
  /** Generate prologue sequence. **/
  String jmac20(Object f) {
    Function func = (Function)f;
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    int size = frameSize(func);
    size = (size + 3) & -4; // round up to 4byte boundary
    String seq =
      "\tpushl\t%ebp\n"
      + "\tmovl\t%esp,%ebp";
    if (size != 0) {
      if (convention == "cygwin" && size > 4000) {
        seq += "\n\tmovl\t$" + size + ",%eax" +
               "\n\tcall\t__alloca";
      } else {
        seq += "\n\tsubl\t$" + size + ",%esp";
      }
    }
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      seq += "\n\tpushl\t" + machineParams.registerToString(reg);
    }
    /*    
    seq += "\n\tpushl\t%edi\n\tpushl\t%esi\n\tpushl\t%ebx";
    */
    return seq;
  }
  
  /** Generate epilogue sequence. **/
  String jmac21(Object f, String rettype) {
    Function func = (Function)f;
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    int size = frameSize(func);
    X86Attr attr = (X86Attr)getFunctionAttr(func);
    String pops = "";
    int n = 0;
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      pops = "\tpopl\t" + machineParams.registerToString(reg) + "\n" + pops;
      n += 4;
    }
    String seq = "";
    if (attr.allocaCalled && n != 0)
      seq = "\tlea\t-" + (size + n) + "(%ebp),%esp\n";
    return seq + pops + "\tleave\n\tret";
  }
  
  String jmac22(String con) {
    return -Integer.parseInt(con) + "";
  }
  
  
  String jmac23(String x) { return "# line " + x; }
  
  String jmac24(String x) { return makeAsmSymbol(x); }
  
  
  String jmac25(String format, Object args) { return emitAsmCode(format, (ImList)args); }
  
  
  /** Emit beginning of segment **/
  void emitBeginningOfSegment(PrintWriter out, String segment) {
    if (convention == "cygwin") {
      if (segment.equals(".text") || segment.equals(".rodata"))
        out.println("\t.text");
      else if (segment.equals(".data"))
        out.println("\t.data");
      else
        out.println("\t.section " + segment);
    } else
      out.println("\t.section " + segment);
  }
  
  
  /** Convert symbol to assembler form.
   **  Prepend "_" when cygwin (COFF), untouched otherwise (ELF). **/
  String makeAsmSymbol(String symbol) {
    if (convention == "cygwin" && symbol.charAt(0) != '.')
      return "_" + symbol;
    else
      return symbol;
  }
  
  public int alignForType(int type) {
    switch (Type.bytes(type)) {
    case 1: return 1;
    case 2: return 2;
    default: return 4;
    }
  }
  
  String segmentForConst() { return ".rodata"; }
  
  /** Emit data **/
  void emitData(PrintWriter out, int type, LirNode node) {
    if (type == I64) {
      long v = ((LirIconst)node).signedValue();
      out.println("\t.long\t" + (v & 0xffffffffL)
                  + "," + ((v >> 32) & 0xffffffffL));
    }
    else if (type == I32) {
      out.println("\t.long\t" + lexpConv.convert(node));
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
      out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16)
                  + ",0x" + Long.toString((bits >> 32) & 0xffffffffL, 16)
                  + " /* " + value + " */");
    }
    else if (type == F32) {
      double value = ((LirFconst)node).value;
      long bits = Float.floatToIntBits((float)value);
      out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16)
                  + " /* " + value + " */");
    }
    else {
      throw new CantHappenException("unknown type: " + type);
    }
  }
  
  
  /** Emit data common **/
  void emitCommon(PrintWriter out, SymStatic symbol, int bytes) {
    if (symbol.linkage == "LDEF")
      out.println("\t.lcomm\t" + makeAsmSymbol(symbol.name)
                  + "," + bytes);
    else {
      if (convention == "cygwin")
        out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes);
      else
        out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes + "," + symbol.boundary);
    }
  }
}
