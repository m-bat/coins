# 1 "loopPara2-loop.c"
  int   main ( ) ;
extern  int   scanf ( ) ;
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
    (&( scanf ) )( (( "%d" ) ),(&( n ) ));
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
    j = (int   )1;
    s = (float   )((double   )10.0);
 
    for ( i = (int   )0;( i ) < ( n ) ; i = (( i ) + ( (int   )1 ) ))
    {
        a[i] = ( (( b[(( j ) - ( (int   )1 ) )] ) + ( s ) ) ) / ( (float   )((int   )2) ) ;
        j = (( j ) + ( (int   )1 ) );
        s = (( (( a[(( i ) - ( (int   )1 ) )] ) + ( s ) ) ) + ( (float   )(j) ) );
    }
    _lab8:;
    return  (int   )0;
}
