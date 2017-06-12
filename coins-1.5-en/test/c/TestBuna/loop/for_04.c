/* for_04 simple for statement */
                      /* array */
int printf(char *s, ...);

int main() {
	int i;
	int a[3];
	for (i = 0; i < 3; i++) 
		a[i] = 10*i;
	printf("a[0] = %d\n", a[0]);
	printf("a[1] = %d\n", a[1]);
	printf("a[2] = %d\n", a[2]);
	return 0;
}