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
 19: con8 -> (INTCONST I8)
 20: con16 -> (INTCONST I16)
 21: con32 -> (INTCONST I32)
 22: con64 -> (INTCONST I64)
 23: con -> con8
 24: con -> con16
 25: con -> con32
 26: con -> con64
 27: stamacx -> (STATIC I64)
 28: stamac -> (STATIC I64)
 29: sta -> (STATIC I64)
 30: addr64 -> stamac
 31: addr64 -> stamacx
 32: asmcon32 -> con8
 33: asmcon32 -> con16
 34: asmcon32 -> con32
 35: asmcon64 -> con64
 36: asmcon64 -> sta
 37: asmcon64 -> (ADD I64 asmcon64 con)
 38: asmcon64 -> (SUB I64 asmcon64 con)
 39: asmcon32 -> (ADD I32 asmcon32 con)
 40: asmcon32 -> (SUB I32 asmcon32 con)
 41: lab -> (LABEL I64)
 42: base64 -> asmcon64
 43: base64 -> regq
 44: base64 -> (ADD I64 regq asmcon64)
 45: base64 -> (ADD I64 asmcon64 regq)
 46: base64 -> (ADD I64 regl asmcon64)
 47: base64 -> (ADD I64 asmcon64 regl)
 48: base64 -> (ADD I64 regq asmcon32)
 49: base64 -> (ADD I64 asmcon32 regq)
 50: base64 -> (SUB I64 regq con)
 51: base32 -> asmcon32
 52: base32 -> regl
 53: base32 -> (ADD I32 regl asmcon32)
 54: base32 -> (ADD I32 asmcon32 regl)
 55: base32 -> (SUB I32 regl con)
 56: index64 -> regq
 57: _1 -> (INTCONST I64 2)
 58: index64 -> (MUL I64 regq _1)
 59: _2 -> (INTCONST I64 4)
 60: index64 -> (MUL I64 regq _2)
 61: _3 -> (INTCONST I64 8)
 62: index64 -> (MUL I64 regq _3)
 63: _4 -> (INTCONST I64 1)
 64: index64 -> (LSHS I64 regq _4)
 65: index64 -> (LSHS I64 regq _1)
 66: _5 -> (INTCONST I64 3)
 67: index64 -> (LSHS I64 regq _5)
 68: index32 -> regl
 69: _6 -> (INTCONST I32 2)
 70: index32 -> (MUL I32 regl _6)
 71: _7 -> (INTCONST I32 4)
 72: index32 -> (MUL I32 regl _7)
 73: _8 -> (INTCONST I32 8)
 74: index32 -> (MUL I32 regl _8)
 75: _9 -> (INTCONST I32 1)
 76: index32 -> (LSHS I32 regl _9)
 77: index32 -> (LSHS I32 regl _6)
 78: _10 -> (INTCONST I32 3)
 79: index32 -> (LSHS I32 regl _10)
 80: addr64 -> base64
 81: addr64 -> index64
 82: addr64 -> (ADD I64 base64 index64)
 83: addr64 -> (ADD I64 index64 base64)
 84: addr64 -> (ADD I64 base64 index32)
 85: addr64 -> (ADD I64 index64 base32)
 86: addr64 -> (ADD I64 base32 index64)
 87: addr64 -> (ADD I64 index32 base64)
 88: addr32 -> base32
 89: addr32 -> index32
 90: addr32 -> (ADD I32 base32 index32)
 91: addr32 -> (ADD I32 index32 base32)
 92: addr -> addr32
 93: addr -> addr64
 94: memq -> (MEM I64 addr)
 95: meml -> (MEM I32 addr)
 96: memw -> (MEM I16 addr)
 97: memb -> (MEM I8 addr)
 98: memf -> (MEM F32 addr)
 99: memd -> (MEM F64 addr)
 100: rcq -> regq
 101: rcq -> asmcon64
 102: rcl -> regl
 103: rcl -> asmcon32
 104: rcw -> regw
 105: rcw -> con
 106: rcb -> regb
 107: rcb -> con
 108: mrcq -> memq
 109: mrcq -> rcq
 110: mregq -> memq
 111: mregq -> regq
 112: mrcl -> meml
 113: mrcl -> rcl
 114: mregl -> meml
 115: mregl -> regl
 116: mrcw -> memw
 117: mrcw -> rcw
 118: mregw -> memw
 119: mregw -> regw
 120: mrcb -> memb
 121: mrcb -> rcb
 122: mregb -> memb
 123: mregb -> regb
 124: callarg -> sta
 125: callarg -> regq
 126: regq -> addr64
 127: regl -> addr32
 128: regl -> mrcl
 129: regw -> mrcw
 130: regb -> mrcb
 131: regq -> mregq
 132: bigint -> (INTCONST I64)
 133: regq -> bigint
 134: bigintmac -> (INTCONST I64)
 135: regq -> bigintmac
 136: regd -> memd
 137: regf -> memf
 138: void -> (SET I64 memq rcq)
 139: void -> (SET I64 memq rcq)
 140: void -> (SET I32 meml rcl)
 141: void -> (SET I16 memw rcw)
 142: void -> (SET I8 memb rcb)
 143: void -> (SET I64 xregq regq)
 144: void -> (SET I32 xregl regl)
 145: void -> (SET I16 xregw regw)
 146: void -> (SET I8 xregb regb)
 147: void -> (SET F32 xregf regf)
 148: void -> (SET F64 xregd regd)
 149: void -> (SET F64 memd regd)
 150: void -> (SET F32 memf regf)
 151: shfct -> memq
 152: shfct -> regq
 153: shfct -> regl
 154: shfct -> regw
 155: shfct -> regb
 156: regl -> (ADD I32 regl mrcl)
 157: regl -> (SUB I32 regl mrcl)
 158: regl -> (BAND I32 regl mrcl)
 159: regl -> (BOR I32 regl mrcl)
 160: regl -> (BXOR I32 regl mrcl)
 161: regq -> (ADD I64 regq mrcq)
 162: regq -> (SUB I64 regq mrcq)
 163: regq -> (BAND I64 regq mrcq)
 164: regq -> (BOR I64 regq mrcq)
 165: regq -> (BXOR I64 regq mrcq)
 166: regl -> (ADD I32 mrcl regl)
 167: regl -> (BAND I32 mrcl regl)
 168: regl -> (BOR I32 mrcl regl)
 169: regl -> (BXOR I32 mrcl regl)
 170: regq -> (ADD I64 mrcq regq)
 171: regq -> (BAND I64 mrcq regq)
 172: regq -> (BOR I64 mrcq regq)
 173: regq -> (BXOR I64 mrcq regq)
 174: regl -> (NEG I32 regl)
 175: regl -> (BNOT I32 regl)
 176: regq -> (NEG I64 regq)
 177: regq -> (BNOT I64 regq)
 178: regl -> (LSHS I32 regl con)
 179: regl -> (RSHS I32 regl con)
 180: regl -> (RSHU I32 regl con)
 181: regq -> (LSHS I64 regq con)
 182: regq -> (RSHS I64 regq con)
 183: regq -> (RSHU I64 regq con)
 184: regl -> (LSHS I32 regl shfct)
 185: regl -> (RSHS I32 regl shfct)
 186: regl -> (RSHU I32 regl shfct)
 187: regq -> (LSHS I64 regq shfct)
 188: regq -> (RSHS I64 regq shfct)
 189: regq -> (RSHU I64 regq shfct)
 190: regl -> (MUL I32 regl mrcl)
 191: regl -> (MUL I32 mrcl regl)
 192: regq -> (MUL I64 regq mrcq)
 193: regq -> (MUL I64 mrcq regq)
 194: regl -> (DIVS I32 regl regl)
 195: regq -> (DIVS I64 regq regq)
 196: regl -> (DIVU I32 regl regl)
 197: regq -> (DIVU I64 regq regq)
 198: regl -> (MODS I32 regl regl)
 199: regq -> (MODS I64 regq regq)
 200: regl -> (MODU I32 regl regl)
 201: regq -> (MODU I64 regq regq)
 202: regmemd -> regd
 203: regmemd -> memd
 204: regmemf -> regf
 205: regmemf -> memf
 206: regd -> (ADD F64 regd regmemd)
 207: regd -> (SUB F64 regd regmemd)
 208: regd -> (MUL F64 regd regmemd)
 209: regd -> (DIVS F64 regd regmemd)
 210: regd -> (NEG F64 regd)
 211: regf -> (ADD F32 regf regmemf)
 212: regf -> (SUB F32 regf regmemf)
 213: regf -> (MUL F32 regf regmemf)
 214: regf -> (DIVS F32 regf regmemf)
 215: regf -> (NEG F32 regf)
 216: regq -> (CONVSX I64 regl)
 217: regq -> (CONVSX I64 mregl)
 218: regq -> regl
 219: regq -> mregl
 220: regq -> (CONVSX I64 regw)
 221: regq -> (CONVSX I64 regb)
 222: regl -> (CONVSX I32 mregw)
 223: regl -> (CONVSX I32 mregb)
 224: regw -> (CONVSX I16 mregb)
 225: regq -> (CONVZX I64 meml)
 226: regq -> (CONVZX I64 regl)
 227: regq -> (CONVZX I64 mregw)
 228: regq -> (CONVZX I64 mregb)
 229: regl -> (CONVZX I32 mregw)
 230: regl -> (CONVZX I32 mregb)
 231: regw -> (CONVZX I16 mregb)
 232: regl -> (CONVIT I32 regq)
 233: regw -> (CONVIT I16 regq)
 234: regb -> (CONVIT I8 regq)
 235: regw -> (CONVIT I16 regl)
 236: regb -> (CONVIT I8 regl)
 237: regb -> (CONVIT I8 regw)
 238: _11 -> (CONVIT I32 regq)
 239: void -> (SET I32 xregl _11)
 240: _12 -> (CONVIT I16 regq)
 241: void -> (SET I16 xregw _12)
 242: _13 -> (CONVIT I8 regq)
 243: void -> (SET I8 xregb _13)
 244: _14 -> (CONVIT I16 regl)
 245: void -> (SET I16 xregw _14)
 246: _15 -> (CONVIT I8 regl)
 247: void -> (SET I8 xregb _15)
 248: _16 -> (CONVIT I8 regw)
 249: void -> (SET I8 xregb _16)
 250: regd -> (CONVSF F64 mregq)
 251: regd -> (CONVSF F64 mregl)
 252: regd -> (CONVSF F64 memw)
 253: regd -> (CONVSF F64 regw)
 254: regf -> (CONVSF F32 mregq)
 255: regf -> (CONVSF F32 mregl)
 256: regf -> (CONVSF F32 memw)
 257: regf -> (CONVSF F32 regw)
 258: regd -> (CONVFX F64 regmemf)
 259: regf -> (CONVFT F32 regmemd)
 260: regq -> (CONVFS I64 regd)
 261: regl -> (CONVFS I32 regd)
 262: regw -> (CONVFS I16 regd)
 263: regb -> (CONVFS I8 regd)
 264: _17 -> (CONVFS I64 regd)
 265: void -> (SET I64 memq _17)
 266: _18 -> (CONVFS I32 regd)
 267: void -> (SET I32 meml _18)
 268: _19 -> (CONVFS I16 regd)
 269: void -> (SET I16 memw _19)
 270: regl -> (CONVFI I32 regmemd)
 271: regw -> (CONVFI I16 regmemd)
 272: regb -> (CONVFI I8 regmemd)
 273: _20 -> (CONVFI I32 regd)
 274: void -> (SET I32 meml _20)
 275: _21 -> (CONVFI I16 regd)
 276: void -> (SET I16 memw _21)
 277: regq -> (CONVFS I64 regf)
 278: regl -> (CONVFS I32 regf)
 279: regw -> (CONVFS I16 regf)
 280: regb -> (CONVFS I8 regf)
 281: _22 -> (CONVFS I64 regf)
 282: void -> (SET I64 memq _22)
 283: _23 -> (CONVFS I32 regf)
 284: void -> (SET I32 meml _23)
 285: _24 -> (CONVFS I16 regf)
 286: void -> (SET I16 memw _24)
 287: regl -> (CONVFI I32 regmemf)
 288: regw -> (CONVFI I16 regmemf)
 289: regb -> (CONVFI I8 regmemf)
 290: _25 -> (CONVFI I32 regf)
 291: void -> (SET I32 meml _25)
 292: _26 -> (CONVFI I16 regf)
 293: void -> (SET I16 memw _26)
 294: void -> (JUMP _ lab)
 295: _27 -> (TSTEQ I64 regq mrcq)
 296: void -> (JUMPC _ _27 lab lab)
 297: _28 -> (TSTNE I64 regq mrcq)
 298: void -> (JUMPC _ _28 lab lab)
 299: _29 -> (TSTLTS I64 regq mrcq)
 300: void -> (JUMPC _ _29 lab lab)
 301: _30 -> (TSTLES I64 regq mrcq)
 302: void -> (JUMPC _ _30 lab lab)
 303: _31 -> (TSTGTS I64 regq mrcq)
 304: void -> (JUMPC _ _31 lab lab)
 305: _32 -> (TSTGES I64 regq mrcq)
 306: void -> (JUMPC _ _32 lab lab)
 307: _33 -> (TSTLTU I64 regq mrcq)
 308: void -> (JUMPC _ _33 lab lab)
 309: _34 -> (TSTLEU I64 regq mrcq)
 310: void -> (JUMPC _ _34 lab lab)
 311: _35 -> (TSTGTU I64 regq mrcq)
 312: void -> (JUMPC _ _35 lab lab)
 313: _36 -> (TSTGEU I64 regq mrcq)
 314: void -> (JUMPC _ _36 lab lab)
 315: _37 -> (TSTEQ I32 regq mrcq)
 316: void -> (JUMPC _ _37 lab lab)
 317: _38 -> (TSTNE I32 regq mrcq)
 318: void -> (JUMPC _ _38 lab lab)
 319: _39 -> (TSTLTS I32 regq mrcq)
 320: void -> (JUMPC _ _39 lab lab)
 321: _40 -> (TSTLES I32 regq mrcq)
 322: void -> (JUMPC _ _40 lab lab)
 323: _41 -> (TSTGTS I32 regq mrcq)
 324: void -> (JUMPC _ _41 lab lab)
 325: _42 -> (TSTGES I32 regq mrcq)
 326: void -> (JUMPC _ _42 lab lab)
 327: _43 -> (TSTLTU I32 regq mrcq)
 328: void -> (JUMPC _ _43 lab lab)
 329: _44 -> (TSTLEU I32 regq mrcq)
 330: void -> (JUMPC _ _44 lab lab)
 331: _45 -> (TSTGTU I32 regq mrcq)
 332: void -> (JUMPC _ _45 lab lab)
 333: _46 -> (TSTGEU I32 regq mrcq)
 334: void -> (JUMPC _ _46 lab lab)
 335: _47 -> (TSTEQ I32 regl mrcl)
 336: void -> (JUMPC _ _47 lab lab)
 337: _48 -> (TSTNE I32 regl mrcl)
 338: void -> (JUMPC _ _48 lab lab)
 339: _49 -> (TSTLTS I32 regl mrcl)
 340: void -> (JUMPC _ _49 lab lab)
 341: _50 -> (TSTLES I32 regl mrcl)
 342: void -> (JUMPC _ _50 lab lab)
 343: _51 -> (TSTGTS I32 regl mrcl)
 344: void -> (JUMPC _ _51 lab lab)
 345: _52 -> (TSTGES I32 regl mrcl)
 346: void -> (JUMPC _ _52 lab lab)
 347: _53 -> (TSTLTU I32 regl mrcl)
 348: void -> (JUMPC _ _53 lab lab)
 349: _54 -> (TSTLEU I32 regl mrcl)
 350: void -> (JUMPC _ _54 lab lab)
 351: _55 -> (TSTGTU I32 regl mrcl)
 352: void -> (JUMPC _ _55 lab lab)
 353: _56 -> (TSTGEU I32 regl mrcl)
 354: void -> (JUMPC _ _56 lab lab)
 355: _57 -> (TSTEQ I64 mrcq regq)
 356: void -> (JUMPC _ _57 lab lab)
 357: _58 -> (TSTNE I64 mrcq regq)
 358: void -> (JUMPC _ _58 lab lab)
 359: _59 -> (TSTLTS I64 mrcq regq)
 360: void -> (JUMPC _ _59 lab lab)
 361: _60 -> (TSTLES I64 mrcq regq)
 362: void -> (JUMPC _ _60 lab lab)
 363: _61 -> (TSTGTS I64 mrcq regq)
 364: void -> (JUMPC _ _61 lab lab)
 365: _62 -> (TSTGES I64 mrcq regq)
 366: void -> (JUMPC _ _62 lab lab)
 367: _63 -> (TSTLTU I64 mrcq regq)
 368: void -> (JUMPC _ _63 lab lab)
 369: _64 -> (TSTLEU I64 mrcq regq)
 370: void -> (JUMPC _ _64 lab lab)
 371: _65 -> (TSTGTU I64 mrcq regq)
 372: void -> (JUMPC _ _65 lab lab)
 373: _66 -> (TSTGEU I64 mrcq regq)
 374: void -> (JUMPC _ _66 lab lab)
 375: _67 -> (TSTEQ I32 mrcq regq)
 376: void -> (JUMPC _ _67 lab lab)
 377: _68 -> (TSTNE I32 mrcq regq)
 378: void -> (JUMPC _ _68 lab lab)
 379: _69 -> (TSTLTS I32 mrcq regq)
 380: void -> (JUMPC _ _69 lab lab)
 381: _70 -> (TSTLES I32 mrcq regq)
 382: void -> (JUMPC _ _70 lab lab)
 383: _71 -> (TSTGTS I32 mrcq regq)
 384: void -> (JUMPC _ _71 lab lab)
 385: _72 -> (TSTGES I32 mrcq regq)
 386: void -> (JUMPC _ _72 lab lab)
 387: _73 -> (TSTLTU I32 mrcq regq)
 388: void -> (JUMPC _ _73 lab lab)
 389: _74 -> (TSTLEU I32 mrcq regq)
 390: void -> (JUMPC _ _74 lab lab)
 391: _75 -> (TSTGTU I32 mrcq regq)
 392: void -> (JUMPC _ _75 lab lab)
 393: _76 -> (TSTGEU I32 mrcq regq)
 394: void -> (JUMPC _ _76 lab lab)
 395: _77 -> (TSTEQ I32 mrcl regl)
 396: void -> (JUMPC _ _77 lab lab)
 397: _78 -> (TSTNE I32 mrcl regl)
 398: void -> (JUMPC _ _78 lab lab)
 399: _79 -> (TSTLTS I32 mrcl regl)
 400: void -> (JUMPC _ _79 lab lab)
 401: _80 -> (TSTLES I32 mrcl regl)
 402: void -> (JUMPC _ _80 lab lab)
 403: _81 -> (TSTGTS I32 mrcl regl)
 404: void -> (JUMPC _ _81 lab lab)
 405: _82 -> (TSTGES I32 mrcl regl)
 406: void -> (JUMPC _ _82 lab lab)
 407: _83 -> (TSTLTU I32 mrcl regl)
 408: void -> (JUMPC _ _83 lab lab)
 409: _84 -> (TSTLEU I32 mrcl regl)
 410: void -> (JUMPC _ _84 lab lab)
 411: _85 -> (TSTGTU I32 mrcl regl)
 412: void -> (JUMPC _ _85 lab lab)
 413: _86 -> (TSTGEU I32 mrcl regl)
 414: void -> (JUMPC _ _86 lab lab)
 415: _87 -> (TSTEQ I32 regb mrcb)
 416: void -> (JUMPC _ _87 lab lab)
 417: _88 -> (TSTNE I32 regb mrcb)
 418: void -> (JUMPC _ _88 lab lab)
 419: _89 -> (TSTEQ I32 mrcb regb)
 420: void -> (JUMPC _ _89 lab lab)
 421: _90 -> (TSTNE I32 mrcb regb)
 422: void -> (JUMPC _ _90 lab lab)
 423: _91 -> (TSTEQ I32 regd regmemd)
 424: void -> (JUMPC _ _91 lab lab)
 425: _92 -> (TSTEQ I32 regf regmemf)
 426: void -> (JUMPC _ _92 lab lab)
 427: _93 -> (TSTNE I32 regd regmemd)
 428: void -> (JUMPC _ _93 lab lab)
 429: _94 -> (TSTNE I32 regf regmemf)
 430: void -> (JUMPC _ _94 lab lab)
 431: _95 -> (TSTLTS I32 regd regmemd)
 432: void -> (JUMPC _ _95 lab lab)
 433: _96 -> (TSTLES I32 regd regmemd)
 434: void -> (JUMPC _ _96 lab lab)
 435: _97 -> (TSTGTS I32 regd regmemd)
 436: void -> (JUMPC _ _97 lab lab)
 437: _98 -> (TSTGES I32 regd regmemd)
 438: void -> (JUMPC _ _98 lab lab)
 439: _99 -> (TSTLTS I32 regf regmemf)
 440: void -> (JUMPC _ _99 lab lab)
 441: _100 -> (TSTLES I32 regf regmemf)
 442: void -> (JUMPC _ _100 lab lab)
 443: _101 -> (TSTGTS I32 regf regmemf)
 444: void -> (JUMPC _ _101 lab lab)
 445: _102 -> (TSTGES I32 regf regmemf)
 446: void -> (JUMPC _ _102 lab lab)
 447: void -> (CALL _ callarg)
