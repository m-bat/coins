/* tpkana1.c  Kana ID (SyntaxError) */
int ��, ��;
main()
{
  int a, b;
  �� = 1;
  �� = �� + 2;
  printf("This test will cause 'parse' error.\n"); /* SF030609 */
  return ��;
}

