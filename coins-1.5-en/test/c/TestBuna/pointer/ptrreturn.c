int printf(char*, ...);

char *substring(char *str, int start) {
  return str+start;
}

int main(int argc, char **argv) {
  char *str;
  str = "Hello World.";
  printf("%s\n", substring(str,6));
}
