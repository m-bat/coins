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
 13: xregfx -> (REG F32)
 14: xregfx -> (SUBREG F32)
 15: xregdx -> (REG F64)
 16: xregdx -> (SUBREG F64)
 17: regb -> xregb
 18: regw -> xregw
 19: regl -> xregl
 20: regq -> xregq
 21: regf -> xregf
 22: regd -> xregd
 23: regfx -> xregfx
 24: regdx -> xregdx
 25: con -> (INTCONST _)
 26: sta -> (STATIC I32)
 27: asmcon -> con
 28: asmcon -> sta
 29: asmcon -> (ADD I32 asmcon con)
 30: asmcon -> (SUB I32 asmcon con)
 31: lab -> (LABEL I32)
 32: base -> asmcon
 33: base -> regl
 34: base -> (ADD I32 regl asmcon)
 35: base -> (ADD I32 asmcon regl)
 36: base -> (SUB I32 regl con)
 37: index -> regl
 38: _1 -> (INTCONST I32 2)
 39: index -> (MUL I32 regl _1)
 40: _2 -> (INTCONST I32 4)
 41: index -> (MUL I32 regl _2)
 42: _3 -> (INTCONST I32 8)
 43: index -> (MUL I32 regl _3)
 44: _4 -> (INTCONST I32 1)
 45: index -> (LSHS I32 regl _4)
 46: index -> (LSHS I32 regl _1)
 47: _5 -> (INTCONST I32 3)
 48: index -> (LSHS I32 regl _5)
 49: addr -> base
 50: addr -> index
 51: addr -> (ADD I32 base index)
 52: addr -> (ADD I32 index base)
 53: memq -> (MEM I64 addr)
 54: meml -> (MEM I32 addr)
 55: memw -> (MEM I16 addr)
 56: memb -> (MEM I8 addr)
 57: memf -> (MEM F32 addr)
 58: memd -> (MEM F64 addr)
 59: rcl -> regl
 60: rcl -> asmcon
 61: rcw -> regw
 62: rcw -> con
 63: rcb -> regb
 64: rcb -> con
 65: mrcl -> meml
 66: mrcl -> rcl
 67: mregl -> meml
 68: mregl -> regl
 69: mrcw -> memw
 70: mrcw -> rcw
 71: mregw -> memw
 72: mregw -> regw
 73: mrcb -> memb
 74: mrcb -> rcb
 75: mregb -> memb
 76: mregb -> regb
 77: mregq -> memq
 78: mregq -> regq
 79: mrcq -> mregq
 80: mrcq -> con
 81: callarg -> sta
 82: callarg -> regl
 83: regl -> addr
 84: regq -> mrcq
 85: regl -> mrcl
 86: regw -> mrcw
 87: regb -> mrcb
 88: regd -> regdx
 89: regf -> regfx
 90: void -> (SET F64 regd regdx)
 91: void -> (SET F32 regf regfx)
 92: regdx -> memd
 93: regfx -> memf
 94: void -> (SET I64 memq regq)
 95: void -> (SET I32 meml rcl)
 96: void -> (SET I16 memw rcw)
 97: void -> (SET I8 memb rcb)
 98: void -> (SET I64 xregq regq)
 99: void -> (SET I32 xregl regl)
 100: void -> (SET I16 xregw regw)
 101: void -> (SET I8 xregb regb)
 102: void -> (SET F64 xregdx regdx)
 103: void -> (SET F32 xregfx regfx)
 104: void -> (SET F64 xregdx regd)
 105: void -> (SET F32 xregfx regf)
 106: void -> (SET F32 xregf regf)
 107: void -> (SET F64 xregd regd)
 108: void -> (SET F64 memd regdx)
 109: void -> (SET F32 memf regfx)
 110: void -> (SET F64 memd regd)
 111: void -> (SET F32 memf regf)
 112: _6 -> (REG I32 "%esp")
 113: _7 -> (SUB I32 _6 regl)
 114: void -> (SET I32 _6 _7)
 115: _8 -> (SUB I32 _6 _3)
 116: _9 -> (MEM I64 _8)
 117: _10 -> (SET I64 _9 mrcq)
 118: _11 -> (SET I32 _6 _8)
 119: void -> (PARALLEL _ _10 _11)
 120: _12 -> (SUB I32 _6 _2)
 121: _13 -> (MEM I32 _12)
 122: _14 -> (SET I32 _13 mrcl)
 123: _15 -> (SET I32 _6 _12)
 124: void -> (PARALLEL _ _14 _15)
 125: _16 -> (MEM I32 _6)
 126: _17 -> (SET I32 _16 regl)
 127: void -> (PARALLEL _ _17 _15)
 128: _18 -> (SET I32 regl _16)
 129: _19 -> (ADD I32 _6 _2)
 130: _20 -> (SET I32 _6 _19)
 131: void -> (PARALLEL _ _18 _20)
 132: _21 -> (MEM F32 _12)
 133: _22 -> (SET F32 _21 memf)
 134: void -> (PARALLEL _ _22 _15)
 135: _23 -> (MEM F64 _8)
 136: _24 -> (SET F64 _23 memd)
 137: void -> (PARALLEL _ _24 _11)
 138: _25 -> (SET F32 _21 regfx)
 139: void -> (PARALLEL _ _25 _15)
 140: _26 -> (SET F64 _23 regdx)
 141: void -> (PARALLEL _ _26 _11)
 142: _27 -> (SET F32 _21 regf)
 143: void -> (PARALLEL _ _27 _15)
 144: _28 -> (SET F64 _23 regd)
 145: void -> (PARALLEL _ _28 _11)
 146: regq -> (ADD I64 regq mrcq)
 147: regq -> (SUB I64 regq mrcq)
 148: regq -> (BAND I64 regq mrcq)
 149: regq -> (BOR I64 regq mrcq)
 150: regq -> (BXOR I64 regq mrcq)
 151: regq -> (NEG I64 regq)
 152: regq -> (BNOT I64 regq)
 153: regq -> (LSHS I64 regq con)
 154: regq -> (LSHS I64 regq con)
 155: regq -> (LSHS I64 regq con)
 156: regq -> (LSHS I64 regq shfct)
 157: regq -> (RSHS I64 regq con)
 158: regq -> (RSHS I64 regq con)
 159: regq -> (RSHS I64 regq con)
 160: regq -> (RSHS I64 regq shfct)
 161: regq -> (RSHU I64 regq con)
 162: regq -> (RSHU I64 regq con)
 163: regq -> (RSHU I64 regq con)
 164: regq -> (RSHU I64 regq shfct)
 165: shfct -> memq
 166: shfct -> regq
 167: shfct -> regl
 168: shfct -> regw
 169: shfct -> regb
 170: regq -> (MUL I64 regq regq)
 171: regq -> (DIVS I64 mrcq mrcq)
 172: regq -> (DIVU I64 mrcq mrcq)
 173: regq -> (MODS I64 mrcq mrcq)
 174: regq -> (MODU I64 mrcq mrcq)
 175: regl -> (ADD I32 regl mrcl)
 176: regl -> (SUB I32 regl mrcl)
 177: regl -> (BAND I32 regl mrcl)
 178: regl -> (BOR I32 regl mrcl)
 179: regl -> (BXOR I32 regl mrcl)
 180: regl -> (ADD I32 mrcl regl)
 181: regl -> (BAND I32 mrcl regl)
 182: regl -> (BOR I32 mrcl regl)
 183: regl -> (BXOR I32 mrcl regl)
 184: regl -> (NEG I32 regl)
 185: regl -> (BNOT I32 regl)
 186: regl -> (LSHS I32 regl con)
 187: regl -> (RSHS I32 regl con)
 188: regl -> (RSHU I32 regl con)
 189: regl -> (LSHS I32 regl shfct)
 190: regl -> (RSHS I32 regl shfct)
 191: regl -> (RSHU I32 regl shfct)
 192: regl -> (MUL I32 regl mrcl)
 193: regl -> (MUL I32 mrcl regl)
 194: regl -> (DIVS I32 regl regl)
 195: regl -> (DIVU I32 regl regl)
 196: regl -> (MODS I32 regl regl)
 197: regl -> (MODU I32 regl regl)
 198: regmemdx -> regdx
 199: regmemdx -> memd
 200: regmemfx -> regfx
 201: regmemfx -> memf
 202: regmemd -> regd
 203: regmemd -> memd
 204: regmemf -> regf
 205: regmemf -> memf
 206: regdx -> (ADD F64 regdx regmemdx)
 207: regdx -> (SUB F64 regdx regmemdx)
 208: regdx -> (MUL F64 regdx regmemdx)
 209: regdx -> (DIVS F64 regdx regmemdx)
 210: regfx -> (ADD F32 regfx regmemfx)
 211: regfx -> (SUB F32 regfx regmemfx)
 212: regfx -> (MUL F32 regfx regmemfx)
 213: regfx -> (DIVS F32 regfx regmemfx)
 214: regdx -> (NEG F64 regdx)
 215: regfx -> (NEG F32 regfx)
 216: regd -> (ADD F64 regd regmemd)
 217: regd -> (SUB F64 regd regmemd)
 218: regd -> (MUL F64 regd regmemd)
 219: regd -> (DIVS F64 regd regmemd)
 220: regd -> (NEG F64 regd)
 221: regf -> (ADD F32 regf regmemf)
 222: regf -> (SUB F32 regf regmemf)
 223: regf -> (MUL F32 regf regmemf)
 224: regf -> (DIVS F32 regf regmemf)
 225: regf -> (NEG F32 regf)
 226: reglb -> regl
 227: reglb -> regb
 228: regq -> (CONVSX I64 regl)
 229: regq -> (CONVSX I64 regw)
 230: regq -> (CONVSX I64 regb)
 231: regl -> (CONVSX I32 mregw)
 232: regl -> (CONVSX I32 mregb)
 233: regw -> (CONVSX I16 mregb)
 234: regq -> (CONVZX I64 mregl)
 235: regq -> (CONVZX I64 mregw)
 236: regq -> (CONVZX I64 mregb)
 237: regq -> (CONVZX I64 mregb)
 238: regl -> (CONVZX I32 mregw)
 239: regl -> (CONVZX I32 mregb)
 240: regw -> (CONVZX I16 mregb)
 241: regl -> (CONVIT I32 regq)
 242: regw -> (CONVIT I16 regq)
 243: regb -> (CONVIT I8 regq)
 244: regw -> (CONVIT I16 regl)
 245: regb -> (CONVIT I8 regl)
 246: regb -> (CONVIT I8 regw)
 247: regdx -> (CONVSF F64 meml)
 248: regdx -> (CONVSF F64 reglb)
 249: regdx -> (CONVSF F64 memw)
 250: regfx -> (CONVSF F32 meml)
 251: regfx -> (CONVSF F32 reglb)
 252: regfx -> (CONVSF F32 memw)
 253: regdx -> (CONVFX F64 regfx)
 254: regfx -> (CONVFT F32 regdx)
 255: regd -> (CONVSF F64 memq)
 256: regd -> (CONVSF F64 regq)
 257: regd -> (CONVSF F64 meml)
 258: regd -> (CONVSF F64 reglb)
 259: regd -> (CONVSF F64 memw)
 260: regd -> (CONVSF F64 regw)
 261: regf -> (CONVSF F32 memq)
 262: regf -> (CONVSF F32 regq)
 263: regf -> (CONVSF F32 meml)
 264: regf -> (CONVSF F32 reglb)
 265: regf -> (CONVSF F32 memw)
 266: regf -> (CONVSF F32 regw)
 267: regd -> (CONVFX F64 regf)
 268: regf -> (CONVFT F32 regd)
 269: regb -> (CONVFS I8 regfx)
 270: regb -> (CONVFS I8 regdx)
 271: regw -> (CONVFS I16 regfx)
 272: regw -> (CONVFS I16 regdx)
 273: regl -> (CONVFS I32 regfx)
 274: regl -> (CONVFS I32 regdx)
 275: regq -> (CONVFS I64 regd)
 276: regl -> (CONVFS I32 regd)
 277: regw -> (CONVFS I16 regd)
 278: regb -> (CONVFS I8 regd)
 279: _29 -> (CONVFS I64 regd)
 280: void -> (SET I64 memq _29)
 281: _30 -> (CONVFS I32 regd)
 282: void -> (SET I32 meml _30)
 283: _31 -> (CONVFS I16 regd)
 284: void -> (SET I32 memw _31)
 285: regq -> (CONVFS I64 regf)
 286: regl -> (CONVFS I32 regf)
 287: regw -> (CONVFS I16 regf)
 288: regb -> (CONVFS I8 regf)
 289: _32 -> (CONVFS I64 regf)
 290: void -> (SET I64 memq _32)
 291: _33 -> (CONVFS I32 regf)
 292: void -> (SET I32 meml _33)
 293: _34 -> (CONVFS I16 regf)
 294: void -> (SET I32 memw _34)
 295: void -> (JUMP _ lab)
 296: _35 -> (TSTEQ I32 regq mrcq)
 297: void -> (JUMPC _ _35 lab lab)
 298: _36 -> (TSTNE I32 regq mrcq)
 299: void -> (JUMPC _ _36 lab lab)
 300: _37 -> (TSTLTS I32 regq mrcq)
 301: void -> (JUMPC _ _37 lab lab)
 302: _38 -> (TSTLES I32 regq mrcq)
 303: void -> (JUMPC _ _38 lab lab)
 304: _39 -> (TSTGTS I32 regq mrcq)
 305: void -> (JUMPC _ _39 lab lab)
 306: _40 -> (TSTGES I32 regq mrcq)
 307: void -> (JUMPC _ _40 lab lab)
 308: _41 -> (TSTLTU I32 regq mrcq)
 309: void -> (JUMPC _ _41 lab lab)
 310: _42 -> (TSTLEU I32 regq mrcq)
 311: void -> (JUMPC _ _42 lab lab)
 312: _43 -> (TSTGTU I32 regq mrcq)
 313: void -> (JUMPC _ _43 lab lab)
 314: _44 -> (TSTGEU I32 regq mrcq)
 315: void -> (JUMPC _ _44 lab lab)
 316: _45 -> (TSTEQ I32 regl mrcl)
 317: void -> (JUMPC _ _45 lab lab)
 318: _46 -> (TSTNE I32 regl mrcl)
 319: void -> (JUMPC _ _46 lab lab)
 320: _47 -> (TSTLTS I32 regl mrcl)
 321: void -> (JUMPC _ _47 lab lab)
 322: _48 -> (TSTLES I32 regl mrcl)
 323: void -> (JUMPC _ _48 lab lab)
 324: _49 -> (TSTGTS I32 regl mrcl)
 325: void -> (JUMPC _ _49 lab lab)
 326: _50 -> (TSTGES I32 regl mrcl)
 327: void -> (JUMPC _ _50 lab lab)
 328: _51 -> (TSTLTU I32 regl mrcl)
 329: void -> (JUMPC _ _51 lab lab)
 330: _52 -> (TSTLEU I32 regl mrcl)
 331: void -> (JUMPC _ _52 lab lab)
 332: _53 -> (TSTGTU I32 regl mrcl)
 333: void -> (JUMPC _ _53 lab lab)
 334: _54 -> (TSTGEU I32 regl mrcl)
 335: void -> (JUMPC _ _54 lab lab)
 336: _55 -> (TSTEQ I32 mrcl regl)
 337: void -> (JUMPC _ _55 lab lab)
 338: _56 -> (TSTNE I32 mrcl regl)
 339: void -> (JUMPC _ _56 lab lab)
 340: _57 -> (TSTLTS I32 mrcl regl)
 341: void -> (JUMPC _ _57 lab lab)
 342: _58 -> (TSTLES I32 mrcl regl)
 343: void -> (JUMPC _ _58 lab lab)
 344: _59 -> (TSTGTS I32 mrcl regl)
 345: void -> (JUMPC _ _59 lab lab)
 346: _60 -> (TSTGES I32 mrcl regl)
 347: void -> (JUMPC _ _60 lab lab)
 348: _61 -> (TSTLTU I32 mrcl regl)
 349: void -> (JUMPC _ _61 lab lab)
 350: _62 -> (TSTLEU I32 mrcl regl)
 351: void -> (JUMPC _ _62 lab lab)
 352: _63 -> (TSTGTU I32 mrcl regl)
 353: void -> (JUMPC _ _63 lab lab)
 354: _64 -> (TSTGEU I32 mrcl regl)
 355: void -> (JUMPC _ _64 lab lab)
 356: _65 -> (TSTEQ I32 regb memb)
 357: void -> (JUMPC _ _65 lab lab)
 358: _66 -> (TSTNE I32 regb memb)
 359: void -> (JUMPC _ _66 lab lab)
 360: _67 -> (TSTEQ I32 regb rcb)
 361: void -> (JUMPC _ _67 lab lab)
 362: _68 -> (TSTNE I32 regb rcb)
 363: void -> (JUMPC _ _68 lab lab)
 364: _69 -> (TSTEQ I32 memb regb)
 365: void -> (JUMPC _ _69 lab lab)
 366: _70 -> (TSTNE I32 memb regb)
 367: void -> (JUMPC _ _70 lab lab)
 368: _71 -> (TSTEQ I32 rcb regb)
 369: void -> (JUMPC _ _71 lab lab)
 370: _72 -> (TSTNE I32 rcb regb)
 371: void -> (JUMPC _ _72 lab lab)
 372: _73 -> (TSTEQ I32 regdx regmemdx)
 373: void -> (JUMPC _ _73 lab lab)
 374: _74 -> (TSTNE I32 regdx regmemdx)
 375: void -> (JUMPC _ _74 lab lab)
 376: _75 -> (TSTLTS I32 regdx regmemdx)
 377: void -> (JUMPC _ _75 lab lab)
 378: _76 -> (TSTLES I32 regdx regmemdx)
 379: void -> (JUMPC _ _76 lab lab)
 380: _77 -> (TSTEQ I32 regfx regmemfx)
 381: void -> (JUMPC _ _77 lab lab)
 382: _78 -> (TSTNE I32 regfx regmemfx)
 383: void -> (JUMPC _ _78 lab lab)
 384: _79 -> (TSTLTS I32 regfx regmemfx)
 385: void -> (JUMPC _ _79 lab lab)
 386: _80 -> (TSTLES I32 regfx regmemfx)
 387: void -> (JUMPC _ _80 lab lab)
 388: _81 -> (TSTGTS I32 regdx regmemdx)
 389: void -> (JUMPC _ _81 lab lab)
 390: _82 -> (TSTGES I32 regdx regmemdx)
 391: void -> (JUMPC _ _82 lab lab)
 392: _83 -> (TSTGTS I32 regfx regmemfx)
 393: void -> (JUMPC _ _83 lab lab)
 394: _84 -> (TSTGES I32 regfx regmemfx)
 395: void -> (JUMPC _ _84 lab lab)
 396: _85 -> (TSTEQ I32 regd regmemd)
 397: void -> (JUMPC _ _85 lab lab)
 398: _86 -> (TSTNE I32 regd regmemd)
 399: void -> (JUMPC _ _86 lab lab)
 400: _87 -> (TSTEQ I32 regf regmemf)
 401: void -> (JUMPC _ _87 lab lab)
 402: _88 -> (TSTNE I32 regf regmemf)
 403: void -> (JUMPC _ _88 lab lab)
 404: _89 -> (TSTLTS I32 regd regmemd)
 405: void -> (JUMPC _ _89 lab lab)
 406: _90 -> (TSTGES I32 regd regmemd)
 407: void -> (JUMPC _ _90 lab lab)
 408: _91 -> (TSTLTS I32 regf regmemf)
 409: void -> (JUMPC _ _91 lab lab)
 410: _92 -> (TSTGES I32 regf regmemf)
 411: void -> (JUMPC _ _92 lab lab)
 412: _93 -> (TSTGTS I32 regd regmemd)
 413: void -> (JUMPC _ _93 lab lab)
 414: _94 -> (TSTLES I32 regd regmemd)
 415: void -> (JUMPC _ _94 lab lab)
 416: _95 -> (TSTGTS I32 regf regmemf)
 417: void -> (JUMPC _ _95 lab lab)
 418: _96 -> (TSTLES I32 regf regmemf)
 419: void -> (JUMPC _ _96 lab lab)
 420: void -> (CALL _ callarg)
