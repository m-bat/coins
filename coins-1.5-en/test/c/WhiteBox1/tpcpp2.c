/* tpcpp2.c -- C preprocessor for tolower() */

int foo(char p)
{
  return tolower(p);
}

int main()
{
  char a;
  printf("%c \n", foo('A'));
  return  0;
}

