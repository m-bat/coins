/* tpconstProp1.c */

int printf(char*, ...);

int main ()
{
  int a, b, x;

  a = 1;
  b = 2;

  if (a+1 < b-1) {
    x = a - b; 
  }else {
    x = a + b;
  }

  printf("%d \n", x);
}
