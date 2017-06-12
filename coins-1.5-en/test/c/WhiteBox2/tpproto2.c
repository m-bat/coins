/* tpproto2.c : prototype without parameter name */

  int printf(char*, ...);

  int sub(int, char*);

  int sub(
    int pI,
    char *pChar)
  {
    return pI + pChar[2];
  }
  
int 
main()
{
  int v;
  v = sub(10, "abc");
  printf("v= %d\n", v);
  return v-v;
}

