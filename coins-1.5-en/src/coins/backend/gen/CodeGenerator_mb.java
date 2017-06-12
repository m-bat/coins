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
 14: void -> (SET I32 xregw regw)
 15: xregw -> regw
 16: void -> (SET I16 xregh regh)
 17: xregh -> regh
 18: void -> (SET I8 xregb regb)
 19: xregb -> regb
 20: void -> (SET F32 xregf regf)
 21: xregf -> regf
 22: void -> (JUMP _ label)
 23: cnstf_ -> (FLOATCONST _)
 24: cnstf -> cnstf_
 25: regf -> cnstf
 26: cnsth -> (INTCONST _)
 27: regh -> cnsth
 28: cnstb -> (INTCONST _)
 29: regb -> cnstb
 30: cnst0 -> (INTCONST _)
 31: cnst -> (INTCONST _)
 32: sta -> (STATIC I32)
 33: asmcnst -> cnst
 34: asmcnst -> sta
 35: asmcnst -> (ADD I32 asmcnst cnst)
 36: asmcnst -> (ADD I32 cnst asmcnst)
 37: asmcnst -> (SUB I32 asmcnst cnst)
 38: regw -> asmcnst
 39: addri -> asmcnst
 40: addrr -> regw
 41: addri -> (ADD I32 regw asmcnst)
 42: addri -> (ADD I32 regw asmcnst)
 43: addri -> (ADD I32 asmcnst regw)
 44: addri -> (SUB I32 regw cnst)
 45: addrr -> (ADD I32 regw regw)
 46: memwi -> (MEM I32 addri)
 47: memhi -> (MEM I16 addri)
 48: membi -> (MEM I8 addri)
 49: memfi -> (MEM F32 addri)
 50: memw -> (MEM I32 addrr)
 51: memh -> (MEM I16 addrr)
 52: memb -> (MEM I8 addrr)
 53: memf -> (MEM F32 addrr)
 54: void -> (SET I32 xregw memwi)
 55: regw -> memwi
 56: void -> (SET I16 xregh memhi)
 57: regh -> memhi
 58: void -> (SET I8 xregb membi)
 59: regb -> membi
 60: void -> (SET F32 xregf memfi)
 61: regf -> memfi
 62: void -> (SET I32 xregw memw)
 63: regw -> memw
 64: void -> (SET I16 xregh memh)
 65: regh -> memh
 66: void -> (SET I8 xregb memb)
 67: regb -> memb
 68: void -> (SET F32 xregf memf)
 69: regf -> memf
 70: void -> (SET I32 memwi regw)
 71: void -> (SET I16 memhi regh)
 72: void -> (SET I8 membi regb)
 73: void -> (SET F32 memfi regf)
 74: void -> (SET I32 memw regw)
 75: void -> (SET I16 memh regh)
 76: void -> (SET I8 memb regb)
 77: void -> (SET F32 memf regf)
 78: regw -> (CONVSX I32 regh)
 79: regw -> (CONVZX I32 regh)
 80: regw -> (CONVSX I32 regb)
 81: regw -> (CONVZX I32 regb)
 82: regh -> (CONVSX I16 regb)
 83: regh -> (CONVZX I16 regb)
 84: regh -> (CONVIT I16 regw)
 85: regb -> (CONVIT I8 regw)
 86: regb -> (CONVIT I8 regh)
 87: _1 -> (CONVIT I16 memwi)
 88: void -> (SET I16 xregh _1)
 89: _2 -> (CONVIT I8 memwi)
 90: void -> (SET I8 xregb _2)
 91: _3 -> (CONVIT I8 memhi)
 92: void -> (SET I8 xregb _3)
 93: _4 -> (CONVIT I16 memw)
 94: void -> (SET I16 xregh _4)
 95: _5 -> (CONVIT I8 memw)
 96: void -> (SET I8 xregb _5)
 97: _6 -> (CONVIT I8 memh)
 98: void -> (SET I8 xregb _6)
 99: _7 -> (CONVIT I16 regw)
 100: void -> (SET I16 memhi _7)
 101: _8 -> (CONVIT I8 regw)
 102: void -> (SET I8 membi _8)
 103: _9 -> (CONVIT I8 regh)
 104: void -> (SET I8 membi _9)
 105: void -> (SET I16 memh _7)
 106: void -> (SET I8 memb _8)
 107: void -> (SET I8 memb _9)
 108: regw -> (NEG I32 regw)
 109: regw -> (BNOT I32 regw)
 110: regw -> (LSHS I32 regw asmcnst)
 111: regw -> (MUL I32 regw asmcnst)
 112: regw -> (ADD I32 regw regw)
 113: regw -> (ADD I32 regw asmcnst)
 114: regw -> (ADD I32 asmcnst regw)
 115: regw -> (MUL I32 regw regw)
 116: regw -> (MUL I32 regw asmcnst)
 117: regw -> (MUL I32 asmcnst regw)
 118: regw -> (BAND I32 regw regw)
 119: regw -> (BAND I32 regw asmcnst)
 120: regw -> (BAND I32 asmcnst regw)
 121: regw -> (BOR I32 regw regw)
 122: regw -> (BOR I32 regw asmcnst)
 123: regw -> (BOR I32 asmcnst regw)
 124: regw -> (BXOR I32 regw regw)
 125: regw -> (BXOR I32 regw asmcnst)
 126: regw -> (BXOR I32 asmcnst regw)
 127: regw -> (MUL I32 regw asmcnst)
 128: regw -> (MUL I32 regw asmcnst)
 129: regw -> (MUL I32 regw asmcnst)
 130: regw -> (MUL I32 regw asmcnst)
 131: regw -> (MUL I32 regw asmcnst)
 132: regw -> (MUL I32 regw asmcnst)
 133: regw -> (MUL I32 regw asmcnst)
 134: regw -> (MUL I32 regw asmcnst)
 135: regw -> (MUL I32 regw asmcnst)
 136: regw -> (MUL I32 regw asmcnst)
 137: regw -> (DIVU I32 regw regw)
 138: regw -> (DIVS I32 regw regw)
 139: regw -> (SUB I32 regw regw)
 140: regw -> (SUB I32 asmcnst regw)
 141: regw -> (MODU I32 regw regw)
 142: regw -> (MODS I32 regw regw)
 143: regf -> (NEG F32 regf)
 144: regf -> (ADD F32 regf regf)
 145: regf -> (SUB F32 regf regf)
 146: regf -> (MUL F32 regf regf)
 147: regf -> (DIVS F32 regf regf)
 148: regw -> (LSHS I32 regw regw)
 149: regw -> (LSHS I32 regw asmcnst)
 150: regw -> (LSHS I32 regw asmcnst)
 151: regw -> (LSHS I32 regw asmcnst)
 152: regw -> (LSHS I32 regw asmcnst)
 153: regw -> (LSHS I32 regw asmcnst)
 154: regw -> (LSHS I32 regw asmcnst)
 155: regw -> (LSHS I32 regw asmcnst)
 156: regw -> (LSHS I32 regw asmcnst)
 157: regw -> (LSHS I32 regw asmcnst)
 158: regw -> (LSHS I32 regw asmcnst)
 159: regw -> (LSHS I32 regw asmcnst)
 160: regw -> (LSHS I32 regw asmcnst)
 161: regw -> (LSHS I32 regw asmcnst)
 162: regw -> (LSHS I32 regw asmcnst)
 163: regw -> (LSHS I32 regw asmcnst)
 164: regw -> (LSHS I32 regw asmcnst)
 165: regw -> (LSHS I32 regw asmcnst)
 166: regw -> (LSHS I32 regw asmcnst)
 167: regw -> (LSHS I32 regw asmcnst)
 168: regw -> (LSHS I32 regw asmcnst)
 169: regw -> (LSHS I32 regw asmcnst)
 170: regw -> (LSHS I32 regw asmcnst)
 171: regw -> (LSHS I32 regw asmcnst)
 172: regw -> (LSHS I32 regw asmcnst)
 173: regw -> (LSHS I32 regw asmcnst)
 174: regw -> (LSHS I32 regw asmcnst)
 175: regw -> (LSHS I32 regw asmcnst)
 176: regw -> (LSHS I32 regw asmcnst)
 177: regw -> (LSHS I32 regw asmcnst)
 178: regw -> (LSHS I32 regw asmcnst)
 179: regw -> (LSHS I32 regw asmcnst)
 180: regw -> (RSHS I32 regw regw)
 181: regw -> (RSHS I32 regw asmcnst)
 182: regw -> (RSHS I32 regw asmcnst)
 183: regw -> (RSHS I32 regw asmcnst)
 184: regw -> (RSHS I32 regw asmcnst)
 185: regw -> (RSHS I32 regw asmcnst)
 186: regw -> (RSHS I32 regw asmcnst)
 187: regw -> (RSHS I32 regw asmcnst)
 188: regw -> (RSHS I32 regw asmcnst)
 189: regw -> (RSHS I32 regw asmcnst)
 190: regw -> (RSHS I32 regw asmcnst)
 191: regw -> (RSHS I32 regw asmcnst)
 192: regw -> (RSHS I32 regw asmcnst)
 193: regw -> (RSHS I32 regw asmcnst)
 194: regw -> (RSHS I32 regw asmcnst)
 195: regw -> (RSHS I32 regw asmcnst)
 196: regw -> (RSHS I32 regw asmcnst)
 197: regw -> (RSHS I32 regw asmcnst)
 198: regw -> (RSHS I32 regw asmcnst)
 199: regw -> (RSHS I32 regw asmcnst)
 200: regw -> (RSHS I32 regw asmcnst)
 201: regw -> (RSHS I32 regw asmcnst)
 202: regw -> (RSHS I32 regw asmcnst)
 203: regw -> (RSHS I32 regw asmcnst)
 204: regw -> (RSHS I32 regw asmcnst)
 205: regw -> (RSHS I32 regw asmcnst)
 206: regw -> (RSHS I32 regw asmcnst)
 207: regw -> (RSHS I32 regw asmcnst)
 208: regw -> (RSHS I32 regw asmcnst)
 209: regw -> (RSHS I32 regw asmcnst)
 210: regw -> (RSHS I32 regw asmcnst)
 211: regw -> (RSHS I32 regw asmcnst)
 212: regw -> (RSHU I32 regw regw)
 213: regw -> (RSHU I32 regw asmcnst)
 214: regw -> (RSHU I32 regw asmcnst)
 215: regw -> (RSHU I32 regw asmcnst)
 216: regw -> (RSHU I32 regw asmcnst)
 217: regw -> (RSHU I32 regw asmcnst)
 218: regw -> (RSHU I32 regw asmcnst)
 219: regw -> (RSHU I32 regw asmcnst)
 220: regw -> (RSHU I32 regw asmcnst)
 221: regw -> (RSHU I32 regw asmcnst)
 222: regw -> (RSHU I32 regw asmcnst)
 223: regw -> (RSHU I32 regw asmcnst)
 224: regw -> (RSHU I32 regw asmcnst)
 225: regw -> (RSHU I32 regw asmcnst)
 226: regw -> (RSHU I32 regw asmcnst)
 227: regw -> (RSHU I32 regw asmcnst)
 228: regw -> (RSHU I32 regw asmcnst)
 229: regw -> (RSHU I32 regw asmcnst)
 230: regw -> (RSHU I32 regw asmcnst)
 231: regw -> (RSHU I32 regw asmcnst)
 232: regw -> (RSHU I32 regw asmcnst)
 233: regw -> (RSHU I32 regw asmcnst)
 234: regw -> (RSHU I32 regw asmcnst)
 235: regw -> (RSHU I32 regw asmcnst)
 236: regw -> (RSHU I32 regw asmcnst)
 237: regw -> (RSHU I32 regw asmcnst)
 238: regw -> (RSHU I32 regw asmcnst)
 239: regw -> (RSHU I32 regw asmcnst)
 240: regw -> (RSHU I32 regw asmcnst)
 241: regw -> (RSHU I32 regw asmcnst)
 242: regw -> (RSHU I32 regw asmcnst)
 243: regw -> (RSHU I32 regw asmcnst)
 244: regw -> (LSHS I32 regw regw)
 245: regw -> (LSHS I32 regw asmcnst)
 246: regw -> (RSHS I32 regw regw)
 247: regw -> (RSHS I32 regw asmcnst)
 248: regw -> (RSHU I32 regw regw)
 249: regw -> (RSHU I32 regw asmcnst)
 250: _10 -> (TSTEQ I32 regw regw)
 251: void -> (JUMPC _ _10 label label)
 252: _11 -> (TSTNE I32 regw regw)
 253: void -> (JUMPC _ _11 label label)
 254: void -> (JUMPC _ _10 label label)
 255: void -> (JUMPC _ _11 label label)
 256: _12 -> (TSTLES I32 regw regw)
 257: void -> (JUMPC _ _12 label label)
 258: _13 -> (TSTLTS I32 regw regw)
 259: void -> (JUMPC _ _13 label label)
 260: _14 -> (TSTGES I32 regw regw)
 261: void -> (JUMPC _ _14 label label)
 262: _15 -> (TSTGTS I32 regw regw)
 263: void -> (JUMPC _ _15 label label)
 264: _16 -> (TSTLEU I32 regw regw)
 265: void -> (JUMPC _ _16 label label)
 266: _17 -> (TSTLTU I32 regw regw)
 267: void -> (JUMPC _ _17 label label)
 268: _18 -> (TSTGEU I32 regw regw)
 269: void -> (JUMPC _ _18 label label)
 270: _19 -> (TSTGTU I32 regw regw)
 271: void -> (JUMPC _ _19 label label)
 272: void -> (JUMPC _ _12 label label)
 273: void -> (JUMPC _ _13 label label)
 274: void -> (JUMPC _ _14 label label)
 275: void -> (JUMPC _ _15 label label)
 276: _20 -> (TSTEQ I32 regf regf)
 277: void -> (JUMPC _ _20 label label)
 278: _21 -> (TSTNE I32 regf regf)
 279: void -> (JUMPC _ _21 label label)
 280: _22 -> (TSTLES I32 regf regf)
 281: void -> (JUMPC _ _22 label label)
 282: _23 -> (TSTLTS I32 regf regf)
 283: void -> (JUMPC _ _23 label label)
 284: _24 -> (TSTGES I32 regf regf)
 285: void -> (JUMPC _ _24 label label)
 286: _25 -> (TSTGTS I32 regf regf)
 287: void -> (JUMPC _ _25 label label)
 288: void -> (CALL _ regw)
 289: void -> (CALL _ asmcnst)
