int printf(char *s, ...);

int main() {
  typedef int array[0x30000000];
  unsigned int a=sizeof(array);
  printf("%u\n",a);
  return 0;
}
