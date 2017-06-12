/* tphomo2.c:  Test of homologous & semi-homologous expression recognition */

int printf(char*, ...);
#include <string.h>

int x = 1, y = 2, z = 3;
typedef struct {
  int year, month, day;
} Date;
typedef struct {
  char name[12];
  Date birth;
  Date graduate;
} PersonRec;
PersonRec tanaka, suzuki, yamada;
typedef struct RGBA {
  unsigned char r, g, b, a;
} RGBA_Rec;
RGBA_Rec *rgbbuf;
RGBA_Rec *rgbp;

int ProcSemi()
{
  int i, j, k;
  int aa[10], bb[10], cc[10];

  /* SF030620[ */
  i = 0;
  j = 1;
  k = 2*i+1;
  aa[i]=123;
  bb[i+j]=234;
  cc[2*i+1]=345;
  /* SF030620] */

  x = (x * 2) + aa[i];
  y = (y * 3) + bb[i + j];
  z = (z * 2) + cc[2 * i + 1];
  x = x + aa[i] * 2;
  y = y + bb[j] * (i + 1);
  z = z + cc[k] * (i - 1);
  if ((x < y)&&(y < z)&&(x < 0))
    aa[i] = 0;
  if ((x < z)&&(y < x)&&(z < 0))
    bb[i] = 0;
  z = z + aa[i] + bb[i+j];
}

unsigned int Funcx(unsigned int p1[17], unsigned int p2[16])
{
  int i, j;
  unsigned int s = 0, t = 0, u,  v;
 
  for (i = 0; i < 8; i ++) {
    v = ((unsigned int)(p1[i] + p1[i+1])>>1) - p2[i];
    if (v >= 0)
      s += v;
    else
      s -= v;
  }
  for (j = 0; j < 8; j ++) {
    u = ((unsigned int)(p1[j] + p1[j+1])>>1) - p2[j];
    if (u >= 0)
      t += u;
    else
      t -= u;
  }
  return s + t;
}

int Func(int p1, int p2, int p3)
{
  return p1 + p2 + p3;
}

void MakeRGBbuffer()
{
  int i;
  
  for (i = 0; i < 16; i++) {
    rgbbuf[i].r = 0;
    rgbbuf[i+1].g = 0;
    rgbbuf[i-1].b = 0;
    rgbbuf[i*2].a = 0;
  }
  rgbp->r = 1;
  rgbp->g = 2;
  rgbp->b = 3;
  rgbp->a = 4;
  i = 0;
}

int main()
{
  int l1, l2, l3, i, a = 7, b = 13, c = 17, v, x;
  unsigned int a1[17] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  unsigned int a2[16] = { 10, 11, 12, 13, 14, 15, 16, 17};

  ProcSemi();
  x = Funcx(a1, a2);
  printf("x=%d y=%d z=%d \n",x, y, z);
  strcpy(tanaka.name, "Tanaka");
  strcpy(suzuki.name, "Suzuki"); /* SF030620 */
  tanaka.birth.year  = 1975;
  tanaka.birth.month = 1;
  tanaka.birth.day   = 10;
  tanaka.graduate.year  = 1993;
  tanaka.graduate.month = 3;
  tanaka.graduate.day   = 20;
  suzuki.birth.year  = 1975;
  suzuki.birth.month = 1;
  suzuki.birth.day   = 10;
  suzuki.graduate.year  = 1993;
  suzuki.graduate.month = 3;
  suzuki.graduate.day   = 20;
  i = 0;
  l1 = Func(i, 1, 2);
  l2 = Func(i, 1, 3);
  l3 = Func(i, 1, 4);
  if (a & 32768)
    if (a & 16384) 
      v = 0;
    else 
      v = 255;
  else 
    v = a * 128;
  x = v;
  if (b & 32768)
    if (b & 16384) 
      v = 0;
    else 
      v = 255;
  else 
    v = b * 128;
  x = v;
  if (c & 32768)
    if (c & 16384) 
      v = 0;
    else 
      v = 255;
  else 
    v = c * 128;
  x = v;
  /* SF030620[ */
  printf("%s\t%d %d %d\t%d %d %d\n",tanaka.name,
	 tanaka.birth.year,tanaka.birth.month,tanaka.birth.day,
	 tanaka.graduate.year,tanaka.graduate.month,tanaka.graduate.day);
  printf("%s\t%d %d %d\t%d %d %d\n",suzuki.name,
	 suzuki.birth.year,suzuki.birth.month,suzuki.birth.day,
	 suzuki.graduate.year,suzuki.graduate.month,suzuki.graduate.day);
  printf("x=%d \n",x);
  printf("%d %d %d\n",l1,l2,l3);
  /* SF030620] */
}

