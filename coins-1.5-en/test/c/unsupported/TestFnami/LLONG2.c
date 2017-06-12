int printf(char *s, ...);

long long a;

int main() {
  a=0x8000000000000000;
  printf("%x\n",(int)(a>>32));
  printf("%x\n",(int)a);
  return 0;
}
