/* Can call function in condition? */

int printf(char *s, ...);
int increment(int a);

int main() {
	int a, b, c;
	a = 0;
	if (increment(a) == 0) {
		b = 0;
	} else {
		b = 1;
	}
	printf("b = %d\n", b);
	return 0;
}

int increment(int a) {
	int b;
	b = a;
	return ++b;
}
