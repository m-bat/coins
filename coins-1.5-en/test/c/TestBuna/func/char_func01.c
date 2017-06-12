/* function call test(char) */

int printf(char *s, ...);

int func(char a, char b) {
	int r;
	return a * b + 66;
}

int main() {
	signed char a, b, result;
	a = 3;
	b = 33;
	result = func(a, b);
	printf("result = %d\n", result);
	return 0;
}
