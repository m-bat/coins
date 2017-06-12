/* Argument spill test(int) */
int printf(char *s, ...);

int calc(
	int a, int b, int c, int d, int e, int f,
	int g, int h
) {
	int res;
	res = a * b / c + d - e - f + g * h;
	printf("res = %d\n", res);
	return res;
}

int main() {
	int a, b, c, d, e, f, g, h;
	int result;
	a = 11;
	b = 22;
	c = 33;
	d = 44;
	e = 55;
	f = 66;
	g = 77;
	h = 88;
	result = calc(a, b, c, d, e, f, g, h);
	printf("result = %d\n", result);
	return result;
}