/* tphir2cmugi0.c HIR2C label ref count bug */
/*    Fujise mail 030715 */

main()
{
  int c;
  int ndigit[10];
  int nother;
  if (c >= '0' && c <= '9')
    ++ndigit[c-'0'];
  else
    ++nother;
}

