int printf(char *s, ...);

int main() {
	int a, b;
	int *a_ptr;
	a = 778;
	a_ptr = &a;
	b = *a_ptr;
	printf("a = %d, b = %d\n", a, b);
	return 0;
}