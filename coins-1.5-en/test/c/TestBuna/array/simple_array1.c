int printf(char *, ...);

int main(int args, char *argv[]){
  long la1[5];

  la1[0] = 0;
  la1[1] = 1;
  la1[2] = 2;
  la1[3] = 3;
  la1[4] = 4;

  printf( "1:%d 2:%d 3:%d 4:%d 5:%d\n", la1[0], la1[1], la1[2], la1[3], la1[4]);
  printf( "Total=%d\n", la1[0]+la1[1]+la1[2]+la1[3]+la1[4]);

  return 0;
}