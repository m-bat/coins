/* Can pointer be an argument? */

int printf(char *s, ...);

int *get_addr(int *arg) {
	int *p;
	p = arg;
	return p;
}

int main() {
	int a, *ap;
	a = 83;
	ap = get_addr(&a);
	printf("*ap = %d\n", *ap);
	return 0;
}