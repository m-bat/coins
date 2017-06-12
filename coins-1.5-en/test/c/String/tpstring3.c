/* tpstring3.c:  String expresion  */

int printf(char* pFrom, ...);

char c1, c2, c3, c4;
char *s1, *s2, *s3;
char (*ca1)[4], (*ca2)[7];
int  i, j, x1, x2, x3;
int main()
{
  i  = 1;
  j  = 0;
  c1 = "string"[i];
  s1 = "abc";
  x1 = *s1;
  c3 = (&"abc")[j][1];
  ca1 = &"abc";
  ca2 = &"string" + 1;
  x2 = (int)(&"abc")[i-1];
  x3 = (int)(&"string" + 1)[i-1];
  s2 = s1;
  s3 = "abcde"+2;
  c2 = s2[1];
  c3 = (*ca1)[1];
  c4 = (*(&"string" + j))[3];
  /* printf("c1 %c c2 %c c3 %c c4 %c s3 %s ca1 %d \n",c1, c2, c3, c4, s3, (int)ca1); /* SF030609 */
  printf("c1 %c c2 %c c3 %c c4 %c s3 %s \n",c1, c2, c3, c4, s3); /* SF030609 */
  return 0;
}

