/* tpptrarith3.c:  Pointer arithmetic * */

int printf(char*, ...);

int  a[4] = {0, 1, 2, 3};
int main()
{
  int *p;
  p = &a[1];
  *(p+1)=4;
  printf("%d %d\n",  *(p+1), a[2]);
  return 1;
} 

