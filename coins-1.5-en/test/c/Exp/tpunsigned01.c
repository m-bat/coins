/* tpunsigned01.c */

int i;
int main()
{
  long a=2147483647L;
  unsigned long b;

  for (i-0; i<32; i++) {
    /*    printf("%x\n", (-1U)>>i); */
    /*    printf("%x\n", (-1)>>i);  */
  }
  b=2147483648UL;
  printf("a=%ld b=%lu \n", a, b); /* SF030620 */ 
  return 0;
}