*/
/*
Sorted Productions:
 78: mregq -> regq
 166: shfct -> regq
 33: base -> regl
 37: index -> regl
 59: rcl -> regl
 68: mregl -> regl
 82: callarg -> regl
 167: shfct -> regl
 226: reglb -> regl
 61: rcw -> regw
 72: mregw -> regw
 168: shfct -> regw
 63: rcb -> regb
 76: mregb -> regb
 169: shfct -> regb
 227: reglb -> regb
 89: regf -> regfx
 200: regmemfx -> regfx
 88: regd -> regdx
 198: regmemdx -> regdx
 204: regmemf -> regf
 202: regmemd -> regd
 17: regb -> xregb
 18: regw -> xregw
 19: regl -> xregl
 20: regq -> xregq
 21: regf -> xregf
 22: regd -> xregd
 23: regfx -> xregfx
 24: regdx -> xregdx
 27: asmcon -> con
 62: rcw -> con
 64: rcb -> con
 80: mrcq -> con
 28: asmcon -> sta
 81: callarg -> sta
 32: base -> asmcon
 60: rcl -> asmcon
 49: addr -> base
 50: addr -> index
 83: regl -> addr
 77: mregq -> memq
 165: shfct -> memq
 65: mrcl -> meml
 67: mregl -> meml
 69: mrcw -> memw
 71: mregw -> memw
 73: mrcb -> memb
 75: mregb -> memb
 93: regfx -> memf
 201: regmemfx -> memf
 205: regmemf -> memf
 92: regdx -> memd
 199: regmemdx -> memd
 203: regmemd -> memd
 66: mrcl -> rcl
 70: mrcw -> rcw
 74: mrcb -> rcb
 85: regl -> mrcl
 86: regw -> mrcw
 87: regb -> mrcb
 79: mrcq -> mregq
 84: regq -> mrcq
 25: con -> (INTCONST _)
 38: _1 -> (INTCONST I32 2)
 40: _2 -> (INTCONST I32 4)
 42: _3 -> (INTCONST I32 8)
 44: _4 -> (INTCONST I32 1)
 47: _5 -> (INTCONST I32 3)
 26: sta -> (STATIC I32)
 1: xregb -> (REG I8)
 3: xregw -> (REG I16)
 5: xregl -> (REG I32)
 112: _6 -> (REG I32 "%esp")
 9: xregf -> (REG F32)
 13: xregfx -> (REG F32)
 7: xregq -> (REG I64)
 11: xregd -> (REG F64)
 15: xregdx -> (REG F64)
 2: xregb -> (SUBREG I8)
 4: xregw -> (SUBREG I16)
 6: xregl -> (SUBREG I32)
 10: xregf -> (SUBREG F32)
 14: xregfx -> (SUBREG F32)
 8: xregq -> (SUBREG I64)
 12: xregd -> (SUBREG F64)
 16: xregdx -> (SUBREG F64)
 31: lab -> (LABEL I32)
 184: regl -> (NEG I32 regl)
 215: regfx -> (NEG F32 regfx)
 225: regf -> (NEG F32 regf)
 151: regq -> (NEG I64 regq)
 214: regdx -> (NEG F64 regdx)
 220: regd -> (NEG F64 regd)
 29: asmcon -> (ADD I32 asmcon con)
 34: base -> (ADD I32 regl asmcon)
 35: base -> (ADD I32 asmcon regl)
 51: addr -> (ADD I32 base index)
 52: addr -> (ADD I32 index base)
 129: _19 -> (ADD I32 _6 _2)
 175: regl -> (ADD I32 regl mrcl)
 180: regl -> (ADD I32 mrcl regl)
 210: regfx -> (ADD F32 regfx regmemfx)
 221: regf -> (ADD F32 regf regmemf)
 146: regq -> (ADD I64 regq mrcq)
 206: regdx -> (ADD F64 regdx regmemdx)
 216: regd -> (ADD F64 regd regmemd)
 30: asmcon -> (SUB I32 asmcon con)
 36: base -> (SUB I32 regl con)
 113: _7 -> (SUB I32 _6 regl)
 115: _8 -> (SUB I32 _6 _3)
 120: _12 -> (SUB I32 _6 _2)
 176: regl -> (SUB I32 regl mrcl)
 211: regfx -> (SUB F32 regfx regmemfx)
 222: regf -> (SUB F32 regf regmemf)
 147: regq -> (SUB I64 regq mrcq)
 207: regdx -> (SUB F64 regdx regmemdx)
 217: regd -> (SUB F64 regd regmemd)
 39: index -> (MUL I32 regl _1)
 41: index -> (MUL I32 regl _2)
 43: index -> (MUL I32 regl _3)
 192: regl -> (MUL I32 regl mrcl)
 193: regl -> (MUL I32 mrcl regl)
 212: regfx -> (MUL F32 regfx regmemfx)
 223: regf -> (MUL F32 regf regmemf)
 170: regq -> (MUL I64 regq regq)
 208: regdx -> (MUL F64 regdx regmemdx)
 218: regd -> (MUL F64 regd regmemd)
 194: regl -> (DIVS I32 regl regl)
 213: regfx -> (DIVS F32 regfx regmemfx)
 224: regf -> (DIVS F32 regf regmemf)
 171: regq -> (DIVS I64 mrcq mrcq)
 209: regdx -> (DIVS F64 regdx regmemdx)
 219: regd -> (DIVS F64 regd regmemd)
 195: regl -> (DIVU I32 regl regl)
 172: regq -> (DIVU I64 mrcq mrcq)
 196: regl -> (MODS I32 regl regl)
 173: regq -> (MODS I64 mrcq mrcq)
 197: regl -> (MODU I32 regl regl)
 174: regq -> (MODU I64 mrcq mrcq)
 233: regw -> (CONVSX I16 mregb)
 231: regl -> (CONVSX I32 mregw)
 232: regl -> (CONVSX I32 mregb)
 228: regq -> (CONVSX I64 regl)
 229: regq -> (CONVSX I64 regw)
 230: regq -> (CONVSX I64 regb)
 240: regw -> (CONVZX I16 mregb)
 238: regl -> (CONVZX I32 mregw)
 239: regl -> (CONVZX I32 mregb)
 234: regq -> (CONVZX I64 mregl)
 235: regq -> (CONVZX I64 mregw)
 236: regq -> (CONVZX I64 mregb)
 237: regq -> (CONVZX I64 mregb)
 243: regb -> (CONVIT I8 regq)
 245: regb -> (CONVIT I8 regl)
 246: regb -> (CONVIT I8 regw)
 242: regw -> (CONVIT I16 regq)
 244: regw -> (CONVIT I16 regl)
 241: regl -> (CONVIT I32 regq)
 253: regdx -> (CONVFX F64 regfx)
 267: regd -> (CONVFX F64 regf)
 254: regfx -> (CONVFT F32 regdx)
 268: regf -> (CONVFT F32 regd)
 269: regb -> (CONVFS I8 regfx)
 270: regb -> (CONVFS I8 regdx)
 278: regb -> (CONVFS I8 regd)
 288: regb -> (CONVFS I8 regf)
 271: regw -> (CONVFS I16 regfx)
 272: regw -> (CONVFS I16 regdx)
 277: regw -> (CONVFS I16 regd)
 283: _31 -> (CONVFS I16 regd)
 287: regw -> (CONVFS I16 regf)
 293: _34 -> (CONVFS I16 regf)
 273: regl -> (CONVFS I32 regfx)
 274: regl -> (CONVFS I32 regdx)
 276: regl -> (CONVFS I32 regd)
 281: _30 -> (CONVFS I32 regd)
 286: regl -> (CONVFS I32 regf)
 291: _33 -> (CONVFS I32 regf)
 275: regq -> (CONVFS I64 regd)
 279: _29 -> (CONVFS I64 regd)
 285: regq -> (CONVFS I64 regf)
 289: _32 -> (CONVFS I64 regf)
 250: regfx -> (CONVSF F32 meml)
 251: regfx -> (CONVSF F32 reglb)
 252: regfx -> (CONVSF F32 memw)
 261: regf -> (CONVSF F32 memq)
 262: regf -> (CONVSF F32 regq)
 263: regf -> (CONVSF F32 meml)
 264: regf -> (CONVSF F32 reglb)
 265: regf -> (CONVSF F32 memw)
 266: regf -> (CONVSF F32 regw)
 247: regdx -> (CONVSF F64 meml)
 248: regdx -> (CONVSF F64 reglb)
 249: regdx -> (CONVSF F64 memw)
 255: regd -> (CONVSF F64 memq)
 256: regd -> (CONVSF F64 regq)
 257: regd -> (CONVSF F64 meml)
 258: regd -> (CONVSF F64 reglb)
 259: regd -> (CONVSF F64 memw)
 260: regd -> (CONVSF F64 regw)
 177: regl -> (BAND I32 regl mrcl)
 181: regl -> (BAND I32 mrcl regl)
 148: regq -> (BAND I64 regq mrcq)
 178: regl -> (BOR I32 regl mrcl)
 182: regl -> (BOR I32 mrcl regl)
 149: regq -> (BOR I64 regq mrcq)
 179: regl -> (BXOR I32 regl mrcl)
 183: regl -> (BXOR I32 mrcl regl)
 150: regq -> (BXOR I64 regq mrcq)
 185: regl -> (BNOT I32 regl)
 152: regq -> (BNOT I64 regq)
 45: index -> (LSHS I32 regl _4)
 46: index -> (LSHS I32 regl _1)
 48: index -> (LSHS I32 regl _5)
 186: regl -> (LSHS I32 regl con)
 189: regl -> (LSHS I32 regl shfct)
 153: regq -> (LSHS I64 regq con)
 154: regq -> (LSHS I64 regq con)
 155: regq -> (LSHS I64 regq con)
 156: regq -> (LSHS I64 regq shfct)
 187: regl -> (RSHS I32 regl con)
 190: regl -> (RSHS I32 regl shfct)
 157: regq -> (RSHS I64 regq con)
 158: regq -> (RSHS I64 regq con)
 159: regq -> (RSHS I64 regq con)
 160: regq -> (RSHS I64 regq shfct)
 188: regl -> (RSHU I32 regl con)
 191: regl -> (RSHU I32 regl shfct)
 161: regq -> (RSHU I64 regq con)
 162: regq -> (RSHU I64 regq con)
 163: regq -> (RSHU I64 regq con)
 164: regq -> (RSHU I64 regq shfct)
 296: _35 -> (TSTEQ I32 regq mrcq)
 316: _45 -> (TSTEQ I32 regl mrcl)
 336: _55 -> (TSTEQ I32 mrcl regl)
 356: _65 -> (TSTEQ I32 regb memb)
 360: _67 -> (TSTEQ I32 regb rcb)
 364: _69 -> (TSTEQ I32 memb regb)
 368: _71 -> (TSTEQ I32 rcb regb)
 372: _73 -> (TSTEQ I32 regdx regmemdx)
 380: _77 -> (TSTEQ I32 regfx regmemfx)
 396: _85 -> (TSTEQ I32 regd regmemd)
 400: _87 -> (TSTEQ I32 regf regmemf)
 298: _36 -> (TSTNE I32 regq mrcq)
 318: _46 -> (TSTNE I32 regl mrcl)
 338: _56 -> (TSTNE I32 mrcl regl)
 358: _66 -> (TSTNE I32 regb memb)
 362: _68 -> (TSTNE I32 regb rcb)
 366: _70 -> (TSTNE I32 memb regb)
 370: _72 -> (TSTNE I32 rcb regb)
 374: _74 -> (TSTNE I32 regdx regmemdx)
 382: _78 -> (TSTNE I32 regfx regmemfx)
 398: _86 -> (TSTNE I32 regd regmemd)
 402: _88 -> (TSTNE I32 regf regmemf)
 300: _37 -> (TSTLTS I32 regq mrcq)
 320: _47 -> (TSTLTS I32 regl mrcl)
 340: _57 -> (TSTLTS I32 mrcl regl)
 376: _75 -> (TSTLTS I32 regdx regmemdx)
 384: _79 -> (TSTLTS I32 regfx regmemfx)
 404: _89 -> (TSTLTS I32 regd regmemd)
 408: _91 -> (TSTLTS I32 regf regmemf)
 302: _38 -> (TSTLES I32 regq mrcq)
 322: _48 -> (TSTLES I32 regl mrcl)
 342: _58 -> (TSTLES I32 mrcl regl)
 378: _76 -> (TSTLES I32 regdx regmemdx)
 386: _80 -> (TSTLES I32 regfx regmemfx)
 414: _94 -> (TSTLES I32 regd regmemd)
 418: _96 -> (TSTLES I32 regf regmemf)
 304: _39 -> (TSTGTS I32 regq mrcq)
 324: _49 -> (TSTGTS I32 regl mrcl)
 344: _59 -> (TSTGTS I32 mrcl regl)
 388: _81 -> (TSTGTS I32 regdx regmemdx)
 392: _83 -> (TSTGTS I32 regfx regmemfx)
 412: _93 -> (TSTGTS I32 regd regmemd)
 416: _95 -> (TSTGTS I32 regf regmemf)
 306: _40 -> (TSTGES I32 regq mrcq)
 326: _50 -> (TSTGES I32 regl mrcl)
 346: _60 -> (TSTGES I32 mrcl regl)
 390: _82 -> (TSTGES I32 regdx regmemdx)
 394: _84 -> (TSTGES I32 regfx regmemfx)
 406: _90 -> (TSTGES I32 regd regmemd)
 410: _92 -> (TSTGES I32 regf regmemf)
 308: _41 -> (TSTLTU I32 regq mrcq)
 328: _51 -> (TSTLTU I32 regl mrcl)
 348: _61 -> (TSTLTU I32 mrcl regl)
 310: _42 -> (TSTLEU I32 regq mrcq)
 330: _52 -> (TSTLEU I32 regl mrcl)
 350: _62 -> (TSTLEU I32 mrcl regl)
 312: _43 -> (TSTGTU I32 regq mrcq)
 332: _53 -> (TSTGTU I32 regl mrcl)
 352: _63 -> (TSTGTU I32 mrcl regl)
 314: _44 -> (TSTGEU I32 regq mrcq)
 334: _54 -> (TSTGEU I32 regl mrcl)
 354: _64 -> (TSTGEU I32 mrcl regl)
 56: memb -> (MEM I8 addr)
 55: memw -> (MEM I16 addr)
 54: meml -> (MEM I32 addr)
 121: _13 -> (MEM I32 _12)
 125: _16 -> (MEM I32 _6)
 57: memf -> (MEM F32 addr)
 132: _21 -> (MEM F32 _12)
 53: memq -> (MEM I64 addr)
 116: _9 -> (MEM I64 _8)
 58: memd -> (MEM F64 addr)
 135: _23 -> (MEM F64 _8)
 97: void -> (SET I8 memb rcb)
 101: void -> (SET I8 xregb regb)
 96: void -> (SET I16 memw rcw)
 100: void -> (SET I16 xregw regw)
 95: void -> (SET I32 meml rcl)
 99: void -> (SET I32 xregl regl)
 114: void -> (SET I32 _6 _7)
 118: _11 -> (SET I32 _6 _8)
 122: _14 -> (SET I32 _13 mrcl)
 123: _15 -> (SET I32 _6 _12)
 126: _17 -> (SET I32 _16 regl)
 128: _18 -> (SET I32 regl _16)
 130: _20 -> (SET I32 _6 _19)
 282: void -> (SET I32 meml _30)
 284: void -> (SET I32 memw _31)
 292: void -> (SET I32 meml _33)
 294: void -> (SET I32 memw _34)
 91: void -> (SET F32 regf regfx)
 103: void -> (SET F32 xregfx regfx)
 105: void -> (SET F32 xregfx regf)
 106: void -> (SET F32 xregf regf)
 109: void -> (SET F32 memf regfx)
 111: void -> (SET F32 memf regf)
 133: _22 -> (SET F32 _21 memf)
 138: _25 -> (SET F32 _21 regfx)
 142: _27 -> (SET F32 _21 regf)
 94: void -> (SET I64 memq regq)
 98: void -> (SET I64 xregq regq)
 117: _10 -> (SET I64 _9 mrcq)
 280: void -> (SET I64 memq _29)
 290: void -> (SET I64 memq _32)
 90: void -> (SET F64 regd regdx)
 102: void -> (SET F64 xregdx regdx)
 104: void -> (SET F64 xregdx regd)
 107: void -> (SET F64 xregd regd)
 108: void -> (SET F64 memd regdx)
 110: void -> (SET F64 memd regd)
 136: _24 -> (SET F64 _23 memd)
 140: _26 -> (SET F64 _23 regdx)
 144: _28 -> (SET F64 _23 regd)
 295: void -> (JUMP _ lab)
 297: void -> (JUMPC _ _35 lab lab)
 299: void -> (JUMPC _ _36 lab lab)
 301: void -> (JUMPC _ _37 lab lab)
 303: void -> (JUMPC _ _38 lab lab)
 305: void -> (JUMPC _ _39 lab lab)
 307: void -> (JUMPC _ _40 lab lab)
 309: void -> (JUMPC _ _41 lab lab)
 311: void -> (JUMPC _ _42 lab lab)
 313: void -> (JUMPC _ _43 lab lab)
 315: void -> (JUMPC _ _44 lab lab)
 317: void -> (JUMPC _ _45 lab lab)
 319: void -> (JUMPC _ _46 lab lab)
 321: void -> (JUMPC _ _47 lab lab)
 323: void -> (JUMPC _ _48 lab lab)
 325: void -> (JUMPC _ _49 lab lab)
 327: void -> (JUMPC _ _50 lab lab)
 329: void -> (JUMPC _ _51 lab lab)
 331: void -> (JUMPC _ _52 lab lab)
 333: void -> (JUMPC _ _53 lab lab)
 335: void -> (JUMPC _ _54 lab lab)
 337: void -> (JUMPC _ _55 lab lab)
 339: void -> (JUMPC _ _56 lab lab)
 341: void -> (JUMPC _ _57 lab lab)
 343: void -> (JUMPC _ _58 lab lab)
 345: void -> (JUMPC _ _59 lab lab)
 347: void -> (JUMPC _ _60 lab lab)
 349: void -> (JUMPC _ _61 lab lab)
 351: void -> (JUMPC _ _62 lab lab)
 353: void -> (JUMPC _ _63 lab lab)
 355: void -> (JUMPC _ _64 lab lab)
 357: void -> (JUMPC _ _65 lab lab)
 359: void -> (JUMPC _ _66 lab lab)
 361: void -> (JUMPC _ _67 lab lab)
 363: void -> (JUMPC _ _68 lab lab)
 365: void -> (JUMPC _ _69 lab lab)
 367: void -> (JUMPC _ _70 lab lab)
 369: void -> (JUMPC _ _71 lab lab)
 371: void -> (JUMPC _ _72 lab lab)
 373: void -> (JUMPC _ _73 lab lab)
 375: void -> (JUMPC _ _74 lab lab)
 377: void -> (JUMPC _ _75 lab lab)
 379: void -> (JUMPC _ _76 lab lab)
 381: void -> (JUMPC _ _77 lab lab)
 383: void -> (JUMPC _ _78 lab lab)
 385: void -> (JUMPC _ _79 lab lab)
 387: void -> (JUMPC _ _80 lab lab)
 389: void -> (JUMPC _ _81 lab lab)
 391: void -> (JUMPC _ _82 lab lab)
 393: void -> (JUMPC _ _83 lab lab)
 395: void -> (JUMPC _ _84 lab lab)
 397: void -> (JUMPC _ _85 lab lab)
 399: void -> (JUMPC _ _86 lab lab)
 401: void -> (JUMPC _ _87 lab lab)
 403: void -> (JUMPC _ _88 lab lab)
 405: void -> (JUMPC _ _89 lab lab)
 407: void -> (JUMPC _ _90 lab lab)
 409: void -> (JUMPC _ _91 lab lab)
 411: void -> (JUMPC _ _92 lab lab)
 413: void -> (JUMPC _ _93 lab lab)
 415: void -> (JUMPC _ _94 lab lab)
 417: void -> (JUMPC _ _95 lab lab)
 419: void -> (JUMPC _ _96 lab lab)
 420: void -> (CALL _ callarg)
 119: void -> (PARALLEL _ _10 _11)
 124: void -> (PARALLEL _ _14 _15)
 127: void -> (PARALLEL _ _17 _15)
 131: void -> (PARALLEL _ _18 _20)
 134: void -> (PARALLEL _ _22 _15)
 137: void -> (PARALLEL _ _24 _11)
 139: void -> (PARALLEL _ _25 _15)
 141: void -> (PARALLEL _ _26 _11)
 143: void -> (PARALLEL _ _27 _15)
 145: void -> (PARALLEL _ _28 _11)
*/
/*
Productions:
 1: _rewr -> (CONVUF _ _)
 2: _rewr -> (CONVFU _ _)
 3: _1 -> (STATIC I32 "__builtin_va_start")
 4: _2 -> (LIST _ _)
 5: _rewr -> (CALL _ _1 _2 _2)
 6: _3 -> (STATIC I32 "alloca")
 7: _rewr -> (CALL _ _3 _2 _2)
 8: _rewr -> (FLOATCONST F32)
 9: _rewr -> (FLOATCONST F64)
 10: _rewr -> (TSTNE I32 sbyteopr sbyteopr)
 11: _rewr -> (TSTEQ I32 sbyteopr sbyteopr)
 12: _rewr -> (TSTNE I32 ubyteopr ubyteopr)
 13: _rewr -> (TSTEQ I32 ubyteopr ubyteopr)
 14: sbyteopr -> (CONVSX I32 _)
 15: sbyteopr -> (INTCONST I32)
 16: ubyteopr -> (CONVZX I32 _)
 17: ubyteopr -> (INTCONST I32)
 18: _4 -> (CONVSX _ _)
 19: _rewr -> (LSHS I32 _ _4)
 20: _rewr -> (RSHS I32 _ _4)
 21: _rewr -> (RSHU I32 _ _4)
 22: _rewr -> (LSHS I64 _ _4)
 23: _rewr -> (RSHS I64 _ _4)
 24: _rewr -> (RSHU I64 _ _4)
 25: _rewr -> (PROLOGUE _)
 26: _rewr -> (EPILOGUE _)
 27: _rewr -> (REG I32 ".strretp")
 28: _rewr -> (CALL _)
 29: _rewr -> (JUMPN _)
 30: _rewr -> (SET _)
*/
/*
Sorted Productions:
 15: sbyteopr -> (INTCONST I32)
 17: ubyteopr -> (INTCONST I32)
 8: _rewr -> (FLOATCONST F32)
 9: _rewr -> (FLOATCONST F64)
 3: _1 -> (STATIC I32 "__builtin_va_start")
 6: _3 -> (STATIC I32 "alloca")
 27: _rewr -> (REG I32 ".strretp")
 18: _4 -> (CONVSX _ _)
 14: sbyteopr -> (CONVSX I32 _)
 16: ubyteopr -> (CONVZX I32 _)
 2: _rewr -> (CONVFU _ _)
 1: _rewr -> (CONVUF _ _)
 19: _rewr -> (LSHS I32 _ _4)
 22: _rewr -> (LSHS I64 _ _4)
 20: _rewr -> (RSHS I32 _ _4)
 23: _rewr -> (RSHS I64 _ _4)
 21: _rewr -> (RSHU I32 _ _4)
 24: _rewr -> (RSHU I64 _ _4)
 11: _rewr -> (TSTEQ I32 sbyteopr sbyteopr)
 13: _rewr -> (TSTEQ I32 ubyteopr ubyteopr)
 10: _rewr -> (TSTNE I32 sbyteopr sbyteopr)
 12: _rewr -> (TSTNE I32 ubyteopr ubyteopr)
 30: _rewr -> (SET _)
 29: _rewr -> (JUMPN _)
 5: _rewr -> (CALL _ _1 _2 _2)
 7: _rewr -> (CALL _ _3 _2 _2)
 28: _rewr -> (CALL _)
 25: _rewr -> (PROLOGUE _)
 26: _rewr -> (EPILOGUE _)
 4: _2 -> (LIST _ _)
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
import coins.backend.lir.LirUnaOp;
import coins.backend.lir.LirUnaOp;
import coins.backend.sym.Symbol;
import coins.backend.LocalTransformer;
import coins.backend.Transformer;
import coins.backend.ana.SaveRegisters;
import coins.backend.util.NumberSet;
import coins.backend.util.BitMapSet;

//
import coins.backend.ana.LoopAnalysis;
import java.util.HashMap;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;




public class CodeGenerator_x86sse2 extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_x86sse2() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 8;
    static final int NRULES = 30 + 1;
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
          if (-128 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 127) record(NT_sbyteopr, 15);
          if (((LirIconst)t).unsignedValue() <= 255) record(NT_ubyteopr, 17);
        }
        break;
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
          if (((LirSymRef)t).symbol.name == "__builtin_va_start") record(NT__1, 3);
          if (((LirSymRef)t).symbol.name == "alloca") record(NT__3, 6);
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
        record(NT__4, 18);
        if (t.type == 514) {
          if (t.kid(0).type == I8) record(NT_sbyteopr, 14);
        }
        break;
      case Op.CONVZX:
        if (t.type == 514) {
          if (t.kid(0).type == I8) record(NT_ubyteopr, 16);
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
        if (kids.length == 1) record(NT__2, 4);
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
    static final int NNONTERM = 145;
    static final int NRULES = 420 + 1;
    static final int START_NT = 10;

    static final int NT__ = 0;
    static final int NT_regq = 1;
    static final int NT_regl = 2;
    static final int NT_reglb = 3;
    static final int NT_regw = 4;
    static final int NT_regb = 5;
    static final int NT_regfx = 6;
    static final int NT_regdx = 7;
    static final int NT_regf = 8;
    static final int NT_regd = 9;
    static final int NT_void = 10;
    static final int NT_xregb = 11;
    static final int NT_xregw = 12;
    static final int NT_xregl = 13;
    static final int NT_xregq = 14;
    static final int NT_xregf = 15;
    static final int NT_xregd = 16;
    static final int NT_xregfx = 17;
    static final int NT_xregdx = 18;
    static final int NT_con = 19;
    static final int NT_sta = 20;
    static final int NT_asmcon = 21;
    static final int NT_lab = 22;
    static final int NT_base = 23;
    static final int NT_index = 24;
    static final int NT__1 = 25;
    static final int NT__2 = 26;
    static final int NT__3 = 27;
    static final int NT__4 = 28;
    static final int NT__5 = 29;
    static final int NT_addr = 30;
    static final int NT_memq = 31;
    static final int NT_meml = 32;
    static final int NT_memw = 33;
    static final int NT_memb = 34;
    static final int NT_memf = 35;
    static final int NT_memd = 36;
    static final int NT_rcl = 37;
    static final int NT_rcw = 38;
    static final int NT_rcb = 39;
    static final int NT_mrcl = 40;
    static final int NT_mregl = 41;
    static final int NT_mrcw = 42;
    static final int NT_mregw = 43;
    static final int NT_mrcb = 44;
    static final int NT_mregb = 45;
    static final int NT_mregq = 46;
    static final int NT_mrcq = 47;
    static final int NT_callarg = 48;
    static final int NT__6 = 49;
    static final int NT__7 = 50;
    static final int NT__8 = 51;
    static final int NT__9 = 52;
    static final int NT__10 = 53;
    static final int NT__11 = 54;
    static final int NT__12 = 55;
    static final int NT__13 = 56;
    static final int NT__14 = 57;
    static final int NT__15 = 58;
    static final int NT__16 = 59;
    static final int NT__17 = 60;
    static final int NT__18 = 61;
    static final int NT__19 = 62;
    static final int NT__20 = 63;
    static final int NT__21 = 64;
    static final int NT__22 = 65;
    static final int NT__23 = 66;
    static final int NT__24 = 67;
    static final int NT__25 = 68;
    static final int NT__26 = 69;
    static final int NT__27 = 70;
    static final int NT__28 = 71;
    static final int NT_shfct = 72;
    static final int NT_regmemdx = 73;
    static final int NT_regmemfx = 74;
    static final int NT_regmemd = 75;
    static final int NT_regmemf = 76;
    static final int NT__29 = 77;
    static final int NT__30 = 78;
    static final int NT__31 = 79;
    static final int NT__32 = 80;
    static final int NT__33 = 81;
    static final int NT__34 = 82;
    static final int NT__35 = 83;
    static final int NT__36 = 84;
    static final int NT__37 = 85;
    static final int NT__38 = 86;
    static final int NT__39 = 87;
    static final int NT__40 = 88;
    static final int NT__41 = 89;
    static final int NT__42 = 90;
    static final int NT__43 = 91;
    static final int NT__44 = 92;
    static final int NT__45 = 93;
    static final int NT__46 = 94;
    static final int NT__47 = 95;
    static final int NT__48 = 96;
    static final int NT__49 = 97;
    static final int NT__50 = 98;
    static final int NT__51 = 99;
    static final int NT__52 = 100;
    static final int NT__53 = 101;
    static final int NT__54 = 102;
    static final int NT__55 = 103;
    static final int NT__56 = 104;
    static final int NT__57 = 105;
    static final int NT__58 = 106;
    static final int NT__59 = 107;
    static final int NT__60 = 108;
    static final int NT__61 = 109;
    static final int NT__62 = 110;
    static final int NT__63 = 111;
    static final int NT__64 = 112;
    static final int NT__65 = 113;
    static final int NT__66 = 114;
    static final int NT__67 = 115;
    static final int NT__68 = 116;
    static final int NT__69 = 117;
    static final int NT__70 = 118;
    static final int NT__71 = 119;
    static final int NT__72 = 120;
    static final int NT__73 = 121;
    static final int NT__74 = 122;
    static final int NT__75 = 123;
    static final int NT__76 = 124;
    static final int NT__77 = 125;
    static final int NT__78 = 126;
    static final int NT__79 = 127;
    static final int NT__80 = 128;
    static final int NT__81 = 129;
    static final int NT__82 = 130;
    static final int NT__83 = 131;
    static final int NT__84 = 132;
    static final int NT__85 = 133;
    static final int NT__86 = 134;
    static final int NT__87 = 135;
    static final int NT__88 = 136;
    static final int NT__89 = 137;
    static final int NT__90 = 138;
    static final int NT__91 = 139;
    static final int NT__92 = 140;
    static final int NT__93 = 141;
    static final int NT__94 = 142;
    static final int NT__95 = 143;
    static final int NT__96 = 144;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regq: return "regq";
      case NT_regl: return "regl";
      case NT_reglb: return "reglb";
      case NT_regw: return "regw";
      case NT_regb: return "regb";
      case NT_regfx: return "regfx";
      case NT_regdx: return "regdx";
      case NT_regf: return "regf";
      case NT_regd: return "regd";
      case NT_void: return "void";
      case NT_xregb: return "xregb";
      case NT_xregw: return "xregw";
      case NT_xregl: return "xregl";
      case NT_xregq: return "xregq";
      case NT_xregf: return "xregf";
      case NT_xregd: return "xregd";
      case NT_xregfx: return "xregfx";
      case NT_xregdx: return "xregdx";
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
      case NT__22: return "_22";
      case NT__23: return "_23";
      case NT__24: return "_24";
      case NT__25: return "_25";
      case NT__26: return "_26";
      case NT__27: return "_27";
      case NT__28: return "_28";
      case NT_shfct: return "shfct";
      case NT_regmemdx: return "regmemdx";
      case NT_regmemfx: return "regmemfx";
      case NT_regmemd: return "regmemd";
      case NT_regmemf: return "regmemf";
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
      case NT__74: return "_74";
      case NT__75: return "_75";
      case NT__76: return "_76";
      case NT__77: return "_77";
      case NT__78: return "_78";
      case NT__79: return "_79";
      case NT__80: return "_80";
      case NT__81: return "_81";
      case NT__82: return "_82";
      case NT__83: return "_83";
      case NT__84: return "_84";
      case NT__85: return "_85";
      case NT__86: return "_86";
      case NT__87: return "_87";
      case NT__88: return "_88";
      case NT__89: return "_89";
      case NT__90: return "_90";
      case NT__91: return "_91";
      case NT__92: return "_92";
      case NT__93: return "_93";
      case NT__94: return "_94";
      case NT__95: return "_95";
      case NT__96: return "_96";
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
          record(NT_mregq, 0 + cost1, 0 + cost2, 78);
          record(NT_shfct, 0 + cost1, 0 + cost2, 166);
          break;
        case NT_regl:
          record(NT_base, 0 + cost1, 0 + cost2, 33);
          record(NT_index, 0 + cost1, 0 + cost2, 37);
          record(NT_rcl, 0 + cost1, 0 + cost2, 59);
          record(NT_mregl, 0 + cost1, 0 + cost2, 68);
          record(NT_callarg, 0 + cost1, 0 + cost2, 82);
          record(NT_shfct, 0 + cost1, 0 + cost2, 167);
          record(NT_reglb, 0 + cost1, 0 + cost2, 226);
          break;
        case NT_regw:
          record(NT_rcw, 0 + cost1, 0 + cost2, 61);
          record(NT_mregw, 0 + cost1, 0 + cost2, 72);
          record(NT_shfct, 0 + cost1, 0 + cost2, 168);
          break;
        case NT_regb:
          record(NT_rcb, 0 + cost1, 0 + cost2, 63);
          record(NT_mregb, 0 + cost1, 0 + cost2, 76);
          record(NT_shfct, 0 + cost1, 0 + cost2, 169);
          record(NT_reglb, 2 + cost1, 2 + cost2, 227);
          break;
        case NT_regfx:
          record(NT_regf, 1 + cost1, 1 + cost2, 89);
          record(NT_regmemfx, 0 + cost1, 0 + cost2, 200);
          break;
        case NT_regdx:
          record(NT_regd, 1 + cost1, 1 + cost2, 88);
          record(NT_regmemdx, 0 + cost1, 0 + cost2, 198);
          break;
        case NT_regf:
          record(NT_regmemf, 0 + cost1, 0 + cost2, 204);
          break;
        case NT_regd:
          record(NT_regmemd, 0 + cost1, 0 + cost2, 202);
          break;
        case NT_xregb:
          record(NT_regb, 0 + cost1, 0 + cost2, 17);
          break;
        case NT_xregw:
          record(NT_regw, 0 + cost1, 0 + cost2, 18);
          break;
        case NT_xregl:
          record(NT_regl, 0 + cost1, 0 + cost2, 19);
          break;
        case NT_xregq:
          record(NT_regq, 0 + cost1, 0 + cost2, 20);
          break;
        case NT_xregf:
          record(NT_regf, 0 + cost1, 0 + cost2, 21);
          break;
        case NT_xregd:
          record(NT_regd, 0 + cost1, 0 + cost2, 22);
          break;
        case NT_xregfx:
          record(NT_regfx, 0 + cost1, 0 + cost2, 23);
          break;
        case NT_xregdx:
          record(NT_regdx, 0 + cost1, 0 + cost2, 24);
          break;
        case NT_con:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 27);
          record(NT_rcw, 0 + cost1, 0 + cost2, 62);
          record(NT_rcb, 0 + cost1, 0 + cost2, 64);
          record(NT_mrcq, 0 + cost1, 0 + cost2, 80);
          break;
        case NT_sta:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 28);
          record(NT_callarg, 0 + cost1, 0 + cost2, 81);
          break;
        case NT_asmcon:
          record(NT_base, 0 + cost1, 0 + cost2, 32);
          record(NT_rcl, 0 + cost1, 0 + cost2, 60);
          break;
        case NT_base:
          record(NT_addr, 0 + cost1, 0 + cost2, 49);
          break;
        case NT_index:
          record(NT_addr, 0 + cost1, 0 + cost2, 50);
          break;
        case NT_addr:
          record(NT_regl, 4 + cost1, 4 + cost2, 83);
          break;
        case NT_memq:
          record(NT_mregq, 0 + cost1, 0 + cost2, 77);
          record(NT_shfct, 1 + cost1, 1 + cost2, 165);
          break;
        case NT_meml:
          record(NT_mrcl, 0 + cost1, 0 + cost2, 65);
          record(NT_mregl, 0 + cost1, 0 + cost2, 67);
          break;
        case NT_memw:
          record(NT_mrcw, 0 + cost1, 0 + cost2, 69);
          record(NT_mregw, 0 + cost1, 0 + cost2, 71);
          break;
        case NT_memb:
          record(NT_mrcb, 0 + cost1, 0 + cost2, 73);
          record(NT_mregb, 0 + cost1, 0 + cost2, 75);
          break;
        case NT_memf:
          record(NT_regfx, 3 + cost1, 3 + cost2, 93);
          record(NT_regmemfx, 0 + cost1, 0 + cost2, 201);
          record(NT_regmemf, 0 + cost1, 0 + cost2, 205);
          break;
        case NT_memd:
          record(NT_regdx, 3 + cost1, 3 + cost2, 92);
          record(NT_regmemdx, 0 + cost1, 0 + cost2, 199);
          record(NT_regmemd, 0 + cost1, 0 + cost2, 203);
          break;
        case NT_rcl:
          record(NT_mrcl, 0 + cost1, 0 + cost2, 66);
          break;
        case NT_rcw:
          record(NT_mrcw, 0 + cost1, 0 + cost2, 70);
          break;
        case NT_rcb:
          record(NT_mrcb, 0 + cost1, 0 + cost2, 74);
          break;
        case NT_mrcl:
          record(NT_regl, 1 + cost1, 1 + cost2, 85);
          break;
        case NT_mrcw:
          record(NT_regw, 1 + cost1, 1 + cost2, 86);
          break;
        case NT_mrcb:
          record(NT_regb, 1 + cost1, 1 + cost2, 87);
          break;
        case NT_mregq:
          record(NT_mrcq, 0 + cost1, 0 + cost2, 79);
          break;
        case NT_mrcq:
          record(NT_regq, 2 + cost1, 2 + cost2, 84);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        rract2(t, kids);
        break;
      case Op.STATIC:
        rract4(t, kids);
        break;
      case Op.REG:
        rract6(t, kids);
        break;
      case Op.SUBREG:
        rract7(t, kids);
        break;
      case Op.LABEL:
        rract8(t, kids);
        break;
      case Op.NEG:
        rract9(t, kids);
        break;
      case Op.ADD:
        rract10(t, kids);
        break;
      case Op.SUB:
        rract11(t, kids);
        break;
      case Op.MUL:
        rract12(t, kids);
        break;
      case Op.DIVS:
        rract13(t, kids);
        break;
      case Op.DIVU:
        rract14(t, kids);
        break;
      case Op.MODS:
        rract15(t, kids);
        break;
      case Op.MODU:
        rract16(t, kids);
        break;
      case Op.CONVSX:
        rract17(t, kids);
        break;
      case Op.CONVZX:
        rract18(t, kids);
        break;
      case Op.CONVIT:
        rract19(t, kids);
        break;
      case Op.CONVFX:
        rract20(t, kids);
        break;
      case Op.CONVFT:
        rract21(t, kids);
        break;
      case Op.CONVFS:
        rract23(t, kids);
        break;
      case Op.CONVSF:
        rract25(t, kids);
        break;
      case Op.BAND:
        rract27(t, kids);
        break;
      case Op.BOR:
        rract28(t, kids);
        break;
      case Op.BXOR:
        rract29(t, kids);
        break;
      case Op.BNOT:
        rract30(t, kids);
        break;
      case Op.LSHS:
        rract31(t, kids);
        break;
      case Op.RSHS:
        rract33(t, kids);
        break;
      case Op.RSHU:
        rract34(t, kids);
        break;
      case Op.TSTEQ:
        rract35(t, kids);
        break;
      case Op.TSTNE:
        rract36(t, kids);
        break;
      case Op.TSTLTS:
        rract37(t, kids);
        break;
      case Op.TSTLES:
        rract38(t, kids);
        break;
      case Op.TSTGTS:
        rract39(t, kids);
        break;
      case Op.TSTGES:
        rract40(t, kids);
        break;
      case Op.TSTLTU:
        rract41(t, kids);
        break;
      case Op.TSTLEU:
        rract42(t, kids);
        break;
      case Op.TSTGTU:
        rract43(t, kids);
        break;
      case Op.TSTGEU:
        rract44(t, kids);
        break;
      case Op.MEM:
        rract47(t, kids);
        break;
      case Op.SET:
        rract48(t, kids);
        break;
      case Op.JUMP:
        rract49(t, kids);
        break;
      case Op.JUMPC:
        rract50(t, kids);
        break;
      case Op.CALL:
        rract53(t, kids);
        break;
      case Op.PARALLEL:
        rract56(t, kids);
        break;
      }
    }
    private void rract2(LirNode t, State kids[]) {
      record(NT_con, 0, 0, 25);
      if (t.type == 514) {
        if (((LirIconst)t).value == 2) record(NT__1, 0, 0, 38);
        if (((LirIconst)t).value == 4) record(NT__2, 0, 0, 40);
        if (((LirIconst)t).value == 8) record(NT__3, 0, 0, 42);
        if (((LirIconst)t).value == 1) record(NT__4, 0, 0, 44);
        if (((LirIconst)t).value == 3) record(NT__5, 0, 0, 47);
      }
    }
    private void rract4(LirNode t, State kids[]) {
      if (t.type == 514) {
        record(NT_sta, 0, 0, 26);
      }
    }
    private void rract6(LirNode t, State kids[]) {
      if (t.type == 130) {
        record(NT_xregb, 0, 0, 1);
      }
      if (t.type == 258) {
        record(NT_xregw, 0, 0, 3);
      }
      if (t.type == 514) {
        record(NT_xregl, 0, 0, 5);
        if (((LirSymRef)t).symbol.name == "%esp") record(NT__6, 0, 0, 112);
      }
      if (t.type == 516) {
        if (!isXmm(t)) record(NT_xregf, 0, 0, 9);
        if (isXmm(t)) record(NT_xregfx, 0, 0, 13);
      }
      if (t.type == 1026) {
        record(NT_xregq, 0, 0, 7);
      }
      if (t.type == 1028) {
        if (!isXmm(t)) record(NT_xregd, 0, 0, 11);
        if (isXmm(t)) record(NT_xregdx, 0, 0, 15);
      }
    }
    private void rract7(LirNode t, State kids[]) {
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
        if (!isXmm(t)) record(NT_xregf, 0, 0, 10);
        if (isXmm(t)) record(NT_xregfx, 0, 0, 14);
      }
      if (t.type == 1026) {
        record(NT_xregq, 0, 0, 8);
      }
      if (t.type == 1028) {
        if (!isXmm(t)) record(NT_xregd, 0, 0, 12);
        if (isXmm(t)) record(NT_xregdx, 0, 0, 16);
      }
    }
    private void rract8(LirNode t, State kids[]) {
      if (t.type == 514) {
        record(NT_lab, 0, 0, 31);
      }
    }
    private void rract9(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 184);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regfx] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_regfx], 1 + kids[0].cost2[NT_regfx], 215);
        if (kids[0].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 225);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 151);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regdx] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_regdx], 1 + kids[0].cost2[NT_regdx], 214);
        if (kids[0].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 220);
      }
    }
    private void rract10(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con], 29);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_asmcon] != 0) record(NT_base, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_asmcon], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_asmcon], 34);
        if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_base, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_regl], 35);
        if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_index] != 0) record(NT_addr, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_index], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_index], 51);
        if (kids[0].rule[NT_index] != 0) if (kids[1].rule[NT_base] != 0) record(NT_addr, 0 + kids[0].cost1[NT_index] + kids[1].cost1[NT_base], 0 + kids[0].cost2[NT_index] + kids[1].cost2[NT_base], 52);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__2] != 0) record(NT__19, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__2], 129);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 175);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 180);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 1 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 210);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 221);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 146);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 1 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 206);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 216);
      }
    }
    private void rract11(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con], 30);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_base, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 36);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__7, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT_regl], 113);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__3] != 0) record(NT__8, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__3], 115);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__2] != 0) record(NT__12, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__2], 120);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 176);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 1 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 211);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 222);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 147);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 1 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 207);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 217);
      }
    }
    private void rract12(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__1] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__1], 39);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__2] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__2], 41);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__3] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__3], 43);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 192);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 193);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 1 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 212);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 223);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 7 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 7 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 170);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 1 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 208);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 2 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 218);
      }
    }
    private void rract13(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 194);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 1 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 213);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 2 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 224);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 171);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT_regdx, 10 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 10 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 209);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 20 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 20 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 219);
      }
    }
    private void rract14(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 195);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 172);
      }
    }
    private void rract15(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 196);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 173);
      }
    }
    private void rract16(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 14 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 14 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 197);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 6 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_mrcq], 6 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_mrcq], 174);
      }
    }
    private void rract17(LirNode t, State kids[]) {
      if (t.type == 258) {
        if (kids[0].rule[NT_mregb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 233);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_mregw] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregw], 2 + kids[0].cost2[NT_mregw], 231);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 232);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regl] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 228);
        if (kids[0].rule[NT_regw] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 229);
        if (kids[0].rule[NT_regb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 230);
      }
    }
    private void rract18(LirNode t, State kids[]) {
      if (t.type == 258) {
        if (kids[0].rule[NT_mregb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 240);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_mregw] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregw], 2 + kids[0].cost2[NT_mregw], 238);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 239);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_mregl] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregl], 2 + kids[0].cost2[NT_mregl], 234);
        if (kids[0].rule[NT_mregw] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregw], 2 + kids[0].cost2[NT_mregw], 235);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 236);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regq, 2 + kids[0].cost1[NT_mregb], 2 + kids[0].cost2[NT_mregb], 237);
      }
    }
    private void rract19(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 243);
        if (kids[0].rule[NT_regl] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 245);
        if (kids[0].rule[NT_regw] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 246);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 242);
        if (kids[0].rule[NT_regl] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 244);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 241);
      }
    }
    private void rract20(LirNode t, State kids[]) {
      if (t.type == 1028) {
        if (kids[0].rule[NT_regfx] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_regfx], 1 + kids[0].cost2[NT_regfx], 253);
        if (kids[0].rule[NT_regf] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 267);
      }
    }
    private void rract21(LirNode t, State kids[]) {
      if (t.type == 516) {
        if (kids[0].rule[NT_regdx] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_regdx], 1 + kids[0].cost2[NT_regdx], 254);
        if (kids[0].rule[NT_regd] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 268);
      }
    }
    private void rract23(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_regfx] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regfx], 2 + kids[0].cost2[NT_regfx], 269);
        if (kids[0].rule[NT_regdx] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regdx], 2 + kids[0].cost2[NT_regdx], 270);
        if (kids[0].rule[NT_regd] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 278);
        if (kids[0].rule[NT_regf] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 288);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_regfx] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regfx], 2 + kids[0].cost2[NT_regfx], 271);
        if (kids[0].rule[NT_regdx] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regdx], 2 + kids[0].cost2[NT_regdx], 272);
        if (kids[0].rule[NT_regd] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 277);
        if (kids[0].rule[NT_regd] != 0) record(NT__31, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 283);
        if (kids[0].rule[NT_regf] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 287);
        if (kids[0].rule[NT_regf] != 0) record(NT__34, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 293);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_regfx] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regfx], 1 + kids[0].cost2[NT_regfx], 273);
        if (kids[0].rule[NT_regdx] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regdx], 1 + kids[0].cost2[NT_regdx], 274);
        if (kids[0].rule[NT_regd] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 276);
        if (kids[0].rule[NT_regd] != 0) record(NT__30, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 281);
        if (kids[0].rule[NT_regf] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 286);
        if (kids[0].rule[NT_regf] != 0) record(NT__33, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 291);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regd] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 275);
        if (kids[0].rule[NT_regd] != 0) record(NT__29, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 279);
        if (kids[0].rule[NT_regf] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 285);
        if (kids[0].rule[NT_regf] != 0) record(NT__32, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 289);
      }
    }
    private void rract25(LirNode t, State kids[]) {
      if (t.type == 516) {
        if (kids[0].rule[NT_meml] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 250);
        if (kids[0].rule[NT_reglb] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_reglb], 1 + kids[0].cost2[NT_reglb], 251);
        if (kids[0].rule[NT_memw] != 0) record(NT_regfx, 1 + kids[0].cost1[NT_memw], 1 + kids[0].cost2[NT_memw], 252);
        if (kids[0].rule[NT_memq] != 0) record(NT_regf, 1 + kids[0].cost1[NT_memq], 1 + kids[0].cost2[NT_memq], 261);
        if (kids[0].rule[NT_regq] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 262);
        if (kids[0].rule[NT_meml] != 0) record(NT_regf, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 263);
        if (kids[0].rule[NT_reglb] != 0) record(NT_regf, 2 + kids[0].cost1[NT_reglb], 2 + kids[0].cost2[NT_reglb], 264);
        if (kids[0].rule[NT_memw] != 0) record(NT_regf, 1 + kids[0].cost1[NT_memw], 1 + kids[0].cost2[NT_memw], 265);
        if (kids[0].rule[NT_regw] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 266);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_meml] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 247);
        if (kids[0].rule[NT_reglb] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_reglb], 1 + kids[0].cost2[NT_reglb], 248);
        if (kids[0].rule[NT_memw] != 0) record(NT_regdx, 1 + kids[0].cost1[NT_memw], 1 + kids[0].cost2[NT_memw], 249);
        if (kids[0].rule[NT_memq] != 0) record(NT_regd, 1 + kids[0].cost1[NT_memq], 1 + kids[0].cost2[NT_memq], 255);
        if (kids[0].rule[NT_regq] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 256);
        if (kids[0].rule[NT_meml] != 0) record(NT_regd, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 257);
        if (kids[0].rule[NT_reglb] != 0) record(NT_regd, 2 + kids[0].cost1[NT_reglb], 2 + kids[0].cost2[NT_reglb], 258);
        if (kids[0].rule[NT_memw] != 0) record(NT_regd, 1 + kids[0].cost1[NT_memw], 1 + kids[0].cost2[NT_memw], 259);
        if (kids[0].rule[NT_regw] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 260);
      }
    }
    private void rract27(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 177);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 181);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 148);
      }
    }
    private void rract28(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 178);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 182);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 149);
      }
    }
    private void rract29(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 179);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 2 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 183);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 150);
      }
    }
    private void rract30(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 185);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq], 2 + kids[0].cost2[NT_regq], 152);
      }
    }
    private void rract31(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 45);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__1] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__1], 46);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__5] != 0) record(NT_index, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__5], 48);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 186);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 189);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 153);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 154);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() > 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 155);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 156);
      }
    }
    private void rract33(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 187);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 190);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 157);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 158);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() > 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 159);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 160);
      }
    }
    private void rract34(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 188);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 191);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() < 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 161);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() == 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 162);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) if (((LirIconst)t.kid(1)).signedValue() > 32) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 163);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 2 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 164);
      }
    }
    private void rract35(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__35, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 296);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__45, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 316);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__55, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 336);
        if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_memb] != 0) record(NT__65, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_memb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_memb], 356);
        if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_rcb] != 0) record(NT__67, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_rcb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_rcb], 360);
        if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__69, 0 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_regb], 364);
        if (kids[0].rule[NT_rcb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__71, 0 + kids[0].cost1[NT_rcb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_rcb] + kids[1].cost2[NT_regb], 368);
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT__73, 0 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 0 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 372);
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT__77, 0 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 0 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 380);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__85, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 396);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__87, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 400);
      }
    }
    private void rract36(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__36, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 298);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__46, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 318);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__56, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 338);
        if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_memb] != 0) record(NT__66, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_memb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_memb], 358);
        if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_rcb] != 0) record(NT__68, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_rcb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_rcb], 362);
        if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__70, 0 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_regb], 366);
        if (kids[0].rule[NT_rcb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__72, 0 + kids[0].cost1[NT_rcb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_rcb] + kids[1].cost2[NT_regb], 370);
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT__74, 0 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 0 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 374);
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT__78, 0 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 0 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 382);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__86, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 398);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__88, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 402);
      }
    }
    private void rract37(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__37, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 300);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__47, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 320);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__57, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 340);
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT__75, 0 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 0 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 376);
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT__79, 0 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 0 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 384);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__89, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 404);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__91, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 408);
      }
    }
    private void rract38(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__38, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 302);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__48, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 322);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__58, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 342);
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT__76, 0 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 0 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 378);
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT__80, 0 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 0 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 386);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__94, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 414);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__96, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 418);
      }
    }
    private void rract39(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__39, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 304);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__49, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 324);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__59, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 344);
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT__81, 0 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 0 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 388);
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT__83, 0 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 0 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 392);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__93, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 412);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__95, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 416);
      }
    }
    private void rract40(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__40, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 306);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__50, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 326);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__60, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 346);
        if (kids[0].rule[NT_regdx] != 0) if (kids[1].rule[NT_regmemdx] != 0) record(NT__82, 0 + kids[0].cost1[NT_regdx] + kids[1].cost1[NT_regmemdx], 0 + kids[0].cost2[NT_regdx] + kids[1].cost2[NT_regmemdx], 390);
        if (kids[0].rule[NT_regfx] != 0) if (kids[1].rule[NT_regmemfx] != 0) record(NT__84, 0 + kids[0].cost1[NT_regfx] + kids[1].cost1[NT_regmemfx], 0 + kids[0].cost2[NT_regfx] + kids[1].cost2[NT_regmemfx], 394);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__90, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 406);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__92, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 410);
      }
    }
    private void rract41(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__41, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 308);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__51, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 328);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__61, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 348);
      }
    }
    private void rract42(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__42, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 310);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__52, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 330);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__62, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 350);
      }
    }
    private void rract43(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__43, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 312);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__53, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 332);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__63, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 352);
      }
    }
    private void rract44(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__44, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 314);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__54, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 334);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__64, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 354);
      }
    }
    private void rract47(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 56);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 55);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_addr] != 0) record(NT_meml, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 54);
        if (kids[0].rule[NT__12] != 0) record(NT__13, 0 + kids[0].cost1[NT__12], 0 + kids[0].cost2[NT__12], 121);
        if (kids[0].rule[NT__6] != 0) record(NT__16, 0 + kids[0].cost1[NT__6], 0 + kids[0].cost2[NT__6], 125);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memf, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 57);
        if (kids[0].rule[NT__12] != 0) record(NT__21, 0 + kids[0].cost1[NT__12], 0 + kids[0].cost2[NT__12], 132);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memq, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 53);
        if (kids[0].rule[NT__8] != 0) record(NT__9, 0 + kids[0].cost1[NT__8], 0 + kids[0].cost2[NT__8], 116);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memd, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 58);
        if (kids[0].rule[NT__8] != 0) record(NT__23, 0 + kids[0].cost1[NT__8], 0 + kids[0].cost2[NT__8], 135);
      }
    }
    private void rract48(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_rcb] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_rcb], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_rcb], 97);
        if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 101);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT_rcw] != 0) record(NT_void, 1 + kids[0].cost1[NT_memw] + kids[1].cost1[NT_rcw], 1 + kids[0].cost2[NT_memw] + kids[1].cost2[NT_rcw], 96);
        if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regw], 100);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT_rcl] != 0) record(NT_void, 1 + kids[0].cost1[NT_meml] + kids[1].cost1[NT_rcl], 1 + kids[0].cost2[NT_meml] + kids[1].cost2[NT_rcl], 95);
        if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regl], 99);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__7] != 0) if (convention == "cygwin") record(NT_void, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__7], 114);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__8] != 0) record(NT__11, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__8], 118);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__14, 0 + kids[0].cost1[NT__13] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT__13] + kids[1].cost2[NT_mrcl], 122);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__12] != 0) record(NT__15, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__12], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__12], 123);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__17, 0 + kids[0].cost1[NT__16] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT__16] + kids[1].cost2[NT_regl], 126);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__16] != 0) record(NT__18, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__16], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__16], 128);
        if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__19] != 0) record(NT__20, 0 + kids[0].cost1[NT__6] + kids[1].cost1[NT__19], 0 + kids[0].cost2[NT__6] + kids[1].cost2[NT__19], 130);
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__30] != 0) record(NT_void, 2 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__30], 2 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__30], 282);
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__31] != 0) record(NT_void, 2 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__31], 2 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__31], 284);
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__33] != 0) record(NT_void, 2 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__33], 2 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__33], 292);
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__34] != 0) record(NT_void, 2 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__34], 2 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__34], 294);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regfx] != 0) record(NT_void, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regfx], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regfx], 91);
        if (kids[0].rule[NT_xregfx] != 0) if (kids[1].rule[NT_regfx] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregfx] + kids[1].cost1[NT_regfx], 1 + kids[0].cost2[NT_xregfx] + kids[1].cost2[NT_regfx], 103);
        if (kids[0].rule[NT_xregfx] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregfx] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregfx] + kids[1].cost2[NT_regf], 105);
        if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 2 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 106);
        if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_regfx] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_regfx], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_regfx], 109);
        if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_regf], 111);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_memf] != 0) record(NT__22, 0 + kids[0].cost1[NT__21] + kids[1].cost1[NT_memf], 0 + kids[0].cost2[NT__21] + kids[1].cost2[NT_memf], 133);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_regfx] != 0) record(NT__25, 0 + kids[0].cost1[NT__21] + kids[1].cost1[NT_regfx], 0 + kids[0].cost2[NT__21] + kids[1].cost2[NT_regfx], 138);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__27, 0 + kids[0].cost1[NT__21] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT__21] + kids[1].cost2[NT_regf], 142);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT_regq], 94);
        if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT_regq], 2 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT_regq], 98);
        if (kids[0].rule[NT__9] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__10, 0 + kids[0].cost1[NT__9] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT__9] + kids[1].cost2[NT_mrcq], 117);
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT__29] != 0) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT__29], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT__29], 280);
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT__32] != 0) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT__32], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT__32], 290);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regdx] != 0) record(NT_void, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regdx], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regdx], 90);
        if (kids[0].rule[NT_xregdx] != 0) if (kids[1].rule[NT_regdx] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregdx] + kids[1].cost1[NT_regdx], 1 + kids[0].cost2[NT_xregdx] + kids[1].cost2[NT_regdx], 102);
        if (kids[0].rule[NT_xregdx] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregdx] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregdx] + kids[1].cost2[NT_regd], 104);
        if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 2 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 107);
        if (kids[0].rule[NT_memd] != 0) if (kids[1].rule[NT_regdx] != 0) record(NT_void, 1 + kids[0].cost1[NT_memd] + kids[1].cost1[NT_regdx], 1 + kids[0].cost2[NT_memd] + kids[1].cost2[NT_regdx], 108);
        if (kids[0].rule[NT_memd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_memd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_memd] + kids[1].cost2[NT_regd], 110);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_memd] != 0) record(NT__24, 0 + kids[0].cost1[NT__23] + kids[1].cost1[NT_memd], 0 + kids[0].cost2[NT__23] + kids[1].cost2[NT_memd], 136);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_regdx] != 0) record(NT__26, 0 + kids[0].cost1[NT__23] + kids[1].cost1[NT_regdx], 0 + kids[0].cost2[NT__23] + kids[1].cost2[NT_regdx], 140);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__28, 0 + kids[0].cost1[NT__23] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT__23] + kids[1].cost2[NT_regd], 144);
      }
    }
    private void rract49(LirNode t, State kids[]) {
      if (kids[0].rule[NT_lab] != 0) record(NT_void, 3 + kids[0].cost1[NT_lab], 3 + kids[0].cost2[NT_lab], 295);
    }
    private void rract50(LirNode t, State kids[]) {
      if (kids[0].rule[NT__35] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__35] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__35] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 297);
      if (kids[0].rule[NT__36] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__36] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__36] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 299);
      if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__37] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__37] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 301);
      if (kids[0].rule[NT__38] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__38] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__38] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 303);
      if (kids[0].rule[NT__39] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__39] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__39] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 305);
      if (kids[0].rule[NT__40] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__40] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__40] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 307);
      if (kids[0].rule[NT__41] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__41] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__41] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 309);
      if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__42] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__42] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 311);
      if (kids[0].rule[NT__43] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__43] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__43] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 313);
      if (kids[0].rule[NT__44] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 8 + kids[0].cost1[NT__44] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 8 + kids[0].cost2[NT__44] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 315);
      if (kids[0].rule[NT__45] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__45] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__45] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 317);
      if (kids[0].rule[NT__46] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__46] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__46] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 319);
      if (kids[0].rule[NT__47] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__47] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__47] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 321);
      if (kids[0].rule[NT__48] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__48] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__48] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 323);
      if (kids[0].rule[NT__49] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__49] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__49] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 325);
      if (kids[0].rule[NT__50] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__50] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__50] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 327);
      if (kids[0].rule[NT__51] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__51] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__51] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 329);
      if (kids[0].rule[NT__52] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__52] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__52] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 331);
      if (kids[0].rule[NT__53] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__53] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__53] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 333);
      if (kids[0].rule[NT__54] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__54] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__54] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 335);
      if (kids[0].rule[NT__55] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__55] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__55] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 337);
      if (kids[0].rule[NT__56] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__56] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__56] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 339);
      if (kids[0].rule[NT__57] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__57] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__57] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 341);
      if (kids[0].rule[NT__58] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__58] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__58] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 343);
      if (kids[0].rule[NT__59] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__59] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__59] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 345);
      if (kids[0].rule[NT__60] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__60] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__60] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 347);
      if (kids[0].rule[NT__61] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__61] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__61] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 349);
      if (kids[0].rule[NT__62] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__62] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__62] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 351);
      if (kids[0].rule[NT__63] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__63] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__63] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 353);
      if (kids[0].rule[NT__64] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__64] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__64] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 355);
      if (kids[0].rule[NT__65] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__65] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__65] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 357);
      if (kids[0].rule[NT__66] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__66] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__66] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 359);
      if (kids[0].rule[NT__67] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__67] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__67] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 361);
      if (kids[0].rule[NT__68] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__68] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__68] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 363);
      if (kids[0].rule[NT__69] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__69] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__69] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 365);
      if (kids[0].rule[NT__70] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__70] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__70] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 367);
      if (kids[0].rule[NT__71] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__71] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__71] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 369);
      if (kids[0].rule[NT__72] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__72] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__72] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 371);
      if (kids[0].rule[NT__73] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__73] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__73] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 373);
      if (kids[0].rule[NT__74] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__74] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__74] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 375);
      if (kids[0].rule[NT__75] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__75] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__75] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 377);
      if (kids[0].rule[NT__76] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__76] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__76] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 379);
      if (kids[0].rule[NT__77] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__77] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__77] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 381);
      if (kids[0].rule[NT__78] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__78] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__78] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 383);
      if (kids[0].rule[NT__79] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__79] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__79] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 385);
      if (kids[0].rule[NT__80] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__80] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__80] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 387);
      if (kids[0].rule[NT__81] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__81] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__81] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 389);
      if (kids[0].rule[NT__82] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__82] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__82] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 391);
      if (kids[0].rule[NT__83] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__83] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__83] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 393);
      if (kids[0].rule[NT__84] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__84] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__84] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 395);
      if (kids[0].rule[NT__85] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__85] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__85] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 397);
      if (kids[0].rule[NT__86] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__86] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__86] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 399);
      if (kids[0].rule[NT__87] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__87] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__87] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 401);
      if (kids[0].rule[NT__88] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__88] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__88] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 403);
      if (kids[0].rule[NT__89] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__89] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__89] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 405);
      if (kids[0].rule[NT__90] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__90] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__90] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 407);
      if (kids[0].rule[NT__91] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__91] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__91] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 409);
      if (kids[0].rule[NT__92] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__92] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__92] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 411);
      if (kids[0].rule[NT__93] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__93] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__93] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 413);
      if (kids[0].rule[NT__94] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__94] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__94] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 415);
      if (kids[0].rule[NT__95] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__95] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__95] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 417);
      if (kids[0].rule[NT__96] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__96] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__96] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 419);
    }
    private void rract53(LirNode t, State kids[]) {
      if (kids[0].rule[NT_callarg] != 0) record(NT_void, 4 + kids[0].cost1[NT_callarg], 4 + kids[0].cost2[NT_callarg], 420);
    }
    private void rract56(LirNode t, State kids[]) {
      if (kids.length == 2) if (kids[0].rule[NT__10] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 2 + kids[0].cost1[NT__10] + kids[1].cost1[NT__11], 2 + kids[0].cost2[NT__10] + kids[1].cost2[NT__11], 119);
      if (kids.length == 2) if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__14] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__14] + kids[1].cost2[NT__15], 124);
      if (kids.length == 2) if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__17] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__17] + kids[1].cost2[NT__15], 127);
      if (kids.length == 2) if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT__20] != 0) record(NT_void, 1 + kids[0].cost1[NT__18] + kids[1].cost1[NT__20], 1 + kids[0].cost2[NT__18] + kids[1].cost2[NT__20], 131);
      if (kids.length == 2) if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__22] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__22] + kids[1].cost2[NT__15], 134);
      if (kids.length == 2) if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT__24] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT__24] + kids[1].cost2[NT__11], 137);
      if (kids.length == 2) if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__25] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__25] + kids[1].cost2[NT__15], 139);
      if (kids.length == 2) if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT__26] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT__26] + kids[1].cost2[NT__11], 141);
      if (kids.length == 2) if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT__15] != 0) record(NT_void, 1 + kids[0].cost1[NT__27] + kids[1].cost1[NT__15], 1 + kids[0].cost2[NT__27] + kids[1].cost2[NT__15], 143);
      if (kids.length == 2) if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT__28] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT__28] + kids[1].cost2[NT__11], 145);
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
    rrinit400();
  }
  static private void rrinit0() {
    rulev[78] = new Rule(78, true, false, 46, "78: mregq -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[166] = new Rule(166, true, false, 72, "166: shfct -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-ebxecx-I64*"});
    rulev[33] = new Rule(33, true, false, 23, "33: base -> regl", null, ImList.list(ImList.list("base",ImList.list(),"$1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[37] = new Rule(37, true, false, 24, "37: index -> regl", null, ImList.list(ImList.list("index","$1","1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[59] = new Rule(59, true, false, 37, "59: rcl -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[68] = new Rule(68, true, false, 41, "68: mregl -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[82] = new Rule(82, true, false, 48, "82: callarg -> regl", null, ImList.list(ImList.list("ind","$1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[167] = new Rule(167, true, false, 72, "167: shfct -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-ecx-I32*"});
    rulev[226] = new Rule(226, true, false, 3, "226: reglb -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[61] = new Rule(61, true, false, 38, "61: rcw -> regw", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-I16*"});
    rulev[72] = new Rule(72, true, false, 43, "72: mregw -> regw", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-I16*"});
    rulev[168] = new Rule(168, true, false, 72, "168: shfct -> regw", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-cx-I16*"});
    rulev[63] = new Rule(63, true, false, 39, "63: rcb -> regb", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-I8*"});
    rulev[76] = new Rule(76, true, false, 45, "76: mregb -> regb", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-I8*"});
    rulev[169] = new Rule(169, true, false, 72, "169: shfct -> regb", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-cl-I8*"});
    rulev[227] = new Rule(227, true, false, 3, "227: reglb -> regb", ImList.list(ImList.list("movsbl","$1","$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[89] = new Rule(89, true, false, 8, "89: regf -> regfx", ImList.list(ImList.list("movss","$1",ImList.list("reserve-tmpq")),ImList.list("flds",ImList.list("reserve-tmpq"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-tmp-F32*", "*reg-F32X*"});
    rulev[200] = new Rule(200, true, false, 74, "200: regmemfx -> regfx", null, null, null, 0, false, false, new int[]{6}, new String[]{null, "*reg-F32X*"});
    rulev[88] = new Rule(88, true, false, 9, "88: regd -> regdx", ImList.list(ImList.list("movsd","$1",ImList.list("reserve-tmpq")),ImList.list("fldl",ImList.list("reserve-tmpq"))), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-tmp-F64*", "*reg-F64X*"});
    rulev[198] = new Rule(198, true, false, 73, "198: regmemdx -> regdx", null, null, null, 0, false, false, new int[]{7}, new String[]{null, "*reg-F64X*"});
    rulev[204] = new Rule(204, true, false, 76, "204: regmemf -> regf", null, null, null, 0, false, false, new int[]{8}, new String[]{null, "*reg-tmp-F32*"});
    rulev[202] = new Rule(202, true, false, 75, "202: regmemd -> regd", null, null, null, 0, false, false, new int[]{9}, new String[]{null, "*reg-tmp-F64*"});
    rulev[17] = new Rule(17, true, false, 5, "17: regb -> xregb", null, null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I8*", null});
    rulev[18] = new Rule(18, true, false, 4, "18: regw -> xregw", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-I16*", null});
    rulev[19] = new Rule(19, true, false, 2, "19: regl -> xregl", null, null, null, 0, false, false, new int[]{13}, new String[]{"*reg-I32*", null});
    rulev[20] = new Rule(20, true, false, 1, "20: regq -> xregq", null, null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I64*", null});
    rulev[21] = new Rule(21, true, false, 8, "21: regf -> xregf", null, null, null, 0, false, false, new int[]{15}, new String[]{"*reg-tmp-F32*", null});
    rulev[22] = new Rule(22, true, false, 9, "22: regd -> xregd", null, null, null, 0, false, false, new int[]{16}, new String[]{"*reg-tmp-F64*", null});
    rulev[23] = new Rule(23, true, false, 6, "23: regfx -> xregfx", null, null, null, 0, false, false, new int[]{17}, new String[]{"*reg-F32X*", null});
    rulev[24] = new Rule(24, true, false, 7, "24: regdx -> xregdx", null, null, null, 0, false, false, new int[]{18}, new String[]{"*reg-F64X*", null});
    rulev[27] = new Rule(27, true, false, 21, "27: asmcon -> con", null, null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[62] = new Rule(62, true, false, 38, "62: rcw -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[64] = new Rule(64, true, false, 39, "64: rcb -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[80] = new Rule(80, true, false, 47, "80: mrcq -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[28] = new Rule(28, true, false, 21, "28: asmcon -> sta", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[81] = new Rule(81, true, false, 48, "81: callarg -> sta", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[32] = new Rule(32, true, false, 23, "32: base -> asmcon", null, ImList.list(ImList.list("base","$1",ImList.list())), null, 0, false, false, new int[]{21}, new String[]{null, null});
    rulev[60] = new Rule(60, true, false, 37, "60: rcl -> asmcon", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{21}, new String[]{null, null});
    rulev[49] = new Rule(49, true, false, 30, "49: addr -> base", null, ImList.list(ImList.list("addr","$1",ImList.list())), null, 0, false, false, new int[]{23}, new String[]{null, null});
    rulev[50] = new Rule(50, true, false, 30, "50: addr -> index", null, ImList.list(ImList.list("addr",ImList.list(),"$1")), null, 0, false, false, new int[]{24}, new String[]{null, null});
    rulev[83] = new Rule(83, true, false, 2, "83: regl -> addr", ImList.list(ImList.list("leal","$1","$0")), null, null, 0, false, false, new int[]{30}, new String[]{"*reg-I32*", null});
    rulev[77] = new Rule(77, true, false, 46, "77: mregq -> memq", null, null, null, 0, false, false, new int[]{31}, new String[]{null, null});
    rulev[165] = new Rule(165, true, false, 72, "165: shfct -> memq", ImList.list(ImList.list("movb","$1","$0")), null, null, 0, false, false, new int[]{31}, new String[]{"*reg-cl-I8*", null});
    rulev[65] = new Rule(65, true, false, 40, "65: mrcl -> meml", null, null, null, 0, false, false, new int[]{32}, new String[]{null, null});
    rulev[67] = new Rule(67, true, false, 41, "67: mregl -> meml", null, null, null, 0, false, false, new int[]{32}, new String[]{null, null});
    rulev[69] = new Rule(69, true, false, 42, "69: mrcw -> memw", null, null, null, 0, false, false, new int[]{33}, new String[]{null, null});
    rulev[71] = new Rule(71, true, false, 43, "71: mregw -> memw", null, null, null, 0, false, false, new int[]{33}, new String[]{null, null});
    rulev[73] = new Rule(73, true, false, 44, "73: mrcb -> memb", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[75] = new Rule(75, true, false, 45, "75: mregb -> memb", null, null, null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[93] = new Rule(93, true, false, 6, "93: regfx -> memf", ImList.list(ImList.list("movss","$1","$0")), null, null, 0, false, false, new int[]{35}, new String[]{"*reg-F32X*", null});
    rulev[201] = new Rule(201, true, false, 74, "201: regmemfx -> memf", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[205] = new Rule(205, true, false, 76, "205: regmemf -> memf", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[92] = new Rule(92, true, false, 7, "92: regdx -> memd", ImList.list(ImList.list("movsd","$1","$0")), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-F64X*", null});
    rulev[199] = new Rule(199, true, false, 73, "199: regmemdx -> memd", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[203] = new Rule(203, true, false, 75, "203: regmemd -> memd", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[66] = new Rule(66, true, false, 40, "66: mrcl -> rcl", null, null, null, 0, false, false, new int[]{37}, new String[]{null, null});
    rulev[70] = new Rule(70, true, false, 42, "70: mrcw -> rcw", null, null, null, 0, false, false, new int[]{38}, new String[]{null, null});
    rulev[74] = new Rule(74, true, false, 44, "74: mrcb -> rcb", null, null, null, 0, false, false, new int[]{39}, new String[]{null, null});
    rulev[85] = new Rule(85, true, false, 2, "85: regl -> mrcl", ImList.list(ImList.list("movl","$1","$0")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I32*", null});
    rulev[86] = new Rule(86, true, false, 4, "86: regw -> mrcw", ImList.list(ImList.list("movw","$1","$0")), null, null, 0, false, false, new int[]{42}, new String[]{"*reg-I16*", null});
    rulev[87] = new Rule(87, true, false, 5, "87: regb -> mrcb", ImList.list(ImList.list("movb","$1","$0")), null, null, 0, false, false, new int[]{44}, new String[]{"*reg-I8*", null});
    rulev[79] = new Rule(79, true, false, 47, "79: mrcq -> mregq", null, null, null, 0, false, false, new int[]{46}, new String[]{null, null});
    rulev[84] = new Rule(84, true, false, 1, "84: regq -> mrcq", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),ImList.list("qlow","$0")),ImList.list("movl",ImList.list("qhigh","$1"),ImList.list("qhigh","$0"))), null, null, 0, true, false, new int[]{47}, new String[]{"*reg-I64*", null});
    rulev[25] = new Rule(25, false, false, 19, "25: con -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[38] = new Rule(38, false, true, 25, "38: _1 -> (INTCONST I32 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[40] = new Rule(40, false, true, 26, "40: _2 -> (INTCONST I32 4)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[42] = new Rule(42, false, true, 27, "42: _3 -> (INTCONST I32 8)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[44] = new Rule(44, false, true, 28, "44: _4 -> (INTCONST I32 1)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[47] = new Rule(47, false, true, 29, "47: _5 -> (INTCONST I32 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[26] = new Rule(26, false, false, 20, "26: sta -> (STATIC I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 11, "1: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 12, "3: xregw -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 13, "5: xregl -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[112] = new Rule(112, false, true, 49, "112: _6 -> (REG I32 \"%esp\")", null, null, null, 0, false, false, new int[]{}, null);
    rulev[9] = new Rule(9, false, false, 15, "9: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[13] = new Rule(13, false, false, 17, "13: xregfx -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 14, "7: xregq -> (REG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[11] = new Rule(11, false, false, 16, "11: xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[15] = new Rule(15, false, false, 18, "15: xregdx -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 11, "2: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[4] = new Rule(4, false, false, 12, "4: xregw -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 13, "6: xregl -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[10] = new Rule(10, false, false, 15, "10: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[14] = new Rule(14, false, false, 17, "14: xregfx -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 14, "8: xregq -> (SUBREG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 16, "12: xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[16] = new Rule(16, false, false, 18, "16: xregdx -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[31] = new Rule(31, false, false, 22, "31: lab -> (LABEL I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[184] = new Rule(184, false, false, 2, "184: regl -> (NEG I32 regl)", ImList.list(ImList.list("negl","$0")), null, null, 2, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[215] = new Rule(215, false, false, 6, "215: regfx -> (NEG F32 regfx)", ImList.list(ImList.list("movss","$1","%xmm7"),ImList.list("pxor","$0","$0"),ImList.list("subss","%xmm7","$0")), null, ImList.list(ImList.list("REG","F64","%xmm7")), 2, false, false, new int[]{6}, new String[]{"*reg-F32X*", "*reg-F32X*"});
    rulev[225] = new Rule(225, false, false, 8, "225: regf -> (NEG F32 regf)", ImList.list(ImList.list("fchs","$1","$0")), null, null, 0, false, false, new int[]{8}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*"});
    rulev[151] = new Rule(151, false, false, 1, "151: regq -> (NEG I64 regq)", ImList.list(ImList.list("negl",ImList.list("qlow","$0")),ImList.list("adcl",ImList.list("imm","0"),ImList.list("qhigh","$0")),ImList.list("negl",ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[214] = new Rule(214, false, false, 7, "214: regdx -> (NEG F64 regdx)", ImList.list(ImList.list("movsd","$1","%xmm7"),ImList.list("pxor","$0","$0"),ImList.list("subsd","%xmm7","$0")), null, ImList.list(ImList.list("REG","F64","%xmm7")), 2, false, false, new int[]{7}, new String[]{"*reg-F64X*", "*reg-F64X*"});
    rulev[220] = new Rule(220, false, false, 9, "220: regd -> (NEG F64 regd)", ImList.list(ImList.list("fchs","$1","$0")), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*"});
    rulev[29] = new Rule(29, false, false, 21, "29: asmcon -> (ADD I32 asmcon con)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{21,19}, new String[]{null, null, null});
    rulev[34] = new Rule(34, false, false, 23, "34: base -> (ADD I32 regl asmcon)", null, ImList.list(ImList.list("base","$2","$1")), null, 0, false, false, new int[]{2,21}, new String[]{null, "*reg-I32*", null});
    rulev[35] = new Rule(35, false, false, 23, "35: base -> (ADD I32 asmcon regl)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{21,2}, new String[]{null, null, "*reg-I32*"});
    rulev[51] = new Rule(51, false, false, 30, "51: addr -> (ADD I32 base index)", null, ImList.list(ImList.list("addr","$1","$2")), null, 0, false, false, new int[]{23,24}, new String[]{null, null, null});
    rulev[52] = new Rule(52, false, false, 30, "52: addr -> (ADD I32 index base)", null, ImList.list(ImList.list("addr","$2","$1")), null, 0, false, false, new int[]{24,23}, new String[]{null, null, null});
    rulev[129] = new Rule(129, false, true, 62, "129: _19 -> (ADD I32 _6 _2)", null, null, null, 0, false, false, new int[]{49,26}, null);
  }
  static private void rrinit100() {
    rulev[175] = new Rule(175, false, false, 2, "175: regl -> (ADD I32 regl mrcl)", ImList.list(ImList.list("addl","$2","$0")), null, null, 2, false, false, new int[]{2,40}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[180] = new Rule(180, false, false, 2, "180: regl -> (ADD I32 mrcl regl)", ImList.list(ImList.list("addl","$1","$0")), null, null, 4, false, false, new int[]{40,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[210] = new Rule(210, false, false, 6, "210: regfx -> (ADD F32 regfx regmemfx)", ImList.list(ImList.list("addss","$2","$0")), null, null, 2, false, false, new int[]{6,74}, new String[]{"*reg-F32X*", "*reg-F32X*", null});
    rulev[221] = new Rule(221, false, false, 8, "221: regf -> (ADD F32 regf regmemf)", ImList.list(ImList.list("fadd","$2","$1","$0")), null, null, 0, false, false, new int[]{8,76}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[146] = new Rule(146, false, false, 1, "146: regq -> (ADD I64 regq mrcq)", ImList.list(ImList.list("addl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("adcl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,47}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[206] = new Rule(206, false, false, 7, "206: regdx -> (ADD F64 regdx regmemdx)", ImList.list(ImList.list("addsd","$2","$0")), null, null, 2, false, false, new int[]{7,73}, new String[]{"*reg-F64X*", "*reg-F64X*", null});
    rulev[216] = new Rule(216, false, false, 9, "216: regd -> (ADD F64 regd regmemd)", ImList.list(ImList.list("fadd","$2","$1","$0")), null, null, 0, false, false, new int[]{9,75}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[30] = new Rule(30, false, false, 21, "30: asmcon -> (SUB I32 asmcon con)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{21,19}, new String[]{null, null, null});
    rulev[36] = new Rule(36, false, false, 23, "36: base -> (SUB I32 regl con)", null, ImList.list(ImList.list("base",ImList.list("minus","$2"),"$1")), null, 0, false, false, new int[]{2,19}, new String[]{null, "*reg-I32*", null});
    rulev[113] = new Rule(113, false, true, 50, "113: _7 -> (SUB I32 _6 regl)", null, null, null, 0, false, false, new int[]{49,2}, null);
    rulev[115] = new Rule(115, false, true, 51, "115: _8 -> (SUB I32 _6 _3)", null, null, null, 0, false, false, new int[]{49,27}, null);
    rulev[120] = new Rule(120, false, true, 55, "120: _12 -> (SUB I32 _6 _2)", null, null, null, 0, false, false, new int[]{49,26}, null);
    rulev[176] = new Rule(176, false, false, 2, "176: regl -> (SUB I32 regl mrcl)", ImList.list(ImList.list("subl","$2","$0")), null, null, 2, false, false, new int[]{2,40}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[211] = new Rule(211, false, false, 6, "211: regfx -> (SUB F32 regfx regmemfx)", ImList.list(ImList.list("subss","$2","$0")), null, null, 2, false, false, new int[]{6,74}, new String[]{"*reg-F32X*", "*reg-F32X*", null});
    rulev[222] = new Rule(222, false, false, 8, "222: regf -> (SUB F32 regf regmemf)", ImList.list(ImList.list("fsub","$2","$1","$0")), null, null, 0, false, false, new int[]{8,76}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[147] = new Rule(147, false, false, 1, "147: regq -> (SUB I64 regq mrcq)", ImList.list(ImList.list("subl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("sbbl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,47}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[207] = new Rule(207, false, false, 7, "207: regdx -> (SUB F64 regdx regmemdx)", ImList.list(ImList.list("subsd","$2","$0")), null, null, 2, false, false, new int[]{7,73}, new String[]{"*reg-F64X*", "*reg-F64X*", null});
    rulev[217] = new Rule(217, false, false, 9, "217: regd -> (SUB F64 regd regmemd)", ImList.list(ImList.list("fsub","$2","$1","$0")), null, null, 0, false, false, new int[]{9,75}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[39] = new Rule(39, false, false, 24, "39: index -> (MUL I32 regl _1)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{2,25}, new String[]{null, "*reg-I32*"});
    rulev[41] = new Rule(41, false, false, 24, "41: index -> (MUL I32 regl _2)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{2,26}, new String[]{null, "*reg-I32*"});
    rulev[43] = new Rule(43, false, false, 24, "43: index -> (MUL I32 regl _3)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{2,27}, new String[]{null, "*reg-I32*"});
    rulev[192] = new Rule(192, false, false, 2, "192: regl -> (MUL I32 regl mrcl)", ImList.list(ImList.list("imull","$2","$0")), null, null, 2, false, false, new int[]{2,40}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[193] = new Rule(193, false, false, 2, "193: regl -> (MUL I32 mrcl regl)", ImList.list(ImList.list("imull","$1","$0")), null, null, 4, false, false, new int[]{40,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[212] = new Rule(212, false, false, 6, "212: regfx -> (MUL F32 regfx regmemfx)", ImList.list(ImList.list("mulss","$2","$0")), null, null, 2, false, false, new int[]{6,74}, new String[]{"*reg-F32X*", "*reg-F32X*", null});
    rulev[223] = new Rule(223, false, false, 8, "223: regf -> (MUL F32 regf regmemf)", ImList.list(ImList.list("fmul","$2","$1","$0")), null, null, 0, false, false, new int[]{8,76}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[170] = new Rule(170, false, false, 1, "170: regq -> (MUL I64 regq regq)", new ImList(ImList.list("movl",ImList.list("qhigh","$2"),"%ebx"), ImList.list(ImList.list("imull","%eax","%ebx"),ImList.list("imull",ImList.list("qlow","$2"),"%edx"),ImList.list("addl","%edx","%ebx"),ImList.list("mull",ImList.list("qlow","$2")),ImList.list("addl","%ebx","%edx"))), null, ImList.list(ImList.list("REG","I32","%ebx")), 0, false, false, new int[]{1,1}, new String[]{"*reg-edxeax-I64*", "*reg-edxeax-I64*", "*reg-I64*"});
    rulev[208] = new Rule(208, false, false, 7, "208: regdx -> (MUL F64 regdx regmemdx)", ImList.list(ImList.list("mulsd","$2","$0")), null, null, 2, false, false, new int[]{7,73}, new String[]{"*reg-F64X*", "*reg-F64X*", null});
    rulev[218] = new Rule(218, false, false, 9, "218: regd -> (MUL F64 regd regmemd)", ImList.list(ImList.list("fmul","$2","$1","$0")), null, null, 0, false, false, new int[]{9,75}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[194] = new Rule(194, false, false, 2, "194: regl -> (DIVS I32 regl regl)", ImList.list(ImList.list("cdq"),ImList.list("idivl","$2")), null, ImList.list(ImList.list("REG","I32","%edx")), 2, false, false, new int[]{2,2}, new String[]{"*reg-eax-I32*", "*reg-I32*", "*reg-mod$2-I32*"});
    rulev[213] = new Rule(213, false, false, 6, "213: regfx -> (DIVS F32 regfx regmemfx)", ImList.list(ImList.list("divss","$2","$0")), null, null, 2, false, false, new int[]{6,74}, new String[]{"*reg-F32X*", "*reg-F32X*", null});
    rulev[224] = new Rule(224, false, false, 8, "224: regf -> (DIVS F32 regf regmemf)", ImList.list(ImList.list("fdiv","$2","$1","$0")), null, null, 0, false, false, new int[]{8,76}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F32*", null});
    rulev[171] = new Rule(171, false, false, 1, "171: regq -> (DIVS I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__divdi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{47,47}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[209] = new Rule(209, false, false, 7, "209: regdx -> (DIVS F64 regdx regmemdx)", ImList.list(ImList.list("divsd","$2","$0")), null, null, 2, false, false, new int[]{7,73}, new String[]{"*reg-F64X*", "*reg-F64X*", null});
    rulev[219] = new Rule(219, false, false, 9, "219: regd -> (DIVS F64 regd regmemd)", ImList.list(ImList.list("fdiv","$2","$1","$0")), null, null, 0, false, false, new int[]{9,75}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F64*", null});
    rulev[195] = new Rule(195, false, false, 2, "195: regl -> (DIVU I32 regl regl)", ImList.list(ImList.list("xorl","%edx","%edx"),ImList.list("divl","$2")), null, ImList.list(ImList.list("REG","I32","%edx")), 2, false, false, new int[]{2,2}, new String[]{"*reg-eax-I32*", "*reg-I32*", "*reg-mod$2-I32*"});
    rulev[172] = new Rule(172, false, false, 1, "172: regq -> (DIVU I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__udivdi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{47,47}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[196] = new Rule(196, false, false, 2, "196: regl -> (MODS I32 regl regl)", ImList.list(ImList.list("cdq"),ImList.list("idivl","$2")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{2,2}, new String[]{"*reg-edx-I32*", "*reg-eax-I32*", "*reg-mod$2-I32*"});
    rulev[173] = new Rule(173, false, false, 1, "173: regq -> (MODS I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__moddi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{47,47}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[197] = new Rule(197, false, false, 2, "197: regl -> (MODU I32 regl regl)", ImList.list(ImList.list("xorl","%edx","%edx"),ImList.list("divl","$2")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{2,2}, new String[]{"*reg-edx-I32*", "*reg-eax-I32*", "*reg-mod$2-I32*"});
    rulev[174] = new Rule(174, false, false, 1, "174: regq -> (MODU I64 mrcq mrcq)", new ImList(ImList.list("pushl",ImList.list("qhigh","$2")), ImList.list(ImList.list("pushl",ImList.list("qlow","$2")),ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1")),ImList.list("call",ImList.list("symbol","__umoddi3")),ImList.list("addl",ImList.list("imm","16"),"%esp"))), null, null, 0, false, false, new int[]{47,47}, new String[]{"*reg-edxeax-I64*", null, null});
    rulev[233] = new Rule(233, false, false, 4, "233: regw -> (CONVSX I16 mregb)", ImList.list(ImList.list("movsbw","$1","$0")), null, null, 0, false, false, new int[]{45}, new String[]{"*reg-I16*", null});
    rulev[231] = new Rule(231, false, false, 2, "231: regl -> (CONVSX I32 mregw)", ImList.list(ImList.list("movswl","$1","$0")), null, null, 0, false, false, new int[]{43}, new String[]{"*reg-I32*", null});
    rulev[232] = new Rule(232, false, false, 2, "232: regl -> (CONVSX I32 mregb)", ImList.list(ImList.list("movsbl","$1","$0")), null, null, 0, false, false, new int[]{45}, new String[]{"*reg-I32*", null});
    rulev[228] = new Rule(228, false, false, 1, "228: regq -> (CONVSX I64 regl)", ImList.list(ImList.list("cdq")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-edxeax-I64*", "*reg-eax-I32*"});
    rulev[229] = new Rule(229, false, false, 1, "229: regq -> (CONVSX I64 regw)", ImList.list(ImList.list("cwde"),ImList.list("cdq")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-edxeax-I64*", "*reg-ax-I16*"});
    rulev[230] = new Rule(230, false, false, 1, "230: regq -> (CONVSX I64 regb)", ImList.list(ImList.list("movsbl","%al","%eax"),ImList.list("cdq")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-edxeax-I64*", "*reg-al-I8*"});
    rulev[240] = new Rule(240, false, false, 4, "240: regw -> (CONVZX I16 mregb)", ImList.list(ImList.list("movzbw","$1","$0")), null, null, 0, false, false, new int[]{45}, new String[]{"*reg-I16*", null});
    rulev[238] = new Rule(238, false, false, 2, "238: regl -> (CONVZX I32 mregw)", ImList.list(ImList.list("movzwl","$1","$0")), null, null, 0, false, false, new int[]{43}, new String[]{"*reg-I32*", null});
    rulev[239] = new Rule(239, false, false, 2, "239: regl -> (CONVZX I32 mregb)", ImList.list(ImList.list("movzbl","$1","$0")), null, null, 0, false, false, new int[]{45}, new String[]{"*reg-I32*", null});
    rulev[234] = new Rule(234, false, false, 1, "234: regq -> (CONVZX I64 mregl)", ImList.list(ImList.list("movl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I64*", null});
    rulev[235] = new Rule(235, false, false, 1, "235: regq -> (CONVZX I64 mregw)", ImList.list(ImList.list("movzwl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{43}, new String[]{"*reg-I64*", null});
    rulev[236] = new Rule(236, false, false, 1, "236: regq -> (CONVZX I64 mregb)", ImList.list(ImList.list("movzwl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{45}, new String[]{"*reg-I64*", null});
    rulev[237] = new Rule(237, false, false, 1, "237: regq -> (CONVZX I64 mregb)", ImList.list(ImList.list("movl","$1",ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 0, false, false, new int[]{45}, new String[]{"*reg-I64*", null});
    rulev[243] = new Rule(243, false, false, 5, "243: regb -> (CONVIT I8 regq)", ImList.list(ImList.list("movb",ImList.list("regblow",ImList.list("qlow","$1")),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-bytable-I64*"});
    rulev[245] = new Rule(245, false, false, 5, "245: regb -> (CONVIT I8 regl)", ImList.list(ImList.list("movb",ImList.list("regblow","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-bytable-I32*"});
    rulev[246] = new Rule(246, false, false, 5, "246: regb -> (CONVIT I8 regw)", ImList.list(ImList.list("movb",ImList.list("regblow","$1"),"$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I8*", "*reg-bytable-I16*"});
    rulev[242] = new Rule(242, false, false, 4, "242: regw -> (CONVIT I16 regq)", ImList.list(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$1")),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I64*"});
    rulev[244] = new Rule(244, false, false, 4, "244: regw -> (CONVIT I16 regl)", ImList.list(ImList.list("movw",ImList.list("regwlow","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[241] = new Rule(241, false, false, 2, "241: regl -> (CONVIT I32 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I64*"});
    rulev[253] = new Rule(253, false, false, 7, "253: regdx -> (CONVFX F64 regfx)", ImList.list(ImList.list("cvtss2sd","$1","$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F64X*", "*reg-F32X*"});
    rulev[267] = new Rule(267, false, false, 9, "267: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("fmov","$1","$0")), null, null, 0, false, false, new int[]{8}, new String[]{"*reg-tmp-F64*", "*reg-tmp-F32*"});
    rulev[254] = new Rule(254, false, false, 6, "254: regfx -> (CONVFT F32 regdx)", ImList.list(ImList.list("cvtsd2ss","$1","$0")), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-F32X*", "*reg-F64X*"});
    rulev[268] = new Rule(268, false, false, 8, "268: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("fstps","$1",ImList.list("reserve-tmpl")),ImList.list("flds",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-tmp-F32*", "*reg-tmp-F64*"});
    rulev[269] = new Rule(269, false, false, 5, "269: regb -> (CONVFS I8 regfx)", ImList.list(ImList.list("cvttss2si","$1","%eax"),ImList.list("movb","%al","$0")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{6}, new String[]{"*reg-I8*", "*reg-F32X*"});
    rulev[270] = new Rule(270, false, false, 5, "270: regb -> (CONVFS I8 regdx)", ImList.list(ImList.list("cvttsd2si","$1","%eax"),ImList.list("movb","%al","$0")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{7}, new String[]{"*reg-I8*", "*reg-F64X*"});
    rulev[278] = new Rule(278, false, false, 5, "278: regb -> (CONVFS I8 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"%ax"), ImList.list(ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movb",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{9}, new String[]{"*reg-I8*", "*reg-tmp-F64*"});
    rulev[288] = new Rule(288, false, false, 5, "288: regb -> (CONVFS I8 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"%ax"), ImList.list(ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movb",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{8}, new String[]{"*reg-I8*", "*reg-tmp-F32*"});
    rulev[271] = new Rule(271, false, false, 4, "271: regw -> (CONVFS I16 regfx)", ImList.list(ImList.list("cvttss2si","$1","%eax"),ImList.list("movw","%ax","$0")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{6}, new String[]{"*reg-I16*", "*reg-F32X*"});
    rulev[272] = new Rule(272, false, false, 4, "272: regw -> (CONVFS I16 regdx)", ImList.list(ImList.list("cvttsd2si","$1","%eax"),ImList.list("movb","%ax","$0")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{7}, new String[]{"*reg-I16*", "*reg-F64X*"});
    rulev[277] = new Rule(277, false, false, 4, "277: regw -> (CONVFS I16 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"$0"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"$0"), ImList.list(ImList.list("movw","$0",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movw",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I16*", "*reg-tmp-F64*"});
    rulev[283] = new Rule(283, false, true, 79, "283: _31 -> (CONVFS I16 regd)", null, null, null, 0, false, false, new int[]{9}, null);
    rulev[287] = new Rule(287, false, false, 4, "287: regw -> (CONVFS I16 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"$0"), new ImList(ImList.list("orw",ImList.list("imm","3072"),"$0"), ImList.list(ImList.list("movw","$0",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movw",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I16*", "*reg-tmp-F32*"});
    rulev[293] = new Rule(293, false, true, 82, "293: _34 -> (CONVFS I16 regf)", null, null, null, 0, false, false, new int[]{8}, null);
    rulev[273] = new Rule(273, false, false, 2, "273: regl -> (CONVFS I32 regfx)", ImList.list(ImList.list("cvttss2si","$1","$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-F32X*"});
    rulev[274] = new Rule(274, false, false, 2, "274: regl -> (CONVFS I32 regdx)", ImList.list(ImList.list("cvttsd2si","$1","$0")), null, null, 0, false, false, new int[]{7}, new String[]{"*reg-I32*", "*reg-F64X*"});
    rulev[276] = new Rule(276, false, false, 2, "276: regl -> (CONVFS I32 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow","$0")), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow","$0")), ImList.list(ImList.list("movw",ImList.list("regwlow","$0"),ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movl",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I32*", "*reg-tmp-F64*"});
    rulev[281] = new Rule(281, false, true, 78, "281: _30 -> (CONVFS I32 regd)", null, null, null, 0, false, false, new int[]{9}, null);
    rulev[286] = new Rule(286, false, false, 2, "286: regl -> (CONVFS I32 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow","$0")), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow","$0")), ImList.list(ImList.list("movw",ImList.list("regwlow","$0"),ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$1",ImList.list("reserve-tmpl")),ImList.list("movl",ImList.list("reserve-tmpl"),"$0"),ImList.list("fldcw",ImList.list("reserve-cw0")))))), null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I32*", "*reg-tmp-F32*"});
    rulev[291] = new Rule(291, false, true, 81, "291: _33 -> (CONVFS I32 regf)", null, null, null, 0, false, false, new int[]{8}, null);
    rulev[275] = new Rule(275, false, false, 1, "275: regq -> (CONVFS I64 regd)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$0")),ImList.list("reserve-cw1")), ImList.list(ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$1",ImList.list("reserve-tmpq")),ImList.list("movl",ImList.list("qlow",ImList.list("reserve-tmpq")),ImList.list("qlow","$0")),ImList.list("movl",ImList.list("qhigh",ImList.list("reserve-tmpq")),ImList.list("qhigh","$0")),ImList.list("fldcw",ImList.list("reserve-cw0"))))))), null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I64*", "*reg-tmp-F64*"});
    rulev[279] = new Rule(279, false, true, 77, "279: _29 -> (CONVFS I64 regd)", null, null, null, 0, false, false, new int[]{9}, null);
    rulev[285] = new Rule(285, false, false, 1, "285: regq -> (CONVFS I64 regf)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("orw",ImList.list("imm","3072"),ImList.list("regwlow",ImList.list("qlow","$0"))), new ImList(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$0")),ImList.list("reserve-cw1")), ImList.list(ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$1",ImList.list("reserve-tmpq")),ImList.list("movl",ImList.list("qlow",ImList.list("reserve-tmpq")),ImList.list("qlow","$0")),ImList.list("movl",ImList.list("qhigh",ImList.list("reserve-tmpq")),ImList.list("qhigh","$0")),ImList.list("fldcw",ImList.list("reserve-cw0"))))))), null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I64*", "*reg-tmp-F32*"});
    rulev[289] = new Rule(289, false, true, 80, "289: _32 -> (CONVFS I64 regf)", null, null, null, 0, false, false, new int[]{8}, null);
    rulev[250] = new Rule(250, false, false, 6, "250: regfx -> (CONVSF F32 meml)", ImList.list(ImList.list("cvtsi2ss","$1","$0")), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-F32X*", null});
    rulev[251] = new Rule(251, false, false, 6, "251: regfx -> (CONVSF F32 reglb)", ImList.list(ImList.list("cvtsi2ss","$1","$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-F32X*", "*reg-I32*"});
    rulev[252] = new Rule(252, false, false, 6, "252: regfx -> (CONVSF F32 memw)", ImList.list(ImList.list("cvtsi2ss","$1","$0")), null, null, 0, false, false, new int[]{33}, new String[]{"*reg-F32X*", null});
    rulev[261] = new Rule(261, false, false, 8, "261: regf -> (CONVSF F32 memq)", ImList.list(ImList.list("fildll","$1","$0")), null, null, 0, false, false, new int[]{31}, new String[]{"*reg-tmp-F32*", null});
    rulev[262] = new Rule(262, false, false, 8, "262: regf -> (CONVSF F32 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),ImList.list("qlow",ImList.list("reserve-tmpq"))),ImList.list("movl",ImList.list("qhigh","$1"),ImList.list("qhigh",ImList.list("reserve-tmpq"))),ImList.list("fildll",ImList.list("reserve-tmpq"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-tmp-F32*", "*reg-I64*"});
    rulev[263] = new Rule(263, false, false, 8, "263: regf -> (CONVSF F32 meml)", ImList.list(ImList.list("fildl","$1","$0")), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-tmp-F32*", null});
    rulev[264] = new Rule(264, false, false, 8, "264: regf -> (CONVSF F32 reglb)", ImList.list(ImList.list("movl","$1",ImList.list("reserve-tmpl")),ImList.list("fildl",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-tmp-F32*", "*reg-I32*"});
    rulev[265] = new Rule(265, false, false, 8, "265: regf -> (CONVSF F32 memw)", ImList.list(ImList.list("filds","$1","$0")), null, null, 0, false, false, new int[]{33}, new String[]{"*reg-tmp-F32*", null});
    rulev[266] = new Rule(266, false, false, 8, "266: regf -> (CONVSF F32 regw)", ImList.list(ImList.list("movw","$1",ImList.list("reserve-tmpl")),ImList.list("filds",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-tmp-F32*", "*reg-I16*"});
    rulev[247] = new Rule(247, false, false, 7, "247: regdx -> (CONVSF F64 meml)", ImList.list(ImList.list("cvtsi2sd","$1","$0")), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-F64X*", null});
    rulev[248] = new Rule(248, false, false, 7, "248: regdx -> (CONVSF F64 reglb)", ImList.list(ImList.list("cvtsi2sd","$1","$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-F64X*", "*reg-I32*"});
    rulev[249] = new Rule(249, false, false, 7, "249: regdx -> (CONVSF F64 memw)", ImList.list(ImList.list("cvtsi2sd","$1","$0")), null, null, 0, false, false, new int[]{33}, new String[]{"*reg-F64X*", null});
    rulev[255] = new Rule(255, false, false, 9, "255: regd -> (CONVSF F64 memq)", ImList.list(ImList.list("fildll","$1","$0")), null, null, 0, false, false, new int[]{31}, new String[]{"*reg-tmp-F64*", null});
    rulev[256] = new Rule(256, false, false, 9, "256: regd -> (CONVSF F64 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),ImList.list("qlow",ImList.list("reserve-tmpq"))),ImList.list("movl",ImList.list("qhigh","$1"),ImList.list("qhigh",ImList.list("reserve-tmpq"))),ImList.list("fildll",ImList.list("reserve-tmpq"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-tmp-F64*", "*reg-I64*"});
    rulev[257] = new Rule(257, false, false, 9, "257: regd -> (CONVSF F64 meml)", ImList.list(ImList.list("fildl","$1","$0")), null, null, 0, false, false, new int[]{32}, new String[]{"*reg-tmp-F64*", null});
    rulev[258] = new Rule(258, false, false, 9, "258: regd -> (CONVSF F64 reglb)", ImList.list(ImList.list("movl","$1",ImList.list("reserve-tmpl")),ImList.list("fildl",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-tmp-F64*", "*reg-I32*"});
    rulev[259] = new Rule(259, false, false, 9, "259: regd -> (CONVSF F64 memw)", ImList.list(ImList.list("filds","$1","$0")), null, null, 0, false, false, new int[]{33}, new String[]{"*reg-tmp-F64*", null});
  }
  static private void rrinit200() {
    rulev[260] = new Rule(260, false, false, 9, "260: regd -> (CONVSF F64 regw)", ImList.list(ImList.list("movw","$1",ImList.list("reserve-tmpl")),ImList.list("filds",ImList.list("reserve-tmpl"),"$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-tmp-F64*", "*reg-I16*"});
    rulev[177] = new Rule(177, false, false, 2, "177: regl -> (BAND I32 regl mrcl)", ImList.list(ImList.list("andl","$2","$0")), null, null, 2, false, false, new int[]{2,40}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[181] = new Rule(181, false, false, 2, "181: regl -> (BAND I32 mrcl regl)", ImList.list(ImList.list("andl","$1","$0")), null, null, 4, false, false, new int[]{40,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[148] = new Rule(148, false, false, 1, "148: regq -> (BAND I64 regq mrcq)", ImList.list(ImList.list("andl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("andl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,47}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[178] = new Rule(178, false, false, 2, "178: regl -> (BOR I32 regl mrcl)", ImList.list(ImList.list("orl","$2","$0")), null, null, 2, false, false, new int[]{2,40}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[182] = new Rule(182, false, false, 2, "182: regl -> (BOR I32 mrcl regl)", ImList.list(ImList.list("orl","$1","$0")), null, null, 4, false, false, new int[]{40,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[149] = new Rule(149, false, false, 1, "149: regq -> (BOR I64 regq mrcq)", ImList.list(ImList.list("orl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("orl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,47}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[179] = new Rule(179, false, false, 2, "179: regl -> (BXOR I32 regl mrcl)", ImList.list(ImList.list("xorl","$2","$0")), null, null, 2, false, false, new int[]{2,40}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[183] = new Rule(183, false, false, 2, "183: regl -> (BXOR I32 mrcl regl)", ImList.list(ImList.list("xorl","$1","$0")), null, null, 4, false, false, new int[]{40,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[150] = new Rule(150, false, false, 1, "150: regq -> (BXOR I64 regq mrcq)", ImList.list(ImList.list("xorl",ImList.list("qlow","$2"),ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,47}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[185] = new Rule(185, false, false, 2, "185: regl -> (BNOT I32 regl)", ImList.list(ImList.list("notl","$0")), null, null, 2, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[152] = new Rule(152, false, false, 1, "152: regq -> (BNOT I64 regq)", ImList.list(ImList.list("notl",ImList.list("qlow","$0")),ImList.list("notl",ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[45] = new Rule(45, false, false, 24, "45: index -> (LSHS I32 regl _4)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{2,28}, new String[]{null, "*reg-I32*"});
    rulev[46] = new Rule(46, false, false, 24, "46: index -> (LSHS I32 regl _1)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{2,25}, new String[]{null, "*reg-I32*"});
    rulev[48] = new Rule(48, false, false, 24, "48: index -> (LSHS I32 regl _5)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{2,29}, new String[]{null, "*reg-I32*"});
    rulev[186] = new Rule(186, false, false, 2, "186: regl -> (LSHS I32 regl con)", ImList.list(ImList.list("sall",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,19}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[189] = new Rule(189, false, false, 2, "189: regl -> (LSHS I32 regl shfct)", ImList.list(ImList.list("sall","%cl","$0")), null, null, 2, false, false, new int[]{2,72}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[153] = new Rule(153, false, false, 1, "153: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("shldl",ImList.list("imm","$2"),ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("shll",ImList.list("imm","$2"),ImList.list("qlow","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[154] = new Rule(154, false, false, 1, "154: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("xorl",ImList.list("qlow","$0"),ImList.list("qlow","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[155] = new Rule(155, false, false, 1, "155: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("xorl",ImList.list("qlow","$0"),ImList.list("qlow","$0")),ImList.list("shll",ImList.list("imm",ImList.list("-32","$2")),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[156] = new Rule(156, false, false, 1, "156: regq -> (LSHS I64 regq shfct)", new ImList(ImList.list("shldl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")), new ImList(ImList.list("shll","%cl",ImList.list("qlow","$0")), ImList.list(ImList.list("testb",ImList.list("imm","32"),"%cl"),ImList.list("je","$L1"),ImList.list("movl",ImList.list("qlow","$0"),ImList.list("qhigh","$0")),ImList.list("xorl",ImList.list("qlow","$0"),ImList.list("qlow","$0")),ImList.list("deflabel","$L1")))), null, null, 2, false, false, new int[]{1,72}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[187] = new Rule(187, false, false, 2, "187: regl -> (RSHS I32 regl con)", ImList.list(ImList.list("sarl",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,19}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[190] = new Rule(190, false, false, 2, "190: regl -> (RSHS I32 regl shfct)", ImList.list(ImList.list("sarl","%cl","$0")), null, null, 2, false, false, new int[]{2,72}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[157] = new Rule(157, false, false, 1, "157: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("shrdl",ImList.list("imm","$2"),ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[158] = new Rule(158, false, false, 1, "158: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","31"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[159] = new Rule(159, false, false, 1, "159: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("sarl",ImList.list("imm",ImList.list("-32","$2")),ImList.list("qhigh","$0")),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","31"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[160] = new Rule(160, false, false, 1, "160: regq -> (RSHS I64 regq shfct)", new ImList(ImList.list("shrdl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")), new ImList(ImList.list("sarl","%cl",ImList.list("qhigh","$0")), ImList.list(ImList.list("testb",ImList.list("imm","32"),"%cl"),ImList.list("je","$L1"),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("sarl",ImList.list("imm","31"),ImList.list("qhigh","$0")),ImList.list("deflabel","$L1")))), null, null, 2, false, false, new int[]{1,72}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[188] = new Rule(188, false, false, 2, "188: regl -> (RSHU I32 regl con)", ImList.list(ImList.list("shrl",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,19}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[191] = new Rule(191, false, false, 2, "191: regl -> (RSHU I32 regl shfct)", ImList.list(ImList.list("shrl","%cl","$0")), null, null, 2, false, false, new int[]{2,72}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[161] = new Rule(161, false, false, 1, "161: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("shrdl",ImList.list("imm","$2"),ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("shrl",ImList.list("imm","$2"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[162] = new Rule(162, false, false, 1, "162: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("xor",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[163] = new Rule(163, false, false, 1, "163: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("shrl",ImList.list("imm",ImList.list("-32","$2")),ImList.list("qhigh","$0")),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("xor",ImList.list("qhigh","$0"),ImList.list("qhigh","$0"))), null, null, 2, false, false, new int[]{1,19}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[164] = new Rule(164, false, false, 1, "164: regq -> (RSHU I64 regq shfct)", new ImList(ImList.list("shrdl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")), new ImList(ImList.list("shrl","%cl",ImList.list("qhigh","$0")), ImList.list(ImList.list("testb",ImList.list("imm","32"),"%cl"),ImList.list("je","$L1"),ImList.list("movl",ImList.list("qhigh","$0"),ImList.list("qlow","$0")),ImList.list("xorl",ImList.list("qhigh","$0"),ImList.list("qhigh","$0")),ImList.list("deflabel","$L1")))), null, null, 2, false, false, new int[]{1,72}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[296] = new Rule(296, false, true, 83, "296: _35 -> (TSTEQ I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[316] = new Rule(316, false, true, 93, "316: _45 -> (TSTEQ I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[336] = new Rule(336, false, true, 103, "336: _55 -> (TSTEQ I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[356] = new Rule(356, false, true, 113, "356: _65 -> (TSTEQ I32 regb memb)", null, null, null, 0, false, false, new int[]{5,34}, null);
    rulev[360] = new Rule(360, false, true, 115, "360: _67 -> (TSTEQ I32 regb rcb)", null, null, null, 0, false, false, new int[]{5,39}, null);
    rulev[364] = new Rule(364, false, true, 117, "364: _69 -> (TSTEQ I32 memb regb)", null, null, null, 0, false, false, new int[]{34,5}, null);
    rulev[368] = new Rule(368, false, true, 119, "368: _71 -> (TSTEQ I32 rcb regb)", null, null, null, 0, false, false, new int[]{39,5}, null);
    rulev[372] = new Rule(372, false, true, 121, "372: _73 -> (TSTEQ I32 regdx regmemdx)", null, null, null, 0, false, false, new int[]{7,73}, null);
    rulev[380] = new Rule(380, false, true, 125, "380: _77 -> (TSTEQ I32 regfx regmemfx)", null, null, null, 0, false, false, new int[]{6,74}, null);
    rulev[396] = new Rule(396, false, true, 133, "396: _85 -> (TSTEQ I32 regd regmemd)", null, null, null, 0, false, false, new int[]{9,75}, null);
    rulev[400] = new Rule(400, false, true, 135, "400: _87 -> (TSTEQ I32 regf regmemf)", null, null, null, 0, false, false, new int[]{8,76}, null);
    rulev[298] = new Rule(298, false, true, 84, "298: _36 -> (TSTNE I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[318] = new Rule(318, false, true, 94, "318: _46 -> (TSTNE I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[338] = new Rule(338, false, true, 104, "338: _56 -> (TSTNE I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[358] = new Rule(358, false, true, 114, "358: _66 -> (TSTNE I32 regb memb)", null, null, null, 0, false, false, new int[]{5,34}, null);
    rulev[362] = new Rule(362, false, true, 116, "362: _68 -> (TSTNE I32 regb rcb)", null, null, null, 0, false, false, new int[]{5,39}, null);
    rulev[366] = new Rule(366, false, true, 118, "366: _70 -> (TSTNE I32 memb regb)", null, null, null, 0, false, false, new int[]{34,5}, null);
    rulev[370] = new Rule(370, false, true, 120, "370: _72 -> (TSTNE I32 rcb regb)", null, null, null, 0, false, false, new int[]{39,5}, null);
    rulev[374] = new Rule(374, false, true, 122, "374: _74 -> (TSTNE I32 regdx regmemdx)", null, null, null, 0, false, false, new int[]{7,73}, null);
    rulev[382] = new Rule(382, false, true, 126, "382: _78 -> (TSTNE I32 regfx regmemfx)", null, null, null, 0, false, false, new int[]{6,74}, null);
    rulev[398] = new Rule(398, false, true, 134, "398: _86 -> (TSTNE I32 regd regmemd)", null, null, null, 0, false, false, new int[]{9,75}, null);
    rulev[402] = new Rule(402, false, true, 136, "402: _88 -> (TSTNE I32 regf regmemf)", null, null, null, 0, false, false, new int[]{8,76}, null);
    rulev[300] = new Rule(300, false, true, 85, "300: _37 -> (TSTLTS I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[320] = new Rule(320, false, true, 95, "320: _47 -> (TSTLTS I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[340] = new Rule(340, false, true, 105, "340: _57 -> (TSTLTS I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[376] = new Rule(376, false, true, 123, "376: _75 -> (TSTLTS I32 regdx regmemdx)", null, null, null, 0, false, false, new int[]{7,73}, null);
    rulev[384] = new Rule(384, false, true, 127, "384: _79 -> (TSTLTS I32 regfx regmemfx)", null, null, null, 0, false, false, new int[]{6,74}, null);
    rulev[404] = new Rule(404, false, true, 137, "404: _89 -> (TSTLTS I32 regd regmemd)", null, null, null, 0, false, false, new int[]{9,75}, null);
    rulev[408] = new Rule(408, false, true, 139, "408: _91 -> (TSTLTS I32 regf regmemf)", null, null, null, 0, false, false, new int[]{8,76}, null);
    rulev[302] = new Rule(302, false, true, 86, "302: _38 -> (TSTLES I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[322] = new Rule(322, false, true, 96, "322: _48 -> (TSTLES I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[342] = new Rule(342, false, true, 106, "342: _58 -> (TSTLES I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[378] = new Rule(378, false, true, 124, "378: _76 -> (TSTLES I32 regdx regmemdx)", null, null, null, 0, false, false, new int[]{7,73}, null);
    rulev[386] = new Rule(386, false, true, 128, "386: _80 -> (TSTLES I32 regfx regmemfx)", null, null, null, 0, false, false, new int[]{6,74}, null);
    rulev[414] = new Rule(414, false, true, 142, "414: _94 -> (TSTLES I32 regd regmemd)", null, null, null, 0, false, false, new int[]{9,75}, null);
    rulev[418] = new Rule(418, false, true, 144, "418: _96 -> (TSTLES I32 regf regmemf)", null, null, null, 0, false, false, new int[]{8,76}, null);
    rulev[304] = new Rule(304, false, true, 87, "304: _39 -> (TSTGTS I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[324] = new Rule(324, false, true, 97, "324: _49 -> (TSTGTS I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[344] = new Rule(344, false, true, 107, "344: _59 -> (TSTGTS I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[388] = new Rule(388, false, true, 129, "388: _81 -> (TSTGTS I32 regdx regmemdx)", null, null, null, 0, false, false, new int[]{7,73}, null);
    rulev[392] = new Rule(392, false, true, 131, "392: _83 -> (TSTGTS I32 regfx regmemfx)", null, null, null, 0, false, false, new int[]{6,74}, null);
    rulev[412] = new Rule(412, false, true, 141, "412: _93 -> (TSTGTS I32 regd regmemd)", null, null, null, 0, false, false, new int[]{9,75}, null);
    rulev[416] = new Rule(416, false, true, 143, "416: _95 -> (TSTGTS I32 regf regmemf)", null, null, null, 0, false, false, new int[]{8,76}, null);
    rulev[306] = new Rule(306, false, true, 88, "306: _40 -> (TSTGES I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[326] = new Rule(326, false, true, 98, "326: _50 -> (TSTGES I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[346] = new Rule(346, false, true, 108, "346: _60 -> (TSTGES I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[390] = new Rule(390, false, true, 130, "390: _82 -> (TSTGES I32 regdx regmemdx)", null, null, null, 0, false, false, new int[]{7,73}, null);
    rulev[394] = new Rule(394, false, true, 132, "394: _84 -> (TSTGES I32 regfx regmemfx)", null, null, null, 0, false, false, new int[]{6,74}, null);
    rulev[406] = new Rule(406, false, true, 138, "406: _90 -> (TSTGES I32 regd regmemd)", null, null, null, 0, false, false, new int[]{9,75}, null);
    rulev[410] = new Rule(410, false, true, 140, "410: _92 -> (TSTGES I32 regf regmemf)", null, null, null, 0, false, false, new int[]{8,76}, null);
    rulev[308] = new Rule(308, false, true, 89, "308: _41 -> (TSTLTU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[328] = new Rule(328, false, true, 99, "328: _51 -> (TSTLTU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[348] = new Rule(348, false, true, 109, "348: _61 -> (TSTLTU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[310] = new Rule(310, false, true, 90, "310: _42 -> (TSTLEU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[330] = new Rule(330, false, true, 100, "330: _52 -> (TSTLEU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[350] = new Rule(350, false, true, 110, "350: _62 -> (TSTLEU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[312] = new Rule(312, false, true, 91, "312: _43 -> (TSTGTU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[332] = new Rule(332, false, true, 101, "332: _53 -> (TSTGTU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[352] = new Rule(352, false, true, 111, "352: _63 -> (TSTGTU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[314] = new Rule(314, false, true, 92, "314: _44 -> (TSTGEU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,47}, null);
    rulev[334] = new Rule(334, false, true, 102, "334: _54 -> (TSTGEU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,40}, null);
    rulev[354] = new Rule(354, false, true, 112, "354: _64 -> (TSTGEU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{40,2}, null);
    rulev[56] = new Rule(56, false, false, 34, "56: memb -> (MEM I8 addr)", null, ImList.list(ImList.list("mem","byte","$1")), null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[55] = new Rule(55, false, false, 33, "55: memw -> (MEM I16 addr)", null, ImList.list(ImList.list("mem","word","$1")), null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[54] = new Rule(54, false, false, 32, "54: meml -> (MEM I32 addr)", null, ImList.list(ImList.list("mem","long","$1")), null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[121] = new Rule(121, false, true, 56, "121: _13 -> (MEM I32 _12)", null, null, null, 0, false, false, new int[]{55}, null);
    rulev[125] = new Rule(125, false, true, 59, "125: _16 -> (MEM I32 _6)", null, null, null, 0, false, false, new int[]{49}, null);
  }
  static private void rrinit300() {
    rulev[57] = new Rule(57, false, false, 35, "57: memf -> (MEM F32 addr)", null, ImList.list(ImList.list("mem","float","$1")), null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[132] = new Rule(132, false, true, 64, "132: _21 -> (MEM F32 _12)", null, null, null, 0, false, false, new int[]{55}, null);
    rulev[53] = new Rule(53, false, false, 31, "53: memq -> (MEM I64 addr)", null, ImList.list(ImList.list("mem","quad","$1")), null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[116] = new Rule(116, false, true, 52, "116: _9 -> (MEM I64 _8)", null, null, null, 0, false, false, new int[]{51}, null);
    rulev[58] = new Rule(58, false, false, 36, "58: memd -> (MEM F64 addr)", null, ImList.list(ImList.list("mem","double","$1")), null, 0, false, false, new int[]{30}, new String[]{null, null});
    rulev[135] = new Rule(135, false, true, 66, "135: _23 -> (MEM F64 _8)", null, null, null, 0, false, false, new int[]{51}, null);
    rulev[97] = new Rule(97, false, false, 10, "97: void -> (SET I8 memb rcb)", ImList.list(ImList.list("movb","$2","$1")), null, null, 0, false, false, new int[]{34,39}, new String[]{null, null, null});
    rulev[101] = new Rule(101, false, false, 10, "101: void -> (SET I8 xregb regb)", ImList.list(ImList.list("movb","$2","$1")), null, null, 0, false, false, new int[]{11,5}, new String[]{null, null, "*reg-I8*"});
    rulev[96] = new Rule(96, false, false, 10, "96: void -> (SET I16 memw rcw)", ImList.list(ImList.list("movw","$2","$1")), null, null, 0, false, false, new int[]{33,38}, new String[]{null, null, null});
    rulev[100] = new Rule(100, false, false, 10, "100: void -> (SET I16 xregw regw)", ImList.list(ImList.list("movw","$2","$1")), null, null, 0, false, false, new int[]{12,4}, new String[]{null, null, "*reg-I16*"});
    rulev[95] = new Rule(95, false, false, 10, "95: void -> (SET I32 meml rcl)", ImList.list(ImList.list("movl","$2","$1")), null, null, 0, false, false, new int[]{32,37}, new String[]{null, null, null});
    rulev[99] = new Rule(99, false, false, 10, "99: void -> (SET I32 xregl regl)", ImList.list(ImList.list("movl","$2","$1")), null, null, 0, false, false, new int[]{13,2}, new String[]{null, null, "*reg-I32*"});
    rulev[114] = new Rule(114, false, false, 10, "114: void -> (SET I32 _6 _7)", ImList.list(ImList.list("call",ImList.list("symbol","_alloca"))), null, null, 0, false, false, new int[]{49,50}, new String[]{null, "*reg-eax-I32*"});
    rulev[118] = new Rule(118, false, true, 54, "118: _11 -> (SET I32 _6 _8)", null, null, null, 0, false, false, new int[]{49,51}, null);
    rulev[122] = new Rule(122, false, true, 57, "122: _14 -> (SET I32 _13 mrcl)", null, null, null, 0, false, false, new int[]{56,40}, null);
    rulev[123] = new Rule(123, false, true, 58, "123: _15 -> (SET I32 _6 _12)", null, null, null, 0, false, false, new int[]{49,55}, null);
    rulev[126] = new Rule(126, false, true, 60, "126: _17 -> (SET I32 _16 regl)", null, null, null, 0, false, false, new int[]{59,2}, null);
    rulev[128] = new Rule(128, false, true, 61, "128: _18 -> (SET I32 regl _16)", null, null, null, 0, false, false, new int[]{2,59}, null);
    rulev[130] = new Rule(130, false, true, 63, "130: _20 -> (SET I32 _6 _19)", null, null, null, 0, false, false, new int[]{49,62}, null);
    rulev[282] = new Rule(282, false, false, 10, "282: void -> (SET I32 meml _30)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{32,78}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[284] = new Rule(284, false, false, 10, "284: void -> (SET I32 memw _31)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistps","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{33,79}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[292] = new Rule(292, false, false, 10, "292: void -> (SET I32 meml _33)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpl","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{32,81}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[294] = new Rule(294, false, false, 10, "294: void -> (SET I32 memw _34)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistps","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{33,82}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[91] = new Rule(91, false, false, 10, "91: void -> (SET F32 regf regfx)", ImList.list(ImList.list("movss","$2",ImList.list("reserve-tmpq")),ImList.list("flds",ImList.list("reserve-tmpq"))), null, null, 0, false, false, new int[]{8,6}, new String[]{null, "*reg-tmp-F32*", "*reg-F32X*"});
    rulev[103] = new Rule(103, false, false, 10, "103: void -> (SET F32 xregfx regfx)", ImList.list(ImList.list("movss","$2","$1")), null, null, 0, false, false, new int[]{17,6}, new String[]{null, null, "*reg-F32X*"});
    rulev[105] = new Rule(105, false, false, 10, "105: void -> (SET F32 xregfx regf)", ImList.list(ImList.list("fstps","$1",ImList.list("reserve-tmpq")),ImList.list("movss",ImList.list("reserve-tmpq"),"$0")), null, null, 0, false, false, new int[]{17,8}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[106] = new Rule(106, false, false, 10, "106: void -> (SET F32 xregf regf)", ImList.list(ImList.list("fmov","$2","$1")), null, null, 0, false, false, new int[]{15,8}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[109] = new Rule(109, false, false, 10, "109: void -> (SET F32 memf regfx)", ImList.list(ImList.list("movss","$2","$1")), null, null, 0, false, false, new int[]{35,6}, new String[]{null, null, "*reg-F32X*"});
    rulev[111] = new Rule(111, false, false, 10, "111: void -> (SET F32 memf regf)", ImList.list(ImList.list("fstp","$2","$1")), null, null, 0, false, false, new int[]{35,8}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[133] = new Rule(133, false, true, 65, "133: _22 -> (SET F32 _21 memf)", null, null, null, 0, false, false, new int[]{64,35}, null);
    rulev[138] = new Rule(138, false, true, 68, "138: _25 -> (SET F32 _21 regfx)", null, null, null, 0, false, false, new int[]{64,6}, null);
    rulev[142] = new Rule(142, false, true, 70, "142: _27 -> (SET F32 _21 regf)", null, null, null, 0, false, false, new int[]{64,8}, null);
    rulev[94] = new Rule(94, false, false, 10, "94: void -> (SET I64 memq regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$2"),"$1"),ImList.list("movl",ImList.list("qhigh","$2"),ImList.list("after","$1","4"))), null, null, 0, false, false, new int[]{31,1}, new String[]{null, null, "*reg-I64*"});
    rulev[98] = new Rule(98, false, false, 10, "98: void -> (SET I64 xregq regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("movl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1"))), null, null, 0, false, false, new int[]{14,1}, new String[]{null, null, "*reg-I64*"});
    rulev[117] = new Rule(117, false, true, 53, "117: _10 -> (SET I64 _9 mrcq)", null, null, null, 0, false, false, new int[]{52,47}, null);
    rulev[280] = new Rule(280, false, false, 10, "280: void -> (SET I64 memq _29)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{31,77}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[290] = new Rule(290, false, false, 10, "290: void -> (SET I64 memq _32)", new ImList(ImList.list("fnstcw",ImList.list("reserve-cw0")), new ImList(ImList.list("movw",ImList.list("reserve-cw0"),"%ax"), ImList.list(ImList.list("orw",ImList.list("imm","3072"),"%ax"),ImList.list("movw","%ax",ImList.list("reserve-cw1")),ImList.list("fldcw",ImList.list("reserve-cw1")),ImList.list("fistpll","$2","$1"),ImList.list("fldcw",ImList.list("reserve-cw0"))))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, true, false, new int[]{31,80}, new String[]{null, null, "*reg-tmp-F32*"});
    rulev[90] = new Rule(90, false, false, 10, "90: void -> (SET F64 regd regdx)", ImList.list(ImList.list("movsd","$2",ImList.list("reserve-tmpq")),ImList.list("fldl",ImList.list("reserve-tmpq"))), null, null, 0, false, false, new int[]{9,7}, new String[]{null, "*reg-tmp-F64*", "*reg-F64X*"});
    rulev[102] = new Rule(102, false, false, 10, "102: void -> (SET F64 xregdx regdx)", ImList.list(ImList.list("movsd","$2","$1")), null, null, 0, false, false, new int[]{18,7}, new String[]{null, null, "*reg-F64X*"});
    rulev[104] = new Rule(104, false, false, 10, "104: void -> (SET F64 xregdx regd)", ImList.list(ImList.list("fstpl",ImList.list("reserve-tmpq")),ImList.list("movsd",ImList.list("reserve-tmpq"),"$0")), null, null, 0, false, false, new int[]{18,9}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[107] = new Rule(107, false, false, 10, "107: void -> (SET F64 xregd regd)", ImList.list(ImList.list("fmov","$2","$1")), null, null, 0, false, false, new int[]{16,9}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[108] = new Rule(108, false, false, 10, "108: void -> (SET F64 memd regdx)", ImList.list(ImList.list("movsd","$2","$1")), null, null, 0, false, false, new int[]{36,7}, new String[]{null, null, "*reg-F64X*"});
    rulev[110] = new Rule(110, false, false, 10, "110: void -> (SET F64 memd regd)", ImList.list(ImList.list("fstp","$2","$1")), null, null, 0, false, false, new int[]{36,9}, new String[]{null, null, "*reg-tmp-F64*"});
    rulev[136] = new Rule(136, false, true, 67, "136: _24 -> (SET F64 _23 memd)", null, null, null, 0, false, false, new int[]{66,36}, null);
    rulev[140] = new Rule(140, false, true, 69, "140: _26 -> (SET F64 _23 regdx)", null, null, null, 0, false, false, new int[]{66,7}, null);
    rulev[144] = new Rule(144, false, true, 71, "144: _28 -> (SET F64 _23 regd)", null, null, null, 0, false, false, new int[]{66,9}, null);
    rulev[295] = new Rule(295, false, false, 10, "295: void -> (JUMP _ lab)", ImList.list(ImList.list("jmp","$1")), null, null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[297] = new Rule(297, false, false, 10, "297: void -> (JUMPC _ _35 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jne","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("je","$3")), null, null, 0, false, false, new int[]{83,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[299] = new Rule(299, false, false, 10, "299: void -> (JUMPC _ _36 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jne","$3"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{84,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[301] = new Rule(301, false, false, 10, "301: void -> (JUMPC _ _37 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jl","$3"),ImList.list("jg","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{85,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[303] = new Rule(303, false, false, 10, "303: void -> (JUMPC _ _38 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jl","$3"),ImList.list("jg","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{86,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[305] = new Rule(305, false, false, 10, "305: void -> (JUMPC _ _39 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jg","$3"),ImList.list("jl","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{87,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[307] = new Rule(307, false, false, 10, "307: void -> (JUMPC _ _40 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jg","$3"),ImList.list("jl","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{88,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[309] = new Rule(309, false, false, 10, "309: void -> (JUMPC _ _41 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jb","$3"),ImList.list("ja","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{89,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[311] = new Rule(311, false, false, 10, "311: void -> (JUMPC _ _42 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("jb","$3"),ImList.list("ja","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{90,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[313] = new Rule(313, false, false, 10, "313: void -> (JUMPC _ _43 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("ja","$3"),ImList.list("jb","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{91,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[315] = new Rule(315, false, false, 10, "315: void -> (JUMPC _ _44 lab lab)", ImList.list(ImList.list("cmpl",ImList.list("qhigh","$2"),ImList.list("qhigh","$1")),ImList.list("ja","$3"),ImList.list("jb","$4"),ImList.list("cmpl",ImList.list("qlow","$2"),ImList.list("qlow","$1")),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{92,22,22}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[317] = new Rule(317, false, false, 10, "317: void -> (JUMPC _ _45 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{93,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[319] = new Rule(319, false, false, 10, "319: void -> (JUMPC _ _46 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{94,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[321] = new Rule(321, false, false, 10, "321: void -> (JUMPC _ _47 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{95,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[323] = new Rule(323, false, false, 10, "323: void -> (JUMPC _ _48 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{96,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[325] = new Rule(325, false, false, 10, "325: void -> (JUMPC _ _49 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{97,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[327] = new Rule(327, false, false, 10, "327: void -> (JUMPC _ _50 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{98,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[329] = new Rule(329, false, false, 10, "329: void -> (JUMPC _ _51 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{99,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[331] = new Rule(331, false, false, 10, "331: void -> (JUMPC _ _52 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{100,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[333] = new Rule(333, false, false, 10, "333: void -> (JUMPC _ _53 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{101,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[335] = new Rule(335, false, false, 10, "335: void -> (JUMPC _ _54 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{102,22,22}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[337] = new Rule(337, false, false, 10, "337: void -> (JUMPC _ _55 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{103,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[339] = new Rule(339, false, false, 10, "339: void -> (JUMPC _ _56 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{104,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[341] = new Rule(341, false, false, 10, "341: void -> (JUMPC _ _57 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{105,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[343] = new Rule(343, false, false, 10, "343: void -> (JUMPC _ _58 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{106,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[345] = new Rule(345, false, false, 10, "345: void -> (JUMPC _ _59 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{107,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[347] = new Rule(347, false, false, 10, "347: void -> (JUMPC _ _60 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{108,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[349] = new Rule(349, false, false, 10, "349: void -> (JUMPC _ _61 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{109,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[351] = new Rule(351, false, false, 10, "351: void -> (JUMPC _ _62 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{110,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[353] = new Rule(353, false, false, 10, "353: void -> (JUMPC _ _63 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{111,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[355] = new Rule(355, false, false, 10, "355: void -> (JUMPC _ _64 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{112,22,22}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[357] = new Rule(357, false, false, 10, "357: void -> (JUMPC _ _65 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{113,22,22}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[359] = new Rule(359, false, false, 10, "359: void -> (JUMPC _ _66 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{114,22,22}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[361] = new Rule(361, false, false, 10, "361: void -> (JUMPC _ _67 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{115,22,22}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[363] = new Rule(363, false, false, 10, "363: void -> (JUMPC _ _68 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{116,22,22}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[365] = new Rule(365, false, false, 10, "365: void -> (JUMPC _ _69 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{117,22,22}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[367] = new Rule(367, false, false, 10, "367: void -> (JUMPC _ _70 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{118,22,22}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[369] = new Rule(369, false, false, 10, "369: void -> (JUMPC _ _71 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{119,22,22}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[371] = new Rule(371, false, false, 10, "371: void -> (JUMPC _ _72 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{120,22,22}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[373] = new Rule(373, false, false, 10, "373: void -> (JUMPC _ _73 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpeqsd","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{121,22,22}, new String[]{null, "*reg-F64X*", null, null, null});
    rulev[375] = new Rule(375, false, false, 10, "375: void -> (JUMPC _ _74 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpneqsd","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{122,22,22}, new String[]{null, "*reg-F64X*", null, null, null});
    rulev[377] = new Rule(377, false, false, 10, "377: void -> (JUMPC _ _75 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpltsd","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{123,22,22}, new String[]{null, "*reg-F64X*", null, null, null});
    rulev[379] = new Rule(379, false, false, 10, "379: void -> (JUMPC _ _76 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmplesd","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{124,22,22}, new String[]{null, "*reg-F64X*", null, null, null});
    rulev[381] = new Rule(381, false, false, 10, "381: void -> (JUMPC _ _77 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpeqss","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{125,22,22}, new String[]{null, "*reg-F32X*", null, null, null});
    rulev[383] = new Rule(383, false, false, 10, "383: void -> (JUMPC _ _78 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpneqss","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{126,22,22}, new String[]{null, "*reg-F32X*", null, null, null});
    rulev[385] = new Rule(385, false, false, 10, "385: void -> (JUMPC _ _79 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpltss","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{127,22,22}, new String[]{null, "*reg-F32X*", null, null, null});
    rulev[387] = new Rule(387, false, false, 10, "387: void -> (JUMPC _ _80 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpless","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{128,22,22}, new String[]{null, "*reg-F32X*", null, null, null});
    rulev[389] = new Rule(389, false, false, 10, "389: void -> (JUMPC _ _81 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpnlesd","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{129,22,22}, new String[]{null, "*reg-F64X*", null, null, null});
    rulev[391] = new Rule(391, false, false, 10, "391: void -> (JUMPC _ _82 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpnltsd","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{130,22,22}, new String[]{null, "*reg-F64X*", null, null, null});
    rulev[393] = new Rule(393, false, false, 10, "393: void -> (JUMPC _ _83 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpnless","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{131,22,22}, new String[]{null, "*reg-F32X*", null, null, null});
    rulev[395] = new Rule(395, false, false, 10, "395: void -> (JUMPC _ _84 lab lab)", new ImList(ImList.list("movq","$1","%xmm7"), ImList.list(ImList.list("cmpnltss","$2","$1"),ImList.list("movd","$1","%eax"),ImList.list("cmp",ImList.list("imm","-1"),"%eax"),ImList.list("movq","%xmm7","$1"),ImList.list("jz","$3"))), null, ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","F64","%xmm7")), 0, false, false, new int[]{132,22,22}, new String[]{null, "*reg-F32X*", null, null, null});
    rulev[397] = new Rule(397, false, false, 10, "397: void -> (JUMPC _ _85 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{133,22,22}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[399] = new Rule(399, false, false, 10, "399: void -> (JUMPC _ _86 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{134,22,22}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[401] = new Rule(401, false, false, 10, "401: void -> (JUMPC _ _87 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{135,22,22}, new String[]{null, "*reg-tmp-F32*", null, null, null});
  }
  static private void rrinit400() {
    rulev[403] = new Rule(403, false, false, 10, "403: void -> (JUMPC _ _88 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("and",ImList.list("imm","69"),"%ah"),ImList.list("cmp",ImList.list("imm","64"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{136,22,22}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[405] = new Rule(405, false, false, 10, "405: void -> (JUMPC _ _89 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{137,22,22}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[407] = new Rule(407, false, false, 10, "407: void -> (JUMPC _ _90 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{138,22,22}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[409] = new Rule(409, false, false, 10, "409: void -> (JUMPC _ _91 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{139,22,22}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[411] = new Rule(411, false, false, 10, "411: void -> (JUMPC _ _92 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("cmpb",ImList.list("imm","1"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{140,22,22}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[413] = new Rule(413, false, false, 10, "413: void -> (JUMPC _ _93 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{141,22,22}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[415] = new Rule(415, false, false, 10, "415: void -> (JUMPC _ _94 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{142,22,22}, new String[]{null, "*reg-tmp-F64*", null, null, null});
    rulev[417] = new Rule(417, false, false, 10, "417: void -> (JUMPC _ _95 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("je","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{143,22,22}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[419] = new Rule(419, false, false, 10, "419: void -> (JUMPC _ _96 lab lab)", ImList.list(ImList.list("fcom","$2","$1"),ImList.list("fnstsw","%ax"),ImList.list("andb",ImList.list("imm","69"),"%ah"),ImList.list("jne","$3")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{144,22,22}, new String[]{null, "*reg-tmp-F32*", null, null, null});
    rulev[420] = new Rule(420, false, false, 10, "420: void -> (CALL _ callarg)", ImList.list(ImList.list("call","$1")), null, null, 0, false, false, new int[]{48}, new String[]{null, null});
    rulev[119] = new Rule(119, false, false, 10, "119: void -> (PARALLEL _ _10 _11)", ImList.list(ImList.list("pushl",ImList.list("qhigh","$1")),ImList.list("pushl",ImList.list("qlow","$1"))), null, null, 0, false, false, new int[]{53,54}, new String[]{null, null});
    rulev[124] = new Rule(124, false, false, 10, "124: void -> (PARALLEL _ _14 _15)", ImList.list(ImList.list("pushl","$1")), null, null, 0, false, false, new int[]{57,58}, new String[]{null, null});
    rulev[127] = new Rule(127, false, false, 10, "127: void -> (PARALLEL _ _17 _15)", ImList.list(ImList.list("_pushl","$1")), null, null, 0, false, false, new int[]{60,58}, new String[]{null, "*reg-I32*"});
    rulev[131] = new Rule(131, false, false, 10, "131: void -> (PARALLEL _ _18 _20)", ImList.list(ImList.list("_popl","$1")), null, null, 0, false, false, new int[]{61,63}, new String[]{null, "*reg-I32*"});
    rulev[134] = new Rule(134, false, false, 10, "134: void -> (PARALLEL _ _22 _15)", ImList.list(ImList.list("pushl","$1")), null, null, 0, false, false, new int[]{65,58}, new String[]{null, null});
    rulev[137] = new Rule(137, false, false, 10, "137: void -> (PARALLEL _ _24 _11)", ImList.list(ImList.list("pushl",ImList.list("after","$1","4")),ImList.list("pushl","$1")), null, null, 0, false, false, new int[]{67,54}, new String[]{null, null});
    rulev[139] = new Rule(139, false, false, 10, "139: void -> (PARALLEL _ _25 _15)", ImList.list(ImList.list("sub",ImList.list("imm","4"),"%esp"),ImList.list("movss","$1",ImList.list("mem","float",ImList.list("addr",ImList.list("base",ImList.list(),"%esp"),ImList.list())))), null, null, 0, false, false, new int[]{68,58}, new String[]{null, "*reg-F32X*"});
    rulev[141] = new Rule(141, false, false, 10, "141: void -> (PARALLEL _ _26 _11)", ImList.list(ImList.list("sub",ImList.list("imm","8"),"%esp"),ImList.list("movsd","$1",ImList.list("mem","double",ImList.list("addr",ImList.list("base",ImList.list(),"%esp"),ImList.list())))), null, null, 0, false, false, new int[]{69,54}, new String[]{null, "*reg-F64X*"});
    rulev[143] = new Rule(143, false, false, 10, "143: void -> (PARALLEL _ _27 _15)", ImList.list(ImList.list("sub",ImList.list("imm","4"),"%esp"),ImList.list("fstp","$1",ImList.list("mem","float",ImList.list("addr",ImList.list("base",ImList.list(),"%esp"),ImList.list())))), null, null, 0, false, false, new int[]{70,58}, new String[]{null, "*reg-tmp-F32*"});
    rulev[145] = new Rule(145, false, false, 10, "145: void -> (PARALLEL _ _28 _11)", ImList.list(ImList.list("sub",ImList.list("imm","8"),"%esp"),ImList.list("fstp","$1",ImList.list("mem","double",ImList.list("addr",ImList.list("base",ImList.list(),"%esp"),ImList.list())))), null, null, 0, false, false, new int[]{71,54}, new String[]{null, "*reg-tmp-F64*"});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 1026: return "*reg-I64*";
    case 514: return "*reg-I32*";
    case 258: return "*reg-I16*";
    case 130: return "*reg-I8*";
    case 1028: return "*reg-F64X*";
    case 516: return "*reg-F32X*";
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
    if (name == "_popl")
      return jmac6(emitObject(form.elem(1)));
    else if (name == "_pushl")
      return jmac7(emitObject(form.elem(1)));
    else if (name == "after")
      return jmac8(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "+")
      return jmac9(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-")
      return jmac10(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-32")
      return jmac11(emitObject(form.elem(1)));
    else if (name == "imm")
      return jmac12(emitObject(form.elem(1)));
    else if (name == "ind")
      return jmac13(emitObject(form.elem(1)));
    else if (name == "mem")
      return jmac14(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "addr")
      return jmac15(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "base")
      return jmac16(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "index")
      return jmac17(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regwlow")
      return jmac18(emitObject(form.elem(1)));
    else if (name == "regblow")
      return jmac19(emitObject(form.elem(1)));
    else if (name == "qlow")
      return jmac20(emitObject(form.elem(1)));
    else if (name == "qhigh")
      return jmac21(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac22(form.elem(1));
    else if (name == "epilogue")
      return jmac23(form.elem(1), emitObject(form.elem(2)));
    else if (name == "minus")
      return jmac24(emitObject(form.elem(1)));
    else if (name == "line")
      return jmac25(emitObject(form.elem(1)));
    else if (name == "symbol")
      return jmac26(emitObject(form.elem(1)));
    else if (name == "deflabel")
      return jmac27(emitObject(form.elem(1)));
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
  
  String currentLine = "0";
  
  ImList regCallClobbers = ImList.list(ImList.list("REG","I32","%eax"),ImList.list("REG","I32","%ecx"),ImList.list("REG","I32","%edx"));
  
  
  /** X86's function attribute **/
  static class X86Attr extends FunctionAttr {
  
    /** Maximum stack space used by call. **/
    int stackRequired;
  
    /** Temporary variable used for int to float conversion **/
    int tmpOffset;
  
    /** alloca called in it **/
    boolean allocaCalled;
  
    /** */
    int stackForXmm;
  
    X86Attr(Function func) {
      super(func);
      stackRequired = 0;
      tmpOffset = 0;
      stackForXmm = 0;
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
  
    // Gprof support by Morio Miki of Tokyo Institute of Technology, Sassa Lab.
    if(root.spec.getCoinsOptions().isSet("gprof")){
      Symbol mcount = module.globalSymtab.addSymbol("mcount", 4, Type.UNKNOWN, 4, ".text", "XREF", ImList.Empty);
       post.add(lir.node(Op.CALL, Type.UNKNOWN, lir.symRef(Op.STATIC, I32, mcount, ImList.Empty), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0]), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0])));
  
      if(convention.equals("cygwin") && func.symbol.name.equals("main")) {
        Symbol monstartup = module.globalSymtab.addSymbol("_monstartup", 4, Type.UNKNOWN, 4, ".text", "XREF", ImList.Empty);
          post.add(lir.node(Op.CALL, Type.UNKNOWN, lir.symRef(Op.STATIC, I32, monstartup, ImList.Empty), lir.node(Op.LIST,   Type.UNKNOWN, new LirNode[0]), lir.node(Op.LIST, Type.UNKNOWN, new LirNode[0])));
       }
       if (!root.spec.getCoinsOptions().isSet("linker")) {
         root.spec.getCoinsOptions().set("linker", "gcc -pg");
       } else {
         String linker = root.spec.getCoinsOptions().getArg("linker");
         if (linker.indexOf("-pg") < 0) {
           root.spec.getCoinsOptions().set("linker", linker + " -pg");
         } /* else already set */
       }
       // System.out.println("*** linker = " + root.spec.getCoinsOptions().getArg("linker"));
  
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
  
      return regnode(type, "%t0");
  
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
  
      // insert dummy push
      if (false) {
        LirNode stackPtr = regnode(I32, "%esp");
        pre.add(lir.node
  	      (Op.PARALLEL, Type.UNKNOWN, lir.node
  	       (Op.SET, I32, lir.node
  		(Op.MEM, I32, stackPtr),
  		regnode(I32, "%ecx_")), lir.node
  	       (Op.SET,
  		I32, stackPtr, lir.node
  		(Op.SUB, I32, stackPtr, lir.iconst(I32, 4)))));
        pre.add(lir.node
  	      (Op.PARALLEL, Type.UNKNOWN, lir.node
  	       (Op.SET, I32, lir.node
  		(Op.MEM, I32, stackPtr),
  		regnode(I32, "%edx_")), lir.node
  	       (Op.SET,
  		I32, stackPtr, lir.node
  		(Op.SUB, I32, stackPtr, lir.iconst(I32, 4)))));
      }
  
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
  
  
    // insert dummy pop
      if (false) {
        LirNode stackPtr = regnode(I32, "%esp");
        post.add(lir.node
  	      (Op.PARALLEL, Type.UNKNOWN, lir.node
  	       (Op.SET, I32,
  		regnode(I32, "%edx_"),
  		lir.node(Op.MEM, I32, stackPtr) ),
  	       lir.node
  	       (Op.SET,
  		I32, stackPtr, lir.node
  		(Op.ADD, I32, stackPtr, lir.iconst(I32, 4)))));
        post.add(lir.node
  	      (Op.PARALLEL, Type.UNKNOWN, lir.node
  	       (Op.SET, I32,
  		regnode(I32, "%ecx_"),
  		lir.node(Op.MEM, I32, stackPtr) ),
  	       lir.node
  	       (Op.SET,
  		I32, stackPtr, lir.node
  		(Op.ADD, I32, stackPtr, lir.iconst(I32, 4)))));
      }
  
  
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
    // eliminateCopy(list);
  }
  
  static final String NOT_FRAME_VAR = " not frame var";
  String getFrameDisp(ImList mem) {
          if (mem.elem() != "mem" ||
              mem.elem2nd() != "long" ||
              !(mem.elem3rd() instanceof ImList)) {
              return NOT_FRAME_VAR;
          }
          ImList addr = (ImList) mem.elem3rd();
          if (addr.elem() != "addr" || !(addr.elem2nd() instanceof ImList)/* || 
  addr.elem3rd() != ImList.Empty*/) {
             return NOT_FRAME_VAR;
          }
          ImList base = (ImList) addr.elem2nd();
          if (base.elem() != "base" || base.elem3rd() != "%ebp") {
             return NOT_FRAME_VAR;
          }
          return base.elem2nd().toString();
  }
  
  /// experimental copy elimination
  void eliminateCopy(BiList list) {
    HashMap dispMap = new HashMap();
    BiLink p = list.first();
  
    // proecess prologue
    String disp = NOT_FRAME_VAR;
    String disp2 = NOT_FRAME_VAR;
  
    for (/* empty */; !p.atEnd(); p = p.next()) {
      ImList inst = (ImList)p.elem();
      String op = (String)inst.elem();
  
      if (op == "deflabel" && dispMap.isEmpty()) {
        // no rewrite
        return;
      }
      else if (!dispMap.isEmpty()) {
        boolean rewritten = false;
        // rewrite disp
        ImList inst1 = (ImList)p.elem();
        ImList inst2 = ImList.Empty;
      
        //System.out.println(inst1);
        for (ImList tmp = inst1;
             !tmp.atEnd(); tmp = tmp.next()) {
            if (tmp.elem() instanceof ImList) {
                ImList t = (ImList) tmp.elem();
                ImList t2 = t;
                String to = (String) dispMap.get(getFrameDisp(t));
                if (to != null) {
                    t2 = rewriteDisp(t, to);
                    //System.out.println("*" + t);
                    //System.out.println("-> " + t2);
                }
                inst2 = new ImList(t2, inst2);
            } else {
                inst2 = new ImList(tmp.elem(), inst2);
            }
        }
        inst2 = inst2.destructiveReverse();
        //System.out.println(" -> " + inst2);
        p.setElem(inst2);
        if (rewritten) {
               continue;
        }
      }
      if (op == "movl" && inst.elem2nd() instanceof ImList && inst.lastElem() ==
   "%eax") {
        // System.out.println(inst);
        ImList mem = (ImList) inst.elem2nd();
        disp = getFrameDisp(mem);
  
        if (disp != NOT_FRAME_VAR && Integer.parseInt(disp) < 0) {
          disp = NOT_FRAME_VAR;
        }
        //System.out.println("check load " + mem + " -> " + disp);
        continue;
      }
      else if (disp != NOT_FRAME_VAR && op == "movl" && inst.elem2nd() == "%eax"
   && inst.elem3rd() instanceof ImList) {
        ImList mem = (ImList) inst.elem3rd();
        disp2 = getFrameDisp(mem);
  
        // System.out.println("check store " + inst);
  
        if (disp2 != NOT_FRAME_VAR) {
          dispMap.put(disp2, disp);
        BiLink tmp = p.prev();  // load node
        tmp.unlink();
        p.unlink();
        }
      }
      disp = NOT_FRAME_VAR;
      disp2 = NOT_FRAME_VAR;
    }
    if (!dispMap.isEmpty()) {
        System.out.println("-- " + func + " -> " + dispMap);
    }
  }
  
  ImList rewriteDisp(ImList mem, String disp) {
      ImList base = ImList.list("base", disp, "%ebp");
      ImList addr = ImList.list("addr", base, ImList.Empty);
      return ImList.list("mem", "long", addr);
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
  
    } else if (dtype == F32) {
  
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
  
  // rewrite dummy pop
  String jmac6(String x) {
    return "\tpopl\t" + x.substring(0, x.length() -1);
  }
  String jmac7(String x) {
    return "\tpushl\t" + x.substring(0, x.length() -1);
  }
  
  String jmac8(String x, String y) {
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
  
  String jmac9(String x, String y) {
    if (y.charAt(0) == '-')
      return x + y;
    else
      return x + "+" + y;
  }
  
  String jmac10(String x, String y) {
    if (y.charAt(0) == '-')
      return x + "+" + y.substring(1);
    else
      return x + "-" + y;
  }
  
  String jmac11(String x) {
    return "" + (Integer.parseInt(x) - 32);
  }
  
  String jmac12(String x) { return "$" + x; }
  
  String jmac13(String x) { return "*" + x; }
  
  String jmac14(String type, String x) { return x; }
  
  String jmac15(String base, String index) {
    if (index == "")
      return base;
    else if (base == "" || base.charAt(base.length() - 1) != ')')
      return base + "(," + index + ")";
    else
      return base.substring(0, base.length() - 1) + "," + index + ")";
  }
  
  String jmac16(String con, String reg) {
    if (reg == "")
      return con;
    else
      return con + "(" + reg + ")";
  }
  
  String jmac17(String reg, String scale) {
    if (scale == "1")
      return reg;
    else
      return reg + "," + scale;
  }
  
  /** Return lower half register name. **/
  String jmac18(String x) { return "%" + x.substring(2); }
  
  /** Return lowest byte register name. **/
  String jmac19(String x) {
    return "%" + x.substring(x.length() - 2, x.length() - 1) + "l";
  }
  
  
  /** Return lower 32bit of memory/register/constant operand. **/
  String jmac20(String x) {
    if (x.charAt(0) == '$')
      return "$" + (Long.parseLong(x.substring(1)) & 0xffffffffL);
    else if (x.charAt(0) == '%')
      return "%" + x.substring(x.length() - 3);
    else
      return x;
  }
  
  
  /** Return upper 32bit of memory/register/constant operand. **/
  String jmac21(String x) {
    if (x.charAt(0) == '$')
      return "$" + ((Long.parseLong(x.substring(1)) >> 32) & 0xffffffffL);
    else if (x.charAt(0) == '%')
      return x.substring(0, x.length() - 3);
    else
      return emitAfter(x, "4");
  }
  
  
  /** Generate prologue sequence. **/
  String jmac22(Object f) {
    Function func = (Function)f;
    X86Attr attr = (X86Attr)getFunctionAttr(func);
  
    // for p2align
    FlowGraph cfg = func.flowGraph();
    LoopAnalysis ana = (LoopAnalysis) func.require(LoopAnalysis.analyzer);
  
    for (BiLink p = cfg.basicBlkList.first();
      !p.atEnd();
      p = p.next()) {
        BasicBlk blk = (BasicBlk) p.elem();
        if (ana.isLoop[blk.id]) {
  // need basic block length check.
  //	System.out.println("loop head " + blk.label());
  	loopInfo.put(blk.label().toString(), "");
        }
    }
    // end
  
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    int size = frameSize(func);
    size = (size + 3) & -4; // round up to 4byte boundary
  //  size = (size + 7) & -8; // round up to 8byte boundary
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
    String xpush = "";
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      String regname = machineParams.registerToString(reg);
      if (regname.startsWith("%x")) {
        xpush += "\n\tmovq\t" + regname + "," + attr.stackForXmm + "(%esp)";
        attr.stackForXmm += 16;
      } else {
        seq += "\n\tpushl\t" + regname;
      }
    }
    if (attr.stackForXmm > 0) {
      xpush = "\n\tsubl\t$" + attr.stackForXmm + ",%esp" + xpush;
    }
    seq += xpush;
    return seq;
  }
  
  /** Generate epilogue sequence. **/
  String jmac23(Object f, String rettype) {
    Function func = (Function)f;
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    int size = frameSize(func);
    X86Attr attr = (X86Attr)getFunctionAttr(func);
    String pops = "";
    String xpops = "";
    int n = 0;
    int xn = 0;
  
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      String regname = machineParams.registerToString(reg);    
  
      if (regname.startsWith("%x")) {
        xpops += "\tmovq\t" + xn + "(%esp)," + regname + "\n";
        xn += 16;
      } else {
        pops = "\tpopl\t" + regname + "\n" + pops;
        n += 4;
      }
    }
    if (attr.stackForXmm > 0) {
      xpops += "\taddl\t$" + attr.stackForXmm + ",%esp\n";
      pops = xpops + pops;
    }
  
    String seq = "";
    if (attr.allocaCalled && n != 0)
      seq = "\tlea\t-" + (size + n + xn) + "(%ebp),%esp\n";
    return seq + pops + "\tleave\n\tret";
  }
  
  String jmac24(String con) {
    return -Integer.parseInt(con) + "";
  }
  
  String jmac25(String x) { currentLine = x; return ".loc 1 " + x + " 0"; }
  
  String jmac26(String x) { return makeAsmSymbol(x); }
  
  /** Emit beginning of segment **/
  void emitBeginningOfSegment(PrintWriter out, String segment) {
    if (convention == "cygwin") {
      if (segment.equals(".text") || segment.equals(".rodata"))
        out.println("\t.text");
      else if (segment.equals(".data"))
        out.println("\t.data");
      else
        out.println("\t.section " + segment);
    } else {
      out.println("\t.section " + segment);
    }
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
      if (convention == "cygwin") {
        out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes);
      } else {
        if (false && bytes >= 32) {
          out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes + ", 16");
        } else {
          out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes + "," + symbol.boundary);
        }
      }
    }
  }
  
  boolean isXmm(LirNode node) {
    //  System.out.println("testing " + node);
  
    // strip subreg
    if (node.opCode == Op.SUBREG) {
      //    System.out.println(" -> SUBREG");
      node = node.kid(0);
      //    System.out.println(" -> kid(0) = " + node);
    }
  
    if (node instanceof LirSymRef) {
      // check %xmm
      Symbol sym = ((LirSymRef) node).symbol;
      String reg = sym.toString();
      if (reg.startsWith("\"%xmm")) {
        //    System.out.println(" -> xmm");
        return true;
      }
      // check &regset
      if (sym.opt().elem() == "&regset") {
        if (sym.opt().elem2nd() == "*reg-F64X*" 
  	|| sym.opt().elem2nd() == "*reg-F32X*") {
  	//		 System.out.println(" -> &regset " + sym.opt().elem2nd());
  		 return true;
        }
      }
    }
    return false;
  }
  
  /**/
  
  //// p2align
  HashMap loopInfo = new HashMap();
  
  String jmac27(String x) { 
    // System.out.println("checking loop: " + x + loopInfo.get(x));
    if (loopInfo.get(x) != null) {
      return "\t.p2align 4,,7\n" + x + ":";
    }
    return x + ":"; 
  }
  
  void emitDataLabel(PrintWriter out, String label) {
      out.println(label + ":");
  }
  
  
  void emitCodeLabel(PrintWriter out, String label) {
    out.println(".p2align\t4,,7");
    super.emitCodeLabel(out, label);
    // out.println(".loc 1 " + currentLine + " 0");
  }
  
  
  void emitBeginningOfModule(Module mod, PrintWriter out)
  {
    //
  }
  
  void emitEndOfModule(Module mod, PrintWriter out)
  {
    // 
  }
}
