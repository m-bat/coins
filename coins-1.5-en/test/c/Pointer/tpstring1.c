/* tpstring1.c: String constant test */

int i, j;
/*extern int strcmp(char* p1, char* p2); */
extern int strcmp(const char* p1, const char* p2); /* SF030620 */
int main()
{
  char* lStr1;
  
  lStr1 = "abc";
  if (strcmp(lStr1, "xyz"))
    i = 1; /* SF030620 */
    /*    i = 0; */
  printf("i=%d \n",i);
  return 0;
}
