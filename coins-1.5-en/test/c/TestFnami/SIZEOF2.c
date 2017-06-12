int printf(char *s, ...);

int main() {
  unsigned int a=sizeof(int [0x30000000]);
  printf("%u\n",a);
  return 0;
}
