/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import java.io.PrintWriter;


public class IntervalTimer {

  private long prevTime;

  public IntervalTimer() {
    this.checkPoint();
  }


  /** Return interval time since last call. (in milliseconds) **/
  public long getIntervalTime() {
    long now = System.currentTimeMillis();
    long val = now - prevTime;
    prevTime = now;
    return val;
  }

  /** Return interval time since specified point. (in milliseconds) **/
  public long getIntervalTime(long prev) {
    long now = System.currentTimeMillis();
    long val = now - prev;
    prevTime = now;
    return val;
  }


  /** Return current lap. **/
  public long checkPoint() {
    return prevTime = System.currentTimeMillis();
  }
    
  /** Return current lap. **/
  public long getLaptime() {
    return prevTime = System.currentTimeMillis();
  }

  /** Force GC and report freed memory size. **/
  public void gcReport(PrintWriter out) {
    long oldfree = Runtime.getRuntime().freeMemory();
    System.gc();
    long incr = Runtime.getRuntime().freeMemory() - oldfree;
    out.println(" ...gc(" + incr + "): " + getIntervalTime());
  }

}
