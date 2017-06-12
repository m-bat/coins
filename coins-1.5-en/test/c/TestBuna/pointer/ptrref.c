int printf(char*, ...);

int main(int argc, char** argv){
  int x;
  int y;
  int *ip;
  x = 1;
  y = 2;
  
  ip = &x;
  y = *ip;
  *ip = 0;

  printf("x=%d y=%d\n", x, y);
  return 0;
}
