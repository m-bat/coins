/* tpStructMri4.c  struct assignment (FUjise 8/12) */

int f1()
{
  int a;
  a = 0;
}
 int main()
{
  struct s {
   int len;
  } v;
  v.len = 10;

  printf("v.len = %d\n",v.len); /* SF030509 */
}

