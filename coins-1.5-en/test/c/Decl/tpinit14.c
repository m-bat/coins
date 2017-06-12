/* tpinit14.c: Use of variable in initiation */ 

int printf(char*, ...);

int main()
{
  int a = 10, c = 3;
  c = 20;
  int b = a;
  int sum = c;  /* Coins:3, gcc:20 */
  printf("%d %d\n", b, sum);
  return 0;
}

