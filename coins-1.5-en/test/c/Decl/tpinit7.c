/* tpinit7.c  Initial value for large array */
/* (Decl) */
int  x[100] = { 10, 11, 12, 1 };
char c1[400] = { ' ', 'a', 'b' };

int f(int i1)
{
  int  a[100] = { 15, 0, 3 };
  char c2[400] = { ' ', 'c', 'd' };
  return a[i1] + c2[i1];
}

main()
{
  int y[200] = { 5, 6, 0 };
  int i, k, sumx, sumy;
   
  sumx = 0;
  sumy = 0;
  for (i = 0; i < 100; i++) {
    sumx = sumx + x[i];
    sumy = sumy + y[i];
  }
  k = f(2);
  printf("%d %d %d \n", k, sumx, sumy); /* ##19 */
  return 0;
}

