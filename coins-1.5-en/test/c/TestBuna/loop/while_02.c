/* while_02.c   simple while statement  */
                               /* array */
int printf(char *s, ...);

int main()
{
  int a[3];
  int i;

  a[0] = 0;
  i = 1;
  while (i < 3) {
    a[i] = a[i-1] + i;
    i = i + 1;
  }
    printf("a[0] = %d\n",a[0]);
    printf("a[1] = %d\n",a[1]);
    printf("a[2] = %d\n",a[2]);
  /* } SF030514 */
  return 0;
}

