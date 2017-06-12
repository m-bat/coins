# 1 "tpwhile0-hir-new.c"






















































extern  int   printf ( char   *  , ... ) ;
  int   main ( ) ;
int   main(  )
{
        int   s;
        int   i;
    s = (int   )0;
    i = (int   )1;
    while (( i ) < ( (int   )100 ) )
    {
        s = (( s ) + ( ( i ) * ( i )  ) );
        i = (( i ) + ( (int   )1 ) );
    }
    _lab4:;
    (&( printf ) )( (( "s=%d i=%d\n" ) ),s,i);
    return  (int   )0;
}
