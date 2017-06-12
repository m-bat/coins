/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.lir;

import coins.backend.CantHappenException;
import coins.backend.Debug;
import coins.backend.Op;
import coins.backend.Type;
import coins.backend.sym.Label;
import coins.backend.util.ImList;
import coins.backend.util.QuotedString;
import java.lang.UnsupportedOperationException;
import java.util.Iterator;

/**
 * New LIR node's basic structure
 *  
 */
public abstract class LirNode {
  /** Key to bind related information */
  public final int id;

  /** Instruction Code */
  public final int opCode;

  /** Type of this node's result */
  public final int type;

  /** Optional arguments list (op opr1 opr2 &hogehoge ...) */
  public final ImList opt;

  // int flags;
  // static final int F_ROOT = 1;
  
  /** Create LIR node */
  LirNode(int id, int opCode, int type, ImList opt) {
    this.id = id;
    this.opCode = opCode;
    this.type = type;
    this.opt = (opt == null) ? ImList.Empty : opt;
  }

  /** Return a deep copy of this node */
  public abstract LirNode makeCopy(LirFactory fac);

  /** Return a shallow copy of this node.
   ** Default action is same as makeCopy. **/
  public LirNode makeShallowCopy(LirFactory fac) { return makeCopy(fac); }

  /** Return a copy of this node without options. **/
  public abstract LirNode replaceOptions(LirFactory fac, ImList newOpt);



  /** Return number of operands
   ** @deprecated use nKids instead. */
  public int nSrcs() { return nKids(); }

  /** Return number of operands */
  public int nKids() { return 0; }

  /** Return nth operand
   ** @deprecated use kid instead. */
  public LirNode src(int n) { return kid(n); }

  /** Return nth operand; Subclass responsibility */
  public LirNode kid(int n) { throw new IllegalArgumentException(); }

  
  /** Set nth operand
   ** @deprecated use setKid instead. */
  public void setSrc(int n, LirNode src) { setKid(n, src); }

  /** Set nth operand; Subclass responsibility */
  public void setKid(int n, LirNode kid) {
    throw new IllegalArgumentException();
  }

  /** Return jump target labels. Targets are packed in an array of Label. */
  public Label[] getTargets() {
    Label[] targets;

    switch (opCode) {
    case Op.JUMP:
      targets = new Label[1];
      targets[0] = ((LirLabelRef)kid(0)).label;
      return targets;

    case Op.JUMPC:
      targets = new Label[2];
      targets[0] = ((LirLabelRef)kid(1)).label;
      targets[1] = ((LirLabelRef)kid(2)).label;
      return targets;

    case Op.JUMPN:
      LirNaryOp cases = (LirNaryOp)kid(1);
      // cases.opCode == Op.LIST
      int n = cases.nKids();
      targets = new Label[n + 1];
      for (int i = 0; i < n; i++)
        targets[i] = ((LirLabelRef)cases.kid(i).kid(1)).label;
      targets[n] = ((LirLabelRef)kid(2)).label;
      return targets;

    case Op.PARALLEL:
      return kid(0).getTargets();

    default:
      return null;
    }
  }


  /** Replace target label y for x. */
  public void replaceLabel(Label x, Label y, LirFactory fac) {
    switch (opCode) {
    case Op.JUMP:
      if (((LirLabelRef)kid(0)).label == x)
        setKid(0, fac.labelRef(y));
      break;

    case Op.JUMPC:
      if (((LirLabelRef)kid(1)).label == x)
        setKid(1, fac.labelRef(y));
      if (((LirLabelRef)kid(2)).label == x)
        setKid(2, fac.labelRef(y));
      break;

    case Op.JUMPN:
      LirNaryOp cases = (LirNaryOp)kid(1);
      int n = cases.kid.length;
      for (int i = 0; i < n; i++) {
        if (((LirLabelRef)cases.kid(i).kid(1)).label == x)
          cases.kid(i).setKid(1, fac.labelRef(y));
      }
      if (((LirLabelRef)kid(2)).label == x)
        setKid(2, fac.labelRef(y));
      break;

    case Op.PARALLEL:
      kid(0).replaceLabel(x, y, fac);
      break;

    default:
      throw new IllegalArgumentException();
    }
  }




