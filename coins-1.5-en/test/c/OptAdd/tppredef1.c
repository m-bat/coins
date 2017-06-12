/* tppredef1.c: Predefined function */
int printf(char*, ...);
double sin(double p1);
  int a = 1, b = 2, c, d;
int main()
{
  double x, y;
  c = a + b;
  x = 1.2;
  y = sin(x) + sin(x);
  d = a + b;
  printf(" %f %d %d", y, c, d);
}


