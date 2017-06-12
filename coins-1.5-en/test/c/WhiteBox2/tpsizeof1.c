/* tpsizeof1.c:  Test sizeof */

int printf(char*, ...);

char c1, ca1[10], ca2[10][20];
char* st1 = "", *st2 = "abc", *st3 = "文字";
short  si1, sia1[3], sia2[3][4], sia3[3][4][5];
int    ix1, ixa1[3], ixa2[3][4], ixa3[3][4][5];
unsigned int ui1, uia1[3], uia2[3][4], uia3[3][4][5];
int    iya1[] = {1, 2, 3}, iya2[][2] = {{1,2}, {2,3}, {3,4}};
long   lx1, lxa1[3], lxa2[3][4], lxa3[3][4][5];
float  fx1, fxa1[3], fxa2[3][4], fxa3[3][4][5];
double dx1, dxa1[3], dxa2[3][4], dxa3[3][4][5];

struct point { int x; int y; } pt1;
struct point pt2;
struct triangle { struct point p1; struct point p2; struct point p3; } tri1;
typedef struct { struct point ps; struct point pe; } line;
line ln1, ln2;
union figure { struct triangle tri; line ln; } fig1;

int
func1(int pi1, int pa1[], int pa2[3], int pa3[3][4], int pa4[3][4][5])
{
  int i1, i2, i3, i4, i5, i6;
  i1= sizeof(pi1);
  i2= sizeof(pa1);
  i3= sizeof(pa2);
  i4= sizeof(pa3);
  i5= sizeof(pa4);
  i6= sizeof(int);
  return i1+i2+i3+i4+i5+i6;
}

int
main( )
{
  int i;
  int j1, j2, j3, j4, j5, j6, j7, j8, j9, j10, j11, j12, j13, j14,
      j15, j16, j17, j18, j19, j20, j21, j22, j23, j24, j25, j26;
  int j27, j28, j29, j30, j31, j32, j33, j34, j35, j36, j37, j38,
      j39, j40, j41, j42;

  j1 = sizeof(c1);
  j2 = sizeof(ca1);
  j3 = sizeof(ca2);
  j4 = sizeof(st1);
  j5 = sizeof(st2);
  j6 = sizeof(st3);
  j7 = sizeof(*st1);
  j8 = sizeof(*st2);
  j9 = sizeof(*st3);
  j10= sizeof("");
  j11= sizeof("abc");
  j12= sizeof("文字");
  j13= sizeof(ix1);
  j14= sizeof(ixa1);
  j15= sizeof(ixa2);
  j16= sizeof(ixa3);
  j17= sizeof(ui1);
  j18= sizeof(uia1);
  j19= sizeof(uia2);
  j20= sizeof(uia3);
  j21= sizeof(iya1);
  j22= sizeof(iya2);
  j23= sizeof(lx1);
  j24= sizeof(lxa1);
  j25= sizeof(lxa2);
  j26= sizeof(lxa3);
  j27= sizeof(dx1);
  j28= sizeof(dxa1);
  j29= sizeof(dxa2);
  j30= sizeof(dxa3);
  j31= func1(ix1, ixa1, uia1, ixa2, ixa3);
  j32= sizeof(double);
  j33= sizeof(struct point);
  j34= sizeof(struct triangle);
  j35= sizeof(line);
  j36= sizeof(union figure); /* j36= sizeof(struct figure); */
  j37= sizeof(pt1);
  j38= sizeof(pt2);
  j39= sizeof(tri1);
  j40= sizeof(ln1);
  j41= sizeof(ln2);
  j42= sizeof(fig1);
  /* SF030609[ */
  printf("j1 .. = %d,%d,%d,%d,%d\n",j1,j2,j3,j4,j5);
  printf("j6 .. = %d,%d,%d,%d,%d\n",j6,j7,j8,j9,j10);
  printf("j11.. = %d,%d,%d,%d,%d\n",j11,j12,j13,j14,j15);
  printf("j16.. = %d,%d,%d,%d,%d\n",j16,j17,j18,j19,j20);
  printf("j21.. = %d,%d,%d,%d,%d\n",j21,j22,j23,j24,j25);
  printf("j26.. = %d,%d,%d,%d,%d\n",j26,j27,j28,j29,j30);
  printf("j31.. = %d,%d,%d,%d,%d\n",j31,j32,j33,j34,j35);
  printf("j36.. = %d,%d,%d,%d,%d\n",j36,j37,j38,j39,j40);
  printf("j41.. = %d,%d\n",j41,j42);
  /* SF030609] */
  return 0;
}

