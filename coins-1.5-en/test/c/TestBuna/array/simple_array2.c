int printf(char *, ...);

int main(int args, char *argv[]){
  int ia1[5];
  int i;

  for (i = 0; i < 5; i++){
    ia1[i] = i;
  }

  printf( "1:%d 2:%d 3:%d 4:%d 5:%d\n", ia1[0], ia1[1], ia1[2], ia1[3], ia1[4]);
  printf( "Total=%d\n", ia1[0]+ia1[1]+ia1[2]+ia1[3]+ia1[4]);

  return 0;
}