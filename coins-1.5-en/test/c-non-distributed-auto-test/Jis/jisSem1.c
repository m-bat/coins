/* jisSem1.c : JIS C 5.1.2.3 Program execution pp.8-9 */

#include <stdio.h>

char line[4] = {'1', '2', '3', '\0'};
char buff[100];
int  column;

char getchar1();

int 
main()
{

  char   c1='a', c2=1;
  float  f1, f2=3.14f;
  double d=2.0;
  short  a1, a2, x11, x12, x13, x21, x22, x23, x24, b1, b2;
  int    sum;
  char   *p;
  int    i;

  c1 = c1 + c2;
  printf("c1=\'a\'+1 : %c\n", c1);

  f1 = f2 * d;
  printf("f1 = f2 * d : %f %f \n", f1, f2);

  a1  = -32754;
  b1  = -15;
  x11 = a1 + 32760 + b1 + 5;
  x12 = (((a1 + 32760) + b1) + 5); 
  x13 = ((a1 + b1) + 32765);
  a2  = -17;
  b2  = 12;
  x21 = a2 + 32760 + b2 + 5;
  x22 = ((a2 + 32765) + b2);
  x23 = (a2 + (b2 + 32765));
  printf("x11 = a1 + 32760 + b1 + 5 : %d\n", x11);
  printf("x12 = (((a1 + 32760) + b1) + 5) : %d\n", x12);
  printf("x13 = ((a1 + b1) + 32765) : %d\n", x13);
  printf("x21 = a2 + 32760 + b2 + 5 : %d\n", x21);
  printf("x22 = ((a2 + 32765) + b2) : %d\n", x22);
  printf("x23 = (a2 + (b2 + 32765)) : %d\n", x23);

  p = &buff[0];
  column = 0;
  sum    = 0;
  for (i = 0; i < 3; i++) { 
    sum = sum * 10 - '0' + (*p++ = getchar1());
  }
  printf("sum %d\n", sum);

  p = &buff[0];
  column = 0;
  sum    = 0;
  for (i = 0; i < 3; i++) { 
    sum = (((sum * 10) - '0') + ((*(p++)) = (getchar1())));
  }
  printf("sum %d\n", sum);

  return 0;
}

char
getchar1()
{
  char c = line[column];
  column++;
  return c;
}

