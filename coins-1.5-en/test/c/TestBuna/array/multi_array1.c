int printf(char *, ...);

int main(int args, char *argv[]){
  int i,j;
  int i1[5][5];

  for(i = 0; i < 5; i++){
    for(j = 0; j < 5; j++){
      i1[i][j] = i+j;
      printf("[%d][%d] = %d\n", i, j, i1[i][j]);
    }
  }

  return 0;
}