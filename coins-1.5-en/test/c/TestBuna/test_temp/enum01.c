int printf(char *s, ...);

enum months { Jan = 1, Feb, Mar, Apr, May, Jun, Jul, Aug,
              Sep, Oct, Nov, Dec };

enum months m;

int main()
{ 
  int i, j, k; 

  m  = Nov; 
  printf("m = %d\n", m);
  return 0; 
} 