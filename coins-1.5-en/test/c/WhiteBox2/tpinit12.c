/* tpinit12.c:  initiation value of minus, structure initiation */
/*     Sasada mail 040127 */

int printf(char*, ...);

int minusG[]={ -1, -2, 2-3+2 };
char largeArray[100] = { 'a', 'b' };
struct point { int x; int y;} pt1 = { -3, 4};

int main()
{
  struct point pt2 = { 5, 6};
  int minusL[]={ -1, -2 };
  char largeArrayL[100] = { 'a', 'b' };
  printf("%d %d %d %d %d \n",minusG[0], minusG[2], minusL[1], pt1.x, pt2.y );
  printf("%c %d %d \n", largeArray[0], largeArray[1], largeArray[99]);
  printf("%c %d %d \n", largeArrayL[0], largeArrayL[1], largeArrayL[99]);
  return 0;
}

