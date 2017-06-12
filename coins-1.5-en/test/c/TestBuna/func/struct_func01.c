/* function call test(sturct) */

int printf(char *s, ...);

struct data {
	double num1;
	char ch;
};

char func(struct data data1, struct data data2) {
	char r;
	r = data1.ch + data2.ch;
	return r;
}

int main() {
	struct data d1, d2;
	char result;
	d1.num1 = 4888.32;
	d1.ch = 'a';
	d2.num1 = 3.3;
	d2.ch = 1;
	result = func(d1, d2);
	printf("result = %d\n", result);
	return 0;
}