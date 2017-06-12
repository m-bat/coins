/* tpstring6-1.c:  character string with \  */
/*   Exclude nonportable characters from tpstring6.c */

int printf(char*, ...);

int main()
{
  char *s1, *s2, *s3, *s4;
  char c1;
  s1 = "";
  s2 = "\n";
  s3 = "\0";
  s4 = "tab\t";
  printf("null:%s newLine:%s 0:%s %s\n", s1, s2, s3, s4);
  s1 = "quote\"";
  s2 = "9\013";
  s3 = "\b";
  s4 = "don\'t";
  /* printf("%s 013:%s backspace:%s %s\n", s1, s2, s3, s4); */
  printf("%s 013:%x backspace:%s %s\n", s1, *s2, s3, s4);
  /* s1 = "0\n1\t2\v3\r4\f5\a6\\7\?8\'9\"0"; */
  s1 = "0\n1\t3\r4\f5\a6\\7\?8\'9\"0";
  s2 = "1\0";
  printf("%s %s\n", s1, s2);
  s3 = "0\n1\t2\v3\r4\f5\a6\\7\?8\'9\"0";
  for (s4 = s3; *s4 != 0; s4++) {
    printf(" %2x", *s4);
  }
  printf("\n");
  return 0;
} 

