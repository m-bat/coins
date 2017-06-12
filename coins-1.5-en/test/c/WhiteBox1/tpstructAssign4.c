/* tpStructAssign4.c:  Structure assignment */

int printf(char*, ...);

int a, b, c;

int main()
{
  struct { int x; int y; } point1, point2;

  point1.x = 1;
  point1.y = point1.x + 1;
  point2 = point1; 
  printf("point2 = {%d,%d}\n",point2.x,point2.y);
  return 0;
}
