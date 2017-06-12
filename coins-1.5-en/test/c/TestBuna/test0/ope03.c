/* combined operations */
int printf(char *s, ...);

int main() {
	int a, b, c;
	a = 10;
	b = 382;
	c = (a - 658) / 38 * b + 3;
	printf("c = %d\n", c);
	return 0;
}