# 1 "tpoptG01-hir-opt.c"
# 1 "<built-in>"
# 1 "<command line>"
# 1 "tpoptG01-hir-opt.c"
# 55 "tpoptG01-hir-opt.c"
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
    int _var1;
    int _var3;
    int _var5;
    a = (int )1;
    b = (int )5;
    _var1 = ((a) + (b));
    c = _var1;
    _var5 = ((a) + ((int )1));
    d = _var5;
    if ((a) > ((int )0))
    {
        c = _var1;
        _var3 = ((_var1) + (c));
        d = _var3;
        e = _var5;
        f = _var3;
    }
    else
    {
        c = _var5;
        d = ((int )3) * (_var5);
        e = a;
        f = ((e) + ((int )1));
    }
    d = ((d) + ((int )2));
    (&(printf))( (("a=%d b=%d c=%d d=%d e=%d f=%d return %d\n")),a,b,c,d,e,f,((d) + ((int )2)));
    return (int )0;
}
