int printf(char *s, ...);
int add_int(int a, int b);
int internal_add_int(int a, int b);
int ret(int a);

int add_int(int a, int b)
{
	return internal_add_int(a, b);
}

int internal_add_int(int a, int b)
{
	int r;
	r = a + b;
	return ret(r);
}

int ret(int a)
{
	printf("#function# ret\n");
	return a;
}

int main(int arg)
{
	int a, b, c;
	a = 7;
	b = add_int(a, 23);
	printf("b = %d\n", b);
	return 0;
}