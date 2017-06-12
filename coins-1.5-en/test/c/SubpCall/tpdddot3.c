/* tpdddot3.c -- Parameter specification of "..." test */
/*               Mixed (int, double) type with more than 4 param     */

extern  struct  _iobuf {
        int     _cnt;
        unsigned char *_ptr;
        unsigned char *_base;
        int     _bufsiz;
        short   _flag;
        char    _file;
} _iob[];

int printf(const char *, ...); 
int fprintf(struct _iobuf *, const char *, ...);

double sum, f1, f2, f3, f4;

int main()
{
  int i, j, k;

  i = 1;
  j = 2;
  k = 3;
  printf("i %d j %d \n", i, j);
  sum = 0.;
  for (f1 = 1.0; f1 < 10.0; f1 = f1 + 1.0) {
    sum = sum + f1;
    f2  = f1 + 1;
    f3  = f2 + 1;
    printf("sum %f f1 %f f2 %f \n", sum, f1, f2);
  }  
  f4 = f3 * 2.0;
  printf("j %d k %d sum %f f1 %f f2 %f f3 %f f4 %f \n", 
         j, k, sum, f1, f2, f3, f4);
}


