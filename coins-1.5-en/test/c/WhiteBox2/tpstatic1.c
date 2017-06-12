/* tpstatic1.c:  */
/*    Error found in SPAEC CPU2000 Fukuda mail 040209 */ 

int printf(char *, ...);

  int g;
  static int x;
int main()
{
  static int y;
  int  z;
  x = 1;
  y = 2;
  z = 3;
  g = 4;
  printf("%d %d %d %d\n", x, y, z, g);
}
