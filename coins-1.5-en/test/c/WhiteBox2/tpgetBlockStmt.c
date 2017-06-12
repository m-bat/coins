/* tpgetBlockStmt.c:  getBlockStmt is bad  */
/*    Fukuda mail 031115 */

int puts(char*);

int main()
{
  int i, j=0;
  int a[30];
  int x = 0, y = 0;
  if (x==y) {
    for (i=j; i<30; i++) {  /* getBlockStmt() returns null for this LoopStmt.*/
      a[i]=i;
    }
  }
  reachable_do(a[j+1]);
  return 0;
}
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

