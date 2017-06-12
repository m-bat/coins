/* tpselect1.c   */

int i = 0, j = 1;

int main()
{
  double a, b[2] = {1.0, 2.0 };
  a = (b[i] < b[j]) ? b[i] : b[j];
  printf("a=%f\n",a); /* SF030620 */
  return 0;
}
