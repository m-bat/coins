/* Can call function in a function? */

int printf(char *s, ...);

int add(int a, int b) {
	return a + b;
}

int main() {
	int a, b;
	a = 32;
	b = add(a, add(10, 9));
	printf("b = %d\n", b);
	return 0;
}