/*
Productions:
 1: _xregb -> (REG I8)
 2: _xregb -> (SUBREG I8)
 3: _xregh -> (REG I16)
 4: _xregh -> (SUBREG I16)
 5: _xregl -> (REG I32)
 6: _xregl -> (SUBREG I32)
 7: _xregf -> (REG F32)
 8: _xregf -> (SUBREG F32)
 9: _xregd -> (REG F64)
 10: _xregd -> (SUBREG F64)
 11: xregb -> _xregb
 12: xregh -> _xregh
 13: xregl -> _xregl
 14: xregf -> _xregf
 15: xregd -> _xregd
 16: regb -> xregb
 17: regh -> xregh
 18: regl -> xregl
 19: regf -> xregf
 20: regd -> xregd
 21: con5 -> (INTCONST _)
 22: con16u -> (INTCONST _)
 23: con16sminus -> (INTCONST _)
 24: con32u -> (INTCONST _)
 25: con32s -> (INTCONST _)
 26: con32 -> con32u
 27: con32 -> con32s
 28: conofs -> (LIST _ con32)
 29: confd -> (FLOATCONST _)
 30: _sta -> (STATIC _)
 31: sta -> _sta
 32: addr -> regl
 33: addr -> con32
 34: addr -> (ADD I32 regl con32)
 35: addr -> (ADD I32 regl conofs)
 36: asmcon -> sta
 37: asmcon -> con32
 38: asmcon -> (ADD I32 asmcon con32)
 39: asmcon -> (SUB I32 asmcon con32)
 40: fun -> asmcon
 41: fun -> addr
 42: rc -> regl
 43: rc -> con32
 44: rc -> conofs
 45: rcs -> regl
 46: rcs -> con5
 47: regl -> con16u
 48: regh -> con16u
 49: regb -> con16u
 50: regl -> con16sminus
 51: regh -> con16sminus
 52: regb -> con16sminus
 53: regl -> con32
 54: regh -> con32
 55: regb -> con32
 56: regf -> confd
 57: regd -> confd
 58: regl -> sta
 59: regh -> sta
 60: void -> (SET I32 xregl regl)
 61: void -> (SET I16 xregh regh)
 62: void -> (SET I8 xregb regb)
 63: _1 -> (INTCONST _ 0)
 64: _2 -> (SUBREG F32 xregl _1)
 65: void -> (SET F32 _2 regf)
 66: _3 -> (SUBREG F32 regl _1)
 67: void -> (SET F32 xregf _3)
 68: void -> (SET F64 xregd regd)
 69: void -> (SET F32 xregf regf)
 70: void -> (SET I32 xregl confd)
 71: void -> (SET I32 xregl regd)
 72: void -> (SET I32 xregl regd)
 73: void -> (SET F64 xregd regl)
 74: void -> (SET F64 xregd regl)
 75: _4 -> (MEM I32 addr)
 76: void -> (LIST _ regl _4)
 77: _5 -> (MEM I16 addr)
 78: void -> (LIST _ regh _5)
 79: _6 -> (MEM I8 addr)
 80: void -> (LIST _ regb _6)
 81: _7 -> (MEM F64 addr)
 82: void -> (LIST _ regd _7)
 83: _8 -> (MEM F32 addr)
 84: void -> (LIST _ regf _8)
 85: void -> (LIST _ regl _4)
 86: void -> (LIST _ regh _5)
 87: void -> (LIST _ regb _6)
 88: void -> (LIST _ regd _7)
 89: void -> (LIST _ regf _8)
 90: regl -> (MEM I32 addr)
 91: regh -> (MEM I16 addr)
 92: regb -> (MEM I8 addr)
 93: regd -> (MEM F64 addr)
 94: regf -> (MEM F32 addr)
 95: regl -> (MEM I32 addr)
 96: regh -> (MEM I16 addr)
 97: regb -> (MEM I8 addr)
 98: regd -> (MEM F64 addr)
 99: regf -> (MEM F32 addr)
 100: regl -> (CONVSX I32 _5)
 101: regl -> (CONVSX I32 _6)
 102: regl -> (CONVSX I32 _5)
 103: regl -> (CONVSX I32 _6)
 104: regl -> (CONVZX I32 _5)
 105: regl -> (CONVZX I32 _6)
 106: regl -> (CONVZX I32 _5)
 107: regl -> (CONVZX I32 _6)
 108: void -> (LIST _ regl _4)
 109: void -> (LIST _ regh _5)
 110: void -> (LIST _ regb _6)
 111: void -> (LIST _ regd _7)
 112: void -> (LIST _ regf _8)
 113: regl -> (MEM I32 addr)
 114: regh -> (MEM I16 addr)
 115: regb -> (MEM I8 addr)
 116: regd -> (MEM F64 addr)
 117: regf -> (MEM F32 addr)
 118: regl -> (CONVSX I32 _5)
 119: regl -> (CONVSX I32 _6)
 120: regl -> (CONVZX I32 _5)
 121: regl -> (CONVZX I32 _6)
 122: void -> (SET I32 _4 regl)
 123: void -> (SET I16 _5 regh)
 124: void -> (SET I8 _6 regb)
 125: void -> (SET F64 _7 regd)
 126: void -> (SET F32 _8 regf)
 127: _9 -> (CONVIT I16 regl)
 128: void -> (SET I16 _5 _9)
 129: _10 -> (CONVIT I8 regl)
 130: void -> (SET I8 _6 _10)
 131: label -> (LABEL _)
 132: void -> (JUMP _ label)
 133: void -> (JUMP _ label)
 134: _11 -> (TSTEQ I32 regl rc)
 135: void -> (JUMPC _ _11 label label)
 136: _12 -> (TSTNE I32 regl rc)
 137: void -> (JUMPC _ _12 label label)
 138: _13 -> (TSTLTS I32 regl rc)
 139: void -> (JUMPC _ _13 label label)
 140: _14 -> (TSTLES I32 regl rc)
 141: void -> (JUMPC _ _14 label label)
 142: _15 -> (TSTGTS I32 regl rc)
 143: void -> (JUMPC _ _15 label label)
 144: _16 -> (TSTGES I32 regl rc)
 145: void -> (JUMPC _ _16 label label)
 146: _17 -> (TSTLTU I32 regl rc)
 147: void -> (JUMPC _ _17 label label)
 148: _18 -> (TSTLEU I32 regl rc)
 149: void -> (JUMPC _ _18 label label)
 150: _19 -> (TSTGTU I32 regl rc)
 151: void -> (JUMPC _ _19 label label)
 152: _20 -> (TSTGEU I32 regl rc)
 153: void -> (JUMPC _ _20 label label)
 154: void -> (JUMPC _ _11 label label)
 155: void -> (JUMPC _ _12 label label)
 156: void -> (JUMPC _ _13 label label)
 157: void -> (JUMPC _ _14 label label)
 158: void -> (JUMPC _ _15 label label)
 159: void -> (JUMPC _ _16 label label)
 160: void -> (JUMPC _ _17 label label)
 161: void -> (JUMPC _ _18 label label)
 162: void -> (JUMPC _ _19 label label)
 163: void -> (JUMPC _ _20 label label)
 164: _21 -> (TSTEQ I32 regd regd)
 165: void -> (JUMPC _ _21 label label)
 166: _22 -> (TSTNE I32 regd regd)
 167: void -> (JUMPC _ _22 label label)
 168: _23 -> (TSTLTS I32 regd regd)
 169: void -> (JUMPC _ _23 label label)
 170: _24 -> (TSTLES I32 regd regd)
 171: void -> (JUMPC _ _24 label label)
 172: _25 -> (TSTEQ I32 regf regf)
 173: void -> (JUMPC _ _25 label label)
 174: _26 -> (TSTNE I32 regf regf)
 175: void -> (JUMPC _ _26 label label)
 176: _27 -> (TSTLTS I32 regf regf)
 177: void -> (JUMPC _ _27 label label)
 178: _28 -> (TSTLES I32 regf regf)
 179: void -> (JUMPC _ _28 label label)
 180: void -> (JUMPC _ _21 label label)
 181: void -> (JUMPC _ _22 label label)
 182: void -> (JUMPC _ _23 label label)
 183: void -> (JUMPC _ _24 label label)
 184: void -> (JUMPC _ _25 label label)
 185: void -> (JUMPC _ _26 label label)
 186: void -> (JUMPC _ _27 label label)
 187: void -> (JUMPC _ _28 label label)
 188: _29 -> (TSTGTS I32 regd regd)
 189: void -> (JUMPC _ _29 label label)
 190: _30 -> (TSTGES I32 regd regd)
 191: void -> (JUMPC _ _30 label label)
 192: _31 -> (TSTGTS I32 regf regf)
 193: void -> (JUMPC _ _31 label label)
 194: _32 -> (TSTGES I32 regf regf)
 195: void -> (JUMPC _ _32 label label)
 196: void -> (JUMPC _ _29 label label)
 197: void -> (JUMPC _ _30 label label)
 198: void -> (JUMPC _ _31 label label)
 199: void -> (JUMPC _ _32 label label)
 200: void -> (JUMP _ label)
 201: void -> (JUMPC _ _11 label label)
 202: void -> (JUMPC _ _12 label label)
 203: void -> (JUMPC _ _17 label label)
 204: void -> (JUMPC _ _13 label label)
 205: void -> (JUMPC _ _14 label label)
 206: void -> (JUMPC _ _15 label label)
 207: void -> (JUMPC _ _16 label label)
 208: void -> (JUMPC _ _20 label label)
 209: _33 -> (TSTGTU I32 regl regl)
 210: void -> (JUMPC _ _33 label label)
 211: _34 -> (TSTLEU I32 regl regl)
 212: void -> (JUMPC _ _34 label label)
 213: void -> (JUMPC _ _21 label label)
 214: void -> (JUMPC _ _23 label label)
 215: void -> (JUMPC _ _24 label label)
 216: void -> (JUMPC _ _25 label label)
 217: void -> (JUMPC _ _27 label label)
 218: void -> (JUMPC _ _28 label label)
 219: void -> (JUMPC _ _22 label label)
 220: void -> (JUMPC _ _26 label label)
 221: void -> (JUMPC _ _29 label label)
 222: void -> (JUMPC _ _30 label label)
 223: void -> (JUMPC _ _31 label label)
 224: void -> (JUMPC _ _32 label label)
 225: regl -> (BAND I32 regl rc)
 226: regl -> (BOR I32 regl rc)
 227: regl -> (BXOR I32 regl rc)
 228: regl -> (ADD I32 regl rc)
 229: regl -> (SUB I32 regl rc)
 230: regl -> (MUL I32 regl rc)
 231: regl -> (DIVS I32 regl rc)
 232: regl -> (DIVU I32 regl rc)
 233: regl -> (ADD I32 regl rc)
 234: regl -> (SUB I32 regl rc)
 235: regl -> (MUL I32 regl rc)
 236: regl -> (DIVS I32 regl regl)
 237: regl -> (DIVU I32 regl regl)
 238: regl -> (MODS I32 regl regl)
 239: regl -> (MODU I32 regl regl)
 240: regl -> (MODS I32 regl regl)
 241: regl -> (MODU I32 regl regl)
 242: regl -> (NEG I32 regl)
 243: regl -> (BNOT I32 regl)
 244: regl -> (BNOT I32 regl)
 245: regl -> (RSHS I32 regl rcs)
 246: regl -> (RSHU I32 regl rcs)
 247: regl -> (LSHS I32 regl rcs)
 248: regf -> (ADD F32 regf regf)
 249: regf -> (SUB F32 regf regf)
 250: regf -> (MUL F32 regf regf)
 251: regf -> (DIVS F32 regf regf)
 252: regf -> (NEG F32 regf)
 253: regd -> (ADD F64 regd regd)
 254: regd -> (SUB F64 regd regd)
 255: regd -> (MUL F64 regd regd)
 256: regd -> (DIVS F64 regd regd)
 257: regd -> (NEG F64 regd)
 258: regl -> (ADD I32 regl rc)
 259: regl -> (SUB I32 regl rc)
 260: regl -> (MUL I32 regl rc)
 261: regl -> (DIVS I32 regl rc)
 262: regl -> (DIVU I32 regl rc)
 263: regl -> (MODS I32 regl regl)
 264: regl -> (MODU I32 regl regl)
 265: regl -> (BNOT I32 regl)
 266: regl -> (CONVSX I32 regh)
 267: regl -> (CONVSX I32 regb)
 268: regh -> (CONVSX I16 regb)
 269: regl -> (CONVZX I32 regh)
 270: regl -> (CONVZX I32 regb)
 271: regh -> (CONVZX I16 regb)
 272: regh -> (CONVIT I16 regl)
 273: regb -> (CONVIT I8 regl)
 274: regb -> (CONVIT I8 regh)
 275: regd -> (CONVFX F64 regf)
 276: regf -> (CONVFT F32 regd)
 277: regl -> (CONVFS I32 regf)
 278: regh -> (CONVFS I16 regf)
 279: regb -> (CONVFS I8 regf)
 280: regl -> (CONVFS I32 regd)
 281: regh -> (CONVFS I16 regd)
 282: regb -> (CONVFS I8 regd)
 283: regf -> (CONVSF F32 regl)
 284: regd -> (CONVSF F64 regl)
 285: regd -> (CONVFX F64 regf)
 286: regf -> (CONVFT F32 regd)
 287: void -> (CALL _ fun)
 288: void -> (CALL _ fun)
 289: void -> (CALL _ fun)
 290: void -> (CALL _ fun)
 291: void -> (CALL _ fun)
 292: void -> (CALL _ fun)
 293: void -> (PARALLEL _ void)
*/
/*
Sorted Productions:
 32: addr -> regl
 42: rc -> regl
 45: rcs -> regl
 11: xregb -> _xregb
 12: xregh -> _xregh
 13: xregl -> _xregl
 14: xregf -> _xregf
 15: xregd -> _xregd
 16: regb -> xregb
 17: regh -> xregh
 18: regl -> xregl
 19: regf -> xregf
 20: regd -> xregd
 46: rcs -> con5
 47: regl -> con16u
 48: regh -> con16u
 49: regb -> con16u
 50: regl -> con16sminus
 51: regh -> con16sminus
 52: regb -> con16sminus
 26: con32 -> con32u
 27: con32 -> con32s
 33: addr -> con32
 37: asmcon -> con32
 43: rc -> con32
 53: regl -> con32
 54: regh -> con32
 55: regb -> con32
 44: rc -> conofs
 56: regf -> confd
 57: regd -> confd
 31: sta -> _sta
 36: asmcon -> sta
 58: regl -> sta
 59: regh -> sta
 41: fun -> addr
 40: fun -> asmcon
 21: con5 -> (INTCONST _)
 22: con16u -> (INTCONST _)
 23: con16sminus -> (INTCONST _)
 24: con32u -> (INTCONST _)
 25: con32s -> (INTCONST _)
 63: _1 -> (INTCONST _ 0)
 29: confd -> (FLOATCONST _)
 30: _sta -> (STATIC _)
 1: _xregb -> (REG I8)
 3: _xregh -> (REG I16)
 5: _xregl -> (REG I32)
 7: _xregf -> (REG F32)
 9: _xregd -> (REG F64)
 2: _xregb -> (SUBREG I8)
 4: _xregh -> (SUBREG I16)
 6: _xregl -> (SUBREG I32)
 8: _xregf -> (SUBREG F32)
 64: _2 -> (SUBREG F32 xregl _1)
 66: _3 -> (SUBREG F32 regl _1)
 10: _xregd -> (SUBREG F64)
 131: label -> (LABEL _)
 242: regl -> (NEG I32 regl)
 252: regf -> (NEG F32 regf)
 257: regd -> (NEG F64 regd)
 34: addr -> (ADD I32 regl con32)
 35: addr -> (ADD I32 regl conofs)
 38: asmcon -> (ADD I32 asmcon con32)
 228: regl -> (ADD I32 regl rc)
 233: regl -> (ADD I32 regl rc)
 258: regl -> (ADD I32 regl rc)
 248: regf -> (ADD F32 regf regf)
 253: regd -> (ADD F64 regd regd)
 39: asmcon -> (SUB I32 asmcon con32)
 229: regl -> (SUB I32 regl rc)
 234: regl -> (SUB I32 regl rc)
 259: regl -> (SUB I32 regl rc)
 249: regf -> (SUB F32 regf regf)
 254: regd -> (SUB F64 regd regd)
 230: regl -> (MUL I32 regl rc)
 235: regl -> (MUL I32 regl rc)
 260: regl -> (MUL I32 regl rc)
 250: regf -> (MUL F32 regf regf)
 255: regd -> (MUL F64 regd regd)
 231: regl -> (DIVS I32 regl rc)
 236: regl -> (DIVS I32 regl regl)
 261: regl -> (DIVS I32 regl rc)
 251: regf -> (DIVS F32 regf regf)
 256: regd -> (DIVS F64 regd regd)
 232: regl -> (DIVU I32 regl rc)
 237: regl -> (DIVU I32 regl regl)
 262: regl -> (DIVU I32 regl rc)
 238: regl -> (MODS I32 regl regl)
 240: regl -> (MODS I32 regl regl)
 263: regl -> (MODS I32 regl regl)
 239: regl -> (MODU I32 regl regl)
 241: regl -> (MODU I32 regl regl)
 264: regl -> (MODU I32 regl regl)
 268: regh -> (CONVSX I16 regb)
 100: regl -> (CONVSX I32 _5)
 101: regl -> (CONVSX I32 _6)
 102: regl -> (CONVSX I32 _5)
 103: regl -> (CONVSX I32 _6)
 118: regl -> (CONVSX I32 _5)
 119: regl -> (CONVSX I32 _6)
 266: regl -> (CONVSX I32 regh)
 267: regl -> (CONVSX I32 regb)
 271: regh -> (CONVZX I16 regb)
 104: regl -> (CONVZX I32 _5)
 105: regl -> (CONVZX I32 _6)
 106: regl -> (CONVZX I32 _5)
 107: regl -> (CONVZX I32 _6)
 120: regl -> (CONVZX I32 _5)
 121: regl -> (CONVZX I32 _6)
 269: regl -> (CONVZX I32 regh)
 270: regl -> (CONVZX I32 regb)
 129: _10 -> (CONVIT I8 regl)
 273: regb -> (CONVIT I8 regl)
 274: regb -> (CONVIT I8 regh)
 127: _9 -> (CONVIT I16 regl)
 272: regh -> (CONVIT I16 regl)
 275: regd -> (CONVFX F64 regf)
 285: regd -> (CONVFX F64 regf)
 276: regf -> (CONVFT F32 regd)
 286: regf -> (CONVFT F32 regd)
 279: regb -> (CONVFS I8 regf)
 282: regb -> (CONVFS I8 regd)
 278: regh -> (CONVFS I16 regf)
 281: regh -> (CONVFS I16 regd)
 277: regl -> (CONVFS I32 regf)
 280: regl -> (CONVFS I32 regd)
 283: regf -> (CONVSF F32 regl)
 284: regd -> (CONVSF F64 regl)
 225: regl -> (BAND I32 regl rc)
 226: regl -> (BOR I32 regl rc)
 227: regl -> (BXOR I32 regl rc)
 243: regl -> (BNOT I32 regl)
 244: regl -> (BNOT I32 regl)
 265: regl -> (BNOT I32 regl)
 247: regl -> (LSHS I32 regl rcs)
 245: regl -> (RSHS I32 regl rcs)
 246: regl -> (RSHU I32 regl rcs)
 134: _11 -> (TSTEQ I32 regl rc)
 164: _21 -> (TSTEQ I32 regd regd)
 172: _25 -> (TSTEQ I32 regf regf)
 136: _12 -> (TSTNE I32 regl rc)
 166: _22 -> (TSTNE I32 regd regd)
 174: _26 -> (TSTNE I32 regf regf)
 138: _13 -> (TSTLTS I32 regl rc)
 168: _23 -> (TSTLTS I32 regd regd)
 176: _27 -> (TSTLTS I32 regf regf)
 140: _14 -> (TSTLES I32 regl rc)
 170: _24 -> (TSTLES I32 regd regd)
 178: _28 -> (TSTLES I32 regf regf)
 142: _15 -> (TSTGTS I32 regl rc)
 188: _29 -> (TSTGTS I32 regd regd)
 192: _31 -> (TSTGTS I32 regf regf)
 144: _16 -> (TSTGES I32 regl rc)
 190: _30 -> (TSTGES I32 regd regd)
 194: _32 -> (TSTGES I32 regf regf)
 146: _17 -> (TSTLTU I32 regl rc)
 148: _18 -> (TSTLEU I32 regl rc)
 211: _34 -> (TSTLEU I32 regl regl)
 150: _19 -> (TSTGTU I32 regl rc)
 209: _33 -> (TSTGTU I32 regl regl)
 152: _20 -> (TSTGEU I32 regl rc)
 79: _6 -> (MEM I8 addr)
 92: regb -> (MEM I8 addr)
 97: regb -> (MEM I8 addr)
 115: regb -> (MEM I8 addr)
 77: _5 -> (MEM I16 addr)
 91: regh -> (MEM I16 addr)
 96: regh -> (MEM I16 addr)
 114: regh -> (MEM I16 addr)
 75: _4 -> (MEM I32 addr)
 90: regl -> (MEM I32 addr)
 95: regl -> (MEM I32 addr)
 113: regl -> (MEM I32 addr)
 83: _8 -> (MEM F32 addr)
 94: regf -> (MEM F32 addr)
 99: regf -> (MEM F32 addr)
 117: regf -> (MEM F32 addr)
 81: _7 -> (MEM F64 addr)
 93: regd -> (MEM F64 addr)
 98: regd -> (MEM F64 addr)
 116: regd -> (MEM F64 addr)
 62: void -> (SET I8 xregb regb)
 124: void -> (SET I8 _6 regb)
 130: void -> (SET I8 _6 _10)
 61: void -> (SET I16 xregh regh)
 123: void -> (SET I16 _5 regh)
 128: void -> (SET I16 _5 _9)
 60: void -> (SET I32 xregl regl)
 70: void -> (SET I32 xregl confd)
 71: void -> (SET I32 xregl regd)
 72: void -> (SET I32 xregl regd)
 122: void -> (SET I32 _4 regl)
 65: void -> (SET F32 _2 regf)
 67: void -> (SET F32 xregf _3)
 69: void -> (SET F32 xregf regf)
 126: void -> (SET F32 _8 regf)
 68: void -> (SET F64 xregd regd)
 73: void -> (SET F64 xregd regl)
 74: void -> (SET F64 xregd regl)
 125: void -> (SET F64 _7 regd)
 132: void -> (JUMP _ label)
 133: void -> (JUMP _ label)
 200: void -> (JUMP _ label)
 135: void -> (JUMPC _ _11 label label)
 137: void -> (JUMPC _ _12 label label)
 139: void -> (JUMPC _ _13 label label)
 141: void -> (JUMPC _ _14 label label)
 143: void -> (JUMPC _ _15 label label)
 145: void -> (JUMPC _ _16 label label)
 147: void -> (JUMPC _ _17 label label)
 149: void -> (JUMPC _ _18 label label)
 151: void -> (JUMPC _ _19 label label)
 153: void -> (JUMPC _ _20 label label)
 154: void -> (JUMPC _ _11 label label)
 155: void -> (JUMPC _ _12 label label)
 156: void -> (JUMPC _ _13 label label)
 157: void -> (JUMPC _ _14 label label)
 158: void -> (JUMPC _ _15 label label)
 159: void -> (JUMPC _ _16 label label)
 160: void -> (JUMPC _ _17 label label)
 161: void -> (JUMPC _ _18 label label)
 162: void -> (JUMPC _ _19 label label)
 163: void -> (JUMPC _ _20 label label)
 165: void -> (JUMPC _ _21 label label)
 167: void -> (JUMPC _ _22 label label)
 169: void -> (JUMPC _ _23 label label)
 171: void -> (JUMPC _ _24 label label)
 173: void -> (JUMPC _ _25 label label)
 175: void -> (JUMPC _ _26 label label)
 177: void -> (JUMPC _ _27 label label)
 179: void -> (JUMPC _ _28 label label)
 180: void -> (JUMPC _ _21 label label)
 181: void -> (JUMPC _ _22 label label)
 182: void -> (JUMPC _ _23 label label)
 183: void -> (JUMPC _ _24 label label)
 184: void -> (JUMPC _ _25 label label)
 185: void -> (JUMPC _ _26 label label)
 186: void -> (JUMPC _ _27 label label)
 187: void -> (JUMPC _ _28 label label)
 189: void -> (JUMPC _ _29 label label)
 191: void -> (JUMPC _ _30 label label)
 193: void -> (JUMPC _ _31 label label)
 195: void -> (JUMPC _ _32 label label)
 196: void -> (JUMPC _ _29 label label)
 197: void -> (JUMPC _ _30 label label)
 198: void -> (JUMPC _ _31 label label)
 199: void -> (JUMPC _ _32 label label)
 201: void -> (JUMPC _ _11 label label)
 202: void -> (JUMPC _ _12 label label)
 203: void -> (JUMPC _ _17 label label)
 204: void -> (JUMPC _ _13 label label)
 205: void -> (JUMPC _ _14 label label)
 206: void -> (JUMPC _ _15 label label)
 207: void -> (JUMPC _ _16 label label)
 208: void -> (JUMPC _ _20 label label)
 210: void -> (JUMPC _ _33 label label)
 212: void -> (JUMPC _ _34 label label)
 213: void -> (JUMPC _ _21 label label)
 214: void -> (JUMPC _ _23 label label)
 215: void -> (JUMPC _ _24 label label)
 216: void -> (JUMPC _ _25 label label)
 217: void -> (JUMPC _ _27 label label)
 218: void -> (JUMPC _ _28 label label)
 219: void -> (JUMPC _ _22 label label)
 220: void -> (JUMPC _ _26 label label)
 221: void -> (JUMPC _ _29 label label)
 222: void -> (JUMPC _ _30 label label)
 223: void -> (JUMPC _ _31 label label)
 224: void -> (JUMPC _ _32 label label)
 287: void -> (CALL _ fun)
 288: void -> (CALL _ fun)
 289: void -> (CALL _ fun)
 290: void -> (CALL _ fun)
 291: void -> (CALL _ fun)
 292: void -> (CALL _ fun)
 293: void -> (PARALLEL _ void)
 28: conofs -> (LIST _ con32)
 76: void -> (LIST _ regl _4)
 78: void -> (LIST _ regh _5)
 80: void -> (LIST _ regb _6)
 82: void -> (LIST _ regd _7)
 84: void -> (LIST _ regf _8)
 85: void -> (LIST _ regl _4)
 86: void -> (LIST _ regh _5)
 87: void -> (LIST _ regb _6)
 88: void -> (LIST _ regd _7)
 89: void -> (LIST _ regf _8)
 108: void -> (LIST _ regl _4)
 109: void -> (LIST _ regh _5)
 110: void -> (LIST _ regb _6)
 111: void -> (LIST _ regd _7)
 112: void -> (LIST _ regf _8)
*/
/*
Productions:
 1: _rewr -> (CONVFU _ _)
 2: _rewr -> (CONVUF _ _)
 3: _rewr -> (PROLOGUE _)
 4: _rewr -> (EPILOGUE _)
 5: _rewr -> (CALL _)
 6: _rewr -> (JUMPN _)
 7: _rewr -> (SET _)
*/
/*
Sorted Productions:
 1: _rewr -> (CONVFU _ _)
 2: _rewr -> (CONVUF _ _)
 7: _rewr -> (SET _)
 6: _rewr -> (JUMPN _)
 5: _rewr -> (CALL _)
 3: _rewr -> (PROLOGUE _)
 4: _rewr -> (EPILOGUE _)
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
import coins.backend.cfg.BasicBlk;
import coins.backend.lir.LirLabelRef;
import java.lang.System;
import java.lang.Integer;
import java.lang.Long;
import java.util.*;




public class CodeGenerator_mips extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_mips() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 2;
    static final int NRULES = 7 + 1;
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
    static final int NNONTERM = 67;
    static final int NRULES = 293 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT_void = 1;
    static final int NT_regl = 2;
    static final int NT_regh = 3;
    static final int NT_regb = 4;
    static final int NT_regd = 5;
    static final int NT_regf = 6;
    static final int NT__xregb = 7;
    static final int NT__xregh = 8;
    static final int NT__xregl = 9;
    static final int NT__xregf = 10;
    static final int NT__xregd = 11;
    static final int NT_xregb = 12;
    static final int NT_xregh = 13;
    static final int NT_xregl = 14;
    static final int NT_xregf = 15;
    static final int NT_xregd = 16;
    static final int NT_con5 = 17;
    static final int NT_con16u = 18;
    static final int NT_con16sminus = 19;
    static final int NT_con32u = 20;
    static final int NT_con32s = 21;
    static final int NT_con32 = 22;
    static final int NT_conofs = 23;
    static final int NT_confd = 24;
    static final int NT__sta = 25;
    static final int NT_sta = 26;
    static final int NT_addr = 27;
    static final int NT_asmcon = 28;
    static final int NT_fun = 29;
    static final int NT_rc = 30;
    static final int NT_rcs = 31;
    static final int NT__1 = 32;
    static final int NT__2 = 33;
    static final int NT__3 = 34;
    static final int NT__4 = 35;
    static final int NT__5 = 36;
    static final int NT__6 = 37;
    static final int NT__7 = 38;
    static final int NT__8 = 39;
    static final int NT__9 = 40;
    static final int NT__10 = 41;
    static final int NT_label = 42;
    static final int NT__11 = 43;
    static final int NT__12 = 44;
    static final int NT__13 = 45;
    static final int NT__14 = 46;
    static final int NT__15 = 47;
    static final int NT__16 = 48;
    static final int NT__17 = 49;
    static final int NT__18 = 50;
    static final int NT__19 = 51;
    static final int NT__20 = 52;
    static final int NT__21 = 53;
    static final int NT__22 = 54;
    static final int NT__23 = 55;
    static final int NT__24 = 56;
    static final int NT__25 = 57;
    static final int NT__26 = 58;
    static final int NT__27 = 59;
    static final int NT__28 = 60;
    static final int NT__29 = 61;
    static final int NT__30 = 62;
    static final int NT__31 = 63;
    static final int NT__32 = 64;
    static final int NT__33 = 65;
    static final int NT__34 = 66;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_void: return "void";
      case NT_regl: return "regl";
      case NT_regh: return "regh";
      case NT_regb: return "regb";
      case NT_regd: return "regd";
      case NT_regf: return "regf";
      case NT__xregb: return "_xregb";
      case NT__xregh: return "_xregh";
      case NT__xregl: return "_xregl";
      case NT__xregf: return "_xregf";
      case NT__xregd: return "_xregd";
      case NT_xregb: return "xregb";
      case NT_xregh: return "xregh";
      case NT_xregl: return "xregl";
      case NT_xregf: return "xregf";
      case NT_xregd: return "xregd";
      case NT_con5: return "con5";
      case NT_con16u: return "con16u";
      case NT_con16sminus: return "con16sminus";
      case NT_con32u: return "con32u";
      case NT_con32s: return "con32s";
      case NT_con32: return "con32";
      case NT_conofs: return "conofs";
      case NT_confd: return "confd";
      case NT__sta: return "_sta";
      case NT_sta: return "sta";
      case NT_addr: return "addr";
      case NT_asmcon: return "asmcon";
      case NT_fun: return "fun";
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
      case NT_label: return "label";
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
          record(NT_addr, 0 + cost1, 0 + cost2, 32);
          record(NT_rc, 0 + cost1, 0 + cost2, 42);
          record(NT_rcs, 0 + cost1, 0 + cost2, 45);
          break;
        case NT__xregb:
          record(NT_xregb, 0 + cost1, 0 + cost2, 11);
          break;
        case NT__xregh:
          record(NT_xregh, 0 + cost1, 0 + cost2, 12);
          break;
        case NT__xregl:
          record(NT_xregl, 0 + cost1, 0 + cost2, 13);
          break;
        case NT__xregf:
          record(NT_xregf, 0 + cost1, 0 + cost2, 14);
          break;
        case NT__xregd:
          record(NT_xregd, 0 + cost1, 0 + cost2, 15);
          break;
        case NT_xregb:
          record(NT_regb, 0 + cost1, 0 + cost2, 16);
          break;
        case NT_xregh:
          record(NT_regh, 0 + cost1, 0 + cost2, 17);
          break;
        case NT_xregl:
          record(NT_regl, 0 + cost1, 0 + cost2, 18);
          break;
        case NT_xregf:
          record(NT_regf, 0 + cost1, 0 + cost2, 19);
          break;
        case NT_xregd:
          record(NT_regd, 0 + cost1, 0 + cost2, 20);
          break;
        case NT_con5:
          record(NT_rcs, 0 + cost1, 0 + cost2, 46);
          break;
        case NT_con16u:
          record(NT_regl, 1 + cost1, 1 + cost2, 47);
          record(NT_regh, 1 + cost1, 1 + cost2, 48);
          record(NT_regb, 1 + cost1, 1 + cost2, 49);
          break;
        case NT_con16sminus:
          record(NT_regl, 1 + cost1, 1 + cost2, 50);
          record(NT_regh, 1 + cost1, 1 + cost2, 51);
          record(NT_regb, 1 + cost1, 1 + cost2, 52);
          break;
        case NT_con32u:
          record(NT_con32, 0 + cost1, 0 + cost2, 26);
          break;
        case NT_con32s:
          record(NT_con32, 0 + cost1, 0 + cost2, 27);
          break;
        case NT_con32:
          record(NT_addr, 0 + cost1, 0 + cost2, 33);
          record(NT_asmcon, 0 + cost1, 0 + cost2, 37);
          record(NT_rc, 0 + cost1, 0 + cost2, 43);
          record(NT_regl, 2 + cost1, 2 + cost2, 53);
          record(NT_regh, 2 + cost1, 2 + cost2, 54);
          record(NT_regb, 2 + cost1, 2 + cost2, 55);
          break;
        case NT_conofs:
          record(NT_rc, 0 + cost1, 0 + cost2, 44);
          break;
        case NT_confd:
          record(NT_regf, 1 + cost1, 1 + cost2, 56);
          record(NT_regd, 1 + cost1, 1 + cost2, 57);
          break;
        case NT__sta:
          record(NT_sta, 0 + cost1, 0 + cost2, 31);
          break;
        case NT_sta:
          record(NT_asmcon, 0 + cost1, 0 + cost2, 36);
          record(NT_regl, 2 + cost1, 2 + cost2, 58);
          record(NT_regh, 2 + cost1, 2 + cost2, 59);
          break;
        case NT_addr:
          record(NT_fun, 0 + cost1, 0 + cost2, 41);
          break;
        case NT_asmcon:
          record(NT_fun, 0 + cost1, 0 + cost2, 40);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        if (0 <= ((LirIconst)t).unsignedValue() && ((LirIconst)t).unsignedValue() <= 31) record(NT_con5, 0, 0, 21);
        if (0L <= ((LirIconst)t).unsignedValue() && ((LirIconst)t).unsignedValue() <= 65535L) record(NT_con16u, 0, 0, 22);
        if (-32768L <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() < 0L) record(NT_con16sminus, 0, 0, 23);
        if (0L <= ((LirIconst)t).unsignedValue() && ((LirIconst)t).unsignedValue() <= 4294967295L) record(NT_con32u, 0, 0, 24);
        if (-2147483648L <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 2147483647L) record(NT_con32s, 0, 0, 25);
        if (((LirIconst)t).value == 0) record(NT__1, 0, 0, 63);
        break;
      case Op.FLOATCONST:
        record(NT_confd, 0, 0, 29);
        break;
      case Op.STATIC:
        record(NT__sta, 0, 0, 30);
        break;
      case Op.REG:
        if (t.type == 130) {
          record(NT__xregb, 0, 0, 1);
        }
        if (t.type == 258) {
          record(NT__xregh, 0, 0, 3);
        }
        if (t.type == 514) {
          record(NT__xregl, 0, 0, 5);
        }
        if (t.type == 516) {
          record(NT__xregf, 0, 0, 7);
        }
        if (t.type == 1028) {
          record(NT__xregd, 0, 0, 9);
        }
        break;
      case Op.SUBREG:
        if (t.type == 130) {
          record(NT__xregb, 0, 0, 2);
        }
        if (t.type == 258) {
          record(NT__xregh, 0, 0, 4);
        }
        if (t.type == 514) {
          record(NT__xregl, 0, 0, 6);
        }
        if (t.type == 516) {
          record(NT__xregf, 0, 0, 8);
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT__1] != 0) record(NT__2, 0 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT__1], 64);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__1] != 0) record(NT__3, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__1], 66);
        }
        if (t.type == 1028) {
          record(NT__xregd, 0, 0, 10);
        }
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 131);
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 242);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 252);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 257);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_con32] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_con32], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_con32], 34);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_conofs] != 0) record(NT_addr, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_conofs], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_conofs], 35);
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con32] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con32], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con32], 38);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 228);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 233);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 258);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 248);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 253);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcon] != 0) if (kids[1].rule[NT_con32] != 0) record(NT_asmcon, 0 + kids[0].cost1[NT_asmcon] + kids[1].cost1[NT_con32], 0 + kids[0].cost2[NT_asmcon] + kids[1].cost2[NT_con32], 39);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 229);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 234);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 259);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 249);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 254);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 230);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 235);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 260);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 250);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 255);
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 231);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 236);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 261);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 251);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 256);
        }
        break;
      case Op.DIVU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 232);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 237);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 262);
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 238);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "standard") record(NT_regl, 4 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 4 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 240);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "linux") record(NT_regl, 4 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 4 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 263);
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 239);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "standard") record(NT_regl, 4 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 4 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 241);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "linux") record(NT_regl, 4 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 4 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 264);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 268);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT__5] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT__5], 1 + kids[0].cost2[NT__5], 100);
          if (kids[0].rule[NT__6] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 101);
          if (kids[0].rule[NT__5] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT__5], 1 + kids[0].cost2[NT__5], 102);
          if (kids[0].rule[NT__6] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 103);
          if (kids[0].rule[NT__5] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT__5], 1 + kids[0].cost2[NT__5], 118);
          if (kids[0].rule[NT__6] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 119);
          if (kids[0].rule[NT_regh] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 266);
          if (kids[0].rule[NT_regb] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 267);
        }
        break;
      case Op.CONVZX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 271);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT__5] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT__5], 1 + kids[0].cost2[NT__5], 104);
          if (kids[0].rule[NT__6] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 105);
          if (kids[0].rule[NT__5] != 0) if (convention == "spim") record(NT_regl, 2 + kids[0].cost1[NT__5], 2 + kids[0].cost2[NT__5], 106);
          if (kids[0].rule[NT__6] != 0) if (convention == "spim") record(NT_regl, 2 + kids[0].cost1[NT__6], 2 + kids[0].cost2[NT__6], 107);
          if (kids[0].rule[NT__5] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT__5], 1 + kids[0].cost2[NT__5], 120);
          if (kids[0].rule[NT__6] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT__6], 1 + kids[0].cost2[NT__6], 121);
          if (kids[0].rule[NT_regh] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 269);
          if (kids[0].rule[NT_regb] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 270);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_regl] != 0) record(NT__10, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 129);
          if (kids[0].rule[NT_regl] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 273);
          if (kids[0].rule[NT_regh] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 274);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regl] != 0) record(NT__9, 0 + kids[0].cost1[NT_regl], 0 + kids[0].cost2[NT_regl], 127);
          if (kids[0].rule[NT_regl] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 272);
        }
        break;
      case Op.CONVFX:
        if (t.type == 1028) {
          if (kids[0].rule[NT_regf] != 0) if (convention == "standard") record(NT_regd, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 275);
          if (kids[0].rule[NT_regf] != 0) if (convention == "linux") record(NT_regd, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 285);
        }
        break;
      case Op.CONVFT:
        if (t.type == 516) {
          if (kids[0].rule[NT_regd] != 0) if (convention == "standard") record(NT_regf, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 276);
          if (kids[0].rule[NT_regd] != 0) if (convention == "linux") record(NT_regf, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 286);
        }
        break;
      case Op.CONVFS:
        if (t.type == 130) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regb, 4 + kids[0].cost1[NT_regf], 4 + kids[0].cost2[NT_regf], 279);
          if (kids[0].rule[NT_regd] != 0) record(NT_regb, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 282);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regh, 4 + kids[0].cost1[NT_regf], 4 + kids[0].cost2[NT_regf], 278);
          if (kids[0].rule[NT_regd] != 0) record(NT_regh, 4 + kids[0].cost1[NT_regd], 4 + kids[0].cost2[NT_regd], 281);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 277);
          if (kids[0].rule[NT_regd] != 0) record(NT_regl, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 280);
        }
        break;
      case Op.CONVSF:
        if (t.type == 516) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regf, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 283);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regl] != 0) record(NT_regd, 2 + kids[0].cost1[NT_regl], 2 + kids[0].cost2[NT_regl], 284);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 225);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 226);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 227);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 243);
          if (kids[0].rule[NT_regl] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 244);
          if (kids[0].rule[NT_regl] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_regl], 1 + kids[0].cost2[NT_regl], 265);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 247);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 245);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rcs] != 0) record(NT_regl, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rcs], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rcs], 246);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__11, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 134);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__21, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 164);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__25, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 172);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__12, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 136);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__22, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 166);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__26, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 174);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__13, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 138);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__23, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 168);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__27, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 176);
        }
        break;
      case Op.TSTLES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__14, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 140);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__24, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 170);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__28, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 178);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__15, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 142);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__29, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 188);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__31, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 192);
        }
        break;
      case Op.TSTGES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__16, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 144);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__30, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 190);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__32, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 194);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__17, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 146);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__18, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 148);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__34, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 211);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__19, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 150);
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT__33, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_regl], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_regl], 209);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT_rc] != 0) record(NT__20, 0 + kids[0].cost1[NT_regl] + kids[1].cost1[NT_rc], 0 + kids[0].cost2[NT_regl] + kids[1].cost2[NT_rc], 152);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addr] != 0) record(NT__6, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 79);
          if (kids[0].rule[NT_addr] != 0) if (convention == "standard") record(NT_regb, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 92);
          if (kids[0].rule[NT_addr] != 0) if (convention == "spim") record(NT_regb, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 97);
          if (kids[0].rule[NT_addr] != 0) if (convention == "linux") record(NT_regb, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 115);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addr] != 0) record(NT__5, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 77);
          if (kids[0].rule[NT_addr] != 0) if (convention == "standard") record(NT_regh, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 91);
          if (kids[0].rule[NT_addr] != 0) if (convention == "spim") record(NT_regh, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 96);
          if (kids[0].rule[NT_addr] != 0) if (convention == "linux") record(NT_regh, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 114);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addr] != 0) record(NT__4, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 75);
          if (kids[0].rule[NT_addr] != 0) if (convention == "standard") record(NT_regl, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 90);
          if (kids[0].rule[NT_addr] != 0) if (convention == "spim") record(NT_regl, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 95);
          if (kids[0].rule[NT_addr] != 0) if (convention == "linux") record(NT_regl, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 113);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_addr] != 0) record(NT__8, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 83);
          if (kids[0].rule[NT_addr] != 0) if (convention == "standard") record(NT_regf, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 94);
          if (kids[0].rule[NT_addr] != 0) if (convention == "spim") record(NT_regf, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 99);
          if (kids[0].rule[NT_addr] != 0) if (convention == "linux") record(NT_regf, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 117);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_addr] != 0) record(NT__7, 0 + kids[0].cost1[NT_addr], 0 + kids[0].cost2[NT_addr], 81);
          if (kids[0].rule[NT_addr] != 0) if (convention == "standard") record(NT_regd, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 93);
          if (kids[0].rule[NT_addr] != 0) if (convention == "spim") record(NT_regd, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 98);
          if (kids[0].rule[NT_addr] != 0) if (convention == "linux") record(NT_regd, 1 + kids[0].cost1[NT_addr], 1 + kids[0].cost2[NT_addr], 116);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 62);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT__6] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT__6] + kids[1].cost2[NT_regb], 124);
          if (kids[0].rule[NT__6] != 0) if (kids[1].rule[NT__10] != 0) record(NT_void, 1 + kids[0].cost1[NT__6] + kids[1].cost1[NT__10], 1 + kids[0].cost2[NT__6] + kids[1].cost2[NT__10], 130);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_regh], 61);
          if (kids[0].rule[NT__5] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT__5] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT__5] + kids[1].cost2[NT_regh], 123);
          if (kids[0].rule[NT__5] != 0) if (kids[1].rule[NT__9] != 0) record(NT_void, 1 + kids[0].cost1[NT__5] + kids[1].cost1[NT__9], 1 + kids[0].cost2[NT__5] + kids[1].cost2[NT__9], 128);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regl], 60);
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_confd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_confd], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_confd], 70);
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regd] != 0) if (convention != "linux") record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regd], 71);
          if (kids[0].rule[NT_xregl] != 0) if (kids[1].rule[NT_regd] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_xregl] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregl] + kids[1].cost2[NT_regd], 72);
          if (kids[0].rule[NT__4] != 0) if (kids[1].rule[NT_regl] != 0) record(NT_void, 1 + kids[0].cost1[NT__4] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT__4] + kids[1].cost2[NT_regl], 122);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT__2] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT__2] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT__2] + kids[1].cost2[NT_regf], 65);
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT__3] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT__3], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT__3], 67);
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 69);
          if (kids[0].rule[NT__8] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT__8] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT__8] + kids[1].cost2[NT_regf], 126);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 68);
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regl] != 0) if (convention != "linux") record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regl], 73);
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regl] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regl], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regl], 74);
          if (kids[0].rule[NT__7] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT__7] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT__7] + kids[1].cost2[NT_regd], 125);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 132);
        if (kids[0].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 133);
        if (kids[0].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 200);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__11] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__11] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 135);
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 137);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 139);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 141);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 143);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 145);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 147);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 149);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 151);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 153);
        if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__11] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__11] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 154);
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 155);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 156);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 157);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 158);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 159);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 160);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 161);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 162);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 163);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 165);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 167);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 169);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 171);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 173);
        if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__26] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__26] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 175);
        if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__27] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__27] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 177);
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__28] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__28] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 179);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 180);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 181);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 182);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 183);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 184);
        if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__26] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__26] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 185);
        if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__27] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__27] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 186);
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__28] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__28] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 187);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__29] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__29] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 189);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__30] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__30] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 191);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__31] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__31] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 193);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "standard") record(NT_void, 3 + kids[0].cost1[NT__32] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__32] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 195);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__29] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__29] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 196);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__30] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__30] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 197);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__31] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__31] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 198);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "spim") record(NT_void, 3 + kids[0].cost1[NT__32] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__32] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 199);
        if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__11] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__11] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 201);
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 202);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 203);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 204);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 205);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 206);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 207);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 208);
        if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__33] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__33] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 210);
        if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT__34] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 1 + kids[0].cost2[NT__34] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 212);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 213);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 214);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 215);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 216);
        if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__27] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__27] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 217);
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__28] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__28] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 218);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 219);
        if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__26] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__26] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 220);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__29] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__29] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 221);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__30] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__30] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 222);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__31] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__31] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 223);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (convention == "linux") record(NT_void, 3 + kids[0].cost1[NT__32] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 3 + kids[0].cost2[NT__32] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 224);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_fun] != 0) if ((t.kid(0).opCode != Op.REG) && (convention == "standard")) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 287);
        if (kids[0].rule[NT_fun] != 0) if ((t.kid(0).opCode != Op.REG) && (convention == "spim")) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 288);
        if (kids[0].rule[NT_fun] != 0) if ((t.kid(0).opCode == Op.REG) && (convention == "standard")) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 289);
        if (kids[0].rule[NT_fun] != 0) if ((t.kid(0).opCode == Op.REG) && (convention == "spim")) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 290);
        if (kids[0].rule[NT_fun] != 0) if ((t.kid(0).opCode != Op.REG) && (convention == "linux")) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 291);
        if (kids[0].rule[NT_fun] != 0) if ((t.kid(0).opCode == Op.REG) && (convention == "linux")) record(NT_void, 2 + kids[0].cost1[NT_fun], 2 + kids[0].cost2[NT_fun], 292);
        break;
      case Op.PARALLEL:
        if (kids.length == 1) if (kids[0].rule[NT_void] != 0) record(NT_void, 0 + kids[0].cost1[NT_void], 0 + kids[0].cost2[NT_void], 293);
        break;
      case Op.LIST:
        if (kids.length == 1) if (kids[0].rule[NT_con32] != 0) record(NT_conofs, 0 + kids[0].cost1[NT_con32], 0 + kids[0].cost2[NT_con32], 28);
        if (kids.length == 2) if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 76);
        if (kids.length == 2) if (kids[0].rule[NT_regh] != 0) if (kids[1].rule[NT__5] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT_regh] + kids[1].cost1[NT__5], 1 + kids[0].cost2[NT_regh] + kids[1].cost2[NT__5], 78);
        if (kids.length == 2) if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT__6] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT_regb] + kids[1].cost1[NT__6], 1 + kids[0].cost2[NT_regb] + kids[1].cost2[NT__6], 80);
        if (kids.length == 2) if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT__7] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT__7], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT__7], 82);
        if (kids.length == 2) if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT__8] != 0) if (convention == "standard") record(NT_void, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT__8], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT__8], 84);
        if (kids.length == 2) if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 85);
        if (kids.length == 2) if (kids[0].rule[NT_regh] != 0) if (kids[1].rule[NT__5] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT_regh] + kids[1].cost1[NT__5], 1 + kids[0].cost2[NT_regh] + kids[1].cost2[NT__5], 86);
        if (kids.length == 2) if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT__6] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT_regb] + kids[1].cost1[NT__6], 1 + kids[0].cost2[NT_regb] + kids[1].cost2[NT__6], 87);
        if (kids.length == 2) if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT__7] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT__7], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT__7], 88);
        if (kids.length == 2) if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT__8] != 0) if (convention == "spim") record(NT_void, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT__8], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT__8], 89);
        if (kids.length == 2) if (kids[0].rule[NT_regl] != 0) if (kids[1].rule[NT__4] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_regl] + kids[1].cost1[NT__4], 1 + kids[0].cost2[NT_regl] + kids[1].cost2[NT__4], 108);
        if (kids.length == 2) if (kids[0].rule[NT_regh] != 0) if (kids[1].rule[NT__5] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_regh] + kids[1].cost1[NT__5], 1 + kids[0].cost2[NT_regh] + kids[1].cost2[NT__5], 109);
        if (kids.length == 2) if (kids[0].rule[NT_regb] != 0) if (kids[1].rule[NT__6] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_regb] + kids[1].cost1[NT__6], 1 + kids[0].cost2[NT_regb] + kids[1].cost2[NT__6], 110);
        if (kids.length == 2) if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT__7] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_regd] + kids[1].cost1[NT__7], 1 + kids[0].cost2[NT_regd] + kids[1].cost2[NT__7], 111);
        if (kids.length == 2) if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT__8] != 0) if (convention == "linux") record(NT_void, 1 + kids[0].cost1[NT_regf] + kids[1].cost1[NT__8], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT__8], 112);
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
    rulev[32] = new Rule(32, true, false, 27, "32: addr -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[42] = new Rule(42, true, false, 30, "42: rc -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[45] = new Rule(45, true, false, 31, "45: rcs -> regl", null, null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I32*"});
    rulev[11] = new Rule(11, true, false, 12, "11: xregb -> _xregb", null, ImList.list(ImList.list("_reg","$1")), null, 0, false, false, new int[]{7}, new String[]{null, null});
    rulev[12] = new Rule(12, true, false, 13, "12: xregh -> _xregh", null, ImList.list(ImList.list("_reg","$1")), null, 0, false, false, new int[]{8}, new String[]{null, null});
    rulev[13] = new Rule(13, true, false, 14, "13: xregl -> _xregl", null, ImList.list(ImList.list("_reg","$1")), null, 0, false, false, new int[]{9}, new String[]{null, null});
    rulev[14] = new Rule(14, true, false, 15, "14: xregf -> _xregf", null, ImList.list(ImList.list("_reg","$1")), null, 0, false, false, new int[]{10}, new String[]{null, null});
    rulev[15] = new Rule(15, true, false, 16, "15: xregd -> _xregd", null, ImList.list(ImList.list("_reg","$1")), null, 0, false, false, new int[]{11}, new String[]{null, null});
    rulev[16] = new Rule(16, true, false, 4, "16: regb -> xregb", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-I8*", null});
    rulev[17] = new Rule(17, true, false, 3, "17: regh -> xregh", null, null, null, 0, false, false, new int[]{13}, new String[]{"*reg-I16*", null});
    rulev[18] = new Rule(18, true, false, 2, "18: regl -> xregl", null, null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I32*", null});
    rulev[19] = new Rule(19, true, false, 6, "19: regf -> xregf", null, null, null, 0, false, false, new int[]{15}, new String[]{"*reg-F32*", null});
    rulev[20] = new Rule(20, true, false, 5, "20: regd -> xregd", null, null, null, 0, false, false, new int[]{16}, new String[]{"*reg-F64*", null});
    rulev[46] = new Rule(46, true, false, 31, "46: rcs -> con5", null, null, null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[47] = new Rule(47, true, false, 2, "47: regl -> con16u", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-I32*", null});
    rulev[48] = new Rule(48, true, false, 3, "48: regh -> con16u", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-I16*", null});
    rulev[49] = new Rule(49, true, false, 4, "49: regb -> con16u", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-I8*", null});
    rulev[50] = new Rule(50, true, false, 2, "50: regl -> con16sminus", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{19}, new String[]{"*reg-I32*", null});
    rulev[51] = new Rule(51, true, false, 3, "51: regh -> con16sminus", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{19}, new String[]{"*reg-I16*", null});
    rulev[52] = new Rule(52, true, false, 4, "52: regb -> con16sminus", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{19}, new String[]{"*reg-I8*", null});
    rulev[26] = new Rule(26, true, false, 22, "26: con32 -> con32u", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[27] = new Rule(27, true, false, 22, "27: con32 -> con32s", null, null, null, 0, false, false, new int[]{21}, new String[]{null, null});
    rulev[33] = new Rule(33, true, false, 27, "33: addr -> con32", null, null, null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[37] = new Rule(37, true, false, 28, "37: asmcon -> con32", null, null, null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[43] = new Rule(43, true, false, 30, "43: rc -> con32", null, null, null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[53] = new Rule(53, true, false, 2, "53: regl -> con32", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{22}, new String[]{"*reg-I32*", null});
    rulev[54] = new Rule(54, true, false, 3, "54: regh -> con32", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{22}, new String[]{"*reg-I16*", null});
    rulev[55] = new Rule(55, true, false, 4, "55: regb -> con32", ImList.list(ImList.list("li",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{22}, new String[]{"*reg-I8*", null});
    rulev[44] = new Rule(44, true, false, 30, "44: rc -> conofs", null, null, null, 0, false, false, new int[]{23}, new String[]{null, null});
    rulev[56] = new Rule(56, true, false, 6, "56: regf -> confd", ImList.list(ImList.list("li.s",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{24}, new String[]{"*reg-F32*", null});
    rulev[57] = new Rule(57, true, false, 5, "57: regd -> confd", ImList.list(ImList.list("li.d",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{24}, new String[]{"*reg-F64*", null});
    rulev[31] = new Rule(31, true, false, 26, "31: sta -> _sta", null, ImList.list(ImList.list("_static","$1")), null, 0, false, false, new int[]{25}, new String[]{null, null});
    rulev[36] = new Rule(36, true, false, 28, "36: asmcon -> sta", null, null, null, 0, false, false, new int[]{26}, new String[]{null, null});
    rulev[58] = new Rule(58, true, false, 2, "58: regl -> sta", ImList.list(ImList.list("la",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{26}, new String[]{"*reg-tmp-I32*", null});
    rulev[59] = new Rule(59, true, false, 3, "59: regh -> sta", ImList.list(ImList.list("la",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{26}, new String[]{"*reg-tmp-I32*", null});
    rulev[41] = new Rule(41, true, false, 29, "41: fun -> addr", null, null, null, 0, false, false, new int[]{27}, new String[]{null, null});
    rulev[40] = new Rule(40, true, false, 29, "40: fun -> asmcon", null, null, null, 0, false, false, new int[]{28}, new String[]{null, null});
    rulev[21] = new Rule(21, false, false, 17, "21: con5 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[22] = new Rule(22, false, false, 18, "22: con16u -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[23] = new Rule(23, false, false, 19, "23: con16sminus -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[24] = new Rule(24, false, false, 20, "24: con32u -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[25] = new Rule(25, false, false, 21, "25: con32s -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[63] = new Rule(63, false, true, 32, "63: _1 -> (INTCONST _ 0)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[29] = new Rule(29, false, false, 24, "29: confd -> (FLOATCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[30] = new Rule(30, false, false, 25, "30: _sta -> (STATIC _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 7, "1: _xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 8, "3: _xregh -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 9, "5: _xregl -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[7] = new Rule(7, false, false, 10, "7: _xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[9] = new Rule(9, false, false, 11, "9: _xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 7, "2: _xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[4] = new Rule(4, false, false, 8, "4: _xregh -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 9, "6: _xregl -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 10, "8: _xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[64] = new Rule(64, false, true, 33, "64: _2 -> (SUBREG F32 xregl _1)", null, null, null, 0, false, false, new int[]{14,32}, null);
    rulev[66] = new Rule(66, false, true, 34, "66: _3 -> (SUBREG F32 regl _1)", null, null, null, 0, false, false, new int[]{2,32}, null);
    rulev[10] = new Rule(10, false, false, 11, "10: _xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[131] = new Rule(131, false, false, 42, "131: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[242] = new Rule(242, false, false, 2, "242: regl -> (NEG I32 regl)", ImList.list(ImList.list("sub",ImList.list("_reg","$0"),ImList.list("_reg","%0"),"$1")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-tmp-I32*", "*reg-I32*"});
    rulev[252] = new Rule(252, false, false, 6, "252: regf -> (NEG F32 regf)", ImList.list(ImList.list("neg.s",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F32*", "*reg-F32*"});
    rulev[257] = new Rule(257, false, false, 5, "257: regd -> (NEG F64 regd)", ImList.list(ImList.list("neg.d",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F64*", "*reg-F64*"});
    rulev[34] = new Rule(34, false, false, 27, "34: addr -> (ADD I32 regl con32)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{2,22}, new String[]{null, "*reg-I32*", null});
    rulev[35] = new Rule(35, false, false, 27, "35: addr -> (ADD I32 regl conofs)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{2,23}, new String[]{null, "*reg-I32*", null});
    rulev[38] = new Rule(38, false, false, 28, "38: asmcon -> (ADD I32 asmcon con32)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{28,22}, new String[]{null, null, null});
    rulev[228] = new Rule(228, false, false, 2, "228: regl -> (ADD I32 regl rc)", ImList.list(ImList.list("addu",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[233] = new Rule(233, false, false, 2, "233: regl -> (ADD I32 regl rc)", ImList.list(ImList.list("add",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[258] = new Rule(258, false, false, 2, "258: regl -> (ADD I32 regl rc)", ImList.list(ImList.list("addu",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[248] = new Rule(248, false, false, 6, "248: regf -> (ADD F32 regf regf)", ImList.list(ImList.list("add.s",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[253] = new Rule(253, false, false, 5, "253: regd -> (ADD F64 regd regd)", ImList.list(ImList.list("add.d",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[39] = new Rule(39, false, false, 28, "39: asmcon -> (SUB I32 asmcon con32)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{28,22}, new String[]{null, null, null});
    rulev[229] = new Rule(229, false, false, 2, "229: regl -> (SUB I32 regl rc)", ImList.list(ImList.list("subu",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[234] = new Rule(234, false, false, 2, "234: regl -> (SUB I32 regl rc)", ImList.list(ImList.list("sub",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[259] = new Rule(259, false, false, 2, "259: regl -> (SUB I32 regl rc)", ImList.list(ImList.list("subu",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[249] = new Rule(249, false, false, 6, "249: regf -> (SUB F32 regf regf)", ImList.list(ImList.list("sub.s",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[254] = new Rule(254, false, false, 5, "254: regd -> (SUB F64 regd regd)", ImList.list(ImList.list("sub.d",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[230] = new Rule(230, false, false, 2, "230: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("mul",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[235] = new Rule(235, false, false, 2, "235: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("addu",ImList.list("_reg","%2"),ImList.list("_reg","%0"),"$1"),ImList.list("addu",ImList.list("_reg","%3"),ImList.list("_reg","%0"),"$2"),ImList.list("jal","__smuls"),ImList.list("nop"),ImList.list("addu",ImList.list("_reg","$0"),ImList.list("_reg","%0"),ImList.list("_reg","%2"))), null, ImList.list(ImList.list("REG","I32","%2"),ImList.list("REG","I32","%3")), 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[260] = new Rule(260, false, false, 2, "260: regl -> (MUL I32 regl rc)", ImList.list(ImList.list("mul",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[250] = new Rule(250, false, false, 6, "250: regf -> (MUL F32 regf regf)", ImList.list(ImList.list("mul.s",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[255] = new Rule(255, false, false, 5, "255: regd -> (MUL F64 regd regd)", ImList.list(ImList.list("mul.d",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[231] = new Rule(231, false, false, 2, "231: regl -> (DIVS I32 regl rc)", ImList.list(ImList.list("div",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[236] = new Rule(236, false, false, 2, "236: regl -> (DIVS I32 regl regl)", ImList.list(ImList.list("addu",ImList.list("_reg","%2"),ImList.list("_reg","%0"),"$1"),ImList.list("addu",ImList.list("_reg","%3"),ImList.list("_reg","%0"),"$2"),ImList.list("jal","__sdivs"),ImList.list("nop"),ImList.list("addu",ImList.list("_reg","$0"),ImList.list("_reg","%0"),ImList.list("_reg","%2"))), null, ImList.list(ImList.list("REG","I32","%2"),ImList.list("REG","I32","%3")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[261] = new Rule(261, false, false, 2, "261: regl -> (DIVS I32 regl rc)", ImList.list(ImList.list("div",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[251] = new Rule(251, false, false, 6, "251: regf -> (DIVS F32 regf regf)", ImList.list(ImList.list("div.s",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{6,6}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[256] = new Rule(256, false, false, 5, "256: regd -> (DIVS F64 regd regd)", ImList.list(ImList.list("div.d",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[232] = new Rule(232, false, false, 2, "232: regl -> (DIVU I32 regl rc)", ImList.list(ImList.list("divu",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[237] = new Rule(237, false, false, 2, "237: regl -> (DIVU I32 regl regl)", ImList.list(ImList.list("addu",ImList.list("_reg","%2"),ImList.list("_reg","%0"),"$1"),ImList.list("addu",ImList.list("_reg","%3"),ImList.list("_reg","%0"),"$2"),ImList.list("jal","__udivs"),ImList.list("nop"),ImList.list("addu",ImList.list("_reg","$0"),ImList.list("_reg","%0"),ImList.list("_reg","%2"))), null, ImList.list(ImList.list("REG","I32","%2"),ImList.list("REG","I32","%3")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[262] = new Rule(262, false, false, 2, "262: regl -> (DIVU I32 regl rc)", ImList.list(ImList.list("divu",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[238] = new Rule(238, false, false, 2, "238: regl -> (MODS I32 regl regl)", ImList.list(ImList.list("addu",ImList.list("_reg","%2"),ImList.list("_reg","%0"),"$1"),ImList.list("addu",ImList.list("_reg","%3"),ImList.list("_reg","%0"),"$2"),ImList.list("jal","__sdivs"),ImList.list("nop"),ImList.list("addu",ImList.list("_reg","$0"),ImList.list("_reg","%0"),ImList.list("_reg","%3"))), null, ImList.list(ImList.list("REG","I32","%2"),ImList.list("REG","I32","%3")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[240] = new Rule(240, false, false, 2, "240: regl -> (MODS I32 regl regl)", new ImList(ImList.list("div",ImList.list("_reg","%0"),"$1","$2"), new ImList(ImList.list("mfhi",ImList.list("_reg","$0")), new ImList(ImList.list(".set","noreorder"), ImList.list(ImList.list("bne","$2",ImList.list("_reg","%0"),"1f"),ImList.list("nop"),ImList.list("break","7"),ImList.list("deflabel","1"),ImList.list(".set","reorder"))))), null, ImList.list(ImList.list("REG","I32","%8"),ImList.list("REG","I32","%9")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[263] = new Rule(263, false, false, 2, "263: regl -> (MODS I32 regl regl)", new ImList(ImList.list("div",ImList.list("_reg","%0"),"$1","$2"), new ImList(ImList.list("mfhi",ImList.list("_reg","$0")), new ImList(ImList.list(".set","noreorder"), ImList.list(ImList.list("bne","$2",ImList.list("_reg","%0"),"1f"),ImList.list("nop"),ImList.list("break","7"),ImList.list("deflabel","1"),ImList.list(".set","reorder"))))), null, ImList.list(ImList.list("REG","I32","%8"),ImList.list("REG","I32","%9")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[239] = new Rule(239, false, false, 2, "239: regl -> (MODU I32 regl regl)", ImList.list(ImList.list("addu",ImList.list("_reg","%2"),ImList.list("_reg","%0"),"$1"),ImList.list("addu",ImList.list("_reg","%3"),ImList.list("_reg","%0"),"$2"),ImList.list("jal","__udivs"),ImList.list("nop"),ImList.list("addu",ImList.list("_reg","$0"),ImList.list("_reg","%0"),ImList.list("_reg","%3"))), null, ImList.list(ImList.list("REG","I32","%2"),ImList.list("REG","I32","%3")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[241] = new Rule(241, false, false, 2, "241: regl -> (MODU I32 regl regl)", new ImList(ImList.list("divu",ImList.list("_reg","%0"),"$1","$2"), new ImList(ImList.list("mfhi",ImList.list("_reg","$0")), new ImList(ImList.list(".set","noreorder"), ImList.list(ImList.list("bne","$2",ImList.list("_reg","%0"),"1f"),ImList.list("nop"),ImList.list("break","7"),ImList.list("deflabel","1"),ImList.list(".set","reorder"))))), null, ImList.list(ImList.list("REG","I32","%8"),ImList.list("REG","I32","%9")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[264] = new Rule(264, false, false, 2, "264: regl -> (MODU I32 regl regl)", new ImList(ImList.list("divu",ImList.list("_reg","%0"),"$1","$2"), new ImList(ImList.list("mfhi",ImList.list("_reg","$0")), new ImList(ImList.list(".set","noreorder"), ImList.list(ImList.list("bne","$2",ImList.list("_reg","%0"),"1f"),ImList.list("nop"),ImList.list("break","7"),ImList.list("deflabel","1"),ImList.list(".set","reorder"))))), null, ImList.list(ImList.list("REG","I32","%8"),ImList.list("REG","I32","%9")), 0, false, false, new int[]{2,2}, new String[]{"*reg-tmp0-I32*", "*reg-I32*", "*reg-tmp1-I32*"});
    rulev[268] = new Rule(268, false, false, 3, "268: regh -> (CONVSX I16 regb)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","8"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"8")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[100] = new Rule(100, false, false, 2, "100: regl -> (CONVSX I32 _5)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[101] = new Rule(101, false, false, 2, "101: regl -> (CONVSX I32 _6)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[102] = new Rule(102, false, false, 2, "102: regl -> (CONVSX I32 _5)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[103] = new Rule(103, false, false, 2, "103: regl -> (CONVSX I32 _6)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[118] = new Rule(118, false, false, 2, "118: regl -> (CONVSX I32 _5)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
  }
  static private void rrinit100() {
    rulev[119] = new Rule(119, false, false, 2, "119: regl -> (CONVSX I32 _6)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[266] = new Rule(266, false, false, 2, "266: regl -> (CONVSX I32 regh)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","16"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[267] = new Rule(267, false, false, 2, "267: regl -> (CONVSX I32 regb)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","24"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[271] = new Rule(271, false, false, 3, "271: regh -> (CONVZX I16 regb)", ImList.list(ImList.list("and",ImList.list("_reg","$0"),"$1","255")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[104] = new Rule(104, false, false, 2, "104: regl -> (CONVZX I32 _5)", ImList.list(ImList.list("lhu",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[105] = new Rule(105, false, false, 2, "105: regl -> (CONVZX I32 _6)", ImList.list(ImList.list("lbu",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[106] = new Rule(106, false, false, 2, "106: regl -> (CONVZX I32 _5)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop"),ImList.list("andi",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"65535")), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[107] = new Rule(107, false, false, 2, "107: regl -> (CONVZX I32 _6)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop"),ImList.list("andi",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"255")), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[120] = new Rule(120, false, false, 2, "120: regl -> (CONVZX I32 _5)", ImList.list(ImList.list("lhu",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{36}, new String[]{"*reg-I32*", null});
    rulev[121] = new Rule(121, false, false, 2, "121: regl -> (CONVZX I32 _6)", ImList.list(ImList.list("lbu",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-I32*", null});
    rulev[269] = new Rule(269, false, false, 2, "269: regl -> (CONVZX I32 regh)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","16"),ImList.list("srl",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[270] = new Rule(270, false, false, 2, "270: regl -> (CONVZX I32 regb)", ImList.list(ImList.list("and",ImList.list("_reg","$0"),"$1","255")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[129] = new Rule(129, false, true, 41, "129: _10 -> (CONVIT I8 regl)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[273] = new Rule(273, false, false, 4, "273: regb -> (CONVIT I8 regl)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","24"),ImList.list("srl",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-I32*"});
    rulev[274] = new Rule(274, false, false, 4, "274: regb -> (CONVIT I8 regh)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","24"),ImList.list("srl",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I8*", "*reg-I16*"});
    rulev[127] = new Rule(127, false, true, 40, "127: _9 -> (CONVIT I16 regl)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[272] = new Rule(272, false, false, 3, "272: regh -> (CONVIT I16 regl)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","16"),ImList.list("srl",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[275] = new Rule(275, false, false, 5, "275: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("cvt.d.s",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F64*", "*reg-F32*"});
    rulev[285] = new Rule(285, false, false, 5, "285: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("cvt.d.s",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{6}, new String[]{"*reg-F64*", "*reg-F32*"});
    rulev[276] = new Rule(276, false, false, 6, "276: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("cvt.s.d",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F64*"});
    rulev[286] = new Rule(286, false, false, 6, "286: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("cvt.s.d",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F64*"});
    rulev[279] = new Rule(279, false, false, 4, "279: regb -> (CONVFS I8 regf)", ImList.list(ImList.list("cvt.w.s",ImList.list("_reg","%f4"),"$1"),ImList.list("mfc1",ImList.list("_reg","$0"),ImList.list("_reg","%f4")),ImList.list("sll",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24")), null, ImList.list(ImList.list("REG","F32","%f4")), 0, false, false, new int[]{6}, new String[]{"*reg-I8*", "*reg-F32*"});
    rulev[282] = new Rule(282, false, false, 4, "282: regb -> (CONVFS I8 regd)", ImList.list(ImList.list("cvt.w.d",ImList.list("_reg","%f4"),"$1"),ImList.list("mtc1",ImList.list("_reg","$0"),ImList.list("_reg","%f4")),ImList.list("sll",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"24")), null, ImList.list(ImList.list("REG","F64","%f4")), 0, false, false, new int[]{5}, new String[]{"*reg-I8*", "*reg-F64*"});
    rulev[278] = new Rule(278, false, false, 3, "278: regh -> (CONVFS I16 regf)", ImList.list(ImList.list("cvt.w.s",ImList.list("_reg","%f4"),"$1"),ImList.list("mfc1",ImList.list("_reg","$0"),ImList.list("_reg","%f4")),ImList.list("sll",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16")), null, ImList.list(ImList.list("REG","F32","%f4")), 0, false, false, new int[]{6}, new String[]{"*reg-I16*", "*reg-F32*"});
    rulev[281] = new Rule(281, false, false, 3, "281: regh -> (CONVFS I16 regd)", ImList.list(ImList.list("cvt.w.d",ImList.list("_reg","%f4"),"$1"),ImList.list("mtc1",ImList.list("_reg","$0"),ImList.list("_reg","%f4")),ImList.list("sll",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16"),ImList.list("sra",ImList.list("_reg","$0"),ImList.list("_reg","$0"),"16")), null, ImList.list(ImList.list("REG","F64","%f4")), 0, false, false, new int[]{5}, new String[]{"*reg-I16*", "*reg-F64*"});
    rulev[277] = new Rule(277, false, false, 2, "277: regl -> (CONVFS I32 regf)", ImList.list(ImList.list("cvt.w.s",ImList.list("_reg","%f4"),"$1"),ImList.list("mfc1",ImList.list("_reg","$0"),ImList.list("_reg","%f4"))), null, ImList.list(ImList.list("REG","F32","%f4")), 0, false, false, new int[]{6}, new String[]{"*reg-I32*", "*reg-F32*"});
    rulev[280] = new Rule(280, false, false, 2, "280: regl -> (CONVFS I32 regd)", ImList.list(ImList.list("cvt.w.d",ImList.list("_reg","%f4"),"$1"),ImList.list("mfc1",ImList.list("_reg","$0"),ImList.list("_reg","%f4"))), null, ImList.list(ImList.list("REG","F64","%f4")), 0, false, false, new int[]{5}, new String[]{"*reg-I32*", "*reg-F64*"});
    rulev[283] = new Rule(283, false, false, 6, "283: regf -> (CONVSF F32 regl)", ImList.list(ImList.list("mtc1","$1",ImList.list("_reg","$0")),ImList.list("cvt.s.w",ImList.list("_reg","$0"),ImList.list("_reg","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-F32*", "*reg-I32*"});
    rulev[284] = new Rule(284, false, false, 5, "284: regd -> (CONVSF F64 regl)", ImList.list(ImList.list("mtc1","$1",ImList.list("_reg","$0")),ImList.list("cvt.d.w",ImList.list("_reg","$0"),ImList.list("_reg","$0"))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-F64*", "*reg-I32*"});
    rulev[225] = new Rule(225, false, false, 2, "225: regl -> (BAND I32 regl rc)", ImList.list(ImList.list("and",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[226] = new Rule(226, false, false, 2, "226: regl -> (BOR I32 regl rc)", ImList.list(ImList.list("or",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[227] = new Rule(227, false, false, 2, "227: regl -> (BXOR I32 regl rc)", ImList.list(ImList.list("xor",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,30}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[243] = new Rule(243, false, false, 2, "243: regl -> (BNOT I32 regl)", ImList.list(ImList.list("not",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-tmp-I32*", "*reg-I32*"});
    rulev[244] = new Rule(244, false, false, 2, "244: regl -> (BNOT I32 regl)", ImList.list(ImList.list("li",ImList.list("_reg","%8"),ImList.list("_imm","-1")),ImList.list("xor",ImList.list("_reg","$0"),"$1",ImList.list("_reg","%8"))), null, ImList.list(ImList.list("REG","I32","%8")), 0, false, false, new int[]{2}, new String[]{"*reg-tmp-I32*", "*reg-I32*"});
    rulev[265] = new Rule(265, false, false, 2, "265: regl -> (BNOT I32 regl)", ImList.list(ImList.list("not",ImList.list("_reg","$0"),"$1")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-tmp-I32*", "*reg-I32*"});
    rulev[247] = new Rule(247, false, false, 2, "247: regl -> (LSHS I32 regl rcs)", ImList.list(ImList.list("sll",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,31}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[245] = new Rule(245, false, false, 2, "245: regl -> (RSHS I32 regl rcs)", ImList.list(ImList.list("sra",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,31}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[246] = new Rule(246, false, false, 2, "246: regl -> (RSHU I32 regl rcs)", ImList.list(ImList.list("srl",ImList.list("_reg","$0"),"$1","$2")), null, null, 0, false, false, new int[]{2,31}, new String[]{"*reg-tmp-I32*", "*reg-I32*", null});
    rulev[134] = new Rule(134, false, true, 43, "134: _11 -> (TSTEQ I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[164] = new Rule(164, false, true, 53, "164: _21 -> (TSTEQ I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[172] = new Rule(172, false, true, 57, "172: _25 -> (TSTEQ I32 regf regf)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[136] = new Rule(136, false, true, 44, "136: _12 -> (TSTNE I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[166] = new Rule(166, false, true, 54, "166: _22 -> (TSTNE I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[174] = new Rule(174, false, true, 58, "174: _26 -> (TSTNE I32 regf regf)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[138] = new Rule(138, false, true, 45, "138: _13 -> (TSTLTS I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[168] = new Rule(168, false, true, 55, "168: _23 -> (TSTLTS I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[176] = new Rule(176, false, true, 59, "176: _27 -> (TSTLTS I32 regf regf)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[140] = new Rule(140, false, true, 46, "140: _14 -> (TSTLES I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[170] = new Rule(170, false, true, 56, "170: _24 -> (TSTLES I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[178] = new Rule(178, false, true, 60, "178: _28 -> (TSTLES I32 regf regf)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[142] = new Rule(142, false, true, 47, "142: _15 -> (TSTGTS I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[188] = new Rule(188, false, true, 61, "188: _29 -> (TSTGTS I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[192] = new Rule(192, false, true, 63, "192: _31 -> (TSTGTS I32 regf regf)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[144] = new Rule(144, false, true, 48, "144: _16 -> (TSTGES I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[190] = new Rule(190, false, true, 62, "190: _30 -> (TSTGES I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[194] = new Rule(194, false, true, 64, "194: _32 -> (TSTGES I32 regf regf)", null, null, null, 0, false, false, new int[]{6,6}, null);
    rulev[146] = new Rule(146, false, true, 49, "146: _17 -> (TSTLTU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[148] = new Rule(148, false, true, 50, "148: _18 -> (TSTLEU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[211] = new Rule(211, false, true, 66, "211: _34 -> (TSTLEU I32 regl regl)", null, null, null, 0, false, false, new int[]{2,2}, null);
    rulev[150] = new Rule(150, false, true, 51, "150: _19 -> (TSTGTU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[209] = new Rule(209, false, true, 65, "209: _33 -> (TSTGTU I32 regl regl)", null, null, null, 0, false, false, new int[]{2,2}, null);
    rulev[152] = new Rule(152, false, true, 52, "152: _20 -> (TSTGEU I32 regl rc)", null, null, null, 0, false, false, new int[]{2,30}, null);
    rulev[79] = new Rule(79, false, true, 37, "79: _6 -> (MEM I8 addr)", null, null, null, 0, false, false, new int[]{27}, null);
    rulev[92] = new Rule(92, false, false, 4, "92: regb -> (MEM I8 addr)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I8*", null});
    rulev[97] = new Rule(97, false, false, 4, "97: regb -> (MEM I8 addr)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I8*", null});
    rulev[115] = new Rule(115, false, false, 4, "115: regb -> (MEM I8 addr)", ImList.list(ImList.list("lb",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I8*", null});
    rulev[77] = new Rule(77, false, true, 36, "77: _5 -> (MEM I16 addr)", null, null, null, 0, false, false, new int[]{27}, null);
    rulev[91] = new Rule(91, false, false, 3, "91: regh -> (MEM I16 addr)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I16*", null});
    rulev[96] = new Rule(96, false, false, 3, "96: regh -> (MEM I16 addr)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I16*", null});
    rulev[114] = new Rule(114, false, false, 3, "114: regh -> (MEM I16 addr)", ImList.list(ImList.list("lh",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I16*", null});
    rulev[75] = new Rule(75, false, true, 35, "75: _4 -> (MEM I32 addr)", null, null, null, 0, false, false, new int[]{27}, null);
    rulev[90] = new Rule(90, false, false, 2, "90: regl -> (MEM I32 addr)", ImList.list(ImList.list("lw",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I32*", null});
    rulev[95] = new Rule(95, false, false, 2, "95: regl -> (MEM I32 addr)", ImList.list(ImList.list("lw",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I32*", null});
    rulev[113] = new Rule(113, false, false, 2, "113: regl -> (MEM I32 addr)", ImList.list(ImList.list("lw",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I32*", null});
    rulev[83] = new Rule(83, false, true, 39, "83: _8 -> (MEM F32 addr)", null, null, null, 0, false, false, new int[]{27}, null);
    rulev[94] = new Rule(94, false, false, 6, "94: regf -> (MEM F32 addr)", ImList.list(ImList.list("l.s",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-F32*", null});
    rulev[99] = new Rule(99, false, false, 6, "99: regf -> (MEM F32 addr)", ImList.list(ImList.list("l.s",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-F32*", null});
    rulev[117] = new Rule(117, false, false, 6, "117: regf -> (MEM F32 addr)", ImList.list(ImList.list("l.s",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-F32*", null});
    rulev[81] = new Rule(81, false, true, 38, "81: _7 -> (MEM F64 addr)", null, null, null, 0, false, false, new int[]{27}, null);
    rulev[93] = new Rule(93, false, false, 5, "93: regd -> (MEM F64 addr)", ImList.list(ImList.list("l.d",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-F64*", null});
    rulev[98] = new Rule(98, false, false, 5, "98: regd -> (MEM F64 addr)", ImList.list(ImList.list("l.d",ImList.list("_reg","$0"),ImList.list("mem","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-F64*", null});
    rulev[116] = new Rule(116, false, false, 5, "116: regd -> (MEM F64 addr)", ImList.list(ImList.list("l.d",ImList.list("_reg","$0"),ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-F64*", null});
    rulev[62] = new Rule(62, false, false, 1, "62: void -> (SET I8 xregb regb)", ImList.list(ImList.list("move","$1","$2")), null, null, 0, false, false, new int[]{12,4}, new String[]{null, null, "*reg-I8*"});
    rulev[124] = new Rule(124, false, false, 1, "124: void -> (SET I8 _6 regb)", ImList.list(ImList.list("sb","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{37,4}, new String[]{null, null, "*reg-I8*"});
    rulev[130] = new Rule(130, false, false, 1, "130: void -> (SET I8 _6 _10)", ImList.list(ImList.list("sb","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{37,41}, new String[]{null, null, "*reg-I32*"});
    rulev[61] = new Rule(61, false, false, 1, "61: void -> (SET I16 xregh regh)", ImList.list(ImList.list("move","$1","$2")), null, null, 0, false, false, new int[]{13,3}, new String[]{null, null, "*reg-I16*"});
    rulev[123] = new Rule(123, false, false, 1, "123: void -> (SET I16 _5 regh)", ImList.list(ImList.list("sh","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{36,3}, new String[]{null, null, "*reg-I16*"});
    rulev[128] = new Rule(128, false, false, 1, "128: void -> (SET I16 _5 _9)", ImList.list(ImList.list("sh","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{36,40}, new String[]{null, null, "*reg-I32*"});
    rulev[60] = new Rule(60, false, false, 1, "60: void -> (SET I32 xregl regl)", ImList.list(ImList.list("move","$1","$2")), null, null, 0, false, false, new int[]{14,2}, new String[]{null, null, "*reg-I32*"});
    rulev[70] = new Rule(70, false, false, 1, "70: void -> (SET I32 xregl confd)", ImList.list(ImList.list("li.d","$1","$2")), null, null, 0, false, false, new int[]{14,24}, new String[]{null, null, null});
    rulev[71] = new Rule(71, false, false, 1, "71: void -> (SET I32 xregl regd)", ImList.list(ImList.list("mfc1","$1","$2"),ImList.list("mfc1",ImList.list("_oddreg","$1"),ImList.list("_oddreg","$2"))), null, null, 0, false, false, new int[]{14,5}, new String[]{null, null, "*reg-F64*"});
    rulev[72] = new Rule(72, false, false, 1, "72: void -> (SET I32 xregl regd)", ImList.list(ImList.list("mfc1","$1",ImList.list("_oddreg","$2")),ImList.list("mfc1",ImList.list("_oddreg","$1"),"$2")), null, null, 0, false, false, new int[]{14,5}, new String[]{null, null, "*reg-F64*"});
    rulev[122] = new Rule(122, false, false, 1, "122: void -> (SET I32 _4 regl)", ImList.list(ImList.list("sw","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{35,2}, new String[]{null, null, "*reg-I32*"});
    rulev[65] = new Rule(65, false, false, 1, "65: void -> (SET F32 _2 regf)", ImList.list(ImList.list("mfc1","$1","$2")), null, null, 0, false, false, new int[]{33,6}, new String[]{null, null, "*reg-F32*"});
    rulev[67] = new Rule(67, false, false, 1, "67: void -> (SET F32 xregf _3)", ImList.list(ImList.list("mtc1","$2","$1")), null, null, 0, false, false, new int[]{15,34}, new String[]{null, null, "*reg-I32*"});
    rulev[69] = new Rule(69, false, false, 1, "69: void -> (SET F32 xregf regf)", ImList.list(ImList.list("mov.s","$1","$2")), null, null, 0, false, false, new int[]{15,6}, new String[]{null, null, "*reg-F32*"});
    rulev[126] = new Rule(126, false, false, 1, "126: void -> (SET F32 _8 regf)", ImList.list(ImList.list("s.s","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{39,6}, new String[]{null, null, "*reg-F32*"});
    rulev[68] = new Rule(68, false, false, 1, "68: void -> (SET F64 xregd regd)", ImList.list(ImList.list("mov.d","$1","$2")), null, null, 0, false, false, new int[]{16,5}, new String[]{null, null, "*reg-F64*"});
    rulev[73] = new Rule(73, false, false, 1, "73: void -> (SET F64 xregd regl)", ImList.list(ImList.list("mtc1","$2","$1"),ImList.list("mtc1",ImList.list("_oddreg","$2"),ImList.list("_oddreg","$1"))), null, null, 0, false, false, new int[]{16,2}, new String[]{null, null, "*reg-I32*"});
    rulev[74] = new Rule(74, false, false, 1, "74: void -> (SET F64 xregd regl)", ImList.list(ImList.list("mtc1","$2",ImList.list("_oddreg","$1")),ImList.list("mtc1",ImList.list("_oddreg","$2"),"$1"),ImList.list("this_rule_not_checked_yet")), null, null, 0, false, false, new int[]{16,2}, new String[]{null, null, "*reg-I32*"});
  }
  static private void rrinit200() {
    rulev[125] = new Rule(125, false, false, 1, "125: void -> (SET F64 _7 regd)", ImList.list(ImList.list("s.d","$2",ImList.list("mem","$1"))), null, null, 0, false, false, new int[]{38,5}, new String[]{null, null, "*reg-F64*"});
    rulev[132] = new Rule(132, false, false, 1, "132: void -> (JUMP _ label)", ImList.list(ImList.list("j",ImList.list("jumplabel","$1"))), null, null, 0, false, false, new int[]{42}, new String[]{null, null});
    rulev[133] = new Rule(133, false, false, 1, "133: void -> (JUMP _ label)", ImList.list(ImList.list("j",ImList.list("jumplabel","$1")),ImList.list("nop")), null, null, 0, false, false, new int[]{42}, new String[]{null, null});
    rulev[200] = new Rule(200, false, false, 1, "200: void -> (JUMP _ label)", ImList.list(ImList.list("j",ImList.list("jumplabel","$1"))), null, null, 0, false, false, new int[]{42}, new String[]{null, null});
    rulev[135] = new Rule(135, false, false, 1, "135: void -> (JUMPC _ _11 label label)", ImList.list(ImList.list("beq","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{43,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[137] = new Rule(137, false, false, 1, "137: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("bne","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{44,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[139] = new Rule(139, false, false, 1, "139: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("blt","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{45,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[141] = new Rule(141, false, false, 1, "141: void -> (JUMPC _ _14 label label)", ImList.list(ImList.list("ble","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{46,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[143] = new Rule(143, false, false, 1, "143: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("bgt","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{47,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[145] = new Rule(145, false, false, 1, "145: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("bge","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{48,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[147] = new Rule(147, false, false, 1, "147: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("bltu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{49,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[149] = new Rule(149, false, false, 1, "149: void -> (JUMPC _ _18 label label)", ImList.list(ImList.list("bleu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{50,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[151] = new Rule(151, false, false, 1, "151: void -> (JUMPC _ _19 label label)", ImList.list(ImList.list("bgtu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{51,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[153] = new Rule(153, false, false, 1, "153: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("bgeu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{52,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[154] = new Rule(154, false, false, 1, "154: void -> (JUMPC _ _11 label label)", ImList.list(ImList.list("beq","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{43,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[155] = new Rule(155, false, false, 1, "155: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("bne","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{44,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[156] = new Rule(156, false, false, 1, "156: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("blt","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{45,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[157] = new Rule(157, false, false, 1, "157: void -> (JUMPC _ _14 label label)", ImList.list(ImList.list("ble","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{46,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[158] = new Rule(158, false, false, 1, "158: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("bgt","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{47,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[159] = new Rule(159, false, false, 1, "159: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("bge","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{48,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[160] = new Rule(160, false, false, 1, "160: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("bltu","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{49,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[161] = new Rule(161, false, false, 1, "161: void -> (JUMPC _ _18 label label)", ImList.list(ImList.list("bleu","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{50,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[162] = new Rule(162, false, false, 1, "162: void -> (JUMPC _ _19 label label)", ImList.list(ImList.list("bgtu","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{51,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[163] = new Rule(163, false, false, 1, "163: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("bgeu","$1","$2",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{52,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[165] = new Rule(165, false, false, 1, "165: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("c.eq.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{53,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[167] = new Rule(167, false, false, 1, "167: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("c.eq.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{54,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[169] = new Rule(169, false, false, 1, "169: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("c.lt.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{55,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[171] = new Rule(171, false, false, 1, "171: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("c.le.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{56,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[173] = new Rule(173, false, false, 1, "173: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("c.eq.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{57,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[175] = new Rule(175, false, false, 1, "175: void -> (JUMPC _ _26 label label)", ImList.list(ImList.list("c.eq.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{58,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[177] = new Rule(177, false, false, 1, "177: void -> (JUMPC _ _27 label label)", ImList.list(ImList.list("c.lt.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{59,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[179] = new Rule(179, false, false, 1, "179: void -> (JUMPC _ _28 label label)", ImList.list(ImList.list("c.le.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{60,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[180] = new Rule(180, false, false, 1, "180: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("c.eq.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{53,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[181] = new Rule(181, false, false, 1, "181: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("c.eq.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{54,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[182] = new Rule(182, false, false, 1, "182: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("c.lt.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{55,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[183] = new Rule(183, false, false, 1, "183: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("c.le.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{56,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[184] = new Rule(184, false, false, 1, "184: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("c.eq.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{57,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[185] = new Rule(185, false, false, 1, "185: void -> (JUMPC _ _26 label label)", ImList.list(ImList.list("c.eq.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{58,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[186] = new Rule(186, false, false, 1, "186: void -> (JUMPC _ _27 label label)", ImList.list(ImList.list("c.lt.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{59,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[187] = new Rule(187, false, false, 1, "187: void -> (JUMPC _ _28 label label)", ImList.list(ImList.list("c.le.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{60,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[189] = new Rule(189, false, false, 1, "189: void -> (JUMPC _ _29 label label)", ImList.list(ImList.list("c.lt.d","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{61,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[191] = new Rule(191, false, false, 1, "191: void -> (JUMPC _ _30 label label)", ImList.list(ImList.list("c.le.d","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{62,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[193] = new Rule(193, false, false, 1, "193: void -> (JUMPC _ _31 label label)", ImList.list(ImList.list("c.lt.s","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{63,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[195] = new Rule(195, false, false, 1, "195: void -> (JUMPC _ _32 label label)", ImList.list(ImList.list("c.le.s","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{64,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[196] = new Rule(196, false, false, 1, "196: void -> (JUMPC _ _29 label label)", ImList.list(ImList.list("c.lt.d","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{61,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[197] = new Rule(197, false, false, 1, "197: void -> (JUMPC _ _30 label label)", ImList.list(ImList.list("c.le.d","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{62,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[198] = new Rule(198, false, false, 1, "198: void -> (JUMPC _ _31 label label)", ImList.list(ImList.list("c.lt.s","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{63,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[199] = new Rule(199, false, false, 1, "199: void -> (JUMPC _ _32 label label)", ImList.list(ImList.list("c.le.s","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3")),ImList.list("nop")), null, null, 0, false, false, new int[]{64,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[201] = new Rule(201, false, false, 1, "201: void -> (JUMPC _ _11 label label)", ImList.list(ImList.list("beq","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{43,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[202] = new Rule(202, false, false, 1, "202: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("bne","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{44,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[203] = new Rule(203, false, false, 1, "203: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("bltu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{49,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[204] = new Rule(204, false, false, 1, "204: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("blt","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{45,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[205] = new Rule(205, false, false, 1, "205: void -> (JUMPC _ _14 label label)", ImList.list(ImList.list("ble","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{46,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[206] = new Rule(206, false, false, 1, "206: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("bgt","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{47,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[207] = new Rule(207, false, false, 1, "207: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("bge","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{48,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[208] = new Rule(208, false, false, 1, "208: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("bgeu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{52,42,42}, new String[]{null, "*reg-I32*", null, null, null});
    rulev[210] = new Rule(210, false, false, 1, "210: void -> (JUMPC _ _33 label label)", ImList.list(ImList.list("bgtu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{65,42,42}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[212] = new Rule(212, false, false, 1, "212: void -> (JUMPC _ _34 label label)", ImList.list(ImList.list("bleu","$1","$2",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{66,42,42}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[213] = new Rule(213, false, false, 1, "213: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("c.eq.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{53,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[214] = new Rule(214, false, false, 1, "214: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("c.lt.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{55,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[215] = new Rule(215, false, false, 1, "215: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("c.le.d","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{56,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[216] = new Rule(216, false, false, 1, "216: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("c.eq.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{57,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[217] = new Rule(217, false, false, 1, "217: void -> (JUMPC _ _27 label label)", ImList.list(ImList.list("c.lt.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{59,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[218] = new Rule(218, false, false, 1, "218: void -> (JUMPC _ _28 label label)", ImList.list(ImList.list("c.le.s","$1","$2"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{60,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[219] = new Rule(219, false, false, 1, "219: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("c.eq.d","$1","$2"),ImList.list("nop"),ImList.list("bc1f",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{54,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[220] = new Rule(220, false, false, 1, "220: void -> (JUMPC _ _26 label label)", ImList.list(ImList.list("c.eq.s","$1","$2"),ImList.list("nop"),ImList.list("bc1f",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{58,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[221] = new Rule(221, false, false, 1, "221: void -> (JUMPC _ _29 label label)", ImList.list(ImList.list("c.lt.d","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{61,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[222] = new Rule(222, false, false, 1, "222: void -> (JUMPC _ _30 label label)", ImList.list(ImList.list("c.le.d","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{62,42,42}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[223] = new Rule(223, false, false, 1, "223: void -> (JUMPC _ _31 label label)", ImList.list(ImList.list("c.lt.s","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{63,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[224] = new Rule(224, false, false, 1, "224: void -> (JUMPC _ _32 label label)", ImList.list(ImList.list("c.le.s","$2","$1"),ImList.list("nop"),ImList.list("bc1t",ImList.list("jumplabel","$3"))), null, null, 0, false, false, new int[]{64,42,42}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[287] = new Rule(287, false, false, 1, "287: void -> (CALL _ fun)", ImList.list(ImList.list("jal","$1")), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[288] = new Rule(288, false, false, 1, "288: void -> (CALL _ fun)", ImList.list(ImList.list("jal","$1"),ImList.list("nop")), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[289] = new Rule(289, false, false, 1, "289: void -> (CALL _ fun)", ImList.list(ImList.list("move",ImList.list("_reg","%25"),"$1"),ImList.list("jal",ImList.list("_reg","%31"),ImList.list("_reg","%25"))), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[290] = new Rule(290, false, false, 1, "290: void -> (CALL _ fun)", ImList.list(ImList.list("move",ImList.list("_reg","%25"),"$1"),ImList.list("jal",ImList.list("_reg","%31"),ImList.list("_reg","%25")),ImList.list("nop")), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[291] = new Rule(291, false, false, 1, "291: void -> (CALL _ fun)", ImList.list(ImList.list("jal","$1")), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[292] = new Rule(292, false, false, 1, "292: void -> (CALL _ fun)", ImList.list(ImList.list("move",ImList.list("_reg","%25"),"$1"),ImList.list("jal",ImList.list("_reg","%31"),ImList.list("_reg","%25"))), null, null, 0, false, false, new int[]{29}, new String[]{null, null});
    rulev[293] = new Rule(293, false, false, 1, "293: void -> (PARALLEL _ void)", null, null, null, 0, false, false, new int[]{1}, new String[]{null, null});
    rulev[28] = new Rule(28, false, false, 23, "28: conofs -> (LIST _ con32)", null, ImList.list(ImList.list("_onstack","$1")), null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[76] = new Rule(76, false, false, 1, "76: void -> (LIST _ regl _4)", ImList.list(ImList.list("lw","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{2,35}, new String[]{null, "*reg-I32*", null});
    rulev[78] = new Rule(78, false, false, 1, "78: void -> (LIST _ regh _5)", ImList.list(ImList.list("lh","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{3,36}, new String[]{null, "*reg-I16*", null});
    rulev[80] = new Rule(80, false, false, 1, "80: void -> (LIST _ regb _6)", ImList.list(ImList.list("lb","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{4,37}, new String[]{null, "*reg-I8*", null});
    rulev[82] = new Rule(82, false, false, 1, "82: void -> (LIST _ regd _7)", ImList.list(ImList.list("l.d","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{5,38}, new String[]{null, "*reg-F64*", null});
    rulev[84] = new Rule(84, false, false, 1, "84: void -> (LIST _ regf _8)", ImList.list(ImList.list("l.s","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{6,39}, new String[]{null, "*reg-F32*", null});
    rulev[85] = new Rule(85, false, false, 1, "85: void -> (LIST _ regl _4)", ImList.list(ImList.list("lw","$1",ImList.list("mem","$2")),ImList.list("nop")), null, null, 0, false, false, new int[]{2,35}, new String[]{null, "*reg-I32*", null});
    rulev[86] = new Rule(86, false, false, 1, "86: void -> (LIST _ regh _5)", ImList.list(ImList.list("lh","$1",ImList.list("mem","$2")),ImList.list("nop")), null, null, 0, false, false, new int[]{3,36}, new String[]{null, "*reg-I16*", null});
    rulev[87] = new Rule(87, false, false, 1, "87: void -> (LIST _ regb _6)", ImList.list(ImList.list("lb","$1",ImList.list("mem","$2")),ImList.list("nop")), null, null, 0, false, false, new int[]{4,37}, new String[]{null, "*reg-I8*", null});
    rulev[88] = new Rule(88, false, false, 1, "88: void -> (LIST _ regd _7)", ImList.list(ImList.list("l.d","$1",ImList.list("mem","$2")),ImList.list("nop")), null, null, 0, false, false, new int[]{5,38}, new String[]{null, "*reg-F64*", null});
    rulev[89] = new Rule(89, false, false, 1, "89: void -> (LIST _ regf _8)", ImList.list(ImList.list("l.s","$1",ImList.list("mem","$2")),ImList.list("nop")), null, null, 0, false, false, new int[]{6,39}, new String[]{null, "*reg-F32*", null});
    rulev[108] = new Rule(108, false, false, 1, "108: void -> (LIST _ regl _4)", ImList.list(ImList.list("lw","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{2,35}, new String[]{null, "*reg-I32*", null});
    rulev[109] = new Rule(109, false, false, 1, "109: void -> (LIST _ regh _5)", ImList.list(ImList.list("lh","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{3,36}, new String[]{null, "*reg-I16*", null});
    rulev[110] = new Rule(110, false, false, 1, "110: void -> (LIST _ regb _6)", ImList.list(ImList.list("lb","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{4,37}, new String[]{null, "*reg-I8*", null});
    rulev[111] = new Rule(111, false, false, 1, "111: void -> (LIST _ regd _7)", ImList.list(ImList.list("l.d","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{5,38}, new String[]{null, "*reg-F64*", null});
    rulev[112] = new Rule(112, false, false, 1, "112: void -> (LIST _ regf _8)", ImList.list(ImList.list("l.s","$1",ImList.list("mem","$2"))), null, null, 0, false, false, new int[]{6,39}, new String[]{null, "*reg-F32*", null});
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
    case Op.SUBREG:
      return jmac1(node);
    default:
      return quiltLirDefault(node);
    }
  }

  /** Expand emit-macro for list form. **/
  String emitList(ImList form, boolean topLevel) {
    String name = (String)form.elem();
    if (name == "+")
      return jmac2(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-")
      return jmac3(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "_imm")
      return jmac4(emitObject(form.elem(1)));
    else if (name == "_onstack")
      return jmac5(emitObject(form.elem(1)));
    else if (name == "mem")
      return jmac6(emitObject(form.elem(1)));
    else if (name == "_reg")
      return jmac7(emitObject(form.elem(1)));
    else if (name == "_oddreg")
      return jmac8(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac9(form.elem(1));
    else if (name == "epilogue")
      return jmac10(form.elem(1), emitObject(form.elem(2)));
    else if (name == "deflabel")
      return jmac11(emitObject(form.elem(1)));
    else if (name == "jumplabel")
      return jmac12(emitObject(form.elem(1)));
    else if (name == "_static")
      return jmac13(emitObject(form.elem(1)));
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
  
  void initializeMachineDep() {
    root.setHook("+AfterToMachineCode", mipsSRRTrig);
  }
  
  public final mipsSRR mipsSRRTrig = new mipsSRR();
  
  /** Processing of Save-and-Restore Registers(SRR) **/
  class mipsSRR implements LocalTransformer {
    private BiList SRRlist; //The list of Save-and-Restore Registers(SRR)
    private class SRR { //A structure of Save-and-Restore Registers(SRR)
      public LirNode reg;
      public long offset;
      public int type;
      public final int TYPE_SAVELOAD = 0;
      public final int TYPE_SAVEONLY = 1;
      public final int TYPE_LOADONLY = 2;
      SRR() {
        reg = null;
        offset = 0;
        type = TYPE_SAVELOAD;
      }
    }
    public String name() { return "mipsSRR"; }
    public String subject() { return "process Saving and Restoring of Registers (hook)"; }
    public boolean doIt(Data data, ImList args) { return true; }
    public boolean doIt(Function func, ImList args) {
      //Build the list of SRR.
      buildSRRlist(func);
      //Store/Load instructions are added by the LIR expression.
      genStorePart(func);
      genLoadPart(func);
      //Store/Load instructions are added for caller saving $f12(printf).
      genCallerSaving(func);
      //Load instructions are making for loading stack arguments
      //with correct offset values at the function's head part.
      genLoadStackArgs(func);
      return true;
    }
    
    /** Slowly but simply, this is helper method. **/
    private boolean lookLike(LirNode node, String pattern) {
      return (node.toString()).indexOf(pattern)>=0;
    }
    
    /** Build SRRlist by checking up SRRs in the function. **/
    private void buildSRRlist(Function func) {
      long offset = 0;  //This is relative offset value.
      LirFactory lir = func.newLir;
      SRRlist = new BiList();  // initialization of SRRlist
      mipsAttr at = (mipsAttr)getFunctionAttr(func);
      //Analyze attributes of this function.
      at.Analyze();
      //Append below SRRs to SRRlist
      {
        LirNode[] regs = {
          lir.symRef(func.module.globalSymtab.get("%31")),  //TYPE_SAVELOAD
          lir.symRef(func.module.globalSymtab.get("%fp")),  //TYPE_SAVELOAD
          lir.symRef(func.module.globalSymtab.get("%28"))   //TYPE_SAVEONLY
        };
        for(int i=0; i<3; i++) {
          if(convention=="standard" || convention=="linux")
            if (i==0 && at.getNumCaller()==0)
              continue;  //Skip "%31", needless
          //Add SRR
          SRR srr = new SRR();
          srr.reg = regs[i];
          srr.type = (i<2)? srr.TYPE_SAVELOAD : srr.TYPE_SAVEONLY;
          srr.offset = offset;
          offset -= 4;
          SRRlist.add( srr );
        }
      }
      //Check up SRRs in the function, and append to SRRlist
      int bytesSRRarea = 0;
      SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
      for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
        //Add SRR
        SRR srr = new SRR();
        int reg = it.next();
        srr.reg = machineParams.registerLir(reg);
        int type = ((LirNode)(srr.reg)).type;
        if (Type.bytes(type) > 4 && (offset % 8) != 0) //(-offset & 7) != 0)
          offset -= 4;  //Adjust to 8 bytes alignment when SRR is 8 bytes(64 bits).
        srr.offset = offset;
        bytesSRRarea = -(int)offset;
        offset -= (Type.bytes(type) <= 4) ? 4 : 8;
        SRRlist.add( srr );
      }
      //Calculate the correct attributes.
      at.setBytesLocalVars(frameSize(func));
      at.setBytesRegsaveArea(bytesSRRarea);
      at.Renew();
      //Replace relative offsets to more accurate relative offsets
      for (Iterator it = SRRlist.iterator(); it.hasNext(); ) {
        SRR srr = (SRR)(it.next());
        srr.offset += at.getFramesize() + at.getFrameoffset();
      }
    }
    
    /** Generate the LirNodes of storing SRRs **/
    private void genStorePart(Function func) {
      LirFactory lir = func.newLir;
      BiList list = func.lirList();
      //Skip (DEFLABEL ..) and (PROLOGUE ..)
      BiLink here = list.first();
      for (; !here.atEnd(); here = here.next()) {
        LirNode node = (LirNode)here.elem();
        if (lookLike(node, "(DEFLABEL ") || lookLike(node, "(PROLOGUE "))
          continue;
        break;
      }
      BiList secondHalf = list.split(here);
      //Save registers
      for (Iterator it = SRRlist.iterator(); it.hasNext(); ) {
        SRR srr = (SRR)(it.next());
        if (srr.type == srr.TYPE_LOADONLY)
          continue;
        int type = ((LirNode)(srr.reg)).type;
        LirNode _savereg = srr.reg;
        LirNode _spreg = lir.symRef(func.module.globalSymtab.get("%sp"));
        LirNode _offset = lir.iconst(I32, srr.offset);
        LirNode _add = lir.node(Op.ADD, I32, _spreg, _offset);
        LirNode _mem = lir.node(Op.MEM, type, _add);
        LirNode _set = lir.node(Op.SET, type, _mem, _savereg);
        list.add(_set);
      }
      //Switch $fp and $sp
      {
        int type = I32;
        LirNode _fpreg = lir.symRef(func.module.globalSymtab.get("%fp"));
        LirNode _spreg = lir.symRef(func.module.globalSymtab.get("%sp"));
        LirNode _set = lir.node(Op.SET, type, _fpreg, _spreg);
        list.add(_set);
      }
      list.concatenate(secondHalf);
    }
  
    /** Generate the LirNodes of loading SRRs **/
    private void genLoadPart(Function func) {
      LirFactory lir = func.newLir;
      BiList list = func.lirList();
      //Keep (EPILOGUE ..)
      LirNode epinode = (LirNode)list.takeLast();
      //Switch $sp and $fp
      {
        int type = I32;
        LirNode _spreg = lir.symRef(func.module.globalSymtab.get("%sp"));
        LirNode _fpreg = lir.symRef(func.module.globalSymtab.get("%fp"));
        LirNode _set = lir.node(Op.SET, type, _spreg, _fpreg);
        list.add(_set);
      }
      //Restore registers
      for (Iterator it = SRRlist.iterator(); it.hasNext(); ) {
        SRR srr = (SRR)(it.next());
        if (srr.type == srr.TYPE_SAVEONLY)
          continue;
        int type = ((LirNode)(srr.reg)).type;
        LirNode _loadreg = srr.reg;
        LirNode _spreg = lir.symRef(func.module.globalSymtab.get("%sp"));
        LirNode _offset = lir.iconst(I32, srr.offset);
        LirNode _add = lir.node(Op.ADD, I32, _spreg, _offset);
        LirNode _mem = lir.node(Op.MEM, type, _add);
        LirNode _list = lir.node(Op.LIST, Type.UNKNOWN, _loadreg, _mem);
        list.add(_list);
      }
      //Put back (EPILOGUE ..)
      list.add(epinode);
    }
    
    /** Add LirNodes of SRR Saving, Restoring of caller. **/
    private void genCallerSaving(Function func) {
      mipsAttr at = (mipsAttr)getFunctionAttr(func);
      //offset is correct offset value, which is round down to 8bytes boundary.
      int offset = 0;
      offset += ((int)(((SRR)((SRRlist.last()).elem())).offset) -8) & -8;
      LirFactory lir = func.newLir;
      BiList list = func.lirList();
      BiLink here = list.first();
      for (; !here.atEnd(); here = here.next()) {
        LirNode node = (LirNode)here.elem();
        if (lookLike(node, "(STATIC I32 \"printf\")")==false)	//051031MAR@JED
          continue;
        else {
          //Build the "_store" node and the "_load" node
          LirNode _fpreg = lir.symRef(func.module.globalSymtab.get("%fp"));
          LirNode _f12reg = lir.symRef(func.module.globalSymtab.get("%f12"));
          LirNode _offset = lir.iconst(I32, offset);
          LirNode _add = lir.node(Op.ADD, I32, _fpreg, _offset);
          LirNode _mem = lir.node(Op.MEM, F64, _add);
          LirNode _store = lir.node(Op.SET, F64, _mem, _f12reg);
          LirNode _load = lir.node(Op.LIST, Type.UNKNOWN, _f12reg, _mem);
          //
          BiList secondHalf = list.split(here);
          LirNode _caller = (LirNode)secondHalf.takeFirst();
          list.add( _store );
          list.add( _caller );
          list.add( _load );
          list.concatenate(secondHalf);
        }
      }
    }
    
    /** (Re)Generate loading of the function's stack arguments     **/
    /** with correct offset values. The savings of arguments are   **/
    /** already generated in rewritePrologue(), though the correct **/
    /** offsets are calculable after the Register Allocation is    **/
    /** performed.                                                 **/
    private void genLoadStackArgs(Function func) {
      boolean bCanProceed = false;
      mipsAttr at = (mipsAttr)getFunctionAttr(func);
      BiList list = func.lirList();
      for (BiLink q = list.first(); !q.atEnd(); q = q.next())
      {
        LirNode node = (LirNode)q.elem();
        //Skip irrelevants.
        if (lookLike(node, ") (MEM") == false
           || lookLike(node, "(REG I32 \"%fp\") (INTCONST I32") == false)
          continue;  //Maybe this is not the loading of stack arguments.
        if (lookLike(node, "%f12"))
          continue;  //Maybe this is caller saving.
        //(Re)Generate new node.
        BiList secondHalf = list.split(q);
        secondHalf.takeFirst();
        int type = node.kid(0).type;
        long offset = ((LirIconst)(node.kid(1).kid(0).kid(1))).value;
        LirNode _fpreg = lir.symRef(func.module.globalSymtab.get("%fp"));
        LirNode _offset = lir.iconst(I32, offset + (long)(at.getFramesize()));
        LirNode _add = lir.node(Op.ADD, I32, _fpreg, _offset);
        LirNode _mem = lir.node(Op.MEM, type, _add);
        LirNode _reg = node.kid(0);
        LirNode _list = lir.node(Op.SET, type, _reg, _mem);
        list.add(_list);
        list.concatenate(secondHalf);
      }
    }
  }
  
  /** Prepare new function attribute information(override for mips). **/
  FunctionAttr newFunctionAttr(Function func) {
    return new mipsAttr(func);
  }
  
  /** mips's function attribute **/
  static class mipsAttr extends FunctionAttr {
    private int bytesLocalVars;     //Bytes of local variable size on stack frame
    private int bytesRegsaveArea;   //Bytes of SRR area size on stack frame
    private int numCaller;        //Number of CALL node, generated by Analyze()
    private int numCallerWithArg; //Number of CALL node with arguments, generated by Analyze()
    private int maxParamCounter;  //Max number of caller's arguments, generated by Analyze()
    private String bitmask;  //Value of .mask directive, generated by Renew()
    private int frameoffset; //Value of SRR area's offset, generated by Renew()
    private int framesize;   //Size of stack frame, generated by Renew()
    private int cprestore;   //Value of .cprestore directive, generated by Renew()
    private int localoffset; //Local variable's offset on stack frame, generated by Renew()
  
    public void setBytesLocalVars(int bytes) { bytesLocalVars = bytes; }
    public void setBytesRegsaveArea(int bytes) { bytesRegsaveArea = bytes; }
    public int getNumCaller() { return numCaller; }
    public String getBitmask() { return bitmask; }
    public int getFrameoffset() { return frameoffset; }
    public int getFramesize() { return framesize; }
    public int getCprestore() { return cprestore; }
    public int getLocaloffset() { return localoffset; }
  
    mipsAttr(Function func) {
      super(func);
      bytesRegsaveArea=0;
      numCaller=0;
      numCallerWithArg=0;
      maxParamCounter=0;
      bitmask="50000000";
      frameoffset=-12;
      framesize = 32;
      cprestore = 0;
      localoffset = 24;
    }
  
    public void Analyze() {
      numCaller=0;
      numCallerWithArg=0;
      maxParamCounter=0;
      //Analyze caller and arguments of caller in this function.
      BiList list = func.lirList();
      for (BiLink q = list.first(); !q.atEnd(); q = q.next()) {
        LirNode ins = (LirNode)q.elem();
        if (lookLike(ins, "(PARALLEL (CALL")) {
          //Analyze caller
          numCaller++;
          LirNode args = ((LirNode)(ins.kid(0))).kid(1);
          int n = args.nKids();
          if (n>0)
            numCallerWithArg++;
          //Analyze arguments of caller
          int paramCounter = 0;
          for (int i = 0; i < n; i++) {
            LirNode arg = args.kid(i);
            if (Type.tag(arg.type)==Type.FLOAT && Type.bits(arg.type) > 32)
              paramCounter+=2;
            else
              paramCounter++;
          }
          if (maxParamCounter<paramCounter)
            maxParamCounter=paramCounter;
        } else if (lookLike(ins, "(PARALLEL (CALL (STATIC I32 \"__")) {
          numCaller++;  //for ctype.h,etc.
        }
      }
    }
  
    public void Renew() {
      renewLocaloffset();
      renewBitmask();
      renewFrameoffset();
      renewFramesize();
      renewCprestore();
    }
  
    private String renewBitmask() {
      int ii;
      int mask;
      if (numCallerWithArg<=0) {
        mask=0x50000000;
      } else if (numCallerWithArg>=9) {
        mask=0xd0ff0000;
      } else {
        mask=0;
        for(ii=0;ii<(int)numCallerWithArg-1;ii++)
          mask |= 1 << (ii+16);
        mask=0xd0000000 | mask;
      }
      return bitmask = Integer.toHexString(mask);
    }
    private int renewFrameoffset() {
      if (numCallerWithArg<0)
        return 0;
      if (numCallerWithArg>=9)
        return -4*2;
      return frameoffset = -4*(4-((int)numCallerWithArg+1)%4);
    }
    private int renewFramesize() {
      framesize = localoffset;
      framesize += bytesLocalVars;              // local variables:
      framesize += bytesRegsaveArea;            // SRRs:
      framesize += (maxParamCounter) * 4;       // CALL node's arguments:
      framesize += calcExtra(framesize);        // extra:
      framesize += 8 + 4;	//caller saving $f12 for printf. + 4 is for alignment.
      return framesize = (framesize + 7) & -8;  // round up to 8byte boundary
    }
    private int calcExtra(int size) {
      int extra;
      extra = ((((size + 7) & -8) + frameoffset) < size)? 8 : 0;
      extra += (framesize+frameoffset<=16)? 8 : 0;
      return extra;
    }
    private int renewCprestore() {
      return cprestore = (framesize+frameoffset-8);
    }
    /** Slowly but simply, this is helper method. **/
    private boolean lookLike(LirNode node, String pattern) {
      return (node.toString()).indexOf(pattern)>=0;
    }
    private boolean renewLocaloffset() {
      int ofs = localoffset;
      boolean bNeed = false;
      boolean bAlign8 = false; 
      boolean bCanProceed = false;
      BiList list = func.lirList();
      BiLink here = list.first();
      for (; !here.atEnd(); here = here.next()) {
        LirNode node = (LirNode)here.elem();
        boolean bStoreI32
           = lookLike(node, "(SET I32 (MEM I32 (ADD I32 (REG I32 \"%sp\")");
        boolean bStoreF32
           = lookLike(node, "(SET F32 (MEM F32 (ADD I32 (REG I32 \"%sp\")");
        boolean bStoreF64
           = lookLike(node, "(SET F64 (MEM F64 (ADD I32 (REG I32 \"%sp\")");
        if ((bStoreI32 || bStoreF32 || bStoreF64) &&
           node.kid(0).kid(0).nKids()==2 ) {
          LirIconst iconst = (LirIconst)(node.kid(0).kid(0).kid(1));
          int _ofs = (int)iconst.value;
          if (ofs <= _ofs) {
            ofs = _ofs;
            bNeed = true;
            if (bStoreF64)
              bAlign8 = true;
          }
        }
      }
      if (bNeed)
        localoffset = ofs + ((bAlign8==false)? 4 : 8);
      return bNeed;
    }
  }
  
  public final mipsArgumentPassingUtil
    mipsArgumentPassing = new mipsArgumentPassingUtil();
  
  /** Processing of Save-and-Restore(SR), as utility class. **/
  class mipsArgumentPassingUtil {
    /** Generate the LirNodes of function's arguments and                **/
    /** Save arguments to registers of "*reg-saved-I32*" or stack frame. **/
    /** NOTE: In case of save to stack frame, src's offset is adjusted   **/
    /**       in mipsSRR.genLoadStackArgs().                             **/
    public LirNode[] genFunctionPrologue(LirNode node, BiList post) {
      int n = node.nKids();
      int types[] = new int[n-1];
      int numUsedRegisters=0;
      LirNode base = lir.symRef(module.globalSymtab.get("%fp"));
      //Save arguments
      for (int i=1;i<n;i++) {
        LirNode arg = node.kid(i);
        types[i-1] = arg.type;
        LirNode src = nthArg(i-1,types,base,1);
        post.add(lir.node(Op.SET, arg.type, arg, src)); 
        if (src.opCode != Op.MEM)
          numUsedRegisters++;
      }
      //Make the list of this function's arguments.
      LirNode[] argv = new LirNode[numUsedRegisters+1];
      argv[0] = node.kid(0);
      for(int i=0;i<numUsedRegisters;i++)
        argv[i+1] = nthArg(i,types,base,1);
      return argv;
    }
  
    /** Generate the LirNodes of function's return value. **/
    public LirNode genFunctionEpilogue(LirNode node, BiList pre) {
      if (node.nKids() < 2)
        return node;
      LirNode ret = node.kid(1);
      LirNode tmp = func.newTemp(ret.type);
      LirNode reg = returnReg(ret.type);
      switch (Type.tag(ret.type)) {
      case Type.INT:
      case Type.FLOAT:
        if (isComplex(ret)) {
          pre.add(lir.node(Op.SET, tmp.type, tmp, ret));
          pre.add(lir.node(Op.SET, reg.type, reg, tmp));
        } else
          pre.add(lir.node(Op.SET, reg.type, reg, ret));
        return lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), reg);
      case Type.AGGREGATE:
        return node; //keeping original infomation.
      default:
        throw new CantHappenException();
      }
    }
  
    /** Generate the Prologue part of CALL node, and arrange arguments  **/
    /** to obey the convention of arguments passing.                    **/
    /** @return LirNodes of arranged arguments of CALL node.            **/
    public LirNode[] genCallPrologue(LirNode node, BiList pre) {
      BiList list1 = new BiList();
      BiList list2 = new BiList();
      LirNode base = lir.symRef(module.globalSymtab.get("%sp"));
      LirNode callee = node.kid(0);
      LirNode args = node.kid(1);
      //mode_nthArg
      //==0:using $4..$7
      //==1:using $4..$7 or $f12,$f14
      int mode_nthArg = 1;
      if (callee.opCode == Op.STATIC)
        if (((LirSymRef)callee).symbol.name.equals("printf"))
          mode_nthArg = 0;
      // parameters
      int n = args.nKids();
      int types[] = new int[n];
      for (int i = 0; i < n; i++) {
        LirNode arg = args.kid(i);
        types[i] = arg.type;
        switch (Type.tag(arg.type)) {
        case Type.INT:
        case Type.FLOAT:
          {
            LirNode temp = func.newTemp(types[i]);
            LirNode dest = nthArg(i,types,base,mode_nthArg);
            if (dest.opCode == Op.MEM || isComplex(arg)) {
              list1.add(lir.node(Op.SET, temp.type, temp, arg));
              list2.add(lir.node(Op.SET, dest.type, dest, temp));
            } else
              list2.add(lir.node(Op.SET, dest.type, dest, arg));
          }
          break;
        case Type.AGGREGATE: //by reference
          {
            types[i] = I32;
            arg = arg.kid(0);
            LirNode dest = nthArg(i,types,base,mode_nthArg);
            list2.add(lir.node(Op.SET, dest.type, dest, arg));
          }
          break;
        default:
          throw new CantHappenException("Unexpected CALL parameter" + node);
        }
      }
      LirNode[] newargv = new LirNode[n];
      int j = 0;
      for (BiLink p = list2.first(); !p.atEnd(); p = p.next()) {
        LirNode ins = (LirNode)p.elem();
        newargv[j++] = ins.kid(0);
      }
      pre.concatenate(list1);
      pre.concatenate(list2);
      return newargv;
    }
  
    /** Generate the Epilogue part of CALL node, and arrange return value. **/
    /** @node should be rewriteCall()'s argument.                       **/
    public void genCallEpilogue(LirNode node, BiList post) {
      LirNode ret = null;
      if (node.kid(0).kid(2).nKids() > 0) {
        ret = node.kid(0).kid(2).kid(0);  //returned L-exp
        if (ret.opCode != Op.FRAME) {
          switch (Type.tag(ret.type)) {
          case Type.INT:
          case Type.FLOAT:
            {
              LirNode reg = returnReg(ret.type);
              LirNode tmp = func.newTemp(ret.type);
              post.add(lir.node(Op.SET, ret.type, tmp, reg));
              post.add(lir.node(Op.SET, ret.type, ret, tmp));
              node.kid(0).kid(2).setKid(0, reg);
              break;
            }
          case Type.AGGREGATE:
            break;  //no action need.
          }
        }
      }
    }
  
    /** Return true if node is a complex one. **/
    private boolean isComplex(LirNode node) {
      switch (node.opCode) {
      case Op.FLOATCONST:
      case Op.INTCONST:
      case Op.REG:
      case Op.STATIC:
      case Op.FRAME:
        return false;
      default:
        return true;
      }
    }
  
    /** Calc difference offset on stack frame **/
    private int calcDifferenceOffset(int no, int[] types) {
      int mew=0;
      int offset=0;
      for(int ii=0;ii<no+1;ii++) {
        int bytes = Type.bytes(types[ii]);
        if (bytes==4)
          mew++;
        if (bytes>4 && mew%2!=0) {
          offset += 4;
          mew--;
        }
        if (ii!=no)
          offset += (bytes<4)? 4 : bytes;
      }
      return offset;
    }
  
    /** Generate actual size registers. **/
    private LirNode genActualSizeRegister(int type, String name) {
      Symbol nameSym = module.globalSymtab.get(name);
      LirNode master = lir.symRef(nameSym);
      if (type == I32)
        return master;
      else if (type == I16)
        return lir.node
          (Op.SUBREG, I16, master, lir.untaggedIconst(I32, 0));
      else if (type == I8)
        return lir.node
          (Op.SUBREG, I8, master, lir.untaggedIconst(I32, 0));
      else if (type == F64)
        return master;
      else if (type == F32)
        return lir.node
          (Op.SUBREG, F32, master, lir.untaggedIconst(I32, 0));
      return null;
    }
  
    /** Return nth argument of the function for parameter passing. **/
    /** @mode==0:using $4..$7                                      **/
    /**      ==1:using $4..$7 or $f12,$f14                         **/
    private LirNode nthArg(int no, int[] types, LirNode base, int mode) {
      int diffofs = calcDifferenceOffset(no, types);
      if (diffofs<16) { //using registers
        if (mode==0)  //mode==using $4..$7
          return genActualSizeRegister(types[no], "%" + ((diffofs/4) + 4));
        else {       //mode==using $4..$7 or $f12,$f14
          LirNode node;
          if (types[no]==F64)
           node = genActualSizeRegister(types[no], "%f" + ((diffofs/8)*2 + 12));
          else
           node = genActualSizeRegister(types[no], "%" + ((diffofs/4) + 4));
          return node;
        }
      } else //using stack frame
        //At this point, diffofs is just only difference value.
        return lir.node(Op.MEM, types[no], 
                 lir.node(Op.ADD, I32, base, lir.iconst(I32, diffofs)));
    }
    
    /** Return the register for value returned. **/
    private LirNode returnReg(int type) {
      switch (Type.tag(type)) {
      case Type.INT:
        switch (Type.bytes(type)) {
        case 1:
        case 2:
        case 4:
          return genActualSizeRegister(type, "%2");
        default:
          return null;
        }
      case Type.FLOAT:
        switch (Type.bytes(type)) {
        case 4: return genActualSizeRegister(type, "%f0");
        case 8: return genActualSizeRegister(type, "%f0");
        default:
          return null;
        }
      default:
        return null;
      }
    }
  }
  
  /** Rewrite FRAME node to target machine form. **/
  LirNode rewriteFrame(LirNode node) {
    Symbol fp = func.module.globalSymtab.get("%fp");
    SymAuto sym = (SymAuto)((LirSymRef)node).symbol;
    int _off = -sym.offset() - Type.bytes(sym.type);
    return lir.node(Op.ADD, node.type, lir.symRef(fp),
             lir.node(Op.LIST, Type.UNKNOWN, 
               lir.iconst(I32, (long)(_off))) ); //This is conofs.
  }
  
  public final mipsAggregateByReference
    mipsAggregateByReferenceTrig = new mipsAggregateByReference();
  
  /** Convert aggregate parameter passing from by-value to by-reference. **/
  class mipsAggregateByReference implements LocalTransformer {
    public boolean doIt(Function func, ImList args) {
      LirFactory lir = func.newLir;
      LirNode epi=null;
      //Prepare
      {
        //Search return value, and memorize it.
        BiList list = new BiList();
        BiLink p = func.flowGraph().basicBlkList.first();
        for (; !p.atEnd(); p = p.next()) {
          LirNode stmt=null;
          BasicBlk blk = (BasicBlk)p.elem();
          for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
            stmt = (LirNode)q.elem();
            if (stmt.opCode == Op.EPILOGUE) {
              if (stmt.nKids()>=2)
                epi = stmt.kid(1);
              else if (stmt.nKids()>=1)
                epi = null;
              break;
            }
          }
          if (stmt.opCode == Op.EPILOGUE)
            break;
        }
      }
      //Change the PROLOGUE/EPILOGUE L-exp.
      {
        // rewrite callee's PROLOGUE/EPILOGUE L-exp.
        BiLink p = func.flowGraph().basicBlkList.first();
        for (; !p.atEnd(); p = p.next()) {
          BasicBlk blk = (BasicBlk)p.elem();
          for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
            LirNode stmt = (LirNode)q.elem();
            if (stmt.opCode == Op.PROLOGUE) {
              LirNode ret = epi;
              if (ret!=null) {
                if (Type.isAggregate(ret.type)) {
                  int n = stmt.nKids();
                  LirNode[] argv = new LirNode[n+1];
                  argv[0]=stmt.kid(0);
                  argv[1]=ret;
                  for (int i=1; i < n; i++) {
                    argv[i+1]=stmt.kid(i);
                  }
                  // Addition of the arguments of callee's PROLOGUE.
                  q.addBefore(lir.node(Op.PROLOGUE, Type.UNKNOWN, argv));
                  q.unlink();
                }
              }
            }
            if (stmt.opCode == Op.EPILOGUE) {
              if (stmt.nKids()<=1)
                continue;
              if (Type.isAggregate(stmt.kid(1).type)) {
                // Removing the return value of callee's EPILOGUE.
                q.addBefore(lir.node(Op.EPILOGUE, Type.UNKNOWN, stmt.kid(0)));
                q.unlink();
              }
            }
          }
        }
        // Rewrite FRAME to Storage.REG when the argument of PROLOGUE is FRAME.
        LirNode[] map = new LirNode[func.localSymtab.idBound()];
        p = func.flowGraph().basicBlkList.first();
        for (; !p.atEnd(); p = p.next()) {
          BasicBlk blk = (BasicBlk)p.elem();
          for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
            LirNode stmt = (LirNode)q.elem();
            if (stmt.opCode == Op.PROLOGUE) {
              int n = stmt.nKids();
              for (int i=0; i < n; i++) {
                if (stmt.kid(i).opCode == Op.MEM
                    && Type.tag(stmt.kid(i).type) == Type.AGGREGATE) {
                  if (stmt.kid(i).kid(0).opCode != Op.FRAME)
                    throw new CantHappenException("expecting FRAME: " + stmt);
                  Symbol var = ((LirSymRef)stmt.kid(i).kid(0)).symbol;
                  String name = (var.name + "%").intern();
                  LirNode reg = lir.symRef
                    (func.addSymbol(name, Storage.REG,
                                    func.module.targetMachine.typeAddress,
                                    0, 0, null));
                  map[var.id] = reg;
                  stmt.setKid(i, reg);
                  func.localSymtab.remove(var);
                }
              }
            }
            rewriteAggRef(stmt, map);
          }
        }
      }
      // Change the CALL L-exp.
      {
        // Rewrite CALL node and append the return value to 1st argument.
        BiLink p = func.flowGraph().basicBlkList.first();
        for (; !p.atEnd(); p = p.next()) {
          BasicBlk blk = (BasicBlk)p.elem();
          for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
            LirNode stmt = (LirNode)q.elem();
            if (stmt.opCode == Op.CALL) {
              LirNode _args = stmt.kid(1);
              LirNode ret = stmt.kid(2);
              int n = _args.nKids();
              if (ret.nKids()>1)
                throw new CantHappenException("expecting Return: " + stmt);
              else if (ret.nKids()==1) {
                if (ret.kid(0).opCode == Op.MEM
                    && Type.tag(ret.kid(0).type) == Type.AGGREGATE) {
                  LirNode newargs[] = new LirNode[n+1];
                  newargs[0]=ret.kid(0);
                  for(int i=0; i<n; i++)
                    newargs[i+1] = _args.kid(i);
                  q.addBefore(lir.operator(Op.CALL, Type.UNKNOWN,
                               stmt.kid(0),
                               lir.operator(Op.LIST, Type.UNKNOWN, newargs, null),
                               ret, null));
                  q.unlink();
                }
              }
            }
          }
        }
      }
      // Change the neighborhood of CALL L-exp.
      {
        int tmpn = 1;
        BiLink p = func.flowGraph().basicBlkList.first();
        for (; !p.atEnd(); p = p.next()) {
          BasicBlk blk = (BasicBlk)p.elem();
          for (BiLink q = blk.instrList().first(); !q.atEnd(); q = q.next()) {
            LirNode stmt = (LirNode)q.elem();
            if (stmt.opCode == Op.CALL) {
              LirNode _args = stmt.kid(1);
              int n = _args.nKids();
              if (n<=0)
                continue;
              {
                //When the 1st argument(equals return value) is AGGREGATE:
                // The 1st argument's value is the address that put 
                // return value at callee side. Cause we need the FRAME node
                // only that is child of the MEM node, we remove the MEM node
                // and set up the FRAME node as 1st argument again.
                LirNode ret = stmt.kid(2);
                if (_args.kid(0).opCode == Op.MEM
                    && Type.tag(_args.kid(0).type) == Type.AGGREGATE
                    && ret.nKids() > 0)
                {
                  if (ret.kid(0).opCode == Op.MEM
                      && Type.tag(ret.kid(0).type) == Type.AGGREGATE)
                  {
                    LirNode copy = _args.kid(0).kid(0);
                    _args.setKid(0, copy);
                  }
                }
              }
              if (n<=1)
                continue;
              {
                //When the one of [2..n-1] arguments is AGGREGATE:
                // We make a copy of the argument and set up it so that
                // it is passed, because there is a possibility that
                // it is destructed.
                for (int i = 1; i < n; i++) {
                  if (_args.kid(i).opCode == Op.MEM
                      && Type.tag(_args.kid(i).type) == Type.AGGREGATE) {
                    // make a copy of aggregate.
                    int type = _args.kid(i).type;
                    String name = ".AG" + tmpn++;
                    LirNode copy = lir.node(Op.MEM, type,
                      lir.symRef(
                        func.addSymbol(name, Storage.FRAME, type, 0, 0, null)));
                    q.addBefore(lir.operator
                                (Op.SET, type, copy, _args.kid(i), null));
                    _args.setKid(i, copy);
                  }
                }
              }
            }
          }
        }
      }
      return true;
    }
    public boolean doIt(Data data, ImList args) { return true; }
    public String name() { return "mipsAggregateByReference"; }
    public String subject() { return "Rewrite Aggregate Parameter Passing"; }
  
    /** Rewrite aggregate FRAME variables in <code>node</code>. **/
    private LirNode rewriteAggRef(LirNode node, LirNode[] map) {
      int n = node.nKids();
      for (int i = 0; i < n; i++)
        node.setKid(i, rewriteAggRef(node.kid(i), map));
      if (node.opCode == Op.FRAME) {
        LirNode reg = map[((LirSymRef)node).symbol.id];
        if (reg != null)
          return reg;
      }
      return node;
    }
  };
  
  /** Return early time pre-rewriting sequence. **/
  public Transformer[] earlyRewritingSequence() {
    return new Transformer[] {
      mipsAggregateByReferenceTrig,
      localEarlyRewritingTrig
    };
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
  
  /** Rewrite PROLOGUE **/
  LirNode rewritePrologue(LirNode node, BiList post) {
    LirNode[] argv = mipsArgumentPassing.genFunctionPrologue(node, post);
    return lir.node(Op.PROLOGUE, Type.UNKNOWN, argv);
  }
  
  /** Rewrite EPILOGUE **/
  LirNode rewriteEpilogue(LirNode node, BiList pre) {
    return mipsArgumentPassing.genFunctionEpilogue(node, pre);
  }
  
  /** Rewrite CALL node. **/
  LirNode rewriteCall(LirNode node, BiList pre, BiList post) {
    LirNode[] newargv = mipsArgumentPassing.genCallPrologue(node,pre);
    //Generate new CALL node
    ImList regCallClobbers = new ImList(ImList.list("REG","I32","%2"), new ImList(ImList.list("REG","I32","%3"), new ImList(ImList.list("REG","I32","%4"), new ImList(ImList.list("REG","I32","%5"), new ImList(ImList.list("REG","I32","%6"), new ImList(ImList.list("REG","I32","%7"), new ImList(ImList.list("REG","I32","%8"), new ImList(ImList.list("REG","I32","%9"), new ImList(ImList.list("REG","I32","%10"), new ImList(ImList.list("REG","I32","%11"), new ImList(ImList.list("REG","I32","%12"), new ImList(ImList.list("REG","I32","%13"), new ImList(ImList.list("REG","I32","%14"), new ImList(ImList.list("REG","I32","%15"), new ImList(ImList.list("REG","I32","%24"), new ImList(ImList.list("REG","I32","%25"), new ImList(ImList.list("REG","F64","%f0"), new ImList(ImList.list("REG","F64","%f2"), new ImList(ImList.list("REG","F64","%f4"), new ImList(ImList.list("REG","F64","%f6"), new ImList(ImList.list("REG","F64","%f8"), ImList.list(ImList.list("REG","F64","%f10"),ImList.list("REG","F64","%f12"),ImList.list("REG","F64","%f14"),ImList.list("REG","F64","%f16"),ImList.list("REG","F64","%f18")))))))))))))))))))))));
    try {
      node = lir.node
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
    mipsArgumentPassing.genCallEpilogue(node, post);
    return node;
  }
  
  /*
  Code building macros.
  */
  
  Object jmac1(LirNode x) {
    Symbol reg = ((LirSymRef)x.kid(0)).symbol;
    int dtype = x.type;
    int offset = (int)((LirIconst)x.kid(1)).value;
    if (dtype == F32 && offset == 1)
      return "%f" + (Integer.parseInt(reg.name.substring(2)) + 1);
    else
      return reg.name;
  }
  
  /*
  Code emission macros.
    Patterns not defined below will be converted to:
     (foo bar baz) --> foo   bar,baz   or foo(bar,baz)
  */
  
  String jmac2(String x, String y) {
    if (y.charAt(0) == '-')
      return x + y;
    else
      return x + "+" + y;
  }
  
  String jmac3(String x, String y) {
    if (y.charAt(0) == '-')
      return x + "+" + y.substring(1);
    else
      return x + "-" + y;
  }
  
  String jmac4(String x) { return x; }	//051031MAR@JED
  
  String jmac5(String x) {
    mipsAttr at = (mipsAttr)getFunctionAttr(func);
    int ofs = at.getLocaloffset() + Integer.parseInt(x);
    return Integer.toString(ofs);
  }
  
  String jmac6(String x) {
    int pos, pos0, pos1;
    String str0, str1, str2;
    str0=x;
    pos0=str0.indexOf('+');
    pos1=str0.indexOf('-');
    if ((pos0<0 && pos1<0)||(pos0>0 && pos1>0)) {
      str1=str0;
      str2="0";
    } else {
      pos=(pos0>0)?pos0:pos1;
      str1=str0.substring(0, pos);
      str2=str0.substring((pos0>0)?pos+1:pos);
    }
    if (str1.charAt(0)!='$' && str2.equals("0"))
      str0 = str1;
    else
      str0 = str2 + "(" + str1 + ")";
    return str0;
  }
  
  String jmac7(String x) {
    String name = "";
    if (x.charAt(0)=='%') {
      String _name = x.substring(1);
  //    if (_name.startsWith("dw"))
  //      _name = _name.substring(2);
      name = "$" + _name;
    }
    return name;
  }
  
  String jmac8(String x) {
    //This (_oddreg x) is reduced from (_oddreg (_reg x))).
    int pos;
    String name = "";
    if (x.startsWith("$f")) {
      name = "$f" + (Integer.parseInt(x.substring(2)) + 1);
    } else if (x.startsWith("$"))
      name = "$" + (Integer.parseInt(x.substring(1)) + 1);
    return name;
  }
  
  String jmac9(Object f) {
    Function func = (Function)f;
    mipsAttr at = (mipsAttr)getFunctionAttr(func);
    String str;
    if (convention=="spim")
      str = "\tsubu\t$sp," + at.getFramesize() + "\n";
    else { //convention=="standard"
      str = "\t.frame\t$fp," + at.getFramesize() + ",$31\n"
          + "\t.mask\t0x" + at.getBitmask() + "," + at.getFrameoffset() + "\n"
          + "\t.fmask\t0x00000000,0\n";
      if (at.getNumCaller()>=0){
        str += "\t.set\tnoreorder\n"
             + "\t.cpload\t$25\n"
             + "\t.set\treorder\n";
      }
      str += "\tsubu\t$sp,$sp," + at.getFramesize() + "\n";
      if (at.getNumCaller()>0) {
        str += "\t.cprestore " + at.getCprestore() + "\n";
      } else {
        str += "\t.cprestore 0\n";
      }
    }
    return str;
  }
  
  String jmac10(Object f, String rettype) {
    Function func = (Function)f;
    mipsAttr at = (mipsAttr)getFunctionAttr(func);
    String str;
    if (convention=="spim")
      str = "\taddu\t$sp," + at.getFramesize() + "\n"
            + "\tjr\t$31\n\tnop\n";
    else //convention=="standard"
      str = "\taddu\t$sp,$sp," + at.getFramesize() + "\n"
            + "\tj\t$31\n\t.end\t" + func.symbol.name;
    return str;
  }
  
  String jmac11(String x) {
    String name;
    name=x;
    if (name.charAt(0)=='.')
      name = "$" + name.substring(1);
    return name + ":";
  }
  
  String jmac12(String x) {
    String name;
    name=x;
    if (name.charAt(0)=='.')
      name = "$" + name.substring(1);
    return name;
  }
  
  String jmac13(String x) {
    String name = x;
    if (x.startsWith("string."))
      name = "$LC" + x.substring(x.indexOf(".")+1);
    else if (convention=="spim")
      name = "_" + name;
    return name;
  }
  
  
  /** Return segment for read-only constant. **/
  String segmentForConst() { return ".rdata"; }
  
  /** Return alignment for type. **/
  public int alignForType(int type) {
    switch (Type.bytes(type)) {
      case 1: return 1;
      case 2: return 2;
      default: return 4;
    }
  }
  
  void emitComment(PrintWriter out, String comment) {
    out.println("# " + comment);
  }
  
  void emitBeginningOfSegment(PrintWriter out, String segment) {
    if (convention=="standard" || convention == "linux")
      out.println("\t" + segment);
  }
  
  void emitEndOfSegment(PrintWriter out, String segment) {
    /* do nothing */
  }
  
  
  void emitDataLabel(PrintWriter out, String label) {
    String name;
    name = label;
    if (name.startsWith("string."))
      name = "$LC" + name.substring(name.indexOf(".")+1);
    else if (convention=="spim")
      name = "_" + name;
  
    if (convention != "linux") {
      /* NB: Backend doesn't assume changing section here. */
      out.println("\t.data");
    }
    out.println(name + ":");
  }
  
  void emitCodeLabel(PrintWriter out, String label) {
    String name;
    name=label;
    if (label.charAt(0)=='.')
      name = "$" + label.substring(1);
    if (convention=="spim") {
      if (name != "main")
        out.println("_" + name + ":");
      else
        out.println(name + ":");
    } else  //convention=="standard"
      out.println(name + ":");
  }
  
  /** Emit data align **/
  void emitAlign(PrintWriter out, int align) {
    if (convention=="standard" || convention == "linux")
      out.println("\t.align\t" + align);
  }
  
  /** Emit data common **/
  void emitCommon(PrintWriter out, SymStatic symbol, int bytes) {
    if (symbol.linkage == "LDEF") {
      if (convention=="standard" || convention == "linux")   //// 2007.7
        out.println("\t.local\t" + symbol.name);
    }
    if (symbol.linkage == "XDEF"){   //// 2007.7
      if (convention=="spim") 
        out.println("\t.globl\t" + "_" + symbol.name);
      else
        out.println("\t.globl\t" + symbol.name);
    }   //// 2007.7
    if (convention=="spim") {
      out.println("\t.data");
      if (bytes > 4)
        out.println("_" + symbol.name + ": .space " + bytes);
      else
        out.println("_" + symbol.name + ": .word 0");
    } else  //convention=="standard"
      out.println("\t.comm\t" + symbol.name + "," + bytes);
  }
  
  /** Emit linkage information of symbol **/
  void emitLinkage(PrintWriter out, SymStatic symbol) {
    if (symbol.linkage == "XDEF") {
      if (convention=="spim") {
        if (symbol.name != "main")
          out.println("\t.globl\t" + "_" + symbol.name);
        else
          out.println("\t.globl\t" + symbol.name);
        out.println("\t" + symbol.segment + "\t");  
      } else {  // convention=="standard"
        out.println("\t.globl\t" + symbol.name);
        if (symbol.segment == ".text")
          out.println("\t.ent\t" + symbol.name);
        else if (symbol.segment == ".data") {
          out.println("\t.type\t" + symbol.name + ",@object");
          out.println("\t.size\t" + symbol.name + "," + symbol.boundary);
        }
      }
    }
    if (convention == "linux") {
      if (symbol.linkage == "LDEF" && symbol.segment == ".text") {
        if (!symbol.name.startsWith("string.")) {
          out.println("\t.ent\t" + symbol.name);
        }
      }
    }
  }
  
  /** Emit data zeros **/
  void emitZeros(PrintWriter out, int bytes) {
    out.println("\t.space\t" + bytes);
  }
  
  /** Emit ident **/
  void emitIdent(PrintWriter out, String word) {
    out.println("# " + word);
  }
  
  /** Emit data **/
  void emitData(PrintWriter out, int type, LirNode node) {
    if (type == I32) {
      String _str = lexpConv.convert(node);
      if (_str.startsWith("string."))
        _str = "$LC" + _str.substring(_str.indexOf(".")+1);
        //##72
      else if (convention=="spim") {
        char lChar0 = _str.charAt(0);
        if (Character.isLetter(lChar0)||
            (lChar0 == '_')) {
          _str = '_' + _str;
        }
      }
      //##72
      out.println("\t.word\t" + _str);
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
  		+ " # " + value + "(double)");
      out.println("\t.word\t0x" + Long.toString(bits & 0xffffffffL, 16));
    }
    else if (type == F32) {
      double value = ((LirFconst)node).value;
      long bits = Float.floatToIntBits((float)value);
      out.println("\t.word\t0x" + Long.toString(bits & 0xffffffffL, 16)
                  + " # " + value + "(float)");
    }
    else {
      throw new CantHappenException("unknown type: " + type);
    }
  }
  
}
