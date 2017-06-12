int printf(char *s, ...);

int main() {
  unsigned long long a=18446744073709551615;
  printf("%x\n",(int)(a>>32));
  printf("%x\n",(int)a);
  return 0;
}
