/* operation on condition */
int printf(char *s, ...);

int main() {
	int a, b, c;
	a = 10;
	b = 1;
	c = 0;
	if (a + 3 < 0) {
		b++;
	} else if (++a > 34 + b) {
		b++;
	} else {
		b--;
	}
	if (b != 0) {
		c++;
	} else {
		c--;
	}
	printf("b = %d, c = %d\n", b, c);
	return 0;
}