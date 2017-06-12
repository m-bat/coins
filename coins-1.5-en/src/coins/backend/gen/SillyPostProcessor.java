/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend.gen;

import java.io.*;

/** Silly Post-processor **/
public class SillyPostProcessor extends Thread {
  OutputStream finalOut;
  PipedOutputStream out;
  PipedInputStream stream;

  private SillyPostProcessor(String name, OutputStream finalOut) {
    super(name);
    try {
      this.out = new PipedOutputStream();
      this.stream = new PipedInputStream(out);
      this.finalOut = finalOut;
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }

  public OutputStream pipeTo() { return out; }

  public void run() {
    try {
      BufferedReader rdr = new BufferedReader(new InputStreamReader(stream));
      PrintWriter wrt = new PrintWriter(finalOut);
      String line;
      int lineno = 0;
      System.out.println("thread started");
      while ((line = rdr.readLine()) != null) {
        wrt.println(line + " # " + lineno);
        lineno++;
      }
      wrt.close();
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }


  public static SillyPostProcessor postProcessor(OutputStream finalOut) {
    SillyPostProcessor thr = new SillyPostProcessor("Sample", finalOut);
    thr.start();
    return thr;
  }

  public void notifyEnd() {
    try {
      out.close();
      join();
    } catch (IOException e) {
      throw new Error(e.getMessage());
    } catch (InterruptedException e) {
      throw new Error(e.getMessage());
    }
  }

}
