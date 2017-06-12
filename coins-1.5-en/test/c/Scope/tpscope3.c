/* tpscope3.c:  struct tag scope */

struct y {
  int x;
};

int y;

int main()
{

  int x;
  struct y z;
  x = 1;
  z.x = x;
  y = 1;
  printf("x,y,z = %d,%d,%d\n",x,y,z.x); /* SF030609 */
  return 0;
}

