/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.ffront;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import coins.IoRoot;

public class F77Scanner extends Scanner{

  /**
    Constructer : Wrapper of Scanner class

   */
  public F77Scanner(java.io.Reader in, IoRoot io)
    throws java.io.IOException,Exception{
    
    super(in);
    io_ = io;
    ByteArrayInputStream bais = new ByteArrayInputStream(f77pp(in));
    yyreset(new java.io.InputStreamReader(bais));
  }
  
  public F77Scanner(java.io.InputStream in, IoRoot io)
    throws java.io.IOException,Exception{
    
    this(new java.io.InputStreamReader(in), io);
  }

  IoRoot io_;
  private int fLineNum;
  private String fLineLabel;

  protected void dp(String msg){
    io_.dbgToHir.print(2,";;" + msg + "\n");
  }
  
  /**
    preprocessor of Fortran 77 file
   */
  protected byte []f77pp(java.io.Reader rin) throws java.io.IOException,Exception{
    ByteArrayOutputStream baout = new ByteArrayOutputStream();
    BufferedWriter out          = new BufferedWriter(new OutputStreamWriter(baout));
    BufferedReader in           = new BufferedReader(rin);
    String line                 = "";
    int line_no                 = 0;
    
    fLineNum = 0;
    fLastSkipNum = 0;

    try{
      while((line = get_next_fortran_line(in)) != null){
        String l = set_syntax(line);
        line_no ++;
        while(line_no < fLineNum){
          line_no++;
          out.newLine();
          dp("EF(" + fLineNum + "): ");
        }
        
        out.write(l);
        out.newLine();
        dp("EF(" + fLineNum + "): " + l);
      }
    }
    catch(Exception e){
      System.out.println("Error is occur at " + fLineNum + ": " + line);
      e.printStackTrace();
      throw e;
    }
    // close file
    out.close();
    return baout.toByteArray();
  }
  
  private String fLastLine = null;
  private String fLastLabel= null;
  private int fLastSkipNum;
  
  String get_next_fortran_line(BufferedReader in)
  throws java.io.IOException, Exception{
    String line;
    boolean cont = false;
    boolean first= true;
    
    int pos, endpos;
    String fline = "";
        
    while(true){
      if(fLastLine != null){
        line = fLastLine;
        fLastLine = null;
      }
      else{
        if((line = in.readLine()) == null){
          break;
        }
      }
      
      // Skip comment or null line
      if(is_comment_line(line)){
        fLastSkipNum ++;
        continue; 
      }
      
      pos = get_line_label_and_pos(line);

      if(pos != 5 || !is_continue_line(line)){
        if(first == false){
          fLastLine = line;
          break;
        }
      }
      else{
        /* continue line */
        cont = true;
        if(fLastLabel.length() > 0){
          throw new Exception("Label invalid with continuation line indicator :");  
        }
      }
      
      if(pos == 5){
        pos ++; 
      }
      
      endpos = line.length();
      if(endpos > 72){
        endpos = 72; 
      }
      fline += line.substring(pos, endpos);
      first = false;
      fLineNum += fLastSkipNum + 1;
      fLastSkipNum = 0;
      if(cont == false){
        fLineLabel = fLastLabel;
      }
    }
    if(fline.length() > 0){
      return fline;
    }
    else{
      return null;
    }
  }
  
  private boolean is_comment_line(String line){
    if(line.length() <= 0){
      return true; 
    }
    else{
      char c = line.charAt(0);
      if(c == ' ' || c == '\t'){
        int i;
        for(i=1; i<line.length(); i++){
          c = line.charAt(i);
          if(c != ' ' && c != '\t'){
            return false; 
          }
        }
        return true;
      }
      else if(Character.isDigit(c)){
        return false;
      }
      else{
        return true;
      }
    }
  }
  
  private boolean is_continue_line(String line){
    if(line.length() > 6){
      char c = line.charAt(5);
      if(c != ' ' && c != '0'){
        return true;
      }
    }
    return false;
  }
  
  private int get_line_label_and_pos(String line)
  throws Exception{
    int i;
    String label = "";
    
    for(i=0;i<5 && line.length() > i;i++){
      char c = line.charAt(i);
      if(Character.isDigit(c)){
        label += c;
      }
      else if(c == '\t'){
        break; 
      }
      else if(c != ' '){
        throw new Exception("Non-numeric character in label field :"+c);        
      }
    }
    if(label.length() > 0){
      fLastLabel = "" + Integer.parseInt(label); 
    }
    else{
      fLastLabel = ""; 
    }
    return i;
  }
  	
  
  final String reserved_words_of_types[] = {
    "INTEGER",
    "REAL",
    "DOUBLEPRECISION",
    "COMPLEX",
    "LOGICAL",
    "CHARACTER",
    "DIMENSION",
  };
  final String reserved_words_use_with_types[] = {
    "FUNCTION",
  };
  
