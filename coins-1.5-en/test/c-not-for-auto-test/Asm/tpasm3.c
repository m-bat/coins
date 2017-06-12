/* tpasm3.c: ASM test */ 
/*   Kitamura mail 060519 */

#pragma opt inline readXYZ

int printf(char*, ...);

int readXYZ(int p)
{
  int c;

  asm("#param %eax,w%I32\n"
      "#clobber %eax\n"
      "inl (%1)\n", p, c);
  return c;
}

int main()
{
  int a;
  a = readXYZ(0x10);
  printf(" %d ", a);
  return 0;
}

