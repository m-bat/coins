/* tpinit3-1.c  Initial value (nested array, nested structure) */
/* (Decl) */
int a[2][3] = {{0, 1, 2}, {3, 4, 5}}, b, c;
 struct str1 { int e1; int e2; int e3; };
 struct strx { int e1; struct str1 st2; int e5; } stx1 = { 1, {2, 3, 4}, 5};
int d, e = 2;
main()
{
  struct strx strv2 = { 7, {8, 9}, 10 };
  int i = 0;
   
  a[d][e] = i + e;
  b = stx1.st2.e1;
  /* SF030620[ */
  printf("a[%d][%d]=%d b=%d \n",d,e,a[d][e],stx1.st2.e1);
  printf("return != 0 -> 'pointer to integer w/o cast'\n");

  return a[0][0];
  /* SF030620] */
  
}

