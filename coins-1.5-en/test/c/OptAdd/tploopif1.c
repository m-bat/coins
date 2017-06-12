/* tploopif1.c   loopif expansion test */

int printf(char*, ...);

int main()
{
  int i= 2, j, m= 3, n= 4, d;
  for(j=0; j<100; j++){
    if ( (m < j) && (n < j))
      d = m + n; 
    else 
      d = 1;
    if (d < 0)
      d = 0;
    if (d > 255)
      d = 255;
  }
  printf("%d \n", d);
}

