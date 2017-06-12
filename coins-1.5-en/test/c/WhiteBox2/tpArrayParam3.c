/* tpArrayParam3.c -- Array parameter and pointer to array parameters */ 


int printf(char*, ...);
int a[6], aa[5][6], aaa[4][5][6];

int 
sub( int pa[6], int paa[5][6], int paaa[4][5][6], 
     int *ppa, int ppaa[][6], int ppaaa[][5][6] )
{
  int i, j, k;
  for (i = 1; i < 6; i++) {
    pa[i] = pa[i-1]+i;
    *(ppa+i) = *(ppa+i-1) + i;
    for (j = 0; j < 5; j++) {
      paa[j][i] = paa[j][i-1] + j;
      *(ppaa[j]+i) = *(ppaa[j]+i-1) + j;
      for (k = 0; k < 4; k++) {
        paaa[k][j][i] = paaa[k][j][i-1] + j+ k;
        *(ppaaa[k][j]+i) = *(ppaaa[k][j]+i-1) + j + k; 
      }
    }
  }
}
int main()
{
  int i, j, k;
  int b[6], bb[5][6], bbb[4][5][6];
  a[0] = 1;
  b[0] = 10;
  for (j = 0; j < 5; j++) {
    aa[j][0] = 10;
    bb[j][0] = 20;
    for (k = 0; k < 4; k++) {
      aaa[k][j][0] = 10;
      bbb[k][j][0] = 100;
    }
  }
  sub(a, aa, aaa, b, bb, bbb);
  for (i = 0; i < 6; i++) {
    printf("\n %d %d ", a[i], b[i] );
    for (j = 0; j < 5; j++) {
      printf("\n  %d %d ", aa[j][i], bb[j][i]);
      for (k = 0; k < 4; k++) {
        printf(" %d %d ", aaa[k][j][i], bbb[k][j][i]);
      }
    }
    
  }
  return 0; 
}



