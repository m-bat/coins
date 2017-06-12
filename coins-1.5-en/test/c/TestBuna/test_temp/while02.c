int printf(char *s, ...);

int a, b, c, i;

int main() {
	a = 1;
	b = 1;
	while (i < 100) {
		a = a + b;
		if (a == 0) {
			i = i + 1;
			continue;
		}
		i = i + 1;
	}
	a = b + c;
	printf("i = %d, a = %d\n", i, a);
	return 0;
}