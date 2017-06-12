/* tpkana1.c  Kana ID (SyntaxError) */
int ‚¢, ‚ë;
main()
{
  int a, b;
  ‚¢ = 1;
  ‚ë = ‚¢ + 2;
  printf("This test will cause 'parse' error.\n"); /* SF030609 */
  return ‚ë;
}

