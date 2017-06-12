int printf(char *s, ...);

int i0=(-1)>>5,i1=(-02)>>5,i2=(-0x3)>>5;
unsigned int ui0=(-1U)>>5,ui1=(-02U)>>5,ui2=020000000000>>5,ui3=(-0x3U)>>5,ui4=0x90000000>>5;
long l0=(-1L)>>5,l1=(-02L)>>5,l2=(-0x3L)>>5;
unsigned long ul0=(-1UL)>>5,ul1=(-02UL)>>5,ul2=020000000000L>>5,ul3=(-0x3UL)>>5,ul4=0x90000000L>>5;
double d0=-4U,d1=030000000000,d2=0xA0000000,d3=-4UL,d4=030000000000L,d5=0xA0000000L;

int main() {
  printf("%08X %08X %08X\n",i0,i1,i2);
  printf("%08X %08X %08X %08X %08X\n",ui0,ui1,ui2,ui3,ui4);
  printf("%08lX %08lX %08lX\n",l0,l1,l2);
  printf("%08lX %08lX %08lX %08lX %08lX\n",ul0,ul1,ul2,ul3,ul4);
  printf("%g %g %g %g %g %g\n",d0,d1,d2,d3,d4,d5);
  return 0;
}
