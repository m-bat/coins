/* if statement */
int printf(char *s, ...);

int main() {
	int a, b;
	b = 0;
	a = 10;
	if (0 < a) {
		b++;
	}
	printf("b = %d\n", b);
	return 0;
}