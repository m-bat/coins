int printf(char *s, ...);

void fi(int c) {
  printf("%08X %08X %08X\n",(-1)>>c,(-02)>>c,(-0x3)>>c);
}

void fui(int c) {
  printf("%08X %08X %08X %08X %08X\n",(-1U)>>c,(-02U)>>c,020000000000>>c,(-0x3U)>>c,0x90000000>>c);
}

void fl(int c) {
  printf("%08lX %08lX %08lX\n",(-1L)>>c,(-02L)>>c,(-0x3L)>>c);
}

void ful(int c) {
  printf("%08lX %08lX %08lX %08lX %08lX\n",(-1UL)>>c,(-02UL)>>c,020000000000L>>c,(-0x3UL)>>c,0x90000000L>>c);
}

int main() {
  int c;

  c=5;
  printf("%08X %08X %08X\n",(-1)>>c,(-02)>>c,(-0x3)>>c);
  printf("%08X %08X %08X\n",(-1)>>c,(-02)>>c,(-0x3)>>c);
  fi(5);

  c=5;
  printf("%08X %08X %08X %08X %08X\n",(-1U)>>c,(-02U)>>c,020000000000>>c,(-0x3U)>>c,0x90000000>>c);
  printf("%08X %08X %08X %08X %08X\n",(-1U)>>c,(-02U)>>c,020000000000>>c,(-0x3U)>>c,0x90000000>>c);
  fui(5);

  c=5;
  printf("%08lX %08lX %08lX\n",(-1L)>>c,(-02L)>>c,(-0x3L)>>c);
  printf("%08lX %08lX %08lX\n",(-1L)>>c,(-02L)>>c,(-0x3L)>>c);
  fl(5);

  c=5;
  printf("%08lX %08lX %08lX %08lX %08lX\n",(-1UL)>>c,(-02UL)>>c,020000000000L>>c,(-0x3UL)>>c,0x90000000L>>c);
  printf("%08lX %08lX %08lX %08lX %08lX\n",(-1UL)>>c,(-02UL)>>c,020000000000L>>c,(-0x3UL)>>c,0x90000000L>>c);
  ful(5);

  return 0;
}
