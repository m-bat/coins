/* function call test(union) */

int printf(char *s, ...);

union data {
	double num1;
	char ch;
};

char func(union data data1, union data data2) {
	char r;
	r = data1.ch + data2.ch;
	return r;
}

int main() {
	union data d1, d2;
	char result;
	d1.num1 = 4888.32;
	d1.ch = 'a';
	d2.num1 = 3.3;
	d2.ch = 1;
	result = func(d1, d2);
	printf("result = %d\n", result);
	return 0;
}