int printf(char *s, ...);
int max(int a, int b);

int main() {
	int a, b, c;
	a = 32;
	b = max(a, 31);
	printf("b = %d\n", b);
}

int max(int a, int b) {
	int c;
	if (a > b) {
		c = a;
	} else {
		c = b;
	}
	return c;
}