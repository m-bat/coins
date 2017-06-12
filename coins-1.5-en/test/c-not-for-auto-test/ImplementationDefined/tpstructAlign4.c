/* tpstructAlign4.c  structure alignment test (with array element) */

int printf(char*, ...);

struct cstr {
  char c1[3];
};

struct sstr {
  short s1[3];
};

struct istr {
  int i1[3];
};

struct lstr {
  long l1[3];
};

struct llstr {
  long long ll1[3];
};

struct dstr {
  double d1[3];
};

struct ldstr {
  long double ld1;
};

struct cstr2 {
  char c1[3];
};

struct sstr2 {
  char c1[3];
  int  s1[3];
};

struct istr2 {
  char c1[3];
  int i1[3];
};

struct lstr2 {
  char c1[3];
  long l1[3];
};

struct llstr2 {
  char c1[3];
  long long ll1[3];
};

struct dstr2 {
  char c1[3];
  double d1[3];
};

struct mix1 {
  char cc1;
  struct cstr cs1;
  char cc2;
  struct sstr ss1;
  char cc3;
  struct istr is1;
  char cc4;
  struct lstr ls1;
  char cc5;
  struct llstr lls1;
  char cc6;
  struct dstr ds1;
  char cc7;
};

struct mix2 {
  struct cstr2 cs2;
  char cc9;
  struct sstr2 ss2;
  char cc10;
  struct istr2 is2;
  char cc11;
  struct lstr2 ls2;
  char cc12;
  struct llstr2 lls2;
  char cc13;
  struct dstr2 ds2;
  char cc14;
};

