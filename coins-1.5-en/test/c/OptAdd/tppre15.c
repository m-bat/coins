/* tppre15.c: PRE test of conditional exp in loop */ 

int printf(char*, ...);

int main()
{
  int i, n, peak;
  int a[100];

  for (i = 0; i < 100; i=i+1) {
    a[i] = i*(100-i);
  }
  
  peak = a[0]; 
  for (i = 1; peak < a[i]; i++) {
    peak = a[i];
  }
  printf(" %d %d", peak, i);
}


