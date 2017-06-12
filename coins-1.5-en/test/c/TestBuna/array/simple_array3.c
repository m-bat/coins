int printf(char *, ...);

int main(int args, char *argv[]){
  short sa1[5];
  int i;

  for (i = -5; i < 0; i++){
    sa1[i+5] = i;
  }

  printf( "1:%d 2:%d 3:%d 4:%d 5:%d\n", sa1[0], sa1[1], sa1[2], sa1[3], sa1[4]);
  printf( "Total=%d\n", sa1[0]+sa1[1]+sa1[2]+sa1[3]+sa1[4]);

  return 0;
}