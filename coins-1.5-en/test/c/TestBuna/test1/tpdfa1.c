/* tpdfa1.c:  Data flow analysis (Dragon book Fig. 10.27 p. 626) */
int printf(char *s, ...);

int i, j, n, a, u1, u2, u3, k, m;
int main()
{

  i = m-1;
  j = n;
  a = u1;
  k = 0;
  while (k < 3) {
    i = i +1;
    j = j -1;
    k = k + 1;
    if (k)
      i = u3;
    else
      a = u2;
  }
  printf("i = %d, a = %d\n", i, a);
  return 0;
}
