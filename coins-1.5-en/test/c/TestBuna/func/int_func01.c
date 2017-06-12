/* function call test(int) */

int printf(char *s, ...);

int func(int a, int b, int c) {
	int r;
	r = a + b + c;
	return r;
}

int main() {
	int a, b, result;
	a = 1;
	b = -2;
	result = func(a, b, 9);
	printf("result = %d\n", result);
	return 0;
}
