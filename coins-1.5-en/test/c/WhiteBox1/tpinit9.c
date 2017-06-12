/* tpinit9.c  Initial value for partial elements */
/* (Decl) */
int  x[3][4] = { {1, 2, 3, 4 }, { 5 }, { 6, 7 } };

int f(int i1)
{
  int  a[10] = { 15, 0, 3 };
  return a[i1] + a[i1+1];
}

main()
{
  int i, j, k, sumx;
   
  sumx = 0;
  for (i = 0; i < 3; i++) 
    for (j = 0; j < 4; j++) 
      sumx = sumx + x[i][j];
  k = f(1);
  printf("%d %d \n", sumx, k);
  return 0;
}

