int printf(char*, ...);

int main(int argc, char **argv) {
  float data[6];
  float *p;
  float n;

  data[0] = 1.1;
  data[1] = 2.2;
  data[2] = 3.3;
  data[3] = 4.4;
  data[4] = 5.5;
  data[5] = 0.0;
  
  for(n=0, p=data; *p > 0; ++p) {
    n += *p;
  }
  printf("answer=%f\n", n);
}