*/
/*
Sorted Productions:
 15: xregw -> regw
 40: addrr -> regw
 17: xregh -> regh
 19: xregb -> regb
 21: xregf -> regf
 4: regw -> xregw
 7: regh -> xregh
 10: regb -> xregb
 13: regf -> xregf
 24: cnstf -> cnstf_
 25: regf -> cnstf
 27: regh -> cnsth
 29: regb -> cnstb
 33: asmcnst -> cnst
 34: asmcnst -> sta
 38: regw -> asmcnst
 39: addri -> asmcnst
 55: regw -> memwi
 57: regh -> memhi
 59: regb -> membi
 61: regf -> memfi
 63: regw -> memw
 65: regh -> memh
 67: regb -> memb
 69: regf -> memf
 26: cnsth -> (INTCONST _)
 28: cnstb -> (INTCONST _)
 30: cnst0 -> (INTCONST _)
 31: cnst -> (INTCONST _)
 23: cnstf_ -> (FLOATCONST _)
 32: sta -> (STATIC I32)
 8: xregb -> (REG I8)
 5: xregh -> (REG I16)
 2: xregw -> (REG I32)
 11: xregf -> (REG F32)
 9: xregb -> (SUBREG I8)
 6: xregh -> (SUBREG I16)
 3: xregw -> (SUBREG I32)
 12: xregf -> (SUBREG F32)
 1: label -> (LABEL _)
 108: regw -> (NEG I32 regw)
 143: regf -> (NEG F32 regf)
 35: asmcnst -> (ADD I32 asmcnst cnst)
 36: asmcnst -> (ADD I32 cnst asmcnst)
 41: addri -> (ADD I32 regw asmcnst)
 42: addri -> (ADD I32 regw asmcnst)
 43: addri -> (ADD I32 asmcnst regw)
 45: addrr -> (ADD I32 regw regw)
 112: regw -> (ADD I32 regw regw)
 113: regw -> (ADD I32 regw asmcnst)
 114: regw -> (ADD I32 asmcnst regw)
 144: regf -> (ADD F32 regf regf)
 37: asmcnst -> (SUB I32 asmcnst cnst)
 44: addri -> (SUB I32 regw cnst)
 139: regw -> (SUB I32 regw regw)
 140: regw -> (SUB I32 asmcnst regw)
 145: regf -> (SUB F32 regf regf)
 111: regw -> (MUL I32 regw asmcnst)
 115: regw -> (MUL I32 regw regw)
 116: regw -> (MUL I32 regw asmcnst)
 117: regw -> (MUL I32 asmcnst regw)
 127: regw -> (MUL I32 regw asmcnst)
 128: regw -> (MUL I32 regw asmcnst)
 129: regw -> (MUL I32 regw asmcnst)
 130: regw -> (MUL I32 regw asmcnst)
 131: regw -> (MUL I32 regw asmcnst)
 132: regw -> (MUL I32 regw asmcnst)
 133: regw -> (MUL I32 regw asmcnst)
 134: regw -> (MUL I32 regw asmcnst)
 135: regw -> (MUL I32 regw asmcnst)
 136: regw -> (MUL I32 regw asmcnst)
 146: regf -> (MUL F32 regf regf)
 138: regw -> (DIVS I32 regw regw)
 147: regf -> (DIVS F32 regf regf)
 137: regw -> (DIVU I32 regw regw)
 142: regw -> (MODS I32 regw regw)
 141: regw -> (MODU I32 regw regw)
 82: regh -> (CONVSX I16 regb)
 78: regw -> (CONVSX I32 regh)
 80: regw -> (CONVSX I32 regb)
 83: regh -> (CONVZX I16 regb)
 79: regw -> (CONVZX I32 regh)
 81: regw -> (CONVZX I32 regb)
 85: regb -> (CONVIT I8 regw)
 86: regb -> (CONVIT I8 regh)
 89: _2 -> (CONVIT I8 memwi)
 91: _3 -> (CONVIT I8 memhi)
 95: _5 -> (CONVIT I8 memw)
 97: _6 -> (CONVIT I8 memh)
 101: _8 -> (CONVIT I8 regw)
 103: _9 -> (CONVIT I8 regh)
 84: regh -> (CONVIT I16 regw)
 87: _1 -> (CONVIT I16 memwi)
 93: _4 -> (CONVIT I16 memw)
 99: _7 -> (CONVIT I16 regw)
 118: regw -> (BAND I32 regw regw)
 119: regw -> (BAND I32 regw asmcnst)
 120: regw -> (BAND I32 asmcnst regw)
 121: regw -> (BOR I32 regw regw)
 122: regw -> (BOR I32 regw asmcnst)
 123: regw -> (BOR I32 asmcnst regw)
 124: regw -> (BXOR I32 regw regw)
 125: regw -> (BXOR I32 regw asmcnst)
 126: regw -> (BXOR I32 asmcnst regw)
 109: regw -> (BNOT I32 regw)
 110: regw -> (LSHS I32 regw asmcnst)
 148: regw -> (LSHS I32 regw regw)
 149: regw -> (LSHS I32 regw asmcnst)
 150: regw -> (LSHS I32 regw asmcnst)
 151: regw -> (LSHS I32 regw asmcnst)
 152: regw -> (LSHS I32 regw asmcnst)
 153: regw -> (LSHS I32 regw asmcnst)
 154: regw -> (LSHS I32 regw asmcnst)
 155: regw -> (LSHS I32 regw asmcnst)
 156: regw -> (LSHS I32 regw asmcnst)
 157: regw -> (LSHS I32 regw asmcnst)
 158: regw -> (LSHS I32 regw asmcnst)
 159: regw -> (LSHS I32 regw asmcnst)
 160: regw -> (LSHS I32 regw asmcnst)
 161: regw -> (LSHS I32 regw asmcnst)
 162: regw -> (LSHS I32 regw asmcnst)
 163: regw -> (LSHS I32 regw asmcnst)
 164: regw -> (LSHS I32 regw asmcnst)
 165: regw -> (LSHS I32 regw asmcnst)
 166: regw -> (LSHS I32 regw asmcnst)
 167: regw -> (LSHS I32 regw asmcnst)
 168: regw -> (LSHS I32 regw asmcnst)
 169: regw -> (LSHS I32 regw asmcnst)
 170: regw -> (LSHS I32 regw asmcnst)
 171: regw -> (LSHS I32 regw asmcnst)
 172: regw -> (LSHS I32 regw asmcnst)
 173: regw -> (LSHS I32 regw asmcnst)
 174: regw -> (LSHS I32 regw asmcnst)
 175: regw -> (LSHS I32 regw asmcnst)
 176: regw -> (LSHS I32 regw asmcnst)
 177: regw -> (LSHS I32 regw asmcnst)
 178: regw -> (LSHS I32 regw asmcnst)
 179: regw -> (LSHS I32 regw asmcnst)
 244: regw -> (LSHS I32 regw regw)
 245: regw -> (LSHS I32 regw asmcnst)
 180: regw -> (RSHS I32 regw regw)
 181: regw -> (RSHS I32 regw asmcnst)
 182: regw -> (RSHS I32 regw asmcnst)
 183: regw -> (RSHS I32 regw asmcnst)
 184: regw -> (RSHS I32 regw asmcnst)
 185: regw -> (RSHS I32 regw asmcnst)
 186: regw -> (RSHS I32 regw asmcnst)
 187: regw -> (RSHS I32 regw asmcnst)
 188: regw -> (RSHS I32 regw asmcnst)
 189: regw -> (RSHS I32 regw asmcnst)
 190: regw -> (RSHS I32 regw asmcnst)
 191: regw -> (RSHS I32 regw asmcnst)
 192: regw -> (RSHS I32 regw asmcnst)
 193: regw -> (RSHS I32 regw asmcnst)
 194: regw -> (RSHS I32 regw asmcnst)
 195: regw -> (RSHS I32 regw asmcnst)
 196: regw -> (RSHS I32 regw asmcnst)
 197: regw -> (RSHS I32 regw asmcnst)
 198: regw -> (RSHS I32 regw asmcnst)
 199: regw -> (RSHS I32 regw asmcnst)
 200: regw -> (RSHS I32 regw asmcnst)
 201: regw -> (RSHS I32 regw asmcnst)
 202: regw -> (RSHS I32 regw asmcnst)
 203: regw -> (RSHS I32 regw asmcnst)
 204: regw -> (RSHS I32 regw asmcnst)
 205: regw -> (RSHS I32 regw asmcnst)
 206: regw -> (RSHS I32 regw asmcnst)
 207: regw -> (RSHS I32 regw asmcnst)
 208: regw -> (RSHS I32 regw asmcnst)
 209: regw -> (RSHS I32 regw asmcnst)
 210: regw -> (RSHS I32 regw asmcnst)
 211: regw -> (RSHS I32 regw asmcnst)
 246: regw -> (RSHS I32 regw regw)
 247: regw -> (RSHS I32 regw asmcnst)
 212: regw -> (RSHU I32 regw regw)
 213: regw -> (RSHU I32 regw asmcnst)
 214: regw -> (RSHU I32 regw asmcnst)
 215: regw -> (RSHU I32 regw asmcnst)
 216: regw -> (RSHU I32 regw asmcnst)
 217: regw -> (RSHU I32 regw asmcnst)
 218: regw -> (RSHU I32 regw asmcnst)
 219: regw -> (RSHU I32 regw asmcnst)
 220: regw -> (RSHU I32 regw asmcnst)
 221: regw -> (RSHU I32 regw asmcnst)
 222: regw -> (RSHU I32 regw asmcnst)
 223: regw -> (RSHU I32 regw asmcnst)
 224: regw -> (RSHU I32 regw asmcnst)
 225: regw -> (RSHU I32 regw asmcnst)
 226: regw -> (RSHU I32 regw asmcnst)
 227: regw -> (RSHU I32 regw asmcnst)
 228: regw -> (RSHU I32 regw asmcnst)
 229: regw -> (RSHU I32 regw asmcnst)
 230: regw -> (RSHU I32 regw asmcnst)
 231: regw -> (RSHU I32 regw asmcnst)
 232: regw -> (RSHU I32 regw asmcnst)
 233: regw -> (RSHU I32 regw asmcnst)
 234: regw -> (RSHU I32 regw asmcnst)
 235: regw -> (RSHU I32 regw asmcnst)
 236: regw -> (RSHU I32 regw asmcnst)
 237: regw -> (RSHU I32 regw asmcnst)
 238: regw -> (RSHU I32 regw asmcnst)
 239: regw -> (RSHU I32 regw asmcnst)
 240: regw -> (RSHU I32 regw asmcnst)
 241: regw -> (RSHU I32 regw asmcnst)
 242: regw -> (RSHU I32 regw asmcnst)
 243: regw -> (RSHU I32 regw asmcnst)
 248: regw -> (RSHU I32 regw regw)
 249: regw -> (RSHU I32 regw asmcnst)
 250: _10 -> (TSTEQ I32 regw regw)
 276: _20 -> (TSTEQ I32 regf regf)
 252: _11 -> (TSTNE I32 regw regw)
 278: _21 -> (TSTNE I32 regf regf)
 258: _13 -> (TSTLTS I32 regw regw)
 282: _23 -> (TSTLTS I32 regf regf)
 256: _12 -> (TSTLES I32 regw regw)
 280: _22 -> (TSTLES I32 regf regf)
 262: _15 -> (TSTGTS I32 regw regw)
 286: _25 -> (TSTGTS I32 regf regf)
 260: _14 -> (TSTGES I32 regw regw)
 284: _24 -> (TSTGES I32 regf regf)
 266: _17 -> (TSTLTU I32 regw regw)
 264: _16 -> (TSTLEU I32 regw regw)
 270: _19 -> (TSTGTU I32 regw regw)
 268: _18 -> (TSTGEU I32 regw regw)
 48: membi -> (MEM I8 addri)
 52: memb -> (MEM I8 addrr)
 47: memhi -> (MEM I16 addri)
 51: memh -> (MEM I16 addrr)
 46: memwi -> (MEM I32 addri)
 50: memw -> (MEM I32 addrr)
 49: memfi -> (MEM F32 addri)
 53: memf -> (MEM F32 addrr)
 18: void -> (SET I8 xregb regb)
 58: void -> (SET I8 xregb membi)
 66: void -> (SET I8 xregb memb)
 72: void -> (SET I8 membi regb)
 76: void -> (SET I8 memb regb)
 90: void -> (SET I8 xregb _2)
 92: void -> (SET I8 xregb _3)
 96: void -> (SET I8 xregb _5)
 98: void -> (SET I8 xregb _6)
 102: void -> (SET I8 membi _8)
 104: void -> (SET I8 membi _9)
 106: void -> (SET I8 memb _8)
 107: void -> (SET I8 memb _9)
 16: void -> (SET I16 xregh regh)
 56: void -> (SET I16 xregh memhi)
 64: void -> (SET I16 xregh memh)
 71: void -> (SET I16 memhi regh)
 75: void -> (SET I16 memh regh)
 88: void -> (SET I16 xregh _1)
 94: void -> (SET I16 xregh _4)
 100: void -> (SET I16 memhi _7)
 105: void -> (SET I16 memh _7)
 14: void -> (SET I32 xregw regw)
 54: void -> (SET I32 xregw memwi)
 62: void -> (SET I32 xregw memw)
 70: void -> (SET I32 memwi regw)
 74: void -> (SET I32 memw regw)
 20: void -> (SET F32 xregf regf)
 60: void -> (SET F32 xregf memfi)
 68: void -> (SET F32 xregf memf)
 73: void -> (SET F32 memfi regf)
 77: void -> (SET F32 memf regf)
 22: void -> (JUMP _ label)
 251: void -> (JUMPC _ _10 label label)
 253: void -> (JUMPC _ _11 label label)
 254: void -> (JUMPC _ _10 label label)
 255: void -> (JUMPC _ _11 label label)
 257: void -> (JUMPC _ _12 label label)
 259: void -> (JUMPC _ _13 label label)
 261: void -> (JUMPC _ _14 label label)
 263: void -> (JUMPC _ _15 label label)
 265: void -> (JUMPC _ _16 label label)
 267: void -> (JUMPC _ _17 label label)
 269: void -> (JUMPC _ _18 label label)
 271: void -> (JUMPC _ _19 label label)
 272: void -> (JUMPC _ _12 label label)
 273: void -> (JUMPC _ _13 label label)
 274: void -> (JUMPC _ _14 label label)
 275: void -> (JUMPC _ _15 label label)
 277: void -> (JUMPC _ _20 label label)
 279: void -> (JUMPC _ _21 label label)
 281: void -> (JUMPC _ _22 label label)
 283: void -> (JUMPC _ _23 label label)
 285: void -> (JUMPC _ _24 label label)
 287: void -> (JUMPC _ _25 label label)
 288: void -> (CALL _ regw)
 289: void -> (CALL _ asmcnst)
*/
/*
Productions:
 1: _rewr -> (CONVUF _ _)
 2: _rewr -> (CONVFU _ _)
 3: _rewr -> (JUMPN _)
 4: _rewr -> (SET _)
 5: _rewr -> (ASM _)
 6: _rewr -> (PROLOGUE _)
 7: _rewr -> (EPILOGUE _)
 8: _rewr -> (MUL _ _ _)
 9: _rewr -> (MODS _ _ _)
 10: _rewr -> (MODU _ _ _)
 11: _rewr -> (DIVS _ _ _)
 12: _rewr -> (DIVU _ _ _)
 13: _rewr -> (CONVFS _)
 14: _rewr -> (CONVSF _)
 15: _rewr -> (ADD _)
 16: _rewr -> (SUB _)
 17: _rewr -> (MUL _)
 18: _rewr -> (DIVS _)
 19: _1 -> (TSTNE _)
 20: _rewr -> (JUMPC _ _1)
 21: _2 -> (TSTEQ _)
 22: _rewr -> (JUMPC _ _2)
 23: _3 -> (TSTLES _)
 24: _rewr -> (JUMPC _ _3)
 25: _4 -> (TSTLTS _)
 26: _rewr -> (JUMPC _ _4)
 27: _5 -> (TSTGES _)
 28: _rewr -> (JUMPC _ _5)
 29: _6 -> (TSTGTS _)
 30: _rewr -> (JUMPC _ _6)
 31: _rewr -> (CALL _)
 32: _rewr -> (CALL _)
*/
/*
Sorted Productions:
 15: _rewr -> (ADD _)
 16: _rewr -> (SUB _)
 8: _rewr -> (MUL _ _ _)
 17: _rewr -> (MUL _)
 11: _rewr -> (DIVS _ _ _)
 18: _rewr -> (DIVS _)
 12: _rewr -> (DIVU _ _ _)
 9: _rewr -> (MODS _ _ _)
 10: _rewr -> (MODU _ _ _)
 13: _rewr -> (CONVFS _)
 2: _rewr -> (CONVFU _ _)
 14: _rewr -> (CONVSF _)
 1: _rewr -> (CONVUF _ _)
 21: _2 -> (TSTEQ _)
 19: _1 -> (TSTNE _)
 25: _4 -> (TSTLTS _)
 23: _3 -> (TSTLES _)
 29: _6 -> (TSTGTS _)
 27: _5 -> (TSTGES _)
 4: _rewr -> (SET _)
 20: _rewr -> (JUMPC _ _1)
 22: _rewr -> (JUMPC _ _2)
 24: _rewr -> (JUMPC _ _3)
 26: _rewr -> (JUMPC _ _4)
 28: _rewr -> (JUMPC _ _5)
 30: _rewr -> (JUMPC _ _6)
 3: _rewr -> (JUMPN _)
 31: _rewr -> (CALL _)
 32: _rewr -> (CALL _)
 6: _rewr -> (PROLOGUE _)
 7: _rewr -> (EPILOGUE _)
 5: _rewr -> (ASM _)
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

// Java code starts here.

import coins.backend.ana.SaveRegisters;
import coins.backend.util.NumberSet;
import coins.backend.lir.LirLabelRef;
import coins.driver.CoinsOptions;
import coins.driver.CommandLine;
import coins.driver.CompileSpecification;
import coins.driver.CompileThread;
import coins.IoRoot;
import java.util.Map;





public class CodeGenerator_mb extends CodeGenerator {

  /** State vector for labeling LIR nodes. Suffix is a LirNode's id. **/
  State[] stateVec;

  /** RewrState vector **/
  private RewrState[] rewrStates;

  /** Create code generator engine. **/
  public CodeGenerator_mb() {}


  /** State label for rewriting engine. **/
  class RewrState {
    static final int NNONTERM = 8;
    static final int NRULES = 32 + 1;
    static final int START_NT = 1;

    static final int NT__ = 0;
    static final int NT__rewr = 1;
    static final int NT__1 = 2;
    static final int NT__2 = 3;
    static final int NT__3 = 4;
    static final int NT__4 = 5;
    static final int NT__5 = 6;
    static final int NT__6 = 7;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT__rewr: return "_rewr";
      case NT__1: return "_1";
      case NT__2: return "_2";
      case NT__3: return "_3";
      case NT__4: return "_4";
      case NT__5: return "_5";
      case NT__6: return "_6";
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
      case Op.ADD:
        if (phase == "early") if (softFloat && Type.tag(t.type) == Type.FLOAT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__addsf3", 2));
        }
        break;
      case Op.SUB:
        if (phase == "early") if (softFloat && Type.tag(t.type) == Type.FLOAT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__subsf3", 2));
        }
        break;
      case Op.MUL:
        if (phase == "early") if (rewriteMul(t.kid(1)))  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__mulsi3", 2));
        }
        if (phase == "early") if (softFloat && Type.tag(t.type) == Type.FLOAT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__mulsf3", 2));
        }
        break;
      case Op.DIVS:
        if (phase == "early") if (noUseDiv)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__divsi3", 2));
        }
        if (phase == "early") if (softFloat && Type.tag(t.type) == Type.FLOAT)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__divsf3", 2));
        }
        break;
      case Op.DIVU:
        if (phase == "early") if (noUseDiv)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__udivsi3", 2));
        }
        break;
      case Op.MODS:
        if (phase == "early") if (noUseMul || noUseDiv)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__modsi3", 2));
        }
        break;
      case Op.MODU:
        if (phase == "early") if (noUseMul || noUseDiv)  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__umodsi3", 2));
        }
        break;
      case Op.CONVFS:
        if (phase == "early")  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__fixsfsi", 1));
        }
        break;
      case Op.CONVFU:
        if (phase == "early")  {
          rewritten = true;
          return rewriteCONVFU(t, pre);
        }
        break;
      case Op.CONVSF:
        if (phase == "early")  {
          rewritten = true;
          return noRescan(rewriteRtlCall(t, pre, "__floatsisf", 1));
        }
        break;
      case Op.CONVUF:
        if (phase == "early")  {
          rewritten = true;
          return rewriteCONVUF(t, pre);
        }
        break;
      case Op.TSTEQ:
        record(NT__2, 21);
        break;
      case Op.TSTNE:
        record(NT__1, 19);
        break;
      case Op.TSTLTS:
        record(NT__4, 25);
        break;
      case Op.TSTLES:
        record(NT__3, 23);
        break;
      case Op.TSTGTS:
        record(NT__6, 29);
        break;
      case Op.TSTGES:
        record(NT__5, 27);
        break;
      case Op.SET:
        if (phase == "late") if (Type.tag(t.type) == Type.AGGREGATE)  {
          rewritten = true;
          return rewriteAggregateCopy(t, pre);
        }
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__1] != 0) if (phase == "early") if (softFloat)  {
          rewritten = true;
          return noRescan(rewriteJumpc(t, pre, "__nesf2", 2));
        }
        if (kids[0].rule[NT__2] != 0) if (phase == "early") if (softFloat)  {
          rewritten = true;
          return noRescan(rewriteJumpc(t, pre, "__eqsf2", 2));
        }
        if (kids[0].rule[NT__3] != 0) if (phase == "early") if (softFloat)  {
          rewritten = true;
          return noRescan(rewriteJumpc(t, pre, "__lesf2", 2));
        }
        if (kids[0].rule[NT__4] != 0) if (phase == "early") if (softFloat)  {
          rewritten = true;
          return noRescan(rewriteJumpc(t, pre, "__ltsf2", 2));
        }
        if (kids[0].rule[NT__5] != 0) if (phase == "early") if (softFloat)  {
          rewritten = true;
          return noRescan(rewriteJumpc(t, pre, "__gesf2", 2));
        }
        if (kids[0].rule[NT__6] != 0) if (phase == "early") if (softFloat)  {
          rewritten = true;
          return noRescan(rewriteJumpc(t, pre, "__gtsf2", 2));
        }
        break;
      case Op.JUMPN:
        if (phase == "late")  {
          rewritten = true;
          return rewriteJumpn(t, pre);
        }
        break;
      case Op.CALL:
        if (phase == "early")  {
          rewritten = true;
          return noRescan(rewriteCall(t, pre, post, true));
        }
        if (phase == "late")  {
          rewritten = true;
          return rewriteCall(t, pre, post, false);
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
    static final int NNONTERM = 54;
    static final int NRULES = 289 + 1;
    static final int START_NT = 5;

    static final int NT__ = 0;
    static final int NT_regw = 1;
    static final int NT_regh = 2;
    static final int NT_regb = 3;
    static final int NT_regf = 4;
    static final int NT_void = 5;
    static final int NT_label = 6;
    static final int NT_xregw = 7;
    static final int NT_xregh = 8;
    static final int NT_xregb = 9;
    static final int NT_xregf = 10;
    static final int NT_cnstf_ = 11;
    static final int NT_cnstf = 12;
    static final int NT_cnsth = 13;
    static final int NT_cnstb = 14;
    static final int NT_cnst0 = 15;
    static final int NT_cnst = 16;
    static final int NT_sta = 17;
    static final int NT_asmcnst = 18;
    static final int NT_addri = 19;
    static final int NT_addrr = 20;
    static final int NT_memwi = 21;
    static final int NT_memhi = 22;
    static final int NT_membi = 23;
    static final int NT_memfi = 24;
    static final int NT_memw = 25;
    static final int NT_memh = 26;
    static final int NT_memb = 27;
    static final int NT_memf = 28;
    static final int NT__1 = 29;
    static final int NT__2 = 30;
    static final int NT__3 = 31;
    static final int NT__4 = 32;
    static final int NT__5 = 33;
    static final int NT__6 = 34;
    static final int NT__7 = 35;
    static final int NT__8 = 36;
    static final int NT__9 = 37;
    static final int NT__10 = 38;
    static final int NT__11 = 39;
    static final int NT__12 = 40;
    static final int NT__13 = 41;
    static final int NT__14 = 42;
    static final int NT__15 = 43;
    static final int NT__16 = 44;
    static final int NT__17 = 45;
    static final int NT__18 = 46;
    static final int NT__19 = 47;
    static final int NT__20 = 48;
    static final int NT__21 = 49;
    static final int NT__22 = 50;
    static final int NT__23 = 51;
    static final int NT__24 = 52;
    static final int NT__25 = 53;

    String nontermName(int nt) {
      switch (nt) {
      case NT__: return "_";
      case NT_regw: return "regw";
      case NT_regh: return "regh";
      case NT_regb: return "regb";
      case NT_regf: return "regf";
      case NT_void: return "void";
      case NT_label: return "label";
      case NT_xregw: return "xregw";
      case NT_xregh: return "xregh";
      case NT_xregb: return "xregb";
      case NT_xregf: return "xregf";
      case NT_cnstf_: return "cnstf_";
      case NT_cnstf: return "cnstf";
      case NT_cnsth: return "cnsth";
      case NT_cnstb: return "cnstb";
      case NT_cnst0: return "cnst0";
      case NT_cnst: return "cnst";
      case NT_sta: return "sta";
      case NT_asmcnst: return "asmcnst";
      case NT_addri: return "addri";
      case NT_addrr: return "addrr";
      case NT_memwi: return "memwi";
      case NT_memhi: return "memhi";
      case NT_membi: return "membi";
      case NT_memfi: return "memfi";
      case NT_memw: return "memw";
      case NT_memh: return "memh";
      case NT_memb: return "memb";
      case NT_memf: return "memf";
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
          record(NT_xregw, 1 + cost1, 1 + cost2, 15);
          record(NT_addrr, 0 + cost1, 0 + cost2, 40);
          break;
        case NT_regh:
          record(NT_xregh, 1 + cost1, 1 + cost2, 17);
          break;
        case NT_regb:
          record(NT_xregb, 1 + cost1, 1 + cost2, 19);
          break;
        case NT_regf:
          record(NT_xregf, 1 + cost1, 1 + cost2, 21);
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
        case NT_cnstf_:
          record(NT_cnstf, 0 + cost1, 0 + cost2, 24);
          break;
        case NT_cnstf:
          record(NT_regf, 1 + cost1, 1 + cost2, 25);
          break;
        case NT_cnsth:
          record(NT_regh, 1 + cost1, 1 + cost2, 27);
          break;
        case NT_cnstb:
          record(NT_regb, 1 + cost1, 1 + cost2, 29);
          break;
        case NT_cnst:
          record(NT_asmcnst, 0 + cost1, 0 + cost2, 33);
          break;
        case NT_sta:
          record(NT_asmcnst, 0 + cost1, 0 + cost2, 34);
          break;
        case NT_asmcnst:
          record(NT_regw, 1 + cost1, 1 + cost2, 38);
          record(NT_addri, 0 + cost1, 0 + cost2, 39);
          break;
        case NT_memwi:
          record(NT_regw, 2 + cost1, 2 + cost2, 55);
          break;
        case NT_memhi:
          record(NT_regh, 2 + cost1, 2 + cost2, 57);
          break;
        case NT_membi:
          record(NT_regb, 2 + cost1, 2 + cost2, 59);
          break;
        case NT_memfi:
          record(NT_regf, 2 + cost1, 2 + cost2, 61);
          break;
        case NT_memw:
          record(NT_regw, 2 + cost1, 2 + cost2, 63);
          break;
        case NT_memh:
          record(NT_regh, 2 + cost1, 2 + cost2, 65);
          break;
        case NT_memb:
          record(NT_regb, 2 + cost1, 2 + cost2, 67);
          break;
        case NT_memf:
          record(NT_regf, 2 + cost1, 2 + cost2, 69);
          break;
        }
      }
    }

    void label(LirNode t, State kids[]) {
      switch (t.opCode) {
      case Op.INTCONST:
        if (Type.bits(((LirIconst)t).type) == 16) record(NT_cnsth, 0, 0, 26);
        if (Type.bits(((LirIconst)t).type) == 8) record(NT_cnstb, 0, 0, 28);
        if (((LirIconst)t).signedValue() == 0) record(NT_cnst0, 0, 0, 30);
        record(NT_cnst, 0, 0, 31);
        break;
      case Op.FLOATCONST:
        if (Type.bits(((LirFconst)t).type) == 32) record(NT_cnstf_, 0, 0, 23);
        break;
      case Op.STATIC:
        if (t.type == 514) {
          record(NT_sta, 0, 0, 32);
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
        }
        if (t.type == 516) {
          record(NT_xregf, 0, 0, 11);
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
        break;
      case Op.LABEL:
        record(NT_label, 0, 0, 1);
        break;
      case Op.NEG:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 108);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) record(NT_regf, 1 + kids[0].cost1[NT_regf], 1 + kids[0].cost2[NT_regf], 143);
        }
        break;
      case Op.ADD:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_cnst] != 0) record(NT_asmcnst, 0 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_cnst], 0 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_cnst], 35);
          if (kids[0].rule[NT_cnst] != 0) if (kids[1].rule[NT_asmcnst] != 0) record(NT_asmcnst, 0 + kids[0].cost1[NT_cnst] + kids[1].cost1[NT_asmcnst], 0 + kids[0].cost2[NT_cnst] + kids[1].cost2[NT_asmcnst], 36);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (eqReg("%vsp", t.kid(0))) record(NT_addri, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 41);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) record(NT_addri, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 42);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_addri, 0 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 43);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_addrr, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 45);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 112);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 113);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 114);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 6 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 144);
        }
        break;
      case Op.SUB:
        if (t.type == 514) {
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_cnst] != 0) record(NT_asmcnst, 0 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_cnst], 0 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_cnst], 37);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_cnst] != 0) record(NT_addri, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_cnst], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_cnst], 44);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 139);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 140);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 6 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 145);
        }
        break;
      case Op.MUL:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 3) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 111);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseMul) record(NT_regw, 3 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 115);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseMul) record(NT_regw, 3 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 116);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseMul) record(NT_regw, 3 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 117);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs && ((LirIconst)t.kid(1)).signedValue() == 5) record(NT_regw, 4 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 127);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs && ((LirIconst)t.kid(1)).signedValue() == 9) record(NT_regw, 4 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 128);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs && ((LirIconst)t.kid(1)).signedValue() == 6) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 129);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs && ((LirIconst)t.kid(1)).signedValue() == 10) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 130);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 7) record(NT_regw, 7 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 5 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 131);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 5) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 5 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 132);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 9) record(NT_regw, 6 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 133);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 6) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 5 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 134);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 10) record(NT_regw, 6 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 135);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 7) record(NT_regw, 6 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 136);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 6 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 146);
        }
        break;
      case Op.DIVS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 34 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 138);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_regf, 30 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 147);
        }
        break;
      case Op.DIVU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 34 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 137);
        }
        break;
      case Op.MODS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseMul && !noUseDiv) record(NT_regw, 38 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 142);
        }
        break;
      case Op.MODU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseMul && !noUseDiv) record(NT_regw, 38 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 141);
        }
        break;
      case Op.CONVSX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 82);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regh] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regh], 1 + kids[0].cost2[NT_regh], 78);
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regb], 1 + kids[0].cost2[NT_regb], 80);
        }
        break;
      case Op.CONVZX:
        if (t.type == 258) {
          if (kids[0].rule[NT_regb] != 0) record(NT_regh, 0 + kids[0].cost1[NT_regb], 0 + kids[0].cost2[NT_regb], 83);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_regh] != 0) record(NT_regw, 0 + kids[0].cost1[NT_regh], 0 + kids[0].cost2[NT_regh], 79);
          if (kids[0].rule[NT_regb] != 0) record(NT_regw, 0 + kids[0].cost1[NT_regb], 0 + kids[0].cost2[NT_regb], 81);
        }
        break;
      case Op.CONVIT:
        if (t.type == 130) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 85);
          if (kids[0].rule[NT_regh] != 0) record(NT_regb, 1 + kids[0].cost1[NT_regh], 1 + kids[0].cost2[NT_regh], 86);
          if (kids[0].rule[NT_memwi] != 0) record(NT__2, 0 + kids[0].cost1[NT_memwi], 0 + kids[0].cost2[NT_memwi], 89);
          if (kids[0].rule[NT_memhi] != 0) record(NT__3, 0 + kids[0].cost1[NT_memhi], 0 + kids[0].cost2[NT_memhi], 91);
          if (kids[0].rule[NT_memw] != 0) record(NT__5, 0 + kids[0].cost1[NT_memw], 0 + kids[0].cost2[NT_memw], 95);
          if (kids[0].rule[NT_memh] != 0) record(NT__6, 0 + kids[0].cost1[NT_memh], 0 + kids[0].cost2[NT_memh], 97);
          if (kids[0].rule[NT_regw] != 0) record(NT__8, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 101);
          if (kids[0].rule[NT_regh] != 0) record(NT__9, 0 + kids[0].cost1[NT_regh], 0 + kids[0].cost2[NT_regh], 103);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regh, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 84);
          if (kids[0].rule[NT_memwi] != 0) record(NT__1, 0 + kids[0].cost1[NT_memwi], 0 + kids[0].cost2[NT_memwi], 87);
          if (kids[0].rule[NT_memw] != 0) record(NT__4, 0 + kids[0].cost1[NT_memw], 0 + kids[0].cost2[NT_memw], 93);
          if (kids[0].rule[NT_regw] != 0) record(NT__7, 0 + kids[0].cost1[NT_regw], 0 + kids[0].cost2[NT_regw], 99);
        }
        break;
      case Op.BAND:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 118);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 119);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 120);
        }
        break;
      case Op.BOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 121);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 122);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 123);
        }
        break;
      case Op.BXOR:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 124);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 125);
          if (kids[0].rule[NT_asmcnst] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_asmcnst] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_asmcnst] + kids[1].cost2[NT_regw], 126);
        }
        break;
      case Op.BNOT:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) record(NT_regw, 1 + kids[0].cost1[NT_regw], 1 + kids[0].cost2[NT_regw], 109);
        }
        break;
      case Op.LSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 1) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 110);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (noUseBs) record(NT_regw, 52 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 148);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 1) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 149);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 2) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 150);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 3) record(NT_regw, 3 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 151);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 4) record(NT_regw, 4 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 4 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 152);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 5) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 5 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 153);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 6) record(NT_regw, 6 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 154);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 7) record(NT_regw, 7 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 7 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 155);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 8) record(NT_regw, 8 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 8 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 156);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 9) record(NT_regw, 9 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 9 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 157);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 10) record(NT_regw, 10 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 10 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 158);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 11) record(NT_regw, 11 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 11 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 159);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 12) record(NT_regw, 12 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 12 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 160);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 13) record(NT_regw, 13 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 13 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 161);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 14) record(NT_regw, 14 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 14 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 162);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 15) record(NT_regw, 15 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 15 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 163);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 16) record(NT_regw, 16 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 16 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 164);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 17) record(NT_regw, 17 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 17 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 165);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 18) record(NT_regw, 18 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 18 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 166);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 19) record(NT_regw, 19 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 19 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 167);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 20) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 168);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 21) record(NT_regw, 21 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 21 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 169);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 22) record(NT_regw, 22 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 22 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 170);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 23) record(NT_regw, 23 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 23 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 171);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 24) record(NT_regw, 24 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 24 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 172);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 25) record(NT_regw, 25 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 25 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 173);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 26) record(NT_regw, 26 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 26 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 174);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 27) record(NT_regw, 27 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 27 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 175);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 28) record(NT_regw, 28 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 28 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 176);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 29) record(NT_regw, 29 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 29 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 177);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 30) record(NT_regw, 30 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 30 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 178);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 31) record(NT_regw, 31 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 31 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 179);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseBs) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 244);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 245);
        }
        break;
      case Op.RSHS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (noUseBs) record(NT_regw, 52 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 180);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 1) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 181);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 2) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 182);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 3) record(NT_regw, 3 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 183);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 4) record(NT_regw, 4 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 4 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 184);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 5) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 5 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 185);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 6) record(NT_regw, 6 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 186);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 7) record(NT_regw, 7 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 7 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 187);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 8) record(NT_regw, 8 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 8 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 188);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 9) record(NT_regw, 9 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 9 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 189);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 10) record(NT_regw, 10 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 10 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 190);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 11) record(NT_regw, 11 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 11 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 191);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 12) record(NT_regw, 12 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 12 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 192);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 13) record(NT_regw, 13 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 13 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 193);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 14) record(NT_regw, 14 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 14 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 194);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 15) record(NT_regw, 15 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 15 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 195);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 16) record(NT_regw, 16 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 16 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 196);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 17) record(NT_regw, 17 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 17 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 197);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 18) record(NT_regw, 18 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 18 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 198);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 19) record(NT_regw, 19 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 19 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 199);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 20) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 200);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 21) record(NT_regw, 21 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 21 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 201);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 22) record(NT_regw, 22 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 22 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 202);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 23) record(NT_regw, 23 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 23 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 203);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 24) record(NT_regw, 24 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 24 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 204);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 25) record(NT_regw, 25 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 25 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 205);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 26) record(NT_regw, 26 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 26 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 206);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 27) record(NT_regw, 27 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 27 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 207);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 28) record(NT_regw, 28 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 28 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 208);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 29) record(NT_regw, 29 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 29 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 209);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 30) record(NT_regw, 30 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 30 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 210);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 31) record(NT_regw, 31 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 31 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 211);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseBs) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 246);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 247);
        }
        break;
      case Op.RSHU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (noUseBs) record(NT_regw, 52 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 212);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (((LirIconst)t.kid(1)).signedValue() == 1) record(NT_regw, 1 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 213);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 2) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 2 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 214);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 3) record(NT_regw, 3 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 3 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 215);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 4) record(NT_regw, 4 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 4 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 216);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 5) record(NT_regw, 5 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 5 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 217);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 6) record(NT_regw, 6 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 6 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 218);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 7) record(NT_regw, 7 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 7 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 219);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 8) record(NT_regw, 8 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 8 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 220);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 9) record(NT_regw, 9 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 9 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 221);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 10) record(NT_regw, 10 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 10 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 222);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 11) record(NT_regw, 11 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 11 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 223);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 12) record(NT_regw, 12 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 12 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 224);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 13) record(NT_regw, 13 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 13 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 225);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 14) record(NT_regw, 14 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 14 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 226);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 15) record(NT_regw, 15 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 15 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 227);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 16) record(NT_regw, 16 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 16 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 228);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 17) record(NT_regw, 17 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 17 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 229);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 18) record(NT_regw, 18 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 18 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 230);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 19) record(NT_regw, 19 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 19 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 231);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 20) record(NT_regw, 20 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 20 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 232);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 21) record(NT_regw, 21 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 21 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 233);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 22) record(NT_regw, 22 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 22 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 234);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 23) record(NT_regw, 23 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 23 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 235);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 24) record(NT_regw, 24 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 24 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 236);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 25) record(NT_regw, 25 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 25 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 237);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 26) record(NT_regw, 26 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 26 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 238);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 27) record(NT_regw, 27 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 27 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 239);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 28) record(NT_regw, 28 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 28 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 240);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 29) record(NT_regw, 29 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 29 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 241);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 30) record(NT_regw, 30 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 30 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 242);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (noUseBs && ((LirIconst)t.kid(1)).signedValue() == 31) record(NT_regw, 31 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 31 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 243);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) if (!noUseBs) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 248);
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_asmcnst] != 0) if (!noUseBs) record(NT_regw, 2 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_asmcnst], 1 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_asmcnst], 249);
        }
        break;
      case Op.TSTEQ:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__10, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 250);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__20, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 276);
        }
        break;
      case Op.TSTNE:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__11, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 252);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__21, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 278);
        }
        break;
      case Op.TSTLTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__13, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 258);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__23, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 282);
        }
        break;
      case Op.TSTLES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__12, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 256);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__22, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 280);
        }
        break;
      case Op.TSTGTS:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__15, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 262);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__25, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 286);
        }
        break;
      case Op.TSTGES:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__14, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 260);
          if (kids[0].rule[NT_regf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT__24, 0 + kids[0].cost1[NT_regf] + kids[1].cost1[NT_regf], 0 + kids[0].cost2[NT_regf] + kids[1].cost2[NT_regf], 284);
        }
        break;
      case Op.TSTLTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__17, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 266);
        }
        break;
      case Op.TSTLEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__16, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 264);
        }
        break;
      case Op.TSTGTU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__19, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 270);
        }
        break;
      case Op.TSTGEU:
        if (t.type == 514) {
          if (kids[0].rule[NT_regw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT__18, 0 + kids[0].cost1[NT_regw] + kids[1].cost1[NT_regw], 0 + kids[0].cost2[NT_regw] + kids[1].cost2[NT_regw], 268);
        }
        break;
      case Op.MEM:
        if (t.type == 130) {
          if (kids[0].rule[NT_addri] != 0) record(NT_membi, 0 + kids[0].cost1[NT_addri], 0 + kids[0].cost2[NT_addri], 48);
          if (kids[0].rule[NT_addrr] != 0) record(NT_memb, 0 + kids[0].cost1[NT_addrr], 0 + kids[0].cost2[NT_addrr], 52);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_addri] != 0) record(NT_memhi, 0 + kids[0].cost1[NT_addri], 0 + kids[0].cost2[NT_addri], 47);
          if (kids[0].rule[NT_addrr] != 0) record(NT_memh, 0 + kids[0].cost1[NT_addrr], 0 + kids[0].cost2[NT_addrr], 51);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_addri] != 0) record(NT_memwi, 0 + kids[0].cost1[NT_addri], 0 + kids[0].cost2[NT_addri], 46);
          if (kids[0].rule[NT_addrr] != 0) record(NT_memw, 0 + kids[0].cost1[NT_addrr], 0 + kids[0].cost2[NT_addrr], 50);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_addri] != 0) record(NT_memfi, 0 + kids[0].cost1[NT_addri], 0 + kids[0].cost2[NT_addri], 49);
          if (kids[0].rule[NT_addrr] != 0) record(NT_memf, 0 + kids[0].cost1[NT_addrr], 0 + kids[0].cost2[NT_addrr], 53);
        }
        break;
      case Op.SET:
        if (t.type == 130) {
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_regb], 1 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_regb], 18);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_membi] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_membi], 2 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_membi], 58);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT_memb] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT_memb], 2 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT_memb], 66);
          if (kids[0].rule[NT_membi] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 2 + kids[0].cost1[NT_membi] + kids[1].cost1[NT_regb], 2 + kids[0].cost2[NT_membi] + kids[1].cost2[NT_regb], 72);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT_regb] != 0) record(NT_void, 2 + kids[0].cost1[NT_memb] + kids[1].cost1[NT_regb], 2 + kids[0].cost2[NT_memb] + kids[1].cost2[NT_regb], 76);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__2] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__2], 2 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__2], 90);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__3] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__3], 2 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__3], 92);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__5] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__5], 2 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__5], 96);
          if (kids[0].rule[NT_xregb] != 0) if (kids[1].rule[NT__6] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregb] + kids[1].cost1[NT__6], 2 + kids[0].cost2[NT_xregb] + kids[1].cost2[NT__6], 98);
          if (kids[0].rule[NT_membi] != 0) if (kids[1].rule[NT__8] != 0) record(NT_void, 2 + kids[0].cost1[NT_membi] + kids[1].cost1[NT__8], 2 + kids[0].cost2[NT_membi] + kids[1].cost2[NT__8], 102);
          if (kids[0].rule[NT_membi] != 0) if (kids[1].rule[NT__9] != 0) record(NT_void, 2 + kids[0].cost1[NT_membi] + kids[1].cost1[NT__9], 2 + kids[0].cost2[NT_membi] + kids[1].cost2[NT__9], 104);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT__8] != 0) record(NT_void, 2 + kids[0].cost1[NT_memb] + kids[1].cost1[NT__8], 2 + kids[0].cost2[NT_memb] + kids[1].cost2[NT__8], 106);
          if (kids[0].rule[NT_memb] != 0) if (kids[1].rule[NT__9] != 0) record(NT_void, 2 + kids[0].cost1[NT_memb] + kids[1].cost1[NT__9], 2 + kids[0].cost2[NT_memb] + kids[1].cost2[NT__9], 107);
        }
        if (t.type == 258) {
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_regh], 1 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_regh], 16);
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_memhi] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_memhi], 2 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_memhi], 56);
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT_memh] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT_memh], 2 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT_memh], 64);
          if (kids[0].rule[NT_memhi] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 2 + kids[0].cost1[NT_memhi] + kids[1].cost1[NT_regh], 2 + kids[0].cost2[NT_memhi] + kids[1].cost2[NT_regh], 71);
          if (kids[0].rule[NT_memh] != 0) if (kids[1].rule[NT_regh] != 0) record(NT_void, 2 + kids[0].cost1[NT_memh] + kids[1].cost1[NT_regh], 2 + kids[0].cost2[NT_memh] + kids[1].cost2[NT_regh], 75);
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT__1] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT__1], 2 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT__1], 88);
          if (kids[0].rule[NT_xregh] != 0) if (kids[1].rule[NT__4] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregh] + kids[1].cost1[NT__4], 2 + kids[0].cost2[NT_xregh] + kids[1].cost2[NT__4], 94);
          if (kids[0].rule[NT_memhi] != 0) if (kids[1].rule[NT__7] != 0) record(NT_void, 2 + kids[0].cost1[NT_memhi] + kids[1].cost1[NT__7], 2 + kids[0].cost2[NT_memhi] + kids[1].cost2[NT__7], 100);
          if (kids[0].rule[NT_memh] != 0) if (kids[1].rule[NT__7] != 0) record(NT_void, 2 + kids[0].cost1[NT_memh] + kids[1].cost1[NT__7], 2 + kids[0].cost2[NT_memh] + kids[1].cost2[NT__7], 105);
        }
        if (t.type == 514) {
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_regw], 1 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_regw], 14);
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_memwi] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_memwi], 2 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_memwi], 54);
          if (kids[0].rule[NT_xregw] != 0) if (kids[1].rule[NT_memw] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregw] + kids[1].cost1[NT_memw], 2 + kids[0].cost2[NT_xregw] + kids[1].cost2[NT_memw], 62);
          if (kids[0].rule[NT_memwi] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 2 + kids[0].cost1[NT_memwi] + kids[1].cost1[NT_regw], 2 + kids[0].cost2[NT_memwi] + kids[1].cost2[NT_regw], 70);
          if (kids[0].rule[NT_memw] != 0) if (kids[1].rule[NT_regw] != 0) record(NT_void, 2 + kids[0].cost1[NT_memw] + kids[1].cost1[NT_regw], 2 + kids[0].cost2[NT_memw] + kids[1].cost2[NT_regw], 74);
        }
        if (t.type == 516) {
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 1 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_regf], 1 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_regf], 20);
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_memfi] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_memfi], 2 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_memfi], 60);
          if (kids[0].rule[NT_xregf] != 0) if (kids[1].rule[NT_memf] != 0) record(NT_void, 2 + kids[0].cost1[NT_xregf] + kids[1].cost1[NT_memf], 2 + kids[0].cost2[NT_xregf] + kids[1].cost2[NT_memf], 68);
          if (kids[0].rule[NT_memfi] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 2 + kids[0].cost1[NT_memfi] + kids[1].cost1[NT_regf], 2 + kids[0].cost2[NT_memfi] + kids[1].cost2[NT_regf], 73);
          if (kids[0].rule[NT_memf] != 0) if (kids[1].rule[NT_regf] != 0) record(NT_void, 2 + kids[0].cost1[NT_memf] + kids[1].cost1[NT_regf], 2 + kids[0].cost2[NT_memf] + kids[1].cost2[NT_regf], 77);
        }
        break;
      case Op.JUMP:
        if (kids[0].rule[NT_label] != 0) record(NT_void, 2 + kids[0].cost1[NT_label], 2 + kids[0].cost2[NT_label], 22);
        break;
      case Op.JUMPC:
        if (kids[0].rule[NT__10] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__10] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__10] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 251);
        if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__11] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__11] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 253);
        if (kids[0].rule[NT__10] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (softFloat && eqReg("%cc", t.kid(0).kid(1))) record(NT_void, 2 + kids[0].cost1[NT__10] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__10] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 254);
        if (kids[0].rule[NT__11] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (softFloat && eqReg("%cc", t.kid(0).kid(1))) record(NT_void, 2 + kids[0].cost1[NT__11] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__11] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 255);
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (softFloat && eqReg("%cc", t.kid(0).kid(1))) record(NT_void, 2 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 257);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (softFloat && eqReg("%cc", t.kid(0).kid(1))) record(NT_void, 2 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 259);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (softFloat && eqReg("%cc", t.kid(0).kid(1))) record(NT_void, 2 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 261);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) if (softFloat && eqReg("%cc", t.kid(0).kid(1))) record(NT_void, 2 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 2 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 263);
        if (kids[0].rule[NT__16] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__16] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__16] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 265);
        if (kids[0].rule[NT__17] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__17] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__17] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 267);
        if (kids[0].rule[NT__18] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__18] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__18] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 269);
        if (kids[0].rule[NT__19] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__19] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__19] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 271);
        if (kids[0].rule[NT__12] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__12] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__12] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 272);
        if (kids[0].rule[NT__13] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__13] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__13] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 273);
        if (kids[0].rule[NT__14] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__14] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__14] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 274);
        if (kids[0].rule[NT__15] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 4 + kids[0].cost1[NT__15] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 4 + kids[0].cost2[NT__15] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 275);
        if (kids[0].rule[NT__20] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 6 + kids[0].cost1[NT__20] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__20] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 277);
        if (kids[0].rule[NT__21] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 6 + kids[0].cost1[NT__21] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__21] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 279);
        if (kids[0].rule[NT__22] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 6 + kids[0].cost1[NT__22] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__22] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 281);
        if (kids[0].rule[NT__23] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 6 + kids[0].cost1[NT__23] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__23] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 283);
        if (kids[0].rule[NT__24] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 6 + kids[0].cost1[NT__24] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__24] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 285);
        if (kids[0].rule[NT__25] != 0) if (kids[1].rule[NT_label] != 0) if (kids[2].rule[NT_label] != 0) record(NT_void, 6 + kids[0].cost1[NT__25] + kids[1].cost1[NT_label] + kids[2].cost1[NT_label], 6 + kids[0].cost2[NT__25] + kids[1].cost2[NT_label] + kids[2].cost2[NT_label], 287);
        break;
      case Op.CALL:
        if (kids[0].rule[NT_regw] != 0) record(NT_void, 3 + kids[0].cost1[NT_regw], 2 + kids[0].cost2[NT_regw], 288);
        if (kids[0].rule[NT_asmcnst] != 0) record(NT_void, 3 + kids[0].cost1[NT_asmcnst], 2 + kids[0].cost2[NT_asmcnst], 289);
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
    rulev[15] = new Rule(15, true, false, 7, "15: xregw -> regw", ImList.list(ImList.list("addk","$0","$1","r0")), null, null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[40] = new Rule(40, true, false, 20, "40: addrr -> regw", null, ImList.list(ImList.list("base","r0","$1")), null, 0, false, false, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[17] = new Rule(17, true, false, 8, "17: xregh -> regh", ImList.list(ImList.list("addk","$0","$1","r0")), null, null, 0, false, false, new int[]{2}, new String[]{null, "*reg-I16*"});
    rulev[19] = new Rule(19, true, false, 9, "19: xregb -> regb", ImList.list(ImList.list("addk","$0","$1","r0")), null, null, 0, false, false, new int[]{3}, new String[]{null, "*reg-I8*"});
    rulev[21] = new Rule(21, true, false, 10, "21: xregf -> regf", ImList.list(ImList.list("addk","$0","$1","r0")), null, null, 0, false, false, new int[]{4}, new String[]{null, "*reg-F32*"});
    rulev[4] = new Rule(4, true, false, 1, "4: regw -> xregw", null, null, null, 0, false, false, new int[]{7}, new String[]{"*reg-I32*", null});
    rulev[7] = new Rule(7, true, false, 2, "7: regh -> xregh", null, null, null, 0, false, false, new int[]{8}, new String[]{"*reg-I16*", null});
    rulev[10] = new Rule(10, true, false, 3, "10: regb -> xregb", null, null, null, 0, false, false, new int[]{9}, new String[]{"*reg-I8*", null});
    rulev[13] = new Rule(13, true, false, 4, "13: regf -> xregf", null, null, null, 0, false, false, new int[]{10}, new String[]{"*reg-F32*", null});
    rulev[24] = new Rule(24, true, false, 12, "24: cnstf -> cnstf_", null, ImList.list(ImList.list("toInt","$1")), null, 0, false, false, new int[]{11}, new String[]{null, null});
    rulev[25] = new Rule(25, true, false, 4, "25: regf -> cnstf", ImList.list(ImList.list("addik","$0","r0","$1")), null, null, 0, false, false, new int[]{12}, new String[]{"*reg-F32*", null});
    rulev[27] = new Rule(27, true, false, 2, "27: regh -> cnsth", ImList.list(ImList.list("addik","$0","r0","$1"),ImList.list("andi","$0","$0","65535")), null, null, 0, false, false, new int[]{13}, new String[]{"*reg-I16*", null});
    rulev[29] = new Rule(29, true, false, 3, "29: regb -> cnstb", ImList.list(ImList.list("addik","$0","r0","$1"),ImList.list("andi","$0","$0","255")), null, null, 0, false, false, new int[]{14}, new String[]{"*reg-I8*", null});
    rulev[33] = new Rule(33, true, false, 18, "33: asmcnst -> cnst", null, null, null, 0, false, false, new int[]{16}, new String[]{null, null});
    rulev[34] = new Rule(34, true, false, 18, "34: asmcnst -> sta", null, null, null, 0, false, false, new int[]{17}, new String[]{null, null});
    rulev[38] = new Rule(38, true, false, 1, "38: regw -> asmcnst", ImList.list(ImList.list("addik","$0","r0","$1")), null, null, 0, false, false, new int[]{18}, new String[]{"*reg-I32*", null});
    rulev[39] = new Rule(39, true, false, 19, "39: addri -> asmcnst", null, ImList.list(ImList.list("base","r0","$1")), null, 0, false, false, new int[]{18}, new String[]{null, null});
    rulev[55] = new Rule(55, true, false, 1, "55: regw -> memwi", ImList.list(ImList.list("lwi","$0","$1")), null, null, 0, false, false, new int[]{21}, new String[]{"*reg-I32*", null});
    rulev[57] = new Rule(57, true, false, 2, "57: regh -> memhi", ImList.list(ImList.list("lhui","$0","$1")), null, null, 0, false, false, new int[]{22}, new String[]{"*reg-I16*", null});
    rulev[59] = new Rule(59, true, false, 3, "59: regb -> membi", ImList.list(ImList.list("lbui","$0","$1")), null, null, 0, false, false, new int[]{23}, new String[]{"*reg-I8*", null});
    rulev[61] = new Rule(61, true, false, 4, "61: regf -> memfi", ImList.list(ImList.list("lwi","$0","$1")), null, null, 0, false, false, new int[]{24}, new String[]{"*reg-F32*", null});
    rulev[63] = new Rule(63, true, false, 1, "63: regw -> memw", ImList.list(ImList.list("lw","$0","$1")), null, null, 0, false, false, new int[]{25}, new String[]{"*reg-I32*", null});
    rulev[65] = new Rule(65, true, false, 2, "65: regh -> memh", ImList.list(ImList.list("lhu","$0","$1")), null, null, 0, false, false, new int[]{26}, new String[]{"*reg-I16*", null});
    rulev[67] = new Rule(67, true, false, 3, "67: regb -> memb", ImList.list(ImList.list("lbu","$0","$1")), null, null, 0, false, false, new int[]{27}, new String[]{"*reg-I8*", null});
    rulev[69] = new Rule(69, true, false, 4, "69: regf -> memf", ImList.list(ImList.list("lw","$0","$1")), null, null, 0, false, false, new int[]{28}, new String[]{"*reg-F32*", null});
    rulev[26] = new Rule(26, false, false, 13, "26: cnsth -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[28] = new Rule(28, false, false, 14, "28: cnstb -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[30] = new Rule(30, false, false, 15, "30: cnst0 -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[31] = new Rule(31, false, false, 16, "31: cnst -> (INTCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[23] = new Rule(23, false, false, 11, "23: cnstf_ -> (FLOATCONST _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[32] = new Rule(32, false, false, 17, "32: sta -> (STATIC I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[8] = new Rule(8, false, false, 9, "8: xregb -> (REG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[5] = new Rule(5, false, false, 8, "5: xregh -> (REG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[2] = new Rule(2, false, false, 7, "2: xregw -> (REG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[11] = new Rule(11, false, false, 10, "11: xregf -> (REG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[9] = new Rule(9, false, false, 9, "9: xregb -> (SUBREG I8)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[6] = new Rule(6, false, false, 8, "6: xregh -> (SUBREG I16)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[3] = new Rule(3, false, false, 7, "3: xregw -> (SUBREG I32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[12] = new Rule(12, false, false, 10, "12: xregf -> (SUBREG F32)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[1] = new Rule(1, false, false, 6, "1: label -> (LABEL _)", null, null, null, 0, false, false, new int[]{}, new String[]{null});
    rulev[108] = new Rule(108, false, false, 1, "108: regw -> (NEG I32 regw)", ImList.list(ImList.list("rsub","$0","$1","r0")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[143] = new Rule(143, false, false, 4, "143: regf -> (NEG F32 regf)", ImList.list(ImList.list("xori","$0","$1","0x80000000")), null, null, 0, false, false, new int[]{4}, new String[]{"*reg-F32*", "*reg-F32*"});
    rulev[35] = new Rule(35, false, false, 18, "35: asmcnst -> (ADD I32 asmcnst cnst)", null, ImList.list(ImList.list("+","$1","$2")), null, 0, false, false, new int[]{18,16}, new String[]{null, null, null});
    rulev[36] = new Rule(36, false, false, 18, "36: asmcnst -> (ADD I32 cnst asmcnst)", null, ImList.list(ImList.list("+","$2","$1")), null, 0, false, false, new int[]{16,18}, new String[]{null, null, null});
    rulev[41] = new Rule(41, false, false, 19, "41: addri -> (ADD I32 regw asmcnst)", null, ImList.list(ImList.list("base","r1",ImList.list("adjDisp","$2"))), null, 0, false, false, new int[]{1,18}, new String[]{null, "*reg-I32*", null});
    rulev[42] = new Rule(42, false, false, 19, "42: addri -> (ADD I32 regw asmcnst)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{1,18}, new String[]{null, "*reg-I32*", null});
    rulev[43] = new Rule(43, false, false, 19, "43: addri -> (ADD I32 asmcnst regw)", null, ImList.list(ImList.list("base","$2","$1")), null, 0, false, false, new int[]{18,1}, new String[]{null, null, "*reg-I32*"});
    rulev[45] = new Rule(45, false, false, 20, "45: addrr -> (ADD I32 regw regw)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{1,1}, new String[]{null, "*reg-I32*", "*reg-I32*"});
    rulev[112] = new Rule(112, false, false, 1, "112: regw -> (ADD I32 regw regw)", ImList.list(ImList.list("add","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[113] = new Rule(113, false, false, 1, "113: regw -> (ADD I32 regw asmcnst)", ImList.list(ImList.list("addi","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[114] = new Rule(114, false, false, 1, "114: regw -> (ADD I32 asmcnst regw)", ImList.list(ImList.list("addi","$0","$2","$1")), null, null, 0, false, false, new int[]{18,1}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[144] = new Rule(144, false, false, 4, "144: regf -> (ADD F32 regf regf)", ImList.list(ImList.list("fadd","$0","$2","$1")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[37] = new Rule(37, false, false, 18, "37: asmcnst -> (SUB I32 asmcnst cnst)", null, ImList.list(ImList.list("-","$1","$2")), null, 0, false, false, new int[]{18,16}, new String[]{null, null, null});
    rulev[44] = new Rule(44, false, false, 19, "44: addri -> (SUB I32 regw cnst)", null, ImList.list(ImList.list("base","$1","$2")), null, 0, false, false, new int[]{1,16}, new String[]{null, "*reg-I32*", null});
    rulev[139] = new Rule(139, false, false, 1, "139: regw -> (SUB I32 regw regw)", ImList.list(ImList.list("rsub","$0","$2","$1")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[140] = new Rule(140, false, false, 1, "140: regw -> (SUB I32 asmcnst regw)", ImList.list(ImList.list("rsubi","$0","$2","$1")), null, null, 0, false, false, new int[]{18,1}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[145] = new Rule(145, false, false, 4, "145: regf -> (SUB F32 regf regf)", ImList.list(ImList.list("frsub","$0","$2","$1")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[111] = new Rule(111, false, false, 1, "111: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","$1"),ImList.list("addk","$0","$0","$1")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[115] = new Rule(115, false, false, 1, "115: regw -> (MUL I32 regw regw)", ImList.list(ImList.list("mul","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[116] = new Rule(116, false, false, 1, "116: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("muli","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[117] = new Rule(117, false, false, 1, "117: regw -> (MUL I32 asmcnst regw)", ImList.list(ImList.list("muli","$0","$2","$1")), null, null, 0, false, false, new int[]{18,1}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[127] = new Rule(127, false, false, 1, "127: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("bsll","r18","$1","2"),ImList.list("addk","$0","$0","r18")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[128] = new Rule(128, false, false, 1, "128: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("bsll","r18","$1","3"),ImList.list("addk","$0","$0","r18")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[129] = new Rule(129, false, false, 1, "129: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("bsll","$0","$1","1"),ImList.list("bsll","r18","$1","2"),ImList.list("addk","$0","$0","r18")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[130] = new Rule(130, false, false, 1, "130: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("bsll","$0","$1","1"),ImList.list("bsll","r18","$1","3"),ImList.list("addk","$0","$0","r18")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[131] = new Rule(131, false, false, 1, "131: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("bsll","r18","$1","1"),ImList.list("addk","$0","r18","$0"),ImList.list("bsll","r18","$1","2"),ImList.list("addk","$0","r18","$0")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[132] = new Rule(132, false, false, 1, "132: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("addk","r18","$1","r0"),ImList.list("addk","$0","r18","r0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","$0")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[133] = new Rule(133, false, false, 1, "133: regw -> (MUL I32 regw asmcnst)", new ImList(ImList.list("addk","r18","$1","r0"), ImList.list(ImList.list("addk","$0","r18","r0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","$0"))), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[134] = new Rule(134, false, false, 1, "134: regw -> (MUL I32 regw asmcnst)", ImList.list(ImList.list("addk","r18","$1","r0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","r0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","$0")), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[135] = new Rule(135, false, false, 1, "135: regw -> (MUL I32 regw asmcnst)", new ImList(ImList.list("addk","r18","$1","r0"), ImList.list(ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","r0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","$0"))), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[136] = new Rule(136, false, false, 1, "136: regw -> (MUL I32 regw asmcnst)", new ImList(ImList.list("addk","r18","$1","r0"), ImList.list(ImList.list("addk","$0","r18","r0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","$0"),ImList.list("addk","r18","r18","r18"),ImList.list("addk","$0","r18","$0"))), null, null, 0, true, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[146] = new Rule(146, false, false, 4, "146: regf -> (MUL F32 regf regf)", ImList.list(ImList.list("fmul","$0","$2","$1")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[138] = new Rule(138, false, false, 1, "138: regw -> (DIVS I32 regw regw)", ImList.list(ImList.list("idiv","$0","$2","$1")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[147] = new Rule(147, false, false, 4, "147: regf -> (DIVS F32 regf regf)", ImList.list(ImList.list("fdiv","$0","$2","$1")), null, null, 0, false, false, new int[]{4,4}, new String[]{"*reg-F32*", "*reg-F32*", "*reg-F32*"});
    rulev[137] = new Rule(137, false, false, 1, "137: regw -> (DIVU I32 regw regw)", ImList.list(ImList.list("idivu","$0","$2","$1")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[142] = new Rule(142, false, false, 1, "142: regw -> (MODS I32 regw regw)", ImList.list(ImList.list("idiv","$0","$2","$1"),ImList.list("mul","$0","$0","$2"),ImList.list("rsubk","$0","$0","$1")), null, null, 0, true, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[141] = new Rule(141, false, false, 1, "141: regw -> (MODU I32 regw regw)", ImList.list(ImList.list("idivu","$0","$2","$1"),ImList.list("mul","$0","$0","$2"),ImList.list("rsubk","$0","$0","$1")), null, null, 0, true, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[82] = new Rule(82, false, false, 2, "82: regh -> (CONVSX I16 regb)", ImList.list(ImList.list("sext8","$0","$1")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[78] = new Rule(78, false, false, 1, "78: regw -> (CONVSX I32 regh)", ImList.list(ImList.list("sext16","$0","$1")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[80] = new Rule(80, false, false, 1, "80: regw -> (CONVSX I32 regb)", ImList.list(ImList.list("sext8","$0","$1")), null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[83] = new Rule(83, false, false, 2, "83: regh -> (CONVZX I16 regb)", null, null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I16*", "*reg-I8*"});
    rulev[79] = new Rule(79, false, false, 1, "79: regw -> (CONVZX I32 regh)", null, null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I32*", "*reg-I16*"});
    rulev[81] = new Rule(81, false, false, 1, "81: regw -> (CONVZX I32 regb)", null, null, null, 0, false, false, new int[]{3}, new String[]{"*reg-I32*", "*reg-I8*"});
    rulev[85] = new Rule(85, false, false, 3, "85: regb -> (CONVIT I8 regw)", ImList.list(ImList.list("andi","$0","$1","255")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I8*", "*reg-I32*"});
    rulev[86] = new Rule(86, false, false, 3, "86: regb -> (CONVIT I8 regh)", ImList.list(ImList.list("andi","$0","$1","255")), null, null, 0, false, false, new int[]{2}, new String[]{"*reg-I8*", "*reg-I16*"});
    rulev[89] = new Rule(89, false, true, 30, "89: _2 -> (CONVIT I8 memwi)", null, null, null, 0, false, false, new int[]{21}, null);
    rulev[91] = new Rule(91, false, true, 31, "91: _3 -> (CONVIT I8 memhi)", null, null, null, 0, false, false, new int[]{22}, null);
    rulev[95] = new Rule(95, false, true, 33, "95: _5 -> (CONVIT I8 memw)", null, null, null, 0, false, false, new int[]{25}, null);
    rulev[97] = new Rule(97, false, true, 34, "97: _6 -> (CONVIT I8 memh)", null, null, null, 0, false, false, new int[]{26}, null);
    rulev[101] = new Rule(101, false, true, 36, "101: _8 -> (CONVIT I8 regw)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[103] = new Rule(103, false, true, 37, "103: _9 -> (CONVIT I8 regh)", null, null, null, 0, false, false, new int[]{2}, null);
    rulev[84] = new Rule(84, false, false, 2, "84: regh -> (CONVIT I16 regw)", ImList.list(ImList.list("andi","$0","$1","65535")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I16*", "*reg-I32*"});
    rulev[87] = new Rule(87, false, true, 29, "87: _1 -> (CONVIT I16 memwi)", null, null, null, 0, false, false, new int[]{21}, null);
    rulev[93] = new Rule(93, false, true, 32, "93: _4 -> (CONVIT I16 memw)", null, null, null, 0, false, false, new int[]{25}, null);
    rulev[99] = new Rule(99, false, true, 35, "99: _7 -> (CONVIT I16 regw)", null, null, null, 0, false, false, new int[]{1}, null);
    rulev[118] = new Rule(118, false, false, 1, "118: regw -> (BAND I32 regw regw)", ImList.list(ImList.list("and","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[119] = new Rule(119, false, false, 1, "119: regw -> (BAND I32 regw asmcnst)", ImList.list(ImList.list("andi","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[120] = new Rule(120, false, false, 1, "120: regw -> (BAND I32 asmcnst regw)", ImList.list(ImList.list("andi","$0","$2","$1")), null, null, 0, false, false, new int[]{18,1}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[121] = new Rule(121, false, false, 1, "121: regw -> (BOR I32 regw regw)", ImList.list(ImList.list("or","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[122] = new Rule(122, false, false, 1, "122: regw -> (BOR I32 regw asmcnst)", ImList.list(ImList.list("ori","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
  }
  static private void rrinit100() {
    rulev[123] = new Rule(123, false, false, 1, "123: regw -> (BOR I32 asmcnst regw)", ImList.list(ImList.list("ori","$0","$2","$1")), null, null, 0, false, false, new int[]{18,1}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[124] = new Rule(124, false, false, 1, "124: regw -> (BXOR I32 regw regw)", ImList.list(ImList.list("xor","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[125] = new Rule(125, false, false, 1, "125: regw -> (BXOR I32 regw asmcnst)", ImList.list(ImList.list("xori","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[126] = new Rule(126, false, false, 1, "126: regw -> (BXOR I32 asmcnst regw)", ImList.list(ImList.list("xori","$0","$2","$1")), null, null, 0, false, false, new int[]{18,1}, new String[]{"*reg-I32*", null, "*reg-I32*"});
    rulev[109] = new Rule(109, false, false, 1, "109: regw -> (BNOT I32 regw)", ImList.list(ImList.list("xori","$0","$1","-1")), null, null, 0, false, false, new int[]{1}, new String[]{"*reg-I32*", "*reg-I32*"});
    rulev[110] = new Rule(110, false, false, 1, "110: regw -> (LSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","$1")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[148] = new Rule(148, false, false, 1, "148: regw -> (LSHS I32 regw regw)", new ImList(ImList.list("andi","r18","$2","31"), new ImList(ImList.list("beqid","r18","$L2"), new ImList(ImList.list("addk","$0","$1","r0"), ImList.list(ImList.list("deflabel","$L1"),ImList.list("addik","r18","r18","-1"),ImList.list("bneid","r18","$L1"),ImList.list("addk","$0","$0","$0"),ImList.list("deflabel","$L2"))))), null, null, 0, true, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[149] = new Rule(149, false, false, 1, "149: regw -> (LSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","$1")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[150] = new Rule(150, false, false, 1, "150: regw -> (LSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[151] = new Rule(151, false, false, 1, "151: regw -> (LSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[152] = new Rule(152, false, false, 1, "152: regw -> (LSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[153] = new Rule(153, false, false, 1, "153: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[154] = new Rule(154, false, false, 1, "154: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[155] = new Rule(155, false, false, 1, "155: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[156] = new Rule(156, false, false, 1, "156: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[157] = new Rule(157, false, false, 1, "157: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[158] = new Rule(158, false, false, 1, "158: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[159] = new Rule(159, false, false, 1, "159: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[160] = new Rule(160, false, false, 1, "160: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[161] = new Rule(161, false, false, 1, "161: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[162] = new Rule(162, false, false, 1, "162: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[163] = new Rule(163, false, false, 1, "163: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[164] = new Rule(164, false, false, 1, "164: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[165] = new Rule(165, false, false, 1, "165: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[166] = new Rule(166, false, false, 1, "166: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[167] = new Rule(167, false, false, 1, "167: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[168] = new Rule(168, false, false, 1, "168: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[169] = new Rule(169, false, false, 1, "169: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[170] = new Rule(170, false, false, 1, "170: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[171] = new Rule(171, false, false, 1, "171: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[172] = new Rule(172, false, false, 1, "172: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[173] = new Rule(173, false, false, 1, "173: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[174] = new Rule(174, false, false, 1, "174: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[175] = new Rule(175, false, false, 1, "175: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[176] = new Rule(176, false, false, 1, "176: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[177] = new Rule(177, false, false, 1, "177: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[178] = new Rule(178, false, false, 1, "178: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0")))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[179] = new Rule(179, false, false, 1, "179: regw -> (LSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), new ImList(ImList.list("addk","$0","$0","$0"), ImList.list(ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"),ImList.list("addk","$0","$0","$0"))))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[244] = new Rule(244, false, false, 1, "244: regw -> (LSHS I32 regw regw)", ImList.list(ImList.list("bsll","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[245] = new Rule(245, false, false, 1, "245: regw -> (LSHS I32 regw asmcnst)", ImList.list(ImList.list("bslli","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[180] = new Rule(180, false, false, 1, "180: regw -> (RSHS I32 regw regw)", new ImList(ImList.list("andi","r18","$2","31"), new ImList(ImList.list("beqid","r18","$L2"), new ImList(ImList.list("addk","$0","$1","r0"), ImList.list(ImList.list("deflabel","$L1"),ImList.list("addik","r18","r18","-1"),ImList.list("bneid","r18","$L1"),ImList.list("sra","$0","$0"),ImList.list("deflabel","$L2"))))), null, null, 0, true, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[181] = new Rule(181, false, false, 1, "181: regw -> (RSHS I32 regw asmcnst)", ImList.list(ImList.list("sra","$0","$1")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[182] = new Rule(182, false, false, 1, "182: regw -> (RSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[183] = new Rule(183, false, false, 1, "183: regw -> (RSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[184] = new Rule(184, false, false, 1, "184: regw -> (RSHS I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[185] = new Rule(185, false, false, 1, "185: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[186] = new Rule(186, false, false, 1, "186: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[187] = new Rule(187, false, false, 1, "187: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[188] = new Rule(188, false, false, 1, "188: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[189] = new Rule(189, false, false, 1, "189: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[190] = new Rule(190, false, false, 1, "190: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[191] = new Rule(191, false, false, 1, "191: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[192] = new Rule(192, false, false, 1, "192: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[193] = new Rule(193, false, false, 1, "193: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[194] = new Rule(194, false, false, 1, "194: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[195] = new Rule(195, false, false, 1, "195: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[196] = new Rule(196, false, false, 1, "196: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[197] = new Rule(197, false, false, 1, "197: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[198] = new Rule(198, false, false, 1, "198: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[199] = new Rule(199, false, false, 1, "199: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[200] = new Rule(200, false, false, 1, "200: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[201] = new Rule(201, false, false, 1, "201: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[202] = new Rule(202, false, false, 1, "202: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[203] = new Rule(203, false, false, 1, "203: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[204] = new Rule(204, false, false, 1, "204: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[205] = new Rule(205, false, false, 1, "205: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[206] = new Rule(206, false, false, 1, "206: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[207] = new Rule(207, false, false, 1, "207: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[208] = new Rule(208, false, false, 1, "208: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[209] = new Rule(209, false, false, 1, "209: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[210] = new Rule(210, false, false, 1, "210: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0")))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[211] = new Rule(211, false, false, 1, "211: regw -> (RSHS I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), new ImList(ImList.list("sra","$0","$0"), ImList.list(ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"),ImList.list("sra","$0","$0"))))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[246] = new Rule(246, false, false, 1, "246: regw -> (RSHS I32 regw regw)", ImList.list(ImList.list("bsra","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[247] = new Rule(247, false, false, 1, "247: regw -> (RSHS I32 regw asmcnst)", ImList.list(ImList.list("bsrai","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[212] = new Rule(212, false, false, 1, "212: regw -> (RSHU I32 regw regw)", new ImList(ImList.list("andi","r18","$2","31"), new ImList(ImList.list("beqid","r18","$L2"), new ImList(ImList.list("addk","$0","$1","r0"), ImList.list(ImList.list("deflabel","$L1"),ImList.list("addik","r18","r18","-1"),ImList.list("bneid","r18","$L1"),ImList.list("srl","$0","$0"),ImList.list("deflabel","$L2"))))), null, null, 0, true, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[213] = new Rule(213, false, false, 1, "213: regw -> (RSHU I32 regw asmcnst)", ImList.list(ImList.list("srl","$0","$1")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[214] = new Rule(214, false, false, 1, "214: regw -> (RSHU I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[215] = new Rule(215, false, false, 1, "215: regw -> (RSHU I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[216] = new Rule(216, false, false, 1, "216: regw -> (RSHU I32 regw asmcnst)", ImList.list(ImList.list("addk","$0","$1","r0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[217] = new Rule(217, false, false, 1, "217: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[218] = new Rule(218, false, false, 1, "218: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[219] = new Rule(219, false, false, 1, "219: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[220] = new Rule(220, false, false, 1, "220: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[221] = new Rule(221, false, false, 1, "221: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[222] = new Rule(222, false, false, 1, "222: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[223] = new Rule(223, false, false, 1, "223: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[224] = new Rule(224, false, false, 1, "224: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[225] = new Rule(225, false, false, 1, "225: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[226] = new Rule(226, false, false, 1, "226: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[227] = new Rule(227, false, false, 1, "227: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[228] = new Rule(228, false, false, 1, "228: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[229] = new Rule(229, false, false, 1, "229: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[230] = new Rule(230, false, false, 1, "230: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[231] = new Rule(231, false, false, 1, "231: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[232] = new Rule(232, false, false, 1, "232: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[233] = new Rule(233, false, false, 1, "233: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[234] = new Rule(234, false, false, 1, "234: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[235] = new Rule(235, false, false, 1, "235: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[236] = new Rule(236, false, false, 1, "236: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[237] = new Rule(237, false, false, 1, "237: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
  }
  static private void rrinit200() {
    rulev[238] = new Rule(238, false, false, 1, "238: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[239] = new Rule(239, false, false, 1, "239: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[240] = new Rule(240, false, false, 1, "240: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[241] = new Rule(241, false, false, 1, "241: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[242] = new Rule(242, false, false, 1, "242: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0")))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[243] = new Rule(243, false, false, 1, "243: regw -> (RSHU I32 regw asmcnst)", new ImList(ImList.list("addk","$0","$1","r0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), new ImList(ImList.list("srl","$0","$0"), ImList.list(ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"),ImList.list("srl","$0","$0"))))))))))))))))))))))))))))), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[248] = new Rule(248, false, false, 1, "248: regw -> (RSHU I32 regw regw)", ImList.list(ImList.list("bsrl","$0","$1","$2")), null, null, 0, false, false, new int[]{1,1}, new String[]{"*reg-I32*", "*reg-I32*", "*reg-I32*"});
    rulev[249] = new Rule(249, false, false, 1, "249: regw -> (RSHU I32 regw asmcnst)", ImList.list(ImList.list("bsrli","$0","$1","$2")), null, null, 0, false, false, new int[]{1,18}, new String[]{"*reg-I32*", "*reg-I32*", null});
    rulev[250] = new Rule(250, false, true, 38, "250: _10 -> (TSTEQ I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[276] = new Rule(276, false, true, 48, "276: _20 -> (TSTEQ I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[252] = new Rule(252, false, true, 39, "252: _11 -> (TSTNE I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[278] = new Rule(278, false, true, 49, "278: _21 -> (TSTNE I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[258] = new Rule(258, false, true, 41, "258: _13 -> (TSTLTS I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[282] = new Rule(282, false, true, 51, "282: _23 -> (TSTLTS I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[256] = new Rule(256, false, true, 40, "256: _12 -> (TSTLES I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[280] = new Rule(280, false, true, 50, "280: _22 -> (TSTLES I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[262] = new Rule(262, false, true, 43, "262: _15 -> (TSTGTS I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[286] = new Rule(286, false, true, 53, "286: _25 -> (TSTGTS I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[260] = new Rule(260, false, true, 42, "260: _14 -> (TSTGES I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[284] = new Rule(284, false, true, 52, "284: _24 -> (TSTGES I32 regf regf)", null, null, null, 0, false, false, new int[]{4,4}, null);
    rulev[266] = new Rule(266, false, true, 45, "266: _17 -> (TSTLTU I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[264] = new Rule(264, false, true, 44, "264: _16 -> (TSTLEU I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[270] = new Rule(270, false, true, 47, "270: _19 -> (TSTGTU I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[268] = new Rule(268, false, true, 46, "268: _18 -> (TSTGEU I32 regw regw)", null, null, null, 0, false, false, new int[]{1,1}, null);
    rulev[48] = new Rule(48, false, false, 23, "48: membi -> (MEM I8 addri)", null, null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[52] = new Rule(52, false, false, 27, "52: memb -> (MEM I8 addrr)", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[47] = new Rule(47, false, false, 22, "47: memhi -> (MEM I16 addri)", null, null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[51] = new Rule(51, false, false, 26, "51: memh -> (MEM I16 addrr)", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[46] = new Rule(46, false, false, 21, "46: memwi -> (MEM I32 addri)", null, null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[50] = new Rule(50, false, false, 25, "50: memw -> (MEM I32 addrr)", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[49] = new Rule(49, false, false, 24, "49: memfi -> (MEM F32 addri)", null, null, null, 0, false, false, new int[]{19}, new String[]{null, null});
    rulev[53] = new Rule(53, false, false, 28, "53: memf -> (MEM F32 addrr)", null, null, null, 0, false, false, new int[]{20}, new String[]{null, null});
    rulev[18] = new Rule(18, false, false, 5, "18: void -> (SET I8 xregb regb)", ImList.list(ImList.list("addk","$1","$2","r0")), null, null, 0, false, false, new int[]{9,3}, new String[]{null, null, "*reg-I8*"});
    rulev[58] = new Rule(58, false, false, 5, "58: void -> (SET I8 xregb membi)", ImList.list(ImList.list("lbui","$1","$2")), null, null, 0, false, false, new int[]{9,23}, new String[]{null, null, null});
    rulev[66] = new Rule(66, false, false, 5, "66: void -> (SET I8 xregb memb)", ImList.list(ImList.list("lbu","$1","$2")), null, null, 0, false, false, new int[]{9,27}, new String[]{null, null, null});
    rulev[72] = new Rule(72, false, false, 5, "72: void -> (SET I8 membi regb)", ImList.list(ImList.list("sbi","$2","$1")), null, null, 0, false, false, new int[]{23,3}, new String[]{null, null, "*reg-I8*"});
    rulev[76] = new Rule(76, false, false, 5, "76: void -> (SET I8 memb regb)", ImList.list(ImList.list("sb","$2","$1")), null, null, 0, false, false, new int[]{27,3}, new String[]{null, null, "*reg-I8*"});
    rulev[90] = new Rule(90, false, false, 5, "90: void -> (SET I8 xregb _2)", ImList.list(ImList.list("lubi","$1","$2")), null, null, 0, false, false, new int[]{9,30}, new String[]{null, null, null});
    rulev[92] = new Rule(92, false, false, 5, "92: void -> (SET I8 xregb _3)", ImList.list(ImList.list("lubi","$1","$2")), null, null, 0, false, false, new int[]{9,31}, new String[]{null, null, null});
    rulev[96] = new Rule(96, false, false, 5, "96: void -> (SET I8 xregb _5)", ImList.list(ImList.list("lub","$1","$2")), null, null, 0, false, false, new int[]{9,33}, new String[]{null, null, null});
    rulev[98] = new Rule(98, false, false, 5, "98: void -> (SET I8 xregb _6)", ImList.list(ImList.list("lub","$1","$2")), null, null, 0, false, false, new int[]{9,34}, new String[]{null, null, null});
    rulev[102] = new Rule(102, false, false, 5, "102: void -> (SET I8 membi _8)", ImList.list(ImList.list("sbi","$2","$1")), null, null, 0, false, false, new int[]{23,36}, new String[]{null, null, "*reg-I32*"});
    rulev[104] = new Rule(104, false, false, 5, "104: void -> (SET I8 membi _9)", ImList.list(ImList.list("sbi","$2","$1")), null, null, 0, false, false, new int[]{23,37}, new String[]{null, null, "*reg-I16*"});
    rulev[106] = new Rule(106, false, false, 5, "106: void -> (SET I8 memb _8)", ImList.list(ImList.list("sb","$2","$1")), null, null, 0, false, false, new int[]{27,36}, new String[]{null, null, "*reg-I32*"});
    rulev[107] = new Rule(107, false, false, 5, "107: void -> (SET I8 memb _9)", ImList.list(ImList.list("sb","$2","$1")), null, null, 0, false, false, new int[]{27,37}, new String[]{null, null, "*reg-I16*"});
    rulev[16] = new Rule(16, false, false, 5, "16: void -> (SET I16 xregh regh)", ImList.list(ImList.list("addk","$1","$2","r0")), null, null, 0, false, false, new int[]{8,2}, new String[]{null, null, "*reg-I16*"});
    rulev[56] = new Rule(56, false, false, 5, "56: void -> (SET I16 xregh memhi)", ImList.list(ImList.list("lhui","$1","$2")), null, null, 0, false, false, new int[]{8,22}, new String[]{null, null, null});
    rulev[64] = new Rule(64, false, false, 5, "64: void -> (SET I16 xregh memh)", ImList.list(ImList.list("lhu","$1","$2")), null, null, 0, false, false, new int[]{8,26}, new String[]{null, null, null});
    rulev[71] = new Rule(71, false, false, 5, "71: void -> (SET I16 memhi regh)", ImList.list(ImList.list("shi","$2","$1")), null, null, 0, false, false, new int[]{22,2}, new String[]{null, null, "*reg-I16*"});
    rulev[75] = new Rule(75, false, false, 5, "75: void -> (SET I16 memh regh)", ImList.list(ImList.list("sh","$2","$1")), null, null, 0, false, false, new int[]{26,2}, new String[]{null, null, "*reg-I16*"});
    rulev[88] = new Rule(88, false, false, 5, "88: void -> (SET I16 xregh _1)", ImList.list(ImList.list("luhi","$1","$2")), null, null, 0, false, false, new int[]{8,29}, new String[]{null, null, null});
    rulev[94] = new Rule(94, false, false, 5, "94: void -> (SET I16 xregh _4)", ImList.list(ImList.list("luh","$1","$2")), null, null, 0, false, false, new int[]{8,32}, new String[]{null, null, null});
    rulev[100] = new Rule(100, false, false, 5, "100: void -> (SET I16 memhi _7)", ImList.list(ImList.list("shi","$2","$1")), null, null, 0, false, false, new int[]{22,35}, new String[]{null, null, "*reg-I32*"});
    rulev[105] = new Rule(105, false, false, 5, "105: void -> (SET I16 memh _7)", ImList.list(ImList.list("sh","$2","$1")), null, null, 0, false, false, new int[]{26,35}, new String[]{null, null, "*reg-I32*"});
    rulev[14] = new Rule(14, false, false, 5, "14: void -> (SET I32 xregw regw)", ImList.list(ImList.list("addk","$1","$2","r0")), null, null, 0, false, false, new int[]{7,1}, new String[]{null, null, "*reg-I32*"});
    rulev[54] = new Rule(54, false, false, 5, "54: void -> (SET I32 xregw memwi)", ImList.list(ImList.list("lwi","$1","$2")), null, null, 0, false, false, new int[]{7,21}, new String[]{null, null, null});
    rulev[62] = new Rule(62, false, false, 5, "62: void -> (SET I32 xregw memw)", ImList.list(ImList.list("lw","$1","$2")), null, null, 0, false, false, new int[]{7,25}, new String[]{null, null, null});
    rulev[70] = new Rule(70, false, false, 5, "70: void -> (SET I32 memwi regw)", ImList.list(ImList.list("swi","$2","$1")), null, null, 0, false, false, new int[]{21,1}, new String[]{null, null, "*reg-I32*"});
    rulev[74] = new Rule(74, false, false, 5, "74: void -> (SET I32 memw regw)", ImList.list(ImList.list("sw","$2","$1")), null, null, 0, false, false, new int[]{25,1}, new String[]{null, null, "*reg-I32*"});
    rulev[20] = new Rule(20, false, false, 5, "20: void -> (SET F32 xregf regf)", ImList.list(ImList.list("addk","$1","$2","r0")), null, null, 0, false, false, new int[]{10,4}, new String[]{null, null, "*reg-F32*"});
    rulev[60] = new Rule(60, false, false, 5, "60: void -> (SET F32 xregf memfi)", ImList.list(ImList.list("lwi","$1","$2")), null, null, 0, false, false, new int[]{10,24}, new String[]{null, null, null});
    rulev[68] = new Rule(68, false, false, 5, "68: void -> (SET F32 xregf memf)", ImList.list(ImList.list("lw","$1","$2")), null, null, 0, false, false, new int[]{10,28}, new String[]{null, null, null});
    rulev[73] = new Rule(73, false, false, 5, "73: void -> (SET F32 memfi regf)", ImList.list(ImList.list("swi","$2","$1")), null, null, 0, false, false, new int[]{24,4}, new String[]{null, null, "*reg-F32*"});
    rulev[77] = new Rule(77, false, false, 5, "77: void -> (SET F32 memf regf)", ImList.list(ImList.list("sw","$2","$1")), null, null, 0, false, false, new int[]{28,4}, new String[]{null, null, "*reg-F32*"});
    rulev[22] = new Rule(22, false, false, 5, "22: void -> (JUMP _ label)", ImList.list(ImList.list("bri","$1")), null, null, 0, false, false, new int[]{6}, new String[]{null, null});
    rulev[251] = new Rule(251, false, false, 5, "251: void -> (JUMPC _ _10 label label)", ImList.list(ImList.list("cmpu","r18","$1","$2"),ImList.list("beqi","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{38,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[253] = new Rule(253, false, false, 5, "253: void -> (JUMPC _ _11 label label)", ImList.list(ImList.list("cmpu","r18","$1","$2"),ImList.list("bnei","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{39,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[254] = new Rule(254, false, false, 5, "254: void -> (JUMPC _ _10 label label)", ImList.list(ImList.list("bnei","$1","$3")), null, null, 0, false, false, new int[]{38,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[255] = new Rule(255, false, false, 5, "255: void -> (JUMPC _ _11 label label)", ImList.list(ImList.list("beqi","$1","$3")), null, null, 0, false, false, new int[]{39,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[257] = new Rule(257, false, false, 5, "257: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("bgti","$1","$3")), null, null, 0, false, false, new int[]{40,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[259] = new Rule(259, false, false, 5, "259: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("bgei","$1","$3")), null, null, 0, false, false, new int[]{41,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[261] = new Rule(261, false, false, 5, "261: void -> (JUMPC _ _14 label label)", ImList.list(ImList.list("blti","$1","$3")), null, null, 0, false, false, new int[]{42,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[263] = new Rule(263, false, false, 5, "263: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("blei","$1","$3")), null, null, 0, false, false, new int[]{43,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[265] = new Rule(265, false, false, 5, "265: void -> (JUMPC _ _16 label label)", ImList.list(ImList.list("cmpu","r18","$1","$2"),ImList.list("bgei","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{44,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[267] = new Rule(267, false, false, 5, "267: void -> (JUMPC _ _17 label label)", ImList.list(ImList.list("cmpu","r18","$1","$2"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{45,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[269] = new Rule(269, false, false, 5, "269: void -> (JUMPC _ _18 label label)", ImList.list(ImList.list("cmpu","r18","$1","$2"),ImList.list("blei","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{46,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[271] = new Rule(271, false, false, 5, "271: void -> (JUMPC _ _19 label label)", ImList.list(ImList.list("cmpu","r18","$1","$2"),ImList.list("blti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{47,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[272] = new Rule(272, false, false, 5, "272: void -> (JUMPC _ _12 label label)", ImList.list(ImList.list("cmp","r18","$1","$2"),ImList.list("bgei","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{40,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[273] = new Rule(273, false, false, 5, "273: void -> (JUMPC _ _13 label label)", ImList.list(ImList.list("cmp","r18","$1","$2"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{41,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[274] = new Rule(274, false, false, 5, "274: void -> (JUMPC _ _14 label label)", ImList.list(ImList.list("cmp","r18","$1","$2"),ImList.list("blei","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{42,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[275] = new Rule(275, false, false, 5, "275: void -> (JUMPC _ _15 label label)", ImList.list(ImList.list("cmp","r18","$1","$2"),ImList.list("blti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{43,6,6}, new String[]{null, "*reg-I32*", "*reg-I32*", null, null});
    rulev[277] = new Rule(277, false, false, 5, "277: void -> (JUMPC _ _20 label label)", ImList.list(ImList.list("fcmp.eq","r18","$2","$1"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{48,6,6}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[279] = new Rule(279, false, false, 5, "279: void -> (JUMPC _ _21 label label)", ImList.list(ImList.list("fcmp.ne","r18","$2","$1"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{49,6,6}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[281] = new Rule(281, false, false, 5, "281: void -> (JUMPC _ _22 label label)", ImList.list(ImList.list("fcmp.le","r18","$2","$1"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{50,6,6}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[283] = new Rule(283, false, false, 5, "283: void -> (JUMPC _ _23 label label)", ImList.list(ImList.list("fcmp.lt","r18","$2","$1"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{51,6,6}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[285] = new Rule(285, false, false, 5, "285: void -> (JUMPC _ _24 label label)", ImList.list(ImList.list("fcmp.ge","r18","$2","$1"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{52,6,6}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[287] = new Rule(287, false, false, 5, "287: void -> (JUMPC _ _25 label label)", ImList.list(ImList.list("fcmp.gt","r18","$2","$1"),ImList.list("bgti","r18","$3")), null, ImList.list(ImList.list("REG","I32","%r18")), 0, false, false, new int[]{53,6,6}, new String[]{null, "*reg-F32*", "*reg-F32*", null, null});
    rulev[288] = new Rule(288, false, false, 5, "288: void -> (CALL _ regw)", ImList.list(ImList.list("brald","r15","$1"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{1}, new String[]{null, "*reg-I32*"});
    rulev[289] = new Rule(289, false, false, 5, "289: void -> (CALL _ asmcnst)", ImList.list(ImList.list("brlid","r15","$1"),ImList.list("delayslot")), null, null, 0, false, true, new int[]{18}, new String[]{null, null});
  }


  /** Return default register set for type. **/
  String defaultRegsetForType(int type) {
    switch (type) {
    case 514: return "*reg-I32*";
    case 258: return "*reg-I16*";
    case 130: return "*reg-I8*";
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
    case Op.REG:
      return jmac1(node);
    case Op.SUBREG:
      return jmac2(node);
    default:
      return quiltLirDefault(node);
    }
  }

  /** Expand emit-macro for list form. **/
  String emitList(ImList form, boolean topLevel) {
    String name = (String)form.elem();
    if (name == "line")
      return jmac3(emitObject(form.elem(1)));
    else if (name == "genasm")
      return jmac4(emitObject(form.elem(1)), form.elem(2));
    else if (name == "delayslot")
      return jmac5();
    else if (name == "base")
      return jmac6(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "+")
      return jmac7(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "-")
      return jmac8(emitObject(form.elem(1)), emitObject(form.elem(2)));
    else if (name == "toInt")
      return jmac9(emitObject(form.elem(1)));
    else if (name == "adjDisp")
      return jmac10(emitObject(form.elem(1)));
    else if (name == "prologue")
      return jmac11(form.elem(1));
    else if (name == "epilogue")
      return jmac12(form.elem(1), emitObject(form.elem(2)));
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
  
  // compile option
  boolean softFloat = false;
  boolean noUseGp = false;
  boolean noUseBs = false;
  boolean noUseMul = false;
  boolean noUseDiv = false;
  boolean isSimulate = false;
  
  private void debug(String s)
  {
      // System.out.println(s);
  }
  
  ImList regCallClobbers = new ImList(ImList.list("REG","I32","%r3"), new ImList(ImList.list("REG","I32","%r4"), new ImList(ImList.list("REG","I32","%r5"), new ImList(ImList.list("REG","I32","%r6"), new ImList(ImList.list("REG","I32","%r7"), ImList.list(ImList.list("REG","I32","%r8"),ImList.list("REG","I32","%r9"),ImList.list("REG","I32","%r10"),ImList.list("REG","I32","%r11"),ImList.list("REG","I32","%r12")))))));
  
  /** MicroBlaze's function attribute **/
  static class MicroBlazeAttr extends FunctionAttr {
  
    /** Maximum stack space used by call. (parameters and return address) **/
    int passedByStack;		// except r5-r10
    int functionParameters;
  
    /** callee save regsiters */
    int calleeSaves;
  
    /** pointer of aggregate return value. */
    LirNode hiddenPtr;
  
    /** real frame size */
    int frameSize;
  
    MicroBlazeAttr(Function func) {
      super(func);
      passedByStack = 0;	// leaf function.
      functionParameters = 0;	// leaf function.
      hiddenPtr = null;
    }
  }
  
  FunctionAttr newFunctionAttr(Function func) {
    MicroBlazeAttr attr = new MicroBlazeAttr(func);
    if (isSimulate) {
      attr.calleeSaves = 13 * REGWIDTH;
    }
    return attr;
  } 
  
  /// init.
  void initializeMachineDep() {
      Thread th = Thread.currentThread();
      if (th instanceof CompileThread) {
  	IoRoot io = ((CompileThread) th).getIoRoot();
  	CompileSpecification spec = io.getCompileSpecification();
  	CoinsOptions coinsOptions = spec.getCoinsOptions();
  	String opt = coinsOptions.getArg("x-mb");
  	if (opt != null) {
  	    Map m = coinsOptions.parseArgument(opt, '/', '.');
  	    if (m.get("no-fpu") != null) {
  		softFloat = true;
  	    }
  	    if (m.get("no-gp") != null) {
  		noUseGp = true;
  	    }
  	    if (m.get("no-bs") != null) {
  		noUseBs = true;
  	    }
  	    if (m.get("no-mul") != null) {
  		noUseMul = true;
  	    }
  	    if (m.get("no-div") != null) {
  		noUseDiv = true;
  	    }
  	}
          if (coinsOptions.isSet("simulate")) {
            isSimulate = true;
          }
      }
  }
  
  /// define constants.
  
  static final int REGWIDTH = 4;
  
  
  /// code building macros
  
  /* NB: Gas of MicroBlaze doesn't accept %-prefix of register */
  Object jmac1(LirNode x) {
    LirSymRef regvar = (LirSymRef)x;
    Symbol reg = regvar.symbol;
    return reg.name.substring(1);
  }
  
  Object jmac2(LirNode x) {
    Symbol reg = ((LirSymRef)x.kid(0)).symbol;
    return reg.name.substring(1);
  }
  
  /// code emission macros
  
  String jmac3(String x) { return "# line " + x; }
  
  String jmac4(String format, Object args) { return emitAsmCode(format, (ImList)args); }
  
  String jmac5() {
    return "\tnop\t#unfilled delayslot";
  }
  
  String jmac6(String reg1, String reg2) {
    return reg1 + "," + reg2;
  }
  
  String jmac7(String c1, String c2) {
    return c1 + "+" + c2;
  }
  String jmac8(String c1, String c2) {
    return c1 + "-" + c2;
  }
  
  String jmac9(String x) {
    double value = Float.parseFloat(x);
    long bits = Float.floatToIntBits((float)value);
    return Long.toString(bits & 0xffffffffL, 10) + " # " + x;
  }
  
  String jmac10(String x) {
      MicroBlazeAttr attr = (MicroBlazeAttr) getFunctionAttr(func);
      debug(" adjDisp " + x + " <- callee save " + attr.calleeSaves);
      long off = Long.parseLong(x) + attr.calleeSaves + frameSize(func);
      debug(" -> " + off);
      return String.valueOf(off);
  }
  
  String jmac11(Object f) {
      Function func = (Function) f;
      MicroBlazeAttr attr = (MicroBlazeAttr) getFunctionAttr(func);
      SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
      if (!isSimulate) {
        attr.calleeSaves = saveList.calleeSave.size() * REGWIDTH;
      }
      int adjustedSize = (frameSize(func) + 3) & -4;
      attr.frameSize = attr.functionParameters + adjustedSize + attr.calleeSaves;
  
      String seq = "";
      if (attr.frameSize > 0) {
  	seq += "\n\taddik\tr1,r1,-" + attr.frameSize;
      }
      if (attr.functionParameters > 0) { // non leaf function
  	seq += "\n\tsw\tr15,r0,r1";
      }
  
      int off = attr.functionParameters + frameSize(func);
      for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
  	int reg = it.next();
  	seq += "\n\tswi\t" + machineParams.registerToString(reg).substring(1) +",r1," + off;
  	off += REGWIDTH;
      }
      return "#prologue: callee save " + attr.calleeSaves +
  	"\n# function params. = " + attr.passedByStack +
  	"\n# frame vars. = " + frameSize(func) + seq;
  }
  
  String jmac12(Object f, String rettype) {
    Function func = (Function)f;
    MicroBlazeAttr attr = (MicroBlazeAttr) getFunctionAttr(func);
    SaveRegisters saveList = (SaveRegisters)func.require(SaveRegisters.analyzer);
    int off = attr.functionParameters + frameSize(func);
  
    String seq = "";
  
    if (attr.functionParameters > 0) { // non leaf function
        seq += "\n\tlw\tr15,r0,r1";
    }
  
    // pop all callee save registers.
    for (NumberSet.Iterator it = saveList.calleeSave.iterator(); it.hasNext(); ) {
        int reg = it.next();
        seq += "\n\tlwi\t" + machineParams.registerToString(reg).substring(1) + ",r1," + off;
        off += 4;
    }
    seq += "\n\trtsd\tr15,8";
    seq += (attr.frameSize > 0) ? "\n\taddik\tr1,r1," + attr.frameSize : "\n\tnop\t#unfilled delayslot";
    return seq;
  }
  
  
  /// Emit data common
  void emitCommon(PrintWriter out, SymStatic symbol, int bytes) {
      emitSegment(out, ".bss");
      if (symbol.linkage == "LDEF")
  	out.println("\t.lcomm\t" + makeAsmSymbol(symbol.name)
  		    + "," + bytes);
      else
  	out.println("\t.comm\t"  + makeAsmSymbol(symbol.name)
  		    + ","  + bytes /*+ "," + symbol.boundary*/);
  }
  
  
  /// LIR utilities
  
  private LirNode setExp(int type, LirNode op1, LirNode op2) {
      return lir.node(Op.SET, type, op1, op2);
  }
  
  private LirNode memExp(int type, LirNode op1) {
      return lir.node(Op.MEM, type, op1);
  }
  
  private LirNode memExp(int type, LirNode op1, int align) {
      return lir.operator
  	(Op.MEM, type, op1,
  	 ImList.list("&align", String.valueOf(align)));
  }
  
  private LirNode setI32(LirNode op1, LirNode op2) {
      return lir.node(Op.SET, I32, op1, op2);
  }
  
  private LirNode memI32(LirNode op1) {
      return memExp(I32, op1);
  }
  
  private LirNode memI32(LirNode op1, int align) {
      return memExp(I32, op1, align);
  }
  
  private LirNode memI32Base(LirNode base, int disp) {
      return memI32(addI32(base, disp));
  }
  
  private LirNode memI32Base(LirNode base, int disp, int align) {
      return memI32(addI32(base, disp), align);
  }
  
  private LirNode addI32(LirNode op1, int n) {
      return lir.node(Op.ADD, I32, op1, lir.iconst(I32, n));
  }
  
  private LirNode regI32(int nth) {
      return lir.symRef(module.globalSymtab.get("%r" + nth));
  }
  
  private LirNode callExp(LirNode callee, LirNode params, LirNode val)
  {
      LirNode list = null;
      try {
  	list = lir.node
  	    (Op.PARALLEL, Type.UNKNOWN, 
  	     noRescan(lir.operator(Op.CALL, Type.UNKNOWN, callee, params, val, ImList.list())), lir.decodeLir(new ImList("CLOBBER", regCallClobbers), func, module));
      } catch (SyntaxError e) {
  	throw new CantHappenException();
      }
      return list;
  }
  
  ///
  
  // check register constraint
  boolean eqReg(String reg, LirNode node)
  {
      if (!(node instanceof LirSymRef)) {
  	return false;
      }
      return ((LirSymRef)node).symbol == func.module.globalSymtab.get(reg);
  }
  
  // check constant multiply
  boolean rewriteMul(LirNode node)
  {
      if (!noUseMul) {		// hard mul
  	return false;
      }
      if (!(node instanceof LirIconst)) {	// soft mul, not const.
      System.out.println("mul::: node is not const: " + node);
  	return true;
      }
      long n = ((LirIconst) node).signedValue();
      return (n < 0 || n > 10);
  }
  
  /// tree rewriting
  private LirNode regnode(int type, String name) {
      LirNode master = lir.symRef(module.globalSymtab.get(name));
      type = castToInt(type);
  
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
  	/* Is it incorrect ? (because float is defined in SUBREG I32) */ //##74
  	if (type == F64)
  	    return master;
  	else if (type == F32)
  	    return lir.node
  		(Op.SUBREG, F32, master, lir.untaggedIconst(I32, 0));
      default:
  	return null;
      }
  }
  
  private boolean isAgg(int type) {
      return Type.tag(type) == Type.AGGREGATE;
  }
  
  // cast aggregate to int.
  private int castToInt(int type) {
      return isAgg(type) ? Type.type(Type.INT, Type.bytes(type) * 8) : type;
  }
  
  LirNode castToInt(LirNode src) {
      if (src.opCode == Op.MEM) {
  	return memExp(castToInt(src.type), src.kid(0));
      }
      return src;
  }
  
  // Test the necessity of using work in case of loading from src to general register. //##74
  boolean needTempArea(LirNode src) {
    // In case of ARM, can not be loaded from floating register directly. //##74
    // Unnecessary in case of MicroBlaze.
    return false;
  }
  
  boolean isMemNode(LirNode arg) {
      return arg.opCode == Op.MEM;
  }
  
  class CallContext {
      LirNode sp = lir.symRef(func.module.globalSymtab.get("%r1"));
      int disp;			// disp. of first argument on stack
      String[] regName = { "%r5",  "%r6",  "%r7",  "%r8",  "%r9",  "%r10" };
      int regUsed;		// -1 -> no used reg.
      int regMax;
      BiList regAssign;
      BiList stackAssign;
      boolean prologue;
      int base = 28;
  
      CallContext(boolean isCallee) {
  	disp = 0;
  	regUsed = -1;
  	regMax = 5;
  	regAssign = new BiList();
  	stackAssign = new BiList();
  	prologue = isCallee;
  	if (prologue) {
  	    sp = lir.symRef(func.module.globalSymtab.get("%vsp"));
  	}
      }
  
      void adjDisp(int n) {
  	disp += ((n + 3) & -4);
      }
  
      int spDisp() {
  	if (prologue) {
  	    MicroBlazeAttr at = (MicroBlazeAttr)getFunctionAttr(func);
  	    return base + at.functionParameters + disp;
  	}
  	return base + disp;
      }
  
      boolean availReg() {
  	return regMax > regUsed;
      }
  
      LirNode copyNode(int type, LirNode op1, LirNode op2) {
  	if (prologue) {
  	    return setExp(type, op2, op1);
  	}
  	return setExp(type, op1, op2);
      }
  
      // todo: double treatment
      LirNode getReg(int type) {
  	if (regUsed >= regMax) {
  	    return null;
  	}
  	regUsed++;
  	return regnode(type, regName[regUsed]);
      }
      void setReg(int type, LirNode src) {
  	regAssign.add(copyNode(castToInt(type), getReg(type), castToInt(src)));
      }
      void setArg(LirNode arg) {
  	int n = Type.bytes(arg.type);
  
  	if (regMax <= regUsed) { // all register is used.
  	    stackAssign.add(copyNode(arg.type, memExp(arg.type, addI32(sp, spDisp()), 4), arg));
  	    // Is there a possibility of unaligned but correct ? //##74
  	    adjDisp(n);
  	    return;
  	}
  	// THere is some item that can be set on a register.
  	if (needTempArea(arg)) {
  	    // Assign to temp. (In ARM, float register can not be copied to 
              //  integer register.
  	    // In MicroBlaze, this processing is unnecessary.
  	    // Write to temp and replace arg by temp.  //##74
  	}
  	if (n <= REGWIDTH) {
  	    // If register is remaining and the size of src does not exceed 
              // the size of register, then it can be copied by simple operation. //##74
  	    setReg(arg.type, arg);
  	    return;
  	}
  	
  	// Here, src is mem in any case.
          // Expand the operation to (several) load operations of I32 length.
          // It is not necessary to consider whether it is structure or not.
          // If basic type is treated as special case, then the (generated) code
          // will increase as the case of arm.tmd.  //##74
  	debug("*** arg is " + arg);
  	int i = 0;
  	for (/* empty */; i < n; i += REGWIDTH) {
  	    if (availReg()) {
  		LirNode split = memI32Base(arg.kid(0), i, REGWIDTH);
  		debug(" -> " + split);
  		setArg(split);
  	    } else {
  		// Remaining one is copy from mem to mem.
  		// Copy of small size items may not produce good code.
                  // It may be better to expand the operation.
  		LirNode split = memExp(Type.type(Type.AGGREGATE, (n - i) * 8), addI32(arg.kid(0), i), 4);
  		debug(" -> " + split);
  		setArg(split);
  		break;
  	    }
  	}
      }
  
      LirNode[] getNewArgv() {
  	// make new parameters (on register)
  	LirNode[] newargv = new LirNode[regUsed + 1];
  	for (int i = 0; i < regUsed + 1; i++) { // NB: regUsed starts -1.
  	    newargv[i] = regnode(I32, regName[i]);
  	}
  	return newargv;
      }
  
      BiList getRegAssign() {
  	return regAssign;
      }
      BiList getStackAssign() {
  	return stackAssign;
      }
      int stackSize() {
  	return disp + base;
      }
  }
  
  
  LirNode rewriteCall(LirNode node, BiList pre, BiList post, boolean dry) {
      LirNode callee = node.kid(0);
      LirNode args = node.kid(1);
  
      LirNode retreg = regI32(4);
      LirNode fval = null;
      CallContext cc = new CallContext(false);
      boolean smallAggr = false;
      MicroBlazeAttr at = (MicroBlazeAttr)getFunctionAttr(func);
  
      if (node.kid(2).nKids() > 0) {
  	fval = node.kid(2).kid(0);
          if (isAgg(fval.type)) {
  	    debug("size " + Type.bytes(fval.type));
  	    if (true /*Type.bytes(fval.type) > REGWIDTH*/) { // pass hidden pointer
  		cc.setReg(I32, fval.kid(0));
  	    } else {	// aggregate return by register
  		// arm only. mb returns always using pointer.
  		smallAggr = true;
  	    }
  	}
      }
  
      for (int i = 0; i < args.nKids(); i++) {
  	cc.setArg(args.kid(i));
      }
  
      if (at.passedByStack < cc.stackSize()) {
  	at.passedByStack = cc.stackSize();
  	if (at.passedByStack > 0) {
  	    at.functionParameters = at.passedByStack + 28;
  	}
      }
  
      if (dry) {
  	debug("dry run: pass-ed by Stack = " + at.passedByStack);
  	return node;
      }
      // insert setup code.
      pre.addAllFirst(cc.getStackAssign());
      pre.addAll(cc.getRegAssign());
  
      // modified CALL node
      LirNode list = callExp(node.kid(0), lir.node(Op.LIST, Type.UNKNOWN, cc.getNewArgv()), node.kid(2));
      
      // value returned
      if (fval != null) {
  	int ftag = Type.tag(fval.type);
  	if (ftag == Type.INT || ftag == Type.FLOAT ||
  	    (ftag == Type.AGGREGATE && smallAggr)) {
  	    int rty = (ftag == Type.AGGREGATE) ? I32 : fval.type;
  	    LirNode reg = regnode(rty, ftag == Type.FLOAT ? "%r3" : "%r3");
  	    LirNode tmp = func.newTemp(rty);
  
  	    post.add(setExp(rty, tmp, reg));
  	    post.add(setExp(rty, (smallAggr) ? memI32(fval.kid(0)) : fval, tmp));
  	    list.kid(0).kid(2).setKid(0, reg);
  	}
      }
      debug("*** required space of function Parameters = " + at.passedByStack);
      debug("*** -pre--> " + pre);
      debug("*** ------> " + list);
      debug("*** -post-> " + post);
      return list;
  }
  
  
  LirNode rewritePrologue(LirNode node, BiList post) {
      MicroBlazeAttr attr = (MicroBlazeAttr) getFunctionAttr(func);
      CallContext cc = new CallContext(true);
  
      debug("*** rewriting prologue " + node);
      debug("*** node.nKids = " + node.nKids());
  
      // Is first parameter a pointer to aggregate return-value ?
      if (func.origEpilogue.nKids() > 1) {
  	int t = func.origEpilogue.kid(1).type;
  	if (Type.tag(t) == Type.AGGREGATE) {
  	    attr.hiddenPtr = func.newTemp(I32);
  	    cc.setArg(attr.hiddenPtr /*node.kid(i)*/);
  	}
      }
  
      for (int i = 1; i < node.nKids(); i++) {
  	cc.setArg(node.kid(i));
      }
  
  /*
    It may be better to check whether the stack size exceeds the limit. 
      if (at.stackRequired < cc.stackSize()) {
  	at.stackRequired = cc.stackSize();
      }
  */
  
      post.addAllFirst(cc.getStackAssign());
      post.addAll(cc.getRegAssign());
  
      // create new prologue
      LirNode list = lir.node(Op.PROLOGUE, Type.UNKNOWN, cc.getNewArgv());
  
      debug("*** ------> " + list);
      debug("*** -post-> " + post);
      return list;
  }
  
  
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
      case Type.FLOAT:
          LirNode r3 = regnode(ret.type, "%r3");
  	pre.add(setExp(ret.type, r3, ret));
  	epilogue = lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), r3);
  	break;
  
      case Type.AGGREGATE:
  	MicroBlazeAttr attr = (MicroBlazeAttr) getFunctionAttr(func);
          r3 = regnode(I32, "%r3");
  	debug("*** reta = " + attr.hiddenPtr);
  	// checkme
  	pre.add(setExp(ret.type, memExp(ret.type, attr.hiddenPtr, 4), ret));
  	pre.add(setI32(r3, attr.hiddenPtr));
  	epilogue = lir.node(Op.EPILOGUE, Type.UNKNOWN, node.kid(0), r3);
  	break;
      }
      debug("*** -pre-> " + pre);
      debug("*** -----> " + epilogue);
      return epilogue;
  }
  
  
  LirNode rewriteFrame(LirNode node) {
      Symbol fp = func.module.globalSymtab.get("%r1");
      MicroBlazeAttr attr = (MicroBlazeAttr) getFunctionAttr(func);
      int n = attr.functionParameters;
      int off = ((SymAuto)((LirSymRef)node).symbol).offset();
      int sz = Type.bytes(((SymAuto)((LirSymRef)node).symbol).type);
      off = -off - sz; // first change sign, and shift down it's size. 
      // debug("cur param size =" + n + ", rewriteing: " + node + ", off = " + off + ", size = " + sz);
      return lir.node
  	(Op.ADD, node.type, lir.symRef(fp), lir.iconst(I32, n + (long)off));
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
  
  
  // rewrite floating compare to asm-exp and integer compare
  LirNode rewriteJumpc(LirNode node, BiList pre, String rtl, int n) {
      debug("rewriteJumpc: $0 = " + node);
      LirNode tst = node.kid(0);
  
      if (Type.tag(tst.kid(0).type) != Type.FLOAT) {
  	return node;
      }
  
      // make temporal
      LirNode tmp = lir.symRef(func.module.globalSymtab.get("%r3"));
      LirNode cc = lir.symRef(func.module.globalSymtab.get("%cc"));
      // resultant exp.
      LirNode list;
  
      // setup parameter
      LirNode[] in = new LirNode[n];
      LirNode[] out = new LirNode[0];
      LirNode[] io = new LirNode[0];
  
      for (int i = 0; i < n; i++) {
  	in[i] = tst.kid(i);
  	// debug("kid = "  + in[i]);
      }
  
      LirNode body = lir.stringconst(
  "\n\taddik\tr19,r5,0\n\taddik\tr20,r6,0" +
  "\n\tbrlid\tr15," + rtl + "\n\tnop\t# unfilled delay slot" +
  "\n\taddik\tr5,r19,0\n\taddik\tr6,r20,0"
  );
      // This delay slot can not be filled by scheduler. //##74
  
      list = lir.operator(Op.ASM, Type.UNKNOWN,
  			new LirNode[] {
  			    body, 
  			    lir.node(Op.LIST, Type.UNKNOWN, in),
  			    lir.node(Op.LIST, Type.UNKNOWN, out),
  			    lir.node(Op.LIST, Type.UNKNOWN, io)
  			},
  			ImList.list("&argtype", 
  				    ImList.list("%r5-F32", "%r6-F32"), 
  				    "&clobber", new ImList("%r3", ImList.list("%r4", "%r7", "%r8", "%r9", "%r19", "%r20")))
  			    );
      
      debug("asm = " + list);
      pre.add(list);
  
      // return modified jumpc node 
      LirNode[] src = new LirNode[3];
      src[0] = lir.operator(tst.opCode, I32, tmp, cc, ImList.Empty);
      src[1] = node.kid(2);
      src[2] = node.kid(1);
      return lir.operator(Op.JUMPC, Type.UNKNOWN, src, ImList.Empty);
  }
}
