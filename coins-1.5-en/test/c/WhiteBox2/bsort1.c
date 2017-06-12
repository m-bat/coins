/* bsort1: bubblesort local var */

int main()
{
  int i, j, nn, mm, w;
  int aa[1000];

  nn = 1000;

  for (i = 0; i < nn; i = i + 2) {
    aa[i] = i;
    aa[i+1] = nn - i;
  }

  i = 0;
  while (i < nn) {
    j = 0;
    while (j < nn-1) {
      if (aa[j] > aa[j+1]) {
        w = aa[j];
        aa[j] = aa[j+1];
        aa[j+1] = w;
      }
      j = j + 1;
    }
    i = i + 1;
  }

  mm = nn;
  if (mm > 100) 
    mm = 100;
  for (i = 0; i < mm; i++)
    printf(" %d", aa[i]);
  return 0; 
}


