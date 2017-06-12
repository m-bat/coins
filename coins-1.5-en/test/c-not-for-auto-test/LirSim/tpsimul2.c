/* tpsimul2.c  LIR simulator test 2 */
#pragma simulate profileOff initiate

  int g1, g2;
  int ga1[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  int ga2[5], g3, g4;
int printf( char*, ... );

void initiate( int *param1, int *param2 ) {
  ga2[0] = 11;
  ga2[1] = 12;
  ga2[3] = 13;
  *param1 = *param2 ;
}

int main() {
  int i, j1, j2, aa[10], bb[5], j3, j4;
  g1 = 10;
  g2 = 20; 
  j1 = 30;
  j2 = 31;
  initiate(&j3, &j1);
  g3 = j1 + j3;
  initiate(&j4, &j2);
  g4 = j2 + j4;
  aa[0] = j1;
  for (i = 1; i < 10; i++) {
    aa[i] = aa[i-1] + ga1[i] + i + 16;
  } 
  for (i = 0; i < 5; i++) {
    bb[i] = aa[i] + aa[i+5] + ga2[i] + 17;
  }
  printf("bb %d %d %d %d %d \n", bb[0], bb[1], bb[2], bb[3], bb[4]);
  return 0;
}
