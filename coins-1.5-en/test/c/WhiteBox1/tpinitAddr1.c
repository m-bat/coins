/* tpinitAddr1.c:  Address as initial value */

int printf(char*, ...);

int GlobalVariable = 10; 
int *g2 = { &GlobalVariable };

int main()
{
  int a = 20;
  int *pa = &a;
  int **pg2 = &g2; 
  printf("%d %d %d \n", *g2, *pa, **pg2);
  return 0;
}

