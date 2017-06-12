/* tpasm1.c: ASM test 1 */ 
int x, y, z;
int main()
{
  int a, b, c;
  a = 1;
  b = a + 2;
  z = -1;
  asm("#param %I32, %I32, w%I32\n"
      "mov %1,%3\n"
      "add %2,%3\n",
      a, b+1, z);
  printf("a=%d b=%d z=%d \n", a, b, z);
  return 0;
} 

