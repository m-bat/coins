/* tpincompletetype1.c:  incomplete type with struct and vect */

#define NULL 0
int aa[10];
int bb[10];
int cc[];
struct address {
  char* name;
  struct location {
    int   yuban;
    char* addr;
  } loc;
};
struct list;
struct tree;
struct list { int number; struct list* next; };
struct tree {
  int number;
  struct tree* child1;
  struct tree* child2;
} t1;
struct address tanaka;

struct list 
append( struct list pList, int pNumber )
{
  struct list lList;
  lList.number = pNumber;
  lList.next = NULL;
  pList.next = &lList;
  return lList;
}

int func( int pa[10], int pb[], int pi)
{
  return pa[pi] + pb[pi];
}

int main()
{
/** incompatible with global type
  struct list;
  struct list { int number; struct list* next; }; 
**/
  struct list numberList, nextList1, nextList2;
  int dd[10];
  int k;
  tanaka.name = "Tanaka";
  tanaka.loc.yuban = 184;
  if (tanaka.loc.addr == 0)
    tanaka.loc.addr = " ";
  numberList.number = 1;
  numberList.next   = NULL;
  nextList1 = append(numberList, 2); 
  nextList2 = append(nextList2, 3); 
  printf("list %d \n", numberList.number);
  printf("list %d \n", nextList1.number);
  printf("list %d \n", nextList2.number);
  aa[1] = 1;
  bb[1] = 2;
  k = func(aa, bb, 1); 
  printf("k %d\n", k);
  return 0;
} 

