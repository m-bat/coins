/* tpdefuseasm1.c: Def/use test program with AsmStmt 1 */ 

int printf(char*, ...);

int k, m; 
int main()
{
  int i, j; 
  i = 1;
  j = i + 1;
  asm("#param %I32, m%I32\n"
      "add %1,%2\n",
      i, j);  /* j = j + i; */
  k = j + i;
  i = k + 1;
  j = i + k;
  asm("#param %I32, %I32, w%I32\n"
      "mov %1,%3\n"
      "add %2,%3\n",
      i, j, k);  /* k = i + j; */
  m = k + j; 
  printf("i=%d j=%d k=%d m=%d",i,j,k, m);
  while (k < 3) {
    printf("check\n");
    i = i +1;
    j = j -1;
    k = k + j;
    if (k) {
      i = 3;
      j = i + j;
      asm("#param %I32, m%I32\n"
          "add %1,%2\n",
          j, k);  /* k = k + j; */
    }else 
      asm("#param %I32, m%I32\n"
          "add %1,%2\n",
          i, j);  /* j = j + i; */
      k = 1;
  }
  return 0;
}
