/* tpblockScope4.c:  getSymNestIterator bug Mori mail 030903 */


int foo()
{
  int a;
  {
    int b;
    {
      int c;
      {
        int d;
      }
    }
  }
  {
    int e;
    {
      int f;
    }
  }
  {
    int g;
  }
}

int bar()
{
  int x;
  {
    int y;
  }
  {
    int v;
  }
}

