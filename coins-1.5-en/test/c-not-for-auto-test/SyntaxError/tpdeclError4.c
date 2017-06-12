/* tpdeclError4.c:  Declaration error 4 */

int printf(char*, ...);

int a[10][10], size;
int  b[][2] = {{1,2}, {3,4}, {5,6}};
int  c[][ ] = {{1,2}, {3,4}, {5,6}};

int main()
{
  int i,j;
  size = 2;
  for( i = 0; i < size; i++){
    for( j = 0; j < size; j++){
      a[i][j]= b[i][j] + c[i][j];
    }
  }
  printf(" %d %d %d %d\n", a[0][0], a[0][1], a[1][0], a[1][1]);
  return 0;
}

