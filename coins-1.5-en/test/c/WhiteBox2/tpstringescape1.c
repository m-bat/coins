/* tpstringescape1.c  escape test */
/*   Mori mail 040307 coins-bug 2004/7 */

int printf(char*, ...);

int main()
{
  char *str, *str2, *str3;
  str = "\\0";
  str2 = "\\\\\\abcd";
  str3 = "xyz\"\tt\ff\nn\0\0";
  printf("str %sEND\n", str);
  printf("str2 %sEND\n", str2);
  printf("str3 %sEND\n", str3);
}
