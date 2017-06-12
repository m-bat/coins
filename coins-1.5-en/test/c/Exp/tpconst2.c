/* tpconst2.c:  Constants */
int    i1, i2, i3;
float  f1, f2, f3;
double d1, d2, d3;
long   l1, l2, l3;
int main()
{
  i1 = 2L;
  l1 = i1;
  l2 = 1;
  l3 = 123456789012345;

  f1 = 3.14f;
  f2 = 1.9999;
  d1 = 1.2345f;
  d1 = d1 + 1.3F;
  d2 = 1.2345L;
  d3 = +1.2345678901L;
  d3 = 1.2L + 1.3l;
  printf("f1+f2=%f d3=%e \n",f1+f2,d3); /* SF030620 */
  return 0;
}

