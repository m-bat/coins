/* tpinit3-1.c  Initial value (nested array, nested structure) */
/* (Decl) */
int a[2][3] = {{0, 1, 2}, {3, 4, 5}}, b, c;
 struct str1 { int e1; int e2; int e3; };
 struct strx { int e1; struct str1 st2; int e5; } stx1 = { 1, {2, 3, 4}, 5};
/*int d, e = 2; /* SF030609 */
int d = 1, e = 2; /* SF030609 */
main()
{
  struct strx strv2 = { 7, {8, 9}, 10 };
  int i = 0;

  a[d][e] = i + e;
  b = stx1.st2.e1;
  /* SF030609[ */
  printf("a = {{%d,%d,%d},{%d,%d,%d}}\n",
         a[0][0],a[0][1],a[0][2],a[1][0],a[1][1],a[1][2]);
  printf("stx1 = {%d,{%d,%d,%d},%d}\n",
         stx1.e1,stx1.st2.e1,stx1.st2.e2,stx1.st2.e3,stx1.e5);
  printf("b,d,e = %d,%d,%d\n",b,d,e);
  /* SF030609] */
  return a;
}

