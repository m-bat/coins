int printf(char*, ...);

int main(int argc, char **argv) {
  char *lines[2];
  char **p;
  char *q;
  lines[0] = "Hello World.";
  lines[1] = "Hello Japan.";
  
  p = lines;
  q = *p;
  *p = *(p+1);
  *(p+1) = q;
  printf("%s\n%s\n", *p, *(p+1));
}
