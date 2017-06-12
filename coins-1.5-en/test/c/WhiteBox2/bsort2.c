/* bsort1: bubblesort local var using for-statement */

int printf(char*, ...);

int main()
{
  int i, j, nn, w;
  int aa[1000];

  for (i = 999; i >= 0; i=i-1) {
    aa[999-i] = i;
  }
    
  nn = 1000;
  for (i = 0; i < nn; i=i+1) {
    for (j = 0; j < nn-1; j=j+1) {
      if (aa[j] > aa[j+1]) {
        w = aa[j];
        aa[j] = aa[j+1];  /* aa[j], aa[j+1] are not redundant because */
        aa[j+1] = w;      /* they are changed in the same BBlock. */
      }
    }
  }
  for (i = 0; i < 100; i=i+1) {
    printf(" %d", aa[i]);
  }
}


