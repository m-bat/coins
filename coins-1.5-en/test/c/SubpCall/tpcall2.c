/* tpcall2.c: Call with type conversion */

float a, b;
double c;

double fn(float p)
{
  float e;

  e = p + 1.0;
  return (double)(p+e);
}
int main()
{
  c = 2.0;
  a = fn(1);
  b = fn(c) * 16;
  printf("a,b,c = %f,%f,%f\n",a,b,c); /* SF030609 */
  return (int)b;
}
