/* tpvoid2.c  -- Function with void & void* parameters */

void* ArrayCreate2(void* x, int pDim, int pSize, int pCount)
{
  return x; /* SF030509 */
}
void  ArrayDecl(void* pLeft, void* pRight)
{
  /* SF030509 */
}
int main()
{
  int *x;
  int *y;

  x = ArrayCreate2(x, 1, 4, 5);
  ArrayDecl(y, x);

  printf("Function with void & void* parameters test\n"); /* SF030509 */

  return 0;
}

