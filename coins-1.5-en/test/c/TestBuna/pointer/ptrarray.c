int printf(char* f, ...);

int main(int argc, char **argv) {
  char *lines[2];
  char *p;
  lines[0] = "Hello World.";
  lines[1] = "Hello Japan.";

  p = lines[0];
  lines[0] = lines[1];
  lines[1] = p;
  printf("%s\n%s\n", lines[0], lines[1]);
}
