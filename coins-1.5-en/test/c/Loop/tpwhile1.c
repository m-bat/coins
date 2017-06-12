/* tpwhile1.c:  while test (bassic) */

int printf(char*, ...);

int main()
{
  int a[10];
  int i, s;

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

