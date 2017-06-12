/* tpStringNakata1 020511 error report */

char *cPtr1 = "abc";
char *cPtr2 = "abc"+1;
char a1[] = "abc";


int main()
{ 
  char *cPtr3 = "abc";
  char *cPtr4 = "abc"+1;
  char a2[] = "abc";
  printf("%s %s \n", a1, a2);
  printf("%s %s %s %s \n", cPtr1, cPtr2, cPtr3, cPtr4);
  return 0; 
}

