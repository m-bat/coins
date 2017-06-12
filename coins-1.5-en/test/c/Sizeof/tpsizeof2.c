/* tpsizeof2.c:  Test sizeof */

#include <stddef.h>
#include <stdio.h>

char c1, ca1[10], ca2[10][20];
char* st1 = "", *st2 = "abc", *st3 = "•¶Žš";
/**
wchar_t wc1, wca1[10], wca2[10][20];
wchar_t * wst1 = L"", *wst2 = L"abc", *wst3 = L"•¶Žš";
**/

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
  printf("func1 pi1 %d pa1 %d pa2 %d pa3 %d pa4 %d \n",
    sizeof(pi1), sizeof(pa1), sizeof(pa2), sizeof(pa3), sizeof(pa4));
  return sizeof(int);
}

int 
main( )
{
  int i, j, k;
  printf("c1 %d ca1 %d ca2 %d \n", sizeof(c1), sizeof(ca1), sizeof(ca2));
/**
  printf("wc1 %d wca1 %d wca2 %d \n", sizeof(wc1), sizeof(wca1), sizeof(wca2));
**/
  printf("st1 %d st2 %d st3 %d \n", sizeof(st1), sizeof(st2), sizeof(st3));
  printf("*st1 %d *st2 %d *st3 %d \n", sizeof(*st1), sizeof(*st2), sizeof(*st3));
/**
  printf("wst1 %d wst2 %d wst3 %d \n", sizeof(wst1), sizeof(wst2), sizeof(wst3));
  printf("*wst1 %d *wst2 %d *wst3 %d \n", sizeof(*wst1), sizeof(*wst2), sizeof(*wst3));
**/
  printf("sizeof null %d abc %d •¶Žš %d \n", sizeof(""), sizeof("abc"), sizeof("•¶Žš"));
/**
  printf("sizeofwchar_t null %d abc %d •¶Žš %d \n", sizeof(L""), sizeof(L"abc"), sizeof(L"•¶Žš"));
**/
  printf("si1 %d sia1 %d sia2 %d sia3 %d \n", sizeof(si1), sizeof(sia1), sizeof(sia2), sizeof(sia3));
  printf("ix1 %d ixa1 %d ixa2 %d ixa3 %d \n", sizeof(ix1), sizeof(ixa1), sizeof(ixa2), sizeof(ixa3));
  printf("ui1 %d sia1 %d uia2 %d uia3 %d \n", sizeof(ui1), sizeof(uia1), sizeof(uia2), sizeof(uia3));
  printf("iya1 %d iya2 %d \n", sizeof(iya1), sizeof(iya2));
  printf("lx1 %d lxa1 %d lxa2 %d lxa3 %d \n", sizeof(lx1), sizeof(lxa1), sizeof(lxa2), sizeof(lxa3));
  printf("dx1 %d dxa1 %d dxa2 %d dxa3 %d \n", sizeof(dx1), sizeof(dxa1), sizeof(dxa2), sizeof(dxa3));
  i = func1(ix1, ixa1, uia1, ixa2, ixa3); 
  printf("int %d point %d triangle %d \n", 
    sizeof(int), sizeof(struct point), sizeof(struct triangle));
  printf("line %d figure %d \n", sizeof(line), sizeof(union figure));
  printf("pt1 %d pt2 %d tri1 %d  \n", sizeof(pt1), sizeof(pt2), sizeof(tri1));
  printf("ln1 %d ln2 %d fig1 %d  \n", sizeof(ln1), sizeof(ln2), sizeof(fig1));
  return 0;
}

