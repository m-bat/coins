int printf(char* f, ...);

int main(int argc, char **argv) {
  unsigned int data[6];
  unsigned int *p;
  unsigned int n;
  data[0] = 1;
  data[1] = 2;
  data[2] = 3;
  data[3] = 4;
  data[4] = 5;
  data[5] = 0;

  for(n=0, p=data; *p != 0; ++p) {
    n += *p;
  }
  printf("answer=%d\n", n);
}
