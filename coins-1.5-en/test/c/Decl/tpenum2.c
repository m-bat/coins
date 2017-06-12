/* tpenum2.c  Enumeration literal declaration (Decl) */

enum months { Jan = 1, Feb, Mar, Apr, May, Jun, Jul, Aug, 
              Sep, Oct, Nov, Dec };

  enum months m;

int main()
{
  int i, j, k;

  m  = Nov;
  printf("enum month m=%d\n",m); /* SF030620 */
  return 0;
}

