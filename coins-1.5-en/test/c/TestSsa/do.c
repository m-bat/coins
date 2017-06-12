#include<stdio.h>
int main()
{
  
  int a;
  int b;
  int c;

  a = 1;
  b = 2;
  c = 0;

  do {
    c = c + 1;
    if (c > 1) {
      a = b;
      break;
    } else {
      b = a;
      continue;
    }

    if (c > 1) {
      a = b;
    } else {
      b = a;
    }
  } while (c < 3);

  a = a - 1;
  b = b - 1;
  c = c - 1;
  printf("%d %d %d\n",a,b,c); /* Added by M.Takahashi */   
  return(0);
}
