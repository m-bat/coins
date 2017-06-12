/* Decl/tpenum4.c  Enumeration literal declaration with constant exp */

enum{ January = 1? 1: 0 }; /* bad constant value */

int main()
{
  printf("%d \n", January);
  return 0;
}

