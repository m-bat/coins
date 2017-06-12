/* tpfuncptr4-1.c  Function pointer
   Extracted from tpfuncptr4.c (K & R. p.149 with call) */

int  printf(char*, ...);
int  ia[13];

int  (*daytab)[13];    /* Pointer to array[13] of int. */

int
main()
{
  ia[3] = 3;
  daytab = (int (*)[13])ia;
  printf("function pointer test. %d\n", (*daytab)[3]);
  return 0;
}
