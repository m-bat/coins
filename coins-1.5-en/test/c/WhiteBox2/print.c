#include <stdio.h>
void print(int pi)
{
  printf("%d ", pi);
}
void printInt(int pi)
{
  printf("%d ", pi);
}
void printStr(char* ps)
{
  printf("%s", ps);
}
void printEol()
{
  printf("\n");
}

int main()
{
  /* SF030609[ */
  print(123);
  printInt(234);
  printStr("345");
  printEol();
  /* SF030609] */
  return 0;
}

