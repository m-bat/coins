/* tpstructAlign2.c  structure alignment test */

struct cstr {
  char c1;
};

struct sstr {
  short s1;
};

struct istr {
  int i1;
};

struct lstr {
  long l1;
};

struct llstr {
  long long ll1;
};

struct dstr {
  double d1;
};

struct ldstr {
  long double ld1;
};

struct cstr2 {
  char c1;
};

struct sstr2 {
  char c1;
  int  s1;
};

struct istr2 {
  char c1;
  int i1;
};

struct lstr2 {
  char c1;
  long l1;
};

struct llstr2 {
  char c1;
  long long ll1;
};

struct dstr2 {
  char c1;
  double d1;
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
/**  gcc for x86
  -224 cs2.c1
  -223 cc9
  -220 ss2.c1
  -216 ss2.s1
  -212 cc10
  -208 is2.c1
  -204 is2.i1
  -200 cc11
  -196 ls2.c1
  -192 ls2.l1
  -188 cc12
  -184 lls2.c1
  -176 lls2.ll1 lower
  -172 lls2.ll1 upper
  -168 cc13
  -160 ds2.c1
  -152 ds2.d1
  -144 cc14
**/

int main()
{
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
  struct mix1 m1;
  struct mix2 m2;

  cc1 = 'a';
  cs1.c1 = '0';
  cc2 = 'a';
  ss1.s1 = 1;
  cc3 = 'b';
  is1.i1 = 2;
  cc4 = 'c';
  ls1.l1 = 3;
  cc5 = 'd';
  lls1.ll1 = 4;
  cc6 = 'e';
  ds1.d1 = 1.0;
  cc7 = 'f';

  m1.cc1 = 'a';
  m1.cs1.c1 = '0';
  m1.cc2 = 'a';
  m1.ss1.s1 = 1;
  m1.cc3 = 'b';
  m1.is1.i1 = 2;
  m1.cc4 = 'c';
  m1.ls1.l1 = 3;
  m1.cc5 = 'd';
  m1.lls1.ll1 = 4;
  m1.cc6 = 'e';
  m1.ds1.d1 = 2.0;
  m1.cc7 = 'f';

  m2.cs2.c1 = '0';
  m2.cc9 = 'a';
  m2.ss2.c1 = 'a';
  m2.ss2.s1 = 1;
  m2.cc10 = 'a';
  m2.is2.c1 = 'a';
  m2.is2.i1 = 2;
  m2.cc11 = 'b';
  m2.ls2.c1 = 'a';
  m2.ls2.l1 = 3;
  m2.cc12 = 'c';
  m2.lls2.c1 = 'a';
  m2.lls2.ll1 = 4;
  m2.cc13 = 'd';
  m2.ds2.c1 = 'a';
  m2.ds2.d1 = 3.0;
  m2.cc14 = 'e';

  /* SF030609[ */
  printf( "%c,%c,%c,%d,%c,%d,%c,%ld,%c,%lld,%c,%f,%c\n",
    cc1, cs1.c1, cc2, ss1.s1, cc3, is1.i1, cc4,
    ls1.l1, cc5, lls1.ll1, cc6, ds1.d1, cc7);

  printf( "m1 = [%c,%c,%c,%d,%c,%d,%c,%ld,%c,%lld,%c,%f,%c]\n",
    m1.cc1, m1.cs1.c1, m1.cc2, m1.ss1.s1, m1.cc3, m1.is1.i1, m1.cc4,
    m1.ls1.l1, m1.cc5, m1.lls1.ll1, m1.cc6, m1.ds1.d1, m1.cc7 );

  printf( "m2 = [%c,%c,%c,%d,%c,%c,%d,%c,%c,%ld,%c,%c,%lld,%c,%c,%f,%c]\n",
    m2.cs2.c1, m2.cc9, m2.ss2.c1, m2.ss2.s1, m2.cc10, m2.is2.c1,
    m2.is2.i1, m2.cc11, m2.ls2.c1, m2.ls2.l1, m2.cc12, m2.lls2.c1,
    m2.lls2.ll1, m2.cc13, m2.ds2.c1, m2.ds2.d1, m2.cc14 );
  /* SF030609] */

  return 0;
}

