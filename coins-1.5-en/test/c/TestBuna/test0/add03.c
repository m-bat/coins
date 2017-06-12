/* basic addition */

int printf(char *s, ...);

int main() {
	int a, b, c, d;
	a = 10;
	b = a + 20;
	printf("b = %d\n", b);
	c = b + 30;
	printf("c = %d\n", c);
	d = c + 40;
	printf("d = %d\n", d);
	return 0;
}