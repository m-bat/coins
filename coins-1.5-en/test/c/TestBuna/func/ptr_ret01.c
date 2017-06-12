/* Return value test(pointer) */

int printf(char *s, ...);

int *get_addr(int val) {
	static int *p;
	p = &val;
	return p;
}

int main() {
	int a;
	int *a_p;
	a = 8888;
	a_p = get_addr(a);
	printf("a_p = %d\n", (int)a);
	return 0;
}