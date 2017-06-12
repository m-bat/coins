/*for_nest_goto_01 for statement */
int printf(char *s, ...); /* int printf(char *s, int a); SF030514 */
int Calc(long a, long b, long e, long g,
			long l, long n, long o, long p, long r);
long banana, apple, orange;

int main()
{
	long a, b, e, g, l, n, o, p, r;

	for (a = 1; a <= 9; a++)	/* a!=0 */
          for (b = 1; b <= 9; b++)	/* b!=0 */
	    for (e = 0; e <= 9; e++)
	      for (g = 0; g <= 9; g++)
		for (l = 0; l <= 9; l++)
	          for (n = 0; n <= 9; n++)
		    for (o = b + 1; o <= 9; o++)  /* o > b+1 */
		      for (p = 0; p <= 9; p++)
			for (r = 0; r <= 9; r++)
		          if (Calc(a, b, e, g, l, n, o, p, r))
			      goto result;
	                  else
		              ;
	/* jump here */
	result:	printf("banana:%ld\n", banana);
	printf("+apple: %ld\n" , apple);
	printf("______  _____\n");
	printf("orange:%ld\n", orange);
        return 0;
}
int Calc(long a, long b, long e, long g,
			long l, long n, long o, long p, long r)
{
	banana = b * 100000 + a * 10000 + n * 1000 + a * 100 + n * 10 + a;
	apple =  a * 10000 + p * 1000 + p * 100 + l * 10 + e;
	orange = o * 100000 + r * 10000 + a * 1000 + n * 100 + g * 10 + e;

	if (orange = banana + apple)
		return (1);
	else
		return (0);
}