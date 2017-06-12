/* tpstructinit2.c  Initial value  for structure */
/*     A structure is assigned as initial value. */

struct price { char *k; int v; };
struct price list[] = { { "book", 500}, { "coffee", 200 } }; /* Parse Error "invalid initializer" */

/* Spec2000/parser/main.c */
int verbosity, screen_width, linkage_count_limit;
struct {char * s; int * p; int isboolean; char * str;} user_variable[] = {
    {"verbosity", &verbosity, 0, "Level of detail to give about the computation"},
    {"width",     &screen_width, 0, "The width of your screen"},
    {"limit",     &linkage_count_limit, 0, "The maximum number of linkages processed"} }; 

/* Spec2000/vortex/bmt.c */
typedef  struct TypeToken
{
   int      Handle;
   int      DbId;
   int      CoreDbId;
}  tokentype;
tokentype NullToken = { 1, 0, 0 };
/* tokentype PartTkn = NullToken; */ /* Initializer element is not constant */

main()
{
  int total;

  tokentype partTkn = NullToken;    /* Structure assignment */
  total = list[0].v + list[1].v;
  *user_variable[0].p = 10;
  *user_variable[1].p = 20;
  *user_variable[2].p = 100;
  printf("%d %s \n", total, user_variable[1].s);
  printf("%d %d %d  \n", verbosity, screen_width, linkage_count_limit);
  printf("%d %d \n", partTkn.Handle, partTkn.CoreDbId);
  return 0;
}

