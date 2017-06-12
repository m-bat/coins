/* basic addition */

int printf(char *s, ...);

int main() {
	int a, b, c, d;
	a = 43;
	b = a + 12;
	printf("b = %d\n", b);
	c = b + a + 32;
	d = 700 + a;
	printf("d = %d\n", d);
	return 0;
}