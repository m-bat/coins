/* function call test(struct, int, short) */

int printf(char *s, ...);

struct data {
	int m1;
};

int func(struct data a, int b, short c) {
	int r;
	r = a.m1 * b + c;
	return r;
}

int main() {
	int result;
	struct data a;
	int b;
	short c;
	a.m1 = 583;
	b = 99;
	c = 2;
	result = func(a, b, c);
	printf("result = %d\n", result);
	return 0;
}
