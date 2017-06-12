/* tpdowhile.c:  do-while loop */

int main()
{
  int s, i;

  s = 0;
  i = 1;
  do {
    s = s + i * i;
    i = i + 1;
  } while (i <= 100);
  printf("s=%d i=%d\n",s,i);
  return 0;
} 

