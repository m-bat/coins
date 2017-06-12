/* tpdfa3.c  Loop with branch */
int printf(char *s, ...);

int i = 0, j = 1, k, m, n;
int a, b, c, d;

int main() 
{
  m = 5;
  b = 1;
  c = m + b;
  k = j + 1;
  a = b + c;
  while (i < 10) {
    if (j > 5) {
      n = 6;
      k = j + 1;
    }else {
      j = j + 1;
      m = m - n;
      d = b + c;
    }
    i++;
  } 
  a = b + c;
  d = d + a;
  printf("d = %d\n", d);
  return d;
}

