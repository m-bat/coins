/* Sizeof/tpsizeofMri2.c  Fujise mail 020816 */

int printf(char*, ...);

typedef double Align; /*?*/
struct v {
  int a;
};
struct v1 {
  struct v x;
  struct v x1;
};

int main()
{
  printf("sizeof struct v =%d\n", sizeof(struct v));
  printf("sizeof struct v1=%d\n", sizeof(struct v1));
  return 0;
}

