#include <stdio.h>

int bitcount(unsigned x)
{
	int b;
	for (b = 0; x != 0; x >>= 1)
		if (x & 01)
			b++;
	return b;
}

main()
{
	printf("3:%d ", bitcount(3));
	printf("63:%d ", bitcount(63));
	printf("100:%d ", bitcount(100));
	printf("0xABCD:%d\n", bitcount(0xABCD));
}
