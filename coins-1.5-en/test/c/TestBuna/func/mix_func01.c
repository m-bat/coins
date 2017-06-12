/* function call test(int, double, char) */

int printf(char *s, ...);

double func(int a, double b, char c) {
	double r;
	r = a + b + c;
	return r;
}

int main() {
	double result;
	double a;
	int b;
	char c;
	a = 6666.66666;
	b = 32;
	c = 'a';
	result = func(a, b, c);
	printf("result = %f\n", result);
	return 0;
}
