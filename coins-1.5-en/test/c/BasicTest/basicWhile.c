/* basicWhile.c  Reachable/unreachable test for while loop */

int puts(char*);

reachable_while(int a)
{
  switch(a)
  {
    case 0:
    while(0)
      case 1: puts("R 0");
    puts("R 0 /");

    while(0)
      puts("R 1");
    puts("R 1 /");

    while(0);
    puts("R 2 /");
  }
}

unreachable_while(int a)
{
  switch(a)
  {
    while(0)
      case 0: puts("U 0");
    puts("U 0 /");
    break;

    while(0);
    puts("U 1 /");
    break;
  }
}

int main()
{
  int i;
  for( i=0; i<1; i++ )
  {
    reachable_while(i);
    unreachable_while(i);
  }
  return 0;
}
