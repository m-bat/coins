/* tpstar1.c:  Type name with * */

char *s1, c1, *s2, c2;
int    x;
int main()
{
  c1 = 'a';
  s1 = "abc";
  s2 = s1;
  s2 = s2 + 1; 
  c2 = s2[1];
  printf("c1=%c s1=%c s2=%d c2=%d \n",*s1, c1, *s2, c2); /* SF030620 */
  return 1;
} 

