/* tpstring4-1.c:  character array with string initial value  */
/*                 with prototype */

int printf(char*, ...);

char name1[]  = "Tanaka";
char name2[7] = "Suzuki";

int main()
{
  char name3[] = "Mita";
  char name4[] = "hitotsubashi";
  printf("%s %s %s %s\n", name1, name2, name3, name4);
  return 0;
} 

