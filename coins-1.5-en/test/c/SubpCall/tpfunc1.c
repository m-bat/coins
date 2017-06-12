/* tpfunc1.c  Function with parameter reference without set. */

int g;

int func0()
{
  int l1;

  l1 = 0;
  g = l1+1;
  return g;
}
void func1(int p)
{
  if (p != 0)
    g = p;
  else
    g = 0;
}
int func2(int p, int q, int r)
{
  int l, l2, l3;

  if (p != 0)
    l = p;
  else
    l = q;
  return l + r;
}

main() /* SF030609 */
{
  printf("func0: %d\n",func0()); /* SF030609 */
  func1(0); /* SF030609 */
  printf("func1: %d\n",g); /* SF030609 */
  func1(1); /* SF030609 */
  printf("func1: %d\n",g); /* SF030609 */
  printf("func2: %d\n",func2(0,1,2)); /* SF030609 */
  printf("func2: %d\n",func2(1,2,3)); /* SF030609 */
}
