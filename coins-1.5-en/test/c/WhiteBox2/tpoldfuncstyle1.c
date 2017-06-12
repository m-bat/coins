/* tpoldfuncstype1.c : Old function stype (without prototype) */

  int printf(char *, ...);

  int sub( pI, pChar)
    int pI;
    char *pChar;
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

