/* tpenum1.c  Enumeration literal declaration */

enum months { Jan = 1, Feb, Mar, Apr, May, Jun, Jul, Aug,
              Sep, Oct, Nov, Dec };

  enum months m;

int main()
{
  int i, j, k;

  m  = Nov;
  printf("m = %d\n",m); /* SF030514 */
  return 0;
}

