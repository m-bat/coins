/* tparray0.c:  basic array */
int i, x[10], j;
int y[20];
int main()
{
  i = 1;
  j = i - 1;
  x[0] = 3;
  x[j] = x[j] + 1;
  y[i] = x[j]*4;
  x[i+1] = y[i];
  printf("x[j]=%d y[i]=%d x[i+1]=%d \n",x[j],y[i],x[i+1]); /* SF030620 */
  return 0;
} 

