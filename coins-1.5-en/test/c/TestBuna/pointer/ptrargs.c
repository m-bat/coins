int printf(char* f, ...);

void swap(int *px, int *py) {
  int temp;

  temp = *px;
  *px = *py;
  *py = temp;
}

int main(int argc, char **argv) {
  int x;
  int y;
  x = 1;
  y = 2;
  
  swap(&x, &y);
  printf("x=%d y=%d\n", x, y);
  return 0;
}
