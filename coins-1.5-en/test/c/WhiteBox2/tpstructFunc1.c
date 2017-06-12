/* tpstructFunc1.c  Function returning struct value */
/*   Fukuda mail 040302 */

int printf(char*, ...);

struct S {
  int i;
};

struct S f(int v)
{
  struct S s1 = {v};
  return s1;
}

main()
{ 
  struct S x;
  x = f(f(1).i);
  printf(" %d \n", x.i);
  return 0;
}

