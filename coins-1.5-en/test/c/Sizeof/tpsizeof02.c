/* tpsizeof02.c */

#include <stdio.h>

char c0[5];
char *c1, *c2, *c3;
int  s1, s2;

char *f(char *p)
{
  return p + sizeof(int)/2;
}

int main()
{
  c0[0] = 'a';
  c0[1] = 'b';
  c0[2] = 'c';
  c0[3] = 'd';
  c1 = "stuvwxyz";
  c2 = f(&c0[0]); 
  c3 = f(c1);
  s1 = sizeof(c1);
  s2 = sizeof(*c1);
  printf("*c1 %c *c2 %c *c3 %c *c1 %s s1 %d s2 %d \n",
          *c1, *c2, *c3, c1, s1, s2); 
  return 0;
}

