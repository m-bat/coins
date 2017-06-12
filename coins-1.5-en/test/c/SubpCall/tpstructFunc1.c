/* tpstructFunc1.c  -- function returning structure */

struct point {
  int x;
  int y;
};

struct point p1;

struct point point(int x, int y)
{
  struct point p10;
  p10.x = x;
  p10.y = y;
  return p10;
}

int main()
{
  struct point p20;
  p20 = point(10, 20); 
  p1  = p20;
  printf("x %d y %d \n", p1.x, p1.y);
  return 0;
}

