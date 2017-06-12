/* tpdim1.c:  Multi-dimension test */

int i, x[10][5], j;
int y[20][10][8], z[30][20][15][5];
int main()
{
  i = 1;
  j = i + 2;
  x[0][4] = 3;
  y[i][j][2] = x[j][i]*4;
  z[1][i][j][3] = 1;
  /* SF030620[ */
  printf("y[i,j,2]=%d z[1,i,j,3]=%d x[3,i+2]=%d \n",
	 y[i][j][2],z[1][i][j][3],x[3][i+2]);
  /* SF030620] */
  return x[3][i+2];
} 

