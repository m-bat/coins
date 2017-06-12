#include <stdio.h>

int binsearch(int x, int v[], int n)
{
	int low, high, mid;
	low = 0;
	high = n - 1;
	while (low <= high) {
		mid = (low+high) / 2;
		if (x < v[mid])
			high = mid - 1;
			else if (x > v[mid])
			low = mid + 1;
			else
				return mid;
	}
	return -1;
}

#define SIZE (sizeof(data)/sizeof(int))

main()
{
	int data[] = {0,1,2,3,4,6,7,8,9};
	printf("%d %d\n", binsearch(3,data,SIZE), binsearch(5,data,SIZE));
}
