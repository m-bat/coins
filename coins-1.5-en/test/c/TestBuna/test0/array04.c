int printf(char *s, ...);

int main() {
	int a[5], b;
	a[3] = 30;
	b = a[3];
	
	printf("b = %d\n", b);
	return 0;
}