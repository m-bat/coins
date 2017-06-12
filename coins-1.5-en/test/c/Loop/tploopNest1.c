/* tploopNest1.c:  PRE test by nested loop (Igarashi) */

int main()
{
  int i, j, k;
  int a[10], b[10];

  for (i = 0; i < 10; i++) {
      a[i] = 0;
      b[i] = 0;
  }

  for (i = 0; i < 10; i++) {

    for (j = 0; j < 11; j++) {
      a[i] = a[i] + j;
    }

    for (k = 0; k < 12; k++) {
      b[i] = b[i] + k;
    }
  }
  /* SF030620[ */
  printf("a = {%d,%d,%d,%d,%d,%d,%d,%d,%d,%d}\n",
    a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9]);
  printf("b = {%d,%d,%d,%d,%d,%d,%d,%d,%d,%d}\n",
    b[0],b[1],b[2],b[3],b[4],b[5],b[6],b[7],b[8],b[9]);
  /* SF030620] */
}
