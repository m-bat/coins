/* basicDo.c  reachable, unreachable test for do while loop. */

int puts(char*);

reachable_do(int a)
{
  switch(a)
  {
    case 0:
    do
      case 1: puts("R 0");
    while(0);
    puts("R 0 /");

    do
      puts("R 1");
    while(0);
    puts("R 1 /");

    do;
    while(0);
    puts("R 2 /");
  }
}

unreachable_do(int a)
{
  switch(a)
  {
    do
      case 0: puts("U 0");
    while(0);
    puts("U 0 /");
    break;

    do
      puts("U 1");
    while(0);
    puts("U 1 /");
    break;

    do;
    while(0);
    puts("U 2 /");
    break;
  }
}

int main()
{
  int i;
  for( i=0; i<1; i++ )
  {
    reachable_do(i);
    unreachable_do(i);
  }
  return 0;
}
