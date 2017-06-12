/* tpstringInit1.c:  character string initiation  */

int printf(char*, ...);

char name1[]  = "";
char name2[]  = "abc";
char name3[]  = "hello world";
char name4[]  = {"hello world"};  /* See char_array_arg_01.c */

int main()
{
  printf("%s %s %s %s \n", name1, name2, name3, name4);
  printf("%d %d %d \n", sizeof(name1), sizeof(name2), sizeof(name3));
  return 0;
} 

