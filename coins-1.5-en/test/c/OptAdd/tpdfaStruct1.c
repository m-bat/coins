/* tpdfaStruct1.c:  Data flow analysis for struct array 1 */

int  printf(char*, ...);

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
  int i = 0, j = 1;
  pa[0].x = 1;
  pa[0].y = 1;
  line1[1].x = pa[0].y + pa[0].y;
  line1[1].y = pa[0].x + pa[0].x + 1;
  line2.from.x = pa[0].y + pa[0].y;
  line2.from.y = pa[0].x + pa[0].x;
  pa[i].x = pa[0].x;
  pa[j].y = pa[0].y ;
  line2.to.x   = 2;
  line2.to.y   = 4;
  line3[1].terminal[0].x = 1;
  line3[1].terminal[0].y = 1;
  line3[1].terminal[1].x = line3[1].terminal[0].x + line3[1].terminal[0].x;
  printf("pa[0] = [%d,%d]\n",pa[0].x,pa[0].y);
  line3[1].terminal[1].y = line3[1].terminal[0].y + line3[1].terminal[0].y;

  printf("line1[1] = [%d,%d]\n",line1[1].x,line1[1].y);
  printf("line2 = [[%d,%d],[%d,%d]]\n",
    line2.from.x,line2.from.y,line2.to.x,line2.to.y);
  printf("line3[1] = [[%d,%d],[%d,%d]]\n",
    line3[1].terminal[0].x,line3[1].terminal[0].y,
    line3[1].terminal[1].x,line3[1].terminal[1].y);

  return 0;
}

