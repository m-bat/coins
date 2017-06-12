/* Sizeof/tpsizeofMri1.c  Fujise mail 020816 */

int printf(char*, ...);

typedef double Align; /*?*/
struct v {
  union header *ptr;
  unsigned size;
};
union header {
  struct v s;
  Align x;
};
typedef union header Header;

int main()
{
  printf("sizeof struct v=%d\n", sizeof(struct v));
  printf("sizeof Align   =%d\n", sizeof(Align));
  printf("sizeof Header  =%d\n", sizeof(Header));
  return 0;
}

