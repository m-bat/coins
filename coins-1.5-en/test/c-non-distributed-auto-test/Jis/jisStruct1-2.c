/* jisStruct1-2.c : Simplification 2 of jisStruct1.c  */

int printf(char *, ...);

union {
  struct {
    int alltypes;
  } n;
  struct {
    int type;
    int intnode;
  } ni;
  struct {
    int type;
    double doublenode;
  } nf;
} u;

int 
main()
{
  double vd;
  int    vi;

  u.nf.type = 1;
  u.nf.doublenode = 3.14;
  if (u.n.alltypes == 1)
    vd = 1.4142/2.0; 
  printf("sin %e \n", vd);
  u.ni.type = 0;
  u.ni.intnode = 99;
  if (u.n.alltypes == 0)
    vi = u.ni.intnode;
  printf("int %d \n", vi);
  return 0;
}

