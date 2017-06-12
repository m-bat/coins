/* operation of including a non-immediate value on SPARC8 */
int printf(char *s, ...);

int main() {
	int a, b, c, d, e, f, g, h, i;
	a = 32;
	b = a + 58433;
	c = b + a + 3 + 4;
	d = a * 2 - (6000 / a) + 333;
	e = c - d;
	f = c / d * a + 23 + b - e + d * 43 / 325 + 328 - (7859 * a);
	g = f;
	printf("g = %d\n", g);
	return 0;
}