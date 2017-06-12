/* tpstructArray3.c:  struct array with increment 1 */

int printf(char*, ...);

int i;
struct point {
  int x;
  int y;
};

struct point pa[10];

int add( int p ) 
{
  return p+1;
}

int main()
{
  i = 1;
  pa[1].x = 1;
  pa[1].y = 1;
  pa[i].x = pa[1].y;
  pa[i].y+=1;
  pa[add(0)].y++;

  printf("pa[1] = [%d,%d]\n",pa[1].x,pa[1].y); /* SF030609 */

  return 0;
}

