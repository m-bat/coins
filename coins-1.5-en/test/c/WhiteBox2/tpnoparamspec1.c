/* tpnoparamspec.c: No parameter specification test */ 


int sub1();

int sub1(int n) 
{
  return n;
}

main()
{
    printf("%d\n",sub1(10)); 
    printf("%d %d\n",sub1(20), sub2(5, 3)); 
    printf("%d %d %d\n",sub1(30), sub2(5, 3), sub3(1, 2, 3)); 
}

int sub2(int p1, int p2)
{
  return p1+p2;
}

int sub3(int p1, int p2, int p3)
{
  return p1+p2 + p3;
}

