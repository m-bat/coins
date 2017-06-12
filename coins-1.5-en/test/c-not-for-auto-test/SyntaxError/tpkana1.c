/* tpkana1.c  Kana ID (SyntaxError) */
int い, ろ;
main()
{
  int a, b;
  い = 1;
  ろ = い + 2;
  printf("This test will cause 'parse' error.\n"); /* SF030609 */
  return ろ;
}

