/* tpstructAlign5-1.c  structure alignment test (with array element) */

int printf( char*, ...);

struct cstr2 {
  char c1[3];
};

struct sstr2 {
  char  c1[3];
  short s1[3];
  char  c;
};

struct istr2 {
  char c1[3];
  int i1[3];
  char c;
};

struct lstr2 {
  char c1[3];
  long l1[3];
  char c;
};

struct llstr2 {
  char c1[3];
  /* long long ll1[3]; */
  double ll1[3]; /* */
  char c;
};

struct dstr2 {
  char c1[3];
  double d1[3];
  char c;
};

struct mix3 {
  struct cstr2 cs2[3];
  char cc9[2];
  struct sstr2 ss2[3];
  char cc10[3];
  struct istr2 is2[3];
  char cc11[3];
  struct lstr2 ls2[3];
  char cc12[3];
  struct llstr2 lls2[3];
  char cc13[3];
  struct dstr2 ds2[3];
  char cc14[3];
};

int main()
{
  struct mix3 m3;

  int lengm3;
  int basem3;
  int i = 0;

  lengm3 = sizeof(m3);

  /* Print contents after filling all spaces. */
  printf("sizeof(struct mix3)=%d, sizeof(m3)=%d \n" ,sizeof(struct mix3), lengm3);
  printf("sizeof cstr2 %d sstr2 %d istr2 %d lstr2 %d llstr2 %d dstr2 %d \n",
          sizeof(struct cstr2), sizeof(struct sstr2), sizeof(struct istr2),
          sizeof(struct lstr2), sizeof(struct llstr2), sizeof(struct dstr2));
  printf("sizeof m3 elements cs2=%d, ss2=%d, is2=%d, ls2=%d, lls2=%d, ds2=%d \n",
         sizeof(m3.cs2), sizeof m3.ss2, sizeof(m3.is2), sizeof(m3.ls2),
         sizeof(m3.lls2), sizeof(m3.ds2));
 
  basem3 = &m3;
  printf("displacements cc9=%d, ss2=%d, cc10=%d, is2=%d, cc11=%d \n",
      m3.cc9-basem3, &m3.ss2[0].c1[0]-basem3, &m3.cc10[0]-basem3,
      &m3.is2[0].c1[0]-basem3, &m3.cc11[0]-basem3);
  printf("displacements ls2=%d, cc12=%d, lls2=%d, cc13=%d, ds2=%d, cc14=%d \n",
      &m3.ls2[0].c1[0]-basem3, m3.cc12-basem3, &m3.lls2[0].c1[0]-basem3, 
      &m3.cc13[0]-basem3, &m3.ds2[0].c1[0]-basem3, &m3.cc14[0]-basem3);

  m3.cs2[0].c1[0] = '0';
  m3.cs2[1].c1[0] = '0';
  m3.cc9[0] = 'a';
    printf(" pass %d\n", i++);
  m3.ss2[0].c1[0] = 'a';
  m3.ss2[1].c1[0] = 'a';
  m3.ss2[0].s1[0] = 1;
  m3.cc10[0] = 'a';
    printf(" pass %d\n", i++);
  m3.is2[0].c1[0] = 'a';
  m3.is2[1].c1[0] = 'a';
  m3.is2[0].i1[0] = 2;
  m3.cc11[0] = 'b';
    printf(" pass %d\n", i++);
  m3.ls2[0].c1[0] = 'a';
  m3.ls2[1].c1[0] = 'a';
  m3.ls2[0].l1[0] = 3;
  m3.cc12[0] = 'c';
    printf(" pass %d\n", i++);
  m3.lls2[0].c1[0] = 'a';
  m3.lls2[1].c1[0] = 'a';
  m3.lls2[0].ll1[0] = 4;
  m3.cc13[0] = 'd';
    printf(" pass %d\n", i++);
  m3.ds2[0].c1[0] = 'a';
  m3.ds2[1].c1[0] = 'a';
  m3.ds2[0].d1[0] = 3.0;
  m3.cc14[0] = 'e';
    printf(" pass %d\n", i++);

  printf( "m3 = [%c,%c,%c,%d,%c,%c,%d,%c,%c,%ld,%c,%c,%lld,%c,%c,%f,%c]\n",
    m3.cs2[0].c1[0], m3.cc9[0], m3.ss2[0].c1[0], m3.ss2[0].s1[0], m3.cc10[0],
    m3.is2[0].c1[0], m3.is2[0].i1[0], m3.cc11[0], m3.ls2[0].c1[0], m3.ls2[0].l1[0],
    m3.cc12[0], m3.lls2[0].c1[0], m3.lls2[0].ll1[0], m3.cc13[0], m3.ds2[0].c1[0],
    m3.ds2[0].d1[0], m3.cc14[0] );

  return 0;
/* 
sizeof(struct mix3)=440, sizeof(m3)=440 
sizeof cstr2 3 sstr2 12 istr2 20 lstr2 20 llstr2 40 dstr2 40 
sizeof m3 elements cs2=9, ss2=36, is2=60, ls2=60, lls2=120, ds2=120 
displacements cc9=9, ss2=12, cc10=48, is2=52, cc11=112 
displacements ls2=116, cc12=176, lls2=184, cc13=304, ds2=312, cc14=432 
m3 = [0,a,a,1,a,a,2,b,a,3,c,a,4,d,a,3.000000,e]
*/

}

