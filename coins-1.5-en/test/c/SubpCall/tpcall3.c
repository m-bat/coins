/* tpcall3.c:  void parameter, parameter conv, result value conv. */
float a, b;
double c;

int fn(float p1)
{
  float e;

  e = a + 1.0;
  return p1+e;
}
void fv(void) {
  a = 1;
  b = 2.0;
  c = b;
}
int main()
{
  fv();
  a = fn(1);
  b = fn(c) * 16;
  printf("%f %f %e \n", a, b, c);
  return 0;
} 

