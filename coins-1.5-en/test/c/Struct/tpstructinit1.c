/* tpstructinit1.c  Initial value  for structure */

struct price { char *k; int v; };
struct price list[] = { { "book", 500}, { "coffee", 200 } }; /* Parse Error "invalid initializer" */

main()
{
  int total;

  total = list[0].v + list[1].v;
  printf("list = [[%s,%d],[%s,%d]]\n", /* SF030609 */
    list[0].k,list[0].v,list[1].k,list[1].v); /* SF030609 */
  printf("total = %d\n",total); /* SF030609 */
  return 0;
}

