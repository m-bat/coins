/*---- tpoptGL1.c: Global optimization with global and local variables ----*/

int printf(char *, ...);

int ff(int p1);

int ga, gb, gc, gd, ge, gf, gh;

int main()
{
  int a, b, c, d, e, f, h;
  a = ff(1);
  b = ff(2 + 3);
  ga = ff(1);
  gb = ff(5);
  c = a + b;
  d = a + 1;
  gc = ga + gb;
  gd = gb + ga;
  if (a) {
    c = a + b;
    d = (a + b) + c;
    e = a + 1;
    f = a + b + c;
    gc = ga + gb;
    gd = ga + gb + gc;
    ge = ga + gc;
    ge = ga + ff(1);
    gf = ga + gb + gc;
  }
  d = d + 2;
  h = a + b + c;
  gh = 3 * (ga + 1);
  printf("a=%d b=%d c=%d d=%d e=%d f=%d h=%d return %d\n",
          a,b,c,d,e,f,h,d+2); 
  printf("ga=%d gb=%d gc=%d gd=%d ge=%d gf=%d gh=%d \n", 
          ga, gb, gc, gd, ge, gf, gh);
  return 0;
}

int ff(int p)
{
  return p;
}

