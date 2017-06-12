/* tploopNest2.c PRE test by nested loop (Igarashi) */

int main()
{
  int i, j, k, l, m, n, o;
  int a, b, c, d, x, y, z;

  a = 1;
  b = 2;
  c = 3;
  d = 4;

  x = a + b;
  y = c + d;

  for (i = 0; i < 10; i++) {
    a = i;
    for (j = 0; j < 11; j++) {
      for (k = 0; k < 12; k++) {
      }
    }
    
    c = i + 1;
    for (l = 0; l < 12; l++) {
      for (m = 0; m < 13; m++) {
      }
    }

    for (n = 0; n < 14; n++) {
      for (o = 0; o < 15; o++) {
	x = a + b;
	y = c + d;
	z = x + y;
      }
    }
  }
  /* SF030620[ */
  printf("a=%d b=%d c=%d d=%d x=%d y=%d z=%d\n",
	 a,b,c,d,x,y,z);
  /* SF030620] */
}
