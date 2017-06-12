/* tpcall1.c:  Basic call */

int a, b;
char c;
int fn(char p)
{
  return (int)p+1;
}
int main()
{
  a = 0;
  b = 1;
  c = 'x';
  printf("a %d b %d c %x \n", a, b, c);
  a = fn(c);
  b = fn(c) * 16;
  printf("a %d b %d c %x \n", a, b, c);
  return 0;
} 