int main()
{
  char cc1[3];
  struct cstr cs1[3];
  char cc2[3];
  struct sstr ss1[3];
  char cc3[3];
  struct istr is1[3];
  char cc4[3];
  struct lstr ls1[3];
  char cc5[3];
  struct llstr lls1[3];
  char cc6[3];
  struct dstr ds1[3];
  char cc7[3];
  struct mix1 m1;
  struct mix2 m2;

  int i, lengm1, lengm2, diff;

  cc1[0] = 'a';
  cs1[0].c1[0] = '0';
  cs1[1].c1[0] = '0';
  cc2[0] = 'a';
  ss1[0].s1[0] = 1;
  ss1[1].s1[0] = 1;
  cc3[0] = 'b';
  is1[0].i1[0] = 2;
  is1[1].i1[0] = 2;
  cc4[0] = 'c';
  ls1[0].l1[0] = 3;
  ls1[1].l1[0] = 3;
  cc5[0] = 'd';
  lls1[0].ll1[0] = 4;
  lls1[1].ll1[0] = 4;
  cc6[0] = 'e';
  ds1[0].d1[0] = 1.0;
  ds1[1].d1[0] = 1.0;
  cc7[0] = 'f';

  m1.cc1 = 'a';
  m1.cs1.c1[0] = '0';
  m1.cc2 = 'a';
  m1.ss1.s1[0] = 1;
  m1.cc3 = 'b';
  m1.is1.i1[0] = 2;
  m1.cc4 = 'c';
  m1.ls1.l1[0] = 3;
  m1.cc5 = 'd';
  m1.lls1.ll1[0] = 4;
  m1.cc6 = 'e';
  m1.ds1.d1[0] = 2.0;
  m1.cc7 = 'f';

  m2.cs2.c1[0] = '0';
  m2.cc9 = 'a';
  m2.ss2.c1[0] = 'a';
  m2.ss2.s1[0] = 1;
  m2.cc10 = 'a';
  m2.is2.c1[0] = 'a';
  m2.is2.i1[0] = 2;
  m2.cc11 = 'b';
  m2.ls2.c1[0] = 'a';
  m2.ls2.l1[0] = 3;
  m2.cc12 = 'c';
  m2.lls2.c1[0] = 'a';
  m2.lls2.ll1[0] = 4;
  m2.cc13 = 'd';
  m2.ds2.c1[0] = 'a';
  m2.ds2.d1[0] = 3.0;
  m2.cc14 = 'e';

  printf( "%c,%c,%c,%d,%c,%d,%c,%ld,%c,%lld,%c,%f,%c\n",
    cc1[0], cs1[0].c1[0], cc2[0], ss1[0].s1[0], cc3[0], is1[0].i1[0], cc4[0],
    ls1[0].l1[0], cc5[0], lls1[0].ll1[0], cc6[0], ds1[0].d1[0], cc7[0]);

  printf( "m1 = [%c,%c,%c,%d,%c,%d,%c,%ld,%c,%lld,%c,%f,%c]\n",
    m1.cc1, m1.cs1.c1[0], m1.cc2, m1.ss1.s1[0], m1.cc3, 
    m1.is1.i1[0], m1.cc4,
    m1.ls1.l1[0], m1.cc5, m1.lls1.ll1[0], m1.cc6, m1.ds1.d1[0], m1.cc7 );

  printf( "m1 = {%d,%d,%d,%d,%d,%d,%d,%ld,%c,%lld,%d,%f,%d}\n",
    m1.cc1, m1.cs1.c1[0], m1.cc2, m1.ss1.s1[0], m1.cc3, 
    m1.is1.i1[0], m1.cc4,
    m1.ls1.l1[0], m1.cc5, m1.lls1.ll1[0], m1.cc6, m1.ds1.d1[0], m1.cc7 );

  printf( "m2 = [%c,%c,%c,%d,%c,%c,%d,%c,%c,%ld,%c,%c,%lld,%c,%c,%f,%c]\n",
    m2.cs2.c1[0], m2.cc9, m2.ss2.c1[0], m2.ss2.s1[0], m2.cc10, m2.is2.c1[0],
    m2.is2.i1[0], m2.cc11, m2.ls2.c1[0], m2.ls2.l1[0], m2.cc12, 
    m2.lls2.c1[0], m2.lls2.ll1[0], m2.cc13, m2.ds2.c1[0], m2.ds2.d1[0], 
    m2.cc14 );

  printf( "m2 = {%d,%d,%d,%d,%d,%d,%d,%d,%d,%ld,%d,%d,%lld,%d,%d,%f,%d}\n",
    m2.cs2.c1[0], m2.cc9, m2.ss2.c1[0], m2.ss2.s1[0], m2.cc10, m2.is2.c1[0],
    m2.is2.i1[0], m2.cc11, m2.ls2.c1[0], m2.ls2.l1[0], m2.cc12, 
    m2.lls2.c1[0], m2.lls2.ll1[0], m2.cc13, m2.ds2.c1[0], m2.ds2.d1[0], 
    m2.cc14 );

  diff = &cc7[2] - &cc1[0];
  lengm1 = sizeof(m1);
  lengm2 = sizeof(m2);

  /* Print contents after filling all spaces. */
  printf("diff cc7-cc1 = %d, sizeof(m1) = %d  sizeof(m2) = %d \n" ,
          diff, lengm1, lengm2);

  for (i = 0; i < lengm1-8; i++)
    m1.cs1.c1[i] = i+32; 
  printf( "m1 = [%c,%c,%c,%d,%c,%d,%c,%ld,%c,%lld,%c,%f,%c]\n",
    m1.cc1, m1.cs1.c1[0], m1.cc2, m1.ss1.s1[0], m1.cc3, 
    m1.is1.i1[0], m1.cc4,
    m1.ls1.l1[0], m1.cc5, m1.lls1.ll1[0], m1.cc6, m1.ds1.d1[0], m1.cc7 );

  printf( "m1 = {%d,%d,%d,%d,%d,%d,%d,%ld,%d,%lld,%d,%x,%d}\n",
    m1.cc1, m1.cs1.c1[0], m1.cc2, m1.ss1.s1[0], m1.cc3, 
    m1.is1.i1[0], m1.cc4,
    m1.ls1.l1[0], m1.cc5, m1.lls1.ll1[0], m1.cc6, m1.ds1.d1[0], m1.cc7 );

  for (i = 0; i < lengm2; i++)
    m2.cs2.c1[i] = i+32; 
  printf( "m2 = [%c,%c,%c,%d,%c,%c,%d,%c,%c,%ld,%c,%c,%lld,%c,%c,%f,%c]\n",
    m2.cs2.c1[0], m2.cc9, m2.ss2.c1[0], m2.ss2.s1[0], m2.cc10, m2.is2.c1[0],
    m2.is2.i1[0], m2.cc11, m2.ls2.c1[0], m2.ls2.l1[0], m2.cc12, 
    m2.lls2.c1[0], m2.lls2.ll1[0], m2.cc13, m2.ds2.c1[0], m2.ds2.d1[0], 
    m2.cc14 );

  printf( "m2 = {%d,%d,%d,%d,%d,%d,%d,%d,%d,%ld,%d,%d,%lld,%d,%d,%x,%d}\n",
    m2.cs2.c1[0], m2.cc9, m2.ss2.c1[0], m2.ss2.s1[0], m2.cc10, m2.is2.c1[0],
    m2.is2.i1[0], m2.cc11, m2.ls2.c1[0], m2.ls2.l1[0], m2.cc12, 
    m2.lls2.c1[0], m2.lls2.ll1[0], m2.cc13, m2.ds2.c1[0], m2.ds2.d1[0], 
    m2.cc14 );
  return 0;
}

