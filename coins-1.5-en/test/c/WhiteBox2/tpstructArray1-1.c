/* tpstructArray1-1.c:  struct array test with common subexpression */

int printf(char*, ...);

int i;
struct point {
  int x;
  int y;
};

struct point pa[10];

int main()
{
  int a, b, c;
  i = 1;
  pa[1].x = 1;
  pa[1].y = 1;
  a = pa[i].x + pa[1].y;
  b = pa[i].x + pa[1].y + 1;
  pa[i].x = pa[1].y;
  c = pa[i].x + pa[1].y + 1;
  pa[i].y = pa[1].x + 1;
  c = c + pa[i].x + pa[1].y;

  printf("pa[1] = [%d,%d]\n",pa[1].x,pa[1].y); /* SF030609 */
  printf("a, b, c %d %d %d \n", a, b, c);

  return 0;
}

