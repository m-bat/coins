/* tptypedef3-1.c:  local typedef test (basic 3) */

typedef int integer;
integer a, b, c;
int     x;
integer y[3], z;
/* typedef integer length; */
/* typedef struct { integer w; integer h; } rectangle; */
int main()
{
  typedef integer length;
  typedef struct { length w; length h; } rectangle;
  length leng1;
  rectangle rect1;
  leng1 = 3;
  rect1.w = 2;
  rect1.h = 3;
  a = 1;
  x = a - 2;
  y[2] = x;

  printf("a,x,leng1 = %d,%d,%d\n",a,x,leng1); /* SF030509 */
  printf("rect1.w,rect1.y = %d,%d\n",rect1.w,rect1.h); /* SF030509 */
  printf("y[2] = %d\n",y[2]); /* SF030509 */

  return x;
}

