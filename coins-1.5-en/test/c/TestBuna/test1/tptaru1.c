/* tptaru1.c */
int printf(char *s, ...);

int main()
{
 int i;
 i = 3;
 if(i) {
  printf("i+1 = %d\n", i+1);
  return i+1;
 } else {
  printf("i+2 = %d\n", i+2);
  return i+2;
 }
 return i;
}