  /** Return true if node is a branch instruction. **/
  public boolean isBranch() {
    switch (opCode) {
    case Op.JUMP:
    case Op.JUMPC:
    case Op.JUMPN:
      return true;
    case Op.PARALLEL:
      int n = nKids();
      for (int i = 0; i < n; i++) {
        if (kid(i).isBranch())
          return true;
      }
      /* fall thru */
    default:
      return false;
    }
  }



  /** Return true if node is a physical register.
      (must be overriden by subclasses) **/
  public boolean isPhysicalRegister() {
    if (opCode == Op.SUBREG)
      return kid(0).isPhysicalRegister();
    else
      return false;
  }



  /** Scanner for LIR expression tree. **/
  public static class Scanner implements Iterator {
    private static final int DEFAULTSIZE = 16;

    /* Lir Node last scanned */
    LirNode scanned;

    LirNode[] useBuf;
    LirNode[] defBuf;
    LirNode[] clobberBuf;
    LirNode[] buf;
    int nUses, nDefs, nClobbers;

    int size;
    int ptr;


    public boolean hasNext() {
      return (ptr < size);
    }


    public Object next() {
      if (ptr < size)
        return buf[ptr++];
      else
        return null;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }


    /** Return iterator for enumerating register variable USEs in tree. **/
    public Iterator forUses(LirNode tree) {
      init(tree);
      buf = useBuf;
      size = nUses;
      ptr = 0;
      return this;
    }


    /** Return iterator for enumerating register variable DEFs in tree. **/
    public Iterator forDefs(LirNode tree) {
      init(tree);
      buf = defBuf;
      size = nDefs;
      ptr = 0;
      return this;
    }


    /** Return iterator for enumerating register CLOBBERs in tree. **/
    public Iterator forClobbers(LirNode tree) {
      init(tree);
      buf = clobberBuf;
      size = nClobbers;
      ptr = 0;
      return this;
    }


    private void init(LirNode tree) {
      if (tree != scanned) {
        if (useBuf == null) {
          useBuf = new LirNode[DEFAULTSIZE];
          defBuf = new LirNode[DEFAULTSIZE];
          clobberBuf = new LirNode[DEFAULTSIZE];
        }
        nUses = nDefs = nClobbers = 0;
        scan(tree);
        scanned = tree;
      }
    }


    private void scan(LirNode tree) {
      switch (tree.opCode) {
      case Op.PARALLEL:
        {
          int n = tree.nKids();
          for (int i = n - 1; i >= 0; i--)
            scan(tree.kid(i));
        }
        break;

      case Op.SET:
        scanLvalue(tree.kid(0));
        scan(tree.kid(1));
        break;

      case Op.CLOBBER:
        {
          int n = tree.nKids();
          for (int i = 0; i < n; i++) {
            if (nClobbers >= clobberBuf.length)
              clobberBuf = grow(clobberBuf);
            clobberBuf[nClobbers++] = tree.kid(i);
          }
        }
        break;

      case Op.PROLOGUE:
        {
          int n = tree.nKids();
          for (int i = 1; i < n; i++)
            scanLvalue(tree.kid(i));
        }
        break;

      case Op.CALL:
        {
          // callee and parameters
          scan(tree.kid(0));
          scan(tree.kid(1));
          // return values
          int n = tree.kid(2).nKids();
          for (int i = 0; i < n; i++)
            scanLvalue(tree.kid(2).kid(i));
        }
        break;

      case Op.ASM:
        {
          // in and inout list
          scan(tree.kid(1));
          scan(tree.kid(3));
          // out list
          int n = tree.kid(2).nKids();
          for (int i = 0; i < n; i++)
            scanLvalue(tree.kid(2).kid(i));
          // inout list
          n = tree.kid(3).nKids();
          for (int i = 0; i < n; i++)
            scanLvalue(tree.kid(3).kid(i));
        }
        break;

      case Op.SUBREG:
        if (tree.kid(0).opCode != Op.REG) {
          scan(tree.kid(0));
          break;
        }
      /* fall thru */
      case Op.REG:
        if (nUses >= useBuf.length)
          useBuf = grow(useBuf);
        useBuf[nUses++] = tree;
        break;

      default:
        {
          int n = tree.nKids();
          for (int i = 0; i < n; i++)
            scan(tree.kid(i));
        }
        break;
      }
    }

