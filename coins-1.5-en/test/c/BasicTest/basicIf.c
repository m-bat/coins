/* basicIf.c    Reachable/unreachable test for if statement */

int puts(char*);

void reachable_true_if(int a)
{
  switch(a)
  {
    case 0:
    if(1)
      puts("RT 0 T");
    else
      puts("RT 0 E");
    puts("RT 0 /");

    if(1)
      case 1: puts("RT 1 T");
    else
      puts("RT 1 E");
    puts("RT 1 /");

    if(1)
      puts("RT 2 T");
    else
      case 2: puts("RT 2 E");
    puts("RT 2 /");

    if(1)
      case 3: puts("RT 3 T");
    else
      case 4: puts("RT 3 E");
    puts("RT 3 /");
  }
}

void reachable_false_if(int a)
{
  switch(a)
  {
    case 0:
    if(0)
      puts("RF 0 T");
    else
      puts("RF 0 E");
    puts("RF 0 /");

    if(0)
      case 1: puts("RF 1 T");
    else
      puts("RF 1 E");
    puts("RF 1 /");

    if(0)
      puts("RF 2 T");
    else
      case 2: puts("RF 2 E");
    puts("RF 2 /");

    if(0)
      case 3: puts("RF 3 T");
    else
      case 4: puts("RF 3 E");
    puts("RF 3 /");
  }
}

void unreachable_true_if(int a)
{
  switch(a)
  {
    if(1)
      puts("UT 0 T");
    else
      puts("UT 0 E");
    puts("UT 0 /");
    break;

    if(1)
      case 0: puts("UT 1 T");
    else
      puts("UT 1 E");
    puts("UT 1 /");
    break;

    if(1)
      puts("UT 2 T");
    else
      case 1: puts("UT 2 E");
    puts("UT 2 /");
    break;

    if(1)
      case 2: puts("UT 3 T");
    else
      case 3: puts("UT 3 E");
    puts("UT 3 /");
    break;
  }
}

void unreachable_false_if(int a)
{
  switch(a)
  {
    if(0)
      puts("UF 0 T");
    else
      puts("UF 0 E");
    puts("UF 0 /");
    break;

    if(0)
      case 0: puts("UF 1 T");
    else
      puts("UF 1 E");
    puts("UF 1 /");
    break;

    if(0)
      puts("UF 2 T");
    else
      case 1: puts("UF 2 E");
    puts("UF 2 /");
    break;

    if(0)
      case 2: puts("UF 3 T");
    else
      case 3: puts("UF 3 E");
    puts("UF 3 /");
    break;
  }
}

int main()
{
  int i;
  for( i=0; i<4; i++ )
  {
    reachable_true_if(i);
    reachable_false_if(i);
    unreachable_true_if(i);
    unreachable_false_if(i);
  }
  return 0;
}
