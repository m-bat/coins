/* Return value test(int) */

int printf(char *s, ...);

int func() {
	int ret;
	ret = 384;
	return -38 * -ret;
}

int main() {
	int a;
	a = func();
	printf("a = %d\n", a);
	return 0;
}