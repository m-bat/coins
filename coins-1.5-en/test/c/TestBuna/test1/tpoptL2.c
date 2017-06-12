/*---- tpoptL2.c: Local optimization (multiple basic blocks) ----*/
int printf(char *s, ...);

int a, b, c, d, e, f;

int main()
{
  a = 1; 
  b = 2 + 3;
  if (a) {
    c = a + b;
    d = (a + b) + c;
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
