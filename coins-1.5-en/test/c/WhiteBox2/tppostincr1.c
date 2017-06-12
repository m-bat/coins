/* tppostincr1.c  post increment test */
/*   Mori mail 040305 coins-bug 2004/5 */

int printf(char*, ...);

double vd[100];
int main()
{
  int j=0, k=0;
  vd[k++]=j&1 ? j+1:j-1;
  printf("%f %f %d \n", vd[0], vd[1], k );
  return 0;
}
