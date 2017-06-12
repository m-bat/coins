/* tpdddot1.c -- Parameter specification of "..." test */

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

int sum, f1, f2, f3;

int main()
{
  int i, j, k;

  i = 1;
  j = 2;
  k = 3;
  printf("i %d j %d \n", i, j);
  sum = 0.;
  for (f1 = 1; f1 < 10; f1 = f1 + 1) {
    sum = sum + f1;
    f2  = f1 + 1;
    f3  = f2 + 1;
    printf("sum %d f1 %d f2 %d \n", sum, f1, f2);
  }  
  printf("k %d sum %d \n", k, sum);
  printf("%d %d %d \n", f1, f2, f3);
}


