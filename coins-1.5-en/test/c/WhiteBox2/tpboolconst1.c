/* tpboolconst1.c:  boolean constant in conditional exp, etc. * */

int printf(char*, ...);

int main()
{
  int  i, j, k, x;
  i = 0;
  for (;;) {
    i++;
    if (i >= 10)
      break;
  }
  j = 0;
  while (j < 10) {
    j++;
  }
  k = 0;
  while (k++ < 10)
    ;
  x = 1;
  if (0)
    x = 2;
  while (0)
    i++;
  
  printf("%d %d %d %d \n", i, j, k, x ); 
  return 0;
} 

