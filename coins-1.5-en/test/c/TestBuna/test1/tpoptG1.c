/*---- tpoptG1.c: Global optimization (integer expressions) ----*/
int printf(char *s, ...);

int a, b, c, d, e, f;

int main()
{
  a = 1; 
  b = 2 + 3;
  c = a + b;
  d = a + 1;
  if (a) {
    c = a + b;
    d = (a + b) + c;
    e = a + 1;
    f = a + b + c;
  }else {
    c = a + 1;
    d = 3 * (a + 1);
    e = a;
    f = e + 1;
  } 
  d = d + 2;
  printf("d = %d\n", d);
  return d + 2;
}
