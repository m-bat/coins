int printf(char *s, ...);

struct s {
  long x;
  char c;
} s;

int main() {
  unsigned int a=sizeof s;
  printf("%u\n",a);
  return 0;
}
