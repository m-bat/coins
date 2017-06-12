int printf(char *s, ...);

int main() {
	int a, b;
	a = 1;
	b = 0;
	while (b < 10) {
		b++;
	}
	printf("b = %d\n", b);
	return 0;
}