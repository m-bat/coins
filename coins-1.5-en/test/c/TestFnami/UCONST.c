int printf(char *s, ...);

int main() {
  printf("%u %u\n",(unsigned)(2147483648/2),2147483648U/2);
  printf("%x %x\n",0x87654321/2,0x87654321U/2);
  return 0;
}
