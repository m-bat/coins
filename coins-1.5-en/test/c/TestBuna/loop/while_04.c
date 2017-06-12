/* while_04.c   simple while statement  */

int printf(char *s, ...);

int main()
{
  int n = 10;
  while (n--)
    printf("n = %d\n", n);
  return 0;
} 

