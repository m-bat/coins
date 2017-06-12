# 1 "tppre11-hir-opt.c"
# 1 "<built-in>"
# 1 "<command line>"
# 1 "tppre11-hir-opt.c"
# 55 "tppre11-hir-opt.c"
 int data[10] = { (int )0, (int )1, (int )2, (int )3, (int )4, (int )5, (int )6, (int )7, (int )8, (int )9 };
 int main ( ) ;
extern int printf ( ) ;
int main( )
{
    int a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int i;
    int _var1;
    int _var3;
    int _var5;
    int _var7;
    a = data[(int )1];
    b = data[(int )2];
    c = data[(int )3];
    d = (int )0;
    for ( i = (int )0,_var5 = ((b) + (c)),_var7 = ((a) + (b));(i) < ((int )10); i = ((i) + ((int )1)))
    {
        d = ((d) + (_var7));
        d = ((d) + (_var5));
    }
    _lab4:;
    d = _var7;
    d = ((d) + (_var5));
    (&(printf))( (("%d %d \n")),c,d);
    return (int )0;
}
