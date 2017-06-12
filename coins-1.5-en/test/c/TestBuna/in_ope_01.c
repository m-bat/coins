/* Can call function in an operator? */

int printf(char *s, ...);

int increment(int a) {
	int ret;
	ret = a;
	return ++ret;
}

int main() {
	int a, b;
	a = 100;
	b = a * increment(99);
	printf("b = %d\n", b);
	return 0;
}