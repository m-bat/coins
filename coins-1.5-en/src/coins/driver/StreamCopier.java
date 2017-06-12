/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.driver;

import coins.FatalError;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A thread to copy data from an incoming stream to an outgoing stream.
 **/
public class StreamCopier extends Thread {
  InputStream fIn;
  OutputStream fOut;

  /**
   *
   * Constructs a copying thread instance.
   *
   * @param in an incoming stream
   * @param out an outgoing stream
   **/
  public StreamCopier(InputStream in, OutputStream out) {
    fIn = in;
    fOut = out;
  }

  private void copyInput(InputStream in, OutputStream out)
    throws IOException {
    if (fIn != null) {
      byte[] buffer = new byte[4096];
      int len;
      while ((len = fIn.read(buffer)) > 0) {
	if (fOut != null) {
	  fOut.write(buffer, 0, len);
	}
      }
    }
  }

  /**
   * Starts copying.
   **/
  public void run() {
    try {
      copyInput(fIn, fOut);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      throw new FatalError(e.getMessage());
    }
  }

  /**
   * Starts copying and waits to finish.
   **/
  public void go() {
    try {
      start();
      join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      throw new FatalError(e.getMessage());
    }
  }
}
