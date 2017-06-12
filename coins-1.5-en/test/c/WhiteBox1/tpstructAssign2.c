/* tpstructAssign2.c:  structure assignment with struct function */

int printf(char*, ...);

#define NULL 0

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
} t1, t2, t3;
struct address tanaka, yamada;

struct tree addChild1( int pNumber, struct tree p1, struct tree *p2 )
{
  struct tree lTree;
  lTree = p1;
  lTree.number = pNumber;
  lTree.child1 = p2;
  return lTree;
}

int main()
{
  tanaka.name = "Tanaka";
  tanaka.loc.yuban = 184;
  tanaka.loc.addr = "Tokyo";
  yamada = tanaka;
  yamada.name = "Yamada";
  printf("name %s yuban %d addr %s \n", yamada.name, yamada.loc.yuban,
         yamada.loc.addr );
  t1.number = 1;
  t1.child1 = NULL;
  t1.child2 = NULL;
  t2.number = 2;
  t2.child1 = &t1;
  t2.child2 = NULL;
  t3 = addChild1( 3, t1, &t2 );
  printf("t3 %d t3.child1 %d \n", t3.number, t3.child1->number);
  return 0;
} 

