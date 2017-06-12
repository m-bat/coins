/* tpDoMri1.c */

int
main()
{
  int a;
  long count;
  a = 100000;
  count = 0;
  do
    count ++;
  while(a--);
  printf("count=%d \n", count);
}
