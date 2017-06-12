/* tpstring7.c:  character string with \, etc.  */

int printf(char*, ...);

int main()
{
  char *s1, *s2, *s3, *s4;
  s1 = "\0";
  s2 = "\\";
  s3 = "\t";
  s4 = "\n";
  printf("0:%s \\%s tab:%s newLine:%s \n", s1, s2, s3, s4);
  s1 = "\a";
  s2 = "\b";
  s3 = "\f";
  s4 = "\r";
  printf("alarm:%s backSpace:%s feed:%s return:%s \n", s1, s2, s3, s4);
  s1 = "\v";
  s2 = "\?";
  s3 = "\'";
  s4 = "\"";
  printf("vtab:%s ?:%s squote:%s dquote:%s \n", s1, s2, s3, s4);
  s1 = "\x00";
  s2 = "\x02";
  s3 = "\x09";
  s4 = "\x0a";
  printf("x00:%s x01:%s x09:%s x0a:%s \n", s1, s2, s3, s4);
  printf("END \n");
  return 0;
} 