  final String reserved_words[] = {
    "BLOCKDATA",
    "COMMON",
    "DATA",
    "EQUIVALENCE",
    "EXTERNAL",
    "FORMAT",
    "IMPLICIT",
    "SAVE",
    "INTRINSIC",
    "PROGRAM",
    "FUNCTION",
    "SUBROUTINE",
    "RETURN",
    "PARAMETER",
    "ENTRY",
    "CALL",
    "CONTINUE",
    "STOP",     // add
    "PAUSE",
    "ELSEIF",   // 
    "ELSE",
    "ENDIF",
    "ENDDO",
    "END",      // must be here
  };
  final String reserved_words_io[] = {
    "READ",
    "WRITE",
    "PRINT",
    "BACKSPACE",
    "ENDFILE",
    "REWIND",
    "OPEN",
    "CLOSE",
    "INQUIRE",
  };

  // don't use
  final String reserved_words2[] = {
    "ASSIGN",
    "IF",
    "DO",
    "GOTO",
  };

  /**
    N=X or ASSIGN(X=Y)
    '=' is not surrounded by paren
    (X=Y) is not assign statement
   */
  protected String is_assign_stmt(String str) throws Exception{
    int kakko = 0;
    boolean in_str = false;
    
    for(int i=0;i<str.length();i++){
      if((str.charAt(i) == '=') && (in_str == false)){
        if(kakko == 0){
          return "_let " + str;
        }
        else{
          return null;
        }
      }
      else if(str.charAt(i) == '\''){
        in_str = !in_str;
      }
      else if((str.charAt(i) == '(') && (in_str == false)){
        kakko++;
      }
      else if((str.charAt(i) == ')') && (in_str == false)){
        kakko--;
      }
    }
    // ASSIGN ?
    if(str.startsWith("ASSIGN")){
      int i;
      String num = "";
      for(i=6;i<str.length();i++){
        char c = str.charAt(i);
        if(Character.isDigit(c)){
          num += c;
        }
        else{
          break;
        }
      }
      if(str.substring(i).startsWith("TO") == false){
          throw new Exception("assign statement error");
      }
      return "_ASSIGN " + num + " _TO " + str.substring(i+2);
    }
    return null;
  }

  /**
    ex)
    TYPE INTEGER X,Y
    TYPE INTEGER FUNCTION X()
    TYPE *num FUNCTION X(..)
   */
  protected String is_typed_stmt(String str){
    String ret = null;
    StringBuffer mod = null;
    
    for(int i=0;i<reserved_words_of_types.length;i++){
      if(str.startsWith(reserved_words_of_types[i])){
        String type = reserved_words_of_types[i];
        int k;
        for(k=type.length();
            Character.isDigit(str.charAt(k)) ||
            str.charAt(k) == '*';
            k++){
          if(mod == null){
            mod = new StringBuffer();
          }
          mod.append(str.charAt(k));
        }
        str = str.substring(k);

        // System.out.println(str);
        
        for(int j=0;j<reserved_words_use_with_types.length;j++){
          if(str.startsWith(reserved_words_use_with_types[j])){
            str = "_" + reserved_words_use_with_types[j] + " " +
                  str.substring(reserved_words_use_with_types[j].length());
          }
        }
        ret = "_" + type + ' ' + (mod == null ? "" : mod.toString() + " ") + str;
        break;
      }
    }
    // System.out.println(ret);
    return ret;
  }

  /**

   */
  protected String is_stmt(String str){
    String ret = null;
    String words[];

    for(int t=0;t<2;t++){
      words = t == 0 ? reserved_words_io : reserved_words;
      for(int i=0;i<words.length;i++){
        // System.out.println(str);
        
        if(str.startsWith(words[i])){
          // find keyword
          return "_" + words[i] + " " + str.substring(words[i].length());
        }
      }
    }
    
    return ret;
  }


  /**
    def(ghi)jk)
              ^ return this position
   */
  protected int get_close_paren_pos(String str) throws Exception{
    int i,n=1;
    for(i=0;i<str.length();i++){
      char c = str.charAt(i);
      if(c == '('){
        n++;
      }
      else if(c == ')'){
        n--;
        if(n==0){
          return i+1;
        }
      }
    }
    if(n != 0){
      // error
      throw new Exception("paren is not closed");
    }
    return 0;
  }

  
  /**
    
   */
  protected String is_arith_if(String str) throws Exception{
    int i,n=0;
    if(!str.startsWith("IF(")){
      return null;
    }
    for(i=get_close_paren_pos(str.substring(3))+3;i<str.length();i++){
      char c = str.charAt(i);
      if(Character.isDigit(c)){
        //
      }
      else if(c == ','){
        n++;
      }
      else{
        return null;
      }
    }
    if(n == 2){
      return "_ARITH_IF " + str.substring(2);
    }
    else{
      return null;
    }
  }
  
