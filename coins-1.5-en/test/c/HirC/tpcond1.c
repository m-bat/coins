/* tpcond1.c -- conditional exp */
int main() 
{
  int a, b, c;

  a = 5;
  b = 10;
  c = a > b ? a : b;
  printf("c=%d\n",c); /* SF030620 */
  return 0;
}

