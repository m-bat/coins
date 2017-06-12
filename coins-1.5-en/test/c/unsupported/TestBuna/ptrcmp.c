int printf(char*, ...);

int main(int argc, char** argv){
  int x, y, z;
  int *ip;
  x = 1;
  y = 2;
  ip = &z;
  *ip = 0;
  
  if (ip != 0) {
      ip = &x;
  }
  y = *ip;
  *ip = 0;

  printf("x=%d y=%d z=%d\n", x, y, z);
  return 0;
}
