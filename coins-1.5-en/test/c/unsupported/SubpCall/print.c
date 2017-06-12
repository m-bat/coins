#include <stdio.h>

void PrintInt(int n)
{
  printf("%d ", n);
}
void PrintIntInt(int n, int m)
{
  printf("%d, %d ", n, m);
}
void PrintNewLine()
{
  printf("\n");
}
void printInt(int n)
{
  printf("%d ", n);
}
void printIntInt(int n, int m)
{
  printf("%d, %d ", n, m);
}
void printFloat2(float pf1, float pf2)
{
  printf("%f, %f ", pf1, pf2);
}
void printDouble2(double pd1, double pd2)
{
  printf("%f, %f ", pd1, pd2);
}
void printStr(char* p)
{
  printf("%s", p);
}
void printChar(char p)
{
  printf("%c", p);
}
void printEol()
{
  printf("\n");
}
void print(int n)
{
  printf(" %d \n", n);
}
int Random(void)
{
  return (random() % 256);
}
/* SF030609[ */
main()
{
  PrintInt(123);
  PrintIntInt(234,345);
  PrintNewLine();
  printInt(456);
  printIntInt(567,678);
  printFloat2(12.34f,23.45f);
  printDouble2(34.56,45.67);
  printStr("abc");
  printChar('a');
  printEol();
  print(789);
  printf("random = %d\n",Random());
}
/* SF030609] */