*/
/*
Sorted Productions:
 43: base64 -> regq
 56: index64 -> regq
 100: rcq -> regq
 111: mregq -> regq
 125: callarg -> regq
 152: shfct -> regq
 52: base32 -> regl
 68: index32 -> regl
 102: rcl -> regl
 115: mregl -> regl
 153: shfct -> regl
 218: regq -> regl
 104: rcw -> regw
 119: mregw -> regw
 154: shfct -> regw
 106: rcb -> regb
 123: mregb -> regb
 155: shfct -> regb
 204: regmemf -> regf
 202: regmemd -> regd
 13: regb -> xregb
 14: regw -> xregw
 15: regl -> xregl
 16: regq -> xregq
 17: regf -> xregf
 18: regd -> xregd
 23: con -> con8
 32: asmcon32 -> con8
 24: con -> con16
 33: asmcon32 -> con16
 25: con -> con32
 34: asmcon32 -> con32
 26: con -> con64
 35: asmcon64 -> con64
 105: rcw -> con
 107: rcb -> con
 31: addr64 -> stamacx
 30: addr64 -> stamac
 36: asmcon64 -> sta
 124: callarg -> sta
 93: addr -> addr64
 126: regq -> addr64
 51: base32 -> asmcon32
 103: rcl -> asmcon32
 42: base64 -> asmcon64
 101: rcq -> asmcon64
 80: addr64 -> base64
 88: addr32 -> base32
 81: addr64 -> index64
 89: addr32 -> index32
 92: addr -> addr32
 127: regl -> addr32
 108: mrcq -> memq
 110: mregq -> memq
 151: shfct -> memq
 112: mrcl -> meml
 114: mregl -> meml
 116: mrcw -> memw
 118: mregw -> memw
 120: mrcb -> memb
 122: mregb -> memb
 137: regf -> memf
 205: regmemf -> memf
 136: regd -> memd
 203: regmemd -> memd
 109: mrcq -> rcq
 113: mrcl -> rcl
 117: mrcw -> rcw
 121: mrcb -> rcb
 131: regq -> mregq
 128: regl -> mrcl
 219: regq -> mregl
 129: regw -> mrcw
 130: regb -> mrcb
 133: regq -> bigint
 135: regq -> bigintmac
 19: con8 -> (INTCONST I8)
 20: con16 -> (INTCONST I16)
 21: con32 -> (INTCONST I32)
 69: _6 -> (INTCONST I32 2)
 71: _7 -> (INTCONST I32 4)
 73: _8 -> (INTCONST I32 8)
 75: _9 -> (INTCONST I32 1)
 78: _10 -> (INTCONST I32 3)
 22: con64 -> (INTCONST I64)
 57: _1 -> (INTCONST I64 2)
 59: _2 -> (INTCONST I64 4)
 61: _3 -> (INTCONST I64 8)
 63: _4 -> (INTCONST I64 1)
 66: _5 -> (INTCONST I64 3)
 132: bigint -> (INTCONST I64)
 134: bigintmac -> (INTCONST I64)
 27: stamacx -> (STATIC I64)
 28: stamac -> (STATIC I64)
 29: sta -> (STATIC I64)
 1: xregb -> (REG I8)
 3: xregw -> (REG I16)
 5: xregl -> (REG I32)
 9: xregf -> (REG F32)
 7: xregq -> (REG I64)
 11: xregd -> (REG F64)
 2: xregb -> (SUBREG I8)
 4: xregw -> (SUBREG I16)
 6: xregl -> (SUBREG I32)
 10: xregf -> (SUBREG F32)
 8: xregq -> (SUBREG I64)
 12: xregd -> (SUBREG F64)
 41: lab -> (LABEL I64)
 174: regl -> (NEG I32 regl)
 215: regf -> (NEG F32 regf)
 176: regq -> (NEG I64 regq)
 210: regd -> (NEG F64 regd)
 39: asmcon32 -> (ADD I32 asmcon32 con)
 53: base32 -> (ADD I32 regl asmcon32)
 54: base32 -> (ADD I32 asmcon32 regl)
 90: addr32 -> (ADD I32 base32 index32)
 91: addr32 -> (ADD I32 index32 base32)
 156: regl -> (ADD I32 regl mrcl)
 166: regl -> (ADD I32 mrcl regl)
 211: regf -> (ADD F32 regf regmemf)
 37: asmcon64 -> (ADD I64 asmcon64 con)
 44: base64 -> (ADD I64 regq asmcon64)
 45: base64 -> (ADD I64 asmcon64 regq)
 46: base64 -> (ADD I64 regl asmcon64)
 47: base64 -> (ADD I64 asmcon64 regl)
 48: base64 -> (ADD I64 regq asmcon32)
 49: base64 -> (ADD I64 asmcon32 regq)
 82: addr64 -> (ADD I64 base64 index64)
 83: addr64 -> (ADD I64 index64 base64)
 84: addr64 -> (ADD I64 base64 index32)
 85: addr64 -> (ADD I64 index64 base32)
 86: addr64 -> (ADD I64 base32 index64)
 87: addr64 -> (ADD I64 index32 base64)
 161: regq -> (ADD I64 regq mrcq)
 170: regq -> (ADD I64 mrcq regq)
 206: regd -> (ADD F64 regd regmemd)
 40: asmcon32 -> (SUB I32 asmcon32 con)
 55: base32 -> (SUB I32 regl con)
 157: regl -> (SUB I32 regl mrcl)
 212: regf -> (SUB F32 regf regmemf)
 38: asmcon64 -> (SUB I64 asmcon64 con)
 50: base64 -> (SUB I64 regq con)
 162: regq -> (SUB I64 regq mrcq)
 207: regd -> (SUB F64 regd regmemd)
 70: index32 -> (MUL I32 regl _6)
 72: index32 -> (MUL I32 regl _7)
 74: index32 -> (MUL I32 regl _8)
 190: regl -> (MUL I32 regl mrcl)
 191: regl -> (MUL I32 mrcl regl)
 213: regf -> (MUL F32 regf regmemf)
 58: index64 -> (MUL I64 regq _1)
 60: index64 -> (MUL I64 regq _2)
 62: index64 -> (MUL I64 regq _3)
 192: regq -> (MUL I64 regq mrcq)
 193: regq -> (MUL I64 mrcq regq)
 208: regd -> (MUL F64 regd regmemd)
 194: regl -> (DIVS I32 regl regl)
 214: regf -> (DIVS F32 regf regmemf)
 195: regq -> (DIVS I64 regq regq)
 209: regd -> (DIVS F64 regd regmemd)
 196: regl -> (DIVU I32 regl regl)
 197: regq -> (DIVU I64 regq regq)
 198: regl -> (MODS I32 regl regl)
 199: regq -> (MODS I64 regq regq)
 200: regl -> (MODU I32 regl regl)
 201: regq -> (MODU I64 regq regq)
 224: regw -> (CONVSX I16 mregb)
 222: regl -> (CONVSX I32 mregw)
 223: regl -> (CONVSX I32 mregb)
 216: regq -> (CONVSX I64 regl)
 217: regq -> (CONVSX I64 mregl)
 220: regq -> (CONVSX I64 regw)
 221: regq -> (CONVSX I64 regb)
 231: regw -> (CONVZX I16 mregb)
 229: regl -> (CONVZX I32 mregw)
 230: regl -> (CONVZX I32 mregb)
 225: regq -> (CONVZX I64 meml)
 226: regq -> (CONVZX I64 regl)
 227: regq -> (CONVZX I64 mregw)
 228: regq -> (CONVZX I64 mregb)
 234: regb -> (CONVIT I8 regq)
 236: regb -> (CONVIT I8 regl)
 237: regb -> (CONVIT I8 regw)
 242: _13 -> (CONVIT I8 regq)
 246: _15 -> (CONVIT I8 regl)
 248: _16 -> (CONVIT I8 regw)
 233: regw -> (CONVIT I16 regq)
 235: regw -> (CONVIT I16 regl)
 240: _12 -> (CONVIT I16 regq)
 244: _14 -> (CONVIT I16 regl)
 232: regl -> (CONVIT I32 regq)
 238: _11 -> (CONVIT I32 regq)
 258: regd -> (CONVFX F64 regmemf)
 259: regf -> (CONVFT F32 regmemd)
 272: regb -> (CONVFI I8 regmemd)
 289: regb -> (CONVFI I8 regmemf)
 271: regw -> (CONVFI I16 regmemd)
 275: _21 -> (CONVFI I16 regd)
 288: regw -> (CONVFI I16 regmemf)
 292: _26 -> (CONVFI I16 regf)
 270: regl -> (CONVFI I32 regmemd)
 273: _20 -> (CONVFI I32 regd)
 287: regl -> (CONVFI I32 regmemf)
 290: _25 -> (CONVFI I32 regf)
 263: regb -> (CONVFS I8 regd)
 280: regb -> (CONVFS I8 regf)
 262: regw -> (CONVFS I16 regd)
 268: _19 -> (CONVFS I16 regd)
 279: regw -> (CONVFS I16 regf)
 285: _24 -> (CONVFS I16 regf)
 261: regl -> (CONVFS I32 regd)
 266: _18 -> (CONVFS I32 regd)
 278: regl -> (CONVFS I32 regf)
 283: _23 -> (CONVFS I32 regf)
 260: regq -> (CONVFS I64 regd)
 264: _17 -> (CONVFS I64 regd)
 277: regq -> (CONVFS I64 regf)
 281: _22 -> (CONVFS I64 regf)
 254: regf -> (CONVSF F32 mregq)
 255: regf -> (CONVSF F32 mregl)
 256: regf -> (CONVSF F32 memw)
 257: regf -> (CONVSF F32 regw)
 250: regd -> (CONVSF F64 mregq)
 251: regd -> (CONVSF F64 mregl)
 252: regd -> (CONVSF F64 memw)
 253: regd -> (CONVSF F64 regw)
 158: regl -> (BAND I32 regl mrcl)
 167: regl -> (BAND I32 mrcl regl)
 163: regq -> (BAND I64 regq mrcq)
 171: regq -> (BAND I64 mrcq regq)
 159: regl -> (BOR I32 regl mrcl)
 168: regl -> (BOR I32 mrcl regl)
 164: regq -> (BOR I64 regq mrcq)
 172: regq -> (BOR I64 mrcq regq)
 160: regl -> (BXOR I32 regl mrcl)
 169: regl -> (BXOR I32 mrcl regl)
 165: regq -> (BXOR I64 regq mrcq)
 173: regq -> (BXOR I64 mrcq regq)
 175: regl -> (BNOT I32 regl)
 177: regq -> (BNOT I64 regq)
 76: index32 -> (LSHS I32 regl _9)
 77: index32 -> (LSHS I32 regl _6)
 79: index32 -> (LSHS I32 regl _10)
 178: regl -> (LSHS I32 regl con)
 184: regl -> (LSHS I32 regl shfct)
 64: index64 -> (LSHS I64 regq _4)
 65: index64 -> (LSHS I64 regq _1)
 67: index64 -> (LSHS I64 regq _5)
 181: regq -> (LSHS I64 regq con)
 187: regq -> (LSHS I64 regq shfct)
 179: regl -> (RSHS I32 regl con)
 185: regl -> (RSHS I32 regl shfct)
 182: regq -> (RSHS I64 regq con)
 188: regq -> (RSHS I64 regq shfct)
 180: regl -> (RSHU I32 regl con)
 186: regl -> (RSHU I32 regl shfct)
 183: regq -> (RSHU I64 regq con)
 189: regq -> (RSHU I64 regq shfct)
 315: _37 -> (TSTEQ I32 regq mrcq)
 335: _47 -> (TSTEQ I32 regl mrcl)
 375: _67 -> (TSTEQ I32 mrcq regq)
 395: _77 -> (TSTEQ I32 mrcl regl)
 415: _87 -> (TSTEQ I32 regb mrcb)
 419: _89 -> (TSTEQ I32 mrcb regb)
 423: _91 -> (TSTEQ I32 regd regmemd)
 425: _92 -> (TSTEQ I32 regf regmemf)
 295: _27 -> (TSTEQ I64 regq mrcq)
 355: _57 -> (TSTEQ I64 mrcq regq)
 317: _38 -> (TSTNE I32 regq mrcq)
 337: _48 -> (TSTNE I32 regl mrcl)
 377: _68 -> (TSTNE I32 mrcq regq)
 397: _78 -> (TSTNE I32 mrcl regl)
 417: _88 -> (TSTNE I32 regb mrcb)
 421: _90 -> (TSTNE I32 mrcb regb)
 427: _93 -> (TSTNE I32 regd regmemd)
 429: _94 -> (TSTNE I32 regf regmemf)
 297: _28 -> (TSTNE I64 regq mrcq)
 357: _58 -> (TSTNE I64 mrcq regq)
 319: _39 -> (TSTLTS I32 regq mrcq)
 339: _49 -> (TSTLTS I32 regl mrcl)
 379: _69 -> (TSTLTS I32 mrcq regq)
 399: _79 -> (TSTLTS I32 mrcl regl)
 431: _95 -> (TSTLTS I32 regd regmemd)
 439: _99 -> (TSTLTS I32 regf regmemf)
 299: _29 -> (TSTLTS I64 regq mrcq)
 359: _59 -> (TSTLTS I64 mrcq regq)
 321: _40 -> (TSTLES I32 regq mrcq)
 341: _50 -> (TSTLES I32 regl mrcl)
 381: _70 -> (TSTLES I32 mrcq regq)
 401: _80 -> (TSTLES I32 mrcl regl)
 433: _96 -> (TSTLES I32 regd regmemd)
 441: _100 -> (TSTLES I32 regf regmemf)
 301: _30 -> (TSTLES I64 regq mrcq)
 361: _60 -> (TSTLES I64 mrcq regq)
 323: _41 -> (TSTGTS I32 regq mrcq)
 343: _51 -> (TSTGTS I32 regl mrcl)
 383: _71 -> (TSTGTS I32 mrcq regq)
 403: _81 -> (TSTGTS I32 mrcl regl)
 435: _97 -> (TSTGTS I32 regd regmemd)
 443: _101 -> (TSTGTS I32 regf regmemf)
 303: _31 -> (TSTGTS I64 regq mrcq)
 363: _61 -> (TSTGTS I64 mrcq regq)
 325: _42 -> (TSTGES I32 regq mrcq)
 345: _52 -> (TSTGES I32 regl mrcl)
 385: _72 -> (TSTGES I32 mrcq regq)
 405: _82 -> (TSTGES I32 mrcl regl)
 437: _98 -> (TSTGES I32 regd regmemd)
 445: _102 -> (TSTGES I32 regf regmemf)
 305: _32 -> (TSTGES I64 regq mrcq)
 365: _62 -> (TSTGES I64 mrcq regq)
 327: _43 -> (TSTLTU I32 regq mrcq)
 347: _53 -> (TSTLTU I32 regl mrcl)
 387: _73 -> (TSTLTU I32 mrcq regq)
 407: _83 -> (TSTLTU I32 mrcl regl)
 307: _33 -> (TSTLTU I64 regq mrcq)
 367: _63 -> (TSTLTU I64 mrcq regq)
 329: _44 -> (TSTLEU I32 regq mrcq)
 349: _54 -> (TSTLEU I32 regl mrcl)
 389: _74 -> (TSTLEU I32 mrcq regq)
 409: _84 -> (TSTLEU I32 mrcl regl)
 309: _34 -> (TSTLEU I64 regq mrcq)
 369: _64 -> (TSTLEU I64 mrcq regq)
 331: _45 -> (TSTGTU I32 regq mrcq)
 351: _55 -> (TSTGTU I32 regl mrcl)
 391: _75 -> (TSTGTU I32 mrcq regq)
 411: _85 -> (TSTGTU I32 mrcl regl)
 311: _35 -> (TSTGTU I64 regq mrcq)
 371: _65 -> (TSTGTU I64 mrcq regq)
 333: _46 -> (TSTGEU I32 regq mrcq)
 353: _56 -> (TSTGEU I32 regl mrcl)
 393: _76 -> (TSTGEU I32 mrcq regq)
 413: _86 -> (TSTGEU I32 mrcl regl)
 313: _36 -> (TSTGEU I64 regq mrcq)
 373: _66 -> (TSTGEU I64 mrcq regq)
 97: memb -> (MEM I8 addr)
 96: memw -> (MEM I16 addr)
 95: meml -> (MEM I32 addr)
 98: memf -> (MEM F32 addr)
 94: memq -> (MEM I64 addr)
 99: memd -> (MEM F64 addr)
 142: void -> (SET I8 memb rcb)
 146: void -> (SET I8 xregb regb)
 243: void -> (SET I8 xregb _13)
 247: void -> (SET I8 xregb _15)
 249: void -> (SET I8 xregb _16)
 141: void -> (SET I16 memw rcw)
 145: void -> (SET I16 xregw regw)
 241: void -> (SET I16 xregw _12)
 245: void -> (SET I16 xregw _14)
 269: void -> (SET I16 memw _19)
 276: void -> (SET I16 memw _21)
 286: void -> (SET I16 memw _24)
 293: void -> (SET I16 memw _26)
 140: void -> (SET I32 meml rcl)
 144: void -> (SET I32 xregl regl)
 239: void -> (SET I32 xregl _11)
 267: void -> (SET I32 meml _18)
 274: void -> (SET I32 meml _20)
 284: void -> (SET I32 meml _23)
 291: void -> (SET I32 meml _25)
 147: void -> (SET F32 xregf regf)
 150: void -> (SET F32 memf regf)
 138: void -> (SET I64 memq rcq)
 139: void -> (SET I64 memq rcq)
 143: void -> (SET I64 xregq regq)
 265: void -> (SET I64 memq _17)
 282: void -> (SET I64 memq _22)
 148: void -> (SET F64 xregd regd)
 149: void -> (SET F64 memd regd)
 294: void -> (JUMP _ lab)
 296: void -> (JUMPC _ _27 lab lab)
 298: void -> (JUMPC _ _28 lab lab)
 300: void -> (JUMPC _ _29 lab lab)
 302: void -> (JUMPC _ _30 lab lab)
 304: void -> (JUMPC _ _31 lab lab)
 306: void -> (JUMPC _ _32 lab lab)
 308: void -> (JUMPC _ _33 lab lab)
 310: void -> (JUMPC _ _34 lab lab)
 312: void -> (JUMPC _ _35 lab lab)
 314: void -> (JUMPC _ _36 lab lab)
 316: void -> (JUMPC _ _37 lab lab)
 318: void -> (JUMPC _ _38 lab lab)
 320: void -> (JUMPC _ _39 lab lab)
 322: void -> (JUMPC _ _40 lab lab)
 324: void -> (JUMPC _ _41 lab lab)
 326: void -> (JUMPC _ _42 lab lab)
 328: void -> (JUMPC _ _43 lab lab)
 330: void -> (JUMPC _ _44 lab lab)
 332: void -> (JUMPC _ _45 lab lab)
 334: void -> (JUMPC _ _46 lab lab)
 336: void -> (JUMPC _ _47 lab lab)
 338: void -> (JUMPC _ _48 lab lab)
 340: void -> (JUMPC _ _49 lab lab)
 342: void -> (JUMPC _ _50 lab lab)
 344: void -> (JUMPC _ _51 lab lab)
 346: void -> (JUMPC _ _52 lab lab)
 348: void -> (JUMPC _ _53 lab lab)
 350: void -> (JUMPC _ _54 lab lab)
 352: void -> (JUMPC _ _55 lab lab)
 354: void -> (JUMPC _ _56 lab lab)
 356: void -> (JUMPC _ _57 lab lab)
 358: void -> (JUMPC _ _58 lab lab)
 360: void -> (JUMPC _ _59 lab lab)
 362: void -> (JUMPC _ _60 lab lab)
 364: void -> (JUMPC _ _61 lab lab)
 366: void -> (JUMPC _ _62 lab lab)
 368: void -> (JUMPC _ _63 lab lab)
 370: void -> (JUMPC _ _64 lab lab)
 372: void -> (JUMPC _ _65 lab lab)
 374: void -> (JUMPC _ _66 lab lab)
 376: void -> (JUMPC _ _67 lab lab)
 378: void -> (JUMPC _ _68 lab lab)
 380: void -> (JUMPC _ _69 lab lab)
 382: void -> (JUMPC _ _70 lab lab)
 384: void -> (JUMPC _ _71 lab lab)
 386: void -> (JUMPC _ _72 lab lab)
 388: void -> (JUMPC _ _73 lab lab)
 390: void -> (JUMPC _ _74 lab lab)
 392: void -> (JUMPC _ _75 lab lab)
 394: void -> (JUMPC _ _76 lab lab)
 396: void -> (JUMPC _ _77 lab lab)
 398: void -> (JUMPC _ _78 lab lab)
 400: void -> (JUMPC _ _79 lab lab)
 402: void -> (JUMPC _ _80 lab lab)
 404: void -> (JUMPC _ _81 lab lab)
 406: void -> (JUMPC _ _82 lab lab)
 408: void -> (JUMPC _ _83 lab lab)
 410: void -> (JUMPC _ _84 lab lab)
 412: void -> (JUMPC _ _85 lab lab)
 414: void -> (JUMPC _ _86 lab lab)
 416: void -> (JUMPC _ _87 lab lab)
 418: void -> (JUMPC _ _88 lab lab)
 420: void -> (JUMPC _ _89 lab lab)
 422: void -> (JUMPC _ _90 lab lab)
 424: void -> (JUMPC _ _91 lab lab)
 426: void -> (JUMPC _ _92 lab lab)
 428: void -> (JUMPC _ _93 lab lab)
 430: void -> (JUMPC _ _94 lab lab)
 432: void -> (JUMPC _ _95 lab lab)
 434: void -> (JUMPC _ _96 lab lab)
 436: void -> (JUMPC _ _97 lab lab)
 438: void -> (JUMPC _ _98 lab lab)
 440: void -> (JUMPC _ _99 lab lab)
 442: void -> (JUMPC _ _100 lab lab)
 444: void -> (JUMPC _ _101 lab lab)
 446: void -> (JUMPC _ _102 lab lab)
 447: void -> (CALL _ callarg)
*/
/*
Productions:
 1: _1 -> (STATIC I64 "__va_start")
 2: _rewr -> (CALL _ _1 _ _)
 3: _rewr -> (CALL _ _1 _ _)
 4: _2 -> (STATIC I64 "__va_arg")
 5: _rewr -> (CALL _ _2 _ _)
 6: _rewr -> (DIVS I32 _ _)
 7: _rewr -> (MODS I32 _ _)
 8: _rewr -> (DIVU I32 _ _)
 9: _rewr -> (MODU I32 _ _)
 10: _rewr -> (DIVS I64 _ _)
 11: _rewr -> (MODS I64 _ _)
 12: _rewr -> (DIVU I64 _ _)
 13: _rewr -> (MODU I64 _ _)
 14: _rewr -> (DIVS I32 _ _)
 15: _rewr -> (MODS I32 _ _)
 16: _rewr -> (DIVU I32 _ _)
 17: _rewr -> (MODU I32 _ _)
 18: _rewr -> (DIVS I64 _ _)
 19: _rewr -> (MODS I64 _ _)
 20: _rewr -> (DIVU I64 _ _)
 21: _rewr -> (MODU I64 _ _)
 22: _rewr -> (MUL I32 _ _)
 23: _rewr -> (MUL I64 _ _)
 24: _3 -> (STATIC I64 "alloca")
 25: _4 -> (LIST _ _)
 26: _rewr -> (CALL _ _3 _4 _4)
 27: _rewr -> (CONVUF _ _)
 28: _rewr -> (CONVFU _ _)
 29: _rewr -> (PROLOGUE _)
 30: _rewr -> (EPILOGUE _)
 31: _rewr -> (CALL _)
 32: _rewr -> (ASM _)
 33: _rewr -> (FLOATCONST F32)
 34: _rewr -> (FLOATCONST F64)
 35: _rewr -> (TSTNE I32 sbyteopr sbyteopr)
 36: _rewr -> (TSTEQ I32 sbyteopr sbyteopr)
 37: _rewr -> (TSTNE I32 ubyteopr ubyteopr)
 38: _rewr -> (TSTEQ I32 ubyteopr ubyteopr)
 39: sbyteopr -> (CONVSX I32 _)
 40: sbyteopr -> (INTCONST I32)
 41: ubyteopr -> (CONVZX I32 _)
 42: ubyteopr -> (INTCONST I32)
 43: _5 -> (CONVSX _ _)
 44: _rewr -> (LSHS I32 _ _5)
 45: _rewr -> (RSHS I32 _ _5)
 46: _rewr -> (RSHU I32 _ _5)
 47: _rewr -> (LSHS I64 _ _5)
 48: _rewr -> (RSHS I64 _ _5)
 49: _rewr -> (RSHU I64 _ _5)
 50: _rewr -> (PROLOGUE _)
 51: _rewr -> (JUMPN _)
 52: _rewr -> (SET _)
*/
/*
Sorted Productions:
 40: sbyteopr -> (INTCONST I32)
 42: ubyteopr -> (INTCONST I32)
 33: _rewr -> (FLOATCONST F32)
 34: _rewr -> (FLOATCONST F64)
 1: _1 -> (STATIC I64 "__va_start")
 4: _2 -> (STATIC I64 "__va_arg")
 24: _3 -> (STATIC I64 "alloca")
 22: _rewr -> (MUL I32 _ _)
 23: _rewr -> (MUL I64 _ _)
 6: _rewr -> (DIVS I32 _ _)
 14: _rewr -> (DIVS I32 _ _)
 10: _rewr -> (DIVS I64 _ _)
 18: _rewr -> (DIVS I64 _ _)
 8: _rewr -> (DIVU I32 _ _)
 16: _rewr -> (DIVU I32 _ _)
 12: _rewr -> (DIVU I64 _ _)
 20: _rewr -> (DIVU I64 _ _)
 7: _rewr -> (MODS I32 _ _)
 15: _rewr -> (MODS I32 _ _)
 11: _rewr -> (MODS I64 _ _)
 19: _rewr -> (MODS I64 _ _)
 9: _rewr -> (MODU I32 _ _)
 17: _rewr -> (MODU I32 _ _)
 13: _rewr -> (MODU I64 _ _)
 21: _rewr -> (MODU I64 _ _)
 43: _5 -> (CONVSX _ _)
 39: sbyteopr -> (CONVSX I32 _)
 41: ubyteopr -> (CONVZX I32 _)
 28: _rewr -> (CONVFU _ _)
 27: _rewr -> (CONVUF _ _)
 44: _rewr -> (LSHS I32 _ _5)
 47: _rewr -> (LSHS I64 _ _5)
 45: _rewr -> (RSHS I32 _ _5)
 48: _rewr -> (RSHS I64 _ _5)
 46: _rewr -> (RSHU I32 _ _5)
 49: _rewr -> (RSHU I64 _ _5)
 36: _rewr -> (TSTEQ I32 sbyteopr sbyteopr)
 38: _rewr -> (TSTEQ I32 ubyteopr ubyteopr)
 35: _rewr -> (TSTNE I32 sbyteopr sbyteopr)
 37: _rewr -> (TSTNE I32 ubyteopr ubyteopr)
 52: _rewr -> (SET _)
 51: _rewr -> (JUMPN _)
 2: _rewr -> (CALL _ _1 _ _)
 3: _rewr -> (CALL _ _1 _ _)
 5: _rewr -> (CALL _ _2 _ _)
 26: _rewr -> (CALL _ _3 _4 _4)
 31: _rewr -> (CALL _)
 29: _rewr -> (PROLOGUE _)
 50: _rewr -> (PROLOGUE _)
 30: _rewr -> (EPILOGUE _)
 25: _4 -> (LIST _ _)
 32: _rewr -> (ASM _)
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
import coins.backend.util.QuotedString;
import coins.backend.lir.LirLabelRef;
import coins.sym.*;
import coins.ir.IrList;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import coins.IoRoot;
import coins.SymRoot;
import coins.driver.CompileThread;




public class CodeGenerator_x86_64 extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_x86_64() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 9;
    static final int NRULES = 52 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;
    static final int NT__1 = 2;
    static final int NT__2 = 3;
    static final int NT__3 = 4;
    static final int NT__4 = 5;
    static final int NT_sbyteopr = 6;
    static final int NT_ubyteopr = 7;
    static final int NT__5 = 8;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT__4: return "_4";
      case NT_sbyteopr: return "sbyteopr";
      case NT_ubyteopr: return "ubyteopr";
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
      case Op.INTCONST:
        if (t.type == 514) {
          if (-128 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 127) record(NT_sbyteopr, 40);
          if (((LirIconst)t).unsignedValue() <= 255) record(NT_ubyteopr, 42);
        }
        break;
      case Op.FLOATCONST:
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
      case Op.STATIC:
        if (t.type == 1026) {
          if (((LirSymRef)t).symbol.name == "__va_start") record(NT__1, 1);
          if (((LirSymRef)t).symbol.name == "__va_arg") record(NT__2, 4);
          if (((LirSymRef)t).symbol.name == "alloca") record(NT__3, 24);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (phase == "late") if (isSmallConst(t.kid(0), t.kid(1)))  {
            rewritten = true;
            return rewriteMULtoShift(t, pre);
          }
        }
        if (t.type == 1026) {
          if (phase == "late") if (isSmallConst(t.kid(0), t.kid(1)))  {
            rewritten = true;
            return rewriteMULtoShift(t, pre);
          }
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.DIVS));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.DIVS));
          }
        }
        if (t.type == 1026) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.DIVS));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.DIVS));
          }
        }
        break;
      case Op.DIVU:
        if (t.type == 514) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.DIVU));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.DIVU));
          }
        }
        if (t.type == 1026) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.DIVU));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.DIVU));
          }
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.MODS));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.MODS));
          }
        }
        if (t.type == 1026) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.MODS));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.MODS));
          }
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.MODU));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.MODU));
          }
        }
        if (t.type == 1026) {
          if (phase == "early")  {
            rewritten = true;
            return noRescan(rewriteDIVMODby1(t, pre, Op.MODU));
          }
          if (phase == "late")  {
            rewritten = true;
            return noRescan(rewriteDIVMOD(t, pre, Op.MODU));
          }
        }
        break;
      case Op.CONVSX:
        record(NT__5, 43);
        if (t.type == 514) {
          if (t.kid(0).type == I8) record(NT_sbyteopr, 39);
        }
        break;
      case Op.CONVZX:
        if (t.type == 514) {
          if (t.kid(0).type == I8) record(NT_ubyteopr, 41);
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
          if (kids[1].rule[NT__5] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.LSHS, 514, t.kid(0), t.kid(1).kid(0));
          }
        }
        if (t.type == 1026) {
          if (kids[1].rule[NT__5] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.LSHS, 1026, t.kid(0), t.kid(1).kid(0));
          }
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[1].rule[NT__5] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHS, 514, t.kid(0), t.kid(1).kid(0));
          }
        }
        if (t.type == 1026) {
          if (kids[1].rule[NT__5] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHS, 1026, t.kid(0), t.kid(1).kid(0));
          }
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[1].rule[NT__5] != 0) if (phase == "late")  {
            rewritten = true;
            return lir.node(Op.RSHU, 514, t.kid(0), t.kid(1).kid(0));
          }
        }
        if (t.type == 1026) {
          if (kids[1].rule[NT__5] != 0) if (phase == "late")  {
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
        if (kids[0].rule[NT__1] != 0) if (phase == "early")  {
          rewritten = true;
          return noRescan(setVaStartCalled(t));
        }
        if (kids[0].rule[NT__1] != 0) if (phase == "late")  {
          rewritten = true;
          return setVaStartCalledLate(t, pre);
        }
        if (kids[0].rule[NT__2] != 0) if (phase == "late")  {
          rewritten = true;
          return rewriteVaArg(t, pre);
        }
        if (kids[0].rule[NT__3] != 0) if (kids[1].rule[NT__4] != 0) if (kids[2].rule[NT__4] != 0) if (phase == "early")  {
          rewritten = true;
          setAllocaCalled();
          {
          pre.add(lir.node(Op.SET, 1026, lir.node(Op.REG, 1026, func.getSymbol("%rsp")), lir.node(Op.SUB, 1026, lir.node(Op.REG, 1026, func.getSymbol("%rsp")), lir.node(Op.BAND, 1026, lir.node(Op.ADD, 1026, t.kid(1).kid(0), lir.iconst(1026, 15)), lir.iconst(1026, -16)))));
          }
          return lir.node(Op.SET, 1026, t.kid(2).kid(0), lir.node(Op.REG, 1026, func.getSymbol("%rsp")));
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
        if (phase == "early")  {
          rewritten = true;
          return paramCount(t);
        }
        break;
      case Op.EPILOGUE:
        if (phase == "late")  {
          rewritten = true;
          return noRescan(rewriteEpilogue(t, pre));
        }
        break;
      case Op.LIST:
        if (kids.length == 1) record(NT__4, 25);
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
    static final int NNONTERM = 158;
    static final int NRULES = 447 + 1;
    static final int START_NT = 7;

    static final int NT__ = 0;
    static final int NT_regq = 1;
    static final int NT_regl = 2;
    static final int NT_regw = 3;
    static final int NT_regb = 4;
    static final int NT_regf = 5;
    static final int NT_regd = 6;
    static final int NT_void = 7;
    static final int NT_xregb = 8;
    static final int NT_xregw = 9;
    static final int NT_xregl = 10;
    static final int NT_xregq = 11;
    static final int NT_xregf = 12;
    static final int NT_xregd = 13;
    static final int NT_con8 = 14;
    static final int NT_con16 = 15;
    static final int NT_con32 = 16;
    static final int NT_con64 = 17;
    static final int NT_con = 18;
    static final int NT_stamacx = 19;
    static final int NT_stamac = 20;
    static final int NT_sta = 21;
    static final int NT_addr64 = 22;
    static final int NT_asmcon32 = 23;
    static final int NT_asmcon64 = 24;
    static final int NT_lab = 25;
    static final int NT_base64 = 26;
    static final int NT_base32 = 27;
    static final int NT_index64 = 28;
    static final int NT__1 = 29;
    static final int NT__2 = 30;
    static final int NT__3 = 31;
    static final int NT__4 = 32;
    static final int NT__5 = 33;
    static final int NT_index32 = 34;
    static final int NT__6 = 35;
    static final int NT__7 = 36;
    static final int NT__8 = 37;
    static final int NT__9 = 38;
    static final int NT__10 = 39;
    static final int NT_addr32 = 40;
    static final int NT_addr = 41;
    static final int NT_memq = 42;
    static final int NT_meml = 43;
    static final int NT_memw = 44;
    static final int NT_memb = 45;
    static final int NT_memf = 46;
    static final int NT_memd = 47;
    static final int NT_rcq = 48;
    static final int NT_rcl = 49;
    static final int NT_rcw = 50;
    static final int NT_rcb = 51;
    static final int NT_mrcq = 52;
    static final int NT_mregq = 53;
    static final int NT_mrcl = 54;
    static final int NT_mregl = 55;
    static final int NT_mrcw = 56;
    static final int NT_mregw = 57;
    static final int NT_mrcb = 58;
    static final int NT_mregb = 59;
    static final int NT_callarg = 60;
    static final int NT_bigint = 61;
    static final int NT_bigintmac = 62;
    static final int NT_shfct = 63;
    static final int NT_regmemd = 64;
    static final int NT_regmemf = 65;
    static final int NT__11 = 66;
    static final int NT__12 = 67;
    static final int NT__13 = 68;
    static final int NT__14 = 69;
    static final int NT__15 = 70;
    static final int NT__16 = 71;
    static final int NT__17 = 72;
    static final int NT__18 = 73;
    static final int NT__19 = 74;
    static final int NT__20 = 75;
    static final int NT__21 = 76;
    static final int NT__22 = 77;
    static final int NT__23 = 78;
    static final int NT__24 = 79;
    static final int NT__25 = 80;
    static final int NT__26 = 81;
    static final int NT__27 = 82;
    static final int NT__28 = 83;
    static final int NT__29 = 84;
    static final int NT__30 = 85;
    static final int NT__31 = 86;
    static final int NT__32 = 87;
    static final int NT__33 = 88;
    static final int NT__34 = 89;
    static final int NT__35 = 90;
    static final int NT__36 = 91;
    static final int NT__37 = 92;
    static final int NT__38 = 93;
    static final int NT__39 = 94;
    static final int NT__40 = 95;
    static final int NT__41 = 96;
    static final int NT__42 = 97;
    static final int NT__43 = 98;
    static final int NT__44 = 99;
    static final int NT__45 = 100;
    static final int NT__46 = 101;
    static final int NT__47 = 102;
    static final int NT__48 = 103;
    static final int NT__49 = 104;
    static final int NT__50 = 105;
    static final int NT__51 = 106;
    static final int NT__52 = 107;
    static final int NT__53 = 108;
    static final int NT__54 = 109;
    static final int NT__55 = 110;
    static final int NT__56 = 111;
    static final int NT__57 = 112;
    static final int NT__58 = 113;
    static final int NT__59 = 114;
    static final int NT__60 = 115;
    static final int NT__61 = 116;
    static final int NT__62 = 117;
    static final int NT__63 = 118;
    static final int NT__64 = 119;
    static final int NT__65 = 120;
    static final int NT__66 = 121;
    static final int NT__67 = 122;
    static final int NT__68 = 123;
    static final int NT__69 = 124;
    static final int NT__70 = 125;
    static final int NT__71 = 126;
    static final int NT__72 = 127;
    static final int NT__73 = 128;
    static final int NT__74 = 129;
    static final int NT__75 = 130;
    static final int NT__76 = 131;
    static final int NT__77 = 132;
    static final int NT__78 = 133;
    static final int NT__79 = 134;
    static final int NT__80 = 135;
    static final int NT__81 = 136;
    static final int NT__82 = 137;
    static final int NT__83 = 138;
    static final int NT__84 = 139;
    static final int NT__85 = 140;
    static final int NT__86 = 141;
    static final int NT__87 = 142;
    static final int NT__88 = 143;
    static final int NT__89 = 144;
    static final int NT__90 = 145;
    static final int NT__91 = 146;
    static final int NT__92 = 147;
    static final int NT__93 = 148;
    static final int NT__94 = 149;
    static final int NT__95 = 150;
    static final int NT__96 = 151;
    static final int NT__97 = 152;
    static final int NT__98 = 153;
    static final int NT__99 = 154;
    static final int NT__100 = 155;
    static final int NT__101 = 156;
    static final int NT__102 = 157;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regq: return "regq";
      case NT_regl: return "regl";
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
      case NT_con8: return "con8";
      case NT_con16: return "con16";
      case NT_con32: return "con32";
      case NT_con64: return "con64";
      case NT_con: return "con";
      case NT_stamacx: return "stamacx";
      case NT_stamac: return "stamac";
      case NT_sta: return "sta";
      case NT_addr64: return "addr64";
      case NT_asmcon32: return "asmcon32";
      case NT_asmcon64: return "asmcon64";
      case NT_lab: return "lab";
      case NT_base64: return "base64";
      case NT_base32: return "base32";
      case NT_index64: return "index64";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT__4: return "_4";
      case NT__5: return "_5";
      case NT_index32: return "index32";
      case NT__6: return "_6";
      case NT__7: return "_7";
      case NT__8: return "_8";
      case NT__9: return "_9";
      case NT__10: return "_10";
      case NT_addr32: return "addr32";
      case NT_addr: return "addr";
      case NT_memq: return "memq";
      case NT_meml: return "meml";
      case NT_memw: return "memw";
      case NT_memb: return "memb";
      case NT_memf: return "memf";
      case NT_memd: return "memd";
      case NT_rcq: return "rcq";
      case NT_rcl: return "rcl";
      case NT_rcw: return "rcw";
      case NT_rcb: return "rcb";
      case NT_mrcq: return "mrcq";
      case NT_mregq: return "mregq";
      case NT_mrcl: return "mrcl";
      case NT_mregl: return "mregl";
      case NT_mrcw: return "mrcw";
      case NT_mregw: return "mregw";
      case NT_mrcb: return "mrcb";
      case NT_mregb: return "mregb";
      case NT_callarg: return "callarg";
      case NT_bigint: return "bigint";
      case NT_bigintmac: return "bigintmac";
      case NT_shfct: return "shfct";
      case NT_regmemd: return "regmemd";
      case NT_regmemf: return "regmemf";
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
      case NT__97: return "_97";
      case NT__98: return "_98";
      case NT__99: return "_99";
      case NT__100: return "_100";
      case NT__101: return "_101";
      case NT__102: return "_102";
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
          record(NT_base64, 0 + cost1, 0 + cost2, 43);
          record(NT_index64, 0 + cost1, 0 + cost2, 56);
          record(NT_rcq, 0 + cost1, 0 + cost2, 100);
          record(NT_mregq, 0 + cost1, 0 + cost2, 111);
          record(NT_callarg, 0 + cost1, 0 + cost2, 125);
          record(NT_shfct, 0 + cost1, 0 + cost2, 152);
          break;
        case NT_regl:
          record(NT_base32, 0 + cost1, 0 + cost2, 52);
          record(NT_index32, 0 + cost1, 0 + cost2, 68);
          record(NT_rcl, 0 + cost1, 0 + cost2, 102);
          record(NT_mregl, 0 + cost1, 0 + cost2, 115);
          record(NT_shfct, 0 + cost1, 0 + cost2, 153);
          record(NT_regq, 2 + cost1, 2 + cost2, 218);
          break;
        case NT_regw:
          record(NT_rcw, 0 + cost1, 0 + cost2, 104);
          record(NT_mregw, 0 + cost1, 0 + cost2, 119);
          record(NT_shfct, 0 + cost1, 0 + cost2, 154);
          break;
        case NT_regb:
          record(NT_rcb, 0 + cost1, 0 + cost2, 106);
          record(NT_mregb, 0 + cost1, 0 + cost2, 123);
          record(NT_shfct, 0 + cost1, 0 + cost2, 155);
          break;
        case NT_regf:
          record(NT_regmemf, 0 + cost1, 0 + cost2, 204);
          break;
        case NT_regd:
          record(NT_regmemd, 0 + cost1, 0 + cost2, 202);
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
        case NT_con8:
          record(NT_con, 0 + cost1, 0 + cost2, 23);
          record(NT_asmcon32, 0 + cost1, 0 + cost2, 32);
          break;
        case NT_con16:
          record(NT_con, 0 + cost1, 0 + cost2, 24);
          record(NT_asmcon32, 0 + cost1, 0 + cost2, 33);
          break;
        case NT_con32:
          record(NT_con, 0 + cost1, 0 + cost2, 25);
          record(NT_asmcon32, 0 + cost1, 0 + cost2, 34);
          break;
        case NT_con64:
          record(NT_con, 0 + cost1, 0 + cost2, 26);
          record(NT_asmcon64, 0 + cost1, 0 + cost2, 35);
          break;
        case NT_con:
          record(NT_rcw, 1 + cost1, 1 + cost2, 105);
          record(NT_rcb, 1 + cost1, 1 + cost2, 107);
          break;
        case NT_stamacx:
          record(NT_addr64, 0 + cost1, 0 + cost2, 31);
          break;
        case NT_stamac:
          record(NT_addr64, 0 + cost1, 0 + cost2, 30);
          break;
        case NT_sta:
          if (conventionIsNotMac()) record(NT_asmcon64, 0 + cost1, 0 + cost2, 36);
          record(NT_callarg, 0 + cost1, 0 + cost2, 124);
          break;
        case NT_addr64:
          record(NT_addr, 0 + cost1, 0 + cost2, 93);
          record(NT_regq, 2 + cost1, 2 + cost2, 126);
          break;
        case NT_asmcon32:
          record(NT_base32, 0 + cost1, 0 + cost2, 51);
          record(NT_rcl, 1 + cost1, 1 + cost2, 103);
          break;
        case NT_asmcon64:
          record(NT_base64, 0 + cost1, 0 + cost2, 42);
          record(NT_rcq, 1 + cost1, 1 + cost2, 101);
          break;
        case NT_base64:
          record(NT_addr64, 0 + cost1, 0 + cost2, 80);
          break;
        case NT_base32:
          record(NT_addr32, 0 + cost1, 0 + cost2, 88);
          break;
        case NT_index64:
          record(NT_addr64, 0 + cost1, 0 + cost2, 81);
          break;
        case NT_index32:
          record(NT_addr32, 0 + cost1, 0 + cost2, 89);
          break;
        case NT_addr32:
          record(NT_addr, 0 + cost1, 0 + cost2, 92);
          record(NT_regl, 2 + cost1, 2 + cost2, 127);
          break;
        case NT_memq:
          record(NT_mrcq, 0 + cost1, 0 + cost2, 108);
          record(NT_mregq, 0 + cost1, 0 + cost2, 110);
          record(NT_shfct, 1 + cost1, 1 + cost2, 151);
          break;
        case NT_meml:
          record(NT_mrcl, 0 + cost1, 0 + cost2, 112);
          record(NT_mregl, 0 + cost1, 0 + cost2, 114);
          break;
        case NT_memw:
          record(NT_mrcw, 0 + cost1, 0 + cost2, 116);
          record(NT_mregw, 0 + cost1, 0 + cost2, 118);
          break;
        case NT_memb:
          record(NT_mrcb, 0 + cost1, 0 + cost2, 120);
          record(NT_mregb, 0 + cost1, 0 + cost2, 122);
          break;
        case NT_memf:
          record(NT_regf, 5 + cost1, 5 + cost2, 137);
          record(NT_regmemf, 0 + cost1, 0 + cost2, 205);
          break;
        case NT_memd:
          record(NT_regd, 5 + cost1, 5 + cost2, 136);
          record(NT_regmemd, 0 + cost1, 0 + cost2, 203);
          break;
        case NT_rcq:
          record(NT_mrcq, 0 + cost1, 0 + cost2, 109);
          break;
        case NT_rcl:
          record(NT_mrcl, 0 + cost1, 0 + cost2, 113);
          break;
        case NT_rcw:
          record(NT_mrcw, 0 + cost1, 0 + cost2, 117);
          break;
        case NT_rcb:
          record(NT_mrcb, 0 + cost1, 0 + cost2, 121);
          break;
        case NT_mregq:
          record(NT_regq, 1 + cost1, 1 + cost2, 131);
          break;
        case NT_mrcl:
          record(NT_regl, 1 + cost1, 1 + cost2, 128);
          break;
        case NT_mregl:
          record(NT_regq, 1 + cost1, 1 + cost2, 219);
          break;
        case NT_mrcw:
          record(NT_regw, 1 + cost1, 1 + cost2, 129);
          break;
        case NT_mrcb:
          record(NT_regb, 1 + cost1, 1 + cost2, 130);
          break;
        case NT_bigint:
          record(NT_regq, 1 + cost1, 1 + cost2, 133);
          break;
        case NT_bigintmac:
          record(NT_regq, 1 + cost1, 1 + cost2, 135);
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
      case Op.CONVFI:
        rract22(t, kids);
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
      }
    }
    private void rract2(LirNode t, State kids[]) {
      if (t.type == 130) {
        record(NT_con8, 0, 0, 19);
      }
      if (t.type == 258) {
        record(NT_con16, 0, 0, 20);
      }
      if (t.type == 514) {
        record(NT_con32, 0, 0, 21);
        if (((LirIconst)t).value == 2) record(NT__6, 0, 0, 69);
        if (((LirIconst)t).value == 4) record(NT__7, 0, 0, 71);
        if (((LirIconst)t).value == 8) record(NT__8, 0, 0, 73);
        if (((LirIconst)t).value == 1) record(NT__9, 0, 0, 75);
        if (((LirIconst)t).value == 3) record(NT__10, 0, 0, 78);
      }
      if (t.type == 1026) {
        if (is32bitConstOrNotConst(t)) record(NT_con64, 0, 0, 22);
        if (((LirIconst)t).value == 2) record(NT__1, 0, 0, 57);
        if (((LirIconst)t).value == 4) record(NT__2, 0, 0, 59);
        if (((LirIconst)t).value == 8) record(NT__3, 0, 0, 61);
        if (((LirIconst)t).value == 1) record(NT__4, 0, 0, 63);
        if (((LirIconst)t).value == 3) record(NT__5, 0, 0, 66);
        if (conventionIsNotMac() && isConstAndNotIn32bit(t)) record(NT_bigint, 0, 0, 132);
        if (conventionIsMac() && isConstAndNotIn32bit(t)) record(NT_bigintmac, 0, 0, 134);
      }
    }
    private void rract4(LirNode t, State kids[]) {
      if (t.type == 1026) {
        if (conventionIsMac() && isX(t)) record(NT_stamacx, 0, 0, 27);
        if (conventionIsMac() && ! isX(t)) record(NT_stamac, 0, 0, 28);
        record(NT_sta, 0, 0, 29);
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
        record(NT_xregf, 0, 0, 10);
      }
      if (t.type == 1026) {
        record(NT_xregq, 0, 0, 8);
      }
      if (t.type == 1028) {
        record(NT_xregd, 0, 0, 12);
      }
    }
    private void rract8(LirNode t, State kids[]) {
      if (t.type == 1026) {
        record(NT_lab, 0, 0, 41);
      }
    }
    private void rract9(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 174);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 215);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 176);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regd] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 210);
      }
    }
    private void rract10(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_asmcon32] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon32, 0 + kids[0].cost1[NT_asmcon32] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon32] + kids[1].cost2[NT_con], 39);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_asmcon32] != 0) record(NT_base32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_asmcon32], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_asmcon32], 53);
        if (kids[0].rule[NT_asmcon32] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_base32, 0 + kids[0].cost1[NT_asmcon32] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_asmcon32] + kids[1].cost2[NT_regl], 54);
        if (kids[0].rule[NT_base32] != 0) if (kids[1].rule[NT_index32] != 0) record(NT_addr32, 0 + kids[0].cost1[NT_base32] + kids[1].cost1[NT_index32], 0 + kids[0].cost2[NT_base32] + kids[1].cost2[NT_index32], 90);
        if (kids[0].rule[NT_index32] != 0) if (kids[1].rule[NT_base32] != 0) record(NT_addr32, 0 + kids[0].cost1[NT_index32] + kids[1].cost1[NT_base32], 0 + kids[0].cost2[NT_index32] + kids[1].cost2[NT_base32], 91);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 156);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 166);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 3 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 211);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_asmcon64] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon64, 0 + kids[0].cost1[NT_asmcon64] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon64] + kids[1].cost2[NT_con], 37);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_asmcon64] != 0) record(NT_base64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_asmcon64], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_asmcon64], 44);
        if (kids[0].rule[NT_asmcon64] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_base64, 0 + kids[0].cost1[NT_asmcon64] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_asmcon64] + kids[1].cost2[NT_regq], 45);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_asmcon64] != 0) record(NT_base64, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_asmcon64], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_asmcon64], 46);
        if (kids[0].rule[NT_asmcon64] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_base64, 0 + kids[0].cost1[NT_asmcon64] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_asmcon64] + kids[1].cost2[NT_regl], 47);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_asmcon32] != 0) record(NT_base64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_asmcon32], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_asmcon32], 48);
        if (kids[0].rule[NT_asmcon32] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_base64, 0 + kids[0].cost1[NT_asmcon32] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_asmcon32] + kids[1].cost2[NT_regq], 49);
        if (kids[0].rule[NT_base64] != 0) if (kids[1].rule[NT_index64] != 0) record(NT_addr64, 0 + kids[0].cost1[NT_base64] + kids[1].cost1[NT_index64], 0 + kids[0].cost2[NT_base64] + kids[1].cost2[NT_index64], 82);
        if (kids[0].rule[NT_index64] != 0) if (kids[1].rule[NT_base64] != 0) record(NT_addr64, 0 + kids[0].cost1[NT_index64] + kids[1].cost1[NT_base64], 0 + kids[0].cost2[NT_index64] + kids[1].cost2[NT_base64], 83);
        if (kids[0].rule[NT_base64] != 0) if (kids[1].rule[NT_index32] != 0) record(NT_addr64, 0 + kids[0].cost1[NT_base64] + kids[1].cost1[NT_index32], 0 + kids[0].cost2[NT_base64] + kids[1].cost2[NT_index32], 84);
        if (kids[0].rule[NT_index64] != 0) if (kids[1].rule[NT_base32] != 0) record(NT_addr64, 0 + kids[0].cost1[NT_index64] + kids[1].cost1[NT_base32], 0 + kids[0].cost2[NT_index64] + kids[1].cost2[NT_base32], 85);
        if (kids[0].rule[NT_base32] != 0) if (kids[1].rule[NT_index64] != 0) record(NT_addr64, 0 + kids[0].cost1[NT_base32] + kids[1].cost1[NT_index64], 0 + kids[0].cost2[NT_base32] + kids[1].cost2[NT_index64], 86);
        if (kids[0].rule[NT_index32] != 0) if (kids[1].rule[NT_base64] != 0) record(NT_addr64, 0 + kids[0].cost1[NT_index32] + kids[1].cost1[NT_base64], 0 + kids[0].cost2[NT_index32] + kids[1].cost2[NT_base64], 87);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 161);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 170);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 3 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 206);
      }
    }
    private void rract11(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_asmcon32] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon32, 0 + kids[0].cost1[NT_asmcon32] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon32] + kids[1].cost2[NT_con], 40);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_base32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 55);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 157);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 3 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 212);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_asmcon64] != 0) if (kids[1].rule[NT_con] != 0) record(NT_asmcon64, 0 + kids[0].cost1[NT_asmcon64] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_asmcon64] + kids[1].cost2[NT_con], 38);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) record(NT_base64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 50);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 162);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 3 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 207);
      }
    }
    private void rract12(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__6] != 0) record(NT_index32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__6], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__6], 70);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__7] != 0) record(NT_index32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__7], 72);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__8] != 0) record(NT_index32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__8], 74);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 4 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 4 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 190);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 4 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 4 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 191);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 4 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 213);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__1] != 0) record(NT_index64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__1], 58);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__2] != 0) record(NT_index64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__2], 60);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__3] != 0) record(NT_index64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__3], 62);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 4 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 4 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 192);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 4 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 4 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 193);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 5 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 5 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 208);
      }
    }
    private void rract13(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 22 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 22 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 194);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT_regf, 18 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 18 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 214);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 22 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 22 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 195);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT_regd, 32 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 32 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 209);
      }
    }
    private void rract14(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 22 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 22 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 196);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 22 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 22 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 197);
      }
    }
    private void rract15(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 22 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 22 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 198);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 22 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 22 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 199);
      }
    }
    private void rract16(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 22 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 22 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 200);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 22 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_regq], 22 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_regq], 201);
      }
    }
    private void rract17(LirNode t, State kids[]) {
      if (t.type == 258) {
        if (kids[0].rule[NT_mregb] != 0) record(NT_regw, 1 + kids[0].cost1[NT_mregb], 1 + kids[0].cost2[NT_mregb], 224);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_mregw] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mregw], 1 + kids[0].cost2[NT_mregw], 222);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mregb], 1 + kids[0].cost2[NT_mregb], 223);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regl] != 0) record(NT_regq, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 216);
        if (kids[0].rule[NT_mregl] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mregl], 1 + kids[0].cost2[NT_mregl], 217);
        if (kids[0].rule[NT_regw] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 220);
        if (kids[0].rule[NT_regb] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 221);
      }
    }
    private void rract18(LirNode t, State kids[]) {
      if (t.type == 258) {
        if (kids[0].rule[NT_mregb] != 0) record(NT_regw, 1 + kids[0].cost1[NT_mregb], 1 + kids[0].cost2[NT_mregb], 231);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_mregw] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mregw], 1 + kids[0].cost2[NT_mregw], 229);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mregb], 1 + kids[0].cost2[NT_mregb], 230);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_meml] != 0) record(NT_regq, 1 + kids[0].cost1[NT_meml], 1 + kids[0].cost2[NT_meml], 225);
        if (kids[0].rule[NT_regl] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 226);
        if (kids[0].rule[NT_mregw] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mregw], 1 + kids[0].cost2[NT_mregw], 227);
        if (kids[0].rule[NT_mregb] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mregb], 1 + kids[0].cost2[NT_mregb], 228);
      }
    }
    private void rract19(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 234);
        if (kids[0].rule[NT_regl] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 236);
        if (kids[0].rule[NT_regw] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 237);
        if (kids[0].rule[NT_regq] != 0) record(NT__13, 0 + kids[0].cost1[NT_regq], 0 + kids[0].cost2[NT_regq], 242);
        if (kids[0].rule[NT_regl] != 0) record(NT__15, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 246);
        if (kids[0].rule[NT_regw] != 0) record(NT__16, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 248);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 233);
        if (kids[0].rule[NT_regl] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 235);
        if (kids[0].rule[NT_regq] != 0) record(NT__12, 0 + kids[0].cost1[NT_regq], 0 + kids[0].cost2[NT_regq], 240);
        if (kids[0].rule[NT_regl] != 0) record(NT__14, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 244);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 232);
        if (kids[0].rule[NT_regq] != 0) record(NT__11, 0 + kids[0].cost1[NT_regq], 0 + kids[0].cost2[NT_regq], 238);
      }
    }
    private void rract20(LirNode t, State kids[]) {
      if (t.type == 1028) {
        if (kids[0].rule[NT_regmemf] != 0) record(NT_regd, 4 + kids[0].cost1[NT_regmemf], 4 + kids[0].cost2[NT_regmemf], 258);
      }
    }
    private void rract21(LirNode t, State kids[]) {
      if (t.type == 516) {
        if (kids[0].rule[NT_regmemd] != 0) record(NT_regf, 4 + kids[0].cost1[NT_regmemd], 4 + kids[0].cost2[NT_regmemd], 259);
      }
    }
    private void rract22(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_regmemd] != 0) record(NT_regb, 4 + kids[0].cost1[NT_regmemd], 4 + kids[0].cost2[NT_regmemd], 272);
        if (kids[0].rule[NT_regmemf] != 0) record(NT_regb, 4 + kids[0].cost1[NT_regmemf], 4 + kids[0].cost2[NT_regmemf], 289);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_regmemd] != 0) record(NT_regw, 4 + kids[0].cost1[NT_regmemd], 4 + kids[0].cost2[NT_regmemd], 271);
        if (kids[0].rule[NT_regd] != 0) record(NT__21, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 275);
        if (kids[0].rule[NT_regmemf] != 0) record(NT_regw, 4 + kids[0].cost1[NT_regmemf], 4 + kids[0].cost2[NT_regmemf], 288);
        if (kids[0].rule[NT_regf] != 0) record(NT__26, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 292);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_regmemd] != 0) record(NT_regl, 4 + kids[0].cost1[NT_regmemd], 4 + kids[0].cost2[NT_regmemd], 270);
        if (kids[0].rule[NT_regd] != 0) record(NT__20, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 273);
        if (kids[0].rule[NT_regmemf] != 0) record(NT_regl, 4 + kids[0].cost1[NT_regmemf], 4 + kids[0].cost2[NT_regmemf], 287);
        if (kids[0].rule[NT_regf] != 0) record(NT__25, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 290);
      }
    }
    private void rract23(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_regd] != 0) record(NT_regb, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 263);
        if (kids[0].rule[NT_regf] != 0) record(NT_regb, 4 + kids[0].cost1[NT_regf], 4 + kids[0].cost2[NT_regf], 280);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_regd] != 0) record(NT_regw, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 262);
        if (kids[0].rule[NT_regd] != 0) record(NT__19, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 268);
        if (kids[0].rule[NT_regf] != 0) record(NT_regw, 4 + kids[0].cost1[NT_regf], 4 + kids[0].cost2[NT_regf], 279);
        if (kids[0].rule[NT_regf] != 0) record(NT__24, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 285);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_regd] != 0) record(NT_regl, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 261);
        if (kids[0].rule[NT_regd] != 0) record(NT__18, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 266);
        if (kids[0].rule[NT_regf] != 0) record(NT_regl, 4 + kids[0].cost1[NT_regf], 4 + kids[0].cost2[NT_regf], 278);
        if (kids[0].rule[NT_regf] != 0) record(NT__23, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 283);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regd] != 0) record(NT_regq, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 260);
        if (kids[0].rule[NT_regd] != 0) record(NT__17, 0 + kids[0].cost1[NT_regd], 0 + kids[0].cost2[NT_regd], 264);
        if (kids[0].rule[NT_regf] != 0) record(NT_regq, 4 + kids[0].cost1[NT_regf], 4 + kids[0].cost2[NT_regf], 277);
        if (kids[0].rule[NT_regf] != 0) record(NT__22, 0 + kids[0].cost1[NT_regf], 0 + kids[0].cost2[NT_regf], 281);
      }
    }
    private void rract25(LirNode t, State kids[]) {
      if (t.type == 516) {
        if (kids[0].rule[NT_mregq] != 0) record(NT_regf, 4 + kids[0].cost1[NT_mregq], 4 + kids[0].cost2[NT_mregq], 254);
        if (kids[0].rule[NT_mregl] != 0) record(NT_regf, 4 + kids[0].cost1[NT_mregl], 4 + kids[0].cost2[NT_mregl], 255);
        if (kids[0].rule[NT_memw] != 0) record(NT_regf, 6 + kids[0].cost1[NT_memw], 6 + kids[0].cost2[NT_memw], 256);
        if (kids[0].rule[NT_regw] != 0) record(NT_regf, 5 + kids[0].cost1[NT_regw], 5 + kids[0].cost2[NT_regw], 257);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_mregq] != 0) record(NT_regd, 4 + kids[0].cost1[NT_mregq], 4 + kids[0].cost2[NT_mregq], 250);
        if (kids[0].rule[NT_mregl] != 0) record(NT_regd, 4 + kids[0].cost1[NT_mregl], 4 + kids[0].cost2[NT_mregl], 251);
        if (kids[0].rule[NT_memw] != 0) record(NT_regd, 6 + kids[0].cost1[NT_memw], 6 + kids[0].cost2[NT_memw], 252);
        if (kids[0].rule[NT_regw] != 0) record(NT_regd, 5 + kids[0].cost1[NT_regw], 5 + kids[0].cost2[NT_regw], 253);
      }
    }
    private void rract27(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 158);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 167);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 163);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 171);
      }
    }
    private void rract28(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 159);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 168);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 164);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 172);
      }
    }
    private void rract29(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 160);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 169);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 165);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 173);
      }
    }
    private void rract30(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 175);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq], 1 + kids[0].cost2[NT_regq], 177);
      }
    }
    private void rract31(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__9] != 0) record(NT_index32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__9], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__9], 76);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__6] != 0) record(NT_index32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__6], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__6], 77);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__10] != 0) record(NT_index32, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__10], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__10], 79);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 178);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 184);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__4] != 0) record(NT_index64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__4], 64);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__1] != 0) record(NT_index64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__1], 65);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT__5] != 0) record(NT_index64, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT__5], 67);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 181);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 187);
      }
    }
    private void rract33(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 179);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 185);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 182);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 188);
      }
    }
    private void rract34(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con], 180);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_shfct], 186);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_con] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_con], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_con], 183);
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_shfct] != 0) record(NT_regq, 1 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_shfct], 1 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_shfct], 189);
      }
    }
    private void rract35(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__37, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 315);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__47, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 335);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__67, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 375);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__77, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 395);
        if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_mrcb] != 0) record(NT__87, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_mrcb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_mrcb], 415);
        if (kids[0].rule[NT_mrcb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__89, 0 + kids[0].cost1[NT_mrcb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_mrcb] + kids[1].cost2[NT_regb], 419);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__91, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 423);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__92, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 425);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__27, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 295);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__57, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 355);
      }
    }
    private void rract36(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__38, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 317);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__48, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 337);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__68, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 377);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__78, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 397);
        if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT_mrcb] != 0) record(NT__88, 0 + kids[0].cost1[NT_regb] + kids[1].cost1[NT_mrcb], 0 + kids[0].cost2[NT_regb] + kids[1].cost2[NT_mrcb], 417);
        if (kids[0].rule[NT_mrcb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT__90, 0 + kids[0].cost1[NT_mrcb] + kids[1].cost1[NT_regb], 0 + kids[0].cost2[NT_mrcb] + kids[1].cost2[NT_regb], 421);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__93, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 427);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__94, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 429);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__28, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 297);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__58, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 357);
      }
    }
    private void rract37(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__39, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 319);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__49, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 339);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__69, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 379);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__79, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 399);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__95, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 431);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__99, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 439);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__29, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 299);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__59, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 359);
      }
    }
    private void rract38(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__40, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 321);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__50, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 341);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__70, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 381);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__80, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 401);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__96, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 433);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__100, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 441);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__30, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 301);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__60, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 361);
      }
    }
    private void rract39(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__41, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 323);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__51, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 343);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__71, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 383);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__81, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 403);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__97, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 435);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__101, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 443);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__31, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 303);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__61, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 363);
      }
    }
    private void rract40(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__42, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 325);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__52, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 345);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__72, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 385);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__82, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 405);
        if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regmemd] != 0) record(NT__98, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regmemd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regmemd], 437);
        if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regmemf] != 0) record(NT__102, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regmemf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regmemf], 445);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__32, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 305);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__62, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 365);
      }
    }
    private void rract41(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__43, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 327);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__53, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 347);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__73, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 387);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__83, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 407);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__33, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 307);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__63, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 367);
      }
    }
    private void rract42(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__44, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 329);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__54, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 349);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__74, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 389);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__84, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 409);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__34, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 309);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__64, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 369);
      }
    }
    private void rract43(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__45, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 331);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__55, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 351);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__75, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 391);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__85, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 411);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__35, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 311);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__65, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 371);
      }
    }
    private void rract44(LirNode t, State kids[]) {
      if (t.type == 514) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__46, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 333);
        if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_mrcl] != 0) record(NT__56, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_mrcl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_mrcl], 353);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__76, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 393);
        if (kids[0].rule[NT_mrcl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__86, 0 + kids[0].cost1[NT_mrcl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_mrcl] + kids[1].cost2[NT_regl], 413);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_regq] != 0) if (kids[1].rule[NT_mrcq] != 0) record(NT__36, 0 + kids[0].cost1[NT_regq] + kids[1].cost1[NT_mrcq], 0 + kids[0].cost2[NT_regq] + kids[1].cost2[NT_mrcq], 313);
        if (kids[0].rule[NT_mrcq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT__66, 0 + kids[0].cost1[NT_mrcq] + kids[1].cost1[NT_regq], 0 + kids[0].cost2[NT_mrcq] + kids[1].cost2[NT_regq], 373);
      }
    }
    private void rract47(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 97);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 96);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_addr] != 0) record(NT_meml, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 95);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memf, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 98);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memq, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 94);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_addr] != 0) record(NT_memd, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 99);
      }
    }
    private void rract48(LirNode t, State kids[]) {
      if (t.type == 130) {
        if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_rcb] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_rcb], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_rcb], 142);
        if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 146);
        if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__13] != 0) if (isOverlappedReg(t.kid(0),t.kid(1).kid(0))) record(NT_void, 0 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__13], 0 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__13], 243);
        if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__15] != 0) if (isOverlappedReg(t.kid(0),t.kid(1).kid(0))) record(NT_void, 0 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__15], 0 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__15], 247);
        if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__16] != 0) if (isOverlappedReg(t.kid(0),t.kid(1).kid(0))) record(NT_void, 0 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__16], 0 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__16], 249);
      }
      if (t.type == 258) {
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT_rcw] != 0) record(NT_void, 1 + kids[0].cost1[NT_memw] + kids[1].cost1[NT_rcw], 1 + kids[0].cost2[NT_memw] + kids[1].cost2[NT_rcw], 141);
        if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regw], 145);
        if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT__12] != 0) if (isOverlappedReg(t.kid(0),t.kid(1).kid(0))) record(NT_void, 0 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT__12], 0 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT__12], 241);
        if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT__14] != 0) if (isOverlappedReg(t.kid(0),t.kid(1).kid(0))) record(NT_void, 0 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT__14], 0 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT__14], 245);
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__19] != 0) record(NT_void, 6 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__19], 6 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__19], 269);
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__21] != 0) record(NT_void, 6 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__21], 6 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__21], 276);
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__24] != 0) record(NT_void, 6 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__24], 6 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__24], 286);
        if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT__26] != 0) record(NT_void, 6 + kids[0].cost1[NT_memw] + kids[1].cost1[NT__26], 6 + kids[0].cost2[NT_memw] + kids[1].cost2[NT__26], 293);
      }
      if (t.type == 514) {
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT_rcl] != 0) record(NT_void, 1 + kids[0].cost1[NT_meml] + kids[1].cost1[NT_rcl], 1 + kids[0].cost2[NT_meml] + kids[1].cost2[NT_rcl], 140);
        if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regl], 144);
        if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT__11] != 0) if (isOverlappedReg(t.kid(0),t.kid(1).kid(0))) record(NT_void, 0 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT__11], 0 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT__11], 239);
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__18] != 0) record(NT_void, 6 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__18], 6 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__18], 267);
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__20] != 0) record(NT_void, 6 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__20], 6 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__20], 274);
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__23] != 0) record(NT_void, 6 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__23], 6 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__23], 284);
        if (kids[0].rule[NT_meml] != 0) if (kids[1].rule[NT__25] != 0) record(NT_void, 6 + kids[0].cost1[NT_meml] + kids[1].cost1[NT__25], 6 + kids[0].cost2[NT_meml] + kids[1].cost2[NT__25], 291);
      }
      if (t.type == 516) {
        if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 147);
        if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_regf], 150);
      }
      if (t.type == 1026) {
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT_rcq] != 0) if (is32bitConstOrNotConst(t.kid(1))) record(NT_void, 1 + kids[0].cost1[NT_memq] + kids[1].cost1[NT_rcq], 1 + kids[0].cost2[NT_memq] + kids[1].cost2[NT_rcq], 138);
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT_rcq] != 0) if (isConstAndNotIn32bit(t.kid(1))) record(NT_void, 2 + kids[0].cost1[NT_memq] + kids[1].cost1[NT_rcq], 2 + kids[0].cost2[NT_memq] + kids[1].cost2[NT_rcq], 139);
        if (kids[0].rule[NT_xregq] != 0) if (kids[1].rule[NT_regq] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregq] + kids[1].cost1[NT_regq], 1 + kids[0].cost2[NT_xregq] + kids[1].cost2[NT_regq], 143);
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT__17] != 0) record(NT_void, 6 + kids[0].cost1[NT_memq] + kids[1].cost1[NT__17], 6 + kids[0].cost2[NT_memq] + kids[1].cost2[NT__17], 265);
        if (kids[0].rule[NT_memq] != 0) if (kids[1].rule[NT__22] != 0) record(NT_void, 6 + kids[0].cost1[NT_memq] + kids[1].cost1[NT__22], 6 + kids[0].cost2[NT_memq] + kids[1].cost2[NT__22], 282);
      }
      if (t.type == 1028) {
        if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 148);
        if (kids[0].rule[NT_memd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_memd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_memd] + kids[1].cost2[NT_regd], 149);
      }
    }
    private void rract49(LirNode t, State kids[]) {
      if (kids[0].rule[NT_lab] != 0) record(NT_void, 3 + kids[0].cost1[NT_lab], 3 + kids[0].cost2[NT_lab], 294);
    }
    private void rract50(LirNode t, State kids[]) {
      if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__27] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__27] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 296);
      if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__28] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__28] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 298);
      if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__29] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__29] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 300);
      if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__30] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__30] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 302);
      if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__31] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__31] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 304);
      if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__32] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__32] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 306);
      if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__33] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__33] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 308);
      if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__34] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__34] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 310);
      if (kids[0].rule[NT__35] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__35] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__35] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 312);
      if (kids[0].rule[NT__36] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__36] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__36] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 314);
      if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__37] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__37] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 316);
      if (kids[0].rule[NT__38] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__38] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__38] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 318);
      if (kids[0].rule[NT__39] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__39] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__39] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 320);
      if (kids[0].rule[NT__40] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__40] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__40] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 322);
      if (kids[0].rule[NT__41] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__41] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__41] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 324);
      if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__42] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__42] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 326);
      if (kids[0].rule[NT__43] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__43] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__43] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 328);
      if (kids[0].rule[NT__44] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__44] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__44] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 330);
      if (kids[0].rule[NT__45] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__45] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__45] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 332);
      if (kids[0].rule[NT__46] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__46] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__46] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 334);
      if (kids[0].rule[NT__47] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__47] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__47] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 336);
      if (kids[0].rule[NT__48] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__48] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__48] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 338);
      if (kids[0].rule[NT__49] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__49] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__49] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 340);
      if (kids[0].rule[NT__50] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__50] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__50] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 342);
      if (kids[0].rule[NT__51] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__51] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__51] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 344);
      if (kids[0].rule[NT__52] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__52] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__52] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 346);
      if (kids[0].rule[NT__53] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__53] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__53] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 348);
      if (kids[0].rule[NT__54] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__54] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__54] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 350);
      if (kids[0].rule[NT__55] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__55] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__55] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 352);
      if (kids[0].rule[NT__56] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__56] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__56] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 354);
      if (kids[0].rule[NT__57] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__57] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__57] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 356);
      if (kids[0].rule[NT__58] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__58] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__58] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 358);
      if (kids[0].rule[NT__59] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__59] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__59] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 360);
      if (kids[0].rule[NT__60] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__60] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__60] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 362);
      if (kids[0].rule[NT__61] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__61] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__61] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 364);
      if (kids[0].rule[NT__62] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__62] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__62] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 366);
      if (kids[0].rule[NT__63] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__63] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__63] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 368);
      if (kids[0].rule[NT__64] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__64] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__64] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 370);
      if (kids[0].rule[NT__65] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__65] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__65] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 372);
      if (kids[0].rule[NT__66] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__66] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__66] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 374);
      if (kids[0].rule[NT__67] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__67] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__67] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 376);
      if (kids[0].rule[NT__68] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__68] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__68] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 378);
      if (kids[0].rule[NT__69] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__69] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__69] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 380);
      if (kids[0].rule[NT__70] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__70] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__70] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 382);
      if (kids[0].rule[NT__71] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__71] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__71] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 384);
      if (kids[0].rule[NT__72] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__72] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__72] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 386);
      if (kids[0].rule[NT__73] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__73] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__73] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 388);
      if (kids[0].rule[NT__74] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__74] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__74] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 390);
      if (kids[0].rule[NT__75] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__75] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__75] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 392);
      if (kids[0].rule[NT__76] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__76] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__76] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 394);
      if (kids[0].rule[NT__77] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__77] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__77] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 396);
      if (kids[0].rule[NT__78] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__78] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__78] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 398);
      if (kids[0].rule[NT__79] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__79] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__79] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 400);
      if (kids[0].rule[NT__80] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__80] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__80] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 402);
      if (kids[0].rule[NT__81] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__81] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__81] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 404);
      if (kids[0].rule[NT__82] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__82] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__82] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 406);
      if (kids[0].rule[NT__83] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__83] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__83] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 408);
      if (kids[0].rule[NT__84] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__84] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__84] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 410);
      if (kids[0].rule[NT__85] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__85] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__85] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 412);
      if (kids[0].rule[NT__86] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__86] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__86] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 414);
      if (kids[0].rule[NT__87] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__87] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__87] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 416);
      if (kids[0].rule[NT__88] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__88] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__88] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 418);
      if (kids[0].rule[NT__89] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__89] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__89] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 420);
      if (kids[0].rule[NT__90] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__90] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__90] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 422);
      if (kids[0].rule[NT__91] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__91] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__91] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 424);
      if (kids[0].rule[NT__92] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__92] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__92] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 426);
      if (kids[0].rule[NT__93] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__93] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__93] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 428);
      if (kids[0].rule[NT__94] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__94] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__94] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 430);
      if (kids[0].rule[NT__95] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__95] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__95] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 432);
      if (kids[0].rule[NT__96] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__96] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__96] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 434);
      if (kids[0].rule[NT__97] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__97] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__97] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 436);
      if (kids[0].rule[NT__98] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__98] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__98] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 438);
      if (kids[0].rule[NT__99] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__99] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__99] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 440);
      if (kids[0].rule[NT__100] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__100] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__100] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 442);
      if (kids[0].rule[NT__101] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__101] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__101] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 444);
      if (kids[0].rule[NT__102] != 0) if (kids[1].rule[NT_lab] != 0) if (kids[2].rule[NT_lab] != 0) record(NT_void, 4 + kids[0].cost1[NT__102] + kids[1].cost1[NT_lab] + kids[2].cost1[NT_lab], 4 + kids[0].cost2[NT__102] + kids[1].cost2[NT_lab] + kids[2].cost2[NT_lab], 446);
    }
    private void rract53(LirNode t, State kids[]) {
      if (kids[0].rule[NT_callarg] != 0) record(NT_void, 4 + kids[0].cost1[NT_callarg], 4 + kids[0].cost2[NT_callarg], 447);
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
    rrinit300();
    rrinit400();
  }
  static private void rrinit0() {
    rulev[43] = new Rule(43, true, false, 26, "43: base64 -> regq", null, ImList.list(ImList.list("base",ImList.list(),"$1")), null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[56] = new Rule(56, true, false, 28, "56: index64 -> regq", null, ImList.list(ImList.list("index","$1","1")), null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[100] = new Rule(100, true, false, 48, "100: rcq -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[111] = new Rule(111, true, false, 53, "111: mregq -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[125] = new Rule(125, true, false, 60, "125: callarg -> regq", null, ImList.list(ImList.list("ind","$1")), null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I64*"});
    rulev[152] = new Rule(152, true, false, 63, "152: shfct -> regq", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-rcx-I64*"});
    rulev[52] = new Rule(52, true, false, 27, "52: base32 -> regl", null, ImList.list(ImList.list("base",ImList.list(),"$1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[68] = new Rule(68, true, false, 34, "68: index32 -> regl", null, ImList.list(ImList.list("index","$1","1")), null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[102] = new Rule(102, true, false, 49, "102: rcl -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[115] = new Rule(115, true, false, 55, "115: mregl -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[153] = new Rule(153, true, false, 63, "153: shfct -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-ecx-I32*"});
    rulev[218] = new Rule(218, true, false, 1, "218: regq -> regl", ImList.list(ImList.list("cltq")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-rax-I64*", "*reg-eax-I32*"});
    rulev[104] = new Rule(104, true, false, 50, "104: rcw -> regw", null, null, null, 0, false, false, new int[]{3}, new String[]{null, "*reg-I16*"});
    rulev[119] = new Rule(119, true, false, 57, "119: mregw -> regw", null, null, null, 0, false, false, new int[]{3}, new String[]{null, "*reg-I16*"});
    rulev[154] = new Rule(154, true, false, 63, "154: shfct -> regw", null, null, null, 0, false, false, new int[]{3}, new String[]{null, "*reg-cx-I16*"});
    rulev[106] = new Rule(106, true, false, 51, "106: rcb -> regb", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-I8*"});
    rulev[123] = new Rule(123, true, false, 59, "123: mregb -> regb", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-I8*"});
    rulev[155] = new Rule(155, true, false, 63, "155: shfct -> regb", null, null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-cl-I8*"});
    rulev[204] = new Rule(204, true, false, 65, "204: regmemf -> regf", null, null, null, 0, false, false, new int[]{5}, new String[]{null, "*reg-F32*"});
    rulev[202] = new Rule(202, true, false, 64, "202: regmemd -> regd", null, null, null, 0, false, false, new int[]{6}, new String[]{null, "*reg-F64*"});
    rulev[13] = new Rule(13, true, false, 4, "13: regb -> xregb", null, null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I8*", null});
    rulev[14] = new Rule(14, true, false, 3, "14: regw -> xregw", null, null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I16*", null});
    rulev[15] = new Rule(15, true, false, 2, "15: regl -> xregl", null, null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I32*", null});
    rulev[16] = new Rule(16, true, false, 1, "16: regq -> xregq", null, null, null, 0, false, false, new int[]{11}, new String[]{"*reg-I64*", null});
    rulev[17] = new Rule(17, true, false, 5, "17: regf -> xregf", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-F32*", null});
    rulev[18] = new Rule(18, true, false, 6, "18: regd -> xregd", null, null, null, 0, false, false, new int[]{13}, new String[]{"*reg-F64*", null});
    rulev[23] = new Rule(23, true, false, 18, "23: con -> con8", null, null, null, 0, false, false, new int[]{14}, new String[]{null, null});
    rulev[32] = new Rule(32, true, false, 23, "32: asmcon32 -> con8", null, null, null, 0, false, false, new int[]{14}, new String[]{null, null});
    rulev[24] = new Rule(24, true, false, 18, "24: con -> con16", null, null, null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[33] = new Rule(33, true, false, 23, "33: asmcon32 -> con16", null, null, null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[25] = new Rule(25, true, false, 18, "25: con -> con32", null, null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[34] = new Rule(34, true, false, 23, "34: asmcon32 -> con32", null, null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[26] = new Rule(26, true, false, 18, "26: con -> con64", null, null, null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[35] = new Rule(35, true, false, 24, "35: asmcon64 -> con64", null, null, null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[105] = new Rule(105, true, false, 50, "105: rcw -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{18}, new String[]{null, null});
    rulev[107] = new Rule(107, true, false, 51, "107: rcb -> con", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{18}, new String[]{null, null});
    rulev[31] = new Rule(31, true, false, 22, "31: addr64 -> stamacx", null, ImList.list(ImList.list("staddrx","$1")), null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[30] = new Rule(30, true, false, 22, "30: addr64 -> stamac", null, ImList.list(ImList.list("staddr","$1")), null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[36] = new Rule(36, true, false, 24, "36: asmcon64 -> sta", null, null, null, 0, false, false, new int[]{21}, new String[]{null, null});
    rulev[124] = new Rule(124, true, false, 60, "124: callarg -> sta", null, null, null, 0, false, false, new int[]{21}, new String[]{null, null});
    rulev[93] = new Rule(93, true, false, 41, "93: addr -> addr64", null, null, null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[126] = new Rule(126, true, false, 1, "126: regq -> addr64", ImList.list(ImList.list("leaq","$1","$0")), null, null, 0, false, false, new int[]{22}, new String[]{"*reg-I64*", null});
    rulev[51] = new Rule(51, true, false, 27, "51: base32 -> asmcon32", null, ImList.list(ImList.list("base","$1",ImList.list())), null, 0, false, false, new int[]{23}, new String[]{null, null});
    rulev[103] = new Rule(103, true, false, 49, "103: rcl -> asmcon32", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{23}, new String[]{null, null});
    rulev[42] = new Rule(42, true, false, 26, "42: base64 -> asmcon64", null, ImList.list(ImList.list("base","$1",ImList.list())), null, 0, false, false, new int[]{24}, new String[]{null, null});
    rulev[101] = new Rule(101, true, false, 48, "101: rcq -> asmcon64", null, ImList.list(ImList.list("imm","$1")), null, 0, false, false, new int[]{24}, new String[]{null, null});
    rulev[80] = new Rule(80, true, false, 22, "80: addr64 -> base64", null, ImList.list(ImList.list("addr","$1",ImList.list())), null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[88] = new Rule(88, true, false, 40, "88: addr32 -> base32", null, ImList.list(ImList.list("addr","$1",ImList.list())), null, 0, false, false, new int[]{27}, new String[]{null, null});
    rulev[81] = new Rule(81, true, false, 22, "81: addr64 -> index64", null, ImList.list(ImList.list("addr",ImList.list(),"$1")), null, 0, false, false, new int[]{28}, new String[]{null, null});
    rulev[89] = new Rule(89, true, false, 40, "89: addr32 -> index32", null, ImList.list(ImList.list("addr",ImList.list(),"$1")), null, 0, false, false, new int[]{34}, new String[]{null, null});
    rulev[92] = new Rule(92, true, false, 41, "92: addr -> addr32", null, null, null, 0, false, false, new int[]{40}, new String[]{null, null});
    rulev[127] = new Rule(127, true, false, 2, "127: regl -> addr32", ImList.list(ImList.list("leal","$1","$0")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I32*", null});
    rulev[108] = new Rule(108, true, false, 52, "108: mrcq -> memq", null, null, null, 0, false, false, new int[]{42}, new String[]{null, null});
    rulev[110] = new Rule(110, true, false, 53, "110: mregq -> memq", null, null, null, 0, false, false, new int[]{42}, new String[]{null, null});
    rulev[151] = new Rule(151, true, false, 63, "151: shfct -> memq", ImList.list(ImList.list("movb","$1","$0")), null, null, 0, false, false, new int[]{42}, new String[]{"*reg-cl-I8*", null});
    rulev[112] = new Rule(112, true, false, 54, "112: mrcl -> meml", null, null, null, 0, false, false, new int[]{43}, new String[]{null, null});
    rulev[114] = new Rule(114, true, false, 55, "114: mregl -> meml", null, null, null, 0, false, false, new int[]{43}, new String[]{null, null});
    rulev[116] = new Rule(116, true, false, 56, "116: mrcw -> memw", null, null, null, 0, false, false, new int[]{44}, new String[]{null, null});
    rulev[118] = new Rule(118, true, false, 57, "118: mregw -> memw", null, null, null, 0, false, false, new int[]{44}, new String[]{null, null});
    rulev[120] = new Rule(120, true, false, 58, "120: mrcb -> memb", null, null, null, 0, false, false, new int[]{45}, new String[]{null, null});
    rulev[122] = new Rule(122, true, false, 59, "122: mregb -> memb", null, null, null, 0, false, false, new int[]{45}, new String[]{null, null});
    rulev[137] = new Rule(137, true, false, 5, "137: regf -> memf", ImList.list(ImList.list("movss","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{46}, new String[]{"*reg-F32*", null});
    rulev[205] = new Rule(205, true, false, 65, "205: regmemf -> memf", null, null, null, 0, false, false, new int[]{46}, new String[]{null, null});
    rulev[136] = new Rule(136, true, false, 6, "136: regd -> memd", ImList.list(ImList.list("movsd","$1","$0")), null, null, 0, false, false, new int[]{47}, new String[]{"*reg-F64*", null});
    rulev[203] = new Rule(203, true, false, 64, "203: regmemd -> memd", null, null, null, 0, false, false, new int[]{47}, new String[]{null, null});
    rulev[109] = new Rule(109, true, false, 52, "109: mrcq -> rcq", null, null, null, 0, false, false, new int[]{48}, new String[]{null, null});
    rulev[113] = new Rule(113, true, false, 54, "113: mrcl -> rcl", null, null, null, 0, false, false, new int[]{49}, new String[]{null, null});
    rulev[117] = new Rule(117, true, false, 56, "117: mrcw -> rcw", null, null, null, 0, false, false, new int[]{50}, new String[]{null, null});
    rulev[121] = new Rule(121, true, false, 58, "121: mrcb -> rcb", null, null, null, 0, false, false, new int[]{51}, new String[]{null, null});
    rulev[131] = new Rule(131, true, false, 1, "131: regq -> mregq", ImList.list(ImList.list("movq","$1","$0")), null, null, 0, false, false, new int[]{53}, new String[]{"*reg-I64*", null});
    rulev[128] = new Rule(128, true, false, 2, "128: regl -> mrcl", ImList.list(ImList.list("movl","$1","$0")), null, null, 0, false, false, new int[]{54}, new String[]{"*reg-I32*", null});
    rulev[219] = new Rule(219, true, false, 1, "219: regq -> mregl", ImList.list(ImList.list("movslq","$1","$0")), null, null, 0, false, false, new int[]{55}, new String[]{"*reg-I64*", null});
    rulev[129] = new Rule(129, true, false, 3, "129: regw -> mrcw", ImList.list(ImList.list("movw","$1","$0")), null, null, 0, false, false, new int[]{56}, new String[]{"*reg-I16*", null});
    rulev[130] = new Rule(130, true, false, 4, "130: regb -> mrcb", ImList.list(ImList.list("movb","$1","$0")), null, null, 0, false, false, new int[]{58}, new String[]{"*reg-I8*", null});
    rulev[133] = new Rule(133, true, false, 1, "133: regq -> bigint", ImList.list(ImList.list("movq",ImList.list("imm","$1"),"$0")), null, null, 0, false, false, new int[]{61}, new String[]{"*reg-I64*", null});
    rulev[135] = new Rule(135, true, false, 1, "135: regq -> bigintmac", ImList.list(ImList.list("movabsq",ImList.list("imm","$1"),"$0")), null, null, 0, false, false, new int[]{62}, new String[]{"*reg-I64*", null});
    rulev[19] = new Rule(19, false, false, 14, "19: con8 -> (INTCONST I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[20] = new Rule(20, false, false, 15, "20: con16 -> (INTCONST I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[21] = new Rule(21, false, false, 16, "21: con32 -> (INTCONST I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[69] = new Rule(69, false, true, 35, "69: _6 -> (INTCONST I32 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[71] = new Rule(71, false, true, 36, "71: _7 -> (INTCONST I32 4)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[73] = new Rule(73, false, true, 37, "73: _8 -> (INTCONST I32 8)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[75] = new Rule(75, false, true, 38, "75: _9 -> (INTCONST I32 1)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[78] = new Rule(78, false, true, 39, "78: _10 -> (INTCONST I32 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[22] = new Rule(22, false, false, 17, "22: con64 -> (INTCONST I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[57] = new Rule(57, false, true, 29, "57: _1 -> (INTCONST I64 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[59] = new Rule(59, false, true, 30, "59: _2 -> (INTCONST I64 4)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[61] = new Rule(61, false, true, 31, "61: _3 -> (INTCONST I64 8)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[63] = new Rule(63, false, true, 32, "63: _4 -> (INTCONST I64 1)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[66] = new Rule(66, false, true, 33, "66: _5 -> (INTCONST I64 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[132] = new Rule(132, false, false, 61, "132: bigint -> (INTCONST I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[134] = new Rule(134, false, false, 62, "134: bigintmac -> (INTCONST I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[27] = new Rule(27, false, false, 19, "27: stamacx -> (STATIC I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[28] = new Rule(28, false, false, 20, "28: stamac -> (STATIC I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[29] = new Rule(29, false, false, 21, "29: sta -> (STATIC I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 8, "1: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 9, "3: xregw -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 10, "5: xregl -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[9] = new Rule(9, false, false, 12, "9: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 11, "7: xregq -> (REG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
  }
  static private void rrinit100() {
    rulev[11] = new Rule(11, false, false, 13, "11: xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 8, "2: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[4] = new Rule(4, false, false, 9, "4: xregw -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 10, "6: xregl -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[10] = new Rule(10, false, false, 12, "10: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 11, "8: xregq -> (SUBREG I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 13, "12: xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[41] = new Rule(41, false, false, 25, "41: lab -> (LABEL I64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[174] = new Rule(174, false, false, 2, "174: regl -> (NEG I32 regl)", ImList.list(ImList.list("negl","$0")), null, null, 2, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[215] = new Rule(215, false, false, 5, "215: regf -> (NEG F32 regf)", ImList.list(ImList.list("movl",ImList.list("imm","2147483648"),"%edx"),ImList.list("movd","%edx","%xmm7"),ImList.list("xorps","%xmm7",ImList.list("fullreg","$1"))), null, ImList.list(ImList.list("REG","I32","%edx"),ImList.list("REG","F64","%xmm7")), 2, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F32*"});
    rulev[176] = new Rule(176, false, false, 1, "176: regq -> (NEG I64 regq)", ImList.list(ImList.list("negq","$0")), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[210] = new Rule(210, false, false, 6, "210: regd -> (NEG F64 regd)", ImList.list(ImList.list("movl",ImList.list("imm","128"),"%edx"),ImList.list("movd","%edx","%xmm7"),ImList.list("pslldq",ImList.list("imm","7"),"%xmm7"),ImList.list("xorpd","%xmm7","$1")), null, ImList.list(ImList.list("REG","I32","%edx"),ImList.list("REG","F64","%xmm7")), 2, false, false, new int[]{6}, new String[]{"*reg-F64*", "*reg-F64*"});
    rulev[39] = new Rule(39, false, false, 23, "39: asmcon32 -> (ADD I32 asmcon32 con)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{23,18}, new String[]{null, null, null});
    rulev[53] = new Rule(53, false, false, 27, "53: base32 -> (ADD I32 regl asmcon32)", null, ImList.list(ImList.list("base","$2","$1")), null, 0, false, false, new int[]{2,23}, new String[]{null, "*reg-I32*", null});
    rulev[54] = new Rule(54, false, false, 27, "54: base32 -> (ADD I32 asmcon32 regl)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{23,2}, new String[]{null, null, "*reg-I32*"});
    rulev[90] = new Rule(90, false, false, 40, "90: addr32 -> (ADD I32 base32 index32)", null, ImList.list(ImList.list("addr","$1","$2")), null, 0, false, false, new int[]{27,34}, new String[]{null, null, null});
    rulev[91] = new Rule(91, false, false, 40, "91: addr32 -> (ADD I32 index32 base32)", null, ImList.list(ImList.list("addr","$2","$1")), null, 0, false, false, new int[]{34,27}, new String[]{null, null, null});
    rulev[156] = new Rule(156, false, false, 2, "156: regl -> (ADD I32 regl mrcl)", ImList.list(ImList.list("addl","$2","$0")), null, null, 2, false, false, new int[]{2,54}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[166] = new Rule(166, false, false, 2, "166: regl -> (ADD I32 mrcl regl)", ImList.list(ImList.list("addl","$1","$0")), null, null, 4, false, false, new int[]{54,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[211] = new Rule(211, false, false, 5, "211: regf -> (ADD F32 regf regmemf)", ImList.list(ImList.list("addss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1"))), null, null, 2, false, false, new int[]{5,65}, new String[]{"*reg-F32*", "*reg-F32*", null});
    rulev[37] = new Rule(37, false, false, 24, "37: asmcon64 -> (ADD I64 asmcon64 con)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{24,18}, new String[]{null, null, null});
    rulev[44] = new Rule(44, false, false, 26, "44: base64 -> (ADD I64 regq asmcon64)", null, ImList.list(ImList.list("base","$2","$1")), null, 0, false, false, new int[]{1,24}, new String[]{null, "*reg-I64*", null});
    rulev[45] = new Rule(45, false, false, 26, "45: base64 -> (ADD I64 asmcon64 regq)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{24,1}, new String[]{null, null, "*reg-I64*"});
    rulev[46] = new Rule(46, false, false, 26, "46: base64 -> (ADD I64 regl asmcon64)", null, ImList.list(ImList.list("base","$2",ImList.list("extreg","$1"))), null, 0, false, false, new int[]{2,24}, new String[]{null, "*reg-I32*", null});
    rulev[47] = new Rule(47, false, false, 26, "47: base64 -> (ADD I64 asmcon64 regl)", null, ImList.list(ImList.list("base","$1",ImList.list("extreg","$2"))), null, 0, false, false, new int[]{24,2}, new String[]{null, null, "*reg-I32*"});
    rulev[48] = new Rule(48, false, false, 26, "48: base64 -> (ADD I64 regq asmcon32)", null, ImList.list(ImList.list("base","$2","$1")), null, 0, false, false, new int[]{1,23}, new String[]{null, "*reg-I64*", null});
    rulev[49] = new Rule(49, false, false, 26, "49: base64 -> (ADD I64 asmcon32 regq)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{23,1}, new String[]{null, null, "*reg-I64*"});
    rulev[82] = new Rule(82, false, false, 22, "82: addr64 -> (ADD I64 base64 index64)", null, ImList.list(ImList.list("addr","$1","$2")), null, 0, false, false, new int[]{26,28}, new String[]{null, null, null});
    rulev[83] = new Rule(83, false, false, 22, "83: addr64 -> (ADD I64 index64 base64)", null, ImList.list(ImList.list("addr","$2","$1")), null, 0, false, false, new int[]{28,26}, new String[]{null, null, null});
    rulev[84] = new Rule(84, false, false, 22, "84: addr64 -> (ADD I64 base64 index32)", null, ImList.list(ImList.list("addr","$1",ImList.list("extindex","$2"))), null, 0, false, false, new int[]{26,34}, new String[]{null, null, null});
    rulev[85] = new Rule(85, false, false, 22, "85: addr64 -> (ADD I64 index64 base32)", null, ImList.list(ImList.list("addr",ImList.list("extbase","$2"),"$1")), null, 0, false, false, new int[]{28,27}, new String[]{null, null, null});
    rulev[86] = new Rule(86, false, false, 22, "86: addr64 -> (ADD I64 base32 index64)", null, ImList.list(ImList.list("addr",ImList.list("extbase","$1"),"$2")), null, 0, false, false, new int[]{27,28}, new String[]{null, null, null});
    rulev[87] = new Rule(87, false, false, 22, "87: addr64 -> (ADD I64 index32 base64)", null, ImList.list(ImList.list("addr","$2",ImList.list("extindex","$1"))), null, 0, false, false, new int[]{34,26}, new String[]{null, null, null});
    rulev[161] = new Rule(161, false, false, 1, "161: regq -> (ADD I64 regq mrcq)", ImList.list(ImList.list("addq","$2","$0")), null, null, 2, false, false, new int[]{1,52}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[170] = new Rule(170, false, false, 1, "170: regq -> (ADD I64 mrcq regq)", ImList.list(ImList.list("addq","$1","$0")), null, null, 4, false, false, new int[]{52,1}, new String[]{"*reg-I64*", null, "*reg-I64*"});
    rulev[206] = new Rule(206, false, false, 6, "206: regd -> (ADD F64 regd regmemd)", ImList.list(ImList.list("addsd","$2","$1")), null, null, 2, false, false, new int[]{6,64}, new String[]{"*reg-F64*", "*reg-F64*", null});
    rulev[40] = new Rule(40, false, false, 23, "40: asmcon32 -> (SUB I32 asmcon32 con)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{23,18}, new String[]{null, null, null});
    rulev[55] = new Rule(55, false, false, 27, "55: base32 -> (SUB I32 regl con)", null, ImList.list(ImList.list("base",ImList.list("minus","$2"),"$1")), null, 0, false, false, new int[]{2,18}, new String[]{null, "*reg-I32*", null});
    rulev[157] = new Rule(157, false, false, 2, "157: regl -> (SUB I32 regl mrcl)", ImList.list(ImList.list("subl","$2","$0")), null, null, 2, false, false, new int[]{2,54}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[212] = new Rule(212, false, false, 5, "212: regf -> (SUB F32 regf regmemf)", ImList.list(ImList.list("subss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1"))), null, null, 2, false, false, new int[]{5,65}, new String[]{"*reg-F32*", "*reg-F32*", null});
    rulev[38] = new Rule(38, false, false, 24, "38: asmcon64 -> (SUB I64 asmcon64 con)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{24,18}, new String[]{null, null, null});
    rulev[50] = new Rule(50, false, false, 26, "50: base64 -> (SUB I64 regq con)", null, ImList.list(ImList.list("base",ImList.list("minus","$2"),"$1")), null, 0, false, false, new int[]{1,18}, new String[]{null, "*reg-I64*", null});
    rulev[162] = new Rule(162, false, false, 1, "162: regq -> (SUB I64 regq mrcq)", ImList.list(ImList.list("subq","$2","$0")), null, null, 2, false, false, new int[]{1,52}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[207] = new Rule(207, false, false, 6, "207: regd -> (SUB F64 regd regmemd)", ImList.list(ImList.list("subsd","$2","$1")), null, null, 2, false, false, new int[]{6,64}, new String[]{"*reg-F64*", "*reg-F64*", null});
    rulev[70] = new Rule(70, false, false, 34, "70: index32 -> (MUL I32 regl _6)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{2,35}, new String[]{null, "*reg-I32*"});
    rulev[72] = new Rule(72, false, false, 34, "72: index32 -> (MUL I32 regl _7)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{2,36}, new String[]{null, "*reg-I32*"});
    rulev[74] = new Rule(74, false, false, 34, "74: index32 -> (MUL I32 regl _8)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{2,37}, new String[]{null, "*reg-I32*"});
    rulev[190] = new Rule(190, false, false, 2, "190: regl -> (MUL I32 regl mrcl)", ImList.list(ImList.list("imull","$2","$0")), null, null, 2, false, false, new int[]{2,54}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[191] = new Rule(191, false, false, 2, "191: regl -> (MUL I32 mrcl regl)", ImList.list(ImList.list("imull","$1","$0")), null, null, 4, false, false, new int[]{54,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[213] = new Rule(213, false, false, 5, "213: regf -> (MUL F32 regf regmemf)", ImList.list(ImList.list("mulss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1"))), null, null, 2, false, false, new int[]{5,65}, new String[]{"*reg-F32*", "*reg-F32*", null});
    rulev[58] = new Rule(58, false, false, 28, "58: index64 -> (MUL I64 regq _1)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-I64*"});
    rulev[60] = new Rule(60, false, false, 28, "60: index64 -> (MUL I64 regq _2)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{1,30}, new String[]{null, "*reg-I64*"});
    rulev[62] = new Rule(62, false, false, 28, "62: index64 -> (MUL I64 regq _3)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{1,31}, new String[]{null, "*reg-I64*"});
    rulev[192] = new Rule(192, false, false, 1, "192: regq -> (MUL I64 regq mrcq)", ImList.list(ImList.list("imulq","$2","$0")), null, null, 2, false, false, new int[]{1,52}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[193] = new Rule(193, false, false, 1, "193: regq -> (MUL I64 mrcq regq)", ImList.list(ImList.list("imulq","$1","$0")), null, null, 4, false, false, new int[]{52,1}, new String[]{"*reg-I64*", null, "*reg-I64*"});
    rulev[208] = new Rule(208, false, false, 6, "208: regd -> (MUL F64 regd regmemd)", ImList.list(ImList.list("mulsd","$2","$1")), null, null, 2, false, false, new int[]{6,64}, new String[]{"*reg-F64*", "*reg-F64*", null});
    rulev[194] = new Rule(194, false, false, 2, "194: regl -> (DIVS I32 regl regl)", ImList.list(ImList.list("cltd"),ImList.list("idivl","$2")), null, ImList.list(ImList.list("REG","I32","%edx")), 2, false, false, new int[]{2,2}, new String[]{"*reg-eax-I32*", "*reg-I32*", "*reg-not-edx-I32*"});
    rulev[214] = new Rule(214, false, false, 5, "214: regf -> (DIVS F32 regf regmemf)", ImList.list(ImList.list("divss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1"))), null, null, 2, false, false, new int[]{5,65}, new String[]{"*reg-F32*", "*reg-F32*", null});
    rulev[195] = new Rule(195, false, false, 1, "195: regq -> (DIVS I64 regq regq)", ImList.list(ImList.list("cqto"),ImList.list("idivq","$2")), null, ImList.list(ImList.list("REG","I64","%rdx")), 2, false, false, new int[]{1,1}, new String[]{"*reg-rax-I64*", "*reg-I64*", "*reg-not-rdx-I64*"});
    rulev[209] = new Rule(209, false, false, 6, "209: regd -> (DIVS F64 regd regmemd)", ImList.list(ImList.list("divsd","$2","$1")), null, null, 2, false, false, new int[]{6,64}, new String[]{"*reg-F64*", "*reg-F64*", null});
    rulev[196] = new Rule(196, false, false, 2, "196: regl -> (DIVU I32 regl regl)", ImList.list(ImList.list("xorl","%edx","%edx"),ImList.list("divl","$2")), null, ImList.list(ImList.list("REG","I32","%edx")), 2, false, false, new int[]{2,2}, new String[]{"*reg-eax-I32*", "*reg-I32*", "*reg-not-edx-I32*"});
    rulev[197] = new Rule(197, false, false, 1, "197: regq -> (DIVU I64 regq regq)", ImList.list(ImList.list("xorq","%rdx","%rdx"),ImList.list("divq","$2")), null, ImList.list(ImList.list("REG","I64","%rdx")), 2, false, false, new int[]{1,1}, new String[]{"*reg-rax-I64*", "*reg-I64*", "*reg-not-rdx-I64*"});
    rulev[198] = new Rule(198, false, false, 2, "198: regl -> (MODS I32 regl regl)", ImList.list(ImList.list("cltd"),ImList.list("idivl","$2")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{2,2}, new String[]{"*reg-edx-I32*", "*reg-eax-I32*", "*reg-not-edx-I32*"});
    rulev[199] = new Rule(199, false, false, 1, "199: regq -> (MODS I64 regq regq)", ImList.list(ImList.list("cqto"),ImList.list("idivq","$2")), null, ImList.list(ImList.list("REG","I64","%rax")), 0, false, false, new int[]{1,1}, new String[]{"*reg-rdx-I64*", "*reg-rax-I64*", "*reg-not-rdx-I64*"});
    rulev[200] = new Rule(200, false, false, 2, "200: regl -> (MODU I32 regl regl)", ImList.list(ImList.list("xorl","%edx","%edx"),ImList.list("divl","$2")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{2,2}, new String[]{"*reg-edx-I32*", "*reg-eax-I32*", "*reg-not-edx-I32*"});
    rulev[201] = new Rule(201, false, false, 1, "201: regq -> (MODU I64 regq regq)", ImList.list(ImList.list("xorq","%rdx","%rdx"),ImList.list("divq","$2")), null, ImList.list(ImList.list("REG","I64","%rax")), 0, false, false, new int[]{1,1}, new String[]{"*reg-rdx-I64*", "*reg-rax-I64*", "*reg-not-rdx-I64*"});
    rulev[224] = new Rule(224, false, false, 3, "224: regw -> (CONVSX I16 mregb)", ImList.list(ImList.list("movsbw","$1","$0")), null, null, 0, false, false, new int[]{59}, new String[]{"*reg-I16*", null});
    rulev[222] = new Rule(222, false, false, 2, "222: regl -> (CONVSX I32 mregw)", ImList.list(ImList.list("movswl","$1","$0")), null, null, 0, false, false, new int[]{57}, new String[]{"*reg-I32*", null});
    rulev[223] = new Rule(223, false, false, 2, "223: regl -> (CONVSX I32 mregb)", ImList.list(ImList.list("movsbl","$1","$0")), null, null, 0, false, false, new int[]{59}, new String[]{"*reg-I32*", null});
    rulev[216] = new Rule(216, false, false, 1, "216: regq -> (CONVSX I64 regl)", ImList.list(ImList.list("cltq")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-rax-I64*", "*reg-eax-I32*"});
    rulev[217] = new Rule(217, false, false, 1, "217: regq -> (CONVSX I64 mregl)", ImList.list(ImList.list("movslq","$1","$0")), null, null, 0, false, false, new int[]{55}, new String[]{"*reg-I64*", null});
    rulev[220] = new Rule(220, false, false, 1, "220: regq -> (CONVSX I64 regw)", ImList.list(ImList.list("movswq","$1","$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I64*", "*reg-I16*"});
    rulev[221] = new Rule(221, false, false, 1, "221: regq -> (CONVSX I64 regb)", ImList.list(ImList.list("movsbq","$1","$0")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I64*", "*reg-I8*"});
    rulev[231] = new Rule(231, false, false, 3, "231: regw -> (CONVZX I16 mregb)", ImList.list(ImList.list("movzbw","$1","$0")), null, null, 0, false, false, new int[]{59}, new String[]{"*reg-I16*", null});
    rulev[229] = new Rule(229, false, false, 2, "229: regl -> (CONVZX I32 mregw)", ImList.list(ImList.list("movzwl","$1","$0")), null, null, 0, false, false, new int[]{57}, new String[]{"*reg-I32*", null});
    rulev[230] = new Rule(230, false, false, 2, "230: regl -> (CONVZX I32 mregb)", ImList.list(ImList.list("movzbl","$1","$0")), null, null, 0, false, false, new int[]{59}, new String[]{"*reg-I32*", null});
    rulev[225] = new Rule(225, false, false, 1, "225: regq -> (CONVZX I64 meml)", ImList.list(ImList.list("movl","$1",ImList.list("qlow","$0"))), null, null, 0, false, false, new int[]{43}, new String[]{"*reg-I64*", null});
    rulev[226] = new Rule(226, false, false, 1, "226: regq -> (CONVZX I64 regl)", ImList.list(ImList.list("movq",ImList.list("regl2q","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I64*", "*reg-I32*"});
    rulev[227] = new Rule(227, false, false, 1, "227: regq -> (CONVZX I64 mregw)", ImList.list(ImList.list("movzwq","$1","$0")), null, null, 0, false, false, new int[]{57}, new String[]{"*reg-I64*", null});
    rulev[228] = new Rule(228, false, false, 1, "228: regq -> (CONVZX I64 mregb)", ImList.list(ImList.list("movzbq","$1","$0")), null, null, 0, false, false, new int[]{59}, new String[]{"*reg-I64*", null});
    rulev[234] = new Rule(234, false, false, 4, "234: regb -> (CONVIT I8 regq)", ImList.list(ImList.list("movb",ImList.list("regblow",ImList.list("qlow","$1")),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-I64*"});
    rulev[236] = new Rule(236, false, false, 4, "236: regb -> (CONVIT I8 regl)", ImList.list(ImList.list("movb",ImList.list("regblow","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-I32*"});
    rulev[237] = new Rule(237, false, false, 4, "237: regb -> (CONVIT I8 regw)", ImList.list(ImList.list("movb",ImList.list("regblow","$1"),"$0")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I8*", "*reg-I16*"});
    rulev[242] = new Rule(242, false, true, 68, "242: _13 -> (CONVIT I8 regq)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[246] = new Rule(246, false, true, 70, "246: _15 -> (CONVIT I8 regl)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[248] = new Rule(248, false, true, 71, "248: _16 -> (CONVIT I8 regw)", null, null, null, 0, false, false, new int[]{3}, null);
    rulev[233] = new Rule(233, false, false, 3, "233: regw -> (CONVIT I16 regq)", ImList.list(ImList.list("movw",ImList.list("regwlow",ImList.list("qlow","$1")),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I64*"});
    rulev[235] = new Rule(235, false, false, 3, "235: regw -> (CONVIT I16 regl)", ImList.list(ImList.list("movw",ImList.list("regwlow","$1"),"$0")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[240] = new Rule(240, false, true, 67, "240: _12 -> (CONVIT I16 regq)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[244] = new Rule(244, false, true, 69, "244: _14 -> (CONVIT I16 regl)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[232] = new Rule(232, false, false, 2, "232: regl -> (CONVIT I32 regq)", ImList.list(ImList.list("movl",ImList.list("qlow","$1"),"$0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I64*"});
    rulev[238] = new Rule(238, false, true, 66, "238: _11 -> (CONVIT I32 regq)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[258] = new Rule(258, false, false, 6, "258: regd -> (CONVFX F64 regmemf)", ImList.list(ImList.list("cvtss2sd",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{65}, new String[]{"*reg-F64*", null});
    rulev[259] = new Rule(259, false, false, 5, "259: regf -> (CONVFT F32 regmemd)", ImList.list(ImList.list("cvtsd2ss","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{64}, new String[]{"*reg-F32*", null});
    rulev[272] = new Rule(272, false, false, 4, "272: regb -> (CONVFI I8 regmemd)", ImList.list(ImList.list("cvtsd2si",ImList.list("fullreg","$1"),ImList.list("regb2l","$0"))), null, null, 0, false, false, new int[]{64}, new String[]{"*reg-I8*", null});
    rulev[289] = new Rule(289, false, false, 4, "289: regb -> (CONVFI I8 regmemf)", ImList.list(ImList.list("cvtss2si",ImList.list("fullreg","$1"),ImList.list("regb2l","$0"))), null, null, 0, false, false, new int[]{65}, new String[]{"*reg-I8*", null});
    rulev[271] = new Rule(271, false, false, 3, "271: regw -> (CONVFI I16 regmemd)", ImList.list(ImList.list("cvtsd2si",ImList.list("fullreg","$1"),ImList.list("regw2l","$0"))), null, null, 0, false, false, new int[]{64}, new String[]{"*reg-I16*", null});
    rulev[275] = new Rule(275, false, true, 76, "275: _21 -> (CONVFI I16 regd)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[288] = new Rule(288, false, false, 3, "288: regw -> (CONVFI I16 regmemf)", ImList.list(ImList.list("cvtss2si",ImList.list("fullreg","$1"),ImList.list("regw2l","$0"))), null, null, 0, false, false, new int[]{65}, new String[]{"*reg-I16*", null});
    rulev[292] = new Rule(292, false, true, 81, "292: _26 -> (CONVFI I16 regf)", null, null, null, 0, false, false, new int[]{5}, null);
  }
  static private void rrinit200() {
    rulev[270] = new Rule(270, false, false, 2, "270: regl -> (CONVFI I32 regmemd)", ImList.list(ImList.list("cvtsd2si",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{64}, new String[]{"*reg-I32*", null});
    rulev[273] = new Rule(273, false, true, 75, "273: _20 -> (CONVFI I32 regd)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[287] = new Rule(287, false, false, 2, "287: regl -> (CONVFI I32 regmemf)", ImList.list(ImList.list("cvtss2si",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{65}, new String[]{"*reg-I32*", null});
    rulev[290] = new Rule(290, false, true, 80, "290: _25 -> (CONVFI I32 regf)", null, null, null, 0, false, false, new int[]{5}, null);
    rulev[263] = new Rule(263, false, false, 4, "263: regb -> (CONVFS I8 regd)", ImList.list(ImList.list("cvttsd2si",ImList.list("fullreg","$1"),ImList.list("regb2l","$0"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I8*", "*reg-F64*"});
    rulev[280] = new Rule(280, false, false, 4, "280: regb -> (CONVFS I8 regf)", ImList.list(ImList.list("cvttss2si",ImList.list("fullreg","$1"),ImList.list("regb2l","$0"))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I8*", "*reg-F32*"});
    rulev[262] = new Rule(262, false, false, 3, "262: regw -> (CONVFS I16 regd)", ImList.list(ImList.list("cvttsd2si",ImList.list("fullreg","$1"),ImList.list("regw2l","$0"))), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I16*", "*reg-F64*"});
    rulev[268] = new Rule(268, false, true, 74, "268: _19 -> (CONVFS I16 regd)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[279] = new Rule(279, false, false, 3, "279: regw -> (CONVFS I16 regf)", ImList.list(ImList.list("cvttss2si",ImList.list("fullreg","$1"),ImList.list("regw2l","$0"))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I16*", "*reg-F32*"});
    rulev[285] = new Rule(285, false, true, 79, "285: _24 -> (CONVFS I16 regf)", null, null, null, 0, false, false, new int[]{5}, null);
    rulev[261] = new Rule(261, false, false, 2, "261: regl -> (CONVFS I32 regd)", ImList.list(ImList.list("cvttsd2si",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-F64*"});
    rulev[266] = new Rule(266, false, true, 73, "266: _18 -> (CONVFS I32 regd)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[278] = new Rule(278, false, false, 2, "278: regl -> (CONVFS I32 regf)", ImList.list(ImList.list("cvttss2si",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I32*", "*reg-F32*"});
    rulev[283] = new Rule(283, false, true, 78, "283: _23 -> (CONVFS I32 regf)", null, null, null, 0, false, false, new int[]{5}, null);
    rulev[260] = new Rule(260, false, false, 1, "260: regq -> (CONVFS I64 regd)", ImList.list(ImList.list("cvttsd2siq",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-I64*", "*reg-F64*"});
    rulev[264] = new Rule(264, false, true, 72, "264: _17 -> (CONVFS I64 regd)", null, null, null, 0, false, false, new int[]{6}, null);
    rulev[277] = new Rule(277, false, false, 1, "277: regq -> (CONVFS I64 regf)", ImList.list(ImList.list("cvttss2siq",ImList.list("fullreg","$1"),"$0")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I64*", "*reg-F32*"});
    rulev[281] = new Rule(281, false, true, 77, "281: _22 -> (CONVFS I64 regf)", null, null, null, 0, false, false, new int[]{5}, null);
    rulev[254] = new Rule(254, false, false, 5, "254: regf -> (CONVSF F32 mregq)", ImList.list(ImList.list("cvtsi2ssq","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{53}, new String[]{"*reg-F32*", null});
    rulev[255] = new Rule(255, false, false, 5, "255: regf -> (CONVSF F32 mregl)", ImList.list(ImList.list("cvtsi2ss","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{55}, new String[]{"*reg-F32*", null});
    rulev[256] = new Rule(256, false, false, 5, "256: regf -> (CONVSF F32 memw)", ImList.list(ImList.list("movswl","$1","%eax"),ImList.list("cvtsi2ss","%eax",ImList.list("fullreg","$0"))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{44}, new String[]{"*reg-F32*", null});
    rulev[257] = new Rule(257, false, false, 5, "257: regf -> (CONVSF F32 regw)", ImList.list(ImList.list("movswl","$1","$1"),ImList.list("cvtsi2ss","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-F32*", "*reg-I16*"});
    rulev[250] = new Rule(250, false, false, 6, "250: regd -> (CONVSF F64 mregq)", ImList.list(ImList.list("cvtsi2sdq","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{53}, new String[]{"*reg-F64*", null});
    rulev[251] = new Rule(251, false, false, 6, "251: regd -> (CONVSF F64 mregl)", ImList.list(ImList.list("cvtsi2sd","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{55}, new String[]{"*reg-F64*", null});
    rulev[252] = new Rule(252, false, false, 6, "252: regd -> (CONVSF F64 memw)", ImList.list(ImList.list("movswl","$1","%eax"),ImList.list("cvtsi2sd","%eax",ImList.list("fullreg","$0"))), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{44}, new String[]{"*reg-F64*", null});
    rulev[253] = new Rule(253, false, false, 6, "253: regd -> (CONVSF F64 regw)", ImList.list(ImList.list("movswl","$1","$1"),ImList.list("cvtsi2sd","$1",ImList.list("fullreg","$0"))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-F64*", "*reg-I16*"});
    rulev[158] = new Rule(158, false, false, 2, "158: regl -> (BAND I32 regl mrcl)", ImList.list(ImList.list("andl","$2","$0")), null, null, 2, false, false, new int[]{2,54}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[167] = new Rule(167, false, false, 2, "167: regl -> (BAND I32 mrcl regl)", ImList.list(ImList.list("andl","$1","$0")), null, null, 4, false, false, new int[]{54,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[163] = new Rule(163, false, false, 1, "163: regq -> (BAND I64 regq mrcq)", ImList.list(ImList.list("andq","$2","$0")), null, null, 2, false, false, new int[]{1,52}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[171] = new Rule(171, false, false, 1, "171: regq -> (BAND I64 mrcq regq)", ImList.list(ImList.list("andq","$1","$0")), null, null, 4, false, false, new int[]{52,1}, new String[]{"*reg-I64*", null, "*reg-I64*"});
    rulev[159] = new Rule(159, false, false, 2, "159: regl -> (BOR I32 regl mrcl)", ImList.list(ImList.list("orl","$2","$0")), null, null, 2, false, false, new int[]{2,54}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[168] = new Rule(168, false, false, 2, "168: regl -> (BOR I32 mrcl regl)", ImList.list(ImList.list("orl","$1","$0")), null, null, 4, false, false, new int[]{54,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[164] = new Rule(164, false, false, 1, "164: regq -> (BOR I64 regq mrcq)", ImList.list(ImList.list("orq","$2","$0")), null, null, 2, false, false, new int[]{1,52}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[172] = new Rule(172, false, false, 1, "172: regq -> (BOR I64 mrcq regq)", ImList.list(ImList.list("orq","$1","$0")), null, null, 4, false, false, new int[]{52,1}, new String[]{"*reg-I64*", null, "*reg-I64*"});
    rulev[160] = new Rule(160, false, false, 2, "160: regl -> (BXOR I32 regl mrcl)", ImList.list(ImList.list("xorl","$2","$0")), null, null, 2, false, false, new int[]{2,54}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[169] = new Rule(169, false, false, 2, "169: regl -> (BXOR I32 mrcl regl)", ImList.list(ImList.list("xorl","$1","$0")), null, null, 4, false, false, new int[]{54,2}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[165] = new Rule(165, false, false, 1, "165: regq -> (BXOR I64 regq mrcq)", ImList.list(ImList.list("xorq","$2","$0")), null, null, 2, false, false, new int[]{1,52}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[173] = new Rule(173, false, false, 1, "173: regq -> (BXOR I64 mrcq regq)", ImList.list(ImList.list("xorq","$1","$0")), null, null, 4, false, false, new int[]{52,1}, new String[]{"*reg-I64*", null, "*reg-I64*"});
    rulev[175] = new Rule(175, false, false, 2, "175: regl -> (BNOT I32 regl)", ImList.list(ImList.list("notl","$0")), null, null, 2, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[177] = new Rule(177, false, false, 1, "177: regq -> (BNOT I64 regq)", ImList.list(ImList.list("notq","$0")), null, null, 2, false, false, new int[]{1}, new String[]{"*reg-I64*", "*reg-I64*"});
    rulev[76] = new Rule(76, false, false, 34, "76: index32 -> (LSHS I32 regl _9)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{2,38}, new String[]{null, "*reg-I32*"});
    rulev[77] = new Rule(77, false, false, 34, "77: index32 -> (LSHS I32 regl _6)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{2,35}, new String[]{null, "*reg-I32*"});
    rulev[79] = new Rule(79, false, false, 34, "79: index32 -> (LSHS I32 regl _10)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{2,39}, new String[]{null, "*reg-I32*"});
    rulev[178] = new Rule(178, false, false, 2, "178: regl -> (LSHS I32 regl con)", ImList.list(ImList.list("sall",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[184] = new Rule(184, false, false, 2, "184: regl -> (LSHS I32 regl shfct)", ImList.list(ImList.list("sall","%cl","$0")), null, null, 2, false, false, new int[]{2,63}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[64] = new Rule(64, false, false, 28, "64: index64 -> (LSHS I64 regq _4)", null, ImList.list(ImList.list("index","$1","2")), null, 0, false, false, new int[]{1,32}, new String[]{null, "*reg-I64*"});
    rulev[65] = new Rule(65, false, false, 28, "65: index64 -> (LSHS I64 regq _1)", null, ImList.list(ImList.list("index","$1","4")), null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-I64*"});
    rulev[67] = new Rule(67, false, false, 28, "67: index64 -> (LSHS I64 regq _5)", null, ImList.list(ImList.list("index","$1","8")), null, 0, false, false, new int[]{1,33}, new String[]{null, "*reg-I64*"});
    rulev[181] = new Rule(181, false, false, 1, "181: regq -> (LSHS I64 regq con)", ImList.list(ImList.list("salq",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{1,18}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[187] = new Rule(187, false, false, 1, "187: regq -> (LSHS I64 regq shfct)", ImList.list(ImList.list("salq","%cl","$0")), null, null, 2, false, false, new int[]{1,63}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[179] = new Rule(179, false, false, 2, "179: regl -> (RSHS I32 regl con)", ImList.list(ImList.list("sarl",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[185] = new Rule(185, false, false, 2, "185: regl -> (RSHS I32 regl shfct)", ImList.list(ImList.list("sarl","%cl","$0")), null, null, 2, false, false, new int[]{2,63}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[182] = new Rule(182, false, false, 1, "182: regq -> (RSHS I64 regq con)", ImList.list(ImList.list("sarq",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{1,18}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[188] = new Rule(188, false, false, 1, "188: regq -> (RSHS I64 regq shfct)", ImList.list(ImList.list("sarq","%cl","$0")), null, null, 2, false, false, new int[]{1,63}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[180] = new Rule(180, false, false, 2, "180: regl -> (RSHU I32 regl con)", ImList.list(ImList.list("shrl",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{2,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[186] = new Rule(186, false, false, 2, "186: regl -> (RSHU I32 regl shfct)", ImList.list(ImList.list("shrl","%cl","$0")), null, null, 2, false, false, new int[]{2,63}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[183] = new Rule(183, false, false, 1, "183: regq -> (RSHU I64 regq con)", ImList.list(ImList.list("shrq",ImList.list("imm","$2"),"$0")), null, null, 2, false, false, new int[]{1,18}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[189] = new Rule(189, false, false, 1, "189: regq -> (RSHU I64 regq shfct)", ImList.list(ImList.list("shrq","%cl","$0")), null, null, 2, false, false, new int[]{1,63}, new String[]{"*reg-I64*", "*reg-I64*", null});
    rulev[315] = new Rule(315, false, true, 92, "315: _37 -> (TSTEQ I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[335] = new Rule(335, false, true, 102, "335: _47 -> (TSTEQ I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[375] = new Rule(375, false, true, 122, "375: _67 -> (TSTEQ I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[395] = new Rule(395, false, true, 132, "395: _77 -> (TSTEQ I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[415] = new Rule(415, false, true, 142, "415: _87 -> (TSTEQ I32 regb mrcb)", null, null, null, 0, false, false, new int[]{4,58}, null);
    rulev[419] = new Rule(419, false, true, 144, "419: _89 -> (TSTEQ I32 mrcb regb)", null, null, null, 0, false, false, new int[]{58,4}, null);
    rulev[423] = new Rule(423, false, true, 146, "423: _91 -> (TSTEQ I32 regd regmemd)", null, null, null, 0, false, false, new int[]{6,64}, null);
    rulev[425] = new Rule(425, false, true, 147, "425: _92 -> (TSTEQ I32 regf regmemf)", null, null, null, 0, false, false, new int[]{5,65}, null);
    rulev[295] = new Rule(295, false, true, 82, "295: _27 -> (TSTEQ I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[355] = new Rule(355, false, true, 112, "355: _57 -> (TSTEQ I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[317] = new Rule(317, false, true, 93, "317: _38 -> (TSTNE I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[337] = new Rule(337, false, true, 103, "337: _48 -> (TSTNE I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[377] = new Rule(377, false, true, 123, "377: _68 -> (TSTNE I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[397] = new Rule(397, false, true, 133, "397: _78 -> (TSTNE I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[417] = new Rule(417, false, true, 143, "417: _88 -> (TSTNE I32 regb mrcb)", null, null, null, 0, false, false, new int[]{4,58}, null);
    rulev[421] = new Rule(421, false, true, 145, "421: _90 -> (TSTNE I32 mrcb regb)", null, null, null, 0, false, false, new int[]{58,4}, null);
    rulev[427] = new Rule(427, false, true, 148, "427: _93 -> (TSTNE I32 regd regmemd)", null, null, null, 0, false, false, new int[]{6,64}, null);
    rulev[429] = new Rule(429, false, true, 149, "429: _94 -> (TSTNE I32 regf regmemf)", null, null, null, 0, false, false, new int[]{5,65}, null);
    rulev[297] = new Rule(297, false, true, 83, "297: _28 -> (TSTNE I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[357] = new Rule(357, false, true, 113, "357: _58 -> (TSTNE I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[319] = new Rule(319, false, true, 94, "319: _39 -> (TSTLTS I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[339] = new Rule(339, false, true, 104, "339: _49 -> (TSTLTS I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[379] = new Rule(379, false, true, 124, "379: _69 -> (TSTLTS I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[399] = new Rule(399, false, true, 134, "399: _79 -> (TSTLTS I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[431] = new Rule(431, false, true, 150, "431: _95 -> (TSTLTS I32 regd regmemd)", null, null, null, 0, false, false, new int[]{6,64}, null);
    rulev[439] = new Rule(439, false, true, 154, "439: _99 -> (TSTLTS I32 regf regmemf)", null, null, null, 0, false, false, new int[]{5,65}, null);
    rulev[299] = new Rule(299, false, true, 84, "299: _29 -> (TSTLTS I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[359] = new Rule(359, false, true, 114, "359: _59 -> (TSTLTS I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[321] = new Rule(321, false, true, 95, "321: _40 -> (TSTLES I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[341] = new Rule(341, false, true, 105, "341: _50 -> (TSTLES I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[381] = new Rule(381, false, true, 125, "381: _70 -> (TSTLES I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[401] = new Rule(401, false, true, 135, "401: _80 -> (TSTLES I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[433] = new Rule(433, false, true, 151, "433: _96 -> (TSTLES I32 regd regmemd)", null, null, null, 0, false, false, new int[]{6,64}, null);
    rulev[441] = new Rule(441, false, true, 155, "441: _100 -> (TSTLES I32 regf regmemf)", null, null, null, 0, false, false, new int[]{5,65}, null);
    rulev[301] = new Rule(301, false, true, 85, "301: _30 -> (TSTLES I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[361] = new Rule(361, false, true, 115, "361: _60 -> (TSTLES I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[323] = new Rule(323, false, true, 96, "323: _41 -> (TSTGTS I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[343] = new Rule(343, false, true, 106, "343: _51 -> (TSTGTS I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[383] = new Rule(383, false, true, 126, "383: _71 -> (TSTGTS I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[403] = new Rule(403, false, true, 136, "403: _81 -> (TSTGTS I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[435] = new Rule(435, false, true, 152, "435: _97 -> (TSTGTS I32 regd regmemd)", null, null, null, 0, false, false, new int[]{6,64}, null);
    rulev[443] = new Rule(443, false, true, 156, "443: _101 -> (TSTGTS I32 regf regmemf)", null, null, null, 0, false, false, new int[]{5,65}, null);
  }
  static private void rrinit300() {
    rulev[303] = new Rule(303, false, true, 86, "303: _31 -> (TSTGTS I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[363] = new Rule(363, false, true, 116, "363: _61 -> (TSTGTS I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[325] = new Rule(325, false, true, 97, "325: _42 -> (TSTGES I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[345] = new Rule(345, false, true, 107, "345: _52 -> (TSTGES I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[385] = new Rule(385, false, true, 127, "385: _72 -> (TSTGES I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[405] = new Rule(405, false, true, 137, "405: _82 -> (TSTGES I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[437] = new Rule(437, false, true, 153, "437: _98 -> (TSTGES I32 regd regmemd)", null, null, null, 0, false, false, new int[]{6,64}, null);
    rulev[445] = new Rule(445, false, true, 157, "445: _102 -> (TSTGES I32 regf regmemf)", null, null, null, 0, false, false, new int[]{5,65}, null);
    rulev[305] = new Rule(305, false, true, 87, "305: _32 -> (TSTGES I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[365] = new Rule(365, false, true, 117, "365: _62 -> (TSTGES I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[327] = new Rule(327, false, true, 98, "327: _43 -> (TSTLTU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[347] = new Rule(347, false, true, 108, "347: _53 -> (TSTLTU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[387] = new Rule(387, false, true, 128, "387: _73 -> (TSTLTU I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[407] = new Rule(407, false, true, 138, "407: _83 -> (TSTLTU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[307] = new Rule(307, false, true, 88, "307: _33 -> (TSTLTU I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[367] = new Rule(367, false, true, 118, "367: _63 -> (TSTLTU I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[329] = new Rule(329, false, true, 99, "329: _44 -> (TSTLEU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[349] = new Rule(349, false, true, 109, "349: _54 -> (TSTLEU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[389] = new Rule(389, false, true, 129, "389: _74 -> (TSTLEU I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[409] = new Rule(409, false, true, 139, "409: _84 -> (TSTLEU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[309] = new Rule(309, false, true, 89, "309: _34 -> (TSTLEU I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[369] = new Rule(369, false, true, 119, "369: _64 -> (TSTLEU I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[331] = new Rule(331, false, true, 100, "331: _45 -> (TSTGTU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[351] = new Rule(351, false, true, 110, "351: _55 -> (TSTGTU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[391] = new Rule(391, false, true, 130, "391: _75 -> (TSTGTU I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[411] = new Rule(411, false, true, 140, "411: _85 -> (TSTGTU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[311] = new Rule(311, false, true, 90, "311: _35 -> (TSTGTU I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[371] = new Rule(371, false, true, 120, "371: _65 -> (TSTGTU I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[333] = new Rule(333, false, true, 101, "333: _46 -> (TSTGEU I32 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[353] = new Rule(353, false, true, 111, "353: _56 -> (TSTGEU I32 regl mrcl)", null, null, null, 0, false, false, new int[]{2,54}, null);
    rulev[393] = new Rule(393, false, true, 131, "393: _76 -> (TSTGEU I32 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[413] = new Rule(413, false, true, 141, "413: _86 -> (TSTGEU I32 mrcl regl)", null, null, null, 0, false, false, new int[]{54,2}, null);
    rulev[313] = new Rule(313, false, true, 91, "313: _36 -> (TSTGEU I64 regq mrcq)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[373] = new Rule(373, false, true, 121, "373: _66 -> (TSTGEU I64 mrcq regq)", null, null, null, 0, false, false, new int[]{52,1}, null);
    rulev[97] = new Rule(97, false, false, 45, "97: memb -> (MEM I8 addr)", null, ImList.list(ImList.list("mem","byte","$1")), null, 0, false, false, new int[]{41}, new String[]{null, null});
    rulev[96] = new Rule(96, false, false, 44, "96: memw -> (MEM I16 addr)", null, ImList.list(ImList.list("mem","word","$1")), null, 0, false, false, new int[]{41}, new String[]{null, null});
    rulev[95] = new Rule(95, false, false, 43, "95: meml -> (MEM I32 addr)", null, ImList.list(ImList.list("mem","long","$1")), null, 0, false, false, new int[]{41}, new String[]{null, null});
    rulev[98] = new Rule(98, false, false, 46, "98: memf -> (MEM F32 addr)", null, ImList.list(ImList.list("mem","float","$1")), null, 0, false, false, new int[]{41}, new String[]{null, null});
    rulev[94] = new Rule(94, false, false, 42, "94: memq -> (MEM I64 addr)", null, ImList.list(ImList.list("mem","quad","$1")), null, 0, false, false, new int[]{41}, new String[]{null, null});
    rulev[99] = new Rule(99, false, false, 47, "99: memd -> (MEM F64 addr)", null, ImList.list(ImList.list("mem","double","$1")), null, 0, false, false, new int[]{41}, new String[]{null, null});
    rulev[142] = new Rule(142, false, false, 7, "142: void -> (SET I8 memb rcb)", ImList.list(ImList.list("movb","$2","$1")), null, null, 0, false, false, new int[]{45,51}, new String[]{null, null, null});
    rulev[146] = new Rule(146, false, false, 7, "146: void -> (SET I8 xregb regb)", ImList.list(ImList.list("movb","$2","$1")), null, null, 0, false, false, new int[]{8,4}, new String[]{null, null, "*reg-I8*"});
    rulev[243] = new Rule(243, false, false, 7, "243: void -> (SET I8 xregb _13)", null, null, null, 0, false, false, new int[]{8,68}, new String[]{null, null, "*reg-I64*"});
    rulev[247] = new Rule(247, false, false, 7, "247: void -> (SET I8 xregb _15)", null, null, null, 0, false, false, new int[]{8,70}, new String[]{null, null, "*reg-I32*"});
    rulev[249] = new Rule(249, false, false, 7, "249: void -> (SET I8 xregb _16)", null, null, null, 0, false, false, new int[]{8,71}, new String[]{null, null, "*reg-I16*"});
    rulev[141] = new Rule(141, false, false, 7, "141: void -> (SET I16 memw rcw)", ImList.list(ImList.list("movw","$2","$1")), null, null, 0, false, false, new int[]{44,50}, new String[]{null, null, null});
    rulev[145] = new Rule(145, false, false, 7, "145: void -> (SET I16 xregw regw)", ImList.list(ImList.list("movw","$2","$1")), null, null, 0, false, false, new int[]{9,3}, new String[]{null, null, "*reg-I16*"});
    rulev[241] = new Rule(241, false, false, 7, "241: void -> (SET I16 xregw _12)", null, null, null, 0, false, false, new int[]{9,67}, new String[]{null, null, "*reg-I64*"});
    rulev[245] = new Rule(245, false, false, 7, "245: void -> (SET I16 xregw _14)", null, null, null, 0, false, false, new int[]{9,69}, new String[]{null, null, "*reg-I32*"});
    rulev[269] = new Rule(269, false, false, 7, "269: void -> (SET I16 memw _19)", ImList.list(ImList.list("cvttsd2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movw","%ax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{44,74}, new String[]{null, null, "*reg-F64*"});
    rulev[276] = new Rule(276, false, false, 7, "276: void -> (SET I16 memw _21)", ImList.list(ImList.list("cvtsd2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movw","%ax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{44,76}, new String[]{null, null, "*reg-F64*"});
    rulev[286] = new Rule(286, false, false, 7, "286: void -> (SET I16 memw _24)", ImList.list(ImList.list("cvttss2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movw","%ax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{44,79}, new String[]{null, null, "*reg-F32*"});
    rulev[293] = new Rule(293, false, false, 7, "293: void -> (SET I16 memw _26)", ImList.list(ImList.list("cvtss2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movw","%ax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{44,81}, new String[]{null, null, "*reg-F32*"});
    rulev[140] = new Rule(140, false, false, 7, "140: void -> (SET I32 meml rcl)", ImList.list(ImList.list("movl","$2","$1")), null, null, 0, false, false, new int[]{43,49}, new String[]{null, null, null});
    rulev[144] = new Rule(144, false, false, 7, "144: void -> (SET I32 xregl regl)", ImList.list(ImList.list("movl","$2","$1")), null, null, 0, false, false, new int[]{10,2}, new String[]{null, null, "*reg-I32*"});
    rulev[239] = new Rule(239, false, false, 7, "239: void -> (SET I32 xregl _11)", null, null, null, 0, false, false, new int[]{10,66}, new String[]{null, null, "*reg-I64*"});
    rulev[267] = new Rule(267, false, false, 7, "267: void -> (SET I32 meml _18)", ImList.list(ImList.list("cvttsd2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movl","%eax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{43,73}, new String[]{null, null, "*reg-F64*"});
    rulev[274] = new Rule(274, false, false, 7, "274: void -> (SET I32 meml _20)", ImList.list(ImList.list("cvtsd2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movl","%eax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{43,75}, new String[]{null, null, "*reg-F64*"});
    rulev[284] = new Rule(284, false, false, 7, "284: void -> (SET I32 meml _23)", ImList.list(ImList.list("cvttss2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movl","%eax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{43,78}, new String[]{null, null, "*reg-F32*"});
    rulev[291] = new Rule(291, false, false, 7, "291: void -> (SET I32 meml _25)", ImList.list(ImList.list("cvtss2si",ImList.list("fullreg","$2"),"%eax"),ImList.list("movl","%eax","$1")), null, ImList.list(ImList.list("REG","I32","%eax")), 0, false, false, new int[]{43,80}, new String[]{null, null, "*reg-F32*"});
    rulev[147] = new Rule(147, false, false, 7, "147: void -> (SET F32 xregf regf)", ImList.list(ImList.list("movss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1"))), null, null, 0, false, false, new int[]{12,5}, new String[]{null, null, "*reg-F32*"});
    rulev[150] = new Rule(150, false, false, 7, "150: void -> (SET F32 memf regf)", ImList.list(ImList.list("movss",ImList.list("fullreg","$2"),"$1")), null, null, 0, false, false, new int[]{46,5}, new String[]{null, null, "*reg-F32*"});
    rulev[138] = new Rule(138, false, false, 7, "138: void -> (SET I64 memq rcq)", ImList.list(ImList.list("movq","$2","$1")), null, null, 0, false, false, new int[]{42,48}, new String[]{null, null, null});
    rulev[139] = new Rule(139, false, false, 7, "139: void -> (SET I64 memq rcq)", ImList.list(ImList.list("movl",ImList.list("qlow","$2"),"$1"),ImList.list("movl",ImList.list("qhigh","$2"),ImList.list("after","$1","4"))), null, null, 0, false, false, new int[]{42,48}, new String[]{null, null, null});
    rulev[143] = new Rule(143, false, false, 7, "143: void -> (SET I64 xregq regq)", ImList.list(ImList.list("movq","$2","$1")), null, null, 0, false, false, new int[]{11,1}, new String[]{null, null, "*reg-I64*"});
    rulev[265] = new Rule(265, false, false, 7, "265: void -> (SET I64 memq _17)", ImList.list(ImList.list("cvttsd2siq",ImList.list("fullreg","$2"),"%rax"),ImList.list("movq","%rax","$1")), null, ImList.list(ImList.list("REG","I64","%rax")), 0, false, false, new int[]{42,72}, new String[]{null, null, "*reg-F64*"});
    rulev[282] = new Rule(282, false, false, 7, "282: void -> (SET I64 memq _22)", ImList.list(ImList.list("cvttss2siq",ImList.list("fullreg","$2"),"%rax"),ImList.list("movq","%rax","$1")), null, ImList.list(ImList.list("REG","I64","%rax")), 0, false, false, new int[]{42,77}, new String[]{null, null, "*reg-F32*"});
    rulev[148] = new Rule(148, false, false, 7, "148: void -> (SET F64 xregd regd)", ImList.list(ImList.list("movsd","$2","$1")), null, null, 0, false, false, new int[]{13,6}, new String[]{null, null, "*reg-F64*"});
    rulev[149] = new Rule(149, false, false, 7, "149: void -> (SET F64 memd regd)", ImList.list(ImList.list("movsd","$2","$1")), null, null, 0, false, false, new int[]{47,6}, new String[]{null, null, "*reg-F64*"});
    rulev[294] = new Rule(294, false, false, 7, "294: void -> (JUMP _ lab)", ImList.list(ImList.list("jmp","$1")), null, null, 0, false, false, new int[]{25}, new String[]{null, null});
    rulev[296] = new Rule(296, false, false, 7, "296: void -> (JUMPC _ _27 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{82,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[298] = new Rule(298, false, false, 7, "298: void -> (JUMPC _ _28 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{83,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[300] = new Rule(300, false, false, 7, "300: void -> (JUMPC _ _29 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{84,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[302] = new Rule(302, false, false, 7, "302: void -> (JUMPC _ _30 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{85,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[304] = new Rule(304, false, false, 7, "304: void -> (JUMPC _ _31 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{86,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[306] = new Rule(306, false, false, 7, "306: void -> (JUMPC _ _32 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{87,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[308] = new Rule(308, false, false, 7, "308: void -> (JUMPC _ _33 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{88,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[310] = new Rule(310, false, false, 7, "310: void -> (JUMPC _ _34 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{89,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[312] = new Rule(312, false, false, 7, "312: void -> (JUMPC _ _35 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{90,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[314] = new Rule(314, false, false, 7, "314: void -> (JUMPC _ _36 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{91,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[316] = new Rule(316, false, false, 7, "316: void -> (JUMPC _ _37 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{92,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[318] = new Rule(318, false, false, 7, "318: void -> (JUMPC _ _38 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{93,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[320] = new Rule(320, false, false, 7, "320: void -> (JUMPC _ _39 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{94,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[322] = new Rule(322, false, false, 7, "322: void -> (JUMPC _ _40 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{95,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[324] = new Rule(324, false, false, 7, "324: void -> (JUMPC _ _41 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{96,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[326] = new Rule(326, false, false, 7, "326: void -> (JUMPC _ _42 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{97,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[328] = new Rule(328, false, false, 7, "328: void -> (JUMPC _ _43 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{98,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[330] = new Rule(330, false, false, 7, "330: void -> (JUMPC _ _44 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{99,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[332] = new Rule(332, false, false, 7, "332: void -> (JUMPC _ _45 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{100,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[334] = new Rule(334, false, false, 7, "334: void -> (JUMPC _ _46 lab lab)", ImList.list(ImList.list("cmpq","$2","$1"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{101,25,25}, new String[]{null, "*reg-I64*", null, null, null});
    rulev[336] = new Rule(336, false, false, 7, "336: void -> (JUMPC _ _47 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{102,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[338] = new Rule(338, false, false, 7, "338: void -> (JUMPC _ _48 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{103,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[340] = new Rule(340, false, false, 7, "340: void -> (JUMPC _ _49 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{104,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[342] = new Rule(342, false, false, 7, "342: void -> (JUMPC _ _50 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{105,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[344] = new Rule(344, false, false, 7, "344: void -> (JUMPC _ _51 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{106,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[346] = new Rule(346, false, false, 7, "346: void -> (JUMPC _ _52 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{107,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[348] = new Rule(348, false, false, 7, "348: void -> (JUMPC _ _53 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{108,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[350] = new Rule(350, false, false, 7, "350: void -> (JUMPC _ _54 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{109,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[352] = new Rule(352, false, false, 7, "352: void -> (JUMPC _ _55 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{110,25,25}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[354] = new Rule(354, false, false, 7, "354: void -> (JUMPC _ _56 lab lab)", ImList.list(ImList.list("cmpl","$2","$1"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{111,25,25}, new String[]{null, "*reg-I32*", null, null, null});
  }
  static private void rrinit400() {
    rulev[356] = new Rule(356, false, false, 7, "356: void -> (JUMPC _ _57 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{112,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[358] = new Rule(358, false, false, 7, "358: void -> (JUMPC _ _58 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{113,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[360] = new Rule(360, false, false, 7, "360: void -> (JUMPC _ _59 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{114,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[362] = new Rule(362, false, false, 7, "362: void -> (JUMPC _ _60 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{115,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[364] = new Rule(364, false, false, 7, "364: void -> (JUMPC _ _61 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{116,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[366] = new Rule(366, false, false, 7, "366: void -> (JUMPC _ _62 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{117,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[368] = new Rule(368, false, false, 7, "368: void -> (JUMPC _ _63 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{118,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[370] = new Rule(370, false, false, 7, "370: void -> (JUMPC _ _64 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{119,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[372] = new Rule(372, false, false, 7, "372: void -> (JUMPC _ _65 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{120,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[374] = new Rule(374, false, false, 7, "374: void -> (JUMPC _ _66 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{121,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[376] = new Rule(376, false, false, 7, "376: void -> (JUMPC _ _67 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{122,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[378] = new Rule(378, false, false, 7, "378: void -> (JUMPC _ _68 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{123,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[380] = new Rule(380, false, false, 7, "380: void -> (JUMPC _ _69 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{124,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[382] = new Rule(382, false, false, 7, "382: void -> (JUMPC _ _70 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{125,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[384] = new Rule(384, false, false, 7, "384: void -> (JUMPC _ _71 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{126,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[386] = new Rule(386, false, false, 7, "386: void -> (JUMPC _ _72 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{127,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[388] = new Rule(388, false, false, 7, "388: void -> (JUMPC _ _73 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{128,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[390] = new Rule(390, false, false, 7, "390: void -> (JUMPC _ _74 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{129,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[392] = new Rule(392, false, false, 7, "392: void -> (JUMPC _ _75 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{130,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[394] = new Rule(394, false, false, 7, "394: void -> (JUMPC _ _76 lab lab)", ImList.list(ImList.list("cmpq","$1","$2"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{131,25,25}, new String[]{null, null, "*reg-I64*", null, null});
    rulev[396] = new Rule(396, false, false, 7, "396: void -> (JUMPC _ _77 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{132,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[398] = new Rule(398, false, false, 7, "398: void -> (JUMPC _ _78 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{133,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[400] = new Rule(400, false, false, 7, "400: void -> (JUMPC _ _79 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jg","$3")), null, null, 0, false, false, new int[]{134,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[402] = new Rule(402, false, false, 7, "402: void -> (JUMPC _ _80 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jge","$3")), null, null, 0, false, false, new int[]{135,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[404] = new Rule(404, false, false, 7, "404: void -> (JUMPC _ _81 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jl","$3")), null, null, 0, false, false, new int[]{136,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[406] = new Rule(406, false, false, 7, "406: void -> (JUMPC _ _82 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jle","$3")), null, null, 0, false, false, new int[]{137,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[408] = new Rule(408, false, false, 7, "408: void -> (JUMPC _ _83 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{138,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[410] = new Rule(410, false, false, 7, "410: void -> (JUMPC _ _84 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{139,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[412] = new Rule(412, false, false, 7, "412: void -> (JUMPC _ _85 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{140,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[414] = new Rule(414, false, false, 7, "414: void -> (JUMPC _ _86 lab lab)", ImList.list(ImList.list("cmpl","$1","$2"),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{141,25,25}, new String[]{null, null, "*reg-I32*", null, null});
    rulev[416] = new Rule(416, false, false, 7, "416: void -> (JUMPC _ _87 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{142,25,25}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[418] = new Rule(418, false, false, 7, "418: void -> (JUMPC _ _88 lab lab)", ImList.list(ImList.list("cmpb","$2","$1"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{143,25,25}, new String[]{null, "*reg-I8*", null, null, null});
    rulev[420] = new Rule(420, false, false, 7, "420: void -> (JUMPC _ _89 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{144,25,25}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[422] = new Rule(422, false, false, 7, "422: void -> (JUMPC _ _90 lab lab)", ImList.list(ImList.list("cmpb","$1","$2"),ImList.list("jne","$3")), null, null, 0, false, false, new int[]{145,25,25}, new String[]{null, null, "*reg-I8*", null, null});
    rulev[424] = new Rule(424, false, false, 7, "424: void -> (JUMPC _ _91 lab lab)", ImList.list(ImList.list("ucomisd",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jp","$4"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{146,25,25}, new String[]{null, "*reg-F64*", null, null, null});
    rulev[426] = new Rule(426, false, false, 7, "426: void -> (JUMPC _ _92 lab lab)", ImList.list(ImList.list("ucomiss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jp","$4"),ImList.list("je","$3")), null, null, 0, false, false, new int[]{147,25,25}, new String[]{null, "*reg-F32*", null, null, null});
    rulev[428] = new Rule(428, false, false, 7, "428: void -> (JUMPC _ _93 lab lab)", ImList.list(ImList.list("ucomisd",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jne","$3"),ImList.list("jp","$3")), null, null, 0, false, false, new int[]{148,25,25}, new String[]{null, "*reg-F64*", null, null, null});
    rulev[430] = new Rule(430, false, false, 7, "430: void -> (JUMPC _ _94 lab lab)", ImList.list(ImList.list("ucomiss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jne","$3"),ImList.list("jp","$3")), null, null, 0, false, false, new int[]{149,25,25}, new String[]{null, "*reg-F32*", null, null, null});
    rulev[432] = new Rule(432, false, false, 7, "432: void -> (JUMPC _ _95 lab lab)", ImList.list(ImList.list("ucomisd",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{150,25,25}, new String[]{null, "*reg-F64*", null, null, null});
    rulev[434] = new Rule(434, false, false, 7, "434: void -> (JUMPC _ _96 lab lab)", ImList.list(ImList.list("ucomisd",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{151,25,25}, new String[]{null, "*reg-F64*", null, null, null});
    rulev[436] = new Rule(436, false, false, 7, "436: void -> (JUMPC _ _97 lab lab)", ImList.list(ImList.list("ucomisd",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{152,25,25}, new String[]{null, "*reg-F64*", null, null, null});
    rulev[438] = new Rule(438, false, false, 7, "438: void -> (JUMPC _ _98 lab lab)", ImList.list(ImList.list("ucomisd",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{153,25,25}, new String[]{null, "*reg-F64*", null, null, null});
    rulev[440] = new Rule(440, false, false, 7, "440: void -> (JUMPC _ _99 lab lab)", ImList.list(ImList.list("ucomiss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jb","$3")), null, null, 0, false, false, new int[]{154,25,25}, new String[]{null, "*reg-F32*", null, null, null});
    rulev[442] = new Rule(442, false, false, 7, "442: void -> (JUMPC _ _100 lab lab)", ImList.list(ImList.list("ucomiss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jbe","$3")), null, null, 0, false, false, new int[]{155,25,25}, new String[]{null, "*reg-F32*", null, null, null});
    rulev[444] = new Rule(444, false, false, 7, "444: void -> (JUMPC _ _101 lab lab)", ImList.list(ImList.list("ucomiss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("ja","$3")), null, null, 0, false, false, new int[]{156,25,25}, new String[]{null, "*reg-F32*", null, null, null});
    rulev[446] = new Rule(446, false, false, 7, "446: void -> (JUMPC _ _102 lab lab)", ImList.list(ImList.list("ucomiss",ImList.list("fullreg","$2"),ImList.list("fullreg","$1")),ImList.list("jae","$3")), null, null, 0, false, false, new int[]{157,25,25}, new String[]{null, "*reg-F32*", null, null, null});
    rulev[447] = new Rule(447, false, false, 7, "447: void -> (CALL _ callarg)", ImList.list(ImList.list("call","$1")), null, null, 0, false, false, new int[]{60}, new String[]{null, null});
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
      return jmac2(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "+")
      return jmac3(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-")
      return jmac4(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-32")
      return jmac5(emitObject(form.elem(1)));
    else if (name == "imm")
      return jmac6(emitObject(form.elem(1)));
    else if (name == "ind")
      return jmac7(emitObject(form.elem(1)));
    else if (name == "mem")
      return jmac8(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "staddr")
      return jmac9(emitObject(form.elem(1)));
    else if (name == "staddrx")
      return jmac10(emitObject(form.elem(1)));
    else if (name == "addr")
      return jmac11(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "base")
      return jmac12(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "index")
      return jmac13(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regwlow")
      return jmac14(emitObject(form.elem(1)));
    else if (name == "regblow")
      return jmac15(emitObject(form.elem(1)));
    else if (name == "qlow")
      return jmac16(emitObject(form.elem(1)));
    else if (name == "extreg")
      return jmac17(emitObject(form.elem(1)));
    else if (name == "extindex")
      return jmac18(emitObject(form.elem(1)));
    else if (name == "extbase")
      return jmac19(emitObject(form.elem(1)));
    else if (name == "qhigh")
      return jmac20(emitObject(form.elem(1)));
    else if (name == "regw2l")
      return jmac21(emitObject(form.elem(1)));
    else if (name == "regb2l")
      return jmac22(emitObject(form.elem(1)));
    else if (name == "regl2q")
      return jmac23(emitObject(form.elem(1)));
    else if (name == "fullreg")
      return jmac24(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac25(form.elem(1));
    else if (name == "epilogue")
      return jmac26(form.elem(1), emitObject(form.elem(2)));
    else if (name == "minus")
      return jmac27(emitObject(form.elem(1)));
    else if (name == "line")
      return jmac28(emitObject(form.elem(1)));
    else if (name == "symbol")
      return jmac29(emitObject(form.elem(1)));
    else if (name == "genasm")
      return jmac30(emitObject(form.elem(1)), form.elem(2));
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
  
  private boolean isOverlappedReg(LirNode node1, LirNode node2){
     if ((node1.opCode != Op.REG) && (node1.opCode != Op.SUBREG))
       return false;
     if ((node2.opCode != Op.REG) && (node2.opCode != Op.SUBREG))
       return false;
     return machineParams.isOverlapped(node1, node2);
  }
  
  private boolean isPower2(LirNode node){
    if (node instanceof LirIconst){
      long value = ((LirIconst)node).signedValue();
      return (value & (value - 1)) == 0;
    }
    return false;
  }
  
  private boolean isSmallConst(LirNode node1, LirNode node2){
    return isSmallConst(node1) || isSmallConst(node2);
  }
  
  private boolean isSmallConst(LirNode node){
    if (node instanceof LirIconst){
      long value = ((LirIconst)node).signedValue();
      return (2 <= value) && (value <= 10);
    }
    return false;
  }
  
  private boolean is32bitConstOrNotConst(LirNode node) {
    if (node instanceof LirIconst) {
      long value = ((LirIconst)node).signedValue();
      return (-2147483648 <= value) && (value <= 2147483647);
    }
    return true;
  }
  
  private boolean isConstAndNotIn32bit(LirNode node) {
    if (node instanceof LirIconst) {
      long value = ((LirIconst)node).signedValue();
      return (-2147483648 > value) || (value > 2147483647);
    }
    return false;
  }
  
  private boolean conventionIsNotMac() {
    return convention != "mac";
  }
  
  private boolean conventionIsMac() {
    return convention == "mac";
  }
  
  private boolean isX(LirNode node) {
      SymStatic sym = (SymStatic)((LirSymRef) node).symbol;
      return sym.linkage == "XREF";
  }
  
  ImList regCallClobbers = new ImList(ImList.list("REG","I64","%rax"), new ImList(ImList.list("REG","I64","%rcx"), new ImList(ImList.list("REG","I64","%rdx"), new ImList(ImList.list("REG","I64","%rsi"), new ImList(ImList.list("REG","I64","%rdi"), new ImList(ImList.list("REG","I64","%r8"), new ImList(ImList.list("REG","I64","%r9"), new ImList(ImList.list("REG","I64","%r10"), new ImList(ImList.list("REG","I64","%r11"), new ImList(ImList.list("REG","F64","%xmm0"), new ImList(ImList.list("REG","F64","%xmm1"), new ImList(ImList.list("REG","F64","%xmm2"), new ImList(ImList.list("REG","F64","%xmm3"), new ImList(ImList.list("REG","F64","%xmm4"), new ImList(ImList.list("REG","F64","%xmm5"), new ImList(ImList.list("REG","F64","%xmm6"), new ImList(ImList.list("REG","F64","%xmm7"), new ImList(ImList.list("REG","F64","%xmm8"), new ImList(ImList.list("REG","F64","%xmm9"), new ImList(ImList.list("REG","F64","%xmm10"), ImList.list(ImList.list("REG","F64","%xmm11"),ImList.list("REG","F64","%xmm12"),ImList.list("REG","F64","%xmm13"),ImList.list("REG","F64","%xmm14"),ImList.list("REG","F64","%xmm15"))))))))))))))))))))));
  
  
  /** X86_64's function attribute **/
  static class X86_64Attr extends FunctionAttr {
  
    /** Maximum stack space used by call. **/
    int stackRequired;
  
    /** alloca called in it **/
    boolean allocaCalled;
    boolean varArgFunction;
    LirNode lastArg0;  // last arg of prologue
    LirNode lastArg;  // last arg of va_start
  
    boolean rbpUsed;
    int numberOfCALLs;
    int stackParams;
    int paramIcount; 
    int paramFcount;
  
    boolean isRecursive;
  
    X86_64Attr(Function func) {
      super(func);
      stackRequired = 0;
      allocaCalled = false;
      varArgFunction = false;
      stackParams = 0;
      isRecursive = false;
    }
  }
  
  FunctionAttr newFunctionAttr(Function func) {
    return new X86_64Attr(func);
  }
  
  
  /** Set alloca called. **/
  void setAllocaCalled() {
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    attr.allocaCalled = true;
  }
  
  LirNode setVaStartCalled(LirNode callNode) {
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    attr.varArgFunction = true;
    attr.lastArg = callNode.kid(1).kid(1);
    checkLastArg(attr);
    func.localSymtab.addSymbol("__argSaveArea", Storage.FRAME, Type.AGGREGATE, 
                               16, -176, null);
    return callNode;
  }
  
  void checkLastArg(X86_64Attr attr) {
    if (attr.lastArg != attr.lastArg0)
      ; //warning: second parameter of 'va_start' not last named argument
  }
  
  LirNode setVaStartCalledLate(LirNode callNode, BiList pre) {
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    LirNode va_list = callNode.kid(1).kid(0);
    LirNode bpreg = regnode(I64, "%rbp");
    LirNode int_address = lir.node(Op.MEM, I64, lir.node
                          (Op.ADD, I64, va_list, lir.iconst(I64, 16)));
    LirNode stack_address = lir.node(Op.MEM, I64, lir.node
                          (Op.ADD, I64, va_list, lir.iconst(I64, 8)));
    LirNode int_offset = lir.node(Op.MEM, I32, va_list);
    LirNode float_offset = lir.node(Op.MEM, I32, lir.node
                          (Op.ADD, I64, va_list, lir.iconst(I64, 4)));
    pre.add(lir.node(Op.SET, I64, int_address, lir.node
                       (Op.SUB, I64, bpreg, lir.iconst(I64, 176))));
    pre.add(lir.node(Op.SET, I64, stack_address, lir.node
                       (Op.ADD, I64, bpreg, lir.iconst(I64, 16))));
    pre.add(lir.node(Op.SET, I32, int_offset, lir.iconst(I32, attr.paramIcount * 8)));
    return lir.node(Op.SET, I32, float_offset, lir.iconst(I32, attr.paramFcount * 16 + 48));
  }
  
  LirNode rewriteVaArg(LirNode node, BiList pre) {
    LirNode ret = node.kid(2).kid(0);
    LirNode lastArg = node.kid(1).kid(1);
    LirNode va_list = node.kid(1).kid(0);
    LirNode int_address = lir.node(Op.MEM, I64, lir.node
                          (Op.ADD, I64, va_list, lir.iconst(I64, 16)));
    LirNode stack_address = lir.node(Op.MEM, I64, lir.node
                          (Op.ADD, I64, va_list, lir.iconst(I64, 8)));
    LirNode int_offset = lir.node(Op.MEM, I32, va_list);
    LirNode float_offset = lir.node(Op.MEM, I32, lir.node
                          (Op.ADD, I64, va_list, lir.iconst(I64, 4)));
    LirNode tmp32 = func.newTemp(int_offset.type);
    LirNode tmp64 = func.newTemp(int_address.type);
    Label trueLabel = new Label(".Lva"+lir.getLabelVariant());
    Label falseLabel = new Label(".Lva"+lir.getLabelVariant());
    Label joinLabel = new Label(".Lva"+lir.getLabelVariant());
    switch (Type.tag(lastArg.type)) {
        case Type.INT:
           pre.add(lir.node
                   (Op.JUMPC, I64, lir.node
                    (Op.TSTLTS, I32, int_offset, lir.iconst(I32, 48)),
                     lir.labelRef(trueLabel), lir.labelRef(falseLabel)));
           pre.add(lir.node
                   (Op.DEFLABEL, I64, lir.labelRef(trueLabel)));
           pre.add(lir.node
                   (Op.SET, I64, tmp64, lir.node
                    (Op.ADD, I64, int_address, int_offset.makeCopy(lir))));
           pre.add(lir.node
                   (Op.SET, I32, int_offset.makeCopy(lir), lir.node
                    (Op.ADD, I32, int_offset.makeCopy(lir), lir.iconst(I32, 8))));
           pre.add(lir.node
                   (Op.JUMP, I64, lir.labelRef(joinLabel)));
           pre.add(lir.node
                   (Op.DEFLABEL, I64, lir.labelRef(falseLabel)));
           pre.add(lir.node
                   (Op.SET, I64, tmp64, stack_address));
           pre.add(lir.node
                   (Op.SET, I64, stack_address.makeCopy(lir), lir.node
                    (Op.ADD, I64, tmp64, lir.iconst(I64, 8))));
           pre.add(lir.node
                   (Op.JUMP, I64, lir.labelRef(joinLabel)));
           pre.add(lir.node
                   (Op.DEFLABEL, I64, lir.labelRef(joinLabel)));
           func.touch();
           return lir.node(Op.SET, ret.type, ret, tmp64);
        case Type.FLOAT:
           pre.add(lir.node
                   (Op.JUMPC, I64, lir.node
                    (Op.TSTLTS, I32, float_offset, lir.iconst(I32, 176)),
                     lir.labelRef(trueLabel), lir.labelRef(falseLabel)));
           pre.add(lir.node
                   (Op.DEFLABEL, I64, lir.labelRef(trueLabel)));
           pre.add(lir.node
          		   (Op.SET, I32, tmp32, float_offset.makeCopy(lir)));
           pre.add(lir.node
                   (Op.SET, I64, tmp64, lir.node
                    (Op.ADD, I64, int_address, tmp32)));
           pre.add(lir.node
                   (Op.SET, I32, float_offset.makeCopy(lir), lir.node
                    (Op.ADD, I32, tmp32, lir.iconst(I32, 16))));
           pre.add(lir.node
                   (Op.JUMP, I64, lir.labelRef(joinLabel)));
           pre.add(lir.node
                   (Op.DEFLABEL, I64, lir.labelRef(falseLabel)));
           pre.add(lir.node
                   (Op.SET, I64, tmp64, stack_address.makeCopy(lir)));
           pre.add(lir.node
                   (Op.SET, I64, stack_address.makeCopy(lir), lir.node
                    (Op.ADD, I64, tmp64, lir.iconst(I64, 8))));
           pre.add(lir.node
                   (Op.JUMP, I64, lir.labelRef(joinLabel)));
           pre.add(lir.node
                   (Op.DEFLABEL, I64, lir.labelRef(joinLabel)));
           func.touch();
           return lir.node(Op.SET, ret.type, ret, tmp64);
       default: //return null;
           throw new CantHappenException("Unsupported type in va_list");
    }
  }
  
  void setFuncAttr(int numberOfCALLs, int maxStackOffset){
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    attr.numberOfCALLs = numberOfCALLs;
    attr.stackRequired = maxStackOffset;
  }
  
  void setStackParams(int stackParams) {
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    attr.stackParams = stackParams;
  }
  
  void setParamCount(int paramIcount, int paramFcount, LirNode lastArg) {
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    attr.paramIcount = paramIcount;
    attr.paramFcount = paramFcount;
    attr.lastArg0 = lastArg;
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
    Symbol rbp = func.getSymbol("%rbp");
    int off = ((SymAuto)((LirSymRef)node).symbol).offset();
    return lir.node
      (Op.ADD, node.type, lir.symRef(rbp), lir.iconst(I64, (long)off));
  }
  
  
  /** Return early time pre-rewriting sequence. **/
  public Transformer[] earlyRewritingSequence() {
    return new Transformer[] {
      localEarlyRewritingTrig
    };
  }
  
  
  /** Return late time pre-rewriting sequence. **/
  public Transformer[] lateRewritingSequence() {
    return new Transformer[] {
  //    AggregatePropagation.trig,
      localLateRewritingTrig,
      TailRecursionTrig,
      ProcessFramesTrig
    };
  }
  
   final LocalTransformer TailRecursionTrig = new LocalTransformer() {
        public boolean doIt(Function f, ImList args) {
          func = f;
          lir = f.newLir;
          tailRecursion();
          return true;
        }
  
        public boolean doIt(Data data, ImList args) { return true; }
        
        public String name() { return "TailRecursion"; }
  
        public String subject() { return "Tail Recursion Elimination"; }
   };
  
  private void tailRecursion() {
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    if ( ! attr.isRecursive )  return;
    Label funcBeginLabel = new Label(func.symbol.name + "_begin");
    List selfVoidCalls = new ArrayList();
    List afterCalls = new ArrayList();
    LirNode currentDefLabel = null;
    BiList bl = func.lirList();
    for (BiLink bp = bl.first(); !bp.atEnd(); bp = bp.next()) {
      LirNode nextNode = (LirNode)bp.elem();
      switch (nextNode.opCode) {
      case Op.PROLOGUE:
          bp.addAfter(lir.node(Op.DEFLABEL, I64, lir.labelRef(funcBeginLabel)));
          break;
      case Op.PARALLEL: 
        // check (PARALLEL (CALL self)) (SET functionvalue) (SET returnvalue functionvalue) (JUMP)
        //  or  (PARALLEL (CALL self)) (JUMP to EPILOGUE)
          LirNode callNode = (LirNode)nextNode.kid(0);
          if (callNode.opCode != Op.CALL) break;
          if ( ! (callNode.kid(0) instanceof LirSymRef) ) break;
          LirSymRef calledFunc = (LirSymRef)callNode.kid(0);
          if (calledFunc.symbol.name != func.symbol.name) break;
          LirNode nextNode1 = (LirNode)bp.next().elem();
          if (nextNode1.opCode == Op.SET) {
            LirNode leftSym = (LirNode)nextNode1.kid(0);
            if ( ! (leftSym instanceof LirSymRef) ) break;
            if ( ((LirSymRef)leftSym).symbol.name.indexOf("functionvalue") < 0 ) break;
            nextNode1 = (LirNode)bp.next().next().elem();
            if (nextNode1.opCode != Op.SET) break;
            leftSym = (LirNode)nextNode1.kid(0);
            if ( ! (leftSym instanceof LirSymRef) ) break;
            if ( ((LirSymRef)leftSym).symbol.name.indexOf("returnvalue") < 0 ) break;
            leftSym = (LirNode)nextNode1.kid(1); // rightSym
            if ( ! (leftSym instanceof LirSymRef) ) break;
            if ( ((LirSymRef)leftSym).symbol.name.indexOf("functionvalue") < 0 ) break;
            nextNode1 = (LirNode)bp.next().next().next().elem();
            if ( ! nextNode1.isBranch() ) break;
            BiLink bp1, bp2, bp3, bp4;
            bp1 = bp; bp2 = bp1.next(); bp3 = bp2.next(); bp4 = bp3.next();
            bp.addBefore(lir.node(Op.JUMP, I64, lir.labelRef(funcBeginLabel)));
            bp = bp.prev();
            bp1.unlink(); bp2.unlink(); bp3.unlink(); bp4.unlink();
            func.touch();
            break;
          } else if (nextNode1.opCode == Op.JUMP) {
            selfVoidCalls.add(bp);
            afterCalls.add((LirLabelRef)nextNode1.kid(0));
          }
          break;
      case Op.DEFLABEL:
          currentDefLabel = nextNode;
          break;
      case Op.EPILOGUE:
          LirLabelRef currentLabel = (LirLabelRef)currentDefLabel.kid(0);
          for (int i = 0; i < selfVoidCalls.size(); i++) {
             if ((LirLabelRef)afterCalls.get(i) == currentLabel) {
                BiLink tp1 = (BiLink)selfVoidCalls.get(i);
                BiLink tp2 = tp1.next();
                tp1.addBefore(lir.node(Op.JUMP, I64, lir.labelRef(funcBeginLabel)));
                tp1.unlink(); tp2.unlink();
                func.touch();
             }
          }
          break;
      default: break;
      }
    }
  }
  
  LirNode rewriteDIVMODby1(LirNode node, BiList pre, int opCode){
    LirNode src2 = node.kid(1);
    if (!(src2 instanceof LirIconst))
      return node;
    long src2Value = ((LirIconst)src2).signedValue();
    if (src2Value == 1){
      switch (opCode){
      case Op.DIVS:
      case Op.DIVU:
        return node.kid(0);
      case Op.MODS: 
      case Op.MODU:
        return lir.iconst(node.type, 0);
      }
    }
    return node;
  }
  
  LirNode rewriteDIVMOD(LirNode node, BiList pre, int opCode){
    LirNode src2 = node.kid(1);
    LirNode dst;
    if (isPower2(src2)){
      dst = rewriteDIVMODtoShift(node, pre, opCode);
      if (dst != null)
        return dst;
    }
    LirNode src1 = node.kid(0);
    LirNode areg = regnode(src1.type, (src1.type == I32 ? "%eax" : "%rax"));
    if (src1.equals(areg))
      return node;
    dst = func.newTemp(node.type);
    pre.add(lir.node(Op.SET, src1.type, areg, src1));
    pre.add(lir.node
             (Op.SET, dst.type, dst, lir.node
               (opCode, src1.type, areg, src2)));
    return dst;
  }
  
  LirNode rewriteDIVMODtoShift(LirNode node, BiList pre, int opCode){
    LirNode src1 = node.kid(0);
    LirNode src2 = node.kid(1);
    int typeBits = Type.bits(src1.type);
    LirNode dst, temp;
    long src2Value = ((LirIconst)src2).signedValue();
    int k; long j = src2Value;
    for (k = 0; (j >>= 1) > 0; k++)
         ;
    if (k >= typeBits)
      return null; 
    if (k != 0 && src1.opCode != Op.REG){
        src1 = func.newTemp(src1.type);
        pre.add(lir.node(Op.SET, src1.type, src1, node.kid(0)));
    }
  
    switch (opCode){
    case Op.DIVS:
  //    if (k == 0)
  //      return src1;
      dst = func.newTemp(node.type);
      temp = func.newTemp(node.type);
      if (k == 1)
        pre.add(lir.node
                (Op.SET, temp.type, temp, lir.node
                 (Op.RSHU, src1.type, src1, lir.iconst(src2.type, typeBits - 1))));
      else
        pre.add(lir.node
                (Op.SET, temp.type, temp, lir.node
                 (Op.RSHU, src1.type, lir.node
                  (Op.RSHS, src1.type, src1, lir.iconst(src2.type, k-1)),
                  lir.iconst(src2.type, typeBits - k)))); 
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.RSHS, temp.type, lir.node
                (Op.ADD, temp.type, temp, src1),
                lir.iconst(src2.type, k))));
      return dst;
    case Op.MODS:
  //    if (k == 0) 
  //      return lir.iconst(node.type, 0);
      dst = func.newTemp(node.type);
      temp = func.newTemp(node.type);
      if (k == 1)
        pre.add(lir.node
                (Op.SET, temp.type, temp, lir.node
                 (Op.RSHU, src1.type, src1, lir.iconst(src2.type, typeBits - 1))));
      else
        pre.add(lir.node
                (Op.SET, temp.type, temp, lir.node
                 (Op.RSHU, src1.type, lir.node
                  (Op.RSHS, src1.type, src1, lir.iconst(src2.type, k-1)),
                  lir.iconst(src2.type, typeBits - k)))); 
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.SUB, src1.type, src1, lir.node
                (Op.BAND, temp.type, lir.node
                 (Op.ADD, temp.type, temp, src1),
                 lir.iconst(src2.type, - src2Value)))));
      return dst;
    case Op.DIVU:
  //    if (k == 0)
  //      return src1;
      return lir.node(Op.RSHU, src1.type, src1, lir.iconst(src2.type, k));
    case Op.MODU:
  //    if (k == 0) 
  //      return lir.iconst(node.type, 0);
      return lir.node(Op.BAND, src1.type, src1, 
                               lir.iconst(src2.type, src2Value - 1));
    default: return node; // for javac check
    }
  }
  
  LirNode rewriteMULtoShift(LirNode node, BiList pre){
    LirNode src1 = node.kid(0);
    LirNode src2 = node.kid(1);
    if (isSmallConst(src1)) {
       src1 = src2;
       src2 = node.kid(0);
    }
    if (src1.opCode != Op.REG) {
      LirNode src1temp = func.newTemp(src1.type);
      pre.add(lir.node(Op.SET, src1temp.type, src1temp, src1));
      src1 = src1temp;
    }
    LirNode dst = func.newTemp(node.type);
    int src2Value = (int)((LirIconst)src2).signedValue();
    switch (src2Value) {
    case 2:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 1))));
      break;
    case 3:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.ADD, src1.type, src1, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 1)))));
      break;
    case 4:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 2))));
      break;
    case 5:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.ADD, src1.type, src1, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 2)))));
      break;
    case 6:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.LSHS, src1.type, lir.node
                (Op.ADD, src1.type, src1, lir.node
                 (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 1))),
                lir.iconst(src2.type, 1))));
      break;
    case 7:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.SUB, src1.type, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 3)),
                src1)));
      break;
    case 8:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 3))));
      break;
    case 9:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.ADD, src1.type, lir.node
                (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 3)),
                src1)));
      break;
    case 10:
      pre.add(lir.node
              (Op.SET, dst.type, dst, lir.node
               (Op.LSHS, src1.type, lir.node
                (Op.ADD, src1.type, src1, lir.node
                 (Op.LSHS, src1.type, src1, lir.iconst(src2.type, 2))),
                lir.iconst(src2.type, 1))));
      break;
    default: return null;
    }
    return dst;
  }
  
  static final int MAX_I_REGPARAM = 6;
  static final String[][] IREGPARAM = { {"%rdi", "%edi", "%di", "%dil"},
                                        {"%rsi", "%esi", "%si", "%sil"},
                                        {"%rdx", "%edx", "%dx", "%dl"},
                                        {"%rcx", "%ecx", "%cx", "%cl"},
                                        {"%r8", "%r8d", "%r8w", "%r8b"},
                                        {"%r9", "%r9d", "%r9w", "%r9b"} };
  static final int MAX_F_REGPARAM = 8;
  
  static final String[] STORE_F_REG = { "\n\tmovaps\t%xmm7,-16(%rbp)",
                                        "\n\tmovaps\t%xmm6,-32(%rbp)",
                                        "\n\tmovaps\t%xmm5,-48(%rbp)",
                                        "\n\tmovaps\t%xmm4,-64(%rbp)",
                                        "\n\tmovaps\t%xmm3,-80(%rbp)",
                                        "\n\tmovaps\t%xmm2,-96(%rbp)",
                                        "\n\tmovaps\t%xmm1,-112(%rbp)", 
                                        "\n\tmovaps\t%xmm0,-128(%rbp)",};  
  static final String[] STORE_I_REG = { "\n\tmovq\t%r9,-136(%rbp)",
                                        "\n\tmovq\t%r8,-144(%rbp)",
                                        "\n\tmovq\t%rcx,-152(%rbp)",
                                        "\n\tmovq\t%rdx,-160(%rbp)",
                                        "\n\tmovq\t%rsi,-168(%rbp)",
                                        "\n\tmovq\t%rdi,-176(%rbp)",}; 
  
  static final String[][] RETURN_I_REG = { {"%rax", "%eax"},
                                           {"%rdx", "%edx"} };
         
  static final int NO_CLASS = 0;
  static final int MEMORY = 1;
  static final int INT_CLASS = 2;
  static final int SSE = 3;
  
  int maxStackOffset = 0;
  int numberOfCALLs = 0;
  boolean rbpUsed = true;
  int[] retAggrClass = null; // classes of returning aggregate 
  LirNode retAggrAddr = null; // address of returning aggregate
  
  SymRoot symRoot = null;
  
  SymRoot getSymRoot() {
    if (symRoot != null)
      return symRoot;
    Thread th = Thread.currentThread();
    IoRoot io = null;
    if (th instanceof CompileThread)
      io = ((CompileThread) th).getIoRoot();
    symRoot = io.symRoot;
    return symRoot;
  }
  
  /** count number of parameters in PROLOGUE **/
  LirNode paramCount(LirNode node) {
    int paramFcount = 0;
    int paramIcount = 0;
    int n = node.nKids();
  
    for (int i = 1; i < n; i++) {
      LirNode arg = node.kid(i);
      switch (Type.tag(arg.type)) {
        case Type.INT: paramIcount++; break;
        case Type.FLOAT: paramFcount++; break;
      }
    }
    setParamCount(paramIcount, paramFcount, node.kid(n-1));////
    return null;
  }
  
  /** Rewrite PROLOGUE **/
  LirNode rewritePrologue(LirNode node, BiList post) {
    int location = 16;
    LirNode base = regnode(I64, "%rbp");
    int paramFcount = 0;
    int paramIcount = 0;
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    maxStackOffset = 0;
    numberOfCALLs = 0;
  
    if (func.origEpilogue.nKids() > 1
        && Type.tag(func.origEpilogue.kid(1).type) == Type.AGGREGATE) {
      // First parameter may be a pointer to struct returning value.
      LirNode ret = func.origEpilogue.kid(1);
      Subp subp = (Subp)getSymRoot().sym.getOriginalSym(func.symbol.name);
      coins.sym.Type retType = subp.getReturnValueType();
      retAggrClass = classify(retType);
      if (retAggrClass[0] == MEMORY) {
        retAggrAddr = func.newTemp(I64);
        post.add(lir.node(Op.SET, I64, retAggrAddr,  // store %rdi to retAggrAddr
                          nthIparam(I64, paramIcount++)));
      }
    }
    
    int n = node.nKids();
    for (int i = 1; i < n; i++) {
      LirNode arg = node.kid(i);
      
      if (arg.opCode == Op.MEM) {
        // set location to symbol table
        if (arg.kid(0).opCode != Op.FRAME)
          throw new CantHappenException("Malformed aggregate parameter");
        SymAuto var = (SymAuto)((LirSymRef)arg.kid(0)).symbol;
      } //// else 
        switch (Type.tag(arg.type)) {
        case Type.INT: 
          if (paramIcount < MAX_I_REGPARAM) {
              post.add(lir.node(Op.SET, arg.type, arg,
                          nthIparam(arg.type, paramIcount++)));
          } else {
              post.add(lir.node(Op.SET, arg.type, arg,
                         stackMem(arg.type, location, base)));
              location += 8;
          }
          break;
        case Type.FLOAT:
          if (paramFcount < MAX_F_REGPARAM) {
             post.add(lir.node(Op.SET, arg.type, arg,
                          nthFparam(arg.type, paramFcount++)));
          } else {
              post.add(lir.node(Op.SET, arg.type, arg,
                         stackMem(arg.type, location, base)));
              location += 8;
          }
          break;
        case Type.AGGREGATE:
          int[] aggrClass = classify(hirType(arg));
          int i_reg = 0;
          int f_reg = 0;
          if (aggrClass[0] == INT_CLASS) i_reg++;
          if (aggrClass[1] == INT_CLASS) i_reg++;
          if (aggrClass[0] == SSE) f_reg++;
          if (aggrClass[1] == SSE) f_reg++;
          if (aggrClass[0] == MEMORY || (paramIcount + i_reg > MAX_I_REGPARAM)
                                   || (paramFcount + f_reg > MAX_F_REGPARAM)) {
            LirNode baseLoc = lir.node(Op.ADD, I64, base, 
                                  lir.iconst(I64, location + adjustEndian(arg.type)));
            post.add(lir.node
  	        (Op.SET, arg.type, arg, lir.operator
                 (Op.MEM, arg.type, baseLoc, ImList.list("&align", "8"))));
            location += Type.bytes(arg.type);
          } else {
            LirNode newArg;
            int size;
            for (int j = 0; j < 2; j++) {
              if (aggrClass[j] == INT_CLASS) {
                if (aggrClass[j+2] > 4)
                   size = I64;
                else
                   size = I32;
                if (j == 0)
                   newArg = lir.node(Op.MEM, size, arg.kid(0));
                else
                   newArg = lir.node(Op.MEM, size, 
                              lir.node(Op.ADD, I64, arg.kid(0), lir.iconst(I64, 8)));
                post.add(lir.node
                       (Op.SET, size, newArg, nthIparam(size, paramIcount++)));
              }
              if (aggrClass[j] == SSE) {
                if (aggrClass[j+2] > 4)
                   size = F64;
                else
                   size = F32;
                if (j == 0)
                   newArg = lir.node(Op.MEM, size, arg.kid(0));
                else
                   newArg = lir.node(Op.MEM, size, 
                             lir.node(Op.ADD, I64, arg.kid(0), lir.iconst(I64, 8)));
                post.add(lir.node
                     (Op.SET, size, newArg, nthFparam(size, paramFcount++)));
              } // if
            } // for
          } // else
  
        } // switch
    } // for
    if (location > 16)
      setStackParams(location - 16);
  //  setParamCount(paramIcount, paramFcount); // done by early rewriting
    return lir.node(Op.PROLOGUE, Type.UNKNOWN, node.kid(0));
  }
  
  private LirNode nthIparam(int type, int counter){
    int rank = 3; // rank = I8
    if (counter < MAX_I_REGPARAM) {
       if (type == I32) rank = 1;
       else if (type == I16) rank = 2;
       else if (type == I64) rank = 0;
       return regnode(type, IREGPARAM[counter][rank]);
    }
    return null;
  }
  
  private ImList nthIparamReg(int type, int counter){ /// 2013.7.25
    int rank = 3; // rank = I8
    String typeString = "I8";
    if (counter < MAX_I_REGPARAM) {
       if (type == I32) { rank = 1; typeString = "I32"; }
       else if (type == I16) { rank = 2; typeString = "I16"; }
       else if (type == I64) { rank = 0; typeString = "I64"; }
       return ImList.list("REG", typeString, IREGPARAM[counter][rank]);
    }
    return null;
  }
  
  private LirNode nthIret(int type, int counter) {
    int rank = 0; // rank = I64
    if (type == I32) rank = 1;
    return regnode(type, RETURN_I_REG[counter][rank]);
  }
  
  private LirNode nthFparam(int type, int counter){
    if (counter < MAX_F_REGPARAM) {
       if (type == F64) //;;;
          return regnode(type, "%xmm" + counter);
       return regnode(type, "%xmm" + counter + "s"); //;;;
    }
    return null;
  }
  
  private LirNode regnode(int type, String name) {
    LirNode master = lir.symRef(module.globalSymtab.get(name));
    switch (Type.tag(type)) {
    case Type.INT:
      return master;
  
    case Type.FLOAT:
  //    if (type == F64)   //;;;
        return master;
  //    else if (type == F32)  //;;;
  //      return lir.node  //;;;
  //        (Op.SUBREG, F32, master, lir.untaggedIconst(I32, 0));  //;;;
  
    default:
      return null;
    }
  }
  
  
  private LirNode stackMem(int type, int location, LirNode base) {
    return lir.node
      (Op.MEM, type, lir.node
       (Op.ADD, I64, base,
        lir.iconst(I64, location + adjustEndian(type))));
  }
  
  private int adjustEndian(int type) { return 0; }
  
  
  /** Return the register for value returned. **/
  LirNode returnReg(int type) {
    switch (Type.tag(type)) {
    case Type.INT:
      switch (Type.bytes(type)) {
      case 1: return regnode(type, "%al");
      case 2: return regnode(type, "%ax");
      case 4: return regnode(type, "%eax");
      case 8: return regnode(type, "%rax");
      default:
        return null;
      }
    case Type.FLOAT:
      if (type == F64)
        return regnode(type, "%xmm0");
      return regnode(type, "%xmm0s");
    default:
      return null;
    }
  }
  
  
  /** Rewrite EPILOGUE **/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
    setFuncAttr(numberOfCALLs, maxStackOffset);
    if (node.nKids() < 2)
      return node;
  
    LirNode ret = node.kid(1);
    LirNode reg = null;
    switch (Type.tag(ret.type)) {
    case Type.INT:
    case Type.FLOAT:
      reg = returnReg(ret.type);
      pre.add(lir.node(Op.SET, ret.type, reg, ret));
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
  
    case Type.AGGREGATE:
      if (retAggrClass[0] == MEMORY) {
        pre.add(lir.node
               (Op.SET, ret.type, lir.operator
                 (Op.MEM, ret.type, retAggrAddr,
                 ImList.list("&align", "8")),
  	      ret));
        return lir.node(Op.EPILOGUE, Type.UNKNOWN, new LirNode[]{});
      }
      LirNode newArg;
      int type;
      int iCounter = 0;
      int fCounter = 0;
      for (int j = 0; j < 2; j++) {
         if (retAggrClass[j] == INT_CLASS) {
            if (retAggrClass[j+2] > 4)
               type = I64;
            else
               type = I32;
            if (j == 0)
               newArg = lir.node(Op.MEM, type, ret.kid(0));
            else
               newArg = lir.node(Op.MEM, type, 
                             lir.node(Op.ADD, I64, ret.kid(0), lir.iconst(I64, 8)));
            reg = nthIret(type, iCounter++);
            pre.add(lir.node
                     (Op.SET, type, reg, newArg));
            }
         if (retAggrClass[j] == SSE) {
            if (retAggrClass[j+2] > 4)
               type = F64;
            else
               type = F32;
            if (j == 0)
               newArg = lir.node(Op.MEM, type, ret.kid(0));
            else
               newArg = lir.node(Op.MEM, type, 
                             lir.node(Op.ADD, I64, ret.kid(0), lir.iconst(I64, 8)));
            reg = nthFparam(type, fCounter++);
            pre.add(lir.node
                     (Op.SET, type, reg, newArg));
         }
      } // for
      return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
    default:
      throw new CantHappenException();
    }
  }
  
  
  boolean isVarArgs(LirNode calledFunc){
    if ( !(calledFunc instanceof LirSymRef) )
      return true;
    String name = ((LirSymRef)calledFunc).symbol.name;
    if (name == "printf" || name == "scanf" || name == "sscanf" || name == "sprintf")
       return true;
    Sym symFunc = getSymRoot().sym.getOriginalSym(name);
    if ( !(symFunc instanceof Subp))
      	return true;
    return ((SubpType)symFunc.getSymType()).hasOptionalParam();
  }
  
  coins.sym.Type hirType(LirNode arg) {
    if (!(arg.kid(0) instanceof LirSymRef))
       throw new CantHappenException("unable to get aggregate info (LirSymRef)");
    LirSymRef lval = (LirSymRef)arg.kid(0);
    Symbol symbol = func.localSymtab.get(lval.symbol.name);
    if (symbol == null)
        symbol = func.module.globalSymtab.get(lval.symbol.name);
    ImList symbolOpt = symbol.opt();
    if (symbolOpt == null || symbolOpt == ImList.Empty)
      	throw new CantHappenException("unable to get aggregate info (symbol.opt())");
    String aggrName = ((QuotedString)symbol.opt().elem2nd()).body.intern();
    Sym symAggr = getSymRoot().sym.getOriginalSym(aggrName);
    coins.sym.Type aggrType = symAggr.getSymType();
    return aggrType;
  }
  
  int[] classify(coins.sym.Type aggrType) {
    int[] aggrClass = new int[4];
    if (aggrType.getSizeValue() > 16) {
      aggrClass[0] = MEMORY;
      return aggrClass;
    }
    aggrClass[0] = NO_CLASS;
    aggrClass[1] = NO_CLASS;
    aggrClass[2] = 0; // real size of 1st 8-byte
    aggrClass[3] = 0; // real size of 2nd 8-byte
    int[] index_disp = new int[2];
    index_disp[0] = 0; //index
    index_disp[1] = 0; //disp of first elem in index-th 8-byte 
    IrList elems = null;
    if ((aggrType instanceof StructType) || (aggrType instanceof UnionType)){
       if (aggrType instanceof StructType)
         elems = ((StructType)aggrType).getElemList();
       else
         elems = ((UnionType)aggrType).getElemList();
       java.util.ListIterator it = elems.iterator();
       while (it.hasNext()){
          Elem nextSym = (Elem)it.next();
          int nextSize = (int)nextSym.getSize();
          int nextDisp = (int)nextSym.evaluateDisp();
          if (index_disp[0]*8 + nextDisp + nextSize - index_disp[1] > 16) {
             aggrClass[0] = MEMORY;
             return aggrClass;
          }
          coins.sym.Type nextType = nextSym.getSymType();
          if (nextType.isScalar()) { 
             if (isInRegister(aggrClass, index_disp, nextSize, nextDisp, nextType))
                continue;  // aggrClass and index_disp may be modified
             else
                return aggrClass;  // aggrClass[0] must be MEMORY
          } else {
             IrList subElems = null;
             if ((nextType instanceof StructType) || (nextType instanceof UnionType)){
                if (nextType instanceof StructType)
                   subElems = ((StructType)nextType).getElemList();
                else
                   subElems = ((UnionType)nextType).getElemList();
                java.util.ListIterator subIt = subElems.iterator();
                while (subIt.hasNext()){
                   nextSym = (Elem)subIt.next();
                   nextSize = (int)nextSym.getSize();
                   int nextDisp2 = nextDisp + (int)nextSym.evaluateDisp();
                   nextType = nextSym.getSymType();
                   if (nextType.isScalar()) { 
                     if (isInRegister(aggrClass, index_disp, nextSize, nextDisp2, nextType))
                        continue;  // aggrClass and index_disp may be modified
                     else
                        return aggrClass;  // aggrClass[0] must be MEMORY
                   } else {
                     System.out.println("unsupported complex aggregate");
                     aggrClass[0] = MEMORY; return aggrClass;
                   }
                }
             } else if (nextType instanceof VectorType){
                int count = (int)((VectorType)nextType).getElemCount();
                nextType = ((VectorType)nextType).getElemType();
                if (! nextType.isScalar()) {
                   System.out.println("unsupported complex aggregate");
                   aggrClass[0] = MEMORY; return aggrClass;
                }
                nextSize = (int)nextType.getSizeValue();
                for (int i = 0; i < count; i++){
                   if (isInRegister(aggrClass, index_disp, nextSize, 
                                    nextDisp + i*nextSize, nextType))
                      continue;  // aggrClass and index_disp may be modified
                   else
                      return aggrClass;  // aggrClass[0] must be MEMORY
                }
             }
          }
       }
       return aggrClass;
    } 
    aggrClass[0] = MEMORY; return aggrClass;
  }
  
  boolean isInRegister(int[] aggrClass, int[] index_disp, 
                       int nextSize, int nextDisp, coins.sym.Type nextType) {
    if (!nextType.isBasicType() && nextType.isScalar())
        nextType = nextType.getFinalOrigin();
    int i = index_disp[0];
    int originalDisp = index_disp[1];
    int nextClass;
    if (nextType.isInteger()) {
      nextClass = INT_CLASS;
    } else {
      nextClass = SSE;
    }
    if (nextDisp + nextSize - originalDisp > 8) {
      if (i > 0) {
        aggrClass[0] = MEMORY;
        return false;
      } else {
        index_disp[0] = 1;
        aggrClass[1] = nextClass;
        index_disp[1] = nextDisp;
        aggrClass[3] = nextSize;
        return true;
      }
    } else {
      if (nextClass == INT_CLASS)
         aggrClass[i] = INT_CLASS;
      else // nextClass == SSE
        if (aggrClass[i] != INT_CLASS)
           aggrClass[i] = SSE;
      aggrClass[i+2] = nextDisp + nextSize - originalDisp; // size
      return true;
    }
    
  }
  
  /** Rewrite CALL node. **/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
    BiList listPreI = new BiList(); // for evaluation of int register parameter expressions
    BiList listI = new BiList(); // for passing int register parameters
    BiList listF = new BiList();  // for float register parameters
    BiList listM = new BiList();  // for stacked parameters
    ImList regCallUses = ImList.list();
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    
    LirNode calledFunc = node.kid(0);
    if (calledFunc instanceof LirSymRef) {
       String calledFuncName = ((LirSymRef)calledFunc).symbol.name;
       if ( calledFuncName == func.symbol.name  && frameIsEmpty(func))
          attr.isRecursive = true;
       if ( convention == "mac" && (calledFuncName == "fprintf" || calledFuncName == "fscanf") )
          throw new CantHappenException("fprintf and fscanf are unsupported in x86_64-mac");
    }
    LirNode args = node.kid(1);
    LirNode ret = null;
    if (node.kid(2).nKids() > 0)
      ret = node.kid(2).kid(0);
    int n = args.nKids();
    LirNode spreg = regnode(I64, "%rsp");
  
    int stackOffset = 0;
    int paramICounter = 0;
    int paramFCounter = 0;
  
    int[] aggrClass2 = null; //for returning aggregate
    LirNode retReg = null; ///ret;
    if (ret != null) {
      switch (Type.tag(ret.type)) {
      case Type.INT:
      case Type.FLOAT:
        retReg = returnReg(ret.type);
        break;
      case Type.AGGREGATE:
        if (ret.opCode != Op.MEM)
          throw new CantHappenException();
        if ( !(calledFunc instanceof LirSymRef) )
          throw new CantHappenException("unable to get called function's name");
        Subp subp = (Subp)getSymRoot().sym.getOriginalSym(((LirSymRef)calledFunc).symbol.name);
        coins.sym.Type retType = subp.getReturnValueType();
        aggrClass2 = classify(retType);
        if (aggrClass2[0] == MEMORY) {
          listI.add(lir.node
                  (Op.SET, I64, nthIparam(I64, paramICounter++), ret.kid(0)));
          retReg = returnReg(I64);
        }
        break;
      }
    }
  
    for (int i = 0; i < n; i++) {
      LirNode arg = args.kid(i);
      // for convention "mac" and STATIC and "XREF"
      if (conventionIsMac()) {
        if (arg.opCode == Op.STATIC && isX(arg)) {
          if (paramICounter < MAX_I_REGPARAM){
            ImList regName = nthIparamReg(arg.type, paramICounter);  /// 2013.7.25
            LirNode temp1 = func.newTemp(arg.type);
            listPreI.add(lir.node
                  (Op.SET, arg.type, temp1, lir.node
                    (Op.MEM, arg.type, arg)));
            listI.add(lir.node
                (Op.SET, arg.type, nthIparam(arg.type, paramICounter++), temp1));
            if (regCallUses.isEmpty())                /// 2013.7.25
                regCallUses = new ImList(regName);
            else
                regCallUses = regCallUses.append( new ImList(regName) );
          } else {
            listM.add(lir.node
                  (Op.SET, arg.type, lir.node
                   (Op.MEM, arg.type, lir.node
                    (Op.ADD, I64, spreg, lir.iconst(I64, stackOffset))),
                   arg));
            stackOffset += 8;
          }
          continue;
        }
      }
      switch (Type.tag(arg.type)) {
      case Type.INT:
        if (Type.bits(arg.type) < 32)
          arg = lir.node(Op.CONVZX, I32, arg);
        if (paramICounter < MAX_I_REGPARAM) {
          ImList regName = nthIparamReg(arg.type, paramICounter);  /// 2013.7.25
          LirNode temp = func.newTemp(arg.type);
          listPreI.add(lir.node
                  (Op.SET, arg.type, temp, arg));
          listI.add(lir.node
                  (Op.SET, arg.type, nthIparam(arg.type, paramICounter++), temp));
          if (regCallUses.isEmpty())              /// 2013.7.25
              regCallUses = new ImList(regName);
          else
              regCallUses = regCallUses.append( new ImList(regName) );
        } else {
          listM.add(lir.node
                  (Op.SET, arg.type, lir.node
                   (Op.MEM, arg.type, lir.node
                    (Op.ADD, I64, spreg, lir.iconst(I64, stackOffset))),
                   arg));
          stackOffset += 8;
        }
        break;
      case Type.FLOAT:
        if (paramFCounter < MAX_F_REGPARAM) {
          if (regCallUses.isEmpty())
              regCallUses = new ImList(ImList.list("REG", "F64", "%xmm" + paramFCounter));
          else
              regCallUses = regCallUses.append(
                             new ImList(ImList.list("REG", "F64", "%xmm" + paramFCounter)));
          listF.add(lir.node
                 (Op.SET, arg.type, nthFparam(arg.type, paramFCounter++), arg));
        } else {
          listM.add(lir.node
                  (Op.SET, arg.type, lir.node
                   (Op.MEM, arg.type, lir.node
                    (Op.ADD, I64, spreg, lir.iconst(I64, stackOffset))),
                   arg));
          stackOffset += 8;
        }
        break;
      case Type.AGGREGATE: 
        int[] aggrClass = classify(hirType(arg));
        int i_reg = 0;
        int f_reg = 0;
        if (aggrClass[0] == INT_CLASS) i_reg++;
        if (aggrClass[1] == INT_CLASS) i_reg++;
        if (aggrClass[0] == SSE) f_reg++;
        if (aggrClass[1] == SSE) f_reg++;
        if (aggrClass[0] == MEMORY || (paramICounter + i_reg > MAX_I_REGPARAM)
                                   || (paramFCounter + f_reg > MAX_F_REGPARAM)) {
          listM.add(lir.node
  	       (Op.SET, arg.type, lir.operator
                 (Op.MEM, arg.type, spreg, ImList.list("&align", "8")), arg));
          stackOffset += Type.bytes(arg.type);
        } else { 
          LirNode newArg;
          int size;
          for (int j = 0; j < 2; j++) {
            if (aggrClass[j] == INT_CLASS) {
              if (aggrClass[j+2] > 4)
                 size = I64;
              else
                 size = I32;
              if (j == 0)
                 newArg = lir.node(Op.MEM, size, arg.kid(0));
              else
                 newArg = lir.node(Op.MEM, size, 
                             lir.node(Op.ADD, I64, arg.kid(0).makeCopy(lir), lir.iconst(I64, 8)));
              ImList regName = nthIparamReg(size, paramICounter);   /// 2013.7.25
              listI.add(lir.node
                     (Op.SET, size, nthIparam(size, paramICounter++), newArg));
              if (regCallUses.isEmpty())      /// 2013.7.25
                 regCallUses = new ImList(regName);
              else
                 regCallUses = regCallUses.append( new ImList(regName) );
            }
            if (aggrClass[j] == SSE) {
              if (aggrClass[j+2] > 4)
                 size = F64;
              else
                 size = F32;
              if (j == 0)
                 newArg = lir.node(Op.MEM, size, arg.kid(0));
              else
                 newArg = lir.node(Op.MEM, size, 
                             lir.node(Op.ADD, I64, arg.kid(0).makeCopy(lir), lir.iconst(I64, 8)));
              if (regCallUses.isEmpty())
                regCallUses = new ImList(ImList.list("REG", "F64", "%xmm" + paramFCounter));
              else
                regCallUses = regCallUses.append(
                             new ImList(ImList.list("REG", "F64", "%xmm" + paramFCounter)));
              listF.add(lir.node
                     (Op.SET, size, nthFparam(size, paramFCounter++), newArg));
            }
          }
        }
        break;
      }
    } // end for
  
    if (attr.allocaCalled && (stackOffset != 0)) {
       int decr = (stackOffset + 15) & -16;
       pre.add(lir.node
                 (Op.SET, I64, spreg, lir.node
                  (Op.SUB, I64, spreg, lir.iconst(I64, decr))));
    }
  
    pre.concatenate(listM);
    pre.concatenate(listF);
    pre.concatenate(listPreI);
    pre.concatenate(listI);
  
    if (maxStackOffset < stackOffset && !attr.allocaCalled)
       maxStackOffset = stackOffset;
    numberOfCALLs++;
  
      if (isVarArgs(node.kid(0))) {
          int noOfFloatReg = Math.min(MAX_F_REGPARAM, paramFCounter);
          pre.add(lir.node
                 (Op.SET, I32, regnode(I32, "%eax"), lir.iconst(I32, noOfFloatReg)));
          if (regCallUses.isEmpty())        /// 2013.7.25
              regCallUses = new ImList(ImList.list("REG", "I32", "%eax"));
          else
              regCallUses = regCallUses.append(
                             new ImList(ImList.list("REG", "I32", "%eax")));
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
         lir.decodeLir(new ImList("USE", regCallUses), func, module),
         lir.decodeLir(new ImList("CLOBBER", regCallClobbers), func, module) );
    } catch (SyntaxError e) {
      throw new CantHappenException();
    }
  
    if (attr.allocaCalled && (stackOffset != 0)) {
       int incr = (stackOffset + 15) & -16;
       post.add(lir.node
                 (Op.SET, I64, spreg, lir.node
                  (Op.ADD, I64, spreg, lir.iconst(I64, incr))));
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
        if (aggrClass2[0] == MEMORY) // no action needed
          break;
        LirNode newAggr;
        int type;
        int iCounter = 0;
        int fCounter = 0;
        for (int k = 0; k < 2; k++) {
            if (aggrClass2[k] == INT_CLASS) {
              if (aggrClass2[k+2] > 4)
                 type = I64;
              else
                 type = I32;
              if (k == 0)
                 newAggr = lir.node(Op.MEM, type, ret.kid(0));
              else
                 newAggr = lir.node(Op.MEM, type, 
                             lir.node(Op.ADD, I64, ret.kid(0).makeCopy(lir), lir.iconst(I64, 8)));
              post.add(lir.node
                     (Op.SET, type, newAggr, nthIret(type, iCounter++)));
            }
            if (aggrClass2[k] == SSE) {
              if (aggrClass2[k+2] > 4)
                 type = F64;
              else
                 type = F32;
              if (k == 0)
                 newAggr = lir.node(Op.MEM, type, ret.kid(0));
              else
                 newAggr = lir.node(Op.MEM, type, 
                             lir.node(Op.ADD, I64, ret.kid(0).makeCopy(lir), lir.iconst(I64, 8)));
              post.add(lir.node
                     (Op.SET, type, newAggr, nthFparam(type, fCounter++)));
            }
          }
  
      }
    }
    return node;
  }
  
  
  /** Postprocess list-form assembler source.
   ** @param list assembler source in list form. **/
  void peepHoleOpt(BiList list) {
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
        return "l";    ///// "d" ???
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
    if (dtype == I32) {
      if (offset == 0) {
        if ( Character.isDigit(reg.name.charAt(2)) )
           return reg.name.substring(0, reg.name.length() - 1) + "d"; 
        return "%r" + reg.name.substring(2);
      }
    } else if (dtype == I16) {
      if (offset == 0) {
        if ( Character.isDigit(reg.name.charAt(2)) )
           return reg.name.substring(0, reg.name.length() - 1) + "w"; 
        return "%" + reg.name.substring(2);
      }
    } else if (dtype == I8) {
      int namel = reg.name.length();
      if (offset == 0) {
        if ( Character.isDigit(reg.name.charAt(2)) )
          return reg.name.substring(0, reg.name.length() - 1) + "b";
        return "%" + reg.name.substring(namel - 2, namel - 1) + "l";
      }
      else if (offset == 1)
        return "%" + reg.name.substring(namel - 2, namel - 1) + "h";
    } else if (dtype == F32 || dtype == F64) {
      if (offset == 0)
        return reg.name;
    }
    throw new CantHappenException();
  }
  
  
  /* Code emission macros.
   *  Patterns not defined below will be converted to:
   *   (foo bar baz) --> foo   bar,baz   or foo(bar,baz)
   */
  
  String jmac2(String x, String y) {
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
  
  String jmac3(String x, String y) {
    if (y.charAt(0) == '-')
      return x + y;
    else
      return x + "+" + y;
  }
  
  String jmac4(String x, String y) {
    if (y.charAt(0) == '-')
      return x + "+" + y.substring(1);
    else
      return x + "-" + y;
  }
  
  String jmac5(String x) {
    return "" + (Integer.parseInt(x) - 32);
  }
  
  String jmac6(String x) { return "$" + x; }
  
  String jmac7(String x) { return "*" + x; }
  
  String jmac8(String type, String x) { return x; }
  
  String jmac9(String base) {
    return base + "(%rip)";
  }
  
  String jmac10(String base) {
    return base + "@GOTPCREL(%rip)";
  }
  
  String jmac11(String base, String index) {
    if (index == "") {
      if ( (base.charAt(base.length() - 1) != ')') && 
           ( Character.isLetter(base.charAt(0)) || base.charAt(0) == '_') )
        return base + "(%rip)";
      return base;
    }
    else if (base == "" || base.charAt(base.length() - 1) != ')')
      return base + "(," + index + ")";
    else
      return base.substring(0, base.length() - 1) + "," + index + ")";
  }
  
  String jmac12(String con, String reg) {
    if (reg == "")
      return con;
    else
      return con + "(" + reg + ")";
  }
  
  String jmac13(String reg, String scale) {
    if (scale == "1")
      return reg;
    else
      return reg + "," + scale;
  }
  
  /** Return lower half register name. **/
  String jmac14(String x) {
     if (Character.isDigit(x.charAt(2)))
        return x.substring(0, x.length() - 1) + "w";
     return "%" + x.substring(2); 
  }
  
  /** Return lowest byte register name. **/
  String jmac15(String x) {
     if (Character.isDigit(x.charAt(2)))
        return x.substring(0, x.length() - 1) + "b";
     if (x.charAt(x.length() - 1) == 'i')
        return "%" + x.substring(x.length() - 2, x.length()) + "l";
     return "%" + x.substring(x.length() - 2, x.length() - 1) + "l";
  }
  
  
  /** Return lower 32bit of memory/register/constant operand. **/
  String jmac16(String x) {
    if (x.charAt(0) == '$')
      return "$" + (Long.parseLong(x.substring(1)) & 0xffffffffL);
    else if (x.charAt(0) == '%'){
      if (Character.isDigit(x.charAt(2)))
        return x + "d";
      return "%e" + x.substring(2);
    }
    else
      return x;
  }
  
  String jmac17(String x) {
    if (x.charAt(0) == '%'){
      if (Character.isDigit(x.charAt(2)))
        return x.substring(0, x.length() - 1);
      return "%r" + x.substring(2);
    }
    else
      return x;
  }
  
  String jmac18(String x) {
    int i = x.indexOf(',');
    String tail;
    if (i < 0) {
       i = x.length();
       tail ="";
    } else
       tail = x.substring(i, x.length());
    if (Character.isDigit(x.charAt(2))) {
       return x.substring(0, i-1) + tail;
    }
    return "%r" + x.substring(2, i) + tail;
  }
  
  String jmac19(String x) {
    int i = x.indexOf(')');
    if (i < 0)
       return x;
    if (Character.isDigit(x.charAt(i-2))) {
      return x.substring(0, i - 1) + ")";
    } 
    return x.substring(0, i - 3) + "r" + x.substring(i-2, i+1);
  }
  
  /** Return upper 32bit of memory/register/constant operand. **/
  String jmac20(String x) {
    if (x.charAt(0) == '$')
      return "$" + ((Long.parseLong(x.substring(1)) >> 32) & 0xffffffffL);
    else if (x.charAt(0) == '%')
      return x.substring(0, x.length() - 3);
    else
      return emitAfter(x, "4");
  }
  
  /** Return expanded 32bit register name.(w to l) **/
  String jmac21(String x) {
    if (Character.isDigit(x.charAt(2)))
       return x.substring(0, x.length() - 1) + "d";
    return "%e" + x.substring(1);
  }
  
  /** Return expanded 32bit register name.(b to l) **/
  String jmac22(String x) {
    if (Character.isDigit(x.charAt(2)))
       return x.substring(0, x.length() - 1) + "d";
    if (x.charAt(x.length() - 2) == 'i')
       return "%e" + x.substring(1,3);
    return "%e" + x.substring(1,2) + "x";
  }
  
  /** Return expanded 64bit register name.(l to q) **/
  String jmac23(String x) {
    if (Character.isDigit(x.charAt(2)))
       return x.substring(0, x.length() - 1);
    return "%r" + x.substring(2,4);
  }
  
  /** Return full register name. **/
  String jmac24(String x){
    if ((x.charAt(0) == '%') && (x.charAt(x.length() - 1) == 's'))
      return x.substring(0, x.length() - 1);
    return x;
  }
  
  /** Generate prologue sequence. **/
  String jmac25(Object f) {
    Function func = (Function)f;
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    int frameSize = frameSize(func); 
    String seq = "";
    boolean rbpUsed = (frameSize != 0) || (attr.stackParams != 0) || attr.allocaCalled; 
    attr.rbpUsed = rbpUsed;
  
    boolean odd = false; // number of callee-save registers is odd or not
    String push = "";
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      push += "\n\tpushq\t" + machineParams.registerToString(reg);
      odd = !odd;
    }
  
    String varArgs = "";
    if (attr.varArgFunction) {
      for (int i = 0; i < 6 - attr.paramIcount; i++)
        varArgs += STORE_I_REG[i];
      for (int i = 0; i < 8 - attr.paramFcount; i++)
        varArgs += STORE_F_REG[i];
    }
  
    if (rbpUsed) {
      seq = "\tpushq\t%rbp\n" + "\tmovq\t%rsp,%rbp";
  
      if (convention == "cygwin" && frameSize > 4000) { // round up frameSize ?
        seq += "\n\tmovq\t$" + frameSize + ",%rax" +
               "\n\tcall\t__alloca";
      } else {
        if ((attr.stackRequired != 0) && (push != "")) { // !attr.allocaCalled
          int fSize = (frameSize + 15) & -16; // round up to 16byte boundary
          if (odd)
             fSize += 8;
          if (fSize == 0)
            seq += push;
          else
            seq += "\n\tsubq\t$" + fSize + ",%rsp" + varArgs + push;
          int cSize = (attr.stackRequired +15) & -16;
          seq += "\n\tsubq\t$" + cSize + ",%rsp";
        } else { // (attr.stackRequired == 0) || (push == "") 
          int fcSize = (frameSize + attr.stackRequired + 15) & -16;
          if (odd)
             fcSize += 8;
          if (fcSize == 0)
             seq += push;
          else
             seq += "\n\tsubq\t$" + fcSize + ",%rsp" + varArgs + push;
        }
      }
    } else { // !rbpUsed == (frameSize == 0) && (attr.stackParams == 0) && !attr.allocaCalled
      seq = push;
      if (attr.numberOfCALLs != 0) {
        int m = (attr.stackRequired + 15) & -16;
        if (!odd)
          m += 8;
        if (m != 0)
          seq += "\n\tsubq\t$" + m + ",%rsp";
      }
    }
  
    return seq;
  }
  
  /** Generate epilogue sequence. **/
  String jmac26(Object f, String rettype) {
    Function func = (Function)f;
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    int frameSize = frameSize(func);
    int size = (frameSize + 15) & -16; // round up to 16byte boundary
    X86_64Attr attr = (X86_64Attr)getFunctionAttr(func);
    String pops = "";
    int n = 0;
    boolean odd = false; // number of callee-save registers is odd or not
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
      int reg = it.next();
      pops = "\tpopq\t" + machineParams.registerToString(reg) + "\n" + pops;
      n += 8;
      odd = !odd;
    }
    String seq = "";
  //  if (attr.allocaCalled && n != 0)
    if (attr.rbpUsed){ // (frameSize != 0) || (attr.stackParams != 0) || attr.allocaCalled
      if ((attr.stackRequired != 0) && (pops != "")) {
        int fSize = (frameSize + 15) & -16; // round up to 16byte boundary
        if (odd)
             fSize += 8; 
        seq = "\tleaq\t-" + (fSize + n) + "(%rbp),%rsp\n";
      } else {  // (attr.stackRequired == 0) || (pops == "")
        if (pops != "") {
          int fcSize = (frameSize + attr.stackRequired + 15) & -16;
          if (odd)
             fcSize += 8;
          seq = "\tleaq\t-" + (fcSize + n) + "(%rbp),%rsp\n";
        }
      }
    } else { // !rbpUsed, i.e. (frameSize == 0) 
      if (attr.numberOfCALLs != 0) {
        int m = (attr.stackRequired + 15) & -16;
        if (!odd)
          m += 8;
        if (m != 0)
          seq = "\taddq\t$" + m + ",%rsp\n";
      }
    }
  //  return seq + pops + "\tpopq\t%rbp\n\tret";
    if (attr.rbpUsed)
       return seq + pops + "\tleave\n\tret";
    return seq + pops + "\tret";
  }
  
  String jmac27(String con) {
    return -Integer.parseInt(con) + "";
  }
  
  
  String jmac28(String x) { return "# line " + x; }
  
  String jmac29(String x) { return makeAsmSymbol(x); }
  
  
  String jmac30(String format, Object args) { return emitAsmCode(format, (ImList)args); }
  
  
  /** Emit beginning of segment **/
  void emitBeginningOfSegment(PrintWriter out, String segment) {
    if (convention == "mac") {
      if (segment.equals(".text") || segment.equals(".rodata"))
        out.println("\t.text");
      else if (segment.equals(".data"))
        out.println("\t.data");
      else
        out.println("\t.section " + segment);
    } else
      out.println("\t.section " + segment);
  }
  
  void emitLinkage(PrintWriter out, SymStatic symbol) {
    if (symbol.linkage == "XDEF"){
        out.println("\t.globl\t" + makeAsmSymbol(symbol.name));
    }
  }
  
  /** Convert symbol to assembler form.
   **  Prepend "_" when mac or cygwin (COFF), untouched otherwise (ELF). **/
  String makeAsmSymbol(String symbol) {
    if (convention == "mac" && symbol.charAt(0) != '.')
      return "_" + symbol;
    else
      return symbol;
  }
  
  public int alignForType(int type) {
    switch (Type.bytes(type)) {
    case 1: return 1;
    case 2: return 2;
    case 4: return 4;
    default: return 8;
    }
  }
  
  String segmentForConst() { return ".rodata"; }
  
  /** Emit data **/
  void emitData(PrintWriter out, int type, LirNode node) {
    if (type == I64) {
      if (node instanceof LirIconst){
        long v = ((LirIconst)node).signedValue();
        out.println("\t.long\t" + (v & 0xffffffffL)
                    + "," + ((v >> 32) & 0xffffffffL));
      } else
          out.println("\t.quad\t" + lexpConv.convert(node));
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
      if (convention == "mac")
        out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes);
      else
        out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
                    + ","  + bytes + "," + symbol.boundary);
    }
  }
  
    /** Emit data zeros **/
    void emitZeros(PrintWriter out, int bytes) {
      if (bytes > 0)
        out.println("\t.space\t" + bytes); /* "\t.skip\t" in CodeGenerator */
    }
}
