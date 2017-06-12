/* mbconst1.c: mb-gcc test 1 */ 
/*   Constant */ 

int printf(char*, ...);
int main()
{
  char  c1, c2, c3, c4;
  short s1, s2, s3, s4;
  int   i1, i2, i3, i4;
  unsigned char  uc1, uc2, uc3, uc4; 
  unsigned short us1, us2, us3, us4; 
  unsigned int   ui1, ui2, ui3, ui4; 
  c1 = '0';
  s1 = 1;
  i1 = 1;
  c2 = 127;        /* 2**07-1 */
  s2 = 32767;      /* 2**15-1 */
  i2 = 2147483647; /* 2**31-1 */
  c3 = 255;        /* 2**08-1 */
  s3 = 65535;      /* 2**16-1 */
  i3 = 4294967295; /* 2**32-1 */
  c4 = -128;       /*-2**07   */
  s4 = -32768;     /*-2**15   */
  i4 = 2147483648; /*-2**31   */
  uc1= 255;        /* 2**08-1 */
  us1= 65535;      /* 2**16-1 */
  ui1= 4294967295; /* 2**32-1 */
  uc2= c1 + c2 + c3 + c4;
  us2= s1 + s2 + s3 + s4;
  ui2= i1 + i2 + i3 + i4;
  printf("uc2=%d us2=%d ui2=%d \n", uc2, us2, ui2);
  return 0;
} 

