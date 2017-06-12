int printf(char *, ...);

int main(int args, char *argv[]){
  int i, j, k;
  float f1[3][4][5];

  for(i = 0; i < 3; i++){
    for(j = 0; j < 4; j++){
      for(k = 0; k < 5; k++){
        f1[i][j][k] = i+j+k*0.0001;
        printf("[%d][%d][%d] = %f\n", i, j, k, f1[i][j][k]);
      }
    }
  }

  return 0;
}