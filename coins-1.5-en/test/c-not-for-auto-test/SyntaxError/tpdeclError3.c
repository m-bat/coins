/* tpdeclError3.c:  Declaration error 3 */
/*    Nakata mail 060823 */

int add(float a[][],float b[][],float c[][],int size)
{
  int i,j;
  for( i = 0; i < size; i++){
    for( j = 0; j < size; j++){
      a[i][j]=b[i][j]+c[i][j];
    }
  }
  return 1;
}

