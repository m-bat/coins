/* tppre16.c: PRE test of conditional exp in do-while loop */ 

int printf(char*, ...);

int main()
{
  int i, j, k;
  int a[100];

  for (i = 0; i < 100; i=i+1) {
    a[i] = i*(100-i);
  }
  i = 50; 
  j = 0;
  do {
    j = j + 1;
    k = a[i] + j;
  }while (j < a[i]);
  printf(" %d %d", j, k);
}


