/* tpinit4.c  Initial value without array bound specification */
/* (Decl) */
int x[] = { 11, 12, 13 };
int a[][3] = {{0, 1, 2}, {3, 4, 5}};
main()
{
  int i = 0, b, c, d;
   
  b = x[2];
  c = a[1][2];
  printf("b=%d c=%d\n",b,c); /* SF030620 */
  return 0;
}

