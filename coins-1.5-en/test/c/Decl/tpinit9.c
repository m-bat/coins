/* tpinit9.c  Initial value  (Decl) */
int main()
{
  int i; int *b[] = {&i};
  i = 3;
  
  printf("*b[0]=%d \n", *b[0]); /* SF030620 */
  return 0;
}
