/* tpstruct1.c:  Simple struct */

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

  tanaka.name = "Tanaka";
  tanaka.zip = 184;
  if (tanaka.zip == 0)
    tanaka.zip = 001;
  point.x = 1;
  point.y = point.x + 1;
  point2.x = point.x;
  point2.y = point.y; /* SF030609 */
  printf("tanaka = {%s,%d}\n",tanaka.name,tanaka.zip); /* SF030609 */
  printf("point = {%d,%d}\n",point.x,point.y); /* SF030609 */
  printf("point2 = {%d,%d}\n",point2.x,point2.y); /* SF030609 */
  return 0;
}
