/*---- tpoptL1.c: Local optimization test 1 ----*/
int a, b = 2, c = 3, d = 4;  /* See Sassa: Language processors, p. 455. */
int printf(char *s, ...);

int main()
{
  a = b + c;
  b = d;
  b = b + c;
  d = c + d;
  
  printf("d = %d\n", d);
  return d;
}
