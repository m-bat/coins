/* mbunsign1.c: mb-gcc test */ 
/*   Unsigned operation */ 

int printf(char*, ...);
int main()
{
  char  c1, c2, c3, c4;
  short s1, s2, s3, s4;
  int   i1, i2, i3, i4;
  unsigned char  uc1, uc2, uc3, uc4; 
  unsigned short us1, us2, us3, us4; 
  unsigned int   ui1, ui2, ui3, ui4; 
  i1 = 1;
  s1 = 1;
  c1 = '9';
  c2 = c1 - '0' + 1; 
  c3 = c2 * 4;
  c4 = c3 / 2;
  uc1 = '9';
  uc2 = uc1 - '0' + 1; 
  uc3 = uc2 * 4;
  uc4 = uc3 / 2;
  while (uc2 < uc3) {
    uc2 = uc2 + 1;
  }
  printf("uc1 %d uc2 %d uc3 %d uc4 %d uc1+c1 %d uc3*c2 %d\n",
      uc1, uc2, uc3, uc4, uc1+c1, uc3*c2);
  s1 = 9;
  s2 = s1 - c2 + 2; 
  s3 = s2 * 4;
  s4 = s3 / 2;
  us1 = 9;
  us2 = us1 + uc2 - 1; 
  us3 = us2 * 4;
  us4 = us3 / 2;
  while (us2 < us3) {
    us2 = us2 + 1;
  }
  printf("us1 %d us2 %d us3 %d us4 %d us1+s1 %d us3*s2 %d\n",
      us1, us2, us3, us4, us1+s1, us3*s2);
  i1 = 9;
  i2 = i1 - c2 + 2; 
  i3 = i2 * 4;
  i4 = i3 / 2;
  ui1 = 9;
  ui2 = ui1 + uc2 - 1; 
  ui3 = ui2 * 4;
  ui4 = ui3 / 2;
  while (ui2 < ui3) {
    ui2 = ui2 + 1;
  }
  printf("ui1 %d ui2 %d ui3 %d ui4 %d ui1+i1 %d ui3*i2 %d\n",
      ui1, ui2, ui3, ui4, ui1+i1, ui3*i2);
  return 0;
} 

