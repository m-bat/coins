/* Nested function call test(int) */

int a, b;

int mul_int(int *a, int *b);
int internal_mul_int(int *a, int *b);
int printf(char *s, ...);

int a;

int main() {
	int b, result;
	a = 10;
	b = 8888;
	result = mul_int(&b, &a);
	printf("result = %d\n", result);
	return 0;
}

int mul_int(int *a, int *b) {
	return internal_mul_int(a, b);
}

int internal_mul_int(int *a, int *b) {
	return (*a) * (*b);
}
