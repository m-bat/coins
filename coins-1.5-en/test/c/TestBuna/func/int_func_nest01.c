/* Nested function call(int) */
int printf(char *s, ...);
int add_int(int a, int b);
int internal_add_int(int a, int b);

int add_int(int a, int b)
{
	return internal_add_int(a, b);
}

int internal_add_int(int a, int b)
{
	int r;
	r = a + b;
	return r;
}

int main(int arg) {
	int a, b, c;
	a = 7;
	b = add_int(a, 23);
	printf("b = %d\n", b);
	return 0;
}