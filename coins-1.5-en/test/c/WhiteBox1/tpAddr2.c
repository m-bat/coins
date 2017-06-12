/* tpAddr2.c by Sassa (modified by Tan) Test of addr and sscanf */

char *data = " 3.14 ";

void readdata(float a[], int b[])
{
  sscanf(data, "%1g", &a[1]);  /* Receiver of sscanf should be float (not double)) */
  sscanf("10.0", "%f", &a[2]);
  sscanf("100", "%d", &b[1]);
}

main()
{
  float  a[10];
  int    b[10];
  readdata(a, b);
  printf("%9.4g %5g %d \n", a[1], a[2], b[1]);
}

