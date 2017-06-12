/* tpdeadcode1.c: deadcode test 1 */ 

int main()
{
  int a, b, c, d, i;
  a = 10; 
  b = 20;
  c = a + b;
  for (i = 0; i < 10000; i=i+1) {
    d = a * b;
  }
}


