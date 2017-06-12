/* tptypedefScope1.c */
/*   Mori mail 021116 */ 

typedef int number;
number x;
float floatingNumber;

int main()
{
  typedef float floatingNumber;
  double number;
  floatingNumber f1;
  x = 3;
  number = 1.5;
  f1 = 2.0;
  printf(" %d %f %f \n", x, number, f1);
  return 0;
}

