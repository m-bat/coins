/* cTest.c: Test to confirm language specifications */

  int g1, g2, g3, g4;
  int a1[20], a2[10][20]; 
  int *pg1, *pg2;
  char c1[100];
  /** struct str1 {int kind; int(*)(int *, int) f;} x; */
  /* Parse error
     No semicolon at end of struct or union
     Data definition has no type or storage class */
  struct str1 {int kind; int val;} x;
 
int func(int pa[], int pi)
{
  int i1, i2;
  pa[0] = 0 ;
  pa[1] = 1 ;
  i1 = 1 ;
  return i1+pa[pi];
}

int main()
{
  int j1, j2, j3;
  int const c3 = 3;

  1;
  j1;
  (1,2,func(a1,2));
  g1 = func(a1, 1);
  pg1 = &g1;
  /** pg2 = &3;  /* invalid lvalue in unary '&' */
  pg2 = &c3; /* Assignment discards `const` from pointer target type */
  g2 = *pg1 + *pg2;
  printf("g1 %d3 g2 %d3 \n", g1, g2);
  j1 = (int)(&c1[0]);
  ((char*)10)[j1] = "abcdef"[2];
  printf("((char*)10)[j1] %c\n", ((char*)10)[j1]);
  if (g1 == 0) {
    /** j2 = a1; /* Assignment makes integer from pointer without a cast */
    j2 = ((int)a1) % sizeof(int); 
    /** 10[j2]="abcdef"[3]; /*Subscripted array is neither array nore pointer*/
    ((int*)10)[j2] = "abcdef"[3];  /** Access=violation exception */
    /** j1 = ((struct {int kind; (int)(*)(int *, int) f;})100).kind; */
     /* Parse error before `(`
        No semicolon at end of struct or union
        Parse error before `)` */
    /** j1 = ((struct str1*)100).kind; */
      /* Request for member `kind` in something not a structure or union */
    j1 = ((struct str1*)100)->kind;
    printf("10[j2] %c\n", ((int*)10)[j2]);
  }
  printf("abcdef[3] %c\n", "abcdef"[3]);
  return 0;
}

