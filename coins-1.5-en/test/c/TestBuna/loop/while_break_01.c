/* while_break_01.c   */
int scanf(char *s, ...); /* int scanf(char *s, int *a); SF030514 */
int printf(char *s, ...); /* int printf(char *s, int a); SF030514 */

int main()
{
  int n=0;
  float x;
  float mean;
  float sum = 0;

  while (scanf("%f",&x) == 1) {
    printf("x = %f\n", x ); /* printf(" x = %d\n", x ); SF030514 */
    sum = sum + x ;
    ++n;
    if (n > 100)
          break;
  }
   mean = sum/n;
   printf("sum = %f\n", sum ); /* printf(" sum = %d\n", sum ); SF030514 */
   printf("mean = %f\n", mean ); /* printf(" mean = %d\n", mean ); SF030514 */
  return 0;
}

