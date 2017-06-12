/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.cfront;

final class SymbolTable {
    private class Entry {
  Entry  next;
  Object key;
  Object value;

  Entry(Object k, Object v, Entry n) {
      next = n;
      key = k;
      value = v;
  }
    }

    private Entry entries[];
    private int count, threshold;
    private SymbolTable parent;

    public void show() {
  for (int i = 0; i < entries.length; ++i) {
      Entry e = entries[i];
      int n = 0;
      while (e != null) {
    ++n;
    e = e.next;
      }

      System.out.print(n + ",");
  }

  System.out.println(".");
    }

    public SymbolTable() { this(101, null); }

    public SymbolTable(SymbolTable _parent) { this(101, _parent); }

    public SymbolTable(int n, SymbolTable _parent) {
  count = 0;
  entries = new Entry[n];
  threshold = n * 3 / 4;
  parent = _parent;
    }

    public SymbolTable getParent() { return parent; }

    public void putInt(String internalRep, int value) {
  put(internalRep, new Integer(value));
    }

    public int getInt(String internalRep) {
  Integer i = (Integer)get(internalRep);
  if (i == null)
      return -1;
  else
      return i.intValue();
    }

    public Object get(String internalRep) {
        if (internalRep != null) {
      Entry ent[] = entries;
      int index = (System.identityHashCode(internalRep) & 0x7FFFFFFF)
    % ent.length;
      for (Entry e = ent[index]; e != null; e = e.next)
    if (e.key == internalRep) {
        return e.value;
    }

  }

  if (parent == null)
      return null;
  else {
      return parent.get(internalRep);
  }
    }

    //##19 BEGIN
    // Search in the current symbol table (do not search parent).
    // If not found, return null.
    public Object getLocal(String internalRep) {
        if (internalRep != null) {
      Entry ent[] = entries;
      int index = (System.identityHashCode(internalRep) & 0x7FFFFFFF)
    % ent.length;
      for (Entry e = ent[index]; e != null; e = e.next)
    if (e.key == internalRep) {
        return e.value;
    }

  }
  return null;
    }
    //##19 END

    public Object put(String internalRep, Object value) {
        if (internalRep == null)
      return null;

  Entry ent[] = entries;
  int hash = System.identityHashCode(internalRep);
  int index = (hash & 0x7FFFFFFF) % ent.length;
  for (Entry e = ent[index]; e != null; e = e.next)
      if (e.key == internalRep) {
    Object old = e.value;
    e.value = value;
    return old;
      }

  if (count >= threshold) {
      rehash();
            ent = entries;
            index = (hash & 0x7FFFFFFF) % ent.length;
  }

  Entry e = new Entry(internalRep, value, ent[index]);
  ent[index] = e;
  ++count;
  return null;
    }

    private void rehash() {
  int oldSize = entries.length;
  Entry oldEnt[] = entries;
  int newSize = (oldSize << 1) + 1;
  Entry newEnt[] = new Entry[newSize];
  threshold = (newSize * 3) >> 2;
  entries = newEnt;
  for (int i = oldSize ; i-- > 0 ;) {
      Entry old = oldEnt[i];
      while (old != null) {
    Entry e = old;
    old = old.next;
    int index = (System.identityHashCode(e.key) & 0x7FFFFFFF)
          % newSize;
    e.next = newEnt[index];
    newEnt[index] = e;
      }
  }
    }
}
