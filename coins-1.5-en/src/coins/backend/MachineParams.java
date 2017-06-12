/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import coins.backend.Module;
import coins.backend.Data;
import coins.backend.Op;
import coins.backend.Storage;
import coins.backend.Type;
import coins.backend.CantHappenException;
import coins.backend.lir.LirIconst;
import coins.backend.lir.LirNode;
import coins.backend.lir.LirSymRef;
import coins.backend.sym.SymTab;
import coins.backend.sym.Symbol;
import coins.backend.util.BitMapSet;
import coins.backend.util.ImList;
import coins.backend.util.NumberSet;
import java.util.HashMap;
import java.util.Map;

/** Object holding Machine Parameters. **/
public abstract class MachineParams {

  /** Register external symbols of built-in functions and static objects. */
  public void addRequired(SymTab symtbl) {}

  /** Return number of physical registers. **/
  public abstract int nRegisters();

  /** Return number of register sets. **/
  public abstract int nRegsets();

  /** Return address type. **/
  public abstract int typeAddress();

  /** Return boolean type. **/
  public abstract int typeBool();

  /** Return array of register's names. **/
  public abstract String[] getSymName();
  public abstract int[] getSymType();
  public abstract int[] getSymRegNumber();

  public abstract short[][] getOverlapReg();
  public abstract short[][] getSuperReg();
  public abstract short[][] getSubReg();

  public abstract String[] getRegsetName();
  public abstract int[] getRegsetNumber();
  public abstract short[][] getRegsetMap();
  public abstract short[] getRegsetNAvail();

  public abstract int[] getCompAndTbl();
  public abstract int[] getCompWeightTbl();

  public abstract int[] getRegsetTypeTbl();


  /** Parameters got from TargetMachine_TARGETCPU */
  private int nRegisters;
  private int nRegsets;

  private Symbol[] regSymbol;
  // private Symbol[] parentSymbol;
  // private int[] regPosition;
  private short[] symbolRegTbl;

  private short[][] overlapRegVec;
  private short[][] superRegVec;
  private short[][] subRegVec;

  private Map regsetTable = new HashMap();
  private BitMapSet[] bitMapVec;
  private short[] regsetNAvailVec;

  private int[] andTbl;
  private char[] weightTbl;

  private int[] regsetTypeTbl;


  private Module module;
  private SymTab symTab;

  public MachineParams() {}

  public void init(Module mod, SymTab symTab) {
    module = mod;
    this.symTab = symTab;

    nRegisters = nRegisters();
    nRegsets = nRegsets();

    regSymbol = new Symbol[nRegisters];
    // parentSymbol = new Symbol[nRegisters];
    // regPosition = new int[nRegisters];
    
    String[] symName = getSymName();
    int[] symType = getSymType();
    int[] symRegNumber = getSymRegNumber();
    int maxId = 0;
    for (int i = 0; i < symName.length; i++) {
      Symbol s = symTab.addSymbol(symName[i], Storage.REG,
                                  symType[i],
                                  Type.bytes(symType[i]),
                                  0, null);
      if (symRegNumber[i] != 0)
        regSymbol[symRegNumber[i]] = s;
      if (s.id > maxId)
        maxId = s.id;
    }

    // Set up (symbol -> register number) table
    symbolRegTbl = new short[maxId + 1];
    for (int i = 0; i < symName.length; i++) {
      if (symRegNumber[i] != 0)
        symbolRegTbl[regSymbol[symRegNumber[i]].id] = (short)symRegNumber[i];
    }

    overlapRegVec = getOverlapReg();
    superRegVec = getSuperReg();
    subRegVec = getSubReg();

    // register set
    String[] regsetName = getRegsetName();
    int[] regsetNumber = getRegsetNumber();
    for (int i = 0; i < regsetName.length; i++)
      regsetTable.put(regsetName[i], new Integer(regsetNumber[i]));

    regsetNAvailVec = getRegsetNAvail();

    bitMapVec = new BitMapSet[nRegsets];
    for (int i = 0; i < nRegsets; i++)
      bitMapVec[i] = new BitMapSet();
    for (int i = 1; i < nRegisters; i++)
      bitMapVec[i].add(i);
    short[][] regsetMap = getRegsetMap();
    for (int i = nRegisters; i < nRegsets; i++) {
      for (int j = 0; j < regsetMap[i-nRegisters].length; j++)
        bitMapVec[i].add(regsetMap[i-nRegisters][j]);
    }

    int[] compAndTbl = getCompAndTbl();
    andTbl = new int[nRegsets * (nRegsets - 1) / 2];
    // decompress
    {
      int k = 0;
      for (int i = 0; i < compAndTbl.length; i++) {
        if (compAndTbl[i] <= 0)
          k = -compAndTbl[i];
        else
          andTbl[k++] = compAndTbl[i];
      }
    }

    int[] compWeightTbl = getCompWeightTbl();
    weightTbl = new char[nRegsets * nRegsets];
    // decompress
    {
      for (int i = 0; i < compWeightTbl.length; i += 2) {
        int k = compWeightTbl[i];
        int n = compWeightTbl[i + 1] / 64;
        int w = compWeightTbl[i + 1] % 64;
        while (n > 0) {
          weightTbl[k++] = (char)w;
          n--;
        }
      }
    }

    regsetTypeTbl = getRegsetTypeTbl();

    // Register required symbols
    addRequired(symTab);
  }


