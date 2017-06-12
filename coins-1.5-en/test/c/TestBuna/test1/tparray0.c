/* tparray0.c:  basic array */
int printf(char *s, ...);

int i, x[10], j;
int y[20];
int main()
{
  i = 1;
  j = i - 1;
  x[0] = 3;
  x[j] = x[j] + 1;
  y[i] = x[j]*4;
  x[i+1] = y[i];
  printf("x[i+1] = %d\n", x[i+1]);
  return 0;
} 

