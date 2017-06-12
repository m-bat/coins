/* tphir2cmugi1.c HIR2C label ref count bug */
/*    Fujise mail 030715 */

main()
{
  int c = '1';
  int ndigit[10] = {0};
  int nother;
  if (c >= '0' && c <= '9')
    ++ndigit[c-'0'];
  else
    ++nother;
  printf("%d \n", ndigit['1'-'0']);
}

