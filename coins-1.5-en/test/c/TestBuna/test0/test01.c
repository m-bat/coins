int printf(char *s, ...);

int main() {
	int a, b, c, d, e;
	a = 1;
	b = -11;
	c = 21;
	d = -31;
	e = 0;
	if (0 < a) {
		printf("0 < a\n");
	} else {
		e--;
	}
	e += 1000;
	if (0 <= b) {
		printf("0 <= b\n");
	} else {
		e--;
	}
	return 0;
}