  /**
    block if => IF (cond) THEN
    arith if => IF (cond) L1,L2,L3
    logic if => IF (cond) exp

    goto if
   */
  protected String is_if_stmt(String str) throws Exception{
    String ret = null;
    if((ret = is_arith_if(str)) != null){
      return ret;
    }
    if(str.startsWith("IF(")){
      int i = get_close_paren_pos(str.substring(3));
      String sub = str.substring(i+3);
      
      // if(cond)GOTO...
      if((ret = is_goto_stmt(sub)) != null){
        return "_" + str.substring(0,i+3) + ret;
      }
      // if(cond)X=Y
      if((ret = is_assign_stmt(sub)) != null){
        return "_" + str.substring(0,i+3) + ret;
      }
      // if(cond) I/O stmt / if(cond) other stmt
      if((ret = is_stmt(sub)) != null){
        return "_" + str.substring(0,i+3) + ret;
      }
      // if(cond) if(cond) a,b,c # arith if
      if((ret = is_arith_if(sub)) != null){
        return "_" + str.substring(0,i+3) + ret;
      }
      
      
      // search keyword "THEN"
      if(str.lastIndexOf("THEN",str.length() - 4) != -1){
        // if stmt is terminated by "THEN", keyword "THEN" change to "_THEN"
        str = "_" + str.substring(0,str.length() - 4) + "_THEN";
        // System.out.println(str);
      }
      return str;
    }
    else if(str.startsWith("ELSEIF") && str.lastIndexOf("THEN",str.length() - 4) != -1){
      return "_" + str.substring(0,str.length() - 4) + "_THEN";
    }
    return null;
  }


  /**
    GOTO         => GOTO N
    COMP_GOTO    => GOTO (L,M,N) T
    ASSIGN_GOTO  => GOTO NAME (L,M,N)
   */
  protected String is_goto_stmt(String str){
    String ret = null;
    if(str.startsWith("GOTO")){
      char c = str.charAt(4);
      if(Character.isDigit(c)){
        return "_GOTO " + str.substring(4);
      }
      else if(c == '('){
        return "_COMP_GOTO " + str.substring(4);
      }
      else{
        return "_ASSIGN_GOTO " + str.substring(4);
      }
    }
    return ret;
  }

  /**
    DO L,cond,step
   */
  protected String is_do_stmt(String str){
    if(str.startsWith("DO")){
      String num = "";
      boolean eq = false;
      boolean ch = false;
      
      int i;
      for(i=2;i<str.length();i++){
        char c = str.charAt(i);
        // System.out.println("***" + i + "," + c);
        if(Character.isDigit(c)){
          if(ch == false){
            num += c;
          }
        }
        else if(Character.isLowerCase(c) || Character.isUpperCase(c)){
          // ok
          ch = true;
        }
        else if(c == '='){
          eq = true;
        }
        else if(c == ','){
          if(eq){
            // it's 'do' stmt
            return "_DO " + num + " " + str.substring(2+num.length());
          }
        }
      }
    }
    return null;
  }

  protected String erase_whitespaces(String str){
    // first , erace space
    boolean in_str = false;
    int n=0, i;
    StringBuffer buff = new StringBuffer();
    
    for(i=0;i<str.length();i++){
      if(str.charAt(i) == '!' && in_str == false){
        // say warning
        // 
        break;
      }
      if(str.charAt(i) == '\''){
        in_str = in_str ? false : true;
      }

      //
      char c = str.charAt(i);
      if(Character.isLowerCase(c) && in_str == false){
        buff.append(Character.toUpperCase(c));
      }
      else if(Character.isWhitespace(c) == false || in_str){
        buff.append(c);
      }
    }

    // format(...) ?
    String ret = buff.toString();
    if(ret.startsWith("FORMAT(")){
      // format(...)
      for(i=0, n=0; n < "FORMAT(".length(); i++){
        if(Character.isWhitespace(str.charAt(i))){
          //
        }
        else{
          n++;
        }
      }
      return "FORMAT(" + str.substring(i);
    }
    return ret;
  }

  protected String set_syntax(String str)
  throws Exception{
    String label = fLineLabel;
    
    // make label
    if(label.length() > 0){
      label = '@' + label + ' ';
    }
    else{
      label = "# ";
    }

    // first , erace space
    boolean in_str = false;
    str = erase_whitespaces(str);
    
    // if this is empty, return  empty string
    if(str.length() == 0){
      return "";
    }
    // System.out.println(str);
    
    //
    String ret = null;
    if(
      (ret = is_do_stmt(str))    != null ||
      (ret = is_if_stmt(str))    != null ||
      (ret = is_assign_stmt(str))!= null ||
      (ret = is_typed_stmt(str)) != null ||
      (ret = is_stmt(str))       != null ||
      (ret = is_goto_stmt(str))  != null ){
      str = ret;
    }
    else{
      System.out.println('[' + str  + ']');
      throw new Exception("can't solve stmt");
    }

    // no need label with these statement
    if(
       // str.startsWith("_ENDIF")  || /* endif can have line number */
      str.startsWith("_ELSEIF") ||
      str.startsWith("_ELSE")){
      label = "";
    }
    if(str.startsWith("_ENDIF") && label != "#"){
      
      return "_ENDIF; " + label + " _CONTINUE;";
    }
    else{
      return label + str + ";";
    }
  }
    
  public static void main(String str[])
    throws
          java.io.FileNotFoundException,java.io.IOException,
          Exception
    {
    if(str.length > 0){
      F77Scanner fs = new F77Scanner(new FileReader(str[0]), null);
    }
  }
}




