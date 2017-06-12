/* Return value test(array) */

int printf(char *s, ...);

static int array[10];

int *setup() {
	int i;
	for (i = 0; i < 10; i++) {
		array[i] = i + 1;
	}
	return array;
}

int main() {
	int *a;
	a = setup();
	printf("a[0] = %d\n", a[0]);
	return 0;
}