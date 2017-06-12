/*
Productions:
 1: label -> (LABEL _)
 2: xregw -> (REG I32)
 3: xregw -> (SUBREG I32)
 4: regw -> xregw
 5: xregh -> (REG I16)
 6: xregh -> (SUBREG I16)
 7: regh -> xregh
 8: xregb -> (REG I8)
 9: xregb -> (SUBREG I8)
 10: regb -> xregb
 11: xregf -> (REG F32)
 12: xregf -> (SUBREG F32)
 13: regf -> xregf
 14: xregd -> (REG F64)
 15: xregd -> (SUBREG F64)
 16: regd -> xregd
 17: cbu -> (INTCONST _)
 18: cnst -> cbu
 19: ch -> (INTCONST _)
 20: cnst -> ch
 21: cw -> (INTCONST _)
 22: cnst -> cw
 23: sta -> (STATIC I32)
 24: asmcnst -> cnst
 25: asmcnst -> (ADD I32 asmcnst cnst)
 26: asmcnst -> (SUB I32 asmcnst cnst)
 27: ximm1 -> (INTCONST _)
 28: xmimm -> (INTCONST _)
 29: ximm -> (INTCONST _)
 30: base -> regw
 31: imm8 -> (INTCONST _)
 32: imm12 -> (INTCONST _)
 33: index -> imm8
 34: index -> imm12
 35: index -> regw
 36: _1 -> (INTCONST I32 1)
 37: sindex -> (LSHS I32 regw _1)
 38: _2 -> (INTCONST I32 2)
 39: sindex -> (LSHS I32 regw _2)
 40: _3 -> (INTCONST I32 3)
 41: sindex -> (LSHS I32 regw _3)
 42: _4 -> (INTCONST I32 4)
 43: sindex -> (LSHS I32 regw _4)
 44: _5 -> (INTCONST I32 5)
 45: sindex -> (LSHS I32 regw _5)
 46: _6 -> (INTCONST I32 6)
 47: sindex -> (LSHS I32 regw _6)
 48: _7 -> (INTCONST I32 7)
 49: sindex -> (LSHS I32 regw _7)
 50: _8 -> (INTCONST I32 8)
 51: sindex -> (LSHS I32 regw _8)
 52: addr2 -> base
 53: addr2 -> (ADD I32 base imm12)
 54: addr2 -> (ADD I32 base imm12)
 55: addr2 -> (SUB I32 base imm12)
 56: addr2 -> (ADD I32 base index)
 57: addr2 -> (SUB I32 base index)
 58: addr2 -> (ADD I32 base sindex)
 59: addr2 -> (SUB I32 base sindex)
 60: addr3 -> base
 61: addr3 -> (ADD I32 base imm8)
 62: addr3 -> (ADD I32 base imm8)
 63: addr3 -> (SUB I32 base imm8)
 64: memw -> (MEM I32 addr2)
 65: memw3 -> (MEM I32 addr3)
 66: memb -> (MEM I8 addr2)
 67: memh -> (MEM I16 addr3)
 68: memsb -> (MEM I8 addr3)
 69: regw -> sta
 70: regw -> asmcnst
 71: void -> (JUMP _ label)
 72: regb -> cbu
 73: regh -> ximm1
 74: regh -> ch
 75: regw -> cw
 76: regw -> ximm
 77: regb -> memb
 78: regh -> memh
 79: regw -> memw
 80: regw -> (CONVZX I32 memb)
 81: regw -> (CONVZX I32 memh)
 82: regw -> (CONVSX I32 memsb)
 83: regw -> (CONVSX I32 memh)
 84: regh -> (CONVZX I16 memb)
 85: regh -> (CONVSX I16 memsb)
 86: regb -> (CONVIT I8 memw3)
 87: regh -> (CONVIT I16 memw3)
 88: regb -> (CONVIT I8 memh)
 89: void -> (SET I32 memw regw)
 90: void -> (SET I16 memh regh)
 91: void -> (SET I8 memb regb)
 92: _9 -> (CONVIT I8 regw)
 93: void -> (SET I8 memb _9)
 94: _10 -> (CONVIT I8 regh)
 95: void -> (SET I8 memb _10)
 96: _11 -> (CONVIT I16 regw)
 97: void -> (SET I16 memh _11)
 98: void -> (SET I32 xregw regw)
 99: void -> (SET I16 xregh regh)
 100: void -> (SET I8 xregb regb)
 101: regw -> (CONVZX I32 regh)
 102: regw -> (CONVZX I32 regb)
 103: regh -> (CONVZX I16 regb)
 104: regw -> (CONVSX I32 regb)
 105: regw -> (CONVSX I32 regh)
 106: regh -> (CONVSX I16 regb)
 107: regb -> (CONVIT I8 regw)
 108: regb -> (CONVIT I8 regh)
 109: regh -> (CONVIT I16 regw)
 110: imm8r -> (INTCONST _)
 111: imm8rm -> (INTCONST _)
 112: imm8rn -> (INTCONST _)
 113: regw -> (ADD I32 regw xmimm)
 114: sregw -> (LSHS I32 regw _1)
 115: sregw -> (RSHS I32 regw _1)
 116: sregw -> (RSHU I32 regw _1)
 117: sregw -> (LSHS I32 regw _2)
 118: sregw -> (RSHS I32 regw _2)
 119: sregw -> (RSHU I32 regw _2)
 120: sregw -> (LSHS I32 regw _3)
 121: sregw -> (RSHS I32 regw _3)
 122: sregw -> (RSHU I32 regw _3)
 123: sregw -> (LSHS I32 regw _4)
 124: sregw -> (RSHS I32 regw _4)
 125: sregw -> (RSHU I32 regw _4)
 126: sregw -> (LSHS I32 regw _5)
 127: sregw -> (RSHS I32 regw _5)
 128: sregw -> (RSHU I32 regw _5)
 129: sregw -> (LSHS I32 regw _6)
 130: sregw -> (RSHS I32 regw _6)
 131: sregw -> (RSHU I32 regw _6)
 132: sregw -> (LSHS I32 regw _7)
 133: sregw -> (RSHS I32 regw _7)
 134: sregw -> (RSHU I32 regw _7)
 135: sregw -> (LSHS I32 regw _8)
 136: sregw -> (RSHS I32 regw _8)
 137: sregw -> (RSHU I32 regw _8)
 138: sregw -> (LSHS I32 regw regw)
 139: sregw -> (RSHS I32 regw regw)
 140: sregw -> (RSHU I32 regw regw)
 141: orop -> imm8r
 142: opop -> imm8r
 143: opop -> imm8rm
 144: logop -> imm8r
 145: logop -> imm8rn
 146: cmpop -> imm8r
 147: cmpop -> imm8rm
 148: orop -> regw
 149: orop -> sregw
 150: opop -> regw
 151: opop -> sregw
 152: logop -> regw
 153: logop -> sregw
 154: cmpop -> regw
 155: cmpop -> sregw
 156: regw -> (ADD I32 regw opop)
 157: regw -> (SUB I32 regw opop)
 158: regw -> (BAND I32 regw logop)
 159: regw -> (SUB I32 orop regw)
 160: regw -> (BOR I32 regw imm8rn)
 161: regw -> (BOR I32 regw orop)
 162: regw -> (BXOR I32 regw imm8rn)
 163: regw -> (BXOR I32 regw orop)
 164: regw -> (BOR I32 regw ximm)
 165: regw -> (BXOR I32 regw ximm)
 166: regw -> (ADD I32 regw ximm)
 167: regw -> (SUB I32 regw ximm)
 168: regw -> (BAND I32 regw ximm)
 169: regw -> (MUL I32 regw regw)
 170: regw -> (LSHS I32 regw regw)
 171: regw -> (RSHS I32 regw regw)
 172: regw -> (RSHU I32 regw regw)
 173: regw -> (BNOT I32 regw)
 174: regw -> (NEG I32 regw)
 175: regw -> (TSTEQ I32 regw cmpop)
 176: _12 -> (TSTEQ I32 regw cmpop)
 177: void -> (JUMPC _ _12 label label)
 178: _13 -> (TSTNE I32 regw cmpop)
 179: void -> (JUMPC _ _13 label label)
 180: _14 -> (TSTLTS I32 regw cmpop)
 181: void -> (JUMPC _ _14 label label)
 182: _15 -> (TSTLES I32 regw cmpop)
 183: void -> (JUMPC _ _15 label label)
 184: _16 -> (TSTGTS I32 regw cmpop)
 185: void -> (JUMPC _ _16 label label)
 186: _17 -> (TSTGES I32 regw cmpop)
 187: void -> (JUMPC _ _17 label label)
 188: _18 -> (TSTLTU I32 regw cmpop)
 189: void -> (JUMPC _ _18 label label)
 190: _19 -> (TSTLEU I32 regw cmpop)
 191: void -> (JUMPC _ _19 label label)
 192: _20 -> (TSTGTU I32 regw cmpop)
 193: void -> (JUMPC _ _20 label label)
 194: _21 -> (TSTGEU I32 regw cmpop)
 195: void -> (JUMPC _ _21 label label)
 196: void -> (CALL _ sta)
 197: void -> (CALL _ regw)
 198: void -> (CALL _ memw)
 199: cnstf -> (FLOATCONST _)
 200: cnstd -> (FLOATCONST _)
 201: memf -> (MEM F32 addr3)
 202: regf -> cnstf
 203: regf -> memf
 204: void -> (SET F32 memf regf)
 205: regf -> (NEG F32 regf)
 206: regf -> (ADD F32 regf regf)
 207: regf -> (SUB F32 regf regf)
 208: regf -> (MUL F32 regf regf)
 209: regf -> (DIVS F32 regf regf)
 210: _22 -> (TSTEQ I32 regf regf)
 211: void -> (JUMPC _ _22 label label)
 212: _23 -> (TSTNE I32 regf regf)
 213: void -> (JUMPC _ _23 label label)
 214: _24 -> (TSTLTS I32 regf regf)
 215: void -> (JUMPC _ _24 label label)
 216: _25 -> (TSTLES I32 regf regf)
 217: void -> (JUMPC _ _25 label label)
 218: _26 -> (TSTGTS I32 regf regf)
 219: void -> (JUMPC _ _26 label label)
 220: _27 -> (TSTGES I32 regf regf)
 221: void -> (JUMPC _ _27 label label)
 222: _28 -> (TSTLTU I32 regf regf)
 223: void -> (JUMPC _ _28 label label)
 224: _29 -> (TSTLEU I32 regf regf)
 225: void -> (JUMPC _ _29 label label)
 226: _30 -> (TSTGTU I32 regf regf)
 227: void -> (JUMPC _ _30 label label)
 228: _31 -> (TSTGEU I32 regf regf)
 229: void -> (JUMPC _ _31 label label)
 230: regw -> (CONVFS I32 regf)
 231: regh -> (CONVFS I16 regf)
 232: regb -> (CONVFS I8 regf)
 233: regf -> (CONVSF F32 regw)
 234: memd -> (MEM F64 addr3)
 235: regd -> cnstd
 236: regd -> memd
 237: void -> (SET F64 memd regd)
 238: regd -> (NEG F64 regd)
 239: regd -> (ADD F64 regd regd)
 240: regd -> (SUB F64 regd regd)
 241: regd -> (MUL F64 regd regd)
 242: regd -> (DIVS F64 regd regd)
 243: _32 -> (TSTEQ I32 regd regd)
 244: void -> (JUMPC _ _32 label label)
 245: _33 -> (TSTNE I32 regd regd)
 246: void -> (JUMPC _ _33 label label)
 247: _34 -> (TSTLTS I32 regd regd)
 248: void -> (JUMPC _ _34 label label)
 249: _35 -> (TSTLES I32 regd regd)
 250: void -> (JUMPC _ _35 label label)
 251: _36 -> (TSTGTS I32 regd regd)
 252: void -> (JUMPC _ _36 label label)
 253: _37 -> (TSTGES I32 regd regd)
 254: void -> (JUMPC _ _37 label label)
 255: _38 -> (TSTLTU I32 regd regd)
 256: void -> (JUMPC _ _38 label label)
 257: _39 -> (TSTLEU I32 regd regd)
 258: void -> (JUMPC _ _39 label label)
 259: _40 -> (TSTGTU I32 regd regd)
 260: void -> (JUMPC _ _40 label label)
 261: _41 -> (TSTGEU I32 regd regd)
 262: void -> (JUMPC _ _41 label label)
 263: regw -> (CONVFS I32 regd)
 264: regh -> (CONVFS I16 regd)
 265: regb -> (CONVFS I8 regd)
 266: regd -> (CONVSF F64 regw)
 267: regd -> (CONVFX F64 regf)
 268: regf -> (CONVFT F32 regd)
 269: void -> (SET F64 xregd regd)
 270: void -> (SET F32 xregf regf)
 271: _42 -> (REG I32 "%sp")
 272: _43 -> (SUB I32 _42 _4)
 273: _44 -> (SET I32 _42 _43)
 274: _45 -> (MEM F32 _42)
 275: _46 -> (SET F32 _45 regf)
 276: void -> (PARALLEL _ _44 _46)
 277: _47 -> (SUB I32 _42 _8)
 278: _48 -> (SET I32 _42 _47)
 279: _49 -> (MEM F64 _42)
 280: _50 -> (SET F64 _49 regd)
 281: void -> (PARALLEL _ _48 _50)
 282: _51 -> (MEM I32 _42)
 283: _52 -> (SET I32 regw _51)
 284: _53 -> (ADD I32 _42 _4)
 285: _54 -> (SET I32 _42 _53)
 286: void -> (PARALLEL _ _52 _54)
 287: _55 -> (MEM I32 _53)
 288: _56 -> (SET I32 regw _55)
 289: _57 -> (ADD I32 _42 _8)
 290: _58 -> (SET I32 _42 _57)
 291: void -> (PARALLEL _ _52 _56 _58)
 292: void -> (SET F32 memf memf)
*/
/*
Sorted Productions:
 30: base -> regw
 35: index -> regw
 148: orop -> regw
 150: opop -> regw
 152: logop -> regw
 154: cmpop -> regw
 4: regw -> xregw
 7: regh -> xregh
 10: regb -> xregb
 13: regf -> xregf
 16: regd -> xregd
 18: cnst -> cbu
 72: regb -> cbu
 24: asmcnst -> cnst
 20: cnst -> ch
 74: regh -> ch
 22: cnst -> cw
 75: regw -> cw
 69: regw -> sta
 70: regw -> asmcnst
 73: regh -> ximm1
 76: regw -> ximm
 52: addr2 -> base
 60: addr3 -> base
 33: index -> imm8
 34: index -> imm12
 79: regw -> memw
 77: regb -> memb
 78: regh -> memh
 141: orop -> imm8r
 142: opop -> imm8r
 144: logop -> imm8r
 146: cmpop -> imm8r
 143: opop -> imm8rm
 147: cmpop -> imm8rm
 145: logop -> imm8rn
 149: orop -> sregw
 151: opop -> sregw
 153: logop -> sregw
 155: cmpop -> sregw
 202: regf -> cnstf
 235: regd -> cnstd
 203: regf -> memf
 236: regd -> memd
 17: cbu -> (INTCONST _)
 19: ch -> (INTCONST _)
 21: cw -> (INTCONST _)
 27: ximm1 -> (INTCONST _)
 28: xmimm -> (INTCONST _)
 29: ximm -> (INTCONST _)
 31: imm8 -> (INTCONST _)
 32: imm12 -> (INTCONST _)
 110: imm8r -> (INTCONST _)
 111: imm8rm -> (INTCONST _)
 112: imm8rn -> (INTCONST _)
 36: _1 -> (INTCONST I32 1)
 38: _2 -> (INTCONST I32 2)
 40: _3 -> (INTCONST I32 3)
 42: _4 -> (INTCONST I32 4)
 44: _5 -> (INTCONST I32 5)
 46: _6 -> (INTCONST I32 6)
 48: _7 -> (INTCONST I32 7)
 50: _8 -> (INTCONST I32 8)
 199: cnstf -> (FLOATCONST _)
 200: cnstd -> (FLOATCONST _)
 23: sta -> (STATIC I32)
 8: xregb -> (REG I8)
 5: xregh -> (REG I16)
 2: xregw -> (REG I32)
 271: _42 -> (REG I32 "%sp")
 11: xregf -> (REG F32)
 14: xregd -> (REG F64)
 9: xregb -> (SUBREG I8)
 6: xregh -> (SUBREG I16)
 3: xregw -> (SUBREG I32)
 12: xregf -> (SUBREG F32)
 15: xregd -> (SUBREG F64)
 1: label -> (LABEL _)
 174: regw -> (NEG I32 regw)
 205: regf -> (NEG F32 regf)
 238: regd -> (NEG F64 regd)
 25: asmcnst -> (ADD I32 asmcnst cnst)
 53: addr2 -> (ADD I32 base imm12)
 54: addr2 -> (ADD I32 base imm12)
 56: addr2 -> (ADD I32 base index)
 58: addr2 -> (ADD I32 base sindex)
 61: addr3 -> (ADD I32 base imm8)
 62: addr3 -> (ADD I32 base imm8)
 113: regw -> (ADD I32 regw xmimm)
 156: regw -> (ADD I32 regw opop)
 166: regw -> (ADD I32 regw ximm)
 284: _53 -> (ADD I32 _42 _4)
 289: _57 -> (ADD I32 _42 _8)
 206: regf -> (ADD F32 regf regf)
 239: regd -> (ADD F64 regd regd)
 26: asmcnst -> (SUB I32 asmcnst cnst)
 55: addr2 -> (SUB I32 base imm12)
 57: addr2 -> (SUB I32 base index)
 59: addr2 -> (SUB I32 base sindex)
 63: addr3 -> (SUB I32 base imm8)
 157: regw -> (SUB I32 regw opop)
 159: regw -> (SUB I32 orop regw)
 167: regw -> (SUB I32 regw ximm)
 272: _43 -> (SUB I32 _42 _4)
 277: _47 -> (SUB I32 _42 _8)
 207: regf -> (SUB F32 regf regf)
 240: regd -> (SUB F64 regd regd)
 169: regw -> (MUL I32 regw regw)
 208: regf -> (MUL F32 regf regf)
 241: regd -> (MUL F64 regd regd)
 209: regf -> (DIVS F32 regf regf)
 242: regd -> (DIVS F64 regd regd)
 85: regh -> (CONVSX I16 memsb)
 106: regh -> (CONVSX I16 regb)
 82: regw -> (CONVSX I32 memsb)
 83: regw -> (CONVSX I32 memh)
 104: regw -> (CONVSX I32 regb)
 105: regw -> (CONVSX I32 regh)
 84: regh -> (CONVZX I16 memb)
 103: regh -> (CONVZX I16 regb)
 80: regw -> (CONVZX I32 memb)
 81: regw -> (CONVZX I32 memh)
 101: regw -> (CONVZX I32 regh)
 102: regw -> (CONVZX I32 regb)
 86: regb -> (CONVIT I8 memw3)
 88: regb -> (CONVIT I8 memh)
 92: _9 -> (CONVIT I8 regw)
 94: _10 -> (CONVIT I8 regh)
 107: regb -> (CONVIT I8 regw)
 108: regb -> (CONVIT I8 regh)
 87: regh -> (CONVIT I16 memw3)
 96: _11 -> (CONVIT I16 regw)
 109: regh -> (CONVIT I16 regw)
 267: regd -> (CONVFX F64 regf)
 268: regf -> (CONVFT F32 regd)
 232: regb -> (CONVFS I8 regf)
 265: regb -> (CONVFS I8 regd)
 231: regh -> (CONVFS I16 regf)
 264: regh -> (CONVFS I16 regd)
 230: regw -> (CONVFS I32 regf)
 263: regw -> (CONVFS I32 regd)
 233: regf -> (CONVSF F32 regw)
 266: regd -> (CONVSF F64 regw)
 158: regw -> (BAND I32 regw logop)
 168: regw -> (BAND I32 regw ximm)
 160: regw -> (BOR I32 regw imm8rn)
 161: regw -> (BOR I32 regw orop)
 164: regw -> (BOR I32 regw ximm)
 162: regw -> (BXOR I32 regw imm8rn)
 163: regw -> (BXOR I32 regw orop)
 165: regw -> (BXOR I32 regw ximm)
 173: regw -> (BNOT I32 regw)
 37: sindex -> (LSHS I32 regw _1)
 39: sindex -> (LSHS I32 regw _2)
 41: sindex -> (LSHS I32 regw _3)
 43: sindex -> (LSHS I32 regw _4)
 45: sindex -> (LSHS I32 regw _5)
 47: sindex -> (LSHS I32 regw _6)
 49: sindex -> (LSHS I32 regw _7)
 51: sindex -> (LSHS I32 regw _8)
 114: sregw -> (LSHS I32 regw _1)
 117: sregw -> (LSHS I32 regw _2)
 120: sregw -> (LSHS I32 regw _3)
 123: sregw -> (LSHS I32 regw _4)
 126: sregw -> (LSHS I32 regw _5)
 129: sregw -> (LSHS I32 regw _6)
 132: sregw -> (LSHS I32 regw _7)
 135: sregw -> (LSHS I32 regw _8)
 138: sregw -> (LSHS I32 regw regw)
 170: regw -> (LSHS I32 regw regw)
 115: sregw -> (RSHS I32 regw _1)
 118: sregw -> (RSHS I32 regw _2)
 121: sregw -> (RSHS I32 regw _3)
 124: sregw -> (RSHS I32 regw _4)
 127: sregw -> (RSHS I32 regw _5)
 130: sregw -> (RSHS I32 regw _6)
 133: sregw -> (RSHS I32 regw _7)
 136: sregw -> (RSHS I32 regw _8)
 139: sregw -> (RSHS I32 regw regw)
 171: regw -> (RSHS I32 regw regw)
 116: sregw -> (RSHU I32 regw _1)
 119: sregw -> (RSHU I32 regw _2)
 122: sregw -> (RSHU I32 regw _3)
 125: sregw -> (RSHU I32 regw _4)
 128: sregw -> (RSHU I32 regw _5)
 131: sregw -> (RSHU I32 regw _6)
 134: sregw -> (RSHU I32 regw _7)
 137: sregw -> (RSHU I32 regw _8)
 140: sregw -> (RSHU I32 regw regw)
 172: regw -> (RSHU I32 regw regw)
 175: regw -> (TSTEQ I32 regw cmpop)
 176: _12 -> (TSTEQ I32 regw cmpop)
 210: _22 -> (TSTEQ I32 regf regf)
 243: _32 -> (TSTEQ I32 regd regd)
 178: _13 -> (TSTNE I32 regw cmpop)
 212: _23 -> (TSTNE I32 regf regf)
 245: _33 -> (TSTNE I32 regd regd)
 180: _14 -> (TSTLTS I32 regw cmpop)
 214: _24 -> (TSTLTS I32 regf regf)
 247: _34 -> (TSTLTS I32 regd regd)
 182: _15 -> (TSTLES I32 regw cmpop)
 216: _25 -> (TSTLES I32 regf regf)
 249: _35 -> (TSTLES I32 regd regd)
 184: _16 -> (TSTGTS I32 regw cmpop)
 218: _26 -> (TSTGTS I32 regf regf)
 251: _36 -> (TSTGTS I32 regd regd)
 186: _17 -> (TSTGES I32 regw cmpop)
 220: _27 -> (TSTGES I32 regf regf)
 253: _37 -> (TSTGES I32 regd regd)
 188: _18 -> (TSTLTU I32 regw cmpop)
 222: _28 -> (TSTLTU I32 regf regf)
 255: _38 -> (TSTLTU I32 regd regd)
 190: _19 -> (TSTLEU I32 regw cmpop)
 224: _29 -> (TSTLEU I32 regf regf)
 257: _39 -> (TSTLEU I32 regd regd)
 192: _20 -> (TSTGTU I32 regw cmpop)
 226: _30 -> (TSTGTU I32 regf regf)
 259: _40 -> (TSTGTU I32 regd regd)
 194: _21 -> (TSTGEU I32 regw cmpop)
 228: _31 -> (TSTGEU I32 regf regf)
 261: _41 -> (TSTGEU I32 regd regd)
 66: memb -> (MEM I8 addr2)
 68: memsb -> (MEM I8 addr3)
 67: memh -> (MEM I16 addr3)
 64: memw -> (MEM I32 addr2)
 65: memw3 -> (MEM I32 addr3)
 282: _51 -> (MEM I32 _42)
 287: _55 -> (MEM I32 _53)
 201: memf -> (MEM F32 addr3)
 274: _45 -> (MEM F32 _42)
 234: memd -> (MEM F64 addr3)
 279: _49 -> (MEM F64 _42)
 91: void -> (SET I8 memb regb)
 93: void -> (SET I8 memb _9)
 95: void -> (SET I8 memb _10)
 100: void -> (SET I8 xregb regb)
 90: void -> (SET I16 memh regh)
 97: void -> (SET I16 memh _11)
 99: void -> (SET I16 xregh regh)
 89: void -> (SET I32 memw regw)
 98: void -> (SET I32 xregw regw)
 273: _44 -> (SET I32 _42 _43)
 278: _48 -> (SET I32 _42 _47)
 283: _52 -> (SET I32 regw _51)
 285: _54 -> (SET I32 _42 _53)
 288: _56 -> (SET I32 regw _55)
 290: _58 -> (SET I32 _42 _57)
 204: void -> (SET F32 memf regf)
 270: void -> (SET F32 xregf regf)
 275: _46 -> (SET F32 _45 regf)
 292: void -> (SET F32 memf memf)
 237: void -> (SET F64 memd regd)
 269: void -> (SET F64 xregd regd)
 280: _50 -> (SET F64 _49 regd)
 71: void -> (JUMP _ label)
 177: void -> (JUMPC _ _12 label label)
 179: void -> (JUMPC _ _13 label label)
 181: void -> (JUMPC _ _14 label label)
 183: void -> (JUMPC _ _15 label label)
 185: void -> (JUMPC _ _16 label label)
 187: void -> (JUMPC _ _17 label label)
 189: void -> (JUMPC _ _18 label label)
 191: void -> (JUMPC _ _19 label label)
 193: void -> (JUMPC _ _20 label label)
 195: void -> (JUMPC _ _21 label label)
 211: void -> (JUMPC _ _22 label label)
 213: void -> (JUMPC _ _23 label label)
 215: void -> (JUMPC _ _24 label label)
 217: void -> (JUMPC _ _25 label label)
 219: void -> (JUMPC _ _26 label label)
 221: void -> (JUMPC _ _27 label label)
 223: void -> (JUMPC _ _28 label label)
 225: void -> (JUMPC _ _29 label label)
 227: void -> (JUMPC _ _30 label label)
 229: void -> (JUMPC _ _31 label label)
 244: void -> (JUMPC _ _32 label label)
 246: void -> (JUMPC _ _33 label label)
 248: void -> (JUMPC _ _34 label label)
 250: void -> (JUMPC _ _35 label label)
 252: void -> (JUMPC _ _36 label label)
 254: void -> (JUMPC _ _37 label label)
 256: void -> (JUMPC _ _38 label label)
 258: void -> (JUMPC _ _39 label label)
 260: void -> (JUMPC _ _40 label label)
 262: void -> (JUMPC _ _41 label label)
 196: void -> (CALL _ sta)
 197: void -> (CALL _ regw)
 198: void -> (CALL _ memw)
 276: void -> (PARALLEL _ _44 _46)
 281: void -> (PARALLEL _ _48 _50)
 286: void -> (PARALLEL _ _52 _54)
 291: void -> (PARALLEL _ _52 _56 _58)
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
 12: _rewr -> (MODS _ _)
 13: _rewr -> (MODU _ _)
 14: _rewr -> (DIVS _ _)
 15: _rewr -> (DIVU _ _)
 16: _rewr -> (JUMPN _)
 17: _rewr -> (SET _)
*/
/*
Sorted Productions:
 7: _1 -> (STATIC I32 "__builtin_va_start")
 10: _3 -> (STATIC I32 "alloca")
 14: _rewr -> (DIVS _ _)
 15: _rewr -> (DIVU _ _)
 12: _rewr -> (MODS _ _)
 13: _rewr -> (MODU _ _)
 2: _rewr -> (CONVFU _ _)
 1: _rewr -> (CONVUF _ _)
 17: _rewr -> (SET _)
 16: _rewr -> (JUMPN _)
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


import coins.backend.asmpp.LiteralAndBranchProcessor;
import coins.backend.asmpp.CPU;
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
import java.io.OutputStreamWriter;
import coins.backend.cfg.BasicBlk;
import coins.backend.cfg.FlowGraph;	
import coins.backend.lir.LirLabelRef;

import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompileThread;
import coins.IoRoot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import coins.backend.ana.SaveRegisters;
import coins.backend.util.NumberSet;

import coins.backend.LocalTransformer;
import coins.backend.Transformer;
import coins.backend.Data;




public class CodeGenerator_arm extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_arm() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 5;
    static final int NRULES = 17 + 1;
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
          if (((LirSymRef)t).symbol.name == "__builtin_va_start") record(NT__1, 7);
          if (((LirSymRef)t).symbol.name == "alloca") record(NT__3, 10);
        }
        break;
      case Op.DIVS:
        if (phase == "early") if (Type.tag(t.type) == Type.INT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__divsi3", 2));
        }
        break;
      case Op.DIVU:
        if (phase == "early") if (Type.tag(t.type) == Type.INT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__udivsi3", 2));
        }
        break;
      case Op.MODS:
        if (phase == "early") if (Type.tag(t.type) == Type.INT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__modsi3", 2));
        }
        break;
      case Op.MODU:
        if (phase == "early") if (Type.tag(t.type) == Type.INT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__umodsi3", 2));
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
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.ADD, 514, lir.node(Op.REG, 514, func.getSymbol("%fp")), lir.iconst(514, makeVaStart(t.kid(1).kid(0)))));
        }
        if (kids[0].rule[NT__3] != 0) if (kids[1].rule[NT__2] != 0) if (kids[2].rule[NT__2] != 0) if (phase == "early")  {
          rewritten = true;
          {
          pre.add(lir.node(Op.SET, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.SUB, 514, lir.node(Op.REG, 514, func.getSymbol("%sp")), lir.node(Op.BAND, 514, lir.node(Op.ADD, 514, t.kid(1).kid(0), lir.iconst(514, 3)), lir.iconst(514, -4)))));
          }
          return lir.node(Op.SET, 514, t.kid(2).kid(0), lir.node(Op.REG, 514, func.getSymbol("%sp")));
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
    static final int NNONTERM = 104;
    static final int NRULES = 292 + 1;
    static final int START_NT = 6;

    static final int NT__ = 0;
    static final int NT_regw = 1;
    static final int NT_regh = 2;
    static final int NT_regb = 3;
    static final int NT_regf = 4;
    static final int NT_regd = 5;
    static final int NT_void = 6;
    static final int NT_label = 7;
    static final int NT_xregw = 8;
    static final int NT_xregh = 9;
    static final int NT_xregb = 10;
    static final int NT_xregf = 11;
    static final int NT_xregd = 12;
    static final int NT_cbu = 13;
    static final int NT_cnst = 14;
    static final int NT_ch = 15;
    static final int NT_cw = 16;
    static final int NT_sta = 17;
    static final int NT_asmcnst = 18;
    static final int NT_ximm1 = 19;
    static final int NT_xmimm = 20;
    static final int NT_ximm = 21;
    static final int NT_base = 22;
    static final int NT_imm8 = 23;
    static final int NT_imm12 = 24;
    static final int NT_index = 25;
    static final int NT_sindex = 26;
    static final int NT__1 = 27;
    static final int NT__2 = 28;
    static final int NT__3 = 29;
    static final int NT__4 = 30;
    static final int NT__5 = 31;
    static final int NT__6 = 32;
    static final int NT__7 = 33;
    static final int NT__8 = 34;
    static final int NT_addr2 = 35;
    static final int NT_addr3 = 36;
    static final int NT_memw = 37;
    static final int NT_memw3 = 38;
    static final int NT_memb = 39;
    static final int NT_memh = 40;
    static final int NT_memsb = 41;
    static final int NT__9 = 42;
    static final int NT__10 = 43;
    static final int NT__11 = 44;
    static final int NT_imm8r = 45;
    static final int NT_imm8rm = 46;
    static final int NT_imm8rn = 47;
    static final int NT_sregw = 48;
    static final int NT_orop = 49;
    static final int NT_opop = 50;
    static final int NT_logop = 51;
    static final int NT_cmpop = 52;
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
    static final int NT_cnstf = 63;
    static final int NT_cnstd = 64;
    static final int NT_memf = 65;
    static final int NT__22 = 66;
    static final int NT__23 = 67;
    static final int NT__24 = 68;
    static final int NT__25 = 69;
    static final int NT__26 = 70;
    static final int NT__27 = 71;
    static final int NT__28 = 72;
    static final int NT__29 = 73;
    static final int NT__30 = 74;
    static final int NT__31 = 75;
    static final int NT_memd = 76;
    static final int NT__32 = 77;
    static final int NT__33 = 78;
    static final int NT__34 = 79;
    static final int NT__35 = 80;
    static final int NT__36 = 81;
    static final int NT__37 = 82;
    static final int NT__38 = 83;
    static final int NT__39 = 84;
    static final int NT__40 = 85;
    static final int NT__41 = 86;
    static final int NT__42 = 87;
    static final int NT__43 = 88;
    static final int NT__44 = 89;
    static final int NT__45 = 90;
    static final int NT__46 = 91;
    static final int NT__47 = 92;
    static final int NT__48 = 93;
    static final int NT__49 = 94;
    static final int NT__50 = 95;
    static final int NT__51 = 96;
    static final int NT__52 = 97;
    static final int NT__53 = 98;
    static final int NT__54 = 99;
    static final int NT__55 = 100;
    static final int NT__56 = 101;
    static final int NT__57 = 102;
    static final int NT__58 = 103;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regw: return "regw";
      case NT_regh: return "regh";
      case NT_regb: return "regb";
      case NT_regf: return "regf";
      case NT_regd: return "regd";
      case NT_void: return "void";
      case NT_label: return "label";
      case NT_xregw: return "xregw";
      case NT_xregh: return "xregh";
      case NT_xregb: return "xregb";
      case NT_xregf: return "xregf";
      case NT_xregd: return "xregd";
      case NT_cbu: return "cbu";
      case NT_cnst: return "cnst";
      case NT_ch: return "ch";
      case NT_cw: return "cw";
      case NT_sta: return "sta";
      case NT_asmcnst: return "asmcnst";
      case NT_ximm1: return "ximm1";
      case NT_xmimm: return "xmimm";
      case NT_ximm: return "ximm";
      case NT_base: return "base";
      case NT_imm8: return "imm8";
      case NT_imm12: return "imm12";
      case NT_index: return "index";
      case NT_sindex: return "sindex";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT__4: return "_4";
      case NT__5: return "_5";
      case NT__6: return "_6";
      case NT__7: return "_7";
      case NT__8: return "_8";
      case NT_addr2: return "addr2";
      case NT_addr3: return "addr3";
      case NT_memw: return "memw";
      case NT_memw3: return "memw3";
      case NT_memb: return "memb";
      case NT_memh: return "memh";
      case NT_memsb: return "memsb";
      case NT__9: return "_9";
      case NT__10: return "_10";
      case NT__11: return "_11";
      case NT_imm8r: return "imm8r";
      case NT_imm8rm: return "imm8rm";
      case NT_imm8rn: return "imm8rn";
      case NT_sregw: return "sregw";
      case NT_orop: return "orop";
      case NT_opop: return "opop";
      case NT_logop: return "logop";
      case NT_cmpop: return "cmpop";
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
      case NT_cnstf: return "cnstf";
      case NT_cnstd: return "cnstd";
      case NT_memf: return "memf";
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
      case NT_memd: return "memd";
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
          record(NT_base, 0 + cost1, 0 + cost2, 30);
          record(NT_index, 0 + cost1, 0 + cost2, 35);
          record(NT_orop, 0 + cost1, 0 + cost2, 148);
          record(NT_opop, 0 + cost1, 0 + cost2, 150);
          record(NT_logop, 0 + cost1, 0 + cost2, 152);
          record(NT_cmpop, 0 + cost1, 0 + cost2, 154);
          break;
        case NT_xregw:
          record(NT_regw, 0 + cost1, 0 + cost2, 4);
          break;
        case NT_xregh:
          record(NT_regh, 0 + cost1, 0 + cost2, 7);
          break;
        case NT_xregb:
          record(NT_regb, 0 + cost1, 0 + cost2, 10);
          break;
        case NT_xregf:
          record(NT_regf, 0 + cost1, 0 + cost2, 13);
          break;
        case NT_xregd:
          record(NT_regd, 0 + cost1, 0 + cost2, 16);
          break;
        case NT_cbu:
          record(NT_cnst, 0 + cost1, 0 + cost2, 18);
          record(NT_regb, 1 + cost1, 1 + cost2, 72);
          break;
        case NT_cnst:
          record(NT_asmcnst, 0 + cost1, 0 + cost2, 24);
          break;
        case NT_ch:
          record(NT_cnst, 0 + cost1, 0 + cost2, 20);
          record(NT_regh, 2 + cost1, 2 + cost2, 74);
          break;
        case NT_cw:
          record(NT_cnst, 0 + cost1, 0 + cost2, 22);
          record(NT_regw, 2 + cost1, 1 + cost2, 75);
          break;
        case NT_sta:
          record(NT_regw, 2 + cost1, 1 + cost2, 69);
          break;
        case NT_asmcnst:
          record(NT_regw, 2 + cost1, 1 + cost2, 70);
          break;
        case NT_ximm1:
          record(NT_regh, 1 + cost1, 1 + cost2, 73);
          break;
        case NT_ximm:
          record(NT_regw, 1 + cost1, 1 + cost2, 76);
          break;
        case NT_base:
          record(NT_addr2, 0 + cost1, 0 + cost2, 52);
          record(NT_addr3, 0 + cost1, 0 + cost2, 60);
          break;
        case NT_imm8:
          record(NT_index, 0 + cost1, 0 + cost2, 33);
          break;
        case NT_imm12:
          record(NT_index, 0 + cost1, 0 + cost2, 34);
          break;
        case NT_memw:
          record(NT_regw, 2 + cost1, 1 + cost2, 79);
          break;
        case NT_memb:
          record(NT_regb, 3 + cost1, 1 + cost2, 77);
          break;
        case NT_memh:
          record(NT_regh, 3 + cost1, 1 + cost2, 78);
          break;
        case NT_imm8r:
          record(NT_orop, 0 + cost1, 0 + cost2, 141);
          record(NT_opop, 0 + cost1, 0 + cost2, 142);
          record(NT_logop, 0 + cost1, 0 + cost2, 144);
          record(NT_cmpop, 0 + cost1, 0 + cost2, 146);
          break;
        case NT_imm8rm:
          record(NT_opop, 0 + cost1, 0 + cost2, 143);
          record(NT_cmpop, 0 + cost1, 0 + cost2, 147);
          break;
        case NT_imm8rn:
          record(NT_logop, 0 + cost1, 0 + cost2, 145);
          break;
        case NT_sregw:
          record(NT_orop, 0 + cost1, 0 + cost2, 149);
          record(NT_opop, 0 + cost1, 0 + cost2, 151);
          record(NT_logop, 0 + cost1, 0 + cost2, 153);
          record(NT_cmpop, 0 + cost1, 0 + cost2, 155);
          break;
        case NT_cnstf:
          record(NT_regf, 4 + cost1, 4 + cost2, 202);
          break;
        case NT_cnstd:
          record(NT_regd, 4 + cost1, 4 + cost2, 235);
          break;
        case NT_memf:
          record(NT_regf, 4 + cost1, 4 + cost2, 203);
          break;
        case NT_memd:
          record(NT_regd, 4 + cost1, 4 + cost2, 236);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        if (Type.bits(((LirIconst)t).type) == 8) record(NT_cbu, 0, 0, 17);
        if (Type.bits(((LirIconst)t).type) == 16) record(NT_ch, 0, 0, 19);
        if (Type.bits(((LirIconst)t).type) == 32) record(NT_cw, 0, 0, 21);
        if (nofBytes(((LirIconst)t).signedValue()) == 1) record(NT_ximm1, 0, 0, 27);
        if (nofBytes(-((LirIconst)t).signedValue()) <= 4) record(NT_xmimm, 0, 0, 28);
        record(NT_ximm, 0, 0, 29);
        if (-255 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 255) record(NT_imm8, 0, 0, 31);
        if (-4095 <= ((LirIconst)t).signedValue() && ((LirIconst)t).signedValue() <= 4095) record(NT_imm12, 0, 0, 32);
        if (isImm8r(((LirIconst)t).signedValue())) record(NT_imm8r, 0, 0, 110);
        if (isImm8r(-((LirIconst)t).signedValue())) record(NT_imm8rm, 0, 0, 111);
        if (isImm8r(~((LirIconst)t).signedValue())) record(NT_imm8rn, 0, 0, 112);
        if (t.type == 514) {
          if (((LirIconst)t).value == 1) record(NT__1, 0, 0, 36);
          if (((LirIconst)t).value == 2) record(NT__2, 0, 0, 38);
          if (((LirIconst)t).value == 3) record(NT__3, 0, 0, 40);
          if (((LirIconst)t).value == 4) record(NT__4, 0, 0, 42);
          if (((LirIconst)t).value == 5) record(NT__5, 0, 0, 44);
          if (((LirIconst)t).value == 6) record(NT__6, 0, 0, 46);
          if (((LirIconst)t).value == 7) record(NT__7, 0, 0, 48);
          if (((LirIconst)t).value == 8) record(NT__8, 0, 0, 50);
        }
        break;
      case Op.FLOATCONST:
        if (Type.bits(((LirFconst)t).type) == 32) record(NT_cnstf, 0, 0, 199);
        if (Type.bits(((LirFconst)t).type) == 64) record(NT_cnstd, 0, 0, 200);
        break;
      case Op.STATIC:
        if (t.type == 514) {
          record(NT_sta, 0, 0, 23);
        }
        break;
      case Op.REG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 8);
        }
        if (t.type == 258) {
          record(NT_xregh, 0, 0, 5);
        }
        if (t.type == 514) {
          record(NT_xregw, 0, 0, 2);
          if (((LirSymRef)t).symbol.name == "%sp") record(NT__42, 0, 0, 271);
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 11);
        }
        if (t.type == 1028) {
          record(NT_xregd, 0, 0, 14);
        }
        break;
      case Op.SUBREG:
        if (t.type == 130) {
          record(NT_xregb, 0, 0, 9);
        }
        if (t.type == 258) {
          record(NT_xregh, 0, 0, 6);
        }
        if (t.type == 514) {
          record(NT_xregw, 0, 0, 3);
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 12);
        }
        if (t.type == 1028) {
          record(NT_xregd, 0, 0, 15);
        }
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 1);
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 174);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 205);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 238);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_cnst] != 0) record(NT_asmcnst, 0 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_cnst], 0 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_cnst], 25);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_imm12] != 0) if (isVirtual(t.kid(0))) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_imm12], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_imm12], 53);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_imm12] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_imm12], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_imm12], 54);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_index] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_index], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_index], 56);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_sindex] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_sindex], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_sindex], 58);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_imm8] != 0) if (isVirtual(t.kid(0))) record(NT_addr3, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_imm8], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_imm8], 61);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_imm8] != 0) record(NT_addr3, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_imm8], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_imm8], 62);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_xmimm] != 0) if (isVirtual(t.kid(0))) record(NT_regw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_xmimm], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_xmimm], 113);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_opop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_opop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_opop], 156);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_ximm] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_ximm], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_ximm], 166);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__4] != 0) record(NT__53, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__4], 284);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__8] != 0) record(NT__57, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__8], 289);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 3 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 206);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 3 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 239);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_cnst] != 0) record(NT_asmcnst, 0 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_cnst], 0 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_cnst], 26);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_imm12] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_imm12], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_imm12], 55);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_index] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_index], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_index], 57);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_sindex] != 0) record(NT_addr2, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_sindex], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_sindex], 59);
          if (kids[0].rule[NT_base] != 0) if (kids[1].rule[NT_imm8] != 0) record(NT_addr3, 0 + kids[0].cost1[NT_base] + kids[1].cost1[NT_imm8], 0 + kids[0].cost2[NT_base] + kids[1].cost2[NT_imm8], 63);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_opop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_opop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_opop], 157);
          if (kids[0].rule[NT_orop] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_orop] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_orop] + kids[1].cost2[NT_regw], 159);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_ximm] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_ximm], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_ximm], 167);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__4] != 0) record(NT__43, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__4], 272);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__8] != 0) record(NT__47, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__8], 277);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 3 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 207);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 3 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 240);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 4 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 169);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 3 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 208);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 3 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 241);
        }
        break;
      case Op.DIVS:
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 3 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 3 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 209);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_regd, 3 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 3 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 242);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_memsb] != 0) record(NT_regh, 3 + kids[0].cost1[NT_memsb], 1 + kids[0].cost2[NT_memsb], 85);
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 3 + kids[0].cost1[NT_regb], 3 + kids[0].cost2[NT_regb], 106);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_memsb] != 0) record(NT_regw, 3 + kids[0].cost1[NT_memsb], 1 + kids[0].cost2[NT_memsb], 82);
          if (kids[0].rule[NT_memh] != 0) record(NT_regw, 3 + kids[0].cost1[NT_memh], 1 + kids[0].cost2[NT_memh], 83);
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regb], 2 + kids[0].cost2[NT_regb], 104);
          if (kids[0].rule[NT_regh] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regh], 2 + kids[0].cost2[NT_regh], 105);
        }
        break;
      case Op.CONVZX:
        if (t.type == 258) {
          if (kids[0].rule[NT_memb] != 0) record(NT_regh, 3 + kids[0].cost1[NT_memb], 1 + kids[0].cost2[NT_memb], 84);
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 103);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_memb] != 0) record(NT_regw, 3 + kids[0].cost1[NT_memb], 1 + kids[0].cost2[NT_memb], 80);
          if (kids[0].rule[NT_memh] != 0) record(NT_regw, 3 + kids[0].cost1[NT_memh], 1 + kids[0].cost2[NT_memh], 81);
          if (kids[0].rule[NT_regh] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regh], 1 + kids[0].cost2[NT_regh], 101);
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 102);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_memw3] != 0) record(NT_regb, 3 + kids[0].cost1[NT_memw3], 1 + kids[0].cost2[NT_memw3], 86);
          if (kids[0].rule[NT_memh] != 0) record(NT_regb, 3 + kids[0].cost1[NT_memh], 1 + kids[0].cost2[NT_memh], 88);
          if (kids[0].rule[NT_regw] != 0) record(NT__9, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 92);
          if (kids[0].rule[NT_regh] != 0) record(NT__10, 0 + kids[0].cost1[NT_regh], 0 + kids[0].cost2[NT_regh], 94);
          if (kids[0].rule[NT_regw] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 107);
          if (kids[0].rule[NT_regh] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regh], 1 + kids[0].cost2[NT_regh], 108);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_memw3] != 0) record(NT_regh, 3 + kids[0].cost1[NT_memw3], 1 + kids[0].cost2[NT_memw3], 87);
          if (kids[0].rule[NT_regw] != 0) record(NT__11, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 96);
          if (kids[0].rule[NT_regw] != 0) record(NT_regh, 2 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 109);
        }
        break;
      case Op.CONVFX:
        if (t.type == 1028) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 267);
        }
        break;
      case Op.CONVFT:
        if (t.type == 516) {
          if (kids[0].rule[NT_regd] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 268);
        }
        break;
      case Op.CONVFS:
        if (t.type == 130) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regf], 2 + kids[0].cost2[NT_regf], 232);
          if (kids[0].rule[NT_regd] != 0) record(NT_regb, 2 + kids[0].cost1[NT_regd], 2 + kids[0].cost2[NT_regd], 265);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regh, 3 + kids[0].cost1[NT_regf], 3 + kids[0].cost2[NT_regf], 231);
          if (kids[0].rule[NT_regd] != 0) record(NT_regh, 3 + kids[0].cost1[NT_regd], 3 + kids[0].cost2[NT_regd], 264);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 230);
          if (kids[0].rule[NT_regd] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regd], 1 + kids[0].cost2[NT_regd], 263);
        }
        break;
      case Op.CONVSF:
        if (t.type == 516) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 233);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regd, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 266);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_logop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_logop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_logop], 158);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_ximm] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_ximm], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_ximm], 168);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_imm8rn] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_imm8rn], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_imm8rn], 160);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_orop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_orop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_orop], 161);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_ximm] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_ximm], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_ximm], 164);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_imm8rn] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_imm8rn], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_imm8rn], 162);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_orop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_orop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_orop], 163);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_ximm] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_ximm], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_ximm], 165);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 173);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__1] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__1], 37);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__2] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__2], 39);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__3] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__3], 41);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__4] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__4], 43);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__5] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__5], 45);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__6] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__6], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__6], 47);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__7] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__7], 49);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__8] != 0) record(NT_sindex, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__8], 51);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__1] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__1], 114);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__2] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__2], 117);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__3] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__3], 120);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__4] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__4], 123);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__5] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__5], 126);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__6] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__6], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__6], 129);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__7] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__7], 132);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__8] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__8], 135);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_sregw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 138);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 170);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__1] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__1], 115);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__2] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__2], 118);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__3] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__3], 121);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__4] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__4], 124);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__5] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__5], 127);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__6] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__6], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__6], 130);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__7] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__7], 133);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__8] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__8], 136);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_sregw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 139);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 171);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__1] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__1], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__1], 116);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__2] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__2], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__2], 119);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__3] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__3], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__3], 122);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__4] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__4], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__4], 125);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__5] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__5], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__5], 128);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__6] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__6], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__6], 131);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__7] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__7], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__7], 134);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__8] != 0) record(NT_sregw, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__8], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__8], 137);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_sregw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 140);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 172);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 175);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__12, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 176);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__22, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 210);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__32, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 243);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__13, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 178);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__23, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 212);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__33, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 245);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__14, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 180);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__24, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 214);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__34, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 247);
        }
        break;
      case Op.TSTLES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__15, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 182);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__25, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 216);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__35, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 249);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__16, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 184);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__26, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 218);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__36, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 251);
        }
        break;
      case Op.TSTGES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__17, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 186);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__27, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 220);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__37, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 253);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__18, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 188);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__28, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 222);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__38, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 255);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__19, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 190);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__29, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 224);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__39, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 257);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__20, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 192);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__30, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 226);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__40, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 259);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cmpop] != 0) record(NT__21, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cmpop], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cmpop], 194);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__31, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 228);
          if (kids[0].rule[NT_regd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__41, 0 + kids[0].cost1[NT_regd] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT_regd] + kids[1].cost2[NT_regd], 261);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addr2] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 66);
          if (kids[0].rule[NT_addr3] != 0) record(NT_memsb, 0 + kids[0].cost1[NT_addr3], 0 + kids[0].cost2[NT_addr3], 68);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addr3] != 0) record(NT_memh, 0 + kids[0].cost1[NT_addr3], 0 + kids[0].cost2[NT_addr3], 67);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addr2] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addr2], 0 + kids[0].cost2[NT_addr2], 64);
          if (kids[0].rule[NT_addr3] != 0) record(NT_memw3, 0 + kids[0].cost1[NT_addr3], 0 + kids[0].cost2[NT_addr3], 65);
          if (kids[0].rule[NT__42] != 0) record(NT__51, 0 + kids[0].cost1[NT__42], 0 + kids[0].cost2[NT__42], 282);
          if (kids[0].rule[NT__53] != 0) record(NT__55, 0 + kids[0].cost1[NT__53], 0 + kids[0].cost2[NT__53], 287);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_addr3] != 0) record(NT_memf, 0 + kids[0].cost1[NT_addr3], 0 + kids[0].cost2[NT_addr3], 201);
          if (kids[0].rule[NT__42] != 0) record(NT__45, 0 + kids[0].cost1[NT__42], 0 + kids[0].cost2[NT__42], 274);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_addr3] != 0) record(NT_memd, 0 + kids[0].cost1[NT_addr3], 0 + kids[0].cost2[NT_addr3], 234);
          if (kids[0].rule[NT__42] != 0) record(NT__49, 0 + kids[0].cost1[NT__42], 0 + kids[0].cost2[NT__42], 279);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_regb], 91);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT__9] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT__9], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT__9], 93);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT__10] != 0) record(NT_void, 1 + kids[0].cost1[NT_memb] + kids[1].cost1[NT__10], 1 + kids[0].cost2[NT_memb] + kids[1].cost2[NT__10], 95);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 100);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_memh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_memh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_memh] + kids[1].cost2[NT_regh], 90);
          if (kids[0].rule[NT_memh] != 0) if (kids[1].rule[NT__11] != 0) record(NT_void, 1 + kids[0].cost1[NT_memh] + kids[1].cost1[NT__11], 1 + kids[0].cost2[NT_memh] + kids[1].cost2[NT__11], 97);
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_regh], 99);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_memw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_memw] + kids[1].cost2[NT_regw], 89);
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regw], 98);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__43] != 0) record(NT__44, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__43], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__43], 273);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__47] != 0) record(NT__48, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__47], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__47], 278);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__51] != 0) record(NT__52, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__51], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__51], 283);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__53] != 0) record(NT__54, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__53], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__53], 285);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT__55] != 0) record(NT__56, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT__55], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT__55], 288);
          if (kids[0].rule[NT__42] != 0) if (kids[1].rule[NT__57] != 0) record(NT__58, 0 + kids[0].cost1[NT__42] + kids[1].cost1[NT__57], 0 + kids[0].cost2[NT__42] + kids[1].cost2[NT__57], 290);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 3 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_regf], 3 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_regf], 204);
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 270);
          if (kids[0].rule[NT__45] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__46, 0 + kids[0].cost1[NT__45] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT__45] + kids[1].cost2[NT_regf], 275);
          if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_memf] != 0) record(NT_void, 1 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_memf], 1 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_memf], 292);
        }
        if (t.type == 1028) {
          if (kids[0].rule[NT_memd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 3 + kids[0].cost1[NT_memd] + kids[1].cost1[NT_regd], 3 + kids[0].cost2[NT_memd] + kids[1].cost2[NT_regd], 237);
          if (kids[0].rule[NT_xregd] != 0) if (kids[1].rule[NT_regd] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregd] + kids[1].cost1[NT_regd], 1 + kids[0].cost2[NT_xregd] + kids[1].cost2[NT_regd], 269);
          if (kids[0].rule[NT__49] != 0) if (kids[1].rule[NT_regd] != 0) record(NT__50, 0 + kids[0].cost1[NT__49] + kids[1].cost1[NT_regd], 0 + kids[0].cost2[NT__49] + kids[1].cost2[NT_regd], 280);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) record(NT_void, 3 + kids[0].cost1[NT_label], 1 + kids[0].cost2[NT_label], 71);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 177);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 179);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 181);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 183);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 185);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 187);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 189);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 191);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 193);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 195);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 211);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 213);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 215);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 217);
        if (kids[0].rule[NT__26] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__26] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__26] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 219);
        if (kids[0].rule[NT__27] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__27] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__27] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 221);
        if (kids[0].rule[NT__28] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__28] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__28] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 223);
        if (kids[0].rule[NT__29] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__29] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__29] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 225);
        if (kids[0].rule[NT__30] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__30] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__30] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 227);
        if (kids[0].rule[NT__31] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__31] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__31] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 229);
        if (kids[0].rule[NT__32] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__32] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__32] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 244);
        if (kids[0].rule[NT__33] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__33] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__33] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 246);
        if (kids[0].rule[NT__34] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__34] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__34] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 248);
        if (kids[0].rule[NT__35] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__35] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__35] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 250);
        if (kids[0].rule[NT__36] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__36] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__36] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 252);
        if (kids[0].rule[NT__37] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__37] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__37] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 254);
        if (kids[0].rule[NT__38] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__38] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__38] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 256);
        if (kids[0].rule[NT__39] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__39] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__39] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 258);
        if (kids[0].rule[NT__40] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__40] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__40] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 260);
        if (kids[0].rule[NT__41] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__41] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__41] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 262);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_sta] != 0) record(NT_void, 3 + kids[0].cost1[NT_sta], 1 + kids[0].cost2[NT_sta], 196);
        if (kids[0].rule[NT_regw] != 0) record(NT_void, 4 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 197);
        if (kids[0].rule[NT_memw] != 0) record(NT_void, 6 + kids[0].cost1[NT_memw], 2 + kids[0].cost2[NT_memw], 198);
        break;
      case Op.PARALLEL:
        if (kids.length == 2) if (kids[0].rule[NT__44] != 0) if (kids[1].rule[NT__46] != 0) record(NT_void, 2 + kids[0].cost1[NT__44] + kids[1].cost1[NT__46], 2 + kids[0].cost2[NT__44] + kids[1].cost2[NT__46], 276);
        if (kids.length == 2) if (kids[0].rule[NT__48] != 0) if (kids[1].rule[NT__50] != 0) record(NT_void, 2 + kids[0].cost1[NT__48] + kids[1].cost1[NT__50], 2 + kids[0].cost2[NT__48] + kids[1].cost2[NT__50], 281);
        if (kids.length == 2) if (kids[0].rule[NT__52] != 0) if (kids[1].rule[NT__54] != 0) record(NT_void, 6 + kids[0].cost1[NT__52] + kids[1].cost1[NT__54], 6 + kids[0].cost2[NT__52] + kids[1].cost2[NT__54], 286);
        if (kids.length == 3) if (kids[0].rule[NT__52] != 0) if (kids[1].rule[NT__56] != 0) if (kids[2].rule[NT__58] != 0) record(NT_void, 6 + kids[0].cost1[NT__52] + kids[1].cost1[NT__56] + kids[2].cost1[NT__58], 6 + kids[0].cost2[NT__52] + kids[1].cost2[NT__56] + kids[2].cost2[NT__58], 291);
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
    rulev[30] = new Rule(30, true, false, 22, "30: base -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[35] = new Rule(35, true, false, 25, "35: index -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[148] = new Rule(148, true, false, 49, "148: orop -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[150] = new Rule(150, true, false, 50, "150: opop -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[152] = new Rule(152, true, false, 51, "152: logop -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[154] = new Rule(154, true, false, 52, "154: cmpop -> regw", null, null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[4] = new Rule(4, true, false, 1, "4: regw -> xregw", null, null, null, 0, false, false, new int[]{8}, new String[]{"*reg-nlr-I32*", null});
    rulev[7] = new Rule(7, true, false, 2, "7: regh -> xregh", null, null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I16*", null});
    rulev[10] = new Rule(10, true, false, 3, "10: regb -> xregb", null, null, null, 0, false, false, new int[]{10}, new String[]{"*reg-I8*", null});
    rulev[13] = new Rule(13, true, false, 4, "13: regf -> xregf", null, null, null, 0, false, false, new int[]{11}, new String[]{"*reg-F32*", null});
    rulev[16] = new Rule(16, true, false, 5, "16: regd -> xregd", null, null, null, 0, false, false, new int[]{12}, new String[]{"*reg-F64*", null});
    rulev[18] = new Rule(18, true, false, 14, "18: cnst -> cbu", null, null, null, 0, false, false, new int[]{13}, new String[]{null, null});
    rulev[72] = new Rule(72, true, false, 3, "72: regb -> cbu", ImList.list(ImList.list("mov","$0",ImList.list("u0","$1"))), null, null, 0, false, false, new int[]{13}, new String[]{"*reg-I8*", null});
    rulev[24] = new Rule(24, true, false, 18, "24: asmcnst -> cnst", null, null, null, 0, false, false, new int[]{14}, new String[]{null, null});
    rulev[20] = new Rule(20, true, false, 14, "20: cnst -> ch", null, null, null, 0, false, false, new int[]{15}, new String[]{null, null});
    rulev[74] = new Rule(74, true, false, 2, "74: regh -> ch", ImList.list(ImList.list("mov","$0",ImList.list("h0","$1")),ImList.list("orr","$0","$0",ImList.list("b1","$1"))), null, null, 0, false, false, new int[]{15}, new String[]{"*reg-I16*", null});
    rulev[22] = new Rule(22, true, false, 14, "22: cnst -> cw", null, null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[75] = new Rule(75, true, false, 1, "75: regw -> cw", ImList.list(ImList.list("ldr","$0",ImList.list("lit","$1"))), null, null, 0, false, false, new int[]{16}, new String[]{"*reg-nlr-I32*", null});
    rulev[69] = new Rule(69, true, false, 1, "69: regw -> sta", ImList.list(ImList.list("ldr","$0",ImList.list("lit","$1"))), null, null, 0, false, false, new int[]{17}, new String[]{"*reg-nlr-I32*", null});
    rulev[70] = new Rule(70, true, false, 1, "70: regw -> asmcnst", ImList.list(ImList.list("ldr","$0",ImList.list("lit","$1"))), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-nlr-I32*", null});
    rulev[73] = new Rule(73, true, false, 2, "73: regh -> ximm1", ImList.list(ImList.list("mov","$0",ImList.list("b0","$1"))), null, null, 0, false, false, new int[]{19}, new String[]{"*reg-I16*", null});
    rulev[76] = new Rule(76, true, false, 1, "76: regw -> ximm", ImList.list(ImList.list("_mov","$0","$1")), null, null, 0, false, false, new int[]{21}, new String[]{"*reg-nlr-I32*", null});
    rulev[52] = new Rule(52, true, false, 35, "52: addr2 -> base", null, ImList.list(ImList.list("amode2","$1",ImList.list())), null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[60] = new Rule(60, true, false, 36, "60: addr3 -> base", null, ImList.list(ImList.list("amode3","$1",ImList.list())), null, 0, false, false, new int[]{22}, new String[]{null, null});
    rulev[33] = new Rule(33, true, false, 25, "33: index -> imm8", null, null, null, 0, false, false, new int[]{23}, new String[]{null, null});
    rulev[34] = new Rule(34, true, false, 25, "34: index -> imm12", null, null, null, 0, false, false, new int[]{24}, new String[]{null, null});
    rulev[79] = new Rule(79, true, false, 1, "79: regw -> memw", ImList.list(ImList.list("ldr","$0","$1")), null, null, 0, false, false, new int[]{37}, new String[]{"*reg-nlr-I32*", null});
    rulev[77] = new Rule(77, true, false, 3, "77: regb -> memb", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I8*", null});
    rulev[78] = new Rule(78, true, false, 2, "78: regh -> memh", ImList.list(ImList.list("ldrh","$0","$1")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I16*", null});
    rulev[141] = new Rule(141, true, false, 49, "141: orop -> imm8r", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{45}, new String[]{null, null});
    rulev[142] = new Rule(142, true, false, 50, "142: opop -> imm8r", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{45}, new String[]{null, null});
    rulev[144] = new Rule(144, true, false, 51, "144: logop -> imm8r", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{45}, new String[]{null, null});
    rulev[146] = new Rule(146, true, false, 52, "146: cmpop -> imm8r", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{45}, new String[]{null, null});
    rulev[143] = new Rule(143, true, false, 50, "143: opop -> imm8rm", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{46}, new String[]{null, null});
    rulev[147] = new Rule(147, true, false, 52, "147: cmpop -> imm8rm", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{46}, new String[]{null, null});
    rulev[145] = new Rule(145, true, false, 51, "145: logop -> imm8rn", null, ImList.list(ImList.list("hash",ImList.list("pls","$1"))), null, 0, false, false, new int[]{47}, new String[]{null, null});
    rulev[149] = new Rule(149, true, false, 49, "149: orop -> sregw", null, null, null, 0, false, false, new int[]{48}, new String[]{null, null});
    rulev[151] = new Rule(151, true, false, 50, "151: opop -> sregw", null, null, null, 0, false, false, new int[]{48}, new String[]{null, null});
    rulev[153] = new Rule(153, true, false, 51, "153: logop -> sregw", null, null, null, 0, false, false, new int[]{48}, new String[]{null, null});
    rulev[155] = new Rule(155, true, false, 52, "155: cmpop -> sregw", null, null, null, 0, false, false, new int[]{48}, new String[]{null, null});
    rulev[202] = new Rule(202, true, false, 4, "202: regf -> cnstf", ImList.list(ImList.list("ldfs","$0",ImList.list("litf","$1"))), null, null, 0, false, false, new int[]{63}, new String[]{"*reg-F32*", null});
    rulev[235] = new Rule(235, true, false, 5, "235: regd -> cnstd", ImList.list(ImList.list("ldfd","$0",ImList.list("litd","$1"))), null, null, 0, false, false, new int[]{64}, new String[]{"*reg-F64*", null});
    rulev[203] = new Rule(203, true, false, 4, "203: regf -> memf", ImList.list(ImList.list("ldfs","$0","$1")), null, null, 0, false, false, new int[]{65}, new String[]{"*reg-F32*", null});
    rulev[236] = new Rule(236, true, false, 5, "236: regd -> memd", ImList.list(ImList.list("ldfd","$0","$1")), null, null, 0, false, false, new int[]{76}, new String[]{"*reg-F64*", null});
    rulev[17] = new Rule(17, false, false, 13, "17: cbu -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[19] = new Rule(19, false, false, 15, "19: ch -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[21] = new Rule(21, false, false, 16, "21: cw -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[27] = new Rule(27, false, false, 19, "27: ximm1 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[28] = new Rule(28, false, false, 20, "28: xmimm -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[29] = new Rule(29, false, false, 21, "29: ximm -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[31] = new Rule(31, false, false, 23, "31: imm8 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[32] = new Rule(32, false, false, 24, "32: imm12 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[110] = new Rule(110, false, false, 45, "110: imm8r -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[111] = new Rule(111, false, false, 46, "111: imm8rm -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[112] = new Rule(112, false, false, 47, "112: imm8rn -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[36] = new Rule(36, false, true, 27, "36: _1 -> (INTCONST I32 1)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[38] = new Rule(38, false, true, 28, "38: _2 -> (INTCONST I32 2)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[40] = new Rule(40, false, true, 29, "40: _3 -> (INTCONST I32 3)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[42] = new Rule(42, false, true, 30, "42: _4 -> (INTCONST I32 4)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[44] = new Rule(44, false, true, 31, "44: _5 -> (INTCONST I32 5)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[46] = new Rule(46, false, true, 32, "46: _6 -> (INTCONST I32 6)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[48] = new Rule(48, false, true, 33, "48: _7 -> (INTCONST I32 7)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[50] = new Rule(50, false, true, 34, "50: _8 -> (INTCONST I32 8)", null, null, null, 0, false, false, new int[]{}, null);
    rulev[199] = new Rule(199, false, false, 63, "199: cnstf -> (FLOATCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[200] = new Rule(200, false, false, 64, "200: cnstd -> (FLOATCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[23] = new Rule(23, false, false, 17, "23: sta -> (STATIC I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 10, "8: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 9, "5: xregh -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 8, "2: xregw -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[271] = new Rule(271, false, true, 87, "271: _42 -> (REG I32 \"%sp\")", null, null, null, 0, false, false, new int[]{}, null);
    rulev[11] = new Rule(11, false, false, 11, "11: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[14] = new Rule(14, false, false, 12, "14: xregd -> (REG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[9] = new Rule(9, false, false, 10, "9: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 9, "6: xregh -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 8, "3: xregw -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 11, "12: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[15] = new Rule(15, false, false, 12, "15: xregd -> (SUBREG F64)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 7, "1: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[174] = new Rule(174, false, false, 1, "174: regw -> (NEG I32 regw)", ImList.list(ImList.list("rsb","$0","$1",ImList.list("hash","0"))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[205] = new Rule(205, false, false, 4, "205: regf -> (NEG F32 regf)", ImList.list(ImList.list("mnfs","$0","$1")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-F32*", "*reg-F32*"});
    rulev[238] = new Rule(238, false, false, 5, "238: regd -> (NEG F64 regd)", ImList.list(ImList.list("mnfd","$0","$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F64*", "*reg-F64*"});
    rulev[25] = new Rule(25, false, false, 18, "25: asmcnst -> (ADD I32 asmcnst cnst)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{18,14}, new String[]{null, null, null});
    rulev[53] = new Rule(53, false, false, 35, "53: addr2 -> (ADD I32 base imm12)", null, ImList.list(ImList.list("amode2","%fp",ImList.list("hash",ImList.list("adjDisp",ImList.list("pls","$2"))))), null, 0, false, false, new int[]{22,24}, new String[]{null, null, null});
    rulev[54] = new Rule(54, false, false, 35, "54: addr2 -> (ADD I32 base imm12)", null, ImList.list(ImList.list("amode2","$1",ImList.list("hash",ImList.list("pls","$2")))), null, 0, false, false, new int[]{22,24}, new String[]{null, null, null});
    rulev[56] = new Rule(56, false, false, 35, "56: addr2 -> (ADD I32 base index)", null, ImList.list(ImList.list("amode2","$1",ImList.list("pind","$2"))), null, 0, false, false, new int[]{22,25}, new String[]{null, null, null});
    rulev[58] = new Rule(58, false, false, 35, "58: addr2 -> (ADD I32 base sindex)", null, ImList.list(ImList.list("amode2","$1",ImList.list("pind","$2"))), null, 0, false, false, new int[]{22,26}, new String[]{null, null, null});
    rulev[61] = new Rule(61, false, false, 36, "61: addr3 -> (ADD I32 base imm8)", null, ImList.list(ImList.list("amode3","%fp",ImList.list("hash",ImList.list("adjDisp",ImList.list("pls","$2"))))), null, 0, false, false, new int[]{22,23}, new String[]{null, null, null});
    rulev[62] = new Rule(62, false, false, 36, "62: addr3 -> (ADD I32 base imm8)", null, ImList.list(ImList.list("amode3","$1",ImList.list("hash",ImList.list("pls","$2")))), null, 0, false, false, new int[]{22,23}, new String[]{null, null, null});
    rulev[113] = new Rule(113, false, false, 1, "113: regw -> (ADD I32 regw xmimm)", ImList.list(ImList.list("sub","$0","%fp",ImList.list("vb0","$2")),ImList.list("sub","$0","$0",ImList.list("vb1","$2")),ImList.list("sub","$0","$0",ImList.list("vb2","$2")),ImList.list("sub","$0","$0",ImList.list("vb3","$2"))), null, null, 0, false, false, new int[]{1,20}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[156] = new Rule(156, false, false, 1, "156: regw -> (ADD I32 regw opop)", ImList.list(ImList.list("add","$0","$1","$2")), null, null, 0, false, false, new int[]{1,50}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[166] = new Rule(166, false, false, 1, "166: regw -> (ADD I32 regw ximm)", ImList.list(new ImList("_add", ImList.list("add","sub","$0","$1","$2"))), null, null, 0, false, false, new int[]{1,21}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[284] = new Rule(284, false, true, 98, "284: _53 -> (ADD I32 _42 _4)", null, null, null, 0, false, false, new int[]{87,30}, null);
    rulev[289] = new Rule(289, false, true, 102, "289: _57 -> (ADD I32 _42 _8)", null, null, null, 0, false, false, new int[]{87,34}, null);
    rulev[206] = new Rule(206, false, false, 4, "206: regf -> (ADD F32 regf regf)", ImList.list(ImList.list("adfs","$0","$1","$2")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[239] = new Rule(239, false, false, 5, "239: regd -> (ADD F64 regd regd)", ImList.list(ImList.list("adfd","$0","$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[26] = new Rule(26, false, false, 18, "26: asmcnst -> (SUB I32 asmcnst cnst)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{18,14}, new String[]{null, null, null});
    rulev[55] = new Rule(55, false, false, 35, "55: addr2 -> (SUB I32 base imm12)", null, ImList.list(ImList.list("amode2","$1",ImList.list("hash",ImList.list("neg","$2")))), null, 0, false, false, new int[]{22,24}, new String[]{null, null, null});
    rulev[57] = new Rule(57, false, false, 35, "57: addr2 -> (SUB I32 base index)", null, ImList.list(ImList.list("amode2","$1",ImList.list("mind","$2"))), null, 0, false, false, new int[]{22,25}, new String[]{null, null, null});
    rulev[59] = new Rule(59, false, false, 35, "59: addr2 -> (SUB I32 base sindex)", null, ImList.list(ImList.list("amode2","$1",ImList.list("mind","$2"))), null, 0, false, false, new int[]{22,26}, new String[]{null, null, null});
    rulev[63] = new Rule(63, false, false, 36, "63: addr3 -> (SUB I32 base imm8)", null, ImList.list(ImList.list("amode3","$1",ImList.list("hash",ImList.list("neg","$2")))), null, 0, false, false, new int[]{22,23}, new String[]{null, null, null});
  }
  static private void rrinit100() {
    rulev[157] = new Rule(157, false, false, 1, "157: regw -> (SUB I32 regw opop)", ImList.list(ImList.list("sub","$0","$1","$2")), null, null, 0, false, false, new int[]{1,50}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[159] = new Rule(159, false, false, 1, "159: regw -> (SUB I32 orop regw)", ImList.list(ImList.list("rsb","$0","$2","$1")), null, null, 0, false, false, new int[]{49,1}, new String[]{"*reg-nlr-I32*", null, "*reg-nlr-I32*"});
    rulev[167] = new Rule(167, false, false, 1, "167: regw -> (SUB I32 regw ximm)", ImList.list(new ImList("_add", ImList.list("sub","add","$0","$1","$2"))), null, null, 0, false, false, new int[]{1,21}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[272] = new Rule(272, false, true, 88, "272: _43 -> (SUB I32 _42 _4)", null, null, null, 0, false, false, new int[]{87,30}, null);
    rulev[277] = new Rule(277, false, true, 92, "277: _47 -> (SUB I32 _42 _8)", null, null, null, 0, false, false, new int[]{87,34}, null);
    rulev[207] = new Rule(207, false, false, 4, "207: regf -> (SUB F32 regf regf)", ImList.list(ImList.list("sufs","$0","$1","$2")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[240] = new Rule(240, false, false, 5, "240: regd -> (SUB F64 regd regd)", ImList.list(ImList.list("sufd","$0","$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[169] = new Rule(169, false, false, 1, "169: regw -> (MUL I32 regw regw)", ImList.list(ImList.list("mov","%lr","$1"),ImList.list("mul","$0","%lr","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[208] = new Rule(208, false, false, 4, "208: regf -> (MUL F32 regf regf)", ImList.list(ImList.list("mufs","$0","$1","$2")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[241] = new Rule(241, false, false, 5, "241: regd -> (MUL F64 regd regd)", ImList.list(ImList.list("mufd","$0","$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[209] = new Rule(209, false, false, 4, "209: regf -> (DIVS F32 regf regf)", ImList.list(ImList.list("dvfs","$0","$1","$2")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[242] = new Rule(242, false, false, 5, "242: regd -> (DIVS F64 regd regd)", ImList.list(ImList.list("dvfd","$0","$1","$2")), null, null, 0, false, false, new int[]{5,5}, new String[]{"*reg-F64*", "*reg-F64*", "*reg-F64*"});
    rulev[85] = new Rule(85, false, false, 2, "85: regh -> (CONVSX I16 memsb)", ImList.list(ImList.list("ldrsb","$0","$1")), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-I16*", null});
    rulev[106] = new Rule(106, false, false, 2, "106: regh -> (CONVSX I16 regb)", ImList.list(ImList.list("mov","$0",ImList.list("lsl","$1",ImList.list("hash","24"))),ImList.list("mov","$0",ImList.list("asr","$0",ImList.list("hash","8"))),ImList.list("mov","$0",ImList.list("lsr","$0",ImList.list("hash","16")))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[82] = new Rule(82, false, false, 1, "82: regw -> (CONVSX I32 memsb)", ImList.list(ImList.list("ldrsb","$0","$1")), null, null, 0, false, false, new int[]{41}, new String[]{"*reg-nlr-I32*", null});
    rulev[83] = new Rule(83, false, false, 1, "83: regw -> (CONVSX I32 memh)", ImList.list(ImList.list("ldrsh","$0","$1")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-nlr-I32*", null});
    rulev[104] = new Rule(104, false, false, 1, "104: regw -> (CONVSX I32 regb)", ImList.list(ImList.list("mov","$0",ImList.list("lsl","$1",ImList.list("hash","24"))),ImList.list("mov","$0",ImList.list("asr","$0",ImList.list("hash","24")))), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-nlr-I32*", "*reg-I8*"});
    rulev[105] = new Rule(105, false, false, 1, "105: regw -> (CONVSX I32 regh)", ImList.list(ImList.list("mov","$0",ImList.list("lsl","$1",ImList.list("hash","16"))),ImList.list("mov","$0",ImList.list("asr","$0",ImList.list("hash","16")))), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-nlr-I32*", "*reg-I16*"});
    rulev[84] = new Rule(84, false, false, 2, "84: regh -> (CONVZX I16 memb)", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-I16*", null});
    rulev[103] = new Rule(103, false, false, 2, "103: regh -> (CONVZX I16 regb)", ImList.list(ImList.list("mov","$0","$1")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[80] = new Rule(80, false, false, 1, "80: regw -> (CONVZX I32 memb)", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{39}, new String[]{"*reg-nlr-I32*", null});
    rulev[81] = new Rule(81, false, false, 1, "81: regw -> (CONVZX I32 memh)", ImList.list(ImList.list("ldrh","$0","$1")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-nlr-I32*", null});
    rulev[101] = new Rule(101, false, false, 1, "101: regw -> (CONVZX I32 regh)", ImList.list(ImList.list("mov","$0","$1")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-nlr-I32*", "*reg-I16*"});
    rulev[102] = new Rule(102, false, false, 1, "102: regw -> (CONVZX I32 regb)", ImList.list(ImList.list("mov","$0","$1")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-nlr-I32*", "*reg-I8*"});
    rulev[86] = new Rule(86, false, false, 3, "86: regb -> (CONVIT I8 memw3)", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{38}, new String[]{"*reg-I8*", null});
    rulev[88] = new Rule(88, false, false, 3, "88: regb -> (CONVIT I8 memh)", ImList.list(ImList.list("ldrb","$0","$1")), null, null, 0, false, false, new int[]{40}, new String[]{"*reg-I8*", null});
    rulev[92] = new Rule(92, false, true, 42, "92: _9 -> (CONVIT I8 regw)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[94] = new Rule(94, false, true, 43, "94: _10 -> (CONVIT I8 regh)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[107] = new Rule(107, false, false, 3, "107: regb -> (CONVIT I8 regw)", ImList.list(ImList.list("and","$0","$1","#255")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-nlr-I32*"});
    rulev[108] = new Rule(108, false, false, 3, "108: regb -> (CONVIT I8 regh)", ImList.list(ImList.list("and","$0","$1","#255")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-I16*"});
    rulev[87] = new Rule(87, false, false, 2, "87: regh -> (CONVIT I16 memw3)", ImList.list(ImList.list("ldrh","$0","$1")), null, null, 0, false, false, new int[]{38}, new String[]{"*reg-I16*", null});
    rulev[96] = new Rule(96, false, true, 44, "96: _11 -> (CONVIT I16 regw)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[109] = new Rule(109, false, false, 2, "109: regh -> (CONVIT I16 regw)", ImList.list(ImList.list("mov","$0",ImList.list("lsl","$1",ImList.list("hash","16"))),ImList.list("mov","$0",ImList.list("lsr","$0",ImList.list("hash","16")))), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-nlr-I32*"});
    rulev[267] = new Rule(267, false, false, 5, "267: regd -> (CONVFX F64 regf)", ImList.list(ImList.list("mvfd","$0","$1")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-F64*", "*reg-F32*"});
    rulev[268] = new Rule(268, false, false, 4, "268: regf -> (CONVFT F32 regd)", ImList.list(ImList.list("mvfs","$0","$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-F32*", "*reg-F64*"});
    rulev[232] = new Rule(232, false, false, 3, "232: regb -> (CONVFS I8 regf)", ImList.list(ImList.list("fixz","$0","$1"),ImList.list("and","$0","$0","#255")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I8*", "*reg-F32*"});
    rulev[265] = new Rule(265, false, false, 3, "265: regb -> (CONVFS I8 regd)", ImList.list(ImList.list("fixz","$0","$1"),ImList.list("and","$0","$0","#255")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I8*", "*reg-F64*"});
    rulev[231] = new Rule(231, false, false, 2, "231: regh -> (CONVFS I16 regf)", ImList.list(ImList.list("fixz","$0","$1"),ImList.list("mov","$0",ImList.list("lsl","$0",ImList.list("hash","16"))),ImList.list("mov","$0",ImList.list("lsr","$0",ImList.list("hash","16")))), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-I16*", "*reg-F32*"});
    rulev[264] = new Rule(264, false, false, 2, "264: regh -> (CONVFS I16 regd)", ImList.list(ImList.list("fixz","$0","$1"),ImList.list("mov","$0",ImList.list("lsl","$0",ImList.list("hash","16"))),ImList.list("mov","$0",ImList.list("lsr","$0",ImList.list("hash","16")))), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-I16*", "*reg-F64*"});
    rulev[230] = new Rule(230, false, false, 1, "230: regw -> (CONVFS I32 regf)", ImList.list(ImList.list("fixz","$0","$1")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-nlr-I32*", "*reg-F32*"});
    rulev[263] = new Rule(263, false, false, 1, "263: regw -> (CONVFS I32 regd)", ImList.list(ImList.list("fixz","$0","$1")), null, null, 0, false, false, new int[]{5}, new String[]{"*reg-nlr-I32*", "*reg-F64*"});
    rulev[233] = new Rule(233, false, false, 4, "233: regf -> (CONVSF F32 regw)", ImList.list(ImList.list("flts","$0","$1")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-F32*", "*reg-nlr-I32*"});
    rulev[266] = new Rule(266, false, false, 5, "266: regd -> (CONVSF F64 regw)", ImList.list(ImList.list("fltd","$0","$1")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-F64*", "*reg-nlr-I32*"});
    rulev[158] = new Rule(158, false, false, 1, "158: regw -> (BAND I32 regw logop)", ImList.list(ImList.list("and","$0","$1","$2")), null, null, 0, false, false, new int[]{1,51}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[168] = new Rule(168, false, false, 1, "168: regw -> (BAND I32 regw ximm)", ImList.list(ImList.list("_bic","bic","$0","$1","$2")), null, null, 0, false, false, new int[]{1,21}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[160] = new Rule(160, false, false, 1, "160: regw -> (BOR I32 regw imm8rn)", ImList.list(ImList.list("mov","%lr",ImList.list("hash","$2")),ImList.list("orr","$0","$1","%lr")), null, null, 0, false, false, new int[]{1,47}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[161] = new Rule(161, false, false, 1, "161: regw -> (BOR I32 regw orop)", ImList.list(ImList.list("orr","$0","$1","$2")), null, null, 0, false, false, new int[]{1,49}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[164] = new Rule(164, false, false, 1, "164: regw -> (BOR I32 regw ximm)", ImList.list(ImList.list("_or","orr","$0","$1","$2")), null, null, 0, false, false, new int[]{1,21}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[162] = new Rule(162, false, false, 1, "162: regw -> (BXOR I32 regw imm8rn)", ImList.list(ImList.list("mov","%lr",ImList.list("hash","$2")),ImList.list("eor","$0","$1","%lr")), null, null, 0, false, false, new int[]{1,47}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[163] = new Rule(163, false, false, 1, "163: regw -> (BXOR I32 regw orop)", ImList.list(ImList.list("eor","$0","$1","$2")), null, null, 0, false, false, new int[]{1,49}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[165] = new Rule(165, false, false, 1, "165: regw -> (BXOR I32 regw ximm)", ImList.list(ImList.list("_or","eor","$0","$1","$2")), null, null, 0, false, false, new int[]{1,21}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[173] = new Rule(173, false, false, 1, "173: regw -> (BNOT I32 regw)", ImList.list(ImList.list("mvn","$0","$1")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[37] = new Rule(37, false, false, 26, "37: sindex -> (LSHS I32 regw _1)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","1"))), null, 0, false, false, new int[]{1,27}, new String[]{null, "*reg-nlr-I32*"});
    rulev[39] = new Rule(39, false, false, 26, "39: sindex -> (LSHS I32 regw _2)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","2"))), null, 0, false, false, new int[]{1,28}, new String[]{null, "*reg-nlr-I32*"});
    rulev[41] = new Rule(41, false, false, 26, "41: sindex -> (LSHS I32 regw _3)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","3"))), null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-nlr-I32*"});
    rulev[43] = new Rule(43, false, false, 26, "43: sindex -> (LSHS I32 regw _4)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","4"))), null, 0, false, false, new int[]{1,30}, new String[]{null, "*reg-nlr-I32*"});
    rulev[45] = new Rule(45, false, false, 26, "45: sindex -> (LSHS I32 regw _5)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","5"))), null, 0, false, false, new int[]{1,31}, new String[]{null, "*reg-nlr-I32*"});
    rulev[47] = new Rule(47, false, false, 26, "47: sindex -> (LSHS I32 regw _6)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","6"))), null, 0, false, false, new int[]{1,32}, new String[]{null, "*reg-nlr-I32*"});
    rulev[49] = new Rule(49, false, false, 26, "49: sindex -> (LSHS I32 regw _7)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","7"))), null, 0, false, false, new int[]{1,33}, new String[]{null, "*reg-nlr-I32*"});
    rulev[51] = new Rule(51, false, false, 26, "51: sindex -> (LSHS I32 regw _8)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","8"))), null, 0, false, false, new int[]{1,34}, new String[]{null, "*reg-nlr-I32*"});
    rulev[114] = new Rule(114, false, false, 48, "114: sregw -> (LSHS I32 regw _1)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","1"))), null, 0, false, false, new int[]{1,27}, new String[]{null, "*reg-nlr-I32*"});
    rulev[117] = new Rule(117, false, false, 48, "117: sregw -> (LSHS I32 regw _2)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","2"))), null, 0, false, false, new int[]{1,28}, new String[]{null, "*reg-nlr-I32*"});
    rulev[120] = new Rule(120, false, false, 48, "120: sregw -> (LSHS I32 regw _3)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","3"))), null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-nlr-I32*"});
    rulev[123] = new Rule(123, false, false, 48, "123: sregw -> (LSHS I32 regw _4)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","4"))), null, 0, false, false, new int[]{1,30}, new String[]{null, "*reg-nlr-I32*"});
    rulev[126] = new Rule(126, false, false, 48, "126: sregw -> (LSHS I32 regw _5)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","5"))), null, 0, false, false, new int[]{1,31}, new String[]{null, "*reg-nlr-I32*"});
    rulev[129] = new Rule(129, false, false, 48, "129: sregw -> (LSHS I32 regw _6)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","6"))), null, 0, false, false, new int[]{1,32}, new String[]{null, "*reg-nlr-I32*"});
    rulev[132] = new Rule(132, false, false, 48, "132: sregw -> (LSHS I32 regw _7)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","7"))), null, 0, false, false, new int[]{1,33}, new String[]{null, "*reg-nlr-I32*"});
    rulev[135] = new Rule(135, false, false, 48, "135: sregw -> (LSHS I32 regw _8)", null, ImList.list(ImList.list("lsl","$1",ImList.list("hash","8"))), null, 0, false, false, new int[]{1,34}, new String[]{null, "*reg-nlr-I32*"});
    rulev[138] = new Rule(138, false, false, 48, "138: sregw -> (LSHS I32 regw regw)", null, ImList.list(ImList.list("lsl","$1","$2")), null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[170] = new Rule(170, false, false, 1, "170: regw -> (LSHS I32 regw regw)", ImList.list(ImList.list("mov","$0",ImList.list("lsl","$1","$2"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[115] = new Rule(115, false, false, 48, "115: sregw -> (RSHS I32 regw _1)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","1"))), null, 0, false, false, new int[]{1,27}, new String[]{null, "*reg-nlr-I32*"});
    rulev[118] = new Rule(118, false, false, 48, "118: sregw -> (RSHS I32 regw _2)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","2"))), null, 0, false, false, new int[]{1,28}, new String[]{null, "*reg-nlr-I32*"});
    rulev[121] = new Rule(121, false, false, 48, "121: sregw -> (RSHS I32 regw _3)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","3"))), null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-nlr-I32*"});
    rulev[124] = new Rule(124, false, false, 48, "124: sregw -> (RSHS I32 regw _4)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","4"))), null, 0, false, false, new int[]{1,30}, new String[]{null, "*reg-nlr-I32*"});
    rulev[127] = new Rule(127, false, false, 48, "127: sregw -> (RSHS I32 regw _5)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","5"))), null, 0, false, false, new int[]{1,31}, new String[]{null, "*reg-nlr-I32*"});
    rulev[130] = new Rule(130, false, false, 48, "130: sregw -> (RSHS I32 regw _6)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","6"))), null, 0, false, false, new int[]{1,32}, new String[]{null, "*reg-nlr-I32*"});
    rulev[133] = new Rule(133, false, false, 48, "133: sregw -> (RSHS I32 regw _7)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","7"))), null, 0, false, false, new int[]{1,33}, new String[]{null, "*reg-nlr-I32*"});
    rulev[136] = new Rule(136, false, false, 48, "136: sregw -> (RSHS I32 regw _8)", null, ImList.list(ImList.list("asr","$1",ImList.list("hash","8"))), null, 0, false, false, new int[]{1,34}, new String[]{null, "*reg-nlr-I32*"});
    rulev[139] = new Rule(139, false, false, 48, "139: sregw -> (RSHS I32 regw regw)", null, ImList.list(ImList.list("asr","$1","$2")), null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[171] = new Rule(171, false, false, 1, "171: regw -> (RSHS I32 regw regw)", ImList.list(ImList.list("mov","$0",ImList.list("asr","$1","$2"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[116] = new Rule(116, false, false, 48, "116: sregw -> (RSHU I32 regw _1)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","1"))), null, 0, false, false, new int[]{1,27}, new String[]{null, "*reg-nlr-I32*"});
    rulev[119] = new Rule(119, false, false, 48, "119: sregw -> (RSHU I32 regw _2)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","2"))), null, 0, false, false, new int[]{1,28}, new String[]{null, "*reg-nlr-I32*"});
    rulev[122] = new Rule(122, false, false, 48, "122: sregw -> (RSHU I32 regw _3)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","3"))), null, 0, false, false, new int[]{1,29}, new String[]{null, "*reg-nlr-I32*"});
    rulev[125] = new Rule(125, false, false, 48, "125: sregw -> (RSHU I32 regw _4)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","4"))), null, 0, false, false, new int[]{1,30}, new String[]{null, "*reg-nlr-I32*"});
    rulev[128] = new Rule(128, false, false, 48, "128: sregw -> (RSHU I32 regw _5)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","5"))), null, 0, false, false, new int[]{1,31}, new String[]{null, "*reg-nlr-I32*"});
    rulev[131] = new Rule(131, false, false, 48, "131: sregw -> (RSHU I32 regw _6)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","6"))), null, 0, false, false, new int[]{1,32}, new String[]{null, "*reg-nlr-I32*"});
    rulev[134] = new Rule(134, false, false, 48, "134: sregw -> (RSHU I32 regw _7)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","7"))), null, 0, false, false, new int[]{1,33}, new String[]{null, "*reg-nlr-I32*"});
    rulev[137] = new Rule(137, false, false, 48, "137: sregw -> (RSHU I32 regw _8)", null, ImList.list(ImList.list("lsr","$1",ImList.list("hash","8"))), null, 0, false, false, new int[]{1,34}, new String[]{null, "*reg-nlr-I32*"});
    rulev[140] = new Rule(140, false, false, 48, "140: sregw -> (RSHU I32 regw regw)", null, ImList.list(ImList.list("lsr","$1","$2")), null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[172] = new Rule(172, false, false, 1, "172: regw -> (RSHU I32 regw regw)", ImList.list(ImList.list("mov","$0",ImList.list("lsr","$1","$2"))), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", "*reg-nlr-I32*"});
    rulev[175] = new Rule(175, false, false, 1, "175: regw -> (TSTEQ I32 regw cmpop)", ImList.list(ImList.list("cmp","$1","$2")), null, null, 0, false, false, new int[]{1,52}, new String[]{"*reg-nlr-I32*", "*reg-nlr-I32*", null});
    rulev[176] = new Rule(176, false, true, 53, "176: _12 -> (TSTEQ I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[210] = new Rule(210, false, true, 66, "210: _22 -> (TSTEQ I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[243] = new Rule(243, false, true, 77, "243: _32 -> (TSTEQ I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[178] = new Rule(178, false, true, 54, "178: _13 -> (TSTNE I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[212] = new Rule(212, false, true, 67, "212: _23 -> (TSTNE I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[245] = new Rule(245, false, true, 78, "245: _33 -> (TSTNE I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[180] = new Rule(180, false, true, 55, "180: _14 -> (TSTLTS I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[214] = new Rule(214, false, true, 68, "214: _24 -> (TSTLTS I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[247] = new Rule(247, false, true, 79, "247: _34 -> (TSTLTS I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
  }
  static private void rrinit200() {
    rulev[182] = new Rule(182, false, true, 56, "182: _15 -> (TSTLES I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[216] = new Rule(216, false, true, 69, "216: _25 -> (TSTLES I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[249] = new Rule(249, false, true, 80, "249: _35 -> (TSTLES I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[184] = new Rule(184, false, true, 57, "184: _16 -> (TSTGTS I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[218] = new Rule(218, false, true, 70, "218: _26 -> (TSTGTS I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[251] = new Rule(251, false, true, 81, "251: _36 -> (TSTGTS I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[186] = new Rule(186, false, true, 58, "186: _17 -> (TSTGES I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[220] = new Rule(220, false, true, 71, "220: _27 -> (TSTGES I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[253] = new Rule(253, false, true, 82, "253: _37 -> (TSTGES I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[188] = new Rule(188, false, true, 59, "188: _18 -> (TSTLTU I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[222] = new Rule(222, false, true, 72, "222: _28 -> (TSTLTU I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[255] = new Rule(255, false, true, 83, "255: _38 -> (TSTLTU I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[190] = new Rule(190, false, true, 60, "190: _19 -> (TSTLEU I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[224] = new Rule(224, false, true, 73, "224: _29 -> (TSTLEU I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[257] = new Rule(257, false, true, 84, "257: _39 -> (TSTLEU I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[192] = new Rule(192, false, true, 61, "192: _20 -> (TSTGTU I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[226] = new Rule(226, false, true, 74, "226: _30 -> (TSTGTU I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[259] = new Rule(259, false, true, 85, "259: _40 -> (TSTGTU I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[194] = new Rule(194, false, true, 62, "194: _21 -> (TSTGEU I32 regw cmpop)", null, null, null, 0, false, false, new int[]{1,52}, null);
    rulev[228] = new Rule(228, false, true, 75, "228: _31 -> (TSTGEU I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[261] = new Rule(261, false, true, 86, "261: _41 -> (TSTGEU I32 regd regd)", null, null, null, 0, false, false, new int[]{5,5}, null);
    rulev[66] = new Rule(66, false, false, 39, "66: memb -> (MEM I8 addr2)", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[68] = new Rule(68, false, false, 41, "68: memsb -> (MEM I8 addr3)", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[67] = new Rule(67, false, false, 40, "67: memh -> (MEM I16 addr3)", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[64] = new Rule(64, false, false, 37, "64: memw -> (MEM I32 addr2)", null, null, null, 0, false, false, new int[]{35}, new String[]{null, null});
    rulev[65] = new Rule(65, false, false, 38, "65: memw3 -> (MEM I32 addr3)", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[282] = new Rule(282, false, true, 96, "282: _51 -> (MEM I32 _42)", null, null, null, 0, false, false, new int[]{87}, null);
    rulev[287] = new Rule(287, false, true, 100, "287: _55 -> (MEM I32 _53)", null, null, null, 0, false, false, new int[]{98}, null);
    rulev[201] = new Rule(201, false, false, 65, "201: memf -> (MEM F32 addr3)", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[274] = new Rule(274, false, true, 90, "274: _45 -> (MEM F32 _42)", null, null, null, 0, false, false, new int[]{87}, null);
    rulev[234] = new Rule(234, false, false, 76, "234: memd -> (MEM F64 addr3)", null, null, null, 0, false, false, new int[]{36}, new String[]{null, null});
    rulev[279] = new Rule(279, false, true, 94, "279: _49 -> (MEM F64 _42)", null, null, null, 0, false, false, new int[]{87}, null);
    rulev[91] = new Rule(91, false, false, 6, "91: void -> (SET I8 memb regb)", ImList.list(ImList.list("strb","$2","$1")), null, null, 0, false, false, new int[]{39,3}, new String[]{null, null, "*reg-I8*"});
    rulev[93] = new Rule(93, false, false, 6, "93: void -> (SET I8 memb _9)", ImList.list(ImList.list("strb","$2","$1")), null, null, 0, false, false, new int[]{39,42}, new String[]{null, null, "*reg-nlr-I32*"});
    rulev[95] = new Rule(95, false, false, 6, "95: void -> (SET I8 memb _10)", ImList.list(ImList.list("strb","$2","$1")), null, null, 0, false, false, new int[]{39,43}, new String[]{null, null, "*reg-I16*"});
    rulev[100] = new Rule(100, false, false, 6, "100: void -> (SET I8 xregb regb)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{10,3}, new String[]{null, null, "*reg-I8*"});
    rulev[90] = new Rule(90, false, false, 6, "90: void -> (SET I16 memh regh)", ImList.list(ImList.list("strh","$2","$1")), null, null, 0, false, false, new int[]{40,2}, new String[]{null, null, "*reg-I16*"});
    rulev[97] = new Rule(97, false, false, 6, "97: void -> (SET I16 memh _11)", ImList.list(ImList.list("strh","$2","$1")), null, null, 0, false, false, new int[]{40,44}, new String[]{null, null, "*reg-nlr-I32*"});
    rulev[99] = new Rule(99, false, false, 6, "99: void -> (SET I16 xregh regh)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{9,2}, new String[]{null, null, "*reg-I16*"});
    rulev[89] = new Rule(89, false, false, 6, "89: void -> (SET I32 memw regw)", ImList.list(ImList.list("str","$2","$1")), null, null, 0, false, false, new int[]{37,1}, new String[]{null, null, "*reg-nlr-I32*"});
    rulev[98] = new Rule(98, false, false, 6, "98: void -> (SET I32 xregw regw)", ImList.list(ImList.list("mov","$1","$2")), null, null, 0, false, false, new int[]{8,1}, new String[]{null, null, "*reg-nlr-I32*"});
    rulev[273] = new Rule(273, false, true, 89, "273: _44 -> (SET I32 _42 _43)", null, null, null, 0, false, false, new int[]{87,88}, null);
    rulev[278] = new Rule(278, false, true, 93, "278: _48 -> (SET I32 _42 _47)", null, null, null, 0, false, false, new int[]{87,92}, null);
    rulev[283] = new Rule(283, false, true, 97, "283: _52 -> (SET I32 regw _51)", null, null, null, 0, false, false, new int[]{1,96}, null);
    rulev[285] = new Rule(285, false, true, 99, "285: _54 -> (SET I32 _42 _53)", null, null, null, 0, false, false, new int[]{87,98}, null);
    rulev[288] = new Rule(288, false, true, 101, "288: _56 -> (SET I32 regw _55)", null, null, null, 0, false, false, new int[]{1,100}, null);
    rulev[290] = new Rule(290, false, true, 103, "290: _58 -> (SET I32 _42 _57)", null, null, null, 0, false, false, new int[]{87,102}, null);
    rulev[204] = new Rule(204, false, false, 6, "204: void -> (SET F32 memf regf)", ImList.list(ImList.list("stfs","$2","$1")), null, null, 0, false, false, new int[]{65,4}, new String[]{null, null, "*reg-F32*"});
    rulev[270] = new Rule(270, false, false, 6, "270: void -> (SET F32 xregf regf)", ImList.list(ImList.list("mvfs","$1","$2")), null, null, 0, false, false, new int[]{11,4}, new String[]{null, null, "*reg-F32*"});
    rulev[275] = new Rule(275, false, true, 91, "275: _46 -> (SET F32 _45 regf)", null, null, null, 0, false, false, new int[]{90,4}, null);
    rulev[292] = new Rule(292, false, false, 6, "292: void -> (SET F32 memf memf)", ImList.list(ImList.list("ldr","%lr","$2"),ImList.list("str","%lr","$1")), null, null, 0, false, false, new int[]{65,65}, new String[]{null, null, null});
    rulev[237] = new Rule(237, false, false, 6, "237: void -> (SET F64 memd regd)", ImList.list(ImList.list("stfd","$2","$1")), null, null, 0, false, false, new int[]{76,5}, new String[]{null, null, "*reg-F64*"});
    rulev[269] = new Rule(269, false, false, 6, "269: void -> (SET F64 xregd regd)", ImList.list(ImList.list("mvfd","$1","$2")), null, null, 0, false, false, new int[]{12,5}, new String[]{null, null, "*reg-F64*"});
    rulev[280] = new Rule(280, false, true, 95, "280: _50 -> (SET F64 _49 regd)", null, null, null, 0, false, false, new int[]{94,5}, null);
    rulev[71] = new Rule(71, false, false, 6, "71: void -> (JUMP _ label)", ImList.list(ImList.list("b","$1")), null, null, 0, false, false, new int[]{7}, new String[]{null, null});
    rulev[177] = new Rule(177, false, false, 6, "177: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("beq","$3")), null, null, 0, false, false, new int[]{53,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[179] = new Rule(179, false, false, 6, "179: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bne","$3")), null, null, 0, false, false, new int[]{54,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[181] = new Rule(181, false, false, 6, "181: void -> (JUMPC _ _14 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("blt","$3")), null, null, 0, false, false, new int[]{55,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[183] = new Rule(183, false, false, 6, "183: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("ble","$3")), null, null, 0, false, false, new int[]{56,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[185] = new Rule(185, false, false, 6, "185: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bgt","$3")), null, null, 0, false, false, new int[]{57,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[187] = new Rule(187, false, false, 6, "187: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bge","$3")), null, null, 0, false, false, new int[]{58,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[189] = new Rule(189, false, false, 6, "189: void -> (JUMPC _ _18 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("blo","$3")), null, null, 0, false, false, new int[]{59,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[191] = new Rule(191, false, false, 6, "191: void -> (JUMPC _ _19 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bls","$3")), null, null, 0, false, false, new int[]{60,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[193] = new Rule(193, false, false, 6, "193: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bhi","$3")), null, null, 0, false, false, new int[]{61,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[195] = new Rule(195, false, false, 6, "195: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("cmp","$1","$2"),ImList.list("bhs","$3")), null, null, 0, false, false, new int[]{62,7,7}, new String[]{null, "*reg-nlr-I32*", null, null, null});
    rulev[211] = new Rule(211, false, false, 6, "211: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("beq","$3")), null, null, 0, false, false, new int[]{66,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[213] = new Rule(213, false, false, 6, "213: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bne","$3")), null, null, 0, false, false, new int[]{67,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[215] = new Rule(215, false, false, 6, "215: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("blt","$3")), null, null, 0, false, false, new int[]{68,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[217] = new Rule(217, false, false, 6, "217: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("ble","$3")), null, null, 0, false, false, new int[]{69,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[219] = new Rule(219, false, false, 6, "219: void -> (JUMPC _ _26 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bgt","$3")), null, null, 0, false, false, new int[]{70,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[221] = new Rule(221, false, false, 6, "221: void -> (JUMPC _ _27 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bge","$3")), null, null, 0, false, false, new int[]{71,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[223] = new Rule(223, false, false, 6, "223: void -> (JUMPC _ _28 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("blo","$3")), null, null, 0, false, false, new int[]{72,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[225] = new Rule(225, false, false, 6, "225: void -> (JUMPC _ _29 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bls","$3")), null, null, 0, false, false, new int[]{73,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[227] = new Rule(227, false, false, 6, "227: void -> (JUMPC _ _30 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bhi","$3")), null, null, 0, false, false, new int[]{74,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[229] = new Rule(229, false, false, 6, "229: void -> (JUMPC _ _31 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bhs","$3")), null, null, 0, false, false, new int[]{75,7,7}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[244] = new Rule(244, false, false, 6, "244: void -> (JUMPC _ _32 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("beq","$3")), null, null, 0, false, false, new int[]{77,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[246] = new Rule(246, false, false, 6, "246: void -> (JUMPC _ _33 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bne","$3")), null, null, 0, false, false, new int[]{78,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[248] = new Rule(248, false, false, 6, "248: void -> (JUMPC _ _34 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("blt","$3")), null, null, 0, false, false, new int[]{79,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[250] = new Rule(250, false, false, 6, "250: void -> (JUMPC _ _35 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("ble","$3")), null, null, 0, false, false, new int[]{80,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[252] = new Rule(252, false, false, 6, "252: void -> (JUMPC _ _36 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bgt","$3")), null, null, 0, false, false, new int[]{81,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[254] = new Rule(254, false, false, 6, "254: void -> (JUMPC _ _37 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bge","$3")), null, null, 0, false, false, new int[]{82,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[256] = new Rule(256, false, false, 6, "256: void -> (JUMPC _ _38 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("blo","$3")), null, null, 0, false, false, new int[]{83,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[258] = new Rule(258, false, false, 6, "258: void -> (JUMPC _ _39 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bls","$3")), null, null, 0, false, false, new int[]{84,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[260] = new Rule(260, false, false, 6, "260: void -> (JUMPC _ _40 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bhi","$3")), null, null, 0, false, false, new int[]{85,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[262] = new Rule(262, false, false, 6, "262: void -> (JUMPC _ _41 label label)", ImList.list(ImList.list("cmfe","$1","$2"),ImList.list("bhs","$3")), null, null, 0, false, false, new int[]{86,7,7}, new String[]{null, "*reg-F64*", "*reg-F64*", null, null});
    rulev[196] = new Rule(196, false, false, 6, "196: void -> (CALL _ sta)", ImList.list(ImList.list("bl","$1")), null, null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[197] = new Rule(197, false, false, 6, "197: void -> (CALL _ regw)", ImList.list(ImList.list("mov","%lr","%pc"),ImList.list("mov","%pc","$1")), null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-nlr-I32*"});
    rulev[198] = new Rule(198, false, false, 6, "198: void -> (CALL _ memw)", ImList.list(ImList.list("mov","%lr","%pc"),ImList.list("mov","%pc","$1")), null, null, 0, false, false, new int[]{37}, new String[]{null, null});
    rulev[276] = new Rule(276, false, false, 6, "276: void -> (PARALLEL _ _44 _46)", ImList.list(ImList.list("stfs","$1",ImList.list("preinc","sp","-4"))), null, null, 0, false, false, new int[]{89,91}, new String[]{null, "*reg-F32*"});
    rulev[281] = new Rule(281, false, false, 6, "281: void -> (PARALLEL _ _48 _50)", ImList.list(ImList.list("stfd","$1",ImList.list("preinc","sp","-8"))), null, null, 0, false, false, new int[]{93,95}, new String[]{null, "*reg-F64*"});
    rulev[286] = new Rule(286, false, false, 6, "286: void -> (PARALLEL _ _52 _54)", ImList.list(ImList.list("ldmfd","sp!",ImList.list("regpair1","$1"))), null, null, 0, false, false, new int[]{97,99}, new String[]{null, "*reg-nlr-I32*"});
    rulev[291] = new Rule(291, false, false, 6, "291: void -> (PARALLEL _ _52 _56 _58)", ImList.list(ImList.list("ldmfd","sp!",ImList.list("regpair2","$1","$2"))), null, null, 0, false, false, new int[]{97,101,103}, new String[]{null, "*reg-nlr-I32*", "*reg-nlr-I32*"});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 514: return "*reg-nlr-I32*";
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
    if (name == "vb0")
      return jmac15(form.elem(1));
    else if (name == "vb1")
      return jmac16(form.elem(1));
    else if (name == "vb2")
      return jmac17(form.elem(1));
    else if (name == "vb3")
      return jmac18(form.elem(1));
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
    if (name == "lit")
      return jmac1(emitObject(form.elem(1)));
    else if (name == "litf")
      return jmac2(emitObject(form.elem(1)));
    else if (name == "litd")
      return jmac3(emitObject(form.elem(1)));
    else if (name == "pls")
      return jmac4(emitObject(form.elem(1)));
    else if (name == "neg")
      return jmac5(emitObject(form.elem(1)));
    else if (name == "pind")
      return jmac6(emitObject(form.elem(1)));
    else if (name == "mind")
      return jmac7(emitObject(form.elem(1)));
    else if (name == "lsl")
      return jmac8(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "asr")
      return jmac9(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "lsr")
      return jmac10(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "amode2")
      return jmac11(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "amode3")
      return jmac12(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "hash")
      return jmac13(emitObject(form.elem(1)));
    else if (name == "adjDisp")
      return jmac14(emitObject(form.elem(1)));
    else if (name == "b0")
      return jmac19(emitObject(form.elem(1)));
    else if (name == "b1")
      return jmac20(emitObject(form.elem(1)));
    else if (name == "h0")
      return jmac21(emitObject(form.elem(1)));
    else if (name == "u0")
      return jmac22(emitObject(form.elem(1)));
    else if (name == "preinc")
      return jmac23(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regpair2")
      return jmac24(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "regpair1")
      return jmac25(emitObject(form.elem(1)));
    else if (name == "regwlow")
      return jmac26(emitObject(form.elem(1)));
    else if (name == "regblow")
      return jmac27(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac28(form.elem(1));
    else if (name == "epilogue")
      return jmac29(form.elem(1), emitObject(form.elem(2)));
    else if (name == "deflabel")
      return jmac30(emitObject(form.elem(1)));
    else if (name == "line")
      return jmac31(emitObject(form.elem(1)));
    else if (name == "genasm")
      return jmac32(emitObject(form.elem(1)), form.elem(2));
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
  boolean isSimulate = false;
  
  void initializeMachineDep() {
      peepHoleOpts.put("_mov", decompMov);
      peepHoleOpts.put("_add", decompAdd);
      peepHoleOpts.put("_or", decompOr);
      peepHoleOpts.put("_bic", decompBic);
  
      Thread th = Thread.currentThread();
      if (th instanceof CompileThread) {
        IoRoot io = ((CompileThread) th).getIoRoot();
        CompileSpecification spec = io.getCompileSpecification();
        CoinsOptions coinsOptions = spec.getCoinsOptions();
        if (coinsOptions.isSet("simulate")) {
          isSimulate = true;
        }
      }
  }
  
  
  /** Return late time pre-rewriting sequence. **/
  public Transformer[] lateRewritingSequence() {
      return new Transformer[] {
  	localLateRewritingTrig,
  	testJumpcTrig,
  	ProcessFramesTrig
      };
  }
  
  
  /** Examine conditional jump **/
  final LocalTransformer testJumpcTrig = new LocalTransformer() {
  	public boolean doIt(Function func, ImList args) {
  	    FlowGraph cfg = func.flowGraph();
  
  	    for (BiLink p = cfg.basicBlkList.first();
  		 !p.atEnd();
  		 p = p.next()) {
  		BasicBlk blk = (BasicBlk) p.elem();
  		for (BiLink inst = ((BiList) blk.instrList()).first();
  		     !inst.atEnd(); inst = inst.next()) {
  		    LirNode lir = (LirNode) inst.elem();
  		    if (lir.opCode == Op.JUMPC) {
  			examineJUMPC(p, lir);
  		    }
  		}
  	    }
  	    return true;
  	}
  	
  	public boolean doIt(Data data, ImList args) { return true; }
        
  	public String name() { return "TestJumpc"; }
  	
  	public String subject() { return "Examin conditional jump"; }
      };
  
  // check bb has only one pred.
  boolean checkPred(BiLink q, BasicBlk bb)
  {
      BiList pred = ((BasicBlk) q.elem()).predList();
  
      if (pred.length() != 1) { // has incoming jump
  	return false;
      }
      return ((BasicBlk) pred.first().elem()).id == bb.id;
  }
  
  boolean checkTarget(Label lab, BiLink q)
  {
      if (!(q.elem() instanceof BasicBlk)) {
  	return false;
      }
      return lab.name().equals(
  			     ((BasicBlk) q.elem()).label().name()
  			     );
  }
  
  boolean checkSucc(BiLink p, BasicBlk bb)
  {
      BiList succ = ((BasicBlk) p.elem()).succList();
  
      if (succ.length() != 1) {
  	return false;
      }
      return ((BasicBlk) succ.first().elem()).id == bb.id;
  }
  
  // todo: Check float ?
  // maximum length of conditional execution.
  private final static int MAX_COND_LEN = 8;
  boolean checkBody(BasicBlk bb)
  {
      int n = 0;
  
      BiList l = bb.instrList();
      for (BiLink p = l.first(); !p.atEnd(); p = p.next()) {
  	LirNode tmp = (LirNode) p.elem();
  	switch (tmp.opCode) {
  	case Op.JUMPC:
  	case Op.CALL:
  	case Op.PARALLEL:
  	case Op.DIVS:
  	case Op.DIVU:
  	case Op.MODS:
  	case Op.MODU:
  	    debug("::: too complex " + bb);
  	    return false;
  	default:
  	    debug("OK: " + tmp);
  	    break;
  	}
  	if (++n > MAX_COND_LEN) {
  	    debug("::: too long " + bb);
  	    return false;
  	}
      }
      return true;
  }
  
  
  void examineJUMPC(BiLink p, LirNode node) {
      BasicBlk blk = (BasicBlk) p.elem();
      int op = node.kid(0).opCode;
  
      // NB: conditinal code is flipped somewhere, 
      LirNode y = node.kid(2);
      LirNode z = node.kid(1);
  
      Label falseTarget = ((LirLabelRef) y).label;
      Label trueTarget = ((LirLabelRef) z).label;
      
      debug2(" then target " + z + " -> " + trueTarget);
      debug2(" else target " + y + " -> " + falseTarget);
  
      BiLink q = null, r = null, s = null;
      if (!p.next().atEnd()) {
  	q = p.next();
  	if (!q.next().atEnd()) {
  	    r = q.next();
  	    if (!r.next().atEnd()) {
  		s = r.next();
  	    }
  	}
      }
      
      if (q == null || r == null) {
  	// bad lir ?
  	debug("examineJUMPC: bad lir ?");
  	return;
      }
  
      // check common condition
      if (!checkPred(q, blk)
  	|| !checkTarget(trueTarget, q) 
  	|| !checkTarget(falseTarget, r)
  	|| !checkBody((BasicBlk) q.elem())) {
  	debug2(" cexec no ");
  	return;
      }
      // now ensured: trueTarget = q &&  falseTarget = r
  
      // if-then ?
      if (checkSucc(q, (BasicBlk) r.elem())) {
  	debug2("if-then type: " + blk.label());
  	debug2("  -> OK, target: " + trueTarget.name());
  	jumpcInfo.put(trueTarget.name().intern(),
  		      new JumpcEntry(op2cc(op), trueTarget.name()));
  	return;
      }
  
      // if-then-else ?
      if (s == null
  	|| !checkPred(r, blk)
  	|| !checkSucc(q, (BasicBlk) s.elem())
  	|| !checkSucc(r, (BasicBlk) s.elem())){
  	debug2(" cexec no");
  	return;
      }
  
      if (!checkBody((BasicBlk) r.elem())) {
  	debug2("if-then-elseNG type: " + blk.label());
  	debug2(" -> OK, target: " + trueTarget.name());
  	jumpcInfo.put(trueTarget.name().intern(),
  		      new JumpcEntry(op2cc(op), trueTarget.name(), null));
  	return;
      }
      debug2("if-then-else type: " + blk.label());
      debug2(" -> OK, target: " + trueTarget.name() + ", " + falseTarget.name());
      jumpcInfo.put(trueTarget.name().intern(),
  		  new JumpcEntry(op2cc(op), trueTarget.name(), falseTarget.name()));
      return;
  }
  
  
  private LiteralAndBranchProcessor pp;
  
  final class Arm extends CPU {
      Arm() {
  	bccRange = new int[] {-0x7ffffff0, 0x7ffffff0};
  	braRange = new int[] {-0x7ffffff0, 0x7ffffff0};
  	literalRange = new int[] {-800, 800};
  	bccMnemo = new String[] { "beq", "bne",
  				  "bhs", "blo", "bhi", "bls",
  				  "bge", "blt", "bgt", "ble" };
  	braMnemo = "b";
  	braLength = 4;
  	codeAlign = 2;
      }
  
      public int codeLength(String inst)
      {
  	return 4;
      }
  
      public String toString() {
  	return "Arm";
      }
  }
  
  /** Run Literal and Branch post processor after generating assembly code. **/
  OutputStream insertPostProcessor(OutputStream out) {
     pp = LiteralAndBranchProcessor.postProcessor(out);
     pp.setCPU(new Arm());
     return pp.pipeTo();
  }
  
  void notifyEndToPostProcessor() {
     pp.notifyEnd();
  }
  
  
  ImList regCallClobbers = new ImList(ImList.list("REG","I32","%r0"), new ImList(ImList.list("REG","I32","%r1"), new ImList(ImList.list("REG","I32","%r2"), new ImList(ImList.list("REG","I32","%r3"), new ImList(ImList.list("REG","I32","%ip"), ImList.list(ImList.list("REG","I32","%lr"),ImList.list("REG","F64","%f0"),ImList.list("REG","F64","%f1"),ImList.list("REG","F64","%f2"),ImList.list("REG","F64","%f3")))))));
  int decompMax = 4;			// maximum decompsition
  
  int tmpCnt = 0;
  
  Map conditionalExec = new HashMap();
  Map jumpcInfo = new HashMap();
  
  /** Arm's function attribute **/
  static class ArmAttr extends FunctionAttr {
    /** Maximum stack space used by call. **/
    /** int stackRequired **/
  
    /** pretend value. */
    /* bytes count of register passed parameter which copied to stack on
       prologue. */
    /** int pretend; **/
  
    /* bytes count of register passed parameter which copied to stack on
       prologue on variadic argument. */
    /** int pretend2; **/
  
    /** pointer of aggregate return value. */
    /** LirNode hiddenPtr **/
  
    /** is variadic ? */
    /** boolean variadic **/
  
    /** callee save register */
    int adjustCalleeSaves;
  
    ArmAttr(Function func) {
      super(func);
      adjustCalleeSaves = -1;
    }
  }
  
  FunctionAttr newFunctionAttr(Function func) {
    ArmAttr attr = new ArmAttr(func);
    // in simulation mode, save all registers.
    if (isSimulate) {
      attr.adjustCalleeSaves = CALLEE_SAVE;
    }
    return attr;
  }
  
  
  
  /** Arm constant */
  class ArmConst {
      private class RotatedValue {
  	private long value;
  	private int lsr;
  
  	RotatedValue(long val, int n) {
  	    value = val & UINT_MAX;
  	    lsr = n;
  	}
  	RotatedValue(long val) {
  	    this(val, 0);
  	}
  
  	long value() {
  	    return value;
  	}
  
  	int lsr() {
  	    return lsr;
  	}
  
  	private void incLsr() {
  	    lsr++;
  	    if (lsr >= 32) {
  		lsr -= 32;
  	    }
  	}
  	private void decLsr() {
  	    lsr--;
  	    if (lsr <= -1) {
  		lsr += 32;
  	    }
  	}
  
  	void ror() {
  	    incLsr();
  	    value = ((value << 31) | (value >>> 1)) & UINT_MAX;
  	}
  
  	void rol() {
  	    decLsr();
  	    value = ((value << 1) | (value >>> 31)) & UINT_MAX;
  	}
  
  	void ror2(int n) {
  	    for (int i = 0; i < n; i++) {
  		ror(); ror();
  	    }
  	}
  	void rol2(int n) {
  	    for (int i = 0; i < n; i++) {
  		rol(); rol();
  	    }
  	}
  	void shr8() {
  	    lsr += 8;
  	    if (lsr > 32) {
  		lsr -= 32;
  	    }
  	    value = (value >>> 8) & UINT_MAX;
  	}
  
  	public String toString() {
  	    return "0x" + 
                  Long.toString((value << lsr) & UINT_MAX | (value >>> (32 - lsr)) & UINT_MAX, 16);
  	    // return Long.toString(value << lsr, 10);
  	}
  
  	void minimize() {
  	    int n = lsr;
  	    long min = value;
  
  	    for (int i = 1; i <= 16; i++) {
  		ror2(1);
  		if (value < min) {
  		    min = value;
  		    n = lsr;
  		}
  	    }
  	    value = min;
  	    lsr = n;
  	}
  
      }
  
      private long rawValue;
      private RotatedValue val;
  
      private static final long UINT_MAX = 0xffffffffL;
  
      private String toHex(int val) {
  	return toHex((long) val);
      }
      private String toHex(long val) {
  	return Long.toString(val & UINT_MAX, 16);
      }
  
      public ArmConst(long val) {
  	rawValue = val & UINT_MAX;
  	this.val = new RotatedValue(rawValue);
      }
  
      RotatedValue b[] = new RotatedValue[4];
      int nofBytes = 0;
      int normalize() {
  	int bi = 0;
  	if (rawValue == 0) {
  	    return nofBytes = 1;
  	}
  	while (val.value() != 0) {
  	    val.minimize();
  
  	    b[bi] = new RotatedValue(val.value() & 0xff, val.lsr());
  	    bi++;
  	    val.shr8();
  	}
  	if (false) {
  	    System.out.println("-- result");
  	    for (int i = 0; i < bi; i++) {
  		System.out.println("  " + i + ": " + b[i]);
  	    }
  	}
  	return nofBytes = bi;
      }
      public String get(int off) {
        if (rawValue == 0) {
          return "0";
        }
        if (b[off] == null) {
          return "0";
        }
        return String.valueOf(b[off]);
      }
  
      public void print() {
  	System.out.println("value: " + toHex(rawValue));
  	//	System.out.println("  = " + rotated + " lsl " + lsr);
  	for (int i = 0; i < nofBytes; i++) {
  	    System.out.println("  " + i + ": " + (b[i].value() + ":" + b[i].lsr()));
  	}
      }
  }
  
  boolean isImm8r(long val) {
      ArmConst c = new ArmConst(val);
      c.normalize();
      return c.nofBytes == 1;
  }
  
  boolean isXimm(long val) {
      ArmConst c = new ArmConst(val);
      c.normalize();
      return c.nofBytes < 3;
  }
  
  int nofBytes(long val) {
      ArmConst c = new ArmConst(val);
      c.normalize();
      int n = c.nofBytes;
      debug("nofByte(" + val + ") = " + n);
      return n <= decompMax ? n : 5;
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
    ArmAttr at = (ArmAttr) getFunctionAttr(func);
    at.isVarArg = true;
    at.stackParamOffset = MAXREGPARAM;
    return paramOffset(arg) + 8;	// not 4
  }
  
  static final int I64 = Type.type(Type.INT, 64);
  static final int I32 = Type.type(Type.INT, 32);
  static final int I16 = Type.type(Type.INT, 16);
  static final int I8 = Type.type(Type.INT, 8);
  static final int F64 = Type.type(Type.FLOAT, 64);
  static final int F32 = Type.type(Type.FLOAT, 32);
  
  static final int MAXREGPARAM = 4;	// regs
  static final int REGWIDTH = 4;		// bytes
  
  static final int CALLEE_SAVE = 28;	// r4-r10
  static final String NEED_OFFSET_REWRITE = "&vframe";
  
  
  static final String[] paramRegv = new String[]{"%r0", "%r1", "%r2", "%r3"};
  
  /** Return number of registers multiply word-size. **/
  int clcvnRegLimit() {  return MAXREGPARAM * 4;  }
  
  /** Return parameter word **/
  LirNode clcvnParamWord(int type, int location, boolean caller) {
    ArmAttr at = (ArmAttr) getFunctionAttr(func);
    if (location < (caller ? MAXREGPARAM * 4 : at.stackParamOffset))
      return clcvnParamReg(type, location, caller);
    else
      return clcvnParamMem(type, location, caller);
  }
  
  /** Return parameter register **/
  LirNode clcvnParamReg(int type, int location, boolean caller) {
    LirNode master = phyReg(paramRegv[location >> 2]);
    if (type == master.type)
      return master;
    else if (type == I16)
      return lir.node
        (Op.SUBREG, I16, master, lir.untaggedIconst(I32, 0));
    else if (type == I8)
      return lir.node
        (Op.SUBREG, I8, master, lir.untaggedIconst(I32, 0));
    else {
      throw new CantHappenException("type: " + type);
    }
  }
  
  /** Return parameter memory **/
  LirNode clcvnParamMem(int type, int location, boolean caller) {
    ArmAttr at = (ArmAttr) getFunctionAttr(func);
    return lir.node
      (Op.MEM, type,
       lir.node
       (Op.ADD, I32, phyReg(caller ? "%sp" : "%fp"),
        lir.iconst(I32, (caller ? (location - 4*MAXREGPARAM)
                                : 4 + (location - at.stackParamOffset)))));
  }
  
  /** Return offset of parameter (callee side) **/
  int clcvnParamOffset(int location) {
    ArmAttr at = (ArmAttr) getFunctionAttr(func);
    return 4 + (location - at.stackParamOffset);
  }
  
  /** Pass floating point number to register/ register and memory. **/
  void clcvnPassFloatRegMem(int location, LirNode arg, BiLink memp, BiLink regp, BiList alist) {
    int w = Type.bytes(arg.type);
    LirNode sp = phyReg("%sp");
  
    LirNode r = phyReg(paramRegv[location >> 2]);
    if (location + w <= MAXREGPARAM * 4) {
      // pass by register
      // push float register on stack
      memp.addBefore
        (lir.node
         (Op.PARALLEL, Type.UNKNOWN, lir.node
          (Op.SET, I32, sp, lir.node
           (Op.SUB, I32, sp, lir.iconst(I32, w))), lir.node
          (Op.SET, arg.type, lir.node(Op.MEM, arg.type, sp), arg)));
  
      // pop Ixx
      if (w > 4) {
        LirNode r2 = phyReg(paramRegv[(location >> 2) + 1]);
        regp.addBefore
          (lir.node
           (Op.PARALLEL, Type.UNKNOWN, lir.node
            (Op.SET, I32, r, lir.node(Op.MEM, I32, sp)), lir.node
            (Op.SET, I32, r2, lir.node
             (Op.MEM, I32, lir.node(Op.ADD, I32, sp, lir.iconst(I32, 4)))), lir.node
            (Op.SET, I32, sp, lir.node(Op.ADD, I32, sp, lir.iconst(I32, 8)))));
      } else {
        regp.addBefore
          (lir.node
           (Op.PARALLEL, Type.UNKNOWN, lir.node
            (Op.SET, I32, r, lir.node(Op.MEM, I32, sp)), lir.node
            (Op.SET, I32, sp, lir.node(Op.ADD, I32, sp, lir.iconst(I32, 4)))));
      }
      
    } else {
      // load first half on register, last half on memory.
      if (w != 8)
        throw new CantHappenException("double expected");
      // store float register on stack
      memp.addBefore
        (lir.node
          (Op.SET, I32, sp, lir.node
           (Op.SUB, I32, sp, lir.iconst(I32, 4))));
      memp.addBefore
        (lir.node
         (Op.SET, arg.type, lir.node(Op.MEM, arg.type, sp), arg));
  
      // pop register
      regp.addBefore
        (lir.node
         (Op.PARALLEL, Type.UNKNOWN, lir.node
          (Op.SET, I32, r, lir.node(Op.MEM, I32, sp)), lir.node
          (Op.SET, I32, sp, lir.node(Op.ADD, I32, sp, lir.iconst(I32, 4)))));
    }
  }
  
  /** Return return register **/
  LirNode clcvnReturnValue(int type) {
    if (Type.tag(type) == Type.INT) {
      return regnode(type, "%r0");
    } else {
      return regnode(type, "%f0");
    }
  }
  
  /** Return partial word of integer object **/
  LirNode clcvnPartialWord(LirNode exp, int part) {
    if (exp.type == I64)
      return lir.node(Op.SUBREG, I32, exp, lir.untaggedIconst(I32, part));
    else
      return exp;
  }
  
  /** Set partial word of integer object **/
  LirNode clcvnSetPartialWord(LirNode lhs, int part, LirNode rhs) {
    if (lhs.type == I64)
      return lir.node
        (Op.SET, I32, lir.node
         (Op.SUBREG, I32, lhs, lir.untaggedIconst(I32, part)), rhs);
    else
      return lir.node(Op.SET, lhs.type, lhs, rhs);
  }
  
  /** Return clobber list **/
  ImList clcvnClobbers() {
    return regCallClobbers;
  }
  
  /** Return true if struct return address **/
  boolean clcvnStructReturnAsFirst() { return true; }
  
  /** clcvnStructReturnPtr(boolean caller): not neccesary **/
      
  
  
  
  
  
  
  private void debug(String s)
  {
      //System.out.println(s);
  }
  
  private void debug2(String s)
  {
      //System.out.println(":::" + s);
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
      Symbol fp = func.module.globalSymtab.get("%fp");
      Symbol vfp = func.module.globalSymtab.get("%vfp");
  
      //    System.out.println("func ; " + func);
  	
    if (!(node instanceof LirSymRef)) {
        bug("rewriteFrame() requires LirSymRef: " + node);
        return node;
    }
    Symbol sym = ((LirSymRef)node).symbol;
  
    if (!(sym instanceof SymAuto)) {
        bug("rewriteFrame() requires SymAuto: " + sym);
    }
  
    int off = ((SymAuto) sym).offset();
    if (off < 0) {
        //      System.out.println("*** rewriting frame " + node + " offset = " + off);
        off -= 12 + CALLEE_SAVE;		// maximize off
        debug(" -> " + off);
        if (isSimulate) {
          // simulation mode
          return addExp(I32, lir.symRef(fp), lir.iconst(I32, (long)off));
        } else {
          // rewrite offset later, returns "vfp + disp" here.
          return addExp(I32, lir.symRef(vfp), lir.iconst(I32, (long)off));
        }
    }
    /* else */
    // offset is already fixed.
    return addExp(I32, lir.symRef(fp), lir.iconst(I32, (long)off));
  }
  
  
  private LirNode regnode(int type, String name) {
      LirNode master = lir.symRef(module.globalSymtab.get(name));
      switch (Type.tag(type)) {
      case Type.INT:
  	if (type == I32)
  	    return master;
  	else if (type == I16)
  	    return lir.node
  		(Op.SUBREG, I16, master, lir.untaggedIconst(I32, 0));
  	else if (type == I8)
  	    return lir.node
  		(Op.SUBREG, I8, master, lir.untaggedIconst(I32, 0));
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
  
  
  
  
  /***********/
  
  
  
  
  
  
  
  class JumpcKey {
      Function func;
      int id;
      JumpcKey(Function f, int i) {
  	func = f;
  	id = i;
      }
  }
  
  class JumpcEntry {
      final String cc;
      final String trueTarget;
      final String falseTarget;
      final boolean ifThenElse;
  
      JumpcEntry(String cc, String trueTarget) {
  	this.cc = cc;
  	ifThenElse = false;
  	this.trueTarget = trueTarget;
  	this.falseTarget = "";
      }
      JumpcEntry(String cc, String trueTarget, String falseTarget) {
  	this.cc = cc;
  	ifThenElse = true;
  	this.trueTarget = trueTarget;
  	this.falseTarget = falseTarget;
      }
      public String toString() {
  	return "[cc = " + cc
  	    + ", label1 = " + trueTarget
  	    + ", label1 = " + falseTarget + "]";
      }
  }
  
  static final Map flipCc = new HashMap();
  static {
      flipCc.put("eq", "ne");
      flipCc.put("ne", "eq");
      flipCc.put("lt", "ge");
      flipCc.put("le", "gt");
      flipCc.put("gt", "le");
      flipCc.put("ge", "lt");
      flipCc.put("lo", "hs");
      flipCc.put("ls", "hi");
      flipCc.put("hi", "ls");
      flipCc.put("hs", "lo");
  }
  
  String op2cc(int op)
  {
      switch (op) {
      case Op.TSTEQ:
  	return "eq";
      case Op.TSTNE:
  	return "ne";
      case Op.TSTLTS:
  	return "lt";
      case Op.TSTLES:
  	return "le";
      case Op.TSTGTS:
  	return "gt";
      case Op.TSTGES:
  	return "ge";
      case Op.TSTLTU:
  	return "lo";
      case Op.TSTLEU:
  	return "ls";
      case Op.TSTGTU:
  	return "hi";
      case Op.TSTGEU:
  	return "hs";
      default:
  	return "badOp";
      }
  }
  
  
  /*
   * Code building macros.
   */
  
  /* none */
  
  /* Code emission macros.
   *  Patterns not defined below will be converted to:
   *   (foo bar baz) --> foo   bar,baz   or foo(bar,baz)
   */
  
  /* literal */
  String jmac1(String x) {
    return "=W" + x;
  }
  
  String jmac2(String x) {
    double value = Float.parseFloat(x);
    long bits = Float.floatToIntBits((float)value);
    return "=W" + Long.toString(bits & 0xffffffffL, 10) + " @ " + x;
  }
    
  String jmac3(String x) {
    double value = Double.parseDouble(x);
    long bits = Double.doubleToLongBits(value);
    return "=D0x" + Long.toString((bits >>> 32) & 0xffffffffL, 16)
         + "_0x"  + Long.toString(bits & 0xffffffffL, 16) + " @ " + x;
  }
  
  
  /* constant */
  String jmac4(String x) {
    return "" + x;
  }
  
  String jmac5(String x) {
    return "-" + x;
  }
  
  String jmac6(String x) {
    return x;
  }
  
  String jmac7(String x) {
    return "-" + x;
  }
  
  
  /* shift */
  String jmac8(String x, String y) {
    return x + ", lsl " + y;
  }
  String jmac9(String x, String y) {
    return x + ", asr " + y;
  }
  String jmac10(String x, String y) {
    return x + ", lsr " + y;
  }
  
  /* amode */
  String jmac11(String x, String y) {
      if (y.length() > 0) {
  	return "[" + x + ", " + y + "]";
      }
      return "[" + x + "]";
  }
  
  String jmac12(String x, String y) {
      if (y.length() > 0) {
  	return "[" + x + ", " + y + "]";
      }
      return "[" + x + "]";
  }
  
  String jmac13(String x) { return "#" + x; }
  
  String jmac14(String x) {
      ArmAttr attr = (ArmAttr) getFunctionAttr(func);
      // System.out.println(" adjDisp " + x + " <- callee save " + attr.adjustCalleeSaves);
      long off = Long.parseLong(x) + CALLEE_SAVE - attr.adjustCalleeSaves;
      // System.out.println(" -> " + off);
      return String.valueOf(off);
  }
  
  Object jmac15(Object x) {
      ArmAttr attr = (ArmAttr) getFunctionAttr(func);
      if (attr.adjustCalleeSaves < 0) {
  	SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
  	attr.adjustCalleeSaves = 0;
  	for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
  	    int reg = it.next();
  	    attr.adjustCalleeSaves += REGWIDTH;
  	}
      }
      long off = Long.parseLong(x.toString()) + CALLEE_SAVE - attr.adjustCalleeSaves;
      ximm = new ArmConst(-off);
      ximm.normalize();
      return ImList.list("#" + ximm.get(0));
  }
  Object jmac16(Object x) {
      return ImList.list("#" + ximm.get(1));
  }
  Object jmac17(Object x) {
      return ImList.list("#" + ximm.get(2));
  }
  Object jmac18(Object x) {
      return ImList.list("#" + ximm.get(3));
  }
  
  /** decompose immediate **/
  ArmConst ximm = null;	// working temporal.
  
  String jmac19(String x) {
    debug("decomposing " + x);
    ximm = new ArmConst(Long.parseLong(x));
    ximm.normalize();
  
    return "#" + ximm.get(0);
  }
  
  String jmac20(String x) {
    return "#" + ximm.get(1);
  }
  
  
  String jmac21(String x) {
    debug("decomposing " + x);
  
    ximm = new ArmConst(Long.parseLong(x) & 0xffff);
    ximm.normalize();
  
    return "#" + ximm.get(0);
  }
  
  String jmac22(String x) {
    return "#" + (Long.parseLong(x) & 0xff);
  }
  
  
  /* special rules */
  String jmac23(String x, String y) {
      return "[" + x + ", #" + y + "]!";
  }
  
  String jmac24(String x, String y) {
      return "{" + x + ", " + y + "}";
  }
  
  String jmac25(String x) {
      return "{" + x + "}";
  }
  
  
  /** Return lower half register name. **/
  String jmac26(String x) { return "%" + x.substring(2); }
  
  /** Return lowest byte register name. **/
  String jmac27(String x) { return "%" + x.substring(2, 3) + "l"; }
  
  /** Generate prologue sequence. **/
  String jmac28(Object f) {
      Function func = (Function)f;
      SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
      int size = frameSize(func);
      ArmAttr attr = (ArmAttr) getFunctionAttr(func);
      if (!isSimulate) {
        attr.adjustCalleeSaves = 0;
      }
  
      int forCall = attr.requiredStack;
      forCall -= 4*MAXREGPARAM;
      if (forCall < 0) forCall = 0;
      size += forCall;
  
      debug("**** defemit prologue: frame size " + size);
  
      size = (size + 3) & -4; // round up to 4byte boundary !!
      // fixme ? (armgcc aligns 8 byte boundary ?)
  
      String seq = "\n@ prologue\n\tmov\tip, sp";
  
      if (false) {
        if (attr.stackParamOffset < 4*MAXREGPARAM) {
  	seq += "\n\tstmfd\tsp!, {";
  	for (int i = attr.stackParamOffset/4; i < 4*MAXREGPARAM; i++) {
            seq += "r" + i;
            if (i == MAXREGPARAM - 1) {
              seq += "}";
            } else {
              seq += ", ";
            }
  	}
        }
      }
  
      /* String seq2 = "\n\tstmfd\tsp!, {fp, ip, lr, pc}";*/
      /* push all callee save registers. */
      String seq2 = "\n\tstmfd\t%sp!, {";
      if (isSimulate) { //
        // save all registers.
        seq2 += "%r4,%r5,%r6,%r7,%r8,%r9,%r10,";
      } else {
        for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
  	int reg = it.next();
  	seq2 += machineParams.registerToString(reg) + ", ";
  	attr.adjustCalleeSaves += REGWIDTH;
        }
      }
      //System.out.println("*** " + attr);
      //System.out.println("*** callee save " + attr.adjustCalleeSaves);
      seq2 += "%fp, %ip, %lr, %pc}";
      
      if (attr.stackParamOffset < 4*MAXREGPARAM) {
  	// saving some register value onto stack in prologue of
  	// non-variadic function
  	seq += "\n\tsub\tsp, sp, #" + (4*MAXREGPARAM - attr.stackParamOffset);
      }
      seq2 += "\n\tsub\tfp, ip, #" + (4 + (4*MAXREGPARAM - attr.stackParamOffset));
  
      if (size > 0) {
  	// NB: need constant rewrite.
  	if (size + attr.adjustCalleeSaves > 1024) {
  	    seq2 += "\n\tldr\tlr, =W" +  (size + attr.adjustCalleeSaves) + "\n\tsub\tsp, sp, lr";
  	} else {
  	    seq2 += "\n\tsub\tsp, sp, #" + (size + attr.adjustCalleeSaves) + "\t@frame 2";
  	}
      } else if (size == 0) {
  	// do nothing
      } else {
  	/* bug */
      }
  
      return seq + seq2;
  }
  
  /** Generate epilogue sequence. **/
  String jmac29(Object f, String rettype) {
      Function func = (Function)f;
      SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
      // pop all callee save registers.
      String seq = "\n@ epilgue\n\tldmea\t%fp, {";
      if (isSimulate) { ////
        seq += "%r4,%r5,%r6,%r7,%r8,%r9,%r10,";
      } else {
        for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
  	int reg = it.next();
  	seq += machineParams.registerToString(reg) + ", ";
        }
      }
      seq += "%fp, %sp, %pc}\n";
      seq += ".ltorg\n";
      return seq;
  }
  
  
  String jmac30(String x) { return x + ":"; }
  
  String jmac31(String x) { return "@line " + x; }
  
  String jmac32(String format, Object args) { return emitAsmCode(format, (ImList)args); }
  
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
      if (symbol.linkage == "LDEF")
  	out.println("\t.local\t" + symbol.name);
      out.println("\t.common\t" + symbol.name + "," + bytes + ","
  		+ symbol.boundary);
  }
  
  /** Emit linkage information of symbol */
  void emitLinkage(PrintWriter out, SymStatic symbol) {
      if (symbol.linkage == "XDEF")
  	out.println("\t.global\t" + symbol.name);
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
  	out.println("\t.long\t0x" + Long.toString((bits >> 32) & 0xffffffffL, 16)
  		    + " @ " + value);
  	out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16));
      }
      else if (type == F32) {
  	double value = ((LirFconst)node).value;
  	long bits = Float.floatToIntBits((float)value);
  	out.println("\t.long\t0x" + Long.toString(bits & 0xffffffffL, 16)
  		    + " @ " + value);
      }
      else {
  	throw new CantHappenException("unknown type: " + type);
      }
  }
  
  /** Emit asm instruction. **/
  String emitAsmCode(String format, ImList args) {
    StringBuffer buf = new StringBuffer();
    int n = format.length();
    for (int i = 0; i < n; ) {
      char c = format.charAt(i++);
      if (c == '%' && i < n) {
        c = format.charAt(i++);
        if (c == '%')
  	buf.append(c);
        else if (Character.isDigit(c)) {
  	if (args.elem(c - '1') instanceof Long) { // adjust disp
  	  Long tmp = (Long) args.elem(c - '1');
  	  ArmAttr attr = (ArmAttr) getFunctionAttr(func);
  	  long off = tmp.longValue() + CALLEE_SAVE - attr.adjustCalleeSaves;
  	  buf.append(Long.toString(off));
  	} else {
  	  buf.append(args.elem(c - '1').toString());
  	}
        }
        else {
  	buf.append('%');
  	buf.append(c);
        }
      } else
        buf.append(c);
    }
    return buf.toString();
  }
  
  // delete redundant sub r, r, #0
  private Object car(Object l) {
      return ((ImList) l).elem();
  }
  private Object cdr(Object l) {
      return ((ImList) l).next();
  }
  
  private boolean isDelSub(ImList tmp) {
      String dst = (String) car(cdr(tmp));
      String src = (String) car(cdr(cdr(tmp)));
      if (dst.equals(src)) {
  	Object imm = car(cdr(cdr(cdr(tmp))));
  	if (imm.equals("#0")) {
  	    return true;
  	}
      }
      return false;
  }
  
  
  ImList addCc(Object cc, ImList inst) {
      String oldinst = car(inst).toString();
      String newinst = oldinst + cc; // default
  
      if (oldinst.equals("bl")) {
  	System.err.println("bl in conditional execution sequence. " + inst);
      } else if (oldinst.startsWith("ldr")
  	|| oldinst.startsWith("str")
  	|| oldinst.startsWith("ldm")
  	|| oldinst.startsWith("stm")
  	|| oldinst.startsWith("ldf")
  	|| oldinst.startsWith("stf")
  	|| oldinst.startsWith("adf")
  	|| oldinst.startsWith("suf")
  	|| oldinst.startsWith("rsf")
  	|| oldinst.startsWith("muf")
  	|| oldinst.startsWith("dvf")
  	|| oldinst.startsWith("mvf")
  	|| oldinst.startsWith("mnf")
  	|| oldinst.startsWith("flt")
  	|| oldinst.startsWith("fix")
  	) {
  	newinst = oldinst.substring(0, 3) + cc + oldinst.substring(3);
      }
  
      return new ImList(newinst, (ImList) cdr(inst));
  }
  
  ImList toComment(String prefix, ImList inst) {
      StringBuffer tmp = new StringBuffer(prefix);
      do {
  	tmp.append(' ').append(car(inst));
  	inst = (ImList) cdr(inst);
      } while (inst != ImList.Empty);
      return new ImList(tmp.toString(), ImList.Empty);
  }
  
  boolean isOp(String op, ImList inst) {
      String tmp = (String) car(inst);
      return op.equals(tmp);
  }
  
  boolean rewriteCondExec(BiLink p) {
      ImList cexec = ImList.Empty;
      ImList tmp = (ImList) p.elem();
      JumpcEntry t = (JumpcEntry) jumpcInfo.get(car(cdr(tmp)).toString());
  
      // todo?: address of floating constant
      if (t == null) {
  	return false;
      }
      
      // comment out conditional jump.
      BiLink bcc = p.prev();
      bcc.addBefore(".ltorg2");	// for asmpp
      bcc.setElem(toComment("@", (ImList) bcc.elem()));
      
      // comment out this label
      p.setElem(toComment("@", tmp));
      
      // add condition (then-part)
      BiLink q;
      for (q = p.next(); !q.atEnd(); q = q.next()) {
  	ImList old = (ImList) q.elem();
  	if (isOp("deflabel", old)) {
  	    break;
  	} else {
  	    if (false && ((String) car(old)).startsWith("b")) {
  		// delete needless last branch of then-part
  		p.addBefore(toComment("@", old));
  	    } else {
  		p.addBefore(addCc(t.cc, old));
  	    }
  	    q.unlink();
  	    // q.setElem(addCc(t.cc, old));
  	}
      }
      
      // optional process for if-then-else
      if (t.ifThenElse && t.falseTarget != null) { 
  	// conditinal execute else-part
  	// comment out this label
  	q.setElem(toComment("@", (ImList) q.elem()));
  
  	// add condition (else-part)
  	for (q = q.next(); !q.atEnd(); q = q.next()) {
  	    ImList old = (ImList) q.elem();
  	    if (isOp("deflabel", old)) {
  		break;
  	    } else {
  		p.addBefore(addCc(flipCc.get(t.cc), old));
  		q.unlink();
  	    }
  	}
      }
      return true;
  }
  
  Object nth(ImList l, int n)
  {
      for (int i = 0 ; i < n; i++) {
  	l = (ImList) cdr(l);
      }
      return (Object) car(l);
  }
  
  
  /* ** constant decompsition */
  private Map peepHoleOpts = new HashMap();
  
  abstract class PeepHoleOp {
      abstract boolean doIt(ImList inst, BiLink l);
      void addBefore(ImList inst, BiLink l) {
  	l.addBefore(inst);
  	// System.out.println("  +-> " + inst);
      }
  }
  
  PeepHoleOp decompMov = new PeepHoleOp() {
  	boolean doIt(ImList inst, BiLink l) {
  	    //System.out.println("inst " + inst);
  
  	    long imm = ((Long) nth(inst, 2)).longValue();
  	    Object dst = car(cdr(inst));
  	    ArmConst val = new ArmConst(imm);
  	    ArmConst val2 = new ArmConst(~imm);
  	    val.normalize();
  	    val2.normalize();
  
  	    if (val2.nofBytes == 1) {
  		addBefore(ImList.list("mvn", dst, "#" + val2.get(0)), l);
  	    } else {
  		addBefore(ImList.list("mov", dst, "#" + val.get(0)), l);
  		for (int i = 1; i < val.nofBytes; i++) {
  		    addBefore(ImList.list("orr", dst, dst, "#" + val.get(i)), l);
  		}
  	    }
  	    return true;
  	}
      };
  
  PeepHoleOp decompAdd = new PeepHoleOp() {
  	boolean doIt(ImList inst, BiLink l) {
  	    //System.out.println("inst " + inst);
  
  	    Object mnemo = nth(inst, 1);
  	    Object mnemo2 = nth(inst, 2);
  	    Object dst = nth(inst, 3);
  	    Object src = nth(inst, 4);
  	    long imm = ((Long) nth(inst, 5)).longValue();
  	    ArmConst val = new ArmConst(imm);
  	    ArmConst val2 = new ArmConst(-imm);
  	    val.normalize();
  	    val2.normalize();
      
  	    if (val2.nofBytes < val.nofBytes) {
  		val = val2;
  		mnemo = mnemo2;
  	    }
  	    
  	    for (int i = 0; i < val.nofBytes; i++) {
  		addBefore(ImList.list(mnemo, dst, src, "#" + val.get(i)), l);
  		src = dst;
  	    }
  	    return true;
  	}
  
      };
  
  void logop(ImList inst, BiLink l, boolean cpl) {
      //System.out.println("inst " + inst);
      Object mnemo = nth(inst, 1);
      Object dst = nth(inst, 2);
      Object src = nth(inst, 3);
      long imm = ((Long) nth(inst, 4)).longValue();
      ArmConst val = new ArmConst(cpl ? ~imm : imm);
      val.normalize();
      
      for (int i = 0; i < val.nofBytes; i++) {
  	ImList tmp = ImList.list(mnemo, dst, src, "#" + val.get(i));
  	l.addBefore(tmp);
  	// System.out.println("  +-> " + tmp);	
  	src = dst;
      }
  }
  
  
  PeepHoleOp decompOr = new PeepHoleOp() {
  	boolean doIt(ImList inst, BiLink l) {
  	    logop(inst, l, false);
  	    return true;
  	}
      };
  
  PeepHoleOp decompBic = new PeepHoleOp() {
  	boolean doIt(ImList inst, BiLink l) {
  	    logop(inst, l, true);
  	    return true;
  	}
      };
  
  
  void peepHoleOpt(BiList list) {
  
      // phase 1: trivial optimize and constant decomposition.
      for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
  	ImList tmp = (ImList) p.elem();
  	//	System.out.println("p1: " + tmp);
  
  	String op = "";
  	if (car(tmp) instanceof String) {
  	    op = (String) car(tmp);
  	} else {
  	    System.out.println("*** " + tmp);
  	    continue;
  	}
  
  	PeepHoleOp opt;
  
  	if ((opt = (PeepHoleOp) peepHoleOpts.get(op)) != null) {
  	    if (opt.doIt(tmp, p)) {
  		p.unlink();
  		continue;
  	    }
  	}
  
  	// eliminate sub r, r, 0
          if (op.equals("sub") && isDelSub(tmp)) {
  	    p.unlink();
  	    continue;
          }
      }
  
      // pass 2: check conditional execution
  if (true) {
      for (BiLink p = list.first(); !p.atEnd(); p = p.next()) {
  	ImList tmp = (ImList) p.elem();
          String op = (String) car(tmp);
  
  	//	System.out.println("p2: " + tmp);
          if (op.equals("deflabel")) {
  	    if (rewriteCondExec(p)) {
  		// skip cexec node
  		p = p.next();
  	    }
  	}
      }
  }
  }
  
  
  // check virtual frame pointer
  boolean isVirtual(LirNode node)
  {
      Symbol vfp = func.module.globalSymtab.get("%vfp");
      if (node instanceof LirSymRef) {
  	Symbol sym = ((LirSymRef)node).symbol;
  	return sym == vfp;
      }
      return false;
  }
  
  // rewrite implicit runtime library call to explicit call-exp
  LirNode rewriteRtlCall(LirNode node, BiList pre, String rtl, int n) {
      debug("rewriteRtlCall: $0 = " + node);
  
      // make temporal
      LirNode tmp = func.newTemp(node.type);
      // resultant exp.
      LirNode list;
  
      // rewrite rtl-call to explicit call-exp.
      LirNode rtl_func = lir.symRef(func.module.globalSymtab.get(rtl));
  
      debug("op = " + node.opCode);
      // setup parameter
      LirNode[] args = new LirNode[n];
      for (int i = 0; i < n; i++) {
  	args[i] = node.kid(i);
      }
      
      list = lir.operator(Op.CALL, Type.UNKNOWN, rtl_func,
  			lir.node(Op.LIST, Type.UNKNOWN, args),
  			lir.node(Op.LIST, Type.UNKNOWN, tmp),
  			ImList.list());
      pre.add(list);
      return tmp;
  }
}
