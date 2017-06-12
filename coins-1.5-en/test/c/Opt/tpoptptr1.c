/* tpoptptr1.c:  Pointer check optimization */

#include <stdio.h> /* SF030620 */

char  *s1, *s2;
int   x;
int   f(char* p1);
/*#define NULL 0 */

int main()
{
  s1 = "abc";
  x = f(s1);
  printf("\t x=%d\n",x);
  s2 = s1 + 1; 
  x = f(s2);
  printf("\t x=%d\n",x);
  return 0;
} 
int f(char* p1)
{
  int   a1, a2;

  if (p1 != NULL) {
    a1 = (int)(*p1);
    if (p1 != NULL)
      a2 = *p1;
    else
      a2 = 0;
  }else {
    a1 = 1;
    if (p1 == NULL)
      a2 = *p1;
    else
      a2 = f(p1);
  }
  printf("return a1 + a2=%d",a1+a2);
  return a1 + a2;
}
  

