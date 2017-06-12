/* basicSwitch.c  reachable/unreachable test for switch satatement. */

int puts(char*);

reachable_switch(int a)
{
  switch(a)
  {
  case  1: puts("R 0 0");
  case  2: puts("R 0 1");
  default: puts("R 0 2");
  }
  puts("R 0 /");

  switch(a)
  {
  case  1: puts("R 1 0"); break;
  case  2: puts("R 1 1"); break;
  default: puts("R 1 2"); break;
  }
  puts("R 1 /");
}

unreachable_switch(int a)
{
  goto LABEL_X;
  switch(a)
  {
  case  1: puts("U 0 0");
  LABEL_X:
  case  2: puts("U 0 1");
  default: puts("U 0 2");
  }
  puts("U 0 /");

  goto LABEL_Y;
  switch(a)
  {
  case  1: puts("U 1 0"); break;
  LABEL_Y:                break;
  case  2: puts("U 1 1"); break;
  default: puts("U 1 2"); break;
  }
  puts("U 1 /");
}

main()
{
  int i = 0;
  for( i=0; i<3; i++ )
  {
    reachable_switch(i);
    unreachable_switch(i);
  }
}
