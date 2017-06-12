/*---- tpoptL2-1.c: Local optimization for local variables ----*/

int printf(char*, ...);

int main()
{
  int a, b, c, d, e, f;

  a = 1; 
  b = 2 + 3;
  e = a + b;  /*##*/
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
  printf("%d %d %d %d \n", c, d, e, f);
  return 0;
}
