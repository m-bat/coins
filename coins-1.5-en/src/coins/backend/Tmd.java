/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.backend;

import java.lang.String;
import java.io.IOException;
import java.io.Writer;

/** Interface for Scheme code generator. **/
public interface Tmd {

  public void init(String tmdfile) throws IOException;

  public String params();
  
  public String params(String sym);
  
  public String restructure(String lfunction);

  public String instsel(String lfunction);

  public void asmout(String lmodule, Writer out);

  public Object evals(String sexp);

}
