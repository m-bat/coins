/* tpstructOpt1.c:  structoptimization 1 */

int a=1, b=2, c;

struct list { int number; struct list* next; };

int main()
{
  int d;
  struct list list1, list2, list3;
  c = a+b; 
  d = a + b + c;
  list3.number = a+b;
  list3.next = 0;
  if (c > 0)
    list2.number = list3.number + list3.number; 
  else
    list2.number = list3.number + 1;
  list2.next = &list3;
  list1.number = list3.number + list3.number;
  if (c <= 0)
    list1.number = list3.number + 1;
  list1.next = &list2;
  
  printf("%d,%d,%d\n", list1.number, list2.number, list3.number); 
  return 0;
}