  /** Convert LirNode to physical register number.
   ** <code>node</code> must be the form like (REG) or (SUBREG (REG)). **/
  public int registerIndex(LirNode node) {
    Symbol sym;
    switch (node.opCode) {
    case Op.REG:
      sym = ((LirSymRef)node).symbol;
      break;

    case Op.SUBREG:
      int position = (int)((LirIconst)node.kid(1)).value;
      String parent = ((LirSymRef)node.kid(0)).symbol.name;
      String name = parent + "/" + Type.toString(node.type) + "/" + position;
      sym = symTab.get(name);
      if (sym == null)
        return registerIndex(node.kid(0));
      break;

    default:
      throw new CantHappenException("REG expected");
    }
    return registerIndex(sym);
  }


  /** Convert symbol to register number. **/
  public int registerIndex(Symbol sym) {
    if (sym.id >= symbolRegTbl.length)
      return 0;

    return symbolRegTbl[sym.id];
  }


  /** Convert register number to symbol. **/
  public Symbol registerSymbol(int reg) {
    return regSymbol[reg];
  }


  /** Convert register number to LirNode. **/
  public LirNode registerLir(int reg) {
    Symbol sym = regSymbol[reg];
    String name = sym.name;
    int p = name.indexOf('/');
    if (p >= 0) {
      Symbol parent = symTab.get(name.substring(0, p));
      int q = name.indexOf('/', p + 1);
      if (q >= 0) {
        int pos = Integer.parseInt(name.substring(q + 1));
        int type = sym.type;
        return module.newLir.node
          (Op.SUBREG, type,
           module.newLir.symRef(parent),
           module.newLir.untaggedIconst(typeAddress(), pos));
      }
    }
    return module.newLir.symRef(sym);
  }


  /** Convert register number to visible form. **/
  public String registerToString(int reg) {
    if (reg == 0)
      return "(nullreg)";
    else
      return regSymbol[reg].printName();
  }


  /** Return register set number whose name is <code>name</code>. **/
  public int getRegSet(String name) {
    Integer n = (Integer)regsetTable.get(name);
    if (n == null)
      throw new CantHappenException("Undefined regset: " + name);
    return n.intValue();
  }

  /** Return type of register set. **/
  public int regSetType(int set) {
    return regsetTypeTbl[set];
  }

  /** Return type of register set. **/
  public int getRegSetType(String name) {
    if (name == null)
      throw new Error("arg: null");
    return regsetTypeTbl[getRegSet(name)];
  }


  /** Return bitmap of register set. **/
  public BitMapSet regSetMap(int set) {
    return bitMapVec[set];
  }

  /** Return number of registers available in the register set. **/
  public int nAvail(int set) {
    return regsetNAvailVec[set];
  }

  /** Return an intersection of two register sets. **/
  public int andSet(int setx, int sety) {
    if (setx == sety)
      return setx;
    if (setx < sety) {
      int w = setx;
      setx = sety;
      sety = w;
    }
    return andTbl[setx * (setx - 1) / 2 + sety];
  }
  

  /** Return interference graph weight = number of edges coming from x to y.
   *   Return 0 if two sets do not interfere. **/
  public int igWeight(int setx, int sety) {
    return weightTbl[setx * nRegsets + sety];
  }


  /** Remove registers overlapping with <code>reg</code>
   **  from register set <code>set</code>. **/
  public void removeRegister(BitMapSet set, int reg) {
    if (reg >= nRegisters)
      return;
    set.remove(reg);
    for (int i = 0; i < overlapRegVec[reg].length; i++)
      set.remove(overlapRegVec[reg][i]);
  }


  /** Return vector of registers that overlaps with <code>reg</code>. **/
  public short[] overlapRegs(int reg) { return overlapRegVec[reg]; }


  /** Return true if register <code>reg</code> is covered by
   **  other registers in register set <code>set</code>. **/
  public boolean covered(int reg, NumberSet set) {
    if (reg >= nRegisters)
      return false;
    for (int i = 0; i < superRegVec[reg].length; i++) {
      if (set.contains(superRegVec[reg][i]))
        return true;
    }
    return false;
  }


  /** Return true if register node1 and node2 overlap **/
  public boolean isOverlapped(LirNode node1, LirNode node2) {
    int index1 = registerIndex(node1);
    int index2 = registerIndex(node2);
    if (index1 == 0 || index2 == 0) {
      if (node1.opCode != node2.opCode){
    	if (node1.opCode == Op.SUBREG)
    		node1 = (LirSymRef)node1.kid(0);
    	if (node2.opCode == Op.SUBREG)
    		node2 = (LirSymRef)node2.kid(0);
      }
      return node1 == node2;
    }
    if (index1 == index2)
      return true;
    if (index1 >= nRegisters || index2 >= nRegisters)
      return false;
    for (int i = 0; i < overlapRegVec[index1].length; i++) {
      if (overlapRegVec[index1][i] == index2)
        return true;
    }
    return false;
  }

  /** Return array of subregisters of register <code>reg</code>. **/
  public short[] subRegs(int reg) {
    return subRegVec[reg >= nRegisters ? 0 : reg];
  }

}
