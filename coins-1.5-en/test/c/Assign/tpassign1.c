/* tpassign1.c:  Basic assignment with arithmetic operations */
int a, b, c;
int x;
int main()
{
  a = 1;
  b = a + 2;
  c = b + a * c; 
  x = a + (b + c) * a;
  /* SF030620[ */
  printf("Basic assignment w/ arithmetic op: global var.\n");
  printf("a=%d b=%d c=%d x=%d \n",a,b,c,x);
  /* SF030620] */  
  return 0;
} 

