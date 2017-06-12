/* basic addition */

int printf(char *s, ...);

int main() {
	int a, b, c;
	a = 43;
	b = a + 12;
	printf("b = %d\n", b);
	c = b + a + 32;
	printf("c = %d\n", c);
	return 0;
}