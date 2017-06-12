/* Matrix multiply by Nakata */

void matmult(float x[][100], float y[][100], float z[][100], int m, int n, int p)
{
  float s;
  int i, j, k;
  for (i=0; i < m; i++) {
    for (j=0; j < p; j++) {
      s = 0.0;
      for (k=0; k < n; k++) {
        s = s + x[k][i] * y[j][k];
      }
      z[j][i] = s;
    }
  }
}

int main()
{
  float xx[100][100], yy[100][100],zz[100][100];
  float sum;
  int   i, j, k;

  /* SF030609[ */
  for (i = 0; i < 100; i++)
    for (j = 0; j < 100; j ++){
      xx[i][j] = 100*i+j;
      yy[i][j] = (100*i+j)%3;
    }

   matmult(xx, yy, zz, 100, 100, 100);

  for (i = 0; i < 10; i++){
    for (j = 0; j < 10; j ++)
      printf("%9.4f ",zz[i][j]);
    printf("\n");
  }

}
