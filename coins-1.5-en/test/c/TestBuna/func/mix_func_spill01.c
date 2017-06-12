/* Argument spill test(int, double) */
int printf(char *s, ...);

int func(
	int a, double b, double c, double d
) {
	int r;
	r = a + b + c + d;
	return r;
}

int main() {
	int a;
	double b, c;
	int result;
	a = 9999;
	b = 32.3;
	c = 99.99;
	result = func(a, b, c, 11.11);
	printf("result = %d\n",result); /* SF030514 */
	return 0;
}