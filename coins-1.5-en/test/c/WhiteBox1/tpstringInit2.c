/* tpstringInit2.c:  character string initiation (local) */
/*   Fujise mail 030205 hir2c problem */

int printf(char*, ...);

int main()
{
  char name1[]  = "";
  char name2[]  = "abc";
  char name3[]  = "hello world";

  printf("%s %s %s \n", name1, name2, name3);
  printf("%d %d %d \n", sizeof(name1), sizeof(name2), sizeof(name3));
  return 0;
} 

