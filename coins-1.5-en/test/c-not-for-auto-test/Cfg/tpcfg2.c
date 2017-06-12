/* tpcfg2.c: BBlockHirSubtreeIterator test */

int main()
{
  int i;
  {
    for (i = 0; i < 100; i++)
      i = 1;
  }
  return i;
}

