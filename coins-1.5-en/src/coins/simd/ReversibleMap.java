/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.simd;

import java.util.HashMap;
import java.util.Set;

public class ReversibleMap {
  HashMap forward=new HashMap();
  HashMap backward=new HashMap();

  public void put(Object k, Object v) {
    forward.put(k, v);
    backward.put(v, k);
  }
  public Object get(Object k) {
    return forward.get(k);
  }
  public Object revget(Object k) {
    return backward.get(k);
  }
  public Set dom() { return forward.keySet(); }
  public Set entries() { return forward.entrySet(); }
}
