/* tpsimple1.c -- Simple arithmetic  */

  float x[10][10], y[10],z[10];
  float sum;
  int   i, j, k;

int main()
{
  sum = 0.0;
  for (i = 0; i < 10; i++) {
    sum = sum + y[i] ;
  }
  printf("sum=%f\n",sum); /* SF030620 */
  return 0;
}

