/* tpAddr1.c by Sassa (modified by Tan) */

void readdata(double a[], int b[])
{
  /* scanf("%1g", &a[1]); 
  */
  sscanf("100", "%d", &b[1]);
}

main()
{
  double a[10];
  int    b[10];
  readdata(a, b);
  printf("%d \n", b[1]);
}

