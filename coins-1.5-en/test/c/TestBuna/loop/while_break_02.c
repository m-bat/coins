/* while_break_02.c   simple while statement  */
/* unlimited while loop */

int printf(char *s, ...);

int main()
{
  int n = 10;
  while (1) {
    printf("n = %d\n", n);
    n--;
    if (n == 0)
      break;
  }
  return 0;
} 

