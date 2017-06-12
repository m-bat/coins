/* tpfoo030409mugi.c  Structure assignment */

int printf(char*, ...);

struct point
{
	int x;
	int y;
};

struct rect
{
	struct point pt1;
	struct point pt2;
};
#define min(a, b) ((a) < (b) ? (a) : (b))
#define max(a, b) ((a) > (b) ? (a) : (b))

struct rect canonrect(struct rect r)
{
	struct rect temp;
	temp.pt1.x = min(r.pt1.x, r.pt2.x);
	temp.pt1.y = min(r.pt1.y, r.pt2.y);
	temp.pt2.x = max(r.pt1.x, r.pt2.x);
	temp.pt2.y = max(r.pt1.y, r.pt2.y);
	return temp;
};
int main() 
{
  struct rect rect1 = { { 0, 0 }, {1, 2} };
  struct rect rect2 = { { -1, 0 }, { 3, 4} };
  struct rect rect3;
  rect3 = rect1;
  printf(" %d %d %d %d \n", rect3.pt1.x, rect3.pt1.y, rect3.pt2.x, rect3.pt2.y);
  rect3 = canonrect(rect1);
  printf(" %d %d %d %d \n", rect3.pt1.x, rect3.pt1.y, rect3.pt2.x, rect3.pt2.y);
  rect3 = canonrect(rect2);
  printf(" %d %d %d %d \n", rect3.pt1.x, rect3.pt1.y, rect3.pt2.x, rect3.pt2.y);
  return 0;
}
