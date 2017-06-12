/* tppre16.c: PRE test of switch case */ 

int printf(char*, ...);

int main()
{
  int i, d;
  int a[100];
  i = 1;
  a[0] = 0;
  a[1] = 1;
  switch (a[i]) {
  case 0:
    d = a[i] + 1;
    break;
  case 1:
    d = a[i]+2;
    break;
  default:
    d = a[i];
  }
  printf(" %d ", d);
}


