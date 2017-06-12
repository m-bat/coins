/* tpAddrMri1.c  Pointer assignment by & operator */

int printf(char*, ...);

int main()
{
  long a, b;  /* ##82 */
  long *p;    /* ##82 */
  a = &b;
  p = a;
  *p = 100;
  printf(" %d \n", b);
  return 0;
}
