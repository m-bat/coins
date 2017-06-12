/* tpwhile0.c:  while test (without array) */

int main()
{
  int s, i;

  s = 0;
  i = 1;
  while (i < 100) {
    s = s + i * i;
    i = i + 1;
  }
  printf("s=%d i=%d\n",s,i); /* SF030620 */
  return 0;
} 

