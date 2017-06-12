/* tparray1-2.c:  2 dimensional array */

int i, j;
int x[20][10];
int y[20];

int main()
{
  for (i = 0; i < 20; i++) {
    y[i] = 0;
    for (j = 0; j < 10; j++) {
      y[i] = y[i] + x[i][j];
    }
  }
  printf("'2 dimensional array test' \n"); /* SF030620 */
  return 0; 
} 

