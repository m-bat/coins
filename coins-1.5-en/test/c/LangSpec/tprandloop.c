/* if-stmt, for-loop, array test */

int a[100];
int next = 1; 

int 
Sum(
    int pData[100], 
    int pCount) 
{
  int s = 0;
  int i;

  for (i = 0; i < pCount; i++)
    s = s + pData[i];
  return s;
}

int
main()
{

  int   i, total;
  float mean;
  unsigned int rand;
  
  i = 0;
  do {  /* Compute random number. 
           See Hirabayashi: Programming examples in C. */
    next = next * 1103515245 + 12345;
    rand = (unsigned int)(next / 0x100000) % 0x8000;
    a[i] = rand;
    i = i + 1;
  }while (i < 100);
  total = Sum(a, 100);
  mean  = total / 100.0;
  printf("total %d  mean %f count %d \n", total, mean, 100);
  return 0;
}

     
