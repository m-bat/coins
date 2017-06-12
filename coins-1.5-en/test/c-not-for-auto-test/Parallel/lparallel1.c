/* lparallel1.c test loop parallel (local variables) */

int main(){
  float x[50],c[50],z[50][100];
  int i,j,n,k;
  n=50;k=0;               /* B1 */
  for (i=0; i<n ; i++) {  /* B2 */ /* B8 */
    x[0]=0 ;              /* B3 */
    x[1]=10 ;
    for (j=2; j<50 ; j++) {        /* B4 */ /* B6 */
      x[j] = (x[j-1] + x[j-2])/2 ; /* B5 */
      z[j][k] = x[j] ;
    }
    k=k+2;                         /* B7 */
  }
  printf("k=%d z[49][k-2]=%f\n", k, z[49][k-2]);  /* B8 */
}
