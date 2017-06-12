int printf(char *, ...);

int main(int args, char *argv[]){
  int i,j,k,l;
  long f1[2][3][4][5];

  for(i = 0; i < 2; i++){
    for(j = 0; j < 3; j++){
      for(k = 0; k < 4; k++){
        for(l = 0; l < 5; l++){
          f1[i][j][k][l] = i*100000+j*100+k*10+l;
          printf("[%d][%d][%d][%d] = %d\n", i, j, k, l, f1[i][j][k][l]);
        }
      }
    }
  }

  return 0;
}