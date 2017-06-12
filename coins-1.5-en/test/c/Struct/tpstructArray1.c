/* tpstructArray1.c:  struct array test 1 */

int i;
struct point {
  int x;
  int y;
};

struct point pa[10];

int main()
{
  i = 1;
  pa[1].x = 1;
  pa[1].y = 1;
  pa[i].x = pa[1].y;
  pa[i].y = pa[1].x + 1;

  printf("pa[1] = [%d,%d]\n",pa[1].x,pa[1].y); /* SF030609 */

  return 0;
}

