int printf(char *s, ...);

static int a;

int main() {
	int b;
	a = 110;
	b = a * 14;
	printf("b = %d\n", b);
	return 0;
}