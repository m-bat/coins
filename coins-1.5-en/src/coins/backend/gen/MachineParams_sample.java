package coins.backend.gen;

import coins.backend.Storage;
import coins.backend.sym.SymTab;
import coins.backend.util.ImList;

public class MachineParams_sample extends coins.backend.MachineParams {

  public int nRegisters() { return 17; }
  public int nRegsets() { return 19; }

  public int typeAddress() { return 514; }
  public int typeBool() { return 514; }
  public String[] getSymName() {
    return new String[]{
      "%r0",
      "%r1",
      "%r2",
      "%r3",
      "%r4",
      "%r5",
      "%r6",
      "%r7",
      "%f0",
      "%f1",
      "%f2",
      "%f3",
      "%f4",
      "%f5",
      "%f6",
      "%f7",
    };
  };
  public int[] getSymType() {
    return new int[] {
      514,
      514,
      514,
      514,
      514,
      514,
      514,
      514,
      1028,
      1028,
      1028,
      1028,
      1028,
      1028,
      1028,
      1028,
    };
  };
  public int[] getSymRegNumber() {
    return new int[] {
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9,
      10,
      11,
      12,
      13,
      14,
      15,
      16,
    };
  };

  public short[][] getOverlapReg() {
    return new short[][] { {},
  /* 1: */ {},
  /* 2: */ {},
  /* 3: */ {},
  /* 4: */ {},
  /* 5: */ {},
  /* 6: */ {},
  /* 7: */ {},
  /* 8: */ {},
  /* 9: */ {},
  /* 10: */ {},
  /* 11: */ {},
  /* 12: */ {},
  /* 13: */ {},
  /* 14: */ {},
  /* 15: */ {},
  /* 16: */ {},
    };
  };

  public short[][] getSuperReg() {
    return new short[][] { {},
  /* 1: */ {},
  /* 2: */ {},
  /* 3: */ {},
  /* 4: */ {},
  /* 5: */ {},
  /* 6: */ {},
  /* 7: */ {},
  /* 8: */ {},
  /* 9: */ {},
  /* 10: */ {},
  /* 11: */ {},
  /* 12: */ {},
  /* 13: */ {},
  /* 14: */ {},
  /* 15: */ {},
  /* 16: */ {},
    };
  };

  public short[][] getSubReg() {
    return new short[][] { {},
  /* 1: */ {},
  /* 2: */ {},
  /* 3: */ {},
  /* 4: */ {},
  /* 5: */ {},
  /* 6: */ {},
  /* 7: */ {},
  /* 8: */ {},
  /* 9: */ {},
  /* 10: */ {},
  /* 11: */ {},
  /* 12: */ {},
  /* 13: */ {},
  /* 14: */ {},
  /* 15: */ {},
  /* 16: */ {},
    };
  };

  public String[] getRegsetName() {
    return new String[] {
      "*reg-I32*",
      "*reg-F64*",
    };
  };
  public int[] getRegsetNumber() {
    return new int[] {
      17,
      18,
    };
  };
  public short[][] getRegsetMap() {
    return new short[][] {
      {1,2,3,4,5,6,7,8,},
      {9,10,11,12,13,14,15,16,},
    };
  };

  public short[] getRegsetNAvail() {
    return new short[] {
      0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 8, 8,     };
  };

  public int[] getCompAndTbl() {
    return new int[] {
      -137,1,2,3,4,5,6,7,8,
      -162,9,10,11,12,13,14,15,16,
    };
  };

  public int[] getCompWeightTbl() {
    return new int[] {
    20,65,
    36,65,
    40,65,
    55,65,
    60,65,
    74,65,
    80,65,
    93,65,
    100,65,
    112,65,
    120,65,
    131,65,
    140,65,
    150,65,
    160,65,
    169,65,
    180,65,
    189,65,
    200,65,
    208,65,
    220,65,
    227,65,
    240,65,
    246,65,
    260,65,
    265,65,
    280,65,
    284,65,
    300,65,
    303,65,
    320,65,
    322,65,
    324,513,
    340,65,
    351,513,
    360,65,
    };
  };

  public int[] getRegsetTypeTbl() {
    return new int[] {
    -1,
    514,
    514,
    514,
    514,
    514,
    514,
    514,
    514,
    1028,
    1028,
    1028,
    1028,
    1028,
    1028,
    1028,
    1028,
    514,
    1028,
    };
  }
}
