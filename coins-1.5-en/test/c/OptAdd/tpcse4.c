/* tpcse4.c  Common subexpression elimination test (local array elements) */

int printf(char*, ...);

int main()
{
  int la[10], lb[10];
  int i, j, k, s, t, u;
  for (i = 0; i < 10; i++) {
    lb[i] = i * i;
    la[i] = i * i + i;
  }
  j = 3;
  k = 4;
  s = (lb[j] + lb[k]) + (la[j] + la[k]);
  s = s + (lb[j] + lb[k]) + (la[j] + la[k]);
  t = lb[j+1] + la[j+1] + lb[j+1] + la[j+1];
  printf("s=%d t=%d\n", s, t);
  s = t + (lb[j] + lb[k]) + (la[j] + la[k]);
  s = s + (lb[j] + lb[k]) + (la[j] + la[k]);
  t = s + lb[j+1] + la[j+1] + lb[j+1] + la[j+1];
  printf("s=%d t=%d\n", s, t);
  return 0;
}

