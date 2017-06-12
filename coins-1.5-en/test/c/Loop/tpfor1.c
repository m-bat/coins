/* tpfor1.c:  for-loop  */

int a[10];
int i;
int main()
{
  for (i = 0; i < 10; i++)
    a[i] = 0;
  for (i = 1; i < 10; i = i + 1) {
    a[i] = a[i-1] + i;
  }
  /* SF030620[ */
  for (i = 0; i < 10; i++)
    printf("a[%d]=%d ",i,a[i]);
  /* SF030620] */
  return 0;
} 

