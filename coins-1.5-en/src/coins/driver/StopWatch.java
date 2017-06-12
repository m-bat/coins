/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import java.util.Date;

/**
 *
 * A stop-watch.<br>
 *
 * This stop-watch is not moving when created, and starts when its start
 * button is pushed.  Its hand keeps moving until the stop button is pushd.
 * You can read the time ever accumulated by this stop-watch, by mili-second
 * order, whether it is moving or not.  After the stop-watch is started and
 * stopped, it can be restarted by pushing its start button again.
 **/

public class StopWatch {
  Date startedTime;
  Date stoppedTime;
  long cumulativeMiliSeconds;
  boolean moving;

  /**
   *
   * Creates a new stop-watch, whose hand is heading zero mili-second and not
   * moving.
   **/
  public StopWatch() {
    startedTime = null;
    stoppedTime = null;
    cumulativeMiliSeconds = 0;
    moving = false;
  }

  /**
   *
   * Starts moving this stop-watch.
   *
   * @return The time this stop-watch was started, if it was already moving.
   * <tt>null</tt>, otherwise.
   **/
  public synchronized Date start() {
    if (moving) {
      return startedTime;
    } else {
      moving = true;
      startedTime = new Date();
      return null;
    }
  }

  /**
   *
   * Stops moving this stop-watch.
   *
   * @return The time this stop-watch was stopped, if it was already stopped.
   * <tt>null</tt>, otherwise.
   **/
  public synchronized Date stop() {
    if (moving) {
      stoppedTime = new Date();
      cumulativeMiliSeconds += stoppedTime.getTime() - startedTime.getTime();
      moving = false;
      return null;
    } else {
      return stoppedTime;
    }
  }

  /**
   *
   * Reads this stop-watch, whether it is moving or not.
   *
   * @return the mili-seconds ever accumulated by this stop-watch.
   **/
  public synchronized long read() {
    if (moving) {
      return
	cumulativeMiliSeconds + new Date().getTime() - startedTime.getTime();
    } else {
      return cumulativeMiliSeconds;
    }
  }

  /**
   *
   * Tests if this stop-watch is moving or not.
   *
   * @return <tt>true</tt> if it is moving.  <tt>false</tt>, otherwise.
   **/
  public synchronized boolean isMoving() {
    return moving;
  }

  /**
   *
   * Resets this stop-watch.  The hand goes back to zero mili-second and stops
   * moving regardless of the current status.
   *
   * @return the cumulative mili-seconds before resetting.
   **/
  public synchronized long reset() {
    long ret = read();
    startedTime = null;
    stoppedTime = null;
    cumulativeMiliSeconds = 0;
    moving = false;
    return ret;
  }
}
