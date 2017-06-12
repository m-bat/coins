/* operation of some non-immediate values on SPARC8 */
int printf(char *s, ...);

int main() {
	int a, b, c, d, e;
	a = 3284;
	b = 38375 + a - 473738;
	c = b * 0 + (a / 11111) * 22222 - 5555;
	printf("c = %d\n", c);
	return 0;
}