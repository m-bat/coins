/* tpstructList1.c:  list of structures */

int printf(char*, ...);

int a, b, c;
struct listNode {
  int value;
  struct listNode *next;
} listAnchor, listNode1;

int main()
{
  struct listNode listNode2;
  
  struct listNode * nodePtr;
  int *valuePtr;
  int *castptr;
  listAnchor.next = &listNode1;
  listNode1.next  = &listNode2;
  listNode2.next  = 0;
  castptr = (int*)listAnchor.next;
  ((struct listNode *)castptr)->value = 2;
  a = 1;
  for (nodePtr = &listAnchor; 
       nodePtr != 0; 
       nodePtr = nodePtr->next) {
    nodePtr->value = a;
    a++;
  }
  valuePtr = &listNode1.value;
  printf("%d %d %d \n", listAnchor.value, *valuePtr, listNode2.value);
  return 0;
} 

