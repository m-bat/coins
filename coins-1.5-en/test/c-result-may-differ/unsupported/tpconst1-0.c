/* Exp/tpconst1-0.c:  Constants (out of range) */

int    i1, i2, i3;
float  f1, f2, f3;
double d1, d2, d3;
char   c1, c2, c3, c4, c5, c6, c7, c8, c9;
char*  s1,*s2,*s3,*s4,*s5,*s6,*s7,*s8;
long   l1, l2, l3;
int    h1, h2, h3;
int    o1, o2, o3;
int main()
{
  i1 = 2147483647;
  i2 = -2148483648;
  i3 = -1 + -2 + 5L + 8u;
  f1 = 3.14;
  f2 = 1.0e6;
  f3 = 1.23456e-3 + 3e0f + 4.f -2.5F + 1.2345678901L;
  d1 = +1.2345678901L;
  d2 = 1.2E+5 + 3.2e3l;
  c1 = '$' + '*';
  c2 = '"' + '"';
  c3 = c2 + 'x';
  c4 = '\'';
  c5 = '\n';
  c6 = '\xff';
  s1 = "";
  s2 = "char_string";
  s3 = " ";
  /* s4 = L"ab"; */
  s5 = "%d\n";
  s6 = "==";
  s7 = "quote\"test";
  s8 = "";
  l1 = 2147483648ul;
  l2 = 12345678901L;
  h1 = 0xffff;
  h2 = 0X12345678;
  h3 = 0x123456789abcdefUL;
  o1 = 07777u;
  o2 = 0123;
  /* SF030620[ */
  printf("i1=%d, i2=%d, i3=%d\n",i1, i2, i3);
  printf("f1=%f, f2=%f, f3=%f\n",f1, f2, f3);
  printf("d1=%f, d2=%f, d3=%f\n",d1, d2, d3);
  printf("c1=%d, c2=%d, c3=%d, c4=%d, c5=%d, c6=%d, c7=%d, c8=%d, c9=%d\n",
	 c1, c2, c3, c4, c5, c6, c7, c8, c9);
  printf("*s1=%s, *s2=%s,*s3=%s,*s5=%s,*s6=%s,*s7=%s,*s8=%s\n",
	 s1, s2, s3, s5, s6, s7, s8);
  printf("l1=%l, l2=%l, l3=%l\n",l1, l2, l3);
  printf("h1=%x, h2=%x, h3=%x\n",h1, h2, h3);
  printf("o1=%o, o2=%o, o3=%o\n",o1, o2, o3);
  /* SF030620] */
  return 0;
}
