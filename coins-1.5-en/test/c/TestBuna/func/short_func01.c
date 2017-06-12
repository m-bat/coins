/* function call test(short) */

int printf(char *s, ...);

int func(short a, short b) {
	int r;
	r = a / b;
	return r;
}

int main() {
	short a, b, result;
	a = 1;
	b = 33333;
	result = func(a, b);
	printf("result = %d\n", result);
	return 0;
}
