/* Decl/tpextern1.c  extern and static. See JIS-C 6.7.2 (Decl) */

int i1=1;
static int i2=2;
extern int i3=3; /* gcc: warning "initialized and extern". */
int i4;
static int i5;
int i1;
/* int i2; */ /* Execution undef. gcc: "conflicting declaration". */
int i2; /* SF030609 */
int i3;
int i4;
/* int i5; */ /* Execution undef. gcc: "conflicting declaration". */
int i5; /* SF030609 */
extern int i1;
extern int i2;
extern int i3;
extern int i4;
extern int i5;

int
main()
{
  int a;
  i4 = 4;
  i5 = 5;
  a = i1 + i2 + i3 + i4 + i5;
  printf("This test will cause 'conflicting declarations' error."); /* SF030609 */
  return a;
}

