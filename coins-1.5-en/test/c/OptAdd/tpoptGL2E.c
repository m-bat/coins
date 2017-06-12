# 1 "Test1/Add2/tpoptGL2-hir-opt.c"
# 1 "<built-in>"
# 1 "<command line>"
# 1 "Test1/Add2/tpoptGL2-hir-opt.c"
# 55 "Test1/Add2/tpoptGL2-hir-opt.c"
extern int printf ( char * , ... ) ;
 int ff ( int ) ;
 int ga;
 int gb;
 int gc;
 int gd;
 int ge;
 int gf;
 int gh;
 int main ( ) ;
int main( )
{
    int a;
    int b;
    int c;
    int d;
    int e;
    int f;
    int h;
    int _var1;
    int _var3;
    int _var5;
    int _var7;
    int _var9;
    int _var11;
    int _var13;
    int _var15;
    int _var17;
    int _var19;
    int _var21;
    a = (&(ff))( (int )1);
    b = (&(ff))( (int )5);
    ga = (&(ff))( (int )1);
    gb = (&(ff))( (int )5);
    c = ((a) + (b));
    d = ((a) + ((int )1));
    _var1 = ga;
    _var3 = gb;
    gc = ((_var1) + (_var3));
    gd = ((_var3) + (_var1));
    if ((a) != ((int )0))
    {
        _var5 = ((a) + (b));
        c = _var5;
        _var7 = ((_var5) + (c));
        d = _var7;
        e = ((a) + ((int )1));
        f = _var7;
        _var1 = ga;
        _var3 = gb;
        _var9 = ((_var1) + (_var3));
        gc = _var9;
        a = ((a) + (_var1));
        _var11 = gc;
        gd = ((_var9) + (_var11));
        _var13 = gd;
        d = ((d) + (_var13));
        ge = ((_var1) + ((&(ff))( (int )1)));
        gf = ((((_var1) + (_var3))) + (_var11));
    }
    else
    {
        c = ((a) + ((int )1));
        d = ((int )3) * (((a) + ((int )1)));
        e = a;
        f = ((e) + ((int )1));
        _var1 = ga;
        gc = ((_var1) + ((&(ff))( (int )1)));
        _var11 = gc;
        c = ((c) + (_var11));
        gd = ((int )3) * (((_var1) + ((int )1)));
        ge = _var1;
        _var15 = ge;
        gf = ((_var15) + ((int )1));
    }
    d = ((d) + ((int )2));
    h = ((((a) + (b))) + (c));
    gh = ((int )3) * (((ga) + ((int )1)));
    (&(printf))( (char * )((("a=%d b=%d c=%d d=%d e=%d f=%d h=%d return %d\n"))),a,b,c,d,e,f,h,((d) + ((int )2)));
    (&(printf))( (char * )((("ga=%d gb=%d gc=%d gd=%d ge=%d gf=%d gh=%d \n"))),ga,gb,gc,gd,ge,gf,gh);
    return (int )0;
}
int ff( int p )
{
    int _var23;
    int _var25;
    int _var27;
    int _var29;
    int _var31;
    gf = (int )10;
    _var23 = gf;
    ge = _var23;
    _var25 = ge;
    gd = _var25;
    _var27 = gd;
    gc = _var27;
    _var29 = gc;
    gb = _var29;
    _var31 = gb;
    ga = _var31;
    return p;
}
