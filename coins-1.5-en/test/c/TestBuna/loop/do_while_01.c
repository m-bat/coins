/* do_while_01.c   simple do while statement  */
int printf(char *s, ...);

int main()
{
  int s, i;

  s = 0;
  i = 1;
  do{
    s = s + i * i;
    i = i + 1;
  } while( i<=100);

   printf(" i = %d\n", i );
   printf(" s = %d\n", s );
  return 0;
} 

