int printf(char *s, ...);

int main() {
  printf("%d %d %d\n",(int)sizeof "ABCD",(int)sizeof *"ABCD","ABCD"[2]);
  return 0;
}
