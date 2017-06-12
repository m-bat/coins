int printf(char *s, ...);

int main() {
	int a[10];
	int i;
	a[0] = 0;
	a[1] = 10;
	a[2] = 20;
	
	for (i = 0; i < 3; i++) {
		printf("a[%d] = %d\n", i, a[i]);
	}
	return 0;
}