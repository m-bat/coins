/* Decl/tpextern1.c  extern function declaration with default type */

extern printf(char*, ...);         /* no type specifier */

int main()
{
  printf("default type");
  return 0;
}