    private final void scanLvalue(LirNode tree) {
      switch (tree.opCode) {
      case Op.SUBREG:
        if (tree.kid(0).opCode != Op.REG) {
          scan(tree.kid(0));
          break;
        }
      /* fall thru */
      case Op.REG:
        if (nDefs >= defBuf.length)
          defBuf = grow(defBuf);
        defBuf[nDefs++] = tree;
        break;
      default:
        // otherwise r-value (in case of (MEM ...))
        scan(tree);
        break;
      }
    }


    private LirNode[] grow(LirNode[] buf) {
      LirNode[] newBuf = new LirNode[buf.length * 2];
      for (int i = 0; i < buf.length; i++)
        newBuf[i] = buf[i];
      return newBuf;
    }

  }





  /** Is register operand? **/
  public boolean isRegisterOperand() {
    return (opCode == Op.REG
            || opCode == Op.SUBREG && kid(0).opCode == Op.REG);
  }



  /** Pick up uses of register variables in this L-expression.
   ** Picked up variables are passed
   **   thru call-back object <code>receiver</code>. **/
  public final void pickUpUses(PickUpVariable receiver) {
    switch (opCode) {
    case Op.PARALLEL:
      {
        int n = nKids();
        for (int i = n - 1; i >= 0; i--)
          kid(i).pickUpUses(receiver);
      }
      break;
    case Op.SET:
      if (!kid(0).isRegisterOperand())
        kid(0).pickUpUses(receiver);
      kid(1).pickUpUses(receiver);
      break;

    case Op.CLOBBER:
    case Op.PROLOGUE:
      // all operands are defined, not used
      break;

    case Op.CALL:
      // callee and parameters
      kid(0).pickUpUses(receiver);
      kid(1).pickUpUses(receiver);
      // return values
      {
        int n = kid(2).nKids();
        for (int i = 0; i < n; i++) {
          if (kid(2).kid(i).opCode == Op.MEM)
            kid(2).kid(i).kid(0).pickUpUses(receiver);
        }
      }
      break;

    case Op.ASM:
      // in and inout list
      kid(1).pickUpUses(receiver);
      kid(3).pickUpUses(receiver);
      // out list
      {
        int n = kid(2).nKids();
        for (int i = 0; i < n; i++) {
          if (kid(2).kid(i).opCode == Op.MEM)
            kid(2).kid(i).kid(0).pickUpUses(receiver);
        }
        n = kid(3).nKids();
        for (int i = 0; i < n; i++) {
          if (kid(3).kid(i).opCode == Op.MEM)
            kid(3).kid(i).kid(0).pickUpUses(receiver);
        }
      }
      break;

    case Op.SUBREG:
      if (kid(0).opCode != Op.REG) {
        kid(0).pickUpUses(receiver);
        break;
      }
    /* fall thru */
    case Op.REG:
      receiver.meetVar(this);
      break;

    default:
      {
        int n = nKids();
        for (int i = 0; i < n; i++)
          kid(i).pickUpUses(receiver);
      }
      break;
    }
  }


