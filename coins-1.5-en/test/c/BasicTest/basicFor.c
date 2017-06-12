/* bassicFor  reachable/unreachable test for for-loop */

int puts(char*);

reachable_for(int a)
{
  switch(a)
  {
    case 0:
    for( puts("R 0 I"); 0; puts("R 0 S") )
      case 1:  puts("R 0 B");
    puts("R 0 /");

    for( puts("R 1 I"); 0; puts("R 1 S") )
       puts("R 1 B");
    puts("R 1 /");

    for( puts("R 2 I"); 0; puts("R 2 S") );
    puts("R 2 /");

    for( puts("R 3 I"); 0;);
    puts("R 3 /");

    for(; 0;);
    puts("R 4 /");
  }
}

unreachable_for(int a)
{
  switch(a)
  {
    for( puts("U 0 I"); 0; puts("U 0 S") )
      case 0:  puts("U 0 B");
    puts("U 0 /");
    break;

    for( puts("U 1 I"); 0; puts("U 1 S") );
    puts("U 1 /");
    break;

    for( puts("U 2 I"); 0; );
    puts("U 2 /");
    break;

    for( ; 0; );
    puts("U 3 /");
    break;
  }
}

int main()
{
  int i;
  for( i=0; i<1; i++ )
  {
    reachable_for(i);
    unreachable_for(i);
  }
  return 0;
}
