/* mbarith1.c: Arithmetic operations */ 

int printf(char*, ...);
int main()
{
  char  c1, c2, c3, c4, c5, c6, c7, c8;
  short s1, s2, s3, s4, s5, s6, s7, s8;
  int   i1, i2, i3, i4, i5, i6, i7, i8;
  unsigned char  uc1, uc2, uc3, uc4, uc5, uc6, uc7, uc8;
  unsigned short us1, us2, us3, us4, us5, us6, us7, us8;
  unsigned int   ui1, ui2, ui3, ui4, ui5, ui6, ui7, ui8;
  c1 = -1;
  c2 = '2' - '0';
  c3 = c2 * c2;
  c4 = c3 / c2;
  c5 = c3 % c2;
  c6 = (c3 << 3) + (c3 << c2) + (c3 >> 2) + (c3 >> c2);
  c7 = c6 & c1 ^ c2;
  c8 = c7 | c3;
  printf("c1 %d c2 %d c3 %d c4 %d c5 %d c6 %d c7 %d c8 %d -c8 %d ~c8 %d\n",
    c1, c2, c3, c4, c5, c6, c7, c8, -c8, ~c8); 
  s1 = -1;
  s2 = 2;
  s3 = s2 * s2;
  s4 = s3 / s2;
  s5 = s3 % s2;
  s6 = (s3 << 3) + (s3 << s2) + (s3 >> 2) + (s3 >> s2);
  s7 = s6 & s1 ^ s2;
  s8 = s7 | s3;
  printf("s1 %d s2 %d s3 %d s4 %d s5 %d s6 %d s7 %d s8 %d -s8 %d ~s8 %d\n",
    s1, s2, s3, s4, s5, s6, s7, s8, -s8, ~s8); 
  i1 = -1;
  i2 = '2' - '0';
  i3 = i2 * i2;
  i4 = i3 / i2;
  i5 = i3 % i2;
  i6 = (i3 << 3) + (i3 << i2) + (i3 >> 2) + (i3 >> i2);
  i7 = i6 & i1 ^ i2;
  i8 = i7 | i3;
  printf("i1 %d i2 %d i3 %d i4 %d i5 %d i6 %d i7 %d i8 %d -i8 %d ~i8 %d\n",
    i1, i2, i3, i4, i5, i6, i7, i8, -i8, ~i8); 
  uc1 = -1;
  uc2 = '2' - '0';
  uc3 = uc2 * uc2;
  uc4 = uc3 / uc2;
  uc5 = uc3 % uc2;
  uc6 = (uc3 << 3) + (uc3 << uc2) + (uc3 >> 2) + (uc3 >> uc2);
  uc7 = uc6 & uc1 ^ uc2;
  uc8 = uc7 | uc3;
  printf("uc1 %d uc2 %d uc3 %d uc4 %d uc5 %d uc6 %d uc7 %d uc8 %d -uc8 %d ~uc8 %d\n",
    uc1, uc2, uc3, uc4, uc5, uc6, uc7, uc8, -uc8, ~uc8); 
  us1 = -1;
  us2 = '2' - '0';
  us3 = us2 * us2;
  us4 = us3 / us2;
  us5 = us3 % us2;
  us6 = (us3 << 3) + (us3 << us2) + (us3 >> 2) + (us3 >> us2);
  us7 = us6 & us1 ^ us2;
  us8 = us7 | us3;
  printf("us1 %d us2 %d us3 %d us4 %d us5 %d us6 %d us7 %d us8 %d -us8 %d ~us8 %d\n",
    us1, us2, us3, us4, us5, us6, us7, us8, -us8, ~us8); 
  ui1 = -1;
  ui2 = '2' - '0';
  ui3 = ui2 * ui2;
  ui4 = ui3 / ui2;
  ui5 = ui3 % ui2;
  ui6 = (ui3 << 3) + (ui3 << ui2) + (ui3 >> 2) + (ui3 >> ui2);
  ui7 = ui6 & ui1 ^ ui2;
  ui8 = ui7 | ui3;
  printf("ui1 %d ui2 %d ui3 %d ui4 %d ui5 %d ui6 %d ui7 %d ui8 %d -ui8 %d ~ui8 %d\n",
    ui1, ui2, ui3, ui4, ui5, ui6, ui7, ui8, -ui8, ~ui8); 
  i1  = c1 - c2;
  c2  = i1 - i2;
  s1  = c1 + c2;
  c3  = s1 - s2;
  i2  = s1 + s2;
  s2  = i2 + i1;
  printf("i1 %d i2 %d s1 %d s2 %d c2 %d c3 %d \n", i1, i2, s1, s2, c2, c3);
  c4  = uc1 + uc2;
  uc4 = c1  + c2; 
  s3  = us1 - us2;
  us3 = s1  + s2;
  i3  = ui1 - ui2;
  ui3 = i1  + i2;
  printf("c4 %d uc4 %d s3 %d us3 %d i3 %d ui3 %d \n", 
          c4, uc4, s3, us3, i3, ui3); 
  return 0;
} 

