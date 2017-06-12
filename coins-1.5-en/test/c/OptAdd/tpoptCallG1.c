/*---- tpoptCallG1.c: Global optimization (integer expressions) ----*/
/*       with global variables, with call, without <stdio> */

int printf(char*, ...);

int a, b, c, d, e, f, g, h;

int main()
{
  a = 1; 
  b = 2 + 3;
  c = a + b;
  d = a + 1;
  e = (a + b) * (a + 1);
  f = (a + b) * (a + 1) + (a + b) + (a + 1);
  if (a) {
    c = a + b;
    d = (a + b) + c;
    e = a + 1;
    printf("c=%d d=%d f=%d \n",c,d,f);
    d = (a + b) + c;
    e = a + 1;
    f = a + b + c;
  }else {
    c = a + 1;
    d = 3 * (a + 1);
    e = a + 1;
    printf("d=%d e=%d \n",d,e);
    e = a + 1;
    f = e + 1;
  } 
  d = d + 2;
  printf("a=%d b=%d c=%d d=%d e=%d f=%d return %d\n",a,b,c,d,e,f,d+2); /* SF030620 */
  return d + 2;
}
