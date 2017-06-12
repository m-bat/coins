/* tpstring4.c:  String constant test 4 */

int i, j;
int xyz;  /* Do not confuse with "xyz" */
extern int strcmp(char* p1, char* p2);
int main()
{
  char* lStr1, *lStr2;
  int   abc;    /* Do not confuse with "abc" */

  lStr1 = "abc";
  lStr2 = "i";
  if (strcmp(lStr1, "xyz"))
    i = 0;
  abc = i;
  xyz = abc + i;

  printf("i = %d\n",i); /* SF030509 */
  printf("abc,xyz = %d,%d\n",abc,xyz); /* SF030509 */
  printf("lStr1,lStr2 = '%s','%s'\n",lStr1,lStr2); /* SF030509 */

  return xyz;
}
