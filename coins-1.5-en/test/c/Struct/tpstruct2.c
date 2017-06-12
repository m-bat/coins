/* tpstruct2.c:  Defered struct and nested struct */

int a, b, c;
struct address {
  char* name;
  struct location {
    int   yuban;
    char* addr;
  } loc;
};
struct list;
struct list { int number; struct list* next; };
struct tree {
  int number;
  struct tree* child1;
  struct tree* child2;
} t1;
struct address tanaka;

int main()
{
  tanaka.name = "Tanaka";
  tanaka.loc.yuban = 184;
  if (tanaka.loc.addr == 0)
    tanaka.loc.addr = " ";
  printf("tanaka = [%s,[%d,%s]]\n", /* SF030609 */
    tanaka.name,tanaka.loc.yuban,tanaka.loc.addr); /* SF030609 */
  return 0;
}

