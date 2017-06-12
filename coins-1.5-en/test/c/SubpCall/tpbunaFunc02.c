int printf(char *str, ...);
int max(int a, int b);

int main() {
	int a, b, c;
	a = 32;
	b = max(a,max(10, 33));
	printf("b = %d\n", b);
	return 0;
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