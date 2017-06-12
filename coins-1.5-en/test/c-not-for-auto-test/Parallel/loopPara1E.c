# 1 "loopPara1-loop.c"
  int   main ( ) ;
int   main(  )
{
        int   i;
        int   j;
        int   k;
        int   n;
        float   s;
        float   q;
        float   a[100];
        float   b[100];
    n = (int   )100;
    j = (int   )1;
    s = (float   )((double   )10.0);
#pragma omp  parallel for  private(q,j) reduction(+:s)
    for ( i = (int   )0;( i ) < ( n ) ; i = (( i ) + ( (int   )1 ) ))
    {
        j = (( ( (int   )1 ) * ( i )  ) + ( (int   )1 ) );
        q = (( b[j] ) + ( a[i] ) );
        a[i] = ( (( b[(( j ) - ( (int   )1 ) )] ) + ( b[(( j ) + ( (int   )1 ) )] ) ) ) / ( (float   )((int   )2) ) ;
        s = (( (( q ) + ( s ) ) ) + ( (float   )((( j ) + ( (int   )1 ) )) ) );
    }
    _lab4:;
}
