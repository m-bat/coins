/* tploopexp8.c */
/*   Loop expansion test (with sawitch) */

int printf(char*, ...);

int main()
{
  int i = 0, j;
  int s = 0;
  for (j = 0; j < 20; j= j+2) {
    switch (j) {
    case 10: s = s + j*10;
    default: s = s + j;
    }
  }
  printf("\n%d ", s);
  for (j = 0; j < 20; j= j+2) {
    switch (j) {
    case 10: s = s + j*10;
             break;
    default: s = s + j;
    }
  }
  printf("\n%d ", s);
  return 0;
}
