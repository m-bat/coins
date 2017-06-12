/* tpenum3.c  Enumeration literal declaration (Decl) with constant exp
              (Fujise 020809) 
#include <ctype.h> may introduce enum with constant expression.
*/


int main()
{
  enum {
     _lSupper = ((0)<8?((1<<(0))<<8):((1<<(0))>>8)), /* bad constant value 11/26*/
     _lSlower = ((1)<8?((1<<(1))<<8):((1<<(1))>>8))
  };

  int a, b;
  a = _lSupper;
  b = _lSlower;
  printf("%d %d \n", a, b);
  return 0;
}

