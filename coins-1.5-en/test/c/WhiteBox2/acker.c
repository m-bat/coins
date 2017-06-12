/* acker.c  Ackermann function */
/*  The Ackermann function is defined as
    A(0,y) = y + 1
    A(x+1,0) = A(x,1)
    A(x+1,Y+1) = A(x,A(x+1,y))
*/
#define TEST 1

#if TEST
	int count = 0;
#endif

int A(int x, int y)
{
	#if TEST
		count++;
	#endif
	if (x == 0) return y + 1;
	if (y == 0) return A(x - 1, 1);
	return A(x - 1, A(x, y - 1));
}

#include <stdio.h>
#include <stdlib.h>

int main()
{
	printf("A(3, 11) = %d\n", A(3, 11));
	#if TEST
		printf("A(x, y) was called %d times.\n", count);
	#endif
	return EXIT_SUCCESS;
}