  /** Pick up definitions of register variables in this L-expression.
   ** Picked up variables are passed thru call-back
   **  object <code>receiver</code>. **/
  public final void pickUpDefs(PickUpVariable receiver) {
    switch (opCode) {
    case Op.PARALLEL:
      {
        int n = nKids();
        for (int i = n - 1; i >= 0; i--)
          kid(i).pickUpDefs(receiver);
      }
      break;

    case Op.SET:
      kid(0).pickReg(receiver);
      break;

    case Op.CLOBBER:
      {
        int n = nKids();
        for (int i = 0; i < n; i++)
          kid(i).pickReg(receiver);
      }
      break;

    case Op.CALL:
      {
        // return values
        int n = kid(2).nKids();
        for (int i = 0; i < n; i++)
          kid(2).kid(i).pickReg(receiver);
      }
      break;

    case Op.ASM:
      {
        // out and inout list
        int n = kid(2).nKids();
        for (int i = 0; i < n; i++)
          kid(2).kid(i).pickReg(receiver);
        n = kid(3).nKids();
        for (int i = 0; i < n; i++)
          kid(3).kid(i).pickReg(receiver);
      }
      break;

    case Op.PROLOGUE:
      {
        int n = nKids();
        for (int i = 1; i < n; i++)
          kid(i).pickReg(receiver);
      }
      break;
    }
  }

  private final void pickReg(PickUpVariable receiver) {
    switch (opCode) {
    case Op.SUBREG:
      if (kid(0).opCode != Op.REG)
        break;
    /* fall thru */
    case Op.REG:
      receiver.meetVar(this);
      break;
    }
  }


  /** Convert to external LIR format. **/
  public Object toSexp() {
    if (opCode == Op.DEFLABEL)
      return ImList.list("DEFLABEL", new QuotedString(((LirLabelRef)kid(0)).label.toString()));

    ImList list = ImList.Empty;

    if (opCode != Op.LIST)
      list = new ImList(Op.toName(opCode), list);
    if (type != Type.UNKNOWN)
      list = new ImList(Type.toString(type), list);

    int n = nKids();
    for (int i = 0; i < n; i++)
      list = new ImList(kid(i) == null ? "<null>" : kid(i).toSexp(), list);

    /*
     * if (Debug.showId) {
     *   list = new ImList("&id", list);
     *   list = new ImList(id, list);
     * }
     */
    for (ImList p = opt; !p.atEnd(); p = p.next())
      list = new ImList(p.elem(), list);
    return list.destructiveReverse();
  }
    

  /** Visualize */
  public String toString() {
    if (opCode == Op.DEFLABEL)
      return "(DEFLABEL \"" + ((LirLabelRef)kid(0)).label + "\")";

    StringBuffer buf = new StringBuffer();
    buf.append("(");
    if (opCode == Op.LIST) {
      if (type != Type.UNKNOWN) {
        buf.append(Type.toString(type));
        buf.append(" ");
      }
    } else {
      buf.append(Op.toName(opCode));
      if (type != Type.UNKNOWN) {
        buf.append(Debug.TypePrefix);
        buf.append(Type.toString(type));
      }
      buf.append(" ");
    }
    int n = nKids();
    for (int i = 0; i < n; i++) {
      if (i != 0)
        buf.append(" ");
      buf.append(kid(i) == null ? "<null>" : kid(i).toString());
    }
    if (Debug.showId)
      buf.append(" &id " + id);
    if (!opt.atEnd()) {
      buf.append(" ");
      buf.append(opt.toStringWOParen());
    }
    buf.append(")");
    return buf.toString();
  }

  /** Return hash value of LirNode. **/
  public int hashCode() {
    int n = nKids();
    int v = opCode + type;
    for (int i = 0; i < n; i++)
      v = v * 129 + kid(i).hashCode();
    return v;
  }

  /** Return true if this object equals to x */
  public boolean equals(Object x) {
    return (x instanceof LirNode
            && ((LirNode)x).opCode == opCode && ((LirNode)x).type == type
            && opt.equals(((LirNode)x).opt));
  }

  /** Accept visitor v */
  public abstract void accept(LirVisitor v);

  /** Convert integer or address constant to string.
   ** @deprecated **/
  public String toStringExp() {
    switch (opCode) {
    case Op.ADD:
      return kid(0).toStringExp() + "+" + kid(1).toStringExp();

    case Op.SUB:
      return kid(0).toStringExp() + "-" + kid(1).toStringExp();

    default:
      throw new CantHappenException("not an address expression: " + this);
    }
  }
}
