/* parserProb0209 errors of C parser */

int printf(char*, ...);

enum{ January = 1? 1: 0 }; /* bad constant value */
extern f0();               /* no type specifier */
int a[2][2] = {1,2,3,4};   /* excess elements in array initializer */

main()
{
  extern f0();                   /* syntax error */
  float f;
  int a[2][2];
  int i; int *b[] = {&i};        /* invalid initializer */
  long long ll = -1;             /*##*/
  f = f / 2; /* (= f (/ f (<cast i->d> 2))) (cast is unnecessary) */
  printf(" %d\n",sizeof(a[0]));   /* 4 (must be 8) */
  printf(" %d\n",sizeof(int[2])); /* 4 (must be 8) */
  0xFFFFFFFF;                    /* (i 4294967295)  (must be (Ui 4294967295)) */
  printf(" %d\n",0xFFFFFFFF>>31); /* -1 (must be 1) */
  main==0;                       /* bad operands */
  1LL;                           /* ; is missing. */
  ((char*)10)[0];                /* ([] 10 0)  (cast is disregarded) */
  ll<<1;           /* (<< ll (<cast i->j> 1))  (cast is unnecessary) */
  printf(" %d\n", ll);
}

