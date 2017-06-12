/* while_03.c   simple while statement  */

int printf(char *s, ...);

int main()
{
  int  i;
  int a[3];
  i = 3;
  while (i) {
    a[--i]=i;
   printf(" i = %d\n", i );
   printf(" a[i] = %d\n", a[i] );
  }
  return 0;
} 

