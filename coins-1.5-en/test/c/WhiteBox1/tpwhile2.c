/* tpwhile2.c:  while test (bassic) */

int printf( char*, ...);

int main()
{
  int a[10];
  int i, j, s1, s2;

  s1 = 0;
  s2 = 0;
  a[0] = 0;
  i = 0;
  while (i++ < 10) {
    a[i] = a[i-1] + i;
    s1 = s1 + a[i];
  }
  j = i;
  while (--j >= 0) {
    s2 = s2 + a[j];
  }
  printf(" s1 %d s2 %d \n", s1, s2);
  return 0;
} 

