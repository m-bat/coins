int printf(char *s, ...);

int main() {
  unsigned int a=sizeof(long long [0x18000000]);
  printf("%u\n",a);
  return 0;
}
