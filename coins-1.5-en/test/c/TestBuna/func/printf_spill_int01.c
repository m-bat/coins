/* Check argument spill by int */

int printf(char *s, ...);

int main() {
	int a, num;
	a = 1;
	num = printf(
		"%d, %d, %d, %d, %d, %d, %d, %d, %d\n",
		1000000, 100000, 10000, 1000, 100, 10, 1, a, a-1
	);
	printf("num = %d\n", num);
	return 0;
}