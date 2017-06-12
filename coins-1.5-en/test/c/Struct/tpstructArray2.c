/* tpstructArray2.c:  struct array test 2 */

int a, b, c;
struct point {
  int x;
  int y;
};
struct {
  int x;
  int y;
} line1[2];
struct line {
  struct point from;
  struct point to;
};
struct line line2;

struct point pa[10];

struct {
  struct {
    int x;
    int y;
  } terminal[2];
} line3[3];

int main()
{
  pa[0].x = 1;
  pa[0].y = 1;
  line1[1].x = pa[0].y;
  line1[1].y = pa[0].x + 1;
  line2.from.x = 2;
  line2.from.y = 3;
  line2.to.x   = 2;
  line2.to.y   = 4;
  line3[1].terminal[0].x = 0;
  line3[1].terminal[0].y = 0;
  line3[1].terminal[1].x = line3[1].terminal[0].x + 1;
  line3[1].terminal[1].y = 2;

  /* SF030609[ */
  printf("pa[0] = [%d,%d]\n",pa[0].x,pa[0].y);
  printf("line1[1] = [%d,%d]\n",line1[1].x,line1[1].y);
  printf("line2 = [[%d,%d],[%d,%d]]\n",
    line2.from.x,line2.from.y,line2.to.x,line2.to.y);
  printf("line3[1] = [[%d,%d],[%d,%d]]\n",
    line3[1].terminal[0].x,line3[1].terminal[0].y,
    line3[1].terminal[1].x,line3[1].terminal[1].y);
  /* SF030609] */

  return 0;
}

