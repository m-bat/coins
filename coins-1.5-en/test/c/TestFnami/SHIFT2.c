int printf(char *s, ...);

int main() {
  unsigned a=-1U;
  a>>=31;
  printf("%u\n",a);
  return 0;
}
