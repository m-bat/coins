/* tpfuncptr4.c  Function pointer
   (K & R. p.149 with call) */

int  printf(char*, ...);
int  i, i1, i2, i3, i4, i5, i6;
int  ia[13];
int  *pi1, *pi2;
char c1, c2, c3, c4, c5;
char *pc1, *pc2, ca[5];

char **argv;           /* Pointer to pointer to char. */
int  (*daytab)[13];    /* Pointer to array[13] of int. */
void *comp();          /* Function returning pointer to void. */
void (*comp2)();       /* Pointer to function returning void. */
char (*(*x())[])();    /* Function returning pointer to array[]
                          of pointer to function returning char. */
char (*(*x2[3])())[5]; /* Array[3] of pointer to function
                          returning pointer to array[5] of char. */

char (*pca5)[5];       /* Pointer to array[5] of char. */
void *pv;

void *comp()           /* Function returning pointer to void. */
{
  pv = &"abc";
  return pv;
}

char (*pfc)();         /* pointer to function returning char. */
char (*apfc[2])();     /* Array of pointer to function
                          returning char. */
char (*(*x())[2])()    /* Function returning pointer to array[2] */
{                      /* of pointer to function returning char. */
  return &apfc;
}

char (*afc())[2];      /* Pointer to function returning  */
int  (*pfi)();         /* Pointer to function returning int. */
int  (*(fpfi()))()     /* Function returning pointer to function. */
{                      /* returning int. */
  return pfi;
}

int  *(*pfpi)();       /* Pointer to function returning
                          pointer to int. */
int  *(*fpfpi())()     /* Function returning pointer to function */
{                      /* returning pointer to int. */
  return pfpi;
}

int  (*(*pfpfpi)())(); /* Pointer to function returning pointer to
                          function returning pointer to int. */

void fvoid()           /* Function returning void. */
{

}

char fchar()           /* Function returning char. */
{
  return 'a';
}

char (*(*pfpchar5)())[5]; /* Pointer to function returning
                             pointer to array[5] of char. */
int  fi1()              /* Function returning int. */
{
  return 1;
}
int  *fpi()             /* Function returning pointer to int. */
{
  return &i1;
}

void sub(void (*pf1)())
{

}
/* SF030620[ */
char (*fpchar5())[5]
{
  return &ca;
}
/* SF030620] */

int
main()
{
  /* SF030620[ */
  pca5 = &ca;
  pfpchar5 = fpchar5;
  /* SF030620] */
  i    = 1;
  c1   = 'b';
  pc1  = &c1;
  argv = &pc1;
  daytab = (int (*)[13])ia;
  /* *comp = fvoid; */ /* Error. Invalid lvalue in assignment */
  comp2 = fvoid;
  comp2 = *fvoid; /* comp2 represents function designator JIS C 6.3.3.2, 6.2.2.1 */
  pfc   = fchar;
  apfc[0] = pfc;
  apfc[1] = pfc;
  c2    = apfc[i]();
  /* SF030620[
  c2    = (*(x())[i])();
  c2    = *pca5[i]; */
  c2    = ((*x())[i])();
  c2    = (*pca5)[i];
  /* SF030620] */
  x2[1] = pfpchar5;
  pca5  = pfpchar5();
  pca5  = x2[1]();
  pfi   = fi1;
  i1    = pfi();
  i2    = (*pfi)();
  i3    = (**pfi)();
  /* fpfi = pfi; */  /* Warning. Assignment from incompatible pointer type. */
  pfpi = fpi;
  pi1  = pfpi();
  i4   = fpfi()();
  i5   = (*fpfi())();
  printf("function pointer test.\n"); /* SF030620 */
  return 0;
}
