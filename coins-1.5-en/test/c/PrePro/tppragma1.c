#pragma SC once

int j;
int k;
#pragma SC once
int m;
#pragma SideEffect printf

int printf(char*, ...);

int a, b, c;
int x, y, z;
int main()
{
  a = 1;
  b = a + 2;
  c = b + a * c;
  x = a + (b + c) * a;
  printf("%d \n", x); /* SF030609 */
  return x;
}


