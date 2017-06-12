/* tpsizeof01.c */

char *c1, *c2;
int  s1, s2;

int main()
{
  c1 = "sizeof";
  s1 = sizeof("sizeof");
  s1 = sizeof "sizeof" ;
  printf("Sizeof expression test.\n"); /* SF030609 */
  return 0;
}

