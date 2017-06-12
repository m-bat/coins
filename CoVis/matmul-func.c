# include <stdio.h>

void  matmult(double * X, double * Y, double * Z, int m, int n, int p){
      double s;
      int i, j, k;
      for ( i=0; i<m; i++){
          for ( j=0; j<p; j++){
             s=0.0;
              for ( k=0; k<n; k++){
                 s=s+X[i*n+k]*Y[k*p+j];
              }
              Z[i*p+j]=s;
           }
      }
 }


void main () {
  double X[4], Y[4], Z[4];
  int i, j;
  for (i = 0; i < 2; i++ ) {
    for (j = 0; j < 2; j++ ) {
      X[i*2+j] = i*2+j;
      Y[i*2+j] = i*2+j;
    }
  }

  matmult(X, Y, Z, 2, 2, 2);

  for (i = 0; i < 2; i++ ) {
    for (j = 0; j < 2; j++ ) {
      printf("%g %g %g\n", X[i*2+j], Y[i*2+j], Z[i*2+j]);
    }
  }
}

