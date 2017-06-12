/* tpstring5.c:  character string with \  */

int printf(char*, ...);

char name1[]  = "";
char name2[]  = "\n";
char name3[]  = "\0\0";
char name4[5] = "tab\t";

int main()
{
  char *s1, *s2, *s3, *s4;
  s1 = "quote\"";
  s2 = "\013";
  s3 = "\b";
  s4 = "don\'t";
  printf("%s %s %s %s\n", s1, s2, s3, s4);
  printf("%s %s %s %s\n", name1, name2, name3, name4);
  return 0;
} 

