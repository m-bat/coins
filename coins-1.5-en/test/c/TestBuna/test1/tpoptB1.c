/* tpoptB1.c:  Branch optimization */
int printf(char *s, ...);

int a, b, c;
int x;
int main()
{
  a = 1;
  b = 2;
  if (a)
    b = 0;
  else 
    ;
  if ((a > b+1)||(a > 2)) {
    x = 0;
    c = a;
  }else {
    goto last;    
  }
last:
  printf("last\n");
  return c;
} 

