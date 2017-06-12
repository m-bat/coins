int printf(char *, ...);

struct struct1{
  int i1;
  long l1;
  short s1;
  char c1;
};

int main(int args, char *argv[]){
  struct struct1 str1;

  str1.i1 = 1;
  str1.l1 = 2;
  str1.s1 = 3;
  str1.c1 = 4;

  printf( "before: i1=%d l1=%d s1=%d c1=%d\n", str1.i1, str1.l1, str1.s1, str1.c1);

  str1.i1 = 5;
  str1.l1 = 6;
  str1.s1 = 7;
  str1.c1 = 8;

  printf( "after: i1=%d l1=%d s1=%d c1=%d\n", str1.i1, str1.l1, str1.s1, str1.c1);

  return 0;
}