/* tpcse3.c  Common subexpression elimination test (array elements) */

int printf(char*, ...);

int ga[10];
int gj = 3, gk = 4;
int main()
{
  int la[10];
  int i, j, k, s, t, u;
  for (i = 0; i < 10; i++) {
    ga[i] = i * i;
    la[i] = i * i + i;
  }
  j = 3;
  k = 4;
  s = (ga[j] + ga[k]) + (la[gj] + la[gk]);
  s = s + (ga[j] + ga[k]) + (la[gj] + la[gk]);
  t = ga[j+1] + la[gj+1] + ga[j+1] + la[gj+1];
  printf("s=%d t=%d\n", s, t);
  s = t + (ga[j] + ga[k]) + (la[gj] + la[gk]);
  s = s + (ga[j] + ga[k]) + (la[gj] + la[gk]);
  t = s + ga[j+1] + la[gj+1] + ga[j+1] + la[gj+1];
  printf("s=%d t=%d\n", s, t);
  return 0;
}

