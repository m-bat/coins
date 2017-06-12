/* tppredef2.c: Predefined function 2 */
int printf(char*, ...);
double sin(double p1);
  int a = 1, b = 2, c;
int main()
{
  double x, y;
  c = a + b;
  x = 1.2;
  if (c > 2) {
    y = sin(x);
  }else
    y = x;
  y = y + sin(x);
  printf(" %.5f %d \n", y, c);
  return 0;
}


