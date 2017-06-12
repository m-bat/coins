int printf(char *s, ...);

int main() {
	int a, b, c, d, i;
	i = 1;
	a = 2;
	b = 3;
	while (i < 100) {
	 c = a + b;
	 i = i + 1;
	}
	printf("c = %d, i = %d\n", c, i);
	return 0;
}