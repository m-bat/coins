/* tparrayPtr1.c:  array pointer */
/*                 corresponding to Java multidimensional array. */
int printf(char*, ...);

int i, j, k, l, m, n;
int x3[10];    /* corresponds to x[5][7][10] in Java */
int *x2[7];
int **x1[5];
int main()
{
  int s;

  i = 1;
  j = 2;
  k = 3;
  x3[3] = 9;
  x2[2] = &x3[0];
  x1[1] = &x2[0];
  s =*(*(x1[1] + 2) + 3);  /* s = x[1][2][3]; in Java */

  printf("*(*(x1[1] + 2) + 3) =  %d \n", s);
  return 0;
} 

