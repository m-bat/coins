/* tpmalloc1.c  Tsukamoto mail 051013 */

#include <stddef.h>
#include <string.h>
int puts(char*);
void *malloc(size_t);

int main(int argc, char **argv) 
{
  char *a, *str;
  a = "Hello World.";
  str = (char*)malloc(strlen(a) + 1);
  strcpy(str, a);
  puts(str);
  return 0;
}

