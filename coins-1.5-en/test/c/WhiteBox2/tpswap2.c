/* tpswap2.c -- Parameter array without array size */

int printf(char*, ...);

void swapx(int pi, int pj, int px[])
{
  int tmp;
  tmp   = px[pi];
  px[pi] = px[pj];
  px[pj] = tmp;
} 

int main()
{
  int i;
  int x[10];

  x[0] = 1;
  for (i = 1; i < 10; i++) {
    x[i] = x[i-1] + x[i-1]; 
  }
  swapx(7, 8, x);
  printf("x[7]=%d x[8]=%d",x[7],x[8]); 
  return 0; 
}

