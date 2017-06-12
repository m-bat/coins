/* initgen.c */

#define COUNT 40000

int printf(char*, ...);

int main()
{
  int n, i, j;
  n = COUNT;
  printf("int data[%d] = {\n", n);
  for (i = 0; i < COUNT; i = i + 10 ) {
    for (j = i; j < i+10; j++) {
      if (j < COUNT-1)
        printf(" %d,", j);
      else
        printf(" %d", j);
    }
    printf("\n"); 
  }
  printf("};\n"); 
}
