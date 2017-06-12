/* tpptr1.c  Kise mail 050929 */

int printf(char*, ...);

int main()
{
  int i, j;
  int *p;
  int *b[] = {&i};
  int *c[1];
  i = 3;
  j = 4;
  p = &j; 
  c[0] = p;
  printf("*b[0]=%d *p=%d *c[0]=%d \n", *b[0], *p, *c[0]);
  return 0;
}

