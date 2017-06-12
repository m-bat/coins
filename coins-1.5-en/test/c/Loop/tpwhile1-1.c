/* tpwhile1-1.c:  while test (basic) */

int printf(char*, ...);

int a[10];
int i, s;
int main()
{
  a[0] = 0;
  i = 1;
  s = 0;
  while (i < 10) {
    a[i] = a[i-1] + i;
    s = s + a[i];
    i = i + 1;
  }
  printf("s %d \n", s);
  return 0;
} 

