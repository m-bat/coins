/* tpforIf2.c: For-statement including if-statement. */

int printf(char*, ...);

int f(int p) 
{
  return p;
}

main( )
{
  int i;
  int s, q;
  s = 0;
  q = 10;
  for (i=0 ; i<20 ; i++) {
    if (f(q)) 
      q = q - 1;
    else
      s = s + i;
  }
  printf("%d \n", s);
}

