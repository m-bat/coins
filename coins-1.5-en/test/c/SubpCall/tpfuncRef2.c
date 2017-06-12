/* tpfuncRef2.c  Function reference  */

int f()
{
  f;
  return 0;
}

int f1(int p1)
{
  f1;
  return p1;
}

int f2(int p1, ...)
{
  return p1;
}

int (*pf)();
int (*pf1)(int);
int (*pf2)(int, ...);
int k;

int main()
{
  int i=1, j=2;
  pf = f;
  k = pf();
  printf("k = %d\n",k); /* SF030609 */
  pf1 = f1;
  k = k + pf1(i);
  printf("k = %d\n",k); /* SF030609 */
  pf2 = f2; /* SF030609 */
  k = k + pf2(0, i);
  printf("k = %d\n",k); /* SF030609 */
  return 0;
}

