/* tpconst4.c:  Constants (Exp) */

int main()
{
  int i;
  i = 0xFFFFFFFF;  /* (i 4294967295)  (should be (Ui 4294967295)) */
  printf("%d\n", i);
  printf("%d\n",0xFFFFFFFF>>31); /* -1 (should be 1) */
  return 0;
}

