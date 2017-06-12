/* tpstruct1-1.c:  Simple struct and common subexp */

int printf(char*, ...);

int a, b, c;
struct address {
  char* name;
  int   zip;
};
struct address tanaka;

int main()
{
  struct { int x; int y; } point;
  struct { int x; int y; } point2;
  int a, b, c;

  tanaka.name = "Tanaka";
  tanaka.zip = 184;
  if (tanaka.zip == 0)
    tanaka.zip = 001;
  point.x = 1;
  a = point.x + point.x;
  point.y = point.x + 1;
  b = point.x + point.x;
  point2.x = point.x + point.x;
  c = point.x + point.y;
  point2.y = point.y; /* SF030609 */
  printf("tanaka = {%s,%d}\n",tanaka.name,tanaka.zip); /* SF030609 */
  printf("point = {%d,%d}\n",point.x,point.y); /* SF030609 */
  printf("point2 = {%d,%d}\n",point2.x,point2.y); /* SF030609 */
  printf("a, b, c = %d %d %d \n", a, b, c);
  return 0;
}
