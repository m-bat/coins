/* tppragmaprof2.c:  pragma test 2 for profiling */

#pragma simulate profileOff fn2 fn3 fn4

int a;
int b, c;
int x;
int fn1(int p)
{
  int d;
  d = 1;
  d = p + d;
#pragma simulate profileOff
  return d;
}

int fn2(int p)
{
  int d;
  d = 1;
#pragma simulate profileOn
  d = p + d;
  return d;
}

int fn3(int p)
{
  int d;
  d = 2;
#pragma simulate profileOn
  d = p * d;
#pragma simulate profileOff
  d = d + 1;
  return d;
}

int fn4(int p)
{
  int d;
  d = 1;
  d = p + d;
  return d;
}

int main()
{
  int i, ee, f, g;
  int a, aa[10];
#pragma simulate open
    a = 0;
    b = 1;
    x = 1;
#pragma simulate profileOn
L1: c = b;
    ee = fn1(c);
    ee = ee + fn2(c);
    aa[0] = 1;
L2: for (i = 1; i < 10; i++) 
L3: {
      aa[i] = aa[i-1] + i;
      x = x + fn3(i);
    }
    g = aa[9];
L4: printf("a,b,c,ee,g = %d,%d,%d,%d,%d\n",a,b,c,ee, g);
#pragma simulate close
  return 0;
}
