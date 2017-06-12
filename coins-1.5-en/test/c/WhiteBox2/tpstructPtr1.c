/* tpstructPtr1.c:  struct and pointer */

int a, b, c;
struct address {
  char* name;
  int   zip;
};
struct address tanaka;

int main()
{
  struct { int x; int y; } point;
  struct address *pa;
  struct { int x; int y; } *pp;
 
  pa = &tanaka;
  pp = &point; 
  
  pa->name = "Tanaka";
  pa->zip = 184;
  pp->x = 1;
  pp->y = point.x + 1;
  printf("%d %d %d \n", tanaka.zip, point.x, point.y);
  return 0;
} 

