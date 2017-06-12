/* if-stmt with for-loop */

int main()
{
  int a, b, c, i;
 
  a = 1;
  b = 2;
  if (a == 1) {
    for (i = 1; i < 10; i++) {
      c = a + b;
    }
  }else {
    a = 3;
    c = a + b;  
  }
  c = a + b;
  printf("c=%d\n",c); /* SF030620 */
  return 0;
}
