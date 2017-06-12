int printf(char* f, ...);

int main(int argc, char **argv) {
  char *p;
  char buf[16];
  p = "Hellow World.";
  
  buf[0] = *p;
  buf[1] = *(p+1);
  buf[2] = *(p+2);
  buf[3] = *(p+3);
  buf[4] = *(p+4);
  buf[5] = '\0';
  printf("buf=%s\n", buf);
}
