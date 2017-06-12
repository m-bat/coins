/* tploopexp1.c */
/*   Loop expansion test */

int printf(char*, ...);

int main()
{
  int i = 0, j;
  int s = 0;
  while (i <= 10) {
    s = s + i;
    i = i + 1;
  }
  printf("\n%d ", s);

  for (i = 0; i<10; i++) {
    for (j = 0; j < 5; j++) {
      s = s + j;
    }       
  }
  printf("\n%d ", s);
}
