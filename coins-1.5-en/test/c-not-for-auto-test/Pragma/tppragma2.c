/* tppragma2.c:  pragma test 2 */

#pragma DUMMY0
int a;
#pragma DUMMY1
#pragma "dummy"DUMMY
int b, c;
int fn1(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}
#pragma FUNC fn1
#pragma VAR (a, b)
int main()
{
  int i, ee, f, g;
  int a, aa[10];
#pragma VAR (a, ee)
    a = 0;
#pragma LIST (a, (b, c), (f, g), ee)
    b = 1;
L1: c = b;
    ee = fn1(c);
    aa[0] = 1;
#pragma LABEL ((L1, c), (L2, i), (L3, (aa, i), (L4, (a, b, c, ee, g))))
L2: for (i = 1; i < 10; i++) 
L3: {
      aa[i] = aa[i-1] + i;
#pragma CONST 0 10 "abc"
    }
    g = aa[9];
L4: printf("a,b,c,ee,g = %d,%d,%d,%d,%d\n",a,b,c,ee, g);
  return 0;
}
