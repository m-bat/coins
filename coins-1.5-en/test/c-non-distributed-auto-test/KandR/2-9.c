#include <stdio.h>

unsigned getbits(unsigned x, int p, int n)
{
	return (x >> (p+1-n)) & ~(~0 << n);
}

main()
{
	printf("%u\n", getbits(0xABCD,11,4));
}
