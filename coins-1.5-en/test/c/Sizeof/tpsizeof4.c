/* tpsizeof4.c:  Test sizeof (Sizeof) */

int printf(char *, ...);
int a[2][2];

int main()
{
  printf("%d\n",sizeof(a[0]));   /* 4 (should be 8) */
  printf("%d\n",sizeof(int[2])); /* 4 (should be 8) */
  return 0;
}

