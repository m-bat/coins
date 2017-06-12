/* tptypedef2.c:  typedef test (char*) */

typedef char* String;
String s;
int    x;
int main()
{
  s = "abc";
  /*
  if (s[0] == 'a')
    return 0;
  else
    return 1;
  */
  printf("s = [%d,%d,%d,%d]\n",s[0],s[1],s[2],s[3]); /* SF030509 */
}

