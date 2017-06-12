/* tpnoteq1.c:  not equal test (Mori mail 041001) */

int printf(char*, ...);

int a, b, c;
int main()
{
  a = 1;
  b = 2;
  if (!(a==b)) {
    c = 0;
  }else {
    c = 1;
  }
  printf("c=%d\n",c);
  return 0;
} 

