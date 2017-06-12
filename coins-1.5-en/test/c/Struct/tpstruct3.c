/* tpstruct3.c:  Defered struct and nested struct, nested union */

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
  union attr {
    char  a1;
    float a2;
    struct {
      int b1;
      int b2;
    } bx;
  } u1;
  struct tree* child1;
  struct tree* child2;
} t1;
struct address tanaka;
struct tree    tr1;
struct list    li1; /* SF030609 */

int main()
{
  tanaka.name = "Tanaka";
  tanaka.loc.yuban = 184;
  if (tanaka.loc.addr == 0)
    tanaka.loc.addr = " ";
  t1.u1.bx.b2 = 0;
  /* SF030609[ */
  printf("tanaka = [%s,[%d,%s]]\n",tanaka.name,tanaka.loc.yuban,tanaka.loc.addr);
  tr1.number   = 123;
  tr1.u1.a1    = 23;
  printf("tr1.u1.a1 = %d\n",tr1.u1.a1);
  tr1.u1.a2    = 34.56f;
  printf("tr1.u1.a2 = %f\n",tr1.u1.a2);
  tr1.u1.bx.b1 = 456;
  tr1.u1.bx.b2 = 567;
  tr1.child1   = &tr1;
  tr1.child2   = &tr1;
  printf("tr1 = [%d,[%d,%d],[%d,...],[%d,...]]\n",
    tr1.number,tr1.u1.bx.b1,tr1.u1.bx.b2,tr1.child1->number,tr1.child2->number);
  li1.number = 678;
  li1.next   = &li1;
  printf("li1 = [%d,[%d,...]]\n",li1.number,li1.next->number);
  /* SF030609] */
  return 0;
}
