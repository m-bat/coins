/* ptrcmp_01.c Pointer assignment and comparison */

int printf(char*, ... );

int main(int argc, char** argv){
  int x, y, z;
  int *ip;
  x = 1;
  y = 2;
  z = 3;
  ip = &z;
  *ip = 0;
  
  if (ip == 0) {
      ip = &x;
  }
  y = *ip;
  *ip = 0;

  printf("x=%d y=%d\n", x, y);
  return 0;
}
