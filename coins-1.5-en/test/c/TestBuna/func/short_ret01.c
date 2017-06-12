/* Return value test(short) */

int printf(char *s, ...);

short func() {
	short ret;
	ret = -99;
	return ret;
}

int main() {
	unsigned short a;
	a = func();
	printf("a = %d\n", a);
	return 0;
}