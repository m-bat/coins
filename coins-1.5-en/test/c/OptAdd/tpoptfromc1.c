/* tpoptfromc1.c:  hirOpt=fromc optimization */

int printf(char*, ...);

int main()
{
  int a, b, c;
  int x;

  a = 1;
  b = 2;
  if (1)
    c = 0;
  else 
    c = 1;
  if (0) {
    x = 0;
  }else {
    x = 1;
    goto L1;    
  }
L1:
  while (1) {
    c = c+1;
    if (c > 2)
      break;
  }
  while (0)
    x = 3;
  goto L2;
  x = 4;
L2:
  if (! (a > b))
    a = 3;
  else
    a = 4;
  b = a + 0;
  a = b * 0;

  printf("%d %d %d %d \n",a,b,c,x);
  return c;
} 

