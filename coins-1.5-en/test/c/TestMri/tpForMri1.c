/* tpForMri1.c */

main()
{
  int i;
  int j;
  int k;
  int c[100];

  j = 100; /* SF030509 */
  k = 200; /* SF030509 */

  for(i=0;i<100;i++) {
	c[i]=100;
	j=j -1;
	k= k +k;
  }

  printf("c = ["); /* SF030509 */
  for(i=0;i<100;i++) /* SF030509 */
    printf("%d,",c[i]); /* SF030509 */
  printf("]\ni,j,k = %d,%d,%d\n",i,j,k); /* SF030509 */
}

