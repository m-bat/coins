/* Return value test(double) */

int printf(char *s, ...);

char func() {
	char ret;
	ret = 11;
	return 11;
}

int main() {
	char a;
	a = func();
	printf("a = %d\n", a);
	return 0;
}