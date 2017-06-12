/*  dither.c  (Watanabe)
**  Ordered dither algorithm 
**  (See Procedural elements for computer graphics, D. Rogers
**   Traanslated by F. Yamaguchi, Nikkan Kogyo, p. 132.)
*/
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/resource.h>

#define LIMX 800
#define LIMY 800

short pixel[LIMX][LIMY];    /* Display dot pattern */
short image[LIMX][LIMY];    /* Input image */
int count = 0;

extern void OrderedDither(
    short in[LIMX][LIMY],    /* Input image */
    int   pxMin, int pxMax,  /* Range on X axis */
    int   pyMin, int pyMax); /* Range on Y axis */

/*---- main ----*/
int main()
{
  int    i, j;
  double startu, starts, endu, ends;

  /* Set up the image */
  for (j = 0; j < LIMY; j = j + 4) {
    for (i = 0; i < LIMX; i = i + 4)
      image[i][j] = rand() % 16;
  }
  /** startu = dtime(&starts);
  **/
  for (i = 0; i < 10; i++) {
    OrderedDither(image, 0, LIMX-1, 0, LIMY-1);
  }
  /* Display pixcel */
  printf("count  %d\n", count);
  for (j = 0; j < 10; j++) {
    printf("\nj=%d ", j);
    for (i = 0; i < 10; i++)
      printf(" %x", image[i][j]);
  }
  return 0;
}

#define LIMX 800
#define LIMY 800

extern short pixel[LIMX][LIMY];    /* Display dot pattern */
extern short image[LIMX][LIMY];    /* Input image */
int   black = 1, white = 0; /* dot is black or white */
int D[4][4] = {{  0,  8,  2, 10},   /* 4*4 dithered matrix */
               { 12,  4, 14,  6},
               {  3, 11,  1,  9},
               { 15,  7, 13,  3} };
int n = 4;
extern int count;

/*---- OrderedDither ----*/
void OrderedDither(
    short in[LIMX][LIMY],    /* Input image */
    int   pxMin, int pxMax,  /* Range on X axis */
    int   pyMin, int pyMax)  /* Range on Y axis */
{
  int lx, ly, li, lj;

  for (ly = pyMax; ly >= pyMin; ly--) {
    for (lx = pxMax; lx >= pxMin; lx--) {
      li = lx % n;
      lj = ly % n;
      if (in[lx][ly] < D[li][lj]) 
        pixel[lx][ly] = black;
      else
        pixel[lx][ly] = white;
      count++;
    }
  }
} /* OrderedDither */

