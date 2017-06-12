/* tpArrayParam2.c  Array parameter test */
/*   Fujinami mail 040620 */

int a[5] = {0, 1, 2, 3, 4};
int b[2][3] = { {1, 2, 3}, {4, 5, 6}};

int printf(char *, ... );

int f( int (*p)[5], int (*q)[2][3])
{
  int a1, b1;
  a1 = (*p)[0] + (*p)[1] + (*p)[2] + (*p)[3] + (*p)[4]; 
  b1 = (*q)[0][0] + (*q)[0][1] + (*q)[0][2]
     + (*q)[1][0] + (*q)[1][1] + (*q)[1][2];
  printf("%d %d\n", a1, b1);
  return a1 + b1;
}

int main()
{
  int x; 
  x = f(&a, &b);
  printf("%d \n", x);
}
