int printf(char *s, ...);

int main() {
  printf("%d\n",1234567890);
  printf("%u\n",1234567890U);
  printf("%u\n",3456789012U);
  printf("%ld\n",1234567890L);
  printf("%lu\n",1234567890UL);
  printf("%lu\n",3456789012UL); /*##*/
  printf("%lu\n",3456789012LU);
  printf("%d\n",017777777777);
  printf("%u\n",020000000000);
  printf("%u\n",037777777777);
  printf("%u\n",017777777777U);
  printf("%u\n",020000000000U);
  printf("%u\n",037777777777U);
  printf("%ld\n",017777777777L);
  printf("%lu\n",020000000000L);
  printf("%lu\n",037777777777L);
  printf("%lu\n",017777777777UL);
  printf("%lu\n",020000000000LU);
  printf("%lu\n",037777777777UL);
  printf("%d\n",0x7FFFFFFF);
  printf("%u\n",0x80000000);
  printf("%u\n",0xFFFFFFFF);
  printf("%u\n",0x7FFFFFFFU);
  printf("%u\n",0x80000000U);
  printf("%u\n",0xFFFFFFFFU);
  printf("%ld\n",0x7FFFFFFFL);
  printf("%lu\n",0x80000000L);
  printf("%lu\n",0xFFFFFFFFL);
  printf("%lu\n",0x7FFFFFFFUL);
  printf("%lu\n",0x80000000LU);
  printf("%lu\n",0xFFFFFFFFUL);
  return 0;
}
