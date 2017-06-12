int printf(char*, ...);

int main(int argc, char **argv) {
  char *s;
  char *p;
  int n;
  s = "Hello World.";
  
  for(n=0, p=s; *p != '\0'; ++p) {
    ++n;
  }
  printf("%s length=%d\n", s, n);
}